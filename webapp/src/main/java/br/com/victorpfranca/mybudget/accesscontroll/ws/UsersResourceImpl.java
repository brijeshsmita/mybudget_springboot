package br.com.victorpfranca.mybudget.accesscontroll.ws;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;

import br.com.victorpfranca.mybudget.accesscontroll.api.UserResource;
import br.com.victorpfranca.mybudget.accesscontroll.api.UsersResource;

public class UsersResourceImpl implements UsersResource {

	@Inject
	private UserResourceImpl userResourceImpl;

	@Override
	public UserResource usuario(String email) {
		if (SecurityUtils.getSubject().getPrincipal().equals(email)) {
			return userResourceImpl.email(email);
		}
		return null;
	}

}
