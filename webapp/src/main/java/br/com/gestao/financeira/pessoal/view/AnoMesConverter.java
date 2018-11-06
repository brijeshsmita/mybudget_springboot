package br.com.gestao.financeira.pessoal.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("anoMesConverter")
public class AnoMesConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		String[] anoMes = value.split("/");
		return new AnoMes(Integer.valueOf(anoMes[1]), Integer.valueOf(anoMes[0]));
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		return ((AnoMes) value).toString();
	}
}