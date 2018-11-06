package br.com.gestao.financeira.pessoal.infra.jsf;

import java.util.Objects;
import java.util.function.Function;

class CustomExceptionResolver<T extends Throwable> implements ExceptionResolver<T> {

    private Class<T> handledType;
    private Function<T, Boolean> handleFunction;

    public CustomExceptionResolver(Class<T> handledType, Function<T, Boolean> handleFunction) {
        this.handledType = Objects.requireNonNull(handledType);
        this.handleFunction = Objects.requireNonNull(handleFunction);
    }

    @Override
    public Class<T> handledType() {
        return handledType;
    }

    @Override
    public boolean handle(T cause) {
        return handleFunction.apply(cause);
    }

}
