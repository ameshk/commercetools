package com.commercetools.beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.values.PCollection;

public class ReadPubSub {
    /**
     * Read pub/sub data from pub/sub subscription
     * @param pipeline Beam pipeline object
     * @param options Beam options to read input config
     * @return PCollection<PubsubMessage>
     */
    public PCollection<PubsubMessage> read(Pipeline pipeline, Options options){
        return pipeline.apply(
                "ReadPubSubSubscription",
                PubsubIO.readMessagesWithAttributes()
                        .fromSubscription(options.getInputSubscription()));
    }
}
