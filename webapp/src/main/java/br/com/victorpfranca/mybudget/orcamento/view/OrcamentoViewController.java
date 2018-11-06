package br.com.victorpfranca.mybudget.orcamento.view;

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
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.category.CategoriaService;
import br.com.victorpfranca.mybudget.orcamento.Orcamento;
import br.com.victorpfranca.mybudget.orcamento.OrcamentoService;
import br.com.victorpfranca.mybudget.periodo.PeriodoPlanejamento;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Named
@ViewScoped
public class OrcamentoViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private OrcamentoService orcamentoService;

	@Inject
	private CategoriaService categoriaService;

	@Inject
	private CriadorOrcamentoDataGrid criadorOrcamentoDataGrid;

	@Inject
	private PeriodoPlanejamento periodoPlanejamento;

	private Map<Category, Map<MonthYear, Orcamento>> orcamentosGridData;

	protected List<Category> categories;

	private List<MonthYear> anosMeses;

	private int selectedTab;

	private Integer filtroAno = MonthYear.getCurrent().getAno();

	private InOut filtroInOut = InOut.S;

	private Orcamento objetoSelecionado;
	private Orcamento objeto;
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
		orcamentosGridData = criadorOrcamentoDataGrid.criar(categories, anosMeses, carregarOrcamentosExistentes());
	}

	private void initPeriodo() {
		anosMeses = periodoPlanejamento.getPeriodoCompleto();
	}

	public void incluir() {
		setSelectedTab(0);
		setTelaGrid(false);
		Orcamento orcamento = new Orcamento();
		setObjeto(orcamento);
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
		Orcamento orcamento = orcamentoService.save(getObjeto());
		setObjeto(orcamento);
		initOrcamentos();
	}

	public void onCellEdit(CellEditEvent event) {

		DataTable o = (DataTable) event.getSource();
		Map.Entry<Category, Map<MonthYear, Orcamento>> categoriaMapEntry = (Map.Entry<Category, Map<MonthYear, Orcamento>>) o
				.getRowData();
		Map<MonthYear, Orcamento> orcamentoMap = categoriaMapEntry.getValue();

		String columnKey = event.getColumn().getColumnKey();
		int index = Integer.valueOf(columnKey.substring(columnKey.length() - 2).replaceAll(":", ""));
		MonthYear monthYear = (MonthYear) getMeses().toArray()[index];

		Orcamento orcamento = orcamentoMap.get(monthYear);
		setObjeto((orcamento));
		salvar();
	}

	private List<Orcamento> carregarOrcamentosExistentes() {
		// carregar orçamentos existentes
		List<Orcamento> orcamentos = null;
		if (filtroInOut.equals(InOut.E)) {
			orcamentos = findOrcamentosReceitas();
		} else
			orcamentos = findOrcamentosDespesas();
		return orcamentos;
	}

	public boolean isTelaGrid() {
		return telaGrid;
	}

	public void setTelaGrid(boolean telaGrid) {
		this.telaGrid = telaGrid;
	}

	public Orcamento getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Orcamento objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	public Orcamento getObjeto() {
		return objeto;
	}

	public void setObjeto(Orcamento objeto) {
		this.objeto = objeto;
	}

	private List<Orcamento> findOrcamentosDespesas() {
		return orcamentoService.findOrcamentosDespesas(filtroAno.intValue());
	}

	private List<Orcamento> findOrcamentosReceitas() {
		return orcamentoService.findOrcamentosReceitas(filtroAno.intValue());
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

	public Map<Category, Map<MonthYear, Orcamento>> getOrcamentos() {
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

	public BigDecimal getTotal(Map<MonthYear, Orcamento> orcamento) {
		return orcamento.values().stream().map(e -> e.getValor()).reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
	}

}
