package br.com.gestao.financeira.pessoal.orcamento;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrcamentoResource {

	@GET
	@Path("/receitas/categoria")
	List<OrcadoRealDTO> receitasReaisOrcadas();

	@GET
	@Path("/despesas/categoria")
	List<OrcadoRealDTO> despesasReaisOrcadas();

}