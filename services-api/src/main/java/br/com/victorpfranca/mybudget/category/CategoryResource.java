package br.com.victorpfranca.mybudget.category;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CategoryResource {

	@GET
	CategoryDTO recuperar();

	@PUT
	void atualizar(@Valid CategoryUpdateDTO categoryUpdateDTO);

	@DELETE
	void remover();

}