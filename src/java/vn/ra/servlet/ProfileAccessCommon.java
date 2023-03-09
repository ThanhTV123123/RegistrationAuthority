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
import vn.ra.object.ROLE;
import vn.ra.object.SOAPSecureProperties;
import vn.ra.process.ConnectConnector;
import vn.ra.utility.Config;

/**
 *
 * @author USER
 */
public class ProfileAccessCommon extends HttpServlet {
private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ProfileAccessCommon.class);
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
                if (sOutInner == 1) {
                    String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    String idParam = request.getParameter("idParam");
                    if (null != idParam) {
                        switch (idParam) {
                            case "editprofileaccess": {
                                //<editor-fold defaultstate="collapsed" desc="editprofileaccess">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ids = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    String Remark = EscapeUtils.escapeHtml(request.getParameter("Remark"));
                                    String Remark_EN = EscapeUtils.escapeHtml(request.getParameter("Remark_EN"));
                                    String ActiveFlag = EscapeUtils.escapeHtml(request.getParameter("ActiveFlag"));
                                    String APPLY_ALL = EscapeUtils.escapeHtml(request.getParameter("APPLY_ALL"));
                                    String ACCESS_PROFILE_ALL = EscapeUtils.escapeHtml(request.getParameter("ACCESS_PROFILE_ALL"));
                                    boolean approveCAProfileEnabled = false;
                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy[0].length > 0) {
                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                        {
                                            if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_AUTO_APPROVED_FOR_EACH_CERTIFICATION_PROFILE_OPTION))
                                            {
                                                if("1".equals(EscapeUtils.CheckTextNull(rsPolicy1.VALUE))) {
                                                    approveCAProfileEnabled = true;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                    String sCERT_POLICY_PROPERTIES = "";
                                    CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfileBranchAccess");
                                    if(resProfileData != null && resProfileData[0] != null && resProfileData[0].length > 0) {
                                        for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                        {
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS)) {
                                                if("1".equals(ACCESS_PROFILE_ALL)) {
                                                    resProfileData1.name = "true";
                                                } else {
                                                    resProfileData1.name = "false";
                                                }
                                            }
                                        }
                                        sCERT_POLICY_PROPERTIES = CommonFunction.renderProfilePolicyForBranchRole(approveCAProfileEnabled, resProfileData[0]);
                                    }
                                    String param1 = com.S_BO_BRANCH_ROLE_UPDATE(ids,
                                            EscapeUtils.escapeHtml(Remark), EscapeUtils.escapeHtml(Remark_EN),
                                            EscapeUtils.escapeHtml(ActiveFlag), sCERT_POLICY_PROPERTIES,
                                            EscapeUtils.escapeHtml(APPLY_ALL), EscapeUtils.escapeHtml(loginUID));
                                    if ("0".equals(param1)) {
                                        strView = "0#0";
                                        request.getSession(false).setAttribute("SessProfileBranchAccess", null);
                                        request.getSession(false).setAttribute("SessRefreshBranchRole", "1");
                                    } else {
                                        strView = param1 + "#" + param1;
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "addprofileaccess": {
                                //<editor-fold defaultstate="collapsed" desc="addprofileaccess">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String Remark = EscapeUtils.escapeHtml(request.getParameter("Remark"));
                                    String Remark_EN = EscapeUtils.escapeHtml(request.getParameter("Remark_EN"));
                                    String GroupCode = EscapeUtils.escapeHtml(request.getParameter("GroupCode"));
                                    String ACCESS_PROFILE_ALL = EscapeUtils.escapeHtml(request.getParameter("ACCESS_PROFILE_ALL"));
                                    boolean approveCAProfileEnabled = false;
                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy[0].length > 0) {
                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
                                        {
                                            if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_AUTO_APPROVED_FOR_EACH_CERTIFICATION_PROFILE_OPTION))
                                            {
                                                if("1".equals(EscapeUtils.CheckTextNull(rsPolicy1.VALUE))) {
                                                    approveCAProfileEnabled = true;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                    String sCERT_POLICY_PROPERTIES = "";
                                    CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfileBranchAccess");
                                    if(resProfileData != null && resProfileData[0] != null && resProfileData[0].length > 0) {
                                        for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                        {
                                            if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS)) {
                                                if("1".equals(ACCESS_PROFILE_ALL)) {
                                                    resProfileData1.name = "true";
                                                } else {
                                                    resProfileData1.name = "false";
                                                }
                                            }
                                        }
                                        sCERT_POLICY_PROPERTIES = CommonFunction.renderProfilePolicyForBranchRole(approveCAProfileEnabled, resProfileData[0]);
                                    }
                                    String param1 = com.S_BO_BRANCH_ROLE_INSERT(GroupCode, EscapeUtils.escapeHtml(Remark),
                                        EscapeUtils.escapeHtml(Remark_EN), sCERT_POLICY_PROPERTIES, EscapeUtils.escapeHtml(loginUID));
                                    if ("0".equals(param1)) {
                                        request.getSession(false).setAttribute("SessProfileBranchAccess", null);
                                        strView = "0#0";
                                    } else {
                                        strView = param1 + "#" + param1;
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "activeprofileaccess": {
                                //<editor-fold defaultstate="collapsed" desc="activeprofileaccess">
//                                ObjectMapper oMapperParse;
                                String sName = EscapeUtils.CheckTextNull(request.getParameter("idName"));
                                String sProfileActive = EscapeUtils.CheckTextNull(request.getParameter("sProfileActive"));
                                boolean booProfileActive = false;
                                if("1".equals(sProfileActive))
                                {
                                    booProfileActive = true;
                                }
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfileBranchAccess");
                                if(resProfileData[0].length > 0)
                                {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                        {
                                            if(resProfileData1.name.equals(sName))
                                            {
                                                resProfileData1.enabled = booProfileActive;
                                            }
                                        }
                                    }
                                    request.getSession(false).setAttribute("SessProfileBranchAccess", resProfileData);
                                    strView = "0#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "activemajoraccess": {
                                //<editor-fold defaultstate="collapsed" desc="activemajoraccess">
                                String sName = EscapeUtils.CheckTextNull(request.getParameter("idName"));
                                String sProfileActive = EscapeUtils.CheckTextNull(request.getParameter("sProfileActive"));
                                boolean booProfileActive = false;
                                if("1".equals(sProfileActive))
                                {
                                    booProfileActive = true;
                                }
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfileBranchAccess");
                                if(resProfileData[0].length > 0)
                                {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_MAJOR_TYPE))
                                        {
                                            if(resProfileData1.name.equals(sName))
                                            {
                                                resProfileData1.enabled = booProfileActive;
                                            }
                                        }
                                    }
                                    request.getSession(false).setAttribute("SessProfileBranchAccess", resProfileData);
                                    strView = "0#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "activeapproveaccess": {
                                //<editor-fold defaultstate="collapsed" desc="activeapproveaccess">
                                String sName = EscapeUtils.CheckTextNull(request.getParameter("idName"));
                                String sProfileActive = EscapeUtils.CheckTextNull(request.getParameter("sProfileActive"));
                                boolean booProfileActive = false;
                                if("1".equals(sProfileActive)) {
                                    booProfileActive = true;
                                }
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessProfileBranchAccess");
                                if(resProfileData[0].length > 0)
                                {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST)
                                            || resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE))
                                        {
                                            if(resProfileData1.name.equals(sName))
                                            {
                                                resProfileData1.approveCAEnabled = booProfileActive;
                                            }
                                        }
                                    }
                                    request.getSession(false).setAttribute("SessProfileBranchAccess", resProfileData);
                                    strView = "0#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "addfactorpolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="addfactorpolicybranch">
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
                                            CommonFunction.LogDebugString(log, "JSON_POLICY_FACTOR", sJSON_POLICY);
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
                                            CommonFunction.LogDebugString(log, "JSON_POLICY_FACTOR", sJSON_POLICY);
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
                            case "activeotherpolicybranch": {
                                //<editor-fold defaultstate="collapsed" desc="activeotherpolicybranch">
                                String sID = EscapeUtils.CheckTextNull(request.getParameter("sID"));
                                String isCheckProfileAll = EscapeUtils.CheckTextNull(request.getParameter("isCheckProfileAll"));
                                String isCheckProfieAutoApprove = EscapeUtils.CheckTextNull(request.getParameter("isCheckProfieAutoApprove"));
                                String isCheckPushNoticeEnabled = EscapeUtils.CheckTextNull(request.getParameter("isCheckPushNoticeEnabled"));
                                String isCheckP12EmailEnabled = EscapeUtils.CheckTextNull(request.getParameter("isCheckP12EmailEnabled"));
                                String isCheckShareCertEnabled = EscapeUtils.CheckTextNull(request.getParameter("isCheckShareCertEnabled"));
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
                                if("1".equals(isCheckShareCertEnabled))
                                {
                                    isCheckShareCertEnabled = "true";
                                } else {
                                    isCheckShareCertEnabled = "false";
                                }
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
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE))
                                        {
                                            resProfileData1.name = isCheckShareCertEnabled;
                                        }
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
                                    CommonFunction.LogDebugString(log, "JSON_POLICY_OTHER", sJSON_POLICY);
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
                                    CommonFunction.LogDebugString(log, "JSON_POLICY_IP", sJSON_POLICY);
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
                                            CommonFunction.LogDebugString(log, "JSON_POLICY_IP", sJSON_POLICY);
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
                                    CommonFunction.LogDebugString(log, "JSON_POLICY_IP", sJSON_POLICY);
                                    String param1 = com.S_BO_BRANCH_UPDATE_PROPERTIES(Integer.parseInt(sID), "","",sJSON_POLICY,"", loginUID,"");
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
                                    CommonFunction.LogDebugString(log, "JSON_POLICY_FUNCTION", sJSON_POLICY);
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
                                            CommonFunction.LogDebugString(log, "JSON_POLICY_FUNCTION", sJSON_POLICY);
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
                                    CommonFunction.LogDebugString(log, "JSON_POLICY_FUNCTION", sJSON_POLICY);
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
