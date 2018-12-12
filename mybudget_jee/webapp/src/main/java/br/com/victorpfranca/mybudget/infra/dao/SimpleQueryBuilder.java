package br.com.victorpfranca.mybudget.infra.dao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;

import br.com.victorpfranca.mybudget.SortOrder;

public class SimpleQueryBuilder<T> {

	private Class<T> resultClass;
	private Map<String, Object> filters;
	private Map<String, From<?, ?>> paths;
	private String sortField;
	private SortOrder sortOrder;

	public SimpleQueryBuilder(Class<T> resultClass) {
		this.resultClass = resultClass;
		this.filters = new HashMap<>();
		this.paths = new HashMap<>();
	}

	public SimpleQueryBuilder<T> filter(Map<String, Object> filters) {
		this.filters.putAll(filters);
		return this;
	}

	public SimpleQueryBuilder<T> filter(String key, Object value) {
		filters.put(key, value);
		return this;
	}

	public TypedQuery<Long> count(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
		EntityType<T> entity = em.getMetamodel().entity(resultClass);
		Root<T> from = criteriaQuery.from(entity);

		addRestrictions(cb, criteriaQuery, from);

		criteriaQuery = criteriaQuery.select(cb.count(from.get(entity.getId(entity.getIdType().getJavaType()))));

		return em.createQuery(criteriaQuery);
	}

	public TypedQuery<T> build(EntityManager em) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = cb.createQuery(resultClass);
		EntityType<T> entity = em.getMetamodel().entity(resultClass);
		Root<T> from = criteriaQuery.from(entity);

		addRestrictions(cb, criteriaQuery, from);

		setOrder(cb, criteriaQuery, from);

		criteriaQuery = criteriaQuery.select(from);

		return em.createQuery(criteriaQuery);
	}

	private void setOrder(CriteriaBuilder cb, CriteriaQuery<T> criteriaQuery, Root<T> from) {
		if (sortField != null) {
			Path<?> sortPath = getPath(from, sortField);
			if (SortOrder.DESC.equals(sortOrder)) {
				criteriaQuery.orderBy(cb.desc(sortPath));
			} else {
				criteriaQuery.orderBy(cb.asc(sortPath));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addRestrictions(CriteriaBuilder cb, CriteriaQuery<?> criteriaQuery, Root<T> from) {
		List<Predicate> restrictions = new ArrayList<>();

		for (Entry<String, Object> filter : filters.entrySet()) {
			Path<?> path = getPath(from, filter.getKey());
			Object valor = filter.getValue();
			if (valor != null) {
				if (valor.getClass() == String.class) {
					restrictions.add(cb.like(cb.lower((Expression<String>) path),
							MessageFormat.format("%{0}%", ((String) valor).toLowerCase())));
				} else {
					restrictions.add(cb.equal(path, filter.getValue()));
				}
			}
		}

		if (!restrictions.isEmpty()) {
			criteriaQuery = criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		}
	}

	private Path<?> getPath(Root<T> from, String key) {
		Path<?> path;
		if (key.contains(".")) {
			String pathKey = getPathKey(key);
			String fieldKey = getFieldKey(key);
			From<?, ?> join = retrieveCriteriaPath(from, pathKey);
			path = join.get(fieldKey);
		} else {
			path = from.get(key);
		}
		return path;
	}

	private From<?, ?> retrieveCriteriaPath(Root<T> from, String pathKey) {
		From<?, ?> join = paths.get(pathKey);
		if (join == null) {
			join = retrieveCriteriaPath(from, pathKey.split("\\."));
		}
		return join;
	}

	private From<?, ?> retrieveCriteriaPath(From<?, ?> from, String[] pathKeySplit) {
		From<?, ?> join = from;
		for (int i = 0; i < pathKeySplit.length; i++) {
			String itKey = joinArrayToString(Arrays.copyOfRange(pathKeySplit, 0, i + 1), '.');
			From<?, ?> itJoin = paths.get(itKey);
			if (itJoin == null) {
				itJoin = join.join(getPathKey(itKey), JoinType.INNER);
			}
			join = itJoin;
		}
		return join;
	}

	private String getPathKey(String path) {
		int lastIndexOf = path.lastIndexOf('.');
		if (lastIndexOf < 0) {
			lastIndexOf = path.length();
		}
		return path.substring(0, lastIndexOf);
	}

	private String getFieldKey(String path) {
		int lastIndexOf = path.lastIndexOf('.');
		if (lastIndexOf < 0) {
			return path.substring(0);
		}
		return path.substring(lastIndexOf + 1);
	}

	static String joinArrayToString(Object[] arr, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i != 0)
				sb.append(separator);
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	public SimpleQueryBuilder<T> sort(String sortField, SortOrder sortOrder) {
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		return this;
	}

}
