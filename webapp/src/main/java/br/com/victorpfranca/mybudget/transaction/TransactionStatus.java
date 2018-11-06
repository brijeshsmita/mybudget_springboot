package br.com.victorpfranca.mybudget.transaction;

public enum TransactionStatus {

	NAO_CONFIRMADO(1, "Agendado"), CONFIRMADO(2, "Pago");

	private TransactionStatus(int value, String descricao) {
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

	public static TransactionStatus fromChar(Character value) {
		for (TransactionStatus transactionStatus : TransactionStatus.values()) {
			if (transactionStatus.getValue().equals(value)) {
				return transactionStatus;
			}
		}
		return null;
	}

}
