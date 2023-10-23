package co.com.ajac.api.messaging.listenerprocessors.sqs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Component
public class SqsExecutor {

    private final SuscriptionSQSProcessor suscriptionSQSProcessor;
    private final PropertiesConfigurationSqs propertiesConfigurationSqs;

    @Autowired
    public SqsExecutor(SuscriptionSQSProcessor suscriptionSQSProcessor, PropertiesConfigurationSqs propertiesConfigurationSqs) {
        this.suscriptionSQSProcessor = suscriptionSQSProcessor;
        this.propertiesConfigurationSqs = propertiesConfigurationSqs;
    }

    public void start() {
        new Timer()
          .scheduleAtFixedRate(suscriptionSQSProcessor, 2L, propertiesConfigurationSqs.getInverval());
    }
}
