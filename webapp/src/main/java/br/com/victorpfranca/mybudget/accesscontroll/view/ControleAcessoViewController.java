package br.com.victorpfranca.mybudget.accesscontroll.view;

import static br.com.victorpfranca.mybudget.view.FacesUtils.redirect;

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

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.accesscontroll.LogAcessoService;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.accesscontroll.UserService;
import br.com.victorpfranca.mybudget.category.SameNameException;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;
import br.com.victorpfranca.mybudget.view.FacesMessages;
import br.com.victorpfranca.mybudget.view.Messages;

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
	private UserService userService;

	public String getFirstName() {
		return Optional.ofNullable(credentialsStore.recuperarUsuarioLogado()).map(User::getFirstName).orElse("");
	}

	public String getEmail() {
		return Optional.ofNullable(credentialsStore.recuperarUsuarioLogado()).map(User::getEmail).orElse("");
	}

	public Boolean getPreCadastro() {
		return Optional.ofNullable(credentialsStore.recuperarUsuarioLogado()).map(User::getPreCadastro)
				.orElse(false);
	}

	public void checkAlreadyLoggedin() {
		if (SecurityUtils.getSubject().isAuthenticated()) {
			User user = credentialsStore.recuperarUsuarioLogado();
			if (user.getPreCadastro()) {
				redirect("/modulos/onboarding/index");
				return;
			}

			redirect("/modulos/home");
		}
	}

	public void login() {
		try {
			SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, remember));
			User user = credentialsStore.recuperarUsuarioLogado();
			logAcessoService.log(user);

			// para usuários que haviam se cadastrado antes da implementação do onBoarding
			// com etapa intermediária de informação de first name
			if (user.getPreCadastro() && !user.getFirstName().equals("pre_cadastro")) {
				userService.completarCadastro(user.getId(), user.getFirstName());
			} else if (user.getPreCadastro()) {
				redirect("/modulos/onboarding/index");
				return;
			}
			redirect("/modulos/home");
		} catch (AuthenticationException | SameNameException | br.com.victorpfranca.mybudget.account.rules.SameNameException | NullableAccountException | TransactionMonthUpdatedException | AccountTypeException | IncompatibleCategoriesException | InvalidTransactionValueException e) {
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
