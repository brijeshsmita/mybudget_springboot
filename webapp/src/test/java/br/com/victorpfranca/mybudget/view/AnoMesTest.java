package br.com.victorpfranca.mybudget.view;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.victorpfranca.mybudget.view.MonthYear;

public class AnoMesTest {

	@Test
	public void shouldBeLessThan() {
		assertTrue(new MonthYear(2000, 1).compareTo(new MonthYear(2000, 2)) < 0);
		assertTrue(new MonthYear(2000, 1).compareTo(new MonthYear(2001, 1)) < 0);
		assertTrue(new MonthYear(2000, 1).compareTo(new MonthYear(2001, 2)) < 0);
		assertTrue(new MonthYear(2000, 3).compareTo(new MonthYear(2001, 2)) < 0);
	}

	@Test
	public void shouldBeEquals() {
		assertTrue(new MonthYear(2000, 1).compareTo(new MonthYear(2000, 1)) == 0);
	}

	@Test
	public void shouldBeGreater() {
		assertTrue(new MonthYear(2000, 2).compareTo(new MonthYear(2000, 1)) > 0);
		assertTrue(new MonthYear(2001, 2).compareTo(new MonthYear(2000, 2)) > 0);
		assertTrue(new MonthYear(2001, 3).compareTo(new MonthYear(2000, 2)) > 0);
		assertTrue(new MonthYear(2001, 2).compareTo(new MonthYear(2000, 3)) > 0);
	}


}
