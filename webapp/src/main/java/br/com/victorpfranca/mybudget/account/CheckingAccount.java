package br.com.victorpfranca.mybudget.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;

public abstract class CheckingAccount extends Account implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract BigDecimal getSaldoInicial();

	public abstract Date getDataSaldoInicial();

	public abstract void setDataSaldoInicial(Date dataSaldoInicial);

	public abstract void setSaldoInicial(BigDecimal saldoInicial);
	
	public CheckingAccount() {
		super();
		setSaldoInicial(BigDecimal.ZERO);
	}
	
	public CheckingAccount(String nome) {
		super(nome);
		setSaldoInicial(BigDecimal.ZERO);
	}

	public CheckingAccountTransaction buildLancamentoSaldoInicial() {
		CheckingAccountTransaction lancamento = new CheckingAccountTransaction(InOut.E, TransactionStatus.CONFIRMADO);

		lancamento.setConta(this);
		lancamento.setValor(getSaldoInicial());
		lancamento.setSaldo(BigDecimal.ZERO);
		lancamento.setSaldoInicial(true);

		lancamento.setData(getDataSaldoInicial());

		return lancamento;
	}

}