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
import br.com.victorpfranca.mybudget.lancamento.LancamentoStatus;
import br.com.victorpfranca.mybudget.lancamento.SerieLancamento;
import br.com.victorpfranca.mybudget.lancamento.extractors.GeradorExtratoCartao;
import br.com.victorpfranca.mybudget.lancamento.extractors.GeradorExtratoLancamentos;

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
	protected GeradorExtratoLancamentos geradorExtratoLancamentos;

	@EJB
	protected GeradorExtratoCartao geradorExtratoCartao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Lancamento saveLancamentoContaCorrente(Lancamento lancamento)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		return criadorLancamentoContaCorrente.save(lancamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Lancamento saveLancamentoCartaoDeCredito(Lancamento lancamento)
			throws ContaNotNullException, MesLancamentoAlteradoException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		return criadorLancamentoCartaoCredito.save(lancamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Lancamento saveLancamentoTransferencia(Lancamento lancamento)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		return criadorLancamentoContaCorrente.saveTransferencia(lancamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SerieLancamento saveSerieLancamentoContaCorrente(Lancamento lancamento)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {
		return criadorSerieLancamento.saveLancamentoContaCorrente(lancamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SerieLancamento saveSerieLancamentoCartaoDeCredito(Lancamento lancamento)
			throws ContaNotNullException, MesLancamentoAlteradoException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {
		return criadorSerieLancamento.saveLancamentoCartaoCredito(lancamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SerieLancamento saveSerieLancamentoTransferencia(Lancamento lancamento)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {
		return criadorSerieLancamento.saveTransferencia(lancamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLancamento(Lancamento lancamento) {
		removedorLancamento.remover(lancamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSerie(SerieLancamento serie) {
		removedorLancamento.removerSerie(serie);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeLancamentoCartao(LancamentoCartaoCredito lancamento) {
		removedorLancamentoCartao.remover(lancamento, true);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeSerieLancamentoCartao(SerieLancamento serie) {
		removedorLancamentoCartao.removerSerie(serie, true);
	}

	public List<Lancamento> extrairExtrato(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial, LancamentoStatus status) {
		return geradorExtratoLancamentos.execute(ano, mes, account, category, saldoInicial, status);
	}

	public List<Lancamento> extrairExtrato(int ano, int mes, Account account, Category category, LancamentoStatus status) {
		return geradorExtratoLancamentos.execute(ano, mes, account, category, status);
	}

	public List<Lancamento> extrairExtratoCartao(int ano, int mes, Account account, Category category) {
		return geradorExtratoCartao.execute(ano, mes, account, category);
	}

	public List<Lancamento> extrairExtratoCartao(int ano, int mes, Account account, Category category,
			BigDecimal saldoInicial) {
		return geradorExtratoCartao.execute(ano, mes, account, category, saldoInicial);
	}

}