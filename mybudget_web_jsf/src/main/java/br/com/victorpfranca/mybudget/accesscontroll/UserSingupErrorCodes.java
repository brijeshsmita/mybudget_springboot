package br.com.victorpfranca.mybudget.accesscontroll;

import br.com.victorpfranca.mybudget.infra.exception.ErrorCode;

public enum UserSingupErrorCodes implements ErrorCode {
    EMAIL_JA_CADASTRADO(1);

    private UserSingupErrorCodes(final int code) {
        this.code = code;
    }

    private final int code;

    @Override
    public int getCode() {
        return code;
    }

}
