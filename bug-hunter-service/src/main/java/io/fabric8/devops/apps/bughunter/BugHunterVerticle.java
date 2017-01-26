package io.fabric8.devops.apps.bughunter;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.configuration.ConfigurationRetriever;
import io.vertx.ext.configuration.ConfigurationRetrieverOptions;
import io.vertx.ext.configuration.ConfigurationStoreOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.kubernetes.KubernetesServiceImporter;
import io.vertx.servicediscovery.types.HttpEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Single;

/**
 * @author kameshs
 */
public class BugHunterVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(BugHunterVerticle.class);

    //FIXME - need to have some query and its factories for standard log analysis
    final String queryString = "kubernetes.namespace_name: \"default\" " +
        "AND kubernetes.labels.group: \"io.fabric8.devops.apps\"" +
        "AND log: \"SimpleCalculatorApplication\" ";

    private ServiceDiscovery serviceDiscovery;

    @Override
    public void start() throws Exception {

        final ConfigurationStoreOptions cfgStoreOpts = new ConfigurationStoreOptions()
            .setType("configmap")
            .setConfig(new JsonObject()
                .put("namespace", "default")
                .put("name", "fabric8-bug-spy"));

        final ConfigurationRetrieverOptions cfgRetrieverOptions = new ConfigurationRetrieverOptions();
        cfgRetrieverOptions
            .addStore(cfgStoreOpts)
            .setScanPeriod(30000);

        ConfigurationRetriever configMapRetriever = ConfigurationRetriever.create(vertx.getDelegate(), cfgRetrieverOptions);

        configMapRetriever.getConfiguration(configMap -> {
            if (configMap.succeeded()) {
                JsonObject configData = configMap.result();
                String esServiceName = configData.getString("elastic-search-service-name");
                int esServicePort = configData.getInteger("elastic-search-service-port");
                LOGGER.info("Elastic Search Service Name: {} and on port {}", esServiceName, esServicePort);

                HttpClientOptions httpClientOptions = new HttpClientOptions();
                httpClientOptions.setDefaultHost(esServiceName);
                httpClientOptions.setDefaultPort(esServicePort);
                elasticSearchEndpoint(httpClientOptions, result -> {
                    if (result.succeeded()) {
                        Single<HttpResponse<Buffer>> queryResponse = result.result();
                        queryResponse.subscribe(res -> {
                            JsonObject response = res.bodyAsJsonObject();
                            if (res.statusCode() == 200) {
                                LOGGER.info("Total Hits {}", response.getJsonObject("hits").getInteger("total"));
                            } else {
                                LOGGER.warn("Invalid response {}", res.statusMessage());
                            }
                        }, error -> LOGGER.error("Error while searching ", error));
                    } else {
                        LOGGER.error("Error while building the client ", result.cause());
                    }
                });

               /*
                TODO: this works only when vert.x kubernetes discovery works for unknown type
                JsonObject serviceFilter = buildServiceFilter(esServiceName);
                discoverServices(serviceFilter, result -> {
                    if (result.succeeded()) {
                        Single<HttpResponse<Buffer>> queryResponse = result.result();
                        queryResponse.subscribe(res -> {
                            JsonObject response = res.bodyAsJsonObject();
                            if (res.statusCode() == 200) {
                                LOGGER.info("Total Hits {}", response.getJsonObject("hits").getInteger("total"));
                            } else {
                                LOGGER.warn("Invalid response {}", res.statusMessage());
                            }
                        }, error -> LOGGER.error("Error while searching ", error));
                    } else {
                        LOGGER.error("Error while building the client ", result.cause());
                    }
                });*/


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
                                      Handler<AsyncResult<Single<HttpResponse<Buffer>>>> queryResponseHandler) {
        io.vertx.core.http.HttpClient httpClient = getVertx().createHttpClient(httpClientOptions);
        HttpClient rxHttpClient = HttpClient.newInstance(httpClient);
        WebClient webClient = WebClient.wrap(rxHttpClient);
        queryResponseHandler.handle(Future.succeededFuture(webClient.get("logstash-*/_search")
            .addQueryParam("q", queryString)
            .rxSend()));
    }


    public void discoverServices(JsonObject serviceFilter,
                                 Handler<AsyncResult<Single<HttpResponse<Buffer>>>> queryResponseHandler) {
        serviceDiscovery = ServiceDiscovery.create(vertx.getDelegate());
        KubernetesServiceImporter kubernetesServiceImporter = new KubernetesServiceImporter();

        serviceDiscovery.registerServiceImporter(kubernetesServiceImporter, new JsonObject(),
            result -> {
                if (result.succeeded()) {
                    LOGGER.info("Building HTTPClient with filter {}", serviceFilter);
                    HttpEndpoint.getClient(serviceDiscovery, serviceFilter, httpClientAsyncResult -> {
                        if (httpClientAsyncResult.succeeded()) {
                            HttpClient rxHttpClient = HttpClient.newInstance(httpClientAsyncResult.result());
                            WebClient webClient = WebClient.wrap(rxHttpClient);
                            queryResponseHandler.handle(Future.succeededFuture(webClient.get("logstash-*/_search")
                                .addQueryParam("q", queryString)
                                .rxSend()));
                        } else {
                            LOGGER.error("Error building HttpClient", httpClientAsyncResult.cause());
                            queryResponseHandler.handle(Future.failedFuture(httpClientAsyncResult.cause()));
                        }
                    });
                } else {
                    LOGGER.error("Error while discovering service", result.cause());
                    queryResponseHandler.handle(Future.failedFuture(result.cause()));
                }
            });
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
