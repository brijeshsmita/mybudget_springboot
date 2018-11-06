package br.com.gestao.financeira.pessoal.conta.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;

@Stateless
public class RemovedorLancamentosCartao {

	@Inject
	private EntityManager em;
	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AtualizadorSaldoConta atualizadorSaldoConta;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute(Conta conta) {

		removerLancamentosFaturaItem(conta);

		removerLancamentosCartao(conta);

		List<Lancamento> faturas = removerFaturas(conta);
		if (faturas != null)
			atualizadorSaldoConta.removeSaldos(faturas, ((ContaCartao) conta).getContaPagamentoFatura());
	}

	private void removerLancamentosCartao(Conta conta) {
		em.createNamedQuery(Lancamento.REMOVE_LANCAMENTOS_CARTAO_CREDITO_QUERY).setParameter("conta", conta)
				.setParameter("saldoInicial", null).executeUpdate();
	}

	private void removerLancamentosFaturaItem(Conta conta) {
		em.createNamedQuery(Lancamento.REMOVE_LANCAMENTOS_FATURA_CARTAO_ITEM_QUERY).setParameter("conta", conta)
				.setParameter("saldoInicial", null).executeUpdate();
	}

	private List<Lancamento> removerFaturas(Conta conta) {
		List<Lancamento> lancamentosFaturas = em
				.createNamedQuery(Lancamento.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, Lancamento.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado())
				.setParameter("cartaoCreditoFatura", conta).setParameter("faturaCartao", true)
				.setParameter("saldoInicial", null).setParameter("ano", null).setParameter("mes", null).setParameter("status", null)
				.setParameter("conta", null).setParameter("categoria", null).getResultList();

		for (Iterator<Lancamento> iterator = lancamentosFaturas.iterator(); iterator.hasNext();) {
			Lancamento lancamento = (Lancamento) iterator.next();
			em.remove(lancamento);
		}

		return lancamentosFaturas;
	}

}
