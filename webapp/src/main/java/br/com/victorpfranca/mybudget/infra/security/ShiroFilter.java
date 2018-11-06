package br.com.victorpfranca.mybudget.infra.security;

import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = "/*", dispatcherTypes = { DispatcherType.FORWARD, DispatcherType.REQUEST,
        DispatcherType.INCLUDE, DispatcherType.ERROR })
public class ShiroFilter extends org.apache.shiro.web.servlet.ShiroFilter {

}
