package com.commercetools.beam;

import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;

public class LogPipeline {

    /**
     * Create and run a beam pipeline
     * Read data from pub/sub topic
     * Convert data to string and TableRow type
     * write data to GCS and Big Query
     * @param options PipelineOptions
     * @return results
     */
    public static PipelineResult run(Options options) {

        TupleTag<String> gcsTag = new TupleTag<>();
        TupleTag<TableRow> bqTag = new TupleTag<>();

        Pipeline pipeline = Pipeline.create(options);

        // Read pub/sub data
        PCollection<PubsubMessage> messages = new ReadPubSub().read(pipeline, options);

        PCollectionTuple resultData = messages.apply("extract data from json", new LogDataProcessor(gcsTag, bqTag));

        // write TableRow type data to big query
        new WriteBigQuery().write(resultData.get(bqTag), options);

        // write String type data to GCS
        new WriteGCS().write(resultData.get(gcsTag), options);

        return pipeline.run();
    }
}
