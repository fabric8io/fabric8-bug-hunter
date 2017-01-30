package io.fabric8.devops.apps.elasticsearch.helper.service;

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
public interface ElasticSearchService {


    String ES_SEARCH_BUS_ADDRESS = ElasticSearchService.class.getName();

    static ElasticSearchService createProxy(Vertx vertx) {
        return new ElasticSearchServiceVertxEBProxy(vertx, ES_SEARCH_BUS_ADDRESS);
    }

    /**
     * @param searchQuery
     * @param resultHandler
     */
    void search(String searchQuery, Handler<AsyncResult<JsonObject>> resultHandler);
}
