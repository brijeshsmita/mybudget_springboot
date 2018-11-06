package br.com.victorpfranca.mybudget.account.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoCartaoCredito;
import br.com.victorpfranca.mybudget.lancamento.rules.CategoriasIncompativeisException;
import br.com.victorpfranca.mybudget.lancamento.rules.ContaNotNullException;
import br.com.victorpfranca.mybudget.lancamento.rules.CriadorLancamentoCartaoCredito;
import br.com.victorpfranca.mybudget.lancamento.rules.CriadorLancamentosIniciaisCartaoCredito;
import br.com.victorpfranca.mybudget.lancamento.rules.MesLancamentoAlteradoException;
import br.com.victorpfranca.mybudget.lancamento.rules.RemovedorLancamentoCartao;
import br.com.victorpfranca.mybudget.lancamento.rules.TipoContaException;
import br.com.victorpfranca.mybudget.lancamento.rules.ValorLancamentoInvalidoException;

@Stateless
public class CreditCardAccountBuilder {

	@EJB
	private AccountDataValidator accountDataValidator;

	@EJB
	private CreditCardInitialTransactionsRemover creditCardInitialTransactionsRemover;

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
	public CreditCardAccount save(CreditCardAccount conta, List<Lancamento> lancamentos)
			throws SameNameException, ContaNotNullException, MesLancamentoAlteradoException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		int cartaoDiaFechmentoAnterior = conta.getCartaoDiaFechamentoAnterior() != null
				? conta.getCartaoDiaFechamentoAnterior().intValue()
				: conta.getCartaoDiaFechamento();
		int cartaoDiaPagamentoAnterior = conta.getCartaoDiaPagamentoAnterior() != null
				? conta.getCartaoDiaPagamentoAnterior().intValue()
				: conta.getCartaoDiaPagamento();

		accountDataValidator.validar(conta);

		conta = em.merge(conta);

		creditCardInitialTransactionsRemover.execute(conta);

		criadorLancamentosIniciaisCartaoCredito.save(conta, lancamentos);

		if (!((cartaoDiaFechmentoAnterior == conta.getCartaoDiaFechamento().intValue())
				&& (cartaoDiaPagamentoAnterior == conta.getCartaoDiaPagamento().intValue()))) {
			atualizarLancamentosAnteriores(conta);
		}

		return conta;
	}

	private void atualizarLancamentosAnteriores(CreditCardAccount conta)
			throws ContaNotNullException, MesLancamentoAlteradoException, TipoContaException,
			CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		List<LancamentoCartaoCredito> lancamentosAnteriores = em
				.createNamedQuery(Lancamento.FIND_LANCAMENTO_CARTAO_QUERY, LancamentoCartaoCredito.class)
				.setParameter("user", credentialsStore.recuperarIdUsuarioLogado()).setParameter("serie", null)
				.setParameter("saldoInicial", false).setParameter("account", conta).getResultList();

		for (Iterator<LancamentoCartaoCredito> iterator = lancamentosAnteriores.iterator(); iterator.hasNext();) {
			LancamentoCartaoCredito lancamentoCartaoCredito = iterator.next();
			removedorLancamentoCartao.remover(lancamentoCartaoCredito, false);
			LancamentoCartaoCredito lancamento = (LancamentoCartaoCredito) lancamentoCartaoCredito.clone();
			lancamento.setId(null);
			lancamento.setAccount(conta);
			lancamento.setSaldoInicial(false);
			criadorLancamentoCartao.save(lancamento);
		}
	}

}
