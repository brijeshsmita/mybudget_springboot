package br.com.gestao.financeira.pessoal.orcamento;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.gestao.financeira.pessoal.view.AnoMes;

@RequestScoped
public class ConsultaOrcamentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private OrcamentoService orcamentoService;

    private Map<AnoMes, List<OrcadoRealMesCategoria>> cacheDespesasPorCategoriaOrcada = new HashMap<>();
    private Map<AnoMes, List<OrcadoRealMesCategoria>> cacheReceitasPorCategoriaOrcada = new HashMap<>();

    public List<OrcadoRealMesCategoria> recuperarDespesasPorCategoriaOrcada(AnoMes filtroAnoMes) {
        return cacheDespesasPorCategoriaOrcada.computeIfAbsent(filtroAnoMes,
                anoMes -> orcamentoService.getDespesasCategoriaOrcada(anoMes.getAno(), anoMes.getMes()));
    }

    public List<OrcadoRealMesCategoria> recuperarReceitasPorCategoriaOrcada(AnoMes filtroAnoMes) {
        return cacheReceitasPorCategoriaOrcada.computeIfAbsent(filtroAnoMes,
                anoMes -> orcamentoService.getReceitasCategoriaOrcada(anoMes.getAno(), anoMes.getMes()));
    }

}
