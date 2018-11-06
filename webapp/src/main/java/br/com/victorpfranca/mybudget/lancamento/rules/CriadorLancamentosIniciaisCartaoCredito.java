package br.com.victorpfranca.mybudget.lancamento.rules;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.account.rules.AccountBalanceUpdater;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoCartaoCredito;

@Stateless
public class CriadorLancamentosIniciaisCartaoCredito extends CriadorLancamentoCartaoCredito {

	@Inject
	private EntityManager em;
	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AccountBalanceUpdater accountBalanceUpdater;

	@Override
	protected Date getDataPrimeiraFatura(LancamentoCartaoCredito lancamento) {
		return lancamento.getData();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	protected List<Lancamento> carregarFaturas(LancamentoCartaoCredito lancamento, Date dataPrimeiraFatura)
			throws ContaNotNullException {
		List<Lancamento> faturasExistentes = em
				.createNamedQuery(Lancamento.FIND_LANCAMENTO_FATURA_QUERY, Lancamento.class)
				.setParameter("cartaoCreditoFatura", (CreditCardAccount) lancamento.getAccount())
				.setParameter("data", dataPrimeiraFatura).getResultList();

		return ((CreditCardAccount) lancamento.getAccount()).carregarFaturasAPartirDe(lancamento, faturasExistentes,
				lancamento.getData());
	}

}
