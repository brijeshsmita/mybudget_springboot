package br.com.victorpfranca.mybudget.accesscontroll.passwordrecovery;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;

import br.com.victorpfranca.mybudget.infra.OrquestradorTimers;

@Singleton
public class PasswordRecoveryRemover {

	@EJB
	private PasswordRecoveryService passwordRecoveryService;

	@Schedule(minute = "*/1", hour = "*", persistent = false)
	@Interceptors({ OrquestradorTimers.class })
	public void remocaoPeriodicaDeRecuperacaoSenha() {
		passwordRecoveryService.inativarCodigosExpirados();
	}

}
