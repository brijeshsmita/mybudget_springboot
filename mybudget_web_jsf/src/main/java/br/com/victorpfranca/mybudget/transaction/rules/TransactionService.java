package br.com.victorpfranca.mybudget.transaction.rules;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionSerie;
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;


public class TransactionService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public List<Transaction> carregarExtratoCorrenteMensal(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial, TransactionStatus status) {
		return new ArrayList<Transaction>();
	}

	public List<Transaction> carregarExtratoCartaoMensal(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial) {
		return new ArrayList<Transaction>();
	}

	
	public TransactionSerie saveSerie(Transaction transaction)
			throws NullableAccountException, IncompatibleCategoriesException, TransactionMonthUpdatedException,
			AccountTypeException, InvalidTransactionValueException, InvalidTransactionSerieDateException {
		return new TransactionSerie();
	}

	
	public Transaction save(Transaction transaction) throws NullableAccountException, IncompatibleCategoriesException,
			TransactionMonthUpdatedException, AccountTypeException, InvalidTransactionValueException {
		return new Transaction();
	}

	
	public Transaction confirmar(Transaction transaction)
			throws NullableAccountException, IncompatibleCategoriesException, TransactionMonthUpdatedException,
			AccountTypeException, InvalidTransactionValueException {
		return new Transaction();
	}

	
	public void remove(Transaction transaction) {
	}

	
	public void removeSerie(TransactionSerie serie) {
	}

	
	public void removeLancamentoCartao(CreditCardTransaction lancamentoCartao) {
	}

	
	public void removeSerieLancamentoCartao(TransactionSerie serie) {
	}

}
