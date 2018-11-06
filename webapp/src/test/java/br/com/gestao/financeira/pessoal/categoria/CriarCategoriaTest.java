package br.com.gestao.financeira.pessoal.categoria;

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

import br.com.gestao.financeira.pessoal.DAOMock;
import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.infra.dao.DAO;

@RunWith(Parameterized.class)
public class CriarCategoriaTest {

	private CriadorCategoria criadorCategoria;
	private DAO<Categoria> categoriaDAO;

	@Parameter(0)
	public Categoria input;

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { new Categoria("categoria entrada", InOut.E) },
				{ new Categoria("categoria saída", InOut.S) } };
		return Arrays.asList(data);
	}

	@Before
	public void init() {
		categoriaDAO = new DAOMock<Categoria>();

		criadorCategoria = new CriadorCategoriaMock();
		criadorCategoria.setCategoriaDao(categoriaDAO);
	}

	@Test
	public void shouldCreateCategoria() {

		try {
			Categoria retorno = criadorCategoria.save(input);
			assertEquals("Nome", input.getNome(), retorno.getNome());
			assertEquals("InOut", input.getInOut(), retorno.getInOut());
		} catch (MesmoNomeExistenteException e) {
			fail();
		}
	}

	@Test
	public void shouldUpdateCategoria() {

		try {
			Categoria retorno = criadorCategoria.save(input);
			Integer id = retorno.getId();

			String novoNome = "alterado";
			input.setNome(novoNome);

			retorno = criadorCategoria.save(input);
			assertEquals("Result", id, retorno.getId());
			assertEquals("Result", novoNome, retorno.getNome());
			assertEquals("Result", input.getInOut(), retorno.getInOut());
		} catch (MesmoNomeExistenteException e) {
			fail();
		}
	}

}
