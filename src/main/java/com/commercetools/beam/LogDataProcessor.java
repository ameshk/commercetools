package com.commercetools.beam;

import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;

class LogDataProcessor extends PTransform<PCollection<PubsubMessage>, PCollectionTuple> {
    private TupleTag<String> gcsTag;
    private TupleTag<TableRow> bqTag;

    public LogDataProcessor(TupleTag<String> gcsTag, TupleTag<TableRow> bqTag) {
        this.gcsTag = gcsTag;
        this.bqTag = bqTag;
    }

    @Override
    public PCollectionTuple expand(PCollection<PubsubMessage> input) {
        PCollection<String> stringCollection = input
                .apply("MapToString", ParDo.of(new MessageToString()));

        PCollection<TableRow> rowCollection = stringCollection
                .apply("stringToRow", ParDo.of(new StringToRow()));

        return PCollectionTuple.of(gcsTag, stringCollection).and(bqTag, rowCollection);
    }
}


