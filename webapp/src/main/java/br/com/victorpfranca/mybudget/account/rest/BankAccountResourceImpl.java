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
import br.com.victorpfranca.mybudget.account.MoneyAccount;
import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.account.rules.CantRemoveException;
import br.com.victorpfranca.mybudget.account.rules.SameNameException;
import br.com.victorpfranca.mybudget.conta.ContaBancoResource;
import br.com.victorpfranca.mybudget.conta.ContaDTO;
import br.com.victorpfranca.mybudget.transaction.Transaction;
import br.com.victorpfranca.mybudget.transaction.rules.CategoriasIncompativeisException;
import br.com.victorpfranca.mybudget.transaction.rules.ContaNotNullException;
import br.com.victorpfranca.mybudget.transaction.rules.MesLancamentoAlteradoException;
import br.com.victorpfranca.mybudget.transaction.rules.RemocaoNaoPermitidaException;
import br.com.victorpfranca.mybudget.transaction.rules.TipoContaException;
import br.com.victorpfranca.mybudget.transaction.rules.ValorLancamentoInvalidoException;

@Path("contasBancos")
public class BankAccountResourceImpl implements ContaBancoResource {

	@Inject
	private BankAccountService bankAccountService;

	@Override
	public List<ContaDTO> findAll() {
		List<Account> accounts = bankAccountService.findAll();

		return accounts.parallelStream().map(this::converterDTO).sequential()
				.sorted(Comparator.comparing(ContaDTO::getNome)).collect(Collectors.toList());
	}

	public ContaDTO find(String uidConta) {
		return converterDTO(bankAccountService.find(Integer.valueOf(uidConta)));

	}

	public void save(ContaDTO contaDTO) {
		Account account = null;

		if (contaDTO.getTipo().equals(AccountType.CONTA_BANCO.getValue())) {
			account = new BankAccount();
			((BankAccount) account).setSaldoInicial(contaDTO.getSaldoInicial());
		} else if (contaDTO.getTipo().equals(AccountType.CARTAO_CREDITO.getValue())) {
			account = new CreditCardAccount();
			((CreditCardAccount) account).setContaPagamentoFatura(bankAccountService.find(contaDTO.getContaPagamentoId()));
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
			}else {
				bankAccountService.saveContaCartao(account, new ArrayList<Transaction>());
			}
		} catch (SameNameException | ContaNotNullException | MesLancamentoAlteradoException
				| TipoContaException | CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void remove(String uidConta) {
		Account account = bankAccountService.find(Integer.valueOf(uidConta));

		try {
			bankAccountService.remove(account);
		} catch (RemocaoNaoPermitidaException | CantRemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ContaDTO converterDTO(Account account) {
		ContaDTO contaDTO = new ContaDTO();
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
