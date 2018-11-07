package br.com.victorpfranca.mybudget.category;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CategoriesResource {

	@GET
	List<CategoryDTO> listar();

	@GET
	@Path("/receitas")
	List<CategoryDTO> listarReceitas();

	@GET
	@Path("/despesas")
	List<CategoryDTO> listarDespesas();

	@POST
	Response inserir(@Valid CategoryRegistryDTO categoria);

	@Path("{id}")
	CategoryResource categoryResource(@PathParam("id") Integer id);

}
