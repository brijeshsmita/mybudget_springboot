package br.com.gestao.financeira.pessoal.conta;

public enum ContaTipo {
	
	CONTA_BANCO(0, "Conta Bancária"), CARTAO_CREDITO(1, "Cartão de Crédito"), CONTA_DINHEIRO(2, "Dinheiro");
	
	private ContaTipo(int value, String descricao) {
		this.value = Character.forDigit(value, Character.MAX_RADIX);
		this.descricao = descricao;
	}

	private final char value;
	private final String descricao;

	public String getDescricao() {
		return descricao;
	}

	public Character getValue() {
		return value;
	}
	
	public static ContaTipo fromChar(Character value) {
		for (ContaTipo contaTipo : ContaTipo.values()) {
			if (contaTipo.getValue().equals(value)) {
				return contaTipo;
			}
		}
		return null;
	}

}
