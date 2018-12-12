package br.com.victorpfranca.mybudget.budget.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.budget.Budget;
import br.com.victorpfranca.mybudget.budget.BudgetService;
import br.com.victorpfranca.mybudget.category.CategoriaService;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.period.PlanningPeriod;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Named
@ViewScoped
public class BudgetViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BudgetService budgetService;

	@Inject
	private CategoriaService categoriaService;

	@Inject
	private BudgetCreatorDataGrid budgetCreatorDataGrid;

	@Inject
	private PlanningPeriod planningPeriod;

	private Map<Category, Map<MonthYear, Budget>> orcamentosGridData;

	protected List<Category> categories;

	private List<MonthYear> anosMeses;

	private int selectedTab;

	private Integer filtroAno = MonthYear.getCurrent().getAno();

	private InOut filtroInOut = InOut.S;

	private Budget objetoSelecionado;
	private Budget objeto;
	private boolean telaGrid = true;

	@PostConstruct
	public void init() {
		setSelectedTab(0);

		initData();
	}

	private void initData() {
		initCategorias();

		initPeriodo();
		initOrcamentos();
	}

	private void initCategorias() {
		categories = new ArrayList<Category>();
		if (filtroInOut.equals(InOut.E))
			categories = categoriaService.findReceitas();
		else
			categories = categoriaService.findDespesas();
	}

	private void initOrcamentos() {
		orcamentosGridData = budgetCreatorDataGrid.criar(categories, anosMeses, carregarOrcamentosExistentes());
	}

	private void initPeriodo() {
		anosMeses = planningPeriod.getPeriodoCompleto();
	}

	public void incluir() {
		setSelectedTab(0);
		setTelaGrid(false);
		Budget budget = new Budget();
		setObjeto(budget);
	}

	public void alterar() {
		setSelectedTab(0);
		setTelaGrid(false);
		setObjeto(getObjetoSelecionado());
		setObjetoSelecionado(null);
		initOrcamentos();
	}

	public void voltar() {
		setTelaGrid(true);
		setSelectedTab(0);
		setObjeto(null);
		setObjetoSelecionado(null);
	}

	public void salvar() {
		Budget budget = budgetService.save(getObjeto());
		setObjeto(budget);
		initOrcamentos();
	}

	public void onCellEdit(CellEditEvent event) {

		DataTable o = (DataTable) event.getSource();
		Map.Entry<Category, Map<MonthYear, Budget>> categoriaMapEntry = (Map.Entry<Category, Map<MonthYear, Budget>>) o
				.getRowData();
		Map<MonthYear, Budget> orcamentoMap = categoriaMapEntry.getValue();

		String columnKey = event.getColumn().getColumnKey();
		int index = Integer.valueOf(columnKey.substring(columnKey.length() - 2).replaceAll(":", ""));
		MonthYear monthYear = (MonthYear) getMeses().toArray()[index];

		Budget budget = orcamentoMap.get(monthYear);
		setObjeto((budget));
		salvar();
	}

	private List<Budget> carregarOrcamentosExistentes() {
		// carregar orçamentos existentes
		List<Budget> budgets = null;
		if (filtroInOut.equals(InOut.E)) {
			budgets = findOrcamentosReceitas();
		} else
			budgets = findOrcamentosDespesas();
		return budgets;
	}

	public boolean isTelaGrid() {
		return telaGrid;
	}

	public void setTelaGrid(boolean telaGrid) {
		this.telaGrid = telaGrid;
	}

	public Budget getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Budget objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	public Budget getObjeto() {
		return objeto;
	}

	public void setObjeto(Budget objeto) {
		this.objeto = objeto;
	}

	private List<Budget> findOrcamentosDespesas() {
		return budgetService.findOrcamentosDespesas(filtroAno.intValue());
	}

	private List<Budget> findOrcamentosReceitas() {
		return budgetService.findOrcamentosReceitas(filtroAno.intValue());
	}

	public void onFilterChangeListener() {
		initData();
	}

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}

	public InOut getFiltroInOut() {
		return filtroInOut;
	}

	public Integer getFiltroAno() {
		return filtroAno;
	}

	public void setFiltroAno(Integer filtroAno) {
		this.filtroAno = filtroAno;
	}

	public void setFiltroInOut(InOut filtroInOut) {
		this.filtroInOut = filtroInOut;
	}

	public Map<Category, Map<MonthYear, Budget>> getOrcamentos() {
		return orcamentosGridData;
	}

	public List<MonthYear> getMeses() {
		List<MonthYear> returnList = new ArrayList<MonthYear>();
		for (Iterator<MonthYear> iterator = anosMeses.iterator(); iterator.hasNext();) {
			MonthYear monthYear = iterator.next();
			if (monthYear.getAno() == filtroAno.intValue())
				returnList.add(monthYear);
		}

		return returnList;
	}

	public List<Integer> getAnosList() {
		List<Integer> anos = new ArrayList<Integer>();
		for (Iterator<MonthYear> iterator = anosMeses.iterator(); iterator.hasNext();) {
			MonthYear monthYear = iterator.next();
			if (!anos.contains(monthYear.getAno()))
				anos.add(monthYear.getAno());
		}
		return anos;
	}

	public BigDecimal getTotal(Map<MonthYear, Budget> budget) {
		return budget.values().stream().map(e -> e.getValor()).reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
	}

}
