package br.com.victorpfranca.mybudget.transaction;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TransactionStatusConverter implements AttributeConverter<TransactionStatus, Character> {

	@Override
	public Character convertToDatabaseColumn(TransactionStatus attribute) {
		return attribute == null ? null : attribute.getValue();
	}

	@Override
	public TransactionStatus convertToEntityAttribute(Character dbData) {
		return TransactionStatus.fromChar(dbData);
	}
}
