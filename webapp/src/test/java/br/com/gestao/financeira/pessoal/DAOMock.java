package br.com.gestao.financeira.pessoal;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import br.com.gestao.financeira.pessoal.infra.dao.DAO;
import br.com.gestao.financeira.pessoal.infra.dao.QueryParam;

public class DAOMock<T> implements DAO<T> {

	protected List<T> entities;

	public DAOMock() {
		this.entities = new ArrayList<T>();
	}

	public DAOMock(List<T> entities) {
		this.entities = entities;
	}

	public T merge(T object) {

		try {
			Object objectId = getObjectId(object);

			if (objectId == null) {
				setRandomObjectId(object);
			} else {
				T entity = find((Serializable) objectId);
				entities.remove(entity);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		entities.add(object);

		return object;
	}

	protected void setRandomObjectId(T object)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		object.getClass().getMethod("setId", Integer.class).invoke(object, (new Random().nextInt()));
	}

	protected Object getObjectId(T object) {
		Object objectId;
		try {
			objectId = object.getClass().getMethod("getId", new Class<?>[0]).invoke(object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return objectId;
	}

	@Override
	public void remove(T o) {
		T entity = find((Serializable) getObjectId(o));
		entities.remove(entity);
	}

	@Override
	public void persist(T object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(Serializable id) {
		T entity = find((Serializable) id);
		entities.remove(entity);
	}

	@Override
	public T find(Serializable id) {
		T entity = null;

		try {
			for (Iterator<T> iterator = entities.iterator(); iterator.hasNext();) {
				entity = iterator.next();
				Integer entityId;
				entityId = (Integer) entity.getClass().getMethod("getId", new Class<?>[0]).invoke(entity);
				if (entityId.equals(id)) {
					break;
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return entity;
	}

	@Override
	public TypedQuery<T> createNamedQuery(String namedQueryName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> findAll() {
		return entities;
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <X> TypedQuery<X> createQuery(CriteriaQuery<X> cq) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> executeQuery(String query, QueryParam... parameters) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <X> X getSingleResult(CriteriaQuery<X> cq) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(T bean) {
		return entities.contains(bean);
	}

}
