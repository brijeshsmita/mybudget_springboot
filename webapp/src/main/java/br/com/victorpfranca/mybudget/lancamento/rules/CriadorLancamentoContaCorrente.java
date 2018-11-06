package br.com.victorpfranca.mybudget.lancamento.rules;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoContaCorrente;

@Stateless
public class CriadorLancamentoContaCorrente {

	@EJB
	CriadorSerieLancamento criadorSerieLancamento;

	@EJB
	AccountBalanceUpdater accountBalanceUpdater;

	@EJB
	RemovedorLancamentoContaCorrente removedorLancamento;

	@EJB
	DAO<Lancamento> lancamentoDao;

	@EJB
	DAO<Account> contaDao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Lancamento save(Lancamento lancamento) throws MesLancamentoAlteradoException, ContaNotNullException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		return save(lancamento.getAccount(), lancamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Lancamento save(Account account, Lancamento lancamento)
			throws MesLancamentoAlteradoException, ContaNotNullException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		lancamento.validar();

		if (((LancamentoContaCorrente) lancamento).isSaldoInicial()) {
			((CheckingAccount) account).setSaldoInicial(lancamento.getValor());
			contaDao.merge(account);
		}

		lancamento.setAccount(account);

		if (!lancamento.contaFoiAlterada()) {
			accountBalanceUpdater.addSaldos(lancamento);
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

		Account contaOrigem = ((LancamentoContaCorrente) lancamentoOrigem).getAccount();
		Account contaDestino = ((LancamentoContaCorrente) lancamentoOrigem).getContaDestino();

		((LancamentoContaCorrente) lancamentoOrigem).setContaOrigem(null);
		lancamentoOrigem = save(lancamentoOrigem);

		Lancamento lancamentoDestino = (Lancamento) lancamentoOrigem.clone();

		lancamentoDestino.setAccount(contaDestino);
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

	public void setAtualizadorSaldoConta(AccountBalanceUpdater accountBalanceUpdater) {
		this.accountBalanceUpdater = accountBalanceUpdater;
	}

	public void setContaDao(DAO<Account> contaDao) {
		this.contaDao = contaDao;
	}

	public void setRemovedorLancamento(RemovedorLancamentoContaCorrente removedorLancamento) {
		this.removedorLancamento = removedorLancamento;
	}

}
