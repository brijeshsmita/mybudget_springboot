package br.com.victorpfranca.mybudget.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class BankAccount extends CheckingAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal saldoInicial;

	private Date dataSaldoInicial;

	public BankAccount() {
		super();
		this.dataSaldoInicial = Calendar.getInstance().getTime();
	}

	public BankAccount(String nome) {
		super(nome);
		this.dataSaldoInicial = Calendar.getInstance().getTime();
	}

	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public Date getDataSaldoInicial() {
		return dataSaldoInicial;
	}

	public void setDataSaldoInicial(Date dataSaldoInicial) {
		this.dataSaldoInicial = dataSaldoInicial;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

}