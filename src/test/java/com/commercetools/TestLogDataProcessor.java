package com.commercetools;

import com.commercetools.beam.LogDataProcessor;
import com.google.api.services.bigquery.model.TableRow;
import com.google.common.collect.ImmutableMap;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestLogDataProcessor {
    private String json = "";

    @Before
    public void setUp() throws Exception {
        try (FileReader fr = new FileReader("src/test/resources/test_data.json");)
        {
            int i;
            while ((i = fr.read()) != -1)
                json = json.concat(String.valueOf((char)i));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testLogDataProcessor() {
        TupleTag<String> gcsTag = new TupleTag<>();
        TupleTag<TableRow> bqTag = new TupleTag<>();


        Map<String, String> attributes = ImmutableMap.of("id", "Ace");

        byte[] payload = "Laces out \nDan!".getBytes();
        PubsubMessage message = new PubsubMessage(payload, attributes);

        byte[] payload1 = json.getBytes();
        PubsubMessage message1 = new PubsubMessage(payload1, attributes);

        List<PubsubMessage> test_data = new ArrayList<>();
        test_data.add(message);
        test_data.add(message1);

        Pipeline p = TestPipeline.create().enableAbandonedNodeEnforcement(false);

        PCollection<PubsubMessage> input = p.apply(Create.of(test_data));

        PCollectionTuple actual = input.apply(new LogDataProcessor(gcsTag, bqTag));

        PAssert.that(actual.get(gcsTag)).containsInAnyOrder(
                "Laces out Dan!",
                json.replaceAll("\n", "")
        );

        PAssert.that(actual.get(bqTag)).containsInAnyOrder(
                new TableRow().set("url", "http://my-domain.example.com/search")
                        .set("region", "EU")
                        .set("method", "GET")
                        .set("status", 200)
                        .set("log_type", "INFO")
                        .set("received_time", DateTime.parse("01-01-01T12:12:12.121221212Z").getMillis()),
                new TableRow().set("url", "null")
                        .set("region", "null")
                        .set("method", "null")
                        .set("status", -1)
                        .set("log_type", "null")
                        .set("received_time", 0L)
        );
        p.run();
    }
}
