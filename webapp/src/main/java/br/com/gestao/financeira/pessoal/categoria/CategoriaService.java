package br.com.gestao.financeira.pessoal.categoria;

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
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemocaoNaoPermitidaException;
import br.com.gestao.financeira.pessoal.orcamento.Orcamento;

@Stateless
public class CategoriaService {

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private CriadorCategoria criadorCategoria;

	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Categoria find(Integer id) {
		return em.find(Categoria.class, id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Categoria> findAll() {
		return em.createNamedQuery(Categoria.FIND_ALL_QUERY, Categoria.class).setParameter("inOut", null)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("nome", null)
				.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Categoria> findReceitas() {
		return em.createNamedQuery(Categoria.FIND_ALL_QUERY, Categoria.class).setParameter("inOut", InOut.E)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("nome", null)
				.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Categoria> findDespesas() {
		return em.createNamedQuery(Categoria.FIND_ALL_QUERY, Categoria.class).setParameter("inOut", InOut.S)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("nome", null)
				.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveCategorias(List<Categoria> categorias) throws MesmoNomeExistenteException {
		for (Iterator<Categoria> iterator = categorias.iterator(); iterator.hasNext();) {
			Categoria categoria = iterator.next();
			criadorCategoria.save(categoria);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Categoria save(Categoria categoria) throws MesmoNomeExistenteException {
		return criadorCategoria.save(categoria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Categoria categoria) throws RemocaoNaoPermitidaException {
		validarSemLancamentos(categoria);
		em.createNamedQuery(Orcamento.REMOVE_BY_CATEGORIA_QUERY).setParameter("categoria", categoria).executeUpdate();
		em.remove(em.contains(categoria) ? categoria : em.merge(categoria));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Integer id) throws RemocaoNaoPermitidaException {
		remove(find(id));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void validarSemLancamentos(Categoria categoria) throws RemocaoNaoPermitidaException {
		List<Lancamento> lancamentosExistentes = em.createNamedQuery(Lancamento.FIND_LANCAMENTO_QUERY, Lancamento.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado())
				.setParameter("categoria", categoria).setParameter("serie", null).getResultList();
		if (!lancamentosExistentes.isEmpty())
			throw new RemocaoNaoPermitidaException("crud.categoria.error.lancamentos_nao_podem_ser_removidos");
	}

}
