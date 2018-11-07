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
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.CreditCardAccountTransactionRemover;
import br.com.victorpfranca.mybudget.transaction.rules.CreditCardInitialTransactionsBuilder;
import br.com.victorpfranca.mybudget.transaction.rules.CreditCardTransactionBuilder;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;

@Stateless
public class CreditCardAccountBuilder {

	@EJB
	private AccountDataValidator accountDataValidator;

	@EJB
	private CreditCardInitialTransactionsRemover creditCardInitialTransactionsRemover;

	@EJB
	private CreditCardAccountTransactionRemover creditCardAccountTransactionRemover;

	@EJB
	private CreditCardTransactionBuilder criadorLancamentoCartao;

	@EJB
	private CreditCardInitialTransactionsBuilder creditCardInitialTransactionsBuilder;

	@EJB
	private CredentialsStore credentialsStore;

	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CreditCardAccount save(CreditCardAccount conta, List<Transaction> transactions)
			throws SameNameException, NullableAccountException, TransactionMonthUpdatedException,
			AccountTypeException, IncompatibleCategoriesException, InvalidTransactionValueException {

		int cartaoDiaFechmentoAnterior = conta.getCartaoDiaFechamentoAnterior() != null
				? conta.getCartaoDiaFechamentoAnterior().intValue()
				: conta.getCartaoDiaFechamento();
		int cartaoDiaPagamentoAnterior = conta.getCartaoDiaPagamentoAnterior() != null
				? conta.getCartaoDiaPagamentoAnterior().intValue()
				: conta.getCartaoDiaPagamento();

		accountDataValidator.validar(conta);

		conta = em.merge(conta);

		creditCardInitialTransactionsRemover.execute(conta);

		creditCardInitialTransactionsBuilder.save(conta, transactions);

		if (!((cartaoDiaFechmentoAnterior == conta.getCartaoDiaFechamento().intValue())
				&& (cartaoDiaPagamentoAnterior == conta.getCartaoDiaPagamento().intValue()))) {
			atualizarLancamentosAnteriores(conta);
		}

		return conta;
	}

	private void atualizarLancamentosAnteriores(CreditCardAccount conta)
			throws NullableAccountException, TransactionMonthUpdatedException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {

		List<CreditCardTransaction> lancamentosAnteriores = em
				.createNamedQuery(Transaction.FIND_LANCAMENTO_CARTAO_QUERY, CreditCardTransaction.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("serie", null)
				.setParameter("saldoInicial", false).setParameter("account", conta).getResultList();

		for (Iterator<CreditCardTransaction> iterator = lancamentosAnteriores.iterator(); iterator.hasNext();) {
			CreditCardTransaction creditCardTransaction = iterator.next();
			creditCardAccountTransactionRemover.remover(creditCardTransaction, false);
			CreditCardTransaction lancamento = (CreditCardTransaction) creditCardTransaction.clone();
			lancamento.setId(null);
			lancamento.setAccount(conta);
			lancamento.setSaldoInicial(false);
			criadorLancamentoCartao.save(lancamento);
		}
	}

}
