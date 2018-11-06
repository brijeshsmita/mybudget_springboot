package br.com.gestao.financeira.pessoal.conta.rules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.conta.ContaCorrente;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoCartaoCredito;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoContaCorrente;
import br.com.gestao.financeira.pessoal.lancamento.extractors.saldofuturo.GeradorSaldoFuturo;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemocaoNaoPermitidaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;
import br.com.gestao.financeira.pessoal.periodo.PeriodoPlanejamento;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ContaService {

	@EJB
	private CriadorContaCorrente criadorContaCorrente;

	@EJB
	private CriadorContaCartao criadorContaCartao;

	@EJB
	private RemovedorContaCorrente removedorContaCorrente;

	@EJB
	private RemovedorContaCartao removedorContaCartao;

	@EJB
	private ReconstrutorSaldosContas reconstrutorSaldosContas;

	@EJB
	private PeriodoPlanejamento periodoPlanejamento;

	@EJB
	protected GeradorSaldoFuturo geradorSaldoFuturo;

	@Inject
	private EntityManager em;

	@EJB
	private CredentialsStore credentialsStore;

	public List<Conta> findAll() {
		return em.createNamedQuery(Conta.FIND_ALL_QUERY, Conta.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).getResultList();
	}

	public Conta find(Integer id) {
		return em.find(Conta.class, id);
	}

	public List<Conta> findContasCorrentes() {
		List<Conta> contasCorrentes = new ArrayList<Conta>();
		contasCorrentes.addAll(findContasBancos());
		contasCorrentes.addAll(findContasDinheiro());
		return contasCorrentes;
	}

	public List<Conta> findContasBancos() {
		return em.createNamedQuery(Conta.FIND_ALL_CONTA_BANCO_QUERY, Conta.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).getResultList();
	}

	public List<Conta> findContasDinheiro() {
		return em.createNamedQuery(Conta.FIND_ALL_CONTA_DINHEIRO_QUERY, Conta.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).getResultList();
	}

	public List<Conta> findContasCartoes() {
		return em.createNamedQuery(Conta.FIND_ALL_CONTA_CARTOES_QUERY, Conta.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).getResultList();
	}

	public List<LancamentoCartaoCredito> findLancamentosIniciaisCartao(ContaCartao conta) {
		return em.createNamedQuery(Lancamento.FIND_LANCAMENTO_INICIAL_CARTAO_QUERY, LancamentoCartaoCredito.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("conta", conta)
				.getResultList();
	}

	public List<LancamentoContaCorrente> findFaturas(ContaCartao conta) {
		return em.createNamedQuery(Lancamento.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, LancamentoContaCorrente.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado())
				.setParameter("cartaoCreditoFatura", conta).setParameter("faturaCartao", true)
				.setParameter("saldoInicial", null).setParameter("ano", null).setParameter("mes", null)
				.setParameter("conta", null).setParameter("categoria", null).setParameter("status", null).getResultList();
	}

	public BigDecimal getSaldosContasCorrentesAte(Integer ano, Integer mes) {

		List<Conta> contas = findContasCorrentes();

		BigDecimal saldo = BigDecimal.ZERO;
		for (Iterator<Conta> iterator = contas.iterator(); iterator.hasNext();) {
			Conta conta = iterator.next();
			saldo = saldo.add(getSaldoAte(conta, ano, mes));
		}

		return saldo;
	}

	public BigDecimal getSaldoAte(Integer conta, Integer ano, Integer mes) {
		return getSaldoAte(em.find(Conta.class, conta), ano, mes);
	}

	public BigDecimal getSaldoAte(Conta conta, Integer ano, Integer mes) {
		List<SaldoConta> saldos = em.createNamedQuery(SaldoConta.FIND_UNTIL_ANO_MES_QUERY, SaldoConta.class)
				.setParameter("usuario", credentialsStore.recuperarUsuarioLogado()).setParameter("conta", conta)
				.setParameter("ano", ano).setParameter("mes", mes).setMaxResults(1).getResultList();

		if (!saldos.isEmpty()) {
			return saldos.get(0).getValor();
		}

		return BigDecimal.ZERO;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Conta> saveContasCorrente(List<Conta> contas)
			throws MesmoNomeExistenteException, ContaNotNullException, MesLancamentoAlteradoException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		List<Conta> contasGravadas = new ArrayList<Conta>();
		for (Iterator<Conta> iterator = contas.iterator(); iterator.hasNext();) {
			Conta conta = iterator.next();
			contasGravadas.add(saveContaCorrente(conta));
		}
		return contasGravadas;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Conta saveContaCorrente(Conta conta)
			throws MesmoNomeExistenteException, ContaNotNullException, MesLancamentoAlteradoException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		conta = criadorContaCorrente.save((ContaCorrente) conta);

		return conta;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveContasCartoes(List<Conta> contas)
			throws MesmoNomeExistenteException, ContaNotNullException, MesLancamentoAlteradoException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {
		for (Iterator<Conta> iterator = contas.iterator(); iterator.hasNext();) {
			Conta conta = iterator.next();
			saveContaCartao(conta, new ArrayList<Lancamento>());
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Conta saveContaCartao(Conta conta, List<Lancamento> lancamentos)
			throws MesmoNomeExistenteException, ContaNotNullException, MesLancamentoAlteradoException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		if (conta instanceof ContaCorrente) {
			conta = criadorContaCorrente.save((ContaCorrente) conta);
		} else if (conta instanceof ContaCartao) {
			conta = criadorContaCartao.save((ContaCartao) conta, lancamentos);
		}

		return conta;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Conta conta) throws RemocaoNaoPermitidaException, NaoRemovivelException {

		if (conta instanceof ContaCorrente) {
			removedorContaCorrente.remover((ContaCorrente) conta);
		} else if (conta instanceof ContaCartao) {
			removedorContaCartao.remover((ContaCartao) conta);
		}

	}

	public Map<Conta, Map<AnoMes, SaldoConta>> reconstruirSaldosContas() {
		return reconstrutorSaldosContas.reconstruirSaldosContasDoInicio();
	}

	public List<SaldoConta> carregarSaldoFuturoPrevisto() {
		AnoMes anoMesAtual = periodoPlanejamento.getMesAtual();
		AnoMes anoMesFinal = periodoPlanejamento.getMesFinal();

		return carregarSaldoFuturoPrevisto(anoMesAtual, anoMesFinal);
	}

	public List<SaldoConta> carregarSaldoFuturoPrevisto(AnoMes anoMesAtual, AnoMes anoMesFinal) {
		return geradorSaldoFuturo.execute(anoMesAtual.getAno(), anoMesAtual.getMes(), anoMesFinal.getAno(),
				anoMesFinal.getMes());
	}

}
