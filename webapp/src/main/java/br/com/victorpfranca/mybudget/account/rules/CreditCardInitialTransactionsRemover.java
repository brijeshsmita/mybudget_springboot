package br.com.victorpfranca.mybudget.account.rules;

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
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;

@Stateless
public class CreditCardInitialTransactionsRemover {

	@Inject
	private EntityManager em;

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute(Account account) {

		removerLancamentosFaturaItem(account);

		removerLancamentosCartao(account);

		List<Lancamento> faturas = removerFaturas(account);
		if (faturas != null)
			accountBalanceUpdater.removeSaldos(faturas, ((CreditCardAccount) account).getAccountPagamentoFatura());
	}

	private void removerLancamentosCartao(Account account) {
		em.createNamedQuery(Lancamento.REMOVE_LANCAMENTOS_CARTAO_CREDITO_QUERY).setParameter("account", account)
				.setParameter("saldoInicial", true).executeUpdate();
	}

	private void removerLancamentosFaturaItem(Account account) {
		em.createNamedQuery(Lancamento.REMOVE_LANCAMENTOS_FATURA_CARTAO_ITEM_QUERY).setParameter("account", account)
				.setParameter("saldoInicial", true).executeUpdate();
	}

	private List<Lancamento> removerFaturas(Account account) {
		List<Lancamento> lancamentosFaturas = em
				.createNamedQuery(Lancamento.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, Lancamento.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado())
				.setParameter("cartaoCreditoFatura", account).setParameter("faturaCartao", true)
				.setParameter("saldoInicial", null).setParameter("ano", null).setParameter("mes", null)
				.setParameter("status", null).setParameter("account", null).setParameter("category", null)
				.getResultList();

		for (Iterator<Lancamento> iterator = lancamentosFaturas.iterator(); iterator.hasNext();) {
			Lancamento lancamento = (Lancamento) iterator.next();
			em.remove(lancamento);
		}

		return lancamentosFaturas;
	}

}
