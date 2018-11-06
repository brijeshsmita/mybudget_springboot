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
import br.com.victorpfranca.mybudget.transaction.TransactionsFilter;
import br.com.victorpfranca.mybudget.view.MonthYear;

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
		MonthYear monthYear = new MonthYear(ano, mes);
		String date = toIso8601(monthYear);
		AccountBalanceFilter accountBalanceFilter = new AccountBalanceFilter(monthYear, conta);
		return Optional.ofNullable(balanceQuery.recuperarSaldoCorrentePrevisto(accountBalanceFilter)).map(saldoDTO(date))
				.orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoInicial(Integer conta) {
		MonthYear monthYear = new MonthYear(ano, mes);
		String date = toIso8601(monthYear);
		AccountBalanceFilter accountBalanceFilter = new AccountBalanceFilter(monthYear, conta);
		return Optional.ofNullable(balanceQuery.recuperarSaldoInicial(accountBalanceFilter)).map(saldoDTO(date))
				.orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoReceitaOrcada() {
		MonthYear monthYear = new MonthYear(ano, mes);
		String date = toIso8601(monthYear);
		return Optional.ofNullable(balanceQuery.recuperarSaldoReceitaOrcada(monthYear)).map(saldoDTO(date)).orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoDespesaOrcada() {
		MonthYear monthYear = new MonthYear(ano, mes);
		String date = toIso8601(monthYear);
		return Optional.ofNullable(balanceQuery.recuperarSaldoDespesaOrcada(monthYear)).map(saldoDTO(date)).orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoFinalPrevisto(Integer categoria, Integer conta) {
		MonthYear monthYear = new MonthYear(ano, mes);
		String date = toIso8601(monthYear);
		TransactionsFilter transactionsFilter = new TransactionsFilter(monthYear, categoria, conta);
		return Optional.ofNullable(balanceQuery.recuperarSaldoFinalPrevisto(transactionsFilter)).map(saldoDTO(date))
				.orElse(null);
	}

	private String toIso8601(MonthYear monthYear) {
		return DateUtils.iso8601(DateUtils.localDateToDate(monthYear.getDate()));
	}

	private Function<BigDecimal, SaldoDTO> saldoDTO(String date) {
		return saldo -> new SaldoDTO(date, saldo);
	}

}