package br.com.victorpfranca.mybudget.view;

import br.com.victorpfranca.mybudget.InOut;

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
public class InOutOptions {

	public InOut[] getOpcoes() {
		return InOut.values();
	}

}
