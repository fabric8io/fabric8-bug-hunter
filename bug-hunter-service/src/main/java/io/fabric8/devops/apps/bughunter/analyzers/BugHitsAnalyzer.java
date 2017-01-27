package io.fabric8.devops.apps.bughunter.analyzers;

import io.fabric8.devops.apps.bughunter.events.ExceptionsEventManager;
import io.fabric8.devops.apps.bughunter.model.AppInfoBuilder;
import io.fabric8.devops.apps.bughunter.model.BugInfo;
import io.fabric8.devops.apps.bughunter.model.BugInfoBuilder;
import io.fabric8.devops.apps.bughunter.model.PodInfoBuilder;
import io.fabric8.devops.apps.bughunter.util.DateUtil;
import io.fabric8.devops.apps.bughunter.util.KubernetesUtil;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.rxjava.core.eventbus.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * @author kameshs
 */
public class BugHitsAnalyzer extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(BugHitsAnalyzer.class);

    EventBus eventBus;

    @Override
    public void start() throws Exception {

        LOGGER.info("Starting Hits Analyzer");

        eventBus = vertx.eventBus();

        MessageConsumer<JsonArray> bugHitsConsumer = eventBus.consumer(ExceptionsEventManager.BUGS_HIT_ANALZYER);

        Observable<Message<JsonArray>> hitsObservable = bugHitsConsumer.toObservable();

        hitsObservable.subscribe(message -> {

            JsonArray hits = message.body();

            LOGGER.info("Analyzing Hits {}", hits.size());

            ArrayList<BugInfo> bugInfos = new ArrayList<>();

            hits.stream().forEach(o -> {
                JsonObject hit = (JsonObject) o;


                BugInfoBuilder bugInfoBuilder = new BugInfoBuilder();
                AppInfoBuilder appInfoBuilder = new AppInfoBuilder();
                PodInfoBuilder podInfoBuilder = new PodInfoBuilder();

                LOGGER.trace("Hit:{}", hit);
                if (hit != null) {

                    String id = hit.getString("_id");
                    float score = hit.getFloat("_score");

                    JsonObject msgSource = hit.getJsonObject("_source");

                    JsonObject kubernetes = msgSource.getJsonObject("kubernetes");
                    String k8sNamespace = kubernetes.getString("namespace_name");
                    String k8sPodId = kubernetes.getString("pod_id");
                    String k8sPodName = kubernetes.getString("pod_name");
                    String podHostName = kubernetes.getString("host");
                    String containerName = kubernetes.getString("container_name");

                    JsonObject labels = kubernetes.getJsonObject("labels");
                    String group = labels.containsKey("group") ? labels.getString("group") : "";
                    String project = labels.containsKey("project") ? labels.getString("project") : "";
                    String version = labels.containsKey("version") ? labels.getString("version") : "";
                    String provider = labels.containsKey("version") ? labels.getString("provider") : "";

                    Map<String, String> appMetadata = KubernetesUtil.deploymentAnnotations(group, project, version, provider);
                    //Application specific information
                    LOGGER.info("App Medata:{}", appMetadata);
                    appInfoBuilder
                        .setBranch(appMetadata.containsKey(KubernetesUtil.SCM_BRANCH)
                            ? appMetadata.get(KubernetesUtil.SCM_BRANCH) : "")
                        .setGroup(group)
                        .setIssueTrackerUrl(appMetadata.containsKey(KubernetesUtil.SCM_ISSUE_TRACKER_URL)
                            ? appMetadata.get(KubernetesUtil.SCM_ISSUE_TRACKER_URL) : "")
                        .setProject(project)
                        .setProjectUrl(appMetadata.containsKey(KubernetesUtil.SCM_PROJECT_URL)
                            ? appMetadata.get(KubernetesUtil.SCM_PROJECT_URL) : "")
                        .setRevision(appMetadata.containsKey(KubernetesUtil.SCM_REVISION)
                            ? appMetadata.get(KubernetesUtil.SCM_REVISION) : "")
                        .setVersion(version)
                        .createAppInfo();

                    bugInfoBuilder.addApp(appInfoBuilder.createAppInfo());

                    //Add Pod Related Information
                    podInfoBuilder
                        .setContainerName(containerName)
                        .setHost(podHostName)
                        .setNamespace(k8sNamespace)
                        .setPodId(k8sPodId)
                        .setPodName(k8sPodName);

                    bugInfoBuilder.addPod(podInfoBuilder.createPodInfo());

                    //Retrieve Log Info
                    String logMessage = msgSource.getString("log");
                    Date timestamp = timestamp(msgSource.getString("@timestamp"));

                    bugInfoBuilder.setId(id)
                        .setScore(score)
                        .setTimestamp(timestamp)
                        .setLogMessage(logMessage);

                    bugInfos.add(bugInfoBuilder.createBugInfo());
                }

            });
            message.reply(Json.encode(bugInfos));
        }, error -> LOGGER.error("Error Analyzing Hits", error));

    }

    private Date timestamp(String strDate) {
        return DateUtil.fromISO8601(strDate);
    }
}
