package br.com.victorpfranca.mybudget.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("2")
public class MoneyAccount extends CheckingAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "saldo_inicial", nullable = false, unique = false)
	private BigDecimal saldoInicial;

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "data_saldo_inicial", nullable = false, unique = false)
	private Date dataSaldoInicial;
	
	public MoneyAccount() {
		super();
		this.dataSaldoInicial = Calendar.getInstance().getTime();
	}

	
	public MoneyAccount(String nome) {
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