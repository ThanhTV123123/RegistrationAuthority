package vn.ra.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author THANH
 */
public class CityCommon extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CityCommon.class);
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
                    String idParam = request.getParameter("idParam");
                    String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    if (null != idParam) switch (idParam) {
                        case "editcity":{
                            //<editor-fold defaultstate="collapsed" desc="editcity">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                String ids = EscapeUtils.escapeHtml(request.getParameter("id"));
                                String pName = EscapeUtils.escapeHtml(request.getParameter("citycode"));
                                String Remark = EscapeUtils.escapeHtml(request.getParameter("Remark"));
                                String Remark_EN = EscapeUtils.escapeHtml(request.getParameter("Remark_EN"));
                                String ActiveFlag = EscapeUtils.escapeHtml(request.getParameter("ActiveFlag"));
                                if(!"".equals(ids) && !"".equals(Remark) && !"".equals(Remark_EN) && !"".equals(pName)
                                    && !"".equals(ActiveFlag))
                                {
                                    if(Remark.length() <= 256 && Remark_EN.length() <= 256 && pName.length() <= 64)
                                    {
                                        String param1 = com.S_BO_PROVINCE_UPDATE(Integer.parseInt(ids),
                                            ActiveFlag, pName, Remark_EN, Remark, loginUID);
                                        if ("0".equals(param1)) {
                                            request.getSession(false).setAttribute("SessRefreshCity", "1");
                                            strView = "0#0";
                                        } else {
                                            strView = param1+"#" + param1;
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "EditCity: Length is invalid");
                                        strView = "11#0";
                                    }
                                } else {
                                    CommonFunction.LogErrorServlet(log, "EditCity: Please enter all required");
                                    strView = "10#0";
                                }
                            } else {
                                strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "addcity":{
                            //<editor-fold defaultstate="collapsed" desc="addcity">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                String pName = request.getParameter("citycode");
                                String Remark = request.getParameter("Remark");
                                String Remark_EN = request.getParameter("Remark_EN");
                                if(!"".equals(Remark) && !"".equals(Remark_EN) && !"".equals(pName))
                                {
                                    if(Remark.length() <= 256 && Remark_EN.length() <= 256 && pName.length() <= 64)
                                    {
                                        String param1 = com.S_BO_PROVINCE_INSERT(EscapeUtils.escapeHtml(pName),
                                                EscapeUtils.escapeHtml(Remark_EN), EscapeUtils.escapeHtml(Remark), loginUID);
                                        if ("0".equals(param1)) {
                                            request.getSession(false).setAttribute("SessRefreshCity", "1");
                                            strView = "0#0";
                                        } else {
                                            strView = param1+"#" + param1;
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "AddCity: Length is invalid");
                                        strView = "11#0";
                                    }
                                } else {
                                    CommonFunction.LogErrorServlet(log, "AddCity: Please enter all required");
                                    strView = "10#0";
                                }
                            } else {
                                strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                            }
                            break;
                            //</editor-fold>
                        }
                    }
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "#0";
                }
            } catch (NumberFormatException | SQLException | UnsupportedEncodingException e) {
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
