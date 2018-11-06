package br.com.gestao.financeira.pessoal.conta.rules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.infra.dao.DAO;
import br.com.gestao.financeira.pessoal.infra.dao.QueryParam;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.periodo.PeriodoPlanejamento;

@Stateless
public class AtualizadorSaldoConta {

	@EJB
	DAO<SaldoConta> saldoContaDao;

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

			List<SaldoConta> saldos = carregarSaldos(lancamentos, null);

			for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
				Lancamento lancamento = (Lancamento) iterator.next();
				addSaldosMesesPosteriores(saldos, lancamento);
			}
			persistir(saldos);
		}
	}

	private void addSaldosMesesPosteriores(List<SaldoConta> saldoContaList, Lancamento lancamento) {
		Integer anoLancamento = lancamento.getAno();
		Integer mesLancamento = lancamento.getMes();

		Iterator<SaldoConta> iterator = saldoContaList.iterator();
		while (iterator.hasNext()) {
			SaldoConta saldoConta = (SaldoConta) iterator.next();
			if (saldoConta.compareDate(anoLancamento, mesLancamento) >= 0)
				saldoConta.add(lancamento);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSaldos(Lancamento lancamento) {
		List<Lancamento> lancamentos = new ArrayList<Lancamento>();
		lancamentos.add(lancamento);
		removeSaldos(lancamentos, lancamento.getConta());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSaldos(List<Lancamento> lancamentos, Conta conta) {
		if (!lancamentos.isEmpty()) {

			List<SaldoConta> saldos = carregarSaldos(lancamentos, conta);

			for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
				Lancamento lancamento = (Lancamento) iterator.next();
				removeSaldosMesesPosteriores(saldos, lancamento);
			}
			persistir(saldos);
		}
	}

	private void removeSaldosMesesPosteriores(List<SaldoConta> saldoContaList, Lancamento lancamento) {
		Integer anoLancamento = lancamento.getAno();
		Integer mesLancamento = lancamento.getMes();

		Iterator<SaldoConta> iterator = saldoContaList.iterator();
		while (iterator.hasNext()) {
			SaldoConta saldoConta = (SaldoConta) iterator.next();
			if (saldoConta.compareDate(anoLancamento, mesLancamento) >= 0)
				saldoConta.remove(lancamento);
		}
	}

	private List<SaldoConta> carregarSaldos(List<Lancamento> lancamentos, Conta conta) {
		List<SaldoConta> novosSaldos = new ArrayList<SaldoConta>();

		Lancamento primeiroLancamento = lancamentos.get(0);
		int anoPrimeiroLancamento = primeiroLancamento.getAno();
		int mesPrimeiroLancamento = primeiroLancamento.getMes();

		if (conta == null) {
			conta = primeiroLancamento.getConta();
		}

		List<SaldoConta> saldosContaLancamento = saldoContaDao.executeQuery(SaldoConta.FIND_FROM_ANO_MES_QUERY,
				QueryParam.build("usuario", credentialsStore.recuperarUsuarioLogado()),
				QueryParam.build("conta", conta), QueryParam.build("ano", anoPrimeiroLancamento),
				QueryParam.build("mes", mesPrimeiroLancamento));

		for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
			Lancamento lancamento = iterator.next();

			BigDecimal saldoAteAnoMesLancamento = getSaldoAte(conta, lancamento.getAno(), lancamento.getMes());

			SaldoConta saldo = new SaldoConta().withConta(conta).withAno(lancamento.getAno())
					.withMes(lancamento.getMes()).withValor(saldoAteAnoMesLancamento);
			if (saldosContaLancamento.isEmpty() || !existeSaldoMesLancamento(saldosContaLancamento, lancamento)) {
				saldo = saldoContaDao.merge(saldo);
				novosSaldos.add(saldo);
			}
		}

		saldosContaLancamento.addAll(novosSaldos);

		return saldosContaLancamento;
	}

	protected BigDecimal getSaldoAte(Conta conta, Integer ano, Integer mes) {
		List<SaldoConta> saldos = saldoContaDao.createNamedQuery(SaldoConta.FIND_UNTIL_ANO_MES_QUERY)
				.setParameter("usuario", credentialsStore.recuperarUsuarioLogado()).setParameter("conta", conta)
				.setParameter("ano", ano).setParameter("mes", mes).setMaxResults(1).getResultList();

		if (!saldos.isEmpty()) {
			return saldos.get(0).getValor();
		}

		return BigDecimal.ZERO;
	}

	private boolean existeSaldoMesLancamento(List<SaldoConta> saldosContaLancamento, Lancamento lancamento) {
		for (Iterator<SaldoConta> iteratorSaldo = saldosContaLancamento.iterator(); iteratorSaldo.hasNext();) {
			SaldoConta saldoConta = iteratorSaldo.next();

			if (saldoConta.compareDate(lancamento.getAno(), lancamento.getMes()) == 0) {
				return true;
			}
		}
		return false;
	}

	private void persistir(List<SaldoConta> saldos) {
		for (Iterator<SaldoConta> iterator = saldos.iterator(); iterator.hasNext();) {
			SaldoConta saldoConta = (SaldoConta) iterator.next();
			saldoContaDao.merge(saldoConta);
		}
	}

	public void setSaldoContaDao(DAO<SaldoConta> saldoContaDao) {
		this.saldoContaDao = saldoContaDao;
	}
	
	public void setCredentialsStore(CredentialsStore credentialsStore) {
		this.credentialsStore = credentialsStore;
	}

}
