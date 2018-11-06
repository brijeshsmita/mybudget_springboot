package br.com.victorpfranca.mybudget.transaction.rest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.conta.SaldoDTO;
import br.com.victorpfranca.mybudget.lancamento.MeuFuturoResource;
import br.com.victorpfranca.mybudget.transaction.MonthlyTransactions;
import br.com.victorpfranca.mybudget.view.MonthYear;

public class LancamentosMensaisResourceImpl implements MeuFuturoResource {

    @Inject
    private MonthlyTransactions monthlyTransactions;

    @Override
    public List<SaldoDTO> lancamentos(Date start, Date end) {
        inicializarLancamentos(start, end);
        return saldosStream().map(this::converterDTO).sequential().sorted(Comparator.comparing(SaldoDTO::getDate))
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal lancamento(Integer ano, Integer mes, Date start, Date end) {
        inicializarLancamentos(start, end);
        return saldosStream().filter(saldo -> saldo.compareDate(ano, mes) == 0).map(AccountBalance::getValor).findFirst()
                .orElse(null);
    }

    @Override
    public SaldoDTO menor(Date start, Date end) {
        inicializarLancamentos(start, end);
        return saldosStream().map(this::converterDTO).min(Comparator.comparing(SaldoDTO::getValor)).orElse(null);
    }

    @Override
    public SaldoDTO maior(Date start, Date end) {
        return saldosStream().map(this::converterDTO).max(Comparator.comparing(SaldoDTO::getValor)).orElse(null);
    }

    @Override
    public SaldoDTO ultimo(Date start, Date end) {
        return saldosStream().map(this::converterDTO).max(Comparator.comparing(SaldoDTO::getDate)).orElse(null);
    }

    private SaldoDTO converterDTO(AccountBalance saldo) {
        return new SaldoDTO(saldoContaDateString(saldo), saldo.getValor());
    }

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private String saldoContaDateString(AccountBalance saldo) {
        return toString(LocalDate.of(saldo.getAno(), saldo.getMes(), 1));
    }

    private String toString(LocalDate date) {
        return df.format(LocalDateConverter.toDate(date));
    }

    private Stream<AccountBalance> saldosStream() {
        return monthlyTransactions.getSaldos().parallelStream();
    }

    private void inicializarLancamentos(Date start, Date end) {
        Function<Date, MonthYear> conversorDateAnoMes = Function.<Date> identity().andThen(LocalDateConverter::fromDate)
                .andThen(MonthYear::new);
        MonthYear anoMesInicial = Optional.ofNullable(start).map(conversorDateAnoMes).orElse(null);
        MonthYear anoMesFinal = Optional.ofNullable(end).map(conversorDateAnoMes).orElse(null);
        if (anoMesInicial != null || anoMesFinal != null)
            monthlyTransactions.inicializar(anoMesInicial, anoMesFinal);
    }

}
