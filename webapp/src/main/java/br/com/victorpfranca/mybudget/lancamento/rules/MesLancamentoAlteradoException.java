package br.com.victorpfranca.mybudget.lancamento.rules;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class MesLancamentoAlteradoException extends Exception {

	private static final long serialVersionUID = 1L;

	public MesLancamentoAlteradoException(String message) {
		super(message);
	}

	public MesLancamentoAlteradoException(String message, Throwable cause) {
		super(message, cause);
	}

}
