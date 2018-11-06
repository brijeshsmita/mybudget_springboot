package br.com.gestao.financeira.pessoal.lancamento.rules;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.conta.rules.AtualizadorSaldoConta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoCartaoCredito;

@Stateless
public class CriadorLancamentosIniciaisCartaoCredito extends CriadorLancamentoCartaoCredito {

	@Inject
	private EntityManager em;
	@EJB
	private CredentialsStore credentialsStore;

	@EJB
	private AtualizadorSaldoConta atualizadorSaldoConta;

	@Override
	protected Date getDataPrimeiraFatura(LancamentoCartaoCredito lancamento) {
		return lancamento.getData();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	protected List<Lancamento> carregarFaturas(LancamentoCartaoCredito lancamento, Date dataPrimeiraFatura)
			throws ContaNotNullException {
		List<Lancamento> faturasExistentes = em
				.createNamedQuery(Lancamento.FIND_LANCAMENTO_FATURA_QUERY, Lancamento.class)
				.setParameter("cartaoCreditoFatura", (ContaCartao) lancamento.getConta())
				.setParameter("data", dataPrimeiraFatura).getResultList();

		return ((ContaCartao) lancamento.getConta()).carregarFaturasAPartirDe(lancamento, faturasExistentes,
				lancamento.getData());
	}

}
