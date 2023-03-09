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
import vn.ra.object.BACKOFFICE_USER;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.object.CERTIFICATION_COMMENT;
import vn.ra.object.CERTIFICATION_DATA_ATTR;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.CERTIFICATION_PROPERTIES_JSON;
import vn.ra.object.CredentialDataAuthen;
import vn.ra.object.DISCOUNT_RATE_PROFILE;
import vn.ra.object.FILE_PROFILE_DATA;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PKI_FORMFACTOR;
import vn.ra.object.PROFILE_DISCOUNT_RATE_DATA;
import vn.ra.object.RESPONSE_CODE;
import vn.ra.object.RESPONSE_LOG;
import vn.ra.object.ROLE_DATA;
import vn.ra.object.TOKEN;
import vn.ra.process.CommonFunction;
import vn.ra.process.CommonReferServlet;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.ConnectDbPhaseTwo;
import vn.ra.process.ConnectFileToPartner;
import vn.ra.process.ConnectJackRabbitNew;
import vn.ra.process.DESEncryption;
import vn.ra.process.JackRabbitCommon;
import vn.ra.process.RSSPProcessCommon;
import vn.ra.process.SessionUploadFileCert;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;
import vn.ra.process.EncodeSOPIN;
import vn.ra.utility.LoadParamSystem;
import vn.ra.object.ProfileContactInfoJson;
import vn.ra.thread.ThreadCallbackApproved;

/**
 *
 * @author USER
 */
public class ReqApproveDeclineCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReqApproveDeclineCommon.class.getName());
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
                    String loginFullname = request.getSession(false).getAttribute("sesFullname").toString().trim();
                    String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                    String sIP_Request = CommonFunction.getClientIpLogin(request);
                    DESEncryption seEncript = new DESEncryption();
                    CERTIFICATION_DATA_ATTR tempLogReq;
                    if (null != idParam) {
                        switch (idParam) {
                            case "approvecertchildrenagency": {
                                //<editor-fold defaultstate="collapsed" desc="approvecertchildrenagency">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String sAGENT_ID_New = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ID"));
                                    String sUSER_ID_New = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                    String CheckCHANGE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckCHANGE_KEY"));
                                    String CheckPRIVATE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckPRIVATE_KEY"));
                                    String CheckREVOKE_ENABLED = EscapeUtils.CheckTextNull(request.getParameter("CheckREVOKE_ENABLED"));
                                    String keepCertSNEnabled = EscapeUtils.CheckTextNull(request.getParameter("keepCertSNEnabled"));
                                    String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                    CommonFunction.LogDebugString(log, "APPROVE-AGENCY", "AGENCY: " + sAGENT_ID_New + "; USER: " + sUSER_ID_New);
                                    String sAGENT_ID="";
                                    String sUSER_ID="";
                                    String sTOKEN_SN = "";
                                    String sVALUE_OLD = "";
                                    String sCSR = "";
                                    String strEmailCust = "";
                                    String pCERTIFICATION_SN = "";
                                    String pCERTIFICATION_AUTHORITY_ID = "";
                                    String strReqValueATTR = "";
                                    boolean sPUSH_NOTICE_ENABLED = false;
                                    int sCERTIFICATION_ID = 0;
                                    int sCERTIFICATION_ATTR_TYPE_ID = 0;
                                    int pPKI_FORMFACTOR_ID = 0;
                                    String sProfileIDBrowser = EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    if(!"".equals(sProfileIDBrowser)) {
                                        pCERTIFICATION_PROFILE_ID = Integer.parseInt(sProfileIDBrowser);
                                    }
                                    int sStatusRequest = 0;
                                    boolean pPRIVATE_KEY_ENABLED = true;
                                    // check agency
                                    boolean isAccessAgency = true;
                                    String pPAST_CERTIFICATE_ID = "";
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sUSER_ID = String.valueOf(rsReq[0][0].CREATED_BY_ID);
                                        sStatusRequest = rsReq[0][0].CERTIFICATION_ATTR_STATE_ID;
                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        pPAST_CERTIFICATE_ID = String.valueOf(rsReq[0][0].PAST_CERTIFICATION_ID);
                                        strEmailCust = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                        //pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        sCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                        sCERTIFICATION_ID = rsReq[0][0].ID;
                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        sCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                        sPUSH_NOTICE_ENABLED = rsReq[0][0].PUSH_NOTICE_ENABLED;
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        String strPasswordP12 = "";
                                        ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                        if ((sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                                && !AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            boolean functionAccess = false;
                                            //<editor-fold defaultstate="collapsed" desc="### CHECK ACCESS FUNCTION">
                                            String SessRoleID = sessionsa.getAttribute("RoleID_ID").toString().trim();
                                            String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                            if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT))
                                            {
                                                if(!SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                    int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                    if(intApprove == 1) {
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                        {
                                                            functionAccess = true;
                                                        }
                                                    }
                                                }
                                            } else {
                                                if(!SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                    int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(ID), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                    if(intApprove == 1) {
                                                        functionAccess = true;
                                                    }
                                                }
                                            }
                                            //</editor-fold>
                                            
                                            if(functionAccess == true)
                                            {
                                                boolean isCSRValid = true;
                                                boolean isCSR_SizeValid = true;
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
                                                            sCSR = EscapeUtils.CheckTextNull(request.getParameter("CSR"));
                                                            if("".equals(sCSR))
                                                            {
                                                                isCSRValid = false;
                                                            }
                                                            if(isCSRValid == true)
                                                            {
                                                                String sKeySizeDB;
                                                                isCSR_SizeValid = false;
                                                                CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                                                db.S_BO_GET_ALGORITHM_KEY_SIZE(String.valueOf(pCERTIFICATION_PROFILE_ID), rsCert);
                                                                if(rsCert[0].length > 0)
                                                                {
                                                                    sKeySizeDB = EscapeUtils.CheckTextNull(rsCert[0][0].KEY_SIZE);
                                                                    String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(sCSR);
                                                                    isCSR_SizeValid = sKeySizeDB.equals(sKeySizeCSR);
                                                                }
                                                            }
                                                        }
                                                        CheckPRIVATE_KEY = "0";
                                                    }
                                                }
                                                if(isCSRValid == true)
                                                {
                                                    if(isCSR_SizeValid == true) {
                                                        //<editor-fold defaultstate="collapsed" desc="### PARAM and GET INFO">
                                                        String PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                                        String EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
                                                        String CACoreSubject = EscapeUtils.CheckTextNull(request.getParameter("CACoreSubject"));
                                                        String CertProfileID = sProfileIDBrowser;//EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                                        String DN = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                                        String pPERSONAL_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pPERSONAL_NAME"));
                                                        String pCOMPANY_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pCOMPANY_NAME"));
                                                        String pDOMAIN_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pDOMAIN_NAME"));
                                                        String pTAX_CODE = "";//EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                                        String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                                        String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                                        String pDECISION = "";//EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                                        String pBUDGET_CODE = "";//EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                                        String pP_ID = "";//EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                                        String pCCCD = "";//EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                                        String pPASSPORT = "";//EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                                        String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                                        String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
                                                        String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION);
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                                            if("0".equals(CheckCHANGE_KEY)) {
                                                                CERTIFICATION[][] rsCertOld = new CERTIFICATION[1][];
                                                                db.S_BO_CERTIFICATION_DETAIL(pPAST_CERTIFICATE_ID, sessLanguage, rsCertOld);
                                                                if(rsCertOld[0].length > 0) {
                                                                    DN = EscapeUtils.CheckTextNull(rsCertOld[0][0].SUBJECT);
                                                                    pPERSONAL_NAME = EscapeUtils.CheckTextNull(rsCertOld[0][0].PERSONAL_NAME);
                                                                    pCOMPANY_NAME = EscapeUtils.CheckTextNull(rsCertOld[0][0].COMPANY_NAME);
                                                                    pDOMAIN_NAME = EscapeUtils.CheckTextNull(rsCertOld[0][0].DOMAIN_NAME);
                                                                    pENTERPRISE_ID = rsCertOld[0][0].ENTERPRISE_ID;
                                                                    pPERSONAL_ID = rsCertOld[0][0].PERSONAL_ID;
//                                                                    pTAX_CODE = EscapeUtils.CheckTextNull(rsCertOld[0][0].TAX_CODE);
//                                                                    pDECISION = EscapeUtils.CheckTextNull(rsCertOld[0][0].DECISION);
//                                                                    pBUDGET_CODE = EscapeUtils.CheckTextNull(rsCertOld[0][0].BUDGET_CODE);
//                                                                    pP_ID = EscapeUtils.CheckTextNull(rsCertOld[0][0].P_ID);
//                                                                    pCCCD = EscapeUtils.CheckTextNull(rsCertOld[0][0].P_EID);
//                                                                    pPASSPORT = EscapeUtils.CheckTextNull(rsCertOld[0][0].PASSPORT);
                                                                    pPROVINCE_ID = String.valueOf(rsCertOld[0][0].CITY_PROVINCE_ID);
                                                                    pDEVICE = EscapeUtils.CheckTextNull(rsCertOld[0][0].SERVICE_UUID);
                                                                }
                                                            }
                                                        }
//                                                        String sEnterpriseCert = "";
//                                                        String sPersonalCert = "";
//                                                        if(!"".equals(pTAX_CODE)) {
//                                                            sEnterpriseCert = LoadParamSystem.getParamStart(Definitions.CONFIG_PARAM_PREFIX_ENTERPRISE_TAX_CODE) + ":" + pTAX_CODE;
//                                                        }
//                                                        if(!"".equals(pBUDGET_CODE)) {
//                                                            sEnterpriseCert = LoadParamSystem.getParamStart(Definitions.CONFIG_PARAM_PREFIX_ENTERPRISE_BUDGET_CODE) + ":" + pBUDGET_CODE;
//                                                        }
//                                                        if(!"".equals(pDECISION)) {
//                                                            sEnterpriseCert = LoadParamSystem.getParamStart(Definitions.CONFIG_PARAM_PREFIX_ENTERPRISE_DECISION) + ":" + pDECISION;
//                                                        }
//                                                        if(!"".equals(pP_ID)) {
//                                                            sPersonalCert = LoadParamSystem.getParamStart(Definitions.CONFIG_PARAM_PREFIX_PERSONAL_CODE) + ":" + pP_ID;
//                                                        }
//                                                        if(!"".equals(pPASSPORT)) {
//                                                            sPersonalCert = LoadParamSystem.getParamStart(Definitions.CONFIG_PARAM_PREFIX_PERSONAL_PASSPORT_CODE) + ":" + pPASSPORT;
//                                                        }
//                                                        if(!"".equals(pCCCD)) {
//                                                            sPersonalCert = LoadParamSystem.getParamStart(Definitions.CONFIG_PARAM_PREFIX_PERSONAL_CITIZEN_CODE) + ":" + pCCCD;
//                                                        }
                                                        CommonFunction.LogDebugString(log, "REGISTRATION-CERTIFICATION", "REQUEST: " + "SUBJECT: " + DN
                                                                + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME
                                                                + "; pDOMAIN_NAME: " + pDOMAIN_NAME + "; pENTERPRISE_ID: " + pENTERPRISE_ID + "; pPERSONAL_ID: " + pPERSONAL_ID
                                                                + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID + "; pCERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID
                                                                + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT + "; CACoreSubject: " + CACoreSubject + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                                + "; PROVINCE_ID: " + pPROVINCE_ID + "; Cert_ProfileID: " + CertProfileID + "; TOKEN_SN: " + sTOKEN_SN);
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
                                                        tempLogReq.provinceName = "ID refer: " + pPROVINCE_ID;
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_BUY_MORE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_COMPENSATION)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_COMPENSATION;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED;
                                                        }
                                                        //</editor-fold>
                                                        String strReq = objectMapper.writeValueAsString(tempLogReq);
                                                        db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME, Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                            Definitions.CONFIG_LOG_FUNCTIONALITY_IN_AGENCY_APPROVED, strReq, loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                        if (sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                                        {
                                                            db.S_BO_CERTIFICATION_UPDATE(sCERTIFICATION_ID, CertProfileID, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME,
                                                                pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, DN, CACoreSubject, PHONE_CONTRACT,
                                                                EMAIL_CONTRACT, pPROVINCE_ID, CheckPRIVATE_KEY, loginUID, "", sCSR, pDEVICE, "", "",
                                                                pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                        }
                                                        ATTRIBUTE_VALUES valueATTR_Last = null;
                                                        String idCERT_REVOCATION_REASON = "";
                                                        String sCertRevokeReason_Frist = "";
                                                        java.sql.Timestamp sCertSuspendTime_Frist = null;
                                                        String sCertSuspendReason_Frist = "";
                                                        boolean isDeleteCertInToken = true;
                                                        //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR">
                                                        if (!"".equals(sVALUE_OLD)) {
                                                            // VALUE ATTR_FRIST
                                                            ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                            String sToken_Frist = valueATTR_Frist.getTokenSn();
                                                            sCertRevokeReason_Frist = valueATTR_Frist.getCerttificateRevokeReason();
                                                            idCERT_REVOCATION_REASON = valueATTR_Frist.getCerttificateRevokeEJBCAReason();
                                                            sCertSuspendReason_Frist = valueATTR_Frist.getCerttificateSuspendReason();
                                                            isDeleteCertInToken = valueATTR_Frist.getCertRevokeDeleteInTokenEnabled();
                                                            sCertSuspendTime_Frist = valueATTR_Frist.getSuspendedTime();
                                                            String sTypeName_Frist = valueATTR_Frist.getTypeName();
                                                            String sCreateUser_Frist = valueATTR_Frist.getCreateUser();
                                                            String sRsspAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                            String sRsspRelyingParty = valueATTR_Frist.getRsspRelyingParty();
                                                            Date sCreateDt_Frist = valueATTR_Frist.getCreateDt();
                                                            // VALUE ATTR_LAST
                                                            valueATTR_Last = new ATTRIBUTE_VALUES();
                                                            ATTRIBUTE_DATA dataATTR_Last = new ATTRIBUTE_DATA();
                                                            dataATTR_Last.setCertificationData(tempLogReq);
                                                            valueATTR_Last.setTokenSn(sToken_Frist);
                                                            valueATTR_Last.setRsspAgreementUUID(sRsspAgreementUUID);
                                                            valueATTR_Last.setRsspRelyingParty(sRsspRelyingParty);
                                                            boolean sChangeKeyEnabled = "1".equals(CheckCHANGE_KEY);
                                                            valueATTR_Last.setChangeKeyEnabled(sChangeKeyEnabled);
                                                            boolean sRevokedEnabled = "1".equals(CheckREVOKE_ENABLED);
                                                            valueATTR_Last.setRevokeOldCertificateEnabled(sRevokedEnabled);
                                                            boolean booKeepCertSNEnabled = "1".equals(keepCertSNEnabled);
                                                            valueATTR_Last.setKeepCertificateSNEnabled(booKeepCertSNEnabled);
                                                            valueATTR_Last.setCerttificateRevokeReason(sCertRevokeReason_Frist);
                                                            valueATTR_Last.setCerttificateRevokeEJBCAReason(idCERT_REVOCATION_REASON);
                                                            valueATTR_Last.setCerttificateSuspendReason(sCertSuspendReason_Frist);
                                                            valueATTR_Last.setCertRevokeDeleteInTokenEnabled(isDeleteCertInToken);
                                                            valueATTR_Last.setCerttificateDeclineReason("");
                                                            valueATTR_Last.setSuspendedTime(sCertSuspendTime_Frist);
                                                            valueATTR_Last.setTypeName(sTypeName_Frist);
                                                            valueATTR_Last.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_IN_AGENCY_APPROVED);
                                                            valueATTR_Last.setCreateUser(sCreateUser_Frist);
                                                            valueATTR_Last.setCreateDt(sCreateDt_Frist);
                                                            valueATTR_Last.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR_Last.setApproveDt(new Date());
                                                            valueATTR_Last.setAttributeData(dataATTR_Last);
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                            // VALUE ATTR
                                                        }
                                                        //</editor-fold>

                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                                        {
                                                            if(!sUSER_ID.equals(sUSER_ID_New))
                                                            {
                                                                if(!sUSER_ID.equals(sUSER_ID_New)) {
                                                                    String strReqValueATTRForDecline = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                    db.S_BO_CERTIFICATION_CHANGED_CREATED_BY(ID, sAGENT_ID, sUSER_ID_New, loginUID, strReqValueATTRForDecline);
                                                                }
                                                            }
                                                        }
                                                        String userLoginID = request.getSession(false).getAttribute("UserID").toString().trim();
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
                                                        String sParam = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(Integer.parseInt(ID), strReqValueATTR, loginUID, approveChilrenID);
                                                        if("0".equals(sParam))
                                                        {
                                                            Config conf = new Config();
                                                            //<editor-fold defaultstate="collapsed" desc="### REPRESENTIVE PROCESS">
                                                            String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
                                                            String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                            String userID = request.getSession(false).getAttribute("UserID").toString().trim();
                                                            if("1".equals(sRepresentEnabled)) {
                                                                if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                    || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                    || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
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
                                                                    db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(sCERTIFICATION_ID, objectMapper.writeValueAsString(profileContact), userID);
                                                                }
                                                            }
                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)){
                                                                String registerNCRepresentative = EscapeUtils.CheckTextNull(request.getParameter("registerNCRepresentative"));
                                                                String registerNCAddress = EscapeUtils.CheckTextNull(request.getParameter("registerNCAddress"));
                                                                ProfileContactInfoJson profileContact = new ProfileContactInfoJson();
                                                                profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(registerNCRepresentative, true);
                                                                profileContact.Address = CommonFunction.replaceCharaterSpecialJson(registerNCAddress, true);
                                                                objectMapper = new ObjectMapper();
                                                                db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(sCERTIFICATION_ID, objectMapper.writeValueAsString(profileContact), userID);
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
                                                                    db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(sCERTIFICATION_ID), strDNSName, loginUID);
                                                                }
                                                            }
                                                            //</editor-fold>

                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                            strView = "0#0";

                                                            //<editor-fold defaultstate="collapsed" desc="### APPROVE AGENCY - CA PROCESS">
                                                            boolean autoApproveAgency = false;
                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                            {
                                                                autoApproveAgency = true;
                                                            }
                                                            if(autoApproveAgency == true)
                                                            {
                                                                String paramPreApprove = db.S_BO_CERTIFICATION_PRE_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                if("0".equals(paramPreApprove)) {
                                                                    boolean autoApproveCA = false;
                                                                    //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                                    CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                    String sProfileCode = "";
                                                                    String pCERT_ATTR_TYPE_CODE = "";
                                                                    if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED
                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                                    {
                                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED;
                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE;
                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE;
                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE;
                                                                        }
                                                                        if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                                        {
                                                                            autoApproveCA = true;
                                                                        }
                                                                    } else {
                                                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(pCERTIFICATION_PROFILE_ID), rsProfile);
                                                                        if(rsProfile[0].length > 0) {
                                                                            sProfileCode = EscapeUtils.CheckTextNull(rsProfile[0][0].NAME);
                                                                            autoApproveCA = CommonFunction.getApproveCAOfProfile(sProfileCode, sessPolicyCert_Data);
                                                                            if(autoApproveCA == true) {
                                                                                if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE) {
                                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_BUY_MORE;
                                                                                }
                                                                                autoApproveCA = CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data);
                                                                            }
                                                                        }
                                                                    }
                                                                    if(autoApproveCA == true)
                                                                    {
                                                                        boolean booRSSP_ACCESS_ENABLED = false;
                                                                        if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                                            String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                                                            if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                                                booRSSP_ACCESS_ENABLED = true;
                                                                                if (!"".equals(sVALUE_OLD)) {
                                                                                    objectMapper = new ObjectMapper();
                                                                                    ATTRIBUTE_VALUES valueRSSP = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                                    if(valueRSSP.getTokenSn().trim().equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN)) {
                                                                                        booRSSP_ACCESS_ENABLED = false;
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        if(booRSSP_ACCESS_ENABLED == false) {
                                                                            String sDiscountRate = "0";
                                                                            String sDiscountRateOption = "0";
                                                                            String CheckDeleteCertForOtherRevoke = "0";
                                                                            String CheckDeleteCertForRevoke = "0";
                                                                            GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                            if (sessGeneralPolicy1[0].length > 0) {
                                                                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                {
                                                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                                                                                    {
                                                                                        sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                    }
                                                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DELETE_OLD_CERTIFICATE))
                                                                                    {
                                                                                        CheckDeleteCertForOtherRevoke = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                    }
                                                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_DELETE_CERT_WHEN_REVOKE))
                                                                                    {
                                                                                        CheckDeleteCertForRevoke = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                    }
                                                                                }
                                                                            }
                                                                            if("1".equals(sDiscountRateOption)) {
                                                                                if(sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                                    || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                                                                    || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                                                                                {
                                                                                    if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL)
                                                                                    {
                                                                                        DISCOUNT_RATE_PROFILE[][] rsDisCount = new DISCOUNT_RATE_PROFILE[1][];
                                                                                        db.S_BO_BRANCH_GET_DISCOUNT_RATE_PROFILE(sAGENT_ID, rsDisCount);
                                                                                        if(rsDisCount[0].length > 0 && !"".equals(rsDisCount[0][0].PROPERTIES))
                                                                                        {
                                                                                            PROFILE_DISCOUNT_RATE_DATA[][] resIPData = new PROFILE_DISCOUNT_RATE_DATA[1][];
                                                                                            CommonFunction.getAllProfileDiscountRate(rsDisCount[0][0].PROPERTIES, resIPData);
                                                                                            if(resIPData[0] != null && resIPData[0].length > 0)
                                                                                            {
                                                                                                for(PROFILE_DISCOUNT_RATE_DATA resIPData1 : resIPData[0])
                                                                                                {
                                                                                                    if(EscapeUtils.CheckTextNull(resIPData1.profileName).equals(sProfileCode))
                                                                                                    {
                                                                                                        sDiscountRate = resIPData1.rosePercent;
                                                                                                        break;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            valueATTR_Last.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR_Last.setApproveCADt(new Date());
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                            if (sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                                                                idCERT_REVOCATION_REASON = String.valueOf(Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID);
                                                                                if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                                    || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                                    CheckDeleteCertForRevoke = "0";
                                                                                }
                                                                                valueATTR_Last.setCerttificateRevokeEJBCAReason(idCERT_REVOCATION_REASON);
                                                                                boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteCertForRevoke);
                                                                                valueATTR_Last.setCertRevokeDeleteInTokenEnabled(sRevokeDeleteInTokenEnabled);
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), CommonFunction.GenJSONTokenATTR(valueATTR_Last), loginUID);
                                                                                if ("0".equals(sApprove)) {
                                                                                    String pCA_NAME = "";
                                                                                    CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                                    db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                                    if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                                        pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                                    }
                                                                                    int[] intRes = new int[1];
                                                                                    String[] sRes = new String[1];
                                                                                    ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Integer.parseInt(idCERT_REVOCATION_REASON),
                                                                                        pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                                    if (intRes[0] == 0) {
                                                                                        db.S_BO_CERTIFICATION_REVOKED(Integer.parseInt(ID), Integer.parseInt(CheckDeleteCertForRevoke), loginUID);
                                                                                        //<editor-fold defaultstate="collapsed" desc="### REVOKE REASON UPDATE">
                                                                                        objectMapper = new ObjectMapper();
                                                                                        CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                                        jsonCertComment.certificateDeclineReason = "";
                                                                                        jsonCertComment.certificateSuspendReason = "";
                                                                                        jsonCertComment.certificateRevokeReason = sCertRevokeReason_Frist;
                                                                                        sCertRevokeReason_Frist = objectMapper.writeValueAsString(jsonCertComment);
                                                                                        db.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(String.valueOf(sCERTIFICATION_ID), sCertRevokeReason_Frist, loginUID);
                                                                                        //</editor-fold>

                                                                                        request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                        strView = "0#0";
                                                                                        
                                                                                        String urlCallback = "";
                                                                                        String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE;
                                                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                        if(rsBranch != null && rsBranch[0].length > 0){
                                                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                        }
                                                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                        Thread t = new Thread(thhreadLog);
                                                                                        t.start();
                                                                                    } else {
                                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                    }
                                                                                } else {
                                                                                    strView = "1#0";
                                                                                }
                                                                            } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                                            {
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
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
                                                                                        pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                                    if (intRes[0] == 0) {
                                                                                        String pCOMMENT = "";
                                                                                        //<editor-fold defaultstate="collapsed" desc="### SUSPEND REASON UPDATE">
                                                                                        objectMapper = new ObjectMapper();
                                                                                        CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                                        jsonCertComment.certificateDeclineReason = "";
                                                                                        jsonCertComment.certificateRevokeReason = "";
                                                                                        jsonCertComment.certificateSuspendReason = sCertSuspendReason_Frist;
                                                                                        pCOMMENT = objectMapper.writeValueAsString(jsonCertComment);
                                                                                        //</editor-fold>

                                                                                        db.S_BO_CERTIFICATION_DISABLE(ID, sCertSuspendTime_Frist, pCOMMENT, loginUID);
                                                                                        sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                                                        strView = "0#0";
                                                                                        String urlCallback = "";
                                                                                        String requestType = "";
                                                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE) {
                                                                                            requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE;
                                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE) {
                                                                                            requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE;
                                                                                        }
                                                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                        if(rsBranch != null && rsBranch[0].length > 0){
                                                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                        }
                                                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                        Thread t = new Thread(thhreadLog);
                                                                                        t.start();
                                                                                    } else {
                                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                    }
                                                                                } else {
                                                                                    strView = "1#0";
                                                                                }
                                                                            } else if (sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                                                                            {
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
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
                                                                                    ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_REMOVEFROMCRL_ID,
                                                                                        pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                                    if (intRes[0] == 0) {
                                                                                        db.S_BO_CERTIFICATION_RECOVERED(ID, loginUID);
                                                                                        sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                                                        strView = "0#0";
                                                                                        String urlCallback = "";
                                                                                        String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED;
                                                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                        if(rsBranch != null && rsBranch[0].length > 0){
                                                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                        }
                                                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                        Thread t = new Thread(thhreadLog);
                                                                                        t.start();
                                                                                    } else {
                                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                    }
                                                                                } else {
                                                                                    strView = "1#0";
                                                                                }
                                                                            } else if (sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                                                                if(CommonFunction.checkApproveCAReqType(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE, sessPolicyCert_Data) == true)
                                                                                {
                                                                                    db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                                    if(!"0".equals(sDiscountRate)) {
                                                                                        db.S_BO_CERTIFICATION_UPDATE(sCERTIFICATION_ID, CertProfileID, "", "", "",
                                                                                            pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, "", "", "",
                                                                                            "", "", "", loginUID, "", "", "", sDiscountRate, "", pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                                    }
                                                                                    if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN))
                                                                                    {
                                                                                        if (sPUSH_NOTICE_ENABLED == true) {
                                                                                            // RA SEND_EMAIL
                                                                                            int[] intRes = new int[1];
                                                                                            String[] sRes = new String[1];
                                                                                            ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                                        }
                                                                                    }
                                                                                    if (pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                                        String sOLD_TOKEN_ID = "";
                                                                                        CERTIFICATION[][] rsGetTokenID = new CERTIFICATION[1][];
                                                                                        db.S_BO_CERTIFICATION_DETAIL(pPAST_CERTIFICATE_ID, sessLanguage, rsGetTokenID);
                                                                                        if (rsGetTokenID[0].length > 0) {
                                                                                            sOLD_TOKEN_ID = String.valueOf(rsGetTokenID[0][0].TOKEN_ID);
                                                                                        }
                                                                                        TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                                                        int sTOKEN_STATE_ID_OLD = 0;
                                                                                        String sTOKEN_SN_OLD = "";
                                                                                        db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsToken_OLD);
                                                                                        if (rsToken_OLD[0].length > 0) {
                                                                                            sTOKEN_STATE_ID_OLD = rsToken_OLD[0][0].TOKEN_STATE_ID;
                                                                                            sTOKEN_SN_OLD = EscapeUtils.CheckTextNull(rsToken_OLD[0][0].TOKEN_SN);
                                                                                        }
                                                                                        if (sTOKEN_STATE_ID_OLD != Definitions.CONFIG_TOKEN_STATE_ID_PERMANENT_INITIALZED) {
                                                                                            ATTRIBUTE_VALUES valueATTR_TOKEN;
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
                                                                                        }
                                                                                    } else {
                                                                                        String urlCallback = "";
                                                                                        String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                        if(rsBranch != null && rsBranch[0].length > 0){
                                                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                        }
                                                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                        Thread t = new Thread(thhreadLog);
                                                                                        t.start();
                                                                                    }
                                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                    strView = "0#0";
                                                                                }
                                                                            } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                                if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE) {
                                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_BUY_MORE;
                                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                                                }
                                                                                if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                                                {
                                                                                    if(!"0".equals(sDiscountRate)) {
                                                                                        db.S_BO_CERTIFICATION_UPDATE(sCERTIFICATION_ID, CertProfileID, "", "", "",
                                                                                            pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, "", "", "",
                                                                                            "", "", "", loginUID, "", "", "", sDiscountRate, "", pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                                    }
                                                                                    if (sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                                        || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                                                                        || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED) {
                                                                                        if (sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                                            String FEE_AMOUNT = EscapeUtils.CheckTextNull(request.getParameter("sFEE_AMOUNT"));
                                                                                            db.S_BO_CERTIFICATION_UPDATE_AMOUNT(sCERTIFICATION_ID, FEE_AMOUNT.replace(",", ""), "", loginUID);
                                                                                        }
                                                                                    }
                                                                                    if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                                        || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                                        CheckDeleteCertForOtherRevoke = "0";
                                                                                    }
                                                                                    if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                                                                                    {
                                                                                        CheckDeleteCertForOtherRevoke = "0";
                                                                                    }
                                                                                    boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteCertForOtherRevoke);
                                                                                    valueATTR_Last.setDeleteOldCertificateEnabled(sRevokeDeleteInTokenEnabled);
                                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                                    String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                                    if ("0".equals(sApprove)) {
                                                                                        // set COMMIT TRUE File Attachment
                                                                                        String[] pRESPONSE_CODE_FILE = new String[1];
                                                                                        db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(Integer.parseInt(ID), loginUID, pRESPONSE_CODE_FILE);
                                                                                        if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN))
                                                                                        {
                                                                                            if (sPUSH_NOTICE_ENABLED == true) {
                                                                                                int[] intRes = new int[1];
                                                                                                String[] sRes = new String[1];
                                                                                                ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                                            }
                                                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                            strView = "0#0";
                                                                                        } else if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                                            || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN)
                                                                                            || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN)
                                                                                            || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                                            //<editor-fold defaultstate="collapsed" desc="### DNS LIST for SSL">
                                                                                            if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN)) {
                                                                                                if(!"".equals(strDNSName)) {
                                                                                                    request.getSession(false).setAttribute("sessDNSNameForSSL_Approve", null);
                                                                                                    db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(sCERTIFICATION_ID), strDNSName, loginUID);
                                                                                                }
                                                                                            }
                                                                                            //</editor-fold>
                                                                                            
                                                                                            if(pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                                                String isActiveSignServer = "0";
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
                                                                                                    CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(sCERTIFICATION_ID), DN, strEmailCust, sessLanguage);
                                                                                                    strView = "0#0";
                                                                                                } else {
                                                                                                    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                                    dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(Integer.parseInt(ID), 1);
                                                                                                    int[] intWSRes = new int[1];
                                                                                                    String[] sWSRes = new String[1];
                                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, ID, intWSRes, sWSRes);
                                                                                                    if (intWSRes[0] == 0) {
                                                                                                        request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                                        strView = "0#0";
                                                                                                    } else {
                                                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                                    }
                                                                                                }
                                                                                            } else {
                                                                                                request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                                strView = "0#0";
                                                                                                String urlCallback = "";
                                                                                                String requestType = "";
                                                                                                if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                                                                                    requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                                                    requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                                                    requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                                                                }
                                                                                                BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                                db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                                if(rsBranch != null && rsBranch[0].length > 0){
                                                                                                    urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                                }
                                                                                                ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                                    urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                                Thread t = new Thread(thhreadLog);
                                                                                                t.start();
                                                                                            }
                                                                                        } else {
                                                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                            strView = "0#0";
                                                                                        }
                                                                                    } else {
                                                                                        strView = sApprove + "#0";
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                                if ("0".equals(sApprove)) {
                                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                    strView = "0#0";
                                                                                } else {
                                                                                    strView = sApprove + "#0";
                                                                                }
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
                                                                                RSSPProcessCommon clsRSSP = new RSSPProcessCommon();
                                                                                String[] sResultRSSP = new String[2];
                                                                                String sAgreementUUID = "";
                                                                                String sRsspRelyingParty = "";
                                                                                String sChangeKeyApprove = "";
                                                                                boolean revokeSetOldStatus = false;
                                                                                if (!"".equals(sVALUE_OLD)) {
                                                                                    ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                                    sAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                                                    sRsspRelyingParty = valueATTR_Frist.getRsspRelyingParty();
                                                                                    revokeSetOldStatus = valueATTR_Frist.getRevokeSetOldStatusEnabled();
                                                                                    if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                                                                                    {
                                                                                        sChangeKeyApprove = valueATTR_Frist.getChangeKeyEnabled() ? "1" : "0";
                                                                                    }
                                                                                }
                                                                                if(!"".equals(sAgreementUUID)) {
                                                                                    if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                                                                                    {
                                                                                        rsReq = new CERTIFICATION[1][];
                                                                                        db.S_BO_CERTIFICATION_DETAIL(pPAST_CERTIFICATE_ID, sessLanguage, rsReq);
                                                                                        if (rsReq[0].length > 0) {
                                                                                            pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                                                                        }
                                                                                    }
                                                                                    String sRequestTypeRssp = "";
                                                                                    if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL){
                                                                                        sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_RENEW_CERT;
                                                                                    } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO){
                                                                                        sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_CHANGE_CERT;
                                                                                    } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE){
                                                                                        sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_REVOKE_CERT;
                                                                                    } else {
                                                                                        sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_PREPARE_CERT;
                                                                                    }
                                                                                    clsRSSP.approveCertificateForSignCloud(sAgreementUUID, String.valueOf(sCERTIFICATION_ID),
                                                                                        CheckREVOKE_ENABLED, sRsspRelyingParty, sResultRSSP, pCERTIFICATION_SN, sChangeKeyApprove,
                                                                                        sRequestTypeRssp, "", credentialAuthen, revokeSetOldStatus);
                                                                                    if("0".equals(sResultRSSP[0])) {
                                                                                        request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                        strView = "0#0";
                                                                                        String[] pRESPONSE_CODE_FILE = new String[1];
                                                                                        db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(Integer.parseInt(ID), loginUID, pRESPONSE_CODE_FILE);
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
                                                                    }
                                                                    //</editor-fold>
                                                                }
                                                            }
                                                            //</editor-fold>
                                                        } else {
                                                            strView = sParam+"#0";
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_CSR_KEYSIZE + "#0";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_CSR_NULL + "#0";
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_DENIED_FUNCTION + "#0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_HAS_STATUS + "#0";
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
                            case "approvecertagency": {
                                //<editor-fold defaultstate="collapsed" desc="approvecertagency">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String sAGENT_ID_New = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ID"));
                                    String sUSER_ID_New = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                    String CheckCHANGE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckCHANGE_KEY"));
                                    String CheckPRIVATE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckPRIVATE_KEY"));
                                    String CheckREVOKE_ENABLED = EscapeUtils.CheckTextNull(request.getParameter("CheckREVOKE_ENABLED"));
                                    String keepCertSNEnabled = EscapeUtils.CheckTextNull(request.getParameter("keepCertSNEnabled"));
                                    String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                    CommonFunction.LogDebugString(log, "APPROVE-AGENCY", "AGENCY: " + sAGENT_ID_New + "; USER: " + sUSER_ID_New);
                                    String sAGENT_ID="";
                                    String sUSER_ID="";
                                    String sTOKEN_SN = "";
                                    String sVALUE_OLD = "";
                                    String sCSR = "";
                                    String strEmailCust = "";
                                    String pCERTIFICATION_SN = "";
                                    String pCERTIFICATION_AUTHORITY_ID = "";
                                    String strReqValueATTR = "";
                                    boolean sPUSH_NOTICE_ENABLED = false;
                                    int sCERTIFICATION_ID = 0;
                                    int sCERTIFICATION_ATTR_TYPE_ID = 0;
                                    int pPKI_FORMFACTOR_ID = 0;
                                    String sProfileIDBrowser = EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    if(!"".equals(sProfileIDBrowser)) {
                                        pCERTIFICATION_PROFILE_ID = Integer.parseInt(sProfileIDBrowser);
                                    }
                                    int sStatusRequest = 0;
                                    boolean pPRIVATE_KEY_ENABLED = true;
                                    // check agency
                                    boolean isAccessAgency = true;
                                    String pPAST_CERTIFICATE_ID = "";
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sUSER_ID = String.valueOf(rsReq[0][0].CREATED_BY_ID);
                                        sStatusRequest = rsReq[0][0].CERTIFICATION_ATTR_STATE_ID;
                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        pPAST_CERTIFICATE_ID = String.valueOf(rsReq[0][0].PAST_CERTIFICATION_ID);
                                        sCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                        sCERTIFICATION_ID = rsReq[0][0].ID;
                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        strEmailCust = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                        sCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                        sPUSH_NOTICE_ENABLED = rsReq[0][0].PUSH_NOTICE_ENABLED;
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        String strPasswordP12 = "";
                                        if ((sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                                && !AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            boolean functionAccess = false;
                                            //<editor-fold defaultstate="collapsed" desc="### CHECK ACCESS FUNCTION">
                                            String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                            ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                            String SessRoleID = sessionsa.getAttribute("RoleID_ID").toString().trim();
                                            if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT))
                                            {
                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                    {
                                                        functionAccess = true;
                                                    }
                                                }
                                            } else {
                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                    functionAccess = true;
                                                }
                                            }
                                            //</editor-fold>
                                            
                                            if(functionAccess == true)
                                            {
                                                boolean isCSRValid = true;
                                                boolean isCSR_SizeValid = true;
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
                                                            sCSR = EscapeUtils.CheckTextNull(request.getParameter("CSR"));
                                                            if("".equals(sCSR))
                                                            {
                                                                isCSRValid = false;
                                                            }
                                                            if(isCSRValid == true)
                                                            {
                                                                String sKeySizeDB;
                                                                isCSR_SizeValid = false;
                                                                CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                                                db.S_BO_GET_ALGORITHM_KEY_SIZE(String.valueOf(pCERTIFICATION_PROFILE_ID), rsCert);
                                                                if(rsCert[0].length > 0)
                                                                {
                                                                    sKeySizeDB = EscapeUtils.CheckTextNull(rsCert[0][0].KEY_SIZE);
                                                                    String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(sCSR);
                                                                    isCSR_SizeValid = sKeySizeDB.equals(sKeySizeCSR);
                                                                }
                                                            }
                                                        }
                                                        CheckPRIVATE_KEY = "0";
                                                    }
                                                }
                                                if(isCSRValid == true)
                                                {
                                                    if(isCSR_SizeValid == true)
                                                    {
                                                        //<editor-fold defaultstate="collapsed" desc="### PARAM and GET INFO">
                                                        String PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                                        String EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
                                                        String CACoreSubject = EscapeUtils.CheckTextNull(request.getParameter("CACoreSubject"));
                                                        String CertProfileID = sProfileIDBrowser;
                                                        String DN = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                                        String pPERSONAL_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pPERSONAL_NAME"));
                                                        String pCOMPANY_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pCOMPANY_NAME"));
                                                        String pDOMAIN_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pDOMAIN_NAME"));
                                                        String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                                        String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                                        String pTAX_CODE = "";// EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                                        String pDECISION = "";//EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                                        String pBUDGET_CODE = "";//EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                                        String pP_ID = "";//EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                                        String pCCCD = "";//EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                                        String pPASSPORT = "";//EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                                        String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                                        String pPROVINCE_DESC = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_DESC"));
                                                        String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                                            if("0".equals(CheckCHANGE_KEY)) {
                                                                CERTIFICATION[][] rsCertOld = new CERTIFICATION[1][];
                                                                db.S_BO_CERTIFICATION_DETAIL(pPAST_CERTIFICATE_ID, sessLanguage, rsCertOld);
                                                                if(rsCertOld[0].length > 0) {
                                                                    DN = EscapeUtils.CheckTextNull(rsCertOld[0][0].SUBJECT);
                                                                    pPERSONAL_NAME = EscapeUtils.CheckTextNull(rsCertOld[0][0].PERSONAL_NAME);
                                                                    pCOMPANY_NAME = EscapeUtils.CheckTextNull(rsCertOld[0][0].COMPANY_NAME);
                                                                    pDOMAIN_NAME = EscapeUtils.CheckTextNull(rsCertOld[0][0].DOMAIN_NAME);
                                                                    pENTERPRISE_ID = rsCertOld[0][0].ENTERPRISE_ID;
                                                                    pPERSONAL_ID = rsCertOld[0][0].PERSONAL_ID;
                                                                    pPROVINCE_ID = String.valueOf(rsCertOld[0][0].CITY_PROVINCE_ID);
                                                                    pDEVICE = EscapeUtils.CheckTextNull(rsCertOld[0][0].SERVICE_UUID);
                                                                }
                                                            }
                                                        }
                                                        CommonFunction.LogDebugString(log, "REGISTRATION-CERTIFICATION", "REQUEST: " + "SUBJECT: " + DN
                                                                + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME
                                                                + "; pDOMAIN_NAME: " + pDOMAIN_NAME + "; pENTERPRISE_ID: " + pENTERPRISE_ID
                                                                + "; pPERSONAL_ID: " + pPERSONAL_ID + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                                + "; pCERTIFICATION_ATTR_TYPE_ID: " + sCERTIFICATION_ATTR_TYPE_ID
                                                                + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                                + "; CACoreSubject: " + CACoreSubject + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                                + "; PROVINCE_ID: " + pPROVINCE_ID + "; Cert_ProfileID: " + CertProfileID + "; TOKEN_SN: " + sTOKEN_SN);
                                                        tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                        objectMapper = new ObjectMapper();
                                                        tempLogReq.personalName = pPERSONAL_NAME;
                                                        tempLogReq.companyName = pCOMPANY_NAME;
                                                        tempLogReq.domainName = pDOMAIN_NAME;
                                                        tempLogReq.deviceUUID = pDEVICE;
                                                        tempLogReq.personalID = pPERSONAL_ID;
                                                        tempLogReq.enterpriseID = pENTERPRISE_ID;
                                                        tempLogReq.emailContract = EMAIL_CONTRACT;
                                                        tempLogReq.phoneContract = PHONE_CONTRACT;
                                                        tempLogReq.issuerSubject = CACoreSubject;
                                                        tempLogReq.subjectDn = DN;
                                                        tempLogReq.provinceName = pPROVINCE_DESC;
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_BUY_MORE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_COMPENSATION)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_COMPENSATION;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED;
                                                        }
                                                        //</editor-fold>
                                                        if (sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                                        {
                                                            db.S_BO_CERTIFICATION_UPDATE(sCERTIFICATION_ID, CertProfileID, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME,
                                                                pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, DN, CACoreSubject, PHONE_CONTRACT,
                                                                EMAIL_CONTRACT, pPROVINCE_ID, CheckPRIVATE_KEY, loginUID, "", sCSR, pDEVICE, "", "",
                                                                pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                        }
                                                        ATTRIBUTE_VALUES valueATTR_Last = null;
                                                        String idCERT_REVOCATION_REASON = "";
                                                        String sCertRevokeReason_Frist = "";
                                                        java.sql.Timestamp sCertSuspendTime_Frist = null;
                                                        String sCertSuspendReason_Frist = "";
                                                        boolean isDeleteCertInToken = true;
                                                        //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR">
                                                        if (!"".equals(sVALUE_OLD)) {
                                                            // VALUE ATTR_FRIST
                                                            ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                            String sToken_Frist = valueATTR_Frist.getTokenSn();
                                                            sCertRevokeReason_Frist = valueATTR_Frist.getCerttificateRevokeReason();
                                                            idCERT_REVOCATION_REASON = valueATTR_Frist.getCerttificateRevokeEJBCAReason();
                                                            sCertSuspendReason_Frist = valueATTR_Frist.getCerttificateSuspendReason();
                                                            isDeleteCertInToken = valueATTR_Frist.getCertRevokeDeleteInTokenEnabled();
                                                            sCertSuspendTime_Frist = valueATTR_Frist.getSuspendedTime();
                                                            String sTypeName_Frist = valueATTR_Frist.getTypeName();
                                                            String sCreateUser_Frist = valueATTR_Frist.getCreateUser();
                                                            String sRsspAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                            String sRsspRelyingParty = valueATTR_Frist.getRsspRelyingParty();
                                                            Date sCreateDt_Frist = valueATTR_Frist.getCreateDt();
                                                            // VALUE ATTR_LAST
                                                            valueATTR_Last = new ATTRIBUTE_VALUES();
                                                            ATTRIBUTE_DATA dataATTR_Last = new ATTRIBUTE_DATA();
                                                            dataATTR_Last.setCertificationData(tempLogReq);
                                                            valueATTR_Last.setTokenSn(sToken_Frist);
                                                            valueATTR_Last.setRsspAgreementUUID(sRsspAgreementUUID);
                                                            valueATTR_Last.setRsspRelyingParty(sRsspRelyingParty);
                                                            boolean sChangeKeyEnabled = "1".equals(CheckCHANGE_KEY);
                                                            valueATTR_Last.setChangeKeyEnabled(sChangeKeyEnabled);
                                                            boolean sRevokedEnabled = "1".equals(CheckREVOKE_ENABLED);
                                                            valueATTR_Last.setRevokeOldCertificateEnabled(sRevokedEnabled);
                                                            boolean booKeepCertSNEnabled = "1".equals(keepCertSNEnabled);
                                                            valueATTR_Last.setKeepCertificateSNEnabled(booKeepCertSNEnabled);
                                                            valueATTR_Last.setCerttificateRevokeReason(sCertRevokeReason_Frist);
                                                            valueATTR_Last.setCerttificateRevokeEJBCAReason(idCERT_REVOCATION_REASON);
                                                            valueATTR_Last.setCerttificateSuspendReason(sCertSuspendReason_Frist);
                                                            valueATTR_Last.setCertRevokeDeleteInTokenEnabled(isDeleteCertInToken);
                                                            valueATTR_Last.setCerttificateDeclineReason("");
                                                            valueATTR_Last.setSuspendedTime(sCertSuspendTime_Frist);
                                                            valueATTR_Last.setTypeName(sTypeName_Frist);
                                                            valueATTR_Last.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                            valueATTR_Last.setCreateUser(sCreateUser_Frist);
                                                            valueATTR_Last.setCreateDt(sCreateDt_Frist);
                                                            valueATTR_Last.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR_Last.setApproveDt(new Date());
                                                            valueATTR_Last.setAttributeData(dataATTR_Last);
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                        }
                                                        //</editor-fold>

                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                                        {
                                                            if(!sUSER_ID.equals(sUSER_ID_New))
                                                            {
                                                                if(!sUSER_ID.equals(sUSER_ID_New)) {
                                                                    String strReqValueATTRForDecline = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                    db.S_BO_CERTIFICATION_CHANGED_CREATED_BY(ID, sAGENT_ID, sUSER_ID_New, loginUID, strReqValueATTRForDecline);
                                                                }
                                                            }
                                                        }
                                                        String sParam = db.S_BO_CERTIFICATION_PRE_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                        if("0".equals(sParam))
                                                        {
                                                            Config conf = new Config();
                                                            //<editor-fold defaultstate="collapsed" desc="### REPRESENTIVE PROCESS">
                                                            String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                            String userID = request.getSession(false).getAttribute("UserID").toString().trim();
                                                            String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
                                                            if("1".equals(sRepresentEnabled)) {
                                                                if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                    || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                    || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
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
                                                                    db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(sCERTIFICATION_ID, objectMapper.writeValueAsString(profileContact), userID);
                                                                }
                                                            }
                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)){
                                                                String registerNCRepresentative = EscapeUtils.CheckTextNull(request.getParameter("registerNCRepresentative"));
                                                                String registerNCAddress = EscapeUtils.CheckTextNull(request.getParameter("registerNCAddress"));
                                                                ProfileContactInfoJson profileContact = new ProfileContactInfoJson();
                                                                profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(registerNCRepresentative, true);
                                                                profileContact.Address = CommonFunction.replaceCharaterSpecialJson(registerNCAddress, true);
                                                                objectMapper = new ObjectMapper();
                                                                db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(sCERTIFICATION_ID, objectMapper.writeValueAsString(profileContact), userID);
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
                                                                    db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(sCERTIFICATION_ID), strDNSName, loginUID);
                                                                }
                                                            }
                                                            //</editor-fold>
                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                            strView = "0#0";
                                                            boolean autoApproveCA = false;
                                                            //<editor-fold defaultstate="collapsed" desc="### APPROVE CA PROCESS">
                                                            CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                            String sProfileCode = "";
                                                            String pCERT_ATTR_TYPE_CODE = "";
                                                            if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED
                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                            {
                                                                if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED) {
                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED;
                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE) {
                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE;
                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE) {
                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE;
                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                                                    pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE;
                                                                }
                                                                if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                                {
                                                                    autoApproveCA = true;
                                                                }
                                                            } else {
                                                                CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(pCERTIFICATION_PROFILE_ID), rsProfile);
                                                                if(rsProfile[0].length > 0) {
                                                                    sProfileCode = EscapeUtils.CheckTextNull(rsProfile[0][0].NAME);
                                                                    autoApproveCA = CommonFunction.getApproveCAOfProfile(sProfileCode, sessPolicyCert_Data);
                                                                    if(autoApproveCA == true) {
                                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_BUY_MORE;
                                                                        }
                                                                        autoApproveCA = CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data);
                                                                    }
                                                                }
                                                            }
                                                            if(autoApproveCA == true)
                                                            {
                                                                boolean booRSSP_ACCESS_ENABLED = false;
                                                                if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                                    String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                                                    if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                                        booRSSP_ACCESS_ENABLED = true;
                                                                        if (!"".equals(sVALUE_OLD)) {
                                                                            objectMapper = new ObjectMapper();
                                                                            ATTRIBUTE_VALUES valueRSSP = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                            if(valueRSSP.getTokenSn().trim().equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN)) {
                                                                                booRSSP_ACCESS_ENABLED = false;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if(booRSSP_ACCESS_ENABLED == false) {
                                                                    String sDiscountRate = "0";
                                                                    String sDiscountRateOption = "0";
                                                                    String CheckDeleteCertForOtherRevoke = "0";
                                                                    String CheckDeleteCertForRevoke = "0";
                                                                    GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                    if (sessGeneralPolicy1[0].length > 0) {
                                                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                        {
                                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                                                                            {
                                                                                sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                            }
                                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DELETE_OLD_CERTIFICATE))
                                                                            {
                                                                                CheckDeleteCertForOtherRevoke = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                            }
                                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_DELETE_CERT_WHEN_REVOKE))
                                                                            {
                                                                                CheckDeleteCertForRevoke = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                            }
                                                                        }
                                                                    }
                                                                    if("1".equals(sDiscountRateOption)) {
                                                                        if(sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                            || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                                                            || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                                                                        {
                                                                            if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL)
                                                                            {
                                                                                DISCOUNT_RATE_PROFILE[][] rsDisCount = new DISCOUNT_RATE_PROFILE[1][];
                                                                                db.S_BO_BRANCH_GET_DISCOUNT_RATE_PROFILE(sAGENT_ID, rsDisCount);
                                                                                if(rsDisCount[0].length > 0 && !"".equals(rsDisCount[0][0].PROPERTIES))
                                                                                {
                                                                                    PROFILE_DISCOUNT_RATE_DATA[][] resIPData = new PROFILE_DISCOUNT_RATE_DATA[1][];
                                                                                    CommonFunction.getAllProfileDiscountRate(rsDisCount[0][0].PROPERTIES, resIPData);
                                                                                    if(resIPData[0] != null && resIPData[0].length > 0)
                                                                                    {
                                                                                        for(PROFILE_DISCOUNT_RATE_DATA resIPData1 : resIPData[0])
                                                                                        {
                                                                                            if(EscapeUtils.CheckTextNull(resIPData1.profileName).equals(sProfileCode))
                                                                                            {
                                                                                                sDiscountRate = resIPData1.rosePercent;
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    valueATTR_Last.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                    valueATTR_Last.setApproveCADt(new Date());
                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                    if (sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                                                        idCERT_REVOCATION_REASON = String.valueOf(Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID);
                                                                        if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                            || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                            CheckDeleteCertForRevoke = "0";
                                                                        }
                                                                        valueATTR_Last.setCerttificateRevokeEJBCAReason(idCERT_REVOCATION_REASON);
                                                                        boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteCertForRevoke);
                                                                        valueATTR_Last.setCertRevokeDeleteInTokenEnabled(sRevokeDeleteInTokenEnabled);
                                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), CommonFunction.GenJSONTokenATTR(valueATTR_Last), loginUID);
                                                                        if ("0".equals(sApprove)) {
                                                                            String pCA_NAME = "";
                                                                            CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                            db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                            if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                                pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                            }
                                                                            int[] intRes = new int[1];
                                                                            String[] sRes = new String[1];
                                                                            ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Integer.parseInt(idCERT_REVOCATION_REASON),
                                                                                pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                            if (intRes[0] == 0) {
                                                                                db.S_BO_CERTIFICATION_REVOKED(Integer.parseInt(ID), Integer.parseInt(CheckDeleteCertForRevoke), loginUID);
                                                                                //<editor-fold defaultstate="collapsed" desc="### REVOKE REASON UPDATE">
                                                                                objectMapper = new ObjectMapper();
                                                                                CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                                jsonCertComment.certificateDeclineReason = "";
                                                                                jsonCertComment.certificateSuspendReason = "";
                                                                                jsonCertComment.certificateRevokeReason = sCertRevokeReason_Frist;
                                                                                sCertRevokeReason_Frist = objectMapper.writeValueAsString(jsonCertComment);
                                                                                db.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(String.valueOf(sCERTIFICATION_ID), sCertRevokeReason_Frist, loginUID);
                                                                                //</editor-fold>

                                                                                request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                strView = "0#0";
                                                                                String urlCallback = "";
                                                                                String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE;
                                                                                BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                if(rsBranch != null && rsBranch[0].length > 0) {
                                                                                    urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                }
                                                                                ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                    urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                Thread t = new Thread(thhreadLog);
                                                                                t.start();
                                                                            } else {
                                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                            }
                                                                        } else {
                                                                            strView = "1#0";
                                                                        }
                                                                    } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                                    {
                                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                        if ("0".equals(sApprove)) {
                                                                            int[] intRes = new int[1];
                                                                            String[] sRes = new String[1];
                                                                            String pCA_NAME = "";
                                                                            CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                            db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                            if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                                pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                            }
                                                                            ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_CERTIFICATEHOLD_ID,
                                                                                pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                            if (intRes[0] == 0) {
                                                                                String pCOMMENT = "";
                                                                                //<editor-fold defaultstate="collapsed" desc="### SUSPEND REASON UPDATE">
                                                                                objectMapper = new ObjectMapper();
                                                                                CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                                jsonCertComment.certificateDeclineReason = "";
                                                                                jsonCertComment.certificateRevokeReason = "";
                                                                                jsonCertComment.certificateSuspendReason = sCertSuspendReason_Frist;
                                                                                pCOMMENT = objectMapper.writeValueAsString(jsonCertComment);
                                                                                //</editor-fold>

                                                                                db.S_BO_CERTIFICATION_DISABLE(ID, sCertSuspendTime_Frist, pCOMMENT, loginUID);
                                                                                sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                                                strView = "0#0";
                                                                                String urlCallback = "";
                                                                                String requestType = "";
                                                                                if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE) {
                                                                                    requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE;
                                                                                } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE) {
                                                                                    requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE;
                                                                                }
                                                                                BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                if(rsBranch != null && rsBranch[0].length > 0){
                                                                                    urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                }
                                                                                ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                    urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                Thread t = new Thread(thhreadLog);
                                                                                t.start();
                                                                            } else {
                                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                            }
                                                                        } else {
                                                                            strView = "1#0";
                                                                        }
                                                                    } else if (sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                                                                    {
                                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                        if ("0".equals(sApprove)) {
                                                                            int[] intRes = new int[1];
                                                                            String[] sRes = new String[1];
                                                                            String pCA_NAME = "";
                                                                            CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                            db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                            if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                                pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                            }
                                                                            ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_REMOVEFROMCRL_ID,
                                                                                pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                            if (intRes[0] == 0) {
                                                                                db.S_BO_CERTIFICATION_RECOVERED(ID, loginUID);
                                                                                sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                                                strView = "0#0";
                                                                                String urlCallback = "";
                                                                                String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED;
                                                                                BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                if(rsBranch != null && rsBranch[0].length > 0){
                                                                                    urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                }
                                                                                ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                    urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                Thread t = new Thread(thhreadLog);
                                                                                t.start();
                                                                            } else {
                                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                            }
                                                                        } else {
                                                                            strView = "1#0";
                                                                        }
                                                                    } else if (sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                                                        if(CommonFunction.checkApproveCAReqType(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE, sessPolicyCert_Data) == true)
                                                                        {
                                                                            db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                            if(!"0".equals(sDiscountRate)) {
                                                                                db.S_BO_CERTIFICATION_UPDATE(sCERTIFICATION_ID, CertProfileID, "", "", "",
                                                                                    pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, "", "", "",
                                                                                    "", "", "", loginUID, "", "", "", sDiscountRate, "", pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                            }
                                                                            if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN))
                                                                            {
                                                                                if (sPUSH_NOTICE_ENABLED == true) {
                                                                                    int[] intRes = new int[1];
                                                                                    String[] sRes = new String[1];
                                                                                    ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                                }
                                                                            }
                                                                            if (pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                                String sOLD_TOKEN_ID = "";
                                                                                CERTIFICATION[][] rsGetTokenID = new CERTIFICATION[1][];
                                                                                db.S_BO_CERTIFICATION_DETAIL(pPAST_CERTIFICATE_ID, sessLanguage, rsGetTokenID);
                                                                                if (rsGetTokenID[0].length > 0) {
                                                                                    sOLD_TOKEN_ID = String.valueOf(rsGetTokenID[0][0].TOKEN_ID);
                                                                                }
                                                                                TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                                                int sTOKEN_STATE_ID_OLD = 0;
                                                                                String sTOKEN_SN_OLD = "";
                                                                                db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsToken_OLD);
                                                                                if (rsToken_OLD[0].length > 0) {
                                                                                    sTOKEN_STATE_ID_OLD = rsToken_OLD[0][0].TOKEN_STATE_ID;
                                                                                    sTOKEN_SN_OLD = EscapeUtils.CheckTextNull(rsToken_OLD[0][0].TOKEN_SN);
                                                                                }
                                                                                if (sTOKEN_STATE_ID_OLD != Definitions.CONFIG_TOKEN_STATE_ID_PERMANENT_INITIALZED) {
                                                                                    ATTRIBUTE_VALUES valueATTR_TOKEN;
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
                                                                                }
                                                                            } else {
                                                                                String urlCallback = "";
                                                                                String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                                                                BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                if(rsBranch != null && rsBranch[0].length > 0){
                                                                                    urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                }
                                                                                ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                    urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                Thread t = new Thread(thhreadLog);
                                                                                t.start();
                                                                            }
                                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                            strView = "0#0";
                                                                        }
                                                                    } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_BUY_MORE;
                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                            pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                                        }
                                                                        if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                                        {
                                                                            if(!"0".equals(sDiscountRate)) {
                                                                                db.S_BO_CERTIFICATION_UPDATE(sCERTIFICATION_ID, CertProfileID, "", "", "",
                                                                                    pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, "", "", "",
                                                                                    "", "", "", loginUID, "", "", "", sDiscountRate, "", pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                            }
                                                                            if (sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                                || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                                                                || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED) {
                                                                                if (sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                        || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                                    String FEE_AMOUNT = EscapeUtils.CheckTextNull(request.getParameter("sFEE_AMOUNT"));
                                                                                    db.S_BO_CERTIFICATION_UPDATE_AMOUNT(sCERTIFICATION_ID, FEE_AMOUNT.replace(",", ""), "", loginUID);
                                                                                }
                                                                            }
                                                                            if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                                || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN) || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                                CheckDeleteCertForOtherRevoke = "0";
                                                                            }
                                                                            if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                                                                            {
                                                                                CheckDeleteCertForOtherRevoke = "0";
                                                                            }
                                                                            boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteCertForOtherRevoke);
                                                                            valueATTR_Last.setDeleteOldCertificateEnabled(sRevokeDeleteInTokenEnabled);
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                            if ("0".equals(sApprove)) {                                                                                
                                                                                // set COMMIT TRUE File Attachment
                                                                                String[] pRESPONSE_CODE_FILE = new String[1];
                                                                                db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(Integer.parseInt(ID), loginUID, pRESPONSE_CODE_FILE);
                                                                                if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN))
                                                                                {
                                                                                    if (sPUSH_NOTICE_ENABLED == true) {
                                                                                        int[] intRes = new int[1];
                                                                                        String[] sRes = new String[1];
                                                                                        ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                                    }
                                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                    strView = "0#0";
                                                                                } else if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                                    || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN)
                                                                                    || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN)
                                                                                    || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                                    //<editor-fold defaultstate="collapsed" desc="### DNS LIST for SSL">
                                                                                    if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN)) {
                                                                                        if(!"".equals(strDNSName)) {
                                                                                            request.getSession(false).setAttribute("sessDNSNameForSSL_Approve", null);
                                                                                            db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(sCERTIFICATION_ID), strDNSName, loginUID);
                                                                                        }
                                                                                    }
                                                                                    //</editor-fold>
                                                                                    
                                                                                    if(pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                                        String isActiveSignServer = "0";
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
                                                                                            CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(sCERTIFICATION_ID), DN, strEmailCust, sessLanguage);
                                                                                            strView = "0#0";
                                                                                        } else {
                                                                                            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                            dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(Integer.parseInt(ID), 1);
                                                                                            int[] intWSRes = new int[1];
                                                                                            String[] sWSRes = new String[1];
                                                                                            ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, ID, intWSRes, sWSRes);
                                                                                            if (intWSRes[0] == 0) {
                                                                                                request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                                strView = "0#0";
                                                                                            } else {
                                                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                        strView = "0#0";
                                                                                        String urlCallback = "";
                                                                                        String requestType = "";
                                                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                                                                            requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                                            requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                                                        } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                                            requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                                                        }
                                                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                        if(rsBranch != null && rsBranch[0].length > 0){
                                                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                        }
                                                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                        Thread t = new Thread(thhreadLog);
                                                                                        t.start();
                                                                                    }
                                                                                } else {
                                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                    strView = "0#0";
                                                                                }
                                                                            } else {
                                                                                strView = sApprove + "#0";
                                                                            }
                                                                        }
                                                                    } else {
                                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                        if ("0".equals(sApprove)) {
                                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                            strView = "0#0";
                                                                        } else {
                                                                            strView = sApprove + "#0";
                                                                        }
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
                                                                        RSSPProcessCommon clsRSSP = new RSSPProcessCommon();
                                                                        String[] sResultRSSP = new String[2];
                                                                        String sAgreementUUID = "";
                                                                        String sRsspRelyingParty = "";
                                                                        String sChangeKeyApprove = "";
                                                                        boolean revokeSetOldStatus = false;
                                                                        if (!"".equals(sVALUE_OLD)) {
                                                                            ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                            sAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                                            sRsspRelyingParty = valueATTR_Frist.getRsspRelyingParty();
                                                                            revokeSetOldStatus = valueATTR_Frist.getRevokeSetOldStatusEnabled();
                                                                            if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                                sChangeKeyApprove = valueATTR_Frist.getChangeKeyEnabled() ? "1" : "0";
                                                                            }
                                                                        }
                                                                        if(!"".equals(sAgreementUUID)) {
                                                                            if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                                rsReq = new CERTIFICATION[1][];
                                                                                db.S_BO_CERTIFICATION_DETAIL(pPAST_CERTIFICATE_ID, sessLanguage, rsReq);
                                                                                if (rsReq[0].length > 0) {
                                                                                    pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                                                                }
                                                                            }
                                                                            String sRequestTypeRssp = "";
                                                                            if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL){
                                                                                sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_RENEW_CERT;
                                                                            } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO){
                                                                                sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_CHANGE_CERT;
                                                                            } else if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE){
                                                                                sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_REVOKE_CERT;
                                                                            } else {
                                                                                sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_PREPARE_CERT;
                                                                            }
                                                                            clsRSSP.approveCertificateForSignCloud(sAgreementUUID, String.valueOf(sCERTIFICATION_ID),
                                                                                CheckREVOKE_ENABLED, sRsspRelyingParty, sResultRSSP, pCERTIFICATION_SN, sChangeKeyApprove,
                                                                                sRequestTypeRssp, "", credentialAuthen, revokeSetOldStatus);
                                                                            if("0".equals(sResultRSSP[0])) {
                                                                                request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                strView = "0#0";
                                                                                String[] pRESPONSE_CODE_FILE = new String[1];
                                                                                db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(Integer.parseInt(ID), loginUID, pRESPONSE_CODE_FILE);
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
                                                            }
                                                            //</editor-fold>

                                                        } else {
                                                            strView = sParam+"#0";
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_CSR_KEYSIZE + "#0";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_CSR_NULL + "#0";
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_DENIED_FUNCTION + "#0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_HAS_STATUS + "#0";
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
                            case "approvecertca": {
                                //<editor-fold defaultstate="collapsed" desc="approvecertca">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String vEditAmount = EscapeUtils.CheckTextNull(request.getParameter("vEditAmount"));
                                    String CheckPUSH_NOTICE = EscapeUtils.CheckTextNull(request.getParameter("CheckPUSH_NOTICE"));
                                    String CheckCHANGE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckCHANGE_KEY"));
                                    String CheckPRIVATE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckPRIVATE_KEY"));
                                    String CheckREVOKE_ENABLED = EscapeUtils.CheckTextNull(request.getParameter("CheckREVOKE_ENABLED"));
                                    String FEE_AMOUNT = EscapeUtils.CheckTextNull(request.getParameter("sFEE_AMOUNT"));
                                    String DURATION_FREE = EscapeUtils.CheckTextNull(request.getParameter("sDURATION_FREE"));
                                    String sAGENT_ID_New = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ID"));
                                    String sUSER_ID_New = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                    String idREVOKE_REASON = EscapeUtils.CheckTextNull(request.getParameter("REVOKE_REASON"));
                                    String idSUSPEND_REASON = EscapeUtils.CheckTextNull(request.getParameter("SUSPEND_REASON"));
                                    String idSUSPEND_TIME = EscapeUtils.CheckTextNull(request.getParameter("SUSPEND_TIME"));
                                    String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                    String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                    System.out.println("pENTERPRISE_ID: " + pENTERPRISE_ID);
                                    System.out.println("pPERSONAL_ID: " + pPERSONAL_ID);
                                    String keepCertSNEnabled = EscapeUtils.CheckTextNull(request.getParameter("keepCertSNEnabled"));
                                    String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                    String sCheckReceivedSoftCopy = EscapeUtils.CheckTextNull(request.getParameter("sCheckReceivedSoftCopy"));
                                    String idReceivedNote = EscapeUtils.CheckTextNull(request.getParameter("idReceivedNote"));
                                    CommonFunction.LogDebugString(log, "APPROVE-CA", "AGENCY: " + sAGENT_ID_New
                                        + "; USER: " + sUSER_ID_New + "; CheckPUSH_NOTICE: " + CheckPUSH_NOTICE);
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    java.sql.Timestamp sSUSPEND_TIME_DB = null;
                                    String sAGENT_ID="";
                                    String sAGENT_TOKEN_ID="";
                                    String sUSER_ID="";
                                    String pPAST_CERTIFICATE_ID = "";
                                    String pCERTIFICATION_AUTHORITY_ID = "";
                                    String pCERTIFICATION_SN = "";
                                    String pTOKEN_SN = "";
                                    String sVALUE_OLD = "";
                                    String sCSR = "";
                                    String strEmailCust = "";
                                    String strReqValueATTR = "";
                                    String CertProfileID = EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                    int sCERTIFICATION_ID = 0;
                                    int sStatusRequest = 0;
                                    int sCertTypeID = 0;
                                    int pTOKEN_ID = 0;
                                    int pPKI_FORMFACTOR_ID = 0;
                                    int pDURATION_OLD = 0;
                                    int pCERT_PROFILE_DURATION = 0;
                                    boolean pPRIVATE_KEY_ENABLED = false;
                                    boolean sPUSH_NOTICE_ENABLED = false;
                                    objectMapper = new ObjectMapper();
                                    boolean isAccessAgency = true;
                                    String strPasswordP12 = "";
                                    CERTIFICATION[][] rsReq;
                                    rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        sCERTIFICATION_ID = rsReq[0][0].ID;
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sAGENT_TOKEN_ID = String.valueOf(rsReq[0][0].BRANCH_TOKEN_ID);
                                        sUSER_ID = String.valueOf(rsReq[0][0].CREATED_BY_ID);
                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        pTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        sCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                        pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                        pPAST_CERTIFICATE_ID = String.valueOf(rsReq[0][0].PAST_CERTIFICATION_ID);
                                        sStatusRequest = rsReq[0][0].CERTIFICATION_ATTR_STATE_ID;
                                        sCertTypeID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        pTOKEN_ID = rsReq[0][0].TOKEN_ID;
                                        pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                        sPUSH_NOTICE_ENABLED = rsReq[0][0].PUSH_NOTICE_ENABLED;
                                        pDURATION_OLD = rsReq[0][0].DURATION;
                                        pCERT_PROFILE_DURATION = rsReq[0][0].CERT_PROFILE_DURATION;
                                        strEmailCust = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                        sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    //</editor-fold>
                                    
                                    if (isAccessAgency == true) {
                                        if (!"".equals(sVALUE_OLD)) {
                                            if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                boolean functionAccess = false;
                                                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                //<editor-fold defaultstate="collapsed" desc="### CHECK ACCESS FUNCTION">
                                                String SessRoleID = sessionsa.getAttribute("RoleID_ID").toString().trim();
                                                if(sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                    || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                                {
                                                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                        || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                        || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                                    {
                                                        functionAccess = true;
                                                    } else {
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                            functionAccess = true;
                                                        }
                                                    }
                                                } else if(sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                                                {
                                                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_USER) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ACCOUNTANT)) {
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                            functionAccess = true;
                                                        }
                                                    } else {
                                                        functionAccess = true;
                                                    }
                                                }
                                                //</editor-fold>

                                                if(functionAccess == true) {
                                                    Config conf = new Config();
                                                    String profileManagerCAOption = LoadParamSystem.getParamStart(Definitions.CONFIG_PROFILE_MANAGER_LEVEL_APPROVE_OFCA);
                                                    boolean booRSSP_ACCESS_ENABLED = false;
                                                    if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                        String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                                        if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                            booRSSP_ACCESS_ENABLED = true;
                                                            if (!"".equals(sVALUE_OLD)) {
                                                                objectMapper = new ObjectMapper();
                                                                ATTRIBUTE_VALUES valueRSSP = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                if(valueRSSP.getTokenSn().trim().equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN)) {
                                                                    booRSSP_ACCESS_ENABLED = false;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if(booRSSP_ACCESS_ENABLED == false) {
                                                        boolean isCSRValid = true;
                                                        boolean isCSR_SizeValid = true;
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
                                                                    sCSR = EscapeUtils.CheckTextNull(request.getParameter("CSR"));
                                                                    if("".equals(sCSR))
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
                                                                            String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(sCSR);
                                                                            isCSR_SizeValid = sKeySizeDB.equals(sKeySizeCSR);
                                                                        }
                                                                    }
                                                                }
                                                                CheckPRIVATE_KEY = "0";
                                                            }
                                                        }
                                                        if(isCSRValid == true)
                                                        {
                                                            if(isCSR_SizeValid == true)
                                                            {
                                                                String pDISCOUNT_RATE = EscapeUtils.CheckTextNull(request.getParameter("pDISCOUNT_RATE"));
                                                                if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                                                {
                                                                    //<editor-fold defaultstate="collapsed" desc="SUPER ADMIN - ADMIN - SURVEYOR CA">
                                                                    if (sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                            || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                                                            || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED) {
                                                                        if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                            if (!"1".equals(vEditAmount)) {
                                                                                FEE_AMOUNT = "";
                                                                            }
                                                                            db.S_BO_CERTIFICATION_UPDATE_AMOUNT(sCERTIFICATION_ID, FEE_AMOUNT.replace(",", ""), CheckPUSH_NOTICE, loginUID);
                                                                        }
                                                                        if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                                                        {
                                                                            db.S_BO_CERTIFICATION_UPDATE_AMOUNT(sCERTIFICATION_ID, "", CheckPUSH_NOTICE, loginUID);
                                                                        }
                                                                        String PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                                                        String EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
                                                                        String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                                                        String DN = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                                                        String pPERSONAL_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pPERSONAL_NAME"));
                                                                        String pCOMPANY_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pCOMPANY_NAME"));
                                                                        String pDOMAIN_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pDOMAIN_NAME"));
                                                                        String pTAX_CODE = EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                                                        String pDECISION = EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                                                        String pBUDGET_CODE = EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                                                        String pP_ID = EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                                                        String pCCCD = EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                                                        String pPASSPORT = EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                                                        String pPROVINCE_DESC = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_DESC"));
                                                                        String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
                                                                        if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                                        {
                                                                            db.S_BO_CERTIFICATION_UPDATE(sCERTIFICATION_ID, CertProfileID, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME,
                                                                                pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, DN, "", PHONE_CONTRACT, EMAIL_CONTRACT, pPROVINCE_ID,
                                                                                CheckPRIVATE_KEY, loginUID, "", sCSR, pDEVICE, pDISCOUNT_RATE, "", pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                        }
                                                                        //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR">
                                                                        ATTRIBUTE_VALUES valueATTR_Last = null;
                                                                        ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                        tempLogReq = valueATTR_Frist.getAttributeData().getCertificationData();
                                                                        if ("1".equals(vEditAmount)) {
                                                                            tempLogReq.feeAmount = Integer.parseInt(FEE_AMOUNT.replace(",", ""));
                                                                        }
                                                                        tempLogReq.subjectDn = DN;
                                                                        tempLogReq.provinceName = pPROVINCE_DESC;
                                                                        String sToken_Frist = valueATTR_Frist.getTokenSn();
                                                                        String sCertRevokeReason_Frist = valueATTR_Frist.getCerttificateRevokeReason();
                                                                        String sCertSuspendReason_Frist = valueATTR_Frist.getCerttificateSuspendReason();
                                                                        sSUSPEND_TIME_DB = valueATTR_Frist.getSuspendedTime();
                                                                        if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                                        {
                                                                            sCertRevokeReason_Frist = idREVOKE_REASON;
                                                                        }
                                                                        if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                                        {
                                                                            sCertSuspendReason_Frist = idSUSPEND_REASON;
                                                                            if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                                            {
                                                                                sSUSPEND_TIME_DB = CommonFunction.ConvertStringToTimeStamp(idSUSPEND_TIME);
                                                                            }
                                                                        }
                                                                        String sTypeName_Frist = valueATTR_Frist.getTypeName();
                                                                        String sCreateUser_Frist = valueATTR_Frist.getCreateUser();
                                                                        Date sCreateDt_Frist = valueATTR_Frist.getCreateDt();
                                                                        String sApproveUser_Frist = EscapeUtils.CheckTextNull(valueATTR_Frist.getApproveUser());
                                                                        Date sApproveDt_Frist = valueATTR_Frist.getApproveDt();
                                                                        // VALUE ATTR_LAST
                                                                        valueATTR_Last = new ATTRIBUTE_VALUES();
                                                                        ATTRIBUTE_DATA dataATTR_Last = new ATTRIBUTE_DATA();
                                                                        dataATTR_Last.setCertificationData(tempLogReq);
                                                                        valueATTR_Last.setTokenSn(sToken_Frist);
                                                                        boolean sChangeKeyEnabled = "1".equals(CheckCHANGE_KEY);
                                                                        valueATTR_Last.setChangeKeyEnabled(sChangeKeyEnabled);
                                                                        boolean sRevokedEnabled = "1".equals(CheckREVOKE_ENABLED);
                                                                        valueATTR_Last.setRevokeOldCertificateEnabled(sRevokedEnabled);
                                                                        boolean booKeepCertSNEnabled = "1".equals(keepCertSNEnabled);
                                                                        valueATTR_Last.setKeepCertificateSNEnabled(booKeepCertSNEnabled);
                                                                        valueATTR_Last.setCerttificateRevokeReason(sCertRevokeReason_Frist);
                                                                        valueATTR_Last.setCerttificateSuspendReason(sCertSuspendReason_Frist);
                                                                        valueATTR_Last.setCerttificateDeclineReason("");
                                                                        valueATTR_Last.setSuspendedTime(sSUSPEND_TIME_DB);
                                                                        valueATTR_Last.setTypeName(sTypeName_Frist);
                                                                        valueATTR_Last.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                        valueATTR_Last.setCreateUser(sCreateUser_Frist);
                                                                        valueATTR_Last.setCreateDt(sCreateDt_Frist);
                                                                        if (!"".equals(sApproveUser_Frist)) {
                                                                            valueATTR_Last.setApproveUser(sApproveUser_Frist);
                                                                        } else {
                                                                            valueATTR_Last.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                        }
                                                                        if (sApproveDt_Frist != null) {
                                                                            valueATTR_Last.setApproveDt(sApproveDt_Frist);
                                                                        } else {
                                                                            valueATTR_Last.setApproveDt(new Date());
                                                                        }
                                                                        valueATTR_Last.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                        valueATTR_Last.setApproveCADt(new Date());
                                                                        valueATTR_Last.setAttributeData(dataATTR_Last);
                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                        //</editor-fold>

                                                                        if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                                                            String idCERT_REVOCATION_REASON = EscapeUtils.CheckTextNull(request.getParameter("CERT_REVOCATION_REASON"));
                                                                            if("".equals(idCERT_REVOCATION_REASON)) {
                                                                                idCERT_REVOCATION_REASON = String.valueOf(Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID);
                                                                            }
                                                                            String CheckDeleteRevoke = EscapeUtils.CheckTextNull(request.getParameter("CheckDeleteRevoke"));
                                                                            if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                                || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN) || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                                CheckDeleteRevoke = "0";
                                                                            }
                                                                            valueATTR_Last.setCerttificateRevokeEJBCAReason(idCERT_REVOCATION_REASON);
                                                                            boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteRevoke);
                                                                            valueATTR_Last.setCertRevokeDeleteInTokenEnabled(sRevokeDeleteInTokenEnabled);
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), CommonFunction.GenJSONTokenATTR(valueATTR_Last), loginUID);
                                                                            if ("0".equals(sApprove)) {
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
                                                                                ConnectConnector.RevokeCertificate(pTOKEN_SN, pCERTIFICATION_SN, Integer.parseInt(idCERT_REVOCATION_REASON),
                                                                                    pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                                if (intRes[0] == 0) {
                                                                                    db.S_BO_CERTIFICATION_REVOKED(Integer.parseInt(ID), Integer.parseInt(CheckDeleteRevoke), loginUID);
                                                                                    //<editor-fold defaultstate="collapsed" desc="### REVOKE REASON UPDATE">
                                                                                    /*objectMapper = new ObjectMapper();
                                                                                    CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                                    jsonCertComment.certificateDeclineReason = "";
                                                                                    jsonCertComment.certificateSuspendReason = "";
                                                                                    jsonCertComment.certificateRevokeReason = idREVOKE_REASON;
                                                                                    idREVOKE_REASON = objectMapper.writeValueAsString(jsonCertComment);
                                                                                    db.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(String.valueOf(sCERTIFICATION_ID), idREVOKE_REASON, loginUID);*/
                                                                                    String userLoginID = request.getSession(false).getAttribute("UserID").toString().trim();
                                                                                    db.S_BO_CERTIFICATION_UPDATE_REVOCATION_REASON(sCERTIFICATION_ID, idREVOKE_REASON, userLoginID);
                                                                                    //</editor-fold>

                                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                    strView = "0#0";
                                                                                    String urlCallback = "";
                                                                                    String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE;
                                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                    db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                    if(rsBranch != null && rsBranch[0].length > 0) {
                                                                                        urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                    }
                                                                                    ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                        urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                    Thread t = new Thread(thhreadLog);
                                                                                    t.start();
                                                                                } else {
                                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                }
                                                                            } else {
                                                                                strView = "1#0";
                                                                            }
                                                                        } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                                        {
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
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
                                                                                ConnectConnector.RevokeCertificate(pTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_CERTIFICATEHOLD_ID,
                                                                                    pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                                if (intRes[0] == 0) {
                                                                                    String pCOMMENT = "";
                                                                                    //<editor-fold defaultstate="collapsed" desc="### SUSPEND REASON UPDATE">
                                                                                    if(sSUSPEND_TIME_DB != null) {
                                                                                        sSUSPEND_TIME_DB = CommonFunction.ConvertStringToTimeStamp(idSUSPEND_TIME);
                                                                                    }
                                                                                    objectMapper = new ObjectMapper();
                                                                                    CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                                    jsonCertComment.certificateDeclineReason = "";
                                                                                    jsonCertComment.certificateRevokeReason = "";
                                                                                    jsonCertComment.certificateSuspendReason = idSUSPEND_REASON;
                                                                                    pCOMMENT = objectMapper.writeValueAsString(jsonCertComment);
                                                                                    //</editor-fold>

                                                                                    db.S_BO_CERTIFICATION_DISABLE(ID, sSUSPEND_TIME_DB, pCOMMENT, loginUID);
                                                                                    sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                                                    strView = "0#0";
                                                                                    String urlCallback = "";
                                                                                    String requestType = "";
                                                                                    if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE) {
                                                                                        requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE;
                                                                                    } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE) {
                                                                                        requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE;
                                                                                    }
                                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                    db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                    if(rsBranch != null && rsBranch[0].length > 0){
                                                                                        urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                    }
                                                                                    ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                        urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                    Thread t = new Thread(thhreadLog);
                                                                                    t.start();
                                                                                } else {
                                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                }
                                                                            } else {
                                                                                strView = "1#0";
                                                                            }
                                                                        } else if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                                                                        {
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                            if ("0".equals(sApprove)) {
                                                                                int[] intRes = new int[1];
                                                                                String[] sRes = new String[1];
                                                                                String pCA_NAME = "";
                                                                                CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                                db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                                if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                                    pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                                }
                                                                                ConnectConnector.RevokeCertificate(pTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_REMOVEFROMCRL_ID,
                                                                                    pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                                if (intRes[0] == 0) {
                                                                                    db.S_BO_CERTIFICATION_RECOVERED(ID, loginUID);
                                                                                    sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                                                    strView = "0#0";
                                                                                    String urlCallback = "";
                                                                                    String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED;
                                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                    db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                    if(rsBranch != null && rsBranch[0].length > 0){
                                                                                        urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                    }
                                                                                    ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                        urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                    Thread t = new Thread(thhreadLog);
                                                                                    t.start();
                                                                                } else {
                                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                }
                                                                            } else {
                                                                                strView = "1#0";
                                                                            }
                                                                        } else if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                                                            if(!sAGENT_ID.equals(sAGENT_ID_New) || !sUSER_ID.equals(sUSER_ID_New))
                                                                            {
                                                                                String strReqValueATTRForDecline = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                                db.S_BO_CERTIFICATION_CHANGED_CREATED_BY(ID, sAGENT_ID_New, sUSER_ID_New, loginUID, strReqValueATTRForDecline);
                                                                            }
                                                                            db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                            if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN)) {
                                                                                if ("1".equals(CheckPUSH_NOTICE)) {
                                                                                    int[] intRes = new int[1];
                                                                                    String[] sRes = new String[1];
                                                                                    ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                                }
                                                                            }
                                                                            if (pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                                String sOLD_TOKEN_ID = "";
                                                                                CERTIFICATION[][] rsGetTokenID = new CERTIFICATION[1][];
                                                                                db.S_BO_CERTIFICATION_DETAIL(pPAST_CERTIFICATE_ID, sessLanguage, rsGetTokenID);
                                                                                if (rsGetTokenID[0].length > 0) {
                                                                                    sOLD_TOKEN_ID = String.valueOf(rsGetTokenID[0][0].TOKEN_ID);
                                                                                }
                                                                                TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                                                int sTOKEN_STATE_ID_OLD = 0;
                                                                                String sTOKEN_SN_OLD = "";
                                                                                db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsToken_OLD);
                                                                                if (rsToken_OLD[0].length > 0) {
                                                                                    sTOKEN_STATE_ID_OLD = rsToken_OLD[0][0].TOKEN_STATE_ID;
                                                                                    sTOKEN_SN_OLD = EscapeUtils.CheckTextNull(rsToken_OLD[0][0].TOKEN_SN);
                                                                                }
                                                                                if (sTOKEN_STATE_ID_OLD != Definitions.CONFIG_TOKEN_STATE_ID_PERMANENT_INITIALZED) {
                                                                                    ATTRIBUTE_VALUES valueATTR_TOKEN;
                                                                                    valueATTR_TOKEN = new ATTRIBUTE_VALUES();
                                                                                    valueATTR_TOKEN.setTokenSn(sTOKEN_SN_OLD);
                                                                                    valueATTR_TOKEN.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PERMANENT_INITIALZED);
                                                                                    valueATTR_TOKEN.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                                                    valueATTR_TOKEN.setCreateUser(loginFullname + " (" + loginUID + ")");
                                                                                    valueATTR_TOKEN.setCreateDt(new Date());
                                                                                    valueATTR_TOKEN.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                                    valueATTR_TOKEN.setApproveDt(new Date());
                                                                                    String strReqTokenATTR = CommonFunction.GenJSONTokenATTR(valueATTR_TOKEN);
                                                                                    int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                                                    db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sOLD_TOKEN_ID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PERMANENT_INITIALZED,
                                                                                        intTOKEN_ATTR_STATE, strReqTokenATTR, loginUID);
                                                                                    db.S_BO_TOKEN_UPDATE(Integer.parseInt(sOLD_TOKEN_ID),
                                                                                            String.valueOf(Definitions.CONFIG_TOKEN_STATE_ID_LOST), "", "", "", "", loginUID);
                                                                                }
                                                                            } else {
                                                                                String urlCallback = "";
                                                                                String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                                                                BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                if(rsBranch != null && rsBranch[0].length > 0){
                                                                                    urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                }
                                                                                ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                    urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                Thread t = new Thread(thhreadLog);
                                                                                t.start();
                                                                            }
                                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                            strView = "0#0";
                                                                        } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                            if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                                                                            {
                                                                                //<editor-fold defaultstate="collapsed" desc="### TOKEN AND CERT TRANSFER">
                                                                                if(!sAGENT_ID.equals(sAGENT_ID_New) || !sUSER_ID.equals(sUSER_ID_New))
                                                                                {
                                                                                    String strReqValueATTRForDecline = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                                    db.S_BO_CERTIFICATION_CHANGED_CREATED_BY(ID, sAGENT_ID_New, sUSER_ID_New, loginUID, strReqValueATTRForDecline);
                                                                                }
                                                                                if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                                {
                                                                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
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
                                                                                    if("0".equals(sNoAllowTranferToken)) {
                                                                                        if(!sAGENT_ID.equals(sAGENT_ID_New))
                                                                                        {
                                                                                            if(!sAGENT_ID_New.equals(sAGENT_TOKEN_ID))
                                                                                            {
                                                                                                db.S_BO_TOKEN_UPDATE_BRANCH(String.valueOf(pTOKEN_ID), sAGENT_ID_New, loginUID);
                                                                                            }
                                                                                        } else {
                                                                                            if(!sAGENT_ID.equals(sAGENT_TOKEN_ID))
                                                                                            {
                                                                                                db.S_BO_TOKEN_UPDATE_BRANCH(String.valueOf(pTOKEN_ID), sAGENT_ID, loginUID);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                //</editor-fold>
                                                                            }
                                                                            String CheckDeleteRevoke = EscapeUtils.CheckTextNull(request.getParameter("CheckDeleteRevoke"));
                                                                            if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                                || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN) || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                                CheckDeleteRevoke = "0";
                                                                            }
                                                                            if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE) {
                                                                                CheckDeleteRevoke = "0";
                                                                            }
                                                                            boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteRevoke);
                                                                            valueATTR_Last.setDeleteOldCertificateEnabled(sRevokeDeleteInTokenEnabled);
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                            
                                                                            //<editor-fold defaultstate="collapsed" desc="### PROMOTION PROFILE">
                                                                            if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                                                                            {
                                                                                if(!"".equals(DURATION_FREE)) {
                                                                                    CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                                    if(rsProfile[0].length > 0) {
                                                                                        pCERT_PROFILE_DURATION = rsProfile[0][0].DURATION;
                                                                                    }
                                                                                    System.out.println("DURATION_FREE: " + DURATION_FREE + "; pCERT_PROFILE_DURATION: " + pCERT_PROFILE_DURATION);
                                                                                    if(Integer.parseInt(DURATION_FREE) <= pCERT_PROFILE_DURATION) {
                                                                                        int pDURATION_NEW = pCERT_PROFILE_DURATION + Integer.parseInt(DURATION_FREE);
                                                                                        if(pDURATION_NEW != pDURATION_OLD) {
                                                                                            db.S_BO_CERTIFICATION_UPDATE_DURATION(sCERTIFICATION_ID, pDURATION_NEW, null, loginUID);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            //</editor-fold>
                                                                            
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                            if ("0".equals(sApprove)) {
                                                                                //<editor-fold defaultstate="collapsed" desc="### PROFILE MANAGER PROCESS">
                                                                                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                                                if("2".equals(profileManagerCAOption) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO)
                                                                                     || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                                    if (Definitions.CONFIG_AGENT_ROOT.equals(AGENT_ID_LOG)) {
                                                                                        String STATE_PROFILE = EscapeUtils.CheckTextNull(request.getParameter("STATE_PROFILE"));
                                                                                        CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessCollectedBriefPro");
                                                                                        String pBRIEF_PROPERTIES = "";
                                                                                        if(resProfileData != null && resProfileData[0].length > 0){
                                                                                            pBRIEF_PROPERTIES = CommonFunction.renderBriefFileType(resProfileData[0], "", idReceivedNote, "");
                                                                                        }
                                                                                        String param1 = db.S_BO_CERTIFICATION_BRIEF_UPDATE_COLLECT_SOFTCOPY(String.valueOf(sCERTIFICATION_ID),
                                                                                            pBRIEF_PROPERTIES, sCheckReceivedSoftCopy, loginUID, STATE_PROFILE);
                                                                                        request.getSession(false).setAttribute("SessCollectedBriefPro", null);
                                                                                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_BRIEF_UPDATE_COLLECT_SOFTCOPY Result", param1);
                                                                                        db.S_BO_FILE_MANAGER_UPDATE_COMMIT_ENABLED(String.valueOf(sCERTIFICATION_ID), loginUID);
                                                                                        
                                                                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && request.getSession(false).getAttribute("sessProfileStateDK01") != null) {
                                                                                            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                            String loginID = request.getSession(false).getAttribute("UserID").toString().trim();
                                                                                            String profileStateDK01 = request.getSession(false).getAttribute("sessProfileStateDK01").toString().trim();
                                                                                            dbTwo.S_BO_CERTIFICATION_BRIEF_UPDATE_BUSINESS_LICENSE_TYPE_ID(sCERTIFICATION_ID, Integer.parseInt(profileStateDK01), Integer.parseInt(loginID));
                                                                                        }
                                                                                    }
                                                                                }
                                                                                //</editor-fold>
                                                                                
                                                                                //<editor-fold defaultstate="collapsed" desc="### REPRESENTIVE PROCESS">
                                                                                conf = new Config();
                                                                                String userID = request.getSession(false).getAttribute("UserID").toString().trim();
                                                                                String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
                                                                                if("1".equals(sRepresentEnabled)) {
                                                                                    if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                        || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                        || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
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
                                                                                        db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(sCERTIFICATION_ID, objectMapper.writeValueAsString(profileContact), userID);
                                                                                    }
                                                                                }
                                                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)){
                                                                                    String registerNCRepresentative = EscapeUtils.CheckTextNull(request.getParameter("registerNCRepresentative"));
                                                                                    String registerNCAddress = EscapeUtils.CheckTextNull(request.getParameter("registerNCAddress"));
                                                                                    ProfileContactInfoJson profileContact = new ProfileContactInfoJson();
                                                                                    profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(registerNCRepresentative, true);
                                                                                    profileContact.Address = CommonFunction.replaceCharaterSpecialJson(registerNCAddress, true);
                                                                                    objectMapper = new ObjectMapper();
                                                                                    db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(sCERTIFICATION_ID, objectMapper.writeValueAsString(profileContact), userID);
                                                                                }
                                                                                //</editor-fold>

                                                                                // set COMMIT TRUE File Attachment
                                                                                String[] pRESPONSE_CODE_FILE = new String[1];
                                                                                db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(Integer.parseInt(ID), loginUID, pRESPONSE_CODE_FILE);
                                                                                if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN))
                                                                                {
                                                                                    if (sPUSH_NOTICE_ENABLED == true) {
                                                                                        // RA SEND_EMAIL
                                                                                        int[] intRes = new int[1];
                                                                                        String[] sRes = new String[1];
                                                                                        ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                                    }
                                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                    strView = "0#0";
                                                                                } else if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                                    || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN)
                                                                                    || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN)
                                                                                    || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                                    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
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
                                                                                            db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(sCERTIFICATION_ID), strDNSName, loginUID);
                                                                                        }
                                                                                    }
                                                                                    //</editor-fold>
                                                                                    if(pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
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
                                                                                            String checkCertConfirm = EscapeUtils.CheckTextNull(request.getParameter("checkCertConfirm"));
                                                                                            if("1".equals(checkCertConfirm)) {
                                                                                                CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(sCERTIFICATION_ID), DN, strEmailCust, sessLanguage);
                                                                                                strView = "0#0";
                                                                                            } else {
                                                                                                dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(Integer.parseInt(ID), 1);
                                                                                                int[] intWSRes = new int[1];
                                                                                                String[] sWSRes = new String[1];
                                                                                                ConnectConnector.EnrollCertificate(pTOKEN_SN, strPasswordP12, ID, intWSRes, sWSRes);
                                                                                                if (intWSRes[0] == 0) {
                                                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                                    strView = "0#0";
                                                                                                } else {
                                                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(Integer.parseInt(ID), 1);
                                                                                            int[] intWSRes = new int[1];
                                                                                            String[] sWSRes = new String[1];
                                                                                            ConnectConnector.EnrollCertificate(pTOKEN_SN, strPasswordP12, ID, intWSRes, sWSRes);
                                                                                            if (intWSRes[0] == 0) {
                                                                                                request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                                strView = "0#0";
                                                                                            } else {
                                                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                        strView = "0#0";
                                                                                        String urlCallback = "";
                                                                                        String requestType = "";
                                                                                        if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                                                                            requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                                                        } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                                            requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                                                        } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                                            requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                                                        }
                                                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                        if(rsBranch != null && rsBranch[0].length > 0){
                                                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                        }
                                                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                        Thread t = new Thread(thhreadLog);
                                                                                        t.start();
                                                                                    }
                                                                                } else {
                                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                    strView = "0#0";
                                                                                }
                                                                            } else {
                                                                                strView = sApprove + "#0";
                                                                            }
                                                                        } else {
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                            if ("0".equals(sApprove)) {
                                                                                request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                strView = "0#0";
                                                                            } else {
                                                                                strView = sApprove + "#0";
                                                                            }
                                                                        }
                                                                    }
                                                                    //</editor-fold>
                                                                } else {
                                                                    //<editor-fold defaultstate="collapsed" desc="USER CA">
                                                                    if (sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                        || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                                                        || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED) {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            // Check Regis - Renew to update amount
                                                                            if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                    || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                    || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                                if (!"1".equals(vEditAmount)) {
                                                                                    FEE_AMOUNT = "";
                                                                                }
                                                                                db.S_BO_CERTIFICATION_UPDATE_AMOUNT(sCERTIFICATION_ID, FEE_AMOUNT.replace(",", ""), CheckPUSH_NOTICE, loginUID);
                                                                            }
                                                                            if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                                                            {
                                                                                db.S_BO_CERTIFICATION_UPDATE_AMOUNT(sCERTIFICATION_ID, "", CheckPUSH_NOTICE, loginUID);
                                                                            }
                                                                            String PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                                                            String EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
                                                                            String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                                                            String DN = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                                                            String pPERSONAL_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pPERSONAL_NAME"));
                                                                            String pCOMPANY_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pCOMPANY_NAME"));
                                                                            String pTAX_CODE = EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                                                            String pDECISION = EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                                                            String pBUDGET_CODE = EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                                                            String pP_ID = EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                                                            String pCCCD = EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                                                            String pPASSPORT = EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                                                            String pPROVINCE_DESC = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_DESC"));
                                                                            String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
                                                                            if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                                            {
                                                                                db.S_BO_CERTIFICATION_UPDATE(sCERTIFICATION_ID, CertProfileID, pPERSONAL_NAME, pCOMPANY_NAME, "",
                                                                                    pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, DN, "", PHONE_CONTRACT, EMAIL_CONTRACT, pPROVINCE_ID,
                                                                                    CheckPRIVATE_KEY, loginUID, "", sCSR, pDEVICE, pDISCOUNT_RATE, "", pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                            }
                                                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR">
                                                                            ATTRIBUTE_VALUES valueATTR_Last = null;
                                                                            // VALUE ATTR_FRIST
                                                                            ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                            tempLogReq = valueATTR_Frist.getAttributeData().getCertificationData();
                                                                            if ("1".equals(vEditAmount)) {
                                                                                tempLogReq.feeAmount = Integer.parseInt(FEE_AMOUNT.replace(",", ""));
                                                                            }
                                                                            tempLogReq.subjectDn = DN;
                                                                            tempLogReq.provinceName = pPROVINCE_DESC;
                                                                            String sToken_Frist = valueATTR_Frist.getTokenSn();
                                                                            String sCertRevokeReason_Frist = valueATTR_Frist.getCerttificateRevokeReason();
                                                                            String sCertSuspendReason_Frist = valueATTR_Frist.getCerttificateSuspendReason();
                                                                            if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                                            {
                                                                                sCertRevokeReason_Frist = idREVOKE_REASON;
                                                                            }
                                                                            if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                                            {
                                                                                sCertSuspendReason_Frist = idSUSPEND_REASON;
                                                                                if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                                                {
                                                                                    sSUSPEND_TIME_DB = CommonFunction.ConvertStringToTimeStamp(idSUSPEND_TIME);
                                                                                }
                                                                            }
                                                                            String sTypeName_Frist = valueATTR_Frist.getTypeName();
                                                                            String sCreateUser_Frist = valueATTR_Frist.getCreateUser();
                                                                            Date sCreateDt_Frist = valueATTR_Frist.getCreateDt();
                                                                            String sApproveUser_Frist = valueATTR_Frist.getApproveUser();
                                                                            Date sApproveDt_Frist = valueATTR_Frist.getApproveDt();
                                                                            // VALUE ATTR_LAST
                                                                            valueATTR_Last = new ATTRIBUTE_VALUES();
                                                                            ATTRIBUTE_DATA dataATTR_Last = new ATTRIBUTE_DATA();
                                                                            dataATTR_Last.setCertificationData(tempLogReq);
                                                                            valueATTR_Last.setTokenSn(sToken_Frist);
                                                                            boolean sChangeKeyEnabled = "1".equals(CheckCHANGE_KEY);
                                                                            valueATTR_Last.setChangeKeyEnabled(sChangeKeyEnabled);
                                                                            boolean sRevokedEnabled = "1".equals(CheckREVOKE_ENABLED);
                                                                            valueATTR_Last.setRevokeOldCertificateEnabled(sRevokedEnabled);
                                                                            boolean booKeepCertSNEnabled = "1".equals(keepCertSNEnabled);
                                                                            valueATTR_Last.setKeepCertificateSNEnabled(booKeepCertSNEnabled);
                                                                            valueATTR_Last.setCerttificateRevokeReason(sCertRevokeReason_Frist);
                                                                            valueATTR_Last.setCerttificateSuspendReason(sCertSuspendReason_Frist);
                                                                            valueATTR_Last.setCerttificateDeclineReason("");
                                                                            valueATTR_Last.setSuspendedTime(sSUSPEND_TIME_DB);
                                                                            valueATTR_Last.setTypeName(sTypeName_Frist);
                                                                            valueATTR_Last.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                            valueATTR_Last.setCreateUser(sCreateUser_Frist);
                                                                            valueATTR_Last.setCreateDt(sCreateDt_Frist);
                                                                            if (!"".equals(sApproveUser_Frist)) {
                                                                                valueATTR_Last.setApproveUser(sApproveUser_Frist);
                                                                            } else {
                                                                                valueATTR_Last.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                                            }
                                                                            if (sApproveDt_Frist != null) {
                                                                                valueATTR_Last.setApproveDt(sApproveDt_Frist);
                                                                            } else {
                                                                                valueATTR_Last.setApproveDt(new Date());
                                                                            }
                                                                            valueATTR_Last.setApproveCAUser(loginFullname + " (" + loginUID + ")");
                                                                            valueATTR_Last.setApproveCADt(new Date());
                                                                            valueATTR_Last.setAttributeData(dataATTR_Last);
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                            //</editor-fold>

                                                                            if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                                                                String idCERT_REVOCATION_REASON = EscapeUtils.CheckTextNull(request.getParameter("CERT_REVOCATION_REASON"));
                                                                                String CheckDeleteRevoke = EscapeUtils.CheckTextNull(request.getParameter("CheckDeleteRevoke"));
                                                                                if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                                    || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN)|| pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                                    CheckDeleteRevoke = "0";
                                                                                }
                                                                                valueATTR_Last.setCerttificateRevokeEJBCAReason(idCERT_REVOCATION_REASON);
                                                                                boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteRevoke);
                                                                                valueATTR_Last.setCertRevokeDeleteInTokenEnabled(sRevokeDeleteInTokenEnabled);
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), CommonFunction.GenJSONTokenATTR(valueATTR_Last), loginUID);
                                                                                if ("0".equals(sApprove)) {
                                                                                    // TYPE REVOKE
                                                                                    String pCA_NAME = "";
                                                                                    CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                                    db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                                    if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                                        pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                                    }
                                                                                    int[] intRes = new int[1];
                                                                                    String[] sRes = new String[1];
                                                                                // AFTER_EDIT
                                                                                    ConnectConnector.RevokeCertificate(pTOKEN_SN, pCERTIFICATION_SN, Integer.parseInt(idCERT_REVOCATION_REASON),
                                                                                        pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                                    if (intRes[0] == 0) {
                                                                                        db.S_BO_CERTIFICATION_REVOKED(Integer.parseInt(ID), Integer.parseInt(CheckDeleteRevoke), loginUID);
                                                                                        //<editor-fold defaultstate="collapsed" desc="### REVOKE REASON UPDATE">
                                                                                        /*objectMapper = new ObjectMapper();
                                                                                        CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                                        jsonCertComment.certificateDeclineReason = "";
                                                                                        jsonCertComment.certificateSuspendReason = "";
                                                                                        jsonCertComment.certificateRevokeReason = idREVOKE_REASON;
                                                                                        idREVOKE_REASON = objectMapper.writeValueAsString(jsonCertComment);
                                                                                        db.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(String.valueOf(sCERTIFICATION_ID), idREVOKE_REASON, loginUID);*/
                                                                                        String userLoginID = request.getSession(false).getAttribute("UserID").toString().trim();
                                                                                        db.S_BO_CERTIFICATION_UPDATE_REVOCATION_REASON(sCERTIFICATION_ID, idREVOKE_REASON, userLoginID);
                                                                                        //</editor-fold>

                                                                                        request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                        strView = "0#0";
                                                                                        String urlCallback = "";
                                                                                        String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE;
                                                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                        if(rsBranch != null && rsBranch[0].length > 0) {
                                                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                        }
                                                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                        Thread t = new Thread(thhreadLog);
                                                                                        t.start();
                                                                                    } else {
                                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                    }
                                                                                } else {
                                                                                    strView = "1#0";
                                                                                }
                                                                            } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                                            {
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
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
                                                                                    ConnectConnector.RevokeCertificate(pTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_CERTIFICATEHOLD_ID,
                                                                                        pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                                    if (intRes[0] == 0) {
                                                                                        String pCOMMENT = "";
                                                                                        //<editor-fold defaultstate="collapsed" desc="### SUSPEND REASON UPDATE">
                                                                                        if(sSUSPEND_TIME_DB != null) {
                                                                                            sSUSPEND_TIME_DB = CommonFunction.ConvertStringToTimeStamp(idSUSPEND_TIME);
                                                                                        }
                                                                                        objectMapper = new ObjectMapper();
                                                                                        CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                                        jsonCertComment.certificateDeclineReason = "";
                                                                                        jsonCertComment.certificateRevokeReason = "";
                                                                                        jsonCertComment.certificateSuspendReason = idSUSPEND_REASON;
                                                                                        pCOMMENT = objectMapper.writeValueAsString(jsonCertComment);
                                                                                        //</editor-fold>

                                                                                        db.S_BO_CERTIFICATION_DISABLE(ID, sSUSPEND_TIME_DB, pCOMMENT, loginUID);
                                                                                        sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                                                        strView = "0#0";
                                                                                        
                                                                                        String urlCallback = "";
                                                                                        String requestType = "";
                                                                                        if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE) {
                                                                                            requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE;
                                                                                        } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE) {
                                                                                            requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE;
                                                                                        }
                                                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                        if(rsBranch != null && rsBranch[0].length > 0){
                                                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                        }
                                                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                        Thread t = new Thread(thhreadLog);
                                                                                        t.start();
                                                                                    } else {
                                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                    }
                                                                                } else {
                                                                                    strView = "1#0";
                                                                                }
                                                                            } else if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED) {
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
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
                                                                                    ConnectConnector.RevokeCertificate(pTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_REMOVEFROMCRL_ID,
                                                                                        pCA_NAME, "", intRes, sRes, sCERTIFICATION_ID, Integer.parseInt(ID));
                                                                                    if (intRes[0] == 0) {
                                                                                        db.S_BO_CERTIFICATION_RECOVERED(ID, loginUID);
                                                                                        sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                                                        strView = "0#0";
                                                                                        String urlCallback = "";
                                                                                        String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED;
                                                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                        if(rsBranch != null && rsBranch[0].length > 0){
                                                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                        }
                                                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                        Thread t = new Thread(thhreadLog);
                                                                                        t.start();
                                                                                    } else {
                                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                    }
                                                                                } else {
                                                                                    strView = "1#0";
                                                                                }
                                                                            } else if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                                                                // TYPE REISSUE
                                                                                if(!sAGENT_ID.equals(sAGENT_ID_New) || !sUSER_ID.equals(sUSER_ID_New)) {
                                                                                    String strReqValueATTRForDecline = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                                    db.S_BO_CERTIFICATION_CHANGED_CREATED_BY(ID, sAGENT_ID_New, sUSER_ID_New, loginUID,
                                                                                        strReqValueATTRForDecline);
                                                                                }
                                                                                db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                                if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN)) {
                                                                                    if ("1".equals(CheckPUSH_NOTICE)) {
                                                                                        int[] intRes = new int[1];
                                                                                        String[] sRes = new String[1];
                                                                                        ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                                    }
                                                                                }
                                                                                if (pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                                    String sOLD_TOKEN_ID = "";
                                                                                    CERTIFICATION[][] rsGetTokenID = new CERTIFICATION[1][];
                                                                                    db.S_BO_CERTIFICATION_DETAIL(pPAST_CERTIFICATE_ID, sessLanguage, rsGetTokenID);
                                                                                    if (rsGetTokenID[0].length > 0) {
                                                                                        sOLD_TOKEN_ID = String.valueOf(rsGetTokenID[0][0].TOKEN_ID);
                                                                                    }
                                                                                    TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                                                    int sTOKEN_STATE_ID_OLD = 0;
                                                                                    String sTOKEN_SN_OLD = "";
                                                                                    db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsToken_OLD);
                                                                                    if (rsToken_OLD[0].length > 0) {
                                                                                        sTOKEN_STATE_ID_OLD = rsToken_OLD[0][0].TOKEN_STATE_ID;
                                                                                        sTOKEN_SN_OLD = EscapeUtils.CheckTextNull(rsToken_OLD[0][0].TOKEN_SN);
                                                                                    }
                                                                                    if (sTOKEN_STATE_ID_OLD != Definitions.CONFIG_TOKEN_STATE_ID_PERMANENT_INITIALZED) {
                                                                                        ATTRIBUTE_VALUES valueATTR_TOKEN;
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
                                                                                    }
                                                                                } else {
                                                                                    String urlCallback = "";
                                                                                    String requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                    db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                    if(rsBranch != null && rsBranch[0].length > 0){
                                                                                        urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                    }
                                                                                    ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                        urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                    Thread t = new Thread(thhreadLog);
                                                                                    t.start();
                                                                                }
                                                                                request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                strView = "0#0";
                                                                            } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                                                                            {
                                                                                if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                    || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                                                                                {
                                                                                    //<editor-fold defaultstate="collapsed" desc="### TOKEN AND CERT TRANSFER">
                                                                                    if(!sAGENT_ID.equals(sAGENT_ID_New) || !sUSER_ID.equals(sUSER_ID_New))
                                                                                    {
                                                                                        String strReqValueATTRForDecline = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                                        db.S_BO_CERTIFICATION_CHANGED_CREATED_BY(ID, sAGENT_ID_New, sUSER_ID_New, loginUID, strReqValueATTRForDecline);
                                                                                    }
                                                                                    if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                                    {
                                                                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
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
                                                                                        if("0".equals(sNoAllowTranferToken)) {
                                                                                            if(!sAGENT_ID.equals(sAGENT_ID_New))
                                                                                            {
                                                                                                if(!sAGENT_ID_New.equals(sAGENT_TOKEN_ID))
                                                                                                {
                                                                                                    db.S_BO_TOKEN_UPDATE_BRANCH(String.valueOf(pTOKEN_ID), sAGENT_ID_New, loginUID);
                                                                                                }
                                                                                            } else {
                                                                                                if(!sAGENT_ID.equals(sAGENT_TOKEN_ID))
                                                                                                {
                                                                                                    db.S_BO_TOKEN_UPDATE_BRANCH(String.valueOf(pTOKEN_ID), sAGENT_ID, loginUID);
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    //</editor-fold>
                                                                                }
                                                                                String CheckDeleteRevoke = EscapeUtils.CheckTextNull(request.getParameter("CheckDeleteRevoke"));
                                                                                if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN) || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                                    || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN) || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                                    CheckDeleteRevoke = "0";
                                                                                }
                                                                                if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                    || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                                                                                {
                                                                                    CheckDeleteRevoke = "0";
                                                                                }
                                                                                boolean sRevokeDeleteInTokenEnabled = "1".equals(CheckDeleteRevoke);
                                                                                valueATTR_Last.setDeleteOldCertificateEnabled(sRevokeDeleteInTokenEnabled);
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                                
                                                                                //<editor-fold defaultstate="collapsed" desc="### PROMOTION PROFILE">
                                                                                if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                    || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                    || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                                                                                {
                                                                                    if(!"".equals(DURATION_FREE)) {
                                                                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                                        if(rsProfile[0].length > 0) {
                                                                                            pCERT_PROFILE_DURATION = rsProfile[0][0].DURATION;
                                                                                        }
                                                                                        System.out.println("DURATION_FREE: " + DURATION_FREE + "; pCERT_PROFILE_DURATION: " + pCERT_PROFILE_DURATION);
                                                                                        if(Integer.parseInt(DURATION_FREE) <= pCERT_PROFILE_DURATION) {
                                                                                            int pDURATION_NEW = pCERT_PROFILE_DURATION + Integer.parseInt(DURATION_FREE);
                                                                                            if(pDURATION_NEW != pDURATION_OLD) {
                                                                                                db.S_BO_CERTIFICATION_UPDATE_DURATION(sCERTIFICATION_ID, pDURATION_NEW, null, loginUID);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                //</editor-fold>
                                                                                
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                                if ("0".equals(sApprove)) {
                                                                                    //<editor-fold defaultstate="collapsed" desc="### PROFILE MANAGER PROCESS">
                                                                                    if("2".equals(profileManagerCAOption)) {
                                                                                        if (Definitions.CONFIG_AGENT_ROOT.equals(AGENT_ID_LOG)) {
                                                                                            String STATE_PROFILE = EscapeUtils.CheckTextNull(request.getParameter("STATE_PROFILE"));
                                                                                            CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessCollectedBriefPro");
                                                                                            String pBRIEF_PROPERTIES = "";
                                                                                            if(resProfileData != null && resProfileData[0].length > 0){
                                                                                                pBRIEF_PROPERTIES = CommonFunction.renderBriefFileType(resProfileData[0], "", idReceivedNote, "");
                                                                                            }
                                                                                            String param1 = db.S_BO_CERTIFICATION_BRIEF_UPDATE_COLLECT_SOFTCOPY(String.valueOf(sCERTIFICATION_ID),
                                                                                                pBRIEF_PROPERTIES, sCheckReceivedSoftCopy, loginUID, STATE_PROFILE);
                                                                                            request.getSession(false).setAttribute("SessCollectedBriefPro", null);
                                                                                            CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_BRIEF_UPDATE_COLLECT_SOFTCOPY Result", param1);
                                                                                            db.S_BO_FILE_MANAGER_UPDATE_COMMIT_ENABLED(String.valueOf(sCERTIFICATION_ID), loginUID);
                                                                                        }
                                                                                    }
                                                                                    //</editor-fold>
                                                                                    
                                                                                    //<editor-fold defaultstate="collapsed" desc="### REPRESENTIVE PROCESS">
                                                                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                                                    String userID = request.getSession(false).getAttribute("UserID").toString().trim();
                                                                                    String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
                                                                                    if("1".equals(sRepresentEnabled)) {
                                                                                        if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
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
                                                                                            db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(sCERTIFICATION_ID, objectMapper.writeValueAsString(profileContact), userID);
                                                                                        }
                                                                                    }
                                                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)){
                                                                                        String registerNCRepresentative = EscapeUtils.CheckTextNull(request.getParameter("registerNCRepresentative"));
                                                                                        String registerNCAddress = EscapeUtils.CheckTextNull(request.getParameter("registerNCAddress"));
                                                                                        ProfileContactInfoJson profileContact = new ProfileContactInfoJson();
                                                                                        profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(registerNCRepresentative, true);
                                                                                        profileContact.Address = CommonFunction.replaceCharaterSpecialJson(registerNCAddress, true);
                                                                                        objectMapper = new ObjectMapper();
                                                                                        db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(sCERTIFICATION_ID, objectMapper.writeValueAsString(profileContact), userID);
                                                                                    }
                                                                                    //</editor-fold>

                                                                                    // set COMMIT TRUE File Attachment
                                                                                    String[] pRESPONSE_CODE_FILE = new String[1];
                                                                                    db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(Integer.parseInt(ID), loginUID, pRESPONSE_CODE_FILE);
                                                                                    if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN))
                                                                                    {
                                                                                        if (sPUSH_NOTICE_ENABLED == true) {
                                                                                            // RA SEND_EMAIL
                                                                                            int[] intRes = new int[1];
                                                                                            String[] sRes = new String[1];
                                                                                            ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                                                                        }
                                                                                        request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                        strView = "0#0";
                                                                                    } else if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                                                                                        || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN)
                                                                                        || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN)
                                                                                        || pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)) {
                                                                                        ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                        
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
                                                                                                db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(sCERTIFICATION_ID), strDNSName, loginUID);
                                                                                            }
                                                                                        }
                                                                                        //</editor-fold>
                                                                                        if(pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
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
                                                                                                String checkCertConfirm = EscapeUtils.CheckTextNull(request.getParameter("checkCertConfirm"));
                                                                                                if("1".equals(checkCertConfirm)) {
                                                                                                    CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(sCERTIFICATION_ID), DN, strEmailCust, sessLanguage);
                                                                                                    strView = "0#0";
                                                                                                } else {
                                                                                                    dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(Integer.parseInt(ID), 1);
                                                                                                    int[] intWSRes = new int[1];
                                                                                                    String[] sWSRes = new String[1];
                                                                                                    ConnectConnector.EnrollCertificate(pTOKEN_SN, strPasswordP12, ID, intWSRes, sWSRes);
                                                                                                    if (intWSRes[0] == 0) {
                                                                                                        request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                                        strView = "0#0";
                                                                                                    } else {
                                                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                                    }
                                                                                                }

                                                                                            } else {
                                                                                                dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(Integer.parseInt(ID), 1);
                                                                                                int[] intWSRes = new int[1];
                                                                                                String[] sWSRes = new String[1];
                                                                                                ConnectConnector.EnrollCertificate(pTOKEN_SN, strPasswordP12, ID, intWSRes, sWSRes);
                                                                                                if (intWSRes[0] == 0) {
                                                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                                    strView = "0#0";
                                                                                                } else {
                                                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                            strView = "0#0";
                                                                                            String urlCallback = "";
                                                                                            String requestType = "";
                                                                                            if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                                                                                requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                                                            } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                                                                                requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                                                            } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                                                requestType = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                                                            }
                                                                                            BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                            db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                            if(rsBranch != null && rsBranch[0].length > 0){
                                                                                                urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                            }
                                                                                            ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(sCERTIFICATION_ID),
                                                                                                urlCallback, Definitions.CONFIG_OPERATION_TYPE_APPROVED, "", requestType);
                                                                                            Thread t = new Thread(thhreadLog);
                                                                                            t.start();
                                                                                        }
                                                                                    } else {
                                                                                        request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                        strView = "0#0";
                                                                                    }
                                                                                } else {
                                                                                    strView = sApprove + "#0";
                                                                                }
                                                                            } else {
                                                                                // TYPE ANOTHER
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                                if ("0".equals(sApprove)) {
                                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                                    strView = "0#0";
                                                                                } else {
                                                                                    strView = sApprove + "#0";
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    //</editor-fold>
                                                                }
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_CSR_KEYSIZE + "#0";
                                                            }
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_CSR_NULL + "#0";
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
                                                            RSSPProcessCommon clsRSSP = new RSSPProcessCommon();
                                                            String[] sResultRSSP = new String[2];
                                                            String sAgreementUUID = "";
                                                            String sRsspRelyingParty = "";
                                                            String sChangeKeyApprove = "";
                                                            boolean revokeSetOldStatus = false;
                                                            if (!"".equals(sVALUE_OLD)) {
                                                                ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                sAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                                sRsspRelyingParty = valueATTR_Frist.getRsspRelyingParty();
                                                                revokeSetOldStatus = valueATTR_Frist.getRevokeSetOldStatusEnabled();
                                                                if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                    || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                    sChangeKeyApprove = valueATTR_Frist.getChangeKeyEnabled() ? "1" : "0";
                                                                }
                                                            }
                                                            if(!"".equals(sAgreementUUID)) {
                                                                if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                    || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                                    rsReq = new CERTIFICATION[1][];
                                                                    db.S_BO_CERTIFICATION_DETAIL(pPAST_CERTIFICATE_ID, sessLanguage, rsReq);
                                                                    if (rsReq[0].length > 0) {
                                                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                                                    }
                                                                }
                                                                String sRequestTypeRssp = "";
                                                                if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL){
                                                                    sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_RENEW_CERT;
                                                                } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO){
                                                                    sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_CHANGE_CERT;
                                                                } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE){
                                                                    sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_REVOKE_CERT;
                                                                } else {
                                                                    sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_PREPARE_CERT;
                                                                }
                                                                clsRSSP.approveCertificateForSignCloud(sAgreementUUID, String.valueOf(sCERTIFICATION_ID),
                                                                    CheckREVOKE_ENABLED, sRsspRelyingParty, sResultRSSP, pCERTIFICATION_SN, sChangeKeyApprove,
                                                                    sRequestTypeRssp, "", credentialAuthen, revokeSetOldStatus);
                                                                if("0".equals(sResultRSSP[0])) {
                                                                    //<editor-fold defaultstate="collapsed" desc="### PROFILE MANAGER PROCESS">
                                                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                                    if("2".equals(profileManagerCAOption) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_HILO)
                                                                         || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                        if (Definitions.CONFIG_AGENT_ROOT.equals(AGENT_ID_LOG)) {
                                                                            String STATE_PROFILE = EscapeUtils.CheckTextNull(request.getParameter("STATE_PROFILE"));
//                                                                            CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessCollectedBriefPro");
                                                                            String pBRIEF_PROPERTIES = "";
//                                                                            if(resProfileData != null && resProfileData[0].length > 0){
//                                                                                pBRIEF_PROPERTIES = CommonFunction.renderBriefFileType(resProfileData[0], "", idReceivedNote, "");
//                                                                            }
                                                                            String param1 = db.S_BO_CERTIFICATION_BRIEF_UPDATE_COLLECT_SOFTCOPY(String.valueOf(sCERTIFICATION_ID),
                                                                                pBRIEF_PROPERTIES, "", loginUID, STATE_PROFILE);
                                                                            request.getSession(false).setAttribute("SessCollectedBriefPro", null);
                                                                            CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_BRIEF_UPDATE_COLLECT_SOFTCOPY Result", param1);
                                                                            db.S_BO_FILE_MANAGER_UPDATE_COMMIT_ENABLED(String.valueOf(sCERTIFICATION_ID), loginUID);
                                                                            //<editor-fold defaultstate="collapsed" desc="### REPRESENTIVE PROCESS">
                                                                            String userID = request.getSession(false).getAttribute("UserID").toString().trim();
                                                                            String sRepresentEnabled = LoadParamSystem.getParamStart(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
                                                                            if("1".equals(sRepresentEnabled)) {
                                                                                if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                                    || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                                    || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
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
                                                                                    db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(sCERTIFICATION_ID, objectMapper.writeValueAsString(profileContact), userID);
                                                                                }
                                                                            }
                                                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)){
                                                                                String registerNCRepresentative = EscapeUtils.CheckTextNull(request.getParameter("registerNCRepresentative"));
                                                                                String registerNCAddress = EscapeUtils.CheckTextNull(request.getParameter("registerNCAddress"));
                                                                                ProfileContactInfoJson profileContact = new ProfileContactInfoJson();
                                                                                profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(registerNCRepresentative, true);
                                                                                profileContact.Address = CommonFunction.replaceCharaterSpecialJson(registerNCAddress, true);
                                                                                objectMapper = new ObjectMapper();
                                                                                db.S_BO_CERTIFICATION_UPDATE_CONTACT_INFO(sCERTIFICATION_ID, objectMapper.writeValueAsString(profileContact), userID);
                                                                            }
                                                                            //</editor-fold>
                                                                        }
                                                                    }
                                                                    //</editor-fold>
                                                                                
                                                                    // Integer.parseInt(ID)
                                                                    String[] pRESPONSE_CODE_FILE = new String[1];
                                                                    db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(Integer.parseInt(ID), loginUID, pRESPONSE_CODE_FILE);
                                                                    sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                                    strView = "0#0";
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
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_DENIED_FUNCTION + "#0";
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_HAS_STATUS + "#0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_APPROVE_AGENCY + "#0";
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
                            case "updatecertagency": {
                                //<editor-fold defaultstate="collapsed" desc="updatecertagency">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String sAGENT_ID_New = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ID"));
                                    String sUSER_ID_New = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                    String CheckCHANGE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckCHANGE_KEY"));
                                    String CheckPRIVATE_KEY = EscapeUtils.CheckTextNull(request.getParameter("CheckPRIVATE_KEY"));
                                    String CheckREVOKE_ENABLED = EscapeUtils.CheckTextNull(request.getParameter("CheckREVOKE_ENABLED"));
                                    String keepCertSNEnabled = EscapeUtils.CheckTextNull(request.getParameter("keepCertSNEnabled"));
                                    String pCOMPONENT_SAN = EscapeUtils.CheckTextNull(request.getParameter("COMPONENT_SAN"));
                                    CommonFunction.LogDebugString(log, "APPROVE-AGENCY", "AGENCY: " + sAGENT_ID_New + "; USER: " + sUSER_ID_New);
                                    String sAGENT_ID="";
                                    String sUSER_ID="";
                                    String sTOKEN_SN = "";
                                    String sVALUE_OLD = "";
                                    String sCSR = "";
                                    String strReqValueATTR = "";
                                    int sCERTIFICATION_ID = 0;
                                    int sCERTIFICATION_ATTR_TYPE_ID = 0;
                                    int pPKI_FORMFACTOR_ID = 0;
                                    String sProfileIDBrowser = EscapeUtils.CheckTextNull(request.getParameter("CertProfileID"));
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    if(!"".equals(sProfileIDBrowser)) {
                                        pCERTIFICATION_PROFILE_ID = Integer.parseInt(sProfileIDBrowser);
                                    }
                                    int sStatusRequest = 0;
                                    boolean pPRIVATE_KEY_ENABLED = true;
                                    // check agency
                                    boolean isAccessAgency = true;
                                    String pPAST_CERTIFICATE_ID = "";
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sUSER_ID = String.valueOf(rsReq[0][0].CREATED_BY_ID);
                                        sStatusRequest = rsReq[0][0].CERTIFICATION_ATTR_STATE_ID;
                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        pPAST_CERTIFICATE_ID = String.valueOf(rsReq[0][0].PAST_CERTIFICATION_ID);
                                        sCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                        sCERTIFICATION_ID = rsReq[0][0].ID;
                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                        sCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        if ((sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                                && !AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            boolean functionAccess = false;
                                            //<editor-fold defaultstate="collapsed" desc="### CHECK ACCESS FUNCTION">
                                            String SessRoleID = sessionsa.getAttribute("RoleID_ID").toString().trim();
                                            if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) && !SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)){
                                                functionAccess = true;
                                            }
                                            //</editor-fold>
                                            
                                            if(functionAccess == true)
                                            {
                                                boolean isCSRValid = true;
                                                boolean isCSR_SizeValid = true;
                                                String strDNSName = "";
                                                if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                {
                                                    if(pPRIVATE_KEY_ENABLED == true)
                                                    {
                                                        CheckPRIVATE_KEY = "1";
                                                    } else {
                                                        if("1".equals(CheckCHANGE_KEY))
                                                        {
                                                            sCSR = EscapeUtils.CheckTextNull(request.getParameter("CSR"));
                                                            if("".equals(sCSR))
                                                            {
                                                                isCSRValid = false;
                                                            }
                                                            if(isCSRValid == true)
                                                            {
                                                                String sKeySizeDB;
                                                                isCSR_SizeValid = false;
                                                                CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                                                db.S_BO_GET_ALGORITHM_KEY_SIZE(String.valueOf(pCERTIFICATION_PROFILE_ID), rsCert);
                                                                if(rsCert[0].length > 0)
                                                                {
                                                                    sKeySizeDB = EscapeUtils.CheckTextNull(rsCert[0][0].KEY_SIZE);
                                                                    String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(sCSR);
                                                                    isCSR_SizeValid = sKeySizeDB.equals(sKeySizeCSR);
                                                                }
                                                            }
                                                        }
                                                        CheckPRIVATE_KEY = "0";
                                                    }
                                                }
                                                if(isCSRValid == true)
                                                {
                                                    if(isCSR_SizeValid == true)
                                                    {
                                                        //<editor-fold defaultstate="collapsed" desc="### PARAM and GET INFO">
                                                        String PHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("PHONE_CONTRACT"));
                                                        String EMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("EMAIL_CONTRACT"));
                                                        String CACoreSubject = EscapeUtils.CheckTextNull(request.getParameter("CACoreSubject"));
                                                        String CertProfileID = sProfileIDBrowser;
                                                        String DN = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                                        String pPERSONAL_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pPERSONAL_NAME"));
                                                        String pCOMPANY_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pCOMPANY_NAME"));
                                                        String pDOMAIN_NAME = EscapeUtils.checkTextNullAndXSS(request.getParameter("pDOMAIN_NAME"));
                                                        String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                                        String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                                        String pTAX_CODE = "";// EscapeUtils.CheckTextNull(request.getParameter("pTAX_CODE"));
                                                        String pDECISION = "";//EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                                        String pBUDGET_CODE = "";//EscapeUtils.CheckTextNull(request.getParameter("pBUDGET_CODE"));
                                                        String pP_ID = "";//EscapeUtils.CheckTextNull(request.getParameter("pP_ID"));
                                                        String pCCCD = "";//EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                                        String pPASSPORT = "";//EscapeUtils.CheckTextNull(request.getParameter("pPASSPORT"));
                                                        String pPROVINCE_ID = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_ID"));
                                                        String pPROVINCE_DESC = EscapeUtils.CheckTextNull(request.getParameter("pPROVINCE_DESC"));
                                                        String pDEVICE = EscapeUtils.CheckTextNull(request.getParameter("pDEVICE"));
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                                            if("0".equals(CheckCHANGE_KEY)) {
                                                                CERTIFICATION[][] rsCertOld = new CERTIFICATION[1][];
                                                                db.S_BO_CERTIFICATION_DETAIL(pPAST_CERTIFICATE_ID, sessLanguage, rsCertOld);
                                                                if(rsCertOld[0].length > 0) {
                                                                    DN = EscapeUtils.CheckTextNull(rsCertOld[0][0].SUBJECT);
                                                                    pPERSONAL_NAME = EscapeUtils.CheckTextNull(rsCertOld[0][0].PERSONAL_NAME);
                                                                    pCOMPANY_NAME = EscapeUtils.CheckTextNull(rsCertOld[0][0].COMPANY_NAME);
                                                                    pDOMAIN_NAME = EscapeUtils.CheckTextNull(rsCertOld[0][0].DOMAIN_NAME);
                                                                    pENTERPRISE_ID = rsCertOld[0][0].ENTERPRISE_ID;
                                                                    pPERSONAL_ID = rsCertOld[0][0].PERSONAL_ID;
                                                                    pPROVINCE_ID = String.valueOf(rsCertOld[0][0].CITY_PROVINCE_ID);
                                                                    pDEVICE = EscapeUtils.CheckTextNull(rsCertOld[0][0].SERVICE_UUID);
                                                                }
                                                            }
                                                        }
                                                        CommonFunction.LogDebugString(log, "REGISTRATION-CERTIFICATION", "REQUEST: " + "SUBJECT: " + DN
                                                                + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME
                                                                + "; pDOMAIN_NAME: " + pDOMAIN_NAME + "; pENTERPRISE_ID: " + pENTERPRISE_ID
                                                                + "; pPERSONAL_ID: " + pPERSONAL_ID + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                                + "; pCERTIFICATION_ATTR_TYPE_ID: " + sCERTIFICATION_ATTR_TYPE_ID
                                                                + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                                + "; CACoreSubject: " + CACoreSubject + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                                + "; PROVINCE_ID: " + pPROVINCE_ID + "; Cert_ProfileID: " + CertProfileID + "; TOKEN_SN: " + sTOKEN_SN);
                                                        tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                        objectMapper = new ObjectMapper();
                                                        tempLogReq.personalName = pPERSONAL_NAME;
                                                        tempLogReq.companyName = pCOMPANY_NAME;
                                                        tempLogReq.domainName = pDOMAIN_NAME;
                                                        tempLogReq.deviceUUID = pDEVICE;
                                                        tempLogReq.personalID = pPERSONAL_ID;
                                                        tempLogReq.enterpriseID = pENTERPRISE_ID;
                                                        tempLogReq.emailContract = EMAIL_CONTRACT;
                                                        tempLogReq.phoneContract = PHONE_CONTRACT;
                                                        tempLogReq.issuerSubject = CACoreSubject;
                                                        tempLogReq.subjectDn = DN;
                                                        tempLogReq.provinceName = pPROVINCE_DESC;
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_BUY_MORE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_COMPENSATION)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_COMPENSATION;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE;
                                                        }
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
                                                        {
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED;
                                                        }
                                                        //</editor-fold>
                                                        String strReq = objectMapper.writeValueAsString(tempLogReq);
                                                        db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME, Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                            Definitions.CONFIG_LOG_FUNCTIONALITY_APPROVE_AGENCY, strReq, loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                        if (sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                                        {
                                                            db.S_BO_CERTIFICATION_UPDATE(sCERTIFICATION_ID, CertProfileID, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME,
                                                                pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, DN, CACoreSubject, PHONE_CONTRACT,
                                                                EMAIL_CONTRACT, pPROVINCE_ID, CheckPRIVATE_KEY, loginUID, "", sCSR, pDEVICE, "", "",
                                                                pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                        }
                                                        ATTRIBUTE_VALUES valueATTR_Last = null;
                                                        String idCERT_REVOCATION_REASON = "";
                                                        String sCertRevokeReason_Frist = "";
                                                        java.sql.Timestamp sCertSuspendTime_Frist = null;
                                                        String sCertSuspendReason_Frist = "";
                                                        boolean isDeleteCertInToken = true;
                                                        //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR">
                                                        if (!"".equals(sVALUE_OLD)) {
                                                            // VALUE ATTR_FRIST
                                                            ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                            String sToken_Frist = valueATTR_Frist.getTokenSn();
                                                            sCertRevokeReason_Frist = valueATTR_Frist.getCerttificateRevokeReason();
                                                            idCERT_REVOCATION_REASON = valueATTR_Frist.getCerttificateRevokeEJBCAReason();
                                                            sCertSuspendReason_Frist = valueATTR_Frist.getCerttificateSuspendReason();
                                                            isDeleteCertInToken = valueATTR_Frist.getCertRevokeDeleteInTokenEnabled();
                                                            sCertSuspendTime_Frist = valueATTR_Frist.getSuspendedTime();
                                                            String sTypeName_Frist = valueATTR_Frist.getTypeName();
                                                            String sCreateUser_Frist = valueATTR_Frist.getCreateUser();
                                                            String sRsspAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                            String sRsspRelyingParty = valueATTR_Frist.getRsspRelyingParty();
                                                            Date sCreateDt_Frist = valueATTR_Frist.getCreateDt();
                                                            // VALUE ATTR_LAST
                                                            valueATTR_Last = new ATTRIBUTE_VALUES();
                                                            ATTRIBUTE_DATA dataATTR_Last = new ATTRIBUTE_DATA();
                                                            dataATTR_Last.setCertificationData(tempLogReq);
                                                            valueATTR_Last.setTokenSn(sToken_Frist);
                                                            valueATTR_Last.setRsspAgreementUUID(sRsspAgreementUUID);
                                                            valueATTR_Last.setRsspRelyingParty(sRsspRelyingParty);
                                                            boolean sChangeKeyEnabled = "1".equals(CheckCHANGE_KEY);
                                                            valueATTR_Last.setChangeKeyEnabled(sChangeKeyEnabled);
                                                            boolean sRevokedEnabled = "1".equals(CheckREVOKE_ENABLED);
                                                            valueATTR_Last.setRevokeOldCertificateEnabled(sRevokedEnabled);
                                                            boolean booKeepCertSNEnabled = "1".equals(keepCertSNEnabled);
                                                            valueATTR_Last.setKeepCertificateSNEnabled(booKeepCertSNEnabled);
                                                            valueATTR_Last.setCerttificateRevokeReason(sCertRevokeReason_Frist);
                                                            valueATTR_Last.setCerttificateRevokeEJBCAReason(idCERT_REVOCATION_REASON);
                                                            valueATTR_Last.setCerttificateSuspendReason(sCertSuspendReason_Frist);
                                                            valueATTR_Last.setCertRevokeDeleteInTokenEnabled(isDeleteCertInToken);
                                                            valueATTR_Last.setCerttificateDeclineReason("");
                                                            valueATTR_Last.setSuspendedTime(sCertSuspendTime_Frist);
                                                            valueATTR_Last.setTypeName(sTypeName_Frist);
                                                            valueATTR_Last.setRequestState(valueATTR_Frist.getRequestState());
                                                            valueATTR_Last.setCreateUser(sCreateUser_Frist);
                                                            valueATTR_Last.setCreateDt(sCreateDt_Frist);
                                                            valueATTR_Last.setApproveUser(loginFullname + " (" + loginUID + ")");
                                                            valueATTR_Last.setApproveDt(new Date());
                                                            valueATTR_Last.setAttributeData(dataATTR_Last);
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                            db.S_BO_CERTIFICATION_ATTR_UPDATE_AGREEMENT_UUID(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                        }
                                                        //</editor-fold>

                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                            || sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE)
                                                        {
                                                            if(!sUSER_ID.equals(sUSER_ID_New))
                                                            {
                                                                if(!sUSER_ID.equals(sUSER_ID_New)) {
                                                                    String strReqValueATTRForDecline = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                                    db.S_BO_CERTIFICATION_CHANGED_CREATED_BY(ID, sAGENT_ID, sUSER_ID_New, loginUID, strReqValueATTRForDecline);
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
                                                                db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(sCERTIFICATION_ID), strDNSName, loginUID);
                                                            }
                                                        }
                                                        //</editor-fold>
                                                            
                                                        strView = "0#0";
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_CSR_KEYSIZE + "#0";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_CSR_NULL + "#0";
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_DENIED_FUNCTION + "#0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_HAS_STATUS + "#0";
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
                            case "declinecert": {
                                //<editor-fold defaultstate="collapsed" desc="declinecert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String idATTR = EscapeUtils.CheckTextNull(request.getParameter("idATTR"));
                                    String idDESC_DECLINE = EscapeUtils.checkTextNullAndXSS(request.getParameter("DESC_DECLINE"));
                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    // check agency
                                    String sAGENT_ID="0";
                                    String sPAST_CERTIFICATE_ID = "";
                                    String pCERTIFICATION_AUTHORITY_ID = "";
                                    String pCERTIFICATION_SN = "";
                                    String strIsPushNotiDecline = "0";
                                    String pTOKEN_SN = "";
                                    String sVALUE_OLD = "";
                                    int sStatusRequest = 0;
                                    int pCERTIFICATION_ID = 0;
                                    int sStatusCert = 0;
                                    int intCERTIFICATION_ATTR_TYPE_ID=0;
                                    int pPKI_FORMFACTOR_ID=0;
                                    boolean isAccessAgency = true;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(idATTR, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_ID = rsReq[0][0].ID;
                                        pTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                        pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                        sPAST_CERTIFICATE_ID = String.valueOf(rsReq[0][0].PAST_CERTIFICATION_ID);
                                        intCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        sStatusRequest = rsReq[0][0].CERTIFICATION_ATTR_STATE_ID;
                                        sStatusCert = rsReq[0][0].CERTIFICATION_STATE_ID;
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    //</editor-fold>
                                    
                                    if (isAccessAgency == true) {
                                        //<editor-fold defaultstate="collapsed" desc="### CHECK AGENCY DECLINE">
                                        String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                        String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                        String SessRoleID = sessionsa.getAttribute("RoleID_ID").toString().trim();
                                        if (sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                            || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                                            if(AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                isAccessAgency = true;
                                            } else {
                                                if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                        isAccessAgency = true;
                                                    } else {
                                                        int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(idATTR), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                        if(intApprove == 1) {
                                                            isAccessAgency = true;
                                                        }
                                                    }
                                                } else {
                                                    if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                            isAccessAgency = true;
                                                        }
                                                    } else {
                                                        int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(Integer.parseInt(idATTR), Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                        if(intApprove == 1) {
                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                isAccessAgency = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        //</editor-fold>
                                    }
                                    
                                    if (isAccessAgency == true) {
                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                        {
                                            CommonFunction.LogDebugString(log, "DECLINE_CERTIFICATION_ATTR", "USER_LOGIN: " + loginUID
                                                + "; STATUS_CERTIFICATION_ATTR: " + sStatusRequest + "; CERTIFICATION_ATTR_TYPE_ID: " + intCERTIFICATION_ATTR_TYPE_ID
                                                 + "; AGENT_ID_LOG: " + AGENT_ID_LOG);
                                            Config conf = new Config();
                                            boolean booRSSP_ACCESS_ENABLED = false;
                                            if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                                if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                    booRSSP_ACCESS_ENABLED = true;
                                                    if (!"".equals(sVALUE_OLD)) {
                                                        objectMapper = new ObjectMapper();
                                                        ATTRIBUTE_VALUES valueRSSP = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                        if(valueRSSP.getTokenSn().trim().equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN)) {
                                                            booRSSP_ACCESS_ENABLED = false;
                                                        }
                                                    }
                                                }
                                            }
                                            if(booRSSP_ACCESS_ENABLED == false) {
                                                String sVALUE_NEW = "";
                                                String sVALUE_SCAN = "";
                                                if(!"".equals(sVALUE_OLD))
                                                {
                                                    objectMapper = new ObjectMapper();
                                                    ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                    valueATTR_Frist.setCerttificateDeclineReason(idDESC_DECLINE);
                                                    sVALUE_NEW = CommonFunction.GenJSONTokenATTR(valueATTR_Frist);
                                                    // certificationAttrId
                                                    valueATTR_Frist.setCertificationAttrId(idATTR);
                                                    sVALUE_SCAN = CommonFunction.GenJSONTokenATTR(valueATTR_Frist);
                                                }
                                                if(intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                    && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                                                    && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                {
                                                    objectMapper = new ObjectMapper();
                                                    CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                    jsonCertComment.certificateDeclineReason = idDESC_DECLINE;
                                                    jsonCertComment.certificateRevokeReason = "";
                                                    jsonCertComment.certificateSuspendReason = "";
                                                    idDESC_DECLINE = objectMapper.writeValueAsString(jsonCertComment);
                                                } else {
                                                    idDESC_DECLINE = "";
                                                }
                                                if (sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                    || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                                {
                                                    db.S_BO_CERTIFICATION_REMOVED(Integer.parseInt(idATTR), idDESC_DECLINE, sVALUE_NEW, loginUID);
                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                    strView = "0#0#" + strIsPushNotiDecline;
                                                    if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                        String urlCallback = "";
                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                        if(rsBranch != null && rsBranch[0].length > 0){
                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                        }
                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(pCERTIFICATION_ID),
                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_DECLINED, idDESC_DECLINE, "");
                                                        Thread t = new Thread(thhreadLog);
                                                        t.start();
                                                    }
                                                } else if (sStatusRequest != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED
                                                    && sStatusRequest != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                                                    && sStatusRequest != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                                                {
                                                    if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        if(sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                                            && (intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                            && (sStatusCert == Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                                                || sStatusCert == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                                                || sStatusCert == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                                                || sStatusCert == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                                                || sStatusCert == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE))
                                                        { }
                                                        else {
                                                            strIsPushNotiDecline = "1";
                                                            db.S_BO_CERTIFICATION_REMOVED(Integer.parseInt(idATTR), idDESC_DECLINE, sVALUE_NEW, loginUID);
                                                            String sOLD_TOKEN_ID = "";
                                                            if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                String urlCallback = "";
                                                                BRANCH[][] rsBranch = new BRANCH[1][];
                                                                db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                if(rsBranch != null && rsBranch[0].length > 0){
                                                                    urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                }
                                                                ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(pCERTIFICATION_ID),
                                                                    urlCallback, Definitions.CONFIG_OPERATION_TYPE_DECLINED, idDESC_DECLINE, "");
                                                                Thread t = new Thread(thhreadLog);
                                                                t.start();
                                                            }
                                                            CERTIFICATION[][] rsGetToken = new CERTIFICATION[1][];
                                                            db.S_BO_CERTIFICATION_DETAIL(sPAST_CERTIFICATE_ID, sessLanguage, rsGetToken);
                                                            if (rsGetToken[0].length > 0) {
                                                                sOLD_TOKEN_ID = String.valueOf(rsGetToken[0][0].TOKEN_ID);
                                                            }
                                                            boolean IsDecline_PERMANENT_INITIALZED = false;
                                                            String sToken_AtrrID = "";
                                                            TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                            db.S_BO_TOKEN_GET_ATTR(sOLD_TOKEN_ID, sessLanguage, rsToken_OLD);
                                                            if (rsToken_OLD[0].length > 0) {
                                                                for (TOKEN rsToken_OLD1 : rsToken_OLD[0]) {
                                                                    if (rsToken_OLD1.TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PERMANENT_INITIALZED) {
                                                                        sToken_AtrrID = String.valueOf(rsToken_OLD1.TOKEN_ATTR_ID);
                                                                        IsDecline_PERMANENT_INITIALZED = true;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (IsDecline_PERMANENT_INITIALZED == true) {
                                                                db.S_BO_TOKEN_ATTR_DECLINED(Integer.parseInt(sToken_AtrrID), loginUID);
                                                                TOKEN[][] rsTokenDetail = new TOKEN[1][];
                                                                db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsTokenDetail);
                                                                if(rsTokenDetail[0].length > 0){
                                                                    if(rsTokenDetail[0][0].TOKEN_STATE_ID == Definitions.CONFIG_TOKEN_STATE_ID_LOST)
                                                                    {
                                                                        db.S_BO_TOKEN_RECOVERY_STATE(sOLD_TOKEN_ID, loginUID);
                                                                    }
                                                                }
                                                            }
                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                            strView = "0#0#" + strIsPushNotiDecline;
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_HAS_STATUS + "#0";
                                                    }
                                                } else if(sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                                                {
                                                    if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        int[] intRes = new int[1];
                                                        String[] sRes = new String[1];
                                                        String raConnectorScan = conf.GetPropertybyCode(Definitions.CONFIG_RACONNECTOR_SCAN_ENABLED);
                                                        if("1".equals(raConnectorScan))
                                                        {
                                                            strIsPushNotiDecline = "1";
                                                            int[] pCERTIFICATE_ATTR_ID_REFUND = new int[1];
                                                            String sParam = db.S_BO_CERTIFICATION_REFUND(String.valueOf(pCERTIFICATION_ID), sVALUE_SCAN,
                                                                loginUID, pCERTIFICATE_ATTR_ID_REFUND);
                                                            if ("0".equals(sParam)) {
                                                                String pCA_NAME = "";
                                                                CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                    pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                }
                                                                ConnectConnector.RevokeCertificate(pTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID,
                                                                    pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID_REFUND[0]);
                                                                if (intRes[0] == 0) {
                                                                    if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                        String urlCallback = "";
                                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                        if(rsBranch != null && rsBranch[0].length > 0){
                                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                        }
                                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(pCERTIFICATION_ID),
                                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_DECLINED, idDESC_DECLINE,"");
                                                                        Thread t = new Thread(thhreadLog);
                                                                        t.start();
                                                                    }
                                                                    String sOLD_TOKEN_ID = "";
                                                                    CERTIFICATION[][] rsGetToken = new CERTIFICATION[1][];
                                                                    db.S_BO_CERTIFICATION_DETAIL(sPAST_CERTIFICATE_ID, sessLanguage, rsGetToken);
                                                                    if (rsGetToken[0].length > 0) {
                                                                        sOLD_TOKEN_ID = String.valueOf(rsGetToken[0][0].TOKEN_ID);
                                                                    }
                                                                    boolean IsDecline_PERMANENT_INITIALZED = false;
                                                                    String sToken_AtrrID = "";
                                                                    TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                                    db.S_BO_TOKEN_GET_ATTR(sOLD_TOKEN_ID, sessLanguage, rsToken_OLD);
                                                                    if (rsToken_OLD[0].length > 0) {
                                                                        for (TOKEN rsToken_OLD1 : rsToken_OLD[0]) {
                                                                            if (rsToken_OLD1.TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PERMANENT_INITIALZED) {
                                                                                sToken_AtrrID = String.valueOf(rsToken_OLD1.TOKEN_ATTR_ID);
                                                                                IsDecline_PERMANENT_INITIALZED = true;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    if (IsDecline_PERMANENT_INITIALZED == true) {
                                                                        db.S_BO_TOKEN_ATTR_DECLINED(Integer.parseInt(sToken_AtrrID), loginUID);
                                                                        TOKEN[][] rsTokenDetail = new TOKEN[1][];
                                                                        db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsTokenDetail);
                                                                        if(rsTokenDetail[0].length > 0){
                                                                            if(rsTokenDetail[0][0].TOKEN_STATE_ID == Definitions.CONFIG_TOKEN_STATE_ID_LOST)
                                                                            {
                                                                                db.S_BO_TOKEN_RECOVERY_STATE(sOLD_TOKEN_ID, loginUID);
                                                                            }
                                                                        }
                                                                    }
                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                    strView = "0#0#" + strIsPushNotiDecline;
                                                                } else {
                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                }
                                                            } else {
                                                                CommonFunction.LogErrorServlet(log, "S_BO_CERTIFICATION_REFUND: RESPONSE_CODE- " + sParam);
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                            }
                                                        } else {
                                                            strIsPushNotiDecline = "1";
                                                            // RACONNECTOR
                                                            String pCA_NAME = "";
                                                            CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                            db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                            if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                            }
                                                            ConnectConnector.RevokeCertificate(pTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID,
                                                                pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, Integer.parseInt(idATTR));
                                                            if (intRes[0] == 0) {
                                                                db.S_BO_CERTIFICATION_DESTROYED(idATTR, idDESC_DECLINE, sVALUE_NEW, loginUID);
                                                                if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                    String urlCallback = "";
                                                                    BRANCH[][] rsBranch = new BRANCH[1][];
                                                                    db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                    if(rsBranch != null && rsBranch[0].length > 0){
                                                                        urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                    }
                                                                    ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(pCERTIFICATION_ID),
                                                                        urlCallback, Definitions.CONFIG_OPERATION_TYPE_DECLINED, idDESC_DECLINE,"");
                                                                    Thread t = new Thread(thhreadLog);
                                                                    t.start();
                                                                }
                                                                
                                                                String sOLD_TOKEN_ID = "";
                                                                CERTIFICATION[][] rsGetToken = new CERTIFICATION[1][];
                                                                db.S_BO_CERTIFICATION_DETAIL(sPAST_CERTIFICATE_ID, sessLanguage, rsGetToken);
                                                                if (rsGetToken[0].length > 0) {
                                                                    sOLD_TOKEN_ID = String.valueOf(rsGetToken[0][0].TOKEN_ID);
                                                                }
                                                                boolean IsDecline_PERMANENT_INITIALZED = false;
                                                                String sToken_AtrrID = "";
                                                                TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                                db.S_BO_TOKEN_GET_ATTR(sOLD_TOKEN_ID, sessLanguage, rsToken_OLD);
                                                                if (rsToken_OLD[0].length > 0) {
                                                                    for (TOKEN rsToken_OLD1 : rsToken_OLD[0]) {
                                                                        if (rsToken_OLD1.TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PERMANENT_INITIALZED) {
                                                                            sToken_AtrrID = String.valueOf(rsToken_OLD1.TOKEN_ATTR_ID);
                                                                            IsDecline_PERMANENT_INITIALZED = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                                if (IsDecline_PERMANENT_INITIALZED == true) {
                                                                    db.S_BO_TOKEN_ATTR_DECLINED(Integer.parseInt(sToken_AtrrID), loginUID);
                                                                    TOKEN[][] rsTokenDetail = new TOKEN[1][];
                                                                    db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsTokenDetail);
                                                                    if(rsTokenDetail[0].length > 0){
                                                                        if(rsTokenDetail[0][0].TOKEN_STATE_ID == Definitions.CONFIG_TOKEN_STATE_ID_LOST)
                                                                        {
                                                                            db.S_BO_TOKEN_RECOVERY_STATE(sOLD_TOKEN_ID, loginUID);
                                                                        }
                                                                    }
                                                                }
                                                                request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                strView = "0#0#" + strIsPushNotiDecline;
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                            }
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_HAS_STATUS + "#0";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_HAS_STATUS + "#0";
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
                                                    RSSPProcessCommon clsRSSP = new RSSPProcessCommon();
                                                    String[] sResultRSSP = new String[2];
                                                    String sAgreementUUID = "";
                                                    String sRsspRelyingParty = "";
                                                    if (!"".equals(sVALUE_OLD)) {
                                                        objectMapper = new ObjectMapper();
                                                        ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                        sAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                        sRsspRelyingParty = valueATTR_Frist.getRsspRelyingParty();
                                                    }
                                                    if(!"".equals(sAgreementUUID)) {
                                                        String sType = "";
                                                        if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION){
                                                            sType = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_PREPARE_CERT;
                                                        } else if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL){
                                                            sType = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_RENEW_CERT;
                                                        } else if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO){
                                                            sType = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_CHANGE_CERT;
                                                        } else if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE){
                                                            sType = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_REVOKE_CERT;
                                                        }
                                                        if(intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                            && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                                                            && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                                            pCERTIFICATION_SN = "";
                                                        }
                                                        if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                            || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                                            rsReq = new CERTIFICATION[1][];
                                                            db.S_BO_CERTIFICATION_DETAIL(sPAST_CERTIFICATE_ID, sessLanguage, rsReq);
                                                            if (rsReq[0].length > 0) {
                                                                pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                                            }
                                                        }
                                                        clsRSSP.declineCertificateForSignCloud(sAgreementUUID, String.valueOf(pCERTIFICATION_ID),
                                                            idDESC_DECLINE, sRsspRelyingParty, sResultRSSP, pCERTIFICATION_SN, sType, credentialAuthen);
                                                        if("0".equals(sResultRSSP[0])) {
                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                            strView = "0#0";
                                                        } else {
                                                            CommonFunction.LogErrorServlet(log, "RSSP Request Decline - Error: " + sResultRSSP[1]);
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#" + sResultRSSP[1];
                                                        }
                                                    } else {
                                                        CommonFunction.LogErrorServlet(log, "RSSP Request Decline - Error: There does not exist the value of Agreement UUID connecting to eSignCloud");
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#There does not exist the value of Agreement UUID connecting to eSignCloud.";
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM + "#0";
                                                }
                                                //</editor-fold>
                                            }
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
                            case "declinecertmulti": {
                                //<editor-fold defaultstate="collapsed" desc="declinecertmulti">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    //String idATTR = EscapeUtils.CheckTextNull(request.getParameter("idATTR"));
                                    String idDESC_DECLINE = EscapeUtils.checkTextNullAndXSS(request.getParameter("DESC_DECLINE"));
                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    // check agency
                                    //String sAGENT_ID;
                                    String sPAST_CERTIFICATE_ID = "";
                                    String pCERTIFICATION_AUTHORITY_ID = "";
                                    String pCERTIFICATION_SN = "";
                                    String strIsPushNotiDecline = "0";
                                    String pTOKEN_SN = "";
                                    String sVALUE_OLD = "";
                                    String sAGENT_ID = "0";
                                    int sStatusRequest = 0;
                                    int pCERTIFICATION_ID = 0;
                                    int sStatusCert = 0;
                                    int intCERTIFICATION_ATTR_TYPE_ID=0;
                                    int pPKI_FORMFACTOR_ID=0;
                                    boolean isAccessAgency = true;
                                    
                                    String isCheckAll = request.getParameter("isCheckAll");
                                    List<CERTIFICATION> listCertTemp = new ArrayList<>();
                                    if (null != isCheckAll) {
                                        switch (isCheckAll) {
                                            case "0":
                                                String idValue = EscapeUtils.CheckTextNull(request.getParameter("idValue"));
                                                idValue = idValue.substring(0, idValue.lastIndexOf(",")).trim();
                                                String[] sPlitValue = idValue.replace(Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX, "").split(",");
                                                if (sPlitValue.length > 0) {
                                                    for (String sPlitValue1 : sPlitValue) {
                                                        if(!"".equals(sPlitValue1) && !"EMPTY".equals(sPlitValue1)) {
                                                            CERTIFICATION itemCertTemp = new CERTIFICATION();
                                                            itemCertTemp.ID = Integer.parseInt(sPlitValue1.trim());
                                                            listCertTemp.add(itemCertTemp);
                                                        }
                                                    }
                                                    sessionsa.setAttribute("SessRefreshNEACLog", "1");
                                                    strView = "0#0";
                                                } else {
                                                    strView = "1#0";
                                                }
                                                break;
                                            case "1":
                                                String isUIDCollection = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_UID_COLLECTION_DISPLAY_ENABLED);
                                                String ToCreateDate = (String) sessionsa.getAttribute("sessToCreateDateApproveReq");
                                                String FromCreateDate = (String) sessionsa.getAttribute("sessFromCreateDateApproveReq");
                                                String TOKEN_ID = (String) sessionsa.getAttribute("sessTOKEN_IDApproveReq");
                                                String CERTIFICATION_SN = (String) sessionsa.getAttribute("sessCERTIFICATION_SNApproveReq");
                                                String BranchOffice = (String) sessionsa.getAttribute("sessBranchOfficeApproveReq");

                                                String PERSONAL_NAME = (String) sessionsa.getAttribute("sessPERSONAL_NAMEApproveReq");
                                                String COMPANY_NAME = (String) sessionsa.getAttribute("sessCOMPANY_NAMEApproveReq");
                                                String FORM_FACTOR = (String) sessionsa.getAttribute("sessFormFactorApproveReq");
                                                String DOMAIN_NAME = (String) sessionsa.getAttribute("sessDOMAIN_NAMEApproveReq");

                                                String TAX_CODE = (String) sessionsa.getAttribute("sessTAX_CODEApproveReq");
                                                String BUDGET_CODE = (String) sessionsa.getAttribute("sessBUDGET_CODEApproveReq");
                                                String DECISION = (String) sessionsa.getAttribute("sessDECISIONApproveReq");
                                                String P_ID = (String) sessionsa.getAttribute("sessP_IDApproveReq");
                                                String CCCD = (String) sessionsa.getAttribute("sessCCCDApproveReq");
                                                String PASSPORT = (String) sessionsa.getAttribute("sessPASSPORTApproveReq");
                                                String CERTIFICATION_STATE = (String) sessionsa.getAttribute("sessCERTIFICATION_STATEApproveReq");
                                                String CERTIFICATION_ATTR_TYPE = (String) sessionsa.getAttribute("sessCERTIFICATION_ATTR_TYPEApproveReq");
                                                String CERTIFICATION_PURPOSE = (String) sessionsa.getAttribute("sessCERTIFICATION_PURPOSEApproveReq");
                                                String DEVICE_UUID_SEARCH = (String) sessionsa.getAttribute("sessDEVICE_UUIDApproveReq");
                                                String HSM_ACTIVE_ENABLED = (String) sessionsa.getAttribute("sessActiveEnabledApproveReq");

                                                String strAlertAllTimes = (String) sessionsa.getAttribute("AlertAllTimeSApproveReq");
                                                String sEnterpriseCert = "";
                                                String sPersonalCert = "";
                                                if(isUIDCollection.equals("1")) {
                                                    String sENTERPRISE_ID = (String) sessionsa.getAttribute("sessENTERPRISE_IDApproveReq");
                                                    String sPERSONAL_ID = (String) sessionsa.getAttribute("sessPERSONAL_IDApproveReq");
                                                    String sENTERPRISE_PREFIX = (String) sessionsa.getAttribute("sessENTERPRISE_PREFIXApproveReq");
                                                    String sPERSONAL_PREFIX = (String) sessionsa.getAttribute("sessPERSONAL_PREFIXApproveReq");
                                                    if(!"".equals(sENTERPRISE_ID)){
                                                        sEnterpriseCert = sENTERPRISE_PREFIX + sENTERPRISE_ID;
                                                    }
                                                    if(!"".equals(sPERSONAL_ID)){
                                                        sPersonalCert = sPERSONAL_PREFIX + sPERSONAL_ID;
                                                    }
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_STATE)) {
                                                    CERTIFICATION_STATE = "";
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_ATTR_TYPE)) {
                                                    CERTIFICATION_ATTR_TYPE = "";
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_PURPOSE)) {
                                                    CERTIFICATION_PURPOSE = "";
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(FORM_FACTOR)) {
                                                    FORM_FACTOR = "";
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                                    BranchOffice = "";
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(HSM_ACTIVE_ENABLED)) {
                                                    HSM_ACTIVE_ENABLED = "";
                                                }
                                                if(!FORM_FACTOR.equals(Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)) {
                                                    HSM_ACTIVE_ENABLED = "";
                                                }
                                                if("1".equals(strAlertAllTimes)) {
                                                    FromCreateDate = "";
                                                    ToCreateDate = "";
                                                }
                                                String pBRANCH_BY = EscapeUtils.escapeHtmlSearch(SessUserAgentID);
                                                Config conf = new Config();
                                                String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                                String sBranchTreeEnable = conf.GetPropertybyCode(Definitions.CONFIG_BRANCH_TREE_ENABLED);
                                                if("1".equals(sBranchTreeEnable)) {
                                                    pBRANCH_BY = "1";
                                                }
                                                if(!isUIDCollection.equals("1")) {
                                                    String[] sUIDResult = new String[2];
                                                    CommonReferServlet.collectFieldToUID(EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                                        EscapeUtils.escapeHtmlSearch(DECISION), EscapeUtils.escapeHtmlSearch(P_ID), EscapeUtils.escapeHtmlSearch(PASSPORT),
                                                        EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                                    sEnterpriseCert = sUIDResult[0];
                                                    sPersonalCert = sUIDResult[1];
                                                }
                                                int ss = db.S_BO_CERTIFICATION_APPROVED_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(CERTIFICATION_STATE), EscapeUtils.escapeHtmlSearch(TOKEN_ID),
                                                    EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE),
                                                    EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                    EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                    pBRANCH_BY, EscapeUtils.escapeHtmlSearch(FORM_FACTOR),
                                                    EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH), EscapeUtils.escapeHtmlSearch(CERTIFICATION_SN),
                                                    "", sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert, HSM_ACTIVE_ENABLED);
                                                if (ss > 0) {
                                                    CERTIFICATION[][] rsCertTemp = new CERTIFICATION[1][];
                                                    db.S_BO_CERTIFICATION_APPROVED_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                        EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                        EscapeUtils.escapeHtmlSearch(CERTIFICATION_STATE), EscapeUtils.escapeHtmlSearch(TOKEN_ID),
                                                        EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE),
                                                        EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                                        EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                                        pBRANCH_BY, EscapeUtils.escapeHtmlSearch(FORM_FACTOR),
                                                        EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH), EscapeUtils.escapeHtmlSearch(CERTIFICATION_SN),
                                                        sessLanguage, rsCertTemp, Definitions.CONFIG_PAGE_SIZE_IPAGNO, ss,
                                                        "", sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert, HSM_ACTIVE_ENABLED);
                                                    if(rsCertTemp[0].length > 0) {
                                                        String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                        String SessRoleID = sessionsa.getAttribute("RoleID_ID").toString().trim();
                                                        for (CERTIFICATION temp1 : rsCertTemp[0]) {
                                                            boolean isAllowDecline = false;
                                                            if(temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                || temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                                            {
                                                                boolean isDeclineAgent = false;
                                                                if(AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                    isDeclineAgent = true;
                                                                } else {
                                                                    if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                                        if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                            isDeclineAgent = true;
                                                                        } else {
                                                                            int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(temp1.CERTIFICATION_ATTR_ID, Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                            if(intApprove == 1) {
                                                                                isDeclineAgent = true;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                                isDeclineAgent = true;
                                                                            }
                                                                        } else {
                                                                            int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(temp1.CERTIFICATION_ATTR_ID, Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                            if(intApprove == 1) {
                                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                                    isDeclineAgent = true;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if(isDeclineAgent == true) {
                                                                    isAllowDecline = true;
                                                                }
                                                            } else if(temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED
                                                                && temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                                                                && temp1.CERTIFICATION_ATTR_STATE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED) {
                                                                if(AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                    if(temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                                                        && temp1.CERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                                                        && temp1.CERTIFICATION_STATE_ID == Definitions.CONFIG_CERTIFICATION_STATE_REVOKED)
                                                                    { } else {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            isAllowDecline = true;
                                                                        }
                                                                    }
                                                                }
                                                            } else if(temp1.CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED) {
                                                                if(AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                    {
                                                                        isAllowDecline = true;
                                                                    }
                                                                }
                                                            }
                                                            if(isAllowDecline == true) {
                                                                CERTIFICATION itemCertTemp = new CERTIFICATION();
                                                                itemCertTemp.ID = temp1.CERTIFICATION_ATTR_ID;
                                                                listCertTemp.add(itemCertTemp);
                                                                
                                                            }
                                                        }
                                                    }
                                                }
                                                break;
                                        }
                                    }
                                    if(listCertTemp != null && listCertTemp.size() > 0) {
                                        for (CERTIFICATION listCertTemp1 : listCertTemp) {
                                            CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                            db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(listCertTemp1.ID), sessLanguage, rsReq);
                                            if (rsReq[0].length > 0) {
                                                pCERTIFICATION_ID = rsReq[0][0].ID;
                                                pTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                                pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                                //sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                                sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                                pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                                sPAST_CERTIFICATE_ID = String.valueOf(rsReq[0][0].PAST_CERTIFICATION_ID);
                                                intCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                                pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                                sStatusRequest = rsReq[0][0].CERTIFICATION_ATTR_STATE_ID;
                                                sStatusCert = rsReq[0][0].CERTIFICATION_STATE_ID;
                                                sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                                if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                    BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                    isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                                }
                                            } else {
                                                isAccessAgency = false;
                                            }
                                            //</editor-fold>

                                            if (isAccessAgency == true) {
                                                //<editor-fold defaultstate="collapsed" desc="### CHECK AGENCY DECLINE">
                                                String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                                String SessRoleID = sessionsa.getAttribute("RoleID_ID").toString().trim();
                                                if (sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                        || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT) {
                                                    if(AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                        isAccessAgency = true;
                                                    } else {
                                                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                isAccessAgency = true;
                                                            } else {
                                                                int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(listCertTemp1.ID, Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                if(intApprove == 1) {
                                                                    isAccessAgency = true;
                                                                }
                                                            }
                                                        } else {
                                                            if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                    isAccessAgency = true;
                                                                }
                                                            } else {
                                                                int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(listCertTemp1.ID, Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                if(intApprove == 1) {
                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                        isAccessAgency = true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                //</editor-fold>
                                            }

                                            if (isAccessAgency == true) {
                                                //<editor-fold defaultstate="collapsed" desc="### Process">
                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                {
                                                    CommonFunction.LogDebugString(log, "DECLINE_CERTIFICATION_ATTR", "USER_LOGIN: " + loginUID
                                                            + "; STATUS_CERTIFICATION_ATTR: " + sStatusRequest + "; CERTIFICATION_ATTR_TYPE_ID: " + intCERTIFICATION_ATTR_TYPE_ID
                                                            + "; AGENT_ID_LOG: " + AGENT_ID_LOG);
                                                    Config conf = new Config();
                                                    boolean booRSSP_ACCESS_ENABLED = false;
                                                    if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                        String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                                        if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                            booRSSP_ACCESS_ENABLED = true;
                                                            if (!"".equals(sVALUE_OLD)) {
                                                                objectMapper = new ObjectMapper();
                                                                ATTRIBUTE_VALUES valueRSSP = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                if(valueRSSP.getTokenSn().trim().equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN)) {
                                                                    booRSSP_ACCESS_ENABLED = false;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if(booRSSP_ACCESS_ENABLED == false) {
                                                        String sVALUE_NEW = "";
                                                        String sVALUE_SCAN = "";
                                                        if(!"".equals(sVALUE_OLD))
                                                        {
                                                            objectMapper = new ObjectMapper();
                                                            ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                            valueATTR_Frist.setCerttificateDeclineReason(idDESC_DECLINE);
                                                            sVALUE_NEW = CommonFunction.GenJSONTokenATTR(valueATTR_Frist);
                                                            valueATTR_Frist.setCertificationAttrId(String.valueOf(listCertTemp1.ID));
                                                            sVALUE_SCAN = CommonFunction.GenJSONTokenATTR(valueATTR_Frist);
                                                        }
                                                        if(intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                                                                && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                        {
                                                            objectMapper = new ObjectMapper();
                                                            CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                            jsonCertComment.certificateDeclineReason = idDESC_DECLINE;
                                                            jsonCertComment.certificateRevokeReason = "";
                                                            jsonCertComment.certificateSuspendReason = "";
                                                            idDESC_DECLINE = objectMapper.writeValueAsString(jsonCertComment);
                                                        } else {
                                                            idDESC_DECLINE = "";
                                                        }
                                                        if (sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT)
                                                        {
                                                            db.S_BO_CERTIFICATION_REMOVED(listCertTemp1.ID, idDESC_DECLINE, sVALUE_NEW, loginUID);
                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                            strView = "0#0#" + strIsPushNotiDecline;
                                                            if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                String urlCallback = "";
                                                                BRANCH[][] rsBranch = new BRANCH[1][];
                                                                db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                if(rsBranch != null && rsBranch[0].length > 0){
                                                                    urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                }
                                                                ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(pCERTIFICATION_ID),
                                                                    urlCallback, Definitions.CONFIG_OPERATION_TYPE_DECLINED, idDESC_DECLINE,"");
                                                                Thread t = new Thread(thhreadLog);
                                                                t.start();
                                                            }
                                                        } else if (sStatusRequest != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_COMMITED
                                                                && sStatusRequest != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_DECLINED
                                                                && sStatusRequest != Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                                                        {
                                                            if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                if(sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED
                                                                        && (intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE
                                                                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                        || intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                                        && (sStatusCert == Definitions.CONFIG_CERTIFICATION_STATE_REVOKED
                                                                        || sStatusCert == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_PERMANENT_DISABLE
                                                                        || sStatusCert == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED_TEMPORARY_DISABLE
                                                                        || sStatusCert == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_PERMANENT_DISABLE
                                                                        || sStatusCert == Definitions.CONFIG_CERTIFICATION_STATE_RENEWAL_TEMPORARY_DISABLE))
                                                                { }
                                                                else {
                                                                    strIsPushNotiDecline = "1";
                                                                    db.S_BO_CERTIFICATION_REMOVED(listCertTemp1.ID, idDESC_DECLINE, sVALUE_NEW, loginUID);
                                                                    if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                        String urlCallback = "";
                                                                        BRANCH[][] rsBranch = new BRANCH[1][];
                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                        if(rsBranch != null && rsBranch[0].length > 0){
                                                                            urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                        }
                                                                        ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(pCERTIFICATION_ID),
                                                                            urlCallback, Definitions.CONFIG_OPERATION_TYPE_DECLINED, idDESC_DECLINE,"");
                                                                        Thread t = new Thread(thhreadLog);
                                                                        t.start();
                                                                    }
                                                                    // check ton tai lenh PERMANENT_INITIALZED ben token attr thi decline
                                                                    String sOLD_TOKEN_ID = "";
                                                                    CERTIFICATION[][] rsGetToken = new CERTIFICATION[1][];
                                                                    db.S_BO_CERTIFICATION_DETAIL(sPAST_CERTIFICATE_ID, sessLanguage, rsGetToken);
                                                                    if (rsGetToken[0].length > 0) {
                                                                        sOLD_TOKEN_ID = String.valueOf(rsGetToken[0][0].TOKEN_ID);
                                                                    }
                                                                    boolean IsDecline_PERMANENT_INITIALZED = false;
                                                                    String sToken_AtrrID = "";
                                                                    TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                                    db.S_BO_TOKEN_GET_ATTR(sOLD_TOKEN_ID, sessLanguage, rsToken_OLD);
                                                                    if (rsToken_OLD[0].length > 0) {
                                                                        for (TOKEN rsToken_OLD1 : rsToken_OLD[0]) {
                                                                            if (rsToken_OLD1.TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PERMANENT_INITIALZED) {
                                                                                sToken_AtrrID = String.valueOf(rsToken_OLD1.TOKEN_ATTR_ID);
                                                                                IsDecline_PERMANENT_INITIALZED = true;
                                                                                break;
                                                                            }
                                                                        }
                                                                    }
                                                                    if (IsDecline_PERMANENT_INITIALZED == true) {
                                                                        db.S_BO_TOKEN_ATTR_DECLINED(Integer.parseInt(sToken_AtrrID), loginUID);
                                                                        TOKEN[][] rsTokenDetail = new TOKEN[1][];
                                                                        db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsTokenDetail);
                                                                        if(rsTokenDetail[0].length > 0){
                                                                            if(rsTokenDetail[0][0].TOKEN_STATE_ID == Definitions.CONFIG_TOKEN_STATE_ID_LOST)
                                                                            {
                                                                                db.S_BO_TOKEN_RECOVERY_STATE(sOLD_TOKEN_ID, loginUID);
                                                                            }
                                                                        }
                                                                    }
                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                    strView = "0#0#" + strIsPushNotiDecline;
                                                                }
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_HAS_STATUS + "#0";
                                                            }
                                                        } else if(sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_ISSUED)
                                                        {
                                                            if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                int[] intRes = new int[1];
                                                                String[] sRes = new String[1];
                                                                String raConnectorScan = conf.GetPropertybyCode(Definitions.CONFIG_RACONNECTOR_SCAN_ENABLED);
                                                                if("1".equals(raConnectorScan))
                                                                {
                                                                    strIsPushNotiDecline = "1";
                                                                    int[] pCERTIFICATE_ATTR_ID_REFUND = new int[1];
                                                                    String sParam = db.S_BO_CERTIFICATION_REFUND(String.valueOf(pCERTIFICATION_ID), sVALUE_SCAN,
                                                                            loginUID, pCERTIFICATE_ATTR_ID_REFUND);
                                                                    if ("0".equals(sParam)) {
                                                                        String pCA_NAME = "";
                                                                        CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                        if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                            pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                        }
                                                                        ConnectConnector.RevokeCertificate(pTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID,
                                                                                pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID_REFUND[0]);
                                                                        if (intRes[0] == 0) {
                                                                            if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                                String urlCallback = "";
                                                                                BRANCH[][] rsBranch = new BRANCH[1][];
                                                                                db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                                if(rsBranch != null && rsBranch[0].length > 0){
                                                                                    urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                                }
                                                                                ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(pCERTIFICATION_ID),
                                                                                    urlCallback, Definitions.CONFIG_OPERATION_TYPE_DECLINED, idDESC_DECLINE,"");
                                                                                Thread t = new Thread(thhreadLog);
                                                                                t.start();
                                                                            }
                                                                            String sOLD_TOKEN_ID = "";
                                                                            CERTIFICATION[][] rsGetToken = new CERTIFICATION[1][];
                                                                            db.S_BO_CERTIFICATION_DETAIL(sPAST_CERTIFICATE_ID, sessLanguage, rsGetToken);
                                                                            if (rsGetToken[0].length > 0) {
                                                                                sOLD_TOKEN_ID = String.valueOf(rsGetToken[0][0].TOKEN_ID);
                                                                            }
                                                                            boolean IsDecline_PERMANENT_INITIALZED = false;
                                                                            String sToken_AtrrID = "";
                                                                            TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                                            db.S_BO_TOKEN_GET_ATTR(sOLD_TOKEN_ID, sessLanguage, rsToken_OLD);
                                                                            if (rsToken_OLD[0].length > 0) {
                                                                                for (TOKEN rsToken_OLD1 : rsToken_OLD[0]) {
                                                                                    if (rsToken_OLD1.TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PERMANENT_INITIALZED) {
                                                                                        sToken_AtrrID = String.valueOf(rsToken_OLD1.TOKEN_ATTR_ID);
                                                                                        IsDecline_PERMANENT_INITIALZED = true;
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (IsDecline_PERMANENT_INITIALZED == true) {
                                                                                db.S_BO_TOKEN_ATTR_DECLINED(Integer.parseInt(sToken_AtrrID), loginUID);
                                                                                TOKEN[][] rsTokenDetail = new TOKEN[1][];
                                                                                db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsTokenDetail);
                                                                                if(rsTokenDetail[0].length > 0){
                                                                                    if(rsTokenDetail[0][0].TOKEN_STATE_ID == Definitions.CONFIG_TOKEN_STATE_ID_LOST)
                                                                                    {
                                                                                        db.S_BO_TOKEN_RECOVERY_STATE(sOLD_TOKEN_ID, loginUID);
                                                                                    }
                                                                                }
                                                                            }
                                                                            request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                            strView = "0#0#" + strIsPushNotiDecline;
                                                                        } else {
                                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                        }
                                                                    } else {
                                                                        CommonFunction.LogErrorServlet(log, "S_BO_CERTIFICATION_REFUND: RESPONSE_CODE- " + sParam);
                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                    }
                                                                } else {
                                                                    strIsPushNotiDecline = "1";
                                                                    String pCA_NAME = "";
                                                                    CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                    db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                    if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                        pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                    }
                                                                    ConnectConnector.RevokeCertificate(pTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID,
                                                                            pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, listCertTemp1.ID);
                                                                    if (intRes[0] == 0) {
                                                                        db.S_BO_CERTIFICATION_DESTROYED(String.valueOf(listCertTemp1.ID), idDESC_DECLINE, sVALUE_NEW, loginUID);
                                                                        if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                            String urlCallback = "";
                                                                            BRANCH[][] rsBranch = new BRANCH[1][];
                                                                            db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                            if(rsBranch != null && rsBranch[0].length > 0){
                                                                                urlCallback = rsBranch[0][0].CALLBACK_URL_APPROVED;
                                                                            }
                                                                            ThreadCallbackApproved thhreadLog = new ThreadCallbackApproved(String.valueOf(pCERTIFICATION_ID),
                                                                                urlCallback, Definitions.CONFIG_OPERATION_TYPE_DECLINED, idDESC_DECLINE,"");
                                                                            Thread t = new Thread(thhreadLog);
                                                                            t.start();
                                                                        }
                                                                        String sOLD_TOKEN_ID = "";
                                                                        CERTIFICATION[][] rsGetToken = new CERTIFICATION[1][];
                                                                        db.S_BO_CERTIFICATION_DETAIL(sPAST_CERTIFICATE_ID, sessLanguage, rsGetToken);
                                                                        if (rsGetToken[0].length > 0) {
                                                                            sOLD_TOKEN_ID = String.valueOf(rsGetToken[0][0].TOKEN_ID);
                                                                        }
                                                                        boolean IsDecline_PERMANENT_INITIALZED = false;
                                                                        String sToken_AtrrID = "";
                                                                        TOKEN[][] rsToken_OLD = new TOKEN[1][];
                                                                        db.S_BO_TOKEN_GET_ATTR(sOLD_TOKEN_ID, sessLanguage, rsToken_OLD);
                                                                        if (rsToken_OLD[0].length > 0) {
                                                                            for (TOKEN rsToken_OLD1 : rsToken_OLD[0]) {
                                                                                if (rsToken_OLD1.TOKEN_ATTR_TYPE_ID == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PERMANENT_INITIALZED) {
                                                                                    sToken_AtrrID = String.valueOf(rsToken_OLD1.TOKEN_ATTR_ID);
                                                                                    IsDecline_PERMANENT_INITIALZED = true;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        if (IsDecline_PERMANENT_INITIALZED == true) {
                                                                            db.S_BO_TOKEN_ATTR_DECLINED(Integer.parseInt(sToken_AtrrID), loginUID);
                                                                            TOKEN[][] rsTokenDetail = new TOKEN[1][];
                                                                            db.S_BO_TOKEN_DETAIL(sOLD_TOKEN_ID, rsTokenDetail);
                                                                            if(rsTokenDetail[0].length > 0){
                                                                                if(rsTokenDetail[0][0].TOKEN_STATE_ID == Definitions.CONFIG_TOKEN_STATE_ID_LOST)
                                                                                {
                                                                                    db.S_BO_TOKEN_RECOVERY_STATE(sOLD_TOKEN_ID, loginUID);
                                                                                }
                                                                            }
                                                                        }
                                                                        request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                        strView = "0#0#" + strIsPushNotiDecline;
                                                                    } else {
                                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                    }
                                                                }
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_HAS_STATUS + "#0";
                                                            }
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_HAS_STATUS + "#0";
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
                                                            RSSPProcessCommon clsRSSP = new RSSPProcessCommon();
                                                            String[] sResultRSSP = new String[2];
                                                            String sAgreementUUID = "";
                                                            String sRsspRelyingParty = "";
                                                            if (!"".equals(sVALUE_OLD)) {
                                                                objectMapper = new ObjectMapper();
                                                                ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                sAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                                sRsspRelyingParty = valueATTR_Frist.getRsspRelyingParty();
                                                            }
                                                            if(!"".equals(sAgreementUUID)) {
                                                                String sType = "";
                                                                if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION){
                                                                    sType = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_PREPARE_CERT;
                                                                } else if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL){
                                                                    sType = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_RENEW_CERT;
                                                                } else if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO){
                                                                    sType = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_CHANGE_CERT;
                                                                } else if(intCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE){
                                                                    sType = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_REVOKE_CERT;
                                                                }
                                                                if(intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE
                                                                        && intCERTIFICATION_ATTR_TYPE_ID != Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                                {
                                                                    pCERTIFICATION_SN = "";
                                                                }
                                                                clsRSSP.declineCertificateForSignCloud(sAgreementUUID, String.valueOf(pCERTIFICATION_ID),
                                                                        idDESC_DECLINE, sRsspRelyingParty, sResultRSSP, pCERTIFICATION_SN, sType, credentialAuthen);
                                                                if("0".equals(sResultRSSP[0])) {
                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                    strView = "0#0";
                                                                } else {
                                                                    CommonFunction.LogErrorServlet(log, "RSSP Request Decline - Error: " + sResultRSSP[1]);
                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#" + sResultRSSP[1];
                                                                }
                                                            } else {
                                                                CommonFunction.LogErrorServlet(log, "RSSP Request Decline - Error: There does not exist the value of Agreement UUID connecting to eSignCloud");
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#There does not exist the value of Agreement UUID connecting to eSignCloud.";
                                                            }
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM + "#0";
                                                        }
                                                        //</editor-fold>
                                                    }
                                                }
                                                //</editor-fold>
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                            }
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "recoverycert": {
                                //<editor-fold defaultstate="collapsed" desc="recoverycert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    if(!"".equals(sID)) {
                                        //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                        boolean isAccessAgency = true;
                                        String strIsPushNotiApprove = "0";
                                        String PHONE_CONTRACT = "";
                                        String EMAIL_CONTRACT = "";
                                        String pCERTIFICATION_SN = "";
                                        String pPERSONAL_ID = "";
                                        String pENTERPRISE_ID = "";
                                        String pSUBJECT = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                        String sOLD_TOKEN_ID = "";
                                        String pISSUER_SUBJECT = "";
                                        String pPROVINCE_ID = "";
                                        String pCERTIFICATION_AUTHORITY_ID = "";
                                        String sTOKEN_SN = "";
                                        int pCERTIFICATION_ID =0;
                                        CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(sID, sessLanguage, rsReq);
                                        if (rsReq[0].length > 0) {
                                            pCERTIFICATION_ID = rsReq[0][0].ID;
                                            sOLD_TOKEN_ID = String.valueOf(rsReq[0][0].TOKEN_ID);
                                            sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                            PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                            EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                            pENTERPRISE_ID = rsReq[0][0].ENTERPRISE_ID;
                                            pPERSONAL_ID = rsReq[0][0].PERSONAL_ID;
                                            pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                            pISSUER_SUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                            pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                            pPROVINCE_ID = String.valueOf(rsReq[0][0].CITY_PROVINCE_ID);
                                            if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                            }
                                        } else {
                                            isAccessAgency = false;
                                        }
                                        //</editor-fold>

                                        if (isAccessAgency == true) {
                                            int sCERTIFICATION_ATTR_TYPE_ID = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED;
                                            String sCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RECOVERED;
                                            String pFUNCTIONALITY_NAME = Definitions.CONFIG_LOG_FUNCTIONALITY_RECOVERED;
                                            String pPAST_CERTIFICATE_ID = sID;
                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                            tempLogReq = new CERTIFICATION_DATA_ATTR();
                                            objectMapper = new ObjectMapper();
                                            tempLogReq.personalID = pPERSONAL_ID;
                                            tempLogReq.enterpriseID = pENTERPRISE_ID;
                                            tempLogReq.emailContract = EMAIL_CONTRACT;
                                            tempLogReq.phoneContract = PHONE_CONTRACT;
                                            tempLogReq.issuerSubject = pISSUER_SUBJECT;
                                            tempLogReq.subjectDn = pSUBJECT;
                                            tempLogReq.tokenSn = sTOKEN_SN;
                                            tempLogReq.typeName = sCERT_ATTR_TYPE_CODE;
                                            String strReq = objectMapper.writeValueAsString(tempLogReq);
                                            db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                    Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                    pFUNCTIONALITY_NAME, strReq, loginUID, System_Log_ID, sIP_Request, sysLog_BillCode);
                                            CommonFunction.LogDebugString(log, "RegistrationCertificate", "SuspendedCert: " + "SUBJECT: " + pSUBJECT
                                                    + "; pPERSONAL_ID: " + pPERSONAL_ID
                                                    + "; pENTERPRISE_ID: " + pENTERPRISE_ID + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                    + "; pCERTIFICATION_ATTR_TYPE_CODE: " + sCERT_ATTR_TYPE_CODE + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                    + "; ISSUER_SUBJECT: " + pISSUER_SUBJECT + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                    + "; TOKEN_ID_NEW: " + sOLD_TOKEN_ID + "; TOKEN_SN_NEW: " + sTOKEN_SN + "; CITY_PROVINCE_ID: " + pPROVINCE_ID);
                                            ATTRIBUTE_VALUES valueATTR;
                                            ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                            dataATTR.setCertificationData(tempLogReq);
                                            valueATTR = new ATTRIBUTE_VALUES();
                                            valueATTR.setTokenSn(sTOKEN_SN);
                                            valueATTR.setCerttificateSuspendReason("");
                                            valueATTR.setCerttificateRevokeReason("");
                                            valueATTR.setCerttificateDeclineReason("");
                                            valueATTR.setSuspendedTime(null);
                                            valueATTR.setTypeName(sCERT_ATTR_TYPE_CODE);
                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                            valueATTR.setCreateUser(loginFullname + " (" + loginUID + ")");
                                            valueATTR.setCreateDt(new Date());
                                            valueATTR.setAttributeData(dataATTR);
                                            //</editor-fold>

                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                            String sParam = db.S_BO_CERTIFICATION_ATTR_INSERT(sID, String.valueOf(sCERTIFICATION_ATTR_TYPE_ID),
                                                strReqValueATTR, loginUID, pCERTIFICATE_ATTR_ID);
                                            if ("0".equals(sParam)) {
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
                                                            ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_REMOVEFROMCRL_ID,
                                                                pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID[0]);
                                                            if (intRes[0] == 0) {
                                                                db.S_BO_CERTIFICATION_RECOVERED(String.valueOf(pCERTIFICATE_ATTR_ID[0]), loginUID);
                                                                sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                strView = "0#0#" + strIsPushNotiApprove;
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#0";
                                                            }
                                                        } else {
                                                            CommonFunction.LogDebugString(log, "ERROR S_BO_CERTIFICATION_APPROVED Response", sApprove);
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
                                                                //### sCERT_REVOCATION_REASON
                                                                ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_REMOVEFROMCRL_ID,
                                                                    pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID[0]);
                                                                if (intRes[0] == 0) {
                                                                    db.S_BO_CERTIFICATION_RECOVERED(String.valueOf(pCERTIFICATE_ATTR_ID[0]), loginUID);
                                                                    sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                    strView = "0#0#" + strIsPushNotiApprove;
                                                                } else {
                                                                    strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#0";
                                                                }
                                                            } else {
                                                                CommonFunction.LogDebugString(log, "ERROR S_BO_CERTIFICATION_APPROVED Response", sApprove);
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
                                                    CommonFunction.LogDebugString(log, "PORTAL - " + sCERT_ATTR_TYPE_CODE, "UserLogin: " + loginUID + "; Level: " + SessLevelBranch
                                                        + "; RoleID: " + sessionsa.getAttribute("RoleID_ID").toString().trim()
                                                        + "; FunctionCert: " + objectMapper.writeValueAsString(sessFunctionCert));
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
                                                            String userLoginID = request.getSession(false).getAttribute("UserID").toString().trim();
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
                                                            String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                                            String userLoginID = request.getSession(false).getAttribute("UserID").toString().trim();
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
                                                        CommonFunction.LogDebugString(log, "PORTAL - " + sCERT_ATTR_TYPE_CODE, "UserLogin: " + loginUID
                                                            + "; PolicyCert: " + objectMapper.writeValueAsString(sessPolicyCert_Data));
                                                        if(CommonFunction.checkApproveCAReqType(sCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true) {
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
                                                                ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_REMOVEFROMCRL_ID,
                                                                    pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID[0]);
                                                                if (intRes[0] == 0) {
                                                                    db.S_BO_CERTIFICATION_RECOVERED(String.valueOf(pCERTIFICATE_ATTR_ID[0]), loginUID);
                                                                    sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                    strView = "0#0#" + strIsPushNotiApprove;
                                                                } else {
                                                                    strView = Definitions.CONFIG_EXCEPTION_ERROR_CORECA_CALL + "#0";
                                                                }
                                                            } else {
                                                                CommonFunction.LogDebugString(log, "ERROR S_BO_CERTIFICATION_APPROVED Response", sApprove);
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                            }
                                                        } else {
                                                            strIsPushNotiApprove = "1";
                                                            sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                            strView = "0#0#" + strIsPushNotiApprove;
                                                        }
                                                        //</editor-fold>
                                                    } else {
                                                        strIsPushNotiApprove = "1";
                                                        sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                        strView = "0#0#" + strIsPushNotiApprove;
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
                                        CommonFunction.LogErrorServlet(log, "CertRecovery: Cert ID cannot be empty");
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "recreatelicense": {
                                //<editor-fold defaultstate="collapsed" desc="recreatelicense">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    // check agency
                                    boolean isAccessAgency = true;
                                    String sAGENT_ID;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
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
                                    if (isAccessAgency == true) {
                                        int[] intRes = new int[1];
                                        String[] sRes = new String[1];
                                        ConnectConnector.ReCreateDigitalCert(EscapeUtils.CheckTextNull(ID), intRes, sRes);
                                        if (intRes[0] == 0) {
                                            strView = "0#0";
                                        } else {
                                            strView = sRes[0] + "#0";
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
                            case "re-revokecert": {
                                //<editor-fold defaultstate="collapsed" desc="re-revokecert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String attrID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    boolean isAccessAgency = true;
                                    String sTOKEN_SN = "";
                                    String pCERTIFICATION_SN = "";
                                    String pCERTIFICATION_AUTHORITY_ID = "";
                                    String sAGENT_ID = "";
                                    String sVALUE = "";
                                    String certID = "";
                                    int pPKI_FORMFACTOR_ID= 0;
                                    int sCertTypeID= 0;
                                    int pCERTIFICATION_ID = 0;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(attrID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_ID = rsReq[0][0].ID;
                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        sVALUE = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                        pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sCertTypeID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        certID = String.valueOf(rsReq[0][0].ID);
                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
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
                                        objectMapper = new ObjectMapper();
                                        CommonFunction.LogDebugString(log, "RE-REVOKE-CERTIFICATION", "REQUEST: " + "pCERTIFICATION_SN: " + pCERTIFICATION_SN
                                            + "; sTOKEN_SN: " + sTOKEN_SN + "; certID: " + certID + "; attrID: " + attrID
                                            + "; pCERTIFICATION_AUTHORITY_ID: " + pCERTIFICATION_AUTHORITY_ID + "; sCertTypeID: " + sCertTypeID);
                                        if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            if (!"".equals(sVALUE) && sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                                String sCertRevokeDeleteInToken = "0";
                                                ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                                                String idDESC_DECLINE = EscapeUtils.CheckTextNull(valueATTR_Frist.getCerttificateRevokeReason());
                                                String sCerttificateRevokeEJBCAReason = EscapeUtils.CheckTextNull(valueATTR_Frist.getCerttificateRevokeEJBCAReason());
                                                if(valueATTR_Frist.getCertRevokeDeleteInTokenEnabled() == true)
                                                {
                                                    sCertRevokeDeleteInToken = "1";
                                                }
                                                if("".equals(sCerttificateRevokeEJBCAReason)) {
                                                    sCerttificateRevokeEJBCAReason = String.valueOf(Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID);
                                                }
                                                boolean isApproveEnabled = false;
                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                {
                                                    isApproveEnabled = true;
                                                } else {
                                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,
                                                        Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                        isApproveEnabled = true;
                                                    }
                                                }
                                                if(isApproveEnabled == true)
                                                {
                                                    Config conf = new Config();
                                                    boolean booRSSP_ACCESS_ENABLED = false;
                                                    if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                        String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                                        if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                            booRSSP_ACCESS_ENABLED = true;
                                                            if (!"".equals(sVALUE)) {
                                                                objectMapper = new ObjectMapper();
                                                                ATTRIBUTE_VALUES valueRSSP = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                                                                if(valueRSSP.getTokenSn().trim().equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN)) {
                                                                    booRSSP_ACCESS_ENABLED = false;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if(booRSSP_ACCESS_ENABLED == false) {
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
                                                        ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Integer.parseInt(sCerttificateRevokeEJBCAReason),
                                                            pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, Integer.parseInt(attrID));
                                                        if (intRes[0] == 0) {
                                                            db.S_BO_CERTIFICATION_REVOKED(Integer.parseInt(attrID), Integer.parseInt(sCertRevokeDeleteInToken), loginUID);
                                                            //<editor-fold defaultstate="collapsed" desc="### Update Reason Revoke">
                                                            CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                            jsonCertComment.certificateDeclineReason = "";
                                                            jsonCertComment.certificateSuspendReason = "";
                                                            jsonCertComment.certificateRevokeReason = idDESC_DECLINE;
                                                            idDESC_DECLINE = objectMapper.writeValueAsString(jsonCertComment);
                                                            db.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(certID, idDESC_DECLINE, loginUID);
                                                            //</editor-fold>

                                                            sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                            strView = "0#0";
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                        }
                                                    }  else {
                                                        //<editor-fold defaultstate="collapsed" desc="### RSSP Process">
                                                        PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                                        db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                                        String sFormFactorPro = "";
                                                        if(rsFormfactorPro[0].length > 0) {
                                                            sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                                        }
                                                        CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                                        if(credentialAuthen != null) {
                                                            RSSPProcessCommon clsRSSP = new RSSPProcessCommon();
                                                            String sAgreementUUID = "";
                                                            String sRelyingPartyRSSP = "";
                                                            boolean revokeSetOldStatus = false;
                                                            if (!"".equals(sVALUE)) {
                                                                objectMapper = new ObjectMapper();
                                                                sAgreementUUID = valueATTR_Frist.getRsspAgreementUUID();
                                                                sRelyingPartyRSSP = valueATTR_Frist.getRsspRelyingParty();
                                                                revokeSetOldStatus = valueATTR_Frist.getRevokeSetOldStatusEnabled();
                                                            }
    //                                                        String sReasonTransfer = idDESC_DECLINE;
    //                                                        if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
    //                                                            String idCERT_REVOCATION_REASON = EscapeUtils.CheckTextNull(request.getParameter("CERT_REVOCATION_REASON"));
    //                                                            if(!"".equals(idCERT_REVOCATION_REASON)) {
    //                                                                if(Integer.parseInt(idCERT_REVOCATION_REASON) != Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID) {
    //                                                                    CERTIFICATION_REVOCATION_REASON[][] rsReasonCA = new CERTIFICATION_REVOCATION_REASON[1][];
    //                                                                    db.S_BO_CERTIFICATION_REVOCATION_REASON_DETAIL(idCERT_REVOCATION_REASON, rsReasonCA);
    //                                                                    if(rsReasonCA[0].length > 0) {
    //                                                                        sReasonTransfer = rsReasonCA[0][0].NAME;
    //                                                                    }
    //                                                                }
    //                                                            }
    //                                                        }
                                                            if(!"".equals(sAgreementUUID) && !"".equals(sRelyingPartyRSSP))
                                                            {
                                                                String sRequestTypeRssp = "";
                                                                if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL){
                                                                    sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_RENEW_CERT;
                                                                } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO){
                                                                    sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_CHANGE_CERT;
                                                                } else if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE){
                                                                    sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_REVOKE_CERT;
                                                                } else {
                                                                    sRequestTypeRssp = Definitions.CONFIG_RSSP_CERTIFICATE_TYPE_PREPARE_CERT;
                                                                }
                                                                String[] sResultRSSP = new String[2];
                                                                clsRSSP.approveCertificateForSignCloud(sAgreementUUID, String.valueOf(pCERTIFICATION_ID), "",
                                                                    sRelyingPartyRSSP, sResultRSSP, pCERTIFICATION_SN,"", sRequestTypeRssp, "",
                                                                    credentialAuthen, revokeSetOldStatus);
                                                                if(!"0".equals(sResultRSSP[0])) {
                                                                    strView = "1#" + sResultRSSP[1];
                                                                } else {
                                                                    sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                                    strView = "0#0";
                                                                }
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR + "#There does not exist the value of Agreement UUID connecting to eSignCloud.";
                                                            }
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM + "#0";
                                                        }
                                                        //</editor-fold>
                                                    }
                                                } else {strView = "1#0";}
//                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
//                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
//                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
//                                                {
//                                                    
//                                                } else {
//                                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
//                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,
//                                                        Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
//                                                        // RACONNECTOR
//                                                        String pCA_NAME = "";
//                                                        CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
//                                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
//                                                        if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
//                                                            pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
//                                                        }
//                                                        int[] intRes = new int[1];
//                                                        String[] sRes = new String[1];
//                                                        // AFTER_EDIT
//                                                        ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Integer.parseInt(sCerttificateRevokeEJBCAReason),
//                                                            pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, Integer.parseInt(attrID));
//                                                        if (intRes[0] == 0) {
//                                                            db.S_BO_CERTIFICATION_REVOKED(Integer.parseInt(attrID), Integer.parseInt(sCertRevokeDeleteInToken), loginUID);
//                                                            //<editor-fold defaultstate="collapsed" desc="### Update Reason Revoke">
//                                                            CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
//                                                            jsonCertComment.certificateDeclineReason = "";
//                                                            jsonCertComment.certificateSuspendReason = "";
//                                                            jsonCertComment.certificateRevokeReason = idDESC_DECLINE;
//                                                            idDESC_DECLINE = objectMapper.writeValueAsString(jsonCertComment);
//                                                            db.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(certID, idDESC_DECLINE, loginUID);
//                                                            //</editor-fold>
//
//                                                            sessionsa.setAttribute("RefreshApproveReqSess", "1");
//                                                            strView = "0#0";
//                                                        } else {
//                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
//                                                        }
//                                                    } else {
//                                                        strView = "1#0";
//                                                    }
//                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_APPROVE_AGENCY + "#0";
                                            }
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
                            case "re-suspendcert": {
                                //<editor-fold defaultstate="collapsed" desc="re-suspendcert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String attrID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    boolean isAccessAgency = true;
                                    String sTOKEN_SN = "";
                                    String pCERTIFICATION_SN = "";
                                    String pCERTIFICATION_AUTHORITY_ID = "";
                                    String sAGENT_ID = "";
                                    String sVALUE = "";
                                    String certID = "";
                                    int sCertTypeID = 0;
                                    int pCERTIFICATION_ID = 0;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(attrID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_ID = rsReq[0][0].ID;
                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        sVALUE = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        certID = String.valueOf(rsReq[0][0].ID);
                                        sCertTypeID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
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
                                        objectMapper = new ObjectMapper();
                                        CommonFunction.LogDebugString(log, "RE-SUSPEND-CERTIFICATION", "REQUEST: " + "pCERTIFICATION_SN: " + pCERTIFICATION_SN
                                            + "; sTOKEN_SN: " + sTOKEN_SN + "; certID: " + certID + "; attrID: " + attrID
                                            + "; pCERTIFICATION_AUTHORITY_ID: " + pCERTIFICATION_AUTHORITY_ID + "; sCertTypeID: " + sCertTypeID);
                                        if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            if (!"".equals(sVALUE) && (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)) {
                                                java.sql.Timestamp sSUSPEND_TIME_DB = null;
                                                ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                                                String sSUSPEND_REASON = EscapeUtils.CheckTextNull(valueATTR_Frist.getCerttificateSuspendReason());
                                                if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE)
                                                {
                                                    sSUSPEND_TIME_DB = valueATTR_Frist.getSuspendedTime();
                                                }
                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
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
                                                        pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, Integer.parseInt(attrID));
                                                    if (intRes[0] == 0) {
                                                        CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                        jsonCertComment.certificateDeclineReason = "";
                                                        jsonCertComment.certificateRevokeReason = "";
                                                        jsonCertComment.certificateSuspendReason = sSUSPEND_REASON;
                                                        String pCOMMENT = objectMapper.writeValueAsString(jsonCertComment);
                                                        db.S_BO_CERTIFICATION_DISABLE(attrID, sSUSPEND_TIME_DB, pCOMMENT, loginUID);
                                                        sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                        strView = "0#0";
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                    }
                                                } else {
                                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,
                                                        Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
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
                                                            pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, Integer.parseInt(attrID));
                                                        if (intRes[0] == 0) {
                                                            CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                            jsonCertComment.certificateDeclineReason = "";
                                                            jsonCertComment.certificateRevokeReason = "";
                                                            jsonCertComment.certificateSuspendReason = sSUSPEND_REASON;
                                                            String pCOMMENT = objectMapper.writeValueAsString(jsonCertComment);
                                                            db.S_BO_CERTIFICATION_DISABLE(attrID, sSUSPEND_TIME_DB, pCOMMENT, loginUID);
                                                            sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                            strView = "0#0";
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                        }
                                                    } else {
                                                        strView = "1#0";
                                                    }
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_APPROVE_AGENCY + "#0";
                                            }
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
                            case "re-recoverycert": {
                                //<editor-fold defaultstate="collapsed" desc="re-recoverycert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String attrID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    boolean isAccessAgency = true;
                                    String sTOKEN_SN = "";
                                    String pCERTIFICATION_SN = "";
                                    String pCERTIFICATION_AUTHORITY_ID = "";
                                    String sAGENT_ID = "";
                                    String sVALUE = "";
                                    String certID = "";
                                    int sCertTypeID = 0;
                                    int pCERTIFICATION_ID = 0;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(attrID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_ID = rsReq[0][0].ID;
                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        sVALUE = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        certID = String.valueOf(rsReq[0][0].ID);
                                        sCertTypeID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
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
                                        CommonFunction.LogDebugString(log, "RE-RECOVERY-CERTIFICATION", "REQUEST: " + "pCERTIFICATION_SN: " + pCERTIFICATION_SN
                                            + "; sTOKEN_SN: " + sTOKEN_SN + "; certID: " + certID + "; attrID: " + attrID
                                            + "; pCERTIFICATION_AUTHORITY_ID: " + pCERTIFICATION_AUTHORITY_ID + "; sCertTypeID: " + sCertTypeID);
                                        if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            if (!"".equals(sVALUE) && sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED) {
                                                if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                    || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
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
                                                    ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_REMOVEFROMCRL_ID,
                                                        pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, Integer.parseInt(attrID));
                                                    if (intRes[0] == 0) {
                                                        db.S_BO_CERTIFICATION_RECOVERED(attrID, loginUID);
                                                        sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                        strView = "0#0";
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                    }
                                                } else {
                                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,
                                                        Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
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
                                                        ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_REMOVEFROMCRL_ID,
                                                            pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, Integer.parseInt(attrID));
                                                        if (intRes[0] == 0) {
                                                            db.S_BO_CERTIFICATION_RECOVERED(attrID, loginUID);
                                                            sessionsa.setAttribute("RefreshApproveReqSess", "1");
                                                            strView = "0#0";
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                        }
                                                    } else {
                                                        strView = "1#0";
                                                    }
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_APPROVE_AGENCY + "#0";
                                            }
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
                            case "editcontractcert": {
                                //<editor-fold defaultstate="collapsed" desc="editcontractcert">
                                String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                CERTIFICATION[][] rs = new CERTIFICATION[1][];
                                db.S_BO_CERTIFICATION_DETAIL(EscapeUtils.escapeHtml(id), sessLanguage, rs);
                                String idPHONE_CONTRACTOld = "";
                                String idEMAIL_CONTRACTOld = "";
                                String idPHONE_REALOld = "";
                                String idEMAIL_REALOld = "";
                                String idCertSN = "";
                                String sVALUE = "";
                                int intPKI_FORMFACTOR_ID=0;
                                if (rs[0].length > 0) {
                                    idPHONE_CONTRACTOld = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT);
                                    idEMAIL_CONTRACTOld = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT);
                                    idPHONE_REALOld = EscapeUtils.CheckTextNull(rs[0][0].PHONE_CONTRACT_REAL);
                                    idEMAIL_REALOld = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT_REAL);
                                    idCertSN = EscapeUtils.CheckTextNull(rs[0][0].CERTIFICATION_SN);
                                    intPKI_FORMFACTOR_ID = rs[0][0].PKI_FORMFACTOR_ID;
                                    sVALUE = EscapeUtils.CheckTextNull(rs[0][0].VALUE);
                                }
                                String idPHONE_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("idPHONE_CONTRACT"));
                                String idEMAIL_CONTRACT = EscapeUtils.CheckTextNull(request.getParameter("idEMAIL_CONTRACT"));
                                String SIGNING_PROFILES = EscapeUtils.CheckTextNull(request.getParameter("SIGNING_PROFILES"));
                                if(!idPHONE_CONTRACT.equals(idPHONE_CONTRACTOld) || !idEMAIL_CONTRACT.equals(idEMAIL_CONTRACTOld))
                                {
                                    String sQuery = db.S_BO_CERTIFICATION_UPDATE_CONTRACT(Integer.parseInt(id), idEMAIL_CONTRACT, idPHONE_CONTRACT, loginUID);
                                    if("0".equals(sQuery)) {
                                        strView = "0#0";
                                    } else {
                                        strView = sQuery+"#0";
                                    }
                                } else {
                                    strView = "0#0";
                                }
                                String sPhoneContactReal = EscapeUtils.CheckTextNull(request.getParameter("sPhoneContactReal"));
                                String sEmailContactReal = EscapeUtils.CheckTextNull(request.getParameter("sEmailContactReal"));
                                if(!sPhoneContactReal.equals(idPHONE_REALOld) || !sEmailContactReal.equals(idEMAIL_REALOld)) {
                                    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                    String userLoginID = request.getSession(false).getAttribute("UserID").toString().trim();
                                    String sQuery = dbTwo.S_BO_CERTIFICATION_UPDATE_CONTACT_REAL(Integer.parseInt(id), sPhoneContactReal, sEmailContactReal, userLoginID);
                                    if(!"0".equals(sQuery)) {
                                        CommonFunction.LogErrorServlet(log, "EditContractCert Real: " + sQuery);
                                    }
                                } else {
                                    strView = "0#0";
                                }
                                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                if((intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                                    || intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_REMOTE_SIGNING)
                                    && (isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_FPT) || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_MID)))
                                {
                                    PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                    db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                    String sFormFactorPro = "";
                                    if(rsFormfactorPro[0].length > 0) {
                                        sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                    }
                                    CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                    if(credentialAuthen != null) {
                                        if(!"".equals(sVALUE)) {
                                            objectMapper = new ObjectMapper();
                                            ATTRIBUTE_VALUES valueRssp = objectMapper.readValue(sVALUE, ATTRIBUTE_VALUES.class);
                                            String agreementUUID = EscapeUtils.CheckTextNull(valueRssp.getRsspAgreementUUID());
                                            String relyingPartyName = EscapeUtils.CheckTextNull(valueRssp.getRsspRelyingParty());
                                            RSSPProcessCommon clsRSSP = new RSSPProcessCommon();
                                            int[] sCode = new int[1]; String[] sParam = new String[3];
                                            clsRSSP.changeSigningProfile(credentialAuthen, relyingPartyName, agreementUUID, idCertSN, SIGNING_PROFILES, sParam, sCode);
                                            if(sCode[0] != 0) {
                                                CommonFunction.LogErrorServlet(log, "changeSigningProfile Error: " + sParam[0]);
                                            }
                                        } else {CommonFunction.LogErrorServlet(log, "changeSigningProfile Error: VALUE ATTR EMPTY");}
                                    } else {CommonFunction.LogErrorServlet(log, "changeSigningProfile Error: CREDENTIAL AUTHEN NULL");}
                                }
                                //</editor-fold>
                                break;
                            }
                            case "resendotp": {
                                //<editor-fold defaultstate="collapsed" desc="resendotp">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sCERTIFICATION_ID = EscapeUtils.CheckTextNull(request.getParameter("sCERTIFICATION_ID"));
                                    int[] intRes = new int[1];
                                    String[] sRes = new String[1];
                                    ConnectConnector.SendMailOTP(String.valueOf(sCERTIFICATION_ID), intRes, sRes);
                                    if(intRes[0] == 0)
                                    {
                                        strView = "0#0";
                                    } else {
                                        strView = "1#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "checktokenvalidcert": {
                                //<editor-fold defaultstate="collapsed" desc="checktokenvalidcert">
                                String pTOKEN_ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                String pRE_TOKEN_ID = EscapeUtils.CheckTextNull(request.getParameter("sRe_ID"));
//                                System.out.println("pTOKEN_ID: " + pTOKEN_ID + " ; pRE_TOKEN_ID: " + pRE_TOKEN_ID);
                                if(!"".equals(pTOKEN_ID)) {
                                    pTOKEN_ID = seEncript.decrypt(URLDecoder.decode(pTOKEN_ID));
                                } else if(!"".equals(pRE_TOKEN_ID))
                                {
                                    pTOKEN_ID = pRE_TOKEN_ID;
                                }
                                String sQuery = db.S_BO_TOKEN_CHECK(pTOKEN_ID);
                                if("0".equals(sQuery))
                                {
                                    strView = "0#0";
                                } else {
                                    strView = sQuery+"#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "changepassp12": {
                                //<editor-fold defaultstate="collapsed" desc="changepassp12">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sCERTIFICATION_ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(sCERTIFICATION_ID, sessLanguage, rsCert);
                                    if(rsCert[0].length > 0)
                                    {
                                        objectMapper = new ObjectMapper();
                                        String strPassword = CommonFunction.randomPasswordP12(8);
                                        EncodeSOPIN encript = new EncodeSOPIN();
                                        List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes = new ArrayList<>();
                                        CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                        attribute.setKey(CERTIFICATION_PROPERTIES_JSON.Attribute.KEY_KEYSTORE_PASSWORD);
                                        attribute.setValue(encript.encode(strPassword));
                                        attributes.add(attribute);
                                        String jsonProperties = "{\"attributes\":" + objectMapper.writeValueAsString(attributes) + "}";
                                        String sUpdate = db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(sCERTIFICATION_ID, jsonProperties, loginUID);
                                        if("0".equals(sUpdate))
                                        {
                                            int[] intRes = new int[1];
                                            String[] sRes = new String[1];
                                            // AFTER_EDIT
                                            String strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                            byte[] sByteFile = ConnectConnector.generateKeystore(strPasswordP12, true, sCERTIFICATION_ID, intRes, sRes);
                                            if(intRes[0] == 0)
                                            {
                                                sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                strView = "0#0";
                                            } else {
                                                strView = "1#0";
                                            }
                                        } else {
                                            strView = "2#0";
                                        }
                                    } else {
                                        strView = "3#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "re-issuecertsoft": {
                                //<editor-fold defaultstate="collapsed" desc="re-issuecertsoft">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String sAGENT_ID;
                                    String sTOKEN_SN = "";
                                    int sTOKEN_ID = 0;
                                    int sStatusRequest = 0;
                                    int sCertTypeID = 0;
                                    int sCertPurposeID = 0;
                                    int sFormFactorID = 0;
                                    objectMapper = new ObjectMapper();
                                    // check agency
                                    boolean isAccessAgency = true;
                                    boolean isBackupKey = true;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(EscapeUtils.CheckTextNull(sID), sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sTOKEN_ID = rsReq[0][0].TOKEN_ID;
                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        sStatusRequest = rsReq[0][0].CERTIFICATION_ATTR_STATE_ID;
                                        sCertTypeID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        sCertPurposeID = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                        sFormFactorID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        isBackupKey = rsReq[0][0].PRIVATE_KEY_ENABLED;
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
                                    if (isAccessAgency == true) {
                                        if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            if (sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED)
                                            {
                                                String strPasswordP12 = "";
                                                if(sFormFactorID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                {
                                                    if(isBackupKey == true)
                                                    {
                                                        strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                                    }
                                                }
                                                if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                                {
                                                    if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                        || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                        || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                        || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                                                    {
//                                                        if(sTOKEN_ID == Definitions.CONFIG_TOKEN_SSL_ID
//                                                            || sTOKEN_ID == Definitions.CONFIG_TOKEN_SIGNSERVER_ID
//                                                            || sTOKEN_ID == Definitions.CONFIG_TOKEN_CODESIGNNING_ID)
//                                                        {
                                                        if(sFormFactorID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                        {
                                                            // RACONNECTOR APPROVE
                                                            int[] intWSRes = new int[1];
                                                            String[] sWSRes = new String[1];
                                                            ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, EscapeUtils.CheckTextNull(sID), intWSRes, sWSRes);
                                                            if (intWSRes[0] == 0) {
                                                                request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                strView = "0#0";
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                            }
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                            CommonFunction.LogErrorServlet(log, "Re-IssueCertSoft: CERTIFICATION_PURPOSE_ID Not yet supported");
                                                        }
                                                    }
                                                } else {
                                                    ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                        if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                        || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                        || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                        || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
//                                                            if(sTOKEN_ID == Definitions.CONFIG_TOKEN_SSL_ID
//                                                                || sTOKEN_ID == Definitions.CONFIG_TOKEN_SIGNSERVER_ID
//                                                                || sTOKEN_ID == Definitions.CONFIG_TOKEN_CODESIGNNING_ID)
//                                                            {
                                                            if(sFormFactorID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                            {
                                                                // RACONNECTOR APPROVE
                                                                int[] intWSRes = new int[1];
                                                                String[] sWSRes = new String[1];
                                                                ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, EscapeUtils.CheckTextNull(sID), intWSRes, sWSRes);
                                                                if (intWSRes[0] == 0) {
                                                                    request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
                                                                    strView = "0#0";
                                                                } else {
                                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                }
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                                CommonFunction.LogErrorServlet(log, "Re-IssueCertSoft: CERTIFICATION_PURPOSE_ID Not yet supported");
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_HAS_STATUS + "#0";
                                            }
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
                            case "re-registrationcertsoft": {
                                //<editor-fold defaultstate="collapsed" desc="re-registrationcertsoft">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = "";// String.valueOf(Definitions.CONFIG_TOKEN_ID_UNKNOWN);
                                    String CERTIFICATION_ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    String sTOKEN_SN = "";// Definitions.CONFIG_TOKEN_UNASSIGN_SN;
                                    String BRANCH_ID = "";
                                    String CheckCHANGE_KEY = "";
                                    String sPRIVATE_KEY = "";
                                    String CheckPRIVATE_KEY = "";
                                    boolean isProcess = true;
                                    String pCSR = EscapeUtils.CheckTextNull(request.getParameter("pCSR"));
                                    String pCERTIFICATION_PURPOSE = EscapeUtils.CheckTextNull(request.getParameter("pCERTIFICATION_PURPOSE"));
                                    if(!"".equals(pCERTIFICATION_PURPOSE))
                                    {
                                        if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_SSL)))
                                        {
                                            ID = String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID);
                                            sTOKEN_SN = Definitions.CONFIG_TOKEN_SSL_SN;
                                        } else if(pCERTIFICATION_PURPOSE.equals(String.valueOf(Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING)))
                                        {
                                            ID = String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID);
                                            sTOKEN_SN = Definitions.CONFIG_TOKEN_CODESIGNNING_SN;
                                        } else {
                                            ID = String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID);
                                            sTOKEN_SN = Definitions.CONFIG_TOKEN_SIGNSERVER_SN;
                                        }
                                    }
                                    String pCREATE_USER = EscapeUtils.CheckTextNull(request.getParameter("CREATE_USER"));
                                    String strIsPushNotiApprove = "0";
                                    // check agency
                                    boolean isAccessAgency = true;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(CERTIFICATION_ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        BRANCH_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                            if (!BRANCH_ID.equals(SessUserAgentID)) {
//                                                isAccessAgency = false;
//                                            }
                                        }
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
                                        String pPAST_CERTIFICATE_ID = Definitions.CONFIG_CERTIFICATE_PAST_CERTIFICATE_ID;
                                        String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION);
                                        String pCERTIFICATION_SN = "";// CommonFunction.generateCertificateSerialNumber();
                                        int pPKI_FORMFACTOR_ID = Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN;
                                        boolean isValidCSR = true;
                                        if(!"".equals(pCSR))
                                        {
                                            String sKeySizeDB = "";
                                            isValidCSR = false;
                                            CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                            db.S_BO_GET_ALGORITHM_KEY_SIZE(CertProfileID, rsCert);
                                            if(rsCert[0].length > 0)
                                            {
                                                sKeySizeDB = EscapeUtils.CheckTextNull(rsCert[0][0].KEY_SIZE);
                                                String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(pCSR);
                                                CommonFunction.LogDebugString(log, "KEY_SIZE_SOFTTOKEN", "DB: " + sKeySizeDB + "; CSR: " + sKeySizeCSR);
                                                if(sKeySizeDB.equals(sKeySizeCSR)) {
                                                    isValidCSR = true;
                                                }
                                                else {
                                                    isValidCSR = false;
                                                }
                                            }
                                        }
                                        if(isValidCSR == true) {
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
                                            
                                            String[] sUIDResult = new String[2];
                                            CommonReferServlet.collectFieldToUID(pTAX_CODE, pBUDGET_CODE, "", pP_ID, pPASSPORT, pCCCD, sUIDResult);
                                            String sEnterpriseCert = sUIDResult[0];
                                            String sPersonalCert = sUIDResult[1];
                                            
                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                            String sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(ID), CertProfileID, sTOKEN_SN,
                                                    pCERTIFICATION_SN, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME, pTAX_CODE, pBUDGET_CODE, pP_ID,
                                                    pPASSPORT, DN, CACoreSubject, PHONE_CONTRACT, EMAIL_CONTRACT, BRANCH_ID,
                                                    pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, "",
                                                    strReqValueATTR, pCREATE_USER, loginUID, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, pCSR,
                                                    CheckPRIVATE_KEY, CheckCHANGE_KEY, sPRIVATE_KEY, pPKI_FORMFACTOR_ID, "", "", pCCCD, null, "", sPersonalCert, sEnterpriseCert);
                                            if ("0".equals(sParam)) {
                                                if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
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
                                                        CERTIFICATION[][] rsReqAttr = new CERTIFICATION[1][];
                                                        db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sessLanguage, rsReqAttr);
                                                        if (rsReqAttr[0].length > 0) {
                                                            String CheckPUSH_NOTICE = EscapeUtils.CheckTextNull(request.getParameter("CheckPUSH_NOTICE"));
                                                            db.S_BO_CERTIFICATION_UPDATE_AMOUNT(rsReqAttr[0][0].ID, "", CheckPUSH_NOTICE, loginUID);
                                                        }
                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                        if ("0".equals(sApprove)) {
                                                            if(!"".equals(ID))
                                                            {
                                                                if(ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                    || ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                    || ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID)))
                                                                {
                                                                    // RACONNECTOR APPROVE
                                                                    int[] intWSRes = new int[1];
                                                                    String[] sWSRes = new String[1];
                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, "", String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                    if (intWSRes[0] == 0) { }
                                                                    else {
                                                                        isProcess = false;
                                                                    }
                                                                } else {
                                                                    CommonFunction.LogErrorServlet(log, "Re-RegistrationCertSoft: CERTIFICATION_PURPOSE_ID UNKNOWN");
                                                                }
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
                                                            CERTIFICATION[][] rsReqAttr = new CERTIFICATION[1][];
                                                            db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sessLanguage, rsReqAttr);
                                                            if (rsReqAttr[0].length > 0) {
                                                                String CheckPUSH_NOTICE = EscapeUtils.CheckTextNull(request.getParameter("CheckPUSH_NOTICE"));
                                                                db.S_BO_CERTIFICATION_UPDATE_AMOUNT(rsReqAttr[0][0].ID, "", CheckPUSH_NOTICE, loginUID);
                                                            }
                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, loginUID);
                                                            if ("0".equals(sApprove)) {
                                                                if(!"".equals(ID))
                                                                {
                                                                    if(ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                    || ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                    || ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID)))
                                                                    {
                                                                        // RACONNECTOR APPROVE
                                                                        int[] intWSRes = new int[1];
                                                                        String[] sWSRes = new String[1];
                                                                        ConnectConnector.EnrollCertificate(sTOKEN_SN, "", String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                        if (intWSRes[0] == 0) { }
                                                                        else {
                                                                            isProcess = false;
                                                                        }
                                                                    } else {
                                                                        CommonFunction.LogErrorServlet(log, "Re-RegistrationCertSoft: CERTIFICATION_PURPOSE_ID UNKNOWN");
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
                                                if(isProcess == true) {
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
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                }
                                            } else {
                                                strView = sParam + "#0";
                                            }
                                        }
                                        else {
                                            strView = Definitions.CONFIG_EXCEPTION_CSR_KEYSIZE + "#0";
                                        }
                                        //
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "checkcertarlamuser": {
                                //<editor-fold defaultstate="collapsed" desc="checkcertarlamuser">
                                String pCERTIFICATION_PURPOSE = EscapeUtils.CheckTextNull(request.getParameter("pCERTIFICATION_PURPOSE"));
                                String pMNS = EscapeUtils.CheckTextNull(request.getParameter("pMNS"));
                                String pMST = EscapeUtils.CheckTextNull(request.getParameter("pMST"));
                                String pDECISION = EscapeUtils.CheckTextNull(request.getParameter("pDECISION"));
                                String pCMND = EscapeUtils.CheckTextNull(request.getParameter("pCMND"));
                                String pHC = EscapeUtils.CheckTextNull(request.getParameter("pHC"));
                                String pCCCD = EscapeUtils.CheckTextNull(request.getParameter("pCCCD"));
                                String pFormFactor = EscapeUtils.CheckTextNull(request.getParameter("pFormFactor"));
                                String pENTERPRISE_ID = EscapeUtils.CheckTextNull(request.getParameter("pENTERPRISE_ID"));
                                String pPERSONAL_ID = EscapeUtils.CheckTextNull(request.getParameter("pPERSONAL_ID"));
                                String sQuery = db.S_BO_CHECK_ARLAM_CERTIFICATION(Integer.parseInt(pCERTIFICATION_PURPOSE),
                                    pMST, pCMND, pMNS, pDECISION, pHC, pCCCD, pFormFactor, pENTERPRISE_ID, pPERSONAL_ID);
                                if("1".equals(sQuery) || "3".equals(sQuery)) {
                                    String strAlam = "";
                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy[0].length > 0) {
                                        for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) 
                                        {
                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ALERT_CHECK_CERT_FOR_USER))
                                            {
                                                strAlam = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                            }
                                        }
                                    }
                                    strView = "0#0#" + strAlam;
                                } else if("2".equals(sQuery)) {
                                    strView = "2#0";
                                } else {
                                    strView = "1#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "resignconfirm": {
                                //<editor-fold defaultstate="collapsed" desc="resignconfirm">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    // check agency
                                    int sUpdate = db.S_BO_CERTIFICATION_UPDATE_RESIGNING_CONFIRMATION_PAPER_ENABLED(Integer.parseInt(ID), "1", loginUID);
                                    if(sUpdate == 0) {
                                        strView = "0#0";
                                    } else {
                                        strView = sUpdate + "#" + sUpdate;
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "cancelresignconfirm": {
                                //<editor-fold defaultstate="collapsed" desc="cancelresignconfirm">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    // check agency
                                    int sUpdate = db.S_BO_CERTIFICATION_UPDATE_RESIGNING_CONFIRMATION_PAPER_ENABLED(Integer.parseInt(ID), "0", loginUID);
                                    if(sUpdate == 0){
                                        strView = "0#0";
                                    } else {
                                        strView = sUpdate + "#" + sUpdate;
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "checkrevokeallow": {
                                //<editor-fold defaultstate="collapsed" desc="checkrevokeallow">
                                Config conf = new Config();
                                String isCheckRevoke = conf.GetTryPropertybyCode(Definitions.CONFIG_FORBIDEN_REVOKE_CONTINUOU_DOUBLE_ENABLED);
                                if("1".equals(isCheckRevoke)) {
                                    boolean isAccessAgency = true;
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String sAGENT_ID = "";
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
                                                sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
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
                                    if (isAccessAgency == true) {
                                        int sResult = db.S_BO_CHECK_ARLAM_FORBIDEN_REVOKE(Integer.parseInt(ID));
                                        if(sResult == 0) {
                                            String[] pNOTIFY = new String[1];
                                            sResult = db.S_BO_CHECK_LIMIT_REVOKE(Integer.parseInt(sAGENT_ID), Integer.parseInt(ID), pNOTIFY);
                                            switch (sResult) {
                                                case 0:
                                                    strView = "0#0#0";
                                                    break;
                                                case 1:
                                                case 2:
                                                    strView = "0#LIMIT_REVOKE#"+pNOTIFY[0];
                                                    break;
                                                default:
                                                    strView = "LIMIT_REVOKE#0#"+pNOTIFY[0];
                                                    break;
                                            }
                                        } else {
                                            strView = "FORBIDEN_REVOKE#0";
                                        }
                                    } else {
                                        
                                    }
                                } else{
                                    strView = "0#0#0";
                                }
                                CommonFunction.LogDebugString(log, "RevokeEvent", strView);
                                //</editor-fold>
                                break;
                            }
                            case "resendmailconfirmhsm": {
                                //<editor-fold defaultstate="collapsed" desc="resendmailconfirmhsm">
                                String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                String isActiveSignServer = "0";
                                CERTIFICATION[][] rs = new CERTIFICATION[1][];
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
                                    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                    db.S_BO_CERTIFICATION_DETAIL(id, sessLanguage, rs);
                                    if (rs[0].length > 0) {
                                        if(rs[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN
                                            && rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED){
                                            String idEMAIL_CONTRACT = EscapeUtils.CheckTextNull(rs[0][0].EMAIL_CONTRACT);
                                            String idDN = EscapeUtils.CheckTextNull(rs[0][0].SUBJECT);
                                            if(!"".equals(idEMAIL_CONTRACT)) {
                                                CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, id, idDN, idEMAIL_CONTRACT, sessLanguage);
                                                String sCONFIRMATION_HSM = rs[0][0].CONFIRMATION_PROPERTIES;
                                                if(!"".equals(sCONFIRMATION_HSM)){
                                                    objectMapper = new ObjectMapper();
                                                    CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(sCONFIRMATION_HSM, CERTIFICATION_PROPERTIES_JSON.class);
                                                    if(itemParsePush.getAttributes().size() > 0) {
                                                        for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
                                                            if(EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getKey()).equals(Definitions.CONFIG_VALUE_HSM_CONFIRM_DECLINE_ENABLED)){
                                                                itemParsePush.getAttributes().get(i).setValue("false");
                                                            }
                                                        }
                                                        dbTwo.S_RAC_CERTIFICATION_ATTR_UPDATE_CONFIRMATION_PROPERTIES(rs[0][0].CERTIFICATION_ATTR_ID, loginUID, objectMapper.writeValueAsString(itemParsePush));
                                                    }
                                                }
                                                dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(rs[0][0].CERTIFICATION_ATTR_ID, 0);
                                                strView = "0#0";
                                            } else {
                                                strView = "1#0";
                                            }
                                        } else {
                                            strView = "1#0";
                                        }
                                    } else {
                                        strView = "1#0";
                                    }
                                } else {
                                    strView = "1#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "registrationcert": {
                                break;
                            }
                            case "registrationcertunassign": {
                                break;
                            }
                            case "registrationcertsoft": {
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
