package br.com.victorpfranca.mybudget.infra.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.accesscontroll.UserService;
import br.com.victorpfranca.mybudget.infra.jsf.GenericExceptionHandler;
import br.com.victorpfranca.mybudget.infra.security.jwt.JwtAuthenticationToken;

public class SecurityRealm extends AuthorizingRealm {
    @Inject private Logger logger;
    @Inject private UserService userService;

    public SecurityRealm() {
        super();
    }
    @Override
    public boolean supports(AuthenticationToken token) {
        return super.supports(token) || token instanceof JwtAuthenticationToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null");
        }
        String username = (String) getAvailablePrincipal(principals);
        Set<String> roleNames = new HashSet<>(getRoleNamesForUsuario(username));
        return new SimpleAuthorizationInfo(roleNames);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String email = (String) token.getPrincipal();
        if (email == null) {
            logger.warn("User is null");
            return null;
        }
        try {
            User user = userService.recuperarViaEmail(email);
            if (!user.getAtivo()) {
                throw new DisabledAccountException();
            }
            return new SimpleAuthenticationInfo(user.getEmail(), user.getSenha(), getName());
        } catch (Exception e) {
            GenericExceptionHandler.handle(NoResultException.class, e, r -> {
                throw new IncorrectCredentialsException();
            });
            throw e;
        }
    }

    private List<String> getRoleNamesForUsuario(String username) {
        boolean isAdmin = Arrays.asList("victorhugof@gmail.com", "erik.liberal@gmail.com", "erik.liberal+adminmeussaldos@gmail.com").stream().anyMatch(s-> s.equals(StringUtils.lowerCase(username)));
        return isAdmin ? Collections.singletonList("ADMIN") : Collections.singletonList("USER");
    }

}