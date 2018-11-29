package br.com.victorpfranca.mybudget.category;

import static br.com.victorpfranca.mybudget.category.Category.FIND_ALL_QUERY;

import java.io.Serializable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Column;
import javax.persistence.Convert;
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

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.InOutConverter;
import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.accesscontroll.User;

@Entity
@Table(name = "categoria")
@NamedQueries({
		@NamedQuery(name = FIND_ALL_QUERY, query = "SELECT c FROM Category c where c.user.id = :user AND (:nome is null OR c.nome = :nome) AND (:inOut is null OR inOut = :inOut) ORDER BY nome") })

public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String GENERATOR = "GeneratorCategoria";

	public static final String FIND_ALL_QUERY = "Category.findAll";

	@Id
	@SequenceGenerator(name = GENERATOR, sequenceName = "sq_categoria", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = GENERATOR, strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;

	@NotNull(message = "Dê um nome para esta category")
	@Column(name = "nome", nullable = false, unique = false)
	private String nome;

	@NotNull
	@Convert(converter = InOutConverter.class)
	@Column(name = "in_out", nullable = false, unique = false)
	private InOut inOut;

	@NotNull
	@JoinColumn(name = "usuario_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public Category() {
	}

	public Category(String nome, InOut inOut) {
		this.nome = nome;
		this.inOut = inOut;
	}

	@PrePersist
	void beforePersist() {
		try {
			this.user = ((CredentialsStore) new InitialContext().lookup("java:module/CredentialsStoreImpl"))
					.recuperarUsuarioLogado();
		} catch (NamingException e) {
			throw new RuntimeException(e.getExplanation());
		}
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public InOut getInOut() {
		return inOut;
	}

	public void setInOut(InOut inOut) {
		this.inOut = inOut;
	}

	public User getUsuario() {
		return user;
	}

	public void setUsuario(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Category))
			return false;
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getNome();
	}

}