package br.com.gestao.financeira.pessoal.conta;

import static br.com.gestao.financeira.pessoal.LocalDateConverter.toDate;
import static br.com.gestao.financeira.pessoal.LocalDateConverter.fromDate;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Date;

import org.junit.Test;

public class CalcularDataPagamentoTest {

	private LocalDate LOCALDATE_2000_01_01 = LocalDate.of(2000, 1, 1);
	private LocalDate LOCALDATE_2000_01_02 = LocalDate.of(2000, 1, 2);
	private LocalDate LOCALDATE_2000_01_03 = LocalDate.of(2000, 1, 3);
	
	private LocalDate LOCALDATE_2000_02_02 = LocalDate.of(2000, 2, 2);
	private LocalDate LOCALDATE_2000_02_03 = LocalDate.of(2000, 2, 3);

	private LocalDate LOCALDATE_2000_03_01 = LocalDate.of(2000, 3, 1);
	
	private LocalDate LOCALDATE_2000_04_01 = LocalDate.of(2000, 4, 1);
	private LocalDate LOCALDATE_2000_05_01 = LocalDate.of(2000, 5, 1);
	
	private LocalDate LOCALDATE_2001_02_01 = LocalDate.of(2001, 2, 1);
	private LocalDate LOCALDATE_2001_03_01 = LocalDate.of(2001, 3, 1);

	private int DAY_OF_MONTH_1 = 1;
	private int DAY_OF_MONTH_2 = 2;
	private int DAY_OF_MONTH_3 = 3;
	private int DAY_OF_MONTH_29 = 29;
	private int DAY_OF_MONTH_31 = 31;
	
	@Test
	public void testDataPagamento_pagamentoDia31_Mes30Dias() {
		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		contaCartao.setCartaoDiaPagamento(DAY_OF_MONTH_31);

		Date dataReferencia = toDate(LOCALDATE_2000_04_01);
		Date dataPagamento = toDate(contaCartao.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_05_01;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_pagamentoDia29_MesFevereiro_AnoNaoBissexto() {
		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		contaCartao.setCartaoDiaPagamento(DAY_OF_MONTH_29);

		Date dataReferencia = toDate(LOCALDATE_2001_02_01);
		Date dataPagamento = toDate(contaCartao.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2001_03_01;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaIgualFechamentoIgualPagamento() {
		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		contaCartao.setCartaoDiaPagamento(DAY_OF_MONTH_2);

		Date dataReferencia = toDate(LOCALDATE_2000_01_02);
		Date dataPagamento = toDate(contaCartao.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_01_02;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}

	@Test
	public void testDataPagamento_referenciaIgualFechamentoMenorQuePagamento() {
		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		contaCartao.setCartaoDiaPagamento(DAY_OF_MONTH_3);

		Date dataReferencia = toDate(LOCALDATE_2000_01_02);
		Date dataPagamento = toDate(contaCartao.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_01_03;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaIgualFechamentoMaiorQuePagamento() {
		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DAY_OF_MONTH_3);
		contaCartao.setCartaoDiaPagamento(DAY_OF_MONTH_2);

		Date dataReferencia = toDate(LOCALDATE_2000_01_03);
		Date dataPagamento = toDate(contaCartao.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_02_02;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaMenorQueFechamentoIgualPagamento() {
		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DAY_OF_MONTH_3);
		contaCartao.setCartaoDiaPagamento(DAY_OF_MONTH_3);

		Date dataReferencia = toDate(LOCALDATE_2000_01_01);
		Date dataPagamento = toDate(contaCartao.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_01_03;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaMaiorQueFechamentoIgualPagamento() {
		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		contaCartao.setCartaoDiaPagamento(DAY_OF_MONTH_2);

		Date dataReferencia = toDate(LOCALDATE_2000_01_03);
		Date dataPagamento = toDate(contaCartao.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_02_02;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaMenorQueFechamentoMenorQuePagamento() {
		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		contaCartao.setCartaoDiaPagamento(DAY_OF_MONTH_3);

		Date dataReferencia = toDate(LOCALDATE_2000_01_01);
		Date dataPagamento = toDate(contaCartao.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_01_03;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}

	@Test
	public void testDataPagamento_referenciaMenorQueFechamentoMaiorQuePagamento() {
		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DAY_OF_MONTH_3);
		contaCartao.setCartaoDiaPagamento(DAY_OF_MONTH_2);

		Date dataReferencia = toDate(LOCALDATE_2000_01_01);
		Date dataPagamento = toDate(contaCartao.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_02_02;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}

	@Test
	public void testDataPagamento_referenciaMaiorQueFechamentoMenorQuePagamento() {
		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		contaCartao.setCartaoDiaPagamento(DAY_OF_MONTH_3);

		Date dataReferencia = toDate(LOCALDATE_2000_01_03);
		Date dataPagamento = toDate(contaCartao.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_02_03;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaMaiorQueFechamentoMaiorQuePagamento() {
		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		contaCartao.setCartaoDiaPagamento(DAY_OF_MONTH_1);

		Date dataReferencia = toDate(LOCALDATE_2000_01_03);
		Date dataPagamento = toDate(contaCartao.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_03_01;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}



}
