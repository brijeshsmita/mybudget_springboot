package br.com.victorpfranca.mybudget.lancamento;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.lancamento.rules.ContaNotNullException;
import br.com.victorpfranca.mybudget.lancamento.rules.TipoContaException;
import br.com.victorpfranca.mybudget.lancamento.rules.ValorLancamentoInvalidoException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("0")
@Getter
@Setter
@NoArgsConstructor
public class LancamentoContaCorrente extends Lancamento {

	private static final long serialVersionUID = 1L;

	@Column(name = "fatura_cartao", nullable = false, unique = false)
	private boolean faturaCartao;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = true, name = "cartao_credito_fatura_id")
	private CreditCardAccount cartaoCreditoFatura;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = true, name = "conta_origem_id")
	private Account contaOrigem;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = true, name = "conta_destino_id")
	private Account contaDestino;

	public LancamentoContaCorrente(InOut inOut, LancamentoStatus status) {
		setStatus(status);
		setInOut(inOut);
	}

	public static Lancamento buildFaturaCartao(CreditCardAccount creditCardAccount, Date data, BigDecimal valorParcela)
			throws ContaNotNullException {
		if (creditCardAccount.getAccountPagamentoFatura() == null) {
			throw new ContaNotNullException("crud_lancamento_validator_conta");
		}

		LancamentoContaCorrente lancamentoFatura = new LancamentoContaCorrente();
		lancamentoFatura.setAccount(creditCardAccount.getAccountPagamentoFatura());
		lancamentoFatura.setValor(valorParcela);
		lancamentoFatura.setSaldo(BigDecimal.ZERO);
		lancamentoFatura.setInOut(InOut.S);
		lancamentoFatura.setFaturaCartao(true);
		lancamentoFatura.setStatus(LancamentoStatus.NAO_CONFIRMADO);
		lancamentoFatura.setData(data);
		lancamentoFatura.setUser(creditCardAccount.getUsuario());

		lancamentoFatura.setCartaoCreditoFatura(creditCardAccount);

		return lancamentoFatura;
	}

	@PostLoad
	public void carregarValoresAnteriores() {
		setValorAnterior(getValor());
		setDataAnterior(getData());
		setContaAnterior(getAccount());
	}

	public boolean isTransferencia() {
		return getContaDestino() != null || getContaOrigem() != null;
	}

	@Override
	public LancamentoVO getVO() {
		LancamentoVO lancamentoVO = super.getVO();
		lancamentoVO.setSaldoInicial(isSaldoInicial());
		lancamentoVO.setFaturaCartao(isFaturaCartao());
		lancamentoVO.setCartaoCreditoFatura(getCartaoCreditoFatura());
		lancamentoVO.setContaOrigem(contaOrigem);
		lancamentoVO.setContaDestino(contaDestino);
		if (contaOrigem != null || contaDestino != null)
			lancamentoVO.setTransferencia(true);

		return lancamentoVO;
	}

	@Override
	public Object clone() {
		LancamentoContaCorrente lancamento = new LancamentoContaCorrente();

		lancamento.setData(data);
		lancamento.setDataAnterior(dataAnterior);
		lancamento.setAno(ano);
		lancamento.setMes(mes);
		lancamento.setCategory(category);
		lancamento.setComentario(comentario);
		lancamento.setAccount(account);
		lancamento.setContaAnterior(contaAnterior);
		lancamento.setInOut(inOut);
		lancamento.setSerie(serie);
		lancamento.setStatus(status);
		lancamento.setValor(valor);
		lancamento.setValorAnterior(valorAnterior);

		lancamento.setSaldoInicial(saldoInicial);
		lancamento.setFaturaCartao(faturaCartao);
		lancamento.setCartaoCreditoFatura(cartaoCreditoFatura);

		lancamento.setContaOrigem(contaOrigem);
		lancamento.setContaDestino(contaDestino);

		return lancamento;
	}

	@Override
	protected void validarConta() throws ContaNotNullException, TipoContaException {
		super.validarConta();
		if (isTransferencia()) {
			if (isAjuste()) {
				throw new TipoLancamentoInvalidoException("crud.lancamento.transferencia.error.ajuste");
			}

			if (getContaDestino() != null && InOut.E.equals(getInOut())) {
				throw new TipoLancamentoInvalidoException("crud.lancamento.transferencia.error.tipo");
			}
		}
	}

	@Override
	protected boolean isPermiteCategoriaNula() {
		return super.isPermiteCategoriaNula() || isTransferencia() || isFaturaCartao();
	}

	@Override
	protected void validarValor() throws ValorLancamentoInvalidoException {
		if (isSaldoInicial())
			return;
		if (isFaturaCartao()) {
			if (getValor().compareTo(BigDecimal.ZERO) < 0)
				throw new ValorLancamentoInvalidoException("crud_lancamento_validator_valor_lancamento");
		} else {
			super.validarValor();
		}
	}

}
