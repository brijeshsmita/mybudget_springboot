package br.com.victorpfranca.mybudget.account;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.ObjectUtils;

import br.com.victorpfranca.mybudget.account.rules.BankAccountService;
import br.com.victorpfranca.mybudget.account.rules.SameNameException;
import br.com.victorpfranca.mybudget.lancamento.rules.CategoriasIncompativeisException;
import br.com.victorpfranca.mybudget.lancamento.rules.ContaNotNullException;
import br.com.victorpfranca.mybudget.lancamento.rules.MesLancamentoAlteradoException;
import br.com.victorpfranca.mybudget.lancamento.rules.TipoContaException;
import br.com.victorpfranca.mybudget.lancamento.rules.ValorLancamentoInvalidoException;

@Stateless
public class InitialAccountsBuilder {

	@EJB
	private BankAccountService bankAccountService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute() throws SameNameException, ContaNotNullException, MesLancamentoAlteradoException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		List<Account> contasBanco = getAccounts("contas_iniciais_bancos", new ConstrutorContaBanco());
		List<Account> contasDinheiro = getAccounts("contas_iniciais_dinheiro", new ConstrutorContaDinheiro());
		List<Account> contasCartao = getAccounts("contas_iniciais_cartao", new ConstrutorContaCartao());
		contasBanco = bankAccountService.saveContasCorrente(contasBanco);
		bankAccountService.saveContasCorrente(contasDinheiro);

		for (Iterator<Account> iterator = contasCartao.iterator(); iterator.hasNext();) {
			CreditCardAccount conta = (CreditCardAccount) iterator.next();
			conta.setContaPagamentoFatura(contasBanco.get(0));

		}
		bankAccountService.saveContasCartoes(contasCartao);
	}

	private List<Account> getAccounts(String fileName, ConstrutorConta construtorConta) {
		List<Account> accounts = new ArrayList<Account>();
		InputStream is = ObjectUtils.firstNonNull(InitialAccountsBuilder.class.getResourceAsStream(fileName),
				InitialAccountsBuilder.class.getResourceAsStream("/" + fileName));

		if (is == null)
			return new ArrayList<Account>();

		Scanner scanner = new Scanner(is, "UTF-8");
		while (scanner.hasNextLine()) {
			accounts.add(construtorConta.build(scanner.nextLine()));
		}
		scanner.close();
		return accounts;
	}

	abstract class ConstrutorConta {
		public abstract Account build(String nome);
	}

	class ConstrutorContaBanco extends ConstrutorConta {
		public Account build(String nome) {
			return new BankAccount(nome);
		}
	}

	class ConstrutorContaCartao extends ConstrutorConta {
		public Account build(String nome) {
			CreditCardAccount creditCardAccount = new CreditCardAccount(nome);
			creditCardAccount.setCartaoDiaFechamento(7);
			creditCardAccount.setCartaoDiaPagamento(15);
			return creditCardAccount;
		}

	}

	class ConstrutorContaDinheiro extends ConstrutorConta {
		public Account build(String nome) {
			return new MoneyAccount(nome);
		}

	}
}
