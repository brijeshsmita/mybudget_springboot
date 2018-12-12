package br.com.victorpfranca.mybudget.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreditCardInvoiceTransactionItem extends Transaction {

	private static final long serialVersionUID = 1L;

	private Integer qtdParcelas;

	private Integer indiceParcela;

	private Transaction lancamentoCartao;

}
