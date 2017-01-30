package io.fabric8.devops.apps.bughunter.service.impl;

import io.fabric8.devops.apps.bughunter.model.BugInfo;
import io.fabric8.devops.apps.bughunter.service.LogsAnalyzerService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author kameshs
 */
public class ExceptionsLogsAnalyzer implements LogsAnalyzerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionsLogsAnalyzer.class);

    private final EventBus eventBus;

    private MessageConsumer<JsonArray> exceptionsHitsConsumer;

    public ExceptionsLogsAnalyzer(Vertx vertx) {
        this.eventBus = vertx.eventBus();
        exceptionsHitsConsumer = eventBus.consumer(EXCEPTIONS_EVENT_BUS_ADDR);
    }

    @Override
    public void analyze(Handler<AsyncResult<JsonObject>> resultHandler) {

        LOGGER.info("ExceptionsLogsAnalyzer is consuming messages from '{}' ", exceptionsHitsConsumer.address());

        exceptionsHitsConsumer.exceptionHandler(throwable ->
            LOGGER.error("Error ExceptionsLogsAnalyzer:", throwable));

        exceptionsHitsConsumer.handler(hitMessage -> {

            LOGGER.info(">>");

            JsonArray hitOfHit = hitMessage.body();

            final JsonObject jBugInfo = new JsonObject();

            final JsonArray jBugInfos = new JsonArray();

            final List<Optional<BugInfo>> bugInfos = hitOfHit.stream()
                .map(JsonObject.class::cast)
                .map(BugHitsAnalyzer::process)
                .collect(Collectors.toList());

            LOGGER.info("Collected {} bug hits", bugInfos.size());

            bugInfos.stream()
                .filter(o -> o.isPresent())
                .map(o -> o.get().toJson())
                .forEach(o -> jBugInfos.add(o));

            jBugInfo.put("bugs", jBugInfos);

            resultHandler.handle(Future.succeededFuture(jBugInfo));

        });


        //TODO define error handler???
    }
}
