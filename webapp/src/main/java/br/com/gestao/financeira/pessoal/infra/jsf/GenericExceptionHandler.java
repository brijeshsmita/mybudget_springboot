package br.com.gestao.financeira.pessoal.infra.jsf;

import java.util.Objects;
import java.util.function.Consumer;

public class GenericExceptionHandler {
    public static <T extends Throwable> void handle(Class<T> type, Throwable exception, Consumer<T> consumer) {
        exception = Objects.requireNonNull(exception);
        consumer = Objects.requireNonNull(consumer);
        type = Objects.requireNonNull(type);
        execute(consumer, type, exception);
    }

    private static <T extends Throwable> void execute(Consumer<T> consumer, Class<T> type, Throwable exception) {
        new CustomExceptionResolver<T>(type, (et) -> {
            consumer.accept(et);
            return true;
        }).tryHandle(exception);
    }
}