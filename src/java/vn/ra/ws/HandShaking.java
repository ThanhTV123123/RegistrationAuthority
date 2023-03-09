/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.apache.log4j.Logger;
import vn.ra.object.RESTJWTSecureProperties;
import vn.ra.object.SOAPSecureProperties;
import vn.ra.process.CommonFunction;
import vn.ra.restful.JWTProcess;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class HandShaking {

    private static final Logger log = Logger.getLogger(HandShaking.class);

    public static RAServiceResp checkCredentialData(CredentialData credentialData, String sJSON_SOAP)
            throws IOException {
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper oMapperParse = new ObjectMapper();
        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
        String usernameClient = EscapeUtils.CheckTextNull(credentialData.username);
        String passwordClient = EscapeUtils.CheckTextNull(credentialData.password);
        String signatureClient = EscapeUtils.CheckTextNull(credentialData.signature);
        String pkcs1SignatureClient = EscapeUtils.CheckTextNull(credentialData.pkcs1Signature);
        String timestampClient = EscapeUtils.CheckTextNull(credentialData.timestamp);
        if ("".equals(usernameClient) || "".equals(passwordClient) || "".equals(signatureClient)
                || "".equals(pkcs1SignatureClient)) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
        }
//        CommonFunction.LogDebugString(log, "ssl2Verify", "Verifying relyingparty username/password");
        // get param from JSON SOAP
        String passwordServer = "";
        String signatureServer = "";
        String pkcs1SignatureServer = "";
        if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
            SOAPSecureProperties itemParsePush = oMapperParse.readValue(sJSON_SOAP, SOAPSecureProperties.class);
            passwordServer = EscapeUtils.CheckTextNull(itemParsePush.password);
            signatureServer = EscapeUtils.CheckTextNull(itemParsePush.signature);
            pkcs1SignatureServer = EscapeUtils.CheckTextNull(itemParsePush.publicKeyPem);
            if ("".equals(passwordServer) || "".equals(signatureServer) || "".equals(pkcs1SignatureServer)) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            }
        }
        // check authen
        if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
            String hashedUserPassClient = CommonFunction.hashPass((usernameClient.toLowerCase() + passwordClient.toLowerCase()).getBytes()); // hash pass param
            if (hashedUserPassClient.equals(passwordServer)) {
                if (signatureClient.equals(signatureServer)) {
                    String signedData;
                    if (!"".equals(timestampClient)) {
                        signedData = usernameClient + passwordClient + signatureClient + timestampClient;
                    } else {
                        signedData = usernameClient + passwordClient + signatureClient;
                    }
                    PublicKey publicKey = CommonFunction.getPublicKeyInPemFormat(pkcs1SignatureServer);
                    if (publicKey != null) {
                        try {
                            Signature s = Signature.getInstance("SHA1withRSA");
                            s.initVerify(publicKey);
                            s.update(signedData.getBytes());
                            if (s.verify(DatatypeConverter.parseBase64Binary(pkcs1SignatureClient))) {
                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                            } else {
                                // invalid signature
                                CommonFunction.LogDebugString(log, "ssl2Verify", "invalid signature");
                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_AUTHEN_SIGNATURE_INVALID;
                            }
                        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
                            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                        }
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_AUTHEN_SIGNATURE_INVALID;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_AUTHEN_PASSWORD_INVALID;
            }
        }
        return raServiceResp;
    }

    public static RAServiceResp checkRestfulJWT(String tokenJWT, String sJSON_REST, String sUsername)
            throws IOException {
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper oMapperParse = new ObjectMapper();
        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
        RESTJWTSecureProperties itemParsePush = oMapperParse.readValue(sJSON_REST, RESTJWTSecureProperties.class);
        String sSecretKey = EscapeUtils.CheckTextNull(itemParsePush.secretKey);
        if (!"".equals(tokenJWT)) {
            tokenJWT = tokenJWT.replace("Bearer", "").trim();
        }
        int[] intResJWT = new int[1];
        Claims claimsDecode = JWTProcess.parseSecretToken(tokenJWT, sSecretKey, intResJWT);
        switch (intResJWT[0]) {
            case 0:
                if (claimsDecode != null) {
                    if (claimsDecode.getSubject().equals(sUsername)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
                }   break;
            case 1:
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_CREDENTIAL_EXPIRE;
                break;
            default:
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
                break;
        }
        return raServiceResp;
    }
    
    public static RAServiceResp checkRestfulAccount(String sJSON_REST, String sUsername, String sPassword, long[] intExpire, String[] sSecretKey)
            throws IOException {
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper oMapperParse = new ObjectMapper();
        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
        RESTJWTSecureProperties itemParsePush = oMapperParse.readValue(sJSON_REST, RESTJWTSecureProperties.class);
        if(itemParsePush != null)
        {
            String sSecretKeyServer = EscapeUtils.CheckTextNull(itemParsePush.secretKey);
            String sExpire = EscapeUtils.CheckTextNull(itemParsePush.expirationTime);
            String passwordServer = EscapeUtils.CheckTextNull(itemParsePush.password);
            if(!"".equals(passwordServer) && !"".equals(sExpire) && !"".equals(sSecretKeyServer)) {
                intExpire[0] = Integer.parseInt(sExpire);
                sSecretKey[0] = sSecretKeyServer;
                String hashedUserPassClient = CommonFunction.hashPass((sUsername.toLowerCase() + sPassword.toLowerCase()).getBytes()); // hash pass param
                if (!hashedUserPassClient.equals(passwordServer)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
        } else {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
        }
        return raServiceResp;
    }

//    public RAServiceResp verify(RAServiceReq raServiceReq, boolean isUseSSL) {
//        CacheProperties cacheProperties = relyingParty.getCacheProperties();
//        int duration = cacheProperties.getDuration();
//        RelyingPartyCacheInfo relyingPartyCacheInfo = Resources.getRelyingPartyCache().get(relyingPartyName);
//        if (relyingPartyCacheInfo == null) {
//            // no cache before
//            SignCloudResp ssl2VerifyResp = ssl2Verify(signCloudReq, relyingParty);
//            if (ssl2VerifyResp.getResponseCode() == Constant.CODE_SUCCESS) {
//                relyingPartyCacheInfo = new RelyingPartyCacheInfo();
//                relyingPartyCacheInfo.setCacheDate(Calendar.getInstance().getTime());
//                relyingPartyCacheInfo.setIpRequest(ipRequest);
//                relyingPartyCacheInfo.setName(relyingPartyName);
//                Resources.getRelyingPartyCache().put(relyingPartyName, relyingPartyCacheInfo);
//                return ssl2VerifyResp;
//            } else {
//                // error while handshaking
//                return ssl2VerifyResp;
//            }
//        } else {
//            // already cache
//            if (ipRequest.compareTo(relyingPartyCacheInfo.getIpRequest()) != 0) {
//                // not matching ip
//                SignCloudResp ssl2VerifyResp = ssl2Verify(signCloudReq, relyingParty);
//                if (ssl2VerifyResp.getResponseCode() == Constant.CODE_SUCCESS) {
//                    relyingPartyCacheInfo = new RelyingPartyCacheInfo();
//                    relyingPartyCacheInfo.setCacheDate(Calendar.getInstance().getTime());
//                    relyingPartyCacheInfo.setIpRequest(ipRequest);
//                    relyingPartyCacheInfo.setName(relyingPartyName);
//                    Resources.getRelyingPartyCache().put(relyingPartyName, relyingPartyCacheInfo);
//                    return ssl2VerifyResp;
//                } else {
//                    // error while handshaking
//                    return ssl2VerifyResp;
//                }
//            } else {
//                // matching IP
//                // checking cache time
//                if (relyingParty.getCacheProperties().getUnit().equals(CacheProperties.UNIT_MINUTE)) {
//                    // cache by minute
//                    long deviationOfCacheTime = Utils.getDifferenceBetweenDatesInMinute(relyingPartyCacheInfo.getCacheDate(), Calendar.getInstance().getTime());
//                    if (deviationOfCacheTime > duration) {
//                        // check again
//                        LOG.debug("Cache timeout. Check CredentialData again");
//                        SignCloudResp ssl2VerifyResp = ssl2Verify(signCloudReq, relyingParty);
//                        if (ssl2VerifyResp.getResponseCode() == Constant.CODE_SUCCESS) {
//                            relyingPartyCacheInfo = new RelyingPartyCacheInfo();
//                            relyingPartyCacheInfo.setCacheDate(Calendar.getInstance().getTime());
//                            relyingPartyCacheInfo.setIpRequest(ipRequest);
//                            relyingPartyCacheInfo.setName(relyingPartyName);
//                            Resources.getRelyingPartyCache().put(relyingPartyName, relyingPartyCacheInfo);
//                            return ssl2VerifyResp;
//                        } else {
//                            // error while handshaking
//                            return ssl2VerifyResp;
//                        }
//                    } else {
//                        LOG.debug("Cache time still valid. No check CredentialData");
//                        return new SignCloudResp(Constant.CODE_SUCCESS, Utils.getResponseMessage(Constant.CODE_SUCCESS, signCloudReq.getLanguage()));
//                    }
//                } else {
//                    // cache by hour
//                    long deviationOfCacheTime = Utils.getDifferenceBetweenDatesInHour(relyingPartyCacheInfo.getCacheDate(), Calendar.getInstance().getTime());
//                    if (deviationOfCacheTime > duration) {
//                        // check again
//                        LOG.debug("Cache timeout. Check CredentialData again");
//                        SignCloudResp ssl2VerifyResp = ssl2Verify(signCloudReq, relyingParty);
//                        if (ssl2VerifyResp.getResponseCode() == Constant.CODE_SUCCESS) {
//                            relyingPartyCacheInfo = new RelyingPartyCacheInfo();
//                            relyingPartyCacheInfo.setCacheDate(Calendar.getInstance().getTime());
//                            relyingPartyCacheInfo.setIpRequest(ipRequest);
//                            relyingPartyCacheInfo.setName(relyingPartyName);
//                            Resources.getRelyingPartyCache().put(relyingPartyName, relyingPartyCacheInfo);
//                            return ssl2VerifyResp;
//                        } else {
//                            // error while handshaking
//                            return ssl2VerifyResp;
//                        }
//                    } else {
//                        LOG.debug("Cache time still valid. No check CredentialData");
//                        return new SignCloudResp(Constant.CODE_SUCCESS, Utils.getResponseMessage(Constant.CODE_SUCCESS, signCloudReq.getLanguage()));
//                    }
//                }
//            }
//        }
//    }
}
