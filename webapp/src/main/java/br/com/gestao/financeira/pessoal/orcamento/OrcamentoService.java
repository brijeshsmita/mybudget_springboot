package br.com.gestao.financeira.pessoal.orcamento;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.infra.dao.DAO;

@Stateless
public class OrcamentoService {

	@Inject
	private EntityManager em;

	@EJB
	private DAO<Orcamento> dao;

	@EJB
	private CredentialsStore credentialsStore;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Orcamento orcamento) {
		dao.remove(orcamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void save(List<Orcamento> orcamentos) {
		for (Iterator<Orcamento> iterator = orcamentos.iterator(); iterator.hasNext();) {
			Orcamento orcamento = iterator.next();
			save(orcamento);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Orcamento save(Orcamento orcamento){
		orcamento = dao.merge(orcamento);

		return orcamento;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getSaldoReceitaOrcada(int year, int month) {
		List<OrcadoRealMes> orcadoRealMes = em
				.createNamedQuery(OrcadoRealMes.FIND_BY_RECEITA_MONTH, OrcadoRealMes.class).setParameter("ano", year)
				.setParameter("mes", month).setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado())
				.getResultList();

		if (orcadoRealMes.isEmpty())
			return BigDecimal.ZERO;

		return orcadoRealMes.get(0).getSaldo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getSaldoReceitaOrcadaAcumulado(int year, int month) {

		List<SaldoOrcadoAcumuladoMes> saldoAcumulado = em
				.createNamedQuery(SaldoOrcadoAcumuladoMes.FIND_BY_RECEITA_UNTIL_MONTH, SaldoOrcadoAcumuladoMes.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", year)
				.setParameter("mes", month).setMaxResults(1).getResultList();

		if (saldoAcumulado.isEmpty())
			return BigDecimal.ZERO;

		return saldoAcumulado.get(0).getSaldo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getSaldoDespesaOrcada(int year, int month) {
		List<OrcadoRealMes> orcadoRealMes = em
				.createNamedQuery(OrcadoRealMes.FIND_BY_DESPESA_MONTH, OrcadoRealMes.class).setParameter("ano", year)
				.setParameter("mes", month).setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado())
				.getResultList();

		if (orcadoRealMes.isEmpty())
			return BigDecimal.ZERO;

		return orcadoRealMes.get(0).getSaldo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getSaldoDespesaOrcadaAcumulado(int year, int month) {

		List<SaldoOrcadoAcumuladoMes> saldoAcumulado = em
				.createNamedQuery(SaldoOrcadoAcumuladoMes.FIND_BY_DESPESA_UNTIL_MONTH, SaldoOrcadoAcumuladoMes.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", year)
				.setParameter("mes", month).setMaxResults(1).getResultList();

		if (saldoAcumulado.isEmpty())
			return BigDecimal.ZERO;

		return saldoAcumulado.get(0).getSaldo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OrcadoRealMesCategoria> getReceitasCategoriaOrcada(int year, int month) {
		return em.createNamedQuery(OrcadoRealMesCategoria.FIND_BY_RECEITA_MONTH, OrcadoRealMesCategoria.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", year)
				.setParameter("mes", month).getResultList();

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OrcadoRealMesCategoria> getDespesasCategoriaOrcada(int year, int month) {
		return em.createNamedQuery(OrcadoRealMesCategoria.FIND_BY_DESPESA_MONTH, OrcadoRealMesCategoria.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", year)
				.setParameter("mes", month).getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Orcamento> findOrcamentosReceitas(int ano) {
		return em.createNamedQuery(Orcamento.FIND_BY_RECEITA_DESPESA_QUERY, Orcamento.class).setParameter("ano", ano)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("inOut", InOut.E)
				.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Orcamento> findOrcamentosDespesas(int ano) {
		return em.createNamedQuery(Orcamento.FIND_BY_RECEITA_DESPESA_QUERY, Orcamento.class).setParameter("ano", ano)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("inOut", InOut.S)
				.getResultList();
	}

}
