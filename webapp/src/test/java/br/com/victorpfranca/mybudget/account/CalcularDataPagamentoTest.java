package br.com.victorpfranca.mybudget.account;

import static br.com.victorpfranca.mybudget.LocalDateConverter.fromDate;
import static br.com.victorpfranca.mybudget.LocalDateConverter.toDate;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Date;

import org.junit.Test;

import br.com.victorpfranca.mybudget.account.CreditCardAccount;

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
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		creditCardAccount.setCartaoDiaPagamento(DAY_OF_MONTH_31);

		Date dataReferencia = toDate(LOCALDATE_2000_04_01);
		Date dataPagamento = toDate(creditCardAccount.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_05_01;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_pagamentoDia29_MesFevereiro_AnoNaoBissexto() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		creditCardAccount.setCartaoDiaPagamento(DAY_OF_MONTH_29);

		Date dataReferencia = toDate(LOCALDATE_2001_02_01);
		Date dataPagamento = toDate(creditCardAccount.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2001_03_01;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaIgualFechamentoIgualPagamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		creditCardAccount.setCartaoDiaPagamento(DAY_OF_MONTH_2);

		Date dataReferencia = toDate(LOCALDATE_2000_01_02);
		Date dataPagamento = toDate(creditCardAccount.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_01_02;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}

	@Test
	public void testDataPagamento_referenciaIgualFechamentoMenorQuePagamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		creditCardAccount.setCartaoDiaPagamento(DAY_OF_MONTH_3);

		Date dataReferencia = toDate(LOCALDATE_2000_01_02);
		Date dataPagamento = toDate(creditCardAccount.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_01_03;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaIgualFechamentoMaiorQuePagamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_3);
		creditCardAccount.setCartaoDiaPagamento(DAY_OF_MONTH_2);

		Date dataReferencia = toDate(LOCALDATE_2000_01_03);
		Date dataPagamento = toDate(creditCardAccount.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_02_02;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaMenorQueFechamentoIgualPagamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_3);
		creditCardAccount.setCartaoDiaPagamento(DAY_OF_MONTH_3);

		Date dataReferencia = toDate(LOCALDATE_2000_01_01);
		Date dataPagamento = toDate(creditCardAccount.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_01_03;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaMaiorQueFechamentoIgualPagamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		creditCardAccount.setCartaoDiaPagamento(DAY_OF_MONTH_2);

		Date dataReferencia = toDate(LOCALDATE_2000_01_03);
		Date dataPagamento = toDate(creditCardAccount.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_02_02;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaMenorQueFechamentoMenorQuePagamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		creditCardAccount.setCartaoDiaPagamento(DAY_OF_MONTH_3);

		Date dataReferencia = toDate(LOCALDATE_2000_01_01);
		Date dataPagamento = toDate(creditCardAccount.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_01_03;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}

	@Test
	public void testDataPagamento_referenciaMenorQueFechamentoMaiorQuePagamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_3);
		creditCardAccount.setCartaoDiaPagamento(DAY_OF_MONTH_2);

		Date dataReferencia = toDate(LOCALDATE_2000_01_01);
		Date dataPagamento = toDate(creditCardAccount.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_02_02;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}

	@Test
	public void testDataPagamento_referenciaMaiorQueFechamentoMenorQuePagamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		creditCardAccount.setCartaoDiaPagamento(DAY_OF_MONTH_3);

		Date dataReferencia = toDate(LOCALDATE_2000_01_03);
		Date dataPagamento = toDate(creditCardAccount.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_02_03;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}
	
	@Test
	public void testDataPagamento_referenciaMaiorQueFechamentoMaiorQuePagamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_2);
		creditCardAccount.setCartaoDiaPagamento(DAY_OF_MONTH_1);

		Date dataReferencia = toDate(LOCALDATE_2000_01_03);
		Date dataPagamento = toDate(creditCardAccount.getDataPagamentoProximo(dataReferencia));
		LocalDate dataPagamentoEsperada = LOCALDATE_2000_03_01;

		assertEquals(dataPagamentoEsperada, fromDate(dataPagamento));
	}



}
