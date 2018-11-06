package br.com.victorpfranca.mybudget.accesscontroll;

import javax.ejb.Remote;

@Remote
public interface CredentialsStore {
    User recuperarUsuarioLogado();

    Integer recuperarIdUsuarioLogado();
}
