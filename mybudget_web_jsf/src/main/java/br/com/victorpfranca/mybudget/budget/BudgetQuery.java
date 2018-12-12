package br.com.victorpfranca.mybudget.budget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import br.com.victorpfranca.mybudget.view.MonthYear;

@RequestScoped
public class BudgetQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<MonthCategoryBudgetReal> recuperarDespesasPorCategoriaOrcada(MonthYear filtroAnoMes) {
		return new ArrayList<MonthCategoryBudgetReal>();
	}

	public List<MonthCategoryBudgetReal> recuperarReceitasPorCategoriaOrcada(MonthYear filtroAnoMes) {
		return new ArrayList<MonthCategoryBudgetReal>();
	}

}
