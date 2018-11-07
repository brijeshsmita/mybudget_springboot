package br.com.victorpfranca.mybudget.transaction;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface MonthTransactionsResource {

	@GET
	List<TransactionDTO> lancamentos(@QueryParam("conta") Integer conta, @QueryParam("categoria") Integer categoria);

	@GET
	@Path("/extratocartao")
	List<CreditCardAccountInvoiceItemDTO> extratoCartao(@QueryParam("conta") Integer conta,
			@QueryParam("categoria") Integer categoria);

	@Path("/lancamento/{uid}")
	MonthTransactionResource lancamento(@PathParam("uid") Integer uid);
}