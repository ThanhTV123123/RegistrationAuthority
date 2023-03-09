/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import java.io.File;
import java.io.FileInputStream;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.utility.PropertiesContent;

/**
 *
 * @author THANH-PC
 */
public class CACommon extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CACommon.class);
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
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html");
        request.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession sessionsa = request.getSession(false);
            ConnectDatabase com = new ConnectDatabase();
            CommonFunction comP = new CommonFunction();
            String strView = "";
            String fileUploaded = "";
            InputStream in = null;
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
                            case "editca": {
                                //<editor-fold defaultstate="collapsed" desc="editca">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String ids = EscapeUtils.escapeHtml(request.getParameter("id"));
                                    String pName = request.getParameter("citycode");
                                    String Remark = request.getParameter("Remark");
                                    String Remark_EN = request.getParameter("Remark_EN");
                                    String ActiveFlag = request.getParameter("ActiveFlag");
                                    String OCSP = EscapeUtils.CheckTextNull(request.getParameter("OCSP"));
                                    String CRL = EscapeUtils.CheckTextNull(request.getParameter("CRL"));
                                    String URI = EscapeUtils.CheckTextNull(request.getParameter("URI"));
                                    String CRLPath = EscapeUtils.CheckTextNull(request.getParameter("CRLPath"));
                                    String strCertificate = EscapeUtils.CheckTextNull(request.getParameter("strCertificate"));
                                    String nameCheckOCSP = EscapeUtils.CheckTextNull(request.getParameter("nameCheckOCSP"));
                                    String nameUniqueDN = EscapeUtils.CheckTextNull(request.getParameter("nameUniqueDN"));
//                                    byte[] bytesCert = strCertificate.getBytes();
                                    byte[] bytesCRL = null;// CommonFunction.getByteFromURL(CRL);
                                    java.sql.Timestamp pCRLDATA_LAST_UPDATED_DT;
                                    java.sql.Timestamp pCRLDATA_NEXT_UPDATED_DT;
                                    
                                    String pCRLDATA_ISSUER_SUBJECT;
                                    String pCRLDATA_AUTHORITY_KEY_ID = "";
//                                    Date[] dateNext = new Date[1];
//                                    Date[] dateThis = new Date[1];
                                    String[] strParse = new String[3];
                                    // CommonFunction.getComponentFromCRL(bytesCRL, strParse, dateNext, dateThis);
                                    pCRLDATA_LAST_UPDATED_DT = null; //new java.sql.Timestamp(dateThis[0].getTime());
                                    pCRLDATA_NEXT_UPDATED_DT = null;// new java.sql.Timestamp(dateNext[0].getTime());
                                    pCRLDATA_ISSUER_SUBJECT = "";// strParse[0];
                                    String param1 = com.S_BO_CERTIFICATION_AUTHORITY_UPDATE(ids,
                                            ActiveFlag, EscapeUtils.escapeHtml(pName), CRLPath, OCSP, nameCheckOCSP,
                                            CRL, strCertificate.trim(), EscapeUtils.escapeHtml(Remark_EN),
                                            EscapeUtils.escapeHtml(Remark), bytesCRL, pCRLDATA_LAST_UPDATED_DT,
                                            pCRLDATA_NEXT_UPDATED_DT, pCRLDATA_ISSUER_SUBJECT,
                                            pCRLDATA_AUTHORITY_KEY_ID, URI, "", loginUID, nameUniqueDN);
                                    if ("0".equals(param1)) {
                                        request.getSession(false).setAttribute("SessRefreshCA", "1");
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
                            case "addca": {
                                //<editor-fold defaultstate="collapsed" desc="addca">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String pName = request.getParameter("citycode");
                                    String pOCSP = request.getParameter("OCSP");
                                    String pCRLPath = request.getParameter("CRLPath");
                                    String pCRL = request.getParameter("CRL");
                                    String pnameCheckOCSP = EscapeUtils.CheckTextNull(request.getParameter("nameCheckOCSP"));
                                    String pURI = request.getParameter("URI");
                                    String Remark = request.getParameter("Remark");
                                    String Remark_EN = request.getParameter("Remark_EN");
                                    String pstrCertificate = EscapeUtils.CheckTextNull(request.getParameter("strCertificate"));
                                    String nameUniqueDN = EscapeUtils.CheckTextNull(request.getParameter("nameUniqueDN"));
                                    if (!"".equals(pstrCertificate)) {
                                        if (!"".equals(pCRL)) {
//                                            byte[] bytesCert = pstrCertificate.getBytes();
                                            byte[] bytesCRL = null;// CommonFunction.getByteFromURL(pCRL);
//                                            if (bytesCRL != null) {
                                            java.sql.Timestamp pCRLDATA_LAST_UPDATED_DT;
                                            java.sql.Timestamp pCRLDATA_NEXT_UPDATED_DT;
                                            String pCRLDATA_ISSUER_SUBJECT;
                                            String pCRLDATA_AUTHORITY_KEY_ID = "";
                                            Date[] dateNext = new Date[1];
                                            Date[] dateThis = new Date[1];
                                            String[] strParse = new String[3];
//                                            CommonFunction.getComponentFromCRL(bytesCRL, strParse, dateNext, dateThis);
                                            pCRLDATA_LAST_UPDATED_DT = null;// new java.sql.Timestamp(dateThis[0].getTime());
                                            pCRLDATA_NEXT_UPDATED_DT = null;//new java.sql.Timestamp(dateNext[0].getTime());
                                            pCRLDATA_ISSUER_SUBJECT = "";// strParse[0];
                                            String param1 = com.S_BO_CERTIFICATION_AUTHORITY_INSERT(EscapeUtils.escapeHtml(pName),
                                                    pCRLPath, pOCSP, pnameCheckOCSP, pCRL, pstrCertificate.trim(), EscapeUtils.escapeHtml(Remark_EN),
                                                    EscapeUtils.escapeHtml(Remark), bytesCRL, pCRLDATA_LAST_UPDATED_DT,
                                                    pCRLDATA_NEXT_UPDATED_DT, pCRLDATA_ISSUER_SUBJECT, pCRLDATA_AUTHORITY_KEY_ID, pURI, loginUID, nameUniqueDN);
                                            if ("0".equals(param1)) {
                                                request.getSession(false).setAttribute("SessRefreshCA", "1");
                                                strView = "0#0";
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        }
//                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "uploadcrl": {
                                //<editor-fold defaultstate="collapsed" desc="uploadcrl">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = request.getParameter("CAID");
                                    fileUploaded = request.getParameter("UrlCRL");
                                    in = new FileInputStream(fileUploaded.trim());
                                    byte[] bytesCRL = IOUtils.toByteArray(in);
                                    if (bytesCRL != null) {
                                        java.sql.Timestamp pCRLDATA_LAST_UPDATED_DT;
                                        java.sql.Timestamp pCRLDATA_NEXT_UPDATED_DT;
                                        String pCRLDATA_ISSUER_SUBJECT;
                                        Date[] dateNext = new Date[1];
                                        Date[] dateThis = new Date[1];
                                        String[] strParse = new String[3];
                                        CommonFunction.getComponentFromCRL(bytesCRL, strParse, dateNext, dateThis);
                                        pCRLDATA_LAST_UPDATED_DT = new java.sql.Timestamp(dateThis[0].getTime());
                                        pCRLDATA_NEXT_UPDATED_DT = new java.sql.Timestamp(dateNext[0].getTime());
                                        pCRLDATA_ISSUER_SUBJECT = strParse[0];
                                        String param1 = com.S_BO_CERTIFICATION_AUTHORITY_CRLDATA_IMPORT(Integer.parseInt(id),
                                                "1", bytesCRL, pCRLDATA_LAST_UPDATED_DT, pCRLDATA_NEXT_UPDATED_DT,
                                                pCRLDATA_ISSUER_SUBJECT, "", "", loginUID);
                                        if ("0".equals(param1)) {
                                            strView = "0#" + CommonFunction.formatDateTimeToDateHour(dateThis[0])
                                                + "#" + CommonFunction.formatDateTimeToDateHour(dateNext[0])
                                                + "#" + pCRLDATA_ISSUER_SUBJECT
                                                + "#" + String.valueOf(comP.convertMoney(bytesCRL.length / 1024));
                                        } else {
                                            strView = param1 + "#" + param1;
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "reloadcrl": {
                                //<editor-fold defaultstate="collapsed" desc="reloadcrl">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = request.getParameter("CAID");
                                    CERTIFICATION_AUTHORITY[][] rsPgin = new CERTIFICATION_AUTHORITY[1][];
                                    com.S_BO_CERTIFICATION_AUTHORITY_DETAIL(EscapeUtils.escapeHtml(id), rsPgin);
                                    String sCRL_URI = "";
                                    if (rsPgin[0].length > 0) {
                                        sCRL_URI = EscapeUtils.CheckTextNull(rsPgin[0][0].CRL_URI);
                                    }
                                    if (!"".equals(sCRL_URI)) {
                                        byte[] bytesCRL = CommonFunction.getByteFromURL(sCRL_URI);
                                        if (bytesCRL != null) {
                                            java.sql.Timestamp pCRLDATA_LAST_UPDATED_DT;
                                            java.sql.Timestamp pCRLDATA_NEXT_UPDATED_DT;
                                            String pCRLDATA_ISSUER_SUBJECT;
                                            Date[] dateNext = new Date[1];
                                            Date[] dateThis = new Date[1];
                                            String[] strParse = new String[3];
                                            CommonFunction.getComponentFromCRL(bytesCRL, strParse, dateNext, dateThis);
                                            pCRLDATA_LAST_UPDATED_DT = new java.sql.Timestamp(dateThis[0].getTime());
                                            pCRLDATA_NEXT_UPDATED_DT = new java.sql.Timestamp(dateNext[0].getTime());
                                            pCRLDATA_ISSUER_SUBJECT = strParse[0];
                                            String param1 = com.S_BO_CERTIFICATION_AUTHORITY_CRLDATA_IMPORT(Integer.parseInt(id),
                                                    "1", bytesCRL, pCRLDATA_LAST_UPDATED_DT, pCRLDATA_NEXT_UPDATED_DT,
                                                    pCRLDATA_ISSUER_SUBJECT, "", "", loginUID);
                                            if ("0".equals(param1)) {
                                                strView = "0#" + CommonFunction.formatDateTimeToDateHour(dateThis[0])
                                                        + "#" + CommonFunction.formatDateTimeToDateHour(dateNext[0])
                                                        + "#" + pCRLDATA_ISSUER_SUBJECT
                                                        + "#" + String.valueOf(comP.convertMoney(bytesCRL.length / 1024));
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_GRID_STRING_NA + "#" + Definitions.CONFIG_GRID_STRING_NA
                                                    + "#" + Definitions.CONFIG_GRID_STRING_NA
                                                    + "#" + Definitions.CONFIG_GRID_STRING_NA
                                                    + "#" + Definitions.CONFIG_GRID_STRING_NA;
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            // properties
                            case "deleteproperties": {
                                //<editor-fold defaultstate="collapsed" desc="deleteproperties">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String value = request.getParameter("value");
                                    String id = request.getParameter("id");
                                    String sSoapProperties = "";
                                    CERTIFICATION_AUTHORITY[][] rsRelying = new CERTIFICATION_AUTHORITY[1][];
                                    com.S_BO_CERTIFICATION_AUTHORITY_DETAIL(EscapeUtils.escapeHtml(id), rsRelying);
                                    if (rsRelying[0].length > 0) {
                                        sSoapProperties = rsRelying[0][0].PROPERTIES;
                                    }
                                    if (!"".equals(sSoapProperties)) {
                                        String sConfig = PropertiesContent.removePropertiesContent(sSoapProperties, value);
                                        if (!"".equals(sConfig.trim())) {
                                            com.S_BO_CERTIFICATION_AUTHORITY_UPDATE(id, "", "", "", "",
                                                    "", "", "", "", "", null, null, null, "", "", "", sConfig, loginUID,"");
                                            strView = "0#0";
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#1";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "editproperties": {
                                //<editor-fold defaultstate="collapsed" desc="editproperties">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String nameType = request.getParameter("nameType");
                                    String nameValue = request.getParameter("nameValue");
                                    CommonFunction.LogDebugString(log, "editproperties", nameValue);
                                    String id = request.getParameter("id");
                                    String sSoapProperties = "";
                                    CERTIFICATION_AUTHORITY[][] rsRelying = new CERTIFICATION_AUTHORITY[1][];
                                    com.S_BO_CERTIFICATION_AUTHORITY_DETAIL(EscapeUtils.escapeHtml(id), rsRelying);
                                    if (rsRelying[0].length > 0) {
                                        sSoapProperties = rsRelying[0][0].PROPERTIES;
                                    }
                                    if (!"".equals(sSoapProperties)) {
                                        String sConfig = PropertiesContent.updatePropertiesContent(sSoapProperties, nameType.trim(), nameValue.trim());
                                        if (!"".equals(sConfig.trim())) {
                                            com.S_BO_CERTIFICATION_AUTHORITY_UPDATE(id, "", "", "", "",
                                                    "", "", "", "", "", null, null, null, "", "", "", sConfig, loginUID,"");
                                            strView = "0#0";
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#1";
                                        }
                                    } else {
                                        strView = "1#1";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                break;
                                //</editor-fold>
                            }
                            case "addproperties": {
                                //<editor-fold defaultstate="collapsed" desc="addproperties">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String nameType = request.getParameter("nameType");
                                    String nameValue = request.getParameter("nameValue");
                                    String id = request.getParameter("id");
                                    String sSoapProperties = "";
                                    CERTIFICATION_AUTHORITY[][] rsRelying = new CERTIFICATION_AUTHORITY[1][];
                                    com.S_BO_CERTIFICATION_AUTHORITY_DETAIL(EscapeUtils.escapeHtml(id), rsRelying);
                                    if (rsRelying[0].length > 0) {
                                        sSoapProperties = rsRelying[0][0].PROPERTIES;
                                    }
                                    if (!"".equals(sSoapProperties)) {
                                        String sConfig = PropertiesContent.insertPropertiesContent(sSoapProperties, nameType.trim(), nameValue.trim());
                                        if (!"".equals(sConfig.trim())) {
                                            com.S_BO_CERTIFICATION_AUTHORITY_UPDATE(id, "", "", "", "",
                                                    "", "", "", "", "", null, null, null, "", "", "", sConfig, loginUID,"");
                                            strView = "0#0";
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#1";
                                        }
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
            } catch (NumberFormatException | SQLException | UnsupportedEncodingException e) {
                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#" + e.getMessage();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if ((new File(fileUploaded)).exists()) {
                        (new File(fileUploaded)).delete();
                    }
                } catch (IOException e) {
                    CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                }
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
