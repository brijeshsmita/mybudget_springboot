package br.com.gestao.financeira.pessoal.view.validation;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.context.FacesContext;
import javax.faces.event.PostValidateEvent;
import javax.inject.Named;

import org.primefaces.component.password.Password;

import br.com.gestao.financeira.pessoal.view.FacesMessages;
import br.com.gestao.financeira.pessoal.view.Messages;

@Named
@RequestScoped
public class ConfirmPasswordValidation implements Serializable {

    private static final long serialVersionUID = 1L;

    public void validatePassword(Password password, Password confirmPassword) {
        if (!Objects.equals(password.getValue(), confirmPassword.getValue())) {
            password.setStyleClass(password.getStyleClass() + " ui-state-error");
            confirmPassword.setStyleClass(password.getStyleClass() + " ui-state-error");
            FacesMessages.error(confirmPassword.getClientId(), Messages.msg("password.confirmation.error"));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    private void validateInputSecret(HtmlInputSecret password, HtmlInputSecret confirmPassword) {
        if (!Objects.equals(password.getValue(), confirmPassword.getValue())) {
            password.setStyleClass(password.getStyleClass() + " ui-state-error");
            confirmPassword.setStyleClass(password.getStyleClass() + " ui-state-error");
            FacesMessages.error(confirmPassword.getClientId(), Messages.msg("password.confirmation.error"));
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    public void passwordValidation(PostValidateEvent postValidateEvent) {
        UIComponent container = postValidateEvent.getComponent();

        Map<String, Object> attributes = container.getAttributes();
        Object passwordId = attributes.get("passwordId");
        Object confirmPasswordId = attributes.get("confirmPasswordId");
        UIComponent password = container.findComponent((String) passwordId);
        UIComponent confirmPassword = container.findComponent((String) confirmPasswordId);

        if (password instanceof Password && confirmPassword instanceof Password) {
            validatePassword((Password) password, (Password) confirmPassword);
        } else if (password instanceof HtmlInputSecret && confirmPassword instanceof HtmlInputSecret) {
            validateInputSecret((HtmlInputSecret) password, (HtmlInputSecret) confirmPassword);
        }

    }

}
