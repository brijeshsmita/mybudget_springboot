package br.com.victorpfranca.mybudget.account;

public enum AccountType {
	
	CONTA_BANCO(0, "Account Bancária"), CARTAO_CREDITO(1, "Cartão de Crédito"), CONTA_DINHEIRO(2, "Dinheiro");
	
	private AccountType(int value, String descricao) {
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
	
	public static AccountType fromChar(Character value) {
		for (AccountType accountType : AccountType.values()) {
			if (accountType.getValue().equals(value)) {
				return accountType;
			}
		}
		return null;
	}

}
