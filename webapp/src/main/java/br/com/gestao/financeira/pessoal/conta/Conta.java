package br.com.gestao.financeira.pessoal.conta;

import static br.com.gestao.financeira.pessoal.conta.Conta.FIND_ALL_CONTA_BANCO_QUERY;
import static br.com.gestao.financeira.pessoal.conta.Conta.FIND_ALL_CONTA_CARTOES_QUERY;
import static br.com.gestao.financeira.pessoal.conta.Conta.FIND_ALL_CONTA_DINHEIRO_QUERY;
import static br.com.gestao.financeira.pessoal.conta.Conta.FIND_ALL_QUERY;
import static br.com.gestao.financeira.pessoal.conta.Conta.FIND_BY_NAME_QUERY;

import java.io.Serializable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@Table(name = "conta")
@NamedQueries({
		@NamedQuery(name = FIND_ALL_QUERY, query = "SELECT c FROM Conta c WHERE c.usuario.id = :usuario ORDER BY nome"),
		@NamedQuery(name = FIND_BY_NAME_QUERY, query = "SELECT c FROM Conta c where c.usuario.id = :usuario AND c.nome = :nome"),
		@NamedQuery(name = FIND_ALL_CONTA_BANCO_QUERY, query = "SELECT c FROM ContaBanco c WHERE c.usuario.id = :usuario ORDER BY nome"),
		@NamedQuery(name = FIND_ALL_CONTA_DINHEIRO_QUERY, query = "SELECT c FROM ContaDinheiro c WHERE c.usuario.id = :usuario ORDER BY nome"),
		@NamedQuery(name = FIND_ALL_CONTA_CARTOES_QUERY, query = "SELECT c FROM ContaCartao c WHERE c.usuario.id = :usuario ORDER BY nome") })

public class Conta implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String GENERATOR = "GeneratorConta";

	public static final String FIND_ALL_QUERY = "Conta.findAll";
	public static final String FIND_BY_NAME_QUERY = "Conta.findByName";
	public static final String FIND_ALL_CONTA_BANCO_QUERY = "Conta.findContasBancos";
	public static final String FIND_ALL_CONTA_DINHEIRO_QUERY = "Conta.findContasDinheiro";
	public static final String FIND_ALL_CONTA_CARTOES_QUERY = "Conta.findContasCartoes";

	@Id
	@SequenceGenerator(name = GENERATOR, sequenceName = "sq_conta", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	protected Integer id;

	@NotNull(message = "Dê um nome para esta conta")
	@Column(name = "nome", nullable = false, unique = false)
	protected String nome;

	@Column(name = "padrao", nullable = false, unique = false)
	private boolean padrao;

	@NotNull
	@JoinColumn(name = "usuario_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;
	
	public Conta() {
	}

	public Conta(String nome) {
		this.nome = nome;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Conta withId(Integer id) {
		setId(id);
		return this;
	}

	public Conta withUsuario(Usuario usuario) {
		setUsuario(usuario);
		return this;
	}

	public String getNome() {
		return nome;
	}

	public Conta withNome(String nome) {
		setNome(nome);
		return this;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isContaCartao() {
		return this instanceof ContaCartao;
	}

	public boolean isPadrao() {
		return padrao;
	}

	public void setPadrao(boolean padrao) {
		this.padrao = padrao;
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
		if (!(obj instanceof Conta))
			return false;
		Conta other = (Conta) obj;
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