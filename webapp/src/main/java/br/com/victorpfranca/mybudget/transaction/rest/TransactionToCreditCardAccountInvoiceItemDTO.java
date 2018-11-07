package br.com.victorpfranca.mybudget.transaction.rest;

import java.util.Optional;

import br.com.victorpfranca.mybudget.infra.date.DateUtils;
import br.com.victorpfranca.mybudget.transaction.CreditCardAccountInvoiceItemDTO;
import br.com.victorpfranca.mybudget.transaction.CreditCardInvoiceTransactionItem;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;

public class TransactionToCreditCardAccountInvoiceItemDTO {

	public CreditCardAccountInvoiceItemDTO converter(Transaction transaction) {
		CreditCardAccountInvoiceItemDTO dto = new CreditCardAccountInvoiceItemDTO();
		dto.setId(transaction.getId());
		dto.setStatus(status(transaction));
		dto.setDataCompra(dataIso8601(transaction));
		dto.setConta(nomeConta(transaction));
		dto.setCategoria(nomeCategoria(transaction));
		dto.setComentario(transaction.getComentario());
		dto.setValorCompra(((CreditCardInvoiceTransactionItem)transaction).getLancamentoCartao().getValor());
		dto.setValorParcela(transaction.getValor());
		dto.setQtdParcelas(((CreditCardInvoiceTransactionItem)transaction).getQtdParcelas());
		dto.setIndiceParcelas(((CreditCardInvoiceTransactionItem)transaction).getIndiceParcela());
		dto.setSaldo(transaction.getSaldo());
		return dto;
	}

	private String dataIso8601(Transaction transaction) {
		return Optional.ofNullable(transaction).map(Transaction::getData).map(DateUtils::iso8601).orElse(null);
	}

	private Character status(Transaction transaction) {
		return Optional.ofNullable(transaction).map(Transaction::getStatus).map(TransactionStatus::getValue).orElse(null);
	}

	private String nomeConta(Transaction transaction) {
		return Optional.ofNullable(transaction).map(Transaction::getAccount).map(c -> c.getNome()).orElse(null);
	}

	private String nomeCategoria(Transaction transaction) {
		return Optional.ofNullable(transaction).map(Transaction::getCategory).map(c -> c.getNome()).orElse(null);
	}

}