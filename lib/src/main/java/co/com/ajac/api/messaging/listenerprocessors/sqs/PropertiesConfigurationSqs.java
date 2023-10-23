package co.com.ajac.api.messaging.listenerprocessors.sqs;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("file:config/aws.yml")
public class PropertiesConfigurationSqs {

    @Value("${sqs.queue}")
    private String queue;

    @Value("${sqs.interval: 1000}")
    private Long inverval;
}
