package br.com.victorpfranca.mybudget.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;

@RunWith(Parameterized.class)
public class CriarLancamentoTransferenciaTest {

	@Parameter(0)
	public BigDecimal valorLancamentoInput;

	@Parameter(1)
	public Date dataLancamentoInput;

	@Parameter(2)
	public InOut inOutInput;

	@Parameter(3)
	public Integer contaOrigemIdInput;

	@Parameter(4)
	public Integer contaDestinoIdInput;

	@Parameter(5)
	public List<Object[]> saldosExpected;

	@Parameters
	public static Collection<Object[]> data() {

		int ano = 2018;
		int dia1 = 1;

		int contaOrigem = 1;
		int contaDestino = 2;

		Object[][] saldos1 = new Object[][] {
				{ ano, Month.JANUARY.getValue(), BigDecimal.TEN.negate(), BigDecimal.TEN } };

		Object[][] data = new Object[][] { { BigDecimal.TEN, toDate(ano, Month.JANUARY, dia1), InOut.S, contaOrigem,
				contaDestino, Arrays.asList(saldos1) } };

		return Arrays.asList(data);

	}

	private LancamentoRulesFacadeMock lancamentoRulesFacade;

	@Before
	public void init() {
		this.lancamentoRulesFacade = LancamentoRulesFacadeMock.build();
	}

	@Test
	public void shouldCreateLancamentoESaldos() {

		CheckingAccountTransaction lancamentoOrigem = newLancamento();

		try {
			lancamentoRulesFacade.saveLancamentoTransferencia(lancamentoOrigem);
		} catch (TransactionMonthUpdatedException | NullableAccountException | AccountTypeException
				| IncompatibleCategoriesException | InvalidTransactionValueException e) {
			fail();
		}

		List<AccountBalance> saldos = lancamentoRulesFacade.getSaldos();
		assertEquals(saldos.size(), (2 * saldosExpected.size()));

		for (int i = 0; i < (saldosExpected.size()); i++) {
			AccountBalance saldoContaOrigem = saldos.get(i);

			//Checar saldo account origem
			assertEquals("Account Origem", contaOrigemIdInput, saldoContaOrigem.getAccount().getId());
			assertEquals("Ano", saldosExpected.get(i)[0], saldoContaOrigem.getAno());
			assertEquals("Mes", saldosExpected.get(i)[1], saldoContaOrigem.getMes());
			assertEquals("Valor", saldosExpected.get(i)[2], saldoContaOrigem.getValor());

			//Checar saldo account destino
			AccountBalance saldoContaDestino = saldos.get(i + 1);
			assertEquals("Account Origem", contaDestinoIdInput, saldoContaDestino.getAccount().getId());
			assertEquals("Ano", saldosExpected.get(i)[0], saldoContaDestino.getAno());
			assertEquals("Mes", saldosExpected.get(i)[1], saldoContaDestino.getMes());
			assertEquals("Valor", saldosExpected.get(i)[3], saldoContaDestino.getValor());
		}

		List<Transaction> transactions = lancamentoRulesFacade.getLancamentos();
		assertEquals(transactions.size(), 2);

		// Checar lancamento account origem
		assertEquals("Account", contaOrigemIdInput, transactions.get(0).getAccount().getId());
		assertNull("Account Origem", ((CheckingAccountTransaction) transactions.get(0)).getContaOrigem());
		assertEquals("Account Destino", contaDestinoIdInput,
				((CheckingAccountTransaction) transactions.get(0)).getContaDestino().getId());
		assertEquals("InOut", InOut.S, transactions.get(0).getInOut());
		assertEquals("Valor", valorLancamentoInput, transactions.get(0).getValor());

		// Checar lancamento account destino
		assertEquals("Account", contaDestinoIdInput, transactions.get(1).getAccount().getId());
		assertEquals("Account Origem", contaOrigemIdInput,
				((CheckingAccountTransaction) transactions.get(1)).getContaOrigem().getId());
		assertNull("Account Destino", ((CheckingAccountTransaction) transactions.get(1)).getContaDestino());
		assertEquals("InOut", InOut.E, transactions.get(1).getInOut());
		assertEquals("Valor", valorLancamentoInput, transactions.get(1).getValor());

	}

	private CheckingAccountTransaction newLancamento() {

		Account contaOrigem = new Account().withId(contaOrigemIdInput).withUsuario(new User()).withNome("contaOrigem");
		Account contaDestino = new Account().withId(contaDestinoIdInput).withUsuario(new User())
				.withNome("contaDestino");
		;

		Transaction transaction = new TransactionBuilder().data(dataLancamentoInput).inOut(inOutInput)
				.status(TransactionStatus.NAO_CONFIRMADO).valor(valorLancamentoInput).buildLancamentoContaCorrente();

		transaction.setAccount(contaOrigem);
		((CheckingAccountTransaction) transaction).setContaDestino(contaDestino);

		return (CheckingAccountTransaction) transaction;

	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

}
