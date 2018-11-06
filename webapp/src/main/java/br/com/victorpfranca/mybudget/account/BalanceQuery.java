package br.com.victorpfranca.mybudget.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.lancamento.FiltrosLancamentos;
import br.com.victorpfranca.mybudget.orcamento.OrcamentoService;
import br.com.victorpfranca.mybudget.view.AnoMes;

@RequestScoped
public class BalanceQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private OrcamentoService orcamentoService;
    @Inject
    private BankAccountService bankAccountService;
    
    private Map<AccountBalanceFilter, BigDecimal> cacheSaldoInicial=new HashMap<>();

    private Map<AnoMes, BigDecimal> cacheSaldoDespesaOrcada=new HashMap<>();
    private Map<AnoMes, BigDecimal> cacheSaldoReceitaOrcada=new HashMap<>();

    private Map<AnoMes, BigDecimal> cacheSaldoDespesaOrcadaAcumulado=new HashMap<>();
    private Map<AnoMes, BigDecimal> cacheSaldoReceitaOrcadaAcumulado=new HashMap<>();
    
    private Map<AnoMes, BigDecimal> cacheSaldo=new HashMap<>();
    
    public BigDecimal recuperarSaldoInicial(AccountBalanceFilter accountBalanceFilter) {
        return cacheSaldoInicial.computeIfAbsent(new AccountBalanceFilter(accountBalanceFilter), filtros->{
            BigDecimal saldoInicial = BigDecimal.ZERO;
            AnoMes anoMesAnterior = filtros.getAnoMes().minusMonths(1);
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

    public BigDecimal recuperarSaldoDespesaOrcada(AnoMes filtroAnoMes) {
        return cacheSaldoDespesaOrcada.computeIfAbsent(filtroAnoMes, 
                anoMes->orcamentoService.getSaldoDespesaOrcada(anoMes.getAno(), anoMes.getMes()));
    }

    public BigDecimal recuperarSaldoReceitaOrcada(AnoMes filtroAnoMes) {
        return cacheSaldoReceitaOrcada.computeIfAbsent(filtroAnoMes, 
                anoMes->orcamentoService.getSaldoReceitaOrcada(anoMes.getAno(), anoMes.getMes()));
    }

    public BigDecimal recuperarSaldoDespesaOrcadaAcumulado(AnoMes filtroAnoMes) {
        return cacheSaldoDespesaOrcadaAcumulado.computeIfAbsent(filtroAnoMes, 
                anoMes->orcamentoService.getSaldoDespesaOrcadaAcumulado(anoMes.getAno(), anoMes.getMes()));
    }

    public BigDecimal recuperarSaldoReceitaOrcadaAcumulado(AnoMes filtroAnoMes) {
        return cacheSaldoReceitaOrcadaAcumulado.computeIfAbsent(filtroAnoMes, 
                anoMes->orcamentoService.getSaldoReceitaOrcadaAcumulado(anoMes.getAno(), anoMes.getMes()));
    }

    public BigDecimal recuperarSaldo(AnoMes filtroAnoMes) {
        return cacheSaldo.computeIfAbsent(filtroAnoMes, 
            anoMes->bankAccountService.getSaldosContasCorrentesAte(anoMes.getAno(), anoMes.getMes()));
    }

    public BigDecimal recuperarSaldoCorrentePrevisto(AccountBalanceFilter filtros) {
        AnoMes anoMes = filtros.getAnoMes();
        AnoMes anoMesAnterior = anoMes.minusMonths(1);
        BigDecimal saldoReceitaOrcadaAcumuladoAnterior = recuperarSaldoReceitaOrcadaAcumulado(anoMesAnterior);
        BigDecimal saldoDespesaOrcadaAcumuladoAnterior = recuperarSaldoDespesaOrcadaAcumulado(anoMesAnterior);
        BigDecimal saldoMesAtual = recuperarSaldo(anoMes);
        return saldoMesAtual.add(saldoReceitaOrcadaAcumuladoAnterior).subtract(saldoDespesaOrcadaAcumuladoAnterior);
    }
    public BigDecimal recuperarSaldoFinalPrevisto(FiltrosLancamentos filtrosLancamentos) {
        BigDecimal saldoCorrentePrevisto = recuperarSaldoCorrentePrevisto(filtrosLancamentos);
        BigDecimal saldoSaldoReceitaOrcada = recuperarSaldoReceitaOrcada(filtrosLancamentos.getAnoMes());
        BigDecimal saldoSaldoDespesaOrcada = recuperarSaldoDespesaOrcada(filtrosLancamentos.getAnoMes());
        return saldoCorrentePrevisto.add(saldoSaldoReceitaOrcada).subtract(saldoSaldoDespesaOrcada);
    }

}
