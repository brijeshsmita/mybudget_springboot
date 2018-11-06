package br.com.gestao.financeira.pessoal.conta;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ContaTipoConverter implements AttributeConverter<ContaTipo, Character> {

	@Override
	public Character convertToDatabaseColumn(ContaTipo attribute) {
		return attribute == null ? null : attribute.getValue();
	}

	@Override
	public ContaTipo convertToEntityAttribute(Character dbData) {
		return ContaTipo.fromChar(dbData);
	}
}
