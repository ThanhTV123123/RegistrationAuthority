/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.thread;

import com.google.gson.JsonObject;
import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import vn.ra.process.CommonFunction;

/**
 *
 * @author vanth
 */
public class ThreadCallbackApproved implements Runnable {

    private static final Logger log = Logger.getLogger(ThreadCallbackApproved.class);
    private final String certificateID;
    private final String urlCalllback;
    private final String operationType;
    private final String declineReason;
    private final String requestType;

    public ThreadCallbackApproved(String certificateID, String urlCalllback, String operationType, String declineReason, String requestType) {
        this.certificateID = certificateID;
        this.urlCalllback = urlCalllback;
        this.operationType = operationType;
        this.declineReason = declineReason;
        this.requestType = requestType;
    }

    @Override
    public void run() {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("certificateID", certificateID);
            json.addProperty("operationType", operationType);
            json.addProperty("declineReason", declineReason);
            json.addProperty("requestType", requestType);
            CloseableHttpResponse response;
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(urlCalllback);
                log.info("REQUEST: " + json.toString() + "; URL: " + urlCalllback);
                StringEntity entity = new StringEntity(json.toString());
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                response = client.execute(httpPost);
                client.close();
            }
            log.info("RESPONSE: " + response.toString());
        } catch (IOException ex) {
            CommonFunction.LogExceptionServlet(log, ex.getMessage(), ex);
        }
    }
}
