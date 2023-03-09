/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
import org.apache.log4j.Logger;
import vn.ra.synch.neac.EFRESPONSE_DATA;
import vn.ra.synch.neac.NERESPONSE_DATA;

/**
 *
 * @author USER
 */
public class HttpUtils {

    final static Logger LOGGER = Logger.getLogger(HttpUtils.class);
    
    // HTTP Post request
    public static HttpResponse postNEACRequestAdd(String sRequestData, String sSourceNEAC, String url, int time)
        throws JsonProcessingException, IOException, NoSuchAlgorithmException, ParseException, Exception {
        SSLUtilities.trustAllHttpsCertificates();
        SSLUtilities.trustAllHostnames();
        URL object=new URL(url);
        HttpURLConnection con = (HttpURLConnection) object.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestMethod("POST");
        con.setConnectTimeout(time);
        try (BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"))) {
            wr.write(sRequestData);
            wr.flush();
        }
        StringBuilder sb = new StringBuilder();
        int HttpResult = con.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");  
                }
            }
//            System.out.println("" + sb.toString());
            ObjectMapper oMapperParse = new ObjectMapper();
            if(sSourceNEAC.equals(Definitions.CONFIG_SYNCH_NEAC_SOURCE_EFY)) {
                EFRESPONSE_DATA rsRes = oMapperParse.readValue(sb.toString(), EFRESPONSE_DATA.class);
                HttpResponse httpRes = new HttpResponse();
                httpRes.setStatus(rsRes.status);
                if(rsRes.code == 200) {
                    httpRes.setCode(1);
                } else {
                    httpRes.setCode(rsRes.code);
                }
                httpRes.setDescription(rsRes.message);
                httpRes.setMsg(sb.toString());
                return httpRes;
            } else {
                System.out.println("LINK1: " + url);
                NERESPONSE_DATA rsRes = oMapperParse.readValue(sb.toString(), NERESPONSE_DATA.class);
                HttpResponse httpRes = new HttpResponse();
                httpRes.setStatus(rsRes.Data);
                httpRes.setCode(rsRes.Status);
                httpRes.setDescription(rsRes.Message);
                httpRes.setMsg(sb.toString());
                return httpRes;
            }
        } else {
            System.out.println("LINK2: " + url);
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String sResData = "";
            String strCurrentLine;
            while ((strCurrentLine = br.readLine()) != null) {
                sResData = strCurrentLine;
            }
            HttpResponse httpRes = new HttpResponse();
            httpRes.setStatus(false);
            httpRes.setCode(HttpResult);
            httpRes.setDescription(con.getResponseMessage());
            httpRes.setMsg(sResData);
            return httpRes;
        }
    }
    
    // HTTP Post request
    public static HttpResponse postNEACRequestRevoke(String sRequestData, String sSourceNEAC, String urlWS, int time, String nameFile, String urlFile)
        throws JsonProcessingException, IOException, NoSuchAlgorithmException, ParseException, Exception {
        SSLUtilities.trustAllHttpsCertificates();
        SSLUtilities.trustAllHostnames();
        if(sSourceNEAC.equals(Definitions.CONFIG_SYNCH_NEAC_SOURCE_EFY)) {
            URL object=new URL(urlWS);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            con.setConnectTimeout(time);
            try (BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"))) {
                wr.write(sRequestData);
                wr.flush();
            }
            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");  
                    }
                }
                System.out.println("" + sb.toString()); 
                ObjectMapper oMapperParse = new ObjectMapper();
                EFRESPONSE_DATA rsRes = oMapperParse.readValue(sb.toString(), EFRESPONSE_DATA.class);
                HttpResponse httpRes = new HttpResponse();
                httpRes.setStatus(rsRes.status);
                if(rsRes.status == true){
                    httpRes.setCode(1);
                } else {
                    httpRes.setCode(rsRes.code);
                }
                httpRes.setDescription(rsRes.message);
                httpRes.setMsg(sb.toString());
                return httpRes;
            } else {
                BufferedReader br;
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String sResData = "";
                String strCurrentLine;
                while ((strCurrentLine = br.readLine()) != null) {
                    sResData = strCurrentLine;
                }
                HttpResponse httpRes = new HttpResponse();
                httpRes.setStatus(false);
                httpRes.setCode(HttpResult);
                httpRes.setDescription(con.getResponseMessage());
                httpRes.setMsg(sResData);
                return httpRes;
            }
        } else {
            URL object=new URL(urlWS);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            con.setConnectTimeout(time);
            try (BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"))) {
                wr.write(sRequestData);
                wr.flush();
            }
            StringBuilder sb = new StringBuilder();
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");  
                    }
                }
                ObjectMapper oMapperParse = new ObjectMapper();
                NERESPONSE_DATA rsRes = oMapperParse.readValue(sb.toString(), NERESPONSE_DATA.class);
                HttpResponse httpRes = new HttpResponse();
                httpRes.setStatus(rsRes.Data);
                httpRes.setCode(rsRes.Status);
                httpRes.setDescription(rsRes.Message);
                httpRes.setMsg(sb.toString());
                return httpRes;
            } else {
                BufferedReader br;
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String sResData = "";
                String strCurrentLine;
                while ((strCurrentLine = br.readLine()) != null) {
                    sResData = strCurrentLine;
                }
                HttpResponse httpRes = new HttpResponse();
                httpRes.setStatus(false);
                httpRes.setCode(HttpResult);
                httpRes.setDescription(con.getResponseMessage());
                httpRes.setMsg(sResData);
                return httpRes;
            }

//            Map<String, String> headers = new HashMap<>();
//            HttpPostMultipart multipart = new HttpPostMultipart(urlWS + "?" + sRequestData, "utf-8", headers);
//            multipart.addFilePart(nameFile, new File(urlFile));
//            String response = multipart.finish();
//            LOGGER.info("response call NEAC Revoke: " + response);
//            ObjectMapper oMapperParse = new ObjectMapper();
//            NERESPONSE_DATA rsRes = oMapperParse.readValue(response, NERESPONSE_DATA.class);
//            HttpResponse httpRes = new HttpResponse();
//            httpRes.setStatus(rsRes.Data);
//            httpRes.setCode(rsRes.Status);
//            httpRes.setDescription(rsRes.Message);
//            httpRes.setMsg(response);
//            return httpRes;
                
//            URL object=new URL(urlWS + "?" + sRequestData);
//            HttpURLConnection con = (HttpURLConnection) object.openConnection();
//            con.setDoOutput(true);
//            con.setDoInput(true);
//            con.setRequestProperty("Content-Type", "multipart/form-data");
//            con.setRequestProperty("Accept", "application/json");
//            con.setRequestMethod("POST");
//            con.setConnectTimeout(time);
//            try (BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"))) {
//                wr.write(sRequestData);
//                wr.flush();
//            }
//            StringBuilder sb = new StringBuilder();
//            int HttpResult = con.getResponseCode();
//            if (HttpResult == HttpURLConnection.HTTP_OK) {
//                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        sb.append(line).append("\n");  
//                    }
//                }
//                System.out.println("" + sb.toString()); 
//                ObjectMapper oMapperParse = new ObjectMapper();
//                NERESPONSE_DATA rsRes = oMapperParse.readValue(sb.toString(), NERESPONSE_DATA.class);
//                HttpResponse httpRes = new HttpResponse();
//                httpRes.setStatus(rsRes.Data);
//                httpRes.setCode(rsRes.Status);
//                httpRes.setDescription(rsRes.Message);
//                httpRes.setMsg(sb.toString());
//                return httpRes;
//            } else {
//                BufferedReader br;
//                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
//                String sResData = "";
//                String strCurrentLine;
//                while ((strCurrentLine = br.readLine()) != null) {
//                    sResData = strCurrentLine;
//                }
//                HttpResponse httpRes = new HttpResponse();
//                httpRes.setStatus(false);
//                httpRes.setCode(HttpResult);
//                httpRes.setDescription(con.getResponseMessage());
//                httpRes.setMsg(sResData);
//                return httpRes;
//            }
        }
        
    }

    public static HttpResponse invokeHttpRequest(URL endpointUrl,
            String httpMethod,
            int timeout,
            Map<String, String> headers,
            String requestBody) {
        LOGGER.debug("payload: {}: " + requestBody);
        HttpURLConnection connection = createHttpConnection(endpointUrl, httpMethod, timeout, headers);
        try {
            if (requestBody != null) {
                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                //DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.write(requestBody);
                wr.flush();
                wr.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Request failed. " + e.getMessage(), e);
        }
        return executeHttpRequest(connection);
    }

    public static HttpResponse executeHttpRequest(HttpURLConnection connection) {
        try {
            HttpResponse response = new HttpResponse();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                LOGGER.debug("<<< Message: {}", connection.getResponseMessage());
                response.setStatus(false);
                response.setMsg(connection.getResponseMessage());
                return response;
            }
            InputStream is;
            try {
                is = connection.getInputStream();
                response.setStatus(true);
            } catch (IOException e) {
                is = connection.getErrorStream();
                response.setStatus(false);
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer msg = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                msg.append(line);
                msg.append('\r');
            }
            rd.close();
            LOGGER.debug("<<< Message: {}: " + msg.toString());
            response.setMsg(msg.toString());
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Request failed. " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static HttpURLConnection createHttpConnection(URL endpointUrl,
            String httpMethod,
            int timeout,
            Map<String, String> headers) {
        try {
            HttpURLConnection connection = (HttpURLConnection) endpointUrl.openConnection();
            connection.setRequestMethod(httpMethod);

            if (headers != null) {
                LOGGER.debug("**************** Restful Request headers ****************");
                for (String headerKey : headers.keySet()) {
                    LOGGER.debug(headerKey + ": " + headers.get(headerKey));
                    connection.setRequestProperty(headerKey, headers.get(headerKey));
                }
            }
            connection.setUseCaches(true);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create connection. " + e.getMessage(), e);
        }
    }

    public static HttpResponse invokeHttpRequest(String[] truststore, String endpointUrl,
            String httpMethod,
            int timeout,
            Map<String, String> headers,
            String requestBody) {

        HttpURLConnection connection = null;
        try {
            URL url = new URL(endpointUrl);
            connection = createHttpConnection(url, httpMethod, timeout, headers);
            if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection https = (HttpsURLConnection) connection;
                //setupSSL(https, truststore);
            }
            connection.connect();
//            connection.setInstanceFollowRedirects(true); 
            HttpURLConnection.setFollowRedirects(true);

            if (requestBody != null) {
                //DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                try (BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"))) {
                    LOGGER.debug(">>> SEND: {}: " + requestBody);
                    wr.write(requestBody);
                    wr.flush();
                }
            }
            HttpResponse response = new HttpResponse();
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                    || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
//                URL redirectUrl = new URL(connection.getHeaderField("Location"));
                return invokeHttpRequest(truststore, connection.getHeaderField("Location"), httpMethod, timeout, headers, requestBody);
            }
            InputStream is;

            if (responseCode > 299) {
                is = connection.getErrorStream();
                response.setStatus(false);
                if (is == null) {
                    String responseMsg = connection.getResponseMessage();
                    LOGGER.debug("<<< RECEIVE: {}: " + responseMsg);
                    response.setMsg(responseMsg);
                    return response;
                }
            } else if (responseCode != HttpURLConnection.HTTP_OK) {
                String responseMsg = connection.getResponseMessage();
                LOGGER.debug("<<< RECEIVE: {}: " + responseMsg);
                response.setMsg(responseMsg);
                response.setStatus(false);
                return response;
            } else {
                try {
                    is = connection.getInputStream();
                    response.setStatus(true);
                } catch (IOException ex) {
                    LOGGER.debug("Error when read inputstream, caused by", ex);
                    is = connection.getErrorStream();
                    response.setStatus(false);
                }
            }

            StringBuilder msg = new StringBuilder();
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                String line;
                while ((line = rd.readLine()) != null) {
                    msg.append(line);
                    msg.append('\r');
//                    msg.append(System.lineSeparator());
                }
            }
            LOGGER.debug("<<< RECEIVE: {}: " + msg.toString());
            response.setMsg(msg.toString());
            return response;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Request failed. " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static HttpResponse invokeHttpRequest(String endpointUrl,
            String httpMethod,
            int timeout,
            Map<String, String> headers,
            String requestBody) {

        HttpURLConnection connection = null;
        try {
            URL url = new URL(endpointUrl);
            connection = createHttpConnection(url, httpMethod, timeout, headers);
            if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection https = (HttpsURLConnection) connection;
                //setupSSL(https);
            }

            if (requestBody != null) {
                //DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                try (BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"))) {
                    //LOGGER.debug(">>> SEND: {}: " + requestBody);
                    wr.write(requestBody);
                    wr.flush();
                }
            }

            HttpResponse httpRes = new HttpResponse();
            int responseCode = connection.getResponseCode();
//            String responseMsg222 = connection.getResponseMessage();
//            connection.toString()
            System.out.println("A: " + connection.toString());
//            LOGGER.debug("Response Code: " + responseCode);
//            InputStream is;
            
//            int responseCode = con.getResponseCode();
            System.out.println("nSending 'POST' request to URL : " + url);
//            System.out.println("Post Data : " + postJsonData);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
             response.append(output);
            }
            in.close();

            //printing result from response
            System.out.println("Error - json: " + response.toString());
            httpRes.setMsg(response.toString());

//            if (responseCode > 299) {
//                is = connection.getErrorStream();
//                response.setStatus(false);
//                if (is == null) {
//                    String responseMsg = connection.getResponseMessage();
////                    LOGGER.debug("<<< RECEIVE: {}: " + responseMsg);
//                    response.setMsg(responseMsg);
//                    return response;
//                }
//            } else if (responseCode != HttpURLConnection.HTTP_OK) {
//                String responseMsg = connection.getResponseMessage();
////                LOGGER.debug("<<< RECEIVE: {}: " + responseMsg);
//                response.setMsg(responseMsg);
//                response.setStatus(false);
//                return response;
//            } else {
//                try {
//                    is = connection.getInputStream();
//                    response.setStatus(true);
//                } catch (IOException ex) {
////                    LOGGER.debug("Error when read inputstream, caused by", ex);
//                    is = connection.getErrorStream();
//                    response.setStatus(false);
//                }
//            }
//
//            StringBuilder msg = new StringBuilder();
//            try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    msg.append(line);
//                    msg.append('\r');
//                }
//            }
////            LOGGER.debug("<<< RECEIVE: {}: " + msg.toString());
//            System.out.println("aaa:" + msg);
//            response.setMsg(msg.toString());
            return httpRes;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Request failed. " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    static class MySSLSocketFactory extends SSLSocketFactory {

        final private SSLSocketFactory sslSocketFactory;

        public MySSLSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            LOGGER.debug("getDefaultCipherSuites");
            return sslSocketFactory.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            LOGGER.debug("getSupportedCipherSuites");
            return sslSocketFactory.getDefaultCipherSuites();
        }

        @Override
        public Socket createSocket(Socket socket, String string, int i, boolean bln) throws IOException {
            LOGGER.debug("createSocket::: " + string);
            LOGGER.debug("createSocket::: " + i);
            LOGGER.debug("createSocket::: " + bln);
            Socket sk = this.sslSocketFactory.createSocket(socket, string, i, bln);
            if (sk instanceof SSLSocket) {
                ((SSLSocket) sk).setEnabledCipherSuites(new String[]{
                    "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
                    "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
                    "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"
                });

                ((SSLSocket) sk).addHandshakeCompletedListener(new HandshakeCompletedListener() {
                    @Override
                    public void handshakeCompleted(HandshakeCompletedEvent hce) {
                        LOGGER.debug("CipherSuite: " + hce.getCipherSuite());
                        SSLSession session = hce.getSession();

                    }
                });
            }
            return sk;
        }

        @Override
        public Socket createSocket(String string, int i) throws IOException, UnknownHostException {
            LOGGER.debug("createSocket:::: " + string);
            LOGGER.debug("createSocket:::: " + i);
            Socket sk = this.sslSocketFactory.createSocket(string, i);
            if (sk instanceof SSLSocket) {
                ((SSLSocket) sk).setEnabledCipherSuites(new String[]{
                    "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
                    "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
                    "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"
                });

                ((SSLSocket) sk).addHandshakeCompletedListener(new HandshakeCompletedListener() {
                    @Override
                    public void handshakeCompleted(HandshakeCompletedEvent hce) {
                        LOGGER.debug("CipherSuite: " + hce.getCipherSuite());
                    }
                });
                // now do the TLS handshake
                ((SSLSocket) sk).startHandshake();
                SSLSession session = ((SSLSocket) sk).getSession();
                if (session == null) {
                    throw new SSLException("Cannot verify SSL socket without session");
                }

                // verify host name (important!)
                if (!HttpsURLConnection.getDefaultHostnameVerifier().verify(string, session)) {
                    throw new SSLPeerUnverifiedException("Cannot verify hostname: " + string);
                }
            }
            return sk;
        }

        @Override
        public Socket createSocket(String string, int i, InetAddress ia, int i1) throws IOException, UnknownHostException {
            LOGGER.debug("createSocket");
            return this.sslSocketFactory.createSocket(string, i, ia, i1);
        }

        @Override
        public Socket createSocket(InetAddress ia, int i) throws IOException {
            LOGGER.debug("createSocket");
            return this.sslSocketFactory.createSocket(ia, i);
        }

        @Override
        public Socket createSocket(InetAddress ia, int i, InetAddress ia1, int i1) throws IOException {
            LOGGER.debug("createSocket");
            return this.sslSocketFactory.createSocket(ia, i, ia1, i1);
        }
    }

//    private static void setupSSL(HttpsURLConnection conHttps, String[] truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, IOException, FileNotFoundException, CertificateException {
//        // Set our connection to use this SSL context, with the "Trust all" manager in place.
//        SSLContext sslContext = wrapX500TrustManager(truststore);
//        SSLSocketFactory sslSocket = new MySSLSocketFactory(sslContext.getSocketFactory());
//
//        conHttps.setSSLSocketFactory(sslSocket);
//        // and set the hostname verifier.
//        conHttps.setHostnameVerifier((String string, SSLSession ssls) -> {
//            try {
//                LOGGER.debug("------------ HostnameVerifier ------------");
//                LOGGER.debug("\t\t String: " + string);
//                LOGGER.debug("\t\t PeerHost: " + ssls.getPeerHost());
//                LOGGER.debug("\t\t PeerPort: " + ssls.getPeerPort());
//                LOGGER.debug("\t\t CipherSuite: " + ssls.getCipherSuite());
//
//                LOGGER.debug("\t\t PeerPrincipal-Name: " + ssls.getPeerPrincipal().getName());
//                LOGGER.debug("------------ |||||||||||||||| ------------");
//            } catch (SSLPeerUnverifiedException ex) {
//                LOGGER.debug("Error when verify hostname, caused by", ex);
//            }
//            return true;
//        });
//    }
//
//    private static void setupSSL(HttpsURLConnection conHttps) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, IOException, CertificateException {
//        // Set our connection to use this SSL context, with the "Trust all" manager in place.
//        conHttps.setSSLSocketFactory(wrapX500TrustManager(null).getSocketFactory());
//
//        // and set the hostname verifier.
//        conHttps.setHostnameVerifier((String string, SSLSession ssls) -> {
//            try {
//                LOGGER.debug("------------ HostnameVerifier ------------");
//                LOGGER.debug("\t\t String: " + string);
//                LOGGER.debug("\t\t PeerHost: " + ssls.getPeerHost());
//                LOGGER.debug("\t\t PeerPort: " + ssls.getPeerPort());
//                LOGGER.debug("\t\t CipherSuite: " + ssls.getCipherSuite());
//
//                LOGGER.debug("\t\t PeerPrincipal-Name: " + ssls.getPeerPrincipal().getName());
//                LOGGER.debug("------------ |||||||||||||||| ------------");
//            } catch (SSLPeerUnverifiedException ex) {
//                LOGGER.debug("Error when verify hostname, caused by", ex);
//            }
//            return true;
//        });
//    }

    public static SSLContext wrapX500TrustManager(String[] trusted) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, FileNotFoundException, IOException, CertificateException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // Using null here initialises the TMF with the default trust store.
        tmf.init((KeyStore) null);
        // Get hold of the default trust manager
        X509TrustManager x509Tm = null;
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                x509Tm = (X509TrustManager) tm;
                LOGGER.debug("Found X509TrustManager: " + x509Tm.getClass().getName());
                break;
            }
        }
        X509Certificate[] defauls = x509Tm.getAcceptedIssuers();
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        if (defauls != null) {
            for (X509Certificate x509 : defauls) {
                trustStore.setCertificateEntry(x509.getSerialNumber().toString(), x509);
            }
        }

        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        if (trusted != null) {
            for (int i = 0; i < trusted.length; i++) {
                try (InputStream inputStream = new FileInputStream(trusted[i])) {
                    X509Certificate cert = (X509Certificate) certFactory.generateCertificate(inputStream);
                    LOGGER.debug("Add CertificateEntry: " + trusted[i]);
                    trustStore.setCertificateEntry("MyTrust" + i, cert);
                }
            }
        }
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

        return sslContext;
    }
}
