package br.com.victorpfranca.mybudget.infra.jsf;

import javax.faces.application.ViewExpiredException;

import br.com.victorpfranca.mybudget.view.FacesUtils;

public class ViewExpiredExceptionResolver implements ExceptionResolver<ViewExpiredException> {

    @Override
    public Class<ViewExpiredException> handledType() {
        return ViewExpiredException.class;
    }

    @Override
    public boolean handle(ViewExpiredException cause) {
        FacesUtils.redirect(cause.getViewId());
        return true;
    }

}
