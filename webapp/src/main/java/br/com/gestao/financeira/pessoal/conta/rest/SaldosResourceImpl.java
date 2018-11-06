package br.com.gestao.financeira.pessoal.conta.rest;

import javax.inject.Inject;
import javax.ws.rs.Path;

import br.com.gestao.financeira.pessoal.conta.SaldoResource;
import br.com.gestao.financeira.pessoal.conta.SaldosResource;
import br.com.gestao.financeira.pessoal.lancamento.MeuFuturoResource;
import br.com.gestao.financeira.pessoal.lancamento.rest.LancamentosMensaisResourceImpl;

@Path("saldos")
public class SaldosResourceImpl implements SaldosResource {
	@Inject
	private SaldoResourceImpl saldoResourceImpl;
	@Inject
	private LancamentosMensaisResourceImpl lancamentosMensaisResource;

	@Override
	public MeuFuturoResource meuFuturo() {
		return lancamentosMensaisResource;
	}

	@Override
	public SaldoResource saldoResource(Integer ano, Integer mes) {
		return saldoResourceImpl.ano(ano).mes(mes);
	}

}
