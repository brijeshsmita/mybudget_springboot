package br.com.victorpfranca.mybudget.transaction.rules;

public class AccountTypeException extends Exception {

	private static final long serialVersionUID = 1L;

	public AccountTypeException(String message) {
		super(message);
	}

	public AccountTypeException(String message, Throwable cause) {
		super(message, cause);
	}

}
