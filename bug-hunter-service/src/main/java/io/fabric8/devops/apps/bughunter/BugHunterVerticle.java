package io.fabric8.devops.apps.bughunter;

import io.fabric8.devops.apps.bughunter.service.LogsAnalyzerService;
import io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

/**
 * @author kameshs
 */
public class BugHunterVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(BugHunterVerticle.class);


    @Override
    public void start() throws Exception {

        LOGGER.info("Starting Bug Hunter with Configuration {}", config());

        int huntingIntervalInSeconds = config().getInteger("hunting-interval");

        ElasticSearchService elasticSearchService = ElasticSearchService.createProxy(vertx);

        LogsAnalyzerService logsAnalyzerService = LogsAnalyzerService.createExceptionAnalyzerProxy(vertx);

        vertx.setPeriodic(huntingIntervalInSeconds * 1000, aLong -> {

            String searchQuery = config().getString("hunting-search-query");

            elasticSearchService.search(searchQuery, result -> {

                if (result.succeeded()) {
                    LOGGER.trace("Result:{}", result.result());
                    JsonObject jsonObject = result.result();
                    JsonObject hits = jsonObject.getJsonObject("hits");
                    JsonArray hitsOfHits = hits.getJsonArray("hits");
                    logsAnalyzerService.analyze(hitsOfHits, hitsAnalysisResult -> {
                        if (hitsAnalysisResult.succeeded()) {
                            JsonObject bugsData = hitsAnalysisResult.result();
                            LOGGER.debug("Analysis Result {}", bugsData);
                            JsonArray bugs = bugsData.getJsonArray("bugs");
                            Observable<Object> bugsObservable = Observable.from(bugs);
                            bugsObservable.map(JsonObject.class::cast)
                                .subscribe(bugData -> elasticSearchService.save("bughunter", "bugs", bugData,
                                    res -> {
                                        if (res.succeeded()) {
                                            LOGGER.info("Saved data:{}", res.result());
                                        } else {
                                            LOGGER.info("Error saving data", res.cause());
                                        }
                                    }));
                        } else {
                            LOGGER.error("Error Analyzing Result", hitsAnalysisResult.cause());
                        }
                    });
                } else {
                    LOGGER.error("Error:", result.cause());
                }
            });
        });
    }
}
