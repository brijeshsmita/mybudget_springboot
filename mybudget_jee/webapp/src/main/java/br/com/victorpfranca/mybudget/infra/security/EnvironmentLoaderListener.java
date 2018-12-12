package br.com.victorpfranca.mybudget.infra.security;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.apache.shiro.web.env.DefaultWebEnvironment;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.primefaces.component.captcha.Captcha;

@WebListener
public class EnvironmentLoaderListener extends org.apache.shiro.web.env.EnvironmentLoaderListener {

    @Inject
    private WebSecurityManager securityManager;
    @Inject
    private FilterChainResolver filterChainResolver;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setInitParameter(ENVIRONMENT_CLASS_PARAM, DefaultWebEnvironment.class.getName());
        sce.getServletContext().setInitParameter(Captcha.PUBLIC_KEY, "6Lc7GGEUAAAAAOcoG82igQpDixoM7ZnvrShFKdMx");
        sce.getServletContext().setInitParameter(Captcha.PRIVATE_KEY, "6Lc7GGEUAAAAAKV5osG7CdMCDDxtt0SJCs_3vcGi");
        super.contextInitialized(sce);
    }

    @Override
    protected WebEnvironment createEnvironment(ServletContext sc) {
        DefaultWebEnvironment webEnvironment = (DefaultWebEnvironment) super.createEnvironment(sc);
        webEnvironment.setSecurityManager(securityManager);
        webEnvironment.setFilterChainResolver(filterChainResolver);
        return webEnvironment;
    }
}
