package io.fabric8.devops.apps.bughunter.analyzers;

import io.fabric8.devops.apps.bughunter.BugHunterVerticle;
import io.fabric8.devops.apps.bughunter.analyzers.BugHitsAnalyzer;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author kameshs
 */
public class ExceptionsEventAnalyzer extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionsEventAnalyzer.class);

    @Override
    public void start() throws Exception {
        LOGGER.info("Starting ExceptionsEventManager ");
        EventBus eventBus = vertx.eventBus();

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setWorker(true);

        MessageConsumer<JsonObject> exceptionsHitsConsumer = eventBus
            .consumer(BugHunterVerticle.EXCEPTIONS_EVENT_BUS_ADDR);

        Observable<Message<JsonObject>> hitsObserverable = exceptionsHitsConsumer.toObservable();

        hitsObserverable
            .subscribe(hitMessage -> {
                JsonObject hits = hitMessage.body().getJsonObject("hits");
                if (hits.containsKey("hits")) {
                    JsonArray hitOfHit = hits.getJsonArray("hits");
                    Observable<Object> hitsOfHitObservable = Observable.from(hitOfHit);

                    List<BugInfo> bugInfos = new ArrayList<>();

                    hitsOfHitObservable.subscribe(o -> {
                            Optional<BugInfo> optBugInfo = BugHitsAnalyzer.process((JsonObject) o);
                            if (optBugInfo.isPresent()) {
                                BugInfo bugInfo = optBugInfo.get();
                                bugInfos.add(bugInfo);
                            }
                        }
                        ,
                        error -> LOGGER.error("Error while analyzing hits", error)
                        , () -> {
                            LOGGER.info("Bugs Analyzed successfully...");
                        }
                    );

                } else {
                    LOGGER.warn("No hits key found in JSON");
                }
            }, error -> LOGGER.error("Error processing hits ", error));
    }
}
