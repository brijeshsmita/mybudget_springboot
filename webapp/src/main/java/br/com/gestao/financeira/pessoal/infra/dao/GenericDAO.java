package br.com.gestao.financeira.pessoal.infra.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

@Stateless
public class GenericDAO<T> implements DAO<T>, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	private Class<T> entityType;

	public GenericDAO() {
	}

	public Class<T> getEntityType() {
		return entityType;
	}

	public void setEntityType(Class<T> entityType) {
		this.entityType = entityType;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persist(T object) {
		em.persist(object);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public T merge(T object) {
		return em.merge(object);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(T object) {
		em.remove(em.merge(object));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Serializable id) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		EntityType<T> entity = em.getMetamodel().entity(entityType);

		CriteriaDelete<T> cd = cb.createCriteriaDelete(entityType);
		Root<T> from = cd.from(entity);

		SingularAttribute<? super T, ?> idAttribute = entity.getId(entity.getIdType().getJavaType());

		cd = cd.where(cb.equal(from.get(idAttribute), id));

		em.createQuery(cd).executeUpdate();
	}

	@Override
	public T find(Serializable id) {
		return em.find(entityType, id);
	}

	@Override
	public List<T> findAll() {
		CriteriaQuery<T> criteria = em.getCriteriaBuilder().createQuery(entityType);
		Root<T> bean = criteria.from(entityType);
		criteria.select(bean);
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return em.getCriteriaBuilder();
	}

	@Override
	public <X> TypedQuery<X> createQuery(CriteriaQuery<X> cq) {
		return em.createQuery(cq);
	}

	@Override
	public TypedQuery<T> createNamedQuery(String namedQueryName) {
		return em.createNamedQuery(namedQueryName, getEntityType());
	}

	@Override
	public List<T> executeQuery(String queryString, QueryParam... parameters) {
		TypedQuery<T> query = em.createNamedQuery(queryString, entityType);

		for (int i = 0; i < parameters.length; i++) {
			query.setParameter(parameters[i].getParamName(), parameters[i].getParamValue());
		}

		return query.getResultList();
	}

	@Override
	public <X> X getSingleResult(CriteriaQuery<X> cq) {
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public boolean contains(T bean) {
		return em.contains(bean);
	}

}
