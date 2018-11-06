package br.com.victorpfranca.mybudget.lancamento.rules;

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
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoContaCorrente;
import br.com.victorpfranca.mybudget.lancamento.SerieLancamento;

@Stateless
public class RemovedorLancamentoContaCorrente {

	@EJB
	private DAO<Lancamento> lancamentoDAO;

	public DAO<Lancamento> getLancamentoDAO() {
		return lancamentoDAO;
	}

	public AccountBalanceUpdater getAtualizadorSaldoConta() {
		return accountBalanceUpdater;
	}

	@EJB
	private DAO<SerieLancamento> serieDAO;

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@EJB
	private CheckingAccountInitialBalanceRemover checkingAccountInitialBalanceRemover;
	
	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerSerie(SerieLancamento serie) {
		List<Lancamento> lancamentosSerie = lancamentoDAO.createNamedQuery(Lancamento.FIND_LANCAMENTO_QUERY)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("serie", serie)
				.setParameter("category", null).getResultList();

		for (Iterator<Lancamento> iterator = lancamentosSerie.iterator(); iterator.hasNext();) {
			Lancamento serieLancamento = (Lancamento) iterator.next();
			remover(serieLancamento);
		}

		serieDAO.remove(serieDAO.contains(serie) ? serie : serieDAO.merge(serie));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Lancamento lancamento) {

		if (((LancamentoContaCorrente) lancamento).isSaldoInicial()) {
			checkingAccountInitialBalanceRemover.execute(lancamento.getAccount());
			((CheckingAccount)lancamento.getAccount()).setSaldoInicial(BigDecimal.ZERO);
			em.merge(lancamento.getAccount());
		} else {
			accountBalanceUpdater.removeSaldos(lancamento);
			lancamentoDAO.remove(lancamentoDAO.contains(lancamento) ? lancamento : lancamentoDAO.merge(lancamento));
		}
	}

	public void setLancamentoDAO(DAO<Lancamento> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}

	public void setAtualizadorSaldoConta(AccountBalanceUpdater accountBalanceUpdater) {
		this.accountBalanceUpdater = accountBalanceUpdater;
	}
	

}
