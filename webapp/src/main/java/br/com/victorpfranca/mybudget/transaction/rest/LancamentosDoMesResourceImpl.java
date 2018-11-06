package br.com.victorpfranca.mybudget.transaction.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import br.com.victorpfranca.mybudget.lancamento.LancamentoDTO;
import br.com.victorpfranca.mybudget.lancamento.LancamentoDoMesResource;
import br.com.victorpfranca.mybudget.lancamento.LancamentoItemFaturaCartaoDTO;
import br.com.victorpfranca.mybudget.lancamento.LancamentosDoMesResource;
import br.com.victorpfranca.mybudget.transaction.TransactionQuery;
import br.com.victorpfranca.mybudget.transaction.TransactionsFilter;
import br.com.victorpfranca.mybudget.view.MonthYear;

public class LancamentosDoMesResourceImpl implements LancamentosDoMesResource {

    @Inject
    private TransactionQuery transactionQuery;
    @Inject
    private LancamentoDoMesResourceImpl lancamentoDoMesResourceImpl;
    @Context
    private HttpServletResponse httpServletResponse;

    private MonthYear monthYear;

    public LancamentosDoMesResourceImpl setAnoMes(MonthYear monthYear) {
        this.monthYear = monthYear;
        return this;
    }

    @Override
    public List<LancamentoDTO> lancamentos(Integer conta, Integer categoria) {
        return transactionQuery.transactions(new TransactionsFilter(monthYear, categoria, conta)).stream()
                .map(new ConversorLancamentoParaLancamentoDTO()::converter)
                .collect(Collectors.toList());
    }

	@Override
	public List<LancamentoItemFaturaCartaoDTO> extratoCartao(Integer conta, Integer categoria) {
        return transactionQuery.extratoCartao(new TransactionsFilter(monthYear, categoria, conta)).stream()
                .map(new ConversorLancamentoParaItemFaturaCartaoDTO()::converter)
                .collect(Collectors.toList());
	}

    @Override
    public LancamentoDoMesResource lancamento(Integer id) {
        return lancamentoDoMesResourceImpl.monthYear(monthYear).id(id);
    }

}
