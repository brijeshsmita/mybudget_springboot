package br.com.victorpfranca.mybudget.transaction;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckingAccountTransaction extends Transaction {

	private static final long serialVersionUID = 1L;

	private boolean faturaCartao;

	private CreditCardAccount cartaoCreditoFatura;

	private Account contaOrigem;

	private Account contaDestino;

	public CheckingAccountTransaction(InOut inOut, TransactionStatus status) {
		setStatus(status);
		setInOut(inOut);
	}

	public boolean isTransferencia() {
		return getContaDestino() != null || getContaOrigem() != null;
	}

	@Override
	public TransactionVO getVO() {
		TransactionVO transactionVO = super.getVO();
		transactionVO.setSaldoInicial(isSaldoInicial());
		transactionVO.setFaturaCartao(isFaturaCartao());
		transactionVO.setCartaoCreditoFatura(getCartaoCreditoFatura());
		transactionVO.setContaOrigem(contaOrigem);
		transactionVO.setContaDestino(contaDestino);
		if (contaOrigem != null || contaDestino != null)
			transactionVO.setTransferencia(true);

		return transactionVO;
	}

	@Override
	public Object clone() {
		CheckingAccountTransaction lancamento = new CheckingAccountTransaction();

		lancamento.setData(data);
		lancamento.setDataAnterior(dataAnterior);
		lancamento.setAno(ano);
		lancamento.setMes(mes);
		lancamento.setCategoria(categoria);
		lancamento.setComentario(comentario);
		lancamento.setConta(conta);
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

}
