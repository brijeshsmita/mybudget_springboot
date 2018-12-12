package br.com.victorpfranca.mybudget.infra;

import java.util.Set;

import javax.faces.application.ProjectStage;
import javax.faces.application.StateManager;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ServletContextInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        String property = System.getProperty("PRODUCTION");
        ProjectStage projectStage = ProjectStage.Development;
        if ("true".equals(property)) {
            projectStage = ProjectStage.Production;
        }
        ctx.addListener(AppSessionListener.class);
        ctx.setInitParameter(ProjectStage.PROJECT_STAGE_PARAM_NAME, projectStage.name());
        ctx.setInitParameter(StateManager.STATE_SAVING_METHOD_PARAM_NAME, StateManager.STATE_SAVING_METHOD_SERVER);
    }

}
