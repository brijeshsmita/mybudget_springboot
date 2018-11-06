package br.com.victorpfranca.mybudget.accesscontroll.ws;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;

import br.com.victorpfranca.mybudget.controleacesso.api.UsuarioResource;
import br.com.victorpfranca.mybudget.controleacesso.api.UsuariosResource;

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
