package br.com.gestao.financeira.pessoal.controleacesso.view;

import static br.com.gestao.financeira.pessoal.view.FacesUtils.redirect;

import java.io.Serializable;
import java.util.Optional;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;

import br.com.gestao.financeira.pessoal.categoria.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.controleacesso.LogAcessoService;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;
import br.com.gestao.financeira.pessoal.controleacesso.UsuarioService;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;
import br.com.gestao.financeira.pessoal.view.FacesMessages;
import br.com.gestao.financeira.pessoal.view.Messages;

@Named
@ViewScoped
public class ControleAcessoViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 1)
	private String username;

	@NotNull
	@Size(min = 1)
	private String password;

	private boolean remember = false;

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private LogAcessoService logAcessoService;
	
	@EJB
	private UsuarioService usuarioService;

	public String getFirstName() {
		return Optional.ofNullable(credentialsStore.recuperarUsuarioLogado()).map(Usuario::getFirstName).orElse("");
	}

	public String getEmail() {
		return Optional.ofNullable(credentialsStore.recuperarUsuarioLogado()).map(Usuario::getEmail).orElse("");
	}

	public Boolean getPreCadastro() {
		return Optional.ofNullable(credentialsStore.recuperarUsuarioLogado()).map(Usuario::getPreCadastro)
				.orElse(false);
	}

	public void checkAlreadyLoggedin() {
		if (SecurityUtils.getSubject().isAuthenticated()) {
			Usuario usuario = credentialsStore.recuperarUsuarioLogado();
			if (usuario.getPreCadastro()) {
				redirect("/modulos/onboarding/index");
				return;
			}

			redirect("/modulos/home");
		}
	}

	public void login() {
		try {
			SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, remember));
			Usuario usuario = credentialsStore.recuperarUsuarioLogado();
			logAcessoService.log(usuario);

			// para usuários que haviam se cadastrado antes da implementação do onBoarding
			// com etapa intermediária de informação de first name
			if (usuario.getPreCadastro() && !usuario.getFirstName().equals("pre_cadastro")) {
				usuarioService.completarCadastro(usuario.getId(), usuario.getFirstName());
			} else if (usuario.getPreCadastro()) {
				redirect("/modulos/onboarding/index");
				return;
			}
			redirect("/modulos/home");
		} catch (AuthenticationException | MesmoNomeExistenteException | br.com.gestao.financeira.pessoal.conta.rules.MesmoNomeExistenteException | ContaNotNullException | MesLancamentoAlteradoException | TipoContaException | CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			FacesMessages.error(Messages.msg(AuthenticationException.class.getName()));
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

}
