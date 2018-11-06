package br.com.gestao.financeira.pessoal.controleacesso.recuperasenha;


public class RecuperacaoSenhaException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public final RecuperacaoSenhaErrorCodes errorCode;

    public RecuperacaoSenhaException(RecuperacaoSenhaErrorCodes errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public RecuperacaoSenhaException(RecuperacaoSenhaErrorCodes errorCode, String arg0) {
        super(arg0);
        this.errorCode = errorCode;
    }

    public RecuperacaoSenhaException(RecuperacaoSenhaErrorCodes errorCode, Throwable arg0) {
        super(arg0);
        this.errorCode = errorCode;
    }

    public RecuperacaoSenhaException(RecuperacaoSenhaErrorCodes errorCode, String arg0, Throwable arg1) {
        super(arg0, arg1);
        this.errorCode = errorCode;
    }

}
