package br.com.gestao.financeira.pessoal.conta.rules;

import javax.ejb.ApplicationException;

import br.com.gestao.financeira.pessoal.conta.ContaCartao;

@ApplicationException(rollback = true)
public class NaoRemovivelException extends Exception {

	private static final long serialVersionUID = 1L;

	private ContaCartao contaCartao;

	public NaoRemovivelException(String message, ContaCartao contaCartao) {
		super(message);
		this.contaCartao = contaCartao;
	}

	public NaoRemovivelException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContaCartao getContaCartao() {
		return contaCartao;
	}
}
