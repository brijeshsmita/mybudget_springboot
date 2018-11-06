package br.com.gestao.financeira.pessoal.infra.jsf;

import br.com.gestao.financeira.pessoal.infra.exception.ErrorCode;
import br.com.gestao.financeira.pessoal.infra.exception.SystemException;
import br.com.gestao.financeira.pessoal.view.FacesMessages;
import br.com.gestao.financeira.pessoal.view.Messages;

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
