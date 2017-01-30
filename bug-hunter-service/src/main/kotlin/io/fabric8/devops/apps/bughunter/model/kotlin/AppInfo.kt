package io.fabric8.devops.apps.bughunter.model.kotlin

import io.fabric8.devops.apps.bughunter.model.AppInfo

/**
 * A function providing a DSL for building [io.fabric8.devops.apps.bughunter.model.AppInfo] objects.
 *
 *
 * @param branch 
 * @param group 
 * @param issueTrackerUrl 
 * @param project 
 * @param projectUrl 
 * @param revision 
 * @param version 
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.fabric8.devops.apps.bughunter.model.AppInfo original] using Vert.x codegen.
 */
fun AppInfo(
  branch: String? = null,
  group: String? = null,
  issueTrackerUrl: String? = null,
  project: String? = null,
  projectUrl: String? = null,
  revision: String? = null,
  version: String? = null): AppInfo = io.fabric8.devops.apps.bughunter.model.AppInfo().apply {

  if (branch != null) {
    this.setBranch(branch)
  }
  if (group != null) {
    this.setGroup(group)
  }
  if (issueTrackerUrl != null) {
    this.setIssueTrackerUrl(issueTrackerUrl)
  }
  if (project != null) {
    this.setProject(project)
  }
  if (projectUrl != null) {
    this.setProjectUrl(projectUrl)
  }
  if (revision != null) {
    this.setRevision(revision)
  }
  if (version != null) {
    this.setVersion(version)
  }
}

