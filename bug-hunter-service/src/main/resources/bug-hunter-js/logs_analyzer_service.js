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

/** @module bug-hunter-js/logs_analyzer_service */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JLogsAnalyzerService = Java.type('io.fabric8.devops.apps.bughunter.service.LogsAnalyzerService');

/**

 @class
*/
var LogsAnalyzerService = function(j_val) {

  var j_logsAnalyzerService = j_val;
  var that = this;

  /**

   @public
   @param hits {todo} 
   @param resultHandler {function} 
   */
  this.analyze = function(hits, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0] instanceof Array && typeof __args[1] === 'function') {
      j_logsAnalyzerService["analyze(io.vertx.core.json.JsonArray,io.vertx.core.Handler)"](utils.convParamJsonArray(hits), function(ar) {
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
  this._jdel = j_logsAnalyzerService;
};

LogsAnalyzerService._jclass = utils.getJavaClass("io.fabric8.devops.apps.bughunter.service.LogsAnalyzerService");
LogsAnalyzerService._jtype = {
  accept: function(obj) {
    return LogsAnalyzerService._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(LogsAnalyzerService.prototype, {});
    LogsAnalyzerService.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
LogsAnalyzerService._create = function(jdel) {
  var obj = Object.create(LogsAnalyzerService.prototype, {});
  LogsAnalyzerService.apply(obj, arguments);
  return obj;
}
/**

 @memberof module:bug-hunter-js/logs_analyzer_service
 @param vertx {Vertx} 
 @return {LogsAnalyzerService}
 */
LogsAnalyzerService.createExceptionAnalyzerProxy = function(vertx) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(LogsAnalyzerService, JLogsAnalyzerService["createExceptionAnalyzerProxy(io.vertx.core.Vertx)"](vertx._jdel));
  } else throw new TypeError('function invoked with invalid arguments');
};

module.exports = LogsAnalyzerService;