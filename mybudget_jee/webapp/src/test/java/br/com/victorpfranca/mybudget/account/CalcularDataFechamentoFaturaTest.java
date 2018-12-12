package br.com.victorpfranca.mybudget.account;

import static br.com.victorpfranca.mybudget.LocalDateConverter.fromDate;
import static br.com.victorpfranca.mybudget.LocalDateConverter.toDate;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Date;

import org.junit.Test;

public class CalcularDataFechamentoFaturaTest {

	private LocalDate LOCALDATE_2000_01_01 = LocalDate.of(2000, 1, 1);
	private LocalDate LOCALDATE_2000_01_02 = LocalDate.of(2000, 1, 2);
	
	private LocalDate LOCALDATE_2000_02_28 = LocalDate.of(2000, 2, 28);
	private LocalDate LOCALDATE_2000_02_29 = LocalDate.of(2000, 2, 29);

	private LocalDate LOCALDATE_2001_02_28 = LocalDate.of(2001, 2, 28);
	private LocalDate LOCALDATE_2001_03_01 = LocalDate.of(2001, 3, 1);
	
	private LocalDate LOCALDATE_2000_04_30 = LocalDate.of(2000, 4, 30);
	private LocalDate LOCALDATE_2000_05_01 = LocalDate.of(2000, 5, 1);

	private int DAY_OF_MONTH_1 = 1;
	private int DAY_OF_MONTH_2 = 2;
	private int DAY_OF_MONTH_29 = 29;
	private int DAY_OF_MONTH_31 = 31;
	
	@Test
	public void testFechamentoProximo_fechamentoDia31_Mes30Dias() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_31);

		Date dataReferencia = toDate(LOCALDATE_2000_04_30);
		Date dataFechamentoProximo = creditCardAccount.getDataFechamentoProximo(dataReferencia);

		LocalDate dataFechamentoProximoEsperada = LOCALDATE_2000_05_01;

		assertEquals(dataFechamentoProximoEsperada, fromDate(dataFechamentoProximo));
	}
	
	@Test
	public void testFechamentoProximo_fechamentoDia29_MesFevereiro_AnoNaoBissexto() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_29);

		Date dataReferencia = toDate(LOCALDATE_2001_02_28);
		Date dataFechamentoProximo = creditCardAccount.getDataFechamentoProximo(dataReferencia);

		LocalDate dataFechamentoProximoEsperada = LOCALDATE_2001_03_01;

		assertEquals(dataFechamentoProximoEsperada, fromDate(dataFechamentoProximo));
	}

	@Test
	public void testFechamentoProximo_fechamentoDia29_MesFevereiro_AnoBissexto() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_29);

		Date dataReferencia = toDate(LOCALDATE_2000_02_28);
		Date dataFechamentoProximo = creditCardAccount.getDataFechamentoProximo(dataReferencia);

		LocalDate dataFechamentoProximoEsperada = LOCALDATE_2000_02_29; 

		assertEquals(dataFechamentoProximoEsperada, fromDate(dataFechamentoProximo));
	}

	@Test
	public void testFechamentoProximo_referenciaIgualFechamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_1);

		Date dataReferencia = toDate(LOCALDATE_2000_01_01);
		Date dataFechamentoProximo = creditCardAccount.getDataFechamentoProximo(dataReferencia);
		LocalDate dataFechamentoProximoEsperada = LOCALDATE_2000_01_01;

		assertEquals(dataFechamentoProximoEsperada, fromDate(dataFechamentoProximo));
	}

	@Test
	public void testFechamentoProximo_referenciaMenorFechamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_2);

		Date dataReferencia = toDate(LOCALDATE_2000_01_01);
		Date dataFechamentoProximo = creditCardAccount.getDataFechamentoProximo(dataReferencia);

		LocalDate dataFechamentoProximoEsperada = LOCALDATE_2000_01_02;

		assertEquals(dataFechamentoProximoEsperada, fromDate(dataFechamentoProximo));
	}

	@Test
	public void testFechamentoProximo_referenciaMaiorFechamento() {
		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DAY_OF_MONTH_1);

		Date dataReferencia = toDate(LOCALDATE_2000_01_02);
		Date dataFechamentoProximo = creditCardAccount.getDataFechamentoProximo(dataReferencia);

		LocalDate dataFechamentoProximoEsperada = LOCALDATE_2000_01_01.plusMonths(1);

		assertEquals(dataFechamentoProximoEsperada, fromDate(dataFechamentoProximo));
	}
	
}
