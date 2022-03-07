package com.commercetools.beam;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.ValueProvider;

public interface Options extends PipelineOptions {

    @Description("Big query output table")
    ValueProvider<String> getOutputTableSpec();

    void setOutputTableSpec(ValueProvider<String> value);

    @Description("Pub/sub subscriber to consume data from")
    ValueProvider<String> getInputSubscription();

    void setInputSubscription(ValueProvider<String> value);

    @Description("GCS bucket name to write data")
    ValueProvider<String> getGcsBucketName();

    void setGcsBucketName(ValueProvider<String> value);
}
