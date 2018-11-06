package br.com.victorpfranca.mybudget.transaction.rules;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.CreditCardTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionSerie;
import br.com.victorpfranca.mybudget.transaction.TransactionStatus;

@Stateless
public class LancamentoService {

	@EJB
	private LancamentoRulesFacade lancamentoRulesFacade;

	public List<Transaction> carregarExtratoCorrenteMensal(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial, TransactionStatus status) {
		return lancamentoRulesFacade.extrairExtrato(ano, mes, account, category, saldoInicial, status);
	}

	public List<Transaction> carregarExtratoCartaoMensal(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial) {
		return lancamentoRulesFacade.extrairExtratoCartao(ano, mes, account, category, saldoInicial);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TransactionSerie saveSerie(Transaction transaction)
			throws ContaNotNullException, CategoriasIncompativeisException, MesLancamentoAlteradoException,
			TipoContaException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {

		TransactionSerie serie = null;
		if (transaction instanceof CreditCardTransaction) {
			serie = lancamentoRulesFacade.saveSerieLancamentoCartaoDeCredito(transaction);
		} else if (transaction instanceof CheckingAccountTransaction
				&& ((CheckingAccountTransaction) transaction).isTransferencia()) {
			serie = lancamentoRulesFacade.saveSerieLancamentoTransferencia(transaction);
		} else {
			serie = lancamentoRulesFacade.saveSerieLancamentoContaCorrente(transaction);
		}

		return serie;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction save(Transaction transaction) throws ContaNotNullException, CategoriasIncompativeisException,
			MesLancamentoAlteradoException, TipoContaException, ValorLancamentoInvalidoException {

		if (transaction instanceof CreditCardTransaction) {
			transaction = lancamentoRulesFacade.saveLancamentoCartaoDeCredito(transaction);
		} else if (transaction instanceof CheckingAccountTransaction
				&& ((CheckingAccountTransaction) transaction).isTransferencia()) {
			transaction = lancamentoRulesFacade.saveLancamentoTransferencia(transaction);
		} else {
			transaction = lancamentoRulesFacade.saveLancamentoContaCorrente(transaction);
		}

		return transaction;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Transaction confirmar(Transaction transaction) throws ContaNotNullException, CategoriasIncompativeisException,
			MesLancamentoAlteradoException, TipoContaException, ValorLancamentoInvalidoException {
		if (transaction.isConfirmado())
			transaction.setStatus(TransactionStatus.NAO_CONFIRMADO);
		else
			transaction.setStatus(TransactionStatus.CONFIRMADO);

		try {
			return save(transaction);
		} catch (ContaNotNullException | CategoriasIncompativeisException | MesLancamentoAlteradoException
				| TipoContaException | ValorLancamentoInvalidoException e) {
			if (transaction.isConfirmado())
				transaction.setStatus(TransactionStatus.NAO_CONFIRMADO);
			else
				transaction.setStatus(TransactionStatus.CONFIRMADO);
			throw e;
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Transaction transaction) {
		lancamentoRulesFacade.removeLancamento(transaction);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSerie(TransactionSerie serie) {
		lancamentoRulesFacade.removeSerie(serie);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLancamentoCartao(CreditCardTransaction lancamentoCartao) {
		lancamentoRulesFacade.removeLancamentoCartao(lancamentoCartao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSerieLancamentoCartao(TransactionSerie serie) {
		lancamentoRulesFacade.removeSerieLancamentoCartao(serie);
	}

}
