/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.DISCOUNT_RATE_PROFILE;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PROFILE_DISCOUNT_RATE_DATA;

/**
 *
 * @author USER
 */
public class CollationCertCommon extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CollationCertCommon.class);
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
                    String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                    String idParam = request.getParameter("idParam");
                    if (null != idParam) {
                        switch (idParam) {
                            case "editcollationcert": {
                                //<editor-fold defaultstate="collapsed" desc="editcollationcert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    String isCheckAll = EscapeUtils.CheckTextNull(request.getParameter("isCheckAll"));
                                    String isCheckAll_Log = "1".equals(isCheckAll) ? "CheckAll" : "0".equals(isCheckAll) ? "CheckList" : "NoValue";
                                    CommonFunction.LogDebugString(log, "isCheckAll", isCheckAll_Log);
                                    CommonFunction.LogDebugString(log, "id list", id);
                                    ConnectDatabase db = new ConnectDatabase();
                                    if ("1".equals(isCheckAll)) {
                                        String ToCreateDate = (String) request.getSession(false).getAttribute("sessMonthStatusCollation");
                                        String FromCreateDate = (String) request.getSession(false).getAttribute("sessYearStatusCollation");
                                        String FromDate = (String) request.getSession(false).getAttribute("sessFromCreateDateCollation");
                                        String ToDate = (String) request.getSession(false).getAttribute("sessToCreateDateCollation");
                                        String STATUS_COLLATION = (String) request.getSession(false).getAttribute("sessStatusCollation");
                                        String idBranchOffice = (String) request.getSession(false).getAttribute("sessBranchOfficeStatusCollation");
                                        request.getSession(false).setAttribute("sessMonthStatusCollation", ToCreateDate);
                                        request.getSession(false).setAttribute("sessYearStatusCollation", FromCreateDate);
                                        request.getSession(false).setAttribute("sessStatusCollation", STATUS_COLLATION);
                                        request.getSession(false).setAttribute("sessBranchOfficeStatusCollation", idBranchOffice);
                                        String SessUserID = request.getSession(false).getAttribute("UserID").toString().trim();
                                        String SessRoleID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
                                        String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                        String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(STATUS_COLLATION)) {
                                            STATUS_COLLATION = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
                                            idBranchOffice = "";
                                        }
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            SessUserID = "";
                                        } else {
                                            if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                                SessUserID = "";
                                            }
                                        }
                                        String strAlertAllTimes = request.getSession(false).getAttribute("AlertAllTimeCertCollation").toString().trim();
                                        if("1".equals(strAlertAllTimes)) {
                                            FromDate = "";
                                            ToDate = "";
                                        } else{
                                            ToCreateDate = "";
                                            FromCreateDate = "";
                                        }
                                        String pBRANCH_BENEFICIARY_ID = SessUserAgentID;
                                        if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            pBRANCH_BENEFICIARY_ID=EscapeUtils.escapeHtmlSearch(idBranchOffice);
                                        }
                                        int ssToken = db.S_BO_CERTIFICATION_CROSS_CHECK_TOTAL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(FromCreateDate),EscapeUtils.escapeHtmlSearch(idBranchOffice),
                                            STATUS_COLLATION, SessUserID, pBRANCH_BENEFICIARY_ID, FromDate, ToDate);
                                        if (ssToken > 0) {
                                            CERTIFICATION[][] rsPginToken = new CERTIFICATION[1][];
                                            db.S_BO_CERTIFICATION_CROSS_CHECK_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                EscapeUtils.escapeHtmlSearch(idBranchOffice),STATUS_COLLATION, SessUserID,
                                                sessLanguage, rsPginToken, 0, ssToken, pBRANCH_BENEFICIARY_ID, FromDate, ToDate);
                                            if(rsPginToken[0].length > 0)
                                            {
                                                for(CERTIFICATION rsItem : rsPginToken[0])
                                                {
                                                    if(rsItem.CROSS_CHECK_ENABLED == false)
                                                    {
                                                        db.S_BO_CERTIFICATION_UPDATE_CROSS_CHECK_STATE(rsItem.ID, loginUID);
                                                    }
                                                }
                                                request.getSession(false).setAttribute("RefreshStatusCollationSess", "1");
                                                strView = "0#0";
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "#0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "#0";
                                        }
                                    } else {
                                        if (!"".equals(id)) {
                                            if (id.split(",").length > 0) {
                                                String[] sPlitValue = id.split(",");
                                                for (String sPlitValue1 : sPlitValue)
                                                {
                                                    String sID = sPlitValue1.replace(Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX, "");
                                                    if (!"".equals(sID)) {
                                                        CERTIFICATION[][] rsCertDetail = new CERTIFICATION[1][];
                                                        db.S_BO_CERTIFICATION_DETAIL(sID, sessLanguage, rsCertDetail);
                                                        if(rsCertDetail[0].length > 0) {
                                                            if(rsCertDetail[0][0].CROSS_CHECK_ENABLED == false)
                                                            {
                                                                db.S_BO_CERTIFICATION_UPDATE_CROSS_CHECK_STATE(rsCertDetail[0][0].ID, loginUID);
                                                            }
                                                        }
                                                    }
                                                }
                                                request.getSession(false).setAttribute("RefreshStatusCollationSess", "1");
                                                strView = "0#0";
                                            } else {
                                                strView = "2#1";
                                            }
                                        } else {
                                            strView = "2#1";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "updateroseagent": {
                                //<editor-fold defaultstate="collapsed" desc="updateroseagent">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    ConnectDatabase db = new ConnectDatabase();
                                    String ToCreateDate = (String) request.getSession(false).getAttribute("sessMonthStatusCollation");
                                    String FromCreateDate = (String) request.getSession(false).getAttribute("sessYearStatusCollation");
                                    String FromDate = (String) request.getSession(false).getAttribute("sessFromCreateDateCollation");
                                    String ToDate = (String) request.getSession(false).getAttribute("sessToCreateDateCollation");
                                    String idBranchOffice = (String) request.getSession(false).getAttribute("sessBranchOfficeStatusCollation");
                                    request.getSession(false).setAttribute("sessMonthStatusCollation", ToCreateDate);
                                    request.getSession(false).setAttribute("sessYearStatusCollation", FromCreateDate);
                                    request.getSession(false).setAttribute("sessBranchOfficeStatusCollation", idBranchOffice);
                                    String strAlertAllTimes = request.getSession(false).getAttribute("AlertAllTimeCertCollation").toString().trim();
                                    if("1".equals(strAlertAllTimes)) {
                                        FromDate = "";
                                        ToDate = "";
                                    } else{
                                        ToCreateDate = "";
                                        FromCreateDate = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
                                        idBranchOffice = "";
                                    }
                                    DISCOUNT_RATE_PROFILE[][] rsRose = new DISCOUNT_RATE_PROFILE[1][];
                                    db.S_BO_BRANCH_GET_DISCOUNT_RATE_PROFILE(idBranchOffice, rsRose);
                                    if(rsRose[0].length > 0)
                                    {
                                        String sProperties = rsRose[0][0].PROPERTIES;
                                        if(!"".equals(sProperties))
                                        {
                                            PROFILE_DISCOUNT_RATE_DATA[][] resIPData = new PROFILE_DISCOUNT_RATE_DATA[1][];
                                            CommonFunction.getAllProfileDiscountRate(sProperties, resIPData);
                                            if(resIPData != null && resIPData[0].length > 0) {
                                                for(PROFILE_DISCOUNT_RATE_DATA resIPData1 : resIPData[0]) {
                                                    if(resIPData1.enabled == true) {
                                                        if(resIPData1.isMoneyType == true) {
                                                            db.S_BO_CERTIFICATION_UPDATE_ROSE(idBranchOffice, ToCreateDate, FromCreateDate,
                                                                resIPData1.profileName, resIPData1.rosePercent, FromDate, ToDate);
                                                        } else {
                                                            db.S_BO_CERTIFICATION_UPDATE_DISCOUNT_RATE(idBranchOffice, ToCreateDate, FromCreateDate,
                                                                resIPData1.profileName, resIPData1.rosePercent, FromDate, ToDate);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    strView = "0#0";
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
