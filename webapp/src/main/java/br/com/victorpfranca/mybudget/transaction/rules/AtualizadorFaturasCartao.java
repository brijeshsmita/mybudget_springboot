package br.com.victorpfranca.mybudget.transaction.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.infra.dao.QueryParam;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.CreditCardInvoiceTransactionItem;
import br.com.victorpfranca.mybudget.transaction.Transaction;

@Stateless
public class AtualizadorFaturasCartao {

	@EJB
	private DAO<Transaction> lancamentoDAO;

	@EJB
	private CredentialsStore credentialsStore;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void abater(List<Transaction> faturasItens) {

		if (faturasItens.isEmpty())
			return;

		CreditCardAccount creditCardAccount = (CreditCardAccount) ((CreditCardInvoiceTransactionItem) faturasItens.get(0)).getLancamentoCartao()
				.getAccount();

		List<Transaction> faturas = lancamentoDAO.executeQuery(Transaction.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY,
				new QueryParam("user", credentialsStore.recuperarIdUsuarioLogado()),
				new QueryParam("cartaoCreditoFatura", creditCardAccount), new QueryParam("faturaCartao", true),
				new QueryParam("saldoInicial", null), new QueryParam("ano", null), new QueryParam("mes", null), new QueryParam("account", null),
				new QueryParam("category", null), new QueryParam("status", null));

		for (Iterator<Transaction> iterator = faturasItens.iterator(); iterator.hasNext();) {
			CreditCardInvoiceTransactionItem faturaItem = (CreditCardInvoiceTransactionItem) iterator.next();

			CheckingAccountTransaction fatura = getFaturaReferente(faturas, faturaItem);
			if (fatura != null) {
				if(faturaItem.getInOut().equals(InOut.E))
					fatura.setValor(fatura.getValor().subtract(faturaItem.getValor()));
				else fatura.setValor(fatura.getValor().add(faturaItem.getValor()));
				lancamentoDAO.merge(fatura);
			}
		}

	}

	private CheckingAccountTransaction getFaturaReferente(List<Transaction> faturas,
			CreditCardInvoiceTransactionItem faturaItem) {

		for (Iterator<Transaction> iterator = faturas.iterator(); iterator.hasNext();) {
			CheckingAccountTransaction fatura = (CheckingAccountTransaction) iterator.next();
			if (fatura.getAno().compareTo(faturaItem.getAno()) == 0
					&& fatura.getMes().compareTo(faturaItem.getMes()) == 0) {
				return fatura;
			}
		}

		return null;
	}
	
	public void setLancamentoDAO(DAO<Transaction> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}
	
	public void setCredentialsStore(CredentialsStore credentialsStore) {
		this.credentialsStore = credentialsStore;
	}

}
