package br.com.victorpfranca.mybudget.budget;

import static br.com.victorpfranca.mybudget.budget.Budget.FIND_ALL;
import static br.com.victorpfranca.mybudget.budget.Budget.FIND_BY_CATEGORIA_QUERY;
import static br.com.victorpfranca.mybudget.budget.Budget.FIND_BY_RECEITA_DESPESA_QUERY;
import static br.com.victorpfranca.mybudget.budget.Budget.REMOVE_BY_CATEGORIA_QUERY;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.category.Category;

@Entity
@Table(name = "orcamento")
@NamedQueries({
		@NamedQuery(name = FIND_ALL, query = "SELECT o FROM Budget o WHERE o.user.id = :user ORDER BY categoria.nome, o.ano, o.mes ASC"),
		@NamedQuery(name = FIND_BY_CATEGORIA_QUERY, query = "SELECT o FROM Budget o WHERE o.user.id = :user AND categoria = :categoria ORDER BY categoria.nome, o.ano, o.mes ASC"),
		@NamedQuery(name = FIND_BY_RECEITA_DESPESA_QUERY, query = "SELECT o FROM Budget o WHERE (:ano is null OR ano = :ano) AND o.user.id = :user AND categoria.inOut = :inOut ORDER BY categoria.nome, o.ano, o.mes ASC"),
		@NamedQuery(name = REMOVE_BY_CATEGORIA_QUERY, query = "DELETE FROM Budget o WHERE categoria = :categoria"), })

public class Budget implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String GENERATOR = "GeneratorOrcamento";

	public static final String FIND_ALL = "Budget.findAll";
	public static final String FIND_BY_CATEGORIA_QUERY = "Budget.findByCategoria";
	public static final String FIND_BY_RECEITA_DESPESA_QUERY = "Budget.findByReceitaDespesa";
	public static final String REMOVE_BY_CATEGORIA_QUERY = "Budget.removeByCategoria";

	@Id
	@SequenceGenerator(name = GENERATOR, sequenceName = "sq_orcamento", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = false, name = "categoria_id")
	private Category categoria;

	@NotNull
	@Column(name = "ano", nullable = false, unique = false)
	private Integer ano;

	@NotNull
	@Column(name = "mes", nullable = false, unique = false)
	private Integer mes;

	@NotNull
	@Column(name = "valor", nullable = false, unique = false)
	private BigDecimal valor;

	@NotNull
	@JoinColumn(name = "usuario_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
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

	@PrePersist
	void beforePersist() {
		try {
			setUsuario(((CredentialsStore) new InitialContext().lookup("java:module/CredentialsStoreImpl"))
					.recuperarUsuarioLogado());
		} catch (NamingException e) {
			throw new RuntimeException(e.getExplanation());
		}
	}

}