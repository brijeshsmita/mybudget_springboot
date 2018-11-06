package br.com.gestao.financeira.pessoal.infra.jsf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.shiro.SecurityUtils;

import com.google.gson.Gson;

import br.com.gestao.financeira.pessoal.infra.log.LogProvider;
import br.com.gestao.financeira.pessoal.view.FacesMessages;

public class AppExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    private Set<Class<? extends ExceptionResolver<?>>> resolvers = new HashSet<>();

    AppExceptionHandler(ExceptionHandler exception) {
        this.wrapped = exception;
        this.resolvers.add(SystemExceptionResolver.class);
        this.resolvers.add(UnknownAccountExceptionResolver.class);
        this.resolvers.add(ViewExpiredExceptionResolver.class);
    }

    private ExceptionResolver<?> instantiate(Class<? extends ExceptionResolver<?>> inst) {
        try {
            return inst.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = getUnhandledExceptionQueuedEvents().iterator();

        if (!unhandledExceptionQueuedEvents.hasNext()) {
            return; // There's no unhandled exception.
        }
        boolean handled = false;
        List<Throwable> unhandled = new ArrayList<>();
        do {
            Throwable exception = unhandledExceptionQueuedEvents.next().getContext().getException();
            unhandledExceptionQueuedEvents.remove();

            for (Class<? extends ExceptionResolver<?>> exceptionResolverClass : resolvers) {
                ExceptionResolver<?> exceptionResolver = instantiate(exceptionResolverClass);
                handled = exceptionResolver.tryHandle(exception);
                if (handled) {
                    break;
                }
            }
            if (!handled) {
                unhandled.add(exception);
            }
        } while (!handled && unhandledExceptionQueuedEvents.hasNext());
        if (!handled && !unhandled.isEmpty()) {
            Throwable exception = unhandled.iterator().next();
            handleUncaughtException(exception);
        }

        wrapped.handle();
    }

    private void handleUncaughtException(Throwable exception) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = null;
        if (facesContext != null) {
            request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        }
        handleUncaughtException(request, exception);
        FacesMessages.error(null, "Ocorreu um erro inesperado");
    }

    public static String handleUncaughtException(HttpServletRequest request, Throwable exception) {
        Map<String, String> customLogParams = new TreeMap<>();
        if (request != null) {
            for (String name : Collections.list(request.getHeaderNames())) {
                customLogParams.put(name, request.getHeader(name));
            }
            customLogParams.put("ip", request.getRemoteAddr());
            if (SecurityUtils.getSubject().isAuthenticated()) {
                customLogParams.put("login", SecurityUtils.getSubject().getPrincipal().toString());
            }
        }
        String id = UUID.randomUUID().toString();
        try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.put("id",
                id)) {
            ctc.put("ip", customLogParams.remove("ip"));
            ctc.put("login", customLogParams.remove("login"));

            ctc.put("headers", new Gson().toJson(customLogParams));
            LogProvider.getLogger(AppExceptionHandler.class).error(exception.getMessage(), exception);
        }
        return id;
    }

}
