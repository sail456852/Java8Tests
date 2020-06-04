/**
 * Copyright 2015 StreamSets Inc.
 * <p>
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package stage.processor.sample;

import com.amazonaws.util.json.Jackson;
import com.example.stage.lib.sample.Errors;
import com.fasterxml.jackson.databind.JsonNode;
import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.base.SingleLaneRecordProcessor;
import geohash.GeoHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpUtil;

import java.util.List;
import java.util.Objects;

public abstract class SampleProcessor extends SingleLaneRecordProcessor {
    private static final Logger logger = LoggerFactory.getLogger(SampleProcessor.class);

    /**
     * Gives access to the UI configuration of the stage provided by the {@link SampleDProcessor} class.
     */
    public abstract String getConfig();

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ConfigIssue> init() {
        // Validate configuration values and open any required resources.
        List<ConfigIssue> issues = super.init();

        if (getConfig().equals("invalidValue")) {
            issues.add(
                    getContext().createConfigIssue(
                            Groups.SAMPLE.name(), "config", Errors.SAMPLE_00, "Here's what's wrong..."
                    )
            );
        }

        // If issues is not empty, the UI will inform the user of each configuration issue in the list.
        return issues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        // Clean up any open resources.
        super.destroy();
    }

    private String url = "https://api.opencagedata.com/geocode/v1/geojson?key=40c5004960f54155bf56be289a792da8&q=";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void process(Record record, SingleLaneBatchMaker batchMaker) throws StageException {
        logger.info("Input record: {}", record);

        try {
            Field latitudeField = record.get("/Latitude");
            Field longitudeField = record.get("/Longitude");
            if (!Objects.isNull(latitudeField) && !Objects.isNull(longitudeField)) {
                double Latitude = latitudeField.getValueAsDouble();
                double Longitude = longitudeField.getValueAsDouble();
                GeoHash hash = GeoHash.withCharacterPrecision(Latitude, Longitude, 5);
                record.set("/extraColumn", Field.create(hash.toBase32()));
            } else { //
                Field countryF = record.get("/Country");
                Field cityF = record.get("/City");
                Field nameF = record.get("/Name");
                String country = countryF.getValueAsString();
                String city = cityF.getValueAsString();
                String name = nameF.getValueAsString();
                String q = country + "  " + city + "  " + name;
                url += q;
                logger.info(url);

                url = url.replaceAll("\\s+", "%");
                logger.info(url);
                String s = HttpUtil.doGet(url, 5000);

                JsonNode features = Jackson.jsonNodeOf(s).get("features");
                if (features != null) {
                    JsonNode feature = features.get(0);
                    JsonNode geometry = feature.get("geometry");
                    JsonNode coordinates = geometry.get("coordinates");
                    JsonNode longitude = coordinates.get(0);
                    logger.info(longitude + " ");
                    JsonNode latitude = coordinates.get(1);
                    logger.info(latitude + " ");
                    // set the result coordinates into record stream
                    record.set("/Latitude", Field.create(latitude.doubleValue()));
                    record.set("/Longitude", Field.create(longitude.doubleValue()));
                    GeoHash hash = GeoHash.withCharacterPrecision(latitude.doubleValue(), longitude.doubleValue(), 5);
                    record.set("/extraColumn", Field.create(hash.toBase32()));
                }else{
                    record.set("/extraColumn", Field.create("N/A"));
                }
            }
        } catch (Exception e) {
            record.set("/extraColumn", Field.create("N/A"));
        }

        logger.info("Output record: {}", record);

        batchMaker.addRecord(record);
    }

}