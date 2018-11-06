package br.com.victorpfranca.mybudget.transaction;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class FrequencyTransactionConverter implements AttributeConverter<TransactionFrequence, Character> {

	@Override
	public Character convertToDatabaseColumn(TransactionFrequence attribute) {
		return attribute == null ? null : attribute.getValue();
	}

	@Override
	public TransactionFrequence convertToEntityAttribute(Character dbData) {
		return TransactionFrequence.fromChar(dbData);
	}
}
