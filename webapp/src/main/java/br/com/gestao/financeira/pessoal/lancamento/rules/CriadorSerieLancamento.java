package br.com.gestao.financeira.pessoal.lancamento.rules;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.SerieLancamento;

@Stateless
public class CriadorSerieLancamento {

	@EJB
	protected CriadorLancamentoContaCorrente criadorLancamentoContaCorrente;

	@EJB
	protected CriadorLancamentoCartaoCredito criadorLancamentoCartaoCredito;

	@Inject
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SerieLancamento saveLancamentoContaCorrente(Lancamento lancamento)
			throws ContaNotNullException, CategoriasIncompativeisException, MesLancamentoAlteradoException,
			TipoContaException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {

		SerieLancamento serie = save(lancamento);
		List<Lancamento> lancamentosItens = serie.gerarLancamentos(lancamento);

		for (Iterator<Lancamento> iterator = lancamentosItens.iterator(); iterator.hasNext();) {
			Lancamento lancamentoItem = (Lancamento) iterator.next();
			criadorLancamentoContaCorrente.save(lancamentoItem);
		}
		return serie;

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SerieLancamento saveLancamentoCartaoCredito(Lancamento lancamento)
			throws ContaNotNullException, CategoriasIncompativeisException, MesLancamentoAlteradoException,
			TipoContaException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {

		SerieLancamento serie = save(lancamento);

		List<Lancamento> lancamentosItens = serie.gerarLancamentos(lancamento);

		for (Iterator<Lancamento> iterator = lancamentosItens.iterator(); iterator.hasNext();) {
			Lancamento lancamentoItem = (Lancamento) iterator.next();
			criadorLancamentoCartaoCredito.save(lancamentoItem);
		}
		return serie;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SerieLancamento saveTransferencia(Lancamento lancamento)
			throws ContaNotNullException, CategoriasIncompativeisException, MesLancamentoAlteradoException,
			TipoContaException, ValorLancamentoInvalidoException, DataSerieLancamentoInvalidaException {

		SerieLancamento serie = save(lancamento);

		List<Lancamento> lancamentosItens = serie.gerarLancamentos(lancamento);

		for (Iterator<Lancamento> iterator = lancamentosItens.iterator(); iterator.hasNext();) {
			Lancamento lancamentoItem = (Lancamento) iterator.next();
			criadorLancamentoContaCorrente.saveTransferencia((lancamentoItem));
		}
		return serie;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private SerieLancamento save(Lancamento lancamento) throws ContaNotNullException, CategoriasIncompativeisException,
			MesLancamentoAlteradoException, TipoContaException, ValorLancamentoInvalidoException,
			DataSerieLancamentoInvalidaException {

		validarSerieLancamento(lancamento);

		removerSerieLancamentos(lancamento);

		// Restrição: Apenas salva nova série, não atualizando seus dados
		// básicos(frequencia, data, etc) nos casos em que este método esteja sendo
		// invocado com objetivo de alteração de dados, pois haveria uma
		// falha na consitência dos dados se a data de início fosse alterada para mês
		// posterior. Se isto ocorresse, haveria remoção de todos os lançamentos da
		// série, mas o tratamento de atualização de saldos seria aplicado apenas à
		// partir do novo mês de início da série. Outros impactos não conhecidos também
		// podem ocorrer.
		SerieLancamento serie = lancamento.getSerie();
		if (serie.getId() == null) {
			serie = em.merge(lancamento.getSerie());
			lancamento.setSerie(serie);
		}

		return serie;
	}

	private void removerSerieLancamentos(Lancamento lancamento) {
		if (lancamento.getSerie().getId() != null) {
			em.createNamedQuery(Lancamento.REMOVE_BY_SERIE_QUERY).setParameter("serie", lancamento.getSerie())
					.executeUpdate();
		}
	}

	private void validarSerieLancamento(Lancamento lancamento) throws DataSerieLancamentoInvalidaException {
		if (lancamento.getSerie() == null)
			throw new IllegalArgumentException();
		
		lancamento.getSerie().validarDatas();
	}

}
