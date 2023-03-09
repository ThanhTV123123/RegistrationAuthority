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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.utility.Definitions;
import org.apache.log4j.Logger;
import vn.ra.process.CommonFunction;

/**
 *
 * @author THANH
 */
public class MenuLinkCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final Logger log = Logger.getLogger(MenuLinkCommon.class.getName());

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
                    String idParam = request.getParameter("idParam");
                    if (null != idParam) {
                        switch (idParam) {
                            case "deletemenu": {
                                //<editor-fold defaultstate="collapsed" desc="case deletemenu">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    String sDelete = db.S_BO_URI_ROLE_DELETE(id, loginUID);
                                    if("0".equals(sDelete)){
                                        strView = "0#0";
                                    } else {
                                        strView = "1#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "addmenu": {
                                //<editor-fold defaultstate="collapsed" desc="case addmenu">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String strRole = EscapeUtils.escapeHtml(request.getParameter("role"));
                                    String strID = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    if(!"".equals(strRole) && !"".equals(strID)) {
                                        db.S_BO_URI_ROLE_INSERT(strID, EscapeUtils.escapeHtml(strRole), loginUID);
                                        strView = "0#0";
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "PermissionMenu: Please enter all required");
                                        strView = "10#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "editmenuscreen": {
                                //<editor-fold defaultstate="collapsed" desc="case editmenuscreen">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ParentId = request.getParameter("ParentId");
                                    String LinkUrl = EscapeUtils.escapeHtml(request.getParameter("LinkUrl"));
                                    String LinkUrlCheck = LinkUrl;
                                    if(Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(ParentId))
                                    {
                                        ParentId = String.valueOf(Definitions.CONFIG_EMPTY_N_A_GLOBAL);
                                        LinkUrlCheck = "1";
                                        LinkUrl = "";
                                    }
                                    String id = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    String Remark = EscapeUtils.escapeHtml(request.getParameter("Remark"));
                                    String Remark_EN = EscapeUtils.escapeHtml(request.getParameter("Remark_EN"));
                                    String LinkName = EscapeUtils.escapeHtml(request.getParameter("LinkName"));
                                    String ActiveFlag = request.getParameter("ActiveFlag");
                                    if(!"".equals(id) && !"".equals(LinkName) && !"".equals(Remark) && !"".equals(Remark_EN)
                                         && !"".equals(LinkUrlCheck))
                                    {
                                        if(Remark.length() <= 256 && Remark_EN.length() <= 256 && LinkUrl.length() <= 256 && LinkName.length() <= 64)
                                        {
                                            int sParam = db.S_BO_URI_UPDATE(id, ActiveFlag, LinkName,
                                                ParentId, Remark, Remark_EN, LinkUrl, loginUID);
                                            strView = String.valueOf(sParam) + "#" + String.valueOf(sParam);
                                            request.getSession(false).setAttribute("SessRefreshMenuScreen", "1");
                                        } else {
                                            CommonFunction.LogErrorServlet(log, "EditMenu: Length is invalid");
                                            strView = "11#0";
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "EditMenu: Please enter all required");
                                        strView = "10#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "addmenuscreen": {
                                //<editor-fold defaultstate="collapsed" desc="case addmenuscreen">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ParentId = request.getParameter("ParentId");
                                    String LinkUrl = EscapeUtils.escapeHtml(request.getParameter("LinkUrl"));
                                    String LinkUrlCheck = LinkUrl;
                                    if(Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(ParentId))
                                    {
                                        ParentId = String.valueOf(Definitions.CONFIG_EMPTY_N_A_GLOBAL);
                                        LinkUrlCheck = "1";
                                        LinkUrl = "";
                                    }
                                    String LinkName = EscapeUtils.escapeHtml(request.getParameter("LinkName"));
                                    String Remark = EscapeUtils.escapeHtml(request.getParameter("Remark"));
                                    String Remark_EN = EscapeUtils.escapeHtml(request.getParameter("Remark_EN"));
                                    if(!"".equals(LinkName) && !"".equals(Remark) && !"".equals(Remark_EN)
                                         && !"".equals(LinkUrlCheck))
                                    {
                                        if(Remark.length() <= 256 && Remark_EN.length() <= 256 && LinkUrl.length() <= 256 && LinkName.length() <= 64)
                                        {
                                            int sParam = db.S_BO_URI_INSERT(LinkName, ParentId, Remark,
                                                Remark_EN, LinkUrl, loginUID);
                                            strView = String.valueOf(sParam) + "#" + String.valueOf(sParam);
                                            request.getSession(false).setAttribute("SessRefreshMenuScreen", "1");
                                        } else {
                                            CommonFunction.LogErrorServlet(log, "AddMenu: Length is invalid");
                                            strView = "11#0";
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "AddMenu: Please enter all required");
                                        strView = "10#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "deletemethodprofile": {
                                //<editor-fold defaultstate="collapsed" desc="deletemethodprofile">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    db.S_BO_PKI_FORMFACTOR_ATTR_REMOVE(id, loginUID);
                                    strView = "0#0";
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "addmethodprofile": {
                                //<editor-fold defaultstate="collapsed" desc="addmethodprofile">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String pPKI_FORMFACTOR_ID = EscapeUtils.escapeHtml(request.getParameter("role"));
                                    String pVALUE = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    if(!"".equals(pPKI_FORMFACTOR_ID) && !"".equals(pVALUE)) {
                                        String pFORMFACTOR_ATTR_TYPE_PROFILE = "1";
                                        db.S_BO_PKI_FORMFACTOR_ATTR_INSERT(pPKI_FORMFACTOR_ID, pFORMFACTOR_ATTR_TYPE_PROFILE,
                                            EscapeUtils.escapeHtml(pVALUE), loginUID);
                                        strView = "0#0";
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "PermissionProfile: Please enter all required");
                                        strView = "10#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "editformfactor": {
                                //<editor-fold defaultstate="collapsed" desc="editformfactor">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String pPKI_FORMFACTOR_ID = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    String Remark = EscapeUtils.escapeHtml(request.getParameter("Remark"));
                                    String Remark_EN = EscapeUtils.escapeHtml(request.getParameter("Remark_EN"));
                                    String JsonProperties = EscapeUtils.escapeHtml(request.getParameter("JsonProperties"));
                                    String ActiveFlag = EscapeUtils.escapeHtml(request.getParameter("ActiveFlag"));
                                    int isActive = 1;
                                    if("0".equals(ActiveFlag)) {
                                        isActive = 0;
                                    }
                                    String[] arrayReplace = {"{}","[]"};
                                    for(String sItem : arrayReplace){
                                        if(JsonProperties.equals(sItem)){
                                            JsonProperties = "";
                                            break;
                                        }
                                    }
                                    if(!"".equals(pPKI_FORMFACTOR_ID) && !"".equals(Remark) && !"".equals(Remark_EN)) {
                                        db.S_BO_PKI_FORMFACTOR_UPDATE(pPKI_FORMFACTOR_ID, Remark,
                                            Remark_EN, JsonProperties, isActive, loginUID);
                                        sessionsa.setAttribute("SessRefreshFormFactor", "1");
                                        strView = "0#0";
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "PermissionProfile: Please enter all required");
                                        strView = "10#0";
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
