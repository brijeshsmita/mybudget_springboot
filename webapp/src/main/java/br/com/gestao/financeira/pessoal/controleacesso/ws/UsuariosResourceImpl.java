package br.com.gestao.financeira.pessoal.controleacesso.ws;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;

import br.com.gestao.financeira.pessoal.controleacesso.api.UsuarioResource;
import br.com.gestao.financeira.pessoal.controleacesso.api.UsuariosResource;

public class UsuariosResourceImpl implements UsuariosResource {

    @Inject
    private UsuarioResourceImpl usuarioResourceImpl;
    
    @Override
    public UsuarioResource usuario(String email) {
        if (SecurityUtils.getSubject().getPrincipal().equals(email)) {
            return usuarioResourceImpl.email(email);
        }
        return null;
    }

}
