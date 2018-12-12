package br.com.victorpfranca.mybudget.accesscontroll.view;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.victorpfranca.mybudget.accesscontroll.UserService;

@Named
@ViewScoped
public class UserViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserService userService;

	private boolean telaGrid;

	@PostConstruct
	public void init() {

		ResourceBundle.clearCache(UserViewController.class.getClassLoader());
		telaGrid = true;
	}

	public boolean isTelaGrid() {
		return telaGrid;
	}

	public void setTelaGrid(boolean telaGrid) {
		this.telaGrid = telaGrid;
	}

	public void ativar(Integer idUsuario) {
		userService.ativar(idUsuario);
	}

	public void inativar(Integer idUsuario) {
		userService.inativar(idUsuario);
	}

}
