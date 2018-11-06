package br.com.victorpfranca.mybudget.lancamento;

import java.math.BigDecimal;
import java.util.Date;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.category.Category;

public class LancamentoBuilder {

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

	private Integer qtdParcelas;

	public LancamentoContaCorrente buildLancamentoContaCorrente() {
		LancamentoContaCorrente lancamento = new LancamentoContaCorrente();

		buildLancamento(lancamento);

		lancamento.setCartaoCreditoFatura(cartaoCreditoFatura);
		lancamento.setContaDestino(contaDestino);
		lancamento.setContaOrigem(contaOrigem);
		lancamento.setFaturaCartao(faturaCartao);

		return lancamento;

	}

	public LancamentoCartaoCredito buildLancamentoCartaoCredito() {
		LancamentoCartaoCredito lancamento = new LancamentoCartaoCredito();

		buildLancamento(lancamento);

		lancamento.setQtdParcelas(qtdParcelas);

		return lancamento;

	}

	private void buildLancamento(Lancamento lancamento) {
		lancamento.setCategory(category);
		lancamento.setComentario(comentario);
		lancamento.setConfirmado(confirmado);
		lancamento.setAccount(account);
		lancamento.setContaAnterior(contaAnterior);
		lancamento.setData(data);
		lancamento.setDataAnterior(dataAnterior);
		lancamento.setId(id);
		lancamento.setInOut(inOut);
		lancamento.setSaldo(saldo);
		lancamento.setSerie(serie);
		lancamento.setStatus(status);
		lancamento.setValor(valor);
		lancamento.setValorAnterior(valorAnterior);
	}

	public LancamentoBuilder setQtdParcelas(Integer qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
		return this;
	}

	public LancamentoBuilder setCartaoCreditoFatura(CreditCardAccount cartaoCreditoFatura) {
		this.cartaoCreditoFatura = cartaoCreditoFatura;
		return this;
	}

	public LancamentoBuilder setContaOrigem(Account contaOrigem) {
		this.contaOrigem = contaOrigem;
		return this;
	}

	public LancamentoBuilder setContaDestino(Account contaDestino) {
		this.contaDestino = contaDestino;
		return this;
	}

	public LancamentoBuilder setFaturaCartao(boolean faturaCartao) {
		this.faturaCartao = faturaCartao;
		return this;
	}

	public LancamentoBuilder setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
		return this;
	}

	public LancamentoBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public LancamentoBuilder valor(BigDecimal valor) {
		this.valor = valor;
		return this;
	}

	public LancamentoBuilder data(Date data) {
		this.data = data;
		return this;
	}

	public LancamentoBuilder setDataAnterior(Date dataAnterior) {
		this.dataAnterior = dataAnterior;
		return this;
	}

	public LancamentoBuilder account(Account account) {
		this.account = account;
		return this;
	}

	public LancamentoBuilder setContaAnterior(Account contaAnterior) {
		this.contaAnterior = contaAnterior;
		return this;
	}

	public LancamentoBuilder category(Category category) {
		this.category = category;
		return this;
	}

	public LancamentoBuilder status(LancamentoStatus status) {
		this.status = status;
		return this;
	}

	public LancamentoBuilder setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
		return this;
	}

	public LancamentoBuilder inOut(InOut inOut) {
		this.inOut = inOut;
		return this;
	}

	public LancamentoBuilder setComentario(String comentario) {
		this.comentario = comentario;
		return this;
	}

	public LancamentoBuilder setValorAnterior(BigDecimal valorAnterior) {
		this.valorAnterior = valorAnterior;
		return this;
	}

	public LancamentoBuilder setSerie(SerieLancamento serie) {
		this.serie = serie;
		return this;
	}

	public LancamentoBuilder setUsuario(User user) {
		this.user = user;
		return this;
	}

}