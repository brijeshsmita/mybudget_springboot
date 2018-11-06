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
import br.com.victorpfranca.mybudget.view.AnoMes;

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

	private Map<Category, Map<AnoMes, Orcamento>> orcamentosGridData;

	protected List<Category> categories;

	private List<AnoMes> anosMeses;

	private int selectedTab;

	private Integer filtroAno = AnoMes.getCurrent().getAno();

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
		Map.Entry<Category, Map<AnoMes, Orcamento>> categoriaMapEntry = (Map.Entry<Category, Map<AnoMes, Orcamento>>) o
				.getRowData();
		Map<AnoMes, Orcamento> orcamentoMap = categoriaMapEntry.getValue();

		String columnKey = event.getColumn().getColumnKey();
		int index = Integer.valueOf(columnKey.substring(columnKey.length() - 2).replaceAll(":", ""));
		AnoMes anoMes = (AnoMes) getMeses().toArray()[index];

		Orcamento orcamento = orcamentoMap.get(anoMes);
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

	public Map<Category, Map<AnoMes, Orcamento>> getOrcamentos() {
		return orcamentosGridData;
	}

	public List<AnoMes> getMeses() {
		List<AnoMes> returnList = new ArrayList<AnoMes>();
		for (Iterator<AnoMes> iterator = anosMeses.iterator(); iterator.hasNext();) {
			AnoMes anoMes = iterator.next();
			if (anoMes.getAno() == filtroAno.intValue())
				returnList.add(anoMes);
		}

		return returnList;
	}

	public List<Integer> getAnosList() {
		List<Integer> anos = new ArrayList<Integer>();
		for (Iterator<AnoMes> iterator = anosMeses.iterator(); iterator.hasNext();) {
			AnoMes anoMes = iterator.next();
			if (!anos.contains(anoMes.getAno()))
				anos.add(anoMes.getAno());
		}
		return anos;
	}

	public BigDecimal getTotal(Map<AnoMes, Orcamento> orcamento) {
		return orcamento.values().stream().map(e -> e.getValor()).reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
	}

}
