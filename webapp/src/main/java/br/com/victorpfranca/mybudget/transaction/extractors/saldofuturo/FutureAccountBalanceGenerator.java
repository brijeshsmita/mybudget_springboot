package br.com.victorpfranca.mybudget.transaction.extractors.saldofuturo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.budget.MonthAccumulatedBudgetedBalance;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class FutureAccountBalanceGenerator {

	@EJB
	private CredentialsStore credentialsStore;

	@Inject
	private EntityManager em;

	@EJB
	private MonthlyAccountBalanceGrouper monthlyAccountBalanceGrouper;

	public List<AccountBalance> execute(int anoFrom, int mesFrom, int anoAte, int mesAte) {

		List<Account> accounts = findContasCorrentes();

		List<AccountBalance> saldosPorContas = getSaldosContas(accounts, anoFrom, mesFrom);

		List<AccountBalance> saldosPorMes = monthlyAccountBalanceGrouper.agruparSaldosPorMes(anoFrom, mesFrom, anoAte, mesAte,
				accounts, saldosPorContas);

		criarPrevisaoComOrcados(anoAte, mesAte, saldosPorMes);

		return saldosPorMes;

	}

	private void criarPrevisaoComOrcados(int anoAte, int mesAte, List<AccountBalance> saldosPreparados) {

		List<MonthAccumulatedBudgetedBalance> orcamentosDespesas = findOrcamentosDespesas(anoAte, mesAte);

		List<MonthAccumulatedBudgetedBalance> orcamentosReceitas = findOrcamentosReceitas(anoAte, mesAte);

		for (Iterator<AccountBalance> iterator = saldosPreparados.iterator(); iterator.hasNext();) {
			AccountBalance accountBalance = iterator.next();

			int ano = accountBalance.getAno();
			int mes = accountBalance.getMes();

			for (int i = orcamentosDespesas.size() - 1; i >= 0; i--) {
				if (orcamentosDespesas.get(i).compareDate(ano, mes) == 0) {
					accountBalance.setValor(accountBalance.getValor().subtract(orcamentosDespesas.get(i).getSaldo()));
					break;
				}
			}

			for (int i = orcamentosReceitas.size() - 1; i >= 0; i--) {
				if (orcamentosReceitas.get(i).compareDate(ano, mes) == 0) {
					accountBalance.setValor(accountBalance.getValor().add(orcamentosReceitas.get(i).getSaldo()));
					break;
				}
			}

		}
	}

	private List<AccountBalance> getSaldosContas(List<Account> accounts, int anoFrom, int mesFrom) {
		List<AccountBalance> saldosPorContas = new ArrayList<AccountBalance>();

		for (Iterator<Account> iterator = accounts.iterator(); iterator.hasNext();) {
			Account account = iterator.next();

			List<AccountBalance> saldosConta = em.createNamedQuery(AccountBalance.FIND_FROM_ANO_MES_QUERY, AccountBalance.class)
					.setParameter("user", credentialsStore.recuperarUsuarioLogado()).setParameter("conta", account)
					.setParameter("ano", anoFrom).setParameter("mes", mesFrom).getResultList();

			if (saldosConta.isEmpty()) {
				saldosConta = em.createNamedQuery(AccountBalance.FIND_UNTIL_ANO_MES_QUERY, AccountBalance.class)
						.setParameter("user", credentialsStore.recuperarUsuarioLogado()).setParameter("conta", account)
						.setParameter("ano", anoFrom).setParameter("mes", mesFrom).setMaxResults(1).getResultList();
			}

			saldosPorContas.addAll(saldosConta);
			saldosPorContas.forEach(s -> s.setConta(account));
		}
		return saldosPorContas;
	}

	private List<Account> findContasCorrentes() {
		List<Account> contasCorrentes = new ArrayList<Account>();
		contasCorrentes.addAll(em.createNamedQuery(Account.FIND_ALL_CONTA_BANCO_QUERY, Account.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).getResultList());
		contasCorrentes.addAll(em.createNamedQuery(Account.FIND_ALL_CONTA_DINHEIRO_QUERY, Account.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).getResultList());
		return contasCorrentes;
	}

	private List<MonthAccumulatedBudgetedBalance> findOrcamentosReceitas(int anoAte, int mesAte) {
		List<MonthAccumulatedBudgetedBalance> orcamentosReceitas = em
				.createNamedQuery(MonthAccumulatedBudgetedBalance.FIND_BY_RECEITA_UNTIL_MONTH, MonthAccumulatedBudgetedBalance.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", anoAte)
				.setParameter("mes", mesAte).getResultList();
		return orcamentosReceitas;
	}

	private List<MonthAccumulatedBudgetedBalance> findOrcamentosDespesas(int anoAte, int mesAte) {
		List<MonthAccumulatedBudgetedBalance> orcamentosDespesas = em
				.createNamedQuery(MonthAccumulatedBudgetedBalance.FIND_BY_DESPESA_UNTIL_MONTH, MonthAccumulatedBudgetedBalance.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", anoAte)
				.setParameter("mes", mesAte).getResultList();
		return orcamentosDespesas;
	}

}
