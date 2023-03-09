/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class TemplateDNCommon extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TemplateDNCommon.class);
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
                    String idParam = request.getParameter("idParam");
                    String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    if (null != idParam) {
                        switch (idParam) {
                            case "deletetemplate": {
                                //<editor-fold defaultstate="collapsed" desc="deletetemplate">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ids = request.getParameter("id");
                                    com.S_BO_CERTIFICATION_PROFILE_ATTR_UPDATE_OLD(Integer.parseInt(ids), "", "0", loginUID);
                                    strView = "0#0";
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "addtemplate": {
                                //<editor-fold defaultstate="collapsed" desc="addtemplate">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String CertTypeID = request.getParameter("CertTypeID");
                                    String SubjectDNAttrID = request.getParameter("SubjectDNAttrID");
                                    String param1 = com.S_BO_CERTIFICATION_PROFILE_ATTR_INSERT(EscapeUtils.escapeHtml(CertTypeID),
                                            EscapeUtils.escapeHtml(SubjectDNAttrID), loginUID);
                                    if ("0".equals(param1)) {
                                        strView = "0#0";
                                    } else {
                                        strView = param1 + "#" + param1;
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "updatemultitemplate": {
                                //<editor-fold defaultstate="collapsed" desc="updatemultitemplate">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sTemplateIDList = request.getParameter("TemplateIDList");
                                    CommonFunction.LogDebugString(log, "UpdateMulti", "TemplateIDList: " + sTemplateIDList);
                                    String[] sPlitValue = sTemplateIDList.split(",");
                                    if(sPlitValue.length > 0)
                                    {
                                        for (String sPlitItem : sPlitValue) {
                                            String ids = sPlitItem.split(Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX)[0];
                                            String sRequired = sPlitItem.split(Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX)[1];
                                            com.S_BO_CERTIFICATION_PROFILE_ATTR_UPDATE_OLD(Integer.parseInt(ids),
                                                sRequired, "", loginUID);
                                        }
                                        strView = "0#0";
                                    } else {
                                        strView = "1#1";
                                    }
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
            } catch (NumberFormatException | SQLException e) {
                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
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
