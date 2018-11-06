package br.com.gestao.financeira.pessoal.controleacesso;

import br.com.gestao.financeira.pessoal.infra.exception.ErrorCode;

public enum CadastroUsuarioErrorCodes implements ErrorCode {
    EMAIL_JA_CADASTRADO(1);

    private CadastroUsuarioErrorCodes(final int code) {
        this.code = code;
    }

    private final int code;

    @Override
    public int getCode() {
        return code;
    }

}
