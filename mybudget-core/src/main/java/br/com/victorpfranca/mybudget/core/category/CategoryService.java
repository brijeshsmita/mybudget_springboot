package br.com.victorpfranca.mybudget.core.category;

import java.util.Optional;

public interface CategoryService {

	public Optional<Category> find(Long id);

	public Iterable<Category> findAll();

	public Category save(Category category);

	public void delete(Long id);

}
