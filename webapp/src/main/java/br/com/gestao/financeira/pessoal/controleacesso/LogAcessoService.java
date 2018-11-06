package br.com.gestao.financeira.pessoal.controleacesso;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.infra.date.api.CurrentDateSupplier;

@Stateless
public class LogAcessoService {

	@EJB
	private CurrentDateSupplier dateUtils;
	
	@EJB
	private UsuarioService usuarioService;
	
	@Inject
	private EntityManager em;

	public void log(Usuario usuario) {
		LogAcesso logAcesso = new LogAcesso();
		logAcesso.setData(dateUtils.currentDate());
		logAcesso.setUsuario(usuario);
		
		em.merge(logAcesso);
		
		usuarioService.incrementarContadorAcesso(usuario);
	}

}
