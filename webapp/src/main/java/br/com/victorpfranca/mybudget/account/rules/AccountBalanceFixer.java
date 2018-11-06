package br.com.victorpfranca.mybudget.account.rules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.account.BankAccount;
import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.account.MoneyAccount;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.view.MonthYear;

/**
 * Este processamento é um apoio para a administração do sistema em casos
 * extremos onde o saldo das contas fiquem corrompidos.
 * 
 * @author victorfranca
 *
 */
@Stateless
public class AccountBalanceFixer {

	@Inject
	private EntityManager em;

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Map<Account, Map<MonthYear, AccountBalance>> reconstruirSaldosContasDoInicio() {

		Map<Account, Map<MonthYear, AccountBalance>> map = new LinkedHashMap<Account, Map<MonthYear, AccountBalance>>();

		List<CheckingAccount> contas = carregarContas();

		for (Iterator<CheckingAccount> iterator = contas.iterator(); iterator.hasNext();) {
			CheckingAccount conta = iterator.next();

			List<CheckingAccountTransaction> lancamentos = carregarLancamentos(conta);

			Map<MonthYear, AccountBalance> mapSaldoContaAnoMes = reconstruirSaldosContasDoInicio(conta, lancamentos);

			map.put(conta, mapSaldoContaAnoMes);
		}

		removerSaldosExistentes();

		for (Map.Entry<Account, Map<MonthYear, AccountBalance>> contaEntry : map.entrySet()) {
			Map<MonthYear, AccountBalance> saldoMap = ((Map<MonthYear, AccountBalance>) contaEntry.getValue());
			gravarSaldos(saldoMap);
		}

		return map;
	}

	public Map<MonthYear, AccountBalance> reconstruirSaldosContasDoInicio(CheckingAccount conta,
			List<CheckingAccountTransaction> lancamentos) {

		Map<MonthYear, AccountBalance> mapSaldoContaAnoMes = new LinkedHashMap<MonthYear, AccountBalance>();

		BigDecimal saldo = BigDecimal.ZERO;

		for (Iterator<CheckingAccountTransaction> iterator2 = lancamentos.iterator(); iterator2.hasNext();) {
			CheckingAccountTransaction lancamento = iterator2.next();

			MonthYear anoMesLancamento = new MonthYear(lancamento.getAno(), lancamento.getMes());
			AccountBalance accountBalance = mapSaldoContaAnoMes.get(anoMesLancamento);
			if (accountBalance == null) {
				accountBalance = new AccountBalance(conta, anoMesLancamento.getAno(), anoMesLancamento.getMes(), saldo);
			}

			lancamento.setValorAnterior(BigDecimal.ZERO);
			accountBalance.add(lancamento);
			saldo = accountBalance.getValor();
			mapSaldoContaAnoMes.put(anoMesLancamento, accountBalance);
		}

		return mapSaldoContaAnoMes;
	}

	private List<CheckingAccountTransaction> carregarLancamentos(Account account) {
		return em.createQuery("select c from CheckingAccountTransaction c where account = :account order by ano asc, mes asc",
				CheckingAccountTransaction.class).setParameter("account", account).getResultList();
	}

	private List<CheckingAccount> carregarContas() {
		List<BankAccount> contasBancos = em.createQuery("select c from BankAccount c", BankAccount.class).getResultList();

		List<MoneyAccount> contasDinheiro = em.createQuery("select c from MoneyAccount c", MoneyAccount.class)
				.getResultList();

		List<CheckingAccount> contas = new ArrayList<CheckingAccount>();
		contas.addAll(contasBancos);
		contas.addAll(contasDinheiro);

		return contas;
	}

	private void gravarSaldos(Map<MonthYear, AccountBalance> mapSaldoContaAnoMes) {
		// gravar os novos saldos de contas
		Collection<AccountBalance> saldos = mapSaldoContaAnoMes.values();
		for (Iterator<AccountBalance> iterator2 = saldos.iterator(); iterator2.hasNext();) {
			AccountBalance accountBalance = iterator2.next();
			em.persist(accountBalance);
		}
	}

	private void removerSaldosExistentes() {
		// remover todos os saldos de contas atuais
		em.createQuery("delete from AccountBalance").executeUpdate();
	}

}
