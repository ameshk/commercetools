package com.commercetools.beam;

import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.values.PCollection;

public class WriteGCS {
    /**
     * Write data to GCS
     * @param gcsData data to be written to gcs
     * @param options beam pipeline options for configuration
     */
    public void write(PCollection<String> gcsData, Options options) {
        gcsData.apply("WriteGCS", TextIO.write().to(options.getGcsBucketName()));
    }
}
