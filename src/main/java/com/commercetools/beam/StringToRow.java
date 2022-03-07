package com.commercetools.beam;

import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.transforms.DoFn;
import org.joda.time.DateTime;
import org.json.JSONObject;

public class StringToRow extends DoFn<String, TableRow> {
    /**
     * Extract data from json string and construct TableRow
     * @param context context
     */
    @ProcessElement
    public void processElement(ProcessContext context) {
        String jsonString = context.element();
        TableRow row = new TableRow();

        String url = "null";
        String method = "null";
        int status = -1;
        String logType = "null";
        String region = "null";
        Long receivedTime = 0L;
        if (jsonString != null)
        {
            JSONObject obj = null;
            try {
                obj = new JSONObject(jsonString);
            }
            catch (Exception e) {
                obj = null;
            }
            url = getUrl(obj);
            region = getRegion(obj);
            method = getMethod(obj);
            status = getStatus(obj);
            logType = getLogType(obj);
            receivedTime = getReceivedTime(obj);
        }
        row.put("url", url);
        row.put("region", region);
        row.put("method", method);
        row.put("status", status);
        row.put("log_type", logType);
        row.put("received_time", receivedTime);
        context.output(row);
    }

    public String getUrl(JSONObject obj) {
        try {
            return obj.getJSONObject("jsonPayload").getString("http_uri");
        } catch (Exception e) {
            return "null";
        }
    }
    public String getRegion(JSONObject obj) {
        try {
            return obj.getJSONObject("jsonPayload").getJSONObject("fields").getString("region");
        } catch (Exception e) {
            return "null";
        }
    }
    public String getMethod(JSONObject obj) {
        try {
            return obj.getJSONObject("jsonPayload").getString("http_method");
        } catch (Exception e) {
            return "null";
        }
    }
    public int getStatus(JSONObject obj) {
        try {
            return obj.getJSONObject("jsonPayload").getInt("http_status");
        } catch (Exception e) {
            return -1;
        }
    }
    public String getLogType(JSONObject obj) {
        try {
            return obj.getString("severity");
        } catch (Exception e) {
            return "null";
        }
    }
    public Long getReceivedTime(JSONObject obj) {
        try {
            return DateTime.parse(obj.getString("receiveTimestamp")).getMillis();
        } catch (Exception e) {
            return 0L;
        }
    }
}
