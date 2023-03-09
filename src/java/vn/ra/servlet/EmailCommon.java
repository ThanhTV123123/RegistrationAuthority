package vn.ra.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import vn.ra.process.ConnectDatabase;
import vn.ra.process.DESEncryption;
import vn.ra.utility.EscapeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.process.CommonFunction;
import vn.ra.utility.Definitions;

/**
 *
 * @author THANH
 */
public class EmailCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EmailCommon.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        response.setContentType("text/html;charset=UTF-8");
        String strView = "";
        try (PrintWriter out = response.getWriter()) {
            HttpSession sessionsa = request.getSession(false);
            ConnectDatabase db = new ConnectDatabase();
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
                    String loginUID = sessionsa.getAttribute("sUserID").toString().trim();
                    String idParam = request.getParameter("idParam");
                    if ("editemail".equals(idParam)) {
                        String anticsrf = request.getParameter("CsrfToken");
                        if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                            DESEncryption des = new DESEncryption();
                            String doID = request.getParameter("doID");
                            String IPMailServer = request.getParameter("IPMailServer");
                            String Port = request.getParameter("Port");
                            String EmailAddress = request.getParameter("EmailAddress");
                            String Passwords = request.getParameter("Passwords");
                            String sPass = "";
                            if (!"".equals(Passwords.trim())) {
                                sPass = des.encrypt(Passwords.trim());
                            }
                            String SubjectMail = "Subject";// request.getParameter("Subject");
                            String Template = "Content"; //request.getParameter("Template");
                            String IsClient = "1";// request.getParameter("IsClient");
                            String param1 = db.BO_CONFIGEMAIL_UPDATE(Integer.parseInt(doID), EscapeUtils.escapeHtml(IPMailServer.trim()), EscapeUtils.escapeHtml(Port.trim()),
                                    EscapeUtils.escapeHtml(EmailAddress.trim()), sPass.trim(), EscapeUtils.escapeHtml(SubjectMail.trim()),
                                    EscapeUtils.escapeHtml(Template.trim()), Integer.parseInt(IsClient));
                            if ("0".equals(param1)) {
                                strView = "0#0";
                            } else {
                                log.info("Error: " + param1 + Definitions.CONFIG_LOG_WRITE_DOWNLINE);
                                strView = "1#" + param1;
                            }
                        } else {
                            strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                        }
                    }
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "#0";
                }
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
        } catch (SQLException ex) {
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
        } catch (SQLException ex) {
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
