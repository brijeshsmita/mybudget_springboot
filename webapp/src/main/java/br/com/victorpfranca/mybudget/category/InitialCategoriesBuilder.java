package br.com.victorpfranca.mybudget.category;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.ObjectUtils;

import br.com.victorpfranca.mybudget.InOut;

@Stateless
public class InitialCategoriesBuilder {

	@EJB
	private CategoriaService categoriaService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute() throws SameNameException {
		List<Category> categories = new ArrayList<Category>();

		InputStream is = ObjectUtils.firstNonNull(
				InitialCategoriesBuilder.class.getResourceAsStream("categorias_iniciais_despesas"),
				InitialCategoriesBuilder.class.getResourceAsStream("/categorias_iniciais_despesas"));

		if (is == null)
			return;
		try {
			saveCategorias(categories, is, InOut.S);
		} catch (SameNameException e) {
			e.printStackTrace();
		}

		categories = new ArrayList<Category>();
		is = ObjectUtils.firstNonNull(
				InitialCategoriesBuilder.class.getResourceAsStream("categorias_iniciais_receitas"),
				InitialCategoriesBuilder.class.getResourceAsStream("/categorias_iniciais_receitas"));
		saveCategorias(categories, is, InOut.E);
	}

	private void saveCategorias(List<Category> categories, InputStream is, InOut inOut)
			throws SameNameException {
		Scanner scanner = new Scanner(is, "UTF-8");
		while (scanner.hasNextLine()) {
			categories.add(new Category(scanner.nextLine(), inOut));
		}
		categoriaService.saveCategorias(categories);
		scanner.close();
	}

}
