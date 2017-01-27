package io.fabric8.devops.apps.bughunter.events;

import io.fabric8.devops.apps.bughunter.BugHunterVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kameshs
 */
public class ExceptionsEventManager extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionsEventManager.class);

    @Override
    public void start() throws Exception {
        LOGGER.info("Starting ExceptionsEventManager ");
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(BugHunterVerticle.EXCEPTIONS_EVENT_BUS_ADDR, message -> {
            LOGGER.debug("Received message:{}", message);

            if (message != null) {
                JsonObject response = (JsonObject) message.body();
                JsonObject hits = response.getJsonObject("hits");

                LOGGER.info("Hits Total:{}", hits.getInteger("total"));
            }
        });
    }
}
