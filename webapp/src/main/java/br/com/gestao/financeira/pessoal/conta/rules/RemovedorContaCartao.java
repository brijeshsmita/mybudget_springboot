package br.com.gestao.financeira.pessoal.conta.rules;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemocaoNaoPermitidaException;

@Stateless
public class RemovedorContaCartao {

	@EJB
	private RemovedorLancamentosCartao removedorLancamentos;

	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ContaCartao conta)
			throws RemocaoNaoPermitidaException, NaoRemovivelException {

		removedorLancamentos.execute(conta);

		em.remove(em.contains(conta) ? conta : em.merge(conta));

	}

}
