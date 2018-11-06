package br.com.gestao.financeira.pessoal.controleacesso;

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

import br.com.gestao.financeira.pessoal.categoria.CriadorCategoriasIniciais;
import br.com.gestao.financeira.pessoal.categoria.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.conta.CriadorContasIniciais;
import br.com.gestao.financeira.pessoal.infra.date.api.CurrentDateSupplier;
import br.com.gestao.financeira.pessoal.infra.exception.SystemException;
import br.com.gestao.financeira.pessoal.infra.mail.MailSender;
import br.com.gestao.financeira.pessoal.infra.security.CryptoPasswordService;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;
import br.com.gestao.financeira.pessoal.orcamento.CriadorOrcamentosIniciais;
import br.com.gestao.financeira.pessoal.view.Messages;
import br.com.gestao.financeira.pessoal.view.validation.PasswordConstraintValidator;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UsuarioService {

	@Inject
	private EntityManager entityManager;

	@Inject
	private CryptoPasswordService cryptoPasswordService;
	@EJB
	private CurrentDateSupplier dateUtils;
	@Inject
	private MailSender mailSender;
	
	@EJB
	private CriadorCategoriasIniciais criadorCategoriasIniciais;
	
	@EJB
	private CriadorContasIniciais criadorContasIniciais;
	
	@EJB
	private CriadorOrcamentosIniciais criadorOrcamentosIniciais;

	public boolean existeUsuarioComEmail(String email) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Usuario> user = cq.from(Usuario.class);
		Expression<Long> countDistinct = cb.countDistinct(user.get(Usuario_.id));
		cq = cq.select(countDistinct).where(cb.equal(user.get(Usuario_.email), StringUtils.lowerCase(email)));

		return entityManager.createQuery(cq).getSingleResult() > 0;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void create(Usuario usuario) {
		if (existeUsuarioComEmail(usuario.getEmail())) {
			throw new SystemException(CadastroUsuarioErrorCodes.EMAIL_JA_CADASTRADO);
		}
		PasswordConstraintValidator.validate(usuario.getSenha());

		usuario.setSenha(cryptoPasswordService.encryptPassword(usuario.getSenha()));
		usuario.setDataCadastro(dateUtils.currentDate());
		usuario.setQuantidadeAcessos(BigDecimal.ZERO);
		usuario.setAtivo(Boolean.TRUE);
		entityManager.persist(usuario);
		
		mailSender.sendMail(usuario.getEmail(), Messages.msg("criarUsuario.mail.title"),
				processarComMustache(Messages.msg("criarUsuario.mail.body"), usuario));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void completarCadastro(Integer id, String firstName) throws MesmoNomeExistenteException,
			br.com.gestao.financeira.pessoal.conta.rules.MesmoNomeExistenteException, ContaNotNullException, MesLancamentoAlteradoException, TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		Usuario usuario = entityManager.find(Usuario.class, id);
		usuario.setFirstName(firstName);
		usuario.setPreCadastro(false);
		entityManager.merge(usuario);
		
		preencherCadastrosIniciais();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void preencherCadastrosIniciais() throws MesmoNomeExistenteException,
			br.com.gestao.financeira.pessoal.conta.rules.MesmoNomeExistenteException, ContaNotNullException,
			MesLancamentoAlteradoException, TipoContaException, CategoriasIncompativeisException,
			ValorLancamentoInvalidoException {
		criadorCategoriasIniciais.execute();
		criadorContasIniciais.execute();
		criadorOrcamentosIniciais.execute();
	}

	private String processarComMustache(String text, Object context) {
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile(new StringReader(text), "input");
		StringWriter stringWriter = new StringWriter();
		mustache.execute(stringWriter, context);
		return stringWriter.toString();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Usuario updatePassword(Integer id, String newPassword) {
		PasswordConstraintValidator.validate(newPassword);

		Usuario usuario = entityManager.find(Usuario.class, id);
		usuario.setSenha(cryptoPasswordService.encryptPassword(newPassword));
		return entityManager.merge(usuario);
	}

	public Long getUsuarioCount() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Usuario> user = cq.from(Usuario.class);
		Expression<Long> countDistinct = cb.countDistinct(user.get(Usuario_.id));
		cq = cq.select(countDistinct);
		return entityManager.createQuery(cq).getSingleResult();
	}

	public Usuario recuperarViaEmail(String email) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
		Root<Usuario> _user = cq.from(Usuario.class);

		cq = cq.select(_user).where(cb.equal(_user.get(Usuario_.email), StringUtils.lowerCase(email)));
		return entityManager.createQuery(cq).getSingleResult();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void ativar(Integer id) {
		Usuario usuario = entityManager.find(Usuario.class, id);
		usuario.setAtivo(true);
		entityManager.merge(usuario);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void inativar(Integer id) {
		Usuario usuario = entityManager.find(Usuario.class, id);
		usuario.setAtivo(false);
		entityManager.merge(usuario);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Asynchronous
	public void incrementarContadorAcesso(Usuario usuario) {
		usuario.setDataUltimoAcesso(dateUtils.currentDate());
		usuario.addContadorAcesso();
		entityManager.merge(usuario);
	}
}
