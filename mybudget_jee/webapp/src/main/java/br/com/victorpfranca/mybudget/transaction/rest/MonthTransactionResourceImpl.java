package br.com.victorpfranca.mybudget.transaction.rest;

import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import br.com.victorpfranca.mybudget.transaction.MonthTransactionResource;
import br.com.victorpfranca.mybudget.transaction.TransactionDTO;
import br.com.victorpfranca.mybudget.transaction.TransactionQuery;
import br.com.victorpfranca.mybudget.transaction.TransactionSerieUpdaterDTO;
import br.com.victorpfranca.mybudget.transaction.TransactionUpdaterDTO;
import br.com.victorpfranca.mybudget.view.MonthYear;

public class MonthTransactionResourceImpl implements MonthTransactionResource {

	@Inject
	private TransactionQuery transactionQuery;
	@Inject
	private TransactionsRestService lancamentosRestResource;
	@Context
	private HttpServletResponse httpServletResponse;

	private Integer id;
	private MonthYear monthYear;

	public MonthTransactionResourceImpl id(Integer id) {
		this.id = id;
		return this;
	}

	public MonthTransactionResourceImpl monthYear(MonthYear monthYear) {
		this.monthYear = monthYear;
		return this;
	}

	@Override
	public TransactionDTO recuperarLancamento() {
		return Optional.ofNullable(transactionQuery.recuperarLancamento(id, monthYear))
				.map(new TransactionToTransactionDTOConversor()::converter).orElse(null);
	}

	@Override
	public void atualizar(TransactionUpdaterDTO atualizacaoLancamento) {
		lancamentosRestResource.atualizar(id, monthYear, atualizacaoLancamento);
	}

	@Override
	public void remover() {
		lancamentosRestResource.remover(id, monthYear);
	}

	@Override
	public void atualizarSerie(TransactionSerieUpdaterDTO atualizacao) {
		lancamentosRestResource.atualizarSerie(id, monthYear, atualizacao);
	}

	@Override
	public void removerSerie() {
		lancamentosRestResource.removerSerie(id, monthYear);
	}

}
