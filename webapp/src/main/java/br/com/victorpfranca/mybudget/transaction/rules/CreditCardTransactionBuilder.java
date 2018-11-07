package br.com.victorpfranca.mybudget.transaction.rules;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.infra.dao.QueryParam;
import br.com.victorpfranca.mybudget.transaction.CreditCardInvoiceTransactionItem;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;

@Stateless
public class CreditCardTransactionBuilder {

	@EJB
	private DAO<Transaction> lancamentoDAO;

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Transaction> save(Account account, List<Transaction> transactions)
			throws NullableAccountException, TransactionMonthUpdatedException, AccountTypeException,
			IncompatibleCategoriesException, InvalidTransactionValueException {
		List<Transaction> lancamentosPersistidos = new ArrayList<Transaction>();

		for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
			Transaction transaction = (Transaction) iterator.next();
			transaction.setAccount(account);
			lancamentosPersistidos.add(save(transaction));
		}

		return lancamentosPersistidos;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction save(Transaction transaction) throws NullableAccountException, TransactionMonthUpdatedException,
			AccountTypeException, IncompatibleCategoriesException, InvalidTransactionValueException {

		transaction.validar();

		transaction = lancamentoDAO.merge(transaction);

		List<Transaction> faturas = atualizarFaturas((CreditCardTransaction) transaction);

		accountBalanceUpdater.addSaldos(faturas);

		atualizarFaturaItem(transaction, faturas);

		return transaction;
	}

	protected List<Transaction> atualizarFaturas(CreditCardTransaction lancamentoCartao) throws NullableAccountException {
		Date dataPagamentoProximo = getDataPrimeiraFatura(lancamentoCartao);

		List<Transaction> faturas = carregarFaturas(lancamentoCartao, dataPagamentoProximo);
		for (Iterator<Transaction> iterator = faturas.iterator(); iterator.hasNext();) {
			Transaction fatura = (Transaction) iterator.next();
			BigDecimal valorAnterior = fatura.getId() == null ? BigDecimal.ZERO : fatura.getValorAnterior();
			lancamentoDAO.merge(fatura);
			fatura.setValorAnterior(valorAnterior);
		}

		return faturas;
	}

	protected Date getDataPrimeiraFatura(CreditCardTransaction lancamento) {
		Date dataPagamentoProximo = Date.from(((CreditCardAccount) lancamento.getAccount())
				.getDataPagamentoProximo(lancamento.getData()).atStartOfDay(ZoneId.systemDefault()).toInstant());
		return dataPagamentoProximo;
	}

	protected void atualizarFaturaItem(Transaction transaction, List<Transaction> faturas) {
		int indiceParcela = 1;
		for (Iterator<Transaction> iterator = faturas.iterator(); iterator.hasNext();) {
			Transaction fatura = (Transaction) iterator.next();
			CreditCardInvoiceTransactionItem faturaItem = ((CreditCardTransaction) transaction)
					.buildFaturaItem(fatura.getData(), fatura.getAno(), fatura.getMes(), indiceParcela++);
			faturaItem.setLancamentoCartao((CreditCardTransaction) transaction);
			faturaItem.setSaldoInicial(transaction.isSaldoInicial());
			lancamentoDAO.merge(faturaItem);
		}
	}

	protected List<Transaction> carregarFaturas(CreditCardTransaction lancamento, Date dataPrimeiraFatura)
			throws NullableAccountException {

		List<Transaction> faturasExistentes = lancamentoDAO.executeQuery(Transaction.FIND_LANCAMENTO_FATURA_QUERY,
				QueryParam.build("cartaoCreditoFatura", (CreditCardAccount) lancamento.getAccount()),
				QueryParam.build("data", dataPrimeiraFatura));

		return ((CreditCardAccount) lancamento.getAccount()).carregarFaturas(lancamento, faturasExistentes);
	}

	public void setLancamentoDAO(DAO<Transaction> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}

	public void setAtualizadorSaldoConta(AccountBalanceUpdater accountBalanceUpdater) {
		this.accountBalanceUpdater = accountBalanceUpdater;
	}

}
