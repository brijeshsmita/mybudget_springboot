package br.com.gestao.financeira.pessoal.infra.dao;

public class QueryParam {

	private String paramName;
	private Object paramValue;

	public QueryParam(String paramName, Object paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
	}

	public static QueryParam build(String paramName, Object paramValue) {
		return new QueryParam(paramName, paramValue);
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}

	public Object getParamValue() {
		return paramValue;
	}

}
