package br.com.victorpfranca.mybudget.infra.jsf;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory parent;
    
    public AppExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new AppExceptionHandler(parent.getExceptionHandler());
    }

}
