package br.com.victorpfranca.mybudget.budget;


public class NoDefaultAccountException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoDefaultAccountException(String message) {
		super(message);
	}

	public NoDefaultAccountException(String message, Throwable cause) {
		super(message, cause);
	}

}
