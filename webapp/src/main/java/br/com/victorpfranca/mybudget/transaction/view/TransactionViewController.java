package br.com.victorpfranca.mybudget.transaction.view;

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
import br.com.victorpfranca.mybudget.budget.BudgetQuery;
import br.com.victorpfranca.mybudget.budget.MonthCategoryBudgetReal;
import br.com.victorpfranca.mybudget.category.CategoriaService;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.period.PlanningPeriod;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionQuery;
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;
import br.com.victorpfranca.mybudget.transaction.TransactionVO;
import br.com.victorpfranca.mybudget.transaction.TransactionsFilter;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionSerieDateException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionService;
import br.com.victorpfranca.mybudget.view.FacesMessages;
import br.com.victorpfranca.mybudget.view.Messages;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Named
@ViewScoped
public class TransactionViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TransactionService transactionService;

	@Inject
	private BankAccountService bankAccountService;

	@Inject
	private CategoriaService categoriaService;

	@Inject
	private TransactionQuery transactionQuery;
	@Inject
	private BalanceQuery balanceQuery;
	@Inject
	private BudgetQuery budgetQuery;

	@Inject
	private PlanningPeriod planningPeriod;

	private int selectedTab;
	private TransactionVO transactionVO;

	private boolean telaGrid = true;
	private List<Transaction> transactions;

	private BigDecimal saldoInicial;
	private BigDecimal saldoCorrentePrevisto;
	private BigDecimal saldoSaldoReceitaOrcada;
	private BigDecimal saldoSaldoDespesaOrcada;
	private BigDecimal saldoFinalPrevisto;

	private List<MonthCategoryBudgetReal> receitaOrcadoRealMesCategoria;
	private List<MonthCategoryBudgetReal> despesaOrcadoRealMesCategoria;

	private MonthYear filtroMes = MonthYear.getCurrent();

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
		TransactionsFilter transactionsFilter = new TransactionsFilter(getFiltroMes(),
				Optional.ofNullable(getFiltroCategoria()).map(Category::getId).orElse(null),
				Optional.ofNullable(getFiltroConta()).map(Account::getId).orElse(null),
				Optional.ofNullable(TransactionStatus.fromChar(getFiltroStatus())).orElse(null));
		this.transactions = transactionQuery.transactions(transactionsFilter);
		this.saldoInicial = balanceQuery.recuperarSaldoInicial(transactionsFilter);
		this.saldoCorrentePrevisto = balanceQuery.recuperarSaldoCorrentePrevisto(transactionsFilter);
		this.saldoSaldoReceitaOrcada = balanceQuery.recuperarSaldoReceitaOrcada(getFiltroMes());
		this.saldoSaldoDespesaOrcada = balanceQuery.recuperarSaldoDespesaOrcada(getFiltroMes());
		this.saldoFinalPrevisto = balanceQuery.recuperarSaldoFinalPrevisto(transactionsFilter);
		this.despesaOrcadoRealMesCategoria = budgetQuery.recuperarDespesasPorCategoriaOrcada(getFiltroMes());
		this.receitaOrcadoRealMesCategoria = budgetQuery.recuperarReceitasPorCategoriaOrcada(getFiltroMes());
	}

	public List<Transaction> getLancamentos() {
		return transactions;
	}

	public void realizarTransferencia() {
		setTratarEmSerie(true);
		setSelectedTab(0);
		setTelaGrid(false);
		TransactionVO lancamento = new TransactionVO(InOut.S, TransactionStatus.NAO_CONFIRMADO);
		lancamento.setTransferencia(true);
		setLancamentoVO(lancamento);
	}

	public void incluirReceita() {
		setTratarEmSerie(true);
		setSelectedTab(0);
		setTelaGrid(false);
		TransactionVO lancamento = new TransactionVO(InOut.E, TransactionStatus.NAO_CONFIRMADO);
		setLancamentoVO(lancamento);
	}

	public void incluirDespesa() {
		setTratarEmSerie(true);
		setSelectedTab(0);
		setTelaGrid(false);
		TransactionVO lancamento = new TransactionVO(InOut.S, TransactionStatus.NAO_CONFIRMADO);
		setLancamentoVO(lancamento);
	}

	public void incluirAjuste() {
		setTratarEmSerie(true);
		setSelectedTab(0);
		setTelaGrid(false);
		TransactionVO lancamento = new TransactionVO(InOut.S, TransactionStatus.NAO_CONFIRMADO);
		lancamento.setAjuste(true);
		setLancamentoVO(lancamento);
	}

	public void confirmar(Transaction transaction) {
		try {
			transaction = transactionService.confirmar(transaction);
			setLancamentoVO(transaction.getVO());
		} catch (NullableAccountException | IncompatibleCategoriesException | TransactionMonthUpdatedException
				| AccountTypeException | InvalidTransactionValueException e) {
			FacesMessages.fatal(Messages.msg(e.getMessage()));
			return;
		}
	}

	public void alterar(Transaction transaction) {
		setTratarEmSerie(false);
		setSelectedTab(0);
		setTelaGrid(false);
		setLancamentoVO(transaction.getVO());
	}

	public void alterarSerie(Transaction transaction) {
		setTratarEmSerie(true);

		setSelectedTab(0);
		setTelaGrid(false);

		TransactionVO vo = transaction.getVO();
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

	public TransactionVO getLancamentoVO() {
		return transactionVO;
	}

	public void setLancamentoVO(TransactionVO lancamento) {
		this.transactionVO = lancamento;
	}

	public void excluir(Transaction transaction) {
		if (transaction != null) {
			transactionService.remove(transaction);
			carregarDadosTelaListagem();
		}
	}

	public void excluirSerie(Transaction transaction) {
		if (transaction != null) {
			transactionService.removeSerie(transaction.getSerie());
			carregarDadosTelaListagem();
		}
	}

	public void salvar() {
		try {

			Transaction transaction = getLancamentoVO().getLancamento();

			if (transactionVO.getRepeteLancamento()) {
				transactionService.saveSerie(transactionVO.getLancamento());
			} else {
				transaction = transactionService.save(transactionVO.getLancamento());
			}

			setLancamentoVO(transaction.getVO());
			voltar();
			carregarDadosTelaListagem();
		} catch (NullableAccountException | IncompatibleCategoriesException | TransactionMonthUpdatedException
				| AccountTypeException | InvalidTransactionValueException | InvalidTransactionSerieDateException e) {
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
		return (apresentaCategorias() && transactionVO.getInOut().equals(InOut.E));
	}

	public boolean apresentaCategoriasDespesas() {
		return (apresentaCategorias() && transactionVO.getInOut().equals(InOut.S));
	}

	private boolean apresentaCategorias() {
		return (!transactionVO.isAjuste()) && (!transactionVO.isTransferencia()) && (!transactionVO.isSaldoInicial())
				&& (!transactionVO.isFaturaCartao());
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

	public boolean isFaturaCartao(Transaction transaction) {
		return transaction instanceof CheckingAccountTransaction && ((CheckingAccountTransaction) transaction).isFaturaCartao()
				? true
				: false;
	}

	public boolean isSaldoInicial(Transaction transaction) {
		return transaction instanceof CheckingAccountTransaction && ((CheckingAccountTransaction) transaction).isSaldoInicial()
				? true
				: false;
	}

	public List<Category> getCategoriasReceitas() {
		return categoriaService.findReceitas();
	}

	public List<Category> getCategoriasDespesas() {
		return categoriaService.findDespesas();
	}

	public List<MonthCategoryBudgetReal> getDespesaOrcadoRealMesCategoria() {
		return despesaOrcadoRealMesCategoria;
	}

	public List<MonthCategoryBudgetReal> getReceitaOrcadoRealMesCategoria() {
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

	public MonthYear getFiltroMes() {
		return filtroMes;
	}

	public void setFiltroMes(MonthYear filtroMes) {
		this.filtroMes = filtroMes;
	}

	public List<MonthYear> getFiltrosMeses() {
		return planningPeriod.getPeriodoCompleto();
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
