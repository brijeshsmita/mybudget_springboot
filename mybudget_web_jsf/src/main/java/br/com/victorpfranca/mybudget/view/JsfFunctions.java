package br.com.victorpfranca.mybudget.view;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@RequestScoped
public class JsfFunctions implements Serializable {
	private static final long serialVersionUID = 1L;

	@Produces
	@Named("currentDate")
	public Date currentDate() {
		return null;
	}

}
