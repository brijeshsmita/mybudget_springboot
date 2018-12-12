package br.com.victorpfranca.mybudget.account;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BalanceResource {
	
	
	@GET
	@Path("corrente")
	BalanceDTO recuperarSaldoCorrente(@QueryParam("conta") Integer conta);

	@GET
	@Path("inicial")
	BalanceDTO recuperarSaldoInicial(@QueryParam("conta") Integer conta);

	@GET
	@Path("orcado/receita")
	BalanceDTO recuperarSaldoReceitaOrcada();

	@GET
	@Path("orcado/despesa")
	BalanceDTO recuperarSaldoDespesaOrcada();

	@GET
	@Path("previsto")
	BalanceDTO recuperarSaldoFinalPrevisto(@QueryParam("conta") Integer conta, @QueryParam("categoria") Integer categoria);

}