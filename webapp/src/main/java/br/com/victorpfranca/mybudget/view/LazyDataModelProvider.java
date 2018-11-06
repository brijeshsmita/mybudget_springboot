package br.com.victorpfranca.mybudget.view;

import java.lang.reflect.ParameterizedType;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.infra.dao.Generic;

public class LazyDataModelProvider {
	@Inject
	private EntityManager em;
	
	@Produces
	@Generic
	@Dependent
	@SuppressWarnings("unchecked")
	public <T> org.primefaces.model.LazyDataModel<T> produceGenericDao(InjectionPoint ip){
		ParameterizedType type = (ParameterizedType) ip.getType();
        Class<T> entityType = (Class<T>) type.getActualTypeArguments()[0];
        return new AppLazyDataModel<T>(em, entityType) {
            private static final long serialVersionUID = 1L;
        };
	}
	
}
