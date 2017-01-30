package io.fabric8.devops.bughunter.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.fabric8.devops.apps.bughunter.BugHunterVerticle;
import io.fabric8.devops.apps.bughunter.service.LogsAnalyzerService;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
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

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author kameshs
 */
@RunWith(VertxUnitRunner.class)
public class HitsAnalyzerTest {

    private static Vertx vertx;

    @BeforeClass
    public static void setup() {
        vertx = Vertx.vertx();
    }

    @Test
    public void testBuildBugInfoModel(TestContext testContext) throws Exception {

        EventBus eventBus = vertx.eventBus();

        String jsonResult = read(this.getClass().getResourceAsStream("/results.json"));

        Map<String, Object> objectMap = new Gson().fromJson(
            jsonResult, new TypeToken<HashMap<String, Object>>() {
            }.getType()
        );

        JsonObject hitsData = new JsonObject(objectMap);


        LogsAnalyzerService logsAnalyzerService = LogsAnalyzerService.createExceptionAnalyzer(vertx);

        eventBus.send(LogsAnalyzerService.EXCEPTIONS_EVENT_BUS_ADDR, hitsData, event -> {
            if (event.succeeded()) {
                System.out.println("success");
            } else {
                System.err.println(event.cause());
            }
        });

        logsAnalyzerService.analyze(result -> {
            if (result.succeeded()) {
                System.out.println(result.result());

            } else {
                System.err.println(result.cause());
            }
            testContext.async().complete();
        });

        SECONDS.sleep(13); //give it some time for the process to run in BG

    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

}
