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
 * Converter for {@link io.fabric8.devops.apps.bughunter.model.AppInfo}.
 *
 * NOTE: This class has been automatically generated from the {@link io.fabric8.devops.apps.bughunter.model.AppInfo} original class using Vert.x codegen.
 */
public class AppInfoConverter {

  public static void fromJson(JsonObject json, AppInfo obj) {
    if (json.getValue("branch") instanceof String) {
      obj.setBranch((String)json.getValue("branch"));
    }
    if (json.getValue("group") instanceof String) {
      obj.setGroup((String)json.getValue("group"));
    }
    if (json.getValue("issueTrackerUrl") instanceof String) {
      obj.setIssueTrackerUrl((String)json.getValue("issueTrackerUrl"));
    }
    if (json.getValue("project") instanceof String) {
      obj.setProject((String)json.getValue("project"));
    }
    if (json.getValue("projectUrl") instanceof String) {
      obj.setProjectUrl((String)json.getValue("projectUrl"));
    }
    if (json.getValue("revision") instanceof String) {
      obj.setRevision((String)json.getValue("revision"));
    }
    if (json.getValue("version") instanceof String) {
      obj.setVersion((String)json.getValue("version"));
    }
  }

  public static void toJson(AppInfo obj, JsonObject json) {
    if (obj.getBranch() != null) {
      json.put("branch", obj.getBranch());
    }
    if (obj.getGroup() != null) {
      json.put("group", obj.getGroup());
    }
    if (obj.getIssueTrackerUrl() != null) {
      json.put("issueTrackerUrl", obj.getIssueTrackerUrl());
    }
    if (obj.getProject() != null) {
      json.put("project", obj.getProject());
    }
    if (obj.getProjectUrl() != null) {
      json.put("projectUrl", obj.getProjectUrl());
    }
    if (obj.getRevision() != null) {
      json.put("revision", obj.getRevision());
    }
    if (obj.getVersion() != null) {
      json.put("version", obj.getVersion());
    }
  }
}