package br.com.gestao.financeira.pessoal.lancamento;

import java.math.BigDecimal;
import java.util.Date;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;

public class LancamentoContaCartaoBuilder {

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

	public LancamentoContaCorrente build() {
		LancamentoContaCorrente lancamento = new LancamentoContaCorrente();

		lancamento.setCartaoCreditoFatura(cartaoCreditoFatura);
		lancamento.setCategoria(categoria);
		lancamento.setComentario(comentario);
		lancamento.setConfirmado(confirmado);
		lancamento.setConta(conta);
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

	public LancamentoContaCartaoBuilder setCartaoCreditoFatura(ContaCartao cartaoCreditoFatura) {
		this.cartaoCreditoFatura = cartaoCreditoFatura;
		return this;
	}

	public LancamentoContaCartaoBuilder setContaOrigem(Conta contaOrigem) {
		this.contaOrigem = contaOrigem;
		return this;
	}

	public LancamentoContaCartaoBuilder setContaDestino(Conta contaDestino) {
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

	public LancamentoContaCartaoBuilder conta(Conta conta) {
		this.conta = conta;
		return this;
	}

	public LancamentoContaCartaoBuilder setContaAnterior(Conta contaAnterior) {
		this.contaAnterior = contaAnterior;
		return this;
	}

	public LancamentoContaCartaoBuilder categoria(Categoria categoria) {
		this.categoria = categoria;
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

	public LancamentoContaCartaoBuilder setUsuario(Usuario usuario) {
		this.usuario = usuario;
		return this;
	}

}