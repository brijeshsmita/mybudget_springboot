package br.com.victorpfranca.mybudget.accesscontroll.passwordrecovery;

public enum PasswordRecoveryErrorCodes {
    PRAZO_RECUPERACAO_INVALIDO(1), CODIGO_RECUPERACAO_INATIVO(2), CODIGO_RECUPERACAO_INCORRETO(3), USUARIO_INEXISTENTE(
            4), USUARIO_INATIVO(5);

    private PasswordRecoveryErrorCodes(int code) {
        this.code = code;
    }

    public final int code;
}
