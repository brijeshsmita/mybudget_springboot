package br.com.victorpfranca.mybudget.infra.jsf;

import br.com.victorpfranca.mybudget.infra.exception.ErrorCode;
import br.com.victorpfranca.mybudget.infra.exception.SystemException;
import br.com.victorpfranca.mybudget.view.FacesMessages;
import br.com.victorpfranca.mybudget.view.Messages;

public class SystemExceptionResolver implements ExceptionResolver<SystemException> {

    @Override
    public boolean handle(SystemException cause) {
        ErrorCode errorCode = cause.getErrorCode();
        if (errorCode != null) {
            FacesMessages.error(cause.handleMessage(Messages::msg));
        }
        return errorCode != null;
    }

    @Override
    public Class<SystemException> handledType() {
        return SystemException.class;
    }

}
