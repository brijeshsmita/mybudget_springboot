package br.com.victorpfranca.mybudget.account;

import java.io.Serializable;

import br.com.victorpfranca.mybudget.accesscontroll.User;

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Integer id;

	protected String nome;

	private boolean padrao;

	private User user;

	public Account() {
	}

	public Account(String nome) {
		this.nome = nome;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Account withId(Integer id) {
		setId(id);
		return this;
	}

	public Account withUsuario(User user) {
		setUsuario(user);
		return this;
	}

	public String getNome() {
		return nome;
	}

	public Account withNome(String nome) {
		setNome(nome);
		return this;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isContaCartao() {
		return this instanceof CreditCardAccount;
	}

	public boolean isPadrao() {
		return padrao;
	}

	public void setPadrao(boolean padrao) {
		this.padrao = padrao;
	}

	public User getUsuario() {
		return user;
	}

	public void setUsuario(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Account))
			return false;
		Account other = (Account) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return getNome();
	}

}