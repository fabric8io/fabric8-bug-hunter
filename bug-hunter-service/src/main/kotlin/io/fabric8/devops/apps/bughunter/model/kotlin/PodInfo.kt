package io.fabric8.devops.apps.bughunter.model.kotlin

import io.fabric8.devops.apps.bughunter.model.PodInfo

/**
 * A function providing a DSL for building [io.fabric8.devops.apps.bughunter.model.PodInfo] objects.
 *
 *
 * @param containerName 
 * @param host 
 * @param namespace 
 * @param podId 
 * @param podName 
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.fabric8.devops.apps.bughunter.model.PodInfo original] using Vert.x codegen.
 */
fun PodInfo(
  containerName: String? = null,
  host: String? = null,
  namespace: String? = null,
  podId: String? = null,
  podName: String? = null): PodInfo = io.fabric8.devops.apps.bughunter.model.PodInfo().apply {

  if (containerName != null) {
    this.setContainerName(containerName)
  }
  if (host != null) {
    this.setHost(host)
  }
  if (namespace != null) {
    this.setNamespace(namespace)
  }
  if (podId != null) {
    this.setPodId(podId)
  }
  if (podName != null) {
    this.setPodName(podName)
  }
}

