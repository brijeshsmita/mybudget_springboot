package br.com.victorpfranca.mybudget.account.rules;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.transaction.rules.DeletionNotPermittedException;

@Stateless
public class CreditCardAccountRemover {

	@EJB
	private CreditCardTransactionRemover removedorLancamentos;

	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(CreditCardAccount conta)
			throws DeletionNotPermittedException, CantRemoveException {

		removedorLancamentos.execute(conta);

		em.remove(em.contains(conta) ? conta : em.merge(conta));

	}

}
