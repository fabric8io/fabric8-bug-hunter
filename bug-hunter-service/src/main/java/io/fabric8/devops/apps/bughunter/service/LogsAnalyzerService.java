package io.fabric8.devops.apps.bughunter.service;

import io.fabric8.devops.apps.bughunter.service.impl.ExceptionsLogsAnalyzer;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author kameshs
 */
@ProxyGen
@VertxGen
public interface LogsAnalyzerService {

    String EXCEPTIONS_EVENT_BUS_ADDR = ExceptionsLogsAnalyzer.class.getName();

    static LogsAnalyzerService createExceptionAnalyzerProxy(Vertx vertx) {
        return new LogsAnalyzerServiceVertxEBProxy(vertx, EXCEPTIONS_EVENT_BUS_ADDR);
    }

    void analyze(JsonArray hits, Handler<AsyncResult<JsonObject>> resultHandler);
}
