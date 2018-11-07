package br.com.victorpfranca.mybudget.accesscontroll.passwordrecovery;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.accesscontroll.User_;
import br.com.victorpfranca.mybudget.infra.date.DateUtils;
import br.com.victorpfranca.mybudget.infra.date.api.CurrentDateSupplier;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PasswordRecoveryQueries {

	@Inject
	private EntityManager entityManager;

	@EJB
	private CurrentDateSupplier dateUtils;

	public boolean confirmaValidadeDoCodigo(String email, String codigo) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> critQuery = cb.createQuery(Long.class);

		Root<PasswordRecovery> recSenha = critQuery.from(PasswordRecovery.class);
		Join<?, User> user = recSenha.join(PasswordRecovery_.alvo, JoinType.INNER);

		Predicate usuarioIgual = cb.equal(user.get(User_.email), StringUtils.lowerCase(email));
		Predicate codigoIgual = cb.equal(recSenha.get(PasswordRecovery_.codigo), codigo);
		Predicate recuperacaoAtiva = cb.isTrue(recSenha.get(PasswordRecovery_.ativo));
		Predicate dataSolicitacaoValida = filtroDeDataValida(cb, recSenha, periodoValidoParaRecuperacaoDeSenha());

		critQuery.select(cb.count(recSenha.get(PasswordRecovery_.id))).where(usuarioIgual, codigoIgual,
				recuperacaoAtiva, dataSolicitacaoValida);

		return entityManager.createQuery(critQuery).getSingleResult() > 0;
	}

	public Pair<Date, Date> periodoValidoParaRecuperacaoDeSenha() {
		LocalDateTime dataFim = dateUtils.currentLocalDateTime();
		Date inicioPeriodoValidoSolicitacao = DateUtils.localDateTimeToDate(dataFim.minusMinutes(45));
		Date fimPeriodoValidoSolicitacao = DateUtils.localDateTimeToDate(dataFim);
		return new ImmutablePair<>(inicioPeriodoValidoSolicitacao, fimPeriodoValidoSolicitacao);
	}

	public PasswordRecovery ultimaRecuperacaoValidaPara(User user) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PasswordRecovery> critQuery = cb.createQuery(PasswordRecovery.class);

		Root<PasswordRecovery> recSenha = critQuery.from(PasswordRecovery.class);

		Predicate usuarioIgual = cb.equal(recSenha.get(PasswordRecovery_.alvo), user);
		Predicate recuperacaoAtiva = cb.isTrue(recSenha.get(PasswordRecovery_.ativo));
		Predicate dataSolicitacaoValida = filtroDeDataValida(cb, recSenha, periodoValidoParaRecuperacaoDeSenha());

		critQuery.select(recSenha).where(usuarioIgual, recuperacaoAtiva, dataSolicitacaoValida);

		return entityManager.createQuery(critQuery).getSingleResult();
	}

	public PasswordRecovery ultimaRecuperacaoValidaPara(String email, String codigo) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PasswordRecovery> critQuery = cb.createQuery(PasswordRecovery.class);

		Root<PasswordRecovery> recSenha = critQuery.from(PasswordRecovery.class);
		Join<?, User> user = recSenha.join(PasswordRecovery_.alvo, JoinType.INNER);

		Predicate usuarioIgual = cb.equal(user.get(User_.email), StringUtils.lowerCase(email));
		Predicate codigoIgual = cb.equal(recSenha.get(PasswordRecovery_.codigo), codigo);
		Predicate recuperacaoAtiva = cb.isTrue(recSenha.get(PasswordRecovery_.ativo));
		Predicate dataSolicitacaoValida = filtroDeDataValida(cb, recSenha, periodoValidoParaRecuperacaoDeSenha());

		critQuery.select(recSenha).where(usuarioIgual, codigoIgual, recuperacaoAtiva, dataSolicitacaoValida);
		try {
			return entityManager.createQuery(critQuery).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<PasswordRecovery> listarAtivasComDataExpirada() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PasswordRecovery> critQuery = cb.createQuery(PasswordRecovery.class);

		Root<PasswordRecovery> recSenha = critQuery.from(PasswordRecovery.class);
		Predicate recuperacaoAtiva = cb.isTrue(recSenha.get(PasswordRecovery_.ativo));

		Predicate dataSolicitacaoInvalida = cb
				.not(filtroDeDataValida(cb, recSenha, periodoValidoParaRecuperacaoDeSenha()));
		critQuery.select(recSenha).where(recuperacaoAtiva, dataSolicitacaoInvalida);

		return entityManager.createQuery(critQuery).getResultList();
	}

	public List<PasswordRecovery> listarAtivasDeUsuario(User user) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<PasswordRecovery> critQuery = cb.createQuery(PasswordRecovery.class);

		Root<PasswordRecovery> recSenha = critQuery.from(PasswordRecovery.class);

		Predicate usuarioIgual = cb.equal(recSenha.get(PasswordRecovery_.alvo), user);
		Predicate recuperacaoAtiva = cb.isTrue(recSenha.get(PasswordRecovery_.ativo));
		Predicate dataSolicitacaoValida = filtroDeDataValida(cb, recSenha, periodoValidoParaRecuperacaoDeSenha());
		critQuery.select(recSenha).where(usuarioIgual, recuperacaoAtiva, dataSolicitacaoValida);
		return entityManager.createQuery(critQuery).getResultList();
	}

	private Predicate filtroDeDataValida(CriteriaBuilder cb, Root<PasswordRecovery> recSenha,
			Pair<Date, Date> periodoValido) {
		Predicate dataSolicitacaoValida = cb.between(recSenha.get(PasswordRecovery_.dataSolicitacao),
				periodoValido.getLeft(), periodoValido.getRight());
		return dataSolicitacaoValida;
	}

}
