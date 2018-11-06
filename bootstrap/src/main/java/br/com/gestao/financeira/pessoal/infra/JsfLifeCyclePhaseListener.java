package br.com.gestao.financeira.pessoal.infra;

import java.text.MessageFormat;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.com.gestao.financeira.pessoal.infra.log.LogProvider;
import org.apache.logging.log4j.Logger;

public class JsfLifeCyclePhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LogProvider.getLogger(PhaseListener.class);

    private volatile long startRequestTime;
    private volatile long startPhaseTime;

    @Override
    public void beforePhase(PhaseEvent event) {
        PhaseId phaseId = event.getPhaseId();
        long currentTimeMillis = System.currentTimeMillis();
        if (PhaseId.RESTORE_VIEW == phaseId) {
            this.startRequestTime = currentTimeMillis;
            LOGGER.info("Start request");
        }
        this.startPhaseTime = currentTimeMillis;
        LOGGER.info(MessageFormat.format("Start ''{0}'' ({1})", phaseId.getName(), phaseId.getOrdinal()));
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        PhaseId phaseId = event.getPhaseId();
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedTime = currentTimeMillis - this.startPhaseTime;
        LOGGER.info(
                MessageFormat.format("End ''{0}'' ({1}) [{2} ms]", phaseId.getName(),
                phaseId.getOrdinal(), elapsedTime));
        if (PhaseId.RENDER_RESPONSE == phaseId) {
            long requestElapsedTime = currentTimeMillis - this.startRequestTime;
            LOGGER.info(MessageFormat.format("End request after [{0} ms]", requestElapsedTime));
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
