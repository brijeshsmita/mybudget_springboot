package br.com.victorpfranca.mybudget.transaction.rules;

public class DeletionNotPermittedException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeletionNotPermittedException(String message) {
		super(message);
	}

	public DeletionNotPermittedException(String message, Throwable cause) {
		super(message, cause);
	}

}
