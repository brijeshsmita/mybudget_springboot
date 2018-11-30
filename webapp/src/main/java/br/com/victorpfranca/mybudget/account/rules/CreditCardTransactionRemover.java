package br.com.victorpfranca.mybudget.account.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.transaction.Transaction;

@Stateless
public class CreditCardTransactionRemover {

	@Inject
	private EntityManager em;
	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute(Account account) {

		removerLancamentosFaturaItem(account);

		removerLancamentosCartao(account);

		List<Transaction> faturas = removerFaturas(account);
		if (faturas != null)
			accountBalanceUpdater.removeSaldos(faturas, ((CreditCardAccount) account).getAccountPagamentoFatura());
	}

	private void removerLancamentosCartao(Account account) {
		em.createNamedQuery(Transaction.REMOVE_LANCAMENTOS_CARTAO_CREDITO_QUERY).setParameter("conta", account)
				.setParameter("saldoInicial", null).executeUpdate();
	}

	private void removerLancamentosFaturaItem(Account account) {
		em.createNamedQuery(Transaction.REMOVE_LANCAMENTOS_FATURA_CARTAO_ITEM_QUERY).setParameter("conta", account)
				.setParameter("saldoInicial", null).executeUpdate();
	}

	private List<Transaction> removerFaturas(Account account) {
		List<Transaction> lancamentosFaturas = em
				.createNamedQuery(Transaction.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, Transaction.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado())
				.setParameter("cartaoCreditoFatura", account).setParameter("faturaCartao", true)
				.setParameter("saldoInicial", null).setParameter("ano", null).setParameter("mes", null).setParameter("status", null)
				.setParameter("conta", null).setParameter("categoria", null).getResultList();

		for (Iterator<Transaction> iterator = lancamentosFaturas.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction) iterator.next();
			em.remove(transaction);
		}

		return lancamentosFaturas;
	}

}
