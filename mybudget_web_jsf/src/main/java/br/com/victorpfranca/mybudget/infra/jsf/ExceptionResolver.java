package br.com.victorpfranca.mybudget.infra.jsf;

public interface ExceptionResolver<T> {

    Class<T> handledType();

    boolean handle(T cause);

    default boolean tryHandle(Throwable t) {
        boolean result = false;
        for (Throwable cause = t; cause != null; cause = cause.getCause()) {
            if (handledType().isInstance(cause)) {
                result = handle(handledType().cast(cause));
                break;
            }
        }
        return result;
    }
}
