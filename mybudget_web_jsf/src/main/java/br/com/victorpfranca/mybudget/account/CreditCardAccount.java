package br.com.victorpfranca.mybudget.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.victorpfranca.mybudget.transaction.Transaction;

public class CreditCardAccount extends Account implements Serializable {

	private static final long serialVersionUID = 1L;

	private Account contaPagamentoFatura;

	private Integer cartaoDiaFechamento;

	private Integer cartaoDiaPagamento;

	private Integer cartaoDiaFechamentoAnterior;

	private Integer cartaoDiaPagamentoAnterior;

	protected List<Transaction> transactions;

	public CreditCardAccount() {
		this.transactions = new ArrayList<Transaction>();
	}

	public CreditCardAccount(String nome) {
		super(nome);
		this.transactions = new ArrayList<Transaction>();
	}

	public Integer getCartaoDiaFechamento() {
		return cartaoDiaFechamento;
	}

	public void setCartaoDiaFechamento(Integer cartaoDiaFechamento) {
		this.cartaoDiaFechamento = cartaoDiaFechamento;
	}

	public Integer getCartaoDiaPagamento() {
		return cartaoDiaPagamento;
	}

	public void setCartaoDiaPagamento(Integer cartaoDiaPagamento) {
		this.cartaoDiaPagamento = cartaoDiaPagamento;
	}

	public Integer getCartaoDiaFechamentoAnterior() {
		return cartaoDiaFechamentoAnterior;
	}

	public void setCartaoDiaFechamentoAnterior(Integer cartaoDiaFechamentoAnterior) {
		this.cartaoDiaFechamentoAnterior = cartaoDiaFechamentoAnterior;
	}

	public Integer getCartaoDiaPagamentoAnterior() {
		return cartaoDiaPagamentoAnterior;
	}

	public void setCartaoDiaPagamentoAnterior(Integer cartaoDiaPagamentoAnterior) {
		this.cartaoDiaPagamentoAnterior = cartaoDiaPagamentoAnterior;
	}

	public Account getAccountPagamentoFatura() {
		return contaPagamentoFatura;
	}

	public void setContaPagamentoFatura(Account contaPagamentoFatura) {
		this.contaPagamentoFatura = contaPagamentoFatura;
	}

	public List<Transaction> getLancamentos() {
		return transactions;
	}

	public void setLancamentos(List<Transaction> transactions) {
		this.transactions = transactions;
	}

}