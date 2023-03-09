/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.object.CERTIFICATION_OWNER;
import vn.ra.object.CERTIFICATION_OWNER_COMMENT;
import vn.ra.object.CERTIFICATION_OWNER_DATA_ATTR;
import vn.ra.object.RESPONSE_CODE;

/**
 *
 * @author USER
 */
public class OwnerCommon extends HttpServlet {
private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OwnerCommon.class);
    private static final long serialVersionUID = 6106269076155338045L;
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
            HttpSession sessionsa = request.getSession(false);
            ConnectDatabase com = new ConnectDatabase();
            String strView = "";
            try {
                int sOutInner;
                if (sessionsa != null) {
                    String strInnerUsername = sessionsa.getAttribute("sUserID").toString().trim();
                    String strInnerSessionKey = sessionsa.getAttribute("sesSessKey").toString().trim();
                    sOutInner = com.CheckIsLoginOnline(strInnerUsername, strInnerSessionKey);
                } else {
                    sOutInner = 2;
                }
                switch (sOutInner) {
                    case 1:
                        String idParam = request.getParameter("idParam");
                        String AGENT_ID_LOG = EscapeUtils.CheckTextNull(request.getSession(false).getAttribute("SessAgentID").toString().trim());
                        String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                        String SessRoleUserID = EscapeUtils.CheckTextNull(request.getSession(false).getAttribute("RoleID_ID").toString().trim());
                        String loginUID = EscapeUtils.CheckTextNull(request.getSession(false).getAttribute("sUserID").toString().trim());
                        String sessLanguage = EscapeUtils.CheckTextNull(request.getSession(false).getAttribute("sessVN").toString().trim());
                        String loginFullname = EscapeUtils.CheckTextNull(request.getSession(false).getAttribute("sesFullname").toString().trim());
                        if (null != idParam) switch (idParam) {
                            case "addowner":{
                                //<editor-fold defaultstate="collapsed" desc="addowner">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sVALID_CODE;
                                    String pPERSONAL_NAME = EscapeUtils.escapeHtml(request.getParameter("pPERSONAL_NAME"));
                                    String pCOMPANY_NAME = request.getParameter("pCOMPANY_NAME");
                                    String CBX_MST_MNS = request.getParameter("pCBX_MST_MNS");
                                    String INPUT_MST_MNS = request.getParameter("pINPUT_MST_MNS");
                                    String CBX_CMND_HC = request.getParameter("pCBX_CMND_HC");
                                    String INPUT_CMND_HC = request.getParameter("pINPUT_CMND_HC");
                                    String pPHONE_CONTRACT = request.getParameter("pPHONE_CONTRACT");
                                    String pEMAIL_CONTRACT = request.getParameter("pEMAIL_CONTRACT");
                                    String pADDRESS = request.getParameter("pADDRESS");
                                    String pREPRESENTATIVE = request.getParameter("pREPRESENTATIVE");
                                    String pREPRESENTATIVE_POSITION = request.getParameter("pREPRESENTATIVE_POSITION");
                                    String sEnterpriseID = "";
                                    String sPersonalID = "";
                                    String pCERTIFICATION_ATTR_TYPE_CODE = Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CODE_REGISTRATION_OWNER;
                                    if(!"".equals(INPUT_MST_MNS) && !"".equals(CBX_MST_MNS)) {
                                        sEnterpriseID = CBX_MST_MNS + INPUT_MST_MNS;
//                                        if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE.replace(":", "")))
//                                        {
//                                            sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE + INPUT_MST_MNS;
//                                        }
//                                        if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE.replace(":", "")))
//                                        {
//                                            sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE + INPUT_MST_MNS;
//                                        }
//                                        if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION.replace(":", "")))
//                                        {
//                                            sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION + INPUT_MST_MNS;
//                                        }
                                    }
                                    if(!"".equals(INPUT_CMND_HC) && !"".equals(CBX_CMND_HC)) {
                                        sPersonalID = CBX_CMND_HC + INPUT_CMND_HC;
//                                        if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND.replace(":", "")))
//                                        {
//                                            sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND + INPUT_CMND_HC;
//                                        }
//                                        if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT.replace(":", "")))
//                                        {
//                                            sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT + INPUT_CMND_HC;
//                                        }
//                                        if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID.replace(":", "")))
//                                        {
//                                            sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID + INPUT_CMND_HC;
//                                        }
                                    }
                                    String pCERTIFICATION_OWNER_TYPE_ID = "1";
                                    if(!"".equals(sEnterpriseID) && "".equals(sPersonalID)) {
                                        pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_ENTERPRISE);
                                    }
                                    if("".equals(sEnterpriseID) && !"".equals(sPersonalID)) {
                                        pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL);
                                    }
                                    if(!"".equals(sEnterpriseID) && !"".equals(sPersonalID)) {
                                        pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL);
                                    }
                                    ObjectMapper objectMapper;
                                    //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR ">
                                    CERTIFICATION_OWNER_DATA_ATTR tempLogReq = new CERTIFICATION_OWNER_DATA_ATTR();
                                    tempLogReq.personalName = pPERSONAL_NAME;
                                    tempLogReq.companyName = pCOMPANY_NAME;
                                    tempLogReq.enterpriseID = sEnterpriseID;
                                    tempLogReq.personalID = sPersonalID;
//                                    if(!"".equals(INPUT_MST_MNS)) {
//                                        if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE.replace(":", "")))
//                                        {
//                                            tempLogReq.taxCode = INPUT_MST_MNS;
//                                        }
//                                        if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE.replace(":", "")))
//                                        {
//                                            tempLogReq.budgetCode = INPUT_MST_MNS;
//                                        }
//                                        if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION.replace(":", "")))
//                                        {
//                                            tempLogReq.decision = INPUT_MST_MNS;
//                                        }
//                                    }
//                                    if(!"".equals(INPUT_CMND_HC)) {
//                                        if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND.replace(":", "")))
//                                        {
//                                            tempLogReq.personalCode = INPUT_CMND_HC;
//                                        }
//                                        if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT.replace(":", "")))
//                                        {
//                                            tempLogReq.passportCode = INPUT_CMND_HC;
//                                        }
//                                        if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID.replace(":", "")))
//                                        {
//                                            tempLogReq.citizenID = INPUT_CMND_HC;
//                                        }
//                                    }
                                    tempLogReq.emailContract = pEMAIL_CONTRACT;
                                    tempLogReq.phoneContract = pPHONE_CONTRACT;
                                    tempLogReq.address = pADDRESS;
                                    tempLogReq.representative = pREPRESENTATIVE;
                                    tempLogReq.representativePosition = pREPRESENTATIVE_POSITION;
                                    tempLogReq.typeName = pCERTIFICATION_ATTR_TYPE_CODE;
                                    tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING;
                                    tempLogReq.createUser = loginFullname + " (" + loginUID + ")";
                                    tempLogReq.createDt = new Date();
                                    //</editor-fold>
                                    
                                    objectMapper = new ObjectMapper();
                                    String pMESSAGING_QUEUE_FUNCTION_ID = String.valueOf(Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_REGISTRATION_OWNER);
                                    String[] pRESPONSE_CODE_NAME = new String[1];
                                    int[] pCERTIFICATION_OWNER_ID = new int[1];
                                    int[] pMESSAGING_QUEUE_ID = new int[1];
                                    String sOwnerUUID = CommonFunction.getUUIDV4();
                                    com.S_BO_CERTIFICATION_OWNER_INSERT(pPERSONAL_NAME, pCOMPANY_NAME,
                                        sEnterpriseID, sPersonalID, pCERTIFICATION_OWNER_TYPE_ID, pPHONE_CONTRACT,
                                        pEMAIL_CONTRACT, loginUID, pADDRESS, pREPRESENTATIVE, pREPRESENTATIVE_POSITION,
                                        pMESSAGING_QUEUE_FUNCTION_ID, objectMapper.writeValueAsString(tempLogReq), sOwnerUUID,
                                        pRESPONSE_CODE_NAME, pCERTIFICATION_OWNER_ID, pMESSAGING_QUEUE_ID);
                                    CommonFunction.LogDebugString(log, "CERTIFICATION_OWNER_INSERT Result", pRESPONSE_CODE_NAME[0]);
                                    if ("0".equals(pRESPONSE_CODE_NAME[0])) {
                                        if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            if (SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                || SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                            {
                                                tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
                                                tempLogReq.approveUser = loginFullname + " (" + loginUID + ")";
                                                tempLogReq.approveDt = new Date();
                                                tempLogReq.approveCAUser = loginFullname + " (" + loginUID + ")";
                                                tempLogReq.approveCADt = new Date();
                                                String sApprove = com.S_BO_API_CERTIFICATION_OWNER_APPROVED(pMESSAGING_QUEUE_ID[0], objectMapper.writeValueAsString(tempLogReq), loginUID);
                                                if ("0".equals(sApprove)) {
                                                    String sInsert = com.S_BO_API_CERTIFICATION_OWNER_REGISTRATION(pMESSAGING_QUEUE_ID[0], "", loginUID);
                                                    if("0".equals(sInsert)) {
                                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                    } else {
                                                        sVALID_CODE = sInsert;
                                                    }
                                                } else {
                                                    sVALID_CODE = sApprove;
                                                }
                                            } else {
                                                tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
                                                tempLogReq.approveUser = loginFullname + " (" + loginUID + ")";
                                                tempLogReq.approveDt = new Date();
                                                String sApprove = com.S_BO_API_CERTIFICATION_OWNER_PRE_APPROVED(pMESSAGING_QUEUE_ID[0], objectMapper.writeValueAsString(tempLogReq), loginUID);
                                                if ("0".equals(sApprove)) {
                                                    sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                } else {
                                                    sVALID_CODE = sApprove;
                                                }
                                            }
                                        } else {
                                            if (SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                || SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
                                                tempLogReq.approveUser = loginFullname + " (" + loginUID + ")";
                                                tempLogReq.approveDt = new Date();
                                                String sApprove = com.S_BO_API_CERTIFICATION_OWNER_PRE_APPROVED(pMESSAGING_QUEUE_ID[0], objectMapper.writeValueAsString(tempLogReq), loginUID);
                                                if ("0".equals(sApprove)) {
                                                    sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                } else {
                                                    sVALID_CODE = sApprove;
                                                }
                                            } else {
                                                sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                            }
                                        }
                                    } else {
                                        sVALID_CODE = pRESPONSE_CODE_NAME[0];
                                    }
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                    {
                                        strView = "0#0";
                                    } else {
                                        RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                                        com.S_BO_API_RESPONSE_CODE_GET_INFO(sVALID_CODE.trim(), Integer.parseInt(sessLanguage), rsResponseCode);
                                        if (rsResponseCode[0].length > 0) {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR+"#"+rsResponseCode[0][0].REMARK;
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "changeownerinfo":{
                                //<editor-fold defaultstate="collapsed" desc="changeownerinfo">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                    String sID = EscapeUtils.escapeHtml(request.getParameter("sID"));
                                    int sOwnerState;
                                    CERTIFICATION_OWNER[][] rs = new CERTIFICATION_OWNER[1][];
                                    com.S_BO_CERTIFICATION_OWNER_DETAIL(sID, sessLanguage, rs);
                                    if(rs[0].length > 0)
                                    {
                                        sOwnerState = rs[0][0].CERTIFICATION_OWNER_STATE_ID;
                                        if(sOwnerState != Definitions.CONFIG_CERTIFICATION_OWNER_STATE_ID_OPERATED)
                                        {
                                            sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_STATE_INVALID;
                                        }
                                    } else {
                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_NO_DATA;
                                    }
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                    {
                                        String pPERSONAL_NAME = EscapeUtils.escapeHtml(request.getParameter("pPERSONAL_NAME"));
                                        String pCOMPANY_NAME = request.getParameter("pCOMPANY_NAME");
                                        String CBX_MST_MNS = request.getParameter("pCBX_MST_MNS");
                                        String INPUT_MST_MNS = request.getParameter("pINPUT_MST_MNS");
                                        String CBX_CMND_HC = request.getParameter("pCBX_CMND_HC");
                                        String INPUT_CMND_HC = request.getParameter("pINPUT_CMND_HC");
                                        String pPHONE_CONTRACT = request.getParameter("pPHONE_CONTRACT");
                                        String pEMAIL_CONTRACT = request.getParameter("pEMAIL_CONTRACT");
                                        String pADDRESS = request.getParameter("pADDRESS");
                                        String pREPRESENTATIVE = request.getParameter("pREPRESENTATIVE");
                                        String pREPRESENTATIVE_POSITION = request.getParameter("pREPRESENTATIVE_POSITION");
                                        String sEnterpriseID = "";
                                        String sPersonalID = "";
                                        String pCERTIFICATION_ATTR_TYPE_CODE = Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CHANGE_OWNER_INFO;
                                        if(!"".equals(INPUT_MST_MNS) && !"".equals(CBX_MST_MNS)) {
                                            sEnterpriseID = CBX_MST_MNS + INPUT_MST_MNS;
//                                            if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE.replace(":", "")))
//                                            {
//                                                sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE + INPUT_MST_MNS;
//                                            }
//                                            if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE.replace(":", "")))
//                                            {
//                                                sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE + INPUT_MST_MNS;
//                                            }
//                                            if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION.replace(":", "")))
//                                            {
//                                                sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION + INPUT_MST_MNS;
//                                            }
                                        }
                                        if(!"".equals(INPUT_CMND_HC) && !"".equals(CBX_CMND_HC)) {
                                            sPersonalID = CBX_CMND_HC + INPUT_CMND_HC;
//                                            if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND.replace(":", "")))
//                                            {
//                                                sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND + INPUT_CMND_HC;
//                                            }
//                                            if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT.replace(":", "")))
//                                            {
//                                                sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT + INPUT_CMND_HC;
//                                            }
//                                            if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID.replace(":", "")))
//                                            {
//                                                sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID + INPUT_CMND_HC;
//                                            }
                                        }
                                        ObjectMapper objectMapper;
                                        //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR ">
                                        CERTIFICATION_OWNER_DATA_ATTR tempLogReq = new CERTIFICATION_OWNER_DATA_ATTR();
                                        tempLogReq.personalName = pPERSONAL_NAME;
                                        tempLogReq.companyName = pCOMPANY_NAME;
                                        tempLogReq.enterpriseID = sEnterpriseID;
                                        tempLogReq.personalID = sPersonalID;
//                                        if(!"".equals(INPUT_MST_MNS)) {
//                                            if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE.replace(":", "")))
//                                            {
//                                                tempLogReq.taxCode = INPUT_MST_MNS;
//                                            }
//                                            if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE.replace(":", "")))
//                                            {
//                                                tempLogReq.budgetCode = INPUT_MST_MNS;
//                                            }
//                                            if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION.replace(":", "")))
//                                            {
//                                                tempLogReq.decision = INPUT_MST_MNS;
//                                            }
//                                        }
//                                        if(!"".equals(INPUT_CMND_HC)) {
//                                            if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND.replace(":", "")))
//                                            {
//                                                tempLogReq.personalCode = INPUT_CMND_HC;
//                                            }
//                                            if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT.replace(":", "")))
//                                            {
//                                                tempLogReq.passportCode = INPUT_CMND_HC;
//                                            }
//                                            if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID.replace(":", "")))
//                                            {
//                                                tempLogReq.citizenID = INPUT_CMND_HC;
//                                            }
//                                        }
                                        tempLogReq.emailContract = pEMAIL_CONTRACT;
                                        tempLogReq.phoneContract = pPHONE_CONTRACT;
                                        tempLogReq.address = pADDRESS;
                                        tempLogReq.representative = pREPRESENTATIVE;
                                        tempLogReq.representativePosition = pREPRESENTATIVE_POSITION;
                                        tempLogReq.typeName = pCERTIFICATION_ATTR_TYPE_CODE;
                                        tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING;
                                        tempLogReq.createUser = loginFullname + " (" + loginUID + ")";
                                        tempLogReq.createDt = new Date();
                                        //</editor-fold>

                                        objectMapper = new ObjectMapper();
                                        String[] pRESPONSE_CODE_NAME = new String[1];
                                        int[] pMESSAGING_QUEUE_ID = new int[1];
                                        int pMESSAGING_QUEUE_STATE_ID = Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_PENDING;
                                        int pMESSAGING_QUEUE_FUNCTION_ID = Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_CHANGE_OWNER_INFO;
                                        com.S_BO_CERTIFICATION_OWNER_INSERT_MESSAGING_QUEUE(Integer.parseInt(sID), pMESSAGING_QUEUE_STATE_ID,
                                            pMESSAGING_QUEUE_FUNCTION_ID, objectMapper.writeValueAsString(tempLogReq),"", loginUID,
                                            pRESPONSE_CODE_NAME, pMESSAGING_QUEUE_ID);
                                        CommonFunction.LogDebugString(log, "MESSAGING_QUEUE INSERT Result", pRESPONSE_CODE_NAME[0]);
                                        if ("0".equals(pRESPONSE_CODE_NAME[0])) {
                                            request.getSession(false).setAttribute("RefreshOwnerListSess", "1");
                                            if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                if (SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                    || SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                    || SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                {
                                                    tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING;
                                                    tempLogReq.approveUser = loginFullname + " (" + loginUID + ")";
                                                    tempLogReq.approveDt = new Date();
                                                    tempLogReq.approveCAUser = loginFullname + " (" + loginUID + ")";
                                                    tempLogReq.approveCADt = new Date();
                                                    String sApprove = com.S_BO_API_CERTIFICATION_OWNER_APPROVED(pMESSAGING_QUEUE_ID[0], objectMapper.writeValueAsString(tempLogReq), loginUID);
                                                    if ("0".equals(sApprove)) {
                                                        String sUpdate = com.S_BO_API_CERTIFICATION_OWNER_CHANGE_INFO(pMESSAGING_QUEUE_ID[0], pADDRESS, pPERSONAL_NAME,
                                                            pCOMPANY_NAME, pPHONE_CONTRACT, pEMAIL_CONTRACT, "", pREPRESENTATIVE,
                                                            pREPRESENTATIVE_POSITION, loginUID);
                                                        if("0".equals(sUpdate)) {
                                                            sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                        } else {
                                                            sVALID_CODE = sUpdate;
                                                        }
                                                    } else {
                                                        sVALID_CODE = sApprove;
                                                    }
                                                } else {
                                                    tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
                                                    tempLogReq.approveUser = loginFullname + " (" + loginUID + ")";
                                                    tempLogReq.approveDt = new Date();
                                                    String sApprove = com.S_BO_API_CERTIFICATION_OWNER_PRE_APPROVED(pMESSAGING_QUEUE_ID[0], objectMapper.writeValueAsString(tempLogReq), loginUID);
                                                    if ("0".equals(sApprove)) {
                                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                    } else {
                                                        sVALID_CODE = sApprove;
                                                    }
                                                }
                                            } else {
                                                if (SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                    || SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                    tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
                                                    tempLogReq.approveUser = loginFullname + " (" + loginUID + ")";
                                                    tempLogReq.approveDt = new Date();
                                                    String sApprove = com.S_BO_API_CERTIFICATION_OWNER_PRE_APPROVED(pMESSAGING_QUEUE_ID[0], objectMapper.writeValueAsString(tempLogReq), loginUID);
                                                    if ("0".equals(sApprove)) {
                                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                    } else {
                                                        sVALID_CODE = sApprove;
                                                    }
                                                } else {
                                                    sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                }
                                            }
                                        } else {
                                            sVALID_CODE = pRESPONSE_CODE_NAME[0];
                                        }
                                    }
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                    {
                                        strView = "0#0";
                                    } else {
                                        RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                                        com.S_BO_API_RESPONSE_CODE_GET_INFO(sVALID_CODE.trim(), Integer.parseInt(sessLanguage), rsResponseCode);
                                        if (rsResponseCode[0].length > 0) {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR+"#"+rsResponseCode[0][0].REMARK;
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "disposeowner":{
                                //<editor-fold defaultstate="collapsed" desc="disposeowner">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                    String sID = EscapeUtils.escapeHtml(request.getParameter("sID"));
                                    int sOwnerState;
                                    String sCOMMENT_OLD = "";
                                    CERTIFICATION_OWNER[][] rs = new CERTIFICATION_OWNER[1][];
                                    com.S_BO_CERTIFICATION_OWNER_DETAIL(sID, sessLanguage, rs);
                                    if(rs[0].length > 0)
                                    {
                                        sOwnerState = rs[0][0].CERTIFICATION_OWNER_STATE_ID;
                                        sCOMMENT_OLD = rs[0][0].COMMENT;
                                        if(sOwnerState != Definitions.CONFIG_CERTIFICATION_OWNER_STATE_ID_OPERATED)
                                        {
                                            sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_STATE_INVALID;
                                        }
                                    } else {
                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_NO_DATA;
                                    }
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                    {
                                        String pDISPOSE_REASON = EscapeUtils.escapeHtml(request.getParameter("REVOKE_REASON"));
                                        String pCERTIFICATION_ATTR_TYPE_CODE = Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CODE_DISPOSE_OWNER;
                                        ObjectMapper objectMapper;
                                        //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR ">
                                        CERTIFICATION_OWNER_DATA_ATTR tempLogReq = new CERTIFICATION_OWNER_DATA_ATTR();
                                        tempLogReq.ownerUUID = sID;
                                        tempLogReq.ownerDisposeReason = pDISPOSE_REASON;
                                        tempLogReq.typeName = pCERTIFICATION_ATTR_TYPE_CODE;
                                        tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING;
                                        tempLogReq.createUser = loginFullname + " (" + loginUID + ")";
                                        tempLogReq.createDt = new Date();
                                        //</editor-fold>

                                        objectMapper = new ObjectMapper();
                                        String[] pRESPONSE_CODE_NAME = new String[1];
                                        int[] pMESSAGING_QUEUE_ID = new int[1];
                                        int pMESSAGING_QUEUE_STATE_ID = Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_PENDING;
                                        int pMESSAGING_QUEUE_FUNCTION_ID = Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_DISPOSE_OWNER;
                                        com.S_BO_CERTIFICATION_OWNER_INSERT_MESSAGING_QUEUE(Integer.parseInt(sID), pMESSAGING_QUEUE_STATE_ID,
                                            pMESSAGING_QUEUE_FUNCTION_ID, objectMapper.writeValueAsString(tempLogReq),"", loginUID,
                                            pRESPONSE_CODE_NAME, pMESSAGING_QUEUE_ID);
                                        CommonFunction.LogDebugString(log, "MESSAGING_QUEUE INSERT Result", pRESPONSE_CODE_NAME[0]);
                                        if ("0".equals(pRESPONSE_CODE_NAME[0])) {
                                            request.getSession(false).setAttribute("RefreshOwnerListSess", "1");
                                            if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                if (SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                    || SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                    || SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                {
                                                    tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_APPROVED;
                                                    tempLogReq.approveUser = loginFullname + " (" + loginUID + ")";
                                                    tempLogReq.approveDt = new Date();
                                                    tempLogReq.approveCAUser = loginFullname + " (" + loginUID + ")";
                                                    tempLogReq.approveCADt = new Date();
                                                    String sApprove = com.S_BO_API_CERTIFICATION_OWNER_APPROVED(pMESSAGING_QUEUE_ID[0], objectMapper.writeValueAsString(tempLogReq), loginUID);
                                                    if ("0".equals(sApprove)) {
                                                        String sOwnerApproveRemark = "";
                                                        if(!"".equals(sCOMMENT_OLD))
                                                        {
                                                            try {
                                                                CERTIFICATION_OWNER_COMMENT jsonCertCommentOld = objectMapper.readValue(sCOMMENT_OLD, CERTIFICATION_OWNER_COMMENT.class);
                                                                sOwnerApproveRemark = jsonCertCommentOld.ownerApproveRemark;
                                                            } catch(IOException e) {}
                                                        }
                                                        CERTIFICATION_OWNER_COMMENT jsonCertComment = new CERTIFICATION_OWNER_COMMENT();
                                                        jsonCertComment.ownerApproveRemark = sOwnerApproveRemark;
                                                        jsonCertComment.ownerDisposeReason = pDISPOSE_REASON;
                                                        String sUpdate = com.S_BO_API_CERTIFICATION_OWNER_DISPOSE(pMESSAGING_QUEUE_ID[0], objectMapper.writeValueAsString(jsonCertComment), loginUID);
                                                        if("0".equals(sUpdate)) {
                                                            sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                        } else {
                                                            sVALID_CODE = sUpdate;
                                                        }
                                                    } else {
                                                        sVALID_CODE = sApprove;
                                                    }
                                                } else {
                                                    tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
                                                    tempLogReq.approveUser = loginFullname + " (" + loginUID + ")";
                                                    tempLogReq.approveDt = new Date();
                                                    String sApprove = com.S_BO_API_CERTIFICATION_OWNER_PRE_APPROVED(pMESSAGING_QUEUE_ID[0], objectMapper.writeValueAsString(tempLogReq), loginUID);
                                                    if ("0".equals(sApprove)) {
                                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                    } else {
                                                        sVALID_CODE = sApprove;
                                                    }
                                                }
                                            } else {
                                                if (SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                    || SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)) {
                                                    tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
                                                    tempLogReq.approveUser = loginFullname + " (" + loginUID + ")";
                                                    tempLogReq.approveDt = new Date();
                                                    String sApprove = com.S_BO_API_CERTIFICATION_OWNER_PRE_APPROVED(pMESSAGING_QUEUE_ID[0], objectMapper.writeValueAsString(tempLogReq), loginUID);
                                                    if ("0".equals(sApprove)) {
                                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                    } else {
                                                        sVALID_CODE = sApprove;
                                                    }
                                                } else {
                                                    sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                }
                                            }
                                        } else {
                                            sVALID_CODE = pRESPONSE_CODE_NAME[0];
                                        }
                                    }
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                    {
                                        strView = "0#0";
                                    } else {
                                        RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                                        com.S_BO_API_RESPONSE_CODE_GET_INFO(sVALID_CODE.trim(), Integer.parseInt(sessLanguage), rsResponseCode);
                                        if (rsResponseCode[0].length > 0) {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR+"#"+rsResponseCode[0][0].REMARK;
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "approveagencyowner":{
                                //<editor-fold defaultstate="collapsed" desc="approveagencyowner">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                    String sID = EscapeUtils.escapeHtml(request.getParameter("sID"));
                                    ObjectMapper objectMapper;
                                    if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        if (!SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                            && !SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                        {
                                            sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_ROLE_DENIED;
                                        }
                                    } else {
                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_ROLE_DENIED;
                                    }
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                    {
                                        int sQueueState = 0;
                                        String sValue = "";
                                        int sQueueID = 0;
                                        int sOwnerID = 0;
                                        int sQueueType = 0;
                                        CERTIFICATION_OWNER[][] rs = new CERTIFICATION_OWNER[1][];
                                        com.S_BO_MESSAGING_QUEUE_APPROVED_DETAIL(sID, sessLanguage, rs);
                                        if(rs[0].length > 0)
                                        {
                                            sOwnerID = rs[0][0].ID;
                                            sQueueID = rs[0][0].MESSAGING_QUEUE_ID;
                                            sQueueState = rs[0][0].MESSAGING_QUEUE_STATE_ID;
                                            sQueueType = rs[0][0].MESSAGING_QUEUE_FUNCTION_ID;
                                            sValue = rs[0][0].VALUE;
                                        }
                                        if(sQueueID == 0 || sQueueState == 0)
                                        {
                                            sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_NO_DATA;
                                        }
                                        if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                        {
                                            if(sQueueState != Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_INITIALIZED
                                                && sQueueState != Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_PENDING)
                                            {
                                                sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_STATE_INVALID;
                                            }
                                        }
                                        if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                        {
                                            String sUserCreate = "";
                                            Date sDateCreate = null;
                                            String sOwnerType = "";
                                            if(!"".equals(sValue))
                                            {
                                                objectMapper = new ObjectMapper();
                                                CERTIFICATION_OWNER_DATA_ATTR valueATTR_Frist = objectMapper.readValue(sValue, CERTIFICATION_OWNER_DATA_ATTR.class);
                                                sUserCreate = EscapeUtils.CheckTextNull(valueATTR_Frist.createUser);
                                                sDateCreate = valueATTR_Frist.createDt;
                                                sOwnerType = valueATTR_Frist.typeName;
                                            }
                                            String pPERSONAL_NAME = EscapeUtils.escapeHtml(request.getParameter("pPERSONAL_NAME"));
                                            String pCOMPANY_NAME = request.getParameter("pCOMPANY_NAME");
                                            String CBX_MST_MNS = request.getParameter("pCBX_MST_MNS");
                                            String INPUT_MST_MNS = request.getParameter("pINPUT_MST_MNS");
                                            String CBX_CMND_HC = request.getParameter("pCBX_CMND_HC");
                                            String INPUT_CMND_HC = request.getParameter("pINPUT_CMND_HC");
                                            String pPHONE_CONTRACT = request.getParameter("pPHONE_CONTRACT");
                                            String pEMAIL_CONTRACT = request.getParameter("pEMAIL_CONTRACT");
                                            String pADDRESS = request.getParameter("pADDRESS");
                                            String pREPRESENTATIVE = request.getParameter("pREPRESENTATIVE");
                                            String pREPRESENTATIVE_POSITION = request.getParameter("pREPRESENTATIVE_POSITION");
                                            String sEnterpriseID = "";
                                            String sPersonalID = "";
                                            if(!"".equals(INPUT_MST_MNS) && !"".equals(CBX_MST_MNS)) {
                                                sEnterpriseID = CBX_MST_MNS + INPUT_MST_MNS;
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE.replace(":", "")))
//                                                {
//                                                    sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE + INPUT_MST_MNS;
//                                                }
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE.replace(":", "")))
//                                                {
//                                                    sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE + INPUT_MST_MNS;
//                                                }
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION.replace(":", "")))
//                                                {
//                                                    sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION + INPUT_MST_MNS;
//                                                }
                                            }
                                            if(!"".equals(INPUT_CMND_HC) && !"".equals(CBX_CMND_HC)) {
                                                sPersonalID = CBX_CMND_HC + INPUT_CMND_HC;
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND.replace(":", "")))
//                                                {
//                                                    sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND + INPUT_CMND_HC;
//                                                }
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT.replace(":", "")))
//                                                {
//                                                    sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT + INPUT_CMND_HC;
//                                                }
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID.replace(":", "")))
//                                                {
//                                                    sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID + INPUT_CMND_HC;
//                                                }
                                            }
                                            objectMapper = new ObjectMapper();
                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR ">
                                            CERTIFICATION_OWNER_DATA_ATTR tempLogReq = new CERTIFICATION_OWNER_DATA_ATTR();
                                            tempLogReq.personalName = pPERSONAL_NAME;
                                            tempLogReq.companyName = pCOMPANY_NAME;
                                            tempLogReq.enterpriseID = sEnterpriseID;
                                            tempLogReq.personalID = sPersonalID;
//                                            if(!"".equals(INPUT_MST_MNS)) {
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE.replace(":", "")))
//                                                {
//                                                    tempLogReq.taxCode = INPUT_MST_MNS;
//                                                }
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE.replace(":", "")))
//                                                {
//                                                    tempLogReq.budgetCode = INPUT_MST_MNS;
//                                                }
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION.replace(":", "")))
//                                                {
//                                                    tempLogReq.decision = INPUT_MST_MNS;
//                                                }
//                                            }
//                                            if(!"".equals(INPUT_CMND_HC)) {
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND.replace(":", "")))
//                                                {
//                                                    tempLogReq.personalCode = INPUT_CMND_HC;
//                                                }
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT.replace(":", "")))
//                                                {
//                                                    tempLogReq.passportCode = INPUT_CMND_HC;
//                                                }
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID.replace(":", "")))
//                                                {
//                                                    tempLogReq.citizenID = INPUT_CMND_HC;
//                                                }
//                                            }
                                            tempLogReq.emailContract = pEMAIL_CONTRACT;
                                            tempLogReq.phoneContract = pPHONE_CONTRACT;
                                            tempLogReq.address = pADDRESS;
                                            tempLogReq.representative = pREPRESENTATIVE;
                                            tempLogReq.representativePosition = pREPRESENTATIVE_POSITION;
                                            if(sQueueType == Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_DISPOSE_OWNER) {
                                                String REVOKE_REASON = EscapeUtils.escapeHtml(request.getParameter("REVOKE_REASON"));
                                                tempLogReq.ownerDisposeReason = REVOKE_REASON;
                                            }
                                            tempLogReq.typeName = sOwnerType;
                                            tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING;
                                            tempLogReq.createUser = sUserCreate;
                                            tempLogReq.createDt = sDateCreate;
                                            //</editor-fold>
                                            
                                            String pCERTIFICATION_OWNER_TYPE_ID = "1";
                                            if(!"".equals(sEnterpriseID) && "".equals(sPersonalID)) {
                                                pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_ENTERPRISE);
                                            }
                                            if("".equals(sEnterpriseID) && !"".equals(sPersonalID)) {
                                                pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL);
                                            }
                                            if(!"".equals(sEnterpriseID) && !"".equals(sPersonalID)) {
                                                pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_STAFF);
                                            }

                                            tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
                                            tempLogReq.approveUser = loginFullname + " (" + loginUID + ")";
                                            tempLogReq.approveDt = new Date();
                                            String sApprove = com.S_BO_API_CERTIFICATION_OWNER_PRE_APPROVED(sQueueID, objectMapper.writeValueAsString(tempLogReq), loginUID);
                                            if ("0".equals(sApprove)) {
                                                if(sQueueType == Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_REGISTRATION_OWNER) {
                                                    String sUpdate = com.S_BO_CERTIFICATION_UPDATE_OWNER(sOwnerID, pADDRESS, pPERSONAL_NAME, pCOMPANY_NAME,
                                                        sEnterpriseID, sPersonalID, pCERTIFICATION_OWNER_TYPE_ID, pPHONE_CONTRACT,
                                                        pEMAIL_CONTRACT, pREPRESENTATIVE, pREPRESENTATIVE_POSITION, loginUID);
                                                    if("".equals(sUpdate)) {
                                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                    } else {
                                                        sVALID_CODE = sUpdate;
                                                    }
                                                } else {
                                                    sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                }
                                            } else {
                                                sVALID_CODE = sApprove;
                                            }
                                        }
                                    }
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                        strView = "0#0";
                                    } else {
                                        RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                                        com.S_BO_API_RESPONSE_CODE_GET_INFO(sVALID_CODE.trim(), Integer.parseInt(sessLanguage), rsResponseCode);
                                        if (rsResponseCode[0].length > 0) {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR+"#"+rsResponseCode[0][0].REMARK;
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "approveacaowner":{
                                //<editor-fold defaultstate="collapsed" desc="approveacaowner">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                    String sID = EscapeUtils.escapeHtml(request.getParameter("sID"));
                                    ObjectMapper objectMapper;
                                    if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        if (!SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) && SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                            && SessRoleUserID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                        {
                                            sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_ROLE_DENIED;
                                        }
                                    }
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                    {
                                        int sQueueState = 0;
                                        String sValue = "";
                                        int sQueueID = 0;
                                        int sQueueType = 0;
                                        int sOwnerID = 0;
                                        CERTIFICATION_OWNER[][] rs = new CERTIFICATION_OWNER[1][];
                                        com.S_BO_MESSAGING_QUEUE_APPROVED_DETAIL(sID, sessLanguage, rs);
                                        if(rs[0].length > 0)
                                        {
                                            sOwnerID = rs[0][0].ID;
                                            sQueueID = rs[0][0].MESSAGING_QUEUE_ID;
                                            sQueueState = rs[0][0].MESSAGING_QUEUE_STATE_ID;
                                            sQueueType = rs[0][0].MESSAGING_QUEUE_FUNCTION_ID;
                                            sValue = rs[0][0].VALUE;
                                            if(sQueueState != Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_INITIALIZED
                                                && sQueueState != Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_PENDING
                                                && sQueueState != Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_PRE_APPROVED)
                                            {
                                                sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_STATE_INVALID;
                                            }
                                        } else {
                                            sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_NO_DATA;
                                        }
                                        if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                        {
                                            String sUserCreate = "";
                                            Date sDateCreate = null;
                                            String sUserApprove = "";
                                            Date sDateApprove = null;
                                            String sOwnerType = "";
                                            if(!"".equals(sValue))
                                            {
                                                objectMapper = new ObjectMapper();
                                                CERTIFICATION_OWNER_DATA_ATTR valueATTR_Frist = objectMapper.readValue(sValue, CERTIFICATION_OWNER_DATA_ATTR.class);
                                                sUserCreate = EscapeUtils.CheckTextNull(valueATTR_Frist.createUser);
                                                sDateCreate = valueATTR_Frist.createDt;
                                                sUserApprove = EscapeUtils.CheckTextNull(valueATTR_Frist.approveUser);
                                                sDateApprove = valueATTR_Frist.approveDt;
                                                sOwnerType = valueATTR_Frist.typeName;
                                            }
                                            String pPERSONAL_NAME = EscapeUtils.escapeHtml(request.getParameter("pPERSONAL_NAME"));
                                            String pCOMPANY_NAME = request.getParameter("pCOMPANY_NAME");
                                            String CBX_MST_MNS = request.getParameter("pCBX_MST_MNS");
                                            String INPUT_MST_MNS = request.getParameter("pINPUT_MST_MNS");
                                            String CBX_CMND_HC = request.getParameter("pCBX_CMND_HC");
                                            String INPUT_CMND_HC = request.getParameter("pINPUT_CMND_HC");
                                            String pPHONE_CONTRACT = request.getParameter("pPHONE_CONTRACT");
                                            String pEMAIL_CONTRACT = request.getParameter("pEMAIL_CONTRACT");
                                            String pADDRESS = request.getParameter("pADDRESS");
                                            String pREPRESENTATIVE = request.getParameter("pREPRESENTATIVE");
                                            String pREPRESENTATIVE_POSITION = request.getParameter("pREPRESENTATIVE_POSITION");
                                            String sEnterpriseID = "";
                                            String sPersonalID = "";
                                            if(!"".equals(INPUT_MST_MNS) && !"".equals(CBX_MST_MNS)) {
                                                sEnterpriseID = CBX_MST_MNS + INPUT_MST_MNS;
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE.replace(":", "")))
//                                                {
//                                                    sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE + INPUT_MST_MNS;
//                                                }
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE.replace(":", "")))
//                                                {
//                                                    sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE + INPUT_MST_MNS;
//                                                }
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION.replace(":", "")))
//                                                {
//                                                    sEnterpriseID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION + INPUT_MST_MNS;
//                                                }
                                            }
                                            if(!"".equals(INPUT_CMND_HC) && !"".equals(CBX_CMND_HC)) {
                                                sPersonalID = CBX_CMND_HC+INPUT_CMND_HC;
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND.replace(":", "")))
//                                                {
//                                                    sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND + INPUT_CMND_HC;
//                                                }
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT.replace(":", "")))
//                                                {
//                                                    sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT + INPUT_CMND_HC;
//                                                }
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID.replace(":", "")))
//                                                {
//                                                    sPersonalID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID + INPUT_CMND_HC;
//                                                }
                                            }
                                            String pCERTIFICATION_OWNER_TYPE_ID = "1";
                                            if(!"".equals(sEnterpriseID) && "".equals(sPersonalID)) {
                                                pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_ENTERPRISE);
                                            }
                                            if("".equals(sEnterpriseID) && !"".equals(sPersonalID)) {
                                                pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL);
                                            }
                                            if(!"".equals(sEnterpriseID) && !"".equals(sPersonalID)) {
                                                pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_STAFF);
                                            }
                                            objectMapper = new ObjectMapper();
                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR ">
                                            CERTIFICATION_OWNER_DATA_ATTR tempLogReq = new CERTIFICATION_OWNER_DATA_ATTR();
                                            tempLogReq.personalName = pPERSONAL_NAME;
                                            tempLogReq.companyName = pCOMPANY_NAME;
                                            tempLogReq.enterpriseID = sEnterpriseID;
                                            tempLogReq.personalID = sPersonalID;
//                                            if(!"".equals(INPUT_MST_MNS)) {
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE.replace(":", "")))
//                                                {
//                                                    tempLogReq.taxCode = INPUT_MST_MNS;
//                                                }
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE.replace(":", "")))
//                                                {
//                                                    tempLogReq.budgetCode = INPUT_MST_MNS;
//                                                }
//                                                if(CBX_MST_MNS.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION.replace(":", "")))
//                                                {
//                                                    tempLogReq.decision = INPUT_MST_MNS;
//                                                }
//                                            }
//                                            if(!"".equals(INPUT_CMND_HC)) {
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND.replace(":", "")))
//                                                {
//                                                    tempLogReq.personalCode = INPUT_CMND_HC;
//                                                }
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT.replace(":", "")))
//                                                {
//                                                    tempLogReq.passportCode = INPUT_CMND_HC;
//                                                }
//                                                if(CBX_CMND_HC.equals(Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID.replace(":", "")))
//                                                {
//                                                    tempLogReq.citizenID = INPUT_CMND_HC;
//                                                }
//                                            }
                                            tempLogReq.emailContract = pEMAIL_CONTRACT;
                                            tempLogReq.phoneContract = pPHONE_CONTRACT;
                                            tempLogReq.address = pADDRESS;
                                            tempLogReq.representative = pREPRESENTATIVE;
                                            tempLogReq.representativePosition = pREPRESENTATIVE_POSITION;
                                            tempLogReq.typeName = sOwnerType;
                                            tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING;
                                            tempLogReq.createUser = sUserCreate;
                                            tempLogReq.createDt = sDateCreate;
                                            //</editor-fold>

                                            if(sQueueState == Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_INITIALIZED
                                                || sQueueState == Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_PENDING)
                                            {
                                                tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
                                                tempLogReq.approveUser = loginFullname + " (" + loginUID + ")";
                                                tempLogReq.approveDt = new Date();
                                                com.S_BO_API_CERTIFICATION_OWNER_PRE_APPROVED(sQueueID, objectMapper.writeValueAsString(tempLogReq), loginUID);
                                            } else {
                                                tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PRE_APPROVED;
                                                tempLogReq.approveUser = sUserApprove;
                                                tempLogReq.approveDt = sDateApprove;
                                            }
                                            tempLogReq.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_APPROVED;
                                            tempLogReq.approveCAUser = loginFullname + " (" + loginUID + ")";
                                            tempLogReq.approveCADt = new Date();
                                            String sApprove = com.S_BO_API_CERTIFICATION_OWNER_APPROVED(sQueueID, objectMapper.writeValueAsString(tempLogReq), loginUID);
                                            if ("0".equals(sApprove)) {
                                                if(sQueueType == Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_REGISTRATION_OWNER)
                                                {
                                                    com.S_BO_API_CERTIFICATION_OWNER_REGISTRATION(sQueueID, "", loginUID);
                                                    com.S_BO_CERTIFICATION_UPDATE_OWNER(sOwnerID, pADDRESS, pPERSONAL_NAME, pCOMPANY_NAME,
                                                        sEnterpriseID, sPersonalID, pCERTIFICATION_OWNER_TYPE_ID, pPHONE_CONTRACT,
                                                        pEMAIL_CONTRACT, pREPRESENTATIVE, pREPRESENTATIVE_POSITION, loginUID);
                                                }
                                                if(sQueueType == Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_CHANGE_OWNER_INFO)
                                                {
                                                    com.S_BO_API_CERTIFICATION_OWNER_CHANGE_INFO(sQueueID, pADDRESS, pPERSONAL_NAME,
                                                        pCOMPANY_NAME, pPHONE_CONTRACT, pEMAIL_CONTRACT, "", pREPRESENTATIVE,
                                                        pREPRESENTATIVE_POSITION, loginUID);
                                                }
                                                if(sQueueType == Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_DISPOSE_OWNER)
                                                {
                                                    objectMapper = new ObjectMapper();
                                                    String REVOKE_REASON = request.getParameter("REVOKE_REASON");
                                                    CERTIFICATION_OWNER_COMMENT jsonCertComment = new CERTIFICATION_OWNER_COMMENT();
                                                    jsonCertComment.ownerDisposeReason = EscapeUtils.CheckTextNull(REVOKE_REASON);
                                                    com.S_BO_API_CERTIFICATION_OWNER_DISPOSE(sQueueID, objectMapper.writeValueAsString(jsonCertComment), loginUID);
                                                }
                                                sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                            } else {
                                                sVALID_CODE = sApprove;
                                            }
                                        }
                                    }
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                        strView = "0#0";
                                    } else {
                                        RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                                        com.S_BO_API_RESPONSE_CODE_GET_INFO(sVALID_CODE.trim(), Integer.parseInt(sessLanguage), rsResponseCode);
                                        if (rsResponseCode[0].length > 0) {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR+"#"+rsResponseCode[0][0].REMARK;
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "declineowner":{
                                //<editor-fold defaultstate="collapsed" desc="declineowner">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                    String sID = EscapeUtils.escapeHtml(request.getParameter("sID"));
                                    String sDeclineReason = EscapeUtils.escapeHtml(request.getParameter("sDeclineReason"));
                                    ObjectMapper objectMapper;
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                    {
                                        int sQueueState;
                                        int sQueueID = 0;
                                        String sCOMMENT_OLD = "";
                                        CERTIFICATION_OWNER[][] rs = new CERTIFICATION_OWNER[1][];
                                        com.S_BO_MESSAGING_QUEUE_APPROVED_DETAIL(sID, sessLanguage, rs);
                                        if(rs[0].length > 0)
                                        {
                                            sQueueID = rs[0][0].MESSAGING_QUEUE_ID;
                                            sQueueState = rs[0][0].MESSAGING_QUEUE_STATE_ID;
                                            sCOMMENT_OLD = rs[0][0].COMMENT;
                                            if(sQueueState == Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_COMMITED
                                                || sQueueState == Definitions.CONFIG_MESSAGING_QUEUE_STATE_ID_DECLINED)
                                            {
                                                sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_STATE_INVALID;
                                            }
                                        } else {
                                            sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_NO_DATA;
                                        }
                                        if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                        {
                                            objectMapper = new ObjectMapper();
                                            String sOwnerDisposeReasonOld = "";
                                            String sOwnerApproveRemarkOld = "";
                                            if(!"".equals(sCOMMENT_OLD))
                                            {
                                                try {
                                                    CERTIFICATION_OWNER_COMMENT jsonCertCommentOld = objectMapper.readValue(sCOMMENT_OLD, CERTIFICATION_OWNER_COMMENT.class);
                                                    sOwnerDisposeReasonOld = jsonCertCommentOld.ownerDisposeReason;
                                                    sOwnerApproveRemarkOld = jsonCertCommentOld.ownerApproveRemark;
                                                } catch(IOException e) {}
                                            }
                                            CERTIFICATION_OWNER_COMMENT jsonCertComment = new CERTIFICATION_OWNER_COMMENT();
                                            jsonCertComment.ownerDisposeReason = sOwnerDisposeReasonOld;
                                            jsonCertComment.ownerApproveRemark = sOwnerApproveRemarkOld;
                                            jsonCertComment.ownerDeclineReason = sDeclineReason;
                                            String sParam = com.S_BO_API_CERTIFICATION_OWNER_DECLINED(sQueueID, objectMapper.writeValueAsString(jsonCertComment),loginUID);
                                            if ("0".equals(sParam)) {
                                                sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                            } else {
                                                sVALID_CODE = sParam;
                                            }
                                        }
                                    }
                                    if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                        strView = "0#0";
                                    } else {
                                        RESPONSE_CODE[][] rsResponseCode = new RESPONSE_CODE[1][];
                                        com.S_BO_API_RESPONSE_CODE_GET_INFO(sVALID_CODE.trim(), Integer.parseInt(sessLanguage), rsResponseCode);
                                        if (rsResponseCode[0].length > 0) {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_DB_ERROR+"#"+rsResponseCode[0][0].REMARK;
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                        }   break;
                    case 2:
                        strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                        break;
                    default:
                        strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "#0";
                        break;
                }
            } catch (NumberFormatException | SQLException | UnsupportedEncodingException e) {
                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#" + e.getMessage();
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
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
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
            CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
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
