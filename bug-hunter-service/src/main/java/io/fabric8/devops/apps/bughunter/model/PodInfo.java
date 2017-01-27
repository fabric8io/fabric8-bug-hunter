package io.fabric8.devops.apps.bughunter.model;

/**
 * @author kameshs
 */
public class PodInfo {
    private String namespace;
    private String host;
    private String containerName;
    private String podId;
    private String podName;

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

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
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
