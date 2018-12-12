package br.com.victorpfranca.mybudget.transaction.view;

import br.com.victorpfranca.mybudget.transaction.TransactionStatus;

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
public class TransactionStatusOptions {

	public TransactionStatus[] getOpcoes() {
		return TransactionStatus.values();
	}

}
