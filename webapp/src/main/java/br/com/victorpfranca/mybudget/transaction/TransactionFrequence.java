package br.com.victorpfranca.mybudget.transaction;

public enum TransactionFrequence {

	MENSAL(1, "Mensal"), QUINZENAL(2, "Quinzenal"), SEMANAL(3, "Semanal");

	private TransactionFrequence(int value, String descricao) {
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
	
	public static TransactionFrequence fromChar(Character value) {
		for (TransactionFrequence transactionFrequence : TransactionFrequence.values()) {
			if (transactionFrequence.getValue().equals(value)) {
				return transactionFrequence;
			}
		}
		return null;
	}

}
