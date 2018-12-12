package br.com.victorpfranca.mybudget.accesscontroll.view;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.accesscontroll.User;

public class CredentialsStore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public User recuperarUsuarioLogado() {
		User user = new User("John", "Willians", "john@mybudget.com", true, LocalDateConverter.now(),
				LocalDateConverter.now(), BigDecimal.ZERO, false);
		return user;
	}

}
