package br.com.gestao.financeira.pessoal.lancamento.rules;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.conta.ContaCorrente;
import br.com.gestao.financeira.pessoal.conta.rules.AtualizadorSaldoConta;
import br.com.gestao.financeira.pessoal.conta.rules.RemovedorSaldoInicialContaCorrente;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.infra.dao.DAO;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoContaCorrente;
import br.com.gestao.financeira.pessoal.lancamento.SerieLancamento;

@Stateless
public class RemovedorLancamentoContaCorrente {

	@EJB
	private DAO<Lancamento> lancamentoDAO;

	public DAO<Lancamento> getLancamentoDAO() {
		return lancamentoDAO;
	}

	public AtualizadorSaldoConta getAtualizadorSaldoConta() {
		return atualizadorSaldoConta;
	}

	@EJB
	private DAO<SerieLancamento> serieDAO;

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AtualizadorSaldoConta atualizadorSaldoConta;

	@EJB
	private RemovedorSaldoInicialContaCorrente removedorSaldoInicialContaCorrente;
	
	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerSerie(SerieLancamento serie) {
		List<Lancamento> lancamentosSerie = lancamentoDAO.createNamedQuery(Lancamento.FIND_LANCAMENTO_QUERY)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("serie", serie)
				.setParameter("categoria", null).getResultList();

		for (Iterator<Lancamento> iterator = lancamentosSerie.iterator(); iterator.hasNext();) {
			Lancamento serieLancamento = (Lancamento) iterator.next();
			remover(serieLancamento);
		}

		serieDAO.remove(serieDAO.contains(serie) ? serie : serieDAO.merge(serie));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Lancamento lancamento) {

		if (((LancamentoContaCorrente) lancamento).isSaldoInicial()) {
			removedorSaldoInicialContaCorrente.execute(lancamento.getConta());
			((ContaCorrente)lancamento.getConta()).setSaldoInicial(BigDecimal.ZERO);
			em.merge(lancamento.getConta());
		} else {
			atualizadorSaldoConta.removeSaldos(lancamento);
			lancamentoDAO.remove(lancamentoDAO.contains(lancamento) ? lancamento : lancamentoDAO.merge(lancamento));
		}
	}

	public void setLancamentoDAO(DAO<Lancamento> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}

	public void setAtualizadorSaldoConta(AtualizadorSaldoConta atualizadorSaldoConta) {
		this.atualizadorSaldoConta = atualizadorSaldoConta;
	}
	

}
