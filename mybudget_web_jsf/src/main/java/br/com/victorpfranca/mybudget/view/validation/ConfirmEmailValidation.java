package br.com.victorpfranca.mybudget.view.validation;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.PostValidateEvent;
import javax.inject.Named;

import br.com.victorpfranca.mybudget.view.FacesMessages;
import br.com.victorpfranca.mybudget.view.Messages;

@Named
@RequestScoped
public class ConfirmEmailValidation implements Serializable {

    private static final long serialVersionUID = 1L;

    public void validate(HtmlInputText email, HtmlInputText confirmEmail) {
        if (!Objects.equals(email.getValue(), confirmEmail.getValue())) {
            email.setStyleClass(email.getStyleClass() + " ui-state-error");
            confirmEmail.setStyleClass(email.getStyleClass() + " ui-state-error");
            FacesMessages.error(confirmEmail.getClientId(), Messages.msg("email.confirmation.error"));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    public void emailValidation(PostValidateEvent postValidateEvent) {
        UIComponent container = postValidateEvent.getComponent();

        Map<String, Object> attributes = container.getAttributes();
        Object emailId = attributes.get("emailId");
        Object confirmEmailId = attributes.get("confirmEmailId");
        UIComponent email = container.findComponent((String) emailId);
        UIComponent confirmEmail = container.findComponent((String) confirmEmailId);

        if (email instanceof HtmlInputText && confirmEmail instanceof HtmlInputText) {
            validate((HtmlInputText) email, (HtmlInputText) confirmEmail);
        }

    }

}
