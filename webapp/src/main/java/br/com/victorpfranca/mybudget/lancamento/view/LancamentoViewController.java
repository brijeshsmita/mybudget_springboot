package br.com.victorpfranca.mybudget.lancamento.view;

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

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.LocalDateConverter;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.BalanceQuery;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.category.CategoriaService;
import br.com.victorpfranca.mybudget.lancamento.ConsultaLancamentos;
import br.com.victorpfranca.mybudget.lancamento.FiltrosLancamentos;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoContaCorrente;
import br.com.victorpfranca.mybudget.lancamento.LancamentoStatus;
import br.com.victorpfranca.mybudget.lancamento.LancamentoVO;
import br.com.victorpfranca.mybudget.lancamento.rules.CategoriasIncompativeisException;
import br.com.victorpfranca.mybudget.lancamento.rules.ContaNotNullException;
import br.com.victorpfranca.mybudget.lancamento.rules.DataSerieLancamentoInvalidaException;
import br.com.victorpfranca.mybudget.lancamento.rules.LancamentoService;
import br.com.victorpfranca.mybudget.lancamento.rules.MesLancamentoAlteradoException;
import br.com.victorpfranca.mybudget.lancamento.rules.TipoContaException;
import br.com.victorpfranca.mybudget.lancamento.rules.ValorLancamentoInvalidoException;
import br.com.victorpfranca.mybudget.orcamento.ConsultaOrcamentos;
import br.com.victorpfranca.mybudget.orcamento.OrcadoRealMesCategoria;
import br.com.victorpfranca.mybudget.periodo.PeriodoPlanejamento;
import br.com.victorpfranca.mybudget.view.AnoMes;
import br.com.victorpfranca.mybudget.view.FacesMessages;
import br.com.victorpfranca.mybudget.view.Messages;

@Named
@ViewScoped
public class LancamentoViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private LancamentoService lancamentoService;

	@Inject
	private BankAccountService bankAccountService;

	@Inject
	private CategoriaService categoriaService;

	@Inject
	private ConsultaLancamentos consultaLancamentos;
	@Inject
	private BalanceQuery balanceQuery;
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

	private Account filtroConta = null;

	private Category filtroCategoria = null;

	private char filtroStatus = 0;

	private boolean tratarEmSerie = true;

	@PostConstruct
	public void init() {
		setSelectedTab(0);
		carregarDadosTelaListagem();
	}

	public void carregarDadosTelaListagem() {
		FiltrosLancamentos filtrosLancamentos = new FiltrosLancamentos(getFiltroMes(),
				Optional.ofNullable(getFiltroCategoria()).map(Category::getId).orElse(null),
				Optional.ofNullable(getFiltroConta()).map(Account::getId).orElse(null),
				Optional.ofNullable(LancamentoStatus.fromChar(getFiltroStatus())).orElse(null));
		this.lancamentos = consultaLancamentos.lancamentos(filtrosLancamentos);
		this.saldoInicial = balanceQuery.recuperarSaldoInicial(filtrosLancamentos);
		this.saldoCorrentePrevisto = balanceQuery.recuperarSaldoCorrentePrevisto(filtrosLancamentos);
		this.saldoSaldoReceitaOrcada = balanceQuery.recuperarSaldoReceitaOrcada(getFiltroMes());
		this.saldoSaldoDespesaOrcada = balanceQuery.recuperarSaldoDespesaOrcada(getFiltroMes());
		this.saldoFinalPrevisto = balanceQuery.recuperarSaldoFinalPrevisto(filtrosLancamentos);
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

	public List<Account> getAccountsFiltro() {
		List<Account> accounts = bankAccountService.findContasBancos();
		accounts.addAll(bankAccountService.findContasDinheiro());
		return accounts;
	}

	public List<Category> getCategoriasFiltro() {
		return categoriaService.findAll();
	}

	public List<Account> getAccounts() {

		if (getLancamentoVO().isTransferencia() || getLancamentoVO().getInOut().equals(InOut.E)) {
			List<Account> accounts = bankAccountService.findContasBancos();
			accounts.addAll(bankAccountService.findContasDinheiro());
			return accounts;
		}

		else {
			List<Account> accounts = bankAccountService.findContasCorrentes();
			if (!isEdicaoLancamentoContaCorrente()) {
				accounts.addAll(bankAccountService.findContasCartoes());
			}
			return accounts;
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

	public List<Category> getCategoriasReceitas() {
		return categoriaService.findReceitas();
	}

	public List<Category> getCategoriasDespesas() {
		return categoriaService.findDespesas();
	}

	public List<OrcadoRealMesCategoria> getDespesaOrcadoRealMesCategoria() {
		return despesaOrcadoRealMesCategoria;
	}

	public List<OrcadoRealMesCategoria> getReceitaOrcadoRealMesCategoria() {
		return receitaOrcadoRealMesCategoria;
	}

	public boolean getIsLancamentoCartao() {
		return getLancamentoVO().getAccount() instanceof CreditCardAccount;
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

	public Account getFiltroConta() {
		return filtroConta;
	}

	public void setFiltroConta(Account filtroConta) {
		this.filtroConta = filtroConta;
	}

	public Category getFiltroCategoria() {
		return filtroCategoria;
	}

	public char getFiltroStatus() {
		return filtroStatus;
	}

	public void setFiltroStatus(char filtroStatus) {
		this.filtroStatus = filtroStatus;
	}

	public void setFiltroCategoria(Category filtroCategoria) {
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
