package br.com.victorpfranca.mybudget.controleacesso.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UsuariosResource {
    
    @Path("{email}")
    UsuarioResource usuario(@PathParam("email") String email);

}

