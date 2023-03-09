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
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vn.ra.object.BACKOFFICE_USER;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATE_ATTRIBUTES;
import vn.ra.object.CERTIFICATE_ATTRIBUTES_RADIO;
import vn.ra.object.CERTIFICATE_ATTRIBUTES_SESSION;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.CERTIFICATION_PROFILE_ATTR;
import vn.ra.object.CERTIFICATION_PURPOSE;
import vn.ra.object.CITY_PROVINCE;
import vn.ra.object.ENTERPRISE_INFO;
import vn.ra.object.FILE_PROFILE_JSON;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.MENULINK;
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

/**
 *
 * @author THANH-PC
 */
public class JSONDetailCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final Logger log = Logger.getLogger(JSONDetailCommon.class.getName());

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
            String SessAgentID = sessionsa.getAttribute("SessAgentID").toString().trim();
            String SessUserAgentID = sessionsa.getAttribute("SessUserAgentID").toString().trim();
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
                switch (sOutInner) {
                    case 1:
                        String pSessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                        String idParam = request.getParameter("idParam");
                        if (null != idParam) {
                            switch (idParam) {
                                case "tokendetailcert": {
                                    //<editor-fold defaultstate="collapsed" desc="certdetaillist">
                                    String anticsrf = request.getParameter("CsrfToken");
                                    if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                        String ids = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                        TOKEN[][] rs = new TOKEN[1][];
                                        db.S_BO_TOKEN_ISSUED_DETAIL(EscapeUtils.escapeHtml(ids), pSessLanguage, rs);
                                        if (rs[0].length > 0) {
                                            boolean isAccessAgencyPage = true;
                                            if (!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                isAccessAgencyPage = CommonFunction.checkBranchTreeInvalidCert(rs[0][0].BRANCH_ID, branchAccess);
//                                                if (!String.valueOf(rs[0][0].BRANCH_ID).equals(SessUserAgentID)) {
//                                                    isAccessAgencyPage = false;
//                                                }
                                            }
                                            if (isAccessAgencyPage == true) {
                                                if (rs[0][0].TOKEN_STATE_ID != Definitions.CONFIG_TOKEN_STATE_ID_LOCKED
                                                        && rs[0][0].TOKEN_STATE_ID != Definitions.CONFIG_TOKEN_STATE_ID_LOST) {
                                                    JSONObject json = new JSONObject();
                                                    json.put("Code", "0");
                                                    json.put("TOKEN_ID", String.valueOf(rs[0][0].ID));
                                                    json.put("TOKEN_SN", EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN));
                                                    json.put("BRANCH_NAME", EscapeUtils.CheckTextNull(rs[0][0].BRANCH_DESC));
                                                    json.put("BRANCH_ID", String.valueOf(rs[0][0].BRANCH_ID));
                                                    listJson.add(json);
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
                            }
                        }
                        break;
                    case 2: {
                        JSONObject json = new JSONObject();
                        json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_LOGIN);
                        listJson.add(json);
                        break;
                    }
                    default: {
                        JSONObject json = new JSONObject();
                        json.put("Code", Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN);
                        listJson.add(json);
                        break;
                    }
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
