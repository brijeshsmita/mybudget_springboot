package br.com.victorpfranca.mybudget.orcamento.view;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.orcamento.Orcamento;
import br.com.victorpfranca.mybudget.orcamento.view.CriadorOrcamentoDataGrid;
import br.com.victorpfranca.mybudget.view.MonthYear;

@RunWith(Parameterized.class)
public class CriadorOrcamentoDataGridTest {

	@Parameter(0)
	public List<Category> categoriasInput;

	@Parameter(1)
	public List<MonthYear> anosMesesInput;

	@Parameter(2)
	public List<Orcamento> orcamentosInput;

	@Parameters
	public static Collection<Object[]> data() {

		List<Category> categories = new ArrayList<Category>();
		Category category = new Category();
		category.setId(1);
		category.setNome("despesa 1");
		category.setInOut(InOut.S);
		categories.add(category);

		List<MonthYear> anosMeses = new ArrayList<MonthYear>();
		for (int i = 1; i <= 12; i++) {
			anosMeses.add(new MonthYear(2018, i));
		}

		List<Orcamento> orcamentos = new ArrayList<Orcamento>();

		preencherOrcamentos(categories.get(0), orcamentos, 2018, 1, 6, BigDecimal.ONE);

		Object[][] data = new Object[][] { { categories, anosMeses, orcamentos } };

		return Arrays.asList(data);
	}

	@Test
	public void shouldInitOrcamentosGrid() {
		CriadorOrcamentoDataGrid criadorOrcamentoDataGrid = new CriadorOrcamentoDataGrid();

		criadorOrcamentoDataGrid.criar(categoriasInput, anosMesesInput, orcamentosInput);

		List<MonthYear> meses = criadorOrcamentoDataGrid.getAnosMeses();
		Object[] mesesArray = meses.toArray();
		assertEquals("Numero meses", meses.size(), anosMesesInput.size());
		for (int i = 0; i < mesesArray.length; i++) {
			assertEquals("Ano", ((MonthYear) mesesArray[i]).getAno(), 2018);
			assertEquals("Mês", ((MonthYear) mesesArray[i]).getMes(), i + 1);
		}

		Map<Category, Map<MonthYear, Orcamento>> orcamentosGridData = criadorOrcamentoDataGrid.getOrcamentosGridData();

		for (Map.Entry<Category, Map<MonthYear, Orcamento>> categoriaEntry : orcamentosGridData.entrySet()) {

			Map<MonthYear, Orcamento> categoriaMap = ((Map<MonthYear, Orcamento>) categoriaEntry.getValue());

			assertEquals("Category", categoriasInput.get(0), categoriaEntry.getKey());

			assertEquals("Valor Orcamento 1", orcamentosInput.get(0).getValor(),
					categoriaMap.get(new MonthYear(2018, 1)).getValor());
			assertEquals("Valor Orcamento 2", orcamentosInput.get(1).getValor(),
					categoriaMap.get(new MonthYear(2018, 2)).getValor());
			assertEquals("Valor Orcamento 3", orcamentosInput.get(2).getValor(),
					categoriaMap.get(new MonthYear(2018, 3)).getValor());
			assertEquals("Valor Orcamento 4", orcamentosInput.get(3).getValor(),
					categoriaMap.get(new MonthYear(2018, 4)).getValor());
			assertEquals("Valor Orcamento 5", orcamentosInput.get(4).getValor(),
					categoriaMap.get(new MonthYear(2018, 5)).getValor());
			assertEquals("Valor Orcamento 6", orcamentosInput.get(5).getValor(),
					categoriaMap.get(new MonthYear(2018, 6)).getValor());
			assertEquals("Valor Orcamento 7", BigDecimal.ZERO, categoriaMap.get(new MonthYear(2018, 7)).getValor());
			assertEquals("Valor Orcamento 8", BigDecimal.ZERO, categoriaMap.get(new MonthYear(2018, 8)).getValor());
			assertEquals("Valor Orcamento 9", BigDecimal.ZERO, categoriaMap.get(new MonthYear(2018, 9)).getValor());
			assertEquals("Valor Orcamento 10", BigDecimal.ZERO, categoriaMap.get(new MonthYear(2018, 10)).getValor());
			assertEquals("Valor Orcamento 11", BigDecimal.ZERO, categoriaMap.get(new MonthYear(2018, 11)).getValor());
			assertEquals("Valor Orcamento 12", BigDecimal.ZERO, categoriaMap.get(new MonthYear(2018, 12)).getValor());

		}
	}

	private static void preencherOrcamentos(Category category, List<Orcamento> orcamentos, int ano, int mesIni,
			int mesFim, BigDecimal valor) {
		for (int i = mesIni; i <= mesFim; i++) {
			Orcamento orcamento = new Orcamento();
			orcamento.setAno(ano);
			orcamento.setMes(i);
			orcamento.setCategory(category);
			orcamento.setValor(valor);
			orcamentos.add(orcamento);
		}
	}

}
