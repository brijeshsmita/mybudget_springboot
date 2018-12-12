package br.com.victorpfranca.mybudget.account.rules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.infra.dao.QueryParam;
import br.com.victorpfranca.mybudget.period.PlanningPeriod;
import br.com.victorpfranca.mybudget.transaction.Transaction;

@Stateless
public class AccountBalanceUpdater {

	@EJB
	DAO<AccountBalance> saldoContaDao;

	@EJB
	private PlanningPeriod planningPeriod;

	@EJB
	private CredentialsStore credentialsStore;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addSaldos(Transaction transaction) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);
		addSaldos(transactions);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addSaldos(List<Transaction> transactions) {
		if (!transactions.isEmpty()) {

			List<AccountBalance> saldos = carregarSaldos(transactions, null);

			for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
				Transaction transaction = (Transaction) iterator.next();
				addSaldosMesesPosteriores(saldos, transaction);
			}
			persistir(saldos);
		}
	}

	private void addSaldosMesesPosteriores(List<AccountBalance> saldoContaList, Transaction transaction) {
		Integer anoLancamento = transaction.getAno();
		Integer mesLancamento = transaction.getMes();

		Iterator<AccountBalance> iterator = saldoContaList.iterator();
		while (iterator.hasNext()) {
			AccountBalance accountBalance = (AccountBalance) iterator.next();
			if (accountBalance.compareDate(anoLancamento, mesLancamento) >= 0)
				accountBalance.add(transaction);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSaldos(Transaction transaction) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);
		removeSaldos(transactions, transaction.getConta());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSaldos(List<Transaction> transactions, Account account) {
		if (!transactions.isEmpty()) {

			List<AccountBalance> saldos = carregarSaldos(transactions, account);

			for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
				Transaction transaction = (Transaction) iterator.next();
				removeSaldosMesesPosteriores(saldos, transaction);
			}
			persistir(saldos);
		}
	}

	private void removeSaldosMesesPosteriores(List<AccountBalance> saldoContaList, Transaction transaction) {
		Integer anoLancamento = transaction.getAno();
		Integer mesLancamento = transaction.getMes();

		Iterator<AccountBalance> iterator = saldoContaList.iterator();
		while (iterator.hasNext()) {
			AccountBalance accountBalance = (AccountBalance) iterator.next();
			if (accountBalance.compareDate(anoLancamento, mesLancamento) >= 0)
				accountBalance.remove(transaction);
		}
	}

	private List<AccountBalance> carregarSaldos(List<Transaction> transactions, Account account) {
		List<AccountBalance> novosSaldos = new ArrayList<AccountBalance>();

		Transaction primeiroLancamento = transactions.get(0);
		int anoPrimeiroLancamento = primeiroLancamento.getAno();
		int mesPrimeiroLancamento = primeiroLancamento.getMes();

		if (account == null) {
			account = primeiroLancamento.getConta();
		}

		List<AccountBalance> saldosContaLancamento = saldoContaDao.executeQuery(AccountBalance.FIND_FROM_ANO_MES_QUERY,
				QueryParam.build("user", credentialsStore.recuperarUsuarioLogado()),
				QueryParam.build("conta", account), QueryParam.build("ano", anoPrimeiroLancamento),
				QueryParam.build("mes", mesPrimeiroLancamento));

		for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = iterator.next();

			BigDecimal saldoAteAnoMesLancamento = getSaldoAte(account, transaction.getAno(), transaction.getMes());

			AccountBalance saldo = new AccountBalance().withConta(account).withAno(transaction.getAno())
					.withMes(transaction.getMes()).withValor(saldoAteAnoMesLancamento);
			if (saldosContaLancamento.isEmpty() || !existeSaldoMesLancamento(saldosContaLancamento, transaction)) {
				saldo = saldoContaDao.merge(saldo);
				novosSaldos.add(saldo);
			}
		}

		saldosContaLancamento.addAll(novosSaldos);

		return saldosContaLancamento;
	}

	protected BigDecimal getSaldoAte(Account account, Integer ano, Integer mes) {
		List<AccountBalance> saldos = saldoContaDao.createNamedQuery(AccountBalance.FIND_UNTIL_ANO_MES_QUERY)
				.setParameter("user", credentialsStore.recuperarUsuarioLogado()).setParameter("conta", account)
				.setParameter("ano", ano).setParameter("mes", mes).setMaxResults(1).getResultList();

		if (!saldos.isEmpty()) {
			return saldos.get(0).getValor();
		}

		return BigDecimal.ZERO;
	}

	private boolean existeSaldoMesLancamento(List<AccountBalance> saldosContaLancamento, Transaction transaction) {
		for (Iterator<AccountBalance> iteratorSaldo = saldosContaLancamento.iterator(); iteratorSaldo.hasNext();) {
			AccountBalance accountBalance = iteratorSaldo.next();

			if (accountBalance.compareDate(transaction.getAno(), transaction.getMes()) == 0) {
				return true;
			}
		}
		return false;
	}

	private void persistir(List<AccountBalance> saldos) {
		for (Iterator<AccountBalance> iterator = saldos.iterator(); iterator.hasNext();) {
			AccountBalance accountBalance = (AccountBalance) iterator.next();
			saldoContaDao.merge(accountBalance);
		}
	}

	public void setSaldoContaDao(DAO<AccountBalance> saldoContaDao) {
		this.saldoContaDao = saldoContaDao;
	}
	
	public void setCredentialsStore(CredentialsStore credentialsStore) {
		this.credentialsStore = credentialsStore;
	}

}
