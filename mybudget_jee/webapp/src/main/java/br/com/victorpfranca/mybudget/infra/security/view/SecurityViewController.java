package br.com.victorpfranca.mybudget.infra.security.view;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

@Named("security")
@RequestScoped
public class SecurityViewController implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean hasRole(String role) {
        Subject subject = SecurityUtils.getSubject();
        return subject.isAuthenticated() && subject.hasRole(role);
    }


}
