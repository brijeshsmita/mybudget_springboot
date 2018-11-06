package br.com.gestao.financeira.pessoal.lancamento;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.gestao.financeira.pessoal.CredentialStoreMock;
import br.com.gestao.financeira.pessoal.DAOMock;
import br.com.gestao.financeira.pessoal.conta.AtualizadorSaldoContaMock;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaServiceMock;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.conta.rules.AtualizadorSaldoConta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.infra.dao.DAO;
import br.com.gestao.financeira.pessoal.lancamento.rules.AtualizadorFaturasCartao;
import br.com.gestao.financeira.pessoal.lancamento.rules.CriadorLancamentoCartaoCredito;
import br.com.gestao.financeira.pessoal.lancamento.rules.CriadorLancamentoContaCorrente;
import br.com.gestao.financeira.pessoal.lancamento.rules.LancamentoRulesFacade;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemovedorLancamentoCartao;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemovedorLancamentoContaCorrente;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemovedorLancamentoFaturaItem;

public class LancamentoRulesFacadeMock extends LancamentoRulesFacade {

	private DAO<Conta> contaDAO;
	private DAO<Lancamento> lancamentoDAO;
	private DAO<SaldoConta> saldoContaDao;
	private CredentialsStore credentialsStore;

	public LancamentoRulesFacadeMock() {
		initDAOs();

		credentialsStore = new CredentialStoreMock();

		AtualizadorSaldoConta atualizadorSaldoConta = new AtualizadorSaldoContaMock();
		atualizadorSaldoConta.setCredentialsStore(credentialsStore);
		atualizadorSaldoConta.setSaldoContaDao(saldoContaDao);

		configurarRemovedor(atualizadorSaldoConta);

		initCriadores(atualizadorSaldoConta);
	}

	private void initDAOs() {
		saldoContaDao = new SaldoContaDAOMock();
		lancamentoDAO = new LancamentoDAOMock();
		contaDAO = new DAOMock<Conta>();
	}

	private void initCriadores(AtualizadorSaldoConta atualizadorSaldoConta) {
		criadorLancamentoContaCorrente = new CriadorLancamentoContaCorrente();
		criadorLancamentoContaCorrente.setAtualizadorSaldoConta(atualizadorSaldoConta);
		criadorLancamentoContaCorrente.setLancamentoDao(lancamentoDAO);
		criadorLancamentoContaCorrente.setRemovedorLancamento(removedorLancamento);
		criadorLancamentoContaCorrente.setContaDao(contaDAO);

		criadorLancamentoCartaoCredito = new CriadorLancamentoCartaoCredito();
		criadorLancamentoCartaoCredito.setAtualizadorSaldoConta(atualizadorSaldoConta);
		criadorLancamentoCartaoCredito.setLancamentoDAO(lancamentoDAO);
	}

	private void configurarRemovedor(AtualizadorSaldoConta atualizadorSaldoConta) {
		removedorLancamento = new RemovedorLancamentoContaCorrente();
		removedorLancamento.setAtualizadorSaldoConta(atualizadorSaldoConta);
		removedorLancamento.setLancamentoDAO(lancamentoDAO);

		removedorLancamentoCartao = new RemovedorLancamentoCartao();
		removedorLancamentoCartao.setAtualizadorSaldoConta(atualizadorSaldoConta);
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

	public List<SaldoConta> getSaldos() {
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
