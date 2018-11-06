package br.com.gestao.org.primefaces.renderkit;

import java.io.IOException;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;

import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.infra.App;

public class HeadRenderer extends org.primefaces.renderkit.HeadRenderer {

	@Inject
	private App app;
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		super.encodeBegin(context, component);
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("meta", component);
		writer.writeAttribute("http-equiv", "X-UA-Compatible", null);
		writer.writeAttribute("content", "IE=edge", null);
		writer.endElement("meta");
		writer.startElement("meta", component);
		writer.writeAttribute("http-equiv", "Content-Type", null);
		writer.writeAttribute("content", "text/html; charset=UTF-8", null);
		writer.endElement("meta");
		writer.startElement("meta", component);
		writer.writeAttribute("name", "viewport", null);
		writer.writeAttribute("content", "width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0",
				null);
		writer.endElement("meta");
		writer.startElement("meta", component);
		writer.writeAttribute("name", "apple-mobile-web-app-capable", null);
		writer.writeAttribute("content", "yes", null);
		writer.endElement("meta");

		
		//Global site tag (gtag.js) - Google Analytics
		if (app.isProductionMode() && !SecurityUtils.getSubject().hasRole("ADMIN")) {
			writer.startElement("script", null);
			writer.writeAttribute("async", "async", null);
			writer.writeAttribute("src", "https://www.googletagmanager.com/gtag/js?id=UA-123316837-1", null);
			writer.endElement("script");

			writer.startElement("script", null);
			writer.writeText(
					"window.dataLayer = window.dataLayer || [];\n" + "		function gtag() {\n"
							+ "			dataLayer.push(arguments);\n" + "		}\n"
							+ "		gtag('js', new Date());\n"
							+ "\n" + "		gtag('config', 'AW-795005430')" //ID de conversãod e campanha
							+ "\n" + "		gtag('config', 'UA-123316837-1');",
					null);
			writer.endElement("script");
		}

		encodeCSS(context, "sentinel-layout", "css/font-icon-layout.css");
		encodeCSS(context, "sentinel-layout", "css/sentinel-layout.css");
		encodeCSS(context, "sentinel-layout", "css/core-layout.css");
		encodeCSS(context, "css", "meussaldos/base.css");
	}

}
