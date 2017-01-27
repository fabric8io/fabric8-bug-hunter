package io.fabric8.devops.apps.bughunter.model;

public class PodInfoBuilder {
    private String namespace;
    private String host;
    private String containerName;
    private String podId;
    private String podName;

    public PodInfoBuilder setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public PodInfoBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public PodInfoBuilder setContainerName(String containerName) {
        this.containerName = containerName;
        return this;
    }

    public PodInfoBuilder setPodId(String podId) {
        this.podId = podId;
        return this;
    }

    public PodInfoBuilder setPodName(String podName) {
        this.podName = podName;
        return this;
    }

    public PodInfo createPodInfo() {
        return new PodInfo(namespace, host, containerName, podId, podName);
    }
}
