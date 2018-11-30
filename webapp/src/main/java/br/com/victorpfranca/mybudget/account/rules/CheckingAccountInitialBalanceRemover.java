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
import br.com.victorpfranca.mybudget.transaction.Transaction;

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

		Transaction transaction = removerLancamento();

		if (transaction != null)
			accountBalanceUpdater.removeSaldos(transaction);
	}

	private Transaction removerLancamento() {
		List<Transaction> lancamentosSaldosIniciais = em
				.createNamedQuery(Transaction.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, Transaction.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("conta", account)
				.setParameter("categoria", null).setParameter("saldoInicial", true).setParameter("ano", null)
				.setParameter("mes", null).setParameter("cartaoCreditoFatura", null).setParameter("faturaCartao", null)
				.setParameter("status", null).getResultList();

		Transaction transaction = null;
		if (!lancamentosSaldosIniciais.isEmpty()) {
			transaction = lancamentosSaldosIniciais.get(0);
			em.remove(transaction);
		}

		return transaction;
	}

}
