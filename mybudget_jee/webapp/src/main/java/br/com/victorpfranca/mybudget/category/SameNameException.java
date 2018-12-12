package br.com.victorpfranca.mybudget.category;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class SameNameException extends Exception {

	private static final long serialVersionUID = 1L;

	public SameNameException(String message) {
		super(message);
	}

	public SameNameException(String message, Throwable cause) {
		super(message, cause);
	}
}
