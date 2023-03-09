/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.uat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
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
import vn.ra.object.BACKOFFICE_USER;
import vn.ra.object.BRANCH;
import vn.ra.object.BranchInfo;
import vn.ra.object.CERTIFICATE_ATTRIBUTES;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_COMMENT;
import vn.ra.object.CERTIFICATION_DATA_ATTR;
import vn.ra.object.CERTIFICATION_OWNER_COMMENT;
import vn.ra.object.CERTIFICATION_OWNER_DATA_ATTR;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.CERTIFICATION_PROPERTIES_JSON;
import vn.ra.object.CERTIFICATION_TYPE_COMPONENT;
import vn.ra.object.CITY_PROVINCE;
import vn.ra.object.CertificateAttributesRadio;
import vn.ra.object.CertificateAuthorityInfo;
import vn.ra.object.CertificateComponentInfo;
import vn.ra.object.CertificateInfo;
import vn.ra.object.CertificateNEACReportInfo;
import vn.ra.object.CertificateOwnerInfo;
import vn.ra.object.CertificateOwnerStateInfo;
import vn.ra.object.CertificateOwnerTypeInfo;
import vn.ra.object.CertificateProfileInfo;
import vn.ra.object.CertificatePurposeInfo;
import vn.ra.object.CertificateReportInfo;
import vn.ra.object.CertificateRevocationReasonInfo;
import vn.ra.object.CertificateStateInfo;
import vn.ra.object.CityProvinceInfo;
import vn.ra.object.FILE_MANAGER;
import vn.ra.object.FILE_PROFILE_JSON;
import vn.ra.object.FileManagerInfo;
import vn.ra.object.FileTypeInfo;
import vn.ra.object.FormFactorUnblockInfo;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.FormfactorInfo;
import vn.ra.object.JSON_USER_BRANCH_DEFAULT;
import vn.ra.object.PeriodicReportInfo;
import vn.ra.object.QueueStateInfo;
import vn.ra.object.RESPONSE_CODE;
import vn.ra.object.RESPONSE_LOG;
import vn.ra.object.ReconciliationReportInfo;
import vn.ra.object.TOKEN;
import vn.ra.object.TOKEN_CHANGE_LOG;
import vn.ra.object.UserInfo;
import vn.ra.object.UserRoleInfo;
import vn.ra.process.CommonFunction;
import vn.ra.process.CommonReferServlet;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.ConnectFileToPartner;
import vn.ra.process.ConnectJackRabbitNew;
import vn.ra.process.JackRabbitCommon;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;
import vn.ra.ws.HandShaking;
import vn.ra.ws.RAServiceReq;
import vn.ra.ws.RAServiceResp;

/**
 *
 * @author USER
 */
public class RAWebseviceDemo {
    private static final Logger log = Logger.getLogger(RAWebseviceDemo.class);

    @Resource
    WebServiceContext wsContext;

    private String getIPAddress() {
        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        return req.getRemoteAddr();
    }

    //<editor-fold defaultstate="collapsed" desc="### 16 registerCertificateForTMSRA">
    @WebMethod(operationName = "registerCertificateForTMSRA")
    public RAServiceResp registerCertificateForTMSRA(@WebParam(name = "registerCertificateForTMSRA") RAServiceReq raServiceReq) {
        ConnectDatabase db = new ConnectDatabase();
        RAServiceResp raServiceResp = new RAServiceResp();
        ObjectMapper objectMapper;
        int[] System_Log_ID = new int[1];
        String[] System_Log_BillCode = new String[1];
        String sIP_Request;
        String sFunctionWS = Definitions.CONFIG_LOG_FUNCTIONALITY_API_REGISTRATION_CERTIFICATION;
        String sTOKEN_SN = "";
        try {
            sIP_Request = getIPAddress();
            String pCERTIFICATION_ATTR_TYPE_CODE = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION);
            objectMapper = new ObjectMapper();
            boolean autoApproveCAServer = false;
            boolean autoApproveCAClient = raServiceReq.approveEnabled;
            String sSOAP_WS = "";
            String sCERT_POLICY_PROPERTIES = "";
            String sCERT_PROFILE_PROPERTIES = "";
            String sFUNCTIONALTITY_PROPERTIES = "";
            String sIP_ADDRESS_PROPERTIES = "";
            String pApproveCAUser = "";
            String strDNSName = "";
            GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
            db.S_BO_GENERAL_POLICY_LIST(String.valueOf(raServiceReq.language), rsPolicy);
            //<editor-fold defaultstate="collapsed" desc="### Authentication Request">
            if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username))) {
                raServiceReq.beneficiaryUser = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser);
                raServiceReq.beneficiaryBranch = EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch);
                // GET SECURITY_PROPERTIES FROM BRANCH
                BRANCH[][] rsBranch = new BRANCH[1][];
                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.credentialData.username), rsBranch);
                if (rsBranch[0].length > 0) {
                    sSOAP_WS = EscapeUtils.CheckTextNull(rsBranch[0][0].SOAP_SECURITY_PROPERTIES);
                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                    sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                    sFUNCTIONALTITY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].FUNCTIONALTITY_PROPERTIES);
                    sIP_ADDRESS_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].IP_ADDRESS_PROPERTIES);
                    if ("".equals(sSOAP_WS) || "".equals(sFUNCTIONALTITY_PROPERTIES) || "".equals(sCERT_PROFILE_PROPERTIES)
                        || "".equals(sIP_ADDRESS_PROPERTIES) || "".equals(sCERT_POLICY_PROPERTIES)) {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                    } else {
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                    }
                    if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                        autoApproveCAServer = CommonFunction.getApproveEnabledCert(sCERT_POLICY_PROPERTIES);
                        pApproveCAUser = CommonFunction.getApproveCAUserCert(sCERT_POLICY_PROPERTIES);
                        raServiceReq.approveUser = pApproveCAUser;
                        //<editor-fold defaultstate="collapsed" desc="### CHECK BRANCH and USER">
                        if (!"".equals(raServiceReq.beneficiaryBranch)) {
                            UserInfo[][] rsUserBranch;
                            int[] pResonseUser;
                            String sCreateAgencyDefault = "0";
                            String sCreateUserDefault = "0";
                            String sValueUserBranch = "";
                            String sUserPassDefault = "";
                            String sBranchPolicyProper = "";
                            if (rsPolicy[0].length > 0) {
                                for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_API_AUTOCREATE_BENEFICIARY_BRANCH)) {
                                        sCreateAgencyDefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_API_AUTOCREATE_BENEFICIARY_USER)) {
                                        sCreateUserDefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_API_DEFAULT_INFO_BENEFICIARY_USER)) {
                                        sValueUserBranch = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DEFAULT_CERTIFICATION_PROFILE_PROPERTIES_FOR_BRANCH_ROLE)) {
                                        sBranchPolicyProper = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DEFAULT_PASSWORD_ACCOUNT)) {
                                        sUserPassDefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                }
                            }
                            rsUserBranch = new UserInfo[1][];
                            pResonseUser = new int[1];
                            db.S_BO_API_GET_USERNAME_BY_BRANCH_CODE(raServiceReq.beneficiaryBranch, pResonseUser, rsUserBranch);
                            if(pResonseUser[0] == 0 && rsUserBranch[0].length > 0) {
                                if (!"".equals(raServiceReq.beneficiaryUser)) {
                                    String sResponseCheckBranch = db.S_BO_API_CHECK_USERNAME_AND_BRANCH_CODE(raServiceReq.beneficiaryBranch, raServiceReq.beneficiaryUser);
                                    if(!"0".equals(sResponseCheckBranch)) {
                                        rsUserBranch = new UserInfo[1][];
                                        db.S_BO_API_USER_DETAIL(raServiceReq.beneficiaryUser, raServiceReq.language, rsUserBranch);
                                        if(rsUserBranch != null && rsUserBranch[0].length > 0) {
                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                                        } else {
                                            if("1".equals(sCreateUserDefault)) {
                                                String userPhone = "";
                                                String userEmail = "";
                                                String userRemark = "";
                                                String userRole = Definitions.CONFIG_ROLE_CODE_AGENT_ADMIN;
                                                JSON_USER_BRANCH_DEFAULT[][] rsCertType = new JSON_USER_BRANCH_DEFAULT[1][];
                                                CommonFunction.getJsonUserBranchDefault(sValueUserBranch, rsCertType);
                                                for(JSON_USER_BRANCH_DEFAULT rsCertType1 : rsCertType[0])
                                                {
                                                    if(rsCertType1.ATTRIBUTE_TYPE.equals(Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_USER_DEFAULT_INFO_TAG))
                                                    {
                                                        userPhone = rsCertType1.MSISDN;
                                                        userEmail = rsCertType1.EMAIL;
                                                        userRemark = rsCertType1.REMARK;
                                                    }
                                                }
                                                String[] pRESPONSE_CODE = new String[1];
                                                int[] pUSER_ID = new int[1];
                                                db.S_BO_API_USER_INSERT(raServiceReq.beneficiaryUser, sUserPassDefault, userRemark, 
                                                    userRole, raServiceReq.beneficiaryBranch,
                                                    userEmail, userPhone, raServiceReq.approveUser, pRESPONSE_CODE, pUSER_ID);
                                                // tao user
                                            } else {
                                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_BENEFICIARY_BRANCH_INVALID;
                                            }
                                        }
                                    }
                                } else {
                                    if(!"".equals(rsUserBranch[0][0].userName)) {
                                        raServiceReq.beneficiaryUser = rsUserBranch[0][0].userName;
                                    } else {
                                        if("1".equals(sCreateUserDefault)) {
                                            String userPhone = "";
                                            String userEmail = "";
                                            String userRemark = "";
                                            String userRole = Definitions.CONFIG_ROLE_CODE_AGENT_ADMIN;
                                            JSON_USER_BRANCH_DEFAULT[][] rsCertType = new JSON_USER_BRANCH_DEFAULT[1][];
                                            CommonFunction.getJsonUserBranchDefault(sValueUserBranch, rsCertType);
                                            for(JSON_USER_BRANCH_DEFAULT rsCertType1 : rsCertType[0])
                                            {
                                                if(rsCertType1.ATTRIBUTE_TYPE.equals(Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_USER_DEFAULT_INFO_TAG))
                                                {
                                                    userPhone = rsCertType1.MSISDN;
                                                    userEmail = rsCertType1.EMAIL;
                                                    userRemark = rsCertType1.REMARK;
                                                }
                                            }
                                            String[] pRESPONSE_CODE = new String[1];
                                            int[] pUSER_ID = new int[1];
                                            raServiceReq.beneficiaryUser = raServiceReq.beneficiaryBranch.toLowerCase() + "_admin";
                                            db.S_BO_API_USER_INSERT(raServiceReq.beneficiaryUser, sUserPassDefault, userRemark, 
                                                userRole, raServiceReq.beneficiaryBranch,
                                                userEmail, userPhone, raServiceReq.approveUser, pRESPONSE_CODE, pUSER_ID);
                                            // tao user
                                        } else {
                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_BENEFICIARY_BRANCH_INVALID;
                                        }
                                    }
                                }
                            } else {
                                if("1".equals(sCreateAgencyDefault)) {
                                    if (!"".equals(raServiceReq.beneficiaryUser)) {
                                        rsUserBranch = new UserInfo[1][];
                                        db.S_BO_API_USER_LIST(raServiceReq.beneficiaryUser, "", "", raServiceReq.language, rsUserBranch);
                                        if(rsUserBranch[0].length > 0) {
                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_BENEFICIARY_BRANCH_INVALID;
                                        } else {
                                            if("1".equals(sCreateUserDefault)) {
                                                String branchPhone = "";
                                                String branchEmail = "";
                                                String branchRemark = "";
                                                String branchParentName = "";
                                                String branchProvince = "";
                                                String branchDISCOUNT_RATE_PROFILE = "FEDERAL";
                                                String branchRole = "FEDERAL";
                                                String userPhone = "";
                                                String userEmail = "";
                                                String userRemark = "";
                                                String userRole = Definitions.CONFIG_ROLE_CODE_AGENT_ADMIN;
                                                JSON_USER_BRANCH_DEFAULT[][] rsCertType = new JSON_USER_BRANCH_DEFAULT[1][];
                                                CommonFunction.getJsonUserBranchDefault(sValueUserBranch, rsCertType);
                                                for(JSON_USER_BRANCH_DEFAULT rsCertType1 : rsCertType[0])
                                                {
                                                    if(rsCertType1.ATTRIBUTE_TYPE.equals(Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_BRANCH_DEFAULT_INFO_TAG))
                                                    {
                                                        branchPhone = rsCertType1.MSISDN;
                                                        branchEmail = rsCertType1.EMAIL;
                                                        branchRemark = rsCertType1.REMARK;
                                                        branchParentName = rsCertType1.PARENT_NAME;
                                                        branchProvince = rsCertType1.PROVINCE_NAME;
                                                    }
                                                    if(rsCertType1.ATTRIBUTE_TYPE.equals(Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_USER_DEFAULT_INFO_TAG))
                                                    {
                                                        userPhone = rsCertType1.MSISDN;
                                                        userEmail = rsCertType1.EMAIL;
                                                        userRemark = rsCertType1.REMARK;
                                                    }
                                                }
                                                int[] pBRANCH_ID = new int[1];
                                                String sParam = db.S_BO_API_BRANCH_INSERT(raServiceReq.beneficiaryBranch, branchProvince,
                                                    branchRemark, branchRemark, branchParentName, raServiceReq.approveUser,
                                                    EscapeUtils.escapeHtml(branchPhone), "", EscapeUtils.escapeHtml(branchEmail),
                                                    "", "", "", null, branchDISCOUNT_RATE_PROFILE, sBranchPolicyProper, branchRole, pBRANCH_ID);
                                                if("0".equals(sParam))
                                                {
                                                    String[] pRESPONSE_CODE = new String[1];
                                                    int[] pUSER_ID = new int[1];
                                                    db.S_BO_API_USER_INSERT(raServiceReq.beneficiaryUser, sUserPassDefault, userRemark, 
                                                        userRole, raServiceReq.beneficiaryBranch,
                                                        userEmail, userPhone, raServiceReq.approveUser, pRESPONSE_CODE, pUSER_ID);
                                                }
                                                // tao branch (dc truyen vao)
                                                // tao user (dc truyen vao)
                                            } else {
                                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                                            }
                                        }
                                    } else {
                                        if("1".equals(sCreateUserDefault)) {
                                            String branchPhone = "";
                                            String branchEmail = "";
                                            String branchRemark = "";
                                            String branchParentName = "";
                                            String branchProvince = "";
                                            String branchDISCOUNT_RATE_PROFILE = "FEDERAL";
                                            String branchRole = "FEDERAL";
                                            String userPhone = "";
                                            String userEmail = "";
                                            String userRemark = "";
                                            String userRole = Definitions.CONFIG_ROLE_CODE_AGENT_ADMIN;
                                            JSON_USER_BRANCH_DEFAULT[][] rsCertType = new JSON_USER_BRANCH_DEFAULT[1][];
                                            CommonFunction.getJsonUserBranchDefault(sValueUserBranch, rsCertType);
                                            for(JSON_USER_BRANCH_DEFAULT rsCertType1 : rsCertType[0])
                                            {
                                                if(rsCertType1.ATTRIBUTE_TYPE.equals(Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_BRANCH_DEFAULT_INFO_TAG))
                                                {
                                                    branchPhone = rsCertType1.MSISDN;
                                                    branchEmail = rsCertType1.EMAIL;
                                                    branchRemark = rsCertType1.REMARK;
                                                    branchParentName = rsCertType1.PARENT_NAME;
                                                    branchProvince = rsCertType1.PROVINCE_NAME;
                                                }
                                                if(rsCertType1.ATTRIBUTE_TYPE.equals(Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_USER_DEFAULT_INFO_TAG))
                                                {
                                                    userPhone = rsCertType1.MSISDN;
                                                    userEmail = rsCertType1.EMAIL;
                                                    userRemark = rsCertType1.REMARK;
                                                }
                                            }
                                            int[] pBRANCH_ID = new int[1];
                                            String sParam = db.S_BO_API_BRANCH_INSERT(raServiceReq.beneficiaryBranch, branchProvince,
                                                branchRemark, branchRemark, branchParentName, raServiceReq.approveUser,
                                                EscapeUtils.escapeHtml(branchPhone), "", EscapeUtils.escapeHtml(branchEmail),
                                                "", "", "", null, branchDISCOUNT_RATE_PROFILE, sBranchPolicyProper, branchRole, pBRANCH_ID);
                                            if("0".equals(sParam))
                                            {
                                                raServiceReq.beneficiaryUser = raServiceReq.beneficiaryBranch.toLowerCase() + "_admin";
                                                String[] pRESPONSE_CODE = new String[1];
                                                int[] pUSER_ID = new int[1];
                                                db.S_BO_API_USER_INSERT(raServiceReq.beneficiaryUser, sUserPassDefault, userRemark, 
                                                    userRole, raServiceReq.beneficiaryBranch,
                                                    userEmail, userPhone, raServiceReq.approveUser, pRESPONSE_CODE, pUSER_ID);
                                            }
                                            // tao branch (dc truyen vao)
                                            // tao user (tu gen)
                                        } else {
                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_USERNAME_BY_INVALID;
                                        }
                                    }
                                } else {
                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_BENEFICIARY_BRANCH_INVALID;
                                }
                            }
//                            if ("".equals(EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser))) {
//                                rsUserBranch = new UserInfo[1][];
//                                pResonseUser = new int[1];
//                                db.S_BO_API_GET_USERNAME_BY_BRANCH_CODE(EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch), pResonseUser, rsUserBranch);
//                                if(pResonseUser[0] == 0) {
//                                    if(rsUserBranch[0].length > 0) {
//                                        raServiceReq.beneficiaryUser = rsUserBranch[0][0].userName;
//                                    } else {
//                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_BENEFICIARY_BRANCH_INVALID;
//                                    }
////                                    raServiceReq.beneficiaryUser = CommonFunction.getBeneficiaryUserCert(sCERT_POLICY_PROPERTIES);
//                                } else {
//                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_BENEFICIARY_BRANCH_INVALID;
//                                }
//                            } else {
//                                String sResponseCheckBranch = db.S_BO_API_CHECK_USERNAME_AND_BRANCH_CODE(EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch), EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser));
//                                if(!"0".equals(sResponseCheckBranch)) {
//                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_BENEFICIARY_BRANCH_INVALID;
//                                }
//                            }
                        } else {
                            String sAgencyDefault = "";
                            if (rsPolicy[0].length > 0) {
                                for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_API_DEFAULT_BENEFICIARY_BRANCH)) {
                                        sAgencyDefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        break;
                                    }
                                }
                            }
                            if(!"".equals(sAgencyDefault)) {
                                raServiceReq.beneficiaryBranch = sAgencyDefault;
                                UserInfo[][] rsUserBranch = new UserInfo[1][];
                                int[] pResonseUser = new int[1];
                                db.S_BO_API_GET_USERNAME_BY_BRANCH_CODE(EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch), pResonseUser, rsUserBranch);
                                if(pResonseUser[0] == 0 && rsUserBranch[0].length > 0) {
                                    raServiceReq.beneficiaryUser = rsUserBranch[0][0].userName;
                                } else {
                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_BENEFICIARY_BRANCH_INVALID;
                                }
                            } else {
                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_BENEFICIARY_BRANCH_INVALID;
                            }
                        }
                        //</editor-fold>
                    }
                } else {
                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_NO_CREDENTIAL;
                }
            } else {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_FAIL_CREDENTIAL;
            }
            //</editor-fold>

            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                raServiceResp = HandShaking.checkCredentialData(raServiceReq.credentialData, sSOAP_WS);
                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                    RAServiceReq raReqTemp = new RAServiceReq();
                    raReqTemp.certificateComponentInfo = raServiceReq.certificateComponentInfo;
                    raReqTemp.beneficiaryUser = raServiceReq.beneficiaryUser;
                    raReqTemp.emailContact = raServiceReq.emailContact;
                    raReqTemp.phoneContact = raServiceReq.phoneContact;
                    raReqTemp.formFactorCode = raServiceReq.formFactorCode;
                    raReqTemp.activationCodeEnabled = raServiceReq.activationCodeEnabled;
                    raReqTemp.beneficiaryBranch = raServiceReq.beneficiaryBranch;
                    raReqTemp.certificateOwnerID = raServiceReq.certificateOwnerID;
//                    raReqTemp.certificateAuthorityCode = raServiceReq.certificateAuthorityCode;
//                    raReqTemp.certificatePurposeCode = raServiceReq.certificatePurposeCode;
                    raReqTemp.certificateProfileCode = raServiceReq.certificateProfileCode;
                    raReqTemp.backupKeyEnabled = raServiceReq.backupKeyEnabled;
                    raReqTemp.certificateNotificationEnabled = raServiceReq.certificateNotificationEnabled;
                    raReqTemp.p12EmailEnabled = raServiceReq.p12EmailEnabled;
                    raReqTemp.approveEnabled = raServiceReq.approveEnabled;
                    raReqTemp.language = raServiceReq.language;
                    raReqTemp.csr = raServiceReq.csr;
                    raReqTemp.credentialData = raServiceReq.credentialData;
                    db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_API_RA, Definitions.CONFIG_LOG_SOURCE_API_RA, sTOKEN_SN, "",
                        sFunctionWS, objectMapper.writeValueAsString(raReqTemp), raServiceReq.beneficiaryUser,
                        System_Log_ID, sIP_Request, System_Log_BillCode);
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
                        ArrayList<CERTIFICATION_POLICY_DATA> tempProfileList;
                        //<editor-fold defaultstate="collapsed" desc="### CHECK PROFILE VALID">
                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERT_PROFILE_INVALID;
                        CERTIFICATION_PROFILE[][] resProfileDB;
                        resProfileDB = new CERTIFICATION_PROFILE[1][];
                        db.S_BO_API_CERTIFICATION_PROFILE_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.certificateProfileCode), resProfileDB);
                        if(resProfileDB[0].length > 0)
                        {
                            raServiceReq.certificateAuthorityCode = resProfileDB[0][0].CERTIFICATION_AUTHORITY_NAME;
                            raServiceReq.certificatePurposeCode = resProfileDB[0][0].CERTIFICATION_PURPOSE_NAME;
                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                        }
                        if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS)
                        {
                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERT_PROFILE_INVALID;
                            boolean accessProfileAll = CommonFunction.checkAPIAccessProfileAll(sCERT_PROFILE_PROPERTIES);
                            if (accessProfileAll == true) {
                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                            } else {
                                tempProfileList = new ArrayList<>();
                                //<editor-fold defaultstate="collapsed" desc="### GET PROFILE LIST ACCESS">
                                CERTIFICATION_POLICY_DATA[][] resPolicyData_Old = new CERTIFICATION_POLICY_DATA[1][];
                                CommonFunction.getProfileCertList(sCERT_PROFILE_PROPERTIES, resPolicyData_Old);
                                for(CERTIFICATION_POLICY_DATA resPolicyCertData_Old1 : resPolicyData_Old[0])
                                {
                                    if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                    {
                                        resProfileDB = new CERTIFICATION_PROFILE[1][];
                                        db.S_BO_API_CERTIFICATION_PROFILE_GET_INFO(EscapeUtils.CheckTextNull(resPolicyCertData_Old1.name), resProfileDB);
                                        if(resProfileDB[0].length > 0)
                                        {
                                            CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                            itemProfileAccess.name = resProfileDB[0][0].NAME;
                                            itemProfileAccess.certificateAuthority = resProfileDB[0][0].CERTIFICATION_AUTHORITY_NAME;
                                            itemProfileAccess.certificatePurpose = resProfileDB[0][0].CERTIFICATION_PURPOSE_NAME;
                                            itemProfileAccess.remark = resPolicyCertData_Old1.remark;
                                            itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
                                            itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST;
                                            tempProfileList.add(itemProfileAccess);
                                        }
                                    }
                                }
                                //</editor-fold>

    //                            CERTIFICATION_POLICY_DATA[][] resPolicyData1 = new CERTIFICATION_POLICY_DATA[1][];
    //                            CommonFunction.getProfileCertNewList(sCERT_POLICY_PROPERTIES, resPolicyData1);
                                if(tempProfileList.size() > 0)
                                {
                                    for (CERTIFICATION_POLICY_DATA tempProfileList1 : tempProfileList)
                                    {
                                        if(tempProfileList1.certificateAuthority.equals(raServiceReq.certificateAuthorityCode)
                                            && tempProfileList1.certificatePurpose.equals(raServiceReq.certificatePurposeCode)
                                            && tempProfileList1.name.equals(EscapeUtils.CheckTextNull(raServiceReq.certificateProfileCode)))
                                        {
    //                                    if (EscapeUtils.CheckTextNull(rsPolicyProperties.name).equals(EscapeUtils.CheckTextNull(raServiceReq.certificateProfileCode))) {
                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        //</editor-fold>

                        if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                            //<editor-fold defaultstate="collapsed" desc="### POLICY CHECK PHONE, EMAIL CUSTORMER, DISCOUNT RATE">
                            String sRegexPolicy = "";
                            String sDiscountRateOption = "0";
                            if (rsPolicy[0].length > 0) {
                                for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL)) {
                                        sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION)) {
                                        sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                    }
                                }
                            }
                            String sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_PHONE);
                            if ("".equals(sREGEX_PHONE.trim())) {
                                sREGEX_PHONE = Definitions.CONFIG_DEFAULT_VALUE_REGEX_PHONE;
                            }
                            if (CommonFunction.regexPhoneValid(EscapeUtils.CheckTextNull(raServiceReq.phoneContact), sREGEX_PHONE) == false) {
                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CUSTOMER_PHONE_INVALID;
                            } else {
                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                            }
                            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                                String sREGEX_EMAIL = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_EMAIL);
                                if ("".equals(sREGEX_EMAIL.trim())) {
                                    sREGEX_EMAIL = Definitions.CONFIG_DEFAULT_VALUE_REGEX_EMAIL;
                                }
                                if (CommonFunction.regexEmailValid(EscapeUtils.CheckTextNull(raServiceReq.emailContact), sREGEX_EMAIL) == false) {
                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CUSTOMER_EMAIL_INVALID;
                                } else {
                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                                }
                            }
                            //</editor-fold>

                            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                                //<editor-fold defaultstate="collapsed" desc="### DNS List for SSL Check">
                                if(EscapeUtils.CheckTextNull(raReqTemp.formFactorCode).equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_SOFT_TOKEN))
                                {
                                    if(EscapeUtils.CheckTextNull(raReqTemp.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_SSL))
                                    {
                                        String sDNSValue = EscapeUtils.CheckTextNull(raServiceReq.dnsName);
                                        if("".equals(sDNSValue))
                                        {
                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SSL_DNS_INVALID;
                                        } else {
                                            List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes = new ArrayList<>();
                                            CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                            attribute.setKey(CERTIFICATION_PROPERTIES_JSON.Attribute.SUBJECT_ALT_NAMES);
                                            attribute.setValue(sDNSValue);
                                            attributes.add(attribute);
                                            strDNSName = "{\"attributes\":" + objectMapper.writeValueAsString(attributes) + "}";
                                        }
                                        if("".equals(strDNSName))
                                        {
                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SSL_DNS_INVALID;
                                        }
                                    }
                                }
                                //</editor-fold>
                                    
                                if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                                    if (raServiceReq.certificateComponentInfo.length > 0) {
                                        int intOUCheckClient = 0;
                                        for (CertificateComponentInfo certComponentInfo1 : raServiceReq.certificateComponentInfo) {
                                            if (EscapeUtils.CheckTextNull(certComponentInfo1.code).equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU)) {
                                                intOUCheckClient = intOUCheckClient + 1;
                                            }
                                        }
                                        CertificateProfileInfo[][] certProfileInfo = new CertificateProfileInfo[1][];
                                        db.S_BO_API_CERTIFICATION_PROFILE_GET_PROPERTIES(EscapeUtils.CheckTextNull(raServiceReq.certificateAuthorityCode),
                                                EscapeUtils.CheckTextNull(raServiceReq.certificateProfileCode),
                                                EscapeUtils.CheckTextNull(raServiceReq.formFactorCode), certProfileInfo);
                                        if (certProfileInfo[0].length > 0) {
                                            boolean sCheckRequire = true;
                                            //<editor-fold defaultstate="collapsed" desc="### Update isreqiure, commonname certificateComponentInfo">
                                            String sPropertiesCert = EscapeUtils.CheckTextNull(certProfileInfo[0][0].certificateProfileProperties);
                                            if (!"".equals(sPropertiesCert)) {
                                                CertificateComponentInfo[][] infoCompNew = new CertificateComponentInfo[1][];
                                                ArrayList<CertificateComponentInfo> listCompNew = new ArrayList<>();
                                                CERTIFICATION_TYPE_COMPONENT[][] resProfileData = new CERTIFICATION_TYPE_COMPONENT[1][];
                                                CommonFunction.getJsonComponentForCert(sPropertiesCert, resProfileData);
                                                int intCompOUSrv = 0;
                                                for(CERTIFICATION_TYPE_COMPONENT resProfileData1 : resProfileData[0])
                                                {
                                                    if(EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID))
                                                    {
                                                        boolean hasComponent = false;
                                                        for (CertificateComponentInfo certComponentInfo1 : raServiceReq.certificateComponentInfo) {
                                                            String sCodeInfoOld = EscapeUtils.CheckTextNull(resProfileData1.name);
                                                            if (sCodeInfoOld.equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE)) {
                                                                sCodeInfoOld = sCodeInfoOld.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE, Definitions.CONFIG_COMPONENT_DN_TAG_UID);
                                                            }
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(EscapeUtils.CheckTextNull(certComponentInfo1.prefix)))
                                                            {
                                                                hasComponent = true;
                                                                CertificateComponentInfo itemCompNew = new CertificateComponentInfo();
                                                                itemCompNew.code = sCodeInfoOld;
                                                                itemCompNew.commomNameType = EscapeUtils.CheckTextNull(resProfileData1.commomNameType);
                                                                itemCompNew.requireEnabled = resProfileData1.require;
                                                                itemCompNew.attributeType = EscapeUtils.CheckTextNull(resProfileData1.attributeType);
                                                                itemCompNew.value = EscapeUtils.CheckTextNull(certComponentInfo1.value);
                                                                itemCompNew.prefix = EscapeUtils.CheckTextNull(resProfileData1.prefix);
                                                                listCompNew.add(itemCompNew);
                                                                break;
                                                            }
                                                        }
                                                        if(hasComponent == false)
                                                        {
                                                            CertificateComponentInfo itemCompNew = new CertificateComponentInfo();
                                                            itemCompNew.code = EscapeUtils.CheckTextNull(resProfileData1.name);
                                                            itemCompNew.commomNameType = EscapeUtils.CheckTextNull(resProfileData1.commomNameType);
                                                            itemCompNew.requireEnabled = resProfileData1.require;
                                                            itemCompNew.attributeType = EscapeUtils.CheckTextNull(resProfileData1.attributeType);
                                                            itemCompNew.value = "";
                                                            itemCompNew.prefix = EscapeUtils.CheckTextNull(resProfileData1.prefix);
                                                            listCompNew.add(itemCompNew);
                                                        }
                                                    } else {
                                                        if(!EscapeUtils.CheckTextNull(resProfileData1.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN))
                                                        {
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU)) {
                                                                boolean hasComponent = false;
                                                                intCompOUSrv = intCompOUSrv + 1;
                                                                int intCompOUClient = 0;
                                                                for (CertificateComponentInfo certComponentInfo1 : raServiceReq.certificateComponentInfo) {
                                                                    if (EscapeUtils.CheckTextNull(certComponentInfo1.code).equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU)) {
                                                                        hasComponent = true;
                                                                        intCompOUClient = intCompOUClient + 1;
                                                                        if(intCompOUSrv == intCompOUClient) {
                                                                            CertificateComponentInfo itemCompNew = new CertificateComponentInfo();
                                                                            itemCompNew.code = EscapeUtils.CheckTextNull(resProfileData1.name);
                                                                            itemCompNew.commomNameType = EscapeUtils.CheckTextNull(resProfileData1.commomNameType);
                                                                            itemCompNew.requireEnabled = resProfileData1.require;
                                                                            itemCompNew.attributeType = EscapeUtils.CheckTextNull(resProfileData1.attributeType);
                                                                            itemCompNew.value = EscapeUtils.CheckTextNull(certComponentInfo1.value);
                                                                            itemCompNew.prefix = EscapeUtils.CheckTextNull(resProfileData1.prefix);
                                                                            listCompNew.add(itemCompNew);
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                                if(hasComponent == false) {
                                                                    CertificateComponentInfo itemCompNew = new CertificateComponentInfo();
                                                                    itemCompNew.code = EscapeUtils.CheckTextNull(resProfileData1.name);
                                                                    itemCompNew.commomNameType = EscapeUtils.CheckTextNull(resProfileData1.commomNameType);
                                                                    itemCompNew.requireEnabled = resProfileData1.require;
                                                                    itemCompNew.attributeType = EscapeUtils.CheckTextNull(resProfileData1.attributeType);
                                                                    itemCompNew.value = "";
                                                                    itemCompNew.prefix = EscapeUtils.CheckTextNull(resProfileData1.prefix);
                                                                    listCompNew.add(itemCompNew);
                                                                }
                                                            } else {
                                                                boolean hasComponent = false;
                                                                for (CertificateComponentInfo certComponentInfo1 : raServiceReq.certificateComponentInfo) {
                                                                    if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(EscapeUtils.CheckTextNull(certComponentInfo1.code))) {
                                                                        hasComponent = true;
                                                                        CertificateComponentInfo itemCompNew = new CertificateComponentInfo();
                                                                        itemCompNew.code = EscapeUtils.CheckTextNull(resProfileData1.name);
                                                                        itemCompNew.commomNameType = EscapeUtils.CheckTextNull(resProfileData1.commomNameType);
                                                                        itemCompNew.requireEnabled = resProfileData1.require;
                                                                        itemCompNew.attributeType = EscapeUtils.CheckTextNull(resProfileData1.attributeType);
                                                                        itemCompNew.value = EscapeUtils.CheckTextNull(certComponentInfo1.value);
                                                                        itemCompNew.prefix = EscapeUtils.CheckTextNull(resProfileData1.prefix);
                                                                        listCompNew.add(itemCompNew);
                                                                        break;
                                                                    }
                                                                }
                                                                if(hasComponent == false)
                                                                {
                                                                    CertificateComponentInfo itemCompNew = new CertificateComponentInfo();
                                                                    itemCompNew.code = EscapeUtils.CheckTextNull(resProfileData1.name);
                                                                    itemCompNew.commomNameType = EscapeUtils.CheckTextNull(resProfileData1.commomNameType);
                                                                    itemCompNew.requireEnabled = resProfileData1.require;
                                                                    itemCompNew.attributeType = EscapeUtils.CheckTextNull(resProfileData1.attributeType);
                                                                    itemCompNew.value = "";
                                                                    itemCompNew.prefix = EscapeUtils.CheckTextNull(resProfileData1.prefix);
                                                                    listCompNew.add(itemCompNew);
                                                                }
                                                            }
                                                        } else {
                                                            boolean hasComponent = false;
                                                            for (CertificateComponentInfo certComponentInfo1 : raServiceReq.certificateComponentInfo) {
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(EscapeUtils.CheckTextNull(certComponentInfo1.code))) {
                                                                    hasComponent = true;
                                                                    CertificateComponentInfo itemCompNew = new CertificateComponentInfo();
                                                                    itemCompNew.code = EscapeUtils.CheckTextNull(resProfileData1.name);
                                                                    itemCompNew.commomNameType = EscapeUtils.CheckTextNull(resProfileData1.commomNameType);
                                                                    itemCompNew.requireEnabled = resProfileData1.require;
                                                                    itemCompNew.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN;
                                                                    itemCompNew.value = EscapeUtils.CheckTextNull(certComponentInfo1.value);
                                                                    itemCompNew.prefix = EscapeUtils.CheckTextNull(resProfileData1.prefix);
                                                                    listCompNew.add(itemCompNew);
                                                                    break;
                                                                }
                                                            }
                                                            if(hasComponent == false)
                                                            {
                                                                CertificateComponentInfo itemCompNew = new CertificateComponentInfo();
                                                                itemCompNew.code = EscapeUtils.CheckTextNull(resProfileData1.name);
                                                                itemCompNew.commomNameType = EscapeUtils.CheckTextNull(resProfileData1.commomNameType);
                                                                itemCompNew.requireEnabled = resProfileData1.require;
                                                                itemCompNew.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN;
                                                                itemCompNew.value = "";
                                                                itemCompNew.prefix = EscapeUtils.CheckTextNull(resProfileData1.prefix);
                                                                listCompNew.add(itemCompNew);
                                                            }
                                                        }
                                                    }
                                                }
                                                infoCompNew[0] = new CertificateComponentInfo[listCompNew.size()];
                                                infoCompNew[0] = listCompNew.toArray(infoCompNew[0]);
                                                raServiceReq.certificateComponentInfo = infoCompNew[0];
                                                CommonFunction.LogDebugString(log, "intCompOUSrv - OU", String.valueOf(intCompOUSrv));
                                                CommonFunction.LogDebugString(log, "intCompOUClient - OU", String.valueOf(intOUCheckClient));
                                                if(intCompOUSrv != intOUCheckClient) {
                                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_COMPONENT_CERT_INVALID;
                                                }
                                                
                                                /*for (CertificateComponentInfo certComponentInfo1 : raServiceReq.certificateComponentInfo) {
                                                    if (certComponentInfo1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE)) {
                                                        certComponentInfo1.code = certComponentInfo1.code.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE, Definitions.CONFIG_COMPONENT_DN_TAG_UID);
                                                    }
                                                    CERTIFICATION_TYPE_COMPONENT[][] resProfileData = new CERTIFICATION_TYPE_COMPONENT[1][];
                                                    CommonFunction.getJsonComponentForCert(sPropertiesCert, resProfileData);
                                                    for(CERTIFICATION_TYPE_COMPONENT resProfileData1 : resProfileData[0])
                                                    {
                                                        if(EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID))
                                                        {
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(certComponentInfo1.prefix)) {
                                                                certComponentInfo1.commomNameType = EscapeUtils.CheckTextNull(resProfileData1.commomNameType);
                                                                certComponentInfo1.requireEnabled = resProfileData1.require;
                                                                certComponentInfo1.attributeType = EscapeUtils.CheckTextNull(resProfileData1.attributeType);
                                                            }
                                                        } else {
                                                            if(!EscapeUtils.CheckTextNull(resProfileData1.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN)) {
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(certComponentInfo1.code)) {
                                                                    certComponentInfo1.commomNameType = EscapeUtils.CheckTextNull(resProfileData1.commomNameType);
                                                                    certComponentInfo1.requireEnabled = resProfileData1.require;
                                                                    certComponentInfo1.attributeType = EscapeUtils.CheckTextNull(resProfileData1.attributeType);
                                                                }
                                                            } else {
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(certComponentInfo1.code)) {
                                                                    certComponentInfo1.commomNameType = EscapeUtils.CheckTextNull(resProfileData1.commomNameType);
                                                                    certComponentInfo1.requireEnabled = resProfileData1.require;
                                                                    certComponentInfo1.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }*/
                                                
                                                    
                                                    // objectMapper = new ObjectMapper();
//                                                    CERTIFICATE_ATTRIBUTES proParse = objectMapper.readValue(sPropertiesCert, CERTIFICATE_ATTRIBUTES.class);
//                                                    for (int i = 0; i < proParse.getAttributes().size(); i++) {
//                                                        if (!proParse.getAttributes().get(i).getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_RADIOBUTTON)) {
//                                                            if (EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getName()).equals(certComponentInfo1.code)) {
//                                                                certComponentInfo1.commomNameType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getCommomNameType());
//                                                                certComponentInfo1.requireEnabled = proParse.getAttributes().get(i).isRequire();
//                                                                certComponentInfo1.attributeType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType());
//                                                            }
//                                                        } else {
//                                                            for (CERTIFICATE_ATTRIBUTES.Attribute attribute : proParse.getAttributes().get(i).getAttributes()) {
//                                                                if (EscapeUtils.CheckTextNull(attribute.getName()).equals(certComponentInfo1.code)) {
//                                                                    certComponentInfo1.commomNameType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getCommomNameType());
//                                                                    certComponentInfo1.requireEnabled = proParse.getAttributes().get(i).isRequire();
//                                                                    certComponentInfo1.attributeType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType());
//                                                                }
//                                                            }
//                                                        }
//                                                    }
                                            } else {
                                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_TEMPLATE_CERT_EMPTY;
                                            }
                                            //</editor-fold>

                                            //<editor-fold defaultstate="collapsed" desc="### TEMP Update isreqiure, commonname certificateComponentInfo">
    //                                        String sPropertiesCert = EscapeUtils.CheckTextNull(certProfileInfo[0][0].certificateProfileProperties);
    //                                        if (!"".equals(sPropertiesCert)) {
    //                                            objectMapper = new ObjectMapper();
    //                                            CERTIFICATE_ATTRIBUTES proParse = objectMapper.readValue(sPropertiesCert, CERTIFICATE_ATTRIBUTES.class);
    //                                            CertificateComponentInfo[][] rsComponentFromServer = new CertificateComponentInfo[1][];
    //                                            ArrayList<CertificateComponentInfo> tempListComServer = new ArrayList<>();
    //                                            int j = 1;
    //                                            for (int i = 0; i < proParse.getAttributes().size(); i++) {
    //                                                if (!proParse.getAttributes().get(i).getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_RADIOBUTTON)) {
    //                                                    CertificateComponentInfo mh = new CertificateComponentInfo();
    //                                                    mh.code = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getName());//.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID, Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE);
    //                                                    mh.prefix = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getPrefix());
    //                                                    mh.requireEnabled = proParse.getAttributes().get(i).isRequire();
    //                                                    mh.attributeType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType());
    //                                                    tempListComServer.add(mh);
    //                                                } else {
    //                                                    CertificateComponentInfo mh = new CertificateComponentInfo();
    //                                                    mh.requireEnabled = proParse.getAttributes().get(i).isRequire();
    //                                                    mh.attributeType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType());
    //                                                    mh.code = "";
    //                                                    mh.prefix = "";
    //                                                    mh.radio = new CertificateAttributesRadio[proParse.getAttributes().get(i).getAttributes().size()];
    //                                                    for (int n = 0; n < proParse.getAttributes().get(i).getAttributes().size(); n++) {
    //                                                        CertificateAttributesRadio radioChild = new CertificateAttributesRadio();
    //                                                        radioChild.code = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getName());//.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID, Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE);
    //                                                        radioChild.prefix = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getPrefix());
    //                                                        mh.radio[n] = radioChild;
    //                                                    }
    //                                                    tempListComServer.add(mh);
    //                                                }
    //                                            }
    //                                            rsComponentFromServer[0] = new CertificateComponentInfo[tempListComServer.size()];
    //                                            rsComponentFromServer[0] = tempListComServer.toArray(rsComponentFromServer[0]);
    //                                            
    //                                            for (CertificateComponentInfo rsComponentFromServer1 : rsComponentFromServer[0]) {
    //                                                for (CertificateComponentInfo certComponentInfo1 : raServiceReq.certificateComponentInfo) {
    //                                                    if (EscapeUtils.CheckTextNull(rsComponentFromServer1.code).equals(EscapeUtils.CheckTextNull(certComponentInfo1.code)))
    //                                                    {
    //                                                        
    //                                                    }
    //                                                }
    //                                            }
    //                                            
    //                                            
    //                                            for (CertificateComponentInfo certComponentInfo1 : raServiceReq.certificateComponentInfo) {
    //                                                if (certComponentInfo1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE)) {
    //                                                    certComponentInfo1.code = certComponentInfo1.code.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE, Definitions.CONFIG_COMPONENT_DN_TAG_UID);
    //                                                }
    //                                                objectMapper = new ObjectMapper();
    //                                                CERTIFICATE_ATTRIBUTES proParse = objectMapper.readValue(sPropertiesCert, CERTIFICATE_ATTRIBUTES.class);
    //                                                for (int i = 0; i < proParse.getAttributes().size(); i++) {
    //                                                    if (!proParse.getAttributes().get(i).getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_RADIOBUTTON)) {
    //                                                        if (EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getName()).equals(certComponentInfo1.code)) {
    //                                                            certComponentInfo1.commomNameType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getCommomNameType());
    //                                                            certComponentInfo1.requireEnabled = proParse.getAttributes().get(i).isRequire();
    //                                                            certComponentInfo1.attributeType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType());
    //                                                        }
    //                                                    } else {
    //                                                        for (CERTIFICATE_ATTRIBUTES.Attribute attribute : proParse.getAttributes().get(i).getAttributes()) {
    //                                                            if (EscapeUtils.CheckTextNull(attribute.getName()).equals(certComponentInfo1.name)) {
    //                                                                certComponentInfo1.commomNameType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getCommomNameType());
    //                                                                certComponentInfo1.requireEnabled = proParse.getAttributes().get(i).isRequire();
    //                                                                certComponentInfo1.attributeType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType());
    //                                                            }
    //                                                        }
    //                                                    }
    //                                                }
    //                                            }
    //                                        } else {
    //                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_TEMPLATE_CERT_EMPTY;
    //                                        }
                                            //</editor-fold>

                                            if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                                                String sCommomNameType = "";
                                                String sCompanyCompoment = "";
                                                String sDomainCompoment = "";
                                                String sPersonalCompoment = "";
                                                String sMSTCompoment = "";
                                                String sMNSCompoment = "";
                                                String sCMNDCompoment = "";
                                                String sDeviceUUID = "";
                                                String sHCCompoment = "";
                                                String sProvinceCodeComponent = "";
                                                String sProvinceNameComponent = "";
                                                String sDNResult = "";
                                                String sSANResult = "";
                                                //<editor-fold defaultstate="collapsed" desc="### Get info component from certificateComponentInfo">
                                                // CHECK REQUIRE FOR UID
                                                boolean hasCompanyUID = false;
                                                boolean hasPesonalUID = false;
                                                boolean hasCompanyValue = false;
                                                boolean hasPesonalValue = false;
                                                for (CertificateComponentInfo compCheckUID : raServiceReq.certificateComponentInfo) {
                                                    if (EscapeUtils.CheckTextNull(compCheckUID.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)) {
                                                        hasCompanyUID = true;
                                                        if (compCheckUID.requireEnabled == true) {
                                                            if (!"".equals(EscapeUtils.CheckTextNull(compCheckUID.value))) {
                                                                hasCompanyValue = true;
                                                                break;
                                                            } else {hasCompanyValue = false;}
                                                        } else {hasCompanyValue = true;}
                                                    }
                                                }
                                                for (CertificateComponentInfo compCheckUID : raServiceReq.certificateComponentInfo) {
                                                    if (EscapeUtils.CheckTextNull(compCheckUID.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)) {
                                                        hasPesonalUID = true;
                                                        if (compCheckUID.requireEnabled == true) {
                                                            if (!"".equals(EscapeUtils.CheckTextNull(compCheckUID.value))) {
                                                                hasPesonalValue = true;
                                                                break;
                                                            } else {hasPesonalValue = false;}
                                                        } else {hasPesonalValue = true;}
                                                    }
                                                }
                                                if(hasCompanyUID == true) {
                                                    if(hasCompanyValue == false) {
                                                        sCheckRequire = false;
                                                    }
                                                }
                                                if(hasPesonalUID == true) {
                                                    if(hasPesonalValue == false) {
                                                        sCheckRequire = false;
                                                    }
                                                }
                                                if (sCheckRequire == true) {
                                                    for (CertificateComponentInfo certComponentInfo2 : raServiceReq.certificateComponentInfo) {
                                                        if (!EscapeUtils.CheckTextNull(certComponentInfo2.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN))
                                                        {
                                                            // check require
                                                            if (certComponentInfo2.requireEnabled == true) {
                                                                if (!EscapeUtils.CheckTextNull(certComponentInfo2.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                                                        && !EscapeUtils.CheckTextNull(certComponentInfo2.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)) {
                                                                    if ("".equals(EscapeUtils.CheckTextNull(certComponentInfo2.value))) {
                                                                        sCheckRequire = false;
                                                                        break;
                                                                    }
                                                                } else {
                                                                }
                                                            }
                                                            // get city province name
                                                            if (EscapeUtils.CheckTextNull(certComponentInfo2.code).equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST)) {
                                                                sProvinceCodeComponent = EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                CityProvinceInfo[][] cityInfo = new CityProvinceInfo[1][];
                                                                db.S_BO_API_PROVINCE_LIST(sProvinceCodeComponent, raServiceReq.language, cityInfo);
                                                                if (cityInfo[0].length > 0) {
                                                                    certComponentInfo2.value = EscapeUtils.CheckTextNull(cityInfo[0][0].cityProvinceName);
                                                                    sProvinceNameComponent = EscapeUtils.CheckTextNull(cityInfo[0][0].cityProvinceName);
                                                                }
                                                            }
                                                            // get company, personal name
                                                            if (!"".equals(EscapeUtils.CheckTextNull(certComponentInfo2.value))) {
                                                                sDNResult += EscapeUtils.CheckTextNull(certComponentInfo2.code) + "=" + EscapeUtils.CheckTextNull(certComponentInfo2.prefix)
                                                                    + CommonFunction.replaceStringCharaterSpecialDN(EscapeUtils.CheckTextNull(certComponentInfo2.value), true, false) + ", ";
                                                            }
                                                            if (EscapeUtils.CheckTextNull(certComponentInfo2.code).equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN)) {
                                                                sCommomNameType = EscapeUtils.CheckTextNull(certComponentInfo2.commomNameType);
                                                                if (sCommomNameType.equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_COMPANY)) {
                                                                    sCompanyCompoment = EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                }
                                                                if (sCommomNameType.equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_PERSON)) {
                                                                    sPersonalCompoment = EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                }
                                                                if (sCommomNameType.equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_DOMAIN_NAME)) {
                                                                    sDomainCompoment = EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                }
                                                                if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_CLIENT))
                                                                {
                                                                    sDeviceUUID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_WEBCLIENT + EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                    sPersonalCompoment = "";
                                                                }
                                                                if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_SERVER))
                                                                {
                                                                    sDeviceUUID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_WEBSERVER + EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                    sPersonalCompoment = "";
                                                                }
                                                                if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_AUTH))
                                                                {
                                                                    sDeviceUUID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_X_ROAD_AUTH + EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                    sPersonalCompoment = "";
                                                                }
                                                                if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING_GOV))
                                                                {
                                                                    sDeviceUUID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CODE_SIGNING + EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                    sPersonalCompoment = "";
                                                                }
                                                                if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_SIGN))
                                                                {
                                                                    sDeviceUUID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_X_ROAD_SIGN + EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                    sPersonalCompoment = "";
                                                                }
                                                            }
            //                                                if (EscapeUtils.CheckTextNull(certComponentInfo2.code).equals(Definitions.CONFIG_COMPONENT_DN_TAG_Domain)) {
            //                                                    sDomainCompoment = EscapeUtils.CheckTextNull(certComponentInfo2.value);
            //                                                }
                                                            if (EscapeUtils.CheckTextNull(certComponentInfo2.code).equals(Definitions.CONFIG_COMPONENT_DN_TAG_O)) {
                                                                if (!"".equals(sCommomNameType)) {
                                                                    if (EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTTYPE_DESC_STAFF)
                                                                        || EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_CLIENT)
                                                                        || EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_SERVER)
                                                                        || EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_AUTH)
                                                                        || EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING_GOV)
                                                                        || EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_SIGN)) {
                                                                        sCompanyCompoment = EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                    }
                                                                }
                                                            }
                                                            if (EscapeUtils.CheckTextNull(certComponentInfo2.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY))
                                                            {
                                                                if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_DEVICE))
                                                                {
                                                                    if(!"".equals(EscapeUtils.CheckTextNull(certComponentInfo2.value)))
                                                                    {
                                                                        sDeviceUUID = EscapeUtils.CheckTextNull(certComponentInfo2.prefix) + EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                    }
                                                                } else {
                                                                    if (EscapeUtils.CheckTextNull(certComponentInfo2.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE + ":")) {
                                                                        sMSTCompoment = EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                    }
                                                                    if (EscapeUtils.CheckTextNull(certComponentInfo2.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE + ":")) {
                                                                        sMNSCompoment = EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                    }
                                                                }
                                                            }
                                                            if(EscapeUtils.CheckTextNull(certComponentInfo2.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL))
                                                            {
                                                                if (EscapeUtils.CheckTextNull(certComponentInfo2.prefix).equals(Definitions.CONFIG_PREFIX_PERSONAL_CODE + ":")) {
                                                                    sCMNDCompoment = EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                }
                                                                if (EscapeUtils.CheckTextNull(certComponentInfo2.prefix).equals(Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE + ":")) {
                                                                    sHCCompoment = EscapeUtils.CheckTextNull(certComponentInfo2.value);
                                                                }
                                                            }
                                                        } else {
                                                            // SAN
                                                            if(!EscapeUtils.CheckTextNull(raReqTemp.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_SSL))
                                                            {
                                                                // check require
                                                                if (certComponentInfo2.requireEnabled == true) {
                                                                    if ("".equals(EscapeUtils.CheckTextNull(certComponentInfo2.value))) {
                                                                        sCheckRequire = false;
                                                                        break;
                                                                    }
                                                                }
                                                                sSANResult = sSANResult + EscapeUtils.CheckTextNull(certComponentInfo2.value) + ";";
                                                            }
                                                        }
                                                    }
                                                    sSANResult = CommonFunction.subLastCharaterSemicolon(sSANResult);
                                                    if(!"".equals(sSANResult)) {
                                                        if(!EscapeUtils.CheckTextNull(raReqTemp.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_SSL)) {
                                                            List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes = new ArrayList<>();
                                                            CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                            attribute.setKey(CERTIFICATION_PROPERTIES_JSON.Attribute.SUBJECT_ALT_NAMES);
                                                            attribute.setValue(sSANResult);
                                                            attributes.add(attribute);
                                                            strDNSName = "{\"attributes\":" + objectMapper.writeValueAsString(attributes) + "}";
                                                            if("".equals(strDNSName))
                                                            {
                                                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SSL_DNS_INVALID;
                                                            }
                                                        }
                                                    }
                                                    if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTTYPE_DESC_STAFF))
                                                    {
                                                        if(!"".equals(sMSTCompoment) && !"".equals(sMNSCompoment))
                                                        {
                                                            sCheckRequire = false;
                                                        }
                                                        if("".equals(sMSTCompoment) && "".equals(sMNSCompoment))
                                                        {
                                                            sCheckRequire = false;
                                                        }
                                                        if(!"".equals(sCMNDCompoment) && !"".equals(sHCCompoment))
                                                        {
                                                            sCheckRequire = false;
                                                        }
                                                        if("".equals(sCMNDCompoment) && "".equals(sHCCompoment))
                                                        {
                                                            sCheckRequire = false;
                                                        }
                                                    } else if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTTYPE_DESC_ENTERPRISE)) {
                                                        if(!"".equals(sMSTCompoment) && !"".equals(sMNSCompoment))
                                                        {
                                                            sCheckRequire = false;
                                                        }
                                                        if("".equals(sMSTCompoment) && "".equals(sMNSCompoment))
                                                        {
                                                            sCheckRequire = false;
                                                        }
                                                    } else if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTTYPE_DESC_PERSONAL))
                                                    {
                                                        if(!"".equals(sCMNDCompoment) && !"".equals(sHCCompoment))
                                                        {
                                                            sCheckRequire = false;
                                                        }
                                                        if("".equals(sCMNDCompoment) && "".equals(sHCCompoment))
                                                        {
                                                            sCheckRequire = false;
                                                        }
                                                    } else if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_DEVICE))
                                                    {
                                                        if("".equals(sDeviceUUID))
                                                        {
                                                            sCheckRequire = false;
                                                        }
                                                    } else if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_PERSONAL_GOV)
                                                        || EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_ENTERPRISE_GOV))
                                                    {

                                                    } else if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_CLIENT)
                                                        || EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_SERVER)
                                                        || EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_AUTH)
                                                        || EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING_GOV)
                                                        || EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_SIGN))
                                                    {
                                                        if("".equals(sDeviceUUID))
                                                        {
                                                            sCheckRequire = false;
                                                        }
                                                    }

                                                    sDNResult = CommonFunction.subLastCharater(sDNResult);
                                                    CommonFunction.LogDebugString(log, "sCommomNameType", sCommomNameType);
                                                    CommonFunction.LogDebugString(log, "sCompanyCompoment", sCompanyCompoment);
                                                    CommonFunction.LogDebugString(log, "sDomainCompoment", sDomainCompoment);
                                                    CommonFunction.LogDebugString(log, "sPersonalCompoment", sPersonalCompoment);
                                                    CommonFunction.LogDebugString(log, "sCMNDCompoment", sCMNDCompoment);
                                                    CommonFunction.LogDebugString(log, "sHCCompoment", sHCCompoment);
                                                    CommonFunction.LogDebugString(log, "sMSTCompoment", sMSTCompoment);
                                                    CommonFunction.LogDebugString(log, "sMNSCompoment", sMNSCompoment);
                                                    CommonFunction.LogDebugString(log, "sServiceUUID", sDeviceUUID);
                                                    CommonFunction.LogDebugString(log, "sDNResult", sDNResult);
                                                }
                                                //</editor-fold>
                                                
                                                boolean isHasFileClient = false;
                                                if (sCheckRequire == true) {
                                                    if ("".equals(sDNResult)) {// edited temp
                                                        //<editor-fold defaultstate="collapsed" desc="### GET FULLNAME">
                                                        // get fullname of username param
                                                        String raFullnameCreate = "";
                                                        String raFullname = "";
                                                        UserInfo[][] userInfo = new UserInfo[1][];
                                                        db.S_BO_API_USER_LIST(EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), "", "", raServiceReq.language, userInfo);
                                                        if (userInfo[0].length > 0) {
                                                            raFullnameCreate = EscapeUtils.CheckTextNull(userInfo[0][0].fullName);
                                                        }
                                                        UserInfo[][] userDetail = new UserInfo[1][];
                                                        db.S_BO_API_USER_DETAIL(pApproveCAUser, raServiceReq.language, userDetail);
                                                        if (userDetail[0].length > 0) {
                                                            raFullname = EscapeUtils.CheckTextNull(userDetail[0][0].fullName);
                                                        }
                                                        //</editor-fold>
                                                        
                                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                                                        //<editor-fold defaultstate="collapsed" desc="### OWNER PROCESS">
                                                        if(raServiceReq.certificateOwnerID == 0)
                                                        {
                                                            String sEnterpriseID = "";
                                                            String sPersonalID = "";
                                                            if(!"".equals(sMSTCompoment)) {
                                                                sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE + sMSTCompoment;
                                                            }
                                                            if(!"".equals(sMNSCompoment)) {
                                                                sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE + sMNSCompoment;
                                                            }
                                                            if(!"".equals(sCMNDCompoment)) {
                                                                sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND + sCMNDCompoment;
                                                            }
                                                            if(!"".equals(sHCCompoment)) {
                                                                sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT + sHCCompoment;
                                                            }
//                                                            if(!"".equals(EscapeUtils.CheckTextNull(raServiceReq.citizenID))) {
//                                                                sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID + EscapeUtils.CheckTextNull(raServiceReq.citizenID);
//                                                            }
                                                            String sCERTIFICATION_OWNER_TYPE = "";
                                                            if(!EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_PERSONAL_GOV)
                                                                && !EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_ENTERPRISE_GOV)
                                                                && !EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_CLIENT)
                                                                && !EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_SERVER)
                                                                && !EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_AUTH)
                                                                && !EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_SIGN)
                                                                && !EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING_GOV))
                                                            {
                                                                if(!"".equals(sEnterpriseID) && "".equals(sPersonalID)) {
                                                                    sCERTIFICATION_OWNER_TYPE = Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_CODE_ENTERPRISE;
                                                                }
                                                                if("".equals(sEnterpriseID) && !"".equals(sPersonalID)) {
                                                                    sCERTIFICATION_OWNER_TYPE = Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_CODE_PERSONAL;
                                                                }
                                                                if(!"".equals(sEnterpriseID) && !"".equals(sPersonalID)) {
                                                                    sCERTIFICATION_OWNER_TYPE = Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_CODE_PERSONAL;
                                                                }
                                                            } else {
                                                                if(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_ENTERPRISE_GOV))
                                                                {
                                                                    sCERTIFICATION_OWNER_TYPE = Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_CODE_ENTERPRISE_GOV;
                                                                } else {
                                                                    sCERTIFICATION_OWNER_TYPE = Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_CODE_PERSONAL_GOV;
                                                                }
                                                            }
                                                            String sLocation = CommonFunction.getLocationInDN(sDNResult).trim();
                                                            String sAddress;
                                                            if(!"".equals(sLocation)) {
                                                                sAddress = CommonFunction.replaceStringCharaterSpecialDN(sLocation, true, true) + ", " + CommonFunction.getStateOrProvinceInDN(sDNResult);
                                                            } else {
                                                                sAddress = CommonFunction.getStateOrProvinceInDN(sDNResult).trim();
                                                            }
                                                            String pMESSAGE_QUEUE_TYPE_CODE = Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CODE_REGISTRATION_OWNER;
                                                            String sOwnerUUID = CommonFunction.getUUIDV4();
                                                            
                                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR ">
                                                            CERTIFICATION_OWNER_DATA_ATTR tempLogReqOwner = new CERTIFICATION_OWNER_DATA_ATTR();
                                                            tempLogReqOwner.personalName = raServiceReq.personalName;
                                                            tempLogReqOwner.companyName = raServiceReq.companyName;
                                                            tempLogReqOwner.taxCode = sMSTCompoment;
                                                            tempLogReqOwner.budgetCode = sMNSCompoment;
                                                            tempLogReqOwner.personalCode = sCMNDCompoment;
                                                            tempLogReqOwner.passportCode =sHCCompoment;
                                                            tempLogReqOwner.citizenID = "";
                                                            tempLogReqOwner.emailContract = raServiceReq.emailContact;
                                                            tempLogReqOwner.phoneContract = raServiceReq.phoneContact;
                                                            tempLogReqOwner.address = raServiceReq.address;
                                                            tempLogReqOwner.representative = "";
                                                            tempLogReqOwner.representativePosition = "";
                                                            tempLogReqOwner.typeName = pMESSAGE_QUEUE_TYPE_CODE;
                                                            tempLogReqOwner.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING;
                                                            tempLogReqOwner.createUser = raFullnameCreate + " (" + raServiceReq.beneficiaryUser + ")";
                                                            tempLogReqOwner.createDt = new Date();
                                                            //</editor-fold>
                                                            int[] pCERTIFICATION_OWNER_ID = new int[1];
                                                            int[] pOWNER_ATTR_ID = new int[1];
                                                            String[] pRESPONSE_CODE = new String[1];
                                                            db.S_BO_API_CERTIFICATION_OWNER_INSERT(sPersonalCompoment, sCompanyCompoment, sEnterpriseID, 
                                                                sPersonalID, sCERTIFICATION_OWNER_TYPE, raServiceReq.phoneContact, raServiceReq.emailContact,
                                                                raServiceReq.beneficiaryUser, sAddress, "", "", pMESSAGE_QUEUE_TYPE_CODE,
                                                                objectMapper.writeValueAsString(tempLogReqOwner), sOwnerUUID, pRESPONSE_CODE,
                                                                pCERTIFICATION_OWNER_ID, pOWNER_ATTR_ID);
                                                            if (!"0".equals(pRESPONSE_CODE[0])) {
                                                                if(pRESPONSE_CODE[0].trim().equals(String.valueOf(Definitions.CONFIG_WS_RESPONSE_CODE_CERTIFICATION_OWNER_EXISTS)))
                                                                {
                                                                    raServiceReq.certificateOwnerID = pCERTIFICATION_OWNER_ID[0];
                                                                } else {
                                                                    raServiceResp.responseCode = Integer.parseInt(pRESPONSE_CODE[0]);
                                                                }
                                                            } else {
                                                                raServiceReq.certificateOwnerID = pCERTIFICATION_OWNER_ID[0];
                                                            }
                                                        }
                                                        //</editor-fold>
                                                        
                                                        if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                                                            //<editor-fold defaultstate="collapsed" desc="### File manager Modules">
                                                            boolean isHasReqFile = false;
                                                            String sPropertiesFile = "";
                                                            CertificatePurposeInfo[][] certPurposeInfo = new CertificatePurposeInfo[1][];
                                                            db.S_BO_API_CERTIFICATION_PURPOSE_GET_FILE_PROPERTIES(EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode), certPurposeInfo);
                                                            if (certPurposeInfo[0].length > 0) {
                                                                sPropertiesFile = EscapeUtils.CheckTextNull(certPurposeInfo[0][0].certificatePurposeFileProperties);
                                                            }
                                                            if (raServiceReq.fileManagerInfo != null) {
                                                                if (raServiceReq.fileManagerInfo.length > 0) {
                                                                    isHasFileClient = true;
                                                                }
                                                            }
                                                            if (isHasFileClient == true) {
                                                                if (!"".equals(sPropertiesFile)) {
                                                                    objectMapper = new ObjectMapper();
                                                                    FILE_PROFILE_JSON itemParsePush = objectMapper.readValue(sPropertiesFile, FILE_PROFILE_JSON.class);
                                                                    for (FileManagerInfo fileManagerInfo1 : raServiceReq.fileManagerInfo) {
                                                                        for (FILE_PROFILE_JSON.Attribute attribute : itemParsePush.getAttributes()) {
                                                                            if (EscapeUtils.CheckTextNull(fileManagerInfo1.fileTypeCode).equals(attribute.getName())) {
                                                                                fileManagerInfo1.requireEnabled = attribute.getIsRequire();
                                                                            }
                                                                            if (attribute.getIsRequire() == true) {
                                                                                isHasReqFile = true;
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_TEMPLATE_FILE_EMPTY;
                                                                }
                                                            } else {
                                                                if (!"".equals(sPropertiesFile)) {
                                                                    objectMapper = new ObjectMapper();
                                                                    FILE_PROFILE_JSON itemParsePush = objectMapper.readValue(sPropertiesFile, FILE_PROFILE_JSON.class);
                                                                    for (FILE_PROFILE_JSON.Attribute attribute : itemParsePush.getAttributes()) {
                                                                        if (attribute.getIsRequire() == true) {
                                                                            isHasReqFile = true;
                                                                        }
                                                                    }
                                                                } else {
                                                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SERVER_TEMPLATE_FILE_EMPTY;
                                                                }
                                                            }
                                                            if (isHasFileClient == true) {
                                                                for (FileManagerInfo fileManagerCheck : raServiceReq.fileManagerInfo) {
                                                                    // check require
                                                                    if (fileManagerCheck.requireEnabled == true) {
                                                                        if (fileManagerCheck.fileByte == null) {
                                                                            sCheckRequire = false;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                                if (sCheckRequire == true) {
                                                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                                                                } else {
                                                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_COMPONENT_FILE_EMPTY;
                                                                }
                                                            } else {
                                                                if (isHasReqFile == true) {
                                                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_TEMPLATE_FILE_EMPTY;
                                                                }
                                                            }
                                                            //</editor-fold>
                                                        }
                                                        if (raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                                                            if("".equals(sProvinceCodeComponent)) {
                                                                // GET CITIPROVINCE from BeneficiaryBranch if "ST" not exists
                                                                BRANCH[][] rsBeneficiaryBranch = new BRANCH[1][];
                                                                db.S_BO_API_BRANCH_GET_INFO(EscapeUtils.CheckTextNull(raServiceReq.beneficiaryBranch), rsBeneficiaryBranch);
                                                                if(rsBeneficiaryBranch[0].length > 0) {
                                                                    CITY_PROVINCE[][] rsBeneficiaryCity = new CITY_PROVINCE[1][];
                                                                    db.S_BO_PROVINCE_DETAIL(String.valueOf(rsBeneficiaryBranch[0][0].PROVINCE_ID), rsBeneficiaryCity);
                                                                    if(rsBeneficiaryCity[0].length > 0)
                                                                    {
                                                                        sProvinceCodeComponent = EscapeUtils.CheckTextNull(rsBeneficiaryCity[0][0].NAME);
                                                                        sProvinceNameComponent = EscapeUtils.CheckTextNull(rsBeneficiaryCity[0][0].REMARK);
                                                                    }
                                                                }
                                                            }
                                                            // get corecasubject
                                                            String CheckCHANGE_KEY = "1";
                                                            String CheckPRIVATE_KEY;
                                                            String sCoreCASubject = "";
                                                            tempProfileList = new ArrayList<>();
                                                            CertificateAuthorityInfo[][] caInfo = new CertificateAuthorityInfo[1][];
                                                            db.S_BO_API_CERTIFICATION_AUTHORITY_LIST(EscapeUtils.CheckTextNull(raServiceReq.certificateAuthorityCode),
                                                                    raServiceReq.language, caInfo, "", tempProfileList);
                                                            if (caInfo[0].length > 0) {
                                                                sCoreCASubject = EscapeUtils.CheckTextNull(caInfo[0][0].certificateAuthorityCoreCASubject);
                                                            }
                                                            String sCSR = EscapeUtils.CheckTextNull(raServiceReq.csr);
                                                            String sCERTIFICATION_SN = "";
                                                            String pPAST_CERTIFICATION_SN = "";
                                                            if (EscapeUtils.CheckTextNull(raServiceReq.formFactorCode).equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_SOFT_TOKEN)) {
                                                                if (EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_SSL)) {
                                                                    sTOKEN_SN = Definitions.CONFIG_TOKEN_SSL_SN;
                                                                } else if (EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING)) {
                                                                    sTOKEN_SN = Definitions.CONFIG_TOKEN_CODESIGNNING_SN;
                                                                } else if (EscapeUtils.CheckTextNull(raServiceReq.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_DEVICE)) {
                                                                    sTOKEN_SN = Definitions.CONFIG_TOKEN_DEVICE_SN;
                                                                } else {
                                                                    sTOKEN_SN = Definitions.CONFIG_TOKEN_SIGNSERVER_SN;
                                                                }
                                                            } else if (EscapeUtils.CheckTextNull(raServiceReq.formFactorCode).equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_ESIGNCLOUD)) {
                                                                sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_FORMFACTOR_ESIGNCLOUD;
                                                            } else if (CommonFunction.checkHardTokenEnabled(EscapeUtils.CheckTextNull(raServiceReq.formFactorCode)) == true) {
                                                                if(raServiceReq.inHouseEnabled == true) {
                                                                    sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_INHOUSE;
                                                                } else {
                                                                    sTOKEN_SN = Definitions.CONFIG_TOKEN_UNASSIGN_SN;
                                                                }
                                                            } else {
                                                                sTOKEN_SN = "";
                                                            }
                                                            String sPromotionDuration = "";
                                                            if(raServiceReq.promotionDuration != -1) {
                                                                sPromotionDuration = String.valueOf(raServiceReq.promotionDuration);
                                                            }
                                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR ">
                                                            CERTIFICATION_DATA_ATTR tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                            tempLogReq.personalName = sPersonalCompoment;
                                                            tempLogReq.companyName = sCompanyCompoment;
                                                            tempLogReq.taxCode = sMSTCompoment;
                                                            tempLogReq.budgetCode = sMNSCompoment;
                                                            tempLogReq.personalCode = sCMNDCompoment;
                                                            tempLogReq.passportCode = sHCCompoment;
                                                            tempLogReq.emailContract = raServiceReq.emailContact;
                                                            tempLogReq.phoneContract = raServiceReq.phoneContact;
                                                            tempLogReq.issuerSubject = sCoreCASubject;
                                                            tempLogReq.subjectDn = sDNResult;
                                                            tempLogReq.tokenSn = sTOKEN_SN;
                                                            tempLogReq.provinceName = sProvinceNameComponent;
                                                            tempLogReq.pkiFromFactorCode = raServiceReq.formFactorCode;
                                                            tempLogReq.typeName = pCERTIFICATION_ATTR_TYPE_CODE;
                                                            ATTRIBUTE_VALUES valueATTR;
                                                            ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                            dataATTR.setCertificationData(tempLogReq);
                                                            valueATTR = new ATTRIBUTE_VALUES();
                                                            valueATTR.setTokenSn(sTOKEN_SN);
                                                            boolean sChangeKeyEnabled = "1".equals(CheckCHANGE_KEY);
                                                            valueATTR.setChangeKeyEnabled(sChangeKeyEnabled);
                                                            valueATTR.setKeepCertificateSNEnabled(false);
                                                            valueATTR.setTypeName(pCERTIFICATION_ATTR_TYPE_CODE);
                                                            valueATTR.setPromotionDuration(sPromotionDuration);
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                            valueATTR.setCreateUser(raFullnameCreate + " (" + raServiceReq.beneficiaryUser + ")");
                                                            valueATTR.setCreateDt(new Date());
                                                            valueATTR.setAttributeData(dataATTR);
                                                            //</editor-fold>

                                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            //<editor-fold defaultstate="collapsed" desc="### LEVEL APPROVE">
                                                            // intLevelApprove - new : 0, agency approve : 1, CA approve : 2
                                                            int intLevelApprove = 0;
                                                            if (autoApproveCAServer == false && autoApproveCAClient == false) {
                                                                intLevelApprove = 0;
                                                            } else if (autoApproveCAServer == false && autoApproveCAClient == true) {
                                                                intLevelApprove = 1;
                                                            } else if (autoApproveCAServer == true && autoApproveCAClient == false) {
                                                                intLevelApprove = 0;
                                                            } else if (autoApproveCAServer == true && autoApproveCAClient == true) {
                                                                intLevelApprove = 2;
                                                            }
                                                            //</editor-fold>

                                                            int[] pCERTIFICATE_ATTR_ID = new int[1];
                                                            int[] pCERTIFICATION_ID = new int[1];
                                                            String[] pRESPONSE_CODE_NAME = new String[1];
                                                            if (CommonFunction.checkHardTokenEnabled(EscapeUtils.CheckTextNull(raServiceReq.formFactorCode)) == true)
                                                            {
                                                                //<editor-fold defaultstate="collapsed" desc="### GET POLICY"> 
                                                                int intOTPNumn = 8;
                                                                if (rsPolicy[0].length > 0) {
                                                                    for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                                                                        if (EscapeUtils.CheckTextNull(rsPolicy1.NAME).equals(Definitions.CONFIG_POLICY_FO_DEFAULT_ACTIVATION_CODE_LENGTH)) {
                                                                            intOTPNumn = Integer.parseInt(rsPolicy1.VALUE);
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                                //</editor-fold>
                                                                if (raServiceReq.backupKeyEnabled == true) {
                                                                    CheckPRIVATE_KEY = "1";
                                                                } else {
                                                                    CheckPRIVATE_KEY = "0";
                                                                }
                                                                pRESPONSE_CODE_NAME[0] = "10000";
                                                                //<editor-fold defaultstate="collapsed" desc="### INSERT CERT AND ACTIVATION CODE">
                                                                while ("10000".equals(pRESPONSE_CODE_NAME[0])) {
                                                                    try {
                                                                        String sOTP = "";
                                                                        //if(!sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SN_INHOUSE)) {
                                                                        sOTP = CommonFunction.getRandomOTP(intOTPNumn);
                                                                        if(raServiceReq.activationCodeEnabled == true) {
                                                                            raServiceResp.activationCode = sOTP;
                                                                        }
                                                                        //}
                                                                        db.S_BO_API_CERTIFICATION_INSERT(EscapeUtils.CheckTextNull(raServiceReq.certificateProfileCode), sTOKEN_SN,
                                                                            sCERTIFICATION_SN, sPersonalCompoment, sCompanyCompoment, sDomainCompoment,
                                                                            sDNResult, sCoreCASubject, EscapeUtils.CheckTextNull(raServiceReq.phoneContact),
                                                                            EscapeUtils.CheckTextNull(raServiceReq.emailContact),
                                                                            sProvinceCodeComponent, pPAST_CERTIFICATION_SN, sOTP, pCERTIFICATION_ATTR_TYPE_CODE,
                                                                            strReqValueATTR, EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser),
                                                                            EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), sCSR,
                                                                            EscapeUtils.CheckTextNull(raServiceReq.certificateAuthorityCode), CheckCHANGE_KEY, CheckPRIVATE_KEY,
                                                                            EscapeUtils.CheckTextNull(raServiceReq.formFactorCode), sDeviceUUID.trim(), sPromotionDuration, pRESPONSE_CODE_NAME, pCERTIFICATION_ID,
                                                                            pCERTIFICATE_ATTR_ID, raServiceReq.certificateOwnerID, null, null, "","");
                                                                        CommonFunction.LogDebugString(log, sTOKEN_SN + " - S_BO_API_CERTIFICATION_INSERT - RESULT", pRESPONSE_CODE_NAME[0]);
                                                                    } catch (Exception e) {
                                                                        if (e.getMessage().contains(Definitions.CONFIG_MYSQL_UNIQUE_ACTIVATION_CODE)) {
                                                                            pRESPONSE_CODE_NAME[0] = "10000";
                                                                        } else {
                                                                            pRESPONSE_CODE_NAME[0] = String.valueOf(Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION);
                                                                            CommonFunction.LogExceptionServlet(log, e.getMessage().trim(), e);
                                                                        }
                                                                    }
                                                                }
                                                                //</editor-fold>

                                                                if ("0".equals(pRESPONSE_CODE_NAME[0])) {
                                                                    raServiceResp.certificateID = pCERTIFICATION_ID[0];
                                                                    raServiceResp.certificateStateCode = Definitions.CONFIG_CERTIFICATION_STATE_CODE_NEW;
                                                                    //<editor-fold defaultstate="collapsed" desc="### File Attach: Check Add to JRB">
                                                                    if (isHasFileClient == true) {
                                                                        String sJRBConfig = "";
                                                                        if (rsPolicy[0].length > 0) {
                                                                            for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                                                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT)) {
                                                                                    sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        if (!"".equals(sJRBConfig)) {
                                                                            String sJRB_Source = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
                                                                            if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY)) {
                                                                                String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
                                                                                String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
                                                                                String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
                                                                                String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
                                                                                String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
                                                                                String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
                                                                                String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
                                                                                String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
                                                                                String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_UP);
                                                                                String idUUID_Temp = Definitions.CONFIG_JACK_RABBIT_UUID_SAMPLE;
                                                                                for (FileManagerInfo fileManagerInsert : raServiceReq.fileManagerInfo) {
                                                                                    String sFileData = new String(Base64.encodeBase64(fileManagerInsert.fileByte), "UTF-8"); //EscapeUtils.CheckTextNull(mhIP.FILE_URL);//CommonFunction.encodeFileToBase64Binary(fileUp);
                                                                                    CloseableHttpResponse pHttpRes = ConnectFileToPartner.upFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                                                                            sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                                                                            sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, idUUID_Temp,
                                                                                            fileManagerInsert.fileName, sFileData);
                                                                                    InputStream isStr = pHttpRes.getEntity().getContent();
                                                                                    String resultUUID = IOUtils.toString(isStr);
                                                                                    CommonFunction.LogDebugString(log, sTOKEN_SN + " - FILE - " + sJRB_Source + " - sUUID", resultUUID);
                                                                                    String sMimeType = fileManagerInsert.mimeType;
                                                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                                                    db.S_BO_API_FILE_MANAGER_INSERT(fileManagerInsert.fileTypeCode, resultUUID, sJRBConfig,
                                                                                            sMimeType, fileManagerInsert.fileName, fileManagerInsert.fileByte.length,
                                                                                            pCERTIFICATION_ID[0], raServiceReq.certificateOwnerID, EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), pFILE_MANAGER_ID);
                                                                                }
                                                                            } else if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
                                                                                String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                                                String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                                                String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                                                String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                                                String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                                                String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                                                String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                                                for (FileManagerInfo fileManagerInsert : raServiceReq.fileManagerInfo) {
                                                                                    String sMimeType = fileManagerInsert.mimeType;
                                                                                    JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                                                                            Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                                    InputStream isFILE_STREAM = new ByteArrayInputStream(fileManagerInsert.fileByte);
                                                                                    JCRFile jrbFile = JackRabbitCommon.getInstance(jcrConfig).uploadFile(fileManagerInsert.fileName, sMimeType, isFILE_STREAM);
                                                                                    if(jrbFile != null) {
                                                                                        CommonFunction.LogDebugString(log, sTOKEN_SN + " - FILE - " + sJRB_Source + " - sUUID", jrbFile.getUuid());
                                                                                        int[] pFILE_MANAGER_ID = new int[1];
                                                                                        db.S_BO_API_FILE_MANAGER_INSERT(fileManagerInsert.fileTypeCode, jrbFile.getUuid(), sJRBConfig,
                                                                                                jrbFile.getMimeType(), jrbFile.getFileName(), fileManagerInsert.fileByte.length,
                                                                                                pCERTIFICATION_ID[0], raServiceReq.certificateOwnerID, EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), pFILE_MANAGER_ID);
                                                                                    }
                                                                                }
                                                                            } else if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
                                                                                String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                                                String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                                                String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                                                String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                                                String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                                                String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                                                String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                                                for (FileManagerInfo fileManagerInsert : raServiceReq.fileManagerInfo) {
                                                                                    String sMimeType = fileManagerInsert.mimeType;
        //                                                                            JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
        //                                                                                Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                                    InputStream isFILE_STREAM = new ByteArrayInputStream(fileManagerInsert.fileByte);
        //                                                                            JCRFile jrbFile = JackRabbitCommon.uploadFile(jcrConfig, fileManagerInsert.fileName, sMimeType, isFILE_STREAM);
                                                                                    ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                                                                                            Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                                    String[] sReturnJRB = new String[2];
                                                                                    vn.mobileid.fms.client.JCRFile jrbFile = openJRB.uploadFile(EscapeUtils.CheckTextNull(fileManagerInsert.fileName),
                                                                                            EscapeUtils.CheckTextNull(sMimeType), isFILE_STREAM, sReturnJRB);
                                                                                    CommonFunction.LogDebugString(log, sTOKEN_SN + " - FILE - " + sJRB_Source + " - sUUID", sReturnJRB[0].trim());
                                                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                                                    db.S_BO_API_FILE_MANAGER_INSERT(fileManagerInsert.fileTypeCode, sReturnJRB[0].trim(), sJRBConfig,
                                                                                            sMimeType, sReturnJRB[1].trim(), fileManagerInsert.fileByte.length,
                                                                                            pCERTIFICATION_ID[0], raServiceReq.certificateOwnerID, EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), pFILE_MANAGER_ID);
                                                                                }
                                                                            } else {
                                                                            }
                                                                        }
                                                                    }
                                                                    //</editor-fold>

                                                                    //<editor-fold defaultstate="collapsed" desc="### Approve request -> PRE_APPROVED, APPROVED">
                                                                    if (intLevelApprove == 1 || intLevelApprove == 2) {
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                        valueATTR.setApproveUser(raFullname + " (" + raServiceReq.approveUser + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                        String sPRE_APPROVED = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                                                                        if("0".equals(sPRE_APPROVED)) {
                                                                            raServiceResp.certificateStateCode = Definitions.CONFIG_CERTIFICATION_STATE_CODE_NEW;
                                                                        } else {
                                                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERT_ERROR_APPROVE;
                                                                        }
                                                                    }
                                                                    if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                                                                        if (intLevelApprove == 2) {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                            valueATTR.setApproveCAUser(raFullname + " (" + raServiceReq.approveUser + ")");
                                                                            valueATTR.setApproveCADt(new Date());
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                                                                            if ("0".equals(sApprove)) {
                                                                                // DISCOUNT RATE
                                                                                if ("1".equals(sDiscountRateOption)) {
                                                                                    CommonReferServlet.updateDiscountRateImportCert(String.valueOf(raServiceResp.certificateID),
                                                                                        EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), EscapeUtils.CheckTextNull(raServiceReq.certificateProfileCode),
                                                                                        sMSTCompoment, sMNSCompoment, sCMNDCompoment, sHCCompoment, EscapeUtils.CheckTextNull(raServiceReq.approveUser), "", "", "", "");
                                                                                }
                                                                                // SET COMMIT_ENABLED TRUE of FILE
                                                                                db.S_BO_API_CERTIFICATION_SUPPLEMENT_FILE(pCERTIFICATE_ATTR_ID[0], raServiceReq.approveUser, pRESPONSE_CODE_NAME);
                                                                                String pushNoticeEnabled = "0";
                                                                                boolean pushNoticeCertServer = CommonFunction.getPushNoticeEnabledCert(sCERT_POLICY_PROPERTIES);
                                                                                if (raServiceReq.certificateNotificationEnabled == true && pushNoticeCertServer == true) {
                                                                                    pushNoticeEnabled = "1";
                                                                                }
                                                                                db.S_BO_CERTIFICATION_UPDATE_AMOUNT(pCERTIFICATION_ID[0], "", pushNoticeEnabled, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                                                                                // select get cert with pCERTIFICATION_ID[0]
                                                                                int[] pRESPONSE_CODE = new int[1];
                                                                                CertificateInfo[][] certInfo = new CertificateInfo[1][];
                                                                                db.S_BO_API_CERTIFICATION_GET_INFO("", "", "", "", "", pCERTIFICATION_ID[0], "", "",
                                                                                        raServiceReq.language, pRESPONSE_CODE, certInfo, "","", "", "");
                                                                                if (certInfo[0].length > 0) {
                                                                                    raServiceResp.certificate = certInfo[0][0].certificate;
                                                                                }
                                                                                if ("1".equals(pushNoticeEnabled)) {
                                                                                    int[] intRes = new int[1];
                                                                                    String[] sRes = new String[1];
                                                                                    ConnectConnector.SendMailOTP(String.valueOf(pCERTIFICATION_ID[0]), intRes, sRes);
                                                                                }
                                                                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                                                                            } else {
                                                                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERT_ERROR_APPROVE;
                                                                            }
                                                                        } else {
                                                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                                                                        }
                                                                    }
                                                                    //</editor-fold>
                                                                } else {
                                                                    raServiceResp.responseCode = Integer.parseInt(pRESPONSE_CODE_NAME[0]);
                                                                }
                                                            } else if (EscapeUtils.CheckTextNull(raServiceReq.formFactorCode).equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_SOFT_TOKEN)
                                                                || EscapeUtils.CheckTextNull(raServiceReq.formFactorCode).equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_ESIGNCLOUD))
                                                            {
                                                                /*if (sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN)
                                                                        || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN)
                                                                        || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN))
                                                                { }*/
                                                                String strPasswordP12 = "";
                                                                //// check if signserver cert
                                                                boolean isValidCSR = true;
    //                                                            if (sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SN_FORMFACTOR_ESIGNCLOUD)) {
                                                                //<editor-fold defaultstate="collapsed" desc="### CSR: check valid">
                                                                if (!"".equals(sCSR)) {
                                                                    CheckPRIVATE_KEY = "0";
                                                                    String sKeySizeDB;
                                                                    isValidCSR = false;
                                                                    CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                                                    db.S_BO_API_GET_ALGORITHM_KEY_SIZE(EscapeUtils.CheckTextNull(raServiceReq.certificateProfileCode), rsCert);
                                                                    if (rsCert[0].length > 0) {
                                                                        sKeySizeDB = EscapeUtils.CheckTextNull(rsCert[0][0].KEY_SIZE);
                                                                        String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(sCSR);
                                                                        isValidCSR = sKeySizeDB.equals(sKeySizeCSR);
                                                                    }
                                                                } else {
                                                                    CheckPRIVATE_KEY = "1";
                                                                    if (!"".equals(EscapeUtils.CheckTextNull(raServiceReq.p12Password))) {
                                                                        strPasswordP12 = EscapeUtils.CheckTextNull(raServiceReq.p12Password);
                                                                    } else {
                                                                        strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                                                    }
                                                                }
                                                                //</editor-fold>
    //                                                            } else {
    //                                                                CheckPRIVATE_KEY = "1";
    //                                                            }

                                                                if (isValidCSR == true) {
                                                                    db.S_BO_API_CERTIFICATION_INSERT(EscapeUtils.CheckTextNull(raServiceReq.certificateProfileCode), sTOKEN_SN,
                                                                        sCERTIFICATION_SN, sPersonalCompoment, sCompanyCompoment, sDomainCompoment,
                                                                        sDNResult, sCoreCASubject, EscapeUtils.CheckTextNull(raServiceReq.phoneContact), EscapeUtils.CheckTextNull(raServiceReq.emailContact),
                                                                        sProvinceCodeComponent, pPAST_CERTIFICATION_SN, "", pCERTIFICATION_ATTR_TYPE_CODE,
                                                                        strReqValueATTR, EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), sCSR,
                                                                        EscapeUtils.CheckTextNull(raServiceReq.certificateAuthorityCode), CheckCHANGE_KEY, CheckPRIVATE_KEY,
                                                                        EscapeUtils.CheckTextNull(raServiceReq.formFactorCode), sDeviceUUID, sPromotionDuration,
                                                                        pRESPONSE_CODE_NAME, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID, raServiceReq.certificateOwnerID, null, null,"","");
                                                                    CommonFunction.LogDebugString(log, sTOKEN_SN + " - S_BO_API_CERTIFICATION_INSERT - RESULT", pRESPONSE_CODE_NAME[0]);
                                                                    if ("0".equals(pRESPONSE_CODE_NAME[0])) {
                                                                        //<editor-fold defaultstate="collapsed" desc="### DNS LIST for SSL">
//                                                                        if(EscapeUtils.CheckTextNull(raReqTemp.formFactorCode).equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_SOFT_TOKEN))
//                                                                        {
//                                                                            if(EscapeUtils.CheckTextNull(raReqTemp.certificatePurposeCode).equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_SSL))
//                                                                            {
//                                                                                if(!"".equals(strDNSName))
//                                                                                {
//                                                                                    db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(pCERTIFICATION_ID[0]), strDNSName, EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser));
//                                                                                }
//                                                                            }
//                                                                        }
                                                                        if(!"".equals(strDNSName))
                                                                        {
                                                                            db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(pCERTIFICATION_ID[0]), strDNSName, EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser));
                                                                        }
                                                                        //</editor-fold>
                                                                    
                                                                        raServiceResp.certificateID = pCERTIFICATION_ID[0];
                                                                        raServiceResp.certificateStateCode = Definitions.CONFIG_CERTIFICATION_STATE_CODE_NEW;
                                                                        //<editor-fold defaultstate="collapsed" desc="### File Attach: Check Add to JRB">
                                                                        if (isHasFileClient == true) {
                                                                            String sJRBConfig = "";
                                                                            if (rsPolicy[0].length > 0) {
                                                                                for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                                                                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT)) {
                                                                                        sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (!"".equals(sJRBConfig)) {
                                                                                String sJRB_Source = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
                                                                                if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY)) {
                                                                                    String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
                                                                                    String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
                                                                                    String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
                                                                                    String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
                                                                                    String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
                                                                                    String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
                                                                                    String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
                                                                                    String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
                                                                                    String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_UP);
                                                                                    String idUUID_Temp = Definitions.CONFIG_JACK_RABBIT_UUID_SAMPLE;
                                                                                    for (FileManagerInfo fileManagerInsert : raServiceReq.fileManagerInfo) {
                                                                                        String sFileData = new String(Base64.encodeBase64(fileManagerInsert.fileByte), "UTF-8"); //EscapeUtils.CheckTextNull(mhIP.FILE_URL);//CommonFunction.encodeFileToBase64Binary(fileUp);
                                                                                        CloseableHttpResponse pHttpRes = ConnectFileToPartner.upFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                                                                                sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                                                                                sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, idUUID_Temp,
                                                                                                fileManagerInsert.fileName, sFileData);
                                                                                        InputStream isStr = pHttpRes.getEntity().getContent();
                                                                                        String resultUUID = IOUtils.toString(isStr);
                                                                                        CommonFunction.LogDebugString(log, sTOKEN_SN + " - FILE - " + sJRB_Source + " - sUUID", resultUUID);
                                                                                        String sMimeType = fileManagerInsert.mimeType;
                                                                                        int[] pFILE_MANAGER_ID = new int[1];
                                                                                        db.S_BO_API_FILE_MANAGER_INSERT(fileManagerInsert.fileTypeCode, resultUUID, sJRBConfig,
                                                                                                sMimeType, fileManagerInsert.fileName, fileManagerInsert.fileByte.length,
                                                                                                pCERTIFICATION_ID[0], raServiceReq.certificateOwnerID, EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), pFILE_MANAGER_ID);
                                                                                    }
                                                                                } else if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
                                                                                    String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                                                    String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                                                    String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                                                    String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                                                    String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                                                    String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                                                    String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                                                    for (FileManagerInfo fileManagerInsert : raServiceReq.fileManagerInfo) {
                                                                                        String sMimeType = fileManagerInsert.mimeType;
                                                                                        JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                                                                                Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                                        InputStream isFILE_STREAM = new ByteArrayInputStream(fileManagerInsert.fileByte);
                                                                                        JCRFile jrbFile = JackRabbitCommon.getInstance(jcrConfig).uploadFile(fileManagerInsert.fileName, sMimeType, isFILE_STREAM);
                                                                                        if(jrbFile != null){
                                                                                            CommonFunction.LogDebugString(log, sTOKEN_SN + " - FILE - " + sJRB_Source + " - sUUID", jrbFile.getUuid());
                                                                                            int[] pFILE_MANAGER_ID = new int[1];
                                                                                            db.S_BO_API_FILE_MANAGER_INSERT(fileManagerInsert.fileTypeCode, jrbFile.getUuid(), sJRBConfig,
                                                                                                    jrbFile.getMimeType(), jrbFile.getFileName(), fileManagerInsert.fileByte.length,
                                                                                                    pCERTIFICATION_ID[0], raServiceReq.certificateOwnerID, EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), pFILE_MANAGER_ID);
                                                                                        }
                                                                                    }
                                                                                } else if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
                                                                                    String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                                                    String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                                                    String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                                                    String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                                                    String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                                                    String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                                                    String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                                                    for (FileManagerInfo fileManagerInsert : raServiceReq.fileManagerInfo) {
                                                                                        String sMimeType = fileManagerInsert.mimeType;
        //                                                                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
        //                                                                                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                                        InputStream isFILE_STREAM = new ByteArrayInputStream(fileManagerInsert.fileByte);
        //                                                                                JCRFile jrbFile = JackRabbitCommon.uploadFile(jcrConfig, fileManagerInsert.fileName, sMimeType, isFILE_STREAM);
                                                                                        ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                                                                                                Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                                        String[] sReturnJRB = new String[2];
                                                                                        vn.mobileid.fms.client.JCRFile jrbFile = openJRB.uploadFile(EscapeUtils.CheckTextNull(fileManagerInsert.fileName),
                                                                                                EscapeUtils.CheckTextNull(sMimeType), isFILE_STREAM, sReturnJRB);
                                                                                        CommonFunction.LogDebugString(log, sTOKEN_SN + " - FILE - " + sJRB_Source + " - sUUID", jrbFile.getUuid());
                                                                                        int[] pFILE_MANAGER_ID = new int[1];
                                                                                        db.S_BO_API_FILE_MANAGER_INSERT(fileManagerInsert.fileTypeCode, sReturnJRB[0].trim(), sJRBConfig,
                                                                                                sMimeType, sReturnJRB[1].trim(), fileManagerInsert.fileByte.length,
                                                                                                pCERTIFICATION_ID[0], raServiceReq.certificateOwnerID, EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), pFILE_MANAGER_ID);
                                                                                    }
                                                                                } else {
                                                                                }
                                                                            }
                                                                        }
                                                                        //</editor-fold>

                                                                        //<editor-fold defaultstate="collapsed" desc="### Approve request -> PRE_APPROVED, APPROVED">
                                                                        if (intLevelApprove == 1 || intLevelApprove == 2) {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                            valueATTR.setApproveUser(raFullname + " (" + raServiceReq.approveUser + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                            String sPRE_APPROVED = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                                                                            if("0".equals(sPRE_APPROVED)) {
                                                                                raServiceResp.certificateStateCode = Definitions.CONFIG_CERTIFICATION_STATE_CODE_NEW;
                                                                            } else {
                                                                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERT_ERROR_APPROVE;
                                                                            }
                                                                        }
                                                                        if(raServiceResp.responseCode == Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS) {
                                                                            if (intLevelApprove == 2) {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                                valueATTR.setApproveCAUser(raFullname + " (" + raServiceReq.approveUser + ")");
                                                                                valueATTR.setApproveCADt(new Date());
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                                                                                if ("0".equals(sApprove)) {
                                                                                    // DISCOUNT RATE
                                                                                    if ("1".equals(sDiscountRateOption)) {
                                                                                        CommonReferServlet.updateDiscountRateImportCert(String.valueOf(raServiceResp.certificateID),
                                                                                            EscapeUtils.CheckTextNull(raServiceReq.beneficiaryUser), EscapeUtils.CheckTextNull(raServiceReq.certificateProfileCode),
                                                                                            sMSTCompoment, sMNSCompoment, sCMNDCompoment, sHCCompoment, EscapeUtils.CheckTextNull(raServiceReq.approveUser), "", "", "", "");
                                                                                    }
                                                                                    // SET COMMIT_ENABLED TRUE of FILE
                                                                                    db.S_BO_API_CERTIFICATION_SUPPLEMENT_FILE(pCERTIFICATE_ATTR_ID[0], raServiceReq.approveUser, pRESPONSE_CODE_NAME);

                                                                                    //<editor-fold defaultstate="collapsed" desc="### UPDATE SEND EMAIL CERT">
                                                                                    if (sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SN_FORMFACTOR_ESIGNCLOUD)) {
                                                                                        if (!"".equals(sCSR)) {
                                                                                            String pushNoticeEnabled = "0";
                                                                                            boolean pushNoticeCertServer = CommonFunction.getPushNoticeEnabledCert(sCERT_POLICY_PROPERTIES);
                                                                                            if (raServiceReq.certificateNotificationEnabled == true && pushNoticeCertServer == true) {
                                                                                                pushNoticeEnabled = "1";
                                                                                            }
                                                                                            db.S_BO_CERTIFICATION_UPDATE_AMOUNT(pCERTIFICATION_ID[0], "", pushNoticeEnabled, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                                                                                        } else {
                                                                                            String p12EmailEnabled = "0";
                                                                                            boolean p12EmailCertServer = CommonFunction.getP12EmailEnabledCert(sCERT_POLICY_PROPERTIES);
                                                                                            if (raServiceReq.p12EmailEnabled == true && p12EmailCertServer == true) {
                                                                                                p12EmailEnabled = "1";
                                                                                            }
                                                                                            db.S_BO_CERTIFICATION_UPDATE_AMOUNT(pCERTIFICATION_ID[0], "", p12EmailEnabled, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                                                                                        }
                                                                                    } else {
                                                                                        String pushNoticeEnabled = "0";
                                                                                        boolean pushNoticeCertServer = CommonFunction.getPushNoticeEnabledCert(sCERT_POLICY_PROPERTIES);
                                                                                        if (raServiceReq.certificateNotificationEnabled == true && pushNoticeCertServer == true) {
                                                                                            pushNoticeEnabled = "1";
                                                                                        }
                                                                                        db.S_BO_CERTIFICATION_UPDATE_AMOUNT(pCERTIFICATION_ID[0], "", pushNoticeEnabled, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                                                                                    }
                                                                                    //</editor-fold>

                                                                                    int[] intWSRes = new int[1];
                                                                                    String[] sWSRes = new String[1];
                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                    if (intWSRes[0] == 0) {
                                                                                        // select get cert with pCERTIFICATION_ID[0]
                                                                                        int[] pRESPONSE_CODE = new int[1];
                                                                                        CertificateInfo[][] certInfo = new CertificateInfo[1][];
                                                                                        db.S_BO_API_CERTIFICATION_GET_INFO("", "", "", "", "", pCERTIFICATION_ID[0], "", "",
                                                                                                raServiceReq.language, pRESPONSE_CODE, certInfo, "", "", "", "");
                                                                                        if (certInfo[0].length > 0) {
                                                                                            raServiceResp.certificate = certInfo[0][0].certificate;
                                                                                            raServiceResp.certificateStateCode = certInfo[0][0].certificateStateCode;
                                                                                            raServiceResp.certificateSN = certInfo[0][0].certificateSN;
                                                                                        }
                                                                                        if (!"".equals(strPasswordP12)) {
                                                                                            int[] intRes = new int[1];
                                                                                            String[] sRes = new String[1];
                                                                                            byte[] sP12Return = ConnectConnector.generateKeystore(strPasswordP12, false, String.valueOf(pCERTIFICATION_ID[0]), intRes, sRes);
                                                                                            raServiceResp.p12Certificate = sP12Return;
                                                                                        }
                                                                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                                                                                    } else {
                                                                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERT_ERROR_ISSUE;
                                                                                    }
                                                                                } else {
                                                                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CERT_ERROR_APPROVE;
                                                                                }
                                                                            } else {
                                                                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_SUCCESS;
                                                                            }
                                                                        }
                                                                        //</editor-fold>

                                                                        // return pass p12
                                                                        if ("".equals(EscapeUtils.CheckTextNull(raServiceReq.p12Password))) {
                                                                            if (!"".equals(strPasswordP12)) {
                                                                                raServiceResp.p12Password = strPasswordP12;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        raServiceResp.responseCode = Integer.parseInt(pRESPONSE_CODE_NAME[0]);
                                                                    }
                                                                } else {
                                                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_CSR_KEYSIZE;
                                                                }
                                                            } else {
                                                                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_REQUEST_NOT_SUPPORT;
                                                            }
                                                        }
                                                    } else {
                                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_NO_DN;
                                                    }
                                                } else {
                                                    raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_COMPONENT_CERT_EMPTY;
                                                }
                                            }
                                        } else {
                                            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CA_PROFILE_INVALID;
                                        }
                                    } else {
                                        raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_TEMPLATE_CERT_EMPTY;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
            CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
        } finally {
            try {
                // get response code return
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
                    objectMapper = new ObjectMapper();
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], String.valueOf(raServiceResp.responseCode),
                            objectMapper.writeValueAsString(raServiceResp), sTOKEN_SN, EscapeUtils.CheckTextNull(raServiceReq.approveUser));
                }
            } catch (Exception e) {
                raServiceResp.responseCode = Definitions.CONFIG_WS_RESPONSE_CODE_EXCEPTION;
                raServiceResp.responseMessage = "An Unknown Error: " + e.getMessage();
                CommonFunction.LogExceptionServlet(log, "An Unknown Error: " + e.getMessage(), e);
            }
        }
        return raServiceResp;
    }
    //</editor-fold>
    
}
