/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author THANH-PC
 */
public class ConnectFileToPartner {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ConnectFileToPartner.class.getName());

    public static CloseableHttpResponse loadFileParner(String IP_CONNECT, String HTTP_CONNECT,
            String CONTEXT_CONNECT, int PORT_CONNECT, String DEFAULT_USER, String DEFAULT_PASS,
            String OWNERCODE_CONNECT, String APPCODE_CONNECT, String FUNCTION_CONNECT, String sUUID) {
        CloseableHttpResponse tempName = null;
        try {
            CommonFunction.LogDebugString(log, "loadFileParner", "IP_CONNECT: " + IP_CONNECT + "; HTTP_CONNECT: "
                    + "; CONTEXT_CONNECT: " + CONTEXT_CONNECT + "; PORT_CONNECT: " + PORT_CONNECT
                    + "; DEFAULT_USER: " + DEFAULT_USER + "; DEFAULT_PASS: " + DEFAULT_PASS
                    + "; OWNERCODE_CONNECT: " + OWNERCODE_CONNECT + "; APPCODE_CONNECT: " + APPCODE_CONNECT
                    + "; FUNCTION_CONNECT: " + FUNCTION_CONNECT + "; sUUID: " + sUUID);
            CloseableHttpClient httpclient = HttpClientBuilder.create().build();

            HttpHost targetHost = new HttpHost(IP_CONNECT, PORT_CONNECT, HTTP_CONNECT);
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                    new UsernamePasswordCredentials(DEFAULT_USER, DEFAULT_PASS));
            AuthCache authCache = new BasicAuthCache();
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(targetHost, basicAuth);
            HttpClientContext context = HttpClientContext.create();
            context.setCredentialsProvider(credsProvider);
            context.setAuthCache(authCache);
            HttpPost httppost = new HttpPost(HTTP_CONNECT + "://" + IP_CONNECT + ":" + PORT_CONNECT + CONTEXT_CONNECT);
            List<NameValuePair> arguments = new ArrayList<>(4);
            arguments.add(new BasicNameValuePair("function", FUNCTION_CONNECT));
            arguments.add(new BasicNameValuePair("stream_id", sUUID));
            arguments.add(new BasicNameValuePair("ownerCode", OWNERCODE_CONNECT));
            arguments.add(new BasicNameValuePair("appCode", APPCODE_CONNECT));
            httppost.setEntity(new UrlEncodedFormEntity(arguments));
            tempName = httpclient.execute(targetHost, httppost, context);
            CommonFunction.LogDebugString(log, "loadFileParner", "Result: " + tempName.getEntity());
        } catch (IOException e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return tempName;
    }

    public static CloseableHttpResponse upFileParner(String IP_CONNECT, String HTTP_CONNECT,
            String CONTEXT_CONNECT, int PORT_CONNECT, String DEFAULT_USER, String DEFAULT_PASS,
            String OWNERCODE_CONNECT, String APPCODE_CONNECT, String FUNCTION_CONNECT, String sUUID,
            String sFileName, String sFileData) {
        CloseableHttpResponse tempName = null;
        try {
            if(!"".equals(sFileName)) {
                sFileName = CommonFunction.clearUnicodeFontString(sFileName);
            }
            CommonFunction.LogDebugString(log, "upFileParner", "IP_CONNECT: " + IP_CONNECT + "; HTTP_CONNECT: "
                    + "; CONTEXT_CONNECT: " + CONTEXT_CONNECT + "; PORT_CONNECT: " + PORT_CONNECT
                    + "; DEFAULT_USER: " + DEFAULT_USER + "; DEFAULT_PASS: " + DEFAULT_PASS
                    + "; OWNERCODE_CONNECT: " + OWNERCODE_CONNECT + "; APPCODE_CONNECT: " + APPCODE_CONNECT
                    + "; FUNCTION_CONNECT: " + FUNCTION_CONNECT + "; sUUID: " + sUUID
                    + "; sFileName: " + sFileName + "; sFileData Size: " + sFileData.length());
            CloseableHttpClient httpclient = HttpClientBuilder.create().build();
            HttpHost targetHost = new HttpHost(IP_CONNECT, PORT_CONNECT, HTTP_CONNECT);
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                    new UsernamePasswordCredentials(DEFAULT_USER, DEFAULT_PASS));
            AuthCache authCache = new BasicAuthCache();
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(targetHost, basicAuth);
            HttpClientContext context = HttpClientContext.create();
            context.setCredentialsProvider(credsProvider);
            context.setAuthCache(authCache);
            HttpPost httppost = new HttpPost(HTTP_CONNECT + "://" + IP_CONNECT + ":" + PORT_CONNECT + CONTEXT_CONNECT);
            List<NameValuePair> arguments = new ArrayList<>(4);
            arguments.add(new BasicNameValuePair("function", FUNCTION_CONNECT));
            arguments.add(new BasicNameValuePair("pk_record", sUUID));
            arguments.add(new BasicNameValuePair("ownerCode", OWNERCODE_CONNECT));
            arguments.add(new BasicNameValuePair("appCode", APPCODE_CONNECT));
            arguments.add(new BasicNameValuePair("1", sFileData));
            arguments.add(new BasicNameValuePair("stagfilelist", "1"));
            arguments.add(new BasicNameValuePair("sfileNamelist", sFileName));
            httppost.setEntity(new UrlEncodedFormEntity(arguments));
            tempName = httpclient.execute(targetHost, httppost, context);
            CommonFunction.LogDebugString(log, "upFileParner", "Result: " + tempName.getEntity());
        } catch (IOException e) {
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }
        return tempName;
    }
}
