package br.com.victorpfranca.mybudget.transaction.rules;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ContaNotNullException extends Exception {

	private static final long serialVersionUID = 1L;

	public ContaNotNullException(String message) {
		super(message);
	}

	public ContaNotNullException(String message, Throwable cause) {
		super(message, cause);
	}

}
