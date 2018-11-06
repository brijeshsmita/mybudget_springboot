package br.com.gestao.financeira.pessoal.infra.jsf;

import javax.faces.application.ViewExpiredException;

import br.com.gestao.financeira.pessoal.view.FacesUtils;

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
