package br.com.gestao.financeira.pessoal.lancamento.rules;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaCorrente;
import br.com.gestao.financeira.pessoal.conta.rules.AtualizadorSaldoConta;
import br.com.gestao.financeira.pessoal.infra.dao.DAO;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoContaCorrente;

@Stateless
public class CriadorLancamentoContaCorrente {

	@EJB
	CriadorSerieLancamento criadorSerieLancamento;

	@EJB
	AtualizadorSaldoConta atualizadorSaldoConta;

	@EJB
	RemovedorLancamentoContaCorrente removedorLancamento;

	@EJB
	DAO<Lancamento> lancamentoDao;

	@EJB
	DAO<Conta> contaDao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Lancamento save(Lancamento lancamento) throws MesLancamentoAlteradoException, ContaNotNullException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		return save(lancamento.getConta(), lancamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Lancamento save(Conta conta, Lancamento lancamento)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		lancamento.validar();

		if (((LancamentoContaCorrente) lancamento).isSaldoInicial()) {
			((ContaCorrente) conta).setSaldoInicial(lancamento.getValor());
			contaDao.merge(conta);
		}

		lancamento.setConta(conta);

		if (!lancamento.contaFoiAlterada()) {
			atualizadorSaldoConta.addSaldos(lancamento);
			lancamento = lancamentoDao.merge(lancamento);
		} else {
			Lancamento lancamentoAnterior = lancamento.getLancamentoAnterior();
			removedorLancamento.remover(lancamentoAnterior);

			lancamento.setContaAnterior(null);
			lancamento.setValorAnterior(BigDecimal.ZERO);
			lancamento.setDataAnterior(null);

			save(lancamento);
		}

		return lancamento;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Lancamento saveTransferencia(Lancamento lancamentoOrigem)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		Conta contaOrigem = ((LancamentoContaCorrente) lancamentoOrigem).getConta();
		Conta contaDestino = ((LancamentoContaCorrente) lancamentoOrigem).getContaDestino();

		((LancamentoContaCorrente) lancamentoOrigem).setContaOrigem(null);
		lancamentoOrigem = save(lancamentoOrigem);

		Lancamento lancamentoDestino = (Lancamento) lancamentoOrigem.clone();

		lancamentoDestino.setConta(contaDestino);
		((LancamentoContaCorrente) lancamentoDestino).setContaOrigem(contaOrigem);
		((LancamentoContaCorrente) lancamentoDestino).setContaDestino(null);

		lancamentoDestino.setInOut(InOut.E);

		lancamentoDestino.setContaAnterior(null);
		lancamentoDestino.setValorAnterior(null);
		lancamentoDestino.setDataAnterior(null);

		save(lancamentoDestino);

		return lancamentoOrigem;
	}

	public void setLancamentoDao(DAO<Lancamento> lancamentoDao) {
		this.lancamentoDao = lancamentoDao;
	}

	public void setAtualizadorSaldoConta(AtualizadorSaldoConta atualizadorSaldoConta) {
		this.atualizadorSaldoConta = atualizadorSaldoConta;
	}

	public void setContaDao(DAO<Conta> contaDao) {
		this.contaDao = contaDao;
	}

	public void setRemovedorLancamento(RemovedorLancamentoContaCorrente removedorLancamento) {
		this.removedorLancamento = removedorLancamento;
	}

}
