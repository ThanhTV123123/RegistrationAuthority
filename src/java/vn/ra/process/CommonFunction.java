/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.sql.*;
import java.util.Locale;
import vn.ra.utility.Definitions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import vn.ra.object.RESPONSE_LOG;
import vn.ra.object.TOKEN_CHANGE_LOG;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.ROLE_DATA;
import vn.ra.object.ROLE_ATTRIBUTES;
import vn.ra.utility.EscapeUtils;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import vn.mobileid.jackrabbit.function.JCRConfig;
//import vn.mobileid.jackrabbit.function.JCRException;
//import vn.mobileid.jackrabbit.function.JCRFile;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATE_ATTRIBUTES;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.object.CERTIFICATION_POLICY_ATTRIBUTE;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_PROPERTIES_JSON;
import vn.ra.object.CERTIFICATION_TYPE_COMPONENT;
import vn.ra.object.CertificateComponentInfo;
import vn.ra.object.CertificateInfo;
import vn.ra.object.CertificateReportInfo;
import vn.ra.object.DNS_NAME_DATA;
import vn.ra.object.DisallowanceList;
import vn.ra.object.FILE_PROFILE_JSON;
import vn.ra.object.JSON_USER_BRANCH_DEFAULT;
import vn.ra.object.MonitorLogType;
import vn.ra.object.PROFILE_DISCOUNT_RATE_ATTRIBUTE;
import vn.ra.object.PROFILE_DISCOUNT_RATE_DATA;
import vn.ra.object.USER_BRANCH_DEFAULT_ATTRIBUTES;
import vn.ra.utility.Config;
import vn.ra.utility.PropertiesContent;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ejbca.util.CertTools;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRFile;
import vn.ra.object.CredentialDataAuthen;
import vn.ra.object.FormFactorJsonProperties;
import vn.ra.synch.neac.EFREQUEST_DATA;
import vn.ra.synch.neac.ListPdfFileBase64;
import vn.ra.synch.neac.NEREQUEST_DATA;
import vn.ra.synch.neac.RequestDataNEAC;
import vn.ra.utility.ConfigLanguage;
import vn.ra.utility.DateHelper;

/**
 *
 * @author Thanh
 */
public class CommonFunction {

    //<editor-fold defaultstate="collapsed" desc="1 - void regenerateSession">
    /**
     *
     * @param request
     */
    public static void regenerateSession(HttpServletRequest request) {
        try {
            HttpSession oldSession = request.getSession();
            HttpSession newSession;
            Enumeration attrNames = oldSession.getAttributeNames();
            Properties props = new Properties();
            if (attrNames != null) {
                while (attrNames.hasMoreElements()) {
                    String key = (String) attrNames.nextElement();
                    props.put(key, oldSession.getAttribute(key));
                }
                //Invalidating previous session
                oldSession = request.getSession(false);
                oldSession.invalidate();
                //Generate new session
                newSession = request.getSession(true);
                attrNames = props.keys();
                while (attrNames.hasMoreElements()) {
                    String key = (String) attrNames.nextElement();
                    newSession.setAttribute(key, props.get(key));
                }
            }
        } catch (Exception e) {
            LogExceptionServlet(log, "regenerateSession: " + e.getMessage().trim(), e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="2 - String RandomStringInt">
    /**
     *
     * @return
     */
    public static String RandomStringInt() {
        String result;
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int millis = now.get(Calendar.MILLISECOND);
        result = String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second) + String.valueOf(millis);
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="4 - int Converter">
    /**
     *
     * @param str
     * @return
     */
    public int Converter(String str) {
        int convrtr = 0;
        if (str == null) {
            str = "0";
        } else if ("null".equals(str.trim())) {
            str = "0";
        } else if ("".equals(str)) {
            str = "0";
        }
        try {
            convrtr = Integer.parseInt(str);
        } catch (Exception e) {
            LogExceptionServlet(log, "Converter: " + e.getMessage(), e);
        }
        return convrtr;
    }
    //</editor-fold>

    private static final Logger log = Logger.getLogger(CommonFunction.class);

    /**
     *
     * @param funcName
     * @param request
     * @param e
     */
    public static void LogExceptionJSP(String funcName, String request, Exception e) {
        log.error(funcName + ": " + request + Definitions.CONFIG_LOG_WRITE_DOWNLINE, e);
    }

    /**
     *
     * @param logsrv
     * @param sRequest
     * @param e
     */
    public static void LogExceptionServlet(org.apache.log4j.Logger logsrv, String sRequest, Exception e) {
        if (logsrv == null) {
            log.error(sRequest + Definitions.CONFIG_LOG_WRITE_DOWNLINE, e);
        } else {
            logsrv.error(sRequest + Definitions.CONFIG_LOG_WRITE_DOWNLINE, e);
        }
    }

    /**
     *
     * @param logsrv
     * @param sRequest
     */
    public static void LogErrorServlet(org.apache.log4j.Logger logsrv, String sRequest) {
        if (logsrv == null) {
            log.error(sRequest + Definitions.CONFIG_LOG_WRITE_DOWNLINE);
        } else {
            logsrv.error(sRequest + Definitions.CONFIG_LOG_WRITE_DOWNLINE);
        }
    }

    /**
     *
     * @param logsrv
     * @param sFuncName
     * @param sRequest
     */
    public static void LogDebugString(org.apache.log4j.Logger logsrv, String sFuncName, String sRequest) {
        if (logsrv == null) {
            log.info(sFuncName + ": " + sRequest + Definitions.CONFIG_LOG_WRITE_DOWNLINE);
        } else {
            logsrv.info(sFuncName + ": " + sRequest + Definitions.CONFIG_LOG_WRITE_DOWNLINE);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="String ClassCss">
    public static String ClassCss(int sID) {
        String sResult;
        if (sID == 1) {
            sResult = "fa fa-cog";
        } else if (sID == 2) {
            sResult = "fa fa-user";
        } else if (sID == 4) {
            sResult = "fa fa-desktop";
        } else if (sID == 16) {
            sResult = "fa fa-table";
        } else if (sID == 18) {
            sResult = "fa fa-clone";
        } else if (sID == 23) {
            sResult = "fa fa-history";
        } else if (sID == 24) {
            sResult = "fa fa-bug";
        } else if (sID == 39) {
            sResult = "fa fa-sitemap";
        } else if (sID == 58) {
            sResult = "fa fa-laptop";
        } else if (sID == 48) {
            sResult = "fa fa-cloud";
        } else if (sID == 53) {
            sResult = "fa fa-tachometer";
        } else if (sID == 83) {
            sResult = "fa fa-bar-chart";
        } else if (sID == 84) {
            sResult = "fa fa-gears";
        } else if (sID == 77) {
            sResult = "fa fa-globe";
        } else {
            sResult = "fa fa-align-right";
        }
        return sResult;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="38 - static String getDNFieldOfCA">
    /**
     *
     * @param caName
     * @param fullName
     * @param LicenseNo
     * @param CompanyName
     * @param mst
     * @param district
     * @param cityProvince
     * @param nation
     * @param organization
     * @param department
     * @return
     */
    public static String getDNFieldOfCA(String caName, String fullName, String LicenseNo, String CompanyName,
            String mst, String district, String cityProvince, String nation, String organization, String department) {
        String DN = "";
        String dateCreated = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a").format(Calendar.getInstance().getTime());
        switch (caName) {
            case "FPT Certification Authority":
                DN = "CN=" + CompanyName + ", O=MST:" + mst + ", C=" + nation + ", L=" + dateCreated;
                break;
            case "CA2":
                break;
            case "VNPT Certification Authority":
                break;
            case "Viettel-CA":
                break;
            case "SmartSign":
                break;
            case "SAFE-CA":
                break;
            case "Bkav-CA":
                break;
            default:
                break;
        }
        return DN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="39 - String formatDateTime">
    /**
     *
     * @param sCharSub
     * @param awayRightEnable
     * @param sCharReplace
     * @param dateTime
     * @return
     */
    public String formatDateTime(String dateTime, String sCharSub, String sCharReplace, boolean awayRightEnable) {
        //dd-mm-yyyy
        if (!"".equals(dateTime)) {
            if (awayRightEnable == true) {
                String[] array = dateTime.split(sCharSub);
                String strDay, strMonth, strYear;
                int day = Integer.parseInt(array[2]);
                int month = Integer.parseInt(array[1]);
                int year = Integer.parseInt(array[0]);
                if (day < 10) {
                    strDay = "0" + day;
                } else {
                    strDay = "" + day;
                }
                if (month < 10) {
                    strMonth = "0" + month;
                } else {
                    strMonth = "" + month;
                }
                strYear = "" + year;
                return strDay + sCharReplace + strMonth + sCharReplace + strYear;
            } else {
                String[] array = dateTime.split(sCharSub);
                String strDay, strMonth, strYear;
                int day = Integer.parseInt(array[0]);
                int month = Integer.parseInt(array[1]);
                int year = Integer.parseInt(array[2]);
                if (day < 10) {
                    strDay = "0" + day;
                } else {
                    strDay = "" + day;
                }
                if (month < 10) {
                    strMonth = "0" + month;
                } else {
                    strMonth = "" + month;
                }
                strYear = "" + year;
                return strYear + sCharReplace + strMonth + sCharReplace + strDay;
            }
        } else {
            return "";
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="40 - String formatDateTimeToDateTime">
    /**
     *
     * @param dateTime
     * @return
     */
    public String formatDateTimeToDateTime(Date dateTime) {
        String strCreateDate = "";
        if (dateTime != null) {
            //Date StrDate = rsPgin.getDate("CreateDate");
            DateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");
            //DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
            strCreateDate = dfDate.format(dateTime);// + " " + dfTime.format(dateTime.getTime());
        }
        return strCreateDate;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="41 - String formatDateTimeToDateTime1">
    /**
     *
     * @param dateTime
     * @return
     */
    public String formatDateTimeToDateTime1(String dateTime) {
        String strCreateDate;
        //Date StrDate = rsPgin.getDate("CreateDate");
        DateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");
        //DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        strCreateDate = dfDate.format(dateTime);// + " " + dfTime.format(dateTime.getTime());

        return strCreateDate;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="formatDateTimeToDateHour">
    /**
     *
     * @param dateTime
     * @return
     */
    public static String formatDateTimeToDateHour(Date dateTime) {
        String strCreateDate;
        DateFormat dfDate = new SimpleDateFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
        strCreateDate = dfDate.format(dateTime);
        return strCreateDate;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="formatDateTimeToDateHour">
    /**
     *
     * @param dateTime
     * @param sSimpleFormat
     * @return
     */
    public static Date convertStringToDate(String dateTime, String sSimpleFormat) {
        try {
            if (!"".equals(dateTime)) {
                DateFormat dfDate = new SimpleDateFormat(sSimpleFormat);
                return dfDate.parse(dateTime);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="42 - String formatDateTimeToDateTimeLast">
    /**
     *
     * @param dateTime
     * @param sTime
     * @return
     */
    public String formatDateTimeToDateTimeLast(Date dateTime, Time sTime) {
        String strCreateDate = "";
        if (dateTime != null) {
            DateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat dfTime = new SimpleDateFormat("HH:mm");
            strCreateDate = dfDate.format(dateTime) + " " + dfTime.format(sTime);
        }
        return strCreateDate;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String[] getCertificateComponents">
    /**
     *
     * @param certstr
     * @return
     */
    public static String[] getCertificateComponents(String certstr) {
        String[] tmp = new String[5];
        try {
            DateFormat formatter = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
            CertificateFactory certFactory1 = CertificateFactory.getInstance("X.509");
            InputStream in = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certstr));
            X509Certificate cert = (X509Certificate) certFactory1.generateCertificate(in);
            tmp[0] = cert.getSerialNumber().toString(16);
            tmp[1] = cert.getSubjectDN().toString();
            tmp[1] = tmp[1].replace("\\", "");
            tmp[2] = cert.getIssuerDN().toString();
            tmp[3] = formatter.format(cert.getNotBefore());
            tmp[4] = formatter.format(cert.getNotAfter());

        } catch (Exception e) {
            LogExceptionServlet(log, "getCertificateComponents: " + e.getMessage().trim(), e);
            tmp = null;
        }
        return tmp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="List VoidCertificateComponents">
    public void VoidCertificateComponents(String certstr, Object[] sss, String[] tmp, int[] intRes) {
        try {
            if (certstr.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
                certstr = certstr.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN, "");
            }
            if (certstr.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
                certstr = certstr.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END, "");
            }
            DateFormat formatter = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
            CertificateFactory certFactory1 = CertificateFactory.getInstance("X.509");
            InputStream in = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certstr));
            X509Certificate cert = (X509Certificate) certFactory1.generateCertificate(in);
            tmp[0] = cert.getSerialNumber().toString(16);
            sss[0] = cert.getSubjectDN();
            sss[0] = sss[0].toString().replace("\\", "");
            sss[1] = cert.getIssuerDN();
            tmp[1] = formatter.format(cert.getNotBefore());
            tmp[2] = formatter.format(cert.getNotAfter());
            intRes[0] = 0;
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, "VoidCertificateComponents: " + e.getMessage(), e);
            intRes[0] = 1;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String[] getCertificateComponentsDatabase">
    public void getCertificateComponentsDatabase(String certstr, String[] sParse,
            Date[] dateValid, Date[] dateEx) {
        try {
            if (certstr.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
                certstr = certstr.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN, "");
            }
            if (certstr.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
                certstr = certstr.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END, "");
            }
            //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            CertificateFactory certFactory1 = CertificateFactory.getInstance("X.509");
            InputStream in = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certstr));
            X509Certificate cert = (X509Certificate) certFactory1.generateCertificate(in);
            String sDN = cert.getSubjectDN().toString();
            sParse[0] = cert.getSerialNumber().toString(16);
            sParse[1] = cert.getSubjectDN().toString();
            sParse[1] = sParse[1].replace("\\", "");
            sParse[2] = cert.getIssuerDN().toString();
            dateValid[0] = cert.getNotBefore();
            dateEx[0] = cert.getNotAfter();
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, "getCertificateComponentsDatabase: " + e.getMessage(), e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ArrayList<String> listCRLCA">
    /**
     *
     * @param crlPath
     * @param path
     * @return
     */
    public static ArrayList<String> listCRLCA(String crlPath, String path) {
        ArrayList<String> result = new ArrayList<>();
        int index = crlPath.lastIndexOf("/");
        String crlFileName = crlPath.substring(++index);
        //String path = "/opt/CAG360/file/crl/";
        try {
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();
            //Arrays.sort(listOfFiles);
            Arrays.sort(listOfFiles, Collections.reverseOrder());
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    String crlFile = listOfFile.getName();
                    if (crlFile.contains(crlFileName)) {
                        result.add(listOfFile.getName());
                    }
                }
            }
        } catch (Exception e) {
            LogExceptionServlet(log, "listCRLCA: " + e.getMessage(), e);
        }
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String deleteCRLCA">
    /**
     *
     * @param fileName
     * @param path
     * @return
     */
    public static String deleteCRLCA(String fileName, String path) {
        String sResult;
        try {
            File f = new File(path + fileName);
            if (f.exists()) {
                f.delete();
            }
            sResult = "0";
        } catch (Exception e) {
            sResult = e.getMessage();
        }
        return sResult;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ArrayList<String> listLogFile">
    /**
     *
     * @param name
     * @param sPath
     * @return
     */
    public static ArrayList<String> listLogFile(String name, String sPath) {
        String fd;
        if (name.compareTo("Client Transaction") == 0) {
            fd = "clientws";
        } else if (name.compareTo("Email") == 0) {
            fd = "email";
        } else if (name.compareTo("Sms") == 0) {
            fd = "sms";
        } else {
            fd = "certvalidation";
        }
        ArrayList<String> result = new ArrayList<>();
        try {
            File folder = new File(sPath + fd);
            File[] listOfFiles = folder.listFiles();
            Arrays.sort(listOfFiles, Collections.reverseOrder());
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    result.add(listOfFile.getName());
                }
            }
        } catch (Exception e) {
            LogExceptionServlet(log, "listLogFile: " + e.getMessage(), e);
        }
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String deleteLogFile">
    /**
     *
     * @param name
     * @param fileName
     * @param sPath
     */
    public static void deleteLogFile(String name, String fileName, String sPath) {
        String fd;
        if (name.compareTo("Client Transaction") == 0) {
            fd = "clientws";
        } else if (name.compareTo("Email") == 0) {
            fd = "email";
        } else if (name.compareTo("Sms") == 0) {
            fd = "sms";
        } else {
            fd = "certvalidation";
        }
        try {
            File f = new File(sPath + fd + "/" + fileName);
            if (f.exists()) {
                f.delete();
            }
        } catch (Exception e) {
            LogExceptionServlet(log, "deleteLogFile: " + e.getMessage(), e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String deleteAllLogFile">
    /**
     *
     * @param name
     * @param sPath
     */
    public static void deleteAllLogFile(String name, String sPath) {
        String fd;
        if (name.compareTo("Client Transaction") == 0) {
            fd = "clientws";
        } else if (name.compareTo("Email") == 0) {
            fd = "email";
        } else if (name.compareTo("Sms") == 0) {
            fd = "sms";
        } else {
            fd = "certvalidation";
        }
        try {
            File folder = new File(sPath + fd);
            File[] listOfFiles = folder.listFiles();
            Arrays.sort(listOfFiles);
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    File f = new File(listOfFile.getAbsolutePath());
                    if (f.exists()) {
                        f.delete();
                    }
                }
            }
        } catch (Exception e) {
            LogExceptionServlet(log, "deleteAllLogFile: " + e.getMessage(), e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String getAttributeCertificate">
    /**
     *
     * @param dn
     * @param key
     * @return
     */
    public static String getAttributeCertificate(String dn, String key) {
        String[] parts = dn.split(",");
        for (String part1 : parts) {
            String[] part = part1.split("=");
            part[0] = part[0].trim();
            if (part[0].compareTo(key) == 0) {
                return part[1];
            }
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String formateDate">
    /**
     *
     * @param date
     * @return
     */
    public String formateDate(Date date) {
        String formattedDate = "";
        if (date != null) {
            formattedDate = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY, Locale.getDefault()).format(date);
        }
        return formattedDate;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="int randInt">
    /**
     *
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String generateNumberDays">
    /**
     *
     * @return
     */
    public static String generateNumberDays() {
        String result;
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int millis = now.get(Calendar.MILLISECOND);
        result = String.valueOf(year).substring(2) + String.valueOf(month) + String.valueOf(day)
                + String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second)
                + String.valueOf(millis);
        return result;
    }
    //</editor-fold>

    /**
     *
     * @return
     */
    public static String generateNumberDay() {
        Calendar now = Calendar.getInstance();
        char[] alphNum = "0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < 5; i++) {
            sb.append(alphNum[rnd.nextInt(alphNum.length)]);
        }
        int millis = now.get(Calendar.MILLISECOND);
        String id = sb.toString().trim() + String.valueOf(millis);
        return id;
    }

    //<editor-fold defaultstate="collapsed" desc="int sizeResultSet">
    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static int sizeResultSet(ResultSet rs) throws SQLException {
        int size = 0;
        while (rs.next()) {
            size++;
        }
        return size;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="List<HashMap<String, Object>> resultSetToList">
    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static List<HashMap<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String ConvertMonthSub">
    /**
     *
     * @param sDay
     * @return
     */
    public String ConvertMonthSub(int sDay) {
        String sReturn;
        Date currentDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(sDay));
        String strDay = new SimpleDateFormat("dd").format(currentDate);
        String strMonth = new SimpleDateFormat("MM").format(currentDate);
        String strYear = new SimpleDateFormat("yyyy").format(currentDate);
        sReturn = strDay + "/" + strMonth + "/" + strYear;
        return sReturn;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String ConvertMonthSub">
    /**
     *
     * @return
     */
    public String ConvertMonthCurent() {
        String sReturn;
        Date currentDate = new Date(System.currentTimeMillis());
        String strMonth = new SimpleDateFormat("MM").format(currentDate);
        String strYear = new SimpleDateFormat("yyyy").format(currentDate);
        sReturn = strMonth + "/" + strYear;
        return sReturn;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String GenNumberMilisecond">
    /**
     *
     * @return
     */
    public static String GenNumberMilisecond() {
        String sReturn;
        Calendar now = Calendar.getInstance();
        int millis = now.get(Calendar.MILLISECOND);
        sReturn = String.valueOf(millis);
        return sReturn;
    }
    //</editor-fold>

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] LoadFile(File file) throws IOException {
        byte[] bytes;
        try (InputStream is = new FileInputStream(file)) {
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
            }
            bytes = new byte[(int) length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
        }
        return bytes;
    }

    //<editor-fold defaultstate="collapsed" desc="RandomPassString">
    /**
     *
     * @return
     */
    public static String RandomPassString() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String CheckTwoDate">
    /**
     *
     * @param dateFrom
     * @param dateTo
     * @return
     */
    public static String CheckTwoDate(Date dateFrom, Date dateTo) {
        String sResultDay;
        try {
            long sDay;
            if (dateFrom != null && dateTo != null) {
                sDay = dateTo.getTime() - dateFrom.getTime();
                if (sDay > 0) {
                    sResultDay = "0#0";
                } else {
                    sResultDay = Definitions.CONFIG_EXCEPTION_STRING_ERROR_LESS + "#" + String.valueOf(sDay);
                }
            } else {
                sResultDay = Definitions.CONFIG_EXCEPTION_STRING_ERROR_NULL + "#0";
            }
        } catch (Exception e) {
            sResultDay = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#0";
            LogExceptionServlet(log, "CheckTwoDate: " + e.getMessage(), e);
        }
        return sResultDay;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="daysBetween">
    /**
     *
     * @param day1
     * @param day2
     * @return
     */
    public static int daysBetween(Calendar day1, Calendar day2) {
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getDateSetEnableRenew">
    /**
     *
     * @param strEffectiveDate
     * @param sDayPolicy
     * @return
     * @throws ParseException
     */
    public static int getDateSetEnableRenew(String strEffectiveDate, int sDayPolicy) throws ParseException {
        int DateExtendUpdate;
        try {
            SimpleDateFormat sf = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY);
            Date sDate1 = new Date();
            Date sDate2 = sf.parse(strEffectiveDate);
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(sDate1);
            cal2.setTime(sDate2);
            int sDay = daysBetween(cal1, cal2);
            if (sDay >= sDayPolicy) {
                DateExtendUpdate = 0;
            } else {
                DateExtendUpdate = 1;
            }
        } catch (Exception e) {
            LogExceptionServlet(log, "getDateSetEnableRenew: " + e.getMessage(), e);
            DateExtendUpdate = 0;
        }
        return DateExtendUpdate;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String LinkGroup">
    /**
     *
     * @param sURL
     * @return
     */
    public String LinkGroup(String sURL) {
        String strResult = sURL;
        if ("../General/ProfileAccessEdit.jsp".equals(sURL) || "../General/ProfileAccessAdd.jsp".equals(sURL)) {
            strResult = "../General/ProfileAccessList.jsp";
        }
        if ("../General/ConfigPolicyEdit.jsp".equals(sURL) || "../General/ConfigPolicyNew.jsp".equals(sURL)) {
            strResult = "../General/ConfigPolicyList.jsp";
        }
        if ("../User/UserEdit.jsp".equals(sURL) || "../User/UserNew.jsp".equals(sURL)) {
            strResult = "../User/UserList.jsp";
        }
        if ("../Profile/ProfileEdit.jsp".equals(sURL)) {
            strResult = "../Profile/ProfileList.jsp";
        }
        if ("../Rose/RoseEdit.jsp".equals(sURL) || "../Rose/RoseNew.jsp".equals(sURL)) {
            strResult = "../Rose/RoseList.jsp";
        }
        if ("../Log/HistoryView.jsp".equals(sURL)) {
            strResult = "../Log/HistoryList.jsp";
        }
        if ("../Certificate/CertExpireView.jsp".equals(sURL)) {
            strResult = "../Certificate/CertExpireList.jsp";
        }
        if ("../User/UserRoleEdit.jsp".equals(sURL) || "../User/UserRoleNew.jsp".equals(sURL)) {
            strResult = "../User/UserRole.jsp";
        }
        if ("../User/MenuScreenEdit.jsp".equals(sURL) || "../User/MenuScreenNew.jsp".equals(sURL)) {
            strResult = "../User/MenuScreenList.jsp";
        }
        if ("../General/BranchEdit.jsp".equals(sURL) || "../General/BranchNew.jsp".equals(sURL)) {
            strResult = "../General/BranchList.jsp";
        }
        if ("../General/CityProvinEdit.jsp".equals(sURL) || "../General/CityProvinNew.jsp".equals(sURL)) {
            strResult = "../General/CityProvin.jsp";
        }
        if ("../General/ResponseCodeEdit.jsp".equals(sURL) || "../General/ResponseCodeNew.jsp".equals(sURL)) {
            strResult = "../General/ResponseCodeList.jsp";
        }
        if ("../General/FunctionEdit.jsp".equals(sURL) || "../General/FunctionNew.jsp".equals(sURL)) {
            strResult = "../General/FunctionList.jsp";
        }
        if ("../Ca/CAEdit.jsp".equals(sURL) || "../Ca/CANew.jsp".equals(sURL)
                || "../Ca/CAPropertiesNew.jsp".equals(sURL) || "../Ca/CAPropertiesEdit.jsp".equals(sURL)) {
            strResult = "../Ca/CAList.jsp";
        }
        if ("../Ca/CertProfileEdit.jsp".equals(sURL) || "../Ca/CertProfileNew.jsp".equals(sURL)) {
            strResult = "../Ca/CertProfileList.jsp";
        }
        if ("../History/TransactionView.jsp".equals(sURL)) {
            strResult = "../History/TransactionList.jsp";
        }
        if ("../Owner/MessagingQueueApprove.jsp".equals(sURL)) {
            strResult = "../Owner/MessagingQueueList.jsp";
        }
        if ("../Owner/ChangeOwnerInfo.jsp".equals(sURL) || "../Owner/OwnerDispose.jsp".equals(sURL)
                || "../Owner/OwnerView.jsp".equals(sURL)) {
            strResult = "../Owner/OwnerList.jsp";
        }
        if ("../Config/ServerEntityEdit.jsp".equals(sURL) || "../Config/ServerEntityNew.jsp".equals(sURL)) {
            strResult = "../Config/ServerEntityList.jsp";
        }
        if ("../General/CertificateTypeNew.jsp".equals(sURL) || "../General/CertificateTypeEdit.jsp".equals(sURL)) {
            strResult = "../General/CertificateTypeList.jsp";
        }
        if ("../Certificate/CertificateApprove.jsp".equals(sURL)) {
            strResult = "../Certificate/CertificateList.jsp";
        }
//        if ("../Certificate/RegisterCertView.jsp".equals(sURL) || "../Certificate/RegisterCertUnAssign.jsp".equals(sURL)
//            || "../Certificate/ReRegisterCert.jsp".equals(sURL) || "../Certificate/RegisterCertSoft.jsp".equals(sURL)
//            || "../Certificate/ReRegisterCertSoft.jsp".equals(sURL))
//        {
//            strResult = "../Certificate/RegisterCertList.jsp";
//        }
        if ("../Certificate/RenewCertView.jsp".equals(sURL) || "../Certificate/ChangeCertView.jsp".equals(sURL)
                || "../Certificate/RevokeCertView.jsp".equals(sURL) || "../Certificate/ReIssueCertView.jsp".equals(sURL)
                || "../Certificate/CertView.jsp".equals(sURL) || "../Certificate/PrintHandover.jsp".equals(sURL)
                || "../Certificate/PrintRegisterPersonal.jsp".equals(sURL) || "../Certificate/PrintRegisterBusiness.jsp".equals(sURL)
                || "../Certificate/PrintRenewPersonal.jsp".equals(sURL) || "../Certificate/PrintRenewBusiness.jsp".equals(sURL)
                || "../Certificate/SuspendCertView.jsp".equals(sURL) || "../Certificate/RecoveryCertView.jsp".equals(sURL)
                || "../Certificate/BuyCertMore.jsp".equals(sURL) || "../Certificate/CertificateEdit.jsp".equals(sURL)) {
            strResult = "../Certificate/RenewCertList.jsp";
        }
        if ("../Certificate/CertificateShareAddMore.jsp".equals(sURL) || "../Certificate/CertificateShareChange.jsp".equals(sURL)
                || "../Certificate/CertificateShareReissue.jsp".equals(sURL) || "../CertificateShareRenew/ReIssueCertView.jsp".equals(sURL)) {
            strResult = "../Certificate/CertificateShareList.jsp";
        }
        if ("../Certificate/RequestView.jsp".equals(sURL)) {
            strResult = "../Certificate/RequestList.jsp";
        }
        if ("../Certificate/RevokeView.jsp".equals(sURL)) {
            strResult = "../Certificate/RevokeList.jsp";
        }
        if ("../Certificate/CertificateReportView.jsp".equals(sURL)) {
            strResult = "../Certificate/CertificateReportList.jsp";
        }
        if ("../Token/TokenEdit.jsp".equals(sURL) || "../Token/InitToken.jsp".equals(sURL)
                || "../Token/ChangeSOPIN.jsp".equals(sURL) || "../Token/TokenNew.jsp".equals(sURL)) {
            strResult = "../Token/TokenList.jsp";
        }
        if ("../Token/TokenLogList.jsp".equals(sURL)) {
            strResult = "../Token/TokenLogView.jsp";
        }
        if ("../Token/TokenApproveView.jsp".equals(sURL)) {
            strResult = "../Token/TokenApproveList.jsp";
        }
        return strResult;
    }
    //</editor-fold>

    /**
     *
     * @param originalFileName
     * @return
     */
    public static boolean checkFileSpecial(String originalFileName) {
        boolean isValid = true;
        final String[] splChars = {"#", "+", "$", "\"", "'", "@", "%", "^", "&", "!", "*", "?", "/", "\\", "{", "}", ";", ":", "?", ",", "~", "*", "<", ">", "[", "]", "|"};
        for (String splChar : splChars) {
            if (originalFileName.contains(splChar)) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    /**
     *
     * @param originalFileName
     * @return
     */
    public static boolean checkCertCharacterSpecial(String originalFileName) {
        boolean isValid = true;
//        final String[] splChars = {"=", "\"", "\\"};
        final String[] splChars = {"=", "\\"};
        for (String splChar : splChars) {
            if (originalFileName.contains(splChar)) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    /**
     *
     * @param strFileName
     * @return
     */
    public static String getExtendFile(String strFileName) {
        String strFileNames;
        if (!strFileName.contains(".")) {
            strFileNames = "";
        } else {
            strFileNames = FilenameUtils.getExtension(strFileName);
        }
        return strFileNames;
    }

    /**
     *
     * @param strFileName
     * @return
     */
    public static boolean checkExtendFIle(String strFileName) {
        boolean s;
        if (!strFileName.contains(".")) {
            s = false;
        } else {
            String strFileNames = FilenameUtils.getExtension(strFileName);
            if (null == strFileNames.toUpperCase()) {
                s = "XLSX".equals(strFileNames.toUpperCase());
            } else {
                switch (strFileNames.toUpperCase()) {
                    case "XLS":
                        s = true;
                        break;
                    case "CSV":
                        s = true;
                        break;
                    default:
                        s = "XLSX".equals(strFileNames.toUpperCase());
                        break;
                }
            }
        }
        return s;
    }

    /**
     *
     * @param sFileName
     * @param sArrayFormat
     * @return
     */
    public static boolean checkExtendAttachCertFile(String sFileName, String sArrayFormat) {
        boolean isResult = false;
        String strFileExten = EscapeUtils.CheckTextNull(FilenameUtils.getExtension(sFileName));
        if (!"".equals(strFileExten)) {
            if (!"".equals(sArrayFormat)) {
                String[] pItemArray = sArrayFormat.split(";");
                for (String sPlitValue : pItemArray) {
                    if (strFileExten.toUpperCase().equals(sPlitValue.trim().toUpperCase())) {
                        isResult = true;
                        break;
                    }
                }
            }
        }
        return isResult;
    }

    /**
     *
     * @param sValue
     * @return
     */
    public static String CheckReplaceImport(String sValue) {
        String sResult;
        sResult = sValue.replace("'", "").trim();
        return sResult;
    }

    /**
     *
     * @param sLinkFile
     * @return
     * @throws IOException
     */
    public static ArrayList readExcelFileLiveXLSX(String sLinkFile) throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                XSSFRow myRow = (XSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                XSSFCell cellSTT = myRow.getCell(0);
                XSSFCell cellChannel = myRow.getCell(1);
                XSSFCell cellUser = myRow.getCell(2);
                XSSFCell cellWorkerID = myRow.getCell(3);
                XSSFCell cellWorkerName = myRow.getCell(4);
                XSSFCell cellKeyName = myRow.getCell(5);
                XSSFCell cellCSR = myRow.getCell(6);
                XSSFCell cellDN = myRow.getCell(7);
                XSSFCell cellCA = myRow.getCell(8);
                XSSFCell cellCert = myRow.getCell(9);
                XSSFCell cellChain = myRow.getCell(10);
                if (CheckCellXSSFEmpty(cellSTT) == null && CheckCellXSSFEmpty(cellChannel) == null
                        && CheckCellXSSFEmpty(cellUser) == null && CheckCellXSSFEmpty(cellWorkerID) == null
                        && CheckCellXSSFEmpty(cellWorkerName) == null && CheckCellXSSFEmpty(cellKeyName) == null
                        && CheckCellXSSFEmpty(cellCSR) == null && CheckCellXSSFEmpty(cellDN) == null && CheckCellXSSFEmpty(cellCA) == null
                        && CheckCellXSSFEmpty(cellCert) == null && CheckCellXSSFEmpty(cellChain) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                    }
                    if (cellChannel == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellChannel.toString()));
                    }
                    if (cellUser == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellUser.toString()));
                    }
                    if (cellWorkerID == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellWorkerID.toString()));
                    }
                    if (cellWorkerName == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellWorkerName.toString()));
                    }
                    if (cellKeyName == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellKeyName.toString()));
                    }
                    if (cellCSR == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellCSR.toString()));
                    }
                    if (cellDN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellDN.toString()));
                    }
                    if (cellCA == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellCA.toString()));
                    }
                    if (cellCert == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellCert.toString()));
                    }
                    if (cellChain == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellChain.toString()));
                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            LogExceptionServlet(log, "readExcelFileLiveXLSX: " + e.getMessage(), e);
        } finally {
            if (new File(sLinkFile).exists()) {
                new File(sLinkFile).delete();
            }
        }
        return cellArrayLisstHolder;
    }

    /**
     *
     * @param sLinkFile
     * @return
     * @throws IOException
     */
    public static ArrayList readExcelFileLive(String sLinkFile) throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                HSSFCell cellSTT = myRow.getCell(0);
                HSSFCell cellChannel = myRow.getCell(1);
                HSSFCell cellUser = myRow.getCell(2);
                HSSFCell cellWorkerID = myRow.getCell(3);
                HSSFCell cellWorkerName = myRow.getCell(4);
                HSSFCell cellKeyName = myRow.getCell(5);
                HSSFCell cellCSR = myRow.getCell(6);
                HSSFCell cellDN = myRow.getCell(7);
                HSSFCell cellCA = myRow.getCell(8);
                HSSFCell cellCert = myRow.getCell(9);
                HSSFCell cellChain = myRow.getCell(10);
                if (CheckCellHSSFEmpty(cellSTT) == null && CheckCellHSSFEmpty(cellChannel) == null
                        && CheckCellHSSFEmpty(cellUser) == null && CheckCellHSSFEmpty(cellWorkerID) == null
                        && CheckCellHSSFEmpty(cellWorkerName) == null && CheckCellHSSFEmpty(cellKeyName) == null
                        && CheckCellHSSFEmpty(cellCSR) == null && CheckCellHSSFEmpty(cellDN) == null && CheckCellHSSFEmpty(cellCA) == null
                        && CheckCellHSSFEmpty(cellCert) == null && CheckCellHSSFEmpty(cellChain) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                    }
                    if (cellChannel == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellChannel.toString()));
                    }
                    if (cellUser == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellUser.toString()));
                    }
                    if (cellWorkerID == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellWorkerID.toString()));
                    }
                    if (cellWorkerName == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellWorkerName.toString()));
                    }
                    if (cellKeyName == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellKeyName.toString()));
                    }
                    if (cellCSR == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellCSR.toString()));
                    }
                    if (cellDN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellDN.toString()));
                    }
                    if (cellCA == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellCA.toString()));
                    }
                    if (cellCert == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellCert.toString()));
                    }
                    if (cellChain == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellChain.toString()));
                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            System.out.println("readExcelFileLive: " + e.getMessage() + Definitions.CONFIG_LOG_WRITE_DOWNLINE);
        } finally {
            if (new File(sLinkFile).exists()) {
                new File(sLinkFile).delete();
            }
        }
        return cellArrayLisstHolder;
    }

    /**
     *
     * @param ssa
     * @return
     */
    public static String CheckCellHSSFEmpty(HSSFCell ssa) {
        String sResult = "";
        if (ssa == null) {
            sResult = null;
        } else {
            if ("".equals(ssa.toString().trim())) {
                sResult = null;
            }
        }
        return sResult;
    }

    /**
     *
     * @param ssa
     * @return
     */
    public static String CheckCellXSSFEmpty(XSSFCell ssa) {
        String sResult = "";
        if (ssa == null) {
            sResult = null;
        } else {
            if ("".equals(ssa.toString().trim())) {
                sResult = null;
            }
        }
        return sResult;
    }

    public static String getThumbprintCertificate(String base64Cert) {
        String thumbprint = "";
        try {
            if (base64Cert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
                base64Cert = base64Cert.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN, "");
            }
            if (base64Cert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
                base64Cert = base64Cert.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END, "");
            }
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            InputStream in = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(base64Cert));
            X509Certificate x509 = (X509Certificate) certFactory.generateCertificate(in);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(x509.getEncoded());
            byte[] hash = md.digest();
            thumbprint = DatatypeConverter.printHexBinary(hash);
        } catch (CertificateException | NoSuchAlgorithmException e) {
            LogExceptionServlet(log, "getThumbprintCertificate: " + e.getMessage(), e);
        }
        return thumbprint;
    }

    public static String getPublicKeyCertificate(String base64Cert) {
        String thumbprint = "";
        try {
            if (base64Cert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
                base64Cert = base64Cert.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN, "");
            }
            if (base64Cert.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
                base64Cert = base64Cert.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END, "");
            }
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            InputStream in = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(base64Cert));
            X509Certificate x509 = (X509Certificate) certFactory.generateCertificate(in);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(x509.getEncoded());
            byte[] hash = md.digest();
            thumbprint = DatatypeConverter.printHexBinary(hash);
        } catch (CertificateException | NoSuchAlgorithmException e) {
            LogExceptionServlet(log, "getPublicKeyCertificate: " + e.getMessage(), e);
        }
        return thumbprint;
    }

    public static boolean compareCurrentDateCert(String sDateInput)
            throws ParseException {
        Date currentDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY);
        String strCurrentDate = df.format(currentDate);
        SimpleDateFormat sdf = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY);
        Date first = sdf.parse(sDateInput);
        Date second = sdf.parse(strCurrentDate);
        boolean before = (second.before(first));
        return before;
    }

    public static String processCompleteText(String completeText) {
        String result = "";
        if (!"".equals(completeText)) {
            Scanner scanner = new Scanner(completeText);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.replaceAll(" ", "").contains("pin=")
                        || line.replaceAll(" ", "").contains("PIN=")) {
                    line = "  pin=********";
                }
                result += line + "\n";
            }
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="String convertMoney">
    public String convertMoney(int doublePayment) {
        String formattedString;
        Double doublePayment1 = (double) doublePayment;
        if (doublePayment1 != 0) {
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            formattedString = decimalFormat.format(doublePayment1);
        } else {
            formattedString = "";
        }
        return formattedString;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String convertMoneyZero">
    public String convertMoneyZero(int doublePayment) {
        String formattedString;
        Double doublePayment1 = (double) doublePayment;
        if (doublePayment1 != 0) {
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            formattedString = decimalFormat.format(doublePayment1);
        } else {
            formattedString = "0";
        }
        return formattedString;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String convertMoneyAnotherZero">
    public String convertMoneyAnotherZero(int doublePayment) {
        String formattedString;
        if (doublePayment != 0) {
            Double doublePayment1 = (double) doublePayment;
            if (doublePayment1 != 0) {
                String pattern = "###,###.###";
                DecimalFormat decimalFormat = new DecimalFormat(pattern);
                formattedString = decimalFormat.format(doublePayment1);
            } else {
                formattedString = "0";
            }
        } else {
            formattedString = "0";
        }
        return formattedString;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String convertMoneyDoubleZero">
    public String convertMoneyDoubleZero(double doublePayment) {
        String formattedString;
        if (doublePayment != 0) {
//            Double doublePayment1 = (double) doublePayment;
            if (doublePayment != 0) {
                String pattern = "###,###.###";
                DecimalFormat decimalFormat = new DecimalFormat(pattern);
                formattedString = decimalFormat.format(doublePayment);
            } else {
                formattedString = "0";
            }
        } else {
            formattedString = "0";
        }
        return formattedString;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Byte[] convertMoney">
    public static byte[] getByteFromURL(String sURL) throws MalformedURLException, IOException {
        byte[] sByte;
        URL u = new URL(sURL);
        URLConnection uc = u.openConnection();
        String contentType = uc.getContentType();
        int contentLength = uc.getContentLength();
        if (contentType.startsWith("text/") || contentLength == -1) {
            throw new IOException("This is not a binary file.");
        }
        InputStream raw = uc.getInputStream();
        sByte = IOUtils.toByteArray(raw);
        return sByte;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Byte[] convertMoney">
    public static void getComponentFromCRL(byte[] byteCRL, String[] sIssuer, Date[] dateNext, Date[] dateThis)
            throws CertificateException, CRLException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509CRL x509crl = (X509CRL) cf.generateCRL(new ByteArrayInputStream(byteCRL));
        sIssuer[0] = SubstringCNInCert(x509crl.getIssuerDN().toString());
        dateNext[0] = x509crl.getNextUpdate();
        dateThis[0] = x509crl.getThisUpdate();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SubstringCNInCert">
    public static String SubstringCNInCert(String strSubjectDN) {
        String sResult = "";
        try {
            if (strSubjectDN.contains(Definitions.CONFIG_COMPONENT_DN_SUBSTRING_CN_KEY)) {
                String sPart1 = strSubjectDN.split(Definitions.CONFIG_COMPONENT_DN_SUBSTRING_CN_KEY)[1];
                sResult = sPart1.split(Definitions.CONFIG_COMPONENT_DN_SUBSTRING_CN_VALUE)[0].replace("\"", "");
            } else {
                sResult = getAttributeCertificate(strSubjectDN, Definitions.CONFIG_COMPONENT_DN_TAG_CN, "=");
            }
        } catch (Exception e) {
            CommonFunction.LogExceptionServlet(log, "SubstringCNInCert: " + e.getMessage(), e);
        }
        return sResult;
    }
    //</editor-fold>

    /**
     *
     * @param dn
     * @param key
     * @param sChar
     * @return
     */
    public static String getAttributeCertificate(String dn, String key, String sChar) {
        String[] parts = dn.split(",");
        for (String part1 : parts) {
            String[] part = part1.split(sChar);
            part[0] = part[0].trim();
            if (part[0].compareTo(key) == 0) {
                return part[1];
            }
        }
        return "";
    }

    public static boolean CheckURLAccessRootAdmin(String sURL, String sRole, String sAgent) {
        boolean isAccess = false;
        if (sURL.contains(Definitions.CONFIG_PAGE_POLICY_LIST) || sURL.contains(Definitions.CONFIG_PAGE_EMAIL_LIST)
                || sURL.contains(Definitions.CONFIG_PAGE_PARAMBACK_LIST) || sURL.contains(Definitions.CONFIG_PAGE_PARAMBACK_EDIT)
                || sURL.contains(Definitions.CONFIG_PAGE_AGENT_LIST) || sURL.contains(Definitions.CONFIG_PAGE_AGENT_EDIT)
                || sURL.contains(Definitions.CONFIG_PAGE_AGENT_ADD)) {
            if (sAgent.equals(Definitions.CONFIG_AGENT_ROOT) && sRole.equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)) {
                isAccess = true;
            }
        } else {
            isAccess = true;
        }
        return isAccess;
    }

    public static boolean StrCheckRoleUser(String strCheck, String strNumber) {
        boolean sss = false;
        try {
            String[] parts = strCheck.split(",");
            for (String part : parts) {
                if (strNumber.equals(part.trim())) {
                    sss = true;
                    break;
                }
            }
        } catch (Exception ex) {
            sss = false;
            LogExceptionJSP("StrCheckRoleUser", ex.getMessage(), ex);
        }
        return sss;
    }

    public static String CheckRoleSet(String strCheck, String strNumber) {
        String sCheck = "";
        if (StrCheckRoleUser(strCheck, strNumber)) {
            sCheck = "checked";
        }
        return sCheck;
    }

    public boolean CheckRoleSetFunctions(String strCheck, String strNumber) {
        boolean sCheck = false;
        if (StrCheckRoleUser(strCheck, strNumber)) {
            sCheck = true;
        }
        return sCheck;
    }

    public static String CheckRoleSetMenuLeft(String strCheck, String strNumber) {
        String sCheck = "none;";
        if (StrCheckRoleUser(strCheck, strNumber)) {
            sCheck = "";
        }
        return sCheck;
    }

    public static String GenJSONTokenLog(TOKEN_CHANGE_LOG tempLogText) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(tempLogText);
    }

    public static String GenJSONTokenATTR(ATTRIBUTE_VALUES tempLogText) throws JsonProcessingException {
        if (tempLogText != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(tempLogText);
        } else {
            return "";
        }
    }

    public static String generateCertificateSerialNumber() throws Exception {
        String uniqueID = UUID.randomUUID().toString();
        String datetime = String.valueOf(System.currentTimeMillis());
        String randomValue = datetime + uniqueID;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(randomValue.getBytes());
        String hashed = DatatypeConverter.printHexBinary(md.digest()).toLowerCase();
        String t = "54010110" + hashed;
        return t.substring(0, 32);
    }

    public static ArrayList readExcelImportTokenXLSX(String sLinkFile) throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                XSSFRow myRow = (XSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                XSSFCell cellSNB = myRow.getCell(0);
                XSSFCell cellName = myRow.getCell(1);
//                XSSFCell cellSOPIN = myRow.getCell(2);  && CheckCellXSSFEmpty(cellSOPIN) == null
                if (CheckCellXSSFEmpty(cellSNB) == null && CheckCellXSSFEmpty(cellName) == null) {

                } else {
                    if (cellSNB == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellSNB.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellSNB.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSNB.toString()));
                        }
                    }
                    if (cellName == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellName.toString()));
                    }
//                    if (cellSOPIN == null) {
//                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
//                    } else {
//                        cellStoreArrayList.add(CheckReplaceImport(cellSOPIN.toString()));
//                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            log.error("readExcelFileLiveXLSX: " + e.getMessage() + ".\n-----------------------------------", e);
        }
        return cellArrayLisstHolder;
    }

    public static ArrayList readExcelImportTokenXLS(String sLinkFile) throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                HSSFCell cellSNB = myRow.getCell(0);
                HSSFCell cellName = myRow.getCell(1);
//                HSSFCell cellSOPIN = myRow.getCell(2);  && CheckCellHSSFEmpty(cellSOPIN) == null
                if (CheckCellHSSFEmpty(cellSNB) == null && CheckCellHSSFEmpty(cellName) == null) {

                } else {
                    if (cellSNB == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellSNB.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellSNB.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSNB.toString()));
                        }
                    }
                    if (cellName == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellName.toString()));
                    }
//                    if (cellSOPIN == null) {
//                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
//                    } else {
//                        cellStoreArrayList.add(CheckReplaceImport(cellSOPIN.toString()));
//                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            System.out.println("readExcelFileLive: " + e.getMessage() + ".\n-----------------------------------");
        }
        return cellArrayLisstHolder;
    }

    public static ArrayList readExcelImportPushXLSX(String sLinkFile) throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                XSSFRow myRow = (XSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                XSSFCell cellSTT = myRow.getCell(0);
                XSSFCell cellPrefixDN = myRow.getCell(1);
                XSSFCell cellUIDDN = myRow.getCell(2);
                XSSFCell cellPrefixCN = myRow.getCell(3);
                XSSFCell cellUIDCN = myRow.getCell(4);
                XSSFCell cellSN = myRow.getCell(5);
                if (CheckCellXSSFEmpty(cellSTT) == null && CheckCellXSSFEmpty(cellPrefixDN) == null && CheckCellXSSFEmpty(cellUIDDN) == null
                        && CheckCellXSSFEmpty(cellPrefixCN) == null && CheckCellXSSFEmpty(cellUIDCN) == null
                        && CheckCellXSSFEmpty(cellSN) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                    }
                    if (cellPrefixDN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellPrefixDN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellPrefixDN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellPrefixDN.toString()));
                        }
                    }
                    if (cellUIDDN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellUIDDN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellUIDDN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellUIDDN.toString()));
                        }
                    }
                    if (cellPrefixCN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellPrefixCN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellPrefixCN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellPrefixCN.toString()));
                        }
                    }
                    if (cellUIDCN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellUIDCN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellUIDCN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellUIDCN.toString()));
                        }
                    }
                    if (cellSN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellSN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellSN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSN.toString()));
                        }
                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            log.error("readExcelImportPushXLSX: " + e.getMessage() + ".\n-----------------------------------", e);
        }
        return cellArrayLisstHolder;
    }

    public static ArrayList readExcelImportPushXLS(String sLinkFile) throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                HSSFCell cellSTT = myRow.getCell(0);
                HSSFCell cellPrefixDN = myRow.getCell(1);
                HSSFCell cellUIDDN = myRow.getCell(2);
                HSSFCell cellPrefixCN = myRow.getCell(3);
                HSSFCell cellUIDCN = myRow.getCell(4);
                HSSFCell cellSN = myRow.getCell(5);
                if (CheckCellHSSFEmpty(cellSTT) == null && CheckCellHSSFEmpty(cellPrefixDN) == null
                        && CheckCellHSSFEmpty(cellUIDDN) == null && CheckCellHSSFEmpty(cellPrefixCN) == null && CheckCellHSSFEmpty(cellUIDCN) == null
                        && CheckCellHSSFEmpty(cellSN) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                    }
                    if (cellPrefixDN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellPrefixDN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellPrefixDN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellPrefixDN.toString()));
                        }
                    }
                    if (cellUIDDN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellUIDDN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellUIDDN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellUIDDN.toString()));
                        }
                    }
                    if (cellPrefixCN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellPrefixCN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellPrefixCN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellPrefixCN.toString()));
                        }
                    }
                    if (cellUIDCN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellUIDCN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellUIDCN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellUIDCN.toString()));
                        }
                    }
                    if (cellSN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellSN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellSN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSN.toString()));
                        }
                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            System.out.println("readExcelImportPushXLS: " + e.getMessage() + ".\n-----------------------------------");
        }
        return cellArrayLisstHolder;
    }

    public static ArrayList readExcelImportCollectProfileXLSX(String sLinkFile) throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                XSSFRow myRow = (XSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                XSSFCell cellSTT = myRow.getCell(0);
                XSSFCell cellCertSN = myRow.getCell(1);
//                XSSFCell cellPrefixEnterprise = myRow.getCell(1);
//                XSSFCell cellUIDEnterprise = myRow.getCell(2);
//                XSSFCell cellPrefixPersonal = myRow.getCell(3);
//                XSSFCell cellUIDPersonal = myRow.getCell(4);
                if (CheckCellXSSFEmpty(cellSTT) == null && CheckCellXSSFEmpty(cellCertSN) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellSTT.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellSTT.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                        }
                    }
                    if (cellCertSN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellCertSN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellCertSN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertSN.toString()));
                        }
                    }
//                    if (cellUIDEnterprise == null) {
//                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
//                    } else {
//                        if(cellUIDEnterprise.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            String sTempProcess = NumberToTextConverter.toText(cellUIDEnterprise.getNumericCellValue());
//                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
//                        } else {
//                            cellStoreArrayList.add(CheckReplaceImport(cellUIDEnterprise.toString()));
//                        }
//                    }
//                    if (cellPrefixPersonal == null) {
//                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
//                    } else {
//                        if(cellPrefixPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            String sTempProcess = NumberToTextConverter.toText(cellPrefixPersonal.getNumericCellValue());
//                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
//                        } else {
//                            cellStoreArrayList.add(CheckReplaceImport(cellPrefixPersonal.toString()));
//                        }
//                    }
//                    if (cellUIDPersonal == null) {
//                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
//                    } else {
//                        if(cellUIDPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            String sTempProcess = NumberToTextConverter.toText(cellUIDPersonal.getNumericCellValue());
//                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
//                        } else {
//                            cellStoreArrayList.add(CheckReplaceImport(cellUIDPersonal.toString()));
//                        }
//                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            log.error("readExcelImportPushXLSX: " + e.getMessage() + ".\n-----------------------------------", e);
        }
        return cellArrayLisstHolder;
    }

    public static ArrayList readExcelImportCollectProfileXLS(String sLinkFile) throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                HSSFCell cellSTT = myRow.getCell(0);
                HSSFCell cellCertSN = myRow.getCell(1);
//                HSSFCell cellUIDEnterprise = myRow.getCell(2);
//                HSSFCell cellPrefixPersonal = myRow.getCell(3);
//                HSSFCell cellUIDPersonal = myRow.getCell(4);
                if (CheckCellHSSFEmpty(cellSTT) == null && CheckCellHSSFEmpty(cellCertSN) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellSTT.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellSTT.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                        }
                    }
                    if (cellCertSN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        if (cellCertSN.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            String sTempProcess = NumberToTextConverter.toText(cellCertSN.getNumericCellValue());
                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertSN.toString()));
                        }
                    }
//                    if (cellUIDEnterprise == null) {
//                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
//                    } else {
//                        if(cellUIDEnterprise.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            String sTempProcess = NumberToTextConverter.toText(cellUIDEnterprise.getNumericCellValue());
//                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
//                        } else {
//                            cellStoreArrayList.add(CheckReplaceImport(cellUIDEnterprise.toString()));
//                        }
//                    }
//                    if (cellPrefixPersonal == null) {
//                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
//                    } else {
//                        if(cellPrefixPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            String sTempProcess = NumberToTextConverter.toText(cellPrefixPersonal.getNumericCellValue());
//                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
//                        } else {
//                            cellStoreArrayList.add(CheckReplaceImport(cellPrefixPersonal.toString()));
//                        }
//                    }
//                    if (cellUIDPersonal == null) {
//                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
//                    } else {
//                        if(cellUIDPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                            String sTempProcess = NumberToTextConverter.toText(cellUIDPersonal.getNumericCellValue());
//                            cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
//                        } else {
//                            cellStoreArrayList.add(CheckReplaceImport(cellUIDPersonal.toString()));
//                        }
//                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            System.out.println("readExcelImportPushXLS: " + e.getMessage() + ".\n-----------------------------------");
        }
        return cellArrayLisstHolder;
    }

    public static ArrayList readExcelImportDisallowanceXLSX(String sLinkFile)
            throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                XSSFRow myRow = (XSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                XSSFCell cellSTT = myRow.getCell(0);
                XSSFCell cellMST = myRow.getCell(1);
                XSSFCell cellMNS = myRow.getCell(2);
                if (CheckCellXSSFEmpty(cellSTT) == null && CheckCellXSSFEmpty(cellMST) == null
                        && CheckCellXSSFEmpty(cellMNS) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                    }
                    if (cellMST == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellMST.toString()));
                    }
                    if (cellMNS == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellMNS.toString()));
                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            log.error("readExcelImportDisallowanceXLSX: " + e.getMessage() + ".\n-----------------------------------", e);
        }
        return cellArrayLisstHolder;
    }

    public static ArrayList readExcelImportDisallowanceXLS(String sLinkFile)
            throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                HSSFCell cellSTT = myRow.getCell(0);
                HSSFCell cellMST = myRow.getCell(1);
                HSSFCell cellMNS = myRow.getCell(2);
                if (CheckCellHSSFEmpty(cellSTT) == null && CheckCellHSSFEmpty(cellMST) == null
                        && CheckCellHSSFEmpty(cellMNS) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                    }
                    if (cellMST == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellMST.toString()));
                    }
                    if (cellMNS == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellMNS.toString()));
                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            System.out.println("readExcelImportDisallowanceXLS: " + e.getMessage() + ".\n-----------------------------------");
        }
        return cellArrayLisstHolder;
    }

    public static boolean checkSNB(String snb) {
        Pattern phoneNumFormat
                = Pattern.compile("[0-9a-zA-Z]+");
        if (snb.isEmpty()) {
            return false;
        } else if (!phoneNumFormat.matcher(snb).matches()) {

            return false;
        } else if (snb.length() > 32) {
            return false;
        }

        return true;
    }

    public static boolean checkSOPINEncode(String sopin) {
        EncodeSOPIN encript = new EncodeSOPIN();
        try {
            String sopinDecode = encript.decode(sopin);
            return !"".equals(sopinDecode);
        } catch (Exception e) {
            return false;
        }
    }

    public static String formatSerialNumber(String certstr) throws CertificateException {
        if (certstr.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN_CONTAINS)) {
            certstr = certstr.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_BEGIN, "");
        }
        if (certstr.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END_CONTAINS)) {
            certstr = certstr.replace(Definitions.CONFIG_WORKER_TAG_CERTIFICATE_END, "");
        }
        CertificateFactory certFactory1 = CertificateFactory.getInstance("X.509");
        InputStream in = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certstr));
        X509Certificate cert = (X509Certificate) certFactory1.generateCertificate(in);
        byte[] serialNumber = cert.getSerialNumber().toByteArray();
        String result = "";
        for (int i = 0; i < serialNumber.length; i++) {
            if (i == serialNumber.length - 1) {
                result += String.format("%02X", serialNumber[i]);
            } else {
                result += String.format("%02X", serialNumber[i]) + " ";
            }
        }
        return result;
    }
    // GET ATTRIBUTE IN COMPONENT DN
    public static final String OID_ST = "2.5.4.8";

    public static String getStateOrProvinceInDN(String dn) {
        X500Name subject = new X500Name(dn);
        RDN[] rdn = subject.getRDNs();
        for (int j = 0; j < rdn.length; j++) {
            AttributeTypeAndValue[] attributeTypeAndValue = rdn[j].getTypesAndValues();
            if (attributeTypeAndValue[0].getType().toString().equals(OID_ST)) {
                return attributeTypeAndValue[0].getValue().toString();
            }
        }
        return "";
    }

    public static final String OID_EMAIL = "1.2.840.113549.1.9.1";

    public static String getEmailInDN(String dn) {
        X500Name subject = new X500Name(dn);
        RDN[] rdn = subject.getRDNs();
        for (int j = 0; j < rdn.length; j++) {
            AttributeTypeAndValue[] attributeTypeAndValue = rdn[j].getTypesAndValues();
            if (attributeTypeAndValue[0].getType().toString().equals(OID_EMAIL)) {
                return attributeTypeAndValue[0].getValue().toString();
            }
        }
        return "";
    }

    public static final String OID_PHONE = "2.5.4.20";

    public static String getPhoneInDN(String dn) {
        X500Name subject = new X500Name(dn);
        RDN[] rdn = subject.getRDNs();
        for (int j = 0; j < rdn.length; j++) {
            AttributeTypeAndValue[] attributeTypeAndValue = rdn[j].getTypesAndValues();
            if (attributeTypeAndValue[0].getType().toString().equals(OID_PHONE)) {
                return attributeTypeAndValue[0].getValue().toString();
            }
        }
        return "";
    }

    public static final String OID_T = "2.5.4.12";

    public static String getRoleInDN(String dn) {
        X500Name subject = new X500Name(dn);
        RDN[] rdn = subject.getRDNs();
        for (int j = 0; j < rdn.length; j++) {
            AttributeTypeAndValue[] attributeTypeAndValue = rdn[j].getTypesAndValues();
            if (attributeTypeAndValue[0].getType().toString().equals(OID_T)) {
                return attributeTypeAndValue[0].getValue().toString();
            }
        }
        return "";
    }

    public static final String OID_L = "2.5.4.7";

    public static String getLocationInDN(String dn) {
        X500Name subject = new X500Name(dn);
        RDN[] rdn = subject.getRDNs();
        for (int j = 0; j < rdn.length; j++) {
            AttributeTypeAndValue[] attributeTypeAndValue = rdn[j].getTypesAndValues();
            if (attributeTypeAndValue[0].getType().toString().equals(OID_L)) {
                return attributeTypeAndValue[0].getValue().toString();
            }
        }
        return "";
    }
    public static final String OID_OU = "2.5.4.11";

    public static String getDepartmentInDN(String dn) {
        X500Name subject = new X500Name(dn);
        RDN[] rdn = subject.getRDNs();
        for (int j = 0; j < rdn.length; j++) {
            AttributeTypeAndValue[] attributeTypeAndValue = rdn[j].getTypesAndValues();
            if (attributeTypeAndValue[0].getType().toString().equals(OID_OU)) {
                return attributeTypeAndValue[0].getValue().toString();
            }
        }
        return "";
    }

    public static final String OID_C = "2.5.4.6";

    public static String getCountryInDN(String dn) {
        X500Name subject = new X500Name(dn);
        RDN[] rdn = subject.getRDNs();
        for (int j = 0; j < rdn.length; j++) {
            AttributeTypeAndValue[] attributeTypeAndValue = rdn[j].getTypesAndValues();
            if (attributeTypeAndValue[0].getType().toString().equals(OID_C)) {
                return attributeTypeAndValue[0].getValue().toString();
            }
        }
        return "";
    }

    public static final String OID_O = "2.5.4.10";

    public static String getORGANIZATIONInDN(String dn) {
        X500Name subject = new X500Name(dn);
        RDN[] rdn = subject.getRDNs();
        for (int j = 0; j < rdn.length; j++) {
            AttributeTypeAndValue[] attributeTypeAndValue = rdn[j].getTypesAndValues();
            if (attributeTypeAndValue[0].getType().toString().equals(OID_O)) {
                return attributeTypeAndValue[0].getValue().toString();
            }
        }
        return "";
    }

    public static byte[] saveByteArrayOutputStream(InputStream body) {
        int c;
        byte[] r = null;
        try {
            ByteArrayOutputStream f = new ByteArrayOutputStream();
            while ((c = body.read()) > -1) {
                f.write(c);
            }
            r = f.toByteArray();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    public static String getRandomOTP(int range) {
        String out = "";
        for (int i = 0; i < range; i++) {
            out += (int) (Math.random() * 10);
        }
        return out;
    }

    public static String getClientIpLogin(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr.trim();
    }

    public static boolean checkViewTokenValid(String sTOKEN_SN) {
        boolean bTokenValid = true;
        if (sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SN_54100000001000)
                || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SSL_SN)
                || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_CODESIGNNING_SN)
                || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SIGNSERVER_SN)
                || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SN_LOST)
                || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_UNASSIGN_SN)
                || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SN_BULK_TOKEN)
                || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SN_FORMFACTOR_ESIGNCLOUD)
                || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_DEVICE_SN)
                || sTOKEN_SN.equals(Definitions.CONFIG_TOKEN_SN_INHOUSE)) {
            bTokenValid = false;
        }
        return bTokenValid;
    }

    public static String dateConvertString(Date sTOKEN_SN) {
        String sDate = "";
        if (sTOKEN_SN != null) {
            sDate = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY).format(sTOKEN_SN);
        }
        return sDate;
    }

    public static String getDateFormat() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    public static String ConvertDateUpdatePolicy(final String sDateBegin) {
        String sDate = "";
        try {
//            LogDebugString(CommonFunction.log, "ConvertDateUpdatePolicy: Date-Begin", sDateBegin);
            if (!"".equals(sDateBegin)) {
                final String vDay = sDateBegin.substring(0, 2);
                final String vMount = sDateBegin.substring(3, 5);
                final String vYear = sDateBegin.substring(6, 10);
                sDate = vYear + "-" + vMount + "-" + vDay;
            }
//            LogDebugString(CommonFunction.log, "ConvertDateUpdatePolicy: Date-End", sDate);
        } catch (Exception e) {
            sDate = "";
            LogExceptionServlet(CommonFunction.log, "ConvertDateUpdatePolicy: " + e.getMessage(), e);
        }
        return sDate;
    }

    public static void LoadRoleToken(String sJSON, ROLE_DATA[][] response) throws IOException {
        if (!"".equals(sJSON)) {
            ArrayList<ROLE_DATA> tempList = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            ROLE_ATTRIBUTES proParse = objectMapper.readValue(sJSON, ROLE_ATTRIBUTES.class);
            int iID = 1;
            for (ROLE_ATTRIBUTES.Attribute attribute : proParse.getAttributes()) {
                if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN)) {
                    for (int n = 0; n < attribute.getAttributes().size(); n++) {
                        ROLE_DATA tempItem = new ROLE_DATA();
                        tempItem.id = iID;
                        tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                        tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                        tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                        tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                        tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                        tempList.add(tempItem);
                        iID++;
                    }
                }
            }
            response[0] = new ROLE_DATA[tempList.size()];
            response[0] = tempList.toArray(response[0]);
        } else {
            response[0] = null;
        }
    }

    public static void LoadRoleCertificate(String sJSON, ROLE_DATA[][] response) throws IOException {
        if (!"".equals(sJSON)) {
            ArrayList<ROLE_DATA> tempList = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            ROLE_ATTRIBUTES proParse = objectMapper.readValue(sJSON, ROLE_ATTRIBUTES.class);
            int iID = 1;
            for (ROLE_ATTRIBUTES.Attribute attribute : proParse.getAttributes()) {
                if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT)) {
                    for (int n = 0; n < attribute.getAttributes().size(); n++) {
                        ROLE_DATA tempItem = new ROLE_DATA();
                        tempItem.id = iID;
                        tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                        tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                        tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                        tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                        tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                        tempList.add(tempItem);
                        iID++;
                    }
                }
            }
            response[0] = new ROLE_DATA[tempList.size()];
            response[0] = tempList.toArray(response[0]);
        } else {
            response[0] = null;
        }
    }

    public static void LoadRoleAnother(String sJSON, ROLE_DATA[][] response)
            throws IOException {
        if (!"".equals(sJSON)) {
            ArrayList<ROLE_DATA> tempList = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            ROLE_ATTRIBUTES proParse = objectMapper.readValue(sJSON, ROLE_ATTRIBUTES.class);
            int iID = 1;
            for (ROLE_ATTRIBUTES.Attribute attribute : proParse.getAttributes()) {
                if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_ANOTHER)) {
                    for (int n = 0; n < attribute.getAttributes().size(); n++) {
                        ROLE_DATA tempItem = new ROLE_DATA();
                        tempItem.id = iID;
                        tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                        tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                        tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                        tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                        tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                        tempList.add(tempItem);
                        iID++;
                    }
                }
            }
            response[0] = new ROLE_DATA[tempList.size()];
            response[0] = tempList.toArray(response[0]);
        } else {
            response[0] = null;
        }
    }

    public static void checkAddPermissionUser(String sKey, String sGroupKey, String attrType,
            ROLE_DATA[][] resDataOld, ROLE_DATA[][] resDataLast) throws IOException {
        if (resDataOld != null && resDataOld[0].length > 0) {
            resDataLast[0] = resDataOld[0];
            if (null == attrType) {
                resDataLast[0] = resDataOld[0];
            } else {
                switch (attrType) {
                    case Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT:
                        boolean isHasKey = false;
                        for (ROLE_DATA item : resDataOld[0]) {
                            if (item.name.equals(sKey) && item.attributeType.equals(sGroupKey)) {
                                isHasKey = true;
                                break;
                            }
                        }
                        if (isHasKey == false) {
                            ArrayList<ROLE_DATA> tempList;
                            tempList = new ArrayList<>();
                            tempList.addAll(Arrays.asList(resDataOld[0]));
                            ROLE_DATA rsItem = new ROLE_DATA();
                            rsItem.id = resDataOld[0].length + 1;
                            rsItem.name = Definitions.CONFIG_ROLE_PROPERTIES_CERT_BUY_MORE;
                            rsItem.remark = "Mua thêm";
                            rsItem.remarkEn = "Buy more";
                            rsItem.enabled = true;
                            rsItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST;
                            tempList.add(rsItem);
                            resDataLast[0] = new ROLE_DATA[tempList.size()];
                            resDataLast[0] = tempList.toArray(resDataLast[0]);
                        }
                        break;
                    case Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN:
                        resDataLast[0] = resDataOld[0];
                        break;
                    default:
                        resDataLast[0] = resDataOld[0];
                        break;
                }
            }
        }
    }

    public static String AddRootRoleCertificate(ROLE_DATA[][] rsToken, ROLE_DATA[][] rsCert, ROLE_DATA[][] rsAnother)
            throws IOException {
        String sJsonResult = "";
        if (rsToken[0] != null && rsCert[0] != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            ROLE_ATTRIBUTES certificationAttributes123 = new ROLE_ATTRIBUTES();
            ArrayList<ROLE_ATTRIBUTES.Attribute> tempListParse = new ArrayList<>();
            // token
            ROLE_ATTRIBUTES.Attribute attrToken = new ROLE_ATTRIBUTES.Attribute();
            attrToken.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_TOKEN);
            ArrayList<ROLE_ATTRIBUTES.Attribute> listToken_Chilren = new ArrayList<>();
            if (rsToken[0].length > 0) {
                for (ROLE_DATA rsRole1 : rsToken[0]) {
                    ROLE_ATTRIBUTES.Attribute attrToken_Chilren = new ROLE_ATTRIBUTES.Attribute();
                    attrToken_Chilren.setEnabled(rsRole1.enabled);
                    attrToken_Chilren.setName(rsRole1.name);
                    attrToken_Chilren.setAttributeType(rsRole1.attributeType);
                    attrToken_Chilren.setRemark(rsRole1.remark);
                    attrToken_Chilren.setRemarkEn(rsRole1.remarkEn);
                    listToken_Chilren.add(attrToken_Chilren);
                }
            }
            attrToken.setAttributes(listToken_Chilren);
            tempListParse.add(attrToken);
            // cert
            ROLE_ATTRIBUTES.Attribute attrCert = new ROLE_ATTRIBUTES.Attribute();
            attrCert.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT);
            ArrayList<ROLE_ATTRIBUTES.Attribute> listCert_Chilren = new ArrayList<>();
            if (rsCert[0].length > 0) {
                for (ROLE_DATA rsCert1 : rsCert[0]) {
                    ROLE_ATTRIBUTES.Attribute attrCert_Chilren = new ROLE_ATTRIBUTES.Attribute();
                    attrCert_Chilren.setEnabled(rsCert1.enabled);
                    attrCert_Chilren.setName(rsCert1.name);
                    attrCert_Chilren.setAttributeType(rsCert1.attributeType);
                    attrCert_Chilren.setRemark(rsCert1.remark);
                    attrCert_Chilren.setRemarkEn(rsCert1.remarkEn);
                    listCert_Chilren.add(attrCert_Chilren);
                }
            }
            attrCert.setAttributes(listCert_Chilren);
            tempListParse.add(attrCert);
            // another
            if (rsAnother[0] != null) {
                ROLE_ATTRIBUTES.Attribute attrAnother = new ROLE_ATTRIBUTES.Attribute();
                attrAnother.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_ANOTHER);
                ArrayList<ROLE_ATTRIBUTES.Attribute> listAnother_Chilren = new ArrayList<>();
                if (rsAnother[0].length > 0) {
                    for (ROLE_DATA rsCert1 : rsAnother[0]) {
                        ROLE_ATTRIBUTES.Attribute attrAnother_Chilren = new ROLE_ATTRIBUTES.Attribute();
                        attrAnother_Chilren.setEnabled(rsCert1.enabled);
                        attrAnother_Chilren.setName(rsCert1.name);
                        attrAnother_Chilren.setAttributeType(rsCert1.attributeType);
                        attrAnother_Chilren.setRemark(rsCert1.remark);
                        attrAnother_Chilren.setRemarkEn(rsCert1.remarkEn);
                        listAnother_Chilren.add(attrAnother_Chilren);
                    }
                }
                attrAnother.setAttributes(listAnother_Chilren);
                tempListParse.add(attrAnother);
            }
            certificationAttributes123.setAttributes(tempListParse);
            sJsonResult = objectMapper.writeValueAsString(certificationAttributes123);
        }
        return sJsonResult;
    }

    public static boolean CheckRoleFuncValid(String sTagFunction, String sAttrType, ROLE_DATA[][] req)
            throws IOException {
        boolean sValid = false;
        if (req != null) {
            if (req[0].length > 0) {
                for (ROLE_DATA req1 : req[0]) {
                    if (EscapeUtils.CheckTextNull(req1.name).equals(sTagFunction)
                            && EscapeUtils.CheckTextNull(req1.attributeType).equals(sAttrType)) {
                        sValid = req1.enabled;
                        break;
                    }
                }
            }
        }
        return sValid;
    }

    public static boolean CheckRoleFuncValidOrNot(String sTagFunction, String sAttrType, ROLE_DATA[][] req)
            throws IOException {
        boolean sValid = false;
        if (req != null) {
            if (req[0].length > 0) {
                boolean isHasRecord = false;
                for (ROLE_DATA req1 : req[0]) {
                    if (EscapeUtils.CheckTextNull(req1.name).equals(sTagFunction)
                            && EscapeUtils.CheckTextNull(req1.attributeType).equals(sAttrType)) {
                        isHasRecord = true;
                        sValid = req1.enabled;
                        break;
                    }
                }
                if (isHasRecord == false) {
                    sValid = true;
                }
            }
        }
        return sValid;
    }

    public static String GetTimeMonthSearch() {
        Calendar now = Calendar.getInstance();
        String month = String.valueOf(now.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }
        return month;
    }

    public static String GetTimeYearSearch() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    public static String generateKeystorePassword(String subjectDn) {
        X500Name subject = new X500Name(subjectDn);
        RDN[] rdn = subject.getRDNs();
        String result = "";
        boolean isSet = false;
        for (int j = 0; j < rdn.length; j++) {
            AttributeTypeAndValue[] attributeTypeAndValue = rdn[j].getTypesAndValues();
            String value = attributeTypeAndValue[0].getValue().toString();
            if (value.contains(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE) || value.contains(Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE)) {
                if (value.contains(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE)) {
                    result = value.substring(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE.length());
                } else {
                    result = value.substring(Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE.length());
                }
                isSet = true;
            } else if (value.contains(Definitions.CONFIG_PREFIX_PERSONAL_CODE) || value.contains(Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE)) {
                if (!isSet) {
                    if (value.contains(Definitions.CONFIG_PREFIX_PERSONAL_CODE)) {
                        result = value.substring(Definitions.CONFIG_PREFIX_PERSONAL_CODE.length());
                    } else {
                        result = value.substring(Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE.length());
                    }
                }
            }
        }
        if (result.equals("")) {
            result = Definitions.CONFIG_DEFAULT_KEYSTORE_PASSWORD;
        }
        return result;
    }

    public static String convertP12ToCertificate(String keystorePassword, String sP12)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
//        String keystorePassword = generateKeystorePassword(sSUBJECT)+ "@" + sID;
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(sP12)), keystorePassword.toCharArray());
        String alias = (String) ks.aliases().nextElement();
        return DatatypeConverter.printBase64Binary(((X509Certificate) ks.getCertificate(alias)).getEncoded());
    }

    public static String getKeySizeFromCSR(String req) throws Exception {
        System.out.println("getKeySizeFromCSR-CSR1: " + req);
        if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_CONTAINS)) {
            req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN, "");
        }
        if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_END_CONTAINS)) {
            req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_END, "");
        }
        if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_NEW_CONTAINS)) {
            req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_NEW, "");
        }
        if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_END_NEW_CONTAINS)) {
            req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_END_NEW, "");
        }
        if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_RENEW_CONTAINS)) {
            req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_RENEW, "");
        }
        if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_END_RENEW_CONTAINS)) {
            req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_END_RENEW, "");
        }
        System.out.println("getKeySizeFromCSR-CSR2: " + req);
        Security.addProvider(new BouncyCastleProvider());
        PKCS10CertificationRequest csr = new PKCS10CertificationRequest(DatatypeConverter.parseBase64Binary(req));
        //hash(csr.getPublicKey().getEncoded());
        RSAPublicKey rsapub = (RSAPublicKey) csr.getPublicKey();
        return String.valueOf(rsapub.getModulus().bitLength()).trim();
    }

    public String convertMoneyFromDouble(Double doublePayment) {
        String formattedString;
        if (doublePayment != 0) {
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            formattedString = decimalFormat.format(doublePayment);
        } else {
            formattedString = "0";
        }
        return formattedString;
    }

    public static boolean checkDateExpireValid(String sDateInput)
            throws ParseException {
        Date currentDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
        String strCurrentDate = df.format(currentDate);
        SimpleDateFormat sdf = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
        Date first = sdf.parse(sDateInput);
        Date second = sdf.parse(strCurrentDate);
        boolean before = (first.before(second));
        return before;
    }

    public static boolean checkDateBiggerCurrent(String sDateInput, String sPattern)
            throws ParseException {
        Date currentDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat(sPattern);
        String strCurrentDate = df.format(currentDate);
        SimpleDateFormat sdf = new SimpleDateFormat(sPattern);
        Date second = sdf.parse(sDateInput);
        Date first = sdf.parse(strCurrentDate);
        boolean before = (first.before(second));
        return before;
    }
    
    public static boolean checkDateLesserCurrent(String sDateInput, String sPattern)
            throws ParseException {
        Date currentDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat(sPattern);
        String strCurrentDate = df.format(currentDate);
        SimpleDateFormat sdf = new SimpleDateFormat(sPattern);
        Date first = sdf.parse(sDateInput);
        Date second = sdf.parse(strCurrentDate);
        boolean before = (first.before(second));
        return before;
    }

    public static boolean checkDateBiggerRequest(String sDateServer, String sDateInput, String sPattern)
            throws ParseException {
        if (!"".equals(sDateServer)) {
            SimpleDateFormat sdfFirst = new SimpleDateFormat(Definitions.CONFIG_DATETIME_FORMAT_YYYYDDMMHHMMSS);
            SimpleDateFormat sdfSecond = new SimpleDateFormat(sPattern);
            Date first = sdfFirst.parse(sDateServer);
            Date second = sdfSecond.parse(sDateInput);
            boolean before = (first.before(second));
            return before;
        } else {
            Date currentDate = new Date();
            SimpleDateFormat df = new SimpleDateFormat(sPattern);
            String strCurrentDate = df.format(currentDate);
            SimpleDateFormat sdf = new SimpleDateFormat(sPattern);
            Date second = sdf.parse(sDateInput);
            Date first = sdf.parse(strCurrentDate);
            boolean before = (first.before(second));
            return before;
        }
    }

    public static boolean checkDateBiggerContract(String sDateServer, String sDateInput, String sPattern)
            throws ParseException {
        if (!"".equals(sDateServer)) {
            SimpleDateFormat sdfFirst = new SimpleDateFormat(sPattern);
            SimpleDateFormat sdfSecond = new SimpleDateFormat(sPattern);
            Date first = sdfFirst.parse(sDateServer);
            Date second = sdfSecond.parse(sDateInput);
            SimpleDateFormat dfCommon = new SimpleDateFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
            String strCurrentFirst = dfCommon.format(first);
            String strCurrentSecond = dfCommon.format(second);
            Date secondCompare = dfCommon.parse(strCurrentSecond);
            Date firstCompare = dfCommon.parse(strCurrentFirst);
            boolean before = (firstCompare.before(secondCompare));
            return before;
        } else {
            return false;
        }
    }

    public static String convertStringDateToString(String sDateInput, String sPattern) {
        try {
            if (!"".equals(sDateInput)) {
                SimpleDateFormat sdf = new SimpleDateFormat(sPattern);
                return sdf.format(sDateInput);
            } else {
                return "";
            }
        } catch (Exception e) {
            LogDebugString(log, "convertStringDateToString", e.getMessage());
            return "";
        }

    }

    public static boolean checkDatePatternValid(String sValue, String sPattern) {
        boolean sValid = false;
        if (!"".equals(sValue)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(sPattern);
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(sValue);
                return true;
            } catch (ParseException pe) {
                return false;
            }
        }
        return sValid;
    }

    public static boolean checkDateAddDayCurrent(String sValue, int day, String sPattern) throws ParseException {
        boolean sValid = false;
        if (!"".equals(sValue)) {
            Date currentDate = new Date();
            SimpleDateFormat df = new SimpleDateFormat(sPattern);
            String strCurrentDate = df.format(currentDate);
            Date firstCheck = df.parse(strCurrentDate);
            Calendar cCheck = Calendar.getInstance();
            cCheck.setTime(firstCheck);
            cCheck.add(Calendar.DATE, day);
            Date first = cCheck.getTime();
            Date second = df.parse(sValue);
            sValid = (second.before(first));
        }
        return sValid;
    }

    public static long remainDayWithCurrentTime(String sDateInput, String sPattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(sPattern);
        Date currentDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat(sPattern);
        String strCurrentDate = df.format(currentDate);
        LocalDate firstDate = LocalDate.parse(strCurrentDate, formatter);
        LocalDate secondDate = LocalDate.parse(sDateInput, formatter);
        long days = ChronoUnit.DAYS.between(firstDate, secondDate);
        return days;
    }

    public static boolean checkExpirationWithProfile(String sExpiration, java.sql.Timestamp pEFFECTIVE_DT, int duration,
            int promotion, String sPattern) throws ParseException {
        boolean sValid;
        SimpleDateFormat df = new SimpleDateFormat(sPattern);
        Date dateExpiration = df.parse(sExpiration);
        if (pEFFECTIVE_DT != null) {
            Date currentDate = new Date(pEFFECTIVE_DT.getTime());
            String strCurrentDate = df.format(currentDate);
            Date firstCheck = df.parse(strCurrentDate);
            Calendar cCheck = Calendar.getInstance();
            cCheck.setTime(firstCheck);
            cCheck.add(Calendar.DATE, duration + promotion);
            Date dateEffective = cCheck.getTime();
            sValid = (dateExpiration.before(dateEffective));
        } else {
            Date currentDate = new Date();
            String strCurrentDate = df.format(currentDate);
            Date firstCheck = df.parse(strCurrentDate);
            Calendar cCheck = Calendar.getInstance();
            cCheck.setTime(firstCheck);
            cCheck.add(Calendar.DATE, duration + promotion);
            Date dateEffective = cCheck.getTime();
            sValid = (dateExpiration.before(dateEffective));
        }
        return sValid;
    }

    public static String encodeFileToBase64Binary(File file) throws Exception {
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStreamReader.read(bytes);
        return new String(Base64.encodeBase64(bytes), "UTF-8");
    }

    public static String GetDateCurrent() {
        DateFormat dateFormat = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String GetDateFromLong(long lDate) {
        Date date = new Date(lDate);
        SimpleDateFormat df2 = new SimpleDateFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
        return df2.format(date);
    }

    //<editor-fold defaultstate="collapsed" desc="String getMonthCurrentForSearch">
    /**
     *
     * @return
     */
    public static String getMonthCurrentForSearch() {
        Calendar now = Calendar.getInstance();
        String month = String.valueOf(now.get(Calendar.MONTH) + 1); // Note: zero based!
        if (month.length() == 1) {
            month = "0" + month;
        }
        return month;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="String getYearCurrentForSearch">
    /**
     *
     * @return
     */
    public static String getYearCurrentForSearch() {
        String result;
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR); // Note: zero based!
        result = String.valueOf(year);
        return result;
    }
    //</editor-fold>

//    String keystorePassword = TokenWSPolicy.generateKeystorePassword(certificateRequest.getSubjectDn())
//            + "@" + certificationId.intValue();
//    KeyStore ks = KeyStore.getInstance("PKCS12");
//    ks.load(new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certificate)),
//            keystorePassword.toCharArray());
//    String alias = (String) ks.aliases().nextElement();
//    certificate = DatatypeConverter.printBase64Binary(((X509Certificate) ks.getCertificate(alias)).getEncoded());
    public static InputStream getStreamFromServerFile(String sUUID, String sJRBConfig) {
        InputStream inStream = null;
        try {
            String sJRB_Source = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_SOURCE);
            if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_EFY)) {
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
                inStream = pHttpRes.getEntity().getContent();
            } else if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_JRB)) {
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
                if (jrbFile != null) {
                    inStream = jrbFile.getStream();
                }
            } else if (sJRB_Source.equals(Definitions.CONFIG_JACK_RABBIT_SOURCE_MID)) {
                String sJRB_Host = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_HOST);
                String sJRB_UserID = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USERID);
                String sJRB_UserPass = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_USER_PASSWORD);
                String sJRB_MaxSession = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAX_SESSION);
                String sJRB_MaxFileFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_MAXFILE_INFOLDER);
                String sJRB_PrefixFolder = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_PREFIX_FOLDER);
                String sJRB_WorkSpace = PropertiesContent.getPropertiesContentKey(sJRBConfig, Definitions.CONFIG_JACK_RABBIT_WORKSPACE);
                ConnectJackRabbitNew openJRB = new ConnectJackRabbitNew(sJRB_Host, sJRB_UserID, sJRB_UserPass,
                        Integer.parseInt(sJRB_MaxSession), Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
                vn.mobileid.fms.client.JCRFile jrbFile = openJRB.downloadFile(sUUID);
                inStream = jrbFile.getStream();
//                JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
//                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//                JCRFile jrbFile = JackRabbitCommon.downloadFile(jcrConfig, sUUID);
//                inStream = jrbFile.getStream();
            }
        } catch (Exception e) {
            LogExceptionServlet(log, e.getMessage(), e);
        }
        return inStream;
    }

    public static String subLastCharater(String str) {
        str = str.trim();
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String subLastCharaterSemicolon(String str) {
        str = str.trim();
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ';') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static void addCorsHeaderPassDomain(HttpServletResponse response, String sDomainAccept) {
        response.setHeader("Access-Control-Allow-Origin", sDomainAccept);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "X-Auth-Token, Content-Type");
        response.setHeader("Access-Control-Expose-Headers", "custom-header1, custom-header2");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "4800");
    }

    public static String getPKCS1Signature(String data, String relyingPartyKeyStore, String relyingPartyKeyStorePassword)
            throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        InputStream is = new FileInputStream(relyingPartyKeyStore);
        keystore.load(is, relyingPartyKeyStorePassword.toCharArray());

        Enumeration<String> e = keystore.aliases();
        String aliasName = "";
        while (e.hasMoreElements()) {
            aliasName = e.nextElement();
        }
        PrivateKey key = (PrivateKey) keystore.getKey(aliasName,
                relyingPartyKeyStorePassword.toCharArray());

        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
        sig.update(data.getBytes());
        return DatatypeConverter.printBase64Binary(sig.sign());
    }
    // hass password
    final public static String HASH_SHA1 = "SHA-1";
    final public static String HASH_SHA256 = "SHA-256";
    final public static String HASH_SHA384 = "SHA-384";
    final public static String HASH_SHA512 = "SHA-512";

    public static String hashPass(byte[] data) {
        return DatatypeConverter.printHexBinary(hashData(hashData(data, HASH_SHA384), HASH_SHA384));
    }

    public static byte[] hashData(byte[] data, String algorithm) {
        byte[] result = null;
        try {
            if (!algorithm.equals(HASH_SHA256)
                    && !algorithm.equals(HASH_SHA384)) {
                algorithm = HASH_SHA1;
            }
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            LogExceptionServlet(log, "No Such Algorithm Exception. Details: " + e.toString(), e);
            e.printStackTrace();
        }
        return result;
    }

    // check PublicKey
    public static PublicKey getPublicKeyInPemFormat(String data) {
        PublicKey pubKeyString = null;
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(DatatypeConverter.parseBase64Binary(data));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            pubKeyString = kf.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pubKeyString;
    }

    // get Beneficiary User Default Cert
    public static String getBeneficiaryUserCert(String sJSON) throws IOException {
        String sBeneficiaryUser = "";
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_BENEFICIACY_USER)) {
                sBeneficiaryUser = EscapeUtils.CheckTextNull(attribute.getName());
                break;
            }
        }
        return sBeneficiaryUser;
    }

    // get Approve CA User Default Cert
    public static String getApproveCAUserCert(String sJSON) throws IOException {
        String sValue = "";
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_APPROVE_CA_USER)) {
                sValue = EscapeUtils.CheckTextNull(attribute.getName());
                break;
            }
        }
        return sValue;
    }

    // get Approve Enabled Cert
    public static boolean getApproveEnabledCert(String sJSON) throws IOException {
        boolean sBeneficiaryUser = false;
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_APPROVED_ENABLED)) {
                String sName = EscapeUtils.CheckTextNull(attribute.getName());
                if ("true".equals(sName)) {
                    sBeneficiaryUser = true;
                }
                break;
            }
        }
        return sBeneficiaryUser;
    }

    // get Approve CA Enabled Cert of Profile
    public static boolean getApproveCAOfProfile(String sProfileName, CERTIFICATION_POLICY_DATA[][] rsCertPolicy) throws IOException {
        boolean checkValid = false;
        if (rsCertPolicy != null && rsCertPolicy[0] != null && rsCertPolicy[0].length > 0) {
            for (CERTIFICATION_POLICY_DATA rsCertItem : rsCertPolicy[0]) {
                if (rsCertItem.attributeType.trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST)) {
                    String sName = EscapeUtils.CheckTextNull(rsCertItem.name);
                    if (sName.equals(sProfileName)) {
                        checkValid = rsCertItem.approveCAEnabled;
                        break;
                    }
                }
            }
        }
        return checkValid;
    }

    // get Share Mode Enabled Cert
    public static boolean getShareModeEnabledCert(String sJSON) throws IOException {
        boolean sBeneficiaryUser = false;
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE)) {
                String sName = EscapeUtils.CheckTextNull(attribute.getName());
                if ("true".equals(sName)) {
                    sBeneficiaryUser = true;
                }
                break;
            }
        }
        return sBeneficiaryUser;
    }

    // get Push Notice Enabled Cert
    public static boolean getPushNoticeEnabledCert(String sJSON) throws IOException {
        boolean sValueEnabled = false;
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PUSH_NOTICE_ENABLED)) {
                String sName = EscapeUtils.CheckTextNull(attribute.getName());
                if ("true".equals(sName)) {
                    sValueEnabled = true;
                }
                break;
            }
        }
        return sValueEnabled;
    }

    // get P12 Email Enabled Cert
    public static boolean getP12EmailEnabledCert(String sJSON) throws IOException {
        boolean sValueEnabled = false;
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_P12_EMAIL_ENABLED)) {
                String sName = EscapeUtils.CheckTextNull(attribute.getName());
                if ("true".equals(sName)) {
                    sValueEnabled = true;
                }
                break;
            }
        }
        return sValueEnabled;
    }

    // API get Profile All Access Cert
    public static boolean checkAPIAccessProfileAll(String sJSON) throws IOException {
        boolean sBeneficiaryUser = false;
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS)) {
                if (attribute.isEnabled() == true) {
                    String sName = EscapeUtils.CheckTextNull(attribute.getName().trim());
                    if ("true".equals(sName)) {
                        sBeneficiaryUser = true;
                    }
                }
                break;
            }
        }
        return sBeneficiaryUser;
    }

    // API get IP All Access Cert
    public static boolean checkAPIAccessIPAll(String sJSON)
            throws IOException {
        boolean sValue = false;
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_IP_ALL_ACCESS)) {
                if (attribute.isEnabled() == true) {
                    String sName = EscapeUtils.CheckTextNull(attribute.getName().trim());
                    if ("true".equals(sName)) {
                        sValue = true;
                    }
                }
                break;
            }
        }
        return sValue;
    }

    // API get Function All Access Cert
    public static boolean checkAPIAccessFunctionAll(String sJSON)
            throws IOException {
        boolean sValue = false;
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_FUNCTIONALITY_ALL_ACCESS)) {
                if (attribute.isEnabled() == true) {
                    String sName = EscapeUtils.CheckTextNull(attribute.getName().trim());
                    if ("true".equals(sName)) {
                        sValue = true;
                    }
                }
                break;
            }
        }
        return sValue;
    }

    // get Approve Enabled Cert
    public static void getProfileCertList(String sJSON, CERTIFICATION_POLICY_DATA[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        int iID = 1;
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    if (attribute.getAttributes().get(n).isEnabled() == true) {
                        CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                        tempItem.id = iID;
                        tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                        tempItem.approveCAEnabled = attribute.getAttributes().get(n).isApproveCAEnabled();
                        tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                        tempItem.certificateAuthority = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificateAuthority());
                        tempItem.certificatePurpose = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificatePurpose());
                        tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                        tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                        tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                        tempList.add(tempItem);
                        iID++;
                    }
                }
            } else if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_SERVICE_TYPE_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    if (attribute.getAttributes().get(n).isEnabled() == true) {
                        CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                        tempItem.id = iID;
                        tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                        tempItem.approveCAEnabled = attribute.getAttributes().get(n).isApproveCAEnabled();
                        tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                        tempItem.certificateAuthority = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificateAuthority());
                        tempItem.certificatePurpose = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificatePurpose());
                        tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                        tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                        tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                        tempList.add(tempItem);
                        iID++;
                    }
                }
            } else if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_MAJOR_TYPE_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    if (attribute.getAttributes().get(n).isEnabled() == true) {
                        CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                        tempItem.id = iID;
                        tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                        tempItem.approveCAEnabled = attribute.getAttributes().get(n).isApproveCAEnabled();
                        tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                        tempItem.certificateAuthority = "";
                        tempItem.certificatePurpose = "";
                        tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                        tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                        tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                        tempList.add(tempItem);
                        iID++;
                    }
                }
            } else if (attribute.getAttributeType().trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS)) {
                if (attribute.isEnabled() == true) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.isEnabled();
                    tempItem.approveCAEnabled = attribute.isApproveCAEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
                    tempItem.certificateAuthority = EscapeUtils.CheckTextNull(attribute.getCertificateAuthority());
                    tempItem.certificatePurpose = EscapeUtils.CheckTextNull(attribute.getCertificatePurpose());
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributeType());
                    tempItem.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
                    tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                    tempList.add(tempItem);
                    iID++;
                }
            } else if (attribute.getAttributeType().trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE)) {
                if (attribute.isEnabled() == true) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.isEnabled();
                    tempItem.approveCAEnabled = attribute.isApproveCAEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
                    tempItem.certificateAuthority = EscapeUtils.CheckTextNull(attribute.getCertificateAuthority());
                    tempItem.certificatePurpose = EscapeUtils.CheckTextNull(attribute.getCertificatePurpose());
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributeType());
                    tempItem.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
                    tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                    tempList.add(tempItem);
                    iID++;
                }
            }
        }
        response[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // get Share Mode Cert
    public static void getShareModeCertList(String sJSON, CERTIFICATION_POLICY_DATA[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        int iID = 1;
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE)) {
                if (attribute.isEnabled() == true) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.isEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
                    tempItem.certificateAuthority = EscapeUtils.CheckTextNull(attribute.getCertificateAuthority());
                    tempItem.certificatePurpose = EscapeUtils.CheckTextNull(attribute.getCertificatePurpose());
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributeType());
                    tempItem.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
                    tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                    tempList.add(tempItem);
                    iID++;
                }
            }
        }
        response[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // get Approve Enabled New Cert
    public static void getProfileCertNewList(String sJSON, CERTIFICATION_POLICY_DATA[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        int iID = 1;
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    if (attribute.getAttributes().get(n).isEnabled() == true) {
                        CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                        tempItem.id = iID;
                        tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                        tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                        tempItem.certificateAuthority = "";// EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificateAuthority());
                        tempItem.certificatePurpose = "";//EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificatePurpose());
                        tempItem.approveCAEnabled = attribute.getAttributes().get(n).isApproveCAEnabled();
                        tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                        tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                        tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                        tempList.add(tempItem);
                        iID++;
                    }
                }
            } else if (attribute.getAttributeType().trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS)) {
                if (attribute.isEnabled() == true) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.isEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
                    tempItem.certificateAuthority = EscapeUtils.CheckTextNull(attribute.getCertificateAuthority());
                    tempItem.certificatePurpose = EscapeUtils.CheckTextNull(attribute.getCertificatePurpose());
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributeType());
                    tempItem.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
                    tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                    tempList.add(tempItem);
                    iID++;
                }
            }
        }
        response[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // get Approve Enabled New Cert for admin
    public static void getProfileCertNewListForAdmin(String sJSON, CERTIFICATION_POLICY_DATA[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        int iID = 1;
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.certificateAuthority = "";// EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificateAuthority());
                    tempItem.certificatePurpose = "";//EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificatePurpose());
                    tempItem.approveCAEnabled = attribute.getAttributes().get(n).isApproveCAEnabled();
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                    tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                    tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                    tempList.add(tempItem);
                    iID++;
                }
            } else if (attribute.getAttributeType().trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS)) {
                CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                tempItem.id = iID;
                tempItem.enabled = attribute.isEnabled();
                tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
                tempItem.certificateAuthority = EscapeUtils.CheckTextNull(attribute.getCertificateAuthority());
                tempItem.certificatePurpose = EscapeUtils.CheckTextNull(attribute.getCertificatePurpose());
                tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributeType());
                tempItem.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
                tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                tempList.add(tempItem);
                iID++;
            } else if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_SERVICE_TYPE_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.certificateAuthority = "";// EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificateAuthority());
                    tempItem.certificatePurpose = "";//EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificatePurpose());
                    tempItem.approveCAEnabled = attribute.getAttributes().get(n).isApproveCAEnabled();
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                    tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                    tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                    tempList.add(tempItem);
                    iID++;
                }
            } else if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_MAJOR_TYPE_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.certificateAuthority = "";// EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificateAuthority());
                    tempItem.certificatePurpose = "";//EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCertificatePurpose());
                    tempItem.approveCAEnabled = attribute.getAttributes().get(n).isApproveCAEnabled();
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                    tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                    tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                    tempList.add(tempItem);
                    iID++;
                }
            }
        }
        response[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // get AAll Profile Cert
    public static void getAllProfilePolicyForBranch(String sJSON, CERTIFICATION_POLICY_DATA[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        int iID = 1;
        boolean isHasSharedMode = false;
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.certificateAuthority = "";
                    tempItem.certificatePurpose = "";
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                    tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                    tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                    tempList.add(tempItem);
                    iID++;
                }
            }
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PKI_FORMFACTOR_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.certificateAuthority = "";
                    tempItem.certificatePurpose = "";
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                    tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                    tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                    tempList.add(tempItem);
                    iID++;
                }
            }
            CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
            tempItem.id = iID;
            tempItem.enabled = attribute.isEnabled();
            tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
            tempItem.certificateAuthority = EscapeUtils.CheckTextNull(attribute.getCertificateAuthority());
            tempItem.certificatePurpose = EscapeUtils.CheckTextNull(attribute.getCertificatePurpose());
            tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributeType());
            tempItem.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
            tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
            tempList.add(tempItem);
            iID++;
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE)) {
                isHasSharedMode = true;
            }
        }
        if (isHasSharedMode == false) {
            CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
            tempItem.id = iID;
            tempItem.enabled = true;
            tempItem.name = "false";
            tempItem.certificateAuthority = "";
            tempItem.certificatePurpose = "";
            tempItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE;
            tempItem.remark = "Cho phép chia sẽ CTS";
            tempItem.remarkEn = "Allow sharing of certificates";
            tempList.add(tempItem);
        }
        response[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // get IP Access List Cert
    public static void getIPAccessList(String sJSON, CERTIFICATION_POLICY_DATA[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        int iID = 1;
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_IP_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    if (attribute.getAttributes().get(n).isEnabled() == true) {
                        CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                        tempItem.id = iID;
                        tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                        tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                        tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                        tempList.add(tempItem);
                        iID++;
                    }
                }
            }
        }
        response[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // get All IP Policy Cert
    public static void getAllIPPolicyForBranch(String sJSON, CERTIFICATION_POLICY_DATA[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        int iID = 1;
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_IP_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.certificateAuthority = "";
                    tempItem.certificatePurpose = "";
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                    tempItem.remark = "";
                    tempItem.remarkEn = "";
                    tempList.add(tempItem);
                    iID++;
                }
            }
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_IP_ALL_ACCESS)) {
                CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                tempItem.id = iID;
                tempItem.enabled = attribute.isEnabled();
                tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
                tempItem.certificateAuthority = "";
                tempItem.certificatePurpose = "";
                tempItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_IP_ALL_ACCESS;
                tempItem.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
                tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                tempList.add(tempItem);
                iID++;
            }
        }
        response[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // get Function Access List Cert
    public static void getFunctionAccessList(String sJSON, CERTIFICATION_POLICY_DATA[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        int iID = 1;
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_FUNCTION_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    if (attribute.getAttributes().get(n).isEnabled() == true) {
                        CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                        tempItem.id = iID;
                        tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                        tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                        tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                        tempList.add(tempItem);
                        iID++;
                    }
                }
            }
        }
        response[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // get All Function Policy Cert
    public static void getAllFunctionPolicyForBranch(String sJSON, CERTIFICATION_POLICY_DATA[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        int iID = 1;
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_FUNCTION_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.certificateAuthority = "";
                    tempItem.certificatePurpose = "";
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                    tempItem.remark = "";
                    tempItem.remarkEn = "";
                    tempList.add(tempItem);
                    iID++;
                }
            }
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_FUNCTIONALITY_ALL_ACCESS)) {
                CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                tempItem.id = iID;
                tempItem.enabled = attribute.isEnabled();
                tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
                tempItem.certificateAuthority = "";
                tempItem.certificatePurpose = "";
                tempItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_FUNCTIONALITY_ALL_ACCESS;
                tempItem.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
                tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
                tempList.add(tempItem);
                iID++;
            }
        }
        response[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // get PKI Form Factor Cert
    public static void getPKIFormFactorCertList(String sJSON, CERTIFICATION_POLICY_DATA[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        int iID = 1;
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PKI_FORMFACTOR_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    if (attribute.getAttributes().get(n).isEnabled() == true) {
                        CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                        tempItem.id = iID;
                        tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                        tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                        tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                        tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                        tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                        tempList.add(tempItem);
                        iID++;
                    }
                }
            }
//            else if (attribute.getAttributeType().trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS)) {
//                if(attribute.isEnabled() == true)
//                {
//                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
//                    tempItem.id = iID;
//                    tempItem.enabled = attribute.isEnabled();
//                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
//                    tempItem.certificateAuthority = EscapeUtils.CheckTextNull(attribute.getCertificateAuthority());
//                    tempItem.certificatePurpose = EscapeUtils.CheckTextNull(attribute.getCertificatePurpose());
//                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributeType());
//                    tempItem.remark = EscapeUtils.CheckTextNull(attribute.getRemark());
//                    tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getRemarkEn());
//                    tempList.add(tempItem);
//                    iID++;
//                }
//            }
        }
        response[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // get All Profile Discount Rate
    public static void getAllProfileDiscountRate(String sJSON, PROFILE_DISCOUNT_RATE_DATA[][] response)
            throws IOException {
        ArrayList<PROFILE_DISCOUNT_RATE_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        PROFILE_DISCOUNT_RATE_ATTRIBUTE proParse = objectMapper.readValue(sJSON, PROFILE_DISCOUNT_RATE_ATTRIBUTE.class);
        int iID = 1;
        for (PROFILE_DISCOUNT_RATE_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROSE_ATTRIBUTE_TYPE_PROFILE_DISCOUNT_RATE_LIST)) {
                if (attribute.getAttributes().size() > 0) {
                    for (int n = 0; n < attribute.getAttributes().size(); n++) {
                        PROFILE_DISCOUNT_RATE_DATA tempItem = new PROFILE_DISCOUNT_RATE_DATA();
                        tempItem.id = iID;
                        tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                        tempItem.isMoneyType = attribute.getAttributes().get(n).getIsMoneyType();
                        tempItem.profileName = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getProfileName());
                        tempItem.profileRemark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getProfileRemark());
                        tempItem.profileRemarkEN = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getProfileRemarkEN());
                        tempItem.rosePercent = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRosePercent());
                        tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                        tempList.add(tempItem);
                        iID++;
                    }
                }
            }
        }
        response[0] = new PROFILE_DISCOUNT_RATE_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // Render Profile Policy Branch Role JSON
    public static String renderProfilePolicyForBranchRole(boolean approveCAProfileEnabled, CERTIFICATION_POLICY_DATA[] rsData)
            throws IOException {
        String sValue;
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE certificationAttributes123;
        ArrayList<CERTIFICATION_POLICY_ATTRIBUTE.Attribute> tempListParse = new ArrayList<>();
        CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrOut;
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        ArrayList<CERTIFICATION_POLICY_ATTRIBUTE.Attribute> tempListProfile;
        tempListProfile = new ArrayList<>();
        // profile list
        attrOut.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_LIST);
        attrOut.setEnabled(true);
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST)) {
                CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrProfile = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                attrProfile.setName(rsData1.name);
                attrProfile.setRemark(rsData1.remark);
                attrProfile.setRemarkEn(rsData1.remarkEn);
                attrProfile.setEnabled(rsData1.enabled);
                if (approveCAProfileEnabled == true) {
                    attrProfile.setApproveCAEnabled(rsData1.approveCAEnabled);
                } else {
                    attrProfile.setApproveCAEnabled(false);
                }
                attrProfile.setAttributeType(rsData1.attributeType);
                tempListProfile.add(attrProfile);
            }
        }
        attrOut.setAttributes(tempListProfile);
        tempListParse.add(attrOut);
        // Request Type
        tempListProfile = new ArrayList<>();
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        attrOut.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_SERVICE_TYPE_LIST);
        attrOut.setEnabled(true);
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE)) {
                CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrProfile = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                attrProfile.setName(rsData1.name);
                attrProfile.setRemark(rsData1.remark);
                attrProfile.setRemarkEn(rsData1.remarkEn);
                attrProfile.setEnabled(rsData1.enabled);
                if (approveCAProfileEnabled == true) {
                    attrProfile.setApproveCAEnabled(rsData1.approveCAEnabled);
                } else {
                    attrProfile.setApproveCAEnabled(false);
                }
                attrProfile.setAttributeType(rsData1.attributeType);
                tempListProfile.add(attrProfile);
            }
        }
        attrOut.setAttributes(tempListProfile);
        tempListParse.add(attrOut);
        // Major
        tempListProfile = new ArrayList<>();
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        attrOut.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_MAJOR_TYPE_LIST);
        attrOut.setEnabled(true);
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_MAJOR_TYPE)) {
                CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrProfile = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                attrProfile.setName(rsData1.name);
                attrProfile.setRemark(rsData1.remark);
                attrProfile.setRemarkEn(rsData1.remarkEn);
                attrProfile.setEnabled(rsData1.enabled);
                if (approveCAProfileEnabled == true) {
                    attrProfile.setApproveCAEnabled(rsData1.approveCAEnabled);
                } else {
                    attrProfile.setApproveCAEnabled(false);
                }
                attrProfile.setAttributeType(rsData1.attributeType);
                tempListProfile.add(attrProfile);
            }
        }
        attrOut.setAttributes(tempListProfile);
        tempListParse.add(attrOut);
        // PROFILE_ALL_ACCESS item
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS)) {
                if (approveCAProfileEnabled == true) {
                    attrOut.setName("false");
                } else {
                    attrOut.setName(rsData1.name);
                }
                attrOut.setRemark(rsData1.remark);
                attrOut.setRemarkEn(rsData1.remarkEn);
                attrOut.setEnabled(rsData1.enabled);
                attrOut.setAttributeType(rsData1.attributeType);
                break;
            }
        }
        tempListParse.add(attrOut);
        // GET JSON FULL
        certificationAttributes123 = new CERTIFICATION_POLICY_ATTRIBUTE();
        certificationAttributes123.setAttributes(tempListParse);
        sValue = objectMapper.writeValueAsString(certificationAttributes123);
        return sValue;
    }

    // API Render Profile Policy JSON
    public static String renderProfilePolicyAPI(CERTIFICATION_POLICY_DATA[] rsData)
            throws IOException {
        String sValue;
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE certificationAttributes123;
        ArrayList<CERTIFICATION_POLICY_ATTRIBUTE.Attribute> tempListParse = new ArrayList<>();
        CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrOut;
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        ArrayList<CERTIFICATION_POLICY_ATTRIBUTE.Attribute> tempListProfile;
        tempListProfile = new ArrayList<>();
        // profile list
        attrOut.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_LIST);
        attrOut.setEnabled(true);
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST)) {
                CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrProfile = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                attrProfile.setName(rsData1.name);
                attrProfile.setRemark(rsData1.remark);
                attrProfile.setRemarkEn(rsData1.remarkEn);
                attrProfile.setEnabled(rsData1.enabled);
                attrProfile.setAttributeType(rsData1.attributeType);
                tempListProfile.add(attrProfile);
            }
        }
        attrOut.setAttributes(tempListProfile);
        tempListParse.add(attrOut);
        // form factor list
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        tempListProfile = new ArrayList<>();
        attrOut.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PKI_FORMFACTOR_LIST);
        attrOut.setEnabled(true);
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PKI_FORMFACTOR_LIST)) {
                CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrProfile = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                attrProfile.setName(rsData1.name);
                attrProfile.setRemark(rsData1.remark);
                attrProfile.setRemarkEn(rsData1.remarkEn);
                attrProfile.setEnabled(rsData1.enabled);
                attrProfile.setAttributeType(rsData1.attributeType);
                tempListProfile.add(attrProfile);
            }
        }
        attrOut.setAttributes(tempListProfile);
        tempListParse.add(attrOut);
        // PUSH_NOTICE_ENABLED item
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PUSH_NOTICE_ENABLED)) {
                attrOut.setName(rsData1.name);
                attrOut.setRemark(rsData1.remark);
                attrOut.setRemarkEn(rsData1.remarkEn);
                attrOut.setEnabled(rsData1.enabled);
                attrOut.setAttributeType(rsData1.attributeType);
                break;
            }
        }
        tempListParse.add(attrOut);
        // P12_EMAIL_ENABLED item
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_P12_EMAIL_ENABLED)) {
                attrOut.setName(rsData1.name);
                attrOut.setRemark(rsData1.remark);
                attrOut.setRemarkEn(rsData1.remarkEn);
                attrOut.setEnabled(rsData1.enabled);
                attrOut.setAttributeType(rsData1.attributeType);
                break;
            }
        }
        tempListParse.add(attrOut);
        // PROFILE_ALL_ACCESS item
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS)) {
                attrOut.setName(rsData1.name);
                attrOut.setRemark(rsData1.remark);
                attrOut.setRemarkEn(rsData1.remarkEn);
                attrOut.setEnabled(rsData1.enabled);
                attrOut.setAttributeType(rsData1.attributeType);
                break;
            }
        }
        tempListParse.add(attrOut);
        // APPROVED_ENABLED item
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_APPROVED_ENABLED)) {
                attrOut.setName(rsData1.name);
                attrOut.setRemark(rsData1.remark);
                attrOut.setRemarkEn(rsData1.remarkEn);
                attrOut.setEnabled(rsData1.enabled);
                attrOut.setAttributeType(rsData1.attributeType);
                break;
            }
        }
        tempListParse.add(attrOut);
        // APPROVE_CA_USER item
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_APPROVE_CA_USER)) {
                attrOut.setName(rsData1.name);
                attrOut.setRemark(rsData1.remark);
                attrOut.setRemarkEn(rsData1.remarkEn);
                attrOut.setEnabled(rsData1.enabled);
                attrOut.setAttributeType(rsData1.attributeType);
                break;
            }
        }
        tempListParse.add(attrOut);
        // BENEFICIACY_USER item
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_BENEFICIACY_USER)) {
                attrOut.setName(rsData1.name);
                attrOut.setRemark(rsData1.remark);
                attrOut.setRemarkEn(rsData1.remarkEn);
                attrOut.setEnabled(rsData1.enabled);
                attrOut.setAttributeType(rsData1.attributeType);
                break;
            }
        }
        tempListParse.add(attrOut);
        // CERTIFICATION_SHARE_MODE item
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE)) {
                attrOut.setName(rsData1.name);
                attrOut.setRemark(rsData1.remark);
                attrOut.setRemarkEn(rsData1.remarkEn);
                attrOut.setEnabled(rsData1.enabled);
                attrOut.setAttributeType(rsData1.attributeType);
                break;
            }
        }
        tempListParse.add(attrOut);

        // GET JSON FULL
        certificationAttributes123 = new CERTIFICATION_POLICY_ATTRIBUTE();
        certificationAttributes123.setAttributes(tempListParse);
        sValue = objectMapper.writeValueAsString(certificationAttributes123);
        return sValue;
    }

    // API Render IP Policy JSON
    public static String renderIPPolicyAPI(CERTIFICATION_POLICY_DATA[] rsData)
            throws IOException {
        String sValue;
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE certificationAttributes123;
        ArrayList<CERTIFICATION_POLICY_ATTRIBUTE.Attribute> tempListParse = new ArrayList<>();
        CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrOut;
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        ArrayList<CERTIFICATION_POLICY_ATTRIBUTE.Attribute> tempListProfile;
        tempListProfile = new ArrayList<>();
        // profile list
        attrOut.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_IP_LIST);
        attrOut.setEnabled(true);
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_IP_LIST)) {
                CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrProfile = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                attrProfile.setName(rsData1.name);
                attrProfile.setEnabled(rsData1.enabled);
                attrProfile.setAttributeType(rsData1.attributeType);
                tempListProfile.add(attrProfile);
            }
        }
        attrOut.setAttributes(tempListProfile);
        tempListParse.add(attrOut);
        // PUSH_NOTICE_ENABLED item
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_IP_ALL_ACCESS)) {
                attrOut.setName(rsData1.name);
                attrOut.setRemark(rsData1.remark);
                attrOut.setRemarkEn(rsData1.remarkEn);
                attrOut.setEnabled(rsData1.enabled);
                attrOut.setAttributeType(rsData1.attributeType);
                break;
            }
        }
        tempListParse.add(attrOut);
        // GET JSON FULL
        certificationAttributes123 = new CERTIFICATION_POLICY_ATTRIBUTE();
        certificationAttributes123.setAttributes(tempListParse);
        sValue = objectMapper.writeValueAsString(certificationAttributes123);
        return sValue;
    }

    // API Render Function Policy JSON
    public static String renderFunctionPPolicyAPI(CERTIFICATION_POLICY_DATA[] rsData)
            throws IOException {
        String sValue;
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE certificationAttributes123;
        ArrayList<CERTIFICATION_POLICY_ATTRIBUTE.Attribute> tempListParse = new ArrayList<>();
        CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrOut;
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        ArrayList<CERTIFICATION_POLICY_ATTRIBUTE.Attribute> tempListProfile;
        tempListProfile = new ArrayList<>();
        // profile list
        attrOut.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_FUNCTION_LIST);
        attrOut.setEnabled(true);
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_FUNCTION_LIST)) {
                CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrProfile = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                attrProfile.setName(rsData1.name);
                attrProfile.setEnabled(rsData1.enabled);
                attrProfile.setAttributeType(rsData1.attributeType);
                tempListProfile.add(attrProfile);
            }
        }
        attrOut.setAttributes(tempListProfile);
        tempListParse.add(attrOut);
        // PUSH_NOTICE_ENABLED item
        attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
        for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
            if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_FUNCTIONALITY_ALL_ACCESS)) {
                attrOut.setName(rsData1.name);
                attrOut.setRemark(rsData1.remark);
                attrOut.setRemarkEn(rsData1.remarkEn);
                attrOut.setEnabled(rsData1.enabled);
                attrOut.setAttributeType(rsData1.attributeType);
                break;
            }
        }
        tempListParse.add(attrOut);
        // GET JSON FULL
        certificationAttributes123 = new CERTIFICATION_POLICY_ATTRIBUTE();
        certificationAttributes123.setAttributes(tempListParse);
        sValue = objectMapper.writeValueAsString(certificationAttributes123);
        return sValue;
    }

    // GET COMPONENT CERT TYPE
    public static void getJsonComponentForCert(String sJSON, CERTIFICATION_TYPE_COMPONENT[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_TYPE_COMPONENT> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATE_ATTRIBUTES proParse = objectMapper.readValue(sJSON, CERTIFICATE_ATTRIBUTES.class);
        for (CERTIFICATE_ATTRIBUTES.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CERTIFICATION_TYPE_COMPONENT tempItem = new CERTIFICATION_TYPE_COMPONENT();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.prefix = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getPrefix());
                    tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                    tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
//                    tempItem.require = attribute.getAttributes().get(n).isRequire();
                    tempItem.require = attribute.isRequire();
                    tempItem.commomNameType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCommomNameType());
                    tempItem.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY;
                    tempList.add(tempItem);
                }
            } else if (attribute.getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CERTIFICATION_TYPE_COMPONENT tempItem = new CERTIFICATION_TYPE_COMPONENT();
                    tempItem.name = attribute.getAttributes().get(n).getName();
                    tempItem.prefix = attribute.getAttributes().get(n).getPrefix();
                    tempItem.remark = attribute.getAttributes().get(n).getRemark();
                    tempItem.remarkEn = attribute.getAttributes().get(n).getRemarkEn();
//                    tempItem.require = attribute.getAttributes().get(n).isRequire();
                    tempItem.require = attribute.isRequire();
                    tempItem.commomNameType = attribute.getAttributes().get(n).getCommomNameType();
                    tempItem.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL;
                    tempList.add(tempItem);
                }
            } else {
                CERTIFICATION_TYPE_COMPONENT tempItem = new CERTIFICATION_TYPE_COMPONENT();
                tempItem.name = attribute.getName();
                tempItem.prefix = attribute.getPrefix();
                tempItem.remark = attribute.getRemark();
                tempItem.remarkEn = attribute.getRemarkEn();
                tempItem.require = attribute.isRequire();
                tempItem.commomNameType = attribute.getCommomNameType();
                tempItem.attributeType = attribute.getAttributeType();
                tempList.add(tempItem);
            }
        }
        if (proParse.getAttributeSans() != null) {
            if (proParse.getAttributeSans().size() > 0) {
                for (CERTIFICATE_ATTRIBUTES.AttributeSan attributeSan : proParse.getAttributeSans()) {
                    CERTIFICATION_TYPE_COMPONENT tempItem = new CERTIFICATION_TYPE_COMPONENT();
                    tempItem.name = attributeSan.getName();
                    tempItem.prefix = attributeSan.getPrefix();
                    tempItem.remark = attributeSan.getRemark();
                    tempItem.remarkEn = attributeSan.getRemarkEn();
                    tempItem.require = attributeSan.isRequire();
                    tempItem.attributeType = attributeSan.getAttributeType();
                    tempList.add(tempItem);
                }
            }
        }
        response[0] = new CERTIFICATION_TYPE_COMPONENT[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // GET COMPONENT CERT TYPE for API
    public static void getJsonAPIComponentForCert(String sJSON, CertificateComponentInfo[][] response, int sLanguage)
            throws IOException {
        ArrayList<CertificateComponentInfo> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATE_ATTRIBUTES proParse = objectMapper.readValue(sJSON, CERTIFICATE_ATTRIBUTES.class);
        for (CERTIFICATE_ATTRIBUTES.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CertificateComponentInfo tempItem = new CertificateComponentInfo();
                    tempItem.code = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.prefix = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getPrefix());
                    if (sLanguage == Definitions.CONFIG_LANGUAGE_ENGLISH) {
                        tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                    } else {
                        tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                    }
                    tempItem.requireEnabled = attribute.getAttributes().get(n).isRequire();
                    tempItem.commomNameType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCommomNameType());
                    tempItem.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY;
                    tempList.add(tempItem);
                }
            } else if (attribute.getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CertificateComponentInfo tempItem = new CertificateComponentInfo();
                    tempItem.code = attribute.getAttributes().get(n).getName();
                    tempItem.prefix = attribute.getAttributes().get(n).getPrefix();
                    if (sLanguage == Definitions.CONFIG_LANGUAGE_ENGLISH) {
                        tempItem.remarkEn = attribute.getAttributes().get(n).getRemarkEn();
                    } else {
                        tempItem.remark = attribute.getAttributes().get(n).getRemark();
                    }
                    tempItem.requireEnabled = attribute.getAttributes().get(n).isRequire();
                    tempItem.commomNameType = attribute.getAttributes().get(n).getCommomNameType();
                    tempItem.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL;
                    tempList.add(tempItem);
                }
            } else {
                CertificateComponentInfo tempItem = new CertificateComponentInfo();
                tempItem.code = attribute.getName();
                tempItem.prefix = attribute.getPrefix();
                if (sLanguage == Definitions.CONFIG_LANGUAGE_ENGLISH) {
                    tempItem.remarkEn = attribute.getRemarkEn();
                } else {
                    tempItem.remark = attribute.getRemark();
                }
                tempItem.requireEnabled = attribute.isRequire();
                tempItem.commomNameType = attribute.getCommomNameType();
                tempItem.attributeType = attribute.getAttributeType();
                tempList.add(tempItem);
            }
        }
        if (proParse.getAttributeSans() != null) {
            if (proParse.getAttributeSans().size() > 0) {
                for (CERTIFICATE_ATTRIBUTES.AttributeSan attributeSan : proParse.getAttributeSans()) {
                    CertificateComponentInfo tempItem = new CertificateComponentInfo();
                    tempItem.code = attributeSan.getName();
                    tempItem.prefix = attributeSan.getPrefix();
                    if (sLanguage == Definitions.CONFIG_LANGUAGE_ENGLISH) {
                        tempItem.remarkEn = attributeSan.getRemarkEn();
                    } else {
                        tempItem.remark = attributeSan.getRemark();
                    }
                    tempItem.requireEnabled = attributeSan.isRequire();
                    tempItem.attributeType = attributeSan.getAttributeType();
                    tempList.add(tempItem);
                }
            }
        }
        response[0] = new CertificateComponentInfo[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // GET COMPONENT CERT TYPE 2 for API
    public static void getJsonAPIComponent2ForCert(String sJSON, CertificateComponentInfo[][] response, int sLanguage)
            throws IOException {
        ArrayList<CertificateComponentInfo> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATE_ATTRIBUTES proParse = objectMapper.readValue(sJSON, CERTIFICATE_ATTRIBUTES.class);
        for (CERTIFICATE_ATTRIBUTES.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CertificateComponentInfo tempItem = new CertificateComponentInfo();
                    tempItem.code = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.prefix = convertPrefixENForAPI(EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getPrefix()), true);
                    if (sLanguage == Definitions.CONFIG_LANGUAGE_ENGLISH) {
                        tempItem.remarkEn = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemarkEn());
                    } else {
                        tempItem.remark = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getRemark());
                    }
                    tempItem.requireEnabled = attribute.getAttributes().get(n).isRequire();
                    tempItem.commomNameType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getCommomNameType());
                    tempItem.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY;
                    tempList.add(tempItem);
                }
            } else if (attribute.getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CertificateComponentInfo tempItem = new CertificateComponentInfo();
                    tempItem.code = attribute.getAttributes().get(n).getName();
                    tempItem.prefix = convertPrefixENForAPI(attribute.getAttributes().get(n).getPrefix(), true);
                    if (sLanguage == Definitions.CONFIG_LANGUAGE_ENGLISH) {
                        tempItem.remarkEn = attribute.getAttributes().get(n).getRemarkEn();
                    } else {
                        tempItem.remark = attribute.getAttributes().get(n).getRemark();
                    }
                    tempItem.requireEnabled = attribute.getAttributes().get(n).isRequire();
                    tempItem.commomNameType = attribute.getAttributes().get(n).getCommomNameType();
                    tempItem.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL;
                    tempList.add(tempItem);
                }
            } else {
                CertificateComponentInfo tempItem = new CertificateComponentInfo();
                tempItem.code = attribute.getName();
                tempItem.prefix = attribute.getPrefix();
                if (sLanguage == Definitions.CONFIG_LANGUAGE_ENGLISH) {
                    tempItem.remarkEn = attribute.getRemarkEn();
                } else {
                    tempItem.remark = attribute.getRemark();
                }
                tempItem.requireEnabled = attribute.isRequire();
                tempItem.commomNameType = attribute.getCommomNameType();
                tempItem.attributeType = attribute.getAttributeType();
                tempList.add(tempItem);
            }
        }
        if (proParse.getAttributeSans() != null) {
            if (proParse.getAttributeSans().size() > 0) {
                for (CERTIFICATE_ATTRIBUTES.AttributeSan attributeSan : proParse.getAttributeSans()) {
                    CertificateComponentInfo tempItem = new CertificateComponentInfo();
                    tempItem.code = attributeSan.getName();
                    tempItem.prefix = attributeSan.getPrefix();
                    if (sLanguage == Definitions.CONFIG_LANGUAGE_ENGLISH) {
                        tempItem.remarkEn = attributeSan.getRemarkEn();
                    } else {
                        tempItem.remark = attributeSan.getRemark();
                    }
                    tempItem.requireEnabled = attributeSan.isRequire();
                    tempItem.attributeType = attributeSan.getAttributeType();
                    tempList.add(tempItem);
                }
            }
        }
        response[0] = new CertificateComponentInfo[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    public static String convertPrefixENForAPI(String sValue, boolean isDirection) {
        if (isDirection == true) {
            if (sValue.equals(Definitions.CONFIG_PREFIX_PERSONAL_CODE)) {
                sValue = sValue.replace(Definitions.CONFIG_PREFIX_PERSONAL_CODE, Definitions.CONFIG_EN_PREFIX_PERSONAL_CODE);
            } else if (sValue.equals(Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE)) {
                sValue = sValue.replace(Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE, Definitions.CONFIG_EN_PREFIX_PERSONAL_PASSPORT_CODE);
            } else if (sValue.equals(Definitions.CONFIG_PREFIX_PERSONAL_CITIZEN_CODE)) {
                sValue = sValue.replace(Definitions.CONFIG_PREFIX_PERSONAL_CITIZEN_CODE, Definitions.CONFIG_EN_PREFIX_PERSONAL_CITIZEN_CODE);
            } else if (sValue.equals(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE)) {
                sValue = sValue.replace(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE, Definitions.CONFIG_EN_PREFIX_ENTERPRISE_TAX_CODE);
            } else if (sValue.equals(Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE)) {
                sValue = sValue.replace(Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE, Definitions.CONFIG_EN_PREFIX_ENTERPRISE_BUDGET_CODE);
            } else if (sValue.equals(Definitions.CONFIG_PREFIX_ENTERPRISE_DECISION)) {
                sValue = sValue.replace(Definitions.CONFIG_PREFIX_ENTERPRISE_DECISION, Definitions.CONFIG_EN_PREFIX_ENTERPRISE_DECISION);
            }
//            switch (sValue) {
//                case Definitions.CONFIG_PREFIX_PERSONAL_CODE:
//                    sValue = sValue.replace(Definitions.CONFIG_PREFIX_PERSONAL_CODE, Definitions.CONFIG_EN_PREFIX_PERSONAL_CODE);
//                    break;
//                case Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE:
//                    sValue = sValue.replace(Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE, Definitions.CONFIG_EN_PREFIX_PERSONAL_PASSPORT_CODE);
//                    break;
//                case Definitions.CONFIG_PREFIX_PERSONAL_CITIZEN_CODE:
//                    sValue = sValue.replace(Definitions.CONFIG_PREFIX_PERSONAL_CITIZEN_CODE, Definitions.CONFIG_EN_PREFIX_PERSONAL_CITIZEN_CODE);
//                    break;
//                case Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE:
//                    sValue = sValue.replace(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE, Definitions.CONFIG_EN_PREFIX_ENTERPRISE_TAX_CODE);
//                    break;
//                case Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE:
//                    sValue = sValue.replace(Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE, Definitions.CONFIG_EN_PREFIX_ENTERPRISE_BUDGET_CODE);
//                    break;
//                case Definitions.CONFIG_PREFIX_ENTERPRISE_DECISION:
//                    sValue = sValue.replace(Definitions.CONFIG_PREFIX_ENTERPRISE_DECISION, Definitions.CONFIG_EN_PREFIX_ENTERPRISE_DECISION);
//                    break;
//                default:
//                    break;
//            }
        } else {
            if (sValue.equals(Definitions.CONFIG_EN_PREFIX_PERSONAL_CODE)) {
                sValue = sValue.replace(Definitions.CONFIG_EN_PREFIX_PERSONAL_CODE, Definitions.CONFIG_PREFIX_PERSONAL_CODE);
            } else if (sValue.equals(Definitions.CONFIG_EN_PREFIX_PERSONAL_PASSPORT_CODE)) {
                sValue = sValue.replace(Definitions.CONFIG_EN_PREFIX_PERSONAL_PASSPORT_CODE, Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE);
            } else if (sValue.equals(Definitions.CONFIG_EN_PREFIX_PERSONAL_CITIZEN_CODE)) {
                sValue = sValue.replace(Definitions.CONFIG_EN_PREFIX_PERSONAL_CITIZEN_CODE, Definitions.CONFIG_PREFIX_PERSONAL_CITIZEN_CODE);
            } else if (sValue.equals(Definitions.CONFIG_EN_PREFIX_ENTERPRISE_TAX_CODE)) {
                sValue = sValue.replace(Definitions.CONFIG_EN_PREFIX_ENTERPRISE_TAX_CODE, Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE);
            } else if (sValue.equals(Definitions.CONFIG_EN_PREFIX_ENTERPRISE_BUDGET_CODE)) {
                sValue = sValue.replace(Definitions.CONFIG_EN_PREFIX_ENTERPRISE_BUDGET_CODE, Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE);
            } else if (sValue.equals(Definitions.CONFIG_EN_PREFIX_ENTERPRISE_DECISION)) {
                sValue = sValue.replace(Definitions.CONFIG_EN_PREFIX_ENTERPRISE_DECISION, Definitions.CONFIG_PREFIX_ENTERPRISE_DECISION);
            }
        }
        return sValue;
    }

    // GET FILE PROFILE CERT TYPE
    public static void getJsonFilePropertiesForCert(String sJSON, FILE_PROFILE_JSON.Attribute[][] response)
            throws IOException {
        ArrayList<FILE_PROFILE_JSON.Attribute> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        FILE_PROFILE_JSON proParse = objectMapper.readValue(sJSON, FILE_PROFILE_JSON.class);
        for (FILE_PROFILE_JSON.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getEnabled() == true) {
                FILE_PROFILE_JSON.Attribute tempItem = new FILE_PROFILE_JSON.Attribute();
                tempItem.setName(attribute.getName());
                tempItem.setRemark(attribute.getRemark());
                tempItem.setRemarkEn(attribute.getRemarkEn());
                tempItem.setIsRequire(attribute.getIsRequire());
                tempItem.setEnabled(attribute.getEnabled());
                tempList.add(tempItem);
            }
        }
        response[0] = new FILE_PROFILE_JSON.Attribute[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // check phone number
    public static boolean checkPhoneNumberValid(String sPhone)
            throws IOException {
        boolean checkValid = false;
        sPhone = sPhone.trim();
        //matches numbers only
        String regexNumber = "^[0-9]*$";
        checkValid = sPhone.matches(regexNumber);
        if (checkValid == true) {
            if (sPhone.length() > 20 || sPhone.length() < 4) {
                checkValid = false;
            }
        }
        return checkValid;
    }

    // check email address
    public static boolean checkEmailAddressValid(String sEmail)
            throws IOException {
        sEmail = sEmail.trim();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (sEmail == null) {
            return false;
        }
        boolean checkValid = pat.matcher(sEmail).matches();
        return checkValid;
    }

    // check number
    public static boolean checkNumberValid(String sValue)
            throws IOException {
        sValue = sValue.trim();
        String regexNumber = "^[0-9]*$";
        boolean checkValid = sValue.matches(regexNumber);
        return checkValid;
    }

    // Check Access CA For Branch
    public static boolean checkAccessCAForBranch(String sCAName, CERTIFICATION_POLICY_DATA[][] rsCertPolicy)
            throws IOException {
        boolean checkValid = false;
        if (rsCertPolicy != null && rsCertPolicy[0] != null && rsCertPolicy[0].length > 0) {
            for (CERTIFICATION_POLICY_DATA rsCertItem : rsCertPolicy[0]) {
                if (rsCertItem.certificateAuthority.trim().equals(sCAName.trim())) {
                    checkValid = true;
                    break;
                }
            }
        }
        return checkValid;
    }

    // Check Access Purpose For Branch
    public static boolean checkAccessPurposeForBranch(String sCAName, String sPurposeName,
            CERTIFICATION_POLICY_DATA[][] rsCertPolicy) throws IOException {
        boolean checkValid = false;
        if (rsCertPolicy != null && rsCertPolicy[0] != null && rsCertPolicy[0].length > 0) {
            for (CERTIFICATION_POLICY_DATA rsCertItem : rsCertPolicy[0]) {
                if (rsCertItem.certificateAuthority.trim().equals(sCAName.trim()) && rsCertItem.certificatePurpose.trim().equals(sPurposeName.trim())) {
                    checkValid = true;
                    break;
                }
            }
        }
        return checkValid;
    }

    // Check Access Profile For Branch
    public static boolean checkAccessPKIFormFactorForBranch(String sFormFactorCode, CERTIFICATION_POLICY_DATA[][] rsCertPolicy)
            throws IOException {
        boolean checkValid = false;
        if (rsCertPolicy != null && rsCertPolicy[0] != null && rsCertPolicy[0].length > 0) {
            for (CERTIFICATION_POLICY_DATA rsCertItem : rsCertPolicy[0]) {
                if (rsCertItem.name.trim().equals(sFormFactorCode.trim())) {
                    checkValid = true;
                    break;
                }
            }
        }
        return checkValid;
    }

    // Check Access Profile For Branch
    public static boolean checkAccessProfileForBranch(String sCAName, String sProfileName,
            CERTIFICATION_POLICY_DATA[][] rsCertPolicy) throws IOException {
        boolean checkValid = false;
        if (rsCertPolicy != null && rsCertPolicy[0] != null && rsCertPolicy[0].length > 0) {
            for (CERTIFICATION_POLICY_DATA rsCertItem : rsCertPolicy[0]) {
                if (rsCertItem.certificateAuthority.trim().equals(sCAName.trim()) && rsCertItem.name.trim().equals(sProfileName.trim())) {
                    checkValid = true;
                    break;
                }
            }
        }
        return checkValid;
    }

    // Check action major for agency
    public static boolean checkActionMajorForBranch(String sMajorName, CERTIFICATION_POLICY_DATA[][] rsCertPolicy)
            throws IOException {
        boolean checkValid = false;
        if (rsCertPolicy != null && rsCertPolicy[0] != null && rsCertPolicy[0].length > 0) {
            for (CERTIFICATION_POLICY_DATA rsCertItem : rsCertPolicy[0]) {
                if (rsCertItem.name.trim().equals(sMajorName.trim())) {
                    checkValid = true;
                    break;
                }
            }
        }
        return checkValid;
    }

    // get Profile All Access Cert
    public static boolean checkAccessProfileAll(CERTIFICATION_POLICY_DATA[][] rsCertPolicy) throws IOException {
        boolean checkValid = false;
        if (rsCertPolicy != null && rsCertPolicy[0] != null && rsCertPolicy[0].length > 0) {
            for (CERTIFICATION_POLICY_DATA rsCertItem : rsCertPolicy[0]) {
                if (rsCertItem.attributeType.trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_PROFILE_ALL_ACCESS)) {
                    String sName = EscapeUtils.CheckTextNull(rsCertItem.name);
                    if ("true".equals(sName)) {
                        checkValid = true;
                    }
                    break;
                }
            }
        }
        return checkValid;
    }

    // Check Allow of Request Type approve CA
    public static boolean checkApproveCAReqType(String sReqTypeName, CERTIFICATION_POLICY_DATA[][] rsCertPolicy) throws IOException {
        boolean checkValid = false;
        if (rsCertPolicy != null && rsCertPolicy[0] != null && rsCertPolicy[0].length > 0) {
            for (CERTIFICATION_POLICY_DATA rsCertItem : rsCertPolicy[0]) {
                if (rsCertItem.attributeType.trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_SERVICE_TYPE)) {
                    String sName = EscapeUtils.CheckTextNull(rsCertItem.name);
                    if (sName.equals(sReqTypeName)) {
                        if (rsCertItem.approveCAEnabled == true) {
                            checkValid = true;
                        }
                        break;
                    }
                }
            }
        }
        return checkValid;
    }

    // get Share mode Access Cert
    public static boolean checkAccessShareModeCert(CERTIFICATION_POLICY_DATA[][] rsCertPolicy) throws IOException {
        boolean checkValid = false;
        if (rsCertPolicy != null && rsCertPolicy[0] != null && rsCertPolicy[0].length > 0) {
            for (CERTIFICATION_POLICY_DATA rsCertItem : rsCertPolicy[0]) {
                if (rsCertItem.attributeType.trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_CERTIFICATION_SHARE_MODE)) {
                    String sName = EscapeUtils.CheckTextNull(rsCertItem.name.trim());
                    if ("true".equals(sName)) {
                        checkValid = true;
                    }
                    break;
                }
            }
        }
        return checkValid;
    }

    public static List<String> ConvertJsonToList(String sJSON_OLD) {
        List<String> listss = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
//            sJSON_OLD = "{\"value\":[\"0908111222\",\"0908111333\",\"thanhtv@tomicalab.com\",\"0908775901\",\"vudp@tomicalab.com\",\"0123456789\"]}";
            DisallowanceList itemParseMenu = objectMapper.readValue(sJSON_OLD, DisallowanceList.class);
            String[] str_array = itemParseMenu.getValue();
            listss = new ArrayList<String>(Arrays.asList(str_array));
        } catch (IOException e) {
            CommonFunction.LogExceptionServlet(null, "ConvertJsonToList: " + e.getMessage(), e);
        }
        return listss;
    }

    public static java.sql.Timestamp ConvertStringToTimeStamp(String yourString) {
        java.sql.Timestamp dateNext = null;
        if (!"".equals(yourString)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
                Date parsedDate = dateFormat.parse(yourString);
                dateNext = new java.sql.Timestamp(parsedDate.getTime());
            } catch (Exception e) {
                CommonFunction.LogExceptionServlet(null, "ConvertStringToTimeStamp: " + e.getMessage(), e);
            }
        }
        return dateNext;
    }

    public static java.sql.Timestamp ConvertStringToTimeStampFormat(String yourString, String sDateFormat) {
        java.sql.Timestamp dateNext = null;
        if (!"".equals(yourString)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(sDateFormat);
                Date parsedDate = dateFormat.parse(yourString);
                dateNext = new java.sql.Timestamp(parsedDate.getTime());
            } catch (Exception e) {
                CommonFunction.LogExceptionServlet(null, "ConvertStringToTimeStamp: " + e.getMessage(), e);
            }
        }
        return dateNext;
    }

    public static java.sql.Timestamp ConvertPatternToTimeStamp(String yourString, String sPattern) {
        java.sql.Timestamp dateNext = null;
        if (!"".equals(yourString)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(sPattern);
                Date parsedDate = dateFormat.parse(yourString);
                dateNext = new java.sql.Timestamp(parsedDate.getTime());
            } catch (Exception e) {
                CommonFunction.LogExceptionServlet(null, "ConvertStringToTimeStamp: " + e.getMessage(), e);
            }
        }
        return dateNext;
    }

    public static java.sql.Timestamp ConvertStringToDateDDMMYY(String yourString) {
        java.sql.Timestamp dateNext = null;
        if (!"".equals(yourString)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY);
                Date parsedDate = dateFormat.parse(yourString);
                dateNext = new java.sql.Timestamp(parsedDate.getTime());
            } catch (Exception e) {
                CommonFunction.LogExceptionServlet(null, "ConvertStringToDateDDMMYY: " + e.getMessage(), e);
            }
        }
        return dateNext;
    }

    public static String ConvertTimeStampToString(java.sql.Timestamp ts) {
        String sDate = "";
        if (ts != null) {
            Date sTOKEN_SN = new Date(ts.getTime());
            sDate = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY).format(sTOKEN_SN);
        }
        return sDate;
    }

    public static String randomPasswordP12(int intNumber) {
        String sValue;
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(intNumber);
        for (int i = 0; i < intNumber; i++) {
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index = (int) (AlphaNumericString.length() * Math.random());
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString.charAt(index));
        }
        sValue = sb.toString();
        return sValue;
    }

    public static boolean regexPhoneValid(String sValue, String sRegex) {
        boolean sValid = false;
        Pattern pattern = Pattern.compile(sRegex);
        Matcher matcher = pattern.matcher(sValue);
        if (matcher.matches()) {
            sValid = true;
        }
        return sValid;
    }

    public static boolean regexEmailValid(String sValue, String sRegex) {
        boolean sValid = false;
        Pattern pattern = Pattern.compile(sRegex);
        Matcher matcher = pattern.matcher(sValue);
        if (matcher.matches()) {
            sValid = true;
        }
        return sValid;
    }

    public static boolean compromiseDateValid(String sValue) {
        boolean sValid = false;
        if (!"".equals(sValue)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(sValue);
                return true;
            } catch (ParseException pe) {
                return false;
            }
        }
        return sValid;
    }

    public static XMLGregorianCalendar ConvertStringXMLGregorian(String strDate)
            throws ParseException, DatatypeConfigurationException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date = sdf.parse(strDate);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        XMLGregorianCalendar strCreateDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        return strCreateDate;

    }

    public static int getCAIDDefault(String sCAName, CERTIFICATION_AUTHORITY[] rsCA) {
        int intValue = 0;
        if (rsCA.length > 0) {
            for (CERTIFICATION_AUTHORITY rsCA1 : rsCA) {
                if (rsCA1.NAME.equals(sCAName)) {
                    intValue = rsCA1.ID;
                    break;
                }
            }
        }
        return intValue;
    }

    public static SessionDNSName getDNSListForSSL(String sPROPERTIES) throws IOException {
        SessionDNSName cartToken = null;
        if (!"".equals(sPROPERTIES)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String strDNSArray = "";
            CERTIFICATION_PROPERTIES_JSON itemParsePush = objectMapper.readValue(sPROPERTIES, CERTIFICATION_PROPERTIES_JSON.class);
            for (int i = 0; i < itemParsePush.getAttributes().size(); i++) {
                if (itemParsePush.getAttributes().get(i).getKey().equals(CERTIFICATION_PROPERTIES_JSON.Attribute.SUBJECT_ALT_NAMES)) {
                    strDNSArray = EscapeUtils.CheckTextNull(itemParsePush.getAttributes().get(i).getValue());
                    break;
                }
            }
            if (!"".equals(strDNSArray)) {
                cartToken = new SessionDNSName();
                if (strDNSArray.contains(";")) {
                    String[] sDNSParse = strDNSArray.split(";");
                    for (String sDNSParse1 : sDNSParse) {
                        DNS_NAME_DATA rsFILE_PROFILE = new DNS_NAME_DATA();
                        rsFILE_PROFILE.DNS_NAME = EscapeUtils.CheckTextNull(sDNSParse1.trim());
                        cartToken.AddRoleFunctionsList(rsFILE_PROFILE);
                    }
                } else {
                    DNS_NAME_DATA rsFILE_PROFILE = new DNS_NAME_DATA();
                    rsFILE_PROFILE.DNS_NAME = EscapeUtils.CheckTextNull(strDNSArray);
                    cartToken.AddRoleFunctionsList(rsFILE_PROFILE);
                }
            }
        }
        return cartToken;
    }

    public static void certListTruncatedForSystemLog(CertificateInfo[] certInfoOld, CertificateInfo[][] certInfoNew) {
        if (certInfoOld != null && certInfoOld.length > 0) {
            ArrayList<CertificateInfo> tempList = new ArrayList<>();
            for (CertificateInfo certInfoOld1 : certInfoOld) {
                CertificateInfo tempItem = new CertificateInfo();
                tempItem.taxCode = certInfoOld1.taxCode;
                tempItem.pid = certInfoOld1.pid;
                tempItem.budgetCode = certInfoOld1.budgetCode;
                tempItem.passport = certInfoOld1.passport;
                tempItem.certificate = "...";
                tempItem.effectiveTime = certInfoOld1.effectiveTime;
                tempItem.expirationTime = certInfoOld1.expirationTime;
                tempItem.certificateSN = certInfoOld1.certificateSN;
                tempItem.personalName = certInfoOld1.personalName;
                tempItem.companyName = certInfoOld1.companyName;
                tempItem.certificateDN = "...";
                tempItem.phoneContact = certInfoOld1.phoneContact;
                tempItem.emailContact = certInfoOld1.emailContact;
                tempItem.tokenSN = certInfoOld1.tokenSN;
                tempItem.domainName = certInfoOld1.domainName;
                tempItem.publicKey = "...";
                tempItem.publicKeyHash = "...";
                tempItem.certificateHash = "...";
                tempItem.certificateAuthorityCode = certInfoOld1.certificateAuthorityCode;
                tempItem.certificatePurposeCode = certInfoOld1.certificatePurposeCode;
                tempItem.certificateProfileCode = certInfoOld1.certificateProfileCode;
                tempItem.certificateStateCode = certInfoOld1.certificateStateCode;
                tempItem.coreCASubject = "...";
                tempItem.csr = "...";
                tempList.add(tempItem);
            }
            certInfoNew[0] = new CertificateInfo[tempList.size()];
            certInfoNew[0] = tempList.toArray(certInfoNew[0]);
        }
    }

    public static void certReportTruncatedForSystemLog(CertificateReportInfo[] certReportOld, CertificateReportInfo[][] certReportNew) {
        if (certReportOld != null && certReportOld.length > 0) {
            ArrayList<CertificateReportInfo> tempList = new ArrayList<>();
            for (CertificateReportInfo certInfoOld1 : certReportOld) {
                CertificateReportInfo tempItem = new CertificateReportInfo();
                tempItem.taxCode = certInfoOld1.taxCode;
                tempItem.pid = certInfoOld1.pid;
                tempItem.budgetCode = certInfoOld1.budgetCode;
                tempItem.passport = certInfoOld1.passport;
                tempItem.certificate = "...";
                tempItem.effectiveTime = certInfoOld1.effectiveTime;
                tempItem.expirationTime = certInfoOld1.expirationTime;
                tempItem.certificateSN = certInfoOld1.certificateSN;
                tempItem.personalName = certInfoOld1.personalName;
                tempItem.companyName = certInfoOld1.companyName;
                tempItem.certificateDN = "...";
                tempItem.phoneContact = certInfoOld1.phoneContact;
                tempItem.emailContact = certInfoOld1.emailContact;
                tempItem.tokenSN = certInfoOld1.tokenSN;
                tempItem.publicKey = "...";
                tempItem.publicKeyHash = "...";
                tempItem.certificateHash = "...";
                tempList.add(tempItem);
            }
            certReportNew[0] = new CertificateReportInfo[tempList.size()];
            certReportNew[0] = tempList.toArray(certReportNew[0]);
        }
    }

    public static String processDataColumnExcel(String sValue, boolean isNo) {

        sValue = sValue.trim();
        sValue = sValue.replace("'", "");
        if (isNo == true) {
            sValue = sValue.replace(".0", "");
        }
        return sValue;
    }

    // get COLLECTED BRIEF Properties
    public static void getCollectedBriefProperties(String sJSON, CERTIFICATION_POLICY_DATA[][] response)
            throws IOException {
        ArrayList<CERTIFICATION_POLICY_DATA> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        CERTIFICATION_POLICY_ATTRIBUTE proParse = objectMapper.readValue(sJSON, CERTIFICATION_POLICY_ATTRIBUTE.class);
        boolean isHasSoftCopy = false;
        int iID = 1;
        for (CERTIFICATION_POLICY_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_FILE_TYPE_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                    tempList.add(tempItem);
                    iID++;
                }
            }
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_FILE_SCAN_LIST)) {
                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                    CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                    tempItem.id = iID;
                    tempItem.enabled = attribute.getAttributes().get(n).isEnabled();
                    tempItem.name = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                    tempItem.attributeType = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getAttributeType());
                    tempList.add(tempItem);
                    iID++;
                }
            }
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_COMMENT)) {
                CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                tempItem.id = iID;
                tempItem.enabled = attribute.isEnabled();
                tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
                tempItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_COMMENT;
                tempList.add(tempItem);
                iID++;
            }
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_DATE)) {
                CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                tempItem.id = iID;
                tempItem.enabled = attribute.isEnabled();
                tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
                tempItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_DATE;
                tempList.add(tempItem);
                iID++;
            }
            if (attribute.getAttributeType().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_SOFTCOPY)) {
                isHasSoftCopy = true;
                CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
                tempItem.id = iID;
                tempItem.enabled = attribute.isEnabled();
                tempItem.name = EscapeUtils.CheckTextNull(attribute.getName());
                tempItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_SOFTCOPY;
                tempList.add(tempItem);
                iID++;
            }
        }
        if (isHasSoftCopy == false) {
            CERTIFICATION_POLICY_DATA tempItem = new CERTIFICATION_POLICY_DATA();
            tempItem.id = iID;
            tempItem.enabled = true;
            tempItem.name = "false";
            tempItem.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_SOFTCOPY;
            tempList.add(tempItem);
        }
        response[0] = new CERTIFICATION_POLICY_DATA[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    // check Brief File Type
    public static boolean checkBriefFileType(String sFileTypeName, CERTIFICATION_POLICY_DATA[][] rsCertPolicy) throws IOException {
        boolean checkValid = false;
        if (rsCertPolicy != null && rsCertPolicy[0] != null && rsCertPolicy[0].length > 0) {
            for (CERTIFICATION_POLICY_DATA rsCertItem : rsCertPolicy[0]) {
                if (rsCertItem.attributeType.trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_TYPE)) {
                    String sName = EscapeUtils.CheckTextNull(rsCertItem.name);
                    if (sName.equals(sFileTypeName)) {
                        checkValid = rsCertItem.enabled;
                        break;
                    }
                }
            }
        }
        return checkValid;
    }

    // check Brief File Scan
    public static boolean checkBriefFileScan(String sFileTypeName, CERTIFICATION_POLICY_DATA[][] rsCertPolicy) throws IOException {
        boolean checkValid = false;
        if (rsCertPolicy != null && rsCertPolicy[0] != null && rsCertPolicy[0].length > 0) {
            for (CERTIFICATION_POLICY_DATA rsCertItem : rsCertPolicy[0]) {
                if (rsCertItem.attributeType.trim().equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_SCAN)) {
                    String sName = EscapeUtils.CheckTextNull(rsCertItem.name);
                    if (sName.equals(sFileTypeName)) {
                        checkValid = rsCertItem.enabled;
                        break;
                    }
                }
            }
        }
        return checkValid;
    }

    // Render Brief File Type
    public static String renderBriefFileType(CERTIFICATION_POLICY_DATA[] rsData, String sDate, String sComment, String sSoftCopy)
            throws IOException {
        String sValue;
        if (rsData != null && rsData.length > 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            CERTIFICATION_POLICY_ATTRIBUTE certificationAttributes123;
            ArrayList<CERTIFICATION_POLICY_ATTRIBUTE.Attribute> tempListParse = new ArrayList<>();
            CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrOut;
            attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
            ArrayList<CERTIFICATION_POLICY_ATTRIBUTE.Attribute> tempListProfile;
            tempListProfile = new ArrayList<>();
            // file type list
            attrOut.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_FILE_TYPE_LIST);
            attrOut.setEnabled(true);
            for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
                if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_TYPE)) {
                    CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrProfile = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                    attrProfile.setName(rsData1.name);
                    attrProfile.setEnabled(rsData1.enabled);
                    attrProfile.setAttributeType(rsData1.attributeType);
                    tempListProfile.add(attrProfile);
                }
            }
            attrOut.setAttributes(tempListProfile);
            tempListParse.add(attrOut);
            // scan list
            attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
            tempListProfile = new ArrayList<>();
            attrOut.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_FILE_SCAN_LIST);
            attrOut.setEnabled(true);
            for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
                if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_ITEM_FILE_SCAN)) {
                    CERTIFICATION_POLICY_ATTRIBUTE.Attribute attrProfile = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                    attrProfile.setName(rsData1.name);
                    attrProfile.setEnabled(rsData1.enabled);
                    attrProfile.setAttributeType(rsData1.attributeType);
                    tempListProfile.add(attrProfile);
                }
            }
            attrOut.setAttributes(tempListProfile);
            tempListParse.add(attrOut);
            if (!"".equals(sComment)) {
                // COLLECT COMMENT item
                attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                boolean isHasRecord = false;
                for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
                    if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_COMMENT)) {
                        isHasRecord = true;
                        attrOut.setName(sComment);
                        attrOut.setEnabled(rsData1.enabled);
                        attrOut.setAttributeType(rsData1.attributeType);
                        break;
                    }
                }
                if (isHasRecord == false) {
                    isHasRecord = true;
                    attrOut.setName(sComment);
                    attrOut.setEnabled(true);
                    attrOut.setAttributeType(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_COMMENT);
                }
                if (isHasRecord == true) {
                    tempListParse.add(attrOut);
                }
            }
            // COLLECT DATE item
            if (!"".equals(sDate)) {
                attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                boolean isHasRecord = false;
                for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
                    if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_DATE)) {
                        isHasRecord = true;
                        attrOut.setName(sDate);
                        attrOut.setEnabled(rsData1.enabled);
                        attrOut.setAttributeType(rsData1.attributeType);
                        break;
                    }
                }
                if (isHasRecord == true) {
                    tempListParse.add(attrOut);
                }
            }
            // COLLECT SOFTCOPY item
            if (!"".equals(sSoftCopy)) {
                attrOut = new CERTIFICATION_POLICY_ATTRIBUTE.Attribute();
                boolean isHasRecord = false;
                for (CERTIFICATION_POLICY_DATA rsData1 : rsData) {
                    if (rsData1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_COLLECTED_COLLECT_SOFTCOPY)) {
                        isHasRecord = true;
                        attrOut.setName(sSoftCopy);
                        attrOut.setEnabled(rsData1.enabled);
                        attrOut.setAttributeType(rsData1.attributeType);
                        break;
                    }
                }
                if (isHasRecord == true) {
                    tempListParse.add(attrOut);
                }
            }
            // GET JSON FULL
            certificationAttributes123 = new CERTIFICATION_POLICY_ATTRIBUTE();
            certificationAttributes123.setAttributes(tempListParse);
            sValue = objectMapper.writeValueAsString(certificationAttributes123);
        } else {
            sValue = "";
        }
        return sValue;
    }

    // Check formFactor HardToken ?
    public static boolean checkHardTokenEnabled(String sCode) {
        boolean sCheck = false;
        if (sCode.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_HARD_TOKEN)
                || sCode.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_HARD_TOKEN_EPASS3003)
                || sCode.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_HARD_TOKEN_SAFENET)
                || sCode.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_HARD_TOKEN_SMARTCARD)) {
            sCheck = true;
        }
        return sCheck;
    }

    // Check formFactor HardToken ?
    public static boolean checkHardTokenIDEnabled(int sID) {
        boolean sCheck = false;
        if (sID == Definitions.CONFIG_PKI_FORMFACTOR_ID_HARD_TOKEN
                || sID == Definitions.CONFIG_PKI_FORMFACTOR_ID_HARD_TOKEN_SAFENET
                || sID == Definitions.CONFIG_PKI_FORMFACTOR_ID_HARD_TOKEN_SMARTCARD
                || sID == Definitions.CONFIG_PKI_FORMFACTOR_ID_HARD_TOKEN_EPASS3003) {
            sCheck = true;
        }
        return sCheck;
    }

    // Check formFactor HardToken ?
    public static boolean checkHardTokenOther(String sCode) {
        boolean sCheck = false;
        if (!sCode.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_HARD_TOKEN)
                && !sCode.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_HARD_TOKEN_EPASS3003)
                && !sCode.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_HARD_TOKEN_SAFENET)
                && !sCode.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_HARD_TOKEN_SMARTCARD)) {
            sCheck = true;
        }
        return sCheck;
    }

    // Check formFactor HardToken ?
    public static String getUUIDV4() {
        UUID uuid = UUID.randomUUID();
//        MessageDigest salt = MessageDigest.getInstance("SHA-256");
//        salt.update(uuid.toString().getBytes("UTF-8"));
//        String digest = bytesToHex(salt.digest());
        return uuid.toString();
    }

    // GET COMPONENT CERT TYPE
    public static void getJsonUserBranchDefault(String sJSON, JSON_USER_BRANCH_DEFAULT[][] response)
            throws IOException {
        ArrayList<JSON_USER_BRANCH_DEFAULT> tempList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        USER_BRANCH_DEFAULT_ATTRIBUTES proParse = objectMapper.readValue(sJSON, USER_BRANCH_DEFAULT_ATTRIBUTES.class);
        for (USER_BRANCH_DEFAULT_ATTRIBUTES.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getAttributeType().equals(Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_BRANCH_DEFAULT_INFO_TAG)) {
                JSON_USER_BRANCH_DEFAULT tempItem = new JSON_USER_BRANCH_DEFAULT();
                tempItem.MSISDN = EscapeUtils.CheckTextNull(attribute.getMSISDN());
                tempItem.EMAIL = EscapeUtils.CheckTextNull(attribute.getEMAIL());
                tempItem.REMARK = EscapeUtils.CheckTextNull(attribute.getREMARK());
                tempItem.PARENT_NAME = EscapeUtils.CheckTextNull(attribute.getPARENT_NAME());
                tempItem.BRANCH_ROLE_NAME = EscapeUtils.CheckTextNull(attribute.getBRANCH_ROLE_NAME());
                tempItem.PROVINCE_NAME = EscapeUtils.CheckTextNull(attribute.getPROVINCE_NAME());
                tempItem.ATTRIBUTE_TYPE = Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_BRANCH_DEFAULT_INFO_TAG;
                tempList.add(tempItem);
//                for (int n = 0; n < attribute.getAttributes().size(); n++) {
//                    JSON_USER_BRANCH_DEFAULT tempItem = new JSON_USER_BRANCH_DEFAULT();
//                    tempItem.MSISDN = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getMSISDN());
//                    tempItem.EMAIL = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getEMAIL());
//                    tempItem.REMARK = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getREMARK());
//                    tempItem.PARENT_NAME = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getPARENT_NAME());
//                    tempItem.BRANCH_ROLE_NAME = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getBRANCH_ROLE_NAME());
//                    tempItem.PROVINCE_NAME = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getPROVINCE_NAME());
//                    tempItem.ATTRIBUTE_TYPE = Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_BRANCH_DEFAULT_INFO_TAG;
//                    tempList.add(tempItem);
//                }
            } else if (attribute.getAttributeType().equals(Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_USER_DEFAULT_INFO_TAG)) {
                JSON_USER_BRANCH_DEFAULT tempItem = new JSON_USER_BRANCH_DEFAULT();
                tempItem.MSISDN = EscapeUtils.CheckTextNull(attribute.getMSISDN());
                tempItem.EMAIL = EscapeUtils.CheckTextNull(attribute.getEMAIL());
                tempItem.REMARK = EscapeUtils.CheckTextNull(attribute.getREMARK());
                tempItem.PARENT_NAME = EscapeUtils.CheckTextNull(attribute.getPARENT_NAME());
                tempItem.BRANCH_ROLE_NAME = EscapeUtils.CheckTextNull(attribute.getBRANCH_ROLE_NAME());
                tempItem.PROVINCE_NAME = EscapeUtils.CheckTextNull(attribute.getPROVINCE_NAME());
                tempItem.ATTRIBUTE_TYPE = Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_USER_DEFAULT_INFO_TAG;
                tempList.add(tempItem);
//                for (int n = 0; n < attribute.getAttributes().size(); n++) {
//                    JSON_USER_BRANCH_DEFAULT tempItem = new JSON_USER_BRANCH_DEFAULT();
//                    tempItem.MSISDN = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getMSISDN());
//                    tempItem.EMAIL = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getEMAIL());
//                    tempItem.REMARK = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getREMARK());
//                    tempItem.PARENT_NAME = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getPARENT_NAME());
//                    tempItem.BRANCH_ROLE_NAME = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getBRANCH_ROLE_NAME());
//                    tempItem.PROVINCE_NAME = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getPROVINCE_NAME());
//                    tempItem.ATTRIBUTE_TYPE = Definitions.CONFIG_BRANCH_ATTRIBUTE_TYPE_USER_DEFAULT_INFO_TAG;
//                    tempList.add(tempItem);
//                }
            }
        }
        response[0] = new JSON_USER_BRANCH_DEFAULT[tempList.size()];
        response[0] = tempList.toArray(response[0]);
    }

    public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {
        try {
            File fileToZip = new File(fileName);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zos.putNextEntry(zipEntry);
            final byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            zos.closeEntry();
            fis.close();
        } finally {
//            if ((new File(fileName)).exists()) {
//                (new File(fileName)).delete();
//            }
        }
    }

    public static String subStringBetweenWord(String value, String a, String b) {
        // Return a substring between the two strings.
        int posA = value.indexOf(a);
        if (posA == -1) {
            return "";
        }
        int posB = value.lastIndexOf(b);
        if (posB == -1) {
            return "";
        }
        int adjustedPosA = posA + a.length();
        if (adjustedPosA >= posB) {
            return "";
        }
        return value.substring(adjustedPosA, posB);
    }

    public static String replaceStringCharaterSpecialDN(String value, boolean isPlusCharater, boolean isSide) {
        if (value != null) {
            value = value.trim();
            if (!"".equals(value)) {
                if (isSide == true) {
                    value = value.replace("\\,", ",");
                    if (isPlusCharater == true) {
                        value = value.replace("\\+", "+");
                    }
                } else {
                    value = value.replace(",", "\\,");
                    if (isPlusCharater == true) {
                        value = value.replace("+", "\\+");
                    }
                }
            }
        } else {
            value = "";
        }
        return value;
    }

    public static ArrayList readExcelImportTokenActionXLSX(String sLinkFile)
            throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                XSSFRow myRow = (XSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                XSSFCell cellSTT = myRow.getCell(0);
                XSSFCell cellTokenSN = myRow.getCell(1);
                XSSFCell cellMST = myRow.getCell(2);
                if (CheckCellXSSFEmpty(cellSTT) == null && CheckCellXSSFEmpty(cellTokenSN) == null
                        && CheckCellXSSFEmpty(cellMST) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                    }
                    if (cellTokenSN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellTokenSN.toString()));
                    }
                    if (cellMST == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellMST.toString()));
                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            log.error("readExcelImportTokenActionXLSX: " + e.getMessage() + ".\n-----------------------------------", e);
        }
        return cellArrayLisstHolder;
    }

    public static ArrayList readExcelImportTokenActionXLS(String sLinkFile)
            throws IOException {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                ArrayList cellStoreArrayList = new ArrayList();
                HSSFCell cellSTT = myRow.getCell(0);
                HSSFCell cellTokenSN = myRow.getCell(1);
                HSSFCell cellMST = myRow.getCell(2);
                if (CheckCellHSSFEmpty(cellSTT) == null && CheckCellHSSFEmpty(cellTokenSN) == null
                        && CheckCellHSSFEmpty(cellMST) == null) {

                } else {
                    if (cellSTT == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                    }
                    if (cellTokenSN == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellTokenSN.toString()));
                    }
                    if (cellMST == null) {
                        cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                    } else {
                        cellStoreArrayList.add(CheckReplaceImport(cellMST.toString()));
                    }
                    cellArrayLisstHolder.add(cellStoreArrayList);
                }
            }
        } catch (Exception e) {
            System.out.println("readExcelImportTokenActionXLS: " + e.getMessage() + ".\n-----------------------------------");
        }
        return cellArrayLisstHolder;
    }

    public static String printConfirmReplaceImgTag(String sValue) {
        if (sValue.contains(Definitions.CONFIG_TEMPLATE_PROCESS_LOGO_FROM_BASE64)) {
            sValue = sValue.replace(Definitions.CONFIG_TEMPLATE_PROCESS_LOGO_FROM_BASE64, "");
        }
        if (sValue.contains(Definitions.CONFIG_TEMPLATE_PROCESS_LOGO_END_BASE64)) {
            sValue = sValue.replace(Definitions.CONFIG_TEMPLATE_PROCESS_LOGO_END_BASE64, "");
        }
        if (sValue.contains(Definitions.CONFIG_TEMPLATE_PROCESS_BACKGROUND_FROM_BASE64)) {
            sValue = sValue.replace(Definitions.CONFIG_TEMPLATE_PROCESS_BACKGROUND_FROM_BASE64, "");
        }
        if (sValue.contains(Definitions.CONFIG_TEMPLATE_PROCESS_BACKGROUND_END_BASE64)) {
            sValue = sValue.replace(Definitions.CONFIG_TEMPLATE_PROCESS_BACKGROUND_END_BASE64, "");
        }
        return sValue;
    }

    public static String clearUnicodeFontString(String str) {
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("đ", "d").replaceAll("Ð", "D").replaceAll("Đ", "D").replaceAll("đ", "d");
    }

    public static boolean checkUnicodeFontString(String str) {
        boolean isValid = true;
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        if (pattern.matcher(temp).find() == false) {
            if (str.contains("Ð") || str.contains("đ") || str.contains("Đ")) {
                isValid = false;
            }
        } else {
            isValid = false;
        }
        return isValid;
    }

    public static ArrayList<String> ReloadLogTimeConfig() {
        ArrayList<String> list = null;
        Config conf = new Config();
        String sValue = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_RELOADLOG_TIMESECOND).trim();
        if (!"".equals(sValue)) {
            list = new ArrayList<String>();
            list.addAll(Arrays.asList(sValue.split(",")));
        }
        return list;
    }

    public static boolean stringContainsNumber(String s) {
        return Pattern.compile("[0-9]").matcher(s).find();
    }

    public static boolean stringContainsAlphaNumeric(String s) {
        Pattern p = Pattern.compile("[a-z]");
        return p.matcher(s).find();
    }

    public static boolean stringContainsSpecial(String s) {
        Pattern p = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        return p.matcher(s).find();
    }

    public static boolean stringContainsAlphaCapital(String s) {
        Pattern p = Pattern.compile("[A-Z]");
        return p.matcher(s).find();
    }

    public static String GetMonitorServerLogString(int numOfLine, String fileName, String sPathUrl) {
        String out = "";
        try {
            if (!"".equals(fileName)) {
                String tailCmd = "tail -n " + numOfLine + " " + sPathUrl + fileName;
                Runtime rt = Runtime.getRuntime();
                Process proc = rt.exec(tailCmd);
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                String s = null;
                while ((s = stdInput.readLine()) != null) {
                    out += s + "\n";
                }
                while ((s = stdError.readLine()) != null) {
                    out += s + "\n";
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return out;
    }

    public static void ListLogTypeConfig(MonitorLogType[][] response) {
        Config conf = new Config();
        String sJson = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_MONITOR_LOGTYPE_LIST).trim();
        if (!"".equals(sJson)) {
            Gson gson = new Gson();
            ArrayList<MonitorLogType> tempList = new ArrayList<MonitorLogType>();
            JsonObject jBillData = gson.fromJson(sJson, JsonObject.class);
            JsonArray jProduct = jBillData.get("TypeList").getAsJsonArray();
            for (int i = 0; i < jProduct.size(); i++) {
                MonitorLogType tempItem = new MonitorLogType();
                JsonObject jtemp = (JsonObject) jProduct.get(i);
                tempItem.LogTypeCode = jtemp.get("TypeCode").getAsString();
                tempItem.LogTypeDesc = jtemp.get("TypeDesc").getAsString();
                tempItem.LogFileName = jtemp.get("FileName").getAsString();
                tempList.add(tempItem);
            }
            response[0] = new MonitorLogType[tempList.size()];
            response[0] = tempList.toArray(response[0]);
        }
    }

    public static String getDateDownServerLog(String sDateBegin) throws ParseException {
        String sDateEnd = "";
        if (!"".equals(sDateBegin)) {
            SimpleDateFormat simpleDate = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_DDMMYYYY);
            Date todayDate = new Date();
            String dateFormat1 = simpleDate.format(todayDate);
            Date testDate1 = simpleDate.parse(dateFormat1);
            Date testDate2 = simpleDate.parse(sDateBegin);
            if (testDate2.compareTo(testDate1) != 0) {
                String[] item = sDateBegin.split("/");
                sDateEnd = item[2] + "-" + item[1] + "-" + item[0];
            }
        }
        System.out.println("A: " + sDateEnd);
        return sDateEnd;
    }

    public static Integer findMaxList(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return Integer.MIN_VALUE;
        }
        List<Integer> sortedlist = new ArrayList<>(list);
        Collections.sort(sortedlist);
        return sortedlist.get(sortedlist.size() - 1);
    }

    public static boolean checkBranchTreeInvalidCert(int branchID, BRANCH[][] rsBranch) {
        boolean isCheck = false;
        if (rsBranch != null) {
            if (rsBranch[0].length > 0) {
                for (BRANCH item : rsBranch[0]) {
                    if (item.ID == branchID) {
                        isCheck = true;
                        break;
                    }
                }
            }
        }
        return isCheck;
    }

    public static boolean checkAgencyAccessMenuID(int menuID) {
        boolean isCheck = false;
        if (menuID != 0) {
            Config conf = new Config();
            String sValue = conf.GetPropertybyCode(Definitions.CONFIG_AGENCY_MENU_ID_LIST_ALLOW).trim();
            String[] sParse = sValue.split(",");
            for (String sParse1 : sParse) {
                if (String.valueOf(menuID).equals(sParse1.trim())) {
                    isCheck = true;
                    break;
                }
            }
        }
        return isCheck;
    }

    public static byte[] getByteFromImage(String filePath, int[] resCode) {
        byte[] response = null;
        resCode[0] = 0;
        BufferedReader bufReader = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream inputStream = new BufferedInputStream(fis);
            response = new byte[(int) file.length()];
            inputStream.read(response);
            inputStream.close();
        } catch (IOException e) {
            resCode[0] = 1;
            LogExceptionServlet(log, "getByteFromImage: " + e.getMessage(), e);
        } finally {
            try {
                bufReader.close();
            } catch (Exception ex) {

            }
        }
        return response;
    }

    public static String getPublicKeyHasrCSR(String req) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        if (!"".equals(req.trim())) {
            Security.addProvider(new BouncyCastleProvider());
//            if (req.contains("-----BEGIN CERTIFICATE REQUEST-----")
//                || req.contains("-----END CERTIFICATE REQUEST-----")) {
//                req = req.replace("-----BEGIN CERTIFICATE REQUEST-----", "");
//                req = req.replace("-----END CERTIFICATE REQUEST-----", "");
//            }
            if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_CONTAINS)) {
                req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN, "");
            }
            if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_END_CONTAINS)) {
                req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_END, "");
            }
            if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_NEW_CONTAINS)) {
                req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_NEW, "");
            }
            if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_END_NEW_CONTAINS)) {
                req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_END_NEW, "");
            }
            if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_RENEW_CONTAINS)) {
                req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_BEGIN_RENEW, "");
            }
            if (req.toUpperCase().contains(Definitions.CONFIG_WORKER_TAG_CSR_END_RENEW_CONTAINS)) {
                req = req.replace(Definitions.CONFIG_WORKER_TAG_CSR_END_RENEW, "");
            }
            PKCS10CertificationRequest csr = new PKCS10CertificationRequest(DatatypeConverter.parseBase64Binary(req));
            return DatatypeConverter.printHexBinary(hashData(csr.getPublicKey().getEncoded(), "SHA-1"));
        } else {
            return "";
        }
    }

    public static X509Certificate getX509Object(String pem) {
        X509Certificate x509 = null;
        try {
            CertificateFactory certFactoryChild = CertificateFactory.getInstance("X.509", "BC");
            InputStream inChild = new ByteArrayInputStream(getX509Der(pem));
            x509 = (X509Certificate) certFactoryChild.generateCertificate(inChild);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return x509;
    }

    private static byte[] getX509Der(String base64Str)
            throws Exception {
        byte[] binary = null;
        if (base64Str.indexOf("-----BEGIN CERTIFICATE-----") != -1) {
            binary = base64Str.getBytes();
        } else {
            binary = DatatypeConverter.parseBase64Binary(base64Str);
        }
        return binary;
    }

    private static List<X509Certificate> getX509(byte[] data) {
        List<X509Certificate> certificates = null;
        try {
            final Collection<Certificate> signerCerts
                    = CertTools.getCertsFromPEM(new ByteArrayInputStream(data));
            certificates = new ArrayList(signerCerts);
        } catch (Exception e) {
            log.warn("CA data isn't in PEM format");
            e.printStackTrace();
        }
        return certificates;
    }

    public static byte[] getP7B(List<Certificate> chain) {
        byte[] p7b = null;
        try {
            CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
            CMSProcessableByteArray msg = new CMSProcessableByteArray("signedData".getBytes());
            JcaCertStore store = new JcaCertStore(chain);
            gen.addCertificates(store);
            CMSSignedData signedData = gen.generate(msg);
            p7b = signedData.getEncoded();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return p7b;
    }

    public static List<Certificate> getChain(String base64Cert, String base64CertCA) {
        List<X509Certificate> chainCAtemp = getX509(base64CertCA.getBytes());
        List<Certificate> chainCA = new ArrayList<>();
        chainCA.add(getX509Object(base64Cert));
        chainCA.add(chainCAtemp.get(0));
        if (chainCAtemp.size() == 2) {
            chainCA.add(chainCAtemp.get(1));
        }
        //chainCA.add(chainCAtemp.get(1));
        return chainCA;
    }

    public static CredentialDataAuthen loadCredentialDataAuthen(String sJson) throws IOException {
        CredentialDataAuthen credentialAuthen = null;
        if (!"".equals(sJson)) {
            ObjectMapper objectMapper = new ObjectMapper();
            FormFactorJsonProperties jsonGroup = objectMapper.readValue(sJson, FormFactorJsonProperties.class);
            if (jsonGroup != null) {
                for (FormFactorJsonProperties.Attribute attribute : jsonGroup.getAttributes()) {
                    if (attribute.getAttributeType().equals(Definitions.CONFIG_FORMFACTOR_ATTRIBUTE_TYPE_MODE)
                            && attribute.getEnabled() == true) {
                        if (attribute.getCredentialDataAuthen() != null) {
                            if (attribute.getCredentialDataAuthen().attributeType.equals(Definitions.CONFIG_FORMFACTOR_ATTRIBUTE_CREDENTIAL_DIRECT)) {
                                credentialAuthen = new CredentialDataAuthen();
                                credentialAuthen.wsUrl = EscapeUtils.CheckTextNull(attribute.getCredentialDataAuthen().wsUrl);
                                credentialAuthen.uuid = EscapeUtils.CheckTextNull(attribute.getCredentialDataAuthen().uuid);
                                credentialAuthen.entityName = EscapeUtils.CheckTextNull(attribute.getCredentialDataAuthen().entityName);
                                credentialAuthen.relyingPartyOwner = EscapeUtils.CheckTextNull(attribute.getCredentialDataAuthen().relyingPartyOwner);
                                credentialAuthen.userId = attribute.getCredentialDataAuthen().userId;
                            }
                        }
                    }
                }
            }
        }
        return credentialAuthen;
    }

    public static String convertDateForNEAC(String sInput, long[] longTime) throws ParseException {
        if (!"".equals(sInput)) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date testCREATED_DATE = df.parse(sInput);
//            DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
            DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(dfDate.format(testCREATED_DATE));
            longTime[0] = DateHelper.getUTCTicks(testCREATED_DATE);
            return dfDate.format(testCREATED_DATE);
        } else {
            return "";
        }
    }

    public static String encodeHmacForNEAC(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

    public static String genRequestRegisterNEAC(String sSourceNEAC, String sUserIDNEAC, String sUserKeyNEAC, String sCert,
            String sCREATED_DATE, String sVALID_FROM, String sVALID_TO,
            String certSN, String sSUBJECT, String sISSUER, int sTYPE, int sCERT_SUBJECT)
            throws Exception {
        long[] longTimeCreate = new long[1];
        if (!"".equals(sCREATED_DATE)) {
            sCREATED_DATE = convertDateForNEAC(sCREATED_DATE, longTimeCreate);
        }
        long[] longTimeFrom = new long[1];
        if (!"".equals(sCREATED_DATE)) {
            sVALID_FROM = convertDateForNEAC(sVALID_FROM, longTimeFrom);
        }
        long[] longTimeTo = new long[1];
        if (!"".equals(sCREATED_DATE)) {
            sVALID_TO = convertDateForNEAC(sVALID_TO, longTimeTo);
        }
        String sVERSION = "";
        String sSIGNATURE_ALGORITHM = "";
        String sSUBJECT_ALTERNATIVE_NAME = "";
        String sAUTHORITY_KEY_IDENTIFIER = "";
        String sTHUMBPRINT = "";
        String sSIGNATURE_HASH_ALGORITHM = "";
        String sPUBLIC_KEY = "";
        String sKEY_USAGE = "";
        String[] parseCert = new String[16];
        GetFeatureCertificate2.parserCertificateComponent(sCert, parseCert);
        if ("0".equals(parseCert[0])) {
            sVERSION = parseCert[1].replace("V", "");
            sSIGNATURE_ALGORITHM = parseCert[2];
            sSIGNATURE_HASH_ALGORITHM = parseCert[3];
            sKEY_USAGE = parseCert[5];
            sAUTHORITY_KEY_IDENTIFIER = parseCert[8];
            sSUBJECT_ALTERNATIVE_NAME = parseCert[12];
            sTHUMBPRINT = parseCert[14];
            sPUBLIC_KEY = parseCert[15];
        }
        ObjectMapper objectMapper;
        if (sSourceNEAC.equals(Definitions.CONFIG_SYNCH_NEAC_SOURCE_EFY)) {
            //<editor-fold defaultstate="collapsed" desc="### EFY Process">
            EFREQUEST_DATA itemReq = new EFREQUEST_DATA();
            itemReq.apiUser = sUserIDNEAC;
            itemReq.apiKey = sUserKeyNEAC;
            itemReq.version = sVERSION;
            itemReq.serialNumber = certSN;
            itemReq.signatureAlgorithm = sSIGNATURE_ALGORITHM;
            itemReq.signatureHashAlgorithm = sSIGNATURE_HASH_ALGORITHM;
            itemReq.issuer = sISSUER;
            itemReq.validFrom = sVALID_FROM;
            itemReq.validTo = sVALID_TO;
            itemReq.subject = sSUBJECT;
            itemReq.publicKey = sPUBLIC_KEY;
            itemReq.authorityKeyIdentifier = sAUTHORITY_KEY_IDENTIFIER;
            itemReq.subjectAlternativeName = sSUBJECT_ALTERNATIVE_NAME;
            itemReq.keyUsage = sKEY_USAGE;
            itemReq.thumbprint = sTHUMBPRINT;
            itemReq.type = sTYPE;
            itemReq.certSubject = sCERT_SUBJECT;
            itemReq.createdDate = sCREATED_DATE;
            itemReq.certificateContent = sCert;
            objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(itemReq);
            //</editor-fold>
        } else if (sSourceNEAC.equals(Definitions.CONFIG_SYNCH_NEAC_SOURCE_NEAC)) {
            //<editor-fold defaultstate="collapsed" desc="### NEAC Process">
            RequestDataNEAC res = new RequestDataNEAC();
            NEREQUEST_DATA itemReqNEAC = new NEREQUEST_DATA();
            itemReqNEAC.UserID = sUserIDNEAC;
            itemReqNEAC.VERSION = sVERSION;
            itemReqNEAC.SERIAL_NUMBER = certSN;
            itemReqNEAC.SIGNATURE_ALGORITHM = sSIGNATURE_ALGORITHM;
            itemReqNEAC.SIGNATURE_HASH_ALGORITHM = sSIGNATURE_HASH_ALGORITHM;
            itemReqNEAC.ISSUER = sISSUER;
            itemReqNEAC.VALID_FROM = sVALID_FROM;
            itemReqNEAC.VALID_TO = sVALID_TO;
            itemReqNEAC.SUBJECT = sSUBJECT;
            itemReqNEAC.PUBLIC_KEY = sPUBLIC_KEY;
            itemReqNEAC.AUTHORITY_KEY_IDENTIFIER = sAUTHORITY_KEY_IDENTIFIER;
            itemReqNEAC.SUBJECT_ALTERNATIVE_NAME = sSUBJECT_ALTERNATIVE_NAME;
            itemReqNEAC.KEY_USAGE = sKEY_USAGE;
            itemReqNEAC.THUMBPRINT = sTHUMBPRINT;
            itemReqNEAC.TYPE = sTYPE;
            itemReqNEAC.CERT_SUBJECT = sCERT_SUBJECT;
            itemReqNEAC.CREATED_DATE = sCREATED_DATE;
            String sFileContent1 = "JVBERi0xLjQKJeLjz9MKMiAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvTGVuZ3RoIDU1Pj5zdHJlYW0KeJwr5HIK4TI2U7AwMFMISeEyUNA1BDP03QwVDI0UQtK4NBKTkjVDsoBSBiAJ1xCuQC4AO5YLlwplbmRzdHJlYW0KZW5kb2JqCjQgMCBvYmoKPDwvQ29udGVudHMgMiAwIFIvVHlwZS9QYWdlL1Jlc291cmNlczw8L1Byb2NTZXQgWy9QREYgL1RleHQgL0ltYWdlQiAvSW1hZ2VDIC9JbWFnZUldL0ZvbnQ8PC9GMSAxIDAgUj4+Pj4vUGFyZW50IDMgMCBSL01lZGlhQm94WzAgMCA1OTUgODQyXT4+CmVuZG9iagoxIDAgb2JqCjw8L1N1YnR5cGUvVHlwZTEvVHlwZS9Gb250L0Jhc2VGb250L0hlbHZldGljYS1PYmxpcXVlL0VuY29kaW5nL1dpbkFuc2lFbmNvZGluZz4+CmVuZG9iagozIDAgb2JqCjw8L0tpZHNbNCAwIFJdL1R5cGUvUGFnZXMvQ291bnQgMS9JVFhUKDUuMS4wKT4+CmVuZG9iago1IDAgb2JqCjw8L1R5cGUvQ2F0YWxvZy9QYWdlcyAzIDAgUj4+CmVuZG9iago2IDAgb2JqCjw8L01vZERhdGUoRDoyMDE5MTAxNjEwNTAwOSswNycwMCcpL0NyZWF0aW9uRGF0ZShEOjIwMTkxMDE2MTA1MDA5KzA3JzAwJykvUHJvZHVjZXIoaVRleHSuIDUuMS4wIKkyMDAwLTIwMTEgMVQzWFQgQlZCQSkvQXV0aG9yKE1pbGluZCkvVGl0bGUoTXkgQ29udmVydGVkIFBERik+PgplbmRvYmoKeHJlZgowIDcKMDAwMDAwMDAwMCA2NTUzNSBmIAowMDAwMDAwMjkzIDAwMDAwIG4gCjAwMDAwMDAwMTUgMDAwMDAgbiAKMDAwMDAwMDM4OSAwMDAwMCBuIAowMDAwMDAwMTM2IDAwMDAwIG4gCjAwMDAwMDA0NTIgMDAwMDAgbiAKMDAwMDAwMDQ5NyAwMDAwMCBuIAp0cmFpbGVyCjw8L0luZm8gNiAwIFIvSUQgWzw0NGI4MmQwMDhlOGFlYTVlMWM0ZWQzYWI5MTg2MzI2OT48MGQ2MWQ1MTFkYzZmNmFjNzQwZmRhNTE4YTk3OWQyYzk+XS9Sb290IDUgMCBSL1NpemUgNz4+CnN0YXJ0eHJlZgo2NzIKJSVFT0YK";
            String sFileName1 = "convert_word_pdf2.pdf";
            String sMD5File = getMD5(sFileName1 + sFileContent1);
            System.out.println("MD5File: " + sMD5File);
            String sDataSignature = "VERSION=" + sVERSION + "&SERIAL_NUMBER=" + certSN
                    + "&SIGNATURE_ALGORITHM=" + sSIGNATURE_ALGORITHM
                    + "&SIGNATURE_HASH_ALGORITHM=" + sSIGNATURE_HASH_ALGORITHM + "&ISSUER=" + sISSUER
                    + "&VALID_FROM=" + longTimeFrom[0] + "&VALID_TO=" + longTimeTo[0] + "&SUBJECT=" + sSUBJECT
                    + "&PUBLIC_KEY=" + sPUBLIC_KEY + "&AUTHORITY_KEY_IDENTIFIER=" + sAUTHORITY_KEY_IDENTIFIER
                    + "&SUBJECT_ALTERNATIVE_NAME=" + sSUBJECT_ALTERNATIVE_NAME + "&KEY_USAGE=" + sKEY_USAGE
                    + "&THUMBPRINT=" + sTHUMBPRINT + "&TYPE=" + sTYPE + "&CERT_SUBJECT=" + sCERT_SUBJECT
                    + "&CREATED_DATE=" + longTimeCreate[0] + "&MD5PdfHash=" + sMD5File + "&UserID=" + sUserIDNEAC + "&UserKey=" + sUserKeyNEAC + "";
            String encodeSignature = encodeHmacForNEAC(sUserKeyNEAC, sDataSignature);
            itemReqNEAC.Signature = encodeSignature;// "ca2d008ca7ed93a586bba9e9d9832f780a9a3d292b7ce7f64467c79e814c4e4d";

            res.certForCAModel = itemReqNEAC;
            ListPdfFileBase64[] arrayList;
            ArrayList<ListPdfFileBase64> tempList = new ArrayList<>();
            ListPdfFileBase64 item = new ListPdfFileBase64();
            item.fileName = sFileName1;
            item.fileBase64Content = sFileContent1;
            tempList.add(item);
            arrayList = new ListPdfFileBase64[tempList.size()];
            arrayList = tempList.toArray(arrayList);
            res.listPdfFileBase64 = arrayList;
//            objectMapper = new ObjectMapper();
//            JsonObject jsonObj = new JsonObject();
//            jsonObj.add("certForCAModel", new Gson().toJsonTree(objectMapper.writeValueAsString(res)));
//            jsonObj
            return new Gson().toJson(res);
            //</editor-fold>
        } else {
            return "";
        }
    }

    public static String genRequestRevokeNEAC(String sSourceNEAC, String sUserIDNEAC, String sUserKeyNEAC, String sCert,
            String sCREATED_DATE, String sVALID_FROM, String sVALID_TO,
            String certSN, String sSUBJECT, String sISSUER, int sTYPE, int sCERT_SUBJECT, File fileCert)
            throws Exception {
        long[] longTimeCreate = new long[1];
        if (!"".equals(sCREATED_DATE)) {
            sCREATED_DATE = convertDateForNEAC(sCREATED_DATE, longTimeCreate);
        }
        long[] longTimeFrom = new long[1];
        if (!"".equals(sCREATED_DATE)) {
            sVALID_FROM = convertDateForNEAC(sVALID_FROM, longTimeFrom);
        }
        long[] longTimeTo = new long[1];
        if (!"".equals(sCREATED_DATE)) {
            sVALID_TO = convertDateForNEAC(sVALID_TO, longTimeTo);
        }
        String sVERSION = "";
        String sSIGNATURE_ALGORITHM = "";
        String sSUBJECT_ALTERNATIVE_NAME = "";
        String sAUTHORITY_KEY_IDENTIFIER = "";
        String sTHUMBPRINT = "";
        String sSIGNATURE_HASH_ALGORITHM = "";
        String sPUBLIC_KEY = "";
        String sKEY_USAGE = "";
        String[] parseCert = new String[16];
        GetFeatureCertificate2.parserCertificateComponent(sCert, parseCert);
        if ("0".equals(parseCert[0])) {
            sVERSION = parseCert[1].replace("V", "");
            sSIGNATURE_ALGORITHM = parseCert[2];
            sSIGNATURE_HASH_ALGORITHM = parseCert[3];
            sKEY_USAGE = parseCert[5];
            sAUTHORITY_KEY_IDENTIFIER = parseCert[8];
            sSUBJECT_ALTERNATIVE_NAME = parseCert[12];
            sTHUMBPRINT = parseCert[14];
            sPUBLIC_KEY = parseCert[15];
        }
        ObjectMapper objectMapper;
        if (sSourceNEAC.equals(Definitions.CONFIG_SYNCH_NEAC_SOURCE_EFY)) {
            //<editor-fold defaultstate="collapsed" desc="### EFY Process">
            EFREQUEST_DATA itemReq = new EFREQUEST_DATA();
            itemReq.apiUser = sUserIDNEAC;
            itemReq.apiKey = sUserKeyNEAC;
            itemReq.version = sVERSION;
            itemReq.serialNumber = certSN;
            itemReq.signatureAlgorithm = sSIGNATURE_ALGORITHM;
            itemReq.signatureHashAlgorithm = sSIGNATURE_HASH_ALGORITHM;
            itemReq.issuer = sISSUER;
            itemReq.validFrom = sVALID_FROM;
            itemReq.validTo = sVALID_TO;
            itemReq.subject = sSUBJECT;
            itemReq.publicKey = sPUBLIC_KEY;
            itemReq.authorityKeyIdentifier = sAUTHORITY_KEY_IDENTIFIER;
            itemReq.subjectAlternativeName = sSUBJECT_ALTERNATIVE_NAME;
            itemReq.keyUsage = sKEY_USAGE;
            itemReq.thumbprint = sTHUMBPRINT;
            itemReq.type = sTYPE;
            itemReq.certSubject = sCERT_SUBJECT;
            itemReq.createdDate = sCREATED_DATE;
            itemReq.certificateContent = sCert;
            objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(itemReq);
            //</editor-fold>
        } else if (sSourceNEAC.equals(Definitions.CONFIG_SYNCH_NEAC_SOURCE_NEAC)) {
            //<editor-fold defaultstate="collapsed" desc="### NEAC Process">
//            RequestDataNEAC res = new RequestDataNEAC();
            NEREQUEST_DATA itemReqNEAC = new NEREQUEST_DATA();
            itemReqNEAC.UserID = sUserIDNEAC;
            itemReqNEAC.VERSION = sVERSION;
            itemReqNEAC.SERIAL_NUMBER = certSN;
            itemReqNEAC.SIGNATURE_ALGORITHM = sSIGNATURE_ALGORITHM;
            itemReqNEAC.SIGNATURE_HASH_ALGORITHM = sSIGNATURE_HASH_ALGORITHM;
            itemReqNEAC.ISSUER = sISSUER;
            itemReqNEAC.VALID_FROM = sVALID_FROM;
            itemReqNEAC.VALID_TO = sVALID_TO;
            itemReqNEAC.SUBJECT = sSUBJECT;
            itemReqNEAC.PUBLIC_KEY = sPUBLIC_KEY;
            itemReqNEAC.AUTHORITY_KEY_IDENTIFIER = sAUTHORITY_KEY_IDENTIFIER;
            itemReqNEAC.SUBJECT_ALTERNATIVE_NAME = sSUBJECT_ALTERNATIVE_NAME;
            itemReqNEAC.KEY_USAGE = sKEY_USAGE;
            itemReqNEAC.THUMBPRINT = sTHUMBPRINT;
            itemReqNEAC.TYPE = sTYPE;
            itemReqNEAC.CERT_SUBJECT = sCERT_SUBJECT;
            itemReqNEAC.CREATED_DATE = sCREATED_DATE;

            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            String checksumCert = CommonFunction.getFileChecksum(md5Digest, fileCert);
            String sDataSignature = "UserID=" + sUserIDNEAC + "&CertFile=" + checksumCert + "&UserKey=" + sUserKeyNEAC + "";
//            String sDataSignature = "VERSION=" + sVERSION + "&SERIAL_NUMBER=" + certSN
//                + "&SIGNATURE_ALGORITHM=" + sSIGNATURE_ALGORITHM
//                + "&SIGNATURE_HASH_ALGORITHM=" + sSIGNATURE_HASH_ALGORITHM + "&ISSUER=" + sISSUER
//                + "&VALID_FROM=" + longTimeFrom[0] + "&VALID_TO=" + longTimeTo[0] + "&SUBJECT=" + sSUBJECT
//                + "&PUBLIC_KEY=" + sPUBLIC_KEY + "&AUTHORITY_KEY_IDENTIFIER=" + sAUTHORITY_KEY_IDENTIFIER
//                + "&SUBJECT_ALTERNATIVE_NAME=" + sSUBJECT_ALTERNATIVE_NAME + "&KEY_USAGE=" + sKEY_USAGE
//                + "&THUMBPRINT=" + sTHUMBPRINT + "&TYPE=" + sTYPE + "&CERT_SUBJECT=" + sCERT_SUBJECT
//                + "&CREATED_DATE=" + longTimeCreate[0] + "&UserID=" + sUserIDNEAC + "&UserKey=" + sUserKeyNEAC + "";
            String encodeSignature = encodeHmacForNEAC(sUserKeyNEAC, sDataSignature);
            return "userId=" + sUserIDNEAC + "&signature=" + encodeSignature;
            //</editor-fold>
        } else {
            return "";
        }
    }

    public static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);
        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;
        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };
        //close the stream; We don't need it now.
        fis.close();
        //Get the hash's bytes
        byte[] bytes = digest.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //return complete hash
        return sb.toString();
    }

    public static void createFileCert(String sCERT_SN, String sCert, String[] resFile) {
        try {
            Config conf = new Config();
            String absoluteDiskPath = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
            File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
            if (!directory.exists()) {
                directory.mkdir();
            }
            String sURLFile = absoluteDiskPath + sCERT_SN + "_NEAC" + Definitions.CONFIG_FILE_EXTENDTION_CERT;
            byte[] sXML = DatatypeConverter.parseBase64Binary(sCert);
            if ((new File(sURLFile)).exists()) {
                (new File(sURLFile)).delete();
            }
            try (FileOutputStream fileOuputStream = new FileOutputStream(sURLFile)) {
                fileOuputStream.write(sXML);
            }
            resFile[0] = sCERT_SN + "_NEAC" + Definitions.CONFIG_FILE_EXTENDTION_CERT;
            resFile[1] = sURLFile;
        } catch (IOException e) {
            LogExceptionJSP("getSignatureFileNEAC", e.getMessage(), e);
            resFile[0] = "";
        }
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            return convertByteToHex1(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertByteToHex1(byte[] data) {
        BigInteger number = new BigInteger(1, data);
        String hashtext = number.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public static String convertByteToHex2(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String convertByteToHex3(byte[] data) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(0xff & data[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String getSignatureFileNEAC(String sURLFile, String sMD5File, String sMD5Cert, String sUserIDNEAC, String sUserKeyNEAC) {
        try {
//            File file = new File(sURLFile);
//            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
//            String checksum = CommonFunction.getFileChecksum(md5Digest, file);
            String sDataSignature = "UserID=" + sUserIDNEAC + "&MD5CertHash=" + sMD5Cert + "&MD5PdfHash=" + sMD5File + "&UserKey=" + sUserKeyNEAC + "";
            return encodeHmacForNEAC(sUserKeyNEAC, sDataSignature);
        } catch (Exception e) {
            LogExceptionJSP("getSignatureFileNEAC", e.getMessage(), e);
            return "";
        }
    }

    public static String replaceCharaterSpecialJson(String value, boolean isSide) {
        if (value != null) {
            value = value.trim();
            if (!"".equals(value)) {
                if (isSide == true) {
                    value = value.replace("\"", "&quot;");
                    value = value.replace("\\", "&bsol;");
                    value = value.replace("'", "&apos;");
                } else {
//                    value = value.replace("&quot;", "\"");
//                    value = value.replace("&bsol;", "\\");
                }
            }
        } else {
            value = "";
        }
        return value;
    }

    public static String replaceCharaterSpecialJsonCSV(String value) {
        if (value != null) {
            value = value.trim();
            if (!"".equals(value)) {
                value = value.replace("&quot;", "\"");
                value = value.replace("&bsol;", "\\");
            }
        } else {
            value = "";
        }
        return value;
    }

    public static String getNumericalOrderStore(String storeName, int numberOrder) {
        String sCall = "";
        if (numberOrder == 1) {
            sCall = "{ call " + storeName + "(?) }";
        } else {
            if (numberOrder > 1) {
                String sOrder = "?";
                for (int i = 1; i < numberOrder; i++) {
                    sOrder = sOrder + ",?";
                }
                sCall = "{ call " + storeName + "(" + sOrder + ") }";
            }
        }
        return sCall;
    }

    public static void subDateTimeDetailDay(String yourString, String[] returnDetail) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
            Date currentDate = dateFormat.parse(yourString);
            returnDetail[0] = new SimpleDateFormat("dd").format(currentDate);
            returnDetail[1] = new SimpleDateFormat("MM").format(currentDate);
            returnDetail[2] = new SimpleDateFormat("yyyy").format(currentDate);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static String getKeyCertConfirm(String sCERTIFICATION_ID, int expireActiveSignServer, String sLanguage) throws Exception {
        String sResult = "";
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, expireActiveSignServer);
        Date dateOne = c.getTime();
        long sTime = dateOne.getTime();
        sResult = sCERTIFICATION_ID + "#" + String.valueOf(sTime) + "#" + sLanguage;
        DESEncryption clsEnrypt = new DESEncryption();
        return clsEnrypt.encrypt(sResult);
    }
    
    public static BRANCH[][] cloneBranchAddAllOption(BRANCH[][] rsBranch, String loginLanguage)
    {
        ConfigLanguage confLanguage = new ConfigLanguage();
        BRANCH[][] rsNew = new BRANCH[1][];
        BRANCH itemBranch = new BRANCH();
        itemBranch.ID = 0;
        itemBranch.PARENT_ID = 0;
        itemBranch.LEVEL_ID = 0;
        itemBranch.NAME = Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL;
        itemBranch.REMARK = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_COMBOBOX_OPTION_ALL, loginLanguage);
        
        ArrayList<BRANCH> tempList = new ArrayList<>();
        tempList.add(itemBranch);
        if(rsBranch != null && rsBranch[0].length > 0) {
            tempList.addAll(Arrays.asList(rsBranch[0]));
        }
        rsNew[0] = new BRANCH[tempList.size()];
        rsNew[0] = tempList.toArray(rsNew[0]);
        return rsNew;
    }
    
    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
