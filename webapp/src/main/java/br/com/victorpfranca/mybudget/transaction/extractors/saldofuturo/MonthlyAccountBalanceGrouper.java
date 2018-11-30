package br.com.victorpfranca.mybudget.transaction.extractors.saldofuturo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class MonthlyAccountBalanceGrouper {

	@EJB
	private AccountBalancePreparator accountBalancePreparator;
	
	@EJB
	private CredentialsStore credentialsStore;
	
	@Inject
	private EntityManager em;

	public List<AccountBalance> agruparSaldosPorMes(int anoFrom, int mesFrom, int anoAte, int mesAte,
			List<Account> accounts, List<AccountBalance> saldosPorContas) {

		Map<Account, List<AccountBalance>> saldosPorContasMap = new HashMap<Account, List<AccountBalance>>();
		for (Iterator<Account> iterator = accounts.iterator(); iterator.hasNext();) {
			Account account = iterator.next();
			saldosPorContasMap.put(account, new ArrayList<AccountBalance>());
		}
		saldosPorContas=new ArrayList<>();
		saldosPorContas.addAll(saldosPorContas.stream().filter(saldoConta -> new MonthYear(saldoConta.getAno(), saldoConta.getMes())
                        .compareTo(new MonthYear(anoAte, mesAte)) <= 0).collect(Collectors.toList()));
		for (Iterator<AccountBalance> iterator = saldosPorContas.iterator(); iterator.hasNext();) {
			AccountBalance saldo = iterator.next();
			saldosPorContasMap.get(saldo.getConta()).add(saldo);
		}

		for (Iterator<List<AccountBalance>> iterator = saldosPorContasMap.values().iterator(); iterator.hasNext();) {
			List<AccountBalance> saldosConta = iterator.next();
			accountBalancePreparator.prepararListSaldos(anoFrom, mesFrom, anoAte, mesAte, saldosConta);
		}

		List<AccountBalance> saldosPorMeses = new ArrayList<AccountBalance>();
		MonthYear anoMesFrom = new MonthYear(anoFrom, mesFrom);
		MonthYear anoMesAte = new MonthYear(anoAte, mesAte);
		while (anoMesFrom.compareTo(anoMesAte) <= 0) {
			AccountBalance accountBalance = new AccountBalance(anoFrom, mesFrom, BigDecimal.ZERO);
			anoMesFrom = anoMesFrom.plusMonths(1);
			anoFrom = anoMesFrom.getAno();
			mesFrom = anoMesFrom.getMes();
			saldosPorMeses.add(accountBalance);
		}

		for (Iterator<List<AccountBalance>> iterator = saldosPorContasMap.values().iterator(); iterator.hasNext();) {
			List<AccountBalance> saldosConta = iterator.next();
			for (int i = 0; i < saldosConta.size(); i++) {
				AccountBalance accountBalance = saldosConta.get(i);
				saldosPorMeses.get(i).setValor(saldosPorMeses.get(i).getValor().add(accountBalance.getValor()));
			}

		}

		return saldosPorMeses;
	}


    List<AccountBalance> listaSaldosPorMesComValorZero(int anoFrom, int mesFrom, int anoAte, int mesAte) {
        List<AccountBalance> saldosPorMeses = new ArrayList<AccountBalance>();
        MonthYear anoMesFrom = new MonthYear(anoFrom, mesFrom);
        MonthYear anoMesAte = new MonthYear(anoAte, mesAte);
        while (anoMesFrom.compareTo(anoMesAte) <= 0) {
            AccountBalance accountBalance = new AccountBalance(anoFrom, mesFrom, BigDecimal.ZERO);
            anoMesFrom = anoMesFrom.plusMonths(1);
            anoFrom = anoMesFrom.getAno();
            mesFrom = anoMesFrom.getMes();
            saldosPorMeses.add(accountBalance);
        }
        return saldosPorMeses;
    }

    public List<AccountBalance> agruparSaldosPorMes(int anoFrom, int mesFrom, int anoAte, int mesAte,
            List<AccountBalance> saldosPorContas) {
        saldosPorContas = inicializarListaDeSaldos(anoFrom, mesFrom, anoAte, mesAte, saldosPorContas);
        return aplicarTransformacoes(anoFrom, mesFrom, anoAte, mesAte, saldosPorContas);
    }

    private List<AccountBalance> aplicarTransformacoes(int anoFrom, int mesFrom, int anoAte, int mesAte,
            List<AccountBalance> saldosPorContas) {
        Predicate<AccountBalance> filtroDesdeDataInicio = saldoConta -> new MonthYear(saldoConta.getAno(), saldoConta.getMes())
                .compareTo(new MonthYear(anoFrom, mesFrom)) >= 0;
        Predicate<AccountBalance> filtroAteDataLimite = saldoConta -> new MonthYear(saldoConta.getAno(), saldoConta.getMes())
                .compareTo(new MonthYear(anoAte, mesAte)) <= 0;
        Function<AccountBalance, MonthYear> conversaoParaAgrupador = saldoConta -> new MonthYear(saldoConta.getAno(),
                saldoConta.getMes());
        Function<Entry<MonthYear, BigDecimal>, AccountBalance> converterSaldoAgrupado = entry -> new AccountBalance(
                entry.getKey().getAno(), entry.getKey().getMes(), entry.getValue());
        Function<AccountBalance, BigDecimal> accountBalance = (s) -> saldoAcumuladoAte(s.getAno(), s.getMes(), saldosPorContas);
        BinaryOperator<BigDecimal> funcaoDeMerge = BigDecimal::max;
        List<AccountBalance> saldosPorMes = saldosPorContas.parallelStream().filter(filtroDesdeDataInicio)
                .filter(filtroAteDataLimite)
                .collect(Collectors.toConcurrentMap(conversaoParaAgrupador, accountBalance, funcaoDeMerge)).entrySet()
                .stream().sorted(Comparator.comparing(Entry::getKey)).map(converterSaldoAgrupado)
                .collect(Collectors.toList());
        return saldosPorMes;
    }

    private ArrayList<AccountBalance> inicializarListaDeSaldos(int anoFrom, int mesFrom, int anoAte, int mesAte,
            List<AccountBalance> saldosPorContas) {
        ArrayList<AccountBalance> arrayList = new ArrayList<>(
                listaSaldosPorMesComValorZero(anoFrom, mesFrom, anoAte, mesAte));
        arrayList.addAll(Optional.ofNullable(saldosPorContas).orElseGet(ArrayList::new));
        saldosPorContas.parallelStream().map(AccountBalance::getConta).filter(CheckingAccount.class::isInstance)
                .map(CheckingAccount.class::cast).map(cc -> {
                    LocalDate date = LocalDateConverter.fromDate(cc.getDataSaldoInicial());
                    return new AccountBalance(date.getYear(), date.getMonthValue(), cc.getSaldoInicial());
                }).forEach(arrayList::add);

        return arrayList;
    }

    BigDecimal saldoAcumuladoAte(int anoFrom, int mesFrom, List<AccountBalance> saldosPorContas) {
        Predicate<AccountBalance> filtroAteDataInicio = saldoConta -> new MonthYear(saldoConta.getAno(), saldoConta.getMes())
                .compareTo(new MonthYear(anoFrom, mesFrom)) <= 0;
        BigDecimal saldoAcumuladoAteDataInicio = Optional.ofNullable(saldosPorContas).orElseGet(ArrayList::new)
                .parallelStream().filter(filtroAteDataInicio).map(AccountBalance::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return saldoAcumuladoAteDataInicio;
    }

}
