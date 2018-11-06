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
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TransactionsReportGenerator {

	@EJB
	private CredentialsStore credentialsStore;

	@Inject
	private EntityManager em;

	public List<Transaction> execute(int ano, int mes, Account account, Category category, TransactionStatus status) {
		return execute(ano, mes, account, category, BigDecimal.ZERO, status);
	}

	public List<Transaction> execute(int ano, int mes, Account account, Category category, BigDecimal saldoInicial,
			TransactionStatus status) {
		List<Transaction> transactions = em
				.createNamedQuery(Transaction.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, Transaction.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", ano)
				.setParameter("mes", mes).setParameter("account", account).setParameter("cartaoCreditoFatura", null)
				.setParameter("faturaCartao", null).setParameter("saldoInicial", null)
				.setParameter("category", category).setParameter("status", status).getResultList();

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
