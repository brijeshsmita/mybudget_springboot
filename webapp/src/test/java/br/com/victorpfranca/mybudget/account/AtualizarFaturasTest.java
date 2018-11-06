package br.com.victorpfranca.mybudget.account;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.account.BankAccount;
import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;
import br.com.victorpfranca.mybudget.transaction.rules.ContaNotNullException;

public class AtualizarFaturasTest {

	private int DIA_1 = 1;
	private int DIA_2 = 2;
	private int DIA_3 = 3;

	private int MES_1 = 1;

	private int ANO_2000 = 2000;

	private BigDecimal VALOR_100 = BigDecimal.TEN.multiply(BigDecimal.TEN);

	private CreditCardAccount creditCardAccount;

	@Before
	public void init() {
		creditCardAccount = new CreditCardAccount();
		creditCardAccount.setCartaoDiaFechamento(DIA_2);
		creditCardAccount.setCartaoDiaPagamento(DIA_3);

		CheckingAccount checkingAccount = new BankAccount();
		creditCardAccount.setContaPagamentoFatura(checkingAccount);
	}

	@Test
	public void testSemFatura() {
		LocalDate date = LocalDate.of(ANO_2000, MES_1, DIA_1);
		CreditCardTransaction lancamento = new CreditCardTransaction(InOut.S, TransactionStatus.NAO_CONFIRMADO);
		lancamento.setValor(VALOR_100);
		lancamento.setData(LocalDateConverter.toDate(date));
		lancamento.setQtdParcelas(1);

		List<Transaction> faturasExistentes = new ArrayList<Transaction>();

		try {
			List<Transaction> faturas = creditCardAccount.carregarFaturas(lancamento, faturasExistentes);
			assertTrue(1 == faturas.size());
			assertTrue(faturas.get(0).getValor().compareTo(VALOR_100) == 0);
		} catch (ContaNotNullException e) {
			fail();
		}
	}

}
