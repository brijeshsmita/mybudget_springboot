package br.com.gestao.financeira.pessoal.conta.rest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Path;

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaBanco;
import br.com.gestao.financeira.pessoal.conta.ContaCartao;
import br.com.gestao.financeira.pessoal.conta.ContaDTO;
import br.com.gestao.financeira.pessoal.conta.ContaDinheiro;
import br.com.gestao.financeira.pessoal.conta.ContaResource;
import br.com.gestao.financeira.pessoal.conta.ContaTipo;
import br.com.gestao.financeira.pessoal.conta.rules.ContaService;
import br.com.gestao.financeira.pessoal.conta.rules.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.conta.rules.NaoRemovivelException;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.infra.date.DateUtils;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemocaoNaoPermitidaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;

@Path("contas")
public class ContaResourceImpl implements ContaResource {

	@Inject
	private ContaService contaService;

	@Override
	public List<ContaDTO> findAll() {
		List<Conta> contas = contaService.findAll();

		return contas.parallelStream().map(this::converterDTO).sequential()
				.sorted(Comparator.comparing(ContaDTO::getNome)).collect(Collectors.toList());
	}

	public ContaDTO find(String uidConta) {
		return converterDTO(contaService.find(Integer.valueOf(uidConta)));

	}

	public void save(ContaDTO contaDTO) {
		Conta conta = null;

		if (contaDTO.getTipo().equals(ContaTipo.CONTA_BANCO.getValue())) {
			conta = new ContaBanco();
			((ContaBanco) conta).setSaldoInicial(contaDTO.getSaldoInicial());
			((ContaBanco) conta).setDataSaldoInicial(DateUtils.iso8601(contaDTO.getDataSaldoInicial()));
		} else if (contaDTO.getTipo().equals(ContaTipo.CARTAO_CREDITO.getValue())) {
			conta = new ContaCartao();
			((ContaCartao) conta).setContaPagamentoFatura(contaService.find(contaDTO.getContaPagamentoId()));
			((ContaCartao) conta).setCartaoDiaFechamento(contaDTO.getDiaFechamento());
			((ContaCartao) conta).setCartaoDiaPagamento(contaDTO.getDiaPagamento());
			
		} else if (contaDTO.getTipo().equals(ContaTipo.CONTA_DINHEIRO.getValue())) {
			conta = new ContaDinheiro();
			((ContaDinheiro) conta).setSaldoInicial(contaDTO.getSaldoInicial());
			((ContaDinheiro) conta).setDataSaldoInicial(DateUtils.iso8601(contaDTO.getDataSaldoInicial()));
		}

		conta.setId(contaDTO.getId());
		conta.setNome(contaDTO.getNome());

		try {
			conta.setUsuario(((CredentialsStore) new InitialContext().lookup("java:module/CredentialsStoreImpl"))
					.recuperarUsuarioLogado());
		} catch (NamingException e1) {
			e1.printStackTrace();
		}

		try {
			if (!contaDTO.getTipo().equals(ContaTipo.CARTAO_CREDITO.getValue())) {
				contaService.saveContaCorrente(conta);
			}else {
				contaService.saveContaCartao(conta, new ArrayList<Lancamento>());
			}
		} catch (MesmoNomeExistenteException | ContaNotNullException | MesLancamentoAlteradoException
				| TipoContaException | CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void remove(String uidConta) {
		Conta conta = contaService.find(Integer.valueOf(uidConta));

		try {
			contaService.remove(conta);
		} catch (RemocaoNaoPermitidaException | NaoRemovivelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ContaDTO converterDTO(Conta conta) {
		ContaDTO contaDTO = new ContaDTO();
		contaDTO.setNome(conta.getNome());
		contaDTO.setId(conta.getId());
		if (conta instanceof ContaBanco) {
			contaDTO.setTipo(ContaTipo.CONTA_BANCO.getValue());
			contaDTO.setSaldoInicial(((ContaBanco) conta).getSaldoInicial());
			contaDTO.setDataSaldoInicial(DateUtils.iso8601(((ContaBanco) conta).getDataSaldoInicial()));
		}

		else if (conta instanceof ContaCartao) {
			contaDTO.setTipo(ContaTipo.CARTAO_CREDITO.getValue());
			contaDTO.setContaPagamentoId(((ContaCartao) conta).getContaPagamentoFatura().getId());
			contaDTO.setContaPagamentoNome(((ContaCartao) conta).getContaPagamentoFatura().getNome());
			contaDTO.setDiaFechamento(((ContaCartao) conta).getCartaoDiaFechamento());
			contaDTO.setDiaPagamento(((ContaCartao) conta).getCartaoDiaPagamento());
		} else if (conta instanceof ContaDinheiro) {
			contaDTO.setTipo(ContaTipo.CONTA_DINHEIRO.getValue());
			contaDTO.setSaldoInicial(((ContaDinheiro) conta).getSaldoInicial());
			contaDTO.setDataSaldoInicial(DateUtils.iso8601(((ContaDinheiro) conta).getDataSaldoInicial()));
		}
		return contaDTO;
	}

}
