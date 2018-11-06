package br.com.victorpfranca.mybudget.account.rest;

import javax.inject.Inject;
import javax.ws.rs.Path;

import br.com.victorpfranca.mybudget.conta.SaldoResource;
import br.com.victorpfranca.mybudget.conta.SaldosResource;
import br.com.victorpfranca.mybudget.lancamento.MeuFuturoResource;
import br.com.victorpfranca.mybudget.transaction.rest.LancamentosMensaisResourceImpl;

@Path("saldos")
public class AccountBalancesResourceImpl implements SaldosResource {
	@Inject
	private AccountBalanceResourceImpl accountBalanceResourceImpl;
	@Inject
	private LancamentosMensaisResourceImpl lancamentosMensaisResource;

	@Override
	public MeuFuturoResource meuFuturo() {
		return lancamentosMensaisResource;
	}

	@Override
	public SaldoResource saldoResource(Integer ano, Integer mes) {
		return accountBalanceResourceImpl.ano(ano).mes(mes);
	}

}
