/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRFile;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.FILE_PROFILE_DATA;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.ProfileActionInfoJson;
import vn.ra.object.ProfileContactInfoJson;
import vn.ra.process.CommonReferServlet;
import vn.ra.process.ConnectDbPhaseTwo;
import vn.ra.process.ConnectFileToPartner;
import vn.ra.process.ConnectJackRabbitNew;
import vn.ra.process.JackRabbitCommon;
import vn.ra.process.SessionUploadFileCert;
import vn.ra.utility.LoadParamSystem;
import vn.ra.utility.PropertiesContent;

/**
 *
 * @author USER
 */
public class ProfileCommon extends HttpServlet {
private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ProfileCommon.class);
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
                            case "editprofile": {
                                //<editor-fold defaultstate="collapsed" desc="editprofile">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String idCert = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    String idOwner = EscapeUtils.escapeHtml(request.getParameter("idOwner"));
                                    String idReceivedDate = EscapeUtils.escapeHtml(request.getParameter("idReceivedDate"));
                                    String idReceivedNote = EscapeUtils.escapeHtml(request.getParameter("idReceivedNote"));
                                    String idCheckEnough = EscapeUtils.escapeHtml(request.getParameter("idCheckEnough"));
                                    String idCheckProfileType = EscapeUtils.escapeHtml(request.getParameter("idCheckProfileType"));
                                    String pEMAIL_CONTRACT = EscapeUtils.escapeHtml(request.getParameter("EMAIL_CONTRACT"));
                                    String pPHONE_CONTRACT = EscapeUtils.escapeHtml(request.getParameter("PHONE_CONTRACT"));
                                    String pFEE_AMOUNT = EscapeUtils.escapeHtml(request.getParameter("FEE_AMOUNT"));
                                    String pTOKEN_AMOUNT = EscapeUtils.escapeHtml(request.getParameter("TOKEN_AMOUNT"));
                                    String pFINE_FOR_LACK_OF_BRIEF = EscapeUtils.escapeHtml(request.getParameter("FINE_FOR_LACK_OF_BRIEF"));
                                    String pCheckReceivedSoftCopy = EscapeUtils.escapeHtml(request.getParameter("sCheckReceivedSoftCopy"));
                                    String idReceivedEmailManager = EscapeUtils.escapeHtml(request.getParameter("idReceivedEmailManager"));
                                    String idReceivedPhoneManager = EscapeUtils.escapeHtml(request.getParameter("idReceivedPhoneManager"));
                                    String idReceivedAddressManager = EscapeUtils.escapeHtml(request.getParameter("idReceivedAddressManager"));
                                    String idReceivedNameManager = EscapeUtils.escapeHtml(request.getParameter("idReceivedNameManager"));
                                    String idReceivedNameContact = EscapeUtils.escapeHtml(request.getParameter("idReceivedNameContact"));
                                    String idReceivedPositionManager = EscapeUtils.escapeHtml(request.getParameter("idReceivedPositionManager"));
                                    String pSTATE_PROFILE = EscapeUtils.escapeHtml(request.getParameter("STATE_PROFILE"));
                                    String idReceivedReadFile = EscapeUtils.escapeHtml(request.getParameter("idReceivedReadFile"));
                                    String registerCMND = EscapeUtils.CheckTextNull(request.getParameter("registerCMND"));
                                    String registerIssuedDate = EscapeUtils.CheckTextNull(request.getParameter("registerIssuedDate"));
                                    String registerIssuedPlace = EscapeUtils.CheckTextNull(request.getParameter("registerIssuedPlace"));
                                    String registerAddressGPKD = EscapeUtils.CheckTextNull(request.getParameter("registerAddressGPKD"));
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### FILE PROCESS">
                                    String sJRBConfig = "";
                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy[0].length > 0) {
                                        for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT))
                                            {
                                                sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                    String sJRB_Source =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
                                    if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY))
                                    {
                                        SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCertProfile");
                                        if (cartToken != null && cartToken.getGH().size() > 0) {
                                            String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
                                            String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
                                            String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
                                            String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
                                            String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
                                            String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
                                            String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
                                            String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
                                            String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_UP);
                                            String idUUID_Temp = Definitions.CONFIG_JACK_RABBIT_UUID_SAMPLE;
                                            ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                            for (FILE_PROFILE_DATA mhIP : ds) {
                                                if(mhIP.FILE_MANAGER_ID == 0)
                                                {
                                                    String sFileData = EscapeUtils.CheckTextNull(mhIP.FILE_URL);
                                                    CloseableHttpResponse pHttpRes = ConnectFileToPartner.upFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                                        sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                                        sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, idUUID_Temp,
                                                        mhIP.FILE_NAME, sFileData);
                                                    InputStream isStr = pHttpRes.getEntity().getContent();
                                                    String resultUUID = IOUtils.toString(isStr);
                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                    com.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, resultUUID, sJRBConfig,
                                                        EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE), mhIP.FILE_NAME,
                                                        (int) mhIP.FILE_SIZE, idCert, idOwner, loginUID, pFILE_MANAGER_ID);
                                                }
                                            }
                                            request.getSession(false).setAttribute("sessUploadFileCertProfile", null);
                                        }
                                    } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
                                        String sJRB_Host =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                        String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                        String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                        String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                        String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                        String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                        String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                        SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCertProfile");
                                        if (cartToken != null && cartToken.getGH().size() > 0) {
                                            ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                            for (FILE_PROFILE_DATA mhIP : ds) {
                                                if(mhIP.FILE_MANAGER_ID == 0)
                                                {
                                                    JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                                        Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                    InputStream isFILE_STREAM = new ByteArrayInputStream(mhIP.FILE_STREAM);
                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                    JackRabbitCommon.getInstance(jcrConfig).uploadFileThread(mhIP.FILE_NAME, mhIP.FILE_MIMETYPE, isFILE_STREAM, mhIP, sJRBConfig,
                                                        idCert, idOwner, loginUID, pFILE_MANAGER_ID);
//                                                    JCRFile jrbFile = JackRabbitCommon.getInstance(jcrConfig).uploadFile(mhIP.FILE_NAME, mhIP.FILE_MIMETYPE, isFILE_STREAM);
//                                                    if(jrbFile != null){
//                                                        int[] pFILE_MANAGER_ID = new int[1];
//                                                        com.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, jrbFile.getUuid(), sJRBConfig, EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE),
//                                                            jrbFile.getFileName(), (int) mhIP.FILE_SIZE, idCert, idOwner, loginUID, pFILE_MANAGER_ID);
//                                                    }
                                                }
                                            }
                                            request.getSession(false).setAttribute("sessUploadFileCertProfile", null);
                                        }
                                    } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
                                        String sJRB_Host =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                        String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                        String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                        String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                        String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                        String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                        String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                        SessionUploadFileCert cartToken = (SessionUploadFileCert) request.getSession(false).getAttribute("sessUploadFileCertProfile");
                                        if (cartToken != null && cartToken.getGH().size() > 0) {
                                            ArrayList<FILE_PROFILE_DATA> ds = cartToken.getGH();
                                            for (FILE_PROFILE_DATA mhIP : ds) {
                                                if(mhIP.FILE_MANAGER_ID == 0)
                                                {
                                                    ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                                                        Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                    InputStream isFILE_STREAM = new ByteArrayInputStream(mhIP.FILE_STREAM);
                                                    String[] sReturnJRB = new String[2];
                                                    vn.mobileid.fms.client.JCRFile jrbFile = openJRB.uploadFile(EscapeUtils.CheckTextNull(mhIP.FILE_NAME),
                                                        EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE), isFILE_STREAM, sReturnJRB);
                                                    int[] pFILE_MANAGER_ID = new int[1];
                                                    com.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, sReturnJRB[0].trim(), sJRBConfig, EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE),
                                                        sReturnJRB[1].trim(), (int) mhIP.FILE_SIZE, idCert, idOwner, loginUID, pFILE_MANAGER_ID);
                                                }
                                            }
                                            request.getSession(false).setAttribute("sessUploadFileCertProfile", null);
                                        }
                                    } else {
                                    }
                                    
                                    //</editor-fold>
                                    
                                    java.sql.Timestamp idReceivedDateDB = null;
                                    idReceivedDateDB = CommonFunction.ConvertStringToDateDDMMYY(idReceivedDate);
                                    String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                    if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                        ProfileContactInfoJson profileContact = new ProfileContactInfoJson();
                                        profileContact.RepresentativeName = CommonFunction.replaceCharaterSpecialJson(idReceivedNameManager, true);
                                        profileContact.RepresentativePhone = idReceivedPhoneManager;
                                        profileContact.RepresentativeEmail = idReceivedEmailManager;
                                        profileContact.ContactName = CommonFunction.replaceCharaterSpecialJson(idReceivedNameContact, true);
                                        profileContact.Position = CommonFunction.replaceCharaterSpecialJson(idReceivedPositionManager, true);
                                        profileContact.Address = CommonFunction.replaceCharaterSpecialJson(idReceivedAddressManager, true);
                                        profileContact.AddressLicense = CommonFunction.replaceCharaterSpecialJson(registerAddressGPKD, true);
                                        profileContact.PIDIssuedBy = CommonFunction.replaceCharaterSpecialJson(registerIssuedPlace, true);
                                        profileContact.PIDDate = registerIssuedDate;
                                        profileContact.PID = registerCMND;
                                        ObjectMapper oMapperParse;
                                        oMapperParse = new ObjectMapper();
                                        CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessCollectedBriefPro");
                                        String pBRIEF_PROPERTIES = "";
                                        if(resProfileData != null && resProfileData[0].length > 0){
                                            pBRIEF_PROPERTIES = CommonFunction.renderBriefFileType(resProfileData[0], "", idReceivedNote, "");
                                        }
                                        String param1 = com.S_BO_CERTIFICATION_BRIEF_UPDATE(idCert, EscapeUtils.escapeHtml(idCheckProfileType), pBRIEF_PROPERTIES,
                                            EscapeUtils.escapeHtml(idCheckEnough), pEMAIL_CONTRACT, pPHONE_CONTRACT, pFEE_AMOUNT.replace(",", ""),
                                            pTOKEN_AMOUNT.replace(",", ""), pFINE_FOR_LACK_OF_BRIEF.replace(",", ""), loginUID, pCheckReceivedSoftCopy,
                                            idReceivedAddressManager, idReceivedDateDB, idReceivedEmailManager, idReceivedPhoneManager,
                                            idReceivedNameManager, idReceivedNameContact, pSTATE_PROFILE, oMapperParse.writeValueAsString(profileContact));
                                        if ("0".equals(param1)) {
                                            String loginID = request.getSession(false).getAttribute("UserID").toString().trim();
                                            String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                            if("1".equals(idCheckEnough) && isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_CMC)){
                                                oMapperParse = new ObjectMapper();
                                                String loginFullname = request.getSession(false).getAttribute("sesFullname").toString().trim();
                                                ProfileActionInfoJson profileAction = new ProfileActionInfoJson();
                                                profileAction.controlTime = new Date();
                                                profileAction.controlUser = loginFullname + " (" + loginUID + ")";
                                                ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                dbTwo.S_BO_CERTIFICATION_UPDATE_INFO_BRIEF(Integer.parseInt(idCert), oMapperParse.writeValueAsString(profileAction), loginID);
                                            }
                                            com.S_BO_BRIEF_CERTIFICATION_UPDATE_PROFILE_NOTE(Integer.parseInt(idCert), idReceivedNote, loginID);
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && request.getSession(false).getAttribute("sessProfileStateDK01") != null) {
                                                ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                String profileStateDK01 = request.getSession(false).getAttribute("sessProfileStateDK01").toString().trim();
                                                dbTwo.S_BO_CERTIFICATION_BRIEF_UPDATE_BUSINESS_LICENSE_TYPE_ID(Integer.parseInt(idCert), Integer.parseInt(profileStateDK01), Integer.parseInt(loginID));
                                            }
                                            strView = "0#0";
                                            request.getSession(false).setAttribute("RefreshProfileCertSess", "1");
                                            if("1".equals(idReceivedReadFile)) {
                                                com.S_BO_FILE_MANAGER_UPDATE_COMMIT_ENABLED(idCert, loginUID);
                                            }
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    } else {
                                        strView = "0#0";
                                        request.getSession(false).setAttribute("RefreshProfileCertSess", "1");
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "editcontrolsprofile": {
                                //<editor-fold defaultstate="collapsed" desc="editcontrolsprofile">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String idCert = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                    if (Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                        int param1 = com.S_BO_CERTIFICATION_BRIEF_UPDATE_COLLECT_ENABLED(Integer.parseInt(idCert), "0", loginUID);
                                        if (param1 == 0) {
                                            strView = "0#0";
                                            request.getSession(false).setAttribute("RefreshProfileCertSess", "1");
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    } else {
                                        strView = "0#0";
                                        request.getSession(false).setAttribute("RefreshProfileCertSess", "1");
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "edittypeprofile": {
                                //<editor-fold defaultstate="collapsed" desc="edittypeprofile">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                String sSum = EscapeUtils.escapeHtml(request.getParameter("vSum"));
                                String ToCreateDate = (String) request.getSession(false).getAttribute("sessMonthProfileCert");
                                String FromCreateDate = (String) request.getSession(false).getAttribute("sessYearProfileCert");
                                String PERSONAL_NAME = (String) request.getSession(false).getAttribute("sessPERSONAL_NAMEProfileCert");
                                String COMPANY_NAME = (String) request.getSession(false).getAttribute("sessCOMPANY_NAMEProfileCert");
                                String TAX_CODE = (String) request.getSession(false).getAttribute("sessTAX_CODEProfileCert");
                                String BUDGET_CODE = (String) request.getSession(false).getAttribute("sessBUDGET_CODEProfileCert");
                                String DECISION = (String) request.getSession(false).getAttribute("sessDECISIONProfileCert");
                                String P_ID = (String) request.getSession(false).getAttribute("sessP_IDProfileCert");
                                String CCCD = (String) request.getSession(false).getAttribute("sessCCCDProfileCert");
                                String PASSPORT = (String) request.getSession(false).getAttribute("sessPASSPORTProfileCert");
                                String idCollectEnabled = (String) request.getSession(false).getAttribute("sessCollectEnabledProfileCert");
                                String idCommitEnabled = (String) request.getSession(false).getAttribute("sessCommitEnabledCert");
                                String BranchOffice = (String) request.getSession(false).getAttribute("sessBranchOfficeProfileCert");
                                String sConfirmResign = (String) request.getSession(false).getAttribute("sessResignProfileCert");
                                String SessUserID = request.getSession(false).getAttribute("UserID").toString().trim();
                                String SessRoleID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idCollectEnabled)) {
                                    idCollectEnabled = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                    BranchOffice = "";
                                }
                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    SessUserID = "";
                                } else {
                                    if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                        SessUserID = "";
                                    }
                                }
                                if(!"0".equals(sSum) && !"".equals(sSum)) {
                                    String[] sUIDResult = new String[2];
                                    CommonReferServlet.collectFieldToUID(TAX_CODE, BUDGET_CODE, DECISION, P_ID, PASSPORT, CCCD, sUIDResult);
                                    String sEnterpriseCert = sUIDResult[0];
                                    String sPersonalCert = sUIDResult[1];
                                    String param1 = com.S_BO_CERTIFICATION_BRIEF_UPDATE_BRIEF_TYPE(COMPANY_NAME, TAX_CODE, BUDGET_CODE, PERSONAL_NAME,
                                        P_ID, PASSPORT, idCollectEnabled, EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                        EscapeUtils.escapeHtmlSearch(FromCreateDate), BranchOffice, loginUID, idCommitEnabled, DECISION, CCCD,
                                        sConfirmResign, sEnterpriseCert, sPersonalCert);
                                    if ("0".equals(param1)) {
                                        int[] pREMAINING_BEGINNING_MONTH = new int[1];
                                        int[] pBRIEF_IN_MONTH = new int[1];
                                        int[] pBRIEF_LACK_IN_MONTH_NO = new int[1];
                                        int[] pBRIEF_COMPENSATE_IN_MONTH_NO = new int[1];
                                        int[] pOVER_TIME_BRIEF_NO = new int[1];
                                        int[] pREMAINING_END_MONTH = new int[1];
                                        com.S_BO_CERTIFICATION_BRIEF_REPORT(BranchOffice, EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(FromCreateDate), SessUserID, pREMAINING_BEGINNING_MONTH, pBRIEF_IN_MONTH, pBRIEF_LACK_IN_MONTH_NO,
                                            pBRIEF_COMPENSATE_IN_MONTH_NO, pOVER_TIME_BRIEF_NO, pREMAINING_END_MONTH);
                                        strView = "0#" + pBRIEF_LACK_IN_MONTH_NO[0] + "#" + pBRIEF_COMPENSATE_IN_MONTH_NO[0] + "#" + pOVER_TIME_BRIEF_NO[0];
                                        request.getSession(false).setAttribute("RefreshProfileCertSess", "1");
                                    } else {
                                        strView = param1 + "#" + param1;
                                    }
                                } else {
                                    strView = "1#0";
                                }
//                                } else {
//                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
//                                }
                                //</editor-fold>
                                break;
                            }
                            case "checkenoughtypeprofile": {
                                //<editor-fold defaultstate="collapsed" desc="checkenoughtypeprofile">
                                String sChecked = EscapeUtils.CheckTextNull(request.getParameter("sChecked"));
                                String sName = EscapeUtils.CheckTextNull(request.getParameter("sName"));
                                boolean booChecked = false;
                                if("1".equals(sChecked))
                                {
                                    booChecked = true;
                                }
                                boolean isHasFileType = false;
                                CERTIFICATION_POLICY_DATA[][] reProfileDataLast = new CERTIFICATION_POLICY_DATA[1][];
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessCollectedBriefPro");
                                if(resProfileData!= null && resProfileData[0].length > 0)
                                {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_TYPE))
                                        {
                                            if(resProfileData1.name.equals(sName))
                                            {
                                                resProfileData1.enabled = booChecked;
                                                isHasFileType = true;
                                            }
                                        }
                                    }
                                    if(isHasFileType == false)
                                    {
                                        if(resProfileData[0].length > 0)
                                        {
                                            ArrayList<CERTIFICATION_POLICY_DATA> tempList;
                                            tempList = new ArrayList<>();
                                            tempList.addAll(Arrays.asList(resProfileData[0]));
                                            CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                                            rsItem.name = sName;
                                            rsItem.enabled = true;
                                            rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_TYPE;
                                            tempList.add(rsItem);
                                            reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                            reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                            request.getSession(false).setAttribute("SessCollectedBriefPro", reProfileDataLast);
                                        }
                                    } else {
                                        request.getSession(false).setAttribute("SessCollectedBriefPro", resProfileData);
                                    }
                                    strView = "0#0";
                                } else {
                                    ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
                                    CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                                    rsItem.name = sName;
                                    rsItem.enabled = true;
                                    rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_TYPE;
                                    tempList.add(rsItem);
                                    reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                    reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                    request.getSession(false).setAttribute("SessCollectedBriefPro", reProfileDataLast);
                                    strView = "0#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "checkscantypeprofile": {
                                //<editor-fold defaultstate="collapsed" desc="checkscantypeprofile">
                                String sChecked = EscapeUtils.CheckTextNull(request.getParameter("sChecked"));
                                String sName = EscapeUtils.CheckTextNull(request.getParameter("sName"));
                                boolean booChecked = false;
                                if("1".equals(sChecked)) {
                                    booChecked = true;
                                }
                                boolean isHasFileType = false;
                                CERTIFICATION_POLICY_DATA[][] reProfileDataLast = new CERTIFICATION_POLICY_DATA[1][];
                                CERTIFICATION_POLICY_DATA[][] resProfileData = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessCollectedBriefPro");
                                if(resProfileData != null && resProfileData[0].length > 0) {
                                    for(CERTIFICATION_POLICY_DATA resProfileData1 : resProfileData[0])
                                    {
                                        if(resProfileData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_SCAN))
                                        {
                                            if(resProfileData1.name.equals(sName))
                                            {
                                                resProfileData1.enabled = booChecked;
                                                isHasFileType = true;
                                            }
                                        }
                                    }
                                    if(isHasFileType == false)
                                    {
                                        if(resProfileData[0].length > 0)
                                        {
                                            ArrayList<CERTIFICATION_POLICY_DATA> tempList;
                                            tempList = new ArrayList<>();
                                            tempList.addAll(Arrays.asList(resProfileData[0]));
                                            CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                                            rsItem.name = sName;
                                            rsItem.enabled = true;
                                            rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_SCAN;
                                            tempList.add(rsItem);
                                            reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                            reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                            request.getSession(false).setAttribute("SessCollectedBriefPro", reProfileDataLast);
                                        }
                                    } else {
                                        request.getSession(false).setAttribute("SessCollectedBriefPro", resProfileData);
                                    }
                                    strView = "0#0";
                                } else {
                                    ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
                                    CERTIFICATION_POLICY_DATA rsItem = new CERTIFICATION_POLICY_DATA();
                                    rsItem.name = sName;
                                    rsItem.enabled = true;
                                    rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_SCAN;
                                    tempList.add(rsItem);
                                    reProfileDataLast[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
                                    reProfileDataLast[0] = tempList.toArray(reProfileDataLast[0]);
                                    request.getSession(false).setAttribute("SessCollectedBriefPro", reProfileDataLast);
                                    strView = "0#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "checkprofilestatedk01": {
                                //<editor-fold defaultstate="collapsed" desc="checkprofilestatedk01">
                                String sChecked = EscapeUtils.CheckTextNull(request.getParameter("sChecked"));
                                String sName = EscapeUtils.CheckTextNull(request.getParameter("sName"));
                                int iBrefType = 0;
                                if(sName.equals(Definitions.CONFIG_BRIEF_TYPE_CODE_SCAN)) {
                                    if("1".equals(sChecked)) {
                                        iBrefType = Definitions.CONFIG_BRIEF_TYPE_ID_SCAN;
                                    }
                                }
                                if(sName.equals(Definitions.CONFIG_BRIEF_TYPE_CODE_DIGITAL_SIGNATURE)) {
                                    if("1".equals(sChecked)) {
                                        iBrefType = Definitions.CONFIG_BRIEF_TYPE_ID_DIGITAL_SIGNATURE;
                                    }
                                }
                                if(sName.equals(Definitions.CONFIG_BRIEF_TYPE_CODE_PAPER)) {
                                    if("1".equals(sChecked)) {
                                        iBrefType = Definitions.CONFIG_BRIEF_TYPE_ID_PAPER;
                                    }
                                }
                                request.getSession(false).setAttribute("sessProfileStateDK01", String.valueOf(iBrefType));
                                strView = "0#0";
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
