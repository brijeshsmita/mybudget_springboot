package br.com.victorpfranca.mybudget.view;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;

import br.com.victorpfranca.mybudget.infra.log.LogProvider;

public class FacesUtils {
    private FacesUtils() {
    }

    public static void redirect(String path) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            String encodeActionURL = externalContext
                    .encodeActionURL(externalContext.getApplicationContextPath() + path);
            if ("partial/ajax".equals(request.getHeader("faces-request"))
                    || "partial/process".equals(request.getHeader("faces-request"))) {

                StringBuilder result = new StringBuilder();
                result.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                result.append("<partial-response>");
                result.append("<redirect url=\"").append(encodeActionURL).append("\">");
                result.append("</redirect>");
                result.append("</partial-response>");
                String outputPath = result.toString();
                facesContext.getExternalContext().getResponseOutputWriter().write(outputPath);
                facesContext.getExternalContext().responseFlushBuffer();
                facesContext.responseComplete();
            } else {
                externalContext.redirect(encodeActionURL);
                facesContext.responseComplete();
            }
        } catch (IOException e) {
            Logger logger = LogProvider.getLogger(FacesUtils.class);
            logger.error(e);
        }
    }

}
