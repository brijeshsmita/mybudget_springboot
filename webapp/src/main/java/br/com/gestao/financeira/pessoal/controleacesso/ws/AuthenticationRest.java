package br.com.gestao.financeira.pessoal.controleacesso.ws;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;

import br.com.gestao.financeira.pessoal.categoria.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.controleacesso.CadastroUsuarioErrorCodes;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;
import br.com.gestao.financeira.pessoal.controleacesso.UsuarioService;
import br.com.gestao.financeira.pessoal.controleacesso.api.AuthResource;
import br.com.gestao.financeira.pessoal.controleacesso.api.RecuperacaoSenha;
import br.com.gestao.financeira.pessoal.controleacesso.api.UsuarioAuthDTO;
import br.com.gestao.financeira.pessoal.controleacesso.api.UsuarioSignup;
import br.com.gestao.financeira.pessoal.controleacesso.recuperasenha.RecuperacaoSenhaService;
import br.com.gestao.financeira.pessoal.infra.AppExceptionMapper;
import br.com.gestao.financeira.pessoal.infra.exception.SystemException;
import br.com.gestao.financeira.pessoal.infra.security.jwt.JwtActions;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;
import br.com.gestao.financeira.pessoal.view.Messages;

@Path("auth")
public class AuthenticationRest implements AuthResource {

    @Inject
    private JwtActions jwtActions;
    @Inject
    private UsuarioService userService;
    @Inject
    private RecuperacaoSenhaService recuperacaoSenhaService;
    
    @Override
    public String login(UsuarioAuthDTO usuarioAuth) {
        return Optional.ofNullable(usuarioAuth).map(this::realizarLogin).map(jwtActions::jwt).orElse(null);
    }

    @Override
    public String signup(UsuarioSignup usuarioSignup) {
        return Optional.ofNullable(usuarioSignup).map(this::registrarUsuario).map(jwtActions::jwt).orElse(null);
    }
    private String realizarLogin(UsuarioAuthDTO usuarioAuthDTO) {
        SecurityUtils.getSubject().login(new UsernamePasswordToken(usuarioAuthDTO.getEmail(), usuarioAuthDTO.getPassword(), false));
        return usuarioAuthDTO.getEmail();
    }

    private String registrarUsuario(UsuarioSignup usuarioSignup) {
        Usuario usuario = new Usuario();
        usuario.setFirstName(usuarioSignup.getNome());
        usuario.setLastName(usuarioSignup.getSobrenome());
        usuario.setEmail(usuarioSignup.getEmail());
        usuario.setSenha(usuarioSignup.getPassword());
		try {
			userService.create(usuario);
			SecurityUtils.getSubject().login(new UsernamePasswordToken(usuarioSignup.getEmail(), usuarioSignup.getPassword(), false));
			userService.preencherCadastrosIniciais();
		} catch (MesmoNomeExistenteException | br.com.gestao.financeira.pessoal.conta.rules.MesmoNomeExistenteException
				| ContaNotNullException | MesLancamentoAlteradoException | TipoContaException
				| CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			AppExceptionMapper.unwrap(e, SystemException.class, this::handleRegistrarUsuarioExceptions);
		}
        return usuarioSignup.getEmail();
    }

    @Override
    public void recoverPassword(RecuperacaoSenha recSenha) {
        try {
            recuperacaoSenhaService.solicitarRecuperacaoSenha(recSenha.getEmail());
        } catch (Exception e) {
            AppExceptionMapper.unwrap(e, UnknownAccountException.class, this::handleRecuperarPasswordExceptions);
            throw e;
        }
    }

    private void handleRecuperarPasswordExceptions(UnknownAccountException exception) {
        throw new WebApplicationException(Response.status(Status.CONFLICT)
                .entity(Messages.msg(UnknownAccountException.class.getName()))
                .type(MediaType.TEXT_PLAIN)
                .build());
    }

    private void handleRegistrarUsuarioExceptions(SystemException exception) {
        if (exception.getErrorCode() == CadastroUsuarioErrorCodes.EMAIL_JA_CADASTRADO) {
            throw new WebApplicationException(Response.status(Status.CONFLICT)
                .entity(exception.handleMessage(Messages::msg))
                .type(MediaType.TEXT_PLAIN)
                .build());
        }
    }

}
