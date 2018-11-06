package br.com.gestao.financeira.pessoal.conta;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoCartaoCredito;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoStatus;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;

public class AtualizarFaturasTest {

	private int DIA_1 = 1;
	private int DIA_2 = 2;
	private int DIA_3 = 3;

	private int MES_1 = 1;

	private int ANO_2000 = 2000;

	private BigDecimal VALOR_100 = BigDecimal.TEN.multiply(BigDecimal.TEN);

	private ContaCartao contaCartao;

	@Before
	public void init() {
		contaCartao = new ContaCartao();
		contaCartao.setCartaoDiaFechamento(DIA_2);
		contaCartao.setCartaoDiaPagamento(DIA_3);

		ContaCorrente contaCorrente = new ContaBanco();
		contaCartao.setContaPagamentoFatura(contaCorrente);
	}

	@Test
	public void testSemFatura() {
		LocalDate date = LocalDate.of(ANO_2000, MES_1, DIA_1);
		LancamentoCartaoCredito lancamento = new LancamentoCartaoCredito(InOut.S, LancamentoStatus.NAO_CONFIRMADO);
		lancamento.setValor(VALOR_100);
		lancamento.setData(LocalDateConverter.toDate(date));
		lancamento.setQtdParcelas(1);

		List<Lancamento> faturasExistentes = new ArrayList<Lancamento>();

		try {
			List<Lancamento> faturas = contaCartao.carregarFaturas(lancamento, faturasExistentes);
			assertTrue(1 == faturas.size());
			assertTrue(faturas.get(0).getValor().compareTo(VALOR_100) == 0);
		} catch (ContaNotNullException e) {
			fail();
		}
	}

}
