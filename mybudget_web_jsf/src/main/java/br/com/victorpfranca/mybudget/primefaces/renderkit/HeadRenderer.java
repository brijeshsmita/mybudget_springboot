package br.com.victorpfranca.mybudget.primefaces.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.inject.Inject;

import br.com.victorpfranca.mybudget.infra.App;

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

		encodeCSS(context, "sentinel-layout", "css/font-icon-layout.css");
		encodeCSS(context, "sentinel-layout", "css/sentinel-layout.css");
		encodeCSS(context, "sentinel-layout", "css/core-layout.css");
		encodeCSS(context, "css", "meussaldos/base.css");
	}

}
