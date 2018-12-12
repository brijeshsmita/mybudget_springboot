package br.com.victorpfranca.mybudget.category;

import java.io.Serializable;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.User;

public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String GENERATOR = "GeneratorCategoria";

	public static final String FIND_ALL_QUERY = "Category.findAll";

	private Integer id;

	private String nome;

	private InOut inOut;

	private User user;

	public Category() {
	}

	public Category(String nome, InOut inOut) {
		this.nome = nome;
		this.inOut = inOut;
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