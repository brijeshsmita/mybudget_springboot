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
public class RemoverLancamentoContaCorrenteTest {

	@Parameter(0)
	public InOut inOutLancamentoInput;

	@Parameter(1)
	public BigDecimal valorLancamentoInput;

	@Parameter(2)
	public Date dataLancamentoInput;

	@Parameter(3)
	public Integer contaInput;

	@Parameter(4)
	public BigDecimal valorLancamentoRemocaoInput;

	@Parameter(5)
	public List<Object[]> saldosContaExpected;

	@Parameters
	public static Collection<Object[]> data() {

		BigDecimal zero = BigDecimal.ZERO;
		BigDecimal ten = BigDecimal.TEN;
		BigDecimal oneHundred = ten.multiply(ten);

		int conta1 = 1;
		int ano = 2018;

		int dia1 = 1;

		Object[][] saldosContaExpected1 = new Object[][] { { conta1, ano, Month.JANUARY.getValue(), zero } };

		BigDecimal ninety = BigDecimal.valueOf(90);
		Object[][] saldosContaExpected2 = new Object[][] { { conta1, ano, Month.JANUARY.getValue(), ninety.negate() } };

		Object[][] data = new Object[][] {
				{ InOut.S, oneHundred, toDate(ano, Month.JANUARY, dia1), conta1, oneHundred,
						Arrays.asList(saldosContaExpected1) },
				{ InOut.S, oneHundred, toDate(ano, Month.JANUARY, dia1), conta1, ten,
						Arrays.asList(saldosContaExpected2) } };

		return Arrays.asList(data);
	}

	private LancamentoRulesFacadeMock lancamentoRulesFacade;

	@Before
	public void init() {
		lancamentoRulesFacade = LancamentoRulesFacadeMock.build();
	}

	@Test
	public void shoulRemoveLancamentoERecomporSaldos() {
		LancamentoContaCorrente lancamento = newLancamento(valorLancamentoInput);
		try {
			lancamentoRulesFacade.saveLancamentoContaCorrente(lancamento);
		} catch (MesLancamentoAlteradoException | ContaNotNullException | TipoContaException
				| CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			fail();
		}

		// Removendo lançamento
		LancamentoContaCorrente lancamentoParaRemover = newLancamento(valorLancamentoRemocaoInput);
		lancamentoRulesFacade.removeLancamento(lancamentoParaRemover);

		// checando saldos das contas atualizados após remoção
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

	private LancamentoContaCorrente newLancamento(BigDecimal valor) {

		Categoria categoriaDespesa = new Categoria();
		categoriaDespesa.setNome("despesa 1");
		categoriaDespesa.setInOut(inOutLancamentoInput);

		Date date = toDate(2018, Month.JANUARY, 1);

		return new LancamentoBuilder().data(date).inOut(inOutLancamentoInput).categoria(categoriaDespesa)
				.conta(new Conta().withId(1)).status(LancamentoStatus.NAO_CONFIRMADO).valor(valor)
				.buildLancamentoContaCorrente();
	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

}
