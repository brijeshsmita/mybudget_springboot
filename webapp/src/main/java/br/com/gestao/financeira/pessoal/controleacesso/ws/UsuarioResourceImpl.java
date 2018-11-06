package br.com.gestao.financeira.pessoal.controleacesso.ws;

import java.util.Optional;

import javax.inject.Inject;

import br.com.gestao.financeira.pessoal.controleacesso.UsuarioService;
import br.com.gestao.financeira.pessoal.controleacesso.api.UsuarioDTO;
import br.com.gestao.financeira.pessoal.controleacesso.api.UsuarioResource;
import br.com.gestao.financeira.pessoal.controleacesso.ws.Helpers.UsuarioDTOConverter;

public class UsuarioResourceImpl implements UsuarioResource {

    @Inject
    private UsuarioService usuarioService;
    
    private String email;
    

    public UsuarioResourceImpl email(String email) {
        this.email=email;
        return this;
    }
    
    @Override
    public UsuarioDTO recuperar() {
        return Optional.ofNullable(usuarioService.recuperarViaEmail(email))
            .map(new UsuarioDTOConverter()::usuarioDTO)
            .orElse(null);
    }

}
