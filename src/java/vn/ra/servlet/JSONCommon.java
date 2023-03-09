/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.mobileid.esigncloud.management.OwnerInfo;
import vn.ra.object.BACKOFFICE_USER;
import vn.ra.object.BRANCH;
import vn.ra.object.BRANCH_ROLE;
import vn.ra.object.CERTIFICATE_ATTRIBUTES;
import vn.ra.object.CERTIFICATE_ATTRIBUTES_RADIO;
import vn.ra.object.CERTIFICATE_ATTRIBUTES_SESSION;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.object.CERTIFICATION_OWNER;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.CERTIFICATION_PROFILE_ATTR;
import vn.ra.object.CERTIFICATION_PURPOSE;
import vn.ra.object.CERTIFICATION_TYPE_COMPONENT;
import vn.ra.object.CITY_PROVINCE;
import vn.ra.object.CredentialDataAuthen;
import vn.ra.object.ENTERPRISE_INFO;
import vn.ra.object.FILE_PROFILE_JSON;
import vn.ra.object.FormFactorJsonProperties;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.MENULINK;
import vn.ra.object.PKI_FORMFACTOR;
import vn.ra.object.PROFILE_DISCOUNT_RATE_DATA;
import vn.ra.object.PUSH_TOKEN;
import vn.ra.object.PUSH_TOKEN_ATTR;
import vn.ra.object.PUSH_TOKEN_EDITED;
import vn.ra.object.ROLE;
import vn.ra.object.ROLE_DATA;
import vn.ra.object.TOKEN;
import vn.ra.process.AddIPRelying;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.SessionPushNotification;
import vn.ra.process.SessionRoleFunctions;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;
import vn.ra.object.ProObj;
import vn.ra.object.ProfileContactInfoJson;
import vn.ra.process.RSSPProcessCommon;
import vn.ra.utility.Config;
import vn.ra.utility.ConfigLog;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author THANH-PC
 */
public class JSONCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final Logger log = Logger.getLogger(JSONCommon.class.getName());

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
            ConnectDatabase db = new ConnectDatabase();
            CommonFunction com = new CommonFunction();
            String strView = "";
            JSONArray listJson = new JSONArray();
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
//                    String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    String pSessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                    String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                    String idParam = request.getParameter("idParam");
                    if (null != idParam) {
                        switch (idParam) {
                            case "listgeneralpolicyedited": {
                                //<editor-fold defaultstate="collapsed" desc="listgeneralpolicyedited">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sLanguage = request.getParameter("vLanguage");
                                    GENERAL_POLICY[][] temp = new GENERAL_POLICY[1][];
                                    db.S_BO_GENERAL_POLICY_ALL(sLanguage, temp);
                                    if (temp[0].length > 0) {
                                        for (GENERAL_POLICY temp1 : temp[0]) {
                                            if (EscapeUtils.CheckTextNull(temp1.MIMETYPE).equals(Definitions.CONFIG_POLICY_MINETYPE_FO_DEFAULT_PUSH_NOTICE_JSON))
                                            {
                                                //<editor-fold defaultstate="collapsed" desc="### text/json/pushnotification">
                                                String sRemarkParent = EscapeUtils.CheckTextNull(temp1.REMARK);
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                                json.put("MIMETYPE", EscapeUtils.CheckTextNull(temp1.MIMETYPE));
                                                json.put("REMARK", sRemarkParent);
                                                json.put("ID", String.valueOf(temp1.ID));
                                                if (temp1.REQUIRED == true) {
                                                    json.put("IsRequired", "1");
                                                } else {
                                                    json.put("IsRequired", "0");
                                                }
                                                if (temp1.FRONT_OFFICE_ENABLED == true) {
                                                    json.put("FRONT_OFFICE_ENABLED", "1");
                                                } else {
                                                    json.put("FRONT_OFFICE_ENABLED", "0");
                                                }
                                                JSONArray listJsonParent = new JSONArray();
                                                ObjectMapper oMapperParse = new ObjectMapper();
                                                PUSH_TOKEN_EDITED itemParsePush = oMapperParse.readValue(temp1.VALUE, PUSH_TOKEN_EDITED.class);
                                                SessionPushNotification cartPushNoti = new SessionPushNotification();
                                                for (PUSH_TOKEN_EDITED.Attribute attribute : itemParsePush.getAttributes()) {
                                                    PUSH_TOKEN_ATTR rsPsuh = new PUSH_TOKEN_ATTR();
                                                    String sRemarkChilrent = EscapeUtils.CheckTextNull(attribute.getRemark());
                                                    if("0".equals(sLanguage))
                                                    {
                                                        sRemarkChilrent = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                                                    }
                                                    JSONObject jsonChilrent = new JSONObject();
                                                    jsonChilrent.put("Code", "0");
                                                    jsonChilrent.put("NAME", EscapeUtils.CheckTextNull(attribute.getName()));
                                                    jsonChilrent.put("MIMETYPE",  EscapeUtils.CheckTextNull(attribute.getMimetype()));
                                                    jsonChilrent.put("REMARK", sRemarkChilrent);
                                                    jsonChilrent.put("VALUE",  EscapeUtils.CheckTextNull(attribute.getValue()));
                                                    jsonChilrent.put("ID", String.valueOf(temp1.ID));
                                                    if (temp1.REQUIRED == true) {
                                                        jsonChilrent.put("IsRequired", "1");
                                                    } else {
                                                        jsonChilrent.put("IsRequired", "0");
                                                    }
                                                    if (temp1.FRONT_OFFICE_ENABLED == true) {
                                                        jsonChilrent.put("FRONT_OFFICE_ENABLED", "1");
                                                    } else {
                                                        jsonChilrent.put("FRONT_OFFICE_ENABLED", "0");
                                                    }
                                                    listJsonParent.add(jsonChilrent);
                                                    rsPsuh.name = EscapeUtils.CheckTextNull(attribute.getName());
                                                    rsPsuh.mimetype = EscapeUtils.CheckTextNull(attribute.getMimetype());
                                                    rsPsuh.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
                                                    rsPsuh.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                                                    rsPsuh.value = EscapeUtils.CheckTextNull(attribute.getValue());
                                                    cartPushNoti.AddSessionPushNotification(rsPsuh);
                                                }
                                                request.getSession(false).setAttribute("sessPushNotificationList", cartPushNoti);
                                                json.put(Definitions.CONFIG_POLICY_FO_DEFAULT_PUSH_NOTICE_JSON, listJsonParent);
                                                listJson.add(json);
                                                //</editor-fold>
                                            }
                                            else if (EscapeUtils.CheckTextNull(temp1.MIMETYPE).equals(Definitions.CONFIG_POLICY_MINETYPE_FO_PUSH_NOTIFICATION_RECORD_COLLECTION)) 
                                            {
                                                //<editor-fold defaultstate="collapsed" desc="### text/json/collectionnotice">
                                                String sRemarkParent = EscapeUtils.CheckTextNull(temp1.REMARK);
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                                json.put("MIMETYPE", EscapeUtils.CheckTextNull(temp1.MIMETYPE));
                                                json.put("REMARK", sRemarkParent);
                                                json.put("ID", String.valueOf(temp1.ID));
                                                if (temp1.REQUIRED == true) {
                                                    json.put("IsRequired", "1");
                                                } else {
                                                    json.put("IsRequired", "0");
                                                }
                                                if (temp1.FRONT_OFFICE_ENABLED == true) {
                                                    json.put("FRONT_OFFICE_ENABLED", "1");
                                                } else {
                                                    json.put("FRONT_OFFICE_ENABLED", "0");
                                                }
                                                JSONArray listJsonParent = new JSONArray();
                                                ObjectMapper oMapperParse = new ObjectMapper();
                                                PUSH_TOKEN_EDITED itemParsePush = oMapperParse.readValue(temp1.VALUE, PUSH_TOKEN_EDITED.class);
                                                SessionPushNotification cartPushNoti = new SessionPushNotification();
                                                for (PUSH_TOKEN_EDITED.Attribute attribute : itemParsePush.getAttributes()) {
                                                    PUSH_TOKEN_ATTR rsPsuh = new PUSH_TOKEN_ATTR();
                                                    String sRemarkChilrent = EscapeUtils.CheckTextNull(attribute.getRemark());
                                                    if("0".equals(sLanguage))
                                                    {
                                                        sRemarkChilrent = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                                                    }
                                                    JSONObject jsonChilrent = new JSONObject();
                                                    jsonChilrent.put("Code", "0");
                                                    jsonChilrent.put("NAME", EscapeUtils.CheckTextNull(attribute.getName()));
                                                    jsonChilrent.put("MIMETYPE",  EscapeUtils.CheckTextNull(attribute.getMimetype()));
                                                    jsonChilrent.put("REMARK", sRemarkChilrent);
                                                    jsonChilrent.put("VALUE",  EscapeUtils.CheckTextNull(attribute.getValue()));
                                                    jsonChilrent.put("ID", String.valueOf(temp1.ID));
                                                    if (temp1.REQUIRED == true) {
                                                        jsonChilrent.put("IsRequired", "1");
                                                    } else {
                                                        jsonChilrent.put("IsRequired", "0");
                                                    }
                                                    if (temp1.FRONT_OFFICE_ENABLED == true) {
                                                        jsonChilrent.put("FRONT_OFFICE_ENABLED", "1");
                                                    } else {
                                                        jsonChilrent.put("FRONT_OFFICE_ENABLED", "0");
                                                    }
                                                    listJsonParent.add(jsonChilrent);
                                                    rsPsuh.name = EscapeUtils.CheckTextNull(attribute.getName());
                                                    rsPsuh.mimetype = EscapeUtils.CheckTextNull(attribute.getMimetype());
                                                    rsPsuh.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
                                                    rsPsuh.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                                                    rsPsuh.value = EscapeUtils.CheckTextNull(attribute.getValue());
                                                    cartPushNoti.AddSessionPushNotification(rsPsuh);
                                                }
                                                request.getSession(false).setAttribute("sessSysDisallowanceList", cartPushNoti);
                                                json.put(Definitions.CONFIG_POLICY_FO_PUSH_NOTIFICATION_RECORD_COLLECTION, listJsonParent);
                                                listJson.add(json);
                                                //</editor-fold>
                                            }
                                            else {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                String sValue = EscapeUtils.CheckTextNull(temp1.VALUE);
                                                String sMIMETYPE = EscapeUtils.CheckTextNull(temp1.MIMETYPE);
                                                if (sMIMETYPE.equals(Definitions.CONFIG_POLICY_MIMETYPE_BOOLEAN)) {
                                                    if (sValue.equals("1")) {
                                                        sValue = "checked";
                                                    } else {
                                                        sValue = "0";
                                                    }
                                                }
                                                if (sMIMETYPE.equals(Definitions.CONFIG_POLICY_MIMETYPE_NUMERIC)) {
                                                    sValue = com.convertMoneyAnotherZero(Integer.parseInt(sValue));
                                                }
                                                json.put("NAME", String.valueOf(temp1.NAME));
                                                json.put("MIMETYPE", sMIMETYPE);
                                                json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                json.put("VALUE", sValue);
                                                json.put("ID", String.valueOf(temp1.ID));
                                                if (temp1.REQUIRED == true) {
                                                    json.put("IsRequired", "1");
                                                } else {
                                                    json.put("IsRequired", "0");
                                                }
                                                if (temp1.FRONT_OFFICE_ENABLED == true) {
                                                    json.put("FRONT_OFFICE_ENABLED", "1");
                                                } else {
                                                    json.put("FRONT_OFFICE_ENABLED", "0");
                                                }
                                                listJson.add(json);
                                                if(String.valueOf(temp1.ID).equals(Definitions.CONFIG_POLICY_ATTR_BO_DMS_PROPERTIES_CURRENT_ID))
                                                {
                                                    request.getSession(false).setAttribute("sessBO_DMS_PROPERTIES_CURRENT", sValue);
                                                }
                                            }
                                        }
                                        if (listJson.size() > 0) {
                                        } else {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "1");
                                            listJson.add(json);
                                        }
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "listgeneralpolicy": {
                                //<editor-fold defaultstate="collapsed" desc="listgeneralpolicy">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
//                                    String sLanguage = request.getParameter("vLanguage");
//                                    GENERAL_POLICY[][] temp = new GENERAL_POLICY[1][];
//                                    db.S_BO_GENERAL_POLICY_LIST(sLanguage, temp);
//                                    if (temp[0].length > 0) {
//                                        for (GENERAL_POLICY temp1 : temp[0]) {
//                                            if (EscapeUtils.CheckTextNull(temp1.NAME).equals(Definitions.CONFIG_POLICY_FO_DEFAULT_PUSH_NOTICE_JSON)) {
//                                                ObjectMapper oMapperParse = new ObjectMapper();
//                                                PUSH_TOKEN itemParsePush = oMapperParse.readValue(temp1.VALUE, PUSH_TOKEN.class);
//                                                JSONObject json1 = new JSONObject();
//                                                json1.put("Code", "0");
//                                                json1.put("NAME", Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_TEXT_COLOR);
//                                                json1.put("MIMETYPE", EscapeUtils.CheckTextNull(temp1.MIMETYPE));
//                                                json1.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
//                                                json1.put("VALUE", EscapeUtils.CheckTextNull(itemParsePush.PUSH_NOTICE_TEXT_COLOR));
//                                                json1.put("ID", String.valueOf(temp1.ID));
//                                                if (temp1.REQUIRED == true) {
//                                                    json1.put("IsRequired", "1");
//                                                } else {
//                                                    json1.put("IsRequired", "0");
//                                                }
//                                                if (temp1.FRONT_OFFICE_ENABLED == true) {
//                                                    json1.put("FRONT_OFFICE_ENABLED", "1");
//                                                } else {
//                                                    json1.put("FRONT_OFFICE_ENABLED", "0");
//                                                }
//                                                listJson.add(json1);
//                                                JSONObject json2 = new JSONObject();
//                                                json2.put("Code", "0");
//                                                json2.put("NAME", Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_BGR_COLOR);
//                                                json2.put("MIMETYPE", EscapeUtils.CheckTextNull(temp1.MIMETYPE));
//                                                json2.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
//                                                json2.put("VALUE", EscapeUtils.CheckTextNull(itemParsePush.PUSH_NOTICE_BGR_COLOR));
//                                                json2.put("ID", String.valueOf(temp1.ID));
//                                                if (temp1.REQUIRED == true) {
//                                                    json2.put("IsRequired", "1");
//                                                } else {
//                                                    json2.put("IsRequired", "0");
//                                                }
//                                                if (temp1.FRONT_OFFICE_ENABLED == true) {
//                                                    json2.put("FRONT_OFFICE_ENABLED", "1");
//                                                } else {
//                                                    json2.put("FRONT_OFFICE_ENABLED", "0");
//                                                }
//                                                listJson.add(json2);
//                                                JSONObject json3 = new JSONObject();
//                                                json3.put("Code", "0");
//                                                json3.put("NAME", Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_URL);
//                                                json3.put("MIMETYPE", EscapeUtils.CheckTextNull(temp1.MIMETYPE));
//                                                json3.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
//                                                json3.put("VALUE", EscapeUtils.CheckTextNull(itemParsePush.PUSH_NOTICE_URL));
//                                                json3.put("ID", String.valueOf(temp1.ID));
//                                                if (temp1.REQUIRED == true) {
//                                                    json3.put("IsRequired", "1");
//                                                } else {
//                                                    json3.put("IsRequired", "0");
//                                                }
//                                                if (temp1.FRONT_OFFICE_ENABLED == true) {
//                                                    json3.put("FRONT_OFFICE_ENABLED", "1");
//                                                } else {
//                                                    json3.put("FRONT_OFFICE_ENABLED", "0");
//                                                }
//                                                listJson.add(json3);
//                                                JSONObject json4 = new JSONObject();
//                                                json4.put("Code", "0");
//                                                json4.put("NAME", Definitions.CONFIG_POLICY_FO_PUSH_NOTICE_CONTENT);
//                                                json4.put("MIMETYPE", EscapeUtils.CheckTextNull(temp1.MIMETYPE));
//                                                json4.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
//                                                json4.put("VALUE", EscapeUtils.CheckTextNull(itemParsePush.PUSH_NOTICE_CONTENT));
//                                                json4.put("ID", String.valueOf(temp1.ID));
//                                                if (temp1.REQUIRED == true) {
//                                                    json4.put("IsRequired", "1");
//                                                } else {
//                                                    json4.put("IsRequired", "0");
//                                                }
//                                                if (temp1.FRONT_OFFICE_ENABLED == true) {
//                                                    json4.put("FRONT_OFFICE_ENABLED", "1");
//                                                } else {
//                                                    json4.put("FRONT_OFFICE_ENABLED", "0");
//                                                }
//                                                listJson.add(json4);
//                                            } else {
//                                                JSONObject json = new JSONObject();
//                                                json.put("Code", "0");
//                                                String sValue = EscapeUtils.CheckTextNull(temp1.VALUE);
//                                                String sMIMETYPE = EscapeUtils.CheckTextNull(temp1.MIMETYPE);
//                                                if (sMIMETYPE.equals(Definitions.CONFIG_POLICY_MIMETYPE_BOOLEAN)) {
//                                                    if (sValue.equals("1")) {
//                                                        sValue = "checked";
//                                                    } else {
//                                                        sValue = "0";
//                                                    }
//                                                }
//                                                if (sMIMETYPE.equals(Definitions.CONFIG_POLICY_MIMETYPE_NUMERIC)) {
//                                                    sValue = com.convertMoneyAnotherZero(Integer.parseInt(sValue));
//                                                }
//                                                json.put("NAME", String.valueOf(temp1.NAME));
//                                                json.put("MIMETYPE", sMIMETYPE);
//                                                json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
//                                                json.put("VALUE", sValue);
//                                                json.put("ID", String.valueOf(temp1.ID));
//                                                if (temp1.REQUIRED == true) {
//                                                    json.put("IsRequired", "1");
//                                                } else {
//                                                    json.put("IsRequired", "0");
//                                                }
//                                                if (temp1.FRONT_OFFICE_ENABLED == true) {
//                                                    json.put("FRONT_OFFICE_ENABLED", "1");
//                                                } else {
//                                                    json.put("FRONT_OFFICE_ENABLED", "0");
//                                                }
//                                                listJson.add(json);
//                                            }
//                                        }
//                                        if (listJson.size() > 0) {
//                                        } else {
//                                            JSONObject json = new JSONObject();
//                                            json.put("Code", "1");
//                                            listJson.add(json);
//                                        }
//                                    }
//                                } else {
//                                    JSONObject json = new JSONObject();
//                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
//                                    listJson.add(json);
//                                }
//                                break;
                                //</editor-fold>
                            }
                            case "listnotassignmenulink": {
                                //<editor-fold defaultstate="collapsed" desc="listnotassigntemplate">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String TypeCertID = EscapeUtils.CheckTextNull(request.getParameter("TypeCertID"));
                                    MENULINK[] temp = db.S_BO_URI_NOT_ROLE_LIST(TypeCertID, pSessLanguage);
                                    if (temp.length > 0) {
                                        for (MENULINK temp1 : temp) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("ID", String.valueOf(temp1.ID));
//                                            json.put("MENULINK_ROLE_ID", String.valueOf(temp1.MENULINK_ROLE_ID));
                                            json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                            json.put("PARENTLINK_REMARK", EscapeUtils.CheckTextNull(temp1.PARENTLINK_REMARK));
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "listyesassignmenulink": {
                                //<editor-fold defaultstate="collapsed" desc="listyesassigntemplate">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String TypeCertID = request.getParameter("TypeCertID");
                                    MENULINK[] temp = db.S_BO_URI_ROLE_LIST(TypeCertID, pSessLanguage);
                                    if (temp.length > 0) {
                                        int i = 1;
                                        for (MENULINK temp1 : temp) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("Index", i++);
                                            json.put("MENULINK_ROLE_ID", String.valueOf(temp1.MENULINK_ROLE_ID));
                                            json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                            json.put("PARENTLINK_REMARK", EscapeUtils.CheckTextNull(temp1.PARENTLINK_REMARK));
                                            json.put("CREATED_DT", EscapeUtils.CheckTextNull(temp1.CREATED_DT));
                                            json.put("LINKURL", EscapeUtils.CheckTextNull(temp1.LINKURL));
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "listnotassigntemplate": {
                                //<editor-fold defaultstate="collapsed" desc="listnotassigntemplate">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String TypeCertID = EscapeUtils.CheckTextNull(request.getParameter("TypeCertID"));
                                    CERTIFICATION_PROFILE_ATTR[][] temp = new CERTIFICATION_PROFILE_ATTR[1][];
                                    db.S_BO_CERTIFICATION_PROFILE_ATTR_NOT_ASSIGN(TypeCertID, pSessLanguage, temp);
                                    if (temp[0].length > 0) {
                                        for (CERTIFICATION_PROFILE_ATTR temp1 : temp[0]) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("CERTIFICATION_PROFILE_ATTR_TYPE_ID", String.valueOf(temp1.CERTIFICATION_PROFILE_ATTR_TYPE_ID));
                                            json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                            json.put("PRE_FIX", EscapeUtils.CheckTextNull(temp1.PRE_FIX));
                                            json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "listyesassigntemplate": {
                                //<editor-fold defaultstate="collapsed" desc="listyesassigntemplate">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String TypeCertID = request.getParameter("TypeCertID");
                                    CERTIFICATION_PROFILE_ATTR[][] temp = new CERTIFICATION_PROFILE_ATTR[1][];
                                    db.S_BO_CERTIFICATION_PROFILE_ATTR_ASSIGN(TypeCertID, pSessLanguage, temp);
                                    if (temp[0].length > 0) {
                                        int i = 1;
                                        for (CERTIFICATION_PROFILE_ATTR temp1 : temp[0]) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("Index", i++);
//                                            json.put("CertTemplateID", String.valueOf(temp1.CertTemplateID));
                                            json.put("CERTIFICATION_PROFILE_ATTR_ID", String.valueOf(temp1.CERTIFICATION_PROFILE_ATTR_ID));
                                            json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                            json.put("PRE_FIX", EscapeUtils.CheckTextNull(temp1.PRE_FIX));
                                            json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                            json.put("CREATED_DT", EscapeUtils.CheckTextNull(temp1.CREATED_DT));
                                            if (temp1.REQUIRED == true) {
                                                json.put("IsRequired", "1");
                                            } else {
                                                json.put("IsRequired", "0");
                                            }
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            // load certificate profile, authority, purpose
                            case "listcitycombobox": {
                                //<editor-fold defaultstate="collapsed" desc="listcitycombobox">
                                CITY_PROVINCE[][] temp = new CITY_PROVINCE[1][];
                                db.S_BO_PROVINCE_COMBOBOX(pSessLanguage, temp);
                                if (temp[0].length > 0) {
                                    for (CITY_PROVINCE temp1 : temp[0]) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "0");
                                        json.put("cityprovinceId", String.valueOf(temp1.ID));
                                        json.put("cityprovincedesc", String.valueOf(temp1.REMARK));
                                        json.put("cityprovincecode", EscapeUtils.CheckTextNull(temp1.NAME));
                                        listJson.add(json);
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "listcacombobox": {
                                //<editor-fold defaultstate="collapsed" desc="listcacombobox">
                                CERTIFICATION_AUTHORITY[][] temp = new CERTIFICATION_AUTHORITY[1][];
                                db.S_BO_CERTIFICATION_AUTHORITY_COMBOBOX(pSessLanguage, temp);
                                if (temp[0].length > 0) {
                                    for (CERTIFICATION_AUTHORITY temp1 : temp[0]) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "0");
                                        json.put("ID", String.valueOf(temp1.ID));
                                        json.put("REMARK", String.valueOf(temp1.REMARK));
                                        json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                        listJson.add(json);
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "listbranchagencycombobox": {
                                //<editor-fold defaultstate="collapsed" desc="listbranchagencycombobox">
                                BRANCH[][] temp = new BRANCH[1][];
                                db.S_BO_BRANCH_COMBOBOX(pSessLanguage, temp);
                                if (temp[0].length > 0) {
                                    for (BRANCH temp1 : temp[0]) {
                                        if(!String.valueOf(temp1.PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("ID", String.valueOf(temp1.ID));
                                            json.put("REMARK", String.valueOf(temp1.REMARK));
                                            json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                            listJson.add(json);
                                        }
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadcert_profile_frist": {
                                //<editor-fold defaultstate="collapsed" desc="loadcert_profile_frist">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                String vCertDurationOrProfileID = request.getParameter("vCertDurationOrProfileID");
                                CERTIFICATION_PROFILE[][] temp = new CERTIFICATION_PROFILE[1][];
                                db.S_BO_CERTIFICATION_PROFILE_DETAIL(vCertDurationOrProfileID, temp);
                                if (temp[0].length > 0) {
                                    int i = 1;
                                    for (CERTIFICATION_PROFILE temp1 : temp[0]) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "0");
                                        json.put("Index", i++);
                                        json.put("PROPERTIES", String.valueOf(temp1.PROPERTIES));
                                        json.put("DURATION_FREE", temp1.DURATION_FREE != 0 ? com.convertMoneyAnotherZero(temp1.DURATION_FREE).trim() : temp1.DURATION_FREE);
                                        json.put("AMOUNT", com.convertMoneyAnotherZero(temp1.AMOUNT).trim());
                                        json.put("RENEWAL_AMOUNT", com.convertMoneyAnotherZero(temp1.RENEWAL_AMOUNT).trim());
                                        json.put("REISSUE_AMOUNT", com.convertMoneyAnotherZero(temp1.REISSUE_AMOUNT).trim());
                                        json.put("CHANGE_AMOUNT", com.convertMoneyAnotherZero(temp1.CHANGE_AMOUNT).trim());
                                        listJson.add(json);
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
//                                } else {
//                                    JSONObject json = new JSONObject();
//                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
//                                    listJson.add(json);
//                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadcert_profile_list": {
                                //<editor-fold defaultstate="collapsed" desc="loadcert_profile_list">
                                String vCertDurationOrProfileID = request.getParameter("vCertDurationOrProfileID");
                                ObjectMapper objectMapper = new ObjectMapper();
                                CERTIFICATION_PROFILE[][] temp = new CERTIFICATION_PROFILE[1][];
                                db.S_BO_CERTIFICATION_PROFILE_DETAIL(vCertDurationOrProfileID, temp);
                                if (temp[0].length > 0) {
                                    int j = 1;
                                    String sProperties = temp[0][0].PROPERTIES;
                                    CERTIFICATE_ATTRIBUTES proParse = objectMapper.readValue(sProperties, CERTIFICATE_ATTRIBUTES.class);
                                    for (int i = 0; i < proParse.getAttributes().size(); i++) {
                                        if (proParse.getAttributes().get(i).getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_RADIOBUTTON)) {
                                            JSONObject jsonParent = new JSONObject();
                                            jsonParent.put("Code", "0");
                                            jsonParent.put("SubjectDNAttrType", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType()));
                                            if (proParse.getAttributes().get(i).isRequire() == true) {
                                                jsonParent.put("IsRequired", "1");
                                            } else {
                                                jsonParent.put("IsRequired", "0");
                                            }
                                            JSONArray listJsonParent = new JSONArray();
                                            for (int n = 0; n < proParse.getAttributes().get(i).getAttributes().size(); n++) {
                                                String sSubjectDNAttrDesc = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getRemark());
                                                if ("0".equals(pSessLanguage)) {
                                                    sSubjectDNAttrDesc = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getRemarkEn());
                                                }
                                                JSONObject jsonChilrent = new JSONObject();
                                                jsonChilrent.put("Code", "0");
                                                jsonChilrent.put("CertTemplateID", j++);
                                                jsonChilrent.put("SubjectDNAttrCode", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getName()));
                                                jsonChilrent.put("SubjectDNAttrType", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType()));
                                                jsonChilrent.put("SubjectDNAttrCNType", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getCommomNameType()));
                                                jsonChilrent.put("SubjectDNAttrPreFix", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getPrefix()));
                                                jsonChilrent.put("SubjectDNAttrDesc", sSubjectDNAttrDesc);
                                                if (proParse.getAttributes().get(i).isRequire() == true) {
                                                    jsonChilrent.put("IsRequired", "1");
                                                } else {
                                                    jsonChilrent.put("IsRequired", "0");
                                                }
                                                listJsonParent.add(jsonChilrent);
                                            }
                                            jsonParent.put("RADIO_LIST", listJsonParent);
                                            listJson.add(jsonParent);
                                        } else {
                                            String sSubjectDNAttrDesc = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getRemark());
                                            if ("0".equals(pSessLanguage)) {
                                                sSubjectDNAttrDesc = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getRemarkEn());
                                            }
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("CertTemplateID", j++);
                                            json.put("SubjectDNAttrCode", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getName()));
                                            json.put("SubjectDNAttrType", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType()));
                                            json.put("SubjectDNAttrCNType", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getCommomNameType()));
                                            json.put("SubjectDNAttrPreFix", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getPrefix()));
                                            json.put("SubjectDNAttrDesc", sSubjectDNAttrDesc);
                                            if (proParse.getAttributes().get(i).isRequire() == true) {
                                                json.put("IsRequired", "1");
                                            } else {
                                                json.put("IsRequired", "0");
                                            }
                                            listJson.add(json);
                                        }
                                    }
//                                    CommonFunction.LogDebugString(log, "JSON-1", listJson.toString());
//                                    CommonFunction.LogDebugString(log, "JSON-2", listJson.toJSONString());
                                }
//                                CommonFunction.LogDebugString(log, "JSON-COMPONENT", listJson.toString());
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadcert_profile_list_new": {
                                //<editor-fold defaultstate="collapsed" desc="loadcert_profile_list_new">
                                String vCertDurationOrProfileID = request.getParameter("vCertDurationOrProfileID");
                                CERTIFICATION_PROFILE[][] temp = new CERTIFICATION_PROFILE[1][];
                                db.S_BO_CERTIFICATION_PROFILE_DETAIL(vCertDurationOrProfileID, temp);
                                if (temp[0].length > 0) {
                                    int j = 1;
                                    String sProperties = temp[0][0].PROPERTIES;
                                    CERTIFICATION_TYPE_COMPONENT[][] rsCertType = new CERTIFICATION_TYPE_COMPONENT[1][];
                                    CommonFunction.getJsonComponentForCert(sProperties, rsCertType);
                                    for(CERTIFICATION_TYPE_COMPONENT rsCertType1 : rsCertType[0])
                                    {
                                        String sSubjectDNAttrDesc = EscapeUtils.CheckTextNull(rsCertType1.remark);
                                        if ("0".equals(pSessLanguage)) {
                                            sSubjectDNAttrDesc = EscapeUtils.CheckTextNull(rsCertType1.remarkEn);
                                        }
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "0");
                                        json.put("CertTemplateID", j++);
                                        json.put("SubjectDNAttrCode", EscapeUtils.CheckTextNull(rsCertType1.name));
                                        switch (rsCertType1.attributeType) {
                                            case Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY:
                                                json.put("SubjectDNAttrType", Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY);
                                                break;
                                            case Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL:
                                                json.put("SubjectDNAttrType", Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL);
                                                break;
                                            case Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN:
                                                json.put("SubjectDNAttrType", Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN);
                                                break;
                                            default:
                                                json.put("SubjectDNAttrType", Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD);
                                                break;
                                        }
                                        json.put("SubjectDNAttrCNType", EscapeUtils.CheckTextNull(rsCertType1.commomNameType));
                                        json.put("SubjectDNAttrPreFix", EscapeUtils.CheckTextNull(rsCertType1.prefix));
                                        json.put("SubjectDNAttrDesc", sSubjectDNAttrDesc);
                                        if (rsCertType1.require == true) {
                                            json.put("IsRequired", "1");
                                        } else {
                                            json.put("IsRequired", "0");
                                        }
                                        listJson.add(json);
                                    }
                                    
//                                    CERTIFICATE_ATTRIBUTES proParse = objectMapper.readValue(sProperties, CERTIFICATE_ATTRIBUTES.class);
//                                    for (int i = 0; i < proParse.getAttributes().size(); i++) {
//                                        if (proParse.getAttributes().get(i).getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_RADIOBUTTON)) {
//                                            JSONObject jsonParent = new JSONObject();
//                                            jsonParent.put("Code", "0");
//                                            jsonParent.put("SubjectDNAttrType", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType()));
//                                            if (proParse.getAttributes().get(i).isRequire() == true) {
//                                                jsonParent.put("IsRequired", "1");
//                                            } else {
//                                                jsonParent.put("IsRequired", "0");
//                                            }
//                                            JSONArray listJsonParent = new JSONArray();
//                                            for (int n = 0; n < proParse.getAttributes().get(i).getAttributes().size(); n++) {
//                                                String sSubjectDNAttrDesc = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getRemark());
//                                                if ("0".equals(pSessLanguage)) {
//                                                    sSubjectDNAttrDesc = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getRemarkEn());
//                                                }
//                                                JSONObject jsonChilrent = new JSONObject();
//                                                jsonChilrent.put("Code", "0");
//                                                jsonChilrent.put("CertTemplateID", j++);
//                                                jsonChilrent.put("SubjectDNAttrCode", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getName()));
//                                                jsonChilrent.put("SubjectDNAttrType", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType()));
//                                                jsonChilrent.put("SubjectDNAttrCNType", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getCommomNameType()));
//                                                jsonChilrent.put("SubjectDNAttrPreFix", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getPrefix()));
//                                                jsonChilrent.put("SubjectDNAttrDesc", sSubjectDNAttrDesc);
//                                                if (proParse.getAttributes().get(i).isRequire() == true) {
//                                                    jsonChilrent.put("IsRequired", "1");
//                                                } else {
//                                                    jsonChilrent.put("IsRequired", "0");
//                                                }
//                                                listJsonParent.add(jsonChilrent);
//                                            }
//                                            jsonParent.put("RADIO_LIST", listJsonParent);
//                                            listJson.add(jsonParent);
//                                        } else {
//                                            String sSubjectDNAttrDesc = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getRemark());
//                                            if ("0".equals(pSessLanguage)) {
//                                                sSubjectDNAttrDesc = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getRemarkEn());
//                                            }
//                                            JSONObject json = new JSONObject();
//                                            json.put("Code", "0");
//                                            json.put("CertTemplateID", j++);
//                                            json.put("SubjectDNAttrCode", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getName()));
//                                            json.put("SubjectDNAttrType", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType()));
//                                            json.put("SubjectDNAttrCNType", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getCommomNameType()));
//                                            json.put("SubjectDNAttrPreFix", EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getPrefix()));
//                                            json.put("SubjectDNAttrDesc", sSubjectDNAttrDesc);
//                                            if (proParse.getAttributes().get(i).isRequire() == true) {
//                                                json.put("IsRequired", "1");
//                                            } else {
//                                                json.put("IsRequired", "0");
//                                            }
//                                            listJson.add(json);
//                                        }
//                                    }
//                                    CommonFunction.LogDebugString(log, "JSON-1", listJson.toString());
//                                    CommonFunction.LogDebugString(log, "JSON-2", listJson.toJSONString());
                                }
//                                CommonFunction.LogDebugString(log, "JSON-COMPONENT", listJson.toString());
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadcert_authority": {
                                //<editor-fold defaultstate="collapsed" desc="loadcert_authority">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String idCA = request.getParameter("idCA");
                                    CERTIFICATION_PURPOSE[][] temp = new CERTIFICATION_PURPOSE[1][];
                                    db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX(idCA, pSessLanguage, temp);
                                    if (temp[0].length > 0) {
                                        for (CERTIFICATION_PURPOSE temp1 : temp[0]) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("ID", String.valueOf(temp1.ID));
                                            json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
//                                    String idCA = request.getParameter("idCA");
//                                    CERTIFICATION_PURPOSE[][] temp = new CERTIFICATION_PURPOSE[1][];
//                                    db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX(idCA, pSessLanguage, temp);
//                                    if (temp[0].length > 0) {
//                                        for (CERTIFICATION_PURPOSE temp1 : temp[0]) {
//                                            JSONObject json = new JSONObject();
//                                            json.put("Code", "0");
//                                            json.put("ID", String.valueOf(temp1.ID));
//                                            json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
//                                            listJson.add(json);
//                                        }
//                                    }
//                                    if (listJson.size() <= 0) {
//                                        JSONObject json = new JSONObject();
//                                        json.put("Code", "1");
//                                        listJson.add(json);
//                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadcert_authority_ofapprove": {
                                //<editor-fold defaultstate="collapsed" desc="loadcert_authority_ofapprove">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String idCA = request.getParameter("idCA");
                                    String idCertPurpose = request.getParameter("idCertPurpose");
                                    CERTIFICATION_PURPOSE[][] temp = new CERTIFICATION_PURPOSE[1][];
                                    if(Integer.parseInt(idCertPurpose) == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                        || Integer.parseInt(idCertPurpose) == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                        || Integer.parseInt(idCertPurpose) == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL)
                                    {
                                        db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX_TOKEN(idCA, pSessLanguage, temp);
                                    } else {
                                        db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX_SOFT_TOKEN(idCA, pSessLanguage, temp);
                                    }
                                    if (temp[0].length > 0) {
                                        for (CERTIFICATION_PURPOSE temp1 : temp[0]) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("ID", String.valueOf(temp1.ID));
                                            json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadcert_authority_oftoken": {
                                //<editor-fold defaultstate="collapsed" desc="loadcert_authority_oftoken - remove temp">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String idCA = request.getParameter("idCA");
                                    String idPKIFormFactor = request.getParameter("idPKIFormFactor");
                                    CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) sessionsa.getAttribute("SessPolicyCert_Data");
                                    CERTIFICATION_PURPOSE[][] temp = new CERTIFICATION_PURPOSE[1][];
                                    db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX_TOKEN(idCA, pSessLanguage, temp);
                                    if (temp[0].length > 0) {
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                        {
                                            for (CERTIFICATION_PURPOSE temp1 : temp[0]) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("ID", String.valueOf(temp1.ID));
                                                json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                listJson.add(json);
                                            }
                                        }
                                        else
                                        {
                                            String sFristCodeCA = "";
                                            CERTIFICATION_AUTHORITY[][] tempCA = new CERTIFICATION_AUTHORITY[1][];
                                            db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(idCA, tempCA);
                                            if(tempCA[0].length > 0)
                                            {
                                                sFristCodeCA = EscapeUtils.CheckTextNull(tempCA[0][0].NAME);
                                            }
                                            boolean accessProfileAll = CommonFunction.checkAccessProfileAll(sessPolicyCert_Data);
                                            if(accessProfileAll == true)
                                            {
                                                for (CERTIFICATION_PURPOSE temp1 : temp[0]) {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("ID", String.valueOf(temp1.ID));
                                                    json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                    listJson.add(json);
                                                }
                                            } else {
                                                for (CERTIFICATION_PURPOSE temp1 : temp[0]) {
                                                    if(CommonFunction.checkAccessPurposeForBranch(sFristCodeCA, temp1.NAME, sessPolicyCert_Data) == true)
                                                    {
                                                        JSONObject json = new JSONObject();
                                                        json.put("Code", "0");
                                                        json.put("ID", String.valueOf(temp1.ID));
                                                        json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                        listJson.add(json);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
//                                    String idCA = request.getParameter("idCA");
//                                    CERTIFICATION_PURPOSE[][] temp = new CERTIFICATION_PURPOSE[1][];
//                                    db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX(idCA, pSessLanguage, temp);
//                                    if (temp[0].length > 0) {
//                                        for (CERTIFICATION_PURPOSE temp1 : temp[0]) {
//                                            JSONObject json = new JSONObject();
//                                            json.put("Code", "0");
//                                            json.put("ID", String.valueOf(temp1.ID));
//                                            json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
//                                            listJson.add(json);
//                                        }
//                                    }
//                                    if (listJson.size() <= 0) {
//                                        JSONObject json = new JSONObject();
//                                        json.put("Code", "1");
//                                        listJson.add(json);
//                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadcert_authority_ofsofttoken": {
                                //<editor-fold defaultstate="collapsed" desc="loadcert_authority_ofsofttoken">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String idCA = request.getParameter("idCA");
                                    CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) sessionsa.getAttribute("SessPolicyCert_Data");
                                    CERTIFICATION_PURPOSE[][] temp = new CERTIFICATION_PURPOSE[1][];
                                    db.S_BO_CA_GET_CERTIFICATION_PURPOSE_COMBOBOX(idCA, pSessLanguage, temp);
                                    if (temp[0].length > 0) {
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                        {
                                            for (CERTIFICATION_PURPOSE temp1 : temp[0]) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("ID", String.valueOf(temp1.ID));
                                                json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                listJson.add(json);
                                            }
                                        } else {
                                            boolean accessProfileAll = CommonFunction.checkAccessProfileAll(sessPolicyCert_Data);
                                            if(accessProfileAll == true)
                                            {
                                                for (CERTIFICATION_PURPOSE temp1 : temp[0]) {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("ID", String.valueOf(temp1.ID));
                                                    json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                    listJson.add(json);
                                                }
                                            } else {
                                                String sFristCodeCA = "";
                                                CERTIFICATION_AUTHORITY[][] tempCA = new CERTIFICATION_AUTHORITY[1][];
                                                db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(idCA, tempCA);
                                                if(tempCA[0].length > 0)
                                                {
                                                    sFristCodeCA = EscapeUtils.CheckTextNull(tempCA[0][0].NAME);
                                                }
                                                for (CERTIFICATION_PURPOSE temp1 : temp[0])
                                                {
                                                    if(CommonFunction.checkAccessPurposeForBranch(sFristCodeCA, temp1.NAME, sessPolicyCert_Data) == true)
                                                    {
                                                        JSONObject json = new JSONObject();
                                                        json.put("Code", "0");
                                                        json.put("ID", String.valueOf(temp1.ID));
                                                        json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                        listJson.add(json);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadcert_purpose": {
                                //<editor-fold defaultstate="collapsed" desc="loadcert_purpose">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) sessionsa.getAttribute("SessPolicyCert_Data");
                                    String idCA = request.getParameter("idCA");
                                    String idPurpose = request.getParameter("idPurpose");
                                    String idFactor = request.getParameter("idFactor");
                                    String idAttrType = EscapeUtils.CheckTextNull(request.getParameter("idAttrType"));
                                    if("".equals(idAttrType)) {
                                        idAttrType = "0";
                                    }
                                    CERTIFICATION_PROFILE[][] temp = new CERTIFICATION_PROFILE[1][];
                                    db.S_BO_CA_GET_DURATION_COMBOBOX_BY_TYPE(idCA, idPurpose, idFactor, Integer.parseInt(idAttrType), pSessLanguage, temp);
                                    if (temp[0].length > 0) {
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                        {
                                            for (CERTIFICATION_PROFILE temp1 : temp[0]) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("ID", String.valueOf(temp1.ID));
                                                json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                                json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                json.put("DURATION", String.valueOf(temp1.DURATION));
                                                listJson.add(json);
                                            }
                                        } else {
                                            boolean accessProfileAll = CommonFunction.checkAccessProfileAll(sessPolicyCert_Data);
                                            if(accessProfileAll == true)
                                            {
                                                for (CERTIFICATION_PROFILE temp1 : temp[0]) {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("ID", String.valueOf(temp1.ID));
                                                    json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                                    json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                    json.put("DURATION", String.valueOf(temp1.DURATION));
                                                    listJson.add(json);
                                                }
                                            } else {
                                                for (CERTIFICATION_PROFILE temp1 : temp[0]) {
                                                    if(CommonFunction.checkAccessProfileForBranch(temp1.CERTIFICATION_AUTHORITY_NAME,
                                                        temp1.NAME, sessPolicyCert_Data) == true)
                                                    {
                                                        JSONObject json = new JSONObject();
                                                        json.put("Code", "0");
                                                        json.put("ID", String.valueOf(temp1.ID));
                                                        json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                                        json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                        json.put("DURATION", String.valueOf(temp1.DURATION));
                                                        listJson.add(json);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadcert_purpose_renew": {
                                //<editor-fold defaultstate="collapsed" desc="loadcert_purpose_renew">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) sessionsa.getAttribute("SessPolicyCert_Data");
                                    String idCA = request.getParameter("idCA");
                                    String idPurpose = request.getParameter("idPurpose");
                                    String idFactor = request.getParameter("idFactor");
                                    CERTIFICATION_PROFILE[][] temp = new CERTIFICATION_PROFILE[1][];
                                    db.S_BO_CA_GET_DURATION_COMBOBOX_RENEWAL(idCA, idPurpose, idFactor, pSessLanguage, temp);
                                    if (temp[0].length > 0) {
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                        {
                                            for (CERTIFICATION_PROFILE temp1 : temp[0]) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("ID", String.valueOf(temp1.ID));
                                                json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                json.put("DURATION", String.valueOf(temp1.DURATION));
                                                listJson.add(json);
                                            }
                                        } else {
                                            boolean accessProfileAll = CommonFunction.checkAccessProfileAll(sessPolicyCert_Data);
                                            if(accessProfileAll == true)
                                            {
                                                for (CERTIFICATION_PROFILE temp1 : temp[0]) {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("ID", String.valueOf(temp1.ID));
                                                    json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                    json.put("DURATION", String.valueOf(temp1.DURATION));
                                                    listJson.add(json);
                                                }
                                            } else {
                                                for (CERTIFICATION_PROFILE temp1 : temp[0]) {
                                                    if(CommonFunction.checkAccessProfileForBranch(temp1.CERTIFICATION_AUTHORITY_NAME,
                                                        temp1.NAME, sessPolicyCert_Data) == true)
                                                    {
                                                        JSONObject json = new JSONObject();
                                                        json.put("Code", "0");
                                                        json.put("ID", String.valueOf(temp1.ID));
                                                        json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                        json.put("DURATION", String.valueOf(temp1.DURATION));
                                                        listJson.add(json);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadpki_formfactorby_purpose": {
                                //<editor-fold defaultstate="collapsed" desc="loadpki_formfactorby_purpose">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    CERTIFICATION_POLICY_DATA[][] sessPolicyFormFactor_Data = (CERTIFICATION_POLICY_DATA[][]) sessionsa.getAttribute("SessPolicyFormFactor_Data");
                                    String idPurpose = request.getParameter("idPurpose");
                                    PKI_FORMFACTOR[][] temp = new PKI_FORMFACTOR[1][];
                                    db.S_BO_CA_GET_PKI_FORMFACTOR_COMBOBOX_FOR_CERTIFICATION_PURPOSE(Integer.parseInt(idPurpose),
                                        pSessLanguage, temp);
                                    if (temp[0].length > 0) {
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                        {
                                            for (PKI_FORMFACTOR temp1 : temp[0]) {
//                                                if(temp1.ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("ID", String.valueOf(temp1.ID));
                                                    json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                    listJson.add(json);
//                                                }
                                            }
                                        } else {
                                            for (PKI_FORMFACTOR temp1 : temp[0]) {
                                                if(CommonFunction.checkAccessPKIFormFactorForBranch(temp1.NAME, sessPolicyFormFactor_Data) == true)
                                                {
//                                                    if(temp1.ID != Definitions.CONFIG_PKI_FORMFACTOR_ID_PKI_USIM) {
                                                        JSONObject json = new JSONObject();
                                                        json.put("Code", "0");
                                                        json.put("ID", String.valueOf(temp1.ID));
                                                        json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                        listJson.add(json);
//                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadpki_formfactorby_purpose_nocert": {
                                //<editor-fold defaultstate="collapsed" desc="loadpki_formfactorby_purpose_nocert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    CERTIFICATION_POLICY_DATA[][] sessPolicyFormFactor_Data = (CERTIFICATION_POLICY_DATA[][]) sessionsa.getAttribute("SessPolicyFormFactor_Data");
                                    String idPurpose = request.getParameter("idPurpose");
                                    PKI_FORMFACTOR[][] temp = new PKI_FORMFACTOR[1][];
                                    db.S_BO_CA_GET_PKI_FORMFACTOR_COMBOBOX_FOR_CERTIFICATION_PURPOSE(Integer.parseInt(idPurpose),
                                        pSessLanguage, temp);
                                    if (temp[0].length > 0) {
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                        {
                                            for (PKI_FORMFACTOR temp1 : temp[0]) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("ID", String.valueOf(temp1.ID));
                                                json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                listJson.add(json);
                                            }
                                        } else {
                                            for (PKI_FORMFACTOR temp1 : temp[0]) {
                                                if(CommonFunction.checkAccessPKIFormFactorForBranch(temp1.NAME, sessPolicyFormFactor_Data) == true)
                                                {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("ID", String.valueOf(temp1.ID));
                                                    json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                    listJson.add(json);
                                                }
                                            }
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadcert_duration": {
                                //<editor-fold defaultstate="collapsed" desc="loadcert_duration">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {

                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loaduser_approve": {
                                //<editor-fold defaultstate="collapsed" desc="loaduser_approve">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String idBranch = request.getParameter("idBranch");
                                    BACKOFFICE_USER[][] temp = new BACKOFFICE_USER[1][];
                                    db.S_BO_GET_USER_SURVEYOR(idBranch, temp);
                                    if (temp[0].length > 0) {
                                        int i = 1;
                                        for (BACKOFFICE_USER temp1 : temp[0]) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("Index", i++);
                                            json.put("ID", String.valueOf(temp1.ID));
                                            json.put("USERNAME", EscapeUtils.CheckTextNull(temp1.USERNAME));
                                            json.put("FULL_NAME", EscapeUtils.CheckTextNull(temp1.FULL_NAME));
                                            json.put("EMAIL", EscapeUtils.CheckTextNull(temp1.EMAIL));
                                            json.put("CREATED_DT", EscapeUtils.CheckTextNull(temp1.CREATED_DT));
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadjsonpushnoti": {
                                //<editor-fold defaultstate="collapsed" desc="loadjsonpushnoti">
                                String vJSON = EscapeUtils.CheckTextNull(request.getParameter("vJSON"));
                                if (!"".equals(vJSON)) {
                                    ObjectMapper oMapperParse = new ObjectMapper();
                                    PUSH_TOKEN itemParsePush = oMapperParse.readValue(vJSON, PUSH_TOKEN.class);
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "0");
                                    json.put("PUSH_NOTICE_TEXT_COLOR", EscapeUtils.CheckTextNull(itemParsePush.PUSH_NOTICE_TEXT_COLOR));
                                    json.put("PUSH_NOTICE_BGR_COLOR", EscapeUtils.CheckTextNull(itemParsePush.PUSH_NOTICE_BGR_COLOR));
                                    json.put("PUSH_NOTICE_URL", EscapeUtils.CheckTextNull(itemParsePush.PUSH_NOTICE_URL));
                                    json.put("PUSH_NOTICE_CONTENT", EscapeUtils.CheckTextNull(itemParsePush.PUSH_NOTICE_CONTENT));
                                    listJson.add(json);
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadproperties_purpose": {
                                //<editor-fold defaultstate="collapsed" desc="loadproperties_purpose">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    request.getSession(false).setAttribute("SessComponentCertTypeAdd",null);
                                    String idID = request.getParameter("idID");
                                    CERTIFICATION_PURPOSE[][] temp = new CERTIFICATION_PURPOSE[1][];
                                    db.S_BO_CERTIFICATION_PURPOSE_DETAIL(idID, temp);
                                    String sPROPERTIES = "";
                                    if (temp[0].length > 0) {
                                        sPROPERTIES = EscapeUtils.CheckTextNull(temp[0][0].PROPERTIES);
                                    }
                                    //pSessLanguage
                                    if (!"".equals(sPROPERTIES)) {
                                        CERTIFICATION_TYPE_COMPONENT[][] resProfileData = new CERTIFICATION_TYPE_COMPONENT[1][];
                                        CommonFunction.getJsonComponentForCert(sPROPERTIES, resProfileData);
                                        if(resProfileData != null) {
                                            request.getSession(false).setAttribute("SessComponentCertTypeAdd", resProfileData);
                                            if(resProfileData[0].length > 0) {
                                                int j = 1;
                                                for(CERTIFICATION_TYPE_COMPONENT mhIP : resProfileData[0]) {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("Index", j++);
                                                    json.put("NAME", EscapeUtils.CheckTextNull(mhIP.name));
                                                    if (mhIP.require == false) {
                                                        json.put("REQUIRED", "0");
                                                    } else {
                                                        json.put("REQUIRED", "1");
                                                    }
                                                    json.put("PREFIX", EscapeUtils.CheckTextNull(mhIP.prefix));
                                                    if("1".equals(pSessLanguage)) {
                                                        json.put("REMARK", EscapeUtils.CheckTextNull(mhIP.remark));
                                                    } else {
                                                        json.put("REMARK_EN", EscapeUtils.CheckTextNull(mhIP.remarkEn));
                                                    }
                                                    json.put("ATTRIBUTE_TYPE", EscapeUtils.CheckTextNull(mhIP.attributeType));
                                                    listJson.add(json);
                                                    j++;
                                                }
                                            }
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadproperties_purpose_bk": {
                                //<editor-fold defaultstate="collapsed" desc="loadproperties_purpose_bk">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    String idID = request.getParameter("idID");
                                    CERTIFICATION_PURPOSE[][] temp = new CERTIFICATION_PURPOSE[1][];
                                    db.S_BO_CERTIFICATION_PURPOSE_DETAIL(idID, temp);
                                    String sPROPERTIES = "";
                                    if (temp[0].length > 0) {
                                        sPROPERTIES = EscapeUtils.CheckTextNull(temp[0][0].PROPERTIES);
                                    }
                                    if (!"".equals(sPROPERTIES)) {
                                        AddIPRelying cartIP = new AddIPRelying();// (AddIPRelying) request.getSession(false).getAttribute("SessAddIPRelying");
//                                        if (cartIP == null) {
                                        //cartIP = new AddIPRelying();
//                                        }
                                        CERTIFICATE_ATTRIBUTES proParse = objectMapper.readValue(sPROPERTIES, CERTIFICATE_ATTRIBUTES.class);
                                        
                                        for (int i = 0; i < proParse.getAttributes().size(); i++) {
                                            if (!proParse.getAttributes().get(i).getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_RADIOBUTTON)) {
                                                CERTIFICATE_ATTRIBUTES_SESSION mh = new CERTIFICATE_ATTRIBUTES_SESSION();
                                                mh.name = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getName());
                                                mh.prefix = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getPrefix());
                                                mh.require = proParse.getAttributes().get(i).isRequire();
                                                mh.remark = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getRemark());
                                                mh.remarkEn = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getRemarkEn());
                                                mh.attributeType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType());
                                                cartIP.AddIPList(mh);
                                            } else {
                                                CERTIFICATE_ATTRIBUTES_SESSION mh = new CERTIFICATE_ATTRIBUTES_SESSION();
                                                mh.require = proParse.getAttributes().get(i).isRequire();
                                                mh.attributeType = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributeType());
                                                mh.commomNameType = "";
                                                mh.name = "";
                                                mh.prefix = "";
                                                mh.remark = "";
                                                mh.remarkEn = "";
                                                mh.radio = new CERTIFICATE_ATTRIBUTES_RADIO[proParse.getAttributes().get(i).getAttributes().size()];
                                                for (int n = 0; n < proParse.getAttributes().get(i).getAttributes().size(); n++) {
                                                    CERTIFICATE_ATTRIBUTES_RADIO radioChild = new CERTIFICATE_ATTRIBUTES_RADIO();
                                                    radioChild.name = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getName());
                                                    radioChild.prefix = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getPrefix());
                                                    radioChild.remark = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getRemark());
                                                    radioChild.remarkEn = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getRemarkEn());
                                                    mh.radio[n] = radioChild;
                                                }
                                                cartIP.AddIPList(mh);
                                            }
                                        }
                                        
//                                        for (int i = 0; i < proParse.getAttributes().size(); i++) {
//                                            if (!proParse.getAttributes().get(i).getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_RADIOBUTTON)) {
//                                                CERTIFICATE_ATTRIBUTES_SESSION mh = new CERTIFICATE_ATTRIBUTES_SESSION();
//                                                mh.name = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getName());
//                                                mh.prefix = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getPrefix());
//                                                mh.require = proParse.getAttributes().get(i).isRequire();
//                                                mh.remark = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getRemark());
//                                                mh.remarkEn = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getRemarkEn());
//                                                cartIP.AddIPList(mh);
//                                            } else {
//                                                for (int n = 0; n < proParse.getAttributes().get(i).getAttributes().size(); n++) {
//                                                    CERTIFICATE_ATTRIBUTES_SESSION mh = new CERTIFICATE_ATTRIBUTES_SESSION();
//                                                    mh.name = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getName());
//                                                    mh.prefix = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getPrefix());
//                                                    mh.require = proParse.getAttributes().get(i).isRequire();
//                                                    mh.remark = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getRemark());
//                                                    mh.remarkEn = EscapeUtils.CheckTextNull(proParse.getAttributes().get(i).getAttributes().get(n).getRemarkEn());
//                                                    cartIP.AddIPList(mh);
//                                                }
//                                            }
//                                        }
                                        request.getSession(false).setAttribute("SessAddIPRelying", cartIP);
                                        int j = 1;
                                        ArrayList<CERTIFICATE_ATTRIBUTES_SESSION> ds = cartIP.getGH();
                                        for (CERTIFICATE_ATTRIBUTES_SESSION mhIP : ds) {
                                            if(mhIP.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_RADIOBUTTON))
                                            {
                                                for (CERTIFICATE_ATTRIBUTES_RADIO radio : mhIP.radio) {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("Index", j++);
                                                    json.put("NAME", radio.name);
                                                    if (mhIP.require == false) {
                                                        json.put("REQUIRED", "0");
                                                    } else {
                                                        json.put("REQUIRED", "1");
                                                    }
                                                    json.put("PREFIX", radio.prefix);
                                                    json.put("REMARK", radio.remark);
                                                    json.put("REMARK_EN", radio.remarkEn);
                                                    listJson.add(json);
                                                }
                                            } else {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("Index", j++);
                                                json.put("NAME", mhIP.name);
                                                if (mhIP.require == false) {
                                                    json.put("REQUIRED", "0");
                                                } else {
                                                    json.put("REQUIRED", "1");
                                                }
                                                json.put("PREFIX", mhIP.prefix);
                                                json.put("REMARK", mhIP.remark);
                                                json.put("REMARK_EN", mhIP.remarkEn);
                                                listJson.add(json);
                                            }
                                        }
                                    }
//                                        int i = 1;
//                                        for (CERTIFICATION_PURPOSE temp1 : temp[0]) {
//                                            JSONObject json = new JSONObject();
//                                            json.put("Code", "0");
//                                            json.put("Index", i++);
//                                            json.put("ID", String.valueOf(temp1.ID));
//                                            listJson.add(json);
//                                        }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "gettokenlistreissue": {
                                //<editor-fold defaultstate="collapsed" desc="gettokenlistreissue">
                                String FromDate = request.getParameter("FromDate");
                                String ToDate = EscapeUtils.CheckTextNull(request.getParameter("ToDate"));
                                String TOKEN_ID = EscapeUtils.CheckTextNull(request.getParameter("TOKEN_ID"));
                                String TOKEN_VERSION = EscapeUtils.CheckTextNull(request.getParameter("TOKEN_VERSION"));
                                String AGENT_ID = EscapeUtils.CheckTextNull(request.getParameter("AGENT_ID"));
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_VERSION)) {
                                    TOKEN_VERSION = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
                                    AGENT_ID = "";
                                }
                                TOKEN[][] temp = new TOKEN[1][];
                                String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                int ss = db.S_BO_TOKEN_TOTAL(EscapeUtils.escapeHtml(FromDate), EscapeUtils.escapeHtml(ToDate),
                                        EscapeUtils.escapeHtmlSearch(TOKEN_ID), "2",
                                        EscapeUtils.escapeHtmlSearch(TOKEN_VERSION), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                        "", "", "", "", "","", "","", sessTreeArrayBranchID, "", "", "");
                                if (ss > 0) {
                                    db.S_BO_TOKEN_LIST(EscapeUtils.escapeHtml(FromDate), EscapeUtils.escapeHtml(ToDate),
                                        EscapeUtils.escapeHtmlSearch(TOKEN_ID), "2",
                                        EscapeUtils.escapeHtmlSearch(TOKEN_VERSION),
                                        EscapeUtils.escapeHtmlSearch(AGENT_ID), pSessLanguage, temp,
                                        Definitions.CONFIG_PAGE_SIZE_IPAGNO, Definitions.CONFIG_PAGE_SIZE_ISWRWS,
                                        "", "", "", "", "","", "","", sessTreeArrayBranchID, "","","");
                                }
                                if (temp[0] != null) {
                                    int sSTT = 0;
                                    for (TOKEN temp1 : temp[0]) {
                                        JSONObject json = new JSONObject();
                                        sSTT++;
                                        String strTOKEN_SN = EscapeUtils.CheckTextNull(temp1.TOKEN_SN);
                                        String strTOKEN_VERSION_DESC = EscapeUtils.CheckTextNull(temp1.TOKEN_VERSION_DESC);
                                        String strTOKEN_STATE_DESC = EscapeUtils.CheckTextNull(temp1.TOKEN_STATE_DESC);
                                        String strCREATED_DT = EscapeUtils.CheckTextNull(temp1.CREATED_DT);
                                        String strBRANCH_DESC = EscapeUtils.CheckTextNull(temp1.BRANCH_DESC);
                                        json.put("Code", "0");
                                        json.put("STT", String.valueOf(sSTT));
                                        json.put("strID", String.valueOf(temp1.ID));
                                        json.put("strTOKEN_SN", strTOKEN_SN);
                                        json.put("strTOKEN_VERSION_DESC", strTOKEN_VERSION_DESC);
                                        json.put("strTOKEN_STATE_DESC", strTOKEN_STATE_DESC);
                                        json.put("strCREATED_DT", strCREATED_DT);
                                        json.put("strBRANCH_DESC", strBRANCH_DESC);
                                        listJson.add(json);
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "getcompanyinfomst": {
                                //<editor-fold defaultstate="collapsed" desc="getcompanyinfomst">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                String vMST = request.getParameter("vMST");
                                ENTERPRISE_INFO[][] rsENTERPRISE_INFO = new ENTERPRISE_INFO[1][];
                                int[] intRes = new int[1];
                                String[] sRes = new String[1];
                                ConnectConnector.GetCompanyFromTaxCode(vMST, rsENTERPRISE_INFO, intRes, sRes);
                                if(intRes[0] == 30){
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA);
                                        listJson.add(json);
                                    }
                                } else {
                                    if (rsENTERPRISE_INFO[0] != null && rsENTERPRISE_INFO[0].length > 0) {
                                        String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                        String sPROVINCE_ID = "";
                                        String sPROVINCE_NAME = EscapeUtils.CheckTextNull(rsENTERPRISE_INFO[0][0].PROVINCE);
                                        if(!"".equals(sPROVINCE_NAME)) {
                                            sPROVINCE_NAME = sPROVINCE_NAME.toUpperCase().replace("THÀNH PHỐ", "");
                                            sPROVINCE_NAME = sPROVINCE_NAME.toUpperCase().replace("TP", "");
                                            sPROVINCE_NAME = sPROVINCE_NAME.toUpperCase().replace("TỈNH", "");
                                            CITY_PROVINCE[][] rsProvince = new CITY_PROVINCE[1][];
                                            db.S_BO_PROVINCE_COMBOBOX(pSessLanguage, rsProvince);
                                            if(rsProvince[0].length > 0) {
                                                for(CITY_PROVINCE rsProvince1 : rsProvince[0]) {
                                                    String sRemark = EscapeUtils.CheckTextNull(rsProvince1.REMARK).toUpperCase();
                                                    if(sRemark.contains(sPROVINCE_NAME.trim())) {
                                                        sPROVINCE_ID = String.valueOf(rsProvince1.ID);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "0");
                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                            json.put("LOCALTION", EscapeUtils.CheckTextNull(rsENTERPRISE_INFO[0][0].LOCALTION));
                                        } else {
                                            json.put("LOCALTION", EscapeUtils.CheckTextNull(rsENTERPRISE_INFO[0][0].ADDRESS));
                                        }
                                        json.put("NAME", EscapeUtils.CheckTextNull(rsENTERPRISE_INFO[0][0].NAME));
                                        json.put("PROVINCE", sPROVINCE_ID);
                                        json.put("ADDRESS", EscapeUtils.CheckTextNull(rsENTERPRISE_INFO[0][0].ADDRESS));
                                        json.put("PRESENTATIVE_NAME", EscapeUtils.CheckTextNull(rsENTERPRISE_INFO[0][0].PRESENTATIVE_NAME));
                                        String beforeDate = EscapeUtils.CheckTextNull(rsENTERPRISE_INFO[0][0].ISSUE_DATE);
//                                        String laterDate = "";
//                                        if(!"".equals(beforeDate)) {
//                                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//                                            SimpleDateFormat sdf2 = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY);
//                                            laterDate = sdf2.format(sdf1.parse(beforeDate));
//                                        }
                                        json.put("ISSUE_DATE", beforeDate);
                                        json.put("PLACEOF_ISSUE", EscapeUtils.CheckTextNull(rsENTERPRISE_INFO[0][0].PLACEOF_ISSUE));
                                        json.put("POSITION", EscapeUtils.CheckTextNull(rsENTERPRISE_INFO[0][0].POSITION));
                                        json.put("PERMANENT_ADDRESS", EscapeUtils.CheckTextNull(rsENTERPRISE_INFO[0][0].PERMANENT_ADDRESS));
                                        json.put("CCCD", EscapeUtils.CheckTextNull(rsENTERPRISE_INFO[0][0].CCCD));
                                        listJson.add(json);
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                }
                                
//                                } else {
//                                    JSONObject json = new JSONObject();
//                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
//                                    listJson.add(json);
//                                }
                                break;
                                //</editor-fold>
                            }
                            case "listcertreportquick": {
                                //<editor-fold defaultstate="collapsed" desc="listcertreportquick">
                                String Tag_ID = EscapeUtils.CheckTextNull(request.getParameter("Tag_ID"));
                                if (!"".equals(Tag_ID)) {
                                    String anticsrf = Tag_ID.split("###")[5];
                                    if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                        String sessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                        String pFROM_DT = Tag_ID.split("###")[0];
                                        String pTO_DT = Tag_ID.split("###")[1];
                                        String pBranchID = Tag_ID.split("###")[2];
                                        String pUserID = Tag_ID.split("###")[3];
                                        String pTypeID = Tag_ID.split("###")[4];
                                        String pBRANCH_NAME = Tag_ID.split("###")[6];
                                        CERTIFICATION[][] temp = new CERTIFICATION[1][];
                                        db.S_BO_REPORT_TOTAL_BRANCH_USER_LIST(pFROM_DT, pTO_DT, pBranchID, pUserID, pTypeID,
                                            pSessLanguage, temp, sessUserAgentID);
                                        if (temp.length > 0) {
                                            int i = 1;
                                            for (CERTIFICATION temp1 : temp[0]) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("Index", i++);
                                                json.put("COMPANY_NAME", EscapeUtils.CheckTextNull(temp1.COMPANY_NAME));
                                                json.put("PERSONAL_NAME", EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME));
                                                json.put("PERSONAL_ID", temp1.PERSONAL_ID);
                                                json.put("ENTERPRISE_ID", temp1.ENTERPRISE_ID);
                                                json.put("PERSONAL_ID_REMARK", temp1.PERSONAL_ID_REMARK);
                                                json.put("ENTERPRISE_ID_REMARK", temp1.ENTERPRISE_ID_REMARK);
                                                json.put("TAX_CODE", EscapeUtils.CheckTextNull(temp1.TAX_CODE));
                                                json.put("BUDGET_CODE", EscapeUtils.CheckTextNull(temp1.BUDGET_CODE));
                                                json.put("DECISION", EscapeUtils.CheckTextNull(temp1.DECISION));
                                                json.put("P_ID", EscapeUtils.CheckTextNull(temp1.P_ID));
                                                json.put("P_EID", EscapeUtils.CheckTextNull(temp1.P_EID));
                                                json.put("PASSPORT", EscapeUtils.CheckTextNull(temp1.PASSPORT));
                                                json.put("CERTIFICATION_PROFILE_DESC", EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_DESC));
                                                json.put("CERTIFICATION_PURPOSE_DESC", EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PURPOSE_DESC));
                                                json.put("CERTIFICATION_STATE_DESC", EscapeUtils.CheckTextNull(temp1.CERTIFICATION_STATE_DESC));
                                                json.put("BRANCH_NAME", pBRANCH_NAME);
                                                listJson.add(json);
                                            }
                                        }
                                        if (listJson.size() <= 0) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "1");
                                            listJson.add(json);
                                        }
                                    } else {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                        listJson.add(json);
                                    }
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loaduser_ofagency": {
                                //<editor-fold defaultstate="collapsed" desc="loaduser_ofagency">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String BRANCH_ID = request.getParameter("BRANCH_ID");
                                    BACKOFFICE_USER[][] temp = new BACKOFFICE_USER[1][];
                                    db.S_BO_GET_USER_BRANCH(BRANCH_ID, temp);
                                    if (temp[0].length > 0) {
                                        for (BACKOFFICE_USER temp1 : temp[0]) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("ID", String.valueOf(temp1.ID));
                                            json.put("USERNAME", EscapeUtils.CheckTextNull(temp1.USERNAME));
                                            json.put("FULL_NAME", EscapeUtils.CheckTextNull(temp1.FULL_NAME));
                                            json.put("ROLE_ID", String.valueOf(temp1.ROLE_ID));
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadadminuser_ofagency": {
                                //<editor-fold defaultstate="collapsed" desc="loadadminuser_ofagency">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                String sessRoleID_ID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
                                String sessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                String BRANCH_ID = request.getParameter("BRANCH_ID");
                                BACKOFFICE_USER[][] temp;
                                if(sessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN) || sessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                {
                                    temp = new BACKOFFICE_USER[1][];
                                    db.S_BO_GET_USER_BRANCH_ALL(BRANCH_ID, temp);
                                    if (temp[0].length > 0) {
                                        for (BACKOFFICE_USER temp1 : temp[0]) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("ID", String.valueOf(temp1.ID));
                                            json.put("USERNAME", EscapeUtils.CheckTextNull(temp1.USERNAME));
                                            json.put("FULL_NAME", EscapeUtils.CheckTextNull(temp1.FULL_NAME));
                                            json.put("ROLE_ID", String.valueOf(temp1.ROLE_ID));
                                            listJson.add(json);
                                        }
                                    }
                                } else {
                                    if(sessUserAgentID.equals(BRANCH_ID)) {
                                        String sessUserID = request.getSession(false).getAttribute("UserID").toString().trim();
                                        temp = new BACKOFFICE_USER[1][];
                                        db.S_BO_USER_DETAIL(sessUserID, pSessLanguage, temp);
                                        if(temp[0].length > 0) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("ID", String.valueOf(temp[0][0].ID));
                                            json.put("USERNAME", EscapeUtils.CheckTextNull(temp[0][0].USERNAME));
                                            json.put("FULL_NAME", EscapeUtils.CheckTextNull(temp[0][0].FULL_NAME));
                                            json.put("ROLE_ID", String.valueOf(temp[0][0].ROLE_ID));
                                            listJson.add(json);
                                        }
                                    } else {
                                        temp = new BACKOFFICE_USER[1][];
                                        db.S_BO_GET_USER_BRANCH_ALL(BRANCH_ID, temp);
                                        if (temp[0].length > 0) {
                                            for (BACKOFFICE_USER temp1 : temp[0]) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("ID", String.valueOf(temp1.ID));
                                                json.put("USERNAME", EscapeUtils.CheckTextNull(temp1.USERNAME));
                                                json.put("FULL_NAME", EscapeUtils.CheckTextNull(temp1.FULL_NAME));
                                                json.put("ROLE_ID", String.valueOf(temp1.ROLE_ID));
                                                listJson.add(json);
                                            }
                                        }
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
//                                } else {
//                                    JSONObject json = new JSONObject();
//                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
//                                    listJson.add(json);
//                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadfunctions_forrole": {
                                //<editor-fold defaultstate="collapsed" desc="loadfunctions_forrole">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sJSON = "";
                                    String idRole = request.getParameter("idRole");
                                    String idLanguage = request.getParameter("idLanguage");
                                    ROLE[][] tempROLE = new ROLE[1][];
                                    db.S_BO_ROLE_DETAIL(idRole, tempROLE);
                                    if (tempROLE[0].length > 0) {
                                        sJSON = EscapeUtils.CheckTextNull(tempROLE[0][0].PROPERTIES);
                                    }
                                    if (!"".equals(sJSON)) {
//                                        SessionRoleFunctions cartToken = (SessionRoleFunctions) sessionsa.getAttribute("sessRoleFunctionsToken");
                                        SessionRoleFunctions cartToken = new SessionRoleFunctions();
//                                        if (cartToken == null) {
//                                            cartToken = new SessionRoleFunctions();
//                                        }
//                                        SessionRoleFunctions cartCert = (SessionRoleFunctions) sessionsa.getAttribute("sessRoleFunctionsCert");
                                        SessionRoleFunctions cartCert = new SessionRoleFunctions();
//                                        if (cartCert == null) {
//                                            cartCert = new SessionRoleFunctions();
//                                        }
//                                        SessionRoleFunctions cartAnother = (SessionRoleFunctions) sessionsa.getAttribute("sessRoleFunctionsAnother");
                                        SessionRoleFunctions cartAnother = new SessionRoleFunctions();
//                                        if (cartAnother == null) {
//                                            cartAnother = new SessionRoleFunctions();
//                                        }
                                        ROLE_DATA[][] rsToken = new ROLE_DATA[1][];
                                        CommonFunction.LoadRoleToken(sJSON, rsToken);
                                        if (rsToken[0].length > 0) {
                                            for (ROLE_DATA rsRole1 : rsToken[0]) {
                                                String sEnabled = "1";
                                                if (rsRole1.enabled == false) {
                                                    sEnabled = "0";
                                                }
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("name", EscapeUtils.CheckTextNull(rsRole1.name));
                                                json.put("attributeType", Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN);
                                                json.put("attributeTypeChilrent", EscapeUtils.CheckTextNull(rsRole1.attributeType));
                                                String sRemark = EscapeUtils.CheckTextNull(rsRole1.remarkEn);
                                                if ("1".equals(idLanguage)) {
                                                    sRemark = EscapeUtils.CheckTextNull(rsRole1.remark);
                                                }
                                                json.put("remark", sRemark);
                                                json.put("enabled", sEnabled);
                                                listJson.add(json);
                                                cartToken.AddRoleFunctionsList(rsRole1);
                                            }
                                            sessionsa.setAttribute("sessRoleFunctionsToken", cartToken);
                                        }
                                        ROLE_DATA[][] rsCert = new ROLE_DATA[1][];
                                        ROLE_DATA[][] rsCertLast = new ROLE_DATA[1][];
                                        CommonFunction.LoadRoleCertificate(sJSON, rsCert);
                                        if (rsCert[0].length > 0) {
                                            CommonFunction.checkAddPermissionUser(Definitions.CONFIG_ROLE_PROPERTIES_CERT_BUY_MORE,
                                                Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT,
                                                rsCert, rsCertLast);
                                            for (ROLE_DATA rsRole1 : rsCertLast[0]) {
                                                String sEnabled = "1";
                                                if (rsRole1.enabled == false) {
                                                    sEnabled = "0";
                                                }
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("name", EscapeUtils.CheckTextNull(rsRole1.name));
                                                json.put("attributeType", Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT);
                                                json.put("attributeTypeChilrent", EscapeUtils.CheckTextNull(rsRole1.attributeType));
                                                String sRemark = EscapeUtils.CheckTextNull(rsRole1.remarkEn);
                                                if ("1".equals(idLanguage)) {
                                                    sRemark = EscapeUtils.CheckTextNull(rsRole1.remark);
                                                }
                                                json.put("remark", sRemark);
                                                json.put("enabled", sEnabled);
                                                listJson.add(json);
                                                cartCert.AddRoleFunctionsList(rsRole1);
                                            }
                                            sessionsa.setAttribute("sessRoleFunctionsCert", cartCert);
                                        }
                                        ROLE_DATA[][] rsAnother = new ROLE_DATA[1][];
                                        CommonFunction.LoadRoleAnother(sJSON, rsAnother);
                                        if (rsAnother[0].length > 0) {
                                            for (ROLE_DATA rsRole1 : rsAnother[0]) {
                                                String sEnabled = "1";
                                                if (rsRole1.enabled == false) {
                                                    sEnabled = "0";
                                                }
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("name", EscapeUtils.CheckTextNull(rsRole1.name));
                                                json.put("attributeType", Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_ANOTHER);
                                                json.put("attributeTypeChilrent", EscapeUtils.CheckTextNull(rsRole1.attributeType));
                                                String sRemark = EscapeUtils.CheckTextNull(rsRole1.remarkEn);
                                                if ("1".equals(idLanguage)) {
                                                    sRemark = EscapeUtils.CheckTextNull(rsRole1.remark);
                                                }
                                                json.put("remark", sRemark);
                                                json.put("enabled", sEnabled);
                                                listJson.add(json);
                                                cartAnother.AddRoleFunctionsList(rsRole1);
                                            }
                                            sessionsa.setAttribute("sessRoleFunctionsAnother", cartAnother);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadfunctions_forroleadd": {
                                //<editor-fold defaultstate="collapsed" desc="loadfunctions_forroleadd">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                    String sJSON = "";
                                    String idRole = request.getParameter("idRole");
                                    String idLanguage = request.getParameter("idLanguage");
                                    ROLE[][] tempROLE = new ROLE[1][];
                                    db.S_BO_ROLE_DETAIL(idRole, tempROLE);
                                    if (tempROLE[0].length > 0) {
                                        sJSON = EscapeUtils.CheckTextNull(tempROLE[0][0].PROPERTIES);
                                    }
                                    if (!"".equals(sJSON)) {
                                        String roleChilrentAgent = "0";
                                        if(!sessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CA)
                                            && !sessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)
                                            && !sessLevelBranch.equals(""))
                                        {
                                            roleChilrentAgent = "1";
                                        }
//                                        SessionRoleFunctions cartToken = (SessionRoleFunctions) sessionsa.getAttribute("sessRoleFunctionsToken");
                                        SessionRoleFunctions cartToken = new SessionRoleFunctions();
//                                        if (cartToken == null) {
//                                            cartToken = new SessionRoleFunctions();
//                                        }
//                                        SessionRoleFunctions cartCert = (SessionRoleFunctions) sessionsa.getAttribute("sessRoleFunctionsCert");
                                        SessionRoleFunctions cartCert = new SessionRoleFunctions();
//                                        if (cartCert == null) {
//                                            cartCert = new SessionRoleFunctions();
//                                        }
//                                        SessionRoleFunctions cartAnother = (SessionRoleFunctions) sessionsa.getAttribute("sessRoleFunctionsAnother");
                                        SessionRoleFunctions cartAnother = new SessionRoleFunctions();
//                                        if (cartAnother == null) {
//                                            cartAnother = new SessionRoleFunctions();
//                                        }
                                        ROLE_DATA[][] rsToken = new ROLE_DATA[1][];
                                        CommonFunction.LoadRoleToken(sJSON, rsToken);
                                        if (rsToken[0].length > 0) {
                                            for (ROLE_DATA rsRole1 : rsToken[0]) {
                                                if(rsRole1.enabled == true) {
                                                    String sEnabled = "1";
                                                    if (rsRole1.enabled == false) {
                                                        sEnabled = "0";
                                                    }
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("name", EscapeUtils.CheckTextNull(rsRole1.name));
                                                    json.put("attributeType", Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN);
                                                    json.put("attributeTypeChilrent", EscapeUtils.CheckTextNull(rsRole1.attributeType));
                                                    String sRemark = EscapeUtils.CheckTextNull(rsRole1.remarkEn);
                                                    if ("1".equals(idLanguage)) {
                                                        sRemark = EscapeUtils.CheckTextNull(rsRole1.remark);
                                                    }
                                                    json.put("remark", sRemark);
                                                    json.put("enabled", sEnabled);
                                                    json.put("disableOption", "0");
                                                    listJson.add(json);
                                                    cartToken.AddRoleFunctionsList(rsRole1);
                                                }
                                            }
                                            sessionsa.setAttribute("sessRoleFunctionsToken", cartToken);
                                        }
                                        ROLE_DATA[][] rsCert = new ROLE_DATA[1][];
                                        ROLE_DATA[][] rsCertLast = new ROLE_DATA[1][];
                                        CommonFunction.LoadRoleCertificate(sJSON, rsCert);
                                        if (rsCert[0].length > 0) {
                                            CommonFunction.checkAddPermissionUser(Definitions.CONFIG_ROLE_PROPERTIES_CERT_BUY_MORE,
                                                Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT,
                                                rsCert, rsCertLast);
                                            for (ROLE_DATA rsRole1 : rsCertLast[0]) {
                                                String disableRolePreApprove = "0";
                                                if(rsRole1.enabled == true) {
                                                    String sEnabled = "1";
                                                    if (rsRole1.enabled == false) {
                                                        sEnabled = "0";
                                                    }
                                                    if("1".equals(roleChilrentAgent)) {
                                                        if(rsRole1.name.equals(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED)
                                                            && rsRole1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST))
                                                        {
                                                            disableRolePreApprove = "1";
                                                            sEnabled = "0";
                                                            rsRole1.enabled = false;
                                                        }
                                                    }
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("name", EscapeUtils.CheckTextNull(rsRole1.name));
                                                    json.put("attributeType", Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT);
                                                    json.put("attributeTypeChilrent", EscapeUtils.CheckTextNull(rsRole1.attributeType));
                                                    String sRemark = EscapeUtils.CheckTextNull(rsRole1.remarkEn);
                                                    if ("1".equals(idLanguage)) {
                                                        sRemark = EscapeUtils.CheckTextNull(rsRole1.remark);
                                                    }
                                                    json.put("remark", sRemark);
                                                    json.put("enabled", sEnabled);
                                                    json.put("disableOption", disableRolePreApprove);
                                                    listJson.add(json);
                                                    cartCert.AddRoleFunctionsList(rsRole1);
                                                }
                                            }
                                            sessionsa.setAttribute("sessRoleFunctionsCert", cartCert);
                                        }
                                        ROLE_DATA[][] rsAnother = new ROLE_DATA[1][];
                                        CommonFunction.LoadRoleAnother(sJSON, rsAnother);
                                        if (rsAnother[0].length > 0) {
                                            for (ROLE_DATA rsRole1 : rsAnother[0]) {
                                                if(rsRole1.enabled == true) {
                                                    String sEnabled = "1";
                                                    if (rsRole1.enabled == false) {
                                                        sEnabled = "0";
                                                    }
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("name", EscapeUtils.CheckTextNull(rsRole1.name));
                                                    json.put("attributeType", Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_ANOTHER);
                                                    json.put("attributeTypeChilrent", EscapeUtils.CheckTextNull(rsRole1.attributeType));
                                                    String sRemark = EscapeUtils.CheckTextNull(rsRole1.remarkEn);
                                                    if ("1".equals(idLanguage)) {
                                                        sRemark = EscapeUtils.CheckTextNull(rsRole1.remark);
                                                    }
                                                    json.put("remark", sRemark);
                                                    json.put("enabled", sEnabled);
                                                    json.put("disableOption", "0");
                                                    listJson.add(json);
                                                    cartAnother.AddRoleFunctionsList(rsRole1);
                                                }
                                            }
                                            sessionsa.setAttribute("sessRoleFunctionsAnother", cartAnother);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadrole_forbranch": {
                                //<editor-fold defaultstate="collapsed" desc="loadrole_forbranch">
                                String idParent = EscapeUtils.CheckTextNull(request.getParameter("idParent"));
                                ROLE[][] tempROLE = new ROLE[1][];
                                db.S_BO_ROLE_COMBOBOX(pSessLanguage, tempROLE);
                                if (tempROLE[0].length > 0) {
                                    if (!"1".equals(idParent)) {
                                        for (ROLE rsRole1 : tempROLE[0]) {
                                            if (rsRole1.CA_ENABLED == false) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("ID", String.valueOf(rsRole1.ID));
                                                json.put("REMARK", EscapeUtils.CheckTextNull(rsRole1.REMARK));
                                                listJson.add(json);
                                            }
                                        }
                                    } else {
                                        for (ROLE rsRole1 : tempROLE[0]) {
                                            if (rsRole1.CA_ENABLED == true) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("ID", String.valueOf(rsRole1.ID));
                                                json.put("REMARK", EscapeUtils.CheckTextNull(rsRole1.REMARK));
                                                listJson.add(json);
                                            }
                                        }
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "gettokenforreissue": {
                                //<editor-fold defaultstate="collapsed" desc="gettokenforreissue">
                                String sFromDate = request.getParameter("FromDate");
                                String sToDate = request.getParameter("ToDate");
                                String sIsCheckDate = request.getParameter("isCheckDate");
                                String sSEARCH_TOKEN_ID = request.getParameter("SEARCH_TOKEN_ID");
                                String sSEARCH_BRANCH = request.getParameter("SEARCH_BRANCH");
                                TOKEN[][] temp = new TOKEN[1][];
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(sSEARCH_BRANCH)) {
                                    sSEARCH_BRANCH = "";
                                }
                                if ("0".equals(sIsCheckDate)) {
                                    sFromDate = "";
                                    sToDate = "";
                                }
                                int[] pIa = new int[1];
                                int[] pIb = new int[1];
                                int sCount = db.S_BO_TOKEN_ISSUED_TOTAL(EscapeUtils.escapeHtml(sFromDate), EscapeUtils.escapeHtml(sToDate),
                                        EscapeUtils.escapeHtmlSearch(sSEARCH_TOKEN_ID),
                                        EscapeUtils.escapeHtmlSearch(sSEARCH_BRANCH), pIa, pIb);
                                if (sCount > 0) {
                                    db.S_BO_TOKEN_ISSUED_LIST(String.valueOf(pIa[0]), String.valueOf(pIb[0]),
                                            EscapeUtils.escapeHtmlSearch(sSEARCH_TOKEN_ID), EscapeUtils.escapeHtmlSearch(sSEARCH_BRANCH),
                                            pSessLanguage, temp, Definitions.CONFIG_PAGE_SIZE_IPAGNO, sCount);
                                }
                                if (temp[0] != null) {
                                    int sSTT = 0;
                                    for (TOKEN temp1 : temp[0]) {
                                        JSONObject json = new JSONObject();
                                        sSTT++;
                                        json.put("Code", "0");
                                        json.put("STT", String.valueOf(sSTT));
                                        json.put("TOKEN_ID", String.valueOf(temp1.ID));
                                        json.put("TOKEN_SN", EscapeUtils.CheckTextNull(temp1.TOKEN_SN));
                                        json.put("TOKEN_VERSION_DESC", EscapeUtils.CheckTextNull(temp1.TOKEN_VERSION_DESC));
                                        json.put("TOKEN_STATE_DESC", EscapeUtils.CheckTextNull(temp1.TOKEN_STATE_DESC));
                                        json.put("BRANCH_DESC", EscapeUtils.CheckTextNull(temp1.BRANCH_DESC));
                                        json.put("CREATED_DT", EscapeUtils.CheckTextNull(temp1.CREATED_DT));
                                        listJson.add(json);
                                    }
                                }

                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadfilemanageofpurpose": {
                                //<editor-fold defaultstate="collapsed" desc="loadfilemanageofpurpose">
                                request.getSession(false).setAttribute("sessUploadFileCert", null);
                                String idPurpose = request.getParameter("idPurpose");
                                String attrTypeID = EscapeUtils.CheckTextNull(request.getParameter("attrTypeID"));
                                String attrTypeCode = "";
                                if(!"".equals(attrTypeID)){
                                    if(Integer.parseInt(attrTypeID) == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                        attrTypeCode = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                    }
                                    if(Integer.parseInt(attrTypeID) == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                        attrTypeCode = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                    }
                                    if(Integer.parseInt(attrTypeID) == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                        attrTypeCode = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                    }
                                    if(Integer.parseInt(attrTypeID) == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                        attrTypeCode = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REISSUE;
                                    }
                                    if(Integer.parseInt(attrTypeID) == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                        attrTypeCode = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REVOKE;
                                    }
                                }
                                String sJSON = "";
                                CERTIFICATION_PURPOSE[][] rsPURPOSE = new CERTIFICATION_PURPOSE[1][];
                                db.S_BO_CERTIFICATION_PURPOSE_DETAIL_BY_CERTIFICATION_ATTR_TYPE(idPurpose, attrTypeCode, rsPURPOSE);
                                if(rsPURPOSE[0].length > 0) {
                                    sJSON = EscapeUtils.CheckTextNull(rsPURPOSE[0][0].FILE_PROPERTIES);
                                }
                                if (!"".equals(sJSON)) {
                                    ObjectMapper oMapperParse = new ObjectMapper();
                                    FILE_PROFILE_JSON itemParsePush = oMapperParse.readValue(sJSON, FILE_PROFILE_JSON.class);
                                    for (FILE_PROFILE_JSON.Attribute attribute : itemParsePush.getAttributes()) {
                                        if(attribute.getEnabled() == true) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("NAME", EscapeUtils.CheckTextNull(attribute.getName()));
                                            if("1".equals(pSessLanguage))
                                            {
                                                json.put("REMARK", EscapeUtils.CheckTextNull(attribute.getRemark()));
                                            } else {
                                                json.put("REMARK", EscapeUtils.CheckTextNull(attribute.getRemarkEn()));
                                            }
                                            listJson.add(json);
                                        }
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "sslparsecert": {
                                //<editor-fold defaultstate="collapsed" desc="sslparsecert">
                                if(request.getSession(false).getAttribute("sessCertSSLToken") != null)
                                {
                                    String sCert = request.getSession(false).getAttribute("sessCertSSLToken").toString().trim();
//                                    String sCert = "MIIGDTCCA/WgAwIBAgIQVBB1Vb8TEs+Y7OWS8TvShTANBgkqhkiG9w0BAQsFADCByDELMAkGA1UEBhMCVk4xFDASBgNVBAgTC0hvIENoaSBNaW5oMRQwEgYDVQQHEwtIbyBDaGkgTWluaDFAMD4GA1UEChM3TW9iaWxlLUlEIFRlY2hub2xvZ2llcyBhbmQgU2VydmljZXMgSm9pbnQgU3RvY2sgQ29tcGFueTEnMCUGA1UECxMeTW9iaWxlLUlEIFRlY2huaWNhbCBEZXBhcnRtZW50MSIwIAYDVQQDExlNb2JpbGUtSUQgVHJ1c3RlZCBOZXR3b3JrMB4XDTE4MTEyNzAxNDQwMFoXDTE5MTEyNzAxMzUxM1owgacxFTATBgNVBBQTDDA5MDkxMjM0NTY3ODEjMCEGCSqGSIb3DQEJARYUdGhhbmh0dkBtb2JpbGUtaWQudm4xEzARBgoJkiaJk/IsZAEBDAM5OTkxGzAZBgNVBAMMElRy4bqnbiBWxINuIFRow6BuaDERMA8GA1UEBwwIUXXhuq1uIDIxFzAVBgNVBAgMDkjhu5MgQ2jDrSBNaW5oMQswCQYDVQQGEwJWTjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAONtPQ/8Cq9jeHYsM3nnr9yAD4OmsRNjOHnYEDIJ+8dddZV5bNa9VolQzk5KP6wtemk2rvycqc+STU0B2+j/tBGz4ccvqkz1/erNKjVmiRgvzNiHZiOn7mu7glRMT/pDKiNepO0cKmivFugcRF2eh6KnGnx/em+DR0snXGYW2r0tYDuFVKXb+Zr2CV5n9lvbMAcmNY/QRhvLnqD9G0ZFmYLySfjiuxRUdvkunVF6oCS+Ub7upTkLZXbvSCQhnSWT0t7UnRjaq+RQ0/tzC2EBv1kGYJCvPfPWIoEIsQ6OcrqqDmZpEeFXXiLLRyubDvNlJ6xg2Wrt7ebS4Gis0AW2C1sCAwEAAaOCARAwggEMMD4GCCsGAQUFBwEBBDIwMDAuBggrBgEFBQcwAYYiaHR0cDovL21vYmlsZS1pZC52bi9vY3NwL3Jlc3BvbmRlcjAdBgNVHQ4EFgQUKtzJZbMEZyVA4M7VVRloMwV+7nQwDAYDVR0TAQH/BAIwADAfBgNVHSMEGDAWgBTzZDJ9sjxd5S7gSXy06mIVlHguqzAsBgNVHR8EJTAjMCGgH6AdhhtodHRwOi8vbW9iaWxlLWlkLnZuL2NybC9nZXQwDgYDVR0PAQH/BAQDAgTwMB0GA1UdJQQWMBQGCCsGAQUFBwMCBggrBgEFBQcDBDAfBgNVHREEGDAWgRR0aGFuaHR2QG1vYmlsZS1pZC52bjANBgkqhkiG9w0BAQsFAAOCAgEAGfEIXhSk8d/3sUElJPS9EKiEdhv3GErDOlXeEXfeyxokxBKUzpm6wudfaZ7rOTQ+PcZgjGDYRbjQzxGVuzhy/aHxktmQEuGYaNwkdqUufcyl9hb4CsELihO6E5WRJgvyhStdp4czw37qD4bGVoZ+uYbRyY6tM6wCNGm6QDU8e8wrWOeJ8SXKO/eFe9fKs+yDQdu86xQiD39tCZN7t7SaX09G5T/+ZabJwVnGtlw1oqUM6S5/koH46KCI3eWSxvopY/ZwJ8PUozAI1eOkTNiRC/UadA/azeDL/A6iftVV9M1yqxMcoc19vTMrjHnbdL6kfwZrocGvYXfSGQX3AE253htH4Cb7eSOSjf5BKD2CODQgMJvDnbMKJX4u5m+gd7xgSdb7FcT16qsFKFgbp7JJrJZd+qTBQJkJ1440zxEDSXb9mOP3UQeTYcjzpFkNPe9X27zAMv1diK+SLiK39lY3KZf5uag3QssCwlnLI83q0P1R9pBl4dGwnhSVOfwTxI6ySJsOcR/gstFiq27vWB31jsroJpFIsOkOEfjWkzY49yITKeQ0fpYS+z5UEB4QmHtgbJakMr/K8dwxETWF/Gws0lPcORAPWHOEcqWVjKND7/VUq8M/xaHJmg0kRgM9mkm3VcejBmJFQJn2jwhBPL8eZhpzZ3tAwOi2TZCpsiwsyKY=";
                                    int[] intRes = new int[1];
                                    Object[] sss = new Object[2];
                                    String[] tmp = new String[3];
                                    com.VoidCertificateComponents(sCert, sss, tmp, intRes);
                                    if (intRes[0] == 0 && sss.length > 0) {
                                        Object strSubjectDN = sss[0].toString().replace(", ", "\n");
                                        Object strIssuerDN = sss[1].toString().replace(", ", "\n");
                                        String strNotBefore = EscapeUtils.CheckTextNull(tmp[1]);
                                        String strNotAfter = EscapeUtils.CheckTextNull(tmp[2]);
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "0");
                                        json.put("SUBJECT", strSubjectDN);
                                        json.put("ISSUER", strIssuerDN);
                                        json.put("DATE_VALID",strNotBefore);
                                        json.put("DATE_EXPIRE", strNotAfter);
                                        listJson.add(json);
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "getpropertiesofca": {
                                //<editor-fold defaultstate="collapsed" desc="getpropertiesofca">
                                String type = EscapeUtils.CheckTextNull(request.getParameter("type"));
                                String id = request.getParameter("id");
                                String sSoapProperties = "";
                                CERTIFICATION_AUTHORITY[][] rsRelying = new CERTIFICATION_AUTHORITY[1][];
                                db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(EscapeUtils.escapeHtml(id), rsRelying);
                                if (rsRelying[0].length > 0) {
                                    sSoapProperties = rsRelying[0][0].PROPERTIES;
                                }
                                if (!"".equals(sSoapProperties)) {
                                    PropertiesContent ss = new PropertiesContent();
                                    ArrayList<ProObj> list;
                                    int s = 1;
                                    list = ss.getPropertiesContent(EscapeUtils.CheckTextNull(sSoapProperties));
                                    if("".equals(type)) {
                                        for (int i = 0; i < list.size(); i++) {
                                            String strID = list.get(i).getKey().trim();
                                            String sValueSub = list.get(i).getValue();
                                            if (sValueSub.length() > 58) {
                                                sValueSub = sValueSub.substring(0, 58) + "...";
                                            }
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("NO", s);
                                            json.put("PROPS_KEY", strID);
                                            json.put("PROPS_VALUE", sValueSub);
                                            listJson.add(json);
                                            s++;
                                        }
                                    } else {
                                        for (int i = 0; i < list.size(); i++) {
                                            String strID = list.get(i).getKey().trim();
                                            if(strID.equals(type)) {
                                                String sValueSub = list.get(i).getValue();
//                                                if (sValueSub.length() > 58) {
//                                                    sValueSub = sValueSub.substring(0, 58) + "...";
//                                                }
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("NO", s);
                                                json.put("PROPS_KEY", strID);
                                                json.put("PROPS_VALUE", sValueSub);
                                                listJson.add(json);
                                            }
                                        }
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadprofilepolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="loadprofilepolicybranch">
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfilePolicyAll");
                                if(resProfileData[0].length > 0)
                                {
                                    int i=1;
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                        {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("NO", String.valueOf(i));
                                            if("1".equals(pSessLanguage))
                                            {
                                                json.put("REMARK", EscapeUtils.CheckTextNull(resProfileData1.remark));
                                            } else {
                                                json.put("REMARK", EscapeUtils.CheckTextNull(resProfileData1.remarkEn));
                                            }
                                            json.put("NAME", EscapeUtils.CheckTextNull(resProfileData1.name));
                                            if(resProfileData1.enabled == true)
                                            {
                                                json.put("ACTIVE", "1");
                                            } else {
                                                json.put("ACTIVE", "0");
                                            }
                                            listJson.add(json);
                                            i++;
                                        }
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadfactorpolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="loadfactorpolicybranch">
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfilePolicyAll");
                                if(resProfileData[0].length > 0)
                                {
                                    int i=1;
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PKI_FORMFACTOR_LIST))
                                        {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("NO", String.valueOf(i));
                                            if("1".equals(pSessLanguage))
                                            {
                                                json.put("REMARK", EscapeUtils.CheckTextNull(resProfileData1.remark));
                                            } else {
                                                json.put("REMARK", EscapeUtils.CheckTextNull(resProfileData1.remarkEn));
                                            }
                                            json.put("NAME", EscapeUtils.CheckTextNull(resProfileData1.name));
                                            if(resProfileData1.enabled == true)
                                            {
                                                json.put("ACTIVE", "1");
                                            } else {
                                                json.put("ACTIVE", "0");
                                            }
                                            listJson.add(json);
                                            i++;
                                        }
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            // ip and function access
                            case "loadippolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="loadippolicybranch">
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessIPPolicyAll");
                                if(resProfileData[0].length > 0)
                                {
                                    int i=1;
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_IP_LIST))
                                        {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("NO", String.valueOf(i));
                                            json.put("NAME", EscapeUtils.CheckTextNull(resProfileData1.name));
                                            if(resProfileData1.enabled == true)
                                            {
                                                json.put("ACTIVE", "1");
                                            } else {
                                                json.put("ACTIVE", "0");
                                            }
                                            listJson.add(json);
                                            i++;
                                        }
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadfunctionpolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="loadfunctionpolicybranch">
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessFunctionPolicyAll");
                                if(resProfileData[0].length > 0)
                                {
                                    int i=1;
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_FUNCTION_LIST))
                                        {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("NO", String.valueOf(i));
                                            json.put("NAME", EscapeUtils.CheckTextNull(resProfileData1.name));
                                            if(resProfileData1.enabled == true)
                                            {
                                                json.put("ACTIVE", "1");
                                            } else {
                                                json.put("ACTIVE", "0");
                                            }
                                            listJson.add(json);
                                            i++;
                                        }
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            // load component cert type
                            case "loadcomponentcerttypeadd": {
                                //<editor-fold defaultstate="collapsed" desc="loadcomponentcerttypeadd">
                                CERTIFICATION_TYPE_COMPONENT[][] resProfileData = (CERTIFICATION_TYPE_COMPONENT[][]) request.getSession(false).getAttribute("SessComponentCertTypeAdd");
                                if(resProfileData[0].length > 0)
                                {
                                    int i=1;
                                    for(CERTIFICATION_TYPE_COMPONENT resProfileData1 : resProfileData[0])
                                    {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "0");
                                        json.put("NO", String.valueOf(i));
                                        json.put("NAME", EscapeUtils.CheckTextNull(resProfileData1.name));
                                        json.put("PREFIX", EscapeUtils.CheckTextNull(resProfileData1.prefix));
                                        if("1".equals(pSessLanguage)) {
                                            json.put("REMARK", EscapeUtils.CheckTextNull(resProfileData1.remark));
                                        } else {
                                            json.put("REMARK", EscapeUtils.CheckTextNull(resProfileData1.remarkEn));
                                        }
                                        if(resProfileData1.require == true)
                                        {
                                            json.put("REQUIRE", "1");
                                        } else {
                                            json.put("REQUIRE", "0");
                                        }
                                        json.put("ATTRIBUTE_TYPE", EscapeUtils.CheckTextNull(resProfileData1.attributeType));
                                        listJson.add(json);
                                        i++;
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadfileprofileadd": {
                                //<editor-fold defaultstate="collapsed" desc="loadfileprofileadd">
                                FILE_PROFILE_JSON.Attribute[][] resProfileData = (FILE_PROFILE_JSON.Attribute[][]) request.getSession(false).getAttribute("SessComponentFileProfileAdd");
                                if(resProfileData[0].length > 0)
                                {
                                    int i=1;
                                    for(FILE_PROFILE_JSON.Attribute resProfileData1 : resProfileData[0])
                                    {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "0");
                                        json.put("NO", String.valueOf(i));
                                        json.put("NAME", EscapeUtils.CheckTextNull(resProfileData1.getName()));
                                        if("1".equals(pSessLanguage)) {
                                            json.put("REMARK", EscapeUtils.CheckTextNull(resProfileData1.getRemark()));
                                        } else {
                                            json.put("REMARK", EscapeUtils.CheckTextNull(resProfileData1.getRemarkEn()));
                                        }
                                        if(resProfileData1.getIsRequire() == true)
                                        {
                                            json.put("REQUIRE", "1");
                                        } else {
                                            json.put("REQUIRE", "0");
                                        }
                                        listJson.add(json);
                                        i++;
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            // owner
                            case "chooseownerforcert": {
                                //<editor-fold defaultstate="collapsed" desc="chooseownerforcert">
                                JSONObject json;
                                String idOwner = EscapeUtils.CheckTextNull(request.getParameter("idOwner"));
                                CERTIFICATION_OWNER[][] temp = new CERTIFICATION_OWNER[1][];
                                db.S_BO_CERTIFICATION_OWNER_DETAIL(idOwner, pSessLanguage, temp);
                                if (temp[0].length > 0) {
                                    for (CERTIFICATION_OWNER temp1 : temp[0])
                                    {
                                        json = new JSONObject();
                                        json.put("Code", "0");
                                        json.put("ENTERPRISE_ID", temp1.ENTERPRISE_ID);
                                        json.put("PERSONAL_ID", temp1.PERSONAL_ID);
                                        json.put("EMAIL", temp1.EMAIL_CONTRACT);
                                        json.put("PHONE_NUMBER", temp1.PHONE_CONTRACT);
                                        json.put("TAX_CODE", EscapeUtils.CheckTextNull(temp1.TAX_CODE));
                                        json.put("MNS", EscapeUtils.CheckTextNull(temp1.BUDGET_CODE));
                                        json.put("DECISION", EscapeUtils.CheckTextNull(temp1.DECISION));
                                        json.put("CMND", EscapeUtils.CheckTextNull(temp1.P_ID));
                                        json.put("HC", EscapeUtils.CheckTextNull(temp1.PASSPORT));
                                        json.put("CCCD", EscapeUtils.CheckTextNull(temp1.CITIZEN_ID));
                                        json.put("OWNER_TYPE_ID", String.valueOf(temp1.CERTIFICATION_OWNER_TYPE_ID));
                                        json.put("COMPANY_NAME", temp1.COMPANY_NAME);
                                        json.put("PERSONAL_NAME", temp1.PERSONAL_NAME);
                                        json.put("OWNER_ID", idOwner);
                                        listJson.add(json);
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadownerforregistercert": {
                                //<editor-fold defaultstate="collapsed" desc="loadownerforregistercert">
                                String FromCreateDate = "";
                                String ToCreateDate = "";
                                String idTAXCODE_MODAL = EscapeUtils.CheckTextNull(request.getParameter("idTAXCODE_MODAL"));
                                String idDECISION_MODAL = EscapeUtils.CheckTextNull(request.getParameter("idDECISION_MODAL"));
                                String idBUDGET_CODE_MODAL = EscapeUtils.CheckTextNull(request.getParameter("idBUDGET_CODE_MODAL"));
                                String idCMND_MODAL = EscapeUtils.CheckTextNull(request.getParameter("idCMND_MODAL"));
                                String idCCCD_MODAL = EscapeUtils.CheckTextNull(request.getParameter("idCCCD_MODAL"));
                                String idPASSPORT_MODAL = EscapeUtils.CheckTextNull(request.getParameter("idPASSPORT_MODAL"));
                                String idCOMPANY_NAME_MODAL = EscapeUtils.CheckTextNull(request.getParameter("idCOMPANY_NAME_MODAL"));
                                String idPERSONAL_NAME_MODAL = EscapeUtils.CheckTextNull(request.getParameter("idPERSONAL_NAME_MODAL"));
                                String idEMAIL_MODAL = EscapeUtils.CheckTextNull(request.getParameter("idEMAIL_MODAL"));
                                String idPHONE_MODAL = EscapeUtils.CheckTextNull(request.getParameter("idPHONE_MODAL"));
                                String pOWNER_USER_RSSP = EscapeUtils.CheckTextNull(request.getParameter("pOWNER_USER_RSSP"));
                                String idHasRSSP = EscapeUtils.CheckTextNull(request.getParameter("idHasRSSP"));
                                String CERTIFICATION_OWNER_STATE = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_STATE_ID_OPERATED);
                                Config conf = new Config();
                                String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                JSONObject json;
                                String sError = "0";
                                if("1".equals(idHasRSSP) && "1".equals(sRSSP_ACCESS_ENABLED))
                                {
                                    String[] sResult = new String[2];
                                    RSSPProcessCommon rssp = new RSSPProcessCommon();
                                    PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                    db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                    String sFormFactorPro = "";
                                    if(rsFormfactorPro[0].length > 0) {
                                        sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                    }
                                    CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                    if(credentialAuthen != null) {
                                        List<OwnerInfo> listOwner = rssp.getOwnerInfoForSignCloud(pOWNER_USER_RSSP, idEMAIL_MODAL, idPHONE_MODAL,
                                            idTAXCODE_MODAL, idBUDGET_CODE_MODAL, idCMND_MODAL, idPASSPORT_MODAL, sResult, idCCCD_MODAL, credentialAuthen);
                                        if("0".equals(sResult[0]))
                                        {
                                            if(listOwner.size() > 0)
                                            {
                                                int sID = 1;
                                                for (OwnerInfo listOwner1 : listOwner) {
                                                    json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("ID", sID);
                                                    json.put("TAX_CODE", EscapeUtils.CheckTextNull(listOwner1.getTaxID()));
                                                    json.put("MNS", EscapeUtils.CheckTextNull(listOwner1.getBudgetID()));
                                                    json.put("DECISION", "");
                                                    json.put("CMND", EscapeUtils.CheckTextNull(listOwner1.getPersonalID()));
                                                    json.put("HC", EscapeUtils.CheckTextNull(listOwner1.getPassportID()));
                                                    json.put("CCCD", EscapeUtils.CheckTextNull(listOwner1.getCitizenID()));
                                                    json.put("PERSONAL_NAME", EscapeUtils.CheckTextNull(listOwner1.getPersonalName()));
                                                    json.put("COMPANY_NAME", EscapeUtils.CheckTextNull(listOwner1.getCompanyName()));
                                                    json.put("EMAIL", EscapeUtils.CheckTextNull(listOwner1.getEmail()));
                                                    json.put("PHONE_NUMBER", EscapeUtils.CheckTextNull(listOwner1.getPhone()));
                                                    json.put("OWNER_UUID", EscapeUtils.CheckTextNull(listOwner1.getUuid()));
                                                    json.put("OWNER_USERNAME", EscapeUtils.CheckTextNull(listOwner1.getUsername()));
                                                    listJson.add(json);
                                                    sID++;
                                                }
                                            }
                                        } else {
                                            sError = sResult[0] + " - " + sResult[1];
                                        }
                                    } else {
                                        CommonFunction.LogDebugString(log, "RSSPProcessCommon-getOwnerInfoForSignCloud", Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM);
                                    }
                                } else {
                                    String pENTERPRISE_ID = "";
                                    String pPERSONAL_ID = "";
                                    if(!"".equals(idTAXCODE_MODAL))
                                    {
                                        pENTERPRISE_ID =Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_TAXCODE + idTAXCODE_MODAL;
                                    } else {
                                        if(!"".equals(idBUDGET_CODE_MODAL))
                                        {
                                            pENTERPRISE_ID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_BUDGETCODE + idBUDGET_CODE_MODAL;
                                        } else {
                                            if(!"".equals(idDECISION_MODAL))
                                            {
                                                pENTERPRISE_ID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_DECISION + idDECISION_MODAL;
                                            }
                                        }
                                    }
                                    if(!"".equals(idCMND_MODAL))
                                    {
                                        pPERSONAL_ID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CMND + idCMND_MODAL;
                                    } else {
                                        if(!"".equals(idCCCD_MODAL))
                                        {
                                            pPERSONAL_ID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_CITIZEN_ID + idCCCD_MODAL;
                                        } else {
                                            if(!"".equals(idPASSPORT_MODAL))
                                            {
                                                pPERSONAL_ID = Definitions.CONFIG_CERTIFICATION_OWNER_PREFIX_PASSPORT + idPASSPORT_MODAL;
                                            }
                                        }
                                    }
                                    int ss = db.S_BO_CERTIFICATION_OWNER_TOTAL(FromCreateDate,
                                        ToCreateDate, CERTIFICATION_OWNER_STATE, "" ,EscapeUtils.escapeHtmlSearch(idCOMPANY_NAME_MODAL),
                                        EscapeUtils.escapeHtmlSearch(pENTERPRISE_ID), EscapeUtils.escapeHtmlSearch(idPERSONAL_NAME_MODAL),
                                        EscapeUtils.escapeHtmlSearch(pPERSONAL_ID), idPHONE_MODAL, idEMAIL_MODAL);
                                    CERTIFICATION_OWNER[][] temp = new CERTIFICATION_OWNER[1][];
                                    if (ss > 0) {
                                        db.S_BO_CERTIFICATION_OWNER_LIST(FromCreateDate, ToCreateDate, CERTIFICATION_OWNER_STATE,
                                            "", EscapeUtils.escapeHtmlSearch(idCOMPANY_NAME_MODAL),
                                            EscapeUtils.escapeHtmlSearch(pENTERPRISE_ID), EscapeUtils.escapeHtmlSearch(idPERSONAL_NAME_MODAL),
                                            EscapeUtils.escapeHtmlSearch(pPERSONAL_ID), idPHONE_MODAL, idEMAIL_MODAL,
                                            Integer.parseInt(pSessLanguage), Definitions.CONFIG_PAGE_SIZE_IPAGNO, ss, temp);
                                        if (temp[0].length > 0) {
                                            for (CERTIFICATION_OWNER temp1 : temp[0]) {
                                                if(temp1.CERTIFICATION_OWNER_STATE_ID != Definitions.CONFIG_CERTIFICATION_OWNER_STATE_ID_DISPOSED
                                                    && temp1.CERTIFICATION_OWNER_STATE_ID != Definitions.CONFIG_CERTIFICATION_OWNER_STATE_ID_DECLINED)
                                                {
                                                    json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("ID", String.valueOf(temp1.ID));
                                                    json.put("TAX_CODE", EscapeUtils.CheckTextNull(temp1.TAX_CODE));
                                                    json.put("MNS", EscapeUtils.CheckTextNull(temp1.BUDGET_CODE));
                                                    json.put("DECISION", EscapeUtils.CheckTextNull(temp1.DECISION));
                                                    json.put("CMND", EscapeUtils.CheckTextNull(temp1.P_ID));
                                                    json.put("HC", EscapeUtils.CheckTextNull(temp1.PASSPORT));
                                                    json.put("CCCD", EscapeUtils.CheckTextNull(temp1.CITIZEN_ID));
                                                    json.put("PERSONAL_NAME", temp1.PERSONAL_NAME);
                                                    json.put("COMPANY_NAME", temp1.COMPANY_NAME);
                                                    json.put("EMAIL", temp1.EMAIL_CONTRACT);
                                                    json.put("PHONE_NUMBER", temp1.PHONE_CONTRACT);
                                                    json.put("OWNER_UUID", "");
                                                    json.put("OWNER_USERNAME", "");
                                                    listJson.add(json);
                                                }
                                            }
                                        }
                                    }
                                }
                                if("0".equals(sError)) {
                                    if (listJson.size() <= 0) {
                                        json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    if (listJson.size() <= 0) {
                                        json = new JSONObject();
                                        json.put("Code", "2");
                                        json.put("Message", "Error: " + sError);
                                        listJson.add(json);
                                    }
                                }
                                break;
                                //</editor-fold>
                            }
                            case "choosecertoldforcert": {
                                //<editor-fold defaultstate="collapsed" desc="choosecertoldforcert">
                                JSONObject json;
                                String idCert = EscapeUtils.CheckTextNull(request.getParameter("idOwner"));
                                CERTIFICATION[][] temp = new CERTIFICATION[1][];
                                db.S_BO_CERTIFICATION_DETAIL(idCert, pSessLanguage, temp);
                                if (temp != null && temp[0].length > 0) {
                                    String sADDRESS = "";
                                    String sPOSITION = "";
                                    String sREPRESENTATIVE_EMAIL = "";
                                    String sREPRESENTATIVE_PHONE = "";
                                    String sREPRESENTATIVE_NAME = "";
                                    String sCONTACT_NAME = "";
                                    String PIDIssuedBy = "";
                                    String PIDDate = "";
                                    String PID = "";
                                    String AddressLicense = "";
                                    CERTIFICATION[][] rsBrief = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_BRIEF_DETAIL(idCert, rsBrief);
                                    String sPrfileContact = EscapeUtils.CheckTextNull(rsBrief[0][0].PROFILE_CONTACT_INFO);
                                    if(!"".equals(sPrfileContact)) {
                                        ObjectMapper oMapperParse = new ObjectMapper();
                                        ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                        if(profileContact != null) {
                                            PIDIssuedBy = CommonFunction.replaceCharaterSpecialJson(profileContact.PIDIssuedBy, false);
                                            PIDDate = EscapeUtils.CheckTextNull(profileContact.PIDDate);
                                            PID = EscapeUtils.CheckTextNull(profileContact.PID);
                                            AddressLicense = CommonFunction.replaceCharaterSpecialJson(profileContact.AddressLicense, false);
                                            sREPRESENTATIVE_EMAIL = EscapeUtils.CheckTextNull(profileContact.RepresentativeEmail);
                                            sREPRESENTATIVE_PHONE = EscapeUtils.CheckTextNull(profileContact.RepresentativePhone);
                                            sREPRESENTATIVE_NAME = CommonFunction.replaceCharaterSpecialJson(profileContact.RepresentativeName, false);
                                            sCONTACT_NAME = CommonFunction.replaceCharaterSpecialJson(profileContact.ContactName, false);
                                            sADDRESS = CommonFunction.replaceCharaterSpecialJson(profileContact.Address, false);
                                            sPOSITION = CommonFunction.replaceCharaterSpecialJson(profileContact.Position, false);
                                        }
                                    }
                                    json = new JSONObject();
                                    json.put("Code", "0");
                                    json.put("PID_ISSUEDBY", PIDIssuedBy);
                                    json.put("PID_DATE", PIDDate);
                                    json.put("PID", PID);
                                    json.put("ADDRESS_LICENSE", AddressLicense);
                                    json.put("REPRESENTATIVE_EMAIL", sREPRESENTATIVE_EMAIL);
                                    json.put("REPRESENTATIVE_PHONE", sREPRESENTATIVE_PHONE);
                                    json.put("REPRESENTATIVE_NAME", sREPRESENTATIVE_NAME);
                                    json.put("CONTACT_NAME", sCONTACT_NAME);
                                    json.put("ADDRESS", sADDRESS);
                                    json.put("POSITION", sPOSITION);
                                    
                                    json.put("ENTERPRISE_ID", temp[0][0].ENTERPRISE_ID);
                                    json.put("PERSONAL_ID", temp[0][0].PERSONAL_ID);
                                    json.put("EMAIL", temp[0][0].EMAIL_CONTRACT);
                                    json.put("PHONE_NUMBER", temp[0][0].PHONE_CONTRACT);
                                    json.put("TAX_CODE", EscapeUtils.CheckTextNull(temp[0][0].TAX_CODE));
                                    json.put("MNS", EscapeUtils.CheckTextNull(temp[0][0].BUDGET_CODE));
                                    json.put("DECISION", EscapeUtils.CheckTextNull(temp[0][0].DECISION));
                                    json.put("CMND", EscapeUtils.CheckTextNull(temp[0][0].P_ID));
                                    json.put("HC", EscapeUtils.CheckTextNull(temp[0][0].PASSPORT));
                                    json.put("CCCD", EscapeUtils.CheckTextNull(temp[0][0].P_EID));
                                    json.put("OWNER_TYPE_ID", String.valueOf(temp[0][0].CERTIFICATION_PURPOSE_ID));
                                    json.put("COMPANY_NAME", temp[0][0].COMPANY_NAME);
                                    json.put("PERSONAL_NAME", temp[0][0].PERSONAL_NAME);
                                    json.put("PROVINCE_ID", temp[0][0].CITY_PROVINCE_ID);
                                    String sSubject = EscapeUtils.CheckTextNull(temp[0][0].SUBJECT);
                                    
                                    json.put("LOCATION", CommonFunction.getLocationInDN(sSubject));
                                    json.put("OWNER_ID", idCert);
                                    listJson.add(json);
                                }
                                if (listJson.size() <= 0) {
                                    json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            // rose
                            case "loadpropertiesrose": {
                                //<editor-fold defaultstate="collapsed" desc="loadpropertiesrose">
                                PROFILE_DISCOUNT_RATE_DATA[][] resProfileData = (PROFILE_DISCOUNT_RATE_DATA[][]) request.getSession(false).getAttribute("SessDiscountRateAccess");
                                if(resProfileData != null && resProfileData[0] != null)
                                {
                                    if(resProfileData[0].length > 0) {
                                        int i=1;
                                        for(PROFILE_DISCOUNT_RATE_DATA resProfileData1 : resProfileData[0])
                                        {
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROSE_ATTRIBUTE_TYPE_PROFILE_DISCOUNT_RATE_ITEM))
                                            {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("NO", String.valueOf(i));
                                                json.put("PROFILE_NAME", EscapeUtils.CheckTextNull(resProfileData1.profileName));
                                                if("1".equals(pSessLanguage)) {
                                                    json.put("PROFILE_REMARK", EscapeUtils.CheckTextNull(resProfileData1.profileRemark));
                                                } else {
                                                    json.put("PROFILE_REMARK", EscapeUtils.CheckTextNull(resProfileData1.profileRemarkEN));
                                                }
                                                json.put("PERCENT", EscapeUtils.CheckTextNull(resProfileData1.rosePercent));
                                                if(resProfileData1.isMoneyType == true){
                                                    json.put("ROSE_TYPE", "1");
                                                } else {
                                                    json.put("ROSE_TYPE", "0");
                                                }
                                                listJson.add(json);
                                                i++;
                                            }
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                }
                                break;
                                //</editor-fold>
                            }
                            // profile access
                            case "loadprofileallproperties": {
                                //<editor-fold defaultstate="collapsed" desc="loadprofileallproperties">
                                String sAllEnabled = EscapeUtils.CheckTextNull(request.getParameter("sAllEnabled"));
                                if("1".equals(sAllEnabled))
                                {
                                    String booApproveCAForProfile = "0";
                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy[0].length > 0) {
                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                        {
                                            if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_AUTO_APPROVED_FOR_EACH_CERTIFICATION_PROFILE_OPTION)) {
                                                booApproveCAForProfile = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                    CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                    db.S_BO_CERTIFICATION_PROFILE_FOR_BRANCH("","",rsProfile);
                                    if(rsProfile[0].length > 0)
                                    {
                                        CERTIFICATION_POLICY_DATA[][] reProfileDataLast = new CERTIFICATION_POLICY_DATA[1][];
                                        CERTIFICATION_POLICY_DATA[][] resProfileData;
                                        ArrayList<CERTIFICATION_POLICY_DATA> tempList;
                                        resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfileBranchAccess");
                                        if(resProfileData != null && resProfileData[0] != null && resProfileData[0].length > 0)
                                        {
                                            tempList = new ArrayList<>();
                                            tempList.addAll(Arrays.asList(resProfileData[0]));
                                            for(CERTIFICATION_PROFILE resProfileDB1 : rsProfile[0])
                                            {
                                                boolean isExistsName = false;
                                                for(CERTIFICATION_POLICY_DATA resProfileSess : resProfileData[0])
                                                {
                                                    if(resProfileDB1.NAME.equals(resProfileSess.name)) {
                                                        isExistsName = true;
                                                        break;
                                                    }
                                                }
                                                if(isExistsName == false)
                                                {
                                                    CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                                                    rsItem.name = resProfileDB1.NAME;
                                                    rsItem.remark = resProfileDB1.REMARK;
                                                    rsItem.remarkEn = resProfileDB1.REMARK_EN;
                                                    rsItem.enabled = true;
                                                    rsItem.approveCAEnabled = "1".equals(booApproveCAForProfile);
                                                    rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST;
                                                    tempList.add(rsItem);
                                                }
                                            }
                                            reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                            reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                            request.getSession(false).setAttribute("SessProfileBranchAccess", reProfileDataLast);
                                            if(reProfileDataLast[0].length > 0)
                                            {
                                                int i=1;
                                                boolean hasProfilePolicy = false;
                                                for(CERTIFICATION_POLICY_DATA resProfileReturn : reProfileDataLast[0])
                                                {
                                                    if(resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST)
                                                        || resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE)
                                                        || resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_MAJOR_TYPE))
                                                    {
                                                        hasProfilePolicy = true;
                                                        JSONObject json = new JSONObject();
                                                        json.put("Code", "0");
                                                        json.put("NO", String.valueOf(i));
                                                        json.put("NAME", EscapeUtils.CheckTextNull(resProfileReturn.name));
                                                        if("1".equals(pSessLanguage))
                                                        {
                                                            json.put("REMARK", EscapeUtils.CheckTextNull(resProfileReturn.remark));
                                                        } else {
                                                            json.put("REMARK", EscapeUtils.CheckTextNull(resProfileReturn.remarkEn));
                                                        }
                                                        if(resProfileReturn.enabled == true)
                                                        {
                                                            json.put("ACTIVE", "1");
                                                        } else {
                                                            json.put("ACTIVE", "0");
                                                        }
                                                        if(resProfileReturn.approveCAEnabled == true)
                                                        {
                                                            json.put("APPROVE_CA", "1");
                                                        } else {
                                                            json.put("APPROVE_CA", "0");
                                                        }
                                                        json.put("ATTRIBUTE_TYPE", EscapeUtils.CheckTextNull(resProfileReturn.attributeType));
                                                        listJson.add(json);
                                                        i++;
                                                    }
                                                }
                                                if(hasProfilePolicy == false)
                                                {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "1");
                                                    listJson.add(json);
                                                }
                                            } else {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "1");
                                                listJson.add(json);
                                            }
                                        } else {
                                            tempList = new ArrayList<>();
                                            for(CERTIFICATION_PROFILE resProfileDB1 : rsProfile[0])
                                            {
                                                CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                                                rsItem.name = resProfileDB1.NAME;
                                                rsItem.remark = resProfileDB1.REMARK;
                                                rsItem.remarkEn = resProfileDB1.REMARK_EN;
                                                rsItem.enabled = true;
                                                rsItem.approveCAEnabled = "1".equals(booApproveCAForProfile);
                                                rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST;
                                                tempList.add(rsItem);
                                            }
                                            reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                            reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                            request.getSession(false).setAttribute("SessProfileBranchAccess", reProfileDataLast);
                                            if(reProfileDataLast[0].length > 0)
                                            {
                                                int i=1;
                                                boolean hasProfilePolicy = false;
                                                for(CERTIFICATION_POLICY_DATA resProfileReturn : reProfileDataLast[0])
                                                {
                                                    if(resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST)
                                                        || resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE)
                                                        || resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_MAJOR_TYPE))
                                                    {
                                                        hasProfilePolicy = true;
                                                        JSONObject json = new JSONObject();
                                                        json.put("Code", "0");
                                                        json.put("NO", String.valueOf(i));
                                                        json.put("NAME", EscapeUtils.CheckTextNull(resProfileReturn.name));
                                                        if("1".equals(pSessLanguage))
                                                        {
                                                            json.put("REMARK", EscapeUtils.CheckTextNull(resProfileReturn.remark));
                                                        } else {
                                                            json.put("REMARK", EscapeUtils.CheckTextNull(resProfileReturn.remarkEn));
                                                        }
                                                        if(resProfileReturn.enabled == true)
                                                        {
                                                            json.put("ACTIVE", "1");
                                                        } else {
                                                            json.put("ACTIVE", "0");
                                                        }
                                                        if(resProfileReturn.approveCAEnabled == true)
                                                        {
                                                            json.put("APPROVE_CA", "1");
                                                        } else {
                                                            json.put("APPROVE_CA", "0");
                                                        }
                                                        json.put("ATTRIBUTE_TYPE", EscapeUtils.CheckTextNull(resProfileReturn.attributeType));
                                                        listJson.add(json);
                                                        i++;
                                                    }
                                                }
                                                if(hasProfilePolicy == false)
                                                {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "1");
                                                    listJson.add(json);
                                                }
                                            } else {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "1");
                                                listJson.add(json);
                                            }
                                        }
                                    } else {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    String hasBranchRole = EscapeUtils.CheckTextNull(request.getParameter("hasBranchRole"));
                                    String idBranch = EscapeUtils.CheckTextNull(request.getParameter("idBranch"));
                                    String idCheckAdditionalEnabled = EscapeUtils.CheckTextNull(request.getParameter("idCheckAdditionalEnabled"));
                                    if(!"0".equals(hasBranchRole)) {
                                        String booApproveCAForProfile = "0";
                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                        if (sessGeneralPolicy[0].length > 0) {
                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                            {
                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_AUTO_APPROVED_FOR_EACH_CERTIFICATION_PROFILE_OPTION))
                                                {
                                                    booApproveCAForProfile = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        if(hasBranchRole.equals(Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL)) {
                                            hasBranchRole = "1";
                                        }
                                        BRANCH_ROLE[][] rsBranchRoleDetail = new BRANCH_ROLE[1][];
                                        db.S_BO_BRANCH_ROLE_DETAIL(hasBranchRole, rsBranchRoleDetail);
                                        if(rsBranchRoleDetail[0].length > 0)
                                        {
                                            CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
                                            CommonFunction.getProfileCertNewListForAdmin(rsBranchRoleDetail[0][0].CERTIFICATION_PROFILE_PROPERTIES, resIPData);
                                            if(resIPData != null && resIPData[0] != null && resIPData[0].length > 0)
                                            {
                                                if("0".equals(idCheckAdditionalEnabled)) {
                                                    CERTIFICATION_POLICY_DATA[][] rsProfileDataCurrent = new CERTIFICATION_POLICY_DATA[1][];// = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfileBranchAccess");
                                                    BRANCH[][] rsBranch = new BRANCH[1][];
                                                    db.S_BO_BRANCH_DETAIL(idBranch, rsBranch);
                                                    if (rsBranch[0].length > 0) {
                                                        String pCERTIFICATION_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                                                        if(!"".equals(pCERTIFICATION_PROFILE_PROPERTIES)) {
                                                            CommonFunction.getProfileCertNewListForAdmin(pCERTIFICATION_PROFILE_PROPERTIES, rsProfileDataCurrent);
                                                        }
                                                    }
                                                    ArrayList<CERTIFICATION_POLICY_DATA> tempList;
                                                    if(rsProfileDataCurrent != null && rsProfileDataCurrent[0] != null && rsProfileDataCurrent[0].length > 0)
                                                    {
                                                        tempList = new ArrayList<>();
                                                        tempList.addAll(Arrays.asList(rsProfileDataCurrent[0]));
                                                        for(CERTIFICATION_POLICY_DATA resNewUpdate : resIPData[0])
                                                        {
                                                            boolean isExistsName = false;
                                                            for(CERTIFICATION_POLICY_DATA resProfileSess : rsProfileDataCurrent[0])
                                                            {
                                                                if(resNewUpdate.name.equals(resProfileSess.name)) {
                                                                    isExistsName = true;
                                                                    break;
                                                                }
                                                            }
                                                            if(isExistsName == false)
                                                            {
                                                                CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                                                                rsItem.name = resNewUpdate.name;
                                                                rsItem.remark = resNewUpdate.remark;
                                                                rsItem.remarkEn = resNewUpdate.remarkEn;
                                                                rsItem.enabled = true;
                                                                rsItem.approveCAEnabled = "1".equals(booApproveCAForProfile);
                                                                rsItem.attributeType = resNewUpdate.attributeType;
                                                                tempList.add(rsItem);
                                                            }
                                                        }
                                                        resIPData[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                                        resIPData[0] = tempList.toArray(resIPData[0]);
                                                    }
                                                }
                                                int i=1;
                                                boolean hasProfilePolicy = false;
                                                for(CERTIFICATION_POLICY_DATA resProfileReturn : resIPData[0])
                                                {
                                                    if(resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST)
                                                        || resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE)
                                                        || resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_MAJOR_TYPE))
                                                    {
                                                        hasProfilePolicy = true;
                                                        JSONObject json = new JSONObject();
                                                        json.put("Code", "0");
                                                        json.put("NO", String.valueOf(i));
                                                        json.put("NAME", EscapeUtils.CheckTextNull(resProfileReturn.name));
                                                        if("1".equals(pSessLanguage))
                                                        {
                                                            json.put("REMARK", EscapeUtils.CheckTextNull(resProfileReturn.remark));
                                                        } else {
                                                            json.put("REMARK", EscapeUtils.CheckTextNull(resProfileReturn.remarkEn));
                                                        }
                                                        if(resProfileReturn.enabled == true)
                                                        {
                                                            json.put("ACTIVE", "1");
                                                        } else {
                                                            json.put("ACTIVE", "0");
                                                        }
                                                        if(resProfileReturn.approveCAEnabled == true)
                                                        {
                                                            json.put("APPROVE_CA", "1");
                                                        } else {
                                                            json.put("APPROVE_CA", "0");
                                                        }
                                                        json.put("ATTRIBUTE_TYPE", EscapeUtils.CheckTextNull(resProfileReturn.attributeType));
                                                        listJson.add(json);
                                                        i++;
                                                    }
                                                }
                                                request.getSession(false).setAttribute("SessProfileBranchAccess", resIPData);
                                                if(hasProfilePolicy == false)
                                                {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "11");
                                                    listJson.add(json);
                                                }
                                            } else {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "11");
                                                listJson.add(json);
                                            }
                                        }
                                    } else {
                                        CERTIFICATION_POLICY_DATA[][] resProfileData;
                                        resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfileBranchAccess");
                                        if(resProfileData != null && resProfileData[0] != null && resProfileData[0].length > 0)
                                        {
                                            int i=1;
                                            boolean hasProfilePolicy = false;
                                            for(CERTIFICATION_POLICY_DATA resProfileReturn : resProfileData[0])
                                            {
                                                if(resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST)
                                                    || resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE)
                                                    || resProfileReturn.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_MAJOR_TYPE))
                                                {
                                                    hasProfilePolicy = true;
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("NO", String.valueOf(i));
                                                    json.put("NAME", EscapeUtils.CheckTextNull(resProfileReturn.name));
                                                    if("1".equals(pSessLanguage))
                                                    {
                                                        json.put("REMARK", EscapeUtils.CheckTextNull(resProfileReturn.remark));
                                                    } else {
                                                        json.put("REMARK", EscapeUtils.CheckTextNull(resProfileReturn.remarkEn));
                                                    }
                                                    if(resProfileReturn.enabled == true)
                                                    {
                                                        json.put("ACTIVE", "1");
                                                    } else {
                                                        json.put("ACTIVE", "0");
                                                    }
                                                    if(resProfileReturn.approveCAEnabled == true)
                                                    {
                                                        json.put("APPROVE_CA", "1");
                                                    } else {
                                                        json.put("APPROVE_CA", "0");
                                                    }
                                                    json.put("ATTRIBUTE_TYPE", EscapeUtils.CheckTextNull(resProfileReturn.attributeType));
                                                    listJson.add(json);
                                                    i++;
                                                }
                                            }
                                            if(hasProfilePolicy == false)
                                            {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "1");
                                                listJson.add(json);
                                            }
                                        } else {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "1");
                                            listJson.add(json);
                                        }
                                    }
                                }
                                break;
                                //</editor-fold>
                            }
                            case "neaclistcertlist": {
                                //<editor-fold defaultstate="collapsed" desc="neaclistcertlist">
                                String pOFFSET = EscapeUtils.CheckTextNull(request.getParameter("Tag_ID"));
                                if (!"".equals(pOFFSET)) {
                                    String pYear = request.getSession(false).getAttribute("sessYearDateNEACControl").toString().trim();
                                    String pQUARTER = request.getSession(false).getAttribute("sessMountDateNEACControl").toString().trim();
                                    CERTIFICATION[][] temp = new CERTIFICATION[1][];
                                    db.S_BO_REPORT_NEAC_DETAIL(pQUARTER, pYear, Integer.parseInt(pOFFSET), Integer.parseInt(pSessLanguage), temp);
                                    if (temp.length > 0) {
                                        int i = 1;
                                        for (CERTIFICATION temp1 : temp[0]) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("Index", i++);
                                            json.put("COMPANY_NAME", temp1.COMPANY_NAME);
                                            json.put("PERSONAL_NAME", temp1.PERSONAL_NAME);
                                            json.put("PERSONAL_ID", temp1.PERSONAL_ID);
                                            json.put("PERSONAL_ID_REMARK", temp1.PERSONAL_ID_REMARK);
                                            json.put("ENTERPRISE_ID", temp1.ENTERPRISE_ID);
                                            json.put("ENTERPRISE_ID_REMARK", temp1.ENTERPRISE_ID_REMARK);
                                            json.put("TAX_CODE", temp1.TAX_CODE);
                                            json.put("BUDGET_CODE", temp1.BUDGET_CODE);
                                            json.put("DECISION", temp1.DECISION);
                                            json.put("P_ID", temp1.P_ID);
                                            json.put("P_EID", temp1.P_EID);
                                            json.put("PASSPORT", temp1.PASSPORT);
                                            json.put("DOMAIN_NAME", temp1.DOMAIN_NAME);
                                            json.put("PKI_FORMFACTOR_DESC", temp1.PKI_FORMFACTOR_DESC);
                                            json.put("CERTIFICATION_PURPOSE_DESC", temp1.CERTIFICATION_PURPOSE_DESC);
                                            json.put("CERTIFICATION_PROFILE_NAME", temp1.CERTIFICATION_PROFILE_NAME);
                                            json.put("CERTIFICATION_STATE_DESC", temp1.CERTIFICATION_STATE_DESC);
                                            json.put("CERTIFICATION_ATTR_STATE_DESC", temp1.CERTIFICATION_ATTR_STATE_DESC);
                                            json.put("CERTIFICATION_ATTR_TYPE_DESC", temp1.CERTIFICATION_ATTR_TYPE_DESC);
                                            json.put("CREATED_BY", temp1.CREATED_BY);
                                            json.put("BRANCH_DESC", temp1.BRANCH_DESC);
                                            json.put("CREATED_DT", temp1.CREATED_DT);
                                            json.put("REVOKED_DT", temp1.REVOKED_DT);
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                }
                                break;
                                //</editor-fold>
                            }
                            case "neaclistcertlist2": {
                                //<editor-fold defaultstate="collapsed" desc="neaclistcertlist2">
                                String pOFFSET = EscapeUtils.CheckTextNull(request.getParameter("Tag_ID"));
                                if (!"".equals(pOFFSET)) {
                                    String sPurposeName = pOFFSET.split("#")[0];
                                    String sStatusName = pOFFSET.split("#")[1];
                                    String pYear = request.getSession(false).getAttribute("sessYearDateNEACControl").toString().trim();
                                    String pQUARTER = request.getSession(false).getAttribute("sessMountDateNEACControl").toString().trim();
                                    CERTIFICATION[][] temp = new CERTIFICATION[1][];
                                    db.S_BO_REPORT_PERIODIC_DETAIL(pQUARTER, pYear, sPurposeName, sStatusName, pSessLanguage, temp);
                                    if (temp.length > 0) {
                                        int i = 1;
                                        for (CERTIFICATION temp1 : temp[0]) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("Index", i++);
                                            json.put("COMPANY_NAME", temp1.COMPANY_NAME);
                                            json.put("PERSONAL_NAME", temp1.PERSONAL_NAME);
                                            json.put("PERSONAL_ID", temp1.PERSONAL_ID);
                                            json.put("PERSONAL_ID_REMARK", temp1.PERSONAL_ID_REMARK);
                                            json.put("ENTERPRISE_ID", temp1.ENTERPRISE_ID);
                                            json.put("ENTERPRISE_ID_REMARK", temp1.ENTERPRISE_ID_REMARK);
                                            json.put("TAX_CODE", temp1.TAX_CODE);
                                            json.put("BUDGET_CODE", temp1.BUDGET_CODE);
                                            json.put("DECISION", temp1.DECISION);
                                            json.put("P_ID", temp1.P_ID);
                                            json.put("P_EID", temp1.P_EID);
                                            json.put("PASSPORT", temp1.PASSPORT);
                                            json.put("DOMAIN_NAME", temp1.DOMAIN_NAME);
                                            json.put("PKI_FORMFACTOR_DESC", temp1.PKI_FORMFACTOR_DESC);
                                            json.put("CERTIFICATION_PURPOSE_DESC", temp1.CERTIFICATION_PURPOSE_DESC);
                                            json.put("CERTIFICATION_PROFILE_NAME", temp1.CERTIFICATION_PROFILE_NAME);
                                            json.put("CERTIFICATION_STATE_DESC", temp1.CERTIFICATION_STATE_DESC);
                                            json.put("CERTIFICATION_ATTR_STATE_DESC", temp1.CERTIFICATION_ATTR_STATE_DESC);
                                            json.put("CERTIFICATION_ATTR_TYPE_DESC", temp1.CERTIFICATION_ATTR_TYPE_DESC);
                                            json.put("CREATED_BY", temp1.CREATED_BY);
                                            json.put("BRANCH_DESC", temp1.BRANCH_DESC);
                                            json.put("CREATED_DT", temp1.CREATED_DT);
                                            json.put("REVOKED_DT", temp1.REVOKED_DT);
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadrsspownerlist": {
                                //<editor-fold defaultstate="collapsed" desc="loadrsspownerlist">
                                RSSPProcessCommon rssp = new RSSPProcessCommon();
                                PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                String sFormFactorPro = "";
                                if(rsFormfactorPro[0].length > 0) {
                                    sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                }
                                CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                if(credentialAuthen != null) {
                                    List<String> listRelyingParty = rssp.getRelyingPartiesRSSP(credentialAuthen);
                                    if (listRelyingParty != null) {
                                        if (listRelyingParty.size() > 0) {
                                            if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                for (String rpRelying : listRelyingParty) {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("NAME", EscapeUtils.CheckTextNull(rpRelying));
                                                    listJson.add(json);
                                                }
                                            } else {
                                                String sChannelList = "";
                                                String pBranchID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                                BRANCH[][] rsBranch = new BRANCH[1][];
                                                db.S_BO_BRANCH_DETAIL(pBranchID, rsBranch);
                                                if(rsBranch != null && rsBranch[0].length > 0) {
                                                    sChannelList = rsBranch[0][0].RP_ACCESSED_FOR_ESIGNCLOUD;
                                                }
                                                if(!"".equals(sChannelList)) {
                                                    String[] sChannelSplit = sChannelList.split(",");
                                                    listRelyingParty.forEach((rpRelying) -> {
                                                        for (String sItem : sChannelSplit) {
                                                            if(EscapeUtils.CheckTextNull(rpRelying).equals(sItem)) {
                                                                JSONObject json = new JSONObject();
                                                                json.put("Code", "0");
                                                                json.put("NAME", EscapeUtils.CheckTextNull(rpRelying));
                                                                listJson.add(json);
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    for (String rpRelying : listRelyingParty) {
                                                        JSONObject json = new JSONObject();
                                                        json.put("Code", "0");
                                                        json.put("NAME", EscapeUtils.CheckTextNull(rpRelying));
                                                        listJson.add(json);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    CommonFunction.LogDebugString(log, "RSSPProcessCommon-getRelyingPartiesRSSP", Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM);
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadrsspauthenmethodlist": {
                                //<editor-fold defaultstate="collapsed" desc="loadrsspauthenmethodlist">
                                RSSPProcessCommon rssp = new RSSPProcessCommon();
                                PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                String sFormFactorPro = "";
                                if(rsFormfactorPro[0].length > 0) {
                                    sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                }
                                CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                if(credentialAuthen != null) {
                                    List<String> listAuthModes = rssp.getAuthModesRSSP(credentialAuthen);
                                    if (listAuthModes != null) {
                                        if (listAuthModes.size() > 0) {
                                            for (String rpAuthen : listAuthModes) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("NAME", EscapeUtils.CheckTextNull(rpAuthen));
                                                listJson.add(json);
                                            }
                                        }
                                    }
                                } else {
                                    CommonFunction.LogDebugString(log, "RSSPProcessCommon-getAuthModesRSSP", Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM);
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadrsspsigningprofileslist": {
                                //<editor-fold defaultstate="collapsed" desc="loadrsspsigningprofileslist">
                                RSSPProcessCommon rssp = new RSSPProcessCommon();
                                PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                String sFormFactorPro = "";
                                if(rsFormfactorPro[0].length > 0) {
                                    sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                }
                                CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                if(credentialAuthen != null) {
                                    List<String> listAuthModes = rssp.getSigningProfilesRSSP(credentialAuthen);
                                    if (listAuthModes != null) {
                                        if (listAuthModes.size() > 0) {
                                            for (String rpAuthen : listAuthModes) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("NAME", EscapeUtils.CheckTextNull(rpAuthen));
                                                listJson.add(json);
                                            }
                                        }
                                    }
                                } else {
                                    CommonFunction.LogDebugString(log, "RSSPProcessCommon-getSigningProfilesRSSP", Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM);
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadcertificedetail": {
                                //<editor-fold defaultstate="collapsed" desc="loadcertificedetail">
                                String Tag_ID = EscapeUtils.CheckTextNull(request.getParameter("Tag_ID"));
                                if (!"".equals(Tag_ID)) {
                                    CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(Tag_ID, pSessLanguage, rsCert);
                                    if(rsCert[0].length > 0)
                                    {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "0");
                                        String sMST_MNS = rsCert[0][0].ENTERPRISE_ID;
//                                        if("".equals(sMST_MNS)) {
//                                            sMST_MNS = EscapeUtils.CheckTextNull(rsCert[0][0].BUDGET_CODE);
//                                        }
                                        String sCMND = rsCert[0][0].PERSONAL_ID;
//                                        if("".equals(sCMND)) {
//                                            sCMND = EscapeUtils.CheckTextNull(rsCert[0][0].P_EID);
//                                        }
//                                        if("".equals(sCMND)) {
//                                            sCMND = EscapeUtils.CheckTextNull(rsCert[0][0].PASSPORT);
//                                        }
                                        json.put("MST_MNS", sMST_MNS);
                                        json.put("CMND", sCMND);
                                        json.put("COMPANY_NAME", EscapeUtils.CheckTextNull(rsCert[0][0].COMPANY_NAME));
                                        json.put("PERSONAL_NAME", EscapeUtils.CheckTextNull(rsCert[0][0].PERSONAL_NAME));
                                        json.put("EFFECTIVE_DT", EscapeUtils.CheckTextNull(rsCert[0][0].EFFECTIVE_DT));
                                        json.put("EXPIRATION_DT", EscapeUtils.CheckTextNull(rsCert[0][0].EXPIRATION_DT));
                                        json.put("CERTIFICATION_PURPOSE_DESC", EscapeUtils.CheckTextNull(rsCert[0][0].CERTIFICATION_PURPOSE_DESC));
                                        json.put("CERTIFICATION_SN", EscapeUtils.CheckTextNull(rsCert[0][0].CERTIFICATION_SN));
                                        json.put("CERTIFICATION_STATE_DESC", EscapeUtils.CheckTextNull(rsCert[0][0].CERTIFICATION_STATE_DESC));
                                        json.put("EMAIL_CONTRACT", EscapeUtils.CheckTextNull(rsCert[0][0].EMAIL_CONTRACT));
                                        json.put("PHONE_CONTRACT", EscapeUtils.CheckTextNull(rsCert[0][0].PHONE_CONTRACT));
                                        listJson.add(json);
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            
                            case "listnotassignprofile": {
                                //<editor-fold defaultstate="collapsed" desc="listnotassignprofile">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String TypeCertID = EscapeUtils.CheckTextNull(request.getParameter("TypeCertID"));
                                    CERTIFICATION_PROFILE[] temp = db.S_BO_UNASSIGN_CERTIFICATION_PROFILE_BY_PKI_FORMFACTOR(TypeCertID, pSessLanguage);
                                    if (temp.length > 0) {
                                        for (CERTIFICATION_PROFILE temp1 : temp) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("ID", String.valueOf(temp1.ID));
                                            json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                            json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "listyesassignprofile": {
                                //<editor-fold defaultstate="collapsed" desc="listyesassignprofile">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String TypeCertID = request.getParameter("TypeCertID");
                                    CERTIFICATION_PROFILE[] temp = db.S_BO_CERTIFICATION_PROFILE_BY_PKI_FORMFACTOR(TypeCertID, pSessLanguage);
                                    if (temp.length > 0) {
                                        int i = 1;
                                        for (CERTIFICATION_PROFILE temp1 : temp) {
                                            JSONObject json = new JSONObject();
                                            json.put("Code", "0");
                                            json.put("Index", i++);
                                            json.put("ID", temp1.ID);
                                            json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                            json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                            json.put("CREATED_DT", EscapeUtils.CheckTextNull(temp1.CREATED_DT));
                                            listJson.add(json);
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadserverlogonline": {
                                //<editor-fold defaultstate="collapsed" desc="loadserverlogonline">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                String fileName = EscapeUtils.CheckTextNull(request.getParameter("TypeLogView"));
                                ConfigLog cnf = new ConfigLog();
                                String pathLogTemp = cnf.GetPropertybyCode("log");
                                pathLogTemp = pathLogTemp.replace("$", "");
                                pathLogTemp = pathLogTemp.replace("{", "");
                                pathLogTemp = pathLogTemp.replace("}", "");
                                pathLogTemp = pathLogTemp.replace("/", "");
                                String pathLog = System.getProperty(pathLogTemp).trim() + "/";
                                if (!"".equals(fileName)) {
                                    String sLog = CommonFunction.GetMonitorServerLogString(200, fileName, pathLog);
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "0");
                                    json.put("ResLogView", sLog);
                                    listJson.add(json);
                                } else {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
//                                } else {
//                                    JSONObject json = new JSONObject();
//                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
//                                    listJson.add(json);
//                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadformfactor_mode": {
                                //<editor-fold defaultstate="collapsed" desc="loadformfactor_mode">
                                String idFactor = request.getParameter("idFactor");
                                PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                db.S_BO_PKI_FORMFACTOR_DETAIL(idFactor, rsFormfactorPro);
                                String sFormFactorPro = "";
                                if(rsFormfactorPro[0].length > 0) {
                                    sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                }
                                if(!"".equals(sFormFactorPro)) {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    FormFactorJsonProperties jsonGroup = objectMapper.readValue(sFormFactorPro, FormFactorJsonProperties.class);
                                    if(jsonGroup != null) {
                                        for (FormFactorJsonProperties.Attribute attribute : jsonGroup.getAttributes()) {
                                            if (attribute.getAttributeType().equals(Definitions.CONFIG_FORMFACTOR_ATTRIBUTE_TYPE_MODE)
                                                && attribute.getEnabled() == true) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("ID", EscapeUtils.CheckTextNull(attribute.getMode()));
                                                if("1".equals(pSessLanguage)){
                                                    json.put("REMARK", EscapeUtils.CheckTextNull(attribute.getRemark()));
                                                } else {
                                                    json.put("REMARK", EscapeUtils.CheckTextNull(attribute.getRemarkEn()));
                                                }
                                                listJson.add(json);
                                            }
                                        }
                                    }
                                }
                                if (listJson.size() <= 0) {
                                    JSONObject json = new JSONObject();
                                    json.put("Code", "1");
                                    listJson.add(json);
                                }
                                break;
                                //</editor-fold>
                            }
                            case "loadprofile_bypurpose": {
                                //<editor-fold defaultstate="collapsed" desc="loadprofile_bypurpose">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String idPurpose = request.getParameter("idPurpose");
                                    CERTIFICATION_PROFILE[][] temp = new CERTIFICATION_PROFILE[1][];
                                    db.S_BO_CERTIFICATION_PROFILE_BY_PURPOSE(idPurpose, pSessLanguage, temp);
                                    if (temp[0].length > 0) {
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                        {
                                            for (CERTIFICATION_PROFILE temp1 : temp[0]) {
                                                JSONObject json = new JSONObject();
                                                json.put("Code", "0");
                                                json.put("ID", String.valueOf(temp1.ID));
                                                json.put("NAME", EscapeUtils.CheckTextNull(temp1.NAME));
                                                json.put("REMARK", EscapeUtils.CheckTextNull(temp1.REMARK));
                                                listJson.add(json);
                                            }
                                        }
                                    }
                                    if (listJson.size() <= 0) {
                                        JSONObject json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
//                                } else {
//                                    JSONObject json = new JSONObject();
//                                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_CSRF);
//                                    listJson.add(json);
//                                }
                                break;
                                //</editor-fold>
                            }
                            case "checkownerforregistercert": {
                                //<editor-fold defaultstate="collapsed" desc="checkownerforregistercert">
                                String FromCreateDate = "";
                                String ToCreateDate = "";
                                String pOWNER_USER_RSSP = EscapeUtils.CheckTextNull(request.getParameter("pOWNER_USER_RSSP"));
                                String CERTIFICATION_OWNER_STATE = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_STATE_ID_OPERATED);
                                Config conf = new Config();
                                String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                JSONObject json;
                                String sError = "0";
                                if("1".equals(sRSSP_ACCESS_ENABLED))
                                {
                                    String[] sResult = new String[2];
                                    RSSPProcessCommon rssp = new RSSPProcessCommon();
                                    PKI_FORMFACTOR[][] rsFormfactorPro = new PKI_FORMFACTOR[1][];
                                    db.S_BO_PKI_FORMFACTOR_DETAIL(String.valueOf(Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD), rsFormfactorPro);
                                    String sFormFactorPro = "";
                                    if(rsFormfactorPro[0].length > 0) {
                                        sFormFactorPro = rsFormfactorPro[0][0].PROPERTIES;
                                    }
                                    CredentialDataAuthen credentialAuthen = CommonFunction.loadCredentialDataAuthen(sFormFactorPro);
                                    if(credentialAuthen != null) {
                                        List<OwnerInfo> listOwner = rssp.getOwnerInfoForSignCloud(pOWNER_USER_RSSP, "", "",
                                            "", "", "", "", sResult, "", credentialAuthen);
                                        if("0".equals(sResult[0])) {
                                            if(listOwner.size() > 0) {
                                                int sID = 1;
                                                for (OwnerInfo listOwner1 : listOwner) {
                                                    json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("ID", sID);
                                                    json.put("OWNER_USERNAME", EscapeUtils.CheckTextNull(listOwner1.getUsername()));
                                                    listJson.add(json);
                                                    sID++;
                                                }
                                            }
                                        } else {
                                            sError = sResult[0] + " - " + sResult[1];
                                        }
                                    } else {
                                        CommonFunction.LogDebugString(log, "RSSPProcessCommon-getOwnerInfoForSignCloud", Definitions.CONFIG_EXCEPTION_STRING_INVALID_EXTERNAL_SYSTEM);
                                    }
                                }
                                if("0".equals(sError)) {
                                    if (listJson.size() <= 0) {
                                        json = new JSONObject();
                                        json.put("Code", "1");
                                        listJson.add(json);
                                    }
                                } else {
                                    if (listJson.size() <= 0) {
                                        json = new JSONObject();
                                        json.put("Code", "2");
                                        json.put("Message", "Error: " + sError);
                                        listJson.add(json);
                                    }
                                }
                                break;
                                //</editor-fold>
                            }
                        }
                    }
                } else if (sOutInner == 2) {
                    JSONObject json = new JSONObject();
                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_LOGIN);
                    listJson.add(json);
                } else {
                    JSONObject json = new JSONObject();
                    json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN);
                    listJson.add(json);
                }
            } catch (ClassNotFoundException | SQLException | NumberFormatException e) {
                CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
                JSONObject json = new JSONObject();
                json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_ERROR);
                listJson.add(json);
            }
            out.println(listJson);
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
