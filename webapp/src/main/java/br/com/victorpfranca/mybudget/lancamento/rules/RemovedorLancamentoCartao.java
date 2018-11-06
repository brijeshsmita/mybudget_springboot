package br.com.victorpfranca.mybudget.lancamento.rules;

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
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.SerieLancamento;

@Stateless
public class RemovedorLancamentoCartao {

	@EJB
	private DAO<Lancamento> lancamentoDAO;

	@EJB
	private DAO<SerieLancamento> serieLancamentoDAO;

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private RemovedorLancamentoFaturaItem removedorLancamentoFaturaItem;

	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@EJB
	private AtualizadorFaturasCartao atualizadorFaturasCartao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Lancamento lancamento, boolean abaterLancamentoFatura) {

		// removerLancamentoFaturaItem
		List<Lancamento> faturaItens = removedorLancamentoFaturaItem.remover(lancamento);

		// abaterLancamentosFaturas
		if (abaterLancamentoFatura)
			atualizadorFaturasCartao.abater(faturaItens);

		// removerLancamentoCartao
		lancamentoDAO.remove(lancamentoDAO.contains(lancamento) ? lancamento : lancamentoDAO.merge(lancamento));

		// recomporSaldoContas
		if (abaterLancamentoFatura)
			accountBalanceUpdater.removeSaldos(faturaItens,
					((CreditCardAccount) lancamento.getAccount()).getAccountPagamentoFatura());

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerSerie(SerieLancamento serie, boolean abaterLancamentoFatura) {
		List<Lancamento> lancamentos = lancamentoDAO.createNamedQuery(Lancamento.FIND_LANCAMENTO_CARTAO_QUERY)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("serie", serie)
				.setParameter("saldInicial", null).setParameter("account", null).getResultList();
		for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
			Lancamento lancamento = (Lancamento) iterator.next();
			remover(lancamento, abaterLancamentoFatura);
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

	public void setLancamentoDAO(DAO<Lancamento> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}

}
