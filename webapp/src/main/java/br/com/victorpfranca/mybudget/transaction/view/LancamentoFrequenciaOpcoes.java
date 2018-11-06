package br.com.victorpfranca.mybudget.transaction.view;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import br.com.victorpfranca.mybudget.transaction.TransactionFrequence;

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
public class LancamentoFrequenciaOpcoes {

	@Produces
	@Named("lancamentoFrequenciaOpcoes")
	@RequestScoped
	public TransactionFrequence[] getOpcoes() {
		return TransactionFrequence.values();
	}

}
