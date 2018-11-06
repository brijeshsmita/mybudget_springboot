package br.com.victorpfranca.mybudget.orcamento;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.victorpfranca.mybudget.view.MonthYear;

@RequestScoped
public class ConsultaOrcamentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private OrcamentoService orcamentoService;

    private Map<MonthYear, List<OrcadoRealMesCategoria>> cacheDespesasPorCategoriaOrcada = new HashMap<>();
    private Map<MonthYear, List<OrcadoRealMesCategoria>> cacheReceitasPorCategoriaOrcada = new HashMap<>();

    public List<OrcadoRealMesCategoria> recuperarDespesasPorCategoriaOrcada(MonthYear filtroAnoMes) {
        return cacheDespesasPorCategoriaOrcada.computeIfAbsent(filtroAnoMes,
                anoMes -> orcamentoService.getDespesasCategoriaOrcada(anoMes.getAno(), anoMes.getMes()));
    }

    public List<OrcadoRealMesCategoria> recuperarReceitasPorCategoriaOrcada(MonthYear filtroAnoMes) {
        return cacheReceitasPorCategoriaOrcada.computeIfAbsent(filtroAnoMes,
                anoMes -> orcamentoService.getReceitasCategoriaOrcada(anoMes.getAno(), anoMes.getMes()));
    }

}
