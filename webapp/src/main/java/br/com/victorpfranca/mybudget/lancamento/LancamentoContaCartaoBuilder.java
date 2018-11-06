package br.com.victorpfranca.mybudget.lancamento;

import java.math.BigDecimal;
import java.util.Date;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.category.Category;

public class LancamentoContaCartaoBuilder {

	private Integer id;

	private String comentario;

	private Date data;

	private Date dataAnterior;

	private LancamentoStatus status;

	private InOut inOut;

	private BigDecimal valor;

	private Account account;

	private Account contaAnterior;

	private SerieLancamento serie;

	private Category category;

	private User user;

	private BigDecimal valorAnterior;

	private BigDecimal saldo;

	private boolean confirmado;

	private CreditCardAccount cartaoCreditoFatura;

	private Account contaDestino;

	private Account contaOrigem;

	private boolean faturaCartao;

	public LancamentoContaCorrente build() {
		LancamentoContaCorrente lancamento = new LancamentoContaCorrente();

		lancamento.setCartaoCreditoFatura(cartaoCreditoFatura);
		lancamento.setCategory(category);
		lancamento.setComentario(comentario);
		lancamento.setConfirmado(confirmado);
		lancamento.setAccount(account);
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

	public LancamentoContaCartaoBuilder setCartaoCreditoFatura(CreditCardAccount cartaoCreditoFatura) {
		this.cartaoCreditoFatura = cartaoCreditoFatura;
		return this;
	}

	public LancamentoContaCartaoBuilder setContaOrigem(Account contaOrigem) {
		this.contaOrigem = contaOrigem;
		return this;
	}

	public LancamentoContaCartaoBuilder setContaDestino(Account contaDestino) {
		this.contaDestino = contaDestino;
		return this;
	}

	public LancamentoContaCartaoBuilder setFaturaCartao(boolean faturaCartao) {
		this.faturaCartao = faturaCartao;
		return this;
	}

	public LancamentoContaCartaoBuilder setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
		return this;
	}

	public LancamentoContaCartaoBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public LancamentoContaCartaoBuilder valor(BigDecimal valor) {
		this.valor = valor;
		return this;
	}

	public LancamentoContaCartaoBuilder data(Date data) {
		this.data = data;
		return this;
	}

	public LancamentoContaCartaoBuilder setDataAnterior(Date dataAnterior) {
		this.dataAnterior = dataAnterior;
		return this;
	}

	public LancamentoContaCartaoBuilder account(Account account) {
		this.account = account;
		return this;
	}

	public LancamentoContaCartaoBuilder setContaAnterior(Account contaAnterior) {
		this.contaAnterior = contaAnterior;
		return this;
	}

	public LancamentoContaCartaoBuilder category(Category category) {
		this.category = category;
		return this;
	}

	public LancamentoContaCartaoBuilder status(LancamentoStatus status) {
		this.status = status;
		return this;
	}

	public LancamentoContaCartaoBuilder setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
		return this;
	}

	public LancamentoContaCartaoBuilder inOut(InOut inOut) {
		this.inOut = inOut;
		return this;
	}

	public LancamentoContaCartaoBuilder setComentario(String comentario) {
		this.comentario = comentario;
		return this;
	}

	public LancamentoContaCartaoBuilder setValorAnterior(BigDecimal valorAnterior) {
		this.valorAnterior = valorAnterior;
		return this;
	}

	public LancamentoContaCartaoBuilder setSerie(SerieLancamento serie) {
		this.serie = serie;
		return this;
	}

	public LancamentoContaCartaoBuilder setUsuario(User user) {
		this.user = user;
		return this;
	}

}