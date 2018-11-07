package br.com.victorpfranca.mybudget.transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("1")
@Getter
@Setter
@NoArgsConstructor
public class CreditCardTransaction extends Transaction {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Parcelou em quantas vezes?")
	@Column(name = "qtd_parcelas", nullable = true, unique = false)
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

	public CreditCardInvoiceTransactionItem buildFaturaItem(Date data, int ano, int mes, int indiceParcela) {
		CreditCardInvoiceTransactionItem faturaItem = new CreditCardInvoiceTransactionItem();

		faturaItem.setCategory(category);
		faturaItem.setComentario(comentario);
		faturaItem.setAccount(account);
		faturaItem.setAjuste(isAjuste());
		faturaItem.setInOut(inOut.equals(InOut.E) ? InOut.S : InOut.E);
		faturaItem.setLancamentoCartao(this);
		faturaItem.setSerie(serie);
		faturaItem.setStatus(status);

		faturaItem.setData(data);
		faturaItem.setMes(mes);
		faturaItem.setAno(ano);
		faturaItem.setValor(valor.divide(BigDecimal.valueOf(qtdParcelas), 2, RoundingMode.HALF_UP));
		faturaItem.setQtdParcelas(qtdParcelas);
		faturaItem.setIndiceParcela(indiceParcela);

		return faturaItem;
	}

	@Override
	protected void validarConta() throws NullableAccountException, AccountTypeException {
		super.validarConta();
		if (!isAjuste() && InOut.E.equals(getInOut())) {
			throw new InvalidTransactionTypeException("crud.lancamento.cartao.error.tipo");
		}
	}

	@Override
	public Object clone() {
		CreditCardTransaction lancamento = new CreditCardTransaction();

		lancamento.setSaldoInicial(saldoInicial);
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

		lancamento.setQtdParcelas(qtdParcelas);

		return lancamento;
	}

}
