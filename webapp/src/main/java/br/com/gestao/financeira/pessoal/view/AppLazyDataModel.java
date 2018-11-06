package br.com.gestao.financeira.pessoal.view;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.SortOrder;
import br.com.gestao.financeira.pessoal.infra.EntityUtil;
import br.com.gestao.financeira.pessoal.infra.dao.SimpleQueryBuilder;

public abstract class AppLazyDataModel<T> extends org.primefaces.model.LazyDataModel<T> {

	private static final long serialVersionUID = 1L;


	private Class<T> type;
	private EntityManager em;

    protected AppLazyDataModel() {
	}

    protected AppLazyDataModel(EntityManager entityManager, Class<T> type) {
		this.em = entityManager;
		this.type = type;
	}

	@Override
	public T getRowData(String rowKey) {
		return em.find(type, Integer.valueOf(rowKey));
	}

	@Override
	public Object getRowKey(T object) {
        return EntityUtil.getId(object);
	}

	@Override
	public List<T> load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder,
			Map<String, Object> filters) {
		SortOrder oSortOrder = sortOrder.name().equals(org.primefaces.model.SortOrder.ASCENDING.name()) ? SortOrder.ASC
				: SortOrder.DESC;
		SimpleQueryBuilder<T> queryBuilder = new SimpleQueryBuilder<>(type).filter(filters).sort(sortField, oSortOrder);
		List<T> beans = queryBuilder.build(em).setFirstResult(first).setMaxResults(pageSize).getResultList();
		Long totalRegistros = queryBuilder.count(em).getSingleResult();

		this.setRowCount(totalRegistros.intValue());

		return beans;
	}

}
