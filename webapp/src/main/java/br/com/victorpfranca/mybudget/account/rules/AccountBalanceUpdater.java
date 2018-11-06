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
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.periodo.PeriodoPlanejamento;

@Stateless
public class AccountBalanceUpdater {

	@EJB
	DAO<AccountBalance> saldoContaDao;

	@EJB
	private PeriodoPlanejamento periodoPlanejamento;

	@EJB
	private CredentialsStore credentialsStore;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addSaldos(Lancamento lancamento) {
		List<Lancamento> lancamentos = new ArrayList<Lancamento>();
		lancamentos.add(lancamento);
		addSaldos(lancamentos);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addSaldos(List<Lancamento> lancamentos) {
		if (!lancamentos.isEmpty()) {

			List<AccountBalance> saldos = carregarSaldos(lancamentos, null);

			for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
				Lancamento lancamento = (Lancamento) iterator.next();
				addSaldosMesesPosteriores(saldos, lancamento);
			}
			persistir(saldos);
		}
	}

	private void addSaldosMesesPosteriores(List<AccountBalance> saldoContaList, Lancamento lancamento) {
		Integer anoLancamento = lancamento.getAno();
		Integer mesLancamento = lancamento.getMes();

		Iterator<AccountBalance> iterator = saldoContaList.iterator();
		while (iterator.hasNext()) {
			AccountBalance accountBalance = (AccountBalance) iterator.next();
			if (accountBalance.compareDate(anoLancamento, mesLancamento) >= 0)
				accountBalance.add(lancamento);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSaldos(Lancamento lancamento) {
		List<Lancamento> lancamentos = new ArrayList<Lancamento>();
		lancamentos.add(lancamento);
		removeSaldos(lancamentos, lancamento.getAccount());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSaldos(List<Lancamento> lancamentos, Account account) {
		if (!lancamentos.isEmpty()) {

			List<AccountBalance> saldos = carregarSaldos(lancamentos, account);

			for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
				Lancamento lancamento = (Lancamento) iterator.next();
				removeSaldosMesesPosteriores(saldos, lancamento);
			}
			persistir(saldos);
		}
	}

	private void removeSaldosMesesPosteriores(List<AccountBalance> saldoContaList, Lancamento lancamento) {
		Integer anoLancamento = lancamento.getAno();
		Integer mesLancamento = lancamento.getMes();

		Iterator<AccountBalance> iterator = saldoContaList.iterator();
		while (iterator.hasNext()) {
			AccountBalance accountBalance = (AccountBalance) iterator.next();
			if (accountBalance.compareDate(anoLancamento, mesLancamento) >= 0)
				accountBalance.remove(lancamento);
		}
	}

	private List<AccountBalance> carregarSaldos(List<Lancamento> lancamentos, Account account) {
		List<AccountBalance> novosSaldos = new ArrayList<AccountBalance>();

		Lancamento primeiroLancamento = lancamentos.get(0);
		int anoPrimeiroLancamento = primeiroLancamento.getAno();
		int mesPrimeiroLancamento = primeiroLancamento.getMes();

		if (account == null) {
			account = primeiroLancamento.getAccount();
		}

		List<AccountBalance> saldosContaLancamento = saldoContaDao.executeQuery(AccountBalance.FIND_FROM_ANO_MES_QUERY,
				QueryParam.build("user", credentialsStore.recuperarUsuarioLogado()),
				QueryParam.build("account", account), QueryParam.build("ano", anoPrimeiroLancamento),
				QueryParam.build("mes", mesPrimeiroLancamento));

		for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
			Lancamento lancamento = iterator.next();

			BigDecimal saldoAteAnoMesLancamento = getSaldoAte(account, lancamento.getAno(), lancamento.getMes());

			AccountBalance saldo = new AccountBalance().withConta(account).withAno(lancamento.getAno())
					.withMes(lancamento.getMes()).withValor(saldoAteAnoMesLancamento);
			if (saldosContaLancamento.isEmpty() || !existeSaldoMesLancamento(saldosContaLancamento, lancamento)) {
				saldo = saldoContaDao.merge(saldo);
				novosSaldos.add(saldo);
			}
		}

		saldosContaLancamento.addAll(novosSaldos);

		return saldosContaLancamento;
	}

	protected BigDecimal getSaldoAte(Account account, Integer ano, Integer mes) {
		List<AccountBalance> saldos = saldoContaDao.createNamedQuery(AccountBalance.FIND_UNTIL_ANO_MES_QUERY)
				.setParameter("user", credentialsStore.recuperarUsuarioLogado()).setParameter("account", account)
				.setParameter("ano", ano).setParameter("mes", mes).setMaxResults(1).getResultList();

		if (!saldos.isEmpty()) {
			return saldos.get(0).getValor();
		}

		return BigDecimal.ZERO;
	}

	private boolean existeSaldoMesLancamento(List<AccountBalance> saldosContaLancamento, Lancamento lancamento) {
		for (Iterator<AccountBalance> iteratorSaldo = saldosContaLancamento.iterator(); iteratorSaldo.hasNext();) {
			AccountBalance accountBalance = iteratorSaldo.next();

			if (accountBalance.compareDate(lancamento.getAno(), lancamento.getMes()) == 0) {
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
