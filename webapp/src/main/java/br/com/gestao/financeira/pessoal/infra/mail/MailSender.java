package br.com.gestao.financeira.pessoal.infra.mail;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.MediaType;

import br.com.gestao.financeira.pessoal.infra.App;

public class MailSender implements Serializable {

	private static final long serialVersionUID = 1L;

	@Resource(mappedName = "java:jboss/mail/meussaldos")
	private Session mailSession;
	private String mailContentType;

	private String mailEncoding;

	@EJB
	private App app;

	@PostConstruct
	void postConstruct() {
		this.mailContentType = MediaType.TEXT_HTML;
		this.mailEncoding = StandardCharsets.UTF_8.displayName();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void sendMail(String sMailFrom, String sMailTo, String sSubject, String sMailText) {
		if (app.isProductionMode()) {
			try {
				MimeMessage message = new MimeMessage(mailSession);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(sMailTo));
				message.setHeader("Content-Type", this.mailContentType + "; charset=\"" + this.mailEncoding + "\"");
				message.setSubject(sSubject);
				message.setFrom(sMailFrom);
				message.setText(sMailText, StandardCharsets.UTF_8.displayName(), "html");
				Transport.send(message);
			} catch (MessagingException e) {
				throw new EJBException(e);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void sendMail(String sMailTo, String sSubject, String sMailText) {
		if (app.isProductionMode()) {
			try {
				MimeMessage message = new MimeMessage(mailSession);
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(sMailTo));
				message.setHeader("Content-Type", this.mailContentType + "; charset=\"" + this.mailEncoding + "\"");
				message.setSubject(sSubject);
				message.setText(sMailText, StandardCharsets.UTF_8.displayName(), "html");
				Transport.send(message);
			} catch (MessagingException e) {
				throw new EJBException(e);
			}
		}
	}

}
