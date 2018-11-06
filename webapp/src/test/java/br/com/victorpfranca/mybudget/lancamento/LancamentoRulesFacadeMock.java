package br.com.victorpfranca.mybudget.lancamento;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.victorpfranca.mybudget.CredentialStoreMock;
import br.com.victorpfranca.mybudget.DAOMock;
import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountBalance;
import br.com.victorpfranca.mybudget.account.AtualizadorSaldoContaMock;
import br.com.victorpfranca.mybudget.account.ContaServiceMock;
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoCartaoCredito;
import br.com.victorpfranca.mybudget.lancamento.LancamentoContaCorrente;
import br.com.victorpfranca.mybudget.lancamento.LancamentoFaturaCartaoItem;
import br.com.victorpfranca.mybudget.lancamento.rules.AtualizadorFaturasCartao;
import br.com.victorpfranca.mybudget.lancamento.rules.CriadorLancamentoCartaoCredito;
import br.com.victorpfranca.mybudget.lancamento.rules.CriadorLancamentoContaCorrente;
import br.com.victorpfranca.mybudget.lancamento.rules.LancamentoRulesFacade;
import br.com.victorpfranca.mybudget.lancamento.rules.RemovedorLancamentoCartao;
import br.com.victorpfranca.mybudget.lancamento.rules.RemovedorLancamentoContaCorrente;
import br.com.victorpfranca.mybudget.lancamento.rules.RemovedorLancamentoFaturaItem;

public class LancamentoRulesFacadeMock extends LancamentoRulesFacade {

	private DAO<Account> contaDAO;
	private DAO<Lancamento> lancamentoDAO;
	private DAO<AccountBalance> saldoContaDao;
	private CredentialsStore credentialsStore;

	public LancamentoRulesFacadeMock() {
		initDAOs();

		credentialsStore = new CredentialStoreMock();

		AccountBalanceUpdater accountBalanceUpdater = new AtualizadorSaldoContaMock();
		accountBalanceUpdater.setCredentialsStore(credentialsStore);
		accountBalanceUpdater.setSaldoContaDao(saldoContaDao);

		configurarRemovedor(accountBalanceUpdater);

		initCriadores(accountBalanceUpdater);
	}

	private void initDAOs() {
		saldoContaDao = new SaldoContaDAOMock();
		lancamentoDAO = new LancamentoDAOMock();
		contaDAO = new DAOMock<Account>();
	}

	private void initCriadores(AccountBalanceUpdater accountBalanceUpdater) {
		criadorLancamentoContaCorrente = new CriadorLancamentoContaCorrente();
		criadorLancamentoContaCorrente.setAtualizadorSaldoConta(accountBalanceUpdater);
		criadorLancamentoContaCorrente.setLancamentoDao(lancamentoDAO);
		criadorLancamentoContaCorrente.setRemovedorLancamento(removedorLancamento);
		criadorLancamentoContaCorrente.setContaDao(contaDAO);

		criadorLancamentoCartaoCredito = new CriadorLancamentoCartaoCredito();
		criadorLancamentoCartaoCredito.setAtualizadorSaldoConta(accountBalanceUpdater);
		criadorLancamentoCartaoCredito.setLancamentoDAO(lancamentoDAO);
	}

	private void configurarRemovedor(AccountBalanceUpdater accountBalanceUpdater) {
		removedorLancamento = new RemovedorLancamentoContaCorrente();
		removedorLancamento.setAtualizadorSaldoConta(accountBalanceUpdater);
		removedorLancamento.setLancamentoDAO(lancamentoDAO);

		removedorLancamentoCartao = new RemovedorLancamentoCartao();
		removedorLancamentoCartao.setAtualizadorSaldoConta(accountBalanceUpdater);
		removedorLancamentoCartao.setLancamentoDAO(lancamentoDAO);

		AtualizadorFaturasCartao atualizadorFaturasCartao = new AtualizadorFaturasCartao();
		atualizadorFaturasCartao.setLancamentoDAO(lancamentoDAO);
		atualizadorFaturasCartao.setCredentialsStore(credentialsStore);
		removedorLancamentoCartao.setAtualizadorFaturasCartao(atualizadorFaturasCartao);

		RemovedorLancamentoFaturaItem removedorLancamentoFaturaItem = new RemovedorLancamentoFaturaItem();
		removedorLancamentoFaturaItem.setLancamentoDAO(lancamentoDAO);
		removedorLancamentoFaturaItem.setCredentialsStore(credentialsStore);
		removedorLancamentoCartao.setRemovedorLancamentoFaturaItem(removedorLancamentoFaturaItem);
	}

	static LancamentoRulesFacadeMock build() {
		return new LancamentoRulesFacadeMock();
	}

	public List<AccountBalance> getSaldos() {
		return saldoContaDao.findAll();
	}

	public List<Lancamento> getLancamentos() {
		return lancamentoDAO.findAll();
	}

	public List<LancamentoCartaoCredito> getLancamentosCartao() {
		List<LancamentoCartaoCredito> lancamentosCartao = new ArrayList<LancamentoCartaoCredito>();

		List<Lancamento> lancamentos = lancamentoDAO.findAll();
		for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
			Lancamento lancamento = iterator.next();
			if (lancamento instanceof LancamentoCartaoCredito) {
				lancamentosCartao.add((LancamentoCartaoCredito) lancamento);
			}

		}
		return lancamentosCartao;
	}

	public List<LancamentoContaCorrente> getLancamentosFaturas() {
		List<LancamentoContaCorrente> faturas = new ArrayList<LancamentoContaCorrente>();

		List<Lancamento> lancamentos = lancamentoDAO.findAll();
		for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
			Lancamento lancamento = iterator.next();
			if (lancamento instanceof LancamentoContaCorrente
					&& ((LancamentoContaCorrente) lancamento).isFaturaCartao()) {
				faturas.add((LancamentoContaCorrente) lancamento);
			}

		}
		return faturas;
	}

	public List<LancamentoFaturaCartaoItem> getLancamentosItensFatura() {
		List<LancamentoFaturaCartaoItem> lancamentosFaturaCartaoItem = new ArrayList<LancamentoFaturaCartaoItem>();

		List<Lancamento> lancamentos = lancamentoDAO.findAll();
		for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
			Lancamento lancamento = iterator.next();
			if (lancamento instanceof LancamentoFaturaCartaoItem) {
				lancamentosFaturaCartaoItem.add((LancamentoFaturaCartaoItem) lancamento);
			}

		}
		return lancamentosFaturaCartaoItem;
	}

}
