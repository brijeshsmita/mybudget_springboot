package br.com.victorpfranca.mybudget.account.rules;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.CheckingAccountTransactionBuilder;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;

@Stateless
public class CheckingAccountBuilder {

	@EJB
	private AccountDataValidator accountDataValidator;

	@EJB
	private CheckingAccountInitialBalanceRemover checkingAccountInitialBalanceRemover;

	@EJB
	private CheckingAccountTransactionBuilder checkingAccountTransactionBuilder;

	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CheckingAccount save(CheckingAccount conta)
			throws SameNameException, TransactionMonthUpdatedException, NullableAccountException,
			AccountTypeException, IncompatibleCategoriesException, InvalidTransactionValueException {

		accountDataValidator.validar(conta);

		conta = em.merge(conta);
		
		checkingAccountInitialBalanceRemover.execute(conta);
		
		checkingAccountTransactionBuilder.save(conta, conta.buildLancamentoSaldoInicial());

		return conta;
	}

}
