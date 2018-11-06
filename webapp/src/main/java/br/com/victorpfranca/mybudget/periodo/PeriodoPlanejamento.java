package br.com.victorpfranca.mybudget.periodo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Stateful
public class PeriodoPlanejamento implements Serializable {

	@EJB
	private CredentialsStore credentialsStore;

	private static final long serialVersionUID = 1L;

	private static final Integer QUANT_MESES_FUTURO = 36;

	private MonthYear mesInicio;

	private List<MonthYear> periodoAnterior;
	private MonthYear mesAtual;
	private List<MonthYear> periodoFuturo;
	private List<MonthYear> periodoCompleto;

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
		this.mesInicio = new MonthYear(dataInicio.getYear(), dataInicio.getMonthValue());
	}

	private void initPeriodoAnterior() {
		periodoAnterior = new ArrayList<MonthYear>();
		MonthYear anoMesAtual = MonthYear.getCurrent();
		LocalDate dataInicio = credentialsStore.recuperarUsuarioLogado().getDataCadastroLocalDate();

		MonthYear anoMesInicio = new MonthYear(dataInicio.getYear(), dataInicio.getMonthValue());
		while (anoMesAtual.compareTo(anoMesInicio) > 0) {
			periodoAnterior.add(anoMesInicio);
			anoMesInicio = anoMesInicio.plusMonths(1);
		}
	}

	private void initMesAtual() {
		this.mesAtual = MonthYear.getCurrent();

	}

	private void initPeriodoFuturo() {
		periodoFuturo = new ArrayList<MonthYear>();

		MonthYear anoMesAtual = MonthYear.getCurrent();
		for (int i = 1; i <= PeriodoPlanejamento.QUANT_MESES_FUTURO - 1; i++) {
			periodoFuturo.add(anoMesAtual.plusMonths(i));
		}
	}

	private void initPeriodoCompleto() {
		periodoCompleto = new ArrayList<MonthYear>();
		periodoCompleto.addAll(periodoAnterior);
		periodoCompleto.add(mesAtual);
		periodoCompleto.addAll(periodoFuturo);
	}

	public List<MonthYear> getPeriodoAnterior() {
		return this.periodoAnterior;
	}

	public MonthYear getMesAtual() {
		return mesAtual;
	}

	public MonthYear getMesFinal() {
		return mesAtual.plusMonths(QUANT_MESES_FUTURO - 1);
	}

	public List<MonthYear> getPeriodoCompleto() {
		return this.periodoCompleto;
	}

	public List<MonthYear> getPeriodoFuturo() {
		return this.periodoFuturo;
	}

	public MonthYear getMesInicio() {
		return mesInicio;
	}

}
