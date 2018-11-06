package br.com.gestao.financeira.pessoal.infra.date;

import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import br.com.gestao.financeira.pessoal.controleacesso.Usuario;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CurrentDateSupplier implements br.com.gestao.financeira.pessoal.infra.date.api.CurrentDateSupplier {

    @PersistenceContext(unitName = "meussaldos")
    protected EntityManager entityManager;

    @Override
    public Date currentDate() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        cq.select(cb.currentTimestamp());
        cq.from(Usuario.class);
        return entityManager.createQuery(cq).setMaxResults(1).getSingleResult();
    }

}
