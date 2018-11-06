package br.com.gestao.financeira.pessoal.controleacesso;

import javax.ejb.Remote;

@Remote
public interface CredentialsStore {
    Usuario recuperarUsuarioLogado();

    Integer recuperarIdUsuarioLogado();
}
