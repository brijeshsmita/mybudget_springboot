package br.com.victorpfranca.mybudget.infra;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.com.victorpfranca.mybudget.view.Messages;

@Provider
public class AppExceptionMapper implements ExceptionMapper<Exception> {

	@Context
	private HttpServletRequest request;

	@Override
	public Response toResponse(Exception exception) {
		Response result = null;

		if ((result = unwrap(exception, WebApplicationException.class, this::resolveWebApplicationException)) != null)
			;
		else if ((result = unwrap(exception, ConstraintViolationException.class,
				this::resolveConstraintViolationException)) != null)
			;
		else {
			for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
				if (EJBException.class.isInstance(cause)) {
					result = Response.status(500)
							.entity("Unfortunately, the application cannot process your request at this time. "
									+ cause.getCause().getClass().getSimpleName())
							.type(MediaType.TEXT_PLAIN).build();
					break;
				}
			}
		}

		return result != null ? result
				: Response.status(500)
						.entity("Unfortunately, the application cannot process your request at this time. "
								+ exception.getClass().getSimpleName())
						.type(MediaType.TEXT_PLAIN).build();
	}

	private Response resolveConstraintViolationException(ConstraintViolationException e) {
		String entity = e.getConstraintViolations().stream().map(ConstraintViolation::toString)
				.collect(Collectors.joining(System.lineSeparator()));
		return Response.status(422).type(MediaType.TEXT_PLAIN).entity(entity).build();
	}

	private Object i18n(Response exResponse) {
		if (MediaType.TEXT_PLAIN_TYPE.equals(exResponse.getMediaType())) {
			Locale locale = request.getLocale();
			return Messages.msg((String) exResponse.getEntity(), locale);
		}
		return exResponse.getEntity();
	}

	private Response resolveWebApplicationException(WebApplicationException e) {
		Response exResponse = e.getResponse();
		return Response.status(exResponse.getStatus()).type(exResponse.getMediaType()).entity(i18n(exResponse)).build();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Exception> void unwrap(Throwable t, Class<T> exceptionType, Consumer<T> c) {
		for (Throwable cause = t; cause != null; cause = cause.getCause()) {
			if (exceptionType.isInstance(cause)) {
				c.accept((T) cause);
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Exception> Response unwrap(Throwable t, Class<T> exceptionType, Function<T, Response> c) {
		Response result = null;
		for (Throwable cause = t; cause != null; cause = cause.getCause()) {
			if (exceptionType.isInstance(cause)) {
				result = c.apply((T) cause);
				break;
			}
		}
		return result;
	}

}
