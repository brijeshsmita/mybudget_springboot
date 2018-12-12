package br.com.victorpfranca.mybudget.account;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.enterprise.context.RequestScoped;

import br.com.victorpfranca.mybudget.transaction.TransactionsFilter;
import br.com.victorpfranca.mybudget.view.MonthYear;

@RequestScoped
public class BalanceQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    public BigDecimal recuperarSaldoInicial(AccountBalanceFilter accountBalanceFilter) {
    	return BigDecimal.ZERO;
    }

    public BigDecimal recuperarSaldoDespesaOrcada(MonthYear filtroAnoMes) {
    	return BigDecimal.ZERO;
    }

    public BigDecimal recuperarSaldoReceitaOrcada(MonthYear filtroAnoMes) {
    	return BigDecimal.ZERO;
    }

    public BigDecimal recuperarSaldoDespesaOrcadaAcumulado(MonthYear filtroAnoMes) {
    	return BigDecimal.ZERO;
    }

    public BigDecimal recuperarSaldoReceitaOrcadaAcumulado(MonthYear filtroAnoMes) {
    	return BigDecimal.ZERO;
    }

    public BigDecimal recuperarSaldo(MonthYear filtroAnoMes) {
    	return BigDecimal.ZERO;
    }

    public BigDecimal recuperarSaldoCorrentePrevisto(AccountBalanceFilter filtros) {
    	return BigDecimal.ZERO;
    }
    public BigDecimal recuperarSaldoFinalPrevisto(TransactionsFilter transactionsFilter) {
    	return BigDecimal.ZERO;
    }

}
