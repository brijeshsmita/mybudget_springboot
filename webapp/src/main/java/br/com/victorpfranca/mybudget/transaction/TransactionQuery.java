package br.com.victorpfranca.mybudget.transaction;

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

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.BalanceQuery;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.lancamento.Lancamento_;
import br.com.victorpfranca.mybudget.transaction.rules.LancamentoService;
import br.com.victorpfranca.mybudget.view.MonthYear;

@RequestScoped
public class TransactionQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private LancamentoService lancamentoService;
	@Inject
	private BalanceQuery contas;
	@Inject
	private EntityManager em;
	private Map<TransactionsFilter, List<Transaction>> cache = new HashMap<>();

	public List<Transaction> transactions(TransactionsFilter transactionsFilter) {
		return cache.computeIfAbsent(transactionsFilter, filtros -> {
			BigDecimal saldoInicial = contas.recuperarSaldoInicial(filtros);
			List<Transaction> transactions = lancamentoService.carregarExtratoCorrenteMensal(filtros.getAnoMes().getAno(),
					filtros.getAnoMes().getMes(),
					Optional.ofNullable(filtros.getAccount()).map(id -> em.find(Account.class, id)).orElse(null),
					Optional.ofNullable(filtros.getCategoria()).map(id -> em.find(Category.class, id)).orElse(null),
					saldoInicial, filtros.getStatus());
			return transactions;
		});
	}

	public List<Transaction> extratoCartao(TransactionsFilter transactionsFilter) {
		return cache.computeIfAbsent(transactionsFilter, filtros -> {
			List<Transaction> transactions = lancamentoService.carregarExtratoCartaoMensal(filtros.getAnoMes().getAno(),
					filtros.getAnoMes().getMes(),
					Optional.ofNullable(filtros.getAccount()).map(id -> em.find(Account.class, id)).orElse(null),
					Optional.ofNullable(filtros.getCategoria()).map(id -> em.find(Category.class, id)).orElse(null),
					BigDecimal.ZERO);
			return transactions;
		});
	}

	public Transaction recuperarLancamento(Integer id, MonthYear monthYear) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Transaction> criteriaQuery = cb.createQuery(Transaction.class);
		Root<Transaction> transaction = criteriaQuery.from(Transaction.class);
		Predicate predicate = cb.equal(transaction.get(Lancamento_.id), id);
		if (monthYear != null) {
			predicate = cb.and(predicate, cb.equal(transaction.get(Lancamento_.ano), monthYear.getAno()),
					cb.equal(transaction.get(Lancamento_.mes), monthYear.getMes()));
		}
		criteriaQuery.select(transaction).where(predicate);
		try {
			return em.createQuery(criteriaQuery).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
