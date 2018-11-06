package br.com.gestao.financeira.pessoal.infra;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import javax.faces.application.FacesMessage;
import javax.faces.convert.ConverterException;
import javax.persistence.Id;

import br.com.gestao.financeira.pessoal.infra.log.LogProvider;
import org.apache.logging.log4j.Logger;
import br.com.gestao.financeira.pessoal.view.Messages;

public class EntityUtil {
    private static final Logger LOG = LogProvider.getLogger(EntityUtil.class);

    private EntityUtil() {
    }

    public static Serializable convertStringValueToIdType(String value, Class<?> entityClass) {
        Class<?> idType = getIdType(entityClass);
        Serializable id;
        if (String.class.equals(idType)) {
            id = value;
        } else if (Long.class.equals(idType)) {
            id = Long.parseLong(value, 10);
        } else if (Integer.class.equals(idType)) {
            id = Integer.parseInt(value, 10);
        } else if (Short.class.equals(idType)) {
            id = Short.parseShort(value, 10);
        } else {
            String msg = Messages.msg("Could not convert from Id type [ %s ] to entity [ %s ]", idType.getName(),
                    entityClass.getName());
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
        }
        return id;
    }

    private static PropertyDescriptor getIdPropertyDescriptor(Class<?> entityType) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(entityType, Object.class);
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                try {
                    if (propertyDescriptor.getReadMethod().isAnnotationPresent(Id.class)
                            || propertyDescriptor.getWriteMethod().isAnnotationPresent(Id.class) || entityType
                                    .getDeclaredField(propertyDescriptor.getName()).isAnnotationPresent(Id.class)) {
                        return propertyDescriptor;
                    }
                } catch (NoSuchFieldException | SecurityException e) {
                    LOG.error(e);
                }
            }
        } catch (IntrospectionException e) {
            LOG.error(e);
        }
        return null;
    }

    public static Class<?> getIdType(Class<?> entityType) {
        PropertyDescriptor propertyDescriptor = getIdPropertyDescriptor(entityType);
        if (propertyDescriptor != null) {
            try {
                return entityType.getDeclaredField(propertyDescriptor.getName()).getType();
            } catch (NoSuchFieldException | SecurityException e) {
                LOG.error(e);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getId(Object entity) {
        PropertyDescriptor propertyDescriptor = getIdPropertyDescriptor(entity.getClass());
        if (propertyDescriptor != null) {
            try {
                return (T) propertyDescriptor.getReadMethod().invoke(entity);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                LOG.error(e);
            }
        }
        return null;
    }

    public static <T> T newInstance(Class<T> entityType) {
        try {
            return entityType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T cloneObject(T entity) {
        if (entity == null)
            return null;
        Class<T> entityClass = (Class<T>) entity.getClass();
        T newInstance = newInstance(entityClass);
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(entityClass, Object.class);
            for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
                property.getWriteMethod().invoke(newInstance, property.getReadMethod().invoke(entity));
            }
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {

        }
        return newInstance;
    }

}
