package br.com.gestao.financeira.pessoal.conta.rules;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class MesmoNomeExistenteException extends Exception {

	private static final long serialVersionUID = 1L;

	public MesmoNomeExistenteException(String message) {
		super(message);
	}

	public MesmoNomeExistenteException(String message, Throwable cause) {
		super(message, cause);
	}
}
