package io.fabric8.devops.bughunter.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.fabric8.devops.apps.bughunter.BugHunterVerticle;
import io.fabric8.devops.apps.bughunter.events.ExceptionsEventManager;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Single;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author kameshs
 */
@RunWith(VertxUnitRunner.class)
public class HitsAnalyzerTest {

    private static Vertx vertx;

    @BeforeClass
    public static void deployVerticles() {
        vertx = Vertx.vertx();
        vertx.deployVerticle(ExceptionsEventManager.class.getName());
    }

    @Test
    public void testBuildBugInfoModel(TestContext testContext) throws Exception {
        EventBus eb = vertx.eventBus();

        String jsonResult = read(this.getClass().getResourceAsStream("/results.json"));

        Map<String, Object> objectMap = new Gson().fromJson(
            jsonResult, new TypeToken<HashMap<String, Object>>() {
            }.getType()
        );

        JsonObject hitsData = new JsonObject(objectMap);

        Single<Message<String>> single = eb.rxSend(BugHunterVerticle.EXCEPTIONS_EVENT_BUS_ADDR, hitsData);

        single.subscribe(response -> {
            //System.out.println(response.body());
            List<Map> bugs = Json.decodeValue(response.body(), List.class);
            assertThat(bugs).isNotNull();
            assertThat(bugs).isNotEmpty();
            assertThat(bugs.size()).isEqualTo(2);
            testContext.async().complete();
            assertThat(bugs.get(0).get("id")).isEqualTo("AVnbzxBZZB-P-5-RnI_G");
            assertThat(bugs.get(1).get("score")).isEqualTo(9.772448);
        }, error -> error.printStackTrace());

        SECONDS.sleep(3); //give it some time for the process to run in BG

    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

}
