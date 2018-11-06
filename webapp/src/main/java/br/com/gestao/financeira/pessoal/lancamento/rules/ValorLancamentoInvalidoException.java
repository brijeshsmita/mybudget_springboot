package br.com.gestao.financeira.pessoal.lancamento.rules;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ValorLancamentoInvalidoException extends Exception {

	private static final long serialVersionUID = 1L;

	public ValorLancamentoInvalidoException(String message) {
		super(message);
	}

	public ValorLancamentoInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}

}
