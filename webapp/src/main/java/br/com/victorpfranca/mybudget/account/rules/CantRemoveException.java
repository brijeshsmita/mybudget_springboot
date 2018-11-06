package br.com.victorpfranca.mybudget.account.rules;

import javax.ejb.ApplicationException;

import br.com.victorpfranca.mybudget.account.CreditCardAccount;

@ApplicationException(rollback = true)
public class CantRemoveException extends Exception {

	private static final long serialVersionUID = 1L;

	private CreditCardAccount creditCardAccount;

	public CantRemoveException(String message, CreditCardAccount creditCardAccount) {
		super(message);
		this.creditCardAccount = creditCardAccount;
	}

	public CantRemoveException(String message, Throwable cause) {
		super(message, cause);
	}

	public CreditCardAccount getAccountCartao() {
		return creditCardAccount;
	}
}
