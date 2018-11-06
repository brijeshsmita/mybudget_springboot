package br.com.victorpfranca.mybudget.accesscontroll.ws;

import java.util.Optional;

import javax.inject.Inject;

import br.com.victorpfranca.mybudget.accesscontroll.UserService;
import br.com.victorpfranca.mybudget.accesscontroll.ws.Helpers.UsuarioDTOConverter;
import br.com.victorpfranca.mybudget.controleacesso.api.UsuarioDTO;
import br.com.victorpfranca.mybudget.controleacesso.api.UsuarioResource;

public class UsuarioResourceImpl implements UsuarioResource {

    @Inject
    private UserService userService;
    
    private String email;
    

    public UsuarioResourceImpl email(String email) {
        this.email=email;
        return this;
    }
    
    @Override
    public UsuarioDTO recuperar() {
        return Optional.ofNullable(userService.recuperarViaEmail(email))
            .map(new UsuarioDTOConverter()::usuarioDTO)
            .orElse(null);
    }

}
