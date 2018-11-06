package br.com.victorpfranca.mybudget.transaction;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.com.victorpfranca.mybudget.DAOMock;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.infra.dao.QueryParam;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.CreditCardInvoiceTransactionItem;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;

public class LancamentoDAOMock extends DAOMock<Transaction> {

	public Transaction merge(Transaction transaction) {

		try {
			Object objectId = getObjectId(transaction);

			if (objectId == null) {
				setRandomObjectId(transaction);
			} else {
				Transaction entity = find((Serializable) objectId);
				entities.remove(entity);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		if (transaction instanceof CheckingAccountTransaction) {
			((CheckingAccountTransaction) transaction).carregarValoresAnteriores();
		}

		entities.add(transaction);

		return transaction;
	}

	@Override
	public List<Transaction> executeQuery(String query, QueryParam... parameters) {
		List<Transaction> found = new ArrayList<Transaction>();

		if (query.equals(Transaction.FIND_LANCAMENTO_FATURA_QUERY)) {

			return findLancamentoFatura(found, parameters);
		} else if (query.equals(Transaction.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY)) {

			findLancamentoContaCorrente(found, parameters);
			return found;
		} else if (query.equals(Transaction.FIND_LANCAMENTO_FATURA_CARTAO_ITEM_QUERY)) {

			return findLancamentoCartao(found, parameters);
		}

		return super.executeQuery(query, parameters);
	}

	private List<Transaction> findLancamentoCartao(List<Transaction> found, QueryParam... parameters) {
		Integer lancamentoCartaoId = null;

		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].getParamName().equals("lancamentoCartao")) {
				lancamentoCartaoId = ((CreditCardTransaction) parameters[i].getParamValue()).getId();
				break;
			}
		}

		for (Iterator<Transaction> iterator = entities.iterator(); iterator.hasNext();) {
			Transaction transaction = iterator.next();
			if (transaction instanceof CreditCardInvoiceTransactionItem && ((CreditCardInvoiceTransactionItem) transaction)
					.getLancamentoCartao().getId().equals(lancamentoCartaoId)) {
				found.add(transaction);
			}
		}
		return found;
	}

	private void findLancamentoContaCorrente(List<Transaction> found, QueryParam... parameters) {
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

		for (Iterator<Transaction> iterator = entities.iterator(); iterator.hasNext();) {
			Transaction transaction = iterator.next();
			if (transaction instanceof CheckingAccountTransaction
					&& ((CheckingAccountTransaction) transaction).getCartaoCreditoFatura() != null
					&& ((CheckingAccountTransaction) transaction).isFaturaCartao() == isFaturaCartao
					&& ((CheckingAccountTransaction) transaction).getCartaoCreditoFatura().getId()
							.equals(cartaoCreditoFatura.getId())) {
				found.add(transaction);
			}
		}
	}

	private List<Transaction> findLancamentoFatura(List<Transaction> found, QueryParam... parameters) {
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

		for (Iterator<Transaction> iterator = entities.iterator(); iterator.hasNext();) {
			Transaction transaction = iterator.next();
			if (transaction instanceof CheckingAccountTransaction
					&& ((CheckingAccountTransaction) transaction).getCartaoCreditoFatura() != null
					&& ((CheckingAccountTransaction) transaction).getCartaoCreditoFatura().getId()
							.equals(cartaoCreditoFatura.getId())
					&& transaction.getData().compareTo(data) >= 0) {
				found.add(transaction);
			}
		}

		return found;
	}

}
