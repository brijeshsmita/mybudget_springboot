package br.com.gestao.financeira.pessoal.conta.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoCartaoCredito;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.CriadorLancamentoCartaoCredito;
import br.com.gestao.financeira.pessoal.lancamento.rules.CriadorLancamentosIniciaisCartaoCredito;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemovedorLancamentoCartao;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;

@Stateless
public class CriadorContaCartao {

	@EJB
	private ValidadorDadosConta validadorDadosConta;

	@EJB
	private RemovedorLancamentosIniciaisCartao removedorLancamentosIniciaisCartao;

	@EJB
	private RemovedorLancamentoCartao removedorLancamentoCartao;

	@EJB
	private CriadorLancamentoCartaoCredito criadorLancamentoCartao;

	@EJB
	private CriadorLancamentosIniciaisCartaoCredito criadorLancamentosIniciaisCartaoCredito;

	@EJB
	private CredentialsStore credentialsStore;

	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ContaCartao save(ContaCartao conta, List<Lancamento> lancamentos)
			throws MesmoNomeExistenteException, ContaNotNullException, MesLancamentoAlteradoException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		int cartaoDiaFechmentoAnterior = conta.getCartaoDiaFechamentoAnterior() != null
				? conta.getCartaoDiaFechamentoAnterior().intValue()
				: conta.getCartaoDiaFechamento();
		int cartaoDiaPagamentoAnterior = conta.getCartaoDiaPagamentoAnterior() != null
				? conta.getCartaoDiaPagamentoAnterior().intValue()
				: conta.getCartaoDiaPagamento();

		validadorDadosConta.validar(conta);

		conta = em.merge(conta);

		removedorLancamentosIniciaisCartao.execute(conta);

		criadorLancamentosIniciaisCartaoCredito.save(conta, lancamentos);

		if (!((cartaoDiaFechmentoAnterior == conta.getCartaoDiaFechamento().intValue())
				&& (cartaoDiaPagamentoAnterior == conta.getCartaoDiaPagamento().intValue()))) {
			atualizarLancamentosAnteriores(conta);
		}

		return conta;
	}

	private void atualizarLancamentosAnteriores(ContaCartao conta)
			throws ContaNotNullException, MesLancamentoAlteradoException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		List<LancamentoCartaoCredito> lancamentosAnteriores = em
				.createNamedQuery(Lancamento.FIND_LANCAMENTO_CARTAO_QUERY, LancamentoCartaoCredito.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("serie", null)
				.setParameter("saldoInicial", false).setParameter("conta", conta).getResultList();

		for (Iterator<LancamentoCartaoCredito> iterator = lancamentosAnteriores.iterator(); iterator.hasNext();) {
			LancamentoCartaoCredito lancamentoCartaoCredito = iterator.next();
			removedorLancamentoCartao.remover(lancamentoCartaoCredito, false);
			LancamentoCartaoCredito lancamento = (LancamentoCartaoCredito) lancamentoCartaoCredito.clone();
			lancamento.setId(null);
			lancamento.setConta(conta);
			lancamento.setSaldoInicial(false);
			criadorLancamentoCartao.save(lancamento);
		}
	}

}
