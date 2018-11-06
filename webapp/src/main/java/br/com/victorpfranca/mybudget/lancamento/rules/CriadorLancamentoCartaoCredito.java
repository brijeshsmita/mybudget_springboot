package br.com.victorpfranca.mybudget.lancamento.rules;

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
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoCartaoCredito;
import br.com.victorpfranca.mybudget.lancamento.LancamentoFaturaCartaoItem;

@Stateless
public class CriadorLancamentoCartaoCredito {

	@EJB
	private DAO<Lancamento> lancamentoDAO;

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Lancamento> save(Account account, List<Lancamento> lancamentos)
			throws ContaNotNullException, MesLancamentoAlteradoException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		List<Lancamento> lancamentosPersistidos = new ArrayList<Lancamento>();

		for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
			Lancamento lancamento = (Lancamento) iterator.next();
			lancamento.setAccount(account);
			lancamentosPersistidos.add(save(lancamento));
		}

		return lancamentosPersistidos;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Lancamento save(Lancamento lancamento) throws ContaNotNullException, MesLancamentoAlteradoException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		lancamento.validar();

		lancamento = lancamentoDAO.merge(lancamento);

		List<Lancamento> faturas = atualizarFaturas((LancamentoCartaoCredito) lancamento);

		accountBalanceUpdater.addSaldos(faturas);

		atualizarFaturaItem(lancamento, faturas);

		return lancamento;
	}

	protected List<Lancamento> atualizarFaturas(LancamentoCartaoCredito lancamentoCartao) throws ContaNotNullException {
		Date dataPagamentoProximo = getDataPrimeiraFatura(lancamentoCartao);

		List<Lancamento> faturas = carregarFaturas(lancamentoCartao, dataPagamentoProximo);
		for (Iterator<Lancamento> iterator = faturas.iterator(); iterator.hasNext();) {
			Lancamento fatura = (Lancamento) iterator.next();
			BigDecimal valorAnterior = fatura.getId() == null ? BigDecimal.ZERO : fatura.getValorAnterior();
			lancamentoDAO.merge(fatura);
			fatura.setValorAnterior(valorAnterior);
		}

		return faturas;
	}

	protected Date getDataPrimeiraFatura(LancamentoCartaoCredito lancamento) {
		Date dataPagamentoProximo = Date.from(((CreditCardAccount) lancamento.getAccount())
				.getDataPagamentoProximo(lancamento.getData()).atStartOfDay(ZoneId.systemDefault()).toInstant());
		return dataPagamentoProximo;
	}

	protected void atualizarFaturaItem(Lancamento lancamento, List<Lancamento> faturas) {
		int indiceParcela = 1;
		for (Iterator<Lancamento> iterator = faturas.iterator(); iterator.hasNext();) {
			Lancamento fatura = (Lancamento) iterator.next();
			LancamentoFaturaCartaoItem faturaItem = ((LancamentoCartaoCredito) lancamento)
					.buildFaturaItem(fatura.getData(), fatura.getAno(), fatura.getMes(), indiceParcela++);
			faturaItem.setLancamentoCartao((LancamentoCartaoCredito) lancamento);
			faturaItem.setSaldoInicial(lancamento.isSaldoInicial());
			lancamentoDAO.merge(faturaItem);
		}
	}

	protected List<Lancamento> carregarFaturas(LancamentoCartaoCredito lancamento, Date dataPrimeiraFatura)
			throws ContaNotNullException {

		List<Lancamento> faturasExistentes = lancamentoDAO.executeQuery(Lancamento.FIND_LANCAMENTO_FATURA_QUERY,
				QueryParam.build("cartaoCreditoFatura", (CreditCardAccount) lancamento.getAccount()),
				QueryParam.build("data", dataPrimeiraFatura));

		return ((CreditCardAccount) lancamento.getAccount()).carregarFaturas(lancamento, faturasExistentes);
	}

	public void setLancamentoDAO(DAO<Lancamento> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}

	public void setAtualizadorSaldoConta(AccountBalanceUpdater accountBalanceUpdater) {
		this.accountBalanceUpdater = accountBalanceUpdater;
	}

}
