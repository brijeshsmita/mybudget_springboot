package br.com.gestao.financeira.pessoal.conta.rest;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Path;

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.ContaBanco;
import br.com.gestao.financeira.pessoal.conta.ContaDTO;
import br.com.gestao.financeira.pessoal.conta.ContaDinheiro;
import br.com.gestao.financeira.pessoal.conta.ContaDinheiroDTO;
import br.com.gestao.financeira.pessoal.conta.ContaDinheiroResource;
import br.com.gestao.financeira.pessoal.conta.ContaTipo;
import br.com.gestao.financeira.pessoal.conta.rules.ContaService;
import br.com.gestao.financeira.pessoal.conta.rules.MesmoNomeExistenteException;
import br.com.gestao.financeira.pessoal.conta.rules.NaoRemovivelException;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.lancamento.rules.CategoriasIncompativeisException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ContaNotNullException;
import br.com.gestao.financeira.pessoal.lancamento.rules.MesLancamentoAlteradoException;
import br.com.gestao.financeira.pessoal.lancamento.rules.RemocaoNaoPermitidaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.TipoContaException;
import br.com.gestao.financeira.pessoal.lancamento.rules.ValorLancamentoInvalidoException;

@Path("contasDinheiro")
public class ContaDinheiroResourceImpl implements ContaDinheiroResource{

	@Inject
	private ContaService contaService;

	@Override
	public List<ContaDinheiroDTO> findAll() {
		List<Conta> contas = contaService.findContasDinheiro();

		return contas.parallelStream().map(this::converterDTO).sequential()
				.sorted(Comparator.comparing(ContaDTO::getNome)).collect(Collectors.toList());
	}

	public void save(ContaDinheiroDTO contaDTO) {
		ContaBanco conta = new ContaBanco();
		
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
			contaService.saveContaCorrente(conta);
		} catch (MesmoNomeExistenteException | ContaNotNullException | MesLancamentoAlteradoException
				| TipoContaException | CategoriasIncompativeisException | ValorLancamentoInvalidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void remove(String uidConta) {
		ContaBanco conta = new ContaBanco();
		conta.setId(Integer.valueOf(uidConta));

		try {
			contaService.remove(conta);
		} catch (RemocaoNaoPermitidaException | NaoRemovivelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private ContaDinheiroDTO converterDTO(Conta conta) {
		ContaDinheiroDTO contaDTO = new ContaDinheiroDTO();
		contaDTO.setNome(conta.getNome());
		contaDTO.setId(conta.getId());
		contaDTO.setSaldoInicial(((ContaDinheiro) conta).getSaldoInicial());
		contaDTO.setTipo(ContaTipo.CONTA_DINHEIRO.getValue());
		return contaDTO;
	}

}
