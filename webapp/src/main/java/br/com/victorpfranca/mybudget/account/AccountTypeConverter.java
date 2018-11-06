package br.com.victorpfranca.mybudget.account;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AccountTypeConverter implements AttributeConverter<AccountType, Character> {

	@Override
	public Character convertToDatabaseColumn(AccountType attribute) {
		return attribute == null ? null : attribute.getValue();
	}

	@Override
	public AccountType convertToEntityAttribute(Character dbData) {
		return AccountType.fromChar(dbData);
	}
}
