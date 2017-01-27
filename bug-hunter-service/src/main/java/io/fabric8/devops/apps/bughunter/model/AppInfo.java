package io.fabric8.devops.apps.bughunter.model;

import io.vertx.core.json.JsonObject;

/**
 * @author kameshs
 */
public class AppInfo {
    private String group;
    private String project;
    private String version;
    private String branch;
    private String revision;
    private String issueTrackerUrl;
    private String projectUrl;

    public AppInfo(String group, String project, String version, String branch, String revision, String issueTrackerUrl, String projectUrl) {
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

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getIssueTrackerUrl() {
        return issueTrackerUrl;
    }

    public void setIssueTrackerUrl(String issueTrackerUrl) {
        this.issueTrackerUrl = issueTrackerUrl;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
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
