package br.com.victorpfranca.mybudget.transaction.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.infra.dao.QueryParam;
import br.com.victorpfranca.mybudget.transaction.Transaction;

@Stateless
public class CreditCardAccountInvoiceItemRemover {

	@EJB
	private DAO<Transaction> lancamentoDAO;

	@EJB
	private CredentialsStore credentialsStore;
	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Transaction> remover(Transaction lancamentoCartaoCredito) {
		List<Transaction> transactions = lancamentoDAO.executeQuery(Transaction.FIND_LANCAMENTO_FATURA_CARTAO_ITEM_QUERY,
				new QueryParam("lancamentoCartao", lancamentoCartaoCredito), new QueryParam("ano", null), new QueryParam("mes", null),
				new QueryParam("user", credentialsStore.recuperarIdUsuarioLogado()), new QueryParam("account", null),
				new QueryParam("category", null));

		for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction) iterator.next();
			lancamentoDAO.remove(transaction);
		}

		return transactions;
	}
	
	public void setCredentialsStore(CredentialsStore credentialsStore) {
		this.credentialsStore = credentialsStore;
	}
	
	public void setLancamentoDAO(DAO<Transaction> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}

}
