package br.com.gestao.financeira.pessoal.infra;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;

@Stateless
public class OrquestradorTimers {

	@Resource
	private TimerService timerService;

	@EJB
	private App app;

	@AroundTimeout
	public Object intercept(InvocationContext ctx) throws Exception {
		if (!app.isProductionMode()) {
			for (Timer timer : timerService.getTimers()) {
				timer.cancel();
			}
		}

		return ctx.proceed();
	}

}
