package br.com.gestao.financeira.pessoal.lancamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.conta.rules.ContaService;
import br.com.gestao.financeira.pessoal.periodo.PeriodoPlanejamento;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@RequestScoped
public class LancamentosMensais implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ContaService lancamentoService;

    @Inject
    private PeriodoPlanejamento periodoPlanejamento;

    private List<SaldoConta> saldos;

    private BigDecimal minValue;
    private BigDecimal maxValue;

    private SaldoConta saldoAtual;
    private SaldoConta saldoProximo;
    private SaldoConta menorSaldo;
    private SaldoConta maiorSaldo;
    private SaldoConta ultimoSaldo;

    private AnoMes inicio;
    private AnoMes fim;

    @PostConstruct
    public void init() {
        inicializar(null, null);
    }
    
    public void inicializar(AnoMes inicio, AnoMes fim) {
        this.inicio=Arrays.asList(inicio, periodoPlanejamento.getMesAtual()).stream()
                .filter(obj-> obj != null)
                .max(Comparator.comparing(Function.identity()))
                .orElse(null);
        this.fim=Arrays.asList(fim, periodoPlanejamento.getMesFinal()).stream()
                .filter(obj-> obj != null)
                .min(Comparator.comparing(Function.identity()))
                .orElse(null);
        carregarSaldos();
        carregarValorMaximoEMinimo();
        carregarSaldosResumo();
    }

    private void carregarSaldos() {
        this.saldos = lancamentoService.carregarSaldoFuturoPrevisto(getInicio(), getFim());
    }

    private void carregarValorMaximoEMinimo() {
        maxValue=Optional.ofNullable(getSaldos()).orElseGet(ArrayList::new).stream()
            .max(Comparator.comparing(SaldoConta::getValor)).map(SaldoConta::getValor).orElse(BigDecimal.ZERO);
        minValue=Optional.ofNullable(getSaldos()).orElseGet(ArrayList::new).stream()
            .min(Comparator.comparing(SaldoConta::getValor)).map(SaldoConta::getValor).orElse(BigDecimal.ZERO);
    }

    private void carregarSaldosResumo() {
        AnoMes anoMesAtual = this.inicio;
        for (Iterator<SaldoConta> iterator = saldos.iterator(); iterator.hasNext();) {
            SaldoConta saldoConta = iterator.next();
            if (saldoConta.compareDate(anoMesAtual.getAno(), anoMesAtual.getMes()) == 0) {
                this.saldoAtual = saldoConta;
                break;
            }
        }

        for (Iterator<SaldoConta> iterator = saldos.iterator(); iterator.hasNext();) {
            SaldoConta saldoConta = iterator.next();
            if (saldoConta.compareDate(anoMesAtual.getAno(), anoMesAtual.getMes()) > 0) {
                this.saldoProximo = saldoConta;
                break;
            }
        }

        this.maiorSaldo = new SaldoConta(anoMesAtual.getAno(), anoMesAtual.getMes(), BigDecimal.ZERO);
        this.menorSaldo = new SaldoConta(anoMesAtual.getAno(), anoMesAtual.getMes(), BigDecimal.ZERO);
        for (Iterator<SaldoConta> iterator = saldos.iterator(); iterator.hasNext();) {
            SaldoConta saldoConta = iterator.next();
            if (saldoConta.getValor().compareTo(menorSaldo.getValor()) < 0) {
                this.menorSaldo = saldoConta;
            }
            if (saldoConta.getValor().compareTo(maiorSaldo.getValor()) > 0) {
                this.maiorSaldo = saldoConta;
            }
        }

        this.ultimoSaldo = saldos.get(saldos.size() - 1);

    }

    public AnoMes getInicio() {
        return inicio;
    }

    public AnoMes getFim() {
        return fim;
    }

    public List<SaldoConta> getSaldos() {
        return saldos;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public SaldoConta getSaldoAtual() {
        return saldoAtual;
    }

    public SaldoConta getSaldoProximo() {
        return saldoProximo;
    }

    public SaldoConta getMenorSaldo() {
        return menorSaldo;
    }

    public SaldoConta getMaiorSaldo() {
        return maiorSaldo;
    }

    public SaldoConta getUltimoSaldo() {
        return ultimoSaldo;
    }

}
