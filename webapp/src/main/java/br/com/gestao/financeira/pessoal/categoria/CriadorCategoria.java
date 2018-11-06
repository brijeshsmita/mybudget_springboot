package br.com.gestao.financeira.pessoal.categoria;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.infra.dao.DAO;
import br.com.gestao.financeira.pessoal.orcamento.Orcamento;

@Stateless
public class CriadorCategoria {

	@EJB
	DAO<Categoria> categoriaDao;

	@EJB
	DAO<Orcamento> orcamentoDao;

	@EJB
	private CredentialsStore credentialsStore;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Categoria save(Categoria categoria) throws MesmoNomeExistenteException {
		validarNomeExistente(categoria);

		categoria = categoriaDao.merge(categoria);

		return categoria;
	}

	protected void validarNomeExistente(Categoria categoria) throws MesmoNomeExistenteException {

		List<Categoria> categorias = categoriaDao.createNamedQuery(Categoria.FIND_ALL_QUERY)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado())
				.setParameter("nome", categoria.getNome()).setParameter("inOut", null).getResultList();

		if (!categorias.isEmpty() && categorias.get(0).getId() != categoria.getId())
			throw new MesmoNomeExistenteException("crud.categoria.error.nome_existente");

	}

	public void setCategoriaDao(DAO<Categoria> categoriaDao) {
		this.categoriaDao = categoriaDao;
	}

}
