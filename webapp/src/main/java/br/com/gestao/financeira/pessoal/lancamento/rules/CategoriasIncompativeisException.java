package br.com.gestao.financeira.pessoal.lancamento.rules;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CategoriasIncompativeisException extends Exception {

	private static final long serialVersionUID = 1L;

	public CategoriasIncompativeisException(String message) {
		super(message);
	}

	public CategoriasIncompativeisException(String message, Throwable cause) {
		super(message, cause);
	}

}
