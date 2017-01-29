package io.fabric8.devops.apps.elasticsearch.helper.service;

import io.fabric8.devops.apps.elasticsearch.helper.service.impl.ElasticSearchServiceImpl;
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

    static ElasticSearchService create(Vertx vertx) {
        return new ElasticSearchServiceImpl(vertx);
    }

    static ElasticSearchService createWithOptions(Vertx vertx, ElasticSearchOptions elasticSearchOptions) {
        return new ElasticSearchServiceImpl(vertx, elasticSearchOptions);
    }

    /**
     * @param searchQuery
     * @param resultHandler
     */
    void search(String searchQuery, Handler<AsyncResult<JsonObject>> resultHandler);
}
