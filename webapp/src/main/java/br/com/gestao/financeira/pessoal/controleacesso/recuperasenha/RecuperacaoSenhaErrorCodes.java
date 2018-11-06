package br.com.gestao.financeira.pessoal.controleacesso.recuperasenha;

public enum RecuperacaoSenhaErrorCodes {
    PRAZO_RECUPERACAO_INVALIDO(1), CODIGO_RECUPERACAO_INATIVO(2), CODIGO_RECUPERACAO_INCORRETO(3), USUARIO_INEXISTENTE(
            4), USUARIO_INATIVO(5);

    private RecuperacaoSenhaErrorCodes(int code) {
        this.code = code;
    }

    public final int code;
}
