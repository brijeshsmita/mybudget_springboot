package br.com.victorpfranca.mybudget.transaction;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.BankAccount;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;

@RunWith(Parameterized.class)

public class ValidarLancamentoTest {

	private static final int YEAR_2000 = 2000;

	@Parameter(0)
	public boolean isFaturaInput;

	@Parameter(1)
	public boolean isSaldoInicialInput;

	@Parameter(2)
	public BigDecimal valorLancamentoInput;

	@Parameter(3)
	public InOut inOutLancamentoInput;

	@Parameter(4)
	public InOut inOutCategoriaInput;

	@Parameter(5)
	public Date dataInput;

	@Parameter(6)
	public Date dataAnteriorInput;

	@Parameter(7)
	public Account contaInput;

	@Parameter(8)
	public Account contaAnteriorInput;

	@Parameter(9)
	public boolean failExpected;

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ false, false, BigDecimal.TEN, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new BankAccount(), null, false },
				{ false, false, BigDecimal.TEN, InOut.S, InOut.E, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new BankAccount(), null, true },
				{ false, false, BigDecimal.TEN, InOut.E, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new BankAccount(), null, true },
				{ false, false, BigDecimal.TEN, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.FEBRUARY, 1), new BankAccount(), null, true },
				{ false, false, BigDecimal.TEN, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new BankAccount(), new CreditCardAccount(), true },
				{ false, false, BigDecimal.TEN, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new BankAccount(), new BankAccount(), false },
				{ false, false, BigDecimal.ZERO, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new BankAccount(), new BankAccount(), true },
				{ false, false, BigDecimal.ONE.negate(), InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(YEAR_2000, Month.JANUARY, 1), new BankAccount(), new BankAccount(), true },

				{ false, true, BigDecimal.ONE.negate(), InOut.E, null, toDate(2000, Month.JANUARY, 1),
						toDate(YEAR_2000, Month.JANUARY, 1), new BankAccount(), new BankAccount(), false },

				{ true, false, BigDecimal.ZERO, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(YEAR_2000, Month.JANUARY, 1), new BankAccount(), new BankAccount(), false },
				{ true, false, BigDecimal.ONE.negate(), InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(YEAR_2000, Month.JANUARY, 1), new BankAccount(), new BankAccount(), true },
				{ true, false, BigDecimal.ONE, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(YEAR_2000, Month.JANUARY, 1), new BankAccount(), new BankAccount(), false } };

		return Arrays.asList(data);
	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

	@Test
	public void shouldValidateLancamento() {
		Transaction transaction = newLancamento();

		try {
			transaction.validar();
			if (failExpected) {
				fail();
			}
		} catch (IncompatibleCategoriesException | TransactionMonthUpdatedException | NullableAccountException
				| AccountTypeException | InvalidTransactionValueException e) {
			if (!failExpected) {
				fail();
			}
		}
	}

	private Transaction newLancamento() {
		Transaction transaction = new CheckingAccountTransaction();

		transaction.setValor(valorLancamentoInput);

		transaction.setInOut(inOutLancamentoInput);

		if (!isSaldoInicialInput) {
			Category category = new Category();
			category.setInOut(inOutCategoriaInput);
			transaction.setCategoria(category);
		} else {
			((CheckingAccountTransaction) transaction).setSaldoInicial(true);
		}

		if (isFaturaInput) {
			((CheckingAccountTransaction) transaction).setFaturaCartao(true);
		}

		transaction.setData(dataInput);
		transaction.setDataAnterior(dataAnteriorInput);

		transaction.setConta(contaInput);
		transaction.setContaAnterior(contaAnteriorInput);

		return transaction;
	}

}
