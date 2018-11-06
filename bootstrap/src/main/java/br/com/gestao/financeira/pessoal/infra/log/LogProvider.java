package br.com.gestao.financeira.pessoal.infra.log;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogProvider {

    @Produces
    @Default
    @Dependent
    public Logger produceGeneric(InjectionPoint ip) {
        return getLogger(ip.getMember().getDeclaringClass());
    }

    public static Logger getLogger(String category) {
        return LogManager.getLogger(category);
    }
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

}
