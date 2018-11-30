package br.com.victorpfranca.mybudget.transaction;

import java.math.BigDecimal;
import java.util.Date;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.category.Category;

public class CreditCardAccountTransactionBuilder {

	private Integer id;

	private String comentario;

	private Date data;

	private Date dataAnterior;

	private TransactionStatus status;

	private InOut inOut;

	private BigDecimal valor;

	private Account account;

	private Account contaAnterior;

	private TransactionSerie serie;

	private Category category;

	private User user;

	private BigDecimal valorAnterior;

	private BigDecimal saldo;

	private boolean confirmado;

	private CreditCardAccount cartaoCreditoFatura;

	private Account contaDestino;

	private Account contaOrigem;

	private boolean faturaCartao;

	public CheckingAccountTransaction build() {
		CheckingAccountTransaction lancamento = new CheckingAccountTransaction();

		lancamento.setCartaoCreditoFatura(cartaoCreditoFatura);
		lancamento.setCategoria(category);
		lancamento.setComentario(comentario);
		lancamento.setConfirmado(confirmado);
		lancamento.setConta(account);
		lancamento.setContaAnterior(contaAnterior);
		lancamento.setContaDestino(contaDestino);
		lancamento.setContaOrigem(contaOrigem);
		lancamento.setData(data);
		lancamento.setDataAnterior(dataAnterior);
		lancamento.setFaturaCartao(faturaCartao);
		lancamento.setId(id);
		lancamento.setInOut(inOut);
		lancamento.setSaldo(saldo);
		lancamento.setSerie(serie);
		lancamento.setStatus(status);
		lancamento.setValor(valor);
		lancamento.setValorAnterior(valorAnterior);

		return lancamento;

	}

	public CreditCardAccountTransactionBuilder setCartaoCreditoFatura(CreditCardAccount cartaoCreditoFatura) {
		this.cartaoCreditoFatura = cartaoCreditoFatura;
		return this;
	}

	public CreditCardAccountTransactionBuilder setContaOrigem(Account contaOrigem) {
		this.contaOrigem = contaOrigem;
		return this;
	}

	public CreditCardAccountTransactionBuilder setContaDestino(Account contaDestino) {
		this.contaDestino = contaDestino;
		return this;
	}

	public CreditCardAccountTransactionBuilder setFaturaCartao(boolean faturaCartao) {
		this.faturaCartao = faturaCartao;
		return this;
	}

	public CreditCardAccountTransactionBuilder setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
		return this;
	}

	public CreditCardAccountTransactionBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public CreditCardAccountTransactionBuilder valor(BigDecimal valor) {
		this.valor = valor;
		return this;
	}

	public CreditCardAccountTransactionBuilder data(Date data) {
		this.data = data;
		return this;
	}

	public CreditCardAccountTransactionBuilder setDataAnterior(Date dataAnterior) {
		this.dataAnterior = dataAnterior;
		return this;
	}

	public CreditCardAccountTransactionBuilder account(Account account) {
		this.account = account;
		return this;
	}

	public CreditCardAccountTransactionBuilder setContaAnterior(Account contaAnterior) {
		this.contaAnterior = contaAnterior;
		return this;
	}

	public CreditCardAccountTransactionBuilder category(Category category) {
		this.category = category;
		return this;
	}

	public CreditCardAccountTransactionBuilder status(TransactionStatus status) {
		this.status = status;
		return this;
	}

	public CreditCardAccountTransactionBuilder setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
		return this;
	}

	public CreditCardAccountTransactionBuilder inOut(InOut inOut) {
		this.inOut = inOut;
		return this;
	}

	public CreditCardAccountTransactionBuilder setComentario(String comentario) {
		this.comentario = comentario;
		return this;
	}

	public CreditCardAccountTransactionBuilder setValorAnterior(BigDecimal valorAnterior) {
		this.valorAnterior = valorAnterior;
		return this;
	}

	public CreditCardAccountTransactionBuilder setSerie(TransactionSerie serie) {
		this.serie = serie;
		return this;
	}

	public CreditCardAccountTransactionBuilder setUsuario(User user) {
		this.user = user;
		return this;
	}

}