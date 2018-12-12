package br.com.victorpfranca.mybudget.transaction.rules;

public class IncompatibleCategoriesException extends Exception {

	private static final long serialVersionUID = 1L;

	public IncompatibleCategoriesException(String message) {
		super(message);
	}

	public IncompatibleCategoriesException(String message, Throwable cause) {
		super(message, cause);
	}

}
