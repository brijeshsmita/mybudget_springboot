package br.com.gestao.financeira.pessoal.orcamento;

import static br.com.gestao.financeira.pessoal.orcamento.Orcamento.FIND_ALL;
import static br.com.gestao.financeira.pessoal.orcamento.Orcamento.FIND_BY_CATEGORIA_QUERY;
import static br.com.gestao.financeira.pessoal.orcamento.Orcamento.FIND_BY_RECEITA_DESPESA_QUERY;
import static br.com.gestao.financeira.pessoal.orcamento.Orcamento.REMOVE_BY_CATEGORIA_QUERY;

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

import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;

@Entity
@Table(name = "orcamento")
@NamedQueries({
		@NamedQuery(name = FIND_ALL, query = "SELECT o FROM Orcamento o WHERE o.usuario.id = :usuario ORDER BY categoria.nome, o.ano, o.mes ASC"),
		@NamedQuery(name = FIND_BY_CATEGORIA_QUERY, query = "SELECT o FROM Orcamento o WHERE o.usuario.id = :usuario AND categoria = :categoria ORDER BY categoria.nome, o.ano, o.mes ASC"),
		@NamedQuery(name = FIND_BY_RECEITA_DESPESA_QUERY, query = "SELECT o FROM Orcamento o WHERE (:ano is null OR ano = :ano) AND o.usuario.id = :usuario AND categoria.inOut = :inOut ORDER BY categoria.nome, o.ano, o.mes ASC"),
		@NamedQuery(name = REMOVE_BY_CATEGORIA_QUERY, query = "DELETE FROM Orcamento o WHERE categoria = :categoria"), })

public class Orcamento implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String GENERATOR = "GeneratorOrcamento";

	public static final String FIND_ALL = "Orcamento.findAll";
	public static final String FIND_BY_CATEGORIA_QUERY = "Orcamento.findByCategoria";
	public static final String FIND_BY_RECEITA_DESPESA_QUERY = "Orcamento.findByReceitaDespesa";
	public static final String REMOVE_BY_CATEGORIA_QUERY = "Orcamento.removeByCategoria";

	@Id
	@SequenceGenerator(name = GENERATOR, sequenceName = "sq_orcamento", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = false, name = "categoria_id")
	private Categoria categoria;

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
	private Usuario usuario;

	public Orcamento() {
		setValor(BigDecimal.ZERO);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Orcamento))
			return false;
		Orcamento other = (Orcamento) obj;
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