/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.ra.object.ATTRIBUTE_DATA;
import vn.ra.object.ATTRIBUTE_VALUES;
import vn.ra.object.BACKOFFICE_USER;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATE_ATTRIBUTES;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_AUTHORITY;
import vn.ra.object.CERTIFICATION_DATA_ATTR;
import vn.ra.object.CERTIFICATION_OWNER;
import vn.ra.object.CERTIFICATION_OWNER_DATA_ATTR;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_PROFILE;
import vn.ra.object.CERTIFICATION_PROPERTIES_JSON;
import vn.ra.object.CERTIFICATION_PURPOSE;
import vn.ra.object.CITY_PROVINCE;
import vn.ra.object.CertificateAttributesRadio;
import vn.ra.object.CertificateComponentInfo;
import vn.ra.object.CertificateInfo;
import vn.ra.object.CertificateProfileInfo;
import vn.ra.object.CityProvinceInfo;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.ProObj;
import vn.ra.object.RESPONSE_CODE;
import vn.ra.object.ROLE_DATA;
import vn.ra.object.UserInfo;
import vn.ra.object.CERTIFICATION_COMMENT;
import vn.ra.process.CommonFunction;
import static vn.ra.process.CommonFunction.CheckCellHSSFEmpty;
import static vn.ra.process.CommonFunction.CheckCellXSSFEmpty;
import static vn.ra.process.CommonFunction.CheckReplaceImport;
import vn.ra.process.CommonReferServlet;
import vn.ra.process.ConnectConnector;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.ConnectDbPhaseTwo;
import vn.ra.utility.Config;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.PropertiesContent;

/**
 *
 * @author THANH-PC
 */
public class RegisterCertificateImport extends HttpServlet {
    private static final long serialVersionUID = 6106269076155338045L;
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RegisterCertificateImport.class.getName());
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
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            ConnectDatabase db = new ConnectDatabase();
            HttpSession sessionsa = request.getSession(false);
            String strView = "";
            InputStream thanhtv = null;
            int isXLSX = 0;
            String fileUploaded = "";
            boolean strAlert = false;
            try {
                int sOutInner;
                if (sessionsa.getAttribute("sUserID") != null || sessionsa.getAttribute("sesSessKey") != null) {
                    String strInnerUsername = sessionsa.getAttribute("sUserID").toString().trim();
                    String strInnerSessionKey = sessionsa.getAttribute("sesSessKey").toString().trim();
                    sOutInner = db.CheckIsLoginOnline(strInnerUsername, strInnerSessionKey);
                } else {
                    sOutInner = 2;
                }
                if (sOutInner == 1) {
                    String userLoginID = request.getSession(false).getAttribute("UserID").toString().trim();
                    String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                    String SessUserAgentID = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SessUserAgentID").toString().trim());
                    String USER_LOG = EscapeUtils.CheckTextNull(request.getSession(false).getAttribute("sUserID").toString().trim());
                    String loginFullname = request.getSession(false).getAttribute("sesFullname").toString().trim();
                    String sessLanguageGlobal = request.getSession(false).getAttribute("sessVN").toString();
                    String AGENT_ID_LOG = EscapeUtils.CheckTextNull(request.getSession(false).getAttribute("SessAgentID").toString().trim());
                    String sIP_Request = CommonFunction.getClientIpLogin(request);
                    int[] System_Log_ID = new int[1];
                    String[] sysLog_BillCode = new String[1];
                    System_Log_ID[0] = 0;
                    String contentType = request.getContentType();
                    if ((contentType != null) && (contentType.contains("multipart/form-data"))) {
                        Boolean bCheckfile = true;
                        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                        if (isMultipart) {
                            FileItemFactory factory = new DiskFileItemFactory();
                            ServletFileUpload upload = new ServletFileUpload(factory);
                            List items = upload.parseRequest(request);
                            Iterator iterator = items.iterator();
                            while (iterator.hasNext()) {
                                FileItem item = (FileItem) iterator.next();
                                if (!item.isFormField()) {
                                    String fileName = item.getName();
                                    strAlert = CommonFunction.checkFileSpecial(fileName);
                                    if (strAlert == true) {
                                        String sExtendFile = CommonFunction.getExtendFile(fileName);
                                        fileName = CommonFunction.generateNumberDay() + "." + sExtendFile;
                                        String root = getServletContext().getRealPath("/");
                                        File path = new File(root + "/uploads");
                                        if (!path.exists()) {
                                            boolean status = path.mkdirs();
                                        }
                                        File uploadedFile = new File(path + "/" + fileName);
                                        fileUploaded = path + "/" + fileName;
                                        isXLSX = fileName.lastIndexOf(".xlsx");
                                        bCheckfile = CommonFunction.checkExtendFIle(fileName);
                                        item.write(uploadedFile);
                                    }
                                }
                            }
                        }
                        if (strAlert == false) {
                            strView = "1#" + Definitions.CONFIG_EXCEPTION_STRING_ERROR_SPECIAL;
                        } else {
                            if (bCheckfile == true) {
                                //<editor-fold defaultstate="collapsed" desc="### POLICY GET">
                                String sRegexPolicy = "";
                                String sCountExcelRequire = "50";
                                int intOTPNumn = 8;
                                String pPUSH_NOTICE_ENABLED = "1";
                                String sDiscountRateOption = "0";
                                String sNoAllowTranferToken = "1";
                                GENERAL_POLICY[][] sessGeneralPolicyRECORD = (GENERAL_POLICY[][]) sessionsa.getAttribute("sessGeneralPolicy_System");
                                if (sessGeneralPolicyRECORD[0].length > 0) {
                                    for (GENERAL_POLICY rsPolicy1 : sessGeneralPolicyRECORD[0]) {
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_REGEX_FOR_PHONE_EMAIL))
                                        {
                                            sRegexPolicy = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        }
                                        if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_RECORD_EXCEL_IMPORT_CERTIFICATION))
                                        {
                                            sCountExcelRequire = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        }
                                        if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_FO_DEFAULT_ACTIVATION_CODE_LENGTH))
                                        {
                                            intOTPNumn = Integer.parseInt(rsPolicy1.VALUE);
                                        }
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DEFAULT_CERTIFICATION_PUSH_NOTICE_ENABLED))
                                        {
                                            pPUSH_NOTICE_ENABLED = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        }
                                        if (rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_DISCOUNT_RATE_PROFILE_OPTION)) {
                                            sDiscountRateOption = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        }
                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_SYS_NO_AUTO_MOVE_TOKEN_FOR_RENEWAL_REVISION_CERTIFICATE_REQUEST)) {
                                            sNoAllowTranferToken = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                        }
                                    }
                                }
                                //</editor-fold>
                                
                                String sREGEX_PHONE = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_PHONE);
                                if("".equals(sREGEX_PHONE.trim()))
                                {
                                    sREGEX_PHONE = Definitions.CONFIG_DEFAULT_VALUE_REGEX_PHONE;
                                }
                                String sREGEX_EMAIL = PropertiesContent.getPropertiesContentKey(sRegexPolicy, Definitions.CONFIG_REGEX_EMAIL);
                                if("".equals(sREGEX_EMAIL.trim()))
                                {
                                    sREGEX_EMAIL = Definitions.CONFIG_DEFAULT_VALUE_REGEX_EMAIL;
                                }
//                                if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                String sCERTIFICATION_TYPEParam = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SESS_CERTIMPORT_CERTIFICATION_TYPE").toString().trim());
                                ArrayList dataHolder;
                                if (isXLSX > 0) {
                                    dataHolder = readExcelCertAllXLSX(fileUploaded, Integer.parseInt(sCERTIFICATION_TYPEParam));
                                } else {
                                    dataHolder = readExcelCertAllXLS(fileUploaded, Integer.parseInt(sCERTIFICATION_TYPEParam));
                                }
                                if(dataHolder.size() > 0 && dataHolder.size() <= Integer.parseInt(sCountExcelRequire) + 1)
                                {
                                    //<editor-fold defaultstate="collapsed" desc="### COLUMN NAME AND DECLARE">
                                    String sColumnSTT = "No";
                                    String sColumnCertificateType = "CertificateType";
                                    String sColumnCertificateSN = "CertificateSN";
                                    String sColumnCertificateAuthority = "CertificateAuthority";
                                    String sColumnMethod = "Method";
                                    String sColumnPersonalName = "PersonalName";
                                    String sColumnCompanyName = "CompanyName";
                                    String sColumnDomainName = "DomainName";
                                    String sColumnOrganization = "Organization";
                                    String sColumnPrefixUIDEnterprise = "PrefixUIDEnterprise";
                                    String sColumnUIDEnterprise = "UIDEnterprise";
                                    String sColumnPrefixUIDPersonal = "PrefixUIDPersonal";
                                    String sColumnUIDPersonal = "UIDPersonal";
                                    String sColumnOrganizationUnit = "OrganizationUnit";
                                    String sColumnTitle = "Title";
                                    String sColumnEmailAddress = "EmailAddress";
                                    String sColumnTelephoneNumber = "TelephoneNumber";
                                    String sColumnLocality = "Locality";
                                    String sColumnStateProvince = "StateProvince";
                                    String sColumnCountry = "Country";
                                    String sColumnCertificateProfile = "CertificateProfile";
                                    String sColumnCustomerPhoneNumer = "CustomerPhoneNumer";
                                    String sColumnCustomerEmail = "CustomerEmail";
                                    String sColumnBeneficiaryUser = "BeneficiaryUser";
                                    String sColumnBackupKeyEnabled = "BackupKeyEnabled";
                                    String sColumnRevokeOldCertificateEnabled = "RevokeOldCertificateEnabled";
                                    String sColumnChangeKeyEnabled = "ChangeKeyEnabled";
                                    String sColumnDeleteCertificateEnabled = "DeleteCertificateEnabled";
                                    String sColumnKeepCertificateSNEnabled = "KeepCertificateSNEnabled";
                                    String sColumnCSR = "CSR";
                                    String sColumnOrganizationUnit2 = "OrganizationUnit2";
                                    String sColumnOrganizationUnit3 = "OrganizationUnit3";
                                    String sColumnOrganizationUnit4 = "OrganizationUnit4";
                                    String sColumnEmailSANAddress = "EmailSANAddress";
                                    String sColumnDNSName1 = "DNSName1";
                                    String sColumnDNSName2 = "DNSName2";
                                    String sColumnDNSName3 = "DNSName3";
                                    String sColumnDNSName4 = "DNSName4";
                                    String sColumnIPAddress1 = "IPAddress1";
                                    String sColumnIPAddress2 = "IPAddress2";
                                    String sColumnIPAddress3 = "IPAddress3";
                                    int success = 0;
                                    int failed = 0;
                                    int failedCERT_PEDDING = 0;
                                    int failedFAILED_INSERT = 0;
                                    int failedNOT_PROFILE = 0;
                                    int failedBENEFICIARYUSER_INVALID = 0;
                                    int failedCOMPONENT_INVALID = 0;
                                    int failedDN_INVALID= 0;
                                    int failedPROFILE_INVALID = 0;
                                    int failedISSUE_ERROR = 0;
                                    int failedAPPROVE_ERROR = 0;
                                    int failedPROVINCE_INVALID = 0;
                                    int failedCERT_INVALID = 0;
                                    int failedAGENCY_DENIED = 0;
                                    int failedCSR = 0;
                                    int failedKeyCSR = 0;
                                    int failedCertType = 0;
                                    int failedSpecialWord = 0;
                                    int failedMethod = 0;
                                    int failedTypeOwnerInvalid = 0;
                                    int failedOwner_incomplete = 0;
                                    String sFailCERT_PEDDING = "";
                                    String sFailFAILED_INSERT = "";
                                    String sFailNOT_PROFILE = "";
                                    String sFailBENEFICIARYUSER_INVALID ="";
                                    String sFailCOMPONENT_INVALID = "";
                                    String sFailDN_INVALID= "";
                                    String sFailPROFILE_INVALID = "";
                                    String sFailISSUE_ERROR = "";
                                    String sFailAPPROVE_ERROR = "";
                                    String sFailPROVINCE_INVALID = "";
                                    String sFailCERT_INVALID = "";
                                    String sFailAGENCY_DENIED = "";
                                    String sFailCSR = "";
                                    String sFailKeyCSR = "";
                                    String sFailCertType = "";
                                    String sFailSpecialWord = "";
                                    String sFailMethod = "";
                                    String sFailTypeOwnerInvalid = "";
                                    String sFailOwner_incomplete = "";
                                    int failedPHONE_INVALID = 0;
                                    int failedEMAIL_INVALID = 0;
                                    int failedPREFIX_INVALID = 0;
                                    String sFailPHONE_INVALID = "";
                                    String sFailEMAIL_INVALID = "";
                                    String sFailPREFIX_INVALID = "";
                                    ArrayList cellStoreArrayList;
                                    int indexOfSTT = 100;
                                    int indexOfCertificateType = 100;
                                    int indexOfCertificateSN = 100;
                                    int indexOfCertificateAuthority = 100;
                                    int indexOfMethod = 100;
                                    int indexOfPersonalName = 100;
                                    int indexOfCompanyName = 100;
                                    int indexOfDomainName = 100;
                                    int indexOfOrganization = 100;
                                    int indexOfPrefixUIDEnterprise = 100;
                                    int indexOfUIDEnterprise = 100;
                                    int indexOfPrefixUIDPersonal = 100;
                                    int indexOfUIDPersonal = 100;
                                    int indexOfOrganizationUnit = 100;
                                    int indexOfTitle = 100;
                                    int indexOfEmailAddress = 100;
                                    int indexOfTelephoneNumber = 100;
                                    int indexOfLocality = 100;
                                    int indexOfStateProvince = 100;
                                    int indexOfCountry = 100;
                                    int indexOfCertificateProfile = 100;
                                    int indexOfCustomerPhoneNumer = 100;
                                    int indexOfCustomerEmail = 100;
                                    int indexOfBeneficiaryUser = 100;
                                    int indexOfBackupKeyEnabled = 100;
                                    int indexOfRevokeOldCertificateEnabled = 100;
                                    int indexOfChangeKeyEnabled = 100;
                                    int indexOfDeleteCertificateEnabled = 100;
                                    int indexOfKeepCertificateSNEnabled = 100;
                                    int indexOfCSR = 100;
                                    int indexOfOrganizationUnit2 = 100;
                                    int indexOfOrganizationUnit3 = 100;
                                    int indexOfOrganizationUnit4 = 100;
                                    int indexOfEmailSANAddress = 100;
                                    int indexOfDNSName1 = 100;
                                    int indexOfDNSName2 = 100;
                                    int indexOfDNSName3 = 100;
                                    int indexOfDNSName4 = 100;
                                    int indexOfIPAddress1 = 100;
                                    int indexOfIPAddress2 = 100;
                                    int indexOfIPAddress3 = 100;
                                    //</editor-fold>
                                    cellStoreArrayList = (ArrayList) dataHolder.get(0);
                                    if(Integer.parseInt(sCERTIFICATION_TYPEParam) == Definitions.CONFIG_SERVICE_TYPE_ID_REGISTRATION) {
                                        //<editor-fold defaultstate="collapsed" desc="### REGISTER">
                                        //<editor-fold defaultstate="collapsed" desc="### GET COLUMN - CHECK COLUMN">
                                        for (int i = 0; i < cellStoreArrayList.size(); i++) {
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnSTT)) {
                                                indexOfSTT = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCertificateType)) {
                                                indexOfCertificateType = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnMethod)) {
                                                indexOfMethod = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnPersonalName)) {
                                                indexOfPersonalName = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCompanyName)) {
                                                indexOfCompanyName = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnDomainName)) {
                                                indexOfDomainName = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnOrganization)) {
                                                indexOfOrganization = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnPrefixUIDEnterprise)) {
                                                indexOfPrefixUIDEnterprise = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnUIDEnterprise)) {
                                                indexOfUIDEnterprise = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnPrefixUIDPersonal)) {
                                                indexOfPrefixUIDPersonal = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnUIDPersonal)) {
                                                indexOfUIDPersonal = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnOrganizationUnit)) {
                                                indexOfOrganizationUnit = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnTitle)) {
                                                indexOfTitle = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnEmailAddress)) {
                                                indexOfEmailAddress = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnTelephoneNumber)) {
                                                indexOfTelephoneNumber = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnLocality)) {
                                                indexOfLocality = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnStateProvince)) {
                                                indexOfStateProvince = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCountry)) {
                                                indexOfCountry = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCertificateProfile)) {
                                                indexOfCertificateProfile = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCustomerPhoneNumer)) {
                                                indexOfCustomerPhoneNumer = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCustomerEmail)) {
                                                indexOfCustomerEmail = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnBeneficiaryUser)) {
                                                indexOfBeneficiaryUser = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnBackupKeyEnabled)) {
                                                indexOfBackupKeyEnabled = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCSR)) {
                                                indexOfCSR = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnOrganizationUnit2)) {
                                                indexOfOrganizationUnit2 = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnOrganizationUnit3)) {
                                                indexOfOrganizationUnit3 = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnOrganizationUnit4)) {
                                                indexOfOrganizationUnit4 = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnEmailSANAddress)) {
                                                indexOfEmailSANAddress = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnDNSName1)) {
                                                indexOfDNSName1 = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnDNSName2)) {
                                                indexOfDNSName2 = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnDNSName3)) {
                                                indexOfDNSName3 = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnDNSName4)) {
                                                indexOfDNSName4 = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnIPAddress1)) {
                                                indexOfIPAddress1 = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnIPAddress2)) {
                                                indexOfIPAddress2 = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnIPAddress3)) {
                                                indexOfIPAddress3 = i;
                                            }
                                        }
                                        boolean booFailColumnName = true;
                                        String sValueFailColumnName = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                        if (indexOfCertificateType == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfMethod == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfPersonalName == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCompanyName == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfDomainName == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfOrganization == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfPrefixUIDEnterprise == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfUIDEnterprise == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfPrefixUIDPersonal == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfUIDPersonal == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfOrganizationUnit == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfTitle == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfEmailAddress == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfTelephoneNumber == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfLocality == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfStateProvince == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCountry == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCertificateProfile == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCustomerPhoneNumer == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCustomerEmail == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfBeneficiaryUser == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfBackupKeyEnabled == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCSR == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfOrganizationUnit2 == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfOrganizationUnit3 == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfOrganizationUnit4 == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfEmailSANAddress == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfDNSName1 == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfDNSName2 == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfDNSName3 == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfDNSName4 == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfIPAddress1 == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfIPAddress2 == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfIPAddress3 == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        //</editor-fold>

                                        if (booFailColumnName == true) {
                                            String sCERTIFICATION_AUTHORITY = "";
                                            String sCERTIFICATION_AUTHORITY_NAME = "";
                                            String CACoreSubject = "";
                                            String sCERTIFICATION_AUTHORITYParam = EscapeUtils.CheckTextNull(sessionsa.getAttribute("SESS_CERTIMPORT_CERTIFICATION_AUTHORITY").toString().trim());
                                            if(!"".equals(sCERTIFICATION_AUTHORITYParam)) {
                                                String[] parts = sCERTIFICATION_AUTHORITYParam.split("###");
                                                sCERTIFICATION_AUTHORITY = parts[0].trim();
                                                CACoreSubject = parts[1].trim();
                                                sCERTIFICATION_AUTHORITY_NAME = parts[2].trim();
                                            }
                                            for (int i = 1; i < dataHolder.size(); i++) {
                                                String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                cellStoreArrayList = (ArrayList) dataHolder.get(i);
                                                String sSTT = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfSTT).toString(), true);
                                                String sCertificateType = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCertificateType).toString(), false);
                                                String sMethod = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfMethod).toString(), false);
                                                String sPersonalName = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfPersonalName).toString(), false);
                                                String sCompanyName = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCompanyName).toString(), false);
                                                String sDomainName = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfDomainName).toString(), false);
                                                String sOrganization = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfOrganization).toString(), false);
                                                String sPrefixUIDEnterprise = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfPrefixUIDEnterprise).toString(), false);
                                                String sUIDEnterprise = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfUIDEnterprise).toString(), false);
                                                String sPrefixUIDPersonal = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfPrefixUIDPersonal).toString(), false);
                                                String sUIDPersonal = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfUIDPersonal).toString(), false);
                                                String sOrganizationUnit = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfOrganizationUnit).toString(), false);
                                                String sTitle = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfTitle).toString(), false);
                                                String sEmailAddress = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfEmailAddress).toString(), false);
                                                String sTelephoneNumber = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfTelephoneNumber).toString(), false);
                                                String sLocality = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfLocality).toString(), false);
                                                String sStateProvince = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfStateProvince).toString(), false);
                                                String sCountry = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCountry).toString(), false);
                                                String sCertificateProfile = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCertificateProfile).toString(), false);
                                                String sCustomerPhoneNumer = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCustomerPhoneNumer).toString(), false);
                                                String sCustomerEmail = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCustomerEmail).toString(), false);
                                                String sBeneficiaryUser = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfBeneficiaryUser).toString(), false);
                                                String sBackupKeyEnabled = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfBackupKeyEnabled).toString(), false);
                                                String sCSR = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCSR).toString(), false);
                                                String sOrganizationUnit2 = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfOrganizationUnit2).toString(), false);
                                                String sOrganizationUnit3 = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfOrganizationUnit3).toString(), false);
                                                String sOrganizationUnit4 = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfOrganizationUnit4).toString(), false);
                                                String sEmailSANAddress = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfEmailSANAddress).toString(), false);
                                                String sDNSName1 = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfDNSName1).toString(), false);
                                                String sDNSName2 = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfDNSName2).toString(), false);
                                                String sDNSName3 = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfDNSName3).toString(), false);
                                                String sDNSName4 = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfDNSName4).toString(), false);
                                                String sIPAddress1 = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfIPAddress1).toString(), false);
                                                String sIPAddress2 = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfIPAddress2).toString(), false);
                                                String sIPAddress3 = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfIPAddress3).toString(), false);
                                                sBackupKeyEnabled = "".equals(sBackupKeyEnabled) ? "FALSE" : sBackupKeyEnabled;
                                                if("TRUE".equals(sBackupKeyEnabled)){sBackupKeyEnabled = "1";} else {sBackupKeyEnabled="0";}
                                                //<editor-fold defaultstate="collapsed" desc="### COLUMN VALUE VALID">
                                                if("".equals(sCountry) || "".equals(sCertificateProfile)
                                                    || "".equals(sCustomerPhoneNumer) || "".equals(sCustomerEmail) || "".equals(sBeneficiaryUser)) {
                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                }
                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                                {
                                                    if(CommonFunction.regexPhoneValid(EscapeUtils.CheckTextNull(sCustomerPhoneNumer), sREGEX_PHONE) == false)
                                                    {
                                                        sVALID_CODE = "PHONE_INVALID";
                                                    } else {
                                                        if(CommonFunction.regexEmailValid(EscapeUtils.CheckTextNull(sCustomerEmail), sREGEX_EMAIL) == false)
                                                        {
                                                            sVALID_CODE = "EMAIL_INVALID";
                                                        }
                                                    }
                                                }
                                                if(!"".equals(sPrefixUIDEnterprise)){
                                                    if(CommonReferServlet.checkPrefixEnterpriseVN(sPrefixUIDEnterprise) == false) {
                                                        sVALID_CODE = "PREFIX_INVALID";
                                                    }
                                                }
                                                if(!"".equals(sPrefixUIDPersonal)){
                                                    if(CommonReferServlet.checkPrefixPersonalVN(sPrefixUIDPersonal) == false) {
                                                        sVALID_CODE = "PREFIX_INVALID";
                                                    }
                                                }
                                                //</editor-fold>

                                                String sTOKEN_ID = "";
                                                String sTOKEN_SN = "";
                                                String CheckPRIVATE_KEY = "1";
                                                String pPROVINCE_DESC = "";
                                                String strPasswordP12 = "";
                                                int[] pCERTIFICATE_ATTR_ID = new int[1];
                                                int[] pCERTIFICATE_ID = new int[1];
                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                                {
                                                    boolean isValidCSR = true;
                                                    //<editor-fold defaultstate="collapsed" desc="### GET DN, TOKEN and BackUpKey">
                                                    if(CommonFunction.checkHardTokenEnabled(sMethod) == true) {
                                                        sTOKEN_ID = String.valueOf(Definitions.CONFIG_TOKEN_UNASSIGN_ID);
                                                        sTOKEN_SN = Definitions.CONFIG_TOKEN_UNASSIGN_SN;
                                                        if("0".equals(sBackupKeyEnabled)) {
                                                            CheckPRIVATE_KEY = "0";
                                                        } else {
                                                            CheckPRIVATE_KEY = "1";
                                                        }
                                                    } else if(sMethod.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_SOFT_TOKEN)) {
                                                        CheckPRIVATE_KEY = "1";
                                                        if(sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_SSL))
                                                        {
                                                            sTOKEN_ID = String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID);
                                                            sTOKEN_SN = Definitions.CONFIG_TOKEN_SSL_SN;
                                                            sCSR = "";
                                                        } else if(sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING))
                                                        {
                                                            sTOKEN_ID = String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID);
                                                            sTOKEN_SN = Definitions.CONFIG_TOKEN_CODESIGNNING_SN;
                                                            sCSR = "";
                                                        } else {
                                                            sTOKEN_ID = String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID);
                                                            sTOKEN_SN = Definitions.CONFIG_TOKEN_SIGNSERVER_SN;
                                                        }
                                                        if("0".equals(sBackupKeyEnabled))
                                                        {
                                                            CheckPRIVATE_KEY = "0";
                                                            String sKeySizeDB;
                                                            isValidCSR = false;
                                                            CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                                            db.S_BO_GET_ALGORITHM_KEY_SIZE_BY_CODE(sCertificateProfile, rsCert);
                                                            if(rsCert[0].length > 0)
                                                            {
                                                                sKeySizeDB = EscapeUtils.CheckTextNull(rsCert[0][0].KEY_SIZE);
                                                                String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(sCSR);
                                                                isValidCSR = sKeySizeDB.equals(sKeySizeCSR);
                                                            }
                                                        } else {
                                                            strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                                        }
                                                    }
                                                    if(isValidCSR == false)
                                                    {
                                                        sVALID_CODE = "CSR_KEY";
                                                    }
                                                    //</editor-fold>
                                                }
                                                // GET PROVINCE
                                                if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE)) {
                                                    if(!"".equals(sStateProvince)) {
                                                        CityProvinceInfo[][] rsCity = new CityProvinceInfo[1][];
                                                        db.S_BO_API_PROVINCE_LIST(sStateProvince, Integer.parseInt(sessLanguageGlobal), rsCity);
                                                        if(rsCity[0].length > 0) {
                                                            pPROVINCE_DESC = EscapeUtils.CheckTextNull(rsCity[0][0].cityProvinceName);
                                                        }
                                                    } else {
                                                        UserInfo[][] rsUserBranch = new UserInfo[1][];
                                                        db.S_BO_API_USER_LIST(sBeneficiaryUser, "", "", Integer.parseInt(sessLanguageGlobal), rsUserBranch);
                                                        if(rsUserBranch[0].length > 0) {
                                                            BRANCH[][] rsBranch = new BRANCH[1][];
                                                            db.S_BO_BRANCH_DETAIL(String.valueOf(rsUserBranch[0][0].raID), rsBranch);
                                                            if(rsBranch[0].length > 0) {
                                                                CITY_PROVINCE[][] rsCity = new CITY_PROVINCE[1][];
                                                                db.S_BO_PROVINCE_DETAIL(String.valueOf(rsBranch[0][0].PROVINCE_ID), rsCity);
                                                                if(rsCity[0].length > 0) {
                                                                    sStateProvince = EscapeUtils.CheckTextNull(rsCity[0][0].NAME);
                                                                    pPROVINCE_DESC = EscapeUtils.CheckTextNull(rsCity[0][0].REMARK);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                String sDNResult = "";
                                                String sCompanyNameDB = "";
                                                String sPersonalNameDB = "";
                                                String sDomainNameDB = "";
                                                String sUIDEnterpriseDB = "";
                                                String sUIDPersonalDB = "";
                                                String sSANProperties = "";
                                                if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE)) {
                                                    List<String> ouClientList = new ArrayList<>(); 
                                                    int intOUCheckClient = 0;
                                                    if (!"".equals(sOrganizationUnit)) {
                                                        intOUCheckClient = intOUCheckClient + 1;
                                                        ouClientList.add(sOrganizationUnit);
                                                    }
                                                    if (!"".equals(sOrganizationUnit2)) {
                                                        intOUCheckClient = intOUCheckClient + 1;
                                                        ouClientList.add(sOrganizationUnit2);
                                                    }
                                                    if (!"".equals(sOrganizationUnit3)) {
                                                        intOUCheckClient = intOUCheckClient + 1;
                                                        ouClientList.add(sOrganizationUnit3);
                                                    }
                                                    if (!"".equals(sOrganizationUnit4)) {
                                                        intOUCheckClient = intOUCheckClient + 1;
                                                        ouClientList.add(sOrganizationUnit4);
                                                    }
                                                    //<editor-fold defaultstate="collapsed" desc="### PROFILE CHECK">
                                                    CertificateProfileInfo[][] rsProfile = new CertificateProfileInfo[1][];
                                                    db.S_BO_API_CERTIFICATION_PROFILE_GET_PROPERTIES(sCERTIFICATION_AUTHORITY_NAME, sCertificateProfile, sMethod, rsProfile);
                                                    if(rsProfile[0].length > 0)
                                                    {
                                                        String sProperties = EscapeUtils.CheckTextNull(rsProfile[0][0].certificateProfileProperties);
                                                        if (!"".equals(sProperties)) {
                                                            ObjectMapper objectMapper = new ObjectMapper();
                                                            CERTIFICATE_ATTRIBUTES proParse = objectMapper.readValue(sProperties, CERTIFICATE_ATTRIBUTES.class);
                                                            CertificateComponentInfo[][] rsComponentFromServer = new CertificateComponentInfo[1][];
                                                            ArrayList<CertificateComponentInfo> tempListComServer = new ArrayList<>();
                                                            int intCompOUSrv = 0;
                                                            for (CERTIFICATE_ATTRIBUTES.Attribute attribute : proParse.getAttributes()) {
                                                                if (attribute.getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY))
                                                                {
                                                                    for (int n = 0; n < attribute.getAttributes().size(); n++) {
                                                                        CertificateComponentInfo mh = new CertificateComponentInfo();
                                                                        mh.code = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                                                                        mh.prefix = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getPrefix());
                                                                        mh.requireEnabled = attribute.isRequire();
                                                                        mh.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY;
                                                                        tempListComServer.add(mh);
                                                                    }
                                                                } else if (attribute.getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL))
                                                                {
                                                                    for (int n = 0; n < attribute.getAttributes().size(); n++) {
                                                                        CertificateComponentInfo mh = new CertificateComponentInfo();
                                                                        mh.code = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                                                                        mh.prefix = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getPrefix());
                                                                        mh.requireEnabled = attribute.isRequire();
                                                                        mh.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL;
                                                                        tempListComServer.add(mh);
                                                                    }
                                                                } else {
                                                                    if (EscapeUtils.CheckTextNull(attribute.getName()).equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU)) {
                                                                        intCompOUSrv = intCompOUSrv + 1;
                                                                    }
                                                                    CertificateComponentInfo mh = new CertificateComponentInfo();
                                                                    mh.code = EscapeUtils.CheckTextNull(attribute.getName());
                                                                    mh.prefix = EscapeUtils.CheckTextNull(attribute.getPrefix());
                                                                    mh.requireEnabled = attribute.isRequire();
                                                                    mh.attributeType = attribute.getAttributeType();
                                                                    tempListComServer.add(mh);
                                                                }
                                                            }
                                                            if(proParse.getAttributeSans() != null ) {
                                                                if(proParse.getAttributeSans().size() > 0 ) {
                                                                    for (CERTIFICATE_ATTRIBUTES.AttributeSan attributeSan : proParse.getAttributeSans()) {
                                                                        CertificateComponentInfo tempItem = new CertificateComponentInfo();
                                                                        tempItem.code = attributeSan.getName();
                                                                        tempItem.prefix = attributeSan.getPrefix();
                                                                        tempItem.requireEnabled = attributeSan.isRequire();
                                                                        tempItem.attributeType = attributeSan.getAttributeType();
                                                                        tempListComServer.add(tempItem);
                                                                    }
                                                                }
                                                            }
                                                            rsComponentFromServer[0] = new CertificateComponentInfo[tempListComServer.size()];
                                                            rsComponentFromServer[0] = tempListComServer.toArray(rsComponentFromServer[0]);
                                                            if(sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_PERSONAL_GOV)
                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_ENTERPRISE_GOV)
                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_CLIENT)
                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_SERVER)
                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_AUTH)
                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_SIGN)
                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING_GOV))
                                                            {
                                                                if(intOUCheckClient > intCompOUSrv) {
                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                }
                                                            }
                                                            if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE)) {
                                                                if(rsComponentFromServer[0].length > 0)
                                                                {
                                                                    boolean requireUIDCompany = false;
                                                                    boolean requireUIDPersonal = false;
                                                                    for(CertificateComponentInfo rsItem : rsComponentFromServer[0]) {
                                                                        if (rsItem.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)) {
                                                                            requireUIDCompany = rsItem.requireEnabled;
                                                                        }
                                                                        if (rsItem.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)) {
                                                                            requireUIDPersonal = rsItem.requireEnabled;
                                                                        }
                                                                    }
                                                                    if(requireUIDCompany == true || requireUIDPersonal == true) {
                                                                        if(CommonFunction.checkHardTokenEnabled(sMethod) == true) {
                                                                            if(sCertificateType.equals(Definitions.CONFIG_CERTTYPE_DESC_STAFF)) {
                                                                                if("".equals(sPersonalName) || "".equals(sOrganization))
                                                                                {
                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                }
                                                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                                                                {
                                                                                    if(requireUIDCompany == true) {
                                                                                        if("".equals(sPrefixUIDEnterprise) || "".equals(sUIDEnterprise))
                                                                                        {
                                                                                            sVALID_CODE = "COMPONENT_INVALID";
                                                                                        }
                                                                                    }
                                                                                    if(requireUIDPersonal == true) {
                                                                                        if("".equals(sPrefixUIDPersonal) || "".equals(sUIDPersonal)) {
                                                                                            sVALID_CODE = "COMPONENT_INVALID";
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else if(sCertificateType.equals(Definitions.CONFIG_CERTTYPE_DESC_ENTERPRISE)) {
                                                                                if("".equals(sCompanyName))
                                                                                {
                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                }
                                                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                                                                {
                                                                                    if(requireUIDCompany == true) {
                                                                                        if("".equals(sPrefixUIDEnterprise) || "".equals(sUIDEnterprise))
                                                                                        {
                                                                                            sVALID_CODE = "COMPONENT_INVALID";
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else if(sCertificateType.equals(Definitions.CONFIG_CERTTYPE_DESC_PERSONAL)) {
                                                                                if("".equals(sPersonalName))
                                                                                {
                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                }
                                                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                                                                {
                                                                                    if(requireUIDPersonal == true) {
                                                                                        if("".equals(sPrefixUIDPersonal) || "".equals(sUIDPersonal)) {
                                                                                            sVALID_CODE = "COMPONENT_INVALID";
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                sVALID_CODE = "CERTIFICATETYPE_INVALID";
                                                                            }
                                                                        } else if(sMethod.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_SOFT_TOKEN)) {
                                                                            if(sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_SSL)) {
                                                                                if("".equals(sDomainName))
                                                                                {
                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                }
                                                                            } else if(sCertificateType.equals(Definitions.CONFIG_CERTTYPE_DESC_STAFF)) {
                                                                                if("".equals(sPersonalName) || "".equals(sOrganization))
                                                                                {
                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                }
                                                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                                                                {
                                                                                    if(requireUIDCompany == true) {
                                                                                        if("".equals(sPrefixUIDEnterprise) || "".equals(sUIDEnterprise))
                                                                                        {
                                                                                            sVALID_CODE = "COMPONENT_INVALID";
                                                                                        }
                                                                                    }
                                                                                    if(requireUIDPersonal == true) {
                                                                                        if("".equals(sPrefixUIDPersonal) || "".equals(sUIDPersonal)) {
                                                                                            sVALID_CODE = "COMPONENT_INVALID";
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else if(sCertificateType.equals(Definitions.CONFIG_CERTTYPE_DESC_ENTERPRISE)
                                                                                || sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING)) {
                                                                                if("".equals(sCompanyName))
                                                                                {
                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                }
                                                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                                                                {
                                                                                    if(requireUIDCompany == true) {
                                                                                        if("".equals(sPrefixUIDEnterprise) || "".equals(sUIDEnterprise))
                                                                                        {
                                                                                            sVALID_CODE = "COMPONENT_INVALID";
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else if(sCertificateType.equals(Definitions.CONFIG_CERTTYPE_DESC_PERSONAL)) {
                                                                                if("".equals(sPersonalName)) {
                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                }
                                                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                                                    if(requireUIDPersonal == true) {
                                                                                        if("".equals(sPrefixUIDPersonal) || "".equals(sUIDPersonal)) {
                                                                                            sVALID_CODE = "COMPONENT_INVALID";
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else if(sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_PERSONAL_GOV)
                                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_ENTERPRISE_GOV)
                                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_CLIENT)
                                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_SERVER)
                                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_AUTH)
                                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_SIGN)
                                                                                && sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING_GOV)) {

                                                                            } else {
                                                                                sVALID_CODE = "CERTIFICATETYPE_INVALID";
                                                                            }
                                                                        } else {
                                                                            sVALID_CODE = "METHOD_INVALID";
                                                                        }
                                                                    }
                                                                }

                                                                List<CERTIFICATION_PROPERTIES_JSON.Attribute> attributesSan = new ArrayList<>();
                                                                if(rsComponentFromServer[0].length > 0)
                                                                {
                                                                    if(sCertificateType.equals(Definitions.CONFIG_CERTTYPE_DESC_ENTERPRISE)
                                                                        || sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING))
                                                                    {
                                                                        if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE)) {
                                                                            //<editor-fold defaultstate="collapsed" desc="### ENTERPRISE - CODE_SIGNING CHECK AND GET DN">
                                                                            for(CertificateComponentInfo rsComponentFromServer1 : rsComponentFromServer[0]) {
                                                                                if(!EscapeUtils.CheckTextNull(rsComponentFromServer1.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN)) {
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDEnterprise+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDEnterprise;
                                                                                        sUIDEnterpriseDB = sUIDEnterprise;
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sCompanyName;
                                                                                        sCompanyNameDB = sCompanyName;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sCompanyName))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_O))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sOrganization;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sOrganization))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sOrganizationUnit;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sOrganizationUnit))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_E))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sEmailAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sEmailAddress))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sTelephoneNumber;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sTelephoneNumber))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_L))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sLocality;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sLocality))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST))
                                                                                    {
                                                                                        rsComponentFromServer1.value = pPROVINCE_DESC;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(pPROVINCE_DESC))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_C))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sCountry;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sCountry))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(EscapeUtils.CheckTextNull(rsComponentFromServer1.value)))
                                                                                    {
                                                                                        if(CommonFunction.checkCertCharacterSpecial(rsComponentFromServer1.value) == false) {
                                                                                            sVALID_CODE = "SPECIAL_INVALID";
                                                                                            break;
                                                                                        }
                                                                                        sDNResult += EscapeUtils.CheckTextNull(rsComponentFromServer1.code) + "=" + EscapeUtils.CheckTextNull(rsComponentFromServer1.prefix)
                                                                                            + CommonFunction.replaceStringCharaterSpecialDN(EscapeUtils.CheckTextNull(rsComponentFromServer1.value), true, false) + ", ";
                                                                                    }
                                                                                } else {
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_rfc822Name)) {
                                                                                        rsComponentFromServer1.value = sEmailSANAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sEmailSANAddress))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                        if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE) && !"".equals(sEmailSANAddress)) {
                                                                                            if(CommonFunction.regexEmailValid(EscapeUtils.CheckTextNull(sEmailSANAddress), sREGEX_EMAIL) == false) {
                                                                                                sVALID_CODE = "EMAIL_INVALID";
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(rsComponentFromServer1.value)) {
                                                                                        CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                                                        attribute.setKey(rsComponentFromServer1.code);
                                                                                        attribute.setValue(rsComponentFromServer1.value);
                                                                                        attributesSan.add(attribute);
                                                                                    }
                                                                                }
                                                                            }
                                                                            //</editor-fold>
                                                                        }
                                                                    } else if(sCertificateType.equals(Definitions.CONFIG_CERTTYPE_DESC_STAFF))
                                                                    {
                                                                        if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE)) {
                                                                            //<editor-fold defaultstate="collapsed" desc="### STAFF CHECK AND GET DN">
                                                                            for(CertificateComponentInfo rsComponentFromServer1 : rsComponentFromServer[0])
                                                                            {
                                                                                if(!EscapeUtils.CheckTextNull(rsComponentFromServer1.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN)) {
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDEnterprise+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDEnterprise;
                                                                                        sUIDEnterpriseDB = sUIDEnterprise;
                                                                                    }
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDPersonal+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDPersonal;
                                                                                        sUIDPersonalDB = sUIDPersonal;
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN)) {
                                                                                        rsComponentFromServer1.value = sPersonalName;
                                                                                        sPersonalNameDB = sPersonalName;
                                                                                        if(rsComponentFromServer1.requireEnabled == true) {
                                                                                            if("".equals(sPersonalName)) {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_O))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sOrganization;
                                                                                        sCompanyNameDB = sOrganization;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sOrganization))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sOrganizationUnit;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sOrganizationUnit))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_T))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sTitle;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sTitle))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_E))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sEmailAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sEmailAddress))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sTelephoneNumber;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sTelephoneNumber))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_L))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sLocality;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sLocality))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST))
                                                                                    {
                                                                                        rsComponentFromServer1.value = pPROVINCE_DESC;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(pPROVINCE_DESC))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_C))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sCountry;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sCountry))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(EscapeUtils.CheckTextNull(rsComponentFromServer1.value)))
                                                                                    {
                                                                                        if(CommonFunction.checkCertCharacterSpecial(rsComponentFromServer1.value) == false) {
                                                                                            sVALID_CODE = "SPECIAL_INVALID";
                                                                                            break;
                                                                                        }
                                                                                        sDNResult += EscapeUtils.CheckTextNull(rsComponentFromServer1.code) + "=" + EscapeUtils.CheckTextNull(rsComponentFromServer1.prefix)
                                                                                            + CommonFunction.replaceStringCharaterSpecialDN(EscapeUtils.CheckTextNull(rsComponentFromServer1.value), true, false) + ", ";
                                                                                    }
                                                                                } else {
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_rfc822Name)) {
                                                                                        rsComponentFromServer1.value = sEmailSANAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sEmailSANAddress))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                        if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE) && !"".equals(sEmailSANAddress)) {
                                                                                            if(CommonFunction.regexEmailValid(EscapeUtils.CheckTextNull(sEmailSANAddress), sREGEX_EMAIL) == false) {
                                                                                                sVALID_CODE = "EMAIL_INVALID";
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(rsComponentFromServer1.value)) {
                                                                                        CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                                                        attribute.setKey(rsComponentFromServer1.code);
                                                                                        attribute.setValue(rsComponentFromServer1.value);
                                                                                        attributesSan.add(attribute);
                                                                                    }
                                                                                }
                                                                            }
                                                                            //</editor-fold>
                                                                        }
                                                                    } else if(sCertificateType.equals(Definitions.CONFIG_CERTTYPE_DESC_PERSONAL))
                                                                    {
                                                                        if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE)) {
                                                                            //<editor-fold defaultstate="collapsed" desc="### PERSONAL CHECK AND GET DN">
                                                                            for(CertificateComponentInfo rsComponentFromServer1 : rsComponentFromServer[0])
                                                                            {
                                                                                if(!EscapeUtils.CheckTextNull(rsComponentFromServer1.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN))
                                                                                {
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDPersonal+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDPersonal;
                                                                                        sUIDPersonalDB = sUIDPersonal;
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sPersonalName;
                                                                                        sPersonalNameDB = sPersonalName;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sPersonalName))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_E))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sEmailAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sEmailAddress))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sTelephoneNumber;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sTelephoneNumber))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_L))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sLocality;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sLocality))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST))
                                                                                    {
                                                                                        rsComponentFromServer1.value = pPROVINCE_DESC;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(pPROVINCE_DESC))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_C))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sCountry;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sCountry))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(EscapeUtils.CheckTextNull(rsComponentFromServer1.value)))
                                                                                    {
                                                                                        if(CommonFunction.checkCertCharacterSpecial(rsComponentFromServer1.value) == false) {
                                                                                            sVALID_CODE = "SPECIAL_INVALID";
                                                                                            break;
                                                                                        }
                                                                                        sDNResult += EscapeUtils.CheckTextNull(rsComponentFromServer1.code) + "=" + EscapeUtils.CheckTextNull(rsComponentFromServer1.prefix)
                                                                                            + CommonFunction.replaceStringCharaterSpecialDN(EscapeUtils.CheckTextNull(rsComponentFromServer1.value), true, false) + ", ";
                                                                                    }
                                                                                } else {
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_rfc822Name)) {
                                                                                        rsComponentFromServer1.value = sEmailSANAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sEmailSANAddress))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                        if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE) && !"".equals(sEmailSANAddress)) {
                                                                                            if(CommonFunction.regexEmailValid(EscapeUtils.CheckTextNull(sEmailSANAddress), sREGEX_EMAIL) == false) {
                                                                                                sVALID_CODE = "EMAIL_INVALID";
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(rsComponentFromServer1.value)) {
                                                                                        CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                                                        attribute.setKey(rsComponentFromServer1.code);
                                                                                        attribute.setValue(rsComponentFromServer1.value);
                                                                                        attributesSan.add(attribute);
                                                                                    }
                                                                                }
                                                                            }
                                                                            //</editor-fold>
                                                                        }
                                                                    } else if(sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_SSL))
                                                                    {
                                                                        if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE)) {
                                                                            //<editor-fold defaultstate="collapsed" desc="### PERSONAL CHECK AND GET DN">
                                                                            for(CertificateComponentInfo rsComponentFromServer1 : rsComponentFromServer[0])
                                                                            {
                                                                                if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID)) {
                                                                                    if(rsComponentFromServer1.prefix.equals(sPrefixUIDEnterprise+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDEnterprise;
                                                                                        sUIDEnterpriseDB = sUIDEnterprise;
                                                                                    }
                                                                                    if(rsComponentFromServer1.prefix.equals(sPrefixUIDPersonal+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDPersonal;
                                                                                        sUIDPersonalDB = sUIDPersonal;
                                                                                    }
                                                                                    if(!"".equals(EscapeUtils.CheckTextNull(rsComponentFromServer1.value))) {
                                                                                        if(CommonFunction.checkCertCharacterSpecial(rsComponentFromServer1.value) == false) {
                                                                                            sVALID_CODE = "SPECIAL_INVALID";
                                                                                            break;
                                                                                        }
                                                                                        sDNResult += EscapeUtils.CheckTextNull(rsComponentFromServer1.code) + "=" + EscapeUtils.CheckTextNull(rsComponentFromServer1.prefix)
                                                                                            + CommonFunction.replaceStringCharaterSpecialDN(EscapeUtils.CheckTextNull(rsComponentFromServer1.value), true, false) + ", ";
                                                                                    }
                                                                                } else {
                                                                                    if(!EscapeUtils.CheckTextNull(rsComponentFromServer1.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN))
                                                                                    {
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sDomainName;
                                                                                            sDomainNameDB = sDomainName;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sDomainName))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_O))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sOrganization;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sOrganization))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_E))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sEmailAddress;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sEmailAddress))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                            if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE) && !"".equals(sEmailAddress)) {
                                                                                                if(CommonFunction.regexEmailValid(EscapeUtils.CheckTextNull(sEmailAddress), sREGEX_EMAIL) == false)
                                                                                                {
                                                                                                    sVALID_CODE = "EMAIL_INVALID";
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sTelephoneNumber;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sTelephoneNumber))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                            if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE) && !"".equals(sTelephoneNumber)) {
                                                                                                if(CommonFunction.regexEmailValid(EscapeUtils.CheckTextNull(sTelephoneNumber), sREGEX_EMAIL) == false)
                                                                                                {
                                                                                                    sVALID_CODE = "EMAIL_INVALID";
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_L))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sLocality;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sLocality))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST))
                                                                                        {
                                                                                            rsComponentFromServer1.value = pPROVINCE_DESC;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(pPROVINCE_DESC))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_C))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sCountry;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sCountry))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(!"".equals(EscapeUtils.CheckTextNull(rsComponentFromServer1.value)))
                                                                                        {
                                                                                            if(CommonFunction.checkCertCharacterSpecial(rsComponentFromServer1.value) == false) {
                                                                                                sVALID_CODE = "SPECIAL_INVALID";
                                                                                                break;
                                                                                            }
                                                                                            sDNResult += EscapeUtils.CheckTextNull(rsComponentFromServer1.code) + "=" + EscapeUtils.CheckTextNull(rsComponentFromServer1.prefix)
                                                                                                + CommonFunction.replaceStringCharaterSpecialDN(EscapeUtils.CheckTextNull(rsComponentFromServer1.value), true, false) + ", ";
                                                                                        }
                                                                                    } else {
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_rfc822Name))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sEmailSANAddress;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sEmailSANAddress))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                            if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE) && !"".equals(sEmailSANAddress)) {
                                                                                                if(CommonFunction.regexEmailValid(EscapeUtils.CheckTextNull(sEmailSANAddress), sREGEX_EMAIL) == false) {
                                                                                                    sVALID_CODE = "EMAIL_INVALID";
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_DNS))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sDNSName1;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sDNSName1))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_DNS2))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sDNSName2;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sDNSName2))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_DNS3))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sDNSName3;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sDNSName3))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_DNS4))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sDNSName4;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sDNSName4))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_IP_SAN))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sIPAddress1;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sIPAddress1))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_IP_SAN2))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sIPAddress2;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sIPAddress2))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_IP_SAN3))
                                                                                        {
                                                                                            rsComponentFromServer1.value = sIPAddress3;
                                                                                            if(rsComponentFromServer1.requireEnabled == true)
                                                                                            {
                                                                                                if("".equals(sIPAddress3))
                                                                                                {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(!"".equals(rsComponentFromServer1.value)) {
                                                                                            CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                                                            attribute.setKey(rsComponentFromServer1.code);
                                                                                            attribute.setValue(rsComponentFromServer1.value);
                                                                                            attributesSan.add(attribute);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            //</editor-fold>
                                                                        }
                                                                    } else if(sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_PERSONAL_GOV)
                                                                        || sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_ENTERPRISE_GOV))
                                                                    {
                                                                        if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE)) {
                                                                            //<editor-fold defaultstate="collapsed" desc="### PERSONAL CHECK AND GET DN">
                                                                            for(CertificateComponentInfo rsComponentFromServer1 : rsComponentFromServer[0])
                                                                            {
                                                                                if(!EscapeUtils.CheckTextNull(rsComponentFromServer1.attributeType).equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_TEXTFIELD_SAN))
                                                                                {
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDEnterprise+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDEnterprise;
                                                                                        sUIDEnterpriseDB = sUIDEnterprise;
                                                                                    }
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDPersonal+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDPersonal;
                                                                                        sUIDPersonalDB = sUIDPersonal;
                                                                                    }
//                                                                                        if(rsComponentFromServer1.prefix.equals(Definitions.CONFIG_PREFIX_PERSONAL_CODE +":")
//                                                                                            || rsComponentFromServer1.prefix.equals(Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE +":")
//                                                                                            || rsComponentFromServer1.prefix.equals(Definitions.CONFIG_PREFIX_PERSONAL_CITIZEN_CODE +":"))
//                                                                                        {
//                                                                                            if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID)
//                                                                                                && rsComponentFromServer1.prefix.equals(Definitions.CONFIG_PREFIX_PERSONAL_CODE +":"))
//                                                                                            {
//                                                                                                rsComponentFromServer1.value = sPersonalID;
//                                                                                                sPIDDB = sPersonalID;
//                                                                                            }
//                                                                                            if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID)
//                                                                                                && rsComponentFromServer1.prefix.equals(Definitions.CONFIG_PREFIX_PERSONAL_PASSPORT_CODE +":")) {
//                                                                                                rsComponentFromServer1.value = sPassport;
//                                                                                                sPassportDB = sPassport;
//                                                                                            }
//                                                                                            if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID)
//                                                                                                && rsComponentFromServer1.prefix.equals(Definitions.CONFIG_PREFIX_PERSONAL_CITIZEN_CODE +":")) {
//                                                                                                rsComponentFromServer1.value = sCCCD;
//                                                                                                sCCCDDB = sCCCD;
//                                                                                            }
//                                                                                        }
//                                                                                        if(rsComponentFromServer1.prefix.equals(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE +":")
//                                                                                            || rsComponentFromServer1.prefix.equals(Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE +":"))
//                                                                                        {
//                                                                                            if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID)
//                                                                                                && rsComponentFromServer1.prefix.equals(Definitions.CONFIG_PREFIX_ENTERPRISE_TAX_CODE+":"))
//                                                                                            {
//                                                                                                rsComponentFromServer1.value = sTaxCode;
//                                                                                                sTaxCodeDB = sTaxCode;
//                                                                                            } else if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID)
//                                                                                                && rsComponentFromServer1.prefix.equals(Definitions.CONFIG_PREFIX_ENTERPRISE_BUDGET_CODE+":")) {
//                                                                                                rsComponentFromServer1.value = sBudgetCode;
//                                                                                                sBudgetCodeDB = sBudgetCode;
//                                                                                            } else if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_UID)
//                                                                                                && rsComponentFromServer1.prefix.equals(Definitions.CONFIG_PREFIX_ENTERPRISE_DECISION + ":")) {
//                                                                                                rsComponentFromServer1.value = sDecision;
//                                                                                                sDecisionDB = sDecision;
//                                                                                            }
//                                                                                        }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN)) {
                                                                                        if(sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_ENTERPRISE_GOV)) {
                                                                                            rsComponentFromServer1.value = sCompanyName;
                                                                                            sCompanyNameDB = sCompanyName;
                                                                                            if(rsComponentFromServer1.requireEnabled == true) {
                                                                                                if("".equals(sCompanyName)) {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            rsComponentFromServer1.value = sPersonalName;
                                                                                            sPersonalNameDB = sPersonalName;
                                                                                            if(rsComponentFromServer1.requireEnabled == true) {
                                                                                                if("".equals(sPersonalName)) {
                                                                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_O)) {
                                                                                        rsComponentFromServer1.value = sOrganization;
//                                                                                            sCompanyNameDB = sOrganization;
                                                                                        if(rsComponentFromServer1.requireEnabled == true) {
                                                                                            if("".equals(sOrganization)) {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU)) {
                                                                                        if(ouClientList.size() > 0){
                                                                                            rsComponentFromServer1.value = ouClientList.get(0);
                                                                                            ouClientList.remove(0);
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_T)) {
                                                                                        rsComponentFromServer1.value = sOrganization;
                                                                                        if(rsComponentFromServer1.requireEnabled == true) {
                                                                                            if("".equals(sPersonalName)) {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_E)) {
                                                                                        rsComponentFromServer1.value = sEmailAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true) {
                                                                                            if("".equals(sEmailAddress)) {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber)) {
                                                                                        rsComponentFromServer1.value = sTelephoneNumber;
                                                                                        if(rsComponentFromServer1.requireEnabled == true) {
                                                                                            if("".equals(sTelephoneNumber)) {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_L)) {
                                                                                        rsComponentFromServer1.value = sLocality;
                                                                                        if(rsComponentFromServer1.requireEnabled == true) {
                                                                                            if("".equals(sLocality)) {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST)) {
                                                                                        rsComponentFromServer1.value = pPROVINCE_DESC;
                                                                                        if(rsComponentFromServer1.requireEnabled == true) {
                                                                                            if("".equals(pPROVINCE_DESC)) {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_C)) {
                                                                                        rsComponentFromServer1.value = sCountry;
                                                                                        if(rsComponentFromServer1.requireEnabled == true) {
                                                                                            if("".equals(sCountry)) {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(EscapeUtils.CheckTextNull(rsComponentFromServer1.value)))
                                                                                    {
                                                                                        if(CommonFunction.checkCertCharacterSpecial(rsComponentFromServer1.value) == false) {
                                                                                            sVALID_CODE = "SPECIAL_INVALID";
                                                                                            break;
                                                                                        }
                                                                                        sDNResult += EscapeUtils.CheckTextNull(rsComponentFromServer1.code) + "=" + EscapeUtils.CheckTextNull(rsComponentFromServer1.prefix)
                                                                                            + CommonFunction.replaceStringCharaterSpecialDN(EscapeUtils.CheckTextNull(rsComponentFromServer1.value), true, false) + ", ";
                                                                                    }
                                                                                } else {
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_SAN_TAG_rfc822Name)) {
                                                                                        rsComponentFromServer1.value = sEmailSANAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sEmailSANAddress)) {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                        if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE) && !"".equals(sEmailSANAddress)) {
                                                                                            if(CommonFunction.regexEmailValid(EscapeUtils.CheckTextNull(sEmailSANAddress), sREGEX_EMAIL) == false) {
                                                                                                sVALID_CODE = "EMAIL_INVALID";
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(rsComponentFromServer1.value)) {
                                                                                        CERTIFICATION_PROPERTIES_JSON.Attribute attribute = new CERTIFICATION_PROPERTIES_JSON.Attribute();
                                                                                        attribute.setKey(rsComponentFromServer1.code);
                                                                                        attribute.setValue(rsComponentFromServer1.value);
                                                                                        attributesSan.add(attribute);
                                                                                    }
                                                                                }
                                                                            }
                                                                            //</editor-fold>
                                                                        }
                                                                    }
                                                                    if(attributesSan.size() > 0) {
                                                                        sSANProperties = "{\"attributes\":" + objectMapper.writeValueAsString(attributesSan) + "}";
                                                                    }
//                                                                        CommonFunction.LogDebugString(log, "sDNResult", sDNResult);
//                                                                        CommonFunction.LogDebugString(log, "sSANProperties", sSANProperties);
                                                                } else {
                                                                    sVALID_CODE = "PROFILE_INVALID";
                                                                }
                                                            }
                                                        } else {
                                                            sVALID_CODE = "PROFILE_INVALID";
                                                        }
                                                    } else {
                                                        sVALID_CODE = "PROFILE_INVALID";
                                                    }
                                                    //</editor-fold>
                                                }
                                                if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE)) {
                                                    if(!"".equals(sDNResult)) {
                                                        sDNResult = CommonFunction.subLastCharater(sDNResult);
                                                        String sParam = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                        ATTRIBUTE_VALUES valueATTR = new ATTRIBUTE_VALUES();
                                                        String strReqValueATTR;
                                                        ObjectMapper objectMapper;
                                                        if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE))
                                                        {
                                                            String hdfOwnerID = "1";
                                                            //<editor-fold defaultstate="collapsed" desc="### OWNER INSERT">
                                                            String sEnterpriseOwnerID = "";
                                                            String sPersonalOwnerID = "";
                                                            if(!"".equals(sPrefixUIDEnterprise) && !"".equals(sUIDEnterpriseDB)) {
                                                                sEnterpriseOwnerID = CommonReferServlet.convertPrefixVNToEN(sPrefixUIDEnterprise + ":" + sUIDEnterpriseDB, true);
                                                            }
                                                            if(!"".equals(sPrefixUIDPersonal) && !"".equals(sUIDPersonalDB)) {
                                                                sPersonalOwnerID = CommonReferServlet.convertPrefixVNToEN(sPrefixUIDPersonal + ":" + sUIDPersonalDB, false);
                                                            }
                                                            String pCERTIFICATION_OWNER_TYPE_ID = "1";
                                                            if(!sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_PERSONAL_GOV)
                                                                && !sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_ENTERPRISE_GOV)
                                                                && !sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_CLIENT)
                                                                && !sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_WEB_SERVER)
                                                                && !sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_AUTH)
                                                                && !sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_X_ROAD_SIGN)
                                                                && !sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING_GOV))
                                                            {
                                                                if(!"".equals(sEnterpriseOwnerID) && "".equals(sPersonalOwnerID)) {
                                                                    pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_ENTERPRISE);
                                                                }
                                                                if("".equals(sEnterpriseOwnerID) && !"".equals(sPersonalOwnerID)) {
                                                                    pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL);
                                                                }
                                                                if(!"".equals(sEnterpriseOwnerID) && !"".equals(sPersonalOwnerID)) {
                                                                    pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL);
                                                                }
                                                            } else {
                                                                if(sCertificateType.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_ENTERPRISE_GOV))
                                                                {
                                                                    pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_ENTERPRISE_GOV);
                                                                } else {
                                                                    pCERTIFICATION_OWNER_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_OWNER_TYPE_ID_PERSONAL_GOV);
                                                                }
                                                            }
                                                            String pMESSAGING_QUEUE_FUNCTION_ID = String.valueOf(Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_ID_REGISTRATION_OWNER);
                                                            String[] pRESPONSE_CODE_NAME = new String[1];
                                                            int[] pCERTIFICATION_OWNER_ID = new int[1];
                                                            int[] pMESSAGING_QUEUE_ID = new int[1];
                                                            String sLocation = CommonFunction.getLocationInDN(sDNResult).trim();
                                                            String pADDRESS;
                                                            if(!"".equals(sLocation)) {
                                                                pADDRESS = CommonFunction.replaceStringCharaterSpecialDN(sLocation, true, true) + ", " + CommonFunction.getStateOrProvinceInDN(sDNResult);
                                                            } else {
                                                                pADDRESS = CommonFunction.getStateOrProvinceInDN(sDNResult);
                                                            }
                                                            String pREPRESENTATIVE = "";
                                                            String pREPRESENTATIVE_POSITION = "";
                                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR OWNER ">
                                                            CERTIFICATION_OWNER_DATA_ATTR tempLogReq_Owner = new CERTIFICATION_OWNER_DATA_ATTR();
                                                            tempLogReq_Owner.personalName = sPersonalNameDB;
                                                            tempLogReq_Owner.companyName = sCompanyNameDB;
                                                            tempLogReq_Owner.taxCode = sEnterpriseOwnerID;
                                                            tempLogReq_Owner.personalCode = sPersonalOwnerID;
                                                            tempLogReq_Owner.emailContract = sCustomerEmail;
                                                            tempLogReq_Owner.phoneContract = sCustomerPhoneNumer;
                                                            tempLogReq_Owner.address = pADDRESS;
                                                            tempLogReq_Owner.representative = pREPRESENTATIVE;
                                                            tempLogReq_Owner.representativePosition = pREPRESENTATIVE_POSITION;
                                                            tempLogReq_Owner.typeName = Definitions.CONFIG_MESSAGING_QUEUE_FUNCTION_CODE_REGISTRATION_OWNER;
                                                            tempLogReq_Owner.requestState = Definitions.CONFIG_MESSAGING_QUEUE_STATE_CODE_PENDING;
                                                            tempLogReq_Owner.createUser = loginFullname + " (" + USER_LOG + ")";
                                                            tempLogReq_Owner.createDt = new Date();
                                                            //</editor-fold>

                                                            String sOwnerUUID = CommonFunction.getUUIDV4();
                                                            objectMapper = new ObjectMapper();
                                                            db.S_BO_CERTIFICATION_OWNER_INSERT(sPersonalNameDB, sCompanyNameDB,
                                                                sEnterpriseOwnerID, sPersonalOwnerID, pCERTIFICATION_OWNER_TYPE_ID, sCustomerPhoneNumer,
                                                                sCustomerEmail, USER_LOG, pADDRESS, pREPRESENTATIVE, pREPRESENTATIVE_POSITION,
                                                                pMESSAGING_QUEUE_FUNCTION_ID, objectMapper.writeValueAsString(tempLogReq_Owner),sOwnerUUID,
                                                                pRESPONSE_CODE_NAME, pCERTIFICATION_OWNER_ID, pMESSAGING_QUEUE_ID);
                                                            CommonFunction.LogDebugString(log, "CERTIFICATION_OWNER IMPORT Result", pRESPONSE_CODE_NAME[0]);
                                                            if (!"0".equals(pRESPONSE_CODE_NAME[0])) {
                                                                if (pRESPONSE_CODE_NAME[0].trim().equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_OWNER_EXISTS))
                                                                {
                                                                    hdfOwnerID = String.valueOf(pCERTIFICATION_OWNER_ID[0]);
                                                                } else {
                                                                    sParam = pRESPONSE_CODE_NAME[0];
                                                                }
                                                            } else {
                                                                hdfOwnerID = String.valueOf(pCERTIFICATION_OWNER_ID[0]);
                                                            }
                                                            //</editor-fold>
                                                            String sEnterpriseCert = "";
                                                            String sPersonalCert = "";
                                                            if(!"".equals(sPrefixUIDEnterprise) && !"".equals(sUIDEnterpriseDB)) {
                                                                sEnterpriseCert = CommonReferServlet.convertPrefixVNToEN(sPrefixUIDEnterprise + ":" + sUIDEnterpriseDB, true);
                                                            }
                                                            if(!"".equals(sPrefixUIDPersonal) && !"".equals(sUIDPersonalDB)) {
                                                                sPersonalCert = CommonReferServlet.convertPrefixVNToEN(sPrefixUIDPersonal + ":" + sUIDPersonalDB, false);
                                                            }
                                                            //<editor-fold defaultstate="collapsed" desc="### ATTR VALUE">
                                                            CERTIFICATION_DATA_ATTR tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                            objectMapper = new ObjectMapper();
                                                            tempLogReq.personalName = "";
                                                            tempLogReq.companyName = sCompanyNameDB;
                                                            tempLogReq.domainName = sDomainNameDB;
                                                            tempLogReq.enterpriseID = sEnterpriseCert;
                                                            tempLogReq.personalID = sPersonalCert;
                                                            tempLogReq.emailContract = sCustomerEmail;
                                                            tempLogReq.phoneContract = sCustomerPhoneNumer;
                                                            tempLogReq.issuerSubject = CACoreSubject;
                                                            tempLogReq.subjectDn = sDNResult;
                                                            tempLogReq.tokenSn = sTOKEN_SN;
                                                            tempLogReq.provinceName = pPROVINCE_DESC;
                                                            tempLogReq.pkiFromFactorCode = sMethod;
                                                            tempLogReq.typeName = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION;
                                                            String strReq = objectMapper.writeValueAsString(tempLogReq);
                                                            db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                                Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                                Definitions.CONFIG_LOG_FUNCTIONALITY_REQUEST_ISSUE, strReq,
                                                                USER_LOG, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                            ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                            dataATTR.setCertificationData(tempLogReq);
                                                            valueATTR.setTokenSn(sTOKEN_SN);
                                                            valueATTR.setChangeKeyEnabled(true);
                                                            valueATTR.setTypeName(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_REGISTRATION);
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                            valueATTR.setCreateUser(loginFullname + " (" + USER_LOG + ")");
                                                            valueATTR.setCreateDt(new Date());
                                                            valueATTR.setAttributeData(dataATTR);
                                                            //</editor-fold>

                                                            //<editor-fold defaultstate="collapsed" desc="### CALL STORE IMPORT">
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            if(sParam.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS))
                                                            {
                                                                if(sMethod.equals(Definitions.CONFIG_PKI_FORMFACTOR_CODE_SOFT_TOKEN))
                                                                {
                                                                    String sSQLParam = db.S_BO_REGISTER_CERTIFICATION_IMPORT(Integer.parseInt(sTOKEN_ID), sCertificateProfile, sTOKEN_SN,
                                                                        sPersonalNameDB, sCompanyNameDB, sDomainNameDB, "", "", "",
                                                                        "", sDNResult, CACoreSubject, sCustomerPhoneNumer,
                                                                        sCustomerEmail, sStateProvince, "", strReqValueATTR, sBeneficiaryUser,
                                                                        USER_LOG, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, sCSR, CheckPRIVATE_KEY, sMethod,
                                                                        sCertificateType, Integer.parseInt(sCERTIFICATION_AUTHORITY),hdfOwnerID, "",
                                                                        "", sEnterpriseCert, sPersonalCert);
                                                                    if(!"0".equals(sSQLParam)) {
                                                                        sParam = sSQLParam;
                                                                    }
                                                                } else if(CommonFunction.checkHardTokenEnabled(sMethod) == true)
                                                                {
                                                                    String sSQLParam = "1000";
                                                                    while ("1000".equals(sSQLParam)) {
                                                                        try {
                                                                            String sOTP = CommonFunction.getRandomOTP(intOTPNumn);
                                                                            sSQLParam = db.S_BO_REGISTER_CERTIFICATION_IMPORT(Integer.parseInt(sTOKEN_ID), sCertificateProfile, sTOKEN_SN,
                                                                                sPersonalNameDB, sCompanyNameDB, sDomainNameDB, "", "", "", "",
                                                                                sDNResult, CACoreSubject, sCustomerPhoneNumer, sCustomerEmail, sStateProvince, sOTP, strReqValueATTR, sBeneficiaryUser,
                                                                                USER_LOG, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, sCSR, CheckPRIVATE_KEY, sMethod,
                                                                                sCertificateType, Integer.parseInt(sCERTIFICATION_AUTHORITY),hdfOwnerID, "", "", sEnterpriseCert, sPersonalCert);
                                                                            if(!"0".equals(sSQLParam)) {
                                                                                sParam = sSQLParam;
                                                                            }
                                                                        } catch (Exception e) {
                                                                            if (e.getMessage().contains(Definitions.CONFIG_MYSQL_UNIQUE_ACTIVATION_CODE)) {
                                                                                sSQLParam = "1000";
                                                                            } else {
                                                                                sParam = "FAILED_INSERT";
                                                                                CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    sParam = "METHOD_INVALID";
                                                                }
                                                            }
                                                            //</editor-fold>
                                                            if (null == sParam) {
                                                                sVALID_CODE = sParam;
                                                            } else switch (sParam) {
                                                                case "0":
                                                                    sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                                    break;
                                                                case "1":
                                                                    sVALID_CODE = "CERT_PEDDING";
                                                                    break;
                                                                case "8":
                                                                    sVALID_CODE = "PROFILE_INVALID";
                                                                    break;
                                                                case "9":
                                                                    sVALID_CODE = "PROVINCE_INVALID";
                                                                    break;
                                                                case "99":
                                                                    sVALID_CODE = "BENEFICIARYUSER_INVALID";
                                                                    break;
                                                                default:
                                                                    sVALID_CODE = sParam;
                                                                    break;
                                                            }
                                                            if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE)) {
                                                                if(!"".equals(sSANProperties)) {
                                                                    db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(pCERTIFICATE_ID[0]), sSANProperties, sBeneficiaryUser);
                                                                }
                                                                //<editor-fold defaultstate="collapsed" desc="### APPROVE - ENROLL CERT">
                                                                if(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS.equals(sVALID_CODE)) {
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                    valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                    valueATTR.setApproveDt(new Date());
                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                    db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                    if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD))
                                                                    {
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                        valueATTR.setApproveCAUser(loginFullname + " (" + USER_LOG + ")");
                                                                        valueATTR.setApproveCADt(new Date());
                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                        CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                                                        String pCERTIFICATION_ID = "";
                                                                        db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sessLanguageGlobal, rsReq);
                                                                        if (rsReq[0].length > 0) {
                                                                            pCERTIFICATION_ID = String.valueOf(rsReq[0][0].ID);
                                                                            db.S_BO_CERTIFICATION_UPDATE_AMOUNT(rsReq[0][0].ID, "", pPUSH_NOTICE_ENABLED, USER_LOG);
                                                                        }
                                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                        if ("0".equals(sApprove))
                                                                        {
                                                                            if ("1".equals(sDiscountRateOption)) {
                                                                                CommonReferServlet.updateDiscountRateImportCert(pCERTIFICATION_ID, sBeneficiaryUser, sCertificateProfile,
                                                                                    "", "", "", "", USER_LOG, "", "", sEnterpriseCert, sPersonalCert);
                                                                            }
                                                                            if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID)))
                                                                            {
                                                                                String isActiveSignServer = "0";
                                                                                GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                if (sessGeneralPolicy1[0].length > 0) {
                                                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                    {
                                                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                            isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if("1".equals(isActiveSignServer)) {
                                                                                    CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), sDNResult, sCustomerEmail, sessLanguageGlobal);
                                                                                } else {
                                                                                    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                    dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                    int[] intWSRes = new int[1];
                                                                                    String[] sWSRes = new String[1];
                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                    if (intWSRes[0] != 0) {
                                                                                        sVALID_CODE = "ISSUE_ERROR";
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_UNASSIGN_ID)))
                                                                                {
                                                                                    if ("1".equals(pPUSH_NOTICE_ENABLED)) {
                                                                                        int[] intRes = new int[1];
                                                                                        String[] sRes = new String[1];
                                                                                        ConnectConnector.SendMailOTP(pCERTIFICATION_ID, intRes, sRes);
                                                                                    }
                                                                                }
                                                                            }
                                                                        } else {
                                                                            sVALID_CODE = "APPROVE_ERROR";
                                                                        }
                                                                    } else {
                                                                        ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                        {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                            valueATTR.setApproveCAUser(loginFullname + " (" + USER_LOG + ")");
                                                                            valueATTR.setApproveCADt(new Date());
                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                            CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                                                            String pCERTIFICATION_ID = "";
                                                                            db.S_BO_CERTIFICATION_APPROVED_DETAIL(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sessLanguageGlobal, rsReq);
                                                                            if (rsReq[0].length > 0) {
                                                                                pCERTIFICATION_ID = String.valueOf(rsReq[0][0].ID);
                                                                                db.S_BO_CERTIFICATION_UPDATE_AMOUNT(rsReq[0][0].ID, "", pPUSH_NOTICE_ENABLED, USER_LOG);
                                                                            }
                                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                            if ("0".equals(sApprove)) {
                                                                                if ("1".equals(sDiscountRateOption)) {
                                                                                    CommonReferServlet.updateDiscountRateImportCert(pCERTIFICATION_ID, sBeneficiaryUser, sCertificateProfile,
                                                                                        "", "", "", "", USER_LOG, "", "", sEnterpriseCert, sPersonalCert);
                                                                                }
                                                                                if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                    || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                    || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID)))
                                                                                {
                                                                                    String isActiveSignServer = "0";
                                                                                    GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                    if (sessGeneralPolicy1[0].length > 0) {
                                                                                        for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                        {
                                                                                            if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                                isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if("1".equals(isActiveSignServer)) {
                                                                                        CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), sDNResult, sCustomerEmail, sessLanguageGlobal);
                                                                                    } else {
                                                                                        ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                        dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                        int[] intWSRes = new int[1];
                                                                                        String[] sWSRes = new String[1];
                                                                                        ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                        if (intWSRes[0] != 0) {
                                                                                            sVALID_CODE = "ISSUE_ERROR";
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_UNASSIGN_ID)))
                                                                                    {
                                                                                        if ("1".equals(pPUSH_NOTICE_ENABLED)) {
                                                                                            int[] intRes = new int[1];
                                                                                            String[] sRes = new String[1];
                                                                                            ConnectConnector.SendMailOTP(pCERTIFICATION_ID, intRes, sRes);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                sVALID_CODE = "APPROVE_ERROR";
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                //</editor-fold>
                                                            }
                                                        }
                                                    } else {
                                                        sVALID_CODE = "DN_INVALID";
                                                    }
                                                }
                                                //<editor-fold defaultstate="collapsed" desc="### RESPONSE CODE">
                                                if(null != sVALID_CODE)
                                                switch (sVALID_CODE) {
                                                    case "OK":
                                                        success = success + 1;
                                                        break;
                                                    case "CERT_PEDDING":
                                                        failed = failed + 1;
                                                        failedCERT_PEDDING = failedCERT_PEDDING + 1;
                                                        sFailCERT_PEDDING = sFailCERT_PEDDING + sSTT +",";
                                                        break;
                                                    case "FAILED_INSERT":
                                                        failed = failed + 1;
                                                        failedFAILED_INSERT = failedFAILED_INSERT + 1;
                                                        sFailFAILED_INSERT = sFailFAILED_INSERT + sSTT +",";
                                                        break;
                                                    case "BENEFICIARYUSER_INVALID":
                                                        failed = failed + 1;
                                                        failedBENEFICIARYUSER_INVALID = failedBENEFICIARYUSER_INVALID + 1;
                                                        sFailBENEFICIARYUSER_INVALID = sFailBENEFICIARYUSER_INVALID + sSTT +",";
                                                        break;
                                                    case "COMPONENT_INVALID":
                                                        failed = failed + 1;
                                                        failedCOMPONENT_INVALID= failedCOMPONENT_INVALID + 1;
                                                        sFailCOMPONENT_INVALID = sFailCOMPONENT_INVALID + sSTT +",";
                                                        break;
                                                    case "DN_INVALID":
                                                        failed = failed + 1;
                                                        failedDN_INVALID = failedDN_INVALID + 1;
                                                        sFailDN_INVALID= sFailDN_INVALID + sSTT +",";
                                                        break;
                                                    case "PROFILE_INVALID":
                                                        failed = failed + 1;
                                                        failedPROFILE_INVALID = failedPROFILE_INVALID + 1;
                                                        sFailPROFILE_INVALID= sFailPROFILE_INVALID + sSTT +",";
                                                        break;
                                                    case "PHONE_INVALID":
                                                        failed = failed + 1;
                                                        failedPHONE_INVALID = failedPHONE_INVALID + 1;
                                                        sFailPHONE_INVALID= sFailPHONE_INVALID + sSTT +",";
                                                        break;
                                                    case "EMAIL_INVALID":
                                                        failed = failed + 1;
                                                        failedEMAIL_INVALID = failedEMAIL_INVALID + 1;
                                                        sFailEMAIL_INVALID= sFailEMAIL_INVALID + sSTT +",";
                                                        break;
                                                    case "PREFIX_INVALID":
                                                        failed = failed + 1;
                                                        failedPREFIX_INVALID = failedPREFIX_INVALID + 1;
                                                        sFailPREFIX_INVALID= sFailPREFIX_INVALID + sSTT +",";
                                                        break;
                                                    case "ISSUE_ERROR":
                                                        failed = failed + 1;
                                                        failedISSUE_ERROR = failedISSUE_ERROR + 1;
                                                        sFailISSUE_ERROR = sFailISSUE_ERROR + sSTT +",";
                                                        break;
                                                    case "APPROVE_ERROR":
                                                        failed = failed + 1;
                                                        failedAPPROVE_ERROR = failedAPPROVE_ERROR + 1;
                                                        sFailAPPROVE_ERROR = sFailAPPROVE_ERROR + sSTT +",";
                                                        break;
                                                    case "PROVINCE_INVALID":
                                                        failed = failed + 1;
                                                        failedPROVINCE_INVALID = failedPROVINCE_INVALID + 1;
                                                        sFailPROVINCE_INVALID= sFailPROVINCE_INVALID + sSTT +",";
                                                        break;
                                                    case "CSR_INVALID":
                                                        failed = failed + 1;
                                                        failedCSR = failedCSR + 1;
                                                        sFailCSR = sFailCSR + sSTT +",";
                                                        break;
                                                    case "CSR_KEY":
                                                        failed = failed + 1;
                                                        failedKeyCSR = failedKeyCSR + 1;
                                                        sFailKeyCSR = sFailKeyCSR + sSTT +",";
                                                        break;
                                                    case "METHOD_INVALID":
                                                        failed = failed + 1;
                                                        failedMethod = failedMethod + 1;
                                                        sFailMethod = sFailMethod + sSTT +",";
                                                        break;
                                                    case "CERTIFICATETYPE_INVALID":
                                                        failed = failed + 1;
                                                        failedCertType = failedCertType + 1;
                                                        sFailCertType = sFailCertType + sSTT +",";
                                                        break;
                                                    case "SPECIAL_INVALID":
                                                        failed = failed + 1;
                                                        failedSpecialWord = failedSpecialWord + 1;
                                                        sFailSpecialWord = sFailSpecialWord + sSTT +",";
                                                        break;
                                                    case "2052":
                                                        failed = failed + 1;
                                                        failedTypeOwnerInvalid = failedTypeOwnerInvalid + 1;
                                                        sFailTypeOwnerInvalid = sFailTypeOwnerInvalid + sSTT +",";
                                                        break;
                                                    case "2053":
                                                        failed = failed + 1;
                                                        failedOwner_incomplete = failedOwner_incomplete + 1;
                                                        sFailOwner_incomplete = sFailOwner_incomplete + sSTT +",";
                                                        break;
                                                    default:
                                                        break;
                                                }
//                                                  //</editor-fold>
                                            }
                                            String sSumShow = "";
                                            //<editor-fold defaultstate="collapsed" desc="### RETURN">
                                            if (failed != 0)
                                            {
                                                sSumShow = "1";
                                                String sSum="";
                                                if(failedCERT_PEDDING != 0) {
                                                    sSum = sSum + "The Certificate Is pedding in system, import failure: " + sColumnSTT + "- " + sFailCERT_PEDDING + "\n";
                                                }
                                                if(failedBENEFICIARYUSER_INVALID != 0){
                                                    sSum = sSum + "Beneficiaty User Is Invalid: " + sColumnSTT + "- " + sFailBENEFICIARYUSER_INVALID + "\n";
                                                }
                                                if(failedFAILED_INSERT != 0){
                                                    sSum = sSum + "Registration error certificate: " + sColumnSTT + "- " + sFailFAILED_INSERT + "\n";
                                                }
//                                                    if(failedCERT_PEDDING != 0) {
//                                                        sSum = sSum + "The Agency Is Invalid: " + sColumnSTT + "- " + sFailCERT_PEDDING + "\n";
//                                                    }
                                                if(failedCOMPONENT_INVALID != 0) {
                                                    sSum = sSum + "Certificate Information Is Invalid: " + sColumnSTT + "- " + sFailCOMPONENT_INVALID + "\n";
                                                }
                                                if(failedDN_INVALID != 0) {
                                                    sSum = sSum + "Output string Subject DN Error: " + sColumnSTT + "- " + sFailDN_INVALID + "\n";
                                                }
                                                if(failedPROFILE_INVALID != 0) {
                                                    sSum = sSum + "Certificate Profile Is Invalid: " + sColumnSTT + "- " + sFailPROFILE_INVALID + "\n";
                                                }
                                                if(failedPHONE_INVALID != 0) {
                                                    sSum = sSum + "Customer Phone Number Is Invalid: " + sColumnSTT + "- " + sFailPHONE_INVALID + "\n";
                                                }
                                                if(failedEMAIL_INVALID != 0) {
                                                    sSum = sSum + "Customer Email Is Invalid: " + sColumnSTT + "- " + sFailEMAIL_INVALID + "\n";
                                                }
                                                if(failedPREFIX_INVALID != 0) {
                                                    sSum = sSum + "Prefix UID Is Invalid: " + sColumnSTT + "- " + sFailPREFIX_INVALID + "\n";
                                                }
                                                if(failedAPPROVE_ERROR != 0) {
                                                    sSum = sSum + "Approve CA certificates Is Errors: " + sColumnSTT + "- " + sFailAPPROVE_ERROR + "\n";
                                                }
                                                if(failedISSUE_ERROR != 0) {
                                                    sSum = sSum + "Issuing a certificate of error: " + sColumnSTT + "- " + sFailISSUE_ERROR + "\n";
                                                }
                                                if(failedPROVINCE_INVALID != 0) {
                                                    sSum = sSum + "Province Code Is Invalid: " + sColumnSTT + "- " + sFailPROVINCE_INVALID + "\n";
                                                }
                                                if(failedCSR != 0) {
                                                    sSum = sSum + "CSR Is Invalid: " + sColumnSTT + "- " + sFailCSR + "\n";
                                                }
                                                if(failedKeyCSR != 0) {
                                                    sSum = sSum + "Key Size Is Invalid: " + sColumnSTT + "- " + sFailKeyCSR + "\n";
                                                }
                                                if(failedMethod != 0) {
                                                    sSum = sSum + "Method Is Invalid: " + sColumnSTT + "- " + sFailMethod + "\n";
                                                }
                                                if(failedCertType != 0) {
                                                    sSum = sSum + "Certificate Type Is Invalid: " + sColumnSTT + "- " + sFailCertType + "\n";
                                                }
                                                if(failedSpecialWord != 0) {
                                                    sSum = sSum + "The certificate information is not allowed to include special characters: \\ =: " + sColumnSTT + "- " + sFailSpecialWord + "\n";
                                                }
                                                if(failedTypeOwnerInvalid != 0) {
                                                    sSum = sSum + "Invalid information for this type of owner: " + sColumnSTT + "- " + sFailTypeOwnerInvalid + "\n";
                                                }
                                                if(failedOwner_incomplete != 0) {
                                                    sSum = sSum + "Certification owner has a queue incomplete: " + sColumnSTT + "- " + sFailOwner_incomplete + "\n";
                                                }
                                                sessionsa.setAttribute("sessRegisterCertImportFailed", sSum);
                                                CommonFunction.LogDebugString(log, "sessRegisterCertImportFailed-1", sSum);
                                            }
                                            //</editor-fold>
                                            strView = "0###" + String.valueOf(success) + "###" + String.valueOf(failed) + "###" + sSumShow;
                                        } else {
                                            strView = "1###" + sValueFailColumnName;
                                        }
                                        //</editor-fold>
                                    } else if(Integer.parseInt(sCERTIFICATION_TYPEParam) == Definitions.CONFIG_SERVICE_TYPE_ID_RENEWAL) {
                                        //<editor-fold defaultstate="collapsed" desc="### RENEW">
                                        //<editor-fold defaultstate="collapsed" desc="### GET COLUMN - CHECK COLUMN">
                                        for (int i = 0; i < cellStoreArrayList.size(); i++) {
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnSTT)) {
                                                indexOfSTT = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCertificateSN)) {
                                                indexOfCertificateSN = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCertificateProfile)) {
                                                indexOfCertificateProfile = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnBeneficiaryUser)) {
                                                indexOfBeneficiaryUser = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnBackupKeyEnabled)) {
                                                indexOfBackupKeyEnabled = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnRevokeOldCertificateEnabled)) {
                                                indexOfRevokeOldCertificateEnabled = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnChangeKeyEnabled)) {
                                                indexOfChangeKeyEnabled = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnDeleteCertificateEnabled)) {
                                                indexOfDeleteCertificateEnabled = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnKeepCertificateSNEnabled)) {
                                                indexOfKeepCertificateSNEnabled = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCSR)) {
                                                indexOfCSR = i;
                                            }
                                        }
                                        boolean booFailColumnName = true;
                                        String sValueFailColumnName = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                        if (indexOfCertificateSN == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCertificateProfile == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfBeneficiaryUser == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfBackupKeyEnabled == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfRevokeOldCertificateEnabled == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfChangeKeyEnabled == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfDeleteCertificateEnabled == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfKeepCertificateSNEnabled == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCSR == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        //</editor-fold>

                                        if (booFailColumnName == true) {
                                            for (int i = 1; i < dataHolder.size(); i++) {
                                                String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                cellStoreArrayList = (ArrayList) dataHolder.get(i);
                                                String sSTT = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfSTT).toString(), true);
                                                String sCertificateSN = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCertificateSN).toString(), false);
                                                String sCertificateProfile = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCertificateProfile).toString(), false);
                                                String pCREATE_USER = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfBeneficiaryUser).toString(), false);
                                                String sBackupKeyEnabled = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfBackupKeyEnabled).toString(), false);
                                                String sRevokeOldCertificateEnabled = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfRevokeOldCertificateEnabled).toString(), false);
                                                String sChangeKeyEnabled = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfChangeKeyEnabled).toString(), false);
                                                String sDeleteCertificateEnabled = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfDeleteCertificateEnabled).toString(), false);
                                                String sKeepCertificateSNEnabled = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfKeepCertificateSNEnabled).toString(), false);
                                                String sCSR = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCSR).toString(), false);
                                                sBackupKeyEnabled = "".equals(sBackupKeyEnabled) ? "FALSE" : sBackupKeyEnabled;
                                                if("TRUE".equals(sBackupKeyEnabled)){sBackupKeyEnabled = "1";} else {sBackupKeyEnabled="0";}
                                                sRevokeOldCertificateEnabled = "".equals(sRevokeOldCertificateEnabled) ? "FALSE" : sRevokeOldCertificateEnabled;
                                                if("TRUE".equals(sRevokeOldCertificateEnabled)){sRevokeOldCertificateEnabled = "1";} else {sRevokeOldCertificateEnabled="0";}
                                                sChangeKeyEnabled = "".equals(sChangeKeyEnabled) ? "0" : sChangeKeyEnabled;
                                                if("TRUE".equals(sChangeKeyEnabled)){sChangeKeyEnabled = "1";} else {sChangeKeyEnabled="0";}
                                                sDeleteCertificateEnabled = "".equals(sDeleteCertificateEnabled) ? "FALSE" : sDeleteCertificateEnabled;
                                                if("TRUE".equals(sDeleteCertificateEnabled)){sDeleteCertificateEnabled = "1";} else {sDeleteCertificateEnabled="0";}
                                                sKeepCertificateSNEnabled = "".equals(sKeepCertificateSNEnabled) ? "FALSE" : sKeepCertificateSNEnabled;
                                                if("TRUE".equals(sKeepCertificateSNEnabled)){sKeepCertificateSNEnabled = "1";} else {sKeepCertificateSNEnabled="0";}
                                                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                boolean isAccessAgency = true;
                                                boolean isProcess = true;
                                                String sTOKEN_ID = "";
                                                String sTOKEN_SN = "";
                                                String PHONE_CONTRACT = "";
                                                String EMAIL_CONTRACT = "";
                                                String DN = "";
                                                String pPERSONAL_NAME = "";
                                                String pCOMPANY_NAME = "";
                                                String pDOMAIN_NAME = "";
                                                String pTAX_CODE = "";
                                                String pDECISION = "";
                                                String pBUDGET_CODE = "";
                                                String pP_ID = "";
                                                String pCCCD = "";
                                                String pPASSPORT = "";
                                                String pCSR = "";
                                                String pDEVICE = "";
                                                String pCERTIFICATION_SN = "";
                                                String pPRIVATE_KEY = "";
                                                String pPROVINCE_ID = "";
                                                String sAGENT_ID = "";
                                                String sAGENT_ID_OLD = "";
                                                String pCOMPONENT_SAN = "";
                                                String sMethod = "";
                                                String CertProfileID = "";
                                                String sCERT_PROFILE_PROPERTIES="";
                                                String sCERT_POLICY_PROPERTIES="";
                                                String sEnterpriseCert = "";
                                                String sPersonalCert = "";
                                                int pPKI_FORMFACTOR_ID = 0;
                                                int pCERTIFICATION_OWNER_ID = 0;
                                                int pCERTIFICATION_PURPOSE_ID_OLD = 0;
                                                int certID = 0;
                                                boolean pPRIVATE_KEY_ENABLED = true;
                                                BRANCH[][] rsBranch;
                                                String pCERTIFICATION_AUTHORITY_CODE = "";
                                                String pCERTIFICATION_PURPOSE_CODE = "";
                                                if("".equals(sCertificateSN) || "".equals(sCertificateProfile)) {
                                                    sVALID_CODE = "PROFILE_INVALID";
                                                } else {
                                                    CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                    db.S_BO_API_CERTIFICATION_PROFILE_GET_INFO(sCertificateProfile, rsProfile);
                                                    if(rsProfile[0].length <= 0) {
                                                        sVALID_CODE = "PROFILE_INVALID";
                                                    } else {
                                                        pCERTIFICATION_AUTHORITY_CODE = rsProfile[0][0].CERTIFICATION_AUTHORITY_NAME;
                                                        CertProfileID = String.valueOf(rsProfile[0][0].ID);
                                                        //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                                        CertificateInfo[][] rsReqID = new CertificateInfo[1][];
                                                        int[] pRESPONSE_CODE = new int[1];
                                                        db.S_BO_API_CERTIFICATION_GET_INFO("", "", "", "", sCertificateSN, 0,
                                                            "", "", Integer.parseInt(sessLanguageGlobal), pRESPONSE_CODE, rsReqID, "", "", "", "");
                                                        if (rsReqID[0].length > 0) {
                                                            for(CertificateInfo rsReqItem : rsReqID[0]) {
                                                                if(rsReqItem.certificateStateId == Definitions.CONFIG_CERTIFICATION_STATE_OPERATED
                                                                    || rsReqItem.certificateStateId == Definitions.CONFIG_CERTIFICATION_STATE_EXPIRED)
                                                                {
                                                                    certID = rsReqItem.certificateID;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        if(certID == 0) {
                                                            sVALID_CODE = "CERT_INVALID";
                                                        } else {
                                                            CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                                            db.S_BO_CERTIFICATION_DETAIL(String.valueOf(certID), sessLanguageGlobal, rsReq);
                                                            if (rsReq[0].length > 0) {
                                                                PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                                                EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                                                pCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                                                DN = EscapeUtils.CheckTextNull(rsReq[0][0].SUBJECT);
                                                                pPERSONAL_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                                                pCOMPANY_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                                                pDOMAIN_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].DOMAIN_NAME);
                                                                pTAX_CODE = EscapeUtils.CheckTextNull(rsReq[0][0].TAX_CODE);
                                                                sEnterpriseCert = rsReq[0][0].ENTERPRISE_ID;
                                                                sPersonalCert = rsReq[0][0].PERSONAL_ID;
                                                                pDECISION = EscapeUtils.CheckTextNull(rsReq[0][0].DECISION);
                                                                pBUDGET_CODE = EscapeUtils.CheckTextNull(rsReq[0][0].BUDGET_CODE);
                                                                pP_ID = EscapeUtils.CheckTextNull(rsReq[0][0].P_ID);
                                                                pCCCD = EscapeUtils.CheckTextNull(rsReq[0][0].P_EID);
                                                                pPASSPORT = EscapeUtils.CheckTextNull(rsReq[0][0].PASSPORT);
                                                                pDEVICE = EscapeUtils.CheckTextNull(rsReq[0][0].SERVICE_UUID);
                                                                sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                                                pCOMPONENT_SAN = EscapeUtils.CheckTextNull(rsReq[0][0].PROPERTIES);
                                                                sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                                                sAGENT_ID_OLD = String.valueOf(rsReq[0][0].BRANCH_ID);
                                                                sTOKEN_ID = String.valueOf(rsReq[0][0].TOKEN_ID);
                                                                pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                                                pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                                                pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                                                pCERTIFICATION_PURPOSE_ID_OLD = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                                                CERTIFICATION_PURPOSE[][] rsPurpose = new CERTIFICATION_PURPOSE[1][];
                                                                db.S_BO_CERTIFICATION_PURPOSE_DETAIL(String.valueOf(pCERTIFICATION_PURPOSE_ID_OLD), rsPurpose);
                                                                if(rsPurpose[0].length > 0) {
                                                                    pCERTIFICATION_PURPOSE_CODE = EscapeUtils.CheckTextNull(rsPurpose[0][0].NAME);
                                                                }
                                                                pPROVINCE_ID = String.valueOf(rsReq[0][0].CITY_PROVINCE_ID);
                                                                sMethod = rsReq[0][0].PKI_FORMFACTOR_NAME;
                                                                if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                    BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                                    isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                                                    if(isAccessAgency == true) {
                                                                        rsBranch = new BRANCH[1][];
                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                        if(rsBranch[0].length > 0) {
                                                                            String sResponseCheckBranch = db.S_BO_API_CHECK_USERNAME_AND_BRANCH_CODE(rsBranch[0][0].NAME, pCREATE_USER);
                                                                            if("1".equals(sResponseCheckBranch)) {
                                                                                sVALID_CODE = "BENEFICIARYUSER_INVALID";
                                                                            }
                                                                        }
                                                                    } else {
                                                                        sVALID_CODE = "AGENCY_DENIED";
                                                                    }
                                                                } else {
                                                                    BACKOFFICE_USER[][] rsUser = new BACKOFFICE_USER[1][];
                                                                    db.S_BO_USER_GET_BY_USERNAME(pCREATE_USER, rsUser);
                                                                    if(rsUser[0].length > 0) {
                                                                        sAGENT_ID = String.valueOf(rsUser[0][0].BRANCH_ID);
                                                                    } else {
                                                                        sVALID_CODE = "BENEFICIARYUSER_INVALID";
                                                                    }
                                                                }
                                                            } else {
                                                                sVALID_CODE = "AGENCY_DENIED";
                                                            }
                                                        }
                                                        //</editor-fold>
                                                    }
                                                }

                                                //<editor-fold defaultstate="collapsed" desc="### CHECK PROFILE">
                                                if (sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                    rsBranch = new BRANCH[1][];
                                                    db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                    if(rsBranch[0].length > 0) {
                                                        sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                                                        sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                                                    }
                                                    if(!"".equals(sCERT_PROFILE_PROPERTIES) && !"".equals(sCERT_POLICY_PROPERTIES)) {
                                                        ArrayList<CERTIFICATION_POLICY_DATA> tempProfileList = new ArrayList<>();
                                                        CERTIFICATION_POLICY_DATA[][] resPolicyData_Old = new CERTIFICATION_POLICY_DATA[1][];
                                                        CommonFunction.getProfileCertList(sCERT_PROFILE_PROPERTIES, resPolicyData_Old);
                                                        for(CERTIFICATION_POLICY_DATA resPolicyCertData_Old1 : resPolicyData_Old[0])
                                                        {
                                                            if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                                            {
                                                                CERTIFICATION_PROFILE[][] resProfileDB = new CERTIFICATION_PROFILE[1][];
                                                                db.S_BO_API_CERTIFICATION_PROFILE_GET_INFO(EscapeUtils.CheckTextNull(resPolicyCertData_Old1.name), resProfileDB);
                                                                if(resProfileDB[0].length > 0)
                                                                {
                                                                    CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                                                    itemProfileAccess.name = resProfileDB[0][0].NAME;
                                                                    itemProfileAccess.certificateAuthority = resProfileDB[0][0].CERTIFICATION_AUTHORITY_NAME;
                                                                    itemProfileAccess.certificatePurpose = resProfileDB[0][0].CERTIFICATION_PURPOSE_NAME;
                                                                    itemProfileAccess.remark = resPolicyCertData_Old1.remark;
                                                                    itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
                                                                    itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST;
                                                                    tempProfileList.add(itemProfileAccess);
                                                                }
                                                            }
                                                        }

                                                        CertificateProfileInfo[][] rsProfileCheck = new CertificateProfileInfo[1][];
                                                        db.S_BO_API_CERTIFICATION_PROFILE_LIST(pCERTIFICATION_AUTHORITY_CODE, pCERTIFICATION_PURPOSE_CODE,
                                                            sMethod, 1, Integer.parseInt(sessLanguageGlobal), rsProfileCheck, sCERT_PROFILE_PROPERTIES, tempProfileList);
                                                        if(rsProfileCheck[0].length > 0) {
                                                            for(CertificateProfileInfo rsProfileCheck1 : rsProfileCheck[0]) {
                                                                if(sCertificateProfile.equals(rsProfileCheck1.certificateProfileCode)) {
                                                                    sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        sVALID_CODE = "AGENCY_NOT_PROFILE";
                                                    }
                                                }
                                                //</editor-fold>

                                                if (sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                    Config conf = new Config();
                                                    boolean booRSSP_ACCESS_ENABLED = false;
                                                    if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                        String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                                        if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                            booRSSP_ACCESS_ENABLED = true;
                                                        }
                                                    }
                                                    if(booRSSP_ACCESS_ENABLED == false) {
                                                        String strPasswordP12 = "";
                                                        boolean isCSRValid = true;
                                                        if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN)
                                                        {
                                                            if(pPRIVATE_KEY_ENABLED == true)
                                                            {
                                                                sBackupKeyEnabled = "1";
                                                                strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                                            } else {
                                                                if("1".equals(sChangeKeyEnabled))
                                                                {
                                                                    pCSR = sCSR;
                                                                    if("".equals(pCSR))
                                                                    {
                                                                        isCSRValid = false;
                                                                    }
                                                                }
                                                                sBackupKeyEnabled = "0";
                                                            }
                                                            sDeleteCertificateEnabled = "0";
                                                        }
                                                        if(isCSRValid == true)
                                                        {
                                                            int[] pCERTIFICATE_ATTR_ID = new int[1];
                                                            int[] pCERTIFICATE_ID = new int[1];
                                                            ObjectMapper objectMapper;
                                                            CERTIFICATION_DATA_ATTR tempLogReq;
                                                            String pPAST_CERTIFICATE_ID = String.valueOf(certID);
                                                            String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_RENEWAL);
                                                            String pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_RENEWAL;
                                                            String CACoreSubject = "";
                                                            CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                                            db.S_BO_API_CERTIFICATION_AUTHORITY_LIST_BY_NAME(pCERTIFICATION_AUTHORITY_CODE, Integer.parseInt(sessLanguageGlobal), rsCA);
                                                            if(rsCA[0].length > 0){
                                                                CACoreSubject = rsCA[0][0].CERTIFICATION_AUTHORITY_CORECA_SUBJECT;
                                                            }

                                                            //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                                            tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                            objectMapper = new ObjectMapper();
                                                            tempLogReq.personalName = pPERSONAL_NAME;
                                                            tempLogReq.companyName = pCOMPANY_NAME;
                                                            tempLogReq.taxCode = pTAX_CODE;
                                                            tempLogReq.decision = pDECISION;
                                                            tempLogReq.budgetCode = pBUDGET_CODE;
                                                            tempLogReq.personalCode = pP_ID;
                                                            tempLogReq.citizenCode = pCCCD;
                                                            tempLogReq.deviceUUID = pDEVICE;
                                                            tempLogReq.phoneContract = PHONE_CONTRACT;
                                                            tempLogReq.emailContract = EMAIL_CONTRACT;
                                                            tempLogReq.issuerSubject = CACoreSubject;
                                                            tempLogReq.subjectDn = DN;
                                                            tempLogReq.tokenSn = sTOKEN_SN;
                                                            tempLogReq.pkiFromFactorId = pPKI_FORMFACTOR_ID;
                                                            tempLogReq.typeName = pCERT_ATTR_TYPE_CODE;
                                                            String strReq = objectMapper.writeValueAsString(tempLogReq);
                                                            db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                                    Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                                    Definitions.CONFIG_LOG_FUNCTIONALITY_REQUEST_RENEWAL, strReq,
                                                                    USER_LOG, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                            CommonFunction.LogDebugString(log, "RENEWAL-CERTIFICATION", "REQUEST: " + "SUBJECT: " + DN
                                                                    + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME
                                                                    + "; sEnterpriseCert: " + sEnterpriseCert + "; sPersonalCert: " + sPersonalCert + "; pDEVICE: " + pDEVICE + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                                    + "; pCERTIFICATION_ATTR_TYPE_ID: " + pCERTIFICATION_ATTR_TYPE_ID
                                                                    + "; pPKI_FORMFACTOR_ID: " + pPKI_FORMFACTOR_ID
                                                                    + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                                    + "; CACoreSubject: " + CACoreSubject + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                                    + "; TOKEN_SN: " + sTOKEN_SN + "; PROVINCE_ID: " + pPROVINCE_ID
                                                                    + "; CheckDeleteCertificate: " + sDeleteCertificateEnabled);
                                                            ATTRIBUTE_VALUES valueATTR;
                                                            ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                            dataATTR.setCertificationData(tempLogReq);
                                                            valueATTR = new ATTRIBUTE_VALUES();
                                                            valueATTR.setTokenSn(sTOKEN_SN);
                                                            boolean booChangeKeyEnabled = "1".equals(sChangeKeyEnabled);
                                                            valueATTR.setChangeKeyEnabled(booChangeKeyEnabled);
                                                            boolean sDeleteInTokenEnabled = "1".equals(sDeleteCertificateEnabled);
                                                            valueATTR.setDeleteOldCertificateEnabled(sDeleteInTokenEnabled);
                                                            boolean booKeepCertSNEnabled = "1".equals(sKeepCertificateSNEnabled);
                                                            valueATTR.setKeepCertificateSNEnabled(booKeepCertSNEnabled);
                                                            valueATTR.setTypeName(pCERT_ATTR_TYPE_CODE);
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                            valueATTR.setCreateUser(loginFullname + " (" + USER_LOG + ")");
                                                            valueATTR.setCreateDt(new Date());
                                                            valueATTR.setAttributeData(dataATTR);
                                                            //</editor-fold>
                                                            String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            String pCREATE_USERID = "";
                                                            BACKOFFICE_USER[][] rsUser = new BACKOFFICE_USER[1][];
                                                            db.S_BO_USER_GET_BY_USERNAME(pCREATE_USER, rsUser);
                                                            if(rsUser[0].length > 0) {
                                                                pCREATE_USERID = String.valueOf(rsUser[0][0].ID);
                                                            }
                                                            String sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(sTOKEN_ID), CertProfileID, sTOKEN_SN,
                                                                    pCERTIFICATION_SN, pPERSONAL_NAME, pCOMPANY_NAME, pDOMAIN_NAME, pTAX_CODE, pBUDGET_CODE, pP_ID,
                                                                    pPASSPORT, DN, CACoreSubject, PHONE_CONTRACT, EMAIL_CONTRACT, sAGENT_ID,
                                                                    pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, "",
                                                                    strReqValueATTR, pCREATE_USERID, USER_LOG, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, pCSR,
                                                                    sBackupKeyEnabled, sChangeKeyEnabled, pPRIVATE_KEY, pPKI_FORMFACTOR_ID, pDEVICE,
                                                                    String.valueOf(pCERTIFICATION_OWNER_ID), pCCCD, null, pDECISION, sPersonalCert, sEnterpriseCert);
                                                            if ("0".equals(sParam)) {
                                                                if(!"".equals(pCOMPONENT_SAN)) {
                                                                    db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(pCERTIFICATE_ID[0]), pCOMPONENT_SAN, USER_LOG);
                                                                }
                                                                boolean pSHARED_MODE_ENABLED = false;
                                                                if(!"".equals(sCERT_POLICY_PROPERTIES)){
                                                                    pSHARED_MODE_ENABLED = CommonFunction.getShareModeEnabledCert(sCERT_POLICY_PROPERTIES);
                                                                }
                                                                String pSHARED_MODE_UPDATE = pSHARED_MODE_ENABLED ? "1" : "0";
                                                                db.S_BO_CERTIFICATION_UPDATE(pCERTIFICATE_ID[0], CertProfileID, "", "", "",
                                                                    pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, "", "", "", "", "",
                                                                    "", USER_LOG, "", "", "", "", pSHARED_MODE_UPDATE, pCCCD, pDECISION, sEnterpriseCert, sPersonalCert);
                                                                if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                    //<editor-fold defaultstate="collapsed" desc="CA PROCESS">
                                                                    boolean isCAApprove = false;
                                                                    if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)) {
                                                                        isCAApprove = true;
                                                                    } else {
                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                            isCAApprove = true;
                                                                        }
                                                                    }
                                                                    if(isCAApprove == true) {
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        valueATTR.setApproveCAUser(loginFullname + " (" + USER_LOG + ")");
                                                                        valueATTR.setApproveCADt(new Date());
                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                        if ("0".equals(sApprove)) {
                                                                            if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                            {
                                                                                if("0".equals(sNoAllowTranferToken)) {
                                                                                    if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                        db.S_BO_TOKEN_UPDATE_BRANCH_RESPONSIBLE(sTOKEN_ID, sAGENT_ID, USER_LOG);
                                                                                    }
                                                                                }
                                                                            }
                                                                            if ("1".equals(sDiscountRateOption)) {
                                                                                CommonReferServlet.updateDiscountRate(String.valueOf(pCERTIFICATE_ID[0]), sAGENT_ID, CertProfileID,
                                                                                    pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, USER_LOG, pCCCD, pDECISION, sEnterpriseCert, sPersonalCert);
                                                                            }
                                                                            if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID)))
                                                                            {
                                                                                String isActiveSignServer = "0";
                                                                                GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                if (sessGeneralPolicy1[0].length > 0) {
                                                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                    {
                                                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                            isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                            break;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if("1".equals(isActiveSignServer)) {
                                                                                    CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT, sessLanguageGlobal);
                                                                                } else {
                                                                                    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                    dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                    int[] intWSRes = new int[1];
                                                                                    String[] sWSRes = new String[1];
                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                    if (intWSRes[0] == 0) { }
                                                                                    else {
                                                                                        isProcess = false;
                                                                                    }
                                                                                }
                                                                            } else { }
                                                                        }
                                                                    } else {
                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                        valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                        valueATTR.setApproveDt(new Date());
                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                        db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                    }
                                                                    //</editor-fold>
                                                                } else {
                                                                    //<editor-fold defaultstate="collapsed" desc="AGENCY PROCESS">
                                                                    boolean checkCallApproveCA = false;
                                                                    String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                                    if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                        || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                                    {
                                                                        if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), USER_LOG);
                                                                            if("0".equals(paramPre)) {
                                                                                checkCallApproveCA = true;
                                                                            }
                                                                        } else {
                                                                            int approveChilrenID = Integer.parseInt(userLoginID);
                                                                            BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                            db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                            if(rsUserApprve[0].length > 0) {
                                                                                for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                    if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                        approveChilrenID = item.ID;
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }
                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                            valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                            valueATTR.setApproveDt(new Date());
                                                                            String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG, approveChilrenID);
                                                                            if("0".equals(paramAgency)) {
                                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                {
                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                    valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                    valueATTR.setApproveDt(new Date());
                                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                                    if("0".equals(paramPre)) {
                                                                                        checkCallApproveCA = true;
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    } else {
                                                                        if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                            {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                                if("0".equals(paramPre)) {
                                                                                    checkCallApproveCA = true;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                            if(intApprove == 1) {
                                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                {
                                                                                    int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                    BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                    db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                    if(rsUserApprve[0].length > 0) {
                                                                                        for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                            if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                                approveChilrenID = item.ID;
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                                    valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                    valueATTR.setApproveDt(new Date());
                                                                                    String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG, approveChilrenID);
                                                                                    if("0".equals(paramAgency)) {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                            valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                            valueATTR.setApproveDt(new Date());
                                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                                            if("0".equals(paramPre)) {
                                                                                                checkCallApproveCA = true;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if(checkCallApproveCA == true) {
                                                                        boolean autoApproveCA = false;
                                                                        CERTIFICATION_PROFILE[][] rsProfile = new CERTIFICATION_PROFILE[1][];
                                                                        db.S_BO_CERTIFICATION_PROFILE_DETAIL(CertProfileID, rsProfile);
                                                                        CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                        if(rsProfile[0].length > 0) {
                                                                            autoApproveCA = CommonFunction.getApproveCAOfProfile(EscapeUtils.CheckTextNull(rsProfile[0][0].NAME), sessPolicyCert_Data);
                                                                        }
                                                                        if(autoApproveCA == true) {
                                                                            if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                                            {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                valueATTR.setApproveCAUser(loginFullname + " (" + USER_LOG + ")");
                                                                                valueATTR.setApproveCADt(new Date());
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                                if ("0".equals(sApprove)) {
                                                                                    if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true) {
                                                                                        if("0".equals(sNoAllowTranferToken)) {
                                                                                            if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                                db.S_BO_TOKEN_UPDATE_BRANCH_RESPONSIBLE(sTOKEN_ID, sAGENT_ID, USER_LOG);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if ("1".equals(sDiscountRateOption)) {
                                                                                        CommonReferServlet.updateDiscountRate(String.valueOf(pCERTIFICATE_ID[0]), sAGENT_ID, CertProfileID,
                                                                                            pTAX_CODE, pBUDGET_CODE, pP_ID, pPASSPORT, USER_LOG, pCCCD, pDECISION, sEnterpriseCert, sPersonalCert);
                                                                                    }
                                                                                    if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID))) {
                                                                                        String isActiveSignServer = "0";
                                                                                        GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                        if (sessGeneralPolicy1[0].length > 0) {
                                                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                            {
                                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                                    isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if("1".equals(isActiveSignServer)) {
                                                                                            CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT, sessLanguageGlobal);
                                                                                        } else {
                                                                                            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                            dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                            int[] intWSRes = new int[1];
                                                                                            String[] sWSRes = new String[1];
                                                                                            ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                            if (intWSRes[0] == 0) { }
                                                                                            else {
                                                                                                isProcess = false;
                                                                                            }
                                                                                        }
                                                                                    } else { }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    //</editor-fold>
                                                                }
                                                            } else {
                                                                sVALID_CODE = "CERT_PEDDING";
                                                            }
                                                        }
                                                    } else {
                                                        //<editor-fold defaultstate="collapsed" desc="RSSP Process">
                                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                        //</editor-fold>
                                                    }
                                                }
                                                //<editor-fold defaultstate="collapsed" desc="### RESPONSE CODE">
                                                if(null != sVALID_CODE)
                                                switch (sVALID_CODE) {
                                                    case "OK":
                                                        success = success + 1;
                                                        break;
                                                    case "CERT_PEDDING":
                                                        failed = failed + 1;
                                                        failedCERT_PEDDING = failedCERT_PEDDING + 1;
                                                        sFailCERT_PEDDING = sFailCERT_PEDDING + sSTT +",";
                                                        break;
                                                    case "AGENCY_NOT_PROFILE":
                                                        failed = failed + 1;
                                                        failedNOT_PROFILE = failedNOT_PROFILE + 1;
                                                        sFailNOT_PROFILE = sFailNOT_PROFILE + sSTT +",";
                                                        break;
                                                    case "BENEFICIARYUSER_INVALID":
                                                        failed = failed + 1;
                                                        failedBENEFICIARYUSER_INVALID = failedBENEFICIARYUSER_INVALID + 1;
                                                        sFailBENEFICIARYUSER_INVALID = sFailBENEFICIARYUSER_INVALID + sSTT +",";
                                                        break;
                                                    case "COMPONENT_INVALID":
                                                        failed = failed + 1;
                                                        failedCOMPONENT_INVALID= failedCOMPONENT_INVALID + 1;
                                                        sFailCOMPONENT_INVALID = sFailCOMPONENT_INVALID + sSTT +",";
                                                        break;
                                                    case "DN_INVALID":
                                                        failed = failed + 1;
                                                        failedDN_INVALID = failedDN_INVALID + 1;
                                                        sFailDN_INVALID= sFailDN_INVALID + sSTT +",";
                                                        break;
                                                    case "PROFILE_INVALID":
                                                        failed = failed + 1;
                                                        failedPROFILE_INVALID = failedPROFILE_INVALID + 1;
                                                        sFailPROFILE_INVALID= sFailPROFILE_INVALID + sSTT +",";
                                                        break;
                                                    case "PHONE_INVALID":
                                                        failed = failed + 1;
                                                        failedPHONE_INVALID = failedPHONE_INVALID + 1;
                                                        sFailPHONE_INVALID= sFailPHONE_INVALID + sSTT +",";
                                                        break;
                                                    case "EMAIL_INVALID":
                                                        failed = failed + 1;
                                                        failedEMAIL_INVALID = failedEMAIL_INVALID + 1;
                                                        sFailEMAIL_INVALID= sFailEMAIL_INVALID + sSTT +",";
                                                        break;
                                                    case "ISSUE_ERROR":
                                                        failed = failed + 1;
                                                        failedISSUE_ERROR = failedISSUE_ERROR + 1;
                                                        sFailISSUE_ERROR = sFailISSUE_ERROR + sSTT +",";
                                                        break;
                                                    case "APPROVE_ERROR":
                                                        failed = failed + 1;
                                                        failedAPPROVE_ERROR = failedAPPROVE_ERROR + 1;
                                                        sFailAPPROVE_ERROR = sFailAPPROVE_ERROR + sSTT +",";
                                                        break;
                                                    case "PROVINCE_INVALID":
                                                        failed = failed + 1;
                                                        failedPROVINCE_INVALID = failedPROVINCE_INVALID + 1;
                                                        sFailPROVINCE_INVALID= sFailPROVINCE_INVALID + sSTT +",";
                                                        break;
                                                    case "CSR_INVALID":
                                                        failed = failed + 1;
                                                        failedCSR = failedCSR + 1;
                                                        sFailCSR = sFailCSR + sSTT +",";
                                                        break;
                                                    case "CSR_KEY":
                                                        failed = failed + 1;
                                                        failedKeyCSR = failedKeyCSR + 1;
                                                        sFailKeyCSR = sFailKeyCSR + sSTT +",";
                                                        break;
                                                    case "METHOD_INVALID":
                                                        failed = failed + 1;
                                                        failedMethod = failedMethod + 1;
                                                        sFailMethod = sFailMethod + sSTT +",";
                                                        break;
                                                    case "CERTIFICATETYPE_INVALID":
                                                        failed = failed + 1;
                                                        failedCertType = failedCertType + 1;
                                                        sFailCertType = sFailCertType + sSTT +",";
                                                        break;
                                                    case "CERT_INVALID":
                                                        failed = failed + 1;
                                                        failedCERT_INVALID = failedCERT_INVALID + 1;
                                                        sFailCERT_INVALID = sFailCERT_INVALID + sSTT +",";
                                                        break;
                                                    case "AGENCY_DENIED":
                                                        failed = failed + 1;
                                                        failedAGENCY_DENIED = failedAGENCY_DENIED + 1;
                                                        sFailAGENCY_DENIED = sFailAGENCY_DENIED + sSTT +",";
                                                        break;
                                                    case "2052":
                                                        failed = failed + 1;
                                                        failedTypeOwnerInvalid = failedTypeOwnerInvalid + 1;
                                                        sFailTypeOwnerInvalid = sFailTypeOwnerInvalid + sSTT +",";
                                                        break;
                                                    case "2053":
                                                        failed = failed + 1;
                                                        failedOwner_incomplete = failedOwner_incomplete + 1;
                                                        sFailOwner_incomplete = sFailOwner_incomplete + sSTT +",";
                                                        break;
                                                    default:
                                                        break;
                                                }
//                                                  //</editor-fold>
                                            }
                                            String sSumShow = "";
                                            //<editor-fold defaultstate="collapsed" desc="### RETURN">
                                            if (failed != 0)
                                            {
                                                sSumShow = "1";
                                                String sSum="";
                                                if(failedCERT_PEDDING != 0) {
                                                    sSum = sSum + "The Certificate Is pedding in system, import failure: " + sColumnSTT + "- " + sFailCERT_PEDDING + "\n";
                                                }
                                                if(failedNOT_PROFILE != 0) {
                                                    sSum = sSum + "Service pack configuration for agent does not exist: " + sColumnSTT + "- " + sFailNOT_PROFILE + "\n";
                                                }
                                                if(failedBENEFICIARYUSER_INVALID != 0){
                                                    sSum = sSum + "Beneficiaty User Is Invalid: " + sColumnSTT + "- " + sFailBENEFICIARYUSER_INVALID + "\n";
                                                }
                                                if(failedCERT_INVALID != 0) {
                                                    sSum = sSum + "The certificate does not exist in the system: " + sColumnSTT + "- " + sFailCERT_INVALID + "\n";
                                                }
                                                if(failedAGENCY_DENIED != 0) {
                                                    sSum = sSum + "Certificate access request was denied: " + sColumnSTT + "- " + sFailAGENCY_DENIED + "\n";
                                                }
                                                if(failedCOMPONENT_INVALID != 0) {
                                                    sSum = sSum + "Certificate Information Is Invalid: " + sColumnSTT + "- " + sFailCOMPONENT_INVALID + "\n";
                                                }
                                                if(failedDN_INVALID != 0) {
                                                    sSum = sSum + "Output string Subject DN Error: " + sColumnSTT + "- " + sFailDN_INVALID + "\n";
                                                }
                                                if(failedPROFILE_INVALID != 0) {
                                                    sSum = sSum + "Certificate Profile Is Invalid: " + sColumnSTT + "- " + sFailPROFILE_INVALID + "\n";
                                                }
                                                if(failedPHONE_INVALID != 0) {
                                                    sSum = sSum + "Customer Phone Number Is Invalid: " + sColumnSTT + "- " + sFailPHONE_INVALID + "\n";
                                                }
                                                if(failedEMAIL_INVALID != 0) {
                                                    sSum = sSum + "Customer Email Is Invalid: " + sColumnSTT + "- " + sFailEMAIL_INVALID + "\n";
                                                }
                                                if(failedAPPROVE_ERROR != 0) {
                                                    sSum = sSum + "Approve CA certificates Is Errors: " + sColumnSTT + "- " + sFailAPPROVE_ERROR + "\n";
                                                }
                                                if(failedISSUE_ERROR != 0) {
                                                    sSum = sSum + "Issuing a certificate of error: " + sColumnSTT + "- " + sFailISSUE_ERROR + "\n";
                                                }
                                                if(failedPROVINCE_INVALID != 0) {
                                                    sSum = sSum + "Province Code Is Invalid: " + sColumnSTT + "- " + sFailPROVINCE_INVALID + "\n";
                                                }
                                                if(failedCSR != 0) {
                                                    sSum = sSum + "CSR Is Invalid: " + sColumnSTT + "- " + sFailCSR + "\n";
                                                }
                                                if(failedKeyCSR != 0) {
                                                    sSum = sSum + "Key Size Is Invalid: " + sColumnSTT + "- " + sFailKeyCSR + "\n";
                                                }
                                                if(failedMethod != 0) {
                                                    sSum = sSum + "Method Is Invalid: " + sColumnSTT + "- " + sFailMethod + "\n";
                                                }
                                                if(failedCertType != 0) {
                                                    sSum = sSum + "Certificate Type Is Invalid: " + sColumnSTT + "- " + sFailCertType + "\n";
                                                }
                                                if(failedTypeOwnerInvalid != 0) {
                                                    sSum = sSum + "Invalid information for this type of owner: " + sColumnSTT + "- " + sFailTypeOwnerInvalid + "\n";
                                                }
                                                if(failedOwner_incomplete != 0) {
                                                    sSum = sSum + "Certification owner has a queue incomplete: " + sColumnSTT + "- " + sFailOwner_incomplete + "\n";
                                                }
                                                sessionsa.setAttribute("sessRegisterCertImportFailed", sSum);
                                                CommonFunction.LogDebugString(log, "sessRegisterCertImportFailed-1", sSum);
                                            }
                                            //</editor-fold>
                                            strView = "0###" + String.valueOf(success) + "###" + String.valueOf(failed) + "###" + sSumShow;
                                        } else {
                                            strView = "1###" + sValueFailColumnName;
                                        }
                                        //</editor-fold>
                                    } else if(Integer.parseInt(sCERTIFICATION_TYPEParam) == Definitions.CONFIG_SERVICE_TYPE_ID_CHANGEINFO) {
                                        //<editor-fold defaultstate="collapsed" desc="### CHANGE">
                                        //<editor-fold defaultstate="collapsed" desc="### GET COLUMN - CHECK COLUMN">
                                        for (int i = 0; i < cellStoreArrayList.size(); i++) {
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnSTT)) {
                                                indexOfSTT = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCertificateSN)) {
                                                indexOfCertificateSN = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCertificateAuthority)) {
                                                indexOfCertificateAuthority = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnPersonalName)) {
                                                indexOfPersonalName = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCompanyName)) {
                                                indexOfCompanyName = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnDomainName)) {
                                                indexOfDomainName = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnOrganization)) {
                                                indexOfOrganization = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnPrefixUIDEnterprise)) {
                                                indexOfPrefixUIDEnterprise = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnUIDEnterprise)) {
                                                indexOfUIDEnterprise = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnPrefixUIDPersonal)) {
                                                indexOfPrefixUIDPersonal = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnUIDPersonal)) {
                                                indexOfUIDPersonal = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnOrganizationUnit)) {
                                                indexOfOrganizationUnit = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnTitle)) {
                                                indexOfTitle = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnEmailAddress)) {
                                                indexOfEmailAddress = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnTelephoneNumber)) {
                                                indexOfTelephoneNumber = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnLocality)) {
                                                indexOfLocality = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnStateProvince)) {
                                                indexOfStateProvince = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCountry)) {
                                                indexOfCountry = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnBeneficiaryUser)) {
                                                indexOfBeneficiaryUser = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnBackupKeyEnabled)) {
                                                indexOfBackupKeyEnabled = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnRevokeOldCertificateEnabled)) {
                                                indexOfRevokeOldCertificateEnabled = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnChangeKeyEnabled)) {
                                                indexOfChangeKeyEnabled = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnDeleteCertificateEnabled)) {
                                                indexOfDeleteCertificateEnabled = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnKeepCertificateSNEnabled)) {
                                                indexOfKeepCertificateSNEnabled = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCSR)) {
                                                indexOfCSR = i;
                                            }
                                        }
                                        boolean booFailColumnName = true;
                                        String sValueFailColumnName = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                        if (indexOfCertificateSN == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCertificateAuthority == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfPersonalName == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCompanyName == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfDomainName == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfOrganization == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfPrefixUIDEnterprise == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfUIDEnterprise == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfPrefixUIDPersonal == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfUIDPersonal == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfOrganizationUnit == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfTitle == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfEmailAddress == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfTelephoneNumber == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfLocality == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfStateProvince == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCountry == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfBeneficiaryUser == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfBackupKeyEnabled == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfRevokeOldCertificateEnabled == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfChangeKeyEnabled == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfDeleteCertificateEnabled == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfKeepCertificateSNEnabled == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfCSR == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        //</editor-fold>

                                        if (booFailColumnName == true) {
                                            for (int i = 1; i < dataHolder.size(); i++) {
                                                String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                cellStoreArrayList = (ArrayList) dataHolder.get(i);
                                                String sSTT = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfSTT).toString(), true);
                                                String sCertificateSN = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCertificateSN).toString(), false);
                                                String sCACodeClient = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCertificateAuthority).toString(), false);
                                                String sPersonalName = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfPersonalName).toString(), false);
                                                String sCompanyName = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCompanyName).toString(), false);
                                                String sDomainName = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfDomainName).toString(), false);
                                                String sOrganization = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfOrganization).toString(), false);
                                                String sPrefixUIDEnterprise = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfPrefixUIDEnterprise).toString(), false);
                                                String sUIDEnterprise = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfUIDEnterprise).toString(), false);
                                                String sPrefixUIDPersonal = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfPrefixUIDPersonal).toString(), false);
                                                String sUIDPersonal = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfUIDPersonal).toString(), false);
                                                String sOrganizationUnit = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfOrganizationUnit).toString(), false);
                                                String sTitle = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfTitle).toString(), false);
                                                String sEmailAddress = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfEmailAddress).toString(), false);
                                                String sTelephoneNumber = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfTelephoneNumber).toString(), false);
                                                String sLocality = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfLocality).toString(), false);
                                                String sStateProvince = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfStateProvince).toString(), false);
                                                String sCountry = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCountry).toString(), false);
                                                String pCREATE_USER = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfBeneficiaryUser).toString(), false);
                                                String sBackupKeyEnabled = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfBackupKeyEnabled).toString(), false);
                                                String sRevokeOldCertificateEnabled = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfRevokeOldCertificateEnabled).toString(), false);
                                                String sChangeKeyEnabled = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfChangeKeyEnabled).toString(), false);
                                                String sDeleteCertificateEnabled = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfDeleteCertificateEnabled).toString(), false);
                                                String sKeepCertificateSNEnabled = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfKeepCertificateSNEnabled).toString(), false);
                                                String sCSR = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCSR).toString(), false);
                                                sBackupKeyEnabled = "".equals(sBackupKeyEnabled) ? "FALSE" : sBackupKeyEnabled;
                                                if("TRUE".equals(sBackupKeyEnabled)){sBackupKeyEnabled = "1";} else {sBackupKeyEnabled="0";}
                                                sRevokeOldCertificateEnabled = "".equals(sRevokeOldCertificateEnabled) ? "FALSE" : sRevokeOldCertificateEnabled;
                                                if("TRUE".equals(sRevokeOldCertificateEnabled)){sRevokeOldCertificateEnabled = "1";} else {sRevokeOldCertificateEnabled="0";}
                                                sChangeKeyEnabled = "".equals(sChangeKeyEnabled) ? "0" : sChangeKeyEnabled;
                                                if("TRUE".equals(sChangeKeyEnabled)){sChangeKeyEnabled = "1";} else {sChangeKeyEnabled="0";}
                                                sDeleteCertificateEnabled = "".equals(sDeleteCertificateEnabled) ? "FALSE" : sDeleteCertificateEnabled;
                                                if("TRUE".equals(sDeleteCertificateEnabled)){sDeleteCertificateEnabled = "1";} else {sDeleteCertificateEnabled="0";}
                                                sKeepCertificateSNEnabled = "".equals(sKeepCertificateSNEnabled) ? "FALSE" : sKeepCertificateSNEnabled;
                                                if("TRUE".equals(sKeepCertificateSNEnabled)){sKeepCertificateSNEnabled = "1";} else {sKeepCertificateSNEnabled="0";}
                                                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                boolean isAccessAgency = true;
                                                boolean isProcess = true;
                                                String sTOKEN_ID = "";
                                                String sTOKEN_SN = "";
                                                String PHONE_CONTRACT = "";
                                                String EMAIL_CONTRACT = "";
                                                String DN = "";
                                                String pCSR = "";
                                                String pDEVICE = "";
                                                String pCERTIFICATION_SN = "";
                                                String pPRIVATE_KEY = "";
                                                String pPROVINCE_ID = "";
                                                String pPROVINCE_DESC = "";
                                                String sAGENT_ID = "";
                                                String sAGENT_ID_OLD = "";
                                                String pCOMPONENT_SAN = "";
                                                String sMethod = "";
//                                                        String CertProfileID = "";
                                                String sCERT_PROFILE_PROPERTIES="";
                                                String sCERT_POLICY_PROPERTIES="";
                                                String pCERTIFICATION_PROFILE_ID="";
                                                int pPKI_FORMFACTOR_ID = 0;
                                                int pCERTIFICATION_OWNER_ID = 0;
                                                int pCERTIFICATION_PURPOSE_ID_OLD = 0;
                                                int certID = 0;
                                                boolean pPRIVATE_KEY_ENABLED = true;
                                                BRANCH[][] rsBranch;
                                                String pCERTIFICATION_AUTHORITY_CODE = "";
                                                String pCERTIFICATION_PURPOSE_CODE = "";
                                                String pCERTIFICATE_PROFILE_CODE = "";
                                                String pCERTIFICATE_PROFILE_ID = "0";
                                                String pDISCOUNT_RATE="0";

                                                if("".equals(sCountry) || "".equals(sCertificateSN)) {
                                                    sVALID_CODE = "COMPONENT_INVALID";
                                                } else {
                                                    //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                                    CertificateInfo[][] rsReqID = new CertificateInfo[1][];
                                                    int[] pRESPONSE_CODE = new int[1];
                                                    db.S_BO_API_CERTIFICATION_GET_INFO("", "", "", "", sCertificateSN, 0,
                                                        Definitions.CONFIG_CERTIFICATION_STATE_CODE_OPERATED, "", Integer.parseInt(sessLanguageGlobal), pRESPONSE_CODE, rsReqID, "", "", "", "");
                                                    if (rsReqID[0].length > 0) {
                                                        certID = rsReqID[0][0].certificateID;
                                                        pCERTIFICATION_PURPOSE_CODE = rsReqID[0][0].certificatePurposeCode;
                                                        pCERTIFICATION_AUTHORITY_CODE = rsReqID[0][0].certificateAuthorityCode;
                                                        pCERTIFICATE_PROFILE_CODE = rsReqID[0][0].certificateProfileCode;
                                                    }
                                                    if(certID == 0) {
                                                        sVALID_CODE = "CERT_INVALID";
                                                    } else {
                                                        CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                                        db.S_BO_CERTIFICATION_DETAIL(String.valueOf(certID), sessLanguageGlobal, rsReq);
                                                        if (rsReq[0].length > 0) {
                                                            PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                                            EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                                            pCSR = EscapeUtils.CheckTextNull(rsReq[0][0].CSR);
                                                            pDEVICE = EscapeUtils.CheckTextNull(rsReq[0][0].SERVICE_UUID);
                                                            sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                                            pCOMPONENT_SAN = EscapeUtils.CheckTextNull(rsReq[0][0].PROPERTIES);
                                                            sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                                            sAGENT_ID_OLD = String.valueOf(rsReq[0][0].BRANCH_ID);
                                                            sTOKEN_ID = String.valueOf(rsReq[0][0].TOKEN_ID);
                                                            pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                                            pPRIVATE_KEY_ENABLED = rsReq[0][0].PRIVATE_KEY_ENABLED;
                                                            pDISCOUNT_RATE = String.valueOf(rsReq[0][0].DISCOUNT_RATE);
                                                            pCERTIFICATION_OWNER_ID = rsReq[0][0].CERTIFICATION_OWNER_ID;
                                                            pCERTIFICATION_PURPOSE_ID_OLD = rsReq[0][0].CERTIFICATION_PURPOSE_ID;
                                                            pCERTIFICATE_PROFILE_ID = String.valueOf(rsReq[0][0].CERTIFICATION_PROFILE_ID);
                                                            pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                                            pPROVINCE_ID = String.valueOf(rsReq[0][0].CITY_PROVINCE_ID);
                                                            sMethod = rsReq[0][0].PKI_FORMFACTOR_NAME;
                                                            if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                                isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                                                if(isAccessAgency == true) {
                                                                    rsBranch = new BRANCH[1][];
                                                                    db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                                    if(rsBranch[0].length > 0) {
                                                                        String sResponseCheckBranch = db.S_BO_API_CHECK_USERNAME_AND_BRANCH_CODE(rsBranch[0][0].NAME, pCREATE_USER);
                                                                        if("1".equals(sResponseCheckBranch)) {
                                                                            sVALID_CODE = "BENEFICIARYUSER_INVALID";
                                                                        }
                                                                    }
                                                                } else {
                                                                    sVALID_CODE = "AGENCY_DENIED";
                                                                }
                                                            } else {
                                                                BACKOFFICE_USER[][] rsUser = new BACKOFFICE_USER[1][];
                                                                db.S_BO_USER_GET_BY_USERNAME(pCREATE_USER, rsUser);
                                                                if(rsUser[0].length > 0) {
                                                                    sAGENT_ID = String.valueOf(rsUser[0][0].BRANCH_ID);
                                                                } else {
                                                                    sVALID_CODE = "BENEFICIARYUSER_INVALID";
                                                                }
                                                            }
                                                        } else {
                                                            sVALID_CODE = "AGENCY_DENIED";
                                                        }
                                                    }
                                                    //</editor-fold>
                                                }
                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                    if(!"".equals(sPrefixUIDEnterprise)){
                                                        if(CommonReferServlet.checkPrefixEnterpriseVN(sPrefixUIDEnterprise) == false) {
                                                            sVALID_CODE = "PREFIX_INVALID";
                                                        }
                                                    }
                                                    if(!"".equals(sPrefixUIDPersonal)){
                                                        if(CommonReferServlet.checkPrefixPersonalVN(sPrefixUIDPersonal) == false) {
                                                            sVALID_CODE = "PREFIX_INVALID";
                                                        }
                                                    }
                                                }

                                                //<editor-fold defaultstate="collapsed" desc="### CHECK CA CHANGE">
                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                    if(!"".equals(sCACodeClient)) {
                                                        if(!sCACodeClient.equals(pCERTIFICATION_AUTHORITY_CODE)) {
                                                            rsBranch = new BRANCH[1][];
                                                            db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranch);
                                                            if(rsBranch[0].length > 0) {
                                                                sCERT_PROFILE_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_PROFILE_PROPERTIES);
                                                                sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranch[0][0].CERTIFICATION_POLICY_PROPERTIES);
                                                            }
                                                            if(!"".equals(sCERT_PROFILE_PROPERTIES) && !"".equals(sCERT_POLICY_PROPERTIES)) {
                                                                ArrayList<CERTIFICATION_POLICY_DATA> tempProfileList = new ArrayList<>();
                                                                CERTIFICATION_POLICY_DATA[][] resPolicyData_Old = new CERTIFICATION_POLICY_DATA[1][];
                                                                CommonFunction.getProfileCertList(sCERT_PROFILE_PROPERTIES, resPolicyData_Old);
                                                                for(CERTIFICATION_POLICY_DATA resPolicyCertData_Old1 : resPolicyData_Old[0])
                                                                {
                                                                    if(resPolicyCertData_Old1.attributeType.equals(Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST))
                                                                    {
                                                                        CERTIFICATION_PROFILE[][] resProfileDB = new CERTIFICATION_PROFILE[1][];
                                                                        db.S_BO_API_CERTIFICATION_PROFILE_GET_INFO(EscapeUtils.CheckTextNull(resPolicyCertData_Old1.name), resProfileDB);
                                                                        if(resProfileDB[0].length > 0)
                                                                        {
                                                                            CERTIFICATION_POLICY_DATA itemProfileAccess = new CERTIFICATION_POLICY_DATA();
                                                                            itemProfileAccess.name = resProfileDB[0][0].NAME;
                                                                            itemProfileAccess.certificateAuthority = resProfileDB[0][0].CERTIFICATION_AUTHORITY_NAME;
                                                                            itemProfileAccess.certificatePurpose = resProfileDB[0][0].CERTIFICATION_PURPOSE_NAME;
                                                                            itemProfileAccess.remark = resPolicyCertData_Old1.remark;
                                                                            itemProfileAccess.remarkEn = resPolicyCertData_Old1.remarkEn;
                                                                            itemProfileAccess.attributeType = Definitions.CONFIG_ROLE_ATTRIBUTE_CERT_POLICY_ITEM_PROFILE_LIST;
                                                                            tempProfileList.add(itemProfileAccess);
                                                                        }
                                                                    }
                                                                }

                                                                CERTIFICATION_PROFILE[][] rsProfileCheck = new CERTIFICATION_PROFILE[1][];
                                                                db.S_BO_API_CERTIFICATION_PROFILE_LIST_HASID(sCACodeClient, pCERTIFICATION_PURPOSE_CODE,
                                                                    sMethod, 0, Integer.parseInt(sessLanguageGlobal), rsProfileCheck, sCERT_PROFILE_PROPERTIES, tempProfileList);
                                                                if(rsProfileCheck[0].length > 0) {
                                                                    int durationProfileOld = 0;
                                                                    CERTIFICATION_PROFILE[][] rsDurationOld = new CERTIFICATION_PROFILE[1][];
                                                                    db.S_BO_CERTIFICATION_PROFILE_DETAIL(pCERTIFICATE_PROFILE_ID, rsDurationOld);
                                                                    if(rsDurationOld[0].length > 0) {
                                                                        durationProfileOld = rsDurationOld[0][0].DURATION;
                                                                    }
                                                                    for(CERTIFICATION_PROFILE rsProfileCheck1 : rsProfileCheck[0]) {
                                                                        if(rsProfileCheck1.DURATION == durationProfileOld) {
                                                                            sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                                            pCERTIFICATION_AUTHORITY_CODE = sCACodeClient;
                                                                            pCERTIFICATE_PROFILE_CODE = rsProfileCheck1.NAME;
                                                                            pCERTIFICATE_PROFILE_ID = String.valueOf(rsProfileCheck1.ID);
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                sVALID_CODE = "AGENCY_NOT_PROFILE";
                                                            }
                                                        }
                                                    }
                                                }
                                                //</editor-fold>

                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                    Config conf = new Config();
                                                    boolean booRSSP_ACCESS_ENABLED = false;
                                                    if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_ESIGNCLOUD) {
                                                        String sRSSP_ACCESS_ENABLED = conf.GetPropertybyCode(Definitions.CONFIG_RSSP_ACCESS_ENABLED);
                                                        if("1".equals(sRSSP_ACCESS_ENABLED)) {
                                                            booRSSP_ACCESS_ENABLED = true;
                                                        }
                                                    }
                                                    if(booRSSP_ACCESS_ENABLED == false) {
                                                        //<editor-fold defaultstate="collapsed" desc="TMS">
                                                        String strPasswordP12 = "";
                                                        boolean isCSRValid = true;
                                                        boolean isCSR_SizeValid = true;
                                                        if(pPKI_FORMFACTOR_ID == Definitions.CONFIG_PKI_FORMFACTOR_ID_SOFT_TOKEN) {
                                                            //<editor-fold defaultstate="collapsed" desc="### CHECK SOFTTKEN">
                                                            if(pPRIVATE_KEY_ENABLED == true)
                                                            {
                                                                sBackupKeyEnabled = "1";
                                                                strPasswordP12 = CommonFunction.randomPasswordP12(8);
                                                            } else {
                                                                if("1".equals(sChangeKeyEnabled))
                                                                {
                                                                    pCSR = sCSR;
                                                                    if("".equals(pCSR))
                                                                    {
                                                                        isCSRValid = false;
                                                                    }
                                                                    if(isCSRValid == true)
                                                                    {
                                                                        String sKeySizeDB;
                                                                        isCSR_SizeValid = false;
                                                                        CERTIFICATION[][] rsCert = new CERTIFICATION[1][];
                                                                        db.S_BO_GET_ALGORITHM_KEY_SIZE(pCERTIFICATE_PROFILE_ID, rsCert);
                                                                        if(rsCert[0].length > 0)
                                                                        {
                                                                            sKeySizeDB = EscapeUtils.CheckTextNull(rsCert[0][0].KEY_SIZE);
                                                                            String sKeySizeCSR = CommonFunction.getKeySizeFromCSR(pCSR);
                                                                            isCSR_SizeValid = sKeySizeDB.equals(sKeySizeCSR);
                                                                        }
                                                                    }
                                                                }
                                                                sBackupKeyEnabled = "0";
                                                            }
                                                            sRevokeOldCertificateEnabled = "0";
                                                            //</editor-fold>
                                                        }
                                                        if(isCSRValid == true) {
                                                            if(isCSR_SizeValid == true) {
                                                                CITY_PROVINCE[][] rsProvince;
                                                                if("".equals(sStateProvince)) {
                                                                    rsProvince = new CITY_PROVINCE[1][];
                                                                    db.S_BO_PROVINCE_DETAIL(pPROVINCE_ID, rsProvince);
                                                                    if (rsProvince[0].length > 0) {
                                                                        pPROVINCE_DESC = rsProvince[0][0].REMARK;
                                                                    }
                                                                } else {
                                                                    rsProvince = new CITY_PROVINCE[1][];
                                                                    db.S_BO_API_PROVINCE_LIST_BY_NAME(sStateProvince, Integer.parseInt(sessLanguageGlobal), rsProvince);
                                                                    if(rsProvince[0].length > 0){
                                                                        pPROVINCE_ID = String.valueOf(rsProvince[0][0].ID);
                                                                        pPROVINCE_DESC = rsProvince[0][0].REMARK;
                                                                    }
                                                                }

                                                                //<editor-fold defaultstate="collapsed" desc="### PROFILE CHECK">
                                                                CertificateProfileInfo[][] rsProfile = new CertificateProfileInfo[1][];
                                                                db.S_BO_API_CERTIFICATION_PROFILE_GET_PROPERTIES(pCERTIFICATION_AUTHORITY_CODE, pCERTIFICATE_PROFILE_CODE, sMethod, rsProfile);
                                                                if(rsProfile[0].length > 0)
                                                                {
                                                                    String sProperties = EscapeUtils.CheckTextNull(rsProfile[0][0].certificateProfileProperties);
                                                                    if (!"".equals(sProperties)) {
                                                                        ObjectMapper objectMapper = new ObjectMapper();
                                                                        CERTIFICATE_ATTRIBUTES proParse = objectMapper.readValue(sProperties, CERTIFICATE_ATTRIBUTES.class);
                                                                        CertificateComponentInfo[][] rsComponentFromServer = new CertificateComponentInfo[1][];
                                                                        ArrayList<CertificateComponentInfo> tempListComServer = new ArrayList<>();
                                                                        for (CERTIFICATE_ATTRIBUTES.Attribute attribute : proParse.getAttributes()) {
                                                                            if (attribute.getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY))
                                                                            {
                                                                                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                                                                                    CertificateComponentInfo mh = new CertificateComponentInfo();
                                                                                    mh.code = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                                                                                    mh.prefix = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getPrefix());
                                                                                    mh.requireEnabled = attribute.getAttributes().get(n).isRequire();
                                                                                    mh.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY;
                                                                                    tempListComServer.add(mh);
                                                                                }
                                                                            } else if (attribute.getAttributeType().equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL))
                                                                            {
                                                                                for (int n = 0; n < attribute.getAttributes().size(); n++) {
                                                                                    CertificateComponentInfo mh = new CertificateComponentInfo();
                                                                                    mh.code = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getName());
                                                                                    mh.prefix = EscapeUtils.CheckTextNull(attribute.getAttributes().get(n).getPrefix());
                                                                                    mh.requireEnabled = attribute.getAttributes().get(n).isRequire();
                                                                                    mh.attributeType = Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL;
                                                                                    tempListComServer.add(mh);
                                                                                }
                                                                            } else {
                                                                                CertificateComponentInfo mh = new CertificateComponentInfo();
                                                                                mh.code = EscapeUtils.CheckTextNull(attribute.getName());
                                                                                mh.prefix = EscapeUtils.CheckTextNull(attribute.getPrefix());
                                                                                mh.requireEnabled = attribute.isRequire();
                                                                                mh.attributeType = attribute.getAttributeType();
                                                                                tempListComServer.add(mh);
                                                                            }
                                                                        }
                                                                        rsComponentFromServer[0] = new CertificateComponentInfo[tempListComServer.size()];
                                                                        rsComponentFromServer[0] = tempListComServer.toArray(rsComponentFromServer[0]);
                                                                        if(rsComponentFromServer[0].length > 0)
                                                                        {
                                                                            if(pCERTIFICATION_PURPOSE_CODE.equals(Definitions.CONFIG_CERTTYPE_DESC_ENTERPRISE)
                                                                                || pCERTIFICATION_PURPOSE_CODE.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_CODE_SIGNING))
                                                                            {
                                                                                //<editor-fold defaultstate="collapsed" desc="### ENTERPRISE - CODE_SIGNING CHECK AND GET DN">
                                                                                for(CertificateComponentInfo rsComponentFromServer1 : rsComponentFromServer[0])
                                                                                {
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDEnterprise+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDEnterprise;
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sCompanyName;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sCompanyName))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_O))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sOrganization;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sOrganization))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sOrganizationUnit;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sOrganizationUnit))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_E))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sEmailAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sEmailAddress))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sTelephoneNumber;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sTelephoneNumber))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_L))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sLocality;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sLocality))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST))
                                                                                    {
                                                                                        rsComponentFromServer1.value = pPROVINCE_DESC;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(pPROVINCE_DESC))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_C))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sCountry;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sCountry))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(EscapeUtils.CheckTextNull(rsComponentFromServer1.value)))
                                                                                    {
                                                                                        DN += EscapeUtils.CheckTextNull(rsComponentFromServer1.code) + "=" + EscapeUtils.CheckTextNull(rsComponentFromServer1.prefix)
                                                                                            + CommonFunction.replaceStringCharaterSpecialDN(EscapeUtils.CheckTextNull(rsComponentFromServer1.value), true, false) + ", ";
                                                                                    }
                                                                                }
                                                                                //</editor-fold>
                                                                            } else if(pCERTIFICATION_PURPOSE_CODE.equals(Definitions.CONFIG_CERTTYPE_DESC_STAFF))
                                                                            {
                                                                                //<editor-fold defaultstate="collapsed" desc="### STAFF CHECK AND GET DN">
                                                                                for(CertificateComponentInfo rsComponentFromServer1 : rsComponentFromServer[0])
                                                                                {
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDEnterprise+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDEnterprise;
                                                                                    }
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDPersonal+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDPersonal;
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sPersonalName;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sPersonalName))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_O))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sOrganization;
                                                                                        sCompanyName = sOrganization;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sOrganization))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_OU))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sOrganizationUnit;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sOrganizationUnit))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_T))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sTitle;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sTitle))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_E))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sEmailAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sEmailAddress))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sTelephoneNumber;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sTelephoneNumber))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_L))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sLocality;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sLocality))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST))
                                                                                    {
                                                                                        rsComponentFromServer1.value = pPROVINCE_DESC;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(pPROVINCE_DESC))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_C))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sCountry;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sCountry))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(EscapeUtils.CheckTextNull(rsComponentFromServer1.value)))
                                                                                    {
                                                                                        DN += EscapeUtils.CheckTextNull(rsComponentFromServer1.code) + "=" + EscapeUtils.CheckTextNull(rsComponentFromServer1.prefix)
                                                                                            + CommonFunction.replaceStringCharaterSpecialDN(EscapeUtils.CheckTextNull(rsComponentFromServer1.value), true, false) + ", ";
                                                                                    }
                                                                                }
                                                                                //</editor-fold>
                                                                            } else if(pCERTIFICATION_PURPOSE_CODE.equals(Definitions.CONFIG_CERTTYPE_DESC_PERSONAL))
                                                                            {
                                                                                //<editor-fold defaultstate="collapsed" desc="### PERSONAL CHECK AND GET DN">
                                                                                for(CertificateComponentInfo rsComponentFromServer1 : rsComponentFromServer[0])
                                                                                {
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDPersonal+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDPersonal;
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sPersonalName;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sPersonalName))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_E))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sEmailAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sEmailAddress))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sTelephoneNumber;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sTelephoneNumber))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_L))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sLocality;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sLocality))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST))
                                                                                    {
                                                                                        rsComponentFromServer1.value = pPROVINCE_DESC;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(pPROVINCE_DESC))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_C))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sCountry;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sCountry))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(EscapeUtils.CheckTextNull(rsComponentFromServer1.value)))
                                                                                    {
                                                                                        DN += EscapeUtils.CheckTextNull(rsComponentFromServer1.code) + "=" + EscapeUtils.CheckTextNull(rsComponentFromServer1.prefix)
                                                                                            + CommonFunction.replaceStringCharaterSpecialDN(EscapeUtils.CheckTextNull(rsComponentFromServer1.value), true, false) + ", ";
                                                                                    }
                                                                                }
                                                                                //</editor-fold>
                                                                            } else if(pCERTIFICATION_PURPOSE_CODE.equals(Definitions.CONFIG_CERTIFICATION_PURPOSE_CODE_SSL))
                                                                            {
                                                                                //<editor-fold defaultstate="collapsed" desc="### PERSONAL CHECK AND GET DN">
                                                                                for(CertificateComponentInfo rsComponentFromServer1 : rsComponentFromServer[0])
                                                                                {
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_COMPANY)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDEnterprise+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDEnterprise;
                                                                                    }
                                                                                    if(rsComponentFromServer1.attributeType.equals(Definitions.CONFIG_DN_ATTRIBUTE_TYPE_COMBOBOX_LIST_PERSONAL)
                                                                                        && rsComponentFromServer1.prefix.equals(sPrefixUIDPersonal+":"))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sUIDPersonal;
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_CN))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sDomainName;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sDomainName))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_O))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sOrganization;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sOrganization))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_E))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sEmailAddress;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sEmailAddress))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_telephoneNumber))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sTelephoneNumber;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sTelephoneNumber))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_L))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sLocality;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sLocality))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_ST))
                                                                                    {
                                                                                        rsComponentFromServer1.value = pPROVINCE_DESC;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(pPROVINCE_DESC))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(rsComponentFromServer1.code.equals(Definitions.CONFIG_COMPONENT_DN_TAG_C))
                                                                                    {
                                                                                        rsComponentFromServer1.value = sCountry;
                                                                                        if(rsComponentFromServer1.requireEnabled == true)
                                                                                        {
                                                                                            if("".equals(sCountry))
                                                                                            {
                                                                                                sVALID_CODE = "COMPONENT_INVALID";
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!"".equals(EscapeUtils.CheckTextNull(rsComponentFromServer1.value)))
                                                                                    {
                                                                                        DN += EscapeUtils.CheckTextNull(rsComponentFromServer1.code) + "=" + EscapeUtils.CheckTextNull(rsComponentFromServer1.prefix)
                                                                                            + CommonFunction.replaceStringCharaterSpecialDN(EscapeUtils.CheckTextNull(rsComponentFromServer1.value), true, false) + ", ";
                                                                                    }
                                                                                }
                                                                                //</editor-fold>
                                                                            }
                                                                        } else {
                                                                            sVALID_CODE = "PROFILE_INVALID";
                                                                        }
                                                                    } else {
                                                                        sVALID_CODE = "PROFILE_INVALID";
                                                                    }
                                                                } else {
                                                                    sVALID_CODE = "PROFILE_INVALID";
                                                                }
                                                                //</editor-fold>
                                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                                    DN = CommonFunction.subLastCharater(DN);
                                                                    String pPAST_CERTIFICATE_ID = String.valueOf(certID);
                                                                    String pCERTIFICATION_ATTR_TYPE_ID = String.valueOf(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_CHANGEINFO);
                                                                    String pCERT_ATTR_TYPE_CODE = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_CHANGEINFO;
                                                                    String CACoreSubject = "";
                                                                    CERTIFICATION_AUTHORITY[][] rsCA = new CERTIFICATION_AUTHORITY[1][];
                                                                    db.S_BO_API_CERTIFICATION_AUTHORITY_LIST_BY_NAME(pCERTIFICATION_AUTHORITY_CODE, Integer.parseInt(sessLanguageGlobal), rsCA);
                                                                    if(rsCA[0].length > 0){
                                                                        CACoreSubject = rsCA[0][0].CERTIFICATION_AUTHORITY_CORECA_SUBJECT;
                                                                    }

                                                                    String sEnterpriseCert = "";
                                                                    if(!"".equals(sPrefixUIDEnterprise) && !"".equals(sUIDEnterprise)) {
                                                                        sEnterpriseCert = CommonReferServlet.convertPrefixVNToEN(sPrefixUIDEnterprise + ":" + sUIDEnterprise, true);
                                                                    }
                                                                    String sPersonalCert = "";
                                                                    if(!"".equals(sPrefixUIDPersonal) && !"".equals(sUIDPersonal)) {
                                                                        sPersonalCert = CommonReferServlet.convertPrefixVNToEN(sPrefixUIDPersonal + ":" + sUIDPersonal, false);
                                                                    }

                                                                    //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                                                    CERTIFICATION_DATA_ATTR tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                                    ObjectMapper objectMapper = new ObjectMapper();
                                                                    tempLogReq.personalName = sPersonalName;
                                                                    tempLogReq.companyName = sDomainName;
                                                                    tempLogReq.domainName = sDomainName;
                                                                    tempLogReq.enterpriseID = sEnterpriseCert;
                                                                    tempLogReq.personalID = sPersonalCert;
                                                                    tempLogReq.deviceUUID = pDEVICE;
                                                                    tempLogReq.emailContract = EMAIL_CONTRACT;
                                                                    tempLogReq.phoneContract = PHONE_CONTRACT;
                                                                    tempLogReq.issuerSubject = CACoreSubject;
                                                                    tempLogReq.subjectDn = DN;
                                                                    tempLogReq.tokenSn = sTOKEN_SN;
                                                                    tempLogReq.provinceName = pPROVINCE_DESC;
                                                                    tempLogReq.pkiFromFactorId = pPKI_FORMFACTOR_ID;
                                                                    tempLogReq.typeName = pCERT_ATTR_TYPE_CODE;
                                                                    String strReq = objectMapper.writeValueAsString(tempLogReq);
                                                                    db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                                            Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                                            Definitions.CONFIG_LOG_FUNCTIONALITY_REQUEST_CHANGE_INFO, strReq,
                                                                            USER_LOG, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                                    if("1".equals(sKeepCertificateSNEnabled)) {
                                                                        sRevokeOldCertificateEnabled = "0";
                                                                    }
                                                                    ATTRIBUTE_VALUES valueATTR;
                                                                    ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                                    dataATTR.setCertificationData(tempLogReq);
                                                                    valueATTR = new ATTRIBUTE_VALUES();
                                                                    valueATTR.setTokenSn(sTOKEN_SN);
                                                                    boolean sBoChangeKeyEnabled = "1".equals(sChangeKeyEnabled);
                                                                    valueATTR.setChangeKeyEnabled(sBoChangeKeyEnabled);
                                                                    boolean sRevokedEnabled = "1".equals(sRevokeOldCertificateEnabled);
                                                                    valueATTR.setRevokeOldCertificateEnabled(sRevokedEnabled);
                                                                    boolean sDeleteInTokenEnabled = "1".equals(sDeleteCertificateEnabled);
                                                                    valueATTR.setDeleteOldCertificateEnabled(sDeleteInTokenEnabled);
                                                                    boolean booKeepCertSNEnabled = "1".equals(sKeepCertificateSNEnabled);
                                                                    valueATTR.setKeepCertificateSNEnabled(booKeepCertSNEnabled);
                                                                    valueATTR.setTypeName(pCERT_ATTR_TYPE_CODE);
                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                    valueATTR.setCreateUser(loginFullname + " (" + USER_LOG + ")");
                                                                    valueATTR.setCreateDt(new Date());
                                                                    valueATTR.setAttributeData(dataATTR);
                                                                    //</editor-fold>
                                                                    int[] pCERTIFICATE_ATTR_ID = new int[1];
                                                                    int[] pCERTIFICATE_ID = new int[1];
                                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                    String pCREATE_USERID = "";
                                                                    BACKOFFICE_USER[][] rsUser = new BACKOFFICE_USER[1][];
                                                                    db.S_BO_USER_GET_BY_USERNAME(pCREATE_USER, rsUser);
                                                                    if(rsUser[0].length > 0) {
                                                                        pCREATE_USERID = String.valueOf(rsUser[0][0].ID);
                                                                    }
                                                                    String sParam = db.S_BO_CERTIFICATION_INSERT(Integer.parseInt(sTOKEN_ID), pCERTIFICATE_PROFILE_ID, sTOKEN_SN,
                                                                            pCERTIFICATION_SN, sPersonalName, sCompanyName, sDomainName, "", "", "",
                                                                            "", DN, CACoreSubject, PHONE_CONTRACT, EMAIL_CONTRACT, sAGENT_ID,
                                                                            pPAST_CERTIFICATE_ID, pCERTIFICATION_ATTR_TYPE_ID, pPROVINCE_ID, "",
                                                                            strReqValueATTR, pCREATE_USERID, USER_LOG, pCERTIFICATE_ATTR_ID, pCERTIFICATE_ID, pCSR,
                                                                            sBackupKeyEnabled, sChangeKeyEnabled, "", pPKI_FORMFACTOR_ID, pDEVICE,
                                                                            String.valueOf(pCERTIFICATION_OWNER_ID), "", null, "", sPersonalCert, sEnterpriseCert);
                                                                    if ("0".equals(sParam)) {
                                                                        if(!"".equals(pCOMPONENT_SAN)) {
                                                                            db.S_BO_CERTIFICATION_UPDATE_PROPERTIES(String.valueOf(pCERTIFICATE_ID[0]), pCOMPONENT_SAN, USER_LOG);
                                                                        }
                                                                        //<editor-fold defaultstate="collapsed" desc="### SHARED_MODE CERT">
                                                                        boolean pSHARED_MODE_ENABLED = false;
                                                                        BRANCH[][] rsBranchShare = new BRANCH[1][];
                                                                        db.S_BO_BRANCH_DETAIL(sAGENT_ID, rsBranchShare);
                                                                        if(rsBranchShare[0].length > 0) {
                                                                            sCERT_POLICY_PROPERTIES = EscapeUtils.CheckTextNull(rsBranchShare[0][0].CERTIFICATION_POLICY_PROPERTIES);
                                                                            if(!"".equals(sCERT_POLICY_PROPERTIES)) {
                                                                                pSHARED_MODE_ENABLED = CommonFunction.getShareModeEnabledCert(sCERT_POLICY_PROPERTIES);
                                                                            }
                                                                        }
                                                                        String pSHARED_MODE_UPDATE = pSHARED_MODE_ENABLED ? "1" : "0";
                                                                        db.S_BO_CERTIFICATION_UPDATE(pCERTIFICATE_ID[0], pCERTIFICATE_PROFILE_ID, sPersonalName, sCompanyName, sDomainName,
                                                                            "", "", "", "", DN, "", "", "", "", sBackupKeyEnabled, USER_LOG, "", "", "", pDISCOUNT_RATE,
                                                                            pSHARED_MODE_UPDATE, "", "", sEnterpriseCert, sPersonalCert);
                                                                        //</editor-fold>

                                                                        if (AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                                            //<editor-fold defaultstate="collapsed" desc="CA PROCESS">
                                                                            boolean isCAApprove = false;
                                                                            if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)) {
                                                                                isCAApprove = true;
                                                                            } else {
                                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                                    isCAApprove = true;
                                                                                }
                                                                            }
                                                                            if(isCAApprove == true) {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                valueATTR.setApproveCAUser(loginFullname + " (" + USER_LOG + ")");
                                                                                valueATTR.setApproveCADt(new Date());
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                                if ("0".equals(sApprove)) {
                                                                                    if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true)
                                                                                    {
                                                                                        if("0".equals(sNoAllowTranferToken)) {
                                                                                            if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                                db.S_BO_TOKEN_UPDATE_BRANCH_RESPONSIBLE(sTOKEN_ID, sAGENT_ID, USER_LOG);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if ("1".equals(sDiscountRateOption)) {
                                                                                        CommonReferServlet.updateDiscountRate(String.valueOf(pCERTIFICATE_ID[0]), sAGENT_ID, pCERTIFICATE_PROFILE_ID,
                                                                                            "","","","", USER_LOG, "","", sEnterpriseCert, sPersonalCert);
                                                                                    }
                                                                                    if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                                        || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID)))
                                                                                    {
                                                                                        String isActiveSignServer = "0";
                                                                                        GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                        if (sessGeneralPolicy1[0].length > 0) {
                                                                                            for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                            {
                                                                                                if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                                    isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                    break;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if("1".equals(isActiveSignServer)) {
                                                                                            CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT.trim(), sessLanguageGlobal);
                                                                                        } else {
                                                                                            ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                            dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                            int[] intWSRes = new int[1];
                                                                                            String[] sWSRes = new String[1];
                                                                                            ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                            if (intWSRes[0] == 0) { }
                                                                                            else {
                                                                                                isProcess = false;
                                                                                            }
                                                                                        }
                                                                                    } else { }
                                                                                }
                                                                            } else {
                                                                                valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                valueATTR.setApproveDt(new Date());
                                                                                strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                            }
                                                                            //</editor-fold>
                                                                        } else {
                                                                            //<editor-fold defaultstate="collapsed" desc="AGENCY PROCESS">
                                                                            boolean checkCallApproveCA = false;
                                                                            String SessLevelBranch = sessionsa.getAttribute("sessLevelBranch").toString().trim();
                                                                            if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)
                                                                                || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_AGENT_SURVEYOR))
                                                                            {
                                                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                    valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                    valueATTR.setApproveDt(new Date());
                                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], CommonFunction.GenJSONTokenATTR(valueATTR), USER_LOG);
                                                                                    if("0".equals(paramPre)) {
                                                                                        checkCallApproveCA = true;
                                                                                    }
                                                                                } else {
                                                                                    int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                    BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                    db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                    if(rsUserApprve[0].length > 0) {
                                                                                        for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                            if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                                approveChilrenID = item.ID;
                                                                                                break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                                    valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                    valueATTR.setApproveDt(new Date());
                                                                                    String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG, approveChilrenID);
                                                                                    if("0".equals(paramAgency)) {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                            valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                            valueATTR.setApproveDt(new Date());
                                                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                            String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                                            if("0".equals(paramPre)) {
                                                                                                checkCallApproveCA = true;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                if(SessLevelBranch.equals(Definitions.CONFIG_BRANCH_LEVEL_CHILREN_ONE)) {
                                                                                    if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                    {
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                        String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                                        if("0".equals(paramPre)) {
                                                                                            checkCallApproveCA = true;
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    int intApprove = db.S_BO_CHECK_BRANCH_APPROVED(pCERTIFICATE_ATTR_ID[0], Integer.parseInt(SessUserAgentID), sessTreeArrayBranchID);
                                                                                    if(intApprove == 1) {
                                                                                        if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_IN_AGENCY_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                        {
                                                                                            int approveChilrenID = Integer.parseInt(userLoginID);
                                                                                            BACKOFFICE_USER[][] rsUserApprve = new BACKOFFICE_USER[1][];
                                                                                            db.S_BO_GET_USER_BRANCH_ALL(SessUserAgentID, rsUserApprve);
                                                                                            if(rsUserApprve[0].length > 0) {
                                                                                                for(BACKOFFICE_USER item : rsUserApprve[0]) {
                                                                                                    if(String.valueOf(item.ROLE_ID).equals(Definitions.CONFIG_ROLE_ID_AGENT_ADMIN)) {
                                                                                                        approveChilrenID = item.ID;
                                                                                                        break;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                                                            valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                            valueATTR.setApproveDt(new Date());
                                                                                            String paramAgency = db.S_BO_CERTIFICATION_PRE_APPROVED_BY_LOW_LEVEL_BRANCH(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG, approveChilrenID);
                                                                                            if("0".equals(paramAgency)) {
                                                                                                if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_PRE_APPROVED,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true)
                                                                                                {
                                                                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                                                                    valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                                    valueATTR.setApproveDt(new Date());
                                                                                                    strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                                    String paramPre = db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                                                    if("0".equals(paramPre)) {
                                                                                                        checkCallApproveCA = true;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            if(checkCallApproveCA == true) {
                                                                                boolean autoApproveCA = false;
                                                                                CERTIFICATION_PROFILE[][] rsProfile1 = new CERTIFICATION_PROFILE[1][];
                                                                                db.S_BO_CERTIFICATION_PROFILE_DETAIL(pCERTIFICATE_PROFILE_ID, rsProfile1);
                                                                                CERTIFICATION_POLICY_DATA[][] sessPolicyCert_Data = (CERTIFICATION_POLICY_DATA[][]) request.getSession(false).getAttribute("SessPolicyCert_Data");
                                                                                if(rsProfile1[0].length > 0) {
                                                                                    autoApproveCA = CommonFunction.getApproveCAOfProfile(EscapeUtils.CheckTextNull(rsProfile1[0][0].NAME), sessPolicyCert_Data);
                                                                                }
                                                                                if(autoApproveCA == true) {
                                                                                    if(CommonFunction.checkApproveCAReqType(pCERT_ATTR_TYPE_CODE, sessPolicyCert_Data) == true)
                                                                                    {
                                                                                        valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                                                        valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                                                        valueATTR.setApproveDt(new Date());
                                                                                        valueATTR.setApproveCAUser(loginFullname + " (" + USER_LOG + ")");
                                                                                        valueATTR.setApproveCADt(new Date());
                                                                                        strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                                                        String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                                                        if ("0".equals(sApprove)) {
                                                                                            if(CommonFunction.checkHardTokenIDEnabled(pPKI_FORMFACTOR_ID) == true) {
                                                                                                if("0".equals(sNoAllowTranferToken)) {
                                                                                                    if(!sAGENT_ID.equals(sAGENT_ID_OLD)) {
                                                                                                        db.S_BO_TOKEN_UPDATE_BRANCH_RESPONSIBLE(sTOKEN_ID, sAGENT_ID, USER_LOG);
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            if ("1".equals(sDiscountRateOption)) {
                                                                                                CommonReferServlet.updateDiscountRate(String.valueOf(pCERTIFICATE_ID[0]), sAGENT_ID, pCERTIFICATE_PROFILE_ID,
                                                                                                    "","","","", USER_LOG, "","", sEnterpriseCert, sPersonalCert);
                                                                                            }
                                                                                            if(sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SSL_ID))
                                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_SIGNSERVER_ID))
                                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_CODESIGNNING_ID))
                                                                                                || sTOKEN_ID.equals(String.valueOf(Definitions.CONFIG_TOKEN_DEVICE_ID))) {
                                                                                                String isActiveSignServer = "0";
                                                                                                GENERAL_POLICY[][] sessGeneralPolicy1 = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
                                                                                                if (sessGeneralPolicy1[0].length > 0) {
                                                                                                    for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy1[0])
                                                                                                    {
                                                                                                        if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_ACTIVATED_SIGNSERVER_ENABLED)) {
                                                                                                            isActiveSignServer = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
                                                                                                            break;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                if("1".equals(isActiveSignServer)) {
                                                                                                    CommonReferServlet.actionSendMailHSM(sessGeneralPolicy1, String.valueOf(pCERTIFICATE_ID[0]), DN, EMAIL_CONTRACT.trim(), sessLanguageGlobal);
                                                                                                } else {
                                                                                                    ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                                                                                    dbTwo.S_BO_CERTIFICATION_ATTR_UPDATE_ACTIVATED_ENABLED(pCERTIFICATE_ATTR_ID[0], 1);
                                                                                                    int[] intWSRes = new int[1];
                                                                                                    String[] sWSRes = new String[1];
                                                                                                    ConnectConnector.EnrollCertificate(sTOKEN_SN, strPasswordP12, String.valueOf(pCERTIFICATE_ATTR_ID[0]), intWSRes, sWSRes);
                                                                                                    if (intWSRes[0] == 0) { }
                                                                                                    else {
                                                                                                        isProcess = false;
                                                                                                    }
                                                                                                }
                                                                                            } else { }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            //</editor-fold>
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        //</editor-fold>
                                                    } else {
                                                        //<editor-fold defaultstate="collapsed" desc="RSSP">
                                                        sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                        //</editor-fold>
                                                    }
                                                }

                                                //<editor-fold defaultstate="collapsed" desc="### RESPONSE CODE">
                                                if(null != sVALID_CODE)
                                                switch (sVALID_CODE) {
                                                    case "OK":
                                                        success = success + 1;
                                                        break;
                                                    case "CERT_PEDDING":
                                                        failed = failed + 1;
                                                        failedCERT_PEDDING = failedCERT_PEDDING + 1;
                                                        sFailCERT_PEDDING = sFailCERT_PEDDING + sSTT +",";
                                                        break;
                                                    case "AGENCY_NOT_PROFILE":
                                                        failed = failed + 1;
                                                        failedNOT_PROFILE = failedNOT_PROFILE + 1;
                                                        sFailNOT_PROFILE = sFailNOT_PROFILE + sSTT +",";
                                                        break;
                                                    case "BENEFICIARYUSER_INVALID":
                                                        failed = failed + 1;
                                                        failedBENEFICIARYUSER_INVALID = failedBENEFICIARYUSER_INVALID + 1;
                                                        sFailBENEFICIARYUSER_INVALID = sFailBENEFICIARYUSER_INVALID + sSTT +",";
                                                        break;
                                                    case "COMPONENT_INVALID":
                                                        failed = failed + 1;
                                                        failedCOMPONENT_INVALID= failedCOMPONENT_INVALID + 1;
                                                        sFailCOMPONENT_INVALID = sFailCOMPONENT_INVALID + sSTT +",";
                                                        break;
                                                    case "DN_INVALID":
                                                        failed = failed + 1;
                                                        failedDN_INVALID = failedDN_INVALID + 1;
                                                        sFailDN_INVALID= sFailDN_INVALID + sSTT +",";
                                                        break;
                                                    case "PROFILE_INVALID":
                                                        failed = failed + 1;
                                                        failedPROFILE_INVALID = failedPROFILE_INVALID + 1;
                                                        sFailPROFILE_INVALID= sFailPROFILE_INVALID + sSTT +",";
                                                        break;
                                                    case "PHONE_INVALID":
                                                        failed = failed + 1;
                                                        failedPHONE_INVALID = failedPHONE_INVALID + 1;
                                                        sFailPHONE_INVALID= sFailPHONE_INVALID + sSTT +",";
                                                        break;
                                                    case "EMAIL_INVALID":
                                                        failed = failed + 1;
                                                        failedEMAIL_INVALID = failedEMAIL_INVALID + 1;
                                                        sFailEMAIL_INVALID= sFailEMAIL_INVALID + sSTT +",";
                                                        break;
                                                    case "PREFIX_INVALID":
                                                        failed = failed + 1;
                                                        failedPREFIX_INVALID = failedPREFIX_INVALID + 1;
                                                        sFailPREFIX_INVALID= sFailPREFIX_INVALID + sSTT +",";
                                                        break;
                                                    case "ISSUE_ERROR":
                                                        failed = failed + 1;
                                                        failedISSUE_ERROR = failedISSUE_ERROR + 1;
                                                        sFailISSUE_ERROR = sFailISSUE_ERROR + sSTT +",";
                                                        break;
                                                    case "APPROVE_ERROR":
                                                        failed = failed + 1;
                                                        failedAPPROVE_ERROR = failedAPPROVE_ERROR + 1;
                                                        sFailAPPROVE_ERROR = sFailAPPROVE_ERROR + sSTT +",";
                                                        break;
                                                    case "PROVINCE_INVALID":
                                                        failed = failed + 1;
                                                        failedPROVINCE_INVALID = failedPROVINCE_INVALID + 1;
                                                        sFailPROVINCE_INVALID= sFailPROVINCE_INVALID + sSTT +",";
                                                        break;
                                                    case "CSR_INVALID":
                                                        failed = failed + 1;
                                                        failedCSR = failedCSR + 1;
                                                        sFailCSR = sFailCSR + sSTT +",";
                                                        break;
                                                    case "CSR_KEY":
                                                        failed = failed + 1;
                                                        failedKeyCSR = failedKeyCSR + 1;
                                                        sFailKeyCSR = sFailKeyCSR + sSTT +",";
                                                        break;
                                                    case "METHOD_INVALID":
                                                        failed = failed + 1;
                                                        failedMethod = failedMethod + 1;
                                                        sFailMethod = sFailMethod + sSTT +",";
                                                        break;
                                                    case "CERTIFICATETYPE_INVALID":
                                                        failed = failed + 1;
                                                        failedCertType = failedCertType + 1;
                                                        sFailCertType = sFailCertType + sSTT +",";
                                                        break;
                                                    case "2052":
                                                        failed = failed + 1;
                                                        failedTypeOwnerInvalid = failedTypeOwnerInvalid + 1;
                                                        sFailTypeOwnerInvalid = sFailTypeOwnerInvalid + sSTT +",";
                                                        break;
                                                    case "2053":
                                                        failed = failed + 1;
                                                        failedOwner_incomplete = failedOwner_incomplete + 1;
                                                        sFailOwner_incomplete = sFailOwner_incomplete + sSTT +",";
                                                        break;
                                                    default:
                                                        break;
                                                }
//                                                  //</editor-fold>
                                            }
                                            String sSumShow = "";
                                            //<editor-fold defaultstate="collapsed" desc="### RETURN">
                                            if (failed != 0)
                                            {
                                                sSumShow = "1";
                                                String sSum="";
                                                if(failedCERT_PEDDING != 0) {
                                                    sSum = sSum + "The Certificate Is pedding in system, import failure: " + sColumnSTT + "- " + sFailCERT_PEDDING + "\n";
                                                }
                                                if(failedNOT_PROFILE != 0) {
                                                    sSum = sSum + "Service pack configuration for agent does not exist: " + sColumnSTT + "- " + sFailNOT_PROFILE + "\n";
                                                }
                                                if(failedBENEFICIARYUSER_INVALID != 0){
                                                    sSum = sSum + "Beneficiaty User Is Invalid: " + sColumnSTT + "- " + sFailBENEFICIARYUSER_INVALID + "\n";
                                                }
                                                if(failedCERT_INVALID != 0) {
                                                    sSum = sSum + "The certificate does not exist in the system: " + sColumnSTT + "- " + sFailCERT_INVALID + "\n";
                                                }
                                                if(failedAGENCY_DENIED != 0) {
                                                    sSum = sSum + "Certificate access request was denied: " + sColumnSTT + "- " + sFailAGENCY_DENIED + "\n";
                                                }
                                                if(failedCOMPONENT_INVALID != 0) {
                                                    sSum = sSum + "Certificate Information Is Invalid: " + sColumnSTT + "- " + sFailCOMPONENT_INVALID + "\n";
                                                }
                                                if(failedDN_INVALID != 0) {
                                                    sSum = sSum + "Output string Subject DN Error: " + sColumnSTT + "- " + sFailDN_INVALID + "\n";
                                                }
                                                if(failedPROFILE_INVALID != 0) {
                                                    sSum = sSum + "Certificate Profile Is Invalid: " + sColumnSTT + "- " + sFailPROFILE_INVALID + "\n";
                                                }
                                                if(failedPHONE_INVALID != 0) {
                                                    sSum = sSum + "Customer Phone Number Is Invalid: " + sColumnSTT + "- " + sFailPHONE_INVALID + "\n";
                                                }
                                                if(failedEMAIL_INVALID != 0) {
                                                    sSum = sSum + "Customer Email Is Invalid: " + sColumnSTT + "- " + sFailEMAIL_INVALID + "\n";
                                                }
                                                if(failedPREFIX_INVALID != 0) {
                                                    sSum = sSum + "Prefix UID Is Invalid: " + sColumnSTT + "- " + sFailPREFIX_INVALID + "\n";
                                                }
                                                if(failedAPPROVE_ERROR != 0) {
                                                    sSum = sSum + "Approve CA certificates Is Errors: " + sColumnSTT + "- " + sFailAPPROVE_ERROR + "\n";
                                                }
                                                if(failedISSUE_ERROR != 0) {
                                                    sSum = sSum + "Issuing a certificate of error: " + sColumnSTT + "- " + sFailISSUE_ERROR + "\n";
                                                }
                                                if(failedPROVINCE_INVALID != 0) {
                                                    sSum = sSum + "Province Code Is Invalid: " + sColumnSTT + "- " + sFailPROVINCE_INVALID + "\n";
                                                }
                                                if(failedCSR != 0) {
                                                    sSum = sSum + "CSR Is Invalid: " + sColumnSTT + "- " + sFailCSR + "\n";
                                                }
                                                if(failedKeyCSR != 0) {
                                                    sSum = sSum + "Key Size Is Invalid: " + sColumnSTT + "- " + sFailKeyCSR + "\n";
                                                }
                                                if(failedMethod != 0) {
                                                    sSum = sSum + "Method Is Invalid: " + sColumnSTT + "- " + sFailMethod + "\n";
                                                }
                                                if(failedCertType != 0) {
                                                    sSum = sSum + "Certificate Type Is Invalid: " + sColumnSTT + "- " + sFailCertType + "\n";
                                                }
                                                if(failedTypeOwnerInvalid != 0) {
                                                    sSum = sSum + "Invalid information for this type of owner: " + sColumnSTT + "- " + sFailTypeOwnerInvalid + "\n";
                                                }
                                                if(failedOwner_incomplete != 0) {
                                                    sSum = sSum + "Certification owner has a queue incomplete: " + sColumnSTT + "- " + sFailOwner_incomplete + "\n";
                                                }
                                                sessionsa.setAttribute("sessRegisterCertImportFailed", sSum);
                                                CommonFunction.LogDebugString(log, "sessRegisterCertImportFailed-1", sSum);
                                            }
                                            //</editor-fold>
                                            strView = "0###" + String.valueOf(success) + "###" + String.valueOf(failed) + "###" + sSumShow;
                                        } else {
                                            strView = "1###" + sValueFailColumnName;
                                        }
                                        //</editor-fold>
                                    }  else if(Integer.parseInt(sCERTIFICATION_TYPEParam) == Definitions.CONFIG_SERVICE_TYPE_ID_SUSPEND) {
                                        int indexOfRequestType = 100;
                                        int indexOfSuspenTime = 100;
                                        int indexOfSuspendReason = 100;
                                        String sColumnRequestType = "RequestType";
                                        String sColumnSuspenTime = "SuspenTime";
                                        String sColumnSuspendReason = "SuspendReason";
                                        //<editor-fold defaultstate="collapsed" desc="### SUSPEND">
                                        //<editor-fold defaultstate="collapsed" desc="### GET COLUMN - CHECK COLUMN">
                                        for (int i = 0; i < cellStoreArrayList.size(); i++) {
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnSTT)) {
                                                indexOfSTT = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnCertificateSN)) {
                                                indexOfCertificateSN = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnRequestType)) {
                                                indexOfRequestType = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnSuspenTime)) {
                                                indexOfSuspenTime = i;
                                            }
                                            if (cellStoreArrayList.get(i).toString().trim().equals(sColumnSuspendReason)) {
                                                indexOfSuspendReason = i;
                                            }
                                        }
                                        boolean booFailColumnName = true;
                                        String sValueFailColumnName = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                        if (indexOfCertificateSN == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfRequestType == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfSuspenTime == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        if (indexOfSuspendReason == 100) {
                                            sValueFailColumnName = Definitions.CONFIG_ERROR_EXCEL_TOKEN_SN;
                                            booFailColumnName = false;
                                        }
                                        //</editor-fold>

                                        if (booFailColumnName == true) {
                                            for (int i = 1; i < dataHolder.size(); i++) {
                                                String sVALID_CODE = Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS;
                                                cellStoreArrayList = (ArrayList) dataHolder.get(i);
                                                String sSTT = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfSTT).toString(), true);
                                                String sCertificateSN = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfCertificateSN).toString(), false);
                                                String sRequestType = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfRequestType).toString(), false);
                                                String sSuspenTime = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfSuspenTime).toString(), false);
                                                String sSuspendReason = CommonFunction.processDataColumnExcel(cellStoreArrayList.get(indexOfSuspendReason).toString(), false);
                                                ROLE_DATA[][] sessFunctionCert = (ROLE_DATA[][]) sessionsa.getAttribute("SessRoleSet_Cert");
                                                boolean isAccessAgency = true;
                                                String PHONE_CONTRACT = "";
                                                String EMAIL_CONTRACT = "";
                                                String pCERTIFICATION_SN = "";
                                                String pPERSONAL_NAME = "";
                                                String pCOMPANY_NAME = "";
                                                String pTAX_CODE = "";
                                                String pBUDGET_CODE = "";
                                                String pP_ID = "";
                                                String pCCCD = "";
                                                String pPASSPORT = "";
                                                String pSUBJECT = EscapeUtils.CheckTextNull(request.getParameter("DN"));
                                                String sOLD_TOKEN_ID = "";
                                                String pISSUER_SUBJECT = "";
                                                String pPROVINCE_ID = "";
                                                String pCERTIFICATION_AUTHORITY_ID = "";
                                                String sTOKEN_SN = "";
                                                int pPKI_FORMFACTOR_ID = 0;
                                                int pCERTIFICATION_ID =0;
                                                int certID = 0;
                                                String sAGENT_ID = "";

                                                //<editor-fold defaultstate="collapsed" desc="### CERTIFICATE DETAIL GET">
                                                CertificateInfo[][] rsReqID = new CertificateInfo[1][];
                                                int[] pRESPONSE_CODE = new int[1];
                                                db.S_BO_API_CERTIFICATION_GET_INFO("", "", "", "", sCertificateSN, 0,
                                                    Definitions.CONFIG_CERTIFICATION_STATE_CODE_OPERATED, "", Integer.parseInt(sessLanguageGlobal), pRESPONSE_CODE, rsReqID, "", "", "", "");
                                                if (rsReqID[0].length > 0) {
                                                    certID = rsReqID[0][0].certificateID;
                                                }
                                                if(certID == 0) {
                                                    sVALID_CODE = "CERT_INVALID";
                                                } else {
                                                    CERTIFICATION[][] rsReq = new CERTIFICATION[1][];
                                                    db.S_BO_CERTIFICATION_DETAIL(String.valueOf(certID), sessLanguageGlobal, rsReq);
                                                    if (rsReq[0].length > 0) {
                                                        pCERTIFICATION_ID = rsReq[0][0].ID;
                                                        sOLD_TOKEN_ID = String.valueOf(rsReq[0][0].TOKEN_ID);
                                                        sTOKEN_SN = EscapeUtils.CheckTextNull(rsReq[0][0].TOKEN_SN);
                                                        PHONE_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].PHONE_CONTRACT);
                                                        EMAIL_CONTRACT = EscapeUtils.CheckTextNull(rsReq[0][0].EMAIL_CONTRACT);
                                                        pPERSONAL_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].PERSONAL_NAME);
                                                        pCOMPANY_NAME = EscapeUtils.CheckTextNull(rsReq[0][0].COMPANY_NAME);
                                                        pTAX_CODE = EscapeUtils.CheckTextNull(rsReq[0][0].TAX_CODE);
                                                        pBUDGET_CODE = EscapeUtils.CheckTextNull(rsReq[0][0].BUDGET_CODE);
                                                        sAGENT_ID = String.valueOf(rsReq[0][0].BRANCH_ID);
                                                        pP_ID = EscapeUtils.CheckTextNull(rsReq[0][0].P_ID);
                                                        pCCCD = EscapeUtils.CheckTextNull(rsReq[0][0].P_EID);
                                                        pPASSPORT = EscapeUtils.CheckTextNull(rsReq[0][0].PASSPORT);
                                                        pISSUER_SUBJECT = EscapeUtils.CheckTextNull(rsReq[0][0].ISSUER_SUBJECT);
                                                        pCERTIFICATION_SN = EscapeUtils.CheckTextNull(rsReq[0][0].CERTIFICATION_SN);
                                                        pCERTIFICATION_AUTHORITY_ID = String.valueOf(rsReq[0][0].CERTIFICATION_AUTHORITY_ID);
                                                        pPROVINCE_ID = String.valueOf(rsReq[0][0].CITY_PROVINCE_ID);
                                                        pPKI_FORMFACTOR_ID = rsReq[0][0].PKI_FORMFACTOR_ID;
                                                        if (!AGENT_ID_LOG.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                            BRANCH[][] branchAccess = (BRANCH[][]) sessionsa.getAttribute("sessTreeBranchSystem");
                                                            isAccessAgency = CommonFunction.checkBranchTreeInvalidCert(rsReq[0][0].BRANCH_ID, branchAccess);
                                                            if(isAccessAgency == false) {
                                                                sVALID_CODE = "AGENCY_DENIED";
                                                            }
                                                        }
                                                    } else {
                                                        sVALID_CODE = "REQUEST_INVALID";
                                                    }
                                                }
                                                //</editor-fold>
                                                
                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                    if(!sRequestType.equals(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE)
                                                        && !sRequestType.equals(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE)) {
                                                        sVALID_CODE = "REQUEST_INVALID";
                                                    }
                                                }

                                                if(sVALID_CODE.equals(Definitions.CONFIG_RESPONSE_CODE_PROGRAMMER_SUCCESS)) {
                                                    int sCERT_ATTR_TYPE_ID = 0;
                                                    String pFUNCTIONALITY_NAME = "";
                                                    java.sql.Timestamp sSUSPEND_TIME_DB = null;
                                                    if(sRequestType.equals(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_PERMANENT_DISABLE)) {
                                                        sCERT_ATTR_TYPE_ID = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_PERMANENT_DISABLE;
                                                        pFUNCTIONALITY_NAME = Definitions.CONFIG_LOG_FUNCTIONALITY_PERMANENT_DISABLE;
                                                    } else if(sRequestType.equals(Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_CODE_TEMPORARY_DISABLE)) {
                                                        sCERT_ATTR_TYPE_ID = Definitions.CONFIG_CERTIFICATION_ATTR_TYPE_ID_TEMPORARY_DISABLE;
                                                        pFUNCTIONALITY_NAME = Definitions.CONFIG_LOG_FUNCTIONALITY_TEMPORARY_DISABLE;
                                                        sSUSPEND_TIME_DB = CommonFunction.ConvertStringToTimeStamp(sSuspenTime);
                                                    }
                                                    String pPAST_CERTIFICATE_ID = String.valueOf(certID);
                                                    //<editor-fold defaultstate="collapsed" desc="### VALUE ATTR - LOG FILE - LOG_SYSTEM">
                                                    CERTIFICATION_DATA_ATTR tempLogReq;
                                                    tempLogReq = new CERTIFICATION_DATA_ATTR();
                                                    ObjectMapper objectMapper = new ObjectMapper();
                                                    tempLogReq.personalName = pPERSONAL_NAME;
                                                    tempLogReq.companyName = pCOMPANY_NAME;
                                                    tempLogReq.taxCode = pTAX_CODE;
                                                    tempLogReq.budgetCode = pBUDGET_CODE;
                                                    tempLogReq.personalCode = pP_ID;
                                                    tempLogReq.citizenCode = pCCCD;
                                                    tempLogReq.passportCode = pPASSPORT;
                                                    tempLogReq.emailContract = EMAIL_CONTRACT;
                                                    tempLogReq.phoneContract = PHONE_CONTRACT;
                                                    tempLogReq.issuerSubject = pISSUER_SUBJECT;
                                                    tempLogReq.subjectDn = pSUBJECT;
                                                    tempLogReq.tokenSn = sTOKEN_SN;
                                                    tempLogReq.pkiFromFactorId = pPKI_FORMFACTOR_ID;
                                                    tempLogReq.typeName = sRequestType;
                                                    String strReq = objectMapper.writeValueAsString(tempLogReq);
                                                    db.S_BO_SYSTEM_LOG_INSERT(Definitions.CONFIG_LOG_SOURCE_ENTITY_NAME,
                                                            Definitions.CONFIG_LOG_DESTINATION_ENTITY_NAME, sTOKEN_SN, "",
                                                            pFUNCTIONALITY_NAME, strReq, USER_LOG, System_Log_ID, sIP_Request, sysLog_BillCode);
                                                    CommonFunction.LogDebugString(log, "RegistrationCertificate", "SuspendedCert: " + "SUBJECT: " + pSUBJECT
                                                            + "; pPERSONAL_NAME: " + pPERSONAL_NAME + "; pCOMPANY_NAME: " + pCOMPANY_NAME
                                                            + "; pTAX_CODE: " + pTAX_CODE
                                                            + "; pPKI_FORMFACTOR_ID: " + pPKI_FORMFACTOR_ID
                                                            + "; pBUDGET_CODE: " + pBUDGET_CODE + "; pP_ID: " + pP_ID + "; pCCCD: " + pCCCD + "; pPASSPORT: "
                                                            + pPASSPORT + "; pPAST_CERTIFICATE_ID: " + pPAST_CERTIFICATE_ID
                                                            + "; pCERTIFICATION_ATTR_TYPE_CODE: " + sRequestType + "; EMAIL_CONTRACT: " + EMAIL_CONTRACT
                                                            + "; ISSUER_SUBJECT: " + pISSUER_SUBJECT + "; PHONE_CONTRACT: " + PHONE_CONTRACT
                                                            + "; TOKEN_ID_NEW: " + sOLD_TOKEN_ID + "; TOKEN_SN_NEW: " + sTOKEN_SN + "; CITY_PROVINCE_ID: " + pPROVINCE_ID);
                                                    ATTRIBUTE_VALUES valueATTR;
                                                    ATTRIBUTE_DATA dataATTR = new ATTRIBUTE_DATA();
                                                    dataATTR.setCertificationData(tempLogReq);
                                                    valueATTR = new ATTRIBUTE_VALUES();
                                                    valueATTR.setTokenSn(sTOKEN_SN);
                                                    valueATTR.setCerttificateSuspendReason(sSuspendReason);
                                                    valueATTR.setSuspendedTime(sSUSPEND_TIME_DB);
                                                    valueATTR.setTypeName(sRequestType);
                                                    valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PENDING);
                                                    valueATTR.setCreateUser(loginFullname + " (" + USER_LOG + ")");
                                                    valueATTR.setCreateDt(new Date());
                                                    valueATTR.setAttributeData(dataATTR);
                                                    //</editor-fold>
                                                    int[] pCERTIFICATE_ATTR_ID = new int[1];
                                                    String strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                    String sParam = db.S_BO_CERTIFICATION_ATTR_INSERT(String.valueOf(certID), String.valueOf(sCERT_ATTR_TYPE_ID),
                                                        strReqValueATTR, USER_LOG, pCERTIFICATE_ATTR_ID);
                                                    if ("0".equals(sParam)) {
                                                        boolean isCAApprove = false;
                                                        if (EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN)
                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)
                                                            || EscapeUtils.CheckTextNull(sessionsa.getAttribute("RoleID_ID").toString().trim()).equals(Definitions.CONFIG_ROLE_ID_CA_ADMIN_CHILD)) {
                                                            isCAApprove = true;
                                                        } else {
                                                            if(CommonFunction.CheckRoleFuncValid(Definitions.CONFIG_ROLE_PROPERTIES_CERT_APPROVED_CERT,Definitions.CONFIG_ROLE_ATTRIBUTE_TYPE_CERT_REQUUEST, sessFunctionCert) == true) {
                                                                isCAApprove = true;
                                                            }
                                                        }
                                                        if(isCAApprove == true) {
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            valueATTR.setApproveCAUser(loginFullname + " (" + USER_LOG + ")");
                                                            valueATTR.setApproveCADt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            String sApprove = db.S_BO_CERTIFICATION_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                            if ("0".equals(sApprove)) {
                                                                int[] intRes = new int[1];
                                                                String[] sRes = new String[1];
                                                                // RACONNECTOR
                                                                String pCA_NAME = "";
                                                                CERTIFICATION_AUTHORITY[][] rsCERTIFICATION_AUTHORITY = new CERTIFICATION_AUTHORITY[1][];
                                                                db.S_BO_CERTIFICATION_AUTHORITY_DETAIL(pCERTIFICATION_AUTHORITY_ID, rsCERTIFICATION_AUTHORITY);
                                                                if (rsCERTIFICATION_AUTHORITY[0].length > 0) {
                                                                    pCA_NAME = EscapeUtils.CheckTextNull(rsCERTIFICATION_AUTHORITY[0][0].NAME);
                                                                }
                                                                //###sCERT_REVOCATION_REASON
                                                                ConnectConnector.RevokeCertificate(sTOKEN_SN, pCERTIFICATION_SN, Definitions.CONFIG_CERTIFICATION_REVOKE_REASON_CERTIFICATEHOLD_ID,
                                                                    pCA_NAME, "", intRes, sRes, pCERTIFICATION_ID, pCERTIFICATE_ATTR_ID[0]);
                                                                if (intRes[0] == 0) {
                                                                    String pCOMMENT = "";
                                                                    objectMapper = new ObjectMapper();
                                                                    CERTIFICATION_COMMENT jsonCertComment = new CERTIFICATION_COMMENT();
                                                                    jsonCertComment.certificateDeclineReason = "";
                                                                    jsonCertComment.certificateRevokeReason = "";
                                                                    jsonCertComment.certificateSuspendReason = sSuspendReason;
                                                                    pCOMMENT = objectMapper.writeValueAsString(jsonCertComment);
                                                                    db.S_BO_CERTIFICATION_DISABLE(String.valueOf(pCERTIFICATE_ATTR_ID[0]), sSUSPEND_TIME_DB, pCOMMENT, USER_LOG);
                                                                    sessionsa.setAttribute("RefreshRenewCertSess", "1");
                                                                } else {
                                                                    sVALID_CODE = "ERROR_CORECA_CALL";
                                                                }
                                                            } else {
                                                                CommonFunction.LogDebugString(log, "ERROR S_BO_CERTIFICATION_APPROVED - RESPONSE", sApprove);
                                                                sVALID_CODE = "ERROR_APPROVED";
                                                            }
                                                        } else {
                                                            valueATTR.setRequestState(Definitions.CONFIG_CERTIFICATION_ATTR_STATE_CODE_PRE_APPROVED);
                                                            valueATTR.setApproveUser(loginFullname + " (" + USER_LOG + ")");
                                                            valueATTR.setApproveDt(new Date());
                                                            strReqValueATTR = CommonFunction.GenJSONTokenATTR(valueATTR);
                                                            db.S_BO_CERTIFICATION_PRE_APPROVED(pCERTIFICATE_ATTR_ID[0], strReqValueATTR, USER_LOG);
                                                        }
                                                    }
                                                }

                                                //<editor-fold defaultstate="collapsed" desc="### RESPONSE CODE">
                                                if(null != sVALID_CODE)
                                                switch (sVALID_CODE) {
                                                    case "OK":
                                                        success = success + 1;
                                                        break;
                                                    case "REQUEST_INVALID":
                                                        failed = failed + 1;
                                                        failedCERT_PEDDING = failedCERT_PEDDING + 1;
                                                        sFailCERT_PEDDING = sFailCERT_PEDDING + sSTT +",";
                                                        break;
                                                    case "ERROR_CORECA_CALL":
                                                        failed = failed + 1;
                                                        failedNOT_PROFILE = failedNOT_PROFILE + 1;
                                                        sFailNOT_PROFILE = sFailNOT_PROFILE + sSTT +",";
                                                        break;
                                                    case "AGENCY_DENIED":
                                                        failed = failed + 1;
                                                        failedBENEFICIARYUSER_INVALID = failedBENEFICIARYUSER_INVALID + 1;
                                                        sFailBENEFICIARYUSER_INVALID = sFailBENEFICIARYUSER_INVALID + sSTT +",";
                                                        break;
                                                    case "ERROR_APPROVED":
                                                        failed = failed + 1;
                                                        failedDN_INVALID = failedDN_INVALID + 1;
                                                        sFailDN_INVALID = sFailDN_INVALID + sSTT +",";
                                                        break;
                                                    default:
                                                        break;
                                                }
//                                              //</editor-fold>
                                            }
                                            String sSumShow = "";
                                            //<editor-fold defaultstate="collapsed" desc="### RETURN">
                                            if (failed != 0)
                                            {
                                                sSumShow = "1";
                                                String sSum="";
                                                if(failedCERT_PEDDING != 0) {
                                                    sSum = sSum + "Request Is Invalid, import failure: " + sColumnSTT + "- " + sFailCERT_PEDDING + "\n";
                                                }
                                                if(failedNOT_PROFILE != 0) {
                                                    sSum = sSum + "Error issuing certificate: " + sColumnSTT + "- " + sFailNOT_PROFILE + "\n";
                                                }
                                                if(failedBENEFICIARYUSER_INVALID != 0) {
                                                    sSum = sSum + "Agency Is Invalid: " + sColumnSTT + "- " + sFailBENEFICIARYUSER_INVALID + "\n";
                                                }
                                                if(failedDN_INVALID != 0) {
                                                    sSum = sSum + "Error CA Approval: " + sColumnSTT + "- " + sFailDN_INVALID + "\n";
                                                }
                                                sessionsa.setAttribute("sessRegisterCertImportFailed", sSum);
                                                CommonFunction.LogDebugString(log, "sessRegisterCertImportFailed-1", sSum);
                                            }
                                            //</editor-fold>
                                            strView = "0###" + String.valueOf(success) + "###" + String.valueOf(failed) + "###" + sSumShow;
                                        } else {
                                            strView = "1###" + sValueFailColumnName;
                                        }
                                        //</editor-fold>
                                    }
                                } else {
                                    strView = "NOT_SIZE###0###"+sCountExcelRequire;
                                }
//                                } else {
//                                    strView = "NOT_CA###0";
//                                }
                            } else {
                                strView = "FORMAT_INVALIE###0";
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
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#" + e.getMessage();
            } catch (Exception e) {
                CommonFunction.LogExceptionServlet(log, e.toString().trim(), e);
                strView = Definitions.CONFIG_EXCEPTION_STRING_ERROR + "#" + e.getMessage();
            } finally {
                if (thanhtv != null) {
                    thanhtv.close();
                }
                if ((new File(fileUploaded)).exists()) {
                    (new File(fileUploaded)).delete();
                }
            }
            out.println(strView);
        }
    }
    
    public static ArrayList readExcelCertAllXLSX(String sLinkFile, int intType) throws IOException
    {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            if(intType == Definitions.CONFIG_SERVICE_TYPE_ID_REGISTRATION) {
                //<editor-fold defaultstate="collapsed" desc="REGISTER">
                while (rowIter.hasNext()) {
                    XSSFRow myRow = (XSSFRow) rowIter.next();
                    ArrayList cellStoreArrayList = new ArrayList();
                    XSSFCell cellSTT = myRow.getCell(0);
                    XSSFCell cellCertificateType = myRow.getCell(1);
                    XSSFCell cellMethod = myRow.getCell(2);
                    XSSFCell cellPersonalName = myRow.getCell(3);
                    XSSFCell cellCompanyName = myRow.getCell(4);
                    XSSFCell cellDomainName = myRow.getCell(5);
                    XSSFCell cellOrganization = myRow.getCell(6);
                    XSSFCell cellPrefixUIDEnterprise = myRow.getCell(7);
                    XSSFCell cellUIDEnterprise = myRow.getCell(8);
                    XSSFCell cellPrefixUIDPersonal = myRow.getCell(9);
                    XSSFCell cellUIDPersonal = myRow.getCell(10);
//                    XSSFCell cellTaxCode = myRow.getCell(7);
//                    XSSFCell cellBudgetCode = myRow.getCell(8);
//                    XSSFCell cellDecision = myRow.getCell(9);
//                    XSSFCell cellPersonalID = myRow.getCell(10);
//                    XSSFCell cellCitizenID = myRow.getCell(11);
//                    XSSFCell cellPassport = myRow.getCell(12);
                    XSSFCell cellOrganizationUnit = myRow.getCell(11);
                    XSSFCell cellTitle = myRow.getCell(12);
                    XSSFCell cellEmailAddress = myRow.getCell(13);
                    XSSFCell cellTelephoneNumber = myRow.getCell(14);
                    XSSFCell cellLocality = myRow.getCell(15);
                    XSSFCell cellStateProvince = myRow.getCell(16);
                    XSSFCell cellCountry = myRow.getCell(17);
                    XSSFCell cellCertificateProfile = myRow.getCell(18);
                    XSSFCell cellCustomerPhoneNumer = myRow.getCell(19);
                    XSSFCell cellCustomerEmail = myRow.getCell(20);
                    XSSFCell cellBeneficiaryUser = myRow.getCell(21);
                    XSSFCell cellBackupKeyEnabled = myRow.getCell(22);
                    XSSFCell cellCSR = myRow.getCell(23);
                    XSSFCell cellOrganizationUnit2 = myRow.getCell(24);
                    XSSFCell cellOrganizationUnit3 = myRow.getCell(25);
                    XSSFCell cellOrganizationUnit4 = myRow.getCell(26);
                    XSSFCell cellEmailSANAddress = myRow.getCell(27);
                    XSSFCell cellDNSName1 = myRow.getCell(28);
                    XSSFCell cellDNSName2 = myRow.getCell(29);
                    XSSFCell cellDNSName3 = myRow.getCell(30);
                    XSSFCell cellDNSName4 = myRow.getCell(31);
                    XSSFCell cellIPAddress = myRow.getCell(32);
                    XSSFCell cellIPAddress2 = myRow.getCell(33);
                    XSSFCell cellIPAddress3 = myRow.getCell(34);
                    if (CheckCellXSSFEmpty(cellSTT) == null && CheckCellXSSFEmpty(cellCertificateType) == null
                        && CheckCellXSSFEmpty(cellMethod) == null && CheckCellXSSFEmpty(cellPersonalName) == null
                        && CheckCellXSSFEmpty(cellCompanyName) == null && CheckCellXSSFEmpty(cellDomainName) == null
                        && CheckCellXSSFEmpty(cellOrganization) == null && CheckCellXSSFEmpty(cellPrefixUIDEnterprise) == null
                        && CheckCellXSSFEmpty(cellUIDEnterprise) == null && CheckCellXSSFEmpty(cellPrefixUIDPersonal) == null
                        && CheckCellXSSFEmpty(cellUIDPersonal) == null && CheckCellXSSFEmpty(cellOrganizationUnit) == null
                        && CheckCellXSSFEmpty(cellTitle) == null && CheckCellXSSFEmpty(cellEmailAddress) == null
                        && CheckCellXSSFEmpty(cellTelephoneNumber) == null && CheckCellXSSFEmpty(cellLocality) == null
                        && CheckCellXSSFEmpty(cellStateProvince) == null && CheckCellXSSFEmpty(cellCountry) == null
                        && CheckCellXSSFEmpty(cellCertificateProfile) == null && CheckCellXSSFEmpty(cellCustomerPhoneNumer) == null
                        && CheckCellXSSFEmpty(cellCustomerEmail) == null && CheckCellXSSFEmpty(cellBeneficiaryUser) == null
                        && CheckCellXSSFEmpty(cellBackupKeyEnabled) == null && CheckCellXSSFEmpty(cellCSR) == null
                        && CheckCellXSSFEmpty(cellOrganizationUnit2) == null && CheckCellXSSFEmpty(cellOrganizationUnit3) == null
                        && CheckCellXSSFEmpty(cellOrganizationUnit4) == null && CheckCellXSSFEmpty(cellEmailSANAddress) == null
                        && CheckCellXSSFEmpty(cellDNSName1) == null && CheckCellXSSFEmpty(cellDNSName2) == null
                        && CheckCellXSSFEmpty(cellDNSName3) == null && CheckCellXSSFEmpty(cellDNSName4) == null
                        && CheckCellXSSFEmpty(cellIPAddress) == null && CheckCellXSSFEmpty(cellIPAddress2) == null && CheckCellXSSFEmpty(cellIPAddress3) == null) {

                    } else {
                        if (cellSTT == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                        }
                        if (cellCertificateType == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateType.toString()));
                        }
                        if (cellMethod == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellMethod.toString()));
                        }
                        if (cellPersonalName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellPersonalName.toString()));
                        }
                        if (cellCompanyName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCompanyName.toString()));
                        }
                        if (cellDomainName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDomainName.toString()));
                        }
                        if (cellOrganization == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganization.toString()));
                        }
                        if (cellPrefixUIDEnterprise == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellPrefixUIDEnterprise.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellPrefixUIDEnterprise.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellPrefixUIDEnterprise.toString()));
                            }
                        }
                        if (cellUIDEnterprise == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellUIDEnterprise.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellUIDEnterprise.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellUIDEnterprise.toString()));
                            }
                        }
                        if (cellPrefixUIDPersonal == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellPrefixUIDPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellPrefixUIDPersonal.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellPrefixUIDPersonal.toString()));
                            }
                        }
                        if (cellUIDPersonal == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellUIDPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellUIDPersonal.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellUIDPersonal.toString()));
                            }
                        }
                        if (cellOrganizationUnit == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganizationUnit.toString()));
                        }
                        if (cellTitle == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellTitle.toString()));
                        }
                        if (cellEmailAddress == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellEmailAddress.toString()));
                        }
                        if (cellTelephoneNumber == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellTelephoneNumber.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellTelephoneNumber.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellTelephoneNumber.toString()));
                            }
                        }
                        if (cellLocality == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellLocality.toString()));
                        }
                        if (cellStateProvince == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellStateProvince.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellStateProvince.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellStateProvince.toString()));
                            }
                        }
                        if (cellCountry == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCountry.toString()));
                        }
                        if (cellCertificateProfile == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateProfile.toString()));
                        }
                        if (cellCustomerPhoneNumer == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellCustomerPhoneNumer.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellCustomerPhoneNumer.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellCustomerPhoneNumer.toString()));
                            }
                        }
                        if (cellCustomerEmail == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCustomerEmail.toString()));
                        }
                        if (cellBeneficiaryUser == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBeneficiaryUser.toString()));
                        }
                        if (cellBackupKeyEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBackupKeyEnabled.toString()));
                        }
                        if (cellCSR == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCSR.toString()));
                        }
                        if (cellOrganizationUnit2 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganizationUnit2.toString()));
                        }
                        if (cellOrganizationUnit3 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganizationUnit3.toString()));
                        }
                        if (cellOrganizationUnit4 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganizationUnit4.toString()));
                        }
                        if (cellEmailSANAddress == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellEmailSANAddress.toString()));
                        }
                        if (cellDNSName1 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDNSName1.toString()));
                        }
                        if (cellDNSName2 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDNSName2.toString()));
                        }
                        if (cellDNSName3 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDNSName3.toString()));
                        }
                        if (cellDNSName4 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDNSName4.toString()));
                        }
                        if (cellIPAddress == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellIPAddress.toString()));
                        }
                        if (cellIPAddress2 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellIPAddress2.toString()));
                        }
                        if (cellIPAddress3 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellIPAddress3.toString()));
                        }
                        cellArrayLisstHolder.add(cellStoreArrayList);
                    }
                }
                //</editor-fold>
            } else if(intType == Definitions.CONFIG_SERVICE_TYPE_ID_RENEWAL) {
                //<editor-fold defaultstate="collapsed" desc="RENEW">
                while (rowIter.hasNext()) {
                    XSSFRow myRow = (XSSFRow) rowIter.next();
                    ArrayList cellStoreArrayList = new ArrayList();
                    XSSFCell cellSTT = myRow.getCell(0);
                    XSSFCell cellCertificateSN = myRow.getCell(1);
                    XSSFCell cellCertificateProfile = myRow.getCell(2);
                    XSSFCell cellBeneficiaryUser = myRow.getCell(3);
                    XSSFCell cellBackupKeyEnabled = myRow.getCell(4);
                    XSSFCell RevokeOldCertificateEnabled = myRow.getCell(5);
                    XSSFCell ChangeKeyEnabled = myRow.getCell(6);
                    XSSFCell DeleteCertificateEnabled = myRow.getCell(7);
                    XSSFCell KeepCertificateSNEnabled = myRow.getCell(8);
                    XSSFCell cellCSR = myRow.getCell(9);
                    if (CheckCellXSSFEmpty(cellSTT) == null && CheckCellXSSFEmpty(cellCertificateSN) == null
                        && CheckCellXSSFEmpty(cellCertificateProfile) == null && CheckCellXSSFEmpty(RevokeOldCertificateEnabled) == null
                        && CheckCellXSSFEmpty(ChangeKeyEnabled) == null && CheckCellXSSFEmpty(DeleteCertificateEnabled) == null
                        && CheckCellXSSFEmpty(KeepCertificateSNEnabled) == null && CheckCellXSSFEmpty(cellCSR) == null
                        && CheckCellXSSFEmpty(cellBackupKeyEnabled) == null) {

                    } else {
                        if (cellSTT == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                        }
                        if (cellCertificateSN == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateSN.toString()));
                        }
                        if (cellCertificateProfile == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateProfile.toString()));
                        }
                        if (RevokeOldCertificateEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(RevokeOldCertificateEnabled.toString()));
                        }
                        if (ChangeKeyEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(ChangeKeyEnabled.toString()));
                        }
                        if (KeepCertificateSNEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(KeepCertificateSNEnabled.toString()));
                        }
                        if (DeleteCertificateEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(DeleteCertificateEnabled.toString()));
                        }
                        if (cellBeneficiaryUser == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBeneficiaryUser.toString()));
                        }
                        if (cellBackupKeyEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBackupKeyEnabled.toString()));
                        }
                        if (cellCSR == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCSR.toString()));
                        }
                        cellArrayLisstHolder.add(cellStoreArrayList);
                    }
                }
                //</editor-fold>
            } else if(intType == Definitions.CONFIG_SERVICE_TYPE_ID_CHANGEINFO) {
                //<editor-fold defaultstate="collapsed" desc="CHANGEINFO">
                while (rowIter.hasNext()) {
                    XSSFRow myRow = (XSSFRow) rowIter.next();
                    ArrayList cellStoreArrayList = new ArrayList();
                    XSSFCell cellSTT = myRow.getCell(0);
                    XSSFCell cellCertificateSN = myRow.getCell(1);
                    XSSFCell cellCertificateAuthority = myRow.getCell(2);
                    XSSFCell cellPersonalName = myRow.getCell(3);
                    XSSFCell cellCompanyName = myRow.getCell(4);
                    XSSFCell cellDomainName = myRow.getCell(5);
                    XSSFCell cellOrganization = myRow.getCell(6);
                    XSSFCell cellPrefixUIDEnterprise = myRow.getCell(7);
                    XSSFCell cellUIDEnterprise = myRow.getCell(8);
                    XSSFCell cellPrefixUIDPersonal = myRow.getCell(9);
                    XSSFCell cellUIDPersonal = myRow.getCell(10);
//                    XSSFCell cellTaxCode = myRow.getCell(7);
//                    XSSFCell cellBudgetCode = myRow.getCell(8);
//                    XSSFCell cellDecision = myRow.getCell(9);
//                    XSSFCell cellPersonalID = myRow.getCell(10);
//                    XSSFCell cellCitizenID = myRow.getCell(11);
//                    XSSFCell cellPassport = myRow.getCell(12);
                    XSSFCell cellOrganizationUnit = myRow.getCell(11);
                    XSSFCell cellTitle = myRow.getCell(12);
                    XSSFCell cellEmailAddress = myRow.getCell(13);
                    XSSFCell cellTelephoneNumber = myRow.getCell(14);
                    XSSFCell cellLocality = myRow.getCell(15);
                    XSSFCell cellStateProvince = myRow.getCell(16);
                    XSSFCell cellCountry = myRow.getCell(17);
                    XSSFCell cellBeneficiaryUser = myRow.getCell(18);
                    XSSFCell cellBackupKeyEnabled = myRow.getCell(19);
                    XSSFCell RevokeOldCertificateEnabled = myRow.getCell(20);
                    XSSFCell ChangeKeyEnabled = myRow.getCell(21);
                    XSSFCell DeleteCertificateEnabled = myRow.getCell(22);
                    XSSFCell KeepCertificateSNEnabled = myRow.getCell(23);
                    XSSFCell cellCSR = myRow.getCell(24);
                    if (CheckCellXSSFEmpty(cellSTT) == null && CheckCellXSSFEmpty(cellCertificateSN) == null
                        && CheckCellXSSFEmpty(cellCertificateAuthority) == null && CheckCellXSSFEmpty(cellPersonalName) == null
                        && CheckCellXSSFEmpty(cellCompanyName) == null && CheckCellXSSFEmpty(cellDomainName) == null
                        && CheckCellXSSFEmpty(cellOrganization) == null && CheckCellXSSFEmpty(cellPrefixUIDEnterprise) == null
                        && CheckCellXSSFEmpty(cellUIDEnterprise) == null && CheckCellXSSFEmpty(cellPrefixUIDPersonal) == null
                        && CheckCellXSSFEmpty(cellUIDPersonal) == null && CheckCellXSSFEmpty(cellOrganizationUnit) == null
                        && CheckCellXSSFEmpty(cellTitle) == null && CheckCellXSSFEmpty(cellEmailAddress) == null
                        && CheckCellXSSFEmpty(cellTelephoneNumber) == null && CheckCellXSSFEmpty(cellLocality) == null
                        && CheckCellXSSFEmpty(cellStateProvince) == null && CheckCellXSSFEmpty(cellCountry) == null
                        && CheckCellXSSFEmpty(cellBeneficiaryUser) == null && CheckCellXSSFEmpty(cellBackupKeyEnabled) == null
                        && CheckCellXSSFEmpty(cellCSR) == null && CheckCellXSSFEmpty(RevokeOldCertificateEnabled) == null
                        && CheckCellXSSFEmpty(ChangeKeyEnabled) == null && CheckCellXSSFEmpty(DeleteCertificateEnabled) == null
                        && CheckCellXSSFEmpty(KeepCertificateSNEnabled) == null) {

                    } else {
                        if (cellSTT == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                        }
                        if (cellCertificateSN == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateSN.toString()));
                        }
                        if (cellCertificateAuthority == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateAuthority.toString()));
                        }
                        if (cellPersonalName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellPersonalName.toString()));
                        }
                        if (cellCompanyName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCompanyName.toString()));
                        }
                        if (cellDomainName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDomainName.toString()));
                        }
                        if (cellOrganization == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganization.toString()));
                        }
                        if (cellPrefixUIDEnterprise == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellPrefixUIDEnterprise.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellPrefixUIDEnterprise.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellPrefixUIDEnterprise.toString()));
                            }
                        }
                        if (cellUIDEnterprise == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellUIDEnterprise.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellUIDEnterprise.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellUIDEnterprise.toString()));
                            }
                        }
                        if (cellPrefixUIDPersonal == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellPrefixUIDPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellPrefixUIDPersonal.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellPrefixUIDPersonal.toString()));
                            }
                        }
                        if (cellUIDPersonal == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellUIDPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellUIDPersonal.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellUIDPersonal.toString()));
                            }
                        }
                        if (cellOrganizationUnit == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganizationUnit.toString()));
                        }
                        if (cellTitle == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellTitle.toString()));
                        }
                        if (cellEmailAddress == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellEmailAddress.toString()));
                        }
                        if (cellTelephoneNumber == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellTelephoneNumber.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellTelephoneNumber.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellTelephoneNumber.toString()));
                            }
                        }
                        if (cellLocality == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellLocality.toString()));
                        }
                        if (cellStateProvince == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellStateProvince.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellStateProvince.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellStateProvince.toString()));
                            }
                        }
                        if (cellCountry == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCountry.toString()));
                        }
                        if (cellBeneficiaryUser == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBeneficiaryUser.toString()));
                        }
                        if (cellBackupKeyEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBackupKeyEnabled.toString()));
                        }
                        if (RevokeOldCertificateEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(RevokeOldCertificateEnabled.toString()));
                        }
                        if (ChangeKeyEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(ChangeKeyEnabled.toString()));
                        }
                        if (DeleteCertificateEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(DeleteCertificateEnabled.toString()));
                        }
                        if (KeepCertificateSNEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(KeepCertificateSNEnabled.toString()));
                        }
                        if (cellCSR == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCSR.toString()));
                        }
                        cellArrayLisstHolder.add(cellStoreArrayList);
                    }
                }
                //</editor-fold>
            } else if(intType == Definitions.CONFIG_SERVICE_TYPE_ID_SUSPEND) {
                //<editor-fold defaultstate="collapsed" desc="SUSPEND">
                while (rowIter.hasNext()) {
                    XSSFRow myRow = (XSSFRow) rowIter.next();
                    ArrayList cellStoreArrayList = new ArrayList();
                    XSSFCell cellSTT = myRow.getCell(0);
                    XSSFCell cellCertificateSN = myRow.getCell(1);
                    XSSFCell cellRequestType = myRow.getCell(2);
                    XSSFCell cellSuspenTime = myRow.getCell(3);
                    XSSFCell cellSuspendReason = myRow.getCell(4);
                    if (CheckCellXSSFEmpty(cellSTT) == null && CheckCellXSSFEmpty(cellCertificateSN) == null
                        && CheckCellXSSFEmpty(cellRequestType) == null && CheckCellXSSFEmpty(cellSuspenTime) == null
                        && CheckCellXSSFEmpty(cellSuspendReason) == null) {

                    } else {
                        if (cellSTT == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                        }
                        if (cellCertificateSN == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateSN.toString()));
                        }
                        if (cellRequestType == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellRequestType.toString()));
                        }
                        if (cellSuspenTime == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSuspenTime.toString()));
                        }
                        if (cellSuspendReason == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSuspendReason.toString()));
                        }
                        cellArrayLisstHolder.add(cellStoreArrayList);
                    }
                }
                //</editor-fold>
            }
        } catch (Exception e) {
            log.error("readExcelCertAllXLSX: " + e.getMessage() + ".\n-----------------------------------", e);
        }
        return cellArrayLisstHolder;
    }
    public static ArrayList readExcelCertAllXLS(String sLinkFile, int intType) throws IOException
    {
        ArrayList cellArrayLisstHolder = new ArrayList();
        try (InputStream myInput = new FileInputStream(sLinkFile)) {
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            if(intType == Definitions.CONFIG_SERVICE_TYPE_ID_REGISTRATION) {
                //<editor-fold defaultstate="collapsed" desc="REGISTER">
                while (rowIter.hasNext()) {
                    HSSFRow myRow = (HSSFRow) rowIter.next();
                    ArrayList cellStoreArrayList = new ArrayList();
                    HSSFCell cellSTT = myRow.getCell(0);
                    HSSFCell cellCertificateType = myRow.getCell(1);
                    HSSFCell cellMethod = myRow.getCell(2);
                    HSSFCell cellPersonalName = myRow.getCell(3);
                    HSSFCell cellCompanyName = myRow.getCell(4);
                    HSSFCell cellDomainName = myRow.getCell(5);
                    HSSFCell cellOrganization = myRow.getCell(6);
                    HSSFCell cellPrefixUIDEnterprise = myRow.getCell(7);
                    HSSFCell cellUIDEnterprise = myRow.getCell(8);
                    HSSFCell cellPrefixUIDPersonal = myRow.getCell(9);
                    HSSFCell cellUIDPersonal = myRow.getCell(10);
//                    HSSFCell cellTaxCode = myRow.getCell(7);
//                    HSSFCell cellBudgetCode = myRow.getCell(8);
//                    HSSFCell cellDecision = myRow.getCell(9);
//                    HSSFCell cellPersonalID = myRow.getCell(10);
//                    HSSFCell cellCitizenID = myRow.getCell(11);
//                    HSSFCell cellPassport = myRow.getCell(12);
                    HSSFCell cellOrganizationUnit = myRow.getCell(11);
                    HSSFCell cellTitle = myRow.getCell(12);
                    HSSFCell cellEmailAddress = myRow.getCell(13);
                    HSSFCell cellTelephoneNumber = myRow.getCell(14);
                    HSSFCell cellLocality = myRow.getCell(15);
                    HSSFCell cellStateProvince = myRow.getCell(16);
                    HSSFCell cellCountry = myRow.getCell(17);
                    HSSFCell cellCertificateProfile = myRow.getCell(18);
                    HSSFCell cellCustomerPhoneNumer = myRow.getCell(19);
                    HSSFCell cellCustomerEmail = myRow.getCell(20);
                    HSSFCell cellBeneficiaryUser = myRow.getCell(21);
                    HSSFCell cellBackupKeyEnabled = myRow.getCell(22);
                    HSSFCell cellCSR = myRow.getCell(23);
                    HSSFCell cellOrganizationUnit2 = myRow.getCell(24);
                    HSSFCell cellOrganizationUnit3 = myRow.getCell(25);
                    HSSFCell cellOrganizationUnit4 = myRow.getCell(26);
                    HSSFCell cellEmailSANAddress = myRow.getCell(27);
                    HSSFCell cellDNSName1 = myRow.getCell(28);
                    HSSFCell cellDNSName2 = myRow.getCell(29);
                    HSSFCell cellDNSName3 = myRow.getCell(30);
                    HSSFCell cellDNSName4 = myRow.getCell(31);
                    HSSFCell cellIPAddress = myRow.getCell(32);
                    HSSFCell cellIPAddress2 = myRow.getCell(33);
                    HSSFCell cellIPAddress3 = myRow.getCell(34);
                    if (CheckCellHSSFEmpty(cellSTT) == null && CheckCellHSSFEmpty(cellCertificateType) == null
                        && CheckCellHSSFEmpty(cellMethod) == null && CheckCellHSSFEmpty(cellPersonalName) == null
                        && CheckCellHSSFEmpty(cellCompanyName) == null && CheckCellHSSFEmpty(cellDomainName) == null
                        && CheckCellHSSFEmpty(cellOrganization) == null && CheckCellHSSFEmpty(cellPrefixUIDEnterprise) == null
                        && CheckCellHSSFEmpty(cellUIDEnterprise) == null && CheckCellHSSFEmpty(cellPrefixUIDPersonal) == null
                        && CheckCellHSSFEmpty(cellUIDPersonal) == null && CheckCellHSSFEmpty(cellOrganizationUnit) == null && CheckCellHSSFEmpty(cellTitle) == null
                        && CheckCellHSSFEmpty(cellEmailAddress) == null && CheckCellHSSFEmpty(cellTelephoneNumber) == null && CheckCellHSSFEmpty(cellLocality) == null
                        && CheckCellHSSFEmpty(cellStateProvince) == null && CheckCellHSSFEmpty(cellCountry) == null && CheckCellHSSFEmpty(cellCertificateProfile) == null
                        && CheckCellHSSFEmpty(cellCustomerPhoneNumer) == null && CheckCellHSSFEmpty(cellCustomerEmail) == null && CheckCellHSSFEmpty(cellBeneficiaryUser) == null
                        && CheckCellHSSFEmpty(cellBackupKeyEnabled) == null && CheckCellHSSFEmpty(cellCSR) == null
                        && CheckCellHSSFEmpty(cellOrganizationUnit2) == null && CheckCellHSSFEmpty(cellOrganizationUnit3) == null
                        && CheckCellHSSFEmpty(cellOrganizationUnit4) == null && CheckCellHSSFEmpty(cellEmailSANAddress) == null
                        && CheckCellHSSFEmpty(cellDNSName1) == null && CheckCellHSSFEmpty(cellDNSName2) == null
                        && CheckCellHSSFEmpty(cellDNSName3) == null && CheckCellHSSFEmpty(cellDNSName4) == null
                        && CheckCellHSSFEmpty(cellIPAddress) == null && CheckCellHSSFEmpty(cellIPAddress2) == null && CheckCellHSSFEmpty(cellIPAddress3) == null)
                    {

                    } else {
                        if (cellSTT == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                        }
                        if (cellCertificateType == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateType.toString()));
                        }
                        if (cellMethod == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellMethod.toString()));
                        }
                        if (cellPersonalName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellPersonalName.toString()));
                        }
                        if (cellCompanyName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCompanyName.toString()));
                        }
                        if (cellDomainName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDomainName.toString()));
                        }
                        if (cellOrganization == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganization.toString()));
                        }
                        if (cellPrefixUIDEnterprise == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellPrefixUIDEnterprise.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellPrefixUIDEnterprise.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellPrefixUIDEnterprise.toString()));
                            }
                        }
                        if (cellUIDEnterprise == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellUIDEnterprise.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellUIDEnterprise.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellUIDEnterprise.toString()));
                            }
                        }
                        if (cellPrefixUIDPersonal == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellPrefixUIDPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellPrefixUIDPersonal.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellPrefixUIDPersonal.toString()));
                            }
                        }
                        if (cellUIDPersonal == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellUIDPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellUIDPersonal.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellUIDPersonal.toString()));
                            }
                        }
                        if (cellOrganizationUnit == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganizationUnit.toString()));
                        }
                        if (cellTitle == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellTitle.toString()));
                        }
                        if (cellEmailAddress == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellEmailAddress.toString()));
                        }
                        if (cellTelephoneNumber == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellTelephoneNumber.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellTelephoneNumber.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellTelephoneNumber.toString()));
                            }
                        }
                        if (cellLocality == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellLocality.toString()));
                        }
                        if (cellStateProvince == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellStateProvince.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellStateProvince.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellStateProvince.toString()));
                            }
                        }
                        if (cellCountry == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCountry.toString()));
                        }
                        if (cellCertificateProfile == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateProfile.toString()));
                        }
                        if (cellCustomerPhoneNumer == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellCustomerPhoneNumer.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellCustomerPhoneNumer.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellCustomerPhoneNumer.toString()));
                            }
                        }
                        if (cellCustomerEmail == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCustomerEmail.toString()));
                        }
                        if (cellBeneficiaryUser == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBeneficiaryUser.toString()));
                        }
                        if (cellBackupKeyEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBackupKeyEnabled.toString()));
                        }
                        if (cellCSR == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCSR.toString()));
                        }
                        if (cellOrganizationUnit2 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganizationUnit2.toString()));
                        }
                        if (cellOrganizationUnit3 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganizationUnit3.toString()));
                        }
                        if (cellOrganizationUnit4 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganizationUnit4.toString()));
                        }
                        if (cellEmailSANAddress == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellEmailSANAddress.toString()));
                        }
                        if (cellDNSName1 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDNSName1.toString()));
                        }
                        if (cellDNSName2 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDNSName2.toString()));
                        }
                        if (cellDNSName3 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDNSName3.toString()));
                        }
                        if (cellDNSName4 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDNSName4.toString()));
                        }
                        if (cellIPAddress == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellIPAddress.toString()));
                        }
                        if (cellIPAddress2 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellIPAddress2.toString()));
                        }
                        if (cellIPAddress3 == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellIPAddress3.toString()));
                        }
                        cellArrayLisstHolder.add(cellStoreArrayList);
                    }
                }
                //</editor-fold>
            } else if(intType == Definitions.CONFIG_SERVICE_TYPE_ID_RENEWAL) {
                //<editor-fold defaultstate="collapsed" desc="RENEW">
                while (rowIter.hasNext()) {
                    HSSFRow myRow = (HSSFRow) rowIter.next();
                    ArrayList cellStoreArrayList = new ArrayList();
                    HSSFCell cellSTT = myRow.getCell(0);
                    HSSFCell cellCertificateSN = myRow.getCell(1);
                    HSSFCell cellCertificateProfile = myRow.getCell(2);
                    HSSFCell cellBeneficiaryUser = myRow.getCell(3);
                    HSSFCell cellBackupKeyEnabled = myRow.getCell(4);
                    HSSFCell RevokeOldCertificateEnabled = myRow.getCell(5);
                    HSSFCell ChangeKeyEnabled = myRow.getCell(6);
                    HSSFCell DeleteCertificateEnabled = myRow.getCell(7);
                    HSSFCell KeepCertificateSNEnabled = myRow.getCell(8);
                    HSSFCell cellCSR = myRow.getCell(9);
                    if (CheckCellHSSFEmpty(cellSTT) == null && CheckCellHSSFEmpty(cellCertificateSN) == null
                        && CheckCellHSSFEmpty(cellCertificateProfile) == null && CheckCellHSSFEmpty(RevokeOldCertificateEnabled) == null
                        && CheckCellHSSFEmpty(ChangeKeyEnabled) == null && CheckCellHSSFEmpty(DeleteCertificateEnabled) == null
                        && CheckCellHSSFEmpty(KeepCertificateSNEnabled) == null && CheckCellHSSFEmpty(cellCSR) == null
                        && CheckCellHSSFEmpty(cellBackupKeyEnabled) == null) {

                    } else {
                        if (cellSTT == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                        }
                        if (cellCertificateSN == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateSN.toString()));
                        }
                        if (cellCertificateProfile == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateProfile.toString()));
                        }
                        if (RevokeOldCertificateEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(RevokeOldCertificateEnabled.toString()));
                        }
                        if (ChangeKeyEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(ChangeKeyEnabled.toString()));
                        }
                        if (KeepCertificateSNEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(KeepCertificateSNEnabled.toString()));
                        }
                        if (DeleteCertificateEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(DeleteCertificateEnabled.toString()));
                        }
                        if (cellBeneficiaryUser == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBeneficiaryUser.toString()));
                        }
                        if (cellBackupKeyEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBackupKeyEnabled.toString()));
                        }
                        if (cellCSR == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCSR.toString()));
                        }
                        cellArrayLisstHolder.add(cellStoreArrayList);
                    }
                }
                //</editor-fold>
            } else if(intType == Definitions.CONFIG_SERVICE_TYPE_ID_CHANGEINFO) {
                //<editor-fold defaultstate="collapsed" desc="CHANGEINFO">
                while (rowIter.hasNext()) {
                    HSSFRow myRow = (HSSFRow) rowIter.next();
                    ArrayList cellStoreArrayList = new ArrayList();
                    HSSFCell cellSTT = myRow.getCell(0);
                    HSSFCell cellCertificateSN = myRow.getCell(1);
                    HSSFCell cellCertificateAuthority = myRow.getCell(2);
                    HSSFCell cellPersonalName = myRow.getCell(3);
                    HSSFCell cellCompanyName = myRow.getCell(4);
                    HSSFCell cellDomainName = myRow.getCell(5);
                    HSSFCell cellOrganization = myRow.getCell(6);
                    HSSFCell cellPrefixUIDEnterprise = myRow.getCell(7);
                    HSSFCell cellUIDEnterprise = myRow.getCell(8);
                    HSSFCell cellPrefixUIDPersonal = myRow.getCell(9);
                    HSSFCell cellUIDPersonal = myRow.getCell(10);
//                    HSSFCell cellTaxCode = myRow.getCell(7);
//                    HSSFCell cellBudgetCode = myRow.getCell(8);
//                    HSSFCell cellDecision = myRow.getCell(9);
//                    HSSFCell cellPersonalID = myRow.getCell(10);
//                    HSSFCell cellCitizenID = myRow.getCell(11);
//                    HSSFCell cellPassport = myRow.getCell(12);
                    HSSFCell cellOrganizationUnit = myRow.getCell(11);
                    HSSFCell cellTitle = myRow.getCell(12);
                    HSSFCell cellEmailAddress = myRow.getCell(13);
                    HSSFCell cellTelephoneNumber = myRow.getCell(14);
                    HSSFCell cellLocality = myRow.getCell(15);
                    HSSFCell cellStateProvince = myRow.getCell(16);
                    HSSFCell cellCountry = myRow.getCell(17);
                    HSSFCell cellBeneficiaryUser = myRow.getCell(18);
                    HSSFCell cellBackupKeyEnabled = myRow.getCell(19);
                    HSSFCell RevokeOldCertificateEnabled = myRow.getCell(20);
                    HSSFCell ChangeKeyEnabled = myRow.getCell(21);
                    HSSFCell DeleteCertificateEnabled = myRow.getCell(22);
                    HSSFCell KeepCertificateSNEnabled = myRow.getCell(23);
                    HSSFCell cellCSR = myRow.getCell(24);
                    if (CheckCellHSSFEmpty(cellSTT) == null && CheckCellHSSFEmpty(cellCertificateSN) == null
                        && CheckCellHSSFEmpty(cellCertificateAuthority) == null && CheckCellHSSFEmpty(cellPersonalName) == null
                        && CheckCellHSSFEmpty(cellCompanyName) == null && CheckCellHSSFEmpty(cellDomainName) == null
                        && CheckCellHSSFEmpty(cellOrganization) == null && CheckCellHSSFEmpty(cellPrefixUIDEnterprise) == null
                        && CheckCellHSSFEmpty(cellUIDEnterprise) == null && CheckCellHSSFEmpty(cellPrefixUIDPersonal) == null
                        && CheckCellHSSFEmpty(cellUIDPersonal) == null && CheckCellHSSFEmpty(cellOrganizationUnit) == null
                        && CheckCellHSSFEmpty(cellTitle) == null && CheckCellHSSFEmpty(cellEmailAddress) == null
                        && CheckCellHSSFEmpty(cellTelephoneNumber) == null && CheckCellHSSFEmpty(cellLocality) == null
                        && CheckCellHSSFEmpty(cellStateProvince) == null && CheckCellHSSFEmpty(cellCountry) == null
                        && CheckCellHSSFEmpty(cellBeneficiaryUser) == null && CheckCellHSSFEmpty(cellBackupKeyEnabled) == null
                        && CheckCellHSSFEmpty(cellCSR) == null && CheckCellHSSFEmpty(RevokeOldCertificateEnabled) == null
                        && CheckCellHSSFEmpty(ChangeKeyEnabled) == null && CheckCellHSSFEmpty(DeleteCertificateEnabled) == null
                        && CheckCellHSSFEmpty(KeepCertificateSNEnabled) == null) {

                    } else {
                        if (cellSTT == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                        }
                        if (cellCertificateSN == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateSN.toString()));
                        }
                        if (cellCertificateAuthority == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateAuthority.toString()));
                        }
                        if (cellPersonalName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellPersonalName.toString()));
                        }
                        if (cellCompanyName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCompanyName.toString()));
                        }
                        if (cellDomainName == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellDomainName.toString()));
                        }
                        if (cellOrganization == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganization.toString()));
                        }
                        if (cellPrefixUIDEnterprise == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellPrefixUIDEnterprise.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellPrefixUIDEnterprise.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellPrefixUIDEnterprise.toString()));
                            }
                        }
                        if (cellUIDEnterprise == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellUIDEnterprise.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellUIDEnterprise.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellUIDEnterprise.toString()));
                            }
                        }
                        if (cellPrefixUIDPersonal == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellPrefixUIDPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellPrefixUIDPersonal.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellPrefixUIDPersonal.toString()));
                            }
                        }
                        if (cellUIDPersonal == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellUIDPersonal.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellUIDPersonal.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellUIDPersonal.toString()));
                            }
                        }
                        if (cellOrganizationUnit == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellOrganizationUnit.toString()));
                        }
                        if (cellTitle == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellTitle.toString()));
                        }
                        if (cellEmailAddress == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellEmailAddress.toString()));
                        }
                        if (cellTelephoneNumber == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellTelephoneNumber.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellTelephoneNumber.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellTelephoneNumber.toString()));
                            }
                        }
                        if (cellLocality == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellLocality.toString()));
                        }
                        if (cellStateProvince == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            if(cellStateProvince.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                String sTempProcess = NumberToTextConverter.toText(cellStateProvince.getNumericCellValue());
                                cellStoreArrayList.add(CheckReplaceImport(sTempProcess));
                            } else {
                                cellStoreArrayList.add(CheckReplaceImport(cellStateProvince.toString()));
                            }
                        }
                        if (cellCountry == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCountry.toString()));
                        }
                        if (cellBeneficiaryUser == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBeneficiaryUser.toString()));
                        }
                        if (cellBackupKeyEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellBackupKeyEnabled.toString()));
                        }
                        if (RevokeOldCertificateEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(RevokeOldCertificateEnabled.toString()));
                        }
                        if (ChangeKeyEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(ChangeKeyEnabled.toString()));
                        }
                        if (DeleteCertificateEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(DeleteCertificateEnabled.toString()));
                        }
                        if (KeepCertificateSNEnabled == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(KeepCertificateSNEnabled.toString()));
                        }
                        if (cellCSR == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCSR.toString()));
                        }
                        cellArrayLisstHolder.add(cellStoreArrayList);
                    }
                }
                //</editor-fold>
            } else if(intType == Definitions.CONFIG_SERVICE_TYPE_ID_SUSPEND) {
                //<editor-fold defaultstate="collapsed" desc="SUSPEND">
                while (rowIter.hasNext()) {
                    HSSFRow myRow = (HSSFRow) rowIter.next();
                    ArrayList cellStoreArrayList = new ArrayList();
                    HSSFCell cellSTT = myRow.getCell(0);
                    HSSFCell cellCertificateSN = myRow.getCell(1);
                    HSSFCell cellRequestType = myRow.getCell(2);
                    HSSFCell cellSuspenTime = myRow.getCell(3);
                    HSSFCell cellSuspenReason = myRow.getCell(4);
                    if (CheckCellHSSFEmpty(cellSTT) == null && CheckCellHSSFEmpty(cellCertificateSN) == null
                        && CheckCellHSSFEmpty(cellRequestType) == null && CheckCellHSSFEmpty(cellSuspenTime) == null
                        && CheckCellHSSFEmpty(cellSuspenReason) == null) {

                    } else {
                        if (cellSTT == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSTT.toString()));
                        }
                        if (cellCertificateSN == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellCertificateSN.toString()));
                        }
                        if (cellRequestType == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellRequestType.toString()));
                        }
                        if (cellSuspenTime == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSuspenTime.toString()));
                        }
                        if (cellSuspenReason == null) {
                            cellStoreArrayList.add(Definitions.CONFIG_IMPORT_STRING_NULL);
                        } else {
                            cellStoreArrayList.add(CheckReplaceImport(cellSuspenReason.toString()));
                        }
                        cellArrayLisstHolder.add(cellStoreArrayList);
                    }
                }
                //</editor-fold>
            }
        } catch (Exception e) {
            System.out.println("readExcelCertAllXLS: " + e.getMessage() + ".\n-----------------------------------");
        }
        return cellArrayLisstHolder;
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
        processRequest(request, response);
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
        processRequest(request, response);
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
