package br.com.victorpfranca.mybudget.budget;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BudgetResource {

	@GET
	@Path("/receitas/categoria")
	List<BudgetRealDTO> receitasReaisOrcadas();

	@GET
	@Path("/despesas/categoria")
	List<BudgetRealDTO> despesasReaisOrcadas();

}