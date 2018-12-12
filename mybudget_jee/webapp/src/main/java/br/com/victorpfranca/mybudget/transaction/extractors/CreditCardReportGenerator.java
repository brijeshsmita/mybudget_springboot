package br.com.victorpfranca.mybudget.transaction.extractors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.Transaction;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CreditCardReportGenerator {

	@EJB
	private CredentialsStore credentialsStore;

	@Inject
	private EntityManager em;
	
	public List<Transaction> execute(int ano, int mes, Account account, Category category) {
		return execute(ano, mes, account, category, BigDecimal.ZERO);
	}

	public List<Transaction> execute(int ano, int mes, Account account, Category category, BigDecimal saldoInicial) {
		List<Transaction> transactions = em
				.createNamedQuery(Transaction.FIND_LANCAMENTO_FATURA_CARTAO_ITEM_QUERY, Transaction.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado())
				.setParameter("lancamentoCartao", null).setParameter("ano", ano).setParameter("mes", mes)
				.setParameter("conta", account).setParameter("categoria", category).getResultList();

		List<Transaction> extrato = new ArrayList<Transaction>();
		BigDecimal saldoAnterior = saldoInicial;
		for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction) iterator.next();
			saldoAnterior = transaction.somarSaldo(saldoAnterior);
			extrato.add(transaction);
		}

		return extrato;
	}

}
