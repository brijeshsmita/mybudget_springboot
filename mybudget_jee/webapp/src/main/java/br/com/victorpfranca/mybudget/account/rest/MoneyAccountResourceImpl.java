package br.com.victorpfranca.mybudget.account.rest;

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
import br.com.victorpfranca.mybudget.account.MoneyAccount;
import br.com.victorpfranca.mybudget.account.MoneyAccountDTO;
import br.com.victorpfranca.mybudget.account.MoneyAccountResource;
import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.account.rules.CantRemoveException;
import br.com.victorpfranca.mybudget.account.rules.SameNameException;
import br.com.victorpfranca.mybudget.transaction.rules.AccountTypeException;
import br.com.victorpfranca.mybudget.transaction.rules.DeletionNotPermittedException;
import br.com.victorpfranca.mybudget.transaction.rules.IncompatibleCategoriesException;
import br.com.victorpfranca.mybudget.transaction.rules.InvalidTransactionValueException;
import br.com.victorpfranca.mybudget.transaction.rules.NullableAccountException;
import br.com.victorpfranca.mybudget.transaction.rules.TransactionMonthUpdatedException;

@Path("contasDinheiro")
public class MoneyAccountResourceImpl implements MoneyAccountResource {

	@Inject
	private BankAccountService bankAccountService;

	@Override
	public List<MoneyAccountDTO> findAll() {
		List<Account> accounts = bankAccountService.findContasDinheiro();

		return accounts.parallelStream().map(this::converterDTO).sequential()
				.sorted(Comparator.comparing(AccountDTO::getNome)).collect(Collectors.toList());
	}

	public void save(MoneyAccountDTO contaDTO) {
		BankAccount conta = new BankAccount();

		conta.setId(contaDTO.getId());
		conta.setNome(contaDTO.getNome());
		conta.setSaldoInicial(contaDTO.getSaldoInicial());
		try {
			conta.setUsuario(((CredentialsStore) new InitialContext().lookup("java:module/CredentialsStoreImpl"))
					.recuperarUsuarioLogado());
		} catch (NamingException e1) {
			e1.printStackTrace();
		}

		try {
			bankAccountService.saveContaCorrente(conta);
		} catch (SameNameException | NullableAccountException | TransactionMonthUpdatedException | AccountTypeException
				| IncompatibleCategoriesException | InvalidTransactionValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void remove(String uidConta) {
		BankAccount conta = new BankAccount();
		conta.setId(Integer.valueOf(uidConta));

		try {
			bankAccountService.remove(conta);
		} catch (DeletionNotPermittedException | CantRemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private MoneyAccountDTO converterDTO(Account account) {
		MoneyAccountDTO contaDTO = new MoneyAccountDTO();
		contaDTO.setNome(account.getNome());
		contaDTO.setId(account.getId());
		contaDTO.setSaldoInicial(((MoneyAccount) account).getSaldoInicial());
		contaDTO.setTipo(AccountType.CONTA_DINHEIRO.getValue());
		return contaDTO;
	}

}
