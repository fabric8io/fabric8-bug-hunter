package io.fabric8.devops.apps.bughunter;

import io.fabric8.devops.apps.bughunter.service.LogsAnalyzerService;
import io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchOptions;
import io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.configuration.ConfigurationRetriever;
import io.vertx.ext.configuration.ConfigurationRetrieverOptions;
import io.vertx.ext.configuration.ConfigurationStoreOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author kameshs
 */
public class BugHunterVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(BugHunterVerticle.class);

    @Override
    public void start() throws Exception {

        EventBus eventBus = vertx.eventBus();

        final ConfigurationStoreOptions cfgStoreOpts = new ConfigurationStoreOptions()
            .setType("configmap")
            .setConfig(new JsonObject()
                .put("namespace", "default")
                .put("name", "bug-hunter"));

        final ConfigurationRetrieverOptions cfgRetrieverOptions = new ConfigurationRetrieverOptions();
        cfgRetrieverOptions
            .addStore(cfgStoreOpts)
            .setScanPeriod(30000);

        ConfigurationRetriever configMapRetriever = ConfigurationRetriever.create(vertx, cfgRetrieverOptions);

        configMapRetriever.getConfiguration(configMap -> {
            if (configMap.succeeded()) {

                JsonObject configData = configMap.result();

                String esServiceName = configData.getString("elastic-search-service-name") == null ? "elasticsearch"
                    : configData.getString("elastic-search-service-name");

                String strIndexes = configData.getString("elastic-search-indexes");

                Objects.requireNonNull(strIndexes, "ConfigMap need to have indexes defined with attribute" +
                    " 'elastic-search-indexes' ");

                List<String> indexes = Stream.of(strIndexes.split(","))
                    .collect(Collectors.toList());

                int esServicePort = configData.getInteger("elastic-search-service-port");

                int huntingIntervalInSeconds = configData.getInteger("hunting-interval");

                String searchQuery = configData.getString("hunting-search-query");

                LOGGER.trace("Elastic Search Service Name: {} and on port {} with schedule " +
                        "{} seconds with Query:{}", esServiceName,
                    esServicePort, huntingIntervalInSeconds, searchQuery);

                ElasticSearchOptions elasticSearchOptions = new ElasticSearchOptions()
                    .setConfigMap("bug-hunter")
                    .setKubernetesNamespace("default")
                    .setSsl(false)
                    .setPort(esServicePort)
                    .setHost(esServiceName)
                    .setIndexes(indexes);

                ElasticSearchService elasticSearchService = ElasticSearchService
                    .createWithOptions(getVertx(), elasticSearchOptions);

                LogsAnalyzerService logsAnalyzerService = LogsAnalyzerService.createExceptionAnalyzer(getVertx());

                //TODO scheduled searches
                elasticSearchService.search(searchQuery, result -> {

                    if (result.succeeded()) {
                        LOGGER.trace("Result:{}", result.result());
                        eventBus.send(LogsAnalyzerService.EXCEPTIONS_EVENT_BUS_ADDR, result.result());
                        logsAnalyzerService.analyze(hitsAnalysisResult -> {
                            if (hitsAnalysisResult.succeeded()) {
                                LOGGER.info("Analysis Result {}", hitsAnalysisResult.result());
                            } else {
                                LOGGER.error("Error Analyzing Result", hitsAnalysisResult.cause());
                            }
                        });
                    } else {
                        LOGGER.error("Error:", result.cause());
                    }
                });
            } else {
                LOGGER.error("Unable to find config map ", configMap.cause());
            }
        });


    }
}
