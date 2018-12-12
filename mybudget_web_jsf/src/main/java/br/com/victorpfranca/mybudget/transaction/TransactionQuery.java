package br.com.victorpfranca.mybudget.transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import br.com.victorpfranca.mybudget.view.MonthYear;

@RequestScoped
public class TransactionQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<Transaction> transactions(TransactionsFilter transactionsFilter) {
		return new ArrayList<Transaction>();
	}

	public List<Transaction> extratoCartao(TransactionsFilter transactionsFilter) {
		return new ArrayList<Transaction>();
	}

	public Transaction recuperarLancamento(Integer id, MonthYear monthYear) {
		return new Transaction();
	}

}
