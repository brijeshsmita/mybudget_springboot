package br.com.gestao.financeira.pessoal.conta.rules;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.conta.ContaCorrente;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.CriadorLancamentoContaCorrente;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;

@Stateless
public class CriadorContaCorrente {

	@EJB
	private ValidadorDadosConta validadorDadosConta;

	@EJB
	private RemovedorSaldoInicialContaCorrente removedorSaldoInicialContaCorrente;

	@EJB
	private CriadorLancamentoContaCorrente criadorLancamentoContaCorrente;

	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ContaCorrente save(ContaCorrente conta)
			throws MesmoNomeExistenteException, MesLancamentoAlteradoException, ContaNotNullException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		validadorDadosConta.validar(conta);

		conta = em.merge(conta);
		
		removedorSaldoInicialContaCorrente.execute(conta);
		
		criadorLancamentoContaCorrente.save(conta, conta.buildLancamentoSaldoInicial());

		return conta;
	}

}
