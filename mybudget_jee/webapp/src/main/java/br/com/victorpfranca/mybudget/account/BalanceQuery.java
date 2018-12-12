package br.com.victorpfranca.mybudget.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.budget.BudgetService;
import br.com.victorpfranca.mybudget.transaction.TransactionsFilter;
import br.com.victorpfranca.mybudget.view.MonthYear;

@RequestScoped
public class BalanceQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private BudgetService budgetService;
    @Inject
    private BankAccountService bankAccountService;
    
    private Map<AccountBalanceFilter, BigDecimal> cacheSaldoInicial=new HashMap<>();

    private Map<MonthYear, BigDecimal> cacheSaldoDespesaOrcada=new HashMap<>();
    private Map<MonthYear, BigDecimal> cacheSaldoReceitaOrcada=new HashMap<>();

    private Map<MonthYear, BigDecimal> cacheSaldoDespesaOrcadaAcumulado=new HashMap<>();
    private Map<MonthYear, BigDecimal> cacheSaldoReceitaOrcadaAcumulado=new HashMap<>();
    
    private Map<MonthYear, BigDecimal> cacheSaldo=new HashMap<>();
    
    public BigDecimal recuperarSaldoInicial(AccountBalanceFilter accountBalanceFilter) {
        return cacheSaldoInicial.computeIfAbsent(new AccountBalanceFilter(accountBalanceFilter), filtros->{
            BigDecimal saldoInicial = BigDecimal.ZERO;
            MonthYear anoMesAnterior = filtros.getAnoMes().minusMonths(1);
            if (filtros.getAccount() == null) {
                BigDecimal saldoReceitaOrcadaAcumuladoAnterior = recuperarSaldoReceitaOrcadaAcumulado(anoMesAnterior);
                BigDecimal saldoDespesaOrcadaAcumuladoAnterior = recuperarSaldoDespesaOrcadaAcumulado(anoMesAnterior);
                BigDecimal saldoMesAnterior = recuperarSaldo(anoMesAnterior);
                saldoInicial = saldoMesAnterior.add(saldoReceitaOrcadaAcumuladoAnterior)
                        .subtract(saldoDespesaOrcadaAcumuladoAnterior);
            } else {
                saldoInicial = bankAccountService.getSaldoAte(filtros.getAccount(), anoMesAnterior.getAno(), anoMesAnterior.getMes());
            }
            return saldoInicial;
        });
    }

    public BigDecimal recuperarSaldoDespesaOrcada(MonthYear filtroAnoMes) {
        return cacheSaldoDespesaOrcada.computeIfAbsent(filtroAnoMes, 
                anoMes->budgetService.getSaldoDespesaOrcada(anoMes.getAno(), anoMes.getMes()));
    }

    public BigDecimal recuperarSaldoReceitaOrcada(MonthYear filtroAnoMes) {
        return cacheSaldoReceitaOrcada.computeIfAbsent(filtroAnoMes, 
                anoMes->budgetService.getSaldoReceitaOrcada(anoMes.getAno(), anoMes.getMes()));
    }

    public BigDecimal recuperarSaldoDespesaOrcadaAcumulado(MonthYear filtroAnoMes) {
        return cacheSaldoDespesaOrcadaAcumulado.computeIfAbsent(filtroAnoMes, 
                anoMes->budgetService.getSaldoDespesaOrcadaAcumulado(anoMes.getAno(), anoMes.getMes()));
    }

    public BigDecimal recuperarSaldoReceitaOrcadaAcumulado(MonthYear filtroAnoMes) {
        return cacheSaldoReceitaOrcadaAcumulado.computeIfAbsent(filtroAnoMes, 
                anoMes->budgetService.getSaldoReceitaOrcadaAcumulado(anoMes.getAno(), anoMes.getMes()));
    }

    public BigDecimal recuperarSaldo(MonthYear filtroAnoMes) {
        return cacheSaldo.computeIfAbsent(filtroAnoMes, 
            anoMes->bankAccountService.getSaldosContasCorrentesAte(anoMes.getAno(), anoMes.getMes()));
    }

    public BigDecimal recuperarSaldoCorrentePrevisto(AccountBalanceFilter filtros) {
        MonthYear monthYear = filtros.getAnoMes();
        MonthYear anoMesAnterior = monthYear.minusMonths(1);
        BigDecimal saldoReceitaOrcadaAcumuladoAnterior = recuperarSaldoReceitaOrcadaAcumulado(anoMesAnterior);
        BigDecimal saldoDespesaOrcadaAcumuladoAnterior = recuperarSaldoDespesaOrcadaAcumulado(anoMesAnterior);
        BigDecimal saldoMesAtual = recuperarSaldo(monthYear);
        return saldoMesAtual.add(saldoReceitaOrcadaAcumuladoAnterior).subtract(saldoDespesaOrcadaAcumuladoAnterior);
    }
    public BigDecimal recuperarSaldoFinalPrevisto(TransactionsFilter transactionsFilter) {
        BigDecimal saldoCorrentePrevisto = recuperarSaldoCorrentePrevisto(transactionsFilter);
        BigDecimal saldoSaldoReceitaOrcada = recuperarSaldoReceitaOrcada(transactionsFilter.getAnoMes());
        BigDecimal saldoSaldoDespesaOrcada = recuperarSaldoDespesaOrcada(transactionsFilter.getAnoMes());
        return saldoCorrentePrevisto.add(saldoSaldoReceitaOrcada).subtract(saldoSaldoDespesaOrcada);
    }

}
