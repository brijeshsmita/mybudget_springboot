package br.com.victorpfranca.mybudget.budget;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.victorpfranca.mybudget.view.MonthYear;

@RequestScoped
public class BudgetQuery implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private BudgetService budgetService;

    private Map<MonthYear, List<MonthCategoryBudgetReal>> cacheDespesasPorCategoriaOrcada = new HashMap<>();
    private Map<MonthYear, List<MonthCategoryBudgetReal>> cacheReceitasPorCategoriaOrcada = new HashMap<>();

    public List<MonthCategoryBudgetReal> recuperarDespesasPorCategoriaOrcada(MonthYear filtroAnoMes) {
        return cacheDespesasPorCategoriaOrcada.computeIfAbsent(filtroAnoMes,
                anoMes -> budgetService.getDespesasCategoriaOrcada(anoMes.getAno(), anoMes.getMes()));
    }

    public List<MonthCategoryBudgetReal> recuperarReceitasPorCategoriaOrcada(MonthYear filtroAnoMes) {
        return cacheReceitasPorCategoriaOrcada.computeIfAbsent(filtroAnoMes,
                anoMes -> budgetService.getReceitasCategoriaOrcada(anoMes.getAno(), anoMes.getMes()));
    }

}
