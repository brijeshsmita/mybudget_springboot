package br.com.victorpfranca.mybudget.transaction;

import java.math.BigDecimal;

import br.com.victorpfranca.mybudget.InOut;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreditCardTransaction extends Transaction {

	private static final long serialVersionUID = 1L;

	private Integer qtdParcelas;

	public CreditCardTransaction(InOut inOut, TransactionStatus status) {
		setStatus(status);
		setInOut(inOut);
		setSaldo(BigDecimal.ZERO);
	}

	@Override
	public TransactionVO getVO() {
		TransactionVO transactionVO = super.getVO();
		transactionVO.setQtdParcelas(getQtdParcelas());
		return transactionVO;
	}

	@Override
	public Object clone() {
		CreditCardTransaction lancamento = new CreditCardTransaction();

		lancamento.setSaldoInicial(saldoInicial);
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

		lancamento.setQtdParcelas(qtdParcelas);

		return lancamento;
	}

}
