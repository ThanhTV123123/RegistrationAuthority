package vn.ra.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.EscapeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.object.BACKOFFICE_USER;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.ROLE;
import vn.ra.object.ROLE_DATA;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectConnector;
import vn.ra.process.SessionRoleFunctions;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;

/**
 *
 * @author THANH
 */
public class UserCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UserCommon.class.getName());

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
                    String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                    String idParam = request.getParameter("idParam");
                    if (null != idParam) {
                        switch (idParam) {
                            case "edituser": {
                                // <editor-fold defaultstate="collapsed" desc="case: edituser methods.">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
//                                    String SessRoleID_ID = sessionsa.getAttribute("RoleID_ID").toString().trim();
                                    String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                    String idss = EscapeUtils.CheckTextNull(request.getParameter("id"));
//                                    String sAdmin1 = request.getParameter("sAdmin");
                                    String ActiveFlag = EscapeUtils.CheckTextNull(request.getParameter("ActiveFlag"));
                                    String GroupName = EscapeUtils.CheckTextNull(request.getParameter("GroupName"));
                                    BACKOFFICE_USER[][] rsUser = new BACKOFFICE_USER[1][];
                                    db.S_BO_USER_DETAIL(idss, sessLanguage, rsUser);
                                    {
                                        if(rsUser[0].length > 0) {
                                            if(SessLevelBranch.equals(rsUser[0][0].BRANCH_LEVEL_ID)) {
                                                if(String.valueOf(rsUser[0][0].ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                    ActiveFlag = "0";
                                                    if(rsUser[0][0].ENABLED == true) {
                                                        ActiveFlag = "1";
                                                    }
                                                    GroupName = String.valueOf(rsUser[0][0].ROLE_ID);
                                                }
                                            }
                                        }
                                    }
//                                    if (!"".equals(sAdmin1)) {
//                                        GroupName = request.getParameter("GroupName1");
//                                    } else {
//                                    }
                                    String BranchName = EscapeUtils.CheckTextNull(request.getParameter("BranchName"));
                                    String FullName = EscapeUtils.CheckTextNull(request.getParameter("FullName"));
                                    String USER_STATE = EscapeUtils.CheckTextNull(request.getParameter("USER_STATE"));
                                    String Email = EscapeUtils.CheckTextNull(request.getParameter("Email"));
                                    String MobileNumber = EscapeUtils.CheckTextNull(request.getParameter("MobileNumber"));
                                    String idSSL = EscapeUtils.CheckTextNull(request.getParameter("idSSL"));
                                    ROLE_DATA[][] rsToken = new ROLE_DATA[1][];
                                    ROLE_DATA[][] rsCert = new ROLE_DATA[1][];
                                    ROLE_DATA[][] rsAnother = new ROLE_DATA[1][];
                                    // ROLE FUNCTIONS TOKEN
                                    SessionRoleFunctions cartToken = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsToken");
                                    if (cartToken != null) {
                                        ArrayList<ROLE_DATA> tempListToken = new ArrayList<>();
                                        ArrayList<ROLE_DATA> ds = cartToken.getGH();
                                        for (ROLE_DATA mhIP : ds) {
                                            ROLE_DATA tempItem = new ROLE_DATA();
                                            tempItem.name = mhIP.name;
                                            tempItem.attributeType = mhIP.attributeType;
                                            tempItem.remarkEn = mhIP.remarkEn;
                                            tempItem.remark = mhIP.remark;
                                            tempItem.enabled = mhIP.enabled;
                                            tempListToken.add(tempItem);
                                        }
                                        rsToken[0] = new ROLE_DATA[tempListToken.size()];
                                        rsToken[0] = tempListToken.toArray(rsToken[0]);
                                    }
                                    // ROLE FUNCTIONS CERT
                                    SessionRoleFunctions cartCert = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsCert");
                                    if (cartCert != null) {
                                        ArrayList<ROLE_DATA> tempListCert = new ArrayList<>();
                                        ArrayList<ROLE_DATA> ds = cartCert.getGH();
                                        for (ROLE_DATA mhIP : ds) {
                                            ROLE_DATA tempItem = new ROLE_DATA();
                                            tempItem.name = mhIP.name;
                                            tempItem.attributeType = mhIP.attributeType;
                                            tempItem.remarkEn = mhIP.remarkEn;
                                            tempItem.remark = mhIP.remark;
                                            tempItem.enabled = mhIP.enabled;
                                            tempListCert.add(tempItem);
                                        }
                                        rsCert[0] = new ROLE_DATA[tempListCert.size()];
                                        rsCert[0] = tempListCert.toArray(rsCert[0]);
                                    }
                                    // ROLE FUNCTIONS ANOTHER
//                                    SessionRoleFunctions cartAnother = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsAnother");
//                                    if (cartAnother != null) {
//                                        ArrayList<ROLE_DATA> tempListAnother = new ArrayList<>();
//                                        ArrayList<ROLE_DATA> ds = cartAnother.getGH();
//                                        for (ROLE_DATA mhIP : ds) {
//                                            ROLE_DATA tempItem = new ROLE_DATA();
//                                            tempItem.name = mhIP.name;
//                                            tempItem.attributeType = mhIP.attributeType;
//                                            tempItem.remarkEn = mhIP.remarkEn;
//                                            tempItem.remark = mhIP.remark;
//                                            tempItem.enabled = mhIP.enabled;
//                                            tempListAnother.add(tempItem);
//                                        }
//                                        rsAnother[0] = new ROLE_DATA[tempListAnother.size()];
//                                        rsAnother[0] = tempListAnother.toArray(rsAnother[0]);
//                                    }
                                    String sRoleJson = CommonFunction.AddRootRoleCertificate(rsToken, rsCert, rsAnother);
                                    String sCertSSL = "";
                                    String sHashSSL = "";
                                    if(!"".equals(idSSL))
                                    {
                                        if(request.getSession(false).getAttribute("sessHashSSLToken") != null)
                                        {
                                            sCertSSL = request.getSession(false).getAttribute("sessCertSSLToken").toString().trim();
                                            sHashSSL = request.getSession(false).getAttribute("sessHashSSLToken").toString().trim();
                                        }
                                    }
                                    int sParamSSL = 0;
                                    String paramCert = db.S_BO_USER_UPDATE_CERTIFICATION(Integer.parseInt(EscapeUtils.escapeHtml(idss)), sCertSSL, sHashSSL, loginUID);
                                    CommonFunction.LogDebugString(log, "USER_UPDATE Step 4", paramCert);
                                    if(!"0".equals(paramCert))
                                    {
                                        sParamSSL = 1;
                                    } else {
                                        request.getSession(false).setAttribute("sessCertSSLToken", null);
                                        request.getSession(false).setAttribute("sessHashSSLToken", null);
                                    }
                                    if(sParamSSL == 0)
                                    {
                                        String param1 = db.S_BO_USER_UPDATE(Integer.parseInt(EscapeUtils.escapeHtml(idss)),
                                                Integer.parseInt(ActiveFlag), EscapeUtils.escapeHtml(FullName.trim()),
                                                EscapeUtils.escapeHtml(GroupName.trim()), BranchName.split("#")[0],
                                                EscapeUtils.escapeHtml(Email.trim()), EscapeUtils.escapeHtml(USER_STATE.trim()),
                                                EscapeUtils.escapeHtml(MobileNumber.trim()), sRoleJson, loginUID);
                                        CommonFunction.LogDebugString(log, "USER_UPDATE Step 5", param1);
                                        if ("0".equals(param1)) {
                                            strView = "0#0";
                                            request.getSession(false).setAttribute("RefreshUserSess", "1");
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    } else
                                    {
                                        strView = "4#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "adduser": {
                                // <editor-fold defaultstate="collapsed" desc="case: adduser methods.">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String Username = EscapeUtils.CheckTextNull(request.getParameter("Username"));
                                    String Password = EscapeUtils.CheckTextNull(request.getParameter("Password"));
                                    String FullName = request.getParameter("FullName");
                                    String UserRole = EscapeUtils.CheckTextNull(request.getParameter("UserRole"));
                                    String BranchName = EscapeUtils.CheckTextNull(request.getParameter("BranchName"));
                                    String Email = EscapeUtils.CheckTextNull(request.getParameter("Email"));
                                    String MobileNumber = EscapeUtils.CheckTextNull(request.getParameter("MobileNumber"));
                                    String idSSL = EscapeUtils.CheckTextNull(request.getParameter("idSSL"));
                                    if(!UserRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) && !UserRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                    {
                                        idSSL = "";
                                    }
                                    boolean boCheckSSL = true;
                                    if(!"".equals(idSSL))
                                    {
                                        if(request.getSession(false).getAttribute("sessHashSSLToken") != null)
                                        {
                                            String sHashSSLCheck = request.getSession(false).getAttribute("sessHashSSLToken").toString().trim();
                                            boolean boExistsSSL = db.S_BO_USER_CHECK_SSL(sHashSSLCheck);
                                            if (boExistsSSL == true) {
                                                boCheckSSL = false;
                                            }
                                        }
                                    }
                                    if(boCheckSSL == true) {
                                        String sRoleJson = "";
                                        if(BranchName.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            UserRole = Definitions.CONFIG_ROLE_ID_CA_ADMIN;
                                            ROLE[][] rsRole = new ROLE[1][];
                                            db.S_BO_ROLE_DETAIL(UserRole, rsRole);
                                            if(rsRole[0].length > 0) {
                                                sRoleJson = EscapeUtils.CheckTextNull(rsRole[0][0].PROPERTIES);
                                            }
                                        } else
                                        {
                                            ROLE_DATA[][] rsToken = new ROLE_DATA[1][];
                                            ROLE_DATA[][] rsCert = new ROLE_DATA[1][];
                                            ROLE_DATA[][] rsAnother = new ROLE_DATA[1][];
                                            // ROLE FUNCTIONS TOKEN
                                            SessionRoleFunctions cartToken = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsToken");
                                            if (cartToken != null) {
                                                ArrayList<ROLE_DATA> tempListToken = new ArrayList<>();
                                                ArrayList<ROLE_DATA> ds = cartToken.getGH();
                                                for (ROLE_DATA mhIP : ds) {
                                                    ROLE_DATA tempItem = new ROLE_DATA();
                                                    tempItem.name = mhIP.name;
                                                    tempItem.attributeType = mhIP.attributeType;
                                                    tempItem.remarkEn = mhIP.remarkEn;
                                                    tempItem.remark = mhIP.remark;
                                                    tempItem.enabled = mhIP.enabled;
                                                    tempListToken.add(tempItem);
                                                }
                                                rsToken[0] = new ROLE_DATA[tempListToken.size()];
                                                rsToken[0] = tempListToken.toArray(rsToken[0]);
                                            }
                                            // ROLE FUNCTIONS CERT
                                            SessionRoleFunctions cartCert = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsCert");
                                            if (cartCert != null) {
                                                ArrayList<ROLE_DATA> tempListCert = new ArrayList<>();
                                                ArrayList<ROLE_DATA> ds = cartCert.getGH();
                                                for (ROLE_DATA mhIP : ds) {
                                                    ROLE_DATA tempItem = new ROLE_DATA();
                                                    tempItem.name = mhIP.name;
                                                    tempItem.attributeType = mhIP.attributeType;
                                                    tempItem.remarkEn = mhIP.remarkEn;
                                                    tempItem.remark = mhIP.remark;
                                                    tempItem.enabled = mhIP.enabled;
                                                    tempListCert.add(tempItem);
                                                }
                                                rsCert[0] = new ROLE_DATA[tempListCert.size()];
                                                rsCert[0] = tempListCert.toArray(rsCert[0]);
                                            }
                                            // ROLE FUNCTIONS ANOTHER
                                            SessionRoleFunctions cartAnother = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsAnother");
                                            if (cartAnother != null) {
                                                ArrayList<ROLE_DATA> tempListAnother = new ArrayList<>();
                                                ArrayList<ROLE_DATA> ds = cartAnother.getGH();
                                                for (ROLE_DATA mhIP : ds) {
                                                    ROLE_DATA tempItem = new ROLE_DATA();
                                                    tempItem.name = mhIP.name;
                                                    tempItem.attributeType = mhIP.attributeType;
                                                    tempItem.remarkEn = mhIP.remarkEn;
                                                    tempItem.remark = mhIP.remark;
                                                    tempItem.enabled = mhIP.enabled;
                                                    tempListAnother.add(tempItem);
                                                }
                                                rsAnother[0] = new ROLE_DATA[tempListAnother.size()];
                                                rsAnother[0] = tempListAnother.toArray(rsAnother[0]);
                                            }
                                            sRoleJson = CommonFunction.AddRootRoleCertificate(rsToken, rsCert, rsAnother);
                                        }
                                        int[] pUSER_ID = new int[1];
                                        String param1 = db.S_BO_USER_INSERT(EscapeUtils.escapeHtml(Username.trim()), Password,
                                                EscapeUtils.escapeHtml(FullName.trim()), UserRole, BranchName.trim(),
                                                EscapeUtils.escapeHtml(Email.trim()),
                                                EscapeUtils.escapeHtml(MobileNumber.trim()), sRoleJson, loginUID, pUSER_ID);
                                        if ("0".equals(param1)) {
                                            if (pUSER_ID[0] > 0) {
                                                int sParamSSL = 0;
                                                if(!"".equals(idSSL))
                                                {
                                                    if(request.getSession(false).getAttribute("sessHashSSLToken") != null)
                                                    {
                                                        String sCertSSL = request.getSession(false).getAttribute("sessCertSSLToken").toString().trim();
                                                        String sHashSSL = request.getSession(false).getAttribute("sessHashSSLToken").toString().trim();
                                                        String paramCert = db.S_BO_USER_UPDATE_CERTIFICATION(pUSER_ID[0], sCertSSL, sHashSSL, loginUID);
                                                        if("0".equals(paramCert))
                                                        {
                                                            request.getSession(false).setAttribute("sessCertSSLToken", null);
                                                            request.getSession(false).setAttribute("sessHashSSLToken", null);
                                                            sParamSSL = 0;
                                                        }
                                                        else
                                                        {
                                                            sParamSSL = 1;
                                                        }
                                                    }
                                                }
                                                int[] psResCode = new int[1];
                                                String[] sResMess = new String[1];
                                                ConnectConnector.SendMailCreateUser(String.valueOf(pUSER_ID[0]),
                                                    Password, psResCode, sResMess);
                                                if (psResCode[0] != 0) {
                                                    CommonFunction.LogErrorServlet(log, "SEND-EMAIL-CREATE-USER: " + sResMess[0]);
                                                }
                                                // SEND_MAIL
                                                request.getSession(false).setAttribute("RefreshUserSess", "1");
                                                if(sParamSSL == 0) {
                                                    strView = "0#0";
                                                } else {
                                                    strView = "4#0";
                                                }
                                            }
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    }
                                    else
                                    {
                                        strView = "4#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "changepass": {
                                // <editor-fold defaultstate="collapsed" desc="case: changepass methods.">
                                String anticsrf = request.getParameter("CsrfToken");
                                String strUsername = request.getParameter("Username");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    if(strUsername.trim().equals(request.getSession(false).getAttribute("sUserID").toString().trim())) {
                                        String strPassOld = EscapeUtils.CheckTextNull(request.getParameter("OldPass"));
                                        String strPassNew = EscapeUtils.CheckTextNull(request.getParameter("NewPass"));
                                        if(!"".equals(strPassOld) && !"".equals(strPassNew))
                                        {
                                            if(strPassNew.contains(" "))
                                            {
                                                strView = "PASS_SPACE#0";
                                            } else {
                                                String strMin = "0";
                                                String strMax = "0";
                                                Boolean strNumericPass = false;
                                                Boolean strAlphaPass = false;
                                                Boolean strSpecialPass = false;
                                                Boolean strUpcaseAlphanumeric = false;
                                                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                if (sessGeneralPolicy[0].length > 0) {
                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
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
                                                if(strPassNew.length() < Integer.parseInt(strMin) || strPassNew.length() > Integer.parseInt(strMax)) {
                                                    sSuccess = "PASS_LENGTH";
                                                } else {
                                                    if(strPassOld.equals(strPassNew)) {
                                                        sSuccess = "PASS_SAME";
                                                    } else {
                                                        if(strNumericPass == true) {
                                                            if(!CommonFunction.stringContainsNumber(strPassNew)) {
                                                                sSuccess = "PASS_NUMBER";
                                                            }
                                                        }
                                                        if("0".equals(sSuccess)) {
                                                            if(strAlphaPass == true) {
                                                                if(!CommonFunction.stringContainsAlphaNumeric(strPassNew)) {
                                                                    sSuccess = "PASS_ALPHA";
                                                                }
                                                            }
                                                        }
                                                        if("0".equals(sSuccess)) {
                                                            if(strSpecialPass == true) {
                                                                if(!CommonFunction.stringContainsSpecial(strPassNew)) {
                                                                    sSuccess = "PASS_SPECIAL";
                                                                }
                                                            }
                                                        }
                                                        if("0".equals(sSuccess)) {
                                                            if(strUpcaseAlphanumeric == true) {
                                                                if(!CommonFunction.stringContainsAlphaCapital(strPassNew)) {
                                                                    sSuccess = "PASS_CAPITAL";
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if("0".equals(sSuccess)) {
                                                    String strUserID = (String) request.getSession(false).getAttribute("UserID");
                                                    String param1 = db.S_BO_USER_RESET_PASSWORD(EscapeUtils.escapeHtml(strUsername),
                                                            strUserID, strPassOld.replace("'", ""), strPassNew.replace("'", ""));
                                                    if ("0".equals(param1)) {
                                                        request.getSession(false).setAttribute("pUserID", strPassNew);
                                                        strView = "0#0";
                                                    } else {
                                                        strView = param1 + "#" + param1;
                                                    }
                                                } else {
                                                    strView = sSuccess + "#0";
                                                }
                                            }
                                        } else {
                                            strView = "PASS_EMPTY#0";
                                        }
                                        System.out.println("strView: " + strView);
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "Change Password: " + strUsername.trim() + " Invalid!");
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "changepassdefault": {
                                // <editor-fold defaultstate="collapsed" desc="case: changepassdefault methods.">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    Config config = new Config();
                                    String strPassConfig = "";// config.GetPropertybyCode(Definitions.CONFIG_DEFAULT_PASSWORD_USER);
                                    GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy1[0].length > 0) {
                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                        {
                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DEFAULT_PASSWORD_ACCOUNT))
                                            {
                                                strPassConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                    if("".equals(strPassConfig)) {
                                        strPassConfig = config.GetPropertybyCode(Definitions.CONFIG_DEFAULT_PASSWORD_USER);
                                    }
                                    String strUsername = request.getParameter("Username").trim();
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id").trim());
//                                    if ("".equals(strUsername.trim())) {
//                                        strUsername = request.getParameter("Username").trim();
//                                    }
                                    String param1 = db.S_BO_USER_RESET_PASSWORD_DEFAULT(strUsername, strPassConfig.trim(), loginUID.trim());
                                    if ("0".equals(param1)) {
                                        if (!"".equals(id)) {
                                            int[] psResCode = new int[1];
                                            String[] sResMess = new String[1];
                                            ConnectConnector.SendMailForgotPass(id, strPassConfig, psResCode, sResMess);
                                            if (psResCode[0] != 0) {
                                                CommonFunction.LogErrorServlet(log, "SEND-EMAIL-RESET-USER: " + sResMess[0]);
                                            }
                                            // SEND_MAIL
                                        }
                                        strView = "0#0#" + strPassConfig;
                                        request.getSession(false).setAttribute("RefreshUserSess", "1");
                                    } else {
                                        strView = param1 + "#" + param1;
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "permisionlistfunroleadd": {
                                //<editor-fold defaultstate="collapsed" desc="permisionlistfunroleadd">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String sROLE_SET = "";
                                    ROLE[][] rsPer = new ROLE[1][];
                                    db.S_BO_ROLE_DETAIL(EscapeUtils.escapeHtml(sID), rsPer);
                                    if (rsPer[0].length > 0) {
                                        sROLE_SET = EscapeUtils.CheckTextNull(rsPer[0][0].PROPERTIES);
                                    }
                                    sessionsa.setAttribute("sessRoleSetListPermision", sROLE_SET);
                                    strView = "0#0";
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "permisionlistfunroleedit": {
                                //<editor-fold defaultstate="collapsed" desc="permisionlistfunroleedit">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String sRoleUser = EscapeUtils.CheckTextNull(request.getParameter("sRoleUser"));
                                    if ("".equals(sRoleUser)) {
                                        sRoleUser = Definitions.CONFIG_ROLESET_CHECK_NULL;
                                    }
                                    String sROLE_SET = "";
                                    ROLE[][] rsPer = new ROLE[1][];
                                    db.S_BO_ROLE_DETAIL(EscapeUtils.escapeHtml(sID), rsPer);
                                    if (rsPer[0].length > 0) {
                                        sROLE_SET = EscapeUtils.CheckTextNull(rsPer[0][0].PROPERTIES);
                                    }
                                    sessionsa.setAttribute("sessRoleSetListPermisionEdit", sROLE_SET + "###" + sRoleUser);
                                    strView = "0#0";
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "backformpage": {
                                //<editor-fold defaultstate="collapsed" desc="backformpage">
                                String idSession = request.getParameter("idSession");
                                request.getSession(false).setAttribute(idSession, "1");
                                strView = "0";
                                //</editor-fold>
                                break;
                            }
                            case "checkcerttouser": {
                                // <editor-fold defaultstate="collapsed" desc="checkcerttouser">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = request.getParameter("id");
//                                    String idRole = EscapeUtils.CheckTextNull(request.getParameter("idRole"));
                                    // ADMIN cung tao lenh, nen van check ton tai CTS //
//                                    if (idRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
//                                        || idRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
//                                        || idRole.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
//                                        || idRole.equals(Definitions.CONFIG_ROLE_ID_CA_ACCOUNTANT)
//                                        || idRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
//                                        || idRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR)
//                                        || idRole.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT))
//                                    {
//                                        strView = "0#0";
//                                    } else {
                                        int param1 = db.S_BO_TOTAL_CERTIFICATION_BY_USER(EscapeUtils.escapeHtml(id.trim()));
                                        if (param1 == 0) {
                                            strView = "0#0";
                                        } else {
                                            strView = "0#1";
                                        }
//                                    }
                                } else {
                                    strView = (new StringBuilder()).append(Definitions.CONFIG_EXCEPTION_STRING_CSRF).append("#0").toString();
                                }
                                break;
                                //</editor-fold>
                            }
                            case "deleteuser": {
                                // <editor-fold defaultstate="collapsed" desc="deleteuser">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf")))
                                {
                                    String id = request.getParameter("id");
                                    String isHasCert = request.getParameter("isHasCert");
                                    String idToUSER = request.getParameter("idToUSER");
                                    String idToAGENT_NAME = request.getParameter("idToAGENT_NAME");
                                    if ("1".equals(isHasCert)) {
                                        int param2 = db.S_BO_CERTIFICATION_CHANGE_CREATED_BY(id, idToUSER, idToAGENT_NAME, loginUID);
                                        if (param2 == 0) {
                                            int param1 = db.S_BO_USER_CANCEL(EscapeUtils.escapeHtml(id.trim()), loginUID);
                                            if (param1 == 0) {
                                                request.getSession(false).setAttribute("RefreshUserSess", "1");
                                                strView = "0#0";
                                            } else {
                                                strView = String.valueOf(param1) + "#" + String.valueOf(param1); 
                                            }
                                        } else {
                                            strView = (new StringBuilder()).append(Definitions.CONFIG_EXCEPTION_STRING_ERROR).append("#0").toString();
                                        }
                                    } else {
                                        int param1 = db.S_BO_USER_CANCEL(EscapeUtils.escapeHtml(id.trim()), loginUID);
                                        if (param1 == 0) {
                                            request.getSession(false).setAttribute("RefreshUserSess", "1");
                                            strView = "0#0";
                                        } else {
                                            strView = String.valueOf(param1) + "#" + String.valueOf(param1); 
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "deletetokenssl": {
                                // <editor-fold defaultstate="collapsed" desc="deletetokenssl">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    request.getSession(false).setAttribute("sessCertSSLToken", null);
                                    request.getSession(false).setAttribute("sessHashSSLToken", null);
                                    strView = "0#0";
                                } else {
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
