package br.com.gestao.financeira.pessoal.lancamento.rules;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class TipoContaAlteradoException extends Exception {

	private static final long serialVersionUID = 1L;

	public TipoContaAlteradoException(String message) {
		super(message);
	}

	public TipoContaAlteradoException(String message, Throwable cause) {
		super(message, cause);
	}

}
