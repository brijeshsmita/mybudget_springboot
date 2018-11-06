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
public class CriadorLancamentoContaCorrente {

	@EJB
	CriadorSerieLancamento criadorSerieLancamento;

	@EJB
	AccountBalanceUpdater accountBalanceUpdater;

	@EJB
	RemovedorLancamentoContaCorrente removedorLancamento;

	@EJB
	DAO<Transaction> lancamentoDao;

	@EJB
	DAO<Account> contaDao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction save(Transaction transaction) throws MesLancamentoAlteradoException, ContaNotNullException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		return save(transaction.getAccount(), transaction);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction save(Account account, Transaction transaction)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		transaction.validar();

		if (((CheckingAccountTransaction) transaction).isSaldoInicial()) {
			((CheckingAccount) account).setSaldoInicial(transaction.getValor());
			contaDao.merge(account);
		}

		transaction.setAccount(account);

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
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		Account contaOrigem = ((CheckingAccountTransaction) lancamentoOrigem).getAccount();
		Account contaDestino = ((CheckingAccountTransaction) lancamentoOrigem).getContaDestino();

		((CheckingAccountTransaction) lancamentoOrigem).setContaOrigem(null);
		lancamentoOrigem = save(lancamentoOrigem);

		Transaction lancamentoDestino = (Transaction) lancamentoOrigem.clone();

		lancamentoDestino.setAccount(contaDestino);
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

	public void setRemovedorLancamento(RemovedorLancamentoContaCorrente removedorLancamento) {
		this.removedorLancamento = removedorLancamento;
	}

}
