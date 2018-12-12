package br.com.victorpfranca.mybudget.budget.rest;

import static br.com.victorpfranca.mybudget.infra.LambdaUtils.compose;
import static br.com.victorpfranca.mybudget.infra.LambdaUtils.nullSafeConvert;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.com.victorpfranca.mybudget.budget.BudgetQuery;
import br.com.victorpfranca.mybudget.budget.BudgetRealDTO;
import br.com.victorpfranca.mybudget.budget.BudgetResource;
import br.com.victorpfranca.mybudget.budget.MonthCategoryBudgetReal;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.infra.date.DateUtils;
import br.com.victorpfranca.mybudget.view.MonthYear;

public class BudgetResourceImpl implements BudgetResource {

	@Inject
	private BudgetQuery budgetQuery;
	private Integer ano;
	private Integer mes;

	public BudgetResourceImpl ano(Integer ano) {
		this.ano = ano;
		return this;
	}

	public BudgetResourceImpl mes(Integer mes) {
		this.mes = mes;
		return this;
	}

	@Override
	public List<BudgetRealDTO> receitasReaisOrcadas() {
		MonthYear monthYear = new MonthYear(ano, mes);
		return budgetQuery.recuperarReceitasPorCategoriaOrcada(monthYear).stream().map(conversorOrcadoReal(monthYear))
				.collect(Collectors.toList());
	}

	@Override
	public List<BudgetRealDTO> despesasReaisOrcadas() {
		MonthYear monthYear = new MonthYear(ano, mes);
		return budgetQuery.recuperarDespesasPorCategoriaOrcada(monthYear).stream().map(conversorOrcadoReal(monthYear))
				.collect(Collectors.toList());
	}

	private Function<MonthCategoryBudgetReal, BudgetRealDTO> conversorOrcadoReal(MonthYear monthYear) {
		return orcadoRealMesCategoria -> converter(orcadoRealMesCategoria, monthYear);
	}

	private BudgetRealDTO converter(MonthCategoryBudgetReal ent, MonthYear monthYear) {
		compose(MonthCategoryBudgetReal::getCategoria, Category::getNome);
		String categoria = nullSafeConvert(ent, compose(MonthCategoryBudgetReal::getCategoria, Category::getNome));
		String data = nullSafeConvert(monthYear,
				compose(MonthYear::getDate, DateUtils::localDateToDate).andThen(DateUtils::iso8601));
		BigDecimal orcado = nullSafeConvert(ent, MonthCategoryBudgetReal::getOrcado);
		BigDecimal realizado = nullSafeConvert(ent, MonthCategoryBudgetReal::getRealizado);
		return new BudgetRealDTO(categoria, data, orcado, realizado);
	}

}
