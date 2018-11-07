package br.com.victorpfranca.mybudget.view;

import java.lang.reflect.InvocationTargetException;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.Logger;

import br.com.victorpfranca.mybudget.infra.log.LogProvider;

@FacesConverter("jpaEntityConverter")
public class JPAEntityConverter implements Converter {

	@Inject
	private EntityManager entityManager;

	private static final Logger LOG = LogProvider.getLogger(JPAEntityConverter.class);

	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		ValueExpression valueExpression = component.getValueExpression("value");
		Class<?> entityClass = valueExpression.getType(context.getELContext());
		if (!entityClass.isAnnotationPresent(Entity.class)) {
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					Messages.msg("not an entity", entityClass.getName()), null));
		}
		Object id = Integer.valueOf(value);

		return entityManager.find(entityClass, id);
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		if (value == null) {
			return null;
		} else if (String.class.equals(value.getClass())) {
			return (String) value;
		} else if (value.getClass().isAnnotationPresent(Entity.class)) {
			try {
				return String.valueOf(value.getClass().getMethod("getId").invoke(value));
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new ConverterException(Messages.msg("error.system"));
			}
		}

		throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
				Messages.msg("not an entity", component.getValueExpression("value").getExpectedType().getName()),
				null));
	}
}