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
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.object.ROLE;
import vn.ra.object.ROLE_ATTRIBUTES;
import vn.ra.object.ROLE_DATA;
import vn.ra.process.CommonFunction;
import vn.ra.process.SessionRoleFunctions;
import vn.ra.utility.Definitions;

/**
 *
 * @author THANH
 */
public class RoleCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RoleCommon.class.getName());

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
        response.addHeader("X-XSS-Protection", "1");
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
                            case "editrole": {
                                // <editor-fold defaultstate="collapsed" desc="editrole">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ids = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    String GroupCode = EscapeUtils.escapeHtml(request.getParameter("GroupCode"));
                                    String Remark = EscapeUtils.CheckTextNull(request.getParameter("Remark"));
                                    String Remark_EN = EscapeUtils.CheckTextNull(request.getParameter("Remark_EN"));
                                    if(!"".equals(ids) && !"".equals(Remark) && !"".equals(Remark_EN) && !"".equals(GroupCode))
                                    {
                                        if(Remark.length() <= 256 && Remark_EN.length() <= 256 && GroupCode.length() <= 64)
                                        {
                                            ROLE_DATA[][] rsToken = new ROLE_DATA[1][];
                                            ROLE_DATA[][] rsCert = new ROLE_DATA[1][];
                                            ROLE_DATA[][] rsAnother = new ROLE_DATA[1][];
                                            // ROLE FUNCTIONS TOKEN
                                            SessionRoleFunctions cartToken = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsToken");
                                            if (cartToken != null) {
                                                ArrayList<ROLE_DATA> tempListToken = new ArrayList<>();
                                                ArrayList<ROLE_DATA> ds = cartToken.getGH();
                                                for (ROLE_DATA mhIP : ds) {
                                                    ROLE_DATA tempItem = new ROLE_DATA();
                                                    tempItem.name = mhIP.name;
                                                    tempItem.attributeType = mhIP.attributeType;
                                                    tempItem.remarkEn = mhIP.remarkEn;
                                                    tempItem.remark = mhIP.remark;
                                                    tempItem.enabled = mhIP.enabled;
                                                    tempListToken.add(tempItem);
                                                }
                                                rsToken[0] = new ROLE_DATA[tempListToken.size()];
                                                rsToken[0] = tempListToken.toArray(rsToken[0]);
                                            }
                                            // ROLE FUNCTIONS CERT
                                            SessionRoleFunctions cartCert = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsCert");
                                            if (cartCert != null) {
                                                ArrayList<ROLE_DATA> tempListCert = new ArrayList<>();
                                                ArrayList<ROLE_DATA> ds = cartCert.getGH();
                                                for (ROLE_DATA mhIP : ds) {
                                                    ROLE_DATA tempItem = new ROLE_DATA();
                                                    tempItem.name = mhIP.name;
                                                    tempItem.attributeType = mhIP.attributeType;
                                                    tempItem.remarkEn = mhIP.remarkEn;
                                                    tempItem.remark = mhIP.remark;
                                                    tempItem.enabled = mhIP.enabled;
                                                    tempListCert.add(tempItem);
                                                }
                                                rsCert[0] = new ROLE_DATA[tempListCert.size()];
                                                rsCert[0] = tempListCert.toArray(rsCert[0]);
                                            }
                                            // ROLE FUNCTIONS ANOTHER
                                            SessionRoleFunctions cartAnother = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsAnother");
                                            if (cartAnother != null) {
                                                ArrayList<ROLE_DATA> tempListAnother = new ArrayList<>();
                                                ArrayList<ROLE_DATA> ds = cartAnother.getGH();
                                                for (ROLE_DATA mhIP : ds) {
                                                    ROLE_DATA tempItem = new ROLE_DATA();
                                                    tempItem.name = mhIP.name;
                                                    tempItem.attributeType = mhIP.attributeType;
                                                    tempItem.remarkEn = mhIP.remarkEn;
                                                    tempItem.remark = mhIP.remark;
                                                    tempItem.enabled = mhIP.enabled;
                                                    tempListAnother.add(tempItem);
                                                }
                                                rsAnother[0] = new ROLE_DATA[tempListAnother.size()];
                                                rsAnother[0] = tempListAnother.toArray(rsAnother[0]);
                                            }
                                            String sRoleJson = CommonFunction.AddRootRoleCertificate(rsToken, rsCert, rsAnother);
                                            String ActiveFlag = request.getParameter("ActiveFlag");
                                            String checkAllUser = "0";
                                            if("1".equals(EscapeUtils.CheckTextNull(request.getParameter("checkAllUser"))))
                                            {
                                                checkAllUser = "1";
                                            }
                                            String param1 = db.S_BO_ROLE_UPDATE(ids, ActiveFlag,
                                                    GroupCode, Remark, Remark_EN, sRoleJson, loginUID.trim(), checkAllUser);
                                            if ("0".equals(param1)) {
                                                strView = "0#0";
                                                request.getSession(false).setAttribute("SessRefreshRole", "1");
                                                request.getSession(false).setAttribute("sessRoleFunctionsToken", null);
                                                request.getSession(false).setAttribute("sessRoleFunctionsCert", null);
                                                request.getSession(false).setAttribute("sessRoleFunctionsAnother", null);
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        } else {
                                            CommonFunction.LogErrorServlet(log, "EditRole: Length is invalid");
                                            strView = "11#0";
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "EditRole: Please enter all required");
                                        strView = "10#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "addrole": {
                                // <editor-fold defaultstate="collapsed" desc="addrole">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String GroupCode = EscapeUtils.escapeHtml(request.getParameter("GroupCode"));
                                    String Remark = EscapeUtils.escapeHtml(request.getParameter("Remark"));
                                    String Remark_EN = EscapeUtils.escapeHtml(request.getParameter("Remark_EN"));
                                    String IsCA = EscapeUtils.CheckTextNull(request.getParameter("IsCA"));
                                    if(!"".equals(GroupCode) && !"".equals(Remark) && !"".equals(Remark_EN) && !"".equals(IsCA))
                                    {
                                        if(Remark.length() <= 256 && Remark_EN.length() <= 256 && GroupCode.length() <= 64)
                                        {
                                            String sRoleJson = "";
                                            if("1".equals(IsCA))
                                            {
                                                ROLE[][] rsRole = new ROLE[1][];
                                                db.S_BO_ROLE_DETAIL(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD, rsRole);
                                                if(rsRole[0].length > 0)
                                                {
                                                    sRoleJson = EscapeUtils.CheckTextNull(rsRole[0][0].PROPERTIES);
                                                }
                                            } else {
                                                ROLE[][] rsRole = new ROLE[1][];
                                                db.S_BO_ROLE_DETAIL(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN, rsRole);
                                                if(rsRole[0].length > 0)
                                                {
                                                    sRoleJson = EscapeUtils.CheckTextNull(rsRole[0][0].PROPERTIES);
                                                }
                                                IsCA = "0";
                                            }
                                            String param1 = db.S_BO_ROLE_INSERT(GroupCode,
                                                    Remark, Remark_EN, sRoleJson, IsCA, loginUID.trim());
                                            if ("0".equals(param1)) {
                                                request.getSession(false).setAttribute("SessRefreshRole", "1");
                                                request.getSession(false).setAttribute("sessRoleFunctionsToken", null);
                                                request.getSession(false).setAttribute("sessRoleFunctionsCert", null);
                                                request.getSession(false).setAttribute("sessRoleFunctionsAnother", null);
                                                strView = "0#0";
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        } else {
                                            CommonFunction.LogErrorServlet(log, "AddRole: Length is invalid");
                                            strView = "11#0";
                                        }
                                    } else {
                                        CommonFunction.LogErrorServlet(log, "AddRole: Please enter all required");
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
