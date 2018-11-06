package br.com.victorpfranca.mybudget.account.rules;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.account.CheckingAccount;
import br.com.victorpfranca.mybudget.transaction.rules.CategoriasIncompativeisException;
import br.com.victorpfranca.mybudget.transaction.rules.ContaNotNullException;
import br.com.victorpfranca.mybudget.transaction.rules.CriadorLancamentoContaCorrente;
import br.com.victorpfranca.mybudget.transaction.rules.MesLancamentoAlteradoException;
import br.com.victorpfranca.mybudget.transaction.rules.TipoContaException;
import br.com.victorpfranca.mybudget.transaction.rules.ValorLancamentoInvalidoException;

@Stateless
public class CheckingAccountBuilder {

	@EJB
	private AccountDataValidator accountDataValidator;

	@EJB
	private CheckingAccountInitialBalanceRemover checkingAccountInitialBalanceRemover;

	@EJB
	private CriadorLancamentoContaCorrente criadorLancamentoContaCorrente;

	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CheckingAccount save(CheckingAccount conta)
			throws SameNameException, MesLancamentoAlteradoException, ContaNotNullException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		accountDataValidator.validar(conta);

		conta = em.merge(conta);
		
		checkingAccountInitialBalanceRemover.execute(conta);
		
		criadorLancamentoContaCorrente.save(conta, conta.buildLancamentoSaldoInicial());

		return conta;
	}

}
