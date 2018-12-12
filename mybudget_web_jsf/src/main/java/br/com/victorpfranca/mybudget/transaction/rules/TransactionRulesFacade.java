package br.com.victorpfranca.mybudget.transaction.rules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionSerie;
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;


public class TransactionRulesFacade {

	
	public Transaction saveLancamentoContaCorrente(Transaction transaction)
			throws TransactionMonthUpdatedException, NullableAccountException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {
 		return new Transaction();
	}

	
	public Transaction saveLancamentoCartaoDeCredito(Transaction transaction)
			throws NullableAccountException, TransactionMonthUpdatedException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {
		return new Transaction();
	}

	
	public Transaction saveLancamentoTransferencia(Transaction transaction)
			throws TransactionMonthUpdatedException, NullableAccountException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {
		return new Transaction();
	}

	
	public TransactionSerie saveSerieLancamentoContaCorrente(Transaction transaction)
			throws TransactionMonthUpdatedException, NullableAccountException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException, InvalidTransactionSerieDateException {
		return new TransactionSerie();
	}

	
	public TransactionSerie saveSerieLancamentoCartaoDeCredito(Transaction transaction)
			throws NullableAccountException, TransactionMonthUpdatedException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException, InvalidTransactionSerieDateException {
		return new TransactionSerie();
	}

	
	public TransactionSerie saveSerieLancamentoTransferencia(Transaction transaction)
			throws TransactionMonthUpdatedException, NullableAccountException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException, InvalidTransactionSerieDateException {
		return new TransactionSerie();
	}

	
	public void removeLancamento(Transaction transaction) {
	}

	
	public void removeSerie(TransactionSerie serie) {
	}

	
	public void removeLancamentoCartao(CreditCardTransaction lancamento) {
	}

	
	public void removeSerieLancamentoCartao(TransactionSerie serie) {
	}

	public List<Transaction> extrairExtrato(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial, TransactionStatus status) {
		return new ArrayList<Transaction>();
	}

	public List<Transaction> extrairExtrato(int ano, int mes, Account account, Category category, TransactionStatus status) {
		return new ArrayList<Transaction>();
	}

	public List<Transaction> extrairExtratoCartao(int ano, int mes, Account account, Category category) {
		return new ArrayList<Transaction>();
	}

	public List<Transaction> extrairExtratoCartao(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial) {
		return new ArrayList<Transaction>();
	}

}