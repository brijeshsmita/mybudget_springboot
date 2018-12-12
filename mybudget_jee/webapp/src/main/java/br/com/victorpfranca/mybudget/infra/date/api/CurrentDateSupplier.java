package br.com.victorpfranca.mybudget.infra.date.api;

import static br.com.victorpfranca.mybudget.infra.date.DateUtils.dateToLocalDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import javax.ejb.Remote;

@Remote
public interface CurrentDateSupplier {

	Date currentDate();

	default LocalDateTime currentLocalDateTime() {
		return dateToLocalDateTime(currentDate());
	}

	default LocalDate currentLocalDate() {
		return currentLocalDateTime().toLocalDate();
	}

	default LocalTime currentLocalTime() {
		return currentLocalDateTime().toLocalTime();
	}

}
