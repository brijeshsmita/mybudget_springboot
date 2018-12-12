package br.com.victorpfranca.mybudget;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class LocalDateConverter {

	public static Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate fromDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date plusMonth(Date date, int monthsToAdd) {
		return toDate(fromDate(date).plusMonths(monthsToAdd));
	}
	
	public static Date now() {
		return toDate(LocalDate.now());
	}

}
