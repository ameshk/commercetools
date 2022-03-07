package com.commercetools.beam;

import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.values.PCollection;

public class WriteBigQuery {

    /**
     * Write data to big query
     * @param bqData data to write to bigquery
     * @param options beam pipeline options for configuration
     */
    public void write(PCollection<TableRow> bqData, Options options) {

        bqData.apply("Write to BQ",
                BigQueryIO
                        .writeTableRows()
                        .withoutValidation()
                        .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                        .withMethod(BigQueryIO.Write.Method.STREAMING_INSERTS)
                        .to(options.getOutputTableSpec()));
    }
}
