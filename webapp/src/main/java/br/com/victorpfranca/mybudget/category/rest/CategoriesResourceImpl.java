package br.com.victorpfranca.mybudget.category.rest;

import static br.com.victorpfranca.mybudget.infra.LambdaUtils.nullSafeConvert;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.categoria.CadastroCategoriaDTO;
import br.com.victorpfranca.mybudget.categoria.CategoriaDTO;
import br.com.victorpfranca.mybudget.categoria.CategoriaResource;
import br.com.victorpfranca.mybudget.categoria.CategoriasResource;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.category.CategoriaService;
import br.com.victorpfranca.mybudget.category.SameNameException;

@Path("categories")
public class CategoriesResourceImpl implements CategoriasResource {

	@Inject
	private CategoryResourceImpl categoryResourceImpl;
	@Inject
	private CategoriaService categoriaService;
	@Inject
	private CategoryConversor categoryConversor;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	@Override
	public List<CategoriaDTO> listar() {
		return categoriaService.findAll().stream().map(categoryConversor::converter).collect(Collectors.toList());
	}

	@Override
	public List<CategoriaDTO> listarReceitas() {
		return categoriaService.findReceitas().stream().map(categoryConversor::converter).collect(Collectors.toList());
	}

	@Override
	public List<CategoriaDTO> listarDespesas() {
		return categoriaService.findDespesas().stream().map(categoryConversor::converter).collect(Collectors.toList());
	}

	@Override
	public Response inserir(CadastroCategoriaDTO categoria) {
		try {
			Category created = categoriaService.save(nullSafeConvert(categoria, categoryConversor::converter));
			URI createdUri = uriInfo.getRequestUriBuilder().path(created.getId().toString()).build();
			return Response.created(createdUri).build();
		} catch (SameNameException e) {
			throw new WebApplicationException(e.getMessage(), e,
					Response.status(422).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build());
		}
	}

	@Override
	public CategoriaResource categoriaResource(Integer id) {
		return categoryResourceImpl.id(id);
	}

}