package br.com.victorpfranca.mybudget.transaction.rules;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class NullableAccountException extends Exception {

	private static final long serialVersionUID = 1L;

	public NullableAccountException(String message) {
		super(message);
	}

	public NullableAccountException(String message, Throwable cause) {
		super(message, cause);
	}

}
