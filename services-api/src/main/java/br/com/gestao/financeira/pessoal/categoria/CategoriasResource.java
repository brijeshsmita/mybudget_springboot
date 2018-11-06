package br.com.gestao.financeira.pessoal.categoria;

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
public interface CategoriasResource {

	@GET
	List<CategoriaDTO> listar();

	@GET
	@Path("/receitas")
	List<CategoriaDTO> listarReceitas();

	@GET
	@Path("/despesas")
	List<CategoriaDTO> listarDespesas();

	@POST
	Response inserir(@Valid CadastroCategoriaDTO categoria);

	@Path("{id}")
	CategoriaResource categoriaResource(@PathParam("id") Integer id);

}
