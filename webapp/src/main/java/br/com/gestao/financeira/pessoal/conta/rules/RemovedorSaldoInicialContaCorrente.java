package br.com.gestao.financeira.pessoal.conta.rules;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;

@Stateless
public class RemovedorSaldoInicialContaCorrente {

	private Conta conta;

	@Inject
	private EntityManager em;
	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AtualizadorSaldoConta atualizadorSaldoConta;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute(Conta conta) {
		this.conta = conta;

		Lancamento lancamento = removerLancamento();

		if (lancamento != null)
			atualizadorSaldoConta.removeSaldos(lancamento);
	}

	private Lancamento removerLancamento() {
		List<Lancamento> lancamentosSaldosIniciais = em
				.createNamedQuery(Lancamento.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, Lancamento.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("conta", conta)
				.setParameter("categoria", null).setParameter("saldoInicial", true).setParameter("ano", null)
				.setParameter("mes", null).setParameter("cartaoCreditoFatura", null).setParameter("faturaCartao", null)
				.setParameter("status", null).getResultList();

		Lancamento lancamento = null;
		if (!lancamentosSaldosIniciais.isEmpty()) {
			lancamento = lancamentosSaldosIniciais.get(0);
			em.remove(lancamento);
		}

		return lancamento;
	}

}
