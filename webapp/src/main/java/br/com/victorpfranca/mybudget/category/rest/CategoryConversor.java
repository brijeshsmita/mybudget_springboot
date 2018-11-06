package br.com.victorpfranca.mybudget.category.rest;

import static br.com.victorpfranca.mybudget.infra.LambdaUtils.compose;
import static br.com.victorpfranca.mybudget.infra.LambdaUtils.nullSafeConvert;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.categoria.CadastroCategoriaDTO;
import br.com.victorpfranca.mybudget.categoria.CategoriaDTO;
import br.com.victorpfranca.mybudget.category.Category;

public class CategoryConversor {
	public Category converter(CadastroCategoriaDTO dto) {
		Category category = new Category();
		category.setInOut(InOut.fromChar(dto.getTipo()));
		category.setNome(dto.getNome());
		return category;
	}

	public CategoriaDTO converter(Category category) {
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		categoriaDTO.setId(category.getId());
		categoriaDTO.setNome(category.getNome());
		categoriaDTO.setTipo(nullSafeConvert(category, compose(Category::getInOut, InOut::getValue)));
		return categoriaDTO;
	}

}