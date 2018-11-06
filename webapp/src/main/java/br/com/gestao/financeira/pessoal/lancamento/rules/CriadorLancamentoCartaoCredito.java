package br.com.gestao.financeira.pessoal.lancamento.rules;

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

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.conta.rules.AtualizadorSaldoConta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.infra.dao.DAO;
import br.com.gestao.financeira.pessoal.infra.dao.QueryParam;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoCartaoCredito;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoFaturaCartaoItem;

@Stateless
public class CriadorLancamentoCartaoCredito {

	@EJB
	private DAO<Lancamento> lancamentoDAO;

	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AtualizadorSaldoConta atualizadorSaldoConta;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Lancamento> save(Conta conta, List<Lancamento> lancamentos)
			throws ContaNotNullException, MesLancamentoAlteradoException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		List<Lancamento> lancamentosPersistidos = new ArrayList<Lancamento>();

		for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
			Lancamento lancamento = (Lancamento) iterator.next();
			lancamento.setConta(conta);
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

		atualizadorSaldoConta.addSaldos(faturas);

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
		Date dataPagamentoProximo = Date.from(((ContaCartao) lancamento.getConta())
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
				QueryParam.build("cartaoCreditoFatura", (ContaCartao) lancamento.getConta()),
				QueryParam.build("data", dataPrimeiraFatura));

		return ((ContaCartao) lancamento.getConta()).carregarFaturas(lancamento, faturasExistentes);
	}

	public void setLancamentoDAO(DAO<Lancamento> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}

	public void setAtualizadorSaldoConta(AtualizadorSaldoConta atualizadorSaldoConta) {
		this.atualizadorSaldoConta = atualizadorSaldoConta;
	}

}
