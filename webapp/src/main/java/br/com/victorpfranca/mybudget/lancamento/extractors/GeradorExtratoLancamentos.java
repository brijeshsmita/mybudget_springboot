package br.com.victorpfranca.mybudget.lancamento.extractors;

import java.math.BigDecimal;
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
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoStatus;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class GeradorExtratoLancamentos {

	@EJB
	private CredentialsStore credentialsStore;

	@Inject
	private EntityManager em;

	public List<Lancamento> execute(int ano, int mes, Account account, Category category, LancamentoStatus status) {
		return execute(ano, mes, account, category, BigDecimal.ZERO, status);
	}

	public List<Lancamento> execute(int ano, int mes, Account account, Category category, BigDecimal saldoInicial,
			LancamentoStatus status) {
		List<Lancamento> lancamentos = em
				.createNamedQuery(Lancamento.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, Lancamento.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", ano)
				.setParameter("mes", mes).setParameter("account", account).setParameter("cartaoCreditoFatura", null)
				.setParameter("faturaCartao", null).setParameter("saldoInicial", null)
				.setParameter("category", category).setParameter("status", status).getResultList();

		List<Lancamento> extrato = new ArrayList<Lancamento>();
		BigDecimal saldoAnterior = saldoInicial;
		for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
			Lancamento lancamento = (Lancamento) iterator.next();
			saldoAnterior = lancamento.somarSaldo(saldoAnterior);
			extrato.add(lancamento);
		}
		return extrato;
	}

}
