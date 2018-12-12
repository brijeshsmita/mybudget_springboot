package br.com.victorpfranca.mybudget.accesscontroll.ws;

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

import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.accesscontroll.UserService;
import br.com.victorpfranca.mybudget.accesscontroll.UserSingupErrorCodes;
import br.com.victorpfranca.mybudget.accesscontroll.api.AuthResource;
import br.com.victorpfranca.mybudget.accesscontroll.api.PasswordRecovery;
import br.com.victorpfranca.mybudget.accesscontroll.api.UserAuthDTO;
import br.com.victorpfranca.mybudget.accesscontroll.api.UserSignup;
import br.com.victorpfranca.mybudget.accesscontroll.passwordrecovery.PasswordRecoveryService;
import br.com.victorpfranca.mybudget.category.SameNameException;
import br.com.victorpfranca.mybudget.infra.AppExceptionMapper;
import br.com.victorpfranca.mybudget.infra.exception.SystemException;
import br.com.victorpfranca.mybudget.infra.security.jwt.JwtActions;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;
import br.com.victorpfranca.mybudget.view.Messages;

@Path("auth")
public class AuthenticationRest implements AuthResource {

	@Inject
	private JwtActions jwtActions;
	@Inject
	private UserService userService;
	@Inject
	private PasswordRecoveryService passwordRecoveryService;

	@Override
	public String login(UserAuthDTO usuarioAuth) {
		return Optional.ofNullable(usuarioAuth).map(this::realizarLogin).map(jwtActions::jwt).orElse(null);
	}

	@Override
	public String signup(UserSignup usuarioSignup) {
		return Optional.ofNullable(usuarioSignup).map(this::registrarUsuario).map(jwtActions::jwt).orElse(null);
	}

	private String realizarLogin(UserAuthDTO usuarioAuthDTO) {
		SecurityUtils.getSubject()
				.login(new UsernamePasswordToken(usuarioAuthDTO.getEmail(), usuarioAuthDTO.getPassword(), false));
		return usuarioAuthDTO.getEmail();
	}

	private String registrarUsuario(UserSignup usuarioSignup) {
		User user = new User();
		user.setFirstName(usuarioSignup.getNome());
		user.setLastName(usuarioSignup.getSobrenome());
		user.setEmail(usuarioSignup.getEmail());
		user.setSenha(usuarioSignup.getPassword());
		try {
			userService.create(user);
			SecurityUtils.getSubject()
					.login(new UsernamePasswordToken(usuarioSignup.getEmail(), usuarioSignup.getPassword(), false));
			userService.preencherCadastrosIniciais();
		} catch (SameNameException | br.com.victorpfranca.mybudget.account.rules.SameNameException
				| NullableAccountException | TransactionMonthUpdatedException | AccountTypeException
				| IncompatibleCategoriesException | InvalidTransactionValueException e) {
			AppExceptionMapper.unwrap(e, SystemException.class, this::handleRegistrarUsuarioExceptions);
		}
		return usuarioSignup.getEmail();
	}

	@Override
	public void recoverPassword(PasswordRecovery recSenha) {
		try {
			passwordRecoveryService.solicitarRecuperacaoSenha(recSenha.getEmail());
		} catch (Exception e) {
			AppExceptionMapper.unwrap(e, UnknownAccountException.class, this::handleRecuperarPasswordExceptions);
			throw e;
		}
	}

	private void handleRecuperarPasswordExceptions(UnknownAccountException exception) {
		throw new WebApplicationException(Response.status(Status.CONFLICT)
				.entity(Messages.msg(UnknownAccountException.class.getName())).type(MediaType.TEXT_PLAIN).build());
	}

	private void handleRegistrarUsuarioExceptions(SystemException exception) {
		if (exception.getErrorCode() == UserSingupErrorCodes.EMAIL_JA_CADASTRADO) {
			throw new WebApplicationException(Response.status(Status.CONFLICT)
					.entity(exception.handleMessage(Messages::msg)).type(MediaType.TEXT_PLAIN).build());
		}
	}

}
