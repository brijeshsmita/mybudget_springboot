package br.com.gestao.financeira.pessoal.lancamento;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.gestao.financeira.pessoal.DAOMock;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.infra.dao.QueryParam;

public class SaldoContaDAOMock extends DAOMock<SaldoConta> {

	@Override
	public List<SaldoConta> executeQuery(String query, QueryParam... parameters) {
		List<SaldoConta> found = new ArrayList<SaldoConta>();

		if (query.equals(SaldoConta.FIND_FROM_ANO_MES_QUERY)) {

			Conta conta = null;
			Integer ano = null;
			Integer mes = null;

			for (int i = 0; i < parameters.length; i++) {
				if (parameters[i].getParamName().equals("conta")) {
					conta = (Conta) parameters[i].getParamValue();
					continue;
				} else if (parameters[i].getParamName().equals("ano")) {
					ano = (Integer) parameters[i].getParamValue();
					continue;
				} else if (parameters[i].getParamName().equals("mes")) {
					mes = (Integer) parameters[i].getParamValue();
				}
			}

			for (Iterator<SaldoConta> iterator = entities.iterator(); iterator.hasNext();) {
				SaldoConta saldoConta = iterator.next();
				if (saldoConta.getConta().getId().equals(conta.getId()) && saldoConta.getAno().compareTo(ano) >= 0
						&& saldoConta.getMes().compareTo(mes) >= 0) {
					found.add(saldoConta);
				}
			}

			return found;
		}

		return super.executeQuery(query, parameters);
	}
}
