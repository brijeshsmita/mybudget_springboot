package br.com.victorpfranca.mybudget.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter("anoMesConverter")
public class MonthYearConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		String[] anoMes = value.split("/");
		return new MonthYear(Integer.valueOf(anoMes[1]), Integer.valueOf(anoMes[0]));
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		return ((MonthYear) value).toString();
	}
}