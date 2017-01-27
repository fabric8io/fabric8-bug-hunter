package io.fabric8.devops.apps.bughunter.events;

import io.fabric8.devops.apps.bughunter.BugHunterVerticle;
import io.fabric8.devops.apps.bughunter.analyzers.BugHitsAnalyzer;
import io.fabric8.devops.apps.bughunter.codec.BugInfoCodec;
import io.fabric8.devops.apps.bughunter.model.BugInfo;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.rxjava.core.eventbus.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Single;

/**
 * @author kameshs
 */
public class ExceptionsEventManager extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionsEventManager.class);
    public static final String BUGS_HIT_ANALZYER = "bugs-hit-analzyer";

    @Override
    public void start() throws Exception {
        LOGGER.info("Starting ExceptionsEventManager ");
        EventBus eventBus = vertx.eventBus();
        eventBus.getDelegate().registerDefaultCodec(BugInfo.class, new BugInfoCodec());

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setWorker(true);

        vertx.deployVerticle(BugHitsAnalyzer.class.getName(), deploymentOptions);

        MessageConsumer<JsonObject> exceptionsHitsConsumer = eventBus
            .consumer(BugHunterVerticle.EXCEPTIONS_EVENT_BUS_ADDR);

        Observable<Message<JsonObject>> hitsObserverable = exceptionsHitsConsumer.toObservable();

        hitsObserverable
            .subscribe(hitMessage -> {
                JsonObject hits = hitMessage.body().getJsonObject("hits");
                if (hits.containsKey("hits")) {
                    JsonArray hitOfHit = hits.getJsonArray("hits");
                    LOGGER.info("Sending {} Bug Hits for Analysis", hitOfHit.size());

                    Single<Message<String>> single = eventBus.rxSend(BUGS_HIT_ANALZYER, hitOfHit);

                    single.subscribe(response -> {
                        String bugInfosJson = response.body();
                        LOGGER.trace("Bug Analyis{}", bugInfosJson);
                        hitMessage.reply(bugInfosJson);
                    }, error -> LOGGER.error("Error while analyzing bug hits", error));

                } else {
                    LOGGER.warn("No hits key found in JSON");
                }
            }, error -> LOGGER.error("Error processing hits ", error));
    }
}
