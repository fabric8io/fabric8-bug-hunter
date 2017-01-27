package io.fabric8.devops.apps.bughunter.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BugInfoBuilder {
    private Date timestamp;
    private List<PodInfo> pods = new ArrayList<>();
    private List<AppInfo> apps = new ArrayList<>();
    private String logMessage;
    private int count;
    private String id;
    private float score;


    public BugInfoBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public BugInfoBuilder setScore(float score) {
        this.score = score;
        return this;
    }

    public BugInfoBuilder setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public BugInfoBuilder addPod(PodInfo pod) {
        this.pods.add(pod);
        return this;
    }

    public BugInfoBuilder addApp(AppInfo app) {
        this.apps.add(app);
        return this;
    }

    public BugInfoBuilder setLogMessage(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }

    public BugInfoBuilder setCount(int count) {
        this.count = count;
        return this;
    }

    public BugInfo createBugInfo() {
        return new BugInfo(id, score, timestamp, pods, apps, logMessage, count);
    }

    public BugInfoBuilder setPods(List<PodInfo> pods) {
        this.pods = pods;
        return this;
    }

    public BugInfoBuilder setApps(List<AppInfo> apps) {
        this.apps = apps;
        return this;
    }
}
