package br.com.gestao.financeira.pessoal.infra.jsf;

import org.apache.shiro.authc.UnknownAccountException;

import br.com.gestao.financeira.pessoal.view.FacesMessages;
import br.com.gestao.financeira.pessoal.view.Messages;

public class UnknownAccountExceptionResolver implements ExceptionResolver<UnknownAccountException> {

    @Override
    public boolean handle(UnknownAccountException cause) {
        FacesMessages.error(Messages.msg(UnknownAccountException.class.getName()));
        return true;
    }

    @Override
    public Class<UnknownAccountException> handledType() {
        return UnknownAccountException.class;
    }

}

