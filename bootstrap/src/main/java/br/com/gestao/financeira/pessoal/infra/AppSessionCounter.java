package br.com.gestao.financeira.pessoal.infra;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.Logger;

import br.com.gestao.financeira.pessoal.infra.log.LogProvider;

public class AppSessionCounter implements HttpSessionListener {

	private static final Logger LOGGER = LogProvider.getLogger(AppSessionCounter.class);

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		log("sessionCreated");
		logHttpSessionData(se.getSession());
		log("sessionCreatedEnd");
	}

	private void logHttpSessionData(HttpSession session) {
		log("HttpSession.getMaxInactiveInterval", session.getMaxInactiveInterval());
		log("HttpSession.getCreationTime", session.getCreationTime());
		log("HttpSession.getId", session.getId());
		log("HttpSession.getLastAccessedTime", session.getLastAccessedTime());
		log("HttpSession.getAttributes", Collections.list(session.getAttributeNames()).stream()
				.map(name -> name + "=[" + session.getAttribute(name) + "]").collect(Collectors.joining(",")));
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log("sessionDestroyed");
		logHttpSessionData(se.getSession());
		log("sessionDestroyedEnd");
	}

	private static void log(Object... anyObject) {
		LOGGER.info(Arrays.stream(anyObject).map(Object::toString).collect(Collectors.joining("\t")));
	}

}
