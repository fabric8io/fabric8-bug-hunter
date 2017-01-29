package io.fabric8.devops.apps.elasticsearch.helper.service;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author kameshs
 */
@DataObject(
    generateConverter = true
)
public class ElasticSearchOptions {

    private boolean ssl;
    private String configMap;
    private String kubernetesNamespace;
    private long configMapScanPeriod;
    private List<String> indexes;
    private String host;
    private int port;

    //TODO add ES search template

    public ElasticSearchOptions() {
        this.ssl = false;
        this.host = "localhost";
        this.port = 9200;
        this.configMapScanPeriod = 30000;
    }

    public ElasticSearchOptions(JsonObject json) {
        ElasticSearchOptionsConverter.fromJson(json, this);
    }

    public boolean isSsl() {
        return ssl;
    }

    public ElasticSearchOptions setSsl(boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    public String getConfigMap() {
        return configMap;
    }

    public ElasticSearchOptions setConfigMap(String configMap) {
        this.configMap = configMap;
        return this;
    }

    public String getHost() {
        return host;
    }

    public ElasticSearchOptions setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ElasticSearchOptions setPort(int port) {
        this.port = port;
        return this;
    }

    public String getKubernetesNamespace() {
        return kubernetesNamespace;
    }

    public ElasticSearchOptions setKubernetesNamespace(String kubernetesNamespace) {
        this.kubernetesNamespace = kubernetesNamespace;
        return this;
    }

    public long getConfigMapScanPeriod() {
        return configMapScanPeriod;
    }

    public ElasticSearchOptions setConfigMapScanPeriod(long configMapScanPeriod) {
        this.configMapScanPeriod = configMapScanPeriod;
        return this;
    }

    public List<String> getIndexes() {
        return indexes;
    }

    public ElasticSearchOptions setIndexes(List<String> indexes) {
        this.indexes = indexes;
        return this;
    }

    public ElasticSearchOptions addIndex(String index) {
        this.indexes.add(index);
        return this;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("configMap", configMap);
        jsonObject.put("host", host);
        jsonObject.put("port", port);
        JsonArray indexArray = new JsonArray();
        indexes.forEach(s -> indexArray.add(s));
        jsonObject.put("indexes", indexArray);
        jsonObject.put("kubernetesNamespace", kubernetesNamespace);
        jsonObject.put("configMapScanPeriod", configMapScanPeriod);
        return jsonObject;
    }
}
