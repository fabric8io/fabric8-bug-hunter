package io.fabric8.devops.apps.bughunter.service;

import io.fabric8.devops.apps.bughunter.service.impl.ExceptionsLogsAnalyzer;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author kameshs
 */
@ProxyGen
@VertxGen
public interface LogsAnalyzerService {

    String EXCEPTIONS_EVENT_BUS_ADDR = ExceptionsLogsAnalyzer.class.getName();

    static LogsAnalyzerService createExceptionAnalyzer(Vertx vertx) {
        return new ExceptionsLogsAnalyzer(vertx);
    }

    void analyze(Handler<AsyncResult<JsonObject>> resultHandler);
}
