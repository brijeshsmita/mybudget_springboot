package br.com.gestao.financeira.pessoal.lancamento.extractors.saldofuturo;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.view.AnoMes;

public class AgrupadorSaldosMesesTest {

    @Test
    public void validaMesesVazios_12Meses() {
        Set<SaldoConta> conjuntoMeses = new TreeSet<>(Comparator.comparing(s->new AnoMes(s.getAno(), s.getMes())));
        conjuntoMeses.addAll(new AgrupadorSaldosMeses().listaSaldosPorMesComValorZero(18, 1, 18, 12));
        assertTrue(conjuntoMeses.size()==12);
    }
    @Test
    public void validaMesesVazios_24Meses() {
        Set<SaldoConta> conjuntoMeses = new TreeSet<>(Comparator.comparing(s->new AnoMes(s.getAno(), s.getMes())));
        conjuntoMeses.addAll(new AgrupadorSaldosMeses().listaSaldosPorMesComValorZero(18, 1, 19, 12));
        assertTrue(conjuntoMeses.size()==24);
    }
    @Test
    public void validaMesesVazios_36Meses() {
        Set<SaldoConta> conjuntoMeses = new TreeSet<>(Comparator.comparing(s->new AnoMes(s.getAno(), s.getMes())));
        conjuntoMeses.addAll(new AgrupadorSaldosMeses().listaSaldosPorMesComValorZero(18, 1, 20, 12));
        assertTrue(conjuntoMeses.size()==36);
    }
    @Test
    public void validaMesesVazios_inicioAnteriorFim() {
        Set<SaldoConta> conjuntoMeses = new TreeSet<>(Comparator.comparing(s->new AnoMes(s.getAno(), s.getMes())));
        conjuntoMeses.addAll(new AgrupadorSaldosMeses().listaSaldosPorMesComValorZero(18, 1, 17, 1));
        assertTrue(conjuntoMeses.size()==0);
    }
    @Test
    public void validaMesesVazios_confereValor() {
        SaldoConta saldoConta = new AgrupadorSaldosMeses().listaSaldosPorMesComValorZero(18, 1, 18, 1).get(0);
        assertTrue(BigDecimal.ZERO.equals(saldoConta.getValor()));
    }
    
    @Test
    public void saldoAcumuladoAte_comValores() {
        List<SaldoConta> saldosPorContas = new ArrayList<>();
        saldosPorContas.add(new SaldoConta(18, 3, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 4, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 5, BigDecimal.valueOf(50.0)));
        BigDecimal saldoAcumulado = new AgrupadorSaldosMeses().saldoAcumuladoAte(18, 8, saldosPorContas);
        assertTrue(BigDecimal.valueOf(150.0).equals(saldoAcumulado));
    }
    @Test
    public void saldoAcumuladoAte_resultadoNegativo() {
        List<SaldoConta> saldosPorContas = new ArrayList<>();
        saldosPorContas.add(new SaldoConta(18, 3, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 4, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 5, BigDecimal.valueOf(-150.0)));
        BigDecimal saldoAcumulado = new AgrupadorSaldosMeses().saldoAcumuladoAte(18, 8, saldosPorContas);
        assertTrue(BigDecimal.valueOf(-50.0).equals(saldoAcumulado));
    }
    @Test
    public void saldoAcumuladoAte_incluindoMesAlvo() {
        List<SaldoConta> saldosPorContas = new ArrayList<>();
        saldosPorContas.add(new SaldoConta(18, 3, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 4, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 5, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 8, BigDecimal.valueOf(50.0)));
        BigDecimal saldoAcumulado = new AgrupadorSaldosMeses().saldoAcumuladoAte(18, 8, saldosPorContas);
        assertTrue(BigDecimal.valueOf(200.0).equals(saldoAcumulado));
    }
    @Test
    public void saldoAcumuladoAte_semValores() {
        List<SaldoConta> saldosPorContas = new ArrayList<>();
        BigDecimal saldoAcumulado = new AgrupadorSaldosMeses().saldoAcumuladoAte(18, 8, saldosPorContas);
        assertTrue(BigDecimal.ZERO.equals(saldoAcumulado));
    }
    @Test
    public void saldoAcumuladoAte_saldosNulo() {
        BigDecimal saldoAcumulado = new AgrupadorSaldosMeses().saldoAcumuladoAte(18, 8, null);
        assertTrue(BigDecimal.ZERO.equals(saldoAcumulado));
    }
    
    @Test
    public void agruparSaldosPorMes_testaApenasValoresAnteriores() {
        List<SaldoConta> saldosPorContas = new ArrayList<>();
        saldosPorContas.add(new SaldoConta(18, 3, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 4, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 5, BigDecimal.valueOf(50.0)));
        
        List<SaldoConta> agrupados = new AgrupadorSaldosMeses().agruparSaldosPorMes(18, 8, 18, 10, saldosPorContas);
        assertEqualsSaldoConta(18, 8, BigDecimal.valueOf(150.0), agrupados.get(0));
        assertEqualsSaldoConta(18, 9, BigDecimal.valueOf(150.0), agrupados.get(1));
        assertEqualsSaldoConta(18, 10, BigDecimal.valueOf(150.0), agrupados.get(2));
    }
    @Test
    public void agruparSaldosPorMes_testaApenasValoresNosMesesAgrupados() {
        List<SaldoConta> saldosPorContas = new ArrayList<>();
        saldosPorContas.add(new SaldoConta(18, 8, BigDecimal.valueOf(10.0)));
        saldosPorContas.add(new SaldoConta(18, 9, BigDecimal.valueOf(20.0)));
        saldosPorContas.add(new SaldoConta(18, 10, BigDecimal.valueOf(30.0)));
        
        List<SaldoConta> agrupados = new AgrupadorSaldosMeses().agruparSaldosPorMes(18, 8, 18, 10, saldosPorContas);
        assertEqualsSaldoConta(18, 8, BigDecimal.valueOf(10.0), agrupados.get(0));
        assertEqualsSaldoConta(18, 9, BigDecimal.valueOf(30.0), agrupados.get(1));
        assertEqualsSaldoConta(18, 10, BigDecimal.valueOf(60.0), agrupados.get(2));
    }
    @Test
    public void agruparSaldosPorMes_testaApenasValoresNosMesesPosteriores() {
        List<SaldoConta> saldosPorContas = new ArrayList<>();
        saldosPorContas.add(new SaldoConta(18, 11, BigDecimal.valueOf(10.0)));
        saldosPorContas.add(new SaldoConta(18, 12, BigDecimal.valueOf(20.0)));
        saldosPorContas.add(new SaldoConta(19, 1, BigDecimal.valueOf(30.0)));
        
        List<SaldoConta> agrupados = new AgrupadorSaldosMeses().agruparSaldosPorMes(18, 8, 18, 10, saldosPorContas);
        assertEqualsSaldoConta(18, 8,  BigDecimal.ZERO, agrupados.get(0));
        assertEqualsSaldoConta(18, 9,  BigDecimal.ZERO, agrupados.get(1));
        assertEqualsSaldoConta(18, 10, BigDecimal.ZERO, agrupados.get(2));
    }
    @Test
    public void agruparSaldosPorMes_testaValoresEmEmesesAnterioresEAgrupados() {
        List<SaldoConta> saldosPorContas = new ArrayList<>();
        saldosPorContas.add(new SaldoConta(18, 3, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 4, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 5, BigDecimal.valueOf(50.0)));
        saldosPorContas.add(new SaldoConta(18, 8, BigDecimal.valueOf(10.0)));
        saldosPorContas.add(new SaldoConta(18, 9, BigDecimal.valueOf(20.0)));
        saldosPorContas.add(new SaldoConta(18, 10, BigDecimal.valueOf(30.0)));
        
        List<SaldoConta> agrupados = new AgrupadorSaldosMeses().agruparSaldosPorMes(18, 8, 18, 10, saldosPorContas);
        assertEqualsSaldoConta(18, 8, BigDecimal.valueOf(160.0), agrupados.get(0));
        assertEqualsSaldoConta(18, 9, BigDecimal.valueOf(180.0), agrupados.get(1));
        assertEqualsSaldoConta(18, 10, BigDecimal.valueOf(210.0), agrupados.get(2));
    }
    @Test
    public void agruparSaldosPorMes_testaSemValores() {
        List<SaldoConta> saldosPorContas = new ArrayList<>();
        List<SaldoConta> agrupados = new AgrupadorSaldosMeses().agruparSaldosPorMes(18, 8, 18, 10, saldosPorContas);
        assertTrue(agrupados.stream().allMatch(s->BigDecimal.ZERO.equals(s.getValor())));
    }
    

    private void assertEqualsSaldoConta(Integer ano, Integer mes, BigDecimal valor, SaldoConta saldo) {
        boolean cond1 = saldo.getAno().equals(ano);
        boolean cond2 = saldo.getMes().equals(mes);
        boolean cond3 = saldo.getValor().equals(valor);
        String expected = MessageFormat.format("{0,number,00}/{1,number,00}: {2}", mes,ano,valor);
        String found = MessageFormat.format("{0,number,00}/{1,number,00}: {2}", saldo.getMes(), saldo.getAno(), saldo.getValor());
        assertTrue(MessageFormat.format("Expected: [{0}], Found: [{1}]", expected,found) ,cond1 && cond2 && cond3);
    }
}
