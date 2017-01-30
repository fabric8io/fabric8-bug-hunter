package io.fabric8.devops.apps.bughunter.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author kameshs
 */
@DataObject(generateConverter = true)
public class BugInfo {

    private String id;
    private float score;
    private String timestamp;
    private PodInfo pod;
    private AppInfo app;
    private String logMessage;
    private int count;

    public BugInfo() {

    }

    public BugInfo(JsonObject json) {
        BugInfoConverter.fromJson(json, this);
    }

    public BugInfo(String id, float score, String timestamp, PodInfo pod, AppInfo app, String logMessage, int count) {
        this.id = id;
        this.score = score;
        this.timestamp = timestamp;
        this.pod = pod;
        this.app = app;
        this.logMessage = logMessage;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public BugInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public BugInfo setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public PodInfo getPod() {
        return pod;
    }

    public BugInfo setPod(PodInfo pod) {
        this.pod = pod;
        return this;
    }

    public AppInfo getApp() {
        return app;
    }

    public BugInfo setApp(AppInfo app) {
        this.app = app;
        return this;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public BugInfo setLogMessage(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }

    public int getCount() {
        return count;
    }

    public BugInfo setCount(int count) {
        this.count = count;
        return this;
    }

    public float getScore() {
        return score;
    }

    public BugInfo setScore(float score) {
        this.score = score;
        return this;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        BugInfoConverter.toJson(this, json);
        return json;
    }
}
