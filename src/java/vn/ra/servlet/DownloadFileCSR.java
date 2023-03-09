package vn.ra.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.cert.Certificate;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRFile;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.object.FILE_MANAGER;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectFileToPartner;
import vn.ra.process.ConnectJackRabbitNew;
import vn.ra.process.JackRabbitCommon;
import vn.ra.utility.Config;
import vn.ra.utility.PropertiesContent;

/**
 *
 * @author THANH-PC
 */
public class DownloadFileCSR extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadFileCSR.class);
    private static final long serialVersionUID = 6106269076155338045L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.lang.ClassNotFoundException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession sessionsa = request.getSession(false);
            ConnectDatabase db = new ConnectDatabase();
            String strView = "";
            FileOutputStream fileOuputStream = null;
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
                    String idParam = EscapeUtils.CheckTextNull(request.getParameter("idParam"));
                    String pSessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                    String pSessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                    Config conf = new Config();
                    if (null != idParam) {
                        switch (idParam) {
                            case "csrhasid": {
                                //<editor-fold defaultstate="collapsed" desc="Gen File CSR Has ID">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    if (!"".equals(id)) {
                                        CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(id, pSessLanguage, rsPgin);
//                                    AgreementSingle[][] rsPgin = new AgreementSingle[1][];
//                                    com.getSignServerDetail(EscapeUtils.escapeHtml(id), "", "", is_TypeDB, rsPgin, tempParamSQL);
                                        String sCSR = "";
                                        String sFileName_Extend = "";
                                        if (rsPgin[0].length > 0) {
//                                            sTOKEN_SN = EscapeUtils.CheckTextNull(rsPgin[0][0].TOKEN_SN);
                                            sCSR = EscapeUtils.CheckTextNull(rsPgin[0][0].CSR);
                                            sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].TAX_CODE);
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].BUDGET_CODE);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].DECISION);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_ID);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].PASSPORT);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_EID);
                                            }
                                        }
                                        if (!"".equals(sCSR) && !"".equals(sFileName_Extend)) {
                                            String sNameFile = sFileName_Extend + Definitions.CONFIG_FILE_EXTENDTION_CSR;
//                                            String queryString = getServletContext().getRealPath("/");
//                                            String outputDirectory = queryString; outputDirectory
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                            byte[] sXML = sCSR.getBytes(Definitions.CONFIG_UNICODE_UTF_8);
                                            if ((new File(absoluteDiskPath)).exists()) {
                                                (new File(absoluteDiskPath)).delete();
                                            }
                                            fileOuputStream = new FileOutputStream(absoluteDiskPath);
                                            fileOuputStream.write(sXML);
                                            strView = "0#" + sNameFile.trim();
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "csrnoid": {
                                //<editor-fold defaultstate="collapsed" desc="Gen File CSR No ID">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String idCSR = EscapeUtils.CheckTextNull(request.getParameter("idCSR"));
                                    if (!"".equals(idCSR)) {
                                        File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                        if (!directory.exists()){
                                            directory.mkdir();
                                        }
                                        String sSigner = EscapeUtils.CheckTextNull(request.getParameter("Signer"));
                                        String sNameFile = sSigner + Definitions.CONFIG_FILE_EXTENDTION_CSR;
                                        String queryString = getServletContext().getRealPath("/");
                                        String outputDirectory = queryString;
                                        String absoluteDiskPath = outputDirectory + conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                        byte[] sXML = idCSR.getBytes(Definitions.CONFIG_UNICODE_UTF_8);
                                        if ((new File(absoluteDiskPath)).exists()) {
                                            (new File(absoluteDiskPath)).delete();
                                        }
                                        fileOuputStream = new FileOutputStream(absoluteDiskPath);
                                        fileOuputStream.write(sXML);
                                        strView = "0#" + sNameFile.trim();
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "certhasid": {
                                //<editor-fold defaultstate="collapsed" desc="Gen File CERTIFICATE HAS ID">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    if (!"".equals(id)) {
                                        String sCert = "";
                                        String sCERT_SN = "";
                                        String sFileName_Extend = "";
//                                        int sCertPurposeID = 0;
                                        int intPKI_FORMFACTOR_ID = 0;
                                        boolean booPRIVATE_KEY_ENABLED = false;
                                        CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(id, pSessLanguage, rsPgin);
                                        if (rsPgin[0].length > 0) {
                                            intPKI_FORMFACTOR_ID = rsPgin[0][0].PKI_FORMFACTOR_ID;
                                            booPRIVATE_KEY_ENABLED = rsPgin[0][0].PRIVATE_KEY_ENABLED;
//                                            sCertPurposeID = rsPgin[0][0].CERTIFICATION_PURPOSE_ID;
                                            sCERT_SN = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION_SN);
                                            sCert = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION);
                                            sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].TAX_CODE);
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].BUDGET_CODE);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].DECISION);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_ID);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].PASSPORT);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_EID);
                                            }
                                        }
                                        if (!"".equals(sCERT_SN)) {
                                            if(!"".equals(sFileName_Extend)){
                                                sFileName_Extend = "_" + sFileName_Extend;
                                            }
                                            String sNameFile = sCERT_SN + sFileName_Extend;
                                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                            boolean isCertP12 = false;
                                            if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                            {
                                                if (booPRIVATE_KEY_ENABLED == true) {
                                                    isCertP12 = true;
                                                }
                                            }
                                            if(isCertP12 == false)
                                            {
                                                if(!"".equals(sCert)) {
                                                    if (sCert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
                                                        sCert = sCert.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN, "");
                                                    }
                                                    if (sCert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
                                                        sCert = sCert.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END, "");
                                                    }
                                                    sNameFile = sNameFile + Definitions.CONFIG_FILE_EXTENDTION_CERT;
                                                    byte[] sXML = DatatypeConverter.parseBase64Binary(sCert);
        //                                            byte[] sXML = sCert.getBytes(Definitions.CONFIG_UNICODE_UTF_8);
                                                    if ((new File(absoluteDiskPath+sNameFile)).exists()) {
                                                        (new File(absoluteDiskPath + sNameFile)).delete();
                                                    }
                                                    fileOuputStream = new FileOutputStream(absoluteDiskPath + sNameFile);
                                                    fileOuputStream.write(sXML);
                                                    strView = "0#" + sNameFile.trim();
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                                }
                                            } else {
                                                sNameFile = sNameFile + Definitions.CONFIG_FILE_EXTENDTION_P12;
                                                int[] intRes = new int[1];
                                                String[] sRes = new String[1];
                                                // AFTER_EDIT
                                                String strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                                byte[] sByteFile = ConnectConnector.generateKeystore(strPasswordP12, true, id, intRes, sRes);
                                                if(intRes[0] == 0)
                                                {
                                                    CommonFunction.LogDebugString(log, "DownFile CERT P12 - Length", String.valueOf(sByteFile.length));
                                                    if ((new File(absoluteDiskPath+sNameFile)).exists()) {
                                                        (new File(absoluteDiskPath+sNameFile)).delete();
                                                    }
                                                    fileOuputStream = new FileOutputStream(absoluteDiskPath + sNameFile);
                                                    fileOuputStream.write(sByteFile);
                                                    strView = "0#" + sNameFile.trim();
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                                }
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "p12certhasid": {
                                //<editor-fold defaultstate="collapsed" desc="p12certhasid">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    if (!"".equals(id)) {
                                        String sCert = "";
                                        String sCERT_SN = "";
                                        String sFileName_Extend = "";
//                                        int intPKI_FORMFACTOR_ID = 0;
//                                        boolean booPRIVATE_KEY_ENABLED = false;
                                        CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(id, pSessLanguage, rsPgin);
                                        if (rsPgin[0].length > 0) {
//                                            intPKI_FORMFACTOR_ID = rsPgin[0][0].PKI_FORMFACTOR_ID;
//                                            booPRIVATE_KEY_ENABLED = rsPgin[0][0].PRIVATE_KEY_ENABLED;
                                            sCERT_SN = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION_SN);
                                            sCert = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION);
                                            sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].TAX_CODE);
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].BUDGET_CODE);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].DECISION);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_ID);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].PASSPORT);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_EID);
                                            }
                                        }
                                        if (!"".equals(sCERT_SN)) {
                                            if(!"".equals(sFileName_Extend)){
                                                sFileName_Extend = "_" + sFileName_Extend;
                                            }
                                            String sNameFile = sCERT_SN + sFileName_Extend;
                                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
//                                            boolean isCertP12 = false;
//                                            if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
//                                            {
//                                                if (booPRIVATE_KEY_ENABLED == true) {
//                                                    isCertP12 = true;
//                                                }
//                                            }
//                                            if(isCertP12 == false) {
                                            if(!"".equals(sCert)) {
                                                if (sCert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
                                                    sCert = sCert.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN, "");
                                                }
                                                if (sCert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
                                                    sCert = sCert.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END, "");
                                                }
                                                sNameFile = sNameFile + Definitions.CONFIG_FILE_EXTENDTION_CERT;
                                                byte[] sXML = DatatypeConverter.parseBase64Binary(sCert);
    //                                            byte[] sXML = sCert.getBytes(Definitions.CONFIG_UNICODE_UTF_8);
                                                if ((new File(absoluteDiskPath+sNameFile)).exists()) {
                                                    (new File(absoluteDiskPath + sNameFile)).delete();
                                                }
                                                fileOuputStream = new FileOutputStream(absoluteDiskPath + sNameFile);
                                                fileOuputStream.write(sXML);
                                                strView = "0#" + sNameFile.trim();
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                            }
//                                            } else {
//                                                sNameFile = sNameFile + Definitions.CONFIG_FILE_EXTENDTION_P12;
//                                                int[] intRes = new int[1];
//                                                String[] sRes = new String[1];
//                                                // AFTER_EDIT
//                                                String strPasswordP12 = CommonFunction.randomPasswordP12(8);
//                                                byte[] sByteFile = ConnectConnector.generateKeystore(strPasswordP12, true, id, intRes, sRes);
//                                                if(intRes[0] == 0)
//                                                {
//                                                    CommonFunction.LogDebugString(log, "DownFile CERT P12 - Length", String.valueOf(sByteFile.length));
//                                                    if ((new File(absoluteDiskPath+sNameFile)).exists()) {
//                                                        (new File(absoluteDiskPath+sNameFile)).delete();
//                                                    }
//                                                    fileOuputStream = new FileOutputStream(absoluteDiskPath + sNameFile);
//                                                    fileOuputStream.write(sByteFile);
//                                                    strView = "0#" + sNameFile.trim();
//                                                } else {
//                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
//                                                }
//                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "pemcerthasid": {
                                //<editor-fold defaultstate="collapsed" desc="Gen File Pem CERTIFICATE HAS ID">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    if (!"".equals(id)) {
                                        String sCert = "";
                                        String sCERT_SN = "";
                                        String sFileName_Extend = "";
                                        int intPKI_FORMFACTOR_ID = 0;
                                        boolean booPRIVATE_KEY_ENABLED = false;
                                        CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(id, pSessLanguage, rsPgin);
                                        if (rsPgin[0].length > 0) {
                                            intPKI_FORMFACTOR_ID = rsPgin[0][0].PKI_FORMFACTOR_ID;
                                            booPRIVATE_KEY_ENABLED = rsPgin[0][0].PRIVATE_KEY_ENABLED;
                                            sCERT_SN = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION_SN);
                                            sCert = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION);
                                            sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].TAX_CODE);
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].BUDGET_CODE);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].DECISION);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_ID);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].PASSPORT);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_EID);
                                            }
                                        }
                                        boolean downEnable = true;
                                        if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                        {
                                            if (booPRIVATE_KEY_ENABLED == true) {
                                                downEnable = false;
                                            }
                                        }
                                        if (!"".equals(sCert) && !"".equals(sCERT_SN) && downEnable == true) {
                                            if (!sCert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
                                                sCert = Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN + "\n" + sCert;
                                            }
                                            if (!sCert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
                                                sCert = sCert+ "\n"+Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END;;
                                            }
                                            if(!"".equals(sFileName_Extend)){
                                                sFileName_Extend = "_" + sFileName_Extend;
                                            }
                                            String sNameFile = sCERT_SN + sFileName_Extend + Definitions.CONFIG_FILE_EXTENDTION_PEM;
                                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
//                                            byte[] sXML = DatatypeConverter.parseBase64Binary(sCert);
                                            byte[] sXML = sCert.getBytes(Definitions.CONFIG_UNICODE_UTF_8);
                                            if ((new File(absoluteDiskPath)).exists()) {
                                                (new File(absoluteDiskPath)).delete();
                                            }
                                            fileOuputStream = new FileOutputStream(absoluteDiskPath);
                                            fileOuputStream.write(sXML);
                                            strView = "0#" + sNameFile.trim();
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "certhasid_approve": {
                                //<editor-fold defaultstate="collapsed" desc="Gen File CERTIFICATE APRROVE HAS ID">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    if (!"".equals(id)) {
                                        String sCert = "";
                                        String sCERT_SN = "";
                                        String sFileName_Extend = "";
                                        String sCSR = "";
                                        int sCertPurposeID = 0;
                                        int intPKI_FORMFACTOR_ID = 0;
                                        boolean booPRIVATE_KEY_ENABLED = false;
                                        CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_APPROVED_DETAIL(id, pSessLanguage, rsPgin);
                                        if (rsPgin[0].length > 0) {
                                            sCertPurposeID = rsPgin[0][0].CERTIFICATION_PURPOSE_ID;
                                            intPKI_FORMFACTOR_ID = rsPgin[0][0].PKI_FORMFACTOR_ID;
                                            booPRIVATE_KEY_ENABLED = rsPgin[0][0].PRIVATE_KEY_ENABLED;
                                            sCSR = EscapeUtils.CheckTextNull(rsPgin[0][0].CSR);
                                            sCERT_SN = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION_SN);
                                            sCert = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION);
                                            sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].TAX_CODE);
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].BUDGET_CODE);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].DECISION);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_ID);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].PASSPORT);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_EID);
                                            }
                                        }
                                        if (!"".equals(sCert) && !"".equals(sCERT_SN)) {
                                            if(!"".equals(sFileName_Extend)){
                                                sFileName_Extend = "_" + sFileName_Extend;
                                            }
                                            String sNameFile = sCERT_SN + sFileName_Extend + Definitions.CONFIG_FILE_EXTENDTION_CERT;
                                            if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                            {
                                                if (booPRIVATE_KEY_ENABLED == true) {
                                                    sNameFile = sCERT_SN + "_" + sFileName_Extend + Definitions.CONFIG_FILE_EXTENDTION_P12;
                                                }
                                            }
//                                            String queryString = getServletContext().getRealPath("/");
//                                            String outputDirectory = queryString; outputDirectory
                                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                            byte[] sXML = DatatypeConverter.parseBase64Binary(sCert);
//                                            byte[] sXML = sCert.getBytes(Definitions.CONFIG_UNICODE_UTF_8);
                                            if ((new File(absoluteDiskPath)).exists()) {
                                                (new File(absoluteDiskPath)).delete();
                                            }
                                            fileOuputStream = new FileOutputStream(absoluteDiskPath);
                                            fileOuputStream.write(sXML);
                                            strView = "0#" + sNameFile.trim();
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "hisexportnoid": {
                                //<editor-fold defaultstate="collapsed" desc="Check File HisExport No ID">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    if (!"".equals(id)) {
                                        String sFileName;
                                        int index = id.lastIndexOf("\\");
                                        if (index == -1) {
                                            index = id.lastIndexOf("/");
                                        }
                                        sFileName = id.substring(++index);
                                        File f = new File(id);
                                        if (!f.exists()) {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                        } else {
                                            strView = "0#" + sFileName.trim();
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "crlhasid": {
                                //<editor-fold defaultstate="collapsed" desc="crlhasid">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("CAID"));
                                    if (!"".equals(id)) {
                                        CERTIFICATION_AUTHORITY[][] rsPgin = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(EscapeUtils.escapeHtml(id), rsPgin);
                                        byte[] sXML = null;
                                        String sCA_URI = "";
                                        if (rsPgin[0].length > 0) {
                                            sCA_URI = EscapeUtils.CheckTextNull(rsPgin[0][0].CA_URI);
                                            sXML = rsPgin[0][0].CRL_BLOB;
                                        }
                                        if (sXML != null && !"".equals(sCA_URI)) {
                                            if (!sCA_URI.contains(Definitions.CONFIG_FILE_EXTENDTION_CRL)) {
                                                sCA_URI = sCA_URI + Definitions.CONFIG_FILE_EXTENDTION_CRL;
                                            }
                                            String sNameFile = sCA_URI;
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String absoluteDiskPath = outputDirectory + conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                            if ((new File(absoluteDiskPath)).exists()) {
                                                (new File(absoluteDiskPath)).delete();
                                            }
                                            fileOuputStream = new FileOutputStream(absoluteDiskPath);
                                            fileOuputStream.write(sXML);
                                            strView = "0#" + sNameFile.trim();
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "downloadpdflicense": {
                                //<editor-fold defaultstate="collapsed" desc="downloadpdflicense">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    if (!"".equals(id)) {
//                                        String sStringLicense = "";
                                        String sCERT_SN = "";
                                        byte[] byteLicense = null;
                                        Blob blobLicense = null;
                                        String sFileName_Extend = "";
                                        CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(id, pSessLanguage, rsPgin);
                                        if (rsPgin[0].length > 0) {
//                                            blobLicense = rsPgin[0][0].CERTIFICATION_LICENSE;
//                                            blobLicense = rsPgin[0][0].CUSTOMER_CONFIRMATION;
                                            byteLicense = blobLicense.getBytes(1, (int) blobLicense.length());
//                                            sStringLicense = new String(byteLicense);
                                            sCERT_SN = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION_SN);
                                            sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].TAX_CODE);
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].BUDGET_CODE);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].DECISION);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_ID);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].PASSPORT);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_EID);
                                            }
                                        }
                                        if (blobLicense != null && !"".equals(sCERT_SN)) {
                                            if(!"".equals(sFileName_Extend)){
                                                sFileName_Extend = "_" + sFileName_Extend;
                                            }
                                            String sNameFile = sCERT_SN + sFileName_Extend + Definitions.CONFIG_FILE_EXTENDTION_PDF;
                                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                            File someFile = new File(absoluteDiskPath);
                                            try (FileOutputStream fos = new FileOutputStream(someFile)) {
                                                fos.write(byteLicense);
                                                fos.flush();
                                            }
//                                            byte[] sXML = sStringLicense.getBytes(Definitions.CONFIG_UNICODE_UTF_8);
//                                            File files = new File(absoluteDiskPath);
//                                            if (files.exists()) {
//                                                files.delete();
//                                            }
//                                            byteLicense = CommonFunction.saveByteArrayOutputStream(blobLicense.getBinaryStream());
//                                            fileOuputStream = new FileOutputStream(absoluteDiskPath);
//                                            fileOuputStream.write(byteLicense);
//                                            files.setReadable(true);
//                                            files.setWritable(true);
//                                            try (FileOutputStream fos = new FileOutputStream(files, false); BufferedOutputStream outputStream = new BufferedOutputStream(fos)) {
//                                                outputStream.write(byteLicense);
//                                            }
                                            CommonFunction.LogDebugString(log, "File-PDF-Signature", absoluteDiskPath);

//                                            fileOuputStream = new FileOutputStream(absoluteDiskPath);
//                                            fileOuputStream.write(byteLicense);
                                            strView = "0#" + sNameFile.trim();
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "downloadfilemanager": {
                                //<editor-fold defaultstate="collapsed" desc="downloadfilemanager">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String pFILE_MANAGER_ID = EscapeUtils.CheckTextNull(request.getParameter("pFILE_MANAGER_ID"));
                                    if (!"".equals(pFILE_MANAGER_ID)) {
                                        byte[] byteLicense = null;
                                        String sFileName = "";
                                        FILE_MANAGER[][] rsFILE_MANAGER = new FILE_MANAGER[1][];
                                        db.S_BO_FILE_MANAGER_DETAIL(pFILE_MANAGER_ID, pSessLanguage, rsFILE_MANAGER);
                                        if (rsFILE_MANAGER[0].length > 0) {
                                            String sUUID = EscapeUtils.CheckTextNull(rsFILE_MANAGER[0][0].UUID);
                                            sFileName = EscapeUtils.CheckTextNull(rsFILE_MANAGER[0][0].FILE_NAME);
                                            String sJRBConfig = EscapeUtils.CheckTextNull(rsFILE_MANAGER[0][0].DMS_PROPERTIES);
                                            // Insert File JackRabbit
                                            String sJRB_Source =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
                                            if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY))
                                            {
                                                String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
                                                String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
                                                String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
                                                String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
                                                String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
                                                String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
                                                String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
                                                String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
                                                String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_DOWN);
                                                CloseableHttpResponse pHttpRes = ConnectFileToPartner.loadFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                                    sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                                    sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, sUUID);
                                                Header sFilename = pHttpRes.getFirstHeader("Location");
                                                String sNameFile = sFilename.getValue();
                                                InputStream data = pHttpRes.getEntity().getContent();
                                                try {
                                                    String sFileLocation = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                                    if (!directory.exists()) {
                                                        directory.mkdir();
                                                    }
                                                    CommonFunction.LogDebugString(log, "FILE-JACKRABBIT-PARTNER", sFileLocation);
                                                    OutputStream output = new FileOutputStream(sFileLocation);
                                                    try {
                                                        ByteStreams.copy(data, output);
                                                    } finally {
                                                    }
                                                } finally {
                                                }
                                                strView = "0#" + sNameFile;
                                            } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
                                                String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                JCRFile jrbFile = JackRabbitCommon.getInstance(jcrConfig).downloadFile(sUUID);
                                                if(jrbFile != null) {
                                                    if (jrbFile.getStream() != null) {
                                                        byteLicense = IOUtils.toByteArray(jrbFile.getStream());
                                                    }
                                                }
                                                if (byteLicense != null) {
                                                    String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sFileName;
                                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                                    if (!directory.exists()) {
                                                        directory.mkdir();
                                                    }
                                                    File someFile = new File(absoluteDiskPath);
                                                    try (FileOutputStream fos = new FileOutputStream(someFile)) {
                                                        fos.write(byteLicense);
                                                        fos.flush();
                                                    }
                                                    strView = "0#" + sFileName;
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                                }
                                            } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
                                                String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
//                                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
//                                                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//                                                JCRFile jrbFile = JackRabbitCommon.downloadFile(jcrConfig, sUUID);
                                                ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                                                    Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                vn.mobileid.fms.client.JCRFile jrbFile = openJRB.downloadFile(sUUID);
                                                if (jrbFile.getStream() != null) {
                                                    byteLicense = IOUtils.toByteArray(jrbFile.getStream());
                                                }
                                                if (byteLicense != null) {
                                                    String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sFileName;
                                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                                    if (!directory.exists()){
                                                        directory.mkdir();
                                                    }
                                                    File someFile = new File(absoluteDiskPath);
                                                    try (FileOutputStream fos = new FileOutputStream(someFile)) {
                                                        fos.write(byteLicense);
                                                        fos.flush();
                                                    }
                                                    strView = "0#" + sFileName;
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                                }
                                            } else {
                                            }
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "downloadlicenseconvertjrb": {
                                //<editor-fold defaultstate="collapsed" desc="downloadlicenseconvertjrb">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    if (!"".equals(id)) {
                                        FILE_MANAGER[][] rsFileMana = new FILE_MANAGER[1][];
                                        db.S_BO_FILE_MANAGER_GET_BY_CERTIFICATION(id, pSessLanguage, rsFileMana);
                                        String sUUID = "";
                                        String sJRBConfig = "";
                                        String sFileName = "";
                                        if (rsFileMana[0].length > 0) {
                                            for (FILE_MANAGER rsFile : rsFileMana[0]) {
                                                if (rsFile.FILE_PROFILE_NAME.equals(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT)) {
                                                    sUUID = EscapeUtils.CheckTextNull(rsFile.UUID);
                                                    sJRBConfig = EscapeUtils.CheckTextNull(rsFile.DMS_PROPERTIES);
                                                    sFileName = EscapeUtils.CheckTextNull(rsFile.FILE_NAME);
                                                    break;
                                                }
                                            }
                                        }
//                                        if ("".equals(sUUID)) {
//                                            String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
//                                            String sCERT_SN = "";
//                                            byte[] byteLicense = null;
//                                            Blob blobLicense = null;
//                                            String sFileName_Extend = "";
//                                            CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
//                                            db.S_BO_CERTIFICATION_DETAIL(id, pSessLanguage, rsPgin);
//                                            if (rsPgin[0].length > 0) {
//                                                blobLicense = rsPgin[0][0].CERTIFICATION_LICENSE;
//                                                byteLicense = blobLicense.getBytes(1, (int) blobLicense.length());
//                                                sCERT_SN = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION_SN);
//                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].TAX_CODE);
//                                                if ("".equals(sFileName_Extend)) {
//                                                    sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].BUDGET_CODE);
//                                                }
//                                                if ("".equals(sFileName_Extend)) {
//                                                    sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_ID);
//                                                }
//                                                if ("".equals(sFileName_Extend)) {
//                                                    sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].PASSPORT);
//                                                }
//                                            }
//                                            // Config JackRabbit
//                                            GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
//                                            db.S_BO_GENERAL_POLICY_LIST("1", rsPolicy);
//                                            if (rsPolicy[0].length > 0) {
//                                                for (GENERAL_POLICY rsPolicy1 : rsPolicy[0]) {
//                                                    if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_DMS_PROPERTIES_CURRENT)) {
//                                                        sJRBConfig = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                            String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
//                                            String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
//                                            String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
//                                            String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
//                                            String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
//                                            String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
//                                            String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
//                                            if (blobLicense != null && !"".equals(sCERT_SN)) {
//                                                int intSize = byteLicense.length;
//                                                sFileName = sCERT_SN + "_" + sFileName_Extend + Definitions.CONFIG_FILE_EXTENDTION_PDF;
//                                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
//                                                        Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//                                                InputStream isFILE_STREAM = new ByteArrayInputStream(byteLicense);
//                                                JCRFile jrbFile = JackRabbitCommon.uploadFile(jcrConfig, sFileName, Definitions.CONFIG_MIMETYPE_PDF, isFILE_STREAM);
//                                                int[] pFILE_MANAGER_ID = new int[1];
//                                                db.S_BO_FILE_MANAGER_INSERT(Definitions.CONFIG_FILE_PROFILE_CODE_E_CONTRACT, jrbFile.getUuid(),
//                                                        sJRBConfig, Definitions.CONFIG_MIMETYPE_PDF, jrbFile.getFileName(),
//                                                        intSize, id, loginUID, pFILE_MANAGER_ID);
//                                                String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sFileName;
//                                                File someFile = new File(absoluteDiskPath);
//                                                try (FileOutputStream fos = new FileOutputStream(someFile)) {
//                                                    fos.write(byteLicense);
//                                                    fos.flush();
//                                                }
//                                                CommonFunction.LogDebugString(log, "FILE-JACKRABBIT", absoluteDiskPath);
//                                                strView = "0#" + sFileName.trim();
//                                            } else {
//                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
//                                            }
//                                        } else {
                                            // Insert File JackRabbit
                                            byte[] byteLicense = null;
//                                            CommonFunction.LogDebugString(log, "downloadlicenseconvertjrb-DMS", "DMS_FILE: " + sJRBConfig);
                                            String sJRB_Source =  PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
//                                            CommonFunction.LogDebugString(log, "downloadlicenseconvertjrb-DMS-SOURCE", "sJRB_Source: " + sJRB_Source);
                                            if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY))
                                            {
                                                String sIP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_IP);
                                                String sHTTP_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PROTOCOL);
                                                String sCONTEXT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_CONTEXT);
                                                String sPORT_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PORT);
                                                String sDEFAULT_USER = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERNAME);
                                                String sDEFAULT_PASS = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PASSWORD);
                                                String sOWNERCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_OWNERCODE);
                                                String sAPPCODE_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_APPCODE);
                                                String sFUNCTION_CONNECT = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_FUNCTION_DOWN);
                                                CloseableHttpResponse pHttpRes = ConnectFileToPartner.loadFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                                    sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                                    sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT, sUUID);
                                                Header sFilename = pHttpRes.getFirstHeader("Location");
                                                String sNameFile = sFilename.getValue();
                                                InputStream data = pHttpRes.getEntity().getContent();
                                                try {
                                                    String sFileLocation = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                                    if (!directory.exists()){
                                                        directory.mkdir();
                                                    }
                                                    CommonFunction.LogDebugString(log, "FILE-JACKRABBIT-PARTNER", sFileLocation);
                                                    OutputStream output = new FileOutputStream(sFileLocation);
                                                    try {
                                                        ByteStreams.copy(data, output);
                                                    } finally {
                                                    }
                                                } finally {
                                                }
                                                strView = "0#" + sNameFile;
                                            } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
                                                String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                                                CommonFunction.LogDebugString(log, "downloadlicenseconvertjrb-Request", "sJRB_Host: " + sJRB_Host
                                                    + "; sJRB_UserID: " + sJRB_UserID
                                                    + "; sJRB_UserPass: " + sJRB_UserPass + "; sJRB_MaxSession: " + sJRB_MaxSession
                                                    + "; sJRB_MaxFileFolder: " + sJRB_MaxFileFolder
                                                    + "; sJRB_PrefixFolder: " + sJRB_PrefixFolder + "; sJRB_WorkSpace: " + sJRB_WorkSpace);
                                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
                                                        Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                JCRFile jrbFile = JackRabbitCommon.getInstance(jcrConfig).downloadFile(sUUID);
                                                if (jrbFile != null) {
                                                    if (jrbFile.getStream() != null) {
                                                        byteLicense = IOUtils.toByteArray(jrbFile.getStream());
                                                    }
                                                }
                                                if (byteLicense != null) {
                                                    String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sFileName;
                                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                                    if (!directory.exists()){
                                                        directory.mkdir();
                                                    }
                                                    File someFile = new File(absoluteDiskPath);
                                                    try (FileOutputStream fos = new FileOutputStream(someFile)) {
                                                        fos.write(byteLicense);
                                                        fos.flush();
                                                    }
                                                    CommonFunction.LogDebugString(log, "FILE-JACKRABBIT", absoluteDiskPath);
                                                    strView = "0#" + sFileName;
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                                }
                                            } else if(sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
                                                String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                                                String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                                                String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                                                String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                                                String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                                                String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                                                String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
//                                                CommonFunction.LogDebugString(log, "downloadlicenseconvertjrb-Request", "sJRB_Host: " + sJRB_Host
//                                                    + "; sJRB_UserID: " + sJRB_UserID
//                                                    + "; sJRB_UserPass: " + sJRB_UserPass + "; sJRB_MaxSession: " + sJRB_MaxSession
//                                                    + "; sJRB_MaxFileFolder: " + sJRB_MaxFileFolder
//                                                    + "; sJRB_PrefixFolder: " + sJRB_PrefixFolder + "; sJRB_WorkSpace: " + sJRB_WorkSpace);
//                                                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
//                                                        Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//                                                JCRFile jrbFile = JackRabbitCommon.downloadFile(jcrConfig, sUUID);
                                                
                                                ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                                                    Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                                                vn.mobileid.fms.client.JCRFile jrbFile = openJRB.downloadFile(sUUID);
                                                if (jrbFile.getStream() != null) {
                                                    byteLicense = IOUtils.toByteArray(jrbFile.getStream());
                                                }
                                                if (byteLicense != null) {
                                                    String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sFileName;
                                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                                    if (!directory.exists()){
                                                        directory.mkdir();
                                                    }
                                                    File someFile = new File(absoluteDiskPath);
                                                    try (FileOutputStream fos = new FileOutputStream(someFile)) {
                                                        fos.write(byteLicense);
                                                        fos.flush();
                                                    }
                                                    CommonFunction.LogDebugString(log, "FILE-JACKRABBIT", absoluteDiskPath);
                                                    strView = "0#" + sFileName;
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                                }
                                            } else {
                                            }
//                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "savefileimportpush": {
                                //<editor-fold defaultstate="collapsed" desc="savefileimportpush">
                                String sXML = sessionsa.getAttribute("sessTokenImportFailed").toString().trim();
                                String sNameFile = CommonFunction.generateNumberDays() + Definitions.CONFIG_FILE_EXTENDTION_TEXT;
                                String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                if (!directory.exists()){
                                    directory.mkdir();
                                }
                                if ((new File(absoluteDiskPath)).exists()) {
                                    (new File(absoluteDiskPath)).delete();
                                }
                                fileOuputStream = new FileOutputStream(absoluteDiskPath);
                                fileOuputStream.write(sXML.getBytes());
                                sessionsa.setAttribute("sessTokenImportFailed", null);
                                strView = "0#" + sNameFile.trim();
                                //</editor-fold>
                                break;
                            }
                            case "savefileimportcontactprofile": {
                                //<editor-fold defaultstate="collapsed" desc="savefileimportcontactprofile">
                                String sXML = sessionsa.getAttribute("sessProfileContactImportFailed").toString().trim();
                                String sNameFile = CommonFunction.generateNumberDays() + Definitions.CONFIG_FILE_EXTENDTION_TEXT;
                                String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                if (!directory.exists()){
                                    directory.mkdir();
                                }
                                if ((new File(absoluteDiskPath)).exists()) {
                                    (new File(absoluteDiskPath)).delete();
                                }
                                fileOuputStream = new FileOutputStream(absoluteDiskPath);
                                fileOuputStream.write(sXML.getBytes());
                                sessionsa.setAttribute("sessProfileContactImportFailed", null);
                                strView = "0#" + sNameFile.trim();
                                //</editor-fold>
                                break;
                            }
                            case "savefileimporterror": {
                                //<editor-fold defaultstate="collapsed" desc="savefileimporterror">
                                String sXML = sessionsa.getAttribute("sessTokenImportFailed").toString().trim();
                                String sNameFile = CommonFunction.generateNumberDays() + Definitions.CONFIG_FILE_EXTENDTION_TEXT;
                                String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                if (!directory.exists()) {
                                    directory.mkdir();
                                }
                                if ((new File(absoluteDiskPath)).exists()) {
                                    (new File(absoluteDiskPath)).delete();
                                }
                                fileOuputStream = new FileOutputStream(absoluteDiskPath);
                                fileOuputStream.write(sXML.getBytes());
//                                sessionsa.setAttribute("sessTokenImportFailed", null);
                                strView = "0#" + sNameFile.trim();
                                //</editor-fold>
                                break;
                            }
                            case "savefileimportDisallowance": {
                                //<editor-fold defaultstate="collapsed" desc="savefileimportDisallowance">
                                String sXML = sessionsa.getAttribute("sessTokenImportFailed").toString().trim();
                                String sNameFile = CommonFunction.generateNumberDays() + Definitions.CONFIG_FILE_EXTENDTION_TEXT;
                                String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                if ((new File(absoluteDiskPath)).exists()) {
                                    (new File(absoluteDiskPath)).delete();
                                }
                                fileOuputStream = new FileOutputStream(absoluteDiskPath);
                                fileOuputStream.write(sXML.getBytes());
                                sessionsa.setAttribute("sessTokenImportFailed", null);
                                strView = "0#" + sNameFile.trim();
                                //</editor-fold>
                                break;
                            }
                            case "savefileimporttokenaction": {
                                //<editor-fold defaultstate="collapsed" desc="savefileimporttokenaction">
                                String sXML = sessionsa.getAttribute("sessTokenActionImportFailed").toString().trim();
                                String sNameFile = CommonFunction.generateNumberDays() + Definitions.CONFIG_FILE_EXTENDTION_TEXT;
                                String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                if ((new File(absoluteDiskPath)).exists()) {
                                    (new File(absoluteDiskPath)).delete();
                                }
                                fileOuputStream = new FileOutputStream(absoluteDiskPath);
                                fileOuputStream.write(sXML.getBytes());
                                sessionsa.setAttribute("sessTokenActionImportFailed", null);
                                strView = "0#" + sNameFile.trim();
                                //</editor-fold>
                                break;
                            }
                            case "savefileimportregistercert": {
                                //<editor-fold defaultstate="collapsed" desc="savefileimportregistercert">
                                String sXML = sessionsa.getAttribute("sessRegisterCertImportFailed").toString().trim();
                                String sNameFile = CommonFunction.generateNumberDays() + Definitions.CONFIG_FILE_EXTENDTION_TEXT;
                                String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                if ((new File(absoluteDiskPath)).exists()) {
                                    (new File(absoluteDiskPath)).delete();
                                }
                                fileOuputStream = new FileOutputStream(absoluteDiskPath);
                                fileOuputStream.write(sXML.getBytes());
                                sessionsa.setAttribute("sessRegisterCertImportFailed", null);
                                strView = "0#" + sNameFile.trim();
                                //</editor-fold>
                                break;
                            } 
                            case "sendmailp12soft": {
                                //<editor-fold defaultstate="collapsed" desc="sendmailp12soft">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    if(pSessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                    {
                                        String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                        if (!"".equals(id)) {
                                            CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                            db.S_BO_CERTIFICATION_DETAIL(id, pSessLanguage, rsPgin);
                                            if (rsPgin[0].length > 0) {
                                                if(rsPgin[0][0].PKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                {
                                                    if (rsPgin[0][0].PRIVATE_KEY_ENABLED == true) {
                                                        int[] intRes = new int[1];
                                                        String[] sRes = new String[1];
                                                        // AFTER_EDIT -> ok
                                                        String strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                                        ConnectConnector.generateKeystore(strPasswordP12, true, id, intRes, sRes);
                                                        if(intRes[0] == 0)
                                                        {
                                                            strView = "0#0";
                                                        } else {
                                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                        }
                                                    } else {
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                        CommonFunction.LogDebugString(log, "P12 Sending Error", "Certificate is not P12 type, please check again");
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                                    CommonFunction.LogDebugString(log, "P12 Sending Error", "Certificate is not SOFT_TOKEN form factor, please check again");
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "downloadp12soft": {
                                //<editor-fold defaultstate="collapsed" desc="downloadp12soft">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    if(pSessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                    {
                                        String SessRoleID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
                                        if(SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN) || SessRoleID.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                        {
                                            String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                            String sP12Password = EscapeUtils.CheckTextNull(request.getParameter("sP12Password"));
                                            if (!"".equals(id) && !"".equals(sP12Password)) {
                                                String sCERT_SN = "";
                                                String sFileName_Extend = "";
                                                int intPKI_FORMFACTOR_ID = 0;
                                                boolean booPRIVATE_KEY_ENABLED = false;
                                                CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                                db.S_BO_CERTIFICATION_DETAIL(id, pSessLanguage, rsPgin);
                                                if (rsPgin[0].length > 0) {
                                                    intPKI_FORMFACTOR_ID = rsPgin[0][0].PKI_FORMFACTOR_ID;
                                                    booPRIVATE_KEY_ENABLED = rsPgin[0][0].PRIVATE_KEY_ENABLED;
                                                    sCERT_SN = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION_SN);
                                                    sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].TAX_CODE);
                                                    if ("".equals(sFileName_Extend)) {
                                                        sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].BUDGET_CODE);
                                                    }
                                                    if ("".equals(sFileName_Extend)) {
                                                        sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].DECISION);
                                                    }
                                                    if ("".equals(sFileName_Extend)) {
                                                        sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_ID);
                                                    }
                                                    if ("".equals(sFileName_Extend)) {
                                                        sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].PASSPORT);
                                                    }
                                                    if ("".equals(sFileName_Extend)) {
                                                        sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_EID);
                                                    }
                                                    if ("".equals(sFileName_Extend)) {
                                                        sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].DOMAIN_NAME);
                                                    }
                                                }
                                                if (!"".equals(sCERT_SN)) {
                                                    if(!"".equals(sFileName_Extend)){
                                                        sFileName_Extend = "_" + sFileName_Extend;
                                                    }
                                                    String sNameFile = sCERT_SN + sFileName_Extend;
                                                    String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                                    if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                    {
                                                        if (booPRIVATE_KEY_ENABLED == true) {
                                                            sNameFile = sNameFile + Definitions.CONFIG_FILE_EXTENDTION_P12;
                                                            int[] intRes = new int[1];
                                                            String[] sRes = new String[1];
                                                            // AFTER_EDIT -> ok
                                                            byte[] sByteFile = ConnectConnector.generateKeystore(sP12Password, false, id, intRes, sRes);
                                                            if(intRes[0] == 0)
                                                            {
                                                                CommonFunction.LogDebugString(log, "DownFile CERT P12 - Length", String.valueOf(sByteFile.length));
                                                                if ((new File(absoluteDiskPath + sNameFile)).exists()) {
                                                                    (new File(absoluteDiskPath + sNameFile)).delete();
                                                                }
                                                                fileOuputStream = new FileOutputStream(absoluteDiskPath + sNameFile);
                                                                fileOuputStream.write(sByteFile);
                                                                strView = "0#" + sNameFile.trim();
                                                            } else {
                                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                            }
                                        }
                                    }
//                                } else {
//                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
//                                }
                                //</editor-fold>
                                break;
                            }
                            case "downloadcertp7p": {
                                //<editor-fold defaultstate="collapsed" desc="downloadcertp7p">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String id = EscapeUtils.CheckTextNull(request.getParameter("id"));
                                    if (!"".equals(id)) {
                                        String sCert = "";
                                        String sCERT_SN = "";
                                        String sFileName_Extend = "";
                                        int intCERTIFICATION_AUTHORITY_ID = 0;
                                        CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                        db.S_BO_CERTIFICATION_DETAIL(id, pSessLanguage, rsPgin);
                                        if (rsPgin[0].length > 0) {
                                            intCERTIFICATION_AUTHORITY_ID = rsPgin[0][0].CERTIFICATION_AUTHORITY_ID;
                                            sCERT_SN = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION_SN);
                                            sCert = EscapeUtils.CheckTextNull(rsPgin[0][0].CERTIFICATION);
                                            sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].TAX_CODE);
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].BUDGET_CODE);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].DECISION);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_ID);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].PASSPORT);
                                            }
                                            if ("".equals(sFileName_Extend)) {
                                                sFileName_Extend = EscapeUtils.CheckTextNull(rsPgin[0][0].P_EID);
                                            }
                                        }
                                        if (!"".equals(sCERT_SN) && intCERTIFICATION_AUTHORITY_ID != 0) {
                                            if(!"".equals(sFileName_Extend)){
                                                sFileName_Extend = "_" + sFileName_Extend;
                                            }
                                            String sNameFile = sCERT_SN + sFileName_Extend + Definitions.CONFIG_FILE_EXTENDTION_P7P;
                                            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()) {
                                                directory.mkdir();
                                            }
                                            CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                            db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(intCERTIFICATION_AUTHORITY_ID), rsCA);
                                            String sCertCa = "";
                                            if(rsCA[0].length > 0) {
                                                sCertCa = EscapeUtils.CheckTextNull(rsCA[0][0].CERTIFICATE);
                                            }
                                            if(!"".equals(sCertCa)) {
                                                List<Certificate> certList = CommonFunction.getChain(sCert, sCertCa);
                                                byte[] byteP7P = CommonFunction.getP7B(certList);
                                                fileOuputStream = new FileOutputStream(absoluteDiskPath + sNameFile);
                                                fileOuputStream.write(byteP7P);
                                                strView = "0#" + sNameFile.trim();
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                            }
//                                            boolean isCertP12 = false;
//                                            if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
//                                            {
//                                                if (booPRIVATE_KEY_ENABLED == true) {
//                                                    isCertP12 = true;
//                                                }
//                                            }
//                                            if(isCertP12 == false)
//                                            {
//                                                if(!"".equals(sCert)) {
//                                                    if (sCert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
//                                                        sCert = sCert.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN, "");
//                                                    }
//                                                    if (sCert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
//                                                        sCert = sCert.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END, "");
//                                                    }
//                                                    sNameFile = sNameFile + Definitions.CONFIG_FILE_EXTENDTION_CERT;
//                                                    byte[] sXML = DatatypeConverter.parseBase64Binary(sCert);
//                                                    if ((new File(absoluteDiskPath+sNameFile)).exists()) {
//                                                        (new File(absoluteDiskPath + sNameFile)).delete();
//                                                    }
//                                                    fileOuputStream = new FileOutputStream(absoluteDiskPath + sNameFile);
//                                                    fileOuputStream.write(sXML);
//                                                    strView = "0#" + sNameFile.trim();
//                                                } else {
//                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
//                                                }
//                                            } else {
//                                                sNameFile = sNameFile + Definitions.CONFIG_FILE_EXTENDTION_P7P;
//                                                int[] intRes = new int[1];
//                                                String[] sRes = new String[1];
//                                                // AFTER_EDIT
//                                                String strPasswordP12 = CommonFunction.randomPasswordP12(8);
//                                                byte[] sByteFile = ConnectConnector.generateKeystore(strPasswordP12, true, id, intRes, sRes);
//                                                if(intRes[0] == 0)
//                                                {
//                                                    CommonFunction.LogDebugString(log, "DownFile CERT P12 - Length", String.valueOf(sByteFile.length));
//                                                    if ((new File(absoluteDiskPath+sNameFile)).exists()) {
//                                                        (new File(absoluteDiskPath+sNameFile)).delete();
//                                                    }
//                                                    fileOuputStream = new FileOutputStream(absoluteDiskPath + sNameFile);
//                                                    fileOuputStream.write(sByteFile);
//                                                    strView = "0#" + sNameFile.trim();
//                                                } else {
//                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
//                                                }
//                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA_WRITE + "#0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                        }
                    }
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "#0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "#0";
                }
            } catch (NumberFormatException | SQLException | ParseException e) {
                CommonFunction.LogExceptionServlet(log, e.getMessage(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
            } finally {
                if (fileOuputStream != null) {
                    fileOuputStream.close();
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
