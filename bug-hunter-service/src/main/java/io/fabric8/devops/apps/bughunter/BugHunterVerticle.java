package io.fabric8.devops.apps.bughunter;

import io.fabric8.devops.apps.bughunter.analyzers.ExceptionsEventAnalyzer;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.configuration.ConfigurationRetriever;
import io.vertx.ext.configuration.ConfigurationRetrieverOptions;
import io.vertx.ext.configuration.ConfigurationStoreOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Single;

import java.time.Duration;

/**
 * @author kameshs
 */
public class BugHunterVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(BugHunterVerticle.class);
    public static final String EXCEPTIONS_EVENT_BUS_ADDR = "exceptions-events";

    @Override
    public void start() throws Exception {

        final EventBus eventBus = vertx.eventBus();

        final ConfigurationStoreOptions cfgStoreOpts = new ConfigurationStoreOptions()
            .setType("configmap")
            .setConfig(new JsonObject()
                .put("namespace", "default")
                .put("name", "bug-hunter"));

        final ConfigurationRetrieverOptions cfgRetrieverOptions = new ConfigurationRetrieverOptions();
        cfgRetrieverOptions
            .addStore(cfgStoreOpts)
            .setScanPeriod(30000);

        ConfigurationRetriever configMapRetriever = ConfigurationRetriever.create(getVertx(), cfgRetrieverOptions);

        configMapRetriever.getConfiguration(configMap -> {
            if (configMap.succeeded()) {

                JsonObject configData = configMap.result();

                String esServiceName = configData.getString("elastic-search-service-name");

                int esServicePort = configData.getInteger("elastic-search-service-port");

                int huntingIntervalInSeconds = configData.getInteger("hunting-interval");

                String searchQuery = configData.getString("hunting-search-query");

                LOGGER.trace("Elastic Search Service Name: {} and on port {} with schedule " +
                        "{} seconds with Query:{}", esServiceName,
                    esServicePort, huntingIntervalInSeconds, searchQuery);

                //TODO is this right to make this worker ???
                final DeploymentOptions exceptionsManagerOpts = new DeploymentOptions();
                exceptionsManagerOpts.setWorker(true);
                vertx.deployVerticle(ExceptionsEventAnalyzer.class.getName(), exceptionsManagerOpts);

                HttpClientOptions httpClientOptions = new HttpClientOptions();
                httpClientOptions.setDefaultHost(esServiceName);
                httpClientOptions.setDefaultPort(esServicePort);

                //FIXME - can I not make only Rx subscribe scheduled ??
                vertx.setPeriodic(Duration.ofSeconds(huntingIntervalInSeconds).toMillis(),
                    aLong -> elasticSearchEndpoint(httpClientOptions, result -> {
                        if (result.succeeded()) {
                            Single<HttpResponse<Buffer>> queryResponse = result.result();
                            //FIXME - wonderful if I can make this scheduled
                            queryResponse.subscribe(res -> {
                                JsonObject response = res.bodyAsJsonObject();
                                if (res.statusCode() == 200) {
                                    eventBus.send(EXCEPTIONS_EVENT_BUS_ADDR, response);
                                } else {
                                    LOGGER.warn("Invalid response {}", res.statusMessage());
                                }
                            }, error -> LOGGER.error("Error while searching ", error));
                        } else {
                            LOGGER.error("Error while building the client ", result.cause());
                        }
                    }, searchQuery));
            } else {
                LOGGER.error("Unable to find config map ", configMap.cause());
            }
        });


    }


    /**
     * @param httpClientOptions
     * @param queryResponseHandler
     */
    public void elasticSearchEndpoint(HttpClientOptions httpClientOptions,
                                      Handler<AsyncResult<Single<HttpResponse<Buffer>>>> queryResponseHandler,
                                      String searchQuery) {
        io.vertx.core.http.HttpClient httpClient = getVertx().createHttpClient(httpClientOptions);
        HttpClient rxHttpClient = HttpClient.newInstance(httpClient);
        WebClient webClient = WebClient.wrap(rxHttpClient);
        queryResponseHandler.handle(Future.succeededFuture(webClient.get("logstash-*/_search")
            .addQueryParam("q", searchQuery)
            .rxSend()));
    }

    /**
     * Builds the Kubernetes service filter based on &quotname&quot;
     *
     * @param serviceName - the name of the service that will be added to the filter
     * @return - {@link JsonObject}
     */
    protected JsonObject buildServiceFilter(String serviceName) {
        JsonObject serviceFilter = new JsonObject();
        serviceFilter.put("name", serviceName);
        return serviceFilter;
    }
}
