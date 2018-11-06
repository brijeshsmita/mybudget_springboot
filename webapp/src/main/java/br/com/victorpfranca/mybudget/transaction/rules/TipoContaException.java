package br.com.victorpfranca.mybudget.transaction.rules;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class TipoContaException extends Exception {

	private static final long serialVersionUID = 1L;

	public TipoContaException(String message) {
		super(message);
	}

	public TipoContaException(String message, Throwable cause) {
		super(message, cause);
	}

}
