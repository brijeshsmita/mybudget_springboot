package br.com.victorpfranca.mybudget.account;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.victorpfranca.mybudget.transaction.MyFutureResource;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BalancesResource {

	@Path("ano/{ano:[0-9]+}/mes/{mes:[0-9]+}")
	BalanceResource balanceResource(@PathParam("ano") Integer ano, @Min(1) @Max(12) @PathParam("mes") Integer mes);

	@Path("meuFuturo")
	MyFutureResource meuFuturo();

}
