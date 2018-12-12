package br.com.victorpfranca.mybudget.budget;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.User;

public class MonthBudgetReal implements Serializable {
	private static final long serialVersionUID = 1L;

	private InOut inOut;

	private Integer ano;

	private Integer mes;

	private BigDecimal saldo;

	private User user;

	public InOut getInOut() {
		return inOut;
	}

	public void setInOut(InOut inOut) {
		this.inOut = inOut;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public User getUsuario() {
		return user;
	}

	public void setUsuario(User user) {
		this.user = user;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public int compareDate(int ano, int mes) {
		LocalDate date = LocalDate.of(this.ano, this.mes, 1);
		return date.compareTo(LocalDate.of(ano, mes, 1));
	}

}