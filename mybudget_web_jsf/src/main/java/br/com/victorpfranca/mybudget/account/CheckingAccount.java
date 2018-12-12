package br.com.victorpfranca.mybudget.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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

}