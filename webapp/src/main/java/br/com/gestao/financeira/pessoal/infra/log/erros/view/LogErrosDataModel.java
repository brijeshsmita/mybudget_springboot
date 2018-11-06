package br.com.gestao.financeira.pessoal.infra.log.erros.view;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.infra.dao.Log;
import br.com.gestao.financeira.pessoal.infra.log.erros.LogErros;
import br.com.gestao.financeira.pessoal.view.AppLazyDataModel;

public class LogErrosDataModel extends AppLazyDataModel<LogErros> {

    private static final long serialVersionUID = 1L;

    @Log
    @Inject
    public LogErrosDataModel(EntityManager entityManager) {
        super(entityManager, LogErros.class);
    }

}