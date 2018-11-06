package br.com.gestao.financeira.pessoal.infra.dao;

import java.util.List;

public interface NewDAO<T> {

	public T save(T o);

	public void remove(T o);

	public List<T> executeQuery(String query, QueryParam... parameters);
}
