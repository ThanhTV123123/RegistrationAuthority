package vn.ra.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.ra.process.ConnectDatabase;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.utility.Definitions;
import org.apache.log4j.Logger;
import vn.mobileid.fms.client.JCRConfig;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PUSH_TOKEN;
import vn.ra.object.PUSH_TOKEN_ATTR;
import vn.ra.object.PUSH_TOKEN_EDITED;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectConnector;
import vn.ra.process.JackRabbitCommon;
import vn.ra.process.SessionPushNotification;
import vn.ra.utility.PropertiesContent;

/**
 *
 * @author THANH
 */
public class PolicyCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final Logger log = Logger.getLogger(PolicyCommon.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException, Exception {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession sessionsa = request.getSession(false);
            ConnectDatabase db = new ConnectDatabase();
            String strView = "";
            try {
                int sOutInner;
                if (sessionsa != null) {
                    String strInnerUsername = sessionsa.getAttribute("sUserID").toString().trim();
                    String strInnerSessionKey = sessionsa.getAttribute("sesSessKey").toString().trim();
                    sOutInner = db.CheckIsLoginOnline(strInnerUsername, strInnerSessionKey);
                } else {
                    sOutInner = 2;
                }
                if (sOutInner == 1) {
                    String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                    String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    String idParam = request.getParameter("idParam");
                    if (null != idParam) {
                        switch (idParam) {
                            case "editpolicyconfig_edited": {
                                //<editor-fold defaultstate="collapsed" desc="editpolicyconfig_edited">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sValue = request.getParameter("vValue");
                                    String sSplit[] = sValue.split("@@@");
                                    String sID_PUSH_NOTICE_JSON = "";
                                    String sVALUE_PUSH_NOTICE_JSON = "";
                                    String sID_NOTIFICATION_RECORD_COLLECTION = "";
                                    String sVALUE_NOTIFICATION_RECORD_COLLECTION = "";
                                    String sIS_UPDATE = "";
                                    String sIS_EMPTY = "0";
                                    for (String sSplit1 : sSplit) {
                                        sIS_UPDATE = "1";
                                        if (sSplit1.split("###")[2].contains(Definitions.CONFIG_POLICY_MINETYPE_FO_DEFAULT_PUSH_NOTICE_JSON)) {
                                            if ("".equals(sID_PUSH_NOTICE_JSON)) {
                                                sID_PUSH_NOTICE_JSON = sSplit1.split("###")[0];
                                            }
                                            if(!"".equals(sID_PUSH_NOTICE_JSON)) {
                                                if(!"".equals(sSplit1.split("###")[1].trim())) {
                                                    sVALUE_PUSH_NOTICE_JSON = sVALUE_PUSH_NOTICE_JSON
                                                        + sID_PUSH_NOTICE_JSON + "###" + sSplit1.split("###")[0].trim().replace(sID_PUSH_NOTICE_JSON+"_", "") + "###" + sSplit1.split("###")[1].trim() + "@@@";
                                                } else {
                                                    sIS_EMPTY = "1";
                                                    break;
                                                }
                                            }
                                        } else if (sSplit1.split("###")[2].contains(Definitions.CONFIG_POLICY_MINETYPE_FO_PUSH_NOTIFICATION_RECORD_COLLECTION)) {
                                            if ("".equals(sID_NOTIFICATION_RECORD_COLLECTION)) {
                                                sID_NOTIFICATION_RECORD_COLLECTION = sSplit1.split("###")[0];
                                            }
                                            if(!"".equals(sID_NOTIFICATION_RECORD_COLLECTION)) {
                                                sVALUE_NOTIFICATION_RECORD_COLLECTION = sVALUE_NOTIFICATION_RECORD_COLLECTION
                                                    + sID_NOTIFICATION_RECORD_COLLECTION + "###" + sSplit1.split("###")[0].trim().replace(sID_NOTIFICATION_RECORD_COLLECTION+"_", "") + "###" + sSplit1.split("###")[1].trim() + "@@@";
                                            }
                                        }
                                        else if(sSplit1.split("###")[0].equals(Definitions.CONFIG_POLICY_ATTR_BO_DMS_PROPERTIES_CURRENT_ID))
                                        {
                                            String sJRBConfig_Old = request.getSession(false).getAttribute("sessBO_DMS_PROPERTIES_CURRENT").toString().trim();
                                            String sJRBConfig_New = sSplit1.split("###")[1].trim();
                                            String sJRB_PassAdmin_Old = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_ADMIN_PASSWORD);
                                            String sJRB_PassAdmin_New = PropertiesContent.getPropertiesContentKey(sJRBConfig_New, Definitions.CONFIG_JACK_RABBIT_ADMIN_PASSWORD);
                                            String sJRB_UserAdmin_Old = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_ADMINID);
                                            String sJRB_UserAdmin_New = PropertiesContent.getPropertiesContentKey(sJRBConfig_New, Definitions.CONFIG_JACK_RABBIT_ADMINID);
                                            String sJRB_PassUser_Old = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                            String sJRB_PassUser_New = PropertiesContent.getPropertiesContentKey(sJRBConfig_New, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                            String sJRB_UserUser_Old = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_USERID);
                                            String sJRB_UserUser_New = PropertiesContent.getPropertiesContentKey(sJRBConfig_New, Definitions.CONFIG_JACK_RABBIT_USERID);
                                            String sJRB_WorkSpace_Old = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                            String sJRB_WorkSpace_New = PropertiesContent.getPropertiesContentKey(sJRBConfig_New, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                            if(!sJRB_PassAdmin_New.equals(sJRB_PassAdmin_Old))
                                            {
                                                //sJRBConfig_New = PropertiesContent.updatePropertiesContent(sJRBConfig_New, Definitions.CONFIG_JACK_RABBIT_ADMIN_PASSWORD, sJRB_PassAdmin_Old);
                                                // call change pass admin
//                                                String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_HOST);
//                                                String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_USERID);
//                                                String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
//                                                String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
//                                                String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
//                                                String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
//                                                String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
//                                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
//                                                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//                                                boolean boChangeAdmin = JackRabbitCommon.changePasswordAdmin(jcrConfig, sJRB_PassAdmin_Old, sJRB_PassAdmin_New);
//                                                CommonFunction.LogDebugString(log, "changePasswordAdmin", String.valueOf(boChangeAdmin));
                                            }
                                            if(!sJRB_PassUser_New.equals(sJRB_PassUser_Old))
                                            {
                                                //sJRBConfig_New = PropertiesContent.updatePropertiesContent(sJRBConfig_New, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD, sJRB_PassUser_Old);
                                                // call change pass user
//                                                String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_HOST);
//                                                String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_USERID);
//                                                String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
//                                                String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
//                                                String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
//                                                String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
//                                                String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig_Old, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
//                                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
//                                                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//                                                boolean boChangeWorkSpace = JackRabbitCommon.changePasswordWorkSpace(jcrConfig, sJRB_WorkSpace_Old, sJRB_UserUser_Old, sJRB_PassUser_Old, sJRB_UserUser_Old, sJRB_PassUser_New);
//                                                CommonFunction.LogDebugString(log, "changePasswordWorkSpace", String.valueOf(boChangeWorkSpace));
                                            }
                                            if(!sJRB_UserAdmin_New.equals(sJRB_UserAdmin_Old))
                                            {
                                                // return username admin for config
                                                //sJRBConfig_New = PropertiesContent.updatePropertiesContent(sJRBConfig_New, Definitions.CONFIG_JACK_RABBIT_ADMINID, sJRB_UserAdmin_Old);
                                            }
                                            if(!sJRB_WorkSpace_New.equals(sJRB_WorkSpace_Old))
                                            {
                                                // return workspacce name
                                                //sJRBConfig_New = PropertiesContent.updatePropertiesContent(sJRBConfig_New, Definitions.CONFIG_JACK_RABBIT_WORKSPACE, sJRB_WorkSpace_Old);
                                            }
                                            if(!sJRB_UserUser_New.equals(sJRB_UserUser_Old))
                                            {
                                                // return username user for config
                                                //sJRBConfig_New = PropertiesContent.updatePropertiesContent(sJRBConfig_New, Definitions.CONFIG_JACK_RABBIT_USERID, sJRB_UserUser_Old);
                                            }
                                            if(!"".equals(sJRBConfig_New)) {
                                                db.S_BO_GENERAL_POLICY_UPDATE(Definitions.CONFIG_POLICY_ATTR_BO_DMS_PROPERTIES_CURRENT_ID, sJRBConfig_New, loginUID);
                                            } else {
                                                sIS_EMPTY = "1";
                                                break;
                                            }
                                        } else {
                                            String sValueSQL = sSplit1.split("###")[1];
//                                            if(!"".equals(sValueSQL.trim()))
//                                            {
//                                                if (sSplit1.split("###")[3].equals(Definitions.CONFIG_POLICY_BO_WEBSITE_PRIVACY_POLICY)
//                                                    || sSplit1.split("###")[3].equals(Definitions.CONFIG_POLICY_BO_WEBSITE_TERMS_CONDITIONS)) {
//                                                    System.out.println("PRIVACY_POLICY-TERMS_CONDITIONS: " + sSplit1.split("###")[3]);
//                                                    db.S_BO_GENERAL_POLICY_UPDATE_BLOB(sSplit1.split("###")[0], sValueSQL, loginUID);
//                                                } else {
                                                if (sSplit1.split("###")[2].equals(Definitions.CONFIG_POLICY_MINETYPE_DATETIME)) {
                                                    sValueSQL = CommonFunction.ConvertDateUpdatePolicy(sValueSQL);
                                                }
                                                if (sSplit1.split("###")[2].equals(Definitions.CONFIG_POLICY_MIMETYPE_NUMERIC)) {
                                                    sValueSQL = sValueSQL.replace(",", "");
                                                }
                                                db.S_BO_GENERAL_POLICY_UPDATE(sSplit1.split("###")[0], sValueSQL, loginUID);
                                                if (sSplit1.split("###")[3].equals(Definitions.CONFIG_POLICY_FO_ALLOW_MULTIPLE_CERTIFICATES_PER_TOKEN)) {
                                                    String sBoolean = "1".equals(sValueSQL) ? "TRUE" : "FALSE";
                                                    CommonFunction.LogDebugString(log, "UPDATE ALLOW_MULTIPLE_CERTIFICATES_PER_TOKEN", sBoolean + " ; USER: " + loginUID);
                                                }
//                                                }
//                                            } else {
//                                                sIS_EMPTY = "1";
//                                                break;
//                                            }
                                        }
                                    }
                                    // PUSH_NOTICE_DEFAULT_JSON
                                    if("0".equals(sIS_EMPTY))
                                    {
                                        if (!"".equals(sVALUE_PUSH_NOTICE_JSON)) {
                                            String sSplitPUSH[] = sVALUE_PUSH_NOTICE_JSON.split("@@@");
                                            SessionPushNotification cartPush = (SessionPushNotification) request.getSession(false).getAttribute("sessPushNotificationList");
                                            if(cartPush != null)
                                            {
                                                for (String sSplitPUSH1 : sSplitPUSH) {
                                                    PUSH_TOKEN_ATTR mhFunc = new PUSH_TOKEN_ATTR();
                                                    mhFunc.name = sSplitPUSH1.split("###")[1].trim();
                                                    mhFunc.value = sSplitPUSH1.split("###")[2].trim();
                                                    cartPush.UpdateSessionPushNotification(mhFunc);
                                                }
                                                PUSH_TOKEN_EDITED certificationAttributes123 = null;
                                                ArrayList<PUSH_TOKEN_ATTR> ds = cartPush.getGH();
                                                ArrayList<PUSH_TOKEN_EDITED.Attribute> tempListParse = new ArrayList<>();
                                                for (PUSH_TOKEN_ATTR mhIP : ds) {
                                                    PUSH_TOKEN_EDITED.Attribute certificationAttributes = new PUSH_TOKEN_EDITED.Attribute();
                                                    certificationAttributes.setName(mhIP.name);
                                                    certificationAttributes.setValue(mhIP.value);
                                                    certificationAttributes.setMimetype(mhIP.mimetype);
                                                    certificationAttributes.setRemark(mhIP.remark);
                                                    certificationAttributes.setRemarkEn(mhIP.remarkEn);
                                                    tempListParse.add(certificationAttributes);
                                                }
                                                certificationAttributes123 = new PUSH_TOKEN_EDITED();
                                                certificationAttributes123.setAttributes(tempListParse);
                                                ObjectMapper objectMapper = new ObjectMapper();
                                                String strJSONPUSH = objectMapper.writeValueAsString(certificationAttributes123);
    //                                            CommonFunction.LogDebugString(log, "PUSH_NOTICE_DEFAULT_JSON", strJSONPUSH);
                                                db.S_BO_GENERAL_POLICY_UPDATE(sID_PUSH_NOTICE_JSON, strJSONPUSH, loginUID);
                                                request.getSession(false).setAttribute("sessPushNotificationList", null);
                                            }
                                        }
                                        // PUSH_NOTIFICATION_RECORD_COLLECTION
                                        if (!"".equals(sVALUE_NOTIFICATION_RECORD_COLLECTION)) {
                                            String sSplitPUSH[] = sVALUE_NOTIFICATION_RECORD_COLLECTION.split("@@@");
                                            SessionPushNotification cartPush = (SessionPushNotification) request.getSession(false).getAttribute("sessSysDisallowanceList");
                                            if(cartPush != null)
                                            {
                                                for (String sSplitPUSH1 : sSplitPUSH) {
                                                    PUSH_TOKEN_ATTR mhFunc = new PUSH_TOKEN_ATTR();
                                                    mhFunc.name = sSplitPUSH1.split("###")[1].trim();
                                                    mhFunc.value = sSplitPUSH1.split("###")[2].trim();
                                                    cartPush.UpdateSessionPushNotification(mhFunc);
                                                }
                                                PUSH_TOKEN_EDITED certificationAttributes123 = null;
                                                ArrayList<PUSH_TOKEN_ATTR> ds = cartPush.getGH();
                                                ArrayList<PUSH_TOKEN_EDITED.Attribute> tempListParse = new ArrayList<>();
                                                for (PUSH_TOKEN_ATTR mhIP : ds) {
                                                    PUSH_TOKEN_EDITED.Attribute certificationAttributes = new PUSH_TOKEN_EDITED.Attribute();
                                                    certificationAttributes.setName(mhIP.name);
                                                    certificationAttributes.setValue(mhIP.value);
                                                    certificationAttributes.setMimetype(mhIP.mimetype);
                                                    certificationAttributes.setRemark(mhIP.remark);
                                                    certificationAttributes.setRemarkEn(mhIP.remarkEn);
                                                    tempListParse.add(certificationAttributes);
                                                }
                                                certificationAttributes123 = new PUSH_TOKEN_EDITED();
                                                certificationAttributes123.setAttributes(tempListParse);
                                                ObjectMapper objectMapper = new ObjectMapper();
                                                String strJSONPUSH = objectMapper.writeValueAsString(certificationAttributes123);
    //                                            CommonFunction.LogDebugString(log, "PUSH_NOTIFICATION_RECORD_COLLECTION", strJSONPUSH);
                                                db.S_BO_GENERAL_POLICY_UPDATE(sID_NOTIFICATION_RECORD_COLLECTION, strJSONPUSH, loginUID);
                                                request.getSession(false).setAttribute("sessPushNotificationList", null);
                                            }
                                        }
                                        strView = "0#0";
                                    } else {
                                        strView = "10#0";
                                    }
                                    if ("1".equals(sIS_UPDATE)) {
                                        //<editor-fold defaultstate="collapsed" desc="### RELOAD POLICY PORTAL">
                                        GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
                                        db.S_BO_GENERAL_POLICY_LIST(sessLanguage, rsPolicy);
                                        String strTimeOut = Definitions.CONFIG_SYSTEM_TIMEOUT;
                                        int sTimeOut = 60;
                                        int sACTIVATION_MAX_COUNTER = 5;
                                        if (rsPolicy[0].length > 0) {
                                            request.getSession(false).setAttribute("sessGeneralPolicy_System", rsPolicy);
                                            for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_TIMEOUT)) {
                                                    sTimeOut = Integer.parseInt(rsPolicy1.VALUE);
                                                    strTimeOut = String.valueOf(sTimeOut * 60);
                                                }
                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_ACTIVATION_MAX_COUNTER)) {
                                                    sACTIVATION_MAX_COUNTER = Integer.parseInt(rsPolicy1.VALUE);
                                                }
                                            }
                                            request.getSession().setMaxInactiveInterval(sTimeOut * 60);
                                        } else {
                                            request.getSession().setMaxInactiveInterval(sTimeOut * 60);
                                        }
                                        request.getSession(false).setAttribute("SessGlobalTimeOut", strTimeOut);
                                        request.getSession(false).setAttribute("SessGlobalActivation_Max_Code", sACTIVATION_MAX_COUNTER);
                                        //</editor-fold>
                                        
                                        int[] intResCall = new int[1];
                                        String[] sResCall = new String[1];
                                        ConnectConnector.ReloadParameters(intResCall, sResCall);
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "editpolicyconfig": {
                                //<editor-fold defaultstate="collapsed" desc="editpolicyconfig">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
//                                    String sValue = request.getParameter("vValue");
//                                    CommonFunction.LogDebugString(log, "String sValue", sValue);
//                                    String sSplit[] = sValue.split("@@@");
//                                    String sID_PUSH_NOTICE_JSON = "";
//                                    String sVALUE_PUSH_NOTICE_JSON = "";
//                                    String sIS_UPDATE = "";
//                                    for (String sSplit1 : sSplit) {
//                                        sIS_UPDATE = "1";
//                                        if (sSplit1.split("###")[0].contains(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_TEXT_COLOR)
//                                                || sSplit1.split("###")[0].contains(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_BGR_COLOR)
//                                                || sSplit1.split("###")[0].contains(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_CONTENT)
//                                                || sSplit1.split("###")[0].contains(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_URL)) {
//                                            if ("".equals(sID_PUSH_NOTICE_JSON)) {
//                                                sID_PUSH_NOTICE_JSON = sSplit1.split("###")[0].replace("_" + Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_TEXT_COLOR, "");
//                                            }
//                                            if (sSplit1.split("###")[0].contains(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_TEXT_COLOR)) {
//                                                sVALUE_PUSH_NOTICE_JSON = sVALUE_PUSH_NOTICE_JSON
//                                                        + Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_TEXT_COLOR + "###" + sSplit1.split("###")[1].trim() + "@@@";
//                                            }
//                                            if (sSplit1.split("###")[0].contains(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_BGR_COLOR)) {
//                                                sVALUE_PUSH_NOTICE_JSON = sVALUE_PUSH_NOTICE_JSON
//                                                        + Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_BGR_COLOR + "###" + sSplit1.split("###")[1].trim() + "@@@";
//                                            }
//                                            if (sSplit1.split("###")[0].contains(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_CONTENT)) {
//                                                sVALUE_PUSH_NOTICE_JSON = sVALUE_PUSH_NOTICE_JSON
//                                                        + Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_CONTENT + "###" + sSplit1.split("###")[1].trim() + "@@@";
//                                            }
//                                            if (sSplit1.split("###")[0].contains(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_URL)) {
//                                                sVALUE_PUSH_NOTICE_JSON = sVALUE_PUSH_NOTICE_JSON
//                                                        + Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_URL + "###" + sSplit1.split("###")[1].trim() + "@@@";
//                                            }
//                                        } else {
//                                            String sValueSQL = sSplit1.split("###")[1];
//                                            if (sSplit1.split("###")[2].equals(Definitions.CONFIG_POLICY_MINETYPE_DATETIME)) {
//                                                sValueSQL = CommonFunction.ConvertDateUpdatePolicy(sValueSQL);
//                                            }
//                                            if (sSplit1.split("###")[2].equals(Definitions.CONFIG_POLICY_MIMETYPE_NUMERIC)) {
//                                                sValueSQL = sValueSQL.replace(",", "");
//                                            }
//                                            db.S_BO_GENERAL_POLICY_UPDATE(sSplit1.split("###")[0], sValueSQL, loginUID);
//                                        }
//                                    }
//                                    CommonFunction.LogDebugString(log, "STRING_PUSH_NOTICE_JSON", sVALUE_PUSH_NOTICE_JSON);
//                                    if (!"".equals(sVALUE_PUSH_NOTICE_JSON)) {
//                                        ObjectMapper objectMapper = new ObjectMapper();
//                                        String sSplitPUSH[] = sVALUE_PUSH_NOTICE_JSON.split("@@@");
//                                        PUSH_TOKEN itemParsePush = new PUSH_TOKEN();
//                                        for (String sSplitPUSH1 : sSplitPUSH) {
//                                            if (!"".equals(sSplitPUSH1.split("###")[0].trim())) {
//                                                if (sSplitPUSH1.split("###")[0].trim().equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_TEXT_COLOR)) {
//                                                    itemParsePush.PUSH_NOTICE_TEXT_COLOR = sSplitPUSH1.split("###")[1].trim();
//                                                }
//                                                if (sSplitPUSH1.split("###")[0].trim().equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_BGR_COLOR)) {
//                                                    itemParsePush.PUSH_NOTICE_BGR_COLOR = sSplitPUSH1.split("###")[1].trim();
//                                                }
//                                                if (sSplitPUSH1.split("###")[0].trim().equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_CONTENT)) {
//                                                    itemParsePush.PUSH_NOTICE_CONTENT = sSplitPUSH1.split("###")[1].trim();
//                                                }
//                                                if (sSplitPUSH1.split("###")[0].trim().equals(Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_URL)) {
//                                                    itemParsePush.PUSH_NOTICE_URL = sSplitPUSH1.split("###")[1].trim();
//                                                }
//                                            }
//                                        }
//                                        String strJSONPUSH = objectMapper.writeValueAsString(itemParsePush);
//                                        CommonFunction.LogDebugString(log, "VALUE_PUSH_NOTICE_JSON", strJSONPUSH);
//                                        db.S_BO_GENERAL_POLICY_UPDATE(sID_PUSH_NOTICE_JSON, strJSONPUSH, loginUID);
//                                    }
//                                    if ("1".equals(sIS_UPDATE)) {
//                                        int[] intResCall = new int[1];
//                                        String[] sResCall = new String[1];
//                                        ConnectConnector.ReloadParameters(intResCall, sResCall);
//                                    }
//                                    strView = "0#0";
//                                } else {
//                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
//                                }
//                                break;
                                //</editor-fold>
                            }
                            case "editpolicytype": {
                                //<editor-fold defaultstate="collapsed" desc="editpolicytype">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String pID = request.getParameter("pID");
                                    String pEnable = request.getParameter("pEnable");
                                    String pRequired = request.getParameter("pRequired");
                                    String pName = request.getParameter("pName");
                                    String pRemark = request.getParameter("pRemark");
                                    String pRemark_EN = request.getParameter("pRemark_EN");
                                    String pMIMETYPE = request.getParameter("pMIMETYPE");
                                    String pFRONT_OFFICE_ENABLED = request.getParameter("pIsModule");
                                    String sParam = db.S_BO_GENERAL_POLICY_ATTR_TYPE_UPDATE(pID, pEnable, pRequired,
                                            pName, pRemark, pRemark_EN, pFRONT_OFFICE_ENABLED, pMIMETYPE, loginUID);
                                    strView = sParam + "#0" + sParam;
                                    request.getSession(false).setAttribute("SessRefreshPolicyType", "1");
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "addpolicytype": {
                                //<editor-fold defaultstate="collapsed" desc="addpolicytype">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String pName = request.getParameter("pName");
                                    String pRequired = request.getParameter("pRequired");
                                    String pValue = request.getParameter("pValue");
                                    String pRemark = request.getParameter("pRemark");
                                    String pRemark_EN = request.getParameter("pRemark_EN");
                                    String pMIMETYPE = request.getParameter("pMIMETYPE");
                                    String pFRONT_OFFICE_ENABLED = request.getParameter("pIsModule");
                                    String sParam = db.S_BO_GENERAL_POLICY_ATTR_TYPE_INSERT(pRequired, pName,
                                            pRemark, pValue, pRemark_EN, pFRONT_OFFICE_ENABLED, pMIMETYPE, loginUID);
                                    strView = sParam + "#0" + sParam;
                                } else {
                                    request.getSession(false).setAttribute("SessRefreshPolicyType", "1");
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                        }
                    }
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "#0";
                }
            } catch (ClassNotFoundException | SQLException | NumberFormatException e) {
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            CommonFunction.LogExceptionServlet(log, ex.getMessage(), ex);
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
        } catch (Exception ex) {
            CommonFunction.LogExceptionServlet(log, ex.getMessage(), ex);
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
