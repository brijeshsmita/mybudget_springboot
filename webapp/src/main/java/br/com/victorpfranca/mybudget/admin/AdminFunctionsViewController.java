package br.com.victorpfranca.mybudget.admin;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.view.AnoMes;

@Named
@ViewScoped
public class AdminFunctionsViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	Map<Account, Map<AnoMes, AccountBalance>> saldosReconstruidos;

	@Inject
	private BankAccountService bankAccountService;

	@PostConstruct
	public void init() {

	}

	public void reconstruirSaldosContas() {
		saldosReconstruidos = bankAccountService.reconstruirSaldosContas();
	}

	public Map<Account, Map<AnoMes, AccountBalance>> getSaldosReconstruidos() {
		return saldosReconstruidos;
	}

}
