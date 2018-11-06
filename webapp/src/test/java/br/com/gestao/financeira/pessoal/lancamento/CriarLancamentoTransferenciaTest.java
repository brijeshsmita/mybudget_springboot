package br.com.gestao.financeira.pessoal.lancamento;

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

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;

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

		LancamentoContaCorrente lancamentoOrigem = newLancamento();

		try {
			lancamentoRulesFacade.saveLancamentoTransferencia(lancamentoOrigem);
		} catch (MesLancamentoAlteradoException | ContaNotNullException | TipoContaException
				| CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			fail();
		}

		List<SaldoConta> saldos = lancamentoRulesFacade.getSaldos();
		assertEquals(saldos.size(), (2 * saldosExpected.size()));

		for (int i = 0; i < (saldosExpected.size()); i++) {
			SaldoConta saldoContaOrigem = saldos.get(i);

			//Checar saldo conta origem
			assertEquals("Conta Origem", contaOrigemIdInput, saldoContaOrigem.getConta().getId());
			assertEquals("Ano", saldosExpected.get(i)[0], saldoContaOrigem.getAno());
			assertEquals("Mes", saldosExpected.get(i)[1], saldoContaOrigem.getMes());
			assertEquals("Valor", saldosExpected.get(i)[2], saldoContaOrigem.getValor());

			//Checar saldo conta destino
			SaldoConta saldoContaDestino = saldos.get(i + 1);
			assertEquals("Conta Origem", contaDestinoIdInput, saldoContaDestino.getConta().getId());
			assertEquals("Ano", saldosExpected.get(i)[0], saldoContaDestino.getAno());
			assertEquals("Mes", saldosExpected.get(i)[1], saldoContaDestino.getMes());
			assertEquals("Valor", saldosExpected.get(i)[3], saldoContaDestino.getValor());
		}

		List<Lancamento> lancamentos = lancamentoRulesFacade.getLancamentos();
		assertEquals(lancamentos.size(), 2);

		// Checar lancamento conta origem
		assertEquals("Conta", contaOrigemIdInput, lancamentos.get(0).getConta().getId());
		assertNull("Conta Origem", ((LancamentoContaCorrente) lancamentos.get(0)).getContaOrigem());
		assertEquals("Conta Destino", contaDestinoIdInput,
				((LancamentoContaCorrente) lancamentos.get(0)).getContaDestino().getId());
		assertEquals("InOut", InOut.S, lancamentos.get(0).getInOut());
		assertEquals("Valor", valorLancamentoInput, lancamentos.get(0).getValor());

		// Checar lancamento conta destino
		assertEquals("Conta", contaDestinoIdInput, lancamentos.get(1).getConta().getId());
		assertEquals("Conta Origem", contaOrigemIdInput,
				((LancamentoContaCorrente) lancamentos.get(1)).getContaOrigem().getId());
		assertNull("Conta Destino", ((LancamentoContaCorrente) lancamentos.get(1)).getContaDestino());
		assertEquals("InOut", InOut.E, lancamentos.get(1).getInOut());
		assertEquals("Valor", valorLancamentoInput, lancamentos.get(1).getValor());

	}

	private LancamentoContaCorrente newLancamento() {

		Conta contaOrigem = new Conta().withId(contaOrigemIdInput).withUsuario(new Usuario()).withNome("contaOrigem");
		Conta contaDestino = new Conta().withId(contaDestinoIdInput).withUsuario(new Usuario())
				.withNome("contaDestino");
		;

		Lancamento lancamento = new LancamentoBuilder().data(dataLancamentoInput).inOut(inOutInput)
				.status(LancamentoStatus.NAO_CONFIRMADO).valor(valorLancamentoInput).buildLancamentoContaCorrente();

		lancamento.setConta(contaOrigem);
		((LancamentoContaCorrente) lancamento).setContaDestino(contaDestino);

		return (LancamentoContaCorrente) lancamento;

	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

}
