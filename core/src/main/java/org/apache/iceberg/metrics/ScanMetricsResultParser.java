/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.iceberg.metrics;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.apache.iceberg.metrics.ScanReport.ScanMetrics;
import org.apache.iceberg.metrics.ScanReport.ScanMetricsResult;
import org.apache.iceberg.relocated.com.google.common.base.Preconditions;
import org.apache.iceberg.util.JsonUtil;

class ScanMetricsResultParser {
  private ScanMetricsResultParser() {}

  static String toJson(ScanMetricsResult metrics) {
    return toJson(metrics, false);
  }

  static String toJson(ScanMetricsResult metrics, boolean pretty) {
    return JsonUtil.generate(gen -> toJson(metrics, gen), pretty);
  }

  static void toJson(ScanMetricsResult metrics, JsonGenerator gen) throws IOException {
    Preconditions.checkArgument(null != metrics, "Invalid scan metrics: null");

    gen.writeStartObject();

    if (null != metrics.totalPlanningDuration()) {
      gen.writeFieldName(ScanMetrics.TOTAL_PLANNING_DURATION);
      TimerResultParser.toJson(metrics.totalPlanningDuration(), gen);
    }

    if (null != metrics.resultDataFiles()) {
      gen.writeFieldName(ScanMetrics.RESULT_DATA_FILES);
      CounterResultParser.toJson(metrics.resultDataFiles(), gen);
    }

    if (null != metrics.resultDeleteFiles()) {
      gen.writeFieldName(ScanMetrics.RESULT_DELETE_FILES);
      CounterResultParser.toJson(metrics.resultDeleteFiles(), gen);
    }

    if (null != metrics.totalDataManifests()) {
      gen.writeFieldName(ScanMetrics.TOTAL_DATA_MANIFESTS);
      CounterResultParser.toJson(metrics.totalDataManifests(), gen);
    }

    if (null != metrics.totalDeleteManifests()) {
      gen.writeFieldName(ScanMetrics.TOTAL_DELETE_MANIFESTS);
      CounterResultParser.toJson(metrics.totalDeleteManifests(), gen);
    }

    if (null != metrics.scannedDataManifests()) {
      gen.writeFieldName(ScanMetrics.SCANNED_DATA_MANIFESTS);
      CounterResultParser.toJson(metrics.scannedDataManifests(), gen);
    }

    if (null != metrics.skippedDataManifests()) {
      gen.writeFieldName(ScanMetrics.SKIPPED_DATA_MANIFESTS);
      CounterResultParser.toJson(metrics.skippedDataManifests(), gen);
    }

    if (null != metrics.totalFileSizeInBytes()) {
      gen.writeFieldName(ScanMetrics.TOTAL_FILE_SIZE_IN_BYTES);
      CounterResultParser.toJson(metrics.totalFileSizeInBytes(), gen);
    }

    if (null != metrics.totalDeleteFileSizeInBytes()) {
      gen.writeFieldName(ScanMetrics.TOTAL_DELETE_FILE_SIZE_IN_BYTES);
      CounterResultParser.toJson(metrics.totalDeleteFileSizeInBytes(), gen);
    }

    if (null != metrics.skippedDataFiles()) {
      gen.writeFieldName(ScanMetrics.SKIPPED_DATA_FILES);
      CounterResultParser.toJson(metrics.skippedDataFiles(), gen);
    }

    if (null != metrics.skippedDeleteFiles()) {
      gen.writeFieldName(ScanMetrics.SKIPPED_DELETE_FILES);
      CounterResultParser.toJson(metrics.skippedDeleteFiles(), gen);
    }

    gen.writeEndObject();
  }

  static ScanMetricsResult fromJson(String json) {
    return JsonUtil.parse(json, ScanMetricsResultParser::fromJson);
  }

  static ScanMetricsResult fromJson(JsonNode json) {
    Preconditions.checkArgument(null != json, "Cannot parse scan metrics from null object");
    Preconditions.checkArgument(
        json.isObject(), "Cannot parse scan metrics from non-object: %s", json);

    return ImmutableScanMetricsResult.builder()
        .totalPlanningDuration(
            TimerResultParser.fromJson(ScanMetrics.TOTAL_PLANNING_DURATION, json))
        .resultDataFiles(CounterResultParser.fromJson(ScanMetrics.RESULT_DATA_FILES, json))
        .resultDeleteFiles(CounterResultParser.fromJson(ScanMetrics.RESULT_DELETE_FILES, json))
        .totalDataManifests(CounterResultParser.fromJson(ScanMetrics.TOTAL_DATA_MANIFESTS, json))
        .totalDeleteManifests(
            CounterResultParser.fromJson(ScanMetrics.TOTAL_DELETE_MANIFESTS, json))
        .scannedDataManifests(
            CounterResultParser.fromJson(ScanMetrics.SCANNED_DATA_MANIFESTS, json))
        .skippedDataManifests(
            CounterResultParser.fromJson(ScanMetrics.SKIPPED_DATA_MANIFESTS, json))
        .totalFileSizeInBytes(
            CounterResultParser.fromJson(ScanMetrics.TOTAL_FILE_SIZE_IN_BYTES, json))
        .totalDeleteFileSizeInBytes(
            CounterResultParser.fromJson(ScanMetrics.TOTAL_DELETE_FILE_SIZE_IN_BYTES, json))
        .skippedDataFiles(CounterResultParser.fromJson(ScanMetrics.SKIPPED_DATA_FILES, json))
        .skippedDeleteFiles(CounterResultParser.fromJson(ScanMetrics.SKIPPED_DELETE_FILES, json))
        .build();
  }
}
