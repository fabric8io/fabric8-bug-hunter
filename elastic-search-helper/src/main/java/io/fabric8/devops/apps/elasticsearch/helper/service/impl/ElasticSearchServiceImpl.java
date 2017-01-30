package io.fabric8.devops.apps.elasticsearch.helper.service.impl;

import io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchOptions;
import io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.configuration.ConfigurationRetriever;
import io.vertx.ext.configuration.ConfigurationRetrieverOptions;
import io.vertx.ext.configuration.ConfigurationStoreOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.serviceproxy.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * @author kameshs
 */
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);

    private static final int CMLOADING = 1000;
    private static final int ESQ1001 = 1001;
    private static final int ESQ1002 = 1002;

    private final Vertx vertx;

    private ElasticSearchOptions elasticSearchOptions;

    private ConfigurationStoreOptions cfgStoreOpts;

    private Optional<ConfigurationRetrieverOptions> cfgRetrieverOptions = Optional.empty();

    public ElasticSearchServiceImpl(Vertx vertx) {
        this(vertx, new ElasticSearchOptions());
    }

    public ElasticSearchServiceImpl(Vertx vertx, ElasticSearchOptions elasticSearchOptions) {

        this.vertx = vertx;

        this.elasticSearchOptions = elasticSearchOptions;

        EventBus eventBus = vertx.eventBus();

        //Search Address Bus
        eventBus.<String>consumer(ES_SEARCH_BUS_ADDRESS, (message) -> search(message.body(),
            searchResult -> {

                if (searchResult.succeeded()) {
                    message.reply(searchResult.result());
                } else {
                    LOGGER.error("Error searching", searchResult.cause());
                    //TODO
                }

            }));

        if (elasticSearchOptions.getConfigMap() != null) {
            cfgStoreOpts = new ConfigurationStoreOptions()
                .setType("configmap")
                .setConfig(new JsonObject()
                    .put("namespace", elasticSearchOptions.getKubernetesNamespace() == null
                        ? "default" : elasticSearchOptions.getKubernetesNamespace())
                    .put("name", elasticSearchOptions.getConfigMap()));
            ConfigurationRetrieverOptions retrieverOptions = new ConfigurationRetrieverOptions()
                .addStore(cfgStoreOpts)
                .setScanPeriod(elasticSearchOptions.getConfigMapScanPeriod());
            cfgRetrieverOptions = Optional.of(retrieverOptions);
        }

    }

    //TODO add some error context info
    @Override
    public void search(String searchQuery, Handler<AsyncResult<JsonObject>> resultHandler) {

        boolean isSsl = elasticSearchOptions.isSsl();
        String esServiceName = elasticSearchOptions.getHost();
        int esServicePort = elasticSearchOptions.getPort();

        List<String> indexes = elasticSearchOptions.getIndexes();

        LOGGER.info("Searching with q={} on indexes {}", searchQuery, indexes);

        if (cfgRetrieverOptions.isPresent()) {
            LOGGER.info("Using ConfigMap for Elastic Search Options");
            ConfigurationRetrieverOptions retrieverOptions = cfgRetrieverOptions.get();
            ConfigurationRetriever configMapRetriever = ConfigurationRetriever.create(vertx, retrieverOptions);
            configMapRetriever.getConfiguration(configMap -> {
                if (configMap.succeeded()) {
                    HttpClientOptions httpClientOptions = createHttpClientOptions(isSsl, esServiceName, esServicePort);
                    query(searchQuery, indexes, resultHandler, httpClientOptions);
                } else {
                    LOGGER.error("Error loading Config Map:", configMap.cause());
                    resultHandler.handle(ServiceException.fail(CMLOADING, configMap.cause().getMessage()));
                }
            });
        } else {
            HttpClientOptions httpClientOptions = createHttpClientOptions(isSsl, esServiceName, esServicePort);
            query(searchQuery, indexes, resultHandler, httpClientOptions);
        }
    }

    /**
     * @param isSsl
     * @param esServiceName
     * @param esServicePort
     * @return
     */
    protected HttpClientOptions createHttpClientOptions(boolean isSsl, String esServiceName, int esServicePort) {
        HttpClientOptions httpClientOptions = new HttpClientOptions();
        httpClientOptions.setSsl(isSsl);
        httpClientOptions.setDefaultHost(esServiceName);
        httpClientOptions.setDefaultPort(esServicePort);
        return httpClientOptions;
    }

    /**
     * @param httpClientOptions
     * @param queryRequestHandler
     * @param searchQuery
     */
    protected void elasticSearchEndpoint(HttpClientOptions httpClientOptions,
                                         Handler<AsyncResult<HttpRequest<Buffer>>> queryRequestHandler,
                                         String uri,
                                         String searchQuery) {
        HttpClient httpClient = vertx.createHttpClient(httpClientOptions);
        WebClient webClient = WebClient.wrap(httpClient);
        HttpRequest<Buffer> httpRequest = webClient.get(uri).addQueryParam("q", searchQuery);
        //TODO add request in cache and reuse it
        queryRequestHandler.handle(Future.succeededFuture(httpRequest));
    }


    /**
     * @param searchQuery
     * @param indexes
     * @param resultHandler
     * @param httpClientOptions
     */
    private void query(String searchQuery, List<String> indexes, Handler<AsyncResult<JsonObject>> resultHandler,
                       HttpClientOptions httpClientOptions) {

        final String uri = StringUtils.join(indexes, ",") + "/_search";

        LOGGER.info("Search URI:{}", uri);

        elasticSearchEndpoint(httpClientOptions, httpRequestAsyncResult -> {

            if (httpRequestAsyncResult.succeeded()) {
                HttpRequest<Buffer> bufferHttpRequest = httpRequestAsyncResult.result();
                bufferHttpRequest.send(responseAsyncResult -> {
                    if (responseAsyncResult.succeeded()) {
                        JsonObject searchResponse = responseAsyncResult.result().bodyAsJsonObject();
                        resultHandler.handle(Future.succeededFuture(searchResponse));
                    } else {
                        LOGGER.error("Error while searching ", responseAsyncResult.cause());
                        resultHandler.handle(ServiceException.fail(ESQ1002, responseAsyncResult.cause().getMessage()));
                    }
                });
            } else {
                LOGGER.error("Error while building request ", httpRequestAsyncResult.cause());
                resultHandler.handle(ServiceException.fail(ESQ1001, httpRequestAsyncResult.cause().getMessage()));
            }
        }, uri, searchQuery);
    }
}
