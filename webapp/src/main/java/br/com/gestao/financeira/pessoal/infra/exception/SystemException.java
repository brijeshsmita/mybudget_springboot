package br.com.gestao.financeira.pessoal.infra.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final ErrorCode errorCode;
    private final Map<String, Object> params;

    private SystemException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.params = new HashMap<>();
    }

    public SystemException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
        this.params = new HashMap<>();
    }
    
    public SystemException param(String key, Object value) {
        params.put(key, value);
        return this;
    }
    
    public static SystemException create(ErrorCode errorCode, Throwable cause) {
        if (cause instanceof SystemException)
            return (SystemException) cause;
        return new SystemException(errorCode, cause);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getParams() {
        return Collections.unmodifiableMap(params);
    }

    public String getI18nKey() {
        return String.format("%s.%03d", getErrorCode().getClass().getName(), getErrorCode().getCode());
    }
    public String handleMessage(Function<String,String> handler) {
        return handler.andThen(this::interpolateParams).apply(getI18nKey());
    }
    private String interpolateParams(String msg) {
        String message=msg;
        for (Entry<String, Object> entry : getParams().entrySet().stream().filter(e -> e.getValue() != null)
                .collect(Collectors.toList())) {
            message=message.replace(String.format("{{%s}}", entry.getKey()), entry.getValue().toString());
        }
        return message;
    }

}
