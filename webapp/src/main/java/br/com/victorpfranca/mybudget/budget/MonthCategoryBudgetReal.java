package br.com.victorpfranca.mybudget.budget;

import static br.com.victorpfranca.mybudget.budget.MonthCategoryBudgetReal.FIND_ALL;
import static br.com.victorpfranca.mybudget.budget.MonthCategoryBudgetReal.FIND_BY_DESPESA_MONTH;
import static br.com.victorpfranca.mybudget.budget.MonthCategoryBudgetReal.FIND_BY_RECEITA_MONTH;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.InOutConverter;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.category.Category;

@Entity
@Table(name = "vw_orcado_real_categoria_mes")
@NamedQueries({
		@NamedQuery(name = FIND_ALL, query = "SELECT o FROM MonthCategoryBudgetReal o WHERE o.user.id = :user"),
		@NamedQuery(name = FIND_BY_DESPESA_MONTH, query = "SELECT o FROM MonthCategoryBudgetReal o where o.user.id = :user and ano = :ano and mes = :mes and inOut = '1' order by orcado-realizado asc"),
		@NamedQuery(name = FIND_BY_RECEITA_MONTH, query = "SELECT o FROM MonthCategoryBudgetReal o where o.user.id = :user and ano = :ano and mes = :mes and inOut = '0' order by orcado-realizado asc") })
public class MonthCategoryBudgetReal implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "MonthCategoryBudgetReal.findAll";
	public static final String FIND_BY_RECEITA_MONTH = "MonthCategoryBudgetReal.findByReceitaMonth";
	public static final String FIND_BY_DESPESA_MONTH = "MonthCategoryBudgetReal.findByDespesaMonth";

	@Id
	@NotNull
	@Convert(converter = InOutConverter.class)
	@Column(name = "in_out", nullable = false, unique = false)
	private InOut inOut;

	@Id
	@NotNull
	@Column(name = "mes", nullable = false, unique = false)
	private Integer mes;

	@Id
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = true, name = "categoria_id")
	private Category category;

	@NotNull
	@Column(name = "orcado", nullable = false, unique = false)
	private BigDecimal orcado;

	@NotNull
	@Column(name = "realizado", nullable = false, unique = false)
	private BigDecimal realizado;

	@NotNull
	@JoinColumn(name = "usuario_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
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
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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