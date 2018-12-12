package br.com.victorpfranca.mybudget.budget;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.category.Category;

public class MonthCategoryBudgetReal implements Serializable {
	private static final long serialVersionUID = 1L;

	private InOut inOut;

	private Integer mes;

	private Category categoria;

	private BigDecimal orcado;

	private BigDecimal realizado;

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

	public BigDecimal getOrcado() {
		return orcado != null ? orcado : BigDecimal.ZERO;
	}

	public void setOrcado(BigDecimal orcado) {
		this.orcado = orcado;
	}

	public BigDecimal getRealizado() {
		return realizado != null ? realizado : BigDecimal.ZERO;
	}

	public void setRealizado(BigDecimal realizado) {
		this.realizado = realizado;
	}

	public Category getCategoria() {
		return categoria;
	}

	public void setCategoria(Category category) {
		this.categoria = category;
	}

	public User getUsuario() {
		return user;
	}

	public void setUsuario(User user) {
		this.user = user;
	}

	public BigDecimal getSaldo() {
		return getOrcado().compareTo(getRealizado()) >= 0 ? getOrcado().subtract(getRealizado()) : BigDecimal.ZERO;
	}

	public BigDecimal getRealPercent() {
		BigDecimal realizado = BigDecimal.ZERO;
		if (getRealizado() != null)
			realizado = getRealizado();
		if (getOrcado() == null || getOrcado().compareTo(BigDecimal.ZERO) == 0)
			return BigDecimal.ZERO;

		return realizado.divide(getOrcado(), 2, RoundingMode.HALF_UP).multiply(BigDecimal.TEN.multiply(BigDecimal.TEN));
	}

}