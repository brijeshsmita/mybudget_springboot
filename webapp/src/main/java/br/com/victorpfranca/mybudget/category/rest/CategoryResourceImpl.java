package br.com.victorpfranca.mybudget.category.rest;

import static br.com.victorpfranca.mybudget.infra.LambdaUtils.nullSafeConvert;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.victorpfranca.mybudget.categoria.AtualizacaoCategoriaDTO;
import br.com.victorpfranca.mybudget.categoria.CategoriaDTO;
import br.com.victorpfranca.mybudget.categoria.CategoriaResource;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.category.CategoriaService;
import br.com.victorpfranca.mybudget.category.SameNameException;
import br.com.victorpfranca.mybudget.lancamento.rules.RemocaoNaoPermitidaException;

public class CategoryResourceImpl implements CategoriaResource {

	private Integer id;
	@Inject
	private CategoriaService categoriaService;
	@Inject
	private CategoryConversor categoryConversor;

	public CategoryResourceImpl id(Integer id) {
		this.id = id;
		return this;
	}

	@Override
	public CategoriaDTO recuperar() {
		return nullSafeConvert(categoriaService.find(id), categoryConversor::converter);
	}

	@Override
	public void atualizar(AtualizacaoCategoriaDTO atualizacaoCategoriaDTO) {
		Optional.ofNullable(atualizacaoCategoriaDTO).ifPresent(dto -> {
			Category category = categoriaService.find(id);
			category.setNome(dto.getNome());
			try {
				categoriaService.save(category);
			} catch (SameNameException e) {
				throw new WebApplicationException(e.getMessage(), e,
						Response.status(422).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build());
			}
		});
	}

	@Override
	public void remover() {
		try {
			categoriaService.remove(id);
		} catch (RemocaoNaoPermitidaException e) {
			throw new WebApplicationException(e.getMessage(), e,
					Response.status(422).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build());
		}
	}

}
