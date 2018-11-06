package br.com.victorpfranca.mybudget.account.rules;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;

@Stateless
public class CheckingAccountInitialBalanceRemover {

	private Account account;

	@Inject
	private EntityManager em;
	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute(Account account) {
		this.account = account;

		Lancamento lancamento = removerLancamento();

		if (lancamento != null)
			accountBalanceUpdater.removeSaldos(lancamento);
	}

	private Lancamento removerLancamento() {
		List<Lancamento> lancamentosSaldosIniciais = em
				.createNamedQuery(Lancamento.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, Lancamento.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("account", account)
				.setParameter("category", null).setParameter("saldoInicial", true).setParameter("ano", null)
				.setParameter("mes", null).setParameter("cartaoCreditoFatura", null).setParameter("faturaCartao", null)
				.setParameter("status", null).getResultList();

		Lancamento lancamento = null;
		if (!lancamentosSaldosIniciais.isEmpty()) {
			lancamento = lancamentosSaldosIniciais.get(0);
			em.remove(lancamento);
		}

		return lancamento;
	}

}
