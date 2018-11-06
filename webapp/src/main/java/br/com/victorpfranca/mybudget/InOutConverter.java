package br.com.victorpfranca.mybudget;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class InOutConverter implements AttributeConverter<InOut, Character> {

	@Override
	public Character convertToDatabaseColumn(InOut attribute) {
		return attribute == null ? null : attribute.getValue();
	}

	@Override
	public InOut convertToEntityAttribute(Character dbData) {
		return InOut.fromChar(dbData);
	}
}
