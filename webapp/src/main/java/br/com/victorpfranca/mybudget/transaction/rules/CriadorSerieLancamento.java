package br.com.victorpfranca.mybudget.transaction.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionSerie;

@Stateless
public class CriadorSerieLancamento {

	@EJB
	protected CriadorLancamentoContaCorrente criadorLancamentoContaCorrente;

	@EJB
	protected CriadorLancamentoCartaoCredito criadorLancamentoCartaoCredito;

	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TransactionSerie saveLancamentoContaCorrente(Transaction transaction)
			throws ContaNotNullException, CategoriasIncompativeisException, MesLancamentoAlteradoException,
			TipoContaException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {

		TransactionSerie serie = save(transaction);
		List<Transaction> lancamentosItens = serie.gerarLancamentos(transaction);

		for (Iterator<Transaction> iterator = lancamentosItens.iterator(); iterator.hasNext();) {
			Transaction lancamentoItem = (Transaction) iterator.next();
			criadorLancamentoContaCorrente.save(lancamentoItem);
		}
		return serie;

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TransactionSerie saveLancamentoCartaoCredito(Transaction transaction)
			throws ContaNotNullException, CategoriasIncompativeisException, MesLancamentoAlteradoException,
			TipoContaException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {

		TransactionSerie serie = save(transaction);

		List<Transaction> lancamentosItens = serie.gerarLancamentos(transaction);

		for (Iterator<Transaction> iterator = lancamentosItens.iterator(); iterator.hasNext();) {
			Transaction lancamentoItem = (Transaction) iterator.next();
			criadorLancamentoCartaoCredito.save(lancamentoItem);
		}
		return serie;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TransactionSerie saveTransferencia(Transaction transaction)
			throws ContaNotNullException, CategoriasIncompativeisException, MesLancamentoAlteradoException,
			TipoContaException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {

		TransactionSerie serie = save(transaction);

		List<Transaction> lancamentosItens = serie.gerarLancamentos(transaction);

		for (Iterator<Transaction> iterator = lancamentosItens.iterator(); iterator.hasNext();) {
			Transaction lancamentoItem = (Transaction) iterator.next();
			criadorLancamentoContaCorrente.saveTransferencia((lancamentoItem));
		}
		return serie;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private TransactionSerie save(Transaction transaction) throws ContaNotNullException, CategoriasIncompativeisException,
			MesLancamentoAlteradoException, TipoContaException, ValorLancamentoInvalidoException,
			DataSerieLancamentoInvalidaException {

		validarSerieLancamento(transaction);

		removerSerieLancamentos(transaction);

		// Restrição: Apenas salva nova série, não atualizando seus dados
		// básicos(frequencia, data, etc) nos casos em que este método esteja sendo
		// invocado com objetivo de alteração de dados, pois haveria uma
		// falha na consitência dos dados se a data de início fosse alterada para mês
		// posterior. Se isto ocorresse, haveria remoção de todos os lançamentos da
		// série, mas o tratamento de atualização de saldos seria aplicado apenas à
		// partir do novo mês de início da série. Outros impactos não conhecidos também
		// podem ocorrer.
		TransactionSerie serie = transaction.getSerie();
		if (serie.getId() == null) {
			serie = em.merge(transaction.getSerie());
			transaction.setSerie(serie);
		}

		return serie;
	}

	private void removerSerieLancamentos(Transaction transaction) {
		if (transaction.getSerie().getId() != null) {
			em.createNamedQuery(Transaction.REMOVE_BY_SERIE_QUERY).setParameter("serie", transaction.getSerie())
					.executeUpdate();
		}
	}

	private void validarSerieLancamento(Transaction transaction) throws DataSerieLancamentoInvalidaException {
		if (transaction.getSerie() == null)
			throw new IllegalArgumentException();
		
		transaction.getSerie().validarDatas();
	}

}
