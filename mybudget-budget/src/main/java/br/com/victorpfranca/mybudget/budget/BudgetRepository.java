package br.com.victorpfranca.mybudget.budget;

import org.springframework.data.repository.CrudRepository;

public interface BudgetRepository extends CrudRepository<Budget, Long> {
	
}
