package br.com.victorpfranca.mybudget.account.rules;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;

@Stateless
public class AccountDataValidator {

	@Inject
	private EntityManager em;

	@EJB
	private CredentialsStore credentialsStore;

	public AccountDataValidator() {
	}

	public void validar(Account account) throws SameNameException {
		List<Account> accounts = em.createNamedQuery(Account.FIND_BY_NAME_QUERY, Account.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado())
				.setParameter("nome", account.getNome()).getResultList();
		if (!accounts.isEmpty() && account.getId() == null)
			throw new SameNameException("crud.conta.error.nome_existente");
	}

}
