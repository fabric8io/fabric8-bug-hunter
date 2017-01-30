package io.fabric8.devops.apps.bughunter;

import io.fabric8.devops.apps.bughunter.service.LogsAnalyzerService;
import io.fabric8.devops.apps.bughunter.service.impl.ExceptionsLogsAnalyzer;
import io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchOptions;
import io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchService;
import io.fabric8.devops.apps.elasticsearch.helper.service.impl.ElasticSearchServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.configuration.ConfigurationRetriever;
import io.vertx.ext.configuration.ConfigurationRetrieverOptions;
import io.vertx.ext.configuration.ConfigurationStoreOptions;
import io.vertx.serviceproxy.ProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author kameshs
 */
public class BugHunterMainVerticle extends AbstractVerticle {


    private static final Logger LOGGER = LoggerFactory.getLogger(BugHunterMainVerticle.class);

    @Override
    public void start() throws Exception {

        //Check if its passed via config to the Verticle
        String k8sNamespace = config().getString("namespace", "default");
        String configMapName = config().getString("namespace", "bug-hunter");
        int configMapScanInterval = config().getInteger("configMapScanInterval", 30000);

        //Register LogAnalyzer Services ...
        LogsAnalyzerService exceptionAnalyzerService = new ExceptionsLogsAnalyzer(vertx);
        ProxyHelper.registerService(LogsAnalyzerService.class, vertx, exceptionAnalyzerService,
            LogsAnalyzerService.EXCEPTIONS_EVENT_BUS_ADDR);

        LOGGER.debug("Exception Log Analyzer Service available at address {}", LogsAnalyzerService.EXCEPTIONS_EVENT_BUS_ADDR);

        //Register Elastic Search Service
        final ConfigurationStoreOptions cfgStoreOpts = new ConfigurationStoreOptions()
            .setType("configmap")
            .setConfig(new JsonObject()
                .put("namespace", k8sNamespace)
                .put("name", configMapName));

        final ConfigurationRetrieverOptions cfgRetrieverOptions = new ConfigurationRetrieverOptions();
        cfgRetrieverOptions
            .addStore(cfgStoreOpts)
            .setScanPeriod(configMapScanInterval);

        ConfigurationRetriever configMapRetriever = ConfigurationRetriever.create(vertx, cfgRetrieverOptions);

        configMapRetriever.getConfiguration(configMap -> {
            if (configMap.succeeded()) {

                JsonObject configData = configMap.result();

                int huntingIntervalInSeconds = configData.getInteger("hunting-interval-seconds");

                String searchQuery = configData.getString("hunting-search-query");

                String esServiceName = configData.getString("elastic-search-service-name") == null ? "elasticsearch"
                    : configData.getString("elastic-search-service-name");

                String strIndexes = configData.getString("elastic-search-indexes");

                Objects.requireNonNull(strIndexes, "ConfigMap need to have indexes defined with attribute" +
                    " 'elastic-search-indexes' ");

                List<String> indexes = Stream.of(strIndexes.split(","))
                    .collect(Collectors.toList());

                int esServicePort = configData.getInteger("elastic-search-service-port");

                ElasticSearchOptions elasticSearchOptions = new ElasticSearchOptions()
                    .setConfigMap("bug-hunter")
                    .setKubernetesNamespace("default")
                    .setSsl(false)
                    .setPort(esServicePort)
                    .setHost(esServiceName)
                    .setIndexes(indexes);

                ElasticSearchService elasticSearchService = new ElasticSearchServiceImpl(vertx, elasticSearchOptions);
                ProxyHelper.registerService(ElasticSearchService.class, vertx, elasticSearchService,
                    ElasticSearchService.ES_SEARCH_BUS_ADDRESS);

                LOGGER.debug("ElasticSearch Service available at address {}", ElasticSearchService.ES_SEARCH_BUS_ADDRESS);

                final JsonObject config = new JsonObject();
                config.put("hunting-search-query", searchQuery);
                config.put("hunting-interval", huntingIntervalInSeconds);

                final DeploymentOptions deploymentOptions = new DeploymentOptions().setConfig(config);

                vertx.deployVerticle(BugHunterVerticle.class.getName(), deploymentOptions);

            } else {
                LOGGER.error("Error while loading ConfigMap {}", config().getString("configMapName"));
            }
        });
    }
}
