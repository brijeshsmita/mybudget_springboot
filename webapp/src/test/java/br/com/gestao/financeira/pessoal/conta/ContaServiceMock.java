package br.com.gestao.financeira.pessoal.conta;

import java.math.BigDecimal;

import br.com.gestao.financeira.pessoal.conta.rules.ContaService;

public class ContaServiceMock extends ContaService {

	@Override
	public BigDecimal getSaldoAte(Conta conta, Integer ano, Integer mes) {
		return BigDecimal.ZERO;
	}

}
