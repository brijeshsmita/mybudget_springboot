package br.com.gestao.financeira.pessoal.categoria.rest;

import static br.com.gestao.financeira.pessoal.infra.LambdaUtils.nullSafeConvert;

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

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.categoria.CadastroCategoriaDTO;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.categoria.CategoriaDTO;
import br.com.gestao.financeira.pessoal.categoria.CategoriaResource;
import br.com.gestao.financeira.pessoal.categoria.CategoriaService;
import br.com.gestao.financeira.pessoal.categoria.CategoriasResource;
import br.com.gestao.financeira.pessoal.categoria.MesmoNomeExistenteException;

@Path("categorias")
public class CategoriasResourceImpl implements CategoriasResource {

	@Inject
	private CategoriaResourceImpl categoriaResourceImpl;
	@Inject
	private CategoriaService categoriaService;
	@Inject
	private ConversorCategoria conversorCategoria;
	@Context
	private HttpServletResponse httpServletResponse;
	@Context
	private UriInfo uriInfo;

	@Override
	public List<CategoriaDTO> listar() {
		return categoriaService.findAll().stream().map(conversorCategoria::converter).collect(Collectors.toList());
	}

	@Override
	public List<CategoriaDTO> listarReceitas() {
		return categoriaService.findReceitas().stream().map(conversorCategoria::converter).collect(Collectors.toList());
	}

	@Override
	public List<CategoriaDTO> listarDespesas() {
		return categoriaService.findDespesas().stream().map(conversorCategoria::converter).collect(Collectors.toList());
	}

	@Override
	public Response inserir(CadastroCategoriaDTO categoria) {
		try {
			Categoria created = categoriaService.save(nullSafeConvert(categoria, conversorCategoria::converter));
			URI createdUri = uriInfo.getRequestUriBuilder().path(created.getId().toString()).build();
			return Response.created(createdUri).build();
		} catch (MesmoNomeExistenteException e) {
			throw new WebApplicationException(e.getMessage(), e,
					Response.status(422).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build());
		}
	}

	@Override
	public CategoriaResource categoriaResource(Integer id) {
		return categoriaResourceImpl.id(id);
	}

}