package br.com.victorpfranca.mybudget.orcamento;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class SemContaPadraoException extends Exception {

	private static final long serialVersionUID = 1L;

	public SemContaPadraoException(String message) {
		super(message);
	}

	public SemContaPadraoException(String message, Throwable cause) {
		super(message, cause);
	}

}
