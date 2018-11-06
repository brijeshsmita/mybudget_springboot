package br.com.gestao.financeira.pessoal;

import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;

public class CredentialStoreMock implements CredentialsStore {

	@Override
	public Usuario recuperarUsuarioLogado() {
		return new Usuario();
	}

	@Override
	public Integer recuperarIdUsuarioLogado() {
		return Integer.valueOf(1);
	}

}
