package br.com.victorpfranca.mybudget.account.rest;

import javax.inject.Inject;
import javax.ws.rs.Path;

import br.com.victorpfranca.mybudget.account.BalanceResource;
import br.com.victorpfranca.mybudget.account.BalancesResource;
import br.com.victorpfranca.mybudget.transaction.MyFutureResource;
import br.com.victorpfranca.mybudget.transaction.rest.MonthlyTransactionsResourceImpl;

@Path("saldos")
public class AccountBalancesResourceImpl implements BalancesResource {
	@Inject
	private AccountBalanceResourceImpl accountBalanceResourceImpl;
	@Inject
	private MonthlyTransactionsResourceImpl lancamentosMensaisResource;

	@Override
	public MyFutureResource meuFuturo() {
		return lancamentosMensaisResource;
	}

	@Override
	public BalanceResource balanceResource(Integer ano, Integer mes) {
		return accountBalanceResourceImpl.ano(ano).mes(mes);
	}

}
