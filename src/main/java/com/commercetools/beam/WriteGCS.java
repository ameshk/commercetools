package com.commercetools.beam;

import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.values.PCollection;

public class WriteGCS {
    public void write(PCollection<String> gcsData, Options options) {
        gcsData.apply("WriteGCS", TextIO.write().to(options.getGcsBucketName()));
    }
}
