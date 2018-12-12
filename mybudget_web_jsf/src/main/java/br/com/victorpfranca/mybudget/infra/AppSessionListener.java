package br.com.victorpfranca.mybudget.infra;

import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Named
public class AppSessionListener implements HttpSessionListener {

	private static int sessionsNumber;
	
	@Inject
	private App app;

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		log("sessionCreated");
		logHttpSessionData(se.getSession());

		synchronized (this) {
			sessionsNumber++;
		}

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

		synchronized (this) {
			sessionsNumber--;
		}

		log("sessionDestroyedEnd");
	}

	private void log(Object... anyObject) {
	    java.util.logging.Logger logger = LogManager.getLogManager().getLogger(AppSessionListener.class.getName());
	    Level loggingLevel = Level.FINE;
	    if (app.isDevelopmentMode()) {
	        loggingLevel = Level.INFO;
	    }
            logger.log(loggingLevel, Arrays.stream(anyObject).map(Object::toString).collect(Collectors.joining("\t")));
	}

	public static int getSessionsNumber() {
		return sessionsNumber;
	}

}
