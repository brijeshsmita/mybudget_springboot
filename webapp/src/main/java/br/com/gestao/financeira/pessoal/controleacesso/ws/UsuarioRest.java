package br.com.gestao.financeira.pessoal.controleacesso.ws;

import javax.inject.Inject;
import javax.ws.rs.Path;

import br.com.gestao.financeira.pessoal.controleacesso.api.UsuariosResource;

@Path("/usuario")
public class UsuarioRest {
    
    @Inject
    private UsuariosResourceImpl usuariosResourceImpl;

    @Path("/")
    public UsuariosResource usuariosResource() {
        return usuariosResourceImpl;
    }
    
}
