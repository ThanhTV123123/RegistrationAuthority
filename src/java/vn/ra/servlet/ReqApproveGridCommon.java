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
import vn.ra.object.DNS_NAME_DATA;
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
import vn.ra.process.SessionDNSName;
import vn.ra.process.SessionUploadFileCert;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;
import vn.ra.process.EncodeSOPIN;
import vn.ra.thread.ThreadCallbackApproved;
/**
 *
 * @author vanth
 */
public class ReqApproveGridCommon extends HttpServlet {
private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReqApproveGridCommon.class.getName());
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
//            int[] pCERTIFICATE_ATTR_ID = new int[1];
//            int[] pCERTIFICATE_ID = new int[1];
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
//                    DESEncryption seEncript = new DESEncryption();
                    CERTIFICATION_DATA_ATTR tempLogReq;
                    if (null != idParam) {
                        switch (idParam) {
                            case "approvecertchildrenagency": {
                                //<editor-fold defaultstate="collapsed" desc="approvecertchildrenagency">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                    String ID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String CheckCHANGE_KEY = "";
                                    String CheckPRIVATE_KEY = "";
                                    String CheckREVOKE_ENABLED ="";
                                    String keepCertSNEnabled = "";
                                    String sAGENT_ID="";
                                    String sTOKEN_SN = "";
                                    String sVALUE_OLD = "";
                                    String pCERTIFICATION_SN = "";
                                    String pCERTIFICATION_AUTHORITY_ID = "";
                                    String strReqValueATTR = "";
                                    boolean sPUSH_NOTICE_ENABLED = false;
                                    int sCERTIFICATION_ID = 0;
                                    int sCERTIFICATION_ATTR_TYPE_ID = 0;
                                    int pPKI_FORMFACTOR_ID = 0;
                                    String CertProfileID = "";
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    int sStatusRequest = 0;
                                    boolean pPRIVATE_KEY_ENABLED = true;
                                    boolean isAccessAgency = true;
                                    String pPAST_CERTIFICATE_ID = "";
                                    String pPERSONAL_ID = "";
                                    String pENTERPRISE_ID = "";
                                    String pTAX_CODE = "";
                                    String pDECISION = "";
                                    String pBUDGET_CODE = "";
                                    String pP_ID = "";
                                    String pCCCD = "";
                                    String pPASSPORT = "";
                                    String pSUBJECT = "";
                                    String strEmailCust = "";
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        CertProfileID = String.valueOf(rsReq[0][0].CERTIFICATION_PROFILE_ID);
                                        pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                        strEmailCust = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                        pSUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sStatusRequest = rsReq[0][0].CERTIFICATION_ATTR_STATE_ID;
                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        pPAST_CERTIFICATE_ID = String.valueOf(rsReq[0][0].PAST_CERTIFICATION_ID);
                                        sCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                        sCERTIFICATION_ID = rsReq[0][0].ID;
                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        sPUSH_NOTICE_ENABLED = rsReq[0][0].PUSH_NOTICE_ENABLED;
                                        pPERSONAL_ID = rsReq[0][0].PERSONAL_ID;
                                        pENTERPRISE_ID = rsReq[0][0].ENTERPRISE_ID;
                                        pTAX_CODE = EscapeUtils.CheckTextNull(rsReq[0][0].TAX_CODE);
                                        pDECISION = EscapeUtils.CheckTextNull(rsReq[0][0].DECISION);
                                        pBUDGET_CODE = EscapeUtils.CheckTextNull(rsReq[0][0].BUDGET_CODE);
                                        pP_ID = EscapeUtils.CheckTextNull(rsReq[0][0].P_ID);
                                        pCCCD = EscapeUtils.CheckTextNull(rsReq[0][0].P_EID);
                                        pPASSPORT = EscapeUtils.CheckTextNull(rsReq[0][0].PASSPORT);
                                        objectMapper = new ObjectMapper();
                                        if(!"".equals(sVALUE_OLD)){
                                            ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                            CheckCHANGE_KEY = valueATTR_Frist.getChangeKeyEnabled() ? "1" : "0";
                                            CheckREVOKE_ENABLED = valueATTR_Frist.getRevokeOldCertificateEnabled() ? "1" : "0";
                                            if(sVALUE_OLD.contains(Definitions.CONFIG_POLICY_VALUE_CONTAIN_CERT_ATTR_KEEP_SN)) {
                                                keepCertSNEnabled = valueATTR_Frist.getKeepCertificateSNEnabled() ? "1" : "0";
                                            }
                                        }
                                        CheckPRIVATE_KEY = pPRIVATE_KEY_ENABLED ? "1" : "0";
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
                                                        CheckPRIVATE_KEY = "0";
                                                    }
                                                }
                                                if(isCSRValid == true)
                                                {
                                                    if(isCSR_SizeValid == true)
                                                    {
                                                        String CheckDeleteCertForOtherRevoke = "0";
                                                        String CheckDeleteCertForRevoke = "0";
                                                        //<editor-fold defaultstate="collapsed" desc="### PARAM and GET INFO">
                                                        if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                                            if("0".equals(CheckCHANGE_KEY)) {
                                                                CERTIFICATION[][] rsCertOld = new CERTIFICATION[1][];
                                                                db.S_BO_CERTIFICATION_DETAIL(pPAST_CERTIFICATE_ID, sessLanguage, rsCertOld);
                                                                if(rsCertOld[0].length > 0) {
                                                                    pPERSONAL_ID = rsCertOld[0][0].PERSONAL_ID;
                                                                    pENTERPRISE_ID = rsCertOld[0][0].ENTERPRISE_ID;
                                                                    pTAX_CODE = EscapeUtils.CheckTextNull(rsCertOld[0][0].TAX_CODE);
                                                                    pDECISION = EscapeUtils.CheckTextNull(rsCertOld[0][0].DECISION);
                                                                    pBUDGET_CODE = EscapeUtils.CheckTextNull(rsCertOld[0][0].BUDGET_CODE);
                                                                    pP_ID = EscapeUtils.CheckTextNull(rsCertOld[0][0].P_ID);
                                                                    pCCCD = EscapeUtils.CheckTextNull(rsCertOld[0][0].P_EID);
                                                                    pPASSPORT = EscapeUtils.CheckTextNull(rsCertOld[0][0].PASSPORT);
                                                                }
                                                            }
                                                        }
                                                        tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                        objectMapper = new ObjectMapper();
                                                        tempLogReq.personalID = pPERSONAL_ID;
                                                        tempLogReq.enterpriseID = pENTERPRISE_ID;
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
                                                            boolean sDeleteOldCertChangeValue = valueATTR_Frist.getDeleteOldCertificateEnabled();
                                                            boolean sDeleteOldCertRevokeValue = valueATTR_Frist.getCertRevokeDeleteInTokenEnabled();
                                                            if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                            {
                                                                CheckDeleteCertForRevoke = sDeleteOldCertRevokeValue == true ? "1" : "0";
                                                            } else {
                                                                CheckDeleteCertForOtherRevoke = sDeleteOldCertChangeValue == true ? "1" : "0";
                                                            }
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
                                                                                }
                                                                                autoApproveCA = CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data);
                                                                            }
                                                                        }
                                                                    }
                                                                    if(autoApproveCA == true) {
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
                                                                            String sDiscountRate = "0";
                                                                            String sDiscountRateOption = "0";
                                                                            GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                            if (sessGeneralPolicy1[0].length > 0) {
                                                                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                {
                                                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                                                                                    {
                                                                                        sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                        break;
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
//                                                                            String[] sUIDResult = new String[2];
//                                                                            CommonReferServlet.collectFieldToUID(pTAX_CODE, pBUDGET_CODE, pDECISION, pP_ID, pPASSPORT, pCCCD, sUIDResult);
//                                                                            String sEnterpriseCert = sUIDResult[0];
//                                                                            String sPersonalCert = sUIDResult[1];
                                                                            
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
                                                                                                // RA SEND_EMAIL
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
                                                                                            if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN))
                                                                                            {
                                                                                                if(!"".equals(strDNSName))
                                                                                                {
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
                                                                                                    CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(sCERTIFICATION_ID), pSUBJECT, strEmailCust, sessLanguage);
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
                                                                                        CheckREVOKE_ENABLED, sRsspRelyingParty, sResultRSSP, pCERTIFICATION_SN,
                                                                                        sChangeKeyApprove, sRequestTypeRssp, "", credentialAuthen, revokeSetOldStatus);
                                                                                    if("0".equals(sResultRSSP[0])) {
                                                                                        request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
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
                                    String sAGENT_ID="";
                                    String sTOKEN_SN = "";
                                    String sVALUE_OLD = "";
                                    String pCERTIFICATION_SN = "";
                                    String pCERTIFICATION_AUTHORITY_ID = "";
                                    String strReqValueATTR = "";
                                    boolean sPUSH_NOTICE_ENABLED = false;
                                    int sCERTIFICATION_ID = 0;
                                    int sCERTIFICATION_ATTR_TYPE_ID = 0;
                                    int pPKI_FORMFACTOR_ID = 0;
                                    String CertProfileID = "";
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    int sStatusRequest = 0;
                                    boolean pPRIVATE_KEY_ENABLED = true;
                                    String pPAST_CERTIFICATE_ID = "";
                                    String pTAX_CODE = "";
                                    String pPERSONAL_ID = "";
                                    String pENTERPRISE_ID = "";
                                    String pDECISION = "";
                                    String pBUDGET_CODE = "";
                                    String pP_ID = "";
                                    String pCCCD = "";
                                    String pPASSPORT = "";
                                    String strEmailCust = "";
                                    String pSUBJECT = "";
                                    boolean isAccessAgency = true;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        CertProfileID = String.valueOf(rsReq[0][0].CERTIFICATION_PROFILE_ID);
                                        pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sStatusRequest = rsReq[0][0].CERTIFICATION_ATTR_STATE_ID;
                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        pPAST_CERTIFICATE_ID = String.valueOf(rsReq[0][0].PAST_CERTIFICATION_ID);
                                        sCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                        sCERTIFICATION_ID = rsReq[0][0].ID;
                                        pPERSONAL_ID = rsReq[0][0].PERSONAL_ID;
                                        pENTERPRISE_ID = rsReq[0][0].ENTERPRISE_ID;
                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        pTAX_CODE = EscapeUtils.CheckTextNull(rsReq[0][0].TAX_CODE);
                                        pDECISION = EscapeUtils.CheckTextNull(rsReq[0][0].DECISION);
                                        pBUDGET_CODE = EscapeUtils.CheckTextNull(rsReq[0][0].BUDGET_CODE);
                                        pP_ID = EscapeUtils.CheckTextNull(rsReq[0][0].P_ID);
                                        pCCCD = EscapeUtils.CheckTextNull(rsReq[0][0].P_EID);
                                        pPASSPORT = EscapeUtils.CheckTextNull(rsReq[0][0].PASSPORT);
                                        sPUSH_NOTICE_ENABLED = rsReq[0][0].PUSH_NOTICE_ENABLED;
                                        strEmailCust = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                        pSUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        objectMapper = new ObjectMapper();
                                        
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
                                            
                                            if(functionAccess == true) {
                                                boolean isCSRValid = true;
                                                boolean isCSR_SizeValid = true;
                                                String strDNSName = "";
                                                if(isCSRValid == true)
                                                {
                                                    if(isCSR_SizeValid == true)
                                                    {
                                                        String CheckDeleteCertForRevoke = "0";
                                                        String CheckDeleteCertForOtherRevoke = "0";
                                                        //<editor-fold defaultstate="collapsed" desc="### PARAM and GET INFO">
                                                        tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                        objectMapper = new ObjectMapper();
                                                        tempLogReq.enterpriseID = pENTERPRISE_ID;
                                                        tempLogReq.personalID = pPERSONAL_ID;
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
                                                            boolean sDeleteOldCertChangeValue = valueATTR_Frist.getDeleteOldCertificateEnabled();
                                                            boolean sDeleteOldCertRevokeValue = valueATTR_Frist.getCertRevokeDeleteInTokenEnabled();
                                                            if(sCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                            {
                                                                CheckDeleteCertForRevoke = sDeleteOldCertRevokeValue == true ? "1" : "0";
                                                            } else {
                                                                CheckDeleteCertForOtherRevoke = sDeleteOldCertChangeValue == true ? "1" : "0";
                                                            }
                                                            boolean sChangeKeyEnabled = valueATTR_Frist.getChangeKeyEnabled();
                                                            valueATTR_Last.setChangeKeyEnabled(sChangeKeyEnabled);
                                                            boolean sRevokedEnabled = valueATTR_Frist.getRevokeOldCertificateEnabled();
                                                            valueATTR_Last.setRevokeOldCertificateEnabled(sRevokedEnabled);
                                                            if(sVALUE_OLD.contains(Definitions.CONFIG_POLICY_VALUE_CONTAIN_CERT_ATTR_KEEP_SN)) {
                                                                boolean booKeepCertSNEnabled = valueATTR_Frist.getKeepCertificateSNEnabled();
                                                                valueATTR_Last.setKeepCertificateSNEnabled(booKeepCertSNEnabled);
                                                            }
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

                                                        String sParam = db.S_BO_CERTIFICATION_PRE_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                        if("0".equals(sParam))
                                                        {
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
                                                                if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true) {
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
                                                                    String sDiscountRate = "0";
                                                                    String sDiscountRateOption = "0";
                                                                    GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                    if (sessGeneralPolicy1[0].length > 0) {
                                                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                        {
                                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                                                                            {
                                                                                sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                break;
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
                                                                    
//                                                                    String[] sUIDResult = new String[2];
//                                                                    CommonReferServlet.collectFieldToUID(pTAX_CODE, pBUDGET_CODE, pDECISION, pP_ID, pPASSPORT, pCCCD, sUIDResult);
//                                                                    String sEnterpriseCert = sUIDResult[0];
//                                                                    String sPersonalCert = sUIDResult[1];
                                                                    
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
                                                                                    if(sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN))
                                                                                    {
                                                                                        if(!"".equals(strDNSName))
                                                                                        {
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
                                                                                            CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(sCERTIFICATION_ID), pSUBJECT, strEmailCust, sessLanguage);
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
                                                                            String CheckREVOKE_ENABLED = "";
                                                                            if(!"".equals(sVALUE_OLD)){
                                                                                ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                                boolean sRevokedEnabled = valueATTR_Frist.getRevokeOldCertificateEnabled();
                                                                                if(sRevokedEnabled == true){CheckREVOKE_ENABLED = "1";} else {CheckREVOKE_ENABLED = "0";}
                                                                            }
                                                                            clsRSSP.approveCertificateForSignCloud(sAgreementUUID, String.valueOf(sCERTIFICATION_ID),
                                                                                CheckREVOKE_ENABLED, sRsspRelyingParty, sResultRSSP, pCERTIFICATION_SN, sChangeKeyApprove,
                                                                                sRequestTypeRssp, "", credentialAuthen, revokeSetOldStatus);
                                                                            if("0".equals(sResultRSSP[0])) {
                                                                                request.getSession(false).setAttribute("RefreshApproveReqSess", "1");
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
                                    String sAGENT_ID_New = "";
                                    String idREVOKE_REASON = "";
                                    String idSUSPEND_REASON = "";
                                    String idSUSPEND_TIME = "";
                                    String keepCertSNEnabled = "";
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                    java.sql.Timestamp sSUSPEND_TIME_DB = null;
                                    String sAGENT_ID="";
                                    String sAGENT_TOKEN_ID="";
//                                    String sUSER_ID="";
                                    String pPAST_CERTIFICATE_ID = "";
                                    String pCERTIFICATION_AUTHORITY_ID = "";
                                    String pCERTIFICATION_SN = "";
                                    String pTOKEN_SN = "";
                                    String sVALUE_OLD = "";
                                    String strReqValueATTR = "";
                                    String CertProfileID = "";
                                    String pTAX_CODE = "";
                                    String pPERSONAL_ID = "";
                                    String pENTERPRISE_ID = "";
                                    String pDECISION = "";
                                    String pBUDGET_CODE = "";
                                    String pP_ID = "";
                                    String pCCCD = "";
                                    String pPASSPORT = "";
                                    int sCERTIFICATION_ID = 0;
                                    int sStatusRequest = 0;
                                    int sCertTypeID = 0;
                                    int pTOKEN_ID = 0;
                                    int pPKI_FORMFACTOR_ID = 0;
                                    boolean pPRIVATE_KEY_ENABLED = false;
                                    boolean sPUSH_NOTICE_ENABLED = false;
                                    objectMapper = new ObjectMapper();
                                    boolean isAccessAgency = true;
                                    String strPasswordP12 = "";
                                    String pDISCOUNT_RATE = "0";
                                    String strEmailCust = "";
                                    String pSUBJECT = "";
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_APPROVED_DETAIL(ID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        sCERTIFICATION_ID = rsReq[0][0].ID;
                                        CertProfileID = String.valueOf(rsReq[0][0].CERTIFICATION_PROFILE_ID);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sAGENT_ID_New = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sAGENT_TOKEN_ID = String.valueOf(rsReq[0][0].BRANCH_TOKEN_ID);
//                                        sUSER_ID = String.valueOf(rsReq[0][0].CREATED_BY_ID);
                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        pTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                        pPAST_CERTIFICATE_ID = String.valueOf(rsReq[0][0].PAST_CERTIFICATION_ID);
                                        sStatusRequest = rsReq[0][0].CERTIFICATION_ATTR_STATE_ID;
                                        sCertTypeID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        pTOKEN_ID = rsReq[0][0].TOKEN_ID;
                                        pENTERPRISE_ID = rsReq[0][0].ENTERPRISE_ID;
                                        pPERSONAL_ID = rsReq[0][0].PERSONAL_ID;
                                        pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                        sPUSH_NOTICE_ENABLED = rsReq[0][0].PUSH_NOTICE_ENABLED;
                                        sVALUE_OLD = EscapeUtils.CheckTextNull(rsReq[0][0].VALUE);
                                        pDISCOUNT_RATE = String.valueOf(rsReq[0][0].DISCOUNT_RATE);
                                        pTAX_CODE = EscapeUtils.CheckTextNull(rsReq[0][0].TAX_CODE);
                                        pDECISION = EscapeUtils.CheckTextNull(rsReq[0][0].DECISION);
                                        pBUDGET_CODE = EscapeUtils.CheckTextNull(rsReq[0][0].BUDGET_CODE);
                                        pP_ID = EscapeUtils.CheckTextNull(rsReq[0][0].P_ID);
                                        pCCCD = EscapeUtils.CheckTextNull(rsReq[0][0].P_EID);
                                        pPASSPORT = EscapeUtils.CheckTextNull(rsReq[0][0].PASSPORT);
                                        strEmailCust = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                        pSUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
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

                                                if(functionAccess == true)
                                                {
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
                                                        boolean isCSRValid = true;
                                                        boolean isCSR_SizeValid = true;
                                                        if(isCSRValid == true)
                                                        {
                                                            if(isCSR_SizeValid == true)
                                                            {
                                                                String pFO_DELETE_CERT_WHEN_REVOKE = "";
                                                                String sDiscountRateOption = "";
                                                                GENERAL_POLICY[][] sessGeneralPolicy;
                                                                sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                if (sessGeneralPolicy[0].length > 0) {
                                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                                    {
                                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_NO_AUTO_MOVE_TOKEN_FOR_RENEWAL_REVISION_CERTIFICATE_REQUEST))
                                                                        {
                                                                            if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                                            {
                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_DELETE_CERT_WHEN_REVOKE))
                                                                                {
                                                                                    pFO_DELETE_CERT_WHEN_REVOKE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                }
                                                                            } else {
                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DELETE_OLD_CERTIFICATE))
                                                                                {
                                                                                    pFO_DELETE_CERT_WHEN_REVOKE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                }
                                                                            }
                                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                                                                            {
                                                                                sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                if("1".equals(sDiscountRateOption)) {
                                                                    if(sStatusRequest== Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PENDING
                                                                        || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_INIT
                                                                        || sStatusRequest == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_PRE_APPROVED)
                                                                    {
                                                                        if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL)
                                                                        {
                                                                            DISCOUNT_RATE_PROFILE[][] rsDisCount = new DISCOUNT_RATE_PROFILE[1][];
                                                                            db.S_BO_BRANCH_GET_DISCOUNT_RATE_PROFILE(sAGENT_ID, rsDisCount);
                                                                            if(rsDisCount[0].length > 0 && !"".equals(rsDisCount[0][0].PROPERTIES))
                                                                            {
                                                                                PROFILE_DISCOUNT_RATE_DATA[][] resIPData = new PROFILE_DISCOUNT_RATE_DATA[1][];
                                                                                CommonFunction.getAllProfileDiscountRate(rsDisCount[0][0].PROPERTIES, resIPData);
                                                                                if(resIPData[0] != null && resIPData[0].length > 0)
                                                                                {
                                                                                    String sProfileCode = "";
                                                                                    CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                                    if(rsProfile[0].length > 0)
                                                                                    {
                                                                                        sProfileCode = EscapeUtils.CheckTextNull(rsProfile[0][0].NAME);
                                                                                    }
                                                                                    for(PROFILE_DISCOUNT_RATE_DATA resIPData1 : resIPData[0])
                                                                                    {
                                                                                        if(EscapeUtils.CheckTextNull(resIPData1.profileName).equals(sProfileCode))
                                                                                        {
                                                                                            pDISCOUNT_RATE = resIPData1.rosePercent;
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                String sCheckReceivedSoftCopy = "";
                                                                String idReceivedNote = "";
                                                                String STATE_PROFILE = "";
                                                                String profileManagerCAOption = conf.GetPropertybyCode(Definitions.CONFIG_PROFILE_MANAGER_LEVEL_APPROVE_OFCA);
                                                                if(pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD
                                                                    && pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_REMOTE_SIGNING)
                                                                {
                                                                    if("2".equals(profileManagerCAOption)) {
                                                                        CERTIFICATION[][] rsBrief = new CERTIFICATION[1][];
                                                                        db.S_BO_CERTIFICATION_BRIEF_DETAIL(String.valueOf(sCERTIFICATION_ID), rsBrief);
                                                                        if(rsBrief[0].length > 0)
                                                                        {
                                                                            STATE_PROFILE = String.valueOf(rsBrief[0][0].FILE_MANAGER_STATE_ID);
                                                                            String sBRIEF_PROPERTIES = EscapeUtils.CheckTextNull(rsBrief[0][0].BRIEF_PROPERTIES);
                                                                            idReceivedNote = rsBrief[0][0].PROFILE_NOTE;
                                                                            sCheckReceivedSoftCopy = rsBrief[0][0].COLLECT_SOFTCOPY ? "1" : "0";
                                                                            if(!"".equals(sBRIEF_PROPERTIES))
                                                                            {
                                                                                CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
                                                                                CommonFunction.getCollectedBriefProperties(sBRIEF_PROPERTIES, resIPData);
                                                                                if(resIPData[0].length > 0)
                                                                                {
                                                                                    request.getSession(false).setAttribute("SessCollectedBriefPro", resIPData);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
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
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE
                                                                            || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                                        {
                                                                            db.S_BO_CERTIFICATION_UPDATE(sCERTIFICATION_ID, "", "", "", "",
                                                                                pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, "", "", "", "", "",
                                                                                "", loginUID, String.valueOf(pPKI_FORMFACTOR_ID), "", "", pDISCOUNT_RATE, "",
                                                                                pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                        }
                                                                        //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR">
                                                                        ATTRIBUTE_VALUES valueATTR_Last = null;
                                                                        // VALUE ATTR_FRIST
                                                                        ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                        tempLogReq = valueATTR_Frist.getAttributeData().getCertificationData();
                                                                        String sToken_Frist = valueATTR_Frist.getTokenSn();
                                                                        String sCertRevokeReason_Frist = valueATTR_Frist.getCerttificateRevokeReason();
                                                                        String sCertSuspendReason_Frist = valueATTR_Frist.getCerttificateSuspendReason();
                                                                        sSUSPEND_TIME_DB = valueATTR_Frist.getSuspendedTime();
                                                                        boolean sDeleteOldCertChangeValue = valueATTR_Frist.getDeleteOldCertificateEnabled();
                                                                        boolean sDeleteOldCertRevokeValue = valueATTR_Frist.getCertRevokeDeleteInTokenEnabled();
                                                                        boolean sChangeKeyEnabled = valueATTR_Frist.getChangeKeyEnabled();
                                                                        boolean sRevokedEnabled = valueATTR_Frist.getRevokeOldCertificateEnabled();
                                                                        boolean booKeepCertSNEnabled = valueATTR_Frist.getKeepCertificateSNEnabled();
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
                                                                        
                                                                        valueATTR_Last.setChangeKeyEnabled(sChangeKeyEnabled);
                                                                        valueATTR_Last.setRevokeOldCertificateEnabled(sRevokedEnabled);
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
                                                                        if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                                        {
                                                                            pFO_DELETE_CERT_WHEN_REVOKE = sDeleteOldCertRevokeValue == true ? "1" : "0";
                                                                        } else {
                                                                            pFO_DELETE_CERT_WHEN_REVOKE = sDeleteOldCertChangeValue == true ? "1" : "0";
                                                                        }

                                                                        if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                                                            String idCERT_REVOCATION_REASON = String.valueOf(Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID);
                                                                            String CheckDeleteRevoke = pFO_DELETE_CERT_WHEN_REVOKE;
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
                                                                                    objectMapper = new ObjectMapper();
                                                                                    CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                                    jsonCertComment.certificateDeclineReason = "";
                                                                                    jsonCertComment.certificateSuspendReason = "";
                                                                                    jsonCertComment.certificateRevokeReason = idREVOKE_REASON;
                                                                                    idREVOKE_REASON = objectMapper.writeValueAsString(jsonCertComment);
                                                                                    db.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(String.valueOf(sCERTIFICATION_ID), idREVOKE_REASON, loginUID);
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
                                                                        } else if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED)
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
                                                                            db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                            if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN))
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
//                                                                                if(!sAGENT_ID.equals(sAGENT_ID_New) || !sUSER_ID.equals(sUSER_ID_New))
//                                                                                {
//                                                                                    String strReqValueATTRForDecline = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
//                                                                                    db.S_BO_CERTIFICATION_CHANGED_CREATED_BY(ID, sAGENT_ID_New, sUSER_ID_New, loginUID, strReqValueATTRForDecline);
//                                                                                }
                                                                                if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                                {
                                                                                    sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
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
                                                                            String CheckDeleteRevoke = pFO_DELETE_CERT_WHEN_REVOKE;
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
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                            if ("0".equals(sApprove)) {
                                                                                //<editor-fold defaultstate="collapsed" desc="### PROFILE MANAGER PROCESS">
                                                                                if("2".equals(profileManagerCAOption)) {
                                                                                    if (Definitions.CONFIG_AGENT_ROOT.equals(AGENT_ID_LOG)) {
                                                                                        CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessCollectedBriefPro");
                                                                                        String pBRIEF_PROPERTIES = CommonFunction.renderBriefFileType(resProfileData[0], "", idReceivedNote, "");
                                                                                        String param1 = db.S_BO_CERTIFICATION_BRIEF_UPDATE_COLLECT_SOFTCOPY(String.valueOf(sCERTIFICATION_ID),
                                                                                            pBRIEF_PROPERTIES, sCheckReceivedSoftCopy, loginUID, STATE_PROFILE);
                                                                                        request.getSession(false).setAttribute("SessCollectedBriefPro", null);
                                                                                        CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_BRIEF_UPDATE_COLLECT_SOFTCOPY Result", param1);
                                                                                        db.S_BO_FILE_MANAGER_UPDATE_COMMIT_ENABLED(String.valueOf(sCERTIFICATION_ID), loginUID);
                                                                                    }
                                                                                }
                                                                                //</editor-fold>

                                                                                // set COMMIT TRUE File Attachment
                                                                                String[] pRESPONSE_CODE_FILE = new String[1];
                                                                                db.S_BO_CERTIFICATION_SUPPLEMENT_FILE(Integer.parseInt(ID), loginUID, pRESPONSE_CODE_FILE);
                                                                                if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN))
                                                                                {
                                                                                    if (sPUSH_NOTICE_ENABLED == true) {
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
                                                                                    if(pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                                        GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
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
                                                                                            CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(sCERTIFICATION_ID), pSUBJECT, strEmailCust, sessLanguage);
                                                                                            strView = "0#0";
                                                                                        } else {
                                                                                            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
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
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                                            {
                                                                                db.S_BO_CERTIFICATION_UPDATE(sCERTIFICATION_ID, "", "", "", "",
                                                                                    pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, "", "", "", "", "",
                                                                                    "", loginUID, String.valueOf(pPKI_FORMFACTOR_ID), "", "", pDISCOUNT_RATE, "",
                                                                                    pCCCD, pDECISION, pENTERPRISE_ID, pPERSONAL_ID);
                                                                            }
                                                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR">
                                                                            ATTRIBUTE_VALUES valueATTR_Last = null;
                                                                            // VALUE ATTR_FRIST
                                                                            ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                            tempLogReq = valueATTR_Frist.getAttributeData().getCertificationData();
                                                                            String sToken_Frist = valueATTR_Frist.getTokenSn();
                                                                            String sCertRevokeReason_Frist = valueATTR_Frist.getCerttificateRevokeReason();
                                                                            String sCertSuspendReason_Frist = valueATTR_Frist.getCerttificateSuspendReason();
                                                                            boolean sDeleteOldCertChangeValue = valueATTR_Frist.getDeleteOldCertificateEnabled();
                                                                            boolean sDeleteOldCertRevokeValue = valueATTR_Frist.getCertRevokeDeleteInTokenEnabled();
                                                                            boolean sChangeKeyEnabled = valueATTR_Frist.getChangeKeyEnabled();
                                                                            boolean sRevokedEnabled = valueATTR_Frist.getRevokeOldCertificateEnabled();
                                                                            boolean booKeepCertSNEnabled = valueATTR_Frist.getKeepCertificateSNEnabled();
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
                                                                            valueATTR_Last.setChangeKeyEnabled(sChangeKeyEnabled);
                                                                            valueATTR_Last.setRevokeOldCertificateEnabled(sRevokedEnabled);
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
                                                                            
                                                                            if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE)
                                                                            {
                                                                                pFO_DELETE_CERT_WHEN_REVOKE = sDeleteOldCertRevokeValue == true ? "1" : "0";
                                                                            } else {
                                                                                pFO_DELETE_CERT_WHEN_REVOKE = sDeleteOldCertChangeValue == true ? "1" : "0";
                                                                            }
                                                                            if (sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                                                                String idCERT_REVOCATION_REASON = String.valueOf(Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_UNSPECIFIED_ID);
                                                                                String CheckDeleteRevoke = pFO_DELETE_CERT_WHEN_REVOKE;
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
                                                                                        objectMapper = new ObjectMapper();
                                                                                        CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                                        jsonCertComment.certificateDeclineReason = "";
                                                                                        jsonCertComment.certificateSuspendReason = "";
                                                                                        jsonCertComment.certificateRevokeReason = idREVOKE_REASON;
                                                                                        idREVOKE_REASON = objectMapper.writeValueAsString(jsonCertComment);
                                                                                        db.S_BO_CERTIFICATION_UPDATE_REVOKED_REASON(String.valueOf(sCERTIFICATION_ID), idREVOKE_REASON, loginUID);
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
                                                                                db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                                if(pTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN))
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
                                                                                || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                                                                            {
                                                                                if(sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL
                                                                                    || sCertTypeID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO)
                                                                                {
                                                                                    //<editor-fold defaultstate="collapsed" desc="### TOKEN AND CERT TRANSFER">
//                                                                                    if(!sAGENT_ID.equals(sAGENT_ID_New) || !sUSER_ID.equals(sUSER_ID_New))
//                                                                                    {
//                                                                                        String strReqValueATTRForDecline = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
//                                                                                        db.S_BO_CERTIFICATION_CHANGED_CREATED_BY(ID, sAGENT_ID_New, sUSER_ID_New, loginUID, strReqValueATTRForDecline);
//                                                                                    }
                                                                                    if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                                    {
                                                                                        sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
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
                                                                                String CheckDeleteRevoke = pFO_DELETE_CERT_WHEN_REVOKE;
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
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(Integer.parseInt(ID), strReqValueATTR, loginUID);
                                                                                if ("0".equals(sApprove)) {
                                                                                    //<editor-fold defaultstate="collapsed" desc="### PROFILE MANAGER PROCESS">
                                                                                    if("2".equals(profileManagerCAOption)) {
                                                                                        if (Definitions.CONFIG_AGENT_ROOT.equals(AGENT_ID_LOG)) {
                                                                                            CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessCollectedBriefPro");
                                                                                            String pBRIEF_PROPERTIES = CommonFunction.renderBriefFileType(resProfileData[0], "", idReceivedNote, "");
                                                                                            String param1 = db.S_BO_CERTIFICATION_BRIEF_UPDATE_COLLECT_SOFTCOPY(String.valueOf(sCERTIFICATION_ID),
                                                                                                pBRIEF_PROPERTIES, sCheckReceivedSoftCopy, loginUID, STATE_PROFILE);
                                                                                            request.getSession(false).setAttribute("SessCollectedBriefPro", null);
                                                                                            CommonFunction.LogDebugString(log, "S_BO_CERTIFICATION_BRIEF_UPDATE_COLLECT_SOFTCOPY Result", param1);
                                                                                            db.S_BO_FILE_MANAGER_UPDATE_COMMIT_ENABLED(String.valueOf(sCERTIFICATION_ID), loginUID);
                                                                                        }
                                                                                    }
                                                                                    //</editor-fold>
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
                                                                                        
                                                                                        if(pPKI_FORMFACTOR_ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PARTNER_HARD_TOKEN) {
                                                                                            GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
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
                                                                                                CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(sCERTIFICATION_ID), pSUBJECT, strEmailCust, sessLanguage);
                                                                                                strView = "0#0";
                                                                                            } else {
                                                                                                ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
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
                                                                String CheckREVOKE_ENABLED = "";
                                                                if(!"".equals(sVALUE_OLD)){
                                                                    ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                                    boolean sRevokedEnabled = valueATTR_Frist.getRevokeOldCertificateEnabled();
                                                                    if(sRevokedEnabled == true){CheckREVOKE_ENABLED = "1";} else {CheckREVOKE_ENABLED = "0";}
                                                                }
                                                                clsRSSP.approveCertificateForSignCloud(sAgreementUUID, String.valueOf(sCERTIFICATION_ID),
                                                                    CheckREVOKE_ENABLED, sRsspRelyingParty, sResultRSSP, pCERTIFICATION_SN, sChangeKeyApprove,
                                                                    sRequestTypeRssp, "", credentialAuthen, revokeSetOldStatus);
                                                                if("0".equals(sResultRSSP[0])) {
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
