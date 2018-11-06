package br.com.victorpfranca.mybudget.account;

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

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.account.BankAccount;
import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceFixer;
import br.com.victorpfranca.mybudget.lancamento.LancamentoContaCorrente;
import br.com.victorpfranca.mybudget.view.AnoMes;

@RunWith(Parameterized.class)
public class ReconstrutorSaldoContasTest {

	@Parameter(0)
	public CheckingAccount contaInput;

	@Parameter(1)
	public List<LancamentoContaCorrente> lancamentosInput;

	@Parameter(2)
	public Map<AnoMes, AccountBalance> saldosExpected;

	@Parameters
	public static Collection<Object[]> data() {

		CheckingAccount contaInput = new BankAccount();

		int ano2000 = 2000;
		int dia1 = 1;

		// Input 1(sem lançamento)
		List<LancamentoContaCorrente> lancamentosInput1 = new ArrayList<LancamentoContaCorrente>();
		Map<AnoMes, AccountBalance> saldosExpected1 = new LinkedHashMap<AnoMes, AccountBalance>();

		// Input 2
		List<LancamentoContaCorrente> lancamentosInput2 = new ArrayList<LancamentoContaCorrente>();
		addLancamento(InOut.S, ano2000, Month.JANUARY, dia1, BigDecimal.TEN, lancamentosInput2);
		addLancamento(InOut.E, ano2000, Month.OCTOBER, dia1, BigDecimal.TEN, lancamentosInput2);
		addLancamento(InOut.S, ano2000, Month.NOVEMBER, dia1, BigDecimal.TEN, lancamentosInput2);
		addLancamento(InOut.S, ano2000, Month.DECEMBER, dia1, BigDecimal.TEN, lancamentosInput2);
		addLancamento(InOut.S, ano2000, Month.DECEMBER, dia1, BigDecimal.TEN, lancamentosInput2);

		Map<AnoMes, AccountBalance> saldosExpected2 = new LinkedHashMap<AnoMes, AccountBalance>();
		saldosExpected2.put(new AnoMes(ano2000, Month.JANUARY.getValue()),
				new AccountBalance(contaInput, ano2000, Month.JANUARY.getValue(), BigDecimal.TEN.negate()));

		saldosExpected2.put(new AnoMes(ano2000, Month.OCTOBER.getValue()),
				new AccountBalance(contaInput, ano2000, Month.OCTOBER.getValue(), BigDecimal.ZERO));

		saldosExpected2.put(new AnoMes(ano2000, Month.NOVEMBER.getValue()),
				new AccountBalance(contaInput, ano2000, Month.NOVEMBER.getValue(), BigDecimal.TEN.negate()));

		saldosExpected2.put(new AnoMes(ano2000, Month.DECEMBER.getValue()),
				new AccountBalance(contaInput, ano2000, Month.DECEMBER.getValue(), new BigDecimal(30).negate()));

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

		AccountBalanceFixer accountBalanceFixer = new AccountBalanceFixer();

		Map<AnoMes, AccountBalance> saldosGerados = accountBalanceFixer.reconstruirSaldosContasDoInicio(contaInput,
				lancamentosInput);

		assertTrue(saldosGerados.keySet().size() == saldosExpected.keySet().size());
		assertTrue(saldosGerados.values().size() == saldosExpected.values().size());

		for (Iterator<AnoMes> iterator = saldosExpected.keySet().iterator(); iterator.hasNext();) {
			AnoMes anoMesExpected = (AnoMes) iterator.next();

			AccountBalance saldoContaGerado = saldosGerados.get(anoMesExpected);
			AccountBalance saldoContaExpected = saldosExpected.get(anoMesExpected);

			assertTrue(saldoContaGerado.getValor().compareTo(saldoContaExpected.getValor()) == 0);
		}

	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

}
