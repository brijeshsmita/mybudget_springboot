package br.com.victorpfranca.mybudget.transaction.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.category.CategoriaService;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.period.PlanningPeriod;
import br.com.victorpfranca.mybudget.transaction.CreditCardInvoiceTransactionItem;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionService;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Named
@ViewScoped
public class CreditCardAccountReportViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TransactionService transactionService;

	@Inject
	private BankAccountService bankAccountService;

	@Inject
	private CategoriaService categoriaService;
	
	@Inject
	private PlanningPeriod planningPeriod;

	private List<Transaction> transactions;

	private MonthYear filtroMes = MonthYear.getCurrent();

	private Account filtroConta = null;

	private Category filtroCategoria = null;

	@PostConstruct
	public void init() {
		carregarDadosTelaListagem();
	}

	public void carregarDadosTelaListagem() {
		List<Account> accounts = getContas();
		if (!accounts.isEmpty() && filtroConta == null)
			filtroConta = accounts.get(0);

		this.transactions = transactionService.carregarExtratoCartaoMensal(filtroMes.getAno(), filtroMes.getMes(),
				filtroConta, filtroCategoria, BigDecimal.ZERO);
	}

	public void excluir(CreditCardInvoiceTransactionItem lancamento) {
		if (lancamento != null) {
			transactionService.removeLancamentoCartao((CreditCardTransaction) lancamento.getLancamentoCartao());
			carregarDadosTelaListagem();
		}
	}

	public void excluirSerie(Transaction transaction) {
		if (transaction != null) {
			transactionService.removeSerieLancamentoCartao(transaction.getSerie());
			carregarDadosTelaListagem();
		}
	}

	public List<Transaction> getLancamentos() {
		return transactions;
	}

	public List<Account> getContas() {
		return bankAccountService.findContasCartoes();
	}

	public List<Category> getCategorias() {
		return categoriaService.findDespesas();
	}

	public MonthYear getFiltroMes() {
		return filtroMes;
	}

	public void setFiltroMes(MonthYear filtroMes) {
		this.filtroMes = filtroMes;
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

	public void setFiltroCategoria(Category filtroCategoria) {
		this.filtroCategoria = filtroCategoria;
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

	public List<MonthYear> getFiltrosMeses() {
		return planningPeriod.getPeriodoCompleto();
	}

}
