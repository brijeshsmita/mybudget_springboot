package br.com.gestao.financeira.pessoal.conta.rules;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;

@Stateless
public class ValidadorDadosConta {

	@Inject
	private EntityManager em;

	@EJB
	private CredentialsStore credentialsStore;

	public ValidadorDadosConta() {
	}

	public void validar(Conta conta) throws MesmoNomeExistenteException {
		List<Conta> contas = em.createNamedQuery(Conta.FIND_BY_NAME_QUERY, Conta.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado())
				.setParameter("nome", conta.getNome()).getResultList();
		if (!contas.isEmpty() && conta.getId() == null)
			throw new MesmoNomeExistenteException("crud.conta.error.nome_existente");
	}

}
