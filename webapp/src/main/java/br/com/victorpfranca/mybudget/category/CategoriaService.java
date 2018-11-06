package br.com.victorpfranca.mybudget.category;

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
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.rules.RemocaoNaoPermitidaException;
import br.com.victorpfranca.mybudget.orcamento.Orcamento;

@Stateless
public class CategoriaService {

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private CategoryBuilder categoryBuilder;

	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Category find(Integer id) {
		return em.find(Category.class, id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Category> findAll() {
		return em.createNamedQuery(Category.FIND_ALL_QUERY, Category.class).setParameter("inOut", null)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("nome", null)
				.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Category> findReceitas() {
		return em.createNamedQuery(Category.FIND_ALL_QUERY, Category.class).setParameter("inOut", InOut.E)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("nome", null)
				.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Category> findDespesas() {
		return em.createNamedQuery(Category.FIND_ALL_QUERY, Category.class).setParameter("inOut", InOut.S)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("nome", null)
				.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveCategorias(List<Category> categories) throws SameNameException {
		for (Iterator<Category> iterator = categories.iterator(); iterator.hasNext();) {
			Category category = iterator.next();
			categoryBuilder.save(category);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Category save(Category category) throws SameNameException {
		return categoryBuilder.save(category);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Category category) throws RemocaoNaoPermitidaException {
		validarSemLancamentos(category);
		em.createNamedQuery(Orcamento.REMOVE_BY_CATEGORIA_QUERY).setParameter("category", category).executeUpdate();
		em.remove(em.contains(category) ? category : em.merge(category));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Integer id) throws RemocaoNaoPermitidaException {
		remove(find(id));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void validarSemLancamentos(Category category) throws RemocaoNaoPermitidaException {
		List<Lancamento> lancamentosExistentes = em.createNamedQuery(Lancamento.FIND_LANCAMENTO_QUERY, Lancamento.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado())
				.setParameter("category", category).setParameter("serie", null).getResultList();
		if (!lancamentosExistentes.isEmpty())
			throw new RemocaoNaoPermitidaException("crud.categoria.error.lancamentos_nao_podem_ser_removidos");
	}

}
