package br.com.gestao.financeira.pessoal.categoria.rest;

import static br.com.gestao.financeira.pessoal.infra.LambdaUtils.nullSafeConvert;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.gestao.financeira.pessoal.categoria.AtualizacaoCategoriaDTO;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.categoria.CategoriaDTO;
import br.com.gestao.financeira.pessoal.categoria.CategoriaResource;
import br.com.gestao.financeira.pessoal.categoria.CategoriaService;
import br.com.gestao.financeira.pessoal.categoria.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemocaoNaoPermitidaException;

public class CategoriaResourceImpl implements CategoriaResource {

	private Integer id;
	@Inject
	private CategoriaService categoriaService;
	@Inject
	private ConversorCategoria conversorCategoria;

	public CategoriaResourceImpl id(Integer id) {
		this.id = id;
		return this;
	}

	@Override
	public CategoriaDTO recuperar() {
		return nullSafeConvert(categoriaService.find(id), conversorCategoria::converter);
	}

	@Override
	public void atualizar(AtualizacaoCategoriaDTO atualizacaoCategoriaDTO) {
		Optional.ofNullable(atualizacaoCategoriaDTO).ifPresent(dto -> {
			Categoria categoria = categoriaService.find(id);
			categoria.setNome(dto.getNome());
			try {
				categoriaService.save(categoria);
			} catch (MesmoNomeExistenteException e) {
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
