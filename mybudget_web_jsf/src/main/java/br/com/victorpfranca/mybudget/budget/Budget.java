package br.com.victorpfranca.mybudget.budget;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.category.Category;

public class Budget implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Category categoria;

	private Integer ano;

	private Integer mes;

	private BigDecimal valor;

	private User user;

	public Budget() {
		setValor(BigDecimal.ZERO);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Category getCategoria() {
		return categoria;
	}

	public void setCategoria(Category category) {
		this.categoria = category;
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

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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
		if (!(obj instanceof Budget))
			return false;
		Budget other = (Budget) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(getCategoria().getNome()).append(getAno()).toString();
	}

}