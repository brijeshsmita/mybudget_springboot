package br.com.gestao.financeira.pessoal.controleacesso.view;

import static br.com.gestao.financeira.pessoal.view.FacesUtils.redirect;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;

import br.com.gestao.financeira.pessoal.categoria.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;
import br.com.gestao.financeira.pessoal.controleacesso.UsuarioService;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;
import br.com.gestao.financeira.pessoal.view.FacesMessages;
import br.com.gestao.financeira.pessoal.view.Messages;
import br.com.gestao.financeira.pessoal.view.validation.Email;
import br.com.gestao.financeira.pessoal.view.validation.ValidPassword;

@Named
@ViewScoped
public class SignUpViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioService userService;

//	@NotNull
//	@Size(min = 1, max = 20)
	private String firstName;
//	@NotNull
//	@Size(min = 1, max = 20)
	private String lastName;
	@NotNull
	@Email
	@Size(min = 1, max = 70)
	private String email;
//	@NotNull
//	@Email
//	@Size(min = 1, max = 70)
	private String emailConfirm;
	@NotNull
	@ValidPassword
	private String password;
	private String confirmPassword;
	
	@EJB
	private CredentialsStore credentialsStore;

	@PostConstruct
	protected void init() {

	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailConfirm() {
		return emailConfirm;
	}

	public void setEmailConfirm(String emailConfirm) {
		this.emailConfirm = emailConfirm;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void preCadastrar() {
		Usuario user = new Usuario();
		user.setFirstName("pre_cadastro");
		user.setLastName(StringUtils.EMPTY);
		user.setEmail(StringUtils.lowerCase(getEmail()));
		user.setSenha(getPassword());
		user.setPreCadastro(true);
		userService.create(user);
		SecurityUtils.getSubject().login(new UsernamePasswordToken(getEmail(), password, false));
		redirect("/modulos/onboarding");
	}

	public void completarCadastro() {
		Integer idUsuario = credentialsStore.recuperarIdUsuarioLogado();
		try {
			userService.completarCadastro(idUsuario, firstName);
		} catch (MesmoNomeExistenteException | br.com.gestao.financeira.pessoal.conta.rules.MesmoNomeExistenteException
				| ContaNotNullException | MesLancamentoAlteradoException | TipoContaException
				| CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			FacesMessages.fatal(Messages.msg(e.getMessage()));
			return;
		}
		redirect("/modulos/home");
	}

}
