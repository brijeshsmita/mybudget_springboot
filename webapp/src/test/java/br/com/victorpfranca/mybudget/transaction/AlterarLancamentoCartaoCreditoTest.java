package br.com.victorpfranca.mybudget.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.account.BankAccount;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;

//@RunWith(Parameterized.class)
public class AlterarLancamentoCartaoCreditoTest {

	@Parameter(0)
	public BigDecimal valorLancamentoInput;

	@Parameter(1)
	public Integer qtdParcelasInput;

	@Parameter(2)
	public Integer qtdParcelasUpdateInput;

	@Parameter(3)
	public Date dataLancamentoInput;

	@Parameter(4)
	public Integer diaFechamentoFaturaInput;

	@Parameter(5)
	public Integer diaPagamentoFaturaInput;

	@Parameter(6)
	public Integer contaCartaoId;

	@Parameter(7)
	public Integer contaBancoId;

	@Parameter(8)
	public Integer anoLancamentoExpected;

	@Parameter(9)
	public Integer mesLancamentoExpected;

	@Parameter(10)
	public List<Object[]> saldosExpected;

	@Parameter(11)
	public List<Object[]> faturasExpected;

	@Parameter(12)
	public List<Object[]> faturasItensExpected;

	@Parameters
	public static Collection<Object[]> data() {

		int ano = 2018;
		Month MES_JANEIRO = Month.JANUARY;
		int dia1 = 1;
		int dia10 = 10;
		int dia20 = 20;

		Date dataFatura = toDate(ano, MES_JANEIRO, dia20);
		Date dataCompra = toDate(ano, MES_JANEIRO, dia1);

		Integer contaCartaoId1 = 1;
		Integer contaBancoId2 = 2;

		BigDecimal valorCompra10 = BigDecimal.TEN;
		BigDecimal valorCompra100 = valorCompra10.multiply(valorCompra10);

		BigDecimal valorFatura20 = BigDecimal.TEN.multiply(BigDecimal.valueOf(2));

		int qtdParcelas10 = 10;
		int qtdParcelas5 = 5;

		Object[][] saldos = new Object[][] { { ano, Month.JANUARY.getValue(), valorFatura20.negate() },
				{ ano, Month.FEBRUARY.getValue(), valorFatura20.negate() },
				{ ano, Month.MARCH.getValue(), valorFatura20.negate() },
				{ ano, Month.APRIL.getValue(), valorFatura20.negate() },
				{ ano, Month.MAY.getValue(), valorFatura20.negate() },
				{ ano, Month.JUNE.getValue(), valorFatura20.negate() },
				{ ano, Month.JULY.getValue(), valorFatura20.negate() },
				{ ano, Month.AUGUST.getValue(), valorFatura20.negate() },
				{ ano, Month.SEPTEMBER.getValue(), valorFatura20.negate() },
				{ ano, Month.OCTOBER.getValue(), valorFatura20.negate() },
				{ ano, Month.NOVEMBER.getValue(), valorFatura20.negate() },
				{ ano, Month.DECEMBER.getValue(), valorFatura20.negate() } };

		Object[][] faturas = new Object[][] { { dataFatura, valorFatura20 } };

		Object[][] faturasItens = new Object[][] { { dataFatura, valorFatura20 } };

		Object[][] data = new Object[][] { { valorCompra100, qtdParcelas10, qtdParcelas5, dataCompra, dia10, dia20,
				contaCartaoId1, contaBancoId2, ano, MES_JANEIRO.getValue(), Arrays.asList(saldos),
				Arrays.asList(faturas), Arrays.asList(faturasItens) } };

		return Arrays.asList(data);

	}

	private LancamentoRulesFacadeMock lancamentoRulesFacade;

	@Before
	public void init() {
		this.lancamentoRulesFacade = LancamentoRulesFacadeMock.build();
	}

//	@Test
	public void shouldUpdateLancamentoESaldos() {

		Transaction transaction = newLancamento();
		try {
			transaction = lancamentoRulesFacade.saveLancamentoCartaoDeCredito(transaction);
			((CreditCardTransaction) transaction).setQtdParcelas(5);
			transaction = lancamentoRulesFacade.saveLancamentoCartaoDeCredito(transaction);

		} catch (TransactionMonthUpdatedException | NullableAccountException | AccountTypeException
				| IncompatibleCategoriesException | InvalidTransactionValueException e) {
			fail();
		}

		// - checar ano/mes do lancamento gerado
		assertEquals("Ano", anoLancamentoExpected, transaction.getAno());
		assertEquals("Mes", mesLancamentoExpected, transaction.getMes());

		// Validação lançamento cartão
		List<CreditCardTransaction> lancamentosCartaoCredito = lancamentoRulesFacade.getLancamentosCartao();
		assertTrue("Qtd transactions cartao", lancamentosCartaoCredito.size() == 1);
		for (Iterator<CreditCardTransaction> iterator = lancamentosCartaoCredito.iterator(); iterator.hasNext();) {
			Transaction lancamentoCartao = iterator.next();
			lancamentoCartao.getData();

			assertTrue("Data", dataLancamentoInput.compareTo(lancamentoCartao.getData()) == 0);
			assertTrue("Ano", anoLancamentoExpected.compareTo(lancamentoCartao.getAno()) == 0);
			assertTrue("Mes", mesLancamentoExpected.compareTo(lancamentoCartao.getMes()) == 0);
			assertTrue("Valor", valorLancamentoInput.compareTo(lancamentoCartao.getValor()) == 0);
			assertTrue("InOut", InOut.S.compareTo(lancamentoCartao.getInOut()) == 0);
			assertTrue("Account", contaCartaoId.compareTo(lancamentoCartao.getAccount().getId()) == 0);
		}

		// Validação faturas
		List<CheckingAccountTransaction> lancamentosFatura = lancamentoRulesFacade.getLancamentosFaturas();
		assertTrue("Qtd faturas", lancamentosFatura.size() == qtdParcelasUpdateInput.intValue());
		int faturaI = 0;
		for (Iterator<CheckingAccountTransaction> iterator = lancamentosFatura.iterator(); iterator.hasNext();) {
			Transaction lancamentoFatura = iterator.next();

			Object[] faturaExpected = faturasExpected.get(0);

			assertTrue("Data", LocalDateConverter.plusMonth((Date) faturaExpected[0], faturaI)
					.compareTo(lancamentoFatura.getData()) == 0);
			assertTrue("Valor", ((BigDecimal) faturaExpected[1]).compareTo(lancamentoFatura.getValor()) == 0);
			assertTrue("InOut", InOut.S.compareTo(lancamentoFatura.getInOut()) == 0);
			assertTrue("Account", contaBancoId.compareTo(lancamentoFatura.getAccount().getId()) == 0);
			assertTrue("CreditCardAccount", contaCartaoId
					.compareTo(((CheckingAccountTransaction) lancamentoFatura).getCartaoCreditoFatura().getId()) == 0);
			assertTrue("isFatura", ((CheckingAccountTransaction) lancamentoFatura).isFaturaCartao());
			faturaI++;
		}

		// Validação fatura item
		List<CreditCardInvoiceTransactionItem> lancamentosFaturaCartaoItem = lancamentoRulesFacade
				.getLancamentosItensFatura();
		assertTrue("Qtd fatura item", lancamentosFaturaCartaoItem.size() == qtdParcelasUpdateInput);
		int itemFaturaI = 0;
		for (Iterator<CreditCardInvoiceTransactionItem> iterator = lancamentosFaturaCartaoItem.iterator(); iterator
				.hasNext();) {
			CreditCardInvoiceTransactionItem creditCardInvoiceTransactionItem = iterator.next();

			Object[] faturaItemExpected = faturasItensExpected.get(0);
			assertTrue("Data", LocalDateConverter.plusMonth((Date) faturaItemExpected[0], itemFaturaI)
					.compareTo(creditCardInvoiceTransactionItem.getData()) == 0);
			assertTrue("Valor",
					((BigDecimal) faturaItemExpected[1]).compareTo(creditCardInvoiceTransactionItem.getValor()) == 0);
			assertTrue("Qtd Parcelas", qtdParcelasInput.compareTo(creditCardInvoiceTransactionItem.getQtdParcelas()) == 0);
			assertTrue("Índice parcela",
					Integer.valueOf(itemFaturaI + 1).compareTo(creditCardInvoiceTransactionItem.getIndiceParcela()) == 0);
			itemFaturaI++;
		}

		// Validação saldos
		List<AccountBalance> saldos = lancamentoRulesFacade.getSaldos();
		assertEquals(saldos.size(), Month.values().length);
		for (int i = 0; i < Month.values().length; i++) {
			AccountBalance saldo = saldos.get(i);

			// - checar ano/mes do lancamento alterado
			assertEquals("Account", contaBancoId, saldo.getAccount().getId());
			assertEquals("Ano", saldosExpected.get(i)[0], saldo.getAno());
			assertEquals("Mes", saldosExpected.get(i)[1], saldo.getMes());
			assertTrue("Valor", ((BigDecimal) saldosExpected.get(i)[2]).compareTo(saldo.getValor()) == 0);
		}

	}

	private CreditCardTransaction newLancamento() {
		BankAccount bankAccount = new BankAccount();
		bankAccount.setId(contaBancoId);
		bankAccount.setNome("account banco");

		CreditCardAccount creditCardAccount = new CreditCardAccount();
		creditCardAccount.setId(contaCartaoId);
		creditCardAccount.setNome("Account Cartao");
		creditCardAccount.setCartaoDiaFechamento(diaFechamentoFaturaInput);
		creditCardAccount.setCartaoDiaPagamento(diaPagamentoFaturaInput);
		creditCardAccount.setContaPagamentoFatura(bankAccount);

		Category categoriaDespesa = new Category();
		categoriaDespesa.setNome("despesa 1");
		categoriaDespesa.setInOut(InOut.S);

		return new TransactionBuilder().data(dataLancamentoInput).inOut(InOut.S).category(categoriaDespesa)
				.account(creditCardAccount).status(TransactionStatus.NAO_CONFIRMADO).valor(valorLancamentoInput)
				.setQtdParcelas(qtdParcelasInput).buildLancamentoCartaoCredito();

	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

}
