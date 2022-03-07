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
     * @param options PipelineOptions
     * @return results
     */
    public static PipelineResult run(Options options) {

        TupleTag<String> gcsTag = new TupleTag<>();
        TupleTag<TableRow> bqTag = new TupleTag<>();

        Pipeline pipeline = Pipeline.create(options);

        PCollection<PubsubMessage> messages = new ReadPubSub().read(pipeline, options);

        PCollectionTuple resultData = messages.apply("extract data from json", new LogDataProcessor(gcsTag, bqTag));

        new WriteBigQuery().write(resultData.get(bqTag), options);

        new WriteGCS().write(resultData.get(gcsTag), options);

        return pipeline.run();
    }
}
