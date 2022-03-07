package com.commercetools;

import com.commercetools.beam.MessageToString;
import com.commercetools.beam.StringToRow;
import com.google.api.services.bigquery.model.TableRow;
import com.google.common.collect.ImmutableMap;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestLogPipeline {
    private StringToRow strToRow = null;
    private JSONObject obj = null;
    private String json = "";

    @Before
    public void setUp() throws Exception {
        strToRow = new StringToRow();
        try (FileReader fr = new FileReader("src/test/resources/test_data.json");)
        {
            int i;
            while ((i = fr.read()) != -1)
                json = json.concat(String.valueOf((char)i));
            obj = new JSONObject(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testStringToRow() {
        String[] testData = {
                json, "{}"
        };
        Pipeline p = TestPipeline.create().enableAbandonedNodeEnforcement(false);
        PCollection<String> input = p.apply(Create.of(Arrays.asList(testData)));
        PCollection<TableRow> output = input.apply(new PTransform<>() {
            @Override
            public PCollection<TableRow> expand(PCollection<String> input) {
                return input.apply(ParDo.of(new StringToRow()));
            }
        });

        PAssert.that(output).containsInAnyOrder(
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



    @Test
    public void testGetUrl() {
        String actualUrl = strToRow.getUrl(obj);
        Assert.assertEquals("http://my-domain.example.com/search", actualUrl);
    }

    @Test
    public void testGetRegion() {
        String actual = strToRow.getRegion(obj);
        Assert.assertEquals("EU", actual);
    }

    @Test
    public void testGetMethod() {
        String actual = strToRow.getMethod(obj);
        Assert.assertEquals("GET", actual);
    }

    @Test
    public void testGetStatus() {
        int actual = strToRow.getStatus(obj);
        Assert.assertEquals(200, actual);
    }

    @Test
    public void testGetLogType() {
        String actual = strToRow.getLogType(obj);
        Assert.assertEquals("INFO", actual);
    }

    @Test
    public void testGetDateTime() {
        Long actual = strToRow.getReceivedTime(obj);
        Long expected = DateTime.parse("01-01-01T12:12:12.121221212Z").getMillis();
        Assert.assertEquals(expected, actual);
    }
}
