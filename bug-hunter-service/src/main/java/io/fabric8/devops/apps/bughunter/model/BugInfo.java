package io.fabric8.devops.apps.bughunter.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kameshs
 */
public class BugInfo{

    private String id;
    private float score;
    private Date timestamp;
    private List<PodInfo> pods = new ArrayList<>();
    private List<AppInfo> apps = new ArrayList<>();
    private String logMessage;
    private int count;

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

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<PodInfo> getPods() {
        return pods;
    }

    public void setPods(List<PodInfo> pods) {
        this.pods = pods;
    }

    public List<AppInfo> getApps() {
        return apps;
    }

    public void setApps(List<AppInfo> apps) {
        this.apps = apps;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
