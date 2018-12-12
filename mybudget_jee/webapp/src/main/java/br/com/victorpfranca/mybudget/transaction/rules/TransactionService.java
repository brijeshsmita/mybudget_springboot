package br.com.victorpfranca.mybudget.transaction.rules;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionSerie;
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;

@Stateless
public class TransactionService {

	@EJB
	private TransactionRulesFacade transactionRulesFacade;

	public List<Transaction> carregarExtratoCorrenteMensal(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial, TransactionStatus status) {
		return transactionRulesFacade.extrairExtrato(ano, mes, account, category, saldoInicial, status);
	}

	public List<Transaction> carregarExtratoCartaoMensal(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial) {
		return transactionRulesFacade.extrairExtratoCartao(ano, mes, account, category, saldoInicial);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TransactionSerie saveSerie(Transaction transaction)
			throws NullableAccountException, IncompatibleCategoriesException, TransactionMonthUpdatedException,
			AccountTypeException, InvalidTransactionValueException, InvalidTransactionSerieDateException {

		TransactionSerie serie = null;
		if (transaction instanceof CreditCardTransaction) {
			serie = transactionRulesFacade.saveSerieLancamentoCartaoDeCredito(transaction);
		} else if (transaction instanceof CheckingAccountTransaction
				&& ((CheckingAccountTransaction) transaction).isTransferencia()) {
			serie = transactionRulesFacade.saveSerieLancamentoTransferencia(transaction);
		} else {
			serie = transactionRulesFacade.saveSerieLancamentoContaCorrente(transaction);
		}

		return serie;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction save(Transaction transaction) throws NullableAccountException, IncompatibleCategoriesException,
			TransactionMonthUpdatedException, AccountTypeException, InvalidTransactionValueException {

		if (transaction instanceof CreditCardTransaction) {
			transaction = transactionRulesFacade.saveLancamentoCartaoDeCredito(transaction);
		} else if (transaction instanceof CheckingAccountTransaction
				&& ((CheckingAccountTransaction) transaction).isTransferencia()) {
			transaction = transactionRulesFacade.saveLancamentoTransferencia(transaction);
		} else {
			transaction = transactionRulesFacade.saveLancamentoContaCorrente(transaction);
		}

		return transaction;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction confirmar(Transaction transaction) throws NullableAccountException, IncompatibleCategoriesException,
			TransactionMonthUpdatedException, AccountTypeException, InvalidTransactionValueException {
		if (transaction.isConfirmado())
			transaction.setStatus(TransactionStatus.NAO_CONFIRMADO);
		else
			transaction.setStatus(TransactionStatus.CONFIRMADO);

		try {
			return save(transaction);
		} catch (NullableAccountException | IncompatibleCategoriesException | TransactionMonthUpdatedException
				| AccountTypeException | InvalidTransactionValueException e) {
			if (transaction.isConfirmado())
				transaction.setStatus(TransactionStatus.NAO_CONFIRMADO);
			else
				transaction.setStatus(TransactionStatus.CONFIRMADO);
			throw e;
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Transaction transaction) {
		transactionRulesFacade.removeLancamento(transaction);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSerie(TransactionSerie serie) {
		transactionRulesFacade.removeSerie(serie);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLancamentoCartao(CreditCardTransaction lancamentoCartao) {
		transactionRulesFacade.removeLancamentoCartao(lancamentoCartao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSerieLancamentoCartao(TransactionSerie serie) {
		transactionRulesFacade.removeSerieLancamentoCartao(serie);
	}

}
