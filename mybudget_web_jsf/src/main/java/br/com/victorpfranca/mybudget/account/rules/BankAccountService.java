package br.com.victorpfranca.mybudget.account.rules;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.DeletionNotPermittedException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;
import br.com.victorpfranca.mybudget.view.MonthYear;

public class BankAccountService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<Account> findAll() {
		return new ArrayList<Account>();
	}

	public Account find(Integer id) {
		return new Account();
	}

	public List<Account> findContasCorrentes() {
		return new ArrayList<Account>();
	}

	public List<Account> findContasBancos() {
		return new ArrayList<Account>();
	}

	public List<Account> findContasDinheiro() {
		return new ArrayList<Account>();
	}

	public List<Account> findContasCartoes() {
		return new ArrayList<Account>();
	}

	public List<CreditCardTransaction> findLancamentosIniciaisCartao(CreditCardAccount conta) {
		return new ArrayList<CreditCardTransaction>();
	}

	public List<CheckingAccountTransaction> findFaturas(CreditCardAccount conta) {
		return new ArrayList<CheckingAccountTransaction>();
	}

	public BigDecimal getSaldosContasCorrentesAte(Integer ano, Integer mes) {
		return BigDecimal.ZERO;
	}

	public BigDecimal getSaldoAte(Integer conta, Integer ano, Integer mes) {
		return BigDecimal.ZERO;
	}

	public BigDecimal getSaldoAte(Account account, Integer ano, Integer mes) {
		return BigDecimal.ZERO;
	}

	public List<Account> saveContasCorrente(List<Account> accounts)
			throws SameNameException, NullableAccountException, TransactionMonthUpdatedException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {
		return new ArrayList<Account>();
	}

	public Account saveContaCorrente(Account account)
			throws SameNameException, NullableAccountException, TransactionMonthUpdatedException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {
		return new Account();
	}

	public void saveContasCartoes(List<Account> accounts)
			throws SameNameException, NullableAccountException, TransactionMonthUpdatedException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {
	}

	public Account saveContaCartao(Account account, List<Transaction> transactions)
			throws SameNameException, NullableAccountException, TransactionMonthUpdatedException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {

		return new Account();
	}

	public void remove(Account account) throws DeletionNotPermittedException, CantRemoveException {

	}

	public Map<Account, Map<MonthYear, AccountBalance>> reconstruirSaldosContas() {
		return new LinkedHashMap<>();
	}

	public List<AccountBalance> carregarSaldoFuturoPrevisto() {
		return new ArrayList<AccountBalance>();
	}

	public List<AccountBalance> carregarSaldoFuturoPrevisto(MonthYear anoMesAtual, MonthYear anoMesFinal) {
		return new ArrayList<AccountBalance>();
	}

}
