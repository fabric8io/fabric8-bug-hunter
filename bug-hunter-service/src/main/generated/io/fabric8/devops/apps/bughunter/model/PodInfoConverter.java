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

package io.fabric8.devops.apps.bughunter.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.fabric8.devops.apps.bughunter.model.PodInfo}.
 *
 * NOTE: This class has been automatically generated from the {@link io.fabric8.devops.apps.bughunter.model.PodInfo} original class using Vert.x codegen.
 */
public class PodInfoConverter {

  public static void fromJson(JsonObject json, PodInfo obj) {
    if (json.getValue("containerName") instanceof String) {
      obj.setContainerName((String)json.getValue("containerName"));
    }
    if (json.getValue("host") instanceof String) {
      obj.setHost((String)json.getValue("host"));
    }
    if (json.getValue("namespace") instanceof String) {
      obj.setNamespace((String)json.getValue("namespace"));
    }
    if (json.getValue("podId") instanceof String) {
      obj.setPodId((String)json.getValue("podId"));
    }
    if (json.getValue("podName") instanceof String) {
      obj.setPodName((String)json.getValue("podName"));
    }
  }

  public static void toJson(PodInfo obj, JsonObject json) {
    if (obj.getContainerName() != null) {
      json.put("containerName", obj.getContainerName());
    }
    if (obj.getHost() != null) {
      json.put("host", obj.getHost());
    }
    if (obj.getNamespace() != null) {
      json.put("namespace", obj.getNamespace());
    }
    if (obj.getPodId() != null) {
      json.put("podId", obj.getPodId());
    }
    if (obj.getPodName() != null) {
      json.put("podName", obj.getPodName());
    }
  }
}