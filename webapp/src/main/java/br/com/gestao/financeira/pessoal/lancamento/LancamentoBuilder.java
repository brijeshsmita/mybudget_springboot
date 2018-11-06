package br.com.gestao.financeira.pessoal.lancamento;

import java.math.BigDecimal;
import java.util.Date;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;

public class LancamentoBuilder {

	private Integer id;

	private String comentario;

	private Date data;

	private Date dataAnterior;

	private LancamentoStatus status;

	private InOut inOut;

	private BigDecimal valor;

	private Conta conta;

	private Conta contaAnterior;

	private SerieLancamento serie;

	private Categoria categoria;

	private Usuario usuario;

	private BigDecimal valorAnterior;

	private BigDecimal saldo;

	private boolean confirmado;

	private ContaCartao cartaoCreditoFatura;

	private Conta contaDestino;

	private Conta contaOrigem;

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
		lancamento.setCategoria(categoria);
		lancamento.setComentario(comentario);
		lancamento.setConfirmado(confirmado);
		lancamento.setConta(conta);
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

	public LancamentoBuilder setCartaoCreditoFatura(ContaCartao cartaoCreditoFatura) {
		this.cartaoCreditoFatura = cartaoCreditoFatura;
		return this;
	}

	public LancamentoBuilder setContaOrigem(Conta contaOrigem) {
		this.contaOrigem = contaOrigem;
		return this;
	}

	public LancamentoBuilder setContaDestino(Conta contaDestino) {
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

	public LancamentoBuilder conta(Conta conta) {
		this.conta = conta;
		return this;
	}

	public LancamentoBuilder setContaAnterior(Conta contaAnterior) {
		this.contaAnterior = contaAnterior;
		return this;
	}

	public LancamentoBuilder categoria(Categoria categoria) {
		this.categoria = categoria;
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

	public LancamentoBuilder setUsuario(Usuario usuario) {
		this.usuario = usuario;
		return this;
	}

}