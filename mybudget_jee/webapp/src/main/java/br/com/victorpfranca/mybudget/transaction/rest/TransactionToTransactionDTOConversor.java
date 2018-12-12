package br.com.victorpfranca.mybudget.transaction.rest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.infra.date.DateUtils;
import br.com.victorpfranca.mybudget.transaction.CategoryDTO;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionDTO;
import br.com.victorpfranca.mybudget.transaction.TransactionFrequence;
import br.com.victorpfranca.mybudget.transaction.TransactionRegistryDTO;
import br.com.victorpfranca.mybudget.transaction.TransactionSerie;
import br.com.victorpfranca.mybudget.transaction.TransactionSerieDTO;
import br.com.victorpfranca.mybudget.transaction.TransactionSerieUpdaterDTO;
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;
import br.com.victorpfranca.mybudget.transaction.TransactionUpdaterDTO;

public class TransactionToTransactionDTOConversor {

	public void aplicarValores(Transaction transaction, TransactionSerieUpdaterDTO dto,
			Function<Integer, Category> categoriaFinder, Function<Integer, Account> contaFinder) {
		transaction.setCategoria(category(dto, categoriaFinder.compose(TransactionSerieUpdaterDTO::getCategoria)));
		transaction.setConta(account(dto, contaFinder.compose(TransactionSerieUpdaterDTO::getConta)));
		transaction.setValor(dto.getValor());
		transaction.setStatus(TransactionStatus.fromChar(dto.getStatus()));
		transaction.setComentario(dto.getComentario());
	}

	public void aplicarValores(Transaction transaction, TransactionUpdaterDTO dto,
			Function<Integer, Category> categoriaFinder, Function<Integer, Account> contaFinder) {
		transaction.setCategoria(category(dto, categoriaFinder.compose(TransactionUpdaterDTO::getCategoria)));
		transaction.setConta(account(dto, contaFinder.compose(TransactionUpdaterDTO::getConta)));
		transaction.setData(data(dto, TransactionUpdaterDTO::getData));
		transaction.setValor(dto.getValor());
		transaction.setStatus(TransactionStatus.fromChar(dto.getStatus()));
		transaction.setComentario(dto.getComentario());
	}

	public Transaction converter(TransactionRegistryDTO dto, Function<Integer, Category> categoriaFinder,
			Function<Integer, Account> contaFinder) {
		Transaction resultado = null;
		Account account = account(dto, contaFinder.compose(TransactionRegistryDTO::getConta));
		if (dto.isLancamentoCartao() || account instanceof CreditCardAccount) {
			resultado = new CreditCardTransaction();
			((CreditCardTransaction) resultado).setQtdParcelas(dto.getParcelas());
		} else {
			resultado = new CheckingAccountTransaction();

			if (dto.getContaDestino() != null)
				((CheckingAccountTransaction) resultado)
						.setContaDestino(account(dto, contaFinder.compose(TransactionRegistryDTO::getContaDestino)));

		}
		resultado.setData(data(dto, TransactionRegistryDTO::getData));
		resultado.setConta(account);
		if (dto.getCategoria() != null)
			resultado.setCategoria(category(dto, categoriaFinder.compose(TransactionRegistryDTO::getCategoria)));
		resultado.setValor(dto.getValor());
		resultado.setInOut(InOut.fromChar(dto.getTipo()));
		resultado.setStatus(TransactionStatus.fromChar(dto.getStatus()));
		resultado.setComentario(dto.getComentario());
		resultado.setSerie(serie(dto));
		resultado.setAjuste(dto.isAjuste());

		return resultado;
	}

	private <X> Date data(X dto, Function<X, String> getData) {
		return Optional.ofNullable(dto).map(getData).map(DateUtils::iso8601).orElse(null);
	}

	private TransactionSerie serie(TransactionRegistryDTO dto) {
		return Optional.ofNullable(dto).map(TransactionRegistryDTO::getSerie).map(serie -> {
			TransactionSerie transactionSerie = new TransactionSerie();
			transactionSerie.setDataInicio(data(serie, TransactionSerieDTO::getDataInicio));
			transactionSerie.setDataLimite(data(serie, TransactionSerieDTO::getDataLimite));
			transactionSerie.setFrequencia(TransactionFrequence.fromChar(serie.getFrequencia()));
			return transactionSerie;
		}).orElse(null);
	}

	private <X> Category category(X dto, Function<X, Category> categoriaFinder) {
		return Optional.ofNullable(dto).map(categoriaFinder).orElse(null);
	}

	private <X> Account account(X dto, Function<X, Account> contaFinder) {
		return Optional.ofNullable(dto).map(contaFinder).orElse(null);
	}

	public TransactionDTO converter(Transaction transaction) {
		TransactionDTO dto = new TransactionDTO();
		dto.setId(transaction.getId());
		dto.setStatus(status(transaction));
		dto.setData(dataIso8601(transaction));
		dto.setConta(nomeConta(transaction));
		dto.setCategoria(nomeCategoria(transaction));
		dto.setContaOrigem(nomeContaOrigem(transaction));
		dto.setContaDestino(nomeContaDestino(transaction));
		dto.setFaturaCartao(isFaturaCartao(transaction));
		dto.setSaldoInicial(isSaldoInicial(transaction));
		dto.setParteSerie(isParteSerie(transaction));
		dto.setComentario(transaction.getComentario());
		dto.setValor(valorResolvido(transaction));
		dto.setSaldo(transaction.getSaldo());
		dto.setAjuste(transaction.isAjuste());
		dto.setCartaoCreditoFatura(nomeCartaoCreditoFatura(transaction));
		return dto;
	}

	private BigDecimal valorResolvido(Transaction transaction) {
		BigDecimal resultado = transaction.getValor();
		if (InOut.S == transaction.getInOut()) {
			resultado = resultado.multiply(BigDecimal.valueOf(-1l));
		}
		return resultado;
	}

	private boolean isParteSerie(Transaction transaction) {
		return transaction.getSerie() != null;
	}

	private boolean isSaldoInicial(Transaction transaction) {
		return transaction instanceof CheckingAccountTransaction
				&& ((CheckingAccountTransaction) transaction).isSaldoInicial();
	}

	private boolean isFaturaCartao(Transaction transaction) {
		return transaction instanceof CheckingAccountTransaction
				&& ((CheckingAccountTransaction) transaction).isFaturaCartao();
	}

	private String dataIso8601(Transaction transaction) {
		return Optional.ofNullable(transaction).map(Transaction::getData).map(DateUtils::iso8601).orElse(null);
	}

	private Character status(Transaction transaction) {
		return Optional.ofNullable(transaction).map(Transaction::getStatus).map(TransactionStatus::getValue)
				.orElse(null);
	}

	private String nomeConta(Transaction transaction) {
		return Optional.ofNullable(transaction).map(Transaction::getConta).map(c -> c.getNome()).orElse(null);
	}

	// private ContaDTO account(Transaction lancamento) {
	// return Optional.ofNullable(lancamento).map(Transaction::getAccount)
	// .map(c -> new ContaDTO(c.getId(), c.getNome(),
	// c instanceof MoneyAccount ? AccountType.CONTA_DINHEIRO.getValue()
	// : c instanceof BankAccount ? AccountType.CONTA_BANCO.getValue()
	// : AccountType.CARTAO_CREDITO.getValue()))
	// .orElse(null);
	// }

	private BigDecimal saldoInicial(Account c) {
		return Optional.ofNullable(c).filter(CheckingAccount.class::isInstance).map(CheckingAccount.class::cast)
				.map(CheckingAccount::getSaldoInicial).orElse(null);
	}

	private String nomeCategoria(Transaction transaction) {
		return Optional.ofNullable(transaction).map(Transaction::getCategoria).map(c -> c.getNome()).orElse(null);
	}

	private CategoryDTO categoria(Transaction transaction) {
		return Optional.ofNullable(transaction).map(Transaction::getCategoria)
				.map(c -> new CategoryDTO(c.getId(), c.getNome())).orElse(null);
	}

	private String nomeContaDestino(Transaction transaction) {
		return Optional.ofNullable(transaction).filter(CheckingAccountTransaction.class::isInstance)
				.map(CheckingAccountTransaction.class::cast).map(CheckingAccountTransaction::getContaDestino)
				.map(Account::getNome).orElse(null);
	}

	private String nomeContaOrigem(Transaction transaction) {
		return Optional.ofNullable(transaction).filter(CheckingAccountTransaction.class::isInstance)
				.map(CheckingAccountTransaction.class::cast).map(CheckingAccountTransaction::getContaOrigem)
				.map(Account::getNome).orElse(null);
	}

	private String nomeCartaoCreditoFatura(Transaction transaction) {
		return Optional.ofNullable(transaction).filter(CheckingAccountTransaction.class::isInstance)
				.map(CheckingAccountTransaction.class::cast).map(CheckingAccountTransaction::getCartaoCreditoFatura)
				.map(Account::getNome).orElse(null);
	}

}