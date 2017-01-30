package io.fabric8.devops.apps.bughunter.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author kameshs
 */
@DataObject(generateConverter = true)
public class PodInfo {
    private String namespace;
    private String host;
    private String containerName;
    private String podId;
    private String podName;

    public PodInfo() {

    }

    public PodInfo(JsonObject json) {
        PodInfoConverter.fromJson(json, this);
    }

    public PodInfo(String namespace, String host, String containerName, String podId, String podName) {
        this.namespace = namespace;
        this.host = host;
        this.containerName = containerName;
        this.podId = podId;
        this.podName = podName;
    }

    public String getNamespace() {
        return namespace;
    }

    public PodInfo setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getHost() {
        return host;
    }

    public PodInfo setHost(String host) {
        this.host = host;
        return this;
    }

    public String getContainerName() {
        return containerName;
    }

    public PodInfo setContainerName(String containerName) {
        this.containerName = containerName;
        return this;
    }

    public String getPodId() {
        return podId;
    }

    public PodInfo setPodId(String podId) {
        this.podId = podId;
        return this;
    }

    public String getPodName() {
        return podName;
    }

    public PodInfo setPodName(String podName) {
        this.podName = podName;
        return this;
    }


    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        PodInfoConverter.toJson(this, json);
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PodInfo podInfo = (PodInfo) o;

        if (namespace != null ? !namespace.equals(podInfo.namespace) : podInfo.namespace != null) return false;
        return podId != null ? podId.equals(podInfo.podId) : podInfo.podId == null;
    }

    @Override
    public int hashCode() {
        int result = namespace != null ? namespace.hashCode() : 0;
        result = 31 * result + (podId != null ? podId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PodInfo{" +
            "namespace='" + namespace + '\'' +
            ", host='" + host + '\'' +
            ", containerName='" + containerName + '\'' +
            ", podId='" + podId + '\'' +
            ", podName='" + podName + '\'' +
            '}';
    }
}
