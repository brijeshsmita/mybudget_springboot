package br.com.gestao.financeira.pessoal.infra;

import java.util.Optional;

import javax.ejb.Singleton;
import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

@Named
@Singleton
public class App {

    private Optional<ProjectStage> projectStage() {
        return Optional.ofNullable(FacesContext.getCurrentInstance()).map(FacesContext::getApplication)
                .map(Application::getProjectStage);
    }

    public String getProjectStage() {
        return projectStage().map(ProjectStage::name).get();
    }

    public boolean isProductionMode() {
        return projectStage().orElse(ProjectStage.Development).equals(ProjectStage.Production);
    }

    public boolean isDevelopmentMode() {
        return !isProductionMode();
    }

    public static App instance() {
        return lookup("java:module/App");
    }

    @SuppressWarnings("unchecked")
    private static <T> T lookup(String name) {
        InitialContext context = null;
        try {
            context = new InitialContext();
            return (T) context.lookup(name);
        } catch (NamingException e) {
            if (is(e, NameNotFoundException.class)) {
                return null;
            } else {
                throw new IllegalStateException(e);
            }
        } finally {
            close(context);
        }
    }

    private static void close(InitialContext context) {
        try {
            if (context != null) {
                context.close();
            }
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }

    }

    private static <T extends Throwable> boolean is(Throwable exception, Class<T> type) {
        Throwable unwrappedException = exception;

        while (unwrappedException != null) {
            if (type.isInstance(unwrappedException)) {
                return true;
            }

            unwrappedException = unwrappedException.getCause();
        }

        return false;
    }

}
