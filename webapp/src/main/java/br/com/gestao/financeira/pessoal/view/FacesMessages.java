package br.com.gestao.financeira.pessoal.view;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class FacesMessages {
	private FacesMessages(){}
	
	public static void addMessage(Severity severity, String clientId, String summary, String detail){
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(clientId, message);
	}
	public static void addMessage(Severity severity, String clientId, String summary){
		FacesMessages.addMessage(severity, clientId, summary, null);
	}
	public static void addMessage(Severity severity, String summary){
		FacesMessages.addMessage(severity, null, summary, null);
	}
	
	public static void info(String clientId, String summary, String detail){
		FacesMessages.addMessage(FacesMessage.SEVERITY_INFO, clientId, summary, detail);
	}
	public static void info(String clientId, String summary){
		FacesMessages.addMessage(FacesMessage.SEVERITY_INFO, clientId, summary, null);
	}
	public static void info(String summary){
		FacesMessages.addMessage(FacesMessage.SEVERITY_INFO, null, summary, null);
	}
	public static void warn(String clientId, String summary, String detail){
		FacesMessages.addMessage(FacesMessage.SEVERITY_WARN, clientId, summary, detail);
	}
	public static void warn(String clientId, String summary){
		FacesMessages.addMessage(FacesMessage.SEVERITY_WARN, clientId, summary, null);
	}
	public static void warn(String summary){
		FacesMessages.addMessage(FacesMessage.SEVERITY_WARN, null, summary, null);
	}
	public static void error(String clientId, String summary, String detail){
		FacesMessages.addMessage(FacesMessage.SEVERITY_ERROR, clientId, summary, detail);
	}
	public static void error(String clientId, String summary){
		FacesMessages.addMessage(FacesMessage.SEVERITY_ERROR, clientId, summary, null);
	}
	public static void error(String summary){
		FacesMessages.addMessage(FacesMessage.SEVERITY_ERROR, null, summary, null);
	}
	public static void fatal(String clientId, String summary, String detail){
		FacesMessages.addMessage(FacesMessage.SEVERITY_FATAL, clientId, summary, detail);
	}
	public static void fatal(String clientId, String summary){
		FacesMessages.addMessage(FacesMessage.SEVERITY_FATAL, clientId, summary, null);
	}
	public static void fatal(String summary){
		FacesMessages.addMessage(FacesMessage.SEVERITY_FATAL, null, summary, null);
	}
	
}
