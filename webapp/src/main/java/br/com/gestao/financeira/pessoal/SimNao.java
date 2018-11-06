package br.com.gestao.financeira.pessoal;

public enum SimNao {

	SIM(true, "Sim"), NAO(false, "Não");

	private boolean value;
	private String descricao;

	private SimNao(boolean value, String descricao) {
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
