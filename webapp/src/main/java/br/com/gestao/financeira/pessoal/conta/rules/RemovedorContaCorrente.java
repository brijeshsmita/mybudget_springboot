package br.com.gestao.financeira.pessoal.conta.rules;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.conta.ContaCorrente;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemocaoNaoPermitidaException;

@Stateless
public class RemovedorContaCorrente {

	@Inject
	private EntityManager em;
	@EJB
	private CredentialsStore credentialsStore;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ContaCorrente conta) throws RemocaoNaoPermitidaException, NaoRemovivelException {

		aprovarRemocao(conta);

		removerLancamentoSaldoInicial(conta);

		removerSaldoConta(conta);

		em.remove(em.contains(conta) ? conta : em.merge(conta));

	}

	private void aprovarRemocao(ContaCorrente conta) throws RemocaoNaoPermitidaException, NaoRemovivelException {
		validarSemLancamentos(conta);

		validarContaCorrenteDeContaCartao(conta);
	}

	private void removerSaldoConta(ContaCorrente conta) {
		em.createNamedQuery(SaldoConta.REMOVE_SALDOS_INICIAIS_QUERY).setParameter("conta", conta).executeUpdate();
	}

	private void removerLancamentoSaldoInicial(ContaCorrente conta) {
		em.createNamedQuery(Lancamento.REMOVE_LANCAMENTOS_CONTA_CORRENTE_QUERY).setParameter("conta", conta)
				.setParameter("saldoInicial", true).executeUpdate();
	}

	private void validarContaCorrenteDeContaCartao(Conta conta) throws NaoRemovivelException {
		List<ContaCartao> contasCartao = em
				.createQuery("select c from ContaCartao c where contaPagamentoFatura = :contaPagamentoFatura",
						ContaCartao.class)
				.setParameter("contaPagamentoFatura", conta).getResultList();
		if (!contasCartao.isEmpty())
			throw new NaoRemovivelException("crud.conta.error.conta_corrente_de_conta_cartao_nao_pode_ser_removida",
					contasCartao.get(0));

	}

	private void validarSemLancamentos(Conta conta) throws RemocaoNaoPermitidaException {
		List<Lancamento> lancamentosExistentes = em
				.createNamedQuery(Lancamento.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, Lancamento.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("conta", conta)
				.setParameter("categoria", null).setParameter("saldoInicial", false).setParameter("ano", null)
				.setParameter("mes", null).setParameter("cartaoCreditoFatura", null).setParameter("faturaCartao", null).setParameter("status", null)
				.getResultList();
		if (!lancamentosExistentes.isEmpty())
			throw new RemocaoNaoPermitidaException("crud.conta.error.lancamentos_nao_podem_ser_removidos");
	}

}
