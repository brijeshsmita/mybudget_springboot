package br.com.victorpfranca.mybudget.infra.dao;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EntityManagerProvider {

	@PersistenceContext(unitName = "meussaldos")
	protected EntityManager emPublic;
    @PersistenceContext(unitName = "meussaldosLog")
    protected EntityManager emLog;

	@Produces
	@Default
    @RequestScoped
    public EntityManager createEntityManager() {
		return emPublic;
	}

    @Produces
    @Log
    @RequestScoped
    public EntityManager createLogEntityManager() {
        return emLog;
    }

}
