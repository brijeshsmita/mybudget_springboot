package br.com.victorpfranca.mybudget.transaction.rules;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionSerie;
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;
import br.com.victorpfranca.mybudget.transaction.extractors.CreditCardReportGenerator;
import br.com.victorpfranca.mybudget.transaction.extractors.TransactionsReportGenerator;

@Stateless
public class LancamentoRulesFacade {

	@EJB
	protected CriadorSerieLancamento criadorSerieLancamento;

	@EJB
	protected CriadorLancamentoContaCorrente criadorLancamentoContaCorrente;

	@EJB
	protected CriadorLancamentoCartaoCredito criadorLancamentoCartaoCredito;

	@EJB
	protected RemovedorLancamentoContaCorrente removedorLancamento;

	@EJB
	protected RemovedorLancamentoCartao removedorLancamentoCartao;

	@EJB
	protected TransactionsReportGenerator transactionsReportGenerator;

	@EJB
	protected CreditCardReportGenerator creditCardReportGenerator;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction saveLancamentoContaCorrente(Transaction transaction)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		return criadorLancamentoContaCorrente.save(transaction);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction saveLancamentoCartaoDeCredito(Transaction transaction)
			throws ContaNotNullException, MesLancamentoAlteradoException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		return criadorLancamentoCartaoCredito.save(transaction);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction saveLancamentoTransferencia(Transaction transaction)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		return criadorLancamentoContaCorrente.saveTransferencia(transaction);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TransactionSerie saveSerieLancamentoContaCorrente(Transaction transaction)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {
		return criadorSerieLancamento.saveLancamentoContaCorrente(transaction);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TransactionSerie saveSerieLancamentoCartaoDeCredito(Transaction transaction)
			throws ContaNotNullException, MesLancamentoAlteradoException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {
		return criadorSerieLancamento.saveLancamentoCartaoCredito(transaction);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TransactionSerie saveSerieLancamentoTransferencia(Transaction transaction)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {
		return criadorSerieLancamento.saveTransferencia(transaction);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLancamento(Transaction transaction) {
		removedorLancamento.remover(transaction);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSerie(TransactionSerie serie) {
		removedorLancamento.removerSerie(serie);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLancamentoCartao(CreditCardTransaction lancamento) {
		removedorLancamentoCartao.remover(lancamento, true);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSerieLancamentoCartao(TransactionSerie serie) {
		removedorLancamentoCartao.removerSerie(serie, true);
	}

	public List<Transaction> extrairExtrato(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial, TransactionStatus status) {
		return transactionsReportGenerator.execute(ano, mes, account, category, saldoInicial, status);
	}

	public List<Transaction> extrairExtrato(int ano, int mes, Account account, Category category, TransactionStatus status) {
		return transactionsReportGenerator.execute(ano, mes, account, category, status);
	}

	public List<Transaction> extrairExtratoCartao(int ano, int mes, Account account, Category category) {
		return creditCardReportGenerator.execute(ano, mes, account, category);
	}

	public List<Transaction> extrairExtratoCartao(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial) {
		return creditCardReportGenerator.execute(ano, mes, account, category, saldoInicial);
	}

}