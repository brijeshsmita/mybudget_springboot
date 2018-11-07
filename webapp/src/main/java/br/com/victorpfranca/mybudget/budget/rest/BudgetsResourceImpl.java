package br.com.victorpfranca.mybudget.budget.rest;

import javax.inject.Inject;
import javax.ws.rs.Path;

import br.com.victorpfranca.mybudget.budget.BudgetResource;
import br.com.victorpfranca.mybudget.budget.BudgetsResource;

@Path("orcamentos")
public class BudgetsResourceImpl implements BudgetsResource {

	@Inject
	private BudgetResourceImpl budgetResourceImpl;

	@Override
	public BudgetResource budgetResource(Integer ano, Integer mes) {
		return budgetResourceImpl.ano(ano).mes(mes);
	}

}
