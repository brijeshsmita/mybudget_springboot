package br.com.gestao.financeira.pessoal.conta.rest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import javax.inject.Inject;

import br.com.gestao.financeira.pessoal.conta.ConsultaSaldos;
import br.com.gestao.financeira.pessoal.conta.FiltrosSaldos;
import br.com.gestao.financeira.pessoal.conta.SaldoDTO;
import br.com.gestao.financeira.pessoal.conta.SaldoResource;
import br.com.gestao.financeira.pessoal.infra.date.DateUtils;
import br.com.gestao.financeira.pessoal.lancamento.FiltrosLancamentos;
import br.com.gestao.financeira.pessoal.view.AnoMes;

public class SaldoResourceImpl implements SaldoResource {

	@Inject
	private ConsultaSaldos consultaSaldos;
	private Integer ano;
	private Integer mes;

	public SaldoResourceImpl mes(Integer mes) {
		this.mes = mes;
		return this;
	}

	public SaldoResourceImpl ano(Integer ano) {
		this.ano = ano;
		return this;
	}

	@Override
	public SaldoDTO recuperarSaldoCorrente(Integer conta) {
		AnoMes anoMes = new AnoMes(ano, mes);
		String date = toIso8601(anoMes);
		FiltrosSaldos filtrosSaldos = new FiltrosSaldos(anoMes, conta);
		return Optional.ofNullable(consultaSaldos.recuperarSaldoCorrentePrevisto(filtrosSaldos)).map(saldoDTO(date))
				.orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoInicial(Integer conta) {
		AnoMes anoMes = new AnoMes(ano, mes);
		String date = toIso8601(anoMes);
		FiltrosSaldos filtrosSaldos = new FiltrosSaldos(anoMes, conta);
		return Optional.ofNullable(consultaSaldos.recuperarSaldoInicial(filtrosSaldos)).map(saldoDTO(date))
				.orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoReceitaOrcada() {
		AnoMes anoMes = new AnoMes(ano, mes);
		String date = toIso8601(anoMes);
		return Optional.ofNullable(consultaSaldos.recuperarSaldoReceitaOrcada(anoMes)).map(saldoDTO(date)).orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoDespesaOrcada() {
		AnoMes anoMes = new AnoMes(ano, mes);
		String date = toIso8601(anoMes);
		return Optional.ofNullable(consultaSaldos.recuperarSaldoDespesaOrcada(anoMes)).map(saldoDTO(date)).orElse(null);
	}

	@Override
	public SaldoDTO recuperarSaldoFinalPrevisto(Integer categoria, Integer conta) {
		AnoMes anoMes = new AnoMes(ano, mes);
		String date = toIso8601(anoMes);
		FiltrosLancamentos filtrosLancamentos = new FiltrosLancamentos(anoMes, categoria, conta);
		return Optional.ofNullable(consultaSaldos.recuperarSaldoFinalPrevisto(filtrosLancamentos)).map(saldoDTO(date))
				.orElse(null);
	}

	private String toIso8601(AnoMes anoMes) {
		return DateUtils.iso8601(DateUtils.localDateToDate(anoMes.getDate()));
	}

	private Function<BigDecimal, SaldoDTO> saldoDTO(String date) {
		return saldo -> new SaldoDTO(date, saldo);
	}

}