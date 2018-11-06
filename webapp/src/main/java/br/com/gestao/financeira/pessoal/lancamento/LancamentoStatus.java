package br.com.gestao.financeira.pessoal.lancamento;

public enum LancamentoStatus {

	NAO_CONFIRMADO(1, "Agendado"), CONFIRMADO(2, "Pago");

	private LancamentoStatus(int value, String descricao) {
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

	public static LancamentoStatus fromChar(Character value) {
		for (LancamentoStatus lancamentoStatus : LancamentoStatus.values()) {
			if (lancamentoStatus.getValue().equals(value)) {
				return lancamentoStatus;
			}
		}
		return null;
	}

}
