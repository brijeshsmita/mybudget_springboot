package br.com.victorpfranca.mybudget.account;

import java.math.BigDecimal;

import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;

public class AtualizadorSaldoContaMock extends AccountBalanceUpdater{
	
	@Override
	public BigDecimal getSaldoAte(Account account, Integer ano, Integer mes) {
		return BigDecimal.ZERO;
	}
	


}
