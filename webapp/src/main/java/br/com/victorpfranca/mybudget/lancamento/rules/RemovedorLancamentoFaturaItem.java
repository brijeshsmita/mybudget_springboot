package br.com.victorpfranca.mybudget.lancamento.rules;

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
import br.com.victorpfranca.mybudget.lancamento.Lancamento;

@Stateless
public class RemovedorLancamentoFaturaItem {

	@EJB
	private DAO<Lancamento> lancamentoDAO;

	@EJB
	private CredentialsStore credentialsStore;
	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Lancamento> remover(Lancamento lancamentoCartaoCredito) {
		List<Lancamento> lancamentos = lancamentoDAO.executeQuery(Lancamento.FIND_LANCAMENTO_FATURA_CARTAO_ITEM_QUERY,
				new QueryParam("lancamentoCartao", lancamentoCartaoCredito), new QueryParam("ano", null), new QueryParam("mes", null),
				new QueryParam("user", credentialsStore.recuperarIdUsuarioLogado()), new QueryParam("account", null),
				new QueryParam("category", null));

		for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
			Lancamento lancamento = (Lancamento) iterator.next();
			lancamentoDAO.remove(lancamento);
		}

		return lancamentos;
	}
	
	public void setCredentialsStore(CredentialsStore credentialsStore) {
		this.credentialsStore = credentialsStore;
	}
	
	public void setLancamentoDAO(DAO<Lancamento> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}

}
