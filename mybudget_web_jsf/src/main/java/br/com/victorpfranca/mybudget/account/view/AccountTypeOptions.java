package br.com.victorpfranca.mybudget.account.view;

import br.com.victorpfranca.mybudget.account.AccountType;

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
public class AccountTypeOptions {

	public AccountType[] getOpcoes() {
		return AccountType.values();
	}

}
