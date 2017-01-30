package io.fabric8.devops.apps.bughunter.model.kotlin

import io.fabric8.devops.apps.bughunter.model.BugInfo
import io.fabric8.devops.apps.bughunter.model.AppInfo
import io.fabric8.devops.apps.bughunter.model.PodInfo

/**
 * A function providing a DSL for building [io.fabric8.devops.apps.bughunter.model.BugInfo] objects.
 *
 *
 * @param app 
 * @param count 
 * @param id 
 * @param logMessage 
 * @param pod 
 * @param score 
 * @param timestamp 
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.fabric8.devops.apps.bughunter.model.BugInfo original] using Vert.x codegen.
 */
fun BugInfo(
  app: io.fabric8.devops.apps.bughunter.model.AppInfo? = null,
  count: Int? = null,
  id: String? = null,
  logMessage: String? = null,
  pod: io.fabric8.devops.apps.bughunter.model.PodInfo? = null,
  score: Float? = null,
  timestamp: String? = null): BugInfo = io.fabric8.devops.apps.bughunter.model.BugInfo().apply {

  if (app != null) {
    this.setApp(app)
  }
  if (count != null) {
    this.setCount(count)
  }
  if (id != null) {
    this.setId(id)
  }
  if (logMessage != null) {
    this.setLogMessage(logMessage)
  }
  if (pod != null) {
    this.setPod(pod)
  }
  if (score != null) {
    this.setScore(score)
  }
  if (timestamp != null) {
    this.setTimestamp(timestamp)
  }
}

