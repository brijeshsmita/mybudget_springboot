package br.com.victorpfranca.mybudget.transaction;

import java.math.BigDecimal;
import java.util.Date;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.category.Category;

public class TransactionBuilder {

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

	private Integer qtdParcelas;

	public CheckingAccountTransaction buildLancamentoContaCorrente() {
		CheckingAccountTransaction lancamento = new CheckingAccountTransaction();

		buildLancamento(lancamento);

		lancamento.setCartaoCreditoFatura(cartaoCreditoFatura);
		lancamento.setContaDestino(contaDestino);
		lancamento.setContaOrigem(contaOrigem);
		lancamento.setFaturaCartao(faturaCartao);

		return lancamento;

	}

	public CreditCardTransaction buildLancamentoCartaoCredito() {
		CreditCardTransaction lancamento = new CreditCardTransaction();

		buildLancamento(lancamento);

		lancamento.setQtdParcelas(qtdParcelas);

		return lancamento;

	}

	private void buildLancamento(Transaction transaction) {
		transaction.setCategoria(category);
		transaction.setComentario(comentario);
		transaction.setConfirmado(confirmado);
		transaction.setConta(account);
		transaction.setContaAnterior(contaAnterior);
		transaction.setData(data);
		transaction.setDataAnterior(dataAnterior);
		transaction.setId(id);
		transaction.setInOut(inOut);
		transaction.setSaldo(saldo);
		transaction.setSerie(serie);
		transaction.setStatus(status);
		transaction.setValor(valor);
		transaction.setValorAnterior(valorAnterior);
	}

	public TransactionBuilder setQtdParcelas(Integer qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
		return this;
	}

	public TransactionBuilder setCartaoCreditoFatura(CreditCardAccount cartaoCreditoFatura) {
		this.cartaoCreditoFatura = cartaoCreditoFatura;
		return this;
	}

	public TransactionBuilder setContaOrigem(Account contaOrigem) {
		this.contaOrigem = contaOrigem;
		return this;
	}

	public TransactionBuilder setContaDestino(Account contaDestino) {
		this.contaDestino = contaDestino;
		return this;
	}

	public TransactionBuilder setFaturaCartao(boolean faturaCartao) {
		this.faturaCartao = faturaCartao;
		return this;
	}

	public TransactionBuilder setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
		return this;
	}

	public TransactionBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public TransactionBuilder valor(BigDecimal valor) {
		this.valor = valor;
		return this;
	}

	public TransactionBuilder data(Date data) {
		this.data = data;
		return this;
	}

	public TransactionBuilder setDataAnterior(Date dataAnterior) {
		this.dataAnterior = dataAnterior;
		return this;
	}

	public TransactionBuilder account(Account account) {
		this.account = account;
		return this;
	}

	public TransactionBuilder setContaAnterior(Account contaAnterior) {
		this.contaAnterior = contaAnterior;
		return this;
	}

	public TransactionBuilder category(Category category) {
		this.category = category;
		return this;
	}

	public TransactionBuilder status(TransactionStatus status) {
		this.status = status;
		return this;
	}

	public TransactionBuilder setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
		return this;
	}

	public TransactionBuilder inOut(InOut inOut) {
		this.inOut = inOut;
		return this;
	}

	public TransactionBuilder setComentario(String comentario) {
		this.comentario = comentario;
		return this;
	}

	public TransactionBuilder setValorAnterior(BigDecimal valorAnterior) {
		this.valorAnterior = valorAnterior;
		return this;
	}

	public TransactionBuilder setSerie(TransactionSerie serie) {
		this.serie = serie;
		return this;
	}

	public TransactionBuilder setUsuario(User user) {
		this.user = user;
		return this;
	}

}