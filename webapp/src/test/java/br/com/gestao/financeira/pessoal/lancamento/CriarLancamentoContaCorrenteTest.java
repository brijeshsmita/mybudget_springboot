package br.com.gestao.financeira.pessoal.lancamento;

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

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaBanco;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;

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

		LancamentoContaCorrente lancamento = newLancamento();
		try {
			lancamento = (LancamentoContaCorrente) lancamentoRulesFacade.saveLancamentoContaCorrente(lancamento);
		} catch (MesLancamentoAlteradoException | ContaNotNullException | TipoContaException
				| CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			fail();
		}

		// - checar ano/mes do lancamento gerado
		assertEquals("Ano", anoLancamentoExpected, lancamento.getAno());
		assertEquals("Mes", mesLancamentoExpected, lancamento.getMes());

		List<SaldoConta> saldos = lancamentoRulesFacade.getSaldos();
		assertEquals(saldos.size(), saldosExpected.size());
		for (int i = 0; i < saldosExpected.size(); i++) {
			SaldoConta saldo = saldos.get(i);

			// - checar ano/mes do lancamento alterado
			assertEquals("Ano", saldosExpected.get(i)[0], saldo.getAno());
			assertEquals("Mes", saldosExpected.get(i)[1], saldo.getMes());

			// - checar saldos contas após alteração
			assertEquals("Valor", saldosExpected.get(i)[2], saldo.getValor());
		}

	}

	private LancamentoContaCorrente newLancamento() {
		Conta contaBanco = new ContaBanco();
		contaBanco.setNome("Conta Banco");

		Categoria categoriaDespesa = new Categoria();
		categoriaDespesa.setNome("despesa 1");
		categoriaDespesa.setInOut(inOutInput);

		return new LancamentoBuilder().data(dataLancamentoInput).inOut(inOutInput).categoria(categoriaDespesa)
				.conta(contaBanco).status(LancamentoStatus.NAO_CONFIRMADO).valor(valorLancamentoInput)
				.buildLancamentoContaCorrente();

	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

}
