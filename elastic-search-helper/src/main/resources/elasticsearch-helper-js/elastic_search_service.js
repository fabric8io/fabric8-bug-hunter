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

/** @module elasticsearch-helper-js/elastic_search_service */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JElasticSearchService = Java.type('io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchService');
var ElasticSearchOptions = Java.type('io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchOptions');

/**

 @class
*/
var ElasticSearchService = function(j_val) {

  var j_elasticSearchService = j_val;
  var that = this;

  /**

   @public
   @param searchQuery {string} 
   @param resultHandler {function} 
   */
  this.search = function(searchQuery, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_elasticSearchService["search(java.lang.String,io.vertx.core.Handler)"](searchQuery, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnJson(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_elasticSearchService;
};

ElasticSearchService._jclass = utils.getJavaClass("io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchService");
ElasticSearchService._jtype = {
  accept: function(obj) {
    return ElasticSearchService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(ElasticSearchService.prototype, {});
    ElasticSearchService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
ElasticSearchService._create = function(jdel) {
  var obj = Object.create(ElasticSearchService.prototype, {});
  ElasticSearchService.apply(obj, arguments);
  return obj;
}
/**

 @memberof module:elasticsearch-helper-js/elastic_search_service
 @param vertx {Vertx} 
 @return {ElasticSearchService}
 */
ElasticSearchService.create = function(vertx) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(ElasticSearchService, JElasticSearchService["create(io.vertx.core.Vertx)"](vertx._jdel));
  } else throw new TypeError('function invoked with invalid arguments');
};

/**

 @memberof module:elasticsearch-helper-js/elastic_search_service
 @param vertx {Vertx} 
 @param elasticSearchOptions {Object} 
 @return {ElasticSearchService}
 */
ElasticSearchService.createWithOptions = function(vertx, elasticSearchOptions) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null)) {
    return utils.convReturnVertxGen(ElasticSearchService, JElasticSearchService["createWithOptions(io.vertx.core.Vertx,io.fabric8.devops.apps.elasticsearch.helper.service.ElasticSearchOptions)"](vertx._jdel, elasticSearchOptions != null ? new ElasticSearchOptions(new JsonObject(Java.asJSONCompatible(elasticSearchOptions))) : null));
  } else throw new TypeError('function invoked with invalid arguments');
};

module.exports = ElasticSearchService;