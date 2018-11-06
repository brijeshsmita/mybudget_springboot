package br.com.gestao.financeira.pessoal.conta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoContaCorrente;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoStatus;

public abstract class ContaCorrente extends Conta implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract BigDecimal getSaldoInicial();

	public abstract Date getDataSaldoInicial();

	public abstract void setDataSaldoInicial(Date dataSaldoInicial);

	public abstract void setSaldoInicial(BigDecimal saldoInicial);
	
	public ContaCorrente() {
		super();
		setSaldoInicial(BigDecimal.ZERO);
	}
	
	public ContaCorrente(String nome) {
		super(nome);
		setSaldoInicial(BigDecimal.ZERO);
	}

	public LancamentoContaCorrente buildLancamentoSaldoInicial() {
		LancamentoContaCorrente lancamento = new LancamentoContaCorrente(InOut.E, LancamentoStatus.CONFIRMADO);

		lancamento.setConta(this);
		lancamento.setValor(getSaldoInicial());
		lancamento.setSaldo(BigDecimal.ZERO);
		lancamento.setSaldoInicial(true);

		lancamento.setData(getDataSaldoInicial());

		return lancamento;
	}

}