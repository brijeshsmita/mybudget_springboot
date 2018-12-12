package br.com.victorpfranca.mybudget.budget;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BudgetService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void excluir(Budget budget) {
	}

	public void save(List<Budget> budgets) {
	}

	public Budget save(Budget budget) {
		return new Budget();
	}

	public BigDecimal getSaldoReceitaOrcada(int year, int month) {
		return BigDecimal.ZERO;
	}

	public BigDecimal getSaldoReceitaOrcadaAcumulado(int year, int month) {
		return BigDecimal.ZERO;
	}

	public BigDecimal getSaldoDespesaOrcada(int year, int month) {
		return BigDecimal.ZERO;
	}

	public BigDecimal getSaldoDespesaOrcadaAcumulado(int year, int month) {
		return BigDecimal.ZERO;
	}

	public List<MonthCategoryBudgetReal> getReceitasCategoriaOrcada(int year, int month) {
		return new ArrayList<MonthCategoryBudgetReal>();
	}

	public List<MonthCategoryBudgetReal> getDespesasCategoriaOrcada(int year, int month) {
		return new ArrayList<MonthCategoryBudgetReal>();
	}

	public List<Budget> findOrcamentosReceitas(int ano) {
		return new ArrayList<Budget>();
	}

	public List<Budget> findOrcamentosDespesas(int ano) {
		return new ArrayList<Budget>();
	}

}
