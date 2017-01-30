package io.fabric8.devops.bughunter.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.fabric8.devops.apps.bughunter.BugHunterVerticle;
import io.fabric8.devops.apps.bughunter.service.LogsAnalyzerService;
import io.fabric8.devops.apps.bughunter.service.impl.ExceptionsLogsAnalyzer;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.serviceproxy.ProxyHelper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kameshs
 */
@RunWith(VertxUnitRunner.class)
public class HitsAnalyzerTest {

    private static Vertx vertx;

    @BeforeClass
    public static void setup() {
        vertx = Vertx.vertx();
        LogsAnalyzerService exceptionAnalyzerService = new ExceptionsLogsAnalyzer(vertx);
        ProxyHelper.registerService(LogsAnalyzerService.class, vertx, exceptionAnalyzerService,
            LogsAnalyzerService.EXCEPTIONS_EVENT_BUS_ADDR);


    }

    @Test
    public void testDeployBugHunterVerticle(TestContext context) {
        //Async async = context.async();

        JsonObject config = new JsonObject()
            .put("hunting-search-query", "*")
            .put("hunting-interval", 10000);

        DeploymentOptions deploymentOptions = new DeploymentOptions().setConfig(config);

        vertx.deployVerticle(BugHunterVerticle.class.getName(), deploymentOptions, context.asyncAssertSuccess());
    }


    @Test
    public void testBuildBugInfoModel(TestContext testContext) throws Exception {

        String jsonResult = read(this.getClass().getResourceAsStream("/results.json"));
        Async async = testContext.async();
        Map<String, Object> objectMap = new Gson().fromJson(
            jsonResult, new TypeToken<HashMap<String, Object>>() {
            }.getType()
        );

        JsonObject hitsData = new JsonObject(objectMap);

        LogsAnalyzerService logsAnalyzerService = LogsAnalyzerService.createExceptionAnalyzerProxy(vertx);

        logsAnalyzerService.analyze(hitsData.getJsonObject("hits").getJsonArray("hits"), result -> {
            if (result.succeeded()) {
                JsonArray bugsInfos = result.result().getJsonArray("bugs");
                testContext.assertNotNull(bugsInfos);
                testContext.assertFalse(bugsInfos.isEmpty());
                testContext.assertEquals(bugsInfos.size(), 2);
                async.complete();
            } else {
                testContext.fail(result.cause());
            }
        });
    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

}
