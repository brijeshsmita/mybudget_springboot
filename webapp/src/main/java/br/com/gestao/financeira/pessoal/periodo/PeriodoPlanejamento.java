package br.com.gestao.financeira.pessoal.periodo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@Stateful
public class PeriodoPlanejamento implements Serializable {

	@EJB
	private CredentialsStore credentialsStore;

	private static final long serialVersionUID = 1L;

	private static final Integer QUANT_MESES_FUTURO = 36;

	private AnoMes mesInicio;

	private List<AnoMes> periodoAnterior;
	private AnoMes mesAtual;
	private List<AnoMes> periodoFuturo;
	private List<AnoMes> periodoCompleto;

	@PostConstruct
	public void init() {
		initMesInicio();
		initPeriodoAnterior();
		initMesAtual();
		initPeriodoFuturo();
		initPeriodoCompleto();
	}

	private void initMesInicio() {
		LocalDate dataInicio = credentialsStore.recuperarUsuarioLogado().getDataCadastroLocalDate();
		this.mesInicio = new AnoMes(dataInicio.getYear(), dataInicio.getMonthValue());
	}

	private void initPeriodoAnterior() {
		periodoAnterior = new ArrayList<AnoMes>();
		AnoMes anoMesAtual = AnoMes.getCurrent();
		LocalDate dataInicio = credentialsStore.recuperarUsuarioLogado().getDataCadastroLocalDate();

		AnoMes anoMesInicio = new AnoMes(dataInicio.getYear(), dataInicio.getMonthValue());
		while (anoMesAtual.compareTo(anoMesInicio) > 0) {
			periodoAnterior.add(anoMesInicio);
			anoMesInicio = anoMesInicio.plusMonths(1);
		}
	}

	private void initMesAtual() {
		this.mesAtual = AnoMes.getCurrent();

	}

	private void initPeriodoFuturo() {
		periodoFuturo = new ArrayList<AnoMes>();

		AnoMes anoMesAtual = AnoMes.getCurrent();
		for (int i = 1; i <= PeriodoPlanejamento.QUANT_MESES_FUTURO - 1; i++) {
			periodoFuturo.add(anoMesAtual.plusMonths(i));
		}
	}

	private void initPeriodoCompleto() {
		periodoCompleto = new ArrayList<AnoMes>();
		periodoCompleto.addAll(periodoAnterior);
		periodoCompleto.add(mesAtual);
		periodoCompleto.addAll(periodoFuturo);
	}

	public List<AnoMes> getPeriodoAnterior() {
		return this.periodoAnterior;
	}

	public AnoMes getMesAtual() {
		return mesAtual;
	}

	public AnoMes getMesFinal() {
		return mesAtual.plusMonths(QUANT_MESES_FUTURO - 1);
	}

	public List<AnoMes> getPeriodoCompleto() {
		return this.periodoCompleto;
	}

	public List<AnoMes> getPeriodoFuturo() {
		return this.periodoFuturo;
	}

	public AnoMes getMesInicio() {
		return mesInicio;
	}

}
