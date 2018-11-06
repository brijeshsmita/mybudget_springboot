package br.com.victorpfranca.mybudget.view;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.victorpfranca.mybudget.view.AnoMes;

public class AnoMesTest {

	@Test
	public void shouldBeLessThan() {
		assertTrue(new AnoMes(2000, 1).compareTo(new AnoMes(2000, 2)) < 0);
		assertTrue(new AnoMes(2000, 1).compareTo(new AnoMes(2001, 1)) < 0);
		assertTrue(new AnoMes(2000, 1).compareTo(new AnoMes(2001, 2)) < 0);
		assertTrue(new AnoMes(2000, 3).compareTo(new AnoMes(2001, 2)) < 0);
	}

	@Test
	public void shouldBeEquals() {
		assertTrue(new AnoMes(2000, 1).compareTo(new AnoMes(2000, 1)) == 0);
	}

	@Test
	public void shouldBeGreater() {
		assertTrue(new AnoMes(2000, 2).compareTo(new AnoMes(2000, 1)) > 0);
		assertTrue(new AnoMes(2001, 2).compareTo(new AnoMes(2000, 2)) > 0);
		assertTrue(new AnoMes(2001, 3).compareTo(new AnoMes(2000, 2)) > 0);
		assertTrue(new AnoMes(2001, 2).compareTo(new AnoMes(2000, 3)) > 0);
	}


}
