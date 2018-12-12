package br.com.victorpfranca.mybudget.transaction.rules;

public class AccountTypeUpdatedException extends Exception {

	private static final long serialVersionUID = 1L;

	public AccountTypeUpdatedException(String message) {
		super(message);
	}

	public AccountTypeUpdatedException(String message, Throwable cause) {
		super(message, cause);
	}

}
