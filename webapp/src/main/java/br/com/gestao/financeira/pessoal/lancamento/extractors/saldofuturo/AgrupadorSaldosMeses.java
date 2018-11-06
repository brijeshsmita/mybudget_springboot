package br.com.gestao.financeira.pessoal.lancamento.extractors.saldofuturo;

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

import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaCorrente;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AgrupadorSaldosMeses {

	@EJB
	private PreparadorSaldoConta preparadorSaldoConta;
	
	@EJB
	private CredentialsStore credentialsStore;
	
	@Inject
	private EntityManager em;

	public List<SaldoConta> agruparSaldosPorMes(int anoFrom, int mesFrom, int anoAte, int mesAte,
			List<Conta> contas, List<SaldoConta> saldosPorContas) {

		Map<Conta, List<SaldoConta>> saldosPorContasMap = new HashMap<Conta, List<SaldoConta>>();
		for (Iterator<Conta> iterator = contas.iterator(); iterator.hasNext();) {
			Conta conta = iterator.next();
			saldosPorContasMap.put(conta, new ArrayList<SaldoConta>());
		}
		saldosPorContas=new ArrayList<>();
		saldosPorContas.addAll(saldosPorContas.stream().filter(saldoConta -> new AnoMes(saldoConta.getAno(), saldoConta.getMes())
                        .compareTo(new AnoMes(anoAte, mesAte)) <= 0).collect(Collectors.toList()));
		for (Iterator<SaldoConta> iterator = saldosPorContas.iterator(); iterator.hasNext();) {
			SaldoConta saldo = iterator.next();
			saldosPorContasMap.get(saldo.getConta()).add(saldo);
		}

		for (Iterator<List<SaldoConta>> iterator = saldosPorContasMap.values().iterator(); iterator.hasNext();) {
			List<SaldoConta> saldosConta = iterator.next();
			preparadorSaldoConta.prepararListSaldos(anoFrom, mesFrom, anoAte, mesAte, saldosConta);
		}

		List<SaldoConta> saldosPorMeses = new ArrayList<SaldoConta>();
		AnoMes anoMesFrom = new AnoMes(anoFrom, mesFrom);
		AnoMes anoMesAte = new AnoMes(anoAte, mesAte);
		while (anoMesFrom.compareTo(anoMesAte) <= 0) {
			SaldoConta saldoConta = new SaldoConta(anoFrom, mesFrom, BigDecimal.ZERO);
			anoMesFrom = anoMesFrom.plusMonths(1);
			anoFrom = anoMesFrom.getAno();
			mesFrom = anoMesFrom.getMes();
			saldosPorMeses.add(saldoConta);
		}

		for (Iterator<List<SaldoConta>> iterator = saldosPorContasMap.values().iterator(); iterator.hasNext();) {
			List<SaldoConta> saldosConta = iterator.next();
			for (int i = 0; i < saldosConta.size(); i++) {
				SaldoConta saldoConta = saldosConta.get(i);
				saldosPorMeses.get(i).setValor(saldosPorMeses.get(i).getValor().add(saldoConta.getValor()));
			}

		}

		return saldosPorMeses;
	}


    List<SaldoConta> listaSaldosPorMesComValorZero(int anoFrom, int mesFrom, int anoAte, int mesAte) {
        List<SaldoConta> saldosPorMeses = new ArrayList<SaldoConta>();
        AnoMes anoMesFrom = new AnoMes(anoFrom, mesFrom);
        AnoMes anoMesAte = new AnoMes(anoAte, mesAte);
        while (anoMesFrom.compareTo(anoMesAte) <= 0) {
            SaldoConta saldoConta = new SaldoConta(anoFrom, mesFrom, BigDecimal.ZERO);
            anoMesFrom = anoMesFrom.plusMonths(1);
            anoFrom = anoMesFrom.getAno();
            mesFrom = anoMesFrom.getMes();
            saldosPorMeses.add(saldoConta);
        }
        return saldosPorMeses;
    }

    public List<SaldoConta> agruparSaldosPorMes(int anoFrom, int mesFrom, int anoAte, int mesAte,
            List<SaldoConta> saldosPorContas) {
        saldosPorContas = inicializarListaDeSaldos(anoFrom, mesFrom, anoAte, mesAte, saldosPorContas);
        return aplicarTransformacoes(anoFrom, mesFrom, anoAte, mesAte, saldosPorContas);
    }

    private List<SaldoConta> aplicarTransformacoes(int anoFrom, int mesFrom, int anoAte, int mesAte,
            List<SaldoConta> saldosPorContas) {
        Predicate<SaldoConta> filtroDesdeDataInicio = saldoConta -> new AnoMes(saldoConta.getAno(), saldoConta.getMes())
                .compareTo(new AnoMes(anoFrom, mesFrom)) >= 0;
        Predicate<SaldoConta> filtroAteDataLimite = saldoConta -> new AnoMes(saldoConta.getAno(), saldoConta.getMes())
                .compareTo(new AnoMes(anoAte, mesAte)) <= 0;
        Function<SaldoConta, AnoMes> conversaoParaAgrupador = saldoConta -> new AnoMes(saldoConta.getAno(),
                saldoConta.getMes());
        Function<Entry<AnoMes, BigDecimal>, SaldoConta> converterSaldoAgrupado = entry -> new SaldoConta(
                entry.getKey().getAno(), entry.getKey().getMes(), entry.getValue());
        Function<SaldoConta, BigDecimal> saldoConta = (s) -> saldoAcumuladoAte(s.getAno(), s.getMes(), saldosPorContas);
        BinaryOperator<BigDecimal> funcaoDeMerge = BigDecimal::max;
        List<SaldoConta> saldosPorMes = saldosPorContas.parallelStream().filter(filtroDesdeDataInicio)
                .filter(filtroAteDataLimite)
                .collect(Collectors.toConcurrentMap(conversaoParaAgrupador, saldoConta, funcaoDeMerge)).entrySet()
                .stream().sorted(Comparator.comparing(Entry::getKey)).map(converterSaldoAgrupado)
                .collect(Collectors.toList());
        return saldosPorMes;
    }

    private ArrayList<SaldoConta> inicializarListaDeSaldos(int anoFrom, int mesFrom, int anoAte, int mesAte,
            List<SaldoConta> saldosPorContas) {
        ArrayList<SaldoConta> arrayList = new ArrayList<>(
                listaSaldosPorMesComValorZero(anoFrom, mesFrom, anoAte, mesAte));
        arrayList.addAll(Optional.ofNullable(saldosPorContas).orElseGet(ArrayList::new));
        saldosPorContas.parallelStream().map(SaldoConta::getConta).filter(ContaCorrente.class::isInstance)
                .map(ContaCorrente.class::cast).map(cc -> {
                    LocalDate date = LocalDateConverter.fromDate(cc.getDataSaldoInicial());
                    return new SaldoConta(date.getYear(), date.getMonthValue(), cc.getSaldoInicial());
                }).forEach(arrayList::add);

        return arrayList;
    }

    BigDecimal saldoAcumuladoAte(int anoFrom, int mesFrom, List<SaldoConta> saldosPorContas) {
        Predicate<SaldoConta> filtroAteDataInicio = saldoConta -> new AnoMes(saldoConta.getAno(), saldoConta.getMes())
                .compareTo(new AnoMes(anoFrom, mesFrom)) <= 0;
        BigDecimal saldoAcumuladoAteDataInicio = Optional.ofNullable(saldosPorContas).orElseGet(ArrayList::new)
                .parallelStream().filter(filtroAteDataInicio).map(SaldoConta::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return saldoAcumuladoAteDataInicio;
    }

}
