package br.com.victorpfranca.mybudget.transaction.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionSerie;

@Stateless
public class RemovedorLancamentoCartao {

	@EJB
	private DAO<Transaction> lancamentoDAO;

	@EJB
	private DAO<TransactionSerie> serieLancamentoDAO;

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private RemovedorLancamentoFaturaItem removedorLancamentoFaturaItem;

	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@EJB
	private AtualizadorFaturasCartao atualizadorFaturasCartao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Transaction transaction, boolean abaterLancamentoFatura) {

		// removerLancamentoFaturaItem
		List<Transaction> faturaItens = removedorLancamentoFaturaItem.remover(transaction);

		// abaterLancamentosFaturas
		if (abaterLancamentoFatura)
			atualizadorFaturasCartao.abater(faturaItens);

		// removerLancamentoCartao
		lancamentoDAO.remove(lancamentoDAO.contains(transaction) ? transaction : lancamentoDAO.merge(transaction));

		// recomporSaldoContas
		if (abaterLancamentoFatura)
			accountBalanceUpdater.removeSaldos(faturaItens,
					((CreditCardAccount) transaction.getAccount()).getAccountPagamentoFatura());

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerSerie(TransactionSerie serie, boolean abaterLancamentoFatura) {
		List<Transaction> transactions = lancamentoDAO.createNamedQuery(Transaction.FIND_LANCAMENTO_CARTAO_QUERY)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("serie", serie)
				.setParameter("saldInicial", null).setParameter("account", null).getResultList();
		for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction) iterator.next();
			remover(transaction, abaterLancamentoFatura);
		}

		serieLancamentoDAO.remove(serieLancamentoDAO.contains(serie) ? serie : serieLancamentoDAO.merge(serie));
	}

	public void setRemovedorLancamentoFaturaItem(RemovedorLancamentoFaturaItem removedorLancamentoFaturaItem) {
		this.removedorLancamentoFaturaItem = removedorLancamentoFaturaItem;
	}

	public void setAtualizadorFaturasCartao(AtualizadorFaturasCartao atualizadorFaturasCartao) {
		this.atualizadorFaturasCartao = atualizadorFaturasCartao;
	}

	public void setAtualizadorSaldoConta(AccountBalanceUpdater accountBalanceUpdater) {
		this.accountBalanceUpdater = accountBalanceUpdater;
	}

	public void setLancamentoDAO(DAO<Transaction> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}

}
