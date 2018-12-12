package br.com.victorpfranca.mybudget.infra.log.erros.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import br.com.victorpfranca.mybudget.infra.log.erros.LogErros;

@Named
@ViewScoped
public class ErrorsLogViewController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ErrorsLogDataModel errorsLogDataModel;

    private boolean telaGrid;

    @PostConstruct
    public void init() {
        telaGrid = true;
    }

    public LazyDataModel<LogErros> getLogErrosDataModel() {
        return errorsLogDataModel;
    }

    public boolean isTelaGrid() {
        return telaGrid;
    }

    public void setTelaGrid(boolean telaGrid) {
        this.telaGrid = telaGrid;
    }

}

