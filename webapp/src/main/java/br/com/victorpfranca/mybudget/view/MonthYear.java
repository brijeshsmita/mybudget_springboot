package br.com.victorpfranca.mybudget.view;

import java.io.Serializable;
import java.time.LocalDate;

public class MonthYear implements Serializable, Comparable<MonthYear> {

	private static final long serialVersionUID = 1L;

	private LocalDate date;

	public MonthYear(LocalDate date) {
	    this.date = date.withDayOfMonth(1);
	}
	
	public MonthYear(int ano, int mes) {

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

	public MonthYear plusMonths(int months) {
		LocalDate date = this.date.plusMonths(months);

		MonthYear monthYear = new MonthYear(date.getYear(), date.getMonthValue());

		return monthYear;
	}

	public MonthYear minusMonths(int months) {
		LocalDate date = this.date.minusMonths(months);

		MonthYear monthYear = new MonthYear(date.getYear(), date.getMonthValue());

		return monthYear;
	}

	public String toString() {
		return String.valueOf(date.getMonthValue()).concat("/").concat(String.valueOf(date.getYear()));
	}

	public static MonthYear getCurrent() {
		return new MonthYear(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
	}
	
	public LocalDate getDate() {
		return date;
	}

	public int compareTo(MonthYear o) {
		return this.date.compareTo(o.getDate());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MonthYear))
			return false;
		return getAno() == ((MonthYear) obj).getAno() && getMes() == ((MonthYear) obj).getMes();
	}

	@Override
	public int hashCode() {
		return new StringBuffer().append(date.getYear()).append(date.getMonthValue()).toString().hashCode();
	}

}
