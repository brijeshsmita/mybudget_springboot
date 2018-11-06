package br.com.victorpfranca.mybudget.transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.victorpfranca.mybudget.CredentialStoreMock;
import br.com.victorpfranca.mybudget.DAOMock;
import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.account.AtualizadorSaldoContaMock;
import br.com.victorpfranca.mybudget.account.ContaServiceMock;
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.CreditCardInvoiceTransactionItem;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.rules.AtualizadorFaturasCartao;
import br.com.victorpfranca.mybudget.transaction.rules.CriadorLancamentoCartaoCredito;
import br.com.victorpfranca.mybudget.transaction.rules.CriadorLancamentoContaCorrente;
import br.com.victorpfranca.mybudget.transaction.rules.LancamentoRulesFacade;
import br.com.victorpfranca.mybudget.transaction.rules.RemovedorLancamentoCartao;
import br.com.victorpfranca.mybudget.transaction.rules.RemovedorLancamentoContaCorrente;
import br.com.victorpfranca.mybudget.transaction.rules.RemovedorLancamentoFaturaItem;

public class LancamentoRulesFacadeMock extends LancamentoRulesFacade {

	private DAO<Account> contaDAO;
	private DAO<Transaction> lancamentoDAO;
	private DAO<AccountBalance> saldoContaDao;
	private CredentialsStore credentialsStore;

	public LancamentoRulesFacadeMock() {
		initDAOs();

		credentialsStore = new CredentialStoreMock();

		AccountBalanceUpdater accountBalanceUpdater = new AtualizadorSaldoContaMock();
		accountBalanceUpdater.setCredentialsStore(credentialsStore);
		accountBalanceUpdater.setSaldoContaDao(saldoContaDao);

		configurarRemovedor(accountBalanceUpdater);

		initCriadores(accountBalanceUpdater);
	}

	private void initDAOs() {
		saldoContaDao = new SaldoContaDAOMock();
		lancamentoDAO = new LancamentoDAOMock();
		contaDAO = new DAOMock<Account>();
	}

	private void initCriadores(AccountBalanceUpdater accountBalanceUpdater) {
		criadorLancamentoContaCorrente = new CriadorLancamentoContaCorrente();
		criadorLancamentoContaCorrente.setAtualizadorSaldoConta(accountBalanceUpdater);
		criadorLancamentoContaCorrente.setLancamentoDao(lancamentoDAO);
		criadorLancamentoContaCorrente.setRemovedorLancamento(removedorLancamento);
		criadorLancamentoContaCorrente.setContaDao(contaDAO);

		criadorLancamentoCartaoCredito = new CriadorLancamentoCartaoCredito();
		criadorLancamentoCartaoCredito.setAtualizadorSaldoConta(accountBalanceUpdater);
		criadorLancamentoCartaoCredito.setLancamentoDAO(lancamentoDAO);
	}

	private void configurarRemovedor(AccountBalanceUpdater accountBalanceUpdater) {
		removedorLancamento = new RemovedorLancamentoContaCorrente();
		removedorLancamento.setAtualizadorSaldoConta(accountBalanceUpdater);
		removedorLancamento.setLancamentoDAO(lancamentoDAO);

		removedorLancamentoCartao = new RemovedorLancamentoCartao();
		removedorLancamentoCartao.setAtualizadorSaldoConta(accountBalanceUpdater);
		removedorLancamentoCartao.setLancamentoDAO(lancamentoDAO);

		AtualizadorFaturasCartao atualizadorFaturasCartao = new AtualizadorFaturasCartao();
		atualizadorFaturasCartao.setLancamentoDAO(lancamentoDAO);
		atualizadorFaturasCartao.setCredentialsStore(credentialsStore);
		removedorLancamentoCartao.setAtualizadorFaturasCartao(atualizadorFaturasCartao);

		RemovedorLancamentoFaturaItem removedorLancamentoFaturaItem = new RemovedorLancamentoFaturaItem();
		removedorLancamentoFaturaItem.setLancamentoDAO(lancamentoDAO);
		removedorLancamentoFaturaItem.setCredentialsStore(credentialsStore);
		removedorLancamentoCartao.setRemovedorLancamentoFaturaItem(removedorLancamentoFaturaItem);
	}

	static LancamentoRulesFacadeMock build() {
		return new LancamentoRulesFacadeMock();
	}

	public List<AccountBalance> getSaldos() {
		return saldoContaDao.findAll();
	}

	public List<Transaction> getLancamentos() {
		return lancamentoDAO.findAll();
	}

	public List<CreditCardTransaction> getLancamentosCartao() {
		List<CreditCardTransaction> lancamentosCartao = new ArrayList<CreditCardTransaction>();

		List<Transaction> transactions = lancamentoDAO.findAll();
		for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = iterator.next();
			if (transaction instanceof CreditCardTransaction) {
				lancamentosCartao.add((CreditCardTransaction) transaction);
			}

		}
		return lancamentosCartao;
	}

	public List<CheckingAccountTransaction> getLancamentosFaturas() {
		List<CheckingAccountTransaction> faturas = new ArrayList<CheckingAccountTransaction>();

		List<Transaction> transactions = lancamentoDAO.findAll();
		for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = iterator.next();
			if (transaction instanceof CheckingAccountTransaction
					&& ((CheckingAccountTransaction) transaction).isFaturaCartao()) {
				faturas.add((CheckingAccountTransaction) transaction);
			}

		}
		return faturas;
	}

	public List<CreditCardInvoiceTransactionItem> getLancamentosItensFatura() {
		List<CreditCardInvoiceTransactionItem> lancamentosFaturaCartaoItem = new ArrayList<CreditCardInvoiceTransactionItem>();

		List<Transaction> transactions = lancamentoDAO.findAll();
		for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = iterator.next();
			if (transaction instanceof CreditCardInvoiceTransactionItem) {
				lancamentosFaturaCartaoItem.add((CreditCardInvoiceTransactionItem) transaction);
			}

		}
		return lancamentosFaturaCartaoItem;
	}

}
