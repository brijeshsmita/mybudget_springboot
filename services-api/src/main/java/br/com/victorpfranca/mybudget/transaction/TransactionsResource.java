package br.com.victorpfranca.mybudget.transaction;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface TransactionsResource {

	@Path("ano/{ano:[0-9]+}/mes/{mes:[0-9]+}")
	MonthTransactionsResource lancamentosDoMes(@PathParam("ano") @NotNull Integer ano,
			@Min(1) @Max(12) @NotNull @PathParam("mes") Integer mes);

	@POST
	void cadastrar(@Valid TransactionRegistryDTO cadastroLancamento);

	@Path("/{uid}")
	MonthTransactionResource lancamento(@PathParam("uid") Integer uid);

}
