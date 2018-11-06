	package br.com.victorpfranca.mybudget.orcamento;

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

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.category.CategoriaService;
import br.com.victorpfranca.mybudget.category.CategoryBuilder;
import br.com.victorpfranca.mybudget.category.InitialCategoriesBuilder;
import br.com.victorpfranca.mybudget.category.SameNameException;
import br.com.victorpfranca.mybudget.periodo.PeriodoPlanejamento;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Stateless
public class CriadorOrcamentosIniciais {
	
	@EJB
	private OrcamentoService orcamentoService;
	
	@EJB
	private CategoriaService categoriaService;
	
	@EJB
	private PeriodoPlanejamento periodoPlanejamento;
	
	private List<Category> categoriasReceitas;
	
	private List<Category> categoriasDespesas;
	
	private List<MonthYear> periodo;
	
	@EJB
	private CategoryBuilder categoryBuilder;

	@Inject
	private EntityManager em;
	
	@EJB
	private CredentialsStore credentialsStore;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute() throws SameNameException {
		
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
		} catch (SameNameException e) {
			e.printStackTrace();
		}

		orcamentos = new ArrayList<Orcamento>();
		is = ObjectUtils.firstNonNull(
				CriadorOrcamentosIniciais.class.getResourceAsStream("orcamentos_iniciais_receitas"),
				CriadorOrcamentosIniciais.class.getResourceAsStream("/orcamentos_iniciais_receitas"));
		
		saveOrcamentos(orcamentos, categoriasReceitas, is);
	}

	private void saveOrcamentos(List<Orcamento> orcamentos, List<Category> categories, InputStream is)
			throws SameNameException {
		Scanner scanner = new Scanner(is, "UTF-8");
		while (scanner.hasNextLine()) {
			
			String categoriaValor = scanner.nextLine();
			String nomeCategoria = categoriaValor.split(";")[0];
			String valor = categoriaValor.split(";")[1];
			Category category = findCategoria(categories, nomeCategoria);
			if(category == null)
				continue;
			
			for (Iterator<MonthYear> iterator = periodo.iterator(); iterator.hasNext();) {
				MonthYear monthYear = (MonthYear) iterator.next();

				Orcamento orcamento = new Orcamento();
				orcamento.setCategory(category);
				orcamento.setAno(monthYear.getAno());
				orcamento.setMes(monthYear.getMes());
				orcamento.setValor(new BigDecimal(valor));
				orcamentos.add(orcamento);
			}
		}
		
		orcamentoService.save(orcamentos);
		scanner.close();
	}

	private Category findCategoria(List<Category> categories, String nome) {
		for (Iterator<Category> iterator = categories.iterator(); iterator.hasNext();) {
			Category category = iterator.next();
			if(category.getNome().equals(nome)) {
				return category;
			}
		}
		return null;
	}
	
	private List<Category> findReceitas() {
		return em.createNamedQuery(Category.FIND_ALL_QUERY, Category.class).setParameter("inOut", InOut.E)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("nome", null)
				.getResultList();
	}

	private List<Category> findDespesas() {
		return em.createNamedQuery(Category.FIND_ALL_QUERY, Category.class).setParameter("inOut", InOut.S)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("nome", null)
				.getResultList();
	}


}
