package br.com.gestao.financeira.pessoal.lancamento.rest;

import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import br.com.gestao.financeira.pessoal.lancamento.AtualizacaoLancamentoDTO;
import br.com.gestao.financeira.pessoal.lancamento.AtualizacaoSerieLancamentoDTO;
import br.com.gestao.financeira.pessoal.lancamento.ConsultaLancamentos;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoDTO;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoDoMesResource;
import br.com.gestao.financeira.pessoal.view.AnoMes;

public class LancamentoDoMesResourceImpl implements LancamentoDoMesResource {

	@Inject
	private ConsultaLancamentos consultaLancamentos;
	@Inject
	private LancamentosRestService lancamentosRestResource;
	@Context
	private HttpServletResponse httpServletResponse;

	private Integer id;
	private AnoMes anoMes;

	public LancamentoDoMesResourceImpl id(Integer id) {
		this.id = id;
		return this;
	}

	public LancamentoDoMesResourceImpl anoMes(AnoMes anoMes) {
		this.anoMes = anoMes;
		return this;
	}

	@Override
	public LancamentoDTO recuperarLancamento() {
		return Optional.ofNullable(consultaLancamentos.recuperarLancamento(id, anoMes))
				.map(new ConversorLancamentoParaLancamentoDTO()::converter).orElse(null);
	}

	@Override
	public void atualizar(AtualizacaoLancamentoDTO atualizacaoLancamento) {
		lancamentosRestResource.atualizar(id, anoMes, atualizacaoLancamento);
	}

	@Override
	public void remover() {
		lancamentosRestResource.remover(id, anoMes);
	}

	@Override
	public void atualizarSerie(AtualizacaoSerieLancamentoDTO atualizacao) {
		lancamentosRestResource.atualizarSerie(id, anoMes, atualizacao);
	}

	@Override
	public void removerSerie() {
		lancamentosRestResource.removerSerie(id, anoMes);
	}

}
