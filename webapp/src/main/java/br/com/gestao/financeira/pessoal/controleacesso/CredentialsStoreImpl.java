package br.com.gestao.financeira.pessoal.controleacesso;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;

@Default
@Stateless
public class CredentialsStoreImpl implements CredentialsStore {

    @Inject
    private UsuarioService usuarioService;

    @Override
    public Usuario recuperarUsuarioLogado() {
        try {
            return usuarioService.recuperarViaEmail((String)SecurityUtils.getSubject().getPrincipal());
        } catch (NoResultException e) {
            throw new UnauthorizedException(e);
        }
    }
    @Override
    public Integer recuperarIdUsuarioLogado(){
        return Optional.ofNullable(recuperarUsuarioLogado()).map(Usuario::getId)
                .orElseThrow(() -> new UnauthorizedException());
    }

}
