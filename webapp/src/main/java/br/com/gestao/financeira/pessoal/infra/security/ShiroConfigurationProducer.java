package br.com.gestao.financeira.pessoal.infra.security;

import java.util.Arrays;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;

import br.com.gestao.financeira.pessoal.infra.security.jwt.JWTFilter;

public class ShiroConfigurationProducer {

    @Inject private SecurityRealm securityRealm;
    @Inject private CryptoPasswordService passwordService;
    @Inject private JWTFilter jwtFilter;

    @Produces
    public WebSecurityManager getSecurityManager() {
        DefaultWebSecurityManager securityManager = null;
        if (securityManager == null) {
            PasswordMatcher passwordMatcher = new PasswordMatcher();
            passwordMatcher.setPasswordService(passwordService);
            securityRealm.setCredentialsMatcher(passwordMatcher);
            securityManager = new DefaultWebSecurityManager(securityRealm);
            addRememberMeManager(securityManager);
        }
        return securityManager;
    }

    private void addRememberMeManager(DefaultWebSecurityManager securityManager) {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCipherKey(String
                .format("0x%s", Hex.encodeToString(new AesCipherService().generateNewKey().getEncoded())).getBytes());
        securityManager.setRememberMeManager(rememberMeManager);
    }

    @Produces
    public FilterChainResolver getFilterChainResolver() {
        FilterChainResolver filterChainResolver = null;
        if (filterChainResolver == null) {

            RolesAuthorizationFilter rolesAuthorizationFilter = new RolesAuthorizationFilter();
            AnonymousFilter anon = new AnonymousFilter();
            UserFilter user = new UserFilter();

            LogoutFilter logout = new LogoutFilter();
            logout.setRedirectUrl("/login");

            user.setLoginUrl("/login");
            rolesAuthorizationFilter.setUnauthorizedUrl("/modulos/home");
            rolesAuthorizationFilter.setLoginUrl("/login");

            DefaultFilterChainManager filterChainManager = new DefaultFilterChainManager();
            filterChainManager.addFilter("logout", logout);
            filterChainManager.addFilter("anon", anon);
            filterChainManager.addFilter("user", user);
            filterChainManager.addFilter("jwt", jwtFilter);
            filterChainManager.addFilter("roles", rolesAuthorizationFilter);
            registrarPaginasPublicas(filterChainManager);
            
            filterChainManager.createChain("/logout", "logout");
            filterChainManager.createChain("/admin/**", "user, roles[ADMIN]");
            filterChainManager.createChain("/exemplos/**", "user, roles[ADMIN]");
            filterChainManager.createChain("/modulos/**", "user");
            filterChainManager.createChain("/services/**", "jwt");
            

            PathMatchingFilterChainResolver resolver = new PathMatchingFilterChainResolver();
            resolver.setFilterChainManager(filterChainManager);
            filterChainResolver = resolver;
        }
        return filterChainResolver;
    }

    private void registrarPaginasPublicas(DefaultFilterChainManager filterChainManager) {
        Arrays.asList("/index", "/login", "/signup", "/error", "/access_denied", "/404", "/javax.faces.resource/**",
                "/recoverPassword",
                "/services/auth/**").forEach(publicPage -> filterChainManager.createChain(publicPage, "anon"));
    }

}
