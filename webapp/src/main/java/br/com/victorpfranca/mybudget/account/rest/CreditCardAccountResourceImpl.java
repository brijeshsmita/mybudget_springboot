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
import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.account.rules.CantRemoveException;
import br.com.victorpfranca.mybudget.account.rules.SameNameException;
import br.com.victorpfranca.mybudget.conta.ContaCartaoDTO;
import br.com.victorpfranca.mybudget.conta.ContaCartaoResource;
import br.com.victorpfranca.mybudget.conta.ContaDTO;
import br.com.victorpfranca.mybudget.lancamento.Lancamento;
import br.com.victorpfranca.mybudget.lancamento.rules.CategoriasIncompativeisException;
import br.com.victorpfranca.mybudget.lancamento.rules.ContaNotNullException;
import br.com.victorpfranca.mybudget.lancamento.rules.MesLancamentoAlteradoException;
import br.com.victorpfranca.mybudget.lancamento.rules.RemocaoNaoPermitidaException;
import br.com.victorpfranca.mybudget.lancamento.rules.TipoContaException;
import br.com.victorpfranca.mybudget.lancamento.rules.ValorLancamentoInvalidoException;

@Path("cartoes")
public class CreditCardAccountResourceImpl implements ContaCartaoResource {

	@Inject
	private BankAccountService bankAccountService;

	@Override
	public List<ContaCartaoDTO> findAll() {
		List<Account> accounts = bankAccountService.findContasCartoes();

		return accounts.parallelStream().map(this::converterDTO).sequential()
				.sorted(Comparator.comparing(ContaDTO::getNome)).collect(Collectors.toList());
	}

	public void save(ContaCartaoDTO contaDTO) {
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
			bankAccountService.saveContaCartao(conta, new ArrayList<Lancamento>());
		} catch (SameNameException | ContaNotNullException | MesLancamentoAlteradoException
				| TipoContaException | CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			e.printStackTrace();
		}
	}

	public void remove(String uidConta) {
		CreditCardAccount conta = new CreditCardAccount();
		conta.setId(Integer.valueOf(uidConta));

		try {
			bankAccountService.remove(conta);
		} catch (RemocaoNaoPermitidaException | CantRemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ContaCartaoDTO converterDTO(Account account) {
		ContaCartaoDTO contaDTO = new ContaCartaoDTO();
		contaDTO.setNome(account.getNome());
		contaDTO.setId(account.getId());
		contaDTO.setContaPagamentoId(((CreditCardAccount) account).getAccountPagamentoFatura().getId());
		contaDTO.setDiaFechamento(((CreditCardAccount) account).getCartaoDiaFechamento());
		contaDTO.setDiaPagamento(((CreditCardAccount) account).getCartaoDiaPagamento());
		contaDTO.setTipo(AccountType.CARTAO_CREDITO.getValue());
		return contaDTO;
	}

}
