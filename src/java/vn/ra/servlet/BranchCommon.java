package vn.ra.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PKI_FORMFACTOR;
import vn.ra.object.RESTJWTSecureProperties;
import vn.ra.object.ROLE;
import vn.ra.object.SOAPSecureProperties;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDbPhaseTwo;
import vn.ra.utility.Config;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author THANH
 */
public class BranchCommon extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BranchCommon.class);
    private static final long serialVersionUID = 6106269076155338045L;

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
                if (sOutInner == 1) {
                    String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    String idParam = request.getParameter("idParam");
                    if (null != idParam) {
                        switch (idParam) {
                            case "editbranch": {
                                //<editor-fold defaultstate="collapsed" desc="editbranch">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                    String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    String BranchParent = EscapeUtils.escapeHtml(request.getParameter("BranchParent"));
                                    String BranchCode = EscapeUtils.escapeHtml(request.getParameter("BranchCode"));
                                    String Address = EscapeUtils.escapeHtml(request.getParameter("Address"));
                                    String CityProvince = request.getParameter("CityProvince");
                                    String WorkPhone = EscapeUtils.escapeHtml(request.getParameter("WorkPhone"));
                                    String DISCOUNT_RATE_PROFILE = EscapeUtils.CheckTextNull(request.getParameter("DISCOUNT_RATE_PROFILE"));
                                    String Remark = EscapeUtils.escapeHtml(request.getParameter("Remark"));
                                    String Remark_EN = EscapeUtils.escapeHtml(request.getParameter("Remark_EN"));
                                    String EMAIL = EscapeUtils.escapeHtml(request.getParameter("EMAIL"));
                                    String REPRESENTATIVE = EscapeUtils.escapeHtml(request.getParameter("REPRESENTATIVE"));
                                    String REPRESENTATIVE_POSITION = EscapeUtils.escapeHtml(request.getParameter("REPRESENTATIVE_POSITION"));
                                    String TAX_CODE = EscapeUtils.escapeHtml(request.getParameter("TAX_CODE"));
                                    String BRANCH_ROLE = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ROLE"));
                                    String pBRANCH_STATE_ID = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_STATE"));
                                    String idShareModeEnabled = EscapeUtils.CheckTextNull(request.getParameter("idShareModeEnabled"));
                                    String CALLBACK_URL_NOTICE = EscapeUtils.CheckTextNull(request.getParameter("CALLBACK_URL_NOTICE"));
                                    String CALLBACK_WHEN_APPROVE = EscapeUtils.CheckTextNull(request.getParameter("CALLBACK_WHEN_APPROVE"));
                                    String LIMIT_REVOKE1 = EscapeUtils.CheckTextNull(request.getParameter("pLIMIT_REVOKE1"));
                                    String LIMIT_REVOKE2 = EscapeUtils.CheckTextNull(request.getParameter("pLIMIT_REVOKE2"));
                                    String LIMIT_REVOKE3 = EscapeUtils.CheckTextNull(request.getParameter("pLIMIT_REVOKE3"));
                                    String sSYS_DISCOUNT_RATE = "";
                                    String sCallBackApproveEnabled = "1";
//                                    String sIssueP12Enabled = "";
                                    boolean isAccessBranch = false;
                                    String sessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                    Config conf = new Config();
                                    String sAgentAccessBranch = conf.GetPropertybyCode(Definitions.CONFIG_BRANCH_TREE_ENABLED);
                                    if("1".equals(sAgentAccessBranch)) {
                                        isAccessBranch = true;
                                    } else {
                                        if(sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            isAccessBranch = true;
                                        }
                                    }
                                    if(isAccessBranch == true) {
                                        if(!"".equals(BranchParent) && !"".equals(Remark) && !"".equals(Remark_EN) && !"".equals(BranchCode)
                                             && !"".equals(pBRANCH_STATE_ID) && !"".equals(CityProvince))
                                        {
                                            int isPass = 0;
                                            if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)){
                                                if("".equals(Address) || "".equals(EMAIL) || "".equals(WorkPhone) || "".equals(REPRESENTATIVE)){
                                                    isPass = 1;
                                                }
                                            }
                                            if(isPass == 0) {
                                                if(Remark.length() <= 256 && Remark_EN.length() <= 256 && Address.length() <= 256 && BranchCode.length() <= 64
                                                    && EMAIL.length() <=  128 && WorkPhone.length() <= 16 && REPRESENTATIVE.length() <= 64)
                                                {
                                                    if(CommonFunction.checkUnicodeFontString(BranchCode) == true) {
                                                        boolean approveCAProfileEnabled = false;
                                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                        if (sessGeneralPolicy[0].length > 0) {
                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                            {
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION))
                                                                {
                                                                    sSYS_DISCOUNT_RATE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                }
                                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_AUTO_APPROVED_FOR_EACH_CERTIFICATION_PROFILE_OPTION))
                                                                {
                                                                    if("1".equals(EscapeUtils.CheckTextNull(rsPolicy1.VALUE))) {
                                                                        approveCAProfileEnabled = true;
                                                                    }
                                                                }
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CALLBACK_URL_APPROVED_ENABLED)) {
                                                                    sCallBackApproveEnabled = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                }
//                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_OPTION_ALLOW_ISSUING_P12)) {
//                                                                    sIssueP12Enabled = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
//                                                                }
                                                            }
                                                        }
                                                        if(!"1".equals(sSYS_DISCOUNT_RATE)) {
                                                            DISCOUNT_RATE_PROFILE = "";
                                                        } else {
                                                            if(DISCOUNT_RATE_PROFILE.equals(Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL)) {
                                                                DISCOUNT_RATE_PROFILE = "1";
                                                            }
                                                        }
                                                        if(BRANCH_ROLE.equals(Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL)) {
                                                            BRANCH_ROLE = "1";
                                                        }
                                                        String pCERTIFICATION_PROFILE_PROPERTIES = "";
                                                        CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfileBranchAccess");
                                                        if(resProfileData != null && resProfileData[0] != null && resProfileData[0].length > 0) {
                                                            pCERTIFICATION_PROFILE_PROPERTIES = CommonFunction.renderProfilePolicyForBranchRole(approveCAProfileEnabled, resProfileData[0]);
                                                        }
                                                        BRANCH[][] rsBranch;
                                                        rsBranch = new BRANCH[1][];
                                                        int intParentOld = 0;
                                                        com.S_BO_BRANCH_DETAIL(ids, rsBranch);
                                                        if(rsBranch[0].length > 0) {
                                                            intParentOld = rsBranch[0][0].PARENT_ID;
                                                        }
                                                        String param1 = com.S_BO_BRANCH_UPDATE(Integer.parseInt(ids), BranchCode,
                                                            CityProvince, Remark_EN, Remark, BranchParent,
                                                            WorkPhone, Address, loginUID, EMAIL, REPRESENTATIVE, REPRESENTATIVE_POSITION,
                                                            TAX_CODE, DISCOUNT_RATE_PROFILE, BRANCH_ROLE,
                                                            pCERTIFICATION_PROFILE_PROPERTIES, pBRANCH_STATE_ID, CALLBACK_URL_NOTICE);
                                                        if ("0".equals(param1)) {
                                                            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                            if(intParentOld != Integer.parseInt(BranchParent)) {
                                                                com.S_BO_BRANCH_CHANGE_PARENT_ID(Integer.parseInt(ids), intParentOld, Integer.parseInt(BranchParent), loginUID);
                                                                //<editor-fold defaultstate="collapsed" desc="### SET TREE BRANCH">
                                                                rsBranch = new BRANCH[1][];
                                                                String sessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                                                String sesSessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                                                                com.S_BO_BRANCH_GET_TREE_BRANCH(Integer.parseInt(sessUserAgentID), Integer.parseInt(sesSessLanguage), rsBranch);
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
                                                                com.S_BO_BRANCH_GET_TREE_BRANCH_ROOT(Integer.parseInt(sessUserAgentID), Integer.parseInt(sesSessLanguage), rsBranch);
                                                                if(rsBranch[0].length > 0) {
                                                                    request.getSession(false).setAttribute("sessTreeBranchSystemRoot", rsBranch);
                                                                }
                                                                rsBranch = new BRANCH[1][];
                                                                String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                                com.S_BO_BRANCH_GET_TREE_BRANCH_AGENCY(Integer.parseInt(sessUserAgentID), Integer.parseInt(sesSessLanguage), rsBranch, SessLevelBranch);
                                                                if(rsBranch[0].length > 0) {
                                                                    request.getSession(false).setAttribute("sessTreeBranchSystemAgency", rsBranch);
                                                                }
                                                                //</editor-fold>
                                                            }
                                                            //<editor-fold defaultstate="collapsed" desc="### LIMIT_REVOKE">
                                                            String sLIMIT_REVOKE = "";
                                                            if(!"".equals(LIMIT_REVOKE1) || !"".equals(LIMIT_REVOKE2) || !"".equals(LIMIT_REVOKE3)){
                                                                sLIMIT_REVOKE = LIMIT_REVOKE1 + ";" + LIMIT_REVOKE2 + ";" + LIMIT_REVOKE3;
                                                            }
                                                            String isCheckRevoke = conf.GetTryPropertybyCode(Definitions.CONFIG_FORBIDEN_REVOKE_CONTINUOU_DOUBLE_ENABLED);
                                                            if("1".equals(isCheckRevoke)) {
                                                                com.S_BO_BRANCH_UPDATE_LIMIT_REVOKE(Integer.parseInt(ids), sLIMIT_REVOKE, loginUID);
                                                            }
                                                            //</editor-fold>
                                                            
                                                            //<editor-fold defaultstate="collapsed" desc="### RP ACCESS ESIGNCLOUD">
                                                            if(sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                String pRP_ACCESS_ESIGNCLOUD = EscapeUtils.CheckTextNull(request.getParameter("pRP_ACCESS_ESIGNCLOUD"));
                                                                com.S_BO_BRANCH_UPDATE_RP_ACCESSED(Integer.parseInt(ids), pRP_ACCESS_ESIGNCLOUD, loginUID);
                                                            }
                                                            //</editor-fold>
                                                            
                                                            //<editor-fold defaultstate="collapsed" desc="UPDATE ISSUE_P12_ENABLED">
//                                                            if("1".equals(sIssueP12Enabled)) {
                                                            String issueP12Enabled = EscapeUtils.CheckTextNull(request.getParameter("issueP12Enabled"));
                                                            dbTwo.S_BO_BRANCH_UPDATE_API_ISSUE_P12_ENABLED(Integer.parseInt(ids), issueP12Enabled, loginUID);
//                                                            }
                                                            //</editor-fold>
                                                            
                                                            //<editor-fold defaultstate="collapsed" desc="CALLBACK_URL_APPROVED">
                                                            if("1".equals(sCallBackApproveEnabled)) {
                                                                dbTwo.S_BO_BRANCH_UPDATE_CALLBACK_URL_APPROVED(Integer.parseInt(ids), CALLBACK_WHEN_APPROVE, loginUID);
                                                            }
                                                            //</editor-fold>
                                                            
                                                            if("1".equals(idShareModeEnabled))
                                                            {
                                                                idShareModeEnabled = "true";
                                                            } else {
                                                                idShareModeEnabled = "false";
                                                            }
                                                            CERTIFICATION_POLICY_DATA[][] resPolicyData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfilePolicyAll");
                                                            if(resPolicyData != null) {
                                                                if(resPolicyData[0].length > 0)
                                                                {
                                                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resPolicyData[0])
                                                                    {
                                                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE))
                                                                        {
                                                                            resProfileData1.name = idShareModeEnabled;
                                                                        }
                                                                    }
                                                                    request.getSession(false).setAttribute("SessProfilePolicyAll", resPolicyData);
                                                                    String sJSON_POLICY = CommonFunction.renderProfilePolicyAPI(resPolicyData[0]);
                                                                    com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(ids), "", sJSON_POLICY, "", "", loginUID,"");
                                                                }
                                                            }
                                                            strView = "0#0";
                                                            request.getSession(false).setAttribute("SessProfileBranchAccess", null);
                                                            request.getSession(false).setAttribute("SessRefreshBranch", "1");
                                                        } else {
                                                            strView = param1 + "#" + param1;
                                                        }
                                                    } else {
                                                        CommonFunction.LogErrorServlet(log, "AddBranch: Branch code does not contain accented characters");
                                                        strView = "12#0";
                                                    }
                                                } else {
                                                    CommonFunction.LogErrorServlet(log, "UpdateBranch: Length is invalid");
                                                    strView = "11#0";
                                                }
                                            } else {
                                                CommonFunction.LogErrorServlet(log, "UpdateBranch: Please enter all required");
                                                strView = "10#0";
                                            }
                                        } else {
                                            CommonFunction.LogErrorServlet(log, "UpdateBranch: Please enter all required");
                                            strView = "10#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "deletebranch": {
                                //<editor-fold defaultstate="collapsed" desc="deletebranch">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    boolean isAccessBranch = false;
                                    String sessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                    Config conf = new Config();
                                    String sAgentAccessBranch = conf.GetPropertybyCode(Definitions.CONFIG_BRANCH_TREE_ENABLED);
                                    if("1".equals(sAgentAccessBranch)) {
                                        isAccessBranch = true;
                                    } else {
                                        if(sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            isAccessBranch = true;
                                        }
                                    }
                                    if(isAccessBranch == true) {
                                        String ids = request.getParameter("id");
                                        String param1 = com.S_BO_BRANCH_CANCEL(Integer.parseInt(ids),
                                                EscapeUtils.escapeHtml(loginUID));
                                        if ("0".equals(param1)) {
                                            strView = "0#0";
                                            request.getSession(false).setAttribute("SessRefreshBranch", "1");
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "editlogobranch": {
                                //<editor-fold defaultstate="collapsed" desc="editlogobranch">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    boolean isAccessBranch = false;
                                    String sessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                    Config conf = new Config();
                                    String sAgentAccessBranch = conf.GetPropertybyCode(Definitions.CONFIG_BRANCH_TREE_ENABLED);
                                    if("1".equals(sAgentAccessBranch)) {
                                        isAccessBranch = true;
                                    } else {
                                        if(sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            isAccessBranch = true;
                                        }
                                    }
                                    if(isAccessBranch == true) {
                                        String ids = request.getParameter("id");
                                        String Logo = EscapeUtils.CheckTextNull(request.getParameter("Logo"));
                                        byte[] bytesLogo = null;
                                        if(!"".equals(Logo))
                                        {
                                            bytesLogo = Logo.getBytes();
                                        }
                                        String param1 = com.S_BO_BRANCH_UPDATE_LOGO(Integer.parseInt(ids),
                                            bytesLogo, EscapeUtils.escapeHtml(loginUID));
                                        if ("0".equals(param1)) {
                                            strView = "0#0";
                                            request.getSession(false).setAttribute("SessRefreshBranch", "1");
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "addbranch": {
                                //<editor-fold defaultstate="collapsed" desc="addbranch">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                    String Remark = EscapeUtils.escapeHtml(request.getParameter("Remark"));
                                    String Remark_EN = EscapeUtils.escapeHtml(request.getParameter("Remark_EN"));
                                    String BranchParent = EscapeUtils.escapeHtml(request.getParameter("BranchParent"));// Definitions.CONFIG_AGENT_ROOT; // request.getParameter("BranchParent");
                                    String BranchCode = EscapeUtils.escapeHtml(request.getParameter("BranchCode"));
                                    String Address = EscapeUtils.escapeHtml(request.getParameter("Address"));
                                    String CityProvince = request.getParameter("CityProvince");
                                    String WorkPhone = EscapeUtils.escapeHtml(request.getParameter("WorkPhone"));
                                    String EMAIL = EscapeUtils.escapeHtml(request.getParameter("EMAIL"));
                                    String DISCOUNT_RATE_PROFILE = EscapeUtils.CheckTextNull(request.getParameter("DISCOUNT_RATE_PROFILE"));
                                    String REPRESENTATIVE = EscapeUtils.escapeHtml(request.getParameter("REPRESENTATIVE"));
                                    String REPRESENTATIVE_POSITION = EscapeUtils.escapeHtml(request.getParameter("REPRESENTATIVE_POSITION"));
                                    String TAX_CODE = EscapeUtils.escapeHtml(request.getParameter("TAX_CODE"));
                                    String Logo = EscapeUtils.CheckTextNull(request.getParameter("Logo"));
                                    String User_Username = EscapeUtils.escapeHtml(request.getParameter("User_Username"));
                                    String User_Fullname = EscapeUtils.escapeHtml(request.getParameter("User_Fullname"));
                                    String User_WorkPhone = EscapeUtils.escapeHtml(request.getParameter("User_WorkPhone"));
                                    String User_EMAIL = EscapeUtils.escapeHtml(request.getParameter("User_EMAIL"));
                                    String BRANCH_ROLE = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ROLE"));
                                    String CALLBACK_URL_NOTICE = EscapeUtils.CheckTextNull(request.getParameter("CALLBACK_URL_NOTICE"));
                                    String CALLBACK_WHEN_APPROVE = EscapeUtils.CheckTextNull(request.getParameter("CALLBACK_WHEN_APPROVE"));
                                    String idShareModeEnabled = EscapeUtils.CheckTextNull(request.getParameter("idShareModeEnabled"));
                                    boolean isAccessBranch = false;
                                    String sessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                    Config conf = new Config();
                                    String sAgentAccessBranch = conf.GetPropertybyCode(Definitions.CONFIG_BRANCH_TREE_ENABLED);
                                    if("1".equals(sAgentAccessBranch)) {
                                        isAccessBranch = true;
                                    } else {
                                        if(sessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            isAccessBranch = true;
                                        }
                                    }
                                    if(isAccessBranch == true) {
                                        if(!"".equals(BranchParent) && !"".equals(Remark) && !"".equals(Remark_EN) && !"".equals(BranchCode)
                                             && !"".equals(User_Username) && !"".equals(CityProvince)
                                             && !"".equals(User_Fullname) && !"".equals(User_WorkPhone) && !"".equals(User_EMAIL))
                                        {
                                            int isPass = 0;
                                            if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                if("".equals(Address) || "".equals(EMAIL) || "".equals(WorkPhone) || "".equals(REPRESENTATIVE)){
                                                    isPass = 1;
                                                }
                                            }
                                            if(isPass == 0) {
                                                if(Remark.length() <= 256 && Remark_EN.length() <= 256 && Address.length() <= 256 && BranchCode.length() <= 64
                                                    && EMAIL.length() <=  128 && WorkPhone.length() <= 16 && REPRESENTATIVE.length() <= 64
                                                    && User_Username.length() <= 64 && User_Fullname.length() <= 64
                                                    && User_EMAIL.length() <= 128 && User_WorkPhone.length() <= 16)
                                                {
                                                    if(CommonFunction.checkUnicodeFontString(BranchCode) == true) {
                                                        byte[] bytesLogo = null;
                                                        if(!"".equals(Logo))
                                                        {
                                                            bytesLogo = Logo.getBytes();
                                                        }
                                                        String sCERT_POLICY_PROPERTIES = "";
                                                        String sPassword = "";
                                                        String sSYS_DISCOUNT_RATE = "";
                                                        String sCallBackApproveEnabled = "1";
//                                                        String sIssueP12Enabled = "";
                                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                        if (sessGeneralPolicy[0].length > 0) {
                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                                            {
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION)) {
                                                                    sSYS_DISCOUNT_RATE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                }
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DEFAULT_PASSWORD_ACCOUNT)) {
                                                                    sPassword = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                }
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DEFAULT_CERTIFICATION_POLICY_PROPERTIES_FOR_BRANCH)) {
                                                                    sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                }
                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CALLBACK_URL_APPROVED_ENABLED)) {
                                                                    sCallBackApproveEnabled = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                }
//                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_OPTION_ALLOW_ISSUING_P12)) {
//                                                                    sIssueP12Enabled = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
//                                                                }
                                                            }
                                                        }
                                                        if(!"1".equals(sSYS_DISCOUNT_RATE)) {
                                                            DISCOUNT_RATE_PROFILE = "";
                                                        } else {
                                                            if(DISCOUNT_RATE_PROFILE.equals(Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL)) {
                                                                DISCOUNT_RATE_PROFILE = "1";
                                                            }
                                                        }
                                                        if(BRANCH_ROLE.equals(Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL)) {
                                                            BRANCH_ROLE = "1";
                                                        }
                                                        if(!"".equals(sCERT_POLICY_PROPERTIES))
                                                        {
                                                            CERTIFICATION_POLICY_DATA[][] resProfileData = new CERTIFICATION_POLICY_DATA[1][];
                                                            CommonFunction.getAllProfilePolicyForBranch(sCERT_POLICY_PROPERTIES, resProfileData);
                                                            if(resProfileData[0].length > 0)
                                                            {
                                                                if("1".equals(idShareModeEnabled))
                                                                {
                                                                    idShareModeEnabled = "true";
                                                                } else {
                                                                    idShareModeEnabled = "false";
                                                                }
                                                                for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                                                {
                                                                    if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE))
                                                                    {
                                                                        resProfileData1.name = idShareModeEnabled;
                                                                    }
                                                                }
                                                                sCERT_POLICY_PROPERTIES = CommonFunction.renderProfilePolicyAPI(resProfileData[0]);
                                                            }
                                                        }
                                                        int[] pBRANCH_ID = new int[1];
                                                        String param1 = com.S_BO_BRANCH_INSERT(BranchCode, CityProvince,
                                                            EscapeUtils.escapeHtml(Remark_EN), EscapeUtils.escapeHtml(Remark),
                                                            EscapeUtils.escapeHtml(BranchParent), EscapeUtils.escapeHtml(loginUID),
                                                            EscapeUtils.escapeHtml(WorkPhone), EscapeUtils.escapeHtml(Address), EscapeUtils.escapeHtml(EMAIL),
                                                            EscapeUtils.escapeHtml(REPRESENTATIVE), EscapeUtils.escapeHtml(REPRESENTATIVE_POSITION),
                                                            EscapeUtils.escapeHtml(TAX_CODE), bytesLogo, DISCOUNT_RATE_PROFILE, sCERT_POLICY_PROPERTIES, BRANCH_ROLE,
                                                            pBRANCH_ID, CALLBACK_URL_NOTICE);
                                                        if ("0".equals(param1)) {
                                                            //<editor-fold defaultstate="collapsed" desc="### SET TREE BRANCH">
                                                            BRANCH[][] rsBranch = new BRANCH[1][];
                                                            String sessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                                            String sesSessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                                                            com.S_BO_BRANCH_GET_TREE_BRANCH(Integer.parseInt(sessUserAgentID), Integer.parseInt(sesSessLanguage), rsBranch);
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
                                                            com.S_BO_BRANCH_GET_TREE_BRANCH_ROOT(Integer.parseInt(sessUserAgentID), Integer.parseInt(sesSessLanguage), rsBranch);
                                                            if(rsBranch[0].length > 0) {
                                                                request.getSession(false).setAttribute("sessTreeBranchSystemRoot", rsBranch);
                                                            }
                                                            rsBranch = new BRANCH[1][];
                                                            String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                            com.S_BO_BRANCH_GET_TREE_BRANCH_AGENCY(Integer.parseInt(sessUserAgentID), Integer.parseInt(sesSessLanguage), rsBranch, SessLevelBranch);
                                                            if(rsBranch[0].length > 0) {
                                                                request.getSession(false).setAttribute("sessTreeBranchSystemAgency", rsBranch);
                                                            }
                                                            //</editor-fold>
                                                            
                                                            //<editor-fold defaultstate="collapsed" desc="ISSUE_P12_ENABLED">
//                                                            if("1".equals(sIssueP12Enabled)) {
                                                            String issueP12Enabled = EscapeUtils.CheckTextNull(request.getParameter("issueP12Enabled"));
                                                            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                            dbTwo.S_BO_BRANCH_UPDATE_API_ISSUE_P12_ENABLED(pBRANCH_ID[0], issueP12Enabled, loginUID);
//                                                            }
                                                            //</editor-fold>
                                                            
                                                            //<editor-fold defaultstate="collapsed" desc="CALLBACK_URL_APPROVED">
                                                            if("1".equals(sCallBackApproveEnabled)) {
                                                                dbTwo.S_BO_BRANCH_UPDATE_CALLBACK_URL_APPROVED(pBRANCH_ID[0], CALLBACK_WHEN_APPROVE, loginUID);
                                                            }
                                                            //</editor-fold>

                                                            Config config = new Config();
                                                            if("".equals(sPassword)) {
                                                                sPassword = config.GetPropertybyCode(Definitions.CONFIG_DEFAULT_PASSWORD_USER);
                                                            }
                                                            String UserRole = Definitions.CONFIG_ROLE_ID_AGENT_ADMIN;
                                                            if("1".equals(BranchParent)) {
                                                                UserRole = Definitions.CONFIG_ROLE_ID_CA_ADMIN;
                                                            }
                                                            String sRoleJson = "";
                                                            ROLE[][] rsRole = new ROLE[1][];
                                                            com.S_BO_ROLE_DETAIL(UserRole, rsRole);
                                                            if(rsRole[0].length > 0){
                                                                sRoleJson = EscapeUtils.CheckTextNull(rsRole[0][0].PROPERTIES);
                                                            }
                                                            int[] pUSER_ID = new int[1];
                                                            String paramUser = com.S_BO_USER_INSERT(User_Username.trim(), sPassword,
                                                                    User_Fullname, UserRole, String.valueOf(pBRANCH_ID[0]),
                                                                    User_EMAIL, User_WorkPhone, sRoleJson, loginUID, pUSER_ID);
                                                            if ("0".equals(paramUser)) {
                                                                if (pUSER_ID[0] > 0) {
                                                                    int[] psResCode = new int[1];
                                                                    String[] sResMess = new String[1];
                                                                    ConnectConnector.SendMailCreateUser(String.valueOf(pUSER_ID[0]),
                                                                        sPassword, psResCode, sResMess);
                                                                    if (psResCode[0] != 0) {
                                                                        CommonFunction.LogErrorServlet(log, "SEND-EMAIL-CREATE-USER: " + sResMess[0]);
                                                                    }
                                                                }
                                                                strView = "0#0";
                                                            } else {
                                                                CommonFunction.LogErrorServlet(log, "S_BO_USER_INSERT Result: " + paramUser);
                                                                strView = "0#USER_EXISTS";
                                                            }
                                                        } else {
                                                            strView = param1 + "#" + param1;
                                                        }
                                                    } else {
                                                        CommonFunction.LogErrorServlet(log, "AddBranch: Branch code does not contain accented characters");
                                                        strView = "12#0";
                                                    }
                                                } else {
                                                    CommonFunction.LogErrorServlet(log, "AddBranch: Length is invalid");
                                                    strView = "11#0";
                                                }
                                            } else {
                                                CommonFunction.LogErrorServlet(log, "AddBranch: Please enter all required");
                                                strView = "10#0";
                                            }
                                        } else {
                                            CommonFunction.LogErrorServlet(log, "AddBranch: Please enter all required");
                                            strView = "10#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "editsoapuibranch": {
                                //<editor-fold defaultstate="collapsed" desc="editsoapuibranch">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    ObjectMapper oMapperParse;
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    String sCheckAPIAllow = EscapeUtils.CheckTextNull(request.getParameter("sCheckAPIAllow"));
                                    String sAPIUsername = EscapeUtils.CheckTextNull(request.getParameter("sAPIUsername"));
                                    String sAPIPassword = EscapeUtils.CheckTextNull(request.getParameter("sAPIPassword"));
                                    String sAPISignature = EscapeUtils.CheckTextNull(request.getParameter("sAPISignature"));
                                    String sAPIPublicKeyPem = EscapeUtils.CheckTextNull(request.getParameter("sAPIPublicKeyPem"));
                                    String sAPIRemark = EscapeUtils.CheckTextNull(request.getParameter("sAPIRemark"));
                                    String sAPIRemarkEN = EscapeUtils.CheckTextNull(request.getParameter("sAPIRemarkEN"));
                                    if("1".equals(sCheckAPIAllow))
                                    {
                                        String sPassHash = "";
                                        if(!"".equals(sAPIPassword))
                                        {
                                            sPassHash = CommonFunction.hashPass((sAPIUsername.toLowerCase() + sAPIPassword.toLowerCase()).getBytes());
                                        } else {
                                            String sJSON_SOAP_OLD = "";
                                            BRANCH[][] rs = new BRANCH[1][];
                                            com.S_BO_BRANCH_DETAIL(id, rs);
                                            if (rs[0].length > 0) {
                                                sJSON_SOAP_OLD = EscapeUtils.CheckTextNull(rs[0][0].SOAP_SECURITY_PROPERTIES);
                                            }
                                            if(!"".equals(sJSON_SOAP_OLD))
                                            {
                                                oMapperParse = new ObjectMapper();
                                                SOAPSecureProperties itemParse = oMapperParse.readValue(sJSON_SOAP_OLD, SOAPSecureProperties.class);
                                                sPassHash = EscapeUtils.CheckTextNull(itemParse.password);
                                            }
                                        }
                                        if(!"".equals(sPassHash))
                                        {
                                            oMapperParse = new ObjectMapper();
                                            SOAPSecureProperties soapItem = new SOAPSecureProperties();
                                            soapItem.attributeType = "SOAPSecureProperties";
                                            soapItem.remark = sAPIRemark;
                                            soapItem.remarkEn = sAPIRemarkEN;
                                            soapItem.password = sPassHash;
                                            soapItem.signature = sAPISignature;
                                            soapItem.publicKeyPem = sAPIPublicKeyPem;
                                            String sJSON_SOAP = oMapperParse.writeValueAsString(soapItem);
//                                            CommonFunction.LogDebugString(log, "SOAP_UI", sJSON_SOAP);
                                            String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(id), sJSON_SOAP,"","","",loginUID,"");
                                            if ("0".equals(param1)) {
                                                strView = "0#0";
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        } else {
                                            strView = "PASS_NULL#0";
                                        }
                                    } else {
                                        String sJSON_SOAP = " ";
                                        String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(id), sJSON_SOAP,"","","",loginUID,"");
                                        if ("0".equals(param1)) {
                                            strView = "0#0";
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "editrestfulbranch": {
                                //<editor-fold defaultstate="collapsed" desc="editrestfulbranch">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    ObjectMapper oMapperParse;
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    String sCheckAPIAllow = EscapeUtils.CheckTextNull(request.getParameter("sCheckAPIAllow"));
                                    String sAPIUsername = EscapeUtils.CheckTextNull(request.getParameter("sRestAPIUsername"));
                                    String sAPIPassword = EscapeUtils.CheckTextNull(request.getParameter("sRestAPIPassword"));
                                    String sRestAPITime = EscapeUtils.CheckTextNull(request.getParameter("sRestAPITime"));
                                    String sRestAPISecret = EscapeUtils.CheckTextNull(request.getParameter("sRestAPISecret"));
                                    String sAPIRemark = EscapeUtils.CheckTextNull(request.getParameter("sRestAPIRemark"));
                                    String sAPIRemarkEN = EscapeUtils.CheckTextNull(request.getParameter("sRestAPIRemarkEN"));
                                    sRestAPITime = sRestAPITime.replace(",", "");
                                    if(!"".equals(sAPIUsername) && !"".equals(sRestAPITime) && !"".equals(sRestAPISecret)
                                        && !"".equals(sAPIRemark) && !"".equals(sAPIRemarkEN))
                                    {
                                        if(CommonFunction.checkNumberValid(sRestAPITime) == false) {
                                            strView = "TIME_FAIL#0";
                                        } else {
                                            if("1".equals(sCheckAPIAllow))
                                            {
                                                String sPassHash = "";
                                                if(!"".equals(sAPIPassword))
                                                {
                                                    sPassHash = CommonFunction.hashPass((sAPIUsername.toLowerCase() + sAPIPassword.toLowerCase()).getBytes());
                                                } else {
                                                    String sJSON_SOAP_OLD = "";
                                                    BRANCH[][] rs = new BRANCH[1][];
                                                    com.S_BO_BRANCH_DETAIL(id, rs);
                                                    if (rs[0].length > 0) {
                                                        sJSON_SOAP_OLD = EscapeUtils.CheckTextNull(rs[0][0].REST_JWT_PROPERTIES);
                                                    }
                                                    if(!"".equals(sJSON_SOAP_OLD))
                                                    {
                                                        oMapperParse = new ObjectMapper();
                                                        RESTJWTSecureProperties itemParse = oMapperParse.readValue(sJSON_SOAP_OLD, RESTJWTSecureProperties.class);
                                                        sPassHash = EscapeUtils.CheckTextNull(itemParse.password);
                                                    }
                                                }
                                                if(!"".equals(sPassHash))
                                                {
                                                    oMapperParse = new ObjectMapper();
                                                    RESTJWTSecureProperties soapItem = new RESTJWTSecureProperties();
                                                    soapItem.attributeType = "RestfulJWTSecureProperties";
                                                    soapItem.remark = sAPIRemark;
                                                    soapItem.remarkEn = sAPIRemarkEN;
                                                    soapItem.password = sPassHash;
                                                    soapItem.expirationTime = sRestAPITime.replace(",", "");
                                                    soapItem.secretKey = sRestAPISecret;
                                                    String sJSON_REST = oMapperParse.writeValueAsString(soapItem);
                                                    String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(id), "","","","",loginUID,sJSON_REST);
                                                    if ("0".equals(param1)) {
                                                        strView = "0#0";
                                                    } else {
                                                        strView = param1 + "#" + param1;
                                                    }
                                                } else {
                                                    strView = "PASS_NULL#0";
                                                }
                                            } else {
                                                String sJSON_REST = " ";
                                                String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(id), "","","","",loginUID,sJSON_REST);
                                                if ("0".equals(param1)) {
                                                    strView = "0#0";
                                                } else {
                                                    strView = param1 + "#" + param1;
                                                }
                                            }
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "BranchUpdateRestAPI: Please fill in the required information");
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            // profile
                            case "activeprofilepolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="activeprofilepolicybranch">
//                                ObjectMapper oMapperParse;
                                String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                String sProfileActive = EscapeUtils.CheckTextNull(request.getParameter("sProfileActive"));
                                String sProfileCode = EscapeUtils.CheckTextNull(request.getParameter("sProfileCode"));
                                boolean booProfileActive = false;
                                if("1".equals(sProfileActive))
                                {
                                    booProfileActive = true;
                                }
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfilePolicyAll");
//                                if(resProfileData[0].length > 0)
//                                {
//                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
//                                    {
//                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
//                                        {
//                                            if(resProfileData1.name.equals(sProfileCode))
//                                            {
//                                                resProfileData1.enabled = booProfileActive;
//                                            }
//                                        }
//                                    }
//                                }
                                if(resProfileData[0].length > 0)
                                {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                        {
                                            if(resProfileData1.name.equals(sProfileCode))
                                            {
                                                resProfileData1.enabled = booProfileActive;
                                            }
                                        }
                                    }
                                    request.getSession(false).setAttribute("SessProfilePolicyAll", resProfileData);
                                    String sJSON_POLICY = CommonFunction.renderProfilePolicyAPI(resProfileData[0]);// oMapperParse.writeValueAsString(resProfileData[0]);
//                                    CommonFunction.LogDebugString(log, "JSON_POLICY", sJSON_POLICY);
                                    String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "", sJSON_POLICY, "", "", loginUID,"");
                                    if ("0".equals(param1)) {
                                        strView = "0#0";
                                    } else {
                                        strView = param1 + "#" + param1;
                                    }
                                }
                                //</editor-fold>
                                break;
                            }
                            case "addprofilepolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="addprofilepolicybranch">
                                String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                String sProfileCert = EscapeUtils.CheckTextNull(request.getParameter("sProfileCert"));
                                CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                com.S_BO_CERTIFICATION_PROFILE_DETAIL(sProfileCert, rsProfile);
                                if(rsProfile[0].length > 0)
                                {
                                    boolean isValid = true;
                                    CERTIFICATION_POLICY_DATA[][] reProfileDataLast = new CERTIFICATION_POLICY_DATA[1][];
                                    CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfilePolicyAll");
                                    if(resProfileData[0].length > 0)
                                    {
                                        for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                        {
                                            if(resProfileData1.name.equals(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME)))
                                            {
                                                isValid = false;
                                                break;
                                            }
                                        }
                                    }
                                    if(isValid == true)
                                    {
                                        if(resProfileData[0].length > 0)
                                        {
                                            ArrayList<CERTIFICATION_POLICY_DATA> tempList;
                                            tempList = new ArrayList<>();
                                            tempList.addAll(Arrays.asList(resProfileData[0]));
                                            CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                                            rsItem.name = EscapeUtils.CheckTextNull(rsProfile[0][0].NAME);
                                            rsItem.remark = rsProfile[0][0].REMARK;
                                            rsItem.remarkEn = rsProfile[0][0].REMARK_EN;
                                            rsItem.enabled = true;
                                            rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST;
                                            tempList.add(rsItem);
                                            reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                            reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                        }
                                        if(reProfileDataLast[0].length > 0)
                                        {
                                            request.getSession(false).setAttribute("SessProfilePolicyAll", reProfileDataLast);
                                            String sJSON_POLICY = CommonFunction.renderProfilePolicyAPI(reProfileDataLast[0]);// oMapperParse.writeValueAsString(resProfileData[0]);
//                                            CommonFunction.LogDebugString(log, "JSON_POLICY_PROFILE", sJSON_POLICY);
                                            String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "",sJSON_POLICY,"","", loginUID,"");
                                            if ("0".equals(param1)) {
                                                strView = "0#0";
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        }
                                    } else {
                                        if(resProfileData[0].length > 0)
                                        {
                                            ArrayList<CERTIFICATION_POLICY_DATA> tempList;
                                            tempList = new ArrayList<>();
                                            tempList.addAll(Arrays.asList(resProfileData[0]));
                                            for(int i=0;i<tempList.size();i++)
                                            {
                                                if(tempList.get(i).name.equals(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME)))
                                                {
                                                    tempList.get(i).remark=rsProfile[0][0].REMARK;
                                                    tempList.get(i).remarkEn=rsProfile[0][0].REMARK_EN;
                                                    tempList.get(i).enabled=true;
                                                    break;
                                                }
                                            }
                                            reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                            reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                        }
                                        if(reProfileDataLast[0].length > 0)
                                        {
                                            request.getSession(false).setAttribute("SessProfilePolicyAll", reProfileDataLast);
                                            String sJSON_POLICY = CommonFunction.renderProfilePolicyAPI(reProfileDataLast[0]);// oMapperParse.writeValueAsString(resProfileData[0]);
//                                            CommonFunction.LogDebugString(log, "JSON_POLICY_PROFILE", sJSON_POLICY);
                                            String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "",sJSON_POLICY,"","", loginUID,"");
                                            if ("0".equals(param1)) {
                                                strView = "0#0";
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        }
                                    }
                                }
                                //</editor-fold>
                                break;
                            }
                            // factor
                            case "activefactorpolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="activefactorpolicybranch">
                                String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                String sFactorActive = EscapeUtils.CheckTextNull(request.getParameter("sFactorActive"));
                                String sFactorCode = EscapeUtils.CheckTextNull(request.getParameter("sFactorCode"));
                                boolean booProfileActive = false;
                                if("1".equals(sFactorActive))
                                {
                                    booProfileActive = true;
                                }
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfilePolicyAll");
                                if(resProfileData[0].length > 0)
                                {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PKI_FORMFACTOR_LIST))
                                        {
                                            if(resProfileData1.name.equals(sFactorCode))
                                            {
                                                resProfileData1.enabled = booProfileActive;
                                            }
                                        }
                                    }
                                    request.getSession(false).setAttribute("SessProfilePolicyAll", resProfileData);
                                    String sJSON_POLICY = CommonFunction.renderProfilePolicyAPI(resProfileData[0]);
                                    String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "", sJSON_POLICY, "", "", loginUID,"");
                                    if ("0".equals(param1)) {
                                        strView = "0#0";
                                    } else {
                                        strView = param1 + "#" + param1;
                                    }
                                }
                                //</editor-fold>
                                break;
                            }
                            case "addfactorpolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="addfactorpolicybranch">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf")))
                                {
                                    String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String sFactorCert = EscapeUtils.CheckTextNull(request.getParameter("sFactorCert"));
                                    PKI_FORMFACTOR[][] rsFactor = new PKI_FORMFACTOR[1][];
                                    com.S_BO_PKI_FORMFACTOR_DETAIL(sFactorCert, rsFactor);
                                    if(rsFactor[0].length > 0)
                                    {
                                        boolean isValid = true;
                                        CERTIFICATION_POLICY_DATA[][] reProfileDataLast = new CERTIFICATION_POLICY_DATA[1][];
                                        CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfilePolicyAll");
                                        if(resProfileData[0].length > 0)
                                        {
                                            for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                            {
                                                if(resProfileData1.name.equals(EscapeUtils.CheckTextNull(rsFactor[0][0].NAME)))
                                                {
                                                    isValid = false;
                                                    break;
                                                }
                                            }
                                        }
                                        if(isValid == true)
                                        {
                                            if(resProfileData[0].length > 0)
                                            {
                                                ArrayList<CERTIFICATION_POLICY_DATA> tempList;
                                                tempList = new ArrayList<>();
                                                tempList.addAll(Arrays.asList(resProfileData[0]));
                                                CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                                                rsItem.name = EscapeUtils.CheckTextNull(rsFactor[0][0].NAME);
                                                rsItem.remark = rsFactor[0][0].REMARK;
                                                rsItem.remarkEn = rsFactor[0][0].REMARK_EN;
                                                rsItem.enabled = true;
                                                rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PKI_FORMFACTOR_LIST;
                                                tempList.add(rsItem);
                                                reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                                reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                            }
                                            if(reProfileDataLast[0].length > 0)
                                            {
                                                request.getSession(false).setAttribute("SessProfilePolicyAll", reProfileDataLast);
                                                String sJSON_POLICY = CommonFunction.renderProfilePolicyAPI(reProfileDataLast[0]);
    //                                            CommonFunction.LogDebugString(log, "JSON_POLICY_FACTOR", sJSON_POLICY);
                                                String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "",sJSON_POLICY,"","", loginUID,"");
                                                if ("0".equals(param1)) {
                                                    strView = "0#0";
                                                } else {
                                                    strView = param1 + "#" + param1;
                                                }
                                            }
                                        } else {
                                            if(resProfileData[0].length > 0)
                                            {
                                                ArrayList<CERTIFICATION_POLICY_DATA> tempList;
                                                tempList = new ArrayList<>();
                                                tempList.addAll(Arrays.asList(resProfileData[0]));
                                                for(int i=0;i<tempList.size();i++)
                                                {
                                                    if(tempList.get(i).name.equals(EscapeUtils.CheckTextNull(rsFactor[0][0].NAME)))
                                                    {
                                                        tempList.get(i).remark=rsFactor[0][0].REMARK;
                                                        tempList.get(i).remarkEn=rsFactor[0][0].REMARK_EN;
                                                        tempList.get(i).enabled=true;
                                                        break;
                                                    }
                                                }
                                                reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                                reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                            }
                                            if(reProfileDataLast[0].length > 0)
                                            {
                                                request.getSession(false).setAttribute("SessProfilePolicyAll", reProfileDataLast);
                                                String sJSON_POLICY = CommonFunction.renderProfilePolicyAPI(reProfileDataLast[0]);
    //                                            CommonFunction.LogDebugString(log, "JSON_POLICY_FACTOR", sJSON_POLICY);
                                                String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "",sJSON_POLICY,"","", loginUID,"");
                                                if ("0".equals(param1)) {
                                                    strView = "0#0";
                                                } else {
                                                    strView = param1 + "#" + param1;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "activeotherpolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="activeotherpolicybranch">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf")))
                                {
                                    String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                    String isCheckProfileAll = EscapeUtils.CheckTextNull(request.getParameter("isCheckProfileAll"));
                                    String isCheckProfieAutoApprove = EscapeUtils.CheckTextNull(request.getParameter("isCheckProfieAutoApprove"));
                                    String isCheckPushNoticeEnabled = EscapeUtils.CheckTextNull(request.getParameter("isCheckPushNoticeEnabled"));
                                    String isCheckP12EmailEnabled = EscapeUtils.CheckTextNull(request.getParameter("isCheckP12EmailEnabled"));
//                                    String isCheckShareCertEnabled = EscapeUtils.CheckTextNull(request.getParameter("isCheckShareCertEnabled"));
                                    String sProfileApproveCAUser = EscapeUtils.CheckTextNull(request.getParameter("sProfileApproveCAUser"));
                                    String sProfileBeneficiacyUser = EscapeUtils.CheckTextNull(request.getParameter("sProfileBeneficiacyUser"));
                                    if("1".equals(isCheckProfileAll))
                                    {
                                        isCheckProfileAll = "true";
                                    } else {
                                        isCheckProfileAll = "false";
                                    }
                                    if("1".equals(isCheckProfieAutoApprove))
                                    {
                                        isCheckProfieAutoApprove = "true";
                                    } else {
                                        isCheckProfieAutoApprove = "false";
                                    }
                                    if("1".equals(isCheckPushNoticeEnabled))
                                    {
                                        isCheckPushNoticeEnabled = "true";
                                    } else {
                                        isCheckPushNoticeEnabled = "false";
                                    }
                                    if("1".equals(isCheckP12EmailEnabled))
                                    {
                                        isCheckP12EmailEnabled = "true";
                                    } else {
                                        isCheckP12EmailEnabled = "false";
                                    }
//                                    if("1".equals(isCheckShareCertEnabled))
//                                    {
//                                        isCheckShareCertEnabled = "true";
//                                    } else {
//                                        isCheckShareCertEnabled = "false";
//                                    }
                                    CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfilePolicyAll");
                                    if(resProfileData[0].length > 0)
                                    {
                                        for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                        {
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_APPROVED_ENABLED))
                                            {
                                                resProfileData1.name = isCheckProfieAutoApprove;
                                            }
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS))
                                            {
                                                resProfileData1.name = isCheckProfileAll;
                                            }
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PUSH_NOTICE_ENABLED))
                                            {
                                                resProfileData1.name = isCheckPushNoticeEnabled;
                                            }
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_P12_EMAIL_ENABLED))
                                            {
                                                resProfileData1.name = isCheckP12EmailEnabled;
                                            }
//                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE))
//                                            {
//                                                resProfileData1.name = isCheckShareCertEnabled;
//                                            }
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_BENEFICIACY_USER))
                                            {
                                                resProfileData1.name = sProfileBeneficiacyUser;
                                            }
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_APPROVE_CA_USER))
                                            {
                                                resProfileData1.name = sProfileApproveCAUser;
                                            }
                                        }
                                        request.getSession(false).setAttribute("SessProfilePolicyAll", resProfileData);
                                        String sJSON_POLICY = CommonFunction.renderProfilePolicyAPI(resProfileData[0]);// oMapperParse.writeValueAsString(resProfileData[0]);
    //                                    CommonFunction.LogDebugString(log, "JSON_POLICY_OTHER", sJSON_POLICY);
                                        String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "", sJSON_POLICY, "", "", loginUID,"");
                                        if ("0".equals(param1)) {
                                            strView = "0#0";
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            // IP Access
                            case "activeippolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="activeippolicybranch">
                                String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                String sIPActive = EscapeUtils.CheckTextNull(request.getParameter("sIPActive"));
                                String sIPCode = EscapeUtils.CheckTextNull(request.getParameter("sIPCode"));
                                boolean booActive = false;
                                if("1".equals(sIPActive))
                                {
                                    booActive = true;
                                }
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessIPPolicyAll");
                                if(resProfileData[0].length > 0)
                                {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_IP_LIST))
                                        {
                                            if(resProfileData1.name.equals(sIPCode))
                                            {
                                                resProfileData1.enabled = booActive;
                                            }
                                        }
                                    }
                                    request.getSession(false).setAttribute("SessIPPolicyAll", resProfileData);
                                    String sJSON_POLICY = CommonFunction.renderIPPolicyAPI(resProfileData[0]);
//                                    CommonFunction.LogDebugString(log, "JSON_POLICY_IP", sJSON_POLICY);
                                    String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "", "", sJSON_POLICY, "", loginUID,"");
                                    if ("0".equals(param1)) {
                                        strView = "0#0";
                                    } else {
                                        strView = param1 + "#" + param1;
                                    }
                                }
                                //</editor-fold>
                                break;
                            }
                            case "addippolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="addippolicybranch">
                                String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                String sIPCert = EscapeUtils.CheckTextNull(request.getParameter("sIPCert"));
                                if(!"".equals(sIPCert))
                                {
                                    boolean isValid = true;
                                    CERTIFICATION_POLICY_DATA[][] reProfileDataLast = new CERTIFICATION_POLICY_DATA[1][];
                                    CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessIPPolicyAll");
                                    if(resProfileData[0].length > 0)
                                    {
                                        for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                        {
                                            if(resProfileData1.name.equals(sIPCert))
                                            {
                                                isValid = false;
                                                break;
                                            }
                                        }
                                    }
                                    if(isValid == true)
                                    {
                                        if(resProfileData[0].length > 0)
                                        {
                                            ArrayList<CERTIFICATION_POLICY_DATA> tempList;
                                            tempList = new ArrayList<>();
                                            tempList.addAll(Arrays.asList(resProfileData[0]));
                                            CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                                            rsItem.name = sIPCert;
                                            rsItem.remark = "";
                                            rsItem.remarkEn = "";
                                            rsItem.enabled = true;
                                            rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_IP_LIST;
                                            tempList.add(rsItem);
                                            reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                            reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                        }
                                        if(reProfileDataLast[0].length > 0)
                                        {
                                            request.getSession(false).setAttribute("SessIPPolicyAll", reProfileDataLast);
                                            String sJSON_POLICY = CommonFunction.renderIPPolicyAPI(reProfileDataLast[0]);
//                                            CommonFunction.LogDebugString(log, "JSON_POLICY_IP", sJSON_POLICY);
                                            String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "","",sJSON_POLICY,"", loginUID,"");
                                            if ("0".equals(param1)) {
                                                strView = "0#0";
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        }
                                    } else {
                                        strView = "IP_EXISTS#0";
                                    }
                                }
                                //</editor-fold>
                                break;
                            }
                            case "activeipallpolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="activeipallpolicybranch">
                                String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                String sCheckIPAll = EscapeUtils.CheckTextNull(request.getParameter("sCheckIPAll"));
                                if("1".equals(sCheckIPAll))
                                {
                                    sCheckIPAll = "true";
                                } else {
                                    sCheckIPAll = "false";
                                }
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessIPPolicyAll");
                                if(resProfileData[0].length > 0)
                                {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_IP_ALL_ACCESS))
                                        {
                                            resProfileData1.name = sCheckIPAll;
                                            break;
                                        }
                                    }
                                    request.getSession(false).setAttribute("SessIPPolicyAll", resProfileData);
                                    String sJSON_POLICY = CommonFunction.renderIPPolicyAPI(resProfileData[0]);
//                                    CommonFunction.LogDebugString(log, "JSON_POLICY_IP", sJSON_POLICY);
                                    String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "","",sJSON_POLICY,"", loginUID, "");
                                    if ("0".equals(param1)) {
                                        strView = "0#0";
                                    } else {
                                        strView = param1 + "#" + param1;
                                    }
                                }
                                //</editor-fold>
                                break;
                            }
                            // Function Access
                            case "activefunctionpolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="activefunctionpolicybranch">
                                String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                String sFunctionActive = EscapeUtils.CheckTextNull(request.getParameter("sFunctionActive"));
                                String sFunctionCode = EscapeUtils.CheckTextNull(request.getParameter("sFunctionCode"));
                                boolean booActive = false;
                                if("1".equals(sFunctionActive))
                                {
                                    booActive = true;
                                }
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessFunctionPolicyAll");
                                if(resProfileData[0].length > 0)
                                {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_FUNCTION_LIST))
                                        {
                                            if(resProfileData1.name.equals(sFunctionCode))
                                            {
                                                resProfileData1.enabled = booActive;
                                            }
                                        }
                                    }
                                    request.getSession(false).setAttribute("SessFunctionPolicyAll", resProfileData);
                                    String sJSON_POLICY = CommonFunction.renderFunctionPPolicyAPI(resProfileData[0]);
//                                    CommonFunction.LogDebugString(log, "JSON_POLICY_FUNCTION", sJSON_POLICY);
                                    String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "", "", "", sJSON_POLICY, loginUID,"");
                                    if ("0".equals(param1)) {
                                        strView = "0#0";
                                    } else {
                                        strView = param1 + "#" + param1;
                                    }
                                }
                                //</editor-fold>
                                break;
                            }
                            case "addfunctionpolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="addfunctionpolicybranch">
                                String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                String sFunctionCert = EscapeUtils.CheckTextNull(request.getParameter("sFunctionCert"));
                                if(!"".equals(sFunctionCert))
                                {
                                    boolean isValid = true;
                                    CERTIFICATION_POLICY_DATA[][] reProfileDataLast = new CERTIFICATION_POLICY_DATA[1][];
                                    CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessFunctionPolicyAll");
                                    if(resProfileData[0].length > 0)
                                    {
                                        for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                        {
                                            if(resProfileData1.name.equals(sFunctionCert))
                                            {
                                                isValid = false;
                                                break;
                                            }
                                        }
                                    }
                                    if(isValid == true)
                                    {
                                        if(resProfileData[0].length > 0)
                                        {
                                            ArrayList<CERTIFICATION_POLICY_DATA> tempList;
                                            tempList = new ArrayList<>();
                                            tempList.addAll(Arrays.asList(resProfileData[0]));
                                            CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                                            rsItem.name = sFunctionCert;
                                            rsItem.remark = "";
                                            rsItem.remarkEn = "";
                                            rsItem.enabled = true;
                                            rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_FUNCTION_LIST;
                                            tempList.add(rsItem);
                                            reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                            reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                        }
                                        if(reProfileDataLast[0].length > 0)
                                        {
                                            request.getSession(false).setAttribute("SessFunctionPolicyAll", reProfileDataLast);
                                            String sJSON_POLICY = CommonFunction.renderFunctionPPolicyAPI(reProfileDataLast[0]);
//                                            CommonFunction.LogDebugString(log, "JSON_POLICY_FUNCTION", sJSON_POLICY);
                                            String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "","","",sJSON_POLICY, loginUID,"");
                                            if ("0".equals(param1)) {
                                                strView = "0#0";
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        }
                                    } else {
                                        strView = "FUNCTION_EXISTS#0";
                                    }
                                }
                                //</editor-fold>
                                break;
                            }
                            case "activefunctionallpolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="activefunctionallpolicybranch">
                                String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                String sCheckFunctionAll = EscapeUtils.CheckTextNull(request.getParameter("sCheckFunctionAll"));
                                if("1".equals(sCheckFunctionAll))
                                {
                                    sCheckFunctionAll = "true";
                                } else {
                                    sCheckFunctionAll = "false";
                                }
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessFunctionPolicyAll");
                                if(resProfileData[0].length > 0)
                                {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_FUNCTIONALITY_ALL_ACCESS))
                                        {
                                            resProfileData1.name = sCheckFunctionAll;
                                            break;
                                        }
                                    }
                                    request.getSession(false).setAttribute("SessFunctionPolicyAll", resProfileData);
                                    String sJSON_POLICY = CommonFunction.renderFunctionPPolicyAPI(resProfileData[0]);
//                                    CommonFunction.LogDebugString(log, "JSON_POLICY_FUNCTION", sJSON_POLICY);
                                    String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "","","",sJSON_POLICY, loginUID,"");
                                    if ("0".equals(param1)) {
                                        strView = "0#0";
                                    } else {
                                        strView = param1 + "#" + param1;
                                    }
                                }
                                //</editor-fold>
                                break;
                            }
                            //
                        }
                    }
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "#0";
                }
            } catch (NumberFormatException | SQLException e) {
                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#" + e.getMessage();
            } finally {
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
