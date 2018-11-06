package br.com.gestao.financeira.pessoal.conta;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SaldoResource {
	
	
	@GET
	@Path("corrente")
	SaldoDTO recuperarSaldoCorrente(@QueryParam("conta") Integer conta);

	@GET
	@Path("inicial")
	SaldoDTO recuperarSaldoInicial(@QueryParam("conta") Integer conta);

	@GET
	@Path("orcado/receita")
	SaldoDTO recuperarSaldoReceitaOrcada();

	@GET
	@Path("orcado/despesa")
	SaldoDTO recuperarSaldoDespesaOrcada();

	@GET
	@Path("previsto")
	SaldoDTO recuperarSaldoFinalPrevisto(@QueryParam("conta") Integer conta, @QueryParam("categoria") Integer categoria);

}