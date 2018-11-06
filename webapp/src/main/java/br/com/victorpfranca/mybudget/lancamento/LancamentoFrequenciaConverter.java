package br.com.victorpfranca.mybudget.lancamento;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LancamentoFrequenciaConverter implements AttributeConverter<LancamentoFrequencia, Character> {

	@Override
	public Character convertToDatabaseColumn(LancamentoFrequencia attribute) {
		return attribute == null ? null : attribute.getValue();
	}

	@Override
	public LancamentoFrequencia convertToEntityAttribute(Character dbData) {
		return LancamentoFrequencia.fromChar(dbData);
	}
}
