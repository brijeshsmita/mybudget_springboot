package br.com.victorpfranca.mybudget.accesscontroll.view;

import static br.com.victorpfranca.mybudget.view.FacesUtils.redirect;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.accesscontroll.UserService;
import br.com.victorpfranca.mybudget.view.validation.Email;
import br.com.victorpfranca.mybudget.view.validation.ValidPassword;

@Named
@ViewScoped
public class SignUpViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserService userService;

	// @NotNull
	// @Size(min = 1, max = 20)
	private String firstName;
	// @NotNull
	// @Size(min = 1, max = 20)
	private String lastName;
	@NotNull
	@Email
	@Size(min = 1, max = 70)
	private String email;
	// @NotNull
	// @Email
	// @Size(min = 1, max = 70)
	private String emailConfirm;
	@NotNull
	@ValidPassword
	private String password;
	private String confirmPassword;

	@Inject
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
		User user = new User("John", "Willians", "john@mybudget.com", true, LocalDateConverter.now(),
				LocalDateConverter.now(), BigDecimal.ZERO, false);
		userService.create(user);
		// SecurityUtils.getSubject().login(new UsernamePasswordToken(getEmail(),
		// password, false));
		redirect("/modulos/onboarding");
	}

	public void completarCadastro() {
		// Integer idUsuario = credentialsStore.recuperarIdUsuarioLogado();
		// try {
		// userService.completarCadastro(idUsuario, firstName);
		// } catch (SameNameException |
		// br.com.victorpfranca.mybudget.account.rules.SameNameException
		// | NullableAccountException | TransactionMonthUpdatedException |
		// AccountTypeException
		// | IncompatibleCategoriesException | InvalidTransactionValueException e) {
		// FacesMessages.fatal(Messages.msg(e.getMessage()));
		// return;
		// }
		redirect("/modulos/home");
	}

}
