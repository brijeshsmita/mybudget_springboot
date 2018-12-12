package br.com.victorpfranca.mybudget.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.accesscontroll.User;

public class AccountBalance implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private Account conta;

	private BigDecimal valor;

	private Integer ano;

	private Integer mes;

	private User user;

	public AccountBalance() {
		this.valor = BigDecimal.ZERO;
	}

	public AccountBalance(Account account, Integer ano, Integer mes, BigDecimal valor) {
		this.conta = account;
		this.user = account.getUsuario();
		this.ano = ano;
		this.mes = mes;
		this.valor = valor;
	}

	public AccountBalance(int ano, int mes, BigDecimal valor) {
		this.ano = ano;
		this.mes = mes;
		this.valor = valor;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Account getConta() {
		return conta;
	}

	public void setConta(Account account) {
		this.conta = account;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getMes() {
		return mes;
	}

	public LocalDate getLocalDate() {
		return LocalDate.of(this.ano, this.mes, 1);
	}

	public Date getDate() {
		return LocalDateConverter.toDate(getLocalDate());
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public User getUsuario() {
		return user;
	}

	public void setUsuario(User user) {
		this.user = user;
	}

	public AccountBalance withConta(Account account) {
		setConta(account);
		return this;
	}

	public AccountBalance withAno(int ano) {
		setAno(ano);
		return this;
	}

	public AccountBalance withMes(int mes) {
		setMes(mes);
		return this;
	}

	public AccountBalance withValor(BigDecimal valor) {
		setValor(valor);
		return this;
	}

	public int compareDate(int ano, int mes) {
		LocalDate date = LocalDate.of(this.ano, this.mes, 1);
		return date.compareTo(LocalDate.of(ano, mes, 1));
	}

}