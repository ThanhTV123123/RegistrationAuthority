/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import vn.ra.object.ServiceRequestExpand;
import vn.ra.restful.JWTProcess;
import vn.ra.thread.ThreadCallbackApproved;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author USER
 */
public class TestREST {

    public static void main(String[] args) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String sExpandParam = "{\"representative\":\"Nguyen A\",\"representativePosition\":\"GD\"}";
//        ServiceRequestExpand jsonReqExpand = objectMapper.readValue(sExpandParam, ServiceRequestExpand.class);
//        String srepresentativePosition = EscapeUtils.CheckTextNull(jsonReqExpand.representativePosition);
//        String srepresentative = EscapeUtils.CheckTextNull(jsonReqExpand.representative);
//        System.out.println("srepresentativePosition: " + srepresentativePosition);
//        System.out.println("srepresentative: " + srepresentative);
        
        /*String urlCallback = "http://uat.crm.vinca.vn/rest/s1/vinca/vinca_callback_test";
        JsonObject json = new JsonObject();
        json.addProperty("certId", 10);
        json.addProperty("operationType", "DECLINED");
        json.addProperty("declineReason", "Lý do từ chối");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(urlCallback);
        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        client.close();*/
        
        String urlCallback = "http://updater.3asoft.vn/webhook/WebhookReceiver003.ashx";
        JsonObject json = new JsonObject();
        json.addProperty("functionType", "1");
        json.addProperty("tokenSN", "1234132138743");
        json.addProperty("certificateID", 10);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try{
            System.out.println(json.toString());
            HttpPost request = new HttpPost(urlCallback);
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            httpClient.execute(request);
            httpClient.close();
        } catch (Exception e){
            
        }

    }
}
