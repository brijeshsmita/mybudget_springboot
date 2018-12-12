package br.com.victorpfranca.mybudget.infra;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.FactoryFinder;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import br.com.victorpfranca.mybudget.infra.log.LogProvider;

@ManagedBean(eager = true)
@ApplicationScoped
public class JsfContextInitializer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private App app;

    @PostConstruct
    private void initialize() {
        if (!app.isProductionMode()) {
            LifecycleFactory factory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            Lifecycle lifecycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
            lifecycle.addPhaseListener(new JsfLifeCyclePhaseListener());
            Logger logger = LogProvider.getLogger(FactoryFinder.LIFECYCLE_FACTORY);
            for (PhaseListener pl : lifecycle.getPhaseListeners()) {
                logger.info("PhaseListener: " + pl.getClass().getName() + " verified loaded.");
            }
        }
    }

}
