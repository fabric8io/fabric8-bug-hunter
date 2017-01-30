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
 * Converter for {@link io.fabric8.devops.apps.bughunter.model.BugInfo}.
 *
 * NOTE: This class has been automatically generated from the {@link io.fabric8.devops.apps.bughunter.model.BugInfo} original class using Vert.x codegen.
 */
public class BugInfoConverter {

  public static void fromJson(JsonObject json, BugInfo obj) {
    if (json.getValue("app") instanceof JsonObject) {
      obj.setApp(new io.fabric8.devops.apps.bughunter.model.AppInfo((JsonObject)json.getValue("app")));
    }
    if (json.getValue("count") instanceof Number) {
      obj.setCount(((Number)json.getValue("count")).intValue());
    }
    if (json.getValue("id") instanceof String) {
      obj.setId((String)json.getValue("id"));
    }
    if (json.getValue("logMessage") instanceof String) {
      obj.setLogMessage((String)json.getValue("logMessage"));
    }
    if (json.getValue("pod") instanceof JsonObject) {
      obj.setPod(new io.fabric8.devops.apps.bughunter.model.PodInfo((JsonObject)json.getValue("pod")));
    }
    if (json.getValue("score") instanceof Number) {
      obj.setScore(((Number)json.getValue("score")).floatValue());
    }
    if (json.getValue("timestamp") instanceof String) {
      obj.setTimestamp((String)json.getValue("timestamp"));
    }
  }

  public static void toJson(BugInfo obj, JsonObject json) {
    if (obj.getApp() != null) {
      json.put("app", obj.getApp().toJson());
    }
    json.put("count", obj.getCount());
    if (obj.getId() != null) {
      json.put("id", obj.getId());
    }
    if (obj.getLogMessage() != null) {
      json.put("logMessage", obj.getLogMessage());
    }
    if (obj.getPod() != null) {
      json.put("pod", obj.getPod().toJson());
    }
    json.put("score", obj.getScore());
    if (obj.getTimestamp() != null) {
      json.put("timestamp", obj.getTimestamp());
    }
  }
}