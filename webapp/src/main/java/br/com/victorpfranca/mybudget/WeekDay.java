package br.com.victorpfranca.mybudget;

public enum WeekDay {

	MONDAY(1, "Segunda"), TUESDAY(2, "Terça"), WEDNSDAY(3, "Quarta"), THURDSDAY(4, "Quinta"), FRIDAY(5,
			"Sexta"), SATURDAY(6, "Sábado"), SUNDAY(7, "Domingo");

	private WeekDay(int value, String descricao) {
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

	public int getIntValue() {
		return Character.getNumericValue(getValue());
	}

	public static WeekDay of(int weekDayIndex) {
		for (WeekDay weekDay : WeekDay.values()) {
			if (weekDay.getIntValue() == weekDayIndex) {
				return weekDay;
			}
		}
		return null;
	}

	public static WeekDay fromChar(Character character) {
		for (WeekDay weekDay : WeekDay.values()) {
			if (weekDay.getValue().equals(character)) {
				return weekDay;
			}
		}
		return null;
	}

}
