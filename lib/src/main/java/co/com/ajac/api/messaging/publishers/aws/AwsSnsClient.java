package co.com.ajac.api.messaging.publishers.aws;

import co.com.ajac.base.errors.AppError;
import co.com.ajac.base.modules.JacksonModulo;
import co.com.ajac.concurrency.FutureEither;
import co.com.ajac.messaging.events.Event;
import co.com.ajac.messaging.publishers.PublishUtil;
import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;


@Slf4j
@Component
public class AwsSnsClient implements PublishUtil {

    private final SnsAsyncClient snsAsyncClient;

    public AwsSnsClient() {
        this.snsAsyncClient = createSnsAsyncClientDefault();
    }

    public AwsSnsClient(SnsAsyncClient snsAsyncClient) {
        this.snsAsyncClient = snsAsyncClient;
    }

    public Future<List<PublishResponse>> publish(List<Event> events) {
        return Future.traverse(events, event -> publish(transformEvent(event), getTopic(event)))
          .map(Seq::toList)
          .map(this::logReponse);
    }

    private Future<PublishResponse> publish(JsonNode message, String topic) {
        return Future.fromCompletableFuture(
          snsAsyncClient
            .publish(createRequest(message, topic))
        );
    }

    private SnsAsyncClient createSnsAsyncClientDefault() {
       return SnsAsyncClient.create();
    }

    private PublishRequest createRequest(JsonNode message, String topic) {
        return PublishRequest.builder()
          .message(message.asText())
          .topicArn(topic)
          .build();
    }


    private List<PublishResponse> logReponse(List<PublishResponse> responses) {
        responses
          .forEach(publishResponse ->
              log.info("Publish: {}", publishResponse)
          );
        return responses;
    }
}
