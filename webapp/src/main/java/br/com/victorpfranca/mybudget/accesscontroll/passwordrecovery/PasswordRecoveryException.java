package br.com.victorpfranca.mybudget.accesscontroll.passwordrecovery;


public class PasswordRecoveryException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public final PasswordRecoveryErrorCodes errorCode;

    public PasswordRecoveryException(PasswordRecoveryErrorCodes errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public PasswordRecoveryException(PasswordRecoveryErrorCodes errorCode, String arg0) {
        super(arg0);
        this.errorCode = errorCode;
    }

    public PasswordRecoveryException(PasswordRecoveryErrorCodes errorCode, Throwable arg0) {
        super(arg0);
        this.errorCode = errorCode;
    }

    public PasswordRecoveryException(PasswordRecoveryErrorCodes errorCode, String arg0, Throwable arg1) {
        super(arg0, arg1);
        this.errorCode = errorCode;
    }

}
