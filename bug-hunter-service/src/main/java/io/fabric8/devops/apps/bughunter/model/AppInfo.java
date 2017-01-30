package io.fabric8.devops.apps.bughunter.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author kameshs
 */
@DataObject(generateConverter = true)
public class AppInfo {
    private String group;
    private String project;
    private String version;
    private String branch;
    private String revision;
    private String issueTrackerUrl;
    private String projectUrl;

    public AppInfo() {
    }

    public AppInfo(JsonObject json) {
        AppInfoConverter.fromJson(json, this);
    }

    public AppInfo(String group, String project, String version, String branch, String revision,
                   String issueTrackerUrl, String projectUrl) {
        this.group = group;
        this.project = project;
        this.version = version;
        this.branch = branch;
        this.revision = revision;
        this.issueTrackerUrl = issueTrackerUrl;
        this.projectUrl = projectUrl;
    }

    public String getBranch() {
        return branch;
    }

    public AppInfo setBranch(String branch) {
        this.branch = branch;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public AppInfo setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getProject() {
        return project;
    }

    public AppInfo setProject(String project) {
        this.project = project;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public AppInfo setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getRevision() {
        return revision;
    }

    public AppInfo setRevision(String revision) {
        this.revision = revision;
        return this;
    }

    public String getIssueTrackerUrl() {
        return issueTrackerUrl;
    }

    public AppInfo setIssueTrackerUrl(String issueTrackerUrl) {
        this.issueTrackerUrl = issueTrackerUrl;
        return this;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public AppInfo setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
        return this;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        AppInfoConverter.toJson(this, json);
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppInfo appInfo = (AppInfo) o;

        if (!group.equals(appInfo.group)) return false;
        if (!project.equals(appInfo.project)) return false;
        if (!version.equals(appInfo.version)) return false;
        return revision.equals(appInfo.revision);
    }

    @Override
    public int hashCode() {
        int result = group.hashCode();
        result = 31 * result + project.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + revision.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
            "group='" + group + '\'' +
            ", project='" + project + '\'' +
            ", version='" + version + '\'' +
            ", branch='" + branch + '\'' +
            ", revision='" + revision + '\'' +
            ", issueTrackerUrl='" + issueTrackerUrl + '\'' +
            ", projectUrl='" + projectUrl + '\'' +
            '}';
    }
}
