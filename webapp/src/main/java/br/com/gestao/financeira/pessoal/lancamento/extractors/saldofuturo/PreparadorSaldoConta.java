package br.com.gestao.financeira.pessoal.lancamento.extractors.saldofuturo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.gestao.financeira.pessoal.conta.SaldoConta;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PreparadorSaldoConta {

	public List<SaldoConta> prepararListSaldos(int anoFrom, int mesFrom, int anoAte, int mesAte,
			List<SaldoConta> saldos) {

		// adiciona um saldo se não tiver nenhum
		if (saldos.isEmpty()) {
			SaldoConta saldoConta = new SaldoConta().withAno(anoFrom).withMes(mesFrom).withValor(BigDecimal.ZERO);
			saldos.add(saldoConta);
		}

		// adiciona primeiro saldo na data inicial se os saldos encontrados forem
		// anteriores ao mês inicial desejado
		if (saldos.get(saldos.size() - 1).compareDate(anoFrom, mesFrom) < 0) {
			saldos.add(0, new SaldoConta().withAno(anoFrom).withMes(mesFrom)
					.withValor(saldos.get(saldos.size() - 1).getValor()));
		}

		// adiciona primeiro saldo na data inicial se os saldos encontrados forem após
		// mês inicial desejado
		if (saldos.get(0).compareDate(anoFrom, mesFrom) > 0) {
			saldos.add(0, new SaldoConta().withAno(anoFrom).withMes(mesFrom).withValor(BigDecimal.ZERO));
		}

		// adiciona último saldo na data final se os saldos encontrados forem anterior
		// ao mês final desejado final
		if (saldos.get(saldos.size() - 1).compareDate(anoAte, mesAte) < 0) {
			saldos.add(new SaldoConta().withAno(anoAte).withMes(mesAte)
					.withValor(saldos.get(saldos.size() - 1).getValor()));
		}

		for (int i = 0; i < saldos.size(); i++) {
			SaldoConta saldo = saldos.get(i);
			if (saldo.compareDate(anoFrom, mesFrom) < 0) {
				saldos.remove(saldo);
			}
		}

		preencherMesesVazios(saldos);

		return saldos;
	}

	private void preencherMesesVazios(List<SaldoConta> saldos) {

		int i = 0;
		int j = 1;

		if (saldos.size() <= 1)
			return;

		if (saldos.get(j).getLocalDate().minusMonths(1).compareTo(saldos.get(i).getLocalDate()) == 0) {
			preencherMesesVazios(saldos.subList(i + 1, saldos.size()));
		} else {
			LocalDate data = saldos.get(i).getLocalDate().plusMonths(1);
			SaldoConta saldo = new SaldoConta().withAno(data.getYear()).withMes(data.getMonthValue())
					.withValor(saldos.get(i).getValor());
			i += 1;
			saldos.add(i, saldo);

			preencherMesesVazios(saldos.subList(j, saldos.size()));
		}
	}

	// public static void main(String[] args) {
	// List<SaldoConta> saldos = new ArrayList<SaldoConta>();
	//
	// saldos.add(new
	// SaldoConta().withAno(2018).withMes(1).withValor(BigDecimal.ONE));
	// saldos.add(new
	// SaldoConta().withAno(2018).withMes(6).withValor(BigDecimal.TEN));
	// saldos.add(new
	// SaldoConta().withAno(2018).withMes(12).withValor(BigDecimal.TEN.multiply(BigDecimal.TEN)));
	//
	// GeradorSaldoFuturo gerador = new GeradorSaldoFuturo();
	// gerador.execute(2017, 1, 2019, 12, saldos);
	//
	// for (Iterator iterator = saldos.iterator(); iterator.hasNext();) {
	// SaldoConta saldoConta = (SaldoConta) iterator.next();
	// System.out.println(saldoConta.getAno() + "-" + saldoConta.getMes() + "-" +
	// saldoConta.getValor());
	// }
	//
	// }

}
