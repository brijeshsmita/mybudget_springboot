package br.com.victorpfranca.mybudget.category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.victorpfranca.mybudget.DAOMock;
import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.infra.dao.DAO;

@RunWith(Parameterized.class)
public class CriarCategoriaTest {

	private CategoryBuilder categoryBuilder;
	private DAO<Category> categoriaDAO;

	@Parameter(0)
	public Category input;

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { new Category("category entrada", InOut.E) },
				{ new Category("category saída", InOut.S) } };
		return Arrays.asList(data);
	}

	@Before
	public void init() {
		categoriaDAO = new DAOMock<Category>();

		categoryBuilder = new CriadorCategoriaMock();
		categoryBuilder.setCategoryDao(categoriaDAO);
	}

	@Test
	public void shouldCreateCategoria() {

		try {
			Category retorno = categoryBuilder.save(input);
			assertEquals("Nome", input.getNome(), retorno.getNome());
			assertEquals("InOut", input.getInOut(), retorno.getInOut());
		} catch (SameNameException e) {
			fail();
		}
	}

	@Test
	public void shouldUpdateCategoria() {

		try {
			Category retorno = categoryBuilder.save(input);
			Integer id = retorno.getId();

			String novoNome = "alterado";
			input.setNome(novoNome);

			retorno = categoryBuilder.save(input);
			assertEquals("Result", id, retorno.getId());
			assertEquals("Result", novoNome, retorno.getNome());
			assertEquals("Result", input.getInOut(), retorno.getInOut());
		} catch (SameNameException e) {
			fail();
		}
	}

}
