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
import br.com.victorpfranca.mybudget.account.AccountType;
import br.com.victorpfranca.mybudget.account.BankAccount;
import br.com.victorpfranca.mybudget.account.CreditCardAccount;
import br.com.victorpfranca.mybudget.account.CreditCardAccountDTO;
import br.com.victorpfranca.mybudget.account.CreditCardAccountResource;
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

@Path("cartoes")
public class CreditCardAccountResourceImpl implements CreditCardAccountResource {

	@Inject
	private BankAccountService bankAccountService;

	@Override
	public List<CreditCardAccountDTO> findAll() {
		List<Account> accounts = bankAccountService.findContasCartoes();

		return accounts.parallelStream().map(this::converterDTO).sequential()
				.sorted(Comparator.comparing(CreditCardAccountDTO::getNome)).collect(Collectors.toList());
	}

	public void save(CreditCardAccountDTO contaDTO) {
		CreditCardAccount conta = new CreditCardAccount();

		conta.setId(contaDTO.getId());
		conta.setNome(contaDTO.getNome());
		conta.setCartaoDiaFechamento(contaDTO.getDiaFechamento());
		conta.setCartaoDiaPagamento(contaDTO.getDiaPagamento());

		BankAccount bankAccount = new BankAccount();
		bankAccount.setId(contaDTO.getContaPagamentoId());

		conta.setContaPagamentoFatura(bankAccount);

		try {
			conta.setUsuario(((CredentialsStore) new InitialContext().lookup("java:module/CredentialsStoreImpl"))
					.recuperarUsuarioLogado());
		} catch (NamingException e1) {
			e1.printStackTrace();
		}

		try {
			bankAccountService.saveContaCartao(conta, new ArrayList<Transaction>());
		} catch (SameNameException | NullableAccountException | TransactionMonthUpdatedException | AccountTypeException
				| IncompatibleCategoriesException | InvalidTransactionValueException e) {
			e.printStackTrace();
		}
	}

	public void remove(String uidConta) {
		CreditCardAccount conta = new CreditCardAccount();
		conta.setId(Integer.valueOf(uidConta));

		try {
			bankAccountService.remove(conta);
		} catch (DeletionNotPermittedException | CantRemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private CreditCardAccountDTO converterDTO(Account account) {
		CreditCardAccountDTO contaDTO = new CreditCardAccountDTO();
		contaDTO.setNome(account.getNome());
		contaDTO.setId(account.getId());
		contaDTO.setContaPagamentoId(((CreditCardAccount) account).getAccountPagamentoFatura().getId());
		contaDTO.setDiaFechamento(((CreditCardAccount) account).getCartaoDiaFechamento());
		contaDTO.setDiaPagamento(((CreditCardAccount) account).getCartaoDiaPagamento());
		contaDTO.setTipo(AccountType.CARTAO_CREDITO.getValue());
		return contaDTO;
	}

}
