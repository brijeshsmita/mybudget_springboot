package br.com.gestao.financeira.pessoal.view;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import br.com.gestao.financeira.pessoal.infra.dao.Generic;
import br.com.gestao.financeira.pessoal.infra.date.api.CurrentDateSupplier;

@RequestScoped
public class JsfFunctions implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private CurrentDateSupplier dateUtils;

    @Generic
    @Produces
    @Named("currentDate")
    public Date currentDate() {
        return dateUtils.currentDate();
    }

}
