package br.com.victorpfranca.mybudget.transaction.extractors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;



public class TransactionsReportGenerator {

	public List<Transaction> execute(int ano, int mes, Account account, Category category, TransactionStatus status) {
		return new ArrayList<Transaction>();
	}

	public List<Transaction> execute(int ano, int mes, Account account, Category category, BigDecimal saldoInicial,
			TransactionStatus status) {
		return new ArrayList<Transaction>();
	}

}
