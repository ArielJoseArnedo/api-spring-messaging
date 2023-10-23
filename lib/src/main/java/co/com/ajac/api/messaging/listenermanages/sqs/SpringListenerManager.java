package co.com.ajac.api.messaging.listenermanages.sqs;

import co.com.ajac.base.modules.JacksonModulo;
import co.com.ajac.messaging.events.Event;
import co.com.ajac.messaging.events.Header;
import co.com.ajac.messaging.events.Message;
import co.com.ajac.messaging.listeners.AbstractListenerManager;
import co.com.ajac.messaging.listeners.AbstractListenerProvider;
import co.com.ajac.messaging.listeners.Listener;
import co.com.ajac.messaging.listeners.models.ListenerModel;
import co.com.ajac.messaging.listeners.models.ListenerState;
import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringListenerManager extends AbstractListenerManager implements JacksonModulo {

    private final AbstractListenerProvider abstractListenerProvider;

    @Autowired
    public SpringListenerManager(AbstractListenerProvider abstractListenerProvider) {
        this.abstractListenerProvider = abstractListenerProvider;
        initListener();
    }

    private void initListener() {
        abstractListenerProvider.getAllListeners()
          .onSuccess(this::buildInstancesListener);
    }

    @Override
    public Option<Listener> provide(String listenerName) {
        return listeners
          .find(listener -> listener.getName().equals(listenerName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Try<Event> deserialize(JsonNode headerJson, JsonNode listenerBodyJson, String listenerName) {
        return listeners
          .find(listener -> listener.getName().equals(listenerName))
          .toTry()
          .flatMap(listener -> Try.of(() -> {
                final Header header = getMapper().readValue(headerJson.toPrettyString(), Header.class);
                final Message message = (Message) getMapper().readValue(listenerBodyJson.toPrettyString(), listener.getClassMessage());
                return buildEvent(header, message);
            })
          );
    }

    @SuppressWarnings("unchecked")
    private void buildInstancesListener(List<ListenerModel> listenerModels) {
        DefaultListableBeanFactory context = new DefaultListableBeanFactory();
        this.listeners = listenerModels
          .filter(listenerModel -> ListenerState.ACTIVE.equals(listenerModel.getListenerState()))
          .map(listenerModel -> Try.of(() -> Class.forName(listenerModel.getPath())))
          .filter(tryListener -> tryListener
            .onFailure(throwable -> log.warn("Listener not found", throwable))
            .isSuccess()
          ).map(Try::get)
          .map(tclass -> (Listener) context.getBean(tclass));
    }
}
