package com.commercetools;

import com.commercetools.beam.MessageToString;
import com.google.common.collect.ImmutableMap;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestMessageToString {
    @Test
    public void testMessageToString() {
        byte[] payload = "Laces out \nDan!".getBytes();
        Map<String, String> attributes = ImmutableMap.of("id", "Ace");
        PubsubMessage message = new PubsubMessage(payload, attributes);

        Pipeline p = TestPipeline.create().enableAbandonedNodeEnforcement(false);

        PCollection<PubsubMessage> input = p.apply(Create.of(List.of(message)));
        PCollection<String> output = input.apply(new PTransform<PCollection<PubsubMessage>, PCollection<String>>() {
            @Override
            public PCollection<String> expand(PCollection<PubsubMessage> input) {
                return input.apply(ParDo.of(new MessageToString()));
            }
        });

        PAssert.that(output).containsInAnyOrder(
                "Laces out Dan!"
        );
        p.run();
    }
}
