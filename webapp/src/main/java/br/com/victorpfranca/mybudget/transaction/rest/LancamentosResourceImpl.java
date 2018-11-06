package br.com.victorpfranca.mybudget.transaction.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import br.com.victorpfranca.mybudget.lancamento.CadastroLancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.LancamentoDoMesResource;
import br.com.victorpfranca.mybudget.lancamento.LancamentosDoMesResource;
import br.com.victorpfranca.mybudget.lancamento.LancamentosResource;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Path("transactions")
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
		return lancamentosDoMesResourceImpl.setAnoMes(new MonthYear(ano, mes));
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
