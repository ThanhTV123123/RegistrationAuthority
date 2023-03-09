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
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import vn.ra.object.DNS_NAME_DATA;
import vn.ra.object.FILE_PROFILE_DATA;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.ProfileContactInfoJson;
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
import vn.ra.process.DESEncryption;
import vn.ra.process.EncodeSOPIN;
import vn.ra.process.JackRabbitCommon;
import vn.ra.process.ServletUptoFunction;
import vn.ra.process.SessionDNSName;
import vn.ra.process.SessionUploadFileCert;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.LoadParamSystem;
import vn.ra.utility.PropertiesContent;

/**
 *
 * @author USER
 */
public class CertificateShareCommon extends HttpServlet {
    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CertificateShareCommon.class.getName());
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
                    String loginUserID = request.getSession(false).getAttribute("UserID").toString().trim();
                    String loginFullname = request.getSession(false).getAttribute("sesFullname").toString().trim();
                    String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                    String sIP_Request = CommonFunction.getClientIpLogin(request);
                    CERTIFICATION_DATA_ATTR tempLogReq;
                    if (null != idParam) {
                        switch (idParam) {
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
                                            if(rsReq[0][0].SHARED_MODE == false) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    //</editor-fold>
                                    
                                    if (isAccessAgency == true) {
                                        if(!"".equals(ID) || !"".equals(sTOKEN_SN)) {
                                            String PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                            String EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
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
                                            String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                            String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                            String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
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
                                                    if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_STAFF))
                                                        || pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE))
                                                        || pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_PERSONAL))) {
                                                        pDEVICE = "";
                                                    }
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
                                                                    pPASSPORT, DN, CACoreSubject.trim(), PHONE_CONTRACT, EMAIL_CONTRACT, BRANCH_ID,
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
                                                        String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
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
                                                            db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(pCERTIFICATE_ID[0], objectMapper.writeValueAsString(profileContact), loginUserID);
                                                        } else {
                                                            CERTIFICATION[][] rsBrief = new CERTIFICATION[1][];
                                                            db.S_BO_CERTIFICATION_BRIEF_DETAIL(ids, rsBrief);
                                                            String sPrfileContact = EscapeUtils.CheckTextNull(rsBrief[0][0].PROFILE_CONTACT_INFO);
                                                            if(!"".equals(sPrfileContact)) {
                                                                objectMapper = new ObjectMapper();
                                                                ProfileContactInfoJson profileContactOld = objectMapper.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                                                if(profileContactOld != null) {
                                                                    db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(pCERTIFICATE_ID[0], sPrfileContact, loginUserID);
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
                                                                    break;
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
                                                                    String sFileData = EscapeUtils.CheckTextNull(mhIP.FILE_URL);
                                                                    CloseableHttpResponse pHttpRes = ConnectFileToPartner.upFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                                                        sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                                                        sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, idUUID_Temp,
                                                                        mhIP.FILE_NAME, sFileData);
                                                                    InputStream isStr = pHttpRes.getEntity().getContent();
                                                                    String resultUUID = IOUtils.toString(isStr);
                                                                    CommonFunction.LogDebugString(log, "UUID", resultUUID);
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
                                                                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
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
                                                                    if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                        int[] intWSRes = new int[1];
                                                                        String[] sWSRes = new String[1];
                                                                        ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                        if (intWSRes[0] == 0) { }
                                                                        else {
                                                                            isProcess = false;
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
                                                            if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                            {
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                boolean autoApproveCA = false;
                                                                CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
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
                                                                            if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                                int[] intWSRes = new int[1];
                                                                                String[] sWSRes = new String[1];
                                                                                ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                if (intWSRes[0] == 0) { }
                                                                                else {
                                                                                    isProcess = false;
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
                                                                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                {
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                    db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                    //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                    boolean autoApproveCA = false;
                                                                    CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                    CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
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
                                                                                if(Integer.parseInt(pPKI_FORMFACTOR) == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                                                    int[] intWSRes = new int[1];
                                                                                    String[] sWSRes = new String[1];
                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                    if (intWSRes[0] == 0) { }
                                                                                    else {
                                                                                        isProcess = false;
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
                                                                }
                                                            }
                                                        }
                                                        if(isProcess == true)
                                                        {
                                                            sessionsa.setAttribute("RefreshCertShareSess", "1");
                                                            if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                                || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_SSL
                                                                || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING
                                                                || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                                strView = "0#" + pStringCERTIFICATION_ID + "#0#" + strIsPushNotiApprove;
                                                            } else {
                                                                strView = "0#" + pStringCERTIFICATION_ID + "#1#" + strIsPushNotiApprove;
                                                            }
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
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
                                        // continue
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
                                    String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                    String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                    String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    // check agency
                                    boolean isAccessAgency = true;
                                    boolean isProcess = true;
                                    String strIsPushNotiApprove = "0";
                                    String sTOKEN_ID = "";
                                    String sTOKEN_SN = "";
                                    String PHONE_CONTRACT = "";
                                    String EMAIL_CONTRACT = "";
                                    String CACoreSubject = "";
                                    String DN = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                    String pPROPERTIES = "";
                                    String pCERTIFICATION_SN = "";
                                    String pPRIVATE_KEY = "";
                                    //String pPROVINCE_ID = "";
                                    String sAGENT_ID = "";
                                    String sAGENT_ID_OLD = "";
                                    int pPKI_FORMFACTOR_ID = 0;
                                    int pCERTIFICATION_OWNER_ID = 0;
                                    int pCERTIFICATION_PURPOSE_ID_INNER = 0;
                                    boolean pPRIVATE_KEY_ENABLED = true;
                                    String pCREATE_USER = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                        EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                        CACoreSubject = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                        pCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                        pPROPERTIES = EscapeUtils.CheckTextNull(rsReq[0][0].PROPERTIES);
                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sAGENT_ID_OLD = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sTOKEN_ID = String.valueOf(rsReq[0][0].TOKEN_ID);
                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                        pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                        pCERTIFICATION_PURPOSE_ID_INNER = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                        //pPROVINCE_ID = String.valueOf(rsReq[0][0].CITY_PROVINCE_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            if(rsReq[0][0].SHARED_MODE == false) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                            } else {
                                                if (!sAGENT_ID.equals(SessUserAgentID)) {
                                                    sAGENT_ID = SessUserAgentID;
                                                }
                                            }
                                        } else {
                                            sAGENT_ID = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ID"));
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    //</editor-fold>
                                    
                                    if (isAccessAgency == true) {
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
                                                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
                                                    PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                                    EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
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
                                                        + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME
                                                        + "; pENTERPRISE_ID: " + pENTERPRISE_ID + "; pPERSONAL_ID: " + pPERSONAL_ID
                                                        + "; pDEVICE: " + pDEVICE + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                        + "; pCERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID
                                                        + "; pPKI_FORMFACTOR_ID: " + pPKI_FORMFACTOR_ID
                                                        + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
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
                                                boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteRevoke);
                                                valueATTR.setDeleteOldCertificateEnabled(sRevokeDeleteInTokenEnabled);
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
                                                    String sSHARE_CERTIFICATE_MODE = "0";
                                                    if (sessGeneralPolicy[0].length > 0) {
                                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                        {
                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DEFAULT_SHARE_CERTIFICATE_MODE_FOR_BRANCH))
                                                            {
                                                                sSHARE_CERTIFICATE_MODE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                break;
                                                            }
                                                        }
                                                    }

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
                                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0]) {
                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT)) {
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
                                                            ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
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
                                                                if("0".equals(sSHARE_CERTIFICATE_MODE)) {
                                                                    if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                    {
                                                                        if("0".equals(sNoAllowTranferToken)) {
                                                                            if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                db.S_BO_TOKEN_UPDATE_BRANCH(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                            }
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
                                                                        // RACONNECTOR APPROVE RENEWAL
                                                                        int[] intWSRes = new int[1];
                                                                        String[] sWSRes = new String[1];
                                                                        ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                        if (intWSRes[0] == 0) { }
                                                                        else {
                                                                            isProcess = false;
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
    //                                                    if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
    //                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
    //                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
    //                                                    {
    //                                                        if(!sAGENT_ID.equals(sAGENT_ID_OLD))
    //                                                        {
    //                                                            db.S_BO_TOKEN_UPDATE_BRANCH(sTOKEN_ID, sAGENT_ID, loginUID);
    //                                                        }
    //                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
    //                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
    //                                                        valueATTR.setApproveDt(new Date());
    //                                                        valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
    //                                                        valueATTR.setApproveCADt(new Date());
    //                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
    //                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
    //                                                        if ("0".equals(sApprove)) {
    //                                                            if(!"".equals(sTOKEN_ID))
    //                                                            {
    //                                                                if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
    //                                                                    || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
    //                                                                    || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
    //                                                                    || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID)))
    //                                                                {
    //                                                                    // RACONNECTOR APPROVE RENEWAL
    //                                                                    int[] intWSRes = new int[1];
    //                                                                    String[] sWSRes = new String[1];
    //                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
    //                                                                    if (intWSRes[0] == 0) { }
    //                                                                    else {
    //                                                                        isProcess = false;
    //                                                                    }
    //                                                                } else { }
    //                                                            }
    //                                                        }
    //                                                    } else {
    //                                                        ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
    //                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
    //                                                        {
    //                                                            if(!sAGENT_ID.equals(sAGENT_ID_OLD))
    //                                                            {
    //                                                                db.S_BO_TOKEN_UPDATE_BRANCH(sTOKEN_ID, sAGENT_ID, loginUID);
    //                                                            }
    //                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
    //                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
    //                                                            valueATTR.setApproveDt(new Date());
    //                                                            valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
    //                                                            valueATTR.setApproveCADt(new Date());
    //                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
    //                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
    //                                                            if ("0".equals(sApprove)) {
    //                                                                if(!"".equals(sTOKEN_ID))
    //                                                                {
    //                                                                    if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
    //                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
    //                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
    //                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID)))
    //                                                                    {
    //                                                                        // RACONNECTOR APPROVE RENEWAL
    //                                                                        int[] intWSRes = new int[1];
    //                                                                        String[] sWSRes = new String[1];
    //                                                                        ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
    //                                                                        if (intWSRes[0] == 0) { }
    //                                                                        else {
    //                                                                            isProcess = false;
    //                                                                        }
    //                                                                    } else { }
    //                                                                }
    //                                                            }
    //                                                        }
    //                                                        else
    //                                                        {
    //                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
    //                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
    //                                                            valueATTR.setApproveDt(new Date());
    //                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
    //                                                            db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
    //                                                        }
    //                                                    }
                                                        //</editor-fold>
                                                    } else {
                                                        //<editor-fold defaultstate="collapsed" desc="### AGENCY USER">
                                                        if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                            //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                            boolean autoApproveCA = false;
//                                                            BRANCH[][] rsBranch = new BRANCH[1][];
//                                                            db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
//                                                            if (rsBranch[0].length > 0) {
//                                                                String sCERTIFICATION_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
//                                                                if(!"".equals(sCERTIFICATION_POLICY_PROPERTIES)) {
//                                                                    autoApproveCA = CommonFunction.getApproveEnabledCert(sCERTIFICATION_POLICY_PROPERTIES);
//                                                                }
//                                                            }
                                                            CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                            db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                            CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
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
                                                                        if("0".equals(sSHARE_CERTIFICATE_MODE)) {
                                                                            if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                            {
                                                                                if("0".equals(sNoAllowTranferToken)) {
                                                                                    if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                        db.S_BO_TOKEN_UPDATE_BRANCH(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                                    }
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
                                                                                // RACONNECTOR APPROVE RENEWAL
                                                                                int[] intWSRes = new int[1];
                                                                                String[] sWSRes = new String[1];
                                                                                ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                if (intWSRes[0] == 0) { }
                                                                                else {
                                                                                    isProcess = false;
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
                                                            ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                boolean autoApproveCA = false;
//                                                                BRANCH[][] rsBranch = new BRANCH[1][];
//                                                                db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
//                                                                if (rsBranch[0].length > 0) {
//                                                                    String sCERTIFICATION_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
//                                                                    if(!"".equals(sCERTIFICATION_POLICY_PROPERTIES)) {
//                                                                        autoApproveCA = CommonFunction.getApproveEnabledCert(sCERTIFICATION_POLICY_PROPERTIES);
//                                                                    }
//                                                                }
                                                                CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
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
                                                                            if("0".equals(sSHARE_CERTIFICATE_MODE)) {
                                                                                if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true) {
                                                                                    if("0".equals(sNoAllowTranferToken)) {
                                                                                        if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                            db.S_BO_TOKEN_UPDATE_BRANCH(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                                        }
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
                                                                                    // RACONNECTOR APPROVE RENEWAL
                                                                                    int[] intWSRes = new int[1];
                                                                                    String[] sWSRes = new String[1];
                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                    if (intWSRes[0] == 0) { }
                                                                                    else {
                                                                                        isProcess = false;
                                                                                    }
                                                                                } else { }
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    strIsPushNotiApprove = "1";
                                                                }
                                                                //</editor-fold>
                                                            }
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
                                                        sessionsa.setAttribute("RefreshCertShareSess", "1");
                                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_SSL
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
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0#0";
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
                                    String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                    String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                                        String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    // check agency
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
                                    String sAGENT_ID = "";
                                    String sAGENT_ID_OLD = "";
                                    String pCREATE_USER = "";
                                    int pPKI_FORMFACTOR_ID=0;
                                    int pCERTIFICATION_OWNER_ID=0;
                                    int pCERTIFICATION_PURPOSE_ID_INNER=0;
                                    String pDISCOUNT_RATE="0";
                                    boolean pPRIVATE_KEY_ENABLED = true;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                        EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        pCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                        CACoreSubject = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sAGENT_ID_OLD = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sTOKEN_ID = String.valueOf(rsReq[0][0].TOKEN_ID);
                                        pCREATE_USER = String.valueOf(rsReq[0][0].CREATED_BY_ID);
                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                        pCERTIFICATION_PURPOSE_ID_INNER = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                        pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                        pDISCOUNT_RATE = String.valueOf(rsReq[0][0].DISCOUNT_RATE);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            if(rsReq[0][0].SHARED_MODE == false) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                            } else {
                                                if (!sAGENT_ID.equals(SessUserAgentID)) {
                                                    sAGENT_ID = SessUserAgentID;
                                                    pCREATE_USER = loginUserID;
                                                }
                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    //</editor-fold>
                                    
                                    if (isAccessAgency == true) {
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
                                                    String pPAST_CERTIFICATE_ID = ID;
                                                    String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO);
                                                    String pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                    if(pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                                        || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                        || pCERTIFICATION_PURPOSE_ID_INNER == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL) {
                                                        pDEVICE = "";
                                                    }
                                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
                                                        PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                                        EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
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
                                                    CommonFunction.LogDebugString(log, "RegistrationCertificate", "RegistrationCert: " + "SUBJECT: " + DN
                                                            + "; PERSONAL_NAME: " + pPERSONAL_NAME + "; COMPANY_NAME: " + pCOMPANY_NAME
                                                            + "; pENTERPRISE_ID: " + pENTERPRISE_ID + "; DOMAIN_NAME: " + pDOMAIN_NAME + "; pPERSONAL_ID: " + pPERSONAL_ID
                                                            + "; PKI_FORMFACTOR_ID: " + pPKI_FORMFACTOR_ID + "; pDEVICE: " + pDEVICE + "; PAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                            + "; CERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                            + "; CACoreSubject: " + CACoreSubject + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                            + "; TOKEN_SN: " + sTOKEN_SN + "; PROVINCE_DESC: " + pPROVINCE_DESC
                                                            + "; CheckCHANGE_KEY: " + CheckCHANGE_KEY + "; CheckREVOKE_ENABLED: " + CheckREVOKE_ENABLED
                                                            + "; CheckDeleteCertificate: " + CheckDeleteRevoke);
                                                    ATTRIBUTE_VALUES valueATTR;
                                                    ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                    dataATTR.setCertificationData(tempLogReq);
                                                    valueATTR = new ATTRIBUTE_VALUES();
                                                    valueATTR.setTokenSn(sTOKEN_SN);
                                                    boolean sChangeKeyEnabled = "1".equals(CheckCHANGE_KEY);
                                                    valueATTR.setChangeKeyEnabled(sChangeKeyEnabled);
                                                    boolean sRevokedEnabled = "1".equals(CheckREVOKE_ENABLED);
                                                    valueATTR.setRevokeOldCertificateEnabled(sRevokedEnabled);
                                                    boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteRevoke);
                                                    valueATTR.setDeleteOldCertificateEnabled(sRevokeDeleteInTokenEnabled);
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
                                                            CheckPRIVATE_KEY, loginUID, "", "", "", pDISCOUNT_RATE, pSHARED_MODE_UPDATE,
                                                            pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
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

                                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                        String sNoAllowTranferToken = "1";
                                                        String sSHARE_CERTIFICATE_MODE = "0";
                                                        if (sessGeneralPolicy[0].length > 0) {
                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                            {
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DEFAULT_SHARE_CERTIFICATE_MODE_FOR_BRANCH))
                                                                {
                                                                    sSHARE_CERTIFICATE_MODE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                }
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_NO_AUTO_MOVE_TOKEN_FOR_RENEWAL_REVISION_CERTIFICATE_REQUEST))
                                                                {
                                                                    sNoAllowTranferToken = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                }
                                                            }
                                                        }
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
                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0]) {
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT)) {
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
                                                                    if("0".equals(sSHARE_CERTIFICATE_MODE)) {
                                                                        if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                        {
                                                                            if("0".equals(sNoAllowTranferToken)) {
                                                                                if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                    db.S_BO_TOKEN_UPDATE_BRANCH(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if(!"".equals(sTOKEN_ID))
                                                                    {
                                                                        if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                            || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                            || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                            || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID))) {
                                                                            // RACONNECTOR APPROVE CHANGEINFO
                                                                            int[] intWSRes = new int[1];
                                                                            String[] sWSRes = new String[1];
                                                                            ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                            if (intWSRes[0] == 0) { }
                                                                            else {
                                                                                isProcess = false;
                                                                            }
                                                                        } else { }
                                                                    }
                                                                }
                                                            } else {
                                                                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
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
                                                                        if("0".equals(sSHARE_CERTIFICATE_MODE)) {
                                                                            if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                            {
                                                                                if("0".equals(sNoAllowTranferToken)) {
                                                                                    if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                        db.S_BO_TOKEN_UPDATE_BRANCH(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                                    }
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
                                                                                // RACONNECTOR APPROVE CHANGEINFO
                                                                                int[] intWSRes = new int[1];
                                                                                String[] sWSRes = new String[1];
                                                                                ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                if (intWSRes[0] == 0) { }
                                                                                else {
                                                                                    isProcess = false;
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
                                                            if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                valueATTR.setApproveDt(new Date());
                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                boolean autoApproveCA = false;
                                                                CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                if(rsProfile[0].length > 0) {
//                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
//                                                                    db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
//                                                                    if (rsBranch[0].length > 0) {
//                                                                        String sCERTIFICATION_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
//                                                                        if(!"".equals(sCERTIFICATION_PROFILE_PROPERTIES)) {
                                                                    autoApproveCA = CommonFunction.getApproveCAOfProfile(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME), sessPolicyCert_Data);
//                                                                        autoApproveCA = CommonFunction.getApproveEnabledCert(sCERTIFICATION_POLICY_PROPERTIES);
//                                                                        }
//                                                                    }
                                                                }
                                                                if(autoApproveCA == true) {
                                                                    if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true) {
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        valueATTR.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR.setApproveCADt(new Date());
                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                        if ("0".equals(sApprove)) {
                                                                            if("0".equals(sSHARE_CERTIFICATE_MODE)) {
                                                                                if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                                {
                                                                                    if("0".equals(sNoAllowTranferToken)) {
                                                                                        if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                            db.S_BO_TOKEN_UPDATE_BRANCH(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            if(!"".equals(sTOKEN_ID))
                                                                            {
                                                                                if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                    || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                    || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                                    || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID))) {
                                                                                    // RACONNECTOR APPROVE CHANGEINFO
                                                                                    int[] intWSRes = new int[1];
                                                                                    String[] sWSRes = new String[1];
                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                    if (intWSRes[0] == 0) { }
                                                                                    else {
                                                                                        isProcess = false;
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
                                                                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,
                                                                        Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                    db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                    //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                    boolean autoApproveCA = false;
//                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
//                                                                    db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
//                                                                    if (rsBranch[0].length > 0) {
//                                                                        String sCERTIFICATION_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
//                                                                        if(!"".equals(sCERTIFICATION_POLICY_PROPERTIES)) {
//                                                                            autoApproveCA = CommonFunction.getApproveEnabledCert(sCERTIFICATION_POLICY_PROPERTIES);
//                                                                        }
//                                                                    }
                                                                    CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                    CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
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
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                                            if ("0".equals(sApprove)) {
                                                                                if("0".equals(sSHARE_CERTIFICATE_MODE)) {
                                                                                    if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                                    {
                                                                                        if("0".equals(sNoAllowTranferToken)) {
                                                                                            if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                                db.S_BO_TOKEN_UPDATE_BRANCH(sTOKEN_ID, sAGENT_ID, loginUID);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(!"".equals(sTOKEN_ID))
                                                                                {
                                                                                    if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID))) {
                                                                                        // RACONNECTOR APPROVE CHANGEINFO
                                                                                        int[] intWSRes = new int[1];
                                                                                        String[] sWSRes = new String[1];
                                                                                        ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                        if (intWSRes[0] == 0) { }
                                                                                        else {
                                                                                            isProcess = false;
                                                                                        }
                                                                                    } else { }
                                                                                }
                                                                            }
                                                                        }
                                                                    } else {
                                                                        strIsPushNotiApprove = "1";
                                                                    }
                                                                    //</editor-fold>
                                                                }
                                                            }
                                                            //</editor-fold>
                                                        }
                                                        if(isProcess == true) {
                                                            sessionsa.setAttribute("RefreshCertShareSess", "1");
                                                            strView = "0#0#" + strIsPushNotiApprove;
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                        }
                                                    } else {
                                                        strView = sParam + "#0";
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
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
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
                                    String sTOKEN_SN = "";
                                    String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String CheckCHANGE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckCHANGE_KEY"));
                                    String CheckPRIVATE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckPRIVATE_KEY"));
                                    String CheckREVOKE_ENABLED = EscapeUtils.CheckTextNull(request.getParameter("CheckREVOKE_ENABLED"));
                                    String pPERSONAL_NAME =EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_NAME"));
                                    String pCOMPANY_NAME = EscapeUtils.CheckTextNull(request.getParameter("pCOMPANY_NAME"));
                                    String pDOMAIN_NAME = EscapeUtils.CheckTextNull(request.getParameter("pDOMAIN_NAME"));
                                    String pTAX_CODE = EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                    String pDECISION =EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                    String pBUDGET_CODE = EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                    String pP_ID = EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                    String pCCCD = EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                    String pPASSPORT = EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                    String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
                                    String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                    String pPROVINCE_DESC = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_DESC"));
                                    String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                    String pCERTIFICATION_PROFILE_ID = EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                    String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                    String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                    if(!"".equals(sID) && !"".equals(pCERTIFICATION_PROFILE_ID)) {
                                        //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                        boolean isAccessAgency = true;
                                        String strIsPushNotiApprove = "0";
                                        String PHONE_CONTRACT = "";
                                        String EMAIL_CONTRACT = "";
                                        String pCERTIFICATION_SN = "";
                                        String pSUBJECT = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                        String sPRIVATE_KEY = "";
                                        String sOLD_TOKEN_ID = "";
                                        String pISSUER_SUBJECT = "";
                                        String sAGENT_ID = "";
                                        String sAGENT_ID_OLD = "";
                                        String pCREATE_USER = "";
                                        String pDISCOUNT_RATE = "";
                                        int pPKI_FORMFACTOR_ID= 0;
                                        int pCERTIFICATION_OWNER_ID= 0;
                                        CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(sID, sessLanguage, rsReq);
                                        if (rsReq[0].length > 0) {
                                            sOLD_TOKEN_ID = String.valueOf(rsReq[0][0].TOKEN_ID);
                                            PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                            EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                            pDISCOUNT_RATE = String.valueOf(rsReq[0][0].DISCOUNT_RATE);
                                            pISSUER_SUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                            sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            sAGENT_ID_OLD = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                            pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                            pCREATE_USER = String.valueOf(rsReq[0][0].CREATED_BY_ID);
                                            if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                if(rsReq[0][0].SHARED_MODE == false) {
                                                    BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                    isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                                } else {
                                                    if (!sAGENT_ID.equals(SessUserAgentID)) {
                                                        sAGENT_ID = SessUserAgentID;
                                                        pCREATE_USER = loginUserID;
                                                    }
                                                }
                                            }
                                        } else {
                                            isAccessAgency = false;
                                        }
                                        //</editor-fold>

                                        if (isAccessAgency == true) {
                                            //<editor-fold defaultstate="collapsed" desc="TOKEN_SN UNASSIGNED">
                                            sTOKEN_SN = Definitions.CONFIG_TOKEN_UNASSIGN_SN;
                                            String sTOKEN_ID_NEW = String.valueOf(Definitions.CONFIG_TOKEN_UNASSIGN_ID);
                                            String pPAST_CERTIFICATE_ID = sID;
                                            String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE);
                                            String pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                            String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                                EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
                                            }
                                                    
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
                                            tempLogReq.typeName = pCERT_ATTR_TYPE_CODE;
                                            String strReq = objectMapper.writeValueAsString(tempLogReq);
                                            db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                    Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                    Definitions.CONFIG_LOG_FUNCTIONALITY_REQUEST_REISSUE, strReq,
                                                    loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                            CommonFunction.LogDebugString(log, "RegistrationCertificate", "RegistrationCert: " + "SUBJECT: " + pSUBJECT
                                                    + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME
                                                    + "; pENTERPRISE_ID: " + pENTERPRISE_ID + "; pPERSONAL_ID: " + pPERSONAL_ID
                                                    + "; pPKI_FORMFACTOR_ID: " + pPKI_FORMFACTOR_ID + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                    + "; pCERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
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
                                            valueATTR.setTypeName(pCERT_ATTR_TYPE_CODE);
                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                            valueATTR.setCreateUser(loginFullname + " (" + loginUID + ")");
                                            valueATTR.setCreateDt(new Date());
                                            valueATTR.setAttributeData(dataATTR);
                                            //</editor-fold>

                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
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
                                            String sParam = "1000";
                                            String strDNSName = "";
                                            while ("1000".equals(sParam)) {
                                                try {
                                                    String sOTP = CommonFunction.getRandomOTP(intOTPNumn);
                                                    sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(sTOKEN_ID_NEW), pCERTIFICATION_PROFILE_ID, sTOKEN_SN,
                                                        pCERTIFICATION_SN, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME, pTAX_CODE, pBUDGET_CODE, pP_ID,
                                                        pPASSPORT, pSUBJECT, pISSUER_SUBJECT, PHONE_CONTRACT, EMAIL_CONTRACT, sAGENT_ID,
                                                        pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, sOTP,
                                                        strReqValueATTR, pCREATE_USER, loginUID, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID,
                                                        "", CheckPRIVATE_KEY, CheckCHANGE_KEY, sPRIVATE_KEY, pPKI_FORMFACTOR_ID, "",
                                                        String.valueOf(pCERTIFICATION_OWNER_ID), pCCCD, null, pDECISION, pPERSONAL_ID, pENTERPRISE_ID);
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
                                                    "", loginUID, "", "", "", pDISCOUNT_RATE, pSHARED_MODE_UPDATE, pCCCD, pDECISION,
                                                    pENTERPRISE_ID, pPERSONAL_ID);
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
                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0]) {
                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT)) {
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
                                                        ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
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
                                                    if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                        //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                        boolean autoApproveCA = false;
    //                                                    BRANCH[][] rsBranch = new BRANCH[1][];
    //                                                    db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
    //                                                    if (rsBranch[0].length > 0) {
    //                                                        String sCERTIFICATION_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
    //                                                        if(!"".equals(sCERTIFICATION_POLICY_PROPERTIES)) {
    //                                                            autoApproveCA = CommonFunction.getApproveEnabledCert(sCERTIFICATION_POLICY_PROPERTIES);
    //                                                        }
    //                                                    }
                                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(pCERTIFICATION_PROFILE_ID, rsProfile);
                                                        CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
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
                                                            }
                                                        } else {
                                                            strIsPushNotiApprove = "1";
                                                        }
                                                        //</editor-fold>
                                                    } else {
                                                        ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,
                                                            Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                            //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                            boolean autoApproveCA = false;
                                                            BRANCH[][] rsBranch = new BRANCH[1][];
                                                            db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                            if (rsBranch[0].length > 0) {
                                                                String sCERTIFICATION_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                                                                if(!"".equals(sCERTIFICATION_POLICY_PROPERTIES)) {
                                                                    autoApproveCA = CommonFunction.getApproveEnabledCert(sCERTIFICATION_POLICY_PROPERTIES);
                                                                }
                                                            }
                                                            CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                            db.S_BO_CERTIFICATION_PROFILE_DETAIL(pCERTIFICATION_PROFILE_ID, rsProfile);
                                                            CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
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
                                                                }
                                                            } else {
                                                                strIsPushNotiApprove = "1";
                                                            }
                                                            //</editor-fold>
                                                        }
                                                    }
                                                    //</editor-fold>
                                                }
                                                sessionsa.setAttribute("RefreshCertShareSess", "1");
                                                strView = "0#0#" + strIsPushNotiApprove;
                                            } else {
                                                strView = sParam + "#0";
                                            }
                                            //</editor-fold>
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
