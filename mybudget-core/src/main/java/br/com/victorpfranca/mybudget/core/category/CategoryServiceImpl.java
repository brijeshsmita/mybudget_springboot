package br.com.victorpfranca.mybudget.core.category;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryServiceImpl(final CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Optional<Category> find(Long id) {
		return categoryRepository.findById(id);
	}

	public Iterable<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Category save(Category category) {
		return categoryRepository.save(category);
	}
	
	public void delete(Long id) {
		categoryRepository.deleteById(id);
	}

}
