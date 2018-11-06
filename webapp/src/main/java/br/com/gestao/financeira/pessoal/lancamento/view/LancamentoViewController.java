package br.com.gestao.financeira.pessoal.lancamento.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.categoria.CategoriaService;
import br.com.gestao.financeira.pessoal.conta.ConsultaSaldos;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.conta.rules.ContaService;
import br.com.gestao.financeira.pessoal.lancamento.ConsultaLancamentos;
import br.com.gestao.financeira.pessoal.lancamento.FiltrosLancamentos;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoContaCorrente;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoStatus;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoVO;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.DataSerieLancamentoInvalidaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.LancamentoService;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;
import br.com.gestao.financeira.pessoal.orcamento.ConsultaOrcamentos;
import br.com.gestao.financeira.pessoal.orcamento.OrcadoRealMesCategoria;
import br.com.gestao.financeira.pessoal.periodo.PeriodoPlanejamento;
import br.com.gestao.financeira.pessoal.view.AnoMes;
import br.com.gestao.financeira.pessoal.view.FacesMessages;
import br.com.gestao.financeira.pessoal.view.Messages;

@Named
@ViewScoped
public class LancamentoViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private LancamentoService lancamentoService;

	@Inject
	private ContaService contaService;

	@Inject
	private CategoriaService categoriaService;

	@Inject
	private ConsultaLancamentos consultaLancamentos;
	@Inject
	private ConsultaSaldos consultaSaldos;
	@Inject
	private ConsultaOrcamentos consultaOrcamentos;

	@Inject
	private PeriodoPlanejamento periodoPlanejamento;

	private int selectedTab;
	private LancamentoVO lancamentoVO;

	private boolean telaGrid = true;
	private List<Lancamento> lancamentos;

	private BigDecimal saldoInicial;
	private BigDecimal saldoCorrentePrevisto;
	private BigDecimal saldoSaldoReceitaOrcada;
	private BigDecimal saldoSaldoDespesaOrcada;
	private BigDecimal saldoFinalPrevisto;

	private List<OrcadoRealMesCategoria> receitaOrcadoRealMesCategoria;
	private List<OrcadoRealMesCategoria> despesaOrcadoRealMesCategoria;

	private AnoMes filtroMes = AnoMes.getCurrent();

	private Conta filtroConta = null;

	private Categoria filtroCategoria = null;

	private char filtroStatus = 0;

	private boolean tratarEmSerie = true;

	@PostConstruct
	public void init() {
		setSelectedTab(0);
		carregarDadosTelaListagem();
	}

	public void carregarDadosTelaListagem() {
		FiltrosLancamentos filtrosLancamentos = new FiltrosLancamentos(getFiltroMes(),
				Optional.ofNullable(getFiltroCategoria()).map(Categoria::getId).orElse(null),
				Optional.ofNullable(getFiltroConta()).map(Conta::getId).orElse(null),
				Optional.ofNullable(LancamentoStatus.fromChar(getFiltroStatus())).orElse(null));
		this.lancamentos = consultaLancamentos.lancamentos(filtrosLancamentos);
		this.saldoInicial = consultaSaldos.recuperarSaldoInicial(filtrosLancamentos);
		this.saldoCorrentePrevisto = consultaSaldos.recuperarSaldoCorrentePrevisto(filtrosLancamentos);
		this.saldoSaldoReceitaOrcada = consultaSaldos.recuperarSaldoReceitaOrcada(getFiltroMes());
		this.saldoSaldoDespesaOrcada = consultaSaldos.recuperarSaldoDespesaOrcada(getFiltroMes());
		this.saldoFinalPrevisto = consultaSaldos.recuperarSaldoFinalPrevisto(filtrosLancamentos);
		this.despesaOrcadoRealMesCategoria = consultaOrcamentos.recuperarDespesasPorCategoriaOrcada(getFiltroMes());
		this.receitaOrcadoRealMesCategoria = consultaOrcamentos.recuperarReceitasPorCategoriaOrcada(getFiltroMes());
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void realizarTransferencia() {
		setTratarEmSerie(true);
		setSelectedTab(0);
		setTelaGrid(false);
		LancamentoVO lancamento = new LancamentoVO(InOut.S, LancamentoStatus.NAO_CONFIRMADO);
		lancamento.setTransferencia(true);
		setLancamentoVO(lancamento);
	}

	public void incluirReceita() {
		setTratarEmSerie(true);
		setSelectedTab(0);
		setTelaGrid(false);
		LancamentoVO lancamento = new LancamentoVO(InOut.E, LancamentoStatus.NAO_CONFIRMADO);
		setLancamentoVO(lancamento);
	}

	public void incluirDespesa() {
		setTratarEmSerie(true);
		setSelectedTab(0);
		setTelaGrid(false);
		LancamentoVO lancamento = new LancamentoVO(InOut.S, LancamentoStatus.NAO_CONFIRMADO);
		setLancamentoVO(lancamento);
	}

	public void incluirAjuste() {
		setTratarEmSerie(true);
		setSelectedTab(0);
		setTelaGrid(false);
		LancamentoVO lancamento = new LancamentoVO(InOut.S, LancamentoStatus.NAO_CONFIRMADO);
		lancamento.setAjuste(true);
		setLancamentoVO(lancamento);
	}

	public void confirmar(Lancamento lancamento) {
		try {
			lancamento = lancamentoService.confirmar(lancamento);
			setLancamentoVO(lancamento.getVO());
		} catch (ContaNotNullException | CategoriasIncompativeisException | MesLancamentoAlteradoException
				| TipoContaException | ValorLancamentoInvalidoException e) {
			FacesMessages.fatal(Messages.msg(e.getMessage()));
			return;
		}
	}

	public void alterar(Lancamento lancamento) {
		setTratarEmSerie(false);
		setSelectedTab(0);
		setTelaGrid(false);
		setLancamentoVO(lancamento.getVO());
	}

	public void alterarSerie(Lancamento lancamento) {
		setTratarEmSerie(true);

		setSelectedTab(0);
		setTelaGrid(false);

		LancamentoVO vo = lancamento.getVO();
		vo.setRepeteLancamento(true);

		setLancamentoVO(vo);

	}

	public void voltar() {
		setTelaGrid(true);
		setSelectedTab(0);
		setLancamentoVO(null);
	}

	public boolean isTelaGrid() {
		return telaGrid;
	}

	public void setTelaGrid(boolean telaGrid) {
		this.telaGrid = telaGrid;
	}

	public LancamentoVO getLancamentoVO() {
		return lancamentoVO;
	}

	public void setLancamentoVO(LancamentoVO lancamento) {
		this.lancamentoVO = lancamento;
	}

	public void excluir(Lancamento lancamento) {
		if (lancamento != null) {
			lancamentoService.remove(lancamento);
			carregarDadosTelaListagem();
		}
	}

	public void excluirSerie(Lancamento lancamento) {
		if (lancamento != null) {
			lancamentoService.removeSerie(lancamento.getSerie());
			carregarDadosTelaListagem();
		}
	}

	public void salvar() {
		try {

			Lancamento lancamento = getLancamentoVO().getLancamento();

			if (lancamentoVO.getRepeteLancamento()) {
				lancamentoService.saveSerie(lancamentoVO.getLancamento());
			} else {
				lancamento = lancamentoService.save(lancamentoVO.getLancamento());
			}

			setLancamentoVO(lancamento.getVO());
			voltar();
			carregarDadosTelaListagem();
		} catch (ContaNotNullException | CategoriasIncompativeisException | MesLancamentoAlteradoException
				| TipoContaException | ValorLancamentoInvalidoException | DataSerieLancamentoInvalidaException e) {
			FacesMessages.fatal(Messages.msg(e.getMessage()));
			return;
		}
	}

	public List<Conta> getContasFiltro() {
		List<Conta> contas = contaService.findContasBancos();
		contas.addAll(contaService.findContasDinheiro());
		return contas;
	}

	public List<Categoria> getCategoriasFiltro() {
		return categoriaService.findAll();
	}

	public List<Conta> getContas() {

		if (getLancamentoVO().isTransferencia() || getLancamentoVO().getInOut().equals(InOut.E)) {
			List<Conta> contas = contaService.findContasBancos();
			contas.addAll(contaService.findContasDinheiro());
			return contas;
		}

		else {
			List<Conta> contas = contaService.findContasCorrentes();
			if (!isEdicaoLancamentoContaCorrente()) {
				contas.addAll(contaService.findContasCartoes());
			}
			return contas;
		}
	}

	public boolean apresentaCategoriasReceitas() {
		return (apresentaCategorias() && lancamentoVO.getInOut().equals(InOut.E));
	}

	public boolean apresentaCategoriasDespesas() {
		return (apresentaCategorias() && lancamentoVO.getInOut().equals(InOut.S));
	}

	private boolean apresentaCategorias() {
		return (!lancamentoVO.isAjuste()) && (!lancamentoVO.isTransferencia()) && (!lancamentoVO.isSaldoInicial())
				&& (!lancamentoVO.isFaturaCartao());
	}

	public boolean isEdicaoLancamentoContaCorrente() {
		return (getLancamentoVO().getId() != null && !getIsLancamentoCartao());
	}

	public BigDecimal getSaldoInicial() {
		return this.saldoInicial;
	}

	public BigDecimal getSaldoCorrentePrevisto() {
		return this.saldoCorrentePrevisto;
	}

	public BigDecimal getSaldoReceitaOrcada() {
		return this.saldoSaldoReceitaOrcada;
	}

	public BigDecimal getSaldoDespesaOrcada() {
		return this.saldoSaldoDespesaOrcada;
	}

	public BigDecimal getSaldoFinalPrevisto() {
		return this.saldoFinalPrevisto;
	}

	public boolean isFaturaCartao(Lancamento lancamento) {
		return lancamento instanceof LancamentoContaCorrente && ((LancamentoContaCorrente) lancamento).isFaturaCartao()
				? true
				: false;
	}

	public boolean isSaldoInicial(Lancamento lancamento) {
		return lancamento instanceof LancamentoContaCorrente && ((LancamentoContaCorrente) lancamento).isSaldoInicial()
				? true
				: false;
	}

	public List<Categoria> getCategoriasReceitas() {
		return categoriaService.findReceitas();
	}

	public List<Categoria> getCategoriasDespesas() {
		return categoriaService.findDespesas();
	}

	public List<OrcadoRealMesCategoria> getDespesaOrcadoRealMesCategoria() {
		return despesaOrcadoRealMesCategoria;
	}

	public List<OrcadoRealMesCategoria> getReceitaOrcadoRealMesCategoria() {
		return receitaOrcadoRealMesCategoria;
	}

	public boolean getIsLancamentoCartao() {
		return getLancamentoVO().getConta() instanceof ContaCartao;
	}

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}

	public AnoMes getFiltroMes() {
		return filtroMes;
	}

	public void setFiltroMes(AnoMes filtroMes) {
		this.filtroMes = filtroMes;
	}

	public List<AnoMes> getFiltrosMeses() {
		return periodoPlanejamento.getPeriodoCompleto();
	}

	public Conta getFiltroConta() {
		return filtroConta;
	}

	public void setFiltroConta(Conta filtroConta) {
		this.filtroConta = filtroConta;
	}

	public Categoria getFiltroCategoria() {
		return filtroCategoria;
	}

	public char getFiltroStatus() {
		return filtroStatus;
	}

	public void setFiltroStatus(char filtroStatus) {
		this.filtroStatus = filtroStatus;
	}

	public void setFiltroCategoria(Categoria filtroCategoria) {
		this.filtroCategoria = filtroCategoria;
	}

	public Date getMesCorrente() {
		return LocalDateConverter.toDate(LocalDate.now().withMonth(getFiltroMes().getMes()));
	}

	public void showNextMonth() {
		if (hasMoreMonths())
			filtroMes = filtroMes.plusMonths(1);

		carregarDadosTelaListagem();
	}

	public boolean hasMoreMonths() {
		if (filtroMes.plusMonths(1).compareTo(getFiltrosMeses().get(getFiltrosMeses().size() - 1)) <= 0) {
			return true;
		}
		return false;
	}

	public void showPreviousMonth() {
		if (hasLessMonths()) {
			filtroMes = filtroMes.minusMonths(1);
		}
		carregarDadosTelaListagem();
	}

	public boolean hasLessMonths() {
		if (filtroMes.minusMonths(1).compareTo(getFiltrosMeses().get(0)) >= 0) {
			return true;
		}
		return false;
	}

	public boolean isTratarEmSerie() {
		return tratarEmSerie;
	}

	public void setTratarEmSerie(boolean alterarSerie) {
		this.tratarEmSerie = alterarSerie;
	}

	public Date getDataInicialCalendario() {
		LocalDate localDate = LocalDate.now();
		return LocalDateConverter.toDate(localDate.withMonth(filtroMes.getMes()));
	}

}
