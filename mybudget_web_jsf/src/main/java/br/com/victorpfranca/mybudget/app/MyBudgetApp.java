package br.com.victorpfranca.mybudget.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;

public class MyBudgetApp {

	public static void main(String[] args) throws Exception {

		Server server = new Server(8080);

		WebAppContext wac = new AliasEnhancedWebAppContext();
		wac.setContextPath("/myBudget");
		wac.setBaseResource(new ResourceCollection(new String[] { "./src/main/webapp", "./target" }));
		wac.setResourceAlias("/WEB-INF/classes/", "/classes/");

		server.setHandler(wac);
		server.setStopAtShutdown(true);
		server.start();
		server.join();
	}

}
