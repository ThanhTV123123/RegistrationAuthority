/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import org.apache.tomcat.util.codec.binary.Base64;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.object.CERTIFICATION_AUTHORITY_ATTR;
import vn.ra.object.CERTIFICATION_OWNER;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.CERTIFICATION_PROPERTIES_JSON;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PAYMENT;
import vn.ra.object.PREFIX_UUID;
import vn.ra.object.ProfileContactInfoJson;
import vn.ra.object.REPORT_CONTROL_NEAC;
import vn.ra.object.REPORT_PER_MONTH;
import vn.ra.object.REPORT_RECURRING_NEAC;
import vn.ra.object.RESPONSE_CODE;
import vn.ra.object.RESPONSE_LOG;
import vn.ra.object.TOKEN;
import vn.ra.object.TOKEN_CHANGE_LOG;
import vn.ra.process.CommonFunction;
import vn.ra.process.CommonReferServlet;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.EncodeSOPIN;
import vn.ra.process.GenFeatureCertificate;
import vn.ra.process.PrintFormFunction;
import vn.ra.uat.TestPDFConvert;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author THANH-PC
 */
public class PrintFormCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PrintFormCommon.class.getName());

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
            CommonFunction com = new CommonFunction();
            HttpSession sessionsa = request.getSession(false);
            String strView = "";
            String idParam = request.getParameter("idParam");
            try {
                int sOutInner;
                if (request.getSession(false) != null) {
                    String strInnerUsername = sessionsa.getAttribute("sUserID").toString().trim();
                    String strInnerSessionKey = sessionsa.getAttribute("sesSessKey").toString().trim();
                    sOutInner = db.CheckIsLoginOnline(strInnerUsername, strInnerSessionKey);
                } else {
                    sOutInner = 2;
                }
                if (sOutInner == 1) {
                    String AGENT_ID_LOG = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SessAgentID").toString().trim());
                    String SessUserAgentID = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SessUserAgentID").toString().trim());
                    String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    String sessLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                    PREFIX_UUID[][] sessPrefixDN = (PREFIX_UUID[][]) request.getSession(false).getAttribute("sessPrefixUIDEnterprise");
                    PREFIX_UUID[][] sessPrefixCN = (PREFIX_UUID[][]) request.getSession(false).getAttribute("sessPrefixUIDPersonal");
                    if (null != idParam) {
                        switch (idParam) {
                            case "printcert": {
                                //<editor-fold defaultstate="collapsed" desc="printcert">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_ID = 0;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PURPOSE_ID = 0;
                                    String pCERTIFICATION_SN = "";
                                    String pPERSONAL_NAME = "";
                                    String pCOMPANY_NAME = "";
                                    String pISSUER_SUBJECT = "";
                                    String pSUBJECT = "";
                                    String pKEY_SIZE = "";
                                    String pEXPIRATION_CERT = "";
                                    String pEXPIRATION_CONTRACT = "";
                                    String pEXFFECTIVE_CERT = "";
                                    String pEXFFECTIVE_CONTRACT = "";
                                    String pCERTIFICATION = "";
                                    String sPROPERTIES = "";
                                    String pCSR = "";
                                    String pIsCompany = "0";
                                    String isShowCompanyName = "0";
                                    String pIsMST = "0";
                                    String pMSTValue = "";
                                    String pIsCMND = "0";
                                    String pCMNDValue = "";
                                    String sAGENT_ID;
                                    int intPKI_FORMFACTOR_ID = 0;
                                    boolean booPRIVATE_KEY_ENABLED = false;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_ID = rsReq[0][0].ID;
                                        intPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        booPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                        pCERTIFICATION = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION);
//                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        pEXFFECTIVE_CERT = EscapeUtils.CheckTextNull(rsReq[0][0].PRINT_EFFECTIVE_DT);
                                        pEXPIRATION_CERT = EscapeUtils.CheckTextNull(rsReq[0][0].PRINT_EXPIRATION_DT);
                                        pEXFFECTIVE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PRINT_EFFECTIVE_DT);
                                        pEXPIRATION_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PRINT_EXPIRATION_CONTRACT_DT);
                                        pISSUER_SUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                        pPERSONAL_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                        pCOMPANY_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                        pSUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        pKEY_SIZE = EscapeUtils.CheckTextNull(rsReq[0][0].KEY_SIZE);
                                        pCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                        sPROPERTIES = EscapeUtils.CheckTextNull(rsReq[0][0].PROPERTIES);
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_PURPOSE_ID = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE_CODESIGNING
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE_SSL) {
                                            pPERSONAL_NAME = pCOMPANY_NAME;
                                            isShowCompanyName = "1";
                                        }
                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_CODESIGNING
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SSL) {
                                            pIsCompany = "1";
                                            isShowCompanyName = "0";
                                        }
                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL) {
                                            isShowCompanyName = "0";
                                        }
                                        pMSTValue = CommonReferServlet.swapPrefixEnterpriseToVN(rsReq[0][0].ENTERPRISE_ID);// EscapeUtils.CheckTextNull(rsReq[0][0].TAX_CODE);
//                                        if("".equals(pMSTValue)){
//                                            pMSTValue = EscapeUtils.CheckTextNull(rsReq[0][0].BUDGET_CODE);
//                                        }
//                                        if("".equals(pMSTValue)){
//                                            pMSTValue = EscapeUtils.CheckTextNull(rsReq[0][0].DECISION);
//                                        }
                                        if(!"".equals(pMSTValue)){
                                            pIsMST = "1";
                                        }
                                        pCMNDValue = CommonReferServlet.swapPrefixPersonalToVN(rsReq[0][0].PERSONAL_ID);// EscapeUtils.CheckTextNull(rsReq[0][0].P_ID);
//                                        if("".equals(pCMNDValue)){
//                                            pCMNDValue = EscapeUtils.CheckTextNull(rsReq[0][0].P_EID);
//                                        }
//                                        if("".equals(pCMNDValue)){
//                                            pCMNDValue = EscapeUtils.CheckTextNull(rsReq[0][0].PASSPORT);
//                                        }
                                        if(!"".equals(pCMNDValue)){
                                            pIsCMND = "1";
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        if (!"".equals(pCERTIFICATION)) {
                                            pSUBJECT = pSUBJECT.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID, Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE);
                                            pCERTIFICATION_SN = CommonFunction.formatSerialNumber(pCERTIFICATION);
                                            int[] intResult = new int[1];
                                            String TinhNang = GenFeatureCertificate.getKeyUsage(pCERTIFICATION);// "Client Authentication, Secure Email, Digital Signature, Non-Repudiation, Key Encipherment";
                                            String pathXSLT = "";
                                            CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                            db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                            if (rsCA[0].length > 0) {
                                                pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_LICENSE_CERTIFICATION);
                                            }
                                            String sOrderNumber = "";
                                            int countCertID = String.valueOf(pCERTIFICATION_ID).length();
                                            if(countCertID < 6)
                                            {
                                                if(countCertID == 5){sOrderNumber="000"+String.valueOf(pCERTIFICATION_ID);}
                                                if(countCertID == 4){sOrderNumber="0000"+String.valueOf(pCERTIFICATION_ID);}
                                                if(countCertID == 3){sOrderNumber="00000"+String.valueOf(pCERTIFICATION_ID);}
                                                if(countCertID == 2){sOrderNumber="000000"+String.valueOf(pCERTIFICATION_ID);}
                                                if(countCertID == 1){sOrderNumber="0000000"+String.valueOf(pCERTIFICATION_ID);}
                                            } else {
                                                sOrderNumber = String.valueOf(pCERTIFICATION_ID);
                                            }
//                                            int[] intRes = new int[1];
//                                            Object[] sss = new Object[2];
//                                            String[] tmp = new String[3];
//                                            com.VoidCertificateComponents(pCERTIFICATION, sss, tmp, intRes);
//                                            if (intRes[0] == 0 && sss.length > 0) {
//                                                Object strIssuerDN = sss[1].toString().replace(", ", "\n");
//                                                pISSUER_SUBJECT = strIssuerDN.toString();
//                                            }
                                            String pISSUER_SUBJECTEnd = "";
                                            if(!"".equals(pISSUER_SUBJECT)){
                                                String[] sss = pISSUER_SUBJECT.split(",");
                                                if(sss.length == 3){
                                                    pISSUER_SUBJECTEnd = sss[2].trim() + ", " + sss[1].trim() + ", " + sss[0].trim();
                                                }
                                            }
                                            Config conf = new Config();
//                                            String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
//                                            String queryString = getServletContext().getRealPath("/");
//                                            String outputDirectory = queryString;
//                                            String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
//                                            File fileImage = new File(pathLogoURL);
//                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
//                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME,sessLanguage));
                                            String pathXML = PrintFormFunction.createXMLCertificateFinal(pPERSONAL_NAME,
                                                    pIsCompany, isShowCompanyName, pCOMPANY_NAME, CommonFunction.replaceStringCharaterSpecialDN(pSUBJECT, true, true),
                                                    pISSUER_SUBJECTEnd, pCERTIFICATION_SN, pEXFFECTIVE_CERT,
                                                    pEXPIRATION_CERT, pEXFFECTIVE_CONTRACT, pEXPIRATION_CONTRACT, pKEY_SIZE, TinhNang, sOrderNumber,
                                                    pIsMST, pMSTValue, pIsCMND, pCMNDValue);
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                            if (intResult[0] == 0) {
                                                strView = "0###" + sResultHTML;
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else {
                                            CommonFunction.LogErrorServlet(log, "PrintForm: Certificate doesn't exists");
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
//                                } else {
//                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
//                                }
                                //</editor-fold>
                                break;
                            }
                            case "printhadover": {
                                //<editor-fold defaultstate="collapsed" desc="printhadover">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    int pCERTIFICATION_PURPOSE_ID = 0;
                                    String sAGENT_ID;
                                    String TenHangHoa = "";
                                    String pCSR = "";
                                    String SerialNumber = "";
                                    String SubjectDN = "";
                                    String IsusuerDN = "";
                                    String DoDaiKhoa = "";
                                    String TinhNang = "";
                                    String pCERTIFICATION = "";
                                    String SoLuongToken = "";
                                    String SoLuongHopDong = "";
                                    String sPROPERTIES = "";
                                    String GCN = "";
                                    String MaXacThuc = "";
                                    int intPKI_FORMFACTOR_ID = 0;
                                    boolean booPRIVATE_KEY_ENABLED = false;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        intPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                        booPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                        pCERTIFICATION = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION);
//                                        if (!EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN).equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN)) {
//                                            SoLuongToken = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
//                                        }
                                        if(CommonFunction.checkViewTokenValid(EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN)) == true)
                                        {
                                            SoLuongToken = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                        }
                                        if (!"".equals(EscapeUtils.CheckTextNull(rsReq[0][0].ACTIVATION_CODE))) {
                                            MaXacThuc = EscapeUtils.CheckTextNull(rsReq[0][0].ACTIVATION_CODE) + " (" + EscapeUtils.CheckTextNull(rsReq[0][0].ACTIVATION_EXPIRATION_DT) + ")";
                                        }
                                        SubjectDN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        IsusuerDN = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                        DoDaiKhoa = EscapeUtils.CheckTextNull(rsReq[0][0].KEY_SIZE);
                                        pCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                        sPROPERTIES = EscapeUtils.CheckTextNull(rsReq[0][0].PROPERTIES);
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        pCERTIFICATION_PURPOSE_ID = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                            if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                isAccessAgency = false;
//                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        SubjectDN = SubjectDN.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID, Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE);
                                        if(intPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                        {
//                                            if(booPRIVATE_KEY_ENABLED == true)
//                                            {
//                                                ObjectMapper objectMapper = new ObjectMapper();
//                                                String strPassword = "";
//                                                CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(sPROPERTIES, CERTIFICATION_PROPERTIES_JSON.class);
//                                                for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
//                                                    if(itemParsePush.getAttributes().get(i).getKey().equals(CERTIFICATION_PROPERTIES_JSON.Attribute.KEY_KEYSTORE_PASSWORD))
//                                                    {
//                                                        EncodeSOPIN encript = new EncodeSOPIN();
//                                                        strPassword = encript.decode(itemParsePush.getAttributes().get(i).getValue());
//                                                        break;
//                                                    }
//                                                }
//                                                pCERTIFICATION = CommonFunction.convertP12ToCertificate(strPassword, pCERTIFICATION);
//                                            }
                                            SoLuongToken = "";
                                            MaXacThuc = "";
                                        }
                                        if (!"".equals(pCERTIFICATION)) {
                                            SerialNumber = CommonFunction.formatSerialNumber(pCERTIFICATION);
                                        }
                                        String CreateDate = "..../..../........";
                                        if (!"".equals(pCERTIFICATION)) {
                                            TinhNang = GenFeatureCertificate.getKeyUsage(pCERTIFICATION);
                                        }
                                        String FROM_COMPANY = EscapeUtils.CheckTextNull(request.getParameter("FROM_COMPANY"));
                                        if ("".equals(FROM_COMPANY)) {
                                            FROM_COMPANY = ".............................................................";
                                        }
                                        String BRANCH_FULLNAME = request.getParameter("BRANCH_FULLNAME");
                                        String BRANCH_ADDRESS = request.getParameter("BRANCH_ADDRESS");
                                        String BRANCH_PHONE = request.getParameter("BRANCH_PHONE");
                                        String TO_COMPANY = "";// request.getParameter("TO_COMPANY");
                                        String TO_FULLNAME = request.getParameter("TO_FULLNAME");
                                        String TO_CMND = request.getParameter("TO_CMND");
                                        String TO_DATE = request.getParameter("TO_DATE");
                                        String TO_PLACE = request.getParameter("TO_PLACE");
                                        String TO_ADDRESS = request.getParameter("TO_ADDRESS");
                                        String TO_PHONE = request.getParameter("TO_PHONE");
                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(pCERTIFICATION_PROFILE_ID), rsProfile);
                                        if (rsProfile[0].length > 0) {
                                            TenHangHoa = EscapeUtils.CheckTextNull(rsProfile[0][0].NAME) + " ("
                                                    + EscapeUtils.CheckTextNull(rsProfile[0][0].REMARK) + "/"
                                                    + EscapeUtils.CheckTextNull(rsProfile[0][0].REMARK_EN) + ")";
                                        }
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_DELIVERY_PAPER);
                                        }
                                        if(!"".equals(pathXSLT)){
                                            Config conf = new Config();
                                            String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
//                                            int[] tempResCode;
//                                            tempResCode = new int[1];
//                                            byte[] imagedata = CommonFunction.getByteFromImage(pathLogoURL, tempResCode);
//                                            String base64Image = "";
//                                            if (tempResCode[0] == 0) {
//                                                base64Image = Base64.encodeBase64String(imagedata);
//                                            }
                                            File fileImage = new File(pathLogoURL);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            String pathXML = PrintFormFunction.createXMLHadoverFinal(CreateDate, FROM_COMPANY,
                                                    FROM_COMPANY, BRANCH_FULLNAME, BRANCH_ADDRESS, BRANCH_PHONE,
                                                    TO_COMPANY, TO_FULLNAME, TO_CMND, TO_DATE, TO_PLACE,
                                                    TO_ADDRESS, TO_PHONE, TenHangHoa, SerialNumber, CommonFunction.replaceStringCharaterSpecialDN(SubjectDN, true, true),
                                                    IsusuerDN, DoDaiKhoa, TinhNang, MaXacThuc,
                                                    SoLuongToken, SoLuongHopDong, GCN);
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                            if (intResult[0] == 0) {
                                                strView = "0###" + sResultHTML;
                                                request.getSession(false).setAttribute("sessTempPrintWord", sResultHTML);
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else { strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0"; }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "printhadover2": {
                                //<editor-fold defaultstate="collapsed" desc="printhadover2">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PURPOSE_ID = 0;
                                    String sAGENT_ID;
                                    String SerialNumber = "";
                                    String pCERTIFICATION = "";
                                    String TimeValid = "";
                                    String taxCode = "";
                                    String CMND = "";
                                    String CompanyName = "";
                                    String CustomerName = "";
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION);
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_PURPOSE_ID = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        CompanyName = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                        CustomerName = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                        CMND = CommonReferServlet.swapPrefixEnterpriseToVN(rsReq[0][0].PERSONAL_ID);
//                                        if("".equals(CMND)) {
//                                            CMND = EscapeUtils.CheckTextNull(rsReq[0][0].P_EID);
//                                        }
//                                        if("".equals(CMND)) {
//                                            CMND = EscapeUtils.CheckTextNull(rsReq[0][0].PASSPORT);
//                                        }
                                        taxCode = CommonReferServlet.swapPrefixPersonalToVN(rsReq[0][0].ENTERPRISE_ID);
//                                        if("".equals(taxCode)) {
//                                            taxCode = EscapeUtils.CheckTextNull(rsReq[0][0].BUDGET_CODE);
//                                        }
//                                        if("".equals(taxCode)) {
//                                            taxCode = EscapeUtils.CheckTextNull(rsReq[0][0].DECISION);
//                                        }
                                        if(!"".equals(EscapeUtils.CheckTextNull(rsReq[0][0].EFFECTIVE_DT)))
                                        {
                                            TimeValid = EscapeUtils.CheckTextNull(rsReq[0][0].EFFECTIVE_DT) + " - " + EscapeUtils.CheckTextNull(rsReq[0][0].EXPIRATION_DT);
                                        }
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                            if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                isAccessAgency = false;
//                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        if (!"".equals(pCERTIFICATION)) {
                                            SerialNumber = CommonFunction.formatSerialNumber(pCERTIFICATION);
                                        }
                                        String CreateDate = EscapeUtils.CheckTextNull(request.getParameter("idDate"));
                                        String FROM_COMPANY = EscapeUtils.CheckTextNull(request.getParameter("FROM_COMPANY"));
                                        String BRANCH_FULLNAME = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_FULLNAME"));
                                        String BRANCH_ADDRESS = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_ADDRESS"));
                                        String BRANCH_PHONE = EscapeUtils.CheckTextNull(request.getParameter("BRANCH_PHONE"));
                                        String TO_COMPANY = EscapeUtils.CheckTextNull(request.getParameter("TO_COMPANY"));
                                        String TO_FULLNAME = EscapeUtils.CheckTextNull(request.getParameter("TO_FULLNAME"));
                                        String TO_ADDRESS = EscapeUtils.CheckTextNull(request.getParameter("TO_ADDRESS"));
                                        String TO_PHONE = EscapeUtils.CheckTextNull(request.getParameter("TO_PHONE"));
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_DELIVERY_PAPER);
                                        }
                                        if(!"".equals(pathXSLT)) {
                                            Config conf = new Config();
                                            String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
//                                            int[] tempResCode;
//                                            tempResCode = new int[1];
//                                            byte[] imagedata = CommonFunction.getByteFromImage(pathLogoURL, tempResCode);
//                                            String base64Image = "";
//                                            if (tempResCode[0] == 0) {
//                                                base64Image = Base64.encodeBase64String(imagedata);
//                                            }
                                            File fileImage = new File(pathLogoURL);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            String pathXML = PrintFormFunction.createXMLHadover2Final(FROM_COMPANY,
                                                    BRANCH_FULLNAME, BRANCH_ADDRESS, BRANCH_PHONE,
                                                    TO_COMPANY, TO_FULLNAME, TO_ADDRESS, TO_PHONE, SerialNumber,
                                                    CompanyName, CustomerName, taxCode, CMND, TimeValid, CreateDate);
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                            if (intResult[0] == 0) {
                                                strView = "0###" + sResultHTML;
                                                request.getSession(false).setAttribute("sessTempPrintWord", sResultHTML);
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else { strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0"; }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "printregisterpersonal": {
                                //<editor-fold defaultstate="collapsed" desc="printregisterpersonal">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String TO_DATE = request.getParameter("TO_DATE");
                                    String TO_PLACE = request.getParameter("TO_PLACE");
                                    String TO_ADDRESS = EscapeUtils.CheckTextNull(request.getParameter("TO_ADDRESS"));
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    int pCERTIFICATION_PURPOSE_ID = 0;
                                    int pCERTIFICATION_ATTR_TYPE_ID = 0;
                                    String sAGENT_ID;
//                                    String TO_CMND = "";
                                    String TO_CMNDHC = "";
//                                    String TO_HC = "";
//                                    String TO_CCCD = "";
                                    String IsCMND = "0";
                                    String IsHC = "0";
                                    String IsCCCD = "0";
                                    String isCapMoi = "0";
                                    String isGiaHan = "0";
                                    String is1Nam = "0";
                                    String is2Nam = "0";
                                    String is3Nam = "0";
                                    String isKhac = "0";
                                    String NoiDungKhac = "";
                                    String SubjectDN = "";
                                    String HoTen = "";
                                    String staffChucVu = "";
                                    String staffPhongBan = "";
                                    String staffToChuc = "";
                                    String DiaChi = "";
                                    String Province = "";
                                    String staffDiaChi = "";
                                    String staffMST = "";
                                    String Email = "";
                                    String Mobile = "";
                                    String staffEmail = "";
                                    String staffMobile = "";
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
//                                        TO_CMND = EscapeUtils.CheckTextNull(rsReq[0][0].P_ID);
//                                        TO_CCCD = EscapeUtils.CheckTextNull(rsReq[0][0].P_EID);
//                                        TO_HC = EscapeUtils.CheckTextNull(rsReq[0][0].PASSPORT);
                                        TO_CMNDHC = rsReq[0][0].PERSONAL_ID;
                                        SubjectDN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_PURPOSE_ID = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                        pCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_CODESIGNING) {
                                            HoTen = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                        } else if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_CODESIGNING) {
                                            HoTen = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                            staffToChuc = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                            staffMST = CommonReferServlet.swapPrefixEnterpriseToVN(rsReq[0][0].ENTERPRISE_ID);
                                        } else {
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        // CMND HC
//                                        if (!"".equals(TO_CMND)) {
//                                            IsCMND = "1";
//                                            TO_CMNDHC = TO_CMND;
//                                        }
//                                        if (!"".equals(TO_HC)) {
//                                            IsHC = "1";
//                                            TO_CMNDHC = TO_HC;
//                                        }
//                                        if (!"".equals(TO_CCCD)) {
//                                            IsCCCD = "1";
//                                            TO_CMNDHC = TO_CCCD;
//                                        }
                                        // NEW and RENEW
                                        if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                            isCapMoi = "1";
                                        } else if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                            isGiaHan = "1";
                                        } else {
                                        }
                                        // DURATION
                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(pCERTIFICATION_PROFILE_ID), rsProfile);
                                        int sDuration = 0;
                                        if (rsProfile[0].length > 0) {
                                            sDuration = rsProfile[0][0].DURATION;
                                        }
                                        int subDuration = sDuration / 365;
                                        if (subDuration == 1) {
                                            is1Nam = "1";
                                        } else if (subDuration == 2) {
                                            is2Nam = "1";
                                        } else if (subDuration == 3) {
                                            is3Nam = "1";
                                        } else {
                                            isKhac = "1";
                                            NoiDungKhac = String.valueOf(sDuration);
                                        }
                                        SubjectDN = SubjectDN.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID, Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE);
                                        staffChucVu = CommonFunction.getRoleInDN(SubjectDN);
                                        Email = CommonFunction.getEmailInDN(SubjectDN);
                                        staffPhongBan = CommonFunction.getDepartmentInDN(SubjectDN);
                                        Province = CommonFunction.getStateOrProvinceInDN(SubjectDN);
                                        DiaChi = Province;
                                        if(!"".equals(CommonFunction.getLocationInDN(SubjectDN).trim())) {
                                            DiaChi = CommonFunction.getLocationInDN(SubjectDN).trim() + ", " + DiaChi;
                                        }
//                                        Calendar now = Calendar.getInstance();
//                                        String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
//                                        if (day.length() == 1) {
//                                            day = "0" + day;
//                                        }
//                                        String mount = String.valueOf(now.get(Calendar.MONTH));
//                                        if (mount.length() == 1) {
//                                            mount = "0" + mount;
//                                        }
//                                        String year = String.valueOf(now.get(Calendar.YEAR));
                                        String ThoiGianDiaDiem = request.getParameter("sDate");// "........., ngày.....tháng.....năm ...... ";
                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_CODESIGNING) {
                                            HoTen = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                        } else if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_CODESIGNING) {
                                            staffEmail = Email;
                                            staffMobile = Mobile;
                                            staffDiaChi = DiaChi;
                                        } else {
                                        }
                                        // XSLT
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_PERSONAL_REGISTRATION_PAPER);
                                        }
                                        if(!"".equals(pathXSLT)) {
                                            Config conf = new Config();
                                            String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
//                                            int[] tempResCode;
//                                            tempResCode = new int[1];
//                                            byte[] imagedata = CommonFunction.getByteFromImage(pathLogoURL, tempResCode);
//                                            String base64Image = "";
//                                            if (tempResCode[0] == 0) {
//                                                base64Image = Base64.encodeBase64String(imagedata);
//                                            }
                                            File fileImage = new File(pathLogoURL);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_BACKGROUD_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            String pathXML = PrintFormFunction.createXMLRegistrationCNFinal(HoTen, IsCMND, IsHC, TO_CMNDHC,
                                                    TO_DATE, TO_PLACE, TO_ADDRESS, Mobile, isCapMoi, isGiaHan, is1Nam,
                                                    is2Nam, is3Nam, isKhac, NoiDungKhac, staffChucVu, staffPhongBan,
                                                    staffToChuc, staffDiaChi, staffMST, staffEmail, staffMobile, ThoiGianDiaDiem, IsCCCD);
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                            if (intResult[0] == 0) {
                                                strView = "0###" + sResultHTML;
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else { strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0"; }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "printregisterpersonal2": {
                                //<editor-fold defaultstate="collapsed" desc="printregisterpersonal2">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String PRINT_ADDRESS_BILLING = request.getParameter("PRINT_ADDRESS_BILLING");
                                    String PRINT_CMND = request.getParameter("PRINT_TAXCODE");
                                    String PRINT_PHONE = request.getParameter("PRINT_PHONE");
                                    String PRINT_EMAIL = request.getParameter("PRINT_EMAIL");
                                    String PRINT_FULLNAME = request.getParameter("PRINT_FULLNAME");
                                    String PRINT_REPRESENTATIVE = request.getParameter("PRINT_REPRESENTATIVE");
                                    String PRINT_ROLE = request.getParameter("PRINT_ROLE");
                                    String sCertTypeID = EscapeUtils.CheckTextNull(request.getParameter("sCertTypeID"));
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    int pCERTIFICATION_ATTR_TYPE_ID = 0;
                                    int pCERTIFICATION_OWNER_ID = 0;
                                    String sAGENT_ID;
                                    String isCapMoi = "0";
                                    String isGiaHan = "0";
                                    String is6Thang = "0";
                                    String is4Nam = "0";
                                    String is1Nam = "0";
                                    String is2Nam = "0";
                                    String is3Nam = "0";
                                    String isKhac = "0";
                                    String soNam = "";
                                    String NoiDungKhac = "";
                                    String SubjectDN = "";
                                    String Province = "";
                                    String donViToChuc = "";
                                    String taxCode = "";
                                    String ThoiGianDiaDiem = EscapeUtils.CheckTextNull(request.getParameter("sDate"));// "........., ngày.....tháng.....năm ...... ";
                                    String REGISTER_DATE = EscapeUtils.CheckTextNull(request.getParameter("REGISTER_DATE"));
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        SubjectDN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        donViToChuc = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                        taxCode = rsReq[0][0].ENTERPRISE_ID;
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        String sDateCreate = EscapeUtils.CheckTextNull(rsReq[0][0].CREATED_DT);
                                        if(!"".equals(REGISTER_DATE)) {
                                            sDateCreate = REGISTER_DATE + " 00:00:00";
                                        }
                                        String[] sDateDetail= new String[3];
                                        CommonFunction.subDateTimeDetailDay(sDateCreate, sDateDetail);
                                        if(sDateDetail.length > 0) {
                                            ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[DD]",sDateDetail[0]);
                                            ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[MM]",sDateDetail[1]);
                                            ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[YYYY]",sDateDetail[2]);
                                        }
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                            if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                isAccessAgency = false;
//                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
//                                        String sNameReceive = "";
//                                        String sAddressReceive = "";
//                                        String sPhoneReceive = "";
//                                        String sEmailReceive = "";
                                        String sNameReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_FULLNAME"));
                                        String sAddressReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_ADDRESS"));
                                        String sPhoneReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_PHONE"));
                                        String sEmailReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_EMAIL"));
                                        /*CERTIFICATION_OWNER[][] rsOwner = new CERTIFICATION_OWNER[1][];
                                        db.S_BO_CERTIFICATION_OWNER_DETAIL(String.valueOf(pCERTIFICATION_OWNER_ID), sessLanguage, rsOwner);
                                        if(rsOwner[0].length > 0)
                                        {
                                            sNameReceive = rsOwner[0][0].PERSONAL_NAME;
                                            sAddressReceive = rsOwner[0][0].ADDRESS;
                                            sPhoneReceive = rsOwner[0][0].PHONE_CONTRACT;
                                            sEmailReceive = rsOwner[0][0].EMAIL_CONTRACT;
                                        }*/
                                        // NEW and RENEW
                                        if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                            isCapMoi = "1";
                                        } else if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                            isGiaHan = "1";
                                        } else {
                                        }
                                        // DURATION
                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(pCERTIFICATION_PROFILE_ID), rsProfile);
                                        int sDuration = 0;
                                        if (rsProfile[0].length > 0) {
                                            sDuration = rsProfile[0][0].DURATION;
                                        }
                                        if(sDuration < 365) {
                                            is6Thang = "1";
                                            soNam = "6 tháng";
                                        } else {
                                            int subDuration = sDuration / 365;
                                            switch (subDuration) {
                                                case 1:
                                                    is1Nam = "1";
                                                    soNam = "1" + " năm";
                                                    break;
                                                case 2:
                                                    is2Nam = "1";
                                                    soNam = "2" + " năm";
                                                    break;
                                                case 3:
                                                    is3Nam = "1";
                                                    soNam = "3" + " năm";
                                                    break;
                                                case 4:
                                                    is4Nam = "1";
                                                    soNam = "4" + " năm";
                                                    break;
                                                default:
                                                    isKhac = "1";
                                                    NoiDungKhac = String.valueOf(sDuration);
                                                    soNam = String.valueOf(sDuration) + " tháng";
                                                    break;
                                            }
                                        }
                                        String isReceiveRegister = "0";
                                        String isReceiveEnter = "0";
                                        String isCheckRegisterInfo = EscapeUtils.CheckTextNull(request.getParameter("isCheckRegisterInfo"));
                                        if("1".equals(isCheckRegisterInfo)) {
                                            isReceiveRegister = "0";
                                            isReceiveEnter = "1";
                                        } else {
                                            isReceiveRegister = "1";
                                            isReceiveEnter = "0";
                                            sNameReceive=""; sAddressReceive=""; sPhoneReceive=""; sEmailReceive="";
                                        }
                                        int profileMonth = sDuration / 30;
                                        Province = CommonFunction.getStateOrProvinceInDN(SubjectDN);
                                        // XSLT
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            if(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_STAFF).equals(sCertTypeID)) {
                                                pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_PERSONAL_ENTERPRISE_REGISTRATION_PAPER);
                                            } else {
                                                pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_PERSONAL_REGISTRATION_PAPER);
                                            }
                                        }
                                        if(!"".equals(pathXSLT)) {
                                            Config conf = new Config();
                                            String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
//                                            int[] tempResCode;
//                                            tempResCode = new int[1];
//                                            byte[] imagedata = CommonFunction.getByteFromImage(pathLogoURL, tempResCode);
//                                            String base64Image = "";
//                                            if (tempResCode[0] == 0) {
//                                                base64Image = Base64.encodeBase64String(imagedata);
//                                            }
                                            File fileImage = new File(pathLogoURL);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_BACKGROUD_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            String pathXML;
                                            if(String.valueOf(Definitions.CONFIG_CERTTYPE_CODE_STAFF).equals(sCertTypeID)) {
                                                pathXML = PrintFormFunction.createXMLRegistrationCNDNFinal2(PRINT_FULLNAME, PRINT_CMND, PRINT_ADDRESS_BILLING,
                                                    PRINT_PHONE, PRINT_EMAIL, is1Nam, is2Nam, is3Nam, isKhac, NoiDungKhac, sNameReceive, sAddressReceive,
                                                    sPhoneReceive, sEmailReceive, ThoiGianDiaDiem, soNam, is6Thang, is4Nam, String.valueOf(profileMonth),
                                                    isReceiveRegister, isReceiveEnter, donViToChuc, taxCode);
                                            } else {
                                                pathXML = PrintFormFunction.createXMLRegistrationCNFinal2(PRINT_FULLNAME, PRINT_CMND, PRINT_ADDRESS_BILLING,
                                                    PRINT_PHONE, PRINT_EMAIL, is1Nam, is2Nam, is3Nam, isKhac, NoiDungKhac, sNameReceive, sAddressReceive,
                                                    sPhoneReceive, sEmailReceive, ThoiGianDiaDiem, soNam, is6Thang, is4Nam,
                                                    String.valueOf(profileMonth), isReceiveRegister, isReceiveEnter);
                                            }
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                            if (intResult[0] == 0) {
                                                strView = "0###" + sResultHTML;
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else { strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0"; }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "printregisterpersonalrar2": {
                                //<editor-fold defaultstate="collapsed" desc="printregisterpersonalrar2">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String PRINT_ADDRESS_BILLING = request.getParameter("PRINT_ADDRESS_BILLING");
                                    String PRINT_TAXCODE = request.getParameter("PRINT_TAXCODE");
                                    String PRINT_PHONE = request.getParameter("PRINT_PHONE");
                                    String PRINT_EMAIL = request.getParameter("PRINT_EMAIL");
                                    String PRINT_FULLNAME = request.getParameter("PRINT_FULLNAME");
                                    String PRINT_REPRESENTATIVE = request.getParameter("PRINT_REPRESENTATIVE");
                                    String PRINT_ROLE = request.getParameter("PRINT_ROLE");
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    int pCERTIFICATION_ATTR_TYPE_ID = 0;
                                    int pCERTIFICATION_OWNER_ID = 0;
                                    String sAGENT_ID;
                                    String isCapMoi = "0";
                                    String isGiaHan = "0";
                                    String is6Thang = "0";
                                    String is4Nam = "0";
                                    String is1Nam = "0";
                                    String is2Nam = "0";
                                    String is3Nam = "0";
                                    String isKhac = "0";
                                    String soNam = "";
                                    String NoiDungKhac = "";
                                    String SubjectDN = "";
                                    String Province = "";
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        SubjectDN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                            if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                isAccessAgency = false;
//                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
//                                        String sNameReceive = "";
//                                        String sAddressReceive = "";
//                                        String sPhoneReceive = "";
//                                        String sEmailReceive = "";
                                        String sNameReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_FULLNAME"));
                                        String sAddressReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_ADDRESS"));
                                        String sPhoneReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_PHONE"));
                                        String sEmailReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_EMAIL"));
                                        /*CERTIFICATION_OWNER[][] rsOwner = new CERTIFICATION_OWNER[1][];
                                        db.S_BO_CERTIFICATION_OWNER_DETAIL(String.valueOf(pCERTIFICATION_OWNER_ID), sessLanguage, rsOwner);
                                        if(rsOwner[0].length > 0)
                                        {
                                            sNameReceive = rsOwner[0][0].PERSONAL_NAME;
                                            sAddressReceive = rsOwner[0][0].ADDRESS;
                                            sPhoneReceive = rsOwner[0][0].PHONE_CONTRACT;
                                            sEmailReceive = rsOwner[0][0].EMAIL_CONTRACT;
                                        }*/
                                        // NEW and RENEW
                                        if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                            isCapMoi = "1";
                                        } else if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                            isGiaHan = "1";
                                        } else {
                                        }
                                        // DURATION
                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(pCERTIFICATION_PROFILE_ID), rsProfile);
                                        int sDuration = 0;
                                        if (rsProfile[0].length > 0) {
                                            sDuration = rsProfile[0][0].DURATION;
                                        }
                                        if(sDuration < 365)
                                        {
                                            is6Thang = "1";
                                            soNam = "6 tháng";
                                        } else {
                                            int subDuration = sDuration / 365;
                                            switch (subDuration) {
                                                case 1:
                                                    is1Nam = "1";
                                                    soNam = "1" + " năm";
                                                    break;
                                                case 2:
                                                    is2Nam = "1";
                                                    soNam = "2" + " năm";
                                                    break;
                                                case 3:
                                                    is3Nam = "1";
                                                    soNam = "3" + " năm";
                                                    break;
                                                case 4:
                                                    is4Nam = "1";
                                                    soNam = "4" + " năm";
                                                    break;
                                                default:
                                                    isKhac = "1";
                                                    NoiDungKhac = String.valueOf(sDuration);
                                                    soNam = String.valueOf(sDuration) + " tháng";
                                                    break;
                                            }
                                        }
                                        Province = CommonFunction.getStateOrProvinceInDN(SubjectDN);
                                        String ThoiGianDiaDiem = request.getParameter("sDate");// "........., ngày.....tháng.....năm ...... ";
                                        Config conf = new Config();
                                        String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_LOGO);
                                        String queryString = getServletContext().getRealPath("/");
                                        String outputDirectory = queryString;
                                        String pathLogoTemplate = outputDirectory + "/Images/" + sNameFile;
//                                        String pathConfirmXML = PrintFormFunction.createXMLConfirmInfo(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_REPRESENTATIVE,
//                                            PRINT_ROLE, "", ThoiGianDiaDiem);
//                                        String sResultConfirmHTML = PrintFormFunction.createStringHtmlInStringExtend(pathConfirmXSLT, pathConfirmXML, null, false, false, intResult);
                                        String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                        File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                        if (!directory.exists()) {
                                            directory.mkdir();
                                        }
                                        String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                        String sFileNameRegis = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REGISTRATION_TEMPLATE + CommonFunction.getDateFormat();
                                        String sFileNameConfirm = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_CONFIRMATION_TEMPLATE + CommonFunction.getDateFormat();
                                        String sPathRegisPDF_temp = pPathURL + sFileNameRegis + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                                        String sPathRegisPDF = pPathURL + sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                                        String sPathConfirmPDF_temp = pPathURL + sFileNameConfirm + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                                        String sPathConfirmPDF = pPathURL + sFileNameConfirm + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                                        String sFileNameZip = pPathURL + sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_RAR;
                                        int[] sReturn = new int[2];
                                        TestPDFConvert.writeFileRarPersonal(pCERTIFICATION_AUTHORITY_ID, pathLogoTemplate, sPathConfirmPDF_temp, sPathConfirmPDF,
                                            sPathRegisPDF_temp, sPathRegisPDF, sFileNameZip, PRINT_FULLNAME, PRINT_TAXCODE, PRINT_REPRESENTATIVE, PRINT_ROLE,
                                            ThoiGianDiaDiem, PRINT_ADDRESS_BILLING, PRINT_PHONE, PRINT_EMAIL, is1Nam, is2Nam, is3Nam, isKhac,
                                            NoiDungKhac, sNameReceive, sAddressReceive, sPhoneReceive, sEmailReceive, soNam, is6Thang, is4Nam, sReturn);
                                        if(sReturn[0] == 0)
                                        {
                                            strView = "0###" + sFileNameZip + "###" + sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_RAR;
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                        }
                                        
                                        // XSLT
//                                        int[] intResult = new int[1];
//                                        String pathRegisXSLT = "";
//                                        String pathConfirmXSLT = "";
//                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
//                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
//                                        if (rsCA[0].length > 0) {
//                                            pathRegisXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_PERSONAL_REGISTRATION_PAPER);
//                                            pathConfirmXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_CONFIRMATION_PAPER);
////                                            pathXSLT = "";
//                                        }
//                                        String pathRegisXML = PrintFormFunction.createXMLRegistrationCNFinal2(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_ADDRESS_BILLING,
//                                            PRINT_PHONE, PRINT_EMAIL, is1Nam, is2Nam, is3Nam, isKhac,
//                                            NoiDungKhac, sNameReceive, sAddressReceive, sPhoneReceive, sEmailReceive, ThoiGianDiaDiem, soNam);
//                                        String sResultRegisHTML = PrintFormFunction.createStringHtmlInStringExtend(pathRegisXSLT, pathRegisXML, null, false, false, intResult);
//                                        if (intResult[0] == 0) {
//                                            Config conf = new Config();
//                                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_LOGO);
//                                            String queryString = getServletContext().getRealPath("/");
//                                            String outputDirectory = queryString;
//                                            String pathLogoTemplate = outputDirectory + "/Images/" + sNameFile;
//                                            String pathConfirmXML = PrintFormFunction.createXMLConfirmInfo(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_REPRESENTATIVE,
//                                                PRINT_ROLE, "", ThoiGianDiaDiem);
//                                            String sResultConfirmHTML = PrintFormFunction.createStringHtmlInStringExtend(pathConfirmXSLT, pathConfirmXML, null, false, false, intResult);
//                                            String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
//                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
//                                            if (!directory.exists()) {
//                                                directory.mkdir();
//                                            }
//                                            String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
//                                            String sFileNameRegis = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REGISTRATION_TEMPLATE + CommonFunction.getDateFormat();
//                                            String sFileNameConfirm = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_CONFIRMATION_TEMPLATE + CommonFunction.getDateFormat();
//                                            String sPathRegisPDF_temp = pPathURL + sFileNameRegis + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
//                                            String sPathRegisPDF = pPathURL + sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
//                                            String sPathConfirmPDF_temp = pPathURL + sFileNameConfirm + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
//                                            String sPathConfirmPDF = pPathURL + sFileNameConfirm + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
//                                            TestPDFConvert.writeFileRarPersonal(pCERTIFICATION_AUTHORITY_ID, pPathURL, sPathConfirmPDF, sPathConfirmPDF_temp, sPathRegisPDF, sPathRegisPDF_temp, PRINT_FULLNAME, PRINT_TAXCODE, PRINT_REPRESENTATIVE, PRINT_ROLE, ThoiGianDiaDiem, PRINT_ADDRESS_BILLING, PRINT_PHONE, PRINT_EMAIL, is1Nam, is2Nam, is3Nam, isKhac, NoiDungKhac, sNameReceive, sAddressReceive, sPhoneReceive, sEmailReceive, soNam);
                                            
                                            
                                            // create html
//                                            String sPathHTML = pPathURL+ sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
//                                            PrintFormFunction.createFile(sPathHTML, sResultRegisHTML);
                                            //<editor-fold defaultstate="collapsed" desc="### PDF Process">
//                                            int[] tempResCode;
//                                            tempResCode = new int[1];
//                                            PrintFormFunction.CreatePDFFromHTML(sResultRegisHTML, sPathRegisPDF_temp, tempResCode);
//                                            PrintFormFunction.AddBackgroundImageToPDF(sPathRegisPDF_temp, sPathRegisPDF, pathLogoTemplate, pathLogoTemplate, tempResCode);
//                                            if(tempResCode[0] == 0)
//                                            {
//                                                tempResCode = new int[1];
//                                                PrintFormFunction.CreatePDFFromHTML(sResultConfirmHTML, sPathConfirmPDF_temp, tempResCode);
//                                                PrintFormFunction.AddBackgroundImageToPDF(sPathConfirmPDF_temp, sPathConfirmPDF, pathLogoTemplate, pathLogoTemplate, tempResCode);
//                                                String sFileNameZip = pPathURL+ sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_RAR;
//                                                try (FileOutputStream fos = new FileOutputStream(sFileNameZip)) {
//                                                    ZipOutputStream zos = new ZipOutputStream(fos);
//                                                    CommonFunction.addToZipFile(sPathRegisPDF, zos);
//                                                    CommonFunction.addToZipFile(sPathConfirmPDF, zos);
//                                                    zos.close();
//                                                }
//                                                strView = "0###" + sFileNameZip + "###" + sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_RAR;
//                                            } else {
//                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
//                                            }
                                            //</editor-fold>
                                            
                                            
//                                            String[] sCode = new String[1];
//                                            PrintFormFunction.convertWord(sResultHTML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""), sPathWord, sPathHTML, sCode);
//                                            if(null == sCode[0]) {
//                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
//                                            } else {
//                                                switch (sCode[0]) {
//                                                    case "0":
//                                                        String sFileNameZip = pPathURL+ sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_ZIP;
//                                                        FileOutputStream fos = new FileOutputStream(sFileNameZip);
//                                                        ZipOutputStream zos = new ZipOutputStream(fos);
////                                                        FileOutputStream fileOuputStream;
//                                                        CommonFunction.addToZipFile(sPathWord, zos);
////                                                        strView = "0#" + sFileName + "#" + strURLPath;
//                                                        strView = "0###" + sFileNameZip + "###" + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_ZIP;
//                                                        zos.close();
//                                                        fos.close();
//                                                        
////                                                        strView = "0###" + sPathWord + "###" + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
//                                                        break;
//                                                    default:
//                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
//                                                        break;
//                                                }
//                                            }
//                                            strView = "0###" + sResultHTML;
//                                        } else {
//                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
//                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "printregisterbusiness": {
                                //<editor-fold defaultstate="collapsed" desc="printregisterbusiness">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String INFO_NAME = request.getParameter("INFO_NAME");
                                    String INFO_ORGANI = request.getParameter("INFO_ORGANI");
                                    String CheckDKDN = request.getParameter("CheckDKDN");
                                    String CheckDT = request.getParameter("CheckDT");
                                    String CheckTL = request.getParameter("CheckTL");
                                    String INFO_ID = request.getParameter("INFO_ID");
                                    String INFO_DATE_GRANT = request.getParameter("INFO_DATE_GRANT");
                                    String INFO_ORGANI_GRANT = request.getParameter("INFO_ORGANI_GRANT");
                                    String INFO_TAXCODE = request.getParameter("INFO_TAXCODE");
                                    String INFO_ADDRESS = request.getParameter("INFO_ADDRESS");
                                    String INFO_EMAIL = request.getParameter("INFO_EMAIL");
                                    String INFO_PHONE = request.getParameter("INFO_PHONE");
                                    String INFO_REPRESEN = request.getParameter("INFO_REPRESEN");
                                    String INFO_ROLE = request.getParameter("INFO_ROLE");
                                    String INFO_CMND = request.getParameter("INFO_CMND");
                                    String CONTACT_FULLNAME = request.getParameter("CONTACT_FULLNAME");
                                    String CONTACT_ROLE = request.getParameter("CONTACT_ROLE");
                                    String CONTACT_EMAIL = request.getParameter("CONTACT_EMAIL");
                                    String CONTACT_PHONE = request.getParameter("CONTACT_PHONE");
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    int pPAST_CERTIFICATION_ID = 0;
                                    int pCERTIFICATION_ATTR_TYPE_ID = 0;
                                    String sAGENT_ID;
                                    String isCapMoi = "0";
                                    String isGiaHan = "0";
                                    String isChangeInfo = "0";
                                    String isRecovery = "0";
                                    String isSuspend = "0";
                                    String isRevoke = "0";
                                    String snRecovery = "";
                                    String snSuspend = "";
                                    String snRevoke = "";
                                    String is1Nam = "0";
                                    String is2Nam = "0";
                                    String is3Nam = "0";
                                    String isKhac = "0";
                                    String NoiDungKhac = "";
                                    String SubjectDNNew = "";
                                    String SubjectDNOld = "";
                                    String certSN = "";
                                    String Province = "";
                                    boolean booDK0314 = false;
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        pPAST_CERTIFICATION_ID = rsReq[0][0].PAST_CERTIFICATION_ID;
                                        SubjectDNNew = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        certSN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                            isCapMoi = "1";
                                        } else if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                            isGiaHan = "1";
                                        } else if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                            booDK0314 = true;
                                            isRevoke = "1";
                                            snRevoke = certSN;
                                        } else if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED) {
                                            booDK0314 = true;
                                            isRecovery = "1";
                                            snRecovery = certSN;
                                        } else if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                            || pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE) {
                                            booDK0314 = true;
                                            isSuspend = "1";
                                            snSuspend = certSN;
                                        } else if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                            booDK0314 = true;
                                            isChangeInfo = "1";
                                            rsReq = new CERTIFICATION[1][];
                                            db.S_BO_CERTIFICATION_GET_INFO(String.valueOf(pPAST_CERTIFICATION_ID), sessLanguage, rsReq);
                                            if (rsReq[0].length > 0) {
                                                SubjectDNOld = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                            }
                                        }
                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(pCERTIFICATION_PROFILE_ID), rsProfile);
                                        int sDuration = 0;
                                        if (rsProfile[0].length > 0) {
                                            sDuration = rsProfile[0][0].DURATION;
                                        }
                                        int subDuration = sDuration / 365;
                                        if (subDuration == 1) {
                                            is1Nam = "1";
                                        } else if (subDuration == 2) {
                                            is2Nam = "1";
                                        } else if (subDuration == 3) {
                                            is3Nam = "1";
                                        } else {
                                            isKhac = "1";
                                            NoiDungKhac = String.valueOf(sDuration);
                                        }
                                        Province = CommonFunction.getStateOrProvinceInDN(SubjectDNNew);
                                        String ThoiGianDiaDiem = request.getParameter("sDate");// "........., ngày.....tháng.....năm ...... ";
                                        String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            if(booDK0314 == true) {
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_WINCA)) {
                                                    pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_CERTIFICATE_REVISION_PAPER);
                                                } else {
                                                    pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_ENTERPRISE_REGISTRATION_PAPER);
                                                }
                                            } else {
                                                pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_ENTERPRISE_REGISTRATION_PAPER);
                                            }
                                        }
                                        if(!"".equals(pathXSLT)) {
                                            Config conf = new Config();
                                            String sNameFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoTemplate = outputDirectory + "/Images/" + sNameFile;
                                            File fileImage = new File(pathLogoTemplate);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_BACKGROUD_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            String pathXML;
                                            if(booDK0314 == true) {
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_WINCA)) {
                                                    pathXML = PrintFormFunction.createXMLChange14DNFinal(INFO_NAME, INFO_ORGANI, CheckDKDN,
                                                        CheckDT, CheckTL, INFO_ID, INFO_DATE_GRANT, INFO_ORGANI_GRANT, INFO_TAXCODE,
                                                        INFO_ADDRESS, INFO_REPRESEN, INFO_ROLE, INFO_CMND, ThoiGianDiaDiem, CONTACT_EMAIL, CONTACT_PHONE,
                                                        isChangeInfo, isSuspend, isRecovery, isRevoke, snSuspend, snRecovery, snRevoke,
                                                        SubjectDNNew, SubjectDNOld);
                                                } else {
                                                    pathXML = PrintFormFunction.createXMLRegistrationDNFinal(INFO_NAME, INFO_ORGANI, CheckDKDN,
                                                        CheckDT, CheckTL, INFO_ID, INFO_DATE_GRANT, INFO_ORGANI_GRANT, INFO_TAXCODE,
                                                        INFO_ADDRESS, INFO_EMAIL, INFO_PHONE, INFO_REPRESEN, INFO_ROLE, INFO_CMND, isCapMoi, isGiaHan,
                                                        is1Nam, is2Nam, is3Nam, isKhac, NoiDungKhac, CONTACT_FULLNAME, CONTACT_ROLE,
                                                        CONTACT_EMAIL, CONTACT_PHONE, ThoiGianDiaDiem);
                                                }
                                            } else {
                                                pathXML = PrintFormFunction.createXMLRegistrationDNFinal(INFO_NAME, INFO_ORGANI, CheckDKDN,
                                                    CheckDT, CheckTL, INFO_ID, INFO_DATE_GRANT, INFO_ORGANI_GRANT, INFO_TAXCODE,
                                                    INFO_ADDRESS, INFO_EMAIL, INFO_PHONE, INFO_REPRESEN, INFO_ROLE, INFO_CMND, isCapMoi, isGiaHan,
                                                    is1Nam, is2Nam, is3Nam, isKhac, NoiDungKhac, CONTACT_FULLNAME, CONTACT_ROLE,
                                                    CONTACT_EMAIL, CONTACT_PHONE, ThoiGianDiaDiem);
                                            }
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                            if (intResult[0] == 0) {
                                                strView = "0###" + sResultHTML;
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else { strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0"; }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "printregisterbusiness2": {
                                //<editor-fold defaultstate="collapsed" desc="printregisterbusiness2">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String PRINT_ADDRESS_BILLING = request.getParameter("PRINT_ADDRESS_BILLING");
                                    String PRINT_TAXCODE = request.getParameter("PRINT_TAXCODE");
                                    String PRINT_PHONE = request.getParameter("PRINT_PHONE");
                                    String PRINT_EMAIL = request.getParameter("PRINT_EMAIL");
                                    String PRINT_FULLNAME = request.getParameter("PRINT_FULLNAME");
                                    String PRINT_REPRESENTATIVE = request.getParameter("PRINT_REPRESENTATIVE");
                                    String PRINT_ROLE = request.getParameter("PRINT_ROLE");
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    int pCERTIFICATION_ATTR_TYPE_ID = 0;
                                    int pCERTIFICATION_OWNER_ID = 0;
                                    String sAGENT_ID;
                                    String isCapMoi = "0";
                                    String isGiaHan = "0";
                                    String is6Thang = "0";
                                    String is1Nam = "0";
                                    String is2Nam = "0";
                                    String is3Nam = "0";
                                    String is4Nam = "0";
                                    String isKhac = "0";
                                    String soNam = "";
                                    String NoiDungKhac = "";
                                    String SubjectDN = "";
                                    String Province = "";
                                    String ThoiGianDiaDiem = request.getParameter("sDate");// "........., ngày.....tháng.....năm ...... ";
                                    String REGISTER_DATE = EscapeUtils.CheckTextNull(request.getParameter("REGISTER_DATE"));
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        SubjectDN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        String sDateCreate = EscapeUtils.CheckTextNull(rsReq[0][0].CREATED_DT);
                                        if(!"".equals(REGISTER_DATE)) {
                                            sDateCreate = REGISTER_DATE + " 00:00:00";
                                        }
                                        String[] sDateDetail= new String[3];
                                        CommonFunction.subDateTimeDetailDay(sDateCreate, sDateDetail);
                                        if(sDateDetail.length > 0) {
                                            ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[DD]",sDateDetail[0]);
                                            ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[MM]",sDateDetail[1]);
                                            ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[YYYY]",sDateDetail[2]);
                                        }
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        String sNameReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_FULLNAME"));
                                        String sAddressReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_ADDRESS"));
                                        String sPhoneReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_PHONE"));
                                        String sEmailReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_EMAIL"));
                                        /*CERTIFICATION_OWNER[][] rsOwner = new CERTIFICATION_OWNER[1][];
                                        db.S_BO_CERTIFICATION_OWNER_DETAIL(String.valueOf(pCERTIFICATION_OWNER_ID), sessLanguage, rsOwner);
                                        if(rsOwner[0].length > 0)
                                        {
                                            sNameReceive = rsOwner[0][0].COMPANY_NAME;
                                            sAddressReceive = rsOwner[0][0].ADDRESS;
                                            sPhoneReceive = rsOwner[0][0].PHONE_CONTRACT;
                                            sEmailReceive = rsOwner[0][0].EMAIL_CONTRACT;
                                        }*/
                                        // NEW and RENEW
                                        if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                            isCapMoi = "1";
                                        } else if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                            isGiaHan = "1";
                                        } else {
                                        }
                                        // DURATION
                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(pCERTIFICATION_PROFILE_ID), rsProfile);
                                        int sDuration = 0;
                                        if (rsProfile[0].length > 0) {
                                            sDuration = rsProfile[0][0].DURATION;
                                        }
                                        if(sDuration < 365)
                                        {
                                            is6Thang = "1";
                                            soNam = "6 tháng";
                                        } else {
                                            int subDuration = sDuration / 365;
                                            switch (subDuration) {
                                                case 1:
                                                    is1Nam = "1";
                                                    soNam = "1 năm";
                                                    break;
                                                case 2:
                                                    is2Nam = "1";
                                                    soNam = "2 năm";
                                                    break;
                                                case 3:
                                                    is3Nam = "1";
                                                    soNam = "3 năm";
                                                    break;
                                                case 4:
                                                    is4Nam = "1";
                                                    soNam = "4 năm";
                                                    break;
                                                default:
                                                    isKhac = "1";
                                                    NoiDungKhac = String.valueOf(sDuration);
                                                    soNam = String.valueOf(sDuration) + " tháng";
                                                    break;
                                            }
                                        }
                                        String isReceiveRegister = "0";
                                        String isReceiveEnter = "0";
                                        String isCheckRegisterInfo = EscapeUtils.CheckTextNull(request.getParameter("isCheckRegisterInfo"));
                                        if("1".equals(isCheckRegisterInfo)) {
                                            isReceiveRegister = "0";
                                            isReceiveEnter = "1";
                                        } else {
                                            isReceiveRegister = "1";
                                            isReceiveEnter = "0";
                                            sNameReceive=""; sAddressReceive=""; sPhoneReceive=""; sEmailReceive="";
                                        }
                                        int profileMonth = sDuration / 30;
                                        Province = CommonFunction.getStateOrProvinceInDN(SubjectDN);
                                        // XSLT
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_ENTERPRISE_REGISTRATION_PAPER);
                                        }
                                        if(!"".equals(pathXSLT)) {
                                            Config conf = new Config();
                                            String sNameFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoTemplate = outputDirectory + "/Images/" + sNameFile;
                                            File fileImage = new File(pathLogoTemplate);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_BACKGROUD_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            String pathXML = PrintFormFunction.createXMLRegistrationDNFinal2(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_ADDRESS_BILLING,
                                                PRINT_PHONE, PRINT_EMAIL, PRINT_REPRESENTATIVE, PRINT_ROLE, is1Nam, is2Nam, is3Nam, isKhac,
                                                NoiDungKhac, sNameReceive, sAddressReceive, sPhoneReceive, sEmailReceive, ThoiGianDiaDiem, soNam, is6Thang,
                                                is4Nam, String.valueOf(profileMonth), isReceiveRegister, isReceiveEnter);
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                            if (intResult[0] == 0) {
                                                strView = "0###" + sResultHTML;
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else { strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0"; }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "printchangeinfo": {
                                //<editor-fold defaultstate="collapsed" desc="printchangeinfo">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String PRINT_TAXCODE = request.getParameter("PRINT_TAXCODE");
                                    String PRINT_CMND = request.getParameter("PRINT_CMND");
                                    String PRINT_FULLNAME = request.getParameter("PRINT_FULLNAME");
                                    boolean isAccessAgency = true;
                                    int pPAST_CERTIFICATION_ID = 0;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    String sAGENT_ID;
                                    String SubjectDNOld=""; String SubjectDNNew="";
                                    CERTIFICATION[][] rsReq;
                                    rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pPAST_CERTIFICATION_ID = rsReq[0][0].PAST_CERTIFICATION_ID;
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        SubjectDNNew = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                            if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                isAccessAgency = false;
//                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        if(pPAST_CERTIFICATION_ID != 0 && pPAST_CERTIFICATION_ID != 1) {
                                            rsReq = new CERTIFICATION[1][];
                                            db.S_BO_CERTIFICATION_GET_INFO(String .valueOf(pPAST_CERTIFICATION_ID), sessLanguage, rsReq);
                                            if (rsReq[0].length > 0) {
                                                SubjectDNOld = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                            }
                                        }
                                        String sNameReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_FULLNAME"));
                                        String sAddressReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_ADDRESS"));
                                        String sPhoneReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_PHONE"));
                                        String sEmailReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_EMAIL"));
                                        String ThoiGianDiaDiem = request.getParameter("sDate");
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_CERTIFICATE_REVISION_PAPER);
                                        }
                                        String pathXML = PrintFormFunction.createXMLChangeInfoFinal(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_CMND,
                                            SubjectDNOld, SubjectDNNew, sNameReceive, sAddressReceive, sPhoneReceive, sEmailReceive, ThoiGianDiaDiem);
                                        String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                        if (intResult[0] == 0) {
                                            strView = "0###" + sResultHTML;
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            
                            case "printreissue_revoke": {
                                //<editor-fold defaultstate="collapsed" desc="printreissue_revoke">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String PRINT_TAXCODE = request.getParameter("PRINT_TAXCODE");
                                    String PRINT_CMND = request.getParameter("PRINT_CMND");
                                    String PRINT_FULLNAME = request.getParameter("PRINT_FULLNAME");
                                    String PRINT_ADDRESS = request.getParameter("PRINT_ADDRESS");
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_ATTR_TYPE_ID = 0;
                                    String sAGENT_ID;
                                    String sCERTIFICATION_SN = "";
                                    String sEFFECTIVE_DT = "";
                                    String sEXPIRATION_DT = "";
                                    CERTIFICATION[][] rsReq;
                                    rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_DETAIL(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        sCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        sEFFECTIVE_DT = EscapeUtils.CheckTextNull(rsReq[0][0].EFFECTIVE_DT);
                                        sEXPIRATION_DT = EscapeUtils.CheckTextNull(rsReq[0][0].EXPIRATION_DT);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                            if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                isAccessAgency = false;
//                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        String isRevokeType = "0";
                                        String isReissueType = "0";
                                        if(pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE) {
                                            isReissueType = "1";
                                        }
                                        if(pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE) {
                                            isRevokeType = "1";
                                        }
                                        String sNameReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_FULLNAME"));
                                        String sAddressReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_ADDRESS"));
                                        String sPhoneReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_PHONE"));
                                        String sEmailReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_EMAIL"));
                                        String ThoiGianDiaDiem = request.getParameter("sDate");
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_CERTIFICATE_REVOCATION_REISSUE_PAPER);
                                        }
                                        String pathXML = PrintFormFunction.createXMLReissueRevokeFinal(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_CMND,
                                            sNameReceive, sAddressReceive, sPhoneReceive, sEmailReceive, ThoiGianDiaDiem,
                                            isReissueType, isRevokeType, PRINT_ADDRESS, sCERTIFICATION_SN, sEFFECTIVE_DT, sEXPIRATION_DT);
                                        String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                        if (intResult[0] == 0) {
                                            strView = "0###" + sResultHTML;
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            
                            case "printregisterbusinessrar2": {
                                //<editor-fold defaultstate="collapsed" desc="printregisterbusinessrar2">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String PRINT_ADDRESS_BILLING = request.getParameter("PRINT_ADDRESS_BILLING");
                                    String PRINT_TAXCODE = request.getParameter("PRINT_TAXCODE");
                                    String PRINT_PHONE = request.getParameter("PRINT_PHONE");
                                    String PRINT_EMAIL = request.getParameter("PRINT_EMAIL");
                                    String PRINT_FULLNAME = request.getParameter("PRINT_FULLNAME");
                                    String PRINT_REPRESENTATIVE = request.getParameter("PRINT_REPRESENTATIVE");
                                    String PRINT_ROLE = request.getParameter("PRINT_ROLE");
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    int pCERTIFICATION_ATTR_TYPE_ID = 0;
                                    int pCERTIFICATION_OWNER_ID = 0;
                                    String sAGENT_ID;
                                    String isCapMoi = "0";
                                    String isGiaHan = "0";
                                    String is6Thang = "0";
                                    String is1Nam = "0";
                                    String is2Nam = "0";
                                    String is3Nam = "0";
                                    String is4Nam = "0";
                                    String isKhac = "0";
                                    String soNam = "";
                                    String NoiDungKhac = "";
                                    String SubjectDN = "";
                                    String Province = "";
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        SubjectDN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                            if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                isAccessAgency = false;
//                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
//                                        String sNameReceive = "";
//                                        String sAddressReceive = "";
//                                        String sPhoneReceive = "";
//                                        String sEmailReceive = "";
                                        String sNameReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_FULLNAME"));
                                        String sAddressReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_ADDRESS"));
                                        String sPhoneReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_PHONE"));
                                        String sEmailReceive = EscapeUtils.CheckTextNull(request.getParameter("PRINT_RETURN_EMAIL"));
                                        /*CERTIFICATION_OWNER[][] rsOwner = new CERTIFICATION_OWNER[1][];
                                        db.S_BO_CERTIFICATION_OWNER_DETAIL(String.valueOf(pCERTIFICATION_OWNER_ID), sessLanguage, rsOwner);
                                        if(rsOwner[0].length > 0)
                                        {
                                            sNameReceive = rsOwner[0][0].COMPANY_NAME;
                                            sAddressReceive = rsOwner[0][0].ADDRESS;
                                            sPhoneReceive = rsOwner[0][0].PHONE_CONTRACT;
                                            sEmailReceive = rsOwner[0][0].EMAIL_CONTRACT;
                                        }*/
                                        // NEW and RENEW
                                        if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                            isCapMoi = "1";
                                        } else if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                            isGiaHan = "1";
                                        } else {
                                        }
                                        // DURATION
                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(pCERTIFICATION_PROFILE_ID), rsProfile);
                                        int sDuration = 0;
                                        if (rsProfile[0].length > 0) {
                                            sDuration = rsProfile[0][0].DURATION;
                                        }
                                        if(sDuration < 365)
                                        {
                                            is6Thang = "1";
                                            soNam = "6 tháng";
                                        } else {
                                            int subDuration = sDuration / 365;
                                            switch (subDuration) {
                                                case 1:
                                                    is1Nam = "1";
                                                    soNam = "1 năm";
                                                    break;
                                                case 2:
                                                    is2Nam = "1";
                                                    soNam = "2 năm";
                                                    break;
                                                case 3:
                                                    is3Nam = "1";
                                                    soNam = "3 năm";
                                                    break;
                                                case 4:
                                                    is4Nam = "1";
                                                    soNam = "4 năm";
                                                    break;
                                                default:
                                                    isKhac = "1";
                                                    NoiDungKhac = String.valueOf(sDuration);
                                                    soNam = String.valueOf(sDuration) + " tháng";
                                                    break;
                                            }
                                        }
                                        Province = CommonFunction.getStateOrProvinceInDN(SubjectDN);
                                        String ThoiGianDiaDiem = request.getParameter("sDate");// "........., ngày.....tháng.....năm ...... ";
                                        // XSLT
                                        int[] intResult = new int[1];
                                        String pathRegisXSLT = "";
                                        String pathConfirmXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            pathRegisXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_ENTERPRISE_REGISTRATION_PAPER);
                                            pathConfirmXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_CONFIRMATION_PAPER);
                                        }
                                        if(!"".equals(pathRegisXSLT) && !"".equals(pathConfirmXSLT))
                                        {
                                            Config conf = new Config();
                                            String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
                                            int[] tempResCode;
//                                            tempResCode = new int[1];
//                                            byte[] imagedata = CommonFunction.getByteFromImage(pathLogoURL, tempResCode);
//                                            String base64Image = "";
//                                            if (tempResCode[0] == 0) {
//                                                base64Image = Base64.encodeBase64String(imagedata);
//                                            }
                                            File fileImage = new File(pathLogoURL);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathRegisXSLT = pathRegisXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathRegisXSLT = pathRegisXSLT.replace(Definitions.CONFIG_TAG_PRINT_BACKGROUD_LOGO, base64Image);
                                            pathRegisXSLT = pathRegisXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            pathConfirmXSLT = pathConfirmXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathConfirmXSLT = pathConfirmXSLT.replace(Definitions.CONFIG_TAG_PRINT_BACKGROUD_LOGO, base64Image);
                                            pathConfirmXSLT = pathConfirmXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            
                                            String pathRegisXML = PrintFormFunction.createXMLRegistrationDNFinal2(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_ADDRESS_BILLING,
                                                PRINT_PHONE, PRINT_EMAIL, PRINT_REPRESENTATIVE, PRINT_ROLE, is1Nam, is2Nam, is3Nam, isKhac,
                                                NoiDungKhac, sNameReceive, sAddressReceive, sPhoneReceive, sEmailReceive, ThoiGianDiaDiem, soNam, is6Thang,
                                                is4Nam, "", "", "");
                                            String sResultRegisHTML = PrintFormFunction.createStringHtmlInString(pathRegisXSLT, pathRegisXML, null, false, false, intResult);
                                            if (intResult[0] == 0) {
                                                String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_LOGO);
                                                String pathLogoTemplate = outputDirectory + "/Images/" + sNameFile;
                                                String pathConfirmXML = PrintFormFunction.createXMLConfirmInfo(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_REPRESENTATIVE,
                                                    PRINT_ROLE, "", ThoiGianDiaDiem);
                                                String sResultConfirmHTML = PrintFormFunction.createStringHtmlInStringExtend(pathConfirmXSLT, pathConfirmXML, null, false, false, intResult);
                                                String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                                if (!directory.exists()) {
                                                    directory.mkdir();
                                                }
                                                String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                                String sFileNameRegis = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REGISTRATION_TEMPLATE + CommonFunction.getDateFormat();
                                                String sFileNameConfirm = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_CONFIRMATION_TEMPLATE + CommonFunction.getDateFormat();
                                                String sPathRegisPDF_temp = pPathURL + sFileNameRegis + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                                                String sPathRegisPDF = pPathURL + sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                                                String sPathConfirmPDF_temp = pPathURL + sFileNameConfirm + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                                                String sPathConfirmPDF = pPathURL + sFileNameConfirm + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                                                // create html
    //                                            String sPathHTML = pPathURL+ sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
    //                                            PrintFormFunction.createFile(sPathHTML, sResultRegisHTML);
                                                //<editor-fold defaultstate="collapsed" desc="### PDF Process">
                                                tempResCode = new int[1];
                                                PrintFormFunction.CreatePDFFromHTML(sResultRegisHTML, sPathRegisPDF_temp, tempResCode);
                                                PrintFormFunction.AddBackgroundImageToPDF(sPathRegisPDF_temp, sPathRegisPDF, pathLogoTemplate, pathLogoTemplate, tempResCode);
                                                if(tempResCode[0] == 0)
                                                {
                                                    tempResCode = new int[1];
                                                    PrintFormFunction.CreatePDFFromHTML(sResultConfirmHTML, sPathConfirmPDF_temp, tempResCode);
                                                    PrintFormFunction.AddBackgroundImageToPDF(sPathConfirmPDF_temp, sPathConfirmPDF, pathLogoTemplate, pathLogoTemplate, tempResCode);
                                                    String sFileNameZip = pPathURL+ sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_RAR;
                                                    try (FileOutputStream fos = new FileOutputStream(sFileNameZip)) {
                                                        ZipOutputStream zos = new ZipOutputStream(fos);
                                                        CommonFunction.addToZipFile(sPathRegisPDF, zos);
                                                        CommonFunction.addToZipFile(sPathConfirmPDF, zos);
                                                        zos.close();
                                                    }
                                                    strView = "0###" + sFileNameZip + "###" + sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_RAR;
                                                } else {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                }
                                                //</editor-fold>
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else { strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0"; }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "printconfirminfo": {
                                //<editor-fold defaultstate="collapsed" desc="printconfirminfo">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String PRINT_ADDRESS_BILLING = request.getParameter("PRINT_ADDRESS_BILLING");
                                    String PRINT_TAXCODE = request.getParameter("PRINT_TAXCODE");
                                    String PRINT_CMND = request.getParameter("PRINT_CMND");
                                    String PRINT_FULLNAME = request.getParameter("PRINT_FULLNAME");
                                    String PRINT_REPRESENTATIVE = request.getParameter("PRINT_REPRESENTATIVE");
                                    String PRINT_ROLE = request.getParameter("PRINT_ROLE");
                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    String sAGENT_ID;
                                    String sCERTIFICATION_SN = "";
                                    String isCertType = "";
                                    String sDistrict = "";
                                    String sProvince = "";
                                    String sOrganization = "";
                                    String sUnit = "";
                                    String sPhone = "";
                                    String sEmail = "";
                                    String sThoigiansudung = "";
                                    DateFormat dfDate1 = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
                                    DateFormat dfDate2 = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY);
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        sCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        String sEffective = EscapeUtils.CheckTextNull(rsReq[0][0].EFFECTIVE_DT);
                                        String sExpire = EscapeUtils.CheckTextNull(rsReq[0][0].EXPIRATION_DT);
                                        if(!"".equals(sEffective) && !"".equals(sExpire)) {
                                            Date effect = dfDate1.parse(sEffective);
                                            Date expire = dfDate1.parse(sExpire);
                                                sThoigiansudung = dfDate2.format(effect) + " - " + dfDate2.format(expire);
                                        }
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NEWTEL)
                                            || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_EFY)
                                            || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_WINCA)) {
                                            if(rsReq[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF) {
                                                isCertType = "0";
                                            } else if(rsReq[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL) {
                                                isCertType = "1";
                                            } else if(rsReq[0][0].CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE) {
                                                isCertType = "2";
                                            }
                                            String sSubjectDN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                            if(!"".equals(sSubjectDN)) {
                                                sDistrict = CommonFunction.getLocationInDN(sSubjectDN);
                                                sProvince = CommonFunction.getStateOrProvinceInDN(sSubjectDN);
                                                sOrganization = CommonFunction.getORGANIZATIONInDN(sSubjectDN);
                                                sUnit = CommonFunction.getDepartmentInDN(sSubjectDN);
                                                sPhone = CommonFunction.getPhoneInDN(sSubjectDN);
                                                sEmail = CommonFunction.getEmailInDN(sSubjectDN);
                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        String ThoiGianDiaDiem = request.getParameter("sDate");// "........., ngày.....tháng.....năm ...... ";
                                        // XSLT
                                        int[] intResult = new int[1];
                                        String pathConfirmXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            pathConfirmXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_CONFIRMATION_PAPER);
                                        }
                                        if(!"".equals(pathConfirmXSLT)) {
                                            Config conf = new Config();
                                            String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
                                            File fileImage = new File(pathLogoURL);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathConfirmXSLT = pathConfirmXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathConfirmXSLT = pathConfirmXSLT.replace(Definitions.CONFIG_TAG_PRINT_BACKGROUD_LOGO, base64Image);
                                            pathConfirmXSLT = pathConfirmXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            pathConfirmXSLT = CommonFunction.printConfirmReplaceImgTag(pathConfirmXSLT);
                                            String pathConfirmXML = PrintFormFunction.createXMLConfirmInfoIsAddress(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_REPRESENTATIVE,
                                                PRINT_ROLE, sCERTIFICATION_SN, ThoiGianDiaDiem, PRINT_ADDRESS_BILLING, PRINT_CMND, "", "",
                                                "", sEmail, sPhone, sDistrict, sProvince, sOrganization, sUnit, isCertType, sThoigiansudung, "", "");
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathConfirmXSLT, pathConfirmXML, null, false, false, intResult);
                                            if (intResult[0] == 0) {
                                                strView = "0###" + sResultHTML;
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "printreportcertcontrol": {
                                //<editor-fold defaultstate="collapsed" desc="printreportcertcontrol">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sBRANCH_ID = sessionsa.getAttribute("sessBranchOfficeReportCertControl").toString().trim();
                                    String sMONTH = sessionsa.getAttribute("sessMountDateReportCertControl").toString().trim();
                                    String sYEAR = sessionsa.getAttribute("sessYearDateReportCertControl").toString().trim();
                                    String sFROMBRANCH_DESC = "";
                                    String sFROMBRANCH_ADDRESS = "";
                                    String sFROMBRANCH_REPRESENTATIVE = "";
                                    String sFROMBRANCH_ROLE = "";
                                    String sTOBRANCH_DESC = "";
                                    String sTOBRANCH_ADDRESS = "";
                                    String sTOBRANCH_REPRESENTATIVE = "";
                                    String sTOBRANCH_ROLE = "";
                                    String sREMARK1 = "";
                                    String sAMOUNT1 = "";
                                    String sNOTE1 = "";
                                    String sREMARK2 = "";
                                    String sAMOUNT2 = "";
                                    String sNOTE2 = "";
                                    String sREMARK3 = "";
                                    String sAMOUNT3 = "";
                                    String sNOTE3 = "";
                                    String sREMARK4 = "";
                                    String sAMOUNT4 = "";
                                    String sNOTE4 = "";
                                    String sREMARK5 = "";
                                    String sAMOUNT5 = "";
                                    String sNOTE5 = "";
                                    String sREMARK6 = "";
                                    String sAMOUNT6 = "";
                                    String sNOTE6 = "";
                                    String sREMARK7 = "";
                                    String sAMOUNT7 = "";
                                    String sNOTE7 = "";
                                    String sREMARK8 = "";
                                    String sAMOUNT8 = "";
                                    String sNOTE8 = "";
                                    
                                    double sInt_AMOUNT1=0;
                                    double sInt_AMOUNT2=0;
                                    double sInt_AMOUNT3=0;
                                    double sInt_AMOUNT4=0;
                                    double sInt_AMOUNT5=0;
                                    double sInt_AMOUNT6=0;
                                    double sInt_AMOUNT7=0;
                                    double sInt_AMOUNT8=0;
                                    double sTOPARENT_ID = 0;
                                    if (!"".equals(sBRANCH_ID)) {
                                        BRANCH[][] rsToBranch = new BRANCH[1][];
                                        db.S_BO_BRANCH_DETAIL(sBRANCH_ID, rsToBranch);
                                        if (rsToBranch[0].length > 0) {
                                            sTOPARENT_ID = rsToBranch[0][0].PARENT_ID;
                                            sTOBRANCH_DESC = EscapeUtils.CheckTextNull(rsToBranch[0][0].REMARK);
                                            sTOBRANCH_ADDRESS = EscapeUtils.CheckTextNull(rsToBranch[0][0].ADDRESS);
                                            sTOBRANCH_REPRESENTATIVE = EscapeUtils.CheckTextNull(rsToBranch[0][0].REPRESENTATIVE);
                                            sTOBRANCH_ROLE = EscapeUtils.CheckTextNull(rsToBranch[0][0].REPRESENTATIVE_POSITION);
                                        }
                                    }
                                    if (sTOPARENT_ID != 0) {
                                        BRANCH[][] rsFromBranch = new BRANCH[1][];
                                        db.S_BO_BRANCH_DETAIL(String.valueOf(sTOPARENT_ID), rsFromBranch);
                                        if (rsFromBranch[0].length > 0) {
                                            sFROMBRANCH_DESC = EscapeUtils.CheckTextNull(rsFromBranch[0][0].REMARK);
                                            sFROMBRANCH_ADDRESS = EscapeUtils.CheckTextNull(rsFromBranch[0][0].ADDRESS);
                                            sFROMBRANCH_REPRESENTATIVE = EscapeUtils.CheckTextNull(rsFromBranch[0][0].REPRESENTATIVE);
                                            sFROMBRANCH_ROLE = EscapeUtils.CheckTextNull(rsFromBranch[0][0].REPRESENTATIVE_POSITION);
                                        }
                                    }
                                    PAYMENT[][] rsReportBranch = new PAYMENT[1][];
                                    db.S_BO_PAYMENT_REPORT_PER_MONTH(sMONTH, sYEAR, sBRANCH_ID, sessLanguage, rsReportBranch);
                                    if (rsReportBranch[0].length > 0) {
                                        for (PAYMENT rsReportBranch1 : rsReportBranch[0]) {
                                            if (rsReportBranch1.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEBIT_AMOUNT) {
                                                sREMARK1 = EscapeUtils.CheckTextNull(rsReportBranch1.PAYMENT_TYPE_REMARK);
                                                sAMOUNT1 = String.valueOf(rsReportBranch1.AMOUNT);
                                                sNOTE1 = EscapeUtils.CheckTextNull(rsReportBranch1.NOTE);
                                                sInt_AMOUNT1 = rsReportBranch1.AMOUNT;
                                            }
                                            if (rsReportBranch1.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_TOTAL_AMOUNT) {
                                                sREMARK2 = EscapeUtils.CheckTextNull(rsReportBranch1.PAYMENT_TYPE_REMARK);
                                                sAMOUNT2 = String.valueOf(rsReportBranch1.AMOUNT);
                                                sNOTE2 = EscapeUtils.CheckTextNull(rsReportBranch1.NOTE);
                                                sInt_AMOUNT2 = rsReportBranch1.AMOUNT;
                                            }
                                            if (rsReportBranch1.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEPOSIT_DEVICE) {
                                                sREMARK3 = EscapeUtils.CheckTextNull(rsReportBranch1.PAYMENT_TYPE_REMARK);
                                                sAMOUNT3 = String.valueOf(rsReportBranch1.AMOUNT);
                                                sNOTE3 = EscapeUtils.CheckTextNull(rsReportBranch1.NOTE);
                                                sInt_AMOUNT3 = rsReportBranch1.AMOUNT;
                                            }
                                            if (rsReportBranch1.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEPOSIT_DEVICE_MINUS) {
                                                sREMARK4 = EscapeUtils.CheckTextNull(rsReportBranch1.PAYMENT_TYPE_REMARK);
                                                sAMOUNT4 = String.valueOf(rsReportBranch1.AMOUNT);
                                                sNOTE4 = EscapeUtils.CheckTextNull(rsReportBranch1.NOTE);
                                                sInt_AMOUNT4 = rsReportBranch1.AMOUNT;
                                            }
                                            if (rsReportBranch1.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_FILE_AMOUNT) {
                                                sREMARK5 = EscapeUtils.CheckTextNull(rsReportBranch1.PAYMENT_TYPE_REMARK);
                                                sAMOUNT5 = String.valueOf(rsReportBranch1.AMOUNT);
                                                sNOTE5 = EscapeUtils.CheckTextNull(rsReportBranch1.NOTE);
                                                sInt_AMOUNT5 = rsReportBranch1.AMOUNT;
                                            }
                                            if (rsReportBranch1.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_RETURN_FILE_AMOUNT) {
                                                sREMARK6 = EscapeUtils.CheckTextNull(rsReportBranch1.PAYMENT_TYPE_REMARK);
                                                sAMOUNT6 = String.valueOf(rsReportBranch1.AMOUNT);
                                                sNOTE6 = EscapeUtils.CheckTextNull(rsReportBranch1.NOTE);
                                                sInt_AMOUNT6 = rsReportBranch1.AMOUNT;
                                            }
                                            if (rsReportBranch1.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_PAID) {
                                                sREMARK7 = EscapeUtils.CheckTextNull(rsReportBranch1.PAYMENT_TYPE_REMARK);
                                                sAMOUNT7 = String.valueOf(rsReportBranch1.AMOUNT);
                                                sNOTE7 = EscapeUtils.CheckTextNull(rsReportBranch1.NOTE);
                                                sInt_AMOUNT7 = rsReportBranch1.AMOUNT;
                                            }
                                            if (rsReportBranch1.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_TOTAL) {
                                                sREMARK8 = EscapeUtils.CheckTextNull(rsReportBranch1.PAYMENT_TYPE_REMARK);
                                                sInt_AMOUNT8 = sInt_AMOUNT1 + sInt_AMOUNT2 - sInt_AMOUNT4
                                                    + sInt_AMOUNT5 + sInt_AMOUNT5 - sInt_AMOUNT6 - sInt_AMOUNT7;
                                                sAMOUNT8 = String.valueOf(sInt_AMOUNT8);
                                                sNOTE8 = EscapeUtils.CheckTextNull(rsReportBranch1.NOTE);
                                            }
                                        }
                                    }
                                    String hopDongKinhDoanh = ".....................";
                                    String strCADefault="";
                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy[0].length > 0) {
                                        for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                            if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT)) {
                                                strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                    int[] intResult = new int[1];
                                    String pathXSLT = "";
                                    CERTIFICATION_AUTHORITY_ATTR[][] rsCAAttr = new CERTIFICATION_AUTHORITY_ATTR[1][];
                                    db.S_BO_CERTIFICATION_AUTHORITY_ATTR_GET(strCADefault, rsCAAttr);
                                    if (rsCAAttr[0].length > 0) {
                                        for (CERTIFICATION_AUTHORITY_ATTR rsCAAttr1 : rsCAAttr[0]) {
                                            if (rsCAAttr1.CERTIFICATION_AUTHORITY_ATTR_TYPE_NAME.equals(Definitions.CONFIG_CA_TEMPLATE_REPORT_DEBT_CONTROL)) {
                                                pathXSLT = EscapeUtils.CheckTextNull(rsCAAttr1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                    Config conf = new Config();
                                    String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                    String queryString = getServletContext().getRealPath("/");
                                    String outputDirectory = queryString;
                                    String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
//                                    int[] tempResCode;
//                                    tempResCode = new int[1];
//                                    byte[] imagedata = CommonFunction.getByteFromImage(pathLogoURL, tempResCode);
//                                    String base64Image = "";
//                                    if (tempResCode[0] == 0) {
//                                        base64Image = Base64.encodeBase64String(imagedata);
//                                    }
                                    File fileImage = new File(pathLogoURL);
                                    String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                    pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                    String pathXML = PrintFormFunction.createXMLCertControlFinal("VNĐ", "",
                                            "....", "....", "........", hopDongKinhDoanh, sMONTH, sYEAR, sMONTH, sYEAR, sMONTH, sYEAR,
                                            "", "", sFROMBRANCH_DESC, sFROMBRANCH_ADDRESS, sFROMBRANCH_REPRESENTATIVE, sFROMBRANCH_ROLE,
                                            sTOBRANCH_DESC, sTOBRANCH_ADDRESS, sTOBRANCH_REPRESENTATIVE, sTOBRANCH_ROLE,
                                            sREMARK1, sAMOUNT1, sNOTE1, sREMARK2, sAMOUNT2, sNOTE2, sREMARK3, sAMOUNT3, sNOTE3,
                                            sREMARK4, sAMOUNT4, sNOTE4, sREMARK5, sAMOUNT5, sNOTE5, sREMARK6, sAMOUNT6, sNOTE6,
                                            sREMARK7, sAMOUNT7, sNOTE7, sREMARK8, sAMOUNT8, sNOTE8);
                                    String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                    if (intResult[0] == 0) {
                                        strView = "0###" + sResultHTML;
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "printreportcertlist": {
                                //<editor-fold defaultstate="collapsed" desc="printreportcertlist">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sBRANCH_ID = sessionsa.getAttribute("sessBranchOfficeReportCertList").toString().trim();
                                    String sMONTH = sessionsa.getAttribute("sessMonthReportCertList").toString().trim();
                                    String sYEAR = sessionsa.getAttribute("sessYearReportCertList").toString().trim();
                                    String PKI_FORMFACTOR = request.getSession(false).getAttribute("sessFormFactorReportCertList").toString().trim();
                                    String sUSER = sessionsa.getAttribute("sessUserReportCertList").toString().trim();
                                    String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
//                                    String idType = EscapeUtils.CheckTextNull(request.getParameter("idType"));
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(sUSER)) {
                                        sUSER = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(PKI_FORMFACTOR)) {
                                        PKI_FORMFACTOR = "";
                                    }
                                    CommonFunction.LogDebugString(log, "PrintReportCertList", "Year: " + sYEAR
                                        + "; Month: " + sMONTH + "; PKI_FORMFACTOR: " + PKI_FORMFACTOR + "; AGENCY: " + sBRANCH_ID);
                                    String pBRANCH_BENEFICIARY_ID = SessUserAgentID;
                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        pBRANCH_BENEFICIARY_ID=EscapeUtils.escapeHtmlSearch(sBRANCH_ID);
                                    }
                                    REPORT_PER_MONTH[][] rsReportBranch = new REPORT_PER_MONTH[1][];
//                                    int sCount=0;
//                                    if(idType.equals(Definitions.CONFIG_EXPORT_TYPE_REPORT_CERT_TOKEN))
//                                    {
//                                        sCount = db.S_BO_REPORT_PER_MONTH_TOTAL(EscapeUtils.escapeHtmlSearch(sMONTH),
//                                            EscapeUtils.escapeHtmlSearch(sYEAR), EscapeUtils.escapeHtmlSearch(sBRANCH_ID),sUSER);
//                                        if (sCount > 0) {
//                                            db.S_BO_REPORT_PER_MONTH_LIST(EscapeUtils.escapeHtmlSearch(sMONTH),
//                                                EscapeUtils.escapeHtmlSearch(sYEAR), EscapeUtils.escapeHtmlSearch(sBRANCH_ID),
//                                                sUSER, sessLanguage, rsReportBranch, Definitions.CONFIG_PAGE_SIZE_IPAGNO, sCount);
//                                        }
//                                    }
//                                    else if(idType.equals(Definitions.CONFIG_EXPORT_TYPE_REPORT_CERT_SIGNSERVER))
//                                    {
//                                        sCount = db.S_BO_REPORT_PER_MONTH_SS_TOTAL(EscapeUtils.escapeHtmlSearch(sMONTH),
//                                            EscapeUtils.escapeHtmlSearch(sYEAR), EscapeUtils.escapeHtmlSearch(sBRANCH_ID), sUSER);
//                                        if (sCount > 0) {
//                                            db.S_BO_REPORT_PER_MONTH_SS_LIST(EscapeUtils.escapeHtmlSearch(sMONTH),
//                                                EscapeUtils.escapeHtmlSearch(sYEAR), EscapeUtils.escapeHtmlSearch(sBRANCH_ID),
//                                                sUSER, sessLanguage, rsReportBranch, Definitions.CONFIG_PAGE_SIZE_IPAGNO, sCount);
//                                        }
//                                    }
                                    int sCount = db.S_BO_REPORT_PER_MONTH_TOTAL(EscapeUtils.escapeHtmlSearch(sMONTH),
                                        EscapeUtils.escapeHtmlSearch(sYEAR), EscapeUtils.escapeHtmlSearch(sBRANCH_ID),
                                        sUSER, PKI_FORMFACTOR, pBRANCH_BENEFICIARY_ID);
                                    if (sCount > 0) {
                                        db.S_BO_REPORT_PER_MONTH_LIST(EscapeUtils.escapeHtmlSearch(sMONTH),
                                            EscapeUtils.escapeHtmlSearch(sYEAR), EscapeUtils.escapeHtmlSearch(sBRANCH_ID),
                                            sUSER, PKI_FORMFACTOR, sessLanguage, rsReportBranch, Definitions.CONFIG_PAGE_SIZE_IPAGNO,
                                            sCount, pBRANCH_BENEFICIARY_ID);
                                    }
                                    if (sCount > 0) {
                                        String strCADefault="";
                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                        if (sessGeneralPolicy[0].length > 0) {
                                            for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT)) {
                                                    strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY_ATTR[][] rsCAAttr = new CERTIFICATION_AUTHORITY_ATTR[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_ATTR_GET(strCADefault, rsCAAttr);
                                        if (rsCAAttr[0].length > 0) {
                                            for (CERTIFICATION_AUTHORITY_ATTR rsCAAttr1 : rsCAAttr[0]) {
                                                if (rsCAAttr1.CERTIFICATION_AUTHORITY_ATTR_TYPE_NAME.equals(Definitions.CONFIG_CA_TEMPLATE_REPORT_CERTIFICATE_LIST)) {
                                                    pathXSLT = EscapeUtils.CheckTextNull(rsCAAttr1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        Config conf = new Config();
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                        String sFromDate = ".................";
                                        String sToDate = ".................";
                                        String sNote1 = "";
                                        String sNote2 = "";
                                        String pathXML = PrintFormFunction.createXMLCertListFinal(sFromDate, sToDate,
                                                rsReportBranch, sNote1, sNote2);
                                        String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                        if (intResult[0] == 0) {
                                            strView = "0###" + sResultHTML;
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "wordrecurringcert": {
                                //<editor-fold defaultstate="collapsed" desc="wordrecurringcert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    Config conf = new Config();
                                    String pNUMBER_HEADER = conf.GetPropertybyCode(Definitions.CONFIG_FILE_REPORT_PERIODIC_NUMBER_HEADER);
                                    String sNUMBER = CommonFunction.GetTimeMonthSearch() + "/" + CommonFunction.GetTimeYearSearch() + "/" + pNUMBER_HEADER;
                                    String sQUATER = EscapeUtils.CheckTextNull(sessionsa.getAttribute("sessMountDateNEACControl").toString().trim());
                                    String sYEAR = sessionsa.getAttribute("sessYearDateNEACControl").toString().trim();
                                    if(!"4".equals(sQUATER))
                                    {
                                        String sNUMBER_ENTERPRISE1 = "";
                                        String sNUMBER_STAFF1 = "";
                                        String sNUMBER_PERSONAL1 = "";
                                        String sSTATUS1 = "";
                                        String sSUM1 = "";
                                        String sNUMBER_ENTERPRISE2 = "";
                                        String sNUMBER_STAFF2 = "";
                                        String sNUMBER_PERSONAL2 = "";
                                        String sSTATUS2 = "";
                                        String sSUM2 = "";
                                        String sNUMBER_ENTERPRISE3 = "";
                                        String sNUMBER_STAFF3 = "";
                                        String sNUMBER_PERSONAL3 = "";
                                        String sSTATUS3 = "";
                                        String sSUM3 = "";
                                        String sDATE_TIME = request.getParameter("sDate");
                                        String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                        REPORT_RECURRING_NEAC[][] rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
                                        db.S_BO_REPORT_PERIODIC_LIST(sQUATER, sYEAR, sessLanguage, Integer.parseInt(isCALoad), rsReportRecurring);
                                        if (rsReportRecurring[0].length > 0)
                                        {
                                            sNUMBER_ENTERPRISE1 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
                                            sNUMBER_STAFF1 = rsReportRecurring[0][0].TOTAL_STAFF != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_STAFF) : "0";
                                            sNUMBER_PERSONAL1 = rsReportRecurring[0][0].TOTAL_PERSONAL!=0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_PERSONAL) : "0";
                                            sSTATUS1 = EscapeUtils.CheckTextNull(rsReportRecurring[0][0].STATUS);
                                            int intSum1 = rsReportRecurring[0][0].TOTAL_ENTERPRISE+rsReportRecurring[0][0].TOTAL_STAFF+rsReportRecurring[0][0].TOTAL_PERSONAL;
                                            sSUM1 = intSum1 != 0 ? com.convertMoney(intSum1) : "0";
                                            sNUMBER_ENTERPRISE2 = rsReportRecurring[0][1].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][1].TOTAL_ENTERPRISE) : "0";
                                            sNUMBER_STAFF2 = rsReportRecurring[0][1].TOTAL_STAFF != 0 ? com.convertMoney(rsReportRecurring[0][1].TOTAL_STAFF) : "0";
                                            sNUMBER_PERSONAL2 = rsReportRecurring[0][1].TOTAL_PERSONAL != 0 ? com.convertMoney(rsReportRecurring[0][1].TOTAL_PERSONAL) : "0";
                                            sSTATUS2 = EscapeUtils.CheckTextNull(rsReportRecurring[0][1].STATUS);
                                            int intSum2 = rsReportRecurring[0][1].TOTAL_ENTERPRISE+rsReportRecurring[0][1].TOTAL_STAFF+rsReportRecurring[0][1].TOTAL_PERSONAL;
                                            sSUM2 = intSum2 != 0 ? com.convertMoney(intSum2) : "0";
                                            sNUMBER_ENTERPRISE3 = rsReportRecurring[0][2].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][2].TOTAL_ENTERPRISE) : "0";
                                            sNUMBER_STAFF3 = rsReportRecurring[0][2].TOTAL_STAFF != 0 ? com.convertMoney(rsReportRecurring[0][2].TOTAL_STAFF) : "0";
                                            sNUMBER_PERSONAL3 = rsReportRecurring[0][2].TOTAL_PERSONAL != 0 ? com.convertMoney(rsReportRecurring[0][2].TOTAL_PERSONAL) : "0";
                                            sSTATUS3 = EscapeUtils.CheckTextNull(rsReportRecurring[0][2].STATUS);
                                            int intSum3 = rsReportRecurring[0][2].TOTAL_ENTERPRISE+rsReportRecurring[0][2].TOTAL_STAFF+rsReportRecurring[0][2].TOTAL_PERSONAL;
                                            sSUM3 = intSum3 != 0 ? com.convertMoney(intSum3) : "0";
                                        }
                                        String strCADefault="";
                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                        if (sessGeneralPolicy[0].length > 0) {
                                            for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT)) {
                                                    strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY_ATTR[][] rsCAAttr = new CERTIFICATION_AUTHORITY_ATTR[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_ATTR_GET(strCADefault, rsCAAttr);
                                        if (rsCAAttr[0].length > 0) {
                                            for (CERTIFICATION_AUTHORITY_ATTR rsCAAttr1 : rsCAAttr[0]) {
                                                if (rsCAAttr1.CERTIFICATION_AUTHORITY_ATTR_TYPE_NAME.equals(Definitions.CONFIG_CA_TEMPLATE_REPORT_PERIODIC)) {
                                                    pathXSLT = EscapeUtils.CheckTextNull(rsCAAttr1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        CommonFunction.LogDebugString(log, "WordRecurring", sQUATER);
                                        if(null == sQUATER) {} else switch (sQUATER) {
                                            case "1":
                                                sQUATER = "I";
                                                break;
                                            case "2":
                                                sQUATER = "II";
                                                break;
                                            case "3":
                                                sQUATER = "III";
                                                break;
                                            case "4":
                                                sQUATER = "IV";
                                                break;
                                            default:
                                                break;
                                        }
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_COMPANY_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_COMPANY_NAME, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SERVER_PRIMARY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOCALTION_SERVER_PRIMARY, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SERVER_BACKUP, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOCALTION_SERVER_BACKUP, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SIGNED_ROLE_RIGHT, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_SIGNED_ROLE_RIGHT, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SIGNED_COMPANY_REPRESEN_RIGHT, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_SIGNED_COMPANY_REPRESEN_RIGHT, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SIGNED_FULLNAME_RIGHT, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_SIGNED_FULLNAME_RIGHT, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SIGNED_SEALED_LEFT, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_SIGNED_SEALED_LEFT, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_UPPERCASE_COMPANY_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_COMPANY_NAME, sessLanguage).toUpperCase());
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_ADDRESS_COMPANY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_ADDRESS_COMPANY, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_PHONE_FAX_COMPANY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_PHONE_FAX_COMPANY, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_EMAIL_COMPANY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_EMAIL_COMPANY, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_WEBSITE_COMPANY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_WEBSITE_COMPANY, sessLanguage));
                                        String pathXML = PrintFormFunction.createXMLReportRecurringFinal(sNUMBER, sQUATER,sYEAR,
                                                sNUMBER_ENTERPRISE1, sNUMBER_STAFF1, sNUMBER_PERSONAL1, sSTATUS1, sSUM1, sNUMBER_ENTERPRISE2,
                                                sNUMBER_STAFF2, sNUMBER_PERSONAL2, sSTATUS2, sSUM2, sDATE_TIME, sNUMBER_ENTERPRISE3,
                                                sNUMBER_STAFF3, sNUMBER_PERSONAL3, sSTATUS3, sSUM3);
                                        String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                        String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                        File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                        if (!directory.exists()){
                                            directory.mkdir();
                                        }
                                        String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                        String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_PDF_PERIODIC + CommonFunction.getDateFormat();
                                        String sPathHTML = pPathURL+ sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
    //                                    String sPathPDF = pPathURL+ sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                                        String sPathWord = pPathURL + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                        if (intResult[0] == 0) {
                                            String[] sCode = new String[1];
                                            PrintFormFunction.convertWord(sResultHTML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""), sPathWord, sPathHTML, sCode);
                                            if(null == sCode[0])
                                            {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            } else switch (sCode[0]) {
                                                case "0":
                                                    strView = "0###" + sPathWord + "###" + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                                    break;
                                                default:
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                    break;
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                        }
                                    } else {
                                        String strCADefault="";
                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                        if (sessGeneralPolicy[0].length > 0) {
                                            for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT)) {
                                                    strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY_ATTR[][] rsCAAttr = new CERTIFICATION_AUTHORITY_ATTR[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_ATTR_GET(strCADefault, rsCAAttr);
                                        if (rsCAAttr[0].length > 0) {
                                            for (CERTIFICATION_AUTHORITY_ATTR rsCAAttr1 : rsCAAttr[0]) {
                                                if (rsCAAttr1.CERTIFICATION_AUTHORITY_ATTR_TYPE_NAME.equals(Definitions.CONFIG_CA_TEMPLATE_REPORT_PERIODIC_IV)) {
                                                    pathXSLT = EscapeUtils.CheckTextNull(rsCAAttr1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        if(!"".equals(pathXSLT))
                                        {
                                            String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                            REPORT_RECURRING_NEAC[][] rsRecurringIssue = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_EXPORT(sQUATER, sYEAR, "ISSUED", sessLanguage, Integer.parseInt(isCALoad), rsRecurringIssue);

                                            REPORT_RECURRING_NEAC[][] rsRecurringRevoke = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_EXPORT(sQUATER, sYEAR, "REVOKED", sessLanguage, Integer.parseInt(isCALoad), rsRecurringRevoke);
										
                                            REPORT_RECURRING_NEAC[][] rsRecurringOperation = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_EXPORT(sQUATER, sYEAR, "OPERATED", sessLanguage, Integer.parseInt(isCALoad), rsRecurringOperation);
                                            String sNUMBER_ISSUED_ENTERPRISE1 = "";
                                            String sNUMBER_ISSUED_ENTERPRISE2 = "";
                                            String sNUMBER_ISSUED_ENTERPRISE3 = "";
                                            String sNUMBER_ISSUED_ENTERPRISE4 = "";
                                            String sNUMBER_ISSUED_PERSONAL1 = "";
                                            String sNUMBER_ISSUED_PERSONAL2 = "";
                                            String sNUMBER_ISSUED_PERSONAL3 = "";
                                            String sNUMBER_ISSUED_PERSONAL4 = "";
                                            String sNUMBER_REVOKE_ENTERPRISE1 = "";
                                            String sNUMBER_REVOKE_ENTERPRISE2 = "";
                                            String sNUMBER_REVOKE_ENTERPRISE3 = "";
                                            String sNUMBER_REVOKE_ENTERPRISE4 = "";
                                            String sNUMBER_REVOKE_PERSONAL1 = "";
                                            String sNUMBER_REVOKE_PERSONAL2 = "";
                                            String sNUMBER_REVOKE_PERSONAL3 = "";
                                            String sNUMBER_REVOKE_PERSONAL4 = "";
                                            String sNUMBER_CONTROL_ENTERPRISE = "";
                                            String sNUMBER_CONTROL_PERSONAL = "";
                                            String sNUMBER_CONTROL_SUM = "";
                                            String sDATE_TIME = request.getParameter("sDate");
                                            REPORT_RECURRING_NEAC[][] rsReportRecurring;
                                            rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_LIST("1", sYEAR, sessLanguage, Integer.parseInt(isCALoad), rsReportRecurring);
                                            if (rsReportRecurring[0].length > 0)
                                            {
                                                sNUMBER_ISSUED_ENTERPRISE1 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
                                                int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
                                                sNUMBER_ISSUED_PERSONAL1 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
                                                sNUMBER_REVOKE_ENTERPRISE1 = rsReportRecurring[0][1].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][1].TOTAL_ENTERPRISE) : "0";
                                                int REVOKE_PERSONAL = rsReportRecurring[0][1].TOTAL_PERSONAL + rsReportRecurring[0][1].TOTAL_STAFF;
                                                sNUMBER_REVOKE_PERSONAL1 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
                                            }
                                            rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_LIST("2", sYEAR, sessLanguage, Integer.parseInt(isCALoad), rsReportRecurring);
                                            if (rsReportRecurring[0].length > 0)
                                            {
                                                sNUMBER_ISSUED_ENTERPRISE2 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
                                                int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
                                                sNUMBER_ISSUED_PERSONAL2 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
                                                sNUMBER_REVOKE_ENTERPRISE2 = rsReportRecurring[0][1].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][1].TOTAL_ENTERPRISE) : "0";
                                                int REVOKE_PERSONAL = rsReportRecurring[0][1].TOTAL_PERSONAL + rsReportRecurring[0][1].TOTAL_STAFF;
                                                sNUMBER_REVOKE_PERSONAL2 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
                                            }
                                            rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_LIST("3", sYEAR, sessLanguage, Integer.parseInt(isCALoad), rsReportRecurring);
                                            if (rsReportRecurring[0].length > 0)
                                            {
                                                sNUMBER_ISSUED_ENTERPRISE3 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
                                                int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
                                                sNUMBER_ISSUED_PERSONAL3 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
                                                sNUMBER_REVOKE_ENTERPRISE3 = rsReportRecurring[0][1].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][1].TOTAL_ENTERPRISE) : "0";
                                                int REVOKE_PERSONAL = rsReportRecurring[0][1].TOTAL_PERSONAL + rsReportRecurring[0][1].TOTAL_STAFF;
                                                sNUMBER_REVOKE_PERSONAL3 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
                                            }
                                            rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_LIST("4", sYEAR, sessLanguage, Integer.parseInt(isCALoad), rsReportRecurring);
                                            if (rsReportRecurring[0].length > 0)
                                            {
                                                sNUMBER_ISSUED_ENTERPRISE4 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
                                                int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
                                                sNUMBER_ISSUED_PERSONAL4 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
                                                sNUMBER_REVOKE_ENTERPRISE4 = rsReportRecurring[0][1].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][1].TOTAL_ENTERPRISE) : "0";
                                                int REVOKE_PERSONAL = rsReportRecurring[0][1].TOTAL_PERSONAL + rsReportRecurring[0][1].TOTAL_STAFF;
                                                sNUMBER_REVOKE_PERSONAL4 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
                                            }
                                            REPORT_CONTROL_NEAC[][] rsReportControl = new REPORT_CONTROL_NEAC[1][];
                                            db.S_BO_REPORT_NEAC_LIST("4", sYEAR, Integer.parseInt(isCALoad), rsReportControl);
                                            if (rsReportControl[0].length > 0)
                                            {
                                                sNUMBER_CONTROL_ENTERPRISE = rsReportControl[0][2].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportControl[0][2].TOTAL_ENTERPRISE) : "0";
                                                int SUM_PERSONAL = rsReportControl[0][2].TOTAL_PERSONAL + rsReportControl[0][2].TOTAL_STAFF;
                                                sNUMBER_CONTROL_PERSONAL = SUM_PERSONAL != 0 ? com.convertMoney(SUM_PERSONAL) : "0";
                                                int intSUM = rsReportControl[0][2].TOTAL_ENTERPRISE + rsReportControl[0][2].TOTAL_PERSONAL + rsReportControl[0][2].TOTAL_STAFF;
                                                sNUMBER_CONTROL_SUM = intSUM != 0 ? com.convertMoney(intSUM) : "0";
                                            }
                                            String pathXML = PrintFormFunction.createXMLReportRecurringIVFinal(sNUMBER, sQUATER, sYEAR,
                                                sNUMBER_ISSUED_ENTERPRISE1, sNUMBER_ISSUED_ENTERPRISE2, sNUMBER_ISSUED_ENTERPRISE3,
                                                sNUMBER_ISSUED_ENTERPRISE4, sNUMBER_ISSUED_PERSONAL1, sNUMBER_ISSUED_PERSONAL2,
                                                sNUMBER_ISSUED_PERSONAL3, sNUMBER_ISSUED_PERSONAL4, sNUMBER_REVOKE_ENTERPRISE1,
                                                sNUMBER_REVOKE_ENTERPRISE2, sNUMBER_REVOKE_ENTERPRISE3, sNUMBER_REVOKE_ENTERPRISE4,
                                                sNUMBER_REVOKE_PERSONAL1, sNUMBER_REVOKE_PERSONAL2, sNUMBER_REVOKE_PERSONAL3,
                                                sNUMBER_REVOKE_PERSONAL4, sNUMBER_CONTROL_ENTERPRISE, sNUMBER_CONTROL_PERSONAL,
                                                sNUMBER_CONTROL_SUM, sDATE_TIME,rsRecurringIssue, rsRecurringRevoke, rsRecurringOperation);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_UPPERCASE_COMPANY_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_COMPANY_NAME, sessLanguage).toUpperCase());
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_COMPANY_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_COMPANY_NAME, sessLanguage));
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SERVER_PRIMARY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOCALTION_SERVER_PRIMARY, sessLanguage));
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SERVER_BACKUP, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOCALTION_SERVER_BACKUP, sessLanguage));
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                            String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                            String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                            String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_PDF_PERIODIC + CommonFunction.getDateFormat();
                                            String sPathHTML = pPathURL+ sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
                                            String sPathWord = pPathURL + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                            if (intResult[0] == 0) {
                                                String[] sCode = new String[1];
                                                PrintFormFunction.convertWord(sResultHTML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""), sPathWord, sPathHTML, sCode);
                                                if(null == sCode[0])
                                                {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                } else switch (sCode[0]) {
                                                    case "0":
                                                        strView = "0###" + sPathWord + "###" + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                                        break;
                                                    default:
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                        break;
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "wordrecurringcert_new": {
                                //<editor-fold defaultstate="collapsed" desc="wordrecurringcert_new">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                    Config conf = new Config();
                                    String pNUMBER_HEADER = conf.GetPropertybyCode(Definitions.CONFIG_FILE_REPORT_PERIODIC_NUMBER_HEADER);
                                    String sNUMBER = CommonFunction.GetTimeMonthSearch() + "/" + CommonFunction.GetTimeYearSearch() + "/" + pNUMBER_HEADER;
                                    String sQUATER = EscapeUtils.CheckTextNull(sessionsa.getAttribute("sessMountDateNEACControl").toString().trim());
                                    String sYEAR = sessionsa.getAttribute("sessYearDateNEACControl").toString().trim();
                                    boolean isCallAs4 = false;
                                    boolean isCallOther4 = false;
                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_WINCA)){
                                        isCallAs4 = true;
                                        isCallOther4 = false;
                                    } else {
                                        if(!"4".equals(sQUATER)) {
                                            isCallOther4 = true;
                                            isCallAs4 = false;
                                        } else {
                                            isCallOther4 = false;
                                            isCallAs4 = true;
                                        }
                                    }
                                    if(isCallOther4 == true) {
                                        //<editor-fold defaultstate="collapsed" desc="### KHAC 4">
                                        String sDATE_TIME = request.getParameter("sDate");
                                        REPORT_RECURRING_NEAC[][] rsRecurringIssue = new REPORT_RECURRING_NEAC[1][];
                                        db.S_BO_REPORT_PERIODIC_EXPORT(sQUATER, sYEAR, "ISSUED", sessLanguage, Integer.parseInt(isCALoad), rsRecurringIssue);
                                        REPORT_RECURRING_NEAC[][] rsRecurringRevoke = new REPORT_RECURRING_NEAC[1][];
                                        db.S_BO_REPORT_PERIODIC_EXPORT(sQUATER, sYEAR, "REVOKED", sessLanguage, Integer.parseInt(isCALoad), rsRecurringRevoke);
                                        REPORT_RECURRING_NEAC[][] rsRecurringOperation = new REPORT_RECURRING_NEAC[1][];
                                        db.S_BO_REPORT_PERIODIC_EXPORT(sQUATER, sYEAR, "OPERATED", sessLanguage, Integer.parseInt(isCALoad), rsRecurringOperation);
                                        String strCADefault="";
                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                        if (sessGeneralPolicy[0].length > 0) {
                                            for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT)) {
                                                    strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY_ATTR[][] rsCAAttr = new CERTIFICATION_AUTHORITY_ATTR[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_ATTR_GET(strCADefault, rsCAAttr);
                                        if (rsCAAttr[0].length > 0) {
                                            for (CERTIFICATION_AUTHORITY_ATTR rsCAAttr1 : rsCAAttr[0]) {
                                                if (rsCAAttr1.CERTIFICATION_AUTHORITY_ATTR_TYPE_NAME.equals(Definitions.CONFIG_CA_TEMPLATE_REPORT_PERIODIC)) {
                                                    pathXSLT = EscapeUtils.CheckTextNull(rsCAAttr1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        if(null == sQUATER) {} else switch (sQUATER) {
                                            case "1":
                                                sQUATER = "I";
                                                break;
                                            case "2":
                                                sQUATER = "II";
                                                break;
                                            case "3":
                                                sQUATER = "III";
                                                break;
                                            default:
                                                break;
                                        }
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_COMPANY_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_COMPANY_NAME, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SERVER_PRIMARY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOCALTION_SERVER_PRIMARY, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SERVER_BACKUP, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOCALTION_SERVER_BACKUP, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SIGNED_ROLE_RIGHT, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_SIGNED_ROLE_RIGHT, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SIGNED_COMPANY_REPRESEN_RIGHT, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_SIGNED_COMPANY_REPRESEN_RIGHT, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SIGNED_FULLNAME_RIGHT, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_SIGNED_FULLNAME_RIGHT, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SIGNED_SEALED_LEFT, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_SIGNED_SEALED_LEFT, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_UPPERCASE_COMPANY_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_COMPANY_NAME, sessLanguage).toUpperCase());
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_ADDRESS_COMPANY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_ADDRESS_COMPANY, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_PHONE_FAX_COMPANY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_PHONE_FAX_COMPANY, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_EMAIL_COMPANY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_EMAIL_COMPANY, sessLanguage));
                                        pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_WEBSITE_COMPANY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_WEBSITE_COMPANY, sessLanguage));
                                        String pathXML = PrintFormFunction.createXMLReportRecurringFinal_New(sQUATER, sYEAR,
                                                rsRecurringIssue, rsRecurringRevoke, rsRecurringOperation, sDATE_TIME);
                                        String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                        String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                        File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                        if (!directory.exists()){
                                            directory.mkdir();
                                        }
                                        String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                        String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_PDF_PERIODIC + CommonFunction.getDateFormat();
                                        String sPathHTML = pPathURL+ sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
                                        String sPathWord = pPathURL + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                        if (intResult[0] == 0) {
                                            String[] sCode = new String[1];
                                            PrintFormFunction.convertWord(sResultHTML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""), sPathWord, sPathHTML, sCode);
                                            if(null == sCode[0])
                                            {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            } else switch (sCode[0]) {
                                                case "0":
                                                    strView = "0###" + sPathWord + "###" + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                                    break;
                                                default:
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                    break;
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                        }
                                        //</editor-fold>
                                    } else if(isCallAs4 == true) {
                                        String strCADefault="";
                                        GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                        if (sessGeneralPolicy[0].length > 0) {
                                            for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT)) {
                                                    strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY_ATTR[][] rsCAAttr = new CERTIFICATION_AUTHORITY_ATTR[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_ATTR_GET(strCADefault, rsCAAttr);
                                        if (rsCAAttr[0].length > 0) {
                                            for (CERTIFICATION_AUTHORITY_ATTR rsCAAttr1 : rsCAAttr[0]) {
                                                if (rsCAAttr1.CERTIFICATION_AUTHORITY_ATTR_TYPE_NAME.equals(Definitions.CONFIG_CA_TEMPLATE_REPORT_PERIODIC_IV)) {
                                                    pathXSLT = EscapeUtils.CheckTextNull(rsCAAttr1.VALUE);
                                                    break;
                                                }
                                            }
                                        }
                                        if(!"".equals(pathXSLT))
                                        {
                                            REPORT_RECURRING_NEAC[][] rsRecurringIssue = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_EXPORT(sQUATER, sYEAR, "ISSUED", sessLanguage, Integer.parseInt(isCALoad), rsRecurringIssue);

                                            REPORT_RECURRING_NEAC[][] rsRecurringRevoke = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_EXPORT(sQUATER, sYEAR, "REVOKED", sessLanguage, Integer.parseInt(isCALoad), rsRecurringRevoke);
                                            
                                            REPORT_RECURRING_NEAC[][] rsRecurringOperation = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_EXPORT(sQUATER, sYEAR, "OPERATED", sessLanguage, Integer.parseInt(isCALoad), rsRecurringOperation);
                                            
                                            String sNUMBER_ISSUED_ENTERPRISE1 = "";
                                            String sNUMBER_ISSUED_ENTERPRISE2 = "";
                                            String sNUMBER_ISSUED_ENTERPRISE3 = "";
                                            String sNUMBER_ISSUED_ENTERPRISE4 = "";
                                            String sNUMBER_ISSUED_PERSONAL1 = "";
                                            String sNUMBER_ISSUED_PERSONAL2 = "";
                                            String sNUMBER_ISSUED_PERSONAL3 = "";
                                            String sNUMBER_ISSUED_PERSONAL4 = "";
                                            String sNUMBER_REVOKE_ENTERPRISE1 = "";
                                            String sNUMBER_REVOKE_ENTERPRISE2 = "";
                                            String sNUMBER_REVOKE_ENTERPRISE3 = "";
                                            String sNUMBER_REVOKE_ENTERPRISE4 = "";
                                            String sNUMBER_REVOKE_PERSONAL1 = "";
                                            String sNUMBER_REVOKE_PERSONAL2 = "";
                                            String sNUMBER_REVOKE_PERSONAL3 = "";
                                            String sNUMBER_REVOKE_PERSONAL4 = "";
                                            String sNUMBER_CONTROL_ENTERPRISE = "";
                                            String sNUMBER_CONTROL_PERSONAL = "";
                                            String sNUMBER_CONTROL_SUM = "";
                                            String sDATE_TIME = request.getParameter("sDate");
                                            REPORT_RECURRING_NEAC[][] rsReportRecurring;
                                            rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_LIST("1", sYEAR, sessLanguage, Integer.parseInt(isCALoad), rsReportRecurring);
                                            if (rsReportRecurring[0].length > 0)
                                            {
                                                sNUMBER_ISSUED_ENTERPRISE1 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
                                                int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
                                                sNUMBER_ISSUED_PERSONAL1 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
                                                sNUMBER_REVOKE_ENTERPRISE1 = rsReportRecurring[0][2].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][2].TOTAL_ENTERPRISE) : "0";
                                                int REVOKE_PERSONAL = rsReportRecurring[0][2].TOTAL_PERSONAL + rsReportRecurring[0][2].TOTAL_STAFF;
                                                sNUMBER_REVOKE_PERSONAL1 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
                                            }
                                            rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_LIST("2", sYEAR, sessLanguage, Integer.parseInt(isCALoad), rsReportRecurring);
                                            if (rsReportRecurring[0].length > 0)
                                            {
                                                sNUMBER_ISSUED_ENTERPRISE2 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
                                                int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
                                                sNUMBER_ISSUED_PERSONAL2 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
                                                sNUMBER_REVOKE_ENTERPRISE2 = rsReportRecurring[0][2].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][2].TOTAL_ENTERPRISE) : "0";
                                                int REVOKE_PERSONAL = rsReportRecurring[0][2].TOTAL_PERSONAL + rsReportRecurring[0][2].TOTAL_STAFF;
                                                sNUMBER_REVOKE_PERSONAL2 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
                                            }
                                            rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_LIST("3", sYEAR, sessLanguage, Integer.parseInt(isCALoad), rsReportRecurring);
                                            if (rsReportRecurring[0].length > 0)
                                            {
                                                sNUMBER_ISSUED_ENTERPRISE3 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
                                                int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
                                                sNUMBER_ISSUED_PERSONAL3 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
                                                sNUMBER_REVOKE_ENTERPRISE3 = rsReportRecurring[0][2].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][2].TOTAL_ENTERPRISE) : "0";
                                                int REVOKE_PERSONAL = rsReportRecurring[0][2].TOTAL_PERSONAL + rsReportRecurring[0][2].TOTAL_STAFF;
                                                sNUMBER_REVOKE_PERSONAL3 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
                                            }
                                            rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
                                            db.S_BO_REPORT_PERIODIC_LIST("4", sYEAR, sessLanguage, Integer.parseInt(isCALoad), rsReportRecurring);
                                            if (rsReportRecurring[0].length > 0)
                                            {
                                                sNUMBER_ISSUED_ENTERPRISE4 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
                                                int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
                                                sNUMBER_ISSUED_PERSONAL4 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
                                                sNUMBER_REVOKE_ENTERPRISE4 = rsReportRecurring[0][2].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][2].TOTAL_ENTERPRISE) : "0";
                                                int REVOKE_PERSONAL = rsReportRecurring[0][2].TOTAL_PERSONAL + rsReportRecurring[0][2].TOTAL_STAFF;
                                                sNUMBER_REVOKE_PERSONAL4 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
                                            }
                                            REPORT_CONTROL_NEAC[][] rsReportControl = new REPORT_CONTROL_NEAC[1][];
                                            db.S_BO_REPORT_NEAC_LIST("4", sYEAR, Integer.parseInt(isCALoad), rsReportControl);
                                            if (rsReportControl[0].length > 0)
                                            {
                                                sNUMBER_CONTROL_ENTERPRISE = rsReportControl[0][2].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportControl[0][2].TOTAL_ENTERPRISE) : "0";
                                                int SUM_PERSONAL = rsReportControl[0][2].TOTAL_PERSONAL + rsReportControl[0][2].TOTAL_STAFF;
                                                sNUMBER_CONTROL_PERSONAL = SUM_PERSONAL != 0 ? com.convertMoney(SUM_PERSONAL) : "0";
                                                int intSUM = rsReportControl[0][2].TOTAL_ENTERPRISE + rsReportControl[0][2].TOTAL_PERSONAL + rsReportControl[0][2].TOTAL_STAFF;
                                                sNUMBER_CONTROL_SUM = intSUM != 0 ? com.convertMoney(intSUM) : "0";
                                            }
                                            String pathXML = PrintFormFunction.createXMLReportRecurringIVFinal(sNUMBER, sQUATER, sYEAR,
                                                sNUMBER_ISSUED_ENTERPRISE1, sNUMBER_ISSUED_ENTERPRISE2, sNUMBER_ISSUED_ENTERPRISE3,
                                                sNUMBER_ISSUED_ENTERPRISE4, sNUMBER_ISSUED_PERSONAL1, sNUMBER_ISSUED_PERSONAL2,
                                                sNUMBER_ISSUED_PERSONAL3, sNUMBER_ISSUED_PERSONAL4, sNUMBER_REVOKE_ENTERPRISE1,
                                                sNUMBER_REVOKE_ENTERPRISE2, sNUMBER_REVOKE_ENTERPRISE3, sNUMBER_REVOKE_ENTERPRISE4,
                                                sNUMBER_REVOKE_PERSONAL1, sNUMBER_REVOKE_PERSONAL2, sNUMBER_REVOKE_PERSONAL3,
                                                sNUMBER_REVOKE_PERSONAL4, sNUMBER_CONTROL_ENTERPRISE, sNUMBER_CONTROL_PERSONAL,
                                                sNUMBER_CONTROL_SUM, sDATE_TIME, rsRecurringIssue, rsRecurringRevoke, rsRecurringOperation);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_UPPERCASE_COMPANY_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_COMPANY_NAME, sessLanguage).toUpperCase());
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_COMPANY_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_COMPANY_NAME, sessLanguage));
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SERVER_PRIMARY, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOCALTION_SERVER_PRIMARY, sessLanguage));
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_SERVER_BACKUP, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOCALTION_SERVER_BACKUP, sessLanguage));
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                            String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                            String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                            String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_PDF_PERIODIC + CommonFunction.getDateFormat();
                                            String sPathHTML = pPathURL+ sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
                                            String sPathWord = pPathURL + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                            if (intResult[0] == 0) {
                                                String[] sCode = new String[1];
                                                PrintFormFunction.convertWord(sResultHTML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""), sPathWord, sPathHTML, sCode);
                                                if(null == sCode[0])
                                                {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                } else switch (sCode[0]) {
                                                    case "0":
                                                        strView = "0###" + sPathWord + "###" + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                                        break;
                                                    default:
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                        break;
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        }
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "wordcontrolcert": {
                                //<editor-fold defaultstate="collapsed" desc="wordcontrolcert">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    Config conf = new Config();
                                    String pNUMBER_HEADER = conf.GetPropertybyCode(Definitions.CONFIG_FILE_REPORT_PERIODIC_NUMBER_HEADER);
                                    String sNUMBER = CommonFunction.GetTimeMonthSearch() + "/" + CommonFunction.GetTimeYearSearch() + "/" + pNUMBER_HEADER;
                                    String sQUATER = EscapeUtils.CheckTextNull(sessionsa.getAttribute("sessMountDateNEACControl").toString().trim());
                                    String sYEAR = sessionsa.getAttribute("sessYearDateNEACControl").toString().trim();
                                    String sCONTENT1 = "";
                                    String sNUMBER_CERT1 = "";
                                    String sCONTENT2 = "";
                                    String sNUMBER_CERT2 = "";
                                    String sCONTENT3 = "";
                                    String sNUMBER_CERT3 = "";
                                    String sDATE_TIME = request.getParameter("sDate");
                                    String sPREFIX_CONTENT = request.getParameter("sPrefixContent");
                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                    REPORT_CONTROL_NEAC[][] rsReportControl = new REPORT_CONTROL_NEAC[1][];
                                    db.S_BO_REPORT_NEAC_LIST(sQUATER, sYEAR, Integer.parseInt(isCALoad), rsReportControl);
                                    if (rsReportControl[0].length > 0) {
                                        sCONTENT1 = sPREFIX_CONTENT + EscapeUtils.CheckTextNull(rsReportControl[0][0].TO_DATE);
                                        sNUMBER_CERT1 = rsReportControl[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportControl[0][0].TOTAL_ENTERPRISE) : "0";
                                        sCONTENT2 = sPREFIX_CONTENT + EscapeUtils.CheckTextNull(rsReportControl[0][1].TO_DATE);
                                        sNUMBER_CERT2 = rsReportControl[0][1].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportControl[0][1].TOTAL_ENTERPRISE) : "0";
                                        sCONTENT3 = sPREFIX_CONTENT + EscapeUtils.CheckTextNull(rsReportControl[0][2].TO_DATE);
                                        sNUMBER_CERT3 = rsReportControl[0][2].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportControl[0][2].TOTAL_ENTERPRISE) : "0";
                                    }
                                    String strCADefault="";
                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy[0].length > 0) {
                                        for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                            if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT)) {
                                                strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                    int[] intResult = new int[1];
                                    String pathXSLT = "";
                                    CERTIFICATION_AUTHORITY_ATTR[][] rsCAAttr = new CERTIFICATION_AUTHORITY_ATTR[1][];
                                    db.S_BO_CERTIFICATION_AUTHORITY_ATTR_GET(strCADefault, rsCAAttr);
                                    if (rsCAAttr[0].length > 0) {
                                        for (CERTIFICATION_AUTHORITY_ATTR rsCAAttr1 : rsCAAttr[0]) {
                                            if (rsCAAttr1.CERTIFICATION_AUTHORITY_ATTR_TYPE_NAME.equals(Definitions.CONFIG_CA_TEMPLATE_REPORT_NEAC)) {
                                                pathXSLT = EscapeUtils.CheckTextNull(rsCAAttr1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                    if(null == sQUATER) {} else switch (sQUATER) {
                                        case "1":
                                            sQUATER = "I";
                                            break;
                                        case "2":
                                            sQUATER = "II";
                                            break;
                                        case "3":
                                            sQUATER = "III";
                                            break;
                                        case "4":
                                            sQUATER = "IV";
                                            break;
                                        default:
                                            break;
                                    }
                                    pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_COMPANY_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_COMPANY_NAME, sessLanguage));
                                    pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_UPPERCASE_COMPANY_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_COMPANY_NAME, sessLanguage).toLowerCase());
                                    String pathXML = PrintFormFunction.createXMLReportControlFinal(sNUMBER, sQUATER, sYEAR,
                                        sCONTENT1, sNUMBER_CERT1, sCONTENT2, sNUMBER_CERT2, sCONTENT3, sNUMBER_CERT3, sDATE_TIME);
                                    String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_PDF_CONTROL + CommonFunction.getDateFormat();
                                    String sPathHTML = pPathURL+ sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
//                                    String sPathPDF = pPathURL+ sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                                    String sPathWord = pPathURL + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                    if (intResult[0] == 0) {
                                        String[] sCode = new String[1];
//                                        PrintFormFunction.convertPdf(sResultHTML, sPathHTML, sPathPDF, sCode);
                                        PrintFormFunction.convertWord(sResultHTML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""), sPathWord, sPathHTML, sCode);
                                        if(null == sCode[0])
                                        {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                        } else switch (sCode[0]) {
                                            case "0":
                                                strView = "0###" + sPathWord + "###" + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                                break;
                                            default:
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                break;
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "wordconfirminfo": {
                                //<editor-fold defaultstate="collapsed" desc="wordconfirminfo">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String PRINT_ADDRESS_BILLING = request.getParameter("PRINT_ADDRESS_BILLING");
                                    String PRINT_TAXCODE = request.getParameter("PRINT_TAXCODE");
                                    String PRINT_CMND = request.getParameter("PRINT_CMND");
//                                    String PRINT_PHONE = request.getParameter("PRINT_PHONE");
//                                    String PRINT_EMAIL = request.getParameter("PRINT_EMAIL");
                                    String PRINT_FULLNAME = request.getParameter("PRINT_FULLNAME");
                                    String PRINT_REPRESENTATIVE = request.getParameter("PRINT_REPRESENTATIVE");
                                    String PRINT_ROLE = request.getParameter("PRINT_ROLE");
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    String sAGENT_ID;
                                    String sCERTIFICATION_SN = "";
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        sCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        String ThoiGianDiaDiem = request.getParameter("sDate");// "........., ngày.....tháng.....năm ...... ";
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_CONFIRMATION_PAPER);
                                        }
                                        if(!"".equals(pathXSLT)) {
                                            Config conf = new Config();
                                            String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
//                                            int[] tempResCode;
//                                            tempResCode = new int[1];
//                                            byte[] imagedata = CommonFunction.getByteFromImage(pathLogoURL, tempResCode);
//                                            String base64Image = "";
//                                            if (tempResCode[0] == 0) {
//                                                base64Image = Base64.encodeBase64String(imagedata);
//                                            }
                                            File fileImage = new File(pathLogoURL);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
//                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_BACKGROUD_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            pathXSLT = CommonFunction.printConfirmReplaceImgTag(pathXSLT);
                                            String pathConfirmXML = PrintFormFunction.createXMLConfirmInfoIsAddress(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_REPRESENTATIVE,
                                                PRINT_ROLE, sCERTIFICATION_SN, ThoiGianDiaDiem, PRINT_ADDRESS_BILLING, PRINT_CMND, "", "", "", "", "", "", "", "", "", "", "", "", "");
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathConfirmXML, null, false, false, intResult);
                                            String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                            String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                            String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_COMFIRMATION + CommonFunction.getDateFormat();
                                            String sPathHTML = pPathURL+ sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
                                            String sPathWord = pPathURL + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                            if (intResult[0] == 0) {
                                                String[] sCode = new String[1];
                                                PrintFormFunction.convertWordHasImage(sResultHTML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""), sPathWord, sPathHTML, sCode);
                                                if(null == sCode[0])
                                                {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                } else switch (sCode[0]) {
                                                    case "0":
                                                        strView = "0###" + sPathWord + "###" + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                                        break;
                                                    default:
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                        break;
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "wordregisterpersonal": {
                                //<editor-fold defaultstate="collapsed" desc="wordregisterpersonal">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String TO_DATE = request.getParameter("TO_DATE");
                                    String TO_PLACE = request.getParameter("TO_PLACE");
                                    String TO_ADDRESS = EscapeUtils.CheckTextNull(request.getParameter("TO_ADDRESS"));
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    int pCERTIFICATION_PURPOSE_ID = 0;
                                    int pCERTIFICATION_ATTR_TYPE_ID = 0;
                                    String sAGENT_ID;
                                    String TO_CMND = "";
                                    String TO_CMNDHC = "";
                                    String TO_HC = "";
                                    String TO_CCCD = "";
                                    String IsCMND = "0";
                                    String IsHC = "0";
                                    String IsCCCD = "0";
                                    String isCapMoi = "0";
                                    String isGiaHan = "0";
                                    String is1Nam = "0";
                                    String is2Nam = "0";
                                    String is3Nam = "0";
                                    String isKhac = "0";
                                    String NoiDungKhac = "";
                                    String SubjectDN = "";
                                    String HoTen = "";
                                    String staffChucVu = "";
                                    String staffPhongBan = "";
                                    String staffToChuc = "";
                                    String DiaChi = "";
                                    String Province = "";
                                    String staffDiaChi = "";
                                    String staffMST = "";
                                    String Email = "";
                                    String Mobile = "";
                                    String staffEmail = "";
                                    String staffMobile = "";
                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        TO_CMND = EscapeUtils.CheckTextNull(rsReq[0][0].P_ID);
                                        TO_CCCD = EscapeUtils.CheckTextNull(rsReq[0][0].P_EID);
                                        TO_HC = EscapeUtils.CheckTextNull(rsReq[0][0].PASSPORT);
                                        SubjectDN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_PURPOSE_ID = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                        pCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
//                                            if (!sAGENT_ID.equals(SessUserAgentID)) {
//                                                isAccessAgency = false;
//                                            }
                                        }
                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_CODESIGNING) {
                                            HoTen = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                        } else if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_CODESIGNING) {
                                            HoTen = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                            staffToChuc = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                            staffMST = CommonReferServlet.swapPrefixEnterpriseToVN(rsReq[0][0].ENTERPRISE_ID);
//                                            if ("".equals(staffMST)) {
//                                                staffMST = EscapeUtils.CheckTextNull(rsReq[0][0].BUDGET_CODE);
//                                            }
//                                            if ("".equals(staffMST)) {
//                                                staffMST = EscapeUtils.CheckTextNull(rsReq[0][0].DECISION);
//                                            }
                                        } else {
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        // CMND HC
                                        if (!"".equals(TO_CMND)) {
                                            IsCMND = "1";
                                            TO_CMNDHC = TO_CMND;
                                        }
                                        if (!"".equals(TO_HC)) {
                                            IsHC = "1";
                                            TO_CMNDHC = TO_HC;
                                        }
                                        if (!"".equals(TO_CCCD)) {
                                            IsCCCD = "1";
                                            TO_CMNDHC = TO_CCCD;
                                        }
                                        // NEW and RENEW
                                        if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                            isCapMoi = "1";
                                        } else if (pCERTIFICATION_ATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                            isGiaHan = "1";
                                        } else {
                                        }
                                        // DURATION
                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(pCERTIFICATION_PROFILE_ID), rsProfile);
                                        int sDuration = 0;
                                        if (rsProfile[0].length > 0) {
                                            sDuration = rsProfile[0][0].DURATION;
                                        }
                                        int subDuration = sDuration / 365;
                                        if (subDuration == 1) {
                                            is1Nam = "1";
                                        } else if (subDuration == 2) {
                                            is2Nam = "1";
                                        } else if (subDuration == 3) {
                                            is3Nam = "1";
                                        } else {
                                            isKhac = "1";
                                            NoiDungKhac = String.valueOf(sDuration);
                                        }
                                        SubjectDN = SubjectDN.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID, Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE);
                                        staffChucVu = CommonFunction.getRoleInDN(SubjectDN);
                                        Email = CommonFunction.getEmailInDN(SubjectDN);
                                        staffPhongBan = CommonFunction.getDepartmentInDN(SubjectDN);
                                        Province = CommonFunction.getStateOrProvinceInDN(SubjectDN);
                                        DiaChi = Province;
                                        if(!"".equals(CommonFunction.getLocationInDN(SubjectDN).trim())) {
                                            DiaChi = CommonFunction.getLocationInDN(SubjectDN).trim() + ", " + DiaChi;
                                        }
                                        String ThoiGianDiaDiem = request.getParameter("sDate");// "........., ngày.....tháng.....năm ...... ";
                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_CODESIGNING) {
                                            HoTen = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                        } else if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_CODESIGNING) {
                                            staffEmail = Email;
                                            staffMobile = Mobile;
                                            staffDiaChi = DiaChi;
                                        } else {
                                        }
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_PERSONAL_REGISTRATION_PAPER);
                                        }
                                        if(!"".equals(pathXSLT)) {
                                            Config conf = new Config();
                                            String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
//                                            int[] tempResCode;
//                                            tempResCode = new int[1];
//                                            byte[] imagedata = CommonFunction.getByteFromImage(pathLogoURL, tempResCode);
//                                            String base64Image = "";
//                                            if (tempResCode[0] == 0) {
//                                                base64Image = Base64.encodeBase64String(imagedata);
//                                            }
                                            File fileImage = new File(pathLogoURL);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_BACKGROUD_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            String pathXML = PrintFormFunction.createXMLRegistrationCNFinal(HoTen, IsCMND, IsHC, TO_CMNDHC,
                                                TO_DATE, TO_PLACE, TO_ADDRESS, Mobile, isCapMoi, isGiaHan, is1Nam,
                                                is2Nam, is3Nam, isKhac, NoiDungKhac, staffChucVu, staffPhongBan,
                                                staffToChuc, staffDiaChi, staffMST, staffEmail, staffMobile, ThoiGianDiaDiem, IsCCCD);
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                            String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                            String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                            String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_COMFIRMATION + CommonFunction.getDateFormat();
                                            String sPathHTML = pPathURL+ sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
                                            String sPathWord = pPathURL + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                            if (intResult[0] == 0) {
                                                String[] sCode = new String[1];
                                                PrintFormFunction.convertWordHasImage(sResultHTML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""), sPathWord, sPathHTML, sCode);
                                                if(null == sCode[0])
                                                {
                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                } else switch (sCode[0]) {
                                                    case "0":
                                                        strView = "0###" + sPathWord + "###" + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
                                                        break;
                                                    default:
                                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                                        break;
                                                }
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "wordrecurringcertiv": {
                                //<editor-fold defaultstate="collapsed" desc="wordrecurringcertiv - cancel">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
//                                    Config conf = new Config();
//                                    String pNUMBER_HEADER = conf.GetPropertybyCode(Definitions.CONFIG_FILE_REPORT_PERIODIC_NUMBER_HEADER);
//                                    String sNUMBER = CommonFunction.GetTimeMonthSearch() + "/" + CommonFunction.GetTimeYearSearch() + "/" + pNUMBER_HEADER;
//                                    String sQUATER = EscapeUtils.CheckTextNull(sessionsa.getAttribute("sessMountDateNEACControl").toString().trim());
//                                    String sYEAR = sessionsa.getAttribute("sessYearDateNEACControl").toString().trim();
//                                    if("4".equals(sQUATER))
//                                    {
//                                        String sNUMBER_ISSUED_ENTERPRISE1 = "";
//                                        String sNUMBER_ISSUED_ENTERPRISE2 = "";
//                                        String sNUMBER_ISSUED_ENTERPRISE3 = "";
//                                        String sNUMBER_ISSUED_ENTERPRISE4 = "";
//                                        String sNUMBER_ISSUED_PERSONAL1 = "";
//                                        String sNUMBER_ISSUED_PERSONAL2 = "";
//                                        String sNUMBER_ISSUED_PERSONAL3 = "";
//                                        String sNUMBER_ISSUED_PERSONAL4 = "";
//                                        String sNUMBER_REVOKE_ENTERPRISE1 = "";
//                                        String sNUMBER_REVOKE_ENTERPRISE2 = "";
//                                        String sNUMBER_REVOKE_ENTERPRISE3 = "";
//                                        String sNUMBER_REVOKE_ENTERPRISE4 = "";
//                                        String sNUMBER_REVOKE_PERSONAL1 = "";
//                                        String sNUMBER_REVOKE_PERSONAL2 = "";
//                                        String sNUMBER_REVOKE_PERSONAL3 = "";
//                                        String sNUMBER_REVOKE_PERSONAL4 = "";
//                                        String sNUMBER_CONTROL_ENTERPRISE = "";
//                                        String sNUMBER_CONTROL_PERSONAL = "";
//                                        String sNUMBER_CONTROL_SUM = "";
//                                        String sDATE_TIME = request.getParameter("sDate");
//                                        REPORT_RECURRING_NEAC[][] rsReportRecurring;
//                                        rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
//                                        db.S_BO_REPORT_PERIODIC_LIST("1", sYEAR, sessLanguage, rsReportRecurring);
//                                        if (rsReportRecurring[0].length > 0)
//                                        {
//                                            sNUMBER_ISSUED_ENTERPRISE1 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
//                                            int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
//                                            sNUMBER_ISSUED_PERSONAL1 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
//                                            sNUMBER_REVOKE_ENTERPRISE1 = rsReportRecurring[0][1].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][1].TOTAL_ENTERPRISE) : "0";
//                                            int REVOKE_PERSONAL = rsReportRecurring[0][1].TOTAL_PERSONAL + rsReportRecurring[0][1].TOTAL_STAFF;
//                                            sNUMBER_REVOKE_PERSONAL1 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
//                                        }
//                                        rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
//                                        db.S_BO_REPORT_PERIODIC_LIST("2", sYEAR, sessLanguage, rsReportRecurring);
//                                        if (rsReportRecurring[0].length > 0)
//                                        {
//                                            sNUMBER_ISSUED_ENTERPRISE2 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
//                                            int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
//                                            sNUMBER_ISSUED_PERSONAL2 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
//                                            sNUMBER_REVOKE_ENTERPRISE2 = rsReportRecurring[0][1].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][1].TOTAL_ENTERPRISE) : "0";
//                                            int REVOKE_PERSONAL = rsReportRecurring[0][1].TOTAL_PERSONAL + rsReportRecurring[0][1].TOTAL_STAFF;
//                                            sNUMBER_REVOKE_PERSONAL2 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
//                                        }
//                                        rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
//                                        db.S_BO_REPORT_PERIODIC_LIST("3", sYEAR, sessLanguage, rsReportRecurring);
//                                        if (rsReportRecurring[0].length > 0)
//                                        {
//                                            sNUMBER_ISSUED_ENTERPRISE3 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
//                                            int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
//                                            sNUMBER_ISSUED_PERSONAL3 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
//                                            sNUMBER_REVOKE_ENTERPRISE3 = rsReportRecurring[0][1].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][1].TOTAL_ENTERPRISE) : "0";
//                                            int REVOKE_PERSONAL = rsReportRecurring[0][1].TOTAL_PERSONAL + rsReportRecurring[0][1].TOTAL_STAFF;
//                                            sNUMBER_REVOKE_PERSONAL3 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
//                                        }
//                                        rsReportRecurring = new REPORT_RECURRING_NEAC[1][];
//                                        db.S_BO_REPORT_PERIODIC_LIST("4", sYEAR, sessLanguage, rsReportRecurring);
//                                        if (rsReportRecurring[0].length > 0)
//                                        {
//                                            sNUMBER_ISSUED_ENTERPRISE4 = rsReportRecurring[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][0].TOTAL_ENTERPRISE) : "0";
//                                            int ISSUED_PERSONAL = rsReportRecurring[0][0].TOTAL_PERSONAL + rsReportRecurring[0][0].TOTAL_STAFF;
//                                            sNUMBER_ISSUED_PERSONAL4 = ISSUED_PERSONAL != 0 ? com.convertMoney(ISSUED_PERSONAL) : "0";
//                                            sNUMBER_REVOKE_ENTERPRISE4 = rsReportRecurring[0][1].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportRecurring[0][1].TOTAL_ENTERPRISE) : "0";
//                                            int REVOKE_PERSONAL = rsReportRecurring[0][1].TOTAL_PERSONAL + rsReportRecurring[0][1].TOTAL_STAFF;
//                                            sNUMBER_REVOKE_PERSONAL4 = REVOKE_PERSONAL != 0 ? com.convertMoney(REVOKE_PERSONAL) : "0";
//                                        }
//                                        REPORT_CONTROL_NEAC[][] rsReportControl = new REPORT_CONTROL_NEAC[1][];
//                                        db.S_BO_REPORT_NEAC_LIST("4", sYEAR, rsReportControl);
//                                        if (rsReportControl[0].length > 0)
//                                        {
//                                            sNUMBER_CONTROL_ENTERPRISE = rsReportControl[0][0].TOTAL_ENTERPRISE != 0 ? com.convertMoney(rsReportControl[0][0].TOTAL_ENTERPRISE) : "0";
//                                            int SUM_PERSONAL = rsReportControl[0][0].TOTAL_PERSONAL + rsReportControl[0][0].TOTAL_STAFF;
//                                            sNUMBER_CONTROL_PERSONAL = SUM_PERSONAL != 0 ? com.convertMoney(SUM_PERSONAL) : "0";
//                                            int intSUM = rsReportControl[0][0].TOTAL_ENTERPRISE + rsReportControl[0][0].TOTAL_PERSONAL + rsReportControl[0][0].TOTAL_STAFF;
//                                            sNUMBER_CONTROL_SUM = intSUM != 0 ? com.convertMoney(intSUM) : "0";
//                                        }
//                                        String strCADefault="";
//                                        GENERAL_POLICY[][] rs = new GENERAL_POLICY[1][];
//                                        db.S_BO_GENERAL_POLICY_LIST(sessLanguage, rs);
//                                        if (rs[0].length > 0) {
//                                            for (GENERAL_POLICY rsPolicy1 : rs[0]) {
//                                                if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_CA_DEFAULT_FOR_EXPORT)) {
//                                                    strCADefault = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                        int[] intResult = new int[1];
//                                        String pathXSLT = "";
//                                        CERTIFICATION_AUTHORITY_ATTR[][] rsCAAttr = new CERTIFICATION_AUTHORITY_ATTR[1][];
//                                        db.S_BO_CERTIFICATION_AUTHORITY_ATTR_GET(strCADefault, rsCAAttr);
//                                        if (rsCAAttr[0].length > 0) {
//                                            for (CERTIFICATION_AUTHORITY_ATTR rsCAAttr1 : rsCAAttr[0]) {
//                                                if (rsCAAttr1.CERTIFICATION_AUTHORITY_ATTR_TYPE_NAME.equals(Definitions.CONFIG_CA_TEMPLATE_REPORT_PERIODIC_IV)) {
//                                                    pathXSLT = EscapeUtils.CheckTextNull(rsCAAttr1.VALUE);
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                        String pathXML = PrintFormFunction.createXMLReportRecurringIVFinal(sNUMBER, sYEAR,
//                                            sNUMBER_ISSUED_ENTERPRISE1, sNUMBER_ISSUED_ENTERPRISE2, sNUMBER_ISSUED_ENTERPRISE3,
//                                            sNUMBER_ISSUED_ENTERPRISE4, sNUMBER_ISSUED_PERSONAL1, sNUMBER_ISSUED_PERSONAL2,
//                                            sNUMBER_ISSUED_PERSONAL3, sNUMBER_ISSUED_PERSONAL4, sNUMBER_REVOKE_ENTERPRISE1,
//                                            sNUMBER_REVOKE_ENTERPRISE2, sNUMBER_REVOKE_ENTERPRISE3, sNUMBER_REVOKE_ENTERPRISE4,
//                                            sNUMBER_REVOKE_PERSONAL1, sNUMBER_REVOKE_PERSONAL2, sNUMBER_REVOKE_PERSONAL3,
//                                            sNUMBER_REVOKE_PERSONAL4, sNUMBER_CONTROL_ENTERPRISE, sNUMBER_CONTROL_PERSONAL,
//                                            sNUMBER_CONTROL_SUM, sDATE_TIME);
//                                        String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
//                                        String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
//                                        String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
//                                        String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_PDF_PERIODIC + CommonFunction.getDateFormat();
//                                        String sPathHTML = pPathURL+ sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_HTML;
//                                        String sPathWord = pPathURL + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
//                                        if (intResult[0] == 0) {
//                                            String[] sCode = new String[1];
//                                            PrintFormFunction.convertWord(sResultHTML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", ""), sPathWord, sPathHTML, sCode);
//                                            if(null == sCode[0])
//                                            {
//                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
//                                            } else switch (sCode[0]) {
//                                                case "0":
//                                                    strView = "0###" + sPathWord + "###" + sFileName + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_WORD;
//                                                    break;
//                                                default:
//                                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
//                                                    break;
//                                            }
//                                        } else {
//                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
//                                        }
//                                    }
//                                } else {
//                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
//                                }
                                //</editor-fold>
                                break;
                            }
                            // change ICA
                            case "printconfirminforedrect": {
                                //<editor-fold defaultstate="collapsed" desc="printconfirminforedrect">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    String ThoiGianDiaDiem = EscapeUtils.CheckTextNull(request.getParameter("thoigiandiadiem"));
                                    String PRINT_ADDRESS_BILLING = "";
                                    String PRINT_TAXCODE = "";
                                    String PRINT_CMND = "";
                                    String PRINT_FULLNAME = "";
                                    String PRINT_REPRESENTATIVE = "";
                                    String PRINT_ROLE = "";
                                    String sPIDPlace = "";
                                    String sPIDDate = "";
                                    String sPhone = "";
                                    String sEmail = "";
                                    String sSubjectDN = "";
                                    String sIssuerDN = "";
                                    String sDATE_CONTRACT = "";
                                    String sTokenSN = "";
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PURPOSE_ID = 0;
                                    String sAGENT_ID;
                                    String sCERTIFICATION_SN = "";
                                    CERTIFICATION[][] rsReq;
                                    rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        sCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        if(!"".equals(ThoiGianDiaDiem)){
                                            String sDay = "";
                                            String sMount = "";
                                            String sYear = "";
                                            String strEFFECTIVE_DT = EscapeUtils.CheckTextNull(rsReq[0][0].EFFECTIVE_DT);
                                            if(!"".equals(strEFFECTIVE_DT)){
                                                sDay = strEFFECTIVE_DT.substring(0,2);
                                                sMount = strEFFECTIVE_DT.substring(3,5);
                                                sYear = strEFFECTIVE_DT.substring(6,10);
                                                ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[DD]",sDay);
                                                ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[MM]",sMount);
                                                ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[YYYY]",sYear);
                                            } else {
                                                String strCREATED_DT = EscapeUtils.CheckTextNull(rsReq[0][0].CREATED_DT);
                                                sDay = strCREATED_DT.substring(0,2);
                                                sMount = strCREATED_DT.substring(3,5);
                                                sYear = strCREATED_DT.substring(6,10);
                                                ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[DD]",sDay);
                                                ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[MM]",sMount);
                                                ThoiGianDiaDiem = ThoiGianDiaDiem.replace("[YYYY]",sYear);
                                            }
                                        }
                                        sPhone = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                        sEmail = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                        PRINT_TAXCODE = rsReq[0][0].ENTERPRISE_ID;
                                        PRINT_FULLNAME = EscapeUtils.CheckTextNull((rsReq[0][0].COMPANY_NAME));
                                        String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                            sSubjectDN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                            CERTIFICATION[][] rsInner = new CERTIFICATION[1][];
                                            db.S_BO_CERTIFICATION_DETAIL(sID, sessLanguage, rsInner);
                                            if (rsInner[0].length > 0) {
                                                sIssuerDN = EscapeUtils.CheckTextNull(rsInner[0][0].ISSUER_SUBJECT);
                                                sTokenSN = EscapeUtils.CheckTextNull(rsInner[0][0].TOKEN_SN);
                                                sDATE_CONTRACT = EscapeUtils.CheckTextNull(rsInner[0][0].PRINT_EFFECTIVE_DT) + " - " + EscapeUtils.CheckTextNull(rsInner[0][0].PRINT_EXPIRATION_CONTRACT_DT);
                                            }
                                        }
                                        if("".equals(PRINT_FULLNAME)) {
                                            PRINT_FULLNAME = EscapeUtils.CheckTextNull((rsReq[0][0].PERSONAL_NAME));
                                        }
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                        pCERTIFICATION_PURPOSE_ID = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                        String isCaNhan = "";
                                        String isCaNhanThuocToChuc = "";
                                        String isToChuc = "";
                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_CODESIGNING) {
                                            isCaNhan = "1";
                                            PRINT_TAXCODE = rsReq[0][0].PERSONAL_ID;
                                        } else if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_CODESIGNING) {
                                            isCaNhanThuocToChuc = "1";
                                        } else if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_ENTERPRISE_GOV
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING_GOV) {
                                            isToChuc = "1";
                                        }
                                        String sPrfileContact = EscapeUtils.CheckTextNull(rsReq[0][0].PROFILE_CONTACT_INFO);
                                        if(!"".equals(sPrfileContact)) {
                                            ObjectMapper oMapperParse = new ObjectMapper();
                                            ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                            if(profileContact != null) {
                                                PRINT_REPRESENTATIVE = CommonFunction.replaceCharaterSpecialJson(profileContact.RepresentativeName, false);
                                                sPIDPlace = CommonFunction.replaceCharaterSpecialJson(profileContact.PIDIssuedBy, false);
                                                sPIDDate = EscapeUtils.CheckTextNull(profileContact.PIDDate);
                                                PRINT_CMND = EscapeUtils.CheckTextNull(profileContact.PID);
                                                PRINT_ROLE = CommonFunction.replaceCharaterSpecialJson(profileContact.Position, false);
                                                if("1".equals(isToChuc) || "1".equals(isCaNhanThuocToChuc)){
                                                    PRINT_ADDRESS_BILLING = CommonFunction.replaceCharaterSpecialJson(profileContact.AddressLicense, false);
                                                }
                                                if("1".equals(isCaNhan)){
                                                    PRINT_ADDRESS_BILLING = CommonFunction.replaceCharaterSpecialJson(profileContact.Address, false);
                                                }
                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
//                                        String ThoiGianDiaDiem = "........., ngày.....tháng.....năm ...... ";
                                        int[] intResult = new int[1];
                                        String pathConfirmXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            pathConfirmXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_CONFIRMATION_PAPER);
                                        }
                                        if(!"".equals(pathConfirmXSLT)) {
                                            Config conf = new Config();
                                            String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
                                            File fileImage = new File(pathLogoURL);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathConfirmXSLT = pathConfirmXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathConfirmXSLT = pathConfirmXSLT.replace(Definitions.CONFIG_TAG_PRINT_BACKGROUD_LOGO, base64Image);
                                            pathConfirmXSLT = pathConfirmXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            pathConfirmXSLT = CommonFunction.printConfirmReplaceImgTag(pathConfirmXSLT);
                                            String pathConfirmXML = PrintFormFunction.createXMLConfirmInfoIsAddress(PRINT_FULLNAME, PRINT_TAXCODE, PRINT_REPRESENTATIVE,
                                                PRINT_ROLE, sCERTIFICATION_SN, ThoiGianDiaDiem, PRINT_ADDRESS_BILLING, PRINT_CMND,
                                                sPIDPlace, sPIDDate, sPhone, sEmail, "", "", "", "", "", "",sDATE_CONTRACT, sSubjectDN, sIssuerDN);
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathConfirmXSLT, pathConfirmXML, null, false, false, intResult);
                                            if (intResult[0] == 0) {
                                                strView = "0###" + sResultHTML;
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0";
                                        }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "printregisterredrect": {
                                //<editor-fold defaultstate="collapsed" desc="printregisterredrect">
                                String anticsrf = request.getParameter("CsrfToken");
                                if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                    String sID = request.getParameter("id");
                                    // check agency
                                    boolean isAccessAgency = true;
                                    int pCERTIFICATION_AUTHORITY_ID = 0;
                                    int pCERTIFICATION_PROFILE_ID = 0;
                                    int pCERTIFICATION_PURPOSE_ID = 0;
                                    int pCERTIFICATION_ATTR_TYPE_ID = 0;
                                    int pPAST_CERTIFICATION_ID = 0;
                                    String sAGENT_ID;
                                    String TO_CMND = "";
                                    String TO_DATE = "";
                                    String TO_PLACE = "";
                                    String CN_ADDRESS = "";
                                    String DN_ADDRESS = "";
                                    String isCapMoi = "0";
                                    String isGiaHan = "0";
                                    String isCapLai = "0";
                                    String isTamDung = "0";
                                    String isKhoiPhuc = "0";
                                    String isThuHoi = "0";
                                    String isCaNhan = "0";
                                    String isToChuc = "0";
                                    String isCaNhanThuocToChuc = "0";
                                    String is1Nam = "0";
                                    String is2Nam = "0";
                                    String is3Nam = "0";
                                    String is4Nam = "0";
                                    String CertSN = "";
                                    String SubjectDN = "";
                                    
                                    String sNameCN = ""; String sChucVu = ""; String sCMNDHC = ""; String NgayCap = "";
                                    String sDLPL = ""; String sChucVuDN = ""; String sCMNDHCDN = ""; String NgayCapDN = "";
                                    String NoiCap = ""; String NoiCapDN = ""; String DiaChiCN = ""; String MobileCN = ""; String EmailCN = "";
                                    String sNameDN = ""; String MST = ""; String DiaChiDN = "";
                                    String DiaChiChangeInfoOld = "";
                                    String DiaChiChangeInfoNew = "";
                                    String sNameCNChangeInfo = "";
                                    String MobileDN = ""; String EmailDN = "";
                                    String sNameDNOld = "";
                                    String sNameDNNew = "";
                                    String sCMNDHCOld = "";
                                    String sCMNDHCNew = "";
                                    String sEmailOld = "";
                                    String sSDTOld = "";
                                    String MobileContact = ""; String EmailContact = "";
                                    String sEmailNew = "";
                                    String sSDTNew = "";
                                    String sFormFactorName = "";
                                    boolean isCallChangeInfoPrint = false;
                                    boolean getCertSNOld = false;
                                    int intATTR_TYPE_ID = 0;
                                    CERTIFICATION[][] rsReq;
                                    rsReq = new CERTIFICATION[1][];
                                    db.S_BO_CERTIFICATION_GET_INFO(sID, sessLanguage, rsReq);
                                    if (rsReq[0].length > 0) {
                                        SubjectDN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                        sEmailNew = CommonFunction.getEmailInDN(SubjectDN);
                                        sSDTNew = CommonFunction.getPhoneInDN(SubjectDN);
                                        DiaChiChangeInfoNew = CommonFunction.getLocationInDN(SubjectDN);
                                        pCERTIFICATION_AUTHORITY_ID = rsReq[0][0].CERTIFICATION_AUTHORITY_ID;
                                        pCERTIFICATION_PURPOSE_ID = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                        pCERTIFICATION_ATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        pPAST_CERTIFICATION_ID = rsReq[0][0].PAST_CERTIFICATION_ID;
                                        pCERTIFICATION_PROFILE_ID = rsReq[0][0].CERTIFICATION_PROFILE_ID;
                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                        sFormFactorName = rsReq[0][0].PKI_FORMFACTOR_NAME;
                                        EmailCN =EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                        MobileCN=EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                        CertSN=EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                        }
                                        String sPrfileContact = EscapeUtils.CheckTextNull(rsReq[0][0].PROFILE_CONTACT_INFO);
                                        if(!"".equals(sPrfileContact)) {
                                            ObjectMapper oMapperParse = new ObjectMapper();
                                            ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                            if(profileContact != null) {
                                                sNameCN = CommonFunction.replaceCharaterSpecialJson(profileContact.RepresentativeName, false);
                                                sChucVu = CommonFunction.replaceCharaterSpecialJson(profileContact.Position, false);
                                                sCMNDHC = EscapeUtils.CheckTextNull(profileContact.PID);
                                                NgayCap = EscapeUtils.CheckTextNull(profileContact.PIDDate);
                                                NoiCap = CommonFunction.replaceCharaterSpecialJson(profileContact.PIDIssuedBy, false);
                                                DiaChiCN = CommonFunction.replaceCharaterSpecialJson(profileContact.Address, false);
                                                DiaChiDN = CommonFunction.replaceCharaterSpecialJson(profileContact.AddressLicense, false);
                                                if(pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF) {
                                                    sDLPL = CommonFunction.replaceCharaterSpecialJson(profileContact.RepresentativeName, false);
                                                    sChucVuDN = CommonFunction.replaceCharaterSpecialJson(profileContact.Position, false);
                                                    sCMNDHCDN = EscapeUtils.CheckTextNull(profileContact.PID);
                                                    NgayCapDN = EscapeUtils.CheckTextNull(profileContact.PIDDate);
                                                    NoiCapDN = CommonFunction.replaceCharaterSpecialJson(profileContact.PIDIssuedBy, false);
                                                }
                                            }
                                        }
                                        if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_CODESIGNING) {
                                            isCaNhan = "1";
                                            sCMNDHCNew = rsReq[0][0].PERSONAL_ID;
                                            sNameDNNew = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                        } else if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SIGNSERVER
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SSL
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_CODESIGNING) {
                                            isCaNhanThuocToChuc = "1";
                                            sNameDN = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                            sNameDNNew = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                            MST = rsReq[0][0].ENTERPRISE_ID;
                                            sCMNDHCNew = rsReq[0][0].PERSONAL_ID;
                                        } else if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_ENTERPRISE_GOV
                                            || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING_GOV) {
                                            isToChuc = "1";
                                            sNameDN = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                            sNameDNNew = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                            MST = rsReq[0][0].ENTERPRISE_ID;
                                        } else {
                                        }
                                        if("1".equals(isCaNhan)) {
                                            sNameCN = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                            sCMNDHC = rsReq[0][0].PERSONAL_ID;
                                            DiaChiDN = "";
                                        }
                                        if("1".equals(isCaNhanThuocToChuc)) {
                                            sNameCN = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                            sChucVu = CommonFunction.getRoleInDN(SubjectDN);
                                            sCMNDHC = rsReq[0][0].PERSONAL_ID;
                                            NgayCap = "";
                                            NoiCap = "";
                                        }
                                        intATTR_TYPE_ID = rsReq[0][0].CERTIFICATION_ATTR_TYPE_ID;
                                        if(intATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION
                                            || intATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_BUY_MORE) {
                                            isCapMoi = "1";
                                        }
                                        if(intATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL) {
                                            isGiaHan = "1";
                                            getCertSNOld = true;
                                        }
                                        if(intATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO) {
                                            isCallChangeInfoPrint = true;
                                        }
                                        if(intATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REVOKE){
                                            isThuHoi = "1";
                                        }
                                        if(intATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REISSUE){
                                            isCapLai = "1";
                                            getCertSNOld = true;
                                        }
                                        if(intATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE
                                            || intATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE) {
                                            isTamDung = "1";
                                        }
                                        if(intATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RECOVERED) {
                                            isKhoiPhuc = "1";
                                        }
                                        if(isCallChangeInfoPrint == true) {
                                            rsReq = new CERTIFICATION[1][];
                                            db.S_BO_CERTIFICATION_GET_INFO(String.valueOf(pPAST_CERTIFICATION_ID), sessLanguage, rsReq);
                                            if (rsReq[0].length > 0) {
                                                String sDNOld = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                                if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL
                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SIGNSERVER
                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_SSL
                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL_CODESIGNING) {
                                                    sNameDNOld = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                                    sCMNDHCOld = rsReq[0][0].PERSONAL_ID;
                                                } else if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF
                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SIGNSERVER
                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_SSL
                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF_CODESIGNING) {
                                                    sNameDNOld = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                                    sCMNDHCOld = rsReq[0][0].PERSONAL_ID;
                                                    MST = rsReq[0][0].ENTERPRISE_ID;
                                                } else if (pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE
                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_ENTERPRISE_GOV
                                                    || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_CODE_SIGNING_GOV) {
                                                    sNameDNOld = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                                    MST = rsReq[0][0].ENTERPRISE_ID;
                                                } else {
                                                }
                                                sEmailOld = CommonFunction.getEmailInDN(sDNOld);
                                                sSDTOld = CommonFunction.getPhoneInDN(sDNOld);
                                                DiaChiChangeInfoOld = CommonFunction.getLocationInDN(sDNOld);
                                                String sPrfileContactOld = EscapeUtils.CheckTextNull(rsReq[0][0].PROFILE_CONTACT_INFO);
                                                if(!"".equals(sPrfileContactOld)) {
                                                    ObjectMapper oMapperParse = new ObjectMapper();
                                                    ProfileContactInfoJson profileContactOld = oMapperParse.readValue(sPrfileContactOld, ProfileContactInfoJson.class);
                                                    if(profileContactOld != null) {
                                                        sNameCNChangeInfo = CommonFunction.replaceCharaterSpecialJson(profileContactOld.RepresentativeName, false);
                                                        sNameCN = CommonFunction.replaceCharaterSpecialJson(profileContactOld.RepresentativeName, false);
                                                    }
                                                }
                                                CertSN=EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                                sNameDN = sNameDNOld;
                                                sCMNDHC = sCMNDHCOld;
                                            }
                                        }
                                        if(getCertSNOld == true) {
                                            rsReq = new CERTIFICATION[1][];
                                            db.S_BO_CERTIFICATION_GET_INFO(String.valueOf(pPAST_CERTIFICATION_ID), sessLanguage, rsReq);
                                            if (rsReq[0].length > 0) {
                                                CertSN=EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                            }
                                        }
                                    } else {
                                        isAccessAgency = false;
                                    }
                                    if (isAccessAgency == true) {
                                        // DURATION
                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(String.valueOf(pCERTIFICATION_PROFILE_ID), rsProfile);
                                        int sDuration = 0;
                                        if (rsProfile[0].length > 0) {
                                            sDuration = rsProfile[0][0].DURATION;
                                        }
                                        int subDuration = sDuration / 365;
                                        switch (subDuration) {
                                            case 1:
                                                is1Nam = "1";
                                                break;
                                            case 2:
                                                is2Nam = "1";
                                                break;
                                            case 3:
                                                is3Nam = "1";
                                                break;
                                            default:
                                                is4Nam = "1";
                                                break;
                                        }
                                        SubjectDN = SubjectDN.replace(Definitions.CONFIG_COMPONENT_DN_TAG_UID, Definitions.CONFIG_COMPONENT_DN_TAG_UID_BEFORE);
                                        MobileDN = CommonFunction.getPhoneInDN(SubjectDN);
                                        EmailDN = CommonFunction.getEmailInDN(SubjectDN);
                                        String ThoiGianDiaDiem = "........., ngày.....tháng.....năm ...... ";
                                        int[] intResult = new int[1];
                                        String pathXSLT = "";
                                        CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                        db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(String.valueOf(pCERTIFICATION_AUTHORITY_ID), rsCA);
                                        if (rsCA[0].length > 0) {
                                            if(isCallChangeInfoPrint == true) {
                                                pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_CERTIFICATE_REVISION_PAPER);
                                            } else {
                                                pathXSLT = EscapeUtils.CheckTextNull(rsCA[0][0].TEMPLATE_PERSONAL_REGISTRATION_PAPER);
                                            }
                                        }
                                        if(!"".equals(pathXSLT)) {
                                            Config conf = new Config();
                                            String sNameLogoFile = conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_LOGO_NAME, sessLanguage);
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoURL = outputDirectory + "/Images/" + sNameLogoFile;
                                            File fileImage = new File(pathLogoURL);
                                            String base64Image = CommonFunction.encodeFileToBase64Binary(fileImage);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_FLOATING_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_BACKGROUD_LOGO, base64Image);
                                            pathXSLT = pathXSLT.replace(Definitions.CONFIG_TAG_PRINT_CA_NAME, conf.GetPrintPropertybyCode(Definitions.CONFIG_TEMPLATE_PRINT_CA_NAME, sessLanguage));
                                            String pathXML = "";
                                            if(isCallChangeInfoPrint == true)
                                            {
                                                if("1".equals(isCaNhan)){sNameDN = sNameCN;}
                                                MobileContact = MobileCN;
                                                EmailContact = EmailCN;
                                                if(sNameDNOld.equals(sNameDNNew)){sNameDNOld = "";sNameDNNew = "";}
                                                if(sCMNDHCOld.equals(sCMNDHCNew)){sCMNDHCOld = "";sCMNDHCNew = "";}
                                                if(sEmailOld.trim().equals(sEmailNew.trim())){
                                                    sEmailOld = "";sEmailNew = "";
                                                }
                                                if(sSDTOld.trim().equals(sSDTNew.trim())){
                                                     sSDTOld = "";sSDTNew = "";
                                                }
                                                if(DiaChiChangeInfoOld.trim().equals(DiaChiChangeInfoNew.trim())){DiaChiChangeInfoOld = "";DiaChiChangeInfoNew = "";}
                                                pathXML = PrintFormFunction.createXMLChangeInfoRedirect(sNameDN, MST, sCMNDHC, CertSN, sNameDNOld, sNameDNNew, sCMNDHCOld, sCMNDHCNew,
                                                    sEmailOld, sEmailNew, sSDTOld, sSDTNew, DiaChiChangeInfoOld,
                                                    DiaChiChangeInfoNew, sNameCNChangeInfo, sChucVu, MobileContact, EmailContact, ThoiGianDiaDiem);
                                            } else {
                                                if("1".equals(isCaNhan)){
                                                    sNameDN = "";
                                                    MST = "";
                                                    DiaChiDN = "";
                                                    sCMNDHCDN = "";
                                                    NgayCapDN = "";
                                                    NoiCapDN = "";
                                                    sChucVuDN = "";
                                                    sDLPL = "";MobileDN = ""; EmailDN = "";
                                                }
                                                if("1".equals(isToChuc)){
                                                    sNameCN = "";
                                                    sChucVu = "";
                                                    sCMNDHC = "";
                                                    NgayCap = "";
                                                    NoiCap = "";
                                                    DiaChiCN = "";
                                                    MobileDN = MobileCN;
                                                    EmailDN = EmailCN;
                                                    MobileCN = "";
                                                    EmailCN = "";
                                                }
                                                if("1".equals(isCaNhanThuocToChuc)){
                                                    MobileDN= ""; EmailDN= "";
                                                }
                                                String isHSM = "0";
                                                String isToken = "0";
                                                if(CommonFunction.checkHardTokenEnabled(sFormFactorName) == true) {
                                                    isToken = "1";
                                                }
                                                if(sFormFactorName.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_SOFT_TOKEN) || sFormFactorName.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_ESIGNCLOUD)) {
                                                    isHSM = "1";
                                                }
                                                if(intATTR_TYPE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_REGISTRATION) {
                                                    CertSN = "";
                                                }
                                                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_MATBAO)) {
                                                    if(!"".equals(sCMNDHC) && sCMNDHC.contains(":")){
                                                        sCMNDHC = sCMNDHC.split(":")[1];
                                                    }
                                                }
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                    if(pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_PERSONAL
                                                        || pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_STAFF) {
                                                        MobileCN = sSDTNew; EmailCN = sEmailNew;
                                                        MobileDN = "";
                                                        EmailDN = "";
                                                    }
                                                    if(pCERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_CODE_ENTERPRISE) {
                                                        MobileDN = sSDTNew; EmailDN = sEmailNew;
                                                        MobileCN = "";
                                                        EmailCN = "";
                                                    }
                                                }
                                                pathXML = PrintFormFunction.createXMLRegistrationRedirect(sNameCN, sChucVu,
                                                    sCMNDHC, NgayCap, NoiCap, DiaChiCN, MobileCN, EmailCN, sNameDN, MST, DiaChiDN, MobileDN, EmailDN,
                                                    isCaNhan, isToChuc, isCaNhanThuocToChuc, isCapMoi, isGiaHan, isCapLai, isTamDung,
                                                    isKhoiPhuc, isThuHoi, is1Nam, is2Nam, is3Nam, is4Nam, CertSN, ThoiGianDiaDiem, isHSM, isToken,
                                                    sDLPL, sChucVuDN, sCMNDHCDN, NgayCapDN, NoiCapDN);
                                            }
                                            String sResultHTML = PrintFormFunction.createStringHtmlInString(pathXSLT, pathXML, null, false, false, intResult);
                                            if (intResult[0] == 0) {
                                                strView = "0###" + sResultHTML;
                                            } else {
                                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                                            }
                                        } else { strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NO_DATA + "###0"; }
                                    } else {
                                        strView = Definitions.CONFIG_EXCEPTION_WRONG_AGENCY + "###0";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "###0";
                                }
                                //</editor-fold>
                                break;
                            }
                        }
                    }
                } else if (sOutInner == 2) {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_LOGIN + "###0";
                } else {
                    strView = Definitions.CONFIG_EXCEPTION_STRING_ANOTHERLOGIN + "###0";
                }
            } catch (NumberFormatException e) {
                CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###" + e.getMessage();
            } catch (Exception e) {
                CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###" + e.getMessage();
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
            CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
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
            CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
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
