package br.com.victorpfranca.mybudget.transaction.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import br.com.victorpfranca.mybudget.transaction.MonthTransactionResource;
import br.com.victorpfranca.mybudget.transaction.MonthTransactionsResource;
import br.com.victorpfranca.mybudget.transaction.TransactionRegistryDTO;
import br.com.victorpfranca.mybudget.transaction.TransactionsResource;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Path("transactions")
public class TransactionsResourceImpl implements TransactionsResource {

	@Inject
	private MonthTransactionsResourceImpl monthTransactionsResourceImpl;
	@Inject
	private MonthTransactionResourceImpl monthTransactionResourceImpl;

	@Inject
	private TransactionsRestService lancamentosRestResource;

	@Context
	private HttpServletResponse httpServletResponse;

	@Override
	public MonthTransactionsResource lancamentosDoMes(Integer ano, Integer mes) {
		return monthTransactionsResourceImpl.setAnoMes(new MonthYear(ano, mes));
	}

	@Override
	public void cadastrar(TransactionRegistryDTO cadastroLancamento) {
		lancamentosRestResource.cadastrar(cadastroLancamento);
		httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
	}

	@Override
	public MonthTransactionResource lancamento(Integer uid) {
		return monthTransactionResourceImpl.id(uid);
	}

}
