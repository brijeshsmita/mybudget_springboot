package br.com.gestao.financeira.pessoal.lancamento.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import br.com.gestao.financeira.pessoal.lancamento.CadastroLancamentoDTO;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoDoMesResource;
import br.com.gestao.financeira.pessoal.lancamento.LancamentosDoMesResource;
import br.com.gestao.financeira.pessoal.lancamento.LancamentosResource;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@Path("lancamentos")
public class LancamentosResourceImpl implements LancamentosResource {

	@Inject
	private LancamentosDoMesResourceImpl lancamentosDoMesResourceImpl;
	@Inject
	private LancamentoDoMesResourceImpl lancamentoDoMesResourceImpl;

	@Inject
	private LancamentosRestService lancamentosRestResource;

	@Context
	private HttpServletResponse httpServletResponse;

	@Override
	public LancamentosDoMesResource lancamentosDoMes(Integer ano, Integer mes) {
		return lancamentosDoMesResourceImpl.setAnoMes(new AnoMes(ano, mes));
	}

	@Override
	public void cadastrar(CadastroLancamentoDTO cadastroLancamento) {
		lancamentosRestResource.cadastrar(cadastroLancamento);
		httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
	}

	@Override
	public LancamentoDoMesResource lancamento(Integer uid) {
		return lancamentoDoMesResourceImpl.id(uid);
	}

}
