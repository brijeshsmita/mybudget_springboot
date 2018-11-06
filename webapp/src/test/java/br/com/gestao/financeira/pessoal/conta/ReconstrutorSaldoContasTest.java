package br.com.gestao.financeira.pessoal.conta;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.conta.rules.ReconstrutorSaldosContas;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoContaCorrente;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@RunWith(Parameterized.class)
public class ReconstrutorSaldoContasTest {

	@Parameter(0)
	public ContaCorrente contaInput;

	@Parameter(1)
	public List<LancamentoContaCorrente> lancamentosInput;

	@Parameter(2)
	public Map<AnoMes, SaldoConta> saldosExpected;

	@Parameters
	public static Collection<Object[]> data() {

		ContaCorrente contaInput = new ContaBanco();

		int ano2000 = 2000;
		int dia1 = 1;

		// Input 1(sem lançamento)
		List<LancamentoContaCorrente> lancamentosInput1 = new ArrayList<LancamentoContaCorrente>();
		Map<AnoMes, SaldoConta> saldosExpected1 = new LinkedHashMap<AnoMes, SaldoConta>();

		// Input 2
		List<LancamentoContaCorrente> lancamentosInput2 = new ArrayList<LancamentoContaCorrente>();
		addLancamento(InOut.S, ano2000, Month.JANUARY, dia1, BigDecimal.TEN, lancamentosInput2);
		addLancamento(InOut.E, ano2000, Month.OCTOBER, dia1, BigDecimal.TEN, lancamentosInput2);
		addLancamento(InOut.S, ano2000, Month.NOVEMBER, dia1, BigDecimal.TEN, lancamentosInput2);
		addLancamento(InOut.S, ano2000, Month.DECEMBER, dia1, BigDecimal.TEN, lancamentosInput2);
		addLancamento(InOut.S, ano2000, Month.DECEMBER, dia1, BigDecimal.TEN, lancamentosInput2);

		Map<AnoMes, SaldoConta> saldosExpected2 = new LinkedHashMap<AnoMes, SaldoConta>();
		saldosExpected2.put(new AnoMes(ano2000, Month.JANUARY.getValue()),
				new SaldoConta(contaInput, ano2000, Month.JANUARY.getValue(), BigDecimal.TEN.negate()));

		saldosExpected2.put(new AnoMes(ano2000, Month.OCTOBER.getValue()),
				new SaldoConta(contaInput, ano2000, Month.OCTOBER.getValue(), BigDecimal.ZERO));

		saldosExpected2.put(new AnoMes(ano2000, Month.NOVEMBER.getValue()),
				new SaldoConta(contaInput, ano2000, Month.NOVEMBER.getValue(), BigDecimal.TEN.negate()));

		saldosExpected2.put(new AnoMes(ano2000, Month.DECEMBER.getValue()),
				new SaldoConta(contaInput, ano2000, Month.DECEMBER.getValue(), new BigDecimal(30).negate()));

		Object[][] data = new Object[][] { { contaInput, lancamentosInput1, saldosExpected1 },
				{ contaInput, lancamentosInput2, saldosExpected2 } };

		return Arrays.asList(data);
	}

	private static void addLancamento(InOut inOut, int ano, Month mes, int dia, BigDecimal valor,
			List<LancamentoContaCorrente> lancamentos) {
		LancamentoContaCorrente lancamento = new LancamentoContaCorrente();
		lancamento.setInOut(inOut);
		lancamento.setData(toDate(ano, mes, dia));
		lancamento.setValor(valor);
		lancamentos.add(lancamento);
	}

	@Test
	public void shouldReturnSaldos() {

		ReconstrutorSaldosContas reconstrutorSaldosContas = new ReconstrutorSaldosContas();

		Map<AnoMes, SaldoConta> saldosGerados = reconstrutorSaldosContas.reconstruirSaldosContasDoInicio(contaInput,
				lancamentosInput);

		assertTrue(saldosGerados.keySet().size() == saldosExpected.keySet().size());
		assertTrue(saldosGerados.values().size() == saldosExpected.values().size());

		for (Iterator<AnoMes> iterator = saldosExpected.keySet().iterator(); iterator.hasNext();) {
			AnoMes anoMesExpected = (AnoMes) iterator.next();

			SaldoConta saldoContaGerado = saldosGerados.get(anoMesExpected);
			SaldoConta saldoContaExpected = saldosExpected.get(anoMesExpected);

			assertTrue(saldoContaGerado.getValor().compareTo(saldoContaExpected.getValor()) == 0);
		}

	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

}
