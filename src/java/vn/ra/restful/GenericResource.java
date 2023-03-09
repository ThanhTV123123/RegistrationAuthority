/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRFile;
import vn.ra.object.ATTRIBUTE_DATA;
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.BRANCH;
import vn.ra.object.BranchInfo;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_DATA_ATTR;
import vn.ra.object.CERTIFICATION_OWNER_DATA_ATTR;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.CERTIFICATION_PROPERTIES_JSON;
import vn.ra.object.CERTIFICATION_TYPE_COMPONENT;
import vn.ra.object.CITY_PROVINCE;
import vn.ra.object.CertificateAuthorityInfo;
import vn.ra.object.CertificateComponentInfo;
import vn.ra.object.CertificateInfo;
import vn.ra.object.CertificateProfileInfo;
import vn.ra.object.CertificatePurposeInfo;
import vn.ra.object.CertificateStateInfo;
import vn.ra.object.CityProvinceInfo;
import vn.ra.object.FILE_PROFILE_JSON;
import vn.ra.object.FileManagerInfo;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.JSON_USER_BRANCH_DEFAULT;
import vn.ra.object.RESPONSE_CODE;
import vn.ra.object.ServiceRequestExpand;
import vn.ra.object.UserInfo;
import vn.ra.process.CommonFunction;
import vn.ra.process.CommonReferServlet;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.ConnectFileToPartner;
import vn.ra.process.ConnectJackRabbitNew;
import vn.ra.process.JackRabbitCommon;
import vn.ra.process.RACoreWSProcess;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;
import vn.ra.ws.CredentialData;
import vn.ra.ws.HandShaking;
import vn.ra.ws.RAServiceReq;
import vn.ra.ws.RAServiceResp;
import java.util.concurrent.TimeUnit;

/**
 * REST Web Service
 *
 * @author USER
 */
@Path("restapi")
public class GenericResource {
    private static final Logger log = Logger.getLogger(GenericResource.class);
//    public static String sJSONRestful = "{\"attributeType\":\"RestfulJWTSecureProperties\",\"remarkEn\":\"Restful JWT Secure Properties\",\"remark\":\"Thuộc tính Restful JWT Secure\",\"password\":\"B09F78AE0C4730F2CFAD5EB438473D31F844A171DDC40CB9A7043315AA5C499C42387EA41814E6CD2B3D7BD9220468F0\",\"secretKey\":\"0LhlmTt2uAcBY_sjmEd5sCxPom_lIxuLZ4cc46LBzYc\",\"expirationTime\":\"30\"}";

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }
    
    //<editor-fold defaultstate="collapsed" desc="### 1 getTestTimerForTMSRA">
    @POST
    @Path("getTestTimerForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getTestTimerForTMSRA(@Suspended final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                System.out.println("Start delay of 10 seconds, Time is: " + CommonFunction.getCurrentTime());
                TimeUnit.SECONDS.sleep(20);
                System.out.println("And delay of 10 seconds, Time is: " + CommonFunction.getCurrentTime());
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
            asyncResponse.resume(new Gson().toJson(raServiceResp));
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 1 getAccessTokenForTMSRA">
    @POST
    @Path("getAccessTokenForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getAccessTokenForTMSRA(@Suspended final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        long[] intExpire = new long[1];intExpire[0] = 30;
        try {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            String userName = EscapeUtils.CheckTextNull(raServiceReq.userName);
            String passWord = EscapeUtils.CheckTextNull(raServiceReq.passWord);
            if("".equals(userName) || "".equals(passWord)) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                String sFUNCTIONALTITY_PROPERTIES = "";
                String sIP_ADDRESS_PROPERTIES = "";
                String sREST_JWT_PROPERTIES = "";
                String sIP_Request = httpServletRequest.getRemoteAddr();
                String[] sSECRET_KEY = new String[1];
                String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_TOKEN_ACCESS;
                //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(userName, rsBranch);
                if (rsBranch[0].length > 0) {
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                    if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp = HandShaking.checkRestfulAccount(sREST_JWT_PROPERTIES, userName, passWord, intExpire, sSECRET_KEY);
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
                //</editor-fold>

                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    //<editor-fold defaultstate="collapsed" desc="### CHECK IP - FUNCTION ACCESS">
                    CERTIFICATION_POLICY_DATA[][] resPolicyData;
                    boolean checkFunctionAccessAll = CommonFunction.checkAPIAccessFunctionAll(sFUNCTIONALTITY_PROPERTIES);
                    if (checkFunctionAccessAll == false) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_ACCESS_FUNCTION_INVALID;
                        resPolicyData = new CERTIFICATION_POLICY_DATA[1][];
                        CommonFunction.getFunctionAccessList(sFUNCTIONALTITY_PROPERTIES, resPolicyData);
                        if (resPolicyData[0].length > 0) {
                            for (CERTIFICATION_POLICY_DATA rsPolicyProperties : resPolicyData[0]) {
                                if (sFunctionWS.equals(EscapeUtils.CheckTextNull(rsPolicyProperties.name))) {
                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                                    break;
                                }
                            }
                        }
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        boolean checkIPAccessAll = CommonFunction.checkAPIAccessIPAll(sIP_ADDRESS_PROPERTIES);
                        if (checkIPAccessAll == false) {
                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_ACCESS_IP_INVALID;
                            resPolicyData = new CERTIFICATION_POLICY_DATA[1][];
                            CommonFunction.getIPAccessList(sIP_ADDRESS_PROPERTIES, resPolicyData);
                            if (resPolicyData[0].length > 0) {
                                for (CERTIFICATION_POLICY_DATA rsPolicyProperties : resPolicyData[0]) {
                                    if (sIP_Request.equals(EscapeUtils.CheckTextNull(rsPolicyProperties.name))) {
                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    //</editor-fold>
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        String sJWT = JWTProcess.generalSecretToken(sSECRET_KEY[0], userName, intExpire[0]);
                        if(!"".equals(sJWT)) {
                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                            raServiceResp.accessToken = sJWT;
                        } else {
                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                        }
                    }
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement jsonElement = gson.toJsonTree(raServiceResp);
            jsonElement.getAsJsonObject().addProperty("expireTimeValue", intExpire[0]);
            jsonElement.getAsJsonObject().addProperty("expireTimeUnit", "Minute");
            String s1 = new Gson().toJson(jsonElement);
            asyncResponse.resume(s1);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 2 registerCertificateForTMSRA">
    @POST
    @Path("registerCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void registerCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sISSUE_P12_ENABLED = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sISSUE_P12_ENABLED = rsBranch[0][0].ISSUE_P12_ENABLED;
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.registerCertificateProcess(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pApproveCAUser,
                        autoApproveCAServer, System_Log_ID, System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, sISSUE_P12_ENABLED, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_RSSP_REST_RETURN_ERROR) {
                        raServiceResp.responseMessage = rsResponseCode[0][0].REMARK + ": " + raServiceResp.responseMessage;
                    } else {
                        raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                    }
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 2 buyMoreCertificateForTMSRA">
    @POST
    @Path("buyMoreCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void buyMoreCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sISSUE_P12_ENABLED = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sISSUE_P12_ENABLED = rsBranch[0][0].ISSUE_P12_ENABLED;
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.buyMoreCertificateProcess(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pApproveCAUser,
                        autoApproveCAServer, System_Log_ID, System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, sISSUE_P12_ENABLED, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_RSSP_REST_RETURN_ERROR) {
                        raServiceResp.responseMessage = rsResponseCode[0][0].REMARK + ": " + raServiceResp.responseMessage;
                    } else {
                        raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                    }
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### 3 registerCertificate2ForTMSRA">
    @POST
    @Path("registerCertificate2ForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void registerCertificate2ForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.registerCertificate2Process(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pApproveCAUser,
                        autoApproveCAServer, System_Log_ID, System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    
    @POST
    @Path("renewCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void renewCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        String pBeneficiaryUserDefault = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.renewCertificateProcess(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pPARENT_ID,
                        pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### changeCertificateInfoForTMSRA">
    @POST
    @Path("changeCertificateInfoForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void changeCertificateInfoForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        String pBeneficiaryUserDefault = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.changeCertificateProcess(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pPARENT_ID,
                        pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### revokeCertificateForTMSRA">
    @POST
    @Path("revokeCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void revokeCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        String pBeneficiaryUserDefault = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.revokeCertificateProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer,
                        System_Log_ID, System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### suspendCertificateForTMSRA">
    @POST
    @Path("suspendCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void suspendCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        String pBeneficiaryUserDefault = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.suspendCertificateProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer,
                        System_Log_ID, System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### recoveryCertificateForTMSRA">
    @POST
    @Path("recoveryCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void recoveryCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        String pBeneficiaryUserDefault = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.recoveryCertificateProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### declineCertificateForTMSRA">
    @POST
    @Path("declineCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void declineCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        String pBeneficiaryUserDefault = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.declineCertificateProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### approveCertificateForTMSRA">
    @POST
    @Path("approveCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void approveCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        String pBeneficiaryUserDefault = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveCertificateProcess(sCERT_POLICY_PROPERTIES, pPARENT_ID, pBRANCH_ID,
                        pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### activationCertificateForTMSRA">
    @POST
    @Path("activationCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void activationCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        String pBeneficiaryUserDefault = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.activationCertificateProcess(sCERT_POLICY_PROPERTIES, pPARENT_ID, pBRANCH_ID,
                        pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### generateCertificateForTMSRA">
    @POST
    @Path("generateCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void generateCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        String pBeneficiaryUserDefault = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.generateCertificateProcess(sCERT_POLICY_PROPERTIES, pPARENT_ID, pBRANCH_ID,
                        pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### reissueCertificateForTMSRA">
    @POST
    @Path("reissueCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void reissueCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        String pBeneficiaryUserDefault = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.reissueCertificateProcess(sCERT_POLICY_PROPERTIES, pPARENT_ID, pBRANCH_ID,
                        pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getBranchForTMSRA">
    @POST
    @Path("getBranchForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getBranchForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sCA_ENABLED = "1";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            String pPARENT_ID;
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                if (!pPARENT_ID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                    sCA_ENABLED = "0";
                }
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    if ("0".equals(sCA_ENABLED)) {
                        raServiceReq.branchCode = agentCode;
                    }
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getBranchProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
//        RAWebservice ws = new RAWebservice();
//
//        RAServiceReq req = new Gson().fromJson(payload, RAServiceReq.class);
//
//        RAServiceResp rsp = ws.getBranchForTMSRA(req);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getUserRoleForTMSRA">
    @POST
    @Path("getUserRoleForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getUserRoleForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sCA_ENABLED = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            String pPARENT_ID;
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                if (!pPARENT_ID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                    sCA_ENABLED = "0";
                }
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getUserRoleProcess(sCA_ENABLED, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getUserForTMSRA">
    @POST
    @Path("getUserForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getUserForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String pBRANCH_NAME = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            String pPARENT_ID;
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                if (!pPARENT_ID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                    pBRANCH_NAME = rsBranch[0][0].NAME;
                } else {
                    pBRANCH_NAME = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
                }
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getUserProcess(pBRANCH_NAME, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCityProvinceForTMSRA">
    @POST
    @Path("getCityProvinceForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCityProvinceForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCityProvinceProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateAuthorityForTMSRA">
    @POST
    @Path("getCertificateAuthorityForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateAuthorityForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_PROFILE_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateAuthorityProcess(sCERT_PROFILE_PROPERTIES, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="### getCertificatePurposeForTMSRA">
    @POST
    @Path("getCertificatePurposeForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificatePurposeForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_PROFILE_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificatePurposeProcess(sCERT_PROFILE_PROPERTIES, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateProfileForTMSRA">
    @POST
    @Path("getCertificateProfileForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateProfileForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_PROFILE_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateProfileProcess(sCERT_PROFILE_PROPERTIES, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateComponentForTMSRA">
    @POST
    @Path("getCertificateComponentForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateComponentForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_PROFILE_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateComponentProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateFileListForTMSRA">
    @POST
    @Path("getCertificateFileListForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateFileListForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_PROFILE_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateFileListProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getFormFactorForTMSRA">
    @POST
    @Path("getFormFactorForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getFormFactorForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getFormFactorProcess(sCERT_POLICY_PROPERTIES, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateStateForTMSRA">
    @POST
    @Path("getCertificateStateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateStateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateStateProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getRequestStateForTMSRA">
    @POST
    @Path("getRequestStateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getRequestStateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getRequestStateProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateRevocationReasonForTMSRA">
    @POST
    @Path("getCertificateRevocationReasonForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateRevocationReasonForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateRevocationReasonProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);                    
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateInfoForTMSRA">
    @POST
    @Path("getCertificateInfoForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateInfoForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    }
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateInfoProcess(pBeneficiaryUserDefault, System_Log_ID, System_Log_BillCode,
                        sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    RAServiceResp raServiceRespLog = new RAServiceResp();
                    raServiceRespLog.responseCode = raServiceResp.responseCode;
                    raServiceRespLog.responseMessage = raServiceResp.responseMessage;
                    CertificateInfo[][] certInfoLog = new CertificateInfo[1][];
                    CommonFunction.certListTruncatedForSystemLog(raServiceResp.certificateInfo, certInfoLog);
                    raServiceRespLog.certificateInfo = certInfoLog[0];
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceRespLog.responseCode),
                        objectMapper.writeValueAsString(raServiceRespLog), sTOKEN_SN, pBeneficiaryUserDefault);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getFileTypeForTMSRA">
    @POST
    @Path("getFileTypeForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getFileTypeForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getFileTypeProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateAttachmentForTMSRA">
    @POST
    @Path("getCertificateAttachmentForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateAttachmentForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateAttachmentProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request,
                        raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### reportCertificateNEACForTMSRA">
    @POST
    @Path("reportCertificateNEACForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void reportCertificateNEACForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String pPARENT_ID = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    }
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.reportCertificateNEACProcess(pPARENT_ID, pBeneficiaryUserDefault, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, pBeneficiaryUserDefault);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### reportCertificateListNEACForTMSRA">
    @POST
    @Path("reportCertificateListNEACForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void reportCertificateListNEACForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String pPARENT_ID = "";
            String pApproveCAUser = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    }
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.reportCertificateListNEACProcess(pPARENT_ID, pBeneficiaryUserDefault, pApproveCAUser,
                        System_Log_ID, System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                if (System_Log_ID[0] != 0) {
                    RAServiceResp raServiceRespLog = new RAServiceResp();
                    raServiceRespLog.responseCode = raServiceResp.responseCode;
                    raServiceRespLog.responseMessage = raServiceResp.responseMessage;
                    raServiceRespLog.countCertificateNEACReportInfo = raServiceResp.certificateNEACReportInfo.length;
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceRespLog.responseCode),
                        objectMapper.writeValueAsString(raServiceRespLog), sTOKEN_SN, pBeneficiaryUserDefault);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### reportCertificateForTMSRA">
    @POST
    @Path("reportCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void reportCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String pPARENT_ID = "";
            String pBRANCH_ID = "";
            String sIP_Request = httpServletRequest.getRemoteAddr();
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = String.valueOf(rsBranch[0][0].ID);
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    }
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.reportCertificateProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                if (System_Log_ID[0] != 0) {
                    RAServiceResp raServiceRespLog = new RAServiceResp();
                    raServiceRespLog.responseCode = raServiceResp.responseCode;
                    raServiceRespLog.responseMessage = raServiceResp.responseMessage;
                    raServiceRespLog.countCertificateReportInfo = raServiceResp.certificateReportInfo.length;
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceRespLog.responseCode),
                        objectMapper.writeValueAsString(raServiceRespLog), sTOKEN_SN, pBeneficiaryUserDefault);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### setCertificateAttachmentForTMSRA">
    @POST
    @Path("setCertificateAttachmentForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void setCertificateAttachmentForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.setCertificateAttachmentProcess(pPARENT_ID, pBRANCH_ID,
                        pApproveCAUser, autoApproveCAServer, System_Log_ID, System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### approveCertificateAttachmentForTMSRA">
    @POST
    @Path("approveCertificateAttachmentForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void approveCertificateAttachmentForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveCertificateAttachmentProcess(pPARENT_ID, pBRANCH_ID,
                        pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### registerCertificateOwnerForTMSRA">
    @POST
    @Path("registerCertificateOwnerForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void registerCertificateOwnerForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_REGISTRATION_CERTIFICATION_OWNER;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = pApproveCAUser;
                    }
                    raServiceReq.beneficiaryUser = pBeneficiaryUserDefault;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.registerCertificateOwnerProcess(pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    RAServiceResp raServiceRespLog = new RAServiceResp();
                    raServiceRespLog.responseCode = raServiceResp.responseCode;
                    raServiceRespLog.responseMessage = raServiceResp.responseMessage;
                    CertificateInfo[][] certInfoLog = new CertificateInfo[1][];
                    CommonFunction.certListTruncatedForSystemLog(raServiceResp.certificateInfo, certInfoLog);
                    raServiceRespLog.certificateInfo = certInfoLog[0];
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceRespLog.responseCode),
                        objectMapper.writeValueAsString(raServiceRespLog), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### changeCertificateOwnerInfoForTMSRA">
    @POST
    @Path("changeCertificateOwnerInfoForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void changeCertificateOwnerInfoForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_CHANGE_CERTIFICATION_OWNER_INFO;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = pApproveCAUser;
                    }
                    raServiceReq.beneficiaryUser = pBeneficiaryUserDefault;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.changeCertificateOwnerInfoProcess(pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="### disposeCertificateOwnerForTMSRA">
    @POST
    @Path("disposeCertificateOwnerForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void disposeCertificateOwnerForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_DISPOSE_CERTIFICATION_OWNER;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = pApproveCAUser;
                    }
                    raServiceReq.beneficiaryUser = pBeneficiaryUserDefault;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.disposeCertificateOwnerProcess(pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### declineCertificateOwnerForTMSRA">
    @POST
    @Path("declineCertificateOwnerForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void declineCertificateOwnerForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_DECLINE_CERTIFICATION_OWNER;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceReq.approveUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.declineCertificateOwnerProcess(System_Log_ID, System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES,
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, raServiceReq.approveUser);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### approveCertificateOwnerForTMSRA">
    @POST
    @Path("approveCertificateOwnerForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void approveCertificateOwnerForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_APPROVAL_CERTIFICATION_OWNER;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = pApproveCAUser;
                    }
                    raServiceReq.beneficiaryUser = pBeneficiaryUserDefault;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveCertificateOwnerProcess(pApproveCAUser, autoApproveCAServer, System_Log_ID, System_Log_BillCode,
                        sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                // get response code return
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, raServiceReq.approveUser);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### setCertificateOwnerAttachmentForTMSRA">
    @POST
    @Path("setCertificateOwnerAttachmentForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void setCertificateOwnerAttachmentForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = pApproveCAUser;
                    }
                    raServiceReq.beneficiaryUser = pBeneficiaryUserDefault;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.setCertificateOwnerAttachmentProcess(pApproveCAUser, autoApproveCAServer, log, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### approveCertificateOwnerAttachmentForTMSRA">
    @POST
    @Path("approveCertificateOwnerAttachmentForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void approveCertificateOwnerAttachmentForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String pBeneficiaryUserDefault = "";
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = pApproveCAUser;
                    }
                    raServiceReq.beneficiaryUser = pBeneficiaryUserDefault;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveCertificateOwnerAttachmentProcess(pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateOwnerTypeForTMSRA">
    @POST
    @Path("getCertificateOwnerTypeForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateOwnerTypeForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTIFICATION_OWNER_TYPE;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateOwnerTypeProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateOwnerStateForTMSRA">
    @POST
    @Path("getCertificateOwnerStateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateOwnerStateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTIFICATION_OWNER_STATE;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateOwnerStateProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateOwnerInfoForTMSRA">
    @POST
    @Path("getCertificateOwnerInfoForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateOwnerInfoForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTICATION_OWNER_INFO;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateOwnerInfoProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateOwnerAttachmentForTMSRA">
    @POST
    @Path("getCertificateOwnerAttachmentForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateOwnerAttachmentForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTIFICATION_OWNER_ATTACHMENT;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateOwnerAttachmentProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        }  finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### setFormFactorUnblockForTMSRA">
    @POST
    @Path("setFormFactorUnblockForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void setFormFactorUnblockForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pPARENT_ID = "";
        String pApproveCAUser = "";
        int pBRANCH_ID = 0;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.setFormFactorUnblockProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### approveFormFactorUnblockForTMSRA">
    @POST
    @Path("approveFormFactorUnblockForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void approveFormFactorUnblockForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pPARENT_ID = "";
        String pApproveCAUser = "";
        int pBRANCH_ID = 0;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveFormFactorUnblockProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### declineFormFactorUnblockForTMSRA">
    @POST
    @Path("declineFormFactorUnblockForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void declineFormFactorUnblockForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pPARENT_ID = "";
        String pApproveCAUser = "";
        int pBRANCH_ID = 0;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.declineFormFactorUnblockProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getFormFactorUnblockForTMSRA">
    @POST
    @Path("getFormFactorUnblockForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getFormFactorUnblockForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_FORMFACTOR_UNBLOCK;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String pPARENT_ID = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getFormFactorUnblockProcess(pPARENT_ID, pBRANCH_ID, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### setFormFactorTokenForTMSRA">    
    @POST
    @Path("setFormFactorTokenForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void setFormFactorTokenForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pPARENT_ID = "";
        String pApproveCAUser = "";
        int pBRANCH_ID = 0;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.setFormFactorTokenProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### approveFormFactorTokenForTMSRA">
    @POST
    @Path("approveFormFactorTokenForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void approveFormFactorTokenForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pPARENT_ID = "";
        String pApproveCAUser = "";
        int pBRANCH_ID = 0;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveFormFactorTokenProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### declineFormFactorTokenForTMSRA">
    @POST
    @Path("declineFormFactorTokenForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void declineFormFactorTokenForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pPARENT_ID = "";
        String pApproveCAUser = "";
        int pBRANCH_ID = 0;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.declineFormFactorTokenProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getFormFactorTokenForTMSRA">
    @POST
    @Path("getFormFactorTokenForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getFormFactorTokenForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_FORMFACTOR_TOKEN;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String pPARENT_ID = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getFormFactorTokenProcess(pPARENT_ID, pBRANCH_ID, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getQueueStateForTMSRA">
    @POST
    @Path("getQueueStateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getQueueStateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_QUEUE_STATE;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getQueueStateProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getQueueTypeForTMSRA">
    @POST
    @Path("getQueueTypeForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getQueueTypeForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_QUEUE_TYPE;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getQueueTypeProcess(sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### registerBeneficiaryUserForTMSRA">
    @POST
    @Path("registerBeneficiaryUserForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void registerBeneficiaryUserForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_REGISTRATION_BENEFICIARY_USER;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String pApproveCAUser;
            String pPARENT_ID = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.registerBeneficiaryUserProcess(pPARENT_ID, pBRANCH_ID, System_Log_ID, System_Log_BillCode,
                        sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, raServiceReq.approveUser);
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateBriefInfoForTMSRA">
    @POST
    @Path("getCertificateBriefInfoForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateBriefInfoForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTIFICATION_BRIEF_INFO;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pBeneficiaryUserDefault = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String pPARENT_ID = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    }
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateBriefInfoProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, System_Log_ID, System_Log_BillCode,
                        sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### compensateCertificateForTMSRA">
    @POST
    @Path("compensateCertificateForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void compensateCertificateForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        boolean autoApproveCAServer = false;
        String pApproveCAUser = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        String pPARENT_ID = "";
        String pBeneficiaryUserDefault = "";
        int pBRANCH_ID = 0;
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.compensateCertificateProcess(sCERT_PROFILE_PROPERTIES, sCERT_POLICY_PROPERTIES, pPARENT_ID,
                        pBRANCH_ID, pBeneficiaryUserDefault, pApproveCAUser, autoApproveCAServer, System_Log_ID,
                        System_Log_BillCode, sTOKEN_SN_LOG, log, sFUNCTIONALTITY_PROPERTIES, 
                        sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getCertificateExpireSoonInfoForTMSRA">
    @POST
    @Path("getCertificateExpireSoonInfoForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getCertificateExpireSoonInfoForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_CERTIFICATION_EXPIRE_SOON_INFO;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pBeneficiaryUserDefault = "";
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String pPARENT_ID = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    pBeneficiaryUserDefault = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
                    if("".equals(pBeneficiaryUserDefault))
                    {
                        pBeneficiaryUserDefault = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    }
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getCertificateExpireSoonInfoProcess(pPARENT_ID, pBRANCH_ID, pBeneficiaryUserDefault, System_Log_ID, System_Log_BillCode,
                        sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES, sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                String sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_54100000001000;
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### setFormFactorTokenBundleForTMSRA">
    @POST
    @Path("setFormFactorTokenBundleForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void setFormFactorTokenBundleForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pPARENT_ID = "";
        String pApproveCAUser = "";
        int pBRANCH_ID = 0;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.setFormFactorTokenBundleProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0 && !"".equals(System_Log_BillCode[0])) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                        objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### approveFormFactorTokenBundleForTMSRA">
    @POST
    @Path("approveFormFactorTokenBundleForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void approveFormFactorTokenBundleForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pPARENT_ID = "";
        String pApproveCAUser = "";
        int pBRANCH_ID = 0;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.approveFormFactorTokenBundleProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD)
                {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### declineFormFactorTokenBundleForTMSRA">
    @POST
    @Path("declineFormFactorTokenBundleForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void declineFormFactorTokenBundleForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
        String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFUNCTIONALTITY_PROPERTIES = "";
        String sIP_ADDRESS_PROPERTIES = "";
        String sREST_JWT_PROPERTIES = "";
        String sCERT_POLICY_PROPERTIES = "";
        String sCERT_PROFILE_PROPERTIES = "";
        String sIP_Request = httpServletRequest.getRemoteAddr();
        String pPARENT_ID = "";
        String pApproveCAUser = "";
        int pBRANCH_ID = 0;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String[] sTOKEN_SN_LOG = new String[1];
        try {
            //<editor-fold defaultstate="collapsed" desc="### CHECK Username">
            raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
            raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                    raServiceReq.approveUser = pApproveCAUser;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.declineFormFactorTokenBundleProcess(pPARENT_ID, pBRANCH_ID, pApproveCAUser, System_Log_ID, System_Log_BillCode,
                        sTOKEN_SN_LOG, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        }
        catch(Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID_OLD) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                }
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
                CertificateStateInfo[][] stateLogInfo = new CertificateStateInfo[1][];
                db.S_BO_API_CERTIFICATION_STATE_LIST(EscapeUtils.CheckTextNull(raServiceResp.certificateStateCode),
                        raServiceReq.language, stateLogInfo);
                if (stateLogInfo[0].length > 0) {
                    raServiceResp.certificateStateName = stateLogInfo[0][0].certificateStateName.trim();
                }
                raServiceResp.billCode = System_Log_BillCode[0];
                if (System_Log_ID[0] != 0) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN_LOG[0], EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="### getFormFactorTokenBundleForTMSRA">
    @POST
    @Path("getFormFactorTokenBundleForTMSRA")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    public void getFormFactorTokenBundleForTMSRA(@Suspended
            final AsyncResponse asyncResponse, @Context
            final HttpServletRequest httpServletRequest, final String payloadBody)
        throws Exception {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        RAServiceReq raServiceReq = new Gson().fromJson(payloadBody, RAServiceReq.class);
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_GET_FORMFACTOR_TOKEN;
        String sIP_Request = httpServletRequest.getRemoteAddr();
        try {
            String payloadHeader = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("Authorization"));
            String agentCode = EscapeUtils.CheckTextNull(httpServletRequest.getHeader("userName"));
            String sCERT_POLICY_PROPERTIES = "";
	    String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String sREST_JWT_PROPERTIES = "";
            String pPARENT_ID = "";
            int pBRANCH_ID = 0;
            //<editor-fold defaultstate="collapsed" desc="### AUTHENTICATION USERNAME">
            BRANCH[][] rsBranch = new BRANCH[1][];
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
            db.S_BO_API_BRANCH_GET_INFO(agentCode, rsBranch);
            if (rsBranch[0].length > 0) {
                pBRANCH_ID = rsBranch[0][0].ID;
                pPARENT_ID = EscapeUtils.CheckTextNull(String.valueOf(rsBranch[0][0].PARENT_ID));
                sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                sREST_JWT_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].REST_JWT_PROPERTIES);
                if ("".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)
                     || "".equals(sREST_JWT_PROPERTIES)) {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkRestfulJWT(payloadHeader, sREST_JWT_PROPERTIES, agentCode);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RACoreWSProcess wsCore = new RACoreWSProcess();
                    wsCore.getFormFactorTokenBundleProcess(pPARENT_ID, pBRANCH_ID, sFUNCTIONALTITY_PROPERTIES, sIP_ADDRESS_PROPERTIES,
                        sIP_Request, raServiceReq, raServiceResp);
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
        } finally {
            try {
                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                db.S_BO_API_RESPONSE_CODE_GET_INFO(String.valueOf(raServiceResp.responseCode), raServiceReq.language, rsResponseCode);
                if (rsResponseCode[0].length > 0) {
                    raServiceResp.responseMessage = rsResponseCode[0][0].REMARK;
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, sIP_Request + " - " + sFunctionWS + " - An Unknown Error: " + e.getMessage(), e);
            }
        }
        asyncResponse.resume(new Gson().toJson(raServiceResp));
    }
    //</editor-fold>
}
