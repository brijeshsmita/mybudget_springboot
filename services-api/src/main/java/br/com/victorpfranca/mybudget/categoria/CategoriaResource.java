package br.com.victorpfranca.mybudget.categoria;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CategoriaResource {

	@GET
	CategoriaDTO recuperar();

	@PUT
	void atualizar(@Valid AtualizacaoCategoriaDTO atualizacaoCategoriaDTO);

	@DELETE
	void remover();

}