package br.com.victorpfranca.mybudget.transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.victorpfranca.mybudget.DAOMock;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.infra.dao.QueryParam;

public class SaldoContaDAOMock extends DAOMock<AccountBalance> {

	@Override
	public List<AccountBalance> executeQuery(String query, QueryParam... parameters) {
		List<AccountBalance> found = new ArrayList<AccountBalance>();

		if (query.equals(AccountBalance.FIND_FROM_ANO_MES_QUERY)) {

			Account account = null;
			Integer ano = null;
			Integer mes = null;

			for (int i = 0; i < parameters.length; i++) {
				if (parameters[i].getParamName().equals("account")) {
					account = (Account) parameters[i].getParamValue();
					continue;
				} else if (parameters[i].getParamName().equals("ano")) {
					ano = (Integer) parameters[i].getParamValue();
					continue;
				} else if (parameters[i].getParamName().equals("mes")) {
					mes = (Integer) parameters[i].getParamValue();
				}
			}

			for (Iterator<AccountBalance> iterator = entities.iterator(); iterator.hasNext();) {
				AccountBalance accountBalance = iterator.next();
				if (accountBalance.getAccount().getId().equals(account.getId()) && accountBalance.getAno().compareTo(ano) >= 0
						&& accountBalance.getMes().compareTo(mes) >= 0) {
					found.add(accountBalance);
				}
			}

			return found;
		}

		return super.executeQuery(query, parameters);
	}
}
