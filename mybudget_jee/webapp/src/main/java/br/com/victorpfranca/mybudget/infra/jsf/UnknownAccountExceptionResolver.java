package br.com.victorpfranca.mybudget.infra.jsf;

import org.apache.shiro.authc.UnknownAccountException;

import br.com.victorpfranca.mybudget.view.FacesMessages;
import br.com.victorpfranca.mybudget.view.Messages;

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

