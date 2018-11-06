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
import br.com.victorpfranca.mybudget.account.AccountType;
import br.com.victorpfranca.mybudget.account.BankAccount;
import br.com.victorpfranca.mybudget.account.MoneyAccount;
import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.account.rules.CantRemoveException;
import br.com.victorpfranca.mybudget.account.rules.SameNameException;
import br.com.victorpfranca.mybudget.conta.ContaDTO;
import br.com.victorpfranca.mybudget.conta.ContaDinheiroDTO;
import br.com.victorpfranca.mybudget.conta.ContaDinheiroResource;
import br.com.victorpfranca.mybudget.lancamento.rules.CategoriasIncompativeisException;
import br.com.victorpfranca.mybudget.lancamento.rules.ContaNotNullException;
import br.com.victorpfranca.mybudget.lancamento.rules.MesLancamentoAlteradoException;
import br.com.victorpfranca.mybudget.lancamento.rules.RemocaoNaoPermitidaException;
import br.com.victorpfranca.mybudget.lancamento.rules.TipoContaException;
import br.com.victorpfranca.mybudget.lancamento.rules.ValorLancamentoInvalidoException;

@Path("contasDinheiro")
public class MoneyAccountResourceImpl implements ContaDinheiroResource{

	@Inject
	private BankAccountService bankAccountService;

	@Override
	public List<ContaDinheiroDTO> findAll() {
		List<Account> accounts = bankAccountService.findContasDinheiro();

		return accounts.parallelStream().map(this::converterDTO).sequential()
				.sorted(Comparator.comparing(ContaDTO::getNome)).collect(Collectors.toList());
	}

	public void save(ContaDinheiroDTO contaDTO) {
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
		} catch (SameNameException | ContaNotNullException | MesLancamentoAlteradoException
				| TipoContaException | CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void remove(String uidConta) {
		BankAccount conta = new BankAccount();
		conta.setId(Integer.valueOf(uidConta));

		try {
			bankAccountService.remove(conta);
		} catch (RemocaoNaoPermitidaException | CantRemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ContaDinheiroDTO converterDTO(Account account) {
		ContaDinheiroDTO contaDTO = new ContaDinheiroDTO();
		contaDTO.setNome(account.getNome());
		contaDTO.setId(account.getId());
		contaDTO.setSaldoInicial(((MoneyAccount) account).getSaldoInicial());
		contaDTO.setTipo(AccountType.CONTA_DINHEIRO.getValue());
		return contaDTO;
	}

}
