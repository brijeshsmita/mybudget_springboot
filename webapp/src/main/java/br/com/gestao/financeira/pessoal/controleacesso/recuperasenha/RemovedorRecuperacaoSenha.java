package br.com.gestao.financeira.pessoal.controleacesso.recuperasenha;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;

import br.com.gestao.financeira.pessoal.infra.OrquestradorTimers;

@Singleton
public class RemovedorRecuperacaoSenha {

	@EJB
	private RecuperacaoSenhaService recuperacaoSenhaService;

	@Schedule(minute = "*/1", hour = "*", persistent = false)
	@Interceptors({ OrquestradorTimers.class })
	public void remocaoPeriodicaDeRecuperacaoSenha() {
		recuperacaoSenhaService.inativarCodigosExpirados();
	}

}
