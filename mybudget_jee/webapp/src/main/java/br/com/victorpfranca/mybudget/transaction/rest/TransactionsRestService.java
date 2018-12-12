package br.com.victorpfranca.mybudget.transaction.rest;

import java.util.Optional;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.groups.Default;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.category.Category;
import br.com.victorpfranca.mybudget.transaction.CheckingAccountTransaction;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.TransactionQuery;
import br.com.victorpfranca.mybudget.transaction.TransactionRegistryDTO;
import br.com.victorpfranca.mybudget.transaction.TransactionSerieUpdaterDTO;
import br.com.victorpfranca.mybudget.transaction.TransactionUpdaterDTO;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionSerieDateException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionService;
import br.com.victorpfranca.mybudget.view.MonthYear;

@Stateless
@LocalBean
public class TransactionsRestService {

	@Inject
	private TransactionQuery transactionQuery;
	@Inject
	private TransactionService transactionService;
	@Inject
	private EntityManager em;

	public void cadastrar(TransactionRegistryDTO cadastroLancamento) {
		Class<?>[] groups = { Valid.class, Default.class };
		Set<ConstraintViolation<TransactionRegistryDTO>> violations = Validation.buildDefaultValidatorFactory()
				.getValidator().validate(cadastroLancamento, groups);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional.ofNullable(cadastroLancamento).map(
				dto -> new TransactionToTransactionDTOConversor().converter(dto, this::findCategoria, this::findConta))
				.ifPresent(this::processarCadastro);
	}

	private void processarCadastro(Transaction transaction) {
		try {
			if (transaction.getSerie() != null) {
				transactionService.saveSerie(transaction);
			} else {
				transactionService.save(transaction);
			}
		} catch (InvalidTransactionSerieDateException | NullableAccountException | IncompatibleCategoriesException
				| TransactionMonthUpdatedException | AccountTypeException | InvalidTransactionValueException e) {
			throw new WebApplicationException(e.getMessage(), e,
					Response.status(422).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build());
		}
	}

	public void atualizar(Integer id, MonthYear monthYear, TransactionUpdaterDTO atualizacaoLancamento) {
		Optional.ofNullable(transactionQuery.recuperarLancamento(id, monthYear))
				.map(this::validaSePodeAlterarLancamento).ifPresent(lancamento -> {
					new TransactionToTransactionDTOConversor().aplicarValores(lancamento, atualizacaoLancamento,
							this::findCategoria, this::findConta);
					try {
						transactionService.save(lancamento);
					} catch (NullableAccountException | IncompatibleCategoriesException | TransactionMonthUpdatedException
							| AccountTypeException | InvalidTransactionValueException e) {
						throw new WebApplicationException(e.getMessage(), e,
								Response.status(422).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build());
					}
				});
	}

	private Account findConta(Integer contaId) {
		return em.find(Account.class, contaId);
	}

	private Category findCategoria(Integer categoriaId) {
		return em.find(Category.class, categoriaId);
	}

	private Transaction validaSeExisteSerie(Transaction transaction) {
		if (transaction.getSerie() == null) {
			throw new WebApplicationException(Response.status(422).type(MediaType.TEXT_PLAIN)
					.entity("crud.lancamento.error.editar.serie.null").build());
		}
		return transaction;
	}

	private Transaction validaSePodeAlterarLancamento(Transaction transaction) {
		if (transaction instanceof CheckingAccountTransaction) {
			CheckingAccountTransaction checkingAccountTransaction = (CheckingAccountTransaction) transaction;
			if (checkingAccountTransaction.isFaturaCartao() || checkingAccountTransaction.isTransferencia()) {
				throw new WebApplicationException(Response.status(422).type(MediaType.TEXT_PLAIN)
						.entity("crud.lancamento.error.editar.faturaCartaoOuTransferencia").build());
			}
		}
		return transaction;
	}

	public void remover(Integer id, MonthYear monthYear) {
		Optional.ofNullable(transactionQuery.recuperarLancamento(id, monthYear)).ifPresent(transactionService::remove);
	}

	public void atualizarSerie(Integer id, MonthYear monthYear, TransactionSerieUpdaterDTO atualizacao) {
		Optional.ofNullable(transactionQuery.recuperarLancamento(id, monthYear)).map(this::validaSeExisteSerie)
				.map(this::validaSePodeAlterarLancamento).ifPresent(lancamento -> {
					new TransactionToTransactionDTOConversor().aplicarValores(lancamento, atualizacao,
							this::findCategoria, this::findConta);
					try {
						transactionService.saveSerie(lancamento);
					} catch (NullableAccountException | IncompatibleCategoriesException | TransactionMonthUpdatedException
							| AccountTypeException | InvalidTransactionValueException
							| InvalidTransactionSerieDateException e) {
						throw new WebApplicationException(e.getMessage(), e,
								Response.status(422).type(MediaType.TEXT_PLAIN).entity(e.getMessage()).build());
					}
				});
	}

	public void removerSerie(Integer id, MonthYear monthYear) {
		Optional.ofNullable(transactionQuery.recuperarLancamento(id, monthYear)).map(this::validaSeExisteSerie)
				.map(Transaction::getSerie).ifPresent(transactionService::removeSerie);
	}

}
