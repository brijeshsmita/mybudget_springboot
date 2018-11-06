package br.com.gestao.financeira.pessoal.infra.log.erros.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import br.com.gestao.financeira.pessoal.infra.log.erros.LogErros;

@Named
@ViewScoped
public class LogErrosViewController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private LogErrosDataModel logErrosDataModel;

    private boolean telaGrid;

    @PostConstruct
    public void init() {
        telaGrid = true;
    }

    public LazyDataModel<LogErros> getLogErrosDataModel() {
        return logErrosDataModel;
    }

    public boolean isTelaGrid() {
        return telaGrid;
    }

    public void setTelaGrid(boolean telaGrid) {
        this.telaGrid = telaGrid;
    }

}

