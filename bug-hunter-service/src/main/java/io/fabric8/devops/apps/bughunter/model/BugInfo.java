package io.fabric8.devops.apps.bughunter.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kameshs
 */
@DataObject(generateConverter = true)
public class BugInfo {

    private String id;
    private float score;
    private Date timestamp;
    private List<PodInfo> pods = new ArrayList<>();
    private List<AppInfo> apps = new ArrayList<>();
    private String logMessage;
    private int count;

    public BugInfo() {

    }

    public BugInfo(JsonObject json) {
        BugInfoConverter.fromJson(json, this);
    }

    public BugInfo(String id, float score, Date timestamp, List<PodInfo> pods, List<AppInfo> apps, String logMessage, int count) {
        this.id = id;
        this.score = score;
        this.timestamp = timestamp;
        this.pods = pods;
        this.apps = apps;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public BugInfo setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public List<PodInfo> getPods() {
        return pods;
    }

    public BugInfo setPods(List<PodInfo> pods) {
        this.pods = pods;
        return this;
    }

    public List<AppInfo> getApps() {
        return apps;
    }

    public BugInfo setApps(List<AppInfo> apps) {
        this.apps = apps;
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
