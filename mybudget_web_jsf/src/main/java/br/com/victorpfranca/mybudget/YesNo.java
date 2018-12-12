package br.com.victorpfranca.mybudget;

public enum YesNo {

	SIM(true, "Sim"), NAO(false, "Não");

	private boolean value;
	private String descricao;

	private YesNo(boolean value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public boolean getValue() {
		return value;
	}

}
