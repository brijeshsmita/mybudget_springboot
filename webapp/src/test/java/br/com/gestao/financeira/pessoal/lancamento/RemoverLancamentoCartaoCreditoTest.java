package br.com.gestao.financeira.pessoal.lancamento;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.conta.ContaBanco;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;

@RunWith(Parameterized.class)
public class RemoverLancamentoCartaoCreditoTest {

	@Parameter(0)
	public BigDecimal valorLancamentoInput;

	@Parameter(1)
	public Integer qtdParcelasInput;

	@Parameter(2)
	public Date dataLancamentoInput;

	@Parameter(3)
	public Integer diaFechamentoFaturaInput;

	@Parameter(4)
	public Integer diaPagamentoFaturaInput;

	@Parameter(5)
	public Integer contaCartaoId;

	@Parameter(6)
	public Integer contaBancoId;

	@Parameter(7)
	public Integer anoLancamentoExpected;

	@Parameter(8)
	public Integer mesLancamentoExpected;

	@Parameter(9)
	public List<Object[]> saldosExpected;

	@Parameter(10)
	public List<Object[]> faturasExpected;

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
		BigDecimal valorFatura0 = BigDecimal.ZERO;

		BigDecimal valorCompra100 = valorCompra10.multiply(valorCompra10);
		int qtdParcelas10 = 10;

		Object[][] saldos = new Object[][] { { ano, Month.JANUARY.getValue(), valorFatura0 },
				{ ano, Month.FEBRUARY.getValue(), valorFatura0 }, { ano, Month.MARCH.getValue(), valorFatura0 },
				{ ano, Month.APRIL.getValue(), valorFatura0 }, { ano, Month.MAY.getValue(), valorFatura0 },
				{ ano, Month.JUNE.getValue(), valorFatura0 }, { ano, Month.JULY.getValue(), valorFatura0 },
				{ ano, Month.AUGUST.getValue(), valorFatura0 }, { ano, Month.SEPTEMBER.getValue(), valorFatura0 },
				{ ano, Month.OCTOBER.getValue(), valorFatura0 } };

		Object[][] faturas = new Object[][] { { dataFatura, valorFatura0 } };

		Object[][] data = new Object[][] { { valorCompra100, qtdParcelas10, dataCompra, dia10, dia20, contaCartaoId1,
				contaBancoId2, ano, MES_JANEIRO.getValue(), Arrays.asList(saldos), Arrays.asList(faturas) } };

		return Arrays.asList(data);

	}

	private LancamentoRulesFacadeMock lancamentoRulesFacade;

	@Before
	public void init() {
		this.lancamentoRulesFacade = LancamentoRulesFacadeMock.build();
	}

	@Test
	public void shouldCreateLancamentoESaldos() {

		Lancamento lancamento = newLancamento();
		try {
			lancamento = lancamentoRulesFacade.saveLancamentoCartaoDeCredito(lancamento);
			lancamentoRulesFacade.removeLancamentoCartao((LancamentoCartaoCredito) lancamento);
		} catch (MesLancamentoAlteradoException | ContaNotNullException | TipoContaException
				| CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			fail();
		}

		// - checar ano/mes do lancamento gerado
		assertEquals("Ano", anoLancamentoExpected, lancamento.getAno());
		assertEquals("Mes", mesLancamentoExpected, lancamento.getMes());

		// Validação lançamento cartão
		List<LancamentoCartaoCredito> lancamentosCartaoCredito = lancamentoRulesFacade.getLancamentosCartao();
		assertTrue("Qtd lancamentos cartao", lancamentosCartaoCredito.size() == 0);

		// Validação faturas
		List<LancamentoContaCorrente> lancamentosFatura = lancamentoRulesFacade.getLancamentosFaturas();
		assertTrue("Qtd faturas", lancamentosFatura.size() == qtdParcelasInput.intValue());
		int faturaI = 0;
		for (Iterator<LancamentoContaCorrente> iterator = lancamentosFatura.iterator(); iterator.hasNext();) {
			Lancamento lancamentoFatura = iterator.next();

			Object[] faturaExpected = faturasExpected.get(0);

			assertTrue("Data", LocalDateConverter.plusMonth((Date) faturaExpected[0], faturaI)
					.compareTo(lancamentoFatura.getData()) == 0);
			assertTrue("Valor", ((BigDecimal) faturaExpected[1]).compareTo(lancamentoFatura.getValor()) == 0);
			assertTrue("InOut", InOut.S.compareTo(lancamentoFatura.getInOut()) == 0);
			assertTrue("Conta", contaBancoId.compareTo(lancamentoFatura.getConta().getId()) == 0);
			assertTrue("ContaCartao", contaCartaoId
					.compareTo(((LancamentoContaCorrente) lancamentoFatura).getCartaoCreditoFatura().getId()) == 0);
			assertTrue("isFatura", ((LancamentoContaCorrente) lancamentoFatura).isFaturaCartao());
			faturaI++;
		}

		// Validação fatura item
		List<LancamentoFaturaCartaoItem> lancamentosFaturaCartaoItem = lancamentoRulesFacade
				.getLancamentosItensFatura();
		assertTrue("Qtd fatura item", lancamentosFaturaCartaoItem.size() == 0);

		// Validação saldos
		List<SaldoConta> saldos = lancamentoRulesFacade.getSaldos();
		assertEquals(saldos.size(), saldosExpected.size());
		for (int i = 0; i < saldosExpected.size(); i++) {
			SaldoConta saldo = saldos.get(i);

			// - checar ano/mes do lancamento alterado
			assertEquals("Conta", contaBancoId, saldo.getConta().getId());
			assertEquals("Ano", saldosExpected.get(i)[0], saldo.getAno());
			assertEquals("Mes", saldosExpected.get(i)[1], saldo.getMes());
			assertTrue("Valor", ((BigDecimal) saldosExpected.get(i)[2]).compareTo(saldo.getValor()) == 0);
		}

	}

	private LancamentoCartaoCredito newLancamento() {
		ContaBanco contaBanco = new ContaBanco();
		contaBanco.setId(contaBancoId);
		contaBanco.setNome("conta banco");

		ContaCartao contaCartao = new ContaCartao();
		contaCartao.setId(contaCartaoId);
		contaCartao.setNome("Conta Cartao");
		contaCartao.setCartaoDiaFechamento(diaFechamentoFaturaInput);
		contaCartao.setCartaoDiaPagamento(diaPagamentoFaturaInput);
		contaCartao.setContaPagamentoFatura(contaBanco);

		Categoria categoriaDespesa = new Categoria();
		categoriaDespesa.setNome("despesa 1");
		categoriaDespesa.setInOut(InOut.S);

		return new LancamentoBuilder().data(dataLancamentoInput).inOut(InOut.S).categoria(categoriaDespesa)
				.conta(contaCartao).status(LancamentoStatus.NAO_CONFIRMADO).valor(valorLancamentoInput)
				.setQtdParcelas(qtdParcelasInput).buildLancamentoCartaoCredito();

	}

	private static Date toDate(int year, Month month, int day) {
		return LocalDateConverter.toDate(LocalDate.of(year, month.getValue(), day));
	}

}
