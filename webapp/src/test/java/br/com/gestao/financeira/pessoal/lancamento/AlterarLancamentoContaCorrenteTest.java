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
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;

@RunWith(Parameterized.class)
public class AlterarLancamentoContaCorrenteTest {

	@Parameter(0)
	public InOut inOutLancamentoInput;

	@Parameter(1)
	public BigDecimal valorLancamentoInput;

	@Parameter(2)
	public Date dataLancamentoInput;

	@Parameter(3)
	public Integer contaInput;

	@Parameter(4)
	public BigDecimal valorLancamentoUpdatedInput;

	@Parameter(5)
	public Date dataLancamentoUpdatedInput;

	@Parameter(6)
	public Integer contaUpdatedInput;

	@Parameter(7)
	public Integer anoLancamentoExpected;

	@Parameter(8)
	public Integer mesLancamentoExpected;

	@Parameter(9)
	public List<Object[]> saldosContaExpected;

	@Parameters
	public static Collection<Object[]> data() {

		BigDecimal zero = BigDecimal.ZERO;
		BigDecimal ten = BigDecimal.TEN;
		BigDecimal oneHundred = ten.multiply(ten);

		int conta1 = 1;
		int conta2 = 2;
		int ano = 2018;

		int dia1 = 1;
		int dia31 = 31;

		Object[][] saldosContaExpected1 = new Object[][] {
				{ conta1, ano, Month.JANUARY.getValue(), oneHundred.negate() } };

		Object[][] saldosContaExpected2 = new Object[][] { { conta1, ano, Month.JANUARY.getValue(), zero },
				{ conta2, ano, Month.JANUARY.getValue(), oneHundred.negate() } };

		Object[][] saldosContaExpected3 = new Object[][] { { conta1, ano, Month.JANUARY.getValue(), oneHundred } };

		Object[][] saldosContaExpected4 = new Object[][] { { conta1, ano, Month.JANUARY.getValue(), zero },
				{ conta2, ano, Month.JANUARY.getValue(), oneHundred }, };

		Object[][] data = new Object[][] {
				{ InOut.S, ten, toDate(ano, Month.JANUARY, dia1), conta1, oneHundred, toDate(ano, Month.JANUARY, dia31),
						conta1, ano, Month.JANUARY.getValue(), Arrays.asList(saldosContaExpected1) },
				{ InOut.S, ten, toDate(ano, Month.JANUARY, dia1), conta1, oneHundred, toDate(ano, Month.JANUARY, dia31),
						conta2, ano, Month.JANUARY.getValue(), Arrays.asList(saldosContaExpected2) },
				{ InOut.E, ten, toDate(ano, Month.JANUARY, dia1), conta1, oneHundred, toDate(ano, Month.JANUARY, dia31),
						conta1, ano, Month.JANUARY.getValue(), Arrays.asList(saldosContaExpected3) },
				{ InOut.E, ten, toDate(ano, Month.JANUARY, dia1), conta1, oneHundred, toDate(ano, Month.JANUARY, dia31),
						conta2, ano, Month.JANUARY.getValue(), Arrays.asList(saldosContaExpected4) } };

		return Arrays.asList(data);
	}

	private LancamentoRulesFacadeMock lancamentoRulesFacade;

	@Before
	public void init() {
		lancamentoRulesFacade = LancamentoRulesFacadeMock.build();
	}

	@Test
	public void shouldUpdateLancamentosEsaldos() {

		LancamentoContaCorrente lancamento = newLancamento();
		try {
			lancamento = (LancamentoContaCorrente) lancamentoRulesFacade.saveLancamentoContaCorrente(lancamento);

			// Alterando lançamento
			lancamento.setValor(valorLancamentoUpdatedInput);
			lancamento.setData(dataLancamentoUpdatedInput);
			lancamento.setConta(new Conta().withId(contaUpdatedInput));

			lancamento = (LancamentoContaCorrente) lancamentoRulesFacade.saveLancamentoContaCorrente(lancamento);

		} catch (MesLancamentoAlteradoException | ContaNotNullException | TipoContaException
				| CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			fail();
		}

		assertEquals("Ano", anoLancamentoExpected, lancamento.getAno());
		assertEquals("Mes", mesLancamentoExpected, lancamento.getMes());

		// checando saldos das contas atualizados após alteração
		List<SaldoConta> saldos = lancamentoRulesFacade.getSaldos();
		assertEquals(saldos.size(), saldosContaExpected.size());
		for (int i = 0; i < saldosContaExpected.size(); i++) {
			SaldoConta saldo = saldos.get(i);

			assertEquals("Conta", saldosContaExpected.get(i)[0], saldo.getConta().getId());
			assertEquals("Ano", saldosContaExpected.get(i)[1], saldo.getAno());
			assertEquals("Mes", saldosContaExpected.get(i)[2], saldo.getMes());
			assertEquals("Valor", saldosContaExpected.get(i)[3], saldo.getValor());
		}
	}

	private LancamentoContaCorrente newLancamento() {
		Conta contaBanco = new Conta().withId(contaInput);

		Categoria categoria = new Categoria();
		categoria.setId(1);
		categoria.setNome("despesa 1");
		categoria.setInOut(inOutLancamentoInput);

		return new LancamentoBuilder().data(dataLancamentoInput).inOut(inOutLancamentoInput).categoria(categoria)
				.conta(contaBanco).status(LancamentoStatus.NAO_CONFIRMADO).valor(valorLancamentoInput)
				.buildLancamentoContaCorrente();
	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

}
