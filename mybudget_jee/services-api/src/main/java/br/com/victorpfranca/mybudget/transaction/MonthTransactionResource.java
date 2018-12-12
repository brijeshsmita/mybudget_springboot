package br.com.victorpfranca.mybudget.transaction;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface MonthTransactionResource {

	@GET
	TransactionDTO recuperarLancamento();

	@PUT
	void atualizar(@Valid TransactionUpdaterDTO atualizacao);

	@DELETE
	void remover();

	@PUT
	@Path("serie")
	void atualizarSerie(@Valid TransactionSerieUpdaterDTO atualizacao);

	@DELETE
	@Path("serie")
	void removerSerie();

}
