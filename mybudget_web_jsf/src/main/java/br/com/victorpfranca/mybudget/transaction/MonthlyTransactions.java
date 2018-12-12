package br.com.victorpfranca.mybudget.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.view.MonthYear;

@RequestScoped
public class MonthlyTransactions implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<AccountBalance> saldos;

	private BigDecimal minValue;
	private BigDecimal maxValue;

	private AccountBalance saldoAtual;
	private AccountBalance saldoProximo;
	private AccountBalance menorSaldo;
	private AccountBalance maiorSaldo;
	private AccountBalance ultimoSaldo;

	private MonthYear inicio;
	private MonthYear fim;
	
	public MonthYear getInicio() {
		return inicio;
	}

	public MonthYear getFim() {
		return fim;
	}

	public List<AccountBalance> getSaldos() {
		return saldos;
	}

	public BigDecimal getMinValue() {
		return minValue;
	}

	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public AccountBalance getSaldoAtual() {
		return saldoAtual;
	}

	public AccountBalance getSaldoProximo() {
		return saldoProximo;
	}

	public AccountBalance getMenorSaldo() {
		return menorSaldo;
	}

	public AccountBalance getMaiorSaldo() {
		return maiorSaldo;
	}

	public AccountBalance getUltimoSaldo() {
		return ultimoSaldo;
	}

}
