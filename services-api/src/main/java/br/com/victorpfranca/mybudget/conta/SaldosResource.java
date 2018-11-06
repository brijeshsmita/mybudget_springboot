package br.com.victorpfranca.mybudget.conta;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.victorpfranca.mybudget.lancamento.MeuFuturoResource;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SaldosResource {

	@Path("ano/{ano:[0-9]+}/mes/{mes:[0-9]+}")
	SaldoResource saldoResource(@PathParam("ano") Integer ano, @Min(1) @Max(12) @PathParam("mes") Integer mes);

	@Path("meuFuturo")
	MeuFuturoResource meuFuturo();

}
