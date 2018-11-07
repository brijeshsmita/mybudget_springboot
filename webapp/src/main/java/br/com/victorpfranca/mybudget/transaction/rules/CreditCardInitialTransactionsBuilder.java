package br.com.victorpfranca.mybudget.transaction.rules;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;

@Stateless
public class CreditCardInitialTransactionsBuilder extends CreditCardTransactionBuilder {

	@Inject
	private EntityManager em;
	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@Override
	protected Date getDataPrimeiraFatura(CreditCardTransaction lancamento) {
		return lancamento.getData();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	protected List<Transaction> carregarFaturas(CreditCardTransaction lancamento, Date dataPrimeiraFatura)
			throws NullableAccountException {
		List<Transaction> faturasExistentes = em
				.createNamedQuery(Transaction.FIND_LANCAMENTO_FATURA_QUERY, Transaction.class)
				.setParameter("cartaoCreditoFatura", (CreditCardAccount) lancamento.getAccount())
				.setParameter("data", dataPrimeiraFatura).getResultList();

		return ((CreditCardAccount) lancamento.getAccount()).carregarFaturasAPartirDe(lancamento, faturasExistentes,
				lancamento.getData());
	}

}
