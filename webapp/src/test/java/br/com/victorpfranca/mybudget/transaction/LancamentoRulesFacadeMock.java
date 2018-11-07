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
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.transaction.rules.CheckingAccountTransactionBuilder;
import br.com.victorpfranca.mybudget.transaction.rules.CheckingAccountTransactionRemover;
import br.com.victorpfranca.mybudget.transaction.rules.CreditCardAccountInvoiceItemRemover;
import br.com.victorpfranca.mybudget.transaction.rules.CreditCardAccountTransactionRemover;
import br.com.victorpfranca.mybudget.transaction.rules.CreditCardInvoiceUpdater;
import br.com.victorpfranca.mybudget.transaction.rules.CreditCardTransactionBuilder;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionRulesFacade;

public class LancamentoRulesFacadeMock extends TransactionRulesFacade {

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
		checkingAccountTransactionBuilder = new CheckingAccountTransactionBuilder();
		checkingAccountTransactionBuilder.setAtualizadorSaldoConta(accountBalanceUpdater);
		checkingAccountTransactionBuilder.setLancamentoDao(lancamentoDAO);
		checkingAccountTransactionBuilder.setRemovedorLancamento(removedorLancamento);
		checkingAccountTransactionBuilder.setContaDao(contaDAO);

		creditCardTransactionBuilder = new CreditCardTransactionBuilder();
		creditCardTransactionBuilder.setAtualizadorSaldoConta(accountBalanceUpdater);
		creditCardTransactionBuilder.setLancamentoDAO(lancamentoDAO);
	}

	private void configurarRemovedor(AccountBalanceUpdater accountBalanceUpdater) {
		removedorLancamento = new CheckingAccountTransactionRemover();
		removedorLancamento.setAtualizadorSaldoConta(accountBalanceUpdater);
		removedorLancamento.setLancamentoDAO(lancamentoDAO);

		creditCardAccountTransactionRemover = new CreditCardAccountTransactionRemover();
		creditCardAccountTransactionRemover.setAtualizadorSaldoConta(accountBalanceUpdater);
		creditCardAccountTransactionRemover.setLancamentoDAO(lancamentoDAO);

		CreditCardInvoiceUpdater creditCardInvoiceUpdater = new CreditCardInvoiceUpdater();
		creditCardInvoiceUpdater.setLancamentoDAO(lancamentoDAO);
		creditCardInvoiceUpdater.setCredentialsStore(credentialsStore);
		creditCardAccountTransactionRemover.setAtualizadorFaturasCartao(creditCardInvoiceUpdater);

		CreditCardAccountInvoiceItemRemover creditCardAccountInvoiceItemRemover = new CreditCardAccountInvoiceItemRemover();
		creditCardAccountInvoiceItemRemover.setLancamentoDAO(lancamentoDAO);
		creditCardAccountInvoiceItemRemover.setCredentialsStore(credentialsStore);
		creditCardAccountTransactionRemover.setRemovedorLancamentoFaturaItem(creditCardAccountInvoiceItemRemover);
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
