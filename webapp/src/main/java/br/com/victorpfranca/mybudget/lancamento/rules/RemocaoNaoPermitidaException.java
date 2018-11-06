package br.com.victorpfranca.mybudget.lancamento.rules;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class RemocaoNaoPermitidaException extends Exception {

	private static final long serialVersionUID = 1L;

	public RemocaoNaoPermitidaException(String message) {
		super(message);
	}

	public RemocaoNaoPermitidaException(String message, Throwable cause) {
		super(message, cause);
	}

}
