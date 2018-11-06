package br.com.gestao.financeira.pessoal.lancamento;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class WeekDaysExpectedDates {

	public static void main(String[] args) {
		Year year = Year.of(2001);

		for (int iDay = 1; iDay <= 7; iDay++) {

			DayOfWeek dayOfWeek = DayOfWeek.of(iDay);

			for (int month = 1; month <= 12; month++) {
				LocalDate localDate = LocalDate.of(year.getValue(), month, 1);

				LocalDate lastDayOfWeekInMonth = localDate.with(TemporalAdjusters.lastInMonth(dayOfWeek));

				System.out.print("public static String[] Y" + year + "_" + dayOfWeek + "_" + Month.of(month) + " = {");
				localDate = localDate.with(TemporalAdjusters.nextOrSame(dayOfWeek));
				while (localDate.compareTo(lastDayOfWeekInMonth) <= 0) {
					System.out.print("\"" + localDate + "\", ");
					localDate = localDate.with(TemporalAdjusters.next(dayOfWeek));
				}
				System.out.println("};");
			}
		}
	}

	public static List<String[]> getExpectedDatesListYear2000(DayOfWeek dayOfWeek) {
		List<String[]> expectedDatesList = new ArrayList<String[]>();

		if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SUNDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_MONDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_TUESDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_WEDNESDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_THURSDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_FRIDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2000_SATURDAY_DECEMBER);
		}

		return expectedDatesList;
	}
	
	public static List<String[]> getExpectedDatesListYear2001(DayOfWeek dayOfWeek) {
		List<String[]> expectedDatesList = new ArrayList<String[]>();

		if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SUNDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_MONDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_TUESDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_WEDNESDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_THURSDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_FRIDAY_DECEMBER);
		} else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_JANUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_FEBRUARY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_MARCH);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_APRIL);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_MAY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_JUNE);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_JULY);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_AUGUST);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_SEPTEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_OCTOBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_NOVEMBER);
			expectedDatesList.add(WeekDaysExpectedDates.Y2001_SATURDAY_DECEMBER);
		}

		return expectedDatesList;
	}

	public static String[] Y2000_MONDAY_JANUARY = { "2000-01-03", "2000-01-10", "2000-01-17", "2000-01-24",
			"2000-01-31" };
	public static String[] Y2000_MONDAY_FEBRUARY = { "2000-02-07", "2000-02-14", "2000-02-21", "2000-02-28" };
	public static String[] Y2000_MONDAY_MARCH = { "2000-03-06", "2000-03-13", "2000-03-20", "2000-03-27" };
	public static String[] Y2000_MONDAY_APRIL = { "2000-04-03", "2000-04-10", "2000-04-17", "2000-04-24" };
	public static String[] Y2000_MONDAY_MAY = { "2000-05-01", "2000-05-08", "2000-05-15", "2000-05-22", "2000-05-29" };
	public static String[] Y2000_MONDAY_JUNE = { "2000-06-05", "2000-06-12", "2000-06-19", "2000-06-26" };
	public static String[] Y2000_MONDAY_JULY = { "2000-07-03", "2000-07-10", "2000-07-17", "2000-07-24", "2000-07-31" };
	public static String[] Y2000_MONDAY_AUGUST = { "2000-08-07", "2000-08-14", "2000-08-21", "2000-08-28" };
	public static String[] Y2000_MONDAY_SEPTEMBER = { "2000-09-04", "2000-09-11", "2000-09-18", "2000-09-25" };
	public static String[] Y2000_MONDAY_OCTOBER = { "2000-10-02", "2000-10-09", "2000-10-16", "2000-10-23",
			"2000-10-30" };
	public static String[] Y2000_MONDAY_NOVEMBER = { "2000-11-06", "2000-11-13", "2000-11-20", "2000-11-27" };
	public static String[] Y2000_MONDAY_DECEMBER = { "2000-12-04", "2000-12-11", "2000-12-18", "2000-12-25" };
	public static String[] Y2000_TUESDAY_JANUARY = { "2000-01-04", "2000-01-11", "2000-01-18", "2000-01-25" };
	public static String[] Y2000_TUESDAY_FEBRUARY = { "2000-02-01", "2000-02-08", "2000-02-15", "2000-02-22",
			"2000-02-29" };
	public static String[] Y2000_TUESDAY_MARCH = { "2000-03-07", "2000-03-14", "2000-03-21", "2000-03-28" };
	public static String[] Y2000_TUESDAY_APRIL = { "2000-04-04", "2000-04-11", "2000-04-18", "2000-04-25" };
	public static String[] Y2000_TUESDAY_MAY = { "2000-05-02", "2000-05-09", "2000-05-16", "2000-05-23", "2000-05-30" };
	public static String[] Y2000_TUESDAY_JUNE = { "2000-06-06", "2000-06-13", "2000-06-20", "2000-06-27" };
	public static String[] Y2000_TUESDAY_JULY = { "2000-07-04", "2000-07-11", "2000-07-18", "2000-07-25" };
	public static String[] Y2000_TUESDAY_AUGUST = { "2000-08-01", "2000-08-08", "2000-08-15", "2000-08-22",
			"2000-08-29" };
	public static String[] Y2000_TUESDAY_SEPTEMBER = { "2000-09-05", "2000-09-12", "2000-09-19", "2000-09-26" };
	public static String[] Y2000_TUESDAY_OCTOBER = { "2000-10-03", "2000-10-10", "2000-10-17", "2000-10-24",
			"2000-10-31" };
	public static String[] Y2000_TUESDAY_NOVEMBER = { "2000-11-07", "2000-11-14", "2000-11-21", "2000-11-28" };
	public static String[] Y2000_TUESDAY_DECEMBER = { "2000-12-05", "2000-12-12", "2000-12-19", "2000-12-26" };
	public static String[] Y2000_WEDNESDAY_JANUARY = { "2000-01-05", "2000-01-12", "2000-01-19", "2000-01-26" };
	public static String[] Y2000_WEDNESDAY_FEBRUARY = { "2000-02-02", "2000-02-09", "2000-02-16", "2000-02-23" };
	public static String[] Y2000_WEDNESDAY_MARCH = { "2000-03-01", "2000-03-08", "2000-03-15", "2000-03-22",
			"2000-03-29" };
	public static String[] Y2000_WEDNESDAY_APRIL = { "2000-04-05", "2000-04-12", "2000-04-19", "2000-04-26" };
	public static String[] Y2000_WEDNESDAY_MAY = { "2000-05-03", "2000-05-10", "2000-05-17", "2000-05-24",
			"2000-05-31" };
	public static String[] Y2000_WEDNESDAY_JUNE = { "2000-06-07", "2000-06-14", "2000-06-21", "2000-06-28" };
	public static String[] Y2000_WEDNESDAY_JULY = { "2000-07-05", "2000-07-12", "2000-07-19", "2000-07-26" };
	public static String[] Y2000_WEDNESDAY_AUGUST = { "2000-08-02", "2000-08-09", "2000-08-16", "2000-08-23",
			"2000-08-30" };
	public static String[] Y2000_WEDNESDAY_SEPTEMBER = { "2000-09-06", "2000-09-13", "2000-09-20", "2000-09-27" };
	public static String[] Y2000_WEDNESDAY_OCTOBER = { "2000-10-04", "2000-10-11", "2000-10-18", "2000-10-25" };
	public static String[] Y2000_WEDNESDAY_NOVEMBER = { "2000-11-01", "2000-11-08", "2000-11-15", "2000-11-22",
			"2000-11-29" };
	public static String[] Y2000_WEDNESDAY_DECEMBER = { "2000-12-06", "2000-12-13", "2000-12-20", "2000-12-27" };
	public static String[] Y2000_THURSDAY_JANUARY = { "2000-01-06", "2000-01-13", "2000-01-20", "2000-01-27" };
	public static String[] Y2000_THURSDAY_FEBRUARY = { "2000-02-03", "2000-02-10", "2000-02-17", "2000-02-24" };
	public static String[] Y2000_THURSDAY_MARCH = { "2000-03-02", "2000-03-09", "2000-03-16", "2000-03-23",
			"2000-03-30" };
	public static String[] Y2000_THURSDAY_APRIL = { "2000-04-06", "2000-04-13", "2000-04-20", "2000-04-27" };
	public static String[] Y2000_THURSDAY_MAY = { "2000-05-04", "2000-05-11", "2000-05-18", "2000-05-25" };
	public static String[] Y2000_THURSDAY_JUNE = { "2000-06-01", "2000-06-08", "2000-06-15", "2000-06-22",
			"2000-06-29" };
	public static String[] Y2000_THURSDAY_JULY = { "2000-07-06", "2000-07-13", "2000-07-20", "2000-07-27" };
	public static String[] Y2000_THURSDAY_AUGUST = { "2000-08-03", "2000-08-10", "2000-08-17", "2000-08-24",
			"2000-08-31" };
	public static String[] Y2000_THURSDAY_SEPTEMBER = { "2000-09-07", "2000-09-14", "2000-09-21", "2000-09-28" };
	public static String[] Y2000_THURSDAY_OCTOBER = { "2000-10-05", "2000-10-12", "2000-10-19", "2000-10-26" };
	public static String[] Y2000_THURSDAY_NOVEMBER = { "2000-11-02", "2000-11-09", "2000-11-16", "2000-11-23",
			"2000-11-30" };
	public static String[] Y2000_THURSDAY_DECEMBER = { "2000-12-07", "2000-12-14", "2000-12-21", "2000-12-28" };
	public static String[] Y2000_FRIDAY_JANUARY = { "2000-01-07", "2000-01-14", "2000-01-21", "2000-01-28" };
	public static String[] Y2000_FRIDAY_FEBRUARY = { "2000-02-04", "2000-02-11", "2000-02-18", "2000-02-25" };
	public static String[] Y2000_FRIDAY_MARCH = { "2000-03-03", "2000-03-10", "2000-03-17", "2000-03-24",
			"2000-03-31" };
	public static String[] Y2000_FRIDAY_APRIL = { "2000-04-07", "2000-04-14", "2000-04-21", "2000-04-28" };
	public static String[] Y2000_FRIDAY_MAY = { "2000-05-05", "2000-05-12", "2000-05-19", "2000-05-26" };
	public static String[] Y2000_FRIDAY_JUNE = { "2000-06-02", "2000-06-09", "2000-06-16", "2000-06-23", "2000-06-30" };
	public static String[] Y2000_FRIDAY_JULY = { "2000-07-07", "2000-07-14", "2000-07-21", "2000-07-28" };
	public static String[] Y2000_FRIDAY_AUGUST = { "2000-08-04", "2000-08-11", "2000-08-18", "2000-08-25" };
	public static String[] Y2000_FRIDAY_SEPTEMBER = { "2000-09-01", "2000-09-08", "2000-09-15", "2000-09-22",
			"2000-09-29" };
	public static String[] Y2000_FRIDAY_OCTOBER = { "2000-10-06", "2000-10-13", "2000-10-20", "2000-10-27" };
	public static String[] Y2000_FRIDAY_NOVEMBER = { "2000-11-03", "2000-11-10", "2000-11-17", "2000-11-24" };
	public static String[] Y2000_FRIDAY_DECEMBER = { "2000-12-01", "2000-12-08", "2000-12-15", "2000-12-22",
			"2000-12-29" };
	public static String[] Y2000_SATURDAY_JANUARY = { "2000-01-01", "2000-01-08", "2000-01-15", "2000-01-22",
			"2000-01-29" };
	public static String[] Y2000_SATURDAY_FEBRUARY = { "2000-02-05", "2000-02-12", "2000-02-19", "2000-02-26" };
	public static String[] Y2000_SATURDAY_MARCH = { "2000-03-04", "2000-03-11", "2000-03-18", "2000-03-25" };
	public static String[] Y2000_SATURDAY_APRIL = { "2000-04-01", "2000-04-08", "2000-04-15", "2000-04-22",
			"2000-04-29" };
	public static String[] Y2000_SATURDAY_MAY = { "2000-05-06", "2000-05-13", "2000-05-20", "2000-05-27" };
	public static String[] Y2000_SATURDAY_JUNE = { "2000-06-03", "2000-06-10", "2000-06-17", "2000-06-24" };
	public static String[] Y2000_SATURDAY_JULY = { "2000-07-01", "2000-07-08", "2000-07-15", "2000-07-22",
			"2000-07-29" };
	public static String[] Y2000_SATURDAY_AUGUST = { "2000-08-05", "2000-08-12", "2000-08-19", "2000-08-26" };
	public static String[] Y2000_SATURDAY_SEPTEMBER = { "2000-09-02", "2000-09-09", "2000-09-16", "2000-09-23",
			"2000-09-30" };
	public static String[] Y2000_SATURDAY_OCTOBER = { "2000-10-07", "2000-10-14", "2000-10-21", "2000-10-28" };
	public static String[] Y2000_SATURDAY_NOVEMBER = { "2000-11-04", "2000-11-11", "2000-11-18", "2000-11-25" };
	public static String[] Y2000_SATURDAY_DECEMBER = { "2000-12-02", "2000-12-09", "2000-12-16", "2000-12-23",
			"2000-12-30" };
	public static String[] Y2000_SUNDAY_JANUARY = { "2000-01-02", "2000-01-09", "2000-01-16", "2000-01-23",
			"2000-01-30" };
	public static String[] Y2000_SUNDAY_FEBRUARY = { "2000-02-06", "2000-02-13", "2000-02-20", "2000-02-27" };
	public static String[] Y2000_SUNDAY_MARCH = { "2000-03-05", "2000-03-12", "2000-03-19", "2000-03-26" };
	public static String[] Y2000_SUNDAY_APRIL = { "2000-04-02", "2000-04-09", "2000-04-16", "2000-04-23",
			"2000-04-30" };
	public static String[] Y2000_SUNDAY_MAY = { "2000-05-07", "2000-05-14", "2000-05-21", "2000-05-28" };
	public static String[] Y2000_SUNDAY_JUNE = { "2000-06-04", "2000-06-11", "2000-06-18", "2000-06-25" };
	public static String[] Y2000_SUNDAY_JULY = { "2000-07-02", "2000-07-09", "2000-07-16", "2000-07-23", "2000-07-30" };
	public static String[] Y2000_SUNDAY_AUGUST = { "2000-08-06", "2000-08-13", "2000-08-20", "2000-08-27" };
	public static String[] Y2000_SUNDAY_SEPTEMBER = { "2000-09-03", "2000-09-10", "2000-09-17", "2000-09-24" };
	public static String[] Y2000_SUNDAY_OCTOBER = { "2000-10-01", "2000-10-08", "2000-10-15", "2000-10-22",
			"2000-10-29" };
	public static String[] Y2000_SUNDAY_NOVEMBER = { "2000-11-05", "2000-11-12", "2000-11-19", "2000-11-26" };
	public static String[] Y2000_SUNDAY_DECEMBER = { "2000-12-03", "2000-12-10", "2000-12-17", "2000-12-24",
			"2000-12-31" };

	public static String[] Y2001_MONDAY_JANUARY = { "2001-01-01", "2001-01-08", "2001-01-15", "2001-01-22",
			"2001-01-29" };
	public static String[] Y2001_MONDAY_FEBRUARY = { "2001-02-05", "2001-02-12", "2001-02-19", "2001-02-26" };
	public static String[] Y2001_MONDAY_MARCH = { "2001-03-05", "2001-03-12", "2001-03-19", "2001-03-26" };
	public static String[] Y2001_MONDAY_APRIL = { "2001-04-02", "2001-04-09", "2001-04-16", "2001-04-23",
			"2001-04-30" };
	public static String[] Y2001_MONDAY_MAY = { "2001-05-07", "2001-05-14", "2001-05-21", "2001-05-28" };
	public static String[] Y2001_MONDAY_JUNE = { "2001-06-04", "2001-06-11", "2001-06-18", "2001-06-25" };
	public static String[] Y2001_MONDAY_JULY = { "2001-07-02", "2001-07-09", "2001-07-16", "2001-07-23", "2001-07-30" };
	public static String[] Y2001_MONDAY_AUGUST = { "2001-08-06", "2001-08-13", "2001-08-20", "2001-08-27" };
	public static String[] Y2001_MONDAY_SEPTEMBER = { "2001-09-03", "2001-09-10", "2001-09-17", "2001-09-24" };
	public static String[] Y2001_MONDAY_OCTOBER = { "2001-10-01", "2001-10-08", "2001-10-15", "2001-10-22",
			"2001-10-29" };
	public static String[] Y2001_MONDAY_NOVEMBER = { "2001-11-05", "2001-11-12", "2001-11-19", "2001-11-26" };
	public static String[] Y2001_MONDAY_DECEMBER = { "2001-12-03", "2001-12-10", "2001-12-17", "2001-12-24",
			"2001-12-31" };
	public static String[] Y2001_TUESDAY_JANUARY = { "2001-01-02", "2001-01-09", "2001-01-16", "2001-01-23",
			"2001-01-30" };
	public static String[] Y2001_TUESDAY_FEBRUARY = { "2001-02-06", "2001-02-13", "2001-02-20", "2001-02-27" };
	public static String[] Y2001_TUESDAY_MARCH = { "2001-03-06", "2001-03-13", "2001-03-20", "2001-03-27" };
	public static String[] Y2001_TUESDAY_APRIL = { "2001-04-03", "2001-04-10", "2001-04-17", "2001-04-24" };
	public static String[] Y2001_TUESDAY_MAY = { "2001-05-01", "2001-05-08", "2001-05-15", "2001-05-22", "2001-05-29" };
	public static String[] Y2001_TUESDAY_JUNE = { "2001-06-05", "2001-06-12", "2001-06-19", "2001-06-26" };
	public static String[] Y2001_TUESDAY_JULY = { "2001-07-03", "2001-07-10", "2001-07-17", "2001-07-24",
			"2001-07-31" };
	public static String[] Y2001_TUESDAY_AUGUST = { "2001-08-07", "2001-08-14", "2001-08-21", "2001-08-28" };
	public static String[] Y2001_TUESDAY_SEPTEMBER = { "2001-09-04", "2001-09-11", "2001-09-18", "2001-09-25" };
	public static String[] Y2001_TUESDAY_OCTOBER = { "2001-10-02", "2001-10-09", "2001-10-16", "2001-10-23",
			"2001-10-30" };
	public static String[] Y2001_TUESDAY_NOVEMBER = { "2001-11-06", "2001-11-13", "2001-11-20", "2001-11-27" };
	public static String[] Y2001_TUESDAY_DECEMBER = { "2001-12-04", "2001-12-11", "2001-12-18", "2001-12-25" };
	public static String[] Y2001_WEDNESDAY_JANUARY = { "2001-01-03", "2001-01-10", "2001-01-17", "2001-01-24",
			"2001-01-31" };
	public static String[] Y2001_WEDNESDAY_FEBRUARY = { "2001-02-07", "2001-02-14", "2001-02-21", "2001-02-28" };
	public static String[] Y2001_WEDNESDAY_MARCH = { "2001-03-07", "2001-03-14", "2001-03-21", "2001-03-28" };
	public static String[] Y2001_WEDNESDAY_APRIL = { "2001-04-04", "2001-04-11", "2001-04-18", "2001-04-25" };
	public static String[] Y2001_WEDNESDAY_MAY = { "2001-05-02", "2001-05-09", "2001-05-16", "2001-05-23",
			"2001-05-30" };
	public static String[] Y2001_WEDNESDAY_JUNE = { "2001-06-06", "2001-06-13", "2001-06-20", "2001-06-27" };
	public static String[] Y2001_WEDNESDAY_JULY = { "2001-07-04", "2001-07-11", "2001-07-18", "2001-07-25" };
	public static String[] Y2001_WEDNESDAY_AUGUST = { "2001-08-01", "2001-08-08", "2001-08-15", "2001-08-22",
			"2001-08-29" };
	public static String[] Y2001_WEDNESDAY_SEPTEMBER = { "2001-09-05", "2001-09-12", "2001-09-19", "2001-09-26" };
	public static String[] Y2001_WEDNESDAY_OCTOBER = { "2001-10-03", "2001-10-10", "2001-10-17", "2001-10-24",
			"2001-10-31" };
	public static String[] Y2001_WEDNESDAY_NOVEMBER = { "2001-11-07", "2001-11-14", "2001-11-21", "2001-11-28" };
	public static String[] Y2001_WEDNESDAY_DECEMBER = { "2001-12-05", "2001-12-12", "2001-12-19", "2001-12-26" };
	public static String[] Y2001_THURSDAY_JANUARY = { "2001-01-04", "2001-01-11", "2001-01-18", "2001-01-25" };
	public static String[] Y2001_THURSDAY_FEBRUARY = { "2001-02-01", "2001-02-08", "2001-02-15", "2001-02-22" };
	public static String[] Y2001_THURSDAY_MARCH = { "2001-03-01", "2001-03-08", "2001-03-15", "2001-03-22",
			"2001-03-29" };
	public static String[] Y2001_THURSDAY_APRIL = { "2001-04-05", "2001-04-12", "2001-04-19", "2001-04-26" };
	public static String[] Y2001_THURSDAY_MAY = { "2001-05-03", "2001-05-10", "2001-05-17", "2001-05-24",
			"2001-05-31" };
	public static String[] Y2001_THURSDAY_JUNE = { "2001-06-07", "2001-06-14", "2001-06-21", "2001-06-28" };
	public static String[] Y2001_THURSDAY_JULY = { "2001-07-05", "2001-07-12", "2001-07-19", "2001-07-26" };
	public static String[] Y2001_THURSDAY_AUGUST = { "2001-08-02", "2001-08-09", "2001-08-16", "2001-08-23",
			"2001-08-30" };
	public static String[] Y2001_THURSDAY_SEPTEMBER = { "2001-09-06", "2001-09-13", "2001-09-20", "2001-09-27" };
	public static String[] Y2001_THURSDAY_OCTOBER = { "2001-10-04", "2001-10-11", "2001-10-18", "2001-10-25" };
	public static String[] Y2001_THURSDAY_NOVEMBER = { "2001-11-01", "2001-11-08", "2001-11-15", "2001-11-22",
			"2001-11-29" };
	public static String[] Y2001_THURSDAY_DECEMBER = { "2001-12-06", "2001-12-13", "2001-12-20", "2001-12-27" };
	public static String[] Y2001_FRIDAY_JANUARY = { "2001-01-05", "2001-01-12", "2001-01-19", "2001-01-26" };
	public static String[] Y2001_FRIDAY_FEBRUARY = { "2001-02-02", "2001-02-09", "2001-02-16", "2001-02-23" };
	public static String[] Y2001_FRIDAY_MARCH = { "2001-03-02", "2001-03-09", "2001-03-16", "2001-03-23",
			"2001-03-30" };
	public static String[] Y2001_FRIDAY_APRIL = { "2001-04-06", "2001-04-13", "2001-04-20", "2001-04-27" };
	public static String[] Y2001_FRIDAY_MAY = { "2001-05-04", "2001-05-11", "2001-05-18", "2001-05-25" };
	public static String[] Y2001_FRIDAY_JUNE = { "2001-06-01", "2001-06-08", "2001-06-15", "2001-06-22", "2001-06-29" };
	public static String[] Y2001_FRIDAY_JULY = { "2001-07-06", "2001-07-13", "2001-07-20", "2001-07-27" };
	public static String[] Y2001_FRIDAY_AUGUST = { "2001-08-03", "2001-08-10", "2001-08-17", "2001-08-24",
			"2001-08-31" };
	public static String[] Y2001_FRIDAY_SEPTEMBER = { "2001-09-07", "2001-09-14", "2001-09-21", "2001-09-28" };
	public static String[] Y2001_FRIDAY_OCTOBER = { "2001-10-05", "2001-10-12", "2001-10-19", "2001-10-26" };
	public static String[] Y2001_FRIDAY_NOVEMBER = { "2001-11-02", "2001-11-09", "2001-11-16", "2001-11-23",
			"2001-11-30" };
	public static String[] Y2001_FRIDAY_DECEMBER = { "2001-12-07", "2001-12-14", "2001-12-21", "2001-12-28" };
	public static String[] Y2001_SATURDAY_JANUARY = { "2001-01-06", "2001-01-13", "2001-01-20", "2001-01-27" };
	public static String[] Y2001_SATURDAY_FEBRUARY = { "2001-02-03", "2001-02-10", "2001-02-17", "2001-02-24" };
	public static String[] Y2001_SATURDAY_MARCH = { "2001-03-03", "2001-03-10", "2001-03-17", "2001-03-24",
			"2001-03-31" };
	public static String[] Y2001_SATURDAY_APRIL = { "2001-04-07", "2001-04-14", "2001-04-21", "2001-04-28" };
	public static String[] Y2001_SATURDAY_MAY = { "2001-05-05", "2001-05-12", "2001-05-19", "2001-05-26" };
	public static String[] Y2001_SATURDAY_JUNE = { "2001-06-02", "2001-06-09", "2001-06-16", "2001-06-23",
			"2001-06-30" };
	public static String[] Y2001_SATURDAY_JULY = { "2001-07-07", "2001-07-14", "2001-07-21", "2001-07-28" };
	public static String[] Y2001_SATURDAY_AUGUST = { "2001-08-04", "2001-08-11", "2001-08-18", "2001-08-25" };
	public static String[] Y2001_SATURDAY_SEPTEMBER = { "2001-09-01", "2001-09-08", "2001-09-15", "2001-09-22",
			"2001-09-29" };
	public static String[] Y2001_SATURDAY_OCTOBER = { "2001-10-06", "2001-10-13", "2001-10-20", "2001-10-27" };
	public static String[] Y2001_SATURDAY_NOVEMBER = { "2001-11-03", "2001-11-10", "2001-11-17", "2001-11-24" };
	public static String[] Y2001_SATURDAY_DECEMBER = { "2001-12-01", "2001-12-08", "2001-12-15", "2001-12-22",
			"2001-12-29" };
	public static String[] Y2001_SUNDAY_JANUARY = { "2001-01-07", "2001-01-14", "2001-01-21", "2001-01-28" };
	public static String[] Y2001_SUNDAY_FEBRUARY = { "2001-02-04", "2001-02-11", "2001-02-18", "2001-02-25" };
	public static String[] Y2001_SUNDAY_MARCH = { "2001-03-04", "2001-03-11", "2001-03-18", "2001-03-25" };
	public static String[] Y2001_SUNDAY_APRIL = { "2001-04-01", "2001-04-08", "2001-04-15", "2001-04-22",
			"2001-04-29" };
	public static String[] Y2001_SUNDAY_MAY = { "2001-05-06", "2001-05-13", "2001-05-20", "2001-05-27" };
	public static String[] Y2001_SUNDAY_JUNE = { "2001-06-03", "2001-06-10", "2001-06-17", "2001-06-24" };
	public static String[] Y2001_SUNDAY_JULY = { "2001-07-01", "2001-07-08", "2001-07-15", "2001-07-22", "2001-07-29" };
	public static String[] Y2001_SUNDAY_AUGUST = { "2001-08-05", "2001-08-12", "2001-08-19", "2001-08-26" };
	public static String[] Y2001_SUNDAY_SEPTEMBER = { "2001-09-02", "2001-09-09", "2001-09-16", "2001-09-23",
			"2001-09-30" };
	public static String[] Y2001_SUNDAY_OCTOBER = { "2001-10-07", "2001-10-14", "2001-10-21", "2001-10-28" };
	public static String[] Y2001_SUNDAY_NOVEMBER = { "2001-11-04", "2001-11-11", "2001-11-18", "2001-11-25" };
	public static String[] Y2001_SUNDAY_DECEMBER = { "2001-12-02", "2001-12-09", "2001-12-16", "2001-12-23",
			"2001-12-30" };

}