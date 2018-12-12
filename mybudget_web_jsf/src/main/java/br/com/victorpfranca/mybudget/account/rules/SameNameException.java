package br.com.victorpfranca.mybudget.account.rules;

public class SameNameException extends Exception {

	private static final long serialVersionUID = 1L;

	public SameNameException(String message) {
		super(message);
	}

	public SameNameException(String message, Throwable cause) {
		super(message, cause);
	}
}
