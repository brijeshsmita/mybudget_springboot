package br.com.victorpfranca.mybudget;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.accesscontroll.User;

public class CredentialStoreMock implements CredentialsStore {

	@Override
	public User recuperarUsuarioLogado() {
		return new User();
	}

	@Override
	public Integer recuperarIdUsuarioLogado() {
		return Integer.valueOf(1);
	}

}
