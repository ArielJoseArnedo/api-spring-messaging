package co.com.ajac.api.messaging.listenerprocessors.sqs;

import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

@Component
public class SuscriptionSQSProcessor extends TimerTask {

    private final SqsAsyncClient sqsAsyncClient;
    private final SqsListenerProcessor sqsListenerProcessor;

    private final PropertiesConfigurationSqs propertiesConfigurationSqs;

    @Autowired
    public SuscriptionSQSProcessor(SqsListenerProcessor sqsListenerProcessor, PropertiesConfigurationSqs propertiesConfigurationSqs) {
        this.propertiesConfigurationSqs = propertiesConfigurationSqs;
        this.sqsAsyncClient = SqsAsyncClient.create();
        this.sqsListenerProcessor = sqsListenerProcessor;
    }

    @Override
    public void run() {
        processingMessage()
          .thenAccept(sqsListenerProcessor);
    }

    private CompletableFuture<Option<Message>> processingMessage() {
         return sqsAsyncClient.receiveMessage(
            ReceiveMessageRequest.builder()
              .queueUrl(propertiesConfigurationSqs.getQueue())
              .maxNumberOfMessages(1)
              .build())
          .thenApply(receiveMessageResponse ->
            receiveMessageResponse.hasMessages()
              ? Option.of(receiveMessageResponse.messages().get(0))
              : Option.none()
          );
    }
}
