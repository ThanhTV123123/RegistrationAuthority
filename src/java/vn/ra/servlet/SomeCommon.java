/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
//import java.sql.Statement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONObject;
import vn.ra.object.CERTIFICATION_PROPERTIES_JSON;
import vn.ra.object.DNS_NAME_DATA;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.ROLE_DATA;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.ConnectFileToPartner;
import vn.ra.process.DESEncryption;
import vn.ra.process.SessionDNSName;
import vn.ra.process.SessionRoleFunctions;
import vn.ra.uat.TestPDFConvert;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.NEAC_LOG;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDbPhaseTwo;
import vn.ra.process.SynchNEACFunction;

/**
 *
 * @author THANH-PC
 */
public class SomeCommon extends HttpServlet {

    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SomeCommon.class.getName());

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
            HttpSession sessionsa = request.getSession(false);
//            ConnectDatabase db = new ConnectDatabase();
            String strView = "";
            String idParam = request.getParameter("idParam");
            try {
                if (null != idParam) {
                    switch (idParam) {
                        case "languagepage": {
                            // <editor-fold defaultstate="collapsed" desc="languagepage">
                            if (sessionsa.getAttribute("sUserID") != null) {
                                String svn = request.getParameter("svn");
                                sessionsa.setAttribute("sessVN", svn.trim());
                                strView = "0";
                            } else {
                                response.sendRedirect("Login.jsp");
                            }
                            break;
                            //</editor-fold>
                        }
                        case "backformpage": {
                            // <editor-fold defaultstate="collapsed" desc="backformpage">
                            String idSession = request.getParameter("idSession");
                            sessionsa.setAttribute(idSession, "1");
                            strView = "0";
                            break;
                            //</editor-fold>
                        }
                        case "checkcsrf": {
                            // <editor-fold defaultstate="collapsed" desc="checkcsrf">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                strView = "0";
                            } else {
                                strView = "1";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "setactiverolefunction": {
                            // <editor-fold defaultstate="collapsed" desc="setactiverolefunction">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                String name = EscapeUtils.CheckTextNull(request.getParameter("name"));
                                String attributeType = EscapeUtils.CheckTextNull(request.getParameter("attributeType"));
                                String enabled = EscapeUtils.CheckTextNull(request.getParameter("enabled"));
                                String type = EscapeUtils.CheckTextNull(request.getParameter("type"));
                                switch (type) {
                                    case Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN:
                                        SessionRoleFunctions cartToken = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsToken");
                                        if (cartToken != null) {
                                            ROLE_DATA mhFunc = new ROLE_DATA();
                                            mhFunc.name = name;
                                            mhFunc.attributeType = attributeType;
                                            mhFunc.enabled = "1".equals(enabled);
                                            String param1 = cartToken.UpdateRoleFunctionsList(mhFunc);
                                            if ("0".equals(param1)) {
                                                strView = "0#0";
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                        }
                                        break;
                                    case Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT:
                                        SessionRoleFunctions cartCert = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsCert");
                                        if (cartCert != null) {
                                            ROLE_DATA mhFunc = new ROLE_DATA();
                                            mhFunc.name = name;
                                            mhFunc.attributeType = attributeType;
                                            mhFunc.enabled = "1".equals(enabled);
                                            String param1 = cartCert.UpdateRoleFunctionsList(mhFunc);
                                            if ("0".equals(param1)) {
                                                strView = "0#0";
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                        }
                                        break;
                                    case Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_ANOTHER:
                                        SessionRoleFunctions cartAnother = (SessionRoleFunctions) request.getSession(false).getAttribute("sessRoleFunctionsAnother");
                                        if (cartAnother != null) {
                                            ROLE_DATA mhFunc = new ROLE_DATA();
                                            mhFunc.name = name;
                                            mhFunc.attributeType = attributeType;
                                            mhFunc.enabled = "1".equals(enabled);
                                            String param1 = cartAnother.UpdateRoleFunctionsList(mhFunc);
                                            if ("0".equals(param1)) {
                                                strView = "0#0";
                                            } else {
                                                strView = param1 + "#" + param1;
                                            }
                                        } else {
                                            strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                        }
                                        break;
                                    default:
                                        strView = "0#0";
                                        break;
                                }
                            } else {
                                strView = "1#1";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "deletepushnotirequestapprove": {
                            // <editor-fold defaultstate="collapsed" desc="deletepushnotirequestapprove">
                            ServletContext sc = request.getSession().getServletContext();
                            if (sc.getAttribute("sessPushNotiRequestApprove") != null) {
                                sc.setAttribute("sessPushNotiRequestApprove", null);
                            }
                            strView = "0#0";
                            break;
                            //</editor-fold>
                        }
                        case "deletepushnotirequestdecline": {
                            // <editor-fold defaultstate="collapsed" desc="deletepushnotirequestdecline">
                            String idBRANCH = EscapeUtils.CheckTextNull(request.getParameter("idBRANCH"));
                            String idUser = EscapeUtils.CheckTextNull(request.getParameter("idUser"));
                            ServletContext sc = request.getSession().getServletContext();
                            if (sc.getAttribute("sessPushNotiRequestDecline-" + idBRANCH + "-" + idUser) != null) {
                                sc.setAttribute("sessPushNotiRequestDecline-" + idBRANCH + "-" + idUser, null);
                            }
                            strView = "0#0";
                            break;
                            //</editor-fold>
                        }
                        case "decodeidurl": {
                            // <editor-fold defaultstate="collapsed" desc="decodeidurl">
                            String idValue = EscapeUtils.CheckTextNull(request.getParameter("idValue"));
                            String idValue_Parse = URLDecoder.decode(idValue);
                            DESEncryption seEncript = new DESEncryption();
                            String sResult = seEncript.decrypt(idValue_Parse);
                            strView = "0#" + sResult;
                            break;
                            //</editor-fold>
                        }
                        case "demodownfilefrompartner": {
                            // <editor-fold defaultstate="collapsed" desc="demodownfilefrompartner">
                            Config conf = new Config();
                            String sUUID = EscapeUtils.CheckTextNull(request.getParameter("idValue"));
                            String sIP_CONNECT = "192.168.1.3";
                            String sHTTP_CONNECT = "http";
                            String sCONTEXT_CONNECT = "/fileserver/";
                            String sPORT_CONNECT = "80";
                            String sDEFAULT_USER = "tms";
                            String sDEFAULT_PASS = "tms@123";
                            String sOWNERCODE_CONNECT = "01";
                            String sAPPCODE_CONNECT = "TMS";
                            String sFUNCTION_CONNECT_DOWN = "1";
                            CloseableHttpResponse pHttpRes = ConnectFileToPartner.loadFileParner(sIP_CONNECT, sHTTP_CONNECT,
                                    sCONTEXT_CONNECT, Integer.parseInt(sPORT_CONNECT), sDEFAULT_USER,
                                    sDEFAULT_PASS, sOWNERCODE_CONNECT, sAPPCODE_CONNECT, sFUNCTION_CONNECT_DOWN, sUUID);
                            Header sFilename = pHttpRes.getFirstHeader("Location");
                            String sNameFile = sFilename.getValue();
                            InputStream data = pHttpRes.getEntity().getContent();
                            try {
                                String sFileLocation = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                CommonFunction.LogDebugString(log, "TEST-DOWNLOAD-FILE-PARTNER", sFileLocation);
                                OutputStream output = new FileOutputStream(sFileLocation);
                                try {
                                    ByteStreams.copy(data, output);
                                } finally {
                                }
                            } finally {
                            }
                            strView = "0#" + sNameFile;
                            break;
                            //</editor-fold>
                        }
                        case "demodownfromca_blob": {
                            // <editor-fold defaultstate="collapsed" desc="demodownfromca_blob">
                            Config conf = new Config();
                            String sUUID = EscapeUtils.CheckTextNull(request.getParameter("idValue"));
                            ConnectDatabase db = new ConnectDatabase();
                            Connection conns = db.OpenDatabase();
                            FileOutputStream fileOuputStream = null;
                            try {
                                String selectSQL = "SELECT `BLOB` FROM CERTIFICATION_AUTHORITY_ATTR\n"
                                        + "WHERE CERTIFICATION_AUTHORITY_ATTR_TYPE_ID = (select ID from CERTIFICATION_AUTHORITY_ATTR_TYPE\n"
                                        + "where NAME = '" + sUUID + "')";
                                String sBLOB = "";
                                Statement stmt = conns.createStatement();
                                ResultSet rs;
                                rs = stmt.executeQuery(selectSQL);
                                while (rs.next()) {
                                    sBLOB = rs.getString("BLOB");
                                }
                                if (sBLOB != null) {
                                    String sNameFile = CommonFunction.generateNumberDays() + ".png";
                                    String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER) + sNameFile;
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                    byte[] data = DatatypeConverter.parseBase64Binary(sBLOB);
                                    File file = new File(absoluteDiskPath);
                                    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                                        outputStream.write(data);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
//                                    byte[] bytes = sBLOB.getBytes(1, (int) sBLOB.length());
//                                    if ((new File(absoluteDiskPath)).exists()) {
//                                        (new File(absoluteDiskPath)).delete();
//                                    }
//                                    fileOuputStream = new FileOutputStream(absoluteDiskPath);
//                                    fileOuputStream.write(bytes);
                                    strView = "0#" + sNameFile.trim();
                                } else {
                                    strView = "1#NULL DATA";
                                }
                            } finally {
                                Connection[] temp_connection = new Connection[]{conns};
                                db.CloseDatabase(temp_connection);
                                if (fileOuputStream != null) {
                                    fileOuputStream.close();
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "demoupfromca_blob": {
                            // <editor-fold defaultstate="collapsed" desc="demoupfromca_blob">
                            String ids = EscapeUtils.CheckTextNull(request.getParameter("idValue"));
                            String Logo = EscapeUtils.CheckTextNull(request.getParameter("Logo"));
                            byte[] bytesLogo = null;
                            if (!"".equals(Logo)) {
                                bytesLogo = Logo.getBytes();
                            }
                            ConnectDatabase db = new ConnectDatabase();
                            Connection conns = db.OpenDatabase();
                            try {
                                String selectSQL = "update CERTIFICATION_AUTHORITY_ATTR set `BLOB` = ?\n" +
                                    "where CERTIFICATION_AUTHORITY_ATTR_TYPE_ID = (SELECT ID FROM CERTIFICATION_AUTHORITY_ATTR_TYPE\n" +
                                    "WHERE `NAME`= ? LIMIT 1);";
                                PreparedStatement statement = conns.prepareStatement(selectSQL);
                                statement.setBytes(1, bytesLogo);
                                statement.setString(2, ids);
                                int rowsInserted = statement.executeUpdate();
                                if (rowsInserted > 0) {
                                    strView = "0#0";
                                } else {
                                    strView = "1#ERROR";
                                }
                            } finally {
                                Connection[] temp_connection = new Connection[]{conns};
                                db.CloseDatabase(temp_connection);
                            }
                            break;
                            //</editor-fold>
                        }
                        case "checkphonevalid": {
                            // <editor-fold defaultstate="collapsed" desc="checkphonevalid">
                            String ids = EscapeUtils.CheckTextNull(request.getParameter("phoneValue"));
                            if(!"".equals(ids)) {
                                String sRegexPolicy = "";
                                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                if (sessGeneralPolicy[0].length > 0) {
                                    for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL))
                                        {
                                            sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                            break;
                                        }
                                    }
                                }
                                if(!"".equals(sRegexPolicy))
                                {
                                    String sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_PHONE);
                                    if("".equals(sREGEX_PHONE))
                                    {
                                        sREGEX_PHONE = Definitions.CONFIG_DEFAULT_VALUE_REGEX_PHONE;
                                    }
                                    CommonFunction.LogDebugString(log, "sREGEX_PHONE", sREGEX_PHONE);
                                    boolean isValid = CommonFunction.regexPhoneValid(ids, sREGEX_PHONE);
                                    if(isValid == true)
                                    {
                                        strView = "0#0";
                                    } else {
                                        strView = "1#0";
                                    }
                                } else {
                                    strView = "0#0";
                                }
                            } else {
                                strView = "0#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "checkemailvalid": {
                            // <editor-fold defaultstate="collapsed" desc="checkphonevalid">
                            String ids = EscapeUtils.CheckTextNull(request.getParameter("emailValue"));
                            if(!"".equals(ids)) {
                                String sRegexPolicy = "";
                                GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                if (sessGeneralPolicy[0].length > 0) {
                                    for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL))
                                        {
                                            sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                            break;
                                        }
                                    }
                                }
                                if(!"".equals(sRegexPolicy)) {
                                    String sREGEX_EMAIL = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_EMAIL);
                                    if("".equals(sREGEX_EMAIL))
                                    {
                                        sREGEX_EMAIL = Definitions.CONFIG_DEFAULT_VALUE_REGEX_EMAIL;
                                    }
                                    CommonFunction.LogDebugString(log, "sREGEX_EMAIL", sREGEX_EMAIL);
                                    boolean isValid = CommonFunction.regexEmailValid(ids, sREGEX_EMAIL);
                                    if(isValid == true)
                                    {
                                        strView = "0#0";
                                    } else {
                                        strView = "1#0";
                                    }
                                } else {
                                    strView = "0#0";
                                }
                            } else {
                                strView = "0#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "democreatednsname": {
                            // <editor-fold defaultstate="collapsed" desc="democreatednsname">
                            ObjectMapper objectMapper = new ObjectMapper();
                            String sRegexPolicy = "";
                            SessionDNSName cartToken = (SessionDNSName) request.getSession(false).getAttribute("sessDNSNameForSSL");
                            if (cartToken != null) {
                                request.getSession(false).setAttribute("sessDNSNameForSSL", cartToken);
                                ArrayList<DNS_NAME_DATA> ds = cartToken.getGH();
                                for (DNS_NAME_DATA mhIP : ds) {
                                    sRegexPolicy = sRegexPolicy + EscapeUtils.CheckTextNull(mhIP.DNS_NAME) + ";";
                                }
                                if(!"".equals(sRegexPolicy))
                                {
                                    sRegexPolicy = CommonFunction.subLastCharaterSemicolon(sRegexPolicy);
                                    strView = "0#"+sRegexPolicy;
                                    List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes = new ArrayList<>();
                                    CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                    attribute.setKey(CERTIFICATION_PROPERTIES_JSON.Attribute.SUBJECT_ALT_NAMES);
                                    attribute.setValue(sRegexPolicy);
                                    attributes.add(attribute);
                                    String jsonProperties = "{\"attributes\":" + objectMapper.writeValueAsString(attributes) + "}";
                                    CommonFunction.LogDebugString(log, "JSON", jsonProperties);
                                } else {
                                    strView = "1#1";
                                }
                            } else {
                                strView = "2#1";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "demoprintrar": {
                            // <editor-fold defaultstate="collapsed" desc="demoprintrar">
                            Config conf = new Config();
//                            String sNameFile = conf.GetPropertybyCode(Definitions.CONFIG_NAMEFILE_LOGO);
//                            String queryString = getServletContext().getRealPath("/");
//                            String outputDirectory = queryString;
//                            String pathLogoTemplate = outputDirectory + "/Images/" + sNameFile;
                            String pathLogoTemplate = "D:\\Programer\\Company\\TMS-RA\\TrustCA\\Primary\\RAPortal_EE6\\web\\Images\\Logo_NCCA.jpg";
                            String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                            if (!directory.exists()) {
                                directory.mkdir();
                            }
                            String strUsernameExport = "File";
                            String sFileNameRegis = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REGISTRATION_TEMPLATE + CommonFunction.getDateFormat();
                            String sFileNameConfirm = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_CONFIRMATION_TEMPLATE + CommonFunction.getDateFormat();
                            String sPathRegisPDF_temp = pPathURL + sFileNameRegis + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                            String sPathRegisPDF = pPathURL + sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                            String sPathConfirmPDF_temp = pPathURL + sFileNameConfirm + "_temp" + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                            String sPathConfirmPDF = pPathURL + sFileNameConfirm + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_PDF;
                            String sFileNameZip = pPathURL + sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_RAR;
                            int[] sReturn = new int[2];
                            TestPDFConvert.writeFileRarPersonal(4, pathLogoTemplate, sPathConfirmPDF_temp, sPathConfirmPDF,
                                sPathRegisPDF_temp, sPathRegisPDF, sFileNameZip, "thanh", "123123333", "ABC", "VH",
                                "12...212", "so 12 nguyen anh", "0123122221", "thanhtv@tomicalab.com", "1", "0", "0", "0",
                                "", "thanh 12", "anb", "092210102", "thanhtv1@tomicalab.com", "2", "0", "0", sReturn);
                            if(sReturn[0] == 0)
                            {
                                strView = "0###" + sFileNameZip + "###" + sFileNameRegis + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_RAR;
                            } else {
                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "###0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "generatecaptcha": {
                            // <editor-fold defaultstate="collapsed" desc="generatecaptcha">
                            int iTotalChars = CommonFunction.randInt(3, 6);
                            int iHeight = 35;
                            int iWidth = 155;
                            try {
                                Font fntStyle1 = new Font("Arial", Font.BOLD, 25) {
                                };
                                Font fntStyle2 = new Font("Verdana", Font.BOLD, 20);
                                Random randChars = new Random();
                                String sImageCode = (Long.toString(Math.abs(randChars.nextLong()), 36)).substring(0, iTotalChars);
                                sessionsa.setAttribute("sessCaptchaCode", sImageCode);
                                BufferedImage biImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
                                Graphics2D g2dImage = (Graphics2D) biImage.getGraphics();
                                int iCircle = 15;
                                for (int i = 0; i < iCircle; i++) {
                                    //g2dImage.setColor(new Color(randChars.nextInt(255), randChars.nextInt(255), randChars.nextInt(255)));
                                    int iRadius = (int) (Math.random() * iHeight / 2.0);
                                    int iX = (int) (Math.random() * iWidth - iRadius);
                                    int iY = (int) (Math.random() * iHeight - iRadius);
                                    //g2dImage.fillRoundRect(iX, iY, iRadius * 2, iRadius * 2,100,100);
                                }
                                g2dImage.setFont(fntStyle1);
                                for (int i = 0; i < iTotalChars; i++) {
                                    //g2dImage.setColor(new Color(randChars.nextInt(255), randChars.nextInt(255), randChars.nextInt(255)));
                                    if (i % 2 == 0) {
                                        g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 29);
                                    } else {
                                        g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 29);
                                    }
                                }
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ImageIO.write(biImage, "jpg", baos);
                                baos.flush();
                                byte[] imageInByteArray = baos.toByteArray();
                                baos.close();
                                String b64 = javax.xml.bind.DatatypeConverter.printBase64Binary(imageInByteArray);
                                strView = sImageCode + "#" + b64;
                                g2dImage.dispose();
                            } catch (IOException e) {
                                CommonFunction.LogExceptionServlet(null,"Generate Captcha: "+ e.getMessage(), e);
                                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "synchneacsimple": {
                            // <editor-fold defaultstate="collapsed" desc="synchneacsimple">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                ConnectDatabase com = new ConnectDatabase();
                                String sessVN = sessionsa.getAttribute("sessVN").toString().trim();
                                String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                                String neacLogID = EscapeUtils.CheckTextNull(request.getParameter("neacLogID"));
                                NEAC_LOG[][] rsLog = new NEAC_LOG[1][];
                                com.S_BO_NEAC_LOG_DETAIL(neacLogID, sessVN, rsLog);
                                if(rsLog[0].length > 0) {
                                    int intRemainingSystem = 0;
                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy[0].length > 0) {
                                        for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                            if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_MAX_RETRY_NEAC_SYNCHRONIZATION)) {
                                                intRemainingSystem = Integer.parseInt(rsPolicy1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                    int[] intRes = new int[1]; String[] strRes = new String[1];
                                    if(rsLog[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING_MANUALLY
                                        || rsLog[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING
                                        || rsLog[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_SPECIAL_CASE
                                        || rsLog[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_ASYNCHRONOUS) {
                                        SynchNEACFunction.synchNEACCertificate(loginUID, rsLog[0][0].ID, rsLog[0][0].CERTIFICATION_ID,
                                            rsLog[0][0].CERTIFICATION_ATTR_TYPE_ID, rsLog[0][0].REMAINING_COUNTER, intRemainingSystem, intRes, strRes);
                                        if(intRes[0] == 0) {
                                            request.getSession(false).setAttribute("SessRefreshNEACLog", "1");
                                            strView = "0#0";
                                        } else {
                                            strView = "1#" + strRes[0];
                                        }
                                    }
                                }
                            } else {
                                strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "approveneacsimple": {
                            // <editor-fold defaultstate="collapsed" desc="approveneacsimple">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                ConnectDatabase com = new ConnectDatabase();
                                String sessVN = sessionsa.getAttribute("sessVN").toString().trim();
                                String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                                String neacLogID = EscapeUtils.CheckTextNull(request.getParameter("neacLogID"));
                                NEAC_LOG[][] rsLog = new NEAC_LOG[1][];
                                com.S_BO_NEAC_LOG_DETAIL(neacLogID, sessVN, rsLog);
                                if(rsLog[0].length > 0) {
                                    if(rsLog[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_INITIALIZE) {
//                                        System.out.println("neacLogID: " + neacLogID);
                                        com.S_BO_NEAC_LOG_UPDATE(Integer.parseInt(neacLogID), Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING,
                                            "","", -1, loginUID);
                                        request.getSession(false).setAttribute("SessRefreshNEACLog", "1");
                                        strView = "0#0";
                                    } else {
                                        CommonFunction.LogDebugString(log, "Decline Request NEAC", "Status is invalid");
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                    }
                                } else {
                                    CommonFunction.LogDebugString(log, "Decline Request NEAC", "NeacLogID does not exist");
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                }
                            } else {
                                strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "approveneacbundles": {
                            // <editor-fold defaultstate="collapsed" desc="approveneacbundles">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                ConnectDatabase db = new ConnectDatabase();
                                String AGENT_ID_LOG = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SessAgentID").toString().trim());
                                String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                                String sessVN = sessionsa.getAttribute("sessVN").toString().trim();
                                if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    String isCheckAll = request.getParameter("isCheckAll");
                                    if (null != isCheckAll) {
                                        switch (isCheckAll) {
                                            case "0":
                                                String idValue = EscapeUtils.CheckTextNull(request.getParameter("idValue"));
                                                idValue = idValue.substring(0, idValue.lastIndexOf(",")).trim();
//                                                idValue = idValue.replace("\n", "").trim();
                                                System.out.println(idValue);
                                                String[] sPlitValue = idValue.replace(Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX, "").split(",");
                                                if (sPlitValue.length > 0) {
                                                    for (String sPlitValue1 : sPlitValue) {
                                                        if(!"".equals(sPlitValue1) && !"EMPTY".equals(sPlitValue1)) {
                                                            NEAC_LOG[][] rsNEAC = new NEAC_LOG[1][];
                                                            db.S_BO_NEAC_LOG_DETAIL(sPlitValue1, sessVN, rsNEAC);
                                                            if(rsNEAC[0].length > 0) {
                                                                if(rsNEAC[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_INITIALIZE) {
    //                                                                System.out.println("sPlitValue1: " + sPlitValue1);
                                                                    db.S_BO_NEAC_LOG_UPDATE(Integer.parseInt(sPlitValue1), Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING,
                                                                        "","", -1, loginUID);
                                                                }
                                                            }
                                                        }
                                                    }
                                                    sessionsa.setAttribute("SessRefreshNEACLog", "1");
                                                    strView = "0#0";
                                                } else {
                                                    strView = "1#0";
                                                }
                                                break;
                                            case "1":
                                                NEAC_LOG[][] rsPgin = new NEAC_LOG[1][];
                                                String ToCreateDate = (String) sessionsa.getAttribute("sessToCreateDateNEACLog");
                                                String FromCreateDate = (String) sessionsa.getAttribute("sessFromCreateDateNEACLog");
                                                String CERT_SN = (String) sessionsa.getAttribute("sessCERT_SNNEACLog");
                                                String NEAC_SYNC_STATE = (String) sessionsa.getAttribute("NEAC_SYNC_STATE");
                                                String CERTIFICATION_ATTR_TYPE = (String) sessionsa.getAttribute("sessCERTIFICATION_ATTR_TYPENEACLog");
                                                String purpose = (String) sessionsa.getAttribute("sessCERTIFICATION_PURPOSENEACLog");
                                                String profile = (String) sessionsa.getAttribute("sessCERTIFICATION_DURATIONNEACLog");
                                                String strAlertAllTimes = (String) sessionsa.getAttribute("AlertAllTimeSNEACLogList");
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(NEAC_SYNC_STATE)) {
                                                    NEAC_SYNC_STATE = "";
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_ATTR_TYPE)) {
                                                    CERTIFICATION_ATTR_TYPE = "";
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(purpose)) {
                                                    purpose = "";
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(profile)) {
                                                    profile = "";
                                                }
                                                if ("1".equals(strAlertAllTimes)) {
                                                    FromCreateDate = "";
                                                    ToCreateDate = "";
                                                }
                                                int sCount = db.S_BO_NEAC_LOG_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(NEAC_SYNC_STATE),
                                                    EscapeUtils.escapeHtmlSearch(CERT_SN), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE),
                                                    sessTreeArrayBranchID,profile, purpose);
                                                if (sCount > 0) {
                                                    db.S_BO_NEAC_LOG_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                        EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                        EscapeUtils.escapeHtmlSearch(NEAC_SYNC_STATE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                                        EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), sessVN, sessTreeArrayBranchID,
                                                        rsPgin, Definitions.CONFIG_GRID_INT_PAGNO, sCount,profile, purpose);
                                                }
                                                if (rsPgin[0].length > 0) {
                                                    for (NEAC_LOG rsPgin1 : rsPgin[0]) {
                                                        if(rsPgin1.NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_INITIALIZE) {
//                                                            System.out.println("rsPgin1.ID: " + rsPgin1.ID);
                                                            db.S_BO_NEAC_LOG_UPDATE(rsPgin1.ID, Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING,
                                                                "","", -1, loginUID);
                                                        }
                                                    }
                                                    sessionsa.setAttribute("SessRefreshNEACLog", "1");
                                                    strView = "0#0";
                                                } else {
                                                    strView = "1#0";
                                                }
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "synchneacbundles": {
                            // <editor-fold defaultstate="collapsed" desc="synchneacbundles">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                ConnectDatabase db = new ConnectDatabase();
                                String AGENT_ID_LOG = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SessAgentID").toString().trim());
                                String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                                String sessVN = sessionsa.getAttribute("sessVN").toString().trim();
                                if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    int intRemainingSystem = 0;
                                    GENERAL_POLICY[][] sessGeneralPolicy = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                    if (sessGeneralPolicy[0].length > 0) {
                                        for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicy[0]) {
                                            if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_MAX_RETRY_NEAC_SYNCHRONIZATION)) {
                                                intRemainingSystem = Integer.parseInt(rsPolicy1.VALUE);
                                                break;
                                            }
                                        }
                                    }
                                    String isCheckAll = request.getParameter("isCheckAll");
                                    if (null != isCheckAll) {
                                        switch (isCheckAll) {
                                            case "0":
                                                String idValue = EscapeUtils.CheckTextNull(request.getParameter("idValue"));
                                                idValue = idValue.substring(0, idValue.lastIndexOf(","));
                                                idValue = idValue.replace("\n", "").trim();
                                                System.out.println(idValue);
                                                String[] sPlitValue = idValue.replace(Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX, "").split(",");
                                                if (sPlitValue.length > 0) {
                                                    for (String sPlitValue1 : sPlitValue) {
                                                        if(!"".equals(sPlitValue1)) {
                                                            NEAC_LOG[][] rsNEAC = new NEAC_LOG[1][];
                                                            db.S_BO_NEAC_LOG_DETAIL(sPlitValue1, sessVN, rsNEAC);
                                                            if(rsNEAC[0].length > 0) {
                                                                int[] intRes = new int[1]; String[] strRes = new String[1];
                                                                if(rsNEAC[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING
                                                                    || rsNEAC[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING_MANUALLY
                                                                    || rsNEAC[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_SPECIAL_CASE
                                                                    || rsNEAC[0][0].NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_ASYNCHRONOUS) {
    //                                                                System.out.println("rsNEAC.CERTIFICATION_SN: " + rsNEAC[0][0].CERTIFICATION_SN);
                                                                    SynchNEACFunction.synchNEACCertificate(loginUID, rsNEAC[0][0].ID, rsNEAC[0][0].CERTIFICATION_ID,
                                                                        rsNEAC[0][0].CERTIFICATION_ATTR_TYPE_ID, rsNEAC[0][0].REMAINING_COUNTER, intRemainingSystem, intRes, strRes);
                                                                    CommonFunction.LogDebugString(log, "SN: " + rsNEAC[0][0].CERTIFICATION_SN, "RES_CODE: " + intRes[0] + " - " + strRes[0]);
                                                                }
                                                            }
                                                        }
                                                    }
                                                    sessionsa.setAttribute("SessRefreshNEACLog", "1");
                                                    strView = "0#0";
                                                } else {
                                                    strView = "1#0";
                                                }
                                                break;
                                            case "1":
                                                NEAC_LOG[][] rsPgin = new NEAC_LOG[1][];
                                                String ToCreateDate = (String) sessionsa.getAttribute("sessToCreateDateNEACLog");
                                                String FromCreateDate = (String) sessionsa.getAttribute("sessFromCreateDateNEACLog");
                                                String CERT_SN = (String) sessionsa.getAttribute("sessCERT_SNNEACLog");
                                                String NEAC_SYNC_STATE = (String) sessionsa.getAttribute("NEAC_SYNC_STATE");
                                                String CERTIFICATION_ATTR_TYPE = (String) sessionsa.getAttribute("sessCERTIFICATION_ATTR_TYPENEACLog");
                                                String purpose = (String) sessionsa.getAttribute("sessCERTIFICATION_PURPOSENEACLog");
                                                String profile = (String) sessionsa.getAttribute("sessCERTIFICATION_DURATIONNEACLog");

                                                String strAlertAllTimes = (String) sessionsa.getAttribute("AlertAllTimeSNEACLogList");
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(NEAC_SYNC_STATE)) {
                                                    NEAC_SYNC_STATE = "";
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_ATTR_TYPE)) {
                                                    CERTIFICATION_ATTR_TYPE = "";
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(purpose)) {
                                                    purpose = "";
                                                }
                                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(profile)) {
                                                    profile = "";
                                                }
                                                if ("1".equals(strAlertAllTimes)) {
                                                    FromCreateDate = "";
                                                    ToCreateDate = "";
                                                }
                                                int sCount = db.S_BO_NEAC_LOG_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                    EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(NEAC_SYNC_STATE),
                                                    EscapeUtils.escapeHtmlSearch(CERT_SN), EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE),
                                                    sessTreeArrayBranchID,profile, purpose);
                                                if (sCount > 0) {
                                                    db.S_BO_NEAC_LOG_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                                        EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                        EscapeUtils.escapeHtmlSearch(NEAC_SYNC_STATE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                                        EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), sessVN, sessTreeArrayBranchID,
                                                        rsPgin, Definitions.CONFIG_GRID_INT_PAGNO, sCount,profile, purpose);
                                                }
                                                if (rsPgin[0].length > 0) {
                                                    for (NEAC_LOG rsPgin1 : rsPgin[0]) {
                                                        int[] intRes = new int[1]; String[] strRes = new String[1];
                                                        if(rsPgin1.NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING
                                                            || rsPgin1.NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_PROCESSING_MANUALLY
                                                            || rsPgin1.NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_SPECIAL_CASE
                                                            || rsPgin1.NEAC_SYNC_STATE_ID == Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_ASYNCHRONOUS)
                                                        {
                                                            SynchNEACFunction.synchNEACCertificate(loginUID, rsPgin1.ID, rsPgin1.CERTIFICATION_ID, 
                                                                rsPgin1.CERTIFICATION_ATTR_TYPE_ID, rsPgin1.REMAINING_COUNTER, intRemainingSystem, intRes, strRes);
//                                                            System.out.println("rsNEAC.IDCERTIFICATION_SN: " + rsPgin1.CERTIFICATION_SN);
                                                            CommonFunction.LogDebugString(log, "SN: " + rsPgin1.CERTIFICATION_SN, "RES_CODE: " + intRes[0] + " - " + strRes[0]);
                                                        }
                                                    }
                                                    sessionsa.setAttribute("SessRefreshNEACLog", "1");
                                                    strView = "0#0";
                                                } else {
                                                    strView = "1#0";
                                                }
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                            //</editor-fold>
                        }
                        case "declineneacsimple": {
                            // <editor-fold defaultstate="collapsed" desc="declineneacsimple">
                            String anticsrf = request.getParameter("CsrfToken");
                            if (anticsrf != null && anticsrf.equals(request.getSession().getAttribute("anticsrf"))) {
                                ConnectDatabase com = new ConnectDatabase();
                                String sessVN = sessionsa.getAttribute("sessVN").toString().trim();
                                String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                                String neacLogID = EscapeUtils.CheckTextNull(request.getParameter("neacLogID"));
                                NEAC_LOG[][] rsLog = new NEAC_LOG[1][];
                                com.S_BO_NEAC_LOG_DETAIL(neacLogID, sessVN, rsLog);
                                if(rsLog[0].length > 0) {
                                    if(rsLog[0][0].NEAC_SYNC_STATE_ID != Definitions.CONFIG_SYNCH_NEAC_STATE_CANCEL
                                        && rsLog[0][0].NEAC_SYNC_STATE_ID != Definitions.CONFIG_SYNCH_NEAC_STATE_SUCCESS
                                        && rsLog[0][0].NEAC_SYNC_STATE_ID != Definitions.CONFIG_SYNCH_NEAC_STATE_ERROR_RESYNCHRONIZE)
                                    {
                                        com.S_BO_NEAC_LOG_UPDATE(Integer.parseInt(neacLogID), Definitions.CONFIG_SYNCH_NEAC_STATE_CANCEL, "", "", -1, loginUID);
                                        request.getSession(false).setAttribute("SessRefreshNEACLog", "1");
                                        strView = "0#0";
                                    } else {
                                        CommonFunction.LogDebugString(log, "Decline Request NEAC", "Status is invalid");
                                        strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                    }
                                } else {
                                    CommonFunction.LogDebugString(log, "Decline Request NEAC", "NeacLogID does not exist");
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
                                }
                            } else {
                                strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "refreshCSRF": {
                            // <editor-fold defaultstate="collapsed" desc="refreshCSRF">
                            String anticsrf = "" + Math.random();
                            request.getSession().setAttribute("anticsrf", anticsrf);
                            strView = "0#"+anticsrf;
                            break;
                            //</editor-fold>
                        }
                        case "confirmCustomerHSM": {
                            // <editor-fold defaultstate="collapsed" desc="confirmCustomerHSM">
                            String isType = EscapeUtils.CheckTextNull(request.getParameter("isType"));
                            String vCertID = EscapeUtils.CheckTextNull(request.getParameter("vCertID"));
                            String vKey = EscapeUtils.CheckTextNull(request.getParameter("vKey"));
                            DESEncryption clsEnrypt=new DESEncryption();
                            String sParse = clsEnrypt.decrypt(vKey);
                            if(!"".equals(sParse)) {
                                String sCertificateID = sParse.split("#")[0].trim();
                                String sTime = sParse.split("#")[1];
                                if(sCertificateID.equals(vCertID) && !"".equals(sCertificateID) && !"".equals(vCertID)) {
                                    Date date = new Date(Long.parseLong(sTime));
                                    Format format = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
                                    System.out.println("CustomerConfirm.jsp: " + format.format(date));
                                    boolean checkExpire = CommonFunction.checkDateBiggerCurrent(format.format(date), Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
                                    if(checkExpire == true) {
                                        CERTIFICATION[][] rs = new CERTIFICATION[1][];
                                        ConnectDatabase db = new ConnectDatabase();
                                        ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                        int certAttrID = 0;
                                        db.S_BO_CERTIFICATION_DETAIL(sCertificateID, "1", rs);
                                        if(rs[0].length > 0) {
                                            if(rs[0][0].CERTIFICATION_ATTR_STATE_ID == Definitions.CONFIG_CERTIFICATION_ATTR_STATE_ID_APPROVED) {
                                                certAttrID = rs[0][0].CERTIFICATION_ATTR_ID;
                                                if(null == isType) {
                                                } else switch (isType) {
                                                    case "1":
                                                        System.out.println("confirmCustomerHSM: 1");
                                                        boolean isConfirmEnabled = false;
                                                        String sCONFIRMATION_HSM = rs[0][0].CONFIRMATION_PROPERTIES;
                                                        if(!"".equals(sCONFIRMATION_HSM)) {
                                                            ObjectMapper objectMapper = new ObjectMapper();
                                                            CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(sCONFIRMATION_HSM, CERTIFICATION_PROPERTIES_JSON.class);
                                                            if(itemParsePush.getAttributes().size() > 0) {
                                                                for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
                                                                    if(EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getKey()).equals(Definitions.CONFIG_VALUE_HSM_CONFIRM_DECLINE_ENABLED)){
                                                                        if(EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getValue()).equals("true")){
                                                                            isConfirmEnabled = true;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if(isConfirmEnabled == false) {
                                                            dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(certAttrID, 1);
                                                            int[] intWSRes = new int[1];
                                                            String[] sWSRes = new String[1];
                                                            String sTOKEN_SN = EscapeUtils.CheckTextNull(rs[0][0].TOKEN_SN);
                                                            String strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                                            ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(certAttrID), intWSRes, sWSRes);
                                                            if(intWSRes[0] == 0){
                                                                strView = "0#1";
                                                            } else {
                                                                strView = "ISSUE_ERROR#1";
                                                            }
                                                        } else {
                                                            strView = "DECLINED#0";
                                                        }
                                                        break;
                                                    case "0":
                                                        System.out.println("confirmCustomerHSM: 0");
                                                        String vDeclineReason = EscapeUtils.CheckTextNull(request.getParameter("vDeclineReason"));
                                                        List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributes = new ArrayList<>();
                                                        CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                        attribute.setKey(Definitions.CONFIG_VALUE_HSM_CONFIRM_DECLINE_ENABLED);
                                                        attribute.setValue("true");
                                                        attributes.add(attribute);
                                                        attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                        attribute.setKey(Definitions.CONFIG_VALUE_HSM_CONFIRM_DECLINE_REASON);
                                                        attribute.setValue(vDeclineReason);
                                                        attributes.add(attribute);
                                                        attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                        attribute.setKey(Definitions.CONFIG_VALUE_HSM_CONFIRM_DECLINE_TIME);
                                                        Date dateDecline = new Date();
                                                        attribute.setValue(String.valueOf(dateDecline.getTime()));
                                                        attributes.add(attribute);
                                                        ObjectMapper objectMapper = new ObjectMapper();
                                                        String sProperties = "{\"attributes\":" + objectMapper.writeValueAsString(attributes) + "}";
                                                        dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(certAttrID, 2);
                                                        dbTwo.S_RAC_CERTIFICATION_ATTR_UPDATE_CONFIRMATION_PROPERTIES(certAttrID, "admin", sProperties);
                                                        strView = "0#0";
                                                        break;
                                                    default:
                                                        System.out.println("confirmCustomerHSM:");
                                                        strView = "TYPE_NOT#0";
                                                        break;
                                                }
                                            }
                                        } else {
                                            strView = "DATA_FOUND#0";
                                        }
                                    } else {
                                        strView = "EXPIRE#0";
                                    }
                                } else {
                                    strView = "CERT_FAIL#0";
                                }
                            } else {
                                strView = "CERT_FAIL#0";
                            }
                            break;
                            //</editor-fold>
                        }
                        case "checkredrecthome": {
                            // <editor-fold defaultstate="collapsed" desc="checkredrecthome">
                            ConnectDatabase db = new ConnectDatabase();
                            String strURLCurrent = EscapeUtils.CheckTextNull(request.getParameter("pageCheck"));
                            System.out.println("strURLCurrent: "+ strURLCurrent);
                            if(sessionsa.getAttribute("RoleID") != null && !"".equals(strURLCurrent)) {
                                String strRole = sessionsa.getAttribute("RoleID").toString().trim();
                                int isURL = db.CheckIsURLRelated(strURLCurrent, strRole);
                                System.out.println("isURL: " + isURL);
                                if(isURL == 1){
                                    strView = "1#0";
                                } else {
                                    strView = "0#0";
                                }
                            } else {
                                strView = "0#0";
                            }
                            break;
                            //</editor-fold>
                        }
                    }
                }
            } catch (NumberFormatException | IOException e) {
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
