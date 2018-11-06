package br.com.gestao.financeira.pessoal.orcamento.view;

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

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.orcamento.Orcamento;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@RunWith(Parameterized.class)
public class CriadorOrcamentoDataGridTest {

	@Parameter(0)
	public List<Categoria> categoriasInput;

	@Parameter(1)
	public List<AnoMes> anosMesesInput;

	@Parameter(2)
	public List<Orcamento> orcamentosInput;

	@Parameters
	public static Collection<Object[]> data() {

		List<Categoria> categorias = new ArrayList<Categoria>();
		Categoria categoria = new Categoria();
		categoria.setId(1);
		categoria.setNome("despesa 1");
		categoria.setInOut(InOut.S);
		categorias.add(categoria);

		List<AnoMes> anosMeses = new ArrayList<AnoMes>();
		for (int i = 1; i <= 12; i++) {
			anosMeses.add(new AnoMes(2018, i));
		}

		List<Orcamento> orcamentos = new ArrayList<Orcamento>();

		preencherOrcamentos(categorias.get(0), orcamentos, 2018, 1, 6, BigDecimal.ONE);

		Object[][] data = new Object[][] { { categorias, anosMeses, orcamentos } };

		return Arrays.asList(data);
	}

	@Test
	public void shouldInitOrcamentosGrid() {
		CriadorOrcamentoDataGrid criadorOrcamentoDataGrid = new CriadorOrcamentoDataGrid();

		criadorOrcamentoDataGrid.criar(categoriasInput, anosMesesInput, orcamentosInput);

		List<AnoMes> meses = criadorOrcamentoDataGrid.getAnosMeses();
		Object[] mesesArray = meses.toArray();
		assertEquals("Numero meses", meses.size(), anosMesesInput.size());
		for (int i = 0; i < mesesArray.length; i++) {
			assertEquals("Ano", ((AnoMes) mesesArray[i]).getAno(), 2018);
			assertEquals("Mês", ((AnoMes) mesesArray[i]).getMes(), i + 1);
		}

		Map<Categoria, Map<AnoMes, Orcamento>> orcamentosGridData = criadorOrcamentoDataGrid.getOrcamentosGridData();

		for (Map.Entry<Categoria, Map<AnoMes, Orcamento>> categoriaEntry : orcamentosGridData.entrySet()) {

			Map<AnoMes, Orcamento> categoriaMap = ((Map<AnoMes, Orcamento>) categoriaEntry.getValue());

			assertEquals("Categoria", categoriasInput.get(0), categoriaEntry.getKey());

			assertEquals("Valor Orcamento 1", orcamentosInput.get(0).getValor(),
					categoriaMap.get(new AnoMes(2018, 1)).getValor());
			assertEquals("Valor Orcamento 2", orcamentosInput.get(1).getValor(),
					categoriaMap.get(new AnoMes(2018, 2)).getValor());
			assertEquals("Valor Orcamento 3", orcamentosInput.get(2).getValor(),
					categoriaMap.get(new AnoMes(2018, 3)).getValor());
			assertEquals("Valor Orcamento 4", orcamentosInput.get(3).getValor(),
					categoriaMap.get(new AnoMes(2018, 4)).getValor());
			assertEquals("Valor Orcamento 5", orcamentosInput.get(4).getValor(),
					categoriaMap.get(new AnoMes(2018, 5)).getValor());
			assertEquals("Valor Orcamento 6", orcamentosInput.get(5).getValor(),
					categoriaMap.get(new AnoMes(2018, 6)).getValor());
			assertEquals("Valor Orcamento 7", BigDecimal.ZERO, categoriaMap.get(new AnoMes(2018, 7)).getValor());
			assertEquals("Valor Orcamento 8", BigDecimal.ZERO, categoriaMap.get(new AnoMes(2018, 8)).getValor());
			assertEquals("Valor Orcamento 9", BigDecimal.ZERO, categoriaMap.get(new AnoMes(2018, 9)).getValor());
			assertEquals("Valor Orcamento 10", BigDecimal.ZERO, categoriaMap.get(new AnoMes(2018, 10)).getValor());
			assertEquals("Valor Orcamento 11", BigDecimal.ZERO, categoriaMap.get(new AnoMes(2018, 11)).getValor());
			assertEquals("Valor Orcamento 12", BigDecimal.ZERO, categoriaMap.get(new AnoMes(2018, 12)).getValor());

		}
	}

	private static void preencherOrcamentos(Categoria categoria, List<Orcamento> orcamentos, int ano, int mesIni,
			int mesFim, BigDecimal valor) {
		for (int i = mesIni; i <= mesFim; i++) {
			Orcamento orcamento = new Orcamento();
			orcamento.setAno(ano);
			orcamento.setMes(i);
			orcamento.setCategoria(categoria);
			orcamento.setValor(valor);
			orcamentos.add(orcamento);
		}
	}

}
