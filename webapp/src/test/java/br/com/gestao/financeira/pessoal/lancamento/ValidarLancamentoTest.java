package br.com.gestao.financeira.pessoal.lancamento;

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

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaBanco;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;

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
	public Conta contaInput;

	@Parameter(8)
	public Conta contaAnteriorInput;

	@Parameter(9)
	public boolean failExpected;

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ false, false, BigDecimal.TEN, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new ContaBanco(), null, false },
				{ false, false, BigDecimal.TEN, InOut.S, InOut.E, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new ContaBanco(), null, true },
				{ false, false, BigDecimal.TEN, InOut.E, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new ContaBanco(), null, true },
				{ false, false, BigDecimal.TEN, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.FEBRUARY, 1), new ContaBanco(), null, true },
				{ false, false, BigDecimal.TEN, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new ContaBanco(), new ContaCartao(), true },
				{ false, false, BigDecimal.TEN, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new ContaBanco(), new ContaBanco(), false },
				{ false, false, BigDecimal.ZERO, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(2000, Month.JANUARY, 1), new ContaBanco(), new ContaBanco(), true },
				{ false, false, BigDecimal.ONE.negate(), InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(YEAR_2000, Month.JANUARY, 1), new ContaBanco(), new ContaBanco(), true },

				{ false, true, BigDecimal.ONE.negate(), InOut.E, null, toDate(2000, Month.JANUARY, 1),
						toDate(YEAR_2000, Month.JANUARY, 1), new ContaBanco(), new ContaBanco(), false },

				{ true, false, BigDecimal.ZERO, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(YEAR_2000, Month.JANUARY, 1), new ContaBanco(), new ContaBanco(), false },
				{ true, false, BigDecimal.ONE.negate(), InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(YEAR_2000, Month.JANUARY, 1), new ContaBanco(), new ContaBanco(), true },
				{ true, false, BigDecimal.ONE, InOut.S, InOut.S, toDate(2000, Month.JANUARY, 1),
						toDate(YEAR_2000, Month.JANUARY, 1), new ContaBanco(), new ContaBanco(), false } };

		return Arrays.asList(data);
	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

	@Test
	public void shouldValidateLancamento() {
		Lancamento lancamento = newLancamento();

		try {
			lancamento.validar();
			if (failExpected) {
				fail();
			}
		} catch (CategoriasIncompativeisException | MesLancamentoAlteradoException | ContaNotNullException
				| TipoContaException | ValorLancamentoInvalidoException e) {
			if (!failExpected) {
				fail();
			}
		}
	}

	private Lancamento newLancamento() {
		Lancamento lancamento = new LancamentoContaCorrente();

		lancamento.setValor(valorLancamentoInput);

		lancamento.setInOut(inOutLancamentoInput);

		if (!isSaldoInicialInput) {
			Categoria categoria = new Categoria();
			categoria.setInOut(inOutCategoriaInput);
			lancamento.setCategoria(categoria);
		} else {
			((LancamentoContaCorrente) lancamento).setSaldoInicial(true);
		}

		if (isFaturaInput) {
			((LancamentoContaCorrente) lancamento).setFaturaCartao(true);
		}

		lancamento.setData(dataInput);
		lancamento.setDataAnterior(dataAnteriorInput);

		lancamento.setConta(contaInput);
		lancamento.setContaAnterior(contaAnteriorInput);

		return lancamento;
	}

}
