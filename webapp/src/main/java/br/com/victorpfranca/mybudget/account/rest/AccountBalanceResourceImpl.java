package br.com.victorpfranca.mybudget.account.rest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import javax.inject.Inject;

import br.com.victorpfranca.mybudget.account.AccountBalanceFilter;
import br.com.victorpfranca.mybudget.account.BalanceQuery;
import br.com.victorpfranca.mybudget.conta.SaldoDTO;
import br.com.victorpfranca.mybudget.conta.SaldoResource;
import br.com.victorpfranca.mybudget.infra.date.DateUtils;
import br.com.victorpfranca.mybudget.lancamento.FiltrosLancamentos;
import br.com.victorpfranca.mybudget.view.AnoMes;

public class AccountBalanceResourceImpl implements SaldoResource {

	@Inject
	private BalanceQuery balanceQuery;
	private Integer ano;
	private Integer mes;

	public AccountBalanceResourceImpl mes(Integer mes) {
		this.mes = mes;
		return this;
	}

	public AccountBalanceResourceImpl ano(Integer ano) {
		this.ano = ano;
		return this;
	}

	@Override
	public SaldoDTO recuperarSaldoCorrente(Integer conta) {
		AnoMes anoMes = new AnoMes(ano, mes);
		String date = toIso8601(anoMes);
		AccountBalanceFilter accountBalanceFilter = new AccountBalanceFilter(anoMes, conta);
		return Optional.ofNullable(balanceQuery.recuperarSaldoCorrentePrevisto(accountBalanceFilter)).map(saldoDTO(date))
				.orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoInicial(Integer conta) {
		AnoMes anoMes = new AnoMes(ano, mes);
		String date = toIso8601(anoMes);
		AccountBalanceFilter accountBalanceFilter = new AccountBalanceFilter(anoMes, conta);
		return Optional.ofNullable(balanceQuery.recuperarSaldoInicial(accountBalanceFilter)).map(saldoDTO(date))
				.orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoReceitaOrcada() {
		AnoMes anoMes = new AnoMes(ano, mes);
		String date = toIso8601(anoMes);
		return Optional.ofNullable(balanceQuery.recuperarSaldoReceitaOrcada(anoMes)).map(saldoDTO(date)).orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoDespesaOrcada() {
		AnoMes anoMes = new AnoMes(ano, mes);
		String date = toIso8601(anoMes);
		return Optional.ofNullable(balanceQuery.recuperarSaldoDespesaOrcada(anoMes)).map(saldoDTO(date)).orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoFinalPrevisto(Integer categoria, Integer conta) {
		AnoMes anoMes = new AnoMes(ano, mes);
		String date = toIso8601(anoMes);
		FiltrosLancamentos filtrosLancamentos = new FiltrosLancamentos(anoMes, categoria, conta);
		return Optional.ofNullable(balanceQuery.recuperarSaldoFinalPrevisto(filtrosLancamentos)).map(saldoDTO(date))
				.orElse(null);
	}

	private String toIso8601(AnoMes anoMes) {
		return DateUtils.iso8601(DateUtils.localDateToDate(anoMes.getDate()));
	}

	private Function<BigDecimal, SaldoDTO> saldoDTO(String date) {
		return saldo -> new SaldoDTO(date, saldo);
	}

}