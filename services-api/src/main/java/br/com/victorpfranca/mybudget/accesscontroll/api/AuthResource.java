package br.com.victorpfranca.mybudget.accesscontroll.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthResource {
    
    @POST
    @Path("login")
    String login(UserAuthDTO usuarioAuth);
    
    @POST
    @Path("signup")
    String signup(UserSignup userSignup);
    
    @POST
    @Path("recoverPassword")
    void recoverPassword(PasswordRecovery recSenha);
    
}
