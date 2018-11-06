package br.com.victorpfranca.mybudget.transaction.rest;

import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import br.com.victorpfranca.mybudget.lancamento.AtualizacaoLancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.AtualizacaoSerieLancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.LancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.LancamentoDoMesResource;
import br.com.victorpfranca.mybudget.transaction.TransactionQuery;
import br.com.victorpfranca.mybudget.view.MonthYear;

public class LancamentoDoMesResourceImpl implements LancamentoDoMesResource {

	@Inject
	private TransactionQuery transactionQuery;
	@Inject
	private LancamentosRestService lancamentosRestResource;
	@Context
	private HttpServletResponse httpServletResponse;

	private Integer id;
	private MonthYear monthYear;

	public LancamentoDoMesResourceImpl id(Integer id) {
		this.id = id;
		return this;
	}

	public LancamentoDoMesResourceImpl monthYear(MonthYear monthYear) {
		this.monthYear = monthYear;
		return this;
	}

	@Override
	public LancamentoDTO recuperarLancamento() {
		return Optional.ofNullable(transactionQuery.recuperarLancamento(id, monthYear))
				.map(new ConversorLancamentoParaLancamentoDTO()::converter).orElse(null);
	}

	@Override
	public void atualizar(AtualizacaoLancamentoDTO atualizacaoLancamento) {
		lancamentosRestResource.atualizar(id, monthYear, atualizacaoLancamento);
	}

	@Override
	public void remover() {
		lancamentosRestResource.remover(id, monthYear);
	}

	@Override
	public void atualizarSerie(AtualizacaoSerieLancamentoDTO atualizacao) {
		lancamentosRestResource.atualizarSerie(id, monthYear, atualizacao);
	}

	@Override
	public void removerSerie() {
		lancamentosRestResource.removerSerie(id, monthYear);
	}

}
