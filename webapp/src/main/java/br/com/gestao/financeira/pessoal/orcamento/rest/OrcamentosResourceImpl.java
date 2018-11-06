package br.com.gestao.financeira.pessoal.orcamento.rest;

import javax.inject.Inject;
import javax.ws.rs.Path;

import br.com.gestao.financeira.pessoal.orcamento.OrcamentoResource;
import br.com.gestao.financeira.pessoal.orcamento.OrcamentosResource;

@Path("orcamentos")
public class OrcamentosResourceImpl implements OrcamentosResource {

	@Inject
	private OrcamentoResourceImpl orcamentoResourceImpl;

	@Override
	public OrcamentoResource orcamentoResource(Integer ano, Integer mes) {
		return orcamentoResourceImpl.ano(ano).mes(mes);
	}

}
