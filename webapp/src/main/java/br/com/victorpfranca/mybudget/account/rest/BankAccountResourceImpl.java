package br.com.victorpfranca.mybudget.account.rest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Path;

import br.com.victorpfranca.mybudget.accesscontroll.CredentialsStore;
import br.com.victorpfranca.mybudget.account.Account;
import br.com.victorpfranca.mybudget.account.AccountDTO;
import br.com.victorpfranca.mybudget.account.AccountType;
import br.com.victorpfranca.mybudget.account.BankAccount;
import br.com.victorpfranca.mybudget.account.BankAccountResource;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.account.MoneyAccount;
import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.account.rules.CantRemoveException;
import br.com.victorpfranca.mybudget.account.rules.SameNameException;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.DeletionNotPermittedException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;

@Path("contasBancos")
public class BankAccountResourceImpl implements BankAccountResource {

	@Inject
	private BankAccountService bankAccountService;

	@Override
	public List<AccountDTO> findAll() {
		List<Account> accounts = bankAccountService.findAll();

		return accounts.parallelStream().map(this::converterDTO).sequential()
				.sorted(Comparator.comparing(AccountDTO::getNome)).collect(Collectors.toList());
	}

	public AccountDTO find(String uidConta) {
		return converterDTO(bankAccountService.find(Integer.valueOf(uidConta)));

	}

	public void save(AccountDTO contaDTO) {
		Account account = null;

		if (contaDTO.getTipo().equals(AccountType.CONTA_BANCO.getValue())) {
			account = new BankAccount();
			((BankAccount) account).setSaldoInicial(contaDTO.getSaldoInicial());
		} else if (contaDTO.getTipo().equals(AccountType.CARTAO_CREDITO.getValue())) {
			account = new CreditCardAccount();
			((CreditCardAccount) account)
					.setContaPagamentoFatura(bankAccountService.find(contaDTO.getContaPagamentoId()));
			((CreditCardAccount) account).setCartaoDiaFechamento(contaDTO.getDiaFechamento());
			((CreditCardAccount) account).setCartaoDiaPagamento(contaDTO.getDiaPagamento());

		} else if (contaDTO.getTipo().equals(AccountType.CONTA_DINHEIRO.getValue())) {
			account = new MoneyAccount();
			((MoneyAccount) account).setSaldoInicial(contaDTO.getSaldoInicial());
		}

		account.setId(contaDTO.getId());
		account.setNome(contaDTO.getNome());

		try {
			account.setUsuario(((CredentialsStore) new InitialContext().lookup("java:module/CredentialsStoreImpl"))
					.recuperarUsuarioLogado());
		} catch (NamingException e1) {
			e1.printStackTrace();
		}

		try {
			if (!contaDTO.getTipo().equals(AccountType.CARTAO_CREDITO.getValue())) {
				bankAccountService.saveContaCorrente(account);
			} else {
				bankAccountService.saveContaCartao(account, new ArrayList<Transaction>());
			}
		} catch (SameNameException | NullableAccountException | TransactionMonthUpdatedException | AccountTypeException
				| IncompatibleCategoriesException | InvalidTransactionValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void remove(String uidConta) {
		Account account = bankAccountService.find(Integer.valueOf(uidConta));

		try {
			bankAccountService.remove(account);
		} catch (DeletionNotPermittedException | CantRemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private AccountDTO converterDTO(Account account) {
		AccountDTO contaDTO = new AccountDTO();
		contaDTO.setNome(account.getNome());
		contaDTO.setId(account.getId());
		if (account instanceof BankAccount) {
			contaDTO.setTipo(AccountType.CONTA_BANCO.getValue());
			contaDTO.setSaldoInicial(((BankAccount) account).getSaldoInicial());
		}

		else if (account instanceof CreditCardAccount) {
			contaDTO.setTipo(AccountType.CARTAO_CREDITO.getValue());
			contaDTO.setContaPagamentoId(((CreditCardAccount) account).getAccountPagamentoFatura().getId());
			contaDTO.setContaPagamentoNome(((CreditCardAccount) account).getAccountPagamentoFatura().getNome());
			contaDTO.setDiaFechamento(((CreditCardAccount) account).getCartaoDiaFechamento());
			contaDTO.setDiaPagamento(((CreditCardAccount) account).getCartaoDiaPagamento());
		} else if (account instanceof MoneyAccount) {
			contaDTO.setTipo(AccountType.CONTA_DINHEIRO.getValue());
			contaDTO.setSaldoInicial(((MoneyAccount) account).getSaldoInicial());
		}
		return contaDTO;
	}

}
