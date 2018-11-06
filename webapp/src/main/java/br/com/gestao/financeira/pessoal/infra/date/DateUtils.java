package br.com.gestao.financeira.pessoal.infra.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtils {

	private DateUtils() {
	}

	private static final SimpleDateFormat ISO8601_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

	public static LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"));
	}

	public static LocalDate dateToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date localDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

	public static Date localDateToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static String iso8601(Date date) {
		return ISO8601_DATE_FORMAT.format(date);
	}

	public static Date iso8601(String string) {
		try {
			return ISO8601_DATE_FORMAT.parse(string);
		} catch (ParseException e) {
			return null;
		}
	}
}
