package vn.ra.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.utility.Definitions;

/**
 *
 * @author THANH
 */
public class LogoutCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LogoutCommon.class.getName());

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
            throws ServletException, IOException, SQLException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        String strView;
        try (PrintWriter out = response.getWriter()) {
            try {
                String sUserName = request.getParameter("sUserName");
                String sSessKey = request.getParameter("sSessKey");
                HttpSession sessionsa = request.getSession(false);
//                long now = new Date().getTime();
//                long lastAccessed = sessionsa.getLastAccessedTime();
//                long timeoutPeriod = sessionsa.getMaxInactiveInterval();
//                long remainingTime = ((timeoutPeriod * 1000) - (now - lastAccessed))/1000;
//                System.out.println("Remaining time is " + remainingTime + " seconds");
//                CommonFunction.LogDebugString(log, "SESSION-STATUS", "TIME: " + CommonFunction.GetDateFromLong(sessionsa.getLastAccessedTime())
//                    + "; USER: " + sUserName + "; TIME REMAINING: " + remainingTime
//                    + "; IP: " + CommonFunction.getClientIpLogin(request));
                if (sessionsa != null) {
                    if(sessionsa.getAttribute("sUserID") != null)
                    {
                        CommonFunction.LogDebugString(log, "SESSION-LOGOUT-BY", "TIME: " + CommonFunction.GetDateCurrent()
                            + "; USER: " + request.getSession(false).getAttribute("sUserID").toString().trim()
                            + "; SESSION-KEY: " + sSessKey + "; IP: " + CommonFunction.getClientIpLogin(request));// + "; TIME: " + CommonFunction.generateNumberDays());
                        String strIdBRANCH = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                        String strIdUser = request.getSession(false).getAttribute("UserID").toString().trim();
                        String strSessRoleID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
                        ServletContext sc = request.getSession().getServletContext();
                        if (sc.getAttribute("sessPushNotiRequestDecline-"+strIdBRANCH + "-"+strIdUser) != null) {
                            sc.setAttribute("sessPushNotiRequestDecline-"+strIdBRANCH + "-"+strIdUser, null);
                        }
                        if(strSessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                            || strSessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)
                            || strSessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR))
                        {
                            if (sc.getAttribute("sessPushNotiRequestApprove") != null) {
                                sc.setAttribute("sessPushNotiRequestApprove", null);
                            }
                        }
                        sessionsa.setAttribute("sUserID", null);
                        sessionsa.setAttribute("UserID", null);
                        sessionsa.invalidate();
                    } else {
                        CommonFunction.LogDebugString(log, "SESSION-EXPIRE-BY", "TIME: " + CommonFunction.GetDateFromLong(sessionsa.getLastAccessedTime())
                            + "; USER: " + sUserName + "; SESSION-KEY: " + sSessKey + "; IP: " + CommonFunction.getClientIpLogin(request));
                    }
                    CommonFunction.regenerateSession(request);
                    strView = "0#0";
                } else {
                    strView = "0#0";
                    CommonFunction.LogDebugString(log, "SESSION-EXPIRE-BY", "TIME: " + CommonFunction.GetDateFromLong(sessionsa.getLastAccessedTime())
                        + "; USER: " + sUserName + "; SESSION-KEY: " + sSessKey + "; IP: " + CommonFunction.getClientIpLogin(request));
                }
            } catch (Exception e) {
                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                strView = "1#" + e.getMessage();
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
