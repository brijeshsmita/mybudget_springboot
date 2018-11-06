package br.com.gestao.financeira.pessoal.view;

import java.io.Serializable;
import java.time.LocalDate;

public class AnoMes implements Serializable, Comparable<AnoMes> {

	private static final long serialVersionUID = 1L;

	private LocalDate date;

	public AnoMes(LocalDate date) {
	    this.date = date.withDayOfMonth(1);
	}
	
	public AnoMes(int ano, int mes) {

		this.date = LocalDate.of(ano, mes, 1);
	}

	public int getAno() {
		return date.getYear();
	}

	public void setAno(int ano) {
		this.date = this.date.withYear(ano);
	}

	public int getMes() {
		return date.getMonthValue();
	}

	public void setMes(int mes) {
		this.date=this.date.withMonth(mes);
	}

	public AnoMes plusMonths(int months) {
		LocalDate date = this.date.plusMonths(months);

		AnoMes anoMes = new AnoMes(date.getYear(), date.getMonthValue());

		return anoMes;
	}

	public AnoMes minusMonths(int months) {
		LocalDate date = this.date.minusMonths(months);

		AnoMes anoMes = new AnoMes(date.getYear(), date.getMonthValue());

		return anoMes;
	}

	public String toString() {
		return String.valueOf(date.getMonthValue()).concat("/").concat(String.valueOf(date.getYear()));
	}

	public static AnoMes getCurrent() {
		return new AnoMes(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
	}
	
	public LocalDate getDate() {
		return date;
	}

	public int compareTo(AnoMes o) {
		return this.date.compareTo(o.getDate());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AnoMes))
			return false;
		return getAno() == ((AnoMes) obj).getAno() && getMes() == ((AnoMes) obj).getMes();
	}

	@Override
	public int hashCode() {
		return new StringBuffer().append(date.getYear()).append(date.getMonthValue()).toString().hashCode();
	}

}
