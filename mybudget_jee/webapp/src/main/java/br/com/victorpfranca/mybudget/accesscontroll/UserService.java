package br.com.victorpfranca.mybudget.accesscontroll;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import br.com.victorpfranca.mybudget.account.InitialAccountsBuilder;
import br.com.victorpfranca.mybudget.budget.InitialBudgetsCreator;
import br.com.victorpfranca.mybudget.category.InitialCategoriesBuilder;
import br.com.victorpfranca.mybudget.category.SameNameException;
import br.com.victorpfranca.mybudget.infra.date.api.CurrentDateSupplier;
import br.com.victorpfranca.mybudget.infra.exception.SystemException;
import br.com.victorpfranca.mybudget.infra.mail.MailSender;
import br.com.victorpfranca.mybudget.infra.security.CryptoPasswordService;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;
import br.com.victorpfranca.mybudget.view.Messages;
import br.com.victorpfranca.mybudget.view.validation.PasswordConstraintValidator;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UserService {

	@Inject
	private EntityManager entityManager;

	@Inject
	private CryptoPasswordService cryptoPasswordService;
	@EJB
	private CurrentDateSupplier dateUtils;
	@Inject
	private MailSender mailSender;
	
	@EJB
	private InitialCategoriesBuilder initialCategoriesBuilder;
	
	@EJB
	private InitialAccountsBuilder initialAccountsBuilder;
	
	@EJB
	private InitialBudgetsCreator initialBudgetsCreator;

	public boolean existeUsuarioComEmail(String email) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<User> user = cq.from(User.class);
		Expression<Long> countDistinct = cb.countDistinct(user.get(User_.id));
		cq = cq.select(countDistinct).where(cb.equal(user.get(User_.email), StringUtils.lowerCase(email)));

		return entityManager.createQuery(cq).getSingleResult() > 0;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void create(User user) {
		if (existeUsuarioComEmail(user.getEmail())) {
			throw new SystemException(UserSingupErrorCodes.EMAIL_JA_CADASTRADO);
		}
		PasswordConstraintValidator.validate(user.getSenha());

		user.setSenha(cryptoPasswordService.encryptPassword(user.getSenha()));
		user.setDataCadastro(dateUtils.currentDate());
		user.setQuantidadeAcessos(BigDecimal.ZERO);
		user.setAtivo(Boolean.TRUE);
		entityManager.persist(user);
		
		mailSender.sendMail(user.getEmail(), Messages.msg("criarUsuario.mail.title"),
				processarComMustache(Messages.msg("criarUsuario.mail.body"), user));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void completarCadastro(Integer id, String firstName) throws SameNameException,
			br.com.victorpfranca.mybudget.account.rules.SameNameException, NullableAccountException, TransactionMonthUpdatedException, AccountTypeException, IncompatibleCategoriesException, InvalidTransactionValueException {
		User user = entityManager.find(User.class, id);
		user.setFirstName(firstName);
		user.setPreCadastro(false);
		entityManager.merge(user);
		
		preencherCadastrosIniciais();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void preencherCadastrosIniciais() throws SameNameException,
			br.com.victorpfranca.mybudget.account.rules.SameNameException, NullableAccountException,
			TransactionMonthUpdatedException, AccountTypeException, IncompatibleCategoriesException,
			InvalidTransactionValueException {
		initialCategoriesBuilder.execute();
		initialAccountsBuilder.execute();
		initialBudgetsCreator.execute();
	}

	private String processarComMustache(String text, Object context) {
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile(new StringReader(text), "input");
		StringWriter stringWriter = new StringWriter();
		mustache.execute(stringWriter, context);
		return stringWriter.toString();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User updatePassword(Integer id, String newPassword) {
		PasswordConstraintValidator.validate(newPassword);

		User user = entityManager.find(User.class, id);
		user.setSenha(cryptoPasswordService.encryptPassword(newPassword));
		return entityManager.merge(user);
	}

	public Long getUsuarioCount() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<User> user = cq.from(User.class);
		Expression<Long> countDistinct = cb.countDistinct(user.get(User_.id));
		cq = cq.select(countDistinct);
		return entityManager.createQuery(cq).getSingleResult();
	}

	public User recuperarViaEmail(String email) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> _user = cq.from(User.class);

		cq = cq.select(_user).where(cb.equal(_user.get(User_.email), StringUtils.lowerCase(email)));
		return entityManager.createQuery(cq).getSingleResult();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void ativar(Integer id) {
		User user = entityManager.find(User.class, id);
		user.setAtivo(true);
		entityManager.merge(user);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void inativar(Integer id) {
		User user = entityManager.find(User.class, id);
		user.setAtivo(false);
		entityManager.merge(user);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Asynchronous
	public void incrementarContadorAcesso(User user) {
		user.setDataUltimoAcesso(dateUtils.currentDate());
		user.addContadorAcesso();
		entityManager.merge(user);
	}
}
