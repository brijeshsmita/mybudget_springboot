package br.com.gestao.financeira.pessoal.lancamento;

public enum LancamentoFrequencia {

	MENSAL(1, "Mensal"), QUINZENAL(2, "Quinzenal"), SEMANAL(3, "Semanal");

	private LancamentoFrequencia(int value, String descricao) {
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
	
	public static LancamentoFrequencia fromChar(Character value) {
		for (LancamentoFrequencia lancamentoFrequencia : LancamentoFrequencia.values()) {
			if (lancamentoFrequencia.getValue().equals(value)) {
				return lancamentoFrequencia;
			}
		}
		return null;
	}

}
