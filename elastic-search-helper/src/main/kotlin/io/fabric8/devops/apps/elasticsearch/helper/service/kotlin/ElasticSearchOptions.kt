package io.fabric8.devops.apps.elasticsearch.helper.service.kotlin

import io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchOptions

/**
 * A function providing a DSL for building [io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchOptions] objects.
 *
 *
 * @param configMap 
 * @param configMapScanPeriod 
 * @param host 
 * @param indexes 
 * @param indexs 
 * @param kubernetesNamespace 
 * @param port 
 * @param ssl 
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchOptions original] using Vert.x codegen.
 */
fun ElasticSearchOptions(
  configMap: String? = null,
  configMapScanPeriod: Long? = null,
  host: String? = null,
  indexes: Iterable<String>? = null,
  indexs: Iterable<String>? = null,
  kubernetesNamespace: String? = null,
  port: Int? = null,
  ssl: Boolean? = null): ElasticSearchOptions = io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchOptions().apply {

  if (configMap != null) {
    this.setConfigMap(configMap)
  }
  if (configMapScanPeriod != null) {
    this.setConfigMapScanPeriod(configMapScanPeriod)
  }
  if (host != null) {
    this.setHost(host)
  }
  if (indexes != null) {
    this.setIndexes(indexes.toList())
  }
  if (indexs != null) {
    for (item in indexs) {
      this.addIndex(item)
    }
  }
  if (kubernetesNamespace != null) {
    this.setKubernetesNamespace(kubernetesNamespace)
  }
  if (port != null) {
    this.setPort(port)
  }
  if (ssl != null) {
    this.setSsl(ssl)
  }
}

