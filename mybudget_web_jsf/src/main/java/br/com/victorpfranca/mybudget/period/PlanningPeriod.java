package br.com.victorpfranca.mybudget.period;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.victorpfranca.mybudget.view.MonthYear;

public class PlanningPeriod implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<MonthYear> getPeriodoAnterior() {
		return new ArrayList<MonthYear>();
	}

	public MonthYear getMesAtual() {
		return new MonthYear(LocalDate.now());
	}

	public MonthYear getMesFinal() {
		return new MonthYear(LocalDate.now());
	}

	public List<MonthYear> getPeriodoCompleto() {
		return new ArrayList<MonthYear>();
	}

	public List<MonthYear> getPeriodoFuturo() {
		return new ArrayList<MonthYear>();
	}

	public MonthYear getMesInicio() {
		return new MonthYear(LocalDate.now());
	}

}
