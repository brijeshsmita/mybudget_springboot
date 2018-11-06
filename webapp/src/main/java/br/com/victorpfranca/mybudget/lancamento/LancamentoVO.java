package br.com.victorpfranca.mybudget.lancamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.category.Category;

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

	private Account account;

	private Account contaAnterior;
	private boolean ajuste;

	private boolean transferencia;
	private Account contaOrigem;
	private Account contaDestino;

	private Category category;

	private BigDecimal valorAnterior;

	private boolean saldoInicial;

	private boolean faturaCartao;

	private CreditCardAccount cartaoCreditoFatura;

	private Integer qtdParcelas;

	private boolean repeteLancamento;
	private LancamentoFrequencia frequencia;

	private Date dataInicio;
	private Date dataLimite;

	private SerieLancamento serie;

	private User user;

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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getContaAnterior() {
		return contaAnterior;
	}

	public void setContaAnterior(Account contaAnterior) {
		this.contaAnterior = contaAnterior;
	}

	public Account getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(Account contaDestino) {
		this.contaDestino = contaDestino;
	}

	public Account getContaOrigem() {
		return contaOrigem;
	}

	public void setContaOrigem(Account contaOrigem) {
		this.contaOrigem = contaOrigem;
	}

	public Category getCategoria() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	public CreditCardAccount getCartaoCreditoFatura() {
		return cartaoCreditoFatura;
	}

	public void setCartaoCreditoFatura(CreditCardAccount cartaoCreditoFatura) {
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

	public User getUsuario() {
		return user;
	}

	public void setUsuario(User user) {
		this.user = user;
	}

	public Lancamento getLancamento() {
		Lancamento lancamento = null;

		if (account instanceof CheckingAccount) {
			lancamento = new LancamentoContaCorrente();
			((LancamentoContaCorrente) lancamento).setSaldoInicial(isSaldoInicial());
			((LancamentoContaCorrente) lancamento).setFaturaCartao(isFaturaCartao());
			((LancamentoContaCorrente) lancamento).setCartaoCreditoFatura(getCartaoCreditoFatura());
			((LancamentoContaCorrente) lancamento).setContaOrigem(getContaOrigem());
			((LancamentoContaCorrente) lancamento).setContaDestino(getContaDestino());

		} else if (account instanceof CreditCardAccount) {
			lancamento = new LancamentoCartaoCredito();
			((LancamentoCartaoCredito) lancamento).setQtdParcelas(getQtdParcelas());
		}

		lancamento.setCategory(getCategoria());
		lancamento.setComentario(getComentario());
		lancamento.setAccount(getAccount());
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
		lancamento.setUser(getUsuario());

		return lancamento;
	}

}