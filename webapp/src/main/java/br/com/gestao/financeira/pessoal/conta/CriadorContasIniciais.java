package br.com.gestao.financeira.pessoal.conta;

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

import br.com.gestao.financeira.pessoal.conta.rules.ContaService;
import br.com.gestao.financeira.pessoal.conta.rules.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;

@Stateless
public class CriadorContasIniciais {

	@EJB
	private ContaService contaService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute() throws MesmoNomeExistenteException, ContaNotNullException, MesLancamentoAlteradoException,
			TipoContaException, CategoriasIncompativeisException, ValorLancamentoInvalidoException {

		List<Conta> contasBanco = getContas("contas_iniciais_bancos", new ConstrutorContaBanco());
		List<Conta> contasDinheiro = getContas("contas_iniciais_dinheiro", new ConstrutorContaDinheiro());
		List<Conta> contasCartao = getContas("contas_iniciais_cartao", new ConstrutorContaCartao());
		contasBanco = contaService.saveContasCorrente(contasBanco);
		contaService.saveContasCorrente(contasDinheiro);

		for (Iterator<Conta> iterator = contasCartao.iterator(); iterator.hasNext();) {
			ContaCartao conta = (ContaCartao) iterator.next();
			conta.setContaPagamentoFatura(contasBanco.get(0));

		}
		contaService.saveContasCartoes(contasCartao);
	}

	private List<Conta> getContas(String fileName, ConstrutorConta construtorConta) {
		List<Conta> contas = new ArrayList<Conta>();
		InputStream is = ObjectUtils.firstNonNull(CriadorContasIniciais.class.getResourceAsStream(fileName),
				CriadorContasIniciais.class.getResourceAsStream("/" + fileName));

		if (is == null)
			return new ArrayList<Conta>();

		Scanner scanner = new Scanner(is, "UTF-8");
		while (scanner.hasNextLine()) {
			contas.add(construtorConta.build(scanner.nextLine()));
		}
		scanner.close();
		return contas;
	}

	abstract class ConstrutorConta {
		public abstract Conta build(String nome);
	}

	class ConstrutorContaBanco extends ConstrutorConta {
		public Conta build(String nome) {
			return new ContaBanco(nome);
		}
	}

	class ConstrutorContaCartao extends ConstrutorConta {
		public Conta build(String nome) {
			ContaCartao contaCartao = new ContaCartao(nome);
			contaCartao.setCartaoDiaFechamento(7);
			contaCartao.setCartaoDiaPagamento(15);
			return contaCartao;
		}

	}

	class ConstrutorContaDinheiro extends ConstrutorConta {
		public Conta build(String nome) {
			return new ContaDinheiro(nome);
		}

	}
}
