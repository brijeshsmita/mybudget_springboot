package br.com.victorpfranca.mybudget.accesscontroll;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.category.SameNameException;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;

public class UserService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean existeUsuarioComEmail(String email) {
		return false;
	}

	public void create(User user) {
	}

	public void completarCadastro(Integer id, String firstName)
			throws SameNameException, br.com.victorpfranca.mybudget.account.rules.SameNameException,
			NullableAccountException, TransactionMonthUpdatedException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {
	}

	public void preencherCadastrosIniciais()
			throws SameNameException, br.com.victorpfranca.mybudget.account.rules.SameNameException,
			NullableAccountException, TransactionMonthUpdatedException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {
	}

	public User updatePassword(Integer id, String newPassword) {

		return new User("John", "Willians", "john@mybudget.com", true, LocalDateConverter.now(),
				LocalDateConverter.now(), BigDecimal.ZERO, false);
	}

	public Long getUsuarioCount() {
		return 1l;
	}

	public User recuperarViaEmail(String email) {
		return new User("John", "Willians", "john@mybudget.com", true, LocalDateConverter.now(),
				LocalDateConverter.now(), BigDecimal.ZERO, false);
	}

	public void ativar(Integer id) {
	}

	public void inativar(Integer id) {
	}

	public void incrementarContadorAcesso(User user) {
	}
}
