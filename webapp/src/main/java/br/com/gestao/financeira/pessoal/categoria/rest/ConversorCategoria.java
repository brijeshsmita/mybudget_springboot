package br.com.gestao.financeira.pessoal.categoria.rest;

import static br.com.gestao.financeira.pessoal.infra.LambdaUtils.compose;
import static br.com.gestao.financeira.pessoal.infra.LambdaUtils.nullSafeConvert;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.categoria.CadastroCategoriaDTO;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.categoria.CategoriaDTO;

public class ConversorCategoria {
	public Categoria converter(CadastroCategoriaDTO dto) {
		Categoria categoria = new Categoria();
		categoria.setInOut(InOut.fromChar(dto.getTipo()));
		categoria.setNome(dto.getNome());
		return categoria;
	}

	public CategoriaDTO converter(Categoria categoria) {
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		categoriaDTO.setId(categoria.getId());
		categoriaDTO.setNome(categoria.getNome());
		categoriaDTO.setTipo(nullSafeConvert(categoria, compose(Categoria::getInOut, InOut::getValue)));
		return categoriaDTO;
	}

}