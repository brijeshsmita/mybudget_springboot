package br.com.gestao.financeira.pessoal.lancamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.conta.ContaCorrente;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;

public class LancamentoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	protected String comentario;

	@NotNull(message = "Qual é a data deste lançamento?")
	private Date data;

	private Date dataAnterior;

	@NotNull(message = "Este é um lançamento agendado ou confirmado?")
	private LancamentoStatus status;

	@NotNull(message = "Este é um lançamento de Receita ou de Despesa?")
	private InOut inOut;

	@NotNull(message = "Qual é o valor deste lançamento?")
	private BigDecimal valor;

	private Conta conta;

	private Conta contaAnterior;
	private boolean ajuste;

	private boolean transferencia;
	private Conta contaOrigem;
	private Conta contaDestino;

	private Categoria categoria;

	private BigDecimal valorAnterior;

	private boolean saldoInicial;

	private boolean faturaCartao;

	private ContaCartao cartaoCreditoFatura;

	private Integer qtdParcelas;

	private boolean repeteLancamento;
	private LancamentoFrequencia frequencia;

	private Date dataInicio;
	private Date dataLimite;

	private SerieLancamento serie;

	private Usuario usuario;

	public LancamentoVO(InOut inOut, LancamentoStatus status) {
		this.inOut = inOut;
		this.status = status;
	}

	public LancamentoVO() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataAnterior() {
		return dataAnterior;
	}

	public void setDataAnterior(Date dataAnterior) {
		this.dataAnterior = dataAnterior;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public Conta getContaAnterior() {
		return contaAnterior;
	}

	public void setContaAnterior(Conta contaAnterior) {
		this.contaAnterior = contaAnterior;
	}

	public Conta getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(Conta contaDestino) {
		this.contaDestino = contaDestino;
	}

	public Conta getContaOrigem() {
		return contaOrigem;
	}

	public void setContaOrigem(Conta contaOrigem) {
		this.contaOrigem = contaOrigem;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public LancamentoStatus getStatus() {
		return status;
	}

	public void setStatus(LancamentoStatus status) {
		this.status = status;
	}

	public boolean isConfirmado() {
		return getStatus().equals(LancamentoStatus.CONFIRMADO);
	}

	public void setConfirmado(boolean confirmado) {
		setStatus(confirmado ? LancamentoStatus.CONFIRMADO : LancamentoStatus.NAO_CONFIRMADO);
	}

	public InOut getInOut() {
		return inOut;
	}

	public void setInOut(InOut inOut) {
		this.inOut = inOut;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public BigDecimal getValorAnterior() {
		return valorAnterior;
	}

	public void setValorAnterior(BigDecimal valorAnterior) {
		this.valorAnterior = valorAnterior;
	}

	public ContaCartao getCartaoCreditoFatura() {
		return cartaoCreditoFatura;
	}

	public void setCartaoCreditoFatura(ContaCartao cartaoCreditoFatura) {
		this.cartaoCreditoFatura = cartaoCreditoFatura;
	}

	public boolean isSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(boolean saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public boolean isFaturaCartao() {
		return faturaCartao;
	}

	public void setFaturaCartao(boolean faturaCartao) {
		this.faturaCartao = faturaCartao;
	}

	public Integer getQtdParcelas() {
		return qtdParcelas;
	}

	public void setQtdParcelas(Integer qtdParcelas) {
		this.qtdParcelas = qtdParcelas;
	}

	public boolean isTransferencia() {
		return transferencia;
	}

	public void setTransferencia(boolean transferencia) {
		this.transferencia = transferencia;
	}

	public boolean isAjuste() {
		return ajuste;
	}

	public void setAjuste(boolean ajuste) {
		this.ajuste = ajuste;
	}

	public SerieLancamento getSerie() {
		return serie;
	}

	public void setSerie(SerieLancamento serie) {
		this.serie = serie;
	}

	public boolean getRepeteLancamento() {
		return repeteLancamento;
	}

	public void setRepeteLancamento(boolean repeteLancamento) {
		this.repeteLancamento = repeteLancamento;
	}

	public LancamentoFrequencia getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(LancamentoFrequencia frequencia) {
		this.frequencia = frequencia;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(Date dataLimite) {
		this.dataLimite = dataLimite;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Lancamento getLancamento() {
		Lancamento lancamento = null;

		if (conta instanceof ContaCorrente) {
			lancamento = new LancamentoContaCorrente();
			((LancamentoContaCorrente) lancamento).setSaldoInicial(isSaldoInicial());
			((LancamentoContaCorrente) lancamento).setFaturaCartao(isFaturaCartao());
			((LancamentoContaCorrente) lancamento).setCartaoCreditoFatura(getCartaoCreditoFatura());
			((LancamentoContaCorrente) lancamento).setContaOrigem(getContaOrigem());
			((LancamentoContaCorrente) lancamento).setContaDestino(getContaDestino());

		} else if (conta instanceof ContaCartao) {
			lancamento = new LancamentoCartaoCredito();
			((LancamentoCartaoCredito) lancamento).setQtdParcelas(getQtdParcelas());
		}

		lancamento.setCategoria(getCategoria());
		lancamento.setComentario(getComentario());
		lancamento.setConta(getConta());
		lancamento.setContaAnterior(getContaAnterior());
		lancamento.setData(getData());
		lancamento.setDataAnterior(getDataAnterior());
		lancamento.setId(getId());
		lancamento.setInOut(getInOut());
		lancamento.setStatus(getStatus());
		lancamento.setValor(getValor());
		lancamento.setValorAnterior(getValorAnterior());
		lancamento.setAjuste(isAjuste());

		if (repeteLancamento) {
			if (serie == null) {
				serie = new SerieLancamento();
			}
			serie.setDataInicio(getDataInicio());
			serie.setFrequencia(frequencia);
			serie.setDataLimite(dataLimite);
		}

		lancamento.setSerie(getSerie());
		lancamento.setUsuario(getUsuario());

		return lancamento;
	}

}