package br.com.victorpfranca.mybudget.admin;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class SystemMonitorViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SystemMonitorService systemMonitorService;

	public Integer getActiveSessions() {
		return 0;
	}

	public int getQuantidadeNovosUsuariosDiaAtual() {
		return systemMonitorService.getQuantidadeNovosUsuariosDiaAtual();
	}

	public int getQuantidadeNovosUsuariosDiaAnterior() {
		return systemMonitorService.getQuantidadeNovosUsuariosDiaAnterior();
	}

	public int getQuantidadeNovosUsuariosUltimosDias(int qtdDias) {
		return systemMonitorService.getQuantidadeNovosUsuariosUltimosDias(qtdDias);
	}

	public int getQuantidadeAcessosDiaAtual() {
		return systemMonitorService.getQuantidadeAcessosDiaAtual();
	}

	public int getQuantidadeAcessosDiaAnterior() {
		return systemMonitorService.getQuantidadeAcessosDiaAnterior();
	}

	public int getQuantidadeAcessosUltimosDias(int qtdDias) {
		return systemMonitorService.getQuantidadeAcessosUltimosDias(qtdDias);
	}

}
