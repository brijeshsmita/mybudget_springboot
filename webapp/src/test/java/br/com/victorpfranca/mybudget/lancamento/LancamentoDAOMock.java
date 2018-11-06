package br.com.victorpfranca.mybudget.lancamento;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.com.victorpfranca.mybudget.DAOMock;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.infra.dao.QueryParam;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoCartaoCredito;
import br.com.victorpfranca.mybudget.lancamento.LancamentoContaCorrente;
import br.com.victorpfranca.mybudget.lancamento.LancamentoFaturaCartaoItem;

public class LancamentoDAOMock extends DAOMock<Lancamento> {

	public Lancamento merge(Lancamento lancamento) {

		try {
			Object objectId = getObjectId(lancamento);

			if (objectId == null) {
				setRandomObjectId(lancamento);
			} else {
				Lancamento entity = find((Serializable) objectId);
				entities.remove(entity);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		if (lancamento instanceof LancamentoContaCorrente) {
			((LancamentoContaCorrente) lancamento).carregarValoresAnteriores();
		}

		entities.add(lancamento);

		return lancamento;
	}

	@Override
	public List<Lancamento> executeQuery(String query, QueryParam... parameters) {
		List<Lancamento> found = new ArrayList<Lancamento>();

		if (query.equals(Lancamento.FIND_LANCAMENTO_FATURA_QUERY)) {

			return findLancamentoFatura(found, parameters);
		} else if (query.equals(Lancamento.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY)) {

			findLancamentoContaCorrente(found, parameters);
			return found;
		} else if (query.equals(Lancamento.FIND_LANCAMENTO_FATURA_CARTAO_ITEM_QUERY)) {

			return findLancamentoCartao(found, parameters);
		}

		return super.executeQuery(query, parameters);
	}

	private List<Lancamento> findLancamentoCartao(List<Lancamento> found, QueryParam... parameters) {
		Integer lancamentoCartaoId = null;

		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].getParamName().equals("lancamentoCartao")) {
				lancamentoCartaoId = ((LancamentoCartaoCredito) parameters[i].getParamValue()).getId();
				break;
			}
		}

		for (Iterator<Lancamento> iterator = entities.iterator(); iterator.hasNext();) {
			Lancamento lancamento = iterator.next();
			if (lancamento instanceof LancamentoFaturaCartaoItem && ((LancamentoFaturaCartaoItem) lancamento)
					.getLancamentoCartao().getId().equals(lancamentoCartaoId)) {
				found.add(lancamento);
			}
		}
		return found;
	}

	private void findLancamentoContaCorrente(List<Lancamento> found, QueryParam... parameters) {
		Account cartaoCreditoFatura = null;
		boolean isFaturaCartao = false;

		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].getParamName().equals("cartaoCreditoFatura")) {
				cartaoCreditoFatura = (Account) parameters[i].getParamValue();
				continue;
			} else if (parameters[i].getParamName().equals("faturaCartao")) {
				isFaturaCartao = (Boolean) parameters[i].getParamValue();
				continue;
			}
		}

		for (Iterator<Lancamento> iterator = entities.iterator(); iterator.hasNext();) {
			Lancamento lancamento = iterator.next();
			if (lancamento instanceof LancamentoContaCorrente
					&& ((LancamentoContaCorrente) lancamento).getCartaoCreditoFatura() != null
					&& ((LancamentoContaCorrente) lancamento).isFaturaCartao() == isFaturaCartao
					&& ((LancamentoContaCorrente) lancamento).getCartaoCreditoFatura().getId()
							.equals(cartaoCreditoFatura.getId())) {
				found.add(lancamento);
			}
		}
	}

	private List<Lancamento> findLancamentoFatura(List<Lancamento> found, QueryParam... parameters) {
		Account cartaoCreditoFatura = null;
		Date data = null;

		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].getParamName().equals("cartaoCreditoFatura")) {
				cartaoCreditoFatura = (Account) parameters[i].getParamValue();
				continue;
			} else if (parameters[i].getParamName().equals("data")) {
				data = (Date) parameters[i].getParamValue();
			}
		}

		for (Iterator<Lancamento> iterator = entities.iterator(); iterator.hasNext();) {
			Lancamento lancamento = iterator.next();
			if (lancamento instanceof LancamentoContaCorrente
					&& ((LancamentoContaCorrente) lancamento).getCartaoCreditoFatura() != null
					&& ((LancamentoContaCorrente) lancamento).getCartaoCreditoFatura().getId()
							.equals(cartaoCreditoFatura.getId())
					&& lancamento.getData().compareTo(data) >= 0) {
				found.add(lancamento);
			}
		}

		return found;
	}

}
