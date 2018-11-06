package br.com.gestao.financeira.pessoal.lancamento.extractors.saldofuturo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.orcamento.SaldoOrcadoAcumuladoMes;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class GeradorSaldoFuturo {

	@EJB
	private CredentialsStore credentialsStore;

	@Inject
	private EntityManager em;

	@EJB
	private AgrupadorSaldosMeses agrupadorSaldosMeses;

	public List<SaldoConta> execute(int anoFrom, int mesFrom, int anoAte, int mesAte) {

		List<Conta> contas = findContasCorrentes();

		List<SaldoConta> saldosPorContas = getSaldosContas(contas, anoFrom, mesFrom);

		List<SaldoConta> saldosPorMes = agrupadorSaldosMeses.agruparSaldosPorMes(anoFrom, mesFrom, anoAte, mesAte,
				contas, saldosPorContas);

		criarPrevisaoComOrcados(anoAte, mesAte, saldosPorMes);

		return saldosPorMes;

	}

	private void criarPrevisaoComOrcados(int anoAte, int mesAte, List<SaldoConta> saldosPreparados) {

		List<SaldoOrcadoAcumuladoMes> orcamentosDespesas = findOrcamentosDespesas(anoAte, mesAte);

		List<SaldoOrcadoAcumuladoMes> orcamentosReceitas = findOrcamentosReceitas(anoAte, mesAte);

		for (Iterator<SaldoConta> iterator = saldosPreparados.iterator(); iterator.hasNext();) {
			SaldoConta saldoConta = iterator.next();

			int ano = saldoConta.getAno();
			int mes = saldoConta.getMes();

			for (int i = orcamentosDespesas.size() - 1; i >= 0; i--) {
				if (orcamentosDespesas.get(i).compareDate(ano, mes) == 0) {
					saldoConta.setValor(saldoConta.getValor().subtract(orcamentosDespesas.get(i).getSaldo()));
					break;
				}
			}

			for (int i = orcamentosReceitas.size() - 1; i >= 0; i--) {
				if (orcamentosReceitas.get(i).compareDate(ano, mes) == 0) {
					saldoConta.setValor(saldoConta.getValor().add(orcamentosReceitas.get(i).getSaldo()));
					break;
				}
			}

		}
	}

	private List<SaldoConta> getSaldosContas(List<Conta> contas, int anoFrom, int mesFrom) {
		List<SaldoConta> saldosPorContas = new ArrayList<SaldoConta>();

		for (Iterator<Conta> iterator = contas.iterator(); iterator.hasNext();) {
			Conta conta = iterator.next();

			List<SaldoConta> saldosConta = em.createNamedQuery(SaldoConta.FIND_FROM_ANO_MES_QUERY, SaldoConta.class)
					.setParameter("usuario", credentialsStore.recuperarUsuarioLogado()).setParameter("conta", conta)
					.setParameter("ano", anoFrom).setParameter("mes", mesFrom).getResultList();

			if (saldosConta.isEmpty()) {
				saldosConta = em.createNamedQuery(SaldoConta.FIND_UNTIL_ANO_MES_QUERY, SaldoConta.class)
						.setParameter("usuario", credentialsStore.recuperarUsuarioLogado()).setParameter("conta", conta)
						.setParameter("ano", anoFrom).setParameter("mes", mesFrom).setMaxResults(1).getResultList();
			}

			saldosPorContas.addAll(saldosConta);
			saldosPorContas.forEach(s -> s.setConta(conta));
		}
		return saldosPorContas;
	}

	private List<Conta> findContasCorrentes() {
		List<Conta> contasCorrentes = new ArrayList<Conta>();
		contasCorrentes.addAll(em.createNamedQuery(Conta.FIND_ALL_CONTA_BANCO_QUERY, Conta.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).getResultList());
		contasCorrentes.addAll(em.createNamedQuery(Conta.FIND_ALL_CONTA_DINHEIRO_QUERY, Conta.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).getResultList());
		return contasCorrentes;
	}

	private List<SaldoOrcadoAcumuladoMes> findOrcamentosReceitas(int anoAte, int mesAte) {
		List<SaldoOrcadoAcumuladoMes> orcamentosReceitas = em
				.createNamedQuery(SaldoOrcadoAcumuladoMes.FIND_BY_RECEITA_UNTIL_MONTH, SaldoOrcadoAcumuladoMes.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", anoAte)
				.setParameter("mes", mesAte).getResultList();
		return orcamentosReceitas;
	}

	private List<SaldoOrcadoAcumuladoMes> findOrcamentosDespesas(int anoAte, int mesAte) {
		List<SaldoOrcadoAcumuladoMes> orcamentosDespesas = em
				.createNamedQuery(SaldoOrcadoAcumuladoMes.FIND_BY_DESPESA_UNTIL_MONTH, SaldoOrcadoAcumuladoMes.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", anoAte)
				.setParameter("mes", mesAte).getResultList();
		return orcamentosDespesas;
	}

}
