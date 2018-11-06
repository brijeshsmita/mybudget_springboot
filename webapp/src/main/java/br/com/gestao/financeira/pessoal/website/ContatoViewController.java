package br.com.gestao.financeira.pessoal.website;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.gestao.financeira.pessoal.infra.mail.MailSender;
import br.com.gestao.financeira.pessoal.view.FacesMessages;
import br.com.gestao.financeira.pessoal.view.Messages;
import br.com.gestao.financeira.pessoal.view.validation.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
@NoArgsConstructor
public class ContatoViewController implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String name;

	@NotNull
	@Email
	private String fromEmail;

	@NotNull
	private String message;

	@Inject
	private MailSender mailSender;

	public void send() {
		StringBuffer sb = new StringBuffer().append(fromEmail).append("<br/>").append(name).append("<br/>")
				.append(message);
		mailSender.sendMail(Messages.msg("contact.from.mail"), Messages.msg("contact.to.mail"),
				Messages.msg("contact.mail.title"), sb.toString());
		FacesMessages.info(Messages.msg("contact.sent.msg"));
	}

}
