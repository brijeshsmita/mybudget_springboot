package br.com.victorpfranca.mybudget.accesscontroll.ws;

import javax.inject.Inject;
import javax.ws.rs.Path;

import br.com.victorpfranca.mybudget.accesscontroll.api.UsersResource;

@Path("/user")
public class UserRest {
    
    @Inject
    private UsersResourceImpl usersResourceImpl;

    @Path("/")
    public UsersResource usuariosResource() {
        return usersResourceImpl;
    }
    
}
