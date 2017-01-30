/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.fabric8.devops.apps.elasticsearch.helper.service;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchOptions} original class using Vert.x codegen.
 */
public class ElasticSearchOptionsConverter {

  public static void fromJson(JsonObject json, ElasticSearchOptions obj) {
    if (json.getValue("configMap") instanceof String) {
      obj.setConfigMap((String)json.getValue("configMap"));
    }
    if (json.getValue("configMapScanPeriod") instanceof Number) {
      obj.setConfigMapScanPeriod(((Number)json.getValue("configMapScanPeriod")).longValue());
    }
    if (json.getValue("host") instanceof String) {
      obj.setHost((String)json.getValue("host"));
    }
    if (json.getValue("indexes") instanceof JsonArray) {
      java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
      json.getJsonArray("indexes").forEach( item -> {
        if (item instanceof String)
          list.add((String)item);
      });
      obj.setIndexes(list);
    }
    if (json.getValue("indexs") instanceof JsonArray) {
      json.getJsonArray("indexs").forEach(item -> {
        if (item instanceof String)
          obj.addIndex((String)item);
      });
    }
    if (json.getValue("kubernetesNamespace") instanceof String) {
      obj.setKubernetesNamespace((String)json.getValue("kubernetesNamespace"));
    }
    if (json.getValue("port") instanceof Number) {
      obj.setPort(((Number)json.getValue("port")).intValue());
    }
    if (json.getValue("ssl") instanceof Boolean) {
      obj.setSsl((Boolean)json.getValue("ssl"));
    }
  }

  public static void toJson(ElasticSearchOptions obj, JsonObject json) {
    if (obj.getConfigMap() != null) {
      json.put("configMap", obj.getConfigMap());
    }
    json.put("configMapScanPeriod", obj.getConfigMapScanPeriod());
    if (obj.getHost() != null) {
      json.put("host", obj.getHost());
    }
    if (obj.getIndexes() != null) {
      JsonArray array = new JsonArray();
      obj.getIndexes().forEach(item -> array.add(item));
      json.put("indexes", array);
    }
    if (obj.getKubernetesNamespace() != null) {
      json.put("kubernetesNamespace", obj.getKubernetesNamespace());
    }
    json.put("port", obj.getPort());
    json.put("ssl", obj.isSsl());
  }
}