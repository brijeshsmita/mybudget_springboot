package br.com.gestao.financeira.pessoal.view;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import br.com.gestao.financeira.pessoal.InOut;

/**
 * Componente para ser utilizado em listas de opções em páginas JDF(ex:
 * <p:selectOneRadio id="inOut" value="#{categoriaViewController.objeto.inOut}"
 * required="true" layout="grid" columns="1">
 * <f:selectItems value="#{inOutOpcoes}" var="inOut" itemValue="#{inOut}"
 * itemLabel="#{inOut.descricao}" /> </p:selectOneRadio>)
 * 
 * @author victorfranca
 *
 */
public class InOutOpcoes {

	@Produces
	@Named("inOutOpcoes")
	@RequestScoped
	public InOut[] getOpcoes() {
		return InOut.values();
	}

}
