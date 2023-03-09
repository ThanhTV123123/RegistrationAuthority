/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ra.servlet;

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
import vn.ra.object.REPORT_PER_MONTH;
import vn.ra.object.REPORT_QUICK_BRANCH;
import vn.ra.object.TOKEN;
import vn.ra.process.CommonFunction;
import vn.ra.process.ConnectDatabase;
import vn.ra.process.EncodeSOPIN;
import vn.ra.utility.Config;
import vn.ra.utility.ConfigLanguage;
import vn.ra.utility.Definitions;
import vn.ra.utility.EscapeUtils;

/**
 *
 * @author USER
 */
public class ExportCSVParam_BK extends HttpServlet {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ExportCSVParam_BK.class);
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
                            case "exportcertquick_bk": {
                                //<editor-fold defaultstate="collapsed" desc="exportcertquick_bk">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                    Config conf = new Config();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();

                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_QUICK + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;

                                    REPORT_QUICK_BRANCH[][] rsReportBranch = new REPORT_QUICK_BRANCH[1][];
                                    String FromCreateDate = request.getSession(false).getAttribute("sessFromCreateDateReportQuickAgent").toString().trim();
                                    String ToCreateDate = request.getSession(false).getAttribute("sessToCreateDateReportQuickAgent").toString().trim();
                                    String idBranchOffice = request.getSession(false).getAttribute("sessBranchOfficeReportQuickAgent").toString().trim();
                                    String idBRANCH_STATE = request.getSession(false).getAttribute("sessBranchStateReportQuickAgent").toString().trim();
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
                                        idBranchOffice = "";
                                    }
                                    com.S_BO_REPORT_TOTAL_BRANCH(FromCreateDate, ToCreateDate, EscapeUtils.escapeHtmlSearch(idBranchOffice), idBRANCH_STATE,
                                        loginLanguage, rsReportBranch, SessUserAgentID, "");
                                    if (rsReportBranch[0].length > 0) {
                                        String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                        String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_AGENCY).trim();
                                        String sCellInit = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_INIT).trim();
                                        String sCellActivation = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_ACTIVATION).trim();
                                        String sCellWorkerRevoke = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_REVOKE).trim();
                                        String sCellSum = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_SUM).trim();
                                        HSSFWorkbook wb = new HSSFWorkbook();
                                        HSSFSheet sheet = wb.createSheet();
                                        sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                        sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
                                        sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_INIT);
                                        sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_ACTIVATION);
                                        sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REVOKE);
                                        sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_SUM);
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
                                            HSSFRow row1 = sheet.createRow(Integer.valueOf(i).intValue());
                                            HSSFCellStyle my_styleBranch = wb.createCellStyle();
                                            HSSFFont fontBranch = wb.createFont();

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
                                                        HSSFRow rowUser = sheet.createRow(Integer.valueOf(i).intValue());
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
                            case "exportcertquick": {
                                //<editor-fold defaultstate="collapsed" desc="exportcertquick">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                    Config conf = new Config();
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
//                                    CommonFunction.LogDebugString(log, "ExportReportQuick", "FromDate: " + FromCreateDate + "; ToDate: " + ToCreateDate + "; AGENCY: " + idBranchOffice);
                                    com.S_BO_REPORT_TOTAL_BRANCH(FromCreateDate, ToCreateDate, EscapeUtils.escapeHtmlSearch(idBranchOffice), idBRANCH_STATE,
                                        loginLanguage, rsReportBranch, SessUserAgentID,"");
                                    if (rsReportBranch[0].length > 0) {
                                        String queryString = getServletContext().getRealPath("/");
                                        String outputDirectory = queryString;
                                        String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                        FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                        inputStream.close();
                                        String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                        String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_AGENCY).trim();
                                        String sCellInit = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_INIT).trim();
                                        String sCellActivation = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_ACTIVATION).trim();
                                        String sCellWorkerRevoke = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_REVOKE).trim();
                                        String sCellSum = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_SUM).trim();
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
                            case "exportreportcertlist_bk": {
                                //<editor-fold defaultstate="collapsed" desc="exportreportcertlist_bk">
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
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_CERTIFICATE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    CommonFunction.LogDebugString(log, "ExportReportCertList", "PathURL: " + strURLPath);
                                    REPORT_PER_MONTH[][] rsReportBranch = new REPORT_PER_MONTH[1][];
                                    String FromCreateDate = request.getSession(false).getAttribute("sessYearReportCertList").toString().trim();
                                    String ToCreateDate = request.getSession(false).getAttribute("sessMonthReportCertList").toString().trim();
                                    String PKI_FORMFACTOR = request.getSession(false).getAttribute("sessFormFactorReportCertList").toString().trim();
                                    String idBranchOffice = request.getSession(false).getAttribute("sessBranchOfficeReportCertList").toString().trim();
                                    String sUSER = request.getSession(false).getAttribute("sessUserReportCertList").toString().trim();
                                    String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
//                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
//                                        idBranchOffice = "";
//                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(sUSER)) {
                                        sUSER = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(PKI_FORMFACTOR)) {
                                        PKI_FORMFACTOR = "";
                                    }
                                    CommonFunction.LogDebugString(log, "ExportReportCertList", "Year: " + FromCreateDate
                                        + "; Month: " + ToCreateDate + "; PKI_FORMFACTOR: " + PKI_FORMFACTOR + "; AGENCY: " + idBranchOffice);
//                                    String idType = EscapeUtils.CheckTextNull(request.getParameter("idType"));
//                                    int ss = 0;
//                                    if (idType.equals(Definitions.CONFIG_EXPORT_TYPE_REPORT_CERT_TOKEN)) {
//                                        ss = com.S_BO_REPORT_PER_MONTH_TOTAL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
//                                                EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice),
//                                                sUSER);
//                                        if (ss > 0) {
//                                            com.S_BO_REPORT_PER_MONTH_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
//                                                    EscapeUtils.escapeHtmlSearch(idBranchOffice), sUSER,
//                                                    loginLanguage, rsReportBranch, Definitions.CONFIG_PAGE_SIZE_IPAGNO, ss);
//                                        }
//                                    } else if (idType.equals(Definitions.CONFIG_EXPORT_TYPE_REPORT_CERT_SIGNSERVER)) {
//                                        ss = com.S_BO_REPORT_PER_MONTH_SS_TOTAL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
//                                                EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice),
//                                                sUSER);
//                                        if (ss > 0) {
//                                            com.S_BO_REPORT_PER_MONTH_SS_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
//                                                EscapeUtils.escapeHtmlSearch(idBranchOffice), sUSER,
//                                                loginLanguage, rsReportBranch, Definitions.CONFIG_PAGE_SIZE_IPAGNO, ss);
//                                        }
//                                    }
                                    int ss = com.S_BO_REPORT_PER_MONTH_TOTAL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                        EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice),
                                        sUSER, PKI_FORMFACTOR, SessUserAgentID);
                                    if (ss > 0) {
                                        com.S_BO_REPORT_PER_MONTH_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(idBranchOffice), sUSER, PKI_FORMFACTOR,
                                            loginLanguage, rsReportBranch, Definitions.CONFIG_PAGE_SIZE_IPAGNO, ss, SessUserAgentID);
                                    }
                                    if (ss > 0) {
                                        String sCellSTT = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_STT, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellAgency = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_AGENT_CODE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellUserCreate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_USER_CREATE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellMST = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_ENTERPRISE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellSTATE = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_CERT_STATUS, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellCompany = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_COMPANY_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellCMND = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_PERSONAL, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellPesonal = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PERSONAL_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sDeviceUUID = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_DEVICE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellProvince = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_PROVINCE_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellProfile = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_SERVICEPACK_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellRequestType = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_REQUEST_TYPE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellFormFactor = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_METHOD_NAME, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDateCreate = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_GEN, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDateCancel = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CANCEL, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellNUM_DATE_CANCEL = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CANCEL_NUMBER, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellDATE_GEN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_DATE_CONTROL, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sTokenSN = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_TOKEN_SN, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        HSSFWorkbook wb = new HSSFWorkbook();
                                        HSSFSheet sheet = wb.createSheet();
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
                                        sheet.setColumnWidth(15, Definitions.CONFIG_EXPORT_QUICK_WIDTH_NUM_DATE_CANCEL);
                                        sheet.setColumnWidth(16, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_GEN);
                                        sheet.setColumnWidth(17, 18*255);
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

//                                        row.createCell((short) 2).setCellValue(sCellUserCreate);
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
                                        
                                        Cell cell71 = row.createCell((short) 8);
                                        cell71.setCellValue(sDeviceUUID);
                                        cell71.setCellStyle(my_style);

                                        Cell cell8 = row.createCell((short) 9);
                                        cell8.setCellValue(sCellProvince);
                                        cell8.setCellStyle(my_style);

                                        Cell cell9 = row.createCell((short) 10);
                                        cell9.setCellValue(sCellProfile);
                                        cell9.setCellStyle(my_style);
                                        
                                        Cell cell10 = row.createCell((short) 11);
                                        cell10.setCellValue(sCellRequestType);
                                        cell10.setCellStyle(my_style);
                                        
                                        Cell cell11 = row.createCell((short) 12);
                                        cell11.setCellValue(sCellFormFactor);
                                        cell11.setCellStyle(my_style);

                                        Cell cell12 = row.createCell((short) 13);
                                        cell12.setCellValue(sCellDateCreate);
                                        cell12.setCellStyle(my_style);

                                        Cell cell13 = row.createCell((short) 14);
                                        cell13.setCellValue(sCellDateCancel);
                                        cell13.setCellStyle(my_style);

                                        Cell cell14 = row.createCell((short) 15);
                                        cell14.setCellValue(sCellNUM_DATE_CANCEL);
                                        cell14.setCellStyle(my_style);

                                        Cell cell15 = row.createCell((short) 16);
                                        cell15.setCellValue(sCellDATE_GEN);
                                        cell15.setCellStyle(my_style);
                                        
                                        Cell cell16 = row.createCell((short) 17);
                                        cell16.setCellValue(sTokenSN);
                                        cell16.setCellStyle(my_style);

                                        int i = 1;
                                        int k = 0;
                                        for (REPORT_PER_MONTH rsReportBranch1 : rsReportBranch[0]) {
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
//                                            my_styleBranch.setFillBackgroundColor((short) 9);

                                            Cell cellBranch = row1.createCell((short) 0);
                                            cellBranch.setCellValue(k);
                                            cellBranch.setCellStyle(my_styleBranch);

                                            Cell cellBranch1 = row1.createCell((short) 1);
                                            cellBranch1.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_NAME));
                                            cellBranch1.setCellStyle(my_styleBranch);

                                            Cell cellBranch2 = row1.createCell((short) 2);
                                            cellBranch2.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.USERNAME_CREATED));
                                            cellBranch2.setCellStyle(my_styleBranch);

                                            String sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.TAX_CODE);
                                            if ("".equals(sMSTAndBUDGET_CODE)) {
                                                sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.BUDGET_CODE);
                                            }
                                            Cell cellBranch3 = row1.createCell((short) 3);
                                            cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                            cellBranch3.setCellStyle(my_styleBranch);

                                            Cell cellBranch4 = row1.createCell((short) 4);
                                            cellBranch4.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_STATE_NAME));
                                            cellBranch4.setCellStyle(my_styleBranch);

                                            Cell cellBranch5 = row1.createCell((short) 5);
                                            cellBranch5.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.COMPANY_NAME));
                                            cellBranch5.setCellStyle(my_styleBranch);

                                            String sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.P_ID);
                                            if ("".equals(sP_IDAndPASSPORT)) {
                                                sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.P_EID);
                                            }
                                            if ("".equals(sP_IDAndPASSPORT)) {
                                                sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.PASSPORT);
                                            }
                                            Cell cellBranch6 = row1.createCell((short) 6);
                                            cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                            cellBranch6.setCellStyle(my_styleBranch);

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

                                            Cell cellBranch12 = row1.createCell((short) 13);
                                            cellBranch12.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CREATED_DT));
                                            cellBranch12.setCellStyle(my_styleBranch);

                                            Cell cellBranch13 = row1.createCell((short) 14);
                                            cellBranch13.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.REVOKED_DT));
                                            cellBranch13.setCellStyle(my_styleBranch);

                                            Cell cellBranch14 = row1.createCell((short) 15);
                                            cellBranch14.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.NUMBER_DELETED));
                                            cellBranch14.setCellStyle(my_styleBranch);

                                            Cell cellBranch15 = row1.createCell((short) 16);
                                            cellBranch15.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.GENERATED_DT));
                                            cellBranch15.setCellStyle(my_styleBranch);
                                            
                                            String valueTokenSN = EscapeUtils.CheckTextNull(rsReportBranch1.TOKEN_SN);
                                            if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                            Cell cellBranch16 = row1.createCell((short) 17);
                                            cellBranch16.setCellValue(valueTokenSN);
                                            cellBranch16.setCellStyle(my_styleBranch);
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
                            case "exportreportcertlist": {
                                //<editor-fold defaultstate="collapsed" desc="exportreportcertlist">
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
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_CERTIFICATE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    REPORT_PER_MONTH[][] rsReportBranch = new REPORT_PER_MONTH[1][];
                                    String FromCreateDate = request.getSession(false).getAttribute("sessYearReportCertList").toString().trim();
                                    String ToCreateDate = request.getSession(false).getAttribute("sessMonthReportCertList").toString().trim();
                                    String PKI_FORMFACTOR = request.getSession(false).getAttribute("sessFormFactorReportCertList").toString().trim();
                                    String idBranchOffice = request.getSession(false).getAttribute("sessBranchOfficeReportCertList").toString().trim();
                                    String sUSER = request.getSession(false).getAttribute("sessUserReportCertList").toString().trim();
                                    String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
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
                                        sUSER, PKI_FORMFACTOR, SessUserAgentID);
                                    if (ss > 0) {
                                        com.S_BO_REPORT_PER_MONTH_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(idBranchOffice), sUSER, PKI_FORMFACTOR,
                                            loginLanguage, rsReportBranch, Definitions.CONFIG_PAGE_SIZE_IPAGNO, ss, SessUserAgentID);
                                    }
                                    if (ss > 0) {
                                        String queryString = getServletContext().getRealPath("/");
                                        String outputDirectory = queryString;
                                        String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                        FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                        inputStream.close();
                                        String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                        String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
                                        String sCellUserCreate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_USER_CREATE).trim();
                                        String sCellMST = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_MST).trim();
                                        String sCellSTATE = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_STATE).trim();
                                        String sCellCompany = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COMPANY).trim();
                                        String sCellCMND = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CMND).trim();
                                        String sDeviceUUID = confLanguage.GetPropertybyCode(Definitions.CONFIG_LANGUAGE_EXPORT_UID_DEVICE, request.getSession(false).getAttribute("sessVN").toString()).trim();
                                        String sCellPesonal = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PESONAL).trim();
                                        String sCellProvince = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PROVINCE).trim();
                                        String sCellProfile = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PROFILE).trim();
                                        String sCellRequestType = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_REQUEST_TYPE).trim();
                                        String sCellFormFactor = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_FORMFACTOR).trim();
                                        String sCellDateCreate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_GEN).trim();
                                        String sCellDateCancel = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_CANCEL).trim();
                                        String sCellNUM_DATE_CANCEL = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_NUM_DATE_CANCEL).trim();
                                        String sCellDATE_GEN = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_COLLATION).trim();
                                        String sTokenSN = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_TOKEN_CODE).trim();
                                        String sCellCertSN = "Số sê-ri CTS";
                                        String sCellDateEffective = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_EFFECTIVE).trim();
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
                                        sheet.setColumnWidth(15, Definitions.CONFIG_EXPORT_QUICK_WIDTH_NUM_DATE_CANCEL);
                                        sheet.setColumnWidth(16, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_GEN);
                                        sheet.setColumnWidth(17, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_GEN);
                                        sheet.setColumnWidth(18, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_GEN);
                                        sheet.setColumnWidth(19, 18*255);
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
                                        
                                        Cell cell16 = row.createCell((short) 18);
                                        cell16.setCellValue(sTokenSN);
                                        cell16.setCellStyle(my_style);
                                        
                                        Cell cell112 = row.createCell((short) 19);
                                        cell112.setCellValue(sCellCertSN);
                                        cell112.setCellStyle(my_style);

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

                                            Cell cellBranch = row1.createCell((short) 0);
                                            cellBranch.setCellValue(k);
                                            cellBranch.setCellStyle(my_styleBranch);

                                            Cell cellBranch1 = row1.createCell((short) 1);
                                            cellBranch1.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_NAME));
                                            cellBranch1.setCellStyle(my_styleBranch);

                                            Cell cellBranch2 = row1.createCell((short) 2);
                                            cellBranch2.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.USERNAME_CREATED));
                                            cellBranch2.setCellStyle(my_styleBranch);

                                            String sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.TAX_CODE);
                                            Cell cellBranch3 = row1.createCell((short) 3);
                                            cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                            cellBranch3.setCellStyle(my_styleBranch);

                                            Cell cellBranch4 = row1.createCell((short) 4);
                                            cellBranch4.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_STATE_NAME));
                                            cellBranch4.setCellStyle(my_styleBranch);

                                            Cell cellBranch5 = row1.createCell((short) 5);
                                            cellBranch5.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.COMPANY_NAME));
                                            cellBranch5.setCellStyle(my_styleBranch);

                                            String sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.P_ID);
                                            Cell cellBranch6 = row1.createCell((short) 6);
                                            cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                            cellBranch6.setCellStyle(my_styleBranch);

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
                                            
                                            String valueTokenSN = EscapeUtils.CheckTextNull(rsReportBranch1.TOKEN_SN);
                                            if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                            Cell cellBranch16 = row1.createCell((short) 18);
                                            cellBranch16.setCellValue(valueTokenSN);
                                            cellBranch16.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch122 = row1.createCell((short) 19);
                                            cellBranch122.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_SN));
                                            cellBranch122.setCellStyle(my_styleBranch);
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
                                        String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                        String sCellContent = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CONTENT).trim();
                                        String sCellAmount = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AMOUNT).trim();
                                        String sCellNote = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_NOTE).trim();
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

//                                        row.createCell((short) 2).setCellValue(sCellUserCreate);
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
//                                            if (rsReportBranchAmount.PAYMENT_TYPE_ID == Definitions.CONFIG_PROCESS_PAYMENT_TYPE_TOTAL) {
//                                                
//                                            }
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
//                                            fontBranch.setColor((short) 12);
                                            my_styleBranch.setFont(fontBranch);
//                                            my_styleBranch.setFillBackgroundColor((short) 9);

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
                            case "exportcertexpire_bk": {
                                //<editor-fold defaultstate="collapsed" desc="exportcertexpire_bk">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    Config conf = new Config();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                            if (!directory.exists()){
                                                directory.mkdir();
                                            }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();

                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_EXPIRE + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    CommonFunction.LogDebugString(log, "ExportCertificateExpire", "PathURL: " + strURLPath);

                                    CERTIFICATION[][] rsReportBranch = new CERTIFICATION[1][];
                                    String DATE_EXPIRE = request.getSession(false).getAttribute("sessDATE_EXPIRECertExpireList").toString().trim();
                                    String BranchOffice = request.getSession(false).getAttribute("sessBranchOfficeCertExpireList").toString().trim();
                                    String UserCert = request.getSession(false).getAttribute("sessUserCertExpireList").toString().trim();
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                        BranchOffice = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(UserCert)) {
                                        UserCert = "";
                                    }
                                    CommonFunction.LogDebugString(log, "ExportCertificateExpire", "DATE_EXPIRE: " + DATE_EXPIRE + "; BranchOffice: " + BranchOffice
                                            + "; UserCert: " + UserCert);
                                    int iCount = com.S_BO_CERTIFICATION_WARNING_EXPIRED_TOTAL("","", BranchOffice,
                                            EscapeUtils.escapeHtmlSearch(UserCert),"");

                                    if (iCount > 0) {
                                        com.S_BO_CERTIFICATION_WARNING_EXPIRED_LIST("","",
                                                EscapeUtils.escapeHtmlSearch(BranchOffice), UserCert, loginLanguage, rsReportBranch,
                                                Definitions.CONFIG_PAGE_SIZE_IPAGNO, Definitions.CONFIG_PAGE_SIZE_ISWRWS,"");
                                        if (rsReportBranch[0].length > 0) {
                                            String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                            String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_AGENCY).trim();
                                            String sCellUser = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_USER_CREATE).trim();
                                            String sCellCompany = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COMPANY).trim();
                                            String sCellMST = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_MST).trim();
                                            String sCellPersonal = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PESONAL).trim();
                                            String sCellCMND = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CMND).trim();
                                            String sCellPhoneContact = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PHONE_CONTACT).trim();
                                            String sCellEmailContact = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_EMAIL_CONTACT).trim();
                                            String sCellDateEffective = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_EFFECTIVE).trim();
                                            String sCellDateExpiration = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_EXPIRATION).trim();
                                            HSSFWorkbook wb = new HSSFWorkbook();
                                            HSSFSheet sheet = wb.createSheet();
                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                            sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
                                            sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                            sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                            sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                            sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                            sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PHONE_CONTACT);
                                            sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_EMAIL_CONTACT);
                                            sheet.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EFFECTIVE);
                                            sheet.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
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

                                            row.createCell((short) 2).setCellValue(sCellUser);
                                            Cell cell2 = row.createCell((short) 2);
                                            cell2.setCellValue(sCellUser);
                                            cell2.setCellStyle(my_style);

                                            Cell cell3 = row.createCell((short) 3);
                                            cell3.setCellValue(sCellCompany);
                                            cell3.setCellStyle(my_style);

                                            Cell cell4 = row.createCell((short) 4);
                                            cell4.setCellValue(sCellMST);
                                            cell4.setCellStyle(my_style);

                                            Cell cell5 = row.createCell((short) 5);
                                            cell5.setCellValue(sCellPersonal);
                                            cell5.setCellStyle(my_style);
                                            
                                            Cell cell6 = row.createCell((short) 6);
                                            cell6.setCellValue(sCellCMND);
                                            cell6.setCellStyle(my_style);
                                            
                                            Cell cell7 = row.createCell((short) 7);
                                            cell7.setCellValue(sCellPhoneContact);
                                            cell7.setCellStyle(my_style);
                                            
                                            Cell cell8 = row.createCell((short) 8);
                                            cell8.setCellValue(sCellEmailContact);
                                            cell8.setCellStyle(my_style);
                                            
                                            Cell cell9 = row.createCell((short) 9);
                                            cell9.setCellValue(sCellDateEffective);
                                            cell9.setCellStyle(my_style);
                                            
                                            Cell cell10 = row.createCell((short) 10);
                                            cell10.setCellValue(sCellDateExpiration);
                                            cell10.setCellStyle(my_style);
                                            int i = 1;
                                            int k = 0;
                                            for (CERTIFICATION rsReportBranch1 : rsReportBranch[0]) {
                                                if (k == 0) {
                                                    k = 1;
                                                } else {
                                                    k++;
                                                }
                                                String strAgency = EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_DESC);// + " (" + EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_NAME) + ")";
                                                HSSFRow row1 = sheet.createRow(Integer.valueOf(i).intValue());
                                                HSSFCellStyle my_styleBranch = wb.createCellStyle();
                                                HSSFFont fontBranch = wb.createFont();

                                                fontBranch.setFontName("Arial");
//                                                fontBranch.setColor((short) 12);
                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setFillBackgroundColor((short) 9);

                                                Cell cellBranch = row1.createCell((short) 0);
                                                cellBranch.setCellValue(k);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row1.createCell((short) 1);
                                                cellBranch1.setCellValue(strAgency);
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch2 = row1.createCell((short) 2);
                                                cellBranch2.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CREATED_BY));
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                Cell cellBranch3 = row1.createCell((short) 3);
                                                cellBranch3.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.COMPANY_NAME));
                                                cellBranch3.setCellStyle(my_styleBranch);
                                                
                                                String sSMTOrMSN = EscapeUtils.CheckTextNull(rsReportBranch1.TAX_CODE);
                                                if("".equals(sSMTOrMSN)) {
                                                    sSMTOrMSN = EscapeUtils.CheckTextNull(rsReportBranch1.BUDGET_CODE);
                                                }
                                                if("".equals(sSMTOrMSN)) {
                                                    sSMTOrMSN = EscapeUtils.CheckTextNull(rsReportBranch1.DECISION);
                                                }
                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(sSMTOrMSN);
                                                cellBranch4.setCellStyle(my_styleBranch);

                                                Cell cellBranch5 = row1.createCell((short) 5);
                                                cellBranch5.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PERSONAL_NAME));
                                                cellBranch5.setCellStyle(my_styleBranch);
                                                
                                                String sCMNDOrHC = EscapeUtils.CheckTextNull(rsReportBranch1.P_ID);
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(rsReportBranch1.P_EID);
                                                }
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(rsReportBranch1.PASSPORT);
                                                }
                                                Cell cellBranch6 = row1.createCell((short) 6);
                                                cellBranch6.setCellValue(EscapeUtils.CheckTextNull(sCMNDOrHC));
                                                cellBranch6.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch7 = row1.createCell((short) 7);
                                                cellBranch7.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PHONE_CONTRACT));
                                                cellBranch7.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch8 = row1.createCell((short) 8);
                                                cellBranch8.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.EMAIL_CONTRACT));
                                                cellBranch8.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch9 = row1.createCell((short) 9);
                                                cellBranch9.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.EFFECTIVE_DT));
                                                cellBranch9.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch10 = row1.createCell((short) 10);
                                                cellBranch10.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.EXPIRATION_DT));
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
                            case "exportcertexpire": {
                                //<editor-fold defaultstate="collapsed" desc="exportcertexpire">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
//                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    Config conf = new Config();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();

                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_EXPIRE + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;

                                    CERTIFICATION[][] rsReportBranch = new CERTIFICATION[1][];
                                    String DATE_EXPIRE = request.getSession(false).getAttribute("sessDATE_EXPIRECertExpireList").toString().trim();
                                    String BranchOffice = request.getSession(false).getAttribute("sessBranchOfficeCertExpireList").toString().trim();
                                    String UserCert = request.getSession(false).getAttribute("sessUserCertExpireList").toString().trim();
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                        BranchOffice = "";
                                    }
                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(UserCert)) {
                                        UserCert = "";
                                    }
                                    CommonFunction.LogDebugString(log, "ExportCertificateExpire", "DATE_EXPIRE: " + DATE_EXPIRE + "; BranchOffice: " + BranchOffice
                                            + "; UserCert: " + UserCert);
                                    int iCount = com.S_BO_CERTIFICATION_WARNING_EXPIRED_TOTAL("","", BranchOffice,
                                            EscapeUtils.escapeHtmlSearch(UserCert),"");

                                    if (iCount > 0) {
                                        com.S_BO_CERTIFICATION_WARNING_EXPIRED_LIST("","",
                                                EscapeUtils.escapeHtmlSearch(BranchOffice), UserCert, loginLanguage, rsReportBranch,
                                                Definitions.CONFIG_PAGE_SIZE_IPAGNO, Definitions.CONFIG_PAGE_SIZE_ISWRWS,"");
                                        if (rsReportBranch[0].length > 0) {
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                            FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                            XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                            inputStream.close();
                                            String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                            String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_AGENCY).trim();
                                            String sCellUser = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_USER_CREATE).trim();
                                            String sCellCompany = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COMPANY).trim();
                                            String sCellMST = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_MST).trim();
                                            String sCellPersonal = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PESONAL).trim();
                                            String sCellCMND = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CMND).trim();
                                            String sCellPhoneContact = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PHONE_CONTACT).trim();
                                            String sCellEmailContact = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_EMAIL_CONTACT).trim();
                                            String sCellDateEffective = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_EFFECTIVE).trim();
                                            String sCellDateExpiration = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_EXPIRATION).trim();
                                            SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                            wb.setCompressTempFiles(true);
                                            SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
                                            sheet.setRandomAccessWindowSize(100);
                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                            sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
                                            sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                            sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                            sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                            sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                            sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PHONE_CONTACT);
                                            sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_EMAIL_CONTACT);
                                            sheet.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EFFECTIVE);
                                            sheet.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_EXPIRATION);
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

                                            Cell cell3 = row.createCell((short) 3);
                                            cell3.setCellValue(sCellCompany);
                                            cell3.setCellStyle(my_style);

                                            Cell cell4 = row.createCell((short) 4);
                                            cell4.setCellValue(sCellMST);
                                            cell4.setCellStyle(my_style);

                                            Cell cell5 = row.createCell((short) 5);
                                            cell5.setCellValue(sCellPersonal);
                                            cell5.setCellStyle(my_style);
                                            
                                            Cell cell6 = row.createCell((short) 6);
                                            cell6.setCellValue(sCellCMND);
                                            cell6.setCellStyle(my_style);
                                            
                                            Cell cell7 = row.createCell((short) 7);
                                            cell7.setCellValue(sCellPhoneContact);
                                            cell7.setCellStyle(my_style);
                                            
                                            Cell cell8 = row.createCell((short) 8);
                                            cell8.setCellValue(sCellEmailContact);
                                            cell8.setCellStyle(my_style);
                                            
                                            Cell cell9 = row.createCell((short) 9);
                                            cell9.setCellValue(sCellDateEffective);
                                            cell9.setCellStyle(my_style);
                                            
                                            Cell cell10 = row.createCell((short) 10);
                                            cell10.setCellValue(sCellDateExpiration);
                                            cell10.setCellStyle(my_style);
                                            int i = 1;
                                            int k = 0;
                                            for (CERTIFICATION rsReportBranch1 : rsReportBranch[0]) {
                                                if (k == 0) {
                                                    k = 1;
                                                } else {
                                                    k++;
                                                }
                                                String strAgency = EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_DESC);// + " (" + EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_NAME) + ")";
                                                Row row1 = sheet.createRow(Integer.valueOf(i).intValue());
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                Font fontBranch = wb.createFont();

                                                fontBranch.setFontName("Arial");
//                                                fontBranch.setColor((short) 12);
                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setFillBackgroundColor((short) 9);

                                                Cell cellBranch = row1.createCell((short) 0);
                                                cellBranch.setCellValue(k);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row1.createCell((short) 1);
                                                cellBranch1.setCellValue(strAgency);
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch2 = row1.createCell((short) 2);
                                                cellBranch2.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CREATED_BY));
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                Cell cellBranch3 = row1.createCell((short) 3);
                                                cellBranch3.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.COMPANY_NAME));
                                                cellBranch3.setCellStyle(my_styleBranch);
                                                
                                                String sSMTOrMSN = EscapeUtils.CheckTextNull(rsReportBranch1.TAX_CODE);
                                                if("".equals(sSMTOrMSN)) {
                                                    sSMTOrMSN = EscapeUtils.CheckTextNull(rsReportBranch1.BUDGET_CODE);
                                                }
                                                if("".equals(sSMTOrMSN)) {
                                                    sSMTOrMSN = EscapeUtils.CheckTextNull(rsReportBranch1.DECISION);
                                                }
                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(sSMTOrMSN);
                                                cellBranch4.setCellStyle(my_styleBranch);

                                                Cell cellBranch5 = row1.createCell((short) 5);
                                                cellBranch5.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PERSONAL_NAME));
                                                cellBranch5.setCellStyle(my_styleBranch);
                                                
                                                String sCMNDOrHC = EscapeUtils.CheckTextNull(rsReportBranch1.P_ID);
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(rsReportBranch1.P_EID);
                                                }
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(rsReportBranch1.PASSPORT);
                                                }
                                                Cell cellBranch6 = row1.createCell((short) 6);
                                                cellBranch6.setCellValue(EscapeUtils.CheckTextNull(sCMNDOrHC));
                                                cellBranch6.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch7 = row1.createCell((short) 7);
                                                cellBranch7.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PHONE_CONTRACT));
                                                cellBranch7.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch8 = row1.createCell((short) 8);
                                                cellBranch8.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.EMAIL_CONTRACT));
                                                cellBranch8.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch9 = row1.createCell((short) 9);
                                                cellBranch9.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.EFFECTIVE_DT));
                                                cellBranch9.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch10 = row1.createCell((short) 10);
                                                cellBranch10.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.EXPIRATION_DT));
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
                                        String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
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
                                        String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
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
                            case "exportcertlist_bk": {
                                //<editor-fold defaultstate="collapsed" desc="exportcertlist_bk">
//                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
//                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
                                    String SessRoleID_ID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
                                    String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                    String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
//                                    int[] pIa = new int[1];
//                                    int[] pIb = new int[1];
                                    Config conf = new Config();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()) {
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_CERTIFICATE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    CommonFunction.LogDebugString(log, "ExportCertList", "PathURL: " + strURLPath);
                                    String ToCreateDate = (String) request.getSession(false).getAttribute("sessToCreateDateRenewCert");
                                    String FromCreateDate = (String) request.getSession(false).getAttribute("sessFromCreateDateRenewCert");
                                    String CERT_SN = (String) request.getSession(false).getAttribute("sessCERT_SNRenewCert");
                                    String FORM_FACTOR = (String) request.getSession(false).getAttribute("sessFormFactorRenewCert");
                                    String TOKEN_SN = (String) request.getSession(false).getAttribute("sessTOKEN_SNRenewCert");
                                    String PERSONAL_NAME = (String) request.getSession(false).getAttribute("sessPERSONAL_NAMERenewCert");
                                    String COMPANY_NAME = (String) request.getSession(false).getAttribute("sessCOMPANY_NAMERenewCert");
                                    String DOMAIN_NAME = (String) request.getSession(false).getAttribute("sessDOMAIN_NAMERenewCert");
                                    String TAX_CODE = (String) request.getSession(false).getAttribute("sessTAX_CODERenewCert");
                                    String BUDGET_CODE = (String) request.getSession(false).getAttribute("sessBUDGET_CODERenewCert");
                                    String P_ID = (String) request.getSession(false).getAttribute("sessP_IDRenewCert");
                                    String CCCD = (String) request.getSession(false).getAttribute("sessCCCDRenewCert");
                                    String PASSPORT = (String) request.getSession(false).getAttribute("sessPASSPORTRenewCert");
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
//                                    pIa[0] = Integer.parseInt((String) request.getSession(false).getAttribute("pIaRenewCert"));
//                                    pIb[0] = Integer.parseInt((String) request.getSession(false).getAttribute("pIbRenewCert"));
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
                                        if(!"".equals(BranchOffice) && !"".equals(USER))
                                        {
                                            SessUserAgentID = BranchOffice;
                                        }
                                    }
                                    else {
                                        BranchOffice = SessUserAgentID;
                                    }
                                    if("1".equals(strAlertAllTimes)) {
                                        ToCreateDate = "";
                                        FromCreateDate = "";
                                    }
                                    String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                    CommonFunction.LogDebugString(log, "ExportCertList", "FromDate: " + FromCreateDate + "; ToDate: " + ToCreateDate + "; AGENCY: " + BranchOffice);
                                    CERTIFICATION[][] rsPgin = new CERTIFICATION[1][];
                                    if (!"".equals(countList)) {
                                        com.S_BO_CERTIFICATION_LIST(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(CERT_SN),
                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
                                            EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
                                            EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(BranchOffice),
                                            USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
                                            EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),
                                            IsByOwner, EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
                                            loginLanguage, rsPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO, Integer.parseInt(countList),
                                            sessTreeArrayBranchID,"", "");
                                        if(rsPgin[0].length > 0)
                                        {
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

                                            String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                            String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
                                            String sCellUserCreate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_USER_CREATE).trim();
                                            String sCellMST = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_MST).trim();
                                            String sCellSTATE = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_STATE).trim();
                                            String sCellCompany = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COMPANY).trim();
                                            String sCellCMND = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CMND).trim();
                                            String sDeviceUUID = "UUID thiết bị";
                                            String sCellPesonal = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PESONAL).trim();
                                            String sCellProfile = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PROFILE).trim();
                                            String sCellRequestType = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_REQUEST_TYPE).trim();
                                            String sCellDateCreate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_CREATE).trim();
                                            String sCellDateGen = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_GEN).trim();
                                            String sCertType = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CERT_TYPE).trim();
                                            String sCertAttrState = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_REQUEST_STATE).trim();
                                            String sTokenSN = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_TOKEN_CODE).trim();
                                            String sCertSN = "Số sê-ri CTS";
                                            HSSFWorkbook wb = new HSSFWorkbook();
                                            HSSFSheet sheet = wb.createSheet();
                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                            sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                            sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                            sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                            sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                            sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CERT_TYPE);
                                            sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STATE);
                                            int iCountTitle = 8;
                                            if(isHasStateTypeRequest == true)
                                            {
                                                sheet.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_STATE);
                                                sheet.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                                iCountTitle = 10;
                                            }
                                            if(isHasCREATED_BY == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                sheet.setColumnWidth(iCountTitle, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                            }
                                            if(isHasBranch == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                sheet.setColumnWidth(iCountTitle, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                            }
                                            sheet.setColumnWidth(iCountTitle+1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                            sheet.setColumnWidth(iCountTitle+2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                            sheet.setColumnWidth(iCountTitle+3, 32*255);
                                            sheet.setColumnWidth(iCountTitle+4, 18*255);
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

                                            Cell cell5 = row.createCell((short) 1);
                                            cell5.setCellValue(sCellCompany);
                                            cell5.setCellStyle(my_style);

                                            Cell cell3 = row.createCell((short) 2);
                                            cell3.setCellValue(sCellMST);
                                            cell3.setCellStyle(my_style);

                                            Cell cell7 = row.createCell((short) 3);
                                            cell7.setCellValue(sCellPesonal);
                                            cell7.setCellStyle(my_style);

                                            Cell cell6 = row.createCell((short) 4);
                                            cell6.setCellValue(sCellCMND);
                                            cell6.setCellStyle(my_style);
                                            
                                            Cell cell61 = row.createCell((short) 5);
                                            cell61.setCellValue(sDeviceUUID);
                                            cell61.setCellStyle(my_style);

                                            Cell cell9 = row.createCell((short) 6);
                                            cell9.setCellValue(sCellProfile);
                                            cell9.setCellStyle(my_style);

                                            Cell cell8 = row.createCell((short) 7);
                                            cell8.setCellValue(sCertType);
                                            cell8.setCellStyle(my_style);

                                            Cell cell4 = row.createCell((short) 8);
                                            cell4.setCellValue(sCellSTATE);
                                            cell4.setCellStyle(my_style);
                                            iCountTitle = 8;
                                            if(isHasStateTypeRequest == true)
                                            {
                                                Cell cell41 = row.createCell((short) 9);
                                                cell41.setCellValue(sCertAttrState);
                                                cell41.setCellStyle(my_style);

                                                Cell cell10 = row.createCell((short) 10);
                                                cell10.setCellValue(sCellRequestType);
                                                cell10.setCellStyle(my_style);
                                                iCountTitle = 10;
                                            }
                                            if(isHasCREATED_BY == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                Cell cell2 = row.createCell((short) iCountTitle);
                                                cell2.setCellValue(sCellUserCreate);
                                                cell2.setCellStyle(my_style);
                                            }
                                            if(isHasBranch == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                Cell cell1 = row.createCell((short) iCountTitle);
                                                cell1.setCellValue(sCellAgency);
                                                cell1.setCellStyle(my_style);
                                            }

                                            Cell cell11 = row.createCell((short) iCountTitle+1);
                                            cell11.setCellValue(sCellDateCreate);
                                            cell11.setCellStyle(my_style);
                                            
                                            Cell cell13 = row.createCell((short) iCountTitle+2);
                                            cell13.setCellValue(sCellDateGen);
                                            cell13.setCellStyle(my_style);

                                            Cell cell12 = row.createCell((short) iCountTitle+3);
                                            cell12.setCellValue(sCertSN);
                                            cell12.setCellStyle(my_style);
                                            
                                            Cell cell14 = row.createCell((short) iCountTitle+4);
                                            cell14.setCellValue(sTokenSN);
                                            cell14.setCellStyle(my_style);

                                            int i = 1;
                                            int k = 0;
                                            for (CERTIFICATION temp1 : rsPgin[0]) {
                                                if (k == 0) {
                                                    k = 1;
                                                } else {
                                                    k++;
                                                }
                                                String sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.TAX_CODE);
                                                if("".equals(sMSTOrMSN))
                                                {
                                                    sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.BUDGET_CODE);
                                                }
                                                String sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_ID);
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_EID);
                                                }
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.PASSPORT);
                                                }
                                                HSSFRow row1 = sheet.createRow(Integer.valueOf(i));
                                                HSSFCellStyle my_styleBranch = wb.createCellStyle();
                                                HSSFFont fontBranch = wb.createFont();

                                                fontBranch.setFontName("Arial");
    //                                            fontBranch.setColor((short) 12);
                                                my_styleBranch.setFont(fontBranch);
    //                                            my_styleBranch.setFillBackgroundColor((short) 9);

                                                Cell cellBranch = row1.createCell((short) 0);
                                                cellBranch.setCellValue(k);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row1.createCell((short) 1);
                                                cellBranch1.setCellValue(EscapeUtils.CheckTextNull(temp1.COMPANY_NAME));
                                                cellBranch1.setCellStyle(my_styleBranch);

                                                Cell cellBranch2 = row1.createCell((short) 2);
                                                cellBranch2.setCellValue(sMSTOrMSN);
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                Cell cellBranch3 = row1.createCell((short) 3);
                                                cellBranch3.setCellValue(EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME));
                                                cellBranch3.setCellStyle(my_styleBranch);

                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(sCMNDOrHC);
                                                cellBranch4.setCellStyle(my_styleBranch);
                                                
                                                String strDeviceUUID = "";
                                                if(temp1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                    strDeviceUUID = temp1.SERVICE_UUID;
                                                }
                                                Cell cellBranch41 = row1.createCell((short) 5);
                                                cellBranch41.setCellValue(strDeviceUUID);
                                                cellBranch41.setCellStyle(my_styleBranch);

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
                                                if(isHasStateTypeRequest == true)
                                                {
                                                    Cell cellBranch8 = row1.createCell((short) 9);
                                                    cellBranch8.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_ATTR_STATE_DESC));
                                                    cellBranch8.setCellStyle(my_styleBranch);

                                                    Cell cellBranch9 = row1.createCell((short) 10);
                                                    cellBranch9.setCellValue(EscapeUtils.CheckTextNull(temp1.SERVICE_TYPE_DESC));
                                                    cellBranch9.setCellStyle(my_styleBranch);
                                                    iCountValue = 10;
                                                }
                                                if(isHasCREATED_BY == true)
                                                {
                                                    iCountValue = iCountValue+1;
                                                    Cell cellBranch10 = row1.createCell((short) iCountValue);
                                                    cellBranch10.setCellValue(EscapeUtils.CheckTextNull(temp1.CREATED_BY));
                                                    cellBranch10.setCellStyle(my_styleBranch);
                                                }
                                                if(isHasBranch == true)
                                                {
                                                    iCountValue = iCountValue+1;
                                                    Cell cellBranch10 = row1.createCell((short) iCountValue);
                                                    cellBranch10.setCellValue(EscapeUtils.CheckTextNull(temp1.BRANCH_DESC));
                                                    cellBranch10.setCellStyle(my_styleBranch);
                                                }
                                                Cell cellBranch12 = row1.createCell((short) iCountValue + 1);
                                                cellBranch12.setCellValue(EscapeUtils.CheckTextNull(temp1.CREATED_DT));
                                                cellBranch12.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch14 = row1.createCell((short) iCountValue + 2);
                                                cellBranch14.setCellValue(EscapeUtils.CheckTextNull(temp1.OPERATED_DT));
                                                cellBranch14.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch13 = row1.createCell((short) iCountValue + 3);
                                                cellBranch13.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_SN));
                                                cellBranch13.setCellStyle(my_styleBranch);
                                                
                                                String valueTokenSN = EscapeUtils.CheckTextNull(temp1.TOKEN_SN);
                                                if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                Cell cellBranch15 = row1.createCell((short) iCountValue + 4);
                                                cellBranch15.setCellValue(valueTokenSN);
                                                cellBranch15.setCellStyle(my_styleBranch);

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
                            case "exportcertlist": {
                                //<editor-fold defaultstate="collapsed" desc="exportcertlist">
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
                                String SessRoleID_ID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                Config conf = new Config();
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
                                String TAX_CODE = (String) request.getSession(false).getAttribute("sessTAX_CODERenewCert");
                                String BUDGET_CODE = (String) request.getSession(false).getAttribute("sessBUDGET_CODERenewCert");
                                String DECISION = (String) request.getSession(false).getAttribute("sessDECISIONRenewCert");
                                String P_ID = (String) request.getSession(false).getAttribute("sessP_IDRenewCert");
                                String CCCD = (String) request.getSession(false).getAttribute("sessCCCDRenewCert");
                                String PASSPORT = (String) request.getSession(false).getAttribute("sessPASSPORTRenewCert");
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
//                                else {
//                                    BranchOffice = SessUserAgentID;
//                                }
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
                                    String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
//                                    com.S_BO_CERTIFICATION_LIST_EXPORT(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
//                                        EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(CERT_SN),
//                                        EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
//                                        EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
//                                        EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(TAX_CODE),
//                                        EscapeUtils.escapeHtmlSearch(BUDGET_CODE), EscapeUtils.escapeHtmlSearch(P_ID),
//                                        EscapeUtils.escapeHtmlSearch(PASSPORT), EscapeUtils.escapeHtmlSearch(BranchOffice),
//                                        USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
//                                        EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),
//                                        IsByOwner, EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
//                                        loginLanguage, rsPgin, EscapeUtils.escapeHtmlSearch(CCCD), sessTreeArrayBranchID, EscapeUtils.escapeHtmlSearch(DECISION));
                                    if(rsPgin[0].length > 0)
                                    {
                                        String queryString = getServletContext().getRealPath("/");
                                        String outputDirectory = queryString;
                                        String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                        FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                        inputStream.close();
                                        String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                        String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
                                        String sCellUserCreate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_USER_CREATE).trim();
                                        String sCellMST = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_MST).trim();
                                        String sCellSTATE = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_STATE).trim();
                                        String sCellCompany = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COMPANY).trim();
                                        String sCellCMND = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CMND).trim();
                                        String sDeviceUUID = "UUID thiết bị";
                                        String sCellPesonal = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PESONAL).trim();
                                        String sCellProfile = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PROFILE).trim();
                                        String sCellRequestType = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_REQUEST_TYPE).trim();
                                        String sCellDateCreate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_CREATE).trim();
                                        String sCellDateGen = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_GEN).trim();
                                        String sCellDateEffective = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_EFFECTIVE).trim();
                                        String sCellDateRevoke = "Ngày thu hồi";
                                        String sCertType = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CERT_TYPE).trim();
                                        String sCertAttrState = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_REQUEST_STATE).trim();
                                        String sTokenSN = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_TOKEN_CODE).trim();
                                        String sCertSN = "Số sê-ri CTS";
                                        String sTokenState = "Trạng thái Token";
                                        String sReqTokenDate = "Ngày yêu cầu khóa Token";
                                        SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                        wb.setCompressTempFiles(true);
                                        CreationHelper createHelper = wb.getCreationHelper();
                                        
                                        //<editor-fold defaultstate="collapsed" desc="### SHEET 1">
                                        if(rsPgin[0][0].ID != 0)
                                        {
//                                            SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
                                            SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("DANH SÁCH CTS");
                                            sheet.setRandomAccessWindowSize(100);
                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                            sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                            sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                            sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                            sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                            sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                            sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CERT_TYPE);
                                            sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STATE);
                                            int iCountTitle = 8;
                                            if(isHasStateTypeRequest == true)
                                            {
                                                sheet.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_STATE);
                                                sheet.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                                iCountTitle = 10;
                                            }
                                            if(isHasCREATED_BY == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                sheet.setColumnWidth(iCountTitle, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                            }
                                            if(isHasBranch == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                sheet.setColumnWidth(iCountTitle, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                            }
                                            sheet.setColumnWidth(iCountTitle+1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                            sheet.setColumnWidth(iCountTitle+2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                            sheet.setColumnWidth(iCountTitle+3, 24 * 255);
                                            sheet.setColumnWidth(iCountTitle+4, 24 * 255);
                                            sheet.setColumnWidth(iCountTitle+5, 32 * 255);
                                            sheet.setColumnWidth(iCountTitle+6, 18 * 255);
                                            sheet.setColumnWidth(iCountTitle+7, 18 * 255);
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
                                            cell5.setCellValue(sCellCompany);
                                            cell5.setCellStyle(my_style);

                                            Cell cell3 = row.createCell((short) 2);
                                            cell3.setCellValue(sCellMST);
                                            cell3.setCellStyle(my_style);

                                            Cell cell7 = row.createCell((short) 3);
                                            cell7.setCellValue(sCellPesonal);
                                            cell7.setCellStyle(my_style);

                                            Cell cell6 = row.createCell((short) 4);
                                            cell6.setCellValue(sCellCMND);
                                            cell6.setCellStyle(my_style);

                                            Cell cell61 = row.createCell((short) 5);
                                            cell61.setCellValue(sDeviceUUID);
                                            cell61.setCellStyle(my_style);

                                            Cell cell9 = row.createCell((short) 6);
                                            cell9.setCellValue(sCellProfile);
                                            cell9.setCellStyle(my_style);

                                            Cell cell8 = row.createCell((short) 7);
                                            cell8.setCellValue(sCertType);
                                            cell8.setCellStyle(my_style);

                                            Cell cell4 = row.createCell((short) 8);
                                            cell4.setCellValue(sCellSTATE);
                                            cell4.setCellStyle(my_style);
                                            iCountTitle = 8;
                                            if(isHasStateTypeRequest == true)
                                            {
                                                Cell cell41 = row.createCell((short) 9);
                                                cell41.setCellValue(sCertAttrState);
                                                cell41.setCellStyle(my_style);

                                                Cell cell10 = row.createCell((short) 10);
                                                cell10.setCellValue(sCellRequestType);
                                                cell10.setCellStyle(my_style);
                                                iCountTitle = 10;
                                            }
                                            if(isHasCREATED_BY == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                Cell cell2 = row.createCell((short) iCountTitle);
                                                cell2.setCellValue(sCellUserCreate);
                                                cell2.setCellStyle(my_style);
                                            }
                                            if(isHasBranch == true)
                                            {
                                                iCountTitle = iCountTitle+1;
                                                Cell cell1 = row.createCell((short) iCountTitle);
                                                cell1.setCellValue(sCellAgency);
                                                cell1.setCellStyle(my_style);
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
                                            
                                            Cell cell1Revoke = row.createCell((short) iCountTitle+4);
                                            cell1Revoke.setCellValue(sCellDateRevoke);
                                            cell1Revoke.setCellStyle(my_style);

                                            Cell cell12 = row.createCell((short) iCountTitle+5);
                                            cell12.setCellValue(sCertSN);
                                            cell12.setCellStyle(my_style);

                                            Cell cell15 = row.createCell((short) iCountTitle+6);
                                            cell15.setCellValue(sTokenSN);
                                            cell15.setCellStyle(my_style);
                                            
                                            Cell cell16 = row.createCell((short) iCountTitle+7);
                                            cell16.setCellValue(sTokenState);
                                            cell16.setCellStyle(my_style);

                                            int i = 1;
                                            int k = 0;
                                            for (CERTIFICATION temp1 : rsPgin[0]) {
                                                if (k == 0) {
                                                    k = 1;
                                                } else {
                                                    k++;
                                                }
                                                String sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.TAX_CODE);
                                                if("".equals(sMSTOrMSN)) {
                                                    sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.BUDGET_CODE);
                                                }
                                                if("".equals(sMSTOrMSN)) {
                                                    sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.DECISION);
                                                }
                                                String sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_ID);
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_EID);
                                                }
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.PASSPORT);
                                                }
                                                Row row1 = sheet.createRow(i);
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                Font fontBranch = wb.createFont();
                                                fontBranch.setFontName("Arial");
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
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                Cell cellBranch3 = row1.createCell((short) 3);
                                                cellBranch3.setCellValue(EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME));
                                                cellBranch3.setCellStyle(my_styleBranch);

                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(sCMNDOrHC);
                                                cellBranch4.setCellStyle(my_styleBranch);

                                                String strDeviceUUID = "";
                                                if(temp1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                    strDeviceUUID = temp1.SERVICE_UUID;
                                                }
                                                Cell cellBranch41 = row1.createCell((short) 5);
                                                cellBranch41.setCellValue(strDeviceUUID);
                                                cellBranch41.setCellStyle(my_styleBranch);

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
                                                
                                                Cell cellValueEffective = row1.createCell((short) iCountValue + 3);
                                                Date dateEffective = CommonFunction.convertStringToDate(temp1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                if(dateEffective != null) {
                                                    cellValueEffective.setCellValue(dateEffective);
                                                } else {
                                                    cellValueEffective.setCellValue("");
                                                }
                                                cellValueEffective.setCellStyle(my_styleBranchDate);
                                                
                                                Cell cellValueRevoke = row1.createCell((short) iCountValue + 4);
                                                Date dateRevoke = CommonFunction.convertStringToDate(temp1.REVOKED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                if(dateRevoke != null) {
                                                    cellValueRevoke.setCellValue(dateRevoke);
                                                } else {
                                                    cellValueRevoke.setCellValue("");
                                                }
                                                cellValueRevoke.setCellStyle(my_styleBranchDate);

                                                Cell cellBranch13 = row1.createCell((short) iCountValue + 5);
                                                cellBranch13.setCellValue(EscapeUtils.CheckTextNull(temp1.CERTIFICATION_SN));
                                                cellBranch13.setCellStyle(my_styleBranch);

                                                String valueTokenSN = EscapeUtils.CheckTextNull(temp1.TOKEN_SN);
                                                if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                Cell cellBranch15 = row1.createCell((short) iCountValue + 6);
                                                cellBranch15.setCellValue(valueTokenSN);
                                                cellBranch15.setCellStyle(my_styleBranch);
                                                
                                                Cell cellBranch16 = row1.createCell((short) iCountValue + 7);
                                                cellBranch16.setCellValue(temp1.TOKEN_STATE_DESC);
                                                cellBranch16.setCellStyle(my_styleBranch);
                                                i++;
                                            }
                                        }
                                        //</editor-fold>
                                        
                                        //<editor-fold defaultstate="collapsed" desc="## SHEET 2">
                                        rsPgin = new CERTIFICATION[1][];
//                                        com.S_BO_CERTIFICATION_LIST_TOKEN_LOCK(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
//                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(CERT_SN),
//                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
//                                            EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
//                                            EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(TAX_CODE),
//                                            EscapeUtils.escapeHtmlSearch(BUDGET_CODE), EscapeUtils.escapeHtmlSearch(P_ID),
//                                            EscapeUtils.escapeHtmlSearch(PASSPORT), EscapeUtils.escapeHtmlSearch(BranchOffice),
//                                            USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
//                                            EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),
//                                            IsByOwner, EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
//                                            loginLanguage, rsPgin, EscapeUtils.escapeHtmlSearch(CCCD), EscapeUtils.escapeHtmlSearch(DECISION));
                                        if(rsPgin[0].length > 0)
                                        {
                                            SXSSFSheet sheet2 = (SXSSFSheet) wb.createSheet("CTS CỦA TOKEN ĐÃ KHÓA");
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
                                                String sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.TAX_CODE);
                                                if("".equals(sMSTOrMSN)) {
                                                    sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.BUDGET_CODE);
                                                }
                                                if("".equals(sMSTOrMSN)) {
                                                    sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.DECISION);
                                                }
                                                String sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_ID);
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_EID);
                                                }
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.PASSPORT);
                                                }
                                                Row row1 = sheet2.createRow(i);
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                Font fontBranch = wb.createFont();
                                                fontBranch.setFontName("Arial");
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
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                Cell cellBranch3 = row1.createCell((short) 3);
                                                cellBranch3.setCellValue(EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME));
                                                cellBranch3.setCellStyle(my_styleBranch);

                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(sCMNDOrHC);
                                                cellBranch4.setCellStyle(my_styleBranch);

                                                String strDeviceUUID = "";
                                                if(temp1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                    strDeviceUUID = temp1.SERVICE_UUID;
                                                }
                                                Cell cellBranch41 = row1.createCell((short) 5);
                                                cellBranch41.setCellValue(strDeviceUUID);
                                                cellBranch41.setCellStyle(my_styleBranch);

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
                                                cellBranch13.setCellStyle(my_styleBranch);

                                                String valueTokenSN = EscapeUtils.CheckTextNull(temp1.TOKEN_SN);
                                                if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                Cell cellBranch15 = row1.createCell((short) iCountValue + 4);
                                                cellBranch15.setCellValue(valueTokenSN);
                                                cellBranch15.setCellStyle(my_styleBranch);

                                                Cell cellBranch152 = row1.createCell((short) iCountValue + 5);
                                                cellBranch152.setCellValue(temp1.CREATED_LOCK_REQUEST_DT);
                                                cellBranch152.setCellStyle(my_styleBranch);
                                                i++;
                                            }
                                        }
                                        //</editor-fold>
                                        
                                        //<editor-fold defaultstate="collapsed" desc="## SHEET 3">
                                        rsPgin = new CERTIFICATION[1][];
//                                        com.S_BO_CERTIFICATION_LIST_TOKEN_WAIT_TO_LOCK(EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(ToCreateDate),
//                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_ATTR_TYPE), EscapeUtils.escapeHtmlSearch(CERT_SN),
//                                            EscapeUtils.escapeHtmlSearch(CERTIFICATION_PURPOSE),
//                                            EscapeUtils.escapeHtmlSearch(PERSONAL_NAME), EscapeUtils.escapeHtmlSearch(COMPANY_NAME),
//                                            EscapeUtils.escapeHtmlSearch(DOMAIN_NAME), EscapeUtils.escapeHtmlSearch(TAX_CODE),
//                                            EscapeUtils.escapeHtmlSearch(BUDGET_CODE), EscapeUtils.escapeHtmlSearch(P_ID),
//                                            EscapeUtils.escapeHtmlSearch(PASSPORT), EscapeUtils.escapeHtmlSearch(BranchOffice),
//                                            USER, SessRoleID_ID, SessUserAgentID, IsTokenLost, EscapeUtils.escapeHtmlSearch(TOKEN_SN),
//                                            EscapeUtils.escapeHtmlSearch(FORM_FACTOR), EscapeUtils.escapeHtmlSearch(DEVICE_UUID_SEARCH),
//                                            IsByOwner, EscapeUtils.escapeHtmlSearch(CERTIFICATION_AUTHORITY_SEARCH), EscapeUtils.escapeHtmlSearch(SERVICE_TYPE_SEARCH),
//                                            loginLanguage, rsPgin, EscapeUtils.escapeHtmlSearch(CCCD), EscapeUtils.escapeHtmlSearch(DECISION));
                                        if(rsPgin[0].length > 0)
                                        {
                                            SXSSFSheet sheet2 = (SXSSFSheet) wb.createSheet("CTS CỦA TOKEN CHỜ KHÓA");
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
                                                String sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.TAX_CODE);
                                                if("".equals(sMSTOrMSN)) {
                                                    sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.BUDGET_CODE);
                                                }
                                                if("".equals(sMSTOrMSN)) {
                                                    sMSTOrMSN = EscapeUtils.CheckTextNull(temp1.DECISION);
                                                }
                                                String sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_ID);
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.P_EID);
                                                }
                                                if("".equals(sCMNDOrHC)) {
                                                    sCMNDOrHC = EscapeUtils.CheckTextNull(temp1.PASSPORT);
                                                }
                                                Row row1 = sheet2.createRow(Integer.valueOf(i));
                                                CellStyle my_styleBranch = wb.createCellStyle();
                                                Font fontBranch = wb.createFont();
                                                fontBranch.setFontName("Arial");
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
                                                cellBranch2.setCellStyle(my_styleBranch);

                                                Cell cellBranch3 = row1.createCell((short) 3);
                                                cellBranch3.setCellValue(EscapeUtils.CheckTextNull(temp1.PERSONAL_NAME));
                                                cellBranch3.setCellStyle(my_styleBranch);

                                                Cell cellBranch4 = row1.createCell((short) 4);
                                                cellBranch4.setCellValue(sCMNDOrHC);
                                                cellBranch4.setCellStyle(my_styleBranch);

                                                String strDeviceUUID = "";
                                                if(temp1.CERTIFICATION_PURPOSE_ID == Definitions.CONFIG_CERTTYPE_ID_DEVICE) {
                                                    strDeviceUUID = temp1.SERVICE_UUID;
                                                }
                                                Cell cellBranch41 = row1.createCell((short) 5);
                                                cellBranch41.setCellValue(strDeviceUUID);
                                                cellBranch41.setCellStyle(my_styleBranch);

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
                                                cellBranch13.setCellStyle(my_styleBranch);

                                                String valueTokenSN = EscapeUtils.CheckTextNull(temp1.TOKEN_SN);
                                                if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                Cell cellBranch15 = row1.createCell((short) iCountValue + 4);
                                                cellBranch15.setCellValue(valueTokenSN);
                                                cellBranch15.setCellStyle(my_styleBranch);

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
                            case "exporttokenreport_bk": {
                                //<editor-fold defaultstate="collapsed" desc="exporttokenreport_bk">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
//                                    String SessRoleID_ID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
//                                    String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
//                                    String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
//                                    int[] pIa = new int[1];
//                                    int[] pIb = new int[1];
                                    Config conf = new Config();
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_TOKEN_EXPORT + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    CommonFunction.LogDebugString(log, "ExportTokenReport", "PathURL: " + strURLPath);
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
                                            String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                            String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
                                            String sCellVertion = "Phiên bản Token";
                                            String sCellTokenSN = "Mã Token";
                                            String sCellState = "Trạng thái";// conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_STATE).trim();
                                            String sCellDateImportExport = "Ngày xuất kho cho đại lý";
                                            HSSFWorkbook wb = new HSSFWorkbook();
                                            HSSFSheet sheet = wb.createSheet();
                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                            sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_TOKEN_SN);
                                            sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_TOKEN_VERSION);
                                            sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STATE);
                                            sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
                                            sheet.setColumnWidth(5, 32 * 255);
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
                                                HSSFRow row1 = sheet.createRow(i);
                                                HSSFCellStyle my_styleBranch = wb.createCellStyle();
                                                HSSFFont fontBranch = wb.createFont();

                                                fontBranch.setFontName("Arial");
    //                                            fontBranch.setColor((short) 12);
                                                my_styleBranch.setFont(fontBranch);
    //                                            my_styleBranch.setFillBackgroundColor((short) 9);

                                                Cell cellBranch = row1.createCell((short) 0);
                                                cellBranch.setCellValue(k);
                                                cellBranch.setCellStyle(my_styleBranch);

                                                Cell cellBranch1 = row1.createCell((short) 1);
                                                cellBranch1.setCellValue(temp1.TOKEN_SN);
                                                cellBranch1.setCellStyle(my_styleBranch);

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
                                                cellBranch5.setCellValue(temp1.IMPORT_EXPORT_DT);
                                                cellBranch5.setCellStyle(my_styleBranch);

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
                            case "exporttokenreport": {
                                //<editor-fold defaultstate="collapsed" desc="exporttokenreport">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
                                    Config conf = new Config();
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
                                            String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                            String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
                                            String sCellVertion = "Phiên bản Token";
                                            String sCellTokenSN = "Mã Token";
                                            String sCellState = "Trạng thái";// conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_STATE).trim();
                                            String sCellDateImportExport = "Ngày xuất kho cho đại lý";
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
                                                cellBranch1.setCellStyle(my_styleBranch);

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
//                            case "exportcollationcertlist_bk": {
//                                //<editor-fold defaultstate="collapsed" desc="exportcollationcertlist_bk">
//                                String anticsrf = request.getParameter("CsrfToken");
//                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
//                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
//                                    CommonFunction clsCom = new CommonFunction();
//                                    String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
////                                    String SessRoleID_ID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
////                                    String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
////                                    String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
//                                    Config conf = new Config();
//                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
//                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
//                                    if (!directory.exists()){
//                                        directory.mkdir();
//                                    }
//                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
//                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_TOKEN_EXPORT + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
//                                    String strURLPath = pPathURL + sFileName;
//                                    CommonFunction.LogDebugString(log, "ExportTokenReport", "PathURL: " + strURLPath);
//                                    String ToCreateDate = (String) request.getSession(false).getAttribute("sessMonthStatusCollation");
//                                    String FromCreateDate = (String) request.getSession(false).getAttribute("sessYearStatusCollation");
//                                    String STATUS_COLLATION = (String) request.getSession(false).getAttribute("sessStatusCollation");
//                                    String idBranchOffice = (String) request.getSession(false).getAttribute("sessBranchOfficeStatusCollation");
//                                    String SessUserID = request.getSession(false).getAttribute("UserID").toString().trim();
//                                    String SessRoleID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
//                                    String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
//                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(STATUS_COLLATION)) {
//                                        STATUS_COLLATION = "";
//                                    }
//                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idBranchOffice)) {
//                                        idBranchOffice = "";
//                                    }
//                                    if(SessAgentID.equals(Definitions.CONFIG_AGENT_ROOT)) {
//                                        SessUserID = "";
//                                    } else {
//                                        if(!SessRoleID.equals(Definitions.CONFIG_ROLE_ID_AGENT_USER)) {
//                                            SessUserID = "";
//                                        }
//                                    }
//                                    CommonFunction.LogDebugString(log, "Export Collation Data", "Mounth: " + FromCreateDate + "; Year: " + ToCreateDate + "; STATUS_COLLATION: " + STATUS_COLLATION);
//                                    if (!"".equals(countList)) {
//                                        CERTIFICATION[][] rsPgin;
//                                        rsPgin = new CERTIFICATION[1][];
//                                        com.S_BO_CERTIFICATION_CROSS_CHECK_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate),
//                                            EscapeUtils.escapeHtmlSearch(FromCreateDate),
//                                            EscapeUtils.escapeHtmlSearch(idBranchOffice),STATUS_COLLATION, SessUserID,
//                                            loginLanguage, rsPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO, Integer.parseInt(countList));
//                                        if(rsPgin[0].length > 0)
//                                        {
//                                            String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
//                                            String sCellApproveDate = "Ngày duyệt"; //conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
//                                            String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
//                                            String sCellUser= conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_USER_CREATE).trim();
//                                            String sCellState = "Trạng thái";// conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_STATE).trim();
//                                            String sCellMST = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_MST).trim();
//                                            String sCellCMND = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CMND).trim();
//                                            String sCellCompany = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COMPANY).trim();
//                                            String sCellPersonal = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PESONAL).trim();
//                                            String sCellProfile = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PROFILE).trim();
//                                            String sCellRequestType = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_REQUEST_TYPE).trim();
//                                            String sCellCreateDate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_CREATE).trim();
//                                            String sCellGenDate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_GEN).trim();
//                                            String sCellCertState = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_STATE).trim();
//                                            String sCellMounth = "Tháng đối soát";
//                                            String sCellChangeDate = "Ngày đổi trạng thái đối soát";
//                                            String sCellCollationState = "Trạng thái hồ sơ";
//                                            String sCellProfileFee = "Phí dịch vụ";
//                                            String sCellTokenQuality = "Số lượng Token";
//                                            String sCellTokenFee = "Phí Token";
//                                            String sCellProvince = "Tỉnh thành";
//                                            String sCellProfileFine = "Tiền phạt hồ sơ";
//                                            String sCellCollationDate = "Ngày đối soát"; 
//                                            String sCellDeclineDate = "Ngày hủy"; 
//                                            String sCellDeclineNumber = "Số ngày hủy"; 
//                                            HSSFWorkbook wb = new HSSFWorkbook();
//                                            HSSFCellStyle my_style = wb.createCellStyle();
//                                            HSSFFont font = wb.createFont();
//                                            font.setBoldweight((short) 700);
//                                            font.setFontName("Verdana");
//                                            my_style.setFont(font);
//                                            my_style.setFillBackgroundColor((short) 9);
//                                            my_style.setAlignment((short) 2);
//                                            my_style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                            my_style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                            my_style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                            my_style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//                                            int i;
//                                            int k;
//                                            HSSFSheet sheet = wb.createSheet("TONG HOP");
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET Aggregate report - close">
////                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
////                                            sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
////                                            sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
////                                            sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
////                                            sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
////                                            sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
////                                            sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
////                                            sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
////                                            sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
////                                            sheet.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
////                                            sheet.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STATE);
////                                            sheet.setColumnWidth(11, 26 * 255);
////                                            sheet.setColumnWidth(12, 30 * 255);
////                                            HSSFRow row = sheet.createRow(0);
////                                            Cell cell = row.createCell((short) 0);
////                                            cell.setCellValue(sCellSTT);
////                                            cell.setCellStyle(my_style);
////
////                                            Cell cell1 = row.createCell((short) 1);
////                                            cell1.setCellValue(sCellApproveDate);
////                                            cell1.setCellStyle(my_style);
////
////                                            Cell cell2 = row.createCell((short) 2);
////                                            cell2.setCellValue(sCellAgency);
////                                            cell2.setCellStyle(my_style);
////
////                                            Cell cell3 = row.createCell((short) 3);
////                                            cell3.setCellValue(sCellUser);
////                                            cell3.setCellStyle(my_style);
////
////                                            Cell cell4 = row.createCell((short) 4);
////                                            cell4.setCellValue(sCellMST);
////                                            cell4.setCellStyle(my_style);
////
////                                            Cell cell5 = row.createCell((short) 5);
////                                            cell5.setCellValue(sCellCompany);
////                                            cell5.setCellStyle(my_style);
////                                            
////                                            Cell cell6 = row.createCell((short) 6);
////                                            cell6.setCellValue(sCellCMND);
////                                            cell6.setCellStyle(my_style);
////                                            
////                                            Cell cell7 = row.createCell((short) 7);
////                                            cell7.setCellValue(sCellPersonal);
////                                            cell7.setCellStyle(my_style);
////                                            
////                                            Cell cell8 = row.createCell((short) 8);
////                                            cell8.setCellValue(sCellProfile);
////                                            cell8.setCellStyle(my_style);
////                                            
////                                            Cell cell9 = row.createCell((short) 9);
////                                            cell9.setCellValue(sCellRequestType);
////                                            cell9.setCellStyle(my_style);
////                                            
////                                            Cell cell10 = row.createCell((short) 10);
////                                            cell10.setCellValue(sCellState);
////                                            cell10.setCellStyle(my_style);
////                                            
////                                            Cell cell11 = row.createCell((short) 11);
////                                            cell11.setCellValue(sCellMounth);
////                                            cell11.setCellStyle(my_style);
////                                            
////                                            Cell cell12 = row.createCell((short) 12);
////                                            cell12.setCellValue(sCellChangeDate);
////                                            cell12.setCellStyle(my_style);
////                                            i = 1;
////                                            k = 0;
////                                            for (CERTIFICATION temp1 : rsPgin[0]) {
////                                                if (k == 0) {
////                                                    k = 1;
////                                                } else {
////                                                    k++;
////                                                }
////                                                HSSFRow row1 = sheet.createRow(Integer.valueOf(i));
////                                                HSSFCellStyle my_styleBranch = wb.createCellStyle();
////                                                HSSFFont fontBranch = wb.createFont();
////
////                                                fontBranch.setFontName("Arial");
////    //                                            fontBranch.setColor((short) 12);
////                                                my_styleBranch.setFont(fontBranch);
////    //                                            my_styleBranch.setFillBackgroundColor((short) 9);
////
////                                                Cell cellBranch = row1.createCell((short) 0);
////                                                cellBranch.setCellValue(k);
////                                                cellBranch.setCellStyle(my_styleBranch);
////
////                                                Cell cellBranch1 = row1.createCell((short) 1);
////                                                cellBranch1.setCellValue(temp1.ISSUED_DT);
////                                                cellBranch1.setCellStyle(my_styleBranch);
////
////                                                Cell cellBranch2 = row1.createCell((short) 2);
////                                                cellBranch2.setCellValue(temp1.BRANCH_DESC);
////                                                cellBranch2.setCellStyle(my_styleBranch);
////
////                                                Cell cellBranch3 = row1.createCell((short) 3);
////                                                cellBranch3.setCellValue(temp1.CREATED_BY);
////                                                cellBranch3.setCellStyle(my_styleBranch);
////
////                                                String sTAX_CODE = temp1.TAX_CODE;
////                                                if(!"".equals(sTAX_CODE)){sTAX_CODE = "'"+sTAX_CODE;}
////                                                Cell cellBranch4 = row1.createCell((short) 4);
////                                                cellBranch4.setCellValue(sTAX_CODE);
////                                                cellBranch4.setCellStyle(my_styleBranch);
////
////                                                Cell cellBranch5 = row1.createCell((short) 5);
////                                                cellBranch5.setCellValue(temp1.COMPANY_NAME);
////                                                cellBranch5.setCellStyle(my_styleBranch);
////
////                                                String sP_ID = temp1.P_ID;
////                                                if(!"".equals(sP_ID)){sP_ID = "'"+sP_ID;}
////                                                Cell cellBranch6 = row1.createCell((short) 6);
////                                                cellBranch6.setCellValue(sP_ID);
////                                                cellBranch6.setCellStyle(my_styleBranch);
////
////                                                Cell cellBranch7 = row1.createCell((short) 7);
////                                                cellBranch7.setCellValue(temp1.PERSONAL_NAME);
////                                                cellBranch7.setCellStyle(my_styleBranch);
////
////                                                Cell cellBranch8 = row1.createCell((short) 8);
////                                                cellBranch8.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
////                                                cellBranch8.setCellStyle(my_styleBranch);
////
////                                                Cell cellBranch9 = row1.createCell((short) 9);
////                                                cellBranch9.setCellValue(temp1.CERTIFICATION_ATTR_TYPE_DESC);
////                                                cellBranch9.setCellStyle(my_styleBranch);
////
////                                                String sCollated = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COLLATION_NO).trim();
////                                                if(temp1.CROSS_CHECK_ENABLED == true) {
////                                                    sCollated = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COLLATION_YES).trim();
////                                                }
////                                                Cell cellBranch10 = row1.createCell((short) 10);
////                                                cellBranch10.setCellValue(sCollated);
////                                                cellBranch10.setCellStyle(my_styleBranch);
////
////                                                Cell cellBranch11 = row1.createCell((short) 11);
////                                                cellBranch11.setCellValue(temp1.CROSS_CHECKED_MOUNTH);
////                                                cellBranch11.setCellStyle(my_styleBranch);
////
////                                                Cell cellBranch12 = row1.createCell((short) 12);
////                                                cellBranch12.setCellValue(temp1.CROSS_CHECKED_DT);
////                                                cellBranch12.setCellStyle(my_styleBranch);
////
////                                                i++;
////                                            }
//                                            //</editor-fold>
//                                            
//                                            BRANCH[][] rsBranch = new BRANCH[1][];
//                                            String sBranchReport = "";
//                                            com.S_BO_BRANCH_DETAIL(EscapeUtils.escapeHtmlSearch(idBranchOffice), rsBranch);
//                                            if(rsPgin[0].length > 0) {
//                                                sBranchReport = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK);
//                                            }
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - HEADER">
//                                            i = 0;
//                                            HSSFRow rowSyntheticTitle;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            HSSFCellStyle my_styleSyntheticTitle = wb.createCellStyle();
//                                            HSSFFont fontBranchSyntheticTitle = wb.createFont();
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            fontBranchSyntheticTitle.setBoldweight((short) 700);
//                                            fontBranchSyntheticTitle.setFontName("Verdana");
//                                            my_styleSyntheticTitle.setFont(fontBranchSyntheticTitle);
//
//                                            Cell cellSyntheticTitle;
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM");
//                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
//                                            i = i+1;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("Độc lập - Tự do - Hạnh phúc");
//                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
//                                            i = i+1;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 6, 8));
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 5);
//                                            cellSyntheticTitle.setCellValue("............, ngày…. Tháng….. Năm 2020");
//                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
//                                            i = i+1;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("BIÊN BẢN ĐỐI SOÁT PHÁT TRIỂN CHỨNG THƯ SỐ THÁNG " + ToCreateDate.trim() + "/" + FromCreateDate.trim());
//                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
//                                            i = i+1;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("GIỮA CÔNG TY CỔ PHẦN HỖ TRỢ DOANH NGHIỆP VÀ " + sBranchReport);
//                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
//                                            i = i+2;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            HSSFCellStyle my_styleSyntheticTitleDN = wb.createCellStyle();
//                                            HSSFFont fontBranchSyntheticTitleDN = wb.createFont();
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            fontBranchSyntheticTitleDN.setBoldweight((short) 700);
//                                            fontBranchSyntheticTitleDN.setFontName("Verdana");
//                                            my_styleSyntheticTitleDN.setFont(fontBranchSyntheticTitleDN);
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("SỐ LƯỢNG KHÁCH HÀNG DOANH NGHIỆP PHÁT TRIỂN ĐƯỢC TRONG THÁNG");
//                                            my_styleSyntheticTitleDN.setAlignment(my_styleSyntheticTitleDN.ALIGN_LEFT);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleDN);
//                                            //</editor-fold>
//                                            
//                                            i = i + 1;
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - DN">
//                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
//                                            sheet.setColumnWidth(1, 18 * 255);
//                                            sheet.setColumnWidth(2, 18 * 255);
//                                            sheet.setColumnWidth(3, 16 * 255);
//                                            sheet.setColumnWidth(4, 16 * 255);
//                                            sheet.setColumnWidth(5, 18 * 255);
//                                            sheet.setColumnWidth(6, 16 * 255);
//                                            sheet.setColumnWidth(7, 28 * 255);
//                                            sheet.setColumnWidth(8, 28 * 255);
//                                            HSSFRow row = sheet.createRow(i);
//                                            Cell cell = row.createCell((short) 0);
//                                            cell.setCellValue(sCellSTT);
//                                            cell.setCellStyle(my_style);
//
//                                            Cell cell1 = row.createCell((short) 1);
//                                            cell1.setCellValue("GÓI DỊCH VỤ");
//                                            cell1.setCellStyle(my_style);
//
//                                            Cell cell2 = row.createCell((short) 2);
//                                            cell2.setCellValue("SỐ LƯỢNG KH");
//                                            cell2.setCellStyle(my_style);
//
//                                            Cell cell3 = row.createCell((short) 3);
//                                            cell3.setCellValue("ĐƠN GIÁ");
//                                            cell3.setCellStyle(my_style);
//
//                                            Cell cell4 = row.createCell((short) 4);
//                                            cell4.setCellValue("THÀNH TIỀN");
//                                            cell4.setCellStyle(my_style);
//
//                                            Cell cell5 = row.createCell((short) 5);
//                                            cell5.setCellValue("TỶ LỆ HOA HỒNG");
//                                            cell5.setCellStyle(my_style);
//                                            
//                                            Cell cell6 = row.createCell((short) 6);
//                                            cell6.setCellValue("HOA HỒNG");
//                                            cell6.setCellStyle(my_style);
//                                            
//                                            Cell cell7 = row.createCell((short) 7);
//                                            cell7.setCellValue("ĐƠN GIÁ PHẢI TRẢ");
//                                            cell7.setCellStyle(my_style);
//                                            
//                                            Cell cell8 = row.createCell((short) 8);
//                                            cell8.setCellValue("TỔNG TIỀN PHẢI TRẢ");
//                                            cell8.setCellStyle(my_style);
//                                            i = i+1;
//                                            k = 0;
//                                            CERTIFICATION_REPORT_SUMMARY[][] rsReport;
//                                            rsReport = new CERTIFICATION_REPORT_SUMMARY[1][];
//                                            com.S_BO_REPORT_SUMMARY_ENTERPRISE(EscapeUtils.escapeHtmlSearch(idBranchOffice),
//                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
//                                                SessUserID, loginLanguage, rsReport);
//                                            double countCustomer = 0;
//                                            double intoMoney = 0;
//                                            double moneyRose = 0;
//                                            double sumMoney = 0;
//                                            boolean isHasReportSumaryDN = false;
//                                            for (CERTIFICATION_REPORT_SUMMARY temp1 : rsReport[0]) {
//                                                isHasReportSumaryDN = true;
//                                                if (k == 0) {
//                                                    k = 1;
//                                                } else {
//                                                    k++;
//                                                }
//                                                HSSFRow row1 = sheet.createRow(i);
//                                                HSSFCellStyle my_styleBranch = wb.createCellStyle();
//                                                HSSFFont fontBranch = wb.createFont();
//
//                                                fontBranch.setFontName("Verdana");
//    //                                            fontBranch.setColor((short) 12);
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//    //                                            my_styleBranch.setFillBackgroundColor((short) 9);
//
//                                                Cell cellBranch = row1.createCell((short) 0);
//                                                cellBranch.setCellValue(k);
//                                                cellBranch.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch1 = row1.createCell((short) 1);
//                                                cellBranch1.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
//                                                cellBranch1.setCellStyle(my_styleBranch);
//                                                
//                                                countCustomer = countCustomer+temp1.NUMBER_CUSTOMER;
//                                                Cell cellBranch2 = row1.createCell((short) 2);
//                                                cellBranch2.setCellValue(temp1.NUMBER_CUSTOMER);
//                                                cellBranch2.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch3 = row1.createCell((short) 3);
//                                                cellBranch3.setCellValue(temp1.CERTIFICATION_PROFILE_AMOUNT);
//                                                cellBranch3.setCellStyle(my_styleBranch);
//                                                
//                                                double intoMoneyItem = temp1.NUMBER_CUSTOMER * temp1.CERTIFICATION_PROFILE_AMOUNT;
//                                                intoMoney = intoMoney + intoMoneyItem;
//                                                Cell cellBranch4 = row1.createCell((short) 4);
//                                                cellBranch4.setCellValue(clsCom.convertMoneyDoubleZero(intoMoneyItem));
//                                                cellBranch4.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch5 = row1.createCell((short) 5);
//                                                cellBranch5.setCellValue(temp1.DISCOUNT_RATE + "%");
//                                                cellBranch5.setCellStyle(my_styleBranch);
//
//                                                double moneyRoseItem = (temp1.CERTIFICATION_PROFILE_AMOUNT / 100) * temp1.DISCOUNT_RATE;
//                                                moneyRose = moneyRose + moneyRoseItem;
//                                                Cell cellBranch6 = row1.createCell((short) 6);
//                                                cellBranch6.setCellValue(clsCom.convertMoneyDoubleZero(moneyRoseItem));
//                                                cellBranch6.setCellStyle(my_styleBranch);
//
//                                                double returnMoneyItem = temp1.CERTIFICATION_PROFILE_AMOUNT - moneyRoseItem;
//                                                Cell cellBranch7 = row1.createCell((short) 7);
//                                                cellBranch7.setCellValue(clsCom.convertMoneyDoubleZero(returnMoneyItem));
//                                                cellBranch7.setCellStyle(my_styleBranch);
//
//                                                double sumMoneyItem = temp1.NUMBER_CUSTOMER * returnMoneyItem;
//                                                sumMoney = sumMoney + sumMoneyItem;
//                                                Cell cellBranch8 = row1.createCell((short) 8);
//                                                cellBranch8.setCellValue(clsCom.convertMoneyDoubleZero(sumMoneyItem));
//                                                cellBranch8.setCellStyle(my_styleBranch);
//
//                                                i++;
//                                            }
//                                            if(isHasReportSumaryDN == true)
//                                            {
//                                                HSSFRow row1 = sheet.createRow(i);
//                                                HSSFCellStyle my_styleBranch = wb.createCellStyle();
//                                                HSSFFont fontBranch = wb.createFont();
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
//
//                                                fontBranch.setFontName("Verdana");
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//
//                                                Cell cellBranch = row1.createCell((short) 0);
//                                                cellBranch.setCellValue("TỔNG");
//                                                cellBranch.setCellStyle(my_style);
//                                                Cell cellBranch1 = row1.createCell((short) 1);
//                                                cellBranch1.setCellStyle(my_style);
//
////                                                Cell cellBranch1 = row1.createCell((short) 1);
////                                                cellBranch1.setCellValue("TỔNG");
////                                                cellBranch1.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch2 = row1.createCell((short) 2);
//                                                cellBranch2.setCellValue(countCustomer);
//                                                cellBranch2.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch3 = row1.createCell((short) 3);
//                                                cellBranch3.setCellValue("");
//                                                cellBranch3.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch4 = row1.createCell((short) 4);
//                                                cellBranch4.setCellValue(clsCom.convertMoneyDoubleZero(intoMoney));
//                                                cellBranch4.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch5 = row1.createCell((short) 5);
//                                                cellBranch5.setCellValue("");
//                                                cellBranch5.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch6 = row1.createCell((short) 6);
//                                                cellBranch6.setCellValue(clsCom.convertMoneyDoubleZero(moneyRose));
//                                                cellBranch6.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch7 = row1.createCell((short) 7);
//                                                cellBranch7.setCellValue("");
//                                                cellBranch7.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch8 = row1.createCell((short) 8);
//                                                cellBranch8.setCellValue(clsCom.convertMoneyDoubleZero(sumMoney));
//                                                cellBranch8.setCellStyle(my_styleBranch);
//                                            }
//                                            //</editor-fold>
//                                            
//                                            i = i+2;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            HSSFCellStyle my_styleSyntheticTitleCN = wb.createCellStyle();
//                                            HSSFFont fontBranchSyntheticTitleCN = wb.createFont();
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            fontBranchSyntheticTitleCN.setBoldweight((short) 700);
//                                            fontBranchSyntheticTitleCN.setFontName("Arial");
//                                            my_styleSyntheticTitleCN.setFont(fontBranchSyntheticTitleCN);
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("SỐ LƯỢNG KHÁCH HÀNG CÁ NHÂN PHÁT TRIỂN ĐƯỢC TRONG THÁNG");
//                                            my_styleSyntheticTitleCN.setAlignment(my_styleSyntheticTitleDN.ALIGN_LEFT);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleCN);
//                                            i = i + 1;
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - CN">
//                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
//                                            sheet.setColumnWidth(1, 18 * 255);
//                                            sheet.setColumnWidth(2, 18 * 255);
//                                            sheet.setColumnWidth(3, 16 * 255);
//                                            sheet.setColumnWidth(4, 16 * 255);
//                                            sheet.setColumnWidth(5, 18 * 255);
//                                            sheet.setColumnWidth(6, 16 * 255);
//                                            sheet.setColumnWidth(7, 28 * 255);
//                                            sheet.setColumnWidth(8, 28 * 255);
//                                            HSSFRow rowOneCN = sheet.createRow(i);
//                                            Cell celwOneCNl = rowOneCN.createCell((short) 0);
//                                            celwOneCNl.setCellValue(sCellSTT);
//                                            celwOneCNl.setCellStyle(my_style);
//
//                                            Cell celwOneCNl1 = rowOneCN.createCell((short) 1);
//                                            celwOneCNl1.setCellValue("GÓI DỊCH VỤ");
//                                            celwOneCNl1.setCellStyle(my_style);
//
//                                            Cell cellwOneCN2 = rowOneCN.createCell((short) 2);
//                                            cellwOneCN2.setCellValue("SL KH");
//                                            cellwOneCN2.setCellStyle(my_style);
//
//                                            Cell cellwOneCN3 = rowOneCN.createCell((short) 3);
//                                            cellwOneCN3.setCellValue("ĐƠN GIÁ");
//                                            cellwOneCN3.setCellStyle(my_style);
//
//                                            Cell cellwOneCN4 = rowOneCN.createCell((short) 4);
//                                            cellwOneCN4.setCellValue("THÀNH TIỀN");
//                                            cellwOneCN4.setCellStyle(my_style);
//
//                                            Cell cellwOneCN5 = rowOneCN.createCell((short) 5);
//                                            cellwOneCN5.setCellValue("TỶ LỆ HOA HỒNG");
//                                            cellwOneCN5.setCellStyle(my_style);
//                                            
//                                            Cell cellwOneCN6 = rowOneCN.createCell((short) 6);
//                                            cellwOneCN6.setCellValue("HOA HỒNG");
//                                            cellwOneCN6.setCellStyle(my_style);
//                                            
//                                            Cell cellwOneCN7 = rowOneCN.createCell((short) 7);
//                                            cellwOneCN7.setCellValue("ĐƠN GIÁ PHẢI TRẢ");
//                                            cellwOneCN7.setCellStyle(my_style);
//                                            
//                                            Cell cellwOneCN8 = rowOneCN.createCell((short) 8);
//                                            cellwOneCN8.setCellValue("TỔNG TIỀN PHẢI TRẢ");
//                                            cellwOneCN8.setCellStyle(my_style);
//                                            i = i+1;
//                                            k = 0;
//                                            rsReport = new CERTIFICATION_REPORT_SUMMARY[1][];
//                                            com.S_BO_REPORT_SUMMARY_PERSONAL(EscapeUtils.escapeHtmlSearch(idBranchOffice),
//                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
//                                                SessUserID, loginLanguage, rsReport);
//                                            countCustomer = 0;
//                                            intoMoney = 0;
//                                            moneyRose = 0;
//                                            sumMoney = 0;
//                                            boolean isHasReportSumaryCN = false;
//                                            for (CERTIFICATION_REPORT_SUMMARY temp1 : rsReport[0]) {
//                                                isHasReportSumaryCN = true;
//                                                if (k == 0) {
//                                                    k = 1;
//                                                } else {
//                                                    k++;
//                                                }
//                                                HSSFRow row1 = sheet.createRow(i);
//                                                HSSFCellStyle my_styleBranch = wb.createCellStyle();
//                                                HSSFFont fontBranch = wb.createFont();
//
//                                                fontBranch.setFontName("Verdana");
//    //                                            fontBranch.setColor((short) 12);
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//    //                                            my_styleBranch.setFillBackgroundColor((short) 9);
//
//                                                Cell cellBranch = row1.createCell((short) 0);
//                                                cellBranch.setCellValue(k);
//                                                cellBranch.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch1 = row1.createCell((short) 1);
//                                                cellBranch1.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
//                                                cellBranch1.setCellStyle(my_styleBranch);
//                                                
//                                                countCustomer = countCustomer+temp1.NUMBER_CUSTOMER;
//                                                Cell cellBranch2 = row1.createCell((short) 2);
//                                                cellBranch2.setCellValue(temp1.NUMBER_CUSTOMER);
//                                                cellBranch2.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch3 = row1.createCell((short) 3);
//                                                cellBranch3.setCellValue(temp1.CERTIFICATION_PROFILE_AMOUNT);
//                                                cellBranch3.setCellStyle(my_styleBranch);
//                                                
//                                                double intoMoneyItem = temp1.NUMBER_CUSTOMER * temp1.CERTIFICATION_PROFILE_AMOUNT;
//                                                intoMoney = intoMoney + intoMoneyItem;
//                                                Cell cellBranch4 = row1.createCell((short) 4);
//                                                cellBranch4.setCellValue(clsCom.convertMoneyDoubleZero(intoMoneyItem));
//                                                cellBranch4.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch5 = row1.createCell((short) 5);
//                                                cellBranch5.setCellValue(temp1.DISCOUNT_RATE + "%");
//                                                cellBranch5.setCellStyle(my_styleBranch);
//
//                                                double moneyRoseItem = (temp1.CERTIFICATION_PROFILE_AMOUNT / 100) * temp1.DISCOUNT_RATE;
//                                                moneyRose = moneyRose + moneyRoseItem;
//                                                Cell cellBranch6 = row1.createCell((short) 6);
//                                                cellBranch6.setCellValue(clsCom.convertMoneyDoubleZero(moneyRoseItem));
//                                                cellBranch6.setCellStyle(my_styleBranch);
//
//                                                double returnMoneyItem = temp1.CERTIFICATION_PROFILE_AMOUNT - moneyRoseItem;
//                                                Cell cellBranch7 = row1.createCell((short) 7);
//                                                cellBranch7.setCellValue(clsCom.convertMoneyDoubleZero(returnMoneyItem));
//                                                cellBranch7.setCellStyle(my_styleBranch);
//
//                                                double sumMoneyItem = temp1.NUMBER_CUSTOMER * returnMoneyItem;
//                                                sumMoney = sumMoney + sumMoneyItem;
//                                                Cell cellBranch8 = row1.createCell((short) 8);
//                                                cellBranch8.setCellValue(clsCom.convertMoneyDoubleZero(sumMoneyItem));
//                                                cellBranch8.setCellStyle(my_styleBranch);
//
//                                                i++;
//                                            }
//                                            if(isHasReportSumaryCN == true)
//                                            {
//                                                HSSFRow row1 = sheet.createRow(i);
//                                                HSSFCellStyle my_styleBranch = wb.createCellStyle();
//                                                HSSFFont fontBranch = wb.createFont();
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
//
//                                                fontBranch.setFontName("Verdana");
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//
//                                                Cell cellBranch = row1.createCell((short) 0);
//                                                cellBranch.setCellValue("TỔNG");
//                                                cellBranch.setCellStyle(my_style);
//                                                Cell cellBranch1 = row1.createCell((short) 1);
//                                                cellBranch1.setCellStyle(my_style);
//
//                                                Cell cellBranch2 = row1.createCell((short) 2);
//                                                cellBranch2.setCellValue(countCustomer);
//                                                cellBranch2.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch3 = row1.createCell((short) 3);
//                                                cellBranch3.setCellValue("");
//                                                cellBranch3.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch4 = row1.createCell((short) 4);
//                                                cellBranch4.setCellValue(clsCom.convertMoneyDoubleZero(intoMoney));
//                                                cellBranch4.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch5 = row1.createCell((short) 5);
//                                                cellBranch5.setCellValue("");
//                                                cellBranch5.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch6 = row1.createCell((short) 6);
//                                                cellBranch6.setCellValue(clsCom.convertMoneyDoubleZero(moneyRose));
//                                                cellBranch6.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch7 = row1.createCell((short) 7);
//                                                cellBranch7.setCellValue("");
//                                                cellBranch7.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch8 = row1.createCell((short) 8);
//                                                cellBranch8.setCellValue(clsCom.convertMoneyDoubleZero(sumMoney));
//                                                cellBranch8.setCellStyle(my_styleBranch);
//                                            }
//                                            //</editor-fold>
//                                            
//                                            i = i + 2;
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - TOKEN">
//                                            HSSFRow rowOneToken = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                            Cell celwOneTokenl = rowOneToken.createCell((short) 0);
//                                            celwOneTokenl.setCellValue("Nội dung");
//                                            celwOneTokenl.setCellStyle(my_style);
//                                            Cell celwOneTokenl01 = rowOneToken.createCell((short) 1);
//                                            celwOneTokenl01.setCellStyle(my_style);
//                                            Cell celwOneTokenl02 = rowOneToken.createCell((short) 2);
//                                            celwOneTokenl02.setCellStyle(my_style);
//
//                                            Cell celwOneTokenl1 = rowOneToken.createCell((short) 3);
//                                            celwOneTokenl1.setCellValue("Số lượng");
//                                            celwOneTokenl1.setCellStyle(my_style);
//
//                                            Cell cellwOneToken2 = rowOneToken.createCell((short) 4);
//                                            cellwOneToken2.setCellValue("Thành tiền");
//                                            cellwOneToken2.setCellStyle(my_style);
//                                            k = 0;
//                                            String sTOKEN_ENTERPRISE = "0";
//                                            GENERAL_POLICY[][] sessGeneralPolicy;
//                                            sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
//                                            if (sessGeneralPolicy[0].length > 0) {
//                                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
//                                                {
//                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_TOKEN_AMOUNT_ENTERPRISE))
//                                                    {
//                                                        sTOKEN_ENTERPRISE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                            int[] pREMAINING_BEGINING_MONTH = new int[1];
//                                            int[] pIMPORT_IN_MONTH = new int[1];
//                                            int[] pTOKEN_USED_IN_MONTH = new int[1];
//                                            int[] pREAMINING_END_MONTH = new int[1];
//                                            String strResult = com.S_BO_TOKEN_REPORT_SUMMARY(EscapeUtils.escapeHtmlSearch(idBranchOffice),
//                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate), SessUserID,
//                                                pREMAINING_BEGINING_MONTH, pIMPORT_IN_MONTH, pTOKEN_USED_IN_MONTH, pREAMINING_END_MONTH);
//                                            if("0".equals(strResult)) {
//                                                i=i+1;
//                                                HSSFRow row1 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                HSSFCellStyle my_styleBranch = wb.createCellStyle();
//                                                HSSFFont fontBranch = wb.createFont();
//
//                                                fontBranch.setFontName("Verdana");
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//
//                                                Cell cellBranch11 = row1.createCell((short) 0);
//                                                cellBranch11.setCellValue("Giá token đặt cọc");
//                                                cellBranch11.setCellStyle(my_styleBranch);
//                                                Cell cellBranch111 = row1.createCell((short) 1);
//                                                cellBranch111.setCellStyle(my_style);
//                                                Cell cellBranch112 = row1.createCell((short) 2);
//                                                cellBranch112.setCellStyle(my_style);
//
//                                                Cell cellBranch12 = row1.createCell((short) 3);
//                                                cellBranch12.setCellValue(clsCom.convertMoneyDoubleZero(Double.parseDouble(sTOKEN_ENTERPRISE.replace(",", ""))));
//                                                cellBranch12.setCellStyle(my_styleBranch);
//                                                
//                                                Cell cellBranch13 = row1.createCell((short) 4);
//                                                cellBranch13.setCellValue("");
//                                                cellBranch13.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                HSSFRow row2 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch21 = row2.createCell((short) 0);
//                                                cellBranch21.setCellValue("Token tồn đầu kỳ");
//                                                cellBranch21.setCellStyle(my_styleBranch);
//                                                Cell cellBranch211 = row2.createCell((short) 1);
//                                                cellBranch211.setCellStyle(my_style);
//                                                Cell cellBranch212 = row2.createCell((short) 2);
//                                                cellBranch212.setCellStyle(my_style);
//
//                                                Cell cellBranch22 = row2.createCell((short) 3);
//                                                cellBranch22.setCellValue(pREMAINING_BEGINING_MONTH[0]);
//                                                cellBranch22.setCellStyle(my_styleBranch);
//                                                
//                                                double s1 = pREMAINING_BEGINING_MONTH[0] * Integer.parseInt(sTOKEN_ENTERPRISE.replace(",", ""));
//                                                Cell cellBranch23 = row2.createCell((short) 4);
//                                                cellBranch23.setCellValue(clsCom.convertMoneyDoubleZero(s1));
//                                                cellBranch23.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                HSSFRow row3 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch31 = row3.createCell((short) 0);
//                                                cellBranch31.setCellValue("Token đặt cọc trong tháng");
//                                                cellBranch31.setCellStyle(my_styleBranch);
//                                                Cell cellBranch311 = row3.createCell((short) 1);
//                                                cellBranch311.setCellStyle(my_style);
//                                                Cell cellBranch312 = row3.createCell((short) 2);
//                                                cellBranch312.setCellStyle(my_style);
//
//                                                Cell cellBranch32 = row3.createCell((short) 3);
//                                                cellBranch32.setCellValue(pIMPORT_IN_MONTH[0]);
//                                                cellBranch32.setCellStyle(my_styleBranch);
//                                                double s2 = pIMPORT_IN_MONTH[0] * Integer.parseInt(sTOKEN_ENTERPRISE.replace(",", ""));
//                                                Cell cellBranch33 = row3.createCell((short) 4);
//                                                cellBranch33.setCellValue(clsCom.convertMoneyDoubleZero(s2));
//                                                cellBranch33.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                HSSFRow row4 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch41 = row4.createCell((short) 0);
//                                                cellBranch41.setCellValue("Token sử dụng trong tháng");
//                                                cellBranch41.setCellStyle(my_styleBranch);
//                                                Cell cellBranch411 = row4.createCell((short) 1);
//                                                cellBranch411.setCellStyle(my_style);
//                                                Cell cellBranch412 = row4.createCell((short) 2);
//                                                cellBranch412.setCellStyle(my_style);
//
//                                                Cell cellBranch42 = row4.createCell((short) 3);
//                                                cellBranch42.setCellValue(pTOKEN_USED_IN_MONTH[0]);
//                                                cellBranch42.setCellStyle(my_styleBranch);
//                                                double s3 = pTOKEN_USED_IN_MONTH[0] * Integer.parseInt(sTOKEN_ENTERPRISE.replace(",", ""));
//                                                Cell cellBranch43 = row4.createCell((short) 4);
//                                                cellBranch43.setCellValue(clsCom.convertMoneyDoubleZero(s3));
//                                                cellBranch43.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                HSSFRow row5 = sheet.createRow(i);
//                                                Cell cellBranch51 = row5.createCell((short) 0);
//                                                cellBranch51.setCellValue("Token tồn cuối kỳ");
//                                                cellBranch51.setCellStyle(my_styleBranch);
//                                                Cell cellBranch511 = row5.createCell((short) 1);
//                                                cellBranch511.setCellStyle(my_style);
//                                                Cell cellBranch512 = row5.createCell((short) 2);
//                                                cellBranch512.setCellStyle(my_style);
//
//                                                Cell cellBranch52 = row5.createCell((short) 3);
//                                                cellBranch52.setCellValue(pREAMINING_END_MONTH[0]);
//                                                cellBranch52.setCellStyle(my_styleBranch);
//                                                double s4 = pREAMINING_END_MONTH[0] * Integer.parseInt(sTOKEN_ENTERPRISE.replace(",", ""));
//                                                Cell cellBranch53 = row5.createCell((short) 4);
//                                                cellBranch53.setCellValue(clsCom.convertMoneyDoubleZero(s4));
//                                                cellBranch53.setCellStyle(my_styleBranch);
//                                            }
//                                            //</editor-fold>
//                                            
//                                            i = i + 2;
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - PROFILE">
////                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
////                                            sheet.setColumnWidth(1, 12 * 255);
////                                            sheet.setColumnWidth(2, 12 * 255);
////                                            sheet.setColumnWidth(3, 18 * 255);
//                                            HSSFRow rowOneProfile = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                            Cell celwOneProfilel = rowOneProfile.createCell((short) 0);
//                                            celwOneProfilel.setCellValue("Nội dung");
//                                            celwOneProfilel.setCellStyle(my_style);
//                                            Cell celwOneProfilel01 = rowOneProfile.createCell((short) 1);
//                                            celwOneProfilel01.setCellStyle(my_style);
//                                            Cell celwOneProfilel02 = rowOneProfile.createCell((short) 2);
//                                            celwOneProfilel02.setCellStyle(my_style);
//
//                                            Cell celwOneProfilel1 = rowOneProfile.createCell((short) 3);
//                                            celwOneProfilel1.setCellValue("Số lượng");
//                                            celwOneProfilel1.setCellStyle(my_style);
//                                            String sLACK_OF_BRIEF = "0";
//                                            sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
//                                            if (sessGeneralPolicy[0].length > 0) {
//                                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
//                                                {
//                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_FINE_FOR_LACK_OF_BRIEF))
//                                                    {
//                                                        sLACK_OF_BRIEF = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                            int[] pREMAINING_BEGINNING_MONTH = new int[1];
//                                            int[] pBRIEF_IN_MONTH = new int[1];
//                                            int[] pBRIEF_LACK_IN_MONTH_NO = new int[1];
//                                            int[] pBRIEF_COMPENSATE_IN_MONTH_NO = new int[1];
//                                            int[] pOVER_TIME_BRIEF_NO = new int[1];
//                                            int[] pREMAINING_END_MONTH = new int[1];
//                                            com.S_BO_CERTIFICATION_BRIEF_REPORT(EscapeUtils.escapeHtmlSearch(idBranchOffice),
//                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate), SessUserID,
//                                                pREMAINING_BEGINNING_MONTH, pBRIEF_IN_MONTH, pBRIEF_LACK_IN_MONTH_NO, pBRIEF_COMPENSATE_IN_MONTH_NO,
//                                                pOVER_TIME_BRIEF_NO, pREMAINING_END_MONTH);
//                                            if(!"".equals(sLACK_OF_BRIEF)) {
//                                                i=i+1;
//                                                HSSFRow row1 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                HSSFCellStyle my_styleBranch = wb.createCellStyle();
//                                                HSSFFont fontBranch = wb.createFont();
//
//                                                fontBranch.setFontName("Verdana");
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//
//                                                Cell cellBranch11 = row1.createCell((short) 0);
//                                                cellBranch11.setCellValue("Tiền tạm giữ trên 1 bộ hồ sơ");
//                                                cellBranch11.setCellStyle(my_styleBranch);
//                                                Cell cellBranch111 = row1.createCell((short) 1);
//                                                cellBranch111.setCellStyle(my_style);
//                                                Cell cellBranch112 = row1.createCell((short) 2);
//                                                cellBranch112.setCellStyle(my_style);
//
//                                                Cell cellBranch12 = row1.createCell((short) 3);
//                                                cellBranch12.setCellValue(clsCom.convertMoneyDoubleZero(Double.parseDouble(sLACK_OF_BRIEF.replace(",", ""))));
//                                                cellBranch12.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                HSSFRow row2 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch21 = row2.createCell((short) 0);
//                                                cellBranch21.setCellValue("Hồ sơ chưa trả đầu kỳ");
//                                                cellBranch21.setCellStyle(my_styleBranch);
//                                                Cell cellBranch211 = row2.createCell((short) 1);
//                                                cellBranch211.setCellStyle(my_style);
//                                                Cell cellBranch212 = row2.createCell((short) 2);
//                                                cellBranch212.setCellStyle(my_style);
//
//                                                Cell cellBranch22 = row2.createCell((short) 3);
//                                                cellBranch22.setCellValue(pREMAINING_BEGINNING_MONTH[0]);
//                                                cellBranch22.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                HSSFRow row3 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch31 = row3.createCell((short) 0);
//                                                cellBranch31.setCellValue("Hồ sơ phát sinh trong kỳ");
//                                                cellBranch31.setCellStyle(my_styleBranch);
//                                                Cell cellBranch311 = row3.createCell((short) 1);
//                                                cellBranch311.setCellStyle(my_style);
//                                                Cell cellBranch312 = row3.createCell((short) 2);
//                                                cellBranch312.setCellStyle(my_style);
//
//                                                Cell cellBranch32 = row3.createCell((short) 3);
//                                                cellBranch32.setCellValue(pBRIEF_IN_MONTH[0]);
//                                                cellBranch32.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                HSSFRow row4 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch41 = row4.createCell((short) 0);
//                                                cellBranch41.setCellValue("Hồ sơ trả trong kỳ");
//                                                cellBranch41.setCellStyle(my_styleBranch);
//                                                Cell cellBranch411 = row4.createCell((short) 1);
//                                                cellBranch411.setCellStyle(my_style);
//                                                Cell cellBranch412 = row4.createCell((short) 2);
//                                                cellBranch412.setCellStyle(my_style);
//
//                                                Cell cellBranch42 = row4.createCell((short) 3);
//                                                cellBranch42.setCellValue(pBRIEF_LACK_IN_MONTH_NO[0]);
//                                                cellBranch42.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                HSSFRow row5 = sheet.createRow(i);
//                                                Cell cellBranch51 = row5.createCell((short) 0);
//                                                cellBranch51.setCellValue("Hồ sơ đã trả bù cho kỳ trước");
//                                                cellBranch51.setCellStyle(my_styleBranch);
//                                                Cell cellBranch511 = row5.createCell((short) 1);
//                                                cellBranch511.setCellStyle(my_style);
//                                                Cell cellBranch512 = row5.createCell((short) 2);
//                                                cellBranch512.setCellStyle(my_style);
//
//                                                Cell cellBranch52 = row5.createCell((short) 3);
//                                                cellBranch52.setCellValue(pBRIEF_COMPENSATE_IN_MONTH_NO[0]);
//                                                cellBranch52.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                HSSFRow row6 = sheet.createRow(i);
//                                                Cell cellBranch61 = row6.createCell((short) 0);
//                                                cellBranch61.setCellValue("Hồ sơ quá hạn");
//                                                cellBranch61.setCellStyle(my_styleBranch);
//                                                Cell cellBranch611 = row6.createCell((short) 1);
//                                                cellBranch611.setCellStyle(my_style);
//                                                Cell cellBranch612 = row6.createCell((short) 2);
//                                                cellBranch612.setCellStyle(my_style);
//
//                                                Cell cellBranch62 = row6.createCell((short) 3);
//                                                cellBranch62.setCellValue(pOVER_TIME_BRIEF_NO[0]);
//                                                cellBranch62.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                HSSFRow row7 = sheet.createRow(i);
//                                                Cell cellBranch71 = row7.createCell((short) 0);
//                                                cellBranch71.setCellValue("Hồ sơ chưa trả cuối kỳ");
//                                                cellBranch71.setCellStyle(my_styleBranch);
//                                                Cell cellBranch711 = row7.createCell((short) 1);
//                                                cellBranch711.setCellStyle(my_style);
//                                                Cell cellBranch712 = row7.createCell((short) 2);
//                                                cellBranch712.setCellStyle(my_style);
//
//                                                Cell cellBranch72 = row7.createCell((short) 3);
//                                                cellBranch72.setCellValue(pREMAINING_END_MONTH[0]);
//                                                cellBranch72.setCellStyle(my_styleBranch);
//                                                
//                                            }
//                                            //</editor-fold>
//                                            
//                                            i = i+3;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 1, 3));
//                                            HSSFCellStyle my_styleSyntheticTitleCA = wb.createCellStyle();
//                                            HSSFFont fontBranchSyntheticTitleCA = wb.createFont();
//                                            fontBranchSyntheticTitleCA.setBoldweight((short) 700);
//                                            fontBranchSyntheticTitleCA.setFontName("Arial");
//                                            my_styleSyntheticTitleCA.setFont(fontBranchSyntheticTitleCA);
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 1);
//                                            cellSyntheticTitle.setCellValue("ĐẠI DIỆN NHÀ CUNG CẤP");
//                                            my_styleSyntheticTitleCA.setAlignment(my_styleSyntheticTitleDN.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleCA);
//                                            
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 5, 7));
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 5);
//                                            cellSyntheticTitle.setCellValue("ĐẠI DIỆN ĐẠI LÝ");
//                                            my_styleSyntheticTitleCA.setAlignment(my_styleSyntheticTitleDN.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleCA);
//                                            
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET Customer Details">
//                                            rsPgin = new CERTIFICATION[1][];
//                                            com.S_BO_REPORT_CROSS_CHECK_DETAIL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
//                                                EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice), SessUserID,
//                                                loginLanguage, rsPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO, Integer.parseInt(countList));
//                                            if(rsPgin[0].length > 0)
//                                            {
//                                                HSSFSheet sheetCusDetail = wb.createSheet("CHI TIET KHACH HANG");
//                                                sheetCusDetail.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
//                                                sheetCusDetail.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
//                                                sheetCusDetail.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
//                                                sheetCusDetail.setColumnWidth(3, 18*255);
//                                                sheetCusDetail.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
//                                                sheetCusDetail.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
//                                                sheetCusDetail.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
//                                                sheetCusDetail.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
//                                                sheetCusDetail.setColumnWidth(8, 18*255);
//                                                sheetCusDetail.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
//                                                sheetCusDetail.setColumnWidth(10, 18*255);
//                                                sheetCusDetail.setColumnWidth(11, 18*255);
//                                                sheetCusDetail.setColumnWidth(12, 18*255);
//                                                sheetCusDetail.setColumnWidth(13, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
//                                                sheetCusDetail.setColumnWidth(14, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
//                                                sheetCusDetail.setColumnWidth(15, 18*255);
//                                                sheetCusDetail.setColumnWidth(16, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_GEN);
//                                                sheetCusDetail.setColumnWidth(17, 26 * 255);
//                                                sheetCusDetail.setColumnWidth(18, 30 * 255);
//                                                sheetCusDetail.setColumnWidth(19, 40 * 255);
//                                                HSSFRow rowCusDetail = sheetCusDetail.createRow(0);
//                                                Cell celCus = rowCusDetail.createCell((short) 0);
//                                                celCus.setCellValue(sCellSTT);
//                                                celCus.setCellStyle(my_style);
//                                                Cell celCus1 = rowCusDetail.createCell((short) 1);
//                                                celCus1.setCellValue(sCellAgency);
//                                                celCus1.setCellStyle(my_style);
//                                                Cell celCus2 = rowCusDetail.createCell((short) 2);
//                                                celCus2.setCellValue(sCellUser);
//                                                celCus2.setCellStyle(my_style);
//                                                Cell celCus3 = rowCusDetail.createCell((short) 3);
//                                                celCus3.setCellValue(sCellCertState);
//                                                celCus3.setCellStyle(my_style);
//                                                Cell celCus4 = rowCusDetail.createCell((short) 4);
//                                                celCus4.setCellValue(sCellMST);
//                                                celCus4.setCellStyle(my_style);
//                                                Cell celCus5 = rowCusDetail.createCell((short) 5);
//                                                celCus5.setCellValue(sCellCompany);
//                                                celCus5.setCellStyle(my_style);
//                                                Cell celCus6 = rowCusDetail.createCell((short) 6);
//                                                celCus6.setCellValue(sCellCMND);
//                                                celCus6.setCellStyle(my_style);
//                                                Cell celCus7 = rowCusDetail.createCell((short) 7);
//                                                celCus7.setCellValue(sCellPersonal);
//                                                celCus7.setCellStyle(my_style);
//                                                Cell celCus8 = rowCusDetail.createCell((short) 8);
//                                                celCus8.setCellValue(sCellProvince);
//                                                celCus8.setCellStyle(my_style);
//                                                Cell celCus9 = rowCusDetail.createCell((short) 9);
//                                                celCus9.setCellValue(sCellProfile);
//                                                celCus9.setCellStyle(my_style);
//                                                Cell celCus10 = rowCusDetail.createCell((short) 10);
//                                                celCus10.setCellValue(sCellProfileFee);
//                                                celCus10.setCellStyle(my_style);
//                                                Cell celCus11 = rowCusDetail.createCell((short) 11);
//                                                celCus11.setCellValue(sCellTokenQuality);
//                                                celCus11.setCellStyle(my_style);
//                                                Cell celCus12 = rowCusDetail.createCell((short) 12);
//                                                celCus12.setCellValue(sCellTokenFee);
//                                                celCus12.setCellStyle(my_style);
//                                                Cell celCus13 = rowCusDetail.createCell((short) 13);
//                                                celCus13.setCellValue(sCellRequestType);
//                                                celCus13.setCellStyle(my_style);
//                                                Cell celCus14 = rowCusDetail.createCell((short) 14);
//                                                celCus14.setCellValue(sCellCreateDate);
//                                                celCus14.setCellStyle(my_style);
//                                                Cell celCus152 = rowCusDetail.createCell((short) 15);
//                                                celCus152.setCellValue("Ngày duyệt CA");
//                                                celCus152.setCellStyle(my_style);
//                                                Cell celCus15 = rowCusDetail.createCell((short) 16);
//                                                celCus15.setCellValue(sCellGenDate);
//                                                celCus15.setCellStyle(my_style);
//                                                Cell celCus16 = rowCusDetail.createCell((short) 17);
//                                                celCus16.setCellValue(sCellCollationDate);
//                                                celCus16.setCellStyle(my_style);
//                                                Cell celCus17 = rowCusDetail.createCell((short) 18);
//                                                celCus17.setCellValue(sCellCollationState);
//                                                celCus17.setCellStyle(my_style);
//                                                Cell celCus18 = rowCusDetail.createCell((short) 19);
//                                                celCus18.setCellValue(sCellProfileFine);
//                                                celCus18.setCellStyle(my_style);
//                                                Cell celCus19 = rowCusDetail.createCell((short) 20);
//                                                celCus19.setCellValue("Số sê-ri CTS");
//                                                celCus19.setCellStyle(my_style);
//                                                i = 1;
//                                                k = 0;
//                                                for (CERTIFICATION temp1 : rsPgin[0]) {
//                                                    if (k == 0) {
//                                                        k = 1;
//                                                    } else {
//                                                        k++;
//                                                    }
//                                                    HSSFRow row1 = sheetCusDetail.createRow(Integer.valueOf(i));
//                                                    HSSFCellStyle my_styleBranch = wb.createCellStyle();
//                                                    HSSFFont fontBranch = wb.createFont();
//
//                                                    fontBranch.setFontName("Arial");
//        //                                            fontBranch.setColor((short) 12);
//                                                    my_styleBranch.setFont(fontBranch);
//        //                                            my_styleBranch.setFillBackgroundColor((short) 9);
//
//                                                    Cell cellBranch = row1.createCell((short) 0);
//                                                    cellBranch.setCellValue(k);
//                                                    cellBranch.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch1 = row1.createCell((short)1);
//                                                    cellBranch1.setCellValue(temp1.BRANCH_DESC);
//                                                    cellBranch1.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch2 = row1.createCell((short) 2);
//                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
//                                                    cellBranch2.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch3 = row1.createCell((short) 3);
//                                                    cellBranch3.setCellValue(temp1.CERTIFICATION_STATE_DESC);
//                                                    cellBranch3.setCellStyle(my_styleBranch);
//
//                                                    String sTAX_CODE = temp1.TAX_CODE;
////                                                    if(!"".equals(sTAX_CODE)){sTAX_CODE = "'"+sTAX_CODE;}
//                                                    Cell cellBranch4 = row1.createCell((short) 4);
//                                                    cellBranch4.setCellValue(sTAX_CODE);
//                                                    cellBranch4.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch5 = row1.createCell((short) 5);
//                                                    cellBranch5.setCellValue(temp1.COMPANY_NAME);
//                                                    cellBranch5.setCellStyle(my_styleBranch);
//
//                                                    String sP_ID = temp1.P_ID;
////                                                    if(!"".equals(sP_ID)){sP_ID = "'"+sP_ID;}
//                                                    Cell cellBranch6 = row1.createCell((short) 6);
//                                                    cellBranch6.setCellValue(sP_ID);
//                                                    cellBranch6.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch7 = row1.createCell((short) 7);
//                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
//                                                    cellBranch7.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch8 = row1.createCell((short) 8);
//                                                    cellBranch8.setCellValue(temp1.CITY_PROVINCE);
//                                                    cellBranch8.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch9 = row1.createCell((short) 9);
//                                                    cellBranch9.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
//                                                    cellBranch9.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch10 = row1.createCell((short) 10);
//                                                    cellBranch10.setCellValue(temp1.FEE_AMOUNT);
//                                                    cellBranch10.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch11 = row1.createCell((short) 11);
//                                                    cellBranch11.setCellValue(temp1.TOKEN_NUMBER);
//                                                    cellBranch11.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch12 = row1.createCell((short) 12);
//                                                    cellBranch12.setCellValue(temp1.TOKEN_AMOUNT);
//                                                    cellBranch12.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch13 = row1.createCell((short) 13);
//                                                    cellBranch13.setCellValue(temp1.CERTIFICATION_ATTR_TYPE_DESC);
//                                                    cellBranch13.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch14 = row1.createCell((short) 14);
//                                                    cellBranch14.setCellValue(temp1.CREATED_DT);
//                                                    cellBranch14.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch152 = row1.createCell((short) 15);
//                                                    cellBranch152.setCellValue(temp1.APPROVAL_CA_DT);
//                                                    cellBranch152.setCellStyle(my_styleBranch);
//                                                    
//                                                    Cell cellBranch15 = row1.createCell((short) 16);
//                                                    cellBranch15.setCellValue(temp1.ISSUED_DT);
//                                                    cellBranch15.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch16 = row1.createCell((short) 17);
//                                                    cellBranch16.setCellValue(temp1.CROSS_CHECKED_DT);
//                                                    cellBranch16.setCellStyle(my_styleBranch);
//
//                                                    String sCollated = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CROSS_CHECK_NOTENOUGH).trim();
//                                                    if(temp1.CROSS_CHECK_ENABLED == true) {
//                                                        sCollated = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CROSS_CHECK_ENOUGH).trim();
//                                                    }
//                                                    Cell cellBranch17 = row1.createCell((short) 18);
//                                                    cellBranch17.setCellValue(sCollated);
//                                                    cellBranch17.setCellStyle(my_styleBranch);
//                                                    
//                                                    Cell cellBranch18 = row1.createCell((short) 19);
//                                                    cellBranch18.setCellValue(temp1.FINE_FOR_LACK_OF_BRIEF);
//                                                    cellBranch18.setCellStyle(my_styleBranch);
//                                                    
//                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
////                                                    if(!"".equals(sCERTIFICATION_SN)){sCERTIFICATION_SN = "'"+sCERTIFICATION_SN;}
//                                                    Cell cellBranch19 = row1.createCell((short) 20);
//                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
//                                                    cellBranch19.setCellStyle(my_styleBranch);
//                                                    i++;
//                                                }
//                                            }
//                                            //</editor-fold>
//                                            
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET Decline Customers">
//                                            rsPgin = new CERTIFICATION[1][];
//                                            com.S_BO_REPORT_CERTIFICATE_DECLINE(EscapeUtils.escapeHtmlSearch(ToCreateDate),
//                                                EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice), SessUserID,
//                                                loginLanguage, rsPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO, Integer.parseInt(countList));
//                                            if(rsPgin[0].length > 0)
//                                            {
//                                                HSSFSheet sheetCusDetail = wb.createSheet("KHACH HANG HUY");
//                                                sheetCusDetail.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
//                                                sheetCusDetail.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
//                                                sheetCusDetail.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
////                                                sheetCusDetail.setColumnWidth(3, 18*255);
//                                                sheetCusDetail.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
//                                                sheetCusDetail.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
//                                                sheetCusDetail.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
//                                                sheetCusDetail.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
//                                                sheetCusDetail.setColumnWidth(7, 18*255);
//                                                sheetCusDetail.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
////                                                sheetCusDetail.setColumnWidth(10, 18*255);
////                                                sheetCusDetail.setColumnWidth(11, 18*255);
////                                                sheetCusDetail.setColumnWidth(12, 18*255);
//                                                sheetCusDetail.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
//                                                sheetCusDetail.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
//                                                sheetCusDetail.setColumnWidth(11, 18*255);
//                                                sheetCusDetail.setColumnWidth(12, 18*255);
//                                                sheetCusDetail.setColumnWidth(13, 30*255);
//                                                sheetCusDetail.setColumnWidth(14, 30*255);
//                                                HSSFRow rowCusDetail = sheetCusDetail.createRow(0);
//                                                Cell celCus = rowCusDetail.createCell((short) 0);
//                                                celCus.setCellValue(sCellSTT);
//                                                celCus.setCellStyle(my_style);
//                                                Cell celCus1 = rowCusDetail.createCell((short) 1);
//                                                celCus1.setCellValue(sCellAgency);
//                                                celCus1.setCellStyle(my_style);
//                                                Cell celCus2 = rowCusDetail.createCell((short) 2);
//                                                celCus2.setCellValue(sCellUser);
//                                                celCus2.setCellStyle(my_style);
//                                                Cell celCus4 = rowCusDetail.createCell((short) 3);
//                                                celCus4.setCellValue(sCellMST);
//                                                celCus4.setCellStyle(my_style);
//                                                Cell celCus5 = rowCusDetail.createCell((short) 4);
//                                                celCus5.setCellValue(sCellCompany);
//                                                celCus5.setCellStyle(my_style);
//                                                Cell celCus6 = rowCusDetail.createCell((short) 5);
//                                                celCus6.setCellValue(sCellCMND);
//                                                celCus6.setCellStyle(my_style);
//                                                Cell celCus7 = rowCusDetail.createCell((short) 6);
//                                                celCus7.setCellValue(sCellPersonal);
//                                                celCus7.setCellStyle(my_style);
//                                                Cell celCus8 = rowCusDetail.createCell((short) 7);
//                                                celCus8.setCellValue(sCellProvince);
//                                                celCus8.setCellStyle(my_style);
//                                                Cell celCus9 = rowCusDetail.createCell((short) 8);
//                                                celCus9.setCellValue(sCellProfile);
//                                                celCus9.setCellStyle(my_style);
//                                                Cell celCus13 = rowCusDetail.createCell((short) 9);
//                                                celCus13.setCellValue(sCellRequestType);
//                                                celCus13.setCellStyle(my_style);
//                                                Cell celCus14 = rowCusDetail.createCell((short) 10);
//                                                celCus14.setCellValue(sCellCreateDate);
//                                                celCus14.setCellStyle(my_style);
//                                                Cell celCus16 = rowCusDetail.createCell((short) 11);
//                                                celCus16.setCellValue(sCellDeclineDate);
//                                                celCus16.setCellStyle(my_style);
//                                                Cell celCus17 = rowCusDetail.createCell((short) 12);
//                                                celCus17.setCellValue(sCellDeclineNumber);
//                                                celCus17.setCellStyle(my_style);
//                                                Cell celCus19 = rowCusDetail.createCell((short) 13);
//                                                celCus19.setCellValue("Ngày duyệt CA");
//                                                celCus19.setCellStyle(my_style);
//                                                Cell celCus18 = rowCusDetail.createCell((short) 14);
//                                                celCus18.setCellValue("Số sê-ri CTS");
//                                                celCus18.setCellStyle(my_style);
//                                                i = 1;
//                                                k = 0;
//                                                for (CERTIFICATION temp1 : rsPgin[0]) {
//                                                    if (k == 0) {
//                                                        k = 1;
//                                                    } else {
//                                                        k++;
//                                                    }
//                                                    HSSFRow row1 = sheetCusDetail.createRow(Integer.valueOf(i));
//                                                    HSSFCellStyle my_styleBranch = wb.createCellStyle();
//                                                    HSSFFont fontBranch = wb.createFont();
//
//                                                    fontBranch.setFontName("Arial");
//        //                                            fontBranch.setColor((short) 12);
//                                                    my_styleBranch.setFont(fontBranch);
//        //                                            my_styleBranch.setFillBackgroundColor((short) 9);
//
//                                                    Cell cellBranch = row1.createCell((short) 0);
//                                                    cellBranch.setCellValue(k);
//                                                    cellBranch.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch1 = row1.createCell((short)1);
//                                                    cellBranch1.setCellValue(temp1.BRANCH_DESC);
//                                                    cellBranch1.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch2 = row1.createCell((short) 2);
//                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
//                                                    cellBranch2.setCellStyle(my_styleBranch);
//                                                    
//                                                    String sTAX_CODE = temp1.TAX_CODE;
////                                                    if(!"".equals(sTAX_CODE)){sTAX_CODE = "'"+sTAX_CODE;}
//                                                    Cell cellBranch4 = row1.createCell((short) 3);
//                                                    cellBranch4.setCellValue(sTAX_CODE);
//                                                    cellBranch4.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch5 = row1.createCell((short) 4);
//                                                    cellBranch5.setCellValue(temp1.COMPANY_NAME);
//                                                    cellBranch5.setCellStyle(my_styleBranch);
//
//                                                    String sP_ID = temp1.P_ID;
////                                                    if(!"".equals(sP_ID)){sP_ID = "'"+sP_ID;}
//                                                    Cell cellBranch6 = row1.createCell((short) 5);
//                                                    cellBranch6.setCellValue(sP_ID);
//                                                    cellBranch6.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch7 = row1.createCell((short) 6);
//                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
//                                                    cellBranch7.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch8 = row1.createCell((short) 7);
//                                                    cellBranch8.setCellValue(temp1.CITY_PROVINCE);
//                                                    cellBranch8.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch9 = row1.createCell((short) 8);
//                                                    cellBranch9.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
//                                                    cellBranch9.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch10 = row1.createCell((short) 9);
//                                                    cellBranch10.setCellValue(temp1.CERTIFICATION_ATTR_TYPE_DESC);
//                                                    cellBranch10.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch11 = row1.createCell((short) 10);
//                                                    cellBranch11.setCellValue(temp1.CREATED_DT);
//                                                    cellBranch11.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch13 = row1.createCell((short) 11);
//                                                    cellBranch13.setCellValue(temp1.DECLINED_DT);
//                                                    cellBranch13.setCellStyle(my_styleBranch);
//
//                                                    Cell cellBranch14 = row1.createCell((short) 12);
//                                                    cellBranch14.setCellValue(temp1.NUMBER_DECLINED_DAYS);
//                                                    cellBranch14.setCellStyle(my_styleBranch);
//                                                    
//                                                    Cell cellBranch16 = row1.createCell((short) 13);
//                                                    cellBranch16.setCellValue(temp1.APPROVAL_CA_DT);
//                                                    cellBranch16.setCellStyle(my_styleBranch);
//                                                    
//                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
////                                                    if(!"".equals(sCERTIFICATION_SN)){sCERTIFICATION_SN = "'"+sCERTIFICATION_SN;}
//                                                    Cell cellBranch15 = row1.createCell((short) 14);
//                                                    cellBranch15.setCellValue(sCERTIFICATION_SN);
//                                                    cellBranch15.setCellStyle(my_styleBranch);
//                                                    i++;
//                                                }
//                                            }
//                                            //</editor-fold>
//                                            
//                                            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
//                                            wb.write(outByteStream);
//                                            byte[] outArray = outByteStream.toByteArray();
//                                            File someFile = new File(strURLPath);
//                                            FileOutputStream fos = new FileOutputStream(someFile);
//                                            fos.write(outArray);
//                                            fos.flush();
//                                            strView = "0#" + strURLPath + "#" + sFileName;
//                                        } else {
//                                            strView = "2#1";
//                                        }
//                                    } else {
//                                        strView = "2#1";
//                                    }
//                                } else {
//                                    strView = Definitions.CONFIG_EXCEPTION_STRING_CSRF + "#0";
//                                }
//                                //</editor-fold>
//                                break;
//                            }
                            case "exportcollationcertlist": {
                                //<editor-fold defaultstate="collapsed" desc="exportcollationcertlist">
                                String anticsrf = request.getParameter("CsrfToken");
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                if ((anticsrf != null) && (anticsrf.equals(request.getSession().getAttribute("anticsrf")))) {
                                    CommonFunction clsCom = new CommonFunction();
                                    String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
                                    Config conf = new Config();
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
                                    CommonFunction.LogDebugString(log, "Export Collation Data", "Mounth: " + FromCreateDate + "; Year: " + ToCreateDate + "; STATUS_COLLATION: " + STATUS_COLLATION);
                                    if (!"".equals(countList)) {
                                        CERTIFICATION[][] rsPgin;
                                        rsPgin = new CERTIFICATION[1][];
                                        com.S_BO_CERTIFICATION_CROSS_CHECK_LIST(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                            EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            EscapeUtils.escapeHtmlSearch(idBranchOffice),STATUS_COLLATION, SessUserID,
                                            loginLanguage, rsPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO, Integer.parseInt(countList), SessUserAgentID, "","");
                                        if(rsPgin[0].length > 0)
                                        {
                                            String queryString = getServletContext().getRealPath("/");
                                            String outputDirectory = queryString;
                                            String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                            FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                            XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                            inputStream.close();
                                            String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                            String sCellApproveDate = "Ngày duyệt"; //conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
                                            String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
                                            String sCellUser= conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_USER_CREATE).trim();
                                            String sCellState = "Trạng thái";// conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_STATE).trim();
                                            String sCellMST = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_MST).trim();
                                            String sCellCMND = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CMND).trim();
                                            String sCellCompany = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COMPANY).trim();
                                            String sCellPersonal = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PESONAL).trim();
                                            String sCellProfile = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PROFILE).trim();
                                            String sCellRequestType = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_REQUEST_TYPE).trim();
                                            String sCellCreateDate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_CREATE).trim();
                                            String sCellGenDate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_GEN).trim();
                                            String sCellCertState = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_STATE).trim();
                                            String sTokenSN = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_TOKEN_CODE).trim();
                                            String sExpirationDT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_EXPIRATION).trim();
                                            String sCellMounth = "Tháng đối soát";
                                            String sCellChangeDate = "Ngày đổi trạng thái đối soát";
                                            String sCellCollationState = "Trạng thái hồ sơ";
                                            String sCellProfileFee = "Phí dịch vụ";
                                            String sCellDateEffective = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_EFFECTIVE).trim();
                                            String sCellTokenQuality = "Số lượng Token";
                                            String sCellTokenFee = "Phí Token";
                                            String sCellProvince = "Tỉnh thành";
                                            String sCellProfileFine = "Tiền phạt hồ sơ";
                                            String sCellCollationDate = "Ngày đối soát"; 
                                            String sCellDeclineDate = "Ngày hủy"; 
                                            String sCellDeclineNumber = "Số ngày hủy";
                                            String sCellRevokeDate = "Ngày thu hồi";
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
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### TONG HOP NEW">
                                            SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("CHI TIET KHACH HANG");
                                            
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
                                            cellSyntheticTitle.setCellValue(conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_REPORT_CONTROL_COMPANY_NAME));
                                            my_styleSyntheticTitleDN.setAlignment(my_styleSyntheticTitleDN.ALIGN_LEFT);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleDN);
                                            
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 6, 10));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 6);
                                            cellSyntheticTitle.setCellValue("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM");
                                            my_styleSyntheticTitleDN.setAlignment(my_styleSyntheticTitleDN.ALIGN_CENTER);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleDN);
                                            
                                            i = i+1;
                                            rowSyntheticTitle = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 4));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                            cellSyntheticTitle.setCellValue(conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_REPORT_CONTROL_CA_NAME));
                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_LEFT);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                            
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 6, 10));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 6);
                                            cellSyntheticTitle.setCellValue("Độc lập - Tự do - Hạnh phúc");
                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                            i = i+1;
                                            rowSyntheticTitle = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 9, 12));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 9);
                                            cellSyntheticTitle.setCellValue("Ngày .... tháng .... năm ....");
                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                            i = i+1;
                                            rowSyntheticTitle = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 12));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                            cellSyntheticTitle.setCellValue(conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_REPORT_CONTROL_WITH_AGENT) + " " + sBranchReport);
                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                            i = i+1;
                                            rowSyntheticTitle = sheet.createRow(i);
                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 12));
                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                            cellSyntheticTitle.setCellValue("Tháng " + ToCreateDate.trim() + "/" + FromCreateDate.trim());
                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                            //</editor-fold>
                                            
                                            i = i + 2;
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET LIST">
                                            int sumNumToken = 0;
                                            int sumRoseAmount = 0;
                                            int sumTokenAmout = 0;
                                            int sumReturnAmount = 0;
                                            Font fontBranch = wb.createFont();
                                            fontBranch.setFontName("Verdana");
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
                                                Integer.parseInt(countList), SessUserAgentID, "","");
                                            if(rsReportPgin[0].length > 0)
                                            {
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
                                                Row rowCusDetail = sheet.createRow(i);
                                                Cell celCus = rowCusDetail.createCell((short) 0);
                                                celCus.setCellValue(sCellSTT);
                                                celCus.setCellStyle(my_style);
                                                Cell celCus1 = rowCusDetail.createCell((short) 1);
                                                celCus1.setCellValue(sCellMounth);
                                                celCus1.setCellStyle(my_style);
                                                Cell celCus2 = rowCusDetail.createCell((short) 2);
                                                celCus2.setCellValue(sCellAgency);
                                                celCus2.setCellStyle(my_style);
                                                Cell celCus4 = rowCusDetail.createCell((short) 3);
                                                celCus4.setCellValue(sCellMST);
                                                celCus4.setCellStyle(my_style);
                                                Cell celCus5 = rowCusDetail.createCell((short) 4);
                                                celCus5.setCellValue(sCellCompany);
                                                celCus5.setCellStyle(my_style);
                                                Cell celCus9 = rowCusDetail.createCell((short) 5);
                                                celCus9.setCellValue(sCellProfile);
                                                celCus9.setCellStyle(my_style);
                                                Cell celFeeAmount = rowCusDetail.createCell((short) 6);
                                                celFeeAmount.setCellValue(sCellProfileFee);
                                                celFeeAmount.setCellStyle(my_style);
                                                Cell celTokenNum = rowCusDetail.createCell((short) 7);
                                                celTokenNum.setCellValue(sCellTokenQuality);
                                                celTokenNum.setCellStyle(my_style);
                                                Cell celTokenAmount = rowCusDetail.createCell((short) 8);
                                                celTokenAmount.setCellValue("Phí token");
                                                celTokenAmount.setCellStyle(my_style);
                                                Cell celRoseAmount = rowCusDetail.createCell((short) 9);
                                                celRoseAmount.setCellValue("Thành tiền hoa hồng");
                                                celRoseAmount.setCellStyle(my_style);
                                                Cell celSumAmount = rowCusDetail.createCell((short) 10);
                                                celSumAmount.setCellValue("Tiền DV phải trả NCC");
                                                celSumAmount.setCellStyle(my_style);
//                                                Cell celDateGen = rowCusDetail.createCell((short) 11);
//                                                celDateGen.setCellValue(sCellGenDate);
//                                                celDateGen.setCellStyle(my_style);
                                                Cell celDateEffective = rowCusDetail.createCell((short) 11);
                                                celDateEffective.setCellValue(sCellDateEffective);
                                                celDateEffective.setCellStyle(my_style);
                                                Cell celDateRevoke = rowCusDetail.createCell((short) 12);
                                                celDateRevoke.setCellValue(sExpirationDT);
                                                celDateRevoke.setCellStyle(my_style);
                                                Cell celCertSN = rowCusDetail.createCell((short) 13);
                                                celCertSN.setCellValue("Số sê-ri CTS");
                                                celCertSN.setCellStyle(my_style);
                                                Cell celCreateUser = rowCusDetail.createCell((short) 14);
                                                celCreateUser.setCellValue(sCellUser);
                                                celCreateUser.setCellStyle(my_style);
                                                Cell celRequestType = rowCusDetail.createCell((short) 15);
                                                celRequestType.setCellValue(sCellRequestType);
                                                celRequestType.setCellStyle(my_style);
                                                Cell celCus6 = rowCusDetail.createCell((short) 16);
                                                celCus6.setCellValue(sCellCMND);
                                                celCus6.setCellStyle(my_style);
                                                Cell celCus7 = rowCusDetail.createCell((short) 17);
                                                celCus7.setCellValue(sCellPersonal);
                                                celCus7.setCellStyle(my_style);
                                                Cell celTokenSN = rowCusDetail.createCell((short) 18);
                                                celTokenSN.setCellValue(sTokenSN);
                                                celTokenSN.setCellStyle(my_style);
                                                
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
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
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

                                                    Cell cellBranch1 = row1.createCell((short) 2);
                                                    cellBranch1.setCellValue(temp1.BRANCH_NAME);
                                                    cellBranch1.setCellStyle(my_styleBranch);

                                                    String sTAX_CODE = temp1.TAX_CODE;
                                                    Cell cellBranch4 = row1.createCell((short) 3);
                                                    cellBranch4.setCellValue(sTAX_CODE);
                                                    cellBranch4.setCellStyle(my_styleBranch);

                                                    Cell cellBranch5 = row1.createCell((short) 4);
                                                    cellBranch5.setCellValue(temp1.COMPANY_NAME);
                                                    cellBranch5.setCellStyle(my_styleBranch);

                                                    Cell cellBranch9 = row1.createCell((short) 5);
                                                    cellBranch9.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
                                                    cellBranch9.setCellStyle(my_styleBranch);

                                                    Cell cellBranch10 = row1.createCell((short) 6);
                                                    cellBranch10.setCellValue(temp1.FEE_AMOUNT);
                                                    cellBranch10.setCellStyle(my_styleMoney);

                                                    Cell cellBranch11 = row1.createCell((short) 7);
                                                    cellBranch11.setCellValue(temp1.TOKEN_NUMBER);
                                                    cellBranch11.setCellStyle(my_styleMoney);

                                                    Cell cellBranch12 = row1.createCell((short) 8);
                                                    cellBranch12.setCellValue(temp1.TOKEN_AMOUNT);
                                                    cellBranch12.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellRoseAmount = row1.createCell((short) 9);
                                                    cellRoseAmount.setCellValue(temp1.ROSE_AMOUNT);
                                                    cellRoseAmount.setCellStyle(my_styleMoney);
                                                    
                                                    Cell cellSumAmount = row1.createCell((short) 10);
                                                    cellSumAmount.setCellValue(temp1.RETURN_AMOUNT);
                                                    cellSumAmount.setCellStyle(my_styleMoney);
                                                    
//                                                    Cell cellBranch15 = row1.createCell((short) 11);
//                                                    Date dateIssue = CommonFunction.convertStringToDate(temp1.ISSUED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
//                                                    if(dateIssue != null) {
//                                                        cellBranch15.setCellValue(dateIssue);
//                                                    } else {
//                                                        cellBranch15.setCellValue("");
//                                                    }
//                                                    cellBranch15.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell cellValueEffective = row1.createCell((short) 11);
                                                    Date dateEffective = CommonFunction.convertStringToDate(temp1.EFFECTIVE_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateEffective != null) {
                                                        cellValueEffective.setCellValue(dateEffective);
                                                    } else {
                                                        cellValueEffective.setCellValue("");
                                                    }
                                                    cellValueEffective.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell cellValueRevoke = row1.createCell((short) 12);
                                                    Date dateRevoke = CommonFunction.convertStringToDate(temp1.EXPIRATION_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateRevoke != null) {
                                                        cellValueRevoke.setCellValue(dateRevoke);
                                                    } else {
                                                        cellValueRevoke.setCellValue("");
                                                    }
                                                    cellValueRevoke.setCellStyle(my_styleBranchDate);
                                                    
                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
                                                    Cell cellBranch19 = row1.createCell((short) 13);
                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
                                                    cellBranch19.setCellStyle(my_styleBranch);

                                                    Cell cellBranch2 = row1.createCell((short) 14);
                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
                                                    cellBranch2.setCellStyle(my_styleBranch);

                                                    Cell cellBranch3 = row1.createCell((short) 15);
                                                    cellBranch3.setCellValue(temp1.SERVICE_TYPE_DESC);
                                                    cellBranch3.setCellStyle(my_styleBranch);

                                                    String sP_ID = temp1.P_ID;
                                                    Cell cellBranch6 = row1.createCell((short) 16);
                                                    cellBranch6.setCellValue(sP_ID);
                                                    cellBranch6.setCellStyle(my_styleBranch);

                                                    Cell cellBranch7 = row1.createCell((short) 17);
                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
                                                    cellBranch7.setCellStyle(my_styleBranch);
                                                    
                                                    String valueTokenSN = temp1.TOKEN_SN;
                                                    if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                    Cell cellBranch21 = row1.createCell((short) 18);
                                                    cellBranch21.setCellValue(valueTokenSN);
                                                    cellBranch21.setCellStyle(my_styleBranch);
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
                                            cellBranch001.setCellValue("Tỉ lệ hoa hồng");
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
                                            cellBranch011.setCellValue("Giá token");
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
                                            cellBranch11.setCellValue("Token tồn đầu kỳ");
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
                                            cellBranch21.setCellValue("Tống số token nhập trong kỳ");
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
                                            cellBranch31.setCellValue("Tổng số token phát sinh trong kỳ");
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
                                            cellBranch41.setCellValue("Token tồn cuối kỳ");
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
                                            cellProfile0l.setCellValue("Tiền dịch vụ phải trả về nhà cung cấp");
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
                                            cellProfile1l.setCellValue("Token xuất hóa đơn trong kỳ");
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
                                            cellProfile2l.setCellValue("Thưởng trong kỳ");
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
                                            cellProfile3l.setCellValue("Tổng tiền trước VAT");
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
                                            cellProfile4l.setCellValue("VAT");
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
                                            cellProfile5l.setCellValue("Tổng tiền sau VAT");
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
                                            cellProfile6l.setCellValue("Số tiền tạm giữ hồ sơ thiếu");
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
                                            cellProfile7l.setCellValue("Số tiền bên B nợ chưa thanh toán kỳ trước");
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
                                            cellProfile8l.setCellValue("TỔNG TIỀN PHẢI TRẢ");
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
                                            cellProfile9l.setCellValue("Số tiền Bên B tạm ứng trong kỳ");
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
                                            cellProfile10l.setCellValue("Số tiền đặt cọc thiết bị được trừ trong kỳ");
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
                                            cellProfile11l.setCellValue("Số tiền phạt hồ sơ được trả lại trong kỳ");
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
                                            cellProfile12l.setCellValue("TỔNG CÁC KHOẢN GIẢM TRỪ");
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
                                            cellProfile13l.setCellValue("SỐ CÒN PHẢI TRẢ CUỐI KỲ");
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
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### TONG HOP CLOSE">
//                                            SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("TONG HOP");
//                                            
//                                            BRANCH[][] rsBranch = new BRANCH[1][];
//                                            String sBranchReport = "";
//                                            com.S_BO_BRANCH_DETAIL(EscapeUtils.escapeHtmlSearch(idBranchOffice), rsBranch);
//                                            if(rsPgin[0].length > 0) {
//                                                sBranchReport = EscapeUtils.CheckTextNull(rsBranch[0][0].REMARK);
//                                            }
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - HEADER">
//                                            i = 0;
//                                            Row rowSyntheticTitle;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            CellStyle my_styleSyntheticTitle = wb.createCellStyle();
//                                            Font fontBranchSyntheticTitle = wb.createFont();
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            fontBranchSyntheticTitle.setBoldweight((short) 700);
//                                            fontBranchSyntheticTitle.setFontName("Verdana");
//                                            my_styleSyntheticTitle.setFont(fontBranchSyntheticTitle);
//
//                                            Cell cellSyntheticTitle;
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM");
//                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
//                                            i = i+1;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("Độc lập - Tự do - Hạnh phúc");
//                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
//                                            i = i+1;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 6, 8));
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 5);
//                                            cellSyntheticTitle.setCellValue("............, ngày…. Tháng….. Năm 2020");
//                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
//                                            i = i+1;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("BIÊN BẢN ĐỐI SOÁT PHÁT TRIỂN CHỨNG THƯ SỐ THÁNG " + ToCreateDate.trim() + "/" + FromCreateDate.trim());
//                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
//                                            i = i+1;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("GIỮA CÔNG TY CỔ PHẦN HỖ TRỢ DOANH NGHIỆP VÀ " + sBranchReport);
//                                            my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
//                                            i = i+2;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            CellStyle my_styleSyntheticTitleDN = wb.createCellStyle();
//                                            Font fontBranchSyntheticTitleDN = wb.createFont();
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            fontBranchSyntheticTitleDN.setBoldweight((short) 700);
//                                            fontBranchSyntheticTitleDN.setFontName("Verdana");
//                                            my_styleSyntheticTitleDN.setFont(fontBranchSyntheticTitleDN);
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("SỐ LƯỢNG KHÁCH HÀNG DOANH NGHIỆP PHÁT TRIỂN ĐƯỢC TRONG THÁNG");
//                                            my_styleSyntheticTitleDN.setAlignment(my_styleSyntheticTitleDN.ALIGN_LEFT);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleDN);
//                                            //</editor-fold>
//                                            
//                                            i = i + 1;
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - DN">
//                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
//                                            sheet.setColumnWidth(1, 18 * 255);
//                                            sheet.setColumnWidth(2, 18 * 255);
//                                            sheet.setColumnWidth(3, 16 * 255);
//                                            sheet.setColumnWidth(4, 16 * 255);
//                                            sheet.setColumnWidth(5, 18 * 255);
//                                            sheet.setColumnWidth(6, 16 * 255);
//                                            sheet.setColumnWidth(7, 28 * 255);
//                                            sheet.setColumnWidth(8, 28 * 255);
//                                            Row row = sheet.createRow(i);
//                                            Cell cell = row.createCell((short) 0);
//                                            cell.setCellValue(sCellSTT);
//                                            cell.setCellStyle(my_style);
//
//                                            Cell cell1 = row.createCell((short) 1);
//                                            cell1.setCellValue("GÓI DỊCH VỤ");
//                                            cell1.setCellStyle(my_style);
//
//                                            Cell cell2 = row.createCell((short) 2);
//                                            cell2.setCellValue("SỐ LƯỢNG KH");
//                                            cell2.setCellStyle(my_style);
//
//                                            Cell cell3 = row.createCell((short) 3);
//                                            cell3.setCellValue("ĐƠN GIÁ");
//                                            cell3.setCellStyle(my_style);
//
//                                            Cell cell4 = row.createCell((short) 4);
//                                            cell4.setCellValue("THÀNH TIỀN");
//                                            cell4.setCellStyle(my_style);
//
//                                            Cell cell5 = row.createCell((short) 5);
//                                            cell5.setCellValue("TỶ LỆ HOA HỒNG");
//                                            cell5.setCellStyle(my_style);
//                                            
//                                            Cell cell6 = row.createCell((short) 6);
//                                            cell6.setCellValue("HOA HỒNG");
//                                            cell6.setCellStyle(my_style);
//                                            
//                                            Cell cell7 = row.createCell((short) 7);
//                                            cell7.setCellValue("ĐƠN GIÁ PHẢI TRẢ");
//                                            cell7.setCellStyle(my_style);
//                                            
//                                            Cell cell8 = row.createCell((short) 8);
//                                            cell8.setCellValue("TỔNG TIỀN PHẢI TRẢ");
//                                            cell8.setCellStyle(my_style);
//                                            i = i+1;
//                                            k = 0;
//                                            CERTIFICATION_REPORT_SUMMARY[][] rsReport;
//                                            rsReport = new CERTIFICATION_REPORT_SUMMARY[1][];
//                                            com.S_BO_REPORT_SUMMARY_ENTERPRISE(EscapeUtils.escapeHtmlSearch(idBranchOffice),
//                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
//                                                SessUserID, loginLanguage, rsReport, SessUserAgentID);
//                                            double countCustomer = 0;
//                                            double intoMoney = 0;
//                                            double moneyRose = 0;
//                                            double sumMoney = 0;
//                                            boolean isHasReportSumaryDN = false;
//                                            for (CERTIFICATION_REPORT_SUMMARY temp1 : rsReport[0]) {
//                                                isHasReportSumaryDN = true;
//                                                if (k == 0) {
//                                                    k = 1;
//                                                } else {
//                                                    k++;
//                                                }
//                                                Row row1 = sheet.createRow(i);
//                                                CellStyle my_styleBranch = wb.createCellStyle();
//                                                Font fontBranch = wb.createFont();
//
//                                                fontBranch.setFontName("Verdana");
//    //                                            fontBranch.setColor((short) 12);
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//    //                                            my_styleBranch.setFillBackgroundColor((short) 9);
//
//                                                Cell cellBranch = row1.createCell((short) 0);
//                                                cellBranch.setCellValue(k);
//                                                cellBranch.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch1 = row1.createCell((short) 1);
//                                                cellBranch1.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
//                                                cellBranch1.setCellStyle(my_styleBranch);
//                                                
//                                                countCustomer = countCustomer+temp1.NUMBER_CUSTOMER;
//                                                Cell cellBranch2 = row1.createCell((short) 2);
//                                                cellBranch2.setCellValue(temp1.NUMBER_CUSTOMER);
//                                                cellBranch2.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch3 = row1.createCell((short) 3);
//                                                cellBranch3.setCellValue(temp1.CERTIFICATION_PROFILE_AMOUNT);
//                                                cellBranch3.setCellStyle(my_styleBranch);
//                                                
//                                                double intoMoneyItem = temp1.NUMBER_CUSTOMER * temp1.CERTIFICATION_PROFILE_AMOUNT;
//                                                intoMoney = intoMoney + intoMoneyItem;
//                                                Cell cellBranch4 = row1.createCell((short) 4);
//                                                cellBranch4.setCellValue(clsCom.convertMoneyDoubleZero(intoMoneyItem));
//                                                cellBranch4.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch5 = row1.createCell((short) 5);
//                                                cellBranch5.setCellValue(temp1.DISCOUNT_RATE + "%");
//                                                cellBranch5.setCellStyle(my_styleBranch);
//
//                                                double moneyRoseItem = (temp1.CERTIFICATION_PROFILE_AMOUNT / 100) * temp1.DISCOUNT_RATE;
//                                                moneyRose = moneyRose + moneyRoseItem;
//                                                Cell cellBranch6 = row1.createCell((short) 6);
//                                                cellBranch6.setCellValue(clsCom.convertMoneyDoubleZero(moneyRoseItem));
//                                                cellBranch6.setCellStyle(my_styleBranch);
//
//                                                double returnMoneyItem = temp1.CERTIFICATION_PROFILE_AMOUNT - moneyRoseItem;
//                                                Cell cellBranch7 = row1.createCell((short) 7);
//                                                cellBranch7.setCellValue(clsCom.convertMoneyDoubleZero(returnMoneyItem));
//                                                cellBranch7.setCellStyle(my_styleBranch);
//
//                                                double sumMoneyItem = temp1.NUMBER_CUSTOMER * returnMoneyItem;
//                                                sumMoney = sumMoney + sumMoneyItem;
//                                                Cell cellBranch8 = row1.createCell((short) 8);
//                                                cellBranch8.setCellValue(clsCom.convertMoneyDoubleZero(sumMoneyItem));
//                                                cellBranch8.setCellStyle(my_styleBranch);
//
//                                                i++;
//                                            }
//                                            if(isHasReportSumaryDN == true)
//                                            {
//                                                Row row1 = sheet.createRow(i);
//                                                CellStyle my_styleBranch = wb.createCellStyle();
//                                                Font fontBranch = wb.createFont();
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
//
//                                                fontBranch.setFontName("Verdana");
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//
//                                                Cell cellBranch = row1.createCell((short) 0);
//                                                cellBranch.setCellValue("TỔNG");
//                                                cellBranch.setCellStyle(my_style);
//                                                Cell cellBranch1 = row1.createCell((short) 1);
//                                                cellBranch1.setCellStyle(my_style);
//
////                                                Cell cellBranch1 = row1.createCell((short) 1);
////                                                cellBranch1.setCellValue("TỔNG");
////                                                cellBranch1.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch2 = row1.createCell((short) 2);
//                                                cellBranch2.setCellValue(countCustomer);
//                                                cellBranch2.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch3 = row1.createCell((short) 3);
//                                                cellBranch3.setCellValue("");
//                                                cellBranch3.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch4 = row1.createCell((short) 4);
//                                                cellBranch4.setCellValue(clsCom.convertMoneyDoubleZero(intoMoney));
//                                                cellBranch4.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch5 = row1.createCell((short) 5);
//                                                cellBranch5.setCellValue("");
//                                                cellBranch5.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch6 = row1.createCell((short) 6);
//                                                cellBranch6.setCellValue(clsCom.convertMoneyDoubleZero(moneyRose));
//                                                cellBranch6.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch7 = row1.createCell((short) 7);
//                                                cellBranch7.setCellValue("");
//                                                cellBranch7.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch8 = row1.createCell((short) 8);
//                                                cellBranch8.setCellValue(clsCom.convertMoneyDoubleZero(sumMoney));
//                                                cellBranch8.setCellStyle(my_styleBranch);
//                                            }
//                                            //</editor-fold>
//                                            
//                                            i = i+2;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            CellStyle my_styleSyntheticTitleCN = wb.createCellStyle();
//                                            Font fontBranchSyntheticTitleCN = wb.createFont();
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 8));
//                                            fontBranchSyntheticTitleCN.setBoldweight((short) 700);
//                                            fontBranchSyntheticTitleCN.setFontName("Arial");
//                                            my_styleSyntheticTitleCN.setFont(fontBranchSyntheticTitleCN);
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
//                                            cellSyntheticTitle.setCellValue("SỐ LƯỢNG KHÁCH HÀNG CÁ NHÂN PHÁT TRIỂN ĐƯỢC TRONG THÁNG");
//                                            my_styleSyntheticTitleCN.setAlignment(my_styleSyntheticTitleDN.ALIGN_LEFT);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleCN);
//                                            i = i + 1;
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - CN">
//                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
//                                            sheet.setColumnWidth(1, 18 * 255);
//                                            sheet.setColumnWidth(2, 18 * 255);
//                                            sheet.setColumnWidth(3, 16 * 255);
//                                            sheet.setColumnWidth(4, 16 * 255);
//                                            sheet.setColumnWidth(5, 18 * 255);
//                                            sheet.setColumnWidth(6, 16 * 255);
//                                            sheet.setColumnWidth(7, 28 * 255);
//                                            sheet.setColumnWidth(8, 28 * 255);
//                                            Row rowOneCN = sheet.createRow(i);
//                                            Cell celwOneCNl = rowOneCN.createCell((short) 0);
//                                            celwOneCNl.setCellValue(sCellSTT);
//                                            celwOneCNl.setCellStyle(my_style);
//
//                                            Cell celwOneCNl1 = rowOneCN.createCell((short) 1);
//                                            celwOneCNl1.setCellValue("GÓI DỊCH VỤ");
//                                            celwOneCNl1.setCellStyle(my_style);
//
//                                            Cell cellwOneCN2 = rowOneCN.createCell((short) 2);
//                                            cellwOneCN2.setCellValue("SL KH");
//                                            cellwOneCN2.setCellStyle(my_style);
//
//                                            Cell cellwOneCN3 = rowOneCN.createCell((short) 3);
//                                            cellwOneCN3.setCellValue("ĐƠN GIÁ");
//                                            cellwOneCN3.setCellStyle(my_style);
//
//                                            Cell cellwOneCN4 = rowOneCN.createCell((short) 4);
//                                            cellwOneCN4.setCellValue("THÀNH TIỀN");
//                                            cellwOneCN4.setCellStyle(my_style);
//
//                                            Cell cellwOneCN5 = rowOneCN.createCell((short) 5);
//                                            cellwOneCN5.setCellValue("TỶ LỆ HOA HỒNG");
//                                            cellwOneCN5.setCellStyle(my_style);
//                                            
//                                            Cell cellwOneCN6 = rowOneCN.createCell((short) 6);
//                                            cellwOneCN6.setCellValue("HOA HỒNG");
//                                            cellwOneCN6.setCellStyle(my_style);
//                                            
//                                            Cell cellwOneCN7 = rowOneCN.createCell((short) 7);
//                                            cellwOneCN7.setCellValue("ĐƠN GIÁ PHẢI TRẢ");
//                                            cellwOneCN7.setCellStyle(my_style);
//                                            
//                                            Cell cellwOneCN8 = rowOneCN.createCell((short) 8);
//                                            cellwOneCN8.setCellValue("TỔNG TIỀN PHẢI TRẢ");
//                                            cellwOneCN8.setCellStyle(my_style);
//                                            i = i+1;
//                                            k = 0;
//                                            rsReport = new CERTIFICATION_REPORT_SUMMARY[1][];
//                                            com.S_BO_REPORT_SUMMARY_PERSONAL(EscapeUtils.escapeHtmlSearch(idBranchOffice),
//                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
//                                                SessUserID, loginLanguage, rsReport, SessUserAgentID);
//                                            countCustomer = 0;
//                                            intoMoney = 0;
//                                            moneyRose = 0;
//                                            sumMoney = 0;
//                                            boolean isHasReportSumaryCN = false;
//                                            for (CERTIFICATION_REPORT_SUMMARY temp1 : rsReport[0]) {
//                                                isHasReportSumaryCN = true;
//                                                if (k == 0) {
//                                                    k = 1;
//                                                } else {
//                                                    k++;
//                                                }
//                                                Row row1 = sheet.createRow(i);
//                                                CellStyle my_styleBranch = wb.createCellStyle();
//                                                Font fontBranch = wb.createFont();
//
//                                                fontBranch.setFontName("Verdana");
//    //                                            fontBranch.setColor((short) 12);
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//    //                                            my_styleBranch.setFillBackgroundColor((short) 9);
//
//                                                Cell cellBranch = row1.createCell((short) 0);
//                                                cellBranch.setCellValue(k);
//                                                cellBranch.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch1 = row1.createCell((short) 1);
//                                                cellBranch1.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
//                                                cellBranch1.setCellStyle(my_styleBranch);
//                                                
//                                                countCustomer = countCustomer+temp1.NUMBER_CUSTOMER;
//                                                Cell cellBranch2 = row1.createCell((short) 2);
//                                                cellBranch2.setCellValue(temp1.NUMBER_CUSTOMER);
//                                                cellBranch2.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch3 = row1.createCell((short) 3);
//                                                cellBranch3.setCellValue(temp1.CERTIFICATION_PROFILE_AMOUNT);
//                                                cellBranch3.setCellStyle(my_styleBranch);
//                                                
//                                                double intoMoneyItem = temp1.NUMBER_CUSTOMER * temp1.CERTIFICATION_PROFILE_AMOUNT;
//                                                intoMoney = intoMoney + intoMoneyItem;
//                                                Cell cellBranch4 = row1.createCell((short) 4);
//                                                cellBranch4.setCellValue(clsCom.convertMoneyDoubleZero(intoMoneyItem));
//                                                cellBranch4.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch5 = row1.createCell((short) 5);
//                                                cellBranch5.setCellValue(temp1.DISCOUNT_RATE + "%");
//                                                cellBranch5.setCellStyle(my_styleBranch);
//
//                                                double moneyRoseItem = (temp1.CERTIFICATION_PROFILE_AMOUNT / 100) * temp1.DISCOUNT_RATE;
//                                                moneyRose = moneyRose + moneyRoseItem;
//                                                Cell cellBranch6 = row1.createCell((short) 6);
//                                                cellBranch6.setCellValue(clsCom.convertMoneyDoubleZero(moneyRoseItem));
//                                                cellBranch6.setCellStyle(my_styleBranch);
//
//                                                double returnMoneyItem = temp1.CERTIFICATION_PROFILE_AMOUNT - moneyRoseItem;
//                                                Cell cellBranch7 = row1.createCell((short) 7);
//                                                cellBranch7.setCellValue(clsCom.convertMoneyDoubleZero(returnMoneyItem));
//                                                cellBranch7.setCellStyle(my_styleBranch);
//
//                                                double sumMoneyItem = temp1.NUMBER_CUSTOMER * returnMoneyItem;
//                                                sumMoney = sumMoney + sumMoneyItem;
//                                                Cell cellBranch8 = row1.createCell((short) 8);
//                                                cellBranch8.setCellValue(clsCom.convertMoneyDoubleZero(sumMoneyItem));
//                                                cellBranch8.setCellStyle(my_styleBranch);
//
//                                                i++;
//                                            }
//                                            if(isHasReportSumaryCN == true)
//                                            {
//                                                Row row1 = sheet.createRow(i);
//                                                CellStyle my_styleBranch = wb.createCellStyle();
//                                                Font fontBranch = wb.createFont();
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
//
//                                                fontBranch.setFontName("Verdana");
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//
//                                                Cell cellBranch = row1.createCell((short) 0);
//                                                cellBranch.setCellValue("TỔNG");
//                                                cellBranch.setCellStyle(my_style);
//                                                Cell cellBranch1 = row1.createCell((short) 1);
//                                                cellBranch1.setCellStyle(my_style);
//
//                                                Cell cellBranch2 = row1.createCell((short) 2);
//                                                cellBranch2.setCellValue(countCustomer);
//                                                cellBranch2.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch3 = row1.createCell((short) 3);
//                                                cellBranch3.setCellValue("");
//                                                cellBranch3.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch4 = row1.createCell((short) 4);
//                                                cellBranch4.setCellValue(clsCom.convertMoneyDoubleZero(intoMoney));
//                                                cellBranch4.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch5 = row1.createCell((short) 5);
//                                                cellBranch5.setCellValue("");
//                                                cellBranch5.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch6 = row1.createCell((short) 6);
//                                                cellBranch6.setCellValue(clsCom.convertMoneyDoubleZero(moneyRose));
//                                                cellBranch6.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch7 = row1.createCell((short) 7);
//                                                cellBranch7.setCellValue("");
//                                                cellBranch7.setCellStyle(my_styleBranch);
//
//                                                Cell cellBranch8 = row1.createCell((short) 8);
//                                                cellBranch8.setCellValue(clsCom.convertMoneyDoubleZero(sumMoney));
//                                                cellBranch8.setCellStyle(my_styleBranch);
//                                            }
//                                            //</editor-fold>
//                                            
//                                            i = i + 2;
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - TOKEN">
//                                            Row rowOneToken = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                            Cell celwOneTokenl = rowOneToken.createCell((short) 0);
//                                            celwOneTokenl.setCellValue("Nội dung");
//                                            celwOneTokenl.setCellStyle(my_style);
//                                            Cell celwOneTokenl01 = rowOneToken.createCell((short) 1);
//                                            celwOneTokenl01.setCellStyle(my_style);
//                                            Cell celwOneTokenl02 = rowOneToken.createCell((short) 2);
//                                            celwOneTokenl02.setCellStyle(my_style);
//
//                                            Cell celwOneTokenl1 = rowOneToken.createCell((short) 3);
//                                            celwOneTokenl1.setCellValue("Số lượng");
//                                            celwOneTokenl1.setCellStyle(my_style);
//
//                                            Cell cellwOneToken2 = rowOneToken.createCell((short) 4);
//                                            cellwOneToken2.setCellValue("Thành tiền");
//                                            cellwOneToken2.setCellStyle(my_style);
//                                            k = 0;
//                                            String sTOKEN_ENTERPRISE = "0";
//                                            GENERAL_POLICY[][] sessGeneralPolicy;
//                                            sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
//                                            if (sessGeneralPolicy[0].length > 0) {
//                                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
//                                                {
//                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_TOKEN_AMOUNT_ENTERPRISE))
//                                                    {
//                                                        sTOKEN_ENTERPRISE = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                            int[] pREMAINING_BEGINING_MONTH = new int[1];
//                                            int[] pIMPORT_IN_MONTH = new int[1];
//                                            int[] pTOKEN_USED_IN_MONTH = new int[1];
//                                            int[] pREAMINING_END_MONTH = new int[1];
//                                            String strResult = com.S_BO_TOKEN_REPORT_SUMMARY(EscapeUtils.escapeHtmlSearch(idBranchOffice),
//                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate), SessUserID,
//                                                pREMAINING_BEGINING_MONTH, pIMPORT_IN_MONTH, pTOKEN_USED_IN_MONTH, pREAMINING_END_MONTH);
//                                            if("0".equals(strResult)) {
//                                                i=i+1;
//                                                Row row1 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                CellStyle my_styleBranch = wb.createCellStyle();
//                                                Font fontBranch = wb.createFont();
//
//                                                fontBranch.setFontName("Verdana");
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//
//                                                Cell cellBranch11 = row1.createCell((short) 0);
//                                                cellBranch11.setCellValue("Giá token đặt cọc");
//                                                cellBranch11.setCellStyle(my_styleBranch);
//                                                Cell cellBranch111 = row1.createCell((short) 1);
//                                                cellBranch111.setCellStyle(my_style);
//                                                Cell cellBranch112 = row1.createCell((short) 2);
//                                                cellBranch112.setCellStyle(my_style);
//
//                                                Cell cellBranch12 = row1.createCell((short) 3);
//                                                cellBranch12.setCellValue(clsCom.convertMoneyDoubleZero(Double.parseDouble(sTOKEN_ENTERPRISE.replace(",", ""))));
//                                                cellBranch12.setCellStyle(my_styleBranch);
//                                                
//                                                Cell cellBranch13 = row1.createCell((short) 4);
//                                                cellBranch13.setCellValue("");
//                                                cellBranch13.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                Row row2 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch21 = row2.createCell((short) 0);
//                                                cellBranch21.setCellValue("Token tồn đầu kỳ");
//                                                cellBranch21.setCellStyle(my_styleBranch);
//                                                Cell cellBranch211 = row2.createCell((short) 1);
//                                                cellBranch211.setCellStyle(my_style);
//                                                Cell cellBranch212 = row2.createCell((short) 2);
//                                                cellBranch212.setCellStyle(my_style);
//
//                                                Cell cellBranch22 = row2.createCell((short) 3);
//                                                cellBranch22.setCellValue(pREMAINING_BEGINING_MONTH[0]);
//                                                cellBranch22.setCellStyle(my_styleBranch);
//                                                
//                                                double s1 = pREMAINING_BEGINING_MONTH[0] * Integer.parseInt(sTOKEN_ENTERPRISE.replace(",", ""));
//                                                Cell cellBranch23 = row2.createCell((short) 4);
//                                                cellBranch23.setCellValue(clsCom.convertMoneyDoubleZero(s1));
//                                                cellBranch23.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                Row row3 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch31 = row3.createCell((short) 0);
//                                                cellBranch31.setCellValue("Token đặt cọc trong tháng");
//                                                cellBranch31.setCellStyle(my_styleBranch);
//                                                Cell cellBranch311 = row3.createCell((short) 1);
//                                                cellBranch311.setCellStyle(my_style);
//                                                Cell cellBranch312 = row3.createCell((short) 2);
//                                                cellBranch312.setCellStyle(my_style);
//
//                                                Cell cellBranch32 = row3.createCell((short) 3);
//                                                cellBranch32.setCellValue(pIMPORT_IN_MONTH[0]);
//                                                cellBranch32.setCellStyle(my_styleBranch);
//                                                double s2 = pIMPORT_IN_MONTH[0] * Integer.parseInt(sTOKEN_ENTERPRISE.replace(",", ""));
//                                                Cell cellBranch33 = row3.createCell((short) 4);
//                                                cellBranch33.setCellValue(clsCom.convertMoneyDoubleZero(s2));
//                                                cellBranch33.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                Row row4 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch41 = row4.createCell((short) 0);
//                                                cellBranch41.setCellValue("Token sử dụng trong tháng");
//                                                cellBranch41.setCellStyle(my_styleBranch);
//                                                Cell cellBranch411 = row4.createCell((short) 1);
//                                                cellBranch411.setCellStyle(my_style);
//                                                Cell cellBranch412 = row4.createCell((short) 2);
//                                                cellBranch412.setCellStyle(my_style);
//
//                                                Cell cellBranch42 = row4.createCell((short) 3);
//                                                cellBranch42.setCellValue(pTOKEN_USED_IN_MONTH[0]);
//                                                cellBranch42.setCellStyle(my_styleBranch);
//                                                double s3 = pTOKEN_USED_IN_MONTH[0] * Integer.parseInt(sTOKEN_ENTERPRISE.replace(",", ""));
//                                                Cell cellBranch43 = row4.createCell((short) 4);
//                                                cellBranch43.setCellValue(clsCom.convertMoneyDoubleZero(s3));
//                                                cellBranch43.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Row row5 = sheet.createRow(i);
//                                                Cell cellBranch51 = row5.createCell((short) 0);
//                                                cellBranch51.setCellValue("Token tồn cuối kỳ");
//                                                cellBranch51.setCellStyle(my_styleBranch);
//                                                Cell cellBranch511 = row5.createCell((short) 1);
//                                                cellBranch511.setCellStyle(my_style);
//                                                Cell cellBranch512 = row5.createCell((short) 2);
//                                                cellBranch512.setCellStyle(my_style);
//
//                                                Cell cellBranch52 = row5.createCell((short) 3);
//                                                cellBranch52.setCellValue(pREAMINING_END_MONTH[0]);
//                                                cellBranch52.setCellStyle(my_styleBranch);
//                                                double s4 = pREAMINING_END_MONTH[0] * Integer.parseInt(sTOKEN_ENTERPRISE.replace(",", ""));
//                                                Cell cellBranch53 = row5.createCell((short) 4);
//                                                cellBranch53.setCellValue(clsCom.convertMoneyDoubleZero(s4));
//                                                cellBranch53.setCellStyle(my_styleBranch);
//                                            }
//                                            //</editor-fold>
//                                            
//                                            i = i + 2;
//                                            //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - PROFILE">
////                                            sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
////                                            sheet.setColumnWidth(1, 12 * 255);
////                                            sheet.setColumnWidth(2, 12 * 255);
////                                            sheet.setColumnWidth(3, 18 * 255);
//                                            Row rowOneProfile = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                            Cell celwOneProfilel = rowOneProfile.createCell((short) 0);
//                                            celwOneProfilel.setCellValue("Nội dung");
//                                            celwOneProfilel.setCellStyle(my_style);
//                                            Cell celwOneProfilel01 = rowOneProfile.createCell((short) 1);
//                                            celwOneProfilel01.setCellStyle(my_style);
//                                            Cell celwOneProfilel02 = rowOneProfile.createCell((short) 2);
//                                            celwOneProfilel02.setCellStyle(my_style);
//
//                                            Cell celwOneProfilel1 = rowOneProfile.createCell((short) 3);
//                                            celwOneProfilel1.setCellValue("Số lượng");
//                                            celwOneProfilel1.setCellStyle(my_style);
//                                            String sLACK_OF_BRIEF = "0";
//                                            sessGeneralPolicy = (GENERAL_POLICY[][]) request.getSession(false).getAttribute("sessGeneralPolicy_System");
//                                            if (sessGeneralPolicy[0].length > 0) {
//                                                for(GENERAL_POLICY rsPolicy1: sessGeneralPolicy[0])
//                                                {
//                                                    if(rsPolicy1.NAME.equals(Definitions.CONFIG_POLICY_BO_FINE_FOR_LACK_OF_BRIEF))
//                                                    {
//                                                        sLACK_OF_BRIEF = EscapeUtils.CheckTextNull(rsPolicy1.VALUE);
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                            int[] pREMAINING_BEGINNING_MONTH = new int[1];
//                                            int[] pBRIEF_IN_MONTH = new int[1];
//                                            int[] pBRIEF_LACK_IN_MONTH_NO = new int[1];
//                                            int[] pBRIEF_COMPENSATE_IN_MONTH_NO = new int[1];
//                                            int[] pOVER_TIME_BRIEF_NO = new int[1];
//                                            int[] pREMAINING_END_MONTH = new int[1];
//                                            com.S_BO_CERTIFICATION_BRIEF_REPORT(EscapeUtils.escapeHtmlSearch(idBranchOffice),
//                                                EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate), SessUserID,
//                                                pREMAINING_BEGINNING_MONTH, pBRIEF_IN_MONTH, pBRIEF_LACK_IN_MONTH_NO, pBRIEF_COMPENSATE_IN_MONTH_NO,
//                                                pOVER_TIME_BRIEF_NO, pREMAINING_END_MONTH);
//                                            if(!"".equals(sLACK_OF_BRIEF)) {
//                                                i=i+1;
//                                                Row row1 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                CellStyle my_styleBranch = wb.createCellStyle();
//                                                Font fontBranch = wb.createFont();
//
//                                                fontBranch.setFontName("Verdana");
//                                                my_styleBranch.setFont(fontBranch);
//                                                my_styleBranch.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderTop(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderRight(HSSFCellStyle.BORDER_THIN);
//                                                my_styleBranch.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//
//                                                Cell cellBranch11 = row1.createCell((short) 0);
//                                                cellBranch11.setCellValue("Tiền tạm giữ trên 1 bộ hồ sơ");
//                                                cellBranch11.setCellStyle(my_styleBranch);
//                                                Cell cellBranch111 = row1.createCell((short) 1);
//                                                cellBranch111.setCellStyle(my_style);
//                                                Cell cellBranch112 = row1.createCell((short) 2);
//                                                cellBranch112.setCellStyle(my_style);
//
//                                                Cell cellBranch12 = row1.createCell((short) 3);
//                                                cellBranch12.setCellValue(clsCom.convertMoneyDoubleZero(Double.parseDouble(sLACK_OF_BRIEF.replace(",", ""))));
//                                                cellBranch12.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                Row row2 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch21 = row2.createCell((short) 0);
//                                                cellBranch21.setCellValue("Hồ sơ chưa trả đầu kỳ");
//                                                cellBranch21.setCellStyle(my_styleBranch);
//                                                Cell cellBranch211 = row2.createCell((short) 1);
//                                                cellBranch211.setCellStyle(my_style);
//                                                Cell cellBranch212 = row2.createCell((short) 2);
//                                                cellBranch212.setCellStyle(my_style);
//
//                                                Cell cellBranch22 = row2.createCell((short) 3);
//                                                cellBranch22.setCellValue(pREMAINING_BEGINNING_MONTH[0]);
//                                                cellBranch22.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                Row row3 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch31 = row3.createCell((short) 0);
//                                                cellBranch31.setCellValue("Hồ sơ phát sinh trong kỳ");
//                                                cellBranch31.setCellStyle(my_styleBranch);
//                                                Cell cellBranch311 = row3.createCell((short) 1);
//                                                cellBranch311.setCellStyle(my_style);
//                                                Cell cellBranch312 = row3.createCell((short) 2);
//                                                cellBranch312.setCellStyle(my_style);
//
//                                                Cell cellBranch32 = row3.createCell((short) 3);
//                                                cellBranch32.setCellValue(pBRIEF_IN_MONTH[0]);
//                                                cellBranch32.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                Row row4 = sheet.createRow(i);
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Cell cellBranch41 = row4.createCell((short) 0);
//                                                cellBranch41.setCellValue("Hồ sơ trả trong kỳ");
//                                                cellBranch41.setCellStyle(my_styleBranch);
//                                                Cell cellBranch411 = row4.createCell((short) 1);
//                                                cellBranch411.setCellStyle(my_style);
//                                                Cell cellBranch412 = row4.createCell((short) 2);
//                                                cellBranch412.setCellStyle(my_style);
//
//                                                Cell cellBranch42 = row4.createCell((short) 3);
//                                                cellBranch42.setCellValue(pBRIEF_LACK_IN_MONTH_NO[0]);
//                                                cellBranch42.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Row row5 = sheet.createRow(i);
//                                                Cell cellBranch51 = row5.createCell((short) 0);
//                                                cellBranch51.setCellValue("Hồ sơ đã trả bù cho kỳ trước");
//                                                cellBranch51.setCellStyle(my_styleBranch);
//                                                Cell cellBranch511 = row5.createCell((short) 1);
//                                                cellBranch511.setCellStyle(my_style);
//                                                Cell cellBranch512 = row5.createCell((short) 2);
//                                                cellBranch512.setCellStyle(my_style);
//
//                                                Cell cellBranch52 = row5.createCell((short) 3);
//                                                cellBranch52.setCellValue(pBRIEF_COMPENSATE_IN_MONTH_NO[0]);
//                                                cellBranch52.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Row row6 = sheet.createRow(i);
//                                                Cell cellBranch61 = row6.createCell((short) 0);
//                                                cellBranch61.setCellValue("Hồ sơ quá hạn");
//                                                cellBranch61.setCellStyle(my_styleBranch);
//                                                Cell cellBranch611 = row6.createCell((short) 1);
//                                                cellBranch611.setCellStyle(my_style);
//                                                Cell cellBranch612 = row6.createCell((short) 2);
//                                                cellBranch612.setCellStyle(my_style);
//
//                                                Cell cellBranch62 = row6.createCell((short) 3);
//                                                cellBranch62.setCellValue(pOVER_TIME_BRIEF_NO[0]);
//                                                cellBranch62.setCellStyle(my_styleBranch);
//                                                ///
//                                                i=i+1;
//                                                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 2));
//                                                Row row7 = sheet.createRow(i);
//                                                Cell cellBranch71 = row7.createCell((short) 0);
//                                                cellBranch71.setCellValue("Hồ sơ chưa trả cuối kỳ");
//                                                cellBranch71.setCellStyle(my_styleBranch);
//                                                Cell cellBranch711 = row7.createCell((short) 1);
//                                                cellBranch711.setCellStyle(my_style);
//                                                Cell cellBranch712 = row7.createCell((short) 2);
//                                                cellBranch712.setCellStyle(my_style);
//
//                                                Cell cellBranch72 = row7.createCell((short) 3);
//                                                cellBranch72.setCellValue(pREMAINING_END_MONTH[0]);
//                                                cellBranch72.setCellStyle(my_styleBranch);
//                                                
//                                            }
//                                            //</editor-fold>
//                                            
//                                            i = i+3;
//                                            rowSyntheticTitle = sheet.createRow(i);
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 1, 3));
//                                            CellStyle my_styleSyntheticTitleCA = wb.createCellStyle();
//                                            Font fontBranchSyntheticTitleCA = wb.createFont();
//                                            fontBranchSyntheticTitleCA.setBoldweight((short) 700);
//                                            fontBranchSyntheticTitleCA.setFontName("Arial");
//                                            my_styleSyntheticTitleCA.setFont(fontBranchSyntheticTitleCA);
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 1);
//                                            cellSyntheticTitle.setCellValue("ĐẠI DIỆN NHÀ CUNG CẤP");
//                                            my_styleSyntheticTitleCA.setAlignment(my_styleSyntheticTitleDN.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleCA);
//                                            
//                                            sheet.addMergedRegion(new CellRangeAddress(i, i, 5, 7));
//                                            cellSyntheticTitle = rowSyntheticTitle.createCell((short) 5);
//                                            cellSyntheticTitle.setCellValue("ĐẠI DIỆN ĐẠI LÝ");
//                                            my_styleSyntheticTitleCA.setAlignment(my_styleSyntheticTitleDN.ALIGN_CENTER);
//                                            cellSyntheticTitle.setCellStyle(my_styleSyntheticTitleCA);
                                            //</editor-fold>
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET Customer Details CLOSE">
                                            /*rsPgin = new CERTIFICATION[1][];
                                            com.S_BO_REPORT_CROSS_CHECK_DETAIL(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice), SessUserID,
                                                loginLanguage, rsPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO,
                                                Integer.parseInt(countList), SessUserAgentID);
                                            if(rsPgin[0].length > 0)
                                            {
                                                SXSSFSheet sheetCusDetail = (SXSSFSheet) wb.createSheet("CHI TIET KHACH HANG");
                                                sheetCusDetail.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                                sheetCusDetail.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
                                                sheetCusDetail.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                                sheetCusDetail.setColumnWidth(3, 18*255);
                                                sheetCusDetail.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                                sheetCusDetail.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                                sheetCusDetail.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                                sheetCusDetail.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                                sheetCusDetail.setColumnWidth(8, 18*255);
                                                sheetCusDetail.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                                sheetCusDetail.setColumnWidth(10, 18*255);
                                                sheetCusDetail.setColumnWidth(11, 18*255);
                                                sheetCusDetail.setColumnWidth(12, 18*255);
                                                sheetCusDetail.setColumnWidth(13, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                                sheetCusDetail.setColumnWidth(14, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
                                                sheetCusDetail.setColumnWidth(15, 24*255);
                                                sheetCusDetail.setColumnWidth(16, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_GEN);
                                                sheetCusDetail.setColumnWidth(17, 26 * 255);
                                                sheetCusDetail.setColumnWidth(18, 30 * 255);
                                                sheetCusDetail.setColumnWidth(19, 40 * 255);
                                                sheetCusDetail.setColumnWidth(20, 32 * 255);
                                                sheetCusDetail.setColumnWidth(21, 18 * 255);
                                                Row rowCusDetail = sheetCusDetail.createRow(0);
                                                Cell celCus = rowCusDetail.createCell((short) 0);
                                                celCus.setCellValue(sCellSTT);
                                                celCus.setCellStyle(my_style);
                                                Cell celCus1 = rowCusDetail.createCell((short) 1);
                                                celCus1.setCellValue(sCellAgency);
                                                celCus1.setCellStyle(my_style);
                                                Cell celCus2 = rowCusDetail.createCell((short) 2);
                                                celCus2.setCellValue(sCellUser);
                                                celCus2.setCellStyle(my_style);
                                                Cell celCus3 = rowCusDetail.createCell((short) 3);
                                                celCus3.setCellValue(sCellCertState);
                                                celCus3.setCellStyle(my_style);
                                                Cell celCus4 = rowCusDetail.createCell((short) 4);
                                                celCus4.setCellValue(sCellMST);
                                                celCus4.setCellStyle(my_style);
                                                Cell celCus5 = rowCusDetail.createCell((short) 5);
                                                celCus5.setCellValue(sCellCompany);
                                                celCus5.setCellStyle(my_style);
                                                Cell celCus6 = rowCusDetail.createCell((short) 6);
                                                celCus6.setCellValue(sCellCMND);
                                                celCus6.setCellStyle(my_style);
                                                Cell celCus7 = rowCusDetail.createCell((short) 7);
                                                celCus7.setCellValue(sCellPersonal);
                                                celCus7.setCellStyle(my_style);
                                                Cell celCus8 = rowCusDetail.createCell((short) 8);
                                                celCus8.setCellValue(sCellProvince);
                                                celCus8.setCellStyle(my_style);
                                                Cell celCus9 = rowCusDetail.createCell((short) 9);
                                                celCus9.setCellValue(sCellProfile);
                                                celCus9.setCellStyle(my_style);
                                                Cell celCus10 = rowCusDetail.createCell((short) 10);
                                                celCus10.setCellValue(sCellProfileFee);
                                                celCus10.setCellStyle(my_style);
                                                Cell celCus11 = rowCusDetail.createCell((short) 11);
                                                celCus11.setCellValue(sCellTokenQuality);
                                                celCus11.setCellStyle(my_style);
                                                Cell celCus12 = rowCusDetail.createCell((short) 12);
                                                celCus12.setCellValue(sCellTokenFee);
                                                celCus12.setCellStyle(my_style);
                                                Cell celCus13 = rowCusDetail.createCell((short) 13);
                                                celCus13.setCellValue(sCellRequestType);
                                                celCus13.setCellStyle(my_style);
                                                Cell celCus14 = rowCusDetail.createCell((short) 14);
                                                celCus14.setCellValue(sCellCreateDate);
                                                celCus14.setCellStyle(my_style);
                                                Cell celCus152 = rowCusDetail.createCell((short) 15);
                                                celCus152.setCellValue("Ngày duyệt CA");
                                                celCus152.setCellStyle(my_style);
                                                Cell celCus15 = rowCusDetail.createCell((short) 16);
                                                celCus15.setCellValue(sCellGenDate);
                                                celCus15.setCellStyle(my_style);
                                                Cell celCus16 = rowCusDetail.createCell((short) 17);
                                                celCus16.setCellValue(sCellCollationDate);
                                                celCus16.setCellStyle(my_style);
                                                Cell celCus17 = rowCusDetail.createCell((short) 18);
                                                celCus17.setCellValue(sCellCollationState);
                                                celCus17.setCellStyle(my_style);
                                                Cell celCus18 = rowCusDetail.createCell((short) 19);
                                                celCus18.setCellValue(sCellProfileFine);
                                                celCus18.setCellStyle(my_style);
                                                Cell celCus19 = rowCusDetail.createCell((short) 20);
                                                celCus19.setCellValue("Số sê-ri CTS");
                                                celCus19.setCellStyle(my_style);
                                                Cell celCus110 = rowCusDetail.createCell((short) 21);
                                                celCus110.setCellValue(sTokenSN);
                                                celCus110.setCellStyle(my_style);
                                                i = 1;
                                                k = 0;
                                                for (CERTIFICATION temp1 : rsPgin[0]) {
                                                    if (k == 0) {
                                                        k = 1;
                                                    } else {
                                                        k++;
                                                    }
                                                    Row row1 = sheetCusDetail.createRow(i);
                                                    CellStyle my_styleBranch = wb.createCellStyle();
                                                    Font fontBranch = wb.createFont();
                                                    fontBranch.setFontName("Arial");
                                                    my_styleBranch.setFont(fontBranch);

                                                    CellStyle my_styleBranchDate = wb.createCellStyle();
                                                    Font fontBranchDate = wb.createFont();
                                                    fontBranchDate.setFontName("Arial");
                                                    my_styleBranchDate.setFont(fontBranchDate);
                                                    my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS));
                                                    
                                                    Cell cellBranch = row1.createCell((short) 0);
                                                    cellBranch.setCellValue(k);
                                                    cellBranch.setCellStyle(my_styleBranch);

                                                    Cell cellBranch1 = row1.createCell((short)1);
                                                    cellBranch1.setCellValue(temp1.BRANCH_DESC);
                                                    cellBranch1.setCellStyle(my_styleBranch);

                                                    Cell cellBranch2 = row1.createCell((short) 2);
                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
                                                    cellBranch2.setCellStyle(my_styleBranch);

                                                    Cell cellBranch3 = row1.createCell((short) 3);
                                                    cellBranch3.setCellValue(temp1.CERTIFICATION_STATE_DESC);
                                                    cellBranch3.setCellStyle(my_styleBranch);

                                                    String sTAX_CODE = temp1.TAX_CODE;
//                                                    if(!"".equals(sTAX_CODE)){sTAX_CODE = "'"+sTAX_CODE;}
                                                    Cell cellBranch4 = row1.createCell((short) 4);
                                                    cellBranch4.setCellValue(sTAX_CODE);
                                                    cellBranch4.setCellStyle(my_styleBranch);

                                                    Cell cellBranch5 = row1.createCell((short) 5);
                                                    cellBranch5.setCellValue(temp1.COMPANY_NAME);
                                                    cellBranch5.setCellStyle(my_styleBranch);

                                                    String sP_ID = temp1.P_ID;
//                                                    if(!"".equals(sP_ID)){sP_ID = "'"+sP_ID;}
                                                    Cell cellBranch6 = row1.createCell((short) 6);
                                                    cellBranch6.setCellValue(sP_ID);
                                                    cellBranch6.setCellStyle(my_styleBranch);

                                                    Cell cellBranch7 = row1.createCell((short) 7);
                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
                                                    cellBranch7.setCellStyle(my_styleBranch);

                                                    Cell cellBranch8 = row1.createCell((short) 8);
                                                    cellBranch8.setCellValue(temp1.CITY_PROVINCE);
                                                    cellBranch8.setCellStyle(my_styleBranch);

                                                    Cell cellBranch9 = row1.createCell((short) 9);
                                                    cellBranch9.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
                                                    cellBranch9.setCellStyle(my_styleBranch);

                                                    Cell cellBranch10 = row1.createCell((short) 10);
                                                    cellBranch10.setCellValue(temp1.FEE_AMOUNT);
                                                    cellBranch10.setCellStyle(my_styleBranch);

                                                    Cell cellBranch11 = row1.createCell((short) 11);
                                                    cellBranch11.setCellValue(temp1.TOKEN_NUMBER);
                                                    cellBranch11.setCellStyle(my_styleBranch);

                                                    Cell cellBranch12 = row1.createCell((short) 12);
                                                    cellBranch12.setCellValue(temp1.TOKEN_AMOUNT);
                                                    cellBranch12.setCellStyle(my_styleBranch);

                                                    Cell cellBranch13 = row1.createCell((short) 13);
                                                    cellBranch13.setCellValue(temp1.CERTIFICATION_ATTR_TYPE_DESC);
                                                    cellBranch13.setCellStyle(my_styleBranch);

                                                    Cell cellBranch14 = row1.createCell((short) 14);
                                                    Date dateCreate = CommonFunction.convertStringToDate(temp1.CREATED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                    if(dateCreate != null) {
                                                        cellBranch14.setCellValue(dateCreate);
                                                    } else {
                                                        cellBranch14.setCellValue("");
                                                    }
                                                    cellBranch14.setCellStyle(my_styleBranchDate);

                                                    Cell cellBranch152 = row1.createCell((short) 15);
                                                    Date dateApprove = CommonFunction.convertStringToDate(temp1.APPROVAL_CA_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                    if(dateApprove != null) {
                                                        cellBranch152.setCellValue(dateApprove);
                                                    } else {
                                                        cellBranch152.setCellValue("");
                                                    }
                                                    cellBranch152.setCellStyle(my_styleBranchDate);
                                                    
                                                    Cell cellBranch15 = row1.createCell((short) 16);
                                                    Date dateIssue = CommonFunction.convertStringToDate(temp1.ISSUED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                                    if(dateIssue != null) {
                                                        cellBranch15.setCellValue(dateIssue);
                                                    } else {
                                                        cellBranch15.setCellValue("");
                                                    }
                                                    cellBranch15.setCellStyle(my_styleBranchDate);

                                                    Cell cellBranch16 = row1.createCell((short) 17);
                                                    cellBranch16.setCellValue(temp1.CROSS_CHECKED_DT);
                                                    cellBranch16.setCellStyle(my_styleBranch);

                                                    String sCollated = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CROSS_CHECK_NOTENOUGH).trim();
                                                    if(temp1.CROSS_CHECK_ENABLED == true) {
                                                        sCollated = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CROSS_CHECK_ENOUGH).trim();
                                                    }
                                                    Cell cellBranch17 = row1.createCell((short) 18);
                                                    cellBranch17.setCellValue(sCollated);
                                                    cellBranch17.setCellStyle(my_styleBranch);
                                                    
                                                    Cell cellBranch18 = row1.createCell((short) 19);
                                                    cellBranch18.setCellValue(temp1.FINE_FOR_LACK_OF_BRIEF);
                                                    cellBranch18.setCellStyle(my_styleBranch);
                                                    
                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
//                                                    if(!"".equals(sCERTIFICATION_SN)){sCERTIFICATION_SN = "'"+sCERTIFICATION_SN;}
                                                    Cell cellBranch19 = row1.createCell((short) 20);
                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
                                                    cellBranch19.setCellStyle(my_styleBranch);
                                                    
                                                    String valueTokenSN = temp1.TOKEN_SN;
                                                    if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                    Cell cellBranch21 = row1.createCell((short) 21);
                                                    cellBranch21.setCellValue(valueTokenSN);
                                                    cellBranch21.setCellStyle(my_styleBranch);
                                                    i++;
                                                }
                                            }*/
                                            //</editor-fold>
                                            
                                            //<editor-fold defaultstate="collapsed" desc="### SHEET Decline Customers">
                                            rsReportPgin = new CERTIFICATION_CONTROL_REPORT[1][];
                                            com.S_BO_REPORT_CERTIFICATE_DECLINE(EscapeUtils.escapeHtmlSearch(ToCreateDate),
                                                EscapeUtils.escapeHtmlSearch(FromCreateDate), EscapeUtils.escapeHtmlSearch(idBranchOffice), SessUserID,
                                                loginLanguage, rsReportPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO,
                                                Integer.parseInt(countList), SessUserAgentID, "","");
                                            if(rsReportPgin[0].length > 0)
                                            {
                                                SXSSFSheet sheetCusDetail = (SXSSFSheet) wb.createSheet("KHACH HANG HUY");
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
                                                sheetCusDetail.setColumnWidth(12, 40 * 255);
                                                sheetCusDetail.setColumnWidth(13, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                                sheetCusDetail.setColumnWidth(14, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                                sheetCusDetail.setColumnWidth(15, 22 * 255);
                                                sheetCusDetail.setColumnWidth(16, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                                sheetCusDetail.setColumnWidth(17, 18 * 255);
                                                sheetCusDetail.setColumnWidth(18, 40 * 255);
//                                                sheetCusDetail.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
//                                                sheetCusDetail.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY);
//                                                sheetCusDetail.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
//                                                sheetCusDetail.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
//                                                sheetCusDetail.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
//                                                sheetCusDetail.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
//                                                sheetCusDetail.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
//                                                sheetCusDetail.setColumnWidth(7, 18*255);
//                                                sheetCusDetail.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
//                                                sheetCusDetail.setColumnWidth(9, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
//                                                sheetCusDetail.setColumnWidth(10, Definitions.CONFIG_EXPORT_QUICK_WIDTH_DATE_CREATE);
//                                                sheetCusDetail.setColumnWidth(11, 24*255);
//                                                sheetCusDetail.setColumnWidth(12, 18*255);
//                                                sheetCusDetail.setColumnWidth(13, 24*255);
//                                                sheetCusDetail.setColumnWidth(14, 30*255);
//                                                sheetCusDetail.setColumnWidth(15, 32*255);
                                                Row rowCusDetail = sheetCusDetail.createRow(0);
                                                Cell celCus = rowCusDetail.createCell((short) 0);
                                                celCus.setCellValue(sCellSTT);
                                                celCus.setCellStyle(my_style);
                                                Cell celCus1 = rowCusDetail.createCell((short) 1);
                                                celCus1.setCellValue(sCellMounth);
                                                celCus1.setCellStyle(my_style);
                                                Cell celCus2 = rowCusDetail.createCell((short) 2);
                                                celCus2.setCellValue(sCellAgency);
                                                celCus2.setCellStyle(my_style);
                                                Cell celCus4 = rowCusDetail.createCell((short) 3);
                                                celCus4.setCellValue(sCellMST);
                                                celCus4.setCellStyle(my_style);
                                                Cell celCus5 = rowCusDetail.createCell((short) 4);
                                                celCus5.setCellValue(sCellCompany);
                                                celCus5.setCellStyle(my_style);
                                                Cell celCus9 = rowCusDetail.createCell((short) 5);
                                                celCus9.setCellValue(sCellProfile);
                                                celCus9.setCellStyle(my_style);
                                                Cell celFeeAmount = rowCusDetail.createCell((short) 6);
                                                celFeeAmount.setCellValue(sCellProfileFee);
                                                celFeeAmount.setCellStyle(my_style);
                                                Cell celTokenNum = rowCusDetail.createCell((short) 7);
                                                celTokenNum.setCellValue(sCellTokenQuality);
                                                celTokenNum.setCellStyle(my_style);
                                                Cell celTokenAmount = rowCusDetail.createCell((short) 8);
                                                celTokenAmount.setCellValue("Phí token");
                                                celTokenAmount.setCellStyle(my_style);
                                                Cell celRoseAmount = rowCusDetail.createCell((short) 9);
                                                celRoseAmount.setCellValue("Thành tiền hoa hồng");
                                                celRoseAmount.setCellStyle(my_style);
                                                Cell celSumAmount = rowCusDetail.createCell((short) 10);
                                                celSumAmount.setCellValue("Tiền DV phải trả NCC");
                                                celSumAmount.setCellStyle(my_style);
                                                Cell celDateGen = rowCusDetail.createCell((short) 11);
                                                celDateGen.setCellValue(sCellGenDate);
                                                celDateGen.setCellStyle(my_style);
                                                Cell celCertSN = rowCusDetail.createCell((short) 12);
                                                celCertSN.setCellValue("Số sê-ri CTS");
                                                celCertSN.setCellStyle(my_style);
                                                Cell celCreateUser = rowCusDetail.createCell((short) 13);
                                                celCreateUser.setCellValue(sCellUser);
                                                celCreateUser.setCellStyle(my_style);
                                                Cell celRequestType = rowCusDetail.createCell((short) 14);
                                                celRequestType.setCellValue(sCellRequestType);
                                                celRequestType.setCellStyle(my_style);
                                                Cell celCus6 = rowCusDetail.createCell((short) 15);
                                                celCus6.setCellValue(sCellCMND);
                                                celCus6.setCellStyle(my_style);
                                                Cell celCus7 = rowCusDetail.createCell((short) 16);
                                                celCus7.setCellValue(sCellPersonal);
                                                celCus7.setCellStyle(my_style);
                                                Cell celTokenSN = rowCusDetail.createCell((short) 17);
                                                celTokenSN.setCellValue(sTokenSN);
                                                celTokenSN.setCellStyle(my_style);
                                                Cell celRevokeDate = rowCusDetail.createCell((short) 18);
                                                celRevokeDate.setCellValue(sCellRevokeDate);
                                                celRevokeDate.setCellStyle(my_style);
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

                                                    Cell cellBranch1 = rowDecline.createCell((short) 2);
                                                    cellBranch1.setCellValue(temp1.BRANCH_NAME);
                                                    cellBranch1.setCellStyle(my_styleDecBranch);

                                                    String sTAX_CODE = temp1.TAX_CODE;
                                                    Cell cellBranch4 = rowDecline.createCell((short) 3);
                                                    cellBranch4.setCellValue(sTAX_CODE);
                                                    cellBranch4.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch5 = rowDecline.createCell((short) 4);
                                                    cellBranch5.setCellValue(temp1.COMPANY_NAME);
                                                    cellBranch5.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch9 = rowDecline.createCell((short) 5);
                                                    cellBranch9.setCellValue(temp1.CERTIFICATION_PROFILE_NAME);
                                                    cellBranch9.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch10 = rowDecline.createCell((short) 6);
                                                    cellBranch10.setCellValue(clsCom.convertMoneyAnotherZero(temp1.FEE_AMOUNT));
                                                    cellBranch10.setCellStyle(my_styleDecBranch);

                                                    Cell cellDec11 = rowDecline.createCell((short) 7);
                                                    cellDec11.setCellValue(clsCom.convertMoneyAnotherZero(temp1.TOKEN_NUMBER));
                                                    cellDec11.setCellStyle(my_styleDecBranch);

                                                    Cell cellDec12 = rowDecline.createCell((short) 8);
                                                    cellDec12.setCellValue(clsCom.convertMoneyAnotherZero(temp1.TOKEN_AMOUNT));
                                                    cellDec12.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellRoseAmount = rowDecline.createCell((short) 9);
                                                    cellRoseAmount.setCellValue(clsCom.convertMoneyAnotherZero(temp1.ROSE_AMOUNT));
                                                    cellRoseAmount.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellSumAmount = rowDecline.createCell((short) 10);
                                                    cellSumAmount.setCellValue(clsCom.convertMoneyAnotherZero(temp1.RETURN_AMOUNT));
                                                    cellSumAmount.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellBranch15 = rowDecline.createCell((short) 11);
                                                    Date dateIssue = CommonFunction.convertStringToDate(temp1.ISSUED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateIssue != null) {
                                                        cellBranch15.setCellValue(dateIssue);
                                                    } else {
                                                        cellBranch15.setCellValue("");
                                                    }
                                                    cellBranch15.setCellStyle(my_styleBranchDate);
                                                    
                                                    String sCERTIFICATION_SN = temp1.CERTIFICATION_SN;
                                                    Cell cellBranch19 = rowDecline.createCell((short) 12);
                                                    cellBranch19.setCellValue(sCERTIFICATION_SN);
                                                    cellBranch19.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch2 = rowDecline.createCell((short) 13);
                                                    cellBranch2.setCellValue(temp1.CREATED_BY);
                                                    cellBranch2.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch3 = rowDecline.createCell((short) 14);
                                                    cellBranch3.setCellValue(temp1.SERVICE_TYPE_DESC);
                                                    cellBranch3.setCellStyle(my_styleDecBranch);

                                                    String sP_ID = temp1.P_ID;
                                                    Cell cellBranch6 = rowDecline.createCell((short) 15);
                                                    cellBranch6.setCellValue(sP_ID);
                                                    cellBranch6.setCellStyle(my_styleDecBranch);

                                                    Cell cellBranch7 = rowDecline.createCell((short) 16);
                                                    cellBranch7.setCellValue(temp1.PERSONAL_NAME);
                                                    cellBranch7.setCellStyle(my_styleDecBranch);
                                                    
                                                    String valueTokenSN = temp1.TOKEN_SN;
                                                    if(CommonFunction.checkViewTokenValid(valueTokenSN) == false){valueTokenSN = "";}
                                                    Cell cellDec21 = rowDecline.createCell((short) 17);
                                                    cellDec21.setCellValue(valueTokenSN);
                                                    cellDec21.setCellStyle(my_styleDecBranch);
                                                    
                                                    Cell cellDec22 = rowDecline.createCell((short) 18);
                                                    Date dateRevoke = CommonFunction.convertStringToDate(temp1.REVOKED_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                                    if(dateRevoke != null) {
                                                        cellDec22.setCellValue(dateRevoke);
                                                    } else {
                                                        cellDec22.setCellValue("");
                                                    }
                                                    cellDec22.setCellStyle(my_styleBranchDate);
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
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_CERTIFICATE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                    String sCellCompany = "Company Name";
                                    String sCellMST = "TaxCode/ Budget Code/ Decision Number";
                                    String sCellPersonal = "Personal Name";
                                    String sCellCMND = "Personal ID/ Citizen ID/ Passport";
                                    String sCellDomain = "Domain Name";
                                    String sCellMethod = "Method";
                                    String sCellProfile = "Profile Code";
                                    String sCellCertType = "Certificate Type";
                                    String sCellStatus = "Status";
                                    String sCellRequest = "Request Type";
                                    String sCellCreateBy = "Create User";
                                    String sCellAgency = "Agency";
                                    String sCellCreateTime = "Create Date";
                                    String sCellRevokeTime = "Revoke Date";
                                    String sCellEffectiveTime = "Effective Date";
                                    String sCellTokenSN = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_TOKEN_CODE).trim();
                                    String sCellCertSN = "Certificate SN";
                                    
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
                                        HSSFFont fontBranch = wb.createFont();
                                        fontBranch.setFontName("Arial");
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
                                        String sMST_MNS = temp1.TAX_CODE;
                                        if("".equals(sMST_MNS)) {
                                            sMST_MNS = temp1.BUDGET_CODE;
                                        }
                                        if("".equals(sMST_MNS)) {
                                            sMST_MNS = temp1.DECISION;
                                        }
                                        Cell cellValue2 = row1.createCell((short) 2);
                                        cellValue2.setCellValue(sMST_MNS);
                                        cellValue2.setCellStyle(my_styleBranch);

                                        Cell cellValue3 = row1.createCell((short) 3);
                                        cellValue3.setCellValue(temp1.PERSONAL_NAME);
                                        cellValue3.setCellStyle(my_styleBranch);
                                        String sCMND_PASSPORT = temp1.P_ID;
                                        if("".equals(sCMND_PASSPORT)) {
                                            sCMND_PASSPORT = temp1.P_EID;
                                        }
                                        if("".equals(sCMND_PASSPORT)) {
                                            sCMND_PASSPORT = temp1.PASSPORT;
                                        }
                                        Cell cellValue4 = row1.createCell((short) 4);
                                        cellValue4.setCellValue(sCMND_PASSPORT);
                                        cellValue4.setCellStyle(my_styleBranch);
                                        
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
                                        cellValue101.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue102 = row1.createCell((short) 12);
                                        cellValue102.setCellValue(temp1.TOKEN_SN);
                                        cellValue102.setCellStyle(my_styleBranch);
                                        
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
                                    String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
                                    File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
                                    if (!directory.exists()){
                                        directory.mkdir();
                                    }
                                    String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
                                    String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_REPORT_CERTIFICATE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
                                    String strURLPath = pPathURL + sFileName;
                                    CommonFunction.LogDebugString(log, "NEACExportDetailList", "PathURL: " + strURLPath);
                                    String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                    String sCellCompany = "Company Name";
                                    String sCellMST = "TaxCode/ Budget Code/ Decision Number";
                                    String sCellPersonal = "Personal Name";
                                    String sCellCMND = "Personal ID/ Citizen ID/ Passport";
                                    String sCellDomain = "Domain Name";
                                    String sCellMethod = "Method";
                                    String sCellProfile = "Profile Code";
                                    String sCellCertType = "Certificate Type";
                                    String sCellStatus = "Status";
                                    String sCellRequest = "Request Type";
                                    String sCellCreateBy = "Create User";
                                    String sCellAgency = "Agency";
                                    String sCellCreateTime = "Create Date";
                                    String sCellEffectiveTime = "Effective Date";
                                    String sCellRevokeTime = "Revoke Date";
                                    String sCellTokenSN = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_TOKEN_CODE).trim();
                                    String sCellCertSN = "Certificate SN";
                                    
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
                                        HSSFFont fontBranch = wb.createFont();
                                        fontBranch.setFontName("Arial");
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
                                        String sMST_MNS = temp1.TAX_CODE;
                                        if("".equals(sMST_MNS)) {
                                            sMST_MNS = temp1.BUDGET_CODE;
                                        }
                                        if("".equals(sMST_MNS)) {
                                            sMST_MNS = temp1.DECISION;
                                        }
                                        Cell cellValue2 = row1.createCell((short) 2);
                                        cellValue2.setCellValue(sMST_MNS);
                                        cellValue2.setCellStyle(my_styleBranch);

                                        Cell cellValue3 = row1.createCell((short) 3);
                                        cellValue3.setCellValue(temp1.PERSONAL_NAME);
                                        cellValue3.setCellStyle(my_styleBranch);
                                        String sCMND_PASSPORT = temp1.P_ID;
                                        if("".equals(sCMND_PASSPORT)) {
                                            sCMND_PASSPORT = temp1.P_EID;
                                        }
                                        if("".equals(sCMND_PASSPORT)) {
                                            sCMND_PASSPORT = temp1.PASSPORT;
                                        }
                                        Cell cellValue4 = row1.createCell((short) 4);
                                        cellValue4.setCellValue(sCMND_PASSPORT);
                                        cellValue4.setCellStyle(my_styleBranch);
                                        
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
                                        cellValue101.setCellStyle(my_styleBranch);
                                        
                                        Cell cellValue102 = row1.createCell((short) 12);
                                        cellValue102.setCellValue(temp1.TOKEN_SN);
                                        cellValue102.setCellStyle(my_styleBranch);
                                        
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
                                                    String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                                    String sCellTokenSN = conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_TOKEN_SN).trim();
                                                    String sCellTokenSOPIN = conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_TOKEN_SOPIN).trim();
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
                                                            Font fontBranch = wb.createFont();

                                                            fontBranch.setFontName("Arial");
                                                            my_styleBranch.setFont(fontBranch);

                                                            Cell cellBranch = row1.createCell((short) 0);
                                                            cellBranch.setCellValue(k);
                                                            cellBranch.setCellStyle(my_styleBranch);

                                                            Cell cellBranch1 = row1.createCell((short) 1);
                                                            cellBranch1.setCellValue(EscapeUtils.CheckTextNull(rsPgin[0][0].TOKEN_SN));
                                                            cellBranch1.setCellType(Cell.CELL_TYPE_STRING);
                                                            cellBranch1.setCellStyle(my_styleBranch);
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
                                                    EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),"");
                                                if (sCount > 0) {
                                                    com.S_BO_TOKEN_IMPORT_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid),
                                                        EscapeUtils.escapeHtmlSearch(ToDateValid),
                                                        EscapeUtils.escapeHtmlSearch(FromTOKEN_ID),
                                                        EscapeUtils.escapeHtmlSearch(ToTOKEN_ID), EscapeUtils.escapeHtmlSearch(AGENT_ID),
                                                        loginLanguage, rsPgin, Definitions.CONFIG_GRID_INT_PAGNO, sCount, "");
                                                    if (rsPgin[0] != null && rsPgin[0].length > 0) {
                                                        String queryString = getServletContext().getRealPath("/");
                                                        String outputDirectory = queryString;
                                                        String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                                        FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                                        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                                        inputStream.close();
                                                        
                                                        String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                                        String sCellTokenSN = conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_TOKEN_SN).trim();
                                                        String sCellTokenSOPIN = conf.GetPropertybyCode(Definitions.CONFIG_COLUMN_EXCEL_TOKEN_SOPIN).trim();
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

                                                            Cell cellBranch = row1.createCell((short) 0);
                                                            cellBranch.setCellValue(k);
                                                            cellBranch.setCellStyle(my_styleBranch);

                                                            Cell cellBranch1 = row1.createCell((short) 1);
                                                            cellBranch1.setCellValue(EscapeUtils.CheckTextNull(temp1.TOKEN_SN));
                                                            cellBranch1.setCellType(Cell.CELL_TYPE_STRING);
                                                            cellBranch1.setCellStyle(my_styleBranch);
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
//                            case "exportprofilemanagerlist_bk": {
//                                //<editor-fold defaultstate="collapsed" desc="exportprofilemanagerlist_bk">
//                                Config conf = new Config();
//                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
//                                String SessRoleID_ID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
//                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
//                                String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
//                                String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
//                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
//                                if (!directory.exists()){
//                                    directory.mkdir();
//                                }
//                                String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
//                                String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_PROFILE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
//                                String strURLPath = pPathURL + sFileName;
//                                CommonFunction.LogDebugString(log, "ExportProfileManagerList", "PathURL: " + strURLPath);
//                                String vSum = EscapeUtils.CheckTextNull(request.getParameter("vSum"));
//                                String ToCreateDate = request.getSession(false).getAttribute("sessMonthProfileCert").toString().trim();
//                                String FromCreateDate = request.getSession(false).getAttribute("sessYearProfileCert").toString().trim();
//                                String PERSONAL_NAME = request.getSession(false).getAttribute("sessPERSONAL_NAMEProfileCert").toString().trim();
//                                String COMPANY_NAME = request.getSession(false).getAttribute("sessCOMPANY_NAMEProfileCert").toString().trim();
//                                String TAX_CODE = request.getSession(false).getAttribute("sessTAX_CODEProfileCert").toString().trim();
//                                String BUDGET_CODE = request.getSession(false).getAttribute("sessBUDGET_CODEProfileCert").toString().trim();
//                                String P_ID = request.getSession(false).getAttribute("sessP_IDProfileCert").toString().trim();
//                                String CCCD = request.getSession(false).getAttribute("sessCCCDProfileCert").toString().trim();
//                                String PASSPORT = request.getSession(false).getAttribute("sessPASSPORTProfileCert").toString().trim();
//                                String idCollectEnabled = request.getSession(false).getAttribute("sessCollectEnabledProfileCert").toString().trim();
//                                String BranchOffice = request.getSession(false).getAttribute("sessBranchOfficeProfileCert").toString().trim();
//                                String idCheckCompensation = request.getSession(false).getAttribute("sessCompensationProfileCert").toString().trim();
//                                String idCheckOverdue = request.getSession(false).getAttribute("sessOverdueProfileCert").toString().trim();
//                                String idCheckCommitEnabled = request.getSession(false).getAttribute("sessCommitEnabledCert").toString().trim();
//                                String stateProfile = request.getSession(false).getAttribute("sessStateProfileProfileCert").toString().trim();
//                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idCollectEnabled)) {
//                                    idCollectEnabled = "";
//                                }
//                                
//                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
//                                    BranchOffice = "";
//                                }
//                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(stateProfile)) {
//                                    stateProfile = "";
//                                }
//                                String SessUserID = "";
//                                CommonFunction.LogDebugString(log, "ExportReportCertList", "vSum: " + vSum
//                                    + "; Month: " + ToCreateDate + "; Year: " + FromCreateDate);
//                                CERTIFICATION[][] rsReportBranch = new CERTIFICATION[1][];
//                                if(!"".equals(vSum)) {
//                                    if(Integer.parseInt(vSum) > 0) {
//                                        com.S_BO_CERTIFICATION_BRIEF_LIST_EXPORT(COMPANY_NAME, TAX_CODE, BUDGET_CODE, PERSONAL_NAME, P_ID,
//                                            PASSPORT, idCollectEnabled, EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
//                                            BranchOffice, idCheckCompensation, idCheckOverdue, SessUserID, loginLanguage,
//                                            rsReportBranch, EscapeUtils.escapeHtmlSearch(CCCD),idCheckCommitEnabled, stateProfile);
//                                    }
//                                }
//                                if (rsReportBranch[0] != null && rsReportBranch[0].length > 0) {
//                                    String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
//                                    String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
//                                    String sCellUserCreate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_USER_CREATE).trim();
//                                    String sCellMST = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_MST).trim();
//                                    String sCellCompany = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COMPANY).trim();
//                                    String sCellCMND = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CMND).trim();
//                                    String sCellPesonal = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PESONAL).trim();
//                                    String sCellProfile = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PROFILE).trim();
//                                    String sCellRequestType = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_REQUEST_TYPE).trim();
//                                    String sCellMonthControl = "Tháng đối soát";
//                                    String sCellStateProfile = "Trạng thái hồ sơ";
//                                    String sCellRecordProfile = "Hình thức thu hồ sơ";
//                                    String sCellTypeProfile = "Loại hồ sơ";
//                                    String sCellAmountFine = "Số tiền phạt hồ sơ";
//                                    String sCellDateReceive = "Ngày nhận hồ sơ";
//                                    String sCellPhoneContract = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PHONE_CONTACT).trim();
//                                    String sCellEmailContract = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_EMAIL_CONTACT).trim();
//                                    String sCelNameContract = "Tên người liên hệ";
//                                    String sCelPhoneRepresent = "SĐT Người đại diện";
//                                    String sCelEmailRepresent = "Email Người đại diện";
//                                    String sCelNameRepresent = "Tên người đại diện";
//                                    String sCelCheckRegister = "Đăng ký sử dụng";
//                                    String sCelCheckConfirm = "Giấy xác nhận";
//                                    String sCelCheckGPKD = "Đăng ký kinh doanh";
//                                    String sCelCheckCMND = "CMND";
//                                    
//                                    String sCellDATE_GEN = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_COLLATION).trim();
//                                    HSSFWorkbook wb = new HSSFWorkbook();
//                                    HSSFSheet sheet = wb.createSheet();
//                                    sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
//                                    sheet.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
//                                    sheet.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
//                                    sheet.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
//                                    sheet.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
//                                    sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
//                                    sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
//                                    sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
//                                    sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
//                                    sheet.setColumnWidth(9, 18 * 255);
//                                    sheet.setColumnWidth(10, 18 * 255);
//                                    sheet.setColumnWidth(11, 18 * 255);
//                                    sheet.setColumnWidth(12, 18 * 255);
//                                    sheet.setColumnWidth(13, 18 * 255);
//                                    sheet.setColumnWidth(14, 18 * 255);
//                                    sheet.setColumnWidth(15, 18 * 255);
//                                    sheet.setColumnWidth(16, 18 * 255);
//                                    sheet.setColumnWidth(17, 18 * 255);
//                                    sheet.setColumnWidth(18, 18 * 255);
//                                    sheet.setColumnWidth(19, 18 * 255);
//                                    sheet.setColumnWidth(20, 18 * 255);
//                                    sheet.setColumnWidth(21, 18 * 255);
//                                    sheet.setColumnWidth(22, 18 * 255);
//                                    sheet.setColumnWidth(23, 18 * 255);
//                                    sheet.setColumnWidth(24, 18 * 255);
//                                    HSSFRow row = sheet.createRow(0);
//
//                                    HSSFCellStyle my_style = wb.createCellStyle();
//                                    HSSFFont font = wb.createFont();
//                                    font.setBoldweight((short) 700);
//                                    font.setFontName("Arial");
//                                    my_style.setFont(font);
//                                    my_style.setFillBackgroundColor((short) 9);
//                                    my_style.setAlignment((short) 2);
//                                    Cell cell = row.createCell((short) 0);
//                                    cell.setCellValue(sCellSTT);
//                                    cell.setCellStyle(my_style);
//
//                                    Cell cell1 = row.createCell((short) 1);
//                                    cell1.setCellValue(sCellAgency);
//                                    cell1.setCellStyle(my_style);
//
////                                        row.createCell((short) 2).setCellValue(sCellUserCreate);
//                                    Cell cell2 = row.createCell((short) 2);
//                                    cell2.setCellValue(sCellUserCreate);
//                                    cell2.setCellStyle(my_style);
//
//                                    Cell cell3 = row.createCell((short) 3);
//                                    cell3.setCellValue(sCellMST);
//                                    cell3.setCellStyle(my_style);
//
//                                    Cell cell5 = row.createCell((short) 4);
//                                    cell5.setCellValue(sCellCompany);
//                                    cell5.setCellStyle(my_style);
//
//                                    Cell cell6 = row.createCell((short) 5);
//                                    cell6.setCellValue(sCellCMND);
//                                    cell6.setCellStyle(my_style);
//
//                                    Cell cell7 = row.createCell((short) 6);
//                                    cell7.setCellValue(sCellPesonal);
//                                    cell7.setCellStyle(my_style);
//
//                                    Cell cell9 = row.createCell((short) 7);
//                                    cell9.setCellValue(sCellProfile);
//                                    cell9.setCellStyle(my_style);
//
//                                    Cell cell10 = row.createCell((short) 8);
//                                    cell10.setCellValue(sCellRequestType);
//                                    cell10.setCellStyle(my_style);
//
//                                    Cell cell11 = row.createCell((short) 9);
//                                    cell11.setCellValue(sCellMonthControl);
//                                    cell11.setCellStyle(my_style);
//
//                                    Cell cell12 = row.createCell((short) 10);
//                                    cell12.setCellValue(sCellStateProfile);
//                                    cell12.setCellStyle(my_style);
//
//                                    Cell cell13 = row.createCell((short) 11);
//                                    cell13.setCellValue(sCellRecordProfile);
//                                    cell13.setCellStyle(my_style);
//
//                                    Cell cell14 = row.createCell((short) 12);
//                                    cell14.setCellValue(sCellTypeProfile);
//                                    cell14.setCellStyle(my_style);
//
//                                    Cell cell15 = row.createCell((short) 13);
//                                    cell15.setCellValue(sCellAmountFine);
//                                    cell15.setCellStyle(my_style);
//
//                                    Cell cell16 = row.createCell((short) 14);
//                                    cell16.setCellValue(sCellDateReceive);
//                                    cell16.setCellStyle(my_style);
//
//                                    Cell cell17 = row.createCell((short) 15);
//                                    cell17.setCellValue(sCellPhoneContract);
//                                    cell17.setCellStyle(my_style);
//
//                                    Cell cell18 = row.createCell((short) 16);
//                                    cell18.setCellValue(sCellEmailContract);
//                                    cell18.setCellStyle(my_style);
//
//                                    Cell cell19 = row.createCell((short) 17);
//                                    cell19.setCellValue(sCelNameContract);
//                                    cell19.setCellStyle(my_style);
//
//                                    Cell cell20 = row.createCell((short) 18);
//                                    cell20.setCellValue(sCelPhoneRepresent);
//                                    cell20.setCellStyle(my_style);
//
//                                    Cell cell21 = row.createCell((short) 19);
//                                    cell21.setCellValue(sCelEmailRepresent);
//                                    cell21.setCellStyle(my_style);
//
//                                    Cell cell22 = row.createCell((short) 20);
//                                    cell22.setCellValue(sCelNameRepresent);
//                                    cell22.setCellStyle(my_style);
//
//                                    Cell cell23 = row.createCell((short) 21);
//                                    cell23.setCellValue(sCelCheckRegister);
//                                    cell23.setCellStyle(my_style);
//
//                                    Cell cell24 = row.createCell((short) 22);
//                                    cell24.setCellValue(sCelCheckConfirm);
//                                    cell24.setCellStyle(my_style);
//
//                                    Cell cell25 = row.createCell((short) 23);
//                                    cell25.setCellValue(sCelCheckGPKD);
//                                    cell25.setCellStyle(my_style);
//
//                                    Cell cell26 = row.createCell((short) 24);
//                                    cell26.setCellValue(sCelCheckCMND);
//                                    cell26.setCellStyle(my_style);
//
//                                    int i = 1;
//                                    int k = 0;
//                                    for (CERTIFICATION rsReportBranch1 : rsReportBranch[0]) {
//                                        if (k == 0) {
//                                            k = 1;
//                                        } else {
//                                            k++;
//                                        }
//                                        HSSFRow row1 = sheet.createRow(Integer.valueOf(i).intValue());
//                                        HSSFCellStyle my_styleBranch = wb.createCellStyle();
//                                        HSSFFont fontBranch = wb.createFont();
//
//                                        fontBranch.setFontName("Arial");
////                                            fontBranch.setColor((short) 12);
//                                        my_styleBranch.setFont(fontBranch);
////                                            my_styleBranch.setFillBackgroundColor((short) 9);
//
//                                        Cell cellBranch = row1.createCell((short) 0);
//                                        cellBranch.setCellValue(k);
//                                        cellBranch.setCellStyle(my_styleBranch);
//
//                                        Cell cellBranch1 = row1.createCell((short) 1);
//                                        cellBranch1.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.BRANCH_DESC));
//                                        cellBranch1.setCellStyle(my_styleBranch);
//
//                                        Cell cellBranch2 = row1.createCell((short) 2);
//                                        cellBranch2.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CREATED_BY));
//                                        cellBranch2.setCellStyle(my_styleBranch);
//
//                                        String sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.TAX_CODE);
//                                        if ("".equals(sMSTAndBUDGET_CODE)) {
//                                            sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.BUDGET_CODE);
//                                        }
//                                        Cell cellBranch3 = row1.createCell((short) 3);
//                                        cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
//                                        cellBranch3.setCellStyle(my_styleBranch);
//
//                                        Cell cellBranch5 = row1.createCell((short) 4);
//                                        cellBranch5.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.COMPANY_NAME));
//                                        cellBranch5.setCellStyle(my_styleBranch);
//
//                                        String sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.P_ID);
//                                        if ("".equals(sP_IDAndPASSPORT)) {
//                                            sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.P_EID);
//                                        }
//                                        if ("".equals(sP_IDAndPASSPORT)) {
//                                            sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.PASSPORT);
//                                        }
//                                        Cell cellBranch6 = row1.createCell((short) 5);
//                                        cellBranch6.setCellValue(sP_IDAndPASSPORT);
//                                        cellBranch6.setCellStyle(my_styleBranch);
//
//                                        Cell cellBranch7 = row1.createCell((short) 6);
//                                        cellBranch7.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PERSONAL_NAME));
//                                        cellBranch7.setCellStyle(my_styleBranch);
//
//                                        Cell cellBranch9 = row1.createCell((short) 7);
//                                        cellBranch9.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_PROFILE_NAME));
//                                        cellBranch9.setCellStyle(my_styleBranch);
//
//                                        Cell cellBranch10 = row1.createCell((short) 8);
//                                        cellBranch10.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_ATTR_TYPE_DESC));
//                                        cellBranch10.setCellStyle(my_styleBranch);
//
//                                        Cell cellBranch11 = row1.createCell((short) 9);
//                                        cellBranch11.setCellValue(ToCreateDate);
//                                        cellBranch11.setCellStyle(my_styleBranch);
//
//                                        String sStateProfile = "Thiếu hồ sơ";
//                                        if(rsReportBranch1.COLLECT_ENABLED == true) {
//                                            sStateProfile = "Đủ hồ sơ";
//                                        }
//                                        Cell cellBranch12 = row1.createCell((short) 10);
//                                        cellBranch12.setCellValue(sStateProfile);
//                                        cellBranch12.setCellStyle(my_styleBranch);
//
//                                        String sRecordProfile = "Hồ sơ trong tháng";
//                                        if(rsReportBranch1.BRIEF_TYPE == true) {
//                                            sRecordProfile = "Hồ sơ quá hạn";
//                                        }
//                                        
//                                        Cell cellBranch13 = row1.createCell((short) 11);
//                                        cellBranch13.setCellValue(sRecordProfile);
//                                        cellBranch13.setCellStyle(my_styleBranch);
//                                        
//                                        String sTypeProfile = "Bản cứng";
//                                        if(rsReportBranch1.COLLECT_SOFTCOPY == true) {
//                                            sTypeProfile = "Bản mềm";
//                                        }
//                                        
//                                        Cell cellBranch14123 = row1.createCell((short) 12);
//                                        cellBranch14123.setCellValue(sTypeProfile);
//                                        cellBranch14123.setCellStyle(my_styleBranch);
//                                        
//                                        Cell cellBranch15222 = row1.createCell((short) 13);
//                                        cellBranch15222.setCellValue(rsReportBranch1.FINE_FOR_LACK_OF_BRIEF);
//                                        cellBranch15222.setCellStyle(my_styleBranch);
//                                        
//                                        Cell cellBranch14 = row1.createCell((short) 14);
//                                        cellBranch14.setCellValue(rsReportBranch1.RECEIVED_BRIEF_DATE);
//                                        cellBranch14.setCellStyle(my_styleBranch);
//                                        
//                                        Cell cellBranch15 = row1.createCell((short) 15);
//                                        cellBranch15.setCellValue(rsReportBranch1.PHONE_CONTRACT);
//                                        cellBranch15.setCellStyle(my_styleBranch);
//                                        
//                                        Cell cellBranch16 = row1.createCell((short) 16);
//                                        cellBranch16.setCellValue(rsReportBranch1.EMAIL_CONTRACT);
//                                        cellBranch16.setCellStyle(my_styleBranch);
//                                        
//                                        Cell cellBranch17 = row1.createCell((short) 17);
//                                        cellBranch17.setCellValue(rsReportBranch1.CONTACT_NAME);
//                                        cellBranch17.setCellStyle(my_styleBranch);
//                                        
//                                        Cell cellBranch18 = row1.createCell((short) 18);
//                                        cellBranch18.setCellValue(rsReportBranch1.REPRESENTATIVE_PHONE);
//                                        cellBranch18.setCellStyle(my_styleBranch);
//                                        
//                                        Cell cellBranch19 = row1.createCell((short) 19);
//                                        cellBranch19.setCellValue(rsReportBranch1.REPRESENTATIVE_EMAIL);
//                                        cellBranch19.setCellStyle(my_styleBranch);
//                                        
//                                        Cell cellBranch20 = row1.createCell((short) 20);
//                                        cellBranch20.setCellValue(rsReportBranch1.REPRESENTATIVE_NAME);
//                                        cellBranch20.setCellStyle(my_styleBranch);
//                                        
//                                        String sRegister = "";
//                                        String sConfirm = "";
//                                        String sDKKD = "";
//                                        String sCMND = "";
//                                        String sBRIEF_PROPERTIES = rsReportBranch1.BRIEF_PROPERTIES;
//                                        if(!"".equals(sBRIEF_PROPERTIES))
//                                        {
//                                            CERTIFICATION_POLICY_DATA[][] resIPData = new CERTIFICATION_POLICY_DATA[1][];
//                                            CommonFunction.getCollectedBriefProperties(sBRIEF_PROPERTIES, resIPData);
//                                            if(resIPData[0].length > 0) {
//                                                boolean bRegister = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_SERVICE_REGISTRATION_DOCUMENT, resIPData);
//                                                if(bRegister == true){sRegister = "x";}
//                                                boolean bConfirm = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_MINUTES_OF_HANDOVER, resIPData);
//                                                if(bConfirm == true){sConfirm = "x";}
//                                                boolean bDKKD = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_PHOTO_ACTIVITY_DECLARATION, resIPData);
//                                                if(bDKKD == true){sDKKD = "x";}
//                                                boolean bCMND = CommonFunction.checkBriefFileType(Definitions.CONFIG_FILE_PROFILE_PHOTO_ID_CARD, resIPData);
//                                                if(bCMND == true){sCMND = "x";}
//                                            }
//                                        }
//                                        Cell cellBranch21 = row1.createCell((short) 21);
//                                        cellBranch21.setCellValue(sRegister);
//                                        cellBranch21.setCellStyle(my_styleBranch);
//                                        
//                                        Cell cellBranch22 = row1.createCell((short) 22);
//                                        cellBranch22.setCellValue(sConfirm);
//                                        cellBranch22.setCellStyle(my_styleBranch);
//                                        
//                                        Cell cellBranch23 = row1.createCell((short) 23);
//                                        cellBranch23.setCellValue(sDKKD);
//                                        cellBranch23.setCellStyle(my_styleBranch);
//                                        
//                                        Cell cellBranch24 = row1.createCell((short) 24);
//                                        cellBranch24.setCellValue(sCMND);
//                                        cellBranch24.setCellStyle(my_styleBranch);
//                                        i++;
//                                    }
//                                    ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
//                                    wb.write(outByteStream);
//                                    byte[] outArray = outByteStream.toByteArray();
//                                    File someFile = new File(strURLPath);
//                                    FileOutputStream fos = new FileOutputStream(someFile);
//                                    fos.write(outArray);
//                                    fos.flush();
//                                    strView = "0#" + strURLPath + "#" + sFileName;
//                                } else {
//                                    strView = "2#1";
//                                }
//                                //</editor-fold>
//                                break;
//                            }
                            case "exportprofilemanagerlist": {
                                //<editor-fold defaultstate="collapsed" desc="exportprofilemanagerlist">
                                Config conf = new Config();
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                String SessRoleID_ID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
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
                                String strAlertAllTimes = (String) request.getSession(false).getAttribute("AlertAllTimeSRenewCert");
                                CommonFunction clFunction = new CommonFunction();
                                if("1".equals(strAlertAllTimes)) {
                                    ToReceiveDate = "";
                                    FromReceiveDate = "";
                                } else {
//                                    ToReceiveDate = clFunction.formatDateTime(ToReceiveDate, "/", "-");
//                                    FromReceiveDate = clFunction.formatDateTime(FromReceiveDate, "/", "-");
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
                                String SessUserID = "";
                                CommonFunction.LogDebugString(log, "ExportReportCertList", "vSum: " + vSum
                                    + "; Month: " + ToCreateDate + "; Year: " + FromCreateDate);
                                CERTIFICATION[][] rsReportBranch = new CERTIFICATION[1][];
                                if(!"".equals(vSum)) {
                                    if(Integer.parseInt(vSum) > 0) {
                                        if("0".equals(ToCreateDate)){ToCreateDate = "";}
                                        com.S_BO_CERTIFICATION_BRIEF_LIST_EXPORT(COMPANY_NAME, PERSONAL_NAME, idCollectEnabled, EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            BranchOffice, idCheckCompensation, idCheckOverdue, SessUserID, loginLanguage,
                                            rsReportBranch, idCheckCommitEnabled, stateProfile,
                                            EscapeUtils.escapeHtmlSearch(FromReceiveDate), EscapeUtils.escapeHtmlSearch(ToReceiveDate), "","","","","","","");
                                    }
                                }
                                if (rsReportBranch[0] != null && rsReportBranch[0].length > 0) {
                                    String queryString = getServletContext().getRealPath("/");
                                    String outputDirectory = queryString;
                                    String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                    FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                    XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                    inputStream.close();
                                    
                                    String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                    String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
                                    String sCellUserCreate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_USER_CREATE).trim();
                                    String sCellMST = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_MST).trim();
                                    String sCellCompany = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COMPANY).trim();
                                    String sCellCMND = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CMND).trim();
                                    String sCellPesonal = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PESONAL).trim();
                                    String sCellProfile = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PROFILE).trim();
                                    String sCellRequestType = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_REQUEST_TYPE).trim();
                                    String sCellMonthControl = "Tháng đối soát";
                                    String sCellStateProfile = "Trạng thái hồ sơ";
                                    String sCellRecordProfile = "Hình thức thu hồ sơ";
                                    String sCellTypeProfile = "Loại hồ sơ";
                                    String sCellAmountFine = "Số tiền phạt hồ sơ";
                                    String sCellDateReceive = "Ngày nhận hồ sơ";
                                    String sCellPhoneContract = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PHONE_CONTACT).trim();
                                    String sCellEmailContract = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_EMAIL_CONTACT).trim();
                                    String sCellTokenSN = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_TOKEN_CODE).trim();
                                    String sCellCertSN = "Số sê-ri CTS";
                                    String sCelNameContract = "Tên người liên hệ";
                                    String sCelPhoneRepresent = "SĐT Người đại diện";
                                    String sCelEmailRepresent = "Email Người đại diện";
                                    String sCelNameRepresent = "Tên người đại diện";
                                    String sCelCheckRegister = "Đăng ký sử dụng";
                                    String sCelCheckConfirm = "Giấy xác nhận";
                                    String sCelCheckGPKD = "Đăng ký kinh doanh";
                                    String sCelCheckCMND = "CMND";
                                    
//                                    String sCellDATE_GEN = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_COLLATION).trim();
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
                                    sheet.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                    sheet.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                    sheet.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                    sheet.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                    sheet.setColumnWidth(9, 18 * 255);
                                    sheet.setColumnWidth(10, 18 * 255);
                                    sheet.setColumnWidth(11, 18 * 255);
                                    sheet.setColumnWidth(12, 18 * 255);
                                    sheet.setColumnWidth(13, 18 * 255);
                                    sheet.setColumnWidth(14, 24 * 255);
                                    sheet.setColumnWidth(15, 18 * 255);
                                    sheet.setColumnWidth(16, 18 * 255);
                                    sheet.setColumnWidth(17, 18 * 255);
                                    sheet.setColumnWidth(18, 18 * 255);
                                    sheet.setColumnWidth(19, 18 * 255);
                                    sheet.setColumnWidth(20, 18 * 255);
                                    sheet.setColumnWidth(21, 18 * 255);
                                    sheet.setColumnWidth(22, 18 * 255);
                                    sheet.setColumnWidth(23, 18 * 255);
                                    sheet.setColumnWidth(24, 18 * 255);
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

//                                        row.createCell((short) 2).setCellValue(sCellUserCreate);
                                    Cell cell2 = row.createCell((short) 2);
                                    cell2.setCellValue(sCellUserCreate);
                                    cell2.setCellStyle(my_style);

                                    Cell cell3 = row.createCell((short) 3);
                                    cell3.setCellValue(sCellMST);
                                    cell3.setCellStyle(my_style);

                                    Cell cell5 = row.createCell((short) 4);
                                    cell5.setCellValue(sCellCompany);
                                    cell5.setCellStyle(my_style);

                                    Cell cell6 = row.createCell((short) 5);
                                    cell6.setCellValue(sCellCMND);
                                    cell6.setCellStyle(my_style);

                                    Cell cell7 = row.createCell((short) 6);
                                    cell7.setCellValue(sCellPesonal);
                                    cell7.setCellStyle(my_style);

                                    Cell cell9 = row.createCell((short) 7);
                                    cell9.setCellValue(sCellProfile);
                                    cell9.setCellStyle(my_style);

                                    Cell cell10 = row.createCell((short) 8);
                                    cell10.setCellValue(sCellRequestType);
                                    cell10.setCellStyle(my_style);

                                    Cell cell11 = row.createCell((short) 9);
                                    cell11.setCellValue(sCellMonthControl);
                                    cell11.setCellStyle(my_style);

                                    Cell cell12 = row.createCell((short) 10);
                                    cell12.setCellValue(sCellStateProfile);
                                    cell12.setCellStyle(my_style);

                                    Cell cell13 = row.createCell((short) 11);
                                    cell13.setCellValue(sCellRecordProfile);
                                    cell13.setCellStyle(my_style);

                                    Cell cell14 = row.createCell((short) 12);
                                    cell14.setCellValue(sCellTypeProfile);
                                    cell14.setCellStyle(my_style);

                                    Cell cell15 = row.createCell((short) 13);
                                    cell15.setCellValue(sCellAmountFine);
                                    cell15.setCellStyle(my_style);

                                    Cell cell16 = row.createCell((short) 14);
                                    cell16.setCellValue(sCellDateReceive);
                                    cell16.setCellStyle(my_style);

                                    Cell cell1612 = row.createCell((short) 15);
                                    cell1612.setCellValue(sCellCertSN);
                                    cell1612.setCellStyle(my_style);

                                    Cell cell1613 = row.createCell((short) 16);
                                    cell1613.setCellValue(sCellTokenSN);
                                    cell1613.setCellStyle(my_style);

                                    Cell cell17 = row.createCell((short) 17);
                                    cell17.setCellValue(sCellPhoneContract);
                                    cell17.setCellStyle(my_style);

                                    Cell cell18 = row.createCell((short) 18);
                                    cell18.setCellValue(sCellEmailContract);
                                    cell18.setCellStyle(my_style);

                                    Cell cell19 = row.createCell((short) 19);
                                    cell19.setCellValue(sCelNameContract);
                                    cell19.setCellStyle(my_style);

                                    Cell cell20 = row.createCell((short) 20);
                                    cell20.setCellValue(sCelPhoneRepresent);
                                    cell20.setCellStyle(my_style);

                                    Cell cell21 = row.createCell((short) 21);
                                    cell21.setCellValue(sCelEmailRepresent);
                                    cell21.setCellStyle(my_style);

                                    Cell cell22 = row.createCell((short) 22);
                                    cell22.setCellValue(sCelNameRepresent);
                                    cell22.setCellStyle(my_style);

                                    Cell cell23 = row.createCell((short) 23);
                                    cell23.setCellValue(sCelCheckRegister);
                                    cell23.setCellStyle(my_style);

                                    Cell cell24 = row.createCell((short) 24);
                                    cell24.setCellValue(sCelCheckConfirm);
                                    cell24.setCellStyle(my_style);

                                    Cell cell25 = row.createCell((short) 25);
                                    cell25.setCellValue(sCelCheckGPKD);
                                    cell25.setCellStyle(my_style);

                                    Cell cell26 = row.createCell((short) 26);
                                    cell26.setCellValue(sCelCheckCMND);
                                    cell26.setCellStyle(my_style);

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
                                        Font fontBranch = wb.createFont();
                                        fontBranch.setFontName("Arial");
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

                                        String sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.TAX_CODE);
                                        if ("".equals(sMSTAndBUDGET_CODE)) {
                                            sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.BUDGET_CODE);
                                        }
                                        if ("".equals(sMSTAndBUDGET_CODE)) {
                                            sMSTAndBUDGET_CODE = EscapeUtils.CheckTextNull(rsReportBranch1.DECISION);
                                        }
                                        Cell cellBranch3 = row1.createCell((short) 3);
                                        cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                        cellBranch3.setCellStyle(my_styleBranch);

                                        Cell cellBranch5 = row1.createCell((short) 4);
                                        cellBranch5.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.COMPANY_NAME));
                                        cellBranch5.setCellStyle(my_styleBranch);

                                        String sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.P_ID);
                                        if ("".equals(sP_IDAndPASSPORT)) {
                                            sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.P_EID);
                                        }
                                        if ("".equals(sP_IDAndPASSPORT)) {
                                            sP_IDAndPASSPORT = EscapeUtils.CheckTextNull(rsReportBranch1.PASSPORT);
                                        }
                                        Cell cellBranch6 = row1.createCell((short) 5);
                                        cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                        cellBranch6.setCellStyle(my_styleBranch);

                                        Cell cellBranch7 = row1.createCell((short) 6);
                                        cellBranch7.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.PERSONAL_NAME));
                                        cellBranch7.setCellStyle(my_styleBranch);

                                        Cell cellBranch9 = row1.createCell((short) 7);
                                        cellBranch9.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_PROFILE_NAME));
                                        cellBranch9.setCellStyle(my_styleBranch);

                                        Cell cellBranch10 = row1.createCell((short) 8);
                                        cellBranch10.setCellValue(EscapeUtils.CheckTextNull(rsReportBranch1.CERTIFICATION_ATTR_TYPE_DESC));
                                        cellBranch10.setCellStyle(my_styleBranch);

                                        Cell cellBranch11 = row1.createCell((short) 9);
                                        cellBranch11.setCellValue(rsReportBranch1.CROSS_CHECKED_MOUNTH);
                                        cellBranch11.setCellStyle(my_styleBranch);

                                        String sStateProfile = "Thiếu hồ sơ";
                                        if(rsReportBranch1.COLLECT_ENABLED == true) {
                                            sStateProfile = "Đủ hồ sơ";
                                        }
                                        Cell cellBranch12 = row1.createCell((short) 10);
                                        cellBranch12.setCellValue(sStateProfile);
                                        cellBranch12.setCellStyle(my_styleBranch);

                                        String sRecordProfile = "Hồ sơ trong tháng";
                                        if(rsReportBranch1.BRIEF_TYPE == true) {
                                            sRecordProfile = "Hồ sơ quá hạn";
                                        }
                                        
                                        Cell cellBranch13 = row1.createCell((short) 11);
                                        cellBranch13.setCellValue(sRecordProfile);
                                        cellBranch13.setCellStyle(my_styleBranch);
                                        
                                        String sTypeProfile = "Bản cứng";
                                        if(rsReportBranch1.COLLECT_SOFTCOPY == true) {
                                            sTypeProfile = "Bản mềm";
                                        }
                                        
                                        Cell cellBranch14123 = row1.createCell((short) 12);
                                        cellBranch14123.setCellValue(sTypeProfile);
                                        cellBranch14123.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch15222 = row1.createCell((short) 13);
                                        cellBranch15222.setCellValue(rsReportBranch1.FINE_FOR_LACK_OF_BRIEF);
                                        cellBranch15222.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch14 = row1.createCell((short) 14);
                                        Date dateReceive = CommonFunction.convertStringToDate(rsReportBranch1.RECEIVED_BRIEF_DATE, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYY);
                                        if(dateReceive != null) {
                                            cellBranch14.setCellValue(dateReceive);
                                        } else {
                                            cellBranch14.setCellValue("");
                                        }
                                        cellBranch14.setCellStyle(my_styleBranchDate);
                                        
                                        Cell cellBranch191 = row1.createCell((short) 15);
                                        cellBranch191.setCellValue(rsReportBranch1.CERTIFICATION_SN);
                                        cellBranch191.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch192 = row1.createCell((short) 16);
                                        cellBranch192.setCellValue(rsReportBranch1.TOKEN_SN);
                                        cellBranch192.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch15 = row1.createCell((short) 17);
                                        cellBranch15.setCellValue(rsReportBranch1.PHONE_CONTRACT);
                                        cellBranch15.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch16 = row1.createCell((short) 18);
                                        cellBranch16.setCellValue(rsReportBranch1.EMAIL_CONTRACT);
                                        cellBranch16.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch17 = row1.createCell((short) 19);
                                        cellBranch17.setCellValue(rsReportBranch1.CONTACT_NAME);
                                        cellBranch17.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch18 = row1.createCell((short) 20);
                                        cellBranch18.setCellValue(rsReportBranch1.REPRESENTATIVE_PHONE);
                                        cellBranch18.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch19 = row1.createCell((short) 21);
                                        cellBranch19.setCellValue(rsReportBranch1.REPRESENTATIVE_EMAIL);
                                        cellBranch19.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch20 = row1.createCell((short) 22);
                                        cellBranch20.setCellValue(rsReportBranch1.REPRESENTATIVE_NAME);
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
                                        Cell cellBranch21 = row1.createCell((short) 23);
                                        cellBranch21.setCellValue(sRegister);
                                        cellBranch21.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch22 = row1.createCell((short) 24);
                                        cellBranch22.setCellValue(sConfirm);
                                        cellBranch22.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch23 = row1.createCell((short) 25);
                                        cellBranch23.setCellValue(sDKKD);
                                        cellBranch23.setCellStyle(my_styleBranch);
                                        
                                        Cell cellBranch24 = row1.createCell((short) 26);
                                        cellBranch24.setCellValue(sCMND);
                                        cellBranch24.setCellStyle(my_styleBranch);
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
//                            case "exporttokenlist_bk": {
//                                //<editor-fold defaultstate="collapsed" desc="exporttokenlist_bk">
//                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
//                                String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
//                                String SessRoleID_ID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
//                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
//                                String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
//                                Config conf = new Config();
//                                String pPathURL = conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER);
//                                File directory = new File(conf.GetPropertybyCode(Definitions.CONFIG_FILE_EXPORT_REQUEST_FOLDER));
//                                if (!directory.exists()) {
//                                    directory.mkdir();
//                                }
//                                String strUsernameExport = request.getSession(false).getAttribute("sUserID").toString().trim();
//                                String sFileName = strUsernameExport + Definitions.CONFIG_EXPORT_FILENAME_TAG_CERTIFICATE_LIST + CommonFunction.getDateFormat() + Definitions.CONFIG_EXPORT_FILENAME_EXTENTION_CSR;
//                                String strURLPath = pPathURL + sFileName;
//                                String strAlertAllTimes = (String) request.getSession(false).getAttribute("AlertAllTimeSUserlist");
//                                String FromDateValid = (String) request.getSession(false).getAttribute("FromCreateDateSTokenlist");
//                                String ToDateValid = (String) request.getSession(false).getAttribute("ToCreateDateSTokenlist");
//                                String TOKEN_ID = (String) request.getSession(false).getAttribute("sessTOKEN_ID");
//                                String TOKEN_STATE = (String) request.getSession(false).getAttribute("sessTOKEN_STATE");
//                                String TOKEN_VERSION = (String) request.getSession(false).getAttribute("sessTOKEN_VERSION");
//                                String AGENT_ID = (String) request.getSession(false).getAttribute("sessAGENT_ID");
//                                String TAX_CODE = (String) request.getSession(false).getAttribute("sessTAX_CODESTokenlist");
//                                String BUDGET_CODE = (String) request.getSession(false).getAttribute("sessBUDGET_CODESTokenlist");
//                                String P_ID = (String) request.getSession(false).getAttribute("sessP_IDSTokenlist");
//                                String CCCD = (String) request.getSession(false).getAttribute("sessCCCDSTokenlist");
//                                String PASSPORT = (String) request.getSession(false).getAttribute("sessPASSPORTSTokenlist");
//                                String TOKEN_SIGNED = (String) request.getSession(false).getAttribute("sessTOKEN_SIGNED");
//                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_STATE)) {
//                                    TOKEN_STATE = "";
//                                }
//                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_VERSION)) {
//                                    TOKEN_VERSION = "";
//                                }
//                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(TOKEN_SIGNED)) {
//                                    TOKEN_SIGNED = "";
//                                }
//                                if (!Definitions.CONFIG_AGENT_ROOT.equals(SessAgentID)) {
//                                    AGENT_ID = SessUserAgentID;
//                                } else {
//                                    if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(AGENT_ID)) {
//                                        AGENT_ID = "";
//                                    }
//                                }
//                                if("1".equals(strAlertAllTimes)) {
//                                    FromDateValid = "";
//                                    ToDateValid = "";
//                                }
//                                CommonFunction.LogDebugString(log, "ExportTokenList", "FromDate: " + FromDateValid + "; ToDate: " + ToDateValid + "; AGENCY: " + AGENT_ID);
//                                TOKEN[][] rsPgin = new TOKEN[1][];
//                                if (!"".equals(countList)) {
//                                    com.S_BO_TOKEN_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
//                                        EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(TOKEN_STATE),
//                                        EscapeUtils.escapeHtmlSearch(TOKEN_VERSION), EscapeUtils.escapeHtmlSearch(AGENT_ID), loginLanguage,
//                                        rsPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO, Integer.parseInt(countList),
//                                        EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
//                                        EscapeUtils.escapeHtmlSearch(P_ID), EscapeUtils.escapeHtmlSearch(PASSPORT), EscapeUtils.escapeHtmlSearch(CCCD),
//                                        EscapeUtils.escapeHtmlSearch(TOKEN_SIGNED));
//                                    if(rsPgin[0].length > 0)
//                                    {
//                                        /*String queryString = getServletContext().getRealPath("/");
//                                        String outputDirectory = queryString;
//                                        String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
//                                        FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
//                                        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
//                                        inputStream.close();*/
//
//                                        String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
//                                        String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
//                                        String sCellSTATE = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_STATE).trim();
//                                        String sCellDateCreate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_CREATE).trim();
//                                        String sCellToken = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_TOKEN_CODE).trim();
//                                        /*SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
//                                        wb.setCompressTempFiles(true);
//                                        SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
//                                        sheet.setRandomAccessWindowSize(100);*/
//                                        HSSFWorkbook wb = new HSSFWorkbook();
//                                        HSSFSheet sheet = wb.createSheet();
//                                        sheet.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
//                                        sheet.setColumnWidth(1, 24*255);
//                                        sheet.setColumnWidth(2, 24*255);
//                                        sheet.setColumnWidth(3, 24*255);
//                                        sheet.setColumnWidth(4, 24*255);
//                                        
//                                        HSSFRow row = sheet.createRow(0);
//                                        HSSFCellStyle my_style = wb.createCellStyle();
//                                        HSSFFont font = wb.createFont();
//                                        font.setBoldweight((short) 700);
//                                        font.setFontName("Arial");
//                                        my_style.setFont(font);
//                                        my_style.setFillBackgroundColor((short) 9);
//                                        my_style.setAlignment((short) 2);
//                                        Cell cell = row.createCell((short) 0);
//                                        cell.setCellValue(sCellSTT);
//                                        cell.setCellStyle(my_style);
//
//                                        Cell cell5 = row.createCell((short) 1);
//                                        cell5.setCellValue(sCellToken);
//                                        cell5.setCellStyle(my_style);
//
//                                        Cell cell3 = row.createCell((short) 2);
//                                        cell3.setCellValue(sCellSTATE);
//                                        cell3.setCellStyle(my_style);
//
//                                        Cell cell7 = row.createCell((short) 3);
//                                        cell7.setCellValue(sCellAgency);
//                                        cell7.setCellStyle(my_style);
//
//                                        Cell cell8 = row.createCell((short) 4);
//                                        cell8.setCellValue(sCellDateCreate);
//                                        cell8.setCellStyle(my_style);
//
//                                        int i = 1;
//                                        int k = 0;
//                                        for (TOKEN temp1 : rsPgin[0]) {
//                                            if (k == 0) {
//                                                k = 1;
//                                            } else {
//                                                k++;
//                                            }
//                                            HSSFRow row1 = sheet.createRow(Integer.valueOf(i));
//                                            HSSFCellStyle my_styleBranch = wb.createCellStyle();
//                                            HSSFFont fontBranch = wb.createFont();
//
//                                            fontBranch.setFontName("Arial");
//                                            my_styleBranch.setFont(fontBranch);
//
//                                            Cell cellBranch = row1.createCell((short) 0);
//                                            cellBranch.setCellValue(k);
//                                            cellBranch.setCellStyle(my_styleBranch);
//
//                                            Cell cellBranch1 = row1.createCell((short) 1);
//                                            cellBranch1.setCellValue(EscapeUtils.CheckTextNull(temp1.TOKEN_SN));
//                                            cellBranch1.setCellStyle(my_styleBranch);
//
//                                            Cell cellBranch2 = row1.createCell((short) 2);
//                                            cellBranch2.setCellValue(EscapeUtils.CheckTextNull(temp1.TOKEN_STATE_DESC));
//                                            cellBranch2.setCellStyle(my_styleBranch);
//
//                                            Cell cellBranch3 = row1.createCell((short) 3);
//                                            cellBranch3.setCellValue(EscapeUtils.CheckTextNull(temp1.BRANCH_DESC));
//                                            cellBranch3.setCellStyle(my_styleBranch);
//
////                                            String sDateEx = EscapeUtils.CheckTextNull(temp1.EXPORT_DATE_TYPE);
//                                            /*SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss");
//                                            Date dDateEx = new Date();
//                                            if(!"".equals(sDateEx)) {
//                                                dDateEx = formatter.parse(sDateEx);
//                                            }*/
////                                            java.sql.Timestamp timestamp = temp1.EXPORT_DATE_TYPE;
////                                            Calendar calendar = Calendar.getInstance();
////                                            calendar.setTimeInMillis(timestamp.getTime());
////                                            timestamp = new Timestamp(calendar.getTimeInMillis());
////                                            System.out.println("AFTER "+timestamp.toString());
//                                            
////                                            SimpleDateFormat datetemp = new SimpleDateFormat(Definitions.CONFIG_DATE_PATTERN_DATE_TIME_DDMMYYYY);
////                                            Date cellValue = datetemp.parse(temp1.EXPORT_DATE_TYPE);
//                                            
//                                            //1. Create the date cell style
////                                            CreationHelper createHelper = wb.getCreationHelper();
////                                            CellStyle cellStyle = wb.createCellStyle();
////                                            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("MMMM dd, yyyy"));
//                                            Cell cellBranch4 = row1.createCell((short) 4);
//                                            cellBranch4.setCellValue(temp1.CREATED_DT);
//                                            cellBranch4.setCellStyle(my_styleBranch);
//                                            i++;
//                                        }
//                                        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
//                                        wb.write(outByteStream);
//                                        byte[] outArray = outByteStream.toByteArray();
//                                        File someFile = new File(strURLPath);
//                                        FileOutputStream fos = new FileOutputStream(someFile);
//                                        fos.write(outArray);
//                                        fos.flush();
//                                        strView = "0#" + strURLPath + "#" + sFileName;
//                                    } else {
//                                        strView = "2#1";
//                                    }
//                                } else {
//                                    strView = "2#1";
//                                }
//                                //</editor-fold>
//                                break;
//                            }
                            case "exporttokenlist": {
                                //<editor-fold defaultstate="collapsed" desc="exporttokenlist">
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
                                String countList = EscapeUtils.CheckTextNull(request.getParameter("countList"));
//                                String SessRoleID_ID = request.getSession(false).getAttribute("RoleID_ID").toString().trim();
                                String SessAgentID = request.getSession(false).getAttribute("SessAgentID").toString().trim();
                                String SessUserAgentID = request.getSession(false).getAttribute("SessUserAgentID").toString().trim();
                                Config conf = new Config();
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
                                    String sessTreeArrayBranchID = sessionsa.getAttribute("sessTreeArrayBranchIDSystem").toString().trim();
                                    com.S_BO_TOKEN_LIST(EscapeUtils.escapeHtmlSearch(FromDateValid), EscapeUtils.escapeHtmlSearch(ToDateValid),
                                        EscapeUtils.escapeHtmlSearch(TOKEN_ID), EscapeUtils.escapeHtmlSearch(TOKEN_STATE),
                                        EscapeUtils.escapeHtmlSearch(TOKEN_VERSION), EscapeUtils.escapeHtmlSearch(AGENT_ID), loginLanguage,
                                        rsPgin, Definitions.CONFIG_PAGE_SIZE_IPAGNO, Integer.parseInt(countList),
                                        EscapeUtils.escapeHtmlSearch(TAX_CODE), EscapeUtils.escapeHtmlSearch(BUDGET_CODE),
                                        EscapeUtils.escapeHtmlSearch(P_ID), EscapeUtils.escapeHtmlSearch(PASSPORT), EscapeUtils.escapeHtmlSearch(CCCD),
                                        EscapeUtils.escapeHtmlSearch(TOKEN_SIGNED), EscapeUtils.escapeHtmlSearch(PHONE_CONTACT),
                                        EscapeUtils.escapeHtmlSearch(EMAIL_CONTACT), sessTreeArrayBranchID, EscapeUtils.escapeHtmlSearch(DECISION),"","");
                                    if(rsPgin[0].length > 0)
                                    {
                                        String queryString = getServletContext().getRealPath("/");
                                        String outputDirectory = queryString;
                                        String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                        FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                        XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                        inputStream.close();

                                        String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                        String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
                                        String sCellSTATE = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_STATE).trim();
                                        String sCellDateCreate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_DATE_CREATE).trim();
                                        String sCellToken = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_TOKEN_CODE).trim();
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
                                            cellBranch1.setCellStyle(my_styleBranch);

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
                            case "exportprofilecontrollist": {
                                //<editor-fold defaultstate="collapsed" desc="exportprofilecontrollist">
                                Config conf = new Config();
                                String loginLanguage = request.getSession(false).getAttribute("sessVN").toString();
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
                                String strAlertAllTimes = (String) request.getSession(false).getAttribute("AlertAllTimeSRenewCert");
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(idCollectEnabled)) {
                                    idCollectEnabled = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(BranchOffice)) {
                                    BranchOffice = "";
                                }
                                if (Definitions.CONFIG_GRID_COMBOBOX_VALUE_ALL.equals(stateProfile)) {
                                    stateProfile = "";
                                }
//                                CommonFunction clFunction = new CommonFunction();
                                if("1".equals(strAlertAllTimes)) {
                                    ToReceiveDate = "";
                                    FromReceiveDate = "";
                                } else {
//                                    ToReceiveDate = clFunction.formatDateTime(ToReceiveDate, "/", "-");
//                                    FromReceiveDate = clFunction.formatDateTime(FromReceiveDate, "/", "-");
                                }
                                String SessUserID = "";
                                CommonFunction.LogDebugString(log, "ExportReportCertList", "vSum: " + vSum
                                    + "; Month: " + ToCreateDate + "; Year: " + FromCreateDate);
                                CERTIFICATION[][] rsReportBranch = new CERTIFICATION[1][];
                                if(!"".equals(vSum)) {
                                    if(Integer.parseInt(vSum) > 0) {
                                        if("0".equals(ToCreateDate)){ToCreateDate = "";}
                                        com.S_BO_CERTIFICATION_BRIEF_LIST_EXPORT(COMPANY_NAME, PERSONAL_NAME, idCollectEnabled, EscapeUtils.escapeHtmlSearch(ToCreateDate), EscapeUtils.escapeHtmlSearch(FromCreateDate),
                                            BranchOffice, idCheckCompensation, idCheckOverdue, SessUserID, loginLanguage,
                                            rsReportBranch, idCheckCommitEnabled, stateProfile, EscapeUtils.escapeHtmlSearch(FromReceiveDate),
                                            EscapeUtils.escapeHtmlSearch(ToReceiveDate), "", "","","","","","");
                                    }
                                }
                                if (rsReportBranch[0] != null && rsReportBranch[0].length > 0) {
                                    String queryString = getServletContext().getRealPath("/");
                                    String outputDirectory = queryString;
                                    String pathLogoTemplate = outputDirectory + "/Images/TemplateCSV.xlsx";
                                    FileInputStream inputStream = new FileInputStream(pathLogoTemplate);
                                    XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
                                    inputStream.close();
                                    
                                    String sCellSTT = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_QUICK_COLUMN_STT).trim();
                                    String sCellAgency = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_AGENCY).trim();
                                    String sCellUserCreate = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_USER_CREATE).trim();
                                    String sCellMST = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_MST).trim();
                                    String sCellCompany = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_COMPANY).trim();
                                    String sCellCMND = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_CMND).trim();
                                    String sCellPesonal = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PESONAL).trim();
                                    String sCellProfile = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_PROFILE).trim();
                                    String sCellRequestType = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_REQUEST_TYPE).trim();
                                    String sCellStateCert = "Trạng thái CTS";
                                    String sCellCertSN = "Số sê-ri CTS";
                                    String sCellTokenSN = conf.GetPropertybyCode(Definitions.CONFIG_PROCESS_EXPORT_CERTLIST_COLUMN_TOKEN_CODE).trim();
                                    String sCellMonthControl = "Tháng đối soát dịch vụ";
                                    String sCellStateProfile = "Trạng thái hồ sơ";
                                    String sCellRecordProfile = "Hình thức thu hồ sơ";
                                    String sCellTypeProfile = "Loại hồ sơ";
                                    String sCellControlState = "Trạng thái đối soát hồ sơ";
                                    String sCellDateControlProfile = "Tháng đối soát hồ sơ";
                                    String sCellAmountFine = "Số tiền phạt hồ sơ";
                                    String sCellDateReceive = "Ngày nhận hồ sơ";
                                    String sCelCheckRegister = "Đăng ký sử dụng";
                                    String sCelCheckConfirm = "Giấy xác nhận";
                                    String sCelCheckGPKD = "Đăng ký kinh doanh";
                                    String sCelCheckCMND = "CMND";
                                    SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
                                    wb.setCompressTempFiles(true);
                                    CreationHelper createHelper = wb.getCreationHelper();
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### SHEET TONG HOP - HEADER">
                                    SXSSFSheet sheet = (SXSSFSheet) wb.createSheet("TONG HOP");
                                    int m = 0;
                                    Row rowSyntheticTitle;
                                    rowSyntheticTitle = sheet.createRow(m);
                                    CellStyle my_styleSyntheticTitle = wb.createCellStyle();
                                    Font fontBranchSyntheticTitle = wb.createFont();
                                    sheet.addMergedRegion(new CellRangeAddress(m, m, 0, 8));
                                    fontBranchSyntheticTitle.setBoldweight((short) 700);
                                    fontBranchSyntheticTitle.setFontName("Verdana");
                                    my_styleSyntheticTitle.setFont(fontBranchSyntheticTitle);

                                    Cell cellSyntheticTitle;
                                    cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                    cellSyntheticTitle.setCellValue("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM");
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                    m = m+1;
                                    rowSyntheticTitle = sheet.createRow(m);
                                    sheet.addMergedRegion(new CellRangeAddress(m, m, 0, 8));
                                    cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                    cellSyntheticTitle.setCellValue("Độc lập - Tự do - Hạnh phúc");
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                    m = m+1;
                                    rowSyntheticTitle = sheet.createRow(m);
                                    sheet.addMergedRegion(new CellRangeAddress(m, m, 6, 8));
                                    cellSyntheticTitle = rowSyntheticTitle.createCell((short) 5);
                                    cellSyntheticTitle.setCellValue("............, ngày…. Tháng….. Năm 2020");
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                    m = m+1;
                                    rowSyntheticTitle = sheet.createRow(m);
                                    sheet.addMergedRegion(new CellRangeAddress(m, m, 0, 8));
                                    cellSyntheticTitle = rowSyntheticTitle.createCell((short) 0);
                                    cellSyntheticTitle.setCellValue("BIÊN BẢN ĐỐI SOÁT PHÁT TRIỂN CHỨNG THƯ SỐ THÁNG " + ToCreateDate.trim() + "/" + FromCreateDate.trim());
                                    my_styleSyntheticTitle.setAlignment(my_styleSyntheticTitle.ALIGN_CENTER);
                                    cellSyntheticTitle.setCellStyle(my_styleSyntheticTitle);
                                    //</editor-fold>
                                            
                                    //<editor-fold defaultstate="collapsed" desc="### Sheet 2">
                                    SXSSFSheet sheet02 = (SXSSFSheet) wb.createSheet("HỒ SƠ DỐI SOÁT");
                                    sheet02.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                    sheet02.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                    sheet02.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                    sheet02.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                    sheet02.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                    sheet02.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                    sheet02.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                    sheet02.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                    sheet02.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                    sheet02.setColumnWidth(9, 18 * 255);
                                    sheet02.setColumnWidth(10, 18 * 255);
                                    sheet02.setColumnWidth(11, 18 * 255);
                                    sheet02.setColumnWidth(12, 18 * 255);
                                    sheet02.setColumnWidth(13, 18 * 255);
                                    sheet02.setColumnWidth(14, 18 * 255);
                                    sheet02.setColumnWidth(15, 24 * 255);
                                    sheet02.setColumnWidth(16, 18 * 255);
                                    sheet02.setColumnWidth(17, 24 * 255);
                                    sheet02.setColumnWidth(18, 18 * 255);
                                    sheet02.setColumnWidth(19, 18 * 255);
                                    sheet02.setColumnWidth(20, 18 * 255);
                                    sheet02.setColumnWidth(21, 18 * 255);
                                    sheet02.setColumnWidth(22, 18 * 255);
                                    sheet02.setColumnWidth(23, 18 * 255);
                                     Row row = sheet02.createRow(0);

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

                                    Cell cell6 = row.createCell((short) 5);
                                    cell6.setCellValue(sCellCMND);
                                    cell6.setCellStyle(my_style);

                                    Cell cell7 = row.createCell((short) 6);
                                    cell7.setCellValue(sCellPesonal);
                                    cell7.setCellStyle(my_style);

                                    Cell cell9 = row.createCell((short) 7);
                                    cell9.setCellValue(sCellProfile);
                                    cell9.setCellStyle(my_style);

                                    Cell cell10 = row.createCell((short) 8);
                                    cell10.setCellValue(sCellRequestType);
                                    cell10.setCellStyle(my_style);
                                    
                                    Cell cell102 = row.createCell((short) 9);
                                    cell102.setCellValue(sCellStateCert);
                                    cell102.setCellStyle(my_style);

                                    Cell cell11 = row.createCell((short) 10);
                                    cell11.setCellValue(sCellMonthControl);
                                    cell11.setCellStyle(my_style);

                                    Cell cell12 = row.createCell((short) 11);
                                    cell12.setCellValue(sCellStateProfile);
                                    cell12.setCellStyle(my_style);

                                    Cell cell13 = row.createCell((short) 12);
                                    cell13.setCellValue(sCellRecordProfile);
                                    cell13.setCellStyle(my_style);

                                    Cell cell14 = row.createCell((short) 13);
                                    cell14.setCellValue(sCellTypeProfile);
                                    cell14.setCellStyle(my_style);

                                    Cell cell15 = row.createCell((short) 14);
                                    cell15.setCellValue(sCellControlState);
                                    cell15.setCellStyle(my_style);

                                    Cell cell16 = row.createCell((short) 15);
                                    cell16.setCellValue(sCellDateControlProfile);
                                    cell16.setCellStyle(my_style);

                                    Cell cell19 = row.createCell((short) 16);
                                    cell19.setCellValue(sCellAmountFine);
                                    cell19.setCellStyle(my_style);

                                    Cell cell20 = row.createCell((short) 17);
                                    cell20.setCellValue(sCellDateReceive);
                                    cell20.setCellStyle(my_style);
                                    
                                    Cell cell203 = row.createCell((short) 18);
                                    cell203.setCellValue(sCellCertSN);
                                    cell203.setCellStyle(my_style);
                                    
                                    Cell cell212 = row.createCell((short) 19);
                                    cell212.setCellValue(sCellTokenSN);
                                    cell212.setCellStyle(my_style);

                                    Cell cell23 = row.createCell((short) 20);
                                    cell23.setCellValue(sCelCheckRegister);
                                    cell23.setCellStyle(my_style);

                                    Cell cell24 = row.createCell((short) 21);
                                    cell24.setCellValue(sCelCheckConfirm);
                                    cell24.setCellStyle(my_style);

                                    Cell cell25 = row.createCell((short) 22);
                                    cell25.setCellValue(sCelCheckGPKD);
                                    cell25.setCellStyle(my_style);

                                    Cell cell26 = row.createCell((short) 23);
                                    cell26.setCellValue(sCelCheckCMND);
                                    cell26.setCellStyle(my_style);
                                    
                                    int i = 1;
                                    int k = 0;
                                    for (CERTIFICATION rsReportBranch1 : rsReportBranch[0]) {
                                        if(rsReportBranch1.COLLECT_ENABLED == true) {
                                            if (k == 0) {
                                                k = 1;
                                            } else {
                                                k++;
                                            }
                                            Row row1 = sheet02.createRow(Integer.valueOf(i));
                                            CellStyle my_styleBranch = wb.createCellStyle();
                                            Font fontBranch = wb.createFont();
                                            fontBranch.setFontName("Arial");
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
                                            cellBranch1.setCellValue(rsReportBranch1.BRANCH_DESC);
                                            cellBranch1.setCellStyle(my_styleBranch);

                                            Cell cellBranch2 = row1.createCell((short) 2);
                                            cellBranch2.setCellValue(rsReportBranch1.CREATED_BY);
                                            cellBranch2.setCellStyle(my_styleBranch);

                                            String sMSTAndBUDGET_CODE = rsReportBranch1.TAX_CODE;
                                            if ("".equals(sMSTAndBUDGET_CODE)) {
                                                sMSTAndBUDGET_CODE = rsReportBranch1.BUDGET_CODE;
                                            }
                                            if ("".equals(sMSTAndBUDGET_CODE)) {
                                                sMSTAndBUDGET_CODE = rsReportBranch1.DECISION;
                                            }
                                            Cell cellBranch3 = row1.createCell((short) 3);
                                            cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                            cellBranch3.setCellStyle(my_styleBranch);

                                            Cell cellBranch5 = row1.createCell((short) 4);
                                            cellBranch5.setCellValue(rsReportBranch1.COMPANY_NAME);
                                            cellBranch5.setCellStyle(my_styleBranch);

                                            String sP_IDAndPASSPORT = rsReportBranch1.P_ID;
                                            if ("".equals(sP_IDAndPASSPORT)) {
                                                sP_IDAndPASSPORT = rsReportBranch1.P_EID;
                                            }
                                            if ("".equals(sP_IDAndPASSPORT)) {
                                                sP_IDAndPASSPORT = rsReportBranch1.PASSPORT;
                                            }
                                            Cell cellBranch6 = row1.createCell((short) 5);
                                            cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                            cellBranch6.setCellStyle(my_styleBranch);

                                            Cell cellBranch7 = row1.createCell((short) 6);
                                            cellBranch7.setCellValue(rsReportBranch1.PERSONAL_NAME);
                                            cellBranch7.setCellStyle(my_styleBranch);

                                            Cell cellBranch9 = row1.createCell((short) 7);
                                            cellBranch9.setCellValue(rsReportBranch1.CERTIFICATION_PROFILE_NAME);
                                            cellBranch9.setCellStyle(my_styleBranch);

                                            Cell cellBranch10 = row1.createCell((short) 8);
                                            cellBranch10.setCellValue(rsReportBranch1.CERTIFICATION_ATTR_TYPE_DESC);
                                            cellBranch10.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch102 = row1.createCell((short) 9);
                                            cellBranch102.setCellValue(rsReportBranch1.CERTIFICATION_STATE_DESC);
                                            cellBranch102.setCellStyle(my_styleBranch);

                                            Cell cellBranch11 = row1.createCell((short) 10);
                                            cellBranch11.setCellValue(rsReportBranch1.CROSS_CHECKED_MOUNTH);
                                            cellBranch11.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch12 = row1.createCell((short) 11);
                                            cellBranch12.setCellValue(rsReportBranch1.FILE_MANAGER_STATE_DESC);
                                            cellBranch12.setCellStyle(my_styleBranch);

                                            String sRecordProfile = "Hồ sơ trong tháng";
                                            if(rsReportBranch1.BRIEF_TYPE == true) {
                                                sRecordProfile = "Hồ sơ trả bù";
                                            }

                                            Cell cellBranch13 = row1.createCell((short) 12);
                                            cellBranch13.setCellValue(sRecordProfile);
                                            cellBranch13.setCellStyle(my_styleBranch);

                                            String sTypeProfile = "Bản cứng";
                                            if(rsReportBranch1.COLLECT_SOFTCOPY == true) {
                                                sTypeProfile = "Bản mềm";
                                            }

                                            Cell cellBranch14123 = row1.createCell((short) 13);
                                            cellBranch14123.setCellValue(sTypeProfile);
                                            cellBranch14123.setCellStyle(my_styleBranch);

                                            String sCOLLECT_ENABLED = "Đã đối soát hồ sơ";
                                            if(rsReportBranch1.COLLECT_ENABLED == false) {
                                                sCOLLECT_ENABLED = "Chưa đối soát hồ sơ";
                                            }
                                            Cell cellBranch15222 = row1.createCell((short) 14);
                                            cellBranch15222.setCellValue(sCOLLECT_ENABLED);
                                            cellBranch15222.setCellStyle(my_styleBranch);

                                            Cell cellBranch14 = row1.createCell((short) 15);
                                            Date dateControls = CommonFunction.convertStringToDate(rsReportBranch1.COLLECTED_FULL_BRIEF_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                            if(dateControls != null) {
                                                cellBranch14.setCellValue(dateControls);
                                            } else {
                                                cellBranch14.setCellValue("");
                                            }
                                            cellBranch14.setCellStyle(my_styleBranchDate);

                                            Cell cellBranch15 = row1.createCell((short) 16);
                                            cellBranch15.setCellValue(rsReportBranch1.FINE_FOR_LACK_OF_BRIEF);
                                            cellBranch15.setCellStyle(my_styleBranch);

                                            Cell cellBranch16 = row1.createCell((short) 17);
                                            Date dateReceive = CommonFunction.convertStringToDate(rsReportBranch1.RECEIVED_BRIEF_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                            if(dateReceive != null) {
                                                cellBranch16.setCellValue(dateReceive);
                                            } else {
                                                cellBranch16.setCellValue("");
                                            }
                                            cellBranch16.setCellStyle(my_styleBranchDate);

                                            Cell cellBranch1612 = row1.createCell((short) 18);
                                            cellBranch1612.setCellValue(rsReportBranch1.CERTIFICATION_SN);
                                            cellBranch1612.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch1613 = row1.createCell((short) 19);
                                            cellBranch1613.setCellValue(rsReportBranch1.TOKEN_SN);
                                            cellBranch1613.setCellStyle(my_styleBranch);

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
                                            Cell cellBranch21 = row1.createCell((short) 20);
                                            cellBranch21.setCellValue(sRegister);
                                            cellBranch21.setCellStyle(my_styleBranch);

                                            Cell cellBranch22 = row1.createCell((short) 21);
                                            cellBranch22.setCellValue(sConfirm);
                                            cellBranch22.setCellStyle(my_styleBranch);

                                            Cell cellBranch23 = row1.createCell((short) 22);
                                            cellBranch23.setCellValue(sDKKD);
                                            cellBranch23.setCellStyle(my_styleBranch);

                                            Cell cellBranch24 = row1.createCell((short) 23);
                                            cellBranch24.setCellValue(sCMND);
                                            cellBranch24.setCellStyle(my_styleBranch);
                                            i++;
                                        }
                                    }
                                    //</editor-fold>
                                    
                                    //<editor-fold defaultstate="collapsed" desc="### Sheet 3">
                                    SXSSFSheet sheet03 = (SXSSFSheet) wb.createSheet("HỒ SƠ THIẾU TRONG THÁNG");
                                    sheet03.setColumnWidth(0, Definitions.CONFIG_EXPORT_QUICK_WIDTH_STT);
                                    sheet03.setColumnWidth(1, Definitions.CONFIG_EXPORT_QUICK_WIDTH_AGENCY_CODE);
                                    sheet03.setColumnWidth(2, Definitions.CONFIG_EXPORT_QUICK_WIDTH_USER_CREATE);
                                    sheet03.setColumnWidth(3, Definitions.CONFIG_EXPORT_QUICK_WIDTH_MST);
                                    sheet03.setColumnWidth(4, Definitions.CONFIG_EXPORT_QUICK_WIDTH_COMPANY);
                                    sheet03.setColumnWidth(5, Definitions.CONFIG_EXPORT_QUICK_WIDTH_CMND);
                                    sheet03.setColumnWidth(6, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PESONAL);
                                    sheet03.setColumnWidth(7, Definitions.CONFIG_EXPORT_QUICK_WIDTH_PROFILE);
                                    sheet03.setColumnWidth(8, Definitions.CONFIG_EXPORT_QUICK_WIDTH_REQUEST_TYPE);
                                    sheet03.setColumnWidth(9, 18 * 255);
                                    sheet03.setColumnWidth(10, 18 * 255);
                                    sheet03.setColumnWidth(11, 18 * 255);
                                    sheet03.setColumnWidth(12, 18 * 255);
                                    sheet03.setColumnWidth(13, 18 * 255);
                                    sheet03.setColumnWidth(14, 18 * 255);
                                    sheet03.setColumnWidth(15, 24 * 255);
                                    sheet03.setColumnWidth(16, 18 * 255);
                                    sheet03.setColumnWidth(17, 18 * 255);
                                    sheet03.setColumnWidth(18, 18 * 255);
                                    sheet03.setColumnWidth(19, 18 * 255);
                                    sheet03.setColumnWidth(20, 18 * 255);
                                    sheet03.setColumnWidth(21, 18 * 255);
                                    Row row3 = sheet03.createRow(0);

                                    CellStyle my_style3 = wb.createCellStyle();
                                    Font font3 = wb.createFont();
                                    font3.setBoldweight((short) 700);
                                    font3.setFontName("Arial");
                                    my_style3.setFont(font3);
                                    my_style3.setFillBackgroundColor((short) 9);
                                    my_style3.setAlignment((short) 2);
                                    Cell cell33 = row3.createCell((short) 0);
                                    cell33.setCellValue(sCellSTT);
                                    cell33.setCellStyle(my_style3);

                                    Cell cell133 = row3.createCell((short) 1);
                                    cell133.setCellValue(sCellAgency);
                                    cell133.setCellStyle(my_style3);

                                    Cell cell233 = row3.createCell((short) 2);
                                    cell233.setCellValue(sCellUserCreate);
                                    cell233.setCellStyle(my_style3);

                                    Cell cell333 = row3.createCell((short) 3);
                                    cell333.setCellValue(sCellMST);
                                    cell333.setCellStyle(my_style3);

                                    Cell cell53 = row3.createCell((short) 4);
                                    cell53.setCellValue(sCellCompany);
                                    cell53.setCellStyle(my_style3);

                                    Cell cell63 = row3.createCell((short) 5);
                                    cell63.setCellValue(sCellCMND);
                                    cell63.setCellStyle(my_style3);

                                    Cell cell73 = row3.createCell((short) 6);
                                    cell73.setCellValue(sCellPesonal);
                                    cell73.setCellStyle(my_style3);

                                    Cell cell93 = row3.createCell((short) 7);
                                    cell93.setCellValue(sCellProfile);
                                    cell93.setCellStyle(my_style3);

                                    Cell cell103 = row3.createCell((short) 8);
                                    cell103.setCellValue(sCellRequestType);
                                    cell103.setCellStyle(my_style3);
                                    
                                    Cell cell1032 = row3.createCell((short) 9);
                                    cell1032.setCellValue(sCellStateCert);
                                    cell1032.setCellStyle(my_style3);

                                    Cell cell113 = row3.createCell((short) 10);
                                    cell113.setCellValue(sCellMonthControl);
                                    cell113.setCellStyle(my_style3);

                                    Cell cell123 = row3.createCell((short) 11);
                                    cell123.setCellValue(sCellStateProfile);
                                    cell123.setCellStyle(my_style3);

                                    Cell cell143 = row3.createCell((short) 12);
                                    cell143.setCellValue(sCellTypeProfile);
                                    cell143.setCellStyle(my_style3);

                                    Cell cell153 = row3.createCell((short) 13);
                                    cell153.setCellValue(sCellControlState);
                                    cell153.setCellStyle(my_style3);

                                    Cell cell193 = row3.createCell((short) 14);
                                    cell193.setCellValue(sCellAmountFine);
                                    cell193.setCellStyle(my_style3);
                                    
                                    Cell cell1931 = row3.createCell((short) 15);
                                    cell1931.setCellValue(sCellDateReceive);
                                    cell1931.setCellStyle(my_style3);
                                    
                                    Cell cell1924 = row3.createCell((short) 16);
                                    cell1924.setCellValue(sCellCertSN);
                                    cell1924.setCellStyle(my_style3);
                                    
                                    Cell cell1921 = row3.createCell((short) 17);
                                    cell1921.setCellValue(sCellTokenSN);
                                    cell1921.setCellStyle(my_style3);

                                    Cell cell2331 = row3.createCell((short) 18);
                                    cell2331.setCellValue(sCelCheckRegister);
                                    cell2331.setCellStyle(my_style3);

                                    Cell cell243 = row3.createCell((short) 19);
                                    cell243.setCellValue(sCelCheckConfirm);
                                    cell243.setCellStyle(my_style3);

                                    Cell cell253 = row3.createCell((short) 20);
                                    cell253.setCellValue(sCelCheckGPKD);
                                    cell253.setCellStyle(my_style3);

                                    Cell cell263 = row3.createCell((short) 21);
                                    cell263.setCellValue(sCelCheckCMND);
                                    cell263.setCellStyle(my_style3);
                                    
                                    int ii = 1;
                                    int kk = 0;
                                    for (CERTIFICATION rsReportBranch1 : rsReportBranch[0]) {
                                        if(rsReportBranch1.COLLECT_ENABLED == false) {
                                            if (kk == 0) {
                                                kk = 1;
                                            } else {
                                                kk++;
                                            }
                                            Row row11 = sheet03.createRow(Integer.valueOf(ii));
                                            CellStyle my_styleBranch = wb.createCellStyle();
                                            Font fontBranch = wb.createFont();
                                            fontBranch.setFontName("Arial");
                                            my_styleBranch.setFont(fontBranch);
                                            
                                            CellStyle my_styleBranchDate = wb.createCellStyle();
                                            Font fontBranchDate = wb.createFont();
                                            fontBranchDate.setFontName("Arial");
                                            my_styleBranchDate.setFont(fontBranchDate);
                                            my_styleBranchDate.setDataFormat(createHelper.createDataFormat().getFormat(Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS));
                                            
                                            Cell cellBranch = row11.createCell((short) 0);
                                            cellBranch.setCellValue(kk);
                                            cellBranch.setCellStyle(my_styleBranch);

                                            Cell cellBranch1 = row11.createCell((short) 1);
                                            cellBranch1.setCellValue(rsReportBranch1.BRANCH_DESC);
                                            cellBranch1.setCellStyle(my_styleBranch);

                                            Cell cellBranch2 = row11.createCell((short) 2);
                                            cellBranch2.setCellValue(rsReportBranch1.CREATED_BY);
                                            cellBranch2.setCellStyle(my_styleBranch);

                                            String sMSTAndBUDGET_CODE = rsReportBranch1.TAX_CODE;
                                            if ("".equals(sMSTAndBUDGET_CODE)) {
                                                sMSTAndBUDGET_CODE = rsReportBranch1.BUDGET_CODE;
                                            }
                                            if ("".equals(sMSTAndBUDGET_CODE)) {
                                                sMSTAndBUDGET_CODE = rsReportBranch1.DECISION;
                                            }
                                            Cell cellBranch3 = row11.createCell((short) 3);
                                            cellBranch3.setCellValue(sMSTAndBUDGET_CODE);
                                            cellBranch3.setCellStyle(my_styleBranch);

                                            Cell cellBranch5 = row11.createCell((short) 4);
                                            cellBranch5.setCellValue(rsReportBranch1.COMPANY_NAME);
                                            cellBranch5.setCellStyle(my_styleBranch);

                                            String sP_IDAndPASSPORT = rsReportBranch1.P_ID;
                                            if ("".equals(sP_IDAndPASSPORT)) {
                                                sP_IDAndPASSPORT = rsReportBranch1.P_EID;
                                            }
                                            if ("".equals(sP_IDAndPASSPORT)) {
                                                sP_IDAndPASSPORT = rsReportBranch1.PASSPORT;
                                            }
                                            Cell cellBranch6 = row11.createCell((short) 5);
                                            cellBranch6.setCellValue(sP_IDAndPASSPORT);
                                            cellBranch6.setCellStyle(my_styleBranch);

                                            Cell cellBranch7 = row11.createCell((short) 6);
                                            cellBranch7.setCellValue(rsReportBranch1.PERSONAL_NAME);
                                            cellBranch7.setCellStyle(my_styleBranch);

                                            Cell cellBranch9 = row11.createCell((short) 7);
                                            cellBranch9.setCellValue(rsReportBranch1.CERTIFICATION_PROFILE_NAME);
                                            cellBranch9.setCellStyle(my_styleBranch);

                                            Cell cellBranch10 = row11.createCell((short) 8);
                                            cellBranch10.setCellValue(rsReportBranch1.CERTIFICATION_ATTR_TYPE_DESC);
                                            cellBranch10.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch101 = row11.createCell((short) 9);
                                            cellBranch101.setCellValue(rsReportBranch1.CERTIFICATION_STATE_DESC);
                                            cellBranch101.setCellStyle(my_styleBranch);

                                            Cell cellBranch11 = row11.createCell((short) 10);
                                            cellBranch11.setCellValue(rsReportBranch1.CROSS_CHECKED_MOUNTH);
                                            cellBranch11.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch12 = row11.createCell((short) 11);
                                            cellBranch12.setCellValue(rsReportBranch1.FILE_MANAGER_STATE_DESC);
                                            cellBranch12.setCellStyle(my_styleBranch);

                                            String sTypeProfile = "Bản cứng";
                                            if(rsReportBranch1.COLLECT_SOFTCOPY == true) {
                                                sTypeProfile = "Bản mềm";
                                            }

                                            Cell cellBranch14123 = row11.createCell((short) 12);
                                            cellBranch14123.setCellValue(sTypeProfile);
                                            cellBranch14123.setCellStyle(my_styleBranch);

                                            String sCOLLECT_ENABLED = "Đã đối soát hồ sơ";
                                            if(rsReportBranch1.COLLECT_ENABLED == false) {
                                                sCOLLECT_ENABLED = "Chưa đối soát hồ sơ";
                                            }
                                            Cell cellBranch15222 = row11.createCell((short) 13);
                                            cellBranch15222.setCellValue(sCOLLECT_ENABLED);
                                            cellBranch15222.setCellStyle(my_styleBranch);

                                            Cell cellBranch15 = row11.createCell((short) 14);
                                            cellBranch15.setCellValue(rsReportBranch1.FINE_FOR_LACK_OF_BRIEF);
                                            cellBranch15.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch1512 = row11.createCell((short) 15);
                                            Date dateReceive = CommonFunction.convertStringToDate(rsReportBranch1.RECEIVED_BRIEF_DT, Definitions.CONFIG_DATETIME_FORMAT_DDMMYYYYHHMMSS);
                                            if(dateReceive != null) {
                                                cellBranch1512.setCellValue(dateReceive);
                                            } else {
                                                cellBranch1512.setCellValue("");
                                            }
                                            cellBranch1512.setCellStyle(my_styleBranchDate);
                                            
                                            Cell cellBranch1414 = row11.createCell((short) 16);
                                            cellBranch1414.setCellValue(rsReportBranch1.CERTIFICATION_SN);
                                            cellBranch1414.setCellStyle(my_styleBranch);
                                            
                                            Cell cellBranch1412 = row11.createCell((short) 17);
                                            cellBranch1412.setCellValue(rsReportBranch1.TOKEN_SN);
                                            cellBranch1412.setCellStyle(my_styleBranch);

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
                                            Cell cellBranch21 = row11.createCell((short) 18);
                                            cellBranch21.setCellValue(sRegister);
                                            cellBranch21.setCellStyle(my_styleBranch);

                                            Cell cellBranch22 = row11.createCell((short) 19);
                                            cellBranch22.setCellValue(sConfirm);
                                            cellBranch22.setCellStyle(my_styleBranch);

                                            Cell cellBranch23 = row11.createCell((short) 20);
                                            cellBranch23.setCellValue(sDKKD);
                                            cellBranch23.setCellStyle(my_styleBranch);

                                            Cell cellBranch24 = row11.createCell((short) 21);
                                            cellBranch24.setCellValue(sCMND);
                                            cellBranch24.setCellStyle(my_styleBranch);
                                            ii++;
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
