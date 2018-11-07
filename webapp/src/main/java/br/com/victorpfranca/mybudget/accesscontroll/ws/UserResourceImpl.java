package br.com.victorpfranca.mybudget.accesscontroll.ws;

import java.util.Optional;

import javax.inject.Inject;

import br.com.victorpfranca.mybudget.accesscontroll.UserService;
import br.com.victorpfranca.mybudget.accesscontroll.api.UserDTO;
import br.com.victorpfranca.mybudget.accesscontroll.api.UserResource;
import br.com.victorpfranca.mybudget.accesscontroll.ws.Helpers.UserDTOConverter;

public class UserResourceImpl implements UserResource {

    @Inject
    private UserService userService;
    
    private String email;
    

    public UserResourceImpl email(String email) {
        this.email=email;
        return this;
    }
    
    @Override
    public UserDTO recuperar() {
        return Optional.ofNullable(userService.recuperarViaEmail(email))
				.map(new UserDTOConverter()::usuarioDTO)
            .orElse(null);
    }

}
