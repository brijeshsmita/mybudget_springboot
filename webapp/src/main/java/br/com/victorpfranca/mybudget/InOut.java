package br.com.victorpfranca.mybudget;

public enum InOut {

	E(0, "Receita"), S(1, "Despesa");

	private InOut(int value, String descricao) {
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

	public static InOut fromChar(Character character) {
		for (InOut inOut : InOut.values()) {
			if (inOut.getValue().equals(character)) {
				return inOut;
			}
		}
		return null;
	}
	
	
}
