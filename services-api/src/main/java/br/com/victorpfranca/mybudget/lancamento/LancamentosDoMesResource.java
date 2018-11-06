package br.com.victorpfranca.mybudget.lancamento;

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
public interface LancamentosDoMesResource {

	@GET
	List<LancamentoDTO> lancamentos(@QueryParam("conta") Integer conta, @QueryParam("categoria") Integer categoria);

	@GET
	@Path("/extratocartao")
	List<LancamentoItemFaturaCartaoDTO> extratoCartao(@QueryParam("conta") Integer conta,
			@QueryParam("categoria") Integer categoria);

	@Path("/lancamento/{uid}")
	LancamentoDoMesResource lancamento(@PathParam("uid") Integer uid);
}