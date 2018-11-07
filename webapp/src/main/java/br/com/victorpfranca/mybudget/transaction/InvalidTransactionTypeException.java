package br.com.victorpfranca.mybudget.transaction;

import javax.ejb.ApplicationException;

import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;

@ApplicationException(rollback = true)
public class InvalidTransactionTypeException extends AccountTypeException {
	private static final long serialVersionUID = 1L;

	public InvalidTransactionTypeException(String message) {
		super(message);
	}

	public InvalidTransactionTypeException(String message, Throwable cause) {
		super(message, cause);
	}

}
