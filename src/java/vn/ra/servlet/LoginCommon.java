package vn.ra.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.EscapeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.object.BACKOFFICE_USER;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATION_POLICY_ATTRIBUTE;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PREFIX_UUID;
import vn.ra.object.ROLE_DATA;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDbPhaseTwo;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author THANH
 */
public class LoginCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoginCommon.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     * @throws javax.mail.MessagingException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, MessagingException, Exception {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession sessionsa = request.getSession(false);
            ConnectDatabase db = new ConnectDatabase();
            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
            String strView = "";
            String idParam = request.getParameter("idParam");
            try {
                if (null != idParam) {
                    switch (idParam) {
                        case "loginpage": {
                            // <editor-fold defaultstate="collapsed" desc="case: loginpage methods.">
//                            String sessionid = request.getSession().getId();
//                            String contextPath = request.getContextPath();
//                            response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; Path=" + contextPath + "; HttpOnly;");
                            CommonFunction.LogDebugString(log, "BEFORE LOGIN_JSSESSION_ID", request.getSession().getId());
//                            String anticsrf = "" + Math.random();
//                            request.getSession().setAttribute("anticsrf", anticsrf);
                            String sUserID = request.getParameter("sUserName");
                            String sPassword = request.getParameter("sPwd");
                            String svn = EscapeUtils.CheckTextNull(request.getParameter("svn"));
                            String sCaptcha = EscapeUtils.CheckTextNull(request.getParameter("sCaptcha"));
                            String sCaptchaSRV = "";
                            if(request.getSession(false).getAttribute("sessCaptchaCode") != null) {
                                sCaptchaSRV = EscapeUtils.CheckTextNull(request.getSession(false).getAttribute("sessCaptchaCode").toString());
                                request.getSession(false).setAttribute("sessCaptchaCode", null);
                            }
                            Config conf = new Config();
                            String sIsCA = conf.GetTryPropertybyCode(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                            if("".equals(sIsCA)){
                                sIsCA = "0";
                            }
                            Boolean isCaptchaCheck = true;
                            if(!sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                if("".equals(sCaptcha) || !sCaptchaSRV.equals(sCaptcha))
                                {
                                    isCaptchaCheck = false;
                                }
                            }
                            if(isCaptchaCheck == false)
                            {
                                strView = "CAPTCHA#0";
                            } else
                            {
                                String sSessKey = CommonFunction.generateNumberDays();
                                String sIPLogin = CommonFunction.getClientIpLogin(request);
    //                            String sLogin = db.S_BO_LOGIN_USER(EscapeUtils.escapeHtml(sUserID),
    //                                    EscapeUtils.escapeHtml(sPassword), sSessKey, sIPLogin);
                                String sLogin = "3";
                                String sCheckBackEnd = conf.GetPropertybyCode(Definitions.CONFIG_IS_FRONTEND_BACKEND);
                                if("1".equals(sCheckBackEnd))
                                {
                                    sLogin = db.S_BO_BE_LOGIN_USER(EscapeUtils.escapeHtml(sUserID),
                                        EscapeUtils.escapeHtml(sPassword), sSessKey, sIPLogin);
                                } else if("0".equals(sCheckBackEnd))
                                {
                                    sLogin = db.S_BO_FE_LOGIN_USER(EscapeUtils.escapeHtml(sUserID),
                                        EscapeUtils.escapeHtml(sPassword), sSessKey, sIPLogin);
                                } else {
                                    sLogin = db.S_BO_LOGIN_USER(EscapeUtils.escapeHtml(sUserID),
                                        EscapeUtils.escapeHtml(sPassword), sSessKey, sIPLogin);
                                }
                                if ("0".equals(sLogin)) {
                                    BACKOFFICE_USER[][] rsdoLogin = new BACKOFFICE_USER[1][];
                                    db.S_BO_LOGIN_USER_LIST(EscapeUtils.escapeHtml(sUserID), EscapeUtils.escapeHtml(sPassword),
                                        svn, rsdoLogin);
                                    if (rsdoLogin[0].length > 0) {
                                        String isUIDCollection = conf.GetTryPropertybyCode(Definitions.CONFIG_IS_UID_COLLECTION_DISPLAY_ENABLED);
                                        // load param
                                        String sSourceNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_SOURCE, "");
                                        String sUrlNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_URL, "");
                                        String sUserIDNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_USERID, "");
                                        String sUserKeyNEAC = conf.GetPrintPropertybyCode(Definitions.CONFIG_SYNCH_NEAC_WS_USERKEY, "");
                                        String sSignProfileEnabled = conf.GetTryPropertybyCode(Definitions.CONFIG_SIGN_CERT_PROFILE_ENABLED);
                                        String sUIDChangeEnabled = conf.GetTryPropertybyCode(Definitions.CONFIG_UID_CHANGE_INFO_BO_ENABLED);
                                        String sRepresentEnabled = conf.GetTryPropertybyCode(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED);
                                        String profileManagerCAOption = conf.GetTryPropertybyCode(Definitions.CONFIG_PROFILE_MANAGER_LEVEL_APPROVE_OFCA);
                                        String fileAutoUpOld = conf.GetTryPropertybyCode(Definitions.CONFIG_FILE_FILE_UP_OLD_TO_NEW_AUTO_ENABLED);
                                        String sP12CreateEnabled = "1";
                                        String sUploadNewLabel = conf.GetTryPropertybyCode(Definitions.CONFIG_NEW_FILE_UPLOAD_HIGHLIGHTS_LABEL);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_SOURCE, sSourceNEAC);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_URL, sUrlNEAC);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_USERID, sUserIDNEAC);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SYNCH_NEAC_WS_USERKEY, sUserKeyNEAC);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_IS_WHICH_ABOUT_CA, sIsCA);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_SIGN_CERT_PROFILE_ENABLED, sSignProfileEnabled);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_IS_UID_COLLECTION_DISPLAY_ENABLED, isUIDCollection);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_NEW_FILE_UPLOAD_HIGHLIGHTS_LABEL, sUploadNewLabel);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_UID_CHANGE_INFO_BO_ENABLED, sUIDChangeEnabled);
                                        CommonFunction.LogDebugString(log, "Login sRepresentEnabled", sRepresentEnabled);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_REGISTER_REPRESENT_FORM_ENABLED, sRepresentEnabled);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_PROFILE_MANAGER_LEVEL_APPROVE_OFCA, profileManagerCAOption);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_FILE_FILE_UP_OLD_TO_NEW_AUTO_ENABLED, fileAutoUpOld);
                                        
                                        CommonFunction.LogDebugString(log, "SESSION-LOGIN-BY", "TIME: " + CommonFunction.GetDateFromLong(request.getSession(false).getLastAccessedTime())
                                            + "; USER: " + sUserID + "; SESSION-KEY: " + sSessKey + "; IP: " + sIPLogin);
                                        Boolean changeFlag = rsdoLogin[0][0].CHANGE_PASS_ENABLED;
                                        request.getSession(false).setAttribute("sUserID", rsdoLogin[0][0].USERNAME);
                                        request.getSession(false).setAttribute("pUserID", sPassword);
                                        request.getSession(false).setAttribute("UserID", String.valueOf(rsdoLogin[0][0].ID));
                                        request.getSession(false).setAttribute("sessLevelBranch", rsdoLogin[0][0].BRANCH_LEVEL_ID);
                                        request.getSession(false).setAttribute("SessAgentCode", EscapeUtils.CheckTextNull(rsdoLogin[0][0].BRANCH_NAME));
                                        request.getSession(false).setAttribute("sesSessKey", sSessKey);
                                        request.getSession(false).setAttribute("sessVN", svn.trim());
                                        GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
                                        db.S_BO_GENERAL_POLICY_LIST(svn.trim(), rsPolicy);
                                        request.getSession(false).setAttribute("sessGeneralPolicy_System", rsPolicy);
                                        // prefix uid
                                        PREFIX_UUID[][] rsPrefix;
                                        rsPrefix = new PREFIX_UUID[1][];
                                        dbTwo.S_BO_PREFIX_UUID_COMBOBOX("ENTERPRISE", svn.trim(), rsPrefix);
                                        request.getSession(false).setAttribute("sessPrefixUIDEnterprise", rsPrefix);
                                        ArrayList<PREFIX_UUID> tempListPrefix;
                                        tempListPrefix = new ArrayList<>();
                                        tempListPrefix.addAll(Arrays.asList(rsPrefix[0]));
                                        
                                        String sJsonPrefixDN = new Gson().toJson(tempListPrefix);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_KEY_PREFIX_ENTERPRISE_JSON, sJsonPrefixDN);
                                        /*String sListPrefixTest = LoadParamSystem.getParamStart(Definitions.CONFIG_KEY_PREFIX_ENTERPRISE_JSON);
                                        PREFIX_UUID[] videoArray = new Gson().fromJson(sListPrefixTest, PREFIX_UUID[].class);
                                        List<PREFIX_UUID> videoList = Arrays.asList(videoArray);
                                        for(int j=0;j<videoList.size();j++) {
                                            System.out.println(j+": " + videoList.get(j).PREFIX_DB);
                                        }*/
                                        
                                        rsPrefix = new PREFIX_UUID[1][];
                                        dbTwo.S_BO_PREFIX_UUID_COMBOBOX("PERSONAL", svn.trim(), rsPrefix);
                                        request.getSession(false).setAttribute("sessPrefixUIDPersonal", rsPrefix);
                                        tempListPrefix = new ArrayList<>();
                                        tempListPrefix.addAll(Arrays.asList(rsPrefix[0]));
                                        String sJsonPrefixCN = new Gson().toJson(rsPrefix[0]);
                                        LoadParamSystem.updateParamSystem(Definitions.CONFIG_KEY_PREFIX_PERSONAL_JSON, sJsonPrefixCN);
                                        // tree branch
                                        BRANCH[][] rsBranch;
                                        rsBranch = new BRANCH[1][];
                                        db.S_BO_BRANCH_GET_TREE_BRANCH(rsdoLogin[0][0].BRANCH_ID, Integer.parseInt(svn.trim()), rsBranch);
                                        if(rsBranch[0].length > 0) {
                                            request.getSession(false).setAttribute("sessTreeBranchSystem", rsBranch);
                                            String sTreeBranchID = "";
                                            for(BRANCH item : rsBranch[0]) {
                                                sTreeBranchID = sTreeBranchID + item.ID + ",";
                                            }
                                            sTreeBranchID = sTreeBranchID.substring(0, sTreeBranchID.lastIndexOf(","));
                                            request.getSession(false).setAttribute("sessTreeArrayBranchIDSystem", sTreeBranchID);
                                        }
                                        rsBranch = new BRANCH[1][];
                                        db.S_BO_BRANCH_GET_TREE_BRANCH_ROOT(rsdoLogin[0][0].BRANCH_ID, Integer.parseInt(svn.trim()), rsBranch);
                                        if(rsBranch[0].length > 0) {
                                            request.getSession(false).setAttribute("sessTreeBranchSystemRoot", rsBranch);
                                        }
                                        rsBranch = new BRANCH[1][];
                                        db.S_BO_BRANCH_GET_TREE_BRANCH_AGENCY(rsdoLogin[0][0].BRANCH_ID, Integer.parseInt(svn.trim()),
                                            rsBranch, String.valueOf(rsdoLogin[0][0].BRANCH_LEVEL_ID));
                                        if(rsBranch[0].length > 0) {
                                            request.getSession(false).setAttribute("sessTreeBranchSystemAgency", rsBranch);
                                        }
                                        if (changeFlag == true) {
                                            request.getSession(false).setAttribute("RoleID_ID", String.valueOf(rsdoLogin[0][0].ROLE_ID));
                                            request.getSession(false).setAttribute("RoleID", rsdoLogin[0][0].ROLE_NAME);
                                            request.getSession(false).setAttribute("RoleDesc", rsdoLogin[0][0].ROLE_REMARK);
                                            request.getSession(false).setAttribute("sesFullname", rsdoLogin[0][0].FULL_NAME);
                                            request.getSession(false).setAttribute("SessUserAgentID", String.valueOf(rsdoLogin[0][0].BRANCH_ID));
                                            request.getSession(false).setAttribute("SessAgentID", String.valueOf(rsdoLogin[0][0].BRANCH_PARENT_ID));
                                            request.getSession(false).setAttribute("SessAgentName", EscapeUtils.CheckTextNull(rsdoLogin[0][0].BRANCH_REMARK));
    //                                        sessionsa.setAttribute("SessRoleSet", EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES));
                                            // sess role token
                                            if(!"".equals(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES))) {
                                                ROLE_DATA[][] rsToken = new ROLE_DATA[1][];
                                                CommonFunction.LoadRoleToken(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES), rsToken);
                                                request.getSession(false).setAttribute("SessRoleSet_Token", rsToken);
                                                // sess role cert
                                                ROLE_DATA[][] rsCert = new ROLE_DATA[1][];
                                                CommonFunction.LoadRoleCertificate(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES), rsCert);
                                                request.getSession(false).setAttribute("SessRoleSet_Cert", rsCert);
                                                // sess role token
                                                ROLE_DATA[][] rsAnother = new ROLE_DATA[1][];
                                                CommonFunction.LoadRoleAnother(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES), rsAnother);
                                                request.getSession(false).setAttribute("SessRoleSet_Another", rsAnother);
                                            } else {
                                                request.getSession(false).setAttribute("SessRoleSet_Token", null);
                                                request.getSession(false).setAttribute("SessRoleSet_Cert", null);
                                                request.getSession(false).setAttribute("SessRoleSet_Another", null);
                                            }
                                            String sLogoBranch = "";
                                            String sNameBranch = "";
                                            String sCERT_POLICY_PROPERTIES;
                                            String sCERT_PROFILE_PROPERTIES;
                                            rsBranch = new BRANCH[1][];
                                            db.S_BO_BRANCH_DETAIL(String.valueOf(rsdoLogin[0][0].BRANCH_ID), rsBranch);
                                            if (rsBranch[0].length > 0) {
                                                sP12CreateEnabled = rsBranch[0][0].ISSUE_P12_ENABLED;
                                                sLogoBranch = EscapeUtils.CheckTextNull(rsBranch[0][0].LOGO);
                                                sNameBranch = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK);
                                                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                                                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                                                if ("0".equals(svn)) {
                                                    sNameBranch = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK_EN);
                                                }
                                                //<editor-fold defaultstate="collapsed" desc="PROFILE, FACTOR ACCESS BRANCH">
                                                if(!"".equals(sCERT_PROFILE_PROPERTIES) && !"".equals(sCERT_POLICY_PROPERTIES)) {
                                                    CERTIFICATION_POLICY_DATA[][] resPolicyCertData_Old = new CERTIFICATION_POLICY_DATA[1][];
                                                    CERTIFICATION_POLICY_DATA[][] resPolicyCertData_New = new CERTIFICATION_POLICY_DATA[1][];
                                                    ArrayList<CERTIFICATION_POLICY_DATA> tempProfileList = new ArrayList<>();
                                                    CommonFunction.getProfileCertList(sCERT_PROFILE_PROPERTIES, resPolicyCertData_Old);
    //                                                CommonFunction.getProfileCertList(sCERT_POLICY_PROPERTIES, resPolicyCertData_Old);
                                                    for(CERTIFICATION_POLICY_DATA resPolicyCertData_Old1 : resPolicyCertData_Old[0])
                                                    {
                                                        if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                                        {
                                                            CERTIFICATION_PROFILE[][] resProfileDB = new CERTIFICATION_PROFILE[1][];
                                                            db.S_BO_API_CERTIFICATION_PROFILE_GET_INFO(EscapeUtils.CheckTextNull(resPolicyCertData_Old1.name), resProfileDB);
                                                            if(resProfileDB[0].length > 0)
                                                            {
                                                                CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                                                itemProfileAccess.name = resProfileDB[0][0].NAME;
                                                                itemProfileAccess.certificateAuthority = resProfileDB[0][0].CERTIFICATION_AUTHORITY_NAME;
                                                                itemProfileAccess.certificatePurpose = resProfileDB[0][0].CERTIFICATION_PURPOSE_NAME;
                                                                itemProfileAccess.remark = resPolicyCertData_Old1.remark;
                                                                itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
                                                                itemProfileAccess.approveCAEnabled = resPolicyCertData_Old1.approveCAEnabled;
                                                                itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST;
                                                                tempProfileList.add(itemProfileAccess);
                                                            }
                                                        } else if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE))
                                                        {
                                                            CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                                            itemProfileAccess.name = resPolicyCertData_Old1.name;
                                                            itemProfileAccess.certificateAuthority = "";
                                                            itemProfileAccess.certificatePurpose = "";
                                                            itemProfileAccess.remark = resPolicyCertData_Old1.remark;
                                                            itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
                                                            itemProfileAccess.approveCAEnabled = resPolicyCertData_Old1.approveCAEnabled;
                                                            itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE;
                                                            tempProfileList.add(itemProfileAccess);
                                                        } else if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_MAJOR_TYPE))
                                                        {
                                                            CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                                            itemProfileAccess.name = resPolicyCertData_Old1.name;
                                                            itemProfileAccess.certificateAuthority = "";
                                                            itemProfileAccess.certificatePurpose = "";
                                                            itemProfileAccess.remark = resPolicyCertData_Old1.remark;
                                                            itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
                                                            itemProfileAccess.approveCAEnabled = resPolicyCertData_Old1.approveCAEnabled;
                                                            itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_MAJOR_TYPE;
                                                            tempProfileList.add(itemProfileAccess);
                                                        } else if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS))
                                                        {
                                                            CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                                            itemProfileAccess.name = resPolicyCertData_Old1.name;
                                                            itemProfileAccess.certificateAuthority = "";
                                                            itemProfileAccess.certificatePurpose = "";
                                                            itemProfileAccess.remark = resPolicyCertData_Old1.remark;
                                                            itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
                                                            itemProfileAccess.approveCAEnabled = resPolicyCertData_Old1.approveCAEnabled;
                                                            itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS;
                                                            tempProfileList.add(itemProfileAccess);
                                                        }
    //                                                    else if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE))
    //                                                    {
    //                                                        CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
    //                                                        itemProfileAccess.name = resPolicyCertData_Old1.name;
    //                                                        itemProfileAccess.certificateAuthority = "";
    //                                                        itemProfileAccess.certificatePurpose = "";
    //                                                        itemProfileAccess.remark = resPolicyCertData_Old1.remark;
    //                                                        itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
    //                                                        itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE;
    //                                                        tempProfileList.add(itemProfileAccess);
    //                                                    }
                                                    }
                                                    ObjectMapper objectMapper = new ObjectMapper();
                                                    CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sCERT_POLICY_PROPERTIES, CERTIFICATION_POLICY_ATTRIBUTE.class);
                                                    for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
                                                        if (attribute.getAttributeType().trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE)) {
                                                            if(attribute.isEnabled() == true) {
                                                                CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                                                itemProfileAccess.name = EscapeUtils.CheckTextNull(attribute.getName());
                                                                itemProfileAccess.certificateAuthority = "";
                                                                itemProfileAccess.certificatePurpose = "";
                                                                itemProfileAccess.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
                                                                itemProfileAccess.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                                                                itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE;
                                                                tempProfileList.add(itemProfileAccess);
                                                            }
                                                        }
                                                    }

                                                    resPolicyCertData_New[0] = new CERTIFICATION_POLICY_DATA[tempProfileList.size()];
                                                    resPolicyCertData_New[0] = tempProfileList.toArray(resPolicyCertData_New[0]);
                                                    request.getSession(false).setAttribute("SessPolicyCert_Data", resPolicyCertData_New);

                                                    CERTIFICATION_POLICY_DATA[][] resPolicyFormFactortData = new CERTIFICATION_POLICY_DATA[1][];
                                                    CommonFunction.getPKIFormFactorCertList(sCERT_POLICY_PROPERTIES, resPolicyFormFactortData);
                                                    request.getSession(false).setAttribute("SessPolicyFormFactor_Data", resPolicyFormFactortData);
                                                }
                                                //</editor-fold>
                                            }
                                            LoadParamSystem.updateParamSystem(Definitions.CONFIG_CREATE_P12_USER_AGENCY_ENABLED, sP12CreateEnabled);
                                            request.getSession(false).setAttribute("sessLogoBranch", sLogoBranch);
                                            request.getSession(false).setAttribute("sessNameBranch", sNameBranch);

                                            String strTimeOut = Definitions.CONFIG_SYSTEM_TIMEOUT;
                                            int sTimeOut = 60;
                                            int sACTIVATION_MAX_COUNTER = 5;
                                            if (rsPolicy[0].length > 0) {
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
                                            strView = "0#0#"+sSessKey;
                                            if(sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                if(!String.valueOf(rsdoLogin[0][0].BRANCH_PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                    strView = "0#2#"+sSessKey;
                                                }
                                            }
                                        } else {
                                            strView = "0#1#"+sSessKey;
                                        }
                                        CommonFunction.regenerateSession(request);
                                        CommonFunction.LogDebugString(log, "AFTER LOGIN_JSSESSION_ID", request.getSession().getId());
                                    } else {
                                        strView = "3#3";
                                    }
                                } else {
                                    strView = sLogin + "#" + sLogin;
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "loginssl": {
                            // <editor-fold defaultstate="collapsed" desc="case: loginssl">
                            String sessionid = request.getSession().getId();
                            String contextPath = request.getContextPath();
                            response.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; Path=" + contextPath + "; HttpOnly;");
                            String anticsrf = "" + Math.random();
                            request.getSession().setAttribute("anticsrf", anticsrf);
                            String sUserID = request.getSession(false).getAttribute("sessHashSSLToken").toString().trim();
                            request.getSession(false).setAttribute("sessHashSSLToken", null);
                            request.getSession(false).setAttribute("sessCertSSLToken", null);
                            String svn = EscapeUtils.CheckTextNull(request.getParameter("svn"));
                            String sSessKey = CommonFunction.generateNumberDays();
                            // IP
                            String sIPLogin = CommonFunction.getClientIpLogin(request);
                            String sLogin = db.S_BO_LOGIN_USER_SSL(EscapeUtils.escapeHtml(sUserID), sSessKey, sIPLogin);
                            if ("0".equals(sLogin)) {
                                BACKOFFICE_USER[][] rsdoLogin = new BACKOFFICE_USER[1][];
                                db.S_BO_LOGIN_USER_SSL_LIST(EscapeUtils.escapeHtml(sUserID), svn, rsdoLogin);
                                if (rsdoLogin[0].length > 0) {
                                    CommonFunction.LogDebugString(log, "LOGIN-S1", "IP: " + sIPLogin + "; USER (HASH): " + sUserID + "; TIME: " + CommonFunction.generateNumberDays());
//                                    Boolean changeFlag = rsdoLogin[0][0].CHANGE_PASS_ENABLED;
                                    request.getSession(false).setAttribute("sUserID", rsdoLogin[0][0].USERNAME);
//                                    request.getSession(false).setAttribute("pUserID", sPassword);
                                    request.getSession(false).setAttribute("UserID", String.valueOf(rsdoLogin[0][0].ID));
                                    request.getSession(false).setAttribute("sesSessKey", sSessKey);
                                    request.getSession(false).setAttribute("sessVN", svn.trim());
//                                    if (changeFlag == true) {
                                    request.getSession(false).setAttribute("RoleID_ID", String.valueOf(rsdoLogin[0][0].ROLE_ID));
                                    request.getSession(false).setAttribute("RoleID", rsdoLogin[0][0].ROLE_NAME);
                                    request.getSession(false).setAttribute("RoleDesc", rsdoLogin[0][0].ROLE_REMARK);
                                    request.getSession(false).setAttribute("sesFullname", rsdoLogin[0][0].FULL_NAME);
                                    request.getSession(false).setAttribute("SessUserAgentID", String.valueOf(rsdoLogin[0][0].BRANCH_ID));
                                    request.getSession(false).setAttribute("SessAgentID", String.valueOf(rsdoLogin[0][0].BRANCH_PARENT_ID));
                                    request.getSession(false).setAttribute("SessAgentName", EscapeUtils.CheckTextNull(rsdoLogin[0][0].BRANCH_REMARK));
                                    // sess role token
                                    if(!"".equals(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES))){
                                        ROLE_DATA[][] rsToken = new ROLE_DATA[1][];
                                        CommonFunction.LoadRoleToken(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES), rsToken);
                                        request.getSession(false).setAttribute("SessRoleSet_Token", rsToken);
                                        // sess role cert
                                        ROLE_DATA[][] rsCert = new ROLE_DATA[1][];
                                        CommonFunction.LoadRoleCertificate(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES), rsCert);
                                        request.getSession(false).setAttribute("SessRoleSet_Cert", rsCert);
                                        // sess role token
                                        ROLE_DATA[][] rsAnother = new ROLE_DATA[1][];
                                        CommonFunction.LoadRoleAnother(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES), rsAnother);
                                        request.getSession(false).setAttribute("SessRoleSet_Another", rsAnother);
                                    } else {
                                        request.getSession(false).setAttribute("SessRoleSet_Token", null);
                                        request.getSession(false).setAttribute("SessRoleSet_Cert", null);
                                        request.getSession(false).setAttribute("SessRoleSet_Another", null);
                                    }
                                    String sLogoBranch = "";
                                    String sNameBranch = "";
                                    BRANCH[][] rsBranch = new BRANCH[1][];
                                    db.S_BO_BRANCH_DETAIL(String.valueOf(rsdoLogin[0][0].BRANCH_ID), rsBranch);
                                    if (rsBranch[0].length > 0) {
                                        sLogoBranch = EscapeUtils.CheckTextNull(rsBranch[0][0].LOGO);
                                        sNameBranch = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK);
                                        if ("0".equals(svn)) {
                                            sNameBranch = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK_EN);
                                        }
                                    }
                                    request.getSession(false).setAttribute("sessLogoBranch", sLogoBranch);
                                    request.getSession(false).setAttribute("sessNameBranch", sNameBranch);
                                    GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
                                    db.S_BO_GENERAL_POLICY_LIST(svn.trim(), rsPolicy);
                                    String strTimeOut = Definitions.CONFIG_SYSTEM_TIMEOUT;
                                    int sTimeOut = 60;
                                    int sACTIVATION_MAX_COUNTER = 5;
                                    if (rsPolicy[0].length > 0) {
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
//                                    CommonFunction.LogDebugString(log, "TimeOut", String.valueOf(sTimeOut * 60));
//                                    CommonFunction.LogDebugString(log, "SessGlobalTimeOut", strTimeOut);
                                    request.getSession(false).setAttribute("SessGlobalTimeOut", strTimeOut);
                                    request.getSession(false).setAttribute("SessGlobalActivation_Max_Code", sACTIVATION_MAX_COUNTER);
                                    CommonFunction.regenerateSession(request);
                                    strView = "0#0";
//                                    } else {
//                                        strView = "0#1";
//                                    }
                                } else {
                                    strView = "3#3";
                                }
                            } else {
                                strView = sLogin + "#" + sLogin;
                            }

                            break;
                            //</editor-fold>
                        }
                        case "changepassfirst": {
                            // <editor-fold defaultstate="collapsed" desc="case: changepassfirst methods.">
                            if (request.getSession(false).getAttribute("sUserID") != null) {
                                String anticsrf = request.getParameter("CsrfToken");
                                String sPwdOld = EscapeUtils.CheckTextNull(request.getParameter("sPwdOld"));
                                String sPasswordNew = EscapeUtils.CheckTextNull(request.getParameter("sPwdNew"));
                                String nameUser = EscapeUtils.CheckTextNull(request.getParameter("nameUser"));
                                String strUserID = (String) request.getSession(false).getAttribute("UserID");
                                String svn = EscapeUtils.CheckTextNull(request.getParameter("svn"));
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    if(nameUser.trim().equals(sessionsa.getAttribute("sUserID").toString().trim())) {
                                        if(!"".equals(sPwdOld) && !"".equals(sPasswordNew))
                                        {
                                            if(sPasswordNew.contains(" "))
                                            {
                                                strView = "PASS_SPACE#0";
                                            } else {
                                                String strMin = "0";
                                                String strMax = "0";
                                                Boolean strNumericPass = false;
                                                Boolean strAlphaPass = false;
                                                Boolean strSpecialPass = false;
                                                Boolean strUpcaseAlphanumeric = false;
                                                GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
                                                db.S_BO_GENERAL_POLICY_LIST(svn.trim(), rsPolicy);
                                                request.getSession(false).setAttribute("sessGeneralPolicy_System", rsPolicy);
                                                if (rsPolicy[0].length > 0) {
                                                    for(GENERAL_POLICY rsPolicy1: rsPolicy[0])
                                                    {
                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_MIN_LENGTH_PASSWORD))
                                                        {
                                                            strMin = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                        }
                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_MAX_LENGTH_PASSWORD))
                                                        {
                                                            strMax = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                        }
                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_NUMERIC_PASSWORD))
                                                        {
                                                            strNumericPass = "1".equals(rsPolicy1.VALUE);
                                                        }
                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ALPHA_PASSWORD))
                                                        {
                                                            strAlphaPass = "1".equals(rsPolicy1.VALUE);
                                                        }
                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SPECIAL_PASSWORD))
                                                        {
                                                            strSpecialPass = "1".equals(rsPolicy1.VALUE);
                                                        }
                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_UPERCASE_PASSWORD))
                                                        {
                                                            strUpcaseAlphanumeric = "1".equals(rsPolicy1.VALUE);
                                                        }
                                                    }
                                                }
                                                String sSuccess = "0";
                                                if(sPasswordNew.length() < Integer.parseInt(strMin) || sPasswordNew.length() > Integer.parseInt(strMax)) {
                                                    sSuccess = "PASS_LENGTH";
                                                } else {
                                                    if(sPwdOld.equals(sPasswordNew)) {
                                                        sSuccess = "PASS_SAME";
                                                    } else {
                                                        if(strNumericPass == true) {
                                                            if(!CommonFunction.stringContainsNumber(sPasswordNew)) {
                                                                sSuccess = "PASS_NUMBER";
                                                            }
                                                        }
                                                        if("0".equals(sSuccess)) {
                                                            if(strAlphaPass == true) {
                                                                if(!CommonFunction.stringContainsAlphaNumeric(sPasswordNew)) {
                                                                    sSuccess = "PASS_ALPHA";
                                                                }
                                                            }
                                                        }
                                                        if("0".equals(sSuccess)) {
                                                            if(strSpecialPass == true) {
                                                                if(!CommonFunction.stringContainsSpecial(sPasswordNew)) {
                                                                    sSuccess = "PASS_SPECIAL";
                                                                }
                                                            }
                                                        }
                                                        if("0".equals(sSuccess)) {
                                                            if(strUpcaseAlphanumeric == true) {
                                                                if(!CommonFunction.stringContainsAlphaCapital(sPasswordNew)) {
                                                                    sSuccess = "PASS_CAPITAL";
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if("0".equals(sSuccess)) {
                                                    String param1 = db.S_BO_USER_RESET_PASSWORD(EscapeUtils.escapeHtml(nameUser),
                                                            strUserID, EscapeUtils.escapeHtml(sPwdOld),
                                                            EscapeUtils.escapeHtml(sPasswordNew));
                                                    if ("0".equals(param1)) {
                                                        BACKOFFICE_USER[][] rsdoLogin = new BACKOFFICE_USER[1][];
                                                        db.S_BO_LOGIN_USER_LIST(EscapeUtils.escapeHtml(nameUser),
                                                                EscapeUtils.escapeHtml(sPasswordNew), svn, rsdoLogin);
                                                        if (rsdoLogin[0].length > 0) {
                                                            request.getSession(false).setAttribute("sUserID", rsdoLogin[0][0].USERNAME);
                                                            request.getSession(false).setAttribute("pUserID", sPasswordNew);
                                                            request.getSession(false).setAttribute("UserID", String.valueOf(rsdoLogin[0][0].ID));
                                                            request.getSession(false).setAttribute("RoleID_ID", String.valueOf(rsdoLogin[0][0].ROLE_ID));
                                                            request.getSession(false).setAttribute("RoleID", rsdoLogin[0][0].ROLE_NAME);
                                                            request.getSession(false).setAttribute("RoleDesc", rsdoLogin[0][0].ROLE_REMARK);
                                                            request.getSession(false).setAttribute("sesFullname", rsdoLogin[0][0].FULL_NAME);
                                                            request.getSession(false).setAttribute("SessUserAgentID", String.valueOf(rsdoLogin[0][0].BRANCH_ID));
                                                            request.getSession(false).setAttribute("SessAgentID", String.valueOf(rsdoLogin[0][0].BRANCH_PARENT_ID));
                //                                            sessionsa.setAttribute("SessAgentID", String.valueOf(rsdoLogin[0][0].BRANCH_ID));
                                                            request.getSession(false).setAttribute("SessAgentName", EscapeUtils.CheckTextNull(rsdoLogin[0][0].BRANCH_REMARK));
                //                                            sessionsa.setAttribute("SessRoleSet", EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES));
                                                            // sess role token
                                                            if(!"".equals(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES))){
                                                                ROLE_DATA[][] rsToken = new ROLE_DATA[1][];
                                                                CommonFunction.LoadRoleToken(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES), rsToken);
                                                                request.getSession(false).setAttribute("SessRoleSet_Token", rsToken);
                                                                // sess role cert
                                                                ROLE_DATA[][] rsCert = new ROLE_DATA[1][];
                                                                CommonFunction.LoadRoleCertificate(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES), rsCert);
                                                                request.getSession(false).setAttribute("SessRoleSet_Cert", rsCert);
                                                                // sess role token
                                                                ROLE_DATA[][] rsAnother = new ROLE_DATA[1][];
                                                                CommonFunction.LoadRoleAnother(EscapeUtils.CheckTextNull(rsdoLogin[0][0].PROPERTIES), rsAnother);
                                                                request.getSession(false).setAttribute("SessRoleSet_Another", rsAnother);
                                                            } else {
                                                                request.getSession(false).setAttribute("SessRoleSet_Token", null);
                                                                request.getSession(false).setAttribute("SessRoleSet_Cert", null);
                                                                request.getSession(false).setAttribute("SessRoleSet_Another", null);
                                                            }
                                                            request.getSession(false).setAttribute("sessVN", svn.trim());
                                                            String sLogoBranch = "";
                                                            String sNameBranch = "";
                                                            String sCERT_PROFILE_PROPERTIES;
                                                            String sCERT_POLICY_PROPERTIES;
                                                            String sP12CreateEnabled = "";
                                                            BRANCH[][] rsBranch = new BRANCH[1][];
                                                            db.S_BO_BRANCH_DETAIL(String.valueOf(rsdoLogin[0][0].BRANCH_ID), rsBranch);
                                                            if (rsBranch[0].length > 0) {
                                                                sP12CreateEnabled = rsBranch[0][0].ISSUE_P12_ENABLED;
                                                                sLogoBranch = EscapeUtils.CheckTextNull(rsBranch[0][0].LOGO);
                                                                sNameBranch = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK);
                                                                if ("0".equals(svn)) {
                                                                    sNameBranch = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK_EN);
                                                                }
                                                                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                                                                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                                                                //<editor-fold defaultstate="collapsed" desc="PROFILE, FACTOR ACCESS BRANCH">
                                                                if(!"".equals(sCERT_POLICY_PROPERTIES) && !"".equals(sCERT_PROFILE_PROPERTIES)) {
                                                                    CERTIFICATION_POLICY_DATA[][] resPolicyCertData_Old = new CERTIFICATION_POLICY_DATA[1][];
                                                                    CERTIFICATION_POLICY_DATA[][] resPolicyCertData_New = new CERTIFICATION_POLICY_DATA[1][];
                                                                    ArrayList<CERTIFICATION_POLICY_DATA> tempProfileList = new ArrayList<>();
                                                                    CommonFunction.getProfileCertList(sCERT_PROFILE_PROPERTIES, resPolicyCertData_Old);
            //                                                        CommonFunction.getProfileCertList(sCERT_POLICY_PROPERTIES, resPolicyCertData_Old);
                    //                                                request.getSession(false).setAttribute("SessPolicyCert_Data", resPolicyCertData_Old);
                                                                    for(CERTIFICATION_POLICY_DATA resPolicyCertData_Old1 : resPolicyCertData_Old[0])
                                                                    {
                                                                        if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                                                        {
                                                                            CERTIFICATION_PROFILE[][] resProfileDB = new CERTIFICATION_PROFILE[1][];
                                                                            db.S_BO_API_CERTIFICATION_PROFILE_GET_INFO(EscapeUtils.CheckTextNull(resPolicyCertData_Old1.name), resProfileDB);
                                                                            if(resProfileDB[0].length > 0)
                                                                            {
                                                                                CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                                                                itemProfileAccess.name = resProfileDB[0][0].NAME;
                                                                                itemProfileAccess.certificateAuthority = resProfileDB[0][0].CERTIFICATION_AUTHORITY_NAME;
                                                                                itemProfileAccess.certificatePurpose = resProfileDB[0][0].CERTIFICATION_PURPOSE_NAME;
                                                                                itemProfileAccess.remark = resPolicyCertData_Old1.remark;
                                                                                itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
                                                                                itemProfileAccess.approveCAEnabled = resPolicyCertData_Old1.approveCAEnabled;
                                                                                itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST;
                                                                                tempProfileList.add(itemProfileAccess);
                                                                            }
                                                                        } else if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE))
                                                                        {
                                                                            CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                                                            itemProfileAccess.name = resPolicyCertData_Old1.name;
                                                                            itemProfileAccess.certificateAuthority = "";
                                                                            itemProfileAccess.certificatePurpose = "";
                                                                            itemProfileAccess.remark = resPolicyCertData_Old1.remark;
                                                                            itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
                                                                            itemProfileAccess.approveCAEnabled = resPolicyCertData_Old1.approveCAEnabled;
                                                                            itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE;
                                                                            tempProfileList.add(itemProfileAccess);
                                                                        } else if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS))
                                                                        {
                                                                            CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                                                            itemProfileAccess.name = resPolicyCertData_Old1.name;
                                                                            itemProfileAccess.certificateAuthority = "";
                                                                            itemProfileAccess.certificatePurpose = "";
                                                                            itemProfileAccess.remark = resPolicyCertData_Old1.remark;
                                                                            itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
                                                                            itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS;
                                                                            tempProfileList.add(itemProfileAccess);
                                                                        }
            //                                                            else if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE))
            //                                                            {
            //                                                                CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
            //                                                                itemProfileAccess.name = resPolicyCertData_Old1.name;
            //                                                                itemProfileAccess.certificateAuthority = "";
            //                                                                itemProfileAccess.certificatePurpose = "";
            //                                                                itemProfileAccess.remark = resPolicyCertData_Old1.remark;
            //                                                                itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
            //                                                                itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE;
            //                                                                tempProfileList.add(itemProfileAccess);
            //                                                            }
                                                                    }
                                                                    ObjectMapper objectMapper = new ObjectMapper();
                                                                    CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sCERT_POLICY_PROPERTIES, CERTIFICATION_POLICY_ATTRIBUTE.class);
                                                                    for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
                                                                        if (attribute.getAttributeType().trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE)) {
                                                                            if(attribute.isEnabled() == true) {
                                                                                CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                                                                itemProfileAccess.name = EscapeUtils.CheckTextNull(attribute.getName());
                                                                                itemProfileAccess.certificateAuthority = "";
                                                                                itemProfileAccess.certificatePurpose = "";
                                                                                itemProfileAccess.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
                                                                                itemProfileAccess.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                                                                                itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE;
                                                                                tempProfileList.add(itemProfileAccess);
                                                                            }
                                                                        }
                                                                    }
                                                                    resPolicyCertData_New[0] = new CERTIFICATION_POLICY_DATA[tempProfileList.size()];
                                                                    resPolicyCertData_New[0] = tempProfileList.toArray(resPolicyCertData_New[0]);
                                                                    request.getSession(false).setAttribute("SessPolicyCert_Data", resPolicyCertData_New);

                                                                    CERTIFICATION_POLICY_DATA[][] resPolicyFormFactortData = new CERTIFICATION_POLICY_DATA[1][];
                                                                    CommonFunction.getPKIFormFactorCertList(sCERT_POLICY_PROPERTIES, resPolicyFormFactortData);
                                                                    request.getSession(false).setAttribute("SessPolicyFormFactor_Data", resPolicyFormFactortData);
                                                                }
                                                                //</editor-fold>
                                                            }
                                                            LoadParamSystem.updateParamSystem(Definitions.CONFIG_CREATE_P12_USER_AGENCY_ENABLED, sP12CreateEnabled);
                                                            request.getSession(false).setAttribute("sessLogoBranch", sLogoBranch);
                                                            request.getSession(false).setAttribute("sessNameBranch", sNameBranch);
                                                            String strTimeOut = Definitions.CONFIG_SYSTEM_TIMEOUT;
                                                            int sTimeOut = 60;
                                                            int sACTIVATION_MAX_COUNTER = 5;
                                                            if (rsPolicy[0].length > 0) {
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
//                                                            CommonFunction.LogDebugString(log, "TimeOut", String.valueOf(sTimeOut * 60));
//                                                            CommonFunction.LogDebugString(log, "SessGlobalTimeOut", strTimeOut);
                                                            request.getSession(false).setAttribute("SessGlobalTimeOut", strTimeOut);
                                                            request.getSession(false).setAttribute("SessGlobalActivation_Max_Code", sACTIVATION_MAX_COUNTER);
                                                            strView = "0#0";
                                                            Config conf = new Config();
                                                            String sIsCA = conf.GetTryPropertybyCode(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                            if(sIsCA.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                                if(!String.valueOf(rsdoLogin[0][0].BRANCH_PARENT_ID).equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                    strView = "0#1";
                                                                }
                                                            }
                                                        } else {
                                                            strView = "1#1";
                                                        }
                                                    } else {
                                                        strView = param1 + "#" + param1;
                                                    }
                                                }  else {
                                                    strView = sSuccess + "#0";
                                                }
                                            }
                                        } else {
                                            strView = "PASS_EMPTY#0";
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "Frist Change Password: " + nameUser.trim() + " Invalid!");
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                            } else {
                                strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "beforeforgotpass": {
                            // <editor-fold defaultstate="collapsed" desc="beforeforgotpass">
                            String anticsrf = request.getParameter("CsrfToken");
                            String strUsername = EscapeUtils.CheckTextNull(request.getParameter("sUsername"));
                            String strEmail = "";
                            String strID = "";
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                BACKOFFICE_USER[][] rsUser = new BACKOFFICE_USER[1][];
                                db.S_BO_GET_USER_FORGOT_MAIL(EscapeUtils.escapeHtml(strUsername), rsUser);
                                if (rsUser[0].length > 0) {
                                    strEmail = EscapeUtils.CheckTextNull(rsUser[0][0].EMAIL);
                                    strID = String.valueOf(rsUser[0][0].ID);
                                }
                                if (!"".equals(strEmail)) {
                                    strView = "0#" + strID + "#" + strEmail;
                                } else {
                                    strView = "1#0";
                                }
                            } else {
                                strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "afterforgotpass": {
                            // <editor-fold defaultstate="collapsed" desc="afterforgotpass">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                String strPassConfig = request.getParameter("randomCode");
                                String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                String strUsername = EscapeUtils.CheckTextNull(request.getParameter("sUsername"));
                                String param1 = db.S_BO_USER_RESET_PASSWORD_DEFAULT(strUsername, strPassConfig.trim(), strUsername);
                                if ("0".equals(param1)) {
                                    if (!"".equals(id)) {
                                        int[] psResCode = new int[1];
                                        String[] sResMess = new String[1];
                                        ConnectConnector.SendMailForgotPass(id, strPassConfig, psResCode, sResMess);
                                        if (psResCode[0] != 0) {
                                            CommonFunction.LogErrorServlet(log, "SEND-EMAIL-RESET-USER: " + sResMess[0]);
                                            strView = "1#0";
                                        } else {
                                            strView = "0#0";
                                        }
                                    } else {
                                        strView = "1#0";
                                    }
                                } else {
                                    strView = param1 + "#" + param1;
                                }
                            } else {
                                strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                            }
                            break;
                            //</editor-fold>
                        }
                    }
                }
            } catch (SQLException | NumberFormatException e) {
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
        try {
            processRequest(request, response);
        } catch (SQLException | MessagingException ex) {
            CommonFunction.LogExceptionServlet(log, ex.getMessage(), ex);
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
        } catch (SQLException | MessagingException ex) {
            CommonFunction.LogExceptionServlet(log, ex.getMessage(), ex);
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
