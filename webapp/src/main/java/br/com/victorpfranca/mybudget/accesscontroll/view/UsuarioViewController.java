package br.com.victorpfranca.mybudget.accesscontroll.view;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.accesscontroll.UserService;
import br.com.victorpfranca.mybudget.infra.dao.Generic;

@Named
@ViewScoped
public class UsuarioViewController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    @Generic
    private LazyDataModel<User> usuarioDataModel;
    @Inject
    private UserService userService;

    private boolean telaGrid;

    @PostConstruct
    public void init() {

        ResourceBundle.clearCache(UsuarioViewController.class.getClassLoader());
        telaGrid = true;
    }

    public LazyDataModel<User> getUsuarioDataModel() {
        return usuarioDataModel;
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
