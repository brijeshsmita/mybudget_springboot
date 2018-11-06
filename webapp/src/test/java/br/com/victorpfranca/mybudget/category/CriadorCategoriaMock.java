package br.com.victorpfranca.mybudget.category;

import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.category.CategoryBuilder;
import br.com.victorpfranca.mybudget.category.SameNameException;

public class CriadorCategoriaMock extends CategoryBuilder {

	@Override
	protected void validarNomeExistente(Category category) throws SameNameException {

	}

}
