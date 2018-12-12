package br.com.victorpfranca.mybudget.infra.log.erros.view;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.infra.dao.Log;
import br.com.victorpfranca.mybudget.infra.log.erros.LogErros;
import br.com.victorpfranca.mybudget.view.AppLazyDataModel;

public class ErrorsLogDataModel extends AppLazyDataModel<LogErros> {

    private static final long serialVersionUID = 1L;

    @Log
    @Inject
    public ErrorsLogDataModel(EntityManager entityManager) {
        super(entityManager, LogErros.class);
    }

}