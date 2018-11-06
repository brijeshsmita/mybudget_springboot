package br.com.victorpfranca.mybudget.accesscontroll.ws;

import javax.inject.Inject;
import javax.ws.rs.Path;

import br.com.victorpfranca.mybudget.controleacesso.api.UsuariosResource;

@Path("/user")
public class UsuarioRest {
    
    @Inject
    private UsuariosResourceImpl usuariosResourceImpl;

    @Path("/")
    public UsuariosResource usuariosResource() {
        return usuariosResourceImpl;
    }
    
}
