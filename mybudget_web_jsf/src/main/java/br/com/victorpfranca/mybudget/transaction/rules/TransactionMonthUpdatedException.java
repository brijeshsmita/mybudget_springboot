package br.com.victorpfranca.mybudget.transaction.rules;

public class TransactionMonthUpdatedException extends Exception {

	private static final long serialVersionUID = 1L;

	public TransactionMonthUpdatedException(String message) {
		super(message);
	}

	public TransactionMonthUpdatedException(String message, Throwable cause) {
		super(message, cause);
	}

}
