/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.object.ATTRIBUTE_DATA;
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.BRANCH;
import vn.ra.object.MENULINK_TOKEN;
import vn.ra.object.PUSH_TOKEN;
import vn.ra.object.RESPONSE_CODE;
import vn.ra.object.RESPONSE_LOG;
import vn.ra.object.ROLE_DATA;
import vn.ra.object.TOKEN;
import vn.ra.object.TOKEN_CHANGE_LOG;
import vn.ra.object.TOKEN_IMPORT;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.EncodeSOPIN;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class TokenCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TokenCommon.class.getName());

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
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            ConnectDatabase db = new ConnectDatabase();
            HttpSession sessionsa = request.getSession(false);
            String strView = "";
            
            String idParam = request.getParameter("idParam");
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
                    String sUID_Update = sessionsa.getAttribute("sUserID").toString().trim();
                    String loginFullname = request.getSession(false).getAttribute("sesFullname").toString().trim();
                    String sessLanguageGlobal = sessionsa.getAttribute("sessVN").toString();
                    String sIP_Request = CommonFunction.getClientIpLogin(request);
                    String[] sysLog_BillCode = new String[1];
                    if (null != idParam) {
                        switch (idParam) {
                            case "edittoken": {
                                //<editor-fold defaultstate="collapsed" desc="edittoken">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("ID");
                                    String sTokenSN = "";
                                    boolean isAccessAgency = true;
                                    int sTOKEN_STATE_ID = 0;
                                    TOKEN[][] rsReq = new TOKEN[1][];
                                    db.S_BO_TOKEN_DETAIL(sID, rsReq);
                                    if (rsReq[0].length > 0) {
                                        sTokenSN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        sTOKEN_STATE_ID = rsReq[0][0].TOKEN_STATE_ID;
                                        String sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
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
                                        String LINK_NOTICE = request.getParameter("LINK_NOTICE");
                                        String COLOR_TEXT = request.getParameter("COLOR_TEXT");
                                        String COLOR_BKGR = request.getParameter("COLOR_BKGR");
                                        String NOTICE_INFO = request.getParameter("NOTICE_INFO");
                                        String NAME_LINK = request.getParameter("NAME_LINK");
                                        String LINK_VALUE = request.getParameter("LINK_VALUE");
                                        String IS_LOCK = request.getParameter("IS_LOCK");
                                        String IS_LOST = request.getParameter("IS_LOST");
                                        String IS_UNLOCK = request.getParameter("IS_UNLOCK");
                                        String LOCK_REASON = EscapeUtils.CheckTextNull(request.getParameter("LOCK_REASON"));
                                        String IS_RE_OPERATION = request.getParameter("IS_RE_OPERATION");
                                        String vIS_PUSH_NOTICE_SET_NO = request.getParameter("vIS_PUSH_NOTICE_SET_NO");
                                        String vIS_MENU_LINK_SET_NO = request.getParameter("vIS_MENU_LINK_SET_NO");
                                        RESPONSE_CODE[][] rsResponse = new RESPONSE_CODE[1][];
                                        db.S_BO_RESPONSE_CODE_DETAIL(String.valueOf(Definitions.CONFIG_RESPONSE_CODE_ID_SUCCESS), rsResponse);
                                        TOKEN_CHANGE_LOG tempLogReq;
                                        ATTRIBUTE_VALUES valueATTR;
                                        RESPONSE_LOG tempLogRes;
                                        MENULINK_TOKEN itemParseMenu;
                                        PUSH_TOKEN itemParsePush;
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        int[] System_Log_ID = new int[1];
                                        ROLE_DATA[][] sessFunctionToken = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Token");

                                        //<editor-fold defaultstate="collapsed" desc="IS_INITIALIZE - CANCEL">
//                                        if (!"0".equals(IS_INITIALIZE)) {
//                                            tempLogReq = new TOKEN_CHANGE_LOG();
//                                            tempLogReq.setTOKEN_SN(sTokenSN);
//                                            tempLogReq.setINITIALIZE(IS_INITIALIZE.equals("1") ? "True" : "False");
//                                            String strReq = CommonFunction.GenJSONTokenLog(tempLogReq);
//                                            db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
//                                                    Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTokenSN, "",
//                                                    Definitions.CONFIG_LOG_FUNCTIONALITY_INITIALIZE, strReq,
//                                                    sUID_Update, System_Log_ID);
//                                            db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_INITIALIZE,
//                                                    "", sUID_Update);
//                                            tempLogRes = new RESPONSE_LOG();
//                                            tempLogRes.ResponseCode = rsResponse[0][0].NAME;
//                                            tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
//                                            String strRes = objectMapper.writeValueAsString(tempLogRes);
//                                            db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, strRes, sUID_Update);
//                                        }
                                        //</editor-fold>

                                        //<editor-fold defaultstate="collapsed" desc="IS_CHANGE_SOPIN - CANCEL">
//                                        if (!"0".equals(IS_CHANGE_SOPIN)) {
//                                            tempLogReq = new TOKEN_CHANGE_LOG();
//                                            tempLogReq.setTOKEN_SN(sTokenSN);
//                                            tempLogReq.setCHANGE_SOPIN(IS_CHANGE_SOPIN.equals("1") ? "True" : "False");
//                                            String strReq = CommonFunction.GenJSONTokenLog(tempLogReq);
//                                            db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
//                                                    Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTokenSN, "",
//                                                    Definitions.CONFIG_LOG_FUNCTIONALITY_CHANGE_SOPIN, strReq,
//                                                    sUID_Update, System_Log_ID);
//                                            db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_CHANGE_SOPIN,
//                                                    "", sUID_Update);
//                                            tempLogRes = new RESPONSE_LOG();
//                                            tempLogRes.ResponseCode = rsResponse[0][0].NAME;
//                                            tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
//                                            String strRes = objectMapper.writeValueAsString(tempLogRes);
//                                            db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, strRes, sUID_Update);
//                                        }
                                        //</editor-fold>

                                        //<editor-fold defaultstate="collapsed" desc="IS_LOCK">
                                        if (!"0".equals(IS_LOCK)) {
                                            if(sTOKEN_STATE_ID != Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_LOCK,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true)
                                                {
                                                    tempLogReq = new TOKEN_CHANGE_LOG();
                                                    tempLogReq.setTOKEN_SN(sTokenSN);
                                                    tempLogReq.setIS_LOCK(IS_LOCK.equals("1") ? "True" : "False");
                                                    String strReq = CommonFunction.GenJSONTokenLog(tempLogReq);
                                                    db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                            Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTokenSN, "",
                                                            Definitions.CONFIG_LOG_FUNCTIONALITY_LOCK, strReq,
                                                            sUID_Update, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                    valueATTR = new ATTRIBUTE_VALUES();
                                                    valueATTR.setTokenSn(sTokenSN);
                                                    valueATTR.setActionReason(LOCK_REASON);
                                                    valueATTR.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_LOCK);
                                                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_PENDING);
                                                    valueATTR.setCreateUser(loginFullname + " (" + sUID_Update + ")");
                                                    valueATTR.setCreateDt(new Date());
                                                    if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                    {
                                                        valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                        db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOCK,
                                                            intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                    } else {
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_APPROVED_TOKEN,
                                                            Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                            valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                            db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOCK,
                                                                intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                        }
                                                        else {
                                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_PENDING);
                                                            db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOCK,
                                                                intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                        }
                                                    }
                                                    // VALUE ATTR
                                                    tempLogRes = new RESPONSE_LOG();
                                                    tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                                    tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                                    String strRes = objectMapper.writeValueAsString(tempLogRes);
                                                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, strRes, "", sUID_Update);
                                                }
                                            }
                                        }
                                        //</editor-fold>

                                        //<editor-fold defaultstate="collapsed" desc="IS_UNLOCK">
                                        if (!"0".equals(IS_UNLOCK)) {
                                            if(sTOKEN_STATE_ID != Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_UNLOCK,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true)
                                                {
                                                    tempLogReq = new TOKEN_CHANGE_LOG();
                                                    tempLogReq.setTOKEN_SN(sTokenSN);
                                                    tempLogReq.setIS_UNLOCK(IS_UNLOCK.equals("1") ? "True" : "False");
                                                    String strReq = CommonFunction.GenJSONTokenLog(tempLogReq);
                                                    db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                            Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTokenSN, "",
                                                            Definitions.CONFIG_LOG_FUNCTIONALITY_UNLOCK, strReq,
                                                            sUID_Update, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                    valueATTR = new ATTRIBUTE_VALUES();
                                                    valueATTR.setTokenSn(sTokenSN);
                                                    valueATTR.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_UNLOCK);
                                                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_PENDING);
                                                    valueATTR.setCreateUser(loginFullname + " (" + sUID_Update + ")");
                                                    valueATTR.setCreateDt(new Date());
                                                    if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                    {
                                                        valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                        db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_UNLOCK,
                                                            intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                    } else {
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_APPROVED_TOKEN,
                                                            Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                            valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                            db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_UNLOCK,
                                                                intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                        }
                                                        else
                                                        {
                                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_PENDING);
                                                            db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_UNLOCK,
                                                                intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                        }
                                                    }
                                                    // VALUE ATTR
                                                    tempLogRes = new RESPONSE_LOG();
                                                    tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                                    tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                                    String strRes = objectMapper.writeValueAsString(tempLogRes);
                                                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, strRes, "", sUID_Update);
                                                }
                                            }
                                        }
                                        //</editor-fold>

                                        //<editor-fold defaultstate="collapsed" desc="IS_MENULINK">
                                        if (!"".equals(NAME_LINK) || !"".equals(LINK_VALUE)) {
                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_MENU_LINK,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true)
                                            {
                                                CommonFunction.LogDebugString(log, "TOKEN-EDIT-MENULINK", "NAME_LINK: " + NAME_LINK + "; LINK_VALUE: " + LINK_VALUE
                                                    + "; IS_MENU_LINK_SET_NO: " + vIS_MENU_LINK_SET_NO);
                                                String strReqMenu;
                                                itemParseMenu = new MENULINK_TOKEN();
                                                if (!"1".equals(vIS_MENU_LINK_SET_NO)) {
                                                    itemParseMenu.MENU_LINK_NAME = NAME_LINK;
                                                    itemParseMenu.MENU_LINK_URL = LINK_VALUE;
                                                } else {
                                                    itemParseMenu.MENU_LINK_NAME = "";
                                                    itemParseMenu.MENU_LINK_URL = "";
                                                }
                                                strReqMenu = objectMapper.writeValueAsString(itemParseMenu);
                                                ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                dataATTR.setMenulink(itemParseMenu);
                                                valueATTR = new ATTRIBUTE_VALUES();
                                                valueATTR.setTokenSn(sTokenSN);
                                                valueATTR.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_MENU_LINK);
                                                valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_PENDING);
                                                valueATTR.setCreateUser(loginFullname + " (" + sUID_Update + ")");
                                                valueATTR.setCreateDt(new Date());
                                                db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                    Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTokenSN, "",
                                                    Definitions.CONFIG_LOG_FUNCTIONALITY_BO_MENU_LINK, strReqMenu,
                                                    sUID_Update, System_Log_ID, sIP_Request, sysLog_BillCode);

                                                if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                {
                                                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                    valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                    valueATTR.setApproveDt(new Date());
                                                    valueATTR.setAttributeData(dataATTR);
                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                                                    db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_MENU_LINK,
                                                        intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                    if ("1".equals(vIS_MENU_LINK_SET_NO)) {
                                                        strReqMenu = Definitions.CONFIG_UPDATE_DEFAULT_VALUE_NULL;
                                                    }
                                                    db.S_BO_TOKEN_UPDATE(Integer.parseInt(sID), "", strReqMenu, "", "", "", sUID_Update);
                                                } else {
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_APPROVED_TOKEN,
                                                        Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                        valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        valueATTR.setAttributeData(dataATTR);
                                                        String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                                                        db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_MENU_LINK,
                                                            intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                        if ("1".equals(vIS_MENU_LINK_SET_NO)) {
                                                            strReqMenu = Definitions.CONFIG_UPDATE_DEFAULT_VALUE_NULL;
                                                        }
                                                        db.S_BO_TOKEN_UPDATE(Integer.parseInt(sID), "", strReqMenu, "", "", "", sUID_Update);
                                                    } else {
                                                        valueATTR.setAttributeData(dataATTR);
                                                        String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_PENDING);
                                                        db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_MENU_LINK,
                                                            intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                    }
                                                }
                                                tempLogRes = new RESPONSE_LOG();
                                                tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                                tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                                String strRes = objectMapper.writeValueAsString(tempLogRes);
                                                db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, strRes, "", sUID_Update);
                                            }
                                        }
                                        //</editor-fold>

                                        //<editor-fold defaultstate="collapsed" desc="IS_PUSH_NOTICE">
                                        if (!"".equals(LINK_NOTICE) || !"".equals(NOTICE_INFO)
                                                || !"".equals(COLOR_TEXT) || !"".equals(COLOR_BKGR)) {
                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_PUSH_NOTFICATION,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true)
                                            {
                                                CommonFunction.LogDebugString(log, "TOKEN-EDIT-PUSH_NOTICE", "NOTICE_INFO: " + NOTICE_INFO
                                                        + "; LINK_NOTICE: " + LINK_NOTICE + "; COLOR_TEXT: " + COLOR_TEXT
                                                        + "; COLOR_BKGR: " + COLOR_BKGR + "; IS_PUSH_NOTICE_SET_NO: " + vIS_PUSH_NOTICE_SET_NO);
                                                String strReqPush;
                                                itemParsePush = new PUSH_TOKEN();
                                                if (!"1".equals(vIS_PUSH_NOTICE_SET_NO)) {
                                                    itemParsePush.PUSH_NOTICE_CONTENT = NOTICE_INFO;
                                                    itemParsePush.PUSH_NOTICE_URL = LINK_NOTICE;
                                                    itemParsePush.PUSH_NOTICE_TEXT_COLOR = COLOR_TEXT;
                                                    itemParsePush.PUSH_NOTICE_BGR_COLOR = COLOR_BKGR;
                                                } else {
                                                    itemParsePush.PUSH_NOTICE_CONTENT = "";
                                                    itemParsePush.PUSH_NOTICE_URL = "";
                                                    itemParsePush.PUSH_NOTICE_TEXT_COLOR = "";
                                                    itemParsePush.PUSH_NOTICE_BGR_COLOR = "";
                                                }
                                                strReqPush = objectMapper.writeValueAsString(itemParsePush);
                                                ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                dataATTR.setSticker(itemParsePush);
                                                valueATTR = new ATTRIBUTE_VALUES();
                                                valueATTR.setTokenSn(sTokenSN);
                                                valueATTR.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PUSH_NOTFICATION);
                                                valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_PENDING);
                                                valueATTR.setCreateUser(loginFullname + " (" + sUID_Update + ")");
                                                valueATTR.setCreateDt(new Date());
                                                db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                    Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTokenSN, "",
                                                    Definitions.CONFIG_LOG_FUNCTIONALITY_PUSH_NOTIFICATION, strReqPush,
                                                    sUID_Update, System_Log_ID, sIP_Request, sysLog_BillCode);

                                                if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                {
                                                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                    valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                    valueATTR.setApproveDt(new Date());
                                                    valueATTR.setAttributeData(dataATTR);
                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                                                    db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PUSH_NOTFICATION,
                                                        intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                    if ("1".equals(vIS_PUSH_NOTICE_SET_NO)) {
                                                        strReqPush = Definitions.CONFIG_UPDATE_DEFAULT_VALUE_NULL;
                                                    }
                                                    db.S_BO_TOKEN_UPDATE(Integer.parseInt(sID), "", "", strReqPush,"", "", sUID_Update);
                                                } else {
                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_APPROVED_TOKEN,
                                                        Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                        valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        valueATTR.setAttributeData(dataATTR);
                                                        String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                                                        db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PUSH_NOTFICATION,
                                                            intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                        if ("1".equals(vIS_PUSH_NOTICE_SET_NO)) {
                                                            strReqPush = Definitions.CONFIG_UPDATE_DEFAULT_VALUE_NULL;
                                                        }
                                                        db.S_BO_TOKEN_UPDATE(Integer.parseInt(sID), "", "", strReqPush,"", "", sUID_Update);
                                                    } else
                                                    {
                                                        valueATTR.setAttributeData(dataATTR);
                                                        String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_PENDING);
                                                        db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PUSH_NOTFICATION,
                                                            intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                    }
                                                }
                                                tempLogRes = new RESPONSE_LOG();
                                                tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                                tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                                String strRes = objectMapper.writeValueAsString(tempLogRes);
                                                db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, strRes, "", sUID_Update);
                                            }
                                        }
                                        //</editor-fold>

                                        //<editor-fold defaultstate="collapsed" desc="IS_LOST">
                                        if (!"0".equals(IS_LOST)) {
                                            if(sTOKEN_STATE_ID != Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_LOST,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true)
                                                {
                                                    tempLogReq = new TOKEN_CHANGE_LOG();
                                                    tempLogReq.setTOKEN_SN(sTokenSN);
                                                    tempLogReq.setIS_LOST(IS_LOST.equals("1") ? "True" : "False");
                                                    String strReq = CommonFunction.GenJSONTokenLog(tempLogReq);
                                                    db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                            Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTokenSN, "",
                                                            Definitions.CONFIG_LOG_FUNCTIONALITY_LOST, strReq,
                                                            sUID_Update, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                    valueATTR = new ATTRIBUTE_VALUES();
                                                    valueATTR.setTokenSn(sTokenSN);
                                                    valueATTR.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_LOST);
                                                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_PENDING);
                                                    valueATTR.setCreateUser(loginFullname + " (" + sUID_Update + ")");
                                                    valueATTR.setCreateDt(new Date());
                                                    if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                    {
                                                        valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                        db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOST,
                                                            intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                    } else {
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_APPROVED_TOKEN,
                                                            Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                            valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                            db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOST,
                                                                intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                        } else {
                                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_PENDING);
                                                            db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOST,
                                                                intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                        }
                                                    }
                                                    tempLogRes = new RESPONSE_LOG();
                                                    tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                                    tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                                    String strRes = objectMapper.writeValueAsString(tempLogRes);
                                                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, strRes, "", sUID_Update);
                                                }
                                            }
                                        }
                                        //</editor-fold>

                                        //<editor-fold defaultstate="collapsed" desc="IS_RE_OPERATION">
                                        if (!"0".equals(IS_RE_OPERATION)) {
                                            if(sTOKEN_STATE_ID == Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_RE_OPERATION,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true)
                                                {
                                                    tempLogReq = new TOKEN_CHANGE_LOG();
                                                    tempLogReq.setTOKEN_SN(sTokenSN);
                                                    tempLogReq.setIS_RE_OPERATION(IS_RE_OPERATION.equals("1") ? "True" : "False");
                                                    String strReq = CommonFunction.GenJSONTokenLog(tempLogReq);
                                                    db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                            Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTokenSN, "",
                                                            Definitions.CONFIG_LOG_FUNCTIONALITY_RE_OPERATION, strReq,
                                                            sUID_Update, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                    // VALUE ATTR
                                                    valueATTR = new ATTRIBUTE_VALUES();
                                                    valueATTR.setTokenSn(sTokenSN);
                                                    valueATTR.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_RE_OPERATION);
                                                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_PENDING);
                                                    valueATTR.setCreateUser(loginFullname + " (" + sUID_Update + ")");
                                                    valueATTR.setCreateDt(new Date());
                                                    if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                    {
                                                        valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                        valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                        valueATTR.setApproveDt(new Date());
                                                        String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                        int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                        db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_RE_OPERATION,
                                                            intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                    } else {
                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_APPROVED_TOKEN,
                                                            Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                            valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                            db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_RE_OPERATION,
                                                                intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                        } else {
                                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_PENDING);
                                                            db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_RE_OPERATION,
                                                                intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                        }
                                                    }
                                                    // VALUE ATTR

                                                    tempLogRes = new RESPONSE_LOG();
                                                    tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                                    tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                                    String strRes = objectMapper.writeValueAsString(tempLogRes);
                                                    db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, strRes, "", sUID_Update);
                                                }
                                            }
                                        }
                                        //</editor-fold>

                                        strView = "0#0";
                                        sessionsa.setAttribute("RefreshTokenSess", "1");
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "addtoken": {
                                //<editor-fold defaultstate="collapsed" desc="addtoken">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                        || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                                    {
                                        String sTokenSN = EscapeUtils.CheckTextNull(request.getParameter("TokenSN"));
                                        String sTokenSOPIN = EscapeUtils.CheckTextNull(request.getParameter("TokenSOPIN"));
                                        String sTOKEN_VERSION = EscapeUtils.CheckTextNull(request.getParameter("TOKEN_VERSION"));
                                        String sBranchID = EscapeUtils.CheckTextNull(request.getParameter("BranchID"));
                                        if(!"".equals(sTokenSN) && !"".equals(sTokenSOPIN)
                                            && !"".equals(sTOKEN_VERSION) && !"".equals(sBranchID))
                                        {
                                            if(sTokenSN.length() <= Definitions.CONFIG_LENGTH_TOKEN_SN
                                                && sTokenSN.length() <= Definitions.CONFIG_LENGTH_TOKEN_SOPIN)
                                            {
                                                EncodeSOPIN encript = new EncodeSOPIN();
                                                int param1 = db.S_BO_TOKEN_INSERT(sTokenSN, encript.encode(sTokenSOPIN), sTOKEN_VERSION, sBranchID, sUID_Update);
                                                if (param1 == 0) {
                                                    strView = "0#0";
                                                } else {
                                                    strView = param1+"#"+param1;
                                                }
                                            } else {
                                                CommonFunction.LogErrorServlet(log, "AddToken: Length is invalid");
                                                strView = "3#0";
                                            }
                                        } else {
                                            CommonFunction.LogErrorServlet(log, "AddToken: Please fill all");
                                            strView = "2#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "resetauthenotp": {
                                //<editor-fold defaultstate="collapsed" desc="resetauthenotp">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    ROLE_DATA[][] sessFunctionToken = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Token");
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_RESET_ACTIVATION_REMAINING_COUNTER,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true)
                                    {
                                        ATTRIBUTE_VALUES valueATTR;
                                        String sID = request.getParameter("ID");
                                        String sTokenSN = "";
                                        boolean isAccessAgency = true;
                                        TOKEN[][] rsReq = new TOKEN[1][];
                                        db.S_BO_TOKEN_DETAIL(sID, rsReq);
                                        if (rsReq[0].length > 0) {
                                            sTokenSN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                            String sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                                if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                    isAccessAgency = false;
//                                                }
                                            }
                                        } else {
                                            isAccessAgency = false;
                                        }
                                        if (isAccessAgency == true) {
                                            RESPONSE_CODE[][] rsResponse = new RESPONSE_CODE[1][];
                                            db.S_BO_RESPONSE_CODE_DETAIL(String.valueOf(Definitions.CONFIG_RESPONSE_CODE_ID_SUCCESS), rsResponse);
                                            TOKEN_CHANGE_LOG tempLogReq;
                                            RESPONSE_LOG tempLogRes;
                                            ObjectMapper objectMapper = new ObjectMapper();
                                            int[] System_Log_ID = new int[1];
                                            tempLogReq = new TOKEN_CHANGE_LOG();
                                            tempLogReq.setTOKEN_SN(sTokenSN);
                                            tempLogReq.setACTIVATION_REMAINING_COUNTER("True");
                                            String strReq = CommonFunction.GenJSONTokenLog(tempLogReq);
                                            db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTokenSN, "",
                                                Definitions.CONFIG_LOG_FUNCTIONALITY_RESET_ACTIVATION_CODE, strReq,
                                                sUID_Update, System_Log_ID, sIP_Request, sysLog_BillCode);
                                            valueATTR = new ATTRIBUTE_VALUES();
                                            valueATTR.setTokenSn(sTokenSN);
                                            valueATTR.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_RESET_ACTIVATION_REMAINING_COUNTER);
                                            valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_PENDING);
                                            valueATTR.setCreateUser(loginFullname + " (" + sUID_Update + ")");
                                            valueATTR.setCreateDt(new Date());

                                            if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                            {
                                                valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                valueATTR.setApproveDt(new Date());
                                                String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                                                db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_RESET_ACTIVATION_REMAINING_COUNTER,
                                                    intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                String sACTIVATION_MAX_COUNTER = sessionsa.getAttribute("SessGlobalActivation_Max_Code").toString().trim();
                                                db.S_BO_TOKEN_UPDATE(Integer.parseInt(sID), "", "", "", "", sACTIVATION_MAX_COUNTER, sUID_Update);
                                            }
                                            else
                                            {
                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_APPROVED_TOKEN,
                                                    Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                    valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                    valueATTR.setApproveDt(new Date());
                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_COMMITED);
                                                    db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_RESET_ACTIVATION_REMAINING_COUNTER,
                                                        intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                    String sACTIVATION_MAX_COUNTER = sessionsa.getAttribute("SessGlobalActivation_Max_Code").toString().trim();
                                                    db.S_BO_TOKEN_UPDATE(Integer.parseInt(sID), "", "", "", "", sACTIVATION_MAX_COUNTER, sUID_Update);
                                                } else {
                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_PENDING);
                                                    db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_RESET_ACTIVATION_REMAINING_COUNTER,
                                                        intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                }
                                            }
                                            tempLogRes = new RESPONSE_LOG();
                                            tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                            tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                            String strRes = objectMapper.writeValueAsString(tempLogRes);
                                            db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, strRes, "", sUID_Update);
                                            strView = "0#0";
                                            sessionsa.setAttribute("RefreshTokenSess", "1");
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "inittoken": {
                                //<editor-fold defaultstate="collapsed" desc="inittoken">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    ROLE_DATA[][] sessFunctionToken = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Token");
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_INITIALIZE,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true)
                                    {
                                        ATTRIBUTE_VALUES valueATTR;
                                        String sID = request.getParameter("ID");
                                        String sTokenSN = "";
                                        boolean isAccessAgency = true;
                                        TOKEN[][] rsReq = new TOKEN[1][];
                                        db.S_BO_TOKEN_DETAIL(sID, rsReq);
                                        if (rsReq[0].length > 0) {
                                            sTokenSN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                            String sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                                if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                    isAccessAgency = false;
//                                                }
                                            }
                                        } else {
                                            isAccessAgency = false;
                                        }
                                        if (isAccessAgency == true) {
                                            RESPONSE_CODE[][] rsResponse = new RESPONSE_CODE[1][];
                                            db.S_BO_RESPONSE_CODE_DETAIL(String.valueOf(Definitions.CONFIG_RESPONSE_CODE_ID_SUCCESS), rsResponse);
                                            TOKEN_CHANGE_LOG tempLogReq;
                                            RESPONSE_LOG tempLogRes;
                                            ObjectMapper objectMapper = new ObjectMapper();
                                            int[] System_Log_ID = new int[1];
                                            tempLogReq = new TOKEN_CHANGE_LOG();
                                            tempLogReq.setTOKEN_SN(sTokenSN);
                                            tempLogReq.setINITIALIZE("True");
                                            String strReq = CommonFunction.GenJSONTokenLog(tempLogReq);
                                            db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTokenSN, "",
                                                Definitions.CONFIG_LOG_FUNCTIONALITY_INITIALIZE, strReq,
                                                sUID_Update, System_Log_ID, sIP_Request, sysLog_BillCode);
                                            // VALUE ATTR
                                            valueATTR = new ATTRIBUTE_VALUES();
                                            valueATTR.setTokenSn(sTokenSN);
                                            valueATTR.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_INITIALIZE);
                                            valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_PENDING);
                                            valueATTR.setCreateUser(loginFullname + " (" + sUID_Update + ")");
                                            valueATTR.setCreateDt(new Date());
                                            // VALUE ATTR
                                            if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                    || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                            {
                                                valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                valueATTR.setApproveDt(new Date());
                                                String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_INITIALIZE,
                                                    intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                            } else {
                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_APPROVED_TOKEN,
                                                    Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                    valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                    valueATTR.setApproveDt(new Date());
                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                    db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_INITIALIZE,
                                                        intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                } else {
                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_PENDING);
                                                    db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_INITIALIZE,
                                                        intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                }
                                            }
                                            tempLogRes = new RESPONSE_LOG();
                                            tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                            tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                            String strRes = objectMapper.writeValueAsString(tempLogRes);
                                            db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, strRes, "", sUID_Update);
                                            strView = "0#0";
                                            sessionsa.setAttribute("RefreshTokenSess", "1");
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "changesopintoken": {
                                //<editor-fold defaultstate="collapsed" desc="changesopintoken">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    ROLE_DATA[][] sessFunctionToken = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Token");
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_CHANGE_SOPIN,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true)
                                    {
                                        ATTRIBUTE_VALUES valueATTR;
                                        String sID = request.getParameter("ID");
                                        String sTokenSN = "";
                                        boolean isAccessAgency = true;
                                        TOKEN[][] rsReq = new TOKEN[1][];
                                        db.S_BO_TOKEN_DETAIL(sID, rsReq);
                                        if (rsReq[0].length > 0) {
                                            sTokenSN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                            String sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                                if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                    isAccessAgency = false;
//                                                }
                                            }
                                        } else {
                                            isAccessAgency = false;
                                        }
                                        if (isAccessAgency == true) {
                                            RESPONSE_CODE[][] rsResponse = new RESPONSE_CODE[1][];
                                            db.S_BO_RESPONSE_CODE_DETAIL(String.valueOf(Definitions.CONFIG_RESPONSE_CODE_ID_SUCCESS), rsResponse);
                                            TOKEN_CHANGE_LOG tempLogReq;
                                            RESPONSE_LOG tempLogRes;
                                            ObjectMapper objectMapper = new ObjectMapper();
                                            int[] System_Log_ID = new int[1];
                                            tempLogReq = new TOKEN_CHANGE_LOG();
                                            tempLogReq.setTOKEN_SN(sTokenSN);
                                            tempLogReq.setCHANGE_SOPIN("True");
                                            String strReq = CommonFunction.GenJSONTokenLog(tempLogReq);
                                            db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                    Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTokenSN, "",
                                                    Definitions.CONFIG_LOG_FUNCTIONALITY_CHANGE_SOPIN, strReq,
                                                    sUID_Update, System_Log_ID, sIP_Request, sysLog_BillCode);
                                            // VALUE ATTR
                                            valueATTR = new ATTRIBUTE_VALUES();
                                            valueATTR.setTokenSn(sTokenSN);
                                            valueATTR.setTypeName(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_CHANGE_SOPIN);
                                            valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_PENDING);
                                            valueATTR.setCreateUser(loginFullname + " (" + sUID_Update + ")");
                                            valueATTR.setCreateDt(new Date());
                                            // VALUE ATTR
                                            if (request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                                                || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                || request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                            {
                                                valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                valueATTR.setApproveDt(new Date());
                                                String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_CHANGE_SOPIN,
                                                    intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                            } else {
                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_APPROVED_TOKEN,
                                                    Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true) {
                                                    valueATTR.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                    valueATTR.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                    valueATTR.setApproveDt(new Date());
                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_APPROVED);
                                                    db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_CHANGE_SOPIN,
                                                        intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                } else {
                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    int intTOKEN_ATTR_STATE = Integer.parseInt(Definitions.CONFIG_TOKEN_ATTR_STATE_ID_PENDING);
                                                    db.S_BO_TOKEN_ATTR_INSERT(Integer.parseInt(sID), Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_CHANGE_SOPIN,
                                                        intTOKEN_ATTR_STATE, strReqValueATTR, sUID_Update);
                                                }
                                            }
                                            tempLogRes = new RESPONSE_LOG();
                                            tempLogRes.ResponseCode = rsResponse[0][0].NAME;
                                            tempLogRes.ResponseMessage = rsResponse[0][0].REMARK;
                                            String strRes = objectMapper.writeValueAsString(tempLogRes);
                                            db.S_BO_SYSTEM_LOG_UPDATE(System_Log_ID[0], rsResponse[0][0].NAME, strRes, "", sUID_Update);
                                            strView = "0#0";
                                            sessionsa.setAttribute("RefreshTokenSess", "1");
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "deletetoken": {
                                //<editor-fold defaultstate="collapsed" desc="deletetoken">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    if(request.getSession(false).getAttribute("RoleID_ID").toString().trim().equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                                        String sID = EscapeUtils.CheckTextNull(request.getParameter("ID"));
                                        String sParam = db.S_BO_TOKEN_DELETE(Integer.parseInt(sID), sUID_Update);
                                        if("0".equals(sParam)) {
                                            strView = "0#0";
                                            sessionsa.setAttribute("RefreshTokenSess", "1");
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                            CommonFunction.LogErrorServlet(log, "Delete Token Error, Output: " + sParam);
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_ROLE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "cancelrequest": {
                                //<editor-fold defaultstate="collapsed" desc="cancelrequest">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    ROLE_DATA[][] sessFunctionToken = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Token");
                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_TOKEN_DECLINED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN_REQUUEST, sessFunctionToken) == true)
                                    {
                                        String sID = request.getParameter("TokenID");
                                        String sToken_AtrrID = request.getParameter("id");
                                        // check agency
                                        boolean isAccessAgency = true;
                                        TOKEN[][] rsReq = new TOKEN[1][];
                                        db.S_BO_TOKEN_DETAIL(sID, rsReq);
                                        if (rsReq[0].length > 0) {
                                            String sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                            if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                                if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                    isAccessAgency = false;
//                                                }
                                            }
                                        } else {
                                            isAccessAgency = false;
                                        }
                                        if (isAccessAgency == true) {
                                            db.S_BO_TOKEN_ATTR_DECLINED(Integer.parseInt(sToken_AtrrID), sUID_Update);
                                            strView = "0#0";
                                            sessionsa.setAttribute("RefreshApproveTokenSess", "1");
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "approvetoken": {
                                //<editor-fold defaultstate="collapsed" desc="approvetoken">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    String sID = request.getParameter("TOKEN_ID");
                                    String vTOKEN_ATTR_ID = request.getParameter("vTOKEN_ATTR_ID");
                                    String LOCK_REASON = EscapeUtils.CheckTextNull(request.getParameter("LOCK_REASON"));
                                    String vTOKEN_ATTR_TYPE_ID = request.getParameter("vTOKEN_ATTR_TYPE_ID");
                                    String vLanguage = request.getParameter("vLanguage");
                                    // check agency
                                    boolean isAccessAgency = true;
                                    TOKEN[][] rsReq = new TOKEN[1][];
                                    db.S_BO_TOKEN_DETAIL(sID, rsReq);
                                    if (rsReq[0].length > 0) {
                                        String sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
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
                                        if (Integer.parseInt(vTOKEN_ATTR_TYPE_ID) == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_INITIALIZE
                                                || Integer.parseInt(vTOKEN_ATTR_TYPE_ID) == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOCK
                                                || Integer.parseInt(vTOKEN_ATTR_TYPE_ID) == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_UNLOCK
                                                || Integer.parseInt(vTOKEN_ATTR_TYPE_ID) == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_CHANGE_SOPIN
                                                || Integer.parseInt(vTOKEN_ATTR_TYPE_ID) == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOST
                                                || Integer.parseInt(vTOKEN_ATTR_TYPE_ID) == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_RE_OPERATION) {
                                            String sVALUE_OLD = "";
                                            String strReqValueATTR = "";
                                            TOKEN[][] rsToken = new TOKEN[1][];
                                            db.S_BO_TOKEN_ATTR_DETAIL(vTOKEN_ATTR_ID, vLanguage, rsToken);
                                            if (rsToken[0].length > 0) {
                                                if (!"".equals(EscapeUtils.CheckTextNull(rsToken[0][0].VALUE))) {
                                                    sVALUE_OLD = EscapeUtils.CheckTextNull(rsToken[0][0].VALUE);
                                                }
                                            }
                                            if(!"".equals(sVALUE_OLD))
                                            {
                                                // VALUE ATTR_FRIST
                                                ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                String sToken_Frist = valueATTR_Frist.getTokenSn();
                                                String sTypeName_Frist = valueATTR_Frist.getTypeName();
                                                String sCreateUser_Frist = valueATTR_Frist.getCreateUser();
                                                Date sCreateDt_Frist = valueATTR_Frist.getCreateDt();
                                                // VALUE ATTR_LAST
                                                ATTRIBUTE_VALUES valueATTR_Last = new ATTRIBUTE_VALUES();
//                                                ATTRIBUTE_DATA dataATTR_Last = new ATTRIBUTE_DATA();
                                                valueATTR_Last.setTokenSn(sToken_Frist);
                                                valueATTR_Last.setTypeName(sTypeName_Frist);
                                                valueATTR_Last.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                if (Integer.parseInt(vTOKEN_ATTR_TYPE_ID) == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_LOCK) {
                                                    valueATTR_Last.setActionReason(LOCK_REASON);
                                                }
                                                valueATTR_Last.setCreateUser(sCreateUser_Frist);
                                                valueATTR_Last.setCreateDt(sCreateDt_Frist);
                                                valueATTR_Last.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                valueATTR_Last.setApproveDt(new Date());
//                                                valueATTR_Last.setAttributeData(dataATTR_Last);
                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                // VALUE ATTR
                                            }
                                            db.S_BO_TOKEN_ATTR_APPROVED(Integer.parseInt(vTOKEN_ATTR_ID),
                                                    Integer.parseInt(sID), vTOKEN_ATTR_TYPE_ID, strReqValueATTR, sUID_Update);
                                            strView = "0#0";
                                            sessionsa.setAttribute("RefreshApproveTokenSess", "1");
                                        }
                                        if (Integer.parseInt(vTOKEN_ATTR_TYPE_ID) == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_MENU_LINK) {
                                            String sJSONMenu = Definitions.CONFIG_UPDATE_DEFAULT_VALUE_NULL;
//                                            String sJSONMenuRequest = "";
                                            String sVALUE_OLD = "";
                                            String strReqValueATTR = "";
                                            TOKEN[][] rsToken = new TOKEN[1][];
                                            db.S_BO_TOKEN_ATTR_DETAIL(vTOKEN_ATTR_ID, vLanguage, rsToken);
                                            if (rsToken[0].length > 0) {
                                                if (!"".equals(EscapeUtils.CheckTextNull(rsToken[0][0].VALUE))) {
                                                    sVALUE_OLD = EscapeUtils.CheckTextNull(rsToken[0][0].VALUE);
                                                }
                                            }
                                            if(!"".equals(sVALUE_OLD))
                                            {
                                                // VALUE ATTR_FRIST
                                                ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                String sToken_Frist = valueATTR_Frist.getTokenSn();
                                                String sTypeName_Frist = valueATTR_Frist.getTypeName();
                                                String sCreateUser_Frist = valueATTR_Frist.getCreateUser();
                                                Date sCreateDt_Frist = valueATTR_Frist.getCreateDt();
                                                // VALUE ATTR_LAST
                                                ATTRIBUTE_VALUES valueATTR_Last = new ATTRIBUTE_VALUES();
                                                ATTRIBUTE_DATA dataATTR_Last = new ATTRIBUTE_DATA();
                                                valueATTR_Last.setTokenSn(sToken_Frist);
                                                valueATTR_Last.setTypeName(sTypeName_Frist);
                                                valueATTR_Last.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                valueATTR_Last.setCreateUser(sCreateUser_Frist);
                                                valueATTR_Last.setCreateDt(sCreateDt_Frist);
                                                valueATTR_Last.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                valueATTR_Last.setApproveDt(new Date());
                                                // VALUE ATTR
                                                ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                ATTRIBUTE_VALUES itemParsePush = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                dataATTR = itemParsePush.getAttributeData();
                                                MENULINK_TOKEN sMenuValue = dataATTR.getMenulink();
                                                if(!"".equals(sMenuValue.MENU_LINK_NAME) || !"".equals(sMenuValue.MENU_LINK_URL))
                                                {
                                                    MENULINK_TOKEN itemMENULINK_TOKEN = new MENULINK_TOKEN();
                                                    itemMENULINK_TOKEN.MENU_LINK_NAME = sMenuValue.MENU_LINK_NAME;
                                                    itemMENULINK_TOKEN.MENU_LINK_URL = sMenuValue.MENU_LINK_URL;
                                                    sJSONMenu = objectMapper.writeValueAsString(itemMENULINK_TOKEN);
                                                }
                                                dataATTR_Last.setMenulink(sMenuValue);
                                                valueATTR_Last.setAttributeData(dataATTR_Last);
                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                            }
                                            db.S_BO_TOKEN_ATTR_APPROVED(Integer.parseInt(vTOKEN_ATTR_ID),
                                                Integer.parseInt(sID), vTOKEN_ATTR_TYPE_ID, strReqValueATTR, sUID_Update);
                                            db.S_BO_TOKEN_UPDATE(Integer.parseInt(sID), "", sJSONMenu, "", "", "", sUID_Update);
                                            strView = "0#0";
                                            sessionsa.setAttribute("RefreshApproveTokenSess", "1");
                                        }
                                        if (Integer.parseInt(vTOKEN_ATTR_TYPE_ID) == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_PUSH_NOTFICATION) {
                                            String sJSONPush = Definitions.CONFIG_UPDATE_DEFAULT_VALUE_NULL;
//                                            String sJSONPushRequest = "";
                                            String sVALUE_OLD = "";
                                            String strReqValueATTR = "";
                                            TOKEN[][] rsToken = new TOKEN[1][];
                                            db.S_BO_TOKEN_ATTR_DETAIL(vTOKEN_ATTR_ID, vLanguage, rsToken);
                                            if (rsToken[0].length > 0) {
                                                if (!"".equals(EscapeUtils.CheckTextNull(rsToken[0][0].VALUE))) {
                                                    sVALUE_OLD = EscapeUtils.CheckTextNull(rsToken[0][0].VALUE);
                                                }
                                            }
                                            if(!"".equals(sVALUE_OLD))
                                            {
                                                // VALUE ATTR_FRIST
                                                ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                String sToken_Frist = valueATTR_Frist.getTokenSn();
                                                String sTypeName_Frist = valueATTR_Frist.getTypeName();
                                                String sCreateUser_Frist = valueATTR_Frist.getCreateUser();
                                                Date sCreateDt_Frist = valueATTR_Frist.getCreateDt();
                                                // VALUE ATTR_LAST
                                                ATTRIBUTE_VALUES valueATTR_Last = new ATTRIBUTE_VALUES();
                                                ATTRIBUTE_DATA dataATTR_Last = new ATTRIBUTE_DATA();
                                                valueATTR_Last.setTokenSn(sToken_Frist);
                                                valueATTR_Last.setTypeName(sTypeName_Frist);
                                                valueATTR_Last.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                valueATTR_Last.setCreateUser(sCreateUser_Frist);
                                                valueATTR_Last.setCreateDt(sCreateDt_Frist);
                                                valueATTR_Last.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                valueATTR_Last.setApproveDt(new Date());
                                                // VALUE ATTR
                                                
                                                ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                ATTRIBUTE_VALUES itemParsePush = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                dataATTR = itemParsePush.getAttributeData();
                                                PUSH_TOKEN sPushValue = dataATTR.getSticker();
                                                if(!"".equals(sPushValue.PUSH_NOTICE_BGR_COLOR) || !"".equals(sPushValue.PUSH_NOTICE_CONTENT)
                                                    || !"".equals(sPushValue.PUSH_NOTICE_TEXT_COLOR) || !"".equals(sPushValue.PUSH_NOTICE_URL))
                                                {
                                                    PUSH_TOKEN itemPUSH_TOKEN = new PUSH_TOKEN();
                                                    itemPUSH_TOKEN.PUSH_NOTICE_CONTENT = sPushValue.PUSH_NOTICE_CONTENT;
                                                    itemPUSH_TOKEN.PUSH_NOTICE_URL = sPushValue.PUSH_NOTICE_URL;
                                                    itemPUSH_TOKEN.PUSH_NOTICE_TEXT_COLOR = sPushValue.PUSH_NOTICE_TEXT_COLOR;
                                                    itemPUSH_TOKEN.PUSH_NOTICE_BGR_COLOR = sPushValue.PUSH_NOTICE_BGR_COLOR;
                                                    sJSONPush = objectMapper.writeValueAsString(itemPUSH_TOKEN);
                                                }
                                                dataATTR_Last.setSticker(sPushValue);
                                                valueATTR_Last.setAttributeData(dataATTR_Last);
                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                            }
                                            db.S_BO_TOKEN_ATTR_APPROVED(Integer.parseInt(vTOKEN_ATTR_ID),
                                                    Integer.parseInt(sID), vTOKEN_ATTR_TYPE_ID, strReqValueATTR, sUID_Update);
                                            db.S_BO_TOKEN_UPDATE(Integer.parseInt(sID), "", "", sJSONPush, "", "", sUID_Update);
                                            strView = "0#0";
                                            sessionsa.setAttribute("RefreshApproveTokenSess", "1");
                                        }
                                        if (Integer.parseInt(vTOKEN_ATTR_TYPE_ID) == Definitions.CONFIG_TOKEN_ATTR_TYPE_ID_RESET_ACTIVATION_REMAINING_COUNTER) {
                                            String sVALUE_OLD = "";
                                            String strReqValueATTR = "";
                                            TOKEN[][] rsToken = new TOKEN[1][];
                                            db.S_BO_TOKEN_ATTR_DETAIL(vTOKEN_ATTR_ID, vLanguage, rsToken);
                                            if (rsToken[0].length > 0) {
                                                if (!"".equals(EscapeUtils.CheckTextNull(rsToken[0][0].VALUE))) {
                                                    sVALUE_OLD = EscapeUtils.CheckTextNull(rsToken[0][0].VALUE);
                                                }
                                            }
                                            if(!"".equals(sVALUE_OLD))
                                            {
                                                // VALUE ATTR_FRIST
                                                ATTRIBUTE_VALUES valueATTR_Frist = objectMapper.readValue(sVALUE_OLD, ATTRIBUTE_VALUES.class);
                                                String sToken_Frist = valueATTR_Frist.getTokenSn();
                                                String sTypeName_Frist = valueATTR_Frist.getTypeName();
                                                String sCreateUser_Frist = valueATTR_Frist.getCreateUser();
                                                Date sCreateDt_Frist = valueATTR_Frist.getCreateDt();
                                                // VALUE ATTR_LAST
                                                ATTRIBUTE_VALUES valueATTR_Last = new ATTRIBUTE_VALUES();
//                                                ATTRIBUTE_DATA dataATTR_Last = new ATTRIBUTE_DATA();
                                                valueATTR_Last.setTokenSn(sToken_Frist);
                                                valueATTR_Last.setTypeName(sTypeName_Frist);
                                                valueATTR_Last.setRequestState(Definitions.CONFIG_TOKEN_ATTR_STATE_CODE_APPROVED);
                                                valueATTR_Last.setCreateUser(sCreateUser_Frist);
                                                valueATTR_Last.setCreateDt(sCreateDt_Frist);
                                                valueATTR_Last.setApproveUser(loginFullname + " (" + sUID_Update + ")");
                                                valueATTR_Last.setApproveDt(new Date());
//                                                valueATTR_Last.setAttributeData(dataATTR_Last);
                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR_Last);
                                                // VALUE ATTR
                                            }
                                            String sACTIVATION_MAX_COUNTER = sessionsa.getAttribute("SessGlobalActivation_Max_Code").toString().trim();
                                            db.S_BO_TOKEN_ATTR_APPROVED(Integer.parseInt(vTOKEN_ATTR_ID),
                                                    Integer.parseInt(sID), vTOKEN_ATTR_TYPE_ID, strReqValueATTR, sUID_Update);
                                            db.S_BO_TOKEN_UPDATE(Integer.parseInt(sID), "", "", "", "", sACTIVATION_MAX_COUNTER, sUID_Update);
                                            strView = "0#0";
                                            sessionsa.setAttribute("RefreshApproveTokenSess", "1");
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
//                            case "addrenewmulti": {
//                                //<editor-fold defaultstate="collapsed" desc="addrenewmulti">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
//                                    int intSuccess = 0;
//                                    int intNoExistToken = 0;
//                                    int intExistoken = 0;
//                                    if (CommonFunction.StrCheckRoleUser(sessionsa.getAttribute("SessRoleSet").toString().trim(), Definitions.CONFIG_FUNROLE_ADD_RENEWAL) == true) {
//                                        String isCheckAll = request.getParameter("isCheckAll");
//                                        if ("0".equals(isCheckAll)) {
//                                            String idValue = EscapeUtils.CheckTextNull(request.getParameter("idValue"));
//                                            String[] sPlitValue = idValue.split(",");
//                                            if (!"".equals(idValue) && sPlitValue.length > 0) {
//                                                for (String sID : sPlitValue) {
//                                                    if (!"".equals(sID)) {
//                                                        int param = db.BACK_CERT_RENEW_REQUEST_INSERT_EXPIRED(sID.replace(Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX, ""),
//                                                                EscapeUtils.CheckTextNull(USER_ID_LOG));
//                                                        if (param == 0) {
//                                                            intSuccess = intSuccess + 1;
//                                                        } else if (param == 1) {
//                                                            intExistoken = intExistoken + 1;
//                                                        } else {
//                                                            intNoExistToken = intNoExistToken + 1;
//                                                        }
//                                                    }
//                                                }
//                                                sessionsa.setAttribute("RefreshTokenSess", "1");
//                                                strView = "0#" + String.valueOf(intSuccess)
//                                                        + "#" + String.valueOf(intExistoken)
//                                                        + "#" + String.valueOf(intNoExistToken);
//                                            } else {
//                                                strView = "1#0";
//                                            }
//                                        } else {
//                                            String idSessIsDate = (String) sessionsa.getAttribute("idSessIsDateS");
//                                            String FromDateValid = (String) sessionsa.getAttribute("sessFromDateValid");
//                                            String ToDateValid = (String) sessionsa.getAttribute("sessToDateValid");
//                                            String FromDateExpire = (String) sessionsa.getAttribute("sessFromDateExpire");
//                                            String ToDateExpire = (String) sessionsa.getAttribute("sessToDateExpire");
//                                            String TOKEN_ID = (String) sessionsa.getAttribute("sessTOKEN_ID");
//                                            String TAX_CODE = (String) sessionsa.getAttribute("sessTAX_CODE");
//                                            String SERIAL_NUMBER_CERT = (String) sessionsa.getAttribute("sessSERIAL_NUMBER_CERT");
//                                            String SUBJECT_NAME = (String) sessionsa.getAttribute("sessSUBJECT_NAME");
//                                            String MOBILE_NO = (String) sessionsa.getAttribute("sessMOBILE_NO");
//                                            String ADDRESS = (String) sessionsa.getAttribute("sessADDRESS");
//                                            String EMAIL = (String) sessionsa.getAttribute("sessEMAIL");
//                                            String LOCATION = (String) sessionsa.getAttribute("sessLOCATION");
//                                            String P_ID = (String) sessionsa.getAttribute("sessP_ID");
//                                            String STATE = (String) sessionsa.getAttribute("sessSTATE");
//                                            String NAME_CN = (String) sessionsa.getAttribute("sessNAME_CN");
//                                            String AGENT_ID = (String) sessionsa.getAttribute("sessAGENT_ID");
//                                            String IS_EXPIRED_DT_AGREE = (String) sessionsa.getAttribute("sessIS_EXPIRED_DT_AGREE");
//                                            if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(IS_EXPIRED_DT_AGREE)) {
//                                                IS_EXPIRED_DT_AGREE = "";
//                                            }
//                                            if (!Definitions.CONFIG_AGENT_ROOT.equals(AGENT_ID_LOG)) {
//                                                AGENT_ID = SessUserAgentID;
//                                            } else {
//                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
//                                                    AGENT_ID = "";
//                                                }
//                                            }
//                                            if (null != idSessIsDate) {
//                                                switch (idSessIsDate) {
//                                                    case "1":
//                                                        FromDateValid = "";
//                                                        ToDateValid = "";
//                                                        break;
//                                                    case "3":
//                                                        FromDateValid = "";
//                                                        ToDateValid = "";
//                                                        FromDateExpire = "";
//                                                        ToDateExpire = "";
//                                                        break;
//                                                    default:
//                                                        FromDateExpire = "";
//                                                        ToDateExpire = "";
//                                                        break;
//                                                }
//                                            }
//                                            TOKEN[][] rsPgin = new TOKEN[1][];
//                                            db.BACK_TOKEN_LIST(EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(TAX_CODE),
//                                                    EscapeUtils.escapeHtmlSearch(SERIAL_NUMBER_CERT), EscapeUtils.escapeHtmlSearch(SUBJECT_NAME),
//                                                    EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
//                                                    EscapeUtils.escapeHtmlSearch(FromDateExpire), EscapeUtils.escapeHtmlSearch(ToDateExpire),
//                                                    EscapeUtils.escapeHtmlSearch(MOBILE_NO),
//                                                    EscapeUtils.escapeHtmlSearch(ADDRESS), EscapeUtils.escapeHtmlSearch(EMAIL),
//                                                    EscapeUtils.escapeHtmlSearch(LOCATION), EscapeUtils.escapeHtmlSearch(P_ID),
//                                                    EscapeUtils.escapeHtmlSearch(STATE), EscapeUtils.escapeHtmlSearch(NAME_CN),
//                                                    EscapeUtils.escapeHtmlSearch(AGENT_ID), EscapeUtils.escapeHtmlSearch(IS_EXPIRED_DT_AGREE),
//                                                    Definitions.CONFIG_GRID_INT_PAGNO, Definitions.CONFIG_GRID_INT_PAGESUME, rsPgin);
//                                            if (rsPgin[0].length > 0) {
//                                                for (TOKEN rsPgin1 : rsPgin[0]) {
//                                                    int param = db.BACK_CERT_RENEW_REQUEST_INSERT_EXPIRED(String.valueOf(rsPgin1.ID),
//                                                            EscapeUtils.CheckTextNull(USER_ID_LOG));
//                                                    if (param == 0) {
//                                                        intSuccess = intSuccess + 1;
//                                                    } else if (param == 1) {
//                                                        intExistoken = intExistoken + 1;
//                                                    } else {
//                                                        intNoExistToken = intNoExistToken + 1;
//                                                    }
//                                                }
//                                                sessionsa.setAttribute("RefreshTokenSess", "1");
//                                                strView = "0#" + String.valueOf(intSuccess)
//                                                        + "#" + String.valueOf(intExistoken)
//                                                        + "#" + String.valueOf(intNoExistToken);
//                                            } else {
//                                                strView = "1#0";
//                                            }
//                                        }
//                                    }
//                                } else {
//                                    strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "#0";
//                                }
//                                //</editor-fold>
//                                break;
//                            }
                            case "checkcsrf": {
                                //<editor-fold defaultstate="collapsed" desc="checkcsrf">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String TOKEN_VERSION = EscapeUtils.CheckTextNull(request.getParameter("TOKEN_VERSION"));
                                    String BranchID = EscapeUtils.CheckTextNull(request.getParameter("BranchID"));
                                    if(!"".equals(TOKEN_VERSION) && !"".equals(BranchID)) {
                                        boolean isAccess = false;
                                        BRANCH[][] rsBranch = (BRANCH[][]) request.getSession(false).getAttribute("sessTreeBranchSystemAgency");
                                        for (BRANCH item : rsBranch[0]) {
                                            if (String.valueOf(item.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                if (item.ID != 1) {
                                                    if(String.valueOf(item.ID).equals(BranchID)) {
                                                        isAccess = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        if(isAccess == true) {
                                            request.getSession(false).setAttribute("TOKEN_VERSION", TOKEN_VERSION);
                                            request.getSession(false).setAttribute("ImportBranchID", BranchID);
                                            strView = "0#0";
                                        } else {
                                            strView = "1#0";
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "TokenImport: Please enter all required");
                                        strView = "10#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "edittokenmultipleagent": {
                                //<editor-fold defaultstate="collapsed" desc="edittokenmultipleagent">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        String isCheckAll = request.getParameter("isCheckAll");
                                        String AGENT_IDMulti = EscapeUtils.CheckTextNull(request.getParameter("isAGENT_IDMulti"));
                                        if(!"".equals(AGENT_IDMulti)) {
                                            if (null != isCheckAll) {
                                                switch (isCheckAll) {
                                                    case "0":
                                                        String idValue = EscapeUtils.CheckTextNull(request.getParameter("idValue"));
                                                        idValue = idValue.substring(0, idValue.lastIndexOf(","));
                                                        String[] sPlitValue = idValue.replace(Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX, "").split(",");
                                                        if (sPlitValue.length > 0) {
                                                            for (String sPlitValue1 : sPlitValue) {
                                                                db.S_BO_TOKEN_UPDATE_BRANCH(sPlitValue1, AGENT_IDMulti, sUID_Update);
                                                            }
                                                            sessionsa.setAttribute("RefreshTokenImportSess", "1");
                                                            strView = "0#0";
                                                        } else {
                                                            strView = "1#0";
                                                        }
                                                        break;
                                                    case "1":
    //                                                    int[] pIa = new int[1];
    //                                                    int[] pIb = new int[1];
                                                        TOKEN[][] rsPgin = new TOKEN[1][];
                                                        String FromTOKEN_ID = (String) sessionsa.getAttribute("sessFromTOKEN_IDImport");
                                                        String ToTOKEN_ID = (String) sessionsa.getAttribute("sessToTOKEN_IDImport");
                                                        String AGENT_ID = (String) sessionsa.getAttribute("sessAGENT_IDImport");
                                                        String idSessIsDate = (String) sessionsa.getAttribute("idSessIsDateSImport");
                                                        String idSessIsToken = (String) sessionsa.getAttribute("idSessIsTokenSImport");
                                                        String FromDateValid = (String) sessionsa.getAttribute("sessFromDateValidImport");
                                                        String ToDateValid = (String) sessionsa.getAttribute("sessToDateValidImport");
                                                        String sessTreeArrayBranchID = (String) sessionsa.getAttribute("sessTreeArrayBranchIDSystem");
                                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
                                                            AGENT_ID = "";
                                                        }
                                                        if (!"1".equals(idSessIsDate)) {
                                                            FromDateValid = "";
                                                            ToDateValid = "";
                                                        }
                                                        if (!"1".equals(idSessIsToken)) {
                                                            FromTOKEN_ID = "";
                                                            ToTOKEN_ID = "";
                                                        }
    //                                                    if (null != idSessIsDate) {
    //                                                        switch (idSessIsDate) {
    //                                                            case "1":
    //                                                                FromDateValid = "";
    //                                                                ToDateValid = "";
    //                                                                break;
    //                                                            case "3":
    ////                                                                FromDateValid = "";
    ////                                                                ToDateValid = "";
    ////                                                                FromTOKEN_ID = "";
    ////                                                                ToTOKEN_ID = "";
    //                                                                break;
    //                                                            default:
    //                                                                FromTOKEN_ID = "";
    //                                                                ToTOKEN_ID = "";
    //                                                                break;
    //                                                        }
    //                                                    }
                                                        int sCount = db.S_BO_TOKEN_IMPORT_TOTAL(EscapeUtils.escapeHtmlSearch(FromDateValid),
                                                                EscapeUtils.escapeHtmlSearch(ToDateValid), EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                                                EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID), sessTreeArrayBranchID);
                                                        if (sCount > 0) {
                                                            db.S_BO_TOKEN_IMPORT_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid),
                                                                EscapeUtils.escapeHtmlSearch(ToDateValid),EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                                                EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                                                sessLanguageGlobal, rsPgin, Definitions.CONFIG_GRID_INT_PAGNO, sCount, sessTreeArrayBranchID);
                                                        }
    //                                                    String sIDMulti = "";
                                                        if (rsPgin[0].length > 0) {
    //                                                        for (TOKEN rsPgin1 : rsPgin[0]) {
    //                                                            sIDMulti = sIDMulti + String.valueOf(rsPgin1.ID) + ",";
    //                                                        }
    //                                                        sIDMulti = sIDMulti.substring(0, sIDMulti.lastIndexOf(","));
                                                            for (TOKEN rsPgin1 : rsPgin[0]) {
                                                                db.S_BO_TOKEN_UPDATE_BRANCH(String.valueOf(rsPgin1.ID),
                                                                        AGENT_IDMulti, sUID_Update);
                                                            }
                                                            sessionsa.setAttribute("RefreshTokenImportSess", "1");
                                                            strView = "0#0";
                                                        } else {
                                                            strView = "1#0";
                                                        }
                                                        break;
                                                }
                                            }
                                        } else {
                                            CommonFunction.LogErrorServlet(log, "TransferToken: Please enter all required");
                                            strView = "10#0";
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
                            case "checkcsrfforpushnotice": {
                                //<editor-fold defaultstate="collapsed" desc="checkcsrfforpushnotice">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
//                                    String vCollectEnabled = EscapeUtils.CheckTextNull(request.getParameter("vCollectEnabled"));
                                    String idPushText = EscapeUtils.CheckTextNull(request.getParameter("idPushText"));
                                    String idPushLink = EscapeUtils.CheckTextNull(request.getParameter("idPushLink"));
                                    request.getSession(false).setAttribute("CONTENT_PUSHNOTICE", idPushText);
                                    request.getSession(false).setAttribute("LINK_PUSHNOTICE", idPushLink);
//                                    request.getSession(false).setAttribute("SESS_COLLECT_ENABLED", vCollectEnabled);
                                    strView = "0#0";
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "checkcsrfforpushdisallowance": {
                                //<editor-fold defaultstate="collapsed" desc="checkcsrfforpushdisallowance">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
//                                    String vSetPush = EscapeUtils.CheckTextNull(request.getParameter("vSetPush"));
//                                    request.getSession(false).setAttribute("SESS_DISALLOWANCE_LIST", vSetPush);
                                    strView = "0#0";
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "checkcsrfforcertificateimport": {
                                //<editor-fold defaultstate="collapsed" desc="checkcsrfforcertificateimport">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String CERTIFICATION_AUTHORITY = EscapeUtils.CheckTextNull(request.getParameter("CERTIFICATION_AUTHORITY"));
                                    String CERTIFICATION_TYPE = EscapeUtils.CheckTextNull(request.getParameter("CERTIFICATION_TYPE"));
                                    request.getSession(false).setAttribute("SESS_CERTIMPORT_CERTIFICATION_AUTHORITY", CERTIFICATION_AUTHORITY);
                                    request.getSession(false).setAttribute("SESS_CERTIMPORT_CERTIFICATION_TYPE", CERTIFICATION_TYPE);
//                                    request.getSession(false).setAttribute("SESS_CERTIMPORT_CERTIFICATION_PURPOSE", CERTIFICATION_PURPOSE);
//                                    request.getSession(false).setAttribute("SESS_CERTIMPORT_PKI_FORMFACTOR", PKI_FORMFACTOR);
                                    strView = "0#0";
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "addreasonlocktoken": {
                                //<editor-fold defaultstate="collapsed" desc="addreasonlocktoken">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String idLockReason = EscapeUtils.CheckTextNull(request.getParameter("idLockReason"));
                                    String idActionToken = EscapeUtils.CheckTextNull(request.getParameter("idActionToken"));
                                    String vCOLOR_TEXT = EscapeUtils.CheckTextNull(request.getParameter("vCOLOR_TEXT"));
                                    String vCOLOR_BKGR = EscapeUtils.CheckTextNull(request.getParameter("vCOLOR_BKGR"));
                                    String vLINK_NOTICE = EscapeUtils.CheckTextNull(request.getParameter("vLINK_NOTICE"));
                                    String vNOTICE_INFO = EscapeUtils.CheckTextNull(request.getParameter("vNOTICE_INFO"));
                                    String vNAME_LINK = EscapeUtils.CheckTextNull(request.getParameter("vNAME_LINK"));
                                    String vLINK_VALUE = EscapeUtils.CheckTextNull(request.getParameter("vLINK_VALUE"));
                                    String vAllApplyEnabled = EscapeUtils.CheckTextNull(request.getParameter("vAllApplyEnabled"));
                                    String vIS_PUSH_NOTICE_SET_NO = EscapeUtils.CheckTextNull(request.getParameter("vIS_PUSH_NOTICE_SET_NO"));
                                    String vIS_MENU_LINK_SET_NO = EscapeUtils.CheckTextNull(request.getParameter("vIS_MENU_LINK_SET_NO"));
                                    String isFail = "0";
                                    if(!"".equals(idActionToken)) {
                                        if(idActionToken.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_PUSH_NOTFICATION))
                                        {
                                            if(vCOLOR_TEXT.length() <= 6 && vCOLOR_BKGR.length() <= 6 && vLINK_NOTICE.length() <= 512
                                                && vNOTICE_INFO.length() <= 1024)
                                            {
                                                isFail = "0";
                                            } else {
                                                isFail = "11";
                                                CommonFunction.LogErrorServlet(log, "ImportActionToken: Length is invalid");
                                            }
                                        } else if(idActionToken.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_MENU_LINK))
                                        {
                                            if(vNAME_LINK.length() <= 256 && vLINK_VALUE.length() <= 512)
                                            {
                                                isFail = "0";
                                            } else {
                                                isFail = "11";
                                                CommonFunction.LogErrorServlet(log, "ImportActionToken: Length is invalid");
                                            }
                                        } else if(idActionToken.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_LOCK))
                                        {
                                            vAllApplyEnabled = "0";
                                            if(idLockReason.length() <= 256)
                                            {
                                                isFail = "0";
                                            } else {
                                                isFail = "11";
                                                CommonFunction.LogErrorServlet(log, "ImportActionToken: Length is invalid");
                                            }
                                        } else if(idActionToken.equals(Definitions.CONFIG_TOKEN_ATTR_TYPE_CODE_LOCK))
                                        {
                                            vAllApplyEnabled = "0";
                                        }
                                    } else {
                                        isFail = "10";
                                        CommonFunction.LogErrorServlet(log, "ImportActionToken: Please enter all required");
                                    }
                                    if("0".equals(isFail)) {
                                        request.getSession(false).setAttribute("sessACTION_TOKEN", idActionToken);
                                        request.getSession(false).setAttribute("pLOCK_REASON", idLockReason);
                                        request.getSession(false).setAttribute("sessCOLOR_TEXT", vCOLOR_TEXT);
                                        request.getSession(false).setAttribute("sessCOLOR_BKGR", vCOLOR_BKGR);
                                        request.getSession(false).setAttribute("sessLINK_NOTICE", vLINK_NOTICE);
                                        request.getSession(false).setAttribute("sessNOTICE_INFO", vNOTICE_INFO);
                                        request.getSession(false).setAttribute("sessNAME_LINK", vNAME_LINK);
                                        request.getSession(false).setAttribute("sessLINK_VALUE", vLINK_VALUE);
                                        request.getSession(false).setAttribute("sessAllApplyEnabled", vAllApplyEnabled);
                                        request.getSession(false).setAttribute("sessPushDefaultEnabled", vIS_PUSH_NOTICE_SET_NO);
                                        request.getSession(false).setAttribute("sessMenuDefaultEnabled", vIS_MENU_LINK_SET_NO);
                                        strView = "0#0";
                                    } else {
                                        strView = isFail + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "checkcsrfforcollectprofile": {
                                //<editor-fold defaultstate="collapsed" desc="checkcsrfforcollectprofile">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
//                                    String vCollectEnabled = EscapeUtils.CheckTextNull(request.getParameter("vCollectEnabled"));
                                    String ControlType = EscapeUtils.CheckTextNull(request.getParameter("ControlType"));
                                    String idSessIsChoise = EscapeUtils.CheckTextNull(request.getParameter("idSessIsChoise"));
                                    request.getSession(false).setAttribute("sessControlType", ControlType);
                                    request.getSession(false).setAttribute("sessIdSessIsChoise", idSessIsChoise);
//                                    request.getSession(false).setAttribute("SESS_COLLECT_ENABLED", vCollectEnabled);
                                    strView = "0#0";
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "checkreregistercert": {
                                //<editor-fold defaultstate="collapsed" desc="checkreregistercert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ids = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    request.getSession(false).setAttribute("SessReRegisterCert", ids);
                                    strView = "0#0";
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "importtokenbundle": {
                                //<editor-fold defaultstate="collapsed" desc="importtokenbundle">
                                TOKEN_IMPORT[][] rsToken = (TOKEN_IMPORT[][]) request.getSession(false).getAttribute("sessTokenImportBundle");
                                String sUUID = CommonFunction.getUUIDV4();
                                if (rsToken != null && rsToken[0].length > 0) {
                                    db.TOKEN_IMPORT_BUNDLE(rsToken, sUUID);
                                    strView = "0#" + sUUID;
                                    request.getSession(false).setAttribute("sessTokenImportBundle", null);
                                } else {
                                    strView = "1#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "editpushnotice": {
                                //<editor-fold defaultstate="collapsed" desc="editpushnotice">
                                if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    String anticsrf = request.getParameter("CsrfToken");
                                    if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                        TOKEN[][] rs = new TOKEN[1][];
                                        String sID = EscapeUtils.escapeHtml(request.getParameter("ID"));
                                        db.S_BO_TOKEN_DETAIL(sID, rs);
                                        if (rs[0].length > 0) {
                                            String sJSON_PUSH_OLD = EscapeUtils.CheckTextNull(rs[0][0].PUSH_NOTICE_JSON);
                                            if(!"".equals(sJSON_PUSH_OLD)) {
                                                ObjectMapper objectMapper = new ObjectMapper();
                                                PUSH_TOKEN jsonParse = objectMapper.readValue(sJSON_PUSH_OLD, PUSH_TOKEN.class);
                                                PUSH_TOKEN itemPushNoContent = new PUSH_TOKEN();
                                                itemPushNoContent.PUSH_NOTICE_BGR_COLOR = jsonParse.PUSH_NOTICE_BGR_COLOR;
                                                itemPushNoContent.PUSH_NOTICE_CONTENT = jsonParse.PUSH_NOTICE_CONTENT;
                                                itemPushNoContent.PUSH_NOTICE_TEXT_COLOR = jsonParse.PUSH_NOTICE_TEXT_COLOR;
                                                itemPushNoContent.PUSH_NOTICE_URL = jsonParse.PUSH_NOTICE_URL;
                                                String sJSON_PUSH_NEW = objectMapper.writeValueAsString(itemPushNoContent);
                                                db.S_BO_TOKEN_UPDATE_PUSH_NOTICE_JSON(sID, sJSON_PUSH_NEW, sUID_Update);
                                            }
                                        }
                                        strView = "0#0";
                                        sessionsa.setAttribute("RefreshTokenSess", "1");
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                    }
                                } else {
                                    strView = "1#0";
                                }
                                //</editor-fold>
                                break;
                            }
//                            case "edittokenmultiple": {
//                                //<editor-fold defaultstate="collapsed" desc="edittokenmultiple">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
//                                    String sUID_Update = sessionsa.getAttribute("UserID").toString().trim();
//                                    String isCheckAll = request.getParameter("isCheckAll");
//                                    String LINK_NOTICE = request.getParameter("LINK_NOTICE");
//                                    String COLOR_TEXT = request.getParameter("COLOR_TEXT");
//                                    String COLOR_BKGR = request.getParameter("COLOR_BKGR");
//                                    String NOTICE_INFO = request.getParameter("NOTICE_INFO");
//                                    String NAME_LINK = request.getParameter("NAME_LINK");
//                                    String LINK_VALUE = request.getParameter("LINK_VALUE");
//                                    String DATE_LIMIT = request.getParameter("DATE_LIMIT");
//                                    String IS_PUSH_NOTICE = request.getParameter("IS_PUSH_NOTICE");
//                                    String IS_MENU_LINK = request.getParameter("IS_MENU_LINK");
//                                    String IS_LOCK = request.getParameter("IS_LOCK");
//                                    String IS_UNLOCK = request.getParameter("IS_UNLOCK");
//                                    String IS_INITIALIZE = request.getParameter("IS_INITIALIZE");
//                                    String IS_CHANGE_SOPIN = request.getParameter("IS_CHANGE_SOPIN");
////                                    String IS_INFORMATION = request.getParameter("IS_INFORMATION");
//                                    String ACTIVE_FLAG = request.getParameter("ACTIVE_FLAG");
//                                    if ("".equals(IS_PUSH_NOTICE)) {
//                                        COLOR_TEXT = "";
//                                        COLOR_BKGR = "";
//                                        LINK_NOTICE = "";
//                                        NOTICE_INFO = "";
//                                    }
//                                    if ("".equals(IS_MENU_LINK)) {
//                                        NAME_LINK = "";
//                                        LINK_VALUE = "";
//                                    }
//                                    if (null != isCheckAll) {
//                                        switch (isCheckAll) {
//                                            case "0":
//                                                String idValue = EscapeUtils.CheckTextNull(request.getParameter("idValue"));
//                                                String[] sPlitValue = idValue.split(",");
//                                                if (!"".equals(idValue) && sPlitValue.length > 0) {
//                                                    db.BACK_TOKEN_UPDATE_MULTIPLE(idValue.replace(Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX, ""),
//                                                            EscapeUtils.escapeHtml(IS_LOCK),
//                                                            EscapeUtils.escapeHtml(IS_UNLOCK), EscapeUtils.escapeHtml(ACTIVE_FLAG),
//                                                            EscapeUtils.escapeHtml(sUID_Update), EscapeUtils.escapeHtml(IS_INITIALIZE),
//                                                            EscapeUtils.escapeHtml(IS_CHANGE_SOPIN), EscapeUtils.escapeHtml(IS_PUSH_NOTICE),
//                                                            EscapeUtils.escapeHtml(IS_MENU_LINK), EscapeUtils.escapeHtml(NAME_LINK),
//                                                            EscapeUtils.escapeHtml(LINK_VALUE), EscapeUtils.escapeHtml(NOTICE_INFO),
//                                                            EscapeUtils.escapeHtml(LINK_NOTICE), EscapeUtils.escapeHtml(COLOR_TEXT),
//                                                            EscapeUtils.escapeHtml(COLOR_BKGR), EscapeUtils.escapeHtml(IS_INFORMATION),
//                                                            EscapeUtils.escapeHtml(DATE_LIMIT), "");
//                                                    sessionsa.setAttribute("RefreshTokenSess", "1");
//                                                    strView = "0#0";
//                                                } else {
//                                                    strView = "1#0";
//                                                }
//                                                break;
//                                            case "1":
//                                                TOKEN[][] rsPgin = new TOKEN[1][];
//                                                String idSessIsDate = (String) sessionsa.getAttribute("idSessIsDateS");
//                                                String FromDateValid = (String) sessionsa.getAttribute("sessFromDateValid");
//                                                String ToDateValid = (String) sessionsa.getAttribute("sessToDateValid");
//                                                String FromDateExpire = (String) sessionsa.getAttribute("sessFromDateExpire");
//                                                String ToDateExpire = (String) sessionsa.getAttribute("sessToDateExpire");
//                                                String TOKEN_ID = (String) sessionsa.getAttribute("sessTOKEN_ID");
//                                                String TAX_CODE = (String) sessionsa.getAttribute("sessTAX_CODE");
//                                                String SERIAL_NUMBER_CERT = (String) sessionsa.getAttribute("sessSERIAL_NUMBER_CERT");
//                                                String SUBJECT_NAME = (String) sessionsa.getAttribute("sessSUBJECT_NAME");
//                                                String MOBILE_NO = (String) sessionsa.getAttribute("sessMOBILE_NO");
//                                                String ADDRESS = (String) sessionsa.getAttribute("sessADDRESS");
//                                                String EMAIL = (String) sessionsa.getAttribute("sessEMAIL");
//                                                String LOCATION = (String) sessionsa.getAttribute("sessLOCATION");
//                                                String P_ID = (String) sessionsa.getAttribute("sessP_ID");
//                                                String STATE = (String) sessionsa.getAttribute("sessSTATE");
//                                                String NAME_CN = (String) sessionsa.getAttribute("sessNAME_CN");
//                                                String AGENT_ID = (String) sessionsa.getAttribute("sessAGENT_ID");
//                                                String IS_EXPIRED_DT_AGREE = (String) sessionsa.getAttribute("sessIS_EXPIRED_DT_AGREE");
//                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
//                                                    AGENT_ID = "";
//                                                }
//                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(IS_EXPIRED_DT_AGREE)) {
//                                                    IS_EXPIRED_DT_AGREE = "";
//                                                }
//                                                if (!Definitions.CONFIG_AGENT_ROOT.equals(AGENT_ID_LOG)) {
//                                                    AGENT_ID = SessUserAgentID;
//                                                } else {
//                                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
//                                                        AGENT_ID = "";
//                                                    }
//                                                }
//                                                if (null != idSessIsDate) {
//                                                    switch (idSessIsDate) {
//                                                        case "1":
//                                                            FromDateValid = "";
//                                                            ToDateValid = "";
//                                                            break;
//                                                        case "3":
//                                                            FromDateValid = "";
//                                                            ToDateValid = "";
//                                                            FromDateExpire = "";
//                                                            ToDateExpire = "";
//                                                            break;
//                                                        case "4":
//                                                            break;
//                                                        default:
//                                                            FromDateExpire = "";
//                                                            ToDateExpire = "";
//                                                            break;
//                                                    }
//                                                }
//                                                int sCount = db.BACK_TOKEN_TOTAL(EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(TAX_CODE),
//                                                        EscapeUtils.escapeHtmlSearch(SERIAL_NUMBER_CERT), EscapeUtils.escapeHtmlSearch(SUBJECT_NAME),
//                                                        EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
//                                                        EscapeUtils.escapeHtmlSearch(FromDateExpire), EscapeUtils.escapeHtmlSearch(ToDateExpire),
//                                                        EscapeUtils.escapeHtmlSearch(MOBILE_NO),
//                                                        EscapeUtils.escapeHtmlSearch(ADDRESS), EscapeUtils.escapeHtmlSearch(EMAIL),
//                                                        EscapeUtils.escapeHtmlSearch(LOCATION), EscapeUtils.escapeHtmlSearch(P_ID),
//                                                        EscapeUtils.escapeHtmlSearch(STATE), EscapeUtils.escapeHtmlSearch(NAME_CN),
//                                                        EscapeUtils.escapeHtmlSearch(AGENT_ID), EscapeUtils.escapeHtmlSearch(IS_EXPIRED_DT_AGREE));
//                                                if (sCount > 0) {
//                                                    db.BACK_TOKEN_LIST(EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(TAX_CODE),
//                                                            EscapeUtils.escapeHtmlSearch(SERIAL_NUMBER_CERT), EscapeUtils.escapeHtmlSearch(SUBJECT_NAME),
//                                                            EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
//                                                            EscapeUtils.escapeHtmlSearch(FromDateExpire), EscapeUtils.escapeHtmlSearch(ToDateExpire),
//                                                            EscapeUtils.escapeHtmlSearch(MOBILE_NO),
//                                                            EscapeUtils.escapeHtmlSearch(ADDRESS), EscapeUtils.escapeHtmlSearch(EMAIL),
//                                                            EscapeUtils.escapeHtmlSearch(LOCATION), EscapeUtils.escapeHtmlSearch(P_ID),
//                                                            EscapeUtils.escapeHtmlSearch(STATE), EscapeUtils.escapeHtmlSearch(NAME_CN),
//                                                            EscapeUtils.escapeHtmlSearch(AGENT_ID), EscapeUtils.escapeHtmlSearch(IS_EXPIRED_DT_AGREE),
//                                                            Definitions.CONFIG_GRID_INT_PAGNO, Definitions.CONFIG_GRID_INT_PAGESUME, rsPgin);
//                                                }
//                                                String sIDMulti = "";
//                                                if (rsPgin[0].length > 0) {
//                                                    for (TOKEN rsPgin1 : rsPgin[0]) {
//                                                        sIDMulti = sIDMulti + String.valueOf(rsPgin1.ID) + ",";
//                                                    }
////                                                        sIDMulti = sIDMulti.substring(0, sIDMulti.lastIndexOf(","));
//                                                    db.BACK_TOKEN_UPDATE_MULTIPLE(sIDMulti, EscapeUtils.escapeHtml(IS_LOCK),
//                                                            EscapeUtils.escapeHtml(IS_UNLOCK), EscapeUtils.escapeHtml(ACTIVE_FLAG),
//                                                            EscapeUtils.escapeHtml(sUID_Update), EscapeUtils.escapeHtml(IS_INITIALIZE),
//                                                            EscapeUtils.escapeHtml(IS_CHANGE_SOPIN), EscapeUtils.escapeHtml(IS_PUSH_NOTICE),
//                                                            EscapeUtils.escapeHtml(IS_MENU_LINK), EscapeUtils.escapeHtml(NAME_LINK),
//                                                            EscapeUtils.escapeHtml(LINK_VALUE), EscapeUtils.escapeHtml(NOTICE_INFO),
//                                                            EscapeUtils.escapeHtml(LINK_NOTICE), EscapeUtils.escapeHtml(COLOR_TEXT),
//                                                            EscapeUtils.escapeHtml(COLOR_BKGR), EscapeUtils.escapeHtml(IS_INFORMATION),
//                                                            EscapeUtils.escapeHtml(DATE_LIMIT), "");
//                                                    sessionsa.setAttribute("RefreshTokenSess", "1");
//                                                    strView = "0#0";
//                                                } else {
//                                                    strView = "1#0";
//                                                }
//                                                break;
//                                        }
//                                    }
//                                } else {
//                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
//                                }
//                                //</editor-fold>
//                                break;
//                            }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
