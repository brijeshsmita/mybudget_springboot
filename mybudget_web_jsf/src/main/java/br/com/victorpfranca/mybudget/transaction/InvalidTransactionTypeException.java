package br.com.victorpfranca.mybudget.transaction;

import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;


public class InvalidTransactionTypeException extends AccountTypeException {
	private static final long serialVersionUID = 1L;

	public InvalidTransactionTypeException(String message) {
		super(message);
	}

	public InvalidTransactionTypeException(String message, Throwable cause) {
		super(message, cause);
	}

}
