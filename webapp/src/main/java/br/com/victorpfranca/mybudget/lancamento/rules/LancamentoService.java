package br.com.victorpfranca.mybudget.lancamento.rules;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoCartaoCredito;
import br.com.victorpfranca.mybudget.lancamento.LancamentoContaCorrente;
import br.com.victorpfranca.mybudget.lancamento.LancamentoStatus;
import br.com.victorpfranca.mybudget.lancamento.SerieLancamento;

@Stateless
public class LancamentoService {

	@EJB
	private LancamentoRulesFacade lancamentoRulesFacade;

	public List<Lancamento> carregarExtratoCorrenteMensal(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial, LancamentoStatus status) {
		return lancamentoRulesFacade.extrairExtrato(ano, mes, account, category, saldoInicial, status);
	}

	public List<Lancamento> carregarExtratoCartaoMensal(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial) {
		return lancamentoRulesFacade.extrairExtratoCartao(ano, mes, account, category, saldoInicial);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SerieLancamento saveSerie(Lancamento lancamento)
			throws ContaNotNullException, CategoriasIncompativeisException, MesLancamentoAlteradoException,
			TipoContaException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {

		SerieLancamento serie = null;
		if (lancamento instanceof LancamentoCartaoCredito) {
			serie = lancamentoRulesFacade.saveSerieLancamentoCartaoDeCredito(lancamento);
		} else if (lancamento instanceof LancamentoContaCorrente
				&& ((LancamentoContaCorrente) lancamento).isTransferencia()) {
			serie = lancamentoRulesFacade.saveSerieLancamentoTransferencia(lancamento);
		} else {
			serie = lancamentoRulesFacade.saveSerieLancamentoContaCorrente(lancamento);
		}

		return serie;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Lancamento save(Lancamento lancamento) throws ContaNotNullException, CategoriasIncompativeisException,
			MesLancamentoAlteradoException, TipoContaException, ValorLancamentoInvalidoException {

		if (lancamento instanceof LancamentoCartaoCredito) {
			lancamento = lancamentoRulesFacade.saveLancamentoCartaoDeCredito(lancamento);
		} else if (lancamento instanceof LancamentoContaCorrente
				&& ((LancamentoContaCorrente) lancamento).isTransferencia()) {
			lancamento = lancamentoRulesFacade.saveLancamentoTransferencia(lancamento);
		} else {
			lancamento = lancamentoRulesFacade.saveLancamentoContaCorrente(lancamento);
		}

		return lancamento;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Lancamento confirmar(Lancamento lancamento) throws ContaNotNullException, CategoriasIncompativeisException,
			MesLancamentoAlteradoException, TipoContaException, ValorLancamentoInvalidoException {
		if (lancamento.isConfirmado())
			lancamento.setStatus(LancamentoStatus.NAO_CONFIRMADO);
		else
			lancamento.setStatus(LancamentoStatus.CONFIRMADO);

		try {
			return save(lancamento);
		} catch (ContaNotNullException | CategoriasIncompativeisException | MesLancamentoAlteradoException
				| TipoContaException | ValorLancamentoInvalidoException e) {
			if (lancamento.isConfirmado())
				lancamento.setStatus(LancamentoStatus.NAO_CONFIRMADO);
			else
				lancamento.setStatus(LancamentoStatus.CONFIRMADO);
			throw e;
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Lancamento lancamento) {
		lancamentoRulesFacade.removeLancamento(lancamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSerie(SerieLancamento serie) {
		lancamentoRulesFacade.removeSerie(serie);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLancamentoCartao(LancamentoCartaoCredito lancamentoCartao) {
		lancamentoRulesFacade.removeLancamentoCartao(lancamentoCartao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSerieLancamentoCartao(SerieLancamento serie) {
		lancamentoRulesFacade.removeSerieLancamentoCartao(serie);
	}

}
