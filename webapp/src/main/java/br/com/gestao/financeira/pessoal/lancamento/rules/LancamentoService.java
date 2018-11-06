package br.com.gestao.financeira.pessoal.lancamento.rules;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoCartaoCredito;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoContaCorrente;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoStatus;
import br.com.gestao.financeira.pessoal.lancamento.SerieLancamento;

@Stateless
public class LancamentoService {

	@EJB
	private LancamentoRulesFacade lancamentoRulesFacade;

	public List<Lancamento> carregarExtratoCorrenteMensal(int ano, int mes, Conta conta, Categoria categoria,
			BigDecimal saldoInicial, LancamentoStatus status) {
		return lancamentoRulesFacade.extrairExtrato(ano, mes, conta, categoria, saldoInicial, status);
	}

	public List<Lancamento> carregarExtratoCartaoMensal(int ano, int mes, Conta conta, Categoria categoria,
			BigDecimal saldoInicial) {
		return lancamentoRulesFacade.extrairExtratoCartao(ano, mes, conta, categoria, saldoInicial);
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
