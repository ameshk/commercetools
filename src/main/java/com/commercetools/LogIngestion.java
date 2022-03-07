package com.commercetools;


import com.commercetools.beam.Options;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

import static com.commercetools.beam.LogPipeline.run;

public class LogIngestion {
    /**
     * This ia the main entry point for pipeline execution
     * It will read the pipeline options - arguments and run a beam pipeline
     * @param args The commandline arguments passed
     */
    public static void main(String[] args) {
        Options options = PipelineOptionsFactory
                .fromArgs(args)
                .withValidation()
                .as(Options.class);

        run(options).waitUntilFinish();
    }
}
