package br.com.gestao.financeira.pessoal.lancamento.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.categoria.CategoriaService;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.rules.ContaService;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoCartaoCredito;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoFaturaCartaoItem;
import br.com.gestao.financeira.pessoal.lancamento.rules.LancamentoService;
import br.com.gestao.financeira.pessoal.periodo.PeriodoPlanejamento;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@Named
@ViewScoped
public class ExtratoCartaoViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private LancamentoService lancamentoService;

	@Inject
	private ContaService contaService;

	@Inject
	private CategoriaService categoriaService;
	
	@Inject
	private PeriodoPlanejamento periodoPlanejamento;

	private List<Lancamento> lancamentos;

	private AnoMes filtroMes = AnoMes.getCurrent();

	private Conta filtroConta = null;

	private Categoria filtroCategoria = null;

	@PostConstruct
	public void init() {
		carregarDadosTelaListagem();
	}

	public void carregarDadosTelaListagem() {
		List<Conta> contas = getContas();
		if (!contas.isEmpty() && filtroConta == null)
			filtroConta = contas.get(0);

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

	public List<Conta> getContas() {
		return contaService.findContasCartoes();
	}

	public List<Categoria> getCategorias() {
		return categoriaService.findDespesas();
	}

	public AnoMes getFiltroMes() {
		return filtroMes;
	}

	public void setFiltroMes(AnoMes filtroMes) {
		this.filtroMes = filtroMes;
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

	public void setFiltroCategoria(Categoria filtroCategoria) {
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
