package com.commercetools.beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.values.PCollection;

public class ReadPubSub {
    public PCollection<PubsubMessage> read(Pipeline pipeline, Options options){
        return pipeline.apply(
                "ReadPubSubSubscription",
                PubsubIO.readMessagesWithAttributes()
                        .fromSubscription(options.getInputSubscription()));
    }
}
