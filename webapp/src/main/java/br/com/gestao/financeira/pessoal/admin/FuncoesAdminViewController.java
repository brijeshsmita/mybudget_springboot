package br.com.gestao.financeira.pessoal.admin;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.conta.rules.ContaService;
import br.com.gestao.financeira.pessoal.view.AnoMes;

@Named
@ViewScoped
public class FuncoesAdminViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	Map<Conta, Map<AnoMes, SaldoConta>> saldosReconstruidos;

	@Inject
	private ContaService contaService;

	@PostConstruct
	public void init() {

	}

	public void reconstruirSaldosContas() {
		saldosReconstruidos = contaService.reconstruirSaldosContas();
	}

	public Map<Conta, Map<AnoMes, SaldoConta>> getSaldosReconstruidos() {
		return saldosReconstruidos;
	}

}
