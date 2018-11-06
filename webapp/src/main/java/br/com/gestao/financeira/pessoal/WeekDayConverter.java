package br.com.gestao.financeira.pessoal;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class WeekDayConverter implements AttributeConverter<WeekDay, Character> {

	@Override
	public Character convertToDatabaseColumn(WeekDay attribute) {
		return attribute == null ? null : attribute.getValue();
	}

	@Override
	public WeekDay convertToEntityAttribute(Character dbData) {
		return WeekDay.fromChar(dbData);
	}
}
