package co.com.ajac.api.messaging.publishers.aws;

import co.com.ajac.messaging.events.Event;
import co.com.ajac.messaging.publishers.PublisherProvider;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("awsSnsPublisher")
public class AwsSnsPublisher implements PublisherProvider {

    private final AwsSnsClient awsSnsClient;

    @Autowired
    public AwsSnsPublisher(AwsSnsClient awsSnsClient) {
        this.awsSnsClient = awsSnsClient;
    }

    @Override
    public Future<List<Event>> publish(List<Event> list) {
        return awsSnsClient.publish(list)
          .map(responses -> list);
    }
}
