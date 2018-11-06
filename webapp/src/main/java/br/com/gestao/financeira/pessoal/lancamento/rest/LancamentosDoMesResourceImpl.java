package br.com.gestao.financeira.pessoal.lancamento.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import br.com.gestao.financeira.pessoal.lancamento.ConsultaLancamentos;
import br.com.gestao.financeira.pessoal.lancamento.FiltrosLancamentos;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoDTO;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoDoMesResource;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoItemFaturaCartaoDTO;
import br.com.gestao.financeira.pessoal.lancamento.LancamentosDoMesResource;
import br.com.gestao.financeira.pessoal.view.AnoMes;

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
