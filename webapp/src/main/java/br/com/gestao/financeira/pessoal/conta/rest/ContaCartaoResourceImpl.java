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
import br.com.gestao.financeira.pessoal.conta.ContaCartaoDTO;
import br.com.gestao.financeira.pessoal.conta.ContaCartaoResource;
import br.com.gestao.financeira.pessoal.conta.ContaDTO;
import br.com.gestao.financeira.pessoal.conta.ContaTipo;
import br.com.gestao.financeira.pessoal.conta.rules.ContaService;
import br.com.gestao.financeira.pessoal.conta.rules.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.conta.rules.NaoRemovivelException;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemocaoNaoPermitidaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;

@Path("cartoes")
public class ContaCartaoResourceImpl implements ContaCartaoResource {

	@Inject
	private ContaService contaService;

	@Override
	public List<ContaCartaoDTO> findAll() {
		List<Conta> contas = contaService.findContasCartoes();

		return contas.parallelStream().map(this::converterDTO).sequential()
				.sorted(Comparator.comparing(ContaDTO::getNome)).collect(Collectors.toList());
	}

	public void save(ContaCartaoDTO contaDTO) {
		ContaCartao conta = new ContaCartao();

		conta.setId(contaDTO.getId());
		conta.setNome(contaDTO.getNome());
		conta.setCartaoDiaFechamento(contaDTO.getDiaFechamento());
		conta.setCartaoDiaPagamento(contaDTO.getDiaPagamento());

		ContaBanco contaBanco = new ContaBanco();
		contaBanco.setId(contaDTO.getContaPagamentoId());

		conta.setContaPagamentoFatura(contaBanco);

		try {
			conta.setUsuario(((CredentialsStore) new InitialContext().lookup("java:module/CredentialsStoreImpl"))
					.recuperarUsuarioLogado());
		} catch (NamingException e1) {
			e1.printStackTrace();
		}

		try {
			contaService.saveContaCartao(conta, new ArrayList<Lancamento>());
		} catch (MesmoNomeExistenteException | ContaNotNullException | MesLancamentoAlteradoException
				| TipoContaException | CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			e.printStackTrace();
		}
	}

	public void remove(String uidConta) {
		ContaCartao conta = new ContaCartao();
		conta.setId(Integer.valueOf(uidConta));

		try {
			contaService.remove(conta);
		} catch (RemocaoNaoPermitidaException | NaoRemovivelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ContaCartaoDTO converterDTO(Conta conta) {
		ContaCartaoDTO contaDTO = new ContaCartaoDTO();
		contaDTO.setNome(conta.getNome());
		contaDTO.setId(conta.getId());
		contaDTO.setContaPagamentoId(((ContaCartao) conta).getContaPagamentoFatura().getId());
		contaDTO.setDiaFechamento(((ContaCartao) conta).getCartaoDiaFechamento());
		contaDTO.setDiaPagamento(((ContaCartao) conta).getCartaoDiaPagamento());
		contaDTO.setTipo(ContaTipo.CARTAO_CREDITO.getValue());
		return contaDTO;
	}

}
