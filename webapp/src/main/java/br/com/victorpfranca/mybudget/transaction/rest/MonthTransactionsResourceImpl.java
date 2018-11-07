package br.com.victorpfranca.mybudget.transaction.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import br.com.victorpfranca.mybudget.transaction.CreditCardAccountInvoiceItemDTO;
import br.com.victorpfranca.mybudget.transaction.MonthTransactionResource;
import br.com.victorpfranca.mybudget.transaction.MonthTransactionsResource;
import br.com.victorpfranca.mybudget.transaction.TransactionDTO;
import br.com.victorpfranca.mybudget.transaction.TransactionQuery;
import br.com.victorpfranca.mybudget.transaction.TransactionsFilter;
import br.com.victorpfranca.mybudget.view.MonthYear;

public class MonthTransactionsResourceImpl implements MonthTransactionsResource {

	@Inject
	private TransactionQuery transactionQuery;
	@Inject
	private MonthTransactionResourceImpl monthTransactionResourceImpl;
	@Context
	private HttpServletResponse httpServletResponse;

	private MonthYear monthYear;

	public MonthTransactionsResourceImpl setAnoMes(MonthYear monthYear) {
		this.monthYear = monthYear;
		return this;
	}

	@Override
	public List<TransactionDTO> lancamentos(Integer conta, Integer categoria) {
		return transactionQuery.transactions(new TransactionsFilter(monthYear, categoria, conta)).stream()
				.map(new TransactionToTransactionDTOConversor()::converter).collect(Collectors.toList());
	}

	@Override
	public List<CreditCardAccountInvoiceItemDTO> extratoCartao(Integer conta, Integer categoria) {
		return transactionQuery.extratoCartao(new TransactionsFilter(monthYear, categoria, conta)).stream()
				.map(new TransactionToCreditCardAccountInvoiceItemDTO()::converter).collect(Collectors.toList());
	}

	@Override
	public MonthTransactionResource lancamento(Integer id) {
		return monthTransactionResourceImpl.monthYear(monthYear).id(id);
	}

}
