/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRFile;
import vn.ra.object.ATTRIBUTE_DATA;
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.object.CERTIFICATION_COMMENT;
import vn.ra.object.CERTIFICATION_DATA_ATTR;
import vn.ra.object.CERTIFICATION_OWNER;
import vn.ra.object.CERTIFICATION_OWNER_DATA_ATTR;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.CERTIFICATION_PROPERTIES_JSON;
import vn.ra.object.FILE_PROFILE_DATA;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.RESPONSE_CODE;
import vn.ra.object.RESPONSE_LOG;
import vn.ra.object.ROLE_DATA;
import vn.ra.object.TOKEN;
import vn.ra.object.TOKEN_CHANGE_LOG;
import vn.ra.process.CommonFunction;
import vn.ra.process.CommonReferServlet;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.ConnectFileToPartner;
import vn.ra.process.ConnectJackRabbitNew;
import vn.ra.process.JackRabbitCommon;
import vn.ra.process.RSSPProcessCommon;
import vn.ra.process.SessionUploadFileCert;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;
import vn.mobileid.esigncloud.management.AgreementDetails;
import vn.mobileid.esigncloud.management.OwnerInfo;
import vn.ra.object.BACKOFFICE_USER;
import vn.ra.object.CERTIFICATION_REVOCATION_REASON;
import vn.ra.object.CERTIFICATION_TYPE_COMPONENT;
import vn.ra.object.CITY_PROVINCE;
import vn.ra.object.CertificateInfo;
import vn.ra.object.CredentialDataAuthen;
import vn.ra.object.FormFactorJsonProperties;
import vn.ra.object.PKI_FORMFACTOR;
import vn.ra.object.PREFIX_UUID;
import vn.ra.object.ProfileContactInfoJson;
import vn.ra.object.RESPONSE_CORECA;
import vn.ra.process.ConnectDbPhaseTwo;
import vn.ra.process.ServletUptoFunction;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author THANH-PC
 */
public class RequestCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RequestCommon.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            ConnectDatabase db = new ConnectDatabase();
            HttpSession sessionsa = request.getSession(false);
            String strView = "";
            String loginUID = "";
            String idParam = request.getParameter("idParam");
            ObjectMapper objectMapper = null;
            int[] pCERTIFICATE_ATTR_ID = new int[1];
            int[] pCERTIFICATE_ID = new int[1];
            int[] System_Log_ID = new int[1];
            String[] sysLog_BillCode = new String[1];
            System_Log_ID[0] = 0;
            try {
                int sOutInner;
                if (request.getSession(false) != null) {
                    String strInnerUsername = sessionsa.getAttribute("sUserID").toString().trim();
                    String strInnerSessionKey = sessionsa.getAttribute("sesSessKey").toString().trim();
                    sOutInner = db.CheckIsLoginOnline(strInnerUsername, strInnerSessionKey);
                } else {
                    sOutInner = 2;
                }
                if (sOutInner == 1) {
                    String AGENT_ID_LOG = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SessAgentID").toString().trim());
                    String SessUserAgentID = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SessUserAgentID").toString().trim());
                    loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    String userLoginID = request.getSession(false).getAttribute("UserID").toString().trim();
                    String loginFullname = request.getSession(false).getAttribute("sesFullname").toString().trim();
                    String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                    String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                    String sIP_Request = CommonFunction.getClientIpLogin(request);
                    CERTIFICATION_DATA_ATTR tempLogReq;
                    if (null != idParam) {
                        switch (idParam) {
                            case "registrationcert_factor": {
                                //<editor-fold defaultstate="collapsed" desc="registrationcert_factor">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = "";
                                    String sTOKEN_SN = "";
                                    String CheckCHANGE_KEY = "1";
                                    String sPRIVATE_KEY = "";
                                    String CheckPRIVATE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckPRIVATE_KEY"));
                                    String BRANCH_ID = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ID"));
                                    String pCSR = EscapeUtils.CheckTextNull(request.getParameter("pCSR"));
                                    String pCERTIFICATION_PURPOSE = EscapeUtils.CheckTextNull(request.getParameter("pCERTIFICATION_PURPOSE"));
                                    String pPKI_FORMFACTOR = EscapeUtils.CheckTextNull(request.getParameter("pPKI_FORMFACTOR"));
                                    String CertProfileID = EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                    String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
                                    String pPERSONAL_NAME = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_NAME"));
                                    String pCOMPANY_NAME = EscapeUtils.CheckTextNull(request.getParameter("pCOMPANY_NAME"));
                                    String PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                    String EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
                                    String DN = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                    String pDOMAIN_NAME = EscapeUtils.CheckTextNull(request.getParameter("pDOMAIN_NAME"));
                                    String pTAX_CODE = EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                    String pDECISION = EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                    String pBUDGET_CODE = EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                    String pP_ID = EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                    String pPASSPORT = EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                    String pCCCD = EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                    String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                    String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                    String pCERT_MODE = EscapeUtils.CheckTextNull(request.getParameter("pCERTIFICATION_MODE"));
                                    String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                    String pPROVINCE_DESC = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_DESC"));
                                    String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                    String sRESPONSE_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                    if("".equals(pPKI_FORMFACTOR))
                                    {
                                        sRESPONSE_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_NO_FORMFACTOR;
                                    }
                                    // case bug
//                                    sRESPONSE_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_NO_FORMFACTOR;
                                    if(sRESPONSE_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                    {
                                        Config conf = new Config();
                                        boolean booRSSP_ACCESS_ENABLED = false;
                                        if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                                            && pCERT_MODE.equals(Definitions.CONFIG_RSSP_CONNECT_MODE_DIRECT)) {
                                            String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                            if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                booRSSP_ACCESS_ENABLED = true;
                                            }
                                        }
                                        if(booRSSP_ACCESS_ENABLED == false) {
                                            boolean isProcess = true;
                                            boolean isValidCSR = true;
                                            boolean checkCSRNotExists = true;
                                            String strDNSName = "";
                                            String strPasswordP12 = "";
                                            if(!"".equals(pCERTIFICATION_PURPOSE)) {
                                                if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                    CheckPRIVATE_KEY = "1";
                                                    if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_SSL))) {
                                                        ID = String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID);
                                                        sTOKEN_SN = Definitions.CONFIG_TOKEN_SSL_SN;
                                                    } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING)))
                                                    {
                                                        ID = String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID);
                                                        sTOKEN_SN = Definitions.CONFIG_TOKEN_CODESIGNNING_SN;
                                                    } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_DEVICE)))
                                                    {
                                                        ID = String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID);
                                                        sTOKEN_SN = Definitions.CONFIG_TOKEN_DEVICE_SN;
                                                    } else {
                                                        ID = String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID);
                                                        sTOKEN_SN = Definitions.CONFIG_TOKEN_SIGNSERVER_SN;
                                                    }
                                                    if(!"".equals(pCSR))
                                                    {
                                                        CheckPRIVATE_KEY = "0";
                                                        String sKeySizeDB;
                                                        isValidCSR = false;
                                                        CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                                        db.S_BO_GET_ALGORITHM_KEY_SIZE(CertProfileID, rsCert);
                                                        if(rsCert[0].length > 0)
                                                        {
                                                            sKeySizeDB = EscapeUtils.CheckTextNull(rsCert[0][0].KEY_SIZE);
                                                            String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(pCSR);
                                                            isValidCSR = sKeySizeDB.equals(sKeySizeCSR);
                                                        }
                                                    } else {
                                                        strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                                    }
                                                } else if(CommonFunction.checkHardTokenIDEnabled(Integer.parseInt(pPKI_FORMFACTOR)) == true
                                                    || (Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM
                                                        && pCERT_MODE.equals(Definitions.CONFIG_RSSP_CONNECT_MODE_AC))
                                                    || (Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                                                        && pCERT_MODE.equals(Definitions.CONFIG_RSSP_CONNECT_MODE_AC)))
                                                {
                                                    ID = String.valueOf(Definitions.CONFIG_TOKEN_UNASSIGN_ID);
                                                    sTOKEN_SN = Definitions.CONFIG_TOKEN_UNASSIGN_SN;
                                                } else if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM
                                                    && pCERT_MODE.equals(Definitions.CONFIG_RSSP_CONNECT_MODE_INHOUSE))
                                                {
                                                    ID = String.valueOf(Definitions.CONFIG_TOKEN_INHOUSE_ID);
                                                    sTOKEN_SN = Definitions.CONFIG_TOKEN_SN_INHOUSE;
                                                } else {}
                                                String sTempDevice = pPERSONAL_NAME;
                                                if("".equals(sTempDevice)) {
                                                    sTempDevice = pCOMPANY_NAME;
                                                }
                                                if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_WEB_SERVER))) {
                                                    pDEVICE = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_WEBSERVER + sTempDevice;
                                                } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_X_ROAD_AUTH))) {
                                                    pDEVICE = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_X_ROAD_AUTH + sTempDevice;
                                                } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_WEB_CLIENT))) {
                                                    pDEVICE = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_WEBCLIENT + sTempDevice;
                                                } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_X_ROAD_SIGN))) {
                                                    pDEVICE = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_X_ROAD_SIGN + sTempDevice;
                                                } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING_GOV))) {
                                                    pDEVICE = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CODE_SIGNING + sTempDevice;
                                                } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_STAFF))
                                                    || pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE))
                                                    || pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_PERSONAL))) {
                                                    pDEVICE = "";
                                                }
                                            }
                                            String pCREATE_USER = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                            String strIsPushNotiApprove = "0";
                                            boolean isAccessAgency = true;
                                            if (isAccessAgency == true) {
                                                if(!"".equals(ID) || !"".equals(sTOKEN_SN)) {
                                                    String hdfOwnerID = EscapeUtils.CheckTextNull(request.getParameter("hdfOwnerID"));
                                                    String CACoreSubject = EscapeUtils.CheckTextNull(request.getParameter("CACoreSubject"));
                                                    String pPAST_CERTIFICATE_ID = Definitions.CONFIG_CERTIFICATE_PAST_CERTIFICATE_ID;
                                                    String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION);
                                                    String pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                    String pCERTIFICATION_SN = "";
                                                    boolean isValidMSTForExists = true;
                                                    String isCheckMSTExists = conf.GetPropertybyCode(Definitions.CONFIG_DENIED_WHEN_EXISTS_TAXCODE_REGISTER_CERT);
                                                    if("2".equals(isCheckMSTExists)) {
                                                        String sOwnerExsist = db.S_BO_CHECK_OWNER_HAVE_EXISTS_CERTIFICATION(pENTERPRISE_ID, pPERSONAL_ID, Integer.parseInt(pPKI_FORMFACTOR));
                                                        if("1".equals(sOwnerExsist)) {
                                                            isValidMSTForExists = false;
                                                        }
                                                    }
                                                    if(isValidMSTForExists == true)
                                                    {
                                                        if(isValidCSR == true) {
                                                            if(checkCSRNotExists == true) {
                                                                //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                                                CommonFunction.LogDebugString(log, "REGISTRATION-CERTIFICATION", "REQUEST: " + "SUBJECT: " + DN
                                                                    + "; PERSONAL_NAME: " + pPERSONAL_NAME + "; COMPANY_NAME: " + pCOMPANY_NAME
                                                                    + "; pENTERPRISE_ID: " + pENTERPRISE_ID + "; pPERSONAL_ID: " + pPERSONAL_ID
                                                                    + "; DOMAIN_NAME: " + pDOMAIN_NAME + "; PAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                                    + "; pCCCD: " + pCCCD + "; CERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID
                                                                    + "; PKI_FORMFACTOR: " + pPKI_FORMFACTOR + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                                    + "; CACoreSubject: " + CACoreSubject + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                                    + "; PROVINCE_ID: " + pPROVINCE_ID + "; TOKEN_SN: " + sTOKEN_SN);
                                                                tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                                objectMapper = new ObjectMapper();
                                                                tempLogReq.personalName = pPERSONAL_NAME;
                                                                tempLogReq.companyName = pCOMPANY_NAME;
                                                                tempLogReq.enterpriseID = pENTERPRISE_ID;
                                                                tempLogReq.personalID = pPERSONAL_ID;
                                                                tempLogReq.emailContract = EMAIL_CONTRACT;
                                                                tempLogReq.phoneContract = PHONE_CONTRACT;
                                                                tempLogReq.issuerSubject = CACoreSubject;
                                                                tempLogReq.subjectDn = DN;
                                                                tempLogReq.tokenSn = sTOKEN_SN;
                                                                tempLogReq.provinceName = pPROVINCE_DESC;
                                                                tempLogReq.pkiFromFactorId = Integer.parseInt(pPKI_FORMFACTOR);
                                                                tempLogReq.typeName = pCERT_ATTR_TYPE_CODE;
                                                                String strReq = objectMapper.writeValueAsString(tempLogReq);
                                                                db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                                        Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                                        Definitions.CONFIG_LOG_FUNCTIONALITY_REQUEST_ISSUE, strReq,
                                                                        loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                                ATTRIBUTE_VALUES valueATTR;
                                                                ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                                dataATTR.setCertificationData(tempLogReq);
                                                                valueATTR = new ATTRIBUTE_VALUES();
                                                                valueATTR.setTokenSn(sTOKEN_SN);
                                                                valueATTR.setChangeKeyEnabled(true);
                                                                valueATTR.setKeepCertificateSNEnabled(false);
                                                                valueATTR.setTypeName(pCERT_ATTR_TYPE_CODE);
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                valueATTR.setCreateUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setCreateDt(new Date());
                                                                valueATTR.setAttributeData(dataATTR);
                                                                //</editor-fold>

                                                                String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                String sParam = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                                //<editor-fold defaultstate="collapsed" desc="### OWNER PROCESS">
                                                                String dnUniqueEnabled = "0";
                                                                CERTIFICATION_PROFILE[][] rsProfile;
                                                                rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                if(rsProfile[0].length > 0) {
                                                                    CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                                                    db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(rsProfile[0][0].CERTIFICATION_AUTHORITY_ID), rsCA);
                                                                    if(rsCA[0].length > 0) {
                                                                        dnUniqueEnabled = rsCA[0][0].ENFORCE_UNIQUE_DN;
                                                                    }
                                                                }
                                                                if("".equals(hdfOwnerID))
                                                                {
                                                                    boolean isCallOwnerInsert = true;
//                                                                    String sEnterpriseID = "";
//                                                                    String sPersonalID = "";
//                                                                    if(!"".equals(pTAX_CODE)) {
//                                                                        sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE + pTAX_CODE;
//                                                                    }
//                                                                    if(!"".equals(pBUDGET_CODE)) {
//                                                                        sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE + pBUDGET_CODE;
//                                                                    }
//                                                                    if(!"".equals(pDECISION)) {
//                                                                        sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION + pDECISION;
//                                                                    }
//                                                                    if(!"".equals(pP_ID)) {
//                                                                        sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND + pP_ID;
//                                                                    }
//                                                                    if(!"".equals(pPASSPORT)) {
//                                                                        sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT + pPASSPORT;
//                                                                    }
//                                                                    if(!"".equals(pCCCD)) {
//                                                                        sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID + pCCCD;
//                                                                    }
                                                                    String pCERTIFICATION_OWNER_TYPE_ID = "";
                                                                    if(!pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_PERSONAL_GOV))
                                                                        && !pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_ENTERPRISE_GOV)))
                                                                    {
                                                                        if(!"".equals(pENTERPRISE_ID) && "".equals(pPERSONAL_ID)) {
                                                                            pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_ENTERPRISE);
                                                                        }
                                                                        if("".equals(pENTERPRISE_ID) && !"".equals(pPERSONAL_ID)) {
                                                                            pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL);
                                                                        }
                                                                        if(!"".equals(pENTERPRISE_ID) && !"".equals(pPERSONAL_ID)) {
                                                                            pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL);
                                                                        }
                                                                        if("".equals(pENTERPRISE_ID) && "".equals(pPERSONAL_ID))
                                                                        {
                                                                            isCallOwnerInsert = false;
                                                                        }
                                                                    } else {
                                                                        if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_PERSONAL_GOV)))
                                                                        {
                                                                            pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL_GOV);
                                                                        } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_ENTERPRISE_GOV)))
                                                                        {
                                                                            pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_ENTERPRISE_GOV);
                                                                        }
                                                                    }
                                                                    if(!"".equals(pCERTIFICATION_OWNER_TYPE_ID)) {
                                                                        if("1".equals(dnUniqueEnabled)) {
                                                                            int isCheckUnique = db.S_BO_CHECK_ENFORCE_UNIQUE_DN(Integer.parseInt(pCERTIFICATION_OWNER_TYPE_ID), pENTERPRISE_ID,
                                                                                pPERSONAL_ID, EMAIL_CONTRACT, DN, 0);
                                                                            if(isCheckUnique != 0) {
                                                                                sParam = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUBJECT_DN_INVALID;
                                                                            }
                                                                        }
                                                                        if(sParam.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                                            if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                                if(!"".equals(pCSR)) {
                                                                                    String sPublicKeyHard = CommonFunction.getPublicKeyHasrCSR(pCSR);
                                                                                    int checkPublicKey = db.S_BO_CHECK_OWNER_HAVE_EXISTS_PUBLIC_KEY_HASH(Integer.parseInt(pCERTIFICATION_OWNER_TYPE_ID), pENTERPRISE_ID,
                                                                                        pPERSONAL_ID, EMAIL_CONTRACT, sPublicKeyHard, 0);
                                                                                    if(checkPublicKey != 0) {
                                                                                        sParam = String.valueOf(Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_CSR_EXISTS);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if(sParam.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                                                    {
                                                                        String pMESSAGING_QUEUE_FUNCTION_ID = String.valueOf(Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_REGISTRATION_OWNER);
                                                                        String[] pRESPONSE_CODE_NAME = new String[1];
                                                                        int[] pCERTIFICATION_OWNER_ID = new int[1];
                                                                        int[] pMESSAGING_QUEUE_ID = new int[1];
                                                                        String sLocation = CommonFunction.getLocationInDN(DN).trim();
                                                                        String pADDRESS;
                                                                        if(!"".equals(sLocation)) {
                                                                            pADDRESS = CommonFunction.replaceStringCharaterSpecialDN(sLocation, true, true) + ", " + CommonFunction.getStateOrProvinceInDN(DN);
                                                                        } else {
                                                                            pADDRESS = CommonFunction.getStateOrProvinceInDN(DN);
                                                                        }
                                                                        if(isCallOwnerInsert == true) {
                                                                            String pREPRESENTATIVE = "";
                                                                            String pREPRESENTATIVE_POSITION = "";
                                                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR OWNER ">
                                                                            CERTIFICATION_OWNER_DATA_ATTR tempLogReq_Owner = new CERTIFICATION_OWNER_DATA_ATTR();
                                                                            tempLogReq_Owner.personalName = pPERSONAL_NAME;
                                                                            tempLogReq_Owner.companyName = pCOMPANY_NAME;
                                                                            tempLogReq_Owner.taxCode = pTAX_CODE;
                                                                            tempLogReq_Owner.decision = pDECISION;
                                                                            tempLogReq_Owner.budgetCode = pBUDGET_CODE;
                                                                            tempLogReq_Owner.personalCode = pP_ID;
                                                                            tempLogReq_Owner.passportCode = pPASSPORT;
                                                                            tempLogReq_Owner.citizenID = pCCCD;
                                                                            tempLogReq_Owner.emailContract = EMAIL_CONTRACT;
                                                                            tempLogReq_Owner.phoneContract = PHONE_CONTRACT;
                                                                            tempLogReq_Owner.address = pADDRESS;
                                                                            tempLogReq_Owner.representative = pREPRESENTATIVE;
                                                                            tempLogReq_Owner.representativePosition = pREPRESENTATIVE_POSITION;
                                                                            tempLogReq_Owner.typeName = Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CODE_REGISTRATION_OWNER;
                                                                            tempLogReq_Owner.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING;
                                                                            tempLogReq_Owner.createUser = loginFullname + " (" + loginUID + ")";
                                                                            tempLogReq_Owner.createDt = new Date();
                                                                            //</editor-fold>
                                                                            String sOwnerUUID = CommonFunction.getUUIDV4();
                                                                            db.S_BO_CERTIFICATION_OWNER_INSERT(pPERSONAL_NAME, pCOMPANY_NAME,
                                                                                pENTERPRISE_ID, pPERSONAL_ID, pCERTIFICATION_OWNER_TYPE_ID, PHONE_CONTRACT,
                                                                                EMAIL_CONTRACT, loginUID, pADDRESS, pREPRESENTATIVE, pREPRESENTATIVE_POSITION,
                                                                                pMESSAGING_QUEUE_FUNCTION_ID, objectMapper.writeValueAsString(tempLogReq_Owner),
                                                                                sOwnerUUID, pRESPONSE_CODE_NAME, pCERTIFICATION_OWNER_ID, pMESSAGING_QUEUE_ID);
                                                                            CommonFunction.LogDebugString(log, "CERTIFICATION_OWNER_INSERT Result", pRESPONSE_CODE_NAME[0]);
                                                                            if (!"0".equals(pRESPONSE_CODE_NAME[0])) {
                                                                                if (pRESPONSE_CODE_NAME[0].trim().equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_OWNER_EXISTS))
                                                                                {
                                                                                    hdfOwnerID = String.valueOf(pCERTIFICATION_OWNER_ID[0]);
                                                                                } else {
                                                                                    sParam = pRESPONSE_CODE_NAME[0];
                                                                                }
                                                                            } else {
                                                                                hdfOwnerID = String.valueOf(pCERTIFICATION_OWNER_ID[0]);
                                                                            }
                                                                        } else {
                                                                            hdfOwnerID = "1";
                                                                        }
                                                                    }
                                                                } else {
                                                                    if("1".equals(dnUniqueEnabled)) {
                                                                        int isCheckUnique = db.S_BO_CHECK_ENFORCE_UNIQUE_DN(0, null, null, null, DN, Integer.parseInt(hdfOwnerID));
                                                                        if(isCheckUnique != 0) {
                                                                            sParam = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUBJECT_DN_INVALID;
                                                                        }
                                                                    }
                                                                    if(sParam.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                                        if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                            if(!"".equals(pCSR)) {
                                                                                String sPublicKeyHard = CommonFunction.getPublicKeyHasrCSR(pCSR);
                                                                                int checkPublicKey = db.S_BO_CHECK_OWNER_HAVE_EXISTS_PUBLIC_KEY_HASH(0, null, null, null, sPublicKeyHard, Integer.parseInt(hdfOwnerID));
                                                                                if(checkPublicKey != 0) {
                                                                                    sParam = String.valueOf(Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_CSR_EXISTS);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if(sParam.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                                        CERTIFICATION_OWNER[][] rs = new CERTIFICATION_OWNER[1][];
                                                                        db.S_BO_CERTIFICATION_OWNER_DETAIL(hdfOwnerID, sessLanguage, rs);
                                                                        if(rs[0].length > 0)
                                                                        {
                                                                            if(rs[0][0].CERTIFICATION_OWNER_STATE_ID == Definitions.CONFIG_CERTIFICATION_OWNER_STATE_ID_DISPOSED
                                                                                || rs[0][0].CERTIFICATION_OWNER_STATE_ID == Definitions.CONFIG_CERTIFICATION_OWNER_STATE_ID_DECLINED)
                                                                            {
                                                                                sParam = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_OWNER_INVALID;
                                                                            }
                                                                        } else {
                                                                            sParam = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_OWNER_INVALID;
                                                                        }
                                                                    }
                                                                }
                                                                //</editor-fold>

                                                                if(sParam.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                                                {
                                                                    if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                                    {
                                                                        sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(ID), CertProfileID, sTOKEN_SN,
                                                                            pCERTIFICATION_SN, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME, pTAX_CODE, pBUDGET_CODE, pP_ID,
                                                                            pPASSPORT, DN, CACoreSubject, PHONE_CONTRACT, EMAIL_CONTRACT, BRANCH_ID,
                                                                            pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, "",
                                                                            strReqValueATTR, pCREATE_USER, loginUID, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, pCSR,
                                                                            CheckPRIVATE_KEY, CheckCHANGE_KEY, sPRIVATE_KEY, Integer.parseInt(pPKI_FORMFACTOR),
                                                                            pDEVICE, hdfOwnerID, pCCCD, null, pDECISION, pPERSONAL_ID, pENTERPRISE_ID);
                                                                    }
                                                                    else if(CommonFunction.checkHardTokenIDEnabled(Integer.parseInt(pPKI_FORMFACTOR)) == true
                                                                        || Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM
                                                                        || (Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                                                                            && pCERT_MODE.equals(Definitions.CONFIG_RSSP_CONNECT_MODE_AC)))
                                                                    {
                                                                        int intOTPNumn = 8;
                                                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                                                        if (sessGeneralPolicy[0].length > 0) {
                                                                            for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_ACTIVATION_CODE_LENGTH))
                                                                                {
                                                                                    intOTPNumn = Integer.parseInt(rsPolicy1.VALUE);
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        sParam = "1000";
                                                                        while ("1000".equals(sParam)) {
                                                                            try {
                                                                                String sOTP = CommonFunction.getRandomOTP(intOTPNumn);
                                                                                sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(ID), CertProfileID, sTOKEN_SN.trim(),
                                                                                    pCERTIFICATION_SN.trim(), pPERSONAL_NAME.trim(), pCOMPANY_NAME.trim(), pDOMAIN_NAME.trim(),
                                                                                    pTAX_CODE.trim(), pBUDGET_CODE.trim(), pP_ID.trim(),
                                                                                    pPASSPORT.trim(), DN, CACoreSubject.trim(), PHONE_CONTRACT.trim(), EMAIL_CONTRACT.trim(), BRANCH_ID,
                                                                                    pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, sOTP,
                                                                                    strReqValueATTR, pCREATE_USER.trim(), loginUID, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, "",
                                                                                    CheckPRIVATE_KEY, CheckCHANGE_KEY, sPRIVATE_KEY, Integer.parseInt(pPKI_FORMFACTOR),
                                                                                    pDEVICE, hdfOwnerID, pCCCD, null, pDECISION, pPERSONAL_ID, pENTERPRISE_ID);
                                                                            } catch (Exception e) {
                                                                                if (e.getMessage().contains(Definitions.CONFIG_MYSQL_UNIQUE_ACTIVATION_CODE)) {
                                                                                    sParam = "1000";
                                                                                } else {
                                                                                    sParam = Definitions.CONFIG_EXCEPTION_STRING_ERROR;
                                                                                    CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if ("0".equals(sParam)) {
                                                                        String notCancel = EscapeUtils.CheckTextNull(request.getParameter("notCancel"));
                                                                        if("1".equals(notCancel)) {
                                                                            db.S_BO_CERTIFICATION_UPDATE_NO_CANCEL_COMMITMENT(pCERTIFICATE_ID[0], "1", userLoginID);
                                                                        }
                                                                        
                                                                        //<editor-fold defaultstate="collapsed" desc="### REPRESENTIVE PROCESS">
                                                                        String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
                                                                        CommonFunction.LogDebugString(log, "Register Cert - outner profile contact", sRepresentEnabled);
                                                                        if("1".equals(sRepresentEnabled)) {
                                                                            CommonFunction.LogDebugString(log, "Register Cert - inner profile contact", sRepresentEnabled);
                                                                            String registerAddressGPKD = EscapeUtils.CheckTextNull(request.getParameter("registerAddressGPKD"));
                                                                            String registerFullname = EscapeUtils.CheckTextNull(request.getParameter("registerFullname"));
                                                                            String registerRole = EscapeUtils.CheckTextNull(request.getParameter("registerRole"));
                                                                            String registerCMND = EscapeUtils.CheckTextNull(request.getParameter("registerCMND"));
                                                                            String registerIssuedDate = EscapeUtils.CheckTextNull(request.getParameter("registerIssuedDate"));
                                                                            String registerIssuedPlace = EscapeUtils.CheckTextNull(request.getParameter("registerIssuedPlace"));
                                                                            String registerIssuedAdress = EscapeUtils.CheckTextNull(request.getParameter("registerIssuedAdress"));
                                                                            ProfileContactInfoJson profileContact = new ProfileContactInfoJson();
                                                                            profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(registerFullname, true);
                                                                            profileContact.RepresentativePhone = "";
                                                                            profileContact.PIDIssuedBy = CommonFunction.replaceCharaterSpecialJson(registerIssuedPlace, true);
                                                                            profileContact.PIDDate = registerIssuedDate;
                                                                            profileContact.PID = registerCMND;
                                                                            profileContact.AddressLicense = CommonFunction.replaceCharaterSpecialJson(registerAddressGPKD, true);
                                                                            profileContact.RepresentativeEmail = "";
                                                                            profileContact.ContactName = "";
                                                                            profileContact.Position = CommonFunction.replaceCharaterSpecialJson(registerRole, true);
                                                                            profileContact.Address = CommonFunction.replaceCharaterSpecialJson(registerIssuedAdress, true);
                                                                            objectMapper = new ObjectMapper();
                                                                            CommonFunction.LogDebugString(log, "Register Cert - json profileContact", objectMapper.writeValueAsString(profileContact));
                                                                            db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(pCERTIFICATE_ID[0], objectMapper.writeValueAsString(profileContact), userLoginID);
                                                                        }
                                                                        String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)){
                                                                            String registerNCRepresentative = EscapeUtils.CheckTextNull(request.getParameter("registerNCRepresentative"));
                                                                            String registerNCAddress = EscapeUtils.CheckTextNull(request.getParameter("registerNCAddress"));
                                                                            ProfileContactInfoJson profileContact = new ProfileContactInfoJson();
                                                                            profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(registerNCRepresentative, true);
                                                                            profileContact.Address = CommonFunction.replaceCharaterSpecialJson(registerNCAddress, true);
                                                                            objectMapper = new ObjectMapper();
                                                                            db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(pCERTIFICATE_ID[0], objectMapper.writeValueAsString(profileContact), userLoginID);
                                                                        }
                                                                        //</editor-fold>
                                                                        
                                                                        //<editor-fold defaultstate="collapsed" desc="### PROPERTIES SAN CERT">
                                                                        if(!"".equals(pCOMPONENT_SAN)) {
                                                                            String[] pCompSanSub = pCOMPONENT_SAN.split("@@@");
                                                                            List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes = new ArrayList<>();
                                                                            for (String sPlitValue : pCompSanSub) {
                                                                                if(!"".equals(sPlitValue)) {
                                                                                    String sKey = "";
                                                                                    if(!"".equals(sPlitValue.split("###")[0].trim())) {
                                                                                        sKey = sPlitValue.split("###")[0].trim();
                                                                                    }
                                                                                    objectMapper = new ObjectMapper();
                                                                                    CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                                                    attribute.setKey(sKey);
                                                                                    attribute.setValue(sPlitValue.split("###")[1]);
                                                                                    attributes.add(attribute);
                                                                                }
                                                                            }
                                                                            if(attributes.size() > 0) {
                                                                                strDNSName = "{\"attributes\":" + objectMapper.writeValueAsString(attributes) + "}";
                                                                                db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(pCERTIFICATE_ID[0]), strDNSName, loginUID);
                                                                            }
                                                                        }
                                                                        //</editor-fold>

                                                                        //<editor-fold defaultstate="collapsed" desc="### SHARED_MODE CERT">
                                                                        boolean pSHARED_MODE_ENABLED = false;
                                                                        BRANCH[][] rsBranchShare = new BRANCH[1][];
                                                                        db.S_BO_BRANCH_DETAIL(BRANCH_ID, rsBranchShare);
                                                                        if(rsBranchShare[0].length > 0) {
                                                                            String sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranchShare[0][0].CERTIFICATION_POLICY_PROPERTIES);
                                                                            if(!"".equals(sCERT_POLICY_PROPERTIES)) {
                                                                                pSHARED_MODE_ENABLED = CommonFunction.getShareModeEnabledCert(sCERT_POLICY_PROPERTIES);
                                                                            }
                                                                        }
                                                                        String pSHARED_MODE = pSHARED_MODE_ENABLED ? "1" : "0";
                                                                        db.S_BO_CERTIFICATION_UPDATE(pCERTIFICATE_ID[0], CertProfileID, "", "", "", pTAX_CODE, pBUDGET_CODE,
                                                                            pP_ID, pPASSPORT, "", "", "", "", "", "", loginUID, "", "", "", "", pSHARED_MODE, pCCCD, pDECISION,
                                                                            pENTERPRISE_ID, pPERSONAL_ID);
                                                                        //</editor-fold>
                                                                        
                                                                        //<editor-fold defaultstate="collapsed" desc="### PUBLIC KEY HASH">
                                                                        if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                            if(!"".equals(pCSR)) {
                                                                                String sKeySizeCSR = CommonFunction.getPublicKeyHasrCSR(pCSR);
                                                                                db.S_BO_CERTIFICATION_UPDATE_CSR_INFO(pCERTIFICATE_ID[0], sKeySizeCSR, loginUID);
                                                                            }
                                                                        }
                                                                        //</editor-fold>

                                                                        int pCERTIFICATION_PURPOSE_ID = 0;
                                                                        String pCERTIFICATION_ID = "";
                                                                        boolean sPUSH_NOTICE_ENABLED = false;
                                                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
                                                                        db.S_BO_CERTIFICATION_APPROVED_DETAIL(EscapeUtils.escapeHtml(String.valueOf(pCERTIFICATE_ATTR_ID[0])), sessLanguage, rs);
                                                                        if (rs[0].length > 0) {
                                                                            sPUSH_NOTICE_ENABLED = rs[0][0].PUSH_NOTICE_ENABLED;
                                                                            pCERTIFICATION_PURPOSE_ID = rs[0][0].CERTIFICATION_PURPOSE_ID;
                                                                            pCERTIFICATION_ID = String.valueOf(rs[0][0].ID);
                                                                        }
                                                                        String sJRBConfig = "";
                                                                        String sDiscountRateOption = "0";
                                                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                        if (sessGeneralPolicy[0].length > 0) {
                                                                            for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT))
                                                                                {
                                                                                    sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                }
                                                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION)) {
                                                                                    sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                }
                                                                            }
                                                                        }
                                                                        //<editor-fold defaultstate="collapsed" desc="### FILE MANAGER INSERT">
                                                                        String sJRB_Source =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
                                                                        if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY))
                                                                        {
                                                                            SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                                            if (cartToken != null) {
                                                                                String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
                                                                                String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
                                                                                String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
                                                                                String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
                                                                                String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
                                                                                String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
                                                                                String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
                                                                                String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
                                                                                String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_UP);
                                                                                String idUUID_Temp = Definitions.CONFIG_JACK_RABBIT_UUID_SAMPLE;// "4D4B4901-BF4C-4236-A9BA-AF2E0D4F5F33";
                                                                                ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                                                                for (FILE_PROFILE_DATA mhIP : ds) {
                        //                                                            File fileUp = new File(mhIP.FILE_URL);
                                                                                    String sFileData = EscapeUtils.CheckTextNull(mhIP.FILE_URL);//CommonFunction.encodeFileToBase64Binary(fileUp);
                                                                                    CloseableHttpResponse pHttpRes = ConnectFileToPartner.upFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                                                                        sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                                                                        sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, idUUID_Temp,
                                                                                        mhIP.FILE_NAME, sFileData);
                                                                                    InputStream isStr = pHttpRes.getEntity().getContent();
                                                                                    String resultUUID = IOUtils.toString(isStr);
                                                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                                                    db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, resultUUID, sJRBConfig,
                                                                                        EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE), mhIP.FILE_NAME,
                                                                                        (int) mhIP.FILE_SIZE, pCERTIFICATION_ID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
                                                                                }
                                                                                request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                                            }
                                                                        } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
                                                                            String sJRB_Host =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                                            String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                                            String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                                            String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                                            String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                                            String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                                            String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                                            SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                                            if (cartToken != null) {
                                                                                ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                                                                for (FILE_PROFILE_DATA mhIP : ds) {
                                                                                    JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                                                                        Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                                    InputStream isFILE_STREAM = new ByteArrayInputStream(mhIP.FILE_STREAM);
                                                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                                                    JackRabbitCommon.getInstance(jcrConfig).uploadFileThread(mhIP.FILE_NAME, mhIP.FILE_MIMETYPE, isFILE_STREAM, mhIP, sJRBConfig,
                                                                                        pCERTIFICATION_ID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
//                                                                                    if(jrbFile != null) {
//                                                                                        int[] pFILE_MANAGER_ID = new int[1];
//                                                                                        db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, jrbFile.getUuid(), sJRBConfig, EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE),
//                                                                                            jrbFile.getFileName(), (int) mhIP.FILE_SIZE, pCERTIFICATION_ID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
//                                                                                    }
                                //                                                    JCRManager. .destroyAllJCR();
                                                                                }
                                                                                request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                                            }
                                                                        } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
                                                                            String sJRB_Host =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                                            String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                                            String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                                            String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                                            String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                                            String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                                            String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                                            SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                                            if (cartToken != null) {
                                                                                ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                                                                for (FILE_PROFILE_DATA mhIP : ds) {
                                                                                    ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                                                                                        Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                                    InputStream isFILE_STREAM = new ByteArrayInputStream(mhIP.FILE_STREAM);
                                                                                    String[] sReturnJRB = new String[2];
                                                                                    vn.mobileid.fms.client.JCRFile jrbFile = openJRB.uploadFile(EscapeUtils.CheckTextNull(mhIP.FILE_NAME),
                                                                                        EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE), isFILE_STREAM, sReturnJRB);
                                                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                                                    db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, sReturnJRB[0].trim(), sJRBConfig, EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE),
                                                                                        sReturnJRB[1].trim(), (int) mhIP.FILE_SIZE, pCERTIFICATION_ID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
                                //                                                    JCRManager. .destroyAllJCR();
                                                                                }
                                                                                request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                                            }
                                                                        } else {
                                                                        }
                                                                        //</editor-fold>

                                                                        if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                            //<editor-fold defaultstate="collapsed" desc="### CA LOGIN APPROVE">
                                                                            boolean isCAApprove = false;
                                                                            if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                                            {
                                                                                isCAApprove = true;
                                                                            } else {
                                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                {
                                                                                    isCAApprove = true;
                                                                                }
                                                                            }
                                                                            if(isCAApprove == true)
                                                                            {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveCADt(new Date());
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                                                {
                                                                                    String CheckPUSH_NOTICE = EscapeUtils.CheckTextNull(request.getParameter("CheckPUSH_NOTICE"));
                                                                                    if("".equals(CheckPUSH_NOTICE)) {
                                                                                        CheckPUSH_NOTICE = "1";
                                                                                    }
                                                                                    db.S_BO_CERTIFICATION_UPDATE_AMOUNT(Integer.parseInt(pCERTIFICATION_ID), "", CheckPUSH_NOTICE, loginUID);
                                                                                }
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                if ("0".equals(sApprove)) {
                                                                                    if ("1".equals(sDiscountRateOption)) {
                                                                                        CommonReferServlet.updateDiscountRate(pCERTIFICATION_ID, BRANCH_ID, CertProfileID,
                                                                                            pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, loginUID, pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                                    }
                                                                                    // set COMMIT TRUE File Attachment
                                                                                    String[] pRESPONSE_CODE_FILE = new String[1];
                                                                                    db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(pCERTIFICATE_ATTR_ID[0], loginUID, pRESPONSE_CODE_FILE);
                                                                                    if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                                        String isActiveSignServer = "0";
                                                                                        GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                        if (sessGeneralPolicy1[0].length > 0) {
                                                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                            {
                                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                                    isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if("1".equals(isActiveSignServer)) {
                                                                                            CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT.trim(), sessLanguage);
                                                                                        } else {
                                                                                            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                            dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                            int[] intWSRes = new int[1];
                                                                                            String[] sWSRes = new String[1];
                                                                                            ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                            if (intWSRes[0] == 0) { }
                                                                                            else {
                                                                                                isProcess = false;
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        if (sPUSH_NOTICE_ENABLED == true) {
                                                                                            int[] intRes = new int[1];
                                                                                            String[] sRes = new String[1];
                                                                                            ConnectConnector.SendMailOTP(pCERTIFICATION_ID, intRes, sRes);
                                                                                        }
                                                                                        isProcess = true;
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                            }
                                                                            //</editor-fold>
                                                                        } else {
                                                                            //<editor-fold defaultstate="collapsed" desc="### AGENCY LOGIN APPROVE">
                                                                            boolean checkCallApproveCA = false;
                                                                            String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                                            objectMapper = new ObjectMapper();
                                                                            if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                                            {
                                                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                    valueATTR.setApproveDt(new Date());
                                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                    if("0".equals(paramPre)) {
                                                                                        checkCallApproveCA = true;
                                                                                    }
                                                                                } else {
                                                                                    int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                    BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                    db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                    if(rsUserApprve[0].length > 0) {
                                                                                        for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                            if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                                approveChilrenID = item.ID;
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_IN_AGENCY_APPROVED);
                                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                    valueATTR.setApproveDt(new Date());
                                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                    String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID, approveChilrenID);
                                                                                    if("0".equals(paramAgency)) {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                            valueATTR.setApproveDt(new Date());
                                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                            if("0".equals(paramPre)) {
                                                                                                checkCallApproveCA = true;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                    {
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                        if("0".equals(paramPre)) {
                                                                                            checkCallApproveCA = true;
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                                    if(intApprove == 1) {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                                            int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                            BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                            db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                            if(rsUserApprve[0].length > 0) {
                                                                                                for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                                    if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                                        approveChilrenID = item.ID;
                                                                                                        break;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_IN_AGENCY_APPROVED);
                                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                            valueATTR.setApproveDt(new Date());
                                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                            String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID, approveChilrenID);
                                                                                            if("0".equals(paramAgency)) {
                                                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                                {
                                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                                    valueATTR.setApproveDt(new Date());
                                                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                                    if("0".equals(paramPre)) {
                                                                                                        checkCallApproveCA = true;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            if(checkCallApproveCA == true) {
                                                                                //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                                boolean autoApproveCA = false;
                                                                                rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                                db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                                CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                                objectMapper = new ObjectMapper();
                                                                                if(rsProfile[0].length > 0) {
                                                                                    autoApproveCA = CommonFunction.getApproveCAOfProfile(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME), sessPolicyCert_Data);
                                                                                }
                                                                                if(autoApproveCA == true) {
                                                                                    if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true) {
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveCADt(new Date());
                                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                        if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                                                        {
                                                                                            String CheckPUSH_NOTICE = EscapeUtils.CheckTextNull(request.getParameter("CheckPUSH_NOTICE"));
                                                                                            if("".equals(CheckPUSH_NOTICE)) {
                                                                                                CheckPUSH_NOTICE = "1";
                                                                                            }
                                                                                            db.S_BO_CERTIFICATION_UPDATE_AMOUNT(Integer.parseInt(pCERTIFICATION_ID), "", CheckPUSH_NOTICE, loginUID);
                                                                                        }
                                                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                        if ("0".equals(sApprove)) {
                                                                                            if ("1".equals(sDiscountRateOption)) {
                                                                                                CommonReferServlet.updateDiscountRate(pCERTIFICATION_ID, BRANCH_ID, CertProfileID,
                                                                                                    pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, loginUID, pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                                            }
                                                                                            // set COMMIT TRUE File Attachment
                                                                                            String[] pRESPONSE_CODE_FILE = new String[1];
                                                                                            db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(pCERTIFICATE_ATTR_ID[0], loginUID, pRESPONSE_CODE_FILE);
                                                                                            if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                                                String isActiveSignServer = "0";
                                                                                                GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                                if (sessGeneralPolicy1[0].length > 0) {
                                                                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                                    {
                                                                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                                            isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                            break;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                if("1".equals(isActiveSignServer)) {
                                                                                                    CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT.trim(), sessLanguage);
                                                                                                } else {
                                                                                                    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                                    dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                                    int[] intWSRes = new int[1];
                                                                                                    String[] sWSRes = new String[1];
                                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                                    if (intWSRes[0] == 0) { }
                                                                                                    else {
                                                                                                        isProcess = false;
                                                                                                    }
                                                                                                }
                                                                                            } else {
                                                                                                if (sPUSH_NOTICE_ENABLED == true) {
                                                                                                    int[] intRes = new int[1];
                                                                                                    String[] sRes = new String[1];
                                                                                                    ConnectConnector.SendMailOTP(pCERTIFICATION_ID, intRes, sRes);
                                                                                                }
                                                                                                isProcess = true;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    strIsPushNotiApprove = "1";
                                                                                }
                                                                                //</editor-fold>
                                                                            } else {
                                                                                strIsPushNotiApprove = "1";
                                                                            }
                                                                            //</editor-fold>
                                                                        }
                                                                        if(isProcess == true) {
                                                                            sessionsa.setAttribute("RefreshRegisTokenSess", "1");
                                                                            if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                                                || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_SSL
                                                                                || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING
                                                                                || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                                                strView = "0#" + pCERTIFICATION_ID + "#0#" + strIsPushNotiApprove;
                                                                            } else {
                                                                                strView = "0#" + pCERTIFICATION_ID + "#1#" + strIsPushNotiApprove;
                                                                            }
                                                                        } else {
                                                                            String sCoreCAError = "";
                                                                            CERTIFICATION[][] rsCertAttr = new CERTIFICATION[1][];
                                                                            db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sessLanguage, rsCertAttr);
                                                                            if(rsCertAttr[0].length > 0) {
                                                                                sCoreCAError = rsCertAttr[0][0].CORE_ERROR_DESCRIPTION;
                                                                            }
                                                                            if(!"".equals(sCoreCAError)) {
                                                                                RESPONSE_CORECA valueLog = objectMapper.readValue(sCoreCAError, RESPONSE_CORECA.class);
                                                                                String sResMess = EscapeUtils.CheckTextNull(valueLog.responseMessage);
                                                                                if(!"".equals(sResMess)) {
                                                                                    strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#0";
                                                                                    CommonFunction.LogDebugString(log, "There was an error returned from CoreCA", valueLog.responseCode + " - " + sResMess);
                                                                                } else {
                                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                }
                                                                            } else {
                                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                            }
                                                                        }
                                                                    } else {
                                                                        strView = sParam + "#0";
                                                                    }
                                                                } else {
                                                                    RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                                                                    db.S_BO_API_RESPONSE_CODE_GET_INFO(sParam, Integer.parseInt(sessLanguage), rsResponseCode);
                                                                    if (rsResponseCode[0].length > 0) {
                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR+"#"+rsResponseCode[0][0].REMARK;
                                                                    } else {
                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                    }
                                                                }
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_CSR_EXISTS + "#0";
                                                            }
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_CSR_KEYSIZE + "#0";
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_TAXCODE_EXISTS_REGISTER + "#0";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_NOEXISTS_TOKEN + "#0";
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                            }
                                        } else {
                                            //<editor-fold defaultstate="collapsed" desc="### RSSP Process">
                                            String pOwnerUserRSSP = EscapeUtils.CheckTextNull(request.getParameter("pOWNER_USER_RSSP"));
                                            String rsspChoiseOwner = EscapeUtils.CheckTextNull(request.getParameter("rsspChoiseOwner"));
                                            PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                            db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                            String sFormFactorPro = "";
                                            if(rsFormfactorPro[0].length > 0) {
                                                sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                            }
                                            CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                                    
                                            if(credentialAuthen != null) {
                                                RSSPProcessCommon clsRSSP = new RSSPProcessCommon();
                                                boolean userRSSPExists = false;
                                                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_CMC) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_MID)) {
                                                    if("1".equals(rsspChoiseOwner)){
                                                        String[] sResult = new String[2];
                                                        List<OwnerInfo> listOwner = clsRSSP.getOwnerInfoForSignCloud(pOwnerUserRSSP, "", "",
                                                            "", "", "", "", sResult, "", credentialAuthen);
                                                        if("0".equals(sResult[0])) {
                                                            if(listOwner.size() > 0) {
                                                                userRSSPExists = true;
                                                            }
                                                        }
                                                    }
                                                }
                                                if(userRSSPExists == true) {
                                                    strView = Definitions.CONFIG_EXCEPTION_RSSP_USERNAME_EXISTS + "#0";
                                                } else {
                                                    String sAgreementUUID = CommonFunction.getUUIDV4();
                                                    AgreementDetails agreementDetails = new AgreementDetails();
                                                    String strEmailSan = "";
                                                    //<editor-fold defaultstate="collapsed" desc="### PROPERTIES SAN CERT">
                                                    if(!"".equals(pCOMPONENT_SAN)) {
                                                        CommonFunction.LogDebugString(log, "pCOMPONENT_SAN", pCOMPONENT_SAN);
                                                        String[] pCompSanSub = pCOMPONENT_SAN.split("@@@");
                                                        for (String sPlitValue : pCompSanSub) {
                                                            if(!"".equals(sPlitValue)) {
                                                                if(sPlitValue.split("###")[0].trim().equals(Definitions.CONFIG_COMPONENT_SAN_TAG_rfc822Name))
                                                                {
                                                                    strEmailSan = sPlitValue.split("###")[1];
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    //</editor-fold>

                                                    String pPROVINCE_CODE = "N/A";
                                                    //<editor-fold defaultstate="collapsed" desc="### GET CITY_PROVIN_CODE">
                                                    if(!"".equals(pPROVINCE_ID)) {
                                                        CITY_PROVINCE[][] rsProvince = new CITY_PROVINCE[1][];
                                                        db.S_BO_PROVINCE_DETAIL(pPROVINCE_ID, rsProvince);
                                                        if(rsProvince[0].length > 0) {
                                                            pPROVINCE_CODE = EscapeUtils.CheckTextNull(rsProvince[0][0].NAME);
                                                        }
                                                    }
                                                    //</editor-fold>

                                                    String sCertProfileCode = "";
                                                    String sCertProfileProperties = "";
                                                    CERTIFICATION_PROFILE[][] rsProfile;
                                                    rsProfile = new CERTIFICATION_PROFILE[1][];
                                                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                    if(rsProfile[0].length > 0)
                                                    {
                                                        sCertProfileCode = EscapeUtils.CheckTextNull(rsProfile[0][0].NAME);
                                                        sCertProfileProperties = EscapeUtils.CheckTextNull(rsProfile[0][0].PROPERTIES);
                                                    }
                                                    int serverOUCount = 0;
                                                    CERTIFICATION_TYPE_COMPONENT[][] resProfileData = new CERTIFICATION_TYPE_COMPONENT[1][];
                                                    CommonFunction.getJsonComponentForCert(sCertProfileProperties, resProfileData);
                                                    for(CERTIFICATION_TYPE_COMPONENT resProfileData1 : resProfileData[0])
                                                    {
                                                        if(EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID)) {
                                                            if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE+":")) {
                                                                if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_STAFF))
                                                                    || pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE))) {
                                                                        agreementDetails.setTaxID(pTAX_CODE);
                                                                } else {
                                                                    if(pPERSONAL_ID.contains(Definitions.CONFIG_EN_PREFIX_ENTERPRISE_TAX_CODE)){
                                                                        agreementDetails.setTaxID(pPERSONAL_ID.replace(Definitions.CONFIG_EN_PREFIX_ENTERPRISE_TAX_CODE+":", ""));
                                                                    }
                                                                }
                                                            }
                                                            if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE+":")) {
                                                                agreementDetails.setBudgetID(pBUDGET_CODE);
                                                            }
                                                            if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_PERSONAL_CODE+":")) {
                                                                agreementDetails.setPersonalID(pP_ID);
                                                            }
                                                            if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE+":")) {
                                                                agreementDetails.setPassportID(pPASSPORT);
                                                            }
                                                            if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_PERSONAL_CITIZEN_CODE+":")) {
                                                                agreementDetails.setCitizenID(pCCCD);
                                                            }
                                                            if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_SOCIAL_INSURANCE_CODE+":")) {
                                                                //pENTERPRISE_ID , pPERSONAL_ID
                                                                if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_STAFF))
                                                                    || pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE))) {
                                                                    if(pENTERPRISE_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE)){
                                                                        agreementDetails.setSocialInsuranceNumber(pENTERPRISE_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE, ""));
                                                                    }
                                                                } else {
                                                                    if(pPERSONAL_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE)){
                                                                        agreementDetails.setSocialInsuranceNumber(pPERSONAL_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE, ""));
                                                                    }
                                                                }
                                                            }
                                                            if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_DECISION+":")) {
                                                                if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_STAFF))
                                                                    || pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE))) {
                                                                    if(pENTERPRISE_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION)){
                                                                        agreementDetails.setDecisionNumber(pENTERPRISE_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION, ""));
                                                                    }
                                                                } else {
                                                                    if(pPERSONAL_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION)){
                                                                        agreementDetails.setDecisionNumber(pPERSONAL_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION, ""));
                                                                    }
                                                                }
                                                            }
                                                            if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_UNIT_CODE+":")) {
                                                                if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_STAFF))
                                                                    || pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE))) {
                                                                    if(pENTERPRISE_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE)){
                                                                        agreementDetails.setUnitCode(pENTERPRISE_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE, ""));
                                                                    }
                                                                } else {
                                                                    if(pPERSONAL_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE)){
                                                                        agreementDetails.setUnitCode(pPERSONAL_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE, ""));
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_T)) {
                                                                agreementDetails.setTitle(CommonFunction.getRoleInDN(DN).trim());
                                                            }
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_C)) {
                                                                agreementDetails.setCountry(CommonFunction.getCountryInDN(DN).trim());
                                                            }
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST)) {
                                                                agreementDetails.setStateOrProvince(pPROVINCE_CODE);
                                                            }
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_E)) {
                                                                agreementDetails.setEmail(CommonFunction.getEmailInDN(DN).trim());
                                                            }
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber)) {
                                                                agreementDetails.setTelephoneNumber(CommonFunction.getPhoneInDN(DN).trim());
                                                            }
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_L)) {
                                                                String sLocation = CommonFunction.getLocationInDN(DN).trim();
                                                                agreementDetails.setLocation(sLocation);
                                                            }
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_O)) {
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.commomNameType).equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_PERSON)) {
                                                                    agreementDetails.setOrganization(pCOMPANY_NAME);
                                                                } else {
                                                                    String sOrganzation = CommonFunction.getORGANIZATIONInDN(DN).trim();
                                                                    agreementDetails.setOrganization(sOrganzation);
                                                                }
                                                            }
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN)) {
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.commomNameType).equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_COMPANY))
                                                                {
                                                                    agreementDetails.setPersonalName(pCOMPANY_NAME);
                                                                } else if(EscapeUtils.CheckTextNull(resProfileData1.commomNameType).equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_PERSON))
                                                                {
                                                                    agreementDetails.setPersonalName(pPERSONAL_NAME);
                                                                }
                                                            }
                                                            if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU)) {
                                                                serverOUCount = serverOUCount + 1;
//                                                                agreementDetails.setOrganizationUnit(CommonFunction.getDepartmentInDN(DN).trim());
                                                            }
                                                        }
                                                    }
                                                    if(!"".equals(strEmailSan)){
                                                        agreementDetails.setRfc822Name(strEmailSan);
                                                    }
                                                    // add ou to rssp
                                                    String storeOU = EscapeUtils.CheckTextNull(request.getParameter("storeOU"));
                                                    CommonReferServlet.addComponentOURSSP(agreementDetails, serverOUCount, storeOU);
                                                    String[] sResultRSSP;
                                                    sResultRSSP = new String[3];
                                                    String pCREATE_USER = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                                    String pAgreeUUIDRSSP = EscapeUtils.CheckTextNull(request.getParameter("pAGREEMENT_UUID_RSSP"));
                                                    String pOwnerUUID = EscapeUtils.CheckTextNull(request.getParameter("hdfOwnerUUID"));
                                                    String pRelyingPartyRSSP = EscapeUtils.CheckTextNull(request.getParameter("RSSP_RELYING_PARTY"));
                                                    String pAuthenModesRSSP = EscapeUtils.CheckTextNull(request.getParameter("RSSP_AUTHENMODES"));
                                                    String pSignProfileRSSP = EscapeUtils.CheckTextNull(request.getParameter("SIGNING_PROFILES"));
                                                    String pOwnerEmailRSSP = EscapeUtils.CheckTextNull(request.getParameter("RSSP_OWNER_EMAIL"));
                                                    String pOwnerPhoneRSSP = EscapeUtils.CheckTextNull(request.getParameter("RSSP_OWNER_PHONE"));
                                                    if(!"".equals(pAgreeUUIDRSSP)) {
                                                        sAgreementUUID = pAgreeUUIDRSSP;
                                                    }
                                                    String pOwnerPassRSSP = "";
                                                    String beneficiaryBranch = "";
                                                    BRANCH[][] rsBranch = new BRANCH[1][];
                                                    db.S_BO_BRANCH_DETAIL(BRANCH_ID, rsBranch);
                                                    if(rsBranch[0].length > 0) {
                                                        beneficiaryBranch = EscapeUtils.CheckTextNull(rsBranch[0][0].NAME);
                                                    }
                                                    String beneficiaryUser = "";
                                                    BACKOFFICE_USER[][] rsUser = new BACKOFFICE_USER[1][];
                                                    db.S_BO_USER_DETAIL(pCREATE_USER, sessLanguage, rsUser);
                                                    if(rsUser[0].length > 0) {
                                                        beneficiaryUser = EscapeUtils.CheckTextNull(rsUser[0][0].USERNAME);
                                                    }
                                                    clsRSSP.prepareCertificateForSignCloud(sAgreementUUID, agreementDetails, sCertProfileCode, PHONE_CONTRACT,
                                                        EMAIL_CONTRACT, beneficiaryBranch, beneficiaryUser, pOwnerUUID, pOwnerUserRSSP, pOwnerPassRSSP,
                                                        pOwnerEmailRSSP, pOwnerPhoneRSSP, pAuthenModesRSSP, pRelyingPartyRSSP, pSignProfileRSSP, sResultRSSP, credentialAuthen);
                                                    if("0".equals(sResultRSSP[0]))
                                                    {
                                                        objectMapper = new ObjectMapper();
                                                        int pCERTIFICATION_PURPOSE_ID = 0;
                                                        String sCertificateID = sResultRSSP[2];
                                                        boolean isProcess = true;
                                                        String isProcessText = "";
                                                        String sVALUE_OLD = "";
                                                        if(!"".equals(sCertificateID) && !"0".equals(sCertificateID))
                                                        {
                                                            CERTIFICATION[][] rsCert;
                                                            rsCert = new CERTIFICATION[1][];
                                                            String hdfOwnerID = "";
                                                            db.S_BO_CERTIFICATION_DETAIL(sCertificateID, sessLanguage, rsCert);
                                                            if(rsCert[0].length > 0)
                                                            {
                                                                sVALUE_OLD = rsCert[0][0].VALUE;
                                                                hdfOwnerID = String.valueOf(rsCert[0][0].CERTIFICATION_OWNER_ID);
                                                                pCERTIFICATION_PURPOSE_ID = rsCert[0][0].CERTIFICATION_PURPOSE_ID;
                                                                pCERTIFICATE_ATTR_ID[0] = rsCert[0][0].CERTIFICATION_ATTR_ID;
                                                            }
                                                            //<editor-fold defaultstate="collapsed" desc="### REPRESENTIVE PROCESS">
                                                            String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
                                                            CommonFunction.LogDebugString(log, "Register Cert - outner profile contact", sRepresentEnabled);
                                                            if("1".equals(sRepresentEnabled)) {
                                                                CommonFunction.LogDebugString(log, "Register Cert - inner profile contact", sRepresentEnabled);
                                                                String registerAddressGPKD = EscapeUtils.CheckTextNull(request.getParameter("registerAddressGPKD"));
                                                                String registerFullname = EscapeUtils.CheckTextNull(request.getParameter("registerFullname"));
                                                                String registerRole = EscapeUtils.CheckTextNull(request.getParameter("registerRole"));
                                                                String registerCMND = EscapeUtils.CheckTextNull(request.getParameter("registerCMND"));
                                                                String registerIssuedDate = EscapeUtils.CheckTextNull(request.getParameter("registerIssuedDate"));
                                                                String registerIssuedPlace = EscapeUtils.CheckTextNull(request.getParameter("registerIssuedPlace"));
                                                                String registerIssuedAdress = EscapeUtils.CheckTextNull(request.getParameter("registerIssuedAdress"));
                                                                ProfileContactInfoJson profileContact = new ProfileContactInfoJson();
                                                                profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(registerFullname, true);
                                                                profileContact.RepresentativePhone = "";
                                                                profileContact.PIDIssuedBy = CommonFunction.replaceCharaterSpecialJson(registerIssuedPlace, true);
                                                                profileContact.PIDDate = registerIssuedDate;
                                                                profileContact.PID = registerCMND;
                                                                profileContact.AddressLicense = CommonFunction.replaceCharaterSpecialJson(registerAddressGPKD, true);
                                                                profileContact.RepresentativeEmail = "";
                                                                profileContact.ContactName = "";
                                                                profileContact.Position = CommonFunction.replaceCharaterSpecialJson(registerRole, true);
                                                                profileContact.Address = CommonFunction.replaceCharaterSpecialJson(registerIssuedAdress, true);
                                                                objectMapper = new ObjectMapper();
                                                                CommonFunction.LogDebugString(log, "Register Cert - json profileContact", objectMapper.writeValueAsString(profileContact));
                                                                db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(Integer.parseInt(sCertificateID), objectMapper.writeValueAsString(profileContact), userLoginID);
                                                            }
                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)){
                                                                String registerNCRepresentative = EscapeUtils.CheckTextNull(request.getParameter("registerNCRepresentative"));
                                                                String registerNCAddress = EscapeUtils.CheckTextNull(request.getParameter("registerNCAddress"));
                                                                ProfileContactInfoJson profileContact = new ProfileContactInfoJson();
                                                                profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(registerNCRepresentative, true);
                                                                profileContact.Address = CommonFunction.replaceCharaterSpecialJson(registerNCAddress, true);
                                                                objectMapper = new ObjectMapper();
                                                                db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(Integer.parseInt(sCertificateID), objectMapper.writeValueAsString(profileContact), userLoginID);
                                                            }
                                                            //</editor-fold>

                                                            String sJRBConfig = "";
                                                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                            if (sessGeneralPolicy[0].length > 0) {
                                                                for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT)) {
                                                                        sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            //<editor-fold defaultstate="collapsed" desc="### FILE MANAGER INSERT">
                                                            String sJRB_Source =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
                                                            if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY))
                                                            {
                                                                SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                                if (cartToken != null) {
                                                                    String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
                                                                    String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
                                                                    String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
                                                                    String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
                                                                    String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
                                                                    String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
                                                                    String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
                                                                    String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
                                                                    String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_UP);
                                                                    String idUUID_Temp = Definitions.CONFIG_JACK_RABBIT_UUID_SAMPLE;// "4D4B4901-BF4C-4236-A9BA-AF2E0D4F5F33";
                                                                    ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                                                    for (FILE_PROFILE_DATA mhIP : ds) {
            //                                                            File fileUp = new File(mhIP.FILE_URL);
                                                                        String sFileData = EscapeUtils.CheckTextNull(mhIP.FILE_URL);//CommonFunction.encodeFileToBase64Binary(fileUp);
                                                                        CloseableHttpResponse pHttpRes = ConnectFileToPartner.upFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                                                            sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                                                            sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, idUUID_Temp,
                                                                            mhIP.FILE_NAME, sFileData);
                                                                        InputStream isStr = pHttpRes.getEntity().getContent();
                                                                        String resultUUID = IOUtils.toString(isStr);
                                                                        int[] pFILE_MANAGER_ID = new int[1];
                                                                        db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, resultUUID, sJRBConfig,
                                                                            EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE), mhIP.FILE_NAME,
                                                                            (int) mhIP.FILE_SIZE, sCertificateID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
                                                                    }
                                                                    request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                                }
                                                            } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
                                                                String sJRB_Host =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                                String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                                String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                                String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                                String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                                String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                                String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                                SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                                if (cartToken != null) {
                                                                    ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                                                    for (FILE_PROFILE_DATA mhIP : ds) {
                                                                        JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                                                            Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                        InputStream isFILE_STREAM = new ByteArrayInputStream(mhIP.FILE_STREAM);
                                                                        int[] pFILE_MANAGER_ID = new int[1];
                                                                        JackRabbitCommon.getInstance(jcrConfig).uploadFileThread(mhIP.FILE_NAME, mhIP.FILE_MIMETYPE, isFILE_STREAM, mhIP, sJRBConfig,
                                                                            sCertificateID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
    //                                                                                    if(jrbFile != null) {
    //                                                                                        int[] pFILE_MANAGER_ID = new int[1];
    //                                                                                        db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, jrbFile.getUuid(), sJRBConfig, EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE),
    //                                                                                            jrbFile.getFileName(), (int) mhIP.FILE_SIZE, pCERTIFICATION_ID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
    //                                                                                    }
                    //                                                    JCRManager. .destroyAllJCR();
                                                                    }
                                                                    request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                                }
                                                            } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
                                                                String sJRB_Host =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                                String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                                String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                                String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                                String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                                String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                                String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                                SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                                if (cartToken != null) {
                                                                    ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                                                    for (FILE_PROFILE_DATA mhIP : ds) {
                                                                        ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                                                                            Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                        InputStream isFILE_STREAM = new ByteArrayInputStream(mhIP.FILE_STREAM);
                                                                        String[] sReturnJRB = new String[2];
                                                                        vn.mobileid.fms.client.JCRFile jrbFile = openJRB.uploadFile(EscapeUtils.CheckTextNull(mhIP.FILE_NAME),
                                                                            EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE), isFILE_STREAM, sReturnJRB);
                                                                        int[] pFILE_MANAGER_ID = new int[1];
                                                                        db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, sReturnJRB[0].trim(), sJRBConfig, EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE),
                                                                            sReturnJRB[1].trim(), (int) mhIP.FILE_SIZE, sCertificateID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
                    //                                                    JCRManager. .destroyAllJCR();
                                                                    }
                                                                    request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                                }
                                                            } else {
                                                            }
                                                            //</editor-fold>

                                                            ATTRIBUTE_VALUES valueATTR = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                            String strIsPushNotiApprove = "0";
                                                            if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                //<editor-fold defaultstate="collapsed" desc="### CA LOGIN APPROVE">
                                                                boolean isCAApprove = false;
                                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                                {
                                                                    isCAApprove = true;
                                                                } else {
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                    {
                                                                        isCAApprove = true;
                                                                    }
                                                                }
                                                                if(isCAApprove == true)
                                                                {
                                                                    sResultRSSP = new String[2];
                                                                    clsRSSP.approveCertificateForSignCloud(sAgreementUUID, sCertificateID, "", pRelyingPartyRSSP, sResultRSSP,
                                                                        "","", Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_PREPARE_CERT, "", credentialAuthen, false);
                                                                    if(!"0".equals(sResultRSSP[0]))
                                                                    {
                                                                        isProcess = false;
                                                                        isProcessText = sResultRSSP[1];
                                                                    }
                                                                } else {
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                }
                                                                //</editor-fold>
                                                            } else {
                                                                //<editor-fold defaultstate="collapsed" desc="### AGENCY LOGIN APPROVE">
                                                                boolean checkCallApproveCA = false;
                                                                String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                   || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                                {
                                                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                        if("0".equals(paramPre)) {
                                                                            checkCallApproveCA = true;
                                                                        }
                                                                    } else {
                                                                       int approveChilrenID = Integer.parseInt(userLoginID);
                                                                       BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                        db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                        if(rsUserApprve[0].length > 0) {
                                                                            for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                    approveChilrenID = item.ID;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                       valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_IN_AGENCY_APPROVED);
                                                                       valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                       valueATTR.setApproveDt(new Date());
                                                                       String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                                       if("0".equals(paramAgency)) {
                                                                           if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                           {
                                                                               valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                if("0".equals(paramPre)) {
                                                                                    checkCallApproveCA = true;
                                                                                }
                                                                           }
                                                                       }
                                                                    }
                                                                } else {
                                                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                            if("0".equals(paramPre)) {
                                                                                checkCallApproveCA = true;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                        if(intApprove == 1) {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                                int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                if(rsUserApprve[0].length > 0) {
                                                                                    for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                        if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                            approveChilrenID = item.ID;
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_IN_AGENCY_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                                                if("0".equals(paramAgency)) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                    {
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                        if("0".equals(paramPre)) {
                                                                                            checkCallApproveCA = true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                if(checkCallApproveCA == true) {
                                                                    //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                    boolean autoApproveCA = false;
                                                                    rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                    CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                    if(rsProfile[0].length > 0) {
                                                                        autoApproveCA = CommonFunction.getApproveCAOfProfile(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME), sessPolicyCert_Data);
                                                                    }
                                                                    if(autoApproveCA == true) {
                                                                        if(CommonFunction.checkApproveCAReqType(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION, sessPolicyCert_Data) == true)
                                                                        {
                                                                            sResultRSSP = new String[2];
                                                                            clsRSSP.approveCertificateForSignCloud(sAgreementUUID, sCertificateID, "", pRelyingPartyRSSP, sResultRSSP,
                                                                                "","",Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_PREPARE_CERT, "", credentialAuthen, false);
                                                                            if(!"0".equals(sResultRSSP[0]))
                                                                            {
                                                                                isProcess = false;
                                                                                isProcessText = sResultRSSP[1];
                                                                            }
                                                                        }
                                                                    } else {
                                                                        strIsPushNotiApprove = "1";
                                                                    }
                                                                    //</editor-fold>
                                                                } else {
                                                                    strIsPushNotiApprove = "1";
                                                                }
                                                                //</editor-fold>
                                                            }
                                                            if(isProcess == true)
                                                            {
                                                                sessionsa.setAttribute("RefreshRegisTokenSess", "1");
                                                                if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_SSL
                                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING
                                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                                    strView = "0#" + sCertificateID + "#0#" + strIsPushNotiApprove;
                                                                } else {
                                                                    strView = "0#" + sCertificateID + "#1#" + strIsPushNotiApprove;
                                                                }
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#" + isProcessText;
                                                            }
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#Error: The certificate information returned from eSignCloud is invalid";
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#Error: " + sResultRSSP[0] + " - " + sResultRSSP[1];
                                                    }
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM + "#0";
                                            }
                                            //</editor-fold>
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "buycertificatemore": {
                                //<editor-fold defaultstate="collapsed" desc="buycertificatemore">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = "";
                                    String sTOKEN_SN = "";
                                    String CheckCHANGE_KEY = "1";
                                    String sPRIVATE_KEY = "";
                                    String CheckPRIVATE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckPRIVATE_KEY"));
                                    String BRANCH_ID = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ID"));
                                    String pCSR = EscapeUtils.CheckTextNull(request.getParameter("pCSR"));
                                    String pCERTIFICATION_PURPOSE = EscapeUtils.CheckTextNull(request.getParameter("pCERTIFICATION_PURPOSE"));
                                    String pPKI_FORMFACTOR = EscapeUtils.CheckTextNull(request.getParameter("pPKI_FORMFACTOR"));
                                    String CertProfileID = EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                    String pPERSONAL_NAME = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_NAME"));
                                    String pCOMPANY_NAME = EscapeUtils.CheckTextNull(request.getParameter("pCOMPANY_NAME"));
                                    String pDOMAIN_NAME = EscapeUtils.CheckTextNull(request.getParameter("pDOMAIN_NAME"));
                                    String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
                                    String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                    boolean isProcess = true;
                                    boolean isValidCSR = true;
                                    boolean checkCSRNotExists = true;
                                    String strDNSName = "";
                                    String strPasswordP12 = "";
                                    if(!"".equals(pCERTIFICATION_PURPOSE))
                                    {
                                        if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                        {
                                            CheckPRIVATE_KEY = "1";
                                            if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_SSL)))
                                            {
                                                ID = String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID);
                                                sTOKEN_SN = Definitions.CONFIG_TOKEN_SSL_SN;
                                            } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING)))
                                            {
                                                ID = String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID);
                                                sTOKEN_SN = Definitions.CONFIG_TOKEN_CODESIGNNING_SN;
                                            } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_DEVICE)))
                                            {
                                                ID = String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID);
                                                sTOKEN_SN = Definitions.CONFIG_TOKEN_DEVICE_SN;
                                            } else {
                                                ID = String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID);
                                                sTOKEN_SN = Definitions.CONFIG_TOKEN_SIGNSERVER_SN;
                                            }
                                            if(!"".equals(pCSR))
                                            {
                                                CheckPRIVATE_KEY = "0";
                                                String sKeySizeDB;
                                                isValidCSR = false;
                                                CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                                db.S_BO_GET_ALGORITHM_KEY_SIZE(CertProfileID, rsCert);
                                                if(rsCert[0].length > 0)
                                                {
                                                    sKeySizeDB = EscapeUtils.CheckTextNull(rsCert[0][0].KEY_SIZE);
                                                    String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(pCSR);
                                                    isValidCSR = sKeySizeDB.equals(sKeySizeCSR);
                                                }
                                            } else {
                                                strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                            }
                                        }
                                        else if(CommonFunction.checkHardTokenIDEnabled(Integer.parseInt(pPKI_FORMFACTOR)) == true)
                                        {
                                            ID = String.valueOf(Definitions.CONFIG_TOKEN_UNASSIGN_ID);
                                            sTOKEN_SN = Definitions.CONFIG_TOKEN_UNASSIGN_SN;
                                        } else {}
                                        
                                        String sTempDevice = pPERSONAL_NAME;
                                        if("".equals(sTempDevice)) {
                                            sTempDevice = pCOMPANY_NAME;
                                        }
                                        if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_WEB_SERVER))) {
                                            pDEVICE = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_WEBSERVER + sTempDevice;
                                        } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_X_ROAD_AUTH))) {
                                            pDEVICE = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_X_ROAD_AUTH + sTempDevice;
                                        } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_WEB_CLIENT))) {
                                            pDEVICE = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_WEBCLIENT + sTempDevice;
                                        } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_X_ROAD_SIGN))) {
                                            pDEVICE = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_X_ROAD_SIGN + sTempDevice;
                                        } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING_GOV))) {
                                            pDEVICE = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CODE_SIGNING + sTempDevice;
                                        } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_STAFF))
                                            || pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE))
                                            || pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_PERSONAL))) {
                                            pDEVICE = "";
                                        }
                                    }
                                    String pCREATE_USER = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                    String strIsPushNotiApprove = "0";
                                    String hdfOwnerID = "";
                                    String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    boolean isAccessAgency = true;
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(ids, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        hdfOwnerID = String.valueOf(rsReq[0][0].CERTIFICATION_OWNER_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    //</editor-fold>
                                    
                                    if (isAccessAgency == true) {
                                        if(!"".equals(ID) || !"".equals(sTOKEN_SN)) {
                                            ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                            String PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                            String EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
                                            String CACoreSubject = EscapeUtils.CheckTextNull(request.getParameter("CACoreSubject"));
                                            String DN = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                            String pTAX_CODE = EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                            String pDECISION = EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                            String pBUDGET_CODE = EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                            String pP_ID = EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                            String pCCCD = EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                            String pPASSPORT = EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                            String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                            String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                            String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                            String pPROVINCE_DESC = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_DESC"));
                                            String pPAST_CERTIFICATE_ID = Definitions.CONFIG_CERTIFICATE_PAST_CERTIFICATE_ID;
                                            String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE);
                                            String pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_BUY_MORE;
                                            String pCERTIFICATION_SN = "";
                                            if(isValidCSR == true) {
                                                if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                    if(!"".equals(pCSR)) {
                                                        String sPublicKeyHard = CommonFunction.getPublicKeyHasrCSR(pCSR);
                                                        int checkPublicKey = db.S_BO_CHECK_OWNER_HAVE_EXISTS_PUBLIC_KEY_HASH(0, null, null, null, sPublicKeyHard, Integer.parseInt(hdfOwnerID));
                                                        if(checkPublicKey != 0) {
                                                            checkCSRNotExists = false;
                                                        }
                                                    }
                                                }
                                                if(checkCSRNotExists == true) {
                                                    //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                                    CommonFunction.LogDebugString(log, "REGISTRATION-CERTIFICATION", "REQUEST: " + "SUBJECT: " + DN
                                                        + "; PERSONAL_NAME: " + pPERSONAL_NAME + "; COMPANY_NAME: " + pCOMPANY_NAME + "; pENTERPRISE_ID: " + pENTERPRISE_ID
                                                        + "; pPERSONAL_ID: " + pPERSONAL_ID + "; DOMAIN_NAME: " + pDOMAIN_NAME + "; PAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                        + "; CERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID
                                                        + "; PKI_FORMFACTOR: " + pPKI_FORMFACTOR + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                        + "; CACoreSubject: " + CACoreSubject + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                        + "; PROVINCE_ID: " + pPROVINCE_ID + "; TOKEN_SN: " + sTOKEN_SN);
                                                    tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                    objectMapper = new ObjectMapper();
                                                    tempLogReq.personalName = pPERSONAL_NAME;
                                                    tempLogReq.companyName = pCOMPANY_NAME;
                                                    tempLogReq.enterpriseID = pENTERPRISE_ID;
                                                    tempLogReq.personalID = pPERSONAL_ID;
                                                    tempLogReq.emailContract = EMAIL_CONTRACT;
                                                    tempLogReq.phoneContract = PHONE_CONTRACT;
                                                    tempLogReq.issuerSubject = CACoreSubject;
                                                    tempLogReq.subjectDn = DN;
                                                    tempLogReq.tokenSn = sTOKEN_SN;
                                                    tempLogReq.provinceName = pPROVINCE_DESC;
                                                    tempLogReq.pkiFromFactorId = Integer.parseInt(pPKI_FORMFACTOR);
                                                    tempLogReq.typeName = pCERT_ATTR_TYPE_CODE;
                                                    String strReq = objectMapper.writeValueAsString(tempLogReq);
                                                    db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                            Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                            Definitions.CONFIG_LOG_FUNCTIONALITY_REQUEST_BUY_MORE, strReq,
                                                            loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                    ATTRIBUTE_VALUES valueATTR;
                                                    ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                    dataATTR.setCertificationData(tempLogReq);
                                                    valueATTR = new ATTRIBUTE_VALUES();
                                                    valueATTR.setTokenSn(sTOKEN_SN);
                                                    valueATTR.setChangeKeyEnabled(true);
                                                    valueATTR.setKeepCertificateSNEnabled(false);
                                                    valueATTR.setTypeName(pCERT_ATTR_TYPE_CODE);
                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                    valueATTR.setCreateUser(loginFullname + " (" + loginUID + ")");
                                                    valueATTR.setCreateDt(new Date());
                                                    valueATTR.setAttributeData(dataATTR);
                                                    //</editor-fold>

                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    String sParam = "";
                                                    if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                    {
                                                        sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(ID), CertProfileID, sTOKEN_SN,
                                                            pCERTIFICATION_SN, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME, pTAX_CODE, pBUDGET_CODE, pP_ID,
                                                            pPASSPORT, DN, CACoreSubject, PHONE_CONTRACT, EMAIL_CONTRACT, BRANCH_ID,
                                                            pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, "",
                                                            strReqValueATTR, pCREATE_USER, loginUID, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, pCSR,
                                                            CheckPRIVATE_KEY, CheckCHANGE_KEY, sPRIVATE_KEY, Integer.parseInt(pPKI_FORMFACTOR),
                                                            pDEVICE, hdfOwnerID, pCCCD, null, pDECISION, pPERSONAL_ID, pENTERPRISE_ID);
                                                    }
                                                    else if(CommonFunction.checkHardTokenIDEnabled(Integer.parseInt(pPKI_FORMFACTOR)) == true)
                                                    {
                                                        int intOTPNumn = 8;
                                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                                        if (sessGeneralPolicy[0].length > 0) {
                                                            for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_ACTIVATION_CODE_LENGTH))
                                                                {
                                                                    intOTPNumn = Integer.parseInt(rsPolicy1.VALUE);
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        sParam = "1000";
                                                        while ("1000".equals(sParam)) {
                                                            try {
                                                                String sOTP = CommonFunction.getRandomOTP(intOTPNumn);
                                                                sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(ID), CertProfileID, sTOKEN_SN.trim(),
                                                                    pCERTIFICATION_SN.trim(), pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME,
                                                                    pTAX_CODE, pBUDGET_CODE, pP_ID,
                                                                    pPASSPORT.trim(), DN, CACoreSubject.trim(), PHONE_CONTRACT, EMAIL_CONTRACT, BRANCH_ID,
                                                                    pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, sOTP,
                                                                    strReqValueATTR, pCREATE_USER.trim(), loginUID, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, "",
                                                                    CheckPRIVATE_KEY, CheckCHANGE_KEY, sPRIVATE_KEY, Integer.parseInt(pPKI_FORMFACTOR),
                                                                    pDEVICE, hdfOwnerID, pCCCD, null, pDECISION, pPERSONAL_ID, pENTERPRISE_ID);

                                                            } catch (Exception e) {
                                                                if (e.getMessage().contains(Definitions.CONFIG_MYSQL_UNIQUE_ACTIVATION_CODE)) {
                                                                    sParam = "1000";
                                                                } else {
                                                                    sParam = Definitions.CONFIG_EXCEPTION_STRING_ERROR;
                                                                    CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if ("0".equals(sParam)) {
                                                        //<editor-fold defaultstate="collapsed" desc="### REPRESENTIVE PROCESS">
                                                        Config conf = new Config();
                                                        String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
                                                        System.out.println("sRepresentEnabled-BuyMore: " + sRepresentEnabled);
                                                        ProfileContactInfoJson profileContact;
                                                        if("1".equals(sRepresentEnabled)) {
                                                            String registerAddressGPKD = EscapeUtils.CheckTextNull(request.getParameter("registerAddressGPKD"));
                                                            String registerFullname = EscapeUtils.CheckTextNull(request.getParameter("registerFullname"));
                                                            String registerRole = EscapeUtils.CheckTextNull(request.getParameter("registerRole"));
                                                            String registerCMND = EscapeUtils.CheckTextNull(request.getParameter("registerCMND"));
                                                            String registerIssuedDate = EscapeUtils.CheckTextNull(request.getParameter("registerIssuedDate"));
                                                            String registerIssuedPlace = EscapeUtils.CheckTextNull(request.getParameter("registerIssuedPlace"));
                                                            String registerIssuedAdress = EscapeUtils.CheckTextNull(request.getParameter("registerIssuedAdress"));
                                                            profileContact = new ProfileContactInfoJson();
                                                            profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(registerFullname, true);
                                                            profileContact.RepresentativePhone = "";
                                                            profileContact.PIDIssuedBy = CommonFunction.replaceCharaterSpecialJson(registerIssuedPlace, true);
                                                            profileContact.PIDDate = registerIssuedDate;
                                                            profileContact.PID = registerCMND;
                                                            profileContact.AddressLicense = CommonFunction.replaceCharaterSpecialJson(registerAddressGPKD, true);
                                                            profileContact.RepresentativeEmail = "";
                                                            profileContact.ContactName = "";
                                                            profileContact.Position = CommonFunction.replaceCharaterSpecialJson(registerRole, true);
                                                            profileContact.Address = CommonFunction.replaceCharaterSpecialJson(registerIssuedAdress, true);
                                                            objectMapper = new ObjectMapper();
                                                            db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(pCERTIFICATE_ID[0], objectMapper.writeValueAsString(profileContact), userLoginID);
                                                        } else {
                                                            CERTIFICATION[][] rsBrief = new CERTIFICATION[1][];
                                                            db.S_BO_CERTIFICATION_BRIEF_DETAIL(ids, rsBrief);
                                                            String sPrfileContact = EscapeUtils.CheckTextNull(rsBrief[0][0].PROFILE_CONTACT_INFO);
                                                            if(!"".equals(sPrfileContact)) {
                                                                objectMapper = new ObjectMapper();
                                                                ProfileContactInfoJson profileContactOld = objectMapper.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                                                if(profileContactOld != null) {
                                                                    db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(pCERTIFICATE_ID[0], sPrfileContact, userLoginID);
                                                                }
                                                            }
                                                        }
                                                        //</editor-fold>

                                                        //<editor-fold defaultstate="collapsed" desc="### PROPERTIES SAN CERT">
                                                        if(!"".equals(pCOMPONENT_SAN)) {
                                                            String[] pCompSanSub = pCOMPONENT_SAN.split("@@@");
                                                            List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes = new ArrayList<>();
                                                            for (String sPlitValue : pCompSanSub) {
                                                                if(!"".equals(sPlitValue)) {
                                                                    String sKey = "";
                                                                    if(!"".equals(sPlitValue.split("###")[0].trim())) {
                                                                        sKey = sPlitValue.split("###")[0].trim();
                                                                    }
                                                                    objectMapper = new ObjectMapper();
                                                                    CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                                    attribute.setKey(sKey);
                                                                    attribute.setValue(sPlitValue.split("###")[1]);
                                                                    attributes.add(attribute);
                                                                }
                                                            }
                                                            if(attributes.size() > 0) {
                                                                strDNSName = "{\"attributes\":" + objectMapper.writeValueAsString(attributes) + "}";
                                                                db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(pCERTIFICATE_ID[0]), strDNSName, loginUID);
                                                            }
                                                        }
                                                        //</editor-fold>

                                                        //<editor-fold defaultstate="collapsed" desc="### SHARED_MODE CERT">
                                                        boolean pSHARED_MODE_ENABLED = false;
                                                        BRANCH[][] rsBranch;
                                                        rsBranch = new BRANCH[1][];
                                                        db.S_BO_BRANCH_DETAIL(BRANCH_ID, rsBranch);
                                                        if(rsBranch[0].length > 0) {
                                                            String sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                                                            if(!"".equals(sCERT_POLICY_PROPERTIES)) {
                                                                pSHARED_MODE_ENABLED = CommonFunction.getShareModeEnabledCert(sCERT_POLICY_PROPERTIES);
                                                            }
                                                        }
                                                        String pSHARED_MODE = pSHARED_MODE_ENABLED ? "1" : "0";
                                                        db.S_BO_CERTIFICATION_UPDATE(pCERTIFICATE_ID[0], CertProfileID, "", "", "", pTAX_CODE, pBUDGET_CODE,
                                                            pP_ID, pPASSPORT, "", "", "", "", "", "", loginUID, "", "", "", "", pSHARED_MODE, pCCCD,
                                                            pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                        //</editor-fold>
                                                        
                                                        //<editor-fold defaultstate="collapsed" desc="### PUBLIC KEY HASH">
                                                        if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                            if(!"".equals(pCSR)) {
                                                                String sPublicKeyCSR = CommonFunction.getPublicKeyHasrCSR(pCSR);
                                                                db.S_BO_CERTIFICATION_UPDATE_CSR_INFO(pCERTIFICATE_ID[0], sPublicKeyCSR, loginUID);
                                                            }
                                                        }
                                                        //</editor-fold>

                                                        int pCERTIFICATION_PURPOSE_ID = 0;
                                                        String pStringCERTIFICATION_ID = "";
                                                        boolean sPUSH_NOTICE_ENABLED = false;
                                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
                                                        db.S_BO_CERTIFICATION_APPROVED_DETAIL(EscapeUtils.escapeHtml(String.valueOf(pCERTIFICATE_ATTR_ID[0])), sessLanguage, rs);
                                                        if (rs[0].length > 0) {
                                                            sPUSH_NOTICE_ENABLED = rs[0][0].PUSH_NOTICE_ENABLED;
                                                            pCERTIFICATION_PURPOSE_ID = rs[0][0].CERTIFICATION_PURPOSE_ID;
                                                            pStringCERTIFICATION_ID = String.valueOf(rs[0][0].ID);
                                                        }
                                                        String sJRBConfig = "";
                                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                        String sDiscountRateOption = "0";
                                                        if (sessGeneralPolicy[0].length > 0) {
                                                            for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT))
                                                                {
                                                                    sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                }
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                                                                {
                                                                    sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                }
                                                            }
                                                        }
                                                        //<editor-fold defaultstate="collapsed" desc="### FILE MANAGER INSERT">
                                                        SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                        int[] intResult = new int[1];
                                                        boolean isAutoSendFileToNew = false;
                                                        if("1".equals(LoadParamSystem.getParamStart(Definitions.CONFIG_FILE_FILE_UP_OLD_TO_NEW_AUTO_ENABLED))) {
                                                            isAutoSendFileToNew = true;
                                                        }
                                                        ServletUptoFunction.fileManagerInsert(sJRBConfig, cartToken, pStringCERTIFICATION_ID,
                                                            hdfOwnerID, loginUID, log, isAutoSendFileToNew, true, intResult);
                                                        if(intResult[0] == 0){
                                                            request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                        }
                                                            
                                                        /*String sJRB_Source =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
                                                        if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY))
                                                        {
                                                            SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                            if (cartToken != null) {
                                                                String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
                                                                String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
                                                                String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
                                                                String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
                                                                String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
                                                                String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
                                                                String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
                                                                String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
                                                                String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_UP);
                                                                String idUUID_Temp = Definitions.CONFIG_JACK_RABBIT_UUID_SAMPLE;// "4D4B4901-BF4C-4236-A9BA-AF2E0D4F5F33";
                                                                ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                                                for (FILE_PROFILE_DATA mhIP : ds) {
                                                                    String sFileData = EscapeUtils.CheckTextNull(mhIP.FILE_URL);//CommonFunction.encodeFileToBase64Binary(fileUp);
                                                                    CloseableHttpResponse pHttpRes = ConnectFileToPartner.upFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                                                        sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                                                        sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, idUUID_Temp,
                                                                        mhIP.FILE_NAME, sFileData);
                                                                    InputStream isStr = pHttpRes.getEntity().getContent();
                                                                    String resultUUID = IOUtils.toString(isStr);
                                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                                    db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, resultUUID, sJRBConfig,
                                                                        EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE), mhIP.FILE_NAME,
                                                                        (int) mhIP.FILE_SIZE, pStringCERTIFICATION_ID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
                                                                }
                                                                request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                            }
                                                        } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
                                                            String sJRB_Host =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                            String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                            String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                            String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                            String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                            String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                            String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                            SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                            if (cartToken != null) {
                                                                ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                                                for (FILE_PROFILE_DATA mhIP : ds) {
                                                                    JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                                                        Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                    InputStream isFILE_STREAM = new ByteArrayInputStream(mhIP.FILE_STREAM);
                                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                                    JackRabbitCommon.getInstance(jcrConfig).uploadFileThread(mhIP.FILE_NAME, mhIP.FILE_MIMETYPE, isFILE_STREAM, mhIP, sJRBConfig,
                                                                        pStringCERTIFICATION_ID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
                                                                }
                                                                request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                            }
                                                        } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
                                                            String sJRB_Host =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                            String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                            String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                            String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                            String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                            String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                            String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                            SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                            if (cartToken != null) {
                                                                ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                                                for (FILE_PROFILE_DATA mhIP : ds) {
                                                                    ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                                                                        Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                                    InputStream isFILE_STREAM = new ByteArrayInputStream(mhIP.FILE_STREAM);
                                                                    String[] sReturnJRB = new String[2];
                                                                    vn.mobileid.fms.client.JCRFile jrbFile = openJRB.uploadFile(EscapeUtils.CheckTextNull(mhIP.FILE_NAME),
                                                                        EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE), isFILE_STREAM, sReturnJRB);
                                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                                    db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, sReturnJRB[0].trim(), sJRBConfig, EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE),
                                                                        sReturnJRB[1].trim(), (int) mhIP.FILE_SIZE, pStringCERTIFICATION_ID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
                                                                }
                                                                request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                            }
                                                        } else {
                                                        }*/
                                                        //</editor-fold>

                                                        if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                            boolean isCAApprove = false;
                                                            if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                            {
                                                                isCAApprove = true;
                                                            } else {
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                {
                                                                    isCAApprove = true;
                                                                }
                                                            }
                                                            if(isCAApprove == true)
                                                            {
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveCADt(new Date());
                                                                if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                                {
                                                                    String CheckPUSH_NOTICE = EscapeUtils.CheckTextNull(request.getParameter("CheckPUSH_NOTICE"));
                                                                    if("".equals(CheckPUSH_NOTICE)) {
                                                                        CheckPUSH_NOTICE = "1";
                                                                    }
                                                                    db.S_BO_CERTIFICATION_UPDATE_AMOUNT(Integer.parseInt(pStringCERTIFICATION_ID), "", CheckPUSH_NOTICE, loginUID);
                                                                }
                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                if ("0".equals(sApprove)) {
                                                                    if ("1".equals(sDiscountRateOption)) {
                                                                        CommonReferServlet.updateDiscountRate(pStringCERTIFICATION_ID, BRANCH_ID, CertProfileID,
                                                                            pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, loginUID, pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                    }
                                                                    // set COMMIT TRUE File Attachment
                                                                    String[] pRESPONSE_CODE_FILE = new String[1];
                                                                    db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(pCERTIFICATE_ATTR_ID[0], loginUID, pRESPONSE_CODE_FILE);
                                                                    if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                        String isActiveSignServer = "0";
                                                                        GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                        if (sessGeneralPolicy1[0].length > 0) {
                                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                            {
                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                    isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        if("1".equals(isActiveSignServer)) {
                                                                            CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT.trim(), sessLanguage);
                                                                        } else {
                                                                            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                            dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                            int[] intWSRes = new int[1];
                                                                            String[] sWSRes = new String[1];
                                                                            ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                            if (intWSRes[0] == 0) { }
                                                                            else {
                                                                                isProcess = false;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        if (sPUSH_NOTICE_ENABLED == true) {
                                                                            int[] intRes = new int[1];
                                                                            String[] sRes = new String[1];
                                                                            ConnectConnector.SendMailOTP(pStringCERTIFICATION_ID, intRes, sRes);
                                                                        }
                                                                        isProcess = true;
                                                                    }
                                                                } else {
                                                                    isProcess = false;
                                                                    CommonFunction.LogErrorServlet(log, "Error: CERTIFICATION APPROVED, ATTR ID: " + String.valueOf(pCERTIFICATE_ATTR_ID[0]));
                                                                }
                                                            } else {
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                            }
                                                        } else {
                                                            boolean checkCallApproveCA = false;
                                                            String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                            objectMapper = new ObjectMapper();
                                                            if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                            {
                                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                    if("0".equals(paramPre)) {
                                                                        checkCallApproveCA = true;
                                                                    }
                                                                } else {
                                                                    int approveChilrenID = Integer.parseInt(userLoginID);
                                                                    BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                    db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                    if(rsUserApprve[0].length > 0) {
                                                                        for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                            if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                approveChilrenID = item.ID;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID, approveChilrenID);
                                                                    if("0".equals(paramAgency)) {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                            if("0".equals(paramPre)) {
                                                                                checkCallApproveCA = true;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                    {
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                        if("0".equals(paramPre)) {
                                                                            checkCallApproveCA = true;
                                                                        }
                                                                    }
                                                                } else {
                                                                    int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                    if(intApprove == 1) {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            int approveChilrenID = Integer.parseInt(userLoginID);
                                                                            BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                            db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                            if(rsUserApprve[0].length > 0) {
                                                                                for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                    if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                        approveChilrenID = item.ID;
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID, approveChilrenID);
                                                                            if("0".equals(paramAgency)) {
                                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                {
                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                    valueATTR.setApproveDt(new Date());
                                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                    if("0".equals(paramPre)) {
                                                                                        checkCallApproveCA = true;
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            if(checkCallApproveCA == true) {
                                                                //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                boolean autoApproveCA = false;
                                                                CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                objectMapper = new ObjectMapper();
                                                                if(rsProfile[0].length > 0) {
                                                                    autoApproveCA = CommonFunction.getApproveCAOfProfile(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME), sessPolicyCert_Data);
                                                                }
                                                                if(autoApproveCA == true) {
                                                                    if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                                    {
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveCADt(new Date());
                                                                        if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                                        {
                                                                            String CheckPUSH_NOTICE = EscapeUtils.CheckTextNull(request.getParameter("CheckPUSH_NOTICE"));
                                                                            if("".equals(CheckPUSH_NOTICE)) {
                                                                                CheckPUSH_NOTICE = "1";
                                                                            }
                                                                            db.S_BO_CERTIFICATION_UPDATE_AMOUNT(Integer.parseInt(pStringCERTIFICATION_ID), "", CheckPUSH_NOTICE, loginUID);
                                                                        }
                                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                        if ("0".equals(sApprove)) {
                                                                            if ("1".equals(sDiscountRateOption)) {
                                                                                CommonReferServlet.updateDiscountRate(pStringCERTIFICATION_ID, BRANCH_ID, CertProfileID,
                                                                                    pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, loginUID, pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                            }
                                                                            String[] pRESPONSE_CODE_FILE = new String[1];
                                                                            db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(pCERTIFICATE_ATTR_ID[0], loginUID, pRESPONSE_CODE_FILE);
                                                                            if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                                String isActiveSignServer = "0";
                                                                                GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                if (sessGeneralPolicy1[0].length > 0) {
                                                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                    {
                                                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                            isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if("1".equals(isActiveSignServer)) {
                                                                                    CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT.trim(), sessLanguage);
                                                                                } else {
                                                                                    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                    dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                    int[] intWSRes = new int[1];
                                                                                    String[] sWSRes = new String[1];
                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                    if (intWSRes[0] == 0) { }
                                                                                    else {
                                                                                        isProcess = false;
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                if (sPUSH_NOTICE_ENABLED == true) {
                                                                                    int[] intRes = new int[1];
                                                                                    String[] sRes = new String[1];
                                                                                    ConnectConnector.SendMailOTP(pStringCERTIFICATION_ID, intRes, sRes);
                                                                                }
                                                                                isProcess = true;
                                                                            }
                                                                        } else {
                                                                            isProcess = false;
                                                                            CommonFunction.LogErrorServlet(log, "Error: CERTIFICATION APPROVED, ATTR ID: " + String.valueOf(pCERTIFICATE_ATTR_ID[0]));
                                                                        }
                                                                    }
                                                                } else {
                                                                    strIsPushNotiApprove = "1";
                                                                }
                                                                //</editor-fold>
                                                            } else {
                                                                strIsPushNotiApprove = "1";
                                                            }
                                                        }
                                                        if(isProcess == true)
                                                        {
                                                            sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                            if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                                || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_SSL
                                                                || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING
                                                                || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                                strView = "0#" + pStringCERTIFICATION_ID + "#0#" + strIsPushNotiApprove;
                                                            } else {
                                                                strView = "0#" + pStringCERTIFICATION_ID + "#1#" + strIsPushNotiApprove;
                                                            }
                                                        } else {
                                                            String sCoreCAError = "";
                                                            CERTIFICATION[][] rsCertAttr = new CERTIFICATION[1][];
                                                            db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sessLanguage, rsCertAttr);
                                                            if(rsCertAttr[0].length > 0) {
                                                                sCoreCAError = rsCertAttr[0][0].CORE_ERROR_DESCRIPTION;
                                                            }
                                                            if(!"".equals(sCoreCAError)) {
                                                                RESPONSE_CORECA valueLog = objectMapper.readValue(sCoreCAError, RESPONSE_CORECA.class);
                                                                String sResMess = EscapeUtils.CheckTextNull(valueLog.responseMessage);
                                                                if(!"".equals(sResMess)) {
    //                                                                strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#" + valueLog.responseCode + " - " + sResMess;
                                                                    strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#0";// + valueLog.responseCode + " - " + sResMess;
                                                                    CommonFunction.LogDebugString(log, "There was an error returned from CoreCA", valueLog.responseCode + " - " + sResMess);
                                                                } else {
                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                }
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                            }
                                                        }
                                                    } else {
                                                        strView = sParam + "#0";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_CSR_EXISTS + "#0";
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_CSR_KEYSIZE + "#0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_NOEXISTS_TOKEN + "#0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "re-registrationcert": {
                                //<editor-fold defaultstate="collapsed" desc="re-registrationcert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String CERTIFICATION_ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String TOKEN_ID = EscapeUtils.CheckTextNull(request.getParameter("sTOKEN_ID"));
                                    String sTOKEN_SN = EscapeUtils.CheckTextNull(request.getParameter("sTOKEN_SN"));
                                    String pCREATE_USER = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    String sBRANCH_ID = "";
                                    String strReqValueATTR = "";
                                    String strIsPushNotiApprove = "0";
                                    // check agency
                                    String sAGENT_ID = "";
                                    boolean isAccessAgency = true;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(CERTIFICATION_ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sBRANCH_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                            if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                isAccessAgency = false;
//                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    //</editor-fold>
                                    
                                    if (isAccessAgency == true) {
                                        String PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                        String EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
                                        String CACoreSubject = EscapeUtils.CheckTextNull(request.getParameter("CACoreSubject"));
                                        String CertProfileID = EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                        String DN = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                        String pPERSONAL_NAME = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_NAME"));
                                        String pCOMPANY_NAME = EscapeUtils.CheckTextNull(request.getParameter("pCOMPANY_NAME"));
                                        String pDOMAIN_NAME = EscapeUtils.CheckTextNull(request.getParameter("pDOMAIN_NAME"));
                                        String pTAX_CODE = EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                        String pBUDGET_CODE = EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                        String pP_ID = EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                        String pCCCD = EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                        String pPASSPORT = EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                        String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                        String pPROVINCE_DESC = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_DESC"));
                                        String CheckCHANGE_KEY = "";
                                        String sPRIVATE_KEY = "";
                                        String CheckPRIVATE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckPRIVATE_KEY"));
                                        String pPAST_CERTIFICATE_ID = Definitions.CONFIG_CERTIFICATE_PAST_CERTIFICATE_ID;
                                        String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION);
                                        String pCERTIFICATION_SN = "";
                                        int pPKI_FORMFACTOR_ID = Definitions.CONFIG_PKI_FORMFACTOR_ID_HARD_TOKEN;
                                        String[] sUIDResult = new String[2];
                                        CommonReferServlet.collectFieldToUID(pTAX_CODE, pBUDGET_CODE, "", pP_ID, pPASSPORT, pCCCD, sUIDResult);
                                        String sEnterpriseCert = sUIDResult[0];
                                        String sPersonalCert = sUIDResult[1];
                                        
                                        //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                        CommonFunction.LogDebugString(log, "REGISTRATION-CERTIFICATION", "REQUEST: " + "SUBJECT: " + DN
                                                + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME + "; pTAX_CODE: " + pTAX_CODE
                                                + "; pBUDGET_CODE: " + pBUDGET_CODE + "; pP_ID: " + pP_ID + "; pCCCD: " + pCCCD + "; pPASSPORT: "
                                                + pPASSPORT + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                + "; pCERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID
                                                + "; pPKI_FORMFACTOR_ID: " + pPKI_FORMFACTOR_ID
                                                + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                + "; CACoreSubject: " + CACoreSubject + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                + "; PROVINCE_ID: " + pPROVINCE_ID + "; TOKEN_SN: " + sTOKEN_SN);
                                        tempLogReq = new CERTIFICATION_DATA_ATTR();
                                        objectMapper = new ObjectMapper();
                                        tempLogReq.personalName = pPERSONAL_NAME;
                                        tempLogReq.companyName = pCOMPANY_NAME;
                                        tempLogReq.taxCode = pTAX_CODE;
                                        tempLogReq.budgetCode = pBUDGET_CODE;
                                        tempLogReq.personalCode = pP_ID;
                                        tempLogReq.citizenCode = pCCCD;
                                        tempLogReq.passportCode = pPASSPORT;
                                        tempLogReq.emailContract = EMAIL_CONTRACT;
                                        tempLogReq.phoneContract = PHONE_CONTRACT;
                                        tempLogReq.issuerSubject = CACoreSubject;
                                        tempLogReq.subjectDn = DN;
                                        tempLogReq.tokenSn = sTOKEN_SN;
                                        tempLogReq.provinceName = pPROVINCE_DESC;
                                        tempLogReq.pkiFromFactorId = pPKI_FORMFACTOR_ID;
                                        tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                        String strReq = objectMapper.writeValueAsString(tempLogReq);
                                        db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                Definitions.CONFIG_LOG_FUNCTIONALITY_REQUEST_ISSUE, strReq,
                                                loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                        ATTRIBUTE_VALUES valueATTR;
                                        ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                        dataATTR.setCertificationData(tempLogReq);
                                        valueATTR = new ATTRIBUTE_VALUES();
                                        valueATTR.setTokenSn(sTOKEN_SN);
                                        valueATTR.setChangeKeyEnabled(true);
                                        valueATTR.setTypeName(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION);
                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                        valueATTR.setCreateUser(loginFullname + " (" + loginUID + ")");
                                        valueATTR.setCreateDt(new Date());
                                        valueATTR.setAttributeData(dataATTR);
                                        //</editor-fold>
                                        
                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                        String sParam = "1000";
                                        if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN))
                                        {
                                            // GEN OTP
                                            int intOTPNumn = 8;
                                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                            if (sessGeneralPolicy[0].length > 0) {
                                                for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_ACTIVATION_CODE_LENGTH)) {
                                                        intOTPNumn = Integer.parseInt(rsPolicy1.VALUE);
                                                        break;
                                                    }
                                                }
                                            }
                                            while ("1000".equals(sParam)) {
                                                try {
                                                    String sOTP = CommonFunction.getRandomOTP(intOTPNumn);
                                                    sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(TOKEN_ID), CertProfileID, sTOKEN_SN,
                                                            pCERTIFICATION_SN, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME, pTAX_CODE, pBUDGET_CODE, pP_ID,
                                                            pPASSPORT, DN, CACoreSubject, PHONE_CONTRACT, EMAIL_CONTRACT, sBRANCH_ID,
                                                            pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, sOTP,
                                                            strReqValueATTR, pCREATE_USER, loginUID, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID,
                                                            "", CheckPRIVATE_KEY, CheckCHANGE_KEY, sPRIVATE_KEY, pPKI_FORMFACTOR_ID, "", "", pCCCD, null,
                                                            "", sPersonalCert, sEnterpriseCert);
                                                } catch (Exception e) {
                                                    if (e.getMessage().contains(Definitions.CONFIG_MYSQL_UNIQUE_ACTIVATION_CODE)) {
                                                        sParam = "1000";
                                                    } else {
                                                        sParam = Definitions.CONFIG_EXCEPTION_STRING_ERROR;
                                                        CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
                                                    }
                                                }
                                            }
                                        } else {
                                            sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(TOKEN_ID), CertProfileID, sTOKEN_SN,
                                                pCERTIFICATION_SN, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME, pTAX_CODE, pBUDGET_CODE, pP_ID,
                                                pPASSPORT, DN, CACoreSubject, PHONE_CONTRACT, EMAIL_CONTRACT, sBRANCH_ID,
                                                pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, "",
                                                strReqValueATTR, pCREATE_USER, loginUID, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, "",
                                                CheckPRIVATE_KEY, CheckCHANGE_KEY, sPRIVATE_KEY, pPKI_FORMFACTOR_ID, "", "", pCCCD,
                                                null,"", sPersonalCert, sEnterpriseCert);
                                        }
                                        if ("0".equals(sParam)) {
                                            if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)) {
                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                    valueATTR.setApproveDt(new Date());
                                                    valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                    valueATTR.setApproveCADt(new Date());
                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                    if ("0".equals(sApprove)) {
                                                        boolean sPUSH_NOTICE_ENABLED = false;
                                                        int sCERTIFICATION_ID = 0;
                                                        CERTIFICATION[][] rsReqSendOTP = new CERTIFICATION[1][];
                                                        db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sessLanguage, rsReqSendOTP);
                                                        if (rsReqSendOTP[0].length > 0) {
                                                            sCERTIFICATION_ID = rsReqSendOTP[0][0].ID;
                                                            sPUSH_NOTICE_ENABLED = rsReqSendOTP[0][0].PUSH_NOTICE_ENABLED;
                                                        }
                                                        if (sPUSH_NOTICE_ENABLED == true) {
                                                            int[] intRes = new int[1];
                                                            String[] sRes = new String[1];
                                                            ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                            // RA SEND_EMAIL
                                                        }
                                                    }
                                                } else {
                                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                        valueATTR.setApproveCADt(new Date());
                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                        if ("0".equals(sApprove)) {
                                                            boolean sPUSH_NOTICE_ENABLED = false;
                                                            int sCERTIFICATION_ID = 0;
                                                            CERTIFICATION[][] rsReqSendOTP = new CERTIFICATION[1][];
                                                            db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sessLanguage, rsReqSendOTP);
                                                            if (rsReqSendOTP[0].length > 0) {
                                                                sCERTIFICATION_ID = rsReqSendOTP[0][0].ID;
                                                                sPUSH_NOTICE_ENABLED = rsReqSendOTP[0][0].PUSH_NOTICE_ENABLED;
                                                            }
                                                            if (sPUSH_NOTICE_ENABLED == true) {
                                                                int[] intRes = new int[1];
                                                                String[] sRes = new String[1];
                                                                ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                // RA SEND_EMAIL
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                    }
                                                }
                                            } else {
                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                    valueATTR.setApproveDt(new Date());
                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                    strIsPushNotiApprove = "1";
                                                } else {
                                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                        strIsPushNotiApprove = "1";
                                                    }
                                                }
                                            }
                                            int pCERTIFICATION_PURPOSE_ID = 0;
                                            String pCERTIFICATION_ID = "";
                                            CERTIFICATION[][] rs = new CERTIFICATION[1][];
                                            db.S_BO_CERTIFICATION_APPROVED_DETAIL(EscapeUtils.escapeHtml(String.valueOf(pCERTIFICATE_ATTR_ID[0])), sessLanguage, rs);
                                            if (rs[0].length > 0) {
                                                pCERTIFICATION_PURPOSE_ID = rs[0][0].CERTIFICATION_PURPOSE_ID;
                                                pCERTIFICATION_ID = String.valueOf(rs[0][0].ID);
                                            }
                                            sessionsa.setAttribute("RefreshRegisTokenSess", "1");
                                            if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE) {
                                                strView = "0#" + pCERTIFICATION_ID + "#0#" + strIsPushNotiApprove;
                                            } else {
                                                strView = "0#" + pCERTIFICATION_ID + "#1#" + strIsPushNotiApprove;
                                            }
                                        } else {
                                            strView = sParam + "#0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "renewalcert": {
                                //<editor-fold defaultstate="collapsed" desc="renewalcert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String pCSR = "";
                                    String CertProfileID = EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                    String CheckCHANGE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckCHANGE_KEY"));
                                    String CheckPRIVATE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckPRIVATE_KEY"));
                                    String CheckDeleteRevoke = EscapeUtils.CheckTextNull(request.getParameter("CheckDeleteRevoke"));
                                    String keepCertSNEnabled = EscapeUtils.CheckTextNull(request.getParameter("keepCertSNEnabled"));
                                    String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                    String pPERSONAL_NAME = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_NAME"));
                                    String pCOMPANY_NAME = EscapeUtils.CheckTextNull(request.getParameter("pCOMPANY_NAME"));
                                    String pDOMAIN_NAME = EscapeUtils.CheckTextNull(request.getParameter("pDOMAIN_NAME"));
                                    String pTAX_CODE = EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                    String pDECISION = EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                    String pBUDGET_CODE = EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                    String pP_ID = EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                    String pCCCD = EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                    String pPASSPORT = EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                    String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                    String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                    String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
                                    String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                    if(!"".equals(ID) && !"".equals(CertProfileID)){
                                        //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                        boolean isAccessAgency = true;
                                        boolean isProcess = true;
                                        String strIsPushNotiApprove = "0";
                                        String sTOKEN_ID = "";
                                        String sTOKEN_SN = "";
                                        String PHONE_CONTRACT = "";
                                        String EMAIL_CONTRACT = "";
                                        String CACoreSubject = "";
                                        String DN = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                        String pCERTIFICATION_SN = "";
                                        String pRSSP_CERT_SN = "";
                                        String pPRIVATE_KEY = "";
                                        String sAGENT_ID = "";
                                        String sAGENT_ID_OLD = "";
                                        String sVALUE_OLD = "";
                                        int pPKI_FORMFACTOR_ID = 0;
                                        int pCERTIFICATION_OWNER_ID = 0;
                                        int pCERTIFICATION_PURPOSE_ID_INNER = 0;
                                        boolean pPRIVATE_KEY_ENABLED = true;
                                        String pCREATE_USER = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                        CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(ID, sessLanguage, rsReq);
                                        if (rsReq[0].length > 0) {
                                            sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                            PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                            EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                            CACoreSubject = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                            pCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                            pRSSP_CERT_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                            sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                            sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            sAGENT_ID_OLD = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            sTOKEN_ID = String.valueOf(rsReq[0][0].TOKEN_ID);
                                            pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                            pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                            pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                            pCERTIFICATION_PURPOSE_ID_INNER = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                            if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                            } else {
                                                sAGENT_ID = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ID"));
                                            }
                                        } else {
                                            isAccessAgency = false;
                                        }
                                        //</editor-fold>

                                        if (isAccessAgency == true) {
                                            Config conf = new Config();
                                            boolean booRSSP_ACCESS_ENABLED = false;
                                            if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                                if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                    booRSSP_ACCESS_ENABLED = true;
                                                }
                                            }
                                            if(booRSSP_ACCESS_ENABLED == false) {
                                                String strPasswordP12 = "";
                                                boolean isCSRValid = true;
                                                boolean checkCSRNotExists = true;
                                                if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                {
                                                    if(pPRIVATE_KEY_ENABLED == true)
                                                    {
                                                        CheckPRIVATE_KEY = "1";
                                                        strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                                    } else {
                                                        if("1".equals(CheckCHANGE_KEY))
                                                        {
                                                            pCSR = EscapeUtils.CheckTextNull(request.getParameter("CSR"));
                                                            if("".equals(pCSR)) {
                                                                isCSRValid = false;
                                                            }
                                                            if(isCSRValid == true) {
                                                                String sPublicKeyHard = CommonFunction.getPublicKeyHasrCSR(pCSR);
                                                                int checkPublicKey = db.S_BO_CHECK_OWNER_HAVE_EXISTS_PUBLIC_KEY_HASH(0, null, null, null, sPublicKeyHard, pCERTIFICATION_OWNER_ID);
                                                                if(checkPublicKey == 0) {
                                                                    checkCSRNotExists = true;
                                                                } else {
                                                                    checkCSRNotExists = false;
                                                                }
                                                            }
                                                        }
                                                        CheckPRIVATE_KEY = "0";
                                                    }
                                                    CheckDeleteRevoke = "0";
                                                }

                                                if(isCSRValid == true) {
                                                    if(checkCSRNotExists == true) {
                                                        String pPAST_CERTIFICATE_ID = ID;
                                                        String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL);
                                                        String pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                        if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                                            || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                            || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL) {
                                                            pDEVICE = "";
                                                        }
                                                        
                                                        //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                                        tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                        objectMapper = new ObjectMapper();
                                                        tempLogReq.personalName = pPERSONAL_NAME;
                                                        tempLogReq.companyName = pCOMPANY_NAME;
                                                        tempLogReq.enterpriseID = pENTERPRISE_ID;
                                                        tempLogReq.personalID = pPERSONAL_ID;
                                                        tempLogReq.deviceUUID = pDEVICE;
                                                        tempLogReq.phoneContract = PHONE_CONTRACT;
                                                        tempLogReq.emailContract = EMAIL_CONTRACT;
                                                        tempLogReq.issuerSubject = CACoreSubject;
                                                        tempLogReq.subjectDn = DN;
                                                        tempLogReq.tokenSn = sTOKEN_SN;
                                                        tempLogReq.pkiFromFactorId = pPKI_FORMFACTOR_ID;
                                                        tempLogReq.typeName = pCERT_ATTR_TYPE_CODE;
                                                        String strReq = objectMapper.writeValueAsString(tempLogReq);
                                                        db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                                Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                                Definitions.CONFIG_LOG_FUNCTIONALITY_REQUEST_RENEWAL, strReq,
                                                                loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                        CommonFunction.LogDebugString(log, "RENEWAL-CERTIFICATION", "REQUEST: " + "SUBJECT: " + DN
                                                                + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME + "; pPERSONAL_ID: " + pPERSONAL_ID
                                                                + "; pENTERPRISE_ID: " + pENTERPRISE_ID + "; pDEVICE: " + pDEVICE + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                                + "; pCERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID
                                                                + "; pPKI_FORMFACTOR_ID: " + pPKI_FORMFACTOR_ID + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                                + "; CACoreSubject: " + CACoreSubject + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                                + "; TOKEN_SN: " + sTOKEN_SN + "; PROVINCE_ID: " + pPROVINCE_ID
                                                                + "; CheckDeleteCertificate: " + CheckDeleteRevoke);
                                                        ATTRIBUTE_VALUES valueATTR;
                                                        ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                        dataATTR.setCertificationData(tempLogReq);
                                                        valueATTR = new ATTRIBUTE_VALUES();
                                                        valueATTR.setTokenSn(sTOKEN_SN);
                                                        boolean sChangeKeyEnabled = "1".equals(CheckCHANGE_KEY);
                                                        valueATTR.setChangeKeyEnabled(sChangeKeyEnabled);
                                                        boolean sDeleteInTokenEnabled = "1".equals(CheckDeleteRevoke);
                                                        valueATTR.setDeleteOldCertificateEnabled(sDeleteInTokenEnabled);
                                                        boolean booKeepCertSNEnabled = "1".equals(keepCertSNEnabled);
                                                        valueATTR.setKeepCertificateSNEnabled(booKeepCertSNEnabled);
                                                        valueATTR.setTypeName(pCERT_ATTR_TYPE_CODE);
                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                        valueATTR.setCreateUser(loginFullname + " (" + loginUID + ")");
                                                        valueATTR.setCreateDt(new Date());
                                                        valueATTR.setAttributeData(dataATTR);
                                                        //</editor-fold>

                                                        String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        String sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(sTOKEN_ID), CertProfileID, sTOKEN_SN,
                                                                pCERTIFICATION_SN, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME, pTAX_CODE, pBUDGET_CODE, pP_ID,
                                                                pPASSPORT, DN, CACoreSubject, PHONE_CONTRACT, EMAIL_CONTRACT, sAGENT_ID,
                                                                pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, "",
                                                                strReqValueATTR, pCREATE_USER, loginUID, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, pCSR,
                                                                CheckPRIVATE_KEY, CheckCHANGE_KEY, pPRIVATE_KEY, pPKI_FORMFACTOR_ID, pDEVICE,
                                                                String.valueOf(pCERTIFICATION_OWNER_ID), pCCCD, null, pDECISION, pPERSONAL_ID, pENTERPRISE_ID);
                                                        if ("0".equals(sParam)) {
                                                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                            //<editor-fold defaultstate="collapsed" desc="### PROPERTIES SAN CERT">
                                                            if(!"".equals(pCOMPONENT_SAN)) {
                                                                String pPROPERTIES_SAN;
                                                                String[] pCompSanSub = pCOMPONENT_SAN.split("@@@");
                                                                List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes = new ArrayList<>();
                                                                for (String sPlitValue : pCompSanSub) {
                                                                    if(!"".equals(sPlitValue)) {
                                                                        String sKey = "";
                                                                        if(!"".equals(sPlitValue.split("###")[0].trim())) {
                                                                            sKey = sPlitValue.split("###")[0].trim();
                                                                        }
                                                                        objectMapper = new ObjectMapper();
                                                                        CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                                        attribute.setKey(sKey);
                                                                        attribute.setValue(sPlitValue.split("###")[1]);
                                                                        attributes.add(attribute);
                                                                    }
                                                                }
                                                                if(attributes.size() > 0) {
                                                                    pPROPERTIES_SAN = "{\"attributes\":" + objectMapper.writeValueAsString(attributes) + "}";
                                                                    db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(pCERTIFICATE_ID[0]), pPROPERTIES_SAN, loginUID);
                                                                }
                                                            }
                                                            //</editor-fold>

                                                            //<editor-fold defaultstate="collapsed" desc="### SHARED_MODE CERT">
                                                            boolean pSHARED_MODE_ENABLED = false;
                                                            BRANCH[][] rsBranchShare = new BRANCH[1][];
                                                            db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranchShare);
                                                            if(rsBranchShare[0].length > 0) {
                                                                String sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranchShare[0][0].CERTIFICATION_POLICY_PROPERTIES);
                                                                if(!"".equals(sCERT_POLICY_PROPERTIES)) {
                                                                    pSHARED_MODE_ENABLED = CommonFunction.getShareModeEnabledCert(sCERT_POLICY_PROPERTIES);
                                                                }
                                                            }
                                                            String pSHARED_MODE_UPDATE = pSHARED_MODE_ENABLED ? "1" : "0";
                                                            if(!"".equals(pSHARED_MODE_UPDATE)) {
                                                                db.S_BO_CERTIFICATION_UPDATE(pCERTIFICATE_ID[0], CertProfileID, "", "", "",
                                                                    pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, "", "", "", "", "",
                                                                    "", loginUID, "", "", "", "", pSHARED_MODE_UPDATE, pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                            }
                                                            //</editor-fold>
                                                            
                                                            //<editor-fold defaultstate="collapsed" desc="### PUBLIC KEY HASH">
                                                            if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                if(pPRIVATE_KEY_ENABLED == false) {
                                                                    if("1".equals(CheckCHANGE_KEY)) {
                                                                        pCSR = EscapeUtils.CheckTextNull(request.getParameter("CSR"));
                                                                        if(!"".equals(pCSR)) {
                                                                            String sPublicKeyCSR = CommonFunction.getPublicKeyHasrCSR(pCSR);
                                                                            db.S_BO_CERTIFICATION_UPDATE_CSR_INFO(pCERTIFICATE_ID[0], sPublicKeyCSR, loginUID);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            //</editor-fold>

                                                            //<editor-fold defaultstate="collapsed" desc="### FILE MANAGER INSERT">
                                                            String sJRBConfig = "";
                                                            if (sessGeneralPolicy[0].length > 0) {
                                                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                                {
                                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT))
                                                                    {
                                                                        sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                            int[] intResult = new int[1];
                                                            boolean isAutoSendFileToNew = false;
                                                            if("1".equals(LoadParamSystem.getParamStart(Definitions.CONFIG_FILE_FILE_UP_OLD_TO_NEW_AUTO_ENABLED))) {
                                                                isAutoSendFileToNew = true;
                                                            }
                                                            ServletUptoFunction.fileManagerInsert(sJRBConfig, cartToken, String.valueOf(pCERTIFICATE_ID[0]),
                                                                String.valueOf(pCERTIFICATION_OWNER_ID), loginUID, log, isAutoSendFileToNew, true, intResult);
                                                            if(intResult[0] == 0){
                                                                request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                            }
                                                            //</editor-fold>

                                                            if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                //<editor-fold defaultstate="collapsed" desc="### AGENCY ROOT">
                                                                boolean isCAApprove = false;
                                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                                {
                                                                    isCAApprove = true;
                                                                } else {
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                    {
                                                                        isCAApprove = true;
                                                                    }
                                                                }
                                                                if(isCAApprove == true)
                                                                {
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveCADt(new Date());
                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                    String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                    if ("0".equals(sApprove)) {
                                                                        String sNoAllowTranferToken = "1";
                                                                        String sDiscountRateOption = "0";
                                                                        if (sessGeneralPolicy[0].length > 0) {
                                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                                            {
                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                                                                                {
                                                                                    sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                }
                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_NO_AUTO_MOVE_TOKEN_FOR_RENEWAL_REVISION_CERTIFICATE_REQUEST))
                                                                                {
                                                                                    sNoAllowTranferToken = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                }
                                                                            }
                                                                        }
                                                                        if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                        {
                                                                            if("0".equals(sNoAllowTranferToken)) {
                                                                                if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                    db.S_BO_TOKEN_UPDATE_BRANCH_RESPONSIBLE(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                                }
                                                                            }
                                                                        }
                                                                        if ("1".equals(sDiscountRateOption)) {
                                                                            CommonReferServlet.updateDiscountRate(String.valueOf(pCERTIFICATE_ID[0]), sAGENT_ID, CertProfileID,
                                                                                pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, loginUID, pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                        }
                                                                        if(!"".equals(sTOKEN_ID))
                                                                        {
                                                                            if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID)))
                                                                            {
                                                                                String isActiveSignServer = "0";
                                                                                GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                if (sessGeneralPolicy1[0].length > 0) {
                                                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                    {
                                                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                            isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if("1".equals(isActiveSignServer)) {
                                                                                    CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT.trim(), sessLanguage);
                                                                                } else {
                                                                                    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                    dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                    int[] intWSRes = new int[1];
                                                                                    String[] sWSRes = new String[1];
                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                    if (intWSRes[0] == 0) { }
                                                                                    else {
                                                                                        isProcess = false;
                                                                                    }
                                                                                }
                                                                            } else { }
                                                                        }
                                                                    }
                                                                } else {
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                    db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                }
                                                                //</editor-fold>
                                                            } else {
                                                                //<editor-fold defaultstate="collapsed" desc="### AGENCY USER">
                                                                boolean checkCallApproveCA = false;
                                                                String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                                objectMapper = new ObjectMapper();
                                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                                {
                                                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                        if("0".equals(paramPre)) {
                                                                            checkCallApproveCA = true;
                                                                        }
                                                                    } else {
                                                                        int approveChilrenID = Integer.parseInt(userLoginID);
                                                                        BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                        db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                        if(rsUserApprve[0].length > 0) {
                                                                            for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                    approveChilrenID = item.ID;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID, approveChilrenID);
                                                                        if("0".equals(paramAgency)) {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                if("0".equals(paramPre)) {
                                                                                    checkCallApproveCA = true;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                            if("0".equals(paramPre)) {
                                                                                checkCallApproveCA = true;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                        if(intApprove == 1) {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                                int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                if(rsUserApprve[0].length > 0) {
                                                                                    for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                        if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                            approveChilrenID = item.ID;
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID, approveChilrenID);
                                                                                if("0".equals(paramAgency)) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                    {
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                        if("0".equals(paramPre)) {
                                                                                            checkCallApproveCA = true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if(checkCallApproveCA == true) {
                                                                    //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                    boolean autoApproveCA = false;
                                                                    CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                    CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                    objectMapper = new ObjectMapper();
                                                                    if(rsProfile[0].length > 0) {
                                                                        autoApproveCA = CommonFunction.getApproveCAOfProfile(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME), sessPolicyCert_Data);
                                                                    }
                                                                    if(autoApproveCA == true) {
                                                                        if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                                        {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveCADt(new Date());
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                            if ("0".equals(sApprove)) {
                                                                                String sDiscountRateOption = "0";
                                                                                String sNoAllowTranferToken = "1";
                                                                                if (sessGeneralPolicy[0].length > 0) {
                                                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                                                    {
                                                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                                                                                        {
                                                                                            sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                        }
                                                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_NO_AUTO_MOVE_TOKEN_FOR_RENEWAL_REVISION_CERTIFICATE_REQUEST))
                                                                                        {
                                                                                            sNoAllowTranferToken = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                                {
                                                                                    if("0".equals(sNoAllowTranferToken)) {
                                                                                        if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                            db.S_BO_TOKEN_UPDATE_BRANCH_RESPONSIBLE(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if ("1".equals(sDiscountRateOption)) {
                                                                                    CommonReferServlet.updateDiscountRate(String.valueOf(pCERTIFICATE_ID[0]), sAGENT_ID, CertProfileID,
                                                                                        pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, loginUID, pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                                }
                                                                                if(!"".equals(sTOKEN_ID))
                                                                                {
                                                                                    if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID)))
                                                                                    {
                                                                                        String isActiveSignServer = "0";
                                                                                        GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                        if (sessGeneralPolicy1[0].length > 0) {
                                                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                            {
                                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                                    isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if("1".equals(isActiveSignServer)) {
                                                                                            CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT.trim(), sessLanguage);
                                                                                        } else {
                                                                                            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                            dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                            int[] intWSRes = new int[1];
                                                                                            String[] sWSRes = new String[1];
                                                                                            ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                            if (intWSRes[0] == 0) { }
                                                                                            else {
                                                                                                isProcess = false;
                                                                                            }
                                                                                        }
                                                                                    } else { }
                                                                                }
                                                                            }
                                                                        }
                                                                    } else {
                                                                        strIsPushNotiApprove = "1";
                                                                    }
                                                                    //</editor-fold>
                                                                } else {
                                                                    strIsPushNotiApprove = "1";
                                                                }
                                                                //</editor-fold>
                                                            }
                                                            if(isProcess == true) {
                                                                int pCERTIFICATION_PURPOSE_ID = 0;
                                                                CERTIFICATION[][] rs = new CERTIFICATION[1][];
                                                                String pCERTIFICATION_ID = "";
                                                                db.S_BO_CERTIFICATION_APPROVED_DETAIL(EscapeUtils.escapeHtml(String.valueOf(pCERTIFICATE_ATTR_ID[0])), sessLanguage, rs);
                                                                if (rs[0].length > 0) {
                                                                    pCERTIFICATION_PURPOSE_ID = rs[0][0].CERTIFICATION_PURPOSE_ID;
                                                                    pCERTIFICATION_ID = String.valueOf(rs[0][0].ID);
                                                                }
                                                                sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_SSL
                                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE
                                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING)
                                                                {
                                                                    strView = "0#" + pCERTIFICATION_ID + "#0#" + strIsPushNotiApprove;
                                                                } else {
                                                                    strView = "0#" + pCERTIFICATION_ID + "#1#" + strIsPushNotiApprove;
                                                                }
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0#0";
                                                            }
                                                        } else {
                                                            strView = sParam + "#0#0";
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_CSR_EXISTS + "#0#0";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_CSR_NULL + "#0#0";
                                                }
                                            } else {
                                                //<editor-fold defaultstate="collapsed" desc="### RSSP PROCESS">
                                                boolean isAccessRssp = true;
                                                if (!"".equals(sVALUE_OLD)) {
                                                    objectMapper = new ObjectMapper();
                                                    ATTRIBUTE_VALUES valueRSSP = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                    if(EscapeUtils.CheckTextNull(valueRSSP.getRsspConnectWSMode()).equals(Definitions.CONFIG_RSSP_CONNECT_MODE_REST)) {
                                                        isAccessRssp = false;
                                                    }
                                                }
                                                if(isAccessRssp == true) {
                                                    PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                                    db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(pPKI_FORMFACTOR_ID), rsFormfactorPro);
                                                    String sFormFactorPro = "";
                                                    if(rsFormfactorPro[0].length > 0) {
                                                        sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                                    }
                                                    CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                                    if(credentialAuthen != null) {
                                                        RSSPProcessCommon clsRSSP = new RSSPProcessCommon();
                                                        AgreementDetails agreementDetails = null;// = new AgreementDetails();
                                                        agreementDetails = new AgreementDetails();
                                                        String sAgreementUUID = "";
                                                        String sRelyingPartyRSSP = "";
                                                        String sCertProfileCode = "";
                                                        String sCertProfileProperties = "";
                                                        //<editor-fold defaultstate="collapsed" desc="### CLOSE PROPERTIES SAN CERT">
//                                                        String strEmailSan = "";
//                                                        if(!"".equals(pCOMPONENT_SAN)) {
//                                                            CommonFunction.LogDebugString(log, "pCOMPONENT_SAN", pCOMPONENT_SAN);
//                                                            String[] pCompSanSub = pCOMPONENT_SAN.split("@@@");
//                                                            for (String sPlitValue : pCompSanSub) {
//                                                                if(!"".equals(sPlitValue)) {
//                                                                    if(sPlitValue.split("###")[0].trim().equals(Definitions.CONFIG_COMPONENT_SAN_TAG_rfc822Name))
//                                                                    {
//                                                                        strEmailSan = sPlitValue.split("###")[1];
//                                                                        break;
//                                                                    }
//                                                                }
//                                                            }
//                                                        }
                                                        //</editor-fold>

                                                        // get CertProfileID
                                                        CERTIFICATION_PROFILE[][] rsProfile;
                                                        rsProfile = new CERTIFICATION_PROFILE[1][];
                                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                        if(rsProfile[0].length > 0) {
                                                            sCertProfileCode = EscapeUtils.CheckTextNull(rsProfile[0][0].NAME);
                                                            sCertProfileProperties = EscapeUtils.CheckTextNull(rsProfile[0][0].PROPERTIES);
                                                        }
                                                        String pPROVINCE_CODE = "N/A";
                                                        //<editor-fold defaultstate="collapsed" desc="### GET CITY_PROVIN_CODE">
                                                        if(!"".equals(pPROVINCE_ID)) {
                                                            CITY_PROVINCE[][] rsProvince = new CITY_PROVINCE[1][];
                                                            db.S_BO_PROVINCE_DETAIL(pPROVINCE_ID, rsProvince);
                                                            if(rsProvince[0].length > 0) {
                                                                pPROVINCE_CODE = EscapeUtils.CheckTextNull(rsProvince[0][0].NAME);
                                                            }
                                                        }
                                                        //</editor-fold>
                                                        int serverOUCount = 0;
                                                        CERTIFICATION_TYPE_COMPONENT[][] resProfileData = new CERTIFICATION_TYPE_COMPONENT[1][];
                                                        CommonFunction.getJsonComponentForCert(sCertProfileProperties, resProfileData);
                                                        for(CERTIFICATION_TYPE_COMPONENT resProfileData1 : resProfileData[0])
                                                        {
                                                            if(EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID))
                                                            {
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE+":")) {
                                                                    agreementDetails.setTaxID(pTAX_CODE);
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE+":")) {
                                                                    agreementDetails.setBudgetID(pBUDGET_CODE);
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_PERSONAL_CODE+":")) {
                                                                    agreementDetails.setPersonalID(pP_ID);
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE+":")) {
                                                                    agreementDetails.setPassportID(pPASSPORT);
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_PERSONAL_CITIZEN_CODE+":")) {
                                                                    agreementDetails.setCitizenID(pCCCD);
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_SOCIAL_INSURANCE_CODE+":")) {
                                                                    if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                                                        || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE) {
                                                                        if(pENTERPRISE_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE)){
                                                                            agreementDetails.setSocialInsuranceNumber(pENTERPRISE_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE, ""));
                                                                        }
                                                                    } else {
                                                                        if(pPERSONAL_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE)){
                                                                            agreementDetails.setSocialInsuranceNumber(pPERSONAL_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE, ""));
                                                                        }
                                                                    }
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_DECISION+":")) {
                                                                    if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                                                        || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE) {
                                                                        if(pENTERPRISE_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION)){
                                                                            agreementDetails.setDecisionNumber(pENTERPRISE_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION, ""));
                                                                        }
                                                                    } else {
                                                                        if(pPERSONAL_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION)){
                                                                            agreementDetails.setDecisionNumber(pPERSONAL_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION, ""));
                                                                        }
                                                                    }
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_UNIT_CODE+":")) {
                                                                    if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                                                        || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE) {
                                                                        if(pENTERPRISE_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE)){
                                                                            agreementDetails.setUnitCode(pENTERPRISE_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE, ""));
                                                                        }
                                                                    } else {
                                                                        if(pPERSONAL_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE)){
                                                                            agreementDetails.setUnitCode(pPERSONAL_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE, ""));
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_T)) {
                                                                    agreementDetails.setTitle(CommonFunction.getRoleInDN(DN).trim());
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_C)) {
                                                                    agreementDetails.setCountry(CommonFunction.getCountryInDN(DN).trim());
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST)) {
                                                                    agreementDetails.setStateOrProvince(pPROVINCE_CODE);
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_E)) {
                                                                    agreementDetails.setEmail(CommonFunction.getEmailInDN(DN).trim());
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber)) {
                                                                    agreementDetails.setTelephoneNumber(CommonFunction.getPhoneInDN(DN).trim());
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_L)) {
                                                                    String sLocation = CommonFunction.getLocationInDN(DN).trim();
                                                                    agreementDetails.setLocation(sLocation);
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_O)) {
                                                                    if(EscapeUtils.CheckTextNull(resProfileData1.commomNameType).equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_PERSON)) {
                                                                        agreementDetails.setOrganization(pCOMPANY_NAME);
                                                                    } else {
                                                                        String sOrganzation = CommonFunction.getORGANIZATIONInDN(DN).trim();
                                                                        agreementDetails.setOrganization(sOrganzation);
                                                                    }
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN)) {
                                                                    if(EscapeUtils.CheckTextNull(resProfileData1.commomNameType).equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_COMPANY))
                                                                    {
                                                                        agreementDetails.setPersonalName(pCOMPANY_NAME);
                                                                    } else if(EscapeUtils.CheckTextNull(resProfileData1.commomNameType).equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_PERSON))
                                                                    {
                                                                        agreementDetails.setPersonalName(pPERSONAL_NAME);
                                                                    }
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU)) {
                                                                    serverOUCount = serverOUCount + 1;
//                                                                    agreementDetails.setOrganizationUnit(CommonFunction.getDepartmentInDN(DN).trim());
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_C)) {
                                                                    agreementDetails.setCountry(CommonFunction.getCountryInDN(DN).trim());
                                                                }
                                                            }
                                                        }
                                                        // add ou to rssp
                                                        String storeOU = EscapeUtils.CheckTextNull(request.getParameter("storeOU"));
                                                        CommonReferServlet.addComponentOURSSP(agreementDetails, serverOUCount, storeOU);
                                                        String[] sResultRSSP = new String[3];
                                                        if (!"".equals(sVALUE_OLD)) {
                                                            objectMapper = new ObjectMapper();
                                                            ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                            sAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                            sRelyingPartyRSSP = valueATTR_Frist.getRsspRelyingParty();
                                                        }
                                                        if(!"".equals(sAgreementUUID) && !"".equals(sRelyingPartyRSSP)) {
                                                            String sBeneficiaryUserName = "";
                                                            BACKOFFICE_USER[][] rsUser;
                                                            rsUser = new BACKOFFICE_USER[1][];
                                                            db.S_BO_USER_DETAIL(pCREATE_USER, sessLanguage, rsUser);
                                                            if(rsUser[0].length > 0) {
                                                                sBeneficiaryUserName = EscapeUtils.CheckTextNull(rsUser[0][0].USERNAME);
                                                            }
                                                            boolean booKeepCertSNEnabled = "1".equals(keepCertSNEnabled);
                                                            clsRSSP.prepareRenewCertificateForSignCloud(sAgreementUUID, agreementDetails, sCertProfileCode, sBeneficiaryUserName,
                                                                    pRSSP_CERT_SN, sRelyingPartyRSSP, booKeepCertSNEnabled, sResultRSSP, credentialAuthen);
                                                            if("0".equals(sResultRSSP[0])) {
                                                                objectMapper = new ObjectMapper();
                                                                int pCERTIFICATION_PURPOSE_ID = 0;
                                                                String sCertificateID = sResultRSSP[2];
                                                                if(!"".equals(sCertificateID) && !"0".equals(sCertificateID)) {
                                                                    String isProcessText = "";
                                                                    String sVALUE = "";
                                                                    CERTIFICATION[][] rsCert;
                                                                    rsCert = new CERTIFICATION[1][];
                                                                    db.S_BO_CERTIFICATION_DETAIL(sCertificateID, sessLanguage, rsCert);
                                                                    if(rsCert[0].length > 0)
                                                                    {
                                                                        sVALUE = rsCert[0][0].VALUE;
                                                                        pCERTIFICATION_PURPOSE_ID = rsCert[0][0].CERTIFICATION_PURPOSE_ID;
                                                                        pCERTIFICATION_OWNER_ID = rsCert[0][0].CERTIFICATION_OWNER_ID;
                                                                        pCERTIFICATE_ATTR_ID[0] = rsCert[0][0].CERTIFICATION_ATTR_ID;
                                                                    }
                                                                    //<editor-fold defaultstate="collapsed" desc="### FILE MANAGER INSERT">
                                                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                    String sJRBConfig = "";
                                                                    if (sessGeneralPolicy[0].length > 0) {
                                                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0]) {
                                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT)) {
                                                                                sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                                    int[] intResult = new int[1];
                                                                    ServletUptoFunction.fileManagerInsert(sJRBConfig, cartToken, sCertificateID,
                                                                        String.valueOf(pCERTIFICATION_OWNER_ID), loginUID, log, false, true, intResult);
                                                                    if(intResult[0] == 0){
                                                                        request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                                    }
                                                                    //</editor-fold>
                                                                    
                                                                    ATTRIBUTE_VALUES valueATTR = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                                                                    if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                        boolean isCAApprove = false;
                                                                        if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                                        {
                                                                            isCAApprove = true;
                                                                        } else {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                                isCAApprove = true;
                                                                            }
                                                                        }
                                                                        if(isCAApprove == true)
                                                                        {
                                                                            String sChangeKeyApprove = valueATTR.getChangeKeyEnabled() ? "1" : "0";
                                                                            sResultRSSP = new String[2];
                                                                            clsRSSP.approveCertificateForSignCloud(sAgreementUUID, sCertificateID, "", sRelyingPartyRSSP, sResultRSSP,
                                                                                pRSSP_CERT_SN, sChangeKeyApprove, Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_RENEW_CERT, "",credentialAuthen, false);
                                                                            if(!"0".equals(sResultRSSP[0]))
                                                                            {
                                                                                isProcess = false;
                                                                                isProcessText = sResultRSSP[1];
                                                                            }
                                                                        } else {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                        }
                                                                    } else {
                                                                        boolean checkCallApproveCA = false;
                                                                        String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                                        if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                                        {
                                                                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                if("0".equals(paramPre)) {
                                                                                    checkCallApproveCA = true;
                                                                                }
                                                                            } else {
                                                                                int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                if(rsUserApprve[0].length > 0) {
                                                                                    for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                        if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                            approveChilrenID = item.ID;
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                                                if("0".equals(paramAgency)) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                    {
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                        if("0".equals(paramPre)) {
                                                                                            checkCallApproveCA = true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                {
                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                    valueATTR.setApproveDt(new Date());
                                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                    if("0".equals(paramPre)) {
                                                                                        checkCallApproveCA = true;
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                                if(intApprove == 1) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                    {
                                                                                        int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                        BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                        db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                        if(rsUserApprve[0].length > 0) {
                                                                                            for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                                if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                                    approveChilrenID = item.ID;
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                                                        if("0".equals(paramAgency)) {
                                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                            {
                                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                                valueATTR.setApproveDt(new Date());
                                                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                                if("0".equals(paramPre)) {
                                                                                                    checkCallApproveCA = true;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        if(checkCallApproveCA == true) {
                                                                            //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                            boolean autoApproveCA = false;
                                                                            rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                            db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                            CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                            if(rsProfile[0].length > 0) {
                                                                                autoApproveCA = CommonFunction.getApproveCAOfProfile(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME), sessPolicyCert_Data);
                                                                            }
                                                                            if(autoApproveCA == true) {
                                                                                if(CommonFunction.checkApproveCAReqType(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION, sessPolicyCert_Data) == true)
                                                                                {
                                                                                    String sChangeKeyApprove = valueATTR.getChangeKeyEnabled() ? "1" : "0";
                                                                                    sResultRSSP = new String[2];
                                                                                    clsRSSP.approveCertificateForSignCloud(sAgreementUUID, sCertificateID, "", sRelyingPartyRSSP, sResultRSSP,
                                                                                        pRSSP_CERT_SN, sChangeKeyApprove, Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_RENEW_CERT, "", credentialAuthen, false);
                                                                                    if(!"0".equals(sResultRSSP[0])) {
                                                                                        isProcess = false;
                                                                                        isProcessText = sResultRSSP[1];
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                strIsPushNotiApprove = "1";
                                                                            }
                                                                            //</editor-fold>
                                                                        } else {
                                                                            strIsPushNotiApprove = "1";
                                                                        }
                                                                    }
                                                                    if(isProcess == true)
                                                                    {
                                                                        sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_SSL
                                                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING
                                                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                                            strView = "0#" + sCertificateID + "#0#" + strIsPushNotiApprove;
                                                                        } else {
                                                                            strView = "0#" + sCertificateID + "#1#" + strIsPushNotiApprove;
                                                                        }
                                                                    } else {
                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#" + isProcessText;
                                                                    }
                                                                } else {
                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#Error: The certificate information returned from eSignCloud is invalid";
                                                                }
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#Error: " + sResultRSSP[0] + " - " + sResultRSSP[1];
                                                            }
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#There does not exist the value of Agreement UUID connecting to eSignCloud.";
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM + "#0";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0#0";
                                                    CommonFunction.LogErrorServlet(log, "RSSP REST not supported yet.");
                                                }
                                                //</editor-fold>
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0#0";
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "CertRenew: Cert ID or Profile ID cannot be empty");
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "revokecert": {
                                //<editor-fold defaultstate="collapsed" desc="revokecert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String idDESC_DECLINE = EscapeUtils.CheckTextNull(request.getParameter("DESC_DECLINE"));
                                    if(!"".equals(ID)) {
                                        //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                        String strIsPushNotiApprove = "0";
                                        boolean isAccessAgency = true;
                                        String sTOKEN_SN = "";
                                        String PHONE_CONTRACT = "";
                                        String EMAIL_CONTRACT = "";
                                        String CACoreSubject = "";
                                        String DN = "";
                                        String pPERSONAL_NAME = "";
                                        String pCOMPANY_NAME = "";
                                        String pENTERPRISE_ID = "";
                                        String pPERSONAL_ID = "";
                                        String pCERTIFICATION_SN = "";
                                        String pCERTIFICATION_AUTHORITY_ID = "";
                                        String sPROVINCE_ID = "";
                                        String sAGENT_ID = "";
                                        String sVALUE_OLD = "";
                                        int pPKI_FORMFACTOR_ID =0;
                                        int pCERTIFICATION_ID =0;
                                        int pCERTIFICATION_OWNER_ID= 0;
                                        int sBeneficiaryUserID =0;
                                        CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(ID, sessLanguage, rsReq);
                                        if (rsReq[0].length > 0) {
                                            for(CERTIFICATION rsReqItem : rsReq[0])
                                            {
                                                if(rsReqItem.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED
                                                    || rsReqItem.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_RENEWED
                                                    || rsReqItem.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVISED
                                                    || rsReqItem.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REISSUED)
                                                {
                                                    pCERTIFICATION_ID = rsReq[0][0].ID;
                                                    pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                                    PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                                    EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                                    CACoreSubject = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                                    DN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                                    pPERSONAL_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                                    pCOMPANY_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                                    pENTERPRISE_ID = rsReq[0][0].ENTERPRISE_ID;
                                                    pPERSONAL_ID = rsReq[0][0].PERSONAL_ID;
                                                    sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                                    sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                                    sPROVINCE_ID = String.valueOf(rsReq[0][0].CITY_PROVINCE_ID);
                                                    pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                                    sBeneficiaryUserID = rsReq[0][0].CREATED_BY_ID;
                                                    pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                                    sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                                    pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                                    if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                        isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                                    }
                                                    break;
                                                }
                                            }
                                        } else {
                                            isAccessAgency = false;
                                        }
                                        //</editor-fold>

                                        if (isAccessAgency == true) {
                                            ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                            String pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE;
                                            Config conf = new Config();
                                            boolean booRSSP_ACCESS_ENABLED = false;
                                            if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                                if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                    booRSSP_ACCESS_ENABLED = true;
                                                    if (!"".equals(sVALUE_OLD)) {
                                                        objectMapper = new ObjectMapper();
                                                        ATTRIBUTE_VALUES valueRSSP = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                        if(EscapeUtils.CheckTextNull(valueRSSP.getRsspConnectWSMode()).equals(Definitions.CONFIG_RSSP_CONNECT_MODE_REST)) {
                                                            booRSSP_ACCESS_ENABLED = false;
                                                        }
                                                    }
                                                }
                                            }
                                            if(booRSSP_ACCESS_ENABLED == false) {
                                                String CheckDeleteRevoke = "";
                                                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                                if (sessGeneralPolicy[0].length > 0) {
                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                    {
                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_DELETE_CERT_WHEN_REVOKE))
                                                        {
                                                            CheckDeleteRevoke = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                            break;
                                                        }
                                                    }
                                                }
                                                String pPAST_CERTIFICATE_ID = ID;
                                                String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE);
                                                //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                                tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                tempLogReq.personalName = pPERSONAL_NAME;
                                                tempLogReq.companyName = pCOMPANY_NAME;
                                                tempLogReq.enterpriseID = pENTERPRISE_ID;
                                                tempLogReq.personalID = pPERSONAL_ID;
                                                tempLogReq.emailContract = EMAIL_CONTRACT;
                                                tempLogReq.phoneContract = PHONE_CONTRACT;
                                                tempLogReq.issuerSubject = CACoreSubject;
                                                tempLogReq.subjectDn = DN;
                                                if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                    if (!"".equals(sVALUE_OLD)) {
                                                        objectMapper = new ObjectMapper();
                                                        ATTRIBUTE_VALUES valueRSSP = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                        if(valueRSSP.getTokenSn() != null) {
                                                            tempLogReq.tokenSn = valueRSSP.getTokenSn().trim();
                                                        }
                                                    }
                                                } else {
                                                    tempLogReq.tokenSn = sTOKEN_SN;
                                                }
                                                tempLogReq.pkiFromFactorId = pPKI_FORMFACTOR_ID;
                                                tempLogReq.typeName = pCERT_ATTR_TYPE_CODE;
                                                objectMapper = new ObjectMapper();
                                                String strReq = objectMapper.writeValueAsString(tempLogReq);
                                                db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                        Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                        Definitions.CONFIG_LOG_FUNCTIONALITY_REQUEST_REVOKE, strReq,
                                                        loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                CommonFunction.LogDebugString(log, "REVOKE-CERTIFICATION", "REQUEST: " + "SUBJECT: " + DN
                                                        + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME + "; pENTERPRISE_ID: " + pENTERPRISE_ID
                                                         + "; pPERSONAL_ID: " + pPERSONAL_ID + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                        + "; pPKI_FORMFACTOR_ID: " + pPKI_FORMFACTOR_ID
                                                        + "; pCERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID
                                                        + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                        + "; CACoreSubject: " + CACoreSubject + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                        + "; TOKEN_SN: " + sTOKEN_SN + "; PROVINCE_ID: " + sPROVINCE_ID);
                                                // VALUE ATTR
                                                ATTRIBUTE_VALUES valueATTR;
                                                ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                dataATTR.setCertificationData(tempLogReq);
                                                valueATTR = new ATTRIBUTE_VALUES();
                                                valueATTR.setTokenSn(sTOKEN_SN);
                                                valueATTR.setCerttificateRevokeReason(idDESC_DECLINE);
                                                boolean sRevokeDeleteInTokenFrist = "1".equals(CheckDeleteRevoke);
                                                valueATTR.setCertRevokeDeleteInTokenEnabled(sRevokeDeleteInTokenFrist);
                                                valueATTR.setTypeName(pCERT_ATTR_TYPE_CODE);
                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                valueATTR.setCreateUser(loginFullname + " (" + loginUID + ")");
                                                valueATTR.setCreateDt(new Date());
                                                valueATTR.setAttributeData(dataATTR);
                                                //</editor-fold>

                                                String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                String sParam = db.S_BO_CERTIFICATION_ATTR_INSERT(ID, pCERTIFICATION_ATTR_TYPE_ID,strReqValueATTR,
                                                    loginUID, pCERTIFICATE_ATTR_ID);
                                                if ("0".equals(sParam)) {
                                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_EFY)
                                                        || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)
                                                        || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
                                                        //<editor-fold defaultstate="collapsed" desc="### FILE MANAGER INSERT">
                                                        String sJRBConfig = "";
                                                        if (sessGeneralPolicy[0].length > 0) {
                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                            {
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT))
                                                                {
                                                                    sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                        int[] intResult = new int[1];
                                                        boolean isAutoSendFileToNew = false;
                                                        if("1".equals(LoadParamSystem.getParamStart(Definitions.CONFIG_FILE_FILE_UP_OLD_TO_NEW_AUTO_ENABLED))) {
                                                            isAutoSendFileToNew = true;
                                                        }
                                                        ServletUptoFunction.fileManagerInsert(sJRBConfig, cartToken, String.valueOf(pCERTIFICATION_ID),
                                                            String.valueOf(pCERTIFICATION_OWNER_ID), loginUID, log, isAutoSendFileToNew, true, intResult);
                                                        if(intResult[0] == 0){
                                                            request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                        }
                                                        //</editor-fold>
                                                    }
                                                    
                                                    if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        //<editor-fold defaultstate="collapsed" desc="### CA PROCESS">
                                                        if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                        {
                                                            // Approve Agency
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                // Approve CA
                                                            String idCERT_REVOCATION_REASON = EscapeUtils.CheckTextNull(request.getParameter("CERT_REVOCATION_REASON"));
                                                            CheckDeleteRevoke = EscapeUtils.CheckTextNull(request.getParameter("CheckDeleteRevoke"));
                                                            if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                CheckDeleteRevoke = "0";
                                                            }
                                                            valueATTR.setCerttificateRevokeEJBCAReason(idCERT_REVOCATION_REASON);
                                                            boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteRevoke);
                                                            valueATTR.setCertRevokeDeleteInTokenEnabled(sRevokeDeleteInTokenEnabled);
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                            valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveCADt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                            if("0".equals(sApprove))
                                                            {
                                                                // RACONNECTOR
                                                                String pCA_NAME = "";
                                                                CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                    pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                }
                                                                int[] intRes = new int[1];
                                                                String[] sRes = new String[1];
                                                                // AFTER_EDIT
                                                                ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Integer.parseInt(idCERT_REVOCATION_REASON), 
                                                                    pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID[0]);
                                                                if (intRes[0] == 0) {
                                                                    db.S_BO_CERTIFICATION_REVOKED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(CheckDeleteRevoke), loginUID);
                                                                    //<editor-fold defaultstate="collapsed" desc="### Update Reason Revoke">
                                                                    /*objectMapper = new ObjectMapper();
                                                                    CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                    jsonCertComment.certificateDeclineReason = "";
                                                                    jsonCertComment.certificateSuspendReason = "";
                                                                    jsonCertComment.certificateRevokeReason = idDESC_DECLINE;
                                                                    idDESC_DECLINE = objectMapper.writeValueAsString(jsonCertComment);
                                                                    db.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(ID, idDESC_DECLINE, loginUID);*/
                                                                    db.S_BO_CERTIFICATION_UPDATE_REVOCATION_REASON(Integer.parseInt(ID), idDESC_DECLINE, userLoginID);
                                                                    //</editor-fold>

                                                                    sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                    strView = "0#0";
                                                                } else {
                                                                    strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#0";
                                                                }
                                                            } else {
                                                                CommonFunction.LogDebugString(log, "ERROR S_BO_CERTIFICATION_APPROVED - RESPONSE", sApprove);
                                                                strView =  Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                            }
                                                        } else {
                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,
                                                                Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                String idCERT_REVOCATION_REASON = EscapeUtils.CheckTextNull(request.getParameter("CERT_REVOCATION_REASON"));
                                                                // Approve Agency
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                    // Approve CA
                                                                CheckDeleteRevoke = EscapeUtils.CheckTextNull(request.getParameter("CheckDeleteRevoke"));
                                                                if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                    || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                    CheckDeleteRevoke = "0";
                                                                }
                                                                valueATTR.setCerttificateRevokeEJBCAReason(idCERT_REVOCATION_REASON);
                                                                boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteRevoke);
                                                                valueATTR.setCertRevokeDeleteInTokenEnabled(sRevokeDeleteInTokenEnabled);
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveCADt(new Date());
                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                if("0".equals(sApprove))
                                                                {
                                                                    // RACONNECTOR
                                                                    String pCA_NAME = "";
                                                                    CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                    db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                    if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                        pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                    }
                                                                    int[] intRes = new int[1];
                                                                    String[] sRes = new String[1];
                                                                            // AFTER_EDIT
                                                                    ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Integer.parseInt(idCERT_REVOCATION_REASON),
                                                                        pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID[0]);
                                                                    if (intRes[0] == 0) {
                                                                        db.S_BO_CERTIFICATION_REVOKED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(CheckDeleteRevoke), loginUID);
                                                                        //<editor-fold defaultstate="collapsed" desc="### Update Reason Revoke">
                                                                        /*objectMapper = new ObjectMapper();
                                                                        CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                        jsonCertComment.certificateDeclineReason = "";
                                                                        jsonCertComment.certificateSuspendReason = "";
                                                                        jsonCertComment.certificateRevokeReason = idDESC_DECLINE;
                                                                        idDESC_DECLINE = objectMapper.writeValueAsString(jsonCertComment);
                                                                        db.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(ID, idDESC_DECLINE, loginUID);*/
                                                                        db.S_BO_CERTIFICATION_UPDATE_REVOCATION_REASON(Integer.parseInt(ID), idDESC_DECLINE, userLoginID);
                                                                        //</editor-fold>

                                                                        sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                        strView = "0#0";
                                                                    } else {
                                                                        strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#0";
                                                                    }
                                                                } else {
                                                                    CommonFunction.LogDebugString(log, "ERROR S_BO_CERTIFICATION_APPROVED - RESPONSE", sApprove);
                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                }
                                                            } else {
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                strView = "0#0";
                                                            }
                                                        }
                                                        //</editor-fold>
                                                    } else {
                                                        //<editor-fold defaultstate="collapsed" desc="### AGENCY PROCESS">
                                                        boolean checkCallApproveCA = false;
                                                        String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                        objectMapper = new ObjectMapper();
                                                        if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                        {
                                                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                if("0".equals(paramPre)) {
                                                                    checkCallApproveCA = true;
                                                                }
                                                            } else {
                                                                int approveChilrenID = Integer.parseInt(userLoginID);
                                                                BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                if(rsUserApprve[0].length > 0) {
                                                                    for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                        if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                            approveChilrenID = item.ID;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID, approveChilrenID);
                                                                if("0".equals(paramAgency)) {
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                    {
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                        if("0".equals(paramPre)) {
                                                                            checkCallApproveCA = true;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                {
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                    if("0".equals(paramPre)) {
                                                                        checkCallApproveCA = true;
                                                                    }
                                                                }
                                                            } else {
                                                                int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                if(intApprove == 1) {
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                    {
                                                                        int approveChilrenID = Integer.parseInt(userLoginID);
                                                                        BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                        db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                        if(rsUserApprve[0].length > 0) {
                                                                            for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                    approveChilrenID = item.ID;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID, approveChilrenID);
                                                                        if("0".equals(paramAgency)) {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                if("0".equals(paramPre)) {
                                                                                    checkCallApproveCA = true;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if(checkCallApproveCA == true)
                                                        {
                                                            //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                            boolean autoApproveCA = false;
                                                            CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                            objectMapper = new ObjectMapper();
                                                            if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                            {
                                                                autoApproveCA = true;
                                                            }
                                                            if(autoApproveCA == true) {
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                String idCERT_REVOCATION_REASON = String.valueOf(Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID);
                                                                if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                    || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                    CheckDeleteRevoke = "0";
                                                                }
                                                                valueATTR.setCerttificateRevokeEJBCAReason(idCERT_REVOCATION_REASON);
                                                                boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteRevoke);
                                                                valueATTR.setCertRevokeDeleteInTokenEnabled(sRevokeDeleteInTokenEnabled);
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveCADt(new Date());
                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                if("0".equals(sApprove))
                                                                {
                                                                    String pCA_NAME = "";
                                                                    CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                    db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                    if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                        pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                    }
                                                                    int[] intRes = new int[1];
                                                                    String[] sRes = new String[1];
                                                                    ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Integer.parseInt(idCERT_REVOCATION_REASON), 
                                                                        pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID[0]);
                                                                    if (intRes[0] == 0) {
                                                                        db.S_BO_CERTIFICATION_REVOKED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(CheckDeleteRevoke), loginUID);
                                                                        //<editor-fold defaultstate="collapsed" desc="### Update Reason Revoke">
                                                                        /*objectMapper = new ObjectMapper();
                                                                        CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                        jsonCertComment.certificateDeclineReason = "";
                                                                        jsonCertComment.certificateSuspendReason = "";
                                                                        jsonCertComment.certificateRevokeReason = idDESC_DECLINE;
                                                                        idDESC_DECLINE = objectMapper.writeValueAsString(jsonCertComment);
                                                                        db.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(ID, idDESC_DECLINE, loginUID);*/
                                                                        db.S_BO_CERTIFICATION_UPDATE_REVOCATION_REASON(Integer.parseInt(ID), idDESC_DECLINE, userLoginID);
                                                                        //</editor-fold>

                                                                        sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                        strView = "0#0";
                                                                    } else {
                                                                        strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#0";
                                                                    }
                                                                } else {
                                                                    CommonFunction.LogDebugString(log, "ERROR S_BO_CERTIFICATION_APPROVED - RESPONSE", sApprove);
                                                                    strView =  Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                }
                                                            } else {
                                                                sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                strView = "0#0";
                                                                strIsPushNotiApprove = "1";
                                                            }
                                                            //</editor-fold>
                                                        } else {
                                                            sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                            strView = "0#0";
                                                            strIsPushNotiApprove = "1";
                                                        }
                                                        //</editor-fold>
                                                    }
                                                } else {
                                                    strView = sParam + "#0";
                                                }
                                            } else {
                                                //<editor-fold defaultstate="collapsed" desc="### RSSP Process">
                                                PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                                db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                                String sFormFactorPro = "";
                                                if(rsFormfactorPro[0].length > 0) {
                                                    sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                                }
                                                CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                                if(credentialAuthen != null) {
                                                    boolean isProcess = true;
                                                    String isProcessText = "";
                                                    RSSPProcessCommon clsRSSP = new RSSPProcessCommon();
                                                    String sAgreementUUID = "";
                                                    String sRelyingPartyRSSP = "";
                                                    String[] sResultRSSP = new String[3];
                                                    if (!"".equals(sVALUE_OLD)) {
                                                        objectMapper = new ObjectMapper();
                                                        ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                        sAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                        sRelyingPartyRSSP = valueATTR_Frist.getRsspRelyingParty();
                                                    }
                                                    String sReasonTransfer = idDESC_DECLINE;
                                                    if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        String idCERT_REVOCATION_REASON = EscapeUtils.CheckTextNull(request.getParameter("CERT_REVOCATION_REASON"));
                                                        if(!"".equals(idCERT_REVOCATION_REASON)) {
                                                            if(Integer.parseInt(idCERT_REVOCATION_REASON) != Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID) {
                                                                CERTIFICATION_REVOCATION_REASON[][] rsReasonCA = new CERTIFICATION_REVOCATION_REASON[1][];
                                                                db.S_BO_CERTIFICATION_REVOCATION_REASON_DETAIL(idCERT_REVOCATION_REASON, rsReasonCA);
                                                                if(rsReasonCA[0].length > 0) {
                                                                    sReasonTransfer = rsReasonCA[0][0].NAME;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if(!"".equals(sAgreementUUID)) {
                                                        ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                        String isRestoreStatusOld = EscapeUtils.CheckTextNull(request.getParameter("CheckRestoreStatusOld"));
                                                        int checkStatus = dbTwo.S_BO_CHECK_OLD_CERTIFICATION_INFO(Integer.parseInt(ID));
                                                        if(checkStatus == 0){
                                                            isRestoreStatusOld = "0";
                                                        }
                                                        clsRSSP.prepareRevokeCertificateForSignCloud(sAgreementUUID, pCERTIFICATION_SN, sReasonTransfer, "",
                                                            sRelyingPartyRSSP, sResultRSSP, credentialAuthen, "1".equals(isRestoreStatusOld));
                                                        if("0".equals(sResultRSSP[0])) {
                                                            String sCertificateID = sResultRSSP[2];
                                                            objectMapper = new ObjectMapper();
                                                            String sVALUE = "";
                                                            int[] pRESPONSE_CODE = new int[1];
                                                            CertificateInfo[][] certInfo = new CertificateInfo[1][];
                                                            db.S_BO_API_CERTIFICATION_GET_INFO("", "", "", "", "", Integer.parseInt(sCertificateID), "", "",
                                                                Integer.parseInt(sessLanguage), pRESPONSE_CODE, certInfo, "", "", "", "");
                                                            if (certInfo[0].length > 0) {
                                                                sVALUE = certInfo[0][0].value;
                                                                pCERTIFICATE_ATTR_ID[0] = certInfo[0][0].requestId;
                                                            }
                                                            ATTRIBUTE_VALUES valueATTR = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                                                            if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                //<editor-fold defaultstate="collapsed" desc="### CA PROCESS">
                                                                boolean isCAApprove = false;
                                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                                {
                                                                    isCAApprove = true;
                                                                } else {
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                    {
                                                                        isCAApprove = true;
                                                                    }
                                                                }
                                                                if(isCAApprove == true)
                                                                {
                                                                    sResultRSSP = new String[2];
                                                                    clsRSSP.approveCertificateForSignCloud(sAgreementUUID, sCertificateID, "",
                                                                        sRelyingPartyRSSP, sResultRSSP, pCERTIFICATION_SN,"", Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_REVOKE_CERT,
                                                                        "", credentialAuthen, "1".equals(isRestoreStatusOld));
                                                                    if(!"0".equals(sResultRSSP[0])) {
                                                                        isProcess = false;
                                                                        isProcessText = sResultRSSP[1];
                                                                    }
                                                                } else {
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                }
                                                                //</editor-fold>
                                                            } else {
                                                                //<editor-fold defaultstate="collapsed" desc="### AGENCY PROCESS">
                                                                boolean checkCallApproveCA = false;
                                                                String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                                {
                                                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                        if("0".equals(paramPre)) {
                                                                            checkCallApproveCA = true;
                                                                        }
                                                                    } else {
                                                                        int approveChilrenID = Integer.parseInt(userLoginID);
                                                                        BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                        db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                        if(rsUserApprve[0].length > 0) {
                                                                            for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                    approveChilrenID = item.ID;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                                        if("0".equals(paramAgency)) {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                if("0".equals(paramPre)) {
                                                                                    checkCallApproveCA = true;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                            if("0".equals(paramPre)) {
                                                                                checkCallApproveCA = true;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                        if(intApprove == 1) {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                                int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                if(rsUserApprve[0].length > 0) {
                                                                                    for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                        if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                            approveChilrenID = item.ID;
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                                                if("0".equals(paramAgency)) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                    {
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                        if("0".equals(paramPre)) {
                                                                                            checkCallApproveCA = true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if(checkCallApproveCA == true){
                                                                    //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                    boolean autoApproveCA = false;
                                                                    CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                    if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                                    {
                                                                        autoApproveCA = true;
                                                                    }
                                                                    if(autoApproveCA == true) {
                                                                        sResultRSSP = new String[2];
                                                                        clsRSSP.approveCertificateForSignCloud(sAgreementUUID, sCertificateID, "",
                                                                            sRelyingPartyRSSP, sResultRSSP, pCERTIFICATION_SN,"", Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_REVOKE_CERT,
                                                                            "", credentialAuthen, "1".equals(isRestoreStatusOld));
                                                                        if(!"0".equals(sResultRSSP[0])) {
                                                                            isProcess = false;
                                                                            isProcessText = sResultRSSP[1];
                                                                        }
                                                                    } else {
                                                                        strIsPushNotiApprove = "1";
                                                                    }
                                                                    //</editor-fold>
                                                                } else {
                                                                    strIsPushNotiApprove = "1";
                                                                }
                                                                //</editor-fold>
                                                            }
                                                            if(isProcess == true)
                                                            {
                                                                sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                strView = "0#0";
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#Error: " + isProcessText;
                                                            }
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#" + sResultRSSP[1];
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#There does not exist the value of Agreement UUID connecting to eSignCloud.";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM + "#0";
                                                }
                                                //</editor-fold>
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "CertRevoke: Cert ID cannot be empty");
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "changeinfocert": {
                                //<editor-fold defaultstate="collapsed" desc="changeinfocert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String CertProfileID = EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                    String CACoreSubject = EscapeUtils.CheckTextNull(request.getParameter("CACoreSubject"));
                                    String DN = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                    String pPERSONAL_NAME = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_NAME"));
                                    String pCOMPANY_NAME = EscapeUtils.CheckTextNull(request.getParameter("pCOMPANY_NAME"));
                                    String pDOMAIN_NAME = EscapeUtils.CheckTextNull(request.getParameter("pDOMAIN_NAME"));
                                    String pTAX_CODE = EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                    String pDECISION = EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                    String pBUDGET_CODE = EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                    String pP_ID = EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                    String pCCCD = EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                    String pPASSPORT = EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                    String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
                                    String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                    String pPROVINCE_DESC = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_DESC"));
                                    String CheckCHANGE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckCHANGE_KEY"));
                                    String CheckPRIVATE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckPRIVATE_KEY"));
                                    String CheckREVOKE_ENABLED = EscapeUtils.CheckTextNull(request.getParameter("CheckREVOKE_ENABLED"));
                                    String CheckDeleteRevoke = EscapeUtils.CheckTextNull(request.getParameter("CheckDeleteRevoke"));
                                    String keepCertSNEnabled = EscapeUtils.CheckTextNull(request.getParameter("keepCertSNEnabled"));
                                    String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                    String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                    String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                    if(!"".equals(ID) && !"".equals(CertProfileID))
                                    {
                                        //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                        boolean isAccessAgency = true;
                                        boolean isProcess = true;
                                        String pCSR = "";
                                        String strIsPushNotiApprove = "0";
                                        String sTOKEN_ID = "";
                                        String sTOKEN_SN = "";
                                        String sPRIVATE_KEY = "";
                                        String PHONE_CONTRACT = "";
                                        String EMAIL_CONTRACT = "";
                                        String pCERTIFICATION_SN = "";
                                        String pRSSP_CERT_SN = "";
                                        String sAGENT_ID = "";
                                        String sAGENT_ID_OLD = "";
                                        String pCREATE_USER = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                        int pPKI_FORMFACTOR_ID=0;
                                        int pCERTIFICATION_OWNER_ID=0;
                                        int pCERTIFICATION_PURPOSE_ID_INNER=0;
                                        String pDISCOUNT_RATE="0";
                                        String sVALUE_OLD="";
                                        String pENTERPRISE_ID_OLD ="";
                                        String pPERSONAL_ID_OLD = "";
                                        boolean pPRIVATE_KEY_ENABLED = true;
                                        CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(ID, sessLanguage, rsReq);
                                        if (rsReq[0].length > 0) {
                                            sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                            PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                            EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                            sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                            pCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                            CACoreSubject = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                            sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            sAGENT_ID_OLD = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            sTOKEN_ID = String.valueOf(rsReq[0][0].TOKEN_ID);
                                            pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                            pENTERPRISE_ID_OLD = rsReq[0][0].ENTERPRISE_ID;
                                            pPERSONAL_ID_OLD = rsReq[0][0].PERSONAL_ID;
                                            pCERTIFICATION_PURPOSE_ID_INNER = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                            pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                            pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                            pRSSP_CERT_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                            pDISCOUNT_RATE = String.valueOf(rsReq[0][0].DISCOUNT_RATE);
                                            if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                            } else {
                                                sAGENT_ID = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ID"));
                                            }
                                        } else {
                                            isAccessAgency = false;
                                        }
                                        //</editor-fold>

                                        if (isAccessAgency == true) {
                                            Config conf = new Config();
                                            boolean booRSSP_ACCESS_ENABLED = false;
                                            if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                                if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                    booRSSP_ACCESS_ENABLED = true;
                                                }
                                            }
                                            if(booRSSP_ACCESS_ENABLED == false) {
                                                String strPasswordP12 = "";
                                                boolean isCSRValid = true;
                                                boolean isCSR_SizeValid = true;
                                                boolean checkCSRNotExists = true;
                                                String strDNSName = "";
                                                if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                {
                                                    if(pPRIVATE_KEY_ENABLED == true)
                                                    {
                                                        CheckPRIVATE_KEY = "1";
                                                        strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                                    } else {
                                                        if("1".equals(CheckCHANGE_KEY))
                                                        {
                                                            pCSR = EscapeUtils.CheckTextNull(request.getParameter("CSR"));
                                                            if("".equals(pCSR))
                                                            {
                                                                isCSRValid = false;
                                                            }
                                                            if(isCSRValid == true)
                                                            {
                                                                String sKeySizeDB;
                                                                isCSR_SizeValid = false;
                                                                CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                                                db.S_BO_GET_ALGORITHM_KEY_SIZE(CertProfileID, rsCert);
                                                                if(rsCert[0].length > 0)
                                                                {
                                                                    sKeySizeDB = EscapeUtils.CheckTextNull(rsCert[0][0].KEY_SIZE);
                                                                    String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(pCSR);
                                                                    isCSR_SizeValid = sKeySizeDB.equals(sKeySizeCSR);
                                                                }
                                                                if(isCSR_SizeValid == true) {
                                                                    String sPublicKeyHard = CommonFunction.getPublicKeyHasrCSR(pCSR);
                                                                    int checkPublicKey = db.S_BO_CHECK_OWNER_HAVE_EXISTS_PUBLIC_KEY_HASH(0, null, null, null, sPublicKeyHard, pCERTIFICATION_OWNER_ID);
                                                                    // call store cert Operation check exist sPublicKeyHard
                                                                    if(checkPublicKey == 0) {
                                                                        checkCSRNotExists = true;
                                                                    } else {
                                                                        checkCSRNotExists = false;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        CheckPRIVATE_KEY = "0";
                                                    }
                                                    CheckDeleteRevoke = "0";
                                                }
                                                if(isCSRValid == true) {
                                                    if(isCSR_SizeValid == true) {
                                                        if(checkCSRNotExists == true) {
                                                            /*boolean isValidMSTForExists = true;
                                                            String isCheckMSTExists = conf.GetPropertybyCode(Definitions.CONFIG_DENIED_WHEN_EXISTS_TAXCODE_REGISTER_CERT);
                                                            if("2".equals(isCheckMSTExists)) {
                                                                String sOwnerExsist = db.S_BO_CHECK_OWNER_HAVE_EXISTS_CERTIFICATION(pENTERPRISE_ID, pPERSONAL_ID, Integer.parseInt(pPKI_FORMFACTOR));
                                                                if("1".equals(sOwnerExsist)) {
                                                                    isValidMSTForExists = false;
                                                                }
                                                            }*/
                                                            
                                                            CERTIFICATION_PROFILE[][] rsProfile;
                                                            String sParamOwner = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                            String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_CMC) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_MID)) {
                                                                if(!pENTERPRISE_ID_OLD.equals(pENTERPRISE_ID) || !pPERSONAL_ID_OLD.equals(pPERSONAL_ID)) {
                                                                    CheckCHANGE_KEY = "1";
                                                                    CheckDeleteRevoke = "1";
                                                                    keepCertSNEnabled = "0";
                                                                    //<editor-fold defaultstate="collapsed" desc="### OWNER PROCESS">
                                                                    String dnUniqueEnabled = "0";
                                                                    rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                    if(rsProfile[0].length > 0) {
                                                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(rsProfile[0][0].CERTIFICATION_AUTHORITY_ID), rsCA);
                                                                        if(rsCA[0].length > 0) {
                                                                            dnUniqueEnabled = rsCA[0][0].ENFORCE_UNIQUE_DN;
                                                                        }
                                                                    }
                                                                    boolean isCallOwnerInsert = true;
                                                                    String pCERTIFICATION_OWNER_TYPE_ID = "";
                                                                    if(pCERTIFICATION_PURPOSE_ID_INNER != Definitions.CONFIG_CERTTYPE_ID_PERSONAL_GOV
                                                                        && pCERTIFICATION_PURPOSE_ID_INNER != Definitions.CONFIG_CERTTYPE_ID_ENTERPRISE_GOV)
                                                                    {
                                                                        if(!"".equals(pENTERPRISE_ID) && "".equals(pPERSONAL_ID)) {
                                                                            pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_ENTERPRISE);
                                                                        }
                                                                        if("".equals(pENTERPRISE_ID) && !"".equals(pPERSONAL_ID)) {
                                                                            pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL);
                                                                        }
                                                                        if(!"".equals(pENTERPRISE_ID) && !"".equals(pPERSONAL_ID)) {
                                                                            pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL);
                                                                        }
                                                                        if("".equals(pENTERPRISE_ID) && "".equals(pPERSONAL_ID))
                                                                        {
                                                                            isCallOwnerInsert = false;
                                                                        }
                                                                    } else {
                                                                        if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_ID_PERSONAL_GOV)
                                                                        {
                                                                            pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL_GOV);
                                                                        } else if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_ID_ENTERPRISE_GOV)
                                                                        {
                                                                            pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_ENTERPRISE_GOV);
                                                                        }
                                                                    }
                                                                    if(!"".equals(pCERTIFICATION_OWNER_TYPE_ID)) {
                                                                        if("1".equals(dnUniqueEnabled)) {
                                                                            int isCheckUnique = db.S_BO_CHECK_ENFORCE_UNIQUE_DN(Integer.parseInt(pCERTIFICATION_OWNER_TYPE_ID), pENTERPRISE_ID,
                                                                                pPERSONAL_ID, EMAIL_CONTRACT, DN, 0);
                                                                            if(isCheckUnique != 0) {
                                                                                sParamOwner = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUBJECT_DN_INVALID;
                                                                            }
                                                                        }
                                                                        if(sParamOwner.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                                            if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                                if(!"".equals(pCSR) && pPRIVATE_KEY_ENABLED == false) {
                                                                                    String sPublicKeyHard = CommonFunction.getPublicKeyHasrCSR(pCSR);
                                                                                    int checkPublicKey = db.S_BO_CHECK_OWNER_HAVE_EXISTS_PUBLIC_KEY_HASH(Integer.parseInt(pCERTIFICATION_OWNER_TYPE_ID), pENTERPRISE_ID,
                                                                                        pPERSONAL_ID, EMAIL_CONTRACT, sPublicKeyHard, 0);
                                                                                    if(checkPublicKey != 0) {
                                                                                        sParamOwner = String.valueOf(Definitions.CONFIG_WS_RESPONSE_CODE_CLIENT_CSR_EXISTS);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        if(sParamOwner.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                                            String pMESSAGING_QUEUE_FUNCTION_ID = String.valueOf(Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_REGISTRATION_OWNER);
                                                                            String[] pRESPONSE_CODE_NAME = new String[1];
                                                                            int[] pCERTIFICATION_OWNER_ID_INNER = new int[1];
                                                                            int[] pMESSAGING_QUEUE_ID = new int[1];
                                                                            String sLocation = CommonFunction.getLocationInDN(DN).trim();
                                                                            String pADDRESS;
                                                                            if(!"".equals(sLocation)) {
                                                                                pADDRESS = CommonFunction.replaceStringCharaterSpecialDN(sLocation, true, true) + ", " + CommonFunction.getStateOrProvinceInDN(DN);
                                                                            } else {
                                                                                pADDRESS = CommonFunction.getStateOrProvinceInDN(DN);
                                                                            }
                                                                            if(isCallOwnerInsert == true) {
                                                                                String pREPRESENTATIVE = "";
                                                                                String pREPRESENTATIVE_POSITION = "";
                                                                                //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR OWNER ">
                                                                                CERTIFICATION_OWNER_DATA_ATTR tempLogReq_Owner = new CERTIFICATION_OWNER_DATA_ATTR();
                                                                                tempLogReq_Owner.personalName = pPERSONAL_NAME;
                                                                                tempLogReq_Owner.companyName = pCOMPANY_NAME;
                                                                                tempLogReq_Owner.taxCode = pTAX_CODE;
                                                                                tempLogReq_Owner.decision = pDECISION;
                                                                                tempLogReq_Owner.budgetCode = pBUDGET_CODE;
                                                                                tempLogReq_Owner.personalCode = pP_ID;
                                                                                tempLogReq_Owner.passportCode = pPASSPORT;
                                                                                tempLogReq_Owner.citizenID = pCCCD;
                                                                                tempLogReq_Owner.emailContract = EMAIL_CONTRACT;
                                                                                tempLogReq_Owner.phoneContract = PHONE_CONTRACT;
                                                                                tempLogReq_Owner.address = pADDRESS;
                                                                                tempLogReq_Owner.representative = pREPRESENTATIVE;
                                                                                tempLogReq_Owner.representativePosition = pREPRESENTATIVE_POSITION;
                                                                                tempLogReq_Owner.typeName = Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CODE_REGISTRATION_OWNER;
                                                                                tempLogReq_Owner.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING;
                                                                                tempLogReq_Owner.createUser = loginFullname + " (" + loginUID + ")";
                                                                                tempLogReq_Owner.createDt = new Date();
                                                                                //</editor-fold>
                                                                                objectMapper = new ObjectMapper();
                                                                                String sOwnerUUID = CommonFunction.getUUIDV4();
                                                                                db.S_BO_CERTIFICATION_OWNER_INSERT(pPERSONAL_NAME, pCOMPANY_NAME,
                                                                                    pENTERPRISE_ID, pPERSONAL_ID, pCERTIFICATION_OWNER_TYPE_ID, PHONE_CONTRACT,
                                                                                    EMAIL_CONTRACT, loginUID, pADDRESS, pREPRESENTATIVE, pREPRESENTATIVE_POSITION,
                                                                                    pMESSAGING_QUEUE_FUNCTION_ID, objectMapper.writeValueAsString(tempLogReq_Owner),
                                                                                    sOwnerUUID, pRESPONSE_CODE_NAME, pCERTIFICATION_OWNER_ID_INNER, pMESSAGING_QUEUE_ID);
                                                                                CommonFunction.LogDebugString(log, "CERTIFICATION_OWNER_INSERT Result", pRESPONSE_CODE_NAME[0]);
                                                                                if (!"0".equals(pRESPONSE_CODE_NAME[0])) {
                                                                                    if (pRESPONSE_CODE_NAME[0].trim().equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_OWNER_EXISTS)) {
                                                                                        pCERTIFICATION_OWNER_ID = pCERTIFICATION_OWNER_ID_INNER[0];
                                                                                    } else {
                                                                                        sParamOwner = pRESPONSE_CODE_NAME[0];
                                                                                    }
                                                                                } else {
                                                                                    pCERTIFICATION_OWNER_ID = pCERTIFICATION_OWNER_ID_INNER[0];
                                                                                }
                                                                            } else {
                                                                                pCERTIFICATION_OWNER_ID = 1;
                                                                            }
                                                                        }
                                                                    }
                                                                    //</editor-fold>
                                                                }
                                                            }
                                                            System.out.println("sParamOwner: " + sParamOwner);
                                                            if(!sParamOwner.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                                RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                                                                db.S_BO_API_RESPONSE_CODE_GET_INFO(sParamOwner, Integer.parseInt(sessLanguage), rsResponseCode);
                                                                if (rsResponseCode[0].length > 0) {
                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR+"#"+rsResponseCode[0][0].REMARK;
                                                                } else {
                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                }
                                                            } else {
                                                                String pPAST_CERTIFICATE_ID = ID;
                                                                String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO);
                                                                String pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                                if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                                                    || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                                    || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL) {
                                                                    pDEVICE = "";
                                                                }
                                                                //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                                                tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                                objectMapper = new ObjectMapper();
                                                                tempLogReq.personalName = pPERSONAL_NAME;
                                                                tempLogReq.companyName = pCOMPANY_NAME;
                                                                tempLogReq.domainName = pDOMAIN_NAME;
                                                                tempLogReq.enterpriseID = pENTERPRISE_ID;
                                                                tempLogReq.personalID = pPERSONAL_ID;
                                                                tempLogReq.deviceUUID = pDEVICE;
                                                                tempLogReq.emailContract = EMAIL_CONTRACT;
                                                                tempLogReq.phoneContract = PHONE_CONTRACT;
                                                                tempLogReq.issuerSubject = CACoreSubject;
                                                                tempLogReq.subjectDn = DN;
                                                                tempLogReq.tokenSn = sTOKEN_SN;
                                                                tempLogReq.provinceName = pPROVINCE_DESC;
                                                                tempLogReq.pkiFromFactorId = pPKI_FORMFACTOR_ID;
                                                                tempLogReq.typeName = pCERT_ATTR_TYPE_CODE;
                                                                String strReq = objectMapper.writeValueAsString(tempLogReq);
                                                                db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                                        Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                                        Definitions.CONFIG_LOG_FUNCTIONALITY_REQUEST_CHANGE_INFO, strReq,
                                                                        loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                                if("1".equals(keepCertSNEnabled)) {
                                                                    CheckREVOKE_ENABLED = "0";
                                                                }
                                                                CommonFunction.LogDebugString(log, "ChangeInfoCertificate", "SUBJECT: " + DN
                                                                        + "; PERSONAL_NAME: " + pPERSONAL_NAME + "; COMPANY_NAME: " + pCOMPANY_NAME
                                                                        + "; pENTERPRISE_ID: " + pENTERPRISE_ID + "; DOMAIN_NAME: " + pDOMAIN_NAME + "; pPERSONAL_ID: " + pPERSONAL_ID
                                                                        + "; sAGENT_ID: " + sAGENT_ID+ "; pCREATE_USER: " + pCREATE_USER + "; PKI_FORMFACTOR_ID: " + pPKI_FORMFACTOR_ID
                                                                        + "; pDEVICE: " + pDEVICE + "; PAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                                        + "; CERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                                        + "; CACoreSubject: " + CACoreSubject + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                                        + "; TOKEN_SN: " + sTOKEN_SN + "; PROVINCE_DESC: " + pPROVINCE_DESC
                                                                        + "; CheckCHANGE_KEY: " + CheckCHANGE_KEY + "; CheckREVOKE_ENABLED: " + CheckREVOKE_ENABLED
                                                                        + "; CheckDeleteCertificate: " + CheckDeleteRevoke+ "; pCERTIFICATION_OWNER_ID: " + pCERTIFICATION_OWNER_ID);
                                                                ATTRIBUTE_VALUES valueATTR;
                                                                ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                                dataATTR.setCertificationData(tempLogReq);
                                                                valueATTR = new ATTRIBUTE_VALUES();
                                                                valueATTR.setTokenSn(sTOKEN_SN);
                                                                boolean sChangeKeyEnabled = "1".equals(CheckCHANGE_KEY);
                                                                valueATTR.setChangeKeyEnabled(sChangeKeyEnabled);
                                                                boolean sRevokedEnabled = "1".equals(CheckREVOKE_ENABLED);
                                                                valueATTR.setRevokeOldCertificateEnabled(sRevokedEnabled);
                                                                boolean sDeleteInTokenEnabled = "1".equals(CheckDeleteRevoke);
                                                                valueATTR.setDeleteOldCertificateEnabled(sDeleteInTokenEnabled);
                                                                boolean booKeepCertSNEnabled = "1".equals(keepCertSNEnabled);
                                                                valueATTR.setKeepCertificateSNEnabled(booKeepCertSNEnabled);
                                                                valueATTR.setTypeName(pCERT_ATTR_TYPE_CODE);
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                valueATTR.setCreateUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setCreateDt(new Date());
                                                                valueATTR.setAttributeData(dataATTR);
                                                                //</editor-fold>

                                                                String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                String sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(sTOKEN_ID), CertProfileID, sTOKEN_SN,
                                                                        pCERTIFICATION_SN, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME, pTAX_CODE, pBUDGET_CODE, pP_ID,
                                                                        pPASSPORT, DN, CACoreSubject, PHONE_CONTRACT, EMAIL_CONTRACT, sAGENT_ID,
                                                                        pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, "",
                                                                        strReqValueATTR, pCREATE_USER, loginUID, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, pCSR,
                                                                        CheckPRIVATE_KEY, CheckCHANGE_KEY, sPRIVATE_KEY, pPKI_FORMFACTOR_ID, pDEVICE,
                                                                        String.valueOf(pCERTIFICATION_OWNER_ID), pCCCD, null, pDECISION, pPERSONAL_ID, pENTERPRISE_ID);
                                                                if ("0".equals(sParam)) {
                                                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                    //<editor-fold defaultstate="collapsed" desc="### SHARED_MODE CERT">
                                                                    boolean pSHARED_MODE_ENABLED = false;
                                                                    BRANCH[][] rsBranchShare = new BRANCH[1][];
                                                                    db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranchShare);
                                                                    if(rsBranchShare[0].length > 0) {
                                                                        String sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranchShare[0][0].CERTIFICATION_POLICY_PROPERTIES);
                                                                        if(!"".equals(sCERT_POLICY_PROPERTIES)) {
                                                                            pSHARED_MODE_ENABLED = CommonFunction.getShareModeEnabledCert(sCERT_POLICY_PROPERTIES);
                                                                        }
                                                                    }
                                                                    String pSHARED_MODE_UPDATE = pSHARED_MODE_ENABLED ? "1" : "0";
                                                                    db.S_BO_CERTIFICATION_UPDATE(pCERTIFICATE_ID[0], CertProfileID, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME,
                                                                        pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, DN, "", "", "", "",
                                                                        CheckPRIVATE_KEY, loginUID, "", "", "", pDISCOUNT_RATE, pSHARED_MODE_UPDATE, pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                    //</editor-fold>

                                                                    //<editor-fold defaultstate="collapsed" desc="### PROPERTIES SAN CERT">
                                                                    if(!"".equals(pCOMPONENT_SAN)) {
                                                                        String[] pCompSanSub = pCOMPONENT_SAN.split("@@@");
                                                                        List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes = new ArrayList<>();
                                                                        for (String sPlitValue : pCompSanSub) {
                                                                            if(!"".equals(sPlitValue)) {
                                                                                String sKey = "";
                                                                                if(!"".equals(sPlitValue.split("###")[0].trim())) {
                                                                                    sKey = sPlitValue.split("###")[0].trim();
                                                                                }
                                                                                objectMapper = new ObjectMapper();
                                                                                CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                                                attribute.setKey(sKey);
                                                                                attribute.setValue(sPlitValue.split("###")[1]);
                                                                                attributes.add(attribute);
                                                                            }
                                                                        }
                                                                        if(attributes.size() > 0) {
                                                                            strDNSName = "{\"attributes\":" + objectMapper.writeValueAsString(attributes) + "}";
                                                                            db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(pCERTIFICATE_ID[0]), strDNSName, loginUID);
                                                                        }
                                                                    }
                                                                    //</editor-fold>

                                                                    //<editor-fold defaultstate="collapsed" desc="### PUBLIC KEY HASH">
                                                                    if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                        if(pPRIVATE_KEY_ENABLED == false) {
                                                                            if("1".equals(CheckCHANGE_KEY)) {
                                                                                pCSR = EscapeUtils.CheckTextNull(request.getParameter("CSR"));
                                                                                if(!"".equals(pCSR)) {
                                                                                    String sPublicKeyCSR = CommonFunction.getPublicKeyHasrCSR(pCSR);
                                                                                    db.S_BO_CERTIFICATION_UPDATE_CSR_INFO(pCERTIFICATE_ID[0], sPublicKeyCSR, loginUID);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    //</editor-fold>

                                                                    //<editor-fold defaultstate="collapsed" desc="### FILE MANAGER INSERT">
                                                                    String sJRBConfig = "";
                                                                    if (sessGeneralPolicy[0].length > 0) {
                                                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                                        {
                                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT))
                                                                            {
                                                                                sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                                    int[] intResult = new int[1];
                                                                    boolean isAutoSendFileToNew = false;
                                                                    if("1".equals(LoadParamSystem.getParamStart(Definitions.CONFIG_FILE_FILE_UP_OLD_TO_NEW_AUTO_ENABLED))) {
                                                                        isAutoSendFileToNew = true;
                                                                    }
                                                                    ServletUptoFunction.fileManagerInsert(sJRBConfig, cartToken, String.valueOf(pCERTIFICATE_ID[0]),
                                                                        String.valueOf(pCERTIFICATION_OWNER_ID), loginUID, log, isAutoSendFileToNew, true, intResult);
                                                                    if(intResult[0] == 0){
                                                                        request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                                    }
                                                                    //</editor-fold>

                                                                    if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                        //<editor-fold defaultstate="collapsed" desc="### AGENCY ROOT LOGIN">
                                                                        if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                                        {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveCADt(new Date());
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                            if ("0".equals(sApprove)) {
                                                                                String sNoAllowTranferToken = "1";
                                                                                if (sessGeneralPolicy[0].length > 0) {
                                                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                                                    {
                                                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_NO_AUTO_MOVE_TOKEN_FOR_RENEWAL_REVISION_CERTIFICATE_REQUEST))
                                                                                        {
                                                                                            sNoAllowTranferToken = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true){
                                                                                    if("0".equals(sNoAllowTranferToken)) {
                                                                                        if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                            db.S_BO_TOKEN_UPDATE_BRANCH_RESPONSIBLE(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(!"".equals(sTOKEN_ID))
                                                                                {
                                                                                    if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID))) {
                                                                                        String isActiveSignServer = "0";
                                                                                        GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                        if (sessGeneralPolicy1[0].length > 0) {
                                                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                            {
                                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                                    isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if("1".equals(isActiveSignServer)) {
                                                                                            CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT.trim(), sessLanguage);
                                                                                        } else {
                                                                                            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                            dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                            int[] intWSRes = new int[1];
                                                                                            String[] sWSRes = new String[1];
                                                                                            ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                            if (intWSRes[0] == 0) { }
                                                                                            else {
                                                                                                isProcess = false;
                                                                                            }
                                                                                        }
                                                                                    } else { }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,
                                                                                Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveCADt(new Date());
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                if ("0".equals(sApprove)) {
                                                                                    String sNoAllowTranferToken = "1";
                                                                                    if (sessGeneralPolicy[0].length > 0) {
                                                                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                                                        {
                                                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_NO_AUTO_MOVE_TOKEN_FOR_RENEWAL_REVISION_CERTIFICATE_REQUEST))
                                                                                            {
                                                                                                sNoAllowTranferToken = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                                    {
                                                                                        if("0".equals(sNoAllowTranferToken)) {
                                                                                            if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                                db.S_BO_TOKEN_UPDATE_BRANCH_RESPONSIBLE(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(sTOKEN_ID))
                                                                                    {
                                                                                        if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                            || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                            || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                                            || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID)))
                                                                                        {
                                                                                            String isActiveSignServer = "0";
                                                                                            GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                            if (sessGeneralPolicy1[0].length > 0) {
                                                                                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                                {
                                                                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                                        isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                        break;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            if("1".equals(isActiveSignServer)) {
                                                                                                CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT.trim(), sessLanguage);
                                                                                            } else {
                                                                                                ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                                dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                                int[] intWSRes = new int[1];
                                                                                                String[] sWSRes = new String[1];
                                                                                                ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                                if (intWSRes[0] == 0) { }
                                                                                                else {
                                                                                                    isProcess = false;
                                                                                                }
                                                                                            }
                                                                                        } else { }
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                            }
                                                                        }
                                                                        //</editor-fold>
                                                                    } else {
                                                                        //<editor-fold defaultstate="collapsed" desc="### AGENCY USER LOGIN">
                                                                        boolean checkCallApproveCA = false;
                                                                        String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                                        objectMapper = new ObjectMapper();
                                                                        if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                                        {
                                                                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                if("0".equals(paramPre)) {
                                                                                    checkCallApproveCA = true;
                                                                                }
                                                                            } else {
                                                                                int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                if(rsUserApprve[0].length > 0) {
                                                                                    for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                        if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                            approveChilrenID = item.ID;
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                                                if("0".equals(paramAgency)) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                    {
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                        if("0".equals(paramPre)) {
                                                                                            checkCallApproveCA = true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                {
                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                    valueATTR.setApproveDt(new Date());
                                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                    if("0".equals(paramPre)) {
                                                                                        checkCallApproveCA = true;
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                                if(intApprove == 1) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                    {
                                                                                        int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                        BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                        db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                        if(rsUserApprve[0].length > 0) {
                                                                                            for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                                if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                                    approveChilrenID = item.ID;
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                                                        if("0".equals(paramAgency)) {
                                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                            {
                                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                                valueATTR.setApproveDt(new Date());
                                                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                                if("0".equals(paramPre)) {
                                                                                                    checkCallApproveCA = true;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        if(checkCallApproveCA == true) {
                                                                            //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                            boolean autoApproveCA = false;
                                                                            rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                            db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                            CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                            objectMapper = new ObjectMapper();
                                                                            if(rsProfile[0].length > 0) {
                                                                                autoApproveCA = CommonFunction.getApproveCAOfProfile(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME), sessPolicyCert_Data);
                                                                            }
                                                                            if(autoApproveCA == true) {
                                                                                if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                                                {
                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                    valueATTR.setApproveDt(new Date());
                                                                                    valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                                    valueATTR.setApproveCADt(new Date());
                                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                    String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                                    if ("0".equals(sApprove)) {
                                                                                        String sNoAllowTranferToken = "1";
                                                                                        if (sessGeneralPolicy[0].length > 0) {
                                                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                                                            {
                                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_NO_AUTO_MOVE_TOKEN_FOR_RENEWAL_REVISION_CERTIFICATE_REQUEST))
                                                                                                {
                                                                                                    sNoAllowTranferToken = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                                        {
                                                                                            if("0".equals(sNoAllowTranferToken)) {
                                                                                                if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                                    db.S_BO_TOKEN_UPDATE_BRANCH_RESPONSIBLE(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(!"".equals(sTOKEN_ID))
                                                                                        {
                                                                                            if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID))) {
                                                                                                String isActiveSignServer = "0";
                                                                                                GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                                if (sessGeneralPolicy1[0].length > 0) {
                                                                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                                    {
                                                                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                                            isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                            break;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                if("1".equals(isActiveSignServer)) {
                                                                                                    CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT.trim(), sessLanguage);
                                                                                                } else {
                                                                                                    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                                    dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                                    int[] intWSRes = new int[1];
                                                                                                    String[] sWSRes = new String[1];
                                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                                    if (intWSRes[0] == 0) { }
                                                                                                    else {
                                                                                                        isProcess = false;
                                                                                                    }
                                                                                                }
                                                                                            } else { }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                strIsPushNotiApprove = "1";
                                                                            }
                                                                            //</editor-fold>
                                                                        } else {
                                                                            strIsPushNotiApprove = "1";
                                                                        }
                                                                        //</editor-fold>
                                                                    }
                                                                    if(isProcess == true) {
                                                                        sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                        strView = "0#0#" + strIsPushNotiApprove;
                                                                    } else {
                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                    }
                                                                } else {
                                                                    strView = sParam + "#0";
                                                                }
                                                            }
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_CSR_EXISTS + "#0#0";
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_CSR_KEYSIZE + "#0#0";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_CSR_NULL + "#0#0";
                                                }
                                            } else {
                                                //<editor-fold defaultstate="collapsed" desc="### RSSP PROCESS">
                                                boolean isAccessRssp = true;
                                                if (!"".equals(sVALUE_OLD)) {
                                                    objectMapper = new ObjectMapper();
                                                    ATTRIBUTE_VALUES valueRSSP = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                    if(EscapeUtils.CheckTextNull(valueRSSP.getRsspConnectWSMode()).equals(Definitions.CONFIG_RSSP_CONNECT_MODE_REST)) {
                                                        isAccessRssp = false;
                                                    }
                                                }
                                                if(isAccessRssp == true) {
                                                    PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                                    db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                                    String sFormFactorPro = "";
                                                    if(rsFormfactorPro[0].length > 0) {
                                                        sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                                    }
                                                    CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                                    if(credentialAuthen != null) {
                                                        RSSPProcessCommon clsRSSP = new RSSPProcessCommon();
                                                        AgreementDetails agreementDetails = new AgreementDetails();
                                                        String sAgreementUUID = "";
                                                        String sRelyingPartyRSSP = "";
                                                        String sCertProfileCode = "";
                                                        String sCertProfileProperties = "";
                                                        //<editor-fold defaultstate="collapsed" desc="### PROPERTIES SAN CERT">
                                                        String strEmailSan = "";
                                                        if(!"".equals(pCOMPONENT_SAN)) {
                                                            CommonFunction.LogDebugString(log, "pCOMPONENT_SAN", pCOMPONENT_SAN);
                                                            String[] pCompSanSub = pCOMPONENT_SAN.split("@@@");
                                                            for (String sPlitValue : pCompSanSub) {
                                                                if(!"".equals(sPlitValue)) {
                                                                    if(sPlitValue.split("###")[0].trim().equals(Definitions.CONFIG_COMPONENT_SAN_TAG_rfc822Name))
                                                                    {
                                                                        strEmailSan = sPlitValue.split("###")[1];
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        //</editor-fold>

                                                        String pPROVINCE_CODE = "N/A";
                                                        //<editor-fold defaultstate="collapsed" desc="### GET CITY_PROVIN_CODE">
                                                        if(!"".equals(pPROVINCE_ID)) {
                                                            CITY_PROVINCE[][] rsProvince = new CITY_PROVINCE[1][];
                                                            db.S_BO_PROVINCE_DETAIL(pPROVINCE_ID, rsProvince);
                                                            if(rsProvince[0].length > 0) {
                                                                pPROVINCE_CODE = EscapeUtils.CheckTextNull(rsProvince[0][0].NAME);
                                                            }
                                                        }
                                                        //</editor-fold>

                                                        // get CertProfileID
                                                        CERTIFICATION_PROFILE[][] rsProfile;
                                                        rsProfile = new CERTIFICATION_PROFILE[1][];
                                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                        if(rsProfile[0].length > 0) {
                                                            sCertProfileCode = EscapeUtils.CheckTextNull(rsProfile[0][0].NAME);
                                                            sCertProfileProperties = EscapeUtils.CheckTextNull(rsProfile[0][0].PROPERTIES);
                                                        }
                                                        int serverOUCount = 0;
                                                        CERTIFICATION_TYPE_COMPONENT[][] resProfileData = new CERTIFICATION_TYPE_COMPONENT[1][];
                                                        CommonFunction.getJsonComponentForCert(sCertProfileProperties, resProfileData);
                                                        for(CERTIFICATION_TYPE_COMPONENT resProfileData1 : resProfileData[0])
                                                        {
                                                            if(EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID))
                                                            {
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE+":")) {
                                                                    agreementDetails.setTaxID(pTAX_CODE);
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE+":")) {
                                                                    agreementDetails.setBudgetID(pBUDGET_CODE);
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_PERSONAL_CODE+":")) {
                                                                    agreementDetails.setPersonalID(pP_ID);
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE+":")) {
                                                                    agreementDetails.setPassportID(pPASSPORT);
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_PERSONAL_CITIZEN_CODE+":")) {
                                                                    agreementDetails.setCitizenID(pCCCD);
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_SOCIAL_INSURANCE_CODE+":")) {
                                                                    if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                                                        || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE) {
                                                                        if(pENTERPRISE_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE)){
                                                                            agreementDetails.setSocialInsuranceNumber(pENTERPRISE_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE, ""));
                                                                        }
                                                                    } else {
                                                                        if(pPERSONAL_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE)){
                                                                            agreementDetails.setSocialInsuranceNumber(pPERSONAL_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_SOCIAL_INSURANCE_CODE, ""));
                                                                        }
                                                                    }
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_DECISION+":")) {
                                                                    if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                                                        || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE) {
                                                                        if(pENTERPRISE_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION)){
                                                                            agreementDetails.setDecisionNumber(pENTERPRISE_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION, ""));
                                                                        }
                                                                    } else {
                                                                        if(pPERSONAL_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION)){
                                                                            agreementDetails.setDecisionNumber(pPERSONAL_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION, ""));
                                                                        }
                                                                    }
                                                                }
                                                                if(EscapeUtils.CheckTextNull(resProfileData1.prefix).equals(Definitions.CONFIG_PREFIX_ENTERPRISE_UNIT_CODE+":")) {
                                                                    if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                                                        || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE) {
                                                                        if(pENTERPRISE_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE)){
                                                                            agreementDetails.setUnitCode(pENTERPRISE_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE, ""));
                                                                        }
                                                                    } else {
                                                                        if(pPERSONAL_ID.contains(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE)){
                                                                            agreementDetails.setUnitCode(pPERSONAL_ID.replace(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_UNIT_CODE, ""));
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_T)) {
                                                                    agreementDetails.setTitle(CommonFunction.getRoleInDN(DN).trim());
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_C)) {
                                                                    agreementDetails.setCountry(CommonFunction.getCountryInDN(DN).trim());
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST)) {
                                                                    agreementDetails.setStateOrProvince(pPROVINCE_CODE);
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_E)) {
                                                                    agreementDetails.setEmail(CommonFunction.getEmailInDN(DN).trim());
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber)) {
                                                                    agreementDetails.setTelephoneNumber(CommonFunction.getPhoneInDN(DN).trim());
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_L)) {
                                                                    String sLocation = CommonFunction.getLocationInDN(DN).trim();
                                                                    agreementDetails.setLocation(sLocation);
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_O)) {
                                                                    if(EscapeUtils.CheckTextNull(resProfileData1.commomNameType).equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_PERSON)) {
                                                                        agreementDetails.setOrganization(pCOMPANY_NAME);
                                                                    } else {
                                                                        String sOrganzation = CommonFunction.getORGANIZATIONInDN(DN).trim();
                                                                        agreementDetails.setOrganization(sOrganzation);
                                                                    }
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN)) {
                                                                    if(EscapeUtils.CheckTextNull(resProfileData1.commomNameType).equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_COMPANY))
                                                                    {
                                                                        agreementDetails.setPersonalName(pCOMPANY_NAME);
                                                                    } else if(EscapeUtils.CheckTextNull(resProfileData1.commomNameType).equals(Definitions.CONFIG_COMPONENT_DN_COMMONNAME_PERSON))
                                                                    {
                                                                        agreementDetails.setPersonalName(pPERSONAL_NAME);
                                                                    }
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU)) {
                                                                    serverOUCount = serverOUCount + 1;
//                                                                    agreementDetails.setOrganizationUnit(CommonFunction.getDepartmentInDN(DN).trim());
                                                                }
                                                                if (EscapeUtils.CheckTextNull(resProfileData1.name).equals(Definitions.CONFIG_COMPONENT_DN_TAG_C)) {
                                                                    agreementDetails.setCountry(CommonFunction.getCountryInDN(DN).trim());
                                                                }
                                                            }
                                                        }
                                                        // add UO to rssp
                                                        String storeOU = EscapeUtils.CheckTextNull(request.getParameter("storeOU"));
                                                        CommonReferServlet.addComponentOURSSP(agreementDetails, serverOUCount, storeOU);
                                                        String[] sResultRSSP = new String[3];
                                                        if (!"".equals(sVALUE_OLD)) {
                                                            objectMapper = new ObjectMapper();
                                                            ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                            sAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                            sRelyingPartyRSSP = valueATTR_Frist.getRsspRelyingParty();
                                                        }
                                                        if(!"".equals(sAgreementUUID) && !"".equals(sRelyingPartyRSSP)) {
                                                            if("1".equals(keepCertSNEnabled)) {
                                                                CheckREVOKE_ENABLED = "0";
                                                            }
                                                            boolean boRevokeOldCertificate = "1".equals(CheckREVOKE_ENABLED);
                                                            boolean booKeepCertSNEnabled = "1".equals(keepCertSNEnabled);
                                                            clsRSSP.prepareChangeCertificateForSignCloud(sAgreementUUID, boRevokeOldCertificate, agreementDetails,
                                                                pRSSP_CERT_SN, sRelyingPartyRSSP, booKeepCertSNEnabled, sResultRSSP, credentialAuthen);
                                                            if("0".equals(sResultRSSP[0])) {
                                                                objectMapper = new ObjectMapper();
                                                                int pCERTIFICATION_PURPOSE_ID = 0;
                                                                String sCertificateID = sResultRSSP[2];
                                                                if(!"".equals(sCertificateID) && !"0".equals(sCertificateID)) {
                                                                    String isProcessText = "";
                                                                    String sVALUE = "";
                                                                    CERTIFICATION[][] rsCert;
                                                                    rsCert = new CERTIFICATION[1][];
                                                                    db.S_BO_CERTIFICATION_DETAIL(sCertificateID, sessLanguage, rsCert);
                                                                    if(rsCert[0].length > 0) {
                                                                        sVALUE = rsCert[0][0].VALUE;
                                                                        pCERTIFICATION_PURPOSE_ID = rsCert[0][0].CERTIFICATION_PURPOSE_ID;
                                                                        pCERTIFICATION_OWNER_ID = rsCert[0][0].CERTIFICATION_OWNER_ID;
                                                                        pCERTIFICATE_ATTR_ID[0] = rsCert[0][0].CERTIFICATION_ATTR_ID;
                                                                    }
                                                                    //<editor-fold defaultstate="collapsed" desc="### FILE MANAGER INSERT">
                                                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                    String sJRBConfig = "";
                                                                    if (sessGeneralPolicy[0].length > 0) {
                                                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0]) {
                                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT)) {
                                                                                sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                                    int[] intResult = new int[1];
                                                                    ServletUptoFunction.fileManagerInsert(sJRBConfig, cartToken, sCertificateID,
                                                                        String.valueOf(pCERTIFICATION_OWNER_ID), loginUID, log, false, true, intResult);
                                                                    if(intResult[0] == 0){
                                                                        request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                                    }
                                                                    //</editor-fold>
                                                                    
                                                                    ATTRIBUTE_VALUES valueATTR = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                                                                    if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                        boolean isCAApprove = false;
                                                                        if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                                        {
                                                                            isCAApprove = true;
                                                                        } else {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                                isCAApprove = true;
                                                                            }
                                                                        }
                                                                        if(isCAApprove == true)
                                                                        {
                                                                            String sChangeKeyApprove = valueATTR.getChangeKeyEnabled() ? "1" : "0";
                                                                            sResultRSSP = new String[2];
                                                                            clsRSSP.approveCertificateForSignCloud(sAgreementUUID, sCertificateID, CheckREVOKE_ENABLED, sRelyingPartyRSSP, sResultRSSP,
                                                                                pRSSP_CERT_SN, sChangeKeyApprove, Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_CHANGE_CERT, "", credentialAuthen, false);
                                                                            if(!"0".equals(sResultRSSP[0]))
                                                                            {
                                                                                isProcess = false;
                                                                                isProcessText = sResultRSSP[1];
                                                                            }
                                                                        } else {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                        }
                                                                    } else {
                                                                        boolean checkCallApproveCA = false;
                                                                        String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                                        if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                                        {
                                                                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                if("0".equals(paramPre)) {
                                                                                    checkCallApproveCA = true;
                                                                                }
                                                                            } else {
                                                                                int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                if(rsUserApprve[0].length > 0) {
                                                                                    for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                        if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                            approveChilrenID = item.ID;
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                                                if("0".equals(paramAgency)) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                    {
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                        if("0".equals(paramPre)) {
                                                                                            checkCallApproveCA = true;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                {
                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                    valueATTR.setApproveDt(new Date());
                                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                    if("0".equals(paramPre)) {
                                                                                        checkCallApproveCA = true;
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                                if(intApprove == 1) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                    {
                                                                                        int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                        BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                        db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                        if(rsUserApprve[0].length > 0) {
                                                                                            for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                                if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                                    approveChilrenID = item.ID;
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                                                        if("0".equals(paramAgency)) {
                                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                            {
                                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                                valueATTR.setApproveDt(new Date());
                                                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                                                if("0".equals(paramPre)) {
                                                                                                    checkCallApproveCA = true;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        if(checkCallApproveCA == true) {
                                                                            //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                            boolean autoApproveCA = false;
                                                                            rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                            db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                            CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                            if(rsProfile[0].length > 0) {
                                                                                autoApproveCA = CommonFunction.getApproveCAOfProfile(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME), sessPolicyCert_Data);
                                                                            }
                                                                            if(autoApproveCA == true) {
                                                                                if(CommonFunction.checkApproveCAReqType(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION, sessPolicyCert_Data) == true)
                                                                                {
                                                                                    String sChangeKeyApprove = valueATTR.getChangeKeyEnabled() ? "1" : "0";
                                                                                    sResultRSSP = new String[2];
                                                                                    clsRSSP.approveCertificateForSignCloud(sAgreementUUID, sCertificateID, CheckREVOKE_ENABLED, sRelyingPartyRSSP,
                                                                                        sResultRSSP, pRSSP_CERT_SN, sChangeKeyApprove, Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_CHANGE_CERT,
                                                                                        "", credentialAuthen, false);
                                                                                    if(!"0".equals(sResultRSSP[0])) {
                                                                                        isProcess = false;
                                                                                        isProcessText = sResultRSSP[1];
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                strIsPushNotiApprove = "1";
                                                                            }
                                                                            //</editor-fold>
                                                                        } else {
                                                                            strIsPushNotiApprove = "1";
                                                                        }
                                                                    }
                                                                    if(isProcess == true)
                                                                    {
                                                                        sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_SSL
                                                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING
                                                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                                            strView = "0#" + sCertificateID + "#0#" + strIsPushNotiApprove;
                                                                        } else {
                                                                            strView = "0#" + sCertificateID + "#1#" + strIsPushNotiApprove;
                                                                        }
                                                                    } else {
                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#" + isProcessText;
                                                                    }
                                                                } else {
                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#Error: The certificate information returned from eSignCloud is invalid";
                                                                }
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#Error: " + sResultRSSP[0] + " - " + sResultRSSP[1];
                                                            }
                                                        } else {
                                                            CommonFunction.LogErrorServlet(log, "Change Certificate Info -> eSignCloud: There does not exist the value of Agreement UUID connecting to eSignCloud.");
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#There does not exist the value of Agreement UUID connecting to eSignCloud.";
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM + "#0";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0#0";
                                                    CommonFunction.LogErrorServlet(log, "RSSP REST not supported yet.");
                                                }
                                                //</editor-fold>
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "CertChangeInfo: Cert ID or Profile ID cannot be empty");
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "reissuecert": {
                                //<editor-fold defaultstate="collapsed" desc="reissuecert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sTOKEN_SN = "";// EscapeUtils.CheckTextNull(request.getParameter("pNEW_TOKEN_SN"));
                                    String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String CheckCHANGE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckCHANGE_KEY"));
                                    String CheckPRIVATE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckPRIVATE_KEY"));
                                    String CheckREVOKE_ENABLED = EscapeUtils.CheckTextNull(request.getParameter("CheckREVOKE_ENABLED"));
                                    String pCERTIFICATION_PROFILE_ID = EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                    if(!"".equals(sID) && !"".equals(pCERTIFICATION_PROFILE_ID)) {
                                        //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                        // check agency
                                        boolean isAccessAgency = true;
                                        String strIsPushNotiApprove = "0";
                                        String PHONE_CONTRACT = "";
                                        String EMAIL_CONTRACT = "";
                                        String pCERTIFICATION_SN = "";
                                        String pPERSONAL_NAME =EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_NAME"));
                                        String pCOMPANY_NAME = EscapeUtils.CheckTextNull(request.getParameter("pCOMPANY_NAME"));
                                        String pDOMAIN_NAME = EscapeUtils.CheckTextNull(request.getParameter("pDOMAIN_NAME"));
                                        String pTAX_CODE = EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                        String pDECISION =EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                        String pBUDGET_CODE = EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                        String pP_ID = EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                        String pCCCD = EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                        String pPASSPORT = EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                        String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                        String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                        String pSUBJECT = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                        String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
                                        String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                        String pPROVINCE_DESC = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_DESC"));
                                        String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                        String sPRIVATE_KEY = "";
                                        String sOLD_TOKEN_ID = "";
                                        String pISSUER_SUBJECT = "";
                                        String sAGENT_ID = "";
                                        String sAGENT_ID_OLD = "";
                                        String pCREATE_USER = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                        String pDISCOUNT_RATE = "";
                                        String pEFFECTIVE_DT = "";
                                        int pPKI_FORMFACTOR_ID = 0;
                                        int pCERTIFICATION_OWNER_ID = 0;
                                        int pCERTIFICATION_PURPOSE_ID_INNER = 0;
                                        CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(sID, sessLanguage, rsReq);
                                        if (rsReq[0].length > 0) {
                                            sOLD_TOKEN_ID = String.valueOf(rsReq[0][0].TOKEN_ID);
                                            PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                            EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                            pDISCOUNT_RATE = String.valueOf(rsReq[0][0].DISCOUNT_RATE);
                                            pISSUER_SUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                            pEFFECTIVE_DT = EscapeUtils.CheckTextNull(rsReq[0][0].EFFECTIVE_DT);
                                            sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            sAGENT_ID_OLD = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                            pCERTIFICATION_PURPOSE_ID_INNER = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                            pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                            if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                            } else {
                                                sAGENT_ID = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ID"));
                                            }
                                        } else {
                                            isAccessAgency = false;
                                        }
                                        //</editor-fold>

                                        if (isAccessAgency == true) {
                                            sTOKEN_SN = Definitions.CONFIG_TOKEN_UNASSIGN_SN;
                                            String sTOKEN_ID_NEW = String.valueOf(Definitions.CONFIG_TOKEN_UNASSIGN_ID);
                                            String pPAST_CERTIFICATE_ID = sID;
                                            String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE);
                                            String pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                            if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                                || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL) {
                                                pDEVICE = "";
                                            }
                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                            tempLogReq = new CERTIFICATION_DATA_ATTR();
                                            objectMapper = new ObjectMapper();
                                            tempLogReq.personalName = pPERSONAL_NAME;
                                            tempLogReq.companyName = pCOMPANY_NAME;
                                            tempLogReq.enterpriseID = pENTERPRISE_ID;
                                            tempLogReq.personalID = pPERSONAL_ID;
                                            tempLogReq.deviceUUID = pDEVICE;
                                            tempLogReq.emailContract = EMAIL_CONTRACT;
                                            tempLogReq.phoneContract = PHONE_CONTRACT;
                                            tempLogReq.issuerSubject = pISSUER_SUBJECT;
                                            tempLogReq.subjectDn = pSUBJECT;
                                            tempLogReq.tokenSn = sTOKEN_SN;
                                            tempLogReq.pkiFromFactorId = pPKI_FORMFACTOR_ID;
                                            tempLogReq.typeName = pCERT_ATTR_TYPE_CODE;
                                            String strReq = objectMapper.writeValueAsString(tempLogReq);
                                            db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                    Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                    Definitions.CONFIG_LOG_FUNCTIONALITY_REQUEST_REISSUE, strReq,
                                                    loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                            CommonFunction.LogDebugString(log, "ReissueCertificate", "SUBJECT: " + pSUBJECT
                                                    + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME
                                                    + "; pENTERPRISE_ID: " + pENTERPRISE_ID + "; sAGENT_ID: " + sAGENT_ID + "; pCREATE_USER: " + pCREATE_USER
                                                    + "; pPKI_FORMFACTOR_ID: " + pPKI_FORMFACTOR_ID
                                                    + "; pPERSONAL_ID: " + pPERSONAL_ID + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                    + "; pCERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID
                                                    + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT + "; pPROVINCE_DESC: " + pPROVINCE_DESC
                                                    + "; ISSUER_SUBJECT: " + pISSUER_SUBJECT + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                    + "; TOKEN_ID_NEW: " + sTOKEN_ID_NEW + "; TOKEN_SN_NEW: " + sTOKEN_SN + "; CITY_PROVINCE_ID: " + pPROVINCE_ID
                                                    + "; CheckCHANGE_KEY: " + CheckCHANGE_KEY + "; CheckREVOKE_ENABLED: " + CheckREVOKE_ENABLED);
                                            ATTRIBUTE_VALUES valueATTR;
                                            ATTRIBUTE_VALUES valueATTR_TOKEN;
                                            ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                            dataATTR.setCertificationData(tempLogReq);
                                            valueATTR = new ATTRIBUTE_VALUES();
                                            valueATTR.setTokenSn(sTOKEN_SN);
                                            boolean sChangeKeyEnabled = "1".equals(CheckCHANGE_KEY);
                                            valueATTR.setChangeKeyEnabled(sChangeKeyEnabled);
                                            boolean sRevokedEnabled = "1".equals(CheckREVOKE_ENABLED);
                                            valueATTR.setRevokeOldCertificateEnabled(sRevokedEnabled);
                                            if(sChangeKeyEnabled == false) {
                                                valueATTR.setKeepCertificateSNEnabled(true);
                                            } else {
                                                valueATTR.setKeepCertificateSNEnabled(false);
                                            }
                                            valueATTR.setTypeName(pCERT_ATTR_TYPE_CODE);
                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                            valueATTR.setCreateUser(loginFullname + " (" + loginUID + ")");
                                            valueATTR.setCreateDt(new Date());
                                            valueATTR.setAttributeData(dataATTR);
                                            //</editor-fold>

                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                            int intOTPNumn = 8;
                                            String checkChangeEffectiveDT = "0";
                                            GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                            if (sessGeneralPolicy[0].length > 0) {
                                                for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_ACTIVATION_CODE_LENGTH)) {
                                                        intOTPNumn = Integer.parseInt(rsPolicy1.VALUE);
                                                    }
                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_OPTION_FOR_CHANGING_EFFECTIVE_DT)) {
                                                        checkChangeEffectiveDT = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                    }
                                                }
                                            }
                                            java.sql.Timestamp pEFFECTIVE_DT_REQ = null;
                                            /*if("1".equals(checkChangeEffectiveDT)) {
                                                if(!"".equals(pEFFECTIVE_DT)) {
                                                    pEFFECTIVE_DT_REQ = CommonFunction.ConvertStringToTimeStamp(pEFFECTIVE_DT);
                                                }
                                            }*/
                                            String strDNSName = "";
                                            String sParam = "1000";
                                            while ("1000".equals(sParam)) {
                                                try {
                                                    String sOTP = CommonFunction.getRandomOTP(intOTPNumn);
                                                    sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(sTOKEN_ID_NEW), pCERTIFICATION_PROFILE_ID, sTOKEN_SN,
                                                        pCERTIFICATION_SN, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME, pTAX_CODE, pBUDGET_CODE, pP_ID,
                                                        pPASSPORT, pSUBJECT, pISSUER_SUBJECT, PHONE_CONTRACT, EMAIL_CONTRACT, sAGENT_ID,
                                                        pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, sOTP,
                                                        strReqValueATTR, pCREATE_USER, loginUID, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID,
                                                        "", CheckPRIVATE_KEY, CheckCHANGE_KEY, sPRIVATE_KEY, pPKI_FORMFACTOR_ID, pDEVICE,
                                                        String.valueOf(pCERTIFICATION_OWNER_ID), pCCCD, pEFFECTIVE_DT_REQ, pDECISION, pPERSONAL_ID, pENTERPRISE_ID);
                                                } catch (Exception e) {
                                                    if (e.getMessage().contains(Definitions.CONFIG_MYSQL_UNIQUE_ACTIVATION_CODE)) {
                                                        sParam = "1000";
                                                    } else {
                                                        sParam = Definitions.CONFIG_EXCEPTION_STRING_ERROR;
                                                        CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
                                                    }
                                                }
                                            }
                                            if ("0".equals(sParam)) {

                                                //<editor-fold defaultstate="collapsed" desc="### SHARED_MODE CERT">
                                                boolean pSHARED_MODE_ENABLED = false;
                                                BRANCH[][] rsBranchShare = new BRANCH[1][];
                                                db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranchShare);
                                                if(rsBranchShare[0].length > 0) {
                                                    String sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranchShare[0][0].CERTIFICATION_POLICY_PROPERTIES);
                                                    if(!"".equals(sCERT_POLICY_PROPERTIES)) {
                                                        pSHARED_MODE_ENABLED = CommonFunction.getShareModeEnabledCert(sCERT_POLICY_PROPERTIES);
                                                    }
                                                }
                                                String pSHARED_MODE_UPDATE = pSHARED_MODE_ENABLED ? "1" : "0";
                                                db.S_BO_CERTIFICATION_UPDATE(pCERTIFICATE_ID[0], pCERTIFICATION_PROFILE_ID, "", "", "",
                                                    pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, "", "", "", "", "",
                                                    "", loginUID, "", "", "", pDISCOUNT_RATE, pSHARED_MODE_UPDATE, pCCCD,
                                                    pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                //</editor-fold>

                                                //<editor-fold defaultstate="collapsed" desc="### PROPERTIES SAN CERT">
                                                if(!"".equals(pCOMPONENT_SAN)) {
                                                    String[] pCompSanSub = pCOMPONENT_SAN.split("@@@");
                                                    List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes = new ArrayList<>();
                                                    for (String sPlitValue : pCompSanSub) {
                                                        if(!"".equals(sPlitValue)) {
                                                            String sKey = "";
                                                            if(!"".equals(sPlitValue.split("###")[0].trim())) {
                                                                    sKey = sPlitValue.split("###")[0].trim();
                                                            }
                                                            objectMapper = new ObjectMapper();
                                                            CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                            attribute.setKey(sKey);
                                                            attribute.setValue(sPlitValue.split("###")[1]);
                                                            attributes.add(attribute);
                                                        }
                                                    }
                                                    if(attributes.size() > 0) {
                                                        strDNSName = "{\"attributes\":" + objectMapper.writeValueAsString(attributes) + "}";
                                                        db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(pCERTIFICATE_ID[0]), strDNSName, loginUID);
                                                    }
                                                }
                                                //</editor-fold>

                                                //<editor-fold defaultstate="collapsed" desc="### FILE MANAGER INSERT">
                                                String sJRBConfig = "";
                                                if (sessGeneralPolicy[0].length > 0) {
                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                    {
                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT))
                                                        {
                                                            sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                            break;
                                                        }
                                                    }
                                                }
                                                SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                int[] intResult = new int[1];
                                                boolean isAutoSendFileToNew = false;
                                                Config conf =new Config();
                                                if("1".equals(LoadParamSystem.getParamStart(Definitions.CONFIG_FILE_FILE_UP_OLD_TO_NEW_AUTO_ENABLED))) {
                                                    isAutoSendFileToNew = true;
                                                }
                                                ServletUptoFunction.fileManagerInsert(sJRBConfig, cartToken, String.valueOf(pCERTIFICATE_ID[0]),
                                                    String.valueOf(pCERTIFICATION_OWNER_ID), loginUID, log, isAutoSendFileToNew, true, intResult);
                                                if(intResult[0] == 0){
                                                    request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                }
                                                //</editor-fold>

                                                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                    //<editor-fold defaultstate="collapsed" desc="### AGENCY ROOT LOGIN">
                                                    if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                    {
                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                        valueATTR.setApproveCADt(new Date());
                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                        if ("0".equals(sApprove)) {
                                                            // send otp
                                                            boolean sPUSH_NOTICE_ENABLED = false;
                                                            int sCERTIFICATION_ID = 0;
                                                            CERTIFICATION[][] rsCertOTP = new CERTIFICATION[1][];
                                                            db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sessLanguage, rsCertOTP);
                                                            if (rsReq[0].length > 0) {
                                                                sCERTIFICATION_ID = rsCertOTP[0][0].ID;
                                                                sPUSH_NOTICE_ENABLED = rsCertOTP[0][0].PUSH_NOTICE_ENABLED;
                                                            }
                                                            if (sPUSH_NOTICE_ENABLED == true) {
                                                                int[] intRes = new int[1];
                                                                String[] sRes = new String[1];
                                                                ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                // RA SEND_EMAIL
                                                            }
                                                            // check token old
                                                            TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                            int sTOKEN_STATE_ID_OLD = 0;
                                                            String sTOKEN_SN_OLD = "";
                                                            db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsToken_OLD);
                                                            if (rsToken_OLD[0].length > 0) {
                                                                sTOKEN_STATE_ID_OLD = rsToken_OLD[0][0].TOKEN_STATE_ID;
                                                                sTOKEN_SN_OLD = EscapeUtils.CheckTextNull(rsToken_OLD[0][0].TOKEN_SN);
                                                            }
                                                            if (sTOKEN_STATE_ID_OLD != Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                                                // insert toekn attr
                                                                int[] System_Log_ID_Token = new int[1];
                                                                System_Log_ID_Token[0] = 0;
                                                                TOKEN_CHANGE_LOG tempLogReqToken = new TOKEN_CHANGE_LOG();
                                                                tempLogReqToken.setTOKEN_SN(sTOKEN_SN_OLD);
                                                                tempLogReqToken.setIS_LOST("True");
                                                                String strTokenReq = CommonFunction.GenJSONTokenLog(tempLogReqToken);
                                                                db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                                        Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN_OLD, "",
                                                                        Definitions.CONFIG_LOG_FUNCTIONALITY_PERMANENT_INITIALZED, strTokenReq,
                                                                        loginUID, System_Log_ID_Token, sIP_Request, sysLog_BillCode);
                                                                // VALUE ATTR
                                                                valueATTR_TOKEN = new ATTRIBUTE_VALUES();
                                                                valueATTR_TOKEN.setTokenSn(sTOKEN_SN_OLD);
                                                                valueATTR_TOKEN.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PERMANENT_INITIALZED);
                                                                valueATTR_TOKEN.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                                valueATTR_TOKEN.setCreateUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR_TOKEN.setCreateDt(new Date());
                                                                valueATTR_TOKEN.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR_TOKEN.setApproveDt(new Date());
                                                                String strReqTokenATTR = CommonFunction.GenJSONTokenATTR(valueATTR_TOKEN);
                                                                // VALUE ATTR
                                                                int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                                db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sOLD_TOKEN_ID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PERMANENT_INITIALZED,
                                                                        intTOKEN_ATTR_STATE, strReqTokenATTR, loginUID);
                                                                db.S_BO_TOKEN_UPDATE(Integer.parseInt(sOLD_TOKEN_ID),
                                                                        String.valueOf(Definitions.CONFIG_TOKEN_STATE_ID_LOST), "", "", "", "", loginUID);
                                                                if (System_Log_ID_Token[0] != 0) {
                                                                    RESPONSE_CODE[][] rsResponse = new RESPONSE_CODE[1][];
                                                                    db.S_BO_RESPONSE_CODE_DETAIL(String.valueOf(Definitions.CONFIG_RESPONSE_CODE_ID_SUCCESS), rsResponse);
                                                                    RESPONSE_LOG tempLogRes = new RESPONSE_LOG();
                                                                    tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                                                    tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                                                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID_Token[0], rsResponse[0][0].NAME, objectMapper.writeValueAsString(tempLogRes), "", loginUID);
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,
                                                            Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveCADt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                            if("0".equals(sApprove))
                                                            {
                                                                // send otp
                                                                boolean sPUSH_NOTICE_ENABLED = false;
                                                                int sCERTIFICATION_ID = 0;
                                                                CERTIFICATION[][] rsCertOTP = new CERTIFICATION[1][];
                                                                db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sessLanguage, rsCertOTP);
                                                                if (rsReq[0].length > 0) {
                                                                    sCERTIFICATION_ID = rsCertOTP[0][0].ID;
                                                                    sPUSH_NOTICE_ENABLED = rsCertOTP[0][0].PUSH_NOTICE_ENABLED;
                                                                }
                                                                if (sPUSH_NOTICE_ENABLED == true) {
                                                                    int[] intRes = new int[1];
                                                                    String[] sRes = new String[1];
                                                                    ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                    // RA SEND_EMAIL
                                                                }
                                                                // check token old
                                                                TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                                int sTOKEN_STATE_ID_OLD = 0;
                                                                String sTOKEN_SN_OLD = "";
                                                                db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsToken_OLD);
                                                                if (rsToken_OLD[0].length > 0) {
                                                                    sTOKEN_STATE_ID_OLD = rsToken_OLD[0][0].TOKEN_STATE_ID;
                                                                    sTOKEN_SN_OLD = EscapeUtils.CheckTextNull(rsToken_OLD[0][0].TOKEN_SN);
                                                                }
                                                                if (sTOKEN_STATE_ID_OLD != Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                                                    // insert toekn attr
                                                                    int[] System_Log_ID_Token = new int[1];
                                                                    System_Log_ID_Token[0] = 0;
                                                                    TOKEN_CHANGE_LOG tempLogReqToken = new TOKEN_CHANGE_LOG();
                                                                    tempLogReqToken.setTOKEN_SN(sTOKEN_SN_OLD);
                                                                    tempLogReqToken.setIS_LOST("True");
                                                                    String strTokenReq = CommonFunction.GenJSONTokenLog(tempLogReqToken);
                                                                    db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                                            Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN_OLD, "",
                                                                            Definitions.CONFIG_LOG_FUNCTIONALITY_PERMANENT_INITIALZED, strTokenReq,
                                                                            loginUID, System_Log_ID_Token, sIP_Request, sysLog_BillCode);
                                                                    // VALUE ATTR
                                                                    valueATTR_TOKEN = new ATTRIBUTE_VALUES();
                                                                    valueATTR_TOKEN.setTokenSn(sTOKEN_SN_OLD);
                                                                    valueATTR_TOKEN.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PERMANENT_INITIALZED);
                                                                    valueATTR_TOKEN.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                                    valueATTR_TOKEN.setCreateUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR_TOKEN.setCreateDt(new Date());
                                                                    valueATTR_TOKEN.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR_TOKEN.setApproveDt(new Date());
                                                                    String strReqTokenATTR = CommonFunction.GenJSONTokenATTR(valueATTR_TOKEN);
                                                                    // VALUE ATTR
                                                                    int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                                    db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sOLD_TOKEN_ID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PERMANENT_INITIALZED,
                                                                            intTOKEN_ATTR_STATE, strReqTokenATTR, loginUID);
                                                                    db.S_BO_TOKEN_UPDATE(Integer.parseInt(sOLD_TOKEN_ID),
                                                                            String.valueOf(Definitions.CONFIG_TOKEN_STATE_ID_LOST), "", "", "", "", loginUID);
                                                                    if (System_Log_ID_Token[0] != 0) {
                                                                        RESPONSE_CODE[][] rsResponse = new RESPONSE_CODE[1][];
                                                                        db.S_BO_RESPONSE_CODE_DETAIL(String.valueOf(Definitions.CONFIG_RESPONSE_CODE_ID_SUCCESS), rsResponse);
                                                                        RESPONSE_LOG tempLogRes = new RESPONSE_LOG();
                                                                        tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                                                        tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                                                        db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID_Token[0], rsResponse[0][0].NAME, objectMapper.writeValueAsString(tempLogRes), "", loginUID);
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                        }
                                                    }
                                                    //</editor-fold>
                                                } else {
                                                    //<editor-fold defaultstate="collapsed" desc="### AGENCY USER LOGIN">
                                                    boolean checkCallApproveCA = false;
                                                    String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                    objectMapper = new ObjectMapper();
                                                    if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                    {
                                                        if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                            if("0".equals(paramPre)) {
                                                                checkCallApproveCA = true;
                                                            }
                                                        } else {
                                                            int approveChilrenID = Integer.parseInt(userLoginID);
                                                            BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                            db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                            if(rsUserApprve[0].length > 0) {
                                                                for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                    if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                        approveChilrenID = item.ID;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID, approveChilrenID);
                                                            if("0".equals(paramAgency)) {
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                {
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                    if("0".equals(paramPre)) {
                                                                        checkCallApproveCA = true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                            {
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                if("0".equals(paramPre)) {
                                                                    checkCallApproveCA = true;
                                                                }
                                                            }
                                                        } else {
                                                            int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                            if(intApprove == 1) {
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                {
                                                                    int approveChilrenID = Integer.parseInt(userLoginID);
                                                                    BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                    db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                    if(rsUserApprve[0].length > 0) {
                                                                        for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                            if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                approveChilrenID = item.ID;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID, approveChilrenID);
                                                                    if("0".equals(paramAgency)) {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                            if("0".equals(paramPre)) {
                                                                                checkCallApproveCA = true;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if(checkCallApproveCA == true){
                                                        //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                        boolean autoApproveCA = false;
                                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(pCERTIFICATION_PROFILE_ID, rsProfile);
                                                        CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                        objectMapper = new ObjectMapper();
                                                        if(rsProfile[0].length > 0) {
                                                            autoApproveCA = CommonFunction.getApproveCAOfProfile(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME), sessPolicyCert_Data);
                                                        }
                                                        if(autoApproveCA == true) {
                                                            if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                            {
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveCADt(new Date());
                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                if ("0".equals(sApprove)) {
                                                                    boolean sPUSH_NOTICE_ENABLED = false;
                                                                    int sCERTIFICATION_ID = 0;
                                                                    CERTIFICATION[][] rsCertOTP = new CERTIFICATION[1][];
                                                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sessLanguage, rsCertOTP);
                                                                    if (rsReq[0].length > 0) {
                                                                        sCERTIFICATION_ID = rsCertOTP[0][0].ID;
                                                                        sPUSH_NOTICE_ENABLED = rsCertOTP[0][0].PUSH_NOTICE_ENABLED;
                                                                    }
                                                                    if (sPUSH_NOTICE_ENABLED == true) {
                                                                        int[] intRes = new int[1];
                                                                        String[] sRes = new String[1];
                                                                        ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                    }
                                                                    TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                                    int sTOKEN_STATE_ID_OLD = 0;
                                                                    String sTOKEN_SN_OLD = "";
                                                                    db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsToken_OLD);
                                                                    if (rsToken_OLD[0].length > 0) {
                                                                        sTOKEN_STATE_ID_OLD = rsToken_OLD[0][0].TOKEN_STATE_ID;
                                                                        sTOKEN_SN_OLD = EscapeUtils.CheckTextNull(rsToken_OLD[0][0].TOKEN_SN);
                                                                    }
                                                                    if (sTOKEN_STATE_ID_OLD != Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                                                        int[] System_Log_ID_Token = new int[1];
                                                                        System_Log_ID_Token[0] = 0;
                                                                        TOKEN_CHANGE_LOG tempLogReqToken = new TOKEN_CHANGE_LOG();
                                                                        tempLogReqToken.setTOKEN_SN(sTOKEN_SN_OLD);
                                                                        tempLogReqToken.setIS_LOST("True");
                                                                        String strTokenReq = CommonFunction.GenJSONTokenLog(tempLogReqToken);
                                                                        db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                                                Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN_OLD, "",
                                                                                Definitions.CONFIG_LOG_FUNCTIONALITY_PERMANENT_INITIALZED, strTokenReq,
                                                                                loginUID, System_Log_ID_Token, sIP_Request, sysLog_BillCode);
                                                                        // VALUE ATTR
                                                                        valueATTR_TOKEN = new ATTRIBUTE_VALUES();
                                                                        valueATTR_TOKEN.setTokenSn(sTOKEN_SN_OLD);
                                                                        valueATTR_TOKEN.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PERMANENT_INITIALZED);
                                                                        valueATTR_TOKEN.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                                        valueATTR_TOKEN.setCreateUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR_TOKEN.setCreateDt(new Date());
                                                                        valueATTR_TOKEN.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR_TOKEN.setApproveDt(new Date());
                                                                        String strReqTokenATTR = CommonFunction.GenJSONTokenATTR(valueATTR_TOKEN);
                                                                        // VALUE ATTR
                                                                        int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                                        db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sOLD_TOKEN_ID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PERMANENT_INITIALZED,
                                                                                intTOKEN_ATTR_STATE, strReqTokenATTR, loginUID);
                                                                        db.S_BO_TOKEN_UPDATE(Integer.parseInt(sOLD_TOKEN_ID),
                                                                                String.valueOf(Definitions.CONFIG_TOKEN_STATE_ID_LOST), "", "", "", "", loginUID);
                                                                        if (System_Log_ID_Token[0] != 0) {
                                                                            RESPONSE_CODE[][] rsResponse = new RESPONSE_CODE[1][];
                                                                            db.S_BO_RESPONSE_CODE_DETAIL(String.valueOf(Definitions.CONFIG_RESPONSE_CODE_ID_SUCCESS), rsResponse);
                                                                            RESPONSE_LOG tempLogRes = new RESPONSE_LOG();
                                                                            tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                                                            tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                                                            db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID_Token[0], rsResponse[0][0].NAME, objectMapper.writeValueAsString(tempLogRes), "", loginUID);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            strIsPushNotiApprove = "1";
                                                        }
                                                        //</editor-fold>
                                                    } else {
                                                        strIsPushNotiApprove = "1";
                                                    }
                                                    //</editor-fold>
                                                }
                                                sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                strView = "0#0#" + strIsPushNotiApprove;
                                            } else {
                                                strView = sParam + "#0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "CertReIssue: Cert ID or Profile ID cannot be empty");
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "suspendcert": {
                                //<editor-fold defaultstate="collapsed" desc="suspendcert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sCERTIFICATION_ATTR_TYPE_ID = EscapeUtils.CheckTextNull(request.getParameter("sCERTIFICATION_ATTR_TYPE"));
                                    String sSUSPEND_TIME = EscapeUtils.CheckTextNull(request.getParameter("sSUSPEND_TIME"));
                                    String sSUSPEND_REASON = EscapeUtils.CheckTextNull(request.getParameter("sSUSPEND_REASON"));
                                    String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    java.sql.Timestamp sSUSPEND_TIME_DB = null;
                                    if(!"".equals(sID)) {
                                        //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                        boolean isAccessAgency = true;
                                        String strIsPushNotiApprove = "0";
                                        String PHONE_CONTRACT = "";
                                        String EMAIL_CONTRACT = "";
                                        String pCERTIFICATION_SN = "";
                                        String pPERSONAL_NAME = "";
                                        String pCOMPANY_NAME = "";
                                        String pDOMAIN_NAME = "";
                                        String pENTERPRISE_ID = "";
                                        String pPERSONAL_ID = "";
                                        String pSUBJECT = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                        String sPRIVATE_KEY = "";
                                        String sOLD_TOKEN_ID = "";
                                        String pISSUER_SUBJECT = "";
                                        String pPROVINCE_ID = "";
                                        String pCERTIFICATION_PROFILE_ID = "";
                                        String pCERTIFICATION_AUTHORITY_ID = "";
                                        String sTOKEN_SN = "";
                                        String sAGENT_ID = "";
                                        String pCREATE_USER = "";
                                        String CheckPRIVATE_KEY = "";
                                        String CheckCHANGE_KEY = "";
                                        String sOTP = "";
                                        int pPKI_FORMFACTOR_ID = 0;
                                        int pCERTIFICATION_ID =0;
                                        int pCERTIFICATION_OWNER_ID =0;
                                        CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(sID, sessLanguage, rsReq);
                                        if (rsReq[0].length > 0) {
                                            pCERTIFICATION_ID = rsReq[0][0].ID;
                                            sOLD_TOKEN_ID = String.valueOf(rsReq[0][0].TOKEN_ID);
                                            sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                            PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                            EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                            pPERSONAL_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                            pCOMPANY_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                            pDOMAIN_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].DOMAIN_NAME);
                                            pENTERPRISE_ID = rsReq[0][0].ENTERPRISE_ID;
                                            pPERSONAL_ID = rsReq[0][0].PERSONAL_ID;
                                            pISSUER_SUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                            pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                            pCERTIFICATION_PROFILE_ID = String.valueOf(rsReq[0][0].CERTIFICATION_PROFILE_ID);
                                            pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                            pPROVINCE_ID = String.valueOf(rsReq[0][0].CITY_PROVINCE_ID);
                                            sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            pCREATE_USER = String.valueOf(rsReq[0][0].CREATED_BY_ID);
                                            pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                            pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                            if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                            }
                                        } else {
                                            isAccessAgency = false;
                                        }
                                        //</editor-fold>

                                        if (isAccessAgency == true) {
                                            String sCERT_ATTR_TYPE_CODE = "";
                                            String pFUNCTIONALITY_NAME = "";
                                            if(Integer.parseInt(sCERTIFICATION_ATTR_TYPE_ID) == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE)
                                            {
                                                sCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE;
                                                pFUNCTIONALITY_NAME = Definitions.CONFIG_LOG_FUNCTIONALITY_PERMANENT_DISABLE;
                                            } else if(Integer.parseInt(sCERTIFICATION_ATTR_TYPE_ID) == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                            {
                                                sCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE;
                                                pFUNCTIONALITY_NAME = Definitions.CONFIG_LOG_FUNCTIONALITY_TEMPORARY_DISABLE;
                                                sSUSPEND_TIME_DB = CommonFunction.ConvertStringToTimeStamp(sSUSPEND_TIME);
                                            }
                                            String pPAST_CERTIFICATE_ID = sID;
                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                            tempLogReq = new CERTIFICATION_DATA_ATTR();
                                            objectMapper = new ObjectMapper();
                                            tempLogReq.personalName = pPERSONAL_NAME;
                                            tempLogReq.companyName = pCOMPANY_NAME;
                                            tempLogReq.enterpriseID = pENTERPRISE_ID;
                                            tempLogReq.personalID = pPERSONAL_ID;
                                            tempLogReq.emailContract = EMAIL_CONTRACT;
                                            tempLogReq.phoneContract = PHONE_CONTRACT;
                                            tempLogReq.issuerSubject = pISSUER_SUBJECT;
                                            tempLogReq.subjectDn = pSUBJECT;
                                            tempLogReq.tokenSn = sTOKEN_SN;
                                            tempLogReq.pkiFromFactorId = pPKI_FORMFACTOR_ID;
                                            tempLogReq.typeName = sCERT_ATTR_TYPE_CODE;
                                            String strReq = objectMapper.writeValueAsString(tempLogReq);
                                            db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                    Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                    pFUNCTIONALITY_NAME, strReq, loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                            CommonFunction.LogDebugString(log, "RegistrationCertificate", "SuspendedCert: " + "SUBJECT: " + pSUBJECT
                                                    + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME
                                                    + "; pENTERPRISE_ID: " + pENTERPRISE_ID + "; pPKI_FORMFACTOR_ID: " + pPKI_FORMFACTOR_ID
                                                    + "; pPERSONAL_ID: " + pPERSONAL_ID + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                    + "; pCERTIFICATION_ATTR_TYPE_CODE: " + sCERT_ATTR_TYPE_CODE + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                    + "; ISSUER_SUBJECT: " + pISSUER_SUBJECT + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                    + "; TOKEN_ID_NEW: " + sOLD_TOKEN_ID + "; TOKEN_SN_NEW: " + sTOKEN_SN + "; CITY_PROVINCE_ID: " + pPROVINCE_ID);
                                            ATTRIBUTE_VALUES valueATTR;
                                            ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                            dataATTR.setCertificationData(tempLogReq);
                                            valueATTR = new ATTRIBUTE_VALUES();
                                            valueATTR.setTokenSn(sTOKEN_SN);
                                            valueATTR.setCerttificateSuspendReason(sSUSPEND_REASON);
                                            valueATTR.setSuspendedTime(sSUSPEND_TIME_DB);
                                            valueATTR.setTypeName(sCERT_ATTR_TYPE_CODE);
                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                            valueATTR.setCreateUser(loginFullname + " (" + loginUID + ")");
                                            valueATTR.setCreateDt(new Date());
                                            valueATTR.setAttributeData(dataATTR);
                                            //</editor-fold>

                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                            String sParam = db.S_BO_CERTIFICATION_ATTR_INSERT(sID, sCERTIFICATION_ATTR_TYPE_ID,
                                                strReqValueATTR, loginUID, pCERTIFICATE_ATTR_ID);
                                            if ("0".equals(sParam)) {
                                                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
                                                    //<editor-fold defaultstate="collapsed" desc="### FILE MANAGER INSERT">
                                                    String sJRBConfig = "";
                                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                                    if (sessGeneralPolicy[0].length > 0) {
                                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                        {
                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT))
                                                            {
                                                                sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCert");
                                                    int[] intResult = new int[1];
                                                    boolean isAutoSendFileToNew = false;
                                                    Config conf =new Config();
                                                    if("1".equals(LoadParamSystem.getParamStart(Definitions.CONFIG_FILE_FILE_UP_OLD_TO_NEW_AUTO_ENABLED))) {
                                                        isAutoSendFileToNew = true;
                                                    }
                                                    ServletUptoFunction.fileManagerInsert(sJRBConfig, cartToken, String.valueOf(pCERTIFICATION_ID),
                                                        String.valueOf(pCERTIFICATION_OWNER_ID), loginUID, log, isAutoSendFileToNew, true, intResult);
                                                    if(intResult[0] == 0){
                                                        request.getSession(false).setAttribute("sessUploadFileCert", null);
                                                    }
                                                    //</editor-fold>
                                                }
                                                    
                                                if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                    //<editor-fold defaultstate="collapsed" desc="### AGENCY ROOT LOGIN">
                                                    if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)) {
                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                        valueATTR.setApproveCADt(new Date());
                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                        if ("0".equals(sApprove)) {
                                                            int[] intRes = new int[1];
                                                            String[] sRes = new String[1];
                                                            // RACONNECTOR
                                                            String pCA_NAME = "";
                                                            CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                            db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                            if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                            }
                                                            //###sCERT_REVOCATION_REASON
                                                            ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_CERTIFICATEHOLD_ID,
                                                                pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID[0]);
                                                            if (intRes[0] == 0) {
                                                                String pCOMMENT = "";
                                                                objectMapper = new ObjectMapper();
                                                                CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                jsonCertComment.certificateDeclineReason = "";
                                                                jsonCertComment.certificateRevokeReason = "";
                                                                jsonCertComment.certificateSuspendReason = sSUSPEND_REASON;
                                                                pCOMMENT = objectMapper.writeValueAsString(jsonCertComment);
                                                                db.S_BO_CERTIFICATION_DISABLE(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sSUSPEND_TIME_DB, pCOMMENT, loginUID);
                                                                sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                strView = "0#0";
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#0";
                                                            }
                                                        } else {
                                                            CommonFunction.LogDebugString(log, "ERROR S_BO_CERTIFICATION_APPROVED - RESPONSE", sApprove);
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                        }
                                                    } else {
                                                        ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,
                                                            Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                        {
                                                            // Approve CA
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveCADt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                            if ("0".equals(sApprove))
                                                            {
                                                                int[] intRes = new int[1];
                                                                String[] sRes = new String[1];
                                                                // RACONNECTOR
                                                                String pCA_NAME = "";
                                                                CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                    pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                }
                                                                //###sCERT_REVOCATION_REASON
                                                                ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_CERTIFICATEHOLD_ID,
                                                                    pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID[0]);
                                                                if (intRes[0] == 0) {
                                                                    String pCOMMENT = "";
                                                                    objectMapper = new ObjectMapper();
                                                                    CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                    jsonCertComment.certificateDeclineReason = "";
                                                                    jsonCertComment.certificateRevokeReason = "";
                                                                    jsonCertComment.certificateSuspendReason = sSUSPEND_REASON;
                                                                    pCOMMENT = objectMapper.writeValueAsString(jsonCertComment);
                                                                    db.S_BO_CERTIFICATION_DISABLE(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sSUSPEND_TIME_DB, pCOMMENT, loginUID);
                                                                    sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                    strView = "0#0";
                                                                } else {
                                                                    strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#0";
                                                                }
                                                            } else {
                                                                CommonFunction.LogDebugString(log, "ERROR S_BO_CERTIFICATION_APPROVED - RESPONSE", sApprove);
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                            }
                                                        } else {
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                            sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                            strView = "0#0";
                                                        }
                                                    }
                                                    //</editor-fold>
                                                } else {
                                                    //<editor-fold defaultstate="collapsed" desc="### AGENCY USER LOGIN">
                                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                    boolean checkCallApproveCA = false;
                                                    String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                    objectMapper = new ObjectMapper();
//                                                    CommonFunction.LogDebugString(log, "PORTAL - " + sCERT_ATTR_TYPE_CODE, "UserLogin: " + loginUID + "; Level: " + SessLevelBranch
//                                                        + "; RoleID: " + sessionsa.getAttribute("RoleID_ID").toString().trim()
//                                                        + "; FunctionCert: " + objectMapper.writeValueAsString(sessFunctionCert));
                                                    if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                    {
                                                        if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                            if("0".equals(paramPre)) {
                                                                checkCallApproveCA = true;
                                                            }
                                                        } else {
                                                            int approveChilrenID = Integer.parseInt(userLoginID);
                                                            BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                            db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                            if(rsUserApprve[0].length > 0) {
                                                                for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                    if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                        approveChilrenID = item.ID;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                            if("0".equals(paramAgency)) {
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                {
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                    if("0".equals(paramPre)) {
                                                                        checkCallApproveCA = true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                            {
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                if("0".equals(paramPre)) {
                                                                    checkCallApproveCA = true;
                                                                }
                                                            }
                                                        } else {
                                                            int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                            if(intApprove == 1) {
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                {
                                                                    int approveChilrenID = Integer.parseInt(userLoginID);
                                                                    BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                    db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                    if(rsUserApprve[0].length > 0) {
                                                                        for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                            if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                approveChilrenID = item.ID;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID, approveChilrenID);
                                                                    if("0".equals(paramAgency)) {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), loginUID);
                                                                            if("0".equals(paramPre)) {
                                                                                checkCallApproveCA = true;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if(checkCallApproveCA == true) {
                                                        //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                        boolean autoApproveCA = false;
                                                        CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                        objectMapper = new ObjectMapper();
//                                                        CommonFunction.LogDebugString(log, "PORTAL - " + sCERT_ATTR_TYPE_CODE, "UserLogin: " + loginUID
//                                                                + "; PolicyCert: " + objectMapper.writeValueAsString(sessPolicyCert_Data));
                                                        if(CommonFunction.checkApproveCAReqType(sCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                        {
                                                            autoApproveCA = true;
                                                        }
                                                        if(autoApproveCA == true) {
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveCADt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                            if ("0".equals(sApprove)) {
                                                                int[] intRes = new int[1];
                                                                String[] sRes = new String[1];
                                                                String pCA_NAME = "";
                                                                CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                    pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                }
                                                                //###sCERT_REVOCATION_REASON
                                                                ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_CERTIFICATEHOLD_ID,
                                                                    pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID[0]);
                                                                if (intRes[0] == 0) {
                                                                    objectMapper = new ObjectMapper();
                                                                    CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                    jsonCertComment.certificateDeclineReason = "";
                                                                    jsonCertComment.certificateRevokeReason = "";
                                                                    jsonCertComment.certificateSuspendReason = sSUSPEND_REASON;
                                                                    String pCOMMENT = objectMapper.writeValueAsString(jsonCertComment);
                                                                    db.S_BO_CERTIFICATION_DISABLE(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sSUSPEND_TIME_DB, pCOMMENT, loginUID);
                                                                    sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                    strView = "0#0";
                                                                } else {
                                                                    strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#0";
                                                                }
                                                            } else {
                                                                CommonFunction.LogDebugString(log, "ERROR S_BO_CERTIFICATION_APPROVED - RESPONSE", sApprove);
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                            }
                                                        } else {
                                                            strIsPushNotiApprove = "1";
                                                            sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                            strView = "0#0";
                                                        }
                                                        //</editor-fold>
                                                    } else {
                                                        strIsPushNotiApprove = "1";
                                                        sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                        strView = "0#0";
                                                    }
                                                    //</editor-fold>
                                                }
                                            } else {
                                                strView = sParam + "#0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "CertSuspend: Cert ID cannot be empty");
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "gethiscertmst": {
                                //<editor-fold defaultstate="collapsed" desc="gethiscertmst">
                                String IsMST = EscapeUtils.CheckTextNull(request.getParameter("IsMST"));
                                String vMST = EscapeUtils.CheckTextNull(request.getParameter("vMST"));
//                                if (null == IsMST) {
//                                    vMST = Definitions.CONFIG_CERTIFICATION_PREFIX_DECISION + ":" + vMST;
//                                } else switch (IsMST) {
//                                    case "1":
//                                        vMST = Definitions.CONFIG_CERTIFICATION_PREFIX_TAXCODE + ":" + vMST;
//                                        break;
//                                    case "0":
//                                        vMST = Definitions.CONFIG_CERTIFICATION_PREFIX_BUDGET_CODE + ":" + vMST;
//                                        break;
//                                    default:
//                                        vMST = Definitions.CONFIG_CERTIFICATION_PREFIX_DECISION + ":" + vMST;
//                                        break;
//                                }
                                String sParam = db.S_BO_CHECK_ENTERPRISE_ID(CommonReferServlet.convertPrefixVNToEN(vMST, true));
                                if ("1".equals(sParam)) {
                                    strView = "0#0";
                                } else {
                                    strView = "1#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "gethiscertcmnd": {
                                //<editor-fold defaultstate="collapsed" desc="gethiscertcmnd">
                                String IsCMND = EscapeUtils.CheckTextNull(request.getParameter("IsCMND"));
                                String vCMND = EscapeUtils.CheckTextNull(request.getParameter("vCMND"));
//                                if (null == IsCMND) {
//                                    vCMND = Definitions.CONFIG_CERTIFICATION_PREFIX_PASSPORT + ":" + vCMND;
//                                } else switch (IsCMND) {
//                                    case "1":
//                                        vCMND = Definitions.CONFIG_CERTIFICATION_PREFIX_PERSONAL_CODE + ":" + vCMND;
//                                        break;
//                                    case "2":
//                                        vCMND = Definitions.CONFIG_CERTIFICATION_PREFIX_CITIZEN_CODE + ":" + vCMND;
//                                        break;
//                                    default:
//                                        vCMND = Definitions.CONFIG_CERTIFICATION_PREFIX_PASSPORT + ":" + vCMND;
//                                        break;
//                                }
                                String sParam = db.S_BO_CHECK_PERSONAL_ID(CommonReferServlet.convertPrefixVNToEN(vCMND, false));
                                if ("1".equals(sParam)) {
                                    strView = "0#0";
                                } else {
                                    strView = "1#0";
                                }
                                //</editor-fold>
                                break;
                            }
                        }
                    }
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "#0";
                }
            } catch (NumberFormatException e) {
                CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#" + e.getMessage();
            } catch (Exception e) {
                CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#" + e.getMessage();
            } finally {
                if (System_Log_ID[0] != 0) {
                    int sResCode = Definitions.CONFIG_RESPONSE_CODE_ID_SUCCESS;
                    if (strView.split("#")[0].trim().equals(Definitions.CONFIG_EXCEPTION_STRING_ERROR)) {
                        sResCode = Definitions.CONFIG_RESPONSE_CODE_ID_EXCEPTION;
                    }
                    RESPONSE_LOG tempLogRes = new RESPONSE_LOG();
                    RESPONSE_CODE[][] rsResponse = new RESPONSE_CODE[1][];
                    db.S_BO_RESPONSE_CODE_DETAIL(String.valueOf(sResCode), rsResponse);
                    tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                    tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, objectMapper.writeValueAsString(tempLogRes), "", loginUID);
                }
            }
            out.println(strView);
//            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
