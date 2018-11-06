package br.com.victorpfranca.mybudget.category;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.orcamento.Orcamento;

@Stateless
public class CategoryBuilder {

	@EJB
	DAO<Category> categoriaDao;

	@EJB
	DAO<Orcamento> orcamentoDao;

	@EJB
	private CredentialsStore credentialsStore;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Category save(Category category) throws SameNameException {
		validarNomeExistente(category);

		category = categoriaDao.merge(category);

		return category;
	}

	protected void validarNomeExistente(Category category) throws SameNameException {

		List<Category> categories = categoriaDao.createNamedQuery(Category.FIND_ALL_QUERY)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado())
				.setParameter("nome", category.getNome()).setParameter("inOut", null).getResultList();

		if (!categories.isEmpty() && categories.get(0).getId() != category.getId())
			throw new SameNameException("crud.categoria.error.nome_existente");

	}

	public void setCategoryDao(DAO<Category> categoriaDao) {
		this.categoriaDao = categoriaDao;
	}

}
