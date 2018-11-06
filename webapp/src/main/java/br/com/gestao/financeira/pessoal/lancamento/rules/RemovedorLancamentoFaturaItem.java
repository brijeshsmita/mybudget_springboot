package br.com.gestao.financeira.pessoal.lancamento.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.gestao.financeira.pessoal.conta.rules.AtualizadorSaldoConta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.infra.dao.DAO;
import br.com.gestao.financeira.pessoal.infra.dao.QueryParam;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;

@Stateless
public class RemovedorLancamentoFaturaItem {

	@EJB
	private DAO<Lancamento> lancamentoDAO;

	@EJB
	private CredentialsStore credentialsStore;
	@EJB
	private AtualizadorSaldoConta atualizadorSaldoConta;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Lancamento> remover(Lancamento lancamentoCartaoCredito) {
		List<Lancamento> lancamentos = lancamentoDAO.executeQuery(Lancamento.FIND_LANCAMENTO_FATURA_CARTAO_ITEM_QUERY,
				new QueryParam("lancamentoCartao", lancamentoCartaoCredito), new QueryParam("ano", null), new QueryParam("mes", null),
				new QueryParam("usuario", credentialsStore.recuperarIdUsuarioLogado()), new QueryParam("conta", null),
				new QueryParam("categoria", null));

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
