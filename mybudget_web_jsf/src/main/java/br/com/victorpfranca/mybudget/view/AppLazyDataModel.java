package br.com.victorpfranca.mybudget.view;

import java.util.List;
import java.util.Map;

public abstract class AppLazyDataModel<T> extends org.primefaces.model.LazyDataModel<T> {

	private static final long serialVersionUID = 1L;

	private Class<T> type;

	protected AppLazyDataModel() {
	}

	protected AppLazyDataModel(Class<T> type) {
		this.type = type;
	}

	@Override
	public T getRowData(String rowKey) {
		return null;
	}

	@Override
	public Object getRowKey(T object) {
		return null;
	}

	@Override
	public List<T> load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder,
			Map<String, Object> filters) {
		return null;
	}

}
