package br.com.gestao.financeira.pessoal.conta.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaBanco;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.conta.ContaDinheiro;
import br.com.gestao.financeira.pessoal.conta.rules.ContaService;
import br.com.gestao.financeira.pessoal.conta.rules.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.conta.rules.NaoRemovivelException;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoCartaoCredito;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoStatus;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemocaoNaoPermitidaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;
import br.com.gestao.financeira.pessoal.periodo.PeriodoPlanejamento;
import br.com.gestao.financeira.pessoal.view.AnoMes;
import br.com.gestao.financeira.pessoal.view.FacesMessages;
import br.com.gestao.financeira.pessoal.view.Messages;

@Named
@ViewScoped
public class ContaViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ContaService contaService;

	@Inject
	private PeriodoPlanejamento periodoPlanejamento;

	private int selectedTab;
	private Conta objeto;
	private boolean telaGrid = true;

	private Map<Date, BigDecimal> faturasPreview;

	@PostConstruct
	public void init() {
		setSelectedTab(0);
	}

	public void incluirContaBanco() {
		setSelectedTab(0);
		setTelaGrid(false);
		Conta conta = new ContaBanco();
		setObjeto(conta);
	}

	public void incluirContaDinheiro() {
		setSelectedTab(0);
		setTelaGrid(false);
		Conta conta = new ContaDinheiro();
		setObjeto(conta);
	}

	public void incluirContaCartao() {
		setSelectedTab(0);
		setTelaGrid(false);
		Conta conta = new ContaCartao();
		setObjeto(conta);
		this.faturasPreview = carregarFaturasPreview();
	}

	public void alterar(Conta conta) {
		setSelectedTab(0);
		setTelaGrid(false);
		setObjeto(conta);

		if (conta instanceof ContaCartao) {
			Map<Date, BigDecimal> faturasPreview = carregarFaturasPreview();
			this.faturasPreview = carregarFaturasCartao(getObjeto(), faturasPreview);
		}
	}

	private Map<Date, BigDecimal> carregarFaturasCartao(Conta conta, Map<Date, BigDecimal> faturasPreview) {

		Calendar cal = Calendar.getInstance();

		List<LancamentoCartaoCredito> lancamentosCartaoExistentes = contaService
				.findLancamentosIniciaisCartao((ContaCartao) getObjeto());
		for (Iterator<LancamentoCartaoCredito> iterator = lancamentosCartaoExistentes.iterator(); iterator.hasNext();) {
			Lancamento faturaExistente = (Lancamento) iterator.next();

			for (Iterator<Date> iterator2 = faturasPreview.keySet().iterator(); iterator2.hasNext();) {
				Date faturaPreviewDate = (Date) iterator2.next();
				cal.setTime(faturaPreviewDate);
				int mes = cal.get(Calendar.MONTH) + 1;
				int ano = cal.get(Calendar.YEAR);
				if ((ano == faturaExistente.getAno().intValue()) && (mes == faturaExistente.getMes().intValue())) {
					faturasPreview.put(faturaPreviewDate, faturaExistente.getValor());
				}
			}
		}

		return faturasPreview;
	}

	private Map<Date, BigDecimal> carregarFaturasPreview() {
		Map<Date, BigDecimal> faturasParaView = new LinkedHashMap<Date, BigDecimal>();

		AnoMes anoMesInicio = periodoPlanejamento.getMesInicio();
		AnoMes anoMesFinal = periodoPlanejamento.getMesFinal();

		while (anoMesInicio.compareTo(anoMesFinal) <= 0) {
			LocalDate localDate = LocalDate.of(anoMesInicio.getAno(), anoMesInicio.getMes(), 1);
			faturasParaView.put(LocalDateConverter.toDate(localDate), null);
			anoMesInicio = anoMesInicio.plusMonths(1);
		}

		return faturasParaView;
	}

	public void voltar() {
		setTelaGrid(true);
		setSelectedTab(0);
		setObjeto(null);
	}

	public boolean isTelaGrid() {
		return telaGrid;
	}

	public void setTelaGrid(boolean telaGrid) {
		this.telaGrid = telaGrid;
	}

	public Conta getObjeto() {
		return objeto;
	}

	public void setObjeto(Conta objeto) {
		this.objeto = objeto;
	}

	public void excluir(Conta conta) {
		try {
			contaService.remove(conta);
		} catch (RemocaoNaoPermitidaException e) {
			FacesMessages.fatal(Messages.msg(e.getMessage()));
		} catch (NaoRemovivelException e) {
			FacesMessages.fatal(Messages.msg(e.getMessage(), e.getContaCartao()));
		}
	}

	public void salvar() throws ContaNotNullException {

		Conta conta = getObjeto();

		try {
			if (conta instanceof ContaCartao) {
				List<Lancamento> lancamentosCartao = criarLancamentosCartao((ContaCartao) conta);
				setObjeto(contaService.saveContaCartao(conta, lancamentosCartao));
			} else {
				setObjeto(contaService.saveContaCorrente(conta));
			}
		} catch (MesmoNomeExistenteException | MesLancamentoAlteradoException | TipoContaException
				| CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			FacesMessages.fatal(Messages.msg(e.getMessage()));
			return;
		}

		voltar();
	}

	private List<Lancamento> criarLancamentosCartao(ContaCartao conta) throws ContaNotNullException {
		List<Lancamento> lancamentosCartao = new ArrayList<Lancamento>();
		for (Iterator<Date> iterator = faturasPreview.keySet().iterator(); iterator.hasNext();) {
			Date faturaDate = (Date) iterator.next();
			if (faturasPreview.get(faturaDate) != null) {
				BigDecimal valor = new BigDecimal(String.valueOf(faturasPreview.get(faturaDate)));

				Date date = LocalDateConverter
						.toDate(LocalDateConverter.fromDate(faturaDate).withDayOfMonth(conta.getCartaoDiaPagamento()));

				LancamentoCartaoCredito lancamentoCartao = new LancamentoCartaoCredito();
				lancamentoCartao.setSaldoInicial(true);
				lancamentoCartao.setConta(conta);
				lancamentoCartao.setData(date);
				lancamentoCartao.setValor(valor);
				lancamentoCartao.setQtdParcelas(1);
				lancamentoCartao.setInOut(InOut.S);
				lancamentoCartao.setStatus(LancamentoStatus.NAO_CONFIRMADO);

				lancamentosCartao.add(lancamentoCartao);
			}
		}
		return lancamentosCartao;
	}

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}

	public Map<Date, BigDecimal> getFaturasPreview() {
		return faturasPreview;
	}

	public void setFaturasPreview(Map<Date, BigDecimal> faturas) {
		this.faturasPreview = faturas;
	}

	public List<Conta> getContasCorrentes() {
		List<Conta> contas = new ArrayList<Conta>();
		contas.addAll(getContasBancos());
		contas.addAll(getContasDinheiro());
		return contas;
	}

	public List<Conta> getContasBancos() {
		return contaService.findContasBancos();
	}

	public List<Conta> getContasCartoes() {
		return contaService.findContasCartoes();
	}

	public List<Conta> getContasDinheiro() {
		return contaService.findContasDinheiro();
	}

	public boolean getIsContaBanco() {
		return getObjeto() instanceof ContaBanco;
	}

	public boolean getIsContaDinheiro() {
		return getObjeto() instanceof ContaDinheiro;
	}

	public boolean getIsContaCartao() {
		return getObjeto() instanceof ContaCartao;
	}

}
