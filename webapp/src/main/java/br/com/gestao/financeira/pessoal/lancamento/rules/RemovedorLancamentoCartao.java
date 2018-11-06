package br.com.gestao.financeira.pessoal.lancamento.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.conta.rules.AtualizadorSaldoConta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.infra.dao.DAO;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.SerieLancamento;

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
	private AtualizadorSaldoConta atualizadorSaldoConta;

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
			atualizadorSaldoConta.removeSaldos(faturaItens,
					((ContaCartao) lancamento.getConta()).getContaPagamentoFatura());

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerSerie(SerieLancamento serie, boolean abaterLancamentoFatura) {
		List<Lancamento> lancamentos = lancamentoDAO.createNamedQuery(Lancamento.FIND_LANCAMENTO_CARTAO_QUERY)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("serie", serie)
				.setParameter("saldInicial", null).setParameter("conta", null).getResultList();
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

	public void setAtualizadorSaldoConta(AtualizadorSaldoConta atualizadorSaldoConta) {
		this.atualizadorSaldoConta = atualizadorSaldoConta;
	}

	public void setLancamentoDAO(DAO<Lancamento> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}

}
