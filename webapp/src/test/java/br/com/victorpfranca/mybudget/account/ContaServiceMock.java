package br.com.victorpfranca.mybudget.account;

import java.math.BigDecimal;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.rules.BankAccountService;

public class ContaServiceMock extends BankAccountService {

	@Override
	public BigDecimal getSaldoAte(Account account, Integer ano, Integer mes) {
		return BigDecimal.ZERO;
	}

}
