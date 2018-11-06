package br.com.gestao.financeira.pessoal.controleacesso.view;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import br.com.gestao.financeira.pessoal.controleacesso.Usuario;
import br.com.gestao.financeira.pessoal.controleacesso.UsuarioService;
import br.com.gestao.financeira.pessoal.infra.dao.Generic;

@Named
@ViewScoped
public class UsuarioViewController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    @Generic
    private LazyDataModel<Usuario> usuarioDataModel;
    @Inject
    private UsuarioService usuarioService;

    private boolean telaGrid;

    @PostConstruct
    public void init() {

        ResourceBundle.clearCache(UsuarioViewController.class.getClassLoader());
        telaGrid = true;
    }

    public LazyDataModel<Usuario> getUsuarioDataModel() {
        return usuarioDataModel;
    }

    public boolean isTelaGrid() {
        return telaGrid;
    }

    public void setTelaGrid(boolean telaGrid) {
        this.telaGrid = telaGrid;
    }

    public void ativar(Integer idUsuario) {
        usuarioService.ativar(idUsuario);
    }

    public void inativar(Integer idUsuario) {
        usuarioService.inativar(idUsuario);
    }

}
