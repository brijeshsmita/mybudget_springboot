package br.com.victorpfranca.mybudget.transaction.rules;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;

@Stateless
public class CheckingAccountTransactionBuilder {

	@EJB
	TransactionSerieBuilder transactionSerieBuilder;

	@EJB
	AccountBalanceUpdater accountBalanceUpdater;

	@EJB
	CheckingAccountTransactionRemover removedorLancamento;

	@EJB
	DAO<Transaction> lancamentoDao;

	@EJB
	DAO<Account> contaDao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction save(Transaction transaction) throws TransactionMonthUpdatedException, NullableAccountException,
			AccountTypeException, IncompatibleCategoriesException, InvalidTransactionValueException {
		return save(transaction.getConta(), transaction);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction save(Account account, Transaction transaction)
			throws TransactionMonthUpdatedException, NullableAccountException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {

		transaction.validar();

		if (((CheckingAccountTransaction) transaction).isSaldoInicial()) {
			((CheckingAccount) account).setSaldoInicial(transaction.getValor());
			contaDao.merge(account);
		}

		transaction.setConta(account);

		if (!transaction.contaFoiAlterada()) {
			accountBalanceUpdater.addSaldos(transaction);
			transaction = lancamentoDao.merge(transaction);
		} else {
			Transaction lancamentoAnterior = transaction.getLancamentoAnterior();
			removedorLancamento.remover(lancamentoAnterior);

			transaction.setContaAnterior(null);
			transaction.setValorAnterior(BigDecimal.ZERO);
			transaction.setDataAnterior(null);

			save(transaction);
		}

		return transaction;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction saveTransferencia(Transaction lancamentoOrigem)
			throws TransactionMonthUpdatedException, NullableAccountException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {

		Account contaOrigem = ((CheckingAccountTransaction) lancamentoOrigem).getConta();
		Account contaDestino = ((CheckingAccountTransaction) lancamentoOrigem).getContaDestino();

		((CheckingAccountTransaction) lancamentoOrigem).setContaOrigem(null);
		lancamentoOrigem = save(lancamentoOrigem);

		Transaction lancamentoDestino = (Transaction) lancamentoOrigem.clone();

		lancamentoDestino.setConta(contaDestino);
		((CheckingAccountTransaction) lancamentoDestino).setContaOrigem(contaOrigem);
		((CheckingAccountTransaction) lancamentoDestino).setContaDestino(null);

		lancamentoDestino.setInOut(InOut.E);

		lancamentoDestino.setContaAnterior(null);
		lancamentoDestino.setValorAnterior(null);
		lancamentoDestino.setDataAnterior(null);

		save(lancamentoDestino);

		return lancamentoOrigem;
	}

	public void setLancamentoDao(DAO<Transaction> lancamentoDao) {
		this.lancamentoDao = lancamentoDao;
	}

	public void setAtualizadorSaldoConta(AccountBalanceUpdater accountBalanceUpdater) {
		this.accountBalanceUpdater = accountBalanceUpdater;
	}

	public void setContaDao(DAO<Account> contaDao) {
		this.contaDao = contaDao;
	}

	public void setRemovedorLancamento(CheckingAccountTransactionRemover removedorLancamento) {
		this.removedorLancamento = removedorLancamento;
	}

}
