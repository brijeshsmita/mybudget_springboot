package br.com.victorpfranca.mybudget.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.account.BankAccount;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;

@RunWith(Parameterized.class)
public class CriarLancamentoContaCorrenteTest {

	@Parameter(0)
	public BigDecimal valorLancamentoInput;

	@Parameter(1)
	public Date dataLancamentoInput;

	@Parameter(2)
	public Integer anoLancamentoExpected;

	@Parameter(3)
	public Integer mesLancamentoExpected;

	@Parameter(4)
	public InOut inOutInput;

	@Parameter(5)
	public List<Object[]> saldosExpected;

	@Parameters
	public static Collection<Object[]> data() {

		int ano = 2018;
		int dia1 = 1;
		int dia31 = 31;

		Object[][] saldos1 = new Object[][] { { ano, Month.JANUARY.getValue(), BigDecimal.TEN.negate() } };

		Object[][] saldos2 = new Object[][] { { ano, Month.FEBRUARY.getValue(), BigDecimal.TEN.negate() } };

		Object[][] saldos3 = new Object[][] { { ano, Month.DECEMBER.getValue(), BigDecimal.TEN.negate() } };

		Object[][] saldos4 = new Object[][] { { ano, Month.JANUARY.getValue(), BigDecimal.TEN } };

		Object[][] saldos5 = new Object[][] { { ano, Month.FEBRUARY.getValue(), BigDecimal.TEN } };

		Object[][] saldos6 = new Object[][] { { ano, Month.DECEMBER.getValue(), BigDecimal.TEN } };

		Object[][] data = new Object[][] {
				{ BigDecimal.TEN, toDate(ano, Month.JANUARY, dia1), ano, Month.JANUARY.getValue(), InOut.S,
						Arrays.asList(saldos1) },
				{ BigDecimal.TEN, toDate(ano, Month.JANUARY, dia31), ano, Month.JANUARY.getValue(), InOut.S,
						Arrays.asList(saldos1) },
				{ BigDecimal.TEN, toDate(ano, Month.FEBRUARY, dia1), ano, Month.FEBRUARY.getValue(), InOut.S,
						Arrays.asList(saldos2) },
				{ BigDecimal.TEN, toDate(ano, Month.DECEMBER, dia1), ano, Month.DECEMBER.getValue(), InOut.S,
						Arrays.asList(saldos3) },

				{ BigDecimal.TEN, toDate(ano, Month.JANUARY, dia1), ano, Month.JANUARY.getValue(), InOut.E,
						Arrays.asList(saldos4) },
				{ BigDecimal.TEN, toDate(ano, Month.JANUARY, dia31), ano, Month.JANUARY.getValue(), InOut.E,
						Arrays.asList(saldos4) },
				{ BigDecimal.TEN, toDate(ano, Month.FEBRUARY, dia1), ano, Month.FEBRUARY.getValue(), InOut.E,
						Arrays.asList(saldos5) },
				{ BigDecimal.TEN, toDate(ano, Month.DECEMBER, dia1), ano, Month.DECEMBER.getValue(), InOut.E,
						Arrays.asList(saldos6) } };

		return Arrays.asList(data);

	}

	private LancamentoRulesFacadeMock lancamentoRulesFacade;

	@Before
	public void init() {
		this.lancamentoRulesFacade = LancamentoRulesFacadeMock.build();
	}

	@Test
	public void shouldCreateLancamentoESaldos() {

		CheckingAccountTransaction lancamento = newLancamento();
		try {
			lancamento = (CheckingAccountTransaction) lancamentoRulesFacade.saveLancamentoContaCorrente(lancamento);
		} catch (TransactionMonthUpdatedException | NullableAccountException | AccountTypeException
				| IncompatibleCategoriesException | InvalidTransactionValueException e) {
			fail();
		}

		// - checar ano/mes do lancamento gerado
		assertEquals("Ano", anoLancamentoExpected, lancamento.getAno());
		assertEquals("Mes", mesLancamentoExpected, lancamento.getMes());

		List<AccountBalance> saldos = lancamentoRulesFacade.getSaldos();
		assertEquals(saldos.size(), saldosExpected.size());
		for (int i = 0; i < saldosExpected.size(); i++) {
			AccountBalance saldo = saldos.get(i);

			// - checar ano/mes do lancamento alterado
			assertEquals("Ano", saldosExpected.get(i)[0], saldo.getAno());
			assertEquals("Mes", saldosExpected.get(i)[1], saldo.getMes());

			// - checar saldos contas após alteração
			assertEquals("Valor", saldosExpected.get(i)[2], saldo.getValor());
		}

	}

	private CheckingAccountTransaction newLancamento() {
		Account contaBanco = new BankAccount();
		contaBanco.setNome("Account Banco");

		Category categoriaDespesa = new Category();
		categoriaDespesa.setNome("despesa 1");
		categoriaDespesa.setInOut(inOutInput);

		return new TransactionBuilder().data(dataLancamentoInput).inOut(inOutInput).category(categoriaDespesa)
				.account(contaBanco).status(TransactionStatus.NAO_CONFIRMADO).valor(valorLancamentoInput)
				.buildLancamentoContaCorrente();

	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

}
