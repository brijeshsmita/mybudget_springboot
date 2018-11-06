package br.com.victorpfranca.mybudget.lancamento.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.category.CategoriaService;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoCartaoCredito;
import br.com.victorpfranca.mybudget.lancamento.LancamentoFaturaCartaoItem;
import br.com.victorpfranca.mybudget.lancamento.rules.LancamentoService;
import br.com.victorpfranca.mybudget.periodo.PeriodoPlanejamento;
import br.com.victorpfranca.mybudget.view.AnoMes;

@Named
@ViewScoped
public class ExtratoCartaoViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private LancamentoService lancamentoService;

	@Inject
	private BankAccountService bankAccountService;

	@Inject
	private CategoriaService categoriaService;
	
	@Inject
	private PeriodoPlanejamento periodoPlanejamento;

	private List<Lancamento> lancamentos;

	private AnoMes filtroMes = AnoMes.getCurrent();

	private Account filtroConta = null;

	private Category filtroCategoria = null;

	@PostConstruct
	public void init() {
		carregarDadosTelaListagem();
	}

	public void carregarDadosTelaListagem() {
		List<Account> accounts = getAccounts();
		if (!accounts.isEmpty() && filtroConta == null)
			filtroConta = accounts.get(0);

		this.lancamentos = lancamentoService.carregarExtratoCartaoMensal(filtroMes.getAno(), filtroMes.getMes(),
				filtroConta, filtroCategoria, BigDecimal.ZERO);
	}

	public void excluir(LancamentoFaturaCartaoItem lancamento) {
		if (lancamento != null) {
			lancamentoService.removeLancamentoCartao((LancamentoCartaoCredito) lancamento.getLancamentoCartao());
			carregarDadosTelaListagem();
		}
	}

	public void excluirSerie(Lancamento lancamento) {
		if (lancamento != null) {
			lancamentoService.removeSerieLancamentoCartao(lancamento.getSerie());
			carregarDadosTelaListagem();
		}
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public List<Account> getAccounts() {
		return bankAccountService.findContasCartoes();
	}

	public List<Category> getCategorias() {
		return categoriaService.findDespesas();
	}

	public AnoMes getFiltroMes() {
		return filtroMes;
	}

	public void setFiltroMes(AnoMes filtroMes) {
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

	public List<AnoMes> getFiltrosMeses() {
		return periodoPlanejamento.getPeriodoCompleto();
	}

}
