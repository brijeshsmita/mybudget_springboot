package br.com.victorpfranca.mybudget.core.category;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CategoryController {

	private CategoryService categoryService;

	@Autowired
	public CategoryController(final CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@GetMapping("/categories")
	ResponseEntity<Iterable<Category>> findAll() {
		return ResponseEntity.ok(categoryService.findAll());
	}

	@GetMapping("/categories/{id}")
	ResponseEntity<Category> find(final @PathVariable("id") Long id) {
		return categoryService.find(id).map(category -> ResponseEntity.ok(category))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/categories/search")
	ResponseEntity<Collection<Category>> search(final @RequestParam(value="name") String name) {
		return ResponseEntity.ok(categoryService.search(name));
	}

	@PostMapping("/categories")
	ResponseEntity<Category> save(@RequestBody Category category) {
		return ResponseEntity.ok(categoryService.save(category));
	}

	@PutMapping("/categories")
	ResponseEntity<Category> update(@RequestBody Category category) {
		return ResponseEntity.ok(categoryService.save(category));
	}

	@DeleteMapping("/categories/{id}")
	void delete(final @PathVariable("id") Long id) {
		categoryService.delete(id);
	}

}
