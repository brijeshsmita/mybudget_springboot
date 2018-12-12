package br.com.victorpfranca.mybudget.view;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import br.com.victorpfranca.mybudget.infra.dao.Generic;
import br.com.victorpfranca.mybudget.infra.date.api.CurrentDateSupplier;

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
