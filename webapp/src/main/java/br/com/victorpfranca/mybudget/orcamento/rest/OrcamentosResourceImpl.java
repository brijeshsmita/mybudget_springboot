package br.com.victorpfranca.mybudget.orcamento.rest;

import javax.inject.Inject;
import javax.ws.rs.Path;

import br.com.victorpfranca.mybudget.orcamento.OrcamentoResource;
import br.com.victorpfranca.mybudget.orcamento.OrcamentosResource;

@Path("orcamentos")
public class OrcamentosResourceImpl implements OrcamentosResource {

	@Inject
	private OrcamentoResourceImpl orcamentoResourceImpl;

	@Override
	public OrcamentoResource orcamentoResource(Integer ano, Integer mes) {
		return orcamentoResourceImpl.ano(ano).mes(mes);
	}

}
