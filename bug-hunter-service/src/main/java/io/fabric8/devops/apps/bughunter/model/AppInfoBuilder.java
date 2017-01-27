package io.fabric8.devops.apps.bughunter.model;

public class AppInfoBuilder {
    private String group;
    private String project;
    private String version;
    private String revision;
    private String issueTrackerUrl;
    private String projectUrl;
    private String branch;

    public AppInfoBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public AppInfoBuilder setProject(String project) {
        this.project = project;
        return this;
    }

    public AppInfoBuilder setVersion(String version) {
        this.version = version;
        return this;
    }

    public AppInfoBuilder setRevision(String revision) {
        this.revision = revision;
        return this;
    }

    public AppInfoBuilder setIssueTrackerUrl(String issueTrackerUrl) {
        this.issueTrackerUrl = issueTrackerUrl;
        return this;
    }

    public AppInfoBuilder setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
        return this;
    }

    public AppInfo createAppInfo() {
        return new AppInfo(group, project, version, branch, revision, issueTrackerUrl, projectUrl);
    }

    public AppInfoBuilder setBranch(String branch) {
        this.branch = branch;
        return this;
    }
}
