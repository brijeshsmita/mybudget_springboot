package br.com.victorpfranca.mybudget.budget;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.infra.dao.DAO;

@Stateless
public class BudgetService {

	@Inject
	private EntityManager em;

	@EJB
	private DAO<Budget> dao;

	@EJB
	private CredentialsStore credentialsStore;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Budget budget) {
		dao.remove(budget);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void save(List<Budget> budgets) {
		for (Iterator<Budget> iterator = budgets.iterator(); iterator.hasNext();) {
			Budget budget = iterator.next();
			save(budget);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Budget save(Budget budget){
		budget = dao.merge(budget);

		return budget;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getSaldoReceitaOrcada(int year, int month) {
		List<MonthBudgetReal> monthBudgetReal = em
				.createNamedQuery(MonthBudgetReal.FIND_BY_RECEITA_MONTH, MonthBudgetReal.class).setParameter("ano", year)
				.setParameter("mes", month).setParameter("user", credentialsStore.recuperarIdUsuarioLogado())
				.getResultList();

		if (monthBudgetReal.isEmpty())
			return BigDecimal.ZERO;

		return monthBudgetReal.get(0).getSaldo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getSaldoReceitaOrcadaAcumulado(int year, int month) {

		List<MonthAccumulatedBudgetedBalance> saldoAcumulado = em
				.createNamedQuery(MonthAccumulatedBudgetedBalance.FIND_BY_RECEITA_UNTIL_MONTH, MonthAccumulatedBudgetedBalance.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", year)
				.setParameter("mes", month).setMaxResults(1).getResultList();

		if (saldoAcumulado.isEmpty())
			return BigDecimal.ZERO;

		return saldoAcumulado.get(0).getSaldo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getSaldoDespesaOrcada(int year, int month) {
		List<MonthBudgetReal> monthBudgetReal = em
				.createNamedQuery(MonthBudgetReal.FIND_BY_DESPESA_MONTH, MonthBudgetReal.class).setParameter("ano", year)
				.setParameter("mes", month).setParameter("user", credentialsStore.recuperarIdUsuarioLogado())
				.getResultList();

		if (monthBudgetReal.isEmpty())
			return BigDecimal.ZERO;

		return monthBudgetReal.get(0).getSaldo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BigDecimal getSaldoDespesaOrcadaAcumulado(int year, int month) {

		List<MonthAccumulatedBudgetedBalance> saldoAcumulado = em
				.createNamedQuery(MonthAccumulatedBudgetedBalance.FIND_BY_DESPESA_UNTIL_MONTH, MonthAccumulatedBudgetedBalance.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", year)
				.setParameter("mes", month).setMaxResults(1).getResultList();

		if (saldoAcumulado.isEmpty())
			return BigDecimal.ZERO;

		return saldoAcumulado.get(0).getSaldo();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MonthCategoryBudgetReal> getReceitasCategoriaOrcada(int year, int month) {
		return em.createNamedQuery(MonthCategoryBudgetReal.FIND_BY_RECEITA_MONTH, MonthCategoryBudgetReal.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", year)
				.setParameter("mes", month).getResultList();

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MonthCategoryBudgetReal> getDespesasCategoriaOrcada(int year, int month) {
		return em.createNamedQuery(MonthCategoryBudgetReal.FIND_BY_DESPESA_MONTH, MonthCategoryBudgetReal.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", year)
				.setParameter("mes", month).getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Budget> findOrcamentosReceitas(int ano) {
		return em.createNamedQuery(Budget.FIND_BY_RECEITA_DESPESA_QUERY, Budget.class).setParameter("ano", ano)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("inOut", InOut.E)
				.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Budget> findOrcamentosDespesas(int ano) {
		return em.createNamedQuery(Budget.FIND_BY_RECEITA_DESPESA_QUERY, Budget.class).setParameter("ano", ano)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("inOut", InOut.S)
				.getResultList();
	}

}
