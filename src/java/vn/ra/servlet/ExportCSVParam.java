/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.scene.input.DataFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.ra.object.BRANCH;
import vn.ra.object.CERTIFICATION;
import vn.ra.object.CERTIFICATION_CONTACT;
import vn.ra.object.CERTIFICATION_CONTROL_REPORT;
import vn.ra.object.CERTIFICATION_POLICY_DATA;
import vn.ra.object.CERTIFICATION_REPORT_SUMMARY;
import vn.ra.object.GENERAL_POLICY;
import vn.ra.object.PAYMENT;
import vn.ra.object.ProfileContactInfoJson;
import vn.ra.object.REPORT_PER_MONTH;
import vn.ra.object.REPORT_QUICK_BRANCH;
import vn.ra.object.TOKEN;
import vn.ra.process.CommonFunction;
import vn.ra.process.CommonReferServlet;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.ConnectDatabaseReport;
import vn.ra.process.ConnectDbPhaseTwo;
import vn.ra.process.EncodeSOPIN;
import vn.ra.utility.Config;
import vn.ra.utility.ConfigLanguage;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;
import vn.ra.utility.LoadParamSystem;

/**
 *
 * @author THANH-PC
 */
public class ExportCSVParam extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ExportCSVParam.class);
    private static final long serialVersionUID = 6106269076155338045L;

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
            ConnectDatabase com = new ConnectDatabase();
            String strView = "";
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
                    String loginUID = request.getSession(false).getAttribute("sUserID").toString().trim();
                    String idParam = request.getParameter("idParam");
                    if (null != idParam) {
                        switch (idParam) {
                            case "exportcertquick": {
                                //<editor-fold defaultstate="collapsed" desc="exportcertquick">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                String sessTreeArrayBranchID = request.getSession(false).getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                    Config conf = new Config();
                                    ConfigLanguage confLanguage = new ConfigLanguage();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_QUICK + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    CommonFunction.LogDebugString(log, "ExportCertificateQuick", "PathURL: " + strURLPath);
                                    REPORT_QUICK_BRANCH[][] rsReportBranch = new REPORT_QUICK_BRANCH[1][];
                                    String FromCreateDate = request.getSession(false).getAttribute("sessFromCreateDateReportQuickAgent").toString().trim();
                                    String ToCreateDate = request.getSession(false).getAttribute("sessToCreateDateReportQuickAgent").toString().trim();
                                    String idBranchOffice = request.getSession(false).getAttribute("sessBranchOfficeReportQuickAgent").toString().trim();
                                    String idBRANCH_STATE = request.getSession(false).getAttribute("sessBranchStateReportQuickAgent").toString().trim();
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
                                        idBranchOffice = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBRANCH_STATE)) {
                                        idBRANCH_STATE = "";
                                    }
                                    com.S_BO_REPORT_TOTAL_BRANCH(FromCreateDate, ToCreateDate, EscapeUtils.escapeHtmlSearch(idBranchOffice), idBRANCH_STATE,
                                        loginLanguage, rsReportBranch, SessUserAgentID, sessTreeArrayBranchID);
                                    if (rsReportBranch[0].length > 0) {
                                        String queryString = getServletContext().getRealPath("/");
                                        String outputDirectory = queryString;
                                        String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                        FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                        inputStream.close();
                                        String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellInit = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_INIT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellActivation = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_OPERATION, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellWorkerRevoke = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_REVOKE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellSum = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOTAL, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                        wb.setCompressTempFiles(true);
                                        SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
                                        sheet.setRandomAccessWindowSize(100);
                                        sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                        sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
                                        sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_INIT);
                                        sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_ACTIVATION);
                                        sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REVOKE);
                                        sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_SUM);
                                        Row row = sheet.createRow(0);
                                        CellStyle my_style = wb.createCellStyle();
                                        Font font = wb.createFont();
                                        font.setBoldweight((short) 700);
                                        font.setFontName("Arial");
                                        my_style.setFont(font);
                                        my_style.setFillBackgroundColor((short) 9);
                                        my_style.setAlignment((short) 2);
                                        Cell cell = row.createCell((short) 0);
                                        cell.setCellValue(sCellSTT);
                                        cell.setCellStyle(my_style);

                                        Cell cell1 = row.createCell((short) 1);
                                        cell1.setCellValue(sCellAgency);
                                        cell1.setCellStyle(my_style);

                                        row.createCell((short) 2).setCellValue(sCellInit);
                                        Cell cell2 = row.createCell((short) 2);
                                        cell2.setCellValue(sCellInit);
                                        cell2.setCellStyle(my_style);

                                        Cell cell3 = row.createCell((short) 3);
                                        cell3.setCellValue(sCellActivation);
                                        cell3.setCellStyle(my_style);

                                        Cell cell4 = row.createCell((short) 4);
                                        cell4.setCellValue(sCellWorkerRevoke);
                                        cell4.setCellStyle(my_style);

                                        Cell cell5 = row.createCell((short) 5);
                                        cell5.setCellValue(sCellSum);
                                        cell5.setCellStyle(my_style);
                                        int i = 1;
                                        int k = 0;
                                        for (REPORT_QUICK_BRANCH rsReportBranch1 : rsReportBranch[0]) {
                                            if (k == 0) {
                                                k = 1;
                                            } else {
                                                k++;
                                            }
                                            String strAgency = EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_DESC) + " (" + EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_NAME) + ")";
                                            int strInit = rsReportBranch1.TOTAL_INITIALIZED;
                                            int strOperation = rsReportBranch1.TOTAL_OPERATED;
                                            int strRevoke = rsReportBranch1.TOTAL_REVOKED;
                                            int sSUMBranch = strInit + strOperation + strRevoke;
                                            Row row1 = sheet.createRow(Integer.valueOf(i).intValue());
                                            CellStyle my_styleBranch = wb.createCellStyle();
                                            Font fontBranch = wb.createFont();

                                            fontBranch.setFontName("Arial");
                                            fontBranch.setColor((short) 12);
                                            my_styleBranch.setFont(fontBranch);
                                            my_styleBranch.setFillBackgroundColor((short) 9);

                                            Cell cellBranch = row1.createCell((short) 0);
                                            cellBranch.setCellValue(k);
                                            cellBranch.setCellStyle(my_styleBranch);

                                            Cell cellBranch1 = row1.createCell((short) 1);
                                            cellBranch1.setCellValue(strAgency);
                                            cellBranch1.setCellStyle(my_styleBranch);

                                            Cell cellBranch2 = row1.createCell((short) 2);
                                            cellBranch2.setCellValue(strInit);
                                            cellBranch2.setCellStyle(my_styleBranch);

                                            Cell cellBranch3 = row1.createCell((short) 3);
                                            cellBranch3.setCellValue(strOperation);
                                            cellBranch3.setCellStyle(my_styleBranch);

                                            Cell cellBranch4 = row1.createCell((short) 4);
                                            cellBranch4.setCellValue(strRevoke);
                                            cellBranch4.setCellStyle(my_styleBranch);

                                            Cell cellBranch5 = row1.createCell((short) 5);
                                            cellBranch5.setCellValue(sSUMBranch);
                                            cellBranch5.setCellStyle(my_styleBranch);

                                            i++;
                                            REPORT_QUICK_BRANCH[][] rsReportUser = new REPORT_QUICK_BRANCH[1][];
                                            com.S_BO_REPORT_TOTAL_BRANCH_USER(FromCreateDate, ToCreateDate,
                                                    String.valueOf(rsReportBranch1.BRANCH_ID), rsReportUser, SessUserAgentID);
                                            if (rsReportUser[0].length > 0) {
                                                for (REPORT_QUICK_BRANCH rsReportUser1 : rsReportUser[0]) {
                                                    int sSUMUser = rsReportUser1.TOTAL_INITIALIZED + rsReportUser1.TOTAL_OPERATED + rsReportUser1.TOTAL_REVOKED;
                                                    if (sSUMUser == 0 && rsReportUser1.USER_STATE_ID == Definitions.CONFIG_USER_STATE_CANCEL_ID) {
                                                    } else {
                                                        Row rowUser = sheet.createRow(Integer.valueOf(i).intValue());
                                                        rowUser.createCell((short) 1).setCellValue(EscapeUtils.CheckTextNull(rsReportUser1.USERNAME));
                                                        rowUser.createCell((short) 2).setCellValue(rsReportUser1.TOTAL_INITIALIZED);
                                                        rowUser.createCell((short) 3).setCellValue(rsReportUser1.TOTAL_OPERATED);
                                                        rowUser.createCell((short) 4).setCellValue(rsReportUser1.TOTAL_REVOKED);
                                                        rowUser.createCell((short) 5).setCellValue(sSUMUser);
                                                        i++;
                                                    }
                                                }
                                            }
                                        }
                                        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                        wb.write(outByteStream);
                                        byte[] outArray = outByteStream.toByteArray();
                                        File someFile = new File(strURLPath);
                                        FileOutputStream fos = new FileOutputStream(someFile);
                                        fos.write(outArray);
                                        fos.flush();
                                        strView = "0#" + strURLPath + "#" + sFileName;
                                    } else {
                                        strView = "2#1";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "exportreportcertlist": {
                                //<editor-fold defaultstate="collapsed" desc="exportreportcertlist">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                    Config conf = new Config();
                                    ConfigLanguage confLanguage = new ConfigLanguage();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_CERTIFICATE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    REPORT_PER_MONTH[][] rsReportBranch = new REPORT_PER_MONTH[1][];
                                    String FromCreateDate = request.getSession(false).getAttribute("sessYearReportCertList").toString().trim();
                                    String ToCreateDate = request.getSession(false).getAttribute("sessMonthReportCertList").toString().trim();
                                    String PKI_FORMFACTOR = request.getSession(false).getAttribute("sessFormFactorReportCertList").toString().trim();
                                    String idBranchOffice = request.getSession(false).getAttribute("sessBranchOfficeReportCertList").toString().trim();
                                    String sUSER = request.getSession(false).getAttribute("sessUserReportCertList").toString().trim();
                                    String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                    String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                    String pBRANCH_BENEFICIARY_ID = SessUserAgentID;
                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        pBRANCH_BENEFICIARY_ID=EscapeUtils.escapeHtmlSearch(idBranchOffice);
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(sUSER)) {
                                        sUSER = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(PKI_FORMFACTOR)) {
                                        PKI_FORMFACTOR = "";
                                    }
                                    CommonFunction.LogDebugString(log, "ExportReportCertList", "Year: " + FromCreateDate
                                        + "; Month: " + ToCreateDate + "; PKI_FORMFACTOR: " + PKI_FORMFACTOR + "; AGENCY: " + idBranchOffice);
                                    int ss = com.S_BO_REPORT_PER_MONTH_TOTAL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                        EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice),
                                        sUSER, PKI_FORMFACTOR, pBRANCH_BENEFICIARY_ID);
                                    if (ss > 0) {
                                        com.S_BO_REPORT_PER_MONTH_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(idBranchOffice), sUSER, PKI_FORMFACTOR,
                                            loginLanguage, rsReportBranch, Definitions.CONFIG_PAGE_SIZE_IPAGNO, ss, pBRANCH_BENEFICIARY_ID);
                                    }
                                    if (ss > 0) {
                                        String queryString = getServletContext().getRealPath("/");
                                        String outputDirectory = queryString;
                                        String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                        FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                        inputStream.close();
                                        String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellUserCreate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_USER_CREATE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellMST = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_ENTERPRISE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellSTATE = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_STATUS, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellCompany = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COMPANY_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sDeviceUUID = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_DEVICE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellPesonal = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PERSONAL_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellProvince = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROVINCE_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICEPACK_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellRequestType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REQUEST_TYPE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellFormFactor = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_METHOD_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDateCreate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CREATE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDateCancel = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CANCEL, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellNUM_DATE_CANCEL = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CANCEL_NUMBER, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDATE_GEN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_GEN, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDATE_EXPIRE = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EXPIRATION, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_SN, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellCertSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDateEffective = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EFFECTIVE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                        wb.setCompressTempFiles(true);
                                        SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
                                        CreationHelper createHelper = wb.getCreationHelper();
                                        sheet.setRandomAccessWindowSize(100);
                                        sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                        sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                        sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                        sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                        sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STATE);
                                        sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                        sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                        sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                        sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                        sheet.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROVINCE);
                                        sheet.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                        sheet.setColumnWidth(11, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                        sheet.setColumnWidth(12, Definitions.CONFIG_EXPORT_QUICK_WIDTH_FORMFACTOR);
                                        sheet.setColumnWidth(13, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                        sheet.setColumnWidth(14, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CANCEL);
                                        sheet.setColumnWidth(15, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_GEN);
                                        sheet.setColumnWidth(16, Definitions.CONFIG_EXPORT_QUICK_WIDTH_NUM_DATE_CANCEL);
                                        sheet.setColumnWidth(17, Definitions.CONFIG_EXPORT_QUICK_WIDTH_NUM_DATE_CANCEL);
                                        sheet.setColumnWidth(18, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_GEN);
                                        sheet.setColumnWidth(19, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_GEN);
                                        sheet.setColumnWidth(20, 18*255);
                                        Row row = sheet.createRow(0);

                                        CellStyle my_style = wb.createCellStyle();
                                        Font font = wb.createFont();
                                        font.setBoldweight((short) 700);
                                        font.setFontName("Arial");
                                        my_style.setFont(font);
                                        my_style.setFillBackgroundColor((short) 9);
                                        my_style.setAlignment((short) 2);
                                        Cell cell = row.createCell((short) 0);
                                        cell.setCellValue(sCellSTT);
                                        cell.setCellStyle(my_style);

                                        Cell cell1 = row.createCell((short) 1);
                                        cell1.setCellValue(sCellAgency);
                                        cell1.setCellStyle(my_style);
                                        Cell cell2 = row.createCell((short) 2);
                                        cell2.setCellValue(sCellUserCreate);
                                        cell2.setCellStyle(my_style);

                                        Cell cell3 = row.createCell((short) 3);
                                        cell3.setCellValue(sCellMST);
                                        cell3.setCellStyle(my_style);

                                        Cell cell4 = row.createCell((short) 4);
                                        cell4.setCellValue(sCellSTATE);
                                        cell4.setCellStyle(my_style);

                                        Cell cell5 = row.createCell((short) 5);
                                        cell5.setCellValue(sCellCompany);
                                        cell5.setCellStyle(my_style);

                                        Cell cell6 = row.createCell((short) 6);
                                        cell6.setCellValue(sCellCMND);
                                        cell6.setCellStyle(my_style);

                                        Cell cell7 = row.createCell((short) 7);
                                        cell7.setCellValue(sCellPesonal);
                                        cell7.setCellStyle(my_style);

                                        Cell cell8 = row.createCell((short) 8);
                                        cell8.setCellValue(sDeviceUUID);
                                        cell8.setCellStyle(my_style);
                                        
                                        Cell cell81 = row.createCell((short) 9);
                                        cell81.setCellValue(sCellProvince);
                                        cell81.setCellStyle(my_style);

                                        Cell cell9 = row.createCell((short) 10);
                                        cell9.setCellValue(sCellProfile);
                                        cell9.setCellStyle(my_style);
                                        
                                        Cell cell10 = row.createCell((short) 11);
                                        cell10.setCellValue(sCellRequestType);
                                        cell10.setCellStyle(my_style);
                                        
                                        Cell cell11 = row.createCell((short) 12);
                                        cell11.setCellValue(sCellFormFactor);
                                        cell11.setCellStyle(my_style);
                                        
                                        if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_CMC)){
                                            Cell cell1Effective = row.createCell((short) 13);
                                            cell1Effective.setCellValue(sCellDateEffective);
                                            cell1Effective.setCellStyle(my_style);

                                            Cell cell15 = row.createCell((short) 14);
                                            cell15.setCellValue(sCellDATE_GEN);
                                            cell15.setCellStyle(my_style);

                                            Cell cell13 = row.createCell((short) 15);
                                            cell13.setCellValue(sCellDateCancel);
                                            cell13.setCellStyle(my_style);

                                            Cell cell14 = row.createCell((short) 16);
                                            cell14.setCellValue(sCellNUM_DATE_CANCEL);
                                            cell14.setCellStyle(my_style);
                                            
                                            Cell cell12 = row.createCell((short) 17);
                                            cell12.setCellValue(sCellDateCreate);
                                            cell12.setCellStyle(my_style);

                                            Cell cell16 = row.createCell((short) 18);
                                            cell16.setCellValue(sTokenSN);
                                            cell16.setCellStyle(my_style);

                                            Cell cell112 = row.createCell((short) 19);
                                            cell112.setCellValue(sCellCertSN);
                                            cell112.setCellStyle(my_style);

                                            Cell cell1Expire = row.createCell((short) 20);
                                            cell1Expire.setCellValue(sCellDATE_EXPIRE);
                                            cell1Expire.setCellStyle(my_style);
                                        } else {
                                            Cell cell12 = row.createCell((short) 13);
                                            cell12.setCellValue(sCellDateCreate);
                                            cell12.setCellStyle(my_style);

                                            Cell cell1Effective = row.createCell((short) 14);
                                            cell1Effective.setCellValue(sCellDateEffective);
                                            cell1Effective.setCellStyle(my_style);

                                            Cell cell13 = row.createCell((short) 15);
                                            cell13.setCellValue(sCellDateCancel);
                                            cell13.setCellStyle(my_style);

                                            Cell cell14 = row.createCell((short) 16);
                                            cell14.setCellValue(sCellNUM_DATE_CANCEL);
                                            cell14.setCellStyle(my_style);

                                            Cell cell15 = row.createCell((short) 17);
                                            cell15.setCellValue(sCellDATE_GEN);
                                            cell15.setCellStyle(my_style);

                                            Cell cell1Expire = row.createCell((short) 18);
                                            cell1Expire.setCellValue(sCellDATE_EXPIRE);
                                            cell1Expire.setCellStyle(my_style);

                                            Cell cell16 = row.createCell((short) 19);
                                            cell16.setCellValue(sTokenSN);
                                            cell16.setCellStyle(my_style);

                                            Cell cell112 = row.createCell((short) 20);
                                            cell112.setCellValue(sCellCertSN);
                                            cell112.setCellStyle(my_style);
                                        }

                                        int i = 1;
                                        int k = 0;
                                        for (REPORT_PER_MONTH rsReportBranch1 : rsReportBranch[0]) {
                                            if (k == 0) {
                                                k = 1;
                                            } else {
                                                k++;
                                            }
                                            Row row1 = sheet.createRow(Integer.valueOf(i).intValue());
                                            CellStyle my_styleBranch = wb.createCellStyle();
                                            Font fontBranch = wb.createFont();

                                            fontBranch.setFontName("Arial");
//                                            fontBranch.setColor((short) 12);
                                            my_styleBranch.setFont(fontBranch);
//                                            my_styleBranch.setFillBackgroundColor((short) 9);

                                            CellStyle my_styleBranchDate = wb.createCellStyle();
                                            Font fontBranchDate = wb.createFont();
                                            fontBranchDate.setFontName("Arial");
                                            my_styleBranchDate.setFont(fontBranchDate);
                                            my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS));
                                            
                                            CellStyle styleText = wb.createCellStyle();
                                            styleText.setFont(fontBranch);
                                            styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));

                                            Cell cellBranch = row1.createCell((short) 0);
                                            cellBranch.setCellValue(k);
                                            cellBranch.setCellStyle(my_styleBranch);

                                            Cell cellBranch1 = row1.createCell((short) 1);
                                            cellBranch1.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_NAME));
                                            cellBranch1.setCellStyle(my_styleBranch);

                                            Cell cellBranch2 = row1.createCell((short) 2);
                                            cellBranch2.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.USERNAME_CREATED));
                                            cellBranch2.setCellStyle(my_styleBranch);

                                            String sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.ENTERPRISE_ID);
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                sMSTAndBUDGET_CODE = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.ENTERPRISE_ID, loginLanguage);
                                            }
                                            Cell cellBranch3 = row1.createCell((short) 3);
                                            cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                            cellBranch3.setCellStyle(styleText);
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(rsReportBranch1.CERTIFICATION_STATE_DESC);
                                                cellBranch4.setCellStyle(my_styleBranch);
                                            } else {
                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_STATE_NAME));
                                                cellBranch4.setCellStyle(my_styleBranch);
                                            }

                                            Cell cellBranch5 = row1.createCell((short) 5);
                                            cellBranch5.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.COMPANY_NAME));
                                            cellBranch5.setCellStyle(my_styleBranch);

                                            String sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.PERSONAL_ID);
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                sP_IDAndPASSPORT = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.PERSONAL_ID, loginLanguage);
                                            }
                                            Cell cellBranch6 = row1.createCell((short) 6);
                                            cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                            cellBranch6.setCellStyle(styleText);

                                            Cell cellBranch7 = row1.createCell((short) 7);
                                            cellBranch7.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PERSONAL_NAME));
                                            cellBranch7.setCellStyle(my_styleBranch);
                                            
                                            String strDeviceUUID = "";
                                            if(rsReportBranch1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                strDeviceUUID = EscapeUtils.CheckTextNull(rsReportBranch1.SERVICE_UUID);
                                            }
                                            Cell cellBranch71 = row1.createCell((short) 8);
                                            cellBranch71.setCellValue(strDeviceUUID);
                                            cellBranch71.setCellStyle(my_styleBranch);

                                            Cell cellBranch8 = row1.createCell((short) 9);
                                            cellBranch8.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PROVINCE_NAME));
                                            cellBranch8.setCellStyle(my_styleBranch);

                                            Cell cellBranch9 = row1.createCell((short) 10);
                                            cellBranch9.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_PROFILE_NAME));
                                            cellBranch9.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch10 = row1.createCell((short) 11);
                                            cellBranch10.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_ATTR_TYPE_DESC));
                                            cellBranch10.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch11 = row1.createCell((short) 12);
                                            cellBranch11.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.FORM_FACTOR_DESC));
                                            cellBranch11.setCellStyle(my_styleBranch);
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_CMC)){
                                                Cell cellValueEffective = row1.createCell((short) 13);
                                                Date dateEffective = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.EFFECTIVE_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateEffective != null) {
                                                    cellValueEffective.setCellValue(dateEffective);
                                                } else {
                                                    cellValueEffective.setCellValue("");
                                                }
                                                cellValueEffective.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch15 = row1.createCell((short) 14);
                                                Date dateGen = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.GENERATED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateGen != null) {
                                                    cellBranch15.setCellValue(dateGen);
                                                } else {
                                                    cellBranch15.setCellValue("");
                                                }
                                                cellBranch15.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch13 = row1.createCell((short) 15);
                                                Date dateRevoke = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.REVOKED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateRevoke != null) {
                                                    cellBranch13.setCellValue(dateRevoke);
                                                } else {
                                                    cellBranch13.setCellValue("");
                                                }
                                                cellBranch13.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch14 = row1.createCell((short) 16);
                                                cellBranch14.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.NUMBER_DELETED));
                                                cellBranch14.setCellStyle(my_styleBranch);
                                                
                                                Date date = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.CREATED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                Cell cellBranch12 = row1.createCell((short) 17);
                                                if(date != null) {
                                                    cellBranch12.setCellValue(date);
                                                } else {
                                                    cellBranch12.setCellValue("");
                                                }
                                                cellBranch12.setCellStyle(my_styleBranchDate);

                                                String valueTokenSN = EscapeUtils.CheckTextNull(rsReportBranch1.TOKEN_SN);
                                                if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                Cell cellBranch16 = row1.createCell((short) 18);
                                                cellBranch16.setCellValue(valueTokenSN);
                                                cellBranch16.setCellStyle(styleText);

                                                Cell cellBranch122 = row1.createCell((short) 19);
                                                cellBranch122.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_SN));
                                                cellBranch122.setCellStyle(styleText);

                                                Cell cellBranchExpire = row1.createCell((short) 20);
                                                Date dateExpire = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.EXPIRATION_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateExpire != null) {
                                                    cellBranchExpire.setCellValue(dateExpire);
                                                } else {
                                                    cellBranchExpire.setCellValue("");
                                                }
                                                cellBranchExpire.setCellStyle(my_styleBranchDate);
                                            } else {
                                                Date date = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.CREATED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                Cell cellBranch12 = row1.createCell((short) 13);
                                                if(date != null) {
                                                    cellBranch12.setCellValue(date);
                                                } else {
                                                    cellBranch12.setCellValue("");
                                                }
                                                cellBranch12.setCellStyle(my_styleBranchDate);

                                                Cell cellValueEffective = row1.createCell((short) 14);
                                                Date dateEffective = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.EFFECTIVE_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateEffective != null) {
                                                    cellValueEffective.setCellValue(dateEffective);
                                                } else {
                                                    cellValueEffective.setCellValue("");
                                                }
                                                cellValueEffective.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch13 = row1.createCell((short) 15);
                                                Date dateRevoke = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.REVOKED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateRevoke != null) {
                                                    cellBranch13.setCellValue(dateRevoke);
                                                } else {
                                                    cellBranch13.setCellValue("");
                                                }
                                                cellBranch13.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch14 = row1.createCell((short) 16);
                                                cellBranch14.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.NUMBER_DELETED));
                                                cellBranch14.setCellStyle(my_styleBranch);

                                                Cell cellBranch15 = row1.createCell((short) 17);
                                                Date dateGen = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.GENERATED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateGen != null) {
                                                    cellBranch15.setCellValue(dateGen);
                                                } else {
                                                    cellBranch15.setCellValue("");
                                                }
                                                cellBranch15.setCellStyle(my_styleBranchDate);

                                                Cell cellBranchExpire = row1.createCell((short) 18);
                                                Date dateExpire = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.EXPIRATION_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateExpire != null) {
                                                    cellBranchExpire.setCellValue(dateExpire);
                                                } else {
                                                    cellBranchExpire.setCellValue("");
                                                }
                                                cellBranchExpire.setCellStyle(my_styleBranchDate);

                                                String valueTokenSN = EscapeUtils.CheckTextNull(rsReportBranch1.TOKEN_SN);
                                                if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                Cell cellBranch16 = row1.createCell((short) 19);
                                                cellBranch16.setCellValue(valueTokenSN);
                                                cellBranch16.setCellStyle(styleText);

                                                Cell cellBranch122 = row1.createCell((short) 20);
                                                cellBranch122.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_SN));
                                                cellBranch122.setCellStyle(styleText);
                                            }
                                            i++;
                                        }
                                        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                        wb.write(outByteStream);
                                        byte[] outArray = outByteStream.toByteArray();
                                        File someFile = new File(strURLPath);
                                        FileOutputStream fos = new FileOutputStream(someFile);
                                        fos.write(outArray);
                                        fos.flush();
                                        strView = "0#" + strURLPath + "#" + sFileName;
                                    } else {
                                        strView = "2#1";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "exportreportcertcontrol": {
                                //<editor-fold defaultstate="collapsed" desc="exportreportcertcontrol">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    CommonFunction comP = new CommonFunction();
                                    Config conf = new Config();
                                    ConfigLanguage confLanguage = new ConfigLanguage();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();

                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_DEBT_CONTROL + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    CommonFunction.LogDebugString(log, "ExportReportCertList", "PathURL: " + strURLPath);

                                    PAYMENT[][] rsReportBranch = new PAYMENT[1][];
                                    String FromCreateDate = request.getSession(false).getAttribute("sessYearDateReportCertControl").toString().trim();
                                    String ToCreateDate = request.getSession(false).getAttribute("sessMountDateReportCertControl").toString().trim();
                                    String idBranchOffice = request.getSession(false).getAttribute("sessBranchOfficeReportCertControl").toString().trim();
                                    CommonFunction.LogDebugString(log, "ExportReportCertList", "FromDate: " + FromCreateDate + "; ToDate: " + ToCreateDate + "; AGENCY: " + idBranchOffice);
                                    com.S_BO_PAYMENT_REPORT_PER_MONTH(ToCreateDate, FromCreateDate,
                                            EscapeUtils.escapeHtmlSearch(idBranchOffice), loginLanguage, rsReportBranch);
                                    if (rsReportBranch[0].length > 0) {
                                        String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellContent = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_CONTENT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellAmount = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_AMOUNT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellNote = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_NOTE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        HSSFWorkbook wb = new HSSFWorkbook();
                                        HSSFSheet sheet = wb.createSheet();
                                        sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                        sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CONTENT);
                                        sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AMOUNT);
                                        sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_NOTE);
                                        HSSFRow row = sheet.createRow(0);

                                        HSSFCellStyle my_style = wb.createCellStyle();
                                        HSSFFont font = wb.createFont();
                                        font.setBoldweight((short) 700);
                                        font.setFontName("Arial");
                                        my_style.setFont(font);
                                        my_style.setFillBackgroundColor((short) 9);
                                        my_style.setAlignment((short) 2);
                                        Cell cell = row.createCell((short) 0);
                                        cell.setCellValue(sCellSTT);
                                        cell.setCellStyle(my_style);

                                        Cell cell1 = row.createCell((short) 1);
                                        cell1.setCellValue(sCellContent);
                                        cell1.setCellStyle(my_style);

                                        Cell cell2 = row.createCell((short) 2);
                                        cell2.setCellValue(sCellAmount);
                                        cell2.setCellStyle(my_style);

                                        Cell cell3 = row.createCell((short) 3);
                                        cell3.setCellValue(sCellNote);
                                        cell3.setCellStyle(my_style);

                                        int i = 1;
                                        int k = 0;
                                        double sInt_AMOUNT1 = 0;
                                        double sInt_AMOUNT2 = 0;
                                        double sInt_AMOUNT3 = 0;
                                        double sInt_AMOUNT4 = 0;
                                        double sInt_AMOUNT5 = 0;
                                        double sInt_AMOUNT6 = 0;
                                        double sInt_AMOUNT7 = 0;
                                        double sInt_AMOUNT8 = 0;
                                        for (PAYMENT rsReportBranchAmount : rsReportBranch[0]) {
                                            if (rsReportBranchAmount.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEBIT_AMOUNT) {
                                                sInt_AMOUNT1 = rsReportBranchAmount.AMOUNT;
                                            }
                                            if (rsReportBranchAmount.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_TOTAL_AMOUNT) {
                                                sInt_AMOUNT2 = rsReportBranchAmount.AMOUNT;
                                            }
                                            if (rsReportBranchAmount.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEPOSIT_DEVICE) {
                                                sInt_AMOUNT3 = rsReportBranchAmount.AMOUNT;
                                            }
                                            if (rsReportBranchAmount.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_DEPOSIT_DEVICE_MINUS) {
                                                sInt_AMOUNT4 = rsReportBranchAmount.AMOUNT;
                                            }
                                            if (rsReportBranchAmount.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_FILE_AMOUNT) {
                                                sInt_AMOUNT5 = rsReportBranchAmount.AMOUNT;
                                            }
                                            if (rsReportBranchAmount.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_RETURN_FILE_AMOUNT) {
                                                sInt_AMOUNT6 = rsReportBranchAmount.AMOUNT;
                                            }
                                            if (rsReportBranchAmount.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_PAID) {
                                                sInt_AMOUNT7 = rsReportBranchAmount.AMOUNT;
                                            }
                                        }
                                        sInt_AMOUNT8 = sInt_AMOUNT1 + sInt_AMOUNT2 - sInt_AMOUNT4
                                                + sInt_AMOUNT5 + sInt_AMOUNT5 - sInt_AMOUNT6 - sInt_AMOUNT7;
                                        for (PAYMENT rsReportBranch1 : rsReportBranch[0]) {
                                            if (k == 0) {
                                                k = 1;
                                            } else {
                                                k++;
                                            }
                                            HSSFRow row1 = sheet.createRow(Integer.valueOf(i).intValue());
                                            HSSFCellStyle my_styleBranch = wb.createCellStyle();
                                            HSSFFont fontBranch = wb.createFont();

                                            fontBranch.setFontName("Arial");
                                            my_styleBranch.setFont(fontBranch);

                                            Cell cellBranch = row1.createCell((short) 0);
                                            cellBranch.setCellValue(k);
                                            cellBranch.setCellStyle(my_styleBranch);

                                            Cell cellBranch1 = row1.createCell((short) 1);
                                            cellBranch1.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PAYMENT_TYPE_REMARK));
                                            cellBranch1.setCellStyle(my_styleBranch);

                                            String sAmountTotal = comP.convertMoneyDoubleZero(rsReportBranch1.AMOUNT);
                                            if (rsReportBranch1.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_TOTAL) {
                                                sAmountTotal = comP.convertMoneyDoubleZero(sInt_AMOUNT8);
                                            }
                                            Cell cellBranch2 = row1.createCell((short) 2);
                                            cellBranch2.setCellValue(EscapeUtils.CheckTextNull(sAmountTotal));
                                            cellBranch2.setCellStyle(my_styleBranch);

                                            Cell cellBranch3 = row1.createCell((short) 3);
                                            cellBranch3.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.NOTE));
                                            cellBranch3.setCellStyle(my_styleBranch);
                                            i++;
                                        }
                                        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                        wb.write(outByteStream);
                                        byte[] outArray = outByteStream.toByteArray();
                                        File someFile = new File(strURLPath);
                                        FileOutputStream fos = new FileOutputStream(someFile);
                                        fos.write(outArray);
                                        fos.flush();
                                        strView = "0#" + strURLPath + "#" + sFileName;
                                    } else {
                                        strView = "2#1";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "exportcertexpire": {
                                //<editor-fold defaultstate="collapsed" desc="exportcertexpire">
//                                String anticsrf = request.getParameter("CsrfToken");
                                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                String sessTreeArrayBranchID = request.getSession(false).getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
//                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    Config conf = new Config();
                                    ConfigLanguage confLanguage = new ConfigLanguage();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();

                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_EXPIRE + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;

                                    CERTIFICATION[][] rsReportBranch = new CERTIFICATION[1][];
                                    String strAlertAllTimes = (String) request.getSession(false).getAttribute("AlertAllTimeCertExpireList");
//                                    String DATE_EXPIRE = request.getSession(false).getAttribute("sessDATE_EXPIRECertExpireList").toString().trim();
                                    String FromCreateDate = request.getSession(false).getAttribute("sessFromCreateDateCertExpireList").toString().trim();
                                    String ToCreateDate = request.getSession(false).getAttribute("sessToCreateDateCertExpireList").toString().trim();
                                    String BranchOffice = request.getSession(false).getAttribute("sessBranchOfficeCertExpireList").toString().trim();
                                    String UserCert = request.getSession(false).getAttribute("sessUserCertExpireList").toString().trim();
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                        BranchOffice = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(UserCert)) {
                                        UserCert = "";
                                    }
                                    if("1".equals(strAlertAllTimes)) {
                                        FromCreateDate = "";
                                        ToCreateDate = "";
                                    }
                                    int iCount = com.S_BO_CERTIFICATION_WARNING_EXPIRED_TOTAL(EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), BranchOffice,
                                            EscapeUtils.escapeHtmlSearch(UserCert), sessTreeArrayBranchID);
                                    if (iCount > 0) {
                                        com.S_BO_CERTIFICATION_WARNING_EXPIRED_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(BranchOffice), UserCert, loginLanguage, rsReportBranch,
                                            Definitions.CONFIG_PAGE_SIZE_IPAGNO, iCount, sessTreeArrayBranchID);
                                        if (rsReportBranch[0].length > 0) {
                                            String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, loginLanguage).trim();
                                            String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, loginLanguage).trim();
                                            String sCellUser = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_USER_CREATE, loginLanguage).trim();
                                            String sCellCompany = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COMPANY_NAME, loginLanguage).trim();
                                            String sCellMST = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_ENTERPRISE, loginLanguage).trim();
                                            String sCellPersonal = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PERSONAL_NAME, loginLanguage).trim();
                                            String sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL, loginLanguage).trim();
                                            String sCellPhoneContact = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PHONE_CONTACT, loginLanguage).trim();
                                            String sCellEmailContact = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_EMAIL_CONTACT, loginLanguage).trim();
                                            String sCellDateEffective = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EFFECTIVE, loginLanguage).trim();
                                            String sCellDateExpiration = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EXPIRATION, loginLanguage).trim();
                                            
                                            String sCellStatus = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_STATUS, loginLanguage).trim();
                                            String sCellSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage).trim();
                                            String sCellAddress = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_ADDRESS, loginLanguage).trim();
                                            String sCellCertType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REQUEST_TYPE, loginLanguage).trim();
                                            String sCellCertProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICEPACK_NAME, loginLanguage).trim();
                                            String sCellPhoneResp = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_PHONE, loginLanguage).trim();
                                            String sCellEmailResp = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_EMAIL, loginLanguage).trim();
                                            String sCelNameRepresent = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_NAME, loginLanguage);
                                            String sCelNameContract = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CONTACT_NAME, loginLanguage);
                                            String sCelCheckRegister = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REGISTER_USE, loginLanguage);
                                            String sCelCheckConfirm = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERTIFICATION, loginLanguage);
                                            String sCelCheckGPKD = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REGISTER_BUSSINESS, loginLanguage);
                                            String sCelCheckCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_IDENTITY_CARD, loginLanguage);
                                            String sCellStateProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_STATUS, loginLanguage);
                                            
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                            FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                            XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                            inputStream.close();
                                            
                                            SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                            wb.setCompressTempFiles(true);
                                            CreationHelper createHelper = wb.getCreationHelper();
                                            SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
                                            sheet.setRandomAccessWindowSize(100);
                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                            sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
                                            sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                            sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                            sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                            sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                            sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PHONE_CONTACT);
                                            sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_EMAIL_CONTACT);
                                            sheet.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EFFECTIVE);
                                            sheet.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(11, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(12, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(13, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(14, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(15, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(16, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(17, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(18, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(19, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(20, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(21, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(22, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            sheet.setColumnWidth(23, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
                                            Row row = sheet.createRow(0);

                                            CellStyle my_style = wb.createCellStyle();
                                            Font font = wb.createFont();
                                            font.setBoldweight((short) 700);
                                            font.setFontName("Arial");
                                            my_style.setFont(font);
                                            my_style.setFillBackgroundColor((short) 9);
                                            my_style.setAlignment((short) 2);
                                            Cell cell = row.createCell((short) 0);
                                            cell.setCellValue(sCellSTT);
                                            cell.setCellStyle(my_style);

                                            Cell cell1 = row.createCell((short) 1);
                                            cell1.setCellValue(sCellAgency);
                                            cell1.setCellStyle(my_style);

                                            row.createCell((short) 2).setCellValue(sCellUser);
                                            Cell cell2 = row.createCell((short) 2);
                                            cell2.setCellValue(sCellUser);
                                            cell2.setCellStyle(my_style);

                                            Cell cell4 = row.createCell((short) 3);
                                            cell4.setCellValue(sCellMST);
                                            cell4.setCellStyle(my_style);

                                            Cell cell3 = row.createCell((short) 4);
                                            cell3.setCellValue(sCellCompany);
                                            cell3.setCellStyle(my_style);
                                            
                                            Cell cell6 = row.createCell((short) 5);
                                            cell6.setCellValue(sCellCMND);
                                            cell6.setCellStyle(my_style);

                                            Cell cell5 = row.createCell((short) 6);
                                            cell5.setCellValue(sCellPersonal);
                                            cell5.setCellStyle(my_style);
                                            
                                            Cell cell12 = row.createCell((short) 7);
                                            cell12.setCellValue(sCellCertProfile);
                                            cell12.setCellStyle(my_style);
                                            
                                            Cell cell13 = row.createCell((short) 8);
                                            cell13.setCellValue(sCellCertType);
                                            cell13.setCellStyle(my_style);
                                            
                                            Cell cellStatus = row.createCell((short) 9);
                                            cellStatus.setCellValue(sCellStatus);
                                            cellStatus.setCellStyle(my_style);
                                            
                                            Cell cell9 = row.createCell((short) 10);
                                            cell9.setCellValue(sCellDateEffective);
                                            cell9.setCellStyle(my_style);
                                            
                                            Cell cell10 = row.createCell((short) 11);
                                            cell10.setCellValue(sCellDateExpiration);
                                            cell10.setCellStyle(my_style);
                                            
                                            Cell cell11 = row.createCell((short) 12);
                                            cell11.setCellValue(sCellSN);
                                            cell11.setCellStyle(my_style);
                                            
                                            Cell cell14 = row.createCell((short) 13);
                                            cell14.setCellValue(sCelNameRepresent);
                                            cell14.setCellStyle(my_style);
                                            
                                            Cell cellPhoneResp = row.createCell((short) 14);
                                            cellPhoneResp.setCellValue(sCellPhoneResp);
                                            cellPhoneResp.setCellStyle(my_style);
                                            
                                            Cell cell15= row.createCell((short) 15);
                                            cell15.setCellValue(sCellEmailResp);
                                            cell15.setCellStyle(my_style);
                                            
                                            Cell cellNameContract= row.createCell((short) 16);
                                            cellNameContract.setCellValue(sCelNameContract);
                                            cellNameContract.setCellStyle(my_style);
                                            
                                            Cell cell16= row.createCell((short) 17);
                                            cell16.setCellValue(sCellAddress);
                                            cell16.setCellStyle(my_style);
                                            
                                            Cell cellStateProfile= row.createCell((short) 18);
                                            cellStateProfile.setCellValue(sCellStateProfile);
                                            cellStateProfile.setCellStyle(my_style);
                                            
                                            Cell cellCheckRegister= row.createCell((short) 19);
                                            cellCheckRegister.setCellValue(sCelCheckRegister);
                                            cellCheckRegister.setCellStyle(my_style);
                                            
                                            Cell cellCheckConfirm= row.createCell((short) 20);
                                            cellCheckConfirm.setCellValue(sCelCheckConfirm);
                                            cellCheckConfirm.setCellStyle(my_style);
                                            
                                            Cell cellCheckGPKD= row.createCell((short) 21);
                                            cellCheckGPKD.setCellValue(sCelCheckGPKD);
                                            cellCheckGPKD.setCellStyle(my_style);
                                            
                                            Cell cellCheckCMND= row.createCell((short) 22);
                                            cellCheckCMND.setCellValue(sCelCheckCMND);
                                            cellCheckCMND.setCellStyle(my_style);
                                            
                                            Cell cell7 = row.createCell((short) 23);
                                            cell7.setCellValue(sCellPhoneContact);
                                            cell7.setCellStyle(my_style);
                                            
                                            Cell cell8 = row.createCell((short) 24);
                                            cell8.setCellValue(sCellEmailContact);
                                            cell8.setCellStyle(my_style);
                                            int i = 1;
                                            int k = 0;
                                            
                                            for (CERTIFICATION rsReportBranch1 : rsReportBranch[0]) {
                                                if (k == 0) {
                                                    k = 1;
                                                } else {
                                                    k++;
                                                }
                                                String strAgency = EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_DESC);// + " (" + EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_NAME) + ")";
                                                Row row1 = sheet.createRow(i);
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                Font fontBranch = wb.createFont();

                                                fontBranch.setFontName("Arial");
                                                my_styleBranch.setFont(fontBranch);
                                                
                                                CellStyle my_styleBranchDate = wb.createCellStyle();
                                                my_styleBranchDate.setFont(fontBranch);
                                                my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));
                                                
                                                Cell cellBranch = row1.createCell((short) 0);
                                                cellBranch.setCellValue(k);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row1.createCell((short) 1);
                                                cellBranch1.setCellValue(strAgency);
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch2 = row1.createCell((short) 2);
                                                cellBranch2.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CREATED_BY));
                                                cellBranch2.setCellStyle(my_styleBranch);
                                                
                                                String sSMTOrMSN = rsReportBranch1.ENTERPRISE_ID;// EscapeUtils.CheckTextNull(rsReportBranch1.TAX_CODE);
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                    sSMTOrMSN = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.ENTERPRISE_ID, loginLanguage);
                                                }
//                                                if("".equals(sSMTOrMSN)) {
//                                                    sSMTOrMSN = EscapeUtils.CheckTextNull(rsReportBranch1.BUDGET_CODE);
//                                                }
//                                                if("".equals(sSMTOrMSN)) {
//                                                    sSMTOrMSN = EscapeUtils.CheckTextNull(rsReportBranch1.DECISION);
//                                                }
                                                Cell cellBranch4 = row1.createCell((short) 3);
                                                cellBranch4.setCellValue(sSMTOrMSN);
                                                cellBranch4.setCellStyle(my_styleBranch);

                                                Cell cellBranch3 = row1.createCell((short) 4);
                                                cellBranch3.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.COMPANY_NAME));
                                                cellBranch3.setCellStyle(my_styleBranch);
                                                
                                                String sCMNDOrHC = rsReportBranch1.PERSONAL_ID;// EscapeUtils.CheckTextNull(rsReportBranch1.P_ID);
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
                                                    sCMNDOrHC = CommonReferServlet.filterPrefixUIDLanguage(rsReportBranch1.PERSONAL_ID, loginLanguage);
                                                }
                                                Cell cellBranch6 = row1.createCell((short) 5);
                                                cellBranch6.setCellValue(EscapeUtils.CheckTextNull(sCMNDOrHC));
                                                cellBranch6.setCellStyle(my_styleBranch);

                                                Cell cellBranch5 = row1.createCell((short) 6);
                                                cellBranch5.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PERSONAL_NAME));
                                                cellBranch5.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch11 = row1.createCell((short) 7);
                                                cellBranch11.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_PROFILE_NAME));
                                                cellBranch11.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch12 = row1.createCell((short) 8);
                                                cellBranch12.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.SERVICE_TYPE_DESC));
                                                cellBranch12.setCellStyle(my_styleBranch);
                                                
                                                Cell valueStatus = row1.createCell((short) 9);
                                                valueStatus.setCellValue(rsReportBranch1.CERTIFICATION_STATE_DESC);
                                                valueStatus.setCellStyle(my_styleBranch);
                                                
                                                Cell cellValueEffective = row1.createCell((short) 10);
                                                Date dateEffective = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.EFFECTIVE_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateEffective != null) {
                                                    cellValueEffective.setCellValue(dateEffective);
                                                } else {
                                                    cellValueEffective.setCellValue("");
                                                }
                                                cellValueEffective.setCellStyle(my_styleBranchDate);
                                                
                                                Cell cellValueExpiration = row1.createCell((short) 11);
                                                Date dateExpiration = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.EXPIRATION_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateExpiration != null) {
                                                    cellValueExpiration.setCellValue(dateExpiration);
                                                } else {
                                                    cellValueExpiration.setCellValue("");
                                                }
                                                cellValueExpiration.setCellStyle(my_styleBranchDate);
                                                
                                                Cell cellCertSN = row1.createCell((short) 12);
                                                cellCertSN.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_SN));
                                                cellCertSN.setCellStyle(my_styleBranch);
                                                
                                                String sREPRESENTATIVE_EMAIL = "";
                                                String sREPRESENTATIVE_PHONE = "";
                                                String sREPRESENTATIVE_NAME = "";
                                                String sContactName = "";
                                                String sADDRESS = "";
                                                String sPrfileContact = EscapeUtils.CheckTextNull(rsReportBranch1.PROFILE_CONTACT_INFO);
                                                if(!"".equals(sPrfileContact)) {
                                                    ObjectMapper oMapperParse = new ObjectMapper();
                                                    ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                                    if(profileContact != null) {
                                                        sREPRESENTATIVE_EMAIL = profileContact.RepresentativeEmail;
                                                        sREPRESENTATIVE_PHONE = profileContact.RepresentativePhone;
                                                        sREPRESENTATIVE_NAME = profileContact.RepresentativeName;
                                                        sContactName = profileContact.ContactName;
                                                        sADDRESS = CommonFunction.replaceCharaterSpecialJson(profileContact.Address, false);
                                                    }
                                                }
                                                
                                                Cell valueRepName = row1.createCell((short) 13);
                                                valueRepName.setCellValue(sREPRESENTATIVE_NAME);
                                                valueRepName.setCellStyle(my_styleBranch);
                                                
                                                Cell valueRepPhone = row1.createCell((short) 14);
                                                valueRepPhone.setCellValue(sREPRESENTATIVE_PHONE);
                                                valueRepPhone.setCellStyle(my_styleBranch);
                                                
                                                Cell valueRepEmail = row1.createCell((short) 15);
                                                valueRepEmail.setCellValue(sREPRESENTATIVE_EMAIL);
                                                valueRepEmail.setCellStyle(my_styleBranch);
                                                
                                                Cell valueContactName = row1.createCell((short) 16);
                                                valueContactName.setCellValue(sContactName);
                                                valueContactName.setCellStyle(my_styleBranch);
                                                
                                                Cell valueADDRESS = row1.createCell((short) 17);
                                                valueADDRESS.setCellValue(sADDRESS);
                                                valueADDRESS.setCellStyle(my_styleBranch);
                                                
                                                Cell valueProStatus = row1.createCell((short) 18);
                                                valueProStatus.setCellValue(rsReportBranch1.FILE_MANAGER_STATE_DESC);
                                                valueProStatus.setCellStyle(my_styleBranch);
                                                
                                                String sRegister = "";
                                                String sConfirm = "";
                                                String sDKKD = "";
                                                String sCMND = "";
                                                String sBRIEF_PROPERTIES = rsReportBranch1.BRIEF_PROPERTIES;
                                                if(!"".equals(sBRIEF_PROPERTIES))
                                                {
                                                    CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
                                                    CommonFunction.getCollectedBriefProperties(sBRIEF_PROPERTIES, resIPData);
                                                    if(resIPData[0].length > 0) {
                                                        boolean bRegister = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_SERVICE_REGISTRATION_DOCUMENT, resIPData);
                                                        if(bRegister == true){sRegister = "x";}
                                                        boolean bConfirm = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_MINUTES_OF_HANDOVER, resIPData);
                                                        if(bConfirm == true){sConfirm = "x";}
                                                        boolean bDKKD = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION, resIPData);
                                                        if(bDKKD == true){sDKKD = "x";}
                                                        boolean bCMND = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD, resIPData);
                                                        if(bCMND == true){sCMND = "x";}
                                                    }
                                                }
                                                Cell valueRegister = row1.createCell((short) 19);
                                                valueRegister.setCellValue(sRegister);
                                                valueRegister.setCellStyle(my_styleBranch);
                                                
                                                Cell valueConfirm = row1.createCell((short) 20);
                                                valueConfirm.setCellValue(sConfirm);
                                                valueConfirm.setCellStyle(my_styleBranch);
                                                
                                                Cell valueDKKD = row1.createCell((short) 21);
                                                valueDKKD.setCellValue(sDKKD);
                                                valueDKKD.setCellStyle(my_styleBranch);
                                                
                                                Cell valueCMND = row1.createCell((short) 22);
                                                valueCMND.setCellValue(sCMND);
                                                valueCMND.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch7 = row1.createCell((short) 23);
                                                cellBranch7.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PHONE_CONTRACT));
                                                cellBranch7.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch8 = row1.createCell((short) 24);
                                                cellBranch8.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.EMAIL_CONTRACT));
                                                cellBranch8.setCellStyle(my_styleBranch);
                                                i++;
                                            }
                                            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                            wb.write(outByteStream);
                                            byte[] outArray = outByteStream.toByteArray();
                                            File someFile = new File(strURLPath);
                                            FileOutputStream fos = new FileOutputStream(someFile);
                                            fos.write(outArray);
                                            fos.flush();
                                            strView = "0#" + strURLPath + "#" + sFileName;
                                        } else {
                                            strView = "2#1";
                                        }
                                    } else {
                                        strView = "2#1";
                                    }
//                                } else {
//                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
//                                }
                                //</editor-fold>
                                break;
                            }
                            case "exportblacklist": {
                                //<editor-fold defaultstate="collapsed" desc="exportblacklist">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    Config conf = new Config();
                                    ConfigLanguage confLanguage = new ConfigLanguage();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_BLACKLIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    GENERAL_POLICY[][] rsPolicy = new GENERAL_POLICY[1][];
                                    com.S_BO_DISALLOWANCE_LIST_GET(loginLanguage, rsPolicy);
                                    String sJSON_CURRENT = "";
                                    if(rsPolicy[0].length > 0)
                                    {
                                        sJSON_CURRENT = rsPolicy[0][0].BLOB;
                                    }
                                    List<String> listBeforeJSON = new ArrayList<>();
                                    if(!"".equals(sJSON_CURRENT))
                                    {
                                        listBeforeJSON = CommonFunction.ConvertJsonToList(sJSON_CURRENT);
                                    }
                                    if (listBeforeJSON.size() > 0) {
                                        String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, loginLanguage).trim();
                                        String sCellAgency = "VALUE";
                                        HSSFWorkbook wb = new HSSFWorkbook();
                                        HSSFSheet sheet = wb.createSheet();
                                        sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                        sheet.setColumnWidth(1, 24 * 255);
                                        HSSFRow row = sheet.createRow(0);

                                        HSSFCellStyle my_style = wb.createCellStyle();
                                        HSSFFont font = wb.createFont();
                                        font.setBoldweight((short) 700);
                                        font.setFontName("Arial");
                                        my_style.setFont(font);
                                        my_style.setFillBackgroundColor((short) 9);
                                        my_style.setAlignment((short) 2);
                                        Cell cell = row.createCell((short) 0);
                                        cell.setCellValue(sCellSTT);
                                        cell.setCellStyle(my_style);
                                        Cell cell1 = row.createCell((short) 1);
                                        cell1.setCellValue(sCellAgency);
                                        cell1.setCellStyle(my_style);
                                        int i = 1;
                                        int k = 0;
                                        for (int j=0;j<listBeforeJSON.size();j++) {
                                            if (k == 0) {
                                                k = 1;
                                            } else {
                                                k++;
                                            }
                                            HSSFRow row1 = sheet.createRow(Integer.valueOf(i).intValue());
                                            HSSFCellStyle my_styleBranch = wb.createCellStyle();
                                            HSSFFont fontBranch = wb.createFont();

                                            fontBranch.setFontName("Arial");
//                                            fontBranch.setColor((short) 12);
                                            my_styleBranch.setFont(fontBranch);
                                            my_styleBranch.setFillBackgroundColor((short) 9);

                                            Cell cellBranch = row1.createCell((short) 0);
                                            cellBranch.setCellValue(k);
                                            cellBranch.setCellStyle(my_styleBranch);

                                            Cell cellBranch1 = row1.createCell((short) 1);
                                            cellBranch1.setCellValue(EscapeUtils.CheckTextNull(listBeforeJSON.get(j)));
                                            cellBranch1.setCellStyle(my_styleBranch);

                                            i++;
                                        }
                                        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                        wb.write(outByteStream);
                                        byte[] outArray = outByteStream.toByteArray();
                                        File someFile = new File(strURLPath);
                                        FileOutputStream fos = new FileOutputStream(someFile);
                                        fos.write(outArray);
                                        fos.flush();
                                        strView = "0#" + strURLPath + "#" + sFileName;
                                    } else {
                                        strView = "2#1";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "exportcontactcertlist": {
                                //<editor-fold defaultstate="collapsed" desc="exportcontactcertlist">
                                String anticsrf = request.getParameter("CsrfToken");
//                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    Config conf = new Config();
                                    ConfigLanguage confLanguage = new ConfigLanguage();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();

                                    String sFileName = "";
                                    String strURLPath ="";
                                    String vTypeContact = EscapeUtils.CheckTextNull(request.getParameter("vTypeContact"));
                                    CERTIFICATION_CONTACT[][] rsContact = new CERTIFICATION_CONTACT[1][];
                                    if("0".equals(vTypeContact))
                                    {
                                        com.S_BO_CERTIFICATION_DISALLOWANCE_LIST_GET_ALL_EMAIL(rsContact);
                                        sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_EMAIL_CONTACT + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                        strURLPath = pPathURL + sFileName;
                                    } else {
                                        com.S_BO_CERTIFICATION_DISALLOWANCE_LIST_GET_ALL_PHONE(rsContact);
                                        sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_PHONE_CONTACT + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                        strURLPath = pPathURL + sFileName;
                                    }
                                    if (rsContact[0].length > 0) {
                                        String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellAgency = "VALUE";
                                        String sCellTotal = "TOTAL";
                                        HSSFWorkbook wb = new HSSFWorkbook();
                                        HSSFSheet sheet = wb.createSheet();
                                        sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                        sheet.setColumnWidth(1, 24 * 255);
                                        HSSFRow row = sheet.createRow(0);

                                        HSSFCellStyle my_style = wb.createCellStyle();
                                        HSSFFont font = wb.createFont();
                                        font.setBoldweight((short) 700);
                                        font.setFontName("Arial");
                                        my_style.setFont(font);
                                        my_style.setFillBackgroundColor((short) 9);
                                        my_style.setAlignment((short) 2);
                                        Cell cell = row.createCell((short) 0);
                                        cell.setCellValue(sCellSTT);
                                        cell.setCellStyle(my_style);
                                        Cell cell1 = row.createCell((short) 1);
                                        cell1.setCellValue(sCellAgency);
                                        cell1.setCellStyle(my_style);
                                        Cell cell2 = row.createCell((short) 2);
                                        cell2.setCellValue(sCellTotal);
                                        cell2.setCellStyle(my_style);
                                        int i = 1;
                                        int k = 0;
                                        for (CERTIFICATION_CONTACT item : rsContact[0]) {
                                            if (k == 0) {
                                                k = 1;
                                            } else {
                                                k++;
                                            }
                                            HSSFRow row1 = sheet.createRow(Integer.valueOf(i).intValue());
                                            HSSFCellStyle my_styleBranch = wb.createCellStyle();
                                            HSSFFont fontBranch = wb.createFont();
                                            fontBranch.setFontName("Arial");
//                                            fontBranch.setColor((short) 12);
                                            my_styleBranch.setFont(fontBranch);
                                            my_styleBranch.setFillBackgroundColor((short) 9);
                                            Cell cellBranch = row1.createCell((short) 0);
                                            cellBranch.setCellValue(k);
                                            cellBranch.setCellStyle(my_styleBranch);
                                            Cell cellBranch1 = row1.createCell((short) 1);
                                            cellBranch1.setCellValue(item.VALUE.trim());
                                            cellBranch1.setCellStyle(my_styleBranch);
                                            Cell cellBranch2 = row1.createCell((short) 2);
                                            cellBranch2.setCellValue(item.TOTAL.trim());
                                            cellBranch2.setCellStyle(my_styleBranch);
                                            i++;
                                        }
                                        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                        wb.write(outByteStream);
                                        byte[] outArray = outByteStream.toByteArray();
                                        File someFile = new File(strURLPath);
                                        FileOutputStream fos = new FileOutputStream(someFile);
                                        fos.write(outArray);
                                        fos.flush();
                                        strView = "0#" + strURLPath + "#" + sFileName;
                                    } else {
                                        strView = "2#1";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "exportcertlist": {
                                //<editor-fold defaultstate="collapsed" desc="exportcertlist">
                                String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                String isUIDCollection = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_UID_COLLECTION_DISPLAY_ENABLED);
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
                                String SessRoleID_ID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                Config conf = new Config();
                                ConfigLanguage confLanguage = new ConfigLanguage();
                                String pDeviceDomainShow = conf.GetPropertybyCode(Definitions.CONFIG_DEVICE_DOMAIN_GRID_SHOW_ENABLED);
                                String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                if (!directory.exists()) {
                                    directory.mkdir();
                                }
                                String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_CERTIFICATE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                String strURLPath = pPathURL + sFileName;
                                String ToCreateDate = (String) request.getSession(false).getAttribute("sessToCreateDateRenewCert");
                                String FromCreateDate = (String) request.getSession(false).getAttribute("sessFromCreateDateRenewCert");
                                String CERT_SN = (String) request.getSession(false).getAttribute("sessCERT_SNRenewCert");
                                String FORM_FACTOR = (String) request.getSession(false).getAttribute("sessFormFactorRenewCert");
                                String TOKEN_SN = (String) request.getSession(false).getAttribute("sessTOKEN_SNRenewCert");
                                String PERSONAL_NAME = (String) request.getSession(false).getAttribute("sessPERSONAL_NAMERenewCert");
                                String COMPANY_NAME = (String) request.getSession(false).getAttribute("sessCOMPANY_NAMERenewCert");
                                String DOMAIN_NAME = (String) request.getSession(false).getAttribute("sessDOMAIN_NAMERenewCert");
                                String TAX_CODE = "";
                                String BUDGET_CODE = "";
                                String DECISION = "";
                                String P_ID = "";
                                String CCCD = "";
                                String PASSPORT = "";
                                String CERTIFICATION_ATTR_TYPE = (String) request.getSession(false).getAttribute("sessCERTIFICATION_ATTR_TYPERenewCert");
                                String CERTIFICATION_PURPOSE = (String) request.getSession(false).getAttribute("sessCERTIFICATION_PURPOSERenewCert");
                                String DEVICE_UUID_SEARCH = (String) request.getSession(false).getAttribute("sessDEVICE_UUIDRenewCert");
                                String IsTokenLost = (String) request.getSession(false).getAttribute("sessTokenLostRenewCert");
                                String IsByOwner = (String) request.getSession(false).getAttribute("sessIsByOwnerRenewCert");
                                String BranchOffice = (String) request.getSession(false).getAttribute("sessBranchOfficeRenewCert");
                                String USER = (String) request.getSession(false).getAttribute("sessUserRenewCert");
                                String CERTIFICATION_AUTHORITY_SEARCH = (String) request.getSession(false).getAttribute("sessCARenewCert");
                                String SERVICE_TYPE_SEARCH = (String) request.getSession(false).getAttribute("sessSERVICE_TYPERenewCert");
                                String strAlertAllTimes = (String) request.getSession(false).getAttribute("AlertAllTimeSRenewCert");
                                String sEnterpriseCert = "";
                                String sPersonalCert = "";
                                if(!isUIDCollection.equals("1")) {
                                    TAX_CODE = (String) request.getSession(false).getAttribute("sessTAX_CODERenewCert");
                                    BUDGET_CODE = (String) request.getSession(false).getAttribute("sessBUDGET_CODERenewCert");
                                    P_ID = (String) request.getSession(false).getAttribute("sessP_IDRenewCert");
                                    CCCD = (String) request.getSession(false).getAttribute("sessCCCDRenewCert");
                                    PASSPORT =(String) request.getSession(false).getAttribute("sessPASSPORTRenewCert");
                                    DECISION = (String) request.getSession(false).getAttribute("sessDECISIONRenewCert");
                                } else {
                                    String sENTERPRISE_PREFIX = (String) request.getSession(false).getAttribute("sessENTERPRISE_PREFIXRenewCert");
                                    String sPERSONAL_PREFIX = (String) request.getSession(false).getAttribute("sessPERSONAL_PREFIXRenewCert");
                                    String sENTERPRISE_ID = (String) request.getSession(false).getAttribute("sessENTERPRISE_IDRenewCert");
                                    String sPERSONAL_ID = (String) request.getSession(false).getAttribute("sessPERSONAL_IDRenewCert");
                                    if(!"".equals(sENTERPRISE_ID)){
                                        sEnterpriseCert = sENTERPRISE_PREFIX + "%"+sENTERPRISE_ID+"%";
                                    }
                                    if(!"".equals(sPERSONAL_ID)){
                                        sPersonalCert = sPERSONAL_PREFIX + "%"+sPERSONAL_ID+"%";
                                    }
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_ATTR_TYPE)) {
                                    CERTIFICATION_ATTR_TYPE = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_PURPOSE)) {
                                    CERTIFICATION_PURPOSE = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(FORM_FACTOR)) {
                                    FORM_FACTOR = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(IsTokenLost)) {
                                    IsTokenLost = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                    BranchOffice = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(USER)) {
                                    USER = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CERTIFICATION_AUTHORITY_SEARCH)) {
                                    CERTIFICATION_AUTHORITY_SEARCH = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(SERVICE_TYPE_SEARCH)) {
                                    SERVICE_TYPE_SEARCH = "";
                                }
                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    if(!"".equals(BranchOffice) && !"".equals(USER)) {
                                        SessUserAgentID = BranchOffice;
                                    }
                                }
                                if("1".equals(strAlertAllTimes)) {
                                    ToCreateDate = "";
                                    FromCreateDate = "";
                                }
                                CommonFunction.LogDebugString(log, "ExportCertList", "FromDate: " + FromCreateDate + "; ToDate: " + ToCreateDate + "; AGENCY: " + BranchOffice);
                                CERTIFICATION[][] rsPgin;
                                rsPgin = new CERTIFICATION[1][];
                                if (!"".equals(countList)) {
                                    boolean isHasStateTypeRequest = false;
                                    if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_USER)
                                        || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER))
                                    {
                                        isHasStateTypeRequest = true;
                                    }
                                    boolean isHasCREATED_BY = false;
                                    if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT))
                                    {
                                        if(!SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                            isHasCREATED_BY = true;
                                        }
                                    } else {
                                        isHasCREATED_BY = true;
                                    }
                                    boolean isHasBranch = false;
                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        isHasBranch = true;
                                    }
                                    String SessUserID = sessionsa.getAttribute("UserID").toString().trim();
                                    if("".equals(USER)) {
                                        if(!SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                            if(SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER) || SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_AGENT_ACCOUNTANT)) {
                                                USER = SessUserID;
                                            }
                                        }
                                    }
                                    if(!isUIDCollection.equals("1")) {
                                        String[] sUIDResult = new String[2];
                                        CommonReferServlet.collectFieldToUID(EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                            EscapeUtils.escapeHtmlSearch(DECISION), EscapeUtils.escapeHtmlSearch(P_ID), EscapeUtils.escapeHtmlSearch(PASSPORT),
                                            EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                        sEnterpriseCert = sUIDResult[0];
                                        sPersonalCert = sUIDResult[1];
                                    }
                                    String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                    com.S_BO_CERTIFICATION_LIST_EXPORT(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                        EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                        EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
                                        EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                        EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                        USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
                                        EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),
                                        IsByOwner, EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
                                        loginLanguage, rsPgin, sessTreeArrayBranchID, sEnterpriseCert, sPersonalCert);
                                    if(rsPgin[0].length > 0) {
                                        String queryString = getServletContext().getRealPath("/");
                                        String outputDirectory = queryString;
                                        String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                        FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                        inputStream.close();
                                        String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellUserCreate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_USER_CREATE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellMST = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_ENTERPRISE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellSTATE = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_STATUS, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellCompany = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COMPANY_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sDeviceUUID = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_DEVICE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellPesonal = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PERSONAL_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICEPACK_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellRequestType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REQUEST_TYPE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDateCreate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CREATE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDateGen = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_GEN, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDateEffective = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EFFECTIVE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDateExpiration = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EXPIRATION, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDateExpireMMYY = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EXPIRATION_ONLY_MMYY, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDateRevoke = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_REVOKE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCertType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_TYPE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCertAttrState = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REQUEST_STATUS, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_SN, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCertSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sTokenState = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_STATUS, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sReqTokenDate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_DATE_LOCK, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellFormFactor = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_METHOD_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                        wb.setCompressTempFiles(true);
                                        Font fontBranch = wb.createFont();
                                        fontBranch.setFontName("Arial");
                                        CreationHelper createHelper = wb.getCreationHelper();
                                        CellStyle styleText = wb.createCellStyle();
                                        styleText.setFont(fontBranch);
                                        styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));
                                        
                                        //<editor-fold defaultstate="collapsed" desc="### SHEET 1">
                                        if(rsPgin[0][0].ID != 0)
                                        {
//                                            SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
                                            SXSSFSheet sheet = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_LIST, request.getSession(false).getAttribute("sessVN").toString()).trim());
                                            sheet.setRandomAccessWindowSize(100);
                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                            int iCountTitle = 0;
                                            if(isHasBranch == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                sheet.setColumnWidth(iCountTitle, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                            }
                                            if(isHasCREATED_BY == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                sheet.setColumnWidth(iCountTitle, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                            }
                                            sheet.setColumnWidth(iCountTitle+1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                            sheet.setColumnWidth(iCountTitle+2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                            sheet.setColumnWidth(iCountTitle+3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                            iCountTitle = iCountTitle+4;
                                            sheet.setColumnWidth(iCountTitle, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            if("1".equals(pDeviceDomainShow)){
                                                iCountTitle = iCountTitle+1;
                                                sheet.setColumnWidth(iCountTitle, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);//device_uuid
                                            }
                                            sheet.setColumnWidth(iCountTitle+1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                            sheet.setColumnWidth(iCountTitle+2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                            sheet.setColumnWidth(iCountTitle+3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CERT_TYPE);
                                            sheet.setColumnWidth(iCountTitle+4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CERT_TYPE);
                                            iCountTitle = iCountTitle+5;
                                            sheet.setColumnWidth(iCountTitle, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STATE);
                                            if(isHasStateTypeRequest == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                sheet.setColumnWidth(iCountTitle, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_STATE);
                                            }
                                            sheet.setColumnWidth(iCountTitle+1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                            sheet.setColumnWidth(iCountTitle+2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                            sheet.setColumnWidth(iCountTitle+3, 24 * 255);
                                            sheet.setColumnWidth(iCountTitle+4, 24 * 255);
                                            sheet.setColumnWidth(iCountTitle+5, 24 * 255);
                                            sheet.setColumnWidth(iCountTitle+6, 28 * 255);
                                            sheet.setColumnWidth(iCountTitle+7, 18 * 255);
                                            sheet.setColumnWidth(iCountTitle+8, 18 * 255);
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                sheet.setColumnWidth(iCountTitle+9, 18 * 255);
                                                sheet.setColumnWidth(iCountTitle+10, 18 * 255);
                                                sheet.setColumnWidth(iCountTitle+11, 24 * 255);
                                                sheet.setColumnWidth(iCountTitle+12, 18 * 255);
                                                sheet.setColumnWidth(iCountTitle+13, 24 * 255);
                                            }
                                            Row row = sheet.createRow(0);

                                            CellStyle my_style = wb.createCellStyle();
                                            Font font = wb.createFont();
                                            font.setBoldweight((short) 700);
                                            font.setFontName("Arial");
                                            my_style.setFont(font);
                                            my_style.setFillBackgroundColor((short) 9);
                                            my_style.setAlignment((short) 2);
                                            Cell cell = row.createCell((short) 0);
                                            cell.setCellValue(sCellSTT);
                                            cell.setCellStyle(my_style);
                                            iCountTitle = 0;
                                            if(isHasBranch == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                Cell cell1 = row.createCell((short) iCountTitle);
                                                cell1.setCellValue(sCellAgency);
                                                cell1.setCellStyle(my_style);
                                            }
                                            if(isHasCREATED_BY == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                Cell cell2 = row.createCell((short) iCountTitle);
                                                cell2.setCellValue(sCellUserCreate);
                                                cell2.setCellStyle(my_style);
                                            }

                                            Cell cell3 = row.createCell((short) iCountTitle+1);
                                            cell3.setCellValue(sCellMST);
                                            cell3.setCellStyle(my_style);

                                            Cell cell5 = row.createCell((short) iCountTitle+2);
                                            cell5.setCellValue(sCellCompany);
                                            cell5.setCellStyle(my_style);
                                            
                                            
                                            Cell cell6 = row.createCell((short) iCountTitle +3);
                                            cell6.setCellValue(sCellCMND);
                                            cell6.setCellStyle(my_style);
                                            
                                            iCountTitle = iCountTitle+4;
                                            Cell cell7 = row.createCell((short) iCountTitle);
                                            cell7.setCellValue(sCellPesonal);
                                            cell7.setCellStyle(my_style);

                                            if("1".equals(pDeviceDomainShow)){
                                                iCountTitle = iCountTitle+1;
                                                Cell cell61 = row.createCell((short) iCountTitle);
                                                cell61.setCellValue(sDeviceUUID);
                                                cell61.setCellStyle(my_style);//device_uuid
                                            }

                                            Cell cell9 = row.createCell((short) iCountTitle+1);
                                            cell9.setCellValue(sCellProfile);
                                            cell9.setCellStyle(my_style);
                                            
                                            Cell cell10 = row.createCell((short) iCountTitle + 2);
                                            cell10.setCellValue(sCellRequestType);
                                            cell10.setCellStyle(my_style);

                                            Cell cell8 = row.createCell((short) iCountTitle+3);
                                            cell8.setCellValue(sCertType);
                                            cell8.setCellStyle(my_style);
                                            
                                            Cell cellMethod = row.createCell((short) iCountTitle+4);
                                            cellMethod.setCellValue(sCellFormFactor);
                                            cellMethod.setCellStyle(my_style);
                                            
                                            iCountTitle = iCountTitle+5;
                                            Cell cell4 = row.createCell((short) iCountTitle);
                                            cell4.setCellValue(sCellSTATE);
                                            cell4.setCellStyle(my_style);
                                            
                                            if(isHasStateTypeRequest == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                Cell cell41 = row.createCell((short) iCountTitle);
                                                cell41.setCellValue(sCertAttrState);
                                                cell41.setCellStyle(my_style);
                                            }

                                            Cell cell11 = row.createCell((short) iCountTitle+1);
                                            cell11.setCellValue(sCellDateCreate);
                                            cell11.setCellStyle(my_style);

                                            Cell cell13 = row.createCell((short) iCountTitle+2);
                                            cell13.setCellValue(sCellDateGen);
                                            cell13.setCellStyle(my_style);
                                            
                                            Cell cell1Effective = row.createCell((short) iCountTitle+3);
                                            cell1Effective.setCellValue(sCellDateEffective);
                                            cell1Effective.setCellStyle(my_style);
                                            
                                            Cell cell1Expiration = row.createCell((short) iCountTitle+4);
                                            cell1Expiration.setCellValue(sCellDateExpiration);
                                            cell1Expiration.setCellStyle(my_style);
                                            
                                            Cell cell1Revoke = row.createCell((short) iCountTitle+5);
                                            cell1Revoke.setCellValue(sCellDateRevoke);
                                            cell1Revoke.setCellStyle(my_style);

                                            Cell cell12 = row.createCell((short) iCountTitle+6);
                                            cell12.setCellValue(sCertSN);
                                            cell12.setCellStyle(my_style);

                                            Cell cell15 = row.createCell((short) iCountTitle+7);
                                            cell15.setCellValue(sTokenSN);
                                            cell15.setCellStyle(my_style);
                                            
                                            Cell cell16 = row.createCell((short) iCountTitle+8);
                                            cell16.setCellValue(sTokenState);
                                            cell16.setCellStyle(my_style);
                                            if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                if(!SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)) {
                                                    Cell cell1ExpireMMYY = row.createCell((short) iCountTitle+9);
                                                    cell1ExpireMMYY.setCellValue(sCellDateExpireMMYY);
                                                    cell1ExpireMMYY.setCellStyle(my_style);

                                                    Cell cellPhone = row.createCell((short) iCountTitle+10);
                                                    cellPhone.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PHONE_CONTACT, request.getSession(false).getAttribute("sessVN").toString()).trim());
                                                    cellPhone.setCellStyle(my_style);

                                                    Cell cellEmail = row.createCell((short) iCountTitle+11);
                                                    cellEmail.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_EMAIL_CONTACT, request.getSession(false).getAttribute("sessVN").toString()).trim());
                                                    cellEmail.setCellStyle(my_style);

                                                    Cell cellPhoneReal = row.createCell((short) iCountTitle+12);
                                                    cellPhoneReal.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PHONE_REAL, request.getSession(false).getAttribute("sessVN").toString()).trim());
                                                    cellPhoneReal.setCellStyle(my_style);

                                                    Cell cellEmailReal = row.createCell((short) iCountTitle+13);
                                                    cellEmailReal.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_EMAIL_REAL, request.getSession(false).getAttribute("sessVN").toString()).trim());
                                                    cellEmailReal.setCellStyle(my_style);
                                                }
                                            }

                                            int i = 1;
                                            int k = 0;
                                            for (CERTIFICATION temp1 : rsPgin[0]) {
                                                if (k == 0) {
                                                    k = 1;
                                                } else {
                                                    k++;
                                                }
                                                String sMSTOrMSN = temp1.ENTERPRISE_ID;
                                                String sCMNDOrHC = temp1.PERSONAL_ID;
//                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
//                                                    sMSTOrMSN = CommonReferServlet.filterPrefixUIDLanguage(temp1.ENTERPRISE_ID, loginLanguage);
//                                                    sCMNDOrHC = CommonReferServlet.filterPrefixUIDLanguage(temp1.PERSONAL_ID, loginLanguage);
//                                                }
                                                Row row1 = sheet.createRow(i);
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                my_styleBranch.setFont(fontBranch);

                                                CellStyle my_styleBranchDate = wb.createCellStyle();
                                                Font fontBranchDate = wb.createFont();
                                                fontBranchDate.setFontName("Arial");
                                                my_styleBranchDate.setFont(fontBranchDate);
                                                my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));
                                                
                                                Cell cellBranch = row1.createCell((short) 0);
                                                cellBranch.setCellValue(k);
                                                cellBranch.setCellStyle(my_styleBranch);
                                                
                                                int iCountValue = 0;
                                                if(isHasBranch == true)
                                                {
                                                    iCountValue = iCountValue+1;
                                                    Cell cellBranch10 = row1.createCell((short) iCountValue);
                                                    cellBranch10.setCellValue(EscapeUtils.CheckTextNull(temp1.BRANCH_DESC));
                                                    cellBranch10.setCellStyle(my_styleBranch);
                                                }
                                                if(isHasCREATED_BY == true)
                                                {
                                                    iCountValue = iCountValue+1;
                                                    Cell cellBranch10 = row1.createCell((short) iCountValue);
                                                    cellBranch10.setCellValue(EscapeUtils.CheckTextNull(temp1.CREATED_BY));
                                                    cellBranch10.setCellStyle(my_styleBranch);
                                                }

                                                Cell cellBranch2 = row1.createCell((short) iCountValue+1);
                                                cellBranch2.setCellValue(sMSTOrMSN);
                                                cellBranch2.setCellStyle(styleText);

                                                Cell cellBranch1 = row1.createCell((short) iCountValue+2);
                                                cellBranch1.setCellValue(EscapeUtils.CheckTextNull(temp1.COMPANY_NAME));
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch4 = row1.createCell((short) iCountValue + 3);
                                                cellBranch4.setCellValue(sCMNDOrHC);
                                                cellBranch4.setCellStyle(styleText);
                                                
                                                iCountValue = iCountValue+4;
                                                Cell cellBranch3 = row1.createCell((short) iCountValue);
                                                cellBranch3.setCellValue(EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME));
                                                cellBranch3.setCellStyle(my_styleBranch);
                                                
                                                if("1".equals(pDeviceDomainShow)) {
                                                    iCountValue = iCountValue+1;
                                                    String strDeviceUUID = "";
                                                    if(temp1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                        strDeviceUUID = temp1.SERVICE_UUID;
                                                    }
                                                    Cell cellBranch41 = row1.createCell((short) iCountValue);
                                                    cellBranch41.setCellValue(strDeviceUUID);
                                                    cellBranch41.setCellStyle(styleText);
                                                }

                                                Cell cellBranch5 = row1.createCell((short) iCountValue + 1);
                                                cellBranch5.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_NAME));
                                                cellBranch5.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch9 = row1.createCell((short) iCountValue + 2);
                                                cellBranch9.setCellValue(EscapeUtils.CheckTextNull(temp1.SERVICE_TYPE_DESC));
                                                cellBranch9.setCellStyle(my_styleBranch);

                                                Cell cellBranch6 = row1.createCell((short) iCountValue + 3);
                                                cellBranch6.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PURPOSE_DESC));
                                                cellBranch6.setCellStyle(my_styleBranch);
                                                
                                                Cell valueMethod = row1.createCell((short) iCountValue + 4);
                                                valueMethod.setCellValue(EscapeUtils.CheckTextNull(temp1.PKI_FORMFACTOR_DESC));
                                                valueMethod.setCellStyle(my_styleBranch);

                                                iCountValue = iCountValue+5;
                                                Cell cellBranch7 = row1.createCell((short) iCountValue);
                                                cellBranch7.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_STATE_DESC));
                                                cellBranch7.setCellStyle(my_styleBranch);
                                                if(isHasStateTypeRequest == true) {
                                                    iCountValue = iCountValue+1;
                                                    Cell cellBranch8 = row1.createCell((short) iCountValue);
                                                    cellBranch8.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_STATE_DESC));
                                                    cellBranch8.setCellStyle(my_styleBranch);
                                                }
                                                Cell cellBranch12 = row1.createCell((short) iCountValue + 1);
                                                Date dateCreate = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(temp1.CREATED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                if(dateCreate != null) {
                                                    cellBranch12.setCellValue(dateCreate);
                                                } else {
                                                    cellBranch12.setCellValue("");
                                                }
                                                cellBranch12.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch14 = row1.createCell((short) iCountValue + 2);
                                                Date dateOperation = CommonFunction.convertStringToDate(temp1.OPERATED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                if(dateOperation != null) {
                                                    cellBranch14.setCellValue(dateOperation);
                                                } else {
                                                    cellBranch14.setCellValue("");
                                                }
                                                cellBranch14.setCellStyle(my_styleBranchDate);
                                                
                                                Cell cellValueEffective = row1.createCell((short) iCountValue + 3);
                                                Date dateEffective = CommonFunction.convertStringToDate(temp1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                if(dateEffective != null) {
                                                    cellValueEffective.setCellValue(dateEffective);
                                                } else {
                                                    cellValueEffective.setCellValue("");
                                                }
                                                cellValueEffective.setCellStyle(my_styleBranchDate);
                                                
                                                Cell cellValueExpiration = row1.createCell((short) iCountValue + 4);
                                                Date dateExpiration = CommonFunction.convertStringToDate(temp1.EXPIRATION_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                if(dateExpiration != null) {
                                                    cellValueExpiration.setCellValue(dateExpiration);
                                                } else {
                                                    cellValueExpiration.setCellValue("");
                                                }
                                                cellValueExpiration.setCellStyle(my_styleBranchDate);
                                                
                                                Cell cellValueRevoke = row1.createCell((short) iCountValue + 5);
                                                Date dateRevoke = CommonFunction.convertStringToDate(temp1.REVOKED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                if(dateRevoke != null) {
                                                    cellValueRevoke.setCellValue(dateRevoke);
                                                } else {
                                                    cellValueRevoke.setCellValue("");
                                                }
                                                cellValueRevoke.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch13 = row1.createCell((short) iCountValue + 6);
                                                cellBranch13.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_SN));
                                                cellBranch13.setCellStyle(styleText);

                                                String valueTokenSN = EscapeUtils.CheckTextNull(temp1.TOKEN_SN);
                                                if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                Cell cellBranch15 = row1.createCell((short) iCountValue + 7);
                                                cellBranch15.setCellValue(valueTokenSN);
                                                cellBranch15.setCellStyle(styleText);
                                                
                                                Cell cellBranch16 = row1.createCell((short) iCountValue + 8);
                                                cellBranch16.setCellValue(temp1.TOKEN_STATE_DESC);
                                                cellBranch16.setCellStyle(my_styleBranch);
                                                
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA) && SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                                    if(!SessRoleID_ID.equals(Definitions.CONFIG_ROLE_ID_CA_SURVEYOR)) {
                                                        Cell valueExpireMMYY = row1.createCell((short) iCountValue + 9);
                                                        valueExpireMMYY.setCellValue(EscapeUtils.CheckTextNull(temp1.EXPIRATION_DT_DESC));
                                                        valueExpireMMYY.setCellStyle(my_styleBranch);

                                                        Cell valuePhone = row1.createCell((short) iCountValue + 10);
                                                        valuePhone.setCellValue(temp1.PHONE_CONTRACT);
                                                        valuePhone.setCellStyle(my_styleBranch);

                                                        Cell valueEmail = row1.createCell((short) iCountValue + 11);
                                                        valueEmail.setCellValue(temp1.EMAIL_CONTRACT);
                                                        valueEmail.setCellStyle(my_styleBranch);

                                                        Cell valuePhoneReal = row1.createCell((short) iCountValue + 12);
                                                        valuePhoneReal.setCellValue(temp1.PHONE_CONTRACT_REAL);
                                                        valuePhoneReal.setCellStyle(my_styleBranch);

                                                        Cell valueEmailReal = row1.createCell((short) iCountValue + 13);
                                                        valueEmailReal.setCellValue(temp1.EMAIL_CONTRACT_REAL);
                                                        valueEmailReal.setCellStyle(my_styleBranch);
                                                    }
                                                }
                                                i++;
                                            }
                                        }
                                        //</editor-fold>
                                        
                                        //<editor-fold defaultstate="collapsed" desc="## SHEET 2">
                                        rsPgin = new CERTIFICATION[1][];
                                        com.S_BO_CERTIFICATION_LIST_TOKEN_LOCK(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
                                            EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                            EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                            USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
                                            EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),
                                            IsByOwner, EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
                                            loginLanguage, rsPgin, sEnterpriseCert, sPersonalCert);
                                        if(rsPgin[0].length > 0)
                                        {
                                            SXSSFSheet sheet2 = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_TOKENLOCKED_LIST, request.getSession(false).getAttribute("sessVN").toString()).trim());
                                            sheet2.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                            sheet2.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                            sheet2.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                            sheet2.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                            sheet2.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            sheet2.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            sheet2.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                            sheet2.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CERT_TYPE);
                                            sheet2.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STATE);
                                            int iCountTitle2 = 8;
                                            if(isHasStateTypeRequest == true)
                                            {
                                                sheet2.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_STATE);
                                                sheet2.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                                iCountTitle2 = 10;
                                            }
                                            if(isHasCREATED_BY == true)
                                            {
                                                iCountTitle2 = iCountTitle2+1;
                                                sheet2.setColumnWidth(iCountTitle2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                            }
                                            if(isHasBranch == true)
                                            {
                                                iCountTitle2 = iCountTitle2+1;
                                                sheet2.setColumnWidth(iCountTitle2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                            }
                                            sheet2.setColumnWidth(iCountTitle2+1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                            sheet2.setColumnWidth(iCountTitle2+2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                            sheet2.setColumnWidth(iCountTitle2+3, 32 * 255);
                                            sheet2.setColumnWidth(iCountTitle2+4, 18 * 255);
                                            sheet2.setColumnWidth(iCountTitle2+5, 24 * 255);

                                            Row row2 = sheet2.createRow(0);
                                            CellStyle my_style2 = wb.createCellStyle();
                                            Font font2 = wb.createFont();
                                            font2.setBoldweight((short) 700);
                                            font2.setFontName("Arial");
                                            my_style2.setFont(font2);
                                            my_style2.setFillBackgroundColor((short) 9);
                                            my_style2.setAlignment((short) 2);
                                            Cell cell2 = row2.createCell((short) 0);
                                            cell2.setCellValue(sCellSTT);
                                            cell2.setCellStyle(my_style2);

                                            Cell cell52 = row2.createCell((short) 1);
                                            cell52.setCellValue(sCellCompany);
                                            cell52.setCellStyle(my_style2);

                                            Cell cell32 = row2.createCell((short) 2);
                                            cell32.setCellValue(sCellMST);
                                            cell32.setCellStyle(my_style2);

                                            Cell cell72 = row2.createCell((short) 3);
                                            cell72.setCellValue(sCellPesonal);
                                            cell72.setCellStyle(my_style2);

                                            Cell cell62 = row2.createCell((short) 4);
                                            cell62.setCellValue(sCellCMND);
                                            cell62.setCellStyle(my_style2);

                                            Cell cell612 = row2.createCell((short) 5);
                                            cell612.setCellValue(sDeviceUUID);
                                            cell612.setCellStyle(my_style2);

                                            Cell cell92 = row2.createCell((short) 6);
                                            cell92.setCellValue(sCellProfile);
                                            cell92.setCellStyle(my_style2);

                                            Cell cell82 = row2.createCell((short) 7);
                                            cell82.setCellValue(sCertType);
                                            cell82.setCellStyle(my_style2);

                                            Cell cell42 = row2.createCell((short) 8);
                                            cell42.setCellValue(sCellSTATE);
                                            cell42.setCellStyle(my_style2);
                                            iCountTitle2 = 8;
                                            if(isHasStateTypeRequest == true)
                                            {
                                                Cell cell412 = row2.createCell((short) 9);
                                                cell412.setCellValue(sCertAttrState);
                                                cell412.setCellStyle(my_style2);

                                                Cell cell102 = row2.createCell((short) 10);
                                                cell102.setCellValue(sCellRequestType);
                                                cell102.setCellStyle(my_style2);
                                                iCountTitle2 = 10;
                                            }
                                            if(isHasCREATED_BY == true)
                                            {
                                                iCountTitle2 = iCountTitle2+1;
                                                Cell cell22 = row2.createCell((short) iCountTitle2);
                                                cell22.setCellValue(sCellUserCreate);
                                                cell22.setCellStyle(my_style2);
                                            }
                                            if(isHasBranch == true)
                                            {
                                                iCountTitle2 = iCountTitle2+1;
                                                Cell cell122 = row2.createCell((short) iCountTitle2);
                                                cell122.setCellValue(sCellAgency);
                                                cell122.setCellStyle(my_style2);
                                            }

                                            Cell cell112 = row2.createCell((short) iCountTitle2+1);
                                            cell112.setCellValue(sCellDateCreate);
                                            cell112.setCellStyle(my_style2);

                                            Cell cell132 = row2.createCell((short) iCountTitle2+2);
                                            cell132.setCellValue(sCellDateGen);
                                            cell132.setCellStyle(my_style2);

                                            Cell cell122 = row2.createCell((short) iCountTitle2+3);
                                            cell122.setCellValue(sCertSN);
                                            cell122.setCellStyle(my_style2);

                                            Cell cell152 = row2.createCell((short) iCountTitle2+4);
                                            cell152.setCellValue(sTokenSN);
                                            cell152.setCellStyle(my_style2);

                                            Cell cell153 = row2.createCell((short) iCountTitle2+5);
                                            cell153.setCellValue(sReqTokenDate);
                                            cell153.setCellStyle(my_style2);

                                            int i = 1;
                                            int k = 0;
                                            for (CERTIFICATION temp1 : rsPgin[0]) {
                                                if (k == 0) {
                                                    k = 1;
                                                } else {
                                                    k++;
                                                }
                                                String sMSTOrMSN = temp1.ENTERPRISE_ID;// EscapeUtils.CheckTextNull(temp1.TAX_CODE);
//                                                if("".equals(sMSTOrMSN)) {
//                                                    sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.BUDGET_CODE);
//                                                }
//                                                if("".equals(sMSTOrMSN)) {
//                                                    sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.DECISION);
//                                                }
                                                String sCMNDOrHC = temp1.PERSONAL_ID;// EscapeUtils.CheckTextNull(temp1.P_ID);
//                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
//                                                    sMSTOrMSN = CommonReferServlet.filterPrefixUIDLanguage(temp1.ENTERPRISE_ID, loginLanguage);
//                                                    sCMNDOrHC = CommonReferServlet.filterPrefixUIDLanguage(temp1.PERSONAL_ID, loginLanguage);
//                                                }
//                                                if("".equals(sCMNDOrHC)) {
//                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_EID);
//                                                }
//                                                if("".equals(sCMNDOrHC)) {
//                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.PASSPORT);
//                                                }
                                                Row row1 = sheet2.createRow(i);
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                my_styleBranch.setFont(fontBranch);

                                                CellStyle my_styleBranchDate = wb.createCellStyle();
                                                Font fontBranchDate = wb.createFont();
                                                fontBranchDate.setFontName("Arial");
                                                my_styleBranchDate.setFont(fontBranchDate);
                                                my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));

                                                Cell cellBranch = row1.createCell((short) 0);
                                                cellBranch.setCellValue(k);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row1.createCell((short) 1);
                                                cellBranch1.setCellValue(EscapeUtils.CheckTextNull(temp1.COMPANY_NAME));
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch2 = row1.createCell((short) 2);
                                                cellBranch2.setCellValue(sMSTOrMSN);
                                                cellBranch2.setCellStyle(styleText);

                                                Cell cellBranch3 = row1.createCell((short) 3);
                                                cellBranch3.setCellValue(EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME));
                                                cellBranch3.setCellStyle(my_styleBranch);

                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(sCMNDOrHC);
                                                cellBranch4.setCellStyle(styleText);

                                                String strDeviceUUID = "";
                                                if(temp1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                    strDeviceUUID = temp1.SERVICE_UUID;
                                                }
                                                Cell cellBranch41 = row1.createCell((short) 5);
                                                cellBranch41.setCellValue(strDeviceUUID);
                                                cellBranch41.setCellStyle(styleText);

                                                Cell cellBranch5 = row1.createCell((short) 6);
                                                cellBranch5.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_NAME));
                                                cellBranch5.setCellStyle(my_styleBranch);

                                                Cell cellBranch6 = row1.createCell((short) 7);
                                                cellBranch6.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PURPOSE_DESC));
                                                cellBranch6.setCellStyle(my_styleBranch);

                                                Cell cellBranch7 = row1.createCell((short) 8);
                                                cellBranch7.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_STATE_DESC));
                                                cellBranch7.setCellStyle(my_styleBranch);
                                                int iCountValue = 8;
                                                if(isHasStateTypeRequest == true) {
                                                    Cell cellBranch8 = row1.createCell((short) 9);
                                                    cellBranch8.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_STATE_DESC));
                                                    cellBranch8.setCellStyle(my_styleBranch);

                                                    Cell cellBranch9 = row1.createCell((short) 10);
                                                    cellBranch9.setCellValue(EscapeUtils.CheckTextNull(temp1.SERVICE_TYPE_DESC));
                                                    cellBranch9.setCellStyle(my_styleBranch);
                                                    iCountValue = 10;
                                                }
                                                if(isHasCREATED_BY == true) {
                                                    iCountValue = iCountValue+1;
                                                    Cell cellBranch10 = row1.createCell((short) iCountValue);
                                                    cellBranch10.setCellValue(EscapeUtils.CheckTextNull(temp1.CREATED_BY));
                                                    cellBranch10.setCellStyle(my_styleBranch);
                                                }
                                                if(isHasBranch == true) {
                                                    iCountValue = iCountValue+1;
                                                    Cell cellBranch10 = row1.createCell((short) iCountValue);
                                                    cellBranch10.setCellValue(EscapeUtils.CheckTextNull(temp1.BRANCH_DESC));
                                                    cellBranch10.setCellStyle(my_styleBranch);
                                                }
                                                Cell cellBranch12 = row1.createCell((short) iCountValue + 1);
                                                Date dateCreate = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(temp1.CREATED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                if(dateCreate != null) {
                                                    cellBranch12.setCellValue(dateCreate);
                                                } else {
                                                    cellBranch12.setCellValue("");
                                                }
                                                cellBranch12.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch14 = row1.createCell((short) iCountValue + 2);
                                                Date dateOperation = CommonFunction.convertStringToDate(temp1.OPERATED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                if(dateOperation != null) {
                                                    cellBranch14.setCellValue(dateOperation);
                                                } else {
                                                    cellBranch14.setCellValue("");
                                                }
                                                cellBranch14.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch13 = row1.createCell((short) iCountValue + 3);
                                                cellBranch13.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_SN));
                                                cellBranch13.setCellStyle(styleText);

                                                String valueTokenSN = EscapeUtils.CheckTextNull(temp1.TOKEN_SN);
                                                if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                Cell cellBranch15 = row1.createCell((short) iCountValue + 4);
                                                cellBranch15.setCellValue(valueTokenSN);
                                                cellBranch15.setCellStyle(styleText);

                                                Cell cellBranch152 = row1.createCell((short) iCountValue + 5);
                                                cellBranch152.setCellValue(temp1.CREATED_LOCK_REQUEST_DT);
                                                cellBranch152.setCellStyle(my_styleBranch);
                                                i++;
                                            }
                                        }
                                        //</editor-fold>
                                        
                                        //<editor-fold defaultstate="collapsed" desc="## SHEET 3">
                                        rsPgin = new CERTIFICATION[1][];
                                        com.S_BO_CERTIFICATION_LIST_TOKEN_WAIT_TO_LOCK(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
                                            EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                            EscapeUtils.escapeHtmlSearch(DOMAIN_NAME),
                                            EscapeUtils.escapeHtmlSearch(BranchOffice),
                                            USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
                                            EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),
                                            IsByOwner, EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
                                            loginLanguage, rsPgin, sEnterpriseCert, sPersonalCert);
                                        if(rsPgin[0].length > 0)
                                        {
                                            SXSSFSheet sheet2 = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_TOKENLOCKWAIT_LIST, request.getSession(false).getAttribute("sessVN").toString()).trim());
                                            sheet2.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                            sheet2.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                            sheet2.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                            sheet2.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                            sheet2.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            sheet2.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            sheet2.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                            sheet2.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CERT_TYPE);
                                            sheet2.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STATE);
                                            int iCountTitle2 = 8;
                                            if(isHasStateTypeRequest == true)
                                            {
                                                sheet2.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_STATE);
                                                sheet2.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                                iCountTitle2 = 10;
                                            }
                                            if(isHasCREATED_BY == true)
                                            {
                                                iCountTitle2 = iCountTitle2+1;
                                                sheet2.setColumnWidth(iCountTitle2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                            }
                                            if(isHasBranch == true)
                                            {
                                                iCountTitle2 = iCountTitle2+1;
                                                sheet2.setColumnWidth(iCountTitle2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                            }
                                            sheet2.setColumnWidth(iCountTitle2+1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                            sheet2.setColumnWidth(iCountTitle2+2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                            sheet2.setColumnWidth(iCountTitle2+3, 32 * 255);
                                            sheet2.setColumnWidth(iCountTitle2+4, 18 * 255);
                                            sheet2.setColumnWidth(iCountTitle2+5, 24 * 255);

                                            Row row2 = sheet2.createRow(0);

                                            CellStyle my_style2 = wb.createCellStyle();
                                            Font font2 = wb.createFont();
                                            font2.setBoldweight((short) 700);
                                            font2.setFontName("Arial");
                                            my_style2.setFont(font2);
                                            my_style2.setFillBackgroundColor((short) 9);
                                            my_style2.setAlignment((short) 2);
                                            Cell cell2 = row2.createCell((short) 0);
                                            cell2.setCellValue(sCellSTT);
                                            cell2.setCellStyle(my_style2);

                                            Cell cell52 = row2.createCell((short) 1);
                                            cell52.setCellValue(sCellCompany);
                                            cell52.setCellStyle(my_style2);

                                            Cell cell32 = row2.createCell((short) 2);
                                            cell32.setCellValue(sCellMST);
                                            cell32.setCellStyle(my_style2);

                                            Cell cell72 = row2.createCell((short) 3);
                                            cell72.setCellValue(sCellPesonal);
                                            cell72.setCellStyle(my_style2);

                                            Cell cell62 = row2.createCell((short) 4);
                                            cell62.setCellValue(sCellCMND);
                                            cell62.setCellStyle(my_style2);

                                            Cell cell612 = row2.createCell((short) 5);
                                            cell612.setCellValue(sDeviceUUID);
                                            cell612.setCellStyle(my_style2);

                                            Cell cell92 = row2.createCell((short) 6);
                                            cell92.setCellValue(sCellProfile);
                                            cell92.setCellStyle(my_style2);

                                            Cell cell82 = row2.createCell((short) 7);
                                            cell82.setCellValue(sCertType);
                                            cell82.setCellStyle(my_style2);

                                            Cell cell42 = row2.createCell((short) 8);
                                            cell42.setCellValue(sCellSTATE);
                                            cell42.setCellStyle(my_style2);
                                            iCountTitle2 = 8;
                                            if(isHasStateTypeRequest == true)
                                            {
                                                Cell cell412 = row2.createCell((short) 9);
                                                cell412.setCellValue(sCertAttrState);
                                                cell412.setCellStyle(my_style2);

                                                Cell cell102 = row2.createCell((short) 10);
                                                cell102.setCellValue(sCellRequestType);
                                                cell102.setCellStyle(my_style2);
                                                iCountTitle2 = 10;
                                            }
                                            if(isHasCREATED_BY == true)
                                            {
                                                iCountTitle2 = iCountTitle2+1;
                                                Cell cell22 = row2.createCell((short) iCountTitle2);
                                                cell22.setCellValue(sCellUserCreate);
                                                cell22.setCellStyle(my_style2);
                                            }
                                            if(isHasBranch == true)
                                            {
                                                iCountTitle2 = iCountTitle2+1;
                                                Cell cell122 = row2.createCell((short) iCountTitle2);
                                                cell122.setCellValue(sCellAgency);
                                                cell122.setCellStyle(my_style2);
                                            }

                                            Cell cell112 = row2.createCell((short) iCountTitle2+1);
                                            cell112.setCellValue(sCellDateCreate);
                                            cell112.setCellStyle(my_style2);

                                            Cell cell132 = row2.createCell((short) iCountTitle2+2);
                                            cell132.setCellValue(sCellDateGen);
                                            cell132.setCellStyle(my_style2);

                                            Cell cell122 = row2.createCell((short) iCountTitle2+3);
                                            cell122.setCellValue(sCertSN);
                                            cell122.setCellStyle(my_style2);

                                            Cell cell152 = row2.createCell((short) iCountTitle2+4);
                                            cell152.setCellValue(sTokenSN);
                                            cell152.setCellStyle(my_style2);
                                            
                                            Cell cell153 = row2.createCell((short) iCountTitle2+5);
                                            cell153.setCellValue(sReqTokenDate);
                                            cell153.setCellStyle(my_style2);

                                            int i = 1;
                                            int k = 0;
                                            for (CERTIFICATION temp1 : rsPgin[0]) {
                                                if (k == 0) {
                                                    k = 1;
                                                } else {
                                                    k++;
                                                }
                                                String sMSTOrMSN = temp1.ENTERPRISE_ID;// EscapeUtils.CheckTextNull(temp1.TAX_CODE);
//                                                if("".equals(sMSTOrMSN)) {
//                                                    sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.BUDGET_CODE);
//                                                }
//                                                if("".equals(sMSTOrMSN)) {
//                                                    sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.DECISION);
//                                                }
                                                String sCMNDOrHC = temp1.PERSONAL_ID;// EscapeUtils.CheckTextNull(temp1.P_ID);
//                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)) {
//                                                    sMSTOrMSN = CommonReferServlet.filterPrefixUIDLanguage(temp1.ENTERPRISE_ID, loginLanguage);
//                                                    sCMNDOrHC = CommonReferServlet.filterPrefixUIDLanguage(temp1.PERSONAL_ID, loginLanguage);
//                                                }
//                                                if("".equals(sCMNDOrHC)) {
//                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_EID);
//                                                }
//                                                if("".equals(sCMNDOrHC)) {
//                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.PASSPORT);
//                                                }
                                                Row row1 = sheet2.createRow(Integer.valueOf(i));
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                my_styleBranch.setFont(fontBranch);

                                                CellStyle my_styleBranchDate = wb.createCellStyle();
                                                Font fontBranchDate = wb.createFont();
                                                fontBranchDate.setFontName("Arial");
                                                my_styleBranchDate.setFont(fontBranchDate);
                                                my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));

                                                Cell cellBranch = row1.createCell((short) 0);
                                                cellBranch.setCellValue(k);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row1.createCell((short) 1);
                                                cellBranch1.setCellValue(EscapeUtils.CheckTextNull(temp1.COMPANY_NAME));
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch2 = row1.createCell((short) 2);
                                                cellBranch2.setCellValue(sMSTOrMSN);
                                                cellBranch2.setCellStyle(styleText);

                                                Cell cellBranch3 = row1.createCell((short) 3);
                                                cellBranch3.setCellValue(EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME));
                                                cellBranch3.setCellStyle(my_styleBranch);

                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(sCMNDOrHC);
                                                cellBranch4.setCellStyle(styleText);

                                                String strDeviceUUID = "";
                                                if(temp1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                    strDeviceUUID = temp1.SERVICE_UUID;
                                                }
                                                Cell cellBranch41 = row1.createCell((short) 5);
                                                cellBranch41.setCellValue(strDeviceUUID);
                                                cellBranch41.setCellStyle(styleText);

                                                Cell cellBranch5 = row1.createCell((short) 6);
                                                cellBranch5.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PROFILE_NAME));
                                                cellBranch5.setCellStyle(my_styleBranch);

                                                Cell cellBranch6 = row1.createCell((short) 7);
                                                cellBranch6.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_PURPOSE_DESC));
                                                cellBranch6.setCellStyle(my_styleBranch);

                                                Cell cellBranch7 = row1.createCell((short) 8);
                                                cellBranch7.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_STATE_DESC));
                                                cellBranch7.setCellStyle(my_styleBranch);
                                                int iCountValue = 8;
                                                if(isHasStateTypeRequest == true) {
                                                    Cell cellBranch8 = row1.createCell((short) 9);
                                                    cellBranch8.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_STATE_DESC));
                                                    cellBranch8.setCellStyle(my_styleBranch);

                                                    Cell cellBranch9 = row1.createCell((short) 10);
                                                    cellBranch9.setCellValue(EscapeUtils.CheckTextNull(temp1.SERVICE_TYPE_DESC));
                                                    cellBranch9.setCellStyle(my_styleBranch);
                                                    iCountValue = 10;
                                                }
                                                if(isHasCREATED_BY == true) {
                                                    iCountValue = iCountValue+1;
                                                    Cell cellBranch10 = row1.createCell((short) iCountValue);
                                                    cellBranch10.setCellValue(EscapeUtils.CheckTextNull(temp1.CREATED_BY));
                                                    cellBranch10.setCellStyle(my_styleBranch);
                                                }
                                                if(isHasBranch == true) {
                                                    iCountValue = iCountValue+1;
                                                    Cell cellBranch10 = row1.createCell((short) iCountValue);
                                                    cellBranch10.setCellValue(EscapeUtils.CheckTextNull(temp1.BRANCH_DESC));
                                                    cellBranch10.setCellStyle(my_styleBranch);
                                                }
                                                Cell cellBranch12 = row1.createCell((short) iCountValue + 1);
                                                Date dateCreate = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(temp1.CREATED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                if(dateCreate != null) {
                                                    cellBranch12.setCellValue(dateCreate);
                                                } else {
                                                    cellBranch12.setCellValue("");
                                                }
                                                cellBranch12.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch14 = row1.createCell((short) iCountValue + 2);
                                                Date dateOperation = CommonFunction.convertStringToDate(temp1.OPERATED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                if(dateOperation != null) {
                                                    cellBranch14.setCellValue(dateOperation);
                                                } else {
                                                    cellBranch14.setCellValue("");
                                                }
                                                cellBranch14.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch13 = row1.createCell((short) iCountValue + 3);
                                                cellBranch13.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_SN));
                                                cellBranch13.setCellStyle(styleText);

                                                String valueTokenSN = EscapeUtils.CheckTextNull(temp1.TOKEN_SN);
                                                if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                Cell cellBranch15 = row1.createCell((short) iCountValue + 4);
                                                cellBranch15.setCellValue(valueTokenSN);
                                                cellBranch15.setCellStyle(styleText);

                                                Cell cellBranch152 = row1.createCell((short) iCountValue + 5);
                                                cellBranch152.setCellValue(temp1.CREATED_LOCK_REQUEST_DT);
                                                cellBranch152.setCellStyle(my_styleBranch);
                                                i++;
                                            }
                                        }
                                        //</editor-fold>
                                        
                                        wb.removeSheetAt(0);
                                        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                        wb.write(outByteStream);
                                        byte[] outArray = outByteStream.toByteArray();
                                        File someFile = new File(strURLPath);
                                        FileOutputStream fos = new FileOutputStream(someFile);
                                        fos.write(outArray);
                                        fos.flush();
                                        strView = "0#" + strURLPath + "#" + sFileName;
                                    } else {
                                        strView = "2#1";
                                    }
                                } else {
                                    strView = "2#1";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "exporttokenreport": {
                                //<editor-fold defaultstate="collapsed" desc="exporttokenreport">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
                                    Config conf = new Config();
                                    ConfigLanguage confLanguage = new ConfigLanguage();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_TOKEN_EXPORT + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    String ToCreateDate = (String) request.getSession(false).getAttribute("sessToCreateDateTokenReport");
                                    String FromCreateDate = (String) request.getSession(false).getAttribute("sessFromCreateDateTokenReport");
                                    String METHOD = (String) request.getSession(false).getAttribute("sessMethodTokenReport");
                                    String AGENT_ID = (String) request.getSession(false).getAttribute("sessAGENT_IDTokenReport");
                                    String pREMAINING_BEGINING_MONTH="0";
                                    String pIMPORT_IN_MONTH="0";
                                    String pTOKEN_USED_IN_MONTH="0";
                                    String pREAMINING_END_MONTH="0";
                                    if("1".equals(METHOD)) {
                                        pREMAINING_BEGINING_MONTH="1";
                                    }
                                    if("2".equals(METHOD)) {
                                        pIMPORT_IN_MONTH="1";
                                    }
                                    if("3".equals(METHOD)) {
                                        pTOKEN_USED_IN_MONTH="1";
                                    }
                                    if("4".equals(METHOD)) {
                                        pREAMINING_END_MONTH="1";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
                                        AGENT_ID = "";
                                    }
                                    CommonFunction.LogDebugString(log, "Export Token Report", "FromDate: " + FromCreateDate + "; ToDate: " + ToCreateDate);
                                    TOKEN[][] rsPgin = new TOKEN[1][];
                                    if (!"".equals(countList)) {
                                        String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                        com.S_BO_TOKEN_REPORT_LIST(AGENT_ID, EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), pREMAINING_BEGINING_MONTH,
                                            pIMPORT_IN_MONTH, pTOKEN_USED_IN_MONTH, pREAMINING_END_MONTH, Definitions.CONFIG_PAGE_SIZE_IPAGNO,
                                            Integer.parseInt(countList), Integer.parseInt(loginLanguage), rsPgin, sessTreeArrayBranchID);
                                        if(rsPgin[0].length > 0)
                                        {
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                            FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                            XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                            inputStream.close();
                                            String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellVertion = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_VERSION, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_SN, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellState = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STATUS, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellDateImportExport = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_RELEASE_AGENT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                            wb.setCompressTempFiles(true);
                                            CreationHelper createHelper = wb.getCreationHelper();
                                            SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
                                            sheet.setRandomAccessWindowSize(100);
                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                            sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_TOKEN_SN);
                                            sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_TOKEN_VERSION);
                                            sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STATE);
                                            sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
                                            sheet.setColumnWidth(5, 32 * 255);
                                            Row row = sheet.createRow(0);

                                            CellStyle my_style = wb.createCellStyle();
                                            Font font = wb.createFont();
                                            font.setBoldweight((short) 700);
                                            font.setFontName("Arial");
                                            my_style.setFont(font);
                                            my_style.setFillBackgroundColor((short) 9);
                                            my_style.setAlignment((short) 2);
                                            Cell cell = row.createCell((short) 0);
                                            cell.setCellValue(sCellSTT);
                                            cell.setCellStyle(my_style);

                                            Cell cell5 = row.createCell((short) 1);
                                            cell5.setCellValue(sCellTokenSN);
                                            cell5.setCellStyle(my_style);

                                            Cell cell3 = row.createCell((short) 2);
                                            cell3.setCellValue(sCellVertion);
                                            cell3.setCellStyle(my_style);

                                            Cell cell7 = row.createCell((short) 3);
                                            cell7.setCellValue(sCellState);
                                            cell7.setCellStyle(my_style);

                                            Cell cell6 = row.createCell((short) 4);
                                            cell6.setCellValue(sCellAgency);
                                            cell6.setCellStyle(my_style);

                                            Cell cell9 = row.createCell((short) 5);
                                            cell9.setCellValue(sCellDateImportExport);
                                            cell9.setCellStyle(my_style);
                                            int i = 1;
                                            int k = 0;
                                            for (TOKEN temp1 : rsPgin[0]) {
                                                if (k == 0) {
                                                    k = 1;
                                                } else {
                                                    k++;
                                                }
                                                Row row1 = sheet.createRow(Integer.valueOf(i));
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                Font fontBranch = wb.createFont();
                                                fontBranch.setFontName("Arial");
                                                my_styleBranch.setFont(fontBranch);
                                                
                                                CellStyle styleText = wb.createCellStyle();
                                                styleText.setFont(fontBranch);
                                                styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));
                                                
                                                CellStyle my_styleBranchDate = wb.createCellStyle();
                                                Font fontBranchDate = wb.createFont();
                                                fontBranchDate.setFontName("Arial");
                                                my_styleBranchDate.setFont(fontBranchDate);
                                                my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS));

                                                Cell cellBranch = row1.createCell((short) 0);
                                                cellBranch.setCellValue(k);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row1.createCell((short) 1);
                                                cellBranch1.setCellValue(temp1.TOKEN_SN);
                                                cellBranch1.setCellStyle(styleText);

                                                Cell cellBranch2 = row1.createCell((short) 2);
                                                cellBranch2.setCellValue(temp1.TOKEN_VERSION_DESC);
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                Cell cellBranch3 = row1.createCell((short) 3);
                                                cellBranch3.setCellValue(temp1.TOKEN_STATE_DESC);
                                                cellBranch3.setCellStyle(my_styleBranch);

                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(temp1.BRANCH_DESC);
                                                cellBranch4.setCellStyle(my_styleBranch);

                                                Cell cellBranch5 = row1.createCell((short) 5);
                                                Date dateGen = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(temp1.IMPORT_EXPORT_DT),
                                                    Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateGen != null) {
                                                    cellBranch5.setCellValue(dateGen);
                                                } else {
                                                    cellBranch5.setCellValue("");
                                                }
                                                cellBranch5.setCellStyle(my_styleBranchDate);
                                                i++;
                                            }
                                            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                            wb.write(outByteStream);
                                            byte[] outArray = outByteStream.toByteArray();
                                            File someFile = new File(strURLPath);
                                            FileOutputStream fos = new FileOutputStream(someFile);
                                            fos.write(outArray);
                                            fos.flush();
                                            strView = "0#" + strURLPath + "#" + sFileName;
                                        } else {
                                            strView = "2#1";
                                        }
                                    } else {
                                        strView = "2#1";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "exportcollationcertlist": {
                                //<editor-fold defaultstate="collapsed" desc="exportcollationcertlist">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString().trim();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                    CommonFunction clsCom = new CommonFunction();
                                    String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
                                    Config conf = new Config();
                                    ConfigLanguage confLanguage = new ConfigLanguage();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()) {
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_PDF_CONTROL + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    String ToCreateDate = (String) request.getSession(false).getAttribute("sessMonthStatusCollation");
                                    String FromCreateDate = (String) request.getSession(false).getAttribute("sessYearStatusCollation");
                                    String FromDate = (String) request.getSession(false).getAttribute("sessFromCreateDateCollation");
                                    String ToDate = (String) request.getSession(false).getAttribute("sessToCreateDateCollation");
                                    String STATUS_COLLATION = (String) request.getSession(false).getAttribute("sessStatusCollation");
                                    String idBranchOffice = (String) request.getSession(false).getAttribute("sessBranchOfficeStatusCollation");
                                    String SessUserAgentID = sessionsa.getAttribute("SessUserAgentID").toString().trim();
                                    String SessUserID = request.getSession(false).getAttribute("UserID").toString().trim();
                                    String SessRoleID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
                                    String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(STATUS_COLLATION)) {
                                        STATUS_COLLATION = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
                                        idBranchOffice = "";
                                    }
                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        SessUserID = "";
                                    } else {
                                        if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
                                            SessUserID = "";
                                        }
                                    }
                                    String strAlertAllTimes = request.getSession(false).getAttribute("AlertAllTimeCertCollation").toString().trim();
                                    if("1".equals(strAlertAllTimes)) {
                                        FromDate = "";
                                        ToDate = "";
                                    } else{
                                        ToCreateDate = "";
                                        FromCreateDate = "";
                                    }
                                    String pBRANCH_BENEFICIARY_ID = SessUserAgentID;
                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                        pBRANCH_BENEFICIARY_ID=EscapeUtils.escapeHtmlSearch(idBranchOffice);
                                    }
                                    CommonFunction.LogDebugString(log, "Export Collation Data", "Mounth: " + FromCreateDate + "; Year: " + ToCreateDate + "; STATUS_COLLATION: " + STATUS_COLLATION);
                                    if (!"".equals(countList)) {
                                        CERTIFICATION[][] rsPgin;
                                        rsPgin = new CERTIFICATION[1][];
                                        com.S_BO_CERTIFICATION_CROSS_CHECK_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(idBranchOffice),STATUS_COLLATION, SessUserID,
                                            loginLanguage, rsPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO, Integer.parseInt(countList),
                                            pBRANCH_BENEFICIARY_ID, FromDate, ToDate);
                                        if(rsPgin[0].length > 0)
                                        {
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                            FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                            XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                            inputStream.close();
                                            String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, loginLanguage).trim();
                                            String sCellApproveDate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_APPROVAL, loginLanguage).trim();
                                            String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, loginLanguage).trim();
                                            String sCellUser= confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_USER_CREATE, loginLanguage).trim();
                                            String sCellState = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STATUS, loginLanguage).trim();
                                            String sCellMST = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_ENTERPRISE, loginLanguage).trim();
                                            String sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL, loginLanguage).trim();
                                            String sCellCompany = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COMPANY_NAME, loginLanguage).trim();
                                            String sCellPersonal = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PERSONAL_NAME, loginLanguage).trim();
                                            String sCellProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICEPACK_NAME, loginLanguage).trim();
                                            String sCellRequestType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REQUEST_TYPE, loginLanguage).trim();
                                            String sCellCreateDate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CREATE, loginLanguage).trim();
                                            String sCellGenDate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_GEN, loginLanguage).trim();
                                            String sCellCertState = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_STATUS, loginLanguage).trim();
                                            String sTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_SN, loginLanguage).trim();
                                            String sExpirationDT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EXPIRATION, loginLanguage).trim();
                                            String sExpirationContractDT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EXPIRATION_CONTRACT, loginLanguage).trim();
                                            String sCellMounth = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONTH_CONTROL, loginLanguage).trim();
                                            String sCellChangeDate =confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CHANGE_CONTROLSTATUS, loginLanguage).trim();
                                            String sCellCollationState = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_STATUS, loginLanguage).trim();
                                            String sCellProfileFee = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICE_FEE, loginLanguage).trim();
                                            String sCellDateEffective = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EFFECTIVE, loginLanguage).trim();
                                            String sCellTokenQuality = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_NUMBER, loginLanguage).trim();
                                            String sCellTokenFee =confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_FEE, loginLanguage).trim();
                                            String sCellProvince = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROVINCE_NAME, loginLanguage).trim();
                                            String sCellProfileFine = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_FINE_AMOUNT, loginLanguage).trim();
                                            String sCellCollationDate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CONTROL, loginLanguage).trim();
                                            String sCellDeclineDate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CANCEL, loginLanguage).trim();
                                            String sCellDeclineNumber = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CANCEL_NUMBER, loginLanguage).trim();
                                            String sCellRevokeDate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_REVOKE, loginLanguage).trim();
                                            String sPastCertType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_OLD_REQUEST_TYPE, loginLanguage).trim();
                                            String sPastCertEffective = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_OLD_CERT_EFFECTIVE, loginLanguage).trim();
                                            String sPastProfileStatus = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_OLD_PROFILE_STATUS, loginLanguage).trim();
                                            String sPastAgentCode = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_OLD_AGENT_CODE, loginLanguage).trim();
                                            String sCertStatus = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_STATUS, loginLanguage).trim();
                                            String sAgentLevelOne = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENCY_LEVEL_ONE, loginLanguage).trim();
                                            GENERAL_POLICY[][] sessGeneralPolicy;
                                            SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                            wb.setCompressTempFiles(true);
                                            CreationHelper createHelper = wb.getCreationHelper();
                                            CellStyle my_style = wb.createCellStyle();
                                            Font font = wb.createFont();
                                            font.setBoldweight((short) 700);
                                            font.setFontName("Verdana");
                                            my_style.setFont(font);
                                            my_style.setWrapText(true);
                                            my_style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                                            my_style.setFillBackgroundColor((short) 9);
                                            my_style.setAlignment((short) 2);
                                            my_style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                                            my_style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                                            my_style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                                            my_style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                                            int i;
                                            int k;
                                            CERTIFICATION_CONTROL_REPORT[][] rsReportPgin;
                                            
                                            Font fontBranch = wb.createFont();
                                            fontBranch.setFontName("Verdana");
                                            CellStyle styleText = wb.createCellStyle();
                                            styleText.setFont(fontBranch);
                                            styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### TONG HOP NEW">
                                            SXSSFSheet sheet = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CUSTOMER_DETAIL, loginLanguage).trim());
                                            
                                            BRANCH[][] rsBranch = new BRANCH[1][];
                                            String sBranchReport = "";
                                            com.S_BO_BRANCH_DETAIL(EscapeUtils.escapeHtmlSearch(idBranchOffice), rsBranch);
                                            if(rsBranch[0].length > 0) {
                                                sBranchReport = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK);
                                            }
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - HEADER">
                                            i = 0;
                                            Row rowSyntheticTitle;
                                            rowSyntheticTitle = sheet.createRow(i);
                                            CellStyle my_styleSyntheticTitle = wb.createCellStyle();
                                            Font fontBranchSyntheticTitle = wb.createFont();
                                            fontBranchSyntheticTitle.setBoldweight((short) 700);
                                            fontBranchSyntheticTitle.setFontName("Verdana");
                                            my_styleSyntheticTitle.setFont(fontBranchSyntheticTitle);
                                            
                                            CellStyle my_styleSyntheticTitleDN = wb.createCellStyle();
                                            Font fontBranchSyntheticTitleDN = wb.createFont();
                                            fontBranchSyntheticTitleDN.setBoldweight((short) 700);
                                            fontBranchSyntheticTitleDN.setFontName("Verdana");
                                            my_styleSyntheticTitleDN.setFont(fontBranchSyntheticTitleDN);
                                            
                                            Cell cellSyntheticTitle;
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 4));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                            cellSyntheticTitle.setCellValue(conf.GetPrintPropertybyCode(Definitions.CONFIG_PROCESS_REPORT_CONTROL_COMPANY_NAME, loginLanguage));
                                            my_styleSyntheticTitleDN.setAlignment(my_styleSyntheticTitleDN.ALIGN_LEFT);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleDN);
                                            
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 6, 10));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 6);
                                            cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_NATIONAL_REGIME, request.getSession(false).getAttribute("sessVN").toString()).trim());
                                            my_styleSyntheticTitleDN.setAlignment(my_styleSyntheticTitleDN.ALIGN_CENTER);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleDN);
                                            
                                            i = i+1;
                                            rowSyntheticTitle = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 4));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                            cellSyntheticTitle.setCellValue(conf.GetPrintPropertybyCode(Definitions.CONFIG_PROCESS_REPORT_CONTROL_CA_NAME, loginLanguage));
                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_LEFT);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                            
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 6, 10));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 6);
                                            cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_NATIONAL_BANNER, request.getSession(false).getAttribute("sessVN").toString()).trim());
                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                            i = i+1;
                                            rowSyntheticTitle = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 9, 12));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 9);
                                            cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_SIGN, request.getSession(false).getAttribute("sessVN").toString()).trim());
                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                            i = i+1;
                                            rowSyntheticTitle = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 12));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                            cellSyntheticTitle.setCellValue(conf.GetPrintPropertybyCode(Definitions.CONFIG_PROCESS_REPORT_CONTROL_WITH_AGENT, loginLanguage) + " " + sBranchReport);
                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                            i = i+1;
                                            rowSyntheticTitle = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 12));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                            cellSyntheticTitle.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_MONTH, loginLanguage) + " " + ToCreateDate.trim() + "/" + FromCreateDate.trim());
                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                            //</editor-fold>
                                            
                                            i = i + 2;
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET LIST">
                                            int sumNumToken = 0;
                                            int sumRoseAmount = 0;
                                            int sumTokenAmout = 0;
                                            int sumReturnAmount = 0;
                                            CellStyle my_styleMoney = wb.createCellStyle();
                                            my_styleMoney.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_MONEY_EXCEL));
                                            my_styleMoney.setFont(fontBranch);
                                            my_styleMoney.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                                            my_styleMoney.setBorderTop(HSSFCellStyle.BORDER_THIN);
                                            my_styleMoney.setBorderRight(HSSFCellStyle.BORDER_THIN);
                                            my_styleMoney.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                                            rsReportPgin = new CERTIFICATION_CONTROL_REPORT[1][];
                                            com.S_BO_REPORT_CROSS_CHECK_DETAIL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice), SessUserID,
                                                loginLanguage, rsReportPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO,
                                                Integer.parseInt(countList), SessUserAgentID, FromDate, ToDate);
                                            if(rsReportPgin[0].length > 0) {
                                                sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                                sheet.setColumnWidth(1, 18*255);
                                                sheet.setColumnWidth(2, 18*255);
                                                sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                                sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                                sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                                sheet.setColumnWidth(6, 18*255);
                                                sheet.setColumnWidth(7, 18*255);
                                                sheet.setColumnWidth(8, 18*255);
                                                sheet.setColumnWidth(9, 22*255);
                                                sheet.setColumnWidth(10, 22*255);
//                                                sheet.setColumnWidth(11, 32*255);
                                                sheet.setColumnWidth(11, 32*255);
                                                sheet.setColumnWidth(12, 32*255);
                                                sheet.setColumnWidth(13, 40 * 255);
                                                sheet.setColumnWidth(14, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                                sheet.setColumnWidth(15, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                                sheet.setColumnWidth(16, 22 * 255);
                                                sheet.setColumnWidth(17, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                                sheet.setColumnWidth(18, 18 * 255);
                                                sheet.setColumnWidth(19, 18 * 255);
                                                sheet.setColumnWidth(20, 18 * 255);
                                                sheet.setColumnWidth(21, 18 * 255);
                                                sheet.setColumnWidth(22, 18 * 255);
                                                Row rowCusDetail = sheet.createRow(i);
                                                Cell celCus = rowCusDetail.createCell((short) 0);
                                                celCus.setCellValue(sCellSTT);
                                                celCus.setCellStyle(my_style);
                                                Cell celCus1 = rowCusDetail.createCell((short) 1);
                                                celCus1.setCellValue(sCellMounth);
                                                celCus1.setCellStyle(my_style);
                                                int iLevelImcre = 0;
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                    Cell celLevel1 = rowCusDetail.createCell((short) 2);
                                                    celLevel1.setCellValue(sAgentLevelOne);
                                                    celLevel1.setCellStyle(my_style);
                                                    iLevelImcre = 1;
                                                }
                                                Cell celCus2 = rowCusDetail.createCell((short) 2 + iLevelImcre);
                                                celCus2.setCellValue(sCellAgency);
                                                celCus2.setCellStyle(my_style);
                                                Cell celCus4 = rowCusDetail.createCell((short) 3 + iLevelImcre);
                                                celCus4.setCellValue(sCellMST);
                                                celCus4.setCellStyle(my_style);
                                                Cell celCus5 = rowCusDetail.createCell((short) 4 + iLevelImcre);
                                                celCus5.setCellValue(sCellCompany);
                                                celCus5.setCellStyle(my_style);
                                                Cell celCus9 = rowCusDetail.createCell((short) 5 + iLevelImcre);
                                                celCus9.setCellValue(sCellProfile);
                                                celCus9.setCellStyle(my_style);
                                                Cell celFeeAmount = rowCusDetail.createCell((short) 6 + iLevelImcre);
                                                celFeeAmount.setCellValue(sCellProfileFee);
                                                celFeeAmount.setCellStyle(my_style);
                                                Cell celTokenNum = rowCusDetail.createCell((short) 7 + iLevelImcre);
                                                celTokenNum.setCellValue(sCellTokenQuality);
                                                celTokenNum.setCellStyle(my_style);
                                                Cell celTokenAmount = rowCusDetail.createCell((short) 8 + iLevelImcre);
                                                celTokenAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_FEE, loginLanguage));
                                                celTokenAmount.setCellStyle(my_style);
                                                Cell celRoseAmount = rowCusDetail.createCell((short) 9 + iLevelImcre);
                                                celRoseAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_ROSE, loginLanguage));
                                                celRoseAmount.setCellStyle(my_style);
                                                Cell celSumAmount = rowCusDetail.createCell((short) 10 + iLevelImcre);
                                                celSumAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONEY_CHARGES_CA, loginLanguage));
                                                celSumAmount.setCellStyle(my_style);
//                                                Cell celDateGen = rowCusDetail.createCell((short) 11);
//                                                celDateGen.setCellValue(sCellGenDate);
//                                                celDateGen.setCellStyle(my_style);
                                                Cell celDateEffective = rowCusDetail.createCell((short) 11 + iLevelImcre);
                                                celDateEffective.setCellValue(sCellDateEffective);
                                                celDateEffective.setCellStyle(my_style);
                                                Cell celDateRevoke = rowCusDetail.createCell((short) 12 + iLevelImcre);
                                                celDateRevoke.setCellValue(sExpirationContractDT);
                                                celDateRevoke.setCellStyle(my_style);
                                                Cell celCertSN = rowCusDetail.createCell((short) 13 + iLevelImcre);
                                                celCertSN.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage));
                                                celCertSN.setCellStyle(my_style);
                                                Cell celRequestType = rowCusDetail.createCell((short) 14 + iLevelImcre);
                                                celRequestType.setCellValue(sCellRequestType);
                                                celRequestType.setCellStyle(my_style);
                                                Cell celCreateUser = rowCusDetail.createCell((short) 15 + iLevelImcre);
                                                celCreateUser.setCellValue(sCellUser);
                                                celCreateUser.setCellStyle(my_style);
                                                Cell celCus6 = rowCusDetail.createCell((short) 16 + iLevelImcre);
                                                celCus6.setCellValue(sCellCMND);
                                                celCus6.setCellStyle(my_style);
                                                Cell celCus7 = rowCusDetail.createCell((short) 17 + iLevelImcre);
                                                celCus7.setCellValue(sCellPersonal);
                                                celCus7.setCellStyle(my_style);
                                                Cell celTokenSN = rowCusDetail.createCell((short) 18 + iLevelImcre);
                                                celTokenSN.setCellValue(sTokenSN);
                                                celTokenSN.setCellStyle(my_style);
                                                Cell celPastCertType = rowCusDetail.createCell((short) 19 + iLevelImcre);
                                                celPastCertType.setCellValue(sPastCertType);
                                                celPastCertType.setCellStyle(my_style);
                                                Cell celPastCertEffective = rowCusDetail.createCell((short) 20 + iLevelImcre);
                                                celPastCertEffective.setCellValue(sPastCertEffective);
                                                celPastCertEffective.setCellStyle(my_style);
                                                Cell celPastProfileStatus = rowCusDetail.createCell((short) 21 + iLevelImcre);
                                                celPastProfileStatus.setCellValue(sPastProfileStatus);
                                                celPastProfileStatus.setCellStyle(my_style);
                                                Cell celPastAgentCode = rowCusDetail.createCell((short) 22 + iLevelImcre);
                                                celPastAgentCode.setCellValue(sPastAgentCode);
                                                celPastAgentCode.setCellStyle(my_style);
                                                Cell celCertState = rowCusDetail.createCell((short) 23 + iLevelImcre);
                                                celCertState.setCellValue(sCertStatus);
                                                celCertState.setCellStyle(my_style);
                                                
                                                i = i+1;
                                                k = 0;
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                my_styleBranch.setFont(fontBranch);
                                                CellStyle my_styleBranchBold = wb.createCellStyle();
                                                my_styleBranchBold.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_MONEY_EXCEL));
                                                Font fontBranchBold = wb.createFont();
                                                fontBranchBold.setBoldweight((short) 700);
                                                fontBranchBold.setFontName("Verdana");
                                                my_styleBranchBold.setFont(fontBranchBold);
                                                for (CERTIFICATION_CONTROL_REPORT temp1 : rsReportPgin[0]) {
                                                    sumNumToken = sumNumToken + temp1.TOKEN_NUMBER;
                                                    sumRoseAmount = sumRoseAmount + temp1.ROSE_AMOUNT;
                                                    sumTokenAmout = sumTokenAmout + temp1.TOKEN_AMOUNT;
                                                    sumReturnAmount = sumReturnAmount + temp1.RETURN_AMOUNT;
                                                    if (k == 0) {
                                                        k = 1;
                                                    } else {
                                                        k++;
                                                    }
                                                    Row row1 = sheet.createRow(i);
                                                    CellStyle my_styleBranchDate = wb.createCellStyle();
                                                    Font fontBranchDate = wb.createFont();
                                                    fontBranchDate.setFontName("Verdana");
                                                    my_styleBranchDate.setFont(fontBranchDate);
                                                    my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));
                                                    
                                                    Cell cellBranch = row1.createCell((short) 0);
                                                    cellBranch.setCellValue(k);
                                                    cellBranch.setCellStyle(my_styleBranch);
                                                    
                                                    Cell cellMounth = row1.createCell((short) 1);
                                                    cellMounth.setCellValue(temp1.CROSS_CHECK_MONTH);
                                                    cellMounth.setCellStyle(my_styleBranch);
                                                    iLevelImcre = 0;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        Cell cellLevelOne = row1.createCell((short) 2);
                                                        cellLevelOne.setCellValue(temp1.BRANCH_LEVEL_1_NAME);
                                                        cellLevelOne.setCellStyle(my_styleBranch);
                                                        iLevelImcre = 1;
                                                    }

                                                    Cell cellBranch1 = row1.createCell((short) 2 + iLevelImcre);
                                                    cellBranch1.setCellValue(temp1.BRANCH_NAME);
                                                    cellBranch1.setCellStyle(my_styleBranch);

                                                    String sTAX_CODE = temp1.ENTERPRISE_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)
                                                        || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        sTAX_CODE = CommonReferServlet.filterPrefixUIDLanguage(temp1.ENTERPRISE_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch4 = row1.createCell((short) 3 + iLevelImcre);
                                                    cellBranch4.setCellValue(sTAX_CODE);
                                                    cellBranch4.setCellStyle(styleText);

                                                    Cell cellBranch5 = row1.createCell((short) 4 + iLevelImcre);
                                                    cellBranch5.setCellValue(temp1.COMPANY_NAME);
                                                    cellBranch5.setCellStyle(my_styleBranch);

                                                    Cell cellBranch9 = row1.createCell((short) 5 + iLevelImcre);
                                                    cellBranch9.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
                                                    cellBranch9.setCellStyle(my_styleBranch);

                                                    Cell cellBranch10 = row1.createCell((short) 6 + iLevelImcre);
                                                    cellBranch10.setCellValue(temp1.FEE_AMOUNT);
                                                    cellBranch10.setCellStyle(my_styleMoney);

                                                    Cell cellBranch11 = row1.createCell((short) 7 + iLevelImcre);
                                                    cellBranch11.setCellValue(temp1.TOKEN_NUMBER);
                                                    cellBranch11.setCellStyle(my_styleBranch);

                                                    Cell cellBranch12 = row1.createCell((short) 8 + iLevelImcre);
                                                    cellBranch12.setCellValue(temp1.TOKEN_AMOUNT);
                                                    cellBranch12.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellRoseAmount = row1.createCell((short) 9 + iLevelImcre);
                                                    cellRoseAmount.setCellValue(temp1.ROSE_AMOUNT);
                                                    cellRoseAmount.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellSumAmount = row1.createCell((short) 10 + iLevelImcre);
                                                    cellSumAmount.setCellValue(temp1.RETURN_AMOUNT);
                                                    cellSumAmount.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellValueEffective = row1.createCell((short) 11 + iLevelImcre);
                                                    Date dateEffective = CommonFunction.convertStringToDate(temp1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateEffective != null) {
                                                        cellValueEffective.setCellValue(dateEffective);
                                                    } else {
                                                        cellValueEffective.setCellValue("");
                                                    }
                                                    cellValueEffective.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell cellValueRevoke = row1.createCell((short) 12 + iLevelImcre);
                                                    Date dateRevoke = CommonFunction.convertStringToDate(temp1.EXPIRATION_CONTRACT_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateRevoke != null) {
                                                        cellValueRevoke.setCellValue(dateRevoke);
                                                    } else {
                                                        cellValueRevoke.setCellValue("");
                                                    }
                                                    cellValueRevoke.setCellStyle(my_styleBranchDate);
                                                    
                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
                                                    Cell cellBranch19 = row1.createCell((short) 13 + iLevelImcre);
                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
                                                    cellBranch19.setCellStyle(styleText);

                                                    Cell cellBranch3 = row1.createCell((short) 14 + iLevelImcre);
                                                    cellBranch3.setCellValue(temp1.SERVICE_TYPE_DESC);
                                                    cellBranch3.setCellStyle(my_styleBranch);

                                                    Cell cellBranch2 = row1.createCell((short) 15 + iLevelImcre);
                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
                                                    cellBranch2.setCellStyle(my_styleBranch);

                                                    String sP_ID = temp1.PERSONAL_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)
                                                        || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        sP_ID = CommonReferServlet.filterPrefixUIDLanguage(temp1.PERSONAL_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch6 = row1.createCell((short) 16 + iLevelImcre);
                                                    cellBranch6.setCellValue(sP_ID);
                                                    cellBranch6.setCellStyle(styleText);

                                                    Cell cellBranch7 = row1.createCell((short) 17 + iLevelImcre);
                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
                                                    cellBranch7.setCellStyle(my_styleBranch);
                                                    
                                                    String valueTokenSN = temp1.TOKEN_SN;
                                                    if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                    Cell cellBranch21 = row1.createCell((short) 18 + iLevelImcre);
                                                    cellBranch21.setCellValue(valueTokenSN);
                                                    cellBranch21.setCellStyle(styleText);
                                                    
                                                    Cell valuePastCertType = row1.createCell((short) 19 + iLevelImcre);
                                                    valuePastCertType.setCellValue(temp1.PAST_SERVICE_TYPE_DESC);
                                                    valuePastCertType.setCellStyle(my_styleBranch);
                                                    
                                                    Cell valuePastCertEffective = row1.createCell((short) 20 + iLevelImcre);
                                                    Date datePastEffective = CommonFunction.convertStringToDate(temp1.PAST_EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(datePastEffective != null) {
                                                        valuePastCertEffective.setCellValue(datePastEffective);
                                                    } else {
                                                        valuePastCertEffective.setCellValue("");
                                                    }
                                                    valuePastCertEffective.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell valuePastProfileStatus = row1.createCell((short) 21 + iLevelImcre);
                                                    valuePastProfileStatus.setCellValue(temp1.PAST_FILE_MANAGER_STATE_DESC);
                                                    valuePastProfileStatus.setCellStyle(my_styleBranch);
                                                    
                                                    Cell valuePastBranchDesc = row1.createCell((short) 22 + iLevelImcre);
                                                    valuePastBranchDesc.setCellValue(temp1.PAST_BRANCH_DESC);
                                                    valuePastBranchDesc.setCellStyle(my_styleBranch);
                                                    
                                                    Cell valueCertState = row1.createCell((short) 23 + iLevelImcre);
                                                    valueCertState.setCellValue(temp1.CERTIFICATE_STATE_DESC);
                                                    valueCertState.setCellStyle(my_styleBranch);
                                                    i++;
                                                }
                                                Row rowSum = sheet.createRow(i);
                                                Cell cellBranch11 = rowSum.createCell((short) 7);
                                                cellBranch11.setCellValue(sumNumToken);
                                                cellBranch11.setCellStyle(my_styleBranchBold);

                                                Cell cellBranch12 = rowSum.createCell((short) 8);
                                                cellBranch12.setCellValue(sumTokenAmout);
                                                cellBranch12.setCellStyle(my_styleBranchBold);

                                                Cell cellRoseAmount = rowSum.createCell((short) 9);
                                                cellRoseAmount.setCellValue(sumRoseAmount);
                                                cellRoseAmount.setCellStyle(my_styleBranchBold);

                                                Cell cellSumAmount = rowSum.createCell((short) 10);
                                                cellSumAmount.setCellValue(sumReturnAmount);
                                                cellSumAmount.setCellStyle(my_styleBranchBold);
                                            }
                                            //</editor-fold>
                                            
                                            i = i + 2;
                                            int iSum = i;
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - TOKEN">
                                            CellStyle my_styleBranch = wb.createCellStyle();
                                            my_styleBranch.setFont(fontBranch);
                                            my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                                            my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
                                            my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
                                            my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                                            CellStyle my_styleBranchBold = wb.createCellStyle();
                                            Font fontBranchBold = wb.createFont();
                                            fontBranchBold.setFontName("Verdana");
                                            fontBranchBold.setBoldweight((short) 700);
                                            my_styleBranchBold.setFont(fontBranchBold);
                                            my_styleBranchBold.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                                            my_styleBranchBold.setBorderTop(HSSFCellStyle.BORDER_THIN);
                                            my_styleBranchBold.setBorderRight(HSSFCellStyle.BORDER_THIN);
                                            my_styleBranchBold.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                                            
                                            Row row1 = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
                                            Cell cellBranch001 = row1.createCell((short) 0);
                                            cellBranch001.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_ROSE_RATE, loginLanguage));
                                            cellBranch001.setCellStyle(my_styleBranchBold);
                                            Cell cellBranch002 = row1.createCell((short) 1);
                                            cellBranch002.setCellStyle(my_styleBranch);
                                            Cell cellBranch003 = row1.createCell((short) 2);
                                            cellBranch003.setCellStyle(my_styleBranch);
                                            Cell cellBranch004 = row1.createCell((short) 3);
                                            cellBranch004.setCellStyle(my_styleBranch);
                                            
                                            i = i + 1;
                                            Row row2 = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
                                            Cell cellBranch011 = row2.createCell((short) 0);
                                            cellBranch011.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_PRICE, loginLanguage));
                                            cellBranch011.setCellStyle(my_styleBranchBold);
                                            Cell cellBranch012 = row2.createCell((short) 1);
                                            cellBranch012.setCellStyle(my_styleBranch);
                                            Cell cellBranch013 = row2.createCell((short) 2);
                                            cellBranch013.setCellStyle(my_styleBranch);
                                            Cell cellBranch014 = row2.createCell((short) 3);
                                            cellBranch014.setCellStyle(my_styleBranch);
                                            i=i+1;
                                            Row row3 = sheet.createRow(i);
                                            
                                            i = i + 1;
                                            Row row4 = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
                                            Cell cellBranch11 = row4.createCell((short) 0);
                                            cellBranch11.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_INVENTORY_BEGIN, loginLanguage));
                                            cellBranch11.setCellStyle(my_styleBranch);
                                            Cell cellBranch12 = row4.createCell((short) 1);
                                            cellBranch12.setCellStyle(my_styleBranch);
                                            Cell cellBranch13 = row4.createCell((short) 2);
                                            cellBranch13.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch14 = row4.createCell((short) 3);
                                            cellBranch14.setCellStyle(my_styleBranch);
                                            ///
                                            i=i+1;
                                            Row row5 = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
                                            Cell cellBranch21 = row5.createCell((short) 0);
                                            cellBranch21.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_TOTAL_IMPORT_PERIOD, loginLanguage));
                                            cellBranch21.setCellStyle(my_styleBranch);
                                            Cell cellBranch211 = row5.createCell((short) 1);
                                            cellBranch211.setCellStyle(my_styleBranch);
                                            Cell cellBranch212 = row5.createCell((short) 2);
                                            cellBranch212.setCellStyle(my_styleBranch);

                                            Cell cellBranch22 = row5.createCell((short) 3);
                                            cellBranch22.setCellValue("");
                                            cellBranch22.setCellStyle(my_styleBranch);
                                            ///
                                            i=i+1;
                                            Row row6 = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
                                            Cell cellBranch31 = row6.createCell((short) 0);
                                            cellBranch31.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_TOTAL_INCURRED_PERIOD, loginLanguage));
                                            cellBranch31.setCellStyle(my_styleBranch);
                                            Cell cellBranch311 = row6.createCell((short) 1);
                                            cellBranch311.setCellStyle(my_styleBranch);
                                            Cell cellBranch312 = row6.createCell((short) 2);
                                            cellBranch312.setCellStyle(my_styleBranch);

                                            Cell cellBranch302 = row6.createCell((short) 3);
                                            cellBranch302.setCellValue("");
                                            cellBranch302.setCellStyle(my_styleBranch);
                                            ///
                                            i=i+1;
                                            Row row7 = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
                                            Cell cellBranch41 = row7.createCell((short) 0);
                                            cellBranch41.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_INVENTORY_END, loginLanguage));
                                            cellBranch41.setCellStyle(my_styleBranch);
                                            Cell cellBranch411 = row7.createCell((short) 1);
                                            cellBranch411.setCellStyle(my_styleBranch);
                                            Cell cellBranch412 = row7.createCell((short) 2);
                                            cellBranch412.setCellStyle(my_styleBranch);

                                            Cell cellBranch42 = row7.createCell((short) 3);
                                            cellBranch42.setCellValue("");
                                            cellBranch42.setCellStyle(my_styleBranch);
                                            i=i+1;
                                            Row row8 = sheet.createRow(i);
                                            i=i+1;
                                            Row row9 = sheet.createRow(i);
                                            i=i+1;
                                            Row row10 = sheet.createRow(i);
                                            i=i+1;
                                            Row row11 = sheet.createRow(i);
                                            i=i+1;
                                            Row row12 = sheet.createRow(i);
                                            i=i+1;
                                            Row row13 = sheet.createRow(i);
                                            i=i+1;
                                            Row row14 = sheet.createRow(i);
                                            i=i+1;
                                            Row row15 = sheet.createRow(i);
                                            i=i+1;
                                            Row row16 = sheet.createRow(i);
                                            i=i+1;
                                            Row row17 = sheet.createRow(i);
                                            //</editor-fold>
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - SUM">
                                            
                                            int iSumAllMoneyStart = 0;
                                            int iSumAllTokenStart = 0;
                                            int iSumBonus = 0;
                                            int iSumBeforeVAT = 0;
                                            int iSumVAT = 0;
                                            int iSumAfterVAT = 0;
                                            
                                            iSumAllMoneyStart = iSum+1;
//                                            Row rowProfile01 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile0l = row1.createCell((short) 5);
                                            cellProfile0l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONEY_RETURN_CA, loginLanguage));
                                            cellProfile0l.setCellStyle(my_styleBranch);
                                            Cell cellProfile02 = row1.createCell((short) 6);
                                            cellProfile02.setCellStyle(my_styleBranch);
                                            Cell cellProfile03 = row1.createCell((short) 7);
                                            cellProfile03.setCellStyle(my_styleBranch);
                                            Cell cellProfile04 = row1.createCell((short) 8);
                                            cellProfile04.setCellStyle(my_styleBranch);
                                            Cell cellProfile05 = row1.createCell((short) 9);
//                                            cellProfile05.setCellValue(clsCom.convertMoneyAnotherZero(sumReturnAmount));
                                            cellProfile05.setCellValue(sumReturnAmount);
//                                            cellProfile05.setCellStyle(my_styleMoney);
                                            cellProfile05.setCellStyle(my_styleMoney);
                                            //
                                            iSum = iSum + 1;
                                            iSumAllTokenStart = iSum+1;
//                                            Row rowProfile11 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile1l = row2.createCell((short) 5);
                                            cellProfile1l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_ISSUED_PERIOD, loginLanguage));
                                            cellProfile1l.setCellStyle(my_styleBranch);
                                            Cell cellProfile12 = row2.createCell((short) 6);
                                            cellProfile12.setCellStyle(my_styleBranch);
                                            Cell cellProfile13 = row2.createCell((short) 7);
                                            cellProfile13.setCellStyle(my_styleBranch);
                                            Cell cellProfile14 = row2.createCell((short) 8);
                                            cellProfile14.setCellStyle(my_styleBranch);
                                            Cell cellProfile15 = row2.createCell((short) 9);
                                            cellProfile15.setCellValue(sumTokenAmout);
                                            cellProfile15.setCellStyle(my_styleMoney);
                                            //
                                            iSum = iSum + 1;
                                            iSumBonus = iSum+1;
//                                            Row rowProfile21 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile2l = row3.createCell((short) 5);
                                            cellProfile2l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_BONUS_PERIOD, loginLanguage));
                                            cellProfile2l.setCellStyle(my_styleBranch);
                                            Cell cellProfile22 = row3.createCell((short) 6);
                                            cellProfile22.setCellStyle(my_styleBranch);
                                            Cell cellProfile23 = row3.createCell((short) 7);
                                            cellProfile23.setCellStyle(my_styleBranch);
                                            Cell cellProfile24 = row3.createCell((short) 8);
                                            cellProfile24.setCellStyle(my_styleBranch);
                                            Cell cellProfile25 = row3.createCell((short) 9);
                                            cellProfile25.setCellValue(0);
                                            cellProfile25.setCellStyle(my_styleMoney);
                                            //
                                            iSum = iSum + 1;
                                            iSumBeforeVAT = iSum+1;
//                                            Row rowProfile31 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile3l = row4.createCell((short) 5);
                                            cellProfile3l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_BEFORE_VAT, loginLanguage));
                                            cellProfile3l.setCellStyle(my_styleBranchBold);
                                            Cell cellProfile32 = row4.createCell((short) 6);
                                            cellProfile32.setCellStyle(my_styleBranch);
                                            Cell cellProfile33 = row4.createCell((short) 7);
                                            cellProfile33.setCellStyle(my_styleBranch);
                                            Cell cellProfile34 = row4.createCell((short) 8);
                                            cellProfile34.setCellStyle(my_styleBranch);
                                            Cell cellProfile35 = row4.createCell((short) 9);
                                            String sFormulaBeforeVAT = "SUM(J"+iSumAllMoneyStart+":J"+iSumAllTokenStart+":J"+iSumBonus+")";
                                            cellProfile35.setCellFormula(sFormulaBeforeVAT);
                                            int vStartBeforeVAT = sumReturnAmount + sumTokenAmout + 0;
                                            cellProfile35.setCellValue(vStartBeforeVAT);
                                            cellProfile35.setCellStyle(my_styleMoney);
                                            //
                                            iSum = iSum + 1;
                                            iSumVAT = iSum+1;
//                                            Row rowProfile41 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile4l = row5.createCell((short) 5);
                                            cellProfile4l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_VAT, loginLanguage));
                                            cellProfile4l.setCellStyle(my_styleBranch);
                                            Cell cellProfile42 = row5.createCell((short) 6);
                                            cellProfile42.setCellStyle(my_styleBranch);
                                            Cell cellProfile43 = row5.createCell((short) 7);
                                            cellProfile43.setCellStyle(my_styleBranch);
                                            Cell cellProfile44 = row5.createCell((short) 8);
                                            cellProfile44.setCellStyle(my_styleBranch);
                                            Cell cellProfile45 = row5.createCell((short) 9);
                                            String sFormulaVAT = "J"+iSumBeforeVAT+"/10";
                                            cellProfile45.setCellFormula(sFormulaVAT);
                                            int vStartVAT = vStartBeforeVAT/10;
                                            cellProfile45.setCellValue(vStartVAT);
                                            cellProfile45.setCellStyle(my_styleMoney);
                                            //
                                            iSum = iSum + 1;
                                            iSumAfterVAT = iSum+1;
//                                            Row rowProfile51 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile5l = row6.createCell((short) 5);
                                            cellProfile5l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_AFTER_VAT, loginLanguage));
                                            cellProfile5l.setCellStyle(my_styleBranchBold);
                                            Cell cellProfile52 = row6.createCell((short) 6);
                                            cellProfile52.setCellStyle(my_styleBranch);
                                            Cell cellProfile53 = row6.createCell((short) 7);
                                            cellProfile53.setCellStyle(my_styleBranch);
                                            Cell cellProfile54 = row6.createCell((short) 8);
                                            cellProfile54.setCellStyle(my_styleBranch);
                                            Cell cellProfile55 = row6.createCell((short) 9);
                                            String sFormulaAfterVAT = "SUM(J"+iSumBeforeVAT+":J"+iSumVAT+")";
                                            cellProfile55.setCellFormula(sFormulaAfterVAT);
                                            int vStartAfterVAT = vStartBeforeVAT+vStartVAT;
                                            cellProfile55.setCellValue(vStartAfterVAT);
                                            cellProfile55.setCellStyle(my_styleMoney);
                                            //
                                            iSum = iSum + 1;
                                            int iSumCustody = iSum+1;
//                                            Row rowProfile61 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile6l = row7.createCell((short) 5);
                                            cellProfile6l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_PROFILE_KEEP_MISSING, loginLanguage));
                                            cellProfile6l.setCellStyle(my_styleBranch);
                                            Cell cellProfile62 = row7.createCell((short) 6);
                                            cellProfile62.setCellStyle(my_styleBranch);
                                            Cell cellProfile63 = row7.createCell((short) 7);
                                            cellProfile63.setCellStyle(my_styleBranch);
                                            Cell cellProfile64 = row7.createCell((short) 8);
                                            cellProfile64.setCellStyle(my_styleBranch);
                                            Cell cellProfile65 = row7.createCell((short) 9);
                                            cellProfile65.setCellValue(0);
                                            cellProfile65.setCellStyle(my_styleMoney);
                                            //
                                            iSum = iSum + 1;
                                            int iSumNotPaid = iSum+1;
//                                            Row rowProfile71 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile7l = row8.createCell((short) 5);
                                            cellProfile7l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_NOT_CHARGE_BEFORE, loginLanguage));
                                            cellProfile7l.setCellStyle(my_styleBranch);
                                            Cell cellProfile72 = row8.createCell((short) 6);
                                            cellProfile72.setCellStyle(my_styleBranch);
                                            Cell cellProfile73 = row8.createCell((short) 7);
                                            cellProfile73.setCellStyle(my_styleBranch);
                                            Cell cellProfile74 = row8.createCell((short) 8);
                                            cellProfile74.setCellStyle(my_styleBranch);
                                            Cell cellProfile75 = row8.createCell((short) 9);
                                            cellProfile75.setCellValue(0);
                                            cellProfile75.setCellStyle(my_styleMoney);
                                            //
                                            iSum = iSum + 1;
                                            int iSumAllMoney = iSum+1;
//                                            Row rowProfile81 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile8l = row9.createCell((short) 5);
                                            cellProfile8l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_TOTAL_RETURN, loginLanguage));
                                            cellProfile8l.setCellStyle(my_styleBranchBold);
                                            Cell cellProfile82 = row9.createCell((short) 6);
                                            cellProfile82.setCellStyle(my_styleBranch);
                                            Cell cellProfile83 = row9.createCell((short) 7);
                                            cellProfile83.setCellStyle(my_styleBranch);
                                            Cell cellProfile84 = row9.createCell((short) 8);
                                            cellProfile84.setCellStyle(my_styleBranch);
                                            Cell cellProfile85 = row9.createCell((short) 9);
                                            String sFormulaAllMoney = "SUM(J"+iSumAfterVAT+":J"+iSumCustody+":J"+iSumNotPaid+")";
                                            cellProfile85.setCellFormula(sFormulaAllMoney);
                                            int vStartAllMoney = vStartAfterVAT+0+0;
                                            cellProfile85.setCellValue(vStartAllMoney);
                                            cellProfile85.setCellStyle(my_styleMoney);
                                            //
                                            iSum = iSum + 1;
                                            int iSumAdvance = iSum+1;
//                                            Row rowProfile91 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile9l = row10.createCell((short) 5);
                                            cellProfile9l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_ADVANCE_PERIOD, loginLanguage));
                                            cellProfile9l.setCellStyle(my_styleBranch);
                                            Cell cellProfile92 = row10.createCell((short) 6);
                                            cellProfile92.setCellStyle(my_styleBranch);
                                            Cell cellProfile93 = row10.createCell((short) 7);
                                            cellProfile93.setCellStyle(my_styleBranch);
                                            Cell cellProfile94 = row10.createCell((short) 8);
                                            cellProfile94.setCellStyle(my_styleBranch);
                                            Cell cellProfile95 = row10.createCell((short) 9);
                                            cellProfile95.setCellValue(0);
                                            cellProfile95.setCellStyle(my_styleMoney);
                                            iSum = iSum + 1;
                                            int iSumDeposit = iSum+1;
//                                            Row rowProfile101 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile10l = row11.createCell((short) 5);
                                            cellProfile10l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_DEPOSIT_DEDUCT_PERIOD, loginLanguage));
                                            cellProfile10l.setCellStyle(my_styleBranch);
                                            Cell cellProfile102 = row11.createCell((short) 6);
                                            cellProfile102.setCellStyle(my_styleBranch);
                                            Cell cellProfile103 = row11.createCell((short) 7);
                                            cellProfile103.setCellStyle(my_styleBranch);
                                            Cell cellProfile104 = row11.createCell((short) 8);
                                            cellProfile104.setCellStyle(my_styleBranch);
                                            Cell cellProfile105 = row11.createCell((short) 9);
                                            cellProfile105.setCellValue(0);
                                            cellProfile105.setCellStyle(my_styleMoney);
                                            iSum = iSum + 1;
                                            int iSumBePaid = iSum+1;
//                                            Row rowProfile111 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile11l = row12.createCell((short) 5);
                                            cellProfile11l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_FINE_RETURN_PERIOD, loginLanguage));
                                            cellProfile11l.setCellStyle(my_styleBranch);
                                            Cell cellProfile112 = row12.createCell((short) 6);
                                            cellProfile112.setCellStyle(my_styleBranch);
                                            Cell cellProfile113 = row12.createCell((short) 7);
                                            cellProfile113.setCellStyle(my_styleBranch);
                                            Cell cellProfile114 = row12.createCell((short) 8);
                                            cellProfile114.setCellStyle(my_styleBranch);
                                            Cell cellProfile115 = row12.createCell((short) 9);
                                            cellProfile115.setCellValue(0);
                                            cellProfile115.setCellStyle(my_styleMoney);//
                                            iSum = iSum + 1;
                                            int iSumAllReduce = iSum+1;
//                                            Row rowProfile121 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile12l = row13.createCell((short) 5);
                                            cellProfile12l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOTAL_DISCOUNTING, loginLanguage));
                                            cellProfile12l.setCellStyle(my_styleBranchBold);
                                            Cell cellProfile122 = row13.createCell((short) 6);
                                            cellProfile122.setCellStyle(my_styleBranch);
                                            Cell cellProfile123 = row13.createCell((short) 7);
                                            cellProfile123.setCellStyle(my_styleBranch);
                                            Cell cellProfile124 = row13.createCell((short) 8);
                                            cellProfile124.setCellStyle(my_styleBranch);
                                            Cell cellProfile125 = row13.createCell((short) 9);
                                            String sFormulaAllReduce = "SUM(J"+iSumAdvance+":J"+iSumDeposit+":J"+iSumBePaid+")";
                                            cellProfile125.setCellFormula(sFormulaAllReduce);
                                            int vStartAllReduce = 0+0+0;
                                            cellProfile125.setCellValue(vStartAllReduce);
                                            cellProfile125.setCellStyle(my_styleMoney);
                                            //
                                            iSum = iSum + 1;
//                                            Row rowProfile131 = sheet.createRow(iSum);
                                            sheet.addMergedRegion(new CellRangeAddress(iSum, iSum, 5, 8));
                                            Cell cellProfile13l = row14.createCell((short) 5);
                                            cellProfile13l.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_PAYABLE_END, loginLanguage));
                                            cellProfile13l.setCellStyle(my_styleBranchBold);
                                            Cell cellProfile132 = row14.createCell((short) 6);
                                            cellProfile132.setCellStyle(my_styleBranch);
                                            Cell cellProfile133 = row14.createCell((short) 7);
                                            cellProfile133.setCellStyle(my_styleBranch);
                                            Cell cellProfile134 = row14.createCell((short) 8);
                                            cellProfile134.setCellStyle(my_styleBranch);
                                            Cell cellProfile135 = row14.createCell((short) 9);
                                            String sFormulaSumAll = "J"+iSumAllMoney+"-J"+iSumAllReduce+"";
                                            cellProfile135.setCellFormula(sFormulaSumAll);
                                            int vStartSumAll = vStartAllMoney - vStartAllReduce;
                                            cellProfile135.setCellValue(vStartSumAll);
                                            cellProfile135.setCellStyle(my_styleMoney);
                                            //</editor-fold>
                                            //</editor-fold>
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET ALL Customers">
                                            rsReportPgin = new CERTIFICATION_CONTROL_REPORT[1][];
                                            com.S_BO_REPORT_TOTAL_CROSS_CHECK_DETAIL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice), SessUserID,
                                                loginLanguage, rsReportPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO,
                                                Integer.parseInt(countList), SessUserAgentID, FromDate, ToDate);
                                            if(rsReportPgin[0].length > 0)
                                            {
                                                SXSSFSheet sheetAll = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CUSTOMER_ALL, loginLanguage));
                                                sheetAll.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                                sheetAll.setColumnWidth(1, 18*255);
                                                sheetAll.setColumnWidth(2, 18*255);
                                                sheetAll.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                                sheetAll.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                                sheetAll.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                                sheetAll.setColumnWidth(6, 18*255);
                                                sheetAll.setColumnWidth(7, 18*255);
                                                sheetAll.setColumnWidth(8, 18*255);
                                                sheetAll.setColumnWidth(9, 22*255);
                                                sheetAll.setColumnWidth(10, 22*255);
                                                sheetAll.setColumnWidth(11, 32*255);
                                                sheetAll.setColumnWidth(12, 32*255);
                                                sheetAll.setColumnWidth(13, 40 * 255);
                                                sheetAll.setColumnWidth(14, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                                sheetAll.setColumnWidth(15, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                                sheetAll.setColumnWidth(16, 22 * 255);
                                                sheetAll.setColumnWidth(17, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                                sheetAll.setColumnWidth(18, 18 * 255);
                                                sheetAll.setColumnWidth(19, 18 * 255);
                                                sheetAll.setColumnWidth(20, 18 * 255);
                                                sheetAll.setColumnWidth(21, 18 * 255);
                                                sheetAll.setColumnWidth(22, 18 * 255);
                                                Row rowCusDetail = sheetAll.createRow(0);
                                                Cell celCus = rowCusDetail.createCell((short) 0);
                                                celCus.setCellValue(sCellSTT);
                                                celCus.setCellStyle(my_style);
                                                Cell celCus1 = rowCusDetail.createCell((short) 1);
                                                celCus1.setCellValue(sCellMounth);
                                                celCus1.setCellStyle(my_style);
                                                int iLevelImcre = 0;
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                    Cell celCus2 = rowCusDetail.createCell((short) 2);
                                                    celCus2.setCellValue(sAgentLevelOne);
                                                    celCus2.setCellStyle(my_style);
                                                    iLevelImcre = 1;
                                                }
                                                Cell celLevel1 = rowCusDetail.createCell((short) 2 +iLevelImcre);
                                                celLevel1.setCellValue(sCellAgency);
                                                celLevel1.setCellStyle(my_style);
                                                Cell celCus4 = rowCusDetail.createCell((short) 3 +iLevelImcre);
                                                celCus4.setCellValue(sCellMST);
                                                celCus4.setCellStyle(my_style);
                                                Cell celCus5 = rowCusDetail.createCell((short) 4 +iLevelImcre);
                                                celCus5.setCellValue(sCellCompany);
                                                celCus5.setCellStyle(my_style);
                                                Cell celCus9 = rowCusDetail.createCell((short) 5 +iLevelImcre);
                                                celCus9.setCellValue(sCellProfile);
                                                celCus9.setCellStyle(my_style);
                                                Cell celFeeAmount = rowCusDetail.createCell((short) 6 +iLevelImcre);
                                                celFeeAmount.setCellValue(sCellProfileFee);
                                                celFeeAmount.setCellStyle(my_style);
                                                Cell celTokenNum = rowCusDetail.createCell((short) 7 +iLevelImcre);
                                                celTokenNum.setCellValue(sCellTokenQuality);
                                                celTokenNum.setCellStyle(my_style);
                                                Cell celTokenAmount = rowCusDetail.createCell((short) 8 +iLevelImcre);
                                                celTokenAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_FEE, loginLanguage));
                                                celTokenAmount.setCellStyle(my_style);
                                                Cell celRoseAmount = rowCusDetail.createCell((short) 9 +iLevelImcre);
                                                celRoseAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_ROSE, loginLanguage));
                                                celRoseAmount.setCellStyle(my_style);
                                                Cell celSumAmount = rowCusDetail.createCell((short) 10 +iLevelImcre);
                                                celSumAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONEY_CHARGES_CA, loginLanguage));
                                                celSumAmount.setCellStyle(my_style);
                                                Cell celDateEffective = rowCusDetail.createCell((short) 11 +iLevelImcre);
                                                celDateEffective.setCellValue(sCellDateEffective);
                                                celDateEffective.setCellStyle(my_style);
                                                Cell celDateRevoke = rowCusDetail.createCell((short) 12 +iLevelImcre);
                                                celDateRevoke.setCellValue(sExpirationContractDT);
                                                celDateRevoke.setCellStyle(my_style);
                                                Cell celCertSN = rowCusDetail.createCell((short) 13 +iLevelImcre);
                                                celCertSN.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage));
                                                celCertSN.setCellStyle(my_style);
                                                Cell celRequestType = rowCusDetail.createCell((short) 14 +iLevelImcre);
                                                celRequestType.setCellValue(sCellRequestType);
                                                celRequestType.setCellStyle(my_style);
                                                Cell celCreateUser = rowCusDetail.createCell((short) 15 +iLevelImcre);
                                                celCreateUser.setCellValue(sCellUser);
                                                celCreateUser.setCellStyle(my_style);
                                                Cell celCus6 = rowCusDetail.createCell((short) 16 +iLevelImcre);
                                                celCus6.setCellValue(sCellCMND);
                                                celCus6.setCellStyle(my_style);
                                                Cell celCus7 = rowCusDetail.createCell((short) 17 +iLevelImcre);
                                                celCus7.setCellValue(sCellPersonal);
                                                celCus7.setCellStyle(my_style);
                                                Cell celTokenSN = rowCusDetail.createCell((short) 18 +iLevelImcre);
                                                celTokenSN.setCellValue(sTokenSN);
                                                celTokenSN.setCellStyle(my_style);
                                                Cell celPastCertType = rowCusDetail.createCell((short) 19 +iLevelImcre);
                                                celPastCertType.setCellValue(sPastCertType);
                                                celPastCertType.setCellStyle(my_style);
                                                Cell celPastCertEffective = rowCusDetail.createCell((short) 20 +iLevelImcre);
                                                celPastCertEffective.setCellValue(sPastCertEffective);
                                                celPastCertEffective.setCellStyle(my_style);
                                                
                                                Cell celPastProfileStatus = rowCusDetail.createCell((short) 21 +iLevelImcre);
                                                celPastProfileStatus.setCellValue(sPastProfileStatus);
                                                celPastProfileStatus.setCellStyle(my_style);
                                                
                                                Cell celPastAgentCode = rowCusDetail.createCell((short) 22 +iLevelImcre);
                                                celPastAgentCode.setCellValue(sPastAgentCode);
                                                celPastAgentCode.setCellStyle(my_style);
                                                
                                                Cell celCertState = rowCusDetail.createCell((short) 23 +iLevelImcre);
                                                celCertState.setCellValue(sCertStatus);
                                                celCertState.setCellStyle(my_style);
                                                
                                                i = 1;
                                                k = 0;
                                                for (CERTIFICATION_CONTROL_REPORT temp1 : rsReportPgin[0]) {
                                                    sumNumToken = sumNumToken + temp1.TOKEN_NUMBER;
                                                    sumRoseAmount = sumRoseAmount + temp1.ROSE_AMOUNT;
                                                    sumTokenAmout = sumTokenAmout + temp1.TOKEN_AMOUNT;
                                                    sumReturnAmount = sumReturnAmount + temp1.RETURN_AMOUNT;
                                                    if (k == 0) {
                                                        k = 1;
                                                    } else {
                                                        k++;
                                                    }
                                                    Row rowAll = sheetAll.createRow(i);
                                                    CellStyle my_styleBranchDate = wb.createCellStyle();
                                                    Font fontBranchDate = wb.createFont();
                                                    fontBranchDate.setFontName("Verdana");
                                                    my_styleBranchDate.setFont(fontBranchDate);
                                                    my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));
                                                    
                                                    Cell cellBranch = rowAll.createCell((short) 0);
                                                    cellBranch.setCellValue(k);
                                                    cellBranch.setCellStyle(my_styleBranch);
                                                    
                                                    Cell cellMounth = rowAll.createCell((short) 1);
                                                    cellMounth.setCellValue(temp1.CROSS_CHECK_MONTH);
                                                    cellMounth.setCellStyle(my_styleBranch);
                                                    iLevelImcre = 0;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        Cell cellBranch1 = rowAll.createCell((short) 2);
                                                        cellBranch1.setCellValue(temp1.BRANCH_LEVEL_1_NAME);
                                                        cellBranch1.setCellStyle(my_styleBranch);
                                                        iLevelImcre = 1;
                                                    }
                                                    
                                                    Cell cellLevel1 = rowAll.createCell((short) 2+iLevelImcre);
                                                    cellLevel1.setCellValue(temp1.BRANCH_NAME);
                                                    cellLevel1.setCellStyle(my_styleBranch);

                                                    String sTAX_CODE = temp1.ENTERPRISE_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC) ||
                                                        isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        sTAX_CODE = CommonReferServlet.filterPrefixUIDLanguage(temp1.ENTERPRISE_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch4 = rowAll.createCell((short) 3+iLevelImcre);
                                                    cellBranch4.setCellValue(sTAX_CODE);
                                                    cellBranch4.setCellStyle(styleText);

                                                    Cell cellBranch5 = rowAll.createCell((short) 4+iLevelImcre);
                                                    cellBranch5.setCellValue(temp1.COMPANY_NAME);
                                                    cellBranch5.setCellStyle(my_styleBranch);

                                                    Cell cellBranch9 = rowAll.createCell((short) 5+iLevelImcre);
                                                    cellBranch9.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
                                                    cellBranch9.setCellStyle(my_styleBranch);

                                                    Cell cellBranch10 = rowAll.createCell((short) 6+iLevelImcre);
                                                    cellBranch10.setCellValue(temp1.FEE_AMOUNT);
                                                    cellBranch10.setCellStyle(my_styleMoney);

                                                    Cell cellBranch11b = rowAll.createCell((short) 7+iLevelImcre);
                                                    cellBranch11b.setCellValue(temp1.TOKEN_NUMBER);
                                                    cellBranch11b.setCellStyle(my_styleBranch);

                                                    Cell cellBranch12b = rowAll.createCell((short) 8+iLevelImcre);
                                                    cellBranch12b.setCellValue(temp1.TOKEN_AMOUNT);
                                                    cellBranch12b.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellRoseAmount = rowAll.createCell((short) 9+iLevelImcre);
                                                    cellRoseAmount.setCellValue(temp1.ROSE_AMOUNT);
                                                    cellRoseAmount.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellSumAmount = rowAll.createCell((short) 10+iLevelImcre);
                                                    cellSumAmount.setCellValue(temp1.RETURN_AMOUNT);
                                                    cellSumAmount.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellValueEffective = rowAll.createCell((short) 11+iLevelImcre);
                                                    Date dateEffective = CommonFunction.convertStringToDate(temp1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateEffective != null) {
                                                        cellValueEffective.setCellValue(dateEffective);
                                                    } else {
                                                        cellValueEffective.setCellValue("");
                                                    }
                                                    cellValueEffective.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell cellValueRevoke = rowAll.createCell((short) 12+iLevelImcre);
                                                    Date dateRevoke = CommonFunction.convertStringToDate(temp1.EXPIRATION_CONTRACT_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateRevoke != null) {
                                                        cellValueRevoke.setCellValue(dateRevoke);
                                                    } else {
                                                        cellValueRevoke.setCellValue("");
                                                    }
                                                    cellValueRevoke.setCellStyle(my_styleBranchDate);
                                                    
                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
                                                    Cell cellBranch19 = rowAll.createCell((short) 13+iLevelImcre);
                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
                                                    cellBranch19.setCellStyle(styleText);

                                                    Cell cellBranch3 = rowAll.createCell((short) 14+iLevelImcre);
                                                    cellBranch3.setCellValue(temp1.SERVICE_TYPE_DESC);
                                                    cellBranch3.setCellStyle(my_styleBranch);

                                                    Cell cellBranch2 = rowAll.createCell((short) 15+iLevelImcre);
                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
                                                    cellBranch2.setCellStyle(my_styleBranch);

                                                    String sP_ID = temp1.PERSONAL_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)
                                                        || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        sP_ID = CommonReferServlet.filterPrefixUIDLanguage(temp1.PERSONAL_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch6 = rowAll.createCell((short) 16+iLevelImcre);
                                                    cellBranch6.setCellValue(sP_ID);
                                                    cellBranch6.setCellStyle(styleText);

                                                    Cell cellBranch7 = rowAll.createCell((short) 17+iLevelImcre);
                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
                                                    cellBranch7.setCellStyle(my_styleBranch);
                                                    
                                                    String valueTokenSN = temp1.TOKEN_SN;
                                                    if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                    Cell cellBranch21b = rowAll.createCell((short) 18+iLevelImcre);
                                                    cellBranch21b.setCellValue(valueTokenSN);
                                                    cellBranch21b.setCellStyle(styleText);
                                                    
                                                    Cell valuePastCertType = rowAll.createCell((short) 19+iLevelImcre);
                                                    valuePastCertType.setCellValue(temp1.PAST_SERVICE_TYPE_DESC);
                                                    valuePastCertType.setCellStyle(my_styleBranch);
                                                    
                                                    Cell valuePastCertEffective = rowAll.createCell((short) 20+iLevelImcre);
                                                    Date datePastEffective = CommonFunction.convertStringToDate(temp1.PAST_EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(datePastEffective != null) {
                                                        valuePastCertEffective.setCellValue(datePastEffective);
                                                    } else {
                                                        valuePastCertEffective.setCellValue("");
                                                    }
                                                    valuePastCertEffective.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell valuePastProfileStatus = rowAll.createCell((short) 21+iLevelImcre);
                                                    valuePastProfileStatus.setCellValue(temp1.PAST_FILE_MANAGER_STATE_DESC);
                                                    valuePastProfileStatus.setCellStyle(my_styleBranch);
                                                    
                                                    Cell valuePastAgentCode = rowAll.createCell((short) 22+iLevelImcre);
                                                    valuePastAgentCode.setCellValue(temp1.PAST_BRANCH_DESC);
                                                    valuePastAgentCode.setCellStyle(my_styleBranch);
                                                    
                                                    Cell valueCertState = rowAll.createCell((short) 23+iLevelImcre);
                                                    valueCertState.setCellValue(temp1.CERTIFICATE_STATE_DESC);
                                                    valueCertState.setCellStyle(my_styleBranch);
                                                    i++;
                                                }
                                            }
                                            //</editor-fold>
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET Decline Customers">
                                            rsReportPgin = new CERTIFICATION_CONTROL_REPORT[1][];
                                            com.S_BO_REPORT_CERTIFICATE_DECLINE(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice), SessUserID,
                                                loginLanguage, rsReportPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO,
                                                Integer.parseInt(countList), SessUserAgentID, FromDate, ToDate);
                                            if(rsReportPgin[0].length > 0)
                                            {
                                                SXSSFSheet sheetCusDetail = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CUSTOMER_CANCEL, loginLanguage));
                                                sheetCusDetail.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                                sheetCusDetail.setColumnWidth(1, 18*255);
                                                sheetCusDetail.setColumnWidth(2, 18*255);
                                                sheetCusDetail.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                                sheetCusDetail.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                                sheetCusDetail.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                                sheetCusDetail.setColumnWidth(6, 18*255);
                                                sheetCusDetail.setColumnWidth(7, 18*255);
                                                sheetCusDetail.setColumnWidth(8, 18*255);
                                                sheetCusDetail.setColumnWidth(9, 22*255);
                                                sheetCusDetail.setColumnWidth(10, 22*255);
                                                sheetCusDetail.setColumnWidth(11, 32*255);
                                                sheetCusDetail.setColumnWidth(12, 24 * 255);
                                                sheetCusDetail.setColumnWidth(13, 24 * 255);
                                                sheetCusDetail.setColumnWidth(14, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                                sheetCusDetail.setColumnWidth(15, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                                sheetCusDetail.setColumnWidth(16, 22 * 255);
                                                sheetCusDetail.setColumnWidth(17, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                                sheetCusDetail.setColumnWidth(18, 18 * 255);
                                                sheetCusDetail.setColumnWidth(19, 18 * 255);
                                                sheetCusDetail.setColumnWidth(20, 18 * 255);
                                                sheetCusDetail.setColumnWidth(21, 18 * 255);
                                                Row rowCusDetail = sheetCusDetail.createRow(0);
                                                Cell celCus = rowCusDetail.createCell((short) 0);
                                                celCus.setCellValue(sCellSTT);
                                                celCus.setCellStyle(my_style);
                                                Cell celCus1 = rowCusDetail.createCell((short) 1);
                                                celCus1.setCellValue(sCellMounth);
                                                celCus1.setCellStyle(my_style);
                                                int iLevelImcre = 0;
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                    Cell celLevel1 = rowCusDetail.createCell((short) 2);
                                                    celLevel1.setCellValue(sAgentLevelOne);
                                                    celLevel1.setCellStyle(my_style);
                                                    iLevelImcre = 1;
                                                }
                                                Cell celCus2 = rowCusDetail.createCell((short) 2+iLevelImcre);
                                                celCus2.setCellValue(sCellAgency);
                                                celCus2.setCellStyle(my_style);
                                                Cell celCus4 = rowCusDetail.createCell((short) 3+iLevelImcre);
                                                celCus4.setCellValue(sCellMST);
                                                celCus4.setCellStyle(my_style);
                                                Cell celCus5 = rowCusDetail.createCell((short) 4+iLevelImcre);
                                                celCus5.setCellValue(sCellCompany);
                                                celCus5.setCellStyle(my_style);
                                                Cell celCus9 = rowCusDetail.createCell((short) 5+iLevelImcre);
                                                celCus9.setCellValue(sCellProfile);
                                                celCus9.setCellStyle(my_style);
                                                Cell celFeeAmount = rowCusDetail.createCell((short) 6+iLevelImcre);
                                                celFeeAmount.setCellValue(sCellProfileFee);
                                                celFeeAmount.setCellStyle(my_style);
                                                Cell celTokenNum = rowCusDetail.createCell((short) 7+iLevelImcre);
                                                celTokenNum.setCellValue(sCellTokenQuality);
                                                celTokenNum.setCellStyle(my_style);
                                                Cell celTokenAmount = rowCusDetail.createCell((short) 8+iLevelImcre);
                                                celTokenAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_FEE, loginLanguage));
                                                celTokenAmount.setCellStyle(my_style);
                                                Cell celRoseAmount = rowCusDetail.createCell((short) 9+iLevelImcre);
                                                celRoseAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_ROSE, loginLanguage));
                                                celRoseAmount.setCellStyle(my_style);
                                                Cell celSumAmount = rowCusDetail.createCell((short) 10+iLevelImcre);
                                                celSumAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONEY_CHARGES_CA, loginLanguage));
                                                celSumAmount.setCellStyle(my_style);
                                                Cell celDateGen = rowCusDetail.createCell((short) 11+iLevelImcre);
                                                celDateGen.setCellValue(sCellGenDate);
                                                celDateGen.setCellStyle(my_style);
                                                Cell celRevokeDate = rowCusDetail.createCell((short) 12+iLevelImcre);
                                                celRevokeDate.setCellValue(sCellRevokeDate);
                                                celRevokeDate.setCellStyle(my_style);
                                                Cell celCertSN = rowCusDetail.createCell((short) 13+iLevelImcre);
                                                celCertSN.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage));
                                                celCertSN.setCellStyle(my_style);
                                                Cell celRequestType = rowCusDetail.createCell((short) 14+iLevelImcre);
                                                celRequestType.setCellValue(sCellRequestType);
                                                celRequestType.setCellStyle(my_style);
                                                Cell celCreateUser = rowCusDetail.createCell((short) 15+iLevelImcre);
                                                celCreateUser.setCellValue(sCellUser);
                                                celCreateUser.setCellStyle(my_style);
                                                Cell celCus6 = rowCusDetail.createCell((short) 16+iLevelImcre);
                                                celCus6.setCellValue(sCellCMND);
                                                celCus6.setCellStyle(my_style);
                                                Cell celCus7 = rowCusDetail.createCell((short) 17+iLevelImcre);
                                                celCus7.setCellValue(sCellPersonal);
                                                celCus7.setCellStyle(my_style);
                                                Cell celTokenSN = rowCusDetail.createCell((short) 18+iLevelImcre);
                                                celTokenSN.setCellValue(sTokenSN);
                                                celTokenSN.setCellStyle(my_style);
                                                
                                                Cell celProfileStatus = rowCusDetail.createCell((short) 19+iLevelImcre);
                                                celProfileStatus.setCellValue(sPastProfileStatus);
                                                celProfileStatus.setCellStyle(my_style);
                                                
                                                Cell celAgentCode = rowCusDetail.createCell((short) 20+iLevelImcre);
                                                celAgentCode.setCellValue(sPastAgentCode);
                                                celAgentCode.setCellStyle(my_style);
                                                i = 1;
                                                k = 0;
                                                for (CERTIFICATION_CONTROL_REPORT temp1 : rsReportPgin[0]) {
                                                    if (k == 0) {
                                                        k = 1;
                                                    } else {
                                                        k++;
                                                    }
                                                    Row rowDecline = sheetCusDetail.createRow(Integer.valueOf(i));
                                                    CellStyle my_styleDecBranch = wb.createCellStyle();
                                                    Font fontDecBranch = wb.createFont();
                                                    fontDecBranch.setFontName("Verdana");
                                                    my_styleDecBranch.setFont(fontDecBranch);
                                                    
                                                    CellStyle my_styleBranchDate = wb.createCellStyle();
                                                    Font fontBranchDate = wb.createFont();
                                                    fontBranchDate.setFontName("Verdana");
                                                    my_styleBranchDate.setFont(fontBranchDate);
                                                    my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));

                                                    Cell cellBranch = rowDecline.createCell((short) 0);
                                                    cellBranch.setCellValue(k);
                                                    cellBranch.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellMounth = rowDecline.createCell((short) 1);
                                                    cellMounth.setCellValue(temp1.CROSS_CHECK_MONTH);
                                                    cellMounth.setCellStyle(my_styleDecBranch);
                                                    
                                                    iLevelImcre = 0;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        Cell cellLevel1 = rowDecline.createCell((short) 2);
                                                        cellLevel1.setCellValue(temp1.BRANCH_LEVEL_1_NAME);
                                                        cellLevel1.setCellStyle(my_styleDecBranch);
                                                        iLevelImcre = 1;
                                                    }
                                                    
                                                    Cell cellBranch1 = rowDecline.createCell((short) 2+iLevelImcre);
                                                    cellBranch1.setCellValue(temp1.BRANCH_NAME);
                                                    cellBranch1.setCellStyle(my_styleDecBranch);

                                                    String sTAX_CODE = temp1.ENTERPRISE_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)
                                                        || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        sTAX_CODE = CommonReferServlet.filterPrefixUIDLanguage(temp1.ENTERPRISE_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch4 = rowDecline.createCell((short) 3+iLevelImcre);
                                                    cellBranch4.setCellValue(sTAX_CODE);
                                                    cellBranch4.setCellStyle(styleText);

                                                    Cell cellBranch5 = rowDecline.createCell((short) 4+iLevelImcre);
                                                    cellBranch5.setCellValue(temp1.COMPANY_NAME);
                                                    cellBranch5.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch9 = rowDecline.createCell((short) 5+iLevelImcre);
                                                    cellBranch9.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
                                                    cellBranch9.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch10 = rowDecline.createCell((short) 6+iLevelImcre);
                                                    cellBranch10.setCellValue(clsCom.convertMoneyAnotherZero(temp1.FEE_AMOUNT));
                                                    cellBranch10.setCellStyle(my_styleDecBranch);

                                                    Cell cellDec11 = rowDecline.createCell((short) 7+iLevelImcre);
                                                    cellDec11.setCellValue(clsCom.convertMoneyAnotherZero(temp1.TOKEN_NUMBER));
                                                    cellDec11.setCellStyle(my_styleDecBranch);

                                                    Cell cellDec12 = rowDecline.createCell((short) 8+iLevelImcre);
                                                    cellDec12.setCellValue(clsCom.convertMoneyAnotherZero(temp1.TOKEN_AMOUNT));
                                                    cellDec12.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellRoseAmount = rowDecline.createCell((short) 9+iLevelImcre);
                                                    cellRoseAmount.setCellValue(clsCom.convertMoneyAnotherZero(temp1.ROSE_AMOUNT));
                                                    cellRoseAmount.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellSumAmount = rowDecline.createCell((short) 10+iLevelImcre);
                                                    cellSumAmount.setCellValue(clsCom.convertMoneyAnotherZero(temp1.RETURN_AMOUNT));
                                                    cellSumAmount.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellBranch15 = rowDecline.createCell((short) 11+iLevelImcre);
                                                    Date dateIssue = CommonFunction.convertStringToDate(temp1.ISSUED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateIssue != null) {
                                                        cellBranch15.setCellValue(dateIssue);
                                                    } else {
                                                        cellBranch15.setCellValue("");
                                                    }
                                                    cellBranch15.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell cellDec22 = rowDecline.createCell((short) 12+iLevelImcre);
                                                    Date dateRevoke = CommonFunction.convertStringToDate(temp1.REVOKED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateRevoke != null) {
                                                        cellDec22.setCellValue(dateRevoke);
                                                    } else {
                                                        cellDec22.setCellValue("");
                                                    }
                                                    cellDec22.setCellStyle(my_styleBranchDate);
                                                    
                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
                                                    Cell cellBranch19 = rowDecline.createCell((short) 13+iLevelImcre);
                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
                                                    cellBranch19.setCellStyle(styleText);

                                                    Cell cellBranch3 = rowDecline.createCell((short) 14+iLevelImcre);
                                                    cellBranch3.setCellValue(temp1.SERVICE_TYPE_DESC);
                                                    cellBranch3.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch2 = rowDecline.createCell((short) 15+iLevelImcre);
                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
                                                    cellBranch2.setCellStyle(my_styleDecBranch);

                                                    String sP_ID = temp1.PERSONAL_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)
                                                        || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        sP_ID = CommonReferServlet.filterPrefixUIDLanguage(temp1.PERSONAL_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch6 = rowDecline.createCell((short) 16+iLevelImcre);
                                                    cellBranch6.setCellValue(sP_ID);
                                                    cellBranch6.setCellStyle(styleText);

                                                    Cell cellBranch7 = rowDecline.createCell((short) 17+iLevelImcre);
                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
                                                    cellBranch7.setCellStyle(my_styleDecBranch);
                                                    
                                                    String valueTokenSN = temp1.TOKEN_SN;
                                                    if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                    Cell cellDec21 = rowDecline.createCell((short) 18+iLevelImcre);
                                                    cellDec21.setCellValue(valueTokenSN);
                                                    cellDec21.setCellStyle(styleText);
                                                    
                                                    Cell cellProfileStatus = rowDecline.createCell((short) 19+iLevelImcre);
                                                    cellProfileStatus.setCellValue(temp1.PAST_FILE_MANAGER_STATE_DESC);
                                                    cellProfileStatus.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellAgentCode = rowDecline.createCell((short) 20+iLevelImcre);
                                                    cellAgentCode.setCellValue(temp1.PAST_BRANCH_DESC);
                                                    cellAgentCode.setCellStyle(my_styleDecBranch);
                                                    i++;
                                                }
                                            }
                                            //</editor-fold>
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET RevokeAuto Customers">
                                            rsReportPgin = new CERTIFICATION_CONTROL_REPORT[1][];
                                            com.S_BO_REPORT_CERTIFICATE_AUTO_REVOKED(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice), SessUserID,
                                                loginLanguage, rsReportPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO,
                                                Integer.parseInt(countList), SessUserAgentID, FromDate, ToDate);
                                            if(rsReportPgin[0].length > 0)
                                            {
                                                SXSSFSheet sheetCusDetail = (SXSSFSheet) wb.createSheet(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CUSTOMER_REVOKE, loginLanguage));
                                                sheetCusDetail.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                                sheetCusDetail.setColumnWidth(1, 18*255);
                                                sheetCusDetail.setColumnWidth(2, 18*255);
                                                sheetCusDetail.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                                sheetCusDetail.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                                sheetCusDetail.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                                sheetCusDetail.setColumnWidth(6, 18*255);
                                                sheetCusDetail.setColumnWidth(7, 18*255);
                                                sheetCusDetail.setColumnWidth(8, 18*255);
                                                sheetCusDetail.setColumnWidth(9, 22*255);
                                                sheetCusDetail.setColumnWidth(10, 22*255);
                                                sheetCusDetail.setColumnWidth(11, 32*255);
                                                sheetCusDetail.setColumnWidth(12, 24 * 255);
                                                sheetCusDetail.setColumnWidth(13, 24 * 255);
                                                sheetCusDetail.setColumnWidth(14, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                                sheetCusDetail.setColumnWidth(15, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                                sheetCusDetail.setColumnWidth(16, 22 * 255);
                                                sheetCusDetail.setColumnWidth(17, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                                sheetCusDetail.setColumnWidth(18, 18 * 255);
                                                sheetCusDetail.setColumnWidth(19, 18 * 255);
                                                sheetCusDetail.setColumnWidth(20, 18 * 255);
                                                sheetCusDetail.setColumnWidth(21, 18 * 255);
                                                Row rowCusDetail = sheetCusDetail.createRow(0);
                                                Cell celCus = rowCusDetail.createCell((short) 0);
                                                celCus.setCellValue(sCellSTT);
                                                celCus.setCellStyle(my_style);
                                                Cell celCus1 = rowCusDetail.createCell((short) 1);
                                                celCus1.setCellValue(sCellMounth);
                                                celCus1.setCellStyle(my_style);
                                                int iLevelImcre = 0;
                                                if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                    Cell celLevel1 = rowCusDetail.createCell((short) 2);
                                                    celLevel1.setCellValue(sAgentLevelOne);
                                                    celLevel1.setCellStyle(my_style);
                                                    iLevelImcre = 1;
                                                }
                                                Cell celCus2 = rowCusDetail.createCell((short) 2+iLevelImcre);
                                                celCus2.setCellValue(sCellAgency);
                                                celCus2.setCellStyle(my_style);
                                                Cell celCus4 = rowCusDetail.createCell((short) 3+iLevelImcre);
                                                celCus4.setCellValue(sCellMST);
                                                celCus4.setCellStyle(my_style);
                                                Cell celCus5 = rowCusDetail.createCell((short) 4+iLevelImcre);
                                                celCus5.setCellValue(sCellCompany);
                                                celCus5.setCellStyle(my_style);
                                                Cell celCus9 = rowCusDetail.createCell((short) 5+iLevelImcre);
                                                celCus9.setCellValue(sCellProfile);
                                                celCus9.setCellStyle(my_style);
                                                Cell celFeeAmount = rowCusDetail.createCell((short) 6+iLevelImcre);
                                                celFeeAmount.setCellValue(sCellProfileFee);
                                                celFeeAmount.setCellStyle(my_style);
                                                Cell celTokenNum = rowCusDetail.createCell((short) 7+iLevelImcre);
                                                celTokenNum.setCellValue(sCellTokenQuality);
                                                celTokenNum.setCellStyle(my_style);
                                                Cell celTokenAmount = rowCusDetail.createCell((short) 8+iLevelImcre);
                                                celTokenAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_FEE, loginLanguage));
                                                celTokenAmount.setCellStyle(my_style);
                                                Cell celRoseAmount = rowCusDetail.createCell((short) 9+iLevelImcre);
                                                celRoseAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AMOUNT_ROSE, loginLanguage));
                                                celRoseAmount.setCellStyle(my_style);
                                                Cell celSumAmount = rowCusDetail.createCell((short) 10+iLevelImcre);
                                                celSumAmount.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONEY_CHARGES_CA, loginLanguage));
                                                celSumAmount.setCellStyle(my_style);
                                                Cell celDateGen = rowCusDetail.createCell((short) 11+iLevelImcre);
                                                celDateGen.setCellValue(sCellGenDate);
                                                celDateGen.setCellStyle(my_style);
                                                Cell celRevokeDate = rowCusDetail.createCell((short) 12+iLevelImcre);
                                                celRevokeDate.setCellValue(sCellRevokeDate);
                                                celRevokeDate.setCellStyle(my_style);
                                                Cell celCertSN = rowCusDetail.createCell((short) 13+iLevelImcre);
                                                celCertSN.setCellValue(confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage));
                                                celCertSN.setCellStyle(my_style);
                                                Cell celRequestType = rowCusDetail.createCell((short) 14+iLevelImcre);
                                                celRequestType.setCellValue(sCellRequestType);
                                                celRequestType.setCellStyle(my_style);
                                                Cell celCreateUser = rowCusDetail.createCell((short) 15+iLevelImcre);
                                                celCreateUser.setCellValue(sCellUser);
                                                celCreateUser.setCellStyle(my_style);
                                                Cell celCus6 = rowCusDetail.createCell((short) 16+iLevelImcre);
                                                celCus6.setCellValue(sCellCMND);
                                                celCus6.setCellStyle(my_style);
                                                Cell celCus7 = rowCusDetail.createCell((short) 17+iLevelImcre);
                                                celCus7.setCellValue(sCellPersonal);
                                                celCus7.setCellStyle(my_style);
                                                Cell celTokenSN = rowCusDetail.createCell((short) 18+iLevelImcre);
                                                celTokenSN.setCellValue(sTokenSN);
                                                celTokenSN.setCellStyle(my_style);
                                                
                                                Cell celProfileStatus = rowCusDetail.createCell((short) 19+iLevelImcre);
                                                celProfileStatus.setCellValue(sPastProfileStatus);
                                                celProfileStatus.setCellStyle(my_style);
                                                Cell celAgentCode = rowCusDetail.createCell((short) 20+iLevelImcre);
                                                celAgentCode.setCellValue(sPastAgentCode);
                                                celAgentCode.setCellStyle(my_style);
                                                i = 1;
                                                k = 0;
                                                for (CERTIFICATION_CONTROL_REPORT temp1 : rsReportPgin[0]) {
                                                    if (k == 0) {
                                                        k = 1;
                                                    } else {
                                                        k++;
                                                    }
                                                    Row rowDecline = sheetCusDetail.createRow(Integer.valueOf(i));
                                                    CellStyle my_styleDecBranch = wb.createCellStyle();
                                                    Font fontDecBranch = wb.createFont();
                                                    fontDecBranch.setFontName("Verdana");
                                                    my_styleDecBranch.setFont(fontDecBranch);
                                                    
                                                    CellStyle my_styleBranchDate = wb.createCellStyle();
                                                    Font fontBranchDate = wb.createFont();
                                                    fontBranchDate.setFontName("Verdana");
                                                    my_styleBranchDate.setFont(fontBranchDate);
                                                    my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));

                                                    Cell cellBranch = rowDecline.createCell((short) 0);
                                                    cellBranch.setCellValue(k);
                                                    cellBranch.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellMounth = rowDecline.createCell((short) 1);
                                                    cellMounth.setCellValue(temp1.CROSS_CHECK_MONTH);
                                                    cellMounth.setCellStyle(my_styleDecBranch);

                                                    iLevelImcre = 0;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        Cell cellLevel1 = rowDecline.createCell((short) 2);
                                                        cellLevel1.setCellValue(temp1.BRANCH_LEVEL_1_NAME);
                                                        cellLevel1.setCellStyle(my_styleDecBranch);
                                                        iLevelImcre = 1;
                                                    }
                                                    
                                                    Cell cellBranch1 = rowDecline.createCell((short) 2+iLevelImcre);
                                                    cellBranch1.setCellValue(temp1.BRANCH_NAME);
                                                    cellBranch1.setCellStyle(my_styleDecBranch);

                                                    String sTAX_CODE = temp1.ENTERPRISE_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)
                                                        || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        sTAX_CODE = CommonReferServlet.filterPrefixUIDLanguage(temp1.ENTERPRISE_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch4 = rowDecline.createCell((short) 3+iLevelImcre);
                                                    cellBranch4.setCellValue(sTAX_CODE);
                                                    cellBranch4.setCellStyle(styleText);

                                                    Cell cellBranch5 = rowDecline.createCell((short) 4+iLevelImcre);
                                                    cellBranch5.setCellValue(temp1.COMPANY_NAME);
                                                    cellBranch5.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch9 = rowDecline.createCell((short) 5+iLevelImcre);
                                                    cellBranch9.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
                                                    cellBranch9.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch10 = rowDecline.createCell((short) 6+iLevelImcre);
                                                    cellBranch10.setCellValue(clsCom.convertMoneyAnotherZero(temp1.FEE_AMOUNT));
                                                    cellBranch10.setCellStyle(my_styleDecBranch);

                                                    Cell cellDec11 = rowDecline.createCell((short) 7+iLevelImcre);
                                                    cellDec11.setCellValue(clsCom.convertMoneyAnotherZero(temp1.TOKEN_NUMBER));
                                                    cellDec11.setCellStyle(my_styleDecBranch);

                                                    Cell cellDec12 = rowDecline.createCell((short) 8+iLevelImcre);
                                                    cellDec12.setCellValue(clsCom.convertMoneyAnotherZero(temp1.TOKEN_AMOUNT));
                                                    cellDec12.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellRoseAmount = rowDecline.createCell((short) 9+iLevelImcre);
                                                    cellRoseAmount.setCellValue(clsCom.convertMoneyAnotherZero(temp1.ROSE_AMOUNT));
                                                    cellRoseAmount.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellSumAmount = rowDecline.createCell((short) 10+iLevelImcre);
                                                    cellSumAmount.setCellValue(clsCom.convertMoneyAnotherZero(temp1.RETURN_AMOUNT));
                                                    cellSumAmount.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellBranch15 = rowDecline.createCell((short) 11+iLevelImcre);
                                                    Date dateIssue = CommonFunction.convertStringToDate(temp1.ISSUED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateIssue != null) {
                                                        cellBranch15.setCellValue(dateIssue);
                                                    } else {
                                                        cellBranch15.setCellValue("");
                                                    }
                                                    cellBranch15.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell cellDec22 = rowDecline.createCell((short) 12+iLevelImcre);
                                                    Date dateRevoke = CommonFunction.convertStringToDate(temp1.REVOKED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateRevoke != null) {
                                                        cellDec22.setCellValue(dateRevoke);
                                                    } else {
                                                        cellDec22.setCellValue("");
                                                    }
                                                    cellDec22.setCellStyle(my_styleBranchDate);
                                                    
                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
                                                    Cell cellBranch19 = rowDecline.createCell((short) 13+iLevelImcre);
                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
                                                    cellBranch19.setCellStyle(styleText);

                                                    Cell cellBranch3 = rowDecline.createCell((short) 14+iLevelImcre);
                                                    cellBranch3.setCellValue(temp1.SERVICE_TYPE_DESC);
                                                    cellBranch3.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch2 = rowDecline.createCell((short) 15+iLevelImcre);
                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
                                                    cellBranch2.setCellStyle(my_styleDecBranch);

                                                    String sP_ID = temp1.PERSONAL_ID;
                                                    if(isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_NC)
                                                        || isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                                        sP_ID = CommonReferServlet.filterPrefixUIDLanguage(temp1.PERSONAL_ID, loginLanguage);
                                                    }
                                                    Cell cellBranch6 = rowDecline.createCell((short) 16+iLevelImcre);
                                                    cellBranch6.setCellValue(sP_ID);
                                                    cellBranch6.setCellStyle(styleText);

                                                    Cell cellBranch7 = rowDecline.createCell((short) 17+iLevelImcre);
                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
                                                    cellBranch7.setCellStyle(my_styleDecBranch);
                                                    
                                                    String valueTokenSN = temp1.TOKEN_SN;
                                                    if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                    Cell cellDec21 = rowDecline.createCell((short) 18+iLevelImcre);
                                                    cellDec21.setCellValue(valueTokenSN);
                                                    cellDec21.setCellStyle(styleText);
                                                    
                                                    Cell cellProfileStatus = rowDecline.createCell((short) 19+iLevelImcre);
                                                    cellProfileStatus.setCellValue(temp1.PAST_FILE_MANAGER_STATE_DESC);
                                                    cellProfileStatus.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellAgentCode = rowDecline.createCell((short) 20+iLevelImcre);
                                                    cellAgentCode.setCellValue(temp1.PAST_BRANCH_DESC);
                                                    cellAgentCode.setCellStyle(my_styleDecBranch);
                                                    i++;
                                                }
                                            }
                                            //</editor-fold>
                                            
                                            wb.removeSheetAt(0);
                                            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                            wb.write(outByteStream);
                                            byte[] outArray = outByteStream.toByteArray();
                                            File someFile = new File(strURLPath);
                                            FileOutputStream fos = new FileOutputStream(someFile);
                                            fos.write(outArray);
                                            fos.flush();
                                            strView = "0#" + strURLPath + "#" + sFileName;
                                        } else {
                                            strView = "2#1";
                                        }
                                    } else {
                                        strView = "2#1";
                                    }
                                } else {
                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "neacexportdetaillist": {
                                //<editor-fold defaultstate="collapsed" desc="neacexportdetaillist">
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                String pYear = request.getSession(false).getAttribute("sessYearDateNEACControl").toString().trim();
                                String pQUARTER = request.getSession(false).getAttribute("sessMountDateNEACControl").toString().trim();
                                String pOFFSET = EscapeUtils.CheckTextNull(request.getParameter("Tag_ID"));
                                CERTIFICATION[][] temp = new CERTIFICATION[1][];
                                com.S_BO_REPORT_NEAC_DETAIL(pQUARTER, pYear, Integer.parseInt(pOFFSET), Integer.parseInt(loginLanguage), temp);
                                if (temp[0].length > 0) {
                                    Config conf = new Config();
                                    ConfigLanguage confLanguage = new ConfigLanguage();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_CERTIFICATE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, loginLanguage);
                                    String sCellCompany = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COMPANY_NAME, loginLanguage);
                                    String sCellMST = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_ENTERPRISE, loginLanguage);
                                    String sCellPersonal = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PERSONAL_NAME, loginLanguage);
                                    String sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL, loginLanguage);
                                    String sCellDomain = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DOMAIN_NAME, loginLanguage);
                                    String sCellMethod = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_METHOD_NAME, loginLanguage);
                                    String sCellProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICEPACK_NAME, loginLanguage);
                                    String sCellCertType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_TYPE, loginLanguage);
                                    String sCellStatus = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STATUS, loginLanguage);
                                    String sCellRequest = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REQUEST_TYPE, loginLanguage);
                                    String sCellCreateBy = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_USER_CREATE, loginLanguage);
                                    String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, loginLanguage);
                                    String sCellCreateTime = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CREATE, loginLanguage);
                                    String sCellRevokeTime = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_REVOKE, loginLanguage);
                                    String sCellEffectiveTime = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EFFECTIVE, loginLanguage);
                                    String sCellTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_SN, loginLanguage);
                                    String sCellCertSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage);
                                    
                                    HSSFWorkbook wb = new HSSFWorkbook();
                                    CreationHelper createHelper = wb.getCreationHelper();  
                                    HSSFSheet sheet = wb.createSheet();
                                    sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                    sheet.setColumnWidth(1, 24 * 255);
                                    sheet.setColumnWidth(15, 24 * 255);
                                    sheet.setColumnWidth(16, 24 * 255);
                                    sheet.setColumnWidth(17, 24 * 255);
                                    HSSFRow row = sheet.createRow(0);
                                    HSSFCellStyle my_style = wb.createCellStyle();
                                    HSSFFont font = wb.createFont();
                                    font.setBoldweight((short) 700);
                                    font.setFontName("Arial");
                                    my_style.setFont(font);
                                    my_style.setFillBackgroundColor((short) 9);
                                    my_style.setAlignment((short) 2);
                                    Cell cell = row.createCell((short) 0);
                                    cell.setCellValue(sCellSTT);
                                    cell.setCellStyle(my_style);
                                    Cell cell1 = row.createCell((short) 1);
                                    cell1.setCellValue(sCellCompany);
                                    cell1.setCellStyle(my_style);
                                    Cell cell2 = row.createCell((short) 2);
                                    cell2.setCellValue(sCellMST);
                                    cell2.setCellStyle(my_style);
                                    Cell cell3 = row.createCell((short) 3);
                                    cell3.setCellValue(sCellPersonal);
                                    cell3.setCellStyle(my_style);
                                    Cell cell4 = row.createCell((short) 4);
                                    cell4.setCellValue(sCellCMND);
                                    cell4.setCellStyle(my_style);
                                    Cell cell5 = row.createCell((short) 5);
                                    cell5.setCellValue(sCellDomain);
                                    cell5.setCellStyle(my_style);
                                    Cell cell6 = row.createCell((short) 6);
                                    cell6.setCellValue(sCellMethod);
                                    cell6.setCellStyle(my_style);
                                    Cell cell7 = row.createCell((short) 7);
                                    cell7.setCellValue(sCellProfile);
                                    cell7.setCellStyle(my_style);
                                    Cell cell8 = row.createCell((short) 8);
                                    cell8.setCellValue(sCellCertType);
                                    cell8.setCellStyle(my_style);
                                    Cell cell9 = row.createCell((short) 9);
                                    cell9.setCellValue(sCellStatus);
                                    cell9.setCellStyle(my_style);
                                    Cell cell10 = row.createCell((short) 10);
                                    cell10.setCellValue(sCellRequest);
                                    cell10.setCellStyle(my_style);
                                    
                                    Cell cell101 = row.createCell((short) 11);
                                    cell101.setCellValue(sCellCertSN);
                                    cell101.setCellStyle(my_style);
                                    Cell cell102 = row.createCell((short) 12);
                                    cell102.setCellValue(sCellTokenSN);
                                    cell102.setCellStyle(my_style);
                                    
                                    Cell cell11 = row.createCell((short) 13);
                                    cell11.setCellValue(sCellCreateBy);
                                    cell11.setCellStyle(my_style);
                                    Cell cell12 = row.createCell((short) 14);
                                    cell12.setCellValue(sCellAgency);
                                    cell12.setCellStyle(my_style);
                                    Cell cell13 = row.createCell((short) 15);
                                    cell13.setCellValue(sCellCreateTime);
                                    cell13.setCellStyle(my_style);
                                    Cell cell14 = row.createCell((short) 16);
                                    cell14.setCellValue(sCellEffectiveTime);
                                    cell14.setCellStyle(my_style);
                                    Cell cell141 = row.createCell((short) 17);
                                    cell141.setCellValue(sCellRevokeTime);
                                    cell141.setCellStyle(my_style);
                                    
                                    Font fontBranch = wb.createFont();
                                    fontBranch.setFontName("Arial");
                                    CellStyle styleText = wb.createCellStyle();
                                    styleText.setFont(fontBranch);
                                    styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));
                                    
                                    int i = 1;
                                    int k = 0;
                                    for (CERTIFICATION temp1 : temp[0]) {
                                        if (k == 0) {
                                            k = 1;
                                        } else {
                                            k++;
                                        }
                                        HSSFRow row1 = sheet.createRow(Integer.valueOf(i).intValue());
                                        HSSFCellStyle my_styleBranch = wb.createCellStyle();
                                        my_styleBranch.setFont(fontBranch);
                                        my_styleBranch.setFillBackgroundColor((short) 9);
                                        
                                        CellStyle my_styleBranchDate = wb.createCellStyle();
                                        Font fontBranchDate = wb.createFont();
                                        fontBranchDate.setFontName("Arial");
                                        my_styleBranchDate.setFont(fontBranchDate);
                                        my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS));

                                        Cell cellBranch = row1.createCell((short) 0);
                                        cellBranch.setCellValue(k);
                                        cellBranch.setCellStyle(my_styleBranch);

                                        Cell cellValue1 = row1.createCell((short) 1);
                                        cellValue1.setCellValue(temp1.COMPANY_NAME);
                                        cellValue1.setCellStyle(my_styleBranch);
                                        String sMST_MNS = temp1.ENTERPRISE_ID;
//                                        if("".equals(sMST_MNS)) {
//                                            sMST_MNS = temp1.BUDGET_CODE;
//                                        }
//                                        if("".equals(sMST_MNS)) {
//                                            sMST_MNS = temp1.DECISION;
//                                        }
                                        Cell cellValue2 = row1.createCell((short) 2);
                                        cellValue2.setCellValue(sMST_MNS);
                                        cellValue2.setCellStyle(styleText);

                                        Cell cellValue3 = row1.createCell((short) 3);
                                        cellValue3.setCellValue(temp1.PERSONAL_NAME);
                                        cellValue3.setCellStyle(my_styleBranch);
                                        String sCMND_PASSPORT = temp1.PERSONAL_ID;
//                                        if("".equals(sCMND_PASSPORT)) {
//                                            sCMND_PASSPORT = temp1.P_EID;
//                                        }
//                                        if("".equals(sCMND_PASSPORT)) {
//                                            sCMND_PASSPORT = temp1.PASSPORT;
//                                        }
                                        Cell cellValue4 = row1.createCell((short) 4);
                                        cellValue4.setCellValue(sCMND_PASSPORT);
                                        cellValue4.setCellStyle(styleText);
                                        
                                        Cell cellValue5 = row1.createCell((short) 5);
                                        cellValue5.setCellValue(temp1.DOMAIN_NAME);
                                        cellValue5.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue6 = row1.createCell((short) 6);
                                        cellValue6.setCellValue(temp1.PKI_FORMFACTOR_DESC);
                                        cellValue6.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue7 = row1.createCell((short) 7);
                                        cellValue7.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
                                        cellValue7.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue8 = row1.createCell((short) 8);
                                        cellValue8.setCellValue(temp1.CERTIFICATION_PURPOSE_DESC);
                                        cellValue8.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue9 = row1.createCell((short) 9);
                                        cellValue9.setCellValue(temp1.CERTIFICATION_STATE_DESC);
                                        cellValue9.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue10 = row1.createCell((short) 10);
                                        cellValue10.setCellValue(temp1.CERTIFICATION_ATTR_TYPE_DESC);
                                        cellValue10.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue101 = row1.createCell((short) 11);
                                        cellValue101.setCellValue(temp1.CERTIFICATION_SN);
                                        cellValue101.setCellStyle(styleText);
                                        
                                        Cell cellValue102 = row1.createCell((short) 12);
                                        cellValue102.setCellValue(temp1.TOKEN_SN);
                                        cellValue102.setCellStyle(styleText);
                                        
                                        Cell cellValue11 = row1.createCell((short) 13);
                                        cellValue11.setCellValue(temp1.CREATED_BY);
                                        cellValue11.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue12 = row1.createCell((short) 14);
                                        cellValue12.setCellValue(temp1.BRANCH_DESC);
                                        cellValue12.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue13 = row1.createCell((short) 15);
                                        Date dateCreate = CommonFunction.convertStringToDate(temp1.CREATED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                        if(dateCreate != null) {
                                            cellValue13.setCellValue(dateCreate);
                                        } else {
                                            cellValue13.setCellValue("");
                                        }
                                        cellValue13.setCellStyle(my_styleBranchDate);
                                        
                                        Cell cellValueEffective = row1.createCell((short) 16);
                                        Date dateEffective = CommonFunction.convertStringToDate(temp1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                        if(dateEffective != null) {
                                            cellValueEffective.setCellValue(dateEffective);
                                        } else {
                                            cellValueEffective.setCellValue("");
                                        }
                                        cellValueEffective.setCellStyle(my_styleBranchDate);
                                        Cell cellValue14 = row1.createCell((short) 17);
                                        Date dateRevoke = CommonFunction.convertStringToDate(temp1.REVOKED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                        if(dateRevoke != null) {
                                            cellValue14.setCellValue(dateRevoke);
                                        } else {
                                            cellValue14.setCellValue("");
                                        }
                                        cellValue14.setCellStyle(my_styleBranchDate);
                                        i++;
                                    }
                                    ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                    wb.write(outByteStream);
                                    byte[] outArray = outByteStream.toByteArray();
                                    File someFile = new File(strURLPath);
                                    FileOutputStream fos = new FileOutputStream(someFile);
                                    fos.write(outArray);
                                    fos.flush();
                                    strView = "0#" + strURLPath + "#" + sFileName;
                                } else {
                                    strView = "2#1";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "neacexportdetaillist2": {
                                //<editor-fold defaultstate="collapsed" desc="neacexportdetaillist2">
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                String pYear = request.getSession(false).getAttribute("sessYearDateNEACControl").toString().trim();
                                String pQUARTER = request.getSession(false).getAttribute("sessMountDateNEACControl").toString().trim();
                                String pOFFSET = EscapeUtils.CheckTextNull(request.getParameter("Tag_ID"));
                                String sPurposeName = pOFFSET.split("#")[0];
                                String sStatusName = pOFFSET.split("#")[1];
                                CERTIFICATION[][] temp = new CERTIFICATION[1][];
                                com.S_BO_REPORT_PERIODIC_DETAIL(pQUARTER, pYear, sPurposeName, sStatusName, loginLanguage, temp);
                                if (temp[0].length > 0) {
                                    Config conf = new Config();
                                    ConfigLanguage confLanguage = new ConfigLanguage();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_CERTIFICATE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    CommonFunction.LogDebugString(log, "NEACExportDetailList", "PathURL: " + strURLPath);
                                    String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, loginLanguage).trim();
                                    String sCellCompany = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COMPANY_NAME, loginLanguage).trim();
                                    String sCellMST = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_ENTERPRISE, loginLanguage).trim();
                                    String sCellPersonal = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PERSONAL_NAME, loginLanguage).trim();;
                                    String sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL, loginLanguage).trim();
                                    String sCellDomain = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DOMAIN_NAME, loginLanguage).trim();
                                    String sCellMethod = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_METHOD_NAME, loginLanguage).trim();
                                    String sCellProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICEPACK_NAME, loginLanguage).trim();
                                    String sCellCertType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_TYPE, loginLanguage).trim();
                                    String sCellStatus = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STATUS, loginLanguage).trim();
                                    String sCellRequest = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REQUEST_TYPE, loginLanguage).trim();
                                    String sCellCreateBy = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_USER_CREATE, loginLanguage).trim();
                                    String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, loginLanguage).trim();
                                    String sCellCreateTime = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CREATE, loginLanguage).trim();
                                    String sCellEffectiveTime = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EFFECTIVE, loginLanguage).trim();
                                    String sCellRevokeTime = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_REVOKE, loginLanguage).trim();
                                    String sCellTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_SN, loginLanguage).trim();
                                    String sCellCertSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage).trim();
                                    
                                    HSSFWorkbook wb = new HSSFWorkbook();
                                    CreationHelper createHelper = wb.getCreationHelper();  
                                    HSSFSheet sheet = wb.createSheet();
                                    sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                    sheet.setColumnWidth(1, 24 * 255);
                                    sheet.setColumnWidth(15, 24 * 255);
                                    sheet.setColumnWidth(16, 24 * 255);
                                    sheet.setColumnWidth(17, 24 * 255);
                                    HSSFRow row = sheet.createRow(0);
                                    HSSFCellStyle my_style = wb.createCellStyle();
                                    HSSFFont font = wb.createFont();
                                    font.setBoldweight((short) 700);
                                    font.setFontName("Arial");
                                    my_style.setFont(font);
                                    my_style.setFillBackgroundColor((short) 9);
                                    my_style.setAlignment((short) 2);
                                    Cell cell = row.createCell((short) 0);
                                    cell.setCellValue(sCellSTT);
                                    cell.setCellStyle(my_style);
                                    Cell cell1 = row.createCell((short) 1);
                                    cell1.setCellValue(sCellCompany);
                                    cell1.setCellStyle(my_style);
                                    Cell cell2 = row.createCell((short) 2);
                                    cell2.setCellValue(sCellMST);
                                    cell2.setCellStyle(my_style);
                                    Cell cell3 = row.createCell((short) 3);
                                    cell3.setCellValue(sCellPersonal);
                                    cell3.setCellStyle(my_style);
                                    Cell cell4 = row.createCell((short) 4);
                                    cell4.setCellValue(sCellCMND);
                                    cell4.setCellStyle(my_style);
                                    Cell cell5 = row.createCell((short) 5);
                                    cell5.setCellValue(sCellDomain);
                                    cell5.setCellStyle(my_style);
                                    Cell cell6 = row.createCell((short) 6);
                                    cell6.setCellValue(sCellMethod);
                                    cell6.setCellStyle(my_style);
                                    Cell cell7 = row.createCell((short) 7);
                                    cell7.setCellValue(sCellProfile);
                                    cell7.setCellStyle(my_style);
                                    Cell cell8 = row.createCell((short) 8);
                                    cell8.setCellValue(sCellCertType);
                                    cell8.setCellStyle(my_style);
                                    Cell cell9 = row.createCell((short) 9);
                                    cell9.setCellValue(sCellStatus);
                                    cell9.setCellStyle(my_style);
                                    Cell cell10 = row.createCell((short) 10);
                                    cell10.setCellValue(sCellRequest);
                                    cell10.setCellStyle(my_style);
                                    
                                    Cell cell101 = row.createCell((short) 11);
                                    cell101.setCellValue(sCellCertSN);
                                    cell101.setCellStyle(my_style);
                                    Cell cell102 = row.createCell((short) 12);
                                    cell102.setCellValue(sCellTokenSN);
                                    cell102.setCellStyle(my_style);
                                    
                                    Cell cell11 = row.createCell((short) 13);
                                    cell11.setCellValue(sCellCreateBy);
                                    cell11.setCellStyle(my_style);
                                    Cell cell12 = row.createCell((short) 14);
                                    cell12.setCellValue(sCellAgency);
                                    cell12.setCellStyle(my_style);
                                    Cell cell13 = row.createCell((short) 15);
                                    cell13.setCellValue(sCellCreateTime);
                                    cell13.setCellStyle(my_style);
                                    Cell cell14 = row.createCell((short) 16);
                                    cell14.setCellValue(sCellEffectiveTime);
                                    cell14.setCellStyle(my_style);
                                    Cell cell147 = row.createCell((short) 17);
                                    cell147.setCellValue(sCellRevokeTime);
                                    cell147.setCellStyle(my_style);
                                    
                                    HSSFFont fontBranch = wb.createFont();
                                    fontBranch.setFontName("Arial");
                                    HSSFCellStyle styleText = wb.createCellStyle();
                                    styleText.setFont(fontBranch);
                                    styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));
                                    
                                    int i = 1;
                                    int k = 0;
                                    for (CERTIFICATION temp1 : temp[0]) {
                                        if (k == 0) {
                                            k = 1;
                                        } else {
                                            k++;
                                        }
                                        HSSFRow row1 = sheet.createRow(Integer.valueOf(i).intValue());
                                        HSSFCellStyle my_styleBranch = wb.createCellStyle();
                                        my_styleBranch.setFont(fontBranch);
                                        my_styleBranch.setFillBackgroundColor((short) 9);
                                        
                                        CellStyle my_styleBranchDate = wb.createCellStyle();
                                        Font fontBranchDate = wb.createFont();
                                        fontBranchDate.setFontName("Arial");
                                        my_styleBranchDate.setFont(fontBranchDate);
                                        my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS));

                                        Cell cellBranch = row1.createCell((short) 0);
                                        cellBranch.setCellValue(k);
                                        cellBranch.setCellStyle(my_styleBranch);

                                        Cell cellValue1 = row1.createCell((short) 1);
                                        cellValue1.setCellValue(temp1.COMPANY_NAME);
                                        cellValue1.setCellStyle(my_styleBranch);
                                        String sMST_MNS = temp1.ENTERPRISE_ID;
//                                        if("".equals(sMST_MNS)) {
//                                            sMST_MNS = temp1.BUDGET_CODE;
//                                        }
//                                        if("".equals(sMST_MNS)) {
//                                            sMST_MNS = temp1.DECISION;
//                                        }
                                        Cell cellValue2 = row1.createCell((short) 2);
                                        cellValue2.setCellValue(sMST_MNS);
                                        cellValue2.setCellStyle(styleText);

                                        Cell cellValue3 = row1.createCell((short) 3);
                                        cellValue3.setCellValue(temp1.PERSONAL_NAME);
                                        cellValue3.setCellStyle(my_styleBranch);
                                        String sCMND_PASSPORT = temp1.PERSONAL_ID;
//                                        if("".equals(sCMND_PASSPORT)) {
//                                            sCMND_PASSPORT = temp1.P_EID;
//                                        }
//                                        if("".equals(sCMND_PASSPORT)) {
//                                            sCMND_PASSPORT = temp1.PASSPORT;
//                                        }
                                        Cell cellValue4 = row1.createCell((short) 4);
                                        cellValue4.setCellValue(sCMND_PASSPORT);
                                        cellValue4.setCellStyle(styleText);
                                        
                                        Cell cellValue5 = row1.createCell((short) 5);
                                        cellValue5.setCellValue(temp1.DOMAIN_NAME);
                                        cellValue5.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue6 = row1.createCell((short) 6);
                                        cellValue6.setCellValue(temp1.PKI_FORMFACTOR_DESC);
                                        cellValue6.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue7 = row1.createCell((short) 7);
                                        cellValue7.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
                                        cellValue7.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue8 = row1.createCell((short) 8);
                                        cellValue8.setCellValue(temp1.CERTIFICATION_PURPOSE_DESC);
                                        cellValue8.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue9 = row1.createCell((short) 9);
                                        cellValue9.setCellValue(temp1.CERTIFICATION_STATE_DESC);
                                        cellValue9.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue10 = row1.createCell((short) 10);
                                        cellValue10.setCellValue(temp1.CERTIFICATION_ATTR_TYPE_DESC);
                                        cellValue10.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue101 = row1.createCell((short) 11);
                                        cellValue101.setCellValue(temp1.CERTIFICATION_SN);
                                        cellValue101.setCellStyle(styleText);
                                        
                                        Cell cellValue102 = row1.createCell((short) 12);
                                        cellValue102.setCellValue(temp1.TOKEN_SN);
                                        cellValue102.setCellStyle(styleText);
                                        
                                        Cell cellValue11 = row1.createCell((short) 13);
                                        cellValue11.setCellValue(temp1.CREATED_BY);
                                        cellValue11.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue12 = row1.createCell((short) 14);
                                        cellValue12.setCellValue(temp1.BRANCH_DESC);
                                        cellValue12.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue13 = row1.createCell((short) 15);
                                        Date dateCreate = CommonFunction.convertStringToDate(temp1.CREATED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                        if(dateCreate != null) {
                                            cellValue13.setCellValue(dateCreate);
                                        } else {
                                            cellValue13.setCellValue("");
                                        }
                                        cellValue13.setCellStyle(my_styleBranchDate);
                                        
                                        Cell cellValueEffective = row1.createCell((short) 16);
                                        Date dateEffective = CommonFunction.convertStringToDate(temp1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                        if(dateEffective != null) {
                                            cellValueEffective.setCellValue(dateEffective);
                                        } else {
                                            cellValueEffective.setCellValue("");
                                        }
                                        cellValueEffective.setCellStyle(my_styleBranchDate);
                                        Cell cellValue14 = row1.createCell((short) 17);
                                        Date dateRevoke = CommonFunction.convertStringToDate(temp1.REVOKED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                        if(dateRevoke != null) {
                                            cellValue14.setCellValue(dateRevoke);
                                        } else {
                                            cellValue14.setCellValue("");
                                        }
                                        cellValue14.setCellStyle(my_styleBranchDate);
                                        i++;
                                    }
                                    ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                    wb.write(outByteStream);
                                    byte[] outArray = outByteStream.toByteArray();
                                    File someFile = new File(strURLPath);
                                    FileOutputStream fos = new FileOutputStream(someFile);
                                    fos.write(outArray);
                                    fos.flush();
                                    strView = "0#" + strURLPath + "#" + sFileName;
                                } else {
                                    strView = "2#1";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "exporttokensopinlist": {
                                //<editor-fold defaultstate="collapsed" desc="exporttokensopinlist">
//                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                    String isCheckAll = request.getParameter("isCheckAll");
                                    if (null != isCheckAll) {
                                        Config conf = new Config();
                                        ConfigLanguage confLanguage = new ConfigLanguage();
                                        String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                        File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                        if (!directory.exists()) {
                                            directory.mkdir();
                                        }
                                        String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                        String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_TOKEN_EXPORT + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                        String strURLPath = pPathURL + sFileName;
                                        switch (isCheckAll) {
                                            case "0":
                                                String idValue = EscapeUtils.CheckTextNull(request.getParameter("idValue"));
                                                idValue = idValue.substring(0, idValue.lastIndexOf(","));
                                                String[] sPlitValue = idValue.replace(Definitions.CONFIG_GRID_TAG_VALUE_CHECKBOX, "").split(",");
                                                if (sPlitValue.length > 0) {
                                                    String queryString = getServletContext().getRealPath("/");
                                                    String outputDirectory = queryString;
                                                    String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                                    FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                                    XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                                    inputStream.close();
                                                    String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, loginLanguage).trim();
                                                    String sCellTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COLUMN_TOKEN_SN, loginLanguage).trim();
                                                    String sCellTokenSOPIN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COLUMN_TOKEN_SOPIN, loginLanguage).trim();
                                                    SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                                    wb.setCompressTempFiles(true);
                                                    //HSSFWorkbook wb = new HSSFWorkbook();
                                                    SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
                                                    sheet.setRandomAccessWindowSize(100);
                                                    sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                                    sheet.setColumnWidth(1, 36*255);
                                                    sheet.setColumnWidth(2, 28*255);
                                                    Row row = sheet.createRow(0);

                                                    CellStyle my_style = wb.createCellStyle();
                                                    Font font = wb.createFont();
                                                    font.setBoldweight((short) 700);
                                                    font.setFontName("Arial");
                                                    my_style.setFont(font);
                                                    my_style.setFillBackgroundColor((short) 9);
                                                    my_style.setAlignment((short) 2);
                                                    Cell cell = row.createCell((short) 0);
                                                    cell.setCellValue(sCellSTT);
                                                    cell.setCellStyle(my_style);

                                                    Cell cell5 = row.createCell((short) 1);
                                                    cell5.setCellValue(sCellTokenSN);
                                                    cell5.setCellStyle(my_style);

                                                    Cell cell3 = row.createCell((short) 2);
                                                    cell3.setCellValue(sCellTokenSOPIN);
                                                    cell3.setCellStyle(my_style);
                                                    Font fontBranch = wb.createFont();
                                                    fontBranch.setFontName("Arial");
                                                    CreationHelper createHelper = wb.getCreationHelper();
                                                    CellStyle styleText = wb.createCellStyle();
                                                    styleText.setFont(fontBranch);
                                                    styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));
                                                    int i = 1;
                                                    int k = 0;
                                                    for (String sPlitValue1 : sPlitValue) {
                                                        TOKEN[][] rsPgin = new TOKEN[1][];
                                                        com.S_BO_TOKEN_DETAIL(sPlitValue1, rsPgin);
                                                        if(rsPgin[0].length > 0)
                                                        {
                                                            if (k == 0) {
                                                                k = 1;
                                                            } else {
                                                                k++;
                                                            }
                                                            Row row1 = sheet.createRow(Integer.valueOf(i));
                                                            CellStyle my_styleBranch = wb.createCellStyle();
                                                            my_styleBranch.setFont(fontBranch);

                                                            Cell cellBranch = row1.createCell((short) 0);
                                                            cellBranch.setCellValue(k);
                                                            cellBranch.setCellStyle(my_styleBranch);

                                                            Cell cellBranch1 = row1.createCell((short) 1);
                                                            cellBranch1.setCellValue(EscapeUtils.CheckTextNull(rsPgin[0][0].TOKEN_SN));
                                                            cellBranch1.setCellType(Cell.CELL_TYPE_STRING);
                                                            cellBranch1.setCellStyle(styleText);
                                                            String sSOPIN = rsPgin[0][0].SO_PIN;
                                                            EncodeSOPIN encript = new EncodeSOPIN();
                                                            if(!"".equals(sSOPIN)) {
                                                                sSOPIN = encript.decode(sSOPIN);
                                                            }
                                                            Cell cellBranch2 = row1.createCell((short) 2);
                                                            cellBranch2.setCellValue(sSOPIN);
                                                            cellBranch2.setCellType(Cell.CELL_TYPE_STRING);
                                                            cellBranch2.setCellStyle(my_styleBranch);
                                                            i++;
                                                        }
                                                    }
                                                    ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                                    wb.write(outByteStream);
                                                    byte[] outArray = outByteStream.toByteArray();
                                                    File someFile = new File(strURLPath);
                                                    FileOutputStream fos = new FileOutputStream(someFile);
                                                    fos.write(outArray);
                                                    fos.flush();
                                                    strView = "0#" + strURLPath + "#" + sFileName;
                                                } else {
                                                    strView = "1#0";
                                                }
                                                break;
                                            case "1":
//                                                int[] pIa = new int[1];
//                                                int[] pIb = new int[1];
                                                String idSessIsDate = (String) request.getSession(false).getAttribute("idSessIsDateSExport");
                                                String idSessIsToken = (String) request.getSession(false).getAttribute("idSessIsTokenSExport");
                                                String FromDateValid = (String) request.getSession(false).getAttribute("sessFromDateValidExport");
                                                String ToDateValid = (String) request.getSession(false).getAttribute("sessToDateValidExport");
                                                String FromTOKEN_ID = (String) request.getSession(false).getAttribute("sessFromTOKEN_IDExport");
                                                String ToTOKEN_ID = (String) request.getSession(false).getAttribute("sessToTOKEN_IDExport");
                                                String AGENT_ID = (String) request.getSession(false).getAttribute("sessAGENT_IDExport");
                                                String sessTreeArrayBranchID = (String) request.getSession(false).getAttribute("sessTreeArrayBranchIDSystem");
                                                request.getSession(false).setAttribute("idSessIsDateSExport", idSessIsDate);
                                                request.getSession(false).setAttribute("idSessIsTokenSExport", idSessIsToken);
                                                request.getSession(false).setAttribute("sessFromDateValidExport", FromDateValid);
                                                request.getSession(false).setAttribute("sessToDateValidExport", ToDateValid);
                                                request.getSession(false).setAttribute("sessFromTOKEN_IDExport", FromTOKEN_ID);
                                                request.getSession(false).setAttribute("sessToTOKEN_IDExport", ToTOKEN_ID);
                                                request.getSession(false).setAttribute("sessAGENT_IDExport", AGENT_ID);
//                                                pIa[0] = Integer.parseInt((String) request.getSession(false).getAttribute("pIaExport"));
//                                                pIb[0] = Integer.parseInt((String) request.getSession(false).getAttribute("pIbExport"));
                                                if (!"1".equals(idSessIsDate)) {
                                                    FromDateValid = "";
                                                    ToDateValid = "";
                                                }
                                                if (!"1".equals(idSessIsToken)) {
                                                    FromTOKEN_ID = "";
                                                    ToTOKEN_ID = "";
                                                }
                                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                                String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                                if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                                    AGENT_ID = SessUserAgentID;
                                                } else {
                                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
                                                        AGENT_ID = "";
                                                    }
                                                }
                                                TOKEN[][] rsPgin = new TOKEN[1][];
                                                int sCount = com.S_BO_TOKEN_IMPORT_TOTAL(EscapeUtils.escapeHtmlSearch(FromDateValid),
                                                    EscapeUtils.escapeHtmlSearch(ToDateValid), EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                                    EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID), sessTreeArrayBranchID);
                                                if (sCount > 0) {
                                                    com.S_BO_TOKEN_IMPORT_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid),
                                                        EscapeUtils.escapeHtmlSearch(ToDateValid),
                                                        EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                                        EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                                        loginLanguage, rsPgin, Definitions.CONFIG_GRID_INT_PAGNO, sCount, sessTreeArrayBranchID);
                                                    if (rsPgin[0] != null && rsPgin[0].length > 0) {
                                                        String queryString = getServletContext().getRealPath("/");
                                                        String outputDirectory = queryString;
                                                        String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                                        FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                                        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                                        inputStream.close();
                                                        
                                                        String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, loginLanguage).trim();
                                                        String sCellTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COLUMN_TOKEN_SN, loginLanguage).trim();
                                                        String sCellTokenSOPIN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COLUMN_TOKEN_SOPIN, loginLanguage).trim();
                                                        SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                                        wb.setCompressTempFiles(true);
                                        
//                                                        HSSFWorkbook wb = new HSSFWorkbook();
                                                        SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
                                                        sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                                        sheet.setColumnWidth(1, 36*255);
                                                        sheet.setColumnWidth(2, 28*255);
                                                        Row row = sheet.createRow(0);

                                                        CellStyle my_style = wb.createCellStyle();
                                                        Font font = wb.createFont();
                                                        font.setBoldweight((short) 700);
                                                        font.setFontName("Arial");
                                                        my_style.setFont(font);
                                                        my_style.setFillBackgroundColor((short) 9);
                                                        my_style.setAlignment((short) 2);
                                                        Cell cell = row.createCell((short) 0);
                                                        cell.setCellValue(sCellSTT);
                                                        cell.setCellStyle(my_style);

                                                        Cell cell5 = row.createCell((short) 1);
                                                        cell5.setCellValue(sCellTokenSN);
                                                        cell5.setCellStyle(my_style);

                                                        Cell cell3 = row.createCell((short) 2);
                                                        cell3.setCellValue(sCellTokenSOPIN);
                                                        cell3.setCellStyle(my_style);
                                                        
                                                        Font fontBranch = wb.createFont();
                                                        fontBranch.setFontName("Arial");
                                                        CreationHelper createHelper = wb.getCreationHelper();
                                                        CellStyle styleText = wb.createCellStyle();
                                                        styleText.setFont(fontBranch);
                                                        styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));

                                                        int i = 1;
                                                        int k = 0;
                                                        for (TOKEN temp1 : rsPgin[0]) {
                                                            if (k == 0) {
                                                                k = 1;
                                                            } else {
                                                                k++;
                                                            }
                                                            Row row1 = sheet.createRow(Integer.valueOf(i));
                                                            CellStyle my_styleBranch = wb.createCellStyle();
                                                            my_styleBranch.setFont(fontBranch);

                                                            Cell cellBranch = row1.createCell((short) 0);
                                                            cellBranch.setCellValue(k);
                                                            cellBranch.setCellStyle(my_styleBranch);

                                                            Cell cellBranch1 = row1.createCell((short) 1);
                                                            cellBranch1.setCellValue(EscapeUtils.CheckTextNull(temp1.TOKEN_SN));
                                                            cellBranch1.setCellType(Cell.CELL_TYPE_STRING);
                                                            cellBranch1.setCellStyle(styleText);
                                                            String sSOPIN = temp1.SO_PIN;
                                                            EncodeSOPIN encript = new EncodeSOPIN();
                                                            if(!"".equals(sSOPIN)) {
                                                                sSOPIN = encript.decode(sSOPIN);
                                                            }
                                                            Cell cellBranch2 = row1.createCell((short) 2);
                                                            cellBranch2.setCellValue(sSOPIN);
                                                            cellBranch2.setCellType(Cell.CELL_TYPE_STRING);
                                                            cellBranch2.setCellStyle(my_styleBranch);

                                                            i++;
                                                        }
                                                        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                                        wb.write(outByteStream);
                                                        byte[] outArray = outByteStream.toByteArray();
                                                        File someFile = new File(strURLPath);
                                                        FileOutputStream fos = new FileOutputStream(someFile);
                                                        fos.write(outArray);
                                                        fos.flush();
                                                        strView = "0#" + strURLPath + "#" + sFileName;
                                                    } else {
                                                        strView = "1#0";
                                                    }
                                                } else {
                                                    strView = "1#0";
                                                }
                                                break;
                                        }
                                    }
                                //</editor-fold>
                                break;
                            }
                            case "exportprofilemanagerlist": {
                                //<editor-fold defaultstate="collapsed" desc="exportprofilemanagerlist">
                                Config conf = new Config();
                                ConfigLanguage confLanguage = new ConfigLanguage();
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
//                                String SessRoleID_ID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
//                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
//                                String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                if (!directory.exists()){
                                    directory.mkdir();
                                }
                                String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_PROFILE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                String strURLPath = pPathURL + sFileName;
                                String vSum = EscapeUtils.CheckTextNull(request.getParameter("vSum"));
                                String ToCreateDate = request.getSession(false).getAttribute("sessMonthProfileCert").toString().trim();
                                String FromCreateDate = request.getSession(false).getAttribute("sessYearProfileCert").toString().trim();
                                String PERSONAL_NAME = request.getSession(false).getAttribute("sessPERSONAL_NAMEProfileCert").toString().trim();
                                String COMPANY_NAME = request.getSession(false).getAttribute("sessCOMPANY_NAMEProfileCert").toString().trim();
                                String TAX_CODE = request.getSession(false).getAttribute("sessTAX_CODEProfileCert").toString().trim();
                                String BUDGET_CODE = request.getSession(false).getAttribute("sessBUDGET_CODEProfileCert").toString().trim();
                                String DECISION = request.getSession(false).getAttribute("sessDECISIONProfileCert").toString().trim();
                                String P_ID = request.getSession(false).getAttribute("sessP_IDProfileCert").toString().trim();
                                String CCCD = request.getSession(false).getAttribute("sessCCCDProfileCert").toString().trim();
                                String PASSPORT = request.getSession(false).getAttribute("sessPASSPORTProfileCert").toString().trim();
                                String idCollectEnabled = request.getSession(false).getAttribute("sessCollectEnabledProfileCert").toString().trim();
                                String BranchOffice = request.getSession(false).getAttribute("sessBranchOfficeProfileCert").toString().trim();
                                String idCheckCompensation = request.getSession(false).getAttribute("sessCompensationProfileCert").toString().trim();
                                String idCheckOverdue = request.getSession(false).getAttribute("sessOverdueProfileCert").toString().trim();
                                String idCheckCommitEnabled = request.getSession(false).getAttribute("sessCommitEnabledCert").toString().trim();
                                String stateProfile = request.getSession(false).getAttribute("sessStateProfileProfileCert").toString().trim();
                                String ToReceiveDate = (String) request.getSession(false).getAttribute("sessToReceiveDateProfileCert");
                                String FromReceiveDate = (String) request.getSession(false).getAttribute("sessFromReceiveDateProfileCert");
                                String sConfirmResign = (String) request.getSession(false).getAttribute("sessResignProfileCert");
                                String sSignedEnabled = (String) request.getSession(false).getAttribute("sessSignedEnabledProfileCert");
                                String strAlertAllTimes = (String) request.getSession(false).getAttribute("AlertAllTimeSRenewCert");
                                String ToControlDate = (String) request.getSession(false).getAttribute("sessToControlProfileCert");
                                String FromControlDate = (String) request.getSession(false).getAttribute("sessFromControlProfileCert");
                                String strAlertAllControl = (String) request.getSession(false).getAttribute("AlertAllControlProfileCert");
                                if("1".equals(strAlertAllTimes)) {
                                    ToReceiveDate = "";
                                    FromReceiveDate = "";
                                }
                                String sEnterpriseCert = "";
                                String sPersonalCert = "";
                                String isUIDCollection = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_UID_COLLECTION_DISPLAY_ENABLED);
                                if(isUIDCollection.equals("1")) {
                                    String sENTERPRISE_PREFIX = (String) request.getSession(false).getAttribute("sessENTERPRISE_PREFIXProfileCert");
                                    String sPERSONAL_PREFIX = (String) request.getSession(false).getAttribute("sessPERSONAL_PREFIXProfileCert");
                                    String sENTERPRISE_ID = (String) request.getSession(false).getAttribute("sessENTERPRISE_IDProfileCert");
                                    String sPERSONAL_ID = (String) request.getSession(false).getAttribute("sessPERSONAL_IDProfileCert");
                                    if(!"".equals(sENTERPRISE_ID)){
                                        sEnterpriseCert = sENTERPRISE_PREFIX + "%"+sENTERPRISE_ID+"%";
                                    }
                                    if(!"".equals(sPERSONAL_ID)){
                                        sPersonalCert = sPERSONAL_PREFIX + "%"+sPERSONAL_ID+"%";
                                    }
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idCollectEnabled)) {
                                    idCollectEnabled = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                    BranchOffice = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(stateProfile)) {
                                    stateProfile = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(sConfirmResign)) {
                                    sConfirmResign = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(sSignedEnabled)) {
                                    sSignedEnabled = "";
                                }
                                if("1".equals(strAlertAllControl)) {
                                    ToControlDate = "";
                                    FromControlDate = "";
                                } else {
                                    ToCreateDate = "";
                                    FromCreateDate = "";
                                }
                                String SessUserID = "";
                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                String pBRANCH_BENEFICIARY_ID = SessUserAgentID;
                                if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
                                    pBRANCH_BENEFICIARY_ID=EscapeUtils.escapeHtmlSearch(BranchOffice);
                                }
                                CommonFunction.LogDebugString(log, "ExportReportCertList", "vSum: " + vSum
                                    + "; Month: " + ToCreateDate + "; Year: " + FromCreateDate);
                                CERTIFICATION[][] rsReportBranch = new CERTIFICATION[1][];
                                if(!"".equals(vSum)) {
                                    if(Integer.parseInt(vSum) > 0) {
                                        if("0".equals(ToCreateDate)){ToCreateDate = "";}
                                        if(!isUIDCollection.equals("1")) {
                                            String[] sUIDResult = new String[2];
                                            CommonReferServlet.collectFieldToUID(TAX_CODE, BUDGET_CODE, DECISION, P_ID, PASSPORT, EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                            sEnterpriseCert = sUIDResult[0];
                                            sPersonalCert = sUIDResult[1];
                                        }
                                        com.S_BO_CERTIFICATION_BRIEF_LIST_EXPORT(COMPANY_NAME, PERSONAL_NAME, idCollectEnabled,
                                            EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            BranchOffice, idCheckCompensation, idCheckOverdue, SessUserID, loginLanguage, rsReportBranch, idCheckCommitEnabled, stateProfile,
                                            EscapeUtils.escapeHtmlSearch(FromReceiveDate), EscapeUtils.escapeHtmlSearch(ToReceiveDate),
                                            sConfirmResign, sSignedEnabled, sEnterpriseCert, sPersonalCert, pBRANCH_BENEFICIARY_ID, FromControlDate, ToControlDate);
                                    }
                                }
                                if (rsReportBranch[0] != null && rsReportBranch[0].length > 0) {
                                    String queryString = getServletContext().getRealPath("/");
                                    String outputDirectory = queryString;
                                    String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                    FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                    XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                    inputStream.close();
                                    
                                    String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, loginLanguage).trim();
                                    String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, loginLanguage).trim();
                                    String sCellUserCreate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_USER_CREATE, loginLanguage).trim();
                                    String sCellMST = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_ENTERPRISE, loginLanguage).trim();
                                    String sCellCompany = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COMPANY_NAME, loginLanguage).trim();
                                    String sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL, loginLanguage).trim();
                                    String sCellPesonal = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PERSONAL_NAME, loginLanguage).trim();
                                    String sCellProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICEPACK_NAME, loginLanguage).trim();
                                    String sCellRequestType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REQUEST_TYPE, loginLanguage).trim();
                                    String sCellMonthControl = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_MONTH_CONTROL, loginLanguage);
                                    String sCellStateProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_STATUS, loginLanguage);
                                    String sCellRecordProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_FORM_SEIZE, loginLanguage);
                                    String sCellTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_TYPE, loginLanguage);
                                    String sCellAmountFine = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_FINE_AMOUNT, loginLanguage);
                                    String sCellDateReceive = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_PROFILE_RECEIVE, loginLanguage);
                                    String sCellPhoneContract = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PHONE_CONTACT, loginLanguage).trim();
                                    String sCellEmailContract = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_EMAIL_CONTACT, loginLanguage).trim();
                                    String sCellTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_SN, loginLanguage).trim();
                                    String sCellCertSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, loginLanguage);
                                    String sCelNameContract = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CONTACT_NAME, loginLanguage);
                                    String sCelPhoneRepresent = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_PHONE, loginLanguage);
                                    String sCelEmailRepresent = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_EMAIL, loginLanguage);
                                    String sCelNameRepresent = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REPRESENTATIVE_NAME, loginLanguage);
                                    String sCelCheckRegister = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REGISTER_USE, loginLanguage);
                                    String sCelCheckConfirm = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERTIFICATION, loginLanguage);
                                    String sCelCheckGPKD = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REGISTER_BUSSINESS, loginLanguage);
                                    String sCelCheckCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_IDENTITY_CARD, loginLanguage);
                                    
                                    String sCelTokenState = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_STATUS, loginLanguage);
                                    String sCelTokenWaitLock = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_WAIT_LOCK, loginLanguage);
                                    String sCelTokenWaitUnlock = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_WAIT_UNLOCK, loginLanguage);
                                    String sCelAddress = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_ADDRESS, loginLanguage);
                                    String sCelNoteProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_NOTE_PROFILE, loginLanguage);
                                    String sCelReasonRevoke = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REASON_REVOKE, loginLanguage);
                                    
                                    String isCALoad = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_WHICH_ABOUT_CA);
                                    SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                    wb.setCompressTempFiles(true);
                                    CreationHelper createHelper = wb.getCreationHelper();
                                    SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);//wb.createSheet();
                                    sheet.setRandomAccessWindowSize(100);
                                    sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                    sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                    sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                    sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                    sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                    sheet.setColumnWidth(5, 24*255);
                                    sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                    sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                    sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                    sheet.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                    sheet.setColumnWidth(10, 18 * 255);
                                    sheet.setColumnWidth(11, 18 * 255);
                                    sheet.setColumnWidth(12, 18 * 255);
                                    sheet.setColumnWidth(13, 18 * 255);
                                    sheet.setColumnWidth(14, 18 * 255);
                                    sheet.setColumnWidth(15, 24 * 255);
                                    sheet.setColumnWidth(16, 18 * 255);
                                    sheet.setColumnWidth(17, 18 * 255);
                                    sheet.setColumnWidth(18, 18 * 255);
                                    sheet.setColumnWidth(19, 18 * 255);
                                    sheet.setColumnWidth(20, 18 * 255);
                                    sheet.setColumnWidth(21, 18 * 255);
                                    sheet.setColumnWidth(22, 18 * 255);
                                    sheet.setColumnWidth(23, 18 * 255);
                                    sheet.setColumnWidth(24, 18 * 255);
                                    sheet.setColumnWidth(25, 18 * 255);
                                    sheet.setColumnWidth(26, 18 * 255);
                                    sheet.setColumnWidth(27, 18 * 255);
                                    sheet.setColumnWidth(28, 24 * 255);
                                    sheet.setColumnWidth(29, 24 * 255);
                                    sheet.setColumnWidth(30, 24 * 255);
                                    sheet.setColumnWidth(31, 32 * 255);
                                    sheet.setColumnWidth(32, 32 * 255);
                                    Row row = sheet.createRow(0);

                                    CellStyle my_style = wb.createCellStyle();
                                    Font font = wb.createFont();
                                    font.setBoldweight((short) 700);
                                    font.setFontName("Arial");
                                    my_style.setFont(font);
                                    my_style.setFillBackgroundColor((short) 9);
                                    my_style.setAlignment((short) 2);
                                    Cell cell = row.createCell((short) 0);
                                    cell.setCellValue(sCellSTT);
                                    cell.setCellStyle(my_style);

                                    Cell cell1 = row.createCell((short) 1);
                                    cell1.setCellValue(sCellAgency);
                                    cell1.setCellStyle(my_style);

                                    Cell cell2 = row.createCell((short) 2);
                                    cell2.setCellValue(sCellUserCreate);
                                    cell2.setCellStyle(my_style);

                                    Cell cell3 = row.createCell((short) 3);
                                    cell3.setCellValue(sCellMST);
                                    cell3.setCellStyle(my_style);

                                    Cell cell5 = row.createCell((short) 4);
                                    cell5.setCellValue(sCellCompany);
                                    cell5.setCellStyle(my_style);
                                    
                                    Cell cell56 = row.createCell((short) 5);
                                    cell56.setCellValue(sCelAddress);
                                    cell56.setCellStyle(my_style);

                                    Cell cell6 = row.createCell((short) 6);
                                    cell6.setCellValue(sCellCMND);
                                    cell6.setCellStyle(my_style);

                                    Cell cell7 = row.createCell((short) 7);
                                    cell7.setCellValue(sCellPesonal);
                                    cell7.setCellStyle(my_style);

                                    Cell cell9 = row.createCell((short) 8);
                                    cell9.setCellValue(sCellProfile);
                                    cell9.setCellStyle(my_style);

                                    Cell cell10 = row.createCell((short) 9);
                                    cell10.setCellValue(sCellRequestType);
                                    cell10.setCellStyle(my_style);

                                    Cell cell11 = row.createCell((short) 10);
                                    cell11.setCellValue(sCellMonthControl);
                                    cell11.setCellStyle(my_style);

                                    Cell cell12 = row.createCell((short) 11);
                                    cell12.setCellValue(sCellStateProfile);
                                    cell12.setCellStyle(my_style);

                                    Cell cell13 = row.createCell((short) 12);
                                    cell13.setCellValue(sCellRecordProfile);
                                    cell13.setCellStyle(my_style);
                                    int intAscending = 1;
                                    if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                        Cell cell14 = row.createCell((short) 13);
                                        cell14.setCellValue(sCellTypeProfile);
                                        cell14.setCellStyle(my_style);
                                        intAscending = 0;
                                    }

                                    Cell cell15 = row.createCell((short) 14 - intAscending);
                                    cell15.setCellValue(sCellAmountFine);
                                    cell15.setCellStyle(my_style);

                                    Cell cell16 = row.createCell((short) 15 - intAscending);
                                    cell16.setCellValue(sCellDateReceive);
                                    cell16.setCellStyle(my_style);

                                    Cell cell1612 = row.createCell((short) 16 - intAscending);
                                    cell1612.setCellValue(sCellCertSN);
                                    cell1612.setCellStyle(my_style);

                                    Cell cell1613 = row.createCell((short) 17 - intAscending);
                                    cell1613.setCellValue(sCellTokenSN);
                                    cell1613.setCellStyle(my_style);

                                    Cell cell17 = row.createCell((short) 18 - intAscending);
                                    cell17.setCellValue(sCellPhoneContract);
                                    cell17.setCellStyle(my_style);

                                    Cell cell18 = row.createCell((short) 19 - intAscending);
                                    cell18.setCellValue(sCellEmailContract);
                                    cell18.setCellStyle(my_style);

                                    Cell cell19 = row.createCell((short) 20 - intAscending);
                                    cell19.setCellValue(sCelNameContract);
                                    cell19.setCellStyle(my_style);

                                    Cell cell20 = row.createCell((short) 21 - intAscending);
                                    cell20.setCellValue(sCelPhoneRepresent);
                                    cell20.setCellStyle(my_style);

                                    Cell cell21 = row.createCell((short) 22 - intAscending);
                                    cell21.setCellValue(sCelEmailRepresent);
                                    cell21.setCellStyle(my_style);

                                    Cell cell22 = row.createCell((short) 23 - intAscending);
                                    cell22.setCellValue(sCelNameRepresent);
                                    cell22.setCellStyle(my_style);

                                    Cell cell23 = row.createCell((short) 24 - intAscending);
                                    cell23.setCellValue(sCelCheckRegister);
                                    cell23.setCellStyle(my_style);

                                    Cell cell24 = row.createCell((short) 25 - intAscending);
                                    cell24.setCellValue(sCelCheckConfirm);
                                    cell24.setCellStyle(my_style);

                                    Cell cell25 = row.createCell((short) 26 - intAscending);
                                    cell25.setCellValue(sCelCheckGPKD);
                                    cell25.setCellStyle(my_style);

                                    Cell cell26 = row.createCell((short) 27 - intAscending);
                                    cell26.setCellValue(sCelCheckCMND);
                                    cell26.setCellStyle(my_style);
                                    
                                    Cell cell27 = row.createCell((short) 28 - intAscending);
                                    cell27.setCellValue(sCelTokenState);
                                    cell27.setCellStyle(my_style);
                                    
                                    Cell cell28 = row.createCell((short) 29 - intAscending);
                                    cell28.setCellValue(sCelTokenWaitLock);
                                    cell28.setCellStyle(my_style);
                                    
                                    Cell cell29= row.createCell((short) 30 - intAscending);
                                    cell29.setCellValue(sCelTokenWaitUnlock);
                                    cell29.setCellStyle(my_style);
                                    
                                    Cell cell31= row.createCell((short) 31 - intAscending);
                                    cell31.setCellValue(sCelNoteProfile);
                                    cell31.setCellStyle(my_style);
                                    
                                    Cell cell32= row.createCell((short) 32 - intAscending);
                                    cell32.setCellValue(sCelReasonRevoke);
                                    cell32.setCellStyle(my_style);
                                    
                                    Font fontBranch = wb.createFont();
                                    fontBranch.setFontName("Arial");
                                    CellStyle styleText = wb.createCellStyle();
                                    styleText.setFont(fontBranch);
                                    styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));

                                    int i = 1;
                                    int k = 0;
                                    for (CERTIFICATION rsReportBranch1 : rsReportBranch[0]) {
                                        if (k == 0) {
                                            k = 1;
                                        } else {
                                            k++;
                                        }
                                        Row row1 = sheet.createRow(Integer.valueOf(i));
                                        CellStyle my_styleBranch = wb.createCellStyle();
                                        my_styleBranch.setFont(fontBranch);
                                        
                                        CellStyle my_styleBranchDate = wb.createCellStyle();
                                        Font fontBranchDate = wb.createFont();
                                        fontBranchDate.setFontName("Arial");
                                        my_styleBranchDate.setFont(fontBranchDate);
                                        my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));

                                        Cell cellBranch = row1.createCell((short) 0);
                                        cellBranch.setCellValue(k);
                                        cellBranch.setCellStyle(my_styleBranch);

                                        Cell cellBranch1 = row1.createCell((short) 1);
                                        cellBranch1.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_DESC));
                                        cellBranch1.setCellStyle(my_styleBranch);

                                        Cell cellBranch2 = row1.createCell((short) 2);
                                        cellBranch2.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CREATED_BY));
                                        cellBranch2.setCellStyle(my_styleBranch);

                                        String sMSTAndBUDGET_CODE = rsReportBranch1.ENTERPRISE_ID;// EscapeUtils.CheckTextNull(rsReportBranch1.TAX_CODE);
//                                        if ("".equals(sMSTAndBUDGET_CODE)) {
//                                            sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.BUDGET_CODE);
//                                        }
//                                        if ("".equals(sMSTAndBUDGET_CODE)) {
//                                            sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.DECISION);
//                                        }
                                        Cell cellBranch3 = row1.createCell((short) 3);
                                        cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                        cellBranch3.setCellStyle(styleText);

                                        Cell cellBranch5 = row1.createCell((short) 4);
                                        cellBranch5.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.COMPANY_NAME));
                                        cellBranch5.setCellStyle(my_styleBranch);
                                        
                                        String sREPRESENTATIVE_EMAIL = "";
                                        String sREPRESENTATIVE_PHONE = "";
                                        String sREPRESENTATIVE_NAME = "";
                                        String sCONTACT_NAME = "";
                                        String sADDRESS = "";
                                        String sPrfileContact = EscapeUtils.CheckTextNull(rsReportBranch1.PROFILE_CONTACT_INFO);
                                        if(!"".equals(sPrfileContact)) {
                                            ObjectMapper oMapperParse = new ObjectMapper();
                                            ProfileContactInfoJson profileContact = oMapperParse.readValue(sPrfileContact, ProfileContactInfoJson.class);
                                            if(profileContact != null) {
                                                sREPRESENTATIVE_EMAIL = profileContact.RepresentativeEmail;
                                                sREPRESENTATIVE_PHONE = profileContact.RepresentativePhone;
                                                sREPRESENTATIVE_NAME = CommonFunction.replaceCharaterSpecialJsonCSV(profileContact.RepresentativeName);
                                                sCONTACT_NAME = CommonFunction.replaceCharaterSpecialJsonCSV(profileContact.ContactName);
                                                sADDRESS = CommonFunction.replaceCharaterSpecialJsonCSV(profileContact.Address);
                                            }
                                        }
                                        
                                        Cell cellAddress = row1.createCell((short) 5);
                                        cellAddress.setCellValue(sADDRESS);
                                        cellAddress.setCellStyle(my_styleBranch);

                                        String sP_IDAndPASSPORT = rsReportBranch1.PERSONAL_ID;// EscapeUtils.CheckTextNull(rsReportBranch1.P_ID);
//                                        if ("".equals(sP_IDAndPASSPORT)) {
//                                            sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.P_EID);
//                                        }
//                                        if ("".equals(sP_IDAndPASSPORT)) {
//                                            sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.PASSPORT);
//                                        }
                                        Cell cellBranch6 = row1.createCell((short) 6);
                                        cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                        cellBranch6.setCellStyle(styleText);

                                        Cell cellBranch7 = row1.createCell((short) 7);
                                        cellBranch7.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PERSONAL_NAME));
                                        cellBranch7.setCellStyle(my_styleBranch);

                                        Cell cellBranch9 = row1.createCell((short) 8);
                                        cellBranch9.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_PROFILE_NAME));
                                        cellBranch9.setCellStyle(my_styleBranch);

                                        Cell cellBranch10 = row1.createCell((short) 9);
                                        cellBranch10.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_ATTR_TYPE_DESC));
                                        cellBranch10.setCellStyle(my_styleBranch);

                                        Cell cellBranch11 = row1.createCell((short) 10);
                                        cellBranch11.setCellValue(rsReportBranch1.CROSS_CHECKED_MOUNTH);
                                        cellBranch11.setCellStyle(my_styleBranch);

                                        String sStateProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_LACK, loginLanguage);
                                        if(rsReportBranch1.COLLECT_ENABLED == true) {
                                            sStateProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_ENOUGH, loginLanguage);
                                        }
                                        Cell cellBranch12 = row1.createCell((short) 11);
                                        cellBranch12.setCellValue(sStateProfile);
                                        cellBranch12.setCellStyle(my_styleBranch);

                                        String sRecordProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROFILE_OF_MONTH, loginLanguage);
                                        if(rsReportBranch1.BRIEF_TYPE == true) {
                                            sRecordProfile =confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_OUT_PROFILE, loginLanguage);
                                        }
                                        
                                        Cell cellBranch13 = row1.createCell((short) 12);
                                        cellBranch13.setCellValue(sRecordProfile);
                                        cellBranch13.setCellStyle(my_styleBranch);
                                        intAscending = 1;
                                        if(!isCALoad.equals(Definitions.CONFIG_IS_WHICH_ABOUT_CA_ICA)) {
                                            String sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SOFT_VERSION, loginLanguage);
                                            if(rsReportBranch1.COLLECT_SOFTCOPY == true) {
                                                sTypeProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_HARD_VERSION, loginLanguage);
                                            }

                                            Cell cellBranch14123 = row1.createCell((short) 13);
                                            cellBranch14123.setCellValue(sTypeProfile);
                                            cellBranch14123.setCellStyle(my_styleBranch);
                                            intAscending = 0;
                                        }
                                        
                                        Cell cellBranch15222 = row1.createCell((short) 14 - intAscending);
                                        cellBranch15222.setCellValue(rsReportBranch1.FINE_FOR_LACK_OF_BRIEF);
                                        cellBranch15222.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch14 = row1.createCell((short) 15 - intAscending);
                                        Date dateReceive = CommonFunction.convertStringToDate(rsReportBranch1.RECEIVED_BRIEF_DATE, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                        if(dateReceive != null) {
                                            cellBranch14.setCellValue(dateReceive);
                                        } else {
                                            cellBranch14.setCellValue("");
                                        }
                                        cellBranch14.setCellStyle(my_styleBranchDate);
                                        
                                        Cell cellBranch191 = row1.createCell((short) 16 - intAscending);
                                        cellBranch191.setCellValue(rsReportBranch1.CERTIFICATION_SN);
                                        cellBranch191.setCellStyle(styleText);
                                        
                                        Cell cellBranch192 = row1.createCell((short) 17 - intAscending);
                                        cellBranch192.setCellValue(rsReportBranch1.TOKEN_SN);
                                        cellBranch192.setCellStyle(styleText);
                                        
                                        Cell cellBranch15 = row1.createCell((short) 18 - intAscending);
                                        cellBranch15.setCellValue(rsReportBranch1.PHONE_CONTRACT);
                                        cellBranch15.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch16 = row1.createCell((short) 19 - intAscending);
                                        cellBranch16.setCellValue(rsReportBranch1.EMAIL_CONTRACT);
                                        cellBranch16.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch17 = row1.createCell((short) 20 - intAscending);
                                        cellBranch17.setCellValue(sCONTACT_NAME);
                                        cellBranch17.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch18 = row1.createCell((short) 21 - intAscending);
                                        cellBranch18.setCellValue(sREPRESENTATIVE_PHONE);
                                        cellBranch18.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch19 = row1.createCell((short) 22 - intAscending);
                                        cellBranch19.setCellValue(sREPRESENTATIVE_EMAIL);
                                        cellBranch19.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch20 = row1.createCell((short) 23 - intAscending);
                                        cellBranch20.setCellValue(sREPRESENTATIVE_NAME);
                                        cellBranch20.setCellStyle(my_styleBranch);
                                        
                                        String sRegister = "";
                                        String sConfirm = "";
                                        String sDKKD = "";
                                        String sCMND = "";
                                        String sBRIEF_PROPERTIES = rsReportBranch1.BRIEF_PROPERTIES;
                                        if(!"".equals(sBRIEF_PROPERTIES))
                                        {
                                            CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
                                            CommonFunction.getCollectedBriefProperties(sBRIEF_PROPERTIES, resIPData);
                                            if(resIPData[0].length > 0) {
                                                boolean bRegister = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_SERVICE_REGISTRATION_DOCUMENT, resIPData);
                                                if(bRegister == true){sRegister = "x";}
                                                boolean bConfirm = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_MINUTES_OF_HANDOVER, resIPData);
                                                if(bConfirm == true){sConfirm = "x";}
                                                boolean bDKKD = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION, resIPData);
                                                if(bDKKD == true){sDKKD = "x";}
                                                boolean bCMND = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD, resIPData);
                                                if(bCMND == true){sCMND = "x";}
                                            }
                                        }
                                        Cell cellBranch21 = row1.createCell((short) 24 - intAscending);
                                        cellBranch21.setCellValue(sRegister);
                                        cellBranch21.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch22 = row1.createCell((short) 25 - intAscending);
                                        cellBranch22.setCellValue(sConfirm);
                                        cellBranch22.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch23 = row1.createCell((short) 26 - intAscending);
                                        cellBranch23.setCellValue(sDKKD);
                                        cellBranch23.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch24 = row1.createCell((short) 27 - intAscending);
                                        cellBranch24.setCellValue(sCMND);
                                        cellBranch24.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch27 = row1.createCell((short) 28 - intAscending);
                                        cellBranch27.setCellValue(rsReportBranch1.TOKEN_STATE_DESC);
                                        cellBranch27.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch28 = row1.createCell((short) 29 - intAscending);
                                        cellBranch28.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_LOCK) ? "x" : "");
                                        cellBranch28.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch29 = row1.createCell((short) 30 - intAscending);
                                        cellBranch29.setCellValue(!"".equals(rsReportBranch1.TOKEN_WAIT_TO_UNLOCK) ? "x" : "");
                                        cellBranch29.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch31 = row1.createCell((short) 31 - intAscending);
                                        cellBranch31.setCellValue(rsReportBranch1.PROFILE_NOTE);
                                        cellBranch31.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch32 = row1.createCell((short) 32 - intAscending);
                                        cellBranch32.setCellValue(rsReportBranch1.REVOCATION_REASON);
                                        cellBranch32.setCellStyle(my_styleBranch);
                                        
                                        i++;
                                    }
                                    ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                    wb.write(outByteStream);
                                    byte[] outArray = outByteStream.toByteArray();
                                    File someFile = new File(strURLPath);
                                    FileOutputStream fos = new FileOutputStream(someFile);
                                    fos.write(outArray);
                                    fos.flush();
                                    strView = "0#" + strURLPath + "#" + sFileName;
                                } else {
                                    strView = "2#1";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "exporttokenlist": {
                                //<editor-fold defaultstate="collapsed" desc="exporttokenlist">
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                Config conf = new Config();
                                ConfigLanguage confLanguage = new ConfigLanguage();
                                String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                if (!directory.exists()) {
                                    directory.mkdir();
                                }
                                String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_CERTIFICATE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                String strURLPath = pPathURL + sFileName;
                                String strAlertAllTimes = (String) request.getSession(false).getAttribute("AlertAllTimeSUserlist");
                                String FromDateValid = (String) request.getSession(false).getAttribute("FromCreateDateSTokenlist");
                                String ToDateValid = (String) request.getSession(false).getAttribute("ToCreateDateSTokenlist");
                                String TOKEN_ID = (String) request.getSession(false).getAttribute("sessTOKEN_ID");
                                String TOKEN_STATE = (String) request.getSession(false).getAttribute("sessTOKEN_STATE");
                                String TOKEN_VERSION = (String) request.getSession(false).getAttribute("sessTOKEN_VERSION");
                                String AGENT_ID = (String) request.getSession(false).getAttribute("sessAGENT_ID");
                                String TAX_CODE = (String) request.getSession(false).getAttribute("sessTAX_CODESTokenlist");
                                String BUDGET_CODE = (String) request.getSession(false).getAttribute("sessBUDGET_CODESTokenlist");
                                String DECISION = (String) request.getSession(false).getAttribute("sessDECISIONSTokenlist");
                                String P_ID = (String) request.getSession(false).getAttribute("sessP_IDSTokenlist");
                                String CCCD = (String) request.getSession(false).getAttribute("sessCCCDSTokenlist");
                                String PASSPORT = (String) request.getSession(false).getAttribute("sessPASSPORTSTokenlist");
                                String TOKEN_SIGNED = (String) request.getSession(false).getAttribute("sessTOKEN_SIGNED");
                                String PHONE_CONTACT = (String) request.getSession(false).getAttribute("sessPHONE_CONTACTSTokenlist");
                                String EMAIL_CONTACT = (String) request.getSession(false).getAttribute("sessEMAIL_CONTACTSTokenlist");
                                String sEnterpriseCert = "";
                                String sPersonalCert = "";
                                String isUIDCollection = LoadParamSystem.getParamStart(Definitions.CONFIG_IS_UID_COLLECTION_DISPLAY_ENABLED);
                                if(isUIDCollection.equals("1")) {
                                    String sENTERPRISE_PREFIX = (String) request.getSession(false).getAttribute("sessENTERPRISE_PREFIXTokenlist");
                                    String sPERSONAL_PREFIX = (String) request.getSession(false).getAttribute("sessPERSONAL_PREFIXTokenlist");
                                    String sENTERPRISE_ID = (String) request.getSession(false).getAttribute("sessENTERPRISE_IDTokenlist");
                                    String sPERSONAL_ID = (String) request.getSession(false).getAttribute("sessPERSONAL_IDTokenlist");
                                    if(!"".equals(sENTERPRISE_ID)){
                                        sEnterpriseCert = sENTERPRISE_PREFIX + sENTERPRISE_ID;
                                    }
                                    if(!"".equals(sPERSONAL_ID)){
                                        sPersonalCert = sPERSONAL_PREFIX + sPERSONAL_ID;
                                    }
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_STATE)) {
                                    TOKEN_STATE = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_VERSION)) {
                                    TOKEN_VERSION = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_SIGNED)) {
                                    TOKEN_SIGNED = "";
                                }
                                if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
                                    AGENT_ID = SessUserAgentID;
                                } else {
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
                                        AGENT_ID = "";
                                    }
                                }
                                if("1".equals(strAlertAllTimes)) {
                                    FromDateValid = "";
                                    ToDateValid = "";
                                }
                                CommonFunction.LogDebugString(log, "ExportTokenList", "FromDate: " + FromDateValid + "; ToDate: " + ToDateValid + "; AGENCY: " + AGENT_ID);
                                TOKEN[][] rsPgin = new TOKEN[1][];
                                if (!"".equals(countList)) {
                                    if(!isUIDCollection.equals("1")) {
                                        String[] sUIDResult = new String[2];
                                        CommonReferServlet.collectFieldToUID(EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                            EscapeUtils.escapeHtmlSearch(DECISION), EscapeUtils.escapeHtmlSearch(P_ID),
                                            EscapeUtils.escapeHtmlSearch(PASSPORT), EscapeUtils.escapeHtmlSearch(CCCD), sUIDResult);
                                        sEnterpriseCert = sUIDResult[0];
                                        sPersonalCert = sUIDResult[1];
                                    }
                                    String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                    com.S_BO_TOKEN_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                        EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(TOKEN_STATE),
                                        EscapeUtils.escapeHtmlSearch(TOKEN_VERSION), EscapeUtils.escapeHtmlSearch(AGENT_ID), loginLanguage,
                                        rsPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO, Integer.parseInt(countList),
                                        TAX_CODE, BUDGET_CODE, P_ID, PASSPORT, CCCD,
                                        EscapeUtils.escapeHtmlSearch(TOKEN_SIGNED), EscapeUtils.escapeHtmlSearch(PHONE_CONTACT),
                                        EscapeUtils.escapeHtmlSearch(EMAIL_CONTACT), sessTreeArrayBranchID,
                                        DECISION, sEnterpriseCert, sPersonalCert);
                                    if(rsPgin[0].length > 0) {
                                        String queryString = getServletContext().getRealPath("/");
                                        String outputDirectory = queryString;
                                        String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                        FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                        inputStream.close();

                                        String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, loginLanguage).trim();
                                        String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, loginLanguage).trim();
                                        String sCellSTATE = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_STATUS, loginLanguage).trim();
                                        String sCellDateCreate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CREATE, loginLanguage).trim();
                                        String sCellToken = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_SN, loginLanguage).trim();
                                        SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                        wb.setCompressTempFiles(true);
                                        CreationHelper createHelper = wb.getCreationHelper();
                                        SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
                                        sheet.setRandomAccessWindowSize(100);
                                        sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                        sheet.setColumnWidth(1, 24*255);
                                        sheet.setColumnWidth(2, 24*255);
                                        sheet.setColumnWidth(3, 24*255);
                                        sheet.setColumnWidth(4, 24*255);
                                        
                                        Row row = sheet.createRow(0);
                                        CellStyle my_style = wb.createCellStyle();
                                        Font font = wb.createFont();
                                        font.setBoldweight((short) 700);
                                        font.setFontName("Arial");
                                        my_style.setFont(font);
                                        my_style.setFillBackgroundColor((short) 9);
                                        my_style.setAlignment((short) 2);
                                        Cell cell = row.createCell((short) 0);
                                        cell.setCellValue(sCellSTT);
                                        cell.setCellStyle(my_style);

                                        Cell cell5 = row.createCell((short) 1);
                                        cell5.setCellValue(sCellToken);
                                        cell5.setCellStyle(my_style);

                                        Cell cell3 = row.createCell((short) 2);
                                        cell3.setCellValue(sCellSTATE);
                                        cell3.setCellStyle(my_style);

                                        Cell cell7 = row.createCell((short) 3);
                                        cell7.setCellValue(sCellAgency);
                                        cell7.setCellStyle(my_style);

                                        Cell cell8 = row.createCell((short) 4);
                                        cell8.setCellValue(sCellDateCreate);
                                        cell8.setCellStyle(my_style);
                                        
                                        Font fontBranch = wb.createFont();
                                        fontBranch.setFontName("Arial");
                                        CellStyle styleText = wb.createCellStyle();
                                        styleText.setFont(fontBranch);
                                        styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));

                                        int i = 1;
                                        int k = 0;
                                        for (TOKEN temp1 : rsPgin[0]) {
                                            if (k == 0) {
                                                k = 1;
                                            } else {
                                                k++;
                                            }
                                            Row row1 = sheet.createRow(Integer.valueOf(i));
                                            CellStyle my_styleBranch = wb.createCellStyle();
                                            my_styleBranch.setFont(fontBranch);

                                            CellStyle my_styleBranchDate = wb.createCellStyle();
                                            Font fontBranchDate = wb.createFont();
                                            fontBranchDate.setFontName("Arial");
                                            my_styleBranchDate.setFont(fontBranchDate);
                                            my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS));
                                            
                                            Cell cellBranch = row1.createCell((short) 0);
                                            cellBranch.setCellValue(k);
                                            cellBranch.setCellStyle(my_styleBranch);

                                            Cell cellBranch1 = row1.createCell((short) 1);
                                            cellBranch1.setCellValue(EscapeUtils.CheckTextNull(temp1.TOKEN_SN));
                                            cellBranch1.setCellStyle(styleText);

                                            Cell cellBranch2 = row1.createCell((short) 2);
                                            cellBranch2.setCellValue(EscapeUtils.CheckTextNull(temp1.TOKEN_STATE_DESC));
                                            cellBranch2.setCellStyle(my_styleBranch);

                                            Cell cellBranch3 = row1.createCell((short) 3);
                                            cellBranch3.setCellValue(EscapeUtils.CheckTextNull(temp1.BRANCH_DESC));
                                            cellBranch3.setCellStyle(my_styleBranch);

//                                            String sDateEx = EscapeUtils.CheckTextNull(temp1.EXPORT_DATE_TYPE);
                                            /*SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss");
                                            Date dDateEx = new Date();
                                            if(!"".equals(sDateEx)) {
                                                dDateEx = formatter.parse(sDateEx);
                                            }*/
//                                            java.sql.Timestamp timestamp = temp1.EXPORT_DATE_TYPE;
//                                            Calendar calendar = Calendar.getInstance();
//                                            calendar.setTimeInMillis(timestamp.getTime());
//                                            timestamp = new Timestamp(calendar.getTimeInMillis());
//                                            System.out.println("AFTER "+timestamp.toString());
                                            
//                                            SimpleDateFormat datetemp = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
//                                            Date cellValue = datetemp.parse(temp1.EXPORT_DATE_TYPE);
                                            
                                            //1. Create the date cell style
//                                            CreationHelper createHelper = wb.getCreationHelper();
//                                            CellStyle cellStyle = wb.createCellStyle();
//                                            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("MMMM dd, yyyy"));
                                            Cell cellBranch4 = row1.createCell((short) 4);
                                            Date dateGen = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(temp1.CREATED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                            if(dateGen != null) {
                                                cellBranch4.setCellValue(dateGen);
                                            } else {
                                                cellBranch4.setCellValue("");
                                            }
                                            cellBranch4.setCellStyle(my_styleBranchDate);
                                            i++;
                                        }
                                        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                        wb.write(outByteStream);
                                        byte[] outArray = outByteStream.toByteArray();
                                        File someFile = new File(strURLPath);
                                        FileOutputStream fos = new FileOutputStream(someFile);
                                        fos.write(outArray);
                                        fos.flush();
                                        strView = "0#" + strURLPath + "#" + sFileName;
                                    } else {
                                        strView = "2#1";
                                    }
                                } else {
                                    strView = "2#1";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "exportagencylist": {
                                //<editor-fold defaultstate="collapsed" desc="exportagencylist">
                                Config conf = new Config();
                                String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                if (!directory.exists()){
                                    directory.mkdir();
                                }
                                String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_AGENCY_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                String strURLPath = pPathURL + sFileName;
                                String vSum = EscapeUtils.CheckTextNull(request.getParameter("vSum"));
                                String ToCreateDate = request.getSession(false).getAttribute("sessToCreateDateBranchList").toString().trim();
                                String FromCreateDate = request.getSession(false).getAttribute("sessFromCreateDateBranchList").toString().trim();
                                String NAME = request.getSession(false).getAttribute("sessNAMEBranchList").toString().trim();
                                String CITY_PROVINCE = request.getSession(false).getAttribute("sessCITY_PROVINCEBranchList").toString().trim();
                                String strAlertAllTimes = request.getSession(false).getAttribute("AlertAllTimeSBranchList").toString().trim();
                                String sessTreeArrayBranchID = request.getSession(false).getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                String sessLanguageGlobal = request.getSession(false).getAttribute("sessVN").toString();
                                
                                if("1".equals(strAlertAllTimes)) {
                                    ToCreateDate = "";
                                    FromCreateDate = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(CITY_PROVINCE)) {
                                    CITY_PROVINCE = "";
                                }
                                BRANCH[][] rsReportBranch = new BRANCH[1][];
                                if(!"".equals(vSum)) {
                                    if(Integer.parseInt(vSum) > 0) {
                                        com.S_BO_BRANCH_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(NAME), EscapeUtils.escapeHtmlSearch(CITY_PROVINCE),
                                            sessLanguageGlobal, sessTreeArrayBranchID, rsReportBranch, Definitions.CONFIG_GRID_INT_PAGNO, Integer.parseInt(vSum));
                                    }
                                }
                                if (rsReportBranch[0] != null && rsReportBranch[0].length > 0) {
                                    String queryString = getServletContext().getRealPath("/");
                                    String outputDirectory = queryString;
                                    String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                    FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                    XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                    inputStream.close();
                                    
                                    String sCellSTT = "No";
                                    String sCellAgencyCode = "Agency Code";
                                    String sCellAgencyName = "Agency Name";
                                    String sCellPhoneNumber = "Phone Number";
                                    String sCellEmail = "Email";
                                    String sCellAddress = "Address";
                                    String sCellParent = "Management agent";
                                    String sCellLevel = "Agent hierarchy";
                                    String sCellCreateDate = "Create Date";
                                    
                                    SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                    wb.setCompressTempFiles(true);
                                    CreationHelper createHelper = wb.getCreationHelper();
                                    SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);//wb.createSheet();
                                    sheet.setRandomAccessWindowSize(100);
                                    sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                    sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                    sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                    sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                    sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                    sheet.setColumnWidth(5, 24*255);
                                    sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                    sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                    sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                    Row row = sheet.createRow(0);

                                    CellStyle my_style = wb.createCellStyle();
                                    Font font = wb.createFont();
                                    font.setBoldweight((short) 700);
                                    font.setFontName("Arial");
                                    my_style.setFont(font);
                                    my_style.setFillBackgroundColor((short) 9);
                                    my_style.setAlignment((short) 2);
                                    Cell cell = row.createCell((short) 0);
                                    cell.setCellValue(sCellSTT);
                                    cell.setCellStyle(my_style);

                                    Cell cell1 = row.createCell((short) 1);
                                    cell1.setCellValue(sCellAgencyCode);
                                    cell1.setCellStyle(my_style);

                                    Cell cell2 = row.createCell((short) 2);
                                    cell2.setCellValue(sCellAgencyName);
                                    cell2.setCellStyle(my_style);

                                    Cell cell6 = row.createCell((short) 3);
                                    cell6.setCellValue(sCellParent);
                                    cell6.setCellStyle(my_style);

                                    Cell cell7 = row.createCell((short) 4);
                                    cell7.setCellValue(sCellLevel);
                                    cell7.setCellStyle(my_style);

                                    Cell cell3 = row.createCell((short) 5);
                                    cell3.setCellValue(sCellPhoneNumber);
                                    cell3.setCellStyle(my_style);

                                    Cell cell5 = row.createCell((short) 6);
                                    cell5.setCellValue(sCellEmail);
                                    cell5.setCellStyle(my_style);
                                    
                                    Cell cell56 = row.createCell((short) 7);
                                    cell56.setCellValue(sCellAddress);
                                    cell56.setCellStyle(my_style);
                                    
                                    Cell cell8 = row.createCell((short) 8);
                                    cell8.setCellValue(sCellCreateDate);
                                    cell8.setCellStyle(my_style);

                                    Font fontBranch = wb.createFont();
                                    fontBranch.setFontName("Arial");
                                    CellStyle styleText = wb.createCellStyle();
                                    styleText.setFont(fontBranch);
                                    styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));

                                    int i = 1;
                                    int k = 0;
                                    for (BRANCH rsReportBranch1 : rsReportBranch[0]) {
                                        if (k == 0) {
                                            k = 1;
                                        } else {
                                            k++;
                                        }
                                        Row row1 = sheet.createRow(Integer.valueOf(i));
                                        CellStyle my_styleBranch = wb.createCellStyle();
                                        my_styleBranch.setFont(fontBranch);
                                        
                                        CellStyle my_styleBranchDate = wb.createCellStyle();
                                        Font fontBranchDate = wb.createFont();
                                        fontBranchDate.setFontName("Arial");
                                        my_styleBranchDate.setFont(fontBranchDate);
                                        my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY));

                                        Cell cellBranch = row1.createCell((short) 0);
                                        cellBranch.setCellValue(k);
                                        cellBranch.setCellStyle(my_styleBranch);

                                        Cell cellBranch1 = row1.createCell((short) 1);
                                        cellBranch1.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.NAME));
                                        cellBranch1.setCellStyle(my_styleBranch);

                                        Cell cellBranch2 = row1.createCell((short) 2);
                                        cellBranch2.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_STATE_DESC));
                                        cellBranch2.setCellStyle(my_styleBranch);

                                        Cell cellBranch5 = row1.createCell((short) 3);
                                        cellBranch5.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PARENT_NAME));
                                        cellBranch5.setCellStyle(my_styleBranch);

                                        int sAGENCY_LEVEL = Integer.parseInt(rsReportBranch1.AGENCY_LEVEL) - 1;
                                        Cell cellBranch3 = row1.createCell((short) 4);
                                        cellBranch3.setCellValue(sAGENCY_LEVEL);
                                        cellBranch3.setCellStyle(styleText);
                                        
                                        Cell cellBranch191 = row1.createCell((short) 5);
                                        cellBranch191.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.MSISDN));
                                        cellBranch191.setCellStyle(styleText);
                                        
                                        Cell cellBranch192 = row1.createCell((short) 6);
                                        cellBranch192.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.EMAIL));
                                        cellBranch192.setCellStyle(styleText);
                                        
                                        Cell cellBranch15 = row1.createCell((short) 7);
                                        cellBranch15.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.ADDRESS));
                                        cellBranch15.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch20 = row1.createCell((short) 8);
                                        Date dateReceive = CommonFunction.convertStringToDate(EscapeUtils.CheckTextNull(rsReportBranch1.CREATED_DT), Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                        if(dateReceive != null) {
                                            cellBranch20.setCellValue(dateReceive);
                                        } else {
                                            cellBranch20.setCellValue("");
                                        }
                                        cellBranch20.setCellStyle(my_styleBranchDate);
                                                                                
                                        i++;
                                    }
                                    ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                    wb.write(outByteStream);
                                    byte[] outArray = outByteStream.toByteArray();
                                    File someFile = new File(strURLPath);
                                    FileOutputStream fos = new FileOutputStream(someFile);
                                    fos.write(outArray);
                                    fos.flush();
                                    strView = "0#" + strURLPath + "#" + sFileName;
                                } else {
                                    strView = "2#1";
                                }
                                //</editor-fold>
                                break;
                            }
                            case "exportesignclousremainlist": {
                                //<editor-fold defaultstate="collapsed" desc="exportesignclousremainlist">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
                                    Config conf = new Config();
                                    ConfigLanguage confLanguage = new ConfigLanguage();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_RSSP_REMAINING_SIGNING_EXPORT + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    CommonFunction.LogDebugString(log, "Export Remaining Signing Report", "COUNTER: " + countList);
                                    CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                    if (!"".equals(countList)) {
                                        String effectiveFrom = request.getSession(false).getAttribute("sessEffectiveFromRemain").toString().trim();
                                        String effectiveTo = request.getSession(false).getAttribute("sessEffectiveToRemain").toString().trim();
                                        String expireFrom = request.getSession(false).getAttribute("sessExpireFromRemain").toString().trim();
                                        String expireTo = request.getSession(false).getAttribute("sessExpireToRemain").toString().trim();
                                        String CERT_SN = request.getSession(false).getAttribute("sessCERT_SNRemain").toString().trim();
                                        String SIGNING_COUNTER = request.getSession(false).getAttribute("sessSIGNING_COUNTERRemain").toString().trim();
                                        String CERTIFICATION_HASH = request.getSession(false).getAttribute("sessCERTIFICATION_HASHRemain").toString().trim();
                                        String BranchOffice = request.getSession(false).getAttribute("sessBranchOfficeRemain").toString().trim();
                                        String COMPANY_NAME = request.getSession(false).getAttribute("sessCOMPANY_NAMERemain").toString().trim();
                                        String PERSONAL_NAME = request.getSession(false).getAttribute("sessPERSONAL_NAMERemain").toString().trim();
                                        String sENTERPRISE_PREFIX = request.getSession(false).getAttribute("sessENTERPRISE_PREFIXRemain").toString().trim();
                                        String sPERSONAL_PREFIX = request.getSession(false).getAttribute("sessPERSONAL_PREFIXRemain").toString().trim();
                                        String sENTERPRISE_ID = request.getSession(false).getAttribute("sessENTERPRISE_IDRemain").toString().trim();
                                        String sPERSONAL_ID = request.getSession(false).getAttribute("sessPERSONAL_IDRemain").toString().trim();
                                        String strAlertAllTimeEffective = request.getSession(false).getAttribute("AlertAllTimeEffectiveRemain").toString().trim();
                                        String strAlertAllTimeExpire = request.getSession(false).getAttribute("AlertAllTimeExpireRemain").toString().trim();
                                        String sEnterpriseCert = "";
                                        String sPersonalCert = "";
                                        if(!"".equals(sENTERPRISE_ID)){
                                            sEnterpriseCert = sENTERPRISE_PREFIX + "%"+sENTERPRISE_ID+"%";
                                        }
                                        if(!"".equals(sPERSONAL_ID)){
                                            sPersonalCert = sPERSONAL_PREFIX + "%"+sPERSONAL_ID+"%";
                                        }
                                        if ("1".equals(strAlertAllTimeEffective)) {
                                            effectiveFrom = "";
                                            effectiveTo = "";
                                        }
                                        if ("1".equals(strAlertAllTimeExpire)) {
                                            expireFrom = "";
                                            expireTo = "";
                                        }
                                        if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                            BranchOffice = "";
                                        }
                                        String sessTreeArrayBranchID = request.getSession(false).getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                        ConnectDbPhaseTwo dbTwo = new ConnectDbPhaseTwo();
                                        dbTwo.S_BO_CERTIFICATION_RSSP_LIST(CERT_SN, CERTIFICATION_HASH, SIGNING_COUNTER,
                                            COMPANY_NAME, PERSONAL_NAME, sEnterpriseCert, sPersonalCert, EscapeUtils.escapeHtmlSearch(effectiveFrom),
                                            EscapeUtils.escapeHtmlSearch(effectiveTo), EscapeUtils.escapeHtmlSearch(expireFrom),
                                            EscapeUtils.escapeHtmlSearch(expireTo), BranchOffice, sessTreeArrayBranchID,
                                            Integer.parseInt(loginLanguage), Definitions.CONFIG_PAGE_SIZE_IPAGNO, Integer.parseInt(countList), rsPgin);
                                        if(rsPgin[0].length > 0) {
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                            FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                            XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                            inputStream.close();
                                            String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_SN, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellThumbprint = "Thumbprint";
                                            String sCellRemain = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_ESIGNCLOUD_REMAIN_SIGNING, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            
                                            String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellMST = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_ENTERPRISE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellCompany = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COMPANY_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellPesonal = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PERSONAL_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellDateEffective = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EFFECTIVE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            String sCellDateExpiration = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_EXPIRATION, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                            
                                            SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                            wb.setCompressTempFiles(true);
                                            CreationHelper createHelper = wb.getCreationHelper();
                                            SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
                                            sheet.setRandomAccessWindowSize(100);
                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                            sheet.setColumnWidth(1, 32 * 255);
                                            sheet.setColumnWidth(2, 28 * 255);
                                            sheet.setColumnWidth(3, 32 * 255);
                                            sheet.setColumnWidth(4, 28 * 255);
                                            sheet.setColumnWidth(5, 32 * 255);
                                            sheet.setColumnWidth(6, 32 * 255);
                                            sheet.setColumnWidth(7, 32 * 255);
                                            sheet.setColumnWidth(8, 32 * 255);
                                            sheet.setColumnWidth(9, 36 * 255);
                                            sheet.setColumnWidth(10, 36 * 255);
                                            Row row = sheet.createRow(0);

                                            CellStyle my_style = wb.createCellStyle();
                                            Font font = wb.createFont();
                                            font.setBoldweight((short) 700);
                                            font.setFontName("Arial");
                                            my_style.setFont(font);
                                            my_style.setFillBackgroundColor((short) 9);
                                            my_style.setAlignment((short) 2);
                                            Cell cell = row.createCell((short) 0);
                                            cell.setCellValue(sCellSTT);
                                            cell.setCellStyle(my_style);

                                            Cell cell1 = row.createCell((short) 1);
                                            cell1.setCellValue(sCellAgency);
                                            cell1.setCellStyle(my_style);

                                            Cell cell2 = row.createCell((short) 2);
                                            cell2.setCellValue(sCellMST);
                                            cell2.setCellStyle(my_style);

                                            Cell cell3 = row.createCell((short) 3);
                                            cell3.setCellValue(sCellCompany);
                                            cell3.setCellStyle(my_style);

                                            Cell cell4 = row.createCell((short) 4);
                                            cell4.setCellValue(sCellCMND);
                                            cell4.setCellStyle(my_style);
                                            
                                            Cell cell5 = row.createCell((short) 5);
                                            cell5.setCellValue(sCellPesonal);
                                            cell5.setCellStyle(my_style);

                                            Cell cell6 = row.createCell((short) 6);
                                            cell6.setCellValue(sCellRemain);
                                            cell6.setCellStyle(my_style);
                                            
                                            Cell cell7 = row.createCell((short) 7);
                                            cell7.setCellValue(sCellDateEffective);
                                            cell7.setCellStyle(my_style);
                                            
                                            Cell cell8 = row.createCell((short) 8);
                                            cell8.setCellValue(sCellDateExpiration);
                                            cell8.setCellStyle(my_style);

                                            Cell cell9 = row.createCell((short) 9);
                                            cell9.setCellValue(sCellSN);
                                            cell9.setCellStyle(my_style);

                                            Cell cell10 = row.createCell((short) 10);
                                            cell10.setCellValue(sCellThumbprint);
                                            cell10.setCellStyle(my_style);
                                            int i = 1;
                                            int k = 0;
                                            for (CERTIFICATION temp1 : rsPgin[0]) {
                                                if (k == 0) {
                                                    k = 1;
                                                } else {
                                                    k++;
                                                }
                                                Row row1 = sheet.createRow(Integer.valueOf(i));
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                Font fontBranch = wb.createFont();
                                                fontBranch.setFontName("Arial");
                                                my_styleBranch.setFont(fontBranch);
                                                
                                                CellStyle styleText = wb.createCellStyle();
                                                styleText.setFont(fontBranch);
                                                styleText.setDataFormat(createHelper.createDataFormat().getFormat("@"));
                                                
                                                CellStyle my_styleBranchDate = wb.createCellStyle();
                                                Font fontBranchDate = wb.createFont();
                                                fontBranchDate.setFontName("Arial");
                                                my_styleBranchDate.setFont(fontBranchDate);
                                                my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS));

                                                Cell cellBranch = row1.createCell((short) 0);
                                                cellBranch.setCellValue(k);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row1.createCell((short) 1);
                                                cellBranch1.setCellValue(temp1.BRANCH_NAME + " - " + temp1.BRANCH_DESC);
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch2 = row1.createCell((short) 2);
                                                cellBranch2.setCellValue(temp1.ENTERPRISE_ID);
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                Cell cellBranch3 = row1.createCell((short) 3);
                                                cellBranch3.setCellValue(temp1.COMPANY_NAME);
                                                cellBranch3.setCellStyle(my_styleBranch);

                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(temp1.PERSONAL_ID);
                                                cellBranch4.setCellStyle(my_styleBranch);

                                                Cell cellBranch5 = row1.createCell((short) 5);
                                                cellBranch5.setCellValue(temp1.PERSONAL_NAME);
                                                cellBranch5.setCellStyle(my_styleBranch);

                                                Cell cellBranch6 = row1.createCell((short) 6);
                                                cellBranch6.setCellValue(temp1.REMAINING_SIGNING_COUNTER);
                                                cellBranch6.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch7 = row1.createCell((short) 7);
                                                Date dateEffective = CommonFunction.convertStringToDate(temp1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateEffective != null) {
                                                    cellBranch7.setCellValue(dateEffective);
                                                } else {
                                                    cellBranch7.setCellValue("");
                                                }
                                                cellBranch7.setCellStyle(my_styleBranchDate);
                                                
                                                Cell cellBranch8 = row1.createCell((short) 8);
                                                Date dateExpire = CommonFunction.convertStringToDate(temp1.EXPIRATION_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                if(dateExpire != null) {
                                                    cellBranch8.setCellValue(dateExpire);
                                                } else {
                                                    cellBranch8.setCellValue("");
                                                }
                                                cellBranch8.setCellStyle(my_styleBranchDate);
                                                
                                                Cell cellBranch9 = row1.createCell((short) 9);
                                                cellBranch9.setCellValue(temp1.CERTIFICATION_SN);
                                                cellBranch9.setCellStyle(styleText);

                                                Cell cellBranch10 = row1.createCell((short) 10);
                                                cellBranch10.setCellValue(temp1.CERTIFICATION_HASH);
                                                cellBranch10.setCellStyle(my_styleBranch);
                                                i++;
                                            }
                                            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                                            wb.write(outByteStream);
                                            byte[] outArray = outByteStream.toByteArray();
                                            File someFile = new File(strURLPath);
                                            FileOutputStream fos = new FileOutputStream(someFile);
                                            fos.write(outArray);
                                            fos.flush();
                                            strView = "0#" + strURLPath + "#" + sFileName;
                                        } else {
                                            strView = "2#1";
                                        }
                                    } else {
                                        strView = "2#1";
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
            } catch (NumberFormatException | SQLException e) {
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
