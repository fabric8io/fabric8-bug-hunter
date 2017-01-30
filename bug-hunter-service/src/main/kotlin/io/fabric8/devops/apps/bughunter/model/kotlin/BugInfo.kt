package io.fabric8.devops.apps.bughunter.model.kotlin

import io.fabric8.devops.apps.bughunter.model.BugInfo
import io.fabric8.devops.apps.bughunter.model.AppInfo
import io.fabric8.devops.apps.bughunter.model.PodInfo

/**
 * A function providing a DSL for building [io.fabric8.devops.apps.bughunter.model.BugInfo] objects.
 *
 *
 * @param apps 
 * @param count 
 * @param id 
 * @param logMessage 
 * @param pods 
 * @param score 
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.fabric8.devops.apps.bughunter.model.BugInfo original] using Vert.x codegen.
 */
fun BugInfo(
  apps: List<io.fabric8.devops.apps.bughunter.model.AppInfo>? = null,
  count: Int? = null,
  id: String? = null,
  logMessage: String? = null,
  pods: List<io.fabric8.devops.apps.bughunter.model.PodInfo>? = null,
  score: Float? = null): BugInfo = io.fabric8.devops.apps.bughunter.model.BugInfo().apply {

  if (apps != null) {
    this.setApps(apps)
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
  if (pods != null) {
    this.setPods(pods)
  }
  if (score != null) {
    this.setScore(score)
  }
}

