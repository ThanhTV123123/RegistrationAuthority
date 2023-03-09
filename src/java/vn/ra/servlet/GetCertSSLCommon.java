/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import org.apache.log4j.Logger;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.object.CERTIFICATION_AUTHORITY_ATTR;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.process.CertificateCheckIn;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author THANH-PC
 */
//@WebServlet(name = "GetCertSSLCommon", urlPatterns = {"/GetCertSSLCommon"})
public class GetCertSSLCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final Logger log = Logger.getLogger(GetCertSSLCommon.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.security.cert.CertificateEncodingException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CertificateEncodingException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        String strView;
        try (PrintWriter out = response.getWriter()) {
//            String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
            ConnectDatabase db = new ConnectDatabase();
            CommonFunction com = new CommonFunction();
            log.info("Begin Servlet SSL");
            String sCert = getPemFromCertificate(extractCertificate(request));
            if(!"".equals(sCert))
            {
                String strCADefault="";
                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                if (sessGeneralPolicy[0].length > 0) {
                    for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                        if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT)) {
                            strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                            break;
                        }
                    }
                }
                int CA_ID = 0;
                CERTIFICATION_AUTHORITY_ATTR[][] rsCAAttr = new CERTIFICATION_AUTHORITY_ATTR[1][];
                db.S_BO_CERTIFICATION_AUTHORITY_ATTR_GET(strCADefault, rsCAAttr);
                if (rsCAAttr[0].length > 0) {
                    CA_ID = rsCAAttr[0][0].CERTIFICATION_AUTHORITY_ID;
                }
                String sCertCA = "";
                CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(CA_ID), rsCA);
                if (rsCA[0].length > 0) {
                    sCertCA = EscapeUtils.CheckTextNull(rsCA[0][0].CERTIFICATE);
                }
                if(!"".equals(sCertCA))
                {
                    if (!sCertCA.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
                        sCertCA = Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN + "\n" + sCertCA;
                    }
                    if (!sCertCA.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
                        sCertCA = sCertCA + "\n" + Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END;
                    }
                    boolean isCheckCTS = CertificateCheckIn.checkCertificateRelation(sCert, sCertCA);
                    if (isCheckCTS == true) {
                        int[] intRes = new int[1];
                        Object[] sss = new Object[2];
                        String[] tmp = new String[3];
                        boolean checkExpireDate = false;
                        com.VoidCertificateComponents(sCert, sss, tmp, intRes);
                        if (intRes[0] == 0 && sss.length > 0) {
                            String strNotAfter = EscapeUtils.CheckTextNull(tmp[2]);
                            CommonFunction.LogDebugString(log, "SSL-CERT-EXPIRE", strNotAfter);
                            if(!"".equals(strNotAfter))
                            {
                                checkExpireDate = CommonFunction.checkDateExpireValid(strNotAfter);
                            }
                        }
                        if(checkExpireDate == false) {
                            String sCertHash = CommonFunction.getThumbprintCertificate(sCert);
                            request.getSession(false).setAttribute("sessCertSSLToken", sCert);
                            request.getSession(false).setAttribute("sessHashSSLToken", sCertHash);
                            strView = "0#" + sCertHash;
                        } else {
                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERRORCTS + "#0";
                        }
                    } else {
                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERRORCTS + "#0";
                    }
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_NO_CERTCHAN + "#0";
                }
            } else {
                strView = "1#0";
            }
            out.println(strView);
        }
    }

    private String getPemFromCertificate(X509Certificate extractCertificate) throws CertificateEncodingException {
        // TODO Auto-generated method stub
        if (extractCertificate != null) {
            log.info("getPemFromCertificate: Have");
            byte[] certEncoded = extractCertificate.getEncoded();
            return DatatypeConverter.printBase64Binary(certEncoded);
        }
        return "";
    }

    protected X509Certificate extractCertificate(HttpServletRequest req) {
        log.info("X509Certificate-Begin");
        X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");
        if (null != certs && certs.length > 0) {
            log.info("X509Certificate-Length: " + certs.length);
            return certs[0];
        } else {
            log.info("X509Certificate-Length: NULL");
            return null;
        }
//        throw new RuntimeException("No X.509 client certificate found in request");
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
            log.error(e.getMessage(), e);
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
            log.error(e.getMessage(), e);
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
