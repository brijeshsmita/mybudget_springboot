package br.com.victorpfranca.mybudget.budget.view;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import br.com.victorpfranca.mybudget.budget.Budget;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Named
public class BudgetCreatorDataGrid implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<Category, Map<MonthYear, Budget>> orcamentosGridData;

	protected List<Category> categories;

	private List<MonthYear> anosMeses;

	private List<Budget> budgets;

	public Map<Category, Map<MonthYear, Budget>> criar(List<Category> categories, List<MonthYear> anosMeses,
			List<Budget> budgets) {

		this.categories = categories;
		this.anosMeses = anosMeses;
		this.budgets = budgets;

		this.orcamentosGridData = iniciarMapsCategorias();

		preencherOrcamentosExistentes();

		iniciarMesesSemOrcamento();

		return orcamentosGridData;
	}

	private void iniciarMesesSemOrcamento() {
		// Preencher orcamentosGridData com meses zerados
		for (Map.Entry<Category, Map<MonthYear, Budget>> categoriaEntry : orcamentosGridData.entrySet()) {
			Map<MonthYear, Budget> categoriaMap = ((Map<MonthYear, Budget>) categoriaEntry.getValue());
			for (Iterator<MonthYear> iterator = anosMeses.iterator(); iterator.hasNext();) {
				MonthYear monthYear = iterator.next();
				if (categoriaMap.get(monthYear) == null) {
					Budget budget = new Budget();
					budget.setCategoria(categoriaEntry.getKey());
					budget.setAno(monthYear.getAno());
					budget.setMes(monthYear.getMes());
					categoriaMap.put(monthYear, budget);
				}
			}
		}
	}

	private void preencherOrcamentosExistentes() {

		for (Iterator<Budget> iterator = budgets.iterator(); iterator.hasNext();) {
			Budget budget = (Budget) iterator.next();
			Map<MonthYear, Budget> orcamentosCategoria = orcamentosGridData.get(budget.getCategoria());

			MonthYear monthYear = new MonthYear(budget.getAno(), budget.getMes());

			orcamentosCategoria.put(monthYear, budget);
			if (!anosMeses.contains(monthYear))
				anosMeses.add(monthYear);
		}
		Collections.sort(anosMeses);
	}

	private Map<Category, Map<MonthYear, Budget>> iniciarMapsCategorias() {
		// inicia valores lista de categories com mapas vazios
		Map<Category, Map<MonthYear, Budget>> orcamentosGridData = new LinkedHashMap<Category, Map<MonthYear, Budget>>();
		for (Iterator<Category> iterator = categories.iterator(); iterator.hasNext();) {
			Category category = (Category) iterator.next();
			orcamentosGridData.put(category, new LinkedHashMap<MonthYear, Budget>());
		}
		return orcamentosGridData;
	}

	public List<MonthYear> getAnosMeses() {
		return anosMeses;
	}

	public List<Category> getCategorias() {
		return categories;
	}

	public Map<Category, Map<MonthYear, Budget>> getOrcamentosGridData() {
		return orcamentosGridData;
	}

}
