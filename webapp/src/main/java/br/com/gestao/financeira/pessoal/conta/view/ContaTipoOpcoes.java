package br.com.gestao.financeira.pessoal.conta.view;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import br.com.gestao.financeira.pessoal.conta.ContaTipo;

/**
 * Componente para ser utilizado em listas de opções em páginas JDF(ex:ex:
 * <p:selectOneRadio id="inOut" value="#{categoriaViewController.objeto.inOut}"
 * required="true" layout="grid" columns="1">
 * <f:selectItems value="#{inOutOpcoes}" var="inOut" itemValue="#{inOut}"
 * itemLabel="#{inOut.descricao}" /> </p:selectOneRadio>)
 * 
 * @author victorfranca
 *
 */
public class ContaTipoOpcoes {

	@Produces
	@Named("contaTipoOpcoes")
	@RequestScoped
	public ContaTipo[] getOpcoes() {
		return ContaTipo.values();
	}

}
