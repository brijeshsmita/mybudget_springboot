package br.com.gestao.financeira.pessoal.lancamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.conta.ConsultaSaldos;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.lancamento.rules.LancamentoService;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@RequestScoped
public class ConsultaLancamentos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private LancamentoService lancamentoService;
	@Inject
	private ConsultaSaldos contas;
	@Inject
	private EntityManager em;
	private Map<FiltrosLancamentos, List<Lancamento>> cache = new HashMap<>();

	public List<Lancamento> lancamentos(FiltrosLancamentos filtrosLancamentos) {
		return cache.computeIfAbsent(filtrosLancamentos, filtros -> {
			BigDecimal saldoInicial = contas.recuperarSaldoInicial(filtros);
			List<Lancamento> lancamentos = lancamentoService.carregarExtratoCorrenteMensal(filtros.getAnoMes().getAno(),
					filtros.getAnoMes().getMes(),
					Optional.ofNullable(filtros.getConta()).map(id -> em.find(Conta.class, id)).orElse(null),
					Optional.ofNullable(filtros.getCategoria()).map(id -> em.find(Categoria.class, id)).orElse(null),
					saldoInicial, filtros.getStatus());
			return lancamentos;
		});
	}

	public List<Lancamento> extratoCartao(FiltrosLancamentos filtrosLancamentos) {
		return cache.computeIfAbsent(filtrosLancamentos, filtros -> {
			List<Lancamento> lancamentos = lancamentoService.carregarExtratoCartaoMensal(filtros.getAnoMes().getAno(),
					filtros.getAnoMes().getMes(),
					Optional.ofNullable(filtros.getConta()).map(id -> em.find(Conta.class, id)).orElse(null),
					Optional.ofNullable(filtros.getCategoria()).map(id -> em.find(Categoria.class, id)).orElse(null),
					BigDecimal.ZERO);
			return lancamentos;
		});
	}

	public Lancamento recuperarLancamento(Integer id, AnoMes anoMes) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteriaQuery = cb.createQuery(Lancamento.class);
		Root<Lancamento> lancamento = criteriaQuery.from(Lancamento.class);
		Predicate predicate = cb.equal(lancamento.get(Lancamento_.id), id);
		if (anoMes != null) {
			predicate = cb.and(predicate, cb.equal(lancamento.get(Lancamento_.ano), anoMes.getAno()),
					cb.equal(lancamento.get(Lancamento_.mes), anoMes.getMes()));
		}
		criteriaQuery.select(lancamento).where(predicate);
		try {
			return em.createQuery(criteriaQuery).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
