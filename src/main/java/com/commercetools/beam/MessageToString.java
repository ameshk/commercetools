package com.commercetools.beam;

import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.DoFn;

import java.nio.charset.StandardCharsets;

public class MessageToString extends DoFn<PubsubMessage, String> {
    @ProcessElement
    public void processElement(ProcessContext context) {
        PubsubMessage message = context.element();
        if (message != null) {
            String stringMessage = new String(message.getPayload(), StandardCharsets.UTF_8).replaceAll("\n", "");
            context.output(stringMessage);
        } else {
            context.output("{}");
        }
    }
}
