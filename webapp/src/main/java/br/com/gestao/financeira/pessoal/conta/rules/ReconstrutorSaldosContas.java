package br.com.gestao.financeira.pessoal.conta.rules;

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

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaBanco;
import br.com.gestao.financeira.pessoal.conta.ContaCorrente;
import br.com.gestao.financeira.pessoal.conta.ContaDinheiro;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoContaCorrente;
import br.com.gestao.financeira.pessoal.view.AnoMes;

/**
 * Este processamento é um apoio para a administração do sistema em casos
 * extremos onde o saldo das contas fiquem corrompidos.
 * 
 * @author victorfranca
 *
 */
@Stateless
public class ReconstrutorSaldosContas {

	@Inject
	private EntityManager em;

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Map<Conta, Map<AnoMes, SaldoConta>> reconstruirSaldosContasDoInicio() {

		Map<Conta, Map<AnoMes, SaldoConta>> map = new LinkedHashMap<Conta, Map<AnoMes, SaldoConta>>();

		List<ContaCorrente> contas = carregarContas();

		for (Iterator<ContaCorrente> iterator = contas.iterator(); iterator.hasNext();) {
			ContaCorrente conta = iterator.next();

			List<LancamentoContaCorrente> lancamentos = carregarLancamentos(conta);

			Map<AnoMes, SaldoConta> mapSaldoContaAnoMes = reconstruirSaldosContasDoInicio(conta, lancamentos);

			map.put(conta, mapSaldoContaAnoMes);
		}

		removerSaldosExistentes();

		for (Map.Entry<Conta, Map<AnoMes, SaldoConta>> contaEntry : map.entrySet()) {
			Map<AnoMes, SaldoConta> saldoMap = ((Map<AnoMes, SaldoConta>) contaEntry.getValue());
			gravarSaldos(saldoMap);
		}

		return map;
	}

	public Map<AnoMes, SaldoConta> reconstruirSaldosContasDoInicio(ContaCorrente conta,
			List<LancamentoContaCorrente> lancamentos) {

		Map<AnoMes, SaldoConta> mapSaldoContaAnoMes = new LinkedHashMap<AnoMes, SaldoConta>();

		BigDecimal saldo = BigDecimal.ZERO;

		for (Iterator<LancamentoContaCorrente> iterator2 = lancamentos.iterator(); iterator2.hasNext();) {
			LancamentoContaCorrente lancamento = iterator2.next();

			AnoMes anoMesLancamento = new AnoMes(lancamento.getAno(), lancamento.getMes());
			SaldoConta saldoConta = mapSaldoContaAnoMes.get(anoMesLancamento);
			if (saldoConta == null) {
				saldoConta = new SaldoConta(conta, anoMesLancamento.getAno(), anoMesLancamento.getMes(), saldo);
			}

			lancamento.setValorAnterior(BigDecimal.ZERO);
			saldoConta.add(lancamento);
			saldo = saldoConta.getValor();
			mapSaldoContaAnoMes.put(anoMesLancamento, saldoConta);
		}

		return mapSaldoContaAnoMes;
	}

	private List<LancamentoContaCorrente> carregarLancamentos(Conta conta) {
		return em.createQuery("select c from LancamentoContaCorrente c where conta = :conta order by ano asc, mes asc",
				LancamentoContaCorrente.class).setParameter("conta", conta).getResultList();
	}

	private List<ContaCorrente> carregarContas() {
		List<ContaBanco> contasBancos = em.createQuery("select c from ContaBanco c", ContaBanco.class).getResultList();

		List<ContaDinheiro> contasDinheiro = em.createQuery("select c from ContaDinheiro c", ContaDinheiro.class)
				.getResultList();

		List<ContaCorrente> contas = new ArrayList<ContaCorrente>();
		contas.addAll(contasBancos);
		contas.addAll(contasDinheiro);

		return contas;
	}

	private void gravarSaldos(Map<AnoMes, SaldoConta> mapSaldoContaAnoMes) {
		// gravar os novos saldos de contas
		Collection<SaldoConta> saldos = mapSaldoContaAnoMes.values();
		for (Iterator<SaldoConta> iterator2 = saldos.iterator(); iterator2.hasNext();) {
			SaldoConta saldoConta = iterator2.next();
			em.persist(saldoConta);
		}
	}

	private void removerSaldosExistentes() {
		// remover todos os saldos de contas atuais
		em.createQuery("delete from SaldoConta").executeUpdate();
	}

}
