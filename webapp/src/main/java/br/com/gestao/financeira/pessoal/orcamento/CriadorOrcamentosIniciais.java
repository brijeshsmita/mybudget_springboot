	package br.com.gestao.financeira.pessoal.orcamento;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.ObjectUtils;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.categoria.CategoriaService;
import br.com.gestao.financeira.pessoal.categoria.CriadorCategoria;
import br.com.gestao.financeira.pessoal.categoria.CriadorCategoriasIniciais;
import br.com.gestao.financeira.pessoal.categoria.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.periodo.PeriodoPlanejamento;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@Stateless
public class CriadorOrcamentosIniciais {
	
	@EJB
	private OrcamentoService orcamentoService;
	
	@EJB
	private CategoriaService categoriaService;
	
	@EJB
	private PeriodoPlanejamento periodoPlanejamento;
	
	private List<Categoria> categoriasReceitas;
	
	private List<Categoria> categoriasDespesas;
	
	private List<AnoMes> periodo;
	
	@EJB
	private CriadorCategoria criadorCategoria;

	@Inject
	private EntityManager em;
	
	@EJB
	private CredentialsStore credentialsStore;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute() throws MesmoNomeExistenteException {
		
		categoriasReceitas = findReceitas();
		
		categoriasDespesas = findDespesas();
		
		periodo = periodoPlanejamento.getPeriodoCompleto();

		List<Orcamento> orcamentos = new ArrayList<Orcamento>();
		
		InputStream is = ObjectUtils.firstNonNull(
				CriadorOrcamentosIniciais.class.getResourceAsStream("orcamentos_iniciais_despesas"),
				CriadorOrcamentosIniciais.class.getResourceAsStream("/orcamentos_iniciais_despesas"));

		if(is == null)
			return;
		
		try {
			saveOrcamentos(orcamentos, categoriasDespesas, is);
		} catch (MesmoNomeExistenteException e) {
			e.printStackTrace();
		}

		orcamentos = new ArrayList<Orcamento>();
		is = ObjectUtils.firstNonNull(
				CriadorOrcamentosIniciais.class.getResourceAsStream("orcamentos_iniciais_receitas"),
				CriadorOrcamentosIniciais.class.getResourceAsStream("/orcamentos_iniciais_receitas"));
		
		saveOrcamentos(orcamentos, categoriasReceitas, is);
	}

	private void saveOrcamentos(List<Orcamento> orcamentos, List<Categoria> categorias, InputStream is)
			throws MesmoNomeExistenteException {
		Scanner scanner = new Scanner(is, "UTF-8");
		while (scanner.hasNextLine()) {
			
			String categoriaValor = scanner.nextLine();
			String nomeCategoria = categoriaValor.split(";")[0];
			String valor = categoriaValor.split(";")[1];
			Categoria categoria = findCategoria(categorias, nomeCategoria);
			if(categoria == null)
				continue;
			
			for (Iterator<AnoMes> iterator = periodo.iterator(); iterator.hasNext();) {
				AnoMes anoMes = (AnoMes) iterator.next();

				Orcamento orcamento = new Orcamento();
				orcamento.setCategoria(categoria);
				orcamento.setAno(anoMes.getAno());
				orcamento.setMes(anoMes.getMes());
				orcamento.setValor(new BigDecimal(valor));
				orcamentos.add(orcamento);
			}
		}
		
		orcamentoService.save(orcamentos);
		scanner.close();
	}

	private Categoria findCategoria(List<Categoria> categorias, String nome) {
		for (Iterator<Categoria> iterator = categorias.iterator(); iterator.hasNext();) {
			Categoria categoria = iterator.next();
			if(categoria.getNome().equals(nome)) {
				return categoria;
			}
		}
		return null;
	}
	
	private List<Categoria> findReceitas() {
		return em.createNamedQuery(Categoria.FIND_ALL_QUERY, Categoria.class).setParameter("inOut", InOut.E)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("nome", null)
				.getResultList();
	}

	private List<Categoria> findDespesas() {
		return em.createNamedQuery(Categoria.FIND_ALL_QUERY, Categoria.class).setParameter("inOut", InOut.S)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("nome", null)
				.getResultList();
	}


}
