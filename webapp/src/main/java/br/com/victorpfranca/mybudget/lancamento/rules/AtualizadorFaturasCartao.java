package br.com.victorpfranca.mybudget.lancamento.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.victorpfranca.mybudget.InOut;
import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.infra.dao.DAO;
import br.com.victorpfranca.mybudget.infra.dao.QueryParam;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.LancamentoContaCorrente;
import br.com.victorpfranca.mybudget.lancamento.LancamentoFaturaCartaoItem;

@Stateless
public class AtualizadorFaturasCartao {

	@EJB
	private DAO<Lancamento> lancamentoDAO;

	@EJB
	private CredentialsStore credentialsStore;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void abater(List<Lancamento> faturasItens) {

		if (faturasItens.isEmpty())
			return;

		CreditCardAccount creditCardAccount = (CreditCardAccount) ((LancamentoFaturaCartaoItem) faturasItens.get(0)).getLancamentoCartao()
				.getAccount();

		List<Lancamento> faturas = lancamentoDAO.executeQuery(Lancamento.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY,
				new QueryParam("user", credentialsStore.recuperarIdUsuarioLogado()),
				new QueryParam("cartaoCreditoFatura", creditCardAccount), new QueryParam("faturaCartao", true),
				new QueryParam("saldoInicial", null), new QueryParam("ano", null), new QueryParam("mes", null), new QueryParam("account", null),
				new QueryParam("category", null), new QueryParam("status", null));

		for (Iterator<Lancamento> iterator = faturasItens.iterator(); iterator.hasNext();) {
			LancamentoFaturaCartaoItem faturaItem = (LancamentoFaturaCartaoItem) iterator.next();

			LancamentoContaCorrente fatura = getFaturaReferente(faturas, faturaItem);
			if (fatura != null) {
				if(faturaItem.getInOut().equals(InOut.E))
					fatura.setValor(fatura.getValor().subtract(faturaItem.getValor()));
				else fatura.setValor(fatura.getValor().add(faturaItem.getValor()));
				lancamentoDAO.merge(fatura);
			}
		}

	}

	private LancamentoContaCorrente getFaturaReferente(List<Lancamento> faturas,
			LancamentoFaturaCartaoItem faturaItem) {

		for (Iterator<Lancamento> iterator = faturas.iterator(); iterator.hasNext();) {
			LancamentoContaCorrente fatura = (LancamentoContaCorrente) iterator.next();
			if (fatura.getAno().compareTo(faturaItem.getAno()) == 0
					&& fatura.getMes().compareTo(faturaItem.getMes()) == 0) {
				return fatura;
			}
		}

		return null;
	}
	
	public void setLancamentoDAO(DAO<Lancamento> lancamentoDAO) {
		this.lancamentoDAO = lancamentoDAO;
	}
	
	public void setCredentialsStore(CredentialsStore credentialsStore) {
		this.credentialsStore = credentialsStore;
	}

}
