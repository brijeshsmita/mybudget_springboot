package br.com.victorpfranca.mybudget.accesscontroll;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.victorpfranca.mybudget.infra.date.api.CurrentDateSupplier;

@Stateless
public class LogAcessoService {

	@EJB
	private CurrentDateSupplier dateUtils;
	
	@EJB
	private UserService userService;
	
	@Inject
	private EntityManager em;

	public void log(User user) {
		LogAcesso logAcesso = new LogAcesso();
		logAcesso.setData(dateUtils.currentDate());
		logAcesso.setUsuario(user);
		
		em.merge(logAcesso);
		
		userService.incrementarContadorAcesso(user);
	}

}
