package br.com.victorpfranca.mybudget.orcamento.view;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import org.apache.logging.log4j.core.appender.SyslogAppender;

import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.orcamento.Orcamento;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Named
public class CriadorOrcamentoDataGrid implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<Category, Map<MonthYear, Orcamento>> orcamentosGridData;

	protected List<Category> categories;

	private List<MonthYear> anosMeses;

	private List<Orcamento> orcamentos;

	public Map<Category, Map<MonthYear, Orcamento>> criar(List<Category> categories, List<MonthYear> anosMeses,
			List<Orcamento> orcamentos) {

		this.categories = categories;
		this.anosMeses = anosMeses;
		this.orcamentos = orcamentos;

		this.orcamentosGridData = iniciarMapsCategorias();

		preencherOrcamentosExistentes();

		iniciarMesesSemOrcamento();

		return orcamentosGridData;
	}

	private void iniciarMesesSemOrcamento() {
		// Preencher orcamentosGridData com meses zerados
		for (Map.Entry<Category, Map<MonthYear, Orcamento>> categoriaEntry : orcamentosGridData.entrySet()) {
			Map<MonthYear, Orcamento> categoriaMap = ((Map<MonthYear, Orcamento>) categoriaEntry.getValue());
			for (Iterator<MonthYear> iterator = anosMeses.iterator(); iterator.hasNext();) {
				MonthYear monthYear = iterator.next();
				if (categoriaMap.get(monthYear) == null) {
					Orcamento orcamento = new Orcamento();
					orcamento.setCategory(categoriaEntry.getKey());
					orcamento.setAno(monthYear.getAno());
					orcamento.setMes(monthYear.getMes());
					categoriaMap.put(monthYear, orcamento);
				}
			}
		}
	}

	private void preencherOrcamentosExistentes() {

		for (Iterator<Orcamento> iterator = orcamentos.iterator(); iterator.hasNext();) {
			Orcamento orcamento = (Orcamento) iterator.next();
			Map<MonthYear, Orcamento> orcamentosCategoria = orcamentosGridData.get(orcamento.getCategoria());

			MonthYear monthYear = new MonthYear(orcamento.getAno(), orcamento.getMes());

			orcamentosCategoria.put(monthYear, orcamento);
			if (!anosMeses.contains(monthYear))
				anosMeses.add(monthYear);
		}
		Collections.sort(anosMeses);
	}

	private Map<Category, Map<MonthYear, Orcamento>> iniciarMapsCategorias() {
		// inicia valores lista de categories com mapas vazios
		Map<Category, Map<MonthYear, Orcamento>> orcamentosGridData = new LinkedHashMap<Category, Map<MonthYear, Orcamento>>();
		for (Iterator<Category> iterator = categories.iterator(); iterator.hasNext();) {
			Category category = (Category) iterator.next();
			orcamentosGridData.put(category, new LinkedHashMap<MonthYear, Orcamento>());
		}
		return orcamentosGridData;
	}

	public List<MonthYear> getAnosMeses() {
		return anosMeses;
	}

	public List<Category> getCategorias() {
		return categories;
	}

	public Map<Category, Map<MonthYear, Orcamento>> getOrcamentosGridData() {
		return orcamentosGridData;
	}

}
