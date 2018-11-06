package br.com.gestao.financeira.pessoal.controleacesso.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface UsuarioResource {

    @GET
    UsuarioDTO recuperar();

}