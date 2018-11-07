package br.com.victorpfranca.mybudget.category.rest;

import static br.com.victorpfranca.mybudget.infra.LambdaUtils.compose;
import static br.com.victorpfranca.mybudget.infra.LambdaUtils.nullSafeConvert;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.category.CategoryDTO;
import br.com.victorpfranca.mybudget.category.CategoryRegistryDTO;

public class CategoryConversor {
	public Category converter(CategoryRegistryDTO dto) {
		Category category = new Category();
		category.setInOut(InOut.fromChar(dto.getTipo()));
		category.setNome(dto.getNome());
		return category;
	}

	public CategoryDTO converter(Category category) {
		CategoryDTO categoriaDTO = new CategoryDTO();
		categoriaDTO.setId(category.getId());
		categoriaDTO.setNome(category.getNome());
		categoriaDTO.setTipo(nullSafeConvert(category, compose(Category::getInOut, InOut::getValue)));
		return categoriaDTO;
	}

}