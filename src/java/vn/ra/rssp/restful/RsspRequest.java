/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.rssp.restful;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
/**
 *
 * @author USER
 */
public class RsspRequest implements Serializable {
    public RsspRequest(String URL, String relyingPartyUser, String relyingPartyPassword, String relyingPartySignature,
        InputStream relyingPartyKeyStore, String relyingPartyKeyStorePassword, String profile) {
        this.URL = URL;
        this.relyingPartyUser = relyingPartyUser;
        this.relyingPartyPassword = relyingPartyPassword;
        this.relyingPartySignature = relyingPartySignature;
        this.relyingPartyKeyStore = relyingPartyKeyStore;
        this.relyingPartyKeyStorePassword = relyingPartyKeyStorePassword;
        this.profile = profile;
    }

    private transient final String URL;
    private transient final String relyingPartyUser;
    private transient final String relyingPartyPassword;
    private transient final String relyingPartySignature;
    private transient final InputStream relyingPartyKeyStore;
    private transient final String relyingPartyKeyStorePassword;

    public String relyingParty;
    public String agreementUUID;
    public String authorizeCode;
    public String oldPassphrase;
    public String newPhone;
    public String otpOldPhone;
    public String otpNewPhone;
    public String otpOldEmail;
    public String otpNewEmail;
    public String user;
    public String oldPassword;
    public String ownerUUID;
    public String signatureAlgorithmParams;
    public String createdRP;
    public String newPassphrase;
    public String userType;
    public String relyingPartyBillCode;
    public Boolean rememberMe;
    public String lang;
    public Integer tokenType;
    public String token;
    public String fullname;
    public String credentialID;
    public String certificates;
    public Boolean certInfo;
    public Boolean authInfo;
    public String notificationTemplate;
    public String notificationSubject;
    public String billCode;
    public Integer numSignatures;
    public String notificationTitle;
    public String notificationMessage;
    public ClientInfo clientInfo;
    public String message;
    public String logoURI;
    public String bgImageURI;
    public String rpIconURI;
    public String rpName;
    public String confirmationPolicy;
    public Integer expirationDuration;
    public Boolean vcEnabled;
    public Boolean acEnabled;
    public Integer messagingMode;
    public String SAD;
    public SearchConditions searchConditions;
    public Integer ownerID;
    public transient String bearer;
    public String newPassword;
    public String username;
    public transient String password;
    public String requestType;
    public String newEmail;
    public TseNotification tseNotification;
    public String profile;
    public String messageCaption;
    public String operationMode;
    public String scaIdentity;
    public String responseURI;
    public Integer validityPeriod;
    public String[] documents;
    public String signAlgo;
    public String signAlgoParams;
    public String signatureFormat;
    public String conformanceLevel;
    public String signedEnvelopeProperty;
    public String email;
    public String phone;
    public String identificationType;
    public String identification;
    public Boolean registerTSEEnabled;
    public DocumentDigests documentDigests;

    public RsspResponse sendPost(RsspFuntion funtion) throws Exception {
        return sendPost(funtion, null);
    }

    public RsspResponse sendPost(RsspFuntion funtion, HttpConfig httpConfig) throws Exception {

//        HttpsURLConnection.setDefaultHostnameVerifier((String arg0, SSLSession arg1) -> true);
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        String endpointUrl = URL + funtion.getMethod();
        System.out.println("Send POST request to " + endpointUrl);
        HttpURLConnection con = null;
        try {

            if (httpConfig != null) {
                con = httpConfig.getHttpConnection(endpointUrl);
            } else {
                URL obj = new URL(endpointUrl);
                con = (HttpURLConnection) obj.openConnection();
            }
            
            con.setRequestMethod("POST");
            String authorization = "";

            switch (funtion) {
                case info:
                    break;
                case auth_login:
//                    if (username == null) {
//                        throw new NullPointerException("Username can't be null");
//                    }
//                    if (password == null) {
//                        throw new NullPointerException("Password can't be null");
//                    }
                    authorization = getAuthorization(username, password);  
                    username = null;
                    userType = null;
                    break;
                case auth_login_ssl_only:
                    authorization = getAuthorization();
                    break;
                default:
                    if (bearer != null) {
                        authorization = "Bearer " + bearer;
                    } else {
                        throw new NullPointerException("Bearer can't be null");
                    }
                    break;
            }
            con.setRequestProperty("Authorization", authorization);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            con.setConnectTimeout(15000);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(new GsonBuilder().disableHtmlEscaping().create().toJson(this).getBytes("UTF-8"));
                wr.flush();
            }
            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("Error while calling server");
            }
            StringBuilder response;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            con.disconnect();
            return new GsonBuilder().disableHtmlEscaping().create().fromJson(response.toString(), RsspResponse.class);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private String getAuthorization(String username, String password) throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);

        return "SSL2 " + Base64.getEncoder().encodeToString(
                relyingPartyUser.concat(":")
                        .concat(relyingPartyPassword).concat(":")
                        .concat(relyingPartySignature).concat(":")
                        .concat(timestamp).concat(":")
                        .concat(pkcs1Signature)
                        .getBytes());
//                + ", basic "
                //+ Base64.getEncoder().encodeToString((userType + ":" + username + ":" + password).getBytes());
    }

    private String getAuthorization() throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String data2sign = relyingPartyUser + relyingPartyPassword + relyingPartySignature + timestamp;
        String pkcs1Signature = getPKCS1Signature(data2sign, relyingPartyKeyStore, relyingPartyKeyStorePassword);

        return "SSL2 " + Base64.getEncoder().encodeToString(
                relyingPartyUser.concat(":")
                        .concat(relyingPartyPassword).concat(":")
                        .concat(relyingPartySignature).concat(":")
                        .concat(timestamp).concat(":")
                        .concat(pkcs1Signature)
                        .getBytes());
    }

    public static String getPKCS1Signature(String data, InputStream isFileStore, String relyingPartyKeyStorePassword) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        //InputStream is = new FileInputStream(relyingPartyKeyStore);
        keystore.load(isFileStore, relyingPartyKeyStorePassword.toCharArray());

        Enumeration<String> e = keystore.aliases();
        String aliasName = "";
        while (e.hasMoreElements()) {
            aliasName = e.nextElement();
        }
        PrivateKey key = (PrivateKey) keystore.getKey(aliasName,
                relyingPartyKeyStorePassword.toCharArray());
       
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
        sig.update(data.getBytes());
        return DatatypeConverter.printBase64Binary(sig.sign());
    }

    @Override
    public String toString() {
        return  new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(this);
    }
}
