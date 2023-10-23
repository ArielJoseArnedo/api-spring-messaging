package co.com.ajac.api.messaging.listenerprovides.aws.dynamo;

import co.com.ajac.messaging.listeners.AbstractListenerProvider;
import co.com.ajac.messaging.listeners.models.ListenerModel;
import co.com.ajac.messaging.listeners.models.ListenerState;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.stream.Stream;

@Slf4j
@Component("dynamoListenerProvider")
public class DynamoListenerProvider implements AbstractListenerProvider {

    private final AwsDynamoClient awsDynamoClient;

    @Autowired
    public DynamoListenerProvider(AwsDynamoClient awsDynamoClient) {
        this.awsDynamoClient = awsDynamoClient;
    }

    @Override
    public Future<List<ListenerModel>> getAllListeners() {
        return Future.of(() -> queryAllListener()
          .map(listenerModelSchema -> ListenerModel.builder()
            .name(listenerModelSchema.getListener())
            .path(listenerModelSchema.getPathClass())
            .listenerState(ListenerState.valueOf(listenerModelSchema.getState()))
            .build()
          )
          .toList()
        ).map(List::ofAll);
    }

    @Override
    public Future<List<ListenerModel>> getListeners(ListenerState listenerState) {
        return null;
    }

    @Cacheable
    public Stream<ListenerModelSchema> queryAllListener() {
        return awsDynamoClient.getDynamoDbEnhancedClient()
          .table("Listener", TableSchema.fromBean(ListenerModelSchema.class))
          .query(QueryEnhancedRequest.builder().build())
          .items()
          .stream();
    }
}
