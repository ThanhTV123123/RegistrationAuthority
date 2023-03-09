/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vn.ra.object.BRANCH;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
public class TraceSystemCommon extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TraceSystemCommon.class);
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
            ConnectDatabase db = new ConnectDatabase();
            String strView = "";
            try {
                String idParam = request.getParameter("idParam");
                if (null != idParam) {
                    switch (idParam) {
                        case "callws": {
                            //<editor-fold defaultstate="collapsed" desc="callws">
                            String sResult = ConnectConnector.TestConnectWS();
                            strView = "0#" + sResult;
                            //</editor-fold>
                            break;
                        }
                        case "calldb": {
                            //<editor-fold defaultstate="collapsed" desc="calldb">
                            int countRole = db.S_BO_ROLE_TOTAL();
                            strView = "0#" + String.valueOf(countRole);
                            //</editor-fold>
                            break;
                        }
                        case "importcertcoreca": {
                            //<editor-fold defaultstate="collapsed" desc="importcertcoreca">
                            HttpSession sessionsa = request.getSession(false);
                            String AGENT_ID_LOG = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SessAgentID").toString().trim());
                            String SessUserAgentID = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SessUserAgentID").toString().trim());
                            String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                            String loginFullname = request.getSession(false).getAttribute("sesFullname").toString().trim();
                            String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                            String ID = String.valueOf(Definitions.CONFIG_TOKEN_UNASSIGN_ID);
                            String BRANCH_ID = request.getParameter("BRANCH_ID");
                            String pCREATE_USER = request.getParameter("CREATE_USER");
                            String TypeRegister = request.getParameter("sTypeRegister");
                            // check agency
                            boolean isAccessAgency = true;
                            String sTOKEN_SN = Definitions.CONFIG_TOKEN_UNASSIGN_SN;
                            if (TypeRegister.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_TOKEN)) {
                                if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                    isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(Integer.parseInt(BRANCH_ID), branchAccess);
//                                    if (!BRANCH_ID.equals(SessUserAgentID)) {
//                                        isAccessAgency = false;
//                                    }
                                }
                            } else {
                                ID = String.valueOf(Definitions.CONFIG_TOKEN_ID_UNKNOWN);
                            }
                            if (isAccessAgency == true) {
                                String PHONE_CONTRACT = request.getParameter("PHONE_CONTRACT");
                                String EMAIL_CONTRACT = request.getParameter("EMAIL_CONTRACT");
                                String CertProfileID = request.getParameter("CertProfileID");
                                String idCer = EscapeUtils.CheckTextNull(request.getParameter("idCer"));
                                String pPAST_CERTIFICATE_ID = Definitions.CONFIG_CERTIFICATE_PAST_CERTIFICATE_ID;
                                String pCERTIFICATION_PURPOSE = EscapeUtils.CheckTextNull(request.getParameter("pCERTIFICATION_PURPOSE"));
                                String pCERTIFICATION_HASH = CommonFunction.getThumbprintCertificate(idCer);
                                
                            }
                            
                            
                            
                            
                            
                            //</editor-fold>
                            break;
                        }
                    }
                }
            } catch (NumberFormatException e) {
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
