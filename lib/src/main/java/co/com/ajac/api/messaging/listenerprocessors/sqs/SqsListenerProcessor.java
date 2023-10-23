package co.com.ajac.api.messaging.listenerprocessors.sqs;

import co.com.ajac.base.errors.AppError;
import co.com.ajac.messaging.events.Event;
import co.com.ajac.messaging.listeners.AbstractListenerManager;
import co.com.ajac.messaging.listeners.ListenerProcessor;
import co.com.ajac.messaging.publishers.PublisherProvider;
import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

@Component
public class SqsListenerProcessor implements ListenerProcessor<Message> {

    private final AbstractListenerManager listenerManager;
    private final PublisherProvider publisherProvider;

    @Autowired
    public SqsListenerProcessor(AbstractListenerManager listenerManager, PublisherProvider publisherProvider) {
        this.listenerManager = listenerManager;
        this.publisherProvider = publisherProvider;
    }

    @Override
    public AbstractListenerManager getListenerManager() {
        return listenerManager;
    }

    @Override
    public PublisherProvider publisher() {
        return publisherProvider;
    }

    @Override
    public Event makeErrorEvent(AppError appError, Message message) {
        return null;
    }

    @Override
    public JsonNode getMessage(Message message) {
        return null;
    }
}
