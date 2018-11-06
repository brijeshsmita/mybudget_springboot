package br.com.gestao.financeira.pessoal.lancamento;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LancamentoStatusConverter implements AttributeConverter<LancamentoStatus, Character> {

	@Override
	public Character convertToDatabaseColumn(LancamentoStatus attribute) {
		return attribute == null ? null : attribute.getValue();
	}

	@Override
	public LancamentoStatus convertToEntityAttribute(Character dbData) {
		return LancamentoStatus.fromChar(dbData);
	}
}
