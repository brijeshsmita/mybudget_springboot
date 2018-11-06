package br.com.victorpfranca.mybudget.lancamento;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.account.BankAccount;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoContaCorrente;
import br.com.victorpfranca.mybudget.lancamento.LancamentoFrequencia;
import br.com.victorpfranca.mybudget.lancamento.SerieLancamento;

public class CalcularDatasSerieLancamentosTest {

	private Lancamento lancamento;

	private Date DATA_LANCAMENTO_2000_01_01 = LocalDateConverter.toDate(LocalDate.of(2000, 1, 1));
	private Date DATA_LANCAMENTO_2000_01_31 = LocalDateConverter.toDate(LocalDate.of(2000, 1, 31));

	private Date DATA_LANCAMENTO_2001_01_28 = LocalDateConverter.toDate(LocalDate.of(2001, 1, 28));
	private Date DATA_LANCAMENTO_2001_01_29 = LocalDateConverter.toDate(LocalDate.of(2001, 1, 29));
	private Date DATA_LANCAMENTO_2001_01_31 = LocalDateConverter.toDate(LocalDate.of(2001, 1, 31));

	private Date DATA_LIMITE_2000 = LocalDateConverter.toDate(LocalDate.of(2000, 12, 31));
	private Date DATA_LIMITE_2001 = LocalDateConverter.toDate(LocalDate.of(2001, 12, 31));

	@Before
	public void init() {
		lancamento = new LancamentoContaCorrente();

		Category category = new Category();
		category.setInOut(InOut.S);
		category.setNome("category teste");

		BankAccount conta = new BankAccount();
		conta.setNome("Banco");

		lancamento.setInOut(InOut.S);
		lancamento.setCategory(category);
		lancamento.setAccount(conta);
		lancamento.setValor(BigDecimal.ZERO);

	}

	@Test
	public void testCriarSerieSemanal() {
		SerieLancamento serie = new SerieLancamento();
		serie.setFrequencia(LancamentoFrequencia.SEMANAL);
		serie.setDataInicio(DATA_LANCAMENTO_2000_01_01);
		serie.setDataLimite(DATA_LIMITE_2000);

		lancamento.setData(DATA_LANCAMENTO_2000_01_01);
		List<Lancamento> lancamentos = serie.gerarLancamentos(lancamento);
		for (int i = 0; i < lancamentos.size(); i++) {
			assertTrue(lancamentos.get(i).getData().equals(toDate(LANCAMENTO_2000_01_01_SEMANAL[i])));
		}

		serie.setDataInicio(DATA_LANCAMENTO_2000_01_31);
		lancamento.setData(DATA_LANCAMENTO_2000_01_31);
		lancamentos = serie.gerarLancamentos(lancamento);
		for (int i = 0; i < lancamentos.size(); i++) {
			assertTrue(lancamentos.get(i).getData().equals(toDate(LANCAMENTO_2000_01_31_SEMANAL[i])));
		}

	}

	@Test
	public void testCriarSerieQuinzenal() {
		SerieLancamento serie = new SerieLancamento();
		serie.setFrequencia(LancamentoFrequencia.QUINZENAL);
		serie.setDataInicio(DATA_LANCAMENTO_2000_01_01);
		serie.setDataLimite(DATA_LIMITE_2000);

		lancamento.setData(DATA_LANCAMENTO_2000_01_01);

		List<Lancamento> lancamentos = serie.gerarLancamentos(lancamento);
		for (int i = 0; i < lancamentos.size(); i++) {
			assertTrue(lancamentos.get(i).getData().equals(toDate(LANCAMENTO_2000_01_01_QUINZENAL[i])));
		}

		serie.setDataInicio(DATA_LANCAMENTO_2000_01_31);
		lancamento.setData(DATA_LANCAMENTO_2000_01_31);
		lancamentos = serie.gerarLancamentos(lancamento);
		for (int i = 0; i < lancamentos.size(); i++) {
			assertTrue(lancamentos.get(i).getData().equals(toDate(LANCAMENTO_2000_01_31_QUINZENAL[i])));
		}
	}

	@Test
	public void testCriarSerieMensal() {
		SerieLancamento serie = new SerieLancamento();
		serie.setFrequencia(LancamentoFrequencia.MENSAL);

		serie.setDataInicio(DATA_LANCAMENTO_2000_01_01);
		serie.setDataLimite(DATA_LIMITE_2000);

		lancamento.setData(DATA_LANCAMENTO_2000_01_01);
		List<Lancamento> lancamentos = serie.gerarLancamentos(lancamento);
		for (int i = 0; i < lancamentos.size(); i++) {
			assertTrue(lancamentos.get(i).getData().equals(toDate(LANCAMENTO_2000_01_01_MENSAL[i])));
		}

		serie.setDataInicio(DATA_LANCAMENTO_2000_01_31);
		serie.setDataLimite(DATA_LIMITE_2000);
		lancamento.setData(DATA_LANCAMENTO_2000_01_31);
		lancamentos = serie.gerarLancamentos(lancamento);
		for (int i = 0; i < lancamentos.size(); i++) {
			assertTrue(lancamentos.get(i).getData().equals(toDate(LANCAMENTO_2000_01_31_MENSAL[i])));
		}

		serie.setDataInicio(DATA_LANCAMENTO_2001_01_28);
		serie.setDataLimite(DATA_LIMITE_2001);
		lancamento.setData(DATA_LANCAMENTO_2001_01_28);
		lancamentos = serie.gerarLancamentos(lancamento);
		for (int i = 0; i < lancamentos.size(); i++) {
			assertTrue(lancamentos.get(i).getData().equals(toDate(LANCAMENTO_2001_01_28_MENSAL[i])));
		}

		serie.setDataInicio(DATA_LANCAMENTO_2001_01_29);
		serie.setDataLimite(DATA_LIMITE_2001);
		lancamento.setData(DATA_LANCAMENTO_2001_01_29);
		lancamentos = serie.gerarLancamentos(lancamento);
		for (int i = 0; i < lancamentos.size(); i++) {
			assertTrue(lancamentos.get(i).getData().equals(toDate(LANCAMENTO_2001_01_29_MENSAL[i])));
		}

		serie.setDataInicio(DATA_LANCAMENTO_2001_01_31);
		serie.setDataLimite(DATA_LIMITE_2001);
		lancamento.setData(DATA_LANCAMENTO_2001_01_31);
		lancamentos = serie.gerarLancamentos(lancamento);
		for (int i = 0; i < lancamentos.size(); i++) {
			assertTrue(lancamentos.get(i).getData().equals(toDate(LANCAMENTO_2001_01_31_MENSAL[i])));
		}

	}

	private Date toDate(String dateString) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dddd");
		Date date = null;
		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
		return date;
	}

	public static String[] LANCAMENTO_2000_01_01_SEMANAL = { "2000-01-01", "2000-01-08", "2000-01-15", "2000-01-22",
			"2000-01-29", "2000-02-05", "2000-02-12", "2000-02-19", "2000-02-26", "2000-03-04", "2000-03-11",
			"2000-03-18", "2000-03-25", "2000-04-01", "2000-04-08", "2000-04-15", "2000-04-22", "2000-04-29",
			"2000-05-06", "2000-05-13", "2000-05-20", "2000-05-27", "2000-06-03", "2000-06-10", "2000-06-17",
			"2000-06-24", "2000-07-01", "2000-07-08", "2000-07-15", "2000-07-22", "2000-07-29", "2000-08-05",
			"2000-08-12", "2000-08-19", "2000-08-26", "2000-09-02", "2000-09-09", "2000-09-16", "2000-09-23",
			"2000-09-30", "2000-10-07", "2000-10-14", "2000-10-21", "2000-10-28", "2000-11-04", "2000-11-11",
			"2000-11-18", "2000-11-25", "2000-12-02", "2000-12-09", "2000-12-16", "2000-12-23", "2000-12-30" };

	public static String[] LANCAMENTO_2000_01_01_QUINZENAL = { "2000-01-01", "2000-01-15", "2000-01-29", "2000-02-12",
			"2000-02-26", "2000-03-11", "2000-03-25", "2000-04-08", "2000-04-22", "2000-05-06", "2000-05-20",
			"2000-06-03", "2000-06-17", "2000-07-01", "2000-07-15", "2000-07-29", "2000-08-12", "2000-08-26",
			"2000-09-09", "2000-09-23", "2000-10-07", "2000-10-21", "2000-11-04", "2000-11-18", "2000-12-02",
			"2000-12-16", "2000-12-30" };

	public static String[] LANCAMENTO_2000_01_01_MENSAL = { "2000-01-01", "2000-02-01", "2000-03-01", "2000-04-01",
			"2000-05-01", "2000-06-01", "2000-07-01", "2000-08-01", "2000-09-01", "2000-10-01", "2000-11-01",
			"2000-12-01" };

	public static String[] LANCAMENTO_2000_01_31_SEMANAL = { "2000-01-31", "2000-02-07", "2000-02-14", "2000-02-21",
			"2000-02-28", "2000-03-06", "2000-03-13", "2000-03-20", "2000-03-27", "2000-04-03", "2000-04-10",
			"2000-04-17", "2000-04-24", "2000-05-01", "2000-05-08", "2000-05-15", "2000-05-22", "2000-05-29",
			"2000-06-05", "2000-06-12", "2000-06-19", "2000-06-26", "2000-07-03", "2000-07-10", "2000-07-17",
			"2000-07-24", "2000-07-31", "2000-08-07", "2000-08-14", "2000-08-21", "2000-08-28", "2000-09-04",
			"2000-09-11", "2000-09-18", "2000-09-25", "2000-10-02", "2000-10-09", "2000-10-16", "2000-10-23",
			"2000-10-30", "2000-11-06", "2000-11-13", "2000-11-20", "2000-11-27", "2000-12-04", "2000-12-11",
			"2000-12-18", "2000-12-25" };

	public static String[] LANCAMENTO_2000_01_31_QUINZENAL = { "2000-01-31", "2000-02-14", "2000-02-28", "2000-03-13",
			"2000-03-27", "2000-04-10", "2000-04-24", "2000-05-08", "2000-05-22", "2000-06-05", "2000-06-19",
			"2000-07-03", "2000-07-17", "2000-07-31", "2000-08-14", "2000-08-28", "2000-09-11", "2000-09-25",
			"2000-10-09", "2000-10-23", "2000-11-06", "2000-11-20", "2000-12-04", "2000-12-18" };

	public static String[] LANCAMENTO_2000_01_31_MENSAL = { "2000-01-31", "2000-02-29", "2000-03-31", "2000-04-30",
			"2000-05-31", "2000-06-30", "2000-07-31", "2000-08-31", "2000-09-30", "2000-10-31", "2000-11-30",
			"2000-12-31" };

	public static String[] LANCAMENTO_2001_01_28_MENSAL = { "2001-01-28", "2001-02-28", "2001-03-28", "2001-04-28",
			"2001-05-28", "2001-06-28", "2001-07-28", "2001-08-28", "2001-09-28", "2001-10-28", "2001-11-28",
			"2001-12-28" };

	public static String[] LANCAMENTO_2001_01_29_MENSAL = { "2001-01-29", "2001-02-28", "2001-03-29", "2001-04-29",
			"2001-05-29", "2001-06-29", "2001-07-29", "2001-08-29", "2001-09-29", "2001-10-29", "2001-11-29",
			"2001-12-29" };

	public static String[] LANCAMENTO_2001_01_31_MENSAL = { "2001-01-31", "2001-02-28", "2001-03-31", "2001-04-30",
			"2001-05-31", "2001-06-30", "2001-07-31", "2001-08-31", "2001-09-30", "2001-10-31", "2001-11-30",
			"2001-12-31" };

	// public static void main(String[] args) {
	// LocalDate date = LocalDate.of(2000, 1, 31);
	// int fator = 1;
	// while (date.isBefore(LocalDate.of(2001, 1, 1))) {
	// System.out.print("\"" + date + "\",");
	// date = date.plusWeeks(2 * fator);
	// }
	// }

}
