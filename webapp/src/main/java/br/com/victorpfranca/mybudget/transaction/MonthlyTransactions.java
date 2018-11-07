package br.com.victorpfranca.mybudget.transaction;

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

import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.period.PlanningPeriod;
import br.com.victorpfranca.mybudget.view.MonthYear;

@RequestScoped
public class MonthlyTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private BankAccountService lancamentoService;

    @Inject
    private PlanningPeriod planningPeriod;

    private List<AccountBalance> saldos;

    private BigDecimal minValue;
    private BigDecimal maxValue;

    private AccountBalance saldoAtual;
    private AccountBalance saldoProximo;
    private AccountBalance menorSaldo;
    private AccountBalance maiorSaldo;
    private AccountBalance ultimoSaldo;

    private MonthYear inicio;
    private MonthYear fim;

    @PostConstruct
    public void init() {
        inicializar(null, null);
    }
    
    public void inicializar(MonthYear inicio, MonthYear fim) {
        this.inicio=Arrays.asList(inicio, planningPeriod.getMesAtual()).stream()
                .filter(obj-> obj != null)
                .max(Comparator.comparing(Function.identity()))
                .orElse(null);
        this.fim=Arrays.asList(fim, planningPeriod.getMesFinal()).stream()
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
            .max(Comparator.comparing(AccountBalance::getValor)).map(AccountBalance::getValor).orElse(BigDecimal.ZERO);
        minValue=Optional.ofNullable(getSaldos()).orElseGet(ArrayList::new).stream()
            .min(Comparator.comparing(AccountBalance::getValor)).map(AccountBalance::getValor).orElse(BigDecimal.ZERO);
    }

    private void carregarSaldosResumo() {
        MonthYear anoMesAtual = this.inicio;
        for (Iterator<AccountBalance> iterator = saldos.iterator(); iterator.hasNext();) {
            AccountBalance accountBalance = iterator.next();
            if (accountBalance.compareDate(anoMesAtual.getAno(), anoMesAtual.getMes()) == 0) {
                this.saldoAtual = accountBalance;
                break;
            }
        }

        for (Iterator<AccountBalance> iterator = saldos.iterator(); iterator.hasNext();) {
            AccountBalance accountBalance = iterator.next();
            if (accountBalance.compareDate(anoMesAtual.getAno(), anoMesAtual.getMes()) > 0) {
                this.saldoProximo = accountBalance;
                break;
            }
        }

        this.maiorSaldo = new AccountBalance(anoMesAtual.getAno(), anoMesAtual.getMes(), BigDecimal.ZERO);
        this.menorSaldo = new AccountBalance(anoMesAtual.getAno(), anoMesAtual.getMes(), BigDecimal.ZERO);
        for (Iterator<AccountBalance> iterator = saldos.iterator(); iterator.hasNext();) {
            AccountBalance accountBalance = iterator.next();
            if (accountBalance.getValor().compareTo(menorSaldo.getValor()) < 0) {
                this.menorSaldo = accountBalance;
            }
            if (accountBalance.getValor().compareTo(maiorSaldo.getValor()) > 0) {
                this.maiorSaldo = accountBalance;
            }
        }

        this.ultimoSaldo = saldos.get(saldos.size() - 1);

    }

    public MonthYear getInicio() {
        return inicio;
    }

    public MonthYear getFim() {
        return fim;
    }

    public List<AccountBalance> getSaldos() {
        return saldos;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public AccountBalance getSaldoAtual() {
        return saldoAtual;
    }

    public AccountBalance getSaldoProximo() {
        return saldoProximo;
    }

    public AccountBalance getMenorSaldo() {
        return menorSaldo;
    }

    public AccountBalance getMaiorSaldo() {
        return maiorSaldo;
    }

    public AccountBalance getUltimoSaldo() {
        return ultimoSaldo;
    }

}
