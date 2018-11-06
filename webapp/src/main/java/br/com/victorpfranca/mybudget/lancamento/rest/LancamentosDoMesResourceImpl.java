package br.com.victorpfranca.mybudget.lancamento.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import br.com.victorpfranca.mybudget.lancamento.ConsultaLancamentos;
import br.com.victorpfranca.mybudget.lancamento.FiltrosLancamentos;
import br.com.victorpfranca.mybudget.lancamento.LancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.LancamentoDoMesResource;
import br.com.victorpfranca.mybudget.lancamento.LancamentoItemFaturaCartaoDTO;
import br.com.victorpfranca.mybudget.lancamento.LancamentosDoMesResource;
import br.com.victorpfranca.mybudget.view.AnoMes;

public class LancamentosDoMesResourceImpl implements LancamentosDoMesResource {

    @Inject
    private ConsultaLancamentos consultaLancamentos;
    @Inject
    private LancamentoDoMesResourceImpl lancamentoDoMesResourceImpl;
    @Context
    private HttpServletResponse httpServletResponse;

    private AnoMes anoMes;

    public LancamentosDoMesResourceImpl setAnoMes(AnoMes anoMes) {
        this.anoMes = anoMes;
        return this;
    }

    @Override
    public List<LancamentoDTO> lancamentos(Integer conta, Integer categoria) {
        return consultaLancamentos.lancamentos(new FiltrosLancamentos(anoMes, categoria, conta)).stream()
                .map(new ConversorLancamentoParaLancamentoDTO()::converter)
                .collect(Collectors.toList());
    }

	@Override
	public List<LancamentoItemFaturaCartaoDTO> extratoCartao(Integer conta, Integer categoria) {
        return consultaLancamentos.extratoCartao(new FiltrosLancamentos(anoMes, categoria, conta)).stream()
                .map(new ConversorLancamentoParaItemFaturaCartaoDTO()::converter)
                .collect(Collectors.toList());
	}

    @Override
    public LancamentoDoMesResource lancamento(Integer id) {
        return lancamentoDoMesResourceImpl.anoMes(anoMes).id(id);
    }

}
