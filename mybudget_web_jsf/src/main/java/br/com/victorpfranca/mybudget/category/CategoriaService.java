package br.com.victorpfranca.mybudget.category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.victorpfranca.mybudget.transaction.rules.DeletionNotPermittedException;

public class CategoriaService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Category find(Integer id) {
		return new Category();
	}

	public List<Category> findAll() {
		return new ArrayList<Category>();
	}

	public List<Category> findReceitas() {
		return new ArrayList<Category>();
	}

	public List<Category> findDespesas() {
		return new ArrayList<Category>();
	}

	public void saveCategorias(List<Category> categories) throws SameNameException {
	}

	public Category save(Category category) throws SameNameException {
		return new Category();
	}

	public void remove(Category category) throws DeletionNotPermittedException {
	}

	public void remove(Integer id) throws DeletionNotPermittedException {
	}

	private void validarSemLancamentos(Category category) throws DeletionNotPermittedException {
	}

}
