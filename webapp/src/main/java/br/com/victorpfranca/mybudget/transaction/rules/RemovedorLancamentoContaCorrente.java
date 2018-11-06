package br.com.victorpfranca.mybudget.transaction.rules;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;
import br.com.victorpfranca.mybudget.account.rules.CheckingAccountInitialBalanceRemover;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionSerie;

@Stateless
public class RemovedorLancamentoContaCorrente {

	@EJB
	private DAO<Transaction> lancamentoDAO;

	public DAO<Transaction> getLancamentoDAO() {
		return lancamentoDAO;
	}

	public AccountBalanceUpdater getAtualizadorSaldoConta() {
		return accountBalanceUpdater;
	}

	@EJB
	private DAO<TransactionSerie> serieDAO;

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@EJB
	private CheckingAccountInitialBalanceRemover checkingAccountInitialBalanceRemover;
	
	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerSerie(TransactionSerie serie) {
		List<Transaction> lancamentosSerie = lancamentoDAO.createNamedQuery(Transaction.FIND_LANCAMENTO_QUERY)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("serie", serie)
				.setParameter("category", null).getResultList();

		for (Iterator<Transaction> iterator = lancamentosSerie.iterator(); iterator.hasNext();) {
			Transaction serieLancamento = (Transaction) iterator.next();
			remover(serieLancamento);
		}

		serieDAO.remove(serieDAO.contains(serie) ? serie : serieDAO.merge(serie));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Transaction transaction) {

		if (((CheckingAccountTransaction) transaction).isSaldoInicial()) {
			checkingAccountInitialBalanceRemover.execute(transaction.getAccount());
			((CheckingAccount)transaction.getAccount()).setSaldoInicial(BigDecimal.ZERO);
			em.merge(transaction.getAccount());
		} else {
			accountBalanceUpdater.removeSaldos(transaction);
			lancamentoDAO.remove(lancamentoDAO.contains(transaction) ? transaction : lancamentoDAO.merge(transaction));
		}
	}

	public void setLancamentoDAO(DAO<Transaction> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}

	public void setAtualizadorSaldoConta(AccountBalanceUpdater accountBalanceUpdater) {
		this.accountBalanceUpdater = accountBalanceUpdater;
	}
	

}
