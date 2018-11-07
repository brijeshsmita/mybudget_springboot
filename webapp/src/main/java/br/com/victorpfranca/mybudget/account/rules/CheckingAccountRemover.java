package br.com.victorpfranca.mybudget.account.rules;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.rules.DeletionNotPermittedException;

@Stateless
public class CheckingAccountRemover {

	@Inject
	private EntityManager em;
	@EJB
	private CredentialsStore credentialsStore;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(CheckingAccount conta) throws DeletionNotPermittedException, CantRemoveException {

		aprovarRemocao(conta);

		removerLancamentoSaldoInicial(conta);

		removerSaldoConta(conta);

		em.remove(em.contains(conta) ? conta : em.merge(conta));

	}

	private void aprovarRemocao(CheckingAccount conta) throws DeletionNotPermittedException, CantRemoveException {
		validarSemLancamentos(conta);

		validarContaCorrenteDeContaCartao(conta);
	}

	private void removerSaldoConta(CheckingAccount conta) {
		em.createNamedQuery(AccountBalance.REMOVE_SALDOS_INICIAIS_QUERY).setParameter("account", conta).executeUpdate();
	}

	private void removerLancamentoSaldoInicial(CheckingAccount conta) {
		em.createNamedQuery(Transaction.REMOVE_LANCAMENTOS_CONTA_CORRENTE_QUERY).setParameter("account", conta)
				.setParameter("saldoInicial", true).executeUpdate();
	}

	private void validarContaCorrenteDeContaCartao(Account account) throws CantRemoveException {
		List<CreditCardAccount> contasCartao = em
				.createQuery("select c from CreditCardAccount c where contaPagamentoFatura = :contaPagamentoFatura",
						CreditCardAccount.class)
				.setParameter("contaPagamentoFatura", account).getResultList();
		if (!contasCartao.isEmpty())
			throw new CantRemoveException("crud.conta.error.conta_corrente_de_conta_cartao_nao_pode_ser_removida",
					contasCartao.get(0));

	}

	private void validarSemLancamentos(Account account) throws DeletionNotPermittedException {
		List<Transaction> lancamentosExistentes = em
				.createNamedQuery(Transaction.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, Transaction.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("account", account)
				.setParameter("category", null).setParameter("saldoInicial", false).setParameter("ano", null)
				.setParameter("mes", null).setParameter("cartaoCreditoFatura", null).setParameter("faturaCartao", null).setParameter("status", null)
				.getResultList();
		if (!lancamentosExistentes.isEmpty())
			throw new DeletionNotPermittedException("crud.conta.error.lancamentos_nao_podem_ser_removidos");
	}

}
