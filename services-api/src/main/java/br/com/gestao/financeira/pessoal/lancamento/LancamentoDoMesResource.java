package br.com.gestao.financeira.pessoal.lancamento;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface LancamentoDoMesResource {

	@GET
	LancamentoDTO recuperarLancamento();

	@PUT
	void atualizar(@Valid AtualizacaoLancamentoDTO atualizacao);

	@DELETE
	void remover();

	@PUT
	@Path("serie")
	void atualizarSerie(@Valid AtualizacaoSerieLancamentoDTO atualizacao);

	@DELETE
	@Path("serie")
	void removerSerie();

}
